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
	var scriptId = $.url().param('scriptId');
	var testDefId = $.url().param('testDefinitionId');
	var hasTestDef = testDefId !== undefined;
	var locations = {};
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
	
	var generateLcps = function (json) {
		var focusedElement = $(document.activeElement);
		var newLocations = {};
		var tBody = $("#lcp-table-body");
		tBody.empty();
		var tr, td, count, max, record, row, checkbox;
		if (json.status !== 200) {
			tr = $("<tr>");
			tr.append("<td colspan=2>An error occurred while attempting to retrieve your locations, carries, and players.</td>");
			tBody.append(tr);
		} else if (json.data.length === 0) {
			tr = $("<tr>");
			tr.append("<td colspan=2>There are no locations, carriers, and players that match your requirements.</td>");
			tBody.append(tr);
			return;
		} 
		max = json.data.length;
		for (count = 0; count < max; count++) {
			record = json.data[count];
			row = "lcp-table-row-" + record.id;
			tr = $("<tr>");
			
			tr.attr("id", row);
			td = $("<td>");
			checkbox = $("<input>");
			checkbox.attr("type", "checkbox");
			checkbox.attr("id", "lcp-" + record.id);
			checkbox.attr("name", "lcpId");
			checkbox.attr("value", record.id);
			if (locations[record.id] !== undefined) {
			    newLocations[record.id] = locations[record.id];
			    checkbox.attr("checked", "true");
			}
			checkbox.change(function () {
				if ($(this).is(':checked')) {
					locations[$(this).val()] = true;
				} else {
					delete locations[$(this).val()];
				}
			});
			td.append(checkbox);
			tr.append(td);
			
			tr.append($("<td>").append(record.name));
			td = $("<td>");
			tBody.append(tr);
		}
		locations = {};
		for (prop in newLocations) {
			locations[prop] = newLocations[prop];
		}

		
		// reset the tab indexes
        $("#form-edit-schedule *").filter(":input").each(function (index) {
        	if ($(this).attr("type") !== "hidden") {
        	    $(this).attr("tabIndex", "" + (index + 1));
        	}
        });

        focusedElement.focus();
		
	}; 
	
    var loadLcps = function () {
    	var qString = "scriptId=" + encodeURIComponent(scriptId);
    	$("#requires-flags *").filter(":input").each(function (index) {
        	if ($(this).attr("name") === "requires" && $(this).is(':checked')) {
        	    qString += "&requires=" + encodeURIComponent($(this).val());
        	}
        });
    	$.ajax({
			url: "/synthetic/api/lcp?" + qString,
			dataType: "json",
			success: generateLcps,
			error: function (response, status, error) {
				console.log("An AJAX error occurred while attempting to initialize the page: " + status + (error !== undefined ? " - " + error : ""));
			}
		});
    };
    
	var generateTestDef = function (json) {
		var testDef, count, max;
		var execScheduleField = $("#execution-schedule");
		
		var flags = {};
		if (json.status !== 200 || json.data.length === 0) {
			handlePageError("The specified test definition could not be retrieved: " + json.message);
		}
        testDef = json.data;
        
        $("#test-definition-id").val(testDef.id);
    	$("#name").val(testDef.name);
    	
    	max = testDef.requiresFlags.length;
    	for (count = 0; count < max; count++) {
    		flags[testDef.requiresFlags[count].description] = true;
    	}
    	
      	$("#requires-flags *").filter(":input").each(function (index) {
        	if ($(this).attr("name") === "requires" && flags[$(this).val()] === true) {
        	    $(this).attr("checked", true);
        	}
        });
      	
      	execScheduleField.val(testDef.execSchedule.rrule);
      	execScheduleField.next().children().first().html($("#execution-schedule option:selected").text());
      	$("#exec-schedule-id").val(testDef.execSchedule.id);
      	
      	max = testDef.lcps.length;
      	for (count = 0; count < max; count++) {
      		locations[testDef.lcps[count].id] = true;
      	}
      	
      	$("#save-schedule").val("Save");
        
        loadLcps();
	};
	
	var loadTestDef = function () {
		if (scriptId === undefined) {
			handlePageError("Unknown value for Script Id");
			return;
		}
		$.ajax({
			url: "/synthetic/api/test-definition?testDefinitionId=" + testDefId,
			dataType: "json",
			success: generateTestDef,
			error: function (response, status, error) {
				handlePageError("An AJAX error occurred while attempting to initialize the page: " + status + (error !== undefined ? " - " + error : ""));
			}
		});
	};


    var generateRequiresFlags = function (json) {
        var section = $("#requires-flags");
        var requiresFlags = json.data;
        var max, count, requiresFlag;
		if (json.status !== 200) {
			window.alert("An error occurred while attempting to initialize the page: " + json.status + " - " + json.message);
			return;
		}
        max = requiresFlags.length;
        for (count = 0; count < max; count++) {
			requiresFlag = requiresFlags[count];
			checkbox = $("<input>");
			checkbox.attr("type", "checkbox");
			checkbox.attr("id", "requires-" + requiresFlag.description);
			checkbox.attr("name", "requires");
			checkbox.attr("value", requiresFlag.description);
			checkbox.change(function () {
				loadLcps();
			});
			section.append(checkbox);
			
			label = $("<label>");
			label.attr("for", "requires-" + requiresFlag.description);
			label.addClass("inline-label");
			label.text("Do these tests require " + requiresFlag.description + "?");
			section.append(label);
			section.append($("<br>"));
        }
        if (!hasTestDef) {
            loadLcps();
        } else {
        	loadTestDef();
        }
    };
    
    var loadRequiresFlags = function (doLoadLcps) {
    	if (doLoadLcps === undefined) {
    		doLoadLcps = false;
    	}
    	$.ajax({
			url: "/synthetic/api/ability-flag?level=2",
			dataType: "json",
			success: generateRequiresFlags,
			error: function (response, status, error) {
				console.log("An AJAX error occurred while attempting to initialize the page: " + status + (error !== undefined ? " - " + error : ""));
			}
		});
    };
    
    var validateForm = function () {
    	var aside = $("#submit-message");
    	var name = $("#name").val();
    	var executionSchedule = $("#execution-schedule").val();
    	var hasName = $.trim(name).length > 0;
    	var hasExecutionSchedule = $.trim(executionSchedule).length > 0;
    	
    	aside.empty();
    	
    	if (!hasName) {
    		aside.text("To create a schedule, you must specify a name.");
    		return false;
    	}
    	
    	if (!hasExecutionSchedule) {
    		aside.text("To create a schedule, you must specify an execution frequency.");
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
    	formData = $("#form-edit-schedule").serialize();
    	console.dir(formData);
    	$.ajax({
			url: "/synthetic/api/test-definition",
			dataType: "json",
			type: "POST",
			data: formData,
			success: function (json) {
				if (json.status !== 200) {
					aside.empty();
					aside.text("An error occurred while attempting to create the schedule: " + json.status + " - " + json.message);
					return;
				}
				window.open("/ruxit/manage-schedules.html?scriptId=" + scriptId, "_self");
			},
			error: function (response, status, error) {
				aside.empty();
				aside.text("An error occurred while attempting to create the schedule: " + status + (error !== undefined ? " - " + error : ""));
			}
		});
    };
    
	var generateScriptHeading = function (json) {
		var script;
		if (json.status !== 200 || json.data.length === 0) {
			handlePageError("The specified script could not be retrieved: " + json.message);
		}
        script = json.data;
        $("#script-name").html("<em>Schedule for " + script.name + "</em>");
	};
	
	var loadScriptHeading = function () {
		if (scriptId === undefined) {
			handlePageError("Unknown value for Script Id");
		}
		$.ajax({
			url: "/synthetic/api/script?scriptId=" + scriptId,
			dataType: "json",
			success: generateScriptHeading,
			error: function (response, status, error) {
				handlePageError("An AJAX error occurred while attempting to initialize the page: " + status + (error !== undefined ? " - " + error : ""));
			}
		});
	};
	
	var loadExecutionSchedule = function () {
		var executionSchedule = $("#execution-schedule");
		executionSchedule.change(function () {
    		var selected = $("#execution-schedule option:selected");
    		executionSchedule.next().children().first().html(selected.text());
    		executionSchedule.focus();
    	});
	};
    
	return {
    	tenantId: tenantId,
    	scriptId: scriptId,
    	testDefId: testDefId,
    	loadScriptHeading: loadScriptHeading,
    	loadExecutionSchedule: loadExecutionSchedule,
        loadRequiresFlags: loadRequiresFlags,
        loadLcps: loadLcps,
        submitForm: submitForm
    };
})();

$(document).ready(function () {
	RUXIT.portal.loadExecutionSchedule();
	RUXIT.portal.loadScriptHeading();
	RUXIT.portal.loadRequiresFlags();
	
	
	$("#script-id").val(RUXIT.portal.scriptId);
	$("#tenant-id").val(RUXIT.portal.tenantId);
	$("#form-edit-schedule").submit(function (e) {
		e.preventDefault();
		RUXIT.portal.submitForm();
		return false;
	});
	

	
	$(document).bind("keypress", function(e){
		   if(e.which === 13) { // return
			   RUXIT.portal.submitForm();
		   }
     });
	$("#name").focus();

});