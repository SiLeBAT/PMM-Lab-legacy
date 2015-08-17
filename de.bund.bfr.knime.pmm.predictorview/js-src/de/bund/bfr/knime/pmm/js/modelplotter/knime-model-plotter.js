bfr_model_plotter = function() {

	var modelPlotter = {
			version: "1.0.0"
	};
	modelPlotter.name = "Model Plotter";
	
	var plotterValue;
	
	modelPlotter.init = function(representation, value) {
		//alert(JSON.stringify(representation));
		plotterValue = value;
		
		var body = document.getElementsByTagName("body")[0];
		body.setAttribute("style", "width:100%; height:100%;");
		
		var layoutContainer = document.createElement("div");
		layoutContainer.setAttribute("style", "width:850px; height:650px;");
		body.appendChild(layoutContainer);
		
		var h = document.createElement("h1");
		h.innerHTML = representation.chartTitle;
		layoutContainer.appendChild(h);
		
		var div = document.createElement("div");
		div.setAttribute("id", "box");
		div.setAttribute("class", "jxgbox");
		div.setAttribute("style", "width:800px; height:600px;");
		layoutContainer.appendChild(div);
		
		// Initialize JSX board to draw function graphs, etc.
		var minXAxis = representation.minXAxis;
		var maxXAxis = representation.maxXAxis;
		var minYAxis = representation.minYAxis;
		var maxYAxis = representation.maxYAxis;		
		var b = JXG.JSXGraph.initBoard('box', {boundingbox: [minXAxis, maxYAxis, maxXAxis, minYAxis], axis:false});
		
		var xunit = "[" + representation.xUnit + "]";
		var yunit = "[" + representation.yUnit + "]";
		
		// Set Y axis, ticks, labels
		var yaxis = b.create('axis', [[0, 0], [0, 1]], {name:yunit, withLabel: true, 
				ticks: {insertTicks: true, ticksDistance: 1, label: {offset: [-20, -20]}}});
			 yaxis.defaultTicks.ticksFunction = function () { return 5; };

	    // Set X axis, ticks, labels
	    var xaxis = b.create('axis', [[0, 0], [1, 0]], {name:xunit, withLabel: true, 
				ticks: {insertTicks: true, ticksDistance: 1, label: {offset: [-20, -20]}}});
			 xaxis.defaultTicks.ticksFunction = function () { return 5; };
		
		var variables = [];
		
		// Prepare string of function f of time
		var functionStr = 'f(Time';
		
		// Append one slider for each function variable
		// Append variable name to function string, separated by ","		
		for (var i = 0; i < representation.variables.length; i++) {
			var v = representation.variables[i];
			if (v.name != 'Time') {				
				variables.push(b.create('slider', [[50,-2 - i], [80,-2 - i], [v.min, v.def, v.max]],
						{name:v.name, point1: {frozen: true}, point2: {frozen: true}}));
				functionStr += ", " + v.name;
			}
		}
		
		// Close parameter brackets of function and add function term
		functionStr += ") = " + representation.func;
		representation.constants.Y0 = representation.y0; 
		
		// Prepare myMath object to call math functions
		// Note that due to math.js the root math object can be either "mathjs" or "math",
		// which depends on the used browser. 		
		var myMath;
		if (typeof define === 'function' && define.amd) {
			myMath = mathjs;
		} else {
			myMath = math;
		}
		
		// Set all constants of the given model representation
		myMath.import(representation.constants);
		
		// CUSTOMIZED FUNCTIONS
		myMath.import({
		  ln: function (x) { return myMath.log(x); },
		  log10: function (x) { return myMath.log(x)/myMath.log(10); }
		});
		
		// Save max value of x axis
		var maxXValue = b.attr.boundingbox[2] + 10;		
		
		// Create js function from function string with time and all given parameters as input
		// variables of the function
		var baseModelFunction = myMath.eval(functionStr);
		
		// Create js function ONLY WITH TIME as input variable of the function. As values of all
		// other input variables the slider values are set
		var timeModelFunction = function(Time){
		 	var varValues = [Time];
		 	for (var i = 0; i < variables.length; i++) {
		 		varValues.push(variables[i].Value());
		 	}		 
		 	return baseModelFunction.apply(null, varValues);
	 	 };		
		
	 	// Create graph of function with time parameter only
		var fg = b.create('functiongraph', [timeModelFunction, 0, maxXValue]);
		
		// On zoom change event remove current function graph and create/draw new function graph 
		// with new max value of x axis
		b.on('boundingbox', function() {
			maxXValue = b.plainBB[2] + 10;
			b.removeObject(fg);
			fg = b.create('functiongraph', [timeModelFunction, 0, maxXValue]);		
		});		
		
		// Auto resize view when shown in WebPortal
		if (parent != undefined && parent.KnimePageLoader != undefined) {
			   parent.KnimePageLoader.autoResize(window.frameElement.id);
		}
	}
	
	modelPlotter.validate = function() {
		return true;
	}
	
	modelPlotter.setValidationError = function() { }
	
	modelPlotter.getComponentValue = function() { 
		return plotterValue;
	}
	
	return modelPlotter;	
}();

