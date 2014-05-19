package de.carduinodroid;

import java.io.IOException;
import java.nio.CharBuffer;
import de.carduinodroid.shared.*;
import de.carduinodroid.utilities.*;
import de.carduinodroid.utilities.Config.Options;
import javax.servlet.http.HttpSession;
import java.util.TimerTask;
import java.util.Timer;

/**
 * \brief This Class is used to handle user-timeouts and to log GPS in a given interval and to handle the waiting queue
 * @author Alexander Rose
 *
 */

public class QueueManager {
	private static Timer caretaker;
	private static Timer GPSLog;
	private static TimerTask GPSLogger;
	private static TimerTask Session;
	private static TimerTask action;
	private static boolean aliveSessions;
	private static long start = 0;
	static Log log;
	static int driveID;
	static int Fahrzeit, gpsLogInterval;
	static Options opt;
	static boolean flag;
	static HttpSession aktSession;
    
	final static boolean DEBUG = false;
	
	/** 
	 * \brief refreshes the options needed in this class (Fahrzeit and GPS interval)
	 * @param opt Options object
	 */
	
	public static void refresh(Options opt) {

		if (!(activeSession.getDriver() == null) && !(Fahrzeit == opt.driveTime)){
			long driveTime = System.currentTimeMillis() - start;
			long remainingTime = (opt.driveTime*60000) - driveTime;
			if (remainingTime <= 100){
				driveTime = opt.driveTime;
				restartTimer();
			}
			else{
				caretaker.cancel();
				caretaker = new Timer();
				caretaker.schedule(new de.carduinodroid.Dummy(action), remainingTime, 60000*opt.driveTime);
			}
		}
		Fahrzeit = opt.driveTime;

    	gpsLogInterval = opt.logGPSInterval;
    	flag = true;
    }
	
	/** 
	 * \brief deletes all Timers and TimerTasks and also deletes all active sessions
	 */
	
	public static void shutDown() {
		if(DEBUG) System.out.println("Shut-Down main");
		Session.cancel();
		action.cancel();
		GPSLogger.cancel();
		GPSLog.cancel();
		Session.cancel();
		activeSession.deleteAll();
	}
    
	/** 
	 * \brief cancels the Timer for waitingqueue handling and restarts the Timer again
	 */
	
	public static void restartTimer() {
    	caretaker.cancel();
    	caretaker = new Timer();
    	caretaker.schedule(new de.carduinodroid.Dummy(action), 2000, 60000*Fahrzeit);	
    	aliveSessions = false;
    }
	
	/** 
	 * \brief if this method is called, it protects the driver from timeout
	 */
	
	public static void receivedPing(HttpSession Session){
		if (activeSession.isDriver(Session)){
			aliveSessions = false;
		}
	}
    
	/** 
	 * \brief calculates the remaining driveTime for the current driver or 0 if there is no driver
	 */
	
	public static long getRemainingTime(){
    	long aktTime = System.currentTimeMillis();
    	long remainingTime = start+60000*Fahrzeit - aktTime;
    	if (remainingTime < 0 || activeSession.getDriver() == null){
    		return 0;
    	}
    	return remainingTime;
    }
	
	/** 
	 * \brief handles the waiting queue and logs GPS in the given interval
	 * @param opt Options object
	 * @param db DBConnector object
	 * @param logng Log object
	 */
	
	public static void main(Options opt, DBConnector db, Log logng) {
    	
    	log = logng;
    	aliveSessions = false;
    	
    	Session = new TimerTask(){
			public void run(){
				
				if (aliveSessions){
					if (DEBUG) System.out.println("TIMEOUT");
					activeSession.deleteSession(activeSession.getDriver());
				}
				if (activeSession.getDriver() != null){
					aliveSessions = true;		
				}
			}
		};
    	
		Timer Sessionhandle = new Timer();
		Sessionhandle.schedule(Session, 10, 5000);

		Fahrzeit = opt.driveTime;
		gpsLogInterval = opt.logGPSInterval;
		if(DEBUG) System.out.println("Main-function");
		
		try {
			action = new TimerTask() {
				DBConnector db = new DBConnector();
				public void run() {					
					if(flag){
			    		caretaker.cancel();
						caretaker = new Timer();
			    		caretaker.schedule(new de.carduinodroid.Dummy(action), 60000*Fahrzeit, 60000*Fahrzeit);
			    		flag = false;
			    	}
					
			    	activeSession.resetDriver();
			    	
					if (waitingqueue.isEmpty() == true){
						caretaker.cancel();
						caretaker = new Timer();
						caretaker.schedule(new de.carduinodroid.Dummy(action), 1000, 60000*Fahrzeit);				
						return;
					}
					else{
						try {
							if(aktSession != null && activeSession.getSocket(aktSession) != null) 
								activeSession.getSocket(aktSession).getWsOutbound().writeTextMessage(CharBuffer.wrap(MyWebSocketServlet.identifierControl + "n"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							aktSession = waitingqueue.getNextUser();
							aliveSessions = false;
							start = System.currentTimeMillis();
							driveID = db.startDrive(db.getUserIdBySession((int)aktSession.getAttribute("DBID")));
							activeSession.setDriver(aktSession, driveID);
							activeSession.getSocket(aktSession).getWsOutbound().writeTextMessage(CharBuffer.wrap(MyWebSocketServlet.identifierControl + "y"));
							///TODO \todo Fahrrechte;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						}
					}
			    
			};
		} catch (Exception e) {
			log.writelogfile(e.getMessage());
			e.printStackTrace();
		}
	
		
		
		caretaker = new Timer();
		caretaker.schedule(action, 100, 60000*Fahrzeit);  
		
		try {
			GPSLogger = new TimerTask() {		
				public void run(){
					String longitude = CarControllerWrapper.getLongitude();
					String latitude = CarControllerWrapper.getLatitude();
					if (longitude == null || latitude == null){
						if(DEBUG) System.out.println("GPS: N/A");
					} else
						log.logGPS(driveID, longitude, latitude);
				}
			};
		} catch (Exception e) {
			log.writelogfile(e.getMessage());
			e.printStackTrace();
		}
		GPSLog = new Timer();
		GPSLog.schedule(GPSLogger, 10, gpsLogInterval * 1000);
    }   
    
}
