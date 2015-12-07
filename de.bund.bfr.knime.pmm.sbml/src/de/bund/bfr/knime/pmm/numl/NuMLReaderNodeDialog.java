/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.numl;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "NuMLReader" Node.
 * 
 * @author Miguel de Alba
 */
public class NuMLReaderNodeDialog extends DefaultNodeSettingsPane {

	DialogComponentFileChooser fileChooser;
	
	/**
	 * New pane for configuring {@link NuMLReaderNodeDialog}.
	 */
	
	protected NuMLReaderNodeDialog() {
		super();
		
		// Set model strings
		final SettingsModelString fileName = new SettingsModelString(NuMLReaderNodeModel.CFGKEY_FILE, "");
		fileName.setEnabled(true);
		
		// Create fileChooser
		fileChooser = new DialogComponentFileChooser(fileName, "filename-history", JFileChooser.OPEN_DIALOG, ".numl");
		
		// Add widgets
		createNewGroup("Data source");
		addDialogComponent(fileChooser);
		
		// start showing fileChooser
		fileChooser.getComponentPanel().setVisible(true);
	}
}
