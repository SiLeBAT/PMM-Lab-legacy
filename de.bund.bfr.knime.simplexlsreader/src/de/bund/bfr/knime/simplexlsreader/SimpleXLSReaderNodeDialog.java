package de.bund.bfr.knime.simplexlsreader;

import javax.swing.JFileChooser;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelString;

/**
 * <code>NodeDialog</code> for the "SimpleXLSReader" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thöns
 */
public class SimpleXLSReaderNodeDialog extends DefaultNodeSettingsPane {

	private static final String FILE_HISTORY = "FileHistory";

	/**
	 * New pane for configuring SimpleXLSReader node dialog. This is just a
	 * suggestion to demonstrate possible default dialog components.
	 */
	protected SimpleXLSReaderNodeDialog() {
		super();

		addDialogComponent(new DialogComponentFileChooser(
				new SettingsModelString(
						SimpleXLSReaderNodeModel.CFGKEY_FILENAME,
						SimpleXLSReaderNodeModel.DEFAULT_FILENAME),
				FILE_HISTORY, JFileChooser.OPEN_DIALOG, "xls"));
		addDialogComponent(new DialogComponentNumberEdit(
				new SettingsModelInteger(
						SimpleXLSReaderNodeModel.CFGKEY_SHEETINDEX,
						SimpleXLSReaderNodeModel.DEFAULT_SHEETINDEX),
				"Sheet Index"));
	}
}
