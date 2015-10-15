package de.bund.bfr.knime.pmm.openfsmr;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.knime.pmm.annotation.sbml.MetadataAnnotation;
import de.bund.bfr.knime.pmm.annotation.sbml.Model1Annotation;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.file.ManualTertiaryModelFile;
import de.bund.bfr.knime.pmm.file.OneStepSecondaryModelFile;
import de.bund.bfr.knime.pmm.file.OneStepTertiaryModelFile;
import de.bund.bfr.knime.pmm.file.PrimaryModelWDataFile;
import de.bund.bfr.knime.pmm.file.PrimaryModelWODataFile;
import de.bund.bfr.knime.pmm.file.TwoStepSecondaryModelFile;
import de.bund.bfr.knime.pmm.file.TwoStepTertiaryModelFile;
import de.bund.bfr.knime.pmm.model.ManualTertiaryModel;
import de.bund.bfr.knime.pmm.model.OneStepSecondaryModel;
import de.bund.bfr.knime.pmm.model.OneStepTertiaryModel;
import de.bund.bfr.knime.pmm.model.PrimaryModelWData;
import de.bund.bfr.knime.pmm.model.PrimaryModelWOData;
import de.bund.bfr.knime.pmm.model.TwoStepSecondaryModel;
import de.bund.bfr.knime.pmm.model.TwoStepTertiaryModel;
import de.bund.bfr.knime.pmm.sbmlutil.Agent;
import de.bund.bfr.knime.pmm.sbmlutil.Limits;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;
import de.bund.bfr.knime.pmm.sbmlutil.Metadata;
import de.bund.bfr.knime.pmm.sbmlutil.Model1Rule;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;
import de.bund.bfr.knime.pmm.sbmlutil.ReaderUtils;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

/**
 * This is the model implementation of SBMLReader.
 * 
 * Author: Miguel de Alba Aparicio (malba@optimumquality.es)
 */
public class OpenFSMRConverterNodeModel extends NodeModel {

	// configuration keys
	public static final String CFGKEY_FILE = "filename";
	// defaults for persistent state
	private static final String DEFAULT_FILE = "c:/temp/foo.xml";

	// persistent state
	private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE, DEFAULT_FILE);

	Reader reader; // current reader

	/**
	 * Constructor for the node model.
	 */
	protected OpenFSMRConverterNodeModel() {
		// 0 input ports and 1 output port
		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		BufferedDataTable[] tables = null;
		tables = loadPMF(exec);
		return tables;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		return new DataTableSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		filename.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		filename.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		// TODO check if the settings could be applied to our model
		// e.g. if the count is in a certain range (which is ensured by the
		// SettingsModel).
		// Do not actually set any values of any member variables.
		filename.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	// Load PMF file
	private BufferedDataTable[] loadPMF(final ExecutionContext exec) throws Exception {
		// Get model type from annotation in the metadata file

		// a) Open archive
		String filepath = filename.getStringValue();
		CombineArchive ca = new CombineArchive(new File(filepath));

		// b) Get annotation
		MetaDataObject mdo = ca.getDescriptions().get(0);
		Element metaParent = mdo.getXmlDescription();
		Element metaElement = metaParent.getChild("modeltype");
		String modelType = metaElement.getText();

		// c) Close archive
		ca.close();

		if (modelType.equals(ModelType.EXPERIMENTAL_DATA.name())) {
			setWarningMessage("OpenFSMR Converter does not support case0 files currently");
			return null;
		} else if (modelType.equals(ModelType.PRIMARY_MODEL_WDATA.name())) {
			reader = new PrimaryModelWDataReader();
		} else if (modelType.equals(ModelType.PRIMARY_MODEL_WODATA.name())) {
			reader = new PrimaryModelWODataReader();
		} else if (modelType.equals(ModelType.TWO_STEP_SECONDARY_MODEL.name())) {
			reader = new TwoStepSecondaryModelReader();
		} else if (modelType.equals(ModelType.ONE_STEP_SECONDARY_MODEL.name())) {
			reader = new OneStepSecondaryModelReader();
		} else if (modelType.equals(ModelType.MANUAL_SECONDARY_MODEL.name())) {
			setWarningMessage("OpenFSMR Converter does not support case2c files currently");
		} else if (modelType.equals(ModelType.TWO_STEP_TERTIARY_MODEL.name())) {
			reader = new TwoStepTertiaryModelReader();
		} else if (modelType.equals(ModelType.ONE_STEP_TERTIARY_MODEL.name())) {
			reader = new OneStepTertiaryModelReader();
		} else if (modelType.equals(ModelType.MANUAL_TERTIARY_MODEL.name())) {
			reader = new ManualTertiaryModelReader();
		}

		BufferedDataContainer container = reader.read(filepath, exec);
		BufferedDataTable[] tables = { container.getTable() };
		return tables;
	}
}

/**
 * Reader interface
 * 
 * @author Miguel Alba
 */
interface Reader {
	/**
	 * Read models from a CombineArchive and returns a KNIME table with them
	 * 
	 * @throws Exception
	 */
	public BufferedDataContainer read(String filepath, ExecutionContext exec) throws Exception;
}

// TODO: ExperimentalDataReader

class PrimaryModelWDataReader implements Reader {

	private static DataTableSpec spec = new OpenFSMRSchema().createSpec();

	public BufferedDataContainer read(String filepath, ExecutionContext exec) throws Exception {

		// Creates container
		BufferedDataContainer container = exec.createDataContainer(spec);

		// Reads in models from file
		List<PrimaryModelWData> models = PrimaryModelWDataFile.read(filepath);

		// Creates tuples and adds them to the container
		for (PrimaryModelWData model : models) {
			KnimeTuple tuple = Util.createTuple(model.getSBMLDoc());
			container.addRowToTable(tuple);
			exec.setProgress((float) container.size() / models.size());
		}
		container.close();

		return container;
	}
}

class PrimaryModelWODataReader implements Reader {

	private static DataTableSpec spec = new OpenFSMRSchema().createSpec();

	public BufferedDataContainer read(String filepath, ExecutionContext exec) throws Exception {
		// Creates container
		BufferedDataContainer container = exec.createDataContainer(spec);

		// Reads in models from file
		List<PrimaryModelWOData> models = PrimaryModelWODataFile.read(filepath);

		// Creates tuples and adds them to the container
		for (PrimaryModelWOData model : models) {
			KnimeTuple tuple = Util.createTuple(model.getSBMLDoc());
			container.addRowToTable(tuple);
			exec.setProgress((float) container.size() / models.size());
		}
		container.close();

		return container;
	}
}

class TwoStepSecondaryModelReader implements Reader {

	private static DataTableSpec spec = new OpenFSMRSchema().createSpec();

	public BufferedDataContainer read(String filepath, ExecutionContext exec) throws Exception {
		// Creates container
		BufferedDataContainer container = exec.createDataContainer(spec);

		// Reads in models from file
		List<TwoStepSecondaryModel> models = TwoStepSecondaryModelFile.read(filepath);

		// Creates tuples and adds them to the container
		for (TwoStepSecondaryModel tssm : models) {
			KnimeTuple tuple = Util.createTuple(tssm.getPrimModels().get(0).getSBMLDoc());
			container.addRowToTable(tuple);
			exec.setProgress((float) container.size() / models.size());
		}
		container.close();

		return container;
	}
}

class OneStepSecondaryModelReader implements Reader {

	private static DataTableSpec spec = new OpenFSMRSchema().createSpec();

	public BufferedDataContainer read(String filepath, ExecutionContext exec) throws Exception {

		// Creates container
		BufferedDataContainer container = exec.createDataContainer(spec);

		// Reads in models from file
		List<OneStepSecondaryModel> models = OneStepSecondaryModelFile.read(filepath);

		// Creates tuples and adds them to the container
		for (OneStepSecondaryModel ossm : models) {
			KnimeTuple tuple = Util.createTuple(ossm.getSBMLDoc());
			container.addRowToTable(tuple);
			exec.setProgress((float) container.size() / models.size());
		}
		container.close();

		return container;

	}
}

class TwoStepTertiaryModelReader implements Reader {

	private static DataTableSpec spec = new OpenFSMRSchema().createSpec();

	public BufferedDataContainer read(String filepath, ExecutionContext exec) throws Exception {
		// Creates container
		BufferedDataContainer container = exec.createDataContainer(spec);

		// Read in models from file
		List<TwoStepTertiaryModel> models = TwoStepTertiaryModelFile.read(filepath);

		// Creates tuples and adds them to the container
		for (TwoStepTertiaryModel tssm : models) {
			KnimeTuple tuple = Util.createTuple(tssm.getTertDoc());
			container.addRowToTable(tuple);
			exec.setProgress((float) container.size() / models.size());
		}
		container.close();

		return container;
	}
}

class OneStepTertiaryModelReader implements Reader {

	private static DataTableSpec spec = new OpenFSMRSchema().createSpec();

	public BufferedDataContainer read(String filepath, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		BufferedDataContainer container = exec.createDataContainer(spec);

		// Read in models from file
		List<OneStepTertiaryModel> models = OneStepTertiaryModelFile.read(filepath);

		// Creates tuples and adds them to the container
		for (OneStepTertiaryModel ostm : models) {
			KnimeTuple tuple = Util.createTuple(ostm.getTertDoc());
			container.addRowToTable(tuple);
			exec.setProgress((float) container.size() / models.size());
		}
		container.close();

		return container;
	}
}

class ManualTertiaryModelReader implements Reader {

	private static DataTableSpec spec = new OpenFSMRSchema().createSpec();

	public BufferedDataContainer read(String filepath, ExecutionContext exec) throws Exception {
		// Creates container
		BufferedDataContainer container = exec.createDataContainer(spec);

		// Read in models from file
		List<ManualTertiaryModel> models = ManualTertiaryModelFile.read(filepath);

		// Creates tuples and adds them to the container
		for (ManualTertiaryModel mtm : models) {
			KnimeTuple tuple = Util.createTuple(mtm.getTertDoc());
			container.addRowToTable(tuple);
			exec.setProgress((float) container.size() / models.size());
		}

		container.close();

		return container;
	}
}

class Util {

	public static KnimeTuple createTuple(SBMLDocument doc) {
		// Add cells to the row
		KnimeTuple tuple = new KnimeTuple(new OpenFSMRSchema());

		Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();
		Model model = doc.getModel();
		Agent species = new Agent(model.getSpecies(0));
		Matrix matrix = new Matrix(model.getCompartment(0));

		tuple.setValue(OpenFSMRSchema.ATT_ID, model.getId());
		tuple.setValue(OpenFSMRSchema.ATT_NAME, model.getName());
		tuple.setValue(OpenFSMRSchema.ATT_LINK, "");

		tuple.setValue(OpenFSMRSchema.ATT_ORGANISM, species.getSpecies().getName());
		tuple.setValue(OpenFSMRSchema.ATT_ORGANISM_DETAIL, species.getDetail());

		tuple.setValue(OpenFSMRSchema.ATT_ENVIRONMENT, matrix.getCompartment().getName());
		tuple.setValue(OpenFSMRSchema.ATT_ENVIRONMENT_DETAIL, matrix.getDetails());

		tuple.setValue(OpenFSMRSchema.ATT_CREATOR, metadata.getGivenName());

		Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());
		String refDesc = null;
		String refDescLink = null;

		if (!m1Annot.getLits().isEmpty()) {
			LiteratureItem lit = m1Annot.getLits().get(0);
			refDesc = lit.getAuthor() + " " + lit.getTitle();
			refDescLink = lit.getWebsite();
		}

		tuple.setValue(OpenFSMRSchema.ATT_REF_DESC, refDesc);
		tuple.setValue(OpenFSMRSchema.ATT_REF_DESC_LINK, refDescLink);

		tuple.setValue(OpenFSMRSchema.ATT_CREATED, metadata.getCreatedDate());
		tuple.setValue(OpenFSMRSchema.ATT_MODIFIED, metadata.getModifiedDate());

		tuple.setValue(OpenFSMRSchema.ATT_RIGHTS, "");

		tuple.setValue(OpenFSMRSchema.ATT_NOTES, "");

		tuple.setValue(OpenFSMRSchema.ATT_CURATION_STATUS, "");

		tuple.setValue(OpenFSMRSchema.ATT_TYPE, metadata.getType());

		tuple.setValue(OpenFSMRSchema.ATT_SUBJECT, new Model1Rule((AssignmentRule) model.getRule(0)).getSubject());

		tuple.setValue(OpenFSMRSchema.ATT_FOOD_PROCESS, "");

		String depUnitID = species.getSpecies().getUnits();
		String depUnitName = model.getUnitDefinition(depUnitID).getName();
		String depUnitCategory;

		Map<String, UnitsFromDB> dbUnits = DBUnits.getDBUnits();

		if (depUnitID.equals("dimensionless")) {
			depUnitCategory = "Dimensionless quantity";
		} else {
			depUnitCategory = dbUnits.get(depUnitName).getKind_of_property_quantity();
		}

		tuple.setValue(OpenFSMRSchema.ATT_DEP, depUnitCategory);
		tuple.setValue(OpenFSMRSchema.ATT_DEP_UNITS, depUnitName);

		Map<String, Limits> limits = ReaderUtils.parseConstraints(model.getListOfConstraints());
		String max = "";
		String min = "";
		if (limits.containsKey(depUnitID)) {
			Limits depLimits = limits.get(depUnitID);
			max = depLimits.getMax().toString();
			min = depLimits.getMin().toString();
		}

		tuple.setValue(OpenFSMRSchema.ATT_DEP_MAX, max);
		tuple.setValue(OpenFSMRSchema.ATT_DEP_MIN, min);

		tuple.setValue(OpenFSMRSchema.ATT_INDEP, Categories.getTimeCategory().getName());

		tuple.setValue(OpenFSMRSchema.ATT_SOFTWARE, "");
		tuple.setValue(OpenFSMRSchema.ATT_SOFTWARE_LINK, "");
		tuple.setValue(OpenFSMRSchema.ATT_ACCESIBILITY, "");
		tuple.setValue(OpenFSMRSchema.ATT_STOCHASTIC_MODELING, 0);
		tuple.setValue(OpenFSMRSchema.ATT_PREDICTION_CONDITIONS, "");

		return tuple;
	}
}