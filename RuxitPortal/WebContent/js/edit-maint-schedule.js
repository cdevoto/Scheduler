var RUXIT = RUXIT || {};

console = console || {
	log: function() {},
	error: function () {},
	warn: function () {},
	assert: function () {},
	dir: function () {},
};

RUXIT.portal = (function () {
	var tenantId = 1;
	var scheduleId = $.url().param('scheduleId');
	var hasSchedule = scheduleId !== undefined;
	var testDefs = {};
	var DEBUG = ($.url().param("debug") === "true");
	DEBUG = true;

	var handlePageError = function (message) {
		if (DEBUG === true) {
		    console.log(message);
		    return;
		}
		console.log(message);
		//window.open("/ruxit/error-page.html", "_self");
	};
	
	var generateTestDefs = function (json) {
		var focusedElement = $(document.activeElement);
		var tBody = $("#select-schedule-table-body");
		tBody.empty();
		var tr, td, count, max, record, row, checkbox;
		if (json.status !== 200) {
			tr = $("<tr>");
			tr.append("<td colspan=2 style=\"width: 480px\">An error occurred while attempting to retrieve your execution schedules.</td>");
			tBody.append(tr);
			console.log(json.message);
		} else if (json.data.length === 0) {
			tr = $("<tr>");
			tr.append("<td colspan=2 style=\"width: 480px\">There are no execution schedules defined.</td>");
			tBody.append(tr);
			return;
		} 
		max = json.data.length;
		for (count = 0; count < max; count++) {
			record = json.data[count];
			row = "select-schedule-table-row-" + record.id;
			tr = $("<tr>");
			
			tr.attr("id", row);
			td = $("<td>");
			td.addClass("table-select");
			checkbox = $("<input>");
			checkbox.attr("type", "checkbox");
			checkbox.attr("id", "test-def-" + record.id);
			checkbox.attr("name", "testDef");
			checkbox.attr("value", record.id);
			if (testDefs[record.id] !== undefined) {
			    checkbox.attr("checked", "true");
			}
			checkbox.change(function () {
				if ($(this).is(':checked')) {
					testDefs[$(this).val()] = true;
				} else {
					delete testDefs[$(this).val()];
				}
			});
			td.append(checkbox);
			tr.append(td);
			
			td = $("<td>");
			td.addClass("table-action");
			td.append(record.name);
			tr.append(td);
			tBody.append(tr);
		}

		
		// reset the tab indexes
        $("#form-edit-maint-schedule *").filter(":input").each(function (index) {
        	if ($(this).attr("type") !== "hidden") {
        	    $(this).attr("tabIndex", "" + (index + 1));
        	}
        });

        focusedElement.focus();
		
	}; 
	
    var loadTestDefs = function () {
    	var qString = "tenantId=" + encodeURIComponent(tenantId);
    	$.ajax({
			url: "/synthetic/api/test-definition?" + qString,
			dataType: "json",
			success: generateTestDefs,
			error: function (response, status, error) {
				console.log("An AJAX error occurred while attempting to initialize the page: " + status + (error !== undefined ? " - " + error : ""));
			}
		});
    };
    
	var generateSchedule = function (json) {
		var schedule, count, max;
		var timeZoneField = $("#timezone");
		
		if (json.status !== 200 || json.data.length === 0) {
			handlePageError("The specified schedule could not be retrieved: " + json.message);
			return;
		}
        schedule = json.data;
        
        $("#schedule-id").val(schedule.id);
    	$("#name").val(schedule.name);
    	
    	
      	$("#timezone").val(schedule.timeZone.id);
      	timeZoneField.next().children().first().html(schedule.timeZone.name);
		
        $("#rrule").val(schedule.rrule);
        $("#duration").val(schedule.duration);
      	
        if (schedule.testDefinitions !== undefined) {
	      	max = schedule.testDefinitions.length;
	      	for (count = 0; count < max; count++) {
	      		testDefs[schedule.testDefinitions[count].id] = true;
	      	}
        }
      	
      	$("#save-schedule").val("Save");
        
        loadTestDefs();
	};
	
	var loadSchedule = function () {
		$.ajax({
			url: "/synthetic/api/schedule?scheduleId=" + scheduleId,
			dataType: "json",
			success: generateSchedule,
			error: function (response, status, error) {
				handlePageError("An AJAX error occurred while attempting to initialize the page: " + status + (error !== undefined ? " - " + error : ""));
			}
		});
	};

    var generateTimeZones = function (json) {
    	var timeZoneField = $("#timezone");
    	var max, count, timeZones, timeZone, option, defaultTimeZone = 1;
		if (json.status !== 200) {
			window.alert("An error occurred while attempting to initialize the page: " + json.status + " - " + json.message);
			return;
		} else if (json.data.length === 0) {
			window.alert("An error occurred while attempting to initialize the page: 500 - No timezones defined.");
			return;
		}
		timeZones = json.data;
	    max = json.data.length;
	    for (count = 0; count < max; count++) {
	    	timeZone = timeZones[count];
	    	if(timeZone.name === "US/Eastern") {
	    		defaultTimeZone = "" + timeZone.id;
	    	}
	    	option = $("<option>");
	    	option.attr("value", timeZone.id);
	    	option.text(timeZone.name);
	    	timeZoneField.append(option);
	    }

    	timeZoneField.change(function () {
    		var selected = $("#timezone option:selected");
    		timeZoneField.next().children().first().html(selected.text());
    		timeZoneField.focus();

    	});
    	
    	$("#timezone option").filter(function() { 
    	    return ($(this).attr("value") === defaultTimeZone); 
    	}).attr("selected", true);
    	timeZoneField.next().children().first().html($("#timezone option:selected").text());
    	
    };

    var loadTimeZones = function () {
    	$.ajax({
			url: "/synthetic/api/timezone",
			dataType: "json",
			success: generateTimeZones,
			error: function (response, status, error) {
				console.log("An AJAX error occurred while attempting to initialize the page: " + status + (error !== undefined ? " - " + error : ""));
			}
		});
    };
    
    
    var validateForm = function () {
    	var aside = $("#submit-message");
    	var name = $("#name").val();
    	var rrule = $("#rrule").val();
       	var timeZone = $("#timezone").val();
      	var duration = $("#duration").val();
    	var hasName = $.trim(name).length > 0;
    	var hasRRule = $.trim(rrule).length > 0;
    	var hasTimeZone = $.trim(timeZone).length > 0;
       	var hasDuration = $.trim(duration).length > 0;
           	
    	aside.empty();
    	
    	if (!hasName) {
    		aside.text("To define a maintenance schedule, you must specify a name.");
    		return false;
    	}
    	
    	if (!hasRRule) {
    		aside.text("To define a schedule, you must specify a recurrence rule.");
    		return false;
    	}
    	
    	if (!hasTimeZone) {
    		aside.text("To define a maintenance schedule, you must specify a timezone.");
    		return false;
    	}
    	
       	if (!hasDuration) {
    		aside.text("To define a maintenance schedule, you must specify a duration.");
    		return false;
    	}
    	
    	
    	return true;
    };

    var submitForm = function () {
    	var aside = $("#submit-message");
    	var formData;
    	if (!validateForm()) {
    	    return;	
    	}
    	formData = $("#form-edit-maint-schedule").serialize();
    	console.dir(formData);
    	$.ajax({
			url: "/synthetic/api/schedule",
			dataType: "json",
			type: "POST",
			data: formData,
			success: function (json) {
				if (json.status !== 200) {
					aside.empty();
					aside.text("An error occurred while attempting to create the maintenance schedule: " + json.status + " - " + json.message);
					return;
				}
				window.open("/ruxit/manage-maint-schedules.html", "_self");
			},
			error: function (response, status, error) {
				aside.empty();
				aside.text("An error occurred while attempting to create the schedule: " + status + (error !== undefined ? " - " + error : ""));
			}
		});
    };
    
    var initSchedule = function () {
    	loadTimeZones();
        if (!hasSchedule) {
            loadTestDefs();
        } else {
        	loadSchedule();
        }
    	$("#tenant-id").val(tenantId);
    	$("#form-edit-maint-schedule").submit(function (e) {
    		e.preventDefault();
    		submitForm();
    		return false;
    	});
    	
    	$("#select-test-defs").change(function () {
    		var checked = $(this).is(':checked');
    		(function (checked) {
	    		$("#select-schedule-table-body *").filter(":input").each(function (index) {
	    			if (checked) {
	    				$(this).attr("checked", true);
	    				testDefs[$(this).val()] = true;
	    			} else {
	    				$(this).attr("checked", false);
	    				delete testDefs[$(this).val()];
	    			}
	    		});
    		})(checked);
		});
    	
    	$(document).bind("keypress", function(e){
    		   if(e.which === 13) { // return
    			   RUXIT.portal.submitForm();
    		   }
         });
    	$("#name").focus();

    };
    
	return {
    	initSchedule: initSchedule
    };
})();

$(document).ready(function () {
	RUXIT.portal.initSchedule();
	
});