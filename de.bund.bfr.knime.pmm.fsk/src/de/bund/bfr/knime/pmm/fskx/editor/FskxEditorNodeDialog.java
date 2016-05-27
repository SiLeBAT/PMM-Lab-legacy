package de.bund.bfr.knime.pmm.fskx.editor;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.fife.ui.rtextarea.RTextScrollPane;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObject;

import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.rsnippet.RSnippet;
import de.bund.bfr.knime.pmm.fskx.ui.RSnippetTextArea;

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

    // Create and populate interface with settings
    removeTab("Model script");
    modelScriptPanel = new ScriptPanel("Model script", this.settings.modelScript);
    addTab("Model script", modelScriptPanel);

    removeTab("Param script");
    paramScriptPanel = new ScriptPanel("Param script", this.settings.paramScript);
    addTab("Param script", paramScriptPanel);

    removeTab("Visualization script");
    vizScriptPanel = new ScriptPanel("Visualization script", this.settings.vizScript);
    addTab("Visualisation script", vizScriptPanel);
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) throws InvalidSettingsException {
    // Update and save settings
    this.settings.modelScript = modelScriptPanel.textArea.getText();
    this.settings.paramScript = paramScriptPanel.textArea.getText();
    this.settings.vizScript = vizScriptPanel.textArea.getText();
    this.settings.saveSettings(settings);
  }
}


/** JPanel with an R script */
class ScriptPanel extends JPanel {

  private static final long serialVersionUID = -2150198208821903469L;

  RSnippetTextArea textArea;

  ScriptPanel(final String title, final String script) {
    super(new BorderLayout());
    setName(title);

    textArea = new RSnippetTextArea(new RSnippet());
    textArea.setLineWrap(true);
    textArea.setText(script);
    textArea.setEditable(true);
    add(new RTextScrollPane(textArea));
  }
}
