<!DOCTYPE HTML> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title>CarDuinoDroid</title>
<link rel="icon" href="static/favicon.ico" type="image/x-icon">

<!--JQuery Import-->
<link rel="stylesheet" href="static/jqueryui_static/css/ui-lightness/jquery-ui-1.10.3.custom.css">
<script type="text/javascript" src="static/jquery-1.9.1.js"></script>
<script type="text/javascript" src="static/jquery-ui.js"></script>
<script type="text/javascript" src="static/counter.js"></script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/lib/tags/customTag.tld" prefix="ct" %>

<!--Custom Stylesheet-->

<link rel="stylesheet" href="static/style.css">

<!--Custom jS-->

<script type="text/javascript" src="static/main.js"></script>
<script type="text/javascript" src="static/websocket.js"></script>
<script type="text/javascript" src="static/chat.js"></script>
<script type="text/javascript" src="static/controls.js"></script>
<script type="text/javascript" src="static/ajax.js"></script>
<script type="text/javascript" src="static/stream.js"></script>

<!-- initializing Websockets -->

<script type="text/javascript">
	$(function() {
		initializeWebsocket();
		window.setInterval(heartbeat,1000);
		$("#main_controls").hide();
	});
</script>

<!-- site-related jS -->

<script type="text/javascript">
/*	Zeichenzähler	*/
$(document).ready(function(){
	$("#main_chat_textinput").jqEasyCounter();
});
</script>
</head>
<body>
<table id="main_table">
	<tr>
    	<td id="main_table_sidebar_left">
        	<div id="main_chat_container">
        		<textarea id="main_chat" readonly rows="0" cols="0"></textarea>
          	  	<textarea name="chat" id="main_chat_textinput" placeholder="Chat here..." rows="0" cols="0"></textarea>		
            	<button id="main_chat_speak">Chat</button>
          	</div>
        </td>
        <td><button id="main_close_left" class="ui-icon ui-icon-triangle-1-w"></button> <button id="main_open_left" class="ui-icon ui-icon-triangle-1-e"></button></td>
        <td id="main_table_stream">   
        	<div id="main_stream">
        	 <img id="image" />
    		</div>
        	<div id="main_controls">
            	<table id="main_control_table">
            		<tr>
            			<td class="main_slider">
                			<div class="slider-vertical" id="main_maxspeed"></div>
                			Max. Speed
                		</td>
                		<td class="main_slider">
                			<div class="slider-vertical" id="main_steerangle"></div>
                			Steering Angle
                		</td>
                        <td id="main_steering">
                            <table id="main_steering_table">
                                <tr>
                                    <td>&nbsp;
                                        
                                    </td>                                    
                                    <td>
                                        <button value="Up" type="button" id="main_steering_button_up">Up</button>
                                    </td>
                                    <td>&nbsp;
                                        
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <button value="Left" type="button" id="main_steering_button_left">Left</button>
                                    </td>
                                    <td>
                                        <button value="Down" type="button" id="main_steering_button_down">Down</button>
                                    </td>
                                    <td>
                                        <button value="Right" type="button" id="main_steering_button_right">Right</button>
                                    </td>
                                </tr>
                            </table>
                        </td>
                        <td id="main_gadget">
                            <button value="Horn [H]" type="button" id="main_gadget_button_horn">Horn [H]</button><br>
                            <button value="Light [L]" type="button" id="main_gadget_button_light">Light [L]</button>
                        </td>
                	</tr>
            	</table>
            </div>
            
        </td>
        <td><button id="main_close_right" class="ui-icon ui-icon-triangle-1-e"></button> <button id="main_open_right" class="ui-icon ui-icon-triangle-1-w"></button></td>
        <td id="main_table_sidebar_right">
        	<div id="main_q_container">	
               	<div id="main_q">Loading Queue</div>
               		<div id="main_button_container">
               		<table>
               			<c:set var="isUser"><ct:chkRight par="1" /></c:set>
               			<c:if test="${isUser == 1}">
               			<tr>
               				<td id="main_qsubmitleft">
				                <form method="POST" action="">
				                	<div>
				                    	<input id="main_qsubmit" type="submit" value="[En/de]queue">
				                    </div>
				                </form>
			                </td>
			                <td>
			                	<form method="post" action="">
			                		<div>
				                    	<input id="main_stopdriving" type="submit" value="Stop driving">
			                    	</div>
			                	</form>
			                </td>
						</tr>
						</c:if>
			            <tr>
			                <td>		                	
			                	<c:set var="isAdmin"><ct:chkRight par="0" /></c:set>
			              		<c:if test="${isAdmin == 1}">
			                	<form method="POST" action="">
			                		<div>
									<input type="hidden" name="action" value="connect">
									<input id="main_connect" type="submit" value="Connect to Car">
									</div>
								</form>
								</c:if>
							</td>
							<td>
								
								<c:set var="isAdmin"><ct:chkRight par="0" /></c:set>
			              		<c:if test="${isAdmin == 1}">
								<form method="POST" action="">
									<div>
									<input type="hidden" name="action" value="admincontrol">
									<input id="main_take_control" type="submit" value="Take Control">
									</div>
								</form>
								</c:if>
							</td>			                
			            </tr>
			            <tr> 			            	
			            	<td>	
			            		<c:set var="isAdmin"><ct:chkRight par="0" /></c:set>
			              		<c:if test="${isAdmin == 1}">		            	
			                	<a href="admin.jsp?menu=1">
			                		<button id="main_admin">Admin</button>
			                	</a>
			                	</c:if>
			                </td>
			                  
			            	<td>
				                <form method="POST" action="">
				                	<div>
					                	<input type="hidden" name="action" value="logout">
					                	<input id="main_logout" type="submit" value="Logout">
				                	</div>
								</form>
							</td>							
						</tr>		                
	                </table>
	                </div>
            </div> 
        </td>
    </tr>
</table>
</body>
</html>
