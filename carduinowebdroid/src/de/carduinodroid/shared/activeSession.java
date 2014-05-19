package de.carduinodroid.shared;

import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WsOutbound;
import java.io.IOException;
import java.nio.CharBuffer;
import de.carduinodroid.QueueManager;
import de.carduinodroid.utilities.CarControllerWrapper;
import de.carduinodroid.utilities.DBConnector;

/**
 * \brief This Class is used to store all active sessions and provides access to them
 * @author Alexander Rose
 *
 */

public class activeSession {

	private static ArrayList<HttpSession> activeTomcat;
	private static int Driver;
	private static int DriverID;
	private static CharBuffer msg;
	static DBConnector db;
	final static boolean DEBUG =false;
	
	/** 
	 * \brief initializes the activeSession queue
	 */
	
	public static void init(){
		activeTomcat = new ArrayList<HttpSession>();
		db = null;
		Driver = -1;
		DriverID = -1;
		try {
			db = new DBConnector();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
	
	/** 
	 * \brief inserts a session into the queue and also creates a session in the DB
	 * @param ipadress of the user (ipv4 and ipv6 are possible)
	 * @param userid of the user
	 * @param HttpSession Tomcat session of the user
	 */
	
	public static int insertSession(String ipadress,String userid, HttpSession Tomcat){
		
		if (activeTomcat.contains(Tomcat)){
			if (DEBUG) System.out.println("bereits verbunden");
			return -1;
		}
		int ID = -1;		
		ID = db.createSession(userid, ipadress);
		if (ID == -1){
			if (DEBUG) System.out.println("konnte Session nicht erstellen");
			return -1;
		}
		Tomcat.setAttribute("DBID", ID);
		Tomcat.setAttribute("Socket", null);
		activeTomcat.add(Tomcat);
		
		return ID;
	}
	
	/** 
	 * @return Returns all SessionID's in the queue
	 */
	
	public static HttpSession[] getAllSessions(){
		HttpSession[] AllSessions = new HttpSession[activeTomcat.size()];
		for(int i = 0; i < AllSessions.length; i++){
			AllSessions[i] = activeTomcat.get(i);
		}
		return AllSessions;
	}
	
	/** 
	 * \brief removes a session from the queue and closes the DB Session. If the Session belongs to a Driver the car is stopped
	 * @param Session of the user (Tomcat)
	 */
	
	public static void deleteSession(HttpSession Session){
		int index = activeTomcat.indexOf(Session);
		if (index == -1){
			if (DEBUG) System.out.println("Session bereits gelöscht");
			return;
		}
		
		try{
			Session.removeAttribute("nickName");
			StreamInbound in = (StreamInbound)Session.getAttribute("Socket");
			WsOutbound out = in.getWsOutbound();
			try {		
				out.writeTextMessage(CharBuffer.wrap("invalid"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			db.closeSession((int)Session.getAttribute("DBID"));
			Session.removeAttribute("DBID");
			Session.removeAttribute("Socket");
			activeTomcat.remove(index);
			Session.invalidate();
		
		}
		catch(Exception ie){
			if (Session.getAttribute("DBID") != null){
				db.closeSession((int)Session.getAttribute("DBID"));
			}
			activeTomcat.remove(index);
		}
		
		if (index < Driver){
			Driver = Driver -1;
		}
		else{
			if (index == Driver){
				db.stopDrive(DriverID);
				Driver = -1;
				QueueManager.restartTimer();
				CarControllerWrapper.setDown(false);
				CarControllerWrapper.setLeft(false);
				CarControllerWrapper.setRight(false);
				CarControllerWrapper.setUp(false);
			}
		}			
	}
	
	/** 
	 * \brief removes all sessions from the queue and closes all DB sessions
	 */
	
	public static void deleteAll(){
		for (int i = 0; i < activeTomcat.size(); i++){
			HttpSession Session = activeTomcat.get(i);
			try{
				Session.removeAttribute("nickName");
				StreamInbound in = (StreamInbound)Session.getAttribute("Socket");
				WsOutbound out = in.getWsOutbound();
				try {
					out.writeTextMessage(CharBuffer.wrap("invalid"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Session.removeAttribute("Socket");
				Session.removeAttribute("DBID");
				Session.invalidate();
			}
			catch(Exception ie){
				System.out.println("Session bereits teilweise oder ganz entfernt");
			}
		}
		db.closeAllOpenSessions();
		activeTomcat.clear();
		Driver = -1;
	}

	/** 
	 * \brief returns the Tomcat Session which belongs to the given Tomcat SessionID
	 * @param SessionID from Tomcat
	 * @return Tomcat Session
	 */
	
	public static HttpSession getSession(String SessionID){
		int index = -1;
		for (int i = 0; i < activeTomcat.size(); i++){
			if (activeTomcat.get(i).getId().equals(SessionID)){
				index = i;
				break;
			}
		}
		if (index == -1){
			return null;
		}
		HttpSession Session = activeTomcat.get(index);
		return Session;
	}

	/** 
	 * \brief returns the Tomcat Session which belongs to the given DB SessionID
	 * @param SessionID from the DB
	 * @return Tomcat Session
	 */
	
	public static HttpSession getSession(int SessionID){
		int index = -1;
		for (int i = 0; i < activeTomcat.size(); i++){
			if ((int)activeTomcat.get(i).getAttribute("DBID") == (SessionID)){
				index = i;
				break;
			}
		}
		if (index == -1){
			return null;
		}
		HttpSession Session = activeTomcat.get(index);
		return Session;
	}
	/** 
	 * \brief saves a Socket for a specific User
	 * @param Session of the User (Tomcat)
	 * @param sock Socket
	 */
	
	public static void insertSocket(HttpSession Session, StreamInbound sock){
		int index = activeTomcat.indexOf(Session);
		if (index == -1){
			return;
		}
		Session.setAttribute("Socket", sock);
	}

	/** 
	 * \brief delete a Socket from a specific User
	 * @param Session of the User (Tomcat)
	 */
	
	public static void deleteSocket(HttpSession Session){
		int index = activeTomcat.indexOf(Session);
		if (index == -1){
			if (DEBUG) System.out.println("Zugehörige Session bereits gelöscht");
			return;
		}
		Session.removeAttribute("Socket");
	}

	/** 
	 * \brief find out if the Session belongs to the current Driver
	 * @param Session of the User (Tomcat)
	 * @return true if its the Driver else false
	 */
	
	public static boolean isDriver(HttpSession Session){
		int index = activeTomcat.indexOf(Session);
		if (index == -1){
			return false;
		}
		boolean isDriver = (index == Driver);
		return isDriver;
	}

	/** 
	 * \brief mark the given User as Driver (if someone else was Diver before, he is no longer the Driver)
	 * @param Session of the User (Tomcat)
	 * @param driverID the ID from the DB given for the actual drive
	 */
	
	public static void setDriver(HttpSession Session, int driverID){
		if (!(Driver == -1)){
			db.stopDrive(DriverID);
		}
		int index = activeTomcat.indexOf(Session);
		Driver = index;
		DriverID = driverID;
	}

	/** 
	 * \brief nobody is Driver after u call this function
	 */
	
	public static void resetDriver(){
		if (!(Driver == -1)){
			db.stopDrive(DriverID);
		}
		Driver = -1;
		DriverID = -1;
	}

	/** 
	 * \brief returns true if the activeSessions-queue contains the given Session
	 * @param Session of the User (Tomcat)
	 */
	
	public static boolean isActive(HttpSession Session){
		return activeTomcat.contains(Session);
	}

	/** 
	 * \brief returns the Socket which belongs to the given Tomcat SessionID
	 * @param Session from Tomcat
	 * @return Socket
	 */
	
	public static StreamInbound getSocket(HttpSession Session){
		int index = activeTomcat.indexOf(Session);
		if (index == -1){
			return null;
		}
		return (StreamInbound)Session.getAttribute("Socket");
	}

	/** 
	 * \brief returns the Tomcat Session which belongs to the current Driver
	 * @return Tomcat Session
	 */
	
	public static HttpSession getDriver(){
		if (Driver == -1) return null;
		return activeTomcat.get(Driver);
	}

	/** 
	 * \brief returns the Number of Sessions, that are active
	 */
	
	public static int getLength(){
		return activeTomcat.size();
	}
}
