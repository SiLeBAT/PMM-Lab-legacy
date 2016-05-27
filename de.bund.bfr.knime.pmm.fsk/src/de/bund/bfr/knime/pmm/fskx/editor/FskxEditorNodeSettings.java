package de.bund.bfr.knime.pmm.fskx.editor;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.ConfigRO;
import org.knime.core.node.config.ConfigWO;

import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;

/**
 * Settings of the FSKX editor node. Holds:
 * <ul>
 * <li>Model script</li>
 * <li>Parameters script</li>
 * <li>Visualization script</li>
 * <li>Model meta data</li>
 * </ul>
 * 
 * @author Miguel Alba
 */
public class FskxEditorNodeSettings {

  // configuration keys
  private static final String MODEL_SCRIPT = "modelScript";
  private static final String PARAM_SCRIPT = "paramScript";
  private static final String VIZ_SCRIPT = "vizScript";

  String modelScript = null;
  String paramScript = null;
  String vizScript = null;
  FSMRTemplate template;


  // --- settings methods ---
  /**
   * Saves scripts to settings object.
   * 
   * @param settings To save to.
   */
  public void saveSettings(final ConfigWO settings) {
    if (modelScript != null) {
      settings.addString(MODEL_SCRIPT, modelScript);
    }
    if (paramScript != null) {
      settings.addString(PARAM_SCRIPT, paramScript);
    }
    if (vizScript != null) {
      settings.addString(VIZ_SCRIPT, vizScript);
    }
  }

  /**
   * Loads scripts in NodeModel.
   * 
   * If some settings are missing, it means that they were not changed in the dialog so the original
   * values from the input will be left intact.
   * 
   * @param settings To load from
   */
  public void loadSettings(final ConfigRO settings) {
    try {
      modelScript = settings.getString(MODEL_SCRIPT);
    } catch (InvalidSettingsException e) {
    }
    
    try {
      paramScript = settings.getString(PARAM_SCRIPT);
    } catch (InvalidSettingsException e) {
    }
    
    try {
      vizScript = settings.getString(VIZ_SCRIPT);
    } catch (InvalidSettingsException e) {
    }
  }
}
