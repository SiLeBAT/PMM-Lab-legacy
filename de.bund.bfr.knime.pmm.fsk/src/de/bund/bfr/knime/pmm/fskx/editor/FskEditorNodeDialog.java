package de.bund.bfr.knime.pmm.fskx.editor;

import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;

import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.ui.MetaDataPane;
import de.bund.bfr.knime.pmm.fskx.ui.ScriptPanel;

class FskEditorNodeDialog extends DataAwareNodeDialogPane {

	private FskEditorNodeSettings settings = new FskEditorNodeSettings();

	private ScriptPanel modelScriptPanel = new ScriptPanel("Model script", "", true);
	private ScriptPanel paramScriptPanel = new ScriptPanel("Parameters script", "", true);
	private ScriptPanel vizScriptPanel = new ScriptPanel("Visualization script", "", true);
	private MetaDataPane metaDataPane;

	public FskEditorNodeDialog() {
		addTab("Model script", modelScriptPanel);
		addTab("Parameters script", paramScriptPanel);
		addTab("Visualization script", vizScriptPanel);
	}

	// --- settings methods ---
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input) throws NotConfigurableException {

		// Create settings: first assigns scripts from input and then apply
		// changes (if existent) in the passed settings
		FskPortObject fskObj = (FskPortObject) input[0];

		this.settings.loadSettings(settings);

		if (!this.settings.isSetModelScript())
			this.settings.setModelScript(fskObj.getModelScript());
		if (!this.settings.isSetParametersScript())
			this.settings.setParametersScript(fskObj.getParamScript());
		if (!this.settings.isSetVisualizationScript())
			this.settings.setVisualizationScript(fskObj.getVizScript());
		if (!this.settings.isSetMetaData()) {
			this.settings.setMetaData(fskObj.getTemplate());
		}

		modelScriptPanel.getTextArea().setText(this.settings.getModelScript());
		paramScriptPanel.getTextArea().setText(this.settings.getParametersScript());
		vizScriptPanel.getTextArea().setText(this.settings.getVisualizationScript());

		// Panel names
		removeTab("Metadata");
		metaDataPane = new MetaDataPane(this.settings.getMetaData(), true);
		addTab("Metadata", metaDataPane);
	}
	
	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
		// Update and save settings
		this.settings.setModelScript(modelScriptPanel.getTextArea().getText());
		this.settings.setParametersScript(paramScriptPanel.getTextArea().getText());
		this.settings.setVisualizationScript(vizScriptPanel.getTextArea().getText());
		this.settings.setMetaData(metaDataPane.getMetaData());
		this.settings.saveSettings(settings);
	}
}
