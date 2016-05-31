package de.bund.bfr.knime.pmm.fskx.editor;

import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;

import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.ui.MetaDataPane;
import de.bund.bfr.knime.pmm.fskx.ui.ScriptPanel;

public class FskxEditorNodeDialog extends DataAwareNodeDialogPane {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(FskxEditorNodeDialog.class);

  private FskxEditorNodeSettings settings;

  private ScriptPanel modelScriptPanel;
  private ScriptPanel paramScriptPanel;
  private ScriptPanel vizScriptPanel;
  private MetaDataPane metaDataPane;
  
  // --- settings methods ---
  @Override
  protected void loadSettingsFrom(NodeSettingsRO settings, PortObject[] input)
      throws NotConfigurableException {

    // Create settings: first assigns scripts from input and then apply changes (if existent) in the
    // passed settings
    FskPortObject fskObj = (FskPortObject) input[0];
    this.settings = new FskxEditorNodeSettings(fskObj.getTemplate());
    this.settings.setModelScript(fskObj.getModelScript());
    this.settings.setParametersScript(fskObj.getParamScript());
    this.settings.setVisualizationScript(fskObj.getVizScript());
    this.settings.loadSettings(settings);
    
    // Panel names
    final String modelPanelName = "Model script";
    final String paramPanelName = "Parameters script";
    final String vizPanelName = "Visualization script";

    // Create and populate interface with settings
    removeTab(modelPanelName);
    modelScriptPanel = new ScriptPanel(modelPanelName, fskObj.getModelScript(), true);
    addTab(modelPanelName, modelScriptPanel);

    removeTab(paramPanelName);
    paramScriptPanel = new ScriptPanel(paramPanelName, fskObj.getParamScript(), true);
    addTab(paramPanelName, paramScriptPanel);

    removeTab(vizPanelName);
    vizScriptPanel = new ScriptPanel(vizPanelName, fskObj.getVizScript(), true);
    addTab(vizPanelName, vizScriptPanel);
    
    removeTab("Metadata");
    metaDataPane = new MetaDataPane(fskObj.getTemplate(), true);
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
