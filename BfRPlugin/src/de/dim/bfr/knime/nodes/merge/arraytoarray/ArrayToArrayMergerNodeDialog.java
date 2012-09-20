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

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;


/**
 * <code>NodeDialog</code> for the "ArrayToArrayMerger" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Data In Motion
 */
public class ArrayToArrayMergerNodeDialog extends NodeDialogPane{

	JPanel configPanel;
	JCheckBox overrideDataBox;
	
	
    protected ArrayToArrayMergerNodeDialog() {
    	configPanel = new JPanel();
    	overrideDataBox = new JCheckBox("Replace duplicate tables on inport one");
    	
    	configPanel.add(overrideDataBox);
    	super.addTab("Options", configPanel);
    }

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
    	settings.addBoolean(ArrayToArrayMergerNodeModel.OVERRIDE, overrideDataBox.isSelected());
	}
	
	@Override
	protected void loadSettingsFrom(org.knime.core.node.NodeSettingsRO settings,
			org.knime.core.node.port.PortObjectSpec[] specs) 
	throws org.knime.core.node.NotConfigurableException {
		
	}
}

