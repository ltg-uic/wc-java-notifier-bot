var hab_part_justToday = false
var org_part_justToday = false

var notifications_poll;



$(document).ready(function () {

    // ================================================================================ PARTICIPATION!

    // Click on habitat
    $("#habitat_fb").live('pageinit', function() {
        hab_part_justToday = false
        getHabitatData()
        setInterval(getHabitatData, 3000)
    });
    

    // Click on today's button - habitat
    $("#hab_today").click(function(event){
        if ($("#hab_today").text()=="Today") {
            // Swith to TODAY view (therefore buttn is overall)
            hab_part_justToday = true
            $("#hab_today .ui-btn-text").text('Overall')
        } else {
            // Swith to OVERALL view (therefore buttn is today)
            hab_part_justToday = false
            $("#hab_today .ui-btn-text").text('Today')
        }
        getHabitatData()
    });

    // Click on organisms
    $("#organism_fb").live('pageinit', function() {
        org_part_justToday = false
        getOrganismsData()
        setInterval(getOrganismsData, 3000)
    });

    // Click on today's button - organisms
    $("#org_today").click(function(event){
        if ($("#org_today").text()=="Today") {
             // Swith to TODAY view (therefore buttn is overall)
            org_part_justToday = true
            $("#org_today .ui-btn-text").text('Overall')
        } else {
            // Swith to OVERALL view (therefore buttn is today)
            org_part_justToday = false
            $("#org_today .ui-btn-text").text('Today')
        }
        getOrganismsData()
    });



    // ================================================================================ NOTIFICATIONS!

    $("#notifications").bind('pagebeforeshow', function() {
        getNotifications()
        notifications_poll = setInterval(getNotifications, 2000)
    });

    $("#notifications").bind('pagebeforehide', function() {
        clearInterval(notifications_poll)
    });

});





function getHabitatData() {
    $('#part_content tr').each(function() {
        var groupId = $(this).attr("id")
        if(groupId==undefined) return;
        if(hab_part_justToday==false) {
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
                        console.log("Error fetching data")
                    }
                }
            });
        } else {
            $.ajax({
                type: "GET",
                url: "/mongoose/wallcology/observations/_count",
                data: { criteria: JSON.stringify('{"type" : "habitat", "timestamp" : {$regex : "/2012-04-10*/", $options : "x"}, "origin" : groupId}') },
                context: this,
                success: function(data) {
                    if (data.ok === 1) {
                        var oldValue = $('#part_content tr#'+groupId+' td:last').html();
                        $('#part_content tr#'+groupId+' td:last').replaceWith('<td class="td_ct">'+data.count+'</td>')
                        if (oldValue!=data.count && oldValue!="") {
                            $('#part_content tr#'+groupId+' td:last').css("background-color", "#FF6347")
                        }
                    } else {
                        console.log("Error fetching data")
                    }
                }
            });
        }
    });
}





function getOrganismsData() {
    $('#org_part_content tr').each(function() {
        var groupId = $(this).attr("id")
        if(groupId==undefined) return;
        if(hab_part_justToday==false) {
            $.ajax({
                type: "GET",
                url: "/mongoose/wallcology/observations/_count",
                data: { criteria: JSON.stringify({"type" : "organism", "origin": groupId}) },
                context: this,
                success: function(data) {
                    if (data.ok === 1) {
                        var oldValue = $('#org_part_content tr#'+groupId+' td:last').html();
                        $('#org_part_content tr#'+groupId+' td:last').replaceWith('<td class="td_ct">'+data.count+'</td>')
                        if (oldValue!=data.count && oldValue!="") {
                            $('#org_part_content tr#'+groupId+' td:last').css("background-color", "#FF6347")
                        }
                    } else {
                        console.log("Error fetching data")
                    }
                }
            });
        } else {
            $.ajax({
                type: "GET",
                url: "/mongoose/wallcology/observations/_count",
                data: { criteria: JSON.stringify('{"type" : "organism", "timestamp" : {$regex : "/2012-04-10*/"}, "origin" : groupId}') },
                context: this,
                success: function(data) {
                    if (data.ok === 1) {
                        var oldValue = $('#org_part_content tr#'+groupId+' td:last').html();
                        $('#org_part_content tr#'+groupId+' td:last').replaceWith('<td class="td_ct">'+data.count+'</td>')
                        if (oldValue!=data.count && oldValue!="") {
                            $('#org_part_content tr#'+groupId+' td:last').css("background-color", "#FF6347")
                        }
                    } else {
                        console.log("Error fetching data")
                    }
                }
            });
        }
    });
}






function getNotifications() {
    $.ajax({
        type: "GET",
        url: "/mongoose/wallcology/notifications/_find",
        data: { criteria : JSON.stringify({$or : [{"status" : "new"} , {"status" : "viewed"}]}), 
                sort : JSON.stringify({"history.new" : -1}), 
                limit: 15},
        context: this,
        success: function(data) {
            if (data.ok === 1) {
                // Process notifications
                if( $('#alert_list li').length == 0) {
                    // No notifications are displayed
                    createList(data.results)
                    $('#alert_list').listview('refresh')
                } else if (data.results.length == 0) {
                    // Delete all displayed notificaitons
                    deleteList(data.results)
                    $('#alert_list').listview('refresh')
                } else{
                    // Add some, delete some
                    updateList(data.results);
                    $('#alert_list').listview('refresh');
                }
            } else {
                console.log("Error fetching notifications")
            }
        }
    });
}


function createList(notifications) {
    $.each(notifications, function(i, notif) {
        appendNewNotification(notif, "bottom")
    });
}

function deleteList(notifications) {
    $('#alert_list li').each(function(index) {
        removeNotification($(this).attr("id"))
    });
}

function updateList(notifications) {
    // Find new notifications... 
    var i=0
    while ( notifications[i]._id.$oid != $('#alert_list li').first().attr("id") ){
        i++;
    }
    // ...and insert them in reverse order
    while (i>0) {
        appendNewNotification(notifications[i-1], "top")
        i--
    }
    // Find old notifications and remove them
    var j= $('#alert_list li').length - 1
    while( $('#alert_list li').eq(j).attr("id") != notifications[notifications.length-1]._id.$oid ) {
        removeNotification($('#alert_list li').eq(j).attr("id"))
        j--
    }
}


function appendNewNotification(msg, where) {
    // 1. Append the notification... 
    if (where=="bottom") {
        //at the end of the list
        $('#alert_list').append('<li id="' + msg._id.$oid + '"><a href="#alert_' + msg._id.$oid + '" >' + msg.title + '</a></li>');
    } else {
        $('#alert_list').prepend('<li id="' + msg._id.$oid + '"><a href="#alert_' + msg._id.$oid + '" >' + msg.title + '</a></li>');
    }
    // 2. Create alerts detail page
    $('<div id="alert_' + msg._id.$oid + '" data-role="page"><div data-role="header">' + 
        '<h1>Details</h1><a href="#" data-rel="back" data-theme="a">Back</a></div>' + 
        '<div id="alerts_list_' + msg._id.$oid + '" data-role="content"></div></div>').appendTo("body");
    // 2.a add discard button to the details page
    $("<button id='no_deal_" + msg._id.$oid + "' class='no_deal'>I can't deal with this now!</button><br />").appendTo('#alerts_list_' + msg._id.$oid);
    // 2.b append alerts to details page
    $.each(msg.alerts, function (i,a) {
        $('<div class=alert >' + a.text + '</div>').appendTo('#alerts_list_' + msg._id.$oid);
    });
    // 2.c add action button
    $("{<br /><button id='deal_" + msg._id.$oid + "' class='deal'>I took action</button>").appendTo('#alerts_list_' + msg._id.$oid);
    // 2.d add events when the users press buttons
    $("#alert_" + msg._id.$oid).bind('pagebeforeshow', function() {
        // listener for no_deal
        $("#no_deal_" + msg._id.$oid).click(function(){
            alert("No deal!")
        });
        // listener for deal
        $("#deal_" + msg._id.$oid).click(function(){
            alert("Deal!")
        });
    });
}


function removeNotification(id) {
    $('#' + id).remove()            // Remove from list
    $("#alert_" + id).remove()      // Remove alerts details page 
}

