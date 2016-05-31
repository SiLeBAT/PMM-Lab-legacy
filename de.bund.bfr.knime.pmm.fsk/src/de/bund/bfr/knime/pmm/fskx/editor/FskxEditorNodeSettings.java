package de.bund.bfr.knime.pmm.fskx.editor;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.config.ConfigRO;
import org.knime.core.node.config.ConfigWO;

import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplateImpl;
import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.ModelType;

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
	private static final String META_DATA = "metaData";

	private String modelScript = null;
	private String paramScript = null;
	private String vizScript = null;
	private FSMRTemplate template;

	private boolean isSetModelScript = false;
	private boolean isSetParametersScript = false;
	private boolean isSetVisualizationScript = false;
	private boolean isSetMetaData = false;

	public FskxEditorNodeSettings() {
		template = null;
	}

	public FskxEditorNodeSettings(FSMRTemplate template) {
		this.template = new FSMRTemplateImpl();
		try {
			if (template.isSetModelName()) {
				this.template.setModelName(new String(template.getModelName()));
			}
			if (template.isSetModelId()) {
				this.template.setModelId(new String(template.getModelId()));
			}
			if (template.isSetModelLink()) {
				this.template.setModelLink(new URL(template.getModelLink().toString()));
			}
			if (template.isSetOrganismName()) {
				this.template.setOrganismName(new String(template.getOrganismName()));
			}
			if (template.isSetOrganismDetails()) {
				this.template.setOrganismDetails(new String(template.getOrganismDetails()));
			}
			if (template.isSetMatrixName()) {
				this.template.setMatrixName(new String(template.getMatrixName()));
			}
			if (template.isSetMatrixDetails()) {
				this.template.setMatrixDetails(new String(template.getMatrixDetails()));
			}
			if (template.isSetCreator()) {
				this.template.setCreator(new String(template.getCreator()));
			}
			if (template.isSetFamilyName()) {
				this.template.setFamilyName(new String(template.getFamilyName()));
			}
			if (template.isSetContact()) {
				this.template.setContact(new String(template.getContact()));
			}
			if (template.isSetReferenceDescription()) {
				this.template.setReferenceDescription(new String(template.getReferenceDescription()));
			}
			if (template.isSetReferenceDescriptionLink()) {
				this.template.setReferenceDescriptionLink(new URL(template.getReferenceDescriptionLink().toString()));
			}
			if (template.isSetCreatedDate()) {
				this.template.setCreatedDate(new Date(template.getCreatedDate().getTime()));
			}
			if (template.isSetModifiedDate()) {
				this.template.setModifiedDate(new Date(template.getModifiedDate().getTime()));
			}
			if (template.isSetRights()) {
				this.template.setRights(new String(template.getRights()));
			}
			if (template.isSetNotes()) {
				this.template.setNotes(new String(template.getNotes()));
			}
			if (template.isSetCurationStatus()) {
				this.template.setCurationStatus(new String(template.getCurationStatus()));
			}
			if (template.isSetModelType()) {
				this.template.setModelType(template.getModelType());
			}
			if (template.isSetModelSubject()) {
				this.template.setModelSubject(template.getModelSubject());
			} else {
				this.template.setModelSubject(ModelClass.UNKNOWN);
			}
			if (template.isSetFoodProcess()) {
				this.template.setFoodProcess(new String(template.getFoodProcess()));
			}
			if (template.isSetDependentVariable()) {
				this.template.setDependentVariable(new String(template.getDependentVariable()));
			}
			if (template.isSetDependentVariableUnit()) {
				this.template.setDependentVariableUnit(new String(template.getDependentVariableUnit()));
			}
			if (template.isSetDependentVariableMin()) {
				this.template.setDependentVariableMin(new Double(template.getDependentVariableMin()));
			}
			if (template.isSetDependentVariableMax()) {
				this.template.setDependentVariableMax(new Double(template.getDependentVariableMax()));
			}
			if (template.isSetIndependentVariables()) {
				this.template.setIndependentVariables(template.getIndependentVariables().clone());
			}
			if (template.isSetIndependentVariablesUnits()) {
				this.template.setIndependentVariablesUnits(template.getIndependentVariablesUnits().clone());
			}
			if (template.isSetIndependentVariablesMins()) {
				this.template.setIndependentVariablesMins(template.getIndependentVariablesMins().clone());
			}
			if (template.isSetIndependentVariablesMaxs()) {
				this.template.setIndependentVariablesMaxs(template.getIndependentVariablesMaxs().clone());
			}
			if (template.isSetHasData()) {
				this.template.setHasData(new Boolean(template.getHasData()));
			}
		} catch (MalformedURLException e) {
			// passed template has valid settings so these exceptions are never
			// thrown
			System.err.println(e.getMessage());
			throw new RuntimeException(e);
		}

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
			NodeSettingsWO templateSettings = settings.addNodeSettings(META_DATA);
			FSMRTemplateSettings.saveSettings(template, templateSettings);
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
				setMetaData(FSMRTemplateSettings.loadSettings(settings.getNodeSettings(META_DATA)));
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

	static class FSMRTemplateSettings {

		private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");

		// configuration keys
		private static final String MODEL_NAME = "modelName";
		private static final String MODEL_ID = "modelId";
		private static final String MODEL_LINK = "modelLink";
		private static final String ORGANISM_NAME = "organismName";
		private static final String ORGANISM_DETAILS = "organismDetails";
		private static final String MATRIX_NAME = "matrixName";
		private static final String MATRIX_DETAILS = "matrixDetails";
		private static final String CREATOR = "creator";
		private static final String FAMILY_NAME = "familyName";
		private static final String CONTACT = "contact";
		private static final String REFERENCE_DESCRIPTION = "referenceDescription";
		private static final String REFERENCE_DESCRIPTION_LINK = "referenceDescriptionLink";
		private static final String CREATED_DATE = "createdDate";
		private static final String MODIFIED_DATE = "modifiedDate";
		private static final String RIGHTS = "rights";
		private static final String NOTES = "notes";
		private static final String CURATION_STATUS = "curationStatus";
		private static final String MODEL_TYPE = "modelType";
		private static final String MODEL_SUBJECT = "modelSubject";
		private static final String FOOD_PROCESS = "foodProcess";
		private static final String DEPENDENT_VARIABLE = "dependentVariable";
		private static final String DEPENDENT_VARIABLE_UNIT = "dependentVariableUnit";
		private static final String DEPENDENT_VARIABLE_MIN = "dependentVariableMin";
		private static final String DEPENDENT_VARIABLE_MAX = "dependentVariableMax";
		private static final String INDEPENDENT_VARIABLES = "independentVariables";
		private static final String INDEPENDENT_VARIABLES_UNITS = "independentVariablesUnits";
		private static final String INDEPENDENT_VARIABLES_MINS = "independentVariablesMins";
		private static final String INDEPENDENT_VARIABLES_MAXS = "independentVariablesMaxs";
		private static final String HAS_DATA = "hasData";

		/**
		 * Saves {@link FSMRTemplate} to settings object.
		 * 
		 * @param settings
		 *            To save to.
		 */
		public static void saveSettings(final FSMRTemplate template, final ConfigWO settings) {
			if (template.isSetModelName()) {
				settings.addString(MODEL_NAME, template.getModelName());
			}
			if (template.isSetModelId()) {
				settings.addString(MODEL_ID, template.getModelId());
			}
			if (template.isSetModelLink()) {
				settings.addString(MODEL_LINK, template.getModelLink().toString());
			}
			if (template.isSetOrganismName()) {
				settings.addString(ORGANISM_NAME, template.getOrganismName());
			}
			if (template.isSetOrganismDetails()) {
				settings.addString(ORGANISM_DETAILS, template.getOrganismDetails());
			}
			if (template.isSetMatrixName()) {
				settings.addString(MATRIX_NAME, template.getMatrixName());
			}
			if (template.isSetMatrixDetails()) {
				settings.addString(MATRIX_DETAILS, template.getMatrixDetails());
			}
			if (template.isSetCreator()) {
				settings.addString(CREATOR, template.getCreator());
			}
			if (template.isSetFamilyName()) {
				settings.addString(FAMILY_NAME, template.getFamilyName());
			}
			if (template.isSetContact()) {
				settings.addString(CONTACT, template.getContact());
			}
			if (template.isSetReferenceDescription()) {
				settings.addString(REFERENCE_DESCRIPTION, template.getReferenceDescription());
			}
			if (template.isSetReferenceDescriptionLink()) {
				settings.addString(REFERENCE_DESCRIPTION_LINK, template.getReferenceDescriptionLink().toString());
			}
			if (template.isSetCreatedDate()) {
				settings.addString(CREATED_DATE, dateFormat.format(template.getCreatedDate()));
			}
			if (template.isSetModifiedDate()) {
				settings.addString(MODIFIED_DATE, dateFormat.format(template.getModifiedDate()));
			}
			if (template.isSetRights()) {
				settings.addString(RIGHTS, template.getRights());
			}
			if (template.isSetNotes()) {
				settings.addString(NOTES, template.getNotes());
			}
			if (template.isSetCurationStatus()) {
				settings.addString(CURATION_STATUS, template.getCurationStatus());
			}
			if (template.isSetModelType()) {
				settings.addString(MODEL_TYPE, template.getModelType().name());
			}
			if (template.isSetModelSubject()) {
				settings.addString(MODEL_SUBJECT, template.getModelSubject().fullName());
			}
			if (template.isSetFoodProcess()) {
				settings.addString(FOOD_PROCESS, template.getFoodProcess());
			}
			if (template.isSetDependentVariable()) {
				settings.addString(DEPENDENT_VARIABLE, template.getDependentVariable());
			}
			if (template.isSetDependentVariableUnit()) {
				settings.addString(DEPENDENT_VARIABLE_UNIT, template.getDependentVariableUnit());
			}
			if (template.isSetDependentVariableMin()) {
				settings.addDouble(DEPENDENT_VARIABLE_MIN, template.getDependentVariableMin());
			}
			if (template.isSetDependentVariableMax()) {
				settings.addDouble(DEPENDENT_VARIABLE_MAX, template.getDependentVariableMax());
			}
			if (template.isSetIndependentVariables()) {
				settings.addStringArray(INDEPENDENT_VARIABLES, template.getIndependentVariables());
			}
			if (template.isSetIndependentVariablesUnits()) {
				settings.addStringArray(INDEPENDENT_VARIABLES_UNITS, template.getIndependentVariablesUnits());
			}
			if (template.isSetIndependentVariablesMins()) {
				Double[] objArray = template.getIndependentVariablesMins();
				double[] primArray = Arrays.stream(objArray).mapToDouble(Double::doubleValue).toArray();
				settings.addDoubleArray(INDEPENDENT_VARIABLES_MINS, primArray);
			}
			if (template.isSetIndependentVariablesMaxs()) {
				Double[] objArray = template.getIndependentVariablesMaxs();
				double[] primArray = Arrays.stream(objArray).mapToDouble(Double::doubleValue).toArray();
				settings.addDoubleArray(INDEPENDENT_VARIABLES_MAXS, primArray);
			}
			if (template.isSetHasData()) {
				settings.addBoolean(HAS_DATA, template.getHasData());
			}
		}

		/**
		 * Loads scripts in NodeModel.
		 * 
		 * @param settings
		 *            To load from
		 */
		public static FSMRTemplate loadSettings(final ConfigRO settings) {
			FSMRTemplate template = new FSMRTemplateImpl();

			try {
				if (settings.containsKey(MODEL_NAME)) {
					template.setModelName(settings.getString(MODEL_NAME));
				}
				if (settings.containsKey(MODEL_ID)) {
					template.setModelId(settings.getString(MODEL_ID));
				}
				if (settings.containsKey(MODEL_LINK)) {
					try {
						template.setModelLink(new URL(settings.getString(MODEL_LINK)));
					} catch (MalformedURLException e) {
						// does not happen -> internal links are checked before
						// being saved
						throw new RuntimeException(e);
					}
				}
				if (settings.containsKey(ORGANISM_NAME)) {
					template.setOrganismName(settings.getString(ORGANISM_NAME));
				}
				if (settings.containsKey(ORGANISM_DETAILS)) {
					template.setOrganismDetails(settings.getString(ORGANISM_DETAILS));
				}
				if (settings.containsKey(MATRIX_NAME)) {
					template.setMatrixName(settings.getString(MATRIX_NAME));
				}
				if (settings.containsKey(MATRIX_DETAILS)) {
					template.setMatrixDetails(settings.getString(MATRIX_DETAILS));
				}
				if (settings.containsKey(CREATOR)) {
					template.setCreator(settings.getString(CREATOR));
				}
				if (settings.containsKey(FAMILY_NAME)) {
					template.setFamilyName(settings.getString(FAMILY_NAME));
				}
				if (settings.containsKey(CONTACT)) {
					template.setContact(settings.getString(CONTACT));
				}
				if (settings.containsKey(REFERENCE_DESCRIPTION)) {
					template.setReferenceDescription(settings.getString(REFERENCE_DESCRIPTION));
				}
				if (settings.containsKey(REFERENCE_DESCRIPTION_LINK)) {
					try {
						template.setReferenceDescriptionLink(new URL(settings.getString(REFERENCE_DESCRIPTION_LINK)));
					} catch (MalformedURLException e) {
						// does not happen -> internal links are checked before
						// being saved
						throw new RuntimeException(e);
					}
				}
				if (settings.containsKey(CREATED_DATE)) {
					try {
						template.setCreatedDate(dateFormat.parse(settings.getString(CREATED_DATE)));
					} catch (ParseException e) {
						// does not happen -> internal dates are checked before
						// being saved
						throw new RuntimeException(e);
					}
				}
				if (settings.containsKey(MODIFIED_DATE)) {
					try {
						template.setModifiedDate(dateFormat.parse(settings.getString(MODIFIED_DATE)));
					} catch (ParseException e) {
						// does not happen -> internal dates are checked before
						// being saved
						throw new RuntimeException(e);
					}
				}
				if (settings.containsKey(RIGHTS)) {
					template.setRights(settings.getString(RIGHTS));
				}
				if (settings.containsKey(NOTES)) {
					template.setNotes(settings.getString(NOTES));
				}
				if (settings.containsKey(CURATION_STATUS)) {
					template.setCurationStatus(settings.getString(CURATION_STATUS));
				}
				if (settings.containsKey(MODEL_TYPE)) {
					template.setModelType(ModelType.valueOf(settings.getString(MODEL_TYPE)));
				}
				if (settings.containsKey(MODEL_SUBJECT)) {
					template.setModelSubject(ModelClass.fromName(settings.getString(MODEL_SUBJECT)));
				}
				if (settings.containsKey(MODEL_SUBJECT)) {
					template.setFoodProcess(settings.getString(MODEL_SUBJECT));
				}
				if (settings.containsKey(DEPENDENT_VARIABLE)) {
					template.setDependentVariable(settings.getString(DEPENDENT_VARIABLE));
				}
				if (settings.containsKey(DEPENDENT_VARIABLE_UNIT)) {
					template.setDependentVariableUnit(settings.getString(DEPENDENT_VARIABLE_UNIT));
				}
				if (settings.containsKey(DEPENDENT_VARIABLE_MIN)) {
					template.setDependentVariableMin(settings.getDouble(DEPENDENT_VARIABLE_MIN));
				}
				if (settings.containsKey(DEPENDENT_VARIABLE_MAX)) {
					template.setDependentVariableMax(settings.getDouble(DEPENDENT_VARIABLE_MAX));
				}
				if (settings.containsKey(INDEPENDENT_VARIABLES)) {
					template.setIndependentVariables(settings.getStringArray(INDEPENDENT_VARIABLES));
				}
				if (settings.containsKey(INDEPENDENT_VARIABLES_UNITS)) {
					template.setIndependentVariables(settings.getStringArray(INDEPENDENT_VARIABLES_UNITS));
				}
				if (settings.containsKey(INDEPENDENT_VARIABLES_MINS)) {
					template.setIndependentVariablesMins(
							Arrays.stream(settings.getDoubleArray(INDEPENDENT_VARIABLES_MINS)).boxed()
									.toArray(Double[]::new));
				}
				if (settings.containsKey(INDEPENDENT_VARIABLES_MAXS)) {
					template.setIndependentVariablesMaxs(
							Arrays.stream(settings.getDoubleArray(INDEPENDENT_VARIABLES_MAXS)).boxed()
									.toArray(Double[]::new));
				}
				if (settings.containsKey(HAS_DATA)) {
					template.setHasData(settings.getBoolean(HAS_DATA));
				}
			} catch (InvalidSettingsException e) {
				// should not occur after containsKey check
				throw new RuntimeException(e);
			}

			return template;
		}
	}
}
