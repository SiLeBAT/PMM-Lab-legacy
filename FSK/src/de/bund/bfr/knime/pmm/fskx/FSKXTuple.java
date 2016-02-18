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
package de.bund.bfr.knime.pmm.fskx;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Random;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.StringCell;

public class FSKXTuple implements DataRow {

  public enum KEYS {
    ORIG_MODEL, SIMP_MODEL, ORIG_PARAM, SIMP_PARAM, ORIG_VIZ, SIMP_VIZ, LIBS, SOURCES
  };

  private DataCell[] cell;
  private final RowKey rowKey;

  public FSKXTuple(EnumMap<KEYS, String> values) {
    cell = new DataCell[KEYS.values().length];
    for (KEYS key : KEYS.values()) {
      cell[key.ordinal()] = new StringCell(values.get(key));
    }
    rowKey = new RowKey(String.valueOf(new Random().nextInt()));
  }

  @Override
  public int getNumCells() {
    return cell.length;
  }

  @Override
  public RowKey getKey() {
    return rowKey;
  }

  @Override
  public DataCell getCell(final int index) {
    return cell[index];
  }

  @Override
  public Iterator<DataCell> iterator() {
    return new FSKXTupleIterator(cell);
  }

  class FSKXTupleIterator implements Iterator<DataCell> {

    private int i;
    private DataCell[] cell;

    public FSKXTupleIterator(final DataCell[] cell) {
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
