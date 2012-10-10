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
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import de.dim.bfr.external.service.BFRNodeService;
import de.dim.bfr.knime.nodes.db.InsertMetaData.ForeignKeyMetaData;
import de.dim.bfr.knime.nodes.db.InsertMetaData.TableMetaData;
import de.dim.bfr.knime.ports.BufferedTableContainer;
import de.dim.bfr.knime.util.PluginUtils;
import de.dim.knime.bfr.internal.BfRNodePluginActivator;

public class DatabaseArrayWriterNodeModel extends NodeModel {

	private static NodeLogger logger = NodeLogger
			.getLogger(DatabaseArrayWriterNodeModel.class);
	/*
	private static final String PRE_COLNAME = "SELECT C.TABLE_NAME FROM "
			+ "(SELECT TABLE_NAME, COUNT(TABLE_NAME) AS COUNTER "
			+ "FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME IN (";
	private static final String POST_COLNAME = ")GROUP BY TABLE_NAME)as C, "
			+ "(SELECT TABLE_NAME, COUNT(TABLE_NAME) AS COUNTER FROM "
			+ "INFORMATION_SCHEMA.COLUMNS GROUP BY TABLE_NAME)as D "
			+ "WHERE C.TABLE_NAME = D.TABLE_NAME AND C.COUNTER = D.COUNTER";
	private static final String CONSTRAINTS = "SELECT DISTINCT COLUMN_NAME FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE WHERE TABLE_NAME = '";// GeschaetzteParameter'";
	*/
	private static final String FOREIGN_KEY = "select PKTABLE_NAME, PKCOLUMN_NAME, FKCOLUMN_NAME from INFORMATION_SCHEMA.SYSTEM_CROSSREFERENCE WHERE FKTABLE_NAME = ?";
	private static final String PRIMARY_KEY = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.SYSTEM_PRIMARYKEYS WHERE TABLE_NAME= ?";

	public static final String CFG_STATEMENT = "statement";
	public static final String DEFAULT_CONFIG_STATEMENT = "INSERT INTO \"PUBLIC\".\"Versuchsbedingungen\" VALUES (;";
	public static final String FINISH_STATEMENT = ");";

	private boolean update = false;

	/**
	 * Creates a new model with one data input.
	 */
	DatabaseArrayWriterNodeModel() {
		super(new PortType[] { BufferedTableContainer.TYPE }, new PortType[0]);
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
//		loadSettings(settings, true);
	}
/*
	private void loadSettings(final NodeSettingsRO settings, final boolean write)
			throws InvalidSettingsException {
//		if (settings.containsKey("test"))
//			update = DatabaseArrayWriterNodePane.UPDATE.equals(settings
//					.getString("test"));
	}
*/
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws CanceledExecutionException,
			Exception {
		HashMap<String, BufferedDataTable> tableMap = convertPortObject(
				inData[0], exec);

		InsertMetaData metaData = buildInsertMetaData(tableMap);

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		//CallableStatement call = null; old - StatUp
		try {
			con = BfRNodePluginActivator.getBfRService().getJDBCConnection();
			con.setAutoCommit(false);
			for (String table : metaData.getOrder()) {
				TableMetaData currentTableMetaData = metaData.getMetaDataForTable(table);
				List<ExecutableStatement> insertStmts = createInsertStatements(currentTableMetaData, tableMap.get(table), metaData, con);
				st = con.createStatement();
				//call = con.prepareCall("CALL IDENTITY()"); old - StatUp
				int i = 0;
				for (ExecutableStatement stmt : insertStmts) {
					st = con.createStatement();
					logger.debug("executing batch for table [" + table
							+ "] with statement " + stmt.getStatement());
					//st.execute(stmt.getStatement()); old - StatUp
					st.execute(stmt.getStatement(), Statement.RETURN_GENERATED_KEYS );

					if (stmt.getType() == ExecutableStatement.INSERT) {
						//call.execute(); old - StatUp
						//rs = call.getResultSet(); old - StatUp
						rs = st.getGeneratedKeys();
						
						logger.debug("looking for results ");
						if (rs.next()) {
							String id = rs.getString(1);
							currentTableMetaData.setNewId(i++, id);
						}
						logger.debug("fetched " + i + " IDs");
					} else {
						currentTableMetaData.setNewId(i++,
								((UpdateStatemt) stmt).getId());
						logger.debug("successfully updated row with id ["
								+ ((UpdateStatemt) stmt).getId() + "]");
					}
					st.close();
					if (rs != null)
						rs.close();
				}
			}
			con.commit();
			
		} catch (SQLException e) {
			logger.error(
					"something went wrong during statement: " + e.getSQLState(),
					e);
			con.rollback();
			throw new CanceledExecutionException(
					"The following SQL Exception happend:" + e.getMessage()
							+ " for SQL " + e.getSQLState());
		} catch (Exception e) {
			logger.error("something went wrong during statement", e);
			con.rollback();
			throw new CanceledExecutionException(
					"The following Exception happend:" + e.getMessage());
		} finally {
			try {
				if (!st.isClosed())
					st.close();
				if (rs != null && !rs.isClosed())
					rs.close();
				BfRNodePluginActivator.getBfRService().closeJDBCConnection(con);
			} catch (SQLException e) {
				logger.error(
						"unable to finallize connection stuff: "
								+ e.getMessage(), e);
			}

		}
		logger.info("finished writing to database");
		logger.info("refreshing Editors from databse");
		BfRNodePluginActivator.getBfRService().refreshServiceContent();
		logger.info("finished refreshing Editors from databse");
		return new BufferedDataTable[0];
	}

	private List<ExecutableStatement> createInsertStatements(
			TableMetaData tableMetaData, BufferedDataTable table,
			InsertMetaData insertMetaData, Connection con) throws CanceledExecutionException {

		List<ExecutableStatement> statements = new ArrayList<ExecutableStatement>();
		List<String> colNames = tableMetaData.getColumnNames();

		RowIterator rowIter = table.iterator();
		logger.debug("building insert statements for table ["
				+ tableMetaData.getTableName() + "]");
		while (rowIter.hasNext()) {
			DataRow row = rowIter.next();
			ExecutableStatement stmt = null;
			if (update
					&& entryAlreadyExists(tableMetaData, colNames, row,
							insertMetaData)) {
				stmt = buildUpdateStatement(tableMetaData, colNames, row,
						insertMetaData);
			} else
				stmt = buildInsertStatement(tableMetaData, colNames, row,
						insertMetaData, con);
			statements.add(stmt);
		}
		return statements;
	}

	private ExecutableStatement buildUpdateStatement(
			TableMetaData tableMetaData, List<String> colNames, DataRow row,
			InsertMetaData insertMetaData) throws CanceledExecutionException {

		StringBuilder updateStmt = new StringBuilder();
		updateStmt.append("update \"" + tableMetaData.getTableName()
				+ "\" set ");
		boolean first = true;
		for (int i = 0; i < row.getNumCells(); i++) {
			String colName = tableMetaData.getColumnNames().get(i);
			if (!tableMetaData.isPrimaryColumn(colName)) {
				if (!first) {
					updateStmt.append(", ");
				}
				first = false;
				updateStmt.append("\"");
				updateStmt.append(colName);
				updateStmt.append("\"=");
				updateStmt.append(normalizedCell(row.getCell(i)));
			}
		}
		updateStmt.append(" where ");
		if (tableMetaData.getPrimaryColumns().size() > 1)
			throw new CanceledExecutionException(
					"This node does not support combined primary keys ");
		String id = null;
		for (String col : tableMetaData.getPrimaryColumns()) {

			if (tableMetaData.getPrimaryColumns().indexOf(col) > 0)
				updateStmt.append("and ");

			updateStmt.append("\"" + col + "\"=");
			updateStmt
					.append(normalizedCell(row.getCell(colNames.indexOf(col))));
			updateStmt.append(" ");
			id = row.getCell(colNames.indexOf(col)).toString();
		}
		tableMetaData.addOldId(id.toString());
		return new UpdateStatemt(id, updateStmt.toString());
	}

	private boolean entryAlreadyExists(TableMetaData tableMetaData,
			List<String> colNames, DataRow row, InsertMetaData insertMetaData) {
		logger.debug("building select Statement");
		StringBuilder selectStmt = new StringBuilder();
		selectStmt.append("select * from \"");
		selectStmt.append(tableMetaData.getTableName());
		selectStmt.append("\" where ");
		for (String col : tableMetaData.getPrimaryColumns()) {

			if (tableMetaData.getPrimaryColumns().indexOf(col) > 0)
				selectStmt.append("and ");

			selectStmt.append("\"" + col + "\"=");
			selectStmt
					.append(normalizedCell(row.getCell(colNames.indexOf(col))));
			selectStmt.append(" ");
		}

		logger.debug("Statement is: [" + selectStmt.toString() + "]");

		BFRNodeService service = BfRNodePluginActivator.getBfRService();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		boolean exists = false;
		try {
			con = service.getJDBCConnection();
			logger.debug("Opening database connection...");
			st = con.createStatement();
			rs = st.executeQuery(selectStmt.toString());
			logger.debug("Executing query " + selectStmt.toString());
			exists = rs.next();

			rs.close();
			st.close();
			BfRNodePluginActivator.getBfRService().closeJDBCConnection(con);
		} catch (SQLException e) {
			logger.error("Unable to close connection: " + e.getSQLState()
					+ "\n" + e.getMessage() + "\n" + e);
		}

		return exists;
	}

	private ExecutableStatement buildInsertStatement(
			TableMetaData tableMetaData, List<String> colNames, DataRow row,
			InsertMetaData insertMetaData, Connection con) throws CanceledExecutionException {
		StringBuffer firstPart = new StringBuffer();
		StringBuffer lastPart = new StringBuffer();
		firstPart.append("INSERT INTO \"" + tableMetaData.getTableName()
				+ "\" (");
		lastPart.append(") VALUES ( ");

		boolean isFirst = true;
		for (int i = 0; i < colNames.size(); i++) {
			String colName = colNames.get(i);
			DataCell cell = row.getCell(i);

			if (tableMetaData.isPrimaryColumn(colName)) {
				tableMetaData.addOldId(cell.toString());
			} else if (tableMetaData.isForeignKey(colName)) {
				if (!isFirst) {
					firstPart.append(" ,");
					lastPart.append(" ,");
				}
				firstPart.append("\"" + colName + "\"");
				String id = null;
				if (insertMetaData.isTableKnown(tableMetaData
						.getForeignKeyMetaDataForColumn(colName)
						.getTargetTableName())
						&& !isEmptyCell(cell)) {
					id = insertMetaData.getMetaDataForTable(
							tableMetaData.getForeignKeyMetaDataForColumn(
									colName).getTargetTableName())
							.getNewIdForOldId(cell.toString());
					if (id == null){
						id = idLookupForTable(cell.toString(), insertMetaData.getMetaDataForTable(
							tableMetaData.getForeignKeyMetaDataForColumn(
									colName).getTargetTableName()), con);
					}
					if (id == null) {
						logger.fatal("Referenced Table ["
								+ tableMetaData.getForeignKeyMetaDataForColumn(
										colName).getTargetTableName()
								+ "] with old ID ["
								+ cell.toString()
								+ "] not found. It seems the given data is inconsistent!");
						throw new CanceledExecutionException(
								"Referenced Table ["
										+ tableMetaData
												.getForeignKeyMetaDataForColumn(
														colName)
												.getTargetTableName()
										+ "] with old ID ["
										+ cell.toString()
										+ "] not found. It seems the given data are inconsistent!");
					}
				} else {
					id = normalizedCell(cell);
				}
				lastPart.append(id);
				isFirst = false;
			} else {
				if (!isFirst) {
					firstPart.append(" ,");
					lastPart.append(" ,");
				}
				firstPart.append("\"" + colName + "\"");
				lastPart.append(normalizedCell(cell));
				isFirst = false;
			}
		}
		lastPart.append(")");
		String stmt = firstPart.toString() + lastPart.toString();
		logger.debug("build insert Statment: " + stmt);
		return new ExecutableStatement(ExecutableStatement.INSERT, stmt);
	}

	private String idLookupForTable(String id,
			TableMetaData metaDataForTable, Connection con) throws CanceledExecutionException {
		StringBuilder selectStmt = new StringBuilder();
		selectStmt.append("select * ");
		selectStmt.append("from \"");
		selectStmt.append(metaDataForTable.getTableName());
		selectStmt.append("\" where ");
		for(String primaryColumn : metaDataForTable.getPrimaryColumns()){
			selectStmt.append("\"");
			selectStmt.append(primaryColumn);
			selectStmt.append("\"");
			selectStmt.append("=");
			selectStmt.append(id);
		}
		Statement stmt = null;
		ResultSet resultSet = null;
		try {
			stmt = con.createStatement();
			logger.debug("searching for ID with stmt [" + selectStmt.toString() + "]");
			resultSet = stmt.executeQuery(selectStmt.toString());
			if(resultSet.next())
				return id;
		} catch (SQLException e) {
			logger.error("error during select statement [" + selectStmt.toString() + "]: " + e.getMessage(), e);
			throw new CanceledExecutionException(e.getMessage());
		} finally {
			try {
				if(resultSet != null && !resultSet.isClosed())
					resultSet.close();
				if(stmt != null && !stmt.isClosed())
					stmt.close();
			} catch (SQLException e) {
				logger.error("Can not close Connection", e);
				throw new CanceledExecutionException(e.getMessage());
			}
		}
		
		return null;
	}

	private boolean isEmptyCell(DataCell cell) {
		return cell == null || cell.isMissing()
				|| "null".equals(cell.toString()) || cell.toString() == null
				|| cell.toString().isEmpty();
	}

	private String normalizedCell(DataCell cell) {
		if (isEmptyCell(cell))
			return "null";
		if (cell instanceof StringCell)
			return "'" + cell.toString() + "'";

		return cell.toString();
	}

	private HashMap<String, BufferedDataTable> convertPortObject(
			PortObject portObject, ExecutionContext exec) throws IOException,
			CanceledExecutionException {
		BufferedTableContainer container = (BufferedTableContainer) portObject;
		HashMap<String, BufferedDataTable> tableMap = new HashMap<String, BufferedDataTable>();

		for (String tableName : container.getTableFileNames()) {
			File file = container.getTable(tableName);
			BufferedDataTable table = PluginUtils.readOutData(file, exec);
			tableMap.put(tableName, table);
		}
		return tableMap;
	}

	private InsertMetaData buildInsertMetaData(
			Map<String, BufferedDataTable> tableDatas)
			throws CanceledExecutionException {
		InsertMetaData metaData = new InsertMetaData(tableDatas.keySet());
		logger.info("start building MetaData");
		// read all the Metadata we need
		for (Map.Entry<String, BufferedDataTable> entry : tableDatas.entrySet()) {
			List<String> columns = new ArrayList<String>();
			for (int i = 0; i < entry.getValue().getDataTableSpec()
					.getNumColumns(); i++) {
				String columnName = entry.getValue().getDataTableSpec()
						.getColumnSpec(i).getName();
				columns.add(columnName);
				logger.debug("adding column [" + columnName + "] to table ["
						+ entry.getKey() + "]");
			}
			String tableName = entry.getKey();
			if ("null".equals(tableName) || tableName == null) {
				tableName = PluginUtils.getTableNameByColNames(columns);
			}
			logger.debug("creating new TableMetaData for table [" + tableName
					+ "]");
			InsertMetaData.TableMetaData tableMetaData = new InsertMetaData.TableMetaData(
					tableName);
			tableMetaData.getColumnNames().addAll(columns);
			addPrimaryKeys(tableMetaData);
			addForeignKeysMetaData(tableMetaData);
			metaData.addTableMetaData(tableMetaData);
			logger.debug("successfully created new TableMetaData for table ["
					+ entry.getKey() + "]");
		}

		// now make sure that the tables get the right order
		for (String tableName : metaData.getTables()) {
			TableMetaData tableMetaData = metaData
					.getMetaDataForTable(tableName);
			logger.debug("processing foreign keys for table [" + tableName
					+ "]");
			for (ForeignKeyMetaData foreignKey : tableMetaData
					.getForeignKeyMetaDatas()) {
				logger.debug("found foreign key from table [" + tableName
						+ "] column [" + foreignKey.getColumnName()
						+ "] to foreign table ["
						+ foreignKey.getTargetTableName()
						+ "] and foreign column ["
						+ foreignKey.getTargetPrimaryKey() + "]");
				if (metaData.getOrder().contains(
						foreignKey.getTargetTableName())) {
					metaData.placeBefore(foreignKey.getTargetTableName(),
							tableName);
				} else {
					logger.debug("the foreign key is not in the input set!");
				}
			}
			logger.debug("finished processing foreign keys for table ["
					+ tableName + "]");
		}
		logger.info("The resulting insert order is:");
		for (String table : metaData.getOrder()) {
			logger.info("\t" + table);
		}
		logger.info("finished building MetaData");
		return metaData;
	}

	private void addForeignKeysMetaData(TableMetaData tableMetaData) {
		logger.debug("Querying for foreign keys...");
		BFRNodeService service = BfRNodePluginActivator.getBfRService();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = service.getJDBCConnection();
			logger.debug("Opening database connection...");
			st = con.prepareStatement(FOREIGN_KEY);
			st.setString(1, tableMetaData.getTableName());
			rs = st.executeQuery();
			logger.debug("Executing query...");
			while (rs.next()) {
				tableMetaData.addForeignKeyMetaData(new ForeignKeyMetaData(rs
						.getString("FKCOLUMN_NAME"), rs
						.getString("PKTABLE_NAME"), rs
						.getString("PKCOLUMN_NAME")));
			}
			rs.close();
			st.close();
			BfRNodePluginActivator.getBfRService().closeJDBCConnection(con);
		} catch (SQLException e) {
			logger.error("Unable to close connection: " + e.getSQLState()
					+ "\n" + e.getMessage() + "\n" + e);
		}
	}

	private void addPrimaryKeys(TableMetaData tableMetaData) {
		logger.debug("Querying for primary keys...");
		BFRNodeService service = BfRNodePluginActivator.getBfRService();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = service.getJDBCConnection();
			logger.debug("Opening database connection...");
			st = con.prepareStatement(PRIMARY_KEY);
			st.setString(1, tableMetaData.getTableName());
			rs = st.executeQuery();
			logger.debug("Executing query...");
			while (rs.next()) {
				tableMetaData.getPrimaryColumns().add(
						rs.getString("COLUMN_NAME"));
			}
			rs.close();
			st.close();
			BfRNodePluginActivator.getBfRService().closeJDBCConnection(con);
		} catch (SQLException e) {
			logger.error("Unable to close connection: " + e.getSQLState()
					+ "\n" + e.getMessage() + "\n" + e);
		}
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
	protected DataTableSpec[] configure(final PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {
		return null;
	}

	private static class ExecutableStatement {

		public static final int INSERT = 0;
		public static final int UPDATE = 1;

		private int type;

		private String statement;

		public ExecutableStatement(int type, String statement) {
			super();
			this.type = type;
			this.statement = statement;
		}

		public int getType() {
			return type;
		}

		public String getStatement() {
			return statement;
		}
	}

	private static class UpdateStatemt extends ExecutableStatement {

		private String id;

		public UpdateStatemt(String id, String statement) {
			super(UPDATE, statement);
			this.id = id;
		}

		public String getId() {
			return id;
		}

	}
}
