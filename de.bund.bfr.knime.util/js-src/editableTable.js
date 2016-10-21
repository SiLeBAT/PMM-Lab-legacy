editable_table = function() {

	var _representation;
	var _value;
    var _knimeTable;

	// Object with GUI functions
	var table = {};

	table.init = function(representation, value) {

        // TODO: DEBUG
        alert(JSON.stringify(representation));
        alert(JSON.stringify(value));

		if (!representation.table) {
			body.append("Error: No data available");
			return;
		}
		_representation = representation;
		_value = value;

        _knimeTable = new kt();
        _knimeTable.setDataTable(_representation.table);

		drawTable();
	}

	table.getComponentValue = function() {

        // $('.table tbody tr').each(function (i, tr) {
        //     $('td', this).each(function (j, td) {
        //         alert(JSON.stringify(this));
        //         var cellVal = this.has('<input>').length ? $('<input>', td).val() : td.text();
        //         _knimeTable.dataTable.rows[i][j];
        //     });
        // });

        // TODO: Save _knimeTable.dataTable to view value somehow

		return _value;
	}

	drawTable = function() {

		var body = $('body');
		$('body').html('<div class="container"></div>');
		$('.container').append('<table class="table table-bordered"><thead></thead><tbody></tbody></table>');

		var thead = $('.table thead');
		for (var i = 0; i < _knimeTable.getColumnNames().length; i++) {
			thead.append('<th>' + _knimeTable.getColumnNames()[i] + '</th>');
		}

		var tbody = $(".table tbody");
        var rows = _knimeTable.getRows();
        for (var i = 0; i < rows.length; i++) {
			tbody.append("<tr></tr>");
            var tr = $('tbody tr:eq(' + i + ')');

            var dataFields = rows[i].data;

            for (var j = 0; j < dataFields.length; j++) {
                if (_knimeTable.getColumnTypes()[j] == 'number') {
                    tr.append('<td><input type="number" value="' + dataFields[j] + '"></input></td>');
                } else {
                    tr.append('<td>' + dataFields[j] + '</td>');
                }
            }
		}
	}

	return table;
}();
