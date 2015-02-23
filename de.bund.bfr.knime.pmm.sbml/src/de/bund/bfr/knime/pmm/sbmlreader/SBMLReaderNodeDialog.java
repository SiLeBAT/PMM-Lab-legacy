package de.bund.bfr.knime.pmm.sbmlreader;

import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentButtonGroup;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
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

	DialogComponentStringSelection modelTypeDlg;
	DialogComponentFileChooser fileChooser;
	DialogComponentFileChooser folderChooser;
	DialogComponentFileChooser zipChooser;

	/**
	 * New pane for configuring SBMLReader node dialog. This is just a
	 * suggestion to demonstrate possible default dialog components.
	 */
	protected SBMLReaderNodeDialog() {
		super();

		// Set model strings
		final SettingsModelString source = new SettingsModelString(
				SBMLReaderNodeModel.CFGKEY_SOURCE, "file");
		final SettingsModelString modelType = new SettingsModelString(
				SBMLReaderNodeModel.CFGKEY_MODEL_TYPE, "primary");

		final SettingsModelString fileName = new SettingsModelString(
				SBMLReaderNodeModel.CFGKEY_FILE, "");
		fileName.setEnabled(true);
		final SettingsModelString folderName = new SettingsModelString(
				SBMLReaderNodeModel.CFGKEY_FOLDER, "");
		folderName.setEnabled(true);

		final SettingsModelString zipName = new SettingsModelString(
				SBMLReaderNodeModel.CFGKEY_ZIP, "");
		zipName.setEnabled(true);

		// Create UI widgets
		final String[] sourceOptions = { "file", "folder", "zip" };
		final DialogComponentButtonGroup sourceDlg = new DialogComponentButtonGroup(
				source, false, "Source", sourceOptions);
		sourceDlg
				.setToolTipText("Choose whether to load one file, a directory or a zip file");
		source.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				
				String sourceVal = source.getStringValue();
				if (sourceVal.equals("file")) {
					modelTypeDlg.getComponentPanel().setVisible(false);
					fileChooser.getComponentPanel().setVisible(true);
					folderChooser.getComponentPanel().setVisible(false);
					zipChooser.getComponentPanel().setVisible(false);
				} else if (sourceVal.equals("folder")) {
					modelTypeDlg.getComponentPanel().setVisible(true);
					fileChooser.getComponentPanel().setVisible(false);
					folderChooser.getComponentPanel().setVisible(true);
					zipChooser.getComponentPanel().setVisible(false);
				} else {
					modelTypeDlg.getComponentPanel().setVisible(true);
					fileChooser.getComponentPanel().setVisible(false);
					folderChooser.getComponentPanel().setVisible(false);
					zipChooser.getComponentPanel().setVisible(true);
				}
			}
		});

		final String[] modelTypeOptions = { "primary", "tertiary" };
		modelTypeDlg = new DialogComponentStringSelection(modelType,
				"Model type", modelTypeOptions);
		modelTypeDlg.setToolTipText("Model type");

		fileChooser = new DialogComponentFileChooser(fileName,
				"filename-history", JFileChooser.OPEN_DIALOG, false);

		folderChooser = new DialogComponentFileChooser(folderName,
				"foldername-history", JFileChooser.OPEN_DIALOG, true);

		zipChooser = new DialogComponentFileChooser(zipName, "zipname-history",
				JFileChooser.OPEN_DIALOG, false);

		// Add widgets
		createNewGroup("Data Source");
		addDialogComponent(sourceDlg);
		addDialogComponent(modelTypeDlg);
		addDialogComponent(fileChooser);
		addDialogComponent(folderChooser);
		addDialogComponent(zipChooser);
		
		// start showing fileChooser
		modelTypeDlg.getComponentPanel().setVisible(false);
		fileChooser.getComponentPanel().setVisible(true);
		folderChooser.getComponentPanel().setVisible(false);
		zipChooser.getComponentPanel().setVisible(false);
	}
}