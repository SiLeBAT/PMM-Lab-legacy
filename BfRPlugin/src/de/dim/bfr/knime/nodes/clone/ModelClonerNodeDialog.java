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

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.knime.base.node.io.tablecreator.table.Spreadsheet;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

/**
 * The dialog class of the Table Creator Node.
 * 
 * @author Heiko Hofer
 */
public final class ModelClonerNodeDialog extends NodeDialogPane {
	   private Spreadsheet m_spreadsheet;
	    private ModelClonerNodeSettings m_settings;

	    /**
	     * Create a new instance.
	     */
	    ModelClonerNodeDialog() {
	        m_settings = new ModelClonerNodeSettings();
	        addTab("Table Creator Settings", createTableCreatorSettingsTab());
	    }

	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    public boolean closeOnESC() {
	        // do not close the dialog when ESC is pressed. 
	        // Allows the table to use ESC to cancel the edit mode of a cell.
	        return false;
	    }
	    
	    private JPanel createTableCreatorSettingsTab() {
	        JPanel p = new JPanel(new BorderLayout());
	        m_spreadsheet = new Spreadsheet();
	        p.add(m_spreadsheet, BorderLayout.CENTER);
	        return p;
	    }
	    /**
	     * {@inheritDoc}
	     */
	    @Override
	    protected void loadSettingsFrom(final NodeSettingsRO settings,
	            final DataTableSpec[] specs) {        
	    	m_settings.loadSettingsForDialog(settings);
	   	
	        m_spreadsheet.setData(
	                m_settings.getColumnProperties(),
	                m_settings.getRowIndices(),
	                m_settings.getColumnIndices(),
	                m_settings.getValues());
	        m_spreadsheet.setRowIdPrefix(m_settings.getRowIdPrefix());
	        m_spreadsheet.setRowIdSuffix(m_settings.getRowIdSuffix());
	        m_spreadsheet.setRowIdStartValue(m_settings.getRowIdStartValue());
	        m_spreadsheet.setHighlightOutputTable(
	                m_settings.getHightlightOutputTable());
	        // I (Heiko) would rather like to give the table the focus, but this
	        // does not seem to work, instead clear focused cell
	        m_spreadsheet.clearFocusedCell();
//	        m_spreadsheet.setData(columnProperties, rowIndices, columnIndices, values)
	    }

	    /**
	     * {@inheritDoc}
	     * @throws InvalidSettingsException
	     */
	    @Override
	    protected void saveSettingsTo(final NodeSettingsWO settings)
	    throws InvalidSettingsException {
	        // Commit editing - This is a workaround for a bug in the Dialog
	        // since the table does not loose focus when OK or Apply is pressed.
	        m_spreadsheet.stopCellEditing();
	        if (m_spreadsheet.hasParseErrors()) {
	            throw new InvalidSettingsException("Some cells cannot be parsed.");
	        }
	        m_settings.setRowIndices(m_spreadsheet.getRowIndices());
	        m_settings.setColumnIndices(m_spreadsheet.getColumnIndices());
	        m_settings.setValues(m_spreadsheet.getValues());
	        m_settings.setColumnProperties(m_spreadsheet.getColumnProperties());
	        m_settings.setRowIdPrefix(m_spreadsheet.getRowIdPrefix());
	        m_settings.setRowIdSuffix(m_spreadsheet.getRowIdSuffix());
	        m_settings.setRowIdStartValue(m_spreadsheet.getRowIdStartValue());
	        m_settings.setHightlightOutputTable(
	                m_spreadsheet.getHightLightOutputTable());
	        m_settings.saveSettings(settings);

	    }


}
