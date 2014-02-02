var RUXIT = RUXIT || {};

console = console || {
	log: function() {},
	error: function () {},
	warn: function () {},
	assert: function () {},
	dir: function () {},
};

RUXIT.vuc = (function () {
	var vucId = $.url().param('id');
	var firstTestRetrieved = false;
	var poll = false;
	var maxTestsDisplayed = 20;
	var testsRetrieved = 0;
	var init = function () {
		var playPause = $("#play-pause");
		$("#customize-supported").change(function () {
			if ($(this).is(':checked')) {
				$("#supported-attributes").removeClass("hidden");
			} else {
				$("#supported-attributes").addClass("hidden");
			}
		});
		
		if (vucId === undefined) {
			$("#vuc-name").append(" (Unknown)");
		} else {
			$("#vuc-name").append(" " + vucId);
		}
		if (vucId !== undefined) {
			playPause.removeClass("hidden");
			playPause.click(function () {
				if (!poll) {
					poll = true;
					playPause.val("Pause");
				} else {
					poll = false;
					playPause.val("Play");
				}
			});
			setInterval(pollForTests, 500);
			getVucDetails();
		}
	},
	
	getVucDetails = function () {
		$.ajax({
			url: "http://localhost:7080/synthetic/api/vuc?id=" + encodeURIComponent(vucId),
			dataType: "json",
			success: loadVucDetails,
			error: function (response, status, error) {
				console.log("An error occurred while attempting to retrieve the VU Controller details '" + name + "':" + status + (error !== undefined ? " - " + error : ""));
			}
		});
	},
	
	loadVucDetails = function (json) {
        var vuc, max, count, vucLcps, vucSupports;
		if (json.status !== 200 || json.data.length === 0) {
			console.log("The specified VU controller could not be retrieved: " + json.message);
		}
        vuc = json.data;
        max = vuc.lcps.length;
        if (max > 0) {
        	vucLcps = $("#vuc-lcps");
        	vucLcps.removeClass("hidden");
        	for (count = 0; count < max; count++) {
        		if (count > 0) {
        			vucLcps.append("&nbsp;&nbsp;&nbsp;");
        		}
        		vucLcps.append(vuc.lcps[count].name);
        	}
        	
        }
        max = vuc.supportsFlags.length;
        if (max > 0) {
        	vucSupports = $("#vuc-supports");
        	vucSupports.removeClass("hidden");
        	vucSupports.append("Supports: ");
        	for (count = 0; count < max; count++) {
        		if (count > 0) {
        			vucSupports.append(", ");
        		}
        		vucSupports.append(vuc.supportsFlags[count].description);
        	}
        	
        }

	},
	
	pollForTests = function () {
		if (!poll) {
			$("#test-table-body tr.hilite").each(function () {
				$(this).removeClass("hilite");
			});
			return;
		}
		var params = "vucId=" + encodeURIComponent(vucId);
		var maxTests = $("#max-tests").val();
		if ($.trim(maxTests).length > 0) {
			params += "&maxTests=" + encodeURIComponent(maxTests);
		}
		if ($("#customize-supported").is(':checked')) {
			var supportsF = 0;
			if ($("#ipv4").is(':checked')) {
				supportsF |= 1;
			}
			if ($("#ipv6").is(':checked')) {
				supportsF |= 2;
			}
			if ($("#sms").is(':checked')) {
				supportsF |= 4;
			}
			params += "&supportsF=" + encodeURIComponent(supportsF);
		}
		$.ajax({
			url: "http://localhost:7080/synthetic/dispatch/poll?" + params,
			dataType: "json",
			success: loadTests,
			error: function (response, status, error) {
				console.log("An error occurred while attempting to retrieve tests '" + name + "':" + status + (error !== undefined ? " - " + error : ""));
			}
		});
	},
	
	loadTests = function (json) {
		var tBody = $("#test-table-body");
		var tr, count, max, count2, max2, record, row, requires;
		$("#test-table-body tr.hilite").each(function () {
			$(this).removeClass("hilite");
		});
		if (json.status !== 200) {
			console.log("An error occurred while attempting to retrieve your tests: status=" + json.status + ", message=" + json.message);
		} else {
			if (json.data.length > 0) {
				testsRetrieved += 1;
				if (!firstTestRetrieved) {
					firstTestRetrieved = true;
					tBody.empty();
				}
				max = json.data.length;
				for (count = 0; count < max; count++) {
					record = json.data[count];
					row = "test-table-row-" + record.testId;
					tr = $("<tr>");
					tr.addClass("hilite");
					tr.attr("id", row);
					tr.append($("<td>").append($.format.date(new Date(), "yyyy-MM-dd HH:mm:ss")));
					tr.append($("<td>").append(record.testId));
					tr.append($("<td>").append(record.tenantId));
					tr.append($("<td>").append(record.scriptId));
					tr.append($("<td>").append(record.scriptLastModified));
					tr.append($("<td>").append(record.testDefinitionId));
					tr.append($("<td>").append(record.testDefinitionLastModified));
					tr.append($("<td>").append(record.priority));
					
					requires = "";
					max2 = record.requires.length;
					for (count2 = 0; count2 < max2; count2++) {
						if (count2 > 0) {
							requires += ", ";
						}
					    requires +=	record.requires[count2].description;
					}
					tr.append($("<td>").append(requires));
					
	
					tBody.prepend(tr);
					if (testsRetrieved >= maxTestsDisplayed) {
						$('#test-table-body tr:last-child').remove();
					}
				}
			}
		}
	};

	return {
		vucId: vucId,
		init: init
	};
})();

$(document).ready(function () {
	RUXIT.vuc.init();
});