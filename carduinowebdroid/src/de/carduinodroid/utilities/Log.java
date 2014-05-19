package de.carduinodroid.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import de.carduinodroid.shared.GPS;
import de.carduinodroid.utilities.Config.Options;

/**
 * \brief Log class for logging everything.
 * \details This class logs everything to a file or to the database.
 * The log is instanced before everything else. Therefore it has no options in the beginning and doesn't know where to save the log file. 
 * The solution to this is to save everything in a list and copy the list to the log file as soon as the options are set. 
 * @author Michael Röding
 */
public class Log {
	Options options = null;
	DBConnector db;
	LinkedList<String> tmpLog;
	
	BufferedWriter writer;
	SimpleDateFormat dateformat;
	Date date;
	File file;
	File path;
	
	public Log() {
		file = null;
		tmpLog = new LinkedList<String>();
	}
	
	/**
	 * \brief Logs chat to database or file depending on settings.
	 * @param userID The UserID of the user who said that.
	 * @param sessionID	The associated SessionID.
	 * @param text The actual chat text (max length is 256!).
	 * @return Returns "true" if successful or "false" if an error occurs. 
	 */
	public boolean logChat(String userID, int sessionID, String text) {
		if(options == null)
			throw new NullPointerException();
		
		if(!options.logChat)
			return true;
		
		if(options.logChatToFile)
		{
			// TODO - nicht unsere Aufgabe
			throw new UnsupportedOperationException("Not implemented yet");
		}
		else
			return db.logChat(userID, sessionID, text);
	}
	
	/**
	 * \brief Logs GPS coordinates to database or file depending on settings.
	 * @param driveID DriveID to search GPS coordinates to. 
	 * @param longitude Longitude
	 * @param latitude Latitude
	 * @return Returns "true" if successful or "false" if an error occurs. 
	 */
	public boolean logGPS(int driveID, String longitude, String latitude) {
		if(options == null)
			throw new NullPointerException();
		
		if(!options.logGPS)
			return true;
		
		if(options.logGPSToFile)
		{
			// TODO - nicht unsere Aufgabe
			throw new UnsupportedOperationException("Not implemented yet");
		}
		else
			return db.logGPS(driveID, longitude, latitude);
	}
	
	/**
	 * \brief Logs GPS coordinates to database or file depending on settings
	 * @param gps GPS coordinates to log.
	 * @return Returns "true" if successful or "false" if an error occurs. 
	 */
	public boolean logGPS(GPS gps) {
		return (logGPS(gps.getDriveID(), gps.getLongitude(), gps.getLatitude()));
	}
	
	/**
	 * \brief Logs enqueue event to database or file depending on settings
	 * @param userID UserID from the user who is enqueued. 
	 * @param sessionID SessionID from the current session. 
	 * @return QueueID assigned by the database. 
	 */
	public int logQueue(String userID, int sessionID) {
		if(options == null)
			throw new NullPointerException();
		
		int queueID = -1;
		if(!options.logQueue)
			return queueID;
		
		if(options.logQueueToFile)
		{
			// TODO - nicht unsere Aufgabe
			throw new UnsupportedOperationException("Not implemented yet");
		}
		else
			return db.logQueue(userID, sessionID);
	}

	/**
	 * \brief General purpose log function.
	 * \details The current timestamp will be added last.
	 * @param string Text to save to the log.
	 */
	public void writelogfile(String string) {
		if(file == null) {
			tmpLog.addLast(string);
			writelogfile_second(string, false, true);
		}
		else
			writelogfile_second(string, false, false);
	}
	
	/**
	 * \brief Writes the given text to log file and to System.out.
	 * \details Optional you can disable writing to file or System.out if you only want one action to take place.
	 * This is used when options are not yet sent and the log file is unknown or the other way round when the temporary list log is written to the log file.
	 * @param line Text to log.
	 * @param skipLiveLog Set to "true" if the text shouldn't printed to System.out.
	 * @param skipFile Set to "true" if the text shouldn't be written to the log file.
	 */
	private void writelogfile_second(String line, boolean skipLiveLog, boolean skipFile){
		try {
			Date data = new Date();
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss" );
			String entry = df.format(data)+" "+line;
			if(!skipFile) {
				writer.write(entry,0,entry.length());
				writer.write(System.getProperty("line.separator"));
				writer.flush();
			}
			if(!skipLiveLog) write_Live_Log(entry);
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * \brief Prints a given text to System.out.
	 * @param msg Text to send to System.out
	 */
	private void write_Live_Log(String msg) {
		System.out.println(msg);
	}

	/**
	 * \brief Sets the options.
	 * @param options The options to set.
	 */
	public void setOptions(Options options) {		
		this.options = options;
		initFile();
		addTmpLog();
	}
	
	/**
	 * \brief Creates the file to write logs to.
	 */
	private void initFile() {
		Date date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat( "yyyy_MM_dd_HH_mm_ss" );
		String logfile = "Log_"+dateformat.format(date)+".txt";
		
		File path = new File(options.filePath + "/logs");
		path.mkdirs();
		file = new File(path.getAbsolutePath(), logfile);

		try {
			file.createNewFile();
		} catch (IOException e) { e.printStackTrace(); }
		
		file.canWrite();
		file.canRead();
		
		try {
			writer = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * \brief Moves the temporary list log to the log file
	 */
	private void addTmpLog() {
		if(tmpLog == null) return;
		
		for(String s : tmpLog)
			writelogfile_second(s, true, false);
		
		tmpLog = null;
	}
	
	/**
	 * \brief Sets the database to use for logging.
	 * @param database The database to set.
	 */
	public void setDB(DBConnector database) {
		this.db = database;
	}
	
	public LinkedList<String> getLog() {
		LinkedList<String> l = new LinkedList<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String zeile = null;
			while ((zeile = in.readLine()) != null) {
				l.add(zeile);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l;
	}
}
