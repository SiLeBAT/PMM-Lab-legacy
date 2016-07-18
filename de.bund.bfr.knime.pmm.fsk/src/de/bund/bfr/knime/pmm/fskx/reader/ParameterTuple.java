/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 ******************************************************************************/
package de.bund.bfr.knime.pmm.fskx.reader;

import java.util.Iterator;
import java.util.Random;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.StringCell;

public class ParameterTuple implements DataRow {

	private DataCell[] cell;
	private final RowKey key;

	public ParameterTuple(final String name, final String value) {
		cell = new DataCell[2];
		cell[0] = new StringCell(name);
		cell[1] = new StringCell(value);
		key = new RowKey(String.valueOf(new Random().nextInt()));
	}

	@Override
	public int getNumCells() {
		return 2;
	}

	@Override
	public RowKey getKey() {
		return key;
	}

	@Override
	public DataCell getCell(final int index) {
		return cell[index];
	}

	@Override
	public Iterator<DataCell> iterator() {
		return new ParameterTupleIterator(cell);
	}

	class ParameterTupleIterator implements Iterator<DataCell> {

		private int i;
		private DataCell[] cell;

		public ParameterTupleIterator(final DataCell[] cell) {
			i = 0;
			this.cell = cell;
		}

		@Override
		public boolean hasNext() {
			return i < cell.length;
		}

		@Override
		public DataCell next() {
			return cell[i++];
		}
	}
}