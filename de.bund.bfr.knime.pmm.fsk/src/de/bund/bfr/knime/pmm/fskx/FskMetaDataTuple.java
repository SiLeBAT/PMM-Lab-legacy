package de.bund.bfr.knime.pmm.fskx;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.StringCell;

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

	public FskMetaDataTuple() {
		cell = new DataCell[Key.values().length];

		cell[Key.name.ordinal()] = new StringCell("");
		cell[Key.id.ordinal()] = new StringCell("");
		cell[Key.model_link.ordinal()] = new StringCell("");
		cell[Key.species.ordinal()] = new StringCell("");
		cell[Key.species_details.ordinal()] = new StringCell("");
		cell[Key.matrix.ordinal()] = new StringCell("");
		cell[Key.matrix_details.ordinal()] = new StringCell("");
		cell[Key.creator.ordinal()] = new StringCell("");
		cell[Key.family_name.ordinal()] = new StringCell("");
		cell[Key.contact.ordinal()] = new StringCell("");
		cell[Key.reference_description.ordinal()] = new StringCell("");
		cell[Key.reference_description_link.ordinal()] = new StringCell("");
		cell[Key.created_date.ordinal()] = new StringCell("");
		cell[Key.modified_date.ordinal()] = new StringCell("");
		cell[Key.rights.ordinal()] = new StringCell("");
		cell[Key.notes.ordinal()] = new StringCell("");
		cell[Key.curation_status.ordinal()] = new StringCell("");
		cell[Key.model_type.ordinal()] = new StringCell("");
		cell[Key.subject.ordinal()] = new StringCell("");
		cell[Key.food_process.ordinal()] = new StringCell("");
		cell[Key.depvar.ordinal()] = new StringCell("");
		cell[Key.depvar_unit.ordinal()] = new StringCell("");
		cell[Key.depvar_min.ordinal()] = new StringCell("");
		cell[Key.depvar_max.ordinal()] = new StringCell("");
		cell[Key.indepvars.ordinal()] = new StringCell("");
		cell[Key.indepvars_units.ordinal()] = new StringCell("");
		cell[Key.indepvars_mins.ordinal()] = new StringCell("");
		cell[Key.indepvars_maxs.ordinal()] = new StringCell("");
		cell[Key.indepvars_values.ordinal()] = new StringCell("");
		cell[Key.has_data.ordinal()] = new StringCell("");

		rowKey = new RowKey(String.valueOf(new Random().nextInt()));
	}

	public FskMetaDataTuple(final DataRow row) {
		cell = new DataCell[Key.values().length];
		for (Key key : Key.values()) {
			cell[key.ordinal()] = row.getCell(key.ordinal());
		}
		rowKey = new RowKey(String.valueOf(new Random().nextInt()));
	}

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy",
			Locale.ENGLISH);

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

	// --- utility ---

	// * model name

	/**
	 * @return the model name
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getModelName() {
		String value = ((StringCell) cell[Key.name.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Model name not set");
		return value;
	}

	/**
	 * @return whether the model name is set
	 */
	public boolean isSetModelName() {
		String value = ((StringCell) cell[Key.name.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param modelName
	 *            Null or empty strings are ignored
	 */
	public void setModelName(final String name) {
		if (name != null && !name.isEmpty())
			cell[Key.name.ordinal()] = new StringCell(name);
	}

	/** Unsets the model name. */
	public void unsetModelName() {
		cell[Key.name.ordinal()] = new StringCell("");
	}

	// * model id

	/**
	 * @return the model identifier
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getModelId() {
		String value = ((StringCell) cell[Key.id.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Model id not set");
		return value;
	}

	/**
	 * @return whether the model identifier is set
	 */
	public boolean isSetModelId() {
		String value = ((StringCell) cell[Key.id.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param modelId
	 *            Null or empty strings are ignored
	 */
	public void setModelId(final String id) {
		if (id != null && !id.isEmpty())
			cell[Key.id.ordinal()] = new StringCell(id);
	}

	/** Unsets the model id. */
	public void unsetModelId() {
		cell[Key.id.ordinal()] = new StringCell("");
	}

	// * model link

	/**
	 * @return the model link
	 * @throws RuntimeException
	 *             if not set
	 */
	public URL getModelLink() {
		try {
			return new URL(((StringCell) cell[Key.model_link.ordinal()]).getStringValue());
		} catch (MalformedURLException error) {
			/*
			 * error occurs when the model link is not set (empty string). When
			 * the model link is set it always keeps a valid url.
			 */
			throw new RuntimeException("Model link is not set");
		}
	}

	/**
	 * @return whether the model link is set
	 */
	public boolean isSetModelLink() {
		String value = ((StringCell) cell[Key.model_link.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param modelLink
	 *            Null links are ignored
	 */
	public void setModelLink(final URL link) {
		if (link != null)
			cell[Key.model_link.ordinal()] = new StringCell(link.toString());
	}

	/** Unsets the model link. */
	public void unsetModelLink() {
		cell[Key.model_link.ordinal()] = new StringCell("");
	}

	// * organism name

	/**
	 * @return the organism name
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getOrganismName() {
		String value = ((StringCell) cell[Key.species.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Organism name is not set");
		return value;
	}

	/**
	 * @return whether the organism name is set
	 */
	public boolean isSetOrganismName() {
		String value = ((StringCell) cell[Key.species.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param organismName
	 *            Null and empty strings are ignored
	 */
	public void setOrganismName(final String name) {
		if (name != null && !name.isEmpty())
			cell[Key.species.ordinal()] = new StringCell(name);
	}

	/** Unsets the organism name */
	public void unsetOrganismName() {
		cell[Key.species.ordinal()] = new StringCell("");
	}

	// * organism details

	/**
	 * @return the organism details
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getOrganismDetails() {
		String value = ((StringCell) cell[Key.species_details.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Organism details are not set");
		return value;
	}

	/**
	 * @return whether the organism details are set
	 */
	public boolean isSetOrganismDetails() {
		String value = ((StringCell) cell[Key.species_details.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param organismDetails
	 *            Null and empty strings are ignored
	 */
	public void setOrganismDetails(final String details) {
		if (details != null && !details.isEmpty())
			cell[Key.species_details.ordinal()] = new StringCell(details);
	}

	/** Unsets the organism details */
	public void unsetOrganismDetails() {
		cell[Key.species_details.ordinal()] = new StringCell("");
	}

	// * matrix name

	/**
	 * @return the matrix name
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getMatrixName() {
		String value = ((StringCell) cell[Key.matrix.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Matrix name is not set");
		return value;
	}

	/**
	 * @return whether the matrix name is set
	 */
	public boolean isSetMatrixName() {
		String value = ((StringCell) cell[Key.matrix.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param matrixName
	 *            Null and empty strings are ignored
	 */
	public void setMatrixName(final String name) {
		if (name != null && !name.isEmpty())
			cell[Key.matrix.ordinal()] = new StringCell(name);
	}

	/** Unsets the matrix name. */
	public void unsetMatrixName() {
		cell[Key.matrix.ordinal()] = new StringCell("");
	}

	// * matrix details

	/**
	 * @return the matrix details
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getMatrixDetails() {
		String value = ((StringCell) cell[Key.matrix_details.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Matrix details are not set");
		return value;
	}

	/**
	 * @return whether the matrix details are set
	 */
	public boolean isSetMatrixDetails() {
		String value = ((StringCell) cell[Key.matrix_details.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param details
	 *            Null and empty strings are ignored
	 */
	public void setMatrixDetails(final String details) {
		if (details != null && !details.isEmpty())
			cell[Key.matrix_details.ordinal()] = new StringCell(details);
	}

	/** Unsets the matrix details. */
	public void unsetMatrixDetails() {
		cell[Key.matrix_details.ordinal()] = new StringCell("");
	}

	// * creator
	/**
	 * @return the creator
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getCreator() {
		String value = ((StringCell) cell[Key.creator.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Creator is not set");
		return value;
	}

	/**
	 * @return whether the creator is set
	 */
	public boolean isSetCreator() {
		String value = ((StringCell) cell[Key.creator.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param creator
	 *            Null and empty strings are ignored.
	 */
	public void setCreator(final String creator) {
		if (creator != null && !creator.isEmpty())
			cell[Key.creator.ordinal()] = new StringCell(creator);
	}

	/** Unsets the creator. */
	public void unsetCreator() {
		cell[Key.creator.ordinal()] = new StringCell("");
	}

	// * family name

	/**
	 * @return the family name
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getFamilyName() {
		String value = ((StringCell) cell[Key.family_name.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Family name is not set");
		return value;
	}

	/**
	 * @return whether the family name is set
	 */
	public boolean isSetFamilyName() {
		String value = ((StringCell) cell[Key.family_name.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param familyName
	 *            Null or empty strings are ignored.
	 */
	public void setFamilyName(final String name) {
		if (name != null && !name.isEmpty())
			cell[Key.family_name.ordinal()] = new StringCell(name);
	}

	/** Unsets the family name. */
	public void unsetFamilyName() {
		cell[Key.family_name.ordinal()] = new StringCell("");
	}

	// * contact
	/**
	 * @return the contact
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getContact() {
		String value = ((StringCell) cell[Key.contact.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Contact is not set");
		return value;
	}

	/**
	 * @return whether the contact is set.
	 */
	public boolean isSetContact() {
		String value = ((StringCell) cell[Key.contact.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param contact
	 *            Null or empty strings are ignored.
	 */
	public void setContact(final String contact) {
		if (contact != null && !contact.isEmpty())
			cell[Key.contact.ordinal()] = new StringCell(contact);
	}

	/** Unsets the contact. */
	public void unsetContact() {
		cell[Key.contact.ordinal()] = new StringCell("");
	}

	// * reference description

	/**
	 * @return the reference description
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getReferenceDescription() {
		String value = ((StringCell) cell[Key.reference_description.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Reference description");
		return value;
	}

	/**
	 * @return whether the reference description is set
	 */
	public boolean isSetReferenceDescription() {
		String value = ((StringCell) cell[Key.reference_description_link.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param description
	 *            Null and empty strings are ignored
	 */
	public void setReferenceDescription(final String description) {
		if (description != null && !description.isEmpty())
			cell[Key.reference_description.ordinal()] = new StringCell(description);
	}

	/** Unsets the description. */
	public void unsetReferenceDescription() {
		cell[Key.reference_description.ordinal()] = new StringCell("");
	}

	// * reference description link

	/**
	 * @return the link of the reference description
	 * @throw RuntimeException if not set
	 */
	public URL getReferenceDescriptionLink() {
		try {
			return new URL(((StringCell) cell[Key.reference_description_link.ordinal()]).getStringValue());
		} catch (MalformedURLException e) {
			// MalformedURLException is only thrown when the
			// reference_description_link is empty (not set)
			throw new RuntimeException("Reference description link is not set");
		}
	}

	/**
	 * @return whether the link of the reference description is set
	 */
	public boolean isSetReferenceDescriptionLink() {
		String value = ((StringCell) cell[Key.reference_description_link.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param link
	 *            Link of the reference description. Null links are ignored.
	 */
	public void setReferenceDescriptionLink(final URL link) {
		if (link != null)
			cell[Key.reference_description_link.ordinal()] = new StringCell(link.toString());
	}

	/** Unsets the reference description link. */
	public void unsetReferenceDescriptionLink() {
		cell[Key.reference_description_link.ordinal()] = new StringCell("");
	}

	// * created date
	/**
	 * @return the creation date
	 * @throws RuntimeException
	 *             if not set
	 */
	public Date getCreatedDate() {
		String value = ((StringCell) cell[Key.created_date.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Creation date is not set");
		try {
			return dateFormat.parse(value);
		} catch (ParseException e) {
			// The dates stored are always checked so this exception should not
			// occur
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
	}

	/**
	 * @return whether the creation date is set
	 */
	public boolean isSetCreatedDate() {
		String value = ((StringCell) cell[Key.created_date.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param date
	 *            Null and empty strings are ignored
	 */
	public void setCreatedDate(final String date) {
		if (date != null && !date.isEmpty())
			cell[Key.created_date.ordinal()] = new StringCell(date);
	}

	/** Unsets the creation date. */
	public void unsetCreatedDate() {
		cell[Key.created_date.ordinal()] = new StringCell("");
	}

	// * modified date
	/**
	 * @return the last modification date
	 * @throws RuntimeException
	 *             if not set
	 */
	public Date getModifiedDate() {
		String value = ((StringCell) cell[Key.modified_date.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Last modification date is not set");
		try {
			return dateFormat.parse(value);
		} catch (ParseException error) {
			// The dates stored are always checked so this exception should not
			// occur
			throw new RuntimeException(error.getMessage(), error.getCause());
		}
	}

	/**
	 * @return whether the last modification date is set
	 */
	public boolean isSetModifiedDate() {
		String value = ((StringCell) cell[Key.modified_date.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param date
	 *            Null and empty strings are ignored
	 */
	public void setModifiedDate(final String date) {
		if (date != null && !date.isEmpty())
			cell[Key.modified_date.ordinal()] = new StringCell(date);
	}

	/** Unsets the last modification date. */
	public void unsetModifiedDate() {
		cell[Key.modified_date.ordinal()] = new StringCell("");
	}

	// * rights

	/**
	 * @return the rights of the model
	 * @throws RuntimeException
	 *             if the rights of the model
	 */
	public String getRights() {
		String value = ((StringCell) cell[Key.rights.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Rights are not set");
		return value;
	}

	/**
	 * @return whether the rights are set
	 */
	public boolean isSetRights() {
		String value = ((StringCell) cell[Key.rights.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param rigths
	 *            Null and empty strings are ignored
	 */
	public void setRights(final String rights) {
		if (rights != null && !rights.isEmpty())
			cell[Key.rights.ordinal()] = new StringCell(rights);
	}

	/** Unsets the rights. */
	public void unsetRights() {
		cell[Key.rights.ordinal()] = new StringCell("");
	}

	// * notes

	/**
	 * @return the model notes
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getNotes() {
		String value = ((StringCell) cell[Key.notes.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Model notes are not set");
		return value;
	}

	/**
	 * @return whether the model notes are set
	 */
	public boolean isSetNotes() {
		String value = ((StringCell) cell[Key.notes.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param notes
	 *            Null or empty notes are ignored
	 */
	public void setNotes(final String notes) {
		if (notes != null && !notes.isEmpty())
			cell[Key.notes.ordinal()] = new StringCell(notes);
	}

	/** Unsets the model notes. */
	public void unsetModelNotes() {
		cell[Key.notes.ordinal()] = new StringCell("");
	}

	// * curation status

	/**
	 * @return the curation status
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getCurationStatus() {
		String value = ((StringCell) cell[Key.curation_status.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Curation status is not set");
		return value;
	}

	/**
	 * @return whether the curation status
	 */
	public boolean isSetCurationStatus() {
		String value = ((StringCell) cell[Key.curation_status.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param curationStatus
	 *            Null and empty strings are ignored
	 */
	public void setCurationStatus(final String status) {
		if (status != null && !status.isEmpty())
			cell[Key.curation_status.ordinal()] = new StringCell(status);
	}

	/** Unsets the curation status. */
	public void unsetCurationStatus() {
		cell[Key.curation_status.ordinal()] = new StringCell("");
	}

	// * model type
	/**
	 * @return the model type
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getModelType() {
		String value = ((StringCell) cell[Key.model_type.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Model type is not set");
		return value;
	}

	/**
	 * @return whether the model type is set
	 */
	public boolean isSetModelType() {
		String value = ((StringCell) cell[Key.model_type.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param modelType
	 *            Null or empty strings are ignored
	 */
	public void setModelType(final String type) {
		if (type != null && !type.isEmpty())
			cell[Key.model_type.ordinal()] = new StringCell(type);
	}

	/** Unset the model type. */
	public void unsetModelType() {
		cell[Key.model_type.ordinal()] = new StringCell("");
	}

	// * model subject

	/**
	 * @return the model subject
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getModelSubject() {
		String value = ((StringCell) cell[Key.subject.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Model subject not set");
		return value;
	}

	/**
	 * @return whether the model subject is set
	 */
	public boolean isSetModelSubject() {
		String value = ((StringCell) cell[Key.subject.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param modelSubject
	 *            Null and empty strings are ignored
	 */
	public void setModelSubject(final String subject) {
		if (subject != null && !subject.isEmpty())
			cell[Key.subject.ordinal()] = new StringCell(subject);
	}

	/** Unsets the model subject. */
	public void unsetModelSubject() {
		cell[Key.subject.ordinal()] = new StringCell("");
	}

	// * food process
	/**
	 * @return the food process
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getFoodProcess() {
		String value = ((StringCell) cell[Key.food_process.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Food process not set");
		return value;
	}

	/**
	 * @return whether the food process is set
	 */
	public boolean isSetFoodProcess() {
		String value = ((StringCell) cell[Key.food_process.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param process
	 *            Null and empty strings are ignored
	 */
	public void setFoodProcess(final String process) {
		cell[Key.food_process.ordinal()] = new StringCell(process);
	}

	/** Unsets the food process. */
	public void unsetFoodProcess() {
		cell[Key.food_process.ordinal()] = new StringCell("");
	}

	// * dependent variable
	/**
	 * @return the dependent variable
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getDependentVariable() {
		String value = ((StringCell) cell[Key.depvar.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Dependent variable is not set");
		return value;
	}

	/**
	 * @return whether the dependent variable is set
	 */
	public boolean isSetDependentVariable() {
		String value = ((StringCell) cell[Key.depvar.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param var
	 *            Null or empty strings are ignored
	 */
	public void setDependentVariable(final String var) {
		if (var != null && !var.isEmpty())
			cell[Key.depvar.ordinal()] = new StringCell(var);
	}

	/** Unsets the dependent variable. */
	public void unsetDependentVariable() {
		cell[Key.depvar.ordinal()] = new StringCell("");
	}

	// * dependent variable unit

	/**
	 * @return the unit of the dependent variable
	 * @throws RuntimeException
	 *             if not set
	 */
	public String getDependentVariableUnit() {
		String value = ((StringCell) cell[Key.depvar_unit.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Unit of the dependent variable is not set");
		return value;
	}

	/**
	 * @return the unit of the dependent variable
	 */
	public boolean isSetDependentVariableUnit() {
		String value = ((StringCell) cell[Key.depvar_unit.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param unit
	 *            Null and empty strings are ignored
	 */
	public void setDependentVariableUnit(final String unit) {
		if (unit != null && !unit.isEmpty())
			cell[Key.depvar_unit.ordinal()] = new StringCell(unit);
	}

	/** Unsets the unit of the dependent variable. */
	public void unsetDependentVariableUnit() {
		cell[Key.depvar_unit.ordinal()] = new StringCell("");
	}

	// * dependent variable min

	/**
	 * @return the minimum value of the dependent variable
	 * @throws RuntimeException
	 *             if not set
	 */
	public double getDependentVariableMin() {
		String value = ((StringCell) cell[Key.depvar_min.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("Minimum value of the dependent variable is not set");
		return Double.parseDouble(value);
	}

	/**
	 * @return whether the minimum value of the dependent variable is set
	 */
	public boolean isSetDependentVariableMin() {
		String value = ((StringCell) cell[Key.depvar_unit.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param min
	 *            Double.NaN is ignored
	 */
	public void setDependentVariableMin(final double min) {
		if (!Double.isNaN(min))
			cell[Key.depvar_min.ordinal()] = new StringCell(Double.toString(min));
	}

	/** Unsets the minimum value of the dependent variable. */
	public void unsetDependentVariableMin() {
		cell[Key.depvar_min.ordinal()] = new StringCell("");
	}

	// * dependent variable max

	/**
	 * @return the maximum value of the dependent variable
	 * @throws RuntimeException
	 *             if not set
	 */
	public double getDependentVariableMax() {
		String value = ((StringCell) cell[Key.depvar_max.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("the maximum value of the dependent variable is not set");
		return Double.parseDouble(value);
	}

	/**
	 * @return whether the maximum value of the dependent variable is not set
	 */
	public boolean isSetDependentVariableMax() {
		String value = ((StringCell) cell[Key.depvar_max.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param max
	 *            Double.NaN is ignored
	 */
	public void setDependentVariableMax(final double max) {
		if (!Double.isNaN(max))
			cell[Key.depvar_max.ordinal()] = new StringCell(Double.toString(max));
	}

	/** Unsets the maximum value of the dependent variable. */
	public void unsetDependentVariableMax() {
		cell[Key.depvar_max.ordinal()] = new StringCell("");
	}

	// * independent variables

	/**
	 * @return the independent variables
	 * @throw RuntimeException if not set
	 */
	public List<String> getIndependentVariables() {
		String formattedVars = ((StringCell) cell[Key.indepvars.ordinal()]).getStringValue();
		if (formattedVars.isEmpty())
			throw new RuntimeException("Independent variables are not set");
		return Arrays.asList(formattedVars.split("\\|\\|"));
	}

	/**
	 * @return whether the independent variables are set
	 */
	public boolean isSetIndependentVariables() {
		String formattedVars = ((StringCell) cell[Key.indepvars.ordinal()]).getStringValue();
		return !formattedVars.isEmpty();
	}

	/**
	 * @vars Null or empty arrays are ignored
	 */
	public void setIndependentVariables(final List<String> vars) {
		if (vars != null && !vars.isEmpty()) {
			String formattedVars = vars.stream().collect(Collectors.joining("||"));
			cell[Key.indepvars.ordinal()] = new StringCell(formattedVars);
		}
	}

	/** Unsets the independent variables. */
	public void unsetIndependentVariables() {
		cell[Key.indepvars.ordinal()] = new StringCell("");
	}

	// * independent variable units

	/**
	 * @return the units of the independent variables
	 * @throw RuntimeException if not set
	 */
	public List<String> getIndependentVariablesUnits() {
		String formattedUnits = ((StringCell) cell[Key.indepvars_units.ordinal()]).getStringValue();
		if (formattedUnits.isEmpty())
			throw new RuntimeException("Units of the independent variables are not set");
		return Arrays.asList(formattedUnits.split("\\|\\|"));
	}

	/**
	 * @return whether the units of the independent varaibles are set
	 */
	public boolean isSetIndependentVariablesUnits() {
		String value = ((StringCell) cell[Key.indepvars_units.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param units
	 *            Null and empty arrays are ignored
	 */
	public void setIndependentVariablesUnits(final List<String> units) {
		String formattedVars = units.stream().collect(Collectors.joining("||"));
		cell[Key.indepvars_units.ordinal()] = new StringCell(formattedVars);
	}

	/** Unsets the units of the independent variables. */
	public void unsetIndependentVariablesUnits() {
		cell[Key.indepvars_units.ordinal()] = new StringCell("");
	}

	// * independent variable mins

	/**
	 * @return the minimum values of the independent variables
	 * @throws RuntimeException
	 *             if not set
	 */
	public List<Double> getIndependentVariablesMins() {
		String formattedMins = ((StringCell) cell[Key.indepvars_mins.ordinal()]).getStringValue();
		if (formattedMins.isEmpty())
			throw new RuntimeException("Minimum values of the independent variables are not set");
		return Arrays.stream(formattedMins.split("\\|\\|")).map(Double::valueOf).collect(Collectors.toList());
	}

	/**
	 * @return whether the minimum values of the independent variables are set
	 */
	public boolean isSetIndependentVariablesMins() {
		String value = ((StringCell) cell[Key.indepvars_mins.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param mins
	 *            Null or empty arrays are ignored
	 */
	public void setIndependentVariablesMins(final List<Double> mins) {
		if (mins != null && !mins.isEmpty()) {
			String formattedMins = mins.stream().map(d -> Double.toString(d)).collect(Collectors.joining("||"));
			cell[Key.indepvars_mins.ordinal()] = new StringCell(formattedMins);
		}
	}

	/** Unsets minimum values of the independent variables. */
	public void unsetIndependentVariablesMins() {
		cell[Key.indepvars_mins.ordinal()] = new StringCell("");
	}

	// * independent variables maxs

	/**
	 * @return the maximum values of the independent variables
	 * @throws RuntimeException
	 *             if not set
	 */
	public List<Double> getIndependentVariablesMaxs() {
		String formattedMaxs = ((StringCell) cell[Key.indepvars_maxs.ordinal()]).getStringValue();
		if (formattedMaxs.isEmpty())
			throw new RuntimeException("Maximum values of the independent variables are not set");
		return Arrays.stream(formattedMaxs.split("\\|\\|")).map(Double::valueOf).collect(Collectors.toList());
	}

	/**
	 * @return whether the maximum values of the independent variables are set
	 */
	public boolean isSetIndependentVariablesMaxs() {
		String value = ((StringCell) cell[Key.indepvars_maxs.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param maxs
	 *            Null and empty arrays are ignored
	 */
	public void setIndependentVariablesMaxs(final List<Double> maxs) {
		String formattedMaxs = maxs.stream().map(d -> Double.toString(d)).collect(Collectors.joining("||"));
		cell[Key.indepvars_maxs.ordinal()] = new StringCell(formattedMaxs);
	}

	/** Unsets the maximum values of the independent variables. */
	public void unsetIndependentVariablesMaxs() {
		cell[Key.indepvars_maxs.ordinal()] = new StringCell("");
	}

	// * independent variables values

	/**
	 * @return the values of the independent variables
	 * @throws RuntimeException
	 *             if not set
	 */
	public List<Double> getIndependentVariablesValues() {
		String formattedValues = ((StringCell) cell[Key.indepvars_values.ordinal()]).getStringValue();
		if (formattedValues.isEmpty())
			throw new RuntimeException("Values of the independent variables are not set");
		return Arrays.stream(formattedValues.split("\\|\\|")).map(Double::valueOf).collect(Collectors.toList());
	}

	/**
	 * @return whether the values of the independent varaibles are set
	 */
	public boolean isSetIndependentVariablesValues() {
		String value = ((StringCell) cell[Key.indepvars_values.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	/**
	 * @param values
	 *            Null or empty string are ignored
	 */
	public void setIndependentVariablesValues(final String values) {
		if (values != null && !values.isEmpty())
			cell[Key.indepvars_values.ordinal()] = new StringCell(values);
	}

	/** Unsets the values of the independent variables. */
	public void unsetIndependentVariablesValues() {
		cell[Key.indepvars_values.ordinal()] = new StringCell("");
	}

	// * has data
	/**
	 * @return hasData condition
	 */
	public String getHasData() {
		String value = ((StringCell) cell[Key.has_data.ordinal()]).getStringValue();
		if (value.isEmpty())
			throw new RuntimeException("HasDate is not set");
		return value;
	}

	public boolean isSetHasData() {
		String value = ((StringCell) cell[Key.has_data.ordinal()]).getStringValue();
		return !value.isEmpty();
	}

	public void setHasData(final String hasData) {
		if (hasData != null && !hasData.isEmpty())
			cell[Key.has_data.ordinal()] = new StringCell(hasData);
	}

	public void unsetHasData() {
		cell[Key.has_data.ordinal()] = new StringCell("");
	}

	// Other utility methods
	public static DataTableSpec createMetaDataTableSpec() {
		int numKeys = Key.values().length;

		String[] names = new String[numKeys];
		DataType[] types = new DataType[numKeys];

		names[Key.name.ordinal()] = "Model name";
		types[Key.name.ordinal()] = StringCell.TYPE;

		names[Key.id.ordinal()] = "Model id";
		types[Key.id.ordinal()] = StringCell.TYPE;

		names[Key.model_link.ordinal()] = "Model link";
		types[Key.model_link.ordinal()] = StringCell.TYPE;

		names[Key.species.ordinal()] = "Organism";
		types[Key.species.ordinal()] = StringCell.TYPE;

		names[Key.species_details.ordinal()] = "Organism details";
		types[Key.species_details.ordinal()] = StringCell.TYPE;

		names[Key.matrix.ordinal()] = "Environment";
		types[Key.matrix.ordinal()] = StringCell.TYPE;

		names[Key.matrix_details.ordinal()] = "Environment details";
		types[Key.matrix_details.ordinal()] = StringCell.TYPE;

		names[Key.creator.ordinal()] = "Model creator";
		types[Key.creator.ordinal()] = StringCell.TYPE;

		names[Key.family_name.ordinal()] = "Model family name";
		types[Key.family_name.ordinal()] = StringCell.TYPE;

		names[Key.contact.ordinal()] = "Model contact";
		types[Key.contact.ordinal()] = StringCell.TYPE;

		names[Key.reference_description.ordinal()] = "Model reference description";
		types[Key.reference_description.ordinal()] = StringCell.TYPE;

		names[Key.reference_description_link.ordinal()] = "Model reference description link";
		types[Key.reference_description_link.ordinal()] = StringCell.TYPE;

		names[Key.created_date.ordinal()] = "Created date";
		types[Key.created_date.ordinal()] = StringCell.TYPE;

		names[Key.modified_date.ordinal()] = "Modified date";
		types[Key.modified_date.ordinal()] = StringCell.TYPE;

		names[Key.rights.ordinal()] = "Rights";
		types[Key.rights.ordinal()] = StringCell.TYPE;

		names[Key.notes.ordinal()] = "Notes";
		types[Key.notes.ordinal()] = StringCell.TYPE;

		names[Key.curation_status.ordinal()] = "Curation status";
		types[Key.curation_status.ordinal()] = StringCell.TYPE;

		names[Key.model_type.ordinal()] = "Model type";
		types[Key.model_type.ordinal()] = StringCell.TYPE;

		names[Key.subject.ordinal()] = "Subject";
		types[Key.subject.ordinal()] = StringCell.TYPE;

		names[Key.food_process.ordinal()] = "Food process";
		types[Key.food_process.ordinal()] = StringCell.TYPE;

		names[Key.depvar.ordinal()] = "Dependent variable";
		types[Key.depvar.ordinal()] = StringCell.TYPE;

		names[Key.depvar_unit.ordinal()] = "Dependent variable unit";
		types[Key.depvar_unit.ordinal()] = StringCell.TYPE;

		names[Key.depvar_min.ordinal()] = "Dependent variable min";
		types[Key.depvar_min.ordinal()] = StringCell.TYPE;

		names[Key.depvar_max.ordinal()] = "Dependent variable max";
		types[Key.depvar_max.ordinal()] = StringCell.TYPE;

		names[Key.indepvars.ordinal()] = "Independent variables";
		types[Key.indepvars.ordinal()] = StringCell.TYPE;

		names[Key.indepvars_units.ordinal()] = "Independent variable units";
		types[Key.indepvars_units.ordinal()] = StringCell.TYPE;

		names[Key.indepvars_mins.ordinal()] = "Independent variable mins";
		types[Key.indepvars_mins.ordinal()] = StringCell.TYPE;

		names[Key.indepvars_maxs.ordinal()] = "Independent variable maxs";
		types[Key.indepvars_maxs.ordinal()] = StringCell.TYPE;

		names[Key.indepvars_values.ordinal()] = "Independent variable values";
		types[Key.indepvars_values.ordinal()] = StringCell.TYPE;

		names[Key.has_data.ordinal()] = "Has data?";
		types[Key.has_data.ordinal()] = StringCell.TYPE;

		return new DataTableSpec(DataTableSpec.createColumnSpecs(names, types));
	}
}