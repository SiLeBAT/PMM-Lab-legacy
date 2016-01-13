bfr_togglecase = function() {
	var toggleCase = {
			version: "1.0.0"
	};
	toggleCase.name = "Toggle case";
	var input;
	var errorMessage;
	var viewRepresentation;
	var viewValid = false;
	
	toggleCase.init = function(representation) {
		if (checkMissingData(representation)) {
			return;
		}

		viewRepresentation = representation;
		
		input = $('<input>');
		input.attr("type", "text");
		input.width(400);
		input.val(representation.currentValue.string);
		input.blur(callUpdate);
		
		errorMessage = $('<span>');
		errorMessage.css('display', 'none');
		errorMessage.css('color', 'red');
		errorMessage.css('font-style', 'italic');
		errorMessage.css('font-size', '75%');

		var qfdiv = $('<div class="quickformcontainer">');
		qfdiv.attr("title", representation.description);
		qfdiv.append(input);
		qfdiv.append('<br>');
		qfdiv.append(errorMessage);
		
		var body = $('body');
		body.append(qfdiv);
		
		resizeParent();
		viewValid = true;
	};
	
	toggleCase.validate = function() {
		return viewValid ? true : false;
	};
	
	toggleCase.setValidationErrorMessage = function(message) {
		if (!viewValid) {
			return;
		}
		if (message === null) {
			errorMessage.text('');
			errorMessage.css('display', 'none');
		} else {
			errorMessage(message);
			errorMessage.css('display', 'inline');
		}
		resizeParent();
	};
	
	toggleCase.value = function() {
		if (!viewValid) {
			return null;
		}
		var viewValue = new Object();
		viewValue.string = input.val();
		return viewValue;
	};

	return toggleCase;
}();