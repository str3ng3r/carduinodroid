<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>CarDuionoDroid Admin</title>

<link rel="icon" href="static/favicon.ico" type="image/x-icon">
<!--JQuery Import-->
<link rel="stylesheet" href="static/jqueryui_static/css/ui-lightness/jquery-ui-1.10.3.custom.css">
<script type="text/javascript" src="static/jquery-1.9.1.js"></script>
<script type="text/javascript" src="static/jquery-ui.js"></script>
<script type="text/javascript" src="static/admin.js"></script>
<script type="text/javascript" src="static/jquery.dataTables.js"></script>

 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <%@ taglib uri="/WEB-INF/lib/tags/customTag.tld" prefix="ct" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!--Custom Stylesheet-->

<link rel="stylesheet" href="static/style.css">
<link rel="stylesheet" href="static/table.css">

<style  type="text/css">
html{ overflow: auto !important; }
body{ overflow: auto !important; }
</style>

<!--Custom jS-->

<script type="text/javascript">
	$(function() { $( "#admin_menu" ).menu();});
	
	$(function() {
		$("input[type=button],input[type=submit], button,#admin_edit").button();
	});
</script>

<script type="text/javascript">
	$(document).ready(function() {
	  $('#admin_usertbl').dataTable( {
		"iDisplayLength": 20,  
	  } );
	} );
</script>

<style  type="text/css">
.ui-menu { width: 150px; }
</style>

</head>
<body>

<ul id="admin_menu">
<li><a href="admin.jsp?menu=1"><span class="ui-icon ui-icon-person admin_menuicons"></span>User</a></li>
<li><a href="admin.jsp?menu=2"><span class="ui-icon ui-icon-script admin_menuicons"></span>Logging</a></li>
<li><a href="admin.jsp?menu=3"><span class="ui-icon ui-icon-wrench admin_menuicons"></span>Settings</a></li>
<li><a href="main.jsp"><span class="ui-icon ui-icon-wrench" id="admin_menuicons"></span>Back to Main</a></li>
</ul>

<c:if test="${param.menu == 1}">
<table>
	<tr>
		<td>
			<div id="admin_usercontainer">
			<table id="admin_usertbl" class="display">
				<thead><tr><th>UserID</th><th>Nickname</th><th>isAdmin</th><th>Edit User</th></tr></thead>
				<tbody>
				<c:set var="userCount"><ct:user par="0" /></c:set>
				<c:set var="userCount" value="${userCount-1}" />
				<c:forEach var="i" begin="0" end="${userCount}">
				<c:set var="string1"><a href="admin.jsp?menu=1&user=${i}">Edit</a></c:set>
					<tr>
						<td><ct:user par="3" num="${i}" /></td>
						<td><ct:user par="1" num="${i}" /></td>
						<td><ct:user par="2" num="${i}" /></td>
						<td>${string1}</td> 
					</tr>
				</c:forEach>
				</tbody>
			</table>			
			</div>
		</td>
		<td id="admin_add">
				<c:if test="${not empty param.user }">
					<c:set scope="page" var="nickname"><ct:user num="${param.user}" par="1"/></c:set>
					<c:set scope="page" var="isAdmin"><ct:user par="2" num="${param.user}" /></c:set>
					<c:set scope="page" var="userids"><ct:user par="3" num="${param.user}" /></c:set>
					<c:if test="${isAdmin}"><c:set var="isAdmincb">checked="checked"</c:set></c:if>
				</c:if>
			<div class="admin_box">
					<form method="POST" action="admin.jsp?menu=1">
						<div>
						<input type="hidden" name="action" value="edituser">
							<div class="bold">Edit User</div>
							<table>
								<tr>
									<td>UserID:</td>
									<td><input type="text" name="userid" value="${userids}"></td>
								</tr>
								<tr>
									<td >Nickname:</td>
									<td><input type="text" name="nickname" value="${nickname}"></td>
								</tr>
								<tr>
									<td >Password:</td>
									<td><input type="text" name="password" placeholder="Set new Password"></td>
								</tr>
								<tr>
									<td >isAdmin:</td>
									<td><input type="checkbox" name="rights" ${isAdmincb}></td>
								</tr>
								<tr>
									<td >Delete User:</td>
									<td><input type="checkbox" name="chkdel1"><input type="checkbox" name="chkdel2"><input type="checkbox" name="chkdel3"></td>
								</tr>
							</table>
						<input type="submit" value="Edit" />
						</div>
					</form> 
			</div>
			<div class="admin_box">
				<form method="POST">
					<div>
					<input type="hidden" name="action" value="adduser">
					<div class="bold">Add User</div>
					<table>
						<tr>
							<td>UserID:</td>
							<td><input type="text" name="userid" placeholder="UserID"></td>
						</tr>
						<tr>
							<td>Nickname:</td>
							<td><input type="text" name="nickname" placeholder="Nickname"></td>
						</tr>
						<tr>
							<td>Password:</td>
							<td><input type="text" name="password" placeholder="Password"></td>
						</tr>
						<tr>
							<td>isAdmin:</td>
							<td><input type="checkbox" name="rights"></td>
						</tr>
					</table>
					<input type="submit" value="Add user">	
					</div>		
				</form>
			</div>
		</td>
	</tr>
</table>

</c:if>
<br />

<c:if test="${param.menu == 2}">
<div id="admin_logcontainer">
<c:set var="logList">
	<ct:getLog />
</c:set>
<c:set var="result" value="${result-1}" />
<table>
	<tr><th>Log Content</th></tr>
	<c:forEach items="${logList}" var="s"> 
		<tr>
			<td><c:out value="${s}" /></td>
		</tr>	
	</c:forEach>
</table>
</div>
</c:if>

<c:if test="${param.menu == 3}">

<div id="admin_setting">
	<form method="POST" action="">
		<div>
			<input type="hidden" name="action" value="connect">
		</div>
	</form>
	<br>

	<c:set scope="page" var="carIP"><ct:getConf par="0" /></c:set>
	<c:set scope="page" var="dbadress"><ct:getConf par="1" /></c:set>
	<c:set scope="page" var="dbpw"><ct:getConf par="2" /></c:set>
	<c:set scope="page" var="dbuser"><ct:getConf par="3" /></c:set>
	<c:set scope="page" var="drivetime"><ct:getConf par="4" /></c:set>
	<c:set scope="page" var="filepath"><ct:getConf par="5" /></c:set>
	<c:set scope="page" var="logchat"><ct:getConf par="6" /></c:set>
	<c:set scope="page" var="logchattofile"><ct:getConf par="7" /></c:set>
	<c:set scope="page" var="loggps"><ct:getConf par="8" /></c:set>
	<c:set scope="page" var="loggpsinterval"><ct:getConf par="9" /></c:set>
	<c:set scope="page" var="loggpstofile"><ct:getConf par="10" /></c:set>
	<c:set scope="page" var="logq"><ct:getConf par="11" /></c:set>
	<c:set scope="page" var="logqtofile"><ct:getConf par="12" /></c:set>
	<c:if test="${logchat}"><c:set var="logchatcb">checked="checked"</c:set></c:if>
	<c:if test="${logchattofile}"><c:set var="logchattofilecb">checked="checked"</c:set></c:if>
	<c:if test="${loggpstofile}"><c:set var="loggpstofilecb">checked="checked"</c:set></c:if>
	<c:if test="${logq}"><c:set var="logqcb">checked="checked"</c:set></c:if>
	<c:if test="${logqtofile}"><c:set var="logqtofilecb">checked="checked"</c:set></c:if>



		<div id="admin_settingscontainer">
		<form method="POST" action="">
			<div>
			<input type="hidden" name="action" value="saveconfig">
				<table>
					<tr>
						<td id="admin_setting_tableleft">
							<table>
								<tr>
									<td>Car IP:</td>
									<td><input class="admin_left_input" type="text" name="IP" value="${carIP}"></td>
								</tr>
								<tr>
									<td>DB-Adress:</td>
									<td><input class="admin_left_input" type="text" name="DBAdress" value="${dbadress}"></td>
								</tr>
								<tr>
									<td>DB-User:</td>
									<td><input class="admin_left_input" type="text" name="DBUser" value="${dbuser}"></td>
								</tr>
								<tr>
									<td>DB-Password:</td>
									<td><input class="admin_left_input" type="text" name="DBPw" value="${dbpw}"></td>
								</tr>
								<tr>
									<td>Drive Time:</td>
									<td><input class="admin_left_input" type="text" name="drivetime" value="${drivetime}"></td>
								</tr>
								<tr>
									<td>File Path:</td>
									<td><input class="admin_left_input" type="text" name="filepath" value="${filepath}"></td>
								</tr>
								<tr>
									<td>Log Chat:</td>
									<td><input type="checkbox" name="logchat" ${logchatcb}></td>
								</tr>
							</table>
						</td>
						<td id="admin_setting_tableright">
							<table>
								<tr>
									<td>Log Chat-to-File:</td>
									<td><input type="checkbox" name="logchattofile" ${logchattofilecb}></td>
								</tr>
								<tr>
									<td>Log GPS:</td>
									<td><input type="checkbox" name="loggps" value="${loggpscb}"></td>
								</tr>
								<tr>
									<td>Log GPS-Interval:</td>
									<td><input type="text" name="loggpsint" value="${loggpsinterval}"></td>
								</tr>
								<tr>
									<td>Log GPS-to-File</td>
									<td><input type="checkbox" name="loggpstofile" ${loggpstofilecb} ></td>
								</tr>
								<tr>
									<td>Log Queue:</td>
									<td><input type="checkbox" name="logq" ${logqcb}></td>
								</tr>
								<tr>
									<td>Log Queue-to-File:</td>
									<td><input type="checkbox" name="logqtofile" ${logqtofilecb}></td>
								</tr>	
							</table>
						</td>
					</tr>
				</table>
			</div>
			<table>
				<tr>
					<td id="main_left_button">
						<input type="submit" value="Save settings">
					</td>
		</form>
					<td>
						<form method="POST" action="">
							<div>
								<input type="hidden" name="action" value="connect">
								<input id="admin_connect" type="submit" value="Connect to Car">
							</div>
						</form>
					</td>
				</tr>
			</table>
		
		</div>
</div>

</c:if>
</body>
</html>