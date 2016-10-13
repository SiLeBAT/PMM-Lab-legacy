metadata_editor = function () {

    var editor = {
	   version: "0.0.1"
    };
    editor.name = "FSK Metadata Editor";

    var _value;

    editor.init = function (representation, value)
    {
        alert(JSON.stringify(value));
        _value = value;
        create_body ();
    };

    editor.getComponentValue = function ()
    {
        return _value;
    };

    return editor;

    // --- utility functions ---
    function create_body ()
    {
        // Utility variables.
        // - Replace null strings with empty strings
        var modelName = _value.modelName === null ? "" : _value.modelName;
        var modelId = _value.modelId === null ? "" : _value.modelId;
        var modelLink = _value.modelLink === null ? "" : _value.modelLink;
        var organism = _value.organism === null ? "" : _value.organism;
        var organismDetails = _value.organismDetails === null ? "" : _value.organismDetails;
        var matrix = _value.matrix === null ? "" : _value.matrix;
        var matrixDetails = _value.matrixDetails === null ? "" : _value.matrixDetails;
        var contact = _value.contact = _value.contact === null ? "" : _value.contact;
        var referenceDescription = _value.referenceDescription === null ? "" : _value.referenceDescription;
        var referenceDescriptionLink = _value.referenceDescriptionLink === null ? "" : _value.referenceDescriptionLink;
        var createdDate = _value.createdDate === null ? "" : _value.createdDate;
        var modifiedDate = _value.modifiedDate === null ? "" : _value.modifiedDate;
        var rights = _value.rights === null ? "" : _value.rights;
        var notes = _value.notes === null ? "" : _value.notes;
        // curated is boolean: no need to assign it a default value
        var modelType = _value.modelType === null ? "" : _value.modelType;
        var modelSubject = _value.modelSubject === null ? "" : _value.modelSubject;
        var foodProcess = _value.foodProcess === null ? "" : _value.foodProcess;
        // var depvar = _value.dependentVariable === null ? "" : _value.dependentVariable;
        // var depvar_unit = _value.dependentVariableUnit === null ? "" : _value.dependentVariableUnit;
        // var depvar_min = isNaN(_value.dependentVariableMin) ? "" : _value.dependentVariableMin;
        // var depvar_max = isNaN(_value.dependentVariableMax) ? "" : _value.dependentVariableMax;
        // var indepvars = _value.independentVariables === null ? "" : _value.independentVariables;
        // var indepvar_units = _value.independentVariableUnits === null ? "" : _value.independentVariableUnits;
        // var indepvar_mins = _value.independentVariableMins === null ? "" : _value.independentVariableMins;
        // var indepvar_maxs = _value.independentVariableMaxs === null ? "" : _value.independentVariableMaxs;
        // var indepvar_values = _value.independentVariableValues === null ? "" : _value.independentVariableValues;

        var varTable =
            '<table class="table table-condensed">' +
            '  <tr>' +
            '    <th>Name</th>' +
            '    <th>Unit</th>' +
            '    <th>Value</th>' +
            '    <th>Min</th>' +
            '    <th>Max</th>' +
            '    <th>Dependent</th>'
            '  </tr>';
        for (var i = 0; i < _value.variables.length; i++) {
            var variable = _value.variables[i];
            varTable +=
                '<tr>' +
                '  <td>' + variable.name + '</td>' +
                '  <td>' + variable.unit + '</td>' +
                '  <td>' + variable.value + '</td>' +
                '  <td>' + variable.min + '</td>' +
                '  <td>' + variable.max + '</td>' +
                // '  <td>' + variable.isDependent + '</td>' +
                '  <td><input type="checkbox"' + (variable.isDependent ? "checked" : "") + '></td>' +
                '</tr>';
        }
        varTable += '</table>';

        var form = 
            '<div class="container">' +

            '  <form class="form-horizontal">' +

            // Model name form
            '    <div class="form-group form-group-sm">' +
            '      <label for="modelName" class="col-sm-3 control-label">Model name</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="modelNameInput" value="' + modelName + '">' +
            '      </div>' +
            '    </div>' +

            // Model id form
            '    <div class="form-group form-group-sm">' +
            '      <label for="modelId" class="col-sm-3 control-label">Model id</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="modelIdInput" value="' + modelId + '">' +
            '      </div>' +
            '    </div>' +

            // Model link form
            '    <div class="form-group form-group-sm">' +
            '      <label for="modelLink" class="col-sm-3 control-label">Model link</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="url" class="form-control" id="modelLinkInput" value="' + modelLink + '">' +
            '      </div>' +
            '    </div>' +

            // Organism form
            '    <div class="form-group form-group-sm">' +
            '      <label for="organism" class="col-sm-3 control-label">Organism</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="organismInput" value="' + organism + '">' +
            '      </div>' +
            '    </div>' +

            // Organism details form
            '    <div class="form-group form-group-sm">' +
            '      <label for="organismDetails" class="col-sm-3 control-label">Organism details</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="organismDetailsInput" value="' + organismDetails + '">' +
            '      </div>' +
            '    </div>' +

            // Matrix form
            '    <div class="form-group form-group-sm">' +
            '      <label for="matrix" class="col-sm-3 control-label">Matrix</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="matrixInput" value="' + matrix + '">' +
            '      </div>' +
            '    </div>' +

            // Matrix details form
            '    <div class="form-group form-group-sm">' +
            '      <label for="matrixDetails" class="col-sm-3 control-label">Matrix details</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="matrixDetailsInput" value="' + matrixDetails + '">' +
            '      </div>' +
            '    </div>' +

            // Contact form
            '    <div class="form-group form-group-sm">' +
            '      <label for="contact" class="col-sm-3 control-label">Contact</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="contactInput" value="' + contact + '">' +
            '      </div>' +
            '    </div>' +

            // Reference description form
            '    <div class="form-group form-group-sm">' +
            '      <label for="referenceDescription" class="col-sm-3 control-label">Referece description</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="referenceDescriptionInput" value="' + referenceDescription + '">' +
            '      </div>' +
            '    </div>' +

            // Reference description link form
            '    <div class="form-group form-group-sm">' +
            '      <label for="referenceDescriptionLink" class="col-sm-3 control-label">Referece description link</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="url" class="form-control" id="referenceDescriptionLinkInput" value="' + referenceDescriptionLink + '">' +
            '      </div>' +
            '    </div>' +

            // Created date form
            '    <div class="form-group form-group-sm">' +
            '      <label for="createdDate" class="col-sm-3 control-label">Created date</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="date" class="form-control" id="createdDateInput" value="' + createdDate + '">' +
            '      </div>' +
            '    </div>' +

            // Modified date form
            '    <div class="form-group form-group-sm">' +
            '      <label for="modifiedDate" class="col-sm-3 control-label">Modified date</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="date" class="form-control" id="modifiedDateInput" value="' + modifiedDate + '">' +
            '      </div>' +
            '    </div>' +

            // Rights form
            '    <div class="form-group form-group-sm">' +
            '      <label for="rights" class="col-sm-3 control-label">Rights</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="rightsInput" value="' + rights + '">' +
            '      </div>' +
            '    </div>' +

            // Notes form
            '    <div class="form-group">' +
            '      <label for="notes" class="col-sm-3 control-label">Notes</label>' +
            '      <div class="col-sm-9">' +
            // '        <input type="text" class="form-control" id="notesInput" value="' + notes + '">' +
            '        <textarea class="form-control" rows="3">' + notes + '</textArea>' +
            '      </div>' +
            '    </div>' +

            // Curated form
            '    <div class="form-group form-group-sm">' +
            '      <label for="curated" class="col-sm-3 control-label">Curated</label>' +
            '      <div class="col-sm-9">' +
            '        <input id="curatedInput" type="checkbox"' + (_value.curated ? " checked" : "") + '>' +
            '      </div>' +
            '    </div>' +

            // Model type form
            '    <div class="form-group form-group-sm">' +
            '      <label for="modelType" class="col-sm-3 control-label">Model type</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="modelTypeInput" value="' + modelType + '">' +
            '      </div>' +
            '    </div>' +

            // Model subject form
            '    <div class="form-group form-group-sm">' +
            '      <label for="modelSubject" class="col-sm-3 control-label">Model subject</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="modelSubjectInput" value="' + modelSubject + '">' +
            '      </div>' +
            '    </div>' +

            // Food process form
            '    <div class="form-group form-group-sm">' +
            '      <label for="foodProcess" class="col-sm-3 control-label">Food process</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="foodProcessInput" value="' + foodProcess + '">' +
            '      </div>' +
            '    </div>' +

/*            // Dependent variable form
            '    <div class="form-group form-group-sm">' +
            '      <label for="depvar" class="col-sm-3 control-label">Dependent variable</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="depvarInput" value="' + depvar + '">' +
            '      </div>' +
            '    </div>' +

            // Dependent variable unit form
            '    <div class="form-group form-group-sm">' +
            '      <label form=depvar_unit" class="col-sm-3 control-label">Dependent variable unit</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="depvar_unitInput" value="' + depvar_unit + '">' +
            '      </div>' +
            '    </div>' +

            // Dependent variable min form
            '    <div class="form-group form-group-sm">' +
            '      <label form=depvar_min" class="col-sm-3 control-label">Dependent variable minimum value</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="depvar_minInput" value="' + depvar_min + '">' +
            '      </div>' +
            '    </div>' +

            // Dependent variable max form
            '    <div class="form-group form-group-sm">' +
            '      <label form=depvar_max" class="col-sm-3 control-label">Dependent variable maximum value</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="depvar_maxInput" value="' + depvar_max + '">' +
            '      </div>' +
            '    </div>' +

            // Independent variables form
            '    <div class="form-group form-group-sm">' +
            '      <label form"indepvar" class="col-sm-3 control-label">Independent variables</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="indepvarInput" value="' + indepvars + '">' +
            '      </div>' +
            '    </div>' +

            // Independent variable units form
            '    <div class="form-group form-group-sm">' +
            '      <label form"indepvar_unit" class="col-sm-3 control-label">Independent variable units</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="indepvar_unitInput" value="' + indepvar_units + '">' +
            '      </div>' +
            '    </div>' +

            // Independent variable mins form
            '    <div class="form-group form-group-sm">' +
            '      <label form"indepvar_mins" class="col-sm-3 control-label">Independent variable mins</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="indepvar_minsInput" value="' + indepvar_mins + '">' +
            '      </div>' +
            '    </div>' +

            // Independent variable max form
            '    <div class="form-group form-group-sm">' +
            '      <label form"indepvar_maxs" class="col-sm-3 control-label">Independent variable maxs</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="indepvar_maxsInput" value="' + indepvar_maxs + '">' +
            '      </div>' +
            '    </div>' +

            // Independent variable values form
            '    <div class="form-group form-group-sm">' +
            '      <label form"indepvar_values" class="col-sm-3 control-label">Independent variable values</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="indepvar_valuesInput" value="' + indepvar_values + '">' +
            '      </div>' +
            '    </div>' +
*/
            // Has data form
            '    <div class="form-group form-group-sm">' +
            '      <label for="hasData" class="col-sm-3 control-label">Has data?</label>' +
            '      <div class="col-sm-9">' +
            '        <input id="hasDataInput" type="checkbox"' + (_value.hasData ? " checked" : "") + '>' +
            '      </div>' +
            '    </div>' +

            // Reset and save buttons
            '    <div class="form-group">' +
            '      <div class="col-sm-offset-3 col-sm-9">' +
            '        <button id="resetButton" type="button" class="btn btn-warning">Reset</button>' +
            '        <button id="saveButton" type="button" class="btn btn-success">Save</button>' +
            // '        <input id="resetButton" class="btn btn-default" type="button" value="Reset">' +
            // '        <input id="saveButton" class="btn btn-default" type="button" value="Save">' +
            '      </div>' +
            '    </div>' +

            '  </form>' +
            '</div>';

        document.createElement("body");
        $("body").html(form);
        $("body").append(varTable);

        $("#resetButton").click(reset);
        $("#saveButton").click(save);
    }

    function reset ()
    {
        $("#modelNameInput").val(_value.modelName === null ? "" : _value.modelName);
        $("#modelIdInput").val(_value.modelId === null ? "" : _value.modelId);
        $("#modelLinkInput").val(_value.modelLink === null ? "" : _value.modelLink);
        $("#organismInput").val(_value.organism === null ? "" : _value.organism);
        $("#organismDetailsInput").val(_value.organismDetails === null ? "" : _value.organismDetails);
        $("#matrixInput").val(_value.matrix === null ? "" : _value.matrix);
        $("#matrixDetailsInput").val(_value.matrixDetails === null ? "" : _value.matrixDetails);
        $("#contactInput").val(_value.contact === null ? "" : _value.contact);
        $("#referenceDescriptionInput").val(_value.referenceDescription === null ? "" : _value.referenceDescription);
        $("#referenceDescriptionLinkInput").val(_value.referenceDescriptionLink === null ? "" : _value.referenceDescriptionLink);
        $("#createdDateInput").val(_value.createdDate === null ? "" : _value.createdDate);
        $("#modifiedDateInput").val(_value.modifiedDate === null ? "" : _value.modifiedDate);
        $("#rightsInput").val(_value.rights === null ? "" : _value.rights);
        $("#notesInput").val(_value.notes === null ? "" : _value.notes);
        $("#curatedInput").prop("checked", _value.curated);
        $("#modelTypeInput").val(_value.modelType === null ? "" : _value.modelType);
        $("#modelSubjectInput").val(_value.modelSubject === null ? "" : _value.modelSubject);
        $("#foodProcessInput").val(_value.foodProcess === null ? "" : _value.foodProcess);
        $("#depvarInput").val(_value.dependentVariable === null ? "" : _value.dependentVariable);
        $("#depvar_unitInput").val(_value.dependentVariableUnit === null ? "" : _value.dependentVariableUnit);
        $("#depvar_minInput").val(_value.dependentVariableMin === null ? "" : _value.dependentVariableMin);
        $("#depvar_maxInput").val(_value.dependentVariableMax === null ? "" : _value.dependentVariableMax);
        $("#indepvarInput").val(_value.independentVariables === null ? "" : _value.independentVariables);
        $("#indepvar_unitInput").val(_value.independentVariableUnits === null ? "" : _value.independentVariableUnits);
        $("#indepvar_minsInput").val(_value.independentVariableMins === null ? "" : _value.independentVariableMins);
        $("#indepvar_maxsInput").val(_value.independentVariableMaxs === null ? "" : _value.independentVariableMaxs);
        $("#indepvar_valuesInput").val(_value.independentVariableValues === null ? "" : _value.independentVariableValues);
        $("#hasDataInput").prop("checked", _value.hasData);
    }


    function save ()
    {
        _value.modelName = $("#modelNameInput").val();
        _value.modelId = $("#modelIdInput").val();
        _value.modelLink = $("#modelLinkInput").val();
        _value.organism = $("#organismInput").val();
        _value.organismDetails = $("#organismDetailsInput").val();
        _value.matrix = $("#matrixInput").val();
        _value.matrixDetails = $("#matrixDetailsInput").val();
        _value.contact = $("#contactInput").val();
        _value.referenceDescription = $("#referenceDescriptionInput").val();
        _value.referenceDescriptionLink = $("#referenceDescriptionLinkInput").val();
        _value.createdDate = $("#createdDateInput").val();
        _value.modifiedDate = $("#modifiedDateInput").val();
        _value.rights = $("#rightsInput").val();
        _value.notes = $("#notesInput").val();
        _value.curated = $("#curatedInput").is(':checked');
        _value.modelType = $("#modelTypeInput").val();
        _value.modelSubject = $("#modelSubjectInput").val();
        _value.foodProcess = $("#foodProcessInput").val();
        // _value.dependentVariable = $("#depvarInput").val();
        // _value.dependentVariableUnit = $("#depvar_unitInput").val();
        // _value.dependentVariableMin = $("#depvar_minInput").val();
        // _value.dependentVariableMax = $("#depvar_maxInput").val();
        // _value.independentVariables = $("#indepvarInput").val() ? $("indepvarInput") : [];
        // _value.independentVariables = $("#indepvarInput").val();
        // _value.independentVariableUnits = $("#indepvar_unitInput").val();
        // _value.independentVariableMins = $("#indepvar_minsInput").val();
        // _value.independentVariableMaxs = $("#indepvar_maxsInput").val();
        // _value.independentVariableValues = $("#indepvar_valuesInput").val();
        _value.hasData = $("#hasDataInput").is(':checked');
    }
}();
