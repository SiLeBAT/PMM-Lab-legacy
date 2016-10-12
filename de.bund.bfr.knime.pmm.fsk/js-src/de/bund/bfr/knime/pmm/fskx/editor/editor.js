metadata_editor = function () {

    var editor = {
	   version: "0.0.1"
    };
    editor.name = "FSK Metadata Editor";

    editor.init = function (representation, value)
    {
        alert("HOLA INIT");
        create_body ();
    };

    editor.getComponentValue = function ()
    {
    };

    return editor;

    // --- utility functions ---
    function create_body ()
    {
        var form =
            '<div class="container">' +
            '  <form class="form-horizontal">' +
            '    <div class="form-group">' +
            '      <label for="exampleInputEmail1">Email address</label>' +
            '      <input type="email" class="form-control" id="exampleInputEmail1" placeholder="Email">' +
            '    </div>' +
            '    <button type="submit" class="btn btn-default">Submit</button>' +
            '  </form>' +
            '</div>';

        var form2 = 
            '<div class="container">' +

            '  <form class="form-horizontal">' +

            // Model name form
            '    <div class="form-group">' +
            '      <label for="modelName" class="col-sm-3 control-label">Model name</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="modelName" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Model id form
            '    <div class="form-group">' +
            '      <label for="modelId" class="col-sm-3 control-label">Model id</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="modelId" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Model link form
            '    <div class="form-group">' +
            '      <label for="modelLink" class="col-sm-3 control-label">Model link</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="url" class="form-control" id="modelLink" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Organism form
            '    <div class="form-group">' +
            '      <label for="organism" class="col-sm-3 control-label">Organism</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="organism" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Organism details form
            '    <div class="form-group">' +
            '      <label for="organismDetails" class="col-sm-3 control-label">Organism details</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="organismDetails" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Matrix form
            '    <div class="form-group">' +
            '      <label for="matrix" class="col-sm-3 control-label">Matrix</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="matrix" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Matrix details form
            '    <div class="form-group">' +
            '      <label for="matrixDetails" class="col-sm-3 control-label">Matrix details</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="matrixDetails" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Contact form
            '    <div class="form-group">' +
            '      <label for="contact" class="col-sm-3 control-label">Contact</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="contact" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Reference description form
            '    <div class="form-group">' +
            '      <label for="referenceDescription" class="col-sm-3 control-label">Referece description</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="referenceDescription" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Reference description link form
            '    <div class="form-group">' +
            '      <label for="referenceDescriptionLink" class="col-sm-3 control-label">Referece description link</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="url" class="form-control" id="referenceDescriptionLink" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Created date form
            '    <div class="form-group">' +
            '      <label for="createdDate" class="col-sm-3 control-label">Created date</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="date" class="form-control" id="createdDate" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Modified date form
            '    <div class="form-group">' +
            '      <label for="modifiedDate" class="col-sm-3 control-label">Modified date</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="date" class="form-control" id="modifiedDate" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Rights form
            '    <div class="form-group">' +
            '      <label for="rights" class="col-sm-3 control-label">Rights</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="rights" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Notes form
            '    <div class="form-group">' +
            '      <label for="notes" class="col-sm-3 control-label">Notes</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="notes" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Curated form
            '    <div class="checkbox">' +
            '      <label>' +
            '        Curated <input type="checkbox">' +
            '      </label>' +
            '    </div>' +

            // Model type form
            '    <div class="form-group">' +
            '      <label for="modelType" class="col-sm-3 control-label">Model type</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="modelType" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Model subject
            '    <div class="form-group">' +
            '      <label for="modelSubject" class="col-sm-3 control-label">Model subject</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="modelSubject" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Food process
            '    <div class="form-group">' +
            '      <label for="foodProcess" class="col-sm-3 control-label">Food process</label>' +
            '      <div class="col-sm-9">' +
            '        <input type="text" class="form-control" id="foodProcess" placeholder="Text input">' +
            '      </div>' +
            '    </div>' +

            // Submit button
            '    <div class="form-group">' +
            '      <div class="col-sm-offset-3 col-sm-9">' +
            '        <button type="submit" class="btn btn-default">Submit</button>' +
            '      </div>' +
            '    </div>' +

            '  </form>' +
            '</div>';
    // String dependentVariable;
    // String dependentVariableUnit;
    // double dependentVariableMin = Double.NaN;
    // double dependentVariableMax = Double.NaN;

    // String[] independentVariables;
    // String[] independentVariableUnits;
    // double[] independentVariableMins;
    // double[] independentVariableMaxs;
    // double[] independentVariableValues;

    // boolean hasData;

        document.createElement("body");
        // $("body").html(text);
        $("body").html(form2);
    }
}();
