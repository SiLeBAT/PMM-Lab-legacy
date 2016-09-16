package de.bund.bfr.knime.pmm.fskx;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.openfsmr.FSMRTemplate;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

public class FskTemplateSettings {

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
	private static final String INDEPENDENT_VARIABLES_MINS = "Independent variables minimum values";
	private static final String INDEPENDENT_VARIABLES_MAXS = "Independent variables maximum values";
	private static final String INDEPENDENT_VARIABLES_VALUES = "Independent variables values";
	private static final String HAS_DATA = "Has data?";

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");

	private FskMetaData template = new FskMetaData();

	public FskMetaData getTemplate() {
		return template;
	}

	public void setTemplate(FskMetaData template) {
		this.template = template;
	}

	/**
	 * Loads {@link FSMRTemplate} from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 */
	public void loadFromNodeSettings(final NodeSettingsRO settings) {

		try {
			template.modelName = settings.getString(MODEL_NAME, null);
			template.modelId = settings.getString(MODEL_ID, null);

			String modelLink = settings.getString(MODEL_LINK, null);
			if (modelLink != null)
				template.modelLink = new URL(modelLink);

			template.organism = settings.getString(ORGANISM_NAME, null);
			template.organismDetails = settings.getString(ORGANISM_DETAILS, null);
			template.matrix = settings.getString(MATRIX_NAME, null);
			template.matrixDetails = settings.getString(MATRIX_DETAILS, null);
			template.creator = settings.getString(CREATOR, null);
			template.familyName = settings.getString(FAMILY_NAME, null);
			template.contact = settings.getString(CONTACT, null);
			template.referenceDescription = settings.getString(REFERENCE_DESCRIPTION, null);

			String referenceDescriptionLink = settings.getString(REFERENCE_DESCRIPTION_LINK, null);
			if (referenceDescriptionLink != null)
				template.referenceDescriptionLink = new URL(referenceDescriptionLink);

			String createdDate = settings.getString(CREATED_DATE, null);
			if (createdDate != null)
				template.createdDate = dateFormat.parse(createdDate);

			String modifiedDate = settings.getString(MODIFIED_DATE, null);
			if (modifiedDate != null)
				template.modifiedDate = dateFormat.parse(modifiedDate);

			template.rights = settings.getString(RIGHTS, null);
			template.notes = settings.getString(NOTES, null);
			template.curated = settings.getBoolean(CURATION_STATUS, false);

			String modelType = settings.getString(MODEL_TYPE, null);
			if (modelType != null) {
				template.type = ModelType.valueOf(modelType);
			}

			String modelSubject = settings.getString(MODEL_SUBJECT, null);
			if (modelSubject != null)
				template.subject = ModelClass.fromName(modelSubject);

			template.foodProcess = settings.getString(FOOD_PROCESS, null);
			template.dependentVariable = settings.getString(DEPENDENT_VARIABLE, null);
			template.dependentVariableUnit = settings.getString(DEPENDENT_VARIABLE_UNIT, null);
			template.dependentVariableMin = settings.getDouble(DEPENDENT_VARIABLE_MIN, Double.NaN);
			template.dependentVariableMax = settings.getDouble(DEPENDENT_VARIABLE_MAX, Double.NaN);

			String[] independentVariables = settings.getStringArray(INDEPENDENT_VARIABLES, (String[]) null);
			if (independentVariables != null)
				template.independentVariables = Arrays.asList(independentVariables);

			double[] independentVariablesMins = settings.getDoubleArray(INDEPENDENT_VARIABLES_MINS, (double[]) null);
			if (independentVariablesMins != null)
				template.independentVariableMins = Arrays.stream(independentVariablesMins).boxed()
						.collect(Collectors.toList());

			double[] independentVariablesMaxs = settings.getDoubleArray(INDEPENDENT_VARIABLES_MAXS, (double[]) null);
			if (independentVariablesMaxs != null)
				template.independentVariableMaxs = Arrays.stream(independentVariablesMaxs).boxed()
						.collect(Collectors.toList());

			double[] indepvarValues = settings.getDoubleArray(INDEPENDENT_VARIABLES_VALUES, (double[]) null);
			if (indepvarValues != null)
				template.independentVariableValues = Arrays.stream(indepvarValues).boxed().collect(Collectors.toList());

			template.hasData = settings.getBoolean(HAS_DATA, false);
		} catch (MalformedURLException e) {
			// does not happen -> internal links are checked before
			// being saved
			throw new RuntimeException(e);
		} catch (ParseException e) {
			// does not happen -> internal dates are checked before
			// being saved
			throw new RuntimeException(e);
		}
	}

	/**
	 * Saves {@link FSMRTemplate} into a {@link NodeSettingsWO}.
	 * <p>
	 * Missing single doubles are replaced with {@link Double#NaN}.
	 * 
	 * @param settings
	 */
	public void saveToNodeSettings(final NodeSettingsWO settings) {
		settings.addString(MODEL_NAME, template.modelName);
		settings.addString(MODEL_ID, template.modelId);
		settings.addString(MODEL_LINK, template.modelLink == null ? "" : template.modelLink.toString());
		settings.addString(ORGANISM_NAME, template.organism);
		settings.addString(ORGANISM_DETAILS, template.organismDetails);
		settings.addString(MATRIX_NAME, template.matrix);
		settings.addString(MATRIX_DETAILS, template.matrixDetails);
		settings.addString(CREATOR, template.creator);
		settings.addString(FAMILY_NAME, template.familyName);
		settings.addString(CONTACT, template.contact);
		settings.addString(REFERENCE_DESCRIPTION, template.referenceDescription);
		settings.addString(REFERENCE_DESCRIPTION_LINK, template.referenceDescriptionLink == null ? "" : template.referenceDescriptionLink.toString());
		settings.addString(CREATED_DATE, template.createdDate == null ? "" : template.createdDate.toString());
		settings.addString(MODIFIED_DATE, template.modifiedDate == null ? "" : template.modifiedDate.toString());
		settings.addString(RIGHTS, template.rights);
		settings.addString(NOTES, template.notes);
		settings.addBoolean(CURATION_STATUS, template.curated);
		settings.addString(MODEL_TYPE, template.type == null ? "" : template.type.name());
		settings.addString(MODEL_SUBJECT, template.subject == null ? "" : template.subject.fullName());
		settings.addString(FOOD_PROCESS, template.foodProcess);
		settings.addString(DEPENDENT_VARIABLE, template.dependentVariable);
		settings.addString(DEPENDENT_VARIABLE_UNIT, template.dependentVariableUnit);
		settings.addDouble(DEPENDENT_VARIABLE_MIN, template.dependentVariableMin);
		settings.addDouble(DEPENDENT_VARIABLE_MAX, template.dependentVariableMax);

		if (template.independentVariables != null && !template.independentVariables.isEmpty()) {
			settings.addStringArray(INDEPENDENT_VARIABLES, template.independentVariables.toArray(new String[template.independentVariables.size()]));
		}
		
		if (template.independentVariableUnits != null && !template.independentVariableUnits.isEmpty()) {
			settings.addStringArray(INDEPENDENT_VARIABLES_UNITS, template.independentVariableUnits.toArray(new String[template.independentVariableUnits.size()]));
		}
		
		if (template.independentVariableMins != null && !template.independentVariableMins.isEmpty()) {
			settings.addDoubleArray(INDEPENDENT_VARIABLES_MINS, template.independentVariableMins.stream().mapToDouble(Double::doubleValue).toArray());
		}

		if (template.independentVariableMaxs != null && !template.independentVariableMaxs.isEmpty()) {
			settings.addDoubleArray(INDEPENDENT_VARIABLES_MAXS, template.independentVariableMaxs.stream().mapToDouble(Double::doubleValue).toArray());
		}

		if (template.independentVariableValues != null && !template.independentVariableValues.isEmpty()) {
			settings.addDoubleArray(INDEPENDENT_VARIABLES_VALUES, template.independentVariableValues.stream().mapToDouble(Double::doubleValue).toArray());
		}
		
		settings.addBoolean(HAS_DATA, template.hasData);
	}
}
