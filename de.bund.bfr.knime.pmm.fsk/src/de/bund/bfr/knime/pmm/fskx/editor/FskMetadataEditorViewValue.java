package de.bund.bfr.knime.pmm.fskx.editor;

import java.util.Objects;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class FskMetadataEditorViewValue extends JSONViewContent {

	private enum Key {
		model_name,
		model_id,
		model_link,
		organism,
		organism_details,
		matrix,
		matrix_details,
		contact,
		reference_desc,
		reference_desc_link,
		created_date,
		modified_date,
		rights,
		notes,
		curated,
		model_type,
		model_subject,
		food_process,
		dep_var,
		dep_var_unit,
		dep_var_min,
		dep_var_max,
		indep_vars,
		indep_var_units,
		indep_var_mins,
		indep_var_maxs,
		indep_var_values,
		has_data
	}

	String modelName;
	String modelId;
	String modelLink;
	String organism;
	String organismDetails;
	String matrix;
	String matrixDetails;
	String contact;
	String referenceDescription;
	String referenceDescriptionLink;
	String createdDate;
	String modifiedDate;
	String rights;
	String notes;
	boolean curated;
	String modelType;
	String modelSubject;
	String foodProcess;

	String dependentVariable;
	String dependentVariableUnit;
	double dependentVariableMin = Double.NaN;
	double dependentVariableMax = Double.NaN;

	String[] independentVariables;
	String[] independentVariableUnits;
	double[] independentVariableMins;
	double[] independentVariableMaxs;
	double[] independentVariableValues;

	boolean hasData;

	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		settings.addString(Key.model_name.name(), modelName);
		settings.addString(Key.model_id.name(), modelId);
		settings.addString(Key.model_link.name(), modelLink);
		settings.addString(Key.organism.name(), organism);
		settings.addString(Key.organism_details.name(), organismDetails);
		settings.addString(Key.matrix.name(), matrix);
		settings.addString(Key.matrix_details.name(), matrixDetails);
		settings.addString(Key.contact.name(), contact);
		settings.addString(Key.reference_desc.name(), referenceDescription);
		settings.addString(Key.reference_desc_link.name(), referenceDescriptionLink);
		settings.addString(Key.created_date.name(), createdDate);
		settings.addString(Key.modified_date.name(), modifiedDate);
		settings.addString(Key.rights.name(), rights);
		settings.addString(Key.notes.name(), notes);
		settings.addBoolean(Key.curated.name(), curated);
		settings.addString(Key.model_type.name(), modelType);
		settings.addString(Key.model_subject.name(), modelSubject);
		settings.addString(Key.food_process.name(), foodProcess);

		settings.addString(Key.dep_var.name(), dependentVariable);
		settings.addString(Key.dep_var_unit.name(), dependentVariableUnit);
		settings.addDouble(Key.dep_var_min.name(), dependentVariableMin);
		settings.addDouble(Key.dep_var_max.name(), dependentVariableMax);

		settings.addStringArray(Key.indep_vars.name(), independentVariables);
		settings.addStringArray(Key.indep_var_units.name(), independentVariableUnits);
		settings.addDoubleArray(Key.indep_var_mins.name(), independentVariableMins);
		settings.addDoubleArray(Key.indep_var_maxs.name(), independentVariableMaxs);
		settings.addDoubleArray(Key.indep_var_values.name(), independentVariableValues);

		settings.addBoolean(Key.has_data.name(), hasData);
	}

	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		modelName = settings.getString(Key.model_name.name());
		modelId = settings.getString(Key.model_id.name());
		modelLink = settings.getString(Key.model_link.name());
		organism = settings.getString(Key.organism.name());
		organismDetails = settings.getString(Key.organism_details.name());
		matrix = settings.getString(Key.matrix.name());
		matrixDetails = settings.getString(Key.matrix_details.name());
		contact = settings.getString(Key.contact.name());
		referenceDescription = settings.getString(Key.reference_desc.name());
		referenceDescriptionLink = settings.getString(Key.reference_desc_link.name());
		createdDate = settings.getString(Key.created_date.name());
		modifiedDate = settings.getString(Key.modified_date.name());
		rights = settings.getString(Key.rights.name());
		notes = settings.getString(Key.notes.name());
		curated = settings.getBoolean(Key.curated.name());
		modelType = settings.getString(Key.model_type.name());
		modelSubject = settings.getString(Key.model_subject.name());
		foodProcess = settings.getString(Key.food_process.name());

		dependentVariable = settings.getString(Key.dep_var.name());
		dependentVariableUnit = settings.getString(Key.dep_var_unit.name());
		dependentVariableMin = settings.getDouble(Key.dep_var_min.name());
		dependentVariableMax = settings.getDouble(Key.dep_var_max.name());

		independentVariables = settings.getStringArray(Key.indep_vars.name());
		independentVariableUnits = settings.getStringArray(Key.indep_var_units.name());
		independentVariableMins = settings.getDoubleArray(Key.indep_var_mins.name());
		independentVariableMaxs = settings.getDoubleArray(Key.indep_var_maxs.name());
		independentVariableValues = settings.getDoubleArray(Key.indep_var_values.name());

		hasData = settings.getBoolean(Key.has_data.name());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FskMetadataEditorViewValue other = (FskMetadataEditorViewValue) obj;

		return Objects.equals(modelName, other.modelName) && Objects.equals(modelId, other.modelId)
				&& Objects.equals(modelLink, other.modelLink) && Objects.equals(organism, other.organism)
				&& Objects.equals(organismDetails, other.organismDetails) && Objects.equals(matrix, other.matrix)
				&& Objects.equals(matrixDetails, other.matrixDetails) && Objects.equals(contact, other.contact)
				&& Objects.equals(referenceDescription, other.referenceDescription)
				&& Objects.equals(referenceDescriptionLink, other.referenceDescriptionLink)
				&& Objects.equals(createdDate, other.createdDate) && Objects.equals(modifiedDate, other.modifiedDate)
				&& Objects.equals(rights, other.rights) && Objects.equals(notes, other.notes)
				&& Objects.equals(curated, other.curated) && Objects.equals(modelType, other.modelType)
				&& Objects.equals(modelSubject, other.modelSubject) && Objects.equals(foodProcess, other.foodProcess)
				&& Objects.equals(dependentVariable, other.dependentVariable)
				&& Objects.equals(dependentVariableUnit, other.dependentVariableUnit)
				&& Objects.equals(dependentVariableMin, other.dependentVariableMin)
				&& Objects.equals(dependentVariableMax, other.dependentVariableMax)
				&& Objects.deepEquals(independentVariables, other.independentVariables)
				&& Objects.deepEquals(independentVariableUnits, other.independentVariableUnits)
				&& Objects.deepEquals(independentVariableMins, other.independentVariableMins)
				&& Objects.deepEquals(independentVariableMaxs, other.independentVariableMaxs)
				&& Objects.deepEquals(independentVariableValues, other.independentVariableValues)
				&& Objects.equals(hasData, other.hasData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(modelName, modelId, modelLink, organism, organismDetails, matrix, matrixDetails, contact,
				referenceDescription, referenceDescriptionLink, createdDate, modifiedDate, rights, notes, curated,
				modelType, modelSubject, foodProcess, dependentVariable, dependentVariableUnit, dependentVariableMin,
				dependentVariableMax, independentVariables, independentVariableUnits, independentVariableMins,
				independentVariableMaxs, independentVariableValues, hasData);
	}
}
