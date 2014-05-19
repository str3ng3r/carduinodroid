package de.carduinodroid.shared;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class GPS {

	private Timestamp datetime;
	private int driverID;
	private String longitude;
	private String latitude;
	
	public GPS(int driveID, String longi, String lati, Timestamp time){
		datetime = time;
		driverID = driveID;
		longitude = longi;
		latitude = lati;
	}

	public String getLongitude(){
		return longitude;
	}
	
	public String getLatitude(){
		return latitude;
	}
	
	public int getDriveID(){
		return driverID;
	}

	public Timestamp getDateTime(){
		return datetime;
	}
	
	public String getDateTimeFormated() {
		return new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(datetime);
	}
}
