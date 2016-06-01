package de.bund.bfr.knime.pmm.fskx.editor;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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

	public FskxEditorNodeSettings() {
		template = null;
	}

	public FskxEditorNodeSettings(FSMRTemplate template) {
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
		private static final String MODEL_NAME = "Model name";
		private static final String MODEL_ID = "Model id";
		private static final String MODEL_LINK = "Model link";
		private static final String ORGANISM_NAME = "Organism name";
		private static final String ORGANISM_DETAILS = "Organism details";
		private static final String MATRIX_NAME = "Matrix name";
		private static final String MATRIX_DETAILS = "Matrix details";
		private static final String CREATOR = "Creator";
		private static final String FAMILY_NAME = "Family name";
		private static final String CONTACT = "Contact";
		private static final String REFERENCE_DESCRIPTION = "Reference description";
		private static final String REFERENCE_DESCRIPTION_LINK = "Reference description link";
		private static final String CREATED_DATE = "Created date";
		private static final String MODIFIED_DATE = "Modified date";
		private static final String RIGHTS = "Rights";
		private static final String NOTES = "Notes";
		private static final String CURATION_STATUS = "Curation status";
		private static final String MODEL_TYPE = "Model type";
		private static final String MODEL_SUBJECT = "Model subject";
		private static final String FOOD_PROCESS = "Food process";
		private static final String DEPENDENT_VARIABLE = "Dependent variable";
		private static final String DEPENDENT_VARIABLE_UNIT = "Dependent variable unit";
		private static final String DEPENDENT_VARIABLE_MIN = "Dependent variable minimum value";
		private static final String DEPENDENT_VARIABLE_MAX = "Dependent variable maximum value";
		private static final String INDEPENDENT_VARIABLES = "Independent variables";
		private static final String INDEPENDENT_VARIABLES_UNITS = "Independent variables units";
		private static final String INDEPENDENT_VARIABLES_MINS = "Independent variables minimums values";
		private static final String INDEPENDENT_VARIABLES_MAXS = "Independent variables maximum values";
		private static final String HAS_DATA = "Has data?";

		/**
		 * Saves {@link FSMRTemplate} to settings object.
		 * 
		 * @param settings
		 *            To save to.
		 */
		public static void saveSettings(final FSMRTemplate template, final ConfigWO settings) {
			settings.addString(MODEL_NAME, template.isSetModelName() ? template.getModelName() : null);
			settings.addString(MODEL_ID, template.isSetModelId() ? template.getModelId() : null);
			settings.addString(MODEL_LINK, template.isSetModelLink() ? template.getModelLink().toString() : null);
			settings.addString(ORGANISM_NAME, template.isSetOrganismName() ? template.getOrganismName() : null);
			settings.addString(ORGANISM_DETAILS,
					template.isSetOrganismDetails() ? template.getOrganismDetails() : null);
			settings.addString(MATRIX_NAME, template.isSetMatrixName() ? template.getMatrixName() : null);
			settings.addString(MATRIX_DETAILS, template.isSetMatrixDetails() ? template.getMatrixDetails() : null);
			settings.addString(CREATOR, template.isSetCreator() ? template.getCreator() : null);
			settings.addString(FAMILY_NAME, template.isSetFamilyName() ? template.getFamilyName() : null);
			settings.addString(CONTACT, template.isSetContact() ? template.getContact() : null);
			settings.addString(REFERENCE_DESCRIPTION,
					template.isSetReferenceDescription() ? template.getReferenceDescription() : null);
			settings.addString(REFERENCE_DESCRIPTION_LINK, template.isSetReferenceDescriptionLink()
					? template.getReferenceDescriptionLink().toString() : null);
			settings.addString(CREATED_DATE,
					template.isSetCreatedDate() ? dateFormat.format(template.getCreatedDate()) : null);
			settings.addString(MODIFIED_DATE,
					template.isSetModifiedDate() ? dateFormat.format(template.getModifiedDate()) : null);
			settings.addString(RIGHTS, template.isSetRights() ? template.getRights() : null);
			settings.addString(NOTES, template.isSetNotes() ? template.getNotes() : null);
			settings.addString(CURATION_STATUS, template.isSetCurationStatus() ? template.getCurationStatus() : null);
			settings.addString(MODEL_TYPE, template.isSetModelType() ? template.getModelType().name() : null);
			settings.addString(MODEL_SUBJECT,
					template.isSetModelSubject() ? template.getModelSubject().fullName() : null);
			settings.addString(FOOD_PROCESS, template.isSetFoodProcess() ? template.getFoodProcess() : null);
			settings.addString(DEPENDENT_VARIABLE,
					template.isSetDependentVariable() ? template.getDependentVariable() : null);
			settings.addString(DEPENDENT_VARIABLE_UNIT, template.isSetDependentVariableUnit() ? template.getDependentVariableUnit() : null);
			settings.addDouble(DEPENDENT_VARIABLE_MIN,
					template.isSetDependentVariableMin() ? template.getDependentVariableMin() : null);
			settings.addDouble(DEPENDENT_VARIABLE_MAX,
					template.isSetDependentVariableMax() ? template.getDependentVariableMax() : null);
			settings.addStringArray(INDEPENDENT_VARIABLES,
					template.isSetIndependentVariables() ? template.getIndependentVariables() : null);
			settings.addStringArray(INDEPENDENT_VARIABLES_UNITS,
					template.isSetIndependentVariablesUnits() ? template.getIndependentVariablesUnits() : null);
			settings.addDoubleArray(INDEPENDENT_VARIABLES_MINS,
					template.isSetIndependentVariablesMins() ? template.getIndependentVariablesMins() : null);
			settings.addDoubleArray(INDEPENDENT_VARIABLES_MAXS,
					template.isSetIndependentVariablesMaxs() ? template.getIndependentVariablesMaxs() : null);
			settings.addBoolean(HAS_DATA, template.isSetHasData() ? template.getHasData() : null);
		}

		/**
		 * Loads scripts in NodeModel.
		 * 
		 * @param settings
		 *            To load from
		 */
		public static FSMRTemplate loadSettings(final ConfigRO settings) throws InvalidSettingsException {
			FSMRTemplate template = new FSMRTemplateImpl();

			String modelName = settings.getString(MODEL_NAME);
			if (modelName != null) {
				template.setModelName(modelName);
			}

			String modelId = settings.getString(MODEL_ID);
			if (modelId != null) {
				template.setModelId(modelId);
			}

			String modelLink = settings.getString(MODEL_LINK);
			if (modelLink != null) {
				try {
					template.setModelLink(new URL(modelLink));
				} catch (MalformedURLException e) {
					// does not happen -> internal links are checked before
					// being saved
					throw new RuntimeException(e);
				}
			}

			String organismName = settings.getString(ORGANISM_NAME);
			if (organismName != null) {
				template.setOrganismName(organismName);
			}

			String organismDetails = settings.getString(ORGANISM_DETAILS);
			if (organismDetails != null) {
				template.setOrganismDetails(organismDetails);
			}

			String matrixName = settings.getString(MATRIX_NAME);
			if (matrixName != null) {
				template.setMatrixName(matrixName);
			}
			
			String matrixDetails = settings.getString(MATRIX_DETAILS);
			if (matrixDetails != null) {
				template.setMatrixDetails(matrixDetails);
			}

			String creator = settings.getString(CREATOR);
			if (creator != null) {
				template.setCreator(creator);
			}
			
			String familyName = settings.getString(FAMILY_NAME);
			if (familyName != null) {
				template.setFamilyName(familyName);
			}

			String contact = settings.getString(CONTACT);
			if (contact != null) {
				template.setContact(contact);
			}
			
			String referenceDescription = settings.getString(REFERENCE_DESCRIPTION);
			if (referenceDescription != null) {
				template.setReferenceDescription(referenceDescription);
			}
			
			String referenceDescriptionLink = settings.getString(REFERENCE_DESCRIPTION_LINK);
			if (referenceDescriptionLink != null) {
				try {
					template.setReferenceDescriptionLink(new URL(referenceDescriptionLink));
				} catch (MalformedURLException e) {
					// does not happen -> internal links are checked before
					// being saved
					throw new RuntimeException(e);
				}
			}
			
			String createdDate = settings.getString(CREATED_DATE);
			if (createdDate != null) {
				try {
					template.setCreatedDate(dateFormat.parse(createdDate));
				} catch (ParseException e) {
					// does not happen -> internal dates are checked before
					// being saved
					throw new RuntimeException(e);
				}
			}
			
			String modifiedDate = settings.getString(MODIFIED_DATE);
			if (modifiedDate != null) {
				try {
					template.setModifiedDate(dateFormat.parse(modifiedDate));
				} catch (ParseException e) {
					// does not happen -> internal dates are checked before
					// being saved
					throw new RuntimeException(e);
				}
			}

			String rights = settings.getString(RIGHTS);
			if (rights != null) {
				template.setRights(rights);
			}
			
			String notes = settings.getString(NOTES);
			if (notes != null) {
				template.setNotes(notes);
			}
			
			String curationStatus = settings.getString(CURATION_STATUS);
			if (curationStatus != null) {
				template.setCurationStatus(curationStatus);
			}
			
			String modelType = settings.getString(MODEL_TYPE);
			if (modelType != null) {
				template.setModelType(ModelType.valueOf(modelType));
			}
			
			String modelSubject = settings.getString(MODEL_SUBJECT);
			if (modelSubject != null) {
				template.setModelSubject(ModelClass.fromName(modelSubject));
			}
			
			String foodProcess = settings.getString(FOOD_PROCESS);
			if (foodProcess != null) {
				template.setFoodProcess(foodProcess);
			}
			
			String dependentVariable = settings.getString(DEPENDENT_VARIABLE);
			if (dependentVariable != null) {
				template.setDependentVariable(dependentVariable);
			}
			
			String dependentVariableUnit = settings.getString(DEPENDENT_VARIABLE_UNIT);
			if (dependentVariableUnit != null) {
				template.setDependentVariableUnit(dependentVariableUnit);
			}
			
			Double dependentVariableMin = settings.getDouble(DEPENDENT_VARIABLE_MIN);
			if (dependentVariableMin != null) {
				template.setDependentVariableMin(dependentVariableMin);
			}

			Double dependentVariableMax = settings.getDouble(DEPENDENT_VARIABLE_MAX);
			if (dependentVariableMax != null) {
				template.setDependentVariableMax(dependentVariableMax);
			}

			String[] independentVariables = settings.getStringArray(INDEPENDENT_VARIABLES);
			if (independentVariables != null) {
				template.setIndependentVariables(independentVariables);
			}

			String[] independentVariablesUnits = settings.getStringArray(INDEPENDENT_VARIABLES_UNITS);
			if (independentVariablesUnits != null) {
				template.setIndependentVariables(independentVariablesUnits);
			}
			
			double[] independentVariablesMins = settings.getDoubleArray(INDEPENDENT_VARIABLES_MINS);
			if (independentVariablesMins != null) {
				template.setIndependentVariablesMins(independentVariablesMins);
			}
			
			double[] independentVariablesMaxs = settings.getDoubleArray(INDEPENDENT_VARIABLES_MAXS);
			if (independentVariablesMaxs != null) {
				template.setIndependentVariablesMaxs(independentVariablesMaxs);
			}

			Boolean hasData = settings.getBoolean(HAS_DATA);
			if (hasData != null) {
				template.setHasData(hasData);
			}
			return template;
		}
	}
}
