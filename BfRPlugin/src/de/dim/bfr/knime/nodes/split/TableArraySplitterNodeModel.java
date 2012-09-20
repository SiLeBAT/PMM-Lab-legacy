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
package de.dim.bfr.knime.nodes.split;

import java.io.File;
import java.io.IOException;

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

import de.dim.bfr.knime.ports.BufferedTableContainer;
import de.dim.bfr.knime.ports.BufferedTableContainerSpec;
import de.dim.bfr.knime.util.PluginUtils;


/**
 * This is the model implementation of TableArraySplitter.
 * 
 *
 * @author DataInMotion
 */
public class TableArraySplitterNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(TableArraySplitterNodeModel.class);

	public static final String CFG_TABLE_NAME = "TABLE_NAME";

	public static final String CFG_TABLE_NAME_ARRAY = "TABLE_ARRAY_NAME";

    private String m_tableName;
    private String[] m_tableNames;
    

    /**
     * Constructor for the node model.
     */
    protected TableArraySplitterNodeModel() {
        super(new PortType[]{BufferedTableContainer.TYPE}, new PortType[]{BufferedTableContainer.TYPE, BufferedDataTable.TYPE});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	BufferedDataTable separatedTable = separateTable(m_tableName,inData[0],exec);
    	logger.info("Split table: " + m_tableName);
        return new PortObject[]{inData[0],separatedTable};
    }

    private BufferedDataTable separateTable(String tableName,
			PortObject portObject, ExecutionContext exec) throws InvalidSettingsException, CanceledExecutionException, IOException {
		
    	if(!(portObject instanceof BufferedTableContainer))
    		throw new InvalidSettingsException("BufferedTableContainer expected!");
    	
    	BufferedDataTable bdt = null; 
    	if (PluginUtils.readOutData(((BufferedTableContainer) portObject).getTable(tableName),exec)!=null)
	    	bdt = PluginUtils.readOutData(((BufferedTableContainer) portObject).getTable(tableName),exec);
    	else
    		throw new CanceledExecutionException("Execution Canceled");
    	
		return bdt;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
//        m_tableName = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
            throws InvalidSettingsException {
    	
    	if(!(inSpecs[0] instanceof BufferedTableContainerSpec))
    		throw new InvalidSettingsException("BufferedTableContainerSpec expected");
    	
    	m_tableNames = ((BufferedTableContainerSpec) inSpecs[0]).getTableNames();
    	if(m_tableName == null)
    		m_tableName = m_tableNames[0];

        return new PortObjectSpec[2];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	settings.addString(CFG_TABLE_NAME,m_tableName);
    	settings.addStringArray(CFG_TABLE_NAME_ARRAY,m_tableNames);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	m_tableName = settings.getString(CFG_TABLE_NAME,null);
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
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
    }

}

