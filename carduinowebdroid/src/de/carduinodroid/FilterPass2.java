package de.carduinodroid;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WsOutbound;

import de.carduinodroid.shared.User;
import de.carduinodroid.shared.activeSession;
import de.carduinodroid.shared.waitingqueue;
import de.carduinodroid.shared.User.Right;
import de.carduinodroid.utilities.CarControllerWrapper;
import de.carduinodroid.utilities.DBConnector;
import de.carduinodroid.utilities.Config.Options;
import de.carduinodroid.utilities.Log;
import de.carduinodroid.utilities.Config;

/**
 * \brief This Class is used to handle the different Servlet-Requests
 * @author Alexander Rose
 * @author Michael Röding
 *
 */

@WebServlet(loadOnStartup=1, value = "/filterPass2")
public class FilterPass2  extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static boolean DEBUG = false;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FilterPass2() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /*
     * what is get and what is post?
     * 
     * get:
     * 
     * post:
     * 	- login
     * 	- enqueue
     * 	- dequeue
     * 	- watchDriver
     * 	-
     * 
     * NO REDIRECT FROM HERE!
     */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// nothing to do here
		if(DEBUG) {
			Map<String, String[]> getParameterMap = request.getParameterMap();		
			Iterator<Entry<String, String[]>> entries = getParameterMap.entrySet().iterator();
			while (entries.hasNext()) {
			    Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) entries.next();
			    String key = (String)entry.getKey();
			    String[] value = (String[])entry.getValue();
			    System.out.println("Key = " + key + ", Value = " + value[0]);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(DEBUG) System.out.println("doPost");
		
		Log log = (Log)request.getServletContext().getAttribute("log");
		Config conf = (Config)request.getServletContext().getAttribute("config");

		if(request instanceof HttpServletRequest) {			
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession();			

			Map<String, String[]> postParameterMap = req.getParameterMap();
			if(DEBUG) {
				Iterator<Entry<String, String[]>> entries = postParameterMap.entrySet().iterator();
				while (entries.hasNext()) {
				    Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) entries.next();
				    String key = (String)entry.getKey();
				    String[] value = (String[])entry.getValue();
				    System.out.println("Key = " + key + ", Value = " + value[0]);
				}
			}
			
			if(postParameterMap.size() > 0 && postParameterMap.containsKey("action")) {
				String ipAdress = req.getRemoteAddr();
				DBConnector db = (DBConnector)request.getServletContext().getAttribute("database");
				
				switch((String)postParameterMap.get("action")[0])  {				
				
				/** 
				 * \brief User tries to login with his User-account
				 */
				
				case "login":
					if(!postParameterMap.containsKey("loginName") || !postParameterMap.containsKey("password"))
						break;
					
					String userID, pw;
					userID = (String)postParameterMap.get("loginName")[0];
					pw = (String)postParameterMap.get("password")[0];
					
					HttpSession[] active = new HttpSession[activeSession.getLength()];
					active = activeSession.getAllSessions();
					for (int i = 0; i < active.length; i++){
						User online = db.getUserBySession((int)active[i].getAttribute("DBID"));
						if (online.getUserID().equals(userID)){
							log.writelogfile("User '" + userID + "' got kicked, cause the same user logged in again");
							User u = db.loginUser(userID, pw);
							
							if(u == null)
								break;
							activeSession.deleteSession(active[i]);
							
							int ID = activeSession.insertSession(ipAdress, userID, session);
							if (ID == -1)
								break;
							
							session.setAttribute("isAdmin", u.isAdmin());
							session.setAttribute("isUser", u.isUser() || u.isAdmin());
							session.setAttribute("nickName", u.getNickname());
							session.setAttribute("userId", u.getUserID());
							session.setAttribute("dbSessionID", ID);
							if(DEBUG) System.out.println("user " + u.getNickname() + " has logged in");
							return;
						}
					}
					User u = db.loginUser(userID, pw);
					
					if(u == null)
						break;
					
					int ID = activeSession.insertSession(ipAdress, userID, session);
					if (ID == -1)
						break;
					session.setAttribute("isAdmin", u.isAdmin());
					session.setAttribute("isUser", u.isUser() || u.isAdmin());
					session.setAttribute("nickName", u.getNickname());
					session.setAttribute("userId", u.getUserID());
					session.setAttribute("dbSessionID", ID);
					
					if(DEBUG) System.out.println("user " + u.getNickname() + " has logged in");
					break;
				
					/** 
					 * \brief Someone tries to enqueue or dequeue from the waitingqueue
					 */
					
				case "toggleq":					
					if (activeSession.isActive(session) == false){
						session.removeAttribute("nickName");
						break;
					}
					User user = db.getUserBySession((int)session.getAttribute("DBID"));
					if (user == null){
						if(DEBUG) System.out.println("User nicht gefunden");
						break;
					}
					if (user.isGuest() == true) return;
					waitingqueue.insertUser(session);
					log.logQueue(user.getUserID(), (int)session.getAttribute("DBID"));
					break;
					
					/** 
					 * \brief Guest-Login without an account
					 */
					
				case "watchDriver":
					userID = "gue" + System.currentTimeMillis();
					db.loginGuest(userID);
					ID = activeSession.insertSession(ipAdress, userID, session);
					if (ID == -1){
						log.writelogfile("error creating session for guest " + userID);
						break;
					}
					session.setAttribute("isAdmin", false);
					session.setAttribute("isUser", false);
					session.setAttribute("nickName", userID);
					session.setAttribute("userId", userID);
					session.setAttribute("dbSessionID", ID);
					break;
				
					/** 
					 * \brief logout from the Server and return to the landing-page
					 */
					
				case "logout":
					activeSession.deleteSession(session);
					waitingqueue.deleteTicket(session);
					break;
				
					/** 
					 * \brief connects the Server to the carduinodroid
					 */
					
				case "connect":
					if (activeSession.isActive(session) == false){
						session.removeAttribute("nickName");
						break;
					}
					String ip = ((Options)request.getServletContext().getAttribute("options")).carduinodroidIP;
					if(ip == null || ip == "") {
						log.writelogfile("unable to connect to carduinodroid because ip is not set");
						break;
					}
					try {
						CarControllerWrapper ccw = CarControllerWrapper.getCarController();
						ccw.connect(ip);
					} catch (Exception e) {
						log.writelogfile(e.getMessage());
					}
					break;
				
					/** 
					 * \brief edits an user (userid, nickname, rights) or deletes an user
					 */
				
				case "edituser":
					if (session.getAttribute("isAdmin").equals(false)){
						if(DEBUG) System.out.println("Admin-Rechte werden für diese Operation benötigt");
						break;
					}
					if (!(postParameterMap.containsKey("userid")) || !(postParameterMap.containsKey("password"))){
						if(DEBUG) System.out.println("Feld unvollständig");
						break;
					}
					boolean isAdmin;
					userID = (String) postParameterMap.get("userid")[0]; 
					
					if (postParameterMap.containsKey("chkdel1") && postParameterMap.containsKey("chkdel2") && postParameterMap.containsKey("chkdel3") && !(userID.equals(db.getUserIdBySession((int)session.getAttribute("DBID"))))){
						int DBID = db.getSessionIDByUserID(userID);
							
						if (!(DBID == -1)){							
							HttpSession deleted = activeSession.getSession(DBID);
							activeSession.deleteSession(deleted);
						}
						db.deleteUser(userID);
						break;
					}
					String Password = (String) postParameterMap.get("password")[0];
					
					if (Password != null && Password.equals("") == false){
						db.changePassword(userID, Password);
					}
					if (!postParameterMap.containsKey("nickname")){
						if(DEBUG) System.out.println("Feld zum editieren von Usern unvollständig");
						break;
					}
					String Nickname = (String) postParameterMap.get("nickname")[0];
					if (postParameterMap.containsKey("rights") == false){
						isAdmin = false;
					}
					else{
						isAdmin = true;
					}
					if (userID.equals(db.getUserIdBySession((int)session.getAttribute("DBID"))) && (!isAdmin)){
						if(DEBUG) System.out.println("Man kann sich nicht selbst das Admin recht entziehen");
						break;
					}
					if (isAdmin){
						db.editUser(userID, Nickname, Right.ADMIN);
					}
					else{
						db.editUser(userID, Nickname, Right.USER);
					}
					break;
				
					/** 
					 * \brief creates a new user
					 */
					
				case "adduser":
					if (session.getAttribute("isAdmin").equals(false)){
						if(DEBUG) System.out.println("Admin-Rechte werden für diese Operation benötigt");
						break;
					}
					
					if (!(postParameterMap.containsKey("userid")) || !(postParameterMap.containsKey("nickname")) || !(postParameterMap.containsKey("password"))){
						if(DEBUG) System.out.println("Feld unvollständig");
						break;
					}
					
					userID = (String) postParameterMap.get("userid")[0]; 
					Nickname = (String) postParameterMap.get("nickname")[0];
					if (postParameterMap.containsKey("rights")){
						isAdmin = true;
					}
					else{
						isAdmin = false;
					}
					String password = (String) postParameterMap.get("password")[0];
					
					if (isAdmin){
						db.createUser(userID, Nickname, password, Right.ADMIN);
					}
					else{
						db.createUser(userID, Nickname, password, Right.USER);
					}
					break;
				
					/** 
					 * \brief saves a new config for the Server
					 */
					
				case "saveconfig":
					if (session.getAttribute("isAdmin").equals(false)){
						if(DEBUG) System.out.println("Admin-Rechte werden für diese Operation benötigt");
						break;
					}
					
					if (false // better readable
						|| !postParameterMap.containsKey("IP") 
						|| !postParameterMap.containsKey("DBAdress") 
						|| !postParameterMap.containsKey("DBUser")
						|| !postParameterMap.containsKey("DBPw") 
						|| !postParameterMap.containsKey("drivetime") 
						|| !postParameterMap.containsKey("filepath")
						|| !postParameterMap.containsKey("loggpsint")){
						if(DEBUG) System.out.println("Feld unvollständig");
						break;
					}
					
					Options opt = conf.getOptions();
					opt.carduinodroidIP = (String) postParameterMap.get("IP")[0];
					opt.dbAddress = (String) postParameterMap.get("DBAdress")[0];
					opt.dbPW = (String) postParameterMap.get("DBPw")[0];
					opt.dbUser = (String) postParameterMap.get("DBUser")[0];
					opt.driveTime = Integer.parseInt(postParameterMap.get("drivetime")[0]);
					opt.filePath = (String) postParameterMap.get("filepath")[0];
					opt.logGPSInterval = Integer.parseInt( postParameterMap.get("loggpsint")[0]);
					if (postParameterMap.containsKey("logchat")){
						opt.logChat = true;
					}
					else{
						opt.logChat = false;
					}
					if (postParameterMap.containsKey("logchattofile")){
						opt.logChatToFile = true;
					}
					else{
						opt.logChatToFile = false;
					}
					if (postParameterMap.containsKey("loggpstofile")){
						opt.logGPSToFile = true;
					}
					else{
						opt.logGPSToFile = false;
					}
					if (postParameterMap.containsKey("logq")){
						opt.logQueue = true;
					}
					else{
						opt.logQueue = false;
					}
					if (postParameterMap.containsKey("logqtofile")){
						opt.logQueueToFile = true;
					}
					else{
						opt.logQueueToFile = false;
					}
					conf.setOptions(opt);
					conf.saveOptions();
					break;
				
					/** 
					 * \brief an Admin can kick out a driver and become driver immediately
					 */
				
				case "admincontrol":
					if (activeSession.isActive(session) == false){
						session.removeAttribute("nickName");
						break;
					}
					
					if (session.getAttribute("isAdmin").equals(false)){
						if(DEBUG) System.out.println("Admin-Rechte werden für diese Operation benötigt");
						break;
					}
					
					waitingqueue.InsertFirst(session);
					activeSession.resetDriver();
					QueueManager.restartTimer();
					break;
				
					/** 
					 * \brief a driver can decide to end his drive before his driving-time ends
					 */
				
				case "stopdriving":
					if (session.equals(activeSession.getDriver())){
						activeSession.resetDriver();
						QueueManager.restartTimer();
					}
					else{
						break;
					}
					break;
				default:
					log.writelogfile("FilterPass2: unknown action " + (String)postParameterMap.get("action")[0]);
					log.writelogfile("FilterPass2: key-values pairs are:");
					Iterator<Entry<String, String[]>> entries = postParameterMap.entrySet().iterator();
					while (entries.hasNext()) {
					    Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) entries.next();
					    String key = (String)entry.getKey();
					    String[] value = (String[])entry.getValue();
					    log.writelogfile("Key = " + key + ", Value = " + value[0]);
					}
					break;
				}
			}
		}
	}

}
