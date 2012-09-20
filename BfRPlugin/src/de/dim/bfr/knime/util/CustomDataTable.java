/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
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
 ******************************************************************************/
package de.dim.bfr.knime.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.node.CanceledExecutionException;

public class CustomDataTable implements DataTable
{
	private DataRow[] rows;
	private DataTableSpec spec;
	
	public CustomDataTable(DataTableSpec spec, DataRow[] rows) {
		this.rows = rows;
		this.spec = spec;
	}

	@Override
	public DataTableSpec getDataTableSpec() {
		return spec;
	}

	@Override
	public RowIterator iterator() {
		return new CustomRowIterator(rows);
	}	
	
	public DataCell getDataCell(int row, int col){
		return rows[row].getCell(col);
	}
	
    public static DataTable getSortedDataTable(final DataTable dataTable, final Comparator<DataRow> comparator) throws CanceledExecutionException 
    {
        ArrayList<DataRow> rowContainer = new ArrayList<DataRow>();

        for (final DataRow r : dataTable) {
        	rowContainer.add(r);
        }
        
        final DataRow[] rowArray = (DataRow[]) rowContainer.toArray(new DataRow[rowContainer.size()]);
        
        Arrays.sort(rowArray, comparator);

        DataTable dt = new CustomDataTable(dataTable.getDataTableSpec(), rowArray);

        return dt;        
    }
}
