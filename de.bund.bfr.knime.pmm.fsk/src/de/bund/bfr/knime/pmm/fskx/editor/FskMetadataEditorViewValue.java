package de.bund.bfr.knime.pmm.fskx.editor;

import java.util.Objects;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.bund.bfr.knime.pmm.fskx.FskMetaDataTuple.Key;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class FskMetadataEditorViewValue extends JSONViewContent {

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
		settings.addString(Key.name.name(), modelName);
		settings.addString(Key.id.name(), modelId);
		settings.addString(Key.model_link.name(), modelLink);
		settings.addString(Key.species.name(), organism);
		settings.addString(Key.species_details.name(), organismDetails);
		settings.addString(Key.matrix.name(), matrix);
		settings.addString(Key.matrix_details.name(), matrixDetails);
		settings.addString(Key.contact.name(), contact);
		settings.addString(Key.reference_description.name(), referenceDescription);
		settings.addString(Key.reference_description_link.name(), referenceDescriptionLink);
		settings.addString(Key.created_date.name(), createdDate);
		settings.addString(Key.modified_date.name(), modifiedDate);
		settings.addString(Key.rights.name(), rights);
		settings.addString(Key.notes.name(), notes);
		settings.addBoolean(Key.curation_status.name(), curated);
		settings.addString(Key.model_type.name(), modelType);
		settings.addString(Key.subject.name(), modelSubject);
		settings.addString(Key.food_process.name(), foodProcess);

		settings.addString(Key.depvar.name(), dependentVariable);
		settings.addString(Key.depvar_unit.name(), dependentVariableUnit);
		settings.addDouble(Key.depvar_min.name(), dependentVariableMin);
		settings.addDouble(Key.depvar_max.name(), dependentVariableMax);

		settings.addStringArray(Key.indepvars.name(), independentVariables);
		settings.addStringArray(Key.indepvars_units.name(), independentVariableUnits);
		settings.addDoubleArray(Key.indepvars_mins.name(), independentVariableMins);
		settings.addDoubleArray(Key.indepvars_maxs.name(), independentVariableMaxs);
		settings.addDoubleArray(Key.indepvars_values.name(), independentVariableValues);

		settings.addBoolean(Key.has_data.name(), hasData);
	}

	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) {
		modelName = settings.getString(Key.name.name(), "");
		modelId = settings.getString(Key.id.name(), "");
		modelLink = settings.getString(Key.model_link.name(), "");
		organism = settings.getString(Key.species.name(), "");
		organismDetails = settings.getString(Key.species_details.name(), "");
		matrix = settings.getString(Key.matrix.name(), "");
		matrixDetails = settings.getString(Key.matrix_details.name(), "");
		contact = settings.getString(Key.contact.name(), "");
		referenceDescription = settings.getString(Key.reference_description.name(), "");
		referenceDescriptionLink = settings.getString(Key.reference_description_link.name(), "");
		createdDate = settings.getString(Key.created_date.name(), "");
		modifiedDate = settings.getString(Key.modified_date.name(), "");
		rights = settings.getString(Key.rights.name(), "");
		notes = settings.getString(Key.notes.name(), "");
		curated = settings.getBoolean(Key.curation_status.name(), false);
		modelType = settings.getString(Key.model_type.name(), "");
		modelSubject = settings.getString(Key.subject.name(), "");
		foodProcess = settings.getString(Key.food_process.name(), "");

		dependentVariable = settings.getString(Key.depvar.name(), "");
		dependentVariableUnit = settings.getString(Key.depvar_unit.name(), "");
		dependentVariableMin = settings.getDouble(Key.depvar_min.name(), Double.NaN);
		dependentVariableMax = settings.getDouble(Key.depvar_max.name(), Double.NaN);

		independentVariables = settings.getStringArray(Key.indepvars.name(), "");
		independentVariableUnits = settings.getStringArray(Key.indepvars_units.name(), "");
		independentVariableMins = settings.getDoubleArray(Key.indepvars_mins.name(), null);
		independentVariableMaxs = settings.getDoubleArray(Key.indepvars_maxs.name(), null);
		independentVariableValues = settings.getDoubleArray(Key.indepvars_values.name(), null);

		hasData = settings.getBoolean(Key.has_data.name(), false);
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
