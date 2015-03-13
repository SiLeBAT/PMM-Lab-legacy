package de.bund.bfr.knime.pmm.sbmlreader;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "SBMLReader" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * Author: Miguel de Alba Aparicio (malba@optimumquality.es)
 */

public class SBMLReaderNodeDialog extends DefaultNodeSettingsPane {

	DialogComponentFileChooser fileChooser;

	/**
	 * New pane for configuring SBMLReader node dialog. This is just a
	 * suggestion to demonstrate possible default dialog components.
	 */
	protected SBMLReaderNodeDialog() {
		super();

		// Set model strings
		final SettingsModelString fileName = new SettingsModelString(
				SBMLReaderNodeModel.CFGKEY_FILE, "");
		fileName.setEnabled(true);

		// Create fileChooser
		fileChooser = new DialogComponentFileChooser(fileName,
				"filename-history", JFileChooser.OPEN_DIALOG, false);

		// Add widgets
		createNewGroup("Data Source");
		addDialogComponent(fileChooser);

		// start showing fileChooser
		fileChooser.getComponentPanel().setVisible(true);
	}
}