/**
 * @file This is the Javascript file for AJAX calls.
 * @author Christoph Braun
 */
$(document).ready(function () {
    $("#main_qsubmit").click(function () {
        $.ajax({
            type: "POST",
            url: "main.jsp",
            data: "action=toggleq",
            success: function (msg) {
                $("main_q").load("queue.jsp");
            }
        });
        return false;
    });

});

$(document).ready(function () {
    $("#main_take_control").click(function () {
        $.ajax({
            type: "POST",
            url: "main.jsp",
            data: "action=admincontrol",
            success: function (msg) {
                $("#main_dialog_control").dialog("open");
            }
        });
        return false;
    });

});

$(document).ready(function () {
    $("#main_stopdriving").click(function () {
        $.ajax({
            type: "POST",
            url: "main.jsp",
            data: "action=stopdriving",
            success: function (msg) {
                $("#main_controls").hide();
                /* TODO: Needs ACK*/
            }
        });
        return false;
    });
});

$(document).ready(function () {
    $.ajaxSetup({
        cache: false
    });
    setInterval(function () {
        $('#main_q').load('queue.jsp');
    }, 1000);
});