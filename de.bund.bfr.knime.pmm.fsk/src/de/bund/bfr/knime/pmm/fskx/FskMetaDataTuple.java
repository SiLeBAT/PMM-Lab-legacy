package de.bund.bfr.knime.pmm.fskx;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.stream.Collectors;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.StringCell;

import de.bund.bfr.pmfml.ModelClass;

public class FskMetaDataTuple implements DataRow {

	public enum Key {
		name,
		id,
		model_link,
		species,
		species_details,
		matrix,
		matrix_details,
		creator,
		family_name,
		contact,
		reference_description,
		reference_description_link,
		created_date,
		modified_date,
		rights,
		notes,
		curation_status,
		model_type,
		subject,
		food_process,
		depvar,
		depvar_unit,
		depvar_min,
		depvar_max,
		indepvars,
		indepvars_units,
		indepvars_mins,
		indepvars_maxs,
		indepvars_values,
		has_data
	}

	private DataCell[] cell;
	private final RowKey rowKey;

	public FskMetaDataTuple(final FskMetaData template) {
		cell = new DataCell[Key.values().length];

		cell[Key.name.ordinal()] = new StringCell("");

		String name = template.isSetModelName() ? template.getModelName() : "";
		cell[Key.name.ordinal()] = new StringCell(name);

		String id = template.isSetModelId() ? template.getModelId() : "";
		cell[Key.id.ordinal()] = new StringCell(id);

		String link = template.isSetModelLink() ? template.getModelLink().toString() : "";
		cell[Key.model_link.ordinal()] = new StringCell(link);

		String organism = template.isSetOrganism() ? template.getOrganism() : "";
		cell[Key.species.ordinal()] = new StringCell(organism);

		String organismDetails = template.isSetOrganismDetails() ? template.getOrganismDetails() : "";
		cell[Key.species_details.ordinal()] = new StringCell(organismDetails);

		String matrix = template.isSetMatrix() ? template.getMatrix() : "";
		cell[Key.matrix.ordinal()] = new StringCell(matrix);

		String matrixDetails = template.isSetMatrixDetails() ? template.getMatrixDetails() : "";
		cell[Key.matrix_details.ordinal()] = new StringCell(matrixDetails);

		String creator = template.isSetCreator() ? template.getCreator() : "";
		cell[Key.creator.ordinal()] = new StringCell(creator);

		String family = template.isSetFamilyName() ? template.getFamilyName() : "";
		cell[Key.family_name.ordinal()] = new StringCell(family);

		String contact = template.isSetContact() ? template.getContact() : "";
		cell[Key.contact.ordinal()] = new StringCell(contact);

		String referenceDescription = template.isSetReferenceDescription() ? template.getReferenceDescription() : "";
		cell[Key.reference_description.ordinal()] = new StringCell(referenceDescription);

		String referenceDescriptionLink = template.isSetReferenceDescriptionLink()
				? template.getReferenceDescriptionLink().toString() : "";
		cell[Key.reference_description_link.ordinal()] = new StringCell(referenceDescriptionLink);

		String createdDate = template.isSetCreatedDate() ? template.getCreatedDate().toString() : "";
		cell[Key.created_date.ordinal()] = new StringCell(createdDate);

		String modifiedDate = template.isSetModifiedDate() ? template.getModifiedDate().toString() : "";
		cell[Key.modified_date.ordinal()] = new StringCell(modifiedDate);

		String rights = template.isSetRights() ? template.getRights() : "";
		cell[Key.rights.ordinal()] = new StringCell(rights);

		String notes = template.isSetNotes() ? template.getNotes() : "";
		cell[Key.notes.ordinal()] = new StringCell(notes);

		cell[Key.curation_status.ordinal()] = new StringCell(Boolean.toString(template.isCurated()));

		String modelType = template.isSetModelType() ? template.getModelType().toString() : "";
		cell[Key.model_type.ordinal()] = new StringCell(modelType);

		String subject = template.isSetModelSubject() ? template.getModelSubject().fullName()
				: ModelClass.UNKNOWN.fullName();
		cell[Key.subject.ordinal()] = new StringCell(subject);

		String foodProcess = template.isSetFoodProcess() ? template.getFoodProcess() : "";
		cell[Key.food_process.ordinal()] = new StringCell(foodProcess);

		String depvar = template.isSetDependentVariable() ? template.getDependentVariable() : "";
		cell[Key.depvar.ordinal()] = new StringCell(depvar);

		String depvarUnit = template.isSetDependentVariableUnit() ? template.getDependentVariableUnit() : "";
		cell[Key.depvar_unit.ordinal()] = new StringCell(depvarUnit);

		String depvarMin = template.isSetDependentVariableMin() ? Double.toString(template.getDependentVariableMin())
				: "";
		cell[Key.depvar_min.ordinal()] = new StringCell(depvarMin);

		String depvarMax = template.isSetDependentVariableMax() ? Double.toString(template.getDependentVariableMax())
				: "";
		cell[Key.depvar_max.ordinal()] = new StringCell(depvarMax);

		String indepvars = template.isSetIndependentVariables()
				? template.getIndependentVariables().stream().collect(Collectors.joining("||")) : "";
		cell[Key.indepvars.ordinal()] = new StringCell(indepvars);

		String indepvarUnits = template.isSetIndependentVariableUnits()
				? template.getIndependentVariableUnits().stream().collect(Collectors.joining("||")) : "";
		cell[Key.indepvars_units.ordinal()] = new StringCell(indepvarUnits);

		String indepvarMins = template.isSetIndependentVariableMins() ? template.getIndependentVariableMins().stream()
				.map(min -> min.toString()).collect(Collectors.joining("||")) : "";
		cell[Key.indepvars_mins.ordinal()] = new StringCell(indepvarMins);

		String indepvarMaxs = template.isSetIndependentVariableMaxs() ? template.getIndependentVariableMaxs().stream()
				.map(max -> max.toString()).collect(Collectors.joining("||")) : "";
		cell[Key.indepvars_maxs.ordinal()] = new StringCell(indepvarMaxs);

		String indepvarValues = template.isSetIndependentVariableValues() ? template.getIndependentVariableValues()
				.stream().map(val -> val.toString()).collect(Collectors.joining("||")) : "";
		cell[Key.indepvars_values.ordinal()] = new StringCell(indepvarValues);

		cell[Key.has_data.ordinal()] = new StringCell(Boolean.toString(template.hasData()));

		rowKey = new RowKey(String.valueOf(new Random().nextInt()));
	}

	// --- DataRow methods ---

	@Override
	public int getNumCells() {
		return cell.length;
	}

	@Override
	public RowKey getKey() {
		return rowKey;
	}

	@Override
	public DataCell getCell(final int index) {
		return cell[index];
	}

	@Override
	public Iterator<DataCell> iterator() {
		return new MetaDataTupleIterator(cell);
	}

	class MetaDataTupleIterator implements Iterator<DataCell> {

		private int i;
		private DataCell[] cell;

		public MetaDataTupleIterator(final DataCell[] cell) {
			i = 0;
			this.cell = cell;
		}

		@Override
		public boolean hasNext() {
			return i < cell.length;
		}

		@Override
		public DataCell next() {
			return cell[i++];
		}
	}
	
	// other utility methods
	public static DataTableSpec createSpec() {

		int numKeys = Key.values().length;

		String[] names = new String[numKeys];
		names[Key.name.ordinal()] = "Model name";
		names[Key.id.ordinal()] = "Model id";
		names[Key.model_link.ordinal()] = "Model link";
		names[Key.species.ordinal()] = "Organism";
		names[Key.species_details.ordinal()] = "Organism details";
		names[Key.matrix.ordinal()] = "Environment";
		names[Key.matrix_details.ordinal()] = "Environment details";
		names[Key.creator.ordinal()] = "Model creator";
		names[Key.family_name.ordinal()] = "Model family name";
		names[Key.contact.ordinal()] = "Model contact";
		names[Key.reference_description.ordinal()] = "Model reference description";
		names[Key.reference_description_link.ordinal()] = "Model reference description link";
		names[Key.created_date.ordinal()] = "Created date";
		names[Key.modified_date.ordinal()] = "Modified date";
		names[Key.rights.ordinal()] = "Rights";
		names[Key.notes.ordinal()] = "Notes";
		names[Key.curation_status.ordinal()] = "Curation status";
		names[Key.model_type.ordinal()] = "Model type";
		names[Key.subject.ordinal()] = "Subject";
		names[Key.food_process.ordinal()] = "Food process";
		names[Key.depvar.ordinal()] = "Dependent variable";
		names[Key.depvar_unit.ordinal()] = "Dependent variable unit";
		names[Key.depvar_min.ordinal()] = "Dependent variable min";
		names[Key.depvar_max.ordinal()] = "Dependent variable max";
		names[Key.indepvars.ordinal()] = "Independent variables";
		names[Key.indepvars_units.ordinal()] = "Independent variable units";
		names[Key.indepvars_mins.ordinal()] = "Independent variable mins";
		names[Key.indepvars_maxs.ordinal()] = "Independent variable maxs";
		names[Key.indepvars_values.ordinal()] = "Independent variable values";
		names[Key.has_data.ordinal()] = "Has data?";

		DataType[] types = new DataType[numKeys];
		Arrays.fill(types, StringCell.TYPE);
		
		return new DataTableSpec(DataTableSpec.createColumnSpecs(names, types));
	}
}