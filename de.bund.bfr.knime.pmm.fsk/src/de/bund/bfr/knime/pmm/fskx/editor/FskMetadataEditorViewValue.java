package de.bund.bfr.knime.pmm.fskx.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import de.bund.bfr.knime.pmm.fskx.FskMetaData.DataType;
import de.bund.bfr.knime.pmm.fskx.FskMetaDataTuple.Key;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class FskMetadataEditorViewValue extends JSONViewContent {

	String modelName;
	String modelId;
	String modelLink;
	String organism;
	String organismDetails;
	String matrix;
	String matrixDetails;
	String contact;
	String software;
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
	List<Variable> variables;
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
		settings.addString(Key.software.name(), software);
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

		Variable depVar = variables.stream().filter(v -> v.isDependent == true).findAny().get();
		List<Variable> indepVars = variables.stream().filter(v -> v.isDependent == false).collect(Collectors.toList());

		settings.addString(Key.depvar.name(), depVar.name);
		settings.addString(Key.depvar_unit.name(), depVar.unit);
		settings.addDouble(Key.depvar_min.name(), depVar.min);
		settings.addDouble(Key.depvar_max.name(), depVar.max);
		settings.addString(Key.depvar_type.name(), depVar.type.name());

		String[] indepVarNames = new String[indepVars.size()];
		String[] indepVarUnits = new String[indepVars.size()];
		String[] indepVarTypes = new String[indepVars.size()];
		double[] indepVarMins = new double[indepVars.size()];
		double[] indepVarMaxs = new double[indepVars.size()];
		double[] indepVarValues = new double[indepVars.size()];

		for (int i = 0; i < indepVars.size(); i++) {
			indepVarNames[i] = indepVars.get(i).name;
			indepVarUnits[i] = indepVars.get(i).unit;
			indepVarTypes[i] = indepVars.get(i).type.name();
			indepVarMins[i] = indepVars.get(i).min;
			indepVarMaxs[i] = indepVars.get(i).max;
			indepVarValues[i] = indepVars.get(i).value;
		}

		settings.addStringArray(Key.indepvars.name(), indepVarNames);
		settings.addStringArray(Key.indepvars_units.name(), indepVarUnits);
		settings.addStringArray(Key.indepvars_types.name(), indepVarTypes);
		settings.addDoubleArray(Key.indepvars_mins.name(), indepVarMins);
		settings.addDoubleArray(Key.indepvars_maxs.name(), indepVarMaxs);
		settings.addDoubleArray(Key.indepvars_values.name(), indepVarValues);

		settings.addBoolean(Key.has_data.name(), hasData);
	}

	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		modelName = settings.getString(Key.name.name(), "");
		modelId = settings.getString(Key.id.name(), "");
		modelLink = settings.getString(Key.model_link.name(), "");
		organism = settings.getString(Key.species.name(), "");
		organismDetails = settings.getString(Key.species_details.name(), "");
		matrix = settings.getString(Key.matrix.name(), "");
		matrixDetails = settings.getString(Key.matrix_details.name(), "");
		contact = settings.getString(Key.contact.name(), "");
		software = settings.getString(Key.software.name(), "");
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

		variables = new ArrayList<>();

		Variable depVar = new Variable();
		depVar.name = settings.getString(Key.depvar.name(), "");
		depVar.unit = settings.getString(Key.depvar_unit.name(), "");
		String typeAsString = settings.getString(Key.depvar_type.name(), "");
		depVar.type = typeAsString.isEmpty() ? null : DataType.valueOf(typeAsString);
		depVar.min = settings.getDouble(Key.depvar_min.name(), Double.NaN);
		depVar.max = settings.getDouble(Key.depvar_max.name(), Double.NaN);
		depVar.isDependent = true;
		variables.add(depVar);

		String[] indepVarNames = settings.getStringArray(Key.indepvars.name());
		String[] indepVarUnits = settings.getStringArray(Key.indepvars_units.name());
		String[] indepVarTypesString = settings.getStringArray(Key.indepvars_types.name());
		double[] indepVarMins = settings.getDoubleArray(Key.indepvars_mins.name());
		double[] indepVarMaxs = settings.getDoubleArray(Key.indepvars_maxs.name());
		double[] indepVarValues = settings.getDoubleArray(Key.indepvars_values.name());

		for (int i = 0; i < indepVarNames.length; i++) {
			Variable indepVar = new Variable();
			indepVar.name = indepVarNames[i];
			if (indepVarUnits != null)
				indepVar.unit = indepVarUnits[i];
			if (indepVarTypesString != null)
				indepVar.type = DataType.valueOf(indepVarTypesString[i]);
			if (indepVarMins != null)
				indepVar.min = indepVarMins[i];
			if (indepVarMaxs != null)
				indepVar.max = indepVarMaxs[i];
			if (indepVarValues != null)
				indepVar.value = indepVarValues[i];
			variables.add(indepVar);
		}

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
				&& Objects.equals(software, other.software)
				&& Objects.equals(referenceDescription, other.referenceDescription)
				&& Objects.equals(referenceDescriptionLink, other.referenceDescriptionLink)
				&& Objects.equals(createdDate, other.createdDate) && Objects.equals(modifiedDate, other.modifiedDate)
				&& Objects.equals(rights, other.rights) && Objects.equals(notes, other.notes)
				&& Objects.equals(curated, other.curated) && Objects.equals(modelType, other.modelType)
				&& Objects.equals(modelSubject, other.modelSubject) && Objects.equals(foodProcess, other.foodProcess)
				&& Objects.equals(variables, other.variables) && Objects.equals(hasData, other.hasData);
	}

	@Override
	public int hashCode() {
		return Objects.hash(modelName, modelId, modelLink, organism, organismDetails, matrix, matrixDetails, contact,
				software, referenceDescription, referenceDescriptionLink, createdDate, modifiedDate, rights, notes,
				curated, modelType, modelSubject, foodProcess, variables, hasData);
	}

	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	static class Variable {
		String name;
		String unit;
		double value;
		double min;
		double max;
		boolean isDependent;
		DataType type;

		@Override
		public String toString() {
			return "Variable [" + name + "]";
		}
	}
}
