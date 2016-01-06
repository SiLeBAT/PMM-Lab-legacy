/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
models = $${Smodels}$$;
organisms = $${Sagents}$$;
matrices = $${Smatrices}$$;
references = $${Sreferences}$$;
categories = $${Scategories}$$;
units = $${Sunits}$$;
secondary = $${Ssecondary}$$;


/****************Initial Load****************/

$(function() {
	create_header();
	create_body();
	create_accordion();
	create_block();
});

/****************HTML HEADER****************/
function create_header(){
	var script = ''; 
	var css = '';

	$("head").append(css).append(script);
}
	
/****************HTML BODY****************/
function create_body(){


	var header =  	'<div class="bs-docs-header" id="content" tabindex="-1" onclick="create_block()" >' + 
					'	<div class="container">' + 
					'		<h1>Editor SBML</h1>' + 
					'		<p>Editor SBML for Knime</p>' + 
					'		<div id="carbonads-container">' + 
					'			<div class="carbonad"><div id="azcarbon"></div></div>' + 
					'		</div>' + 
					'	</div>' +
					'</div>';

	var container = '<div class="container">' +
					'	<div class="row">' +
					'		<div class="col-md-3" id="leftCol">' +
					'			<div class="panel-group" ></div>' +
					'			<div class="panel-group" id="sidebar" role="tablist" aria-multiselectable="true"></div>' +
					'		</div>' +
					'		<div class="col-md-9" id="rightCol"></div>' +
					'	</div>' +
					'</div>';

	document.createElement('body');
	$("body").html(header + container);
}

/****************SIDEBAR ACCORDION****************/
function create_accordion() {
	
	var accordion = '';

	for (i = 0; i < models.length; i++) {

		var modelsSecondary = modelsPrimary = '';

		//Get Type Model
		if(models[i].Model1Schema)		var category = (models[i].Model2Schema) ? "T" : "P" ;
		else if(models[i].Model2Schema)	var category = "S";	
		
		if(category != 'S')		name1 = (models[i].Model1Schema.EstModel.name != null)?  models[i].Model1Schema.EstModel.name.substring(0, 17) : "Model-"+ i;
		else 					name1 = (models[i].Model2Schema.EstModelSec.name != null)?  models[i].Model1Schema.EstModelSec.name.substring(0, 17) : "Model-"+ i;	

		//Only Model Tertiary
		if(category == "T"){

			modelsSecondary =  '<li class="list-group-item model2 lvl-1">' +
								'	<div>' +
								'		<div class=" item-first some-second some-second-plus" data-id="'+ i +'" data-status="close" ></div>' +
								'		<div >Secondary Models</div>' +
								'	</div>';

			for (j = 0; j < models[i].Model2Schema.length; j++) {

				var name2 = models[i].Model2Schema[j].DependentSec.name; 

				var conditions = ' data-id="'+ i +'" data-value="'+ j +'" data-name="'+ name2 +'"'; 

				modelsSecondary += 	'<li class="list-group-item  second" '+ conditions +' data-category="S" data-type="model" >' +
									'	<div class="lvl-2">' +
									'		<div id="model'+i+'-'+j+'" '+conditions+' data-status="close" class="col-sm-1 item-first some-accordion"></div>' +
									'		<div class="item-second pencil clickable" '+ conditions +' data-category="S" data-type="model" >'+ name2 + '</div>' +
									'	</div>' +
									'</li>' +
									'<li class="list-group-item clickable second"'+ conditions +' data-category="S" data-type="references">' +
									'	<div class="lvl-3">' +
									'		<div class="item-second pencil">References</div>' +
									'	</div>' +
									'</li>' +

									'<li class="list-group-item clickable second"'+ conditions +' data-category="S" data-type="formula">' +
									'	<div class="lvl-3">' +
									'		<div class="item-second pencil">Formula</div>' +
									'	</div>' +
									'</li>';
			}	
		}

		if(category != 'S'){	
			modelsPrimary = '<li class="list-group-item clickable pencil lvl-1" data-id="'+ i +'" data-value="-1" data-type="organism"> Organism </li>' +
							'<li class="list-group-item clickable pencil lvl-1" data-id="'+ i +'" data-value="-1" data-type="matrix"> Matrix </li>';	
		}

		accordion += 	'<div class="panel panel-default">'+
						'	<div class="panel-heading collapsed clickable" data-id="'+ i +'" data-value="-1"  data-category="'+ category +'" '+
						'		 data-type="model" role="tab" data-toggle="collapse" data-parent="#sidebar" href="#collapse'+ i +'" '+
						'		 id="link-window-'+ i + '"aria-expanded="false" aria-controls="collapse0">' +
						'		<h4 class="panel-title">' +
						'			<div class="type-model">'+ category +'</div>'+
						'			<a class="link-model">'+ name1 +'</a>' +
						'		</h4>' +
						'	</div>' +
						'	<div id="collapse'+ i + '" class="panel-collapse collapse " role="tabpanel" >' +
						'		<ul class="list-group">' +
						modelsPrimary +
						'			<li class="list-group-item clickable pencil lvl-1" data-id="'+i+'" data-type="references"  data-category="'+ category +'" data-value="-1"> References </li>' +
						'			<li class="list-group-item clickable pencil lvl-1" data-id="'+i+'" data-type="formula"  data-category="'+ category +'"  data-value="-1"> Formula</li>' +
						modelsSecondary +
						'		</ul>' +
						'	</div>' +
						'</div>';
	}	

	$("#sidebar").append(accordion);
	$(".second").hide();

	$(".some-second-plus, .some-second-minus").on("click",function(){open_models2($(this))});
	$("div.some-accordion, div.some-normal").on("click",function(){open_model2Schema($(this))});
	
}

/****************BLOCK RIGHT****************/
function create_block() {

	var container = '<div class="bs-callout bs-callout-info" id="callout-popover-data">' +
					'	<h4 id="titleModel"> Data attributes for individual popovers </h4>' +
					'	<p>Options for individual popovers can alternatively be specified through the use of data attributes, as explained above.</p>' +
					'	<div id="insertModule"></div>' +
					'	<div class="clearfix"></div></br>' +
					'	<div class="form-group"><input class="btn btn-default pull-right btn-submit" style="display:none" type="submit" onclick="save_block()" value="Submit"></div>' +
					'	<div class="clearfix"></div>' +
					'</div>';

	$("#rightCol").html(container);
	load_block();
}

/****************LOAD AND SAVE BLOCK: MODEL, MATRIX, ORGANISM, REFERENCES, FORMULAS, SECONDARY MODEL****************/
function load_block() {

	$(".clickable").click(function(){ 

		$(".pencil").removeClass('pencil-active');
		$(this).addClass('pencil-active');
		$(this).find("div.pencil").addClass('pencil-active');

		var container = '<input type="hidden" class="form-control" id="primary" value="'+$(this).data('id')+'">' +
						'<input type="hidden" class="form-control" id="secondary" value="'+$(this).data('value')+'">' +
						'<input type="hidden" class="form-control" id="type" value="'+$(this).data('type')+'">' +
						'<input type="hidden" class="form-control" id="category" value="'+$(this).data('category')+'">';
						
		$("#insertModule").html(container);
	
		eval("insert_"+$(this).data('type'))( $(this).data('id'), $(this).data('value'), $(this).data('category'));
		$(".btn-submit").show();
	});
}

function save_block(){
	
	index = $("#primary").val();
	value = $("#secondary").val();
	type = $("#type").val();
	category = $("#category").val();
	
	eval("save_"+type)(index, value, category);
}

/****************FUNCTIONS LOAD AND SAVE MODELS****************/
function insert_model(id, value, category){

	if(value == -1)	$(".some-second-minus").click();

	mod = get_category(id, value, category, "EstModel");

	$("#titleModel").html(""+ mod.title +" Model info");
	
	name = 			(mod.sel.name == null)? 		"" : mod.sel.name;
	r2 = 			(mod.sel.r2 == null)? 			"" : mod.sel.r2;
	aic = 			(mod.sel.aic == null)? 			"" : mod.sel.aic;
	bic = 			(mod.sel.bic == null)? 			"" : mod.sel.bic;
	rms = 			(mod.sel.rms == null)? 			"" : mod.sel.rms;

	comment = 		(mod.sel.comment == null)? 		"" : mod.sel.comment;
	qualityScore = 	(mod.sel.qualityScore == null)? 0  : mod.sel.qualityScore;

	checked = 		(mod.sel.checked == true)? 		"checked" : "" ;

	var container = '<form class="form-horizontal">' +
					'  <div class="form-group">' +
					'    <label for="title" class="col-sm-3 control-label">Title</label>' +
					'    <div class="col-sm-5">' +
					'      <input type="text" class="form-control" id="title" value="'+ name +'">' +
					'    </div>' +
					'  </div>' +
					'	<h4>Goodness of fit</h4>' +
					'	<div class="row col-md-offset-3">' +
					'		<div class="col-xs-3 ">' +
					'			<label for="r2">R2</label>' +
					'			<input type="text" class="form-control" id="r2" value="'+ r2 +'">' +
					'		</div>' +
					'		<div class="col-xs-3">' +
					'			<label for="rms">RMS</label>' +
					'			<input type="text" class="form-control" id="rms" value="'+ rms +'">' +
					'		</div>' +
					'	</div>' +
					'	<div class="row col-md-offset-3">' +
					'		<div class="col-xs-3 ">' +
					'			<label for="aic">AIC</label>' +
					'			<input type="text" class="form-control" id="aic" value="'+ aic +'">' +
					'		</div>' +
					'		<div class="col-xs-3">' +
					'			<label for="bic">BIC</label>' +
					'			<input type="text" class="form-control" id="bic" value="'+ bic +'">' +
					'		</div>' +
					'	</div>' +
					'	<h4>Subjective quality</h4>' +
					'  <div class="form-group">' +
					'    <label for="title" class="col-sm-3 control-label">Quality Score</label>' +
					'    <div class="col-sm-5">' +
					'		<select class="form-control" id="ddlQualityScore">' +
					'			<option value="0" style="color: black">White</option>' +
					'			<option value="1" style="color: green">Green</option>' +
					'			<option value="2" style="color: yellow">Yellow</option>' +
					'			<option value="3" style="color: red">Red</option>' +
					'		</select>' +
					'    </div>' +
					'  </div>'  +
					'  <div class="form-group">' +
					'    <label for="title" class="col-sm-3 control-label">Validated</label>' +
					'    <div class="col-sm-5">' +
					'		<input type="checkbox" id="validated" '+ checked +'>Checked</label>' +
					'    </div>' +
					'  </div>'  +
					'  <div class="form-group">' +
					'    <label for="title" class="col-sm-3 control-label">Description</label>' +
					'    <div class="col-sm-5">' +
					'	<div class="form-group"><textarea id="desc" class="form-control" rows="3">'+ comment +'</textarea></div>'+
					'    </div>' +
					'  </div>'  +
					'</form>';

	$("#insertModule").append(container);
	$("#ddlQualityScore").val(qualityScore);
}

function save_model(id, value, category){

	var mod = get_category(id, value, category, "EstModel");
	var error = 1; 

	if(isNaN($("#r2").val()))  print_error("error", "Goodness of Fitness (R2): Not a number", "Error!");
	if(isNaN($("#aic").val())) print_error("error", "Goodness of Fitness (AIC): Not a number", "Error!");
	if(isNaN($("#bic").val())) print_error("error", "Goodness of Fitness (BIC): Not a number", "Error!");
	if(isNaN($("#rms").val())) print_error("error", "Goodness of Fitness (RMS): Not a number", "Error!");

	if(isNaN($("#r2").val())+ isNaN($("#aic").val()) + isNaN($("#bic").val()) + isNaN($("#rms").val())) error = 0;  
	if(error){

		mod.sel.name = 			($("#title").val() == "")? 				null : $("#title").val();
		mod.sel.r2 = 			($("#r2").val() == "")? 				null : $("#r2").val();
		mod.sel.aic = 			($("#aic").val() == "")? 				null : $("#aic").val();
		mod.sel.bic = 			($("#bic").val() == "")? 				null : $("#bic").val();	
		mod.sel.rms = 			($("#rms").val() == "")? 				null : $("#rms").val();
		mod.sel.comment = 		($("#desc").val() == "")? 				null : $("#desc").val();
		mod.sel.checked = 		$("#validated").prop("checked")
		mod.sel.qualityScore = 	$("#ddlQualityScore").val();

		if(category != 'S'){
			if(mod.sel.name != null)	$("#link-window-"+ id +" h4 a.link-model").text(mod.sel.name.substring(0, 17));
			else 						$("#link-window-"+ id +" h4 a.link-model").text("Model-"+id);
		} else if(value == -1){
			if(mod.sel.name != null)	$("#link-window-"+ id +" h4 a.link-model").text(mod.sel.name.substring(0, 17));
			else 						$("#link-window-"+ id +" h4 a.link-model").text("Model-"+id);
		} 	
		print_error("success", "Data Model Save", "Save!");
	}
}

/****************FUNCTIONS LOAD AND SAVE ORGANISM****************/
function insert_organism(id) {
	
	$("#titleModel").html("Organism info");

	var container = '<form class="form-horizontal">' +
					'  </br>' +
					'  <div class="form-horizontal">' +
					'    <label for="ddlOrganism" class="col-sm-3 control-label">Organism</label>' +
					'    <div class="col-sm-5">' +
					'      <select type="text" class="form-control" id="ddlOrganism"></select>' +
					'    </div>' +
					'  </div>' +
					'</form>';

	
	$("#insertModule").append(container);
	
	load_combo("#ddlOrganism", organisms, models[id].TimeSeries.Organism.id, 0, 'name');
	$("#ddlOrganism").select2();
}

function save_organism(id) {

	var agent_id = $('#ddlOrganism').val();

	// Search agent
	for (var i = 0; i < organisms.length; i++) {
		if (agent_id == organisms[i].id) {
			agent_name = organisms[i].name;
			break;
		}
	}

	models[id].TimeSeries.Organism.id = agent_id;
	models[id].TimeSeries.Organism.name = agent_name;
	print_error("success", "Save Organism", "Save!");
}

/****************FUNCTIONS LOAD AND SAVE MATRIX****************/
function insert_matrix(id){

	$("#titleModel").html("Matrix info");
	
	var container = '<form class="form-horizontal">' +
					'  </br>' +
					'  <div class="form-group">' +
					'    <label for="identifier" class="col-sm-3 control-label">Matrix</label>' +
					'    <div class="col-sm-5">' +
					'      <select type="text" class="form-control" id="ddlMatrix"></select>' +
					'    </div>' +
					'  </div>' +
					'</form>';
	
	
	$("#insertModule").append(container);

	load_combo("#ddlMatrix", matrices, models[id].TimeSeries.Matrix.id, 0, 'name');
	$("#ddlMatrix").select2();
}

function save_matrix(id) {

	var matrix_id = $('#ddlMatrix').val();

	// Search matrix
	for (var i = 0; i < matrices.length; i++) {
		if (matrix_id == matrices[i].id) {
			matrix_name = matrices[i].name;
			break;
		}
	}

	models[id].TimeSeries.Matrix.id = matrix_id;
	models[id].TimeSeries.Matrix.name = matrix_name;
	print_error("success", "Save Matrix", "Save!");
}

/****************FUNCTIONS LOAD, SAVE REFERENCES****************/
function insert_references(id, value, category) {

	$("#titleModel").html("References info");

	var mod = get_category(id, value, category, "M_Literatur");

	var container = '<form class="form-horizontal">' +
					'  	<div class="form-group" id="listReference">' +
					'    	<table class="table">' +
					'      		<thead>' +
					'        		<tr><th>#</th><th>ID</th><th>Title</th></tr>' +
					'      		</thead>' +
					'      		<tbody id="ref_table_body"></tbody>' +
					'    	</table></br>' +
					'    	<span style="float: left;">New Reference : </span>' +
					'    	<div class="col-sm-5">' +
					'      		<select type="text" class="form-control" id="ddlReference"></select>' +
					'    		</div>' +
					'    	<input id="clear_button" class="btn btn-default pull-right" type="button" value="Clear">' +					
					'  	</div>' +
					'</form>';

	$("#insertModule").append(container);

	// Add literature items to table
	litItems = mod.sel;
	for (litNum = 0; litNum < litItems.length; litNum++) {
		add_reference(litItems[litNum].id, litItems[litNum].title);
	}

	load_combo("#ddlReference", references, null, 1, 'title');

	$("#ddlReference").select2().on("change", change_reference);
	$("#clear_button").click(clear_references);
}

function add_reference(id, title) {

	var row = '<tr data-id="'+id+'">' +
			  '  <td><input type="checkbox"></td>' +
			  '  <td>' + id + '</td>' +
			  '  <td>' + title + '</td>' +
			  '</tr>';

	$("#ref_table_body").append(row);
}

function change_reference(){
	add_reference($("select option:selected").val(), $("select option:selected").text());
	load_combo("#ddlReference", references, null, 1, 'title');

}

function clear_references() {
	$('#ref_table_body tr input:checked').parent().parent().remove();
	load_combo("#ddlReference", references, null, 1, 'title');
}

function save_references(id, value,category) {

	var M_Literatur = Array ();

	$('#ref_table_body tr').each(function(){

		data = $(this).data('id'); 
		Lit = $.grep(references, function(e){ return e.id == data; });
		M_Literatur.push(Lit[0]);

	});

	switch(category) {
	    case 'T': models[id].Model1Schema.M_Literatur = M_Literatur; break;
	    case 'P': models[id].Model1Schema.M_Literatur = M_Literatur; break;
	    case 'S':
        	if(value == -1)	models[id].Model2Schema.M_LiteraturSec = M_Literatur; 
        	else     		models[id].Model2Schema[value].M_LiteraturSec = M_Literatur; 
	        break;
	}

	print_error("success", "Save References", "Save!");
}

/****************FUNCTIONS LOAD AND SAVE FORMULA & PARAMETERS****************/
function insert_formula(id, value,category){
 
 	var mod = get_category(id, value, category, "CatModel");

 	$("#titleModel").html(mod.title+" Formula");
 	
	var sbtr = mod.sel.formula;
 	var name =  mod.sel.name; 

	if (sbtr.lastIndexOf("*((((") == -1){
		var formula = sbtr.substring(sbtr.indexOf("=")+1);
		var boundary =  '';
	} else {
		var formula = sbtr.substring(sbtr.indexOf("=")+1,sbtr.lastIndexOf("*(((("));
		var boundary = sbtr.substring(sbtr.lastIndexOf("*((((")+6,sbtr.lastIndexOf(")))))"));
	}
 	
 	var selector = '';
 	if(category == 'S'){
		selector +=	'<div class="form-group">' +
					'    <label for ="ddlFormula" class="col-sm-3 control-label">Formula DB</label>' +
					'    <div class="col-sm-5">' +
					'      <select type="text" class="form-control" id="ddlFormula"></select>' +
					'    </div>' +
					'	 </div>';
 	}

	var container = '<form class="form form-horizontal1">' +
					selector +
					'  <div class="form-group">' +
					'    <label for="identifier" class="col-sm-3 control-label">Formula Name</label>' +
					'    <div class="col-sm-9">' +
					'      <input type="text" class="form-control" id="name" value="'+name+'">' +
					'    </div>' +
					'  </div>' + 
					'  <div class="form-group">' +
					'    <label for="identifier" class="col-sm-3 control-label">Formula</label>' +
					'    <div class="col-sm-9">' +
					'      <input type="text" class="form-control" id="formula" value="'+formula+'" data-value="'+formula+'">' +
					'    </div>' +
					'  </div>' +

					'  <div class="form-group">' +
					'    <label for="title" class="col-sm-3 control-label">Boundary Conditions</label>' +
					'    <div class="col-sm-5">' +
					'      <input type="text" class="form-control" id="boundary_conditions" value="'+boundary+'" data-value="'+boundary+'">' +
					'    </div>' +
					'  </div>' +
					'	<div class="form-group">' +
					'    <label for ="identifier" class="col-sm-3 control-label">Category</label>' +
					'    <div class="col-sm-5">' +
					'      <select type="text" class="form-control" id="ddlCategory"></select>' +
					'    </div>' +
					'  </div>' +
					' <div  id="divParameter">' +
					'  <table  id="tableList"> ' +
					'    <thead>' +
					'        <tr>' +
					'			 <th dta-field="Select" data-sortable="false">#</th>' +
					'            <th data-field="Parameter" data-sortable="true">Parameter</th>' +
					'            <th data-field="Unit" data-sortable="false">Unit</th>' +
					'            <th data-field="Indep" data-sortable="false">Ind</th>' +
					'            <th data-field="Value" data-sortable="false">Value</th>' +
					'            <th data-field="Error" data-sortable="false" >S.E.</th>' +
					'            <th data-field="Min" data-sortable="false">Min</th>' +
					'            <th data-field="Max" data-sortable="false" >Max</th>' +
					'            <th data-field="Description" data-sortable="false">Description</th>' +
					'        </tr>' +
					'    </thead>' +
					'    <tbody id="par_table_body"></tbody>' +
					'  </table>';
					' </div>' +
					'</form>';

	$("#insertModule").append(container);
	load_combo("#ddlCategory", categories, mod.sel.modelClass, 0, 'name');
	$("#ddlCategory").select2();

	if(category == 'S'){
		load_combo("#ddlFormula", secondary, mod.sel.id, 1, 'name');
			$("#ddlFormula").select2();
	}

	// Add dependent parameter
 	var mod = get_category(id, value, category, "Dependent");
 	var dep = mod.sel.name;
	add_parameter(mod.sel, 'D',"-", "-");

	// Add independent parameter
	var mod = get_category(id, value, category, "Independent");
	for (var p = 0; p < mod.sel.length; p++) {
		add_parameter(mod.sel[p], 'I',"-", "-");
	}

	//Add parameter
	var mod = get_category(id, value, category, "Parameter");
	for (var p = 0; p < mod.sel.length; p++) {
		value = (mod.sel[p].value ==  null) ? 			'' : mod.sel[p].value ;
		error = (mod.sel[p].error ==  null) ? 			'' : mod.sel[p].error ;
		add_parameter(mod.sel[p], 'P', value, error, category);	
	}

	$("#tableList").bootstrapTable();

	check_dependent(category);
	editable_text();
	editable_description();
	editable_unit();
	change_parameters(dep, category);
	load_formula();
	
	$("td.item-first").on("click", function(){ change_model2Schema($(this))});
}

function add_parameter(par, type, value, error, category){

	condition = '';

	switch(type) {
	    case 'D':
	        editable = clase = check = '';
	        break;
	    case 'I':
	       	editable = clase = ''; 
	       	check = '<input type="checkbox" checked class="checkeable" >'; 
	        break;
	   case 'P':
	        check = '<input type="checkbox" class="checkeable" >'; 
			editable = ' class="textEdit editable editable-click" '; 
			
			if(category != "S"){
				if($('.second[data-id="'+ $("#primary").val() +'"][data-name="'+par.name+'"]').length != 0){
					condition += ' data-status="2" ';
					clase = ' class="col-sm-1 item-first some-success"  '; 

				}else{
					condition += 'data-status="1" ';
					clase = ' class="col-sm-1 item-first some-remove" '; 
				}
			} else {
				clase = ''; 
			}

	        break;
	}

	var row ='<tr id="'+par.name +'" data-type="'+type+'" data-name="'+par.name+'"  '+ condition +'>' +
			'  <td '+ clase +'></td>' +
			'  <td>' + par.name + '</td>' +
			'  <td><a class="selectUnit editable editable-click">'+((par.unit == null) ? '' : par.unit) +'</a></td>' +
			'  <td>'+ check +'</td>' +  
			'  <td><a '+ editable +'>'+ value +'</a></td>' +
			'  <td><a '+ editable +'>'+ error +'</a></td>' +
			'  <td><a class="textEdit editable editable-click">'+ ((par.min==  null) ? '' : par.min) +'</a></td>' +
			'  <td><a class="textEdit editable editable-click">'+ ((par.max ==  null) ? '' : par.max) +'</a></td>' +
			'  <td><a class="description editable editable-click">'+ ((par.description ==  null) ? '' : par.description) +'</a></td>' +
			'</tr>';

	$("#par_table_body").append(row);
}

function check_dependent(category){

	 $(".checkeable").click(function() {

	 	el = $(this).parent().parent();
	 	
	 	if($(this).prop("checked")){
	 	
	 		el.find("td:eq(0)").removeClass('col-sm-1 item-first some-success').removeClass('col-sm-1 item-first some-remove');
	 		el.find("td:eq(4)").html("-");
	 		el.find("td:eq(5)").html("-");
	 		el.data('type', 'I');
	 		el.find("td:eq(0)").off( "click");
	 	}else{

	 		if(category != 'S'){
				if(el.data('status') == 2)
		 			el.find("td:eq(0)").addClass('col-sm-1 item-first some-success')
		 		else
		 			el.find("td:eq(0)").addClass('col-sm-1 item-first some-remove');
	 		} else {
				if(el.data('status') == 2)
		 			el.find("td:eq(0)").addClass('col-sm-1 item-first')
		 		else
		 			el.find("td:eq(0)").addClass('col-sm-1 item-first');
	 		}
			
	 		el.find("td:eq(4)").html('<a class="textEdit editable editable-click"></a>')
	 		el.find("td:eq(5)").html('<a class="textEdit editable editable-click"></a>')
 			el.data('type', 'P');

	 		editable_text();
	 		$("td.item-first").on("click", function(){ change_model2Schema($(this))});
	 		
	 	}        
    });  
}

function editable_text(){

	$('#tableList td a.textEdit').editable({
	    type: 'text',
	    name: 'description',
	    url: '/post',
	    title: 'Enter value',
	    validate: function(value) {
	    	
	    		if (isNaN(value)) return 'Not a number';
		}
	});

}

function editable_description(){

	$('#tableList td a.description').editable({
	    type: 'textarea',
	    name: 'description',
	    url: '/post',
	    title: 'Enter description'
	});

}
	
function editable_unit()
{
	
	$('#tableList td a.selectUnit').editable({
	    type: 'select2',
	    name: 'description',
	    title: 'Enter unit',
       	source: units,
	    select2:{
            width: '230px',
            allowClear: true,   
        }
	});
}

function change_parameters(name, category)
{

	$("#formula, #boundary_conditions").change(function(){

		if($("#formula").val() == ''){
			$("#formula").val($("#formula").data('value'));
			$("#boundary_conditions").val($("#boundary_conditions").data('value'));
			print_error("error", "Incorrect Formula 1", "Error!");
			return;
		}

		try{
			cond = ($("#boundary_conditions").val() == "") ? "" : "*" + $("#boundary_conditions").val()
			var tree = jsep($("#formula").val() + cond);
		}catch(err) {
			$("#formula").val($("#formula").data('value'));
			$("#boundary_conditions").val($("#boundary_conditions").data('value'));
			print_error("error", "Incorrect Formula 2", "Error!");
			return;
		}
		
		var ids = [name];

		function parseIds(node) {
			if (node == null) {
				return;
			}
			parseIds(node.left);
			parseIds(node.right);
			// Parse arguments
			if (node.arguments != null) {
					for (var narg = 0; narg < node.arguments.length; narg++) {
						parseIds(node.arguments[narg]);
					}
				}
			if (node.type == "Identifier") {
				ids.push(node.name);
			}
		}

		parseIds(tree);

		ids = ids.filter(function(item, pos, self) {
			return self.indexOf(item) == pos;
		});

		cad = "";
		$.each(ids, function(i,e){
			cad += "#"+e+","; 

			if($("#"+e).length ==0){

				par = {name : e, min: '', max: '', description: '', unit: ''}; 
				add_parameter(par, 'P', '' ,'', category);
				editable_text();
				editable_description();
				editable_unit();
				check_dependent(category)
				$("td.item-first").on("click", function(){ change_model2Schema($(this))});
			}

		});

		if(category == "S"){
			$("#ddlFormula option:first-child").attr("selected", "selected");
			$("#ddlFormula").select2();
		}

		cad =  cad.substring(0, cad.length-1);
		$("#par_table_body tr:not('"+cad+"')").remove();
	
		$("#formula").data('value',$("#formula").val());
		$("#boundary_conditions").data('value',$("#boundary_conditions").val());
	});

}

function load_formula(){

	$("#ddlFormula").on("change", function(){

		if($(this).val() == '') return;

		num = $(this).val();
		Sec = $.grep(secondary, function(e){ return e.Model2Schema.CatModelSec.id == num; });
		sel = Sec[0].Model2Schema;

		$("#name").val(sel.CatModelSec.name);
		$("#ddlCategory").val(sel.CatModelSec.modelClass).change();

		var sbtr = sel.CatModelSec.formula;
		if (sbtr.lastIndexOf("*((((") == -1){
			var formula = sbtr.substring(sbtr.indexOf("=")+1);
			var boundary =  '';
		} else {
			var formula = sbtr.substring(sbtr.indexOf("=")+1,sbtr.lastIndexOf("*(((("));
			var boundary = sbtr.substring(sbtr.lastIndexOf("*((((")+6,sbtr.lastIndexOf(")))))"));
		}

		$("#formula").val(formula).data('value',formula)
	 	$("#boundary_conditions").val(boundary);
	 	$("#par_table_body").html("");

	 	id = $("#primary").val()
	 	value = $("#secondary").val();

	 	/*Add Dependent parameter*/
	 	if(value != -1)	sel.DependentSec.name = $("#model"+ id +"-"+ value +"").data('name');
	 	else 			sel.DependentSec.name = sel.DependentSec.origname;
		add_parameter(sel.DependentSec, 'D',"-", "-");

		// Add independent parameter
		for (var p = 0; p < sel.IndependentSec.length; p++) {
			add_parameter(sel.IndependentSec[p], 'I',"-", "-");
		}

		//Add parameter
		var mod = sel.ParameterSec;
		for (var p = 0; p < sel.ParameterSec.length; p++) {
			value = (sel.ParameterSec[p].value ==  null) ? 			'' : sel.ParameterSec[p].value ;
			error = (sel.ParameterSec[p].error ==  null) ? 			'' : sel.ParameterSec[p].error ;
			add_parameter(sel.ParameterSec[p], 'P', value, error, 'S');	
		}

		check_dependent('category');
		editable_text();
		editable_description();
		editable_unit();
	});

}

function save_formula(id, value, category){

	formula = $("#formula").val();
	boundary_conditions = $("#boundary_conditions").val();
	var cond = (boundary_conditions == "")? "": "*((((("+boundary_conditions+")))))";
	var mod = get_category(id, value, category, "CatModel");

	mod.sel.name = $("#name").val();
	mod.sel.modelClass =  $("#ddlCategory").val()*1;

	if(category != 'S'){
		rule = "Value="+formula+cond;

		if (mod.sel.formula != rule){
			mod.sel.id = get_random();
			mod.sel.formula = rule;
			mod.sel.dbuuid = "";
		}

	}else{

		if(value != -1)	name = $("#model"+ id +"-"+ value +"").data('name');
	 	else 			name = models[id].Model2Schema.DependentSec.name;

		mod.sel.formula = name+"="+formula+cond;

		if($("#ddlFormula").val() == ""){
			mod.sel.id = get_random();	
			mod.sel.dbuuid = "";
		}else{
			Sec = $.grep(secondary, function(e){ return e.Model2Schema.CatModelSec.id ==  $("#ddlFormula").val();});
			sel = Sec[0].Model2Schema.CatModelSec;

			mod.sel.comment = sel.comment;
			mod.sel.id = sel.id;	
			mod.sel.dbuuid = sel.dbuuid;
		}
	}

	save_parameters(id, value, category);

	print_error("success", "Save Formula & Parameters", "Save!");
}

function save_parameters(id, value, category){

	Depen = {};	Secon = {};
	Indep = [];	Param = [];

	if(category != 'S'){
		model = models[id].Model1Schema;
	}else if(value == -1){
		model = models[id].Model2Schema;
	}else{
		model = models[id].Model2Schema[value];
	}

	$("#par_table_body tr").each(function(){

		cat 	= null; 
		name 	= $(this).data('name');
		desc 	= $(this).find("td:eq(8) a").text();
		max 	= $(this).find("td:eq(7) a").text();
		min 	= $(this).find("td:eq(6) a").text();   
		unit 	= $(this).find("td:eq(2) a").text();
		val 	= $(this).find("td:eq(4) a").text();
		error 	= $(this).find("td:eq(5) a").text();
		
		for (var el in units ){ 

			item = $.grep(units[el].children, function(e){ return e.id== unit; });
			if(item.length != 0){
				cat = units[el].text;
				break;
			}
		}

		var object1 = {};
		object1['category'] 	= cat;
		object1['description'] 	= (desc == 'Empty')? null : desc;
		object1['max'] 			= (max == 'Empty')? null : max;
		object1['min'] 			= (min == 'Empty')? null : min;
		object1['name']			= name;
		object1['origname'] 	= name;
		object1['unit'] 		= (unit == 'Empty')? null : unit;


		//Save Dependent & Independents & Parameters
		if($(this).data('type') == 'D')
			Depen = object1;
		else if($(this).data('type') == 'I')
			Indep.push(object1);
		else if($(this).data('type') == 'P'){

			var mod = get_category(id, value, category, "Parameter")

			Par = $.grep(mod.sel, function(e){ return e.name == name; });

			if(Par.length == 0){
				object1['P'] 			= null;
				object1['t'] 			= null;
				object1['correlation'] 	= null;
				object1['maxGuess'] 	= null;
				object1['minGuess'] 	= null;
			}else{
				object1['P'] 			= Par[0].P;
				object1['t'] 			= Par[0].t;
				object1['correlation'] 	= Par[0].correlation;
				object1['maxGuess'] 	= Par[0].maxGuess;
				object1['minGuess'] 	= Par[0].minGuess;
				
			}

			object1['value'] 		= (val == 'Empty')? null : value;
			object1['error'] 		= (error == 'Empty')? null : error;
			Param.push(object1);
		
		}
	});

	if(category != 'S'){
		model.Dependent = Depen;
		model.Independent = Indep;
		model.Parameter = Param;
		check_model2Schema(id)
	}else{
		model.DependentSec = Depen;
		model.IndependentSec = Indep;
		model.ParameterSec = Param;
	}
}

/**************** FUNCTION SECONDARY MODELS ****************/ 
function open_models2(el){

	i = el.data('id');

	if(el.data('status') == 'close'){
		$("#collapse"+ i +" .some-normal").click()
		$('#collapse'+ i +' .second[data-type="model"]').slideDown();
		el.data('status', 'open').addClass('some-second-minus').removeClass('some-second-plus');
		
	}else if(el.data('status') == 'open'){

		$('#collapse'+ i +' .second[data-type="model"]').slideUp();
		el.data('status', 'close').addClass('some-second-plus').removeClass('some-second-minus');
		$("#collapse"+ i +" .some-normal").click()
	}
}

function open_model2Schema(el){

	if(el.data('status') == 'close'){
		$("#collapse"+ i +" .some-normal").click()
		$('.second[data-id="'+el.data('id')+'"][data-value="'+ el.data('value') +'"]:not([data-type="model"])').slideDown();
		el.data('status', 'open').addClass('some-normal').removeClass('some-accordion');
		
	}
	else if(el.data('status') == 'open'){

		$('.second[data-id="'+ el.data('id') +'"][data-value="'+ el.data('value') +'"]:not([data-type="model"])').slideUp();
		el.data('status', 'close').addClass('some-accordion').removeClass('some-normal');
	}

}

function change_model2Schema(el){
	data = el.parent().data();

	if(data.status == 1){	//NORMAL TO ADD
		el.parent().data('status', 2);
		el.addClass('some-success').removeClass('some-remove');
	}else if (data.status == 2){ //ADD TO DELETE
		el.parent().data('status', 1);
		el.addClass('some-remove').removeClass('some-success');
	}
}

function check_model2Schema(id){

	var model2Schema = [];
	
 	$("#par_table_body tr td[class='col-sm-1 item-first some-success']").each(function(){

		name =	$(this).parent().attr('id');

		second = $(".item-second[data-id='"+id+"'][data-name='"+name+"']");

		if(second.length != 0){
			model2Schema.push(models[id].Model2Schema[second.data('value')])	
		}else{
			
			var nsecon = JSON.parse(JSON.stringify(secondary[0].Model2Schema));
			var sbtr =  nsecon.CatModelSec.formula;
			var formula = sbtr.substring(sbtr.indexOf("=")+1);

			var num = model2Schema.length;
			model2Schema.push(nsecon);
			model2Schema[num].DependentSec.name = name;
			model2Schema[num].CatModelSec.formula = name+"="+formula;		
			
		}
	});

	if(model2Schema.length == 0){
		delete models[id].Model2Schema;
	}else{
		models[id].Model2Schema = model2Schema;
	}

	//Reload accordion
	$("#sidebar").html("");
	create_accordion();
	load_block();
	$("#collapse"+id).collapse('show');
	$("#collapse"+id+" .clickable[data-type='formula']").addClass('pencil-active');

}

/****************GLOBAL FUNCTIONS****************/
function load_combo(name, data, selected, value, text)
{
	var list = '';

	if (value) list = "<option value=''></option>";
	if (data != null) {
		for(var i=0; i < data.length; i++){
			dat = (name == '#ddlFormula')? data[i].Model2Schema.CatModelSec : data[i]

			var sel = (selected == dat.id) ? "selected" : "" ;
			list += "<option value='" + dat['id'] + "' " + sel+ ">" + dat[text] + "</option>";				
		}      
	} else {
		list = "<option value=''></option>";
	}
		
	$(name).html(list);

	if(name == '#ddlReference'){
		$('#ref_table_body tr').each(function(){
			$('#ddlReference option[value="' + $(this).data('id')+ '"').remove();
		})
	}
	
}

function get_category(id, value, category, tipo){

	switch(category) {
	    case 'T':
	       	title = 'Tertiary';
			selected = "models["+id+"].Model1Schema."+tipo;
	        break;
	    case 'P':
        	title = 'Primary';
			selected = "models["+id+"].Model1Schema."+tipo;
	        break;
	    case 'S':
        	title = 'Secondary';
        	if(value == -1)
        		selected = "models["+id+"].Model2Schema."+tipo+"Sec";
        	else
        		selected = "models["+id+"].Model2Schema["+value+"]."+tipo+"Sec";
	        break;
	}

	return {sel: eval(selected), title: title};  
}

function get_random(){
	var randNumMin = -999999;
	var randNumMax = -100000;
	return (Math.floor(Math.random() * (randNumMax - randNumMin + 1)) + randNumMin);
}

function print_error(type, msg, title){
	toastr.options = {
		"closeButton": true,
		"debug": true,
		"newestOnTop": false,
		"progressBar": true,
		"positionClass": "toast-top-right",
		"preventDuplicates": false,
		"showDuration": "300",
		"hideDuration": "100",
		"timeOut": "5000",
		"extendedTimeOut": "1000",
		"showEasing": "swing",
		"hideEasing": "linear",
		"showMethod": "fadeIn",
		"hideMethod": "fadeOut"
	}
	
	return toastr[type](msg, title);
}