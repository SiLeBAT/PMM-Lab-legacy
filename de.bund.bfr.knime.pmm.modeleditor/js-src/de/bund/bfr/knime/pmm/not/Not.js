bfr_not = function() {
	var not = {
			version: "1.0.0"
	};
	not.name = "Not";
	var input;
	var viewValid = false;

	not.init = function(representation) {
		if (checkMissingData(representation)) {
			return;
		}
		var body = $("body");
		var qfdiv = $('<div class="quickformcontainer">');
		body.append(qfdiv);
		input = $('<input>');
		qfdiv.attr("title", representation.description);
		qfdiv.append('<div class="label">' + representation.label + '</div>');
		qfdiv.append(input);
		input.attr("type", "checkbox");
		var checked = representation.currentValue.boolean;
		input.prop("checked", checked);
		input.blur(callUpdate);
		resizeParent();
		viewValid = true;
	};

	not.value = function() {
		if (!viewValid) {
			return null;
		}
		var viewValue = new Object();
		viewValue.boolean = input.prop("checked");
		return viewValue;
	};
	
	return not;
}();