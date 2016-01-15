pmm_plotter = function() {


	var modelPlotter = {
			version: "1.0.0"
	};
	modelPlotter.name = "Model Plotter";
	
	var plotterValue;
	var plotterRep;
	var variableSliders;
	var variableIndices;
	var functionGraph;
	var jsxBoard;
	
	var globalNumber = 1;
	var modelObjects = [];
	var colorsArray = [];
	
	var msgAdd = "Hinzufügen";
	var msgChoose = "Modell auswählen";
	
	var buttonWidth = "width: 250px;";
	var sliderWidth = "width: 190px;";
	var sliderInputWidth = "width: 40px;";
	var sliderBoxHeight = "height: 33px;";
	var sliderStepSize = 0.0001;
	var totalHeight = "height: 800px;";
	var plotWidth = 600;
	var plotHeight = 400;
	
	modelPlotter.init = function(representation, value) {

		plotterValue = value;
		plotterRep = representation;
		
		var body = document.getElementsByTagName("body")[0];
		body.setAttribute("style", "background: #fdfdfd; width:100%; height:100%; font-family:Verdana,Helvetica,sans-serif; font-size:12px; overflow:hidden;");

		/*
		 * new plotter layout
		 */
		var layoutWrapper = document.createElement("div");
		layoutWrapper.setAttribute("id", "layoutWrapper");
		layoutWrapper.setAttribute("style", "width:900px;");
		var leftWrapper = document.createElement("div");
		leftWrapper.setAttribute("id", "leftWrapper");
		leftWrapper.setAttribute("style", "width:300px; display: block; float: left;" + totalHeight);
		var rightWrapper = document.createElement("div");
		rightWrapper.setAttribute("id", "rightWrapper");
		rightWrapper.setAttribute("style", "width:600px; display: block; float: left;" + totalHeight);
		layoutWrapper.appendChild(leftWrapper);
		layoutWrapper.appendChild(rightWrapper);
		
		// selection
		var modelSelectionMenu = document.createElement("select");
		modelSelectionMenu.innerHTML = msgChoose;
		modelSelectionMenu.setAttribute("id", "selectModel");
		modelSelectionMenu.setAttribute("name", "selectModel");
		modelSelectionMenu.setAttribute("style" , buttonWidth);
		modelSelectionMenu.setAttribute("required");
		leftWrapper.appendChild(modelSelectionMenu);
		
		
		// selection + options
		var option0 = document.createElement("option");
		option0.setAttribute("hidden");
		option0.setAttribute("disabled");
		option0.setAttribute("selected");
		option0.setAttribute("value", "");
		option0.innerHTML = msgChoose;
		modelSelectionMenu.appendChild(option0);
		
		var optGroup1 = document.createElement("optgroup");
		optGroup1.setAttribute("label", "Typ A");
		optGroup1.setAttribute("id", "optGroupA");
		modelSelectionMenu.appendChild(optGroup1);

		var optGroup2 = document.createElement("optgroup");
		optGroup2.setAttribute("label", "Typ B");
		modelSelectionMenu.appendChild(optGroup2);
		
		var option3 = document.createElement("option");
		option3.innerHTML = "Beispiel 3";
		optGroup2.appendChild(option3);
		
		var option4 = document.createElement("option");
		option4.innerHTML = "Beispiel 4";
		optGroup2.appendChild(option4);
		
		
		// add button
		var addButton = document.createElement("button");
		addButton.innerHTML = msgAdd;
		addButton.setAttribute("id", "addButton");
		addButton.setAttribute("style" , buttonWidth);
		addButton.addEventListener("click", function() { 
			addFunctionFromSelection(); 
			});
		leftWrapper.appendChild(addButton);
		
		// slider wrapper
		var sliderWrapper = document.createElement("div");
		sliderWrapper.setAttribute("id", "sliderWrapper");
		sliderWrapper.setAttribute("style" , buttonWidth);
		leftWrapper.appendChild(sliderWrapper);
		
		// body
		body.appendChild(layoutWrapper);
		// --- //
		
		
		/*
		 * new plotter
		 */
		var d3Plot = document.createElement("div");
		d3Plot.setAttribute("id", "d3plotter");
		rightWrapper.appendChild(d3Plot);
				
		drawD3Plot();
		
		// dynamic options
		addSelectOption(plotterValue.dbuuid, plotterValue.modelName);
		addSelectOption("123", "Beispiel");
		/******/

		
		var table = document.createElement("table");
		var row1 = document.createElement("tr");
		var td1 = document.createElement("td");
		row1.appendChild(td1);
		var row2 = document.createElement("tr");
		var td2 = document.createElement("td");
		td2.setAttribute("align", "right");
		row2.appendChild(td2);
		table.appendChild(row1);
		table.appendChild(row2);
		body.appendChild(table);

		
		var layoutContainer = document.createElement("div");
		layoutContainer.setAttribute("style", "width:500px; height:350px;");
		td1.appendChild(layoutContainer);
		
		var constantsBox = document.createElement("div");
		constantsBox.setAttribute("style", "border-style:solid; border-width:1px; width:60%; text-align:left; padding:10px;");
		td2.appendChild(constantsBox);
		
		var constantsHeadline = document.createElement("h3");
		constantsHeadline.innerHTML = "Model constants";
		constantsHeadline.setAttribute("style", "margin:5px;");
		constantsBox.appendChild(constantsHeadline);
		
		var selectBox = document.createElement("select");
		selectBox.setAttribute("id", "funcConstants");
		selectBox.setAttribute("style", "margin:5px;");
		selectBox.addEventListener("change", function() { constantSelected(); });
		constantsBox.appendChild(selectBox);
		
		var textInput = document.createElement("input");
		textInput.setAttribute("id", "funcConstantValue");
		textInput.setAttribute("type", "text");
		textInput.setAttribute("size", "7");
		textInput.setAttribute("style", "margin:5px;");
		textInput.addEventListener("change", function() { checkNumberInput(); });
		constantsBox.appendChild(textInput);
		
		var applyButton = document.createElement("button");
		applyButton.innerHTML = "Apply me";
		applyButton.setAttribute("id", "funcConstantValueChange");
		applyButton.setAttribute("style", "margin:5px;");
		applyButton.addEventListener("click", function() { applyNewValue(); });
		constantsBox.appendChild(applyButton);
		
		var h = document.createElement("h1");
		h.innerHTML = "Plotter Test";//plotterValue.chartTitle;
		layoutContainer.appendChild(h);
		
		var div = document.createElement("div");
		div.setAttribute("id", "box");
		div.setAttribute("class", "jxgbox");
		div.setAttribute("style", "width:400px; height:350px;");
		layoutContainer.appendChild(div);
		
	    // add constants in view representation to select box as options
	    var constantsSelectBox = document.getElementById('funcConstants');
		for (var c in plotterValue.constants) {
		    var opt = document.createElement('option');
			opt.value = c;
			opt.innerHTML = c;
			constantsSelectBox.appendChild(opt);
		}
		
		// set value in text field to value of selected constant
		constantSelected();
		
		// Initialize JSX board to draw function graphs, etc.
		var minXAxis = plotterValue.minXAxis;
		var maxXAxis = plotterValue.maxXAxis;
		var minYAxis = plotterValue.minYAxis;
		var maxYAxis = plotterValue.maxYAxis;		
		jsxBoard = JXG.JSXGraph.initBoard('box', {boundingbox: [minXAxis, maxYAxis, maxXAxis, minYAxis], axis:false});
		
		var xunit = "[" + plotterValue.xUnit + "]";
		var yunit = "[" + plotterValue.yUnit + "]";
		
		// Set Y axis, ticks, labels
		var yaxis = jsxBoard.create('axis', [[0, 0], [0, 1]], {name:yunit, withLabel: true, 
				ticks: {insertTicks: true, ticksDistance: 1, label: {offset: [-20, -20]}}});
			 yaxis.defaultTicks.ticksFunction = function () { return 5; };

	    // Set X axis, ticks, labels
	    var xaxis = jsxBoard.create('axis', [[0, 0], [1, 0]], {name:xunit, withLabel: true, 
				ticks: {insertTicks: true, ticksDistance: 1, label: {offset: [-20, -20]}}});
			 xaxis.defaultTicks.ticksFunction = function () { return 5; };
			 
		variableSliders = [];
		variableIndices = [];
		// Append one slider for each function variable		
		for (var i = 0; i < plotterValue.variables.length; i++) {
			var v = plotterValue.variables[i];
			if (v.name != 'Time') {				
				variableSliders.push(jsxBoard.create('slider', [[50,-2 - i], [80,-2 - i], [v.min, v.def, v.max]],
						{name:v.name, point1: {frozen: true}, point2: {frozen: true}}));
				variableIndices.push(i);
			}
		}		
				
		plotterValue.constants.Y0 = plotterValue.y0;
		
		// Creates time based function and updates plottable model graph 
		updateFunctionGraph();
		
		// On zoom change event remove current function graph and create/draw new function graph 
		// with new max value of x axis
		jsxBoard.on('boundingbox', function() {
			maxXValue = jsxBoard.plainBB[2] + 10;
			jsxBoard.removeObject(functionGraph);
			functionGraph = jsxBoard.create('functiongraph', [timeModelFunction, 0, maxXValue]);		
		});		
		
		// Auto resize view when shown in WebPortal
		if (parent !== undefined && parent.KnimePageLoader !== undefined) {
			   parent.KnimePageLoader.autoResize(window.frameElement.id);
		}
		
		/*
		 * jQueryUI functions
		 */
		$(function() {
			  $("button").button({
			      icons: {
			        primary: "ui-icon-plus"
			      }
			  });
			  $("select").selectmenu();
		});
		/***/
	};
	
	

	// read selected constant from select box and show value of constant in text field 
	constantSelected = function() {
		var constantsSelectBox = document.getElementById('funcConstants');
		var constant = constantsSelectBox.options[constantsSelectBox.selectedIndex].value;
		document.getElementById('funcConstantValue').value = plotterValue.constants[constant];
	};
	
	// checks if input of text field is number
	checkNumberInput = function() {
		var constantsSelectBox = document.getElementById('funcConstants');
		var constant = constantsSelectBox.options[constantsSelectBox.selectedIndex].value;
		var newValueField = document.getElementById('funcConstantValue');
		var newValue = newValueField.value;
		
		var newNumberValue = parseFloat(newValue);
		if (isNaN(newNumberValue)) {
			newValueField.style.background = "red";
		} else {
			newValueField.style.background = "white";
		}
	};
	
	applyNewValue = function() {	
		var constantsSelectBox = document.getElementById('funcConstants');
		var constant = constantsSelectBox.options[constantsSelectBox.selectedIndex].value;
		var newValue = document.getElementById('funcConstantValue').value;
		
		var newNumberValue = parseFloat(newValue);
		if (!isNaN(newNumberValue)) {
			plotterValue.constants[constant] = newNumberValue;
			if (constant == "Y0") {
				// set new Y0 value to view value
				plotterValue.y0 = newNumberValue;
			}
			
			jsxBoard.removeObject(functionGraph);
			updateFunctionGraph();
		}
	};
	
	/*
	 * adds a new option to the selection menu
	 */
	function addSelectOption(dbuuid, modelName)
	{
		// TODO: dynamisches Mappen von Typen zu Gruppen
		var option = document.createElement("option");
		option.setAttribute("value", dbuuid);
		option.innerHTML = "(" + dbuuid + ") " + modelName;
		
		var group = document.getElementById("optGroupA");
		group.appendChild(option);
	}

	/*
	 * parse functiom from model and modify it according to framework needs
	 */
	function prepareFunction(functionString) {
		// replace "Time" with "x" using regex
		// gi: global, case-insensitive
		return functionString.replace(/Time/gi, "x");
	}
	
	/*
	 * 1. determines the selected model from the selection menu
	 * 2. gets the model data
	 * 3. calls addFunctionObject() with the model data
	 */
	function addFunctionFromSelection()
	{
		// get the selection
		var selectMenu = document.getElementById("selectModel");
		var selection = selectMenu.options[selectMenu.selectedIndex].value;

		// get the model data
		if(plotterValue.dbuuid == selection)
		{
			plotterValue.constants.Y0 = plotterValue.y0; // set the value from the settings here
			var functionAsString = prepareFunction(plotterValue.func);
			var functionConstants = plotterValue.constants;
			var dbuuid = plotterValue.dbuuid;
			// call subsequent method
			addFunctionObject(dbuuid, functionAsString, functionConstants);
		}
		else // to be removed
		{
			globalNumber++; 
			addFunctionObject(globalNumber, "x-" + globalNumber, null);
		}
	}
	
	/*
	 * adds a function to the functions array and redraws the plot
	 * @param dbuuid
	 * @param functionAsString the function string as returend by prepareFunction()
	 * @param the function constants as an array 
	 */
	function addFunctionObject(dbuuid, functionAsString, functionConstants)
	{
		var color = getNextColor(); // functionPlot provides 9 colors
		var maxRange = 100; // obligatoric for the range feature
		var range = [0, maxRange];
		
		var funcObj = { 
			 dbuuid: dbuuid,
			 fn: functionAsString,
			 scope: functionConstants,
			 color: color,
			 range: range,
			 skipTip: false
		};
		modelObjects.push(funcObj);
		// update plot after adding new function
		generateParameterSliders();
		drawD3Plot();
	}

	
    /*
     * 
     */
	function generateParameterSliders()
	{
	    var sliderWrapper = document.getElementById("sliderWrapper");
	    
	    for (var modelIndex in modelObjects)
	    {
	    	var constants = modelObjects[modelIndex].scope;
	    	if(constants)
	    	{
		    	$.each(constants, function(constant, value)
				{
		    		var sliderId = "slider_" + constant.toUpperCase();
		    		if(document.getElementById(sliderId))
		    		{
		    			// do not add known parameters twice
		    			return true;
		    		}
		    		
		    		/*
		    		 * the layout structue is as follows:
		    		 * > sliderBox
		    		 * >> sliderLabel
		    		 * >> slider | >> sliderValueDiv
		    		 * 			   >>> sliderValue
		    		 */
				    var sliderBox = document.createElement("p");
				    sliderBox.setAttribute("id", sliderId);
				    sliderBox.setAttribute("style" , buttonWidth + sliderBoxHeight);
				    sliderWrapper.appendChild(sliderBox);
				    
					var sliderLabel = document.createElement("div");
					sliderLabel.innerHTML = constant;
					sliderLabel.setAttribute("style" , "font-weight: bold;");
					sliderBox.appendChild(sliderLabel);
					
					var slider = document.createElement("div");
					slider.setAttribute("style" , sliderWidth + "display: block; float: left; margin: 3px");
					sliderBox.appendChild(slider);
									    
					var sliderValueDiv = document.createElement("div");
					sliderValueDiv.setAttribute("style" , sliderInputWidth + "display: block; float: left;");
					sliderBox.appendChild(sliderValueDiv);
					
					var sliderValue = document.createElement("input");
					sliderValue.setAttribute("type", "text");
					sliderValue.setAttribute("style" , sliderInputWidth + "font-weight: bold;");
					sliderValueDiv.appendChild(sliderValue);
					
					$(sliderValue).val(value);
				    $(slider).slider({
				    	value: value,
				    	step: sliderStepSize,
				    	// changing the slider changes the input field
				        slide: function( event, ui ) {
				            $(sliderValue).val( ui.value );
				            window.setTimeout(updateFunctionConstant(constant, ui.value), 100);
				        }
				    });
					$(sliderValue).change(function() {
						// changing the input field changes the slider
						$(slider).slider("value", this.value);
						window.setTimeout(updateFunctionConstant(constant, this.value), 100);
					});
					$(sliderValue).keyup(function() {
						$(this).change();
					});
				});
	    	}
	    }
	}

	/* 
	 * 
	 */
	function updateFunctionConstant(constant, value)
	{
		if(value == "0")
			value = 0.0001;
		for(var modelIndex in modelObjects)
		{
			var constants = modelObjects[modelIndex].scope;
			if(constants[constant])
				constants[constant] = value;
		}
		drawD3Plot();
	}

	/*
	 * redraws the plot and all graphs based on the modelObjects array and its data
	 */
	function drawD3Plot() 
	{
		functionPlot({
			  target: '#d3plotter',
			  xDomain: [plotterValue.minXAxis, plotterValue.maxXAxis],
			  yDomain: [plotterValue.minYAxis, plotterValue.maxYAxis],
			  xLabel: plotterValue.xUnit,
			  yLabel: plotterValue.yUnit,
			  witdh: plotWidth,
			  height: plotHeight,
			  tip: 
			  {
			    xLine: true,    // dashed line parallel to y = 0
			    yLine: true,    // dashed line parallel to x = 0
			    renderer: function (x, y, index) {
			      return y;
				}
			  },
			  data: modelObjects
		});
	}
	
	/*
	 * color iterator based on the colors delivered by functionPlot (10 colors)
	 */
	function getNextColor()
	{
		if(colorsArray.length <= 0)
			colorsArray = functionPlot.globals.COLORS.slice(0); // clone function plot colors array
		return colorsArray.shift();
	}
	/*******/
	
	
	
	function createFunctionStr() {
		var functionStr = 'f(Time';
		// Append variable name to function string, separated by ","		
		for (var i = 0; i < plotterValue.variables.length; i++) {
			var v = plotterValue.variables[i];
			if (v.name != 'Time') {				
				functionStr += ", " + v.name;
			}
		}
		// Close parameter brackets of function and add function term
		functionStr += ") = " + plotterValue.func;
		
		return functionStr;
	}
	
	function createMath() {
		// Note that due to math.js the root math object can be either "mathjs" or "math",
		// which depends on the used browser.		
		var myMath;
		if (typeof define === 'function' && define.amd) {
			myMath = mathjs;
		} else {
			myMath = math;
		}
		
		// Set all constants of the given model representation
		myMath.import(plotterValue.constants, { override: true });
		
		// CUSTOMIZED FUNCTIONS
		myMath.import({
		  ln: function (x) { return myMath.log(x); },
		  log10: function (x) { return myMath.log(x)/myMath.log(10); }
		});
		
		return myMath;
	}
	
	function createTimeBasedModelFunction(baseModelFunction) {
		var timeModelFunction = function(Time){
		 	var varValues = [Time];
		 	for (var i = 0; i < variableSliders.length; i++) {
		 		varValues.push(variableSliders[i].Value());
		 	}		 
		 	return baseModelFunction.apply(null, varValues);
	 	 };	
	 	 return timeModelFunction;
	}
	
	function updateFunctionGraph() {
		// Save max value of x axis
		var maxXValue = jsxBoard.attr.boundingbox[2] + 10;
		
		// Prepare string of function f of time
		var functionStr = createFunctionStr();
		plotterValue.functionFull = functionStr;
		
		// Prepare myMath object to call math functions 		
		var myMath = createMath();
		
		// Create js function from function string with time and all given parameters as input
		// variables of the function
		var baseModelFunction = myMath.eval(functionStr);
		
		// Create js function ONLY WITH TIME as input variable of the function. As values of all
		// other input variables the slider values are set
		var timeModelFunction = createTimeBasedModelFunction(baseModelFunction);
		
	 	// Create graph of function with time parameter only
		functionGraph = jsxBoard.create('functiongraph', [timeModelFunction, 0, maxXValue]);
	}
	
	modelPlotter.validate = function() {
		return true;
	};
	
	modelPlotter.setValidationError = function() { };
	
	modelPlotter.getComponentValue = function() {
		// set current slider values as variables values
		for (var i = 0; i < variableIndices.length; i++) {
			var v = plotterValue.variables[variableIndices[i]];
			v.def = variableSliders[i].Value();
			plotterValue.variables[variableIndices[i]] = v;
		}
		return plotterValue;
	};
	
	// maintenance function
	function show(obj)
	{
		alert(JSON.stringify(obj, null, 4));
	}
	
	
	return modelPlotter;	
}();

