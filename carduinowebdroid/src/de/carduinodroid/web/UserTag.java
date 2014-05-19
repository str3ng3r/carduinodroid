package de.carduinodroid.web;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspWriter;
import de.carduinodroid.utilities.DBConnector;

import de.carduinodroid.shared.User;

/**
 * \brief This class handles the User related custom tags.
 * \details The User tag returns User data (Nickname,Rights,UserCount).
 * @author Christoph Braun
 *
 */
public class UserTag extends TagSupport{

	private static final long serialVersionUID = 1L;
	private int userNr;
	private int param;
	
	public void setNum(int i) {
		userNr = i;
	}

	public void setPar(int o) {
		param = o;
	}
	
	/**
	 * \brief Starting tag handler
	 * \details This tag has 3 Functions.
	 * 1. Get how many Users are registered.
	 * 2. Get the Nickname of a specific User.
	 * 3. Check if the User is an admin.
	 * @return EVAL_PAGE. Tells the Server to continue evaluating Tags.
	 * @throws Exception Throws a default Jsp Exception with full stacktrace.
	 */
	public int doStartTag() throws JspException{
		
		String Name;
		boolean isAdmin;
		DBConnector db = (DBConnector)pageContext.getServletContext().getAttribute("database");
		JspWriter out = pageContext.getOut();
		List<User> UserList = new ArrayList<User>();
		UserList = db.getAllUser(true);
		
		switch(param) {
		case 0:
			try {
				out.print(UserList.size());
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		case 1:	
			try {
				Name = UserList.get(userNr).getNickname();			
				out.print(Name);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;			
		case 2:
			try {
				isAdmin = UserList.get(userNr).isAdmin();
				out.print(isAdmin);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		case 3:
			try {
				Name = UserList.get(userNr).getUserID();
				out.print(Name);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		
		default:
			throw new JspException("Error: invalid parameter in UserTag");
		}
		return EVAL_PAGE;
		
		
	}

	public int doEndTag() throws JspException{
		return EVAL_PAGE;
	}
	
}
