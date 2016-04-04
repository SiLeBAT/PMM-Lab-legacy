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
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.StringCell;

public class FSKXTuple implements DataRow {

  public enum KEYS {
    MODEL_SCRIPT, PARAM_SCRIPT, VIZ_SCRIPT,
    /** libraries */
    LIBS,
    /** sources */
    SOURCES
  }

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
    this.cell = new DataCell[KEYS.values().length];

    // // checks optional columns
    // if (!values.containsKey(KEYS.ORIG_MODEL)) throw new MissingValueError("Missing original
    // model");
    // if (!values.containsKey(KEYS.SIMP_MODEL)) throw new MissingValueError("Missing simplified
    // model");
    // if (!values.containsKey(KEYS.LIBS)) throw new MissingValueError("Missing libraries");
    // if (!values.containsKey(KEYS.SOURCES)) throw new MissingValueError("Missing sources");

    // assigns mandatory columns
    this.cell[KEYS.MODEL_SCRIPT.ordinal()] = new StringCell(values.get(KEYS.MODEL_SCRIPT));
    this.cell[KEYS.LIBS.ordinal()] = new StringCell(values.get(KEYS.LIBS));
    this.cell[KEYS.SOURCES.ordinal()] = new StringCell(values.get(KEYS.SOURCES));

    // assigns optional columns
    this.cell[KEYS.PARAM_SCRIPT.ordinal()] =
        new StringCell(values.containsKey(KEYS.PARAM_SCRIPT) ? values.get(KEYS.PARAM_SCRIPT) : "");
    this.cell[KEYS.VIZ_SCRIPT.ordinal()] =
        new StringCell(values.containsKey(KEYS.VIZ_SCRIPT) ? values.get(KEYS.VIZ_SCRIPT) : "");

    this.rowKey = new RowKey(String.valueOf(new Random().nextInt()));
  }

  @Override
  public int getNumCells() {
    return this.cell.length;
  }

  @Override
  public RowKey getKey() {
    return this.rowKey;
  }

  @Override
  public DataCell getCell(final int index) {
    return this.cell[index];
  }

  @Override
  public Iterator<DataCell> iterator() {
    return new FSKXTupleIterator(this.cell);
  }

  class FSKXTupleIterator implements Iterator<DataCell> {

    private int i;
    private DataCell[] cell;

    public FSKXTupleIterator(final DataCell[] cell) {
      this.i = 0;
      this.cell = cell;
    }

    @Override
    public boolean hasNext() {
      return this.i < this.cell.length;
    }

    @Override
    public DataCell next() {
      return this.cell[this.i++];
    }
  }

  public static DataTableSpec createTableSpec() {
    String[] colNames = new String[5];
    DataType[] colTypes = new DataType[5];

    colNames[KEYS.MODEL_SCRIPT.ordinal()] = "model";
    colTypes[KEYS.MODEL_SCRIPT.ordinal()] = StringCell.TYPE;

    colNames[KEYS.PARAM_SCRIPT.ordinal()] = "params";
    colTypes[KEYS.PARAM_SCRIPT.ordinal()] = StringCell.TYPE;

    colNames[KEYS.VIZ_SCRIPT.ordinal()] = "visualization";
    colTypes[KEYS.VIZ_SCRIPT.ordinal()] = StringCell.TYPE;

    colNames[KEYS.LIBS.ordinal()] = "RLibraries";
    colTypes[KEYS.LIBS.ordinal()] = StringCell.TYPE;

    colNames[KEYS.SOURCES.ordinal()] = "RSources";
    colTypes[KEYS.SOURCES.ordinal()] = StringCell.TYPE;

    return new DataTableSpec(DataTableSpec.createColumnSpecs(colNames, colTypes));
  }
}
