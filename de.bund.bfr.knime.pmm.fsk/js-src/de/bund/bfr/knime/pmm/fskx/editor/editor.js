metadata_editor = function () {

    var editor = {
	   version: "0.0.1"
    };
    editor.name = "FSK Metadata Editor";

    var _value;

    editor.init = function (representation, value)
    {
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
                '  <td><input type="number" class="form-control input-sm" value="' + variable.value + '"></td>' +
                '  <td><input type="number" class="form-control input-sm" value="' + variable.min + '"></td>' +
                '  <td><input type="number" class="form-control input-sm" value="' + variable.max + '"></td>' +
                '  <td><input type="checkbox" class="form-control" ' + (variable.isDependent ? "checked" : "") + ' disabled></td>' +
                '</tr>';
        }
        varTable += '</table>';

        var form = 
            '<form class="form-horizontal">' +

            // Model name form
            '  <div class="form-group form-group-sm">' +
            '    <label for="modelName" class="col-sm-3 control-label">Model name</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control" id="modelNameInput" value="' + modelName + '">' +
            '    </div>' +
            '  </div>' +

            // Model id form
            '  <div class="form-group form-group-sm">' +
            '    <label for="modelId" class="col-sm-3 control-label">Model id</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control" id="modelIdInput" value="' + modelId + '">' +
            '    </div>' +
            '  </div>' +

            // Model link form
            '  <div class="form-group form-group-sm">' +
            '    <label for="modelLink" class="col-sm-3 control-label">Model link</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="url" class="form-control" id="modelLinkInput" value="' + modelLink + '">' +
            '    </div>' +
            '  </div>' +

            // Organism form
            '  <div class="form-group form-group-sm">' +
            '    <label for="organism" class="col-sm-3 control-label">Organism</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control" id="organismInput" value="' + organism + '">' +
            '    </div>' +
            '  </div>' +

            // Organism details form
            '  <div class="form-group form-group-sm">' +
            '    <label for="organismDetails" class="col-sm-3 control-label">Organism details</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control" id="organismDetailsInput" value="' + organismDetails + '">' +
            '    </div>' +
            '  </div>' +

            // Matrix form
            '  <div class="form-group form-group-sm">' +
            '    <label for="matrix" class="col-sm-3 control-label">Matrix</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control" id="matrixInput" value="' + matrix + '">' +
            '    </div>' +
            '  </div>' +

            // Matrix details form
            '  <div class="form-group form-group-sm">' +
            '    <label for="matrixDetails" class="col-sm-3 control-label">Matrix details</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control" id="matrixDetailsInput" value="' + matrixDetails + '">' +
            '    </div>' +
            '  </div>' +

            // Contact form
            '  <div class="form-group form-group-sm">' +
            '    <label for="contact" class="col-sm-3 control-label">Contact</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control" id="contactInput" value="' + contact + '">' +
            '    </div>' +
            '  </div>' +

            // Software form
            '  <div class="form-group form-group-sm">' +
            '    <label for="software" class="col-sm-3 control-label">Software</label>' +
            '    <div class="col-sm-9">' +
            '      <select class="form-control" id="softwareInput">' +
            '        <option>R</option>' +
            '        <option>Matlab</option>' +
            '      </select>' +
            '    </div>' +
            '  </div>' +

            // Reference description form
            '  <div class="form-group form-group-sm">' +
            '    <label for="referenceDescription" class="col-sm-3 control-label">Referece description</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control" id="referenceDescriptionInput" value="' + referenceDescription + '">' +
            '    </div>' +
            '  </div>' +

            // Reference description link form
            '  <div class="form-group form-group-sm">' +
            '    <label for="referenceDescriptionLink" class="col-sm-3 control-label">Referece description link</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="url" class="form-control" id="referenceDescriptionLinkInput" value="' + referenceDescriptionLink + '">' +
            '    </div>' +
            '  </div>' +

            // Created date form
            '  <div class="form-group form-group-sm">' +
            '    <label for="createdDate" class="col-sm-3 control-label">Created date</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="date" class="form-control" id="createdDateInput" value="' + createdDate + '">' +
            '    </div>' +
            '  </div>' +

            // Modified date form
            '  <div class="form-group form-group-sm">' +
            '    <label for="modifiedDate" class="col-sm-3 control-label">Modified date</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="date" class="form-control" id="modifiedDateInput" value="' + modifiedDate + '">' +
            '    </div>' +
            '  </div>' +

            // Rights form
            '  <div class="form-group form-group-sm">' +
            '    <label for="rights" class="col-sm-3 control-label">Rights</label>' +
            '      <div class="col-sm-9">' +
            '      <input type="text" class="form-control" id="rightsInput" value="' + rights + '">' +
            '    </div>' +
            '  </div>' +

            // Notes form
            '  <div class="form-group">' +
            '    <label for="notes" class="col-sm-3 control-label">Notes</label>' +
            '    <div class="col-sm-9">' +
            // '      <input type="text" class="form-control" id="notesInput" value="' + notes + '">' +
            '      <textarea class="form-control" rows="3">' + notes + '</textArea>' +
            '    </div>' +
            '  </div>' +

            // Curated form
            '  <div class="form-group form-group-sm">' +
            '    <label for="curated" class="col-sm-3 control-label">Curated</label>' +
            '    <div class="col-sm-9">' +
            '      <input id="curatedInput" type="checkbox"' + (_value.curated ? " checked" : "") + '>' +
            '    </div>' +
            '  </div>' +

            // Model type form
            '  <div class="form-group form-group-sm">' +
            '    <label for="modelType" class="col-sm-3 control-label">Model type</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control" id="modelTypeInput" value="' + modelType + '">' +
            '    </div>' +
            '  </div>' +

            // Model subject form
            '  <div class="form-group form-group-sm">' +
            '    <label for="modelSubject" class="col-sm-3 control-label">Model subject</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control" id="modelSubjectInput" value="' + modelSubject + '">' +
            '    </div>' +
            '  </div>' +

            // Food process form
            '  <div class="form-group form-group-sm">' +
            '    <label for="foodProcess" class="col-sm-3 control-label">Food process</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control" id="foodProcessInput" value="' + foodProcess + '">' +
            '    </div>' +
            '  </div>' +

            // Has data form
            '  <div class="form-group form-group-sm">' +
            '    <label for="hasData" class="col-sm-3 control-label">Has data?</label>' +
            '    <div class="col-sm-9">' +
            '      <input id="hasDataInput" type="checkbox"' + (_value.hasData ? " checked" : "") + '>' +
            '    </div>' +
            '  </div>' +

            '</form>';

        var buttonDiv = 
            '<div class="col-sm-offset-3">' +
            '  <button id="resetButton" type="button" class="btn btn-warning">Reset</button>' +
            '  <button id="saveButton" type="button" class="btn btn-success">Save</button>' +
            '</div>';

        document.createElement("body");
        $("body").html('<div class="container">' + form + varTable + buttonDiv + '</div');

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
        $("#softwareInput").val(_value.software === null ? "" : _value.software);
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

        var table = $("body div table");
        table.find("tr:gt(0)").remove();
        for (var i = 0; i < _value.variables.length; i++) {
            var variable = _value.variables[i];
            var row =
                '<tr>' +
                '  <td>' + variable.name + '</td>' +
                '  <td>' + variable.unit + '</td>' +
                '  <td><input type="number" class="form-control input-sm" value="' + variable.value + '"></td>' +
                '  <td><input type="number" class="form-control input-sm" value="' + variable.min + '"></td>' +
                '  <td><input type="number" class="form-control input-sm" value="' + variable.max + '"></td>' +
                '  <td><input type="checkbox" class="form-control" ' + (variable.isDependent ? "checked" : "") + ' disabled></td>' +
                '</tr>';
            table.append(row);
        }

        $("#hasDataInput").prop("checked", _value.hasData);
    }


    function save ()
    {
        // var hasErrors = false;

        // // Validate table
        // $("body div table tr:not(:first)").each(function() {

        //     alert("Validate table");

        //     var valueInput = $("td:eq(2) input", this);
        //     var minInput = $("td:eq(3) input", this);
        //     var maxInput = $("td:eq(4) input", this);

        //     // Check NaNs
        //     if (!$.isNumeric(valueInput.val())) {
        //        valueInput.addClass("has-error");
        //        hasErrors = true;
        //        return; 
        //     }

        //     if (!$.isNumeric(minInput.val())) {
        //         minInput.addClass("has-error");
        //         hasErrors = true;
        //         return;
        //     }

        //     if (!$.isNumeric(maxInput.val())) {
        //         maxInput.addClass("has-error");
        //         hasErrors = true;
        //         return;
        //     }

        //     var value = parseFloat(valueInput.val());
        //     var min = parseFloat(minInput.val());
        //     var max = parseFloat(maxInput.val());

        //     alert(value + " " + min + " " + max);

        //     // Validate min and max
        //     if (!(min < max)) {
        //         $("td:eq(3) input", this).addClass("has-error");
        //         hasErrors = true;
        //         return;
        //     }

        //     if (!(max > min)) {
        //         $("td:eq(4) input", this).addClass("has-error");
        //         hasErrors = true;
        //         return;
        //     }

        //     // Validate value
        //     if (isNaN(value) || value > min || value < max) {
        //         $("td:eq(2) input", this).text();
        //         hasErrors = true;
        //         return;
        //     }
        // });

        // if (hasErrors) {
        //     return;
        // }

        _value.modelName = $("#modelNameInput").val();
        _value.modelId = $("#modelIdInput").val();
        _value.modelLink = $("#modelLinkInput").val();
        _value.organism = $("#organismInput").val();
        _value.organismDetails = $("#organismDetailsInput").val();
        _value.matrix = $("#matrixInput").val();
        _value.matrixDetails = $("#matrixDetailsInput").val();
        _value.contact = $("#contactInput").val();
        _value.software = $("#softwareInput").val();
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

        _value.variables = []
        $("body div table tr:not(:first)").each(function() {
            var variable = {};
            variable.name = $("td:eq(0)", this).text();
            variable.unit = $("td:eq(1)", this).text();
            variable.value = $("td:eq(2) input", this).val();
            variable.min = $("td:eq(3) input", this).val();
            variable.max = $("td:eq(4) input", this).val();
            variable.isDependent = $("td:eq(5) input", this).is(":checked");
            _value.variables.push(variable);
        })

        _value.hasData = $("#hasDataInput").is(':checked');
    }
}();
