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
	
	var _globalNumber = 1;
	var _modelObjects = [];
	var colorsArray = [];
	
	var msgAdd = "Hinzufügen";
	var msgChoose = "Modell auswählen";
	
	var buttonWidth = "width: 250px;";
	var sliderWidth = "width: 190px;";
	var sliderInputWidth = "width: 40px;";
	var sliderBoxHeight = "height: 33px;";
	var _sliderStepSize = 0.0001;
	var totalHeight = "height: 800px;";
	var plotWidth = 600;
	var plotHeight = 400;
	
	modelPlotter.init = function(representation, value) {

		plotterValue = value;
		plotterRep = representation;
		
		// body
		var body = document.getElementsByTagName("body")[0];
		body.setAttribute("style", "background: #fdfdfd; width:100%; height:100%; font-family:Verdana,Helvetica,sans-serif; font-size:12px; overflow:hidden;");

		/*
		 * layout
		 */
		var layoutWrapper = document.createElement("div");
		layoutWrapper.setAttribute("id", "layoutWrapper");
		layoutWrapper.setAttribute("style", "width:900px;");
		body.appendChild(layoutWrapper);
		
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
		
		// plot
		var d3Plot = document.createElement("div");
		d3Plot.setAttribute("id", "d3plotter");
		rightWrapper.appendChild(d3Plot);
		
		// meta data
		var metaDataWrapper = document.createElement("div");
		metaDataWrapper.setAttribute("id", "metaDataWrapper");
		rightWrapper.appendChild(metaDataWrapper);
		
		// --- //
		
		// dynamic options
		addSelectOption(plotterValue.dbuuid, plotterValue.modelName);
		addSelectOption("123", "Beispiel");
				
		/*
		 * jQueryUI functions
		 */
		$(function() {
			// make all html buttons jquery buttons
			$("button").button({
			    icons: {
			        primary: "ui-icon-plus"
			    }
			});
			// make all html selects jquery select menus
			$("select").selectmenu();
			$("#metaDataWrapper").accordion({
				content: "height-style",
				collapsible: true
			});
		});
		/***/
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
		// replace "T" and "Time" with "x" using regex
		// gi: global, case-insensitive
		var newString = functionString;
//		show("old string: " + newString);
		newString = newString.replace(/Time/gi, "x");
		newString = newString.replace(/\bT\b/gi, "x");
//		show("new string: " + newString);
		return newString;
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
			_globalNumber++; 
			addFunctionObject(_globalNumber, "x-" + _globalNumber, null);
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
		var maxRange = plotterValue.maxXAxis; // obligatoric for the range feature // TODO: dynamic maximum
		var range = [0, maxRange];
		
		var modelObj = { 
			 dbuuid: dbuuid,
			 fn: functionAsString,
			 scope: functionConstants,
			 color: color,
			 range: range,
			 skipTip: false
		};
		_modelObjects.push(modelObj);
		// update plot after adding new function
		generateParameterSliders();
		addMetaData(modelObj);
		drawD3Plot();
	}
	
	function addMetaData(modelObject) 
	{
		var header = document.createElement("h3");
		header.setAttribute("id", "h" + modelObject.dbuuid);
		header.innerHTML = modelObject.dbuuid;
		$("#metaDataWrapper").append(header);
		
		var metaDiv = document.createElement("div");
		metaDiv.setAttribute("id", modelObject.dbuuid);
		$("#metaDataWrapper").append(metaDiv);
		
		var paragraphFunc = document.createElement("p");
		metaDiv.appendChild(paragraphFunc);
		
		var functionHeader  = document.createElement("div");
		functionHeader.setAttribute("style", "font-weight: bold;");
		functionHeader.innerHTML = "Funktion";
		paragraphFunc.appendChild(functionHeader);	
		
		var functionElem = document.createElement("div");
		functionElem.innerHTML = modelObject.fn;
		paragraphFunc.appendChild(functionElem);	
		
		var paragraphScope = document.createElement("p");
		metaDiv.appendChild(paragraphScope);
		
		var scopeHeader  = document.createElement("div");
		scopeHeader.setAttribute("style", "font-weight: bold;");
		scopeHeader.innerHTML = "Parameter";
		paragraphScope.appendChild(scopeHeader);
		
		var scopeElem = document.createElement("div");
		scopeElem.innerHTML = modelObject.scope;
		paragraphScope.appendChild(scopeElem);	
		
		$("#metaDataWrapper").accordion("refresh");
	}

    /*
     * adds sliders for all dynamic constants
     */
	function generateParameterSliders()
	{
	    var sliderWrapper = document.getElementById("sliderWrapper");
	    
	    for (var modelIndex in _modelObjects)
	    {
	    	var constants = _modelObjects[modelIndex].scope;
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
					 * the layout structure is as follows:
					 * > sliderBox
					 * >> sliderLabel
					 * >> slider | >> sliderValueDiv
					 * 			   >>> sliderValueInput
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
					
					var sliderValueInput = document.createElement("input");
					sliderValueInput.setAttribute("type", "number");
					sliderValueInput.setAttribute("style" , sliderInputWidth + "font-weight: bold;");
					sliderValueDiv.appendChild(sliderValueInput);
					
					var sliderMin;
					var sliderMax;
					
					if(value > 0)
					{
						sliderMin = value / 2;
						sliderMax = value * 2;
					}
					else if(value < 0) 
					{
						sliderMin = value + value;
						sliderMax = value - value;
		    		}
					else
					{
						sliderMin = 0;
						sliderMax = 1;
					}
					sliderValueInput.setAttribute("min", sliderMin);
					sliderValueInput.setAttribute("max", sliderMax);
					
					$(sliderValueInput).val(value);
				    $(slider).slider({
				    	value: value,
				    	min: sliderMin,
				    	max: sliderMax,
				    	step: _sliderStepSize,
				    	// changing the slider changes the input field
				        slide: function( event, ui ) {
				            $(sliderValueInput).val( ui.value );
				            // delay prevents excessive redrawing
				            window.setTimeout(updateFunctionConstant(constant, ui.value), 30);
				        }
				    });
					$(sliderValueInput).change(function() {
						// changing the input field changes the slider
						$(slider).slider("value", this.value);
							// delay prevents excessive redrawing
							window.setTimeout(updateFunctionConstant(constant, this.value), 30);
					});
					// react immediately on key input
					$(sliderValueInput).keyup(function() {
						$(this).change();
					});
				});
	    	}
	    }
	}

	/* 
	 * update a constant value in all functions
	 */
	function updateFunctionConstant(constant, value)
	{
		newValue = parseFloat(value);
		for(var modelIndex in _modelObjects)
		{
			var constants = _modelObjects[modelIndex].scope;
			if(constants && constants[constant] != undefined)
				constants[constant] = newValue;
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
		    data: _modelObjects
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
	
	
	// maintenance function
	function show(obj)
	{
		alert(JSON.stringify(obj, null, 4));
	}

	/*
	 * mandatory for JS
	 */
	modelPlotter.validate = function() 
	{
		return true;
	}
	
	modelPlotter.setValidationError = function () 
	{ 
		show("validation error");
	}
	
	modelPlotter.getComponentValue = function() 
	{
	    return plotterValue;
	}
	
	return modelPlotter;	
}();

