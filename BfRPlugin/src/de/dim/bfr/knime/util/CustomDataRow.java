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

import org.knime.base.node.io.filereader.DataCellFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;

public class CustomDataRow extends ArrayList<DataCell> implements DataRow 
{
	private static DataCellFactory dataCellFactory = new DataCellFactory();
	
	private static final long serialVersionUID = 4559882227163113566L;
	
	private RowKey rowKey;
	
	public CustomDataRow(int index) {
		this.rowKey = RowKey.createRowKey(index);
	}
	
	@Override
	public int getNumCells() {
		return size();
	}

	@Override
	public RowKey getKey() {
		return rowKey;
	}

	@Override
	public DataCell getCell(int index) {
	    return get(index);
	}
	
	public boolean addCell(Object o) {
		DataType type = null; 
	
		if(o instanceof Integer)
			type = IntCell.TYPE;
		else
			type = StringCell.TYPE;

		DataCell dc = dataCellFactory.createDataCellOfType(type, o.toString());		
		return add(dc);
	}
}
