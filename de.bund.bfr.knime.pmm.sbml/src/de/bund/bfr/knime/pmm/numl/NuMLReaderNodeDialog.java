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
		fileChooser = new DialogComponentFileChooser(fileName, "filename-history", JFileChooser.OPEN_DIALOG, false);
		
		// Add widgets
		createNewGroup("Data source");
		addDialogComponent(fileChooser);
		
		// start showing fileChooser
		fileChooser.getComponentPanel().setVisible(true);
	}
}
