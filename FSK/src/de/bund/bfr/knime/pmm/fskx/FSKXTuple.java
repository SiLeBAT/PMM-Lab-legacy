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
    /** original model script */
    ORIG_MODEL,
    /** simplified model script */
    SIMP_MODEL,
    /** original parameters script */
    ORIG_PARAM,
    /** simplified parameters script */
    SIMP_PARAM,
    /** original visualization script */
    ORIG_VIZ,
    /** simplified visualization script */
    SIMP_VIZ,
    /** libraries */
    LIBS,
    /** sources */
    SOURCES
  };

  private DataCell[] cell;
  private final RowKey rowKey;

  /**
   * Builds a {@link FSKXTuple} from a EnumMap with keys {@link FSKXTuple.KEYS}.
   * 
   * @param values Map with values. The mandatory values are:
   *        <ul>
   *        <li>Original model ({@link KEYS#ORIG_MODEL})
   *        <li>Simplified model ({@link KEYS#SIMP_MODEL})
   *        <li>Libraries ({@link KEYS#LIBS})
   *        <li>Sources ({@link KEYS#SOURCES})
   *        </ul>
   * @throws MissingValueError if a mandatory values is missing.
   */
  public FSKXTuple(EnumMap<KEYS, String> values) throws MissingValueError {
    cell = new DataCell[KEYS.values().length];
    
    // checks optional columns
    if (!values.containsKey(KEYS.ORIG_MODEL)) throw new MissingValueError("Missing original model");
    if (!values.containsKey(KEYS.SIMP_MODEL)) throw new MissingValueError("Missing simplified model");
    if (!values.containsKey(KEYS.LIBS)) throw new MissingValueError("Missing libraries");
    if (!values.containsKey(KEYS.SOURCES)) throw new MissingValueError("Missing sources");

    // assigns mandatory columns
    cell[KEYS.ORIG_MODEL.ordinal()] = new StringCell(values.get(KEYS.ORIG_MODEL));
    cell[KEYS.SIMP_MODEL.ordinal()] = new StringCell(values.get(KEYS.SIMP_MODEL));
    cell[KEYS.LIBS.ordinal()] = new StringCell(values.get(KEYS.LIBS));
    cell[KEYS.SOURCES.ordinal()] = new StringCell(values.get(KEYS.SOURCES));

    // assigns optional columns
    cell[KEYS.ORIG_PARAM.ordinal()] =
        new StringCell(values.containsKey(KEYS.ORIG_PARAM) ? values.get(KEYS.ORIG_PARAM) : "");
    cell[KEYS.SIMP_PARAM.ordinal()] =
        new StringCell(values.containsKey(KEYS.SIMP_PARAM) ? values.get(KEYS.SIMP_PARAM) : "");
    cell[KEYS.ORIG_VIZ.ordinal()] =
        new StringCell(values.containsKey(KEYS.ORIG_VIZ) ? values.get(KEYS.ORIG_VIZ) : "");
    cell[KEYS.SIMP_VIZ.ordinal()] =
        new StringCell(values.containsKey(KEYS.SIMP_VIZ) ? values.get(KEYS.SIMP_VIZ) : "");

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
