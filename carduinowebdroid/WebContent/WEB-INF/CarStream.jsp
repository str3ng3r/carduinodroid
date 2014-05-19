<%-- <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>CarStream.jsp</title>
    
    <Style>
/*CarDuinoDroid Main Stylesheet*/

html{
	height: 100%;
	width: 100%;
	position:relative;
	overflow:hidden !important;
}

body {
	font-family: Tahoma, Geneva, sans-serif;
	height: 100%;
	width: 100%;
	overflow:hidden !important;
	margin:0px;
}

#main_stream{
	border:1px solid #ECD6BF;
	padding: 3px;
	height: auto !important;
	height: 75%;
	min-height: 75%;
	border-radius: 4px;
	box-shadow:
        0 2px 2px rgba(0,0,0,0.2),
        0 1px 5px rgba(0,0,0,0.2),
        0 0 0 12px rgba(255,255,255,0.4);
}

</Style>--%>
<!-- <script type="text/javascript">

	/* momentan in websocket.js */
	/*	@author Vincenz Vogel */
	
	var frame = 0;
	ws = new WebSocket("ws://"+ location.host + "/carduinowebdroid/websocketStream");
    ws.binaryType = "arraybuffer"; //Binärtyp auf arraybuffer setzen	
    ws.onopen = function(){
    	console.log("Websocket Ready!!");
    	sendBinaery();
    	
    };
    ws.onclose = function(){
    		console.log("Websocket Closed!!");
    };
    ws.onmessage = function(message){
    	console.log("Daten empfangen " + message.data.byteLength);
    	//Datentyp ermitteln
  	    if (message.data instanceof ArrayBuffer) {
  	    	var canvas = document.getElementById('CarStream');
  	    	var ctx = canvas.getContext('2d');
  	    	ctx.fillStyle = "rgb(255, 255, 200)";
  	        ctx.fillRect(0, 0, 1080, 720);
  	      	console.log( "Daten = " + message.data);
  	      	
  	      	var IntBuffer = new Uint8Array( message.data);  
  	   		// Bildbreite und Höhe ermitteln byte[] -> int
  	      	var B = 0;     
  	      	for (var i = 0; i < 4; i++) {
  	          	B |= ((IntBuffer[3-i] & 0xff) << (i << 3));
  	      	}
  	      	console.log( "Bildbreite = " + B);
  	      	var H = 0;     
	      	for (var i = 0; i < 4; i++) {
	          	H |= ((IntBuffer[7-i] & 0xff) << (i << 3));
	      	}
	      	console.log( "Bildhöhe = " + H);
	      	
  	      	var img = new Image();
  	    	var img = ctx.getImageData(0, 0, B, H);
  	    	
  	    	console.log("Dummy Image erzeugt");
  	    	console.log( "Messagelänge = "  + message.data.byteLength );
  	    	console.log( "IntBufLänge = "  + IntBuffer.byteLength);
  	    	
  	    	var i = 8;
  	    	for (var y = 0; y < H; y++) {  
  	    		for (var x = 0; x < B; x++) {
  	    	    	var idx = (x + y * B) * 4;
  	    	    	img.data[idx + 0] = IntBuffer[i + 0]; // & 0xff;
  	    	    	img.data[idx + 1] = (IntBuffer[i + 1]); // << 8) & 0xff00;
  	    	    	img.data[idx + 2] = (IntBuffer[i + 2]); // << 16) & 0xff0000;
  	    	    	img.data[idx + 3] = 255; //IntBuffer[i + 3] & 0xff000000;
  	    	    	i = i + 4;
  	    	    }
  	    	}
  	    	  	    	
  	    	console.log("Daten auf Image verschoben i = " + i);
  	    	// Bildmitte berechnen
  	    	var bx = Math.floor(canvas.width / 2);
  	    	bx = Math.floor(bx - B/2);
  	    	if (bx < 0) { bx = 0; };
  	    	console.log("Bildmitte B = " + bx);
  	    	var by = Math.floor(canvas.height / 2);
  	    	by = Math.floor(by - H/2);
  	    	if (by < 0) { by = 0; };
  	    	console.log("Bildmitte H = " + by);
  	    	ctx.putImageData( img, bx, 10);
  	    	frame = frame + 1;
  	    	ctx.strokeText(frame, 20, 20, 40)
  	    	ctx.restore();
  	    	
  	    }
  	    else if (message.data instanceof Blob) {
  	    	alert("Blob");
  	    }
  	    else if (typeof (message.data === "string")) {
  	    	
  	    	alert("string");
  	    }
  	    // näschstes Bild anfordern
  	  	sendBinaery();
    };
    	
    ws.onerror = function(){
    		console.log("Websocket Error!!");
    };
    	
       	
    /**
     * Close Websocketconnection.
     */
    function closeConnect(){
    	ws.close();
    }
    
    function sendMessage() {
		  ws.send(2);
	}
        
    function sendBinaery() {
    	var message = new ArrayBuffer(9);
        var dataViewMessage = new DataView(message);

        dataViewMessage.setInt8(0, 25); // einen Binärwert generieren
        
        //message per Websocket wegschicken
        ws.send(message);	
        console.log("DataView verschickt");
    }
   
    </script> -->
    
<%-- </head>
  <body>
    <h3> Hallo, der CarStream meldet sich. </h3>
    <p> <%= (new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss")).format(new Date()) + " h" %> </p>
    <div id="main_stream"><center>
        <canvas id="CarStream" width="1080" height="720">
        Dein Browser kann diese Grafik nicht darstellen.
        Oder Javascript ist nicht aktiviert</canvas></center>
    </div>
    
    <p> <a href='/CarStreamTest/'>zur&uuml;ck</a> </p>
  </body>
</html> --%>