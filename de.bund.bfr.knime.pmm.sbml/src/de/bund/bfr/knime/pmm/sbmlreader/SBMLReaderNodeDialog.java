package de.bund.bfr.knime.pmm.sbmlreader;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "SBMLReader" node.
 * 
 * @author Miguel de Alba
 */
public class SBMLReaderNodeDialog extends DefaultNodeSettingsPane {

	DialogComponentFileChooser fileChooser;

	/**
	 * New pane for configuring {@link SBMLReaderNodeDialog}
	 */
	protected SBMLReaderNodeDialog() {
		super();

		// Set model strings
		final SettingsModelString fileName = new SettingsModelString(SBMLReaderNodeModel.CFGKEY_FILE, "");
		fileName.setEnabled(true);

		// Create fileChooser
		fileChooser = new DialogComponentFileChooser(fileName, "filename-history", JFileChooser.OPEN_DIALOG, ".sbml");

		// Add widgets
		createNewGroup("Data source");
		addDialogComponent(fileChooser);

		// starts showing fileChooser
		fileChooser.getComponentPanel().setVisible(true);
	}
}
