<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd"> 
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title>CarDuinoDroid</title>
<link rel="icon" href="static/favicon.ico" type="image/x-icon">
<!--JQuery Import-->
<link rel="stylesheet" href="static/jqueryui_static/css/ui-lightness/jquery-ui-1.10.3.custom.css">
<script type="text/javascript" src="static/jquery-1.9.1.js"></script>
<script type="text/javascript" src="static/jquery-ui.js"></script>

<!--Custom Stylesheet-->

<link rel="stylesheet" href="static/style.css">

<style>
html{ overflow: auto !important; }
body{ overflow: auto !important; }
</style>

<!--Custom jS-->

<script type="text/javascript" src="static/index.js"></script>
</head>
<body>

<p>
<div id="index_logo">
	<img src="static/logo.png" alt="CarDuinoDroid">
</div>

<p>
<div id="index_loginbox">
<form method="POST" action="">
	<div>
		<h1>Login</h1>
	    	<table id="index_logintable">
	        	<tr>
	            	<td>UserID:</td>
	                <td><input type="text" name="loginName" ></td>
	            </tr>
	            <tr>
	            	<td>Password:</td>
	                <td><input type="password" name="password"></td>
	            </tr>
	       </table>
	    <input id="index_buttonsubmit" type="submit" value="Login">
	    <input type="hidden" name="action" value="login">
       </div>
</form>
</div>

<p>
<div id="index_guestbox">
    <form method="POST" action="">
    	<div>
	    	<input type="hidden" name="action" value="watchDriver">
	        <input type="submit" value="Watch a Driver">
        </div>     
    </form> 
</div>

<p>
<div id="index_note">
  Welcome to Carduinodroid. If you want to watch another driver, please click on "Watch a driver." We hope you enjoy our website.
</div>
<p>
<div id="index_footer">
<a href="impress.jsp">Impress</a> | <a href="about.jsp" >About</a>
</div>

</body>
</html>