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
		
		var h = document.createElement("h1");
		h.innerHTML = representation.chartTitle;
		body.appendChild(h);
		
		var div = document.createElement("div");
		div.setAttribute("id", "box");
		div.setAttribute("class", "jxgbox");
		div.setAttribute("style", "width:800px; height:600px;");
		body.appendChild(div);
		
		var b = JXG.JSXGraph.initBoard('box', {boundingbox: [-10, 20, 100, -10], axis:false});
		
		var yaxis = b.create('axis', [[0, 0], [0, 1]], {name:'log10(count/g)', withLabel: true, 
				ticks: {insertTicks: true, ticksDistance: 1, label: {offset: [-20, -20]}}});
			 yaxis.defaultTicks.ticksFunction = function () { return 5; };

	   var xaxis = b.create('axis', [[0, 0], [1, 0]], {name:'h', withLabel: true, 
				ticks: {insertTicks: true, ticksDistance: 1, label: {offset: [-20, -20]}}});
			 xaxis.defaultTicks.ticksFunction = function () { return 5; };
		
		var variables = [];
		var functionStr = 'f(Time';
		
		for (var i = 0; i < representation.variables.length; i++) {
			var v = representation.variables[i];
			if (v.name != 'Time') {				
				variables.push(b.create('slider', [[10,-2 - i],[40,-2 - i],[v.min,v.def,v.max]], {name:v.name}));
				functionStr += ", " + v.name;
			}
		}
		
		functionStr += ") = ";
		var func = representation.func;
		representation.constants.Y0 = representation.y0; 
		
		math.import(representation.constants);
		
		// CUSTOMIZED FUNCTIONS
		math.import({
		  ln: function (x) { return math.log(x); },
		  log10: function (x) { return math.log(x)/math.log(10); }
		});
		
		f = math.eval(functionStr + func);
		b.create('functiongraph', [function(Time){
			 	var varValues = [Time];
			 	for (var i = 0; i < variables.length; i++) {
			 		varValues.push(variables[i].Value());
			 	}
			 
			 	return f.apply(null, varValues);
		 	 }, 
		0, 90]);
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

