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

import java.util.Iterator;
import java.util.Random;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.StringCell;

public class LibTuple implements DataRow {
  
  static final String COL_NAME = "location";
  
  private DataCell[] cells;
  private final RowKey rowKey;
  
  /**
   * Builds a {@link LibTuple} from a name and path.
   * 
   * @param name Library name.
   * @param value File path of the library.
   */
  public LibTuple(String name, String path) {
    cells = new DataCell[] { new StringCell(name), new StringCell(path) };
    rowKey = new RowKey(String.valueOf(new Random().nextInt()));
  }
  
  @Override
  public int getNumCells() {
    return cells.length;
  }
  
  @Override
  public RowKey getKey() {
    return rowKey;
  }
  
  @Override
  public DataCell getCell(final int index) {
    return cells[index];
  }
  
  @Override
  public Iterator<DataCell> iterator() {
    return new LibTupleIterator(cells);
  }
  
  public class LibTupleIterator implements Iterator<DataCell> {
    private int i;
    private DataCell[] cell;
    
    public LibTupleIterator(DataCell[] cell) {
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
    
    @Override
    public void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
    }
  }

}
