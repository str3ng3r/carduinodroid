package de.carduinodroid.utilities;

import java.io.*;
import java.util.*;

import de.carduinodroid.QueueManager;

/**
 * \brief Config class managing options.
 * \details This class proved methods to load and save options.
 * The options will be saved in config.properties and can be edited with a normal text editor.
 * \details To change the options call setOptions(Options options) followed by saveOptions()
 * @author Michael Röding
 *
 */
public class Config {
	/**
	 * \brief Actual options
	 * \details This class contains all available options.
	 * @author Michael Röding
	 *
	 */
	public class Options {
		public int driveTime; /** in minutes */
		public int maxDriveSpeed; /** 0-100 */
		public String dbAddress; /** Internet address of the database server */
		public String dbUser; /** database username */
		public String dbPW; /** database password */
		public String filePath; /**  file path which should be used as root folder for everything else (like log and options) */
		public boolean logChat; /** should the chat be logged */
		public boolean logChatToFile; /** should the chat be logged to file */
		public boolean logGPS; /** should gps be logged */
		public boolean logGPSToFile; /** should gps be logged to file */
		public int logGPSInterval; /** in seconds */
		public boolean logQueue; /** should queue events be logged */
		public boolean logQueueToFile; /** should queue events be logged to file */
		public String carduinodroidIP; /** IP address of the carduinodroid */
	}
	
	private Log log;
	private Options options;
	private String filePath;
	private String optionsPath;
	
	/**
	 * \brief Sets log, file path and creates the options file if necessary.
	 * @param logIN Log to use for logging
	 * @param filePath_in File path which should be used as root folder for everything else
	 */
	public Config(Log logIN, String filePath_in) {
		this.log = logIN;
		filePath = filePath_in;
		optionsPath = filePath + "/config/config.properties";
		options = new Options();
		
		File folder = new File(filePath + "/config/");
		if(!folder.exists()) {
			logIN.writelogfile("creating folder");
			folder.mkdir();
		}
		
		File file = new File(optionsPath);
		if(!file.exists())
			try {
				logIN.writelogfile("creating empty settings file");
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * \brief Loads options from file
	 */
	public void readOptions() {
		try{
			Properties p = new Properties();			
			p.load(new FileInputStream(optionsPath));
			
			log.writelogfile("loading settings");
		
			Enumeration<Object> keys = p.keys();
			while (keys.hasMoreElements()) {
				String key = (String)keys.nextElement();
				String value = (String)p.get(key);
				log.writelogfile(key + " = " + value);
			}
			
			options.driveTime = Integer.parseInt(p.getProperty("driveTime"));
			options.maxDriveSpeed = Integer.parseInt(p.getProperty("maxDriveSpeed"));	
			options.dbAddress = p.getProperty("dbAddress");
			options.dbUser = p.getProperty("dbUser");
			options.dbPW = p.getProperty("dbPW");
			options.filePath = p.getProperty("filePath");
			options.logChat = Boolean.valueOf(p.getProperty("logChat"));
			options.logChatToFile = Boolean.valueOf(p.getProperty("logChatToFile"));
			options.logGPS = Boolean.valueOf(p.getProperty("logGPS"));
			options.logGPSToFile = Boolean.valueOf(p.getProperty("logGPSToFile"));
			options.logGPSInterval = Integer.parseInt(p.getProperty("logGPSInterval"));
			options.logQueue = Boolean.valueOf(p.getProperty("logQueue"));
			options.logQueueToFile = Boolean.valueOf(p.getProperty("logQueueToFile"));
			options.carduinodroidIP = p.getProperty("carduinodroidIP");
			
			log.writelogfile("settings loaded");
		}
		catch (Exception e) {
			  System.out.println(e);
		}
	}
	
	/**
	 * \brief Saves options to file
	 */
	public void saveOptions() {
		try{
			log.writelogfile("saving settings");
			Properties p = new Properties();
			p.setProperty("driveTime", String.valueOf(options.driveTime));
			p.setProperty("maxDriveSpeed", String.valueOf(options.maxDriveSpeed));
			p.setProperty("dbAddress", options.dbAddress);
			p.setProperty("dbUser", options.dbUser);
			p.setProperty("dbPW", options.dbPW);
			p.setProperty("filePath", filePath);
			p.setProperty("logChat", String.valueOf(options.logChat));
			p.setProperty("logChatToFile", String.valueOf(options.logChatToFile));
			p.setProperty("logGPS", String.valueOf(options.logGPS));
			p.setProperty("logGPSToFile", String.valueOf(options.logGPSToFile));
			p.setProperty("logGPSInterval", String.valueOf(options.logGPSInterval));
			p.setProperty("logQueue", String.valueOf(options.logQueue));
			p.setProperty("logQueueToFile", String.valueOf(options.logQueueToFile));
			p.setProperty("carduinodroidIP", options.carduinodroidIP);
			p.store(new FileOutputStream(optionsPath), null);
			
			Enumeration<Object> keys = p.keys();
			while (keys.hasMoreElements()) {
				String key = (String)keys.nextElement();
				String value = (String)p.get(key);
				log.writelogfile(key + " = " + value);
			}
			
			log.writelogfile("settings saved");
		}
		catch (Exception e) {
			  System.out.println(e);
		}
	}
	
	/**
	 * \brief Creates default options. 
	 * @param localhost ///TODO \todo: remove this switch
	 */
	public void setDefault(boolean localhost) {    	
    	
    	options = new Options();
    	options.driveTime = 10;
    	options.maxDriveSpeed = 50;
    	options.logGPSInterval = 5;
    	if(localhost) {
    		options.dbAddress = "localhost:3306/carduinodroid";
    		options.dbUser = "root";
    		options.dbPW = "test";
    	} else {
    		options.dbAddress = "sehraf-pi:3306/carduinodroid";
    		options.dbUser = "test";
    		options.dbPW = "test";
    	}
    	options.filePath = filePath;
		options.logChat = true;
		options.logChatToFile = false;
		options.logGPS = true;
		options.logGPSToFile = false;
		options.logQueue = true;
		options.logQueueToFile = false;
		options.carduinodroidIP = "";
    	saveOptions();
    	QueueManager.refresh(options);
	}

	/**
	 * \brief Returns the options.
	 * @return Returns the options.
	 */
	public Options getOptions() {
		return options;
	}
	
	/**
	 * \brief Set options.
	 * @param options The options to set.
	 */
	public void setOptions(Options options) {
		this.options = options;
		QueueManager.refresh(options);
	}
	
	
}
