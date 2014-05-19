package de.carduinodroid.web;

import java.io.IOException;
import java.util.LinkedList;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import de.carduinodroid.utilities.Log;

/**
 * \brief This class handles the Log related custom tags.
 * \details The Log tag returns all logs.
 * @author Michael Röding
 *
 */

public class LogTag extends TagSupport {
	
	private static final long serialVersionUID = 1L;

	public int doStartTag() throws JspException {
		
		Log log = (Log)pageContext.getServletContext().getAttribute("log");
		JspWriter out = pageContext.getOut();
		LinkedList<String> logList = new LinkedList<String>();
		logList = log.getLog();
		
		try {
			for(String s : logList)	
				out.print(s+",");
		} catch (IOException e) { 
			throw new JspException("Error: " + e.getMessage()); 
		}
		return EVAL_PAGE;
	}
	
		
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
}
