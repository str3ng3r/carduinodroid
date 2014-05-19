package de.carduinodroid.web;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import de.carduinodroid.QueueManager;

/**
 * \brief This class handles the Time related custom tags.
 * \details The Time tag returns remaining drive time.
 * @author Christoph Braun
 *
 */

public class TimeTag extends TagSupport{
	
	private static final long serialVersionUID = 1L;
	private long remTime;
	private long remTimeMin;
	private long remTimeSec;

	/**
	 * \brief Returns remaining drive time in Minutes:Seconds format
	 */
	public int doStartTag() throws JspException {
		remTime = QueueManager.getRemainingTime();
		JspWriter out = pageContext.getOut();
		remTime /= 1000;
		remTimeMin = (long)Math.floor(remTime / 60);
		remTimeSec = remTime - (remTimeMin * 60);
		try {
			out.print(remTimeMin + ":" + remTimeSec);
		} catch(IOException e){
			throw new JspException("Error: " + e.getMessage());

		}

		return EVAL_PAGE;
	}
	
	public int doEndTag() throws JspException {
		
		return EVAL_PAGE;
	}

}
