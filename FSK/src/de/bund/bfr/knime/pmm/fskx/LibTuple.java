/***************************************************************************************************
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
 **************************************************************************************************/
package de.bund.bfr.knime.pmm.fskx;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.date.DateAndTimeCell;
import org.knime.core.data.def.StringCell;

import com.google.common.base.Joiner;

public class LibTuple implements DataRow {

  private DataCell[] cells;
  private final RowKey rowKey;

  public static enum Columns {
    PACKAGE, TYPE, TITLE, VERSION, DATE, DESCRIPTION, DEPS, PATH
  };

  /**
   * Constructs a {@link LibTuple} instance from meta data and path of the library.
   */
  public LibTuple(RPackageMetadata libMetaData, String path) {
    cells = new DataCell[8];

    cells[Columns.PACKAGE.ordinal()] = new StringCell(libMetaData.m_package);

    if (libMetaData.m_type == null) {
      cells[Columns.TYPE.ordinal()] = new StringCell("");
    } else {
      cells[Columns.TYPE.ordinal()] = new StringCell(libMetaData.m_type);
    }

    if (libMetaData.m_title == null) {
      cells[Columns.TITLE.ordinal()] = new StringCell("");
    } else {
      cells[Columns.TITLE.ordinal()] = new StringCell(libMetaData.m_title);
    }

    if (libMetaData.m_version == null) {
      cells[Columns.VERSION.ordinal()] = new StringCell("");
    } else {
      cells[Columns.VERSION.ordinal()] = new StringCell(libMetaData.m_version.toString());
    }

    if (libMetaData.m_date == null) {
      cells[Columns.DATE.ordinal()] = new DateAndTimeCell(0, 0, 0);
    } else {
      cells[Columns.DATE.ordinal()] = new DateAndTimeCell(libMetaData.m_date.get(Calendar.YEAR),
          libMetaData.m_date.get(Calendar.MONTH), libMetaData.m_date.get(Calendar.DAY_OF_MONTH));
    }

    if (libMetaData.m_description == null) {
      cells[Columns.DESCRIPTION.ordinal()] = new StringCell("");
    } else {
      cells[Columns.DESCRIPTION.ordinal()] = new StringCell(libMetaData.m_description);
    }

    cells[Columns.DEPS.ordinal()] = new StringCell(Joiner.on("|").join(libMetaData.m_dependencies));

    cells[Columns.PATH.ordinal()] = new StringCell(path);
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

  public static DataTableSpec createTableSpec() {
    String[] colNames = new String[8];
    DataType[] colTypes = new DataType[8];

    colNames[Columns.PACKAGE.ordinal()] = RPackageMetadata.PACKAGE;
    colTypes[Columns.PACKAGE.ordinal()] = StringCell.TYPE;

    colNames[Columns.TYPE.ordinal()] = RPackageMetadata.TYPE;
    colTypes[Columns.TYPE.ordinal()] = StringCell.TYPE;

    colNames[Columns.TITLE.ordinal()] = RPackageMetadata.TITLE;
    colTypes[Columns.TITLE.ordinal()] = StringCell.TYPE;

    colNames[Columns.VERSION.ordinal()] = RPackageMetadata.VERSION;
    colTypes[Columns.VERSION.ordinal()] = StringCell.TYPE;

    colNames[Columns.DATE.ordinal()] = RPackageMetadata.DATE;
    colTypes[Columns.DATE.ordinal()] = DateAndTimeCell.TYPE;

    colNames[Columns.DESCRIPTION.ordinal()] = RPackageMetadata.DESCRIPTION;
    colTypes[Columns.DESCRIPTION.ordinal()] = StringCell.TYPE;

    colNames[Columns.DEPS.ordinal()] = RPackageMetadata.DEPENDENCIES;
    colTypes[Columns.DEPS.ordinal()] = StringCell.TYPE;

    colNames[Columns.PATH.ordinal()] = "path";
    colTypes[Columns.PATH.ordinal()] = StringCell.TYPE;

    DataColumnSpec[] colSpecs = DataTableSpec.createColumnSpecs(colNames, colTypes);

    return new DataTableSpec(colSpecs);
  }
}
