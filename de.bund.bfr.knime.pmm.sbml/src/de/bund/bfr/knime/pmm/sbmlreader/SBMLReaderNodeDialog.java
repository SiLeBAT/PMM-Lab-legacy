package de.bund.bfr.knime.pmm.sbmlreader;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
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

		// Create UI widgets
		final String[] sourceOptions = { "file", "folder" };
		final DialogComponentButtonGroup sourceDlg = new DialogComponentButtonGroup(
				source, false, "Source", sourceOptions);
		sourceDlg
				.setToolTipText("Choose whether to load one single file or a whole directory");

		final String[] modelTypeOptions = { "primary", "tertiary" };
		final DialogComponentStringSelection modelTypeDlg = new DialogComponentStringSelection(
				modelType, "Model type", modelTypeOptions);
		modelTypeDlg.setToolTipText("Model type");
		modelTypeDlg.getComponentPanel().setVisible(false);

		final DialogComponentFileChooser fileChooser = new DialogComponentFileChooser(
				fileName, "filename-history", JFileChooser.OPEN_DIALOG, false);

		final DialogComponentFileChooser folderChooser = new DialogComponentFileChooser(
				folderName, "foldername-history", JFileChooser.OPEN_DIALOG,
				true);
		folderChooser.getComponentPanel().setVisible(false);

		source.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				JPanel modelTypePanel = modelTypeDlg.getComponentPanel();
				JPanel fileChooserPanel = fileChooser.getComponentPanel();
				JPanel folderChooserPanel = folderChooser.getComponentPanel();
				if (source.getStringValue() == "file") {
					// hide modelTypeDlg and folderChooser
					modelTypePanel.setVisible(false);
					fileChooserPanel.setVisible(true);
					folderChooserPanel.setVisible(false);
				} else {
					// show modelTypeDlg
					modelTypePanel.setVisible(true);
					fileChooserPanel.setVisible(false);
					folderChooserPanel.setVisible(true);
				}
			}
		});

		// Add widgets
		createNewGroup("Data Source");
		addDialogComponent(sourceDlg);
		addDialogComponent(modelTypeDlg);
		addDialogComponent(fileChooser);
		addDialogComponent(folderChooser);
	}
}