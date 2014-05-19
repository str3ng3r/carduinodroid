/**
 * @File This is the Javascript file for the controls.
 * @author Sven-Leonhard Weiler
 * @param identifier
 *            To seperate between chat messages and control messages serverside.
 */
identifierControl = "Co%:";

w = false;
s = false;
a = false;
d = false;
l = false;
h = false;
/**
 * Send to server. Append s to start control and e to end control.
 */
function controlToServer(msg) {
	//$("#main_chat").append("Controller: " + msg + "\n");
	if($("#main_controls").is(":visible")) {
		ws.send(identifierControl + msg);
	}
}

/**
 * Heartbeat
 */
function heartbeat() {
	if($("#main_controls").is(":visible")) {
		ws.send(identifierHeartbeat);
	}
}

/**
 * Initialize Slider.
 */
$(function() {
	$("#main_maxspeed").on( "slidestop", function( event, ui ) {
		controlToServer("speed" + ui.value);
	} );
	$("#main_steerangle").on( "slidestop", function( event, ui ) {
		controlToServer("angle" + ui.value);
	} );
});


function controlHandleMessage(message) {
    if(message.data.charAt(4) === "y") {
		$("#main_controls").show();
		$("#main_chat").append("Controller: show" + "\n");
    } else if(message.data.charAt(4) === "n") {
		$("#main_controls").hide();
		$("#main_chat").append("Controller: hide" + "\n");
    }
}

/**
 * Register controlToServer to buttons.
 * 
 * @param control
 *            message identifier
 */
$(function() {
	$('#main_gadget_button_horn').mousedown(function() {
		if (!h) {
			controlToServer("hs");
			h = true;
		}
	});
	$('#main_gadget_button_light').mousedown(function() {
		if (!l) {
			controlToServer("ls");
			l = true;
		}
	});
	$('#main_steering_button_left').mousedown(function() {
		if (!a) {
			controlToServer("as");
			a = true;
		}
	});
	$('#main_steering_button_up').mousedown(function() {
		if (!w) {
			controlToServer("ws");
			w = true;
		}
	});
	$('#main_steering_button_right').mousedown(function() {
		if (!d) {
			controlToServer("ds");
			d = true;
		}
	});
	$('#main_steering_button_down').mousedown(function() {
		if (!s) {
			controlToServer("ss");
			s = true;
		}
	});
	$('#main_gadget_button_horn').mouseup(function() {
		if (h) {
			controlToServer("he");
			h = false;
		}
	});
	$('#main_gadget_button_light').mouseup(function() {
		if (l) {
			controlToServer("le");
			l = false;
		}
	});
	$('#main_steering_button_left').mouseup(function() {
		if (a) {
			controlToServer("ae");
			a = false;
		}
	});
	$('#main_steering_button_up').mouseup(function() {
		if (w) {
			controlToServer("we");
			w = false;
		}
	});
	$('#main_steering_button_right').mouseup(function() {
		if (d) {
			controlToServer("de");
			d = false;
		}
	});
	$('#main_steering_button_down').mouseup(function() {
		if (s) {
			controlToServer("se");
			s = false;
		}
	});
});
