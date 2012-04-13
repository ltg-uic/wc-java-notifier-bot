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
        data: {sort : JSON.stringify({"history.new" : -1}), limit: 15},
        context: this,
        success: function(data) {
            if (data.ok === 1) {
                // Process all notifications
                $.each(data.results, function(i, n) {
                    processNotification(n)
                });
            } else {
                console.log("Error fetching notifications")
            }
        }
    });
}




function processNotification(msg) {
    // Drop the old notifications...
    // Check if, in the notification list, there is already a notification with the same id and if so, return
    // TODO
    // If not, append the new notification at the top of the list
    $('#alert_list').append('<li><a href="#alert_' + msg._id.$oid + '" >' + msg.title + '</a></li>');
    $('#alert_list').listview('refresh');
    // remove alerts details page
    $("#alert_" + msg._id.$oid).remove();
    // create alerts detail page
    $('<div id="alert_' + msg._id.$oid + '" data-role="page"><div data-role="header">' + 
        '<h1>Details</h1><a href="#" data-rel="back" data-theme="a">Back</a></div>' + 
        '<div id="alerts_list_' + msg._id.$oid + '" data-role="content"></div></div>').appendTo("body");
    // add discard button
    $("<button id='no_deal_" + msg._id.$oid + "' class='no_deal'>I can't deal with this now!</button><br />").appendTo('#alerts_list_' + msg._id.$oid);
    // append alerts to details page
    $.each(msg.alerts, function (i,a) {
        $('<div class=alert >' + a.text + '</div>').appendTo('#alerts_list_' + msg._id.$oid);
    });
    // add action button
    $("{<br /><button id='deal_" + msg._id.$oid + "' class='deal'>I took action</button>").appendTo('#alerts_list_' + msg._id.$oid);
    // add events when the users press buttons
    // $("#alert_' + msg._id.$oid").bind('pagebeforeshow', function() {
    //     // listener for no_deal
    //     $("#no_deal_" + msg._id.$oid).click(function(){
    //         alert("No deal!")
    //     });
    //     // listener for deal
    //     $("#deal_" + msg._id.$oid).click(function(){
    //         alert("Deal!")
    //     });
    // });

}

