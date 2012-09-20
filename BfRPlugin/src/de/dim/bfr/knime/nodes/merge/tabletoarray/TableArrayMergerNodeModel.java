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
package de.dim.bfr.knime.nodes.merge.tabletoarray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataTableSpec;
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
import de.dim.bfr.knime.util.PluginUtils;


/**
 * This is the model implementation of TableArraySplitter.
 * 
 *
 * @author DataInMotion
 */
public class TableArrayMergerNodeModel extends NodeModel {
    
    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(TableArrayMergerNodeModel.class);

	public static final String CFG_TABLE_NAME 		= "CFG_TABLE_NAME";
	public static final String CFG_TABLE_NAME_ARRAY = "TABLE_ARRAY_NAME";
	public static final String CFG_NEW_TABLE_NAME = "CFG_NEW_TABLE_NAME";

	private static final String NEWDATA = "NEWDATA";
	

    private String tableName;
    private String[] tableNames;

	private String identifiedName;
	private String[] identifiedTable;
    

    /**
     * Constructor for the node model.
     */
    protected TableArrayMergerNodeModel() {
        super(new PortType[]{new PortType(BufferedTableContainer.class, true),BufferedDataTable.TYPE}, new PortType[]{BufferedTableContainer.TYPE});
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	if (tableName == null)
    		tableName = "NEWDATA";
    	logger.info("Single table identified as:" + tableName);
    	BufferedTableContainer out = new BufferedTableContainer();
    	if(inData[0] != null){
    		((BufferedTableContainer)inData[0]).addTableFile(tableName, PluginUtils.writeIntoCsvFile((BufferedDataTable) inData[1], exec));
    		out = ((BufferedTableContainer)inData[0]); 
    	}else if(inData[1] != null){
    		out = new BufferedTableContainer();
    		out.addTableFile(tableName, PluginUtils.writeIntoCsvFile((BufferedDataTable) inData[1], exec));
    	}
    		
        return new PortObject[]{out};
    }

	/**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
    	tableNames = null;
    	tableName = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
            throws InvalidSettingsException {

    	if(inSpecs[1] == null) {
    		logger.error("No table data found at input port 1");
    	}
    	List<String> colNames = new ArrayList<String>();
    	
    	DataTableSpec tableSpec = ((DataTableSpec)inSpecs[1]);
    	for (int i = 0; i < tableSpec.getNumColumns(); i++) {
    		colNames.add(tableSpec.getColumnSpec(i).getName());
    	}
    	if (!colNames.isEmpty()) {
	    	List<String> namesList = PluginUtils.getTableNamesByColNames(colNames);
	    	tableNames = namesList.toArray(new String[namesList.size()]);
	    	if (tableName == null && tableNames.length>0) {
	    		tableName = tableNames[0];
	    	}
    	}
    		
        return new PortObjectSpec[1];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	if (tableName != null)
			settings.addString(CFG_TABLE_NAME, tableName);
    	if (tableNames!=null)
	    	settings.addStringArray(CFG_TABLE_NAME_ARRAY, tableNames);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	tableName = ((settings.containsKey(CFG_TABLE_NAME) && settings.getString(CFG_TABLE_NAME) != null) ? settings.getString(CFG_TABLE_NAME) : NEWDATA);
//    	tableName = settings.getString(CFG_TABLE_NAME);
    	
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

