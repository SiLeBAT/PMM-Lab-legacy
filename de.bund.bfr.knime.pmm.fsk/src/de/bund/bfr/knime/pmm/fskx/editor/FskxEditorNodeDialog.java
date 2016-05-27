package de.bund.bfr.knime.pmm.fskx.editor;

import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;

import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.ui.ScriptPanel;

public class FskxEditorNodeDialog extends DataAwareNodeDialogPane {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(FskxEditorNodeDialog.class);

  private FskxEditorNodeSettings settings;

  private ScriptPanel modelScriptPanel;
  private ScriptPanel paramScriptPanel;
  private ScriptPanel vizScriptPanel;
  
  // --- settings methods ---
  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input)
      throws NotConfigurableException {

    // Create settings: first assigns scripts from input and then apply changes (if existent) in the
    // passed settings
    FskPortObject fskObj = (FskPortObject) input[0];
    this.settings = new FskxEditorNodeSettings();
    this.settings.modelScript = fskObj.getModelScript();
    this.settings.paramScript = fskObj.getParamScript();
    this.settings.vizScript = fskObj.getVizScript();
    this.settings.loadSettings(settings);
    
    // Panel names
    final String modelPanelName = "Model script";
    final String paramPanelName = "Parameters script";
    final String vizPanelName = "Visualization script";

    // Create and populate interface with settings
    removeTab(modelPanelName);
    modelScriptPanel = new ScriptPanel(modelPanelName, this.settings.modelScript, true);
    addTab(modelPanelName, modelScriptPanel);

    removeTab(paramPanelName);
    paramScriptPanel = new ScriptPanel(paramPanelName, this.settings.paramScript, true);
    addTab(paramPanelName, paramScriptPanel);

    removeTab(vizPanelName);
    vizScriptPanel = new ScriptPanel(vizPanelName, this.settings.vizScript, true);
    addTab(vizPanelName, vizScriptPanel);
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    // Update and save settings
    this.settings.modelScript = modelScriptPanel.getTextArea().getText();
    this.settings.paramScript = paramScriptPanel.getTextArea().getText();
    this.settings.vizScript = vizScriptPanel.getTextArea().getText();
    this.settings.saveSettings(settings);
  }
}
