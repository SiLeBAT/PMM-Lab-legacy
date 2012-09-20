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
package de.dim.bfr.knime.nodes.merge.arraytoarray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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


/**
 * This is the model implementation of ArrayToArrayMerger.
 * 
 *
 * @author Data In Motion
 */
public class ArrayToArrayMergerNodeModel extends NodeModel {
    
    private static final NodeLogger logger = NodeLogger
            .getLogger(ArrayToArrayMergerNodeModel.class);
	public static final String CFG_TABLE_NAME = "TABLENAME";
	public static final String CFG_TABLE_NAME_ARRAY = "TABLENAMES";
	private String tableName;
	private String[] tableNames;
	
    public static final String OVERRIDE = "OV";
	private boolean overwrite;
	private List<String> tableNamesInPort2;
	
    protected ArrayToArrayMergerNodeModel() {
    	super(new PortType[]{BufferedTableContainer.TYPE, BufferedTableContainer.TYPE}, new PortType[]{BufferedTableContainer.TYPE});
    }
  /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {
    	BufferedTableContainer container = null;
    	
    	if (inData[0] != null) {
    		container = ((BufferedTableContainer)inData[0]);
    		if (inData[1] != null) {
    			BufferedTableContainer container2 = ((BufferedTableContainer)inData[1]);
    			
				for (String e : tableNamesInPort2) {
					container.addTableFile(e, container2.getTable(e));
				}
    		}
    	}
    	
        return new PortObject[]{container};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
    	tableName = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
            throws InvalidSettingsException {
    	if(!(inSpecs[0] instanceof BufferedTableContainerSpec))
    		throw new InvalidSettingsException("BufferedTableContainerSpec expected");
    	
    	tableNames = ((BufferedTableContainerSpec) inSpecs[0]).getTableNames();
    	if(tableName == null)
    		tableName = tableNames[0];

    	ArrayList<String> overwritables = new ArrayList<String>();
    	LinkedList<String> tableNamesInPort1 = new LinkedList<String>(Arrays.asList(((BufferedTableContainerSpec) inSpecs[0]).getTableNames()));
    	tableNamesInPort2 = new LinkedList<String>(Arrays.asList(((BufferedTableContainerSpec) inSpecs[1]).getTableNames()));
    	ArrayList<String> names = new ArrayList<String>();
    	
		for (String f : tableNamesInPort2) {
			if (tableNamesInPort1.contains(f)) {
				overwritables.add(f);
			}
		}
		logger.info("port 0 provides: " + tableNamesInPort1.toString());
		logger.info("port 1 provides: " + tableNamesInPort2.toString());
		if (overwrite) {
			logger.info("The following tables form inport 1 will be overwritten: " + overwritables.toString());
			tableNamesInPort1.removeAll(overwritables);
			names.addAll(tableNamesInPort1);
		} else {
			tableNamesInPort2.removeAll(overwritables);
			if (tableNamesInPort2.size() < 1) {
				logger.info("Data provided by Inport 2 will be ignored.");
			} else {
			logger.info("Duplicate table names " + overwritables.toString() + " from inport 2 will be ignored.");
			}
			names.addAll(tableNamesInPort1);
			names.addAll(tableNamesInPort2);
		}
    	
        return new PortObjectSpec[1];
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
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
		overwrite = settings.getBoolean(OVERRIDE, false);
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

