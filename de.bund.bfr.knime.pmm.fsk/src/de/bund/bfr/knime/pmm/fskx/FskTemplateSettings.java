package de.bund.bfr.knime.pmm.fskx;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.openfsmr.FSMRTemplate;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

public class FskTemplateSettings {

	// configuration keys
	private static enum Key {
		modelName,
		modelId,
		modelLink,
		organismName,
		organismDetails,
		matrixName,
		matrixDetails,
		creator,
		familyName,
		contact,
		referenceDescription,
		referenceDescriptionLink,
		createdDate,
		modifiedDate,
		rights,
		notes,
		curationStatus,
		modelType,
		modelSubject,
		foodProcess,
		dependentVariable,
		dependentVariableUnit,
		dependentVariableMin,
		dependentVariableMax,
		independentVariables,
		independentVariableUnits,
		independentVariableMins,
		independentVariableMaxs,
		independentVariableValues,
		hasData
	}

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");

	public FskMetaData template = new FskMetaData();

	/**
	 * Loads {@link FSMRTemplate} from a {@link NodeSettingsRO}.
	 * 
	 * @param settings
	 */
	public void loadFromNodeSettings(final NodeSettingsRO settings) {

		try {
			template.modelName = settings.getString(Key.modelName.name(), null);
			template.modelId = settings.getString(Key.modelId.name(), null);

			String modelLink = settings.getString(Key.modelLink.name(), null);
			if (modelLink != null)
				template.modelLink = new URL(modelLink);

			template.organism = settings.getString(Key.organismName.name(), null);
			template.organismDetails = settings.getString(Key.organismDetails.name(), null);
			template.matrix = settings.getString(Key.matrixName.name(), null);
			template.matrixDetails = settings.getString(Key.matrixDetails.name(), null);
			template.creator = settings.getString(Key.creator.name(), null);
			template.familyName = settings.getString(Key.familyName.name(), null);
			template.contact = settings.getString(Key.contact.name(), null);
			template.referenceDescription = settings.getString(Key.referenceDescription.name(), null);

			String referenceDescriptionLink = settings.getString(Key.referenceDescriptionLink.name(), null);
			if (referenceDescriptionLink != null)
				template.referenceDescriptionLink = new URL(referenceDescriptionLink);

			String createdDate = settings.getString(Key.createdDate.name(), null);
			if (createdDate != null)
				template.createdDate = dateFormat.parse(createdDate);

			String modifiedDate = settings.getString(Key.modifiedDate.name(), null);
			if (modifiedDate != null)
				template.modifiedDate = dateFormat.parse(modifiedDate);

			template.rights = settings.getString(Key.rights.name(), null);
			template.notes = settings.getString(Key.notes.name(), null);
			template.curated = settings.getBoolean(Key.curationStatus.name(), false);

			String modelType = settings.getString(Key.modelType.name(), null);
			if (modelType != null) {
				template.type = ModelType.valueOf(modelType);
			}

			String modelSubject = settings.getString(Key.modelSubject.name(), null);
			if (modelSubject != null)
				template.subject = ModelClass.fromName(modelSubject);

			template.foodProcess = settings.getString(Key.foodProcess.name(), null);
			template.dependentVariable = settings.getString(Key.dependentVariable.name(), null);
			template.dependentVariableUnit = settings.getString(Key.dependentVariableUnit.name(), null);
			template.dependentVariableMin = settings.getDouble(Key.dependentVariableMin.name(), Double.NaN);
			template.dependentVariableMax = settings.getDouble(Key.dependentVariableMax.name(), Double.NaN);

			template.independentVariables = settings.getStringArray(Key.independentVariables.name(), (String[]) null);
			template.independentVariableUnits = settings.getStringArray(Key.independentVariableUnits.name(),
					(String[]) null);
			template.independentVariableMins = settings.getDoubleArray(Key.independentVariableMins.name(),
					(double[]) null);
			template.independentVariableMaxs = settings.getDoubleArray(Key.independentVariableMaxs.name(),
					(double[]) null);
			template.independentVariableValues = settings.getDoubleArray(Key.independentVariableValues.name(),
					(double[]) null);

			template.hasData = settings.getBoolean(Key.hasData.name(), false);
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
	 * Missing string values are replace with <code>null</code>
	 * </p>
	 * <p>
	 * Missing single doubles are replaced with {@link Double#NaN}.
	 * </p>
	 * 
	 * @param settings
	 */
	public void saveToNodeSettings(final NodeSettingsWO settings) {
		settings.addString(Key.modelName.name(), template.modelName);
		settings.addString(Key.modelId.name(), template.modelId);
		settings.addString(Key.modelLink.name(), template.modelLink == null ? null : template.modelLink.toString());
		settings.addString(Key.organismName.name(), template.organism);
		settings.addString(Key.organismDetails.name(), template.organismDetails);
		settings.addString(Key.matrixName.name(), template.matrix);
		settings.addString(Key.matrixDetails.name(), template.matrixDetails);
		settings.addString(Key.creator.name(), template.creator);
		settings.addString(Key.familyName.name(), template.familyName);
		settings.addString(Key.contact.name(), template.contact);
		settings.addString(Key.referenceDescription.name(), template.referenceDescription);
		settings.addString(Key.referenceDescriptionLink.name(),
				template.referenceDescriptionLink == null ? null : template.referenceDescriptionLink.toString());
		settings.addString(Key.createdDate.name(),
				template.createdDate == null ? null : FskMetaData.dateFormat.format(template.createdDate));
		settings.addString(Key.modifiedDate.name(),
				template.modifiedDate == null ? null : FskMetaData.dateFormat.format(template.modifiedDate));
		settings.addString(Key.rights.name(), template.rights);
		settings.addString(Key.notes.name(), template.notes);
		settings.addBoolean(Key.curationStatus.name(), template.curated);
		settings.addString(Key.modelType.name(), template.type == null ? null : template.type.name());
		settings.addString(Key.modelSubject.name(), template.subject == null ? "" : template.subject.fullName());
		settings.addString(Key.foodProcess.name(), template.foodProcess);

		settings.addString(Key.dependentVariable.name(), template.dependentVariable);
		settings.addString(Key.dependentVariableUnit.name(), template.dependentVariableUnit);
		settings.addDouble(Key.dependentVariableMin.name(), template.dependentVariableMin);
		settings.addDouble(Key.dependentVariableMax.name(), template.dependentVariableMax);

		settings.addStringArray(Key.independentVariables.name(), template.independentVariables);
		settings.addStringArray(Key.independentVariableUnits.name(), template.independentVariableUnits);
		settings.addDoubleArray(Key.independentVariableMins.name(), template.independentVariableMins);
		settings.addDoubleArray(Key.independentVariableMaxs.name(), template.independentVariableMaxs);
		settings.addDoubleArray(Key.independentVariableValues.name(), template.independentVariableValues);

		settings.addBoolean(Key.hasData.name(), template.hasData);
	}
}
