package de.bund.bfr.knime.pmm.fskx.editor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.fskx.FskMetaData;
import de.bund.bfr.knime.pmm.fskx.Variable;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.ModelType;

public class FskMetaDataSettings {

	public FskMetaData metaData = new FskMetaData();

	private static enum Key {
		NAME,
		ID,
		LINK,
		ORGANISM,
		ORGANISM_DETAILS,
		MATRIX,
		MATRIX_DETAILS,
		CREATOR,
		FAMILY_NAME,
		CONTACT,
		SOFTWARE,
		REFERENCE_DESCRIPTION,
		REFERENCE_DESCRIPTION_LINK,
		CREATED_DATE,
		MODIFIED_DATE,
		RIGHTS,
		NOTES,
		CURATED,
		TYPE,
		SUBJECT,
		FOOD_PROCESS,
		DEP_VAR,
		INDEP_VARS,
		HAS_DATA
	};

	// Default values
	private static String DEF_STRING = "";
	private static long DEF_DATE = -1;
	private static short DEF_ENUM = -1;
	private static boolean DEF_BOOL = false;

	public void saveSettings(final NodeSettingsWO settings) {
		if (metaData == null)
			return;
		
		settings.addString(Key.NAME.name(), metaData.modelName);
		settings.addString(Key.ID.name(), metaData.modelId);
		settings.addString(Key.LINK.name(), metaData.modelLink);

		settings.addString(Key.ORGANISM.name(), metaData.organism);
		settings.addString(Key.ORGANISM_DETAILS.name(), metaData.organismDetails);

		settings.addString(Key.MATRIX.name(), metaData.matrix);
		settings.addString(Key.MATRIX_DETAILS.name(), metaData.matrixDetails);

		settings.addString(Key.CREATOR.name(), metaData.creator);
		settings.addString(Key.FAMILY_NAME.name(), metaData.familyName);
		settings.addString(Key.CONTACT.name(), metaData.contact);
		settings.addString(Key.SOFTWARE.name(), metaData.software == null ? "" : metaData.software.name());

		settings.addString(Key.REFERENCE_DESCRIPTION.name(), metaData.referenceDescription);
		settings.addString(Key.REFERENCE_DESCRIPTION_LINK.name(), metaData.referenceDescriptionLink);

		settings.addLong(Key.CREATED_DATE.name(), metaData.createdDate == null ? 0 : metaData.createdDate.getTime());
		settings.addLong(Key.MODIFIED_DATE.name(), metaData.modifiedDate == null ? 0 : metaData.modifiedDate.getTime());

		settings.addString(Key.RIGHTS.name(), metaData.rights);
		settings.addString(Key.NOTES.name(), metaData.notes);

		settings.addBoolean(Key.CURATED.name(), metaData.curated);
		settings.addInt(Key.TYPE.name(), metaData.type == null ? -1 : metaData.type.ordinal());
		settings.addInt(Key.SUBJECT.name(), metaData.subject == null ? -1 : metaData.subject.ordinal());

		settings.addString(Key.FOOD_PROCESS.name(), metaData.foodProcess);

		{
			try (ByteArrayOutputStream out = new ByteArrayOutputStream();
					ObjectOutputStream os = new ObjectOutputStream(out)) {
				os.writeObject(metaData.dependentVariable);
				settings.addByteArray(Key.DEP_VAR.name(), out.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		{
			try (ByteArrayOutputStream out = new ByteArrayOutputStream();
					ObjectOutputStream os = new ObjectOutputStream(out)) {
				os.writeObject(metaData.independentVariables);
				settings.addByteArray(Key.INDEP_VARS.name(), out.toByteArray());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		settings.addBoolean(Key.HAS_DATA.name(), metaData.hasData);
	}

	public void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		if (settings.keySet().isEmpty())
			return;
		
		metaData.modelName = settings.getString(Key.NAME.name());
		metaData.modelId = settings.getString(Key.ID.name());
		metaData.modelLink = settings.getString(Key.LINK.name());

		metaData.organism = settings.getString(Key.ORGANISM.name());
		metaData.organismDetails = settings.getString(Key.ORGANISM_DETAILS.name());

		metaData.matrix = settings.getString(Key.MATRIX.name());
		metaData.matrixDetails = settings.getString(Key.MATRIX_DETAILS.name());

		metaData.creator = settings.getString(Key.CREATOR.name());
		metaData.familyName = settings.getString(Key.FAMILY_NAME.name());
		metaData.contact = settings.getString(Key.CONTACT.name());
		{
			String softwareString = settings.getString(Key.SOFTWARE.name());
			metaData.software = softwareString.isEmpty() ? null : FskMetaData.Software.valueOf(softwareString);
		}

		metaData.referenceDescription = settings.getString(Key.REFERENCE_DESCRIPTION.name());
		metaData.referenceDescriptionLink = settings.getString(Key.REFERENCE_DESCRIPTION_LINK.name());

		{
			long createdDate = settings.getLong(Key.CREATED_DATE.name());
			metaData.createdDate = createdDate == 0 ? null : new Date(createdDate);
		}
		{
			long modifiedDate = settings.getLong(Key.MODIFIED_DATE.name());
			metaData.modifiedDate = modifiedDate == 0 ? null : new Date(modifiedDate);
		}

		metaData.rights = settings.getString(Key.RIGHTS.name());
		metaData.notes = settings.getString(Key.NOTES.name());

		metaData.curated = settings.getBoolean(Key.CURATED.name());
		{
			int type = settings.getInt(Key.TYPE.name());
			metaData.type = type == -1 ? null : ModelType.values()[type];
		}
		{
			int subject = settings.getInt(Key.SUBJECT.name());
			metaData.subject = subject == -1 ? null : ModelClass.values()[subject];
		}

		metaData.foodProcess = settings.getString(Key.FOOD_PROCESS.name());

		try (ByteArrayInputStream in = new ByteArrayInputStream(settings.getByteArray(Key.DEP_VAR.name()));
				ObjectInputStream is = new ObjectInputStream(in)) {
			metaData.dependentVariable = (Variable) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try (ByteArrayInputStream in = new ByteArrayInputStream(settings.getByteArray(Key.INDEP_VARS.name()));
				ObjectInputStream is = new ObjectInputStream(in)) {
			metaData.independentVariables = (List<Variable>) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		metaData.hasData = settings.getBoolean(Key.HAS_DATA.name());
	}

	public void loadSettingsForDialog(final NodeSettingsRO settings) {
		metaData.modelName = settings.getString(Key.NAME.name(), DEF_STRING);
		metaData.modelId = settings.getString(Key.ID.name(), DEF_STRING);
		metaData.modelLink = settings.getString(Key.LINK.name(), DEF_STRING);

		metaData.organism = settings.getString(Key.ORGANISM.name(), DEF_STRING);
		metaData.organismDetails = settings.getString(Key.ORGANISM_DETAILS.name(), DEF_STRING);

		metaData.matrix = settings.getString(Key.MATRIX.name(), DEF_STRING);
		metaData.matrixDetails = settings.getString(Key.MATRIX_DETAILS.name(), DEF_STRING);

		metaData.creator = settings.getString(Key.CREATOR.name(), DEF_STRING);
		metaData.familyName = settings.getString(Key.FAMILY_NAME.name(), DEF_STRING);
		metaData.contact = settings.getString(Key.CONTACT.name(), DEF_STRING);
		{
			String softwareString = settings.getString(Key.SOFTWARE.name(), DEF_STRING);
			metaData.software = softwareString.isEmpty() ? null : FskMetaData.Software.valueOf(softwareString);
		}

		metaData.referenceDescription = settings.getString(Key.REFERENCE_DESCRIPTION.name(), DEF_STRING);
		metaData.referenceDescriptionLink = settings.getString(Key.REFERENCE_DESCRIPTION_LINK.name(), DEF_STRING);

		{
			long createdDate = settings.getLong(Key.CREATED_DATE.name(), DEF_DATE);
			metaData.createdDate = createdDate == 0 ? null : new Date(createdDate);
		}
		{
			long modifiedDate = settings.getLong(Key.MODIFIED_DATE.name(), DEF_DATE);
			metaData.modifiedDate = modifiedDate == 0 ? null : new Date(modifiedDate);
		}

		metaData.rights = settings.getString(Key.RIGHTS.name(), DEF_STRING);
		metaData.notes = settings.getString(Key.NOTES.name(), DEF_STRING);

		metaData.curated = settings.getBoolean(Key.CURATED.name(), DEF_BOOL);
		{
			int type = settings.getInt(Key.TYPE.name(), DEF_ENUM);
			metaData.type = type == DEF_ENUM ? null : ModelType.values()[type];
		}
		{
			int subject = settings.getInt(Key.SUBJECT.name(), DEF_ENUM);
			metaData.subject = subject == DEF_ENUM ? null : ModelClass.values()[subject];
		}

		metaData.foodProcess = settings.getString(Key.FOOD_PROCESS.name(), DEF_STRING);

		try (ByteArrayInputStream in = new ByteArrayInputStream(settings.getByteArray(Key.DEP_VAR.name()));
				ObjectInputStream is = new ObjectInputStream(in)) {
			metaData.dependentVariable = (Variable) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidSettingsException e) {
			metaData.dependentVariable = new Variable();
		}
		
		try (ByteArrayInputStream in = new ByteArrayInputStream(settings.getByteArray(Key.INDEP_VARS.name()));
				ObjectInputStream is = new ObjectInputStream(in)) {
			metaData.independentVariables = (List<Variable>) is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidSettingsException e) {
			metaData.independentVariables = new ArrayList<>();
		}

		metaData.hasData = settings.getBoolean(Key.HAS_DATA.name(), DEF_BOOL);
	}
}
