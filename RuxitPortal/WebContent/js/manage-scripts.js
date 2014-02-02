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
	
	var recordTestScript = function () {
		window.open("/ruxit/create-script.html", "_self");
	};
	
	var manageTestSchedule = function (scriptId) {
		window.open("/ruxit/manage-schedules.html?scriptId=" + scriptId, "_self");
	};
	
	
	var deleteTestScript = function (scriptId, scriptName, row) {
		$.ajax({
			url: "/synthetic/api/script?scriptId=" + scriptId,
			type: 'DELETE',
			dataType: "json",
			success: function (json) {
				if (json.status == 200) {
				var r = $("#" + row), tBody;
					r.remove();
					if ($("#script-table-body tr").length === 0) {
						tBody = $("#script-table-body");
						tr = $("<tr>");
						tr.append("<td colspan=2>You currently have no scripts defined.</td>");
						tBody.append(tr);
					}
				} else {
					window.alert("An error occurred while attempting to delete script '" + scriptName + "': " + json.status + " - " + json.message); 
				}
			},
			error: function (response, status, error) {
				window.alert("An error occurred while attempting to delete script '" + scriptName + "':" + status + (error !== undefined ? " - " + error : ""));
			}
		});
	};
	
	var generateScriptTable = function (json) {
		var tBody = $("#script-table-body");
		tBody.empty();
		var tr, td, count, max, script, scriptRow, manageScheduleLink, deleteScriptLink;
		if (json.status !== 200) {
			tr = $("<tr>");
			tr.append("<td colspan=2>An error occured while attempting to retrieve your scripts.</td>");
			tBody.append(tr);
		} else if (json.data.length === 0) {
			tr = $("<tr>");
			tr.append("<td colspan=2>You currently have no scripts defined.</td>");
			tBody.append(tr);
		} else {
			max = json.data.length;
			for (count = 0; count < max; count++) {
				script = json.data[count];
				scriptRow = "script-table-row-" + script.id;
				tr = $("<tr>");
				
				tr.attr("id", scriptRow);
				tr.append($("<td>").append(script.name));
				td = $("<td>");
				td.addClass("table-action");
				tr.append(td);
				manageScheduleLink = $("<a>");
				manageScheduleLink.attr("href", "#");
				manageScheduleLink.append("Manage Schedule");
				(function (scriptId) {
					manageScheduleLink.click(function () {
					    manageTestSchedule(scriptId);
					    return false;
					});
				})(script.id);
				td.append(manageScheduleLink).append(" | ");
				deleteScriptLink = $("<a>");
				deleteScriptLink.attr("href", "#");
				deleteScriptLink.append("Delete");
				(function (scriptId, scriptName, scriptRow) {
					deleteScriptLink.click(function () {
					    deleteTestScript(scriptId, scriptName, scriptRow);
					    return false;
					});
				})(script.id, script.name, scriptRow);
				td.append(deleteScriptLink);
				tBody.append(tr);
			}
		}
	};
	
	var loadTestScripts = function () {
		$.ajax({
			url: "/synthetic/api/script?tenantId=" + tenantId,
			dataType: "json",
			success: generateScriptTable,
			error: function (response, status, error) {
				console.log("An AJAX error occurred while attempting to initialize the page: " + status + (error !== undefined ? " - " + error : ""));
			}
		});
	};
	
	return {
		tenantId: tenantId,
		recordTestScript: recordTestScript,
		manageTestSchedule: manageTestSchedule,
		deleteTestScript: deleteTestScript,
		loadTestScripts: loadTestScripts
	};
})();

$(document).ready(function () {
	$("#record-script").click(RUXIT.portal.recordTestScript);
	RUXIT.portal.loadTestScripts();
	
});