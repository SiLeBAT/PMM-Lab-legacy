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

import com.google.common.base.Strings;

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

		cell[Key.name.ordinal()] = new StringCell(Strings.nullToEmpty(template.modelName));
		cell[Key.id.ordinal()] = new StringCell(Strings.nullToEmpty(template.modelId));
		cell[Key.model_link.ordinal()] = new StringCell(
				template.modelLink == null ? "" : template.modelLink.toString());

		cell[Key.species.ordinal()] = new StringCell(Strings.nullToEmpty(template.organism));
		cell[Key.species_details.ordinal()] = new StringCell(Strings.nullToEmpty(template.organismDetails));

		cell[Key.matrix.ordinal()] = new StringCell(Strings.nullToEmpty(template.matrix));
		cell[Key.matrix_details.ordinal()] = new StringCell(Strings.nullToEmpty(template.matrixDetails));

		cell[Key.creator.ordinal()] = new StringCell(Strings.nullToEmpty(template.creator));
		cell[Key.family_name.ordinal()] = new StringCell(Strings.nullToEmpty(template.creator));
		cell[Key.contact.ordinal()] = new StringCell(Strings.nullToEmpty(template.contact));
		cell[Key.reference_description.ordinal()] = new StringCell(Strings.nullToEmpty(template.referenceDescription));
		cell[Key.reference_description_link.ordinal()] = new StringCell(
				template.referenceDescriptionLink == null ? "" : template.referenceDescriptionLink.toString());
		cell[Key.created_date.ordinal()] = new StringCell(
				template.createdDate == null ? "" : FskMetaData.dateFormat.format(template.createdDate));
		cell[Key.modified_date.ordinal()] = new StringCell(
				template.modifiedDate == null ? "" : FskMetaData.dateFormat.format(template.modifiedDate));
		cell[Key.rights.ordinal()] = new StringCell(Strings.nullToEmpty(template.rights));
		cell[Key.notes.ordinal()] = new StringCell(Strings.nullToEmpty(template.notes));
		cell[Key.curation_status.ordinal()] = new StringCell(Boolean.toString(template.curated));
		cell[Key.model_type.ordinal()] = new StringCell(template.type == null ? "" : template.type.toString());
		cell[Key.subject.ordinal()] = new StringCell(
				template.subject == null ? ModelClass.UNKNOWN.fullName() : template.subject.fullName());
		cell[Key.food_process.ordinal()] = new StringCell(Strings.nullToEmpty(template.foodProcess));

		cell[Key.depvar.ordinal()] = new StringCell(Strings.nullToEmpty(template.dependentVariable));
		cell[Key.depvar_unit.ordinal()] = new StringCell(Strings.nullToEmpty(template.dependentVariableUnit));
		cell[Key.depvar_min.ordinal()] = new StringCell(
				Double.isNaN(template.dependentVariableMin) ? "" : Double.toString(template.dependentVariableMin));
		cell[Key.depvar_max.ordinal()] = new StringCell(
				Double.isNaN(template.dependentVariableMax) ? "" : Double.toString(template.dependentVariableMax));

		cell[Key.indepvars.ordinal()] = new StringCell(
				template.independentVariables == null || template.independentVariables.length == 0 ? ""
						: String.join("||", template.independentVariables));
		cell[Key.indepvars_units.ordinal()] = new StringCell(
				template.independentVariableUnits == null || template.independentVariableUnits.length == 0 ? ""
						: String.join("||", template.independentVariableUnits));
		cell[Key.indepvars_mins.ordinal()] = new StringCell(
				template.independentVariableMins == null || template.independentVariableMins.length == 0 ? ""
						: Arrays.stream(template.independentVariableMins).mapToObj(d -> Double.toString(d))
								.collect(Collectors.joining("||")));
		cell[Key.indepvars_maxs.ordinal()] = new StringCell(
				template.independentVariableMaxs == null || template.independentVariableMaxs.length == 0 ? ""
						: Arrays.stream(template.independentVariableMaxs).mapToObj(d -> Double.toString(d))
								.collect(Collectors.joining("||")));
		cell[Key.indepvars_values.ordinal()] = new StringCell(
				template.independentVariableValues == null || template.independentVariableValues.length == 0 ? ""
						: Arrays.stream(template.independentVariableValues).mapToObj(d -> Double.toString(d))
								.collect(Collectors.joining("||")));

		cell[Key.has_data.ordinal()] = new StringCell(Boolean.toString(template.hasData));

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
		return new MetaDataTupleIterator();
	}

	class MetaDataTupleIterator implements Iterator<DataCell> {

		private int i = 0;

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