/**
 * @File This is the Javascript file for the Websocketconnection.
 * @author Sven-Leonhard Weiler
 */
identifierHeartbeat = "Hb%:";
identifierImageFrame = "If%:";
/**
 * Initialize and define Websocket.
 */
function initializeWebsocket() {

    ws = new WebSocket("ws://" + location.host + "/carduinowebdroid/websocket");
    //ws.binaryType = "arraybuffer";
    
    ws.onopen = function () {
        $("#main_chat").append("Websocket Connected!" + "\n");
        sendBinaery();
    };
    ws.onclose = function () {
        $("#main_chat").append("Websocket Closed!" + "\n");
        // try reconnect after 2 sek
        // window.setTimeout(initializeWebsocket, 2000);
    };
    ws.onmessage = function (message) {
    	if (message.data.match(identifierImageFrame + "*")) streamHandleMessage(message);
		else if (message.data.match(identifierChat + "*")) chatHandleMessage(message);
		else if (message.data.match(identifierControl + "*")) controlHandleMessage(message);
		else if (message.data.match("invalid")) window.location.replace("index.jsp");
    };
    ws.onerror = function () {
        $("#main_chat").append("Websocket Error!" + "\n");
    };

}

/**
 * Close Websocketconnection.
 */
function closeConnect() {
    ws.close();
}