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
	
	var createMaintSchedule = function () {
		window.open("/ruxit/edit-maint-schedule.html", "_self");
	};
	
	var editMaintSchedule = function (scheduleId) {
		window.open("/ruxit/edit-maint-schedule.html?scheduleId=" + scheduleId, "_self");
	};
	
	
	var deleteMaintSchedule = function (scheduleId, scheduleName, row) {
		$.ajax({
			url: "/synthetic/api/schedule?scheduleId=" + scheduleId,
			type: 'DELETE',
			dataType: "json",
			success: function (json) {
				if (json.status == 200) {
				    var r = $("#" + row), tBody;
					r.remove();
					if ($("#maint-schedule-table-body tr").length === 0) {
						tBody = $("#maint-schedule-table-body");
						tr = $("<tr>");
						tr.append("<td colspan=2>You currently have no maintenance schedules defined.</td>");
						tBody.append(tr);
					}
				} else {
					window.alert("An error occurred while attempting to delete maintenance schedule '" + scheduleName + "': " + json.status + " - " + json.message); 
				}
			},
			error: function (response, status, error) {
				window.alert("An error occurred while attempting to delete maintenance schedule '" + scheduleName + "':" + status + (error !== undefined ? " - " + error : ""));
			}
		});
	};
	
	var generateMaintScheduleTable = function (json) {
		var tBody = $("#maint-schedule-table-body");
		tBody.empty();
		var tr, td, count, max, schedule, scheduleRow, editLink, deleteLink;
		if (json.status !== 200) {
			tr = $("<tr>");
			tr.append("<td colspan=2>An error occured while attempting to retrieve your maintenance schedules.</td>");
			tBody.append(tr);
		} else if (json.data.length === 0) {
			tr = $("<tr>");
			tr.append("<td colspan=2>You currently have no maintenance schedules defined.</td>");
			tBody.append(tr);
		} else {
			max = json.data.length;
			for (count = 0; count < max; count++) {
				schedule = json.data[count];
				scheduleRow = "maint-schedule-row-" + schedule.id;
				tr = $("<tr>");
				
				tr.attr("id", scheduleRow);
				tr.append($("<td>").append(schedule.name));
				td = $("<td>");
				td.addClass("table-action");
				tr.append(td);
				editLink = $("<a>");
				editLink.attr("href", "#");
				editLink.append("Edit");
				(function (scheduleId) {
					editLink.click(function () {
					    editMaintSchedule(scheduleId);
					    return false;
					});
				})(schedule.id);
				td.append(editLink).append(" | ");
				deleteLink = $("<a>");
				deleteLink.attr("href", "#");
				deleteLink.append("Delete");
				(function (scheduleId, scheduleName, scheduleRow) {
					deleteLink.click(function () {
					    deleteMaintSchedule(scheduleId, scheduleName, scheduleRow);
					    return false;
					});
				})(schedule.id, schedule.name, scheduleRow);
				td.append(deleteLink);
				tBody.append(tr);
			}
		}
	};
	
	var loadMaintSchedules = function () {
		$.ajax({
			url: "/synthetic/api/schedule?tenantId=" + encodeURIComponent(tenantId) + "&isMaintenance=true",
			dataType: "json",
			success: generateMaintScheduleTable,
			error: function (response, status, error) {
				console.log("An AJAX error occurred while attempting to initialize the page: " + status + (error !== undefined ? " - " + error : ""));
			}
		});
	};
	
	return {
		tenantId: tenantId,
		createMaintSchedule: createMaintSchedule,
		editMaintSchedule: editMaintSchedule,
		deleteMaintSchedule: deleteMaintSchedule,
		loadMaintSchedules: loadMaintSchedules
	};
})();

$(document).ready(function () {
	$("#create-maint-schedule").click(RUXIT.portal.createMaintSchedule);
	RUXIT.portal.loadMaintSchedules();
	
});