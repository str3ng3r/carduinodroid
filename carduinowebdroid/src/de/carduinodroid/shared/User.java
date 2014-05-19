package de.carduinodroid.shared;

public class User {
	public enum Right {
		GUEST,
		USER,
		ADMIN;
	}	
	
	private String userID; 		// login name 
	private String nickname;	// anzeige name
	private Right right = Right.GUEST;
	
	public User() {	}
	
	public User (String id, Right r) {
		userID = id;
		right = r;
	}

	public User (String id, String nick, Right r) {
		userID = id;
		nickname = nick;
		right = r;
	}

	public Right getRight() { return right; }
	public boolean isAdmin() { return right == Right.ADMIN; }
	public boolean isUser() { return right == Right.USER; }
	public boolean isGuest() { return right == Right.GUEST; }
//	public void setRight(Right r) { right = r; }
	
	/**
	 * @return the userID
	 */
	public String getUserID() {
		return userID;
	}
	/**
	 * @return the userNickname
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * @param userNickname the userNickname to set
	 */
	public void setNickname(String userNickname) {
		this.nickname = userNickname;
	}	

}
