var BOSH_SERVICE = '/http-bind';
var USERNAME = 'wallcology-notifier-interface@localhost';
var PASSWORD = 'wallcology-notifier-interface';
var RESOURCE = 'strophe';

var connection = null;


function appendNotification(msg) {
    // Parse JSON message
    try {
        var jsonMsg = JSON.parse(msg);
    } catch (e) {
        //console.warn("Notification is not JSON, ignoring");
        return;
    }
    $('#alert_list').append('<li><a href="#alert_' + jsonMsg.id + '" >' + jsonMsg.title + '</a></li>');
    $('#alert_list').listview('refresh');
    createAlertDetailsPage(jsonMsg);
}


function createAlertDetailsPage(jsonMsg) {
    $('<div id="alert_' + jsonMsg.id + '" data-role="page"><div data-role="header"> <h1>Details</h1><a href="#" data-rel="back" data-theme="a">Back</a></div> <div id="alerts_list_' + jsonMsg.id + '" data-role="content"></div></div>').appendTo("body");
    for (var i = 0; i < jsonMsg.alerts.length; i++) {
           $('<div class=alert >' + jsonMsg.alerts[i].text + '</div>').appendTo('#alerts_list_'+jsonMsg.id);
       };
    $('<br /><button>I took action</button>').appendTo('#alerts_list_'+jsonMsg.id);
}


function onConnect(status)
{
    if (status == Strophe.Status.CONNECTING) {
        log('Strophe is connecting.');
    } else if (status == Strophe.Status.CONNFAIL) {
        log('Strophe failed to connect.');
        $('#connect').get(0).value = 'connect';
    } else if (status == Strophe.Status.DISCONNECTING) {
        log('Strophe is disconnecting.');
    } else if (status == Strophe.Status.DISCONNECTED) {
        log('Strophe is disconnected.');
        $('#connect').get(0).value = 'connect';
    } else if (status == Strophe.Status.CONNECTED) {
        log('Strophe is connected.');
        addDefaultXmppHandlers();
        connection.send($pres().tree());
    }
}

function addDefaultXmppHandlers() {
        // Add message handler
        connection.addHandler(onMessage, null, 'message', null, null,  null); 
        // Set up a pinger to keep the connection alive
        connection.ping.addPingHandler(function(ping) {
            log("GOT PING! sending pong...")
            connection.ping.pong(ping)
        })
        pingInterval = 14 * 1000 // default is 14 seconds
        connection.addTimedHandler(pingInterval, function() {
            log("SENDING PING!")
            connection.ping.ping(Strophe.getDomainFromJid(USERNAME))
            return true
        })
}

function onMessage(msg) {
    var to = msg.getAttribute('to');
    var from = msg.getAttribute('from');
    var type = msg.getAttribute('type');
    var elems = msg.getElementsByTagName('body');

    if (elems.length > 0) {
    var body = elems[0];

    appendNotification(Strophe.getText(body));
    
    //var reply = $msg({to: from, from: to, type: 'chat'})
    //        .cnode(Strophe.copyElement(body));
    //connection.send(reply.tree());
    //log('ECHOBOT: I sent ' + from + ': ' + Strophe.getText(body));
    }

    // we must return true to keep the handler alive.  
    // returning false would remove it after it finishes.
    return true;
}

$(document).ready(function () {
    connection = new Strophe.Connection(BOSH_SERVICE);
    //connection.rawInput = function (data) { log('RECV: ' + data); };
    //connection.rawOutput = function (data) { log('SEND: ' + data); };
    //Strophe.log = function (level, msg) { log('LOG: ' + msg); };
    connection.connect(USERNAME+'/'+RESOURCE, PASSWORD, onConnect);
    $('#debug_disconnect').click(function(event){
     connection.disconnect();
     event.preventDefault();
    });

    // Click on habitat
    $("#habitat_fb").live('pageinit', function() {
        getParticipationData()
        setInterval(getParticipationData, 3000)
    });
});


function getParticipationData() {
    $('#part_content tr').each(function() {
        var groupId = $(this).attr("id")
        if(groupId==undefined) return
        $.ajax({
            type: "GET",
            url: "/mongoose/wallcology/observations/_count",
            data: { criteria: JSON.stringify({"type" : "habitat", "origin": groupId}) },
            context: this,
            success: function(data) {
                if (data.ok === 1) {
                    var oldValue = $('#part_content tr#'+groupId+' td:last').html();
                    $('#part_content tr#'+groupId+' td:last').replaceWith('<td class="td_ct">'+data.count+'</td>')
                    if (oldValue!=data.count && oldValue!="") {
                        $('#part_content tr#'+groupId+' td:last').css("background-color", "#FF6347")
                    }
                } else {
                    log("Error fetching data")
                }
            }
        });
    });
    //console.log("Updated")
}
