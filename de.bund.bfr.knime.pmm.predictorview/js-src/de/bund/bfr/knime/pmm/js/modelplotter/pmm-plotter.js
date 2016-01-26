pmm_plotter = function() {
	
	/*
	 * @author Markus Freitag, EITCO GmbH, MFreitag@eitco.de, 2015
	 * 
	 * Please try to avoid native JavaScript for the creation of DOM elements. 
	 * Use jQuery for the sake of clarity whenever possible. Improvements of 
	 * code readability are welcome.
	 */

	var modelPlotter = {
			version: "1.0.0"
	};
	modelPlotter.name = "PMM Model Plotter";
	
	var plotterValue;
	var plotterRep;
	
	var _globalNumber = 1;
	var _modelObjects = [];
	var _colorsArray = [];
	var _rawModels = [];
	
	var msgAdd = "Hinzufügen";
	var msgChoose = "Modell auswählen";
	
	/* the following values are subject to change */
	var _buttonWidth = "width: 250px;"; // not only used for buttons
	var _sliderWidth = "width: 190px;";
	var _sliderInputWidth = "width: 40px;";
	var _sliderBoxHeight = "height: 33px;";
	var _sliderStepSize = 0.0001; // aligns perfectly with the input field size
	var _totalHeight = "height: 800px;";
	var _plotWidth = 600;
	var _plotHeight = 400;
	
	// TODO: eliminate msg strings from code
	
	modelPlotter.init = function(representation, value) {

		_rawModels = value.models.models;
		plotterValue = value;
		plotterRep = representation; // not used

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
		leftWrapper.setAttribute("style", "width:300px; display: block; float: left;" + _totalHeight);
		
		var rightWrapper = document.createElement("div");
		rightWrapper.setAttribute("id", "rightWrapper");
		rightWrapper.setAttribute("style", "width:600px; display: block; float: left;" + _totalHeight);
		layoutWrapper.appendChild(leftWrapper);
		layoutWrapper.appendChild(rightWrapper);
		
		// selection
		var modelSelectionMenu = document.createElement("select");
		modelSelectionMenu.innerHTML = msgChoose;
		modelSelectionMenu.setAttribute("id", "modelSelectionMenu");
		modelSelectionMenu.setAttribute("style" , _buttonWidth);
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
		
		// add button
		var addButton = document.createElement("button");
		addButton.innerHTML = msgAdd;
		addButton.setAttribute("id", "addButton");
		addButton.setAttribute("style", _buttonWidth + "margin-bottom: 3px;");
		addButton.addEventListener("click", function() 
			{ 
				addFunctionFromSelection(); 
			}
		);
		leftWrapper.appendChild(addButton);
		
		// slider wrapper
		var sliderWrapper = document.createElement("div");
		sliderWrapper.setAttribute("id", "sliderWrapper");
		sliderWrapper.setAttribute("style" , _buttonWidth);
		leftWrapper.appendChild(sliderWrapper);
		
		var nextButton = $("<button>", {id: "nextButton", style: _buttonWidth, text: "Weiter" });
		nextButton.on("click", function() 
			{ 
				$("#layoutWrapper").fadeOut(500);
				showInputForm();
			}
		);
		$("#leftWrapper").append(nextButton);
		
		var plotterWrapper = document.createElement("div");
		plotterWrapper.setAttribute("id", "plotterWrapper");
		rightWrapper.appendChild(plotterWrapper);
		
		// meta data
		var metaDataWrapper = document.createElement("div");
		metaDataWrapper.setAttribute("id", "metaDataWrapper");
		rightWrapper.appendChild(metaDataWrapper);
		
		// --- //
		
		// dynamic options
		addSelectOptions(_rawModels);
		addSelectOption("g325234", "Modell Alpha", "Beispiele");
		addSelectOption("g948342", "Modell Beta 1", "Beispiele");
		addSelectOption("g451263", "Modell Beta 2", "Beispiele");
				
		/*
		 * jQueryUI
		 */
		$(function() {
			
			// make all html buttons jquery buttons
			$("#nextButton").button({
				icons: {
					primary: "ui-icon-arrow-1-e"
				},
				disabled: true
			});
			
			// make all html buttons jquery buttons
			$("#addButton").button({
			    icons: {
			        primary: "ui-icon-plus"
			    },
			    disabled: true
			}).click( function () 
				// once a model is added, we can activate the "next" button
				{
					$("#nextButton").button( "option", "disabled", false );
				}
			);
			
			// make select menu a jquery select menu
			$("#modelSelectionMenu").selectmenu({
				change: function () {
					$("#addButton").button( "option", "disabled", false );
				}
			});
			
			$("#metaDataWrapper").accordion({
				content: "height-style",
				collapsible: true
			});
		});
		/***/
	};
	
	function showInputForm()
	{
		$("#layoutWrapper").empty();
		inputMember = ["Name des Berichts", "Namen der Autoren", "Kommentar"]
		
		var form = $("<form>", { style: _buttonWidth });
		$.each(inputMember, function(i) {
			var paragraph = $("<p>", { style: _buttonWidth });
			var label = $("<div>", { text: inputMember[i], style: "font-weight: bold;  font-size: 10px;" + _buttonWidth });
			var input = $('<input>', { id: "input_" + inputMember[i].replace(/\s/g,""), style: "width: 224px;" })
			  .button()
			  .css({
			    'font' : 'inherit',
			    'background': '#eeeeee',
			    'color' : 'inherit',
			    'text-align' : 'left',
			    'outline' : 'thick',
			    'cursor' : 'text'
			  });
			form.append(paragraph);
			paragraph.append(label);
			paragraph.append(input);
		})
		$(document.body).append(form);
		
		var finishButton = $("<button>", {id: "finishButton", style: _buttonWidth, text: "Fertig" }).button();
		finishButton.on("click", function() 
			{ 
				plotterValue.reportName = $("#input_" + inputMember[0].replace(/\s/g,"")).val();
				plotterValue.authors = $("#input_" + inputMember[1].replace(/\s/g,"")).val();
				plotterValue.comment = $("#input_" + inputMember[2].replace(/\s/g,"")).val();
				
				show(plotterValue.reportName);
				show(plotterValue.authors);
				show(plotterValue.comment);
				
				$(document.body).fadeOut();
			}
		);
		$(document.body).append(finishButton);
	}
	/*
	 * adds a new option to the selection menu
	 * @param dbuuid id of the model
	 * @param modelName name of the model
	 */
	function addSelectOption(dbuuid, modelName, type)
	{
		// TODO: dynamisches Mappen von Typen zu Gruppen
		if(!type || type == "")
			type = "Kein Typ";
		
		var option = document.createElement("option");
		option.setAttribute("value", dbuuid);
		option.innerHTML = "[" + dbuuid + "] " + modelName;
		
		var groupId = "optGroup_" + type;
		var group = document.getElementById(groupId);
		if(!group) 
		{
			var group = document.createElement("optgroup");
			group.setAttribute("id", groupId);
			group.setAttribute("label", type);
			document.getElementById("modelSelectionMenu").appendChild(group);
		}
		group.appendChild(option);
	}
	
	/*
	 * adds a new option to the selection menu
	 * @param dbuuid id of the model
	 * @param modelName name of the model
	 */
	function addSelectOptions(modelsArray)
	{
		if(modelsArray)
			$.each(modelsArray, function(i) 
				{
					var type = modelsArray[i].type;
					var dbuuid = modelsArray[i].dbuuid;
					var modelName = modelsArray[i].estModel.name;
					addSelectOption(dbuuid, modelName, type);
				}
			);
	}

	/*
	 * parse function from model and modify it according to framework needs
	 * @param functionString formula as delivered by the java class
	 * @return parsed function 
	 */
	function prepareFunction(functionString) {
		// replace "T" and "Time" with "x" using regex
		// gi: global, case-insensitive
		var newString = functionString;
		if(newString.indexOf("=") != -1)
			newString = newString.split("=")[1];
		newString = newString.replace(/Time/gi, "x");
		newString = newString.replace(/\bT\b/gi, "x");
		return newString;
	}
	
	/*
	 * extract paremter names and values
	 * @param functionString formula as delivered by the java class
	 * @return reduced parameter array
	 */
	function prepareConstants(parameterArray) 
	{
		var newParameterArray = {};
		$.each(parameterArray, function(index, param) {
			var name = param["name"];
			var value = param["value"];
			newParameterArray[name] = value; 
		});
		return newParameterArray;
	}
	
	/*
	 * 1. determines the selected model from the selection menu
	 * 2. gets the model data
	 * 3. calls addFunctionObject() with the model data
	 */
	function addFunctionFromSelection()
	{
		// get the selection
		var selectMenu = document.getElementById("modelSelectionMenu");
		var selection = selectMenu.options[selectMenu.selectedIndex].value;

		// get the model data
		var model;
		$.each(_rawModels, function(i, object)
		{
			if(_rawModels[i].dbuuid == selection)
			{
				model = _rawModels[i];
				return true;
			}
		});

		if(model)
		{
			model.params.params.Y0 = plotterValue.y0; // set the value from the settings here
			var functionAsString = prepareFunction(model.catModel.formula);
			var functionConstants = prepareConstants(model.params.params);
			var dbuuid = model.dbuuid;
			var modelName = model.estModel.name;
			// call subsequent method
			addFunctionObject(dbuuid, functionAsString, functionConstants, model);
		}
		// TODO: just for testing purposes
		else
		{
			_globalNumber++;
			model =
				{
					"estModel": 
						{
							"name": "Test " + _globalNumber
						},
					"matrix": ""
				};
			addFunctionObject(_globalNumber, "x*0.1" + _globalNumber*2, null, model);
		}
	}
	
	/*
	 * adds a function to the functions array and redraws the plot
	 * @param dbuuid
	 * @param functionAsString the function string as returend by prepareFunction()
	 * @param the function constants as an array 
	 */
	function addFunctionObject(dbuuid, functionAsString, functionConstants, model)
	{
		var color = getNextColor(); // functionPlot provides 9 colors
		var maxRange = plotterValue.maxXAxis; // obligatoric for the range feature // TODO: dynamic maximum
		var range = [0, maxRange];
		
		var modelObj = { 
			 name: model.estModel.name,
			 dbuuid: dbuuid,
			 fn: functionAsString,
			 scope: functionConstants,
			 color: color,
			 range: range,
			 skipTip: false,
			 modelData: model
		};
		_modelObjects.push(modelObj);
		addMetaData(modelObj);

		// update plot and sliders after adding new function
		updateParameterSliders();
		drawD3Plot();
	}
	
	/*
	 * deletes a model for good - including graph and meta data
	 * @param id dbuuid of the model
	 */
	function deleteFunctionObject(id)
	{
		deleteMetaDataSection(id);
		try
		{
			deleteModelObject(id);
		}
		catch (e)
		{
			show(e);
		}
		if(_modelObjects.length == 0)
		{
			$("#nextButton").button( "option", "disabled", true);
		}
		updateParameterSliders();
		drawD3Plot();
	}
	
	function deleteModelObject(id)
	{
		$.each(_modelObjects, function (index, object) 
			{
				if(object && object.dbuuid == id)
				{
					_modelObjects.splice(index, 1);
					return true;
				}
			}
		);
	}
	
	function deleteMetaDataSection(id)
	{
		// remove meta data header
		var header = document.getElementById("h" + id);
		header.parentElement.removeChild(header);
		
		// remove meta data
		var data = document.getElementById(id);
		data.parentElement.removeChild(data);
		
		$("#metaDataWrapper").accordion("refresh");
	}
	
	/*
	 * adds a new entry for a new model object and shows it in the accordion below the plot
	 * @param modelObject the recently added modelObject
	 */
	function addMetaData(modelObject) 
	{
		/*
		 * Accordion needs a header followed by a div. We add a paragraph per parameter.
		 * The individual paragraph includes two divs, containing the parameter name and 
		 * value respectively.
		 * 
		 * Structure for each meta entry:
		 * > h3 (header)
		 * >> div (button>
		 * > div
		 * >> p
		 * >>> div (bold)
		 * >>> div
		 * >> p 
		 * >>> div /bold)
		 * >>> div
		 * ...
		 */
		var header = document.createElement("h3");
		header.setAttribute("id", "h" + modelObject.dbuuid);
		header.innerHTML = modelObject.dbuuid;
		
		// accordion-specific jQuery semantic for append()
		$("#metaDataWrapper").append(header);
		
		var deleteDiv = document.createElement("span");
		deleteDiv.setAttribute("style", "float: right; color: transparent; background: transparent; border: transparent;")
		header.appendChild(deleteDiv);
		
		var deleteButton = document.createElement("button");
	    $(deleteButton).button({
	        icons: {
	          primary: "ui-icon-closethick"
	        },
	        text: false
	    }).click(function(event) {
	    	deleteFunctionObject(modelObject.dbuuid);
	    });
	    deleteButton.setAttribute("style", 	"color: transparent; background: transparent; border: transparent;");
		deleteDiv.appendChild(deleteButton);
		
		// color field
		var colorDiv = document.createElement("span");
		colorDiv.setAttribute("style", "float: left; color: " + modelObject.color + "; background:  " + modelObject.color + "; border: 1px solid #cac3c3; margin-right: 5px; height: 10px; width: 10px; margin-top: 3px;")
		header.appendChild(colorDiv);

		var colorDivSub = document.createElement("button");
	    $(colorDivSub).button({
	        icons: {
	          primary: "ui-icon-blank"
	        },
	        text: false
	    });
		colorDivSub.setAttribute("style", "float: left; color: " + modelObject.color + "; background: " + modelObject.color + "; border: 0px; height: 10px; width: 10px;")
		colorDiv.appendChild(colorDivSub);
		
		// meta content divs divs
		var metaDiv = document.createElement("div");
		metaDiv.setAttribute("id", modelObject.dbuuid);
		$("#metaDataWrapper").append(metaDiv);

		// name of the model
		addMetaParagraph("Name", modelObject.name, "Kein Name gegeben");
		// model formula (function)
		addMetaParagraph("Quality Score", modelObject.modelData.estModel.qualityScore, "Kein Quality Score gegeben");
		// matrix data
		addMetaParagraph("Funktion", reparseFunction(modelObject.fn), "Keine Funktion gegeben");
		// function parameter
		addMetaParagraph("Initiale Parameter", unfoldScope(modelObject.scope), "Keine Parameter gegeben");
		// quality score
		addMetaParagraph("Matrix", modelObject.modelData.matrix.name + "; " + modelObject.modelData.matrix.detail, "Keine Matrixinformationen gegeben");
		
		// ... add more paragraphs/attributes here ...
		
		// use jquery to refresh the accordion values
		$("#metaDataWrapper").accordion("refresh");
		
		var numSections = document.getElementById("metaDataWrapper").childNodes.length / 2;
		// open last index
		$("#metaDataWrapper").accordion({ active: (numSections - 1) });
		
		function addMetaParagraph(title, content, alt) 
		{
			var header = "<div style='font-weight: bold; font-size:10px;'>" + title + "</div>";
			if(!content || content == "; ")
				var content = alt;
			var inner = "<div>" + content + "</div>";
			
			var paragraph = $("<p></p>").append(header, inner);
			$(metaDiv).append(paragraph);
		}
		
		function reparseFunction(formula)
		{
			return formula.replace(/\bx\b/gi, "Zeit");
		}
		
		function unfoldScope(paramArray)
		{
			if(!paramArray)
				return null;
			var list = "";
			$.each(paramArray, function(elem) 
				{
					list += ("<li>" + elem + ": " + paramArray[elem] + "</li>");
				}
			);
			var domElement = "<ul type='square'>" + list + "</ul>";
			return domElement;
		}
	}

    /*
     * adds sliders for all dynamic constants
     */
	function updateParameterSliders()
	{
	    var sliderWrapper = document.getElementById("sliderWrapper");
	    var sliderIds = []; // ids of all sliders that correspond to a constant
	    
	    
	    for (var modelIndex in _modelObjects)
	    {
	    	var constants = _modelObjects[modelIndex].scope;
	    	if(constants)
	    	{
		    	$.each(constants, function(constant, value)
		    	{
					var sliderId = "slider_" + constant.toUpperCase();
					sliderIds.push(sliderId); // remember active sliders
					
					// do not recreate if already in the DOM
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
				    sliderBox.setAttribute("style" , _buttonWidth + _sliderBoxHeight);
				    sliderWrapper.appendChild(sliderBox);
				    
					var sliderLabel = document.createElement("div");
					sliderLabel.innerHTML = constant;
					sliderLabel.setAttribute("style" , "font-weight: bold; font-size: 10px;");
					sliderBox.appendChild(sliderLabel);
					
					var slider = document.createElement("div");
					slider.setAttribute("style" , _sliderWidth + "display: block; float: left; margin: 3px");
					sliderBox.appendChild(slider);
									    
					var sliderValueDiv = document.createElement("div");
					sliderValueDiv.setAttribute("style" , _sliderInputWidth + "display: block; float: left;");
					sliderBox.appendChild(sliderValueDiv);
					
					var sliderValueInput = document.createElement("input");
					sliderValueInput.setAttribute("type", "number");
					sliderValueInput.setAttribute("style" , _sliderInputWidth + "font-weight: bold;");
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
						sliderMax = 0;
		    		}
					else
					{
						sliderMin = 0;
						sliderMax = 1;
					}
					sliderValueInput.setAttribute("min", sliderMin);
					sliderValueInput.setAttribute("max", sliderMax);
					
					// set input field to initial value
					$(sliderValueInput).val(value);
					
					// configure slider, its range and init value
				    $(slider).slider({
				    	value: value,
				    	min: sliderMin,
				    	max: sliderMax,
				    	step: _sliderStepSize,
				    	// changing the slider changes the input field
				        slide: function( event, ui ) {
				            $(sliderValueInput).val( ui.value );
				            // delay prevents excessive redrawing
				            window.setTimeout(updateFunctionConstant(constant, ui.value), 100);
				        }
				    });
					$(sliderValueInput).change(function() {
						// changing the input field changes the slider
						$(slider).slider("value", this.value);
							// delay prevents excessive redrawing
							window.setTimeout(updateFunctionConstant(constant, this.value), 100);
					});
					// react immediately on key input
					$(sliderValueInput).keyup(function() {
						$(this).change();
					});
				});
	    	}
	    }
	    
	    // at last, we delete unused sliders
	    var allIds = []; // ids of all shown sliders
	    
	    var sliderWrapperChildren = sliderWrapper.children;
	    for(var i = 0; i < sliderWrapperChildren.length; i++) 
	    {
	    	allIds.push(sliderWrapperChildren[i].id);
	    };

	    $.each(allIds, function(i) {
	    	// check if slider is still used
	    	var found = sliderIds.indexOf(allIds[i]);
	    	// if not used, remove from DOM
	    	if(found == -1)
	    		sliderWrapper.removeChild(document.getElementById(allIds[i]));
	    });
	}

	/* 
	 * update a constant value in all functions
	 * @param constant parameter name
	 * @param constant (new) parameter value
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
		// the plot element has to be reset because otherwise functionPlot may draw artifacts
		var d3Plot = document.getElementById("d3plotter");
		if(d3Plot)
		{
			d3Plot.parentElement.removeChild(d3Plot)
		}
		d3Plot = document.createElement("div");
		d3Plot.setAttribute("id", "d3plotter");
		
		var wrapper = document.getElementById("plotterWrapper");
		wrapper.appendChild(d3Plot);
		
		var xUnit = "unknown";
		$.each(_rawModels[0].indeps.indeps, function(i) {
			var currentIndep = _rawModels[0].indeps.indeps[i];
			if(currentIndep["name"] == "Time" || currentIndep["name"] == "T")
			{
				xUnit = currentIndep["name"] + " in " + currentIndep["unit"];
				return true;
			}
		});
		
		var yUnit = "unknown";
		$.each(_rawModels[0].params.params, function(i) {
			var currentParam = _rawModels[0].params.params[i];
			if(currentParam["unit"])
			{
				yUnit = currentParam["unit"];
				return true;
			}
		});
		
		// plot
		functionPlot({
		    target: '#d3plotter',
		    xDomain: [plotterValue.minXAxis, plotterValue.maxXAxis],
		    yDomain: [plotterValue.minYAxis, plotterValue.maxYAxis],
		    xLabel: xUnit,
		    yLabel: yUnit,
		    height: _plotHeight,
		    witdh: _plotWidth,
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
		if(_colorsArray.length <= 0)
			_colorsArray = functionPlot.globals.COLORS.slice(0); // clone function plot colors array
		return _colorsArray.shift();
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
	
	/*
	 * KNIME
	 */
	if (parent !== undefined && parent.KnimePageLoader !== undefined)
	{
		parent.KnimePageLoader.autoResize(window, frameElement.id)
	}
	
	return modelPlotter;	
}();

