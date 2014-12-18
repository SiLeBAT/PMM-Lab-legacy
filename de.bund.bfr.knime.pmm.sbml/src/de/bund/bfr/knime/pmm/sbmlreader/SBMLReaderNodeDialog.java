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

interface SBMLReaderDlgState {
	void switchState();

	void showUi();
}

class FileState implements SBMLReaderDlgState {
	SBMLReaderNodeDialog dlg;

	public FileState(SBMLReaderNodeDialog dlg) {
		this.dlg = dlg;
	}

	public void switchState() {
		this.dlg.setState(this.dlg.getDirState());
	}

	public void showUi() {
		dlg.getModelTypePanel().setVisible(false);
		dlg.getFileChooserPanel().setVisible(true);
		dlg.getFolderChooserPanel().setVisible(false);
	}
}

class DirState implements SBMLReaderDlgState {
	SBMLReaderNodeDialog dlg;

	public DirState(SBMLReaderNodeDialog dlg) {
		this.dlg = dlg;
	}

	public void switchState() {
		this.dlg.setState(this.dlg.getFileState());
	}

	public void showUi() {
		dlg.getModelTypePanel().setVisible(true);
		dlg.getFileChooserPanel().setVisible(false);
		dlg.getFolderChooserPanel().setVisible(true);
	}
}

public class SBMLReaderNodeDialog extends DefaultNodeSettingsPane {

	SBMLReaderDlgState fileState;
	SBMLReaderDlgState dirState;

	SBMLReaderDlgState state; // current state

	DialogComponentStringSelection modelTypeDlg;
	DialogComponentFileChooser fileChooser;
	DialogComponentFileChooser folderChooser;

	/**
	 * New pane for configuring SBMLReader node dialog. This is just a
	 * suggestion to demonstrate possible default dialog components.
	 */
	protected SBMLReaderNodeDialog() {
		super();

		// Init states
		fileState = new FileState(this);
		dirState = new DirState(this);
		state = fileState;
		
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
		source.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				state.switchState();
				state.showUi();
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

		// Add widgets
		createNewGroup("Data Source");
		addDialogComponent(sourceDlg);
		addDialogComponent(modelTypeDlg);
		addDialogComponent(fileChooser);
		addDialogComponent(folderChooser);
		
		state.showUi();
	}

	// Widget related methods
	public JPanel getModelTypePanel() {
		return modelTypeDlg.getComponentPanel();
	}

	public JPanel getFileChooserPanel() {
		return fileChooser.getComponentPanel();
	}

	public JPanel getFolderChooserPanel() {
		return folderChooser.getComponentPanel();
	}

	// State related methods
	public SBMLReaderDlgState getFileState() {
		return fileState;
	}

	public SBMLReaderDlgState getDirState() {
		return dirState;
	}

	public void setState(SBMLReaderDlgState state) {
		this.state = state;
	}
}