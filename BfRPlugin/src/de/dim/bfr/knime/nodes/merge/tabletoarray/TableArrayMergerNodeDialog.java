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

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
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
public class TableArrayMergerNodeDialog extends NodeDialogPane{
	

	private String[] tableNames;
	private JComboBox comboBox;
	private JPanel configPanel;

    protected TableArrayMergerNodeDialog() {
		tableNames = new String[] {"no single table identified"};
    	
		JPanel tablePanel = new JPanel();
    	configPanel = new JPanel();
        tablePanel.setBorder(BorderFactory
                .createTitledBorder(" table name "));
    	comboBox = new JComboBox(tableNames);
    	comboBox.setEditable(true);
        tablePanel.add(comboBox);
    	configPanel.add(tablePanel);
    	
    	super.addTab("Options", configPanel);
    }
    
	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		String table = comboBox.getSelectedItem().toString();
		settings.addString(TableArrayMergerNodeModel.CFG_TABLE_NAME,table);
	}
	
	@Override
	protected void loadSettingsFrom(final NodeSettingsRO settings,
			PortObjectSpec[] specs) throws NotConfigurableException {
		tableNames = settings.getStringArray(
				TableArrayMergerNodeModel.CFG_TABLE_NAME_ARRAY, "");
		comboBox.removeAllItems();
		comboBox.addItem("NEWDATA");
		for (String e : tableNames) {
			comboBox.addItem(e);
		}
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				comboBox.setSelectedItem(settings.getString(TableArrayMergerNodeModel.CFG_TABLE_NAME, "NEWDATA"));
				
			}
		});
	}
}

