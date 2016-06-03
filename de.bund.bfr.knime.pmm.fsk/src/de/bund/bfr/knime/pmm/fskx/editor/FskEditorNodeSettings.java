package de.bund.bfr.knime.pmm.fskx.editor;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplateImpl;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplateSettings;

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
public class FskEditorNodeSettings {

	// configuration keys
	private static final String MODEL_SCRIPT = "Model script";
	private static final String PARAM_SCRIPT = "Parameters script";
	private static final String VIZ_SCRIPT = "Visualization script";
	private static final String META_DATA = "Meta data";

	private String modelScript = null;
	private String paramScript = null;
	private String vizScript = null;
	private FSMRTemplate template;

	private boolean isSetModelScript = false;
	private boolean isSetParametersScript = false;
	private boolean isSetVisualizationScript = false;
	private boolean isSetMetaData = false;

	public FskEditorNodeSettings() {
		template = null;
	}

	public FskEditorNodeSettings(FSMRTemplate template) {
		this.template = new FSMRTemplateImpl(template);
		isSetMetaData = true;
	}

	// --- settings methods ---
	/**
	 * Saves scripts to settings object.
	 * 
	 * @param settings
	 *            To save to.
	 */
	public void saveSettings(final NodeSettingsWO settings) {
		if (isSetModelScript) {
			settings.addString(MODEL_SCRIPT, modelScript);
		}
		if (isSetParametersScript) {
			settings.addString(PARAM_SCRIPT, paramScript);
		}
		if (isSetVisualizationScript) {
			settings.addString(VIZ_SCRIPT, vizScript);
		}
		if (isSetMetaData) {
			FSMRTemplateSettings templateSettings = new FSMRTemplateSettings();
			templateSettings.setTemplate(template);
			templateSettings.saveToNodeSettings(settings.addNodeSettings(META_DATA));
		}
	}

	/**
	 * Loads scripts in NodeModel.
	 * 
	 * If some settings are missing, it means that they were not changed in the
	 * dialog so the original values from the input will be left intact.
	 * 
	 * @param settings
	 *            To load from
	 */
	public void loadSettings(final NodeSettingsRO settings) {
		try {
			if (settings.containsKey(MODEL_SCRIPT)) {
				setModelScript(settings.getString(MODEL_SCRIPT));
			}
			if (settings.containsKey(PARAM_SCRIPT)) {
				setParametersScript(settings.getString(PARAM_SCRIPT));
			}
			if (settings.containsKey(VIZ_SCRIPT)) {
				setVisualizationScript(settings.getString(VIZ_SCRIPT));
			}
			if (settings.containsKey(META_DATA)) {
				FSMRTemplateSettings templateSettings = new FSMRTemplateSettings();
				templateSettings.loadFromNodeSettings(settings.getNodeSettings(META_DATA));
				setMetaData(templateSettings.getTemplate());
			}
		} catch (InvalidSettingsException e) {
			// should not occur with containsKey check
			throw new RuntimeException(e);
		}
	}

	// model script methods
	public String getModelScript() {
		return modelScript;
	}

	public void setModelScript(final String modelScript) {
		this.modelScript = modelScript;
		isSetModelScript = true;
	}

	public boolean isSetModelScript() {
		return isSetModelScript;
	}

	public void unsetModelScript() {
		modelScript = null;
		isSetModelScript = false;
	}

	// param script methods
	public String getParametersScript() {
		return paramScript;
	}

	public void setParametersScript(final String parametersScript) {
		this.paramScript = parametersScript;
		isSetParametersScript = true;
	}

	public boolean isSetParametersScript() {
		return isSetParametersScript;
	}

	public void unsetParametersScript() {
		paramScript = null;
		isSetParametersScript = false;
	}

	// visualization script methods
	public String getVisualizationScript() {
		return vizScript;
	}

	public void setVisualizationScript(final String visualizationScript) {
		this.vizScript = visualizationScript;
		isSetVisualizationScript = true;
	}

	public boolean isSetVisualizationScript() {
		return isSetVisualizationScript;
	}

	public void unsetVisualizationScript() {
		vizScript = null;
		isSetVisualizationScript = false;
	}

	// meta data methods
	public FSMRTemplate getMetaData() {
		return template;
	}

	public void setMetaData(FSMRTemplate template) {
		this.template = template;
		isSetMetaData = true;
	}

	public boolean isSetMetaData() {
		return isSetMetaData;
	}

	public void unsetMetaData() {
		template = null;
		isSetMetaData = false;
	}
}
