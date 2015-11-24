package de.bund.bfr.knime.pmm.pmfreader2;

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

public class PMFReaderNodeDialog extends DefaultNodeSettingsPane {

	DialogComponentFileChooser fileChooser;

	/**
	 * New pane for configuring SBMLReader node dialog.
	 */
	protected PMFReaderNodeDialog() {
		super();

		// Set model strings
		final SettingsModelString fileName = new SettingsModelString(PMFReaderNodeModel.CFGKEY_FILE, "");
		fileName.setEnabled(true);

		// Create fileChooser
		fileChooser = new DialogComponentFileChooser(fileName, "filename-history", JFileChooser.OPEN_DIALOG, ".pmf");

		// Add widgets
		createNewGroup("Data Source");
		addDialogComponent(fileChooser);

		// start showing fileChooser
		fileChooser.getComponentPanel().setVisible(true);
	}
}