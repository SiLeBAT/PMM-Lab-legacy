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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.knime.base.node.io.database.DBVariableSupportNodeModel;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.workflow.CredentialsProvider;

import de.dim.bfr.external.service.BFRNodeService;
import de.dim.bfr.knime.util.PluginUtils;
import de.dim.knime.bfr.internal.BfRNodePluginActivator;

class SimpleDatabaseReaderNodeModel extends NodeModel implements DBVariableSupportNodeModel {
    public static final String CFG_STATEMENT = "statement";
	private static final String DEFAULT_CFG_STATEMENT = "SELECT * FROM \"PUBLIC\".\"Versuchsbedingungen\";";
	private static final String EXPERIMENT = "Versuchsbedingungen";
	
	private DataTableSpec 	m_lastSpec 	= null;
	private String m_query;
    private DataTableSpec	m_spec 		= null;
	
	
	private final SettingsModelString m_testQuery =
		new SettingsModelString(SimpleDatabaseReaderNodeModel.CFG_STATEMENT, null);
	private int m_selectedIndex;
	private File file;
   
    /**
     * Creates a new database reader with one data out-port.
     * @param ins number data input ports
     * @param outs number data output ports
     */
    SimpleDatabaseReaderNodeModel(final int ins, final int outs) 
    {
        super(ins, outs);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec) throws CanceledExecutionException, Exception 
    {
		BufferedDataTable[] tables = new BufferedDataTable[]{PluginUtils.readFromDB(m_testQuery.getStringValue(), exec)};
    	List<String> colNames = new ArrayList<String>();
    	for (int i = 0; i < tables[0].getDataTableSpec().getNumColumns(); i++) {
    		colNames.add(tables[0].getDataTableSpec().getColumnSpec(i).getName());
    	}
    	if (PluginUtils.getTableNameByColNames(colNames).equals(EXPERIMENT)) {
			file = PluginUtils.writeIntoCsvFile(tables[0], exec);
    		pushFlowVariableString(PluginUtils.VERSUCHSBEDINGUNGEN, file.getAbsolutePath());
    	}
    	return tables;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delegatePeekFlowVariableInt(final String name) {
        return super.peekFlowVariableInt(name);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public double delegatePeekFlowVariableDouble(final String name) {
        return peekFlowVariableDouble(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String delegatePeekFlowVariableString(final String name) {
        return peekFlowVariableString(name);
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
            final ExecutionMonitor exec) throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File nodeInternDir,
            final ExecutionMonitor exec) throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException 
    {
        try 
        {
        	BFRNodeService service = BfRNodePluginActivator.getBfRService();
        	if(!service.arePreferencesSet())
        		throw new InvalidSettingsException("Invalide oder fehlende BfR Plugin Prefrences gesetzt");
        	if(m_testQuery.getStringValue() == null || m_testQuery.getStringValue().isEmpty())
        		throw new InvalidSettingsException("Bitte eine Tabelle auswählen");
        	CredentialsProvider cp = getCredentialsProvider();
         	SimpleDatabaseConnectionSettings query_connection = new SimpleDatabaseConnectionSettings();
         	Connection conn = service.getJDBCConnection();
         	if (conn == null) 
                throw new InvalidSettingsException("No database connection available.");

         	
         	Statement st = conn.createStatement();
         	
            final ResultSet result = st.executeQuery(m_testQuery.getStringValue());
            
            m_lastSpec = PluginUtils.createTableSpec(result.getMetaData());
            st.close();
            service.closeJDBCConnection(conn);
//            conn.close();
            
            return new DataTableSpec[]{m_lastSpec};
        } 
        catch (InvalidSettingsException e) { m_lastSpec = null; throw e; } 
        catch (Throwable t) { m_lastSpec = null; throw new InvalidSettingsException(t); }
    }

    /**
     * @param newQuery the new query to set
     */
    final void setQuery(final String newQuery) {
        m_query = newQuery;
    }

    /**
     * @return current query
     */
    final String getQuery() {
        return m_query;
    }

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_testQuery.saveSettingsTo(settings);
		settings.addInt(SimpleDatabaseConnectionSettings.TABLE_INDEX, m_selectedIndex);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings)
			throws InvalidSettingsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
			throws InvalidSettingsException {
		m_testQuery.loadSettingsFrom(settings);
		m_selectedIndex = settings.getInt(SimpleDatabaseConnectionSettings.TABLE_INDEX, 0);      
		
	}

}
