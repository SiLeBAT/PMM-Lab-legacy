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
package fskx;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.StringCell;

public class FSKXTuple implements DataRow {

  private DataCell[] cell;
  private final RowKey key;

  public FSKXTuple(final String modelScript, final String paramScript, final String vizScript,
      final Set<String> libraries, final Set<String> sources) {
    cell = new DataCell[5];
    cell[0] = new StringCell(modelScript);
    cell[1] = new StringCell(paramScript);
    cell[2] = new StringCell(vizScript);
    cell[3] = new StringCell(String.join("|", libraries));
    cell[4] = new StringCell(String.join("|", sources));
    key = new RowKey(String.valueOf(new Random().nextInt()));
  }

  @Override
  public int getNumCells() {
    return cell.length;
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
