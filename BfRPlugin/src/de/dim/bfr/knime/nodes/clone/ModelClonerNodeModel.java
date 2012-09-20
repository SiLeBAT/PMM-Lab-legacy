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
package de.dim.bfr.knime.nodes.clone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.knime.base.node.io.filereader.ColProperty;
import org.knime.base.node.io.filereader.DataCellFactory;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;




/**
 * The node model of the table creator node that allows the creation of a
 * data table.
 *
 * @author Heiko Hofer
 */
public class ModelClonerNodeModel extends NodeModel {
    private ModelClonerNodeSettings m_settings;
	private DataTableSpec[] inSpecs;


    /** Creates a new instance. */
//    protected BfRTableManipulatorNodeModel() {
//        super(new PortType[0], new PortType[]{BufferedDataTable.TYPE});
//        m_settings = new BfRTableManipulatorNodeSettings();
//    }


    public ModelClonerNodeModel(int inPorts, int outPorts) {
    	super(inPorts, outPorts);
    	m_settings = new ModelClonerNodeSettings();    	
	}


	/**
     * {@inheritDoc}
     */
    @Override
//    BufferedDataTable
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
    	this.inSpecs = inSpecs;
    	createSpec();
    	return inSpecs;
    }
    
    private DataTableSpec createSpec() throws InvalidSettingsException {
        int numColumns = max(m_settings.getColumnIndices()) + 1;
        if (! m_settings.getColumnProperties().isEmpty()) {
            numColumns = Math.max(numColumns,
                    m_settings.getColumnProperties().lastKey() + 1);
        }
        for (int i = 0; i < numColumns; i++) {
            if (!m_settings.getColumnProperties().containsKey(i)) {
                throw new InvalidSettingsException("Name and type is not "
                        + "specified for every column.");
            }
        }

        List<DataColumnSpec> colList = new ArrayList<DataColumnSpec>();
        for (int i = 0; i < numColumns; i++) {
            if (!m_settings.getColumnProperties().get(i).getSkipThisColumn()) {
                colList.add(
                       m_settings.getColumnProperties().get(i).getColumnSpec());
            }
        }
        DataColumnSpec[] cols =
            colList.toArray(new DataColumnSpec[colList.size()]);
        return new DataTableSpec(cols);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec)
            throws Exception {
        DataTableSpec outSpec = this.inSpecs[0];

        BufferedDataContainer cont = exec.createDataContainer(
                outSpec, true);

        int numColProps = m_settings.getColumnProperties().size();
        ColProperty[] colProps = new ColProperty[numColProps];
        for (int i = 0; i < numColProps; i++) {
            colProps[i] = m_settings.getColumnProperties().get(i);
        }
        int cc = 0;
        int[] notSkippedMap = new int[numColProps];
        for (int i = 0; i < numColProps; i++) {
            notSkippedMap[i] = cc;
            if (!colProps[i].getSkipThisColumn()) {
                cc++;
            }
        }
        int numRows = max(m_settings.getRowIndices()) + 1;
        String rowIdPrefix = m_settings.getRowIdPrefix();
        String rowIdSuffix = m_settings.getRowIdSuffix();
        int rowIdStartWidth = m_settings.getRowIdStartValue();
        int c = 0;
        DataCellFactory cellFactory = new DataCellFactory();
        for (int i = 0; i < numRows; i++) {
            DataCell[] cells = new DataCell[outSpec.getNumColumns()];
            for (int k = 0; k < numColProps; k++) {
                String value = "";
                if (c < m_settings.getRowIndices().length
                        && m_settings.getRowIndices()[c] == i
                        && m_settings.getColumnIndices()[c] == k) {
                    value = m_settings.getValues()[c];
                    c++;
                }
                if (colProps[k].getSkipThisColumn()) {
                    continue;
                }
                String missValPattern = colProps[k].getMissingValuePattern();
                cellFactory.setMissingValuePattern(missValPattern);
                DataCell result = cellFactory.createDataCellOfType(
                            colProps[k].getColumnSpec().getType(), value);
                if (null != result) {
                    cells[notSkippedMap[k]] = result;
                } else {
                    throw new InvalidSettingsException(
                            cellFactory.getErrorMessage());
                }
            }
            StringBuilder rowId = new StringBuilder();
            rowId.append(rowIdPrefix);
            rowId.append(Integer.toString(i + rowIdStartWidth));
            rowId.append(rowIdSuffix);

            DataRow row = new DefaultRow(rowId.toString(), cells);
            cont.addRowToTable(row);
        }
        cont.close();

        BufferedDataTable out = cont.getTable();

        return new BufferedDataTable[]{out};
    }



    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
        throws InvalidSettingsException {
        ModelClonerNodeSettings s = new ModelClonerNodeSettings();
        s.loadSettings(settings);
    }

    private int max(final int[] values) {
        int max = -1;
        for(int i = 0; i < values.length; i++) {
            max = Math.max(max, values[i]);
        }
        return max;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
        throws InvalidSettingsException {
        m_settings.loadSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	
    	DataTableSpec dts = inSpecs[0]; 

    	SortedMap<Integer, ColProperty> colProps = new TreeMap<Integer, ColProperty>();
    	
    	for(int i=0; i<dts.getNumColumns(); i++)
    	{    		
    		ColProperty cp = new ColProperty();
    		cp.setColumnSpec(dts.getColumnSpec(i));
    		colProps.put(i, cp);
    	}
    	
		m_settings.setColumnProperties(colProps);
    	
        m_settings.saveSettings(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File nodeInternDir,
            final ExecutionMonitor exec)
            throws IOException {
        // Node has no internal data.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File nodeInternDir,
            final ExecutionMonitor exec)
            throws IOException {
        // Node has no internal data.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // Node has no internal data.
    }

}
