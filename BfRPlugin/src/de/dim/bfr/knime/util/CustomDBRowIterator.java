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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.DoubleValue;
import org.knime.core.data.IntValue;
import org.knime.core.data.RowIterator;
import org.knime.core.data.RowKey;
import org.knime.core.data.date.DateAndTimeCell;
import org.knime.core.data.date.DateAndTimeValue;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.util.FileUtil;

public class CustomDBRowIterator extends RowIterator 
{
    private final ResultSet m_result;
    private DataTableSpec m_spec;

    private boolean m_hasExceptionReported = false;

    private int m_rowCounter = 0;

    /** FIXME: Some database (such as sqlite do NOT support) methods like
     * #getAsciiStream nor #getBinaryStream and will fail with an
     * SQLException. To prevent this exception for each ResultSet's value,
     * this flag for each column indicated that this exception has been
     * thrown and we directly can access the value via #getString.
     */
    private final boolean[] m_streamException;

    /**
     * Creates new iterator.
     * @param result result set to iterate
     */
    public CustomDBRowIterator(DataTableSpec spec, final ResultSet result) {
        m_result = result;
        m_spec = spec;
        m_streamException = new boolean[m_spec.getNumColumns()];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        boolean ret = false;
        try {
            ret = m_result.next();
        } catch (SQLException sql) {
            ret = false;
        }
        if (!ret) {
            try {
                m_result.close();
            } catch (SQLException e) {
                //LOGGER.error("SQL Exception while closing result set: ", e);
            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRow next() {
        DataCell[] cells = new DataCell[m_spec.getNumColumns()];
        for (int i = 0; i < cells.length; i++) {
            DataType type = m_spec.getColumnSpec(i).getType();
            int dbType = Types.NULL;
            final DataCell cell;
            try {
                dbType = m_result.getMetaData().getColumnType(i + 1);
                if (type.isCompatible(IntValue.class)) {
                    switch (dbType) {
                        // all types that can be interpreted as integer
                        case Types.TINYINT:
                            cell = readByte(i);
                            break;
                        case Types.SMALLINT:
                            cell = readShort(i);
                            break;
                        case Types.INTEGER:
                            cell = readInt(i);
                            break;
                        case Types.BIT:
                        case Types.BOOLEAN:
                            cell = readBoolean(i);
                            break;
                        default: cell = readInt(i);
                    }
                } else if (type.isCompatible(DoubleValue.class)) {
                    switch (dbType) {
                        // all types that can be interpreted as double
                        case Types.REAL:
                            cell = readFloat(i);
                            break;
                        case Types.FLOAT:
                        case Types.DOUBLE:
                            cell = readDouble(i);
                            break;
                        case Types.DECIMAL:
                        case Types.NUMERIC:
                            cell = readBigDecimal(i);
                            break;
                        case Types.BIGINT:
                            cell = readLong(i);
                            break;
                        default: cell = readDouble(i);
                    }
                } else if (type.isCompatible(DateAndTimeValue.class)) {
                    switch (dbType) {
                        case Types.DATE:
                            cell = readDate(i); break;
                        case Types.TIME:
                            cell = readTime(i); break;
                        case Types.TIMESTAMP:
                            cell = readTimestamp(i); break;
                        default: cell = readString(i);
                    }
                } else {
                    switch (dbType) {
                        case Types.CLOB:
                            cell = readClob(i); break;
                        case Types.BLOB:
                            cell = readBlob(i); break;
                        case Types.ARRAY:
                            cell = readArray(i); break;
                        case Types.CHAR:
                        case Types.VARCHAR:
                            cell = readString(i); break;
                        case Types.LONGVARCHAR:
                            cell = readAsciiStream(i); break;
                        case Types.BINARY:
                        case Types.VARBINARY:
                            cell = readBytes(i); break;
                        case Types.LONGVARBINARY:
                            cell = readBinaryStream(i); break;
                        case Types.REF:
                            cell = readRef(i); break;
                        case Types.NCHAR:
                        case Types.NVARCHAR:
                        case Types.LONGNVARCHAR:
                            cell = readNString(i); break;
                        case Types.NCLOB:
                            cell = readNClob(i); break;
                        case Types.DATALINK:
                            cell = readURL(i); break;
                        case Types.STRUCT:
                        case Types.JAVA_OBJECT:
                            cell = readObject(i); break;
                        default:
                            cell = readObject(i); break;

                    }
                }
                // finally set the new cell into the array of cells
                cells[i] = cell;
            } catch (SQLException sqle) {
                handlerException(
                        "SQL Exception reading Object of type \""
                        + dbType + "\": ", sqle);
            } catch (IOException ioe) {
                handlerException(
                        "I/O Exception reading Object of type \""
                        + dbType + "\": ", ioe);
            }
        }
        int rowId = m_rowCounter;
        try {
            rowId = m_result.getRow();
        } catch (SQLException sqle) {
             // ignored
        }
        m_rowCounter++;
        return new DefaultRow(RowKey.createRowKey(rowId), cells);
    }

    private DataCell readClob(final int i)
            throws IOException, SQLException {
        Clob clob = m_result.getClob(i + 1);
        if (wasNull() || clob == null) {
            return DataType.getMissingCell();
        } else {
            Reader reader = clob.getCharacterStream();
            StringWriter writer = new StringWriter();
            FileUtil.copy(reader, writer);
            reader.close();
            writer.close();
            return new StringCell(writer.toString());
        }
    }

    private DataCell readNClob(final int i)
            throws IOException, SQLException {
        NClob nclob = m_result.getNClob(i + 1);
        if (wasNull() || nclob == null) {
            return DataType.getMissingCell();
        } else {
            Reader reader = nclob.getCharacterStream();
            StringWriter writer = new StringWriter();
            FileUtil.copy(reader, writer);
            reader.close();
            writer.close();
            return new StringCell(writer.toString());
        }
    }

    private DataCell readBlob(final int i)
            throws IOException, SQLException {
       Blob blob = m_result.getBlob(i + 1);
       if (wasNull() || blob == null) {
           return DataType.getMissingCell();
       } else {
           InputStreamReader reader =
               // TODO: using default encoding
               new InputStreamReader(blob.getBinaryStream());
           StringWriter writer = new StringWriter();
           FileUtil.copy(reader, writer);
           reader.close();
           writer.close();
           return new StringCell(writer.toString());
       }
    }

    private DataCell readAsciiStream(final int i)
            throws IOException, SQLException {
        if (m_streamException[i]) {
            return readString(i);
        }
        try {
            InputStream is = m_result.getAsciiStream(i + 1);
            if (wasNull() || is == null) {
                return DataType.getMissingCell();
            } else {
                InputStreamReader reader =
                // TODO: using default encoding
                        new InputStreamReader(is);
                StringWriter writer = new StringWriter();
                FileUtil.copy(reader, writer);
                reader.close();
                writer.close();
                return new StringCell(writer.toString());
            }
        } catch (SQLException sql) {
            m_streamException[i] = true;
            handlerException("Can't read from ASCII stream, "
                    + "trying to read string... ", sql);
            return readString(i);
        }
    }

    private DataCell readByte(final int i) throws SQLException {
        byte b = m_result.getByte(i + 1);
        if (wasNull()) {
            return DataType.getMissingCell();
        } else {
            return new IntCell(b);
        }
    }

    private DataCell readShort(final int i) throws SQLException {
        short s = m_result.getShort(i + 1);
        if (wasNull()) {
            return DataType.getMissingCell();
        } else {
            return new IntCell(s);
        }
    }

    private DataCell readInt(final int i) throws SQLException {
        int integer = m_result.getInt(i + 1);
        if (wasNull()) {
            return DataType.getMissingCell();
        } else {
            return new IntCell(integer);
        }
    }

    private DataCell readBoolean(final int i) throws SQLException {
        boolean b = m_result.getBoolean(i + 1);
        if (wasNull()) {
            return DataType.getMissingCell();
        } else {
            return new IntCell(b ? 1 : 0);
        }
    }

    private DataCell readDouble(final int i) throws SQLException {
        double d = m_result.getDouble(i + 1);
        if (wasNull()) {
            return DataType.getMissingCell();
        } else {
            return new DoubleCell(d);
        }
    }

    private DataCell readFloat(final int i) throws SQLException {
        float f = m_result.getFloat(i + 1);
        if (wasNull()) {
            return DataType.getMissingCell();
        } else {
            return new DoubleCell(f);
        }
    }

    private DataCell readLong(final int i) throws SQLException {
        long l = m_result.getLong(i + 1);
        if (wasNull()) {
            return DataType.getMissingCell();
        } else {
            return new DoubleCell(l);
        }
    }

    private DataCell readString(final int i) throws SQLException {
        String s = m_result.getString(i + 1);
        if (wasNull() || s == null) {
            return DataType.getMissingCell();
        } else {
            return new StringCell(s);
        }
    }

    private DataCell readBytes(final int i) throws SQLException {
        byte[] bytes = m_result.getBytes(i + 1);
        if (wasNull() || bytes == null) {
            return DataType.getMissingCell();
        } else {
            return new StringCell(new String(bytes));
        }
    }

    private DataCell readBigDecimal(final int i) throws SQLException {
        BigDecimal bc = m_result.getBigDecimal(i + 1);
        if (wasNull() || bc == null) {
            return DataType.getMissingCell();
        } else {
            return new DoubleCell(bc.doubleValue());
        }

    }

    private DataCell readBinaryStream(final int i)
            throws IOException, SQLException {
        if (m_streamException[i]) {
            return readString(i);
        }
        try {
            InputStream is = m_result.getBinaryStream(i + 1);
            if (wasNull() || is == null) {
                return DataType.getMissingCell();
            } else {
                InputStreamReader reader =
                // TODO: using default encoding
                        new InputStreamReader(is);
                StringWriter writer = new StringWriter();
                FileUtil.copy(reader, writer);
                reader.close();
                writer.close();
                return new StringCell(writer.toString());
            }
        } catch (SQLException sql) {
            m_streamException[i] = true;
            handlerException("Can't read from binary stream, "
                    + "trying to read string... ", sql);
            return readString(i);
        }
    }

    private DataCell readNString(final int i) throws SQLException {
        String str = m_result.getNString(i + 1);
        if (wasNull() || str == null) {
            return DataType.getMissingCell();
        } else {
            return new StringCell(str);
        }
    }

    private DataCell readDate(final int i) throws SQLException {
        Date date = m_result.getDate(i + 1);
        if (wasNull() || date == null) {
            return DataType.getMissingCell();
        } else {
            return new DateAndTimeCell(date.getTime(), true, false, false);
        }
    }

    private DataCell readTime(final int i) throws SQLException {
        Time time = m_result.getTime(i + 1);
        if (wasNull() || time == null) {
            return DataType.getMissingCell();
        } else {
            return new DateAndTimeCell(time.getTime(), false, true, true);
        }
    }

    private DataCell readTimestamp(final int i) throws SQLException {
        Timestamp timestamp = m_result.getTimestamp(i + 1);
        if (wasNull() || timestamp == null) {
            return DataType.getMissingCell();
        } else {
            return new DateAndTimeCell(
                    timestamp.getTime(), true, true, true);
        }
    }

    private DataCell readArray(final int i) throws SQLException {
        Array array = m_result.getArray(i + 1);
        if (wasNull() || array == null) {
            return DataType.getMissingCell();
        } else {
            return new StringCell(array.getArray().toString());
        }
    }

    private DataCell readRef(final int i) throws SQLException {
        Ref ref = m_result.getRef(i + 1);
        if (wasNull() || ref == null) {
            return DataType.getMissingCell();
        } else {
            return new StringCell(ref.getObject().toString());
        }
    }

    private DataCell readURL(final int i) throws SQLException {
        URL url = m_result.getURL(i + 1);
        if (url == null || wasNull()) {
            return DataType.getMissingCell();
        } else {
            return new StringCell(url.toString());
        }
    }

    private DataCell readObject(final int i) throws SQLException {
        Object o = m_result.getObject(i + 1);
        if (o == null || wasNull()) {
            return DataType.getMissingCell();
        } else {
            return new StringCell(o.toString());
        }
    }

    private boolean wasNull() {
        try {
            return m_result.wasNull();
        } catch (SQLException sqle) {
            handlerException("SQL Exception: ", sqle);
            return true;
        }
    }

    private void handlerException(final String msg, final Exception e) {
        if (m_hasExceptionReported) {
            //LOGGER.debug(msg + e.getMessage(), e);
        } else {
            m_hasExceptionReported = true;
            //LOGGER.error(msg + e.getMessage()
              //      + " - all further errors are suppressed "
                //    + "and reported on debug level only", e);
        }
    }
}
