package de.bund.bfr.knime.pmm.fskx.editor;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplateSettings;
import de.bund.bfr.openfsmr.FSMRTemplate;

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
class FskEditorNodeSettings {

	// configuration keys
	private static final String MODEL_SCRIPT = "Model script";
	private static final String PARAM_SCRIPT = "Parameters script";
	private static final String VIZ_SCRIPT = "Visualization script";
	private static final String META_DATA = "Meta data";

	private String modelScript = null;
	private String paramScript = null;
	private String vizScript = null;
	private FSMRTemplate template = null;

	private boolean isSetModelScript = false;
	private boolean isSetParametersScript = false;
	private boolean isSetVisualizationScript = false;
	private boolean isSetMetaData = false;

	// --- settings methods ---
	/**
	 * Saves scripts to settings object.
	 * 
	 * @param settings
	 *            To save to.
	 */
	void saveSettings(final NodeSettingsWO settings) {
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
	void loadSettings(final NodeSettingsRO settings) {
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
	String getModelScript() {
		return modelScript;
	}

	void setModelScript(final String modelScript) {
		this.modelScript = modelScript;
		isSetModelScript = true;
	}

	boolean isSetModelScript() {
		return isSetModelScript;
	}

	void unsetModelScript() {
		modelScript = null;
		isSetModelScript = false;
	}

	// param script methods
	String getParametersScript() {
		return paramScript;
	}

	void setParametersScript(final String parametersScript) {
		this.paramScript = parametersScript;
		isSetParametersScript = true;
	}

	boolean isSetParametersScript() {
		return isSetParametersScript;
	}

	void unsetParametersScript() {
		paramScript = null;
		isSetParametersScript = false;
	}

	// visualization script methods
	String getVisualizationScript() {
		return vizScript;
	}

	void setVisualizationScript(final String visualizationScript) {
		this.vizScript = visualizationScript;
		isSetVisualizationScript = true;
	}

	boolean isSetVisualizationScript() {
		return isSetVisualizationScript;
	}

	void unsetVisualizationScript() {
		vizScript = null;
		isSetVisualizationScript = false;
	}

	// meta data methods
	FSMRTemplate getMetaData() {
		return template;
	}

	void setMetaData(FSMRTemplate template) {
		this.template = template;
		isSetMetaData = true;
	}

	boolean isSetMetaData() {
		return isSetMetaData;
	}

	void unsetMetaData() {
		template = null;
		isSetMetaData = false;
	}
}
