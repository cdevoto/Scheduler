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
	var DEBUG = ($.url().param("debug") === "true");
	DEBUG = true;
	var suspendedMap = {};
	
	var createTestSchedule = function () {
		window.open("/ruxit/edit-schedule.html?scriptId=" + scriptId, "_self");
	};
	
	var editTestSchedule = function (id) {
		window.open("/ruxit/edit-schedule.html?scriptId=" + scriptId + "&testDefinitionId=" + id, "_self");
	};
	
	
	var suspendTestSchedule = function (id, name, suspendLink) {
		var suspended = suspendedMap[id];
 		suspendedMap[id] = suspended = !suspended;
		
		$.ajax({
			url: "/synthetic/api/test-definition?testDefinitionId=" + id + "&suspended=" + suspended,
			dataType: "json",
			success: function (json) {
				if (json.status == 200) {
			 		if (suspended) {
			 			suspendLink.text("Resume");
			 		} else {
			 			suspendLink.text("Suspend");
			 		}
				} else {
					window.alert("An error occurred while attempting to suspend/resume schedule '" + name + "': " + json.status + " - " + json.message); 
				}

			},
			error: function (response, status, error) {
				window.alert("An error occurred while attempting to suspend/resume schedule '" + name + "': " + status + (error !== undefined ? " - " + error : ""));
			}
		});
	};

	var deleteTestSchedule = function (id, name, row) {
		$.ajax({
			url: "/synthetic/api/test-definition?testDefinitionId=" + id,
			type: 'DELETE',
			dataType: "json",
			success: function (json) {
				if (json.status == 200) {
					var r = $("#" + row), tBody;
					r.remove();
					if ($("#schedule-table-body tr").length === 0) {
						tBody = $("#schedule-table-body");
						tr = $("<tr>");
						tr.append("<td colspan=2 style=\"width:480px\">You currently have no schedules defined.</td>");
						tBody.append(tr);
					}
				} else {
					window.alert("An error occurred while attempting to delete schedule '" + name + "': " + json.status + " - " + json.message); 
				}
			},
			error: function (response, status, error) {
				window.alert("An error occurred while attempting to delete schedule '" + name + "': " + status + (error !== undefined ? " - " + error : ""));
			}
		});
	};
	
	var executeInstantTest = function (id, name) {
		$.ajax({
			url: "/synthetic/api/instant-test?testDefinitionId=" + id,
			dataType: "json",
			success: function (json) {
				if (json.status == 200) {
					window.alert("Instant tests executing for schedule '" + name + ".");
				} else {
					window.alert("An error occurred while attempting to execute instant tests for schedule '" + name + "': " + json.status + " - " + json.message); 
				}

			},
			error: function (response, status, error) {
				window.alert("An error occurred while attempting to execute instant tests for schedule '" + name + "': " + status + (error !== undefined ? " - " + error : ""));
			}
		});
	};
	
	var generateScheduleTable = function (json) {
		var tBody = $("#schedule-table-body");
		tBody.empty();
		var tr, td, count, max, record, row, editLink, instantTestLink, deleteLink, suspendLink;
		if (json.status !== 200) {
			tr = $("<tr>");
			tr.append("<td colspan=2 style=\"width:480px\">An error occurred while attempting to retrieve your schedules.</td>");
			tBody.append(tr);
		} else if (json.data.length === 0) {
			tr = $("<tr>");
			tr.append("<td colspan=2 style=\"width:480px\">You currently have no schedules defined.</td>");
			tBody.append(tr);
		} else {
			max = json.data.length;
			for (count = 0; count < max; count++) {
				record = json.data[count];
				row = "schedule-table-row-" + record.id;
				tr = $("<tr>");
				
				tr.attr("id", row);
                // Schedule
				td = $("<td>");
				td.addClass("table-label");
				td.append(record.name);
				tr.append(td);
				
				// Actions
				td = $("<td>");
				td.addClass("table-action");
				tr.append(td);
				
				// Edit
				editLink = $("<a>");
				editLink.attr("href", "#");
				editLink.append("Edit");
				(function (id) {
					editLink.click(function () {
					    editTestSchedule(id);
					    return false;
					});
				})(record.id);
				td.append(editLink).append(" | ");
				
				// Instant Test
				instantTestLink = $("<a>");
				instantTestLink.attr("href", "#");
				instantTestLink.append("Instant Test");
				(function (id, name) {
					instantTestLink.click(function () {
					    executeInstantTest(id, name);
					    return false;
					});
				})(record.id, record.name);
				td.append(instantTestLink).append(" | ");

				// Suspend / Resume
				suspendedMap[record.id] = record.suspended;
				suspendLink = $("<a>");
				suspendLink.attr("href", "#");
				if (record.suspended) {
					suspendLink.append("Resume");
				} else {
					suspendLink.append("Suspend");
				}
				(function (id, name, suspendLink) {
					suspendLink.click(function () {
					    suspendTestSchedule(id, name, suspendLink);
					    return false;
					});
				})(record.id, record.name, suspendLink);
				td.append(suspendLink).append(" | ");
				
				// Delete
				deleteLink = $("<a>");
				deleteLink.attr("href", "#");
				deleteLink.append("Delete");
				(function (id, name, row) {
					deleteLink.click(function () {
					    deleteTestSchedule(id, name, row);
					    return false;
					});
				})(record.id, record.name, row);
				td.append(deleteLink);
				tBody.append(tr);
			}
		}
	};
	
	var loadTestSchedules = function () {
		$.ajax({
			url: "/synthetic/api/test-definition?scriptId=" + scriptId,
			dataType: "json",
			success: generateScheduleTable,
			error: function (response, status, error) {
				console.log("An AJAX error occurred while attempting to initialize the page: " + status + (error !== undefined ? " - " + error : ""));
			}
		});
	};
	
	var generateScriptHeading = function (json) {
		var script;
		if (json.status !== 200 || json.data.length === 0) {
			handlePageError("The specified script could not be retrieved: " + json.message);
		}
        script = json.data;
        $("#script-name").html("<em>" + script.name + "</em>");
	};
	
	var loadScriptHeading = function () {
		if (scriptId === undefined) {
			handlePageError("Unknown value for Script Id");
			return;
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
	
	var handlePageError = function (message) {
		if (DEBUG === true) {
		    console.log(message);
		    return;
		}
		console.log(message);
		//window.open("/ruxit/error-page.html", "_self");
	};
	
	return {
		tenantId: tenantId,
		loadScriptHeading: loadScriptHeading,
		createTestSchedule: createTestSchedule,
		editTestSchedule: editTestSchedule,
		deleteTestSchedule: deleteTestSchedule,
		loadTestSchedules: loadTestSchedules
	};
})();

$(document).ready(function () {
	$("#create-schedule").click(RUXIT.portal.createTestSchedule);
	RUXIT.portal.loadScriptHeading();
	RUXIT.portal.loadTestSchedules();
});