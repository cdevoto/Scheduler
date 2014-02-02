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

    var loadScriptTypes = function (json) {
    	var scriptTypeField = $("#script-type");
    	var max, count, scriptTypes, scriptType, option;
		if (json.status !== 200) {
			window.alert("An error occurred while attempting to initialize the page: " + json.status + " - " + json.message);
			return;
		} else if (json.data.length === 0) {
			window.alert("An error occurred while attempting to initialize the page: 500 - No script types defined.");
			return;
		}
		scriptTypes = json.data;
		scriptTypes.map = {};
	    max = json.data.length;
	    for (count = 0; count < max; count++) {
	    	scriptType = scriptTypes[count];
	    	option = $("<option>");
	    	option.attr("value", scriptType.id);
	    	option.text(scriptType.name);
	    	scriptTypeField.append(option);
	    	scriptTypes.map["type-" + scriptType.id] = scriptType;
	    }

    	scriptTypeField.change(function () {
    		var selected = $("#script-type option:selected");
            var section = $("#ability-flags");
    		var value = scriptTypeField.val();
    		var scriptType = scriptTypes.map["type-" + value];
    		var count, max, abilityFlag, checkbox, label;
    		scriptTypeField.next().children().first().html(selected.text());
    		section.empty();
    		if (scriptType === undefined) {
    			return;
    		}
    		max = scriptType.abilityFlags.length;
    		for (count = 0; count < max; count++) {
    			abilityFlag = scriptType.abilityFlags[count];
    			checkbox = $("<input>");
    			checkbox.attr("type", "checkbox");
    			checkbox.attr("id", "requires-" + abilityFlag.description);
    			checkbox.attr("name", "requires");
    			checkbox.attr("value", abilityFlag.description);
    			checkbox.attr("tabIndex", "" + (count + 3));
    			section.append(checkbox);
    			
    			label = $("<label>");
    			label.attr("for", "requires-" + abilityFlag.description);
    			label.addClass("inline-label");
    			label.text("Does this script require " + abilityFlag.description + "?");
    			section.append(label);
    			section.append($("<br>"));
    		}
    		$("#record-script").attr("tabIndex", "" + (count + 3));
    		scriptTypeField.focus();
    	});
    };

    var initializeScriptTypes = function () {
    	$.ajax({
			url: "/synthetic/api/script-type",
			dataType: "json",
			success: loadScriptTypes,
			error: function (response, status, error) {
				console.log("An AJAX error occurred while attempting to initialize the page: " + status + (error !== undefined ? " - " + error : ""));
			}
		});
    };
    
    var submitForm = function () {
    	var aside = $("#submit-message");
    	var name = $("#name").val();
    	var scriptType = $("#script-type").val();
    	var formData = [];
    	aside.empty();
    	if ($.trim(name).length === 0 || $.trim(scriptType).length === 0) {
    		aside.text("To create a script, you must specify a name and a script type.");
    		return;
    	}
    	
    	formData = $("#form-create-script").serialize();
    	$.ajax({
			url: "/synthetic/api/script",
			dataType: "json",
			type: "POST",
			data: formData,
			success: function (json) {
				if (json.status !== 200) {
					aside.empty();
					aside.text("An error occurred while attempting to create the script: " + json.status + " - " + json.message);
					return;
				}
				window.open("/ruxit/manage-scripts.html", "_self");
			},
			error: function (response, status, error) {
				aside.empty();
				aside.text("An error occurred while attempting to create the script: " + status + (error !== undefined ? " - " + error : ""));
			}
		});
    	
    };
    
    return {
    	tenantId: tenantId,
        initializeScriptTypes: initializeScriptTypes,
        submitForm: submitForm
    };
})();

$(document).ready(function () {
	RUXIT.portal.initializeScriptTypes();
	
	$("#tenant-id").val(RUXIT.portal.tenantId);
	$("#name").focus();
	$("#form-create-script").submit(function (e) {
		e.preventDefault();
		RUXIT.portal.submitForm();
		return false;
	});
	
	$(document).bind("keypress", function(e){
		   if(e.which === 13) { // return
			   RUXIT.portal.submitForm();
		   }
     });
	
});