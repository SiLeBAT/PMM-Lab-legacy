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

import org.knime.core.data.DataRow;
import org.knime.core.data.RowIterator;

public class CustomRowIterator extends RowIterator
{
	private int index;
	private DataRow[] dataRows;

	public CustomRowIterator(DataRow[] rows) {
		this.dataRows = rows;
		this.index = -1;
	}
	
	@Override
	public boolean hasNext() {
		return (index < dataRows.length-1);
	}

	@Override
	public DataRow next() {
		return dataRows[++index];
	}
}
