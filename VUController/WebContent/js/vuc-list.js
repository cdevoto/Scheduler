var RUXIT = RUXIT || {};

console = console || {
	log: function() {},
	error: function () {},
	warn: function () {},
	assert: function () {},
	dir: function () {},
};

RUXIT.vuc = (function () {
	
	var init = function () {
		getVucs();
	},
	
	getVucs = function () {
		$.ajax({
			url: "http://localhost:7080/synthetic/api/vuc",
			dataType: "json",
			success: loadVucs,
			error: function (response, status, error) {
				console.log("An error occurred while attempting to retrieve the VU Controller list:" + status + (error !== undefined ? " - " + error : ""));
			}
		});
	},
	
	loadVucs = function (json) {
		var tBody = $("#vuc-table-body");
		var tr, td, count, max, count2, max2, record, row, requires, vucSupports, vucLcps;
		if (json.status !== 200) {
			console.log("An error occurred while attempting to retrieve the VU Controllers: status=" + json.status + ", message=" + json.message);
		} else {
			if (json.data.length > 0) {
				tBody.empty();
				max = json.data.length;
				for (count = 0; count < max; count++) {
					record = json.data[count];
					row = "vuc-table-row-" + record.id;
					tr = $("<tr>");
					tr.attr("id", row);

					td = $("<td>");
					td.addClass("vuc-table-id");
					td.append(record.id);
					tr.append(td);
					
			        max2 = record.supportsFlags.length;
		        	vucSupports = "";
			        if (max2 > 0) {
			        	for (count2 = 0; count2 < max2; count2++) {
			        		if (count2 > 0) {
			        			vucSupports += ", ";
			        		}
			        		vucSupports += record.supportsFlags[count2].description;
			        	}
			        }
					td = $("<td>");
					td.addClass("vuc-table-supports");
					td.append(vucSupports);
					tr.append(td);

			        max2 = record.lcps.length;
		        	vucLcps = "";
			        if (max2 > 0) {
			        	for (count2 = 0; count2 < max2; count2++) {
			        		if (count2 > 0) {
			        			vucLcps += ", ";
			        		}
			        		vucLcps += record.lcps[count2].name;
			        	}
			        }
					td = $("<td>");
					td.addClass("vuc-table-lcps");
					td.append(vucLcps);
					tr.append(td);

			        (function (id) {
				        tr.click(function () {
				        	window.open("/vuc/vuc.html?id=" + id, "_blank");
				        });
			        })(record.id);
					tBody.append(tr);
				}
			}
		}
	};

	return {
		init: init
	};
})();

$(document).ready(function () {
	RUXIT.vuc.init();
});