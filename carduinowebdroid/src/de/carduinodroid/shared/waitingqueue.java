package de.carduinodroid.shared;

import java.util.LinkedList;
import de.carduinodroid.utilities.DBConnector;
import javax.servlet.http.HttpSession;

/**
 * \brief This Class is used to queue all users who want to get driver-rights
 * @author Alexander Rose
 *
 */

public class waitingqueue {

	private static LinkedList<HttpSession> Warteschlange = null;
	static DBConnector db;
	static final boolean DEBUG = false;
	
	/** 
	 * \brief creates the internal waitingqueue and receives Connection to DB
	 * @param db2 DBConnector to our DB
	 */
	
	public static void initqueue(DBConnector db2){
		if (Warteschlange == null){
			Warteschlange = new LinkedList<HttpSession>();
			db = db2;
		}
		else{
			
		}
	}
	
	/** 
	 * \brief returns the waiting queue
	 * @return returns a LinkedList of HttpSessions
	 */
	
	public static LinkedList<HttpSession> getqueue(){
		return Warteschlange;
	}
	
	/** 
	 * \brief returns the first User from the Queue and removes the User from the Queue
	 * @return returns the Tomcat Session from the first User as HttpSession
	 */
	
	public static HttpSession getNextUser(){
		return Warteschlange.removeFirst();
	}
	
	/** 
	 * \brief inserts a new User at the end of the waiting queue
	 * @param Session from the User (Tomcat)
	 */
	
	public static void insertUser(HttpSession Session){
		if (Warteschlange.contains(Session) || activeSession.isDriver(Session)){
			if (DEBUG) System.out.println("Wurde bereits eingereiht");
			if (Warteschlange.contains(Session)){
				Warteschlange.remove(Session);
			}
			return;
		}
		Warteschlange.add(Session);
		if (DEBUG) System.out.println("User in Warteschlange eingereiht");
	}
	
	/** 
	 * @return returns the Length of the waiting queue
	 */
	
	public static int getLength(){
		return Warteschlange.size();
	}

	/** 
	 * @return Returns true if waiting queue is empty and false if its not empty
	 */
	
	public static boolean isEmpty(){
		return Warteschlange.isEmpty();
	}
	
	/** 
	 * \brief removes a User from the waiting queue
	 * @param Session of the User (Tomcat)
	 */
	
	public static void deleteTicket(HttpSession Session){
		int index = Warteschlange.indexOf(Session);
		if (index == -1){
			if (DEBUG) System.out.println("Wurde bereits gelöscht");
			return;
		}
		Warteschlange.remove(index);
	}
	
	/** 
	 * \brief inserts a User at the first position of the waiting queue
	 * @param Session of the User (Tomcat)
	 */
	
	public static void InsertFirst(HttpSession Session){
		if (Warteschlange.contains(Session)){
			int index = Warteschlange.indexOf(Session);
			Warteschlange.remove(index);
		}
		Warteschlange.addFirst(Session);
	}
	
	/** 
	 * @return Returns the nicknames of all users in the waiting queue in the right order
	 */
	
	public static String[] getNickname(){
		String[] Nickname = new String[Warteschlange.size()];
		for(int i = 0;i < Nickname.length; i++){			
			User user = db.getUserBySession((int)(Warteschlange.get(i).getAttribute("DBID")));
			Nickname[i] = user.getNickname();
		}
		
		return Nickname;
	}
	
	/** 
	 * \brief returns the waiting queue
	 * @return returns an Array of HttpSessions
	 */
	
	public static HttpSession[] getAllSessions(){
		HttpSession[] Liste = new  HttpSession [Warteschlange.size()];
		for (int i = 0;i < Warteschlange.size();i++){
			Liste[i] = Warteschlange.get(i);
		}
		return Liste;
	}
}
