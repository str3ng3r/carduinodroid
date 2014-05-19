package de.carduinodroid.desktop.Controller;

/**
 * Methods receive the data strings from the Android and send them to the GUI.
 * @version 18.06.2012
 * @author Christian Schulze
 */


public class Packagedata {
			
	Controller_Computer controller_computer;
	String [] tokens;
	
	public Packagedata(Controller_Computer ControllerComputer){
		controller_computer = ControllerComputer;
	}
	
	/**
	 * This method gets the data- String and split it into an array.
	 * Then the informationbox will be updated.
	 * @param data is the received String from the Android.
	 */
	
	public void receive_package(String data) 
	{
		tokens = data.split(";",-1);
		for (int i = 0; i < tokens.length; i++) tokens[i] = tokens[i].trim(); //Leerzeichen weg vorn und hinten
		if (tokens[0].equals("1"))
			updateInformationbox();
		if (tokens[0].equals("2"))
			updateItemIndex();
	}
	
	/**
	 * This method send the separate information to the GUI. 
	 */
	
	// TODO: brauchen wir das?
	private void updateInformationbox()
	{
		double doublelong= ((double)Math.round(Double.parseDouble(tokens[5]) * 1000000)) / 1000000;
		double doublelat= ((double)Math.round(Double.parseDouble(tokens[6]) * 1000000)) / 1000000;
		String Long = String.valueOf(doublelong);
		String Lat = String.valueOf(doublelat);
		
		if(!(Long.equals("0.0")&Lat.equals("0.0")))
		{	
			controller_computer.parent.setLongitude(String.valueOf(tokens[5]));
			controller_computer.parent.setLatitude(String.valueOf(tokens[6]));
//			controller_computer.gpstrack.writegpxfile(String.valueOf(tokens[5]), String.valueOf(tokens[6]));
		}else
		{
//			controller_computer.gui_computer.longitude.setText("N/A");
//			controller_computer.gui_computer.latitude.setText("N/A");
			controller_computer.parent.setLongitude(null);
			controller_computer.parent.setLatitude(null);
		}
				
//		if (tokens[4].equals("1"))
//			controller_computer.gui_computer.connection_type.setText("WLAN");
//		if (tokens[3].equals("1"))
//			controller_computer.gui_computer.connection_type.setText("Mobile Internet");

		//MobileAvailable tokens[1]; 
		//WLANAvailable tokens[2];
		//Mobile tokens[3];
		//WLAN tokens[4];
	}
	
	private void updateItemIndex()
	{
		int itemslength = tokens.length;
		String[] resolution = new String[itemslength-1];
		
		for (int i = 0; i<itemslength-1; i++)
		{
			resolution[itemslength-2-i] = tokens[i+1];
		}
		
		controller_computer.parent.setResolutions(resolution);
	}
	
}
	

