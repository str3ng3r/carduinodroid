/**
 * @File This is the Javascript file for the Websocketconnection.
 * @author Vincenz Vogel
 * @author Sven-Leonhard Weiler
 */
function streamHandleMessage(message) {
	data = message.data;
	msg = data.substring(identifierImageFrame.length, data.length);
	$("#image").attr('src', 'data:image/png;base64,'+msg);
}