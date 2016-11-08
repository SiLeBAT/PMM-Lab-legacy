metadata_editor = function () {

    var editor = {
	   version: "0.0.1"
    };
    editor.name = "FSK Metadata Editor";

    var _value;  // Raw FskMetadataEditorViewValue

    editor.init = function (representation, value)
    {
        _value = value;
        _data = value.metadata;
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
        var varTable =
            '<table class="table table-condensed">' +
            '  <tr>' +
            '    <th>Name</th>' +
            '    <th>Unit</th>' +
            '    <th>Type</th>' +
            '    <th>Value</th>' +
            '    <th>Min</th>' +
            '    <th>Max</th>' +
            '    <th>Dependent</th>'
            '  </tr>';
        // Row with dependent variable
        varTable +=
            '<tr>' +
            '  <td>' + _value.metadata.dependentVariable.name + '</td>' +
            '  <td>' + _value.metadata.dependentVariable.unit + '</td>' +
            '  <td>' + _value.metadata.dependentVariable.type + '</td>' +
            '  <td><input type="number" class="form-control input-sm" value="' + _value.metadata.dependentVariable.value + '"></td>' +
            '  <td><input type="number" class="form-control input-sm" value="' + _value.metadata.dependentVariable.min + '"></td>' +
            '  <td><input type="number" class="form-control input-sm" value="' + _value.metadata.dependentVariable.max + '"></td>' +
            '  <td><input type="checkbox" class="form-control" checked disabled></td>' +
            '</tr>';
        // Row with independent variables
        for (var i = 0; i < _value.metadata.independentVariables.length; i++) {
            var variable = _value.metadata.independentVariables[i];
            varTable +=
                '<tr>' +
                '  <td>' + variable.name + '</td>' +
                '  <td>' + variable.unit + '</td>' +
                '  <td>' + variable.type + '</td>' +
                '  <td><input type="number" class="form-control input-sm" value="' + variable.value + '"></td>' +
                '  <td><input type="number" class="form-control input-sm" value="' + variable.min + '"></td>' +
                '  <td><input type="number" class="form-control input-sm" value="' + variable.max + '"></td>' +
                '  <td><input type="checkbox" class="form-control" disabled></td>' +
                '</tr>';
        }
        varTable += '</table>';

        var software = nullToEmpty(_value.metadata.software);

        var form = 
            '<form class="form-horizontal">' +

            // Model name form
            '  <div class="form-group form-group-sm">' +
            '    <label for="modelName" class="col-sm-3 control-label">Model name:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" id="modelNameInput" class="form-control no-border" value="' + nullToEmpty(_value.metadata.modelName) + '">' + 
            '    </div>' +
            '  </div>' +

            // Model id form
            '  <div class="form-group form-group-sm">' +
            '    <label for="modelId" class="col-sm-3 control-label">Model id:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" id="modelIdInput" class="form-control no-border" value="' + nullToEmpty(_value.metadata.modelId) + '">' +
            '    </div>' +
            '  </div>' +

            // Model link form
            '  <div class="form-group form-group-sm">' +
            '    <label for="modelLinkInput" class="col-sm-3 control-label">Model link:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="url" class="form-control no-border" id="modelLinkInput" value="' + nullToEmpty(_value.metadata.modelLink) + '">' +
            '    </div>' +
            '  </div>' +

            // Organism form
            '  <div class="form-group form-group-sm">' +
            '    <label for="organism" class="col-sm-3 control-label">Organism:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="organismInput" value="' + nullToEmpty(_value.metadata.organism) + '">' +
            '    </div>' +
            '  </div>' +

            // Organism details form
            '  <div class="form-group form-group-sm">' +
            '    <label for="organismDetails" class="col-sm-3 control-label">Organism details:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="organismDetailsInput" value="' + nullToEmpty(_value.metadata.organismDetails) + '">' +
            '    </div>' +
            '  </div>' +

            // Matrix form
            '  <div class="form-group form-group-sm">' +
            '    <label for="matrix" class="col-sm-3 control-label">Matrix:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="matrixInput" value="' + nullToEmpty(_value.metadata.matrix) + '">' +
            '    </div>' +
            '  </div>' +

            // Matrix details form
            '  <div class="form-group form-group-sm">' +
            '    <label for="matrixDetails" class="col-sm-3 control-label">Matrix details:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="matrixDetailsInput" value="' + nullToEmpty(_value.metadata.matrixDetails) + '">' +
            '    </div>' +
            '  </div>' +

            // Creator form
            '  <div class="form-group form-group-sm">' +
            '    <label for="creator" class="col-sm-3 control-label">Creator:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="creatorInput" value="' + nullToEmpty(_value.metadata.creator) + '">' +
            '    </div>' +
            '  </div>' +

            // Family name form
            '  <div class="form-group form-group-sm">' +
            '    <label for="familyName" class="col-sm-3 control-label">Family name:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="familyNameInput" value="' + nullToEmpty(_value.metadata.familyName) + '">' +
            '    </div>' +
            '  </div>' +

            // Contact form
            '  <div class="form-group form-group-sm">' +
            '    <label for="contact" class="col-sm-3 control-label">Contact:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="contactInput" value="' + nullToEmpty(_value.metadata.contact) + '">' +
            '    </div>' +
            '  </div>' +

            // Software form
            '  <div class="form-group form-group-sm">' +
            '    <label for="software" class="col-sm-3 control-label">Software:</label>' +
            '    <div class="col-sm-9">' +
            '      <select class="form-control no-border" id="softwareInput" >' +
            '        <option value="R" ' + ("R" === software ? "selected" : "") + '>R</option>' +
            '        <option value="Matlab" ' + ("Matlab" === software ? "selected" : "") + '>Matlab</option>' +
            '      </select>' +
            '    </div>' +
            '  </div>' +

            // Reference description form
            '  <div class="form-group form-group-sm">' +
            '    <label for="referenceDescription" class="col-sm-3 control-label">Reference description:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="referenceDescriptionInput" value="' + nullToEmpty(_value.metadata.referenceDescription) + '">' +
            '    </div>' +
            '  </div>' +

            // Reference description link form
            '  <div class="form-group form-group-sm">' +
            '    <label for="referenceDescriptionLink" class="col-sm-3 control-label">Reference description link:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type"url" class="form-control no-border" id="referenceDescriptionLinkInput" value="' + nullToEmpty(_value.metadata.referenceDescriptionLink) + '">' +
            '    </div>' +
            '  </div>' +

            // Created date form
            '  <div class="form-group form-group-sm">' +
            '    <label for="createdDate" class="col-sm-3 control-label">Created date:</label>' +
            '    <div class="col-sm-9">' +
            // '      <input type="date" class="form-control no-border" id="createdDateInput" placeholder="MM.dd.yyyy" value="' + nullToEmpty(_value.metadata.createdDate) + '">' +
            '      <input type="text" class="form-control no-border" id="createdDateInput" value="' + nullToEmpty(_value.metadata.createdDate) + '">' + 
            '    </div>' +
            '  </div>' +

            // Modified date form
            '  <div class="form-group form-group-sm">' +
            '    <label for="modifiedDate" class="col-sm-3 control-label">Modified date:</label>' +
            '    <div class="col-sm-9">' +
            // '      <input type="date" class="form-control no-border" id="modifiedDateInput" placeholder="MM.dd.yyyy" value="' + nullToEmpty(_value.metadata.modifiedDate) + '">' +
            '      <input type="text" class="form-control no-border" id="modifiedDateInput" value="' + nullToEmpty(_value.metadata.modifiedDate) + '">' + 
            '    </div>' +
            '  </div>' +

            // Rights form
            '  <div class="form-group form-group-sm">' +
            '    <label for="rights" class="col-sm-3 control-label">Rights:</label>' +
            '      <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="rightsInput" value="' + nullToEmpty(_value.metadata.rights) + '">' +
            '    </div>' +
            '  </div>' +

            // Notes form
            '  <div class="form-group form-group-sm">' +
            '    <label for="notes" class="col-sm-3 control-label">Notes:</label>' +
            '    <div class="col-sm-9">' +
            '      <textarea id="notesInput" class="form-control no-border" rows="3">' + nullToEmpty(_value.metadata.notes) + '</textArea>' +
            '    </div>' +
            '  </div>' +

            // Curated form
            '  <div class="form-group form-group-sm">' +
            '    <label for="curated" class="col-sm-3 control-label">Curated:</label>' +
            '    <div class="col-sm-9">' +
            '      <input id="curatedInput" type="checkbox"' + (_value.metadata.curated ? " checked" : "") + '>' +
            '    </div>' +
            '  </div>' +

            // Model type form
            '  <div class="form-group form-group-sm">' +
            '    <label for="modelType" class="col-sm-3 control-label">Model type:</label>' +
            '    <div class="col-sm-9">' +
            '      <select class="form-control no-border" id="typeInput">' +
            '        <option value="EXPERIMENTAL_DATA" ' + ('EXPERIMENTAL_DATA' === _value.metadata.type ? 'selected' : '') + '>Experimental data</option>' +
            '        <option value="PRIMARY_MODEL_WDATA" ' + ('PRIMARY_MODEL_WDATA' === _value.metadata.type ? 'selected' : '') + '>Primary model with data</option>' +
            '        <option value="PRIMARY_MODEL_WODATA" ' + ('PRIMARY_MODEL_WODATA' === _value.metadata.type ? 'selected' : '') + '>Primary model without data</option>' +
            '        <option value="TWO_STEP_SECONDARY_MODEL" ' + ('TWO_STEP_SECONDARY_MODEL' === _value.metadata.type ? 'selected' : '') + '>Two step secondary model</option>' +
            '        <option value="ONE_STEP_SECONDARY_MODEL" ' + ('ONE_STEP_SECONDARY_MODEL' === _value.metadata.type ? 'selected' : '') + '>One step secondary model</option>' +
            '        <option value="MANUAL_SECONDARY_MODEL" ' + ('MANUAL_SECONDARY_MODEL' === _value.metadata.type ? 'selected' : '') + '>Manual secondary model</option>' +
            '        <option value="TWO_STEP_TERTIARY_MODEL" ' + ('TWO_STEP_TERTIARY_MODEL' === _value.metadata.type ? 'selected' : '') + '>Two step tertiary model</option>' +
            '        <option value="ONE_STEP_TERTIARY_MODEL" ' + ('ONE_STEP_TERTIARY_MODEL' === _value.metadata.type ? 'selected' : '') + '>One step tertiary model</option>' +
            '        <option value="MANUAL_TERTIARY_MODEL" ' + ('MANUAL_TERTIARY_MODEL' === _value.metadata.type ? 'selected' : '') + '>Manual tertiary model</option>' +
            '      </select>' +
            '    </div>' +
            '  </div>' +

            // Model subject form
            '  <div class="form-group form-group-sm">' +
            '    <label for="modelSubject" class="col-sm-3 control-label">Model subject:</label>' +
            '    <div class="col-sm-9">' +
            '      <select class="form-control no-border" id="subjectInput">' +
            '        <option value="UNKNOWN" ' + ('UNKNOWN' === _value.metadata.subject ? 'selected' : '') + '>unkown</option>' +
            '        <option value="GROWTH" ' + ('GROWTH' === _value.metadata.subject ? 'selected' : '') + '>growth</option>' +
            '        <option value="INACTIVATION" ' + ('INACTIVATION' === _value.metadata.subject ? 'selected' : '') + '>inactivation</option>' +
            '        <option value="SURVIVAL" ' + ('SURVIVAL' === _value.metadata.subject ? 'selected' : '') + '>survival</option>' +
            '        <option value="GROWTH_INACTIVATION" ' + ('GROWTH_INACTIVATION' === _value.metadata.subject ? 'selected' : '') + '>growth/inactivation</option>' +
            '        <option value="INACTIVATION_SURVIVAL" ' + ('INACTIVATION_SURVIVAL' === _value.metadata.subject ? 'selected' : '') + '>inactivation/survival</option>' +
            '        <option value="GROWTH_SURVIVAL" ' + ('GROWTH_SURVIVAL' === _value.metadata.subject ? 'selected' : '') + '>growth/survival</option>' +
            '        <option value="GROWTH_INACTIVATION_SURVIVAL" ' + ('GROWTH_INACTIVATION_SURVIVAL' === _value.metadata.subject ? 'selected' : '') + '>growth/inactivation/survival</option>' +
            '        <option value="T" ' + ('T' === _value.metadata.subject ? 'selected' : '') + '>T</option>' +
            '        <option value="PH" ' + ('PH' === _value.metadata.subject ? 'selected' : '') + '>pH</option>' +
            '        <option value="AW" ' + ('AW' === _value.metadata.subject ? 'selected' : '') + '>aw</option>' +
            '        <option value="T_PH" ' + ('T_PH' === _value.metadata.subject ? 'selected' : '') + '>T/pH</option>' +
            '        <option value="T_AW" ' + ('T_AW' === _value.metadata.subject ? 'selected' : '') + '>T/aw</option>' +
            '        <option value="PH_AW" ' + ('PH_AW' === _value.metadata.subject ? 'selected' : '') + '>pH/aw</option>' +
            '        <option value="T_PH_AW" ' + ('T_PH_AW' === _value.metadata.subject ? 'selected' : '') + '>T/pH/aw</option>' +
            '      </select>' +
            '    </div>' +
            '  </div>' +

            // Food process form
            '  <div class="form-group form-group-sm">' +
            '    <label for="foodProcess" class="col-sm-3 control-label">Food process:</label>' +
            '    <div class="col-sm-9">' +
            '      <input type="text" class="form-control no-border" id="foodProcessInput" value="' + nullToEmpty(_value.metadata.foodProcess) + '">' +
            '    </div>' +
            '  </div>' +

            // Has data form
            '  <div class="form-group form-group-sm">' +
            '    <label for="hasData" class="col-sm-3 control-label">Has data?:</label>' +
            '    <div class="col-sm-9">' +
            '      <input id="hasDataInput" type="checkbox"' + (_value.metadata.hasData ? " checked" : "") + '>' +
            '    </div>' +
            '  </div>' +

            '</form>';

        document.createElement("body");
        $("body").html('<div class="container">' + form + varTable + '</div');

        // Create date pickers
        $("#createdDateInput").datepicker( { dateFormat: "mm.dd.yy"} );
        $("#modifiedDateInput").datepicker( { dateFormat: "mm.dd.yy"} );

        saveData();
    }

    function loadData () {
        $('#modelNameInput').val(nullToEmpty(_value.metadata.modelName));
        $('#modelIdInput').val(nullToEmpty(_value.metadata.modelId));
        $('#modelLinkInput').val(nullToEmpty(_value.metadata.modelLink));

        $('#organismInput').val(nullToEmpty(_value.metadata.organism));
        $('#organismDetailsInput').val(nullToEmpty(_value.metadata.organismDetails));

        $('#matrixInput').val(nullToEmpty(_value.metadata.matrix));
        $('#matrixDetailsInput').val(nullToEmpty(_value.metadata.matrixDetails));

        $('#creatorInput').val(nullToEmpty(_value.metadata.creator));
        $('#familyNameInput').val(nullToEmpty(_value.metadata.familyName));
        $('#contactInput').val(nullToEmpty(_value.metadata.contact));
        if (_value.metadata.software) {
            $('#softwareInput option[value="' + _value.metadata.software + '"]').prop('selected', true);
        }

        $('#referenceDescriptionInput').val(nullToEmpty(_value.metadata.referenceDescription));
        $('#referenceDescriptionLinkInput').val(nullToEmpty(_value.metadata.referenceDescriptionLink));

        $('#createdDateInput').val(nullToEmpty(_value.metadata.createdDate));
        $('#modifiedDateInput').val(nullToEmpty(_value.metadata.modifiedDate));

        $('#rightsInput').val(nullToEmpty(_value.metadata.rights));
        $('#notesInput').val(nullToEmpty(_value.metadata.notes));
        $('#curatedInput').prop('checked', _value.metadata.curated);

        $('#typeInput').val(nullToEmpty(_value.metadata.type));
        $('#subjectInput').val(nullToEmpty(_value.metadata.subject));
        $('#foodProcess').val(nullToEmpty(_value.metadata.foodProcess));
        $('#hasData').prop('checked', _value.metadata.hasData);

        // TODO: variables
    }

    /**
     * Saves data on modification.
     * 
     * - Text inputs use the oninput event.
     * - Selects and checkboxes use the onchange event.
     */
    function saveData () {
        $("#modelNameInput").on('input', function() { _value.metadata.modelName = $(this).val(); });
        $("#modelIdInput").on('input', function() { _value.metadata.modelId = $(this).val(); });
        $("#modelLinkInput").on('input', function() { _value.metadata.modelLink = $(this).val(); });

        $("#organismInput").on('input', function() { _value.metadata.organism = $(this).val(); });
        $("#organismDetailsInput").change(function() { _value.metadata.organismDetails = $(this).val(); });

        $("#matrixInput").on('input', function() { _value.metadata.matrix = $(this).val(); });
        $("#matrixDetailsInput").on('input', function() { _value.metadata.matrixDetails = $(this).val(); });

        $("#creatorInput").on('input', function() { _value.metadata.creator = $(this).val(); });
        $("#familyNameInput").on('input', function() { _value.metadata.familyName = $(this).val(); });
        $("#contactInput").on('input', function() { _value.metadata.contact = $(this).val(); });
        $("#softwareInput").change(function() { _value.metadata.software = $(this).val(); });

        $("#referenceDescriptionInput").on('input', function() { _value.metadata.referenceDescription = $(this).val(); });
        $("#referenceDescriptionLinkInput").on('input', function() { _value.metadata.referenceDescriptionLink = $(this).val(); });

        $("#createdDateInput").on('input', function() { _value.metadata.createdDate = $(this).val(); });
        $("#modifiedDateInput").on('input', function() { _value.metadata.modifiedDate = $(this).val(); });

        $("#rightsInput").on('input', function() { _value.metadata.rights = $(this).val(); });
        $("#notesInput").on('input', function() { _value.metadata.notes = $(this).val(); });
        $("#curatedInput").change(function() { _value.metadata.curated  = $(this).is(':checked'); });

        $("#typeInput").change(function() { _value.metadata.type = $(this).val(); });
        $('#subjectInput').change(function() { _value.metadata.subject = $(this).val(); });
        $("#foodProcessInput").on('input', function() { _value.metadata.foodProcess = $(this).val(); });
        $("#hasDataInput").change(function() { _value.metadata.hasData = $(this).is(':checked'); });

        // Save changes in variables table
        $("body div table tr:not(:first)").each(function(i, row) {
            // Value change
            $("td:eq(3) input", this).change(function() {
                _value.metadata.independentVariables[i].value = $(this).val();
            });
            // Minimum value change
            $("td:eq(4) input", this).change(function() {
                _value.metadata.independentVariables[i].min = $(this).val();
            });
            // Maximum value change
            $("td:eq(5) input", this).change(function() {
                _value.metadata.independentVariables[i].max = $(this).val();
            });
        });
    }

    function nullToEmpty(stringVar) {
        return stringVar === null ? "" : stringVar;
    }
}();
