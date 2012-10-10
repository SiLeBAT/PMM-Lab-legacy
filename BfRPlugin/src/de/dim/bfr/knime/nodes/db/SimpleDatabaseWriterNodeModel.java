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
package de.dim.bfr.knime.nodes.db;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.dim.bfr.external.service.BFRNodeService;
import de.dim.knime.bfr.internal.BfRNodePluginActivator;

/**
 * Database writer model which creates a new table and adds the entire table to
 * it.
 *
 * @author Data In Motion UG
 */
class SimpleDatabaseWriterNodeModel extends NodeModel {

	private static NodeLogger logger = NodeLogger.getLogger(SimpleDatabaseWriterNodeModel.class);
    private static final String PRE_COLNAME = "SELECT C.TABLE_NAME FROM " +
					"(SELECT TABLE_NAME, COUNT(TABLE_NAME) AS COUNTER " +
					"FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME IN (";
    private static final String POST_COLNAME= ")GROUP BY TABLE_NAME)as C, " +
					"(SELECT TABLE_NAME, COUNT(TABLE_NAME) AS COUNTER FROM " +
					"INFORMATION_SCHEMA.COLUMNS GROUP BY TABLE_NAME)as D " +
					"WHERE C.TABLE_NAME = D.TABLE_NAME AND C.COUNTER = D.COUNTER";
    private static final String CONSTRAINTS = "SELECT DISTINCT COLUMN_NAME FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE WHERE TABLE_NAME = '";//GeschaetzteParameter'";

	public static final String CFG_STATEMENT = "statement";
	public static final String DEFAULT_CONFIG_STATEMENT = "INSERT INTO \"PUBLIC\".\"Versuchsbedingungen\" VALUES (;";
	public static final String FINISH_STATEMENT = ");";
	
	//private boolean update = true;
	
	
    /**
     * Creates a new model with one data input.
     */
    SimpleDatabaseWriterNodeModel() {
        super(1, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        loadSettings(settings, true);
    }

    private void loadSettings(
            final NodeSettingsRO settings, final boolean write)
            throws InvalidSettingsException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws CanceledExecutionException,
            Exception {
    	
    	//Map<String, BufferedDataTable> tableDatas = new HashMap<String, BufferedDataTable>();
    	//InsertMetaData metaData = buildInsertMetaData(tableDatas);
    	
        return new BufferedDataTable[0];
    }	
/*
    private InsertMetaData buildInsertMetaData(
			Map<String, BufferedDataTable> tableDatas) {
		InsertMetaData metaData = new InsertMetaData(tableDatas.keySet());
		
		for(Map.Entry<String, BufferedDataTable> entry : tableDatas.entrySet()){
			InsertMetaData.TableMetaData tableMetaData = new InsertMetaData.TableMetaData(entry.getKey());
			for (int i = 0; i < entry.getValue().getDataTableSpec().getNumColumns(); i++) {
				String columnName = entry.getValue().getDataTableSpec().getColumnSpec(i).getName();
				tableMetaData.getColumnNames().add(columnName);
			}
			metaData.addTableMetaData(tableMetaData);
		}
		
		return metaData;
	}
	private PreparedStatementInput prepareStatement(DataTable table) {
    	String tableName = getTableNameByColumnNames(table);
    	PreparedStatementInput input = null;
    	List<String> columnNames = new ArrayList<String>();
    	for (int i = 0; i < table.getDataTableSpec().getNumColumns(); i++) {
    		columnNames.add(table.getDataTableSpec().getColumnSpec(i).getName());
    	}
		return input;
	}
*/
    
	protected List<String> buildStatement(DataTable table) {
		String names = "";
		String values = "";
		List<String> statements = new ArrayList<String>();
    	StringBuilder sbuildNames = new StringBuilder();
    	StringBuilder sbuildValues = new StringBuilder();
    	String tableName = getTableNameByColumnNames(table);
    	List<String> constraints = getTableConstraints(tableName);
    	List<Integer> positions = new ArrayList<Integer>();
    	sbuildNames.append("INSERT INTO \"" + tableName + "\" (");
		if (tableName != null) {
			for (int i = 0; i < table.getDataTableSpec().getNumColumns(); i++) {
				String columnName = table.getDataTableSpec().getColumnSpec(i).getName();
				//take out all constraints:
//				if (constraints.contains(columnName)) { 
				//take out primary keys only:
				if (constraints.get(0).equals(columnName)) {
					positions.add(i);
				} else {
					if (i != (table.getDataTableSpec().getNumColumns() - 1)) {
						sbuildNames.append("\"" + columnName + "\", ");
					} else if (i == table.getDataTableSpec().getNumColumns() - 1) {
						sbuildNames.append("\"" + columnName + "\") VALUES (");
					}
				}
			}
			names = sbuildNames.toString();
			
        	RowIterator rowIter = table.iterator();
        	while (rowIter.hasNext()) {
        		DataRow row = rowIter.next();
        		Iterator<DataCell> colIter = row.iterator();
        		int cellPosition = 0;
        		while (colIter.hasNext()) {
        			DataCell cell = colIter.next();
        			
        			//take out all constraints
					if (positions.contains(cellPosition)) {							
						sbuildValues.append("");									
					} else if (cell.isMissing() || cell.toString().isEmpty())  {	
        				sbuildValues.append(", null");
    				} else {
        				sbuildValues.append(", " + cell.toString());
    				}
					cellPosition++;
        		}
	        	sbuildValues.append(")");
	        	values = sbuildValues.substring(1);
	        	sbuildValues.delete(0, sbuildValues.length());
	        	sbuildNames.delete(0, sbuildNames.length()).append(names);
				sbuildNames.append(values);
				statements.add(sbuildNames.toString());
        	}
		}
		return statements;
		
	}

	private List<String> getTableConstraints(String tableName) {
		List<String> constraints = new ArrayList<String>();
		BFRNodeService service = BfRNodePluginActivator.getBfRService();
		StringBuilder stmt = new StringBuilder();
		stmt.append(CONSTRAINTS + tableName + "\'");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = service.getJDBCConnection();
			st = con.createStatement();
			rs = st.executeQuery(stmt.toString());
			while (rs.next()) {
				constraints.add(rs.getObject(1).toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
				rs.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return constraints;
	}
	/*
	private List<String> getTableDetailedConstraints(String tableName) {
		List<String> constraints = new ArrayList<String>();
		BFRNodeService service = BfRNodePluginActivator.getBfRService();
		StringBuilder stmt = new StringBuilder();
		stmt.append(CONSTRAINTS + tableName + "\'");
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = service.getJDBCConnection();
			st = con.createStatement();
			rs = st.executeQuery(stmt.toString());
			while (rs.next()) {
				constraints.add(rs.getObject(1).toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				st.close();
				rs.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return constraints;
	}
*/
	private String getTableNameByColumnNames(DataTable table) {
		StringBuilder tableStatement = new StringBuilder();
    	StringBuilder buildColNames = new StringBuilder();
    	for (int i = 0; i < table.getDataTableSpec().getNumColumns(); i++) {
    		buildColNames.append("', '" + table.getDataTableSpec().getColumnSpec(i).getName());
    	}
    	buildColNames.append("'");
    	String colNames = buildColNames.substring(2);
    	tableStatement.append(PRE_COLNAME).append(colNames).append(POST_COLNAME);
    	String result = "";
    	BFRNodeService service = BfRNodePluginActivator.getBfRService();
    	Connection con = service.getJDBCConnection();
		logger.info("Identifying table...");
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(tableStatement.toString());
			rs.next();
			result = rs.getObject(1).toString();
			logger.info("Identified table as: [" + result + "]");
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Could not identify the target database table.");
		} finally {
			try {
				stmt.close();
				rs.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File nodeInternDir,
            final ExecutionMonitor exec) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File nodeInternDir,
            final ExecutionMonitor exec) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
        if (inSpecs[0].getNumColumns() == 0) {
            throw new InvalidSettingsException("No columns in input data.");
        }
        return new DataTableSpec[0];
    }
    /*
    private static class PreparedStatementInput{
    	
    	String tableName = "";
    	
    	Map<String, String> values = new HashMap<String, String>();
		Map<String, String> mappedIds = new HashMap<String, String>();
    	
    	public String getTableName() {
			return tableName;
		}
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		public Map<String, String> getValues() {
			return values;
		}
		public Map<String, String> getMappedIds() {
			return mappedIds;
		}
    }
    */
}
