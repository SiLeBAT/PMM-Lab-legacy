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
package de.dim.bfr.knime.nodes.filterdispatcher;

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

import de.dim.bfr.knime.util.PluginUtils;


/**
 * This is the model implementation of Preload.
 * Preload Data necessary to handle selections of experiments.
 *
 * @author Data In Motion
 */
public class FilterDispatcherNodeModel extends NodeModel {
    
    private static final NodeLogger logger = NodeLogger
            .getLogger(FilterDispatcherNodeModel.class);
	private File file;
        
    /**
     * Constructor for the node model.
     */
    protected FilterDispatcherNodeModel() {
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		logger.info("Preloading data necessary for handling a selection of experiments...");
		BufferedDataTable table = inData[0];

		file = PluginUtils.writeIntoCsvFile(table, exec);
		pushFlowVariableString(PluginUtils.VERSUCHSBEDINGUNGEN,
				file.getAbsolutePath());
		logger.info("Preload finished successfully.");

		return new BufferedDataTable[] { inData[0] };
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
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {
		List<String> colNames = new ArrayList<String>();
		for (int i = 0; i < inSpecs[0].getNumColumns(); i++) {
			colNames.add(inSpecs[0].getColumnSpec(i).getName());
		}
		if (!PluginUtils.getTableNameByColNames(colNames).equals(
				PluginUtils.VERSUCHSBEDINGUNGEN)) 
			throw new InvalidSettingsException("The table at the input port is not an extract of the table \"Versuchsbedingungen\"");
        return new DataTableSpec[]{null};
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

