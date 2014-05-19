package de.carduinodroid.web;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import de.carduinodroid.utilities.Config.Options;

/**
 * \brief This class handles the Config related custom tags.
 * \details The User tag returns all config settings.
 * @author Christoph Braun
 *
 */

public class ConfigTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	private int parameter;
	
	public void setPar(int i) {
		parameter = i;
	}
	
	public int doStartTag() throws JspException {
		Options option = (Options)pageContext.getServletContext().getAttribute("options");
		JspWriter out = pageContext.getOut();
		
		switch(parameter){
		case 0: 
			try {
				out.print(option.carduinodroidIP);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			} 
			break;
		case 1:
			try {
				out.print(option.dbAddress);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		case 2:
			try {
				out.print(option.dbPW);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		case 3:
			try {
				out.print(option.dbUser);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		case 4:
			try {
				out.print(option.driveTime);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		case 5:
			try {
				out.print(option.filePath);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		case 6:
			try {
				out.print(option.logChat);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		case 7:
			try {
				out.print(option.logChatToFile);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		case 8:
			try {
				out.print(option.logGPS);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		case 9:
			try {
				out.print(option.logGPSInterval);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		case 10:
			try {
				out.print(option.logGPSToFile);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		case 11:
			try {
				out.print(option.logQueue);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		case 12:
			try {
				out.print(option.logQueueToFile);
			} catch (IOException e) {
				throw new JspException("Error: " + e.getMessage());
			}
			break;
		}
						
		return EVAL_PAGE;
	}
	
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

}
