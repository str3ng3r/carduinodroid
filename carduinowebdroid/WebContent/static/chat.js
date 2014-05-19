/**
 * @File This is the Javascript file for the chat.
 * @author Sven-Leonhard Weiler
 * 
 */
identifierChat = "Ch%:";
/**
 * Register .append(newPart) function to be able to add text to the chat
 * textarea.
 */
$(function() {

	$.fn.extend({
		append : function(newPart) {
			return this.each(function() {
				$(this).val($(this).val() + newPart);
			});
		}
	});

});

/**
 * Register postToServer on Chat_button.click event.
 */
$(function() {
	$("#main_chat_speak").mousedown(function() {
			postToServer();
			$("#main_chat_textinput").val('');
		});
});

/**
 * Handle incoming messages.
 */
function chatHandleMessage(message) {
	data = message.data;
	msg = data.substring(identifierChat.length, data.length);
	$("#main_chat").append(msg + "\n");
    $('#main_chat').scrollTop($('#main_chat')[0].scrollHeight);
}

/**
 * Send chat_input to server.
 */
function postToServer() {
	if($("#main_chat_textinput").val().trim() !== "") ws.send(identifierChat + $("#main_chat_textinput").val().trim());
}