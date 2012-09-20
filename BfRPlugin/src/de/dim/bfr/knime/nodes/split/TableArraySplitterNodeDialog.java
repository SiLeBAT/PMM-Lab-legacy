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

import java.util.Arrays;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObjectSpec;

/**
 * <code>NodeDialog</code> for the "TableArraySplitter" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author DataInMotion
 */
public class TableArraySplitterNodeDialog extends DefaultNodeSettingsPane {

	private String[] m_tableNames;
	private SettingsModelString m_tableNameModel;
	private DialogComponentStringSelection m_dialogComponent;

    protected TableArraySplitterNodeDialog() {
    	m_tableNames = new String[]{"no tables available"};
        m_tableNameModel = new SettingsModelString(TableArraySplitterNodeModel.CFG_TABLE_NAME_ARRAY, "");
        m_dialogComponent = new DialogComponentStringSelection(
				m_tableNameModel,"tablename: ",m_tableNames);
		addDialogComponent(m_dialogComponent);
    }
    
	@Override
	public void loadAdditionalSettingsFrom(NodeSettingsRO settings,
			PortObjectSpec[] specs) throws NotConfigurableException {
    	m_tableNames = settings.getStringArray(TableArraySplitterNodeModel.CFG_TABLE_NAME_ARRAY,"");
    	try {
			m_dialogComponent.replaceListItems(Arrays.asList(m_tableNames), settings.getString(TableArraySplitterNodeModel.CFG_TABLE_NAME));
		} catch (InvalidSettingsException e) {
			e.printStackTrace();
		}
	}
    
    @Override
    public void saveAdditionalSettingsTo(NodeSettingsWO settings)
    		throws InvalidSettingsException {
    	settings.addString(TableArraySplitterNodeModel.CFG_TABLE_NAME, m_tableNameModel.getStringValue());
    }
}

