package de.bund.bfr.knime.pmm.openfsmr;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.ExperimentalDataFile;
import de.bund.bfr.pmf.file.PMFMetadataNode;
import de.bund.bfr.pmf.model.ExperimentalData;
import de.bund.bfr.pmf.numl.ConcentrationOntology;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.numl.ResultComponent;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

/**
 * This is the model implementation of OpenFSMRConverter.
 * 
 * Author: Miguel de Alba Aparicio
 */
public class OpenFSMRConverterNodeModel extends NodeModel {

	// configuration keys
	public static final String CFGKEY_FILE = "filename";
	// defaults for persistent state
	private static final String DEFAULT_FILE = "c:/temp/foo.xml";

	// persistent state
	private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE, DEFAULT_FILE);

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
		PMFMetadataNode pmfMetadataNode = new PMFMetadataNode(metaParent);
		ModelType modelType = pmfMetadataNode.getModelType();

		// c) Close archive
		ca.close();

		DataTableSpec tableSpec = new OpenFSMRSchema().createSpec();
		BufferedDataContainer container = exec.createDataContainer(tableSpec);
		
		switch (modelType) {
		case EXPERIMENTAL_DATA:
			List<ExperimentalData> eds = ExperimentalDataFile.read(filepath);
			// Creates tuples and adds them to the container
			for (ExperimentalData ed : eds) {
				KnimeTuple tuple = processData(ed);
				container.addRowToTable(tuple);
				
				float progress = container.size() / eds.size();
				exec.setProgress(progress);
			}
			break;
		case PRIMARY_MODEL_WDATA:
			// TODO: Case 1a
			setWarningMessage("OpenFSMR Converter does not support case 1a files");
			break;
		case PRIMARY_MODEL_WODATA:
			// TODO: Case 1b
			setWarningMessage("OpenFSMR Converter does not support case 1b files");
			break;
		case TWO_STEP_SECONDARY_MODEL:
			// TODO: Case 2a
			setWarningMessage("OpenFSMR Converteer does not support case 2a files");
			break;
		case ONE_STEP_SECONDARY_MODEL:
			// TODO: Case 2b
			setWarningMessage("OpenFSMR Converteer does not support case 2b files");
			break;
		case MANUAL_SECONDARY_MODEL:
			// TODO: Case 2c
			setWarningMessage("OpenFSMR Converteer does not support case 2c files");
			break;
		case TWO_STEP_TERTIARY_MODEL:
			// TODO: Case 3a
			setWarningMessage("OpenFSMR Converteer does not support case 3a files");
			break;
		case ONE_STEP_TERTIARY_MODEL:
			// TODO: Case 3b
			setWarningMessage("OpenFSMR Converteer does not support case 3b files");
			break;
		case MANUAL_TERTIARY_MODEL:
			// TODO: Case 3c
			setWarningMessage("OpenFSMR Converteer does not support case 3c files");
			break;
		}
		
		container.close();
		BufferedDataTable[] tables = { container.getTable() };
		return tables;
	}
	
	private static KnimeTuple processData(ExperimentalData ed) {
		FSMRTemplate template = new FSMRTemplateImpl();

		NuMLDocument doc = ed.getDoc();
		ConcentrationOntology concOntology = doc.getConcentrationOntologyTerm();

		// PMF organism
		PMFSpecies species = concOntology.getSpecies();
		template.setOrganismName(species.getName());
		if (species.isSetDetail()) {
			String organismDetail = species.getDetail();
			template.setOrganismDetails(organismDetail);
		}

		// PMF environment
		PMFCompartment compartment = concOntology.getCompartment();
		template.setMatrixName(compartment.getName());
		if (compartment.isSetDetail()) {
			String matrixDetail = compartment.getDetail();
			template.setMatrixDetails(matrixDetail);
		}

		ResultComponent rc = doc.getResultComponent();
		// Sets creator
		if (rc.isSetCreatorGivenName()) {
			String creatorGivenName = rc.getCreatorGivenName();
			template.setCreator(creatorGivenName);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);
		// Sets created date
		if (rc.isSetCreatedDate()) {
			String createdDateAsString = rc.getCreatedDate();
			try {
				Date createdDate = dateFormat.parse(createdDateAsString);
				template.setCreatedDate(createdDate);
			} catch (ParseException e) {
				System.err.println(createdDateAsString + " is not a valid date");
				e.printStackTrace();
			}
		}

		// Sets modified date
		if (rc.isSetModifiedDate()) {
			String modifiedDateAsString = rc.getModifiedDate();
			try {
				Date modifiedDate = dateFormat.parse(modifiedDateAsString);
				template.setModifiedDate(modifiedDate);
			} catch (ParseException e) {
				System.err.println(modifiedDateAsString + " is not a valid date");
				e.printStackTrace();
			}
		}

		// Sets model rights
		if (rc.isSetRights()) {
			String rights = rc.getRights();
			template.setRights(rights);
		}

		// Sets model type
		template.setModelType(ModelType.EXPERIMENTAL_DATA);

		// Sets model subject as ModelClass.UNKNOWN since the data files do not
		// keep the subject of the model
		template.setModelSubject(ModelClass.UNKNOWN);

		KnimeTuple tuple = Util.createTupleFromTemplate(template);

		return tuple;
	}
}

/**
 * Reader interface
 * 
 * @author Miguel Alba
 */
//interface Reader {
//	/**
//	 * Read models from a CombineArchive and returns a KNIME table with them
//	 * 
//	 * @throws Exception
//	 */
//	public BufferedDataContainer read(String filepath, ExecutionContext exec) throws Exception;
//}

// TODO: PrimaryModelWDataReader
// class PrimaryModelWDataReader implements Reader {
//
// private static DataTableSpec spec = new OpenFSMRSchema().createSpec();
//
// public BufferedDataContainer read(String filepath, ExecutionContext exec)
// throws Exception {
//
// // Creates container
// BufferedDataContainer container = exec.createDataContainer(spec);
//
// // Reads in models from file
// List<PrimaryModelWData> models = PrimaryModelWDataFile.read(filepath);
//
// // Creates tuples and adds them to the container
// for (PrimaryModelWData model : models) {
// KnimeTuple tuple = Util.createTuple(model.getSBMLDoc());
// container.addRowToTable(tuple);
// exec.setProgress((float) container.size() / models.size());
// }
// container.close();
//
// return container;
// }
// }

// TODO: PrimaryModelWODataReader
// class PrimaryModelWODataReader implements Reader {
//
// private static DataTableSpec spec = new OpenFSMRSchema().createSpec();
//
// public BufferedDataContainer read(String filepath, ExecutionContext exec)
// throws Exception {
// // Creates container
// BufferedDataContainer container = exec.createDataContainer(spec);
//
// // Reads in models from file
// List<PrimaryModelWOData> models = PrimaryModelWODataFile.read(filepath);
//
// // Creates tuples and adds them to the container
// for (PrimaryModelWOData model : models) {
// KnimeTuple tuple = Util.createTuple(model.getSBMLDoc());
// container.addRowToTable(tuple);
// exec.setProgress((float) container.size() / models.size());
// }
// container.close();
//
// return container;
// }
// }

// TODO: TwoStepSecondaryModelReader
// class TwoStepSecondaryModelReader implements Reader {
//
// private static DataTableSpec spec = new OpenFSMRSchema().createSpec();
//
// public BufferedDataContainer read(String filepath, ExecutionContext exec)
// throws Exception {
// // Creates container
// BufferedDataContainer container = exec.createDataContainer(spec);
//
// // Reads in models from file
// List<TwoStepSecondaryModel> models =
// TwoStepSecondaryModelFile.read(filepath);
//
// // Creates tuples and adds them to the container
// for (TwoStepSecondaryModel tssm : models) {
// KnimeTuple tuple =
// Util.createTuple(tssm.getPrimModels().get(0).getSBMLDoc());
// container.addRowToTable(tuple);
// exec.setProgress((float) container.size() / models.size());
// }
// container.close();
//
// return container;
// }
// }

// TODO: OneStepSecondaryModelReader
// class OneStepSecondaryModelReader implements Reader {
//
// private static DataTableSpec spec = new OpenFSMRSchema().createSpec();
//
// public BufferedDataContainer read(String filepath, ExecutionContext exec)
// throws Exception {
//
// // Creates container
// BufferedDataContainer container = exec.createDataContainer(spec);
//
// // Reads in models from file
// List<OneStepSecondaryModel> models =
// OneStepSecondaryModelFile.read(filepath);
//
// // Creates tuples and adds them to the container
// for (OneStepSecondaryModel ossm : models) {
// KnimeTuple tuple = Util.createTuple(ossm.getSBMLDoc());
// container.addRowToTable(tuple);
// exec.setProgress((float) container.size() / models.size());
// }
// container.close();
//
// return container;
//
// }
// }

// TODO: TwoStepTertiaryModelReader
// class TwoStepTertiaryModelReader implements Reader {
//
// private static DataTableSpec spec = new OpenFSMRSchema().createSpec();
//
// public BufferedDataContainer read(String filepath, ExecutionContext exec)
// throws Exception {
// // Creates container
// BufferedDataContainer container = exec.createDataContainer(spec);
//
// // Read in models from file
// List<TwoStepTertiaryModel> models = TwoStepTertiaryModelFile.read(filepath);
//
// // Creates tuples and adds them to the container
// for (TwoStepTertiaryModel tssm : models) {
// KnimeTuple tuple = Util.createTuple(tssm.getTertDoc());
// container.addRowToTable(tuple);
// exec.setProgress((float) container.size() / models.size());
// }
// container.close();
//
// return container;
// }
// }

// TODO: OneStepTertiaryModelReader
// class OneStepTertiaryModelReader implements Reader {
//
// private static DataTableSpec spec = new OpenFSMRSchema().createSpec();
//
// public BufferedDataContainer read(String filepath, ExecutionContext exec)
// throws Exception {
// // Creates table spec and container
// BufferedDataContainer container = exec.createDataContainer(spec);
//
// // Read in models from file
// List<OneStepTertiaryModel> models = OneStepTertiaryModelFile.read(filepath);
//
// // Creates tuples and adds them to the container
// for (OneStepTertiaryModel ostm : models) {
// KnimeTuple tuple = Util.createTuple(ostm.getTertDoc());
// container.addRowToTable(tuple);
// exec.setProgress((float) container.size() / models.size());
// }
// container.close();
//
// return container;
// }
// }

// TODO: ManualTertiaryModelReader
// class ManualTertiaryModelReader implements Reader {
//
// private static DataTableSpec spec = new OpenFSMRSchema().createSpec();
//
// public BufferedDataContainer read(String filepath, ExecutionContext exec)
// throws Exception {
// // Creates container
// BufferedDataContainer container = exec.createDataContainer(spec);
//
// // Read in models from file
// List<ManualTertiaryModel> models = ManualTertiaryModelFile.read(filepath);
//
// // Creates tuples and adds them to the container
// for (ManualTertiaryModel mtm : models) {
// KnimeTuple tuple = Util.createTuple(mtm.getTertDoc());
// container.addRowToTable(tuple);
// exec.setProgress((float) container.size() / models.size());
// }
//
// container.close();
//
// return container;
// }
// }

class Util {

	public static KnimeTuple createTuple(SBMLDocument doc) {
		// Add cells to the row
		KnimeTuple tuple = new KnimeTuple(new OpenFSMRSchema());
		//
		// Metadata metadata = new
		// MetadataAnnotation(doc.getAnnotation()).getMetadata();
		// Model model = doc.getModel();
		// Agent species = new Agent(model.getSpecies(0));
		// Matrix matrix = new Matrix(model.getCompartment(0));
		//
		// tuple.setValue(OpenFSMRSchema.ATT_MODEL_ID, model.getId());
		// tuple.setValue(OpenFSMRSchema.ATT_MODEL_NAME, model.getName());
		// tuple.setValue(OpenFSMRSchema.ATT_MODEL_LINK, "");
		//
		// tuple.setValue(OpenFSMRSchema.ATT_ORGANISM_NAME,
		// species.getSpecies().getName());
		// tuple.setValue(OpenFSMRSchema.ATT_ORGANISM_DETAIL,
		// species.getDetail());
		//
		// tuple.setValue(OpenFSMRSchema.ATT_ENVIRONMENT_NAME,
		// matrix.getCompartment().getName());
		// tuple.setValue(OpenFSMRSchema.ATT_ENVIRONMENT_DETAIL,
		// matrix.getDetails());
		//
		// tuple.setValue(OpenFSMRSchema.ATT_CREATOR, metadata.getGivenName());
		//
		// Model1Annotation m1Annot = new
		// Model1Annotation(model.getAnnotation());
		// String refDesc = null;
		// String refDescLink = null;
		//
		// if (!m1Annot.getLits().isEmpty()) {
		// LiteratureItem lit = m1Annot.getLits().get(0);
		// refDesc = lit.getAuthor() + " " + lit.getTitle();
		// refDescLink = lit.getWebsite();
		// }
		//
		// tuple.setValue(OpenFSMRSchema.ATT_REFERENCE_DESCRIPTION, refDesc);
		// tuple.setValue(OpenFSMRSchema.ATT_REFERENCE_DESCRIPTION_LINK,
		// refDescLink);
		//
		// tuple.setValue(OpenFSMRSchema.ATT_CREATED_DATE,
		// metadata.getCreatedDate());
		// tuple.setValue(OpenFSMRSchema.ATT_MODIFIED,
		// metadata.getModifiedDate());
		//
		// tuple.setValue(OpenFSMRSchema.ATT_RIGHTS, "");
		//
		// tuple.setValue(OpenFSMRSchema.ATT_NOTES, "");
		//
		// tuple.setValue(OpenFSMRSchema.ATT_CURATION_STATUS, "");
		//
		// tuple.setValue(OpenFSMRSchema.ATT_MODEL_TYPE, metadata.getType());
		//
		// tuple.setValue(OpenFSMRSchema.ATT_MODEL_SUBJECT, new
		// Model1Rule((AssignmentRule) model.getRule(0)).getSubject());
		//
		// tuple.setValue(OpenFSMRSchema.ATT_FOOD_PROCESS, "");
		//
		// String depUnitID = species.getSpecies().getUnits();
		// String depUnitName = model.getUnitDefinition(depUnitID).getName();
		// String depUnitCategory;
		//
		// Map<String, UnitsFromDB> dbUnits = DBUnits.getDBUnits();
		//
		// if (depUnitID.equals("dimensionless")) {
		// depUnitCategory = "Dimensionless quantity";
		// } else {
		// depUnitCategory =
		// dbUnits.get(depUnitName).getKind_of_property_quantity();
		// }
		//
		// tuple.setValue(OpenFSMRSchema.ATT_DEPENDENT_VARIABLE,
		// depUnitCategory);
		// tuple.setValue(OpenFSMRSchema.ATT_DEPENDENT_VARIABLE_UNIT,
		// depUnitName);
		//
		// Map<String, Limits> limits =
		// ReaderUtils.parseConstraints(model.getListOfConstraints());
		// String max = "";
		// String min = "";
		// if (limits.containsKey(depUnitID)) {
		// Limits depLimits = limits.get(depUnitID);
		// max = depLimits.getMax().toString();
		// min = depLimits.getMin().toString();
		// }
		//
		// tuple.setValue(OpenFSMRSchema.ATT_DEPENDENT_VARIABLE_MAX, max);
		// tuple.setValue(OpenFSMRSchema.ATT_DEPENDENT_VARIABLE_MIN, min);
		//
		// tuple.setValue(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE,
		// Categories.getTimeCategory().getName());
		//
		// tuple.setValue(OpenFSMRSchema.ATT_SOFTWARE, "");
		// tuple.setValue(OpenFSMRSchema.ATT_SOFTWARE_LINK, "");
		// tuple.setValue(OpenFSMRSchema.ATT_ACCESIBILITY, "");
		// tuple.setValue(OpenFSMRSchema.ATT_STOCHASTIC_MODELING, 0);
		// tuple.setValue(OpenFSMRSchema.ATT_PREDICTION_CONDITIONS, "");

		return tuple;
	}

	public static KnimeTuple createTupleFromTemplate(FSMRTemplate template) {
		KnimeTuple tuple = new KnimeTuple(new OpenFSMRSchema());

		if (template.isSetModelName()) {
			String modelName = template.getModelName();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_NAME, modelName);
		}

		if (template.isSetModelId()) {
			String modelId = template.getModelId();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_ID, modelId);
		}

		if (template.isSetModelLink()) {
			URL modelLink = template.getModelLink();
			String modelLinkAsString = modelLink.toString();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_LINK, modelLinkAsString);
		}

		if (template.isSetOrganismName()) {
			String organismName = template.getOrganismName();
			tuple.setValue(OpenFSMRSchema.ATT_ORGANISM_NAME, organismName);
		}

		if (template.isSetOrganismDetails()) {
			String organismDetails = template.getOrganismDetails();
			tuple.setValue(OpenFSMRSchema.ATT_ORGANISM_DETAIL, organismDetails);
		}

		if (template.isSetCreator()) {
			String creator = template.getCreator();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_CREATOR, creator);
		}

		if (template.isSetReferenceDescription()) {
			String referenceDescription = template.getReferenceDescription();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION, referenceDescription);
		}

		if (template.isSetReferenceDescriptionLink()) {
			URL referenceDescriptionLink = template.getReferenceDescriptionLink();
			String referenceDescriptionLinkAsString = referenceDescriptionLink.toString();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION_LINK, referenceDescriptionLinkAsString);
		}

		if (template.isSetCreatedDate()) {
			Date createdDate = template.getCreatedDate();
			String createdDateAsString = createdDate.toString();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_CREATED_DATE, createdDateAsString);
		}

		if (template.isSetModifiedDate()) {
			Date modifiedDate = template.getModifiedDate();
			String modifiedDateAsString = modifiedDate.toString();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_MODIFIED_DATE, modifiedDateAsString);
		}

		if (template.isSetRights()) {
			String rights = template.getRights();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_RIGHTS, rights);
		}

		if (template.isSetNotes()) {
			String notes = template.getNotes();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_NOTES, notes);
		}

		if (template.isSetCurationStatus()) {
			String curationStatus = template.getCurationStatus();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_CURATION_STATUS, curationStatus);
		}

		if (template.isSetModelType()) {
			ModelType modelType = template.getModelType();
			String modelTypeAsString = modelType.toString();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_TYPE, modelTypeAsString);
		}

		if (template.isSetModelSubject()) {
			ModelClass modelSubject = template.getModelSubject();
			String modelSubjectAsString = modelSubject.toString();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_SUBJECT, modelSubjectAsString);
		}

		if (template.isSetFoodProcess()) {
			String foodProcess = template.getFoodProcess();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_FOOD_PROCESS, foodProcess);
		}

		if (template.isSetDependentVariable()) {
			String dependentVariable = template.getDependentVariable();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE, dependentVariable);
		}

		if (template.isSetDependentVariableUnit()) {
			String dependentVariableUnit = template.getDependentVariableUnit();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_UNIT, dependentVariableUnit);
		}

		if (template.isSetDependentVariableMin()) {
			double dependentVariableMin = template.getDependentVariableMin();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MIN, dependentVariableMin);
		}

		if (template.isSetDependentVariableMax()) {
			double dependentVariableMax = template.getDependentVariableMax();
			tuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MAX, dependentVariableMax);
		}

		if (template.isSetIndependentVariable()) {
			String independentVariable = template.getIndependentVariable();
			tuple.setValue(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE, independentVariable);
		}

		if (template.isSetSoftware()) {
			String software = template.getSoftware();
			tuple.setValue(OpenFSMRSchema.ATT_SOFTWARE, software);
		}

		if (template.isSetSoftwareLink()) {
			URL softwareLink = template.getSoftwareLink();
			String softwareLinkAsString = softwareLink.toString();
			tuple.setValue(OpenFSMRSchema.ATT_SOFTWARE_LINK, softwareLinkAsString);
		}

		if (template.isSetSoftwareNotes()) {
			String softwareNotes = template.getSoftwareNotes();
			tuple.setValue(OpenFSMRSchema.ATT_SOFTWARE_NOTES, softwareNotes);
		}

		if (template.isSetSoftwareAccessibility()) {
			String softwareAccessibility = template.getSoftwareNotes();
			tuple.setValue(OpenFSMRSchema.ATT_SOFTWARE_NOTES, softwareAccessibility);
		}

		if (template.isSetSoftwareStochasticModeling()) {
			boolean softwareStochasticModeling = template.getSoftwareStochasticModeling();
			int softwareStochasticModelingAsInt = softwareStochasticModeling ? 1 : 0;
			tuple.setValue(OpenFSMRSchema.ATT_SOFTWARE_STOCHASTIC_MODELING, softwareStochasticModelingAsInt);
		}

		if (template.isSetSoftwarePredictionConditions()) {
			String softwarePredictionConditions = template.getSoftwarePredictionConditions();
			tuple.setValue(OpenFSMRSchema.ATT_SOFTWARE_PREDICTION_CONDITIONS, softwarePredictionConditions);
		}

		if (template.isSetInitLevelUnit()) {
			String initLevelUnit = template.getInitLevelUnit();
			tuple.setValue(OpenFSMRSchema.ATT_INIT_LEVEL_UNIT, initLevelUnit);
		}

		if (template.isSetInitLevelMin()) {
			double initLevelMin = template.getInitLevelMin();
			tuple.setValue(OpenFSMRSchema.ATT_INIT_LEVEL_MIN, initLevelMin);
		}

		if (template.isSetInitLevelMax()) {
			double initLevelMax = template.getInitLevelMax();
			tuple.setValue(OpenFSMRSchema.ATT_INIT_LEVEL_MAX, initLevelMax);
		}

		if (template.isSetTimeUnit()) {
			String timeUnit = template.getTimeUnit();
			tuple.setValue(OpenFSMRSchema.ATT_TIME_UNIT, timeUnit);
		}

		if (template.isSetTimeMin()) {
			double timeMin = template.getTimeMin();
			tuple.setValue(OpenFSMRSchema.ATT_TIME_MIN, timeMin);
		}

		if (template.isSetTimeMax()) {
			double timeMax = template.getTimeMax();
			tuple.setValue(OpenFSMRSchema.ATT_TIME_MAX, timeMax);
		}

		if (template.isSetTemperatureUnit()) {
			String temperatureUnit = template.getTemperatureUnit();
			tuple.setValue(OpenFSMRSchema.ATT_TEMP_UNIT, temperatureUnit);
		}

		if (template.isSetTemperatureMin()) {
			double temperatureMin = template.getTemperatureMin();
			tuple.setValue(OpenFSMRSchema.ATT_TEMP_MIN, temperatureMin);
		}

		if (template.isSetTemperatureMax()) {
			double temperatureMax = template.getTemperatureMax();
			tuple.setValue(OpenFSMRSchema.ATT_TEMP_MAX, temperatureMax);
		}

		if (template.isSetPhUnit()) {
			String phUnit = template.getPhUnit();
			tuple.setValue(OpenFSMRSchema.ATT_PH_UNIT, phUnit);
		}

		if (template.isSetPhMin()) {
			double phMin = template.getPhMin();
			tuple.setValue(OpenFSMRSchema.ATT_PH_MIN, phMin);
		}

		if (template.isSetPhMax()) {
			double phMax = template.getPhMax();
			tuple.setValue(OpenFSMRSchema.ATT_PH_MAX, phMax);
		}

		if (template.isSetAwUnit()) {
			String awUnit = template.getAwUnit();
			tuple.setValue(OpenFSMRSchema.ATT_AW_UNIT, awUnit);
		}

		if (template.isSetAwMin()) {
			double awMin = template.getAwMin();
			tuple.setValue(OpenFSMRSchema.ATT_AW_MIN, awMin);
		}

		if (template.isSetAwMax()) {
			double awMax = template.getAwMax();
			tuple.setValue(OpenFSMRSchema.ATT_AW_MAX, awMax);
		}

		if (template.isSetNaClUnit()) {
			String naclUnit = template.getNaClUnit();
			tuple.setValue(OpenFSMRSchema.ATT_NaCl_UNIT, naclUnit);
		}

		if (template.isSetNaClMin()) {
			double naclMin = template.getNaClMin();
			tuple.setValue(OpenFSMRSchema.ATT_NaCl_MIN, naclMin);
		}

		if (template.isSetNaClMax()) {
			double naclMax = template.getNaClMax();
			tuple.setValue(OpenFSMRSchema.ATT_NaCl_MAX, naclMax);
		}

		if (template.isSetAltaUnit()) {
			String altaUnit = template.getAltaUnit();
			tuple.setValue(OpenFSMRSchema.ATT_ALTA_UNIT, altaUnit);
		}

		if (template.isSetAltaMin()) {
			double altaMin = template.getAltaMin();
			tuple.setValue(OpenFSMRSchema.ATT_ALTA_MIN, altaMin);
		}

		if (template.isSetAltaMax()) {
			double altaMax = template.getAltaMax();
			tuple.setValue(OpenFSMRSchema.ATT_ALTA_MAX, altaMax);
		}

		if (template.isSetCO2Unit()) {
			String co2Unit = template.getCO2Unit();
			tuple.setValue(OpenFSMRSchema.ATT_CO2_UNIT, co2Unit);
		}

		if (template.isSetCO2Min()) {
			double co2Min = template.getCO2Min();
			tuple.setValue(OpenFSMRSchema.ATT_CO2_MIN, co2Min);
		}

		if (template.isSetCO2Max()) {
			double co2Max = template.getCO2Max();
			tuple.setValue(OpenFSMRSchema.ATT_CO2_MAX, co2Max);
		}

		if (template.isSetClO2Unit()) {
			String clo2Unit = template.getClO2Unit();
			tuple.setValue(OpenFSMRSchema.ATT_ClO2_UNIT, clo2Unit);
		}

		if (template.isSetClO2Min()) {
			double clo2Min = template.getClO2Min();
			tuple.setValue(OpenFSMRSchema.ATT_ClO2_MIN, clo2Min);
		}

		if (template.isSetClO2Max()) {
			double clo2Max = template.getClO2Max();
			tuple.setValue(OpenFSMRSchema.ATT_ClO2_MAX, clo2Max);
		}

		if (template.isSetEdtaUnit()) {
			String edtaUnit = template.getEdtaUnit();
			tuple.setValue(OpenFSMRSchema.ATT_EDTA_UNIT, edtaUnit);
		}

		if (template.isSetEdtaMin()) {
			double edtaMin = template.getEdtaMin();
			tuple.setValue(OpenFSMRSchema.ATT_EDTA_MIN, edtaMin);
		}

		if (template.isSetEdtaMax()) {
			double edtaMax = template.getEdtaMax();
			tuple.setValue(OpenFSMRSchema.ATT_EDTA_MAX, edtaMax);
		}

		if (template.isSetHClUnit()) {
			String hclUnit = template.getHClUnit();
			tuple.setValue(OpenFSMRSchema.ATT_HCl_UNIT, hclUnit);
		}

		if (template.isSetHClMin()) {
			double hclMin = template.getHClMin();
			tuple.setValue(OpenFSMRSchema.ATT_HCl_MIN, hclMin);
		}

		if (template.isSetHClMax()) {
			double hclMax = template.getHClMax();
			tuple.setValue(OpenFSMRSchema.ATT_HCl_MAX, hclMax);
		}

		if (template.isSetN2Unit()) {
			String n2Unit = template.getN2Unit();
			tuple.setValue(OpenFSMRSchema.ATT_N2_UNIT, n2Unit);
		}

		if (template.isSetN2Min()) {
			double n2Min = template.getN2Min();
			tuple.setValue(OpenFSMRSchema.ATT_N2_MIN, n2Min);
		}

		if (template.isSetN2Max()) {
			double n2Max = template.getN2Max();
			tuple.setValue(OpenFSMRSchema.ATT_N2_MAX, n2Max);
		}

		if (template.isSetO2Unit()) {
			String o2Unit = template.getO2Unit();
			tuple.setValue(OpenFSMRSchema.ATT_O2_UNIT, o2Unit);
		}

		if (template.isSetO2Min()) {
			double o2Min = template.getO2Min();
			tuple.setValue(OpenFSMRSchema.ATT_O2_MIN, o2Min);
		}

		if (template.isSetO2Max()) {
			double o2Max = template.getO2Max();
			tuple.setValue(OpenFSMRSchema.ATT_O2_MAX, o2Max);
		}

		if (template.isSetAceticAcidUnit()) {
			String aceticAcidUnit = template.getAceticAcidUnit();
			tuple.setValue(OpenFSMRSchema.ATT_ACETIC_ACID_UNIT, aceticAcidUnit);
		}

		if (template.isSetAceticAcidMin()) {
			double aceticAcidMin = template.getAceticAcidMin();
			tuple.setValue(OpenFSMRSchema.ATT_ACETIC_ACID_MIN, aceticAcidMin);
		}

		if (template.isSetAceticAcidMax()) {
			double aceticAcidMax = template.getAceticAcidMin();
			tuple.setValue(OpenFSMRSchema.ATT_ACETIC_ACID_MIN, aceticAcidMax);
		}

		if (template.isSetAdditives()) {
			String additives = template.getAdditives();
			tuple.setValue(OpenFSMRSchema.ATT_ADDITIVES, additives);
		}

		if (template.isSetAnaerobic()) {
			boolean anaerobic = template.getAnaerobic();
			tuple.setValue(OpenFSMRSchema.ATT_ANAEROBIC, anaerobic);
		}

		if (template.isSetAntimicrobialDippingTimeUnit()) {
			String antimicrobialDippingTimeUnit = template.getAntimicrobialDippingTimeUnit();
			tuple.setValue(OpenFSMRSchema.ATT_ANTIMICROBIAL_DIPPING_TIME_UNIT, antimicrobialDippingTimeUnit);
		}

		if (template.isSetAntimicrobialDippingTimeMin()) {
			double antimicrobialDippingTimeMin = template.getAntimicrobialDippingTimeMin();
			tuple.setValue(OpenFSMRSchema.ATT_ANTIMICROBIAL_DIPPING_TIME_MIN, antimicrobialDippingTimeMin);
		}

		if (template.isSetAntimicrobialDippingTimeMax()) {
			double antimicrobialDippingTimeMax = template.getAntimicrobialDippingTimeMin();
			tuple.setValue(OpenFSMRSchema.ATT_ANTIMICROBIAL_DIPPING_TIME_MIN, antimicrobialDippingTimeMax);
		}

		if (template.isSetApplePolyphenolUnit()) {
			String applePolyphenolUnit = template.getApplePolyphenolUnit();
			tuple.setValue(OpenFSMRSchema.ATT_APPLE_POLYPHENOL_UNIT, applePolyphenolUnit);
		}

		if (template.isSetApplePolyphenolMin()) {
			double applePolyphenolMin = template.getApplePolyphenolMin();
			tuple.setValue(OpenFSMRSchema.ATT_APPLE_POLYPHENOL_MIN, applePolyphenolMin);
		}

		if (template.isSetApplePolyphenolMax()) {
			double applePolyphenolMax = template.getApplePolyphenolMin();
			tuple.setValue(OpenFSMRSchema.ATT_APPLE_POLYPHENOL_MIN, applePolyphenolMax);
		}

		if (template.isSetAscorbiccAcidUnit()) {
			String ascorbiccAcidUnit = template.getAscorbiccAcidUnit();
			tuple.setValue(OpenFSMRSchema.ATT_ASCORBICC_ACID_UNIT, ascorbiccAcidUnit);
		}

		if (template.isSetAscorbiccAcidMin()) {
			double ascorbiccAcidMin = template.getAscorbiccAcidMin();
			tuple.setValue(OpenFSMRSchema.ATT_ASCORBICC_ACID_MIN, ascorbiccAcidMin);
		}

		if (template.isSetAscorbiccAcidMax()) {
			double ascorbiccAcidMax = template.getAscorbiccAcidMax();
			tuple.setValue(OpenFSMRSchema.ATT_ASCORBICC_ACID_MAX, ascorbiccAcidMax);
		}

		if (template.isSetAtrInduced()) {
			boolean atrInduced = template.getAtrInduced();
			tuple.setValue(OpenFSMRSchema.ATT_ATR_INDUCED, atrInduced);
		}

		if (template.isSetAttachmentTimeUnit()) {
			String attachmentTimeUnit = template.getAttachmentTimeUnit();
			tuple.setValue(OpenFSMRSchema.ATT_ATTACHMENT_TIME_UNIT, attachmentTimeUnit);
		}

		if (template.isSetAttachmentTimeMin()) {
			double attachmentTimeMin = template.getAttachmentTimeMin();
			tuple.setValue(OpenFSMRSchema.ATT_ATTACHMENT_TIME_MIN, attachmentTimeMin);
		}

		if (template.isSetAttachmentTimeMax()) {
			double attachmentTimeMax = template.getAttachmentTimeMax();
			tuple.setValue(OpenFSMRSchema.ATT_ATTACHMENT_TIME_MAX, attachmentTimeMax);
		}

		if (template.isSetBeanOilUnit()) {
			String beanOilUnit = template.getBeanOilUnit();
			tuple.setValue(OpenFSMRSchema.ATT_BEAN_OIL_UNIT, beanOilUnit);
		}

		if (template.isSetBeanOilMin()) {
			double beanOilMin = template.getBeanOilMin();
			tuple.setValue(OpenFSMRSchema.ATT_BEAN_OIL_MIN, beanOilMin);
		}

		if (template.isSetBeanOilMax()) {
			double beanOilMax = template.getBeanOilMin();
			tuple.setValue(OpenFSMRSchema.ATT_BEAN_OIL_MIN, beanOilMax);
		}

		if (template.isSetBenzoicAcidUnit()) {
			String benzoicAcidUnit = template.getBenzoicAcidUnit();
			tuple.setValue(OpenFSMRSchema.ATT_BENZOIC_ACID_UNIT, benzoicAcidUnit);
		}

		if (template.isSetBenzoicAcidMin()) {
			double benzoicAcidMin = template.getBenzoicAcidMin();
			tuple.setValue(OpenFSMRSchema.ATT_BENZOIC_ACID_MIN, benzoicAcidMin);
		}

		if (template.isSetBenzoicAcidMax()) {
			double benzoicAcidMax = template.getBenzoicAcidMin();
			tuple.setValue(OpenFSMRSchema.ATT_BENZOIC_ACID_MIN, benzoicAcidMax);
		}

		if (template.isSetBetaineUnit()) {
			String betaineUnit = template.getBetaineUnit();
			tuple.setValue(OpenFSMRSchema.ATT_BETAINE_UNIT, betaineUnit);
		}

		if (template.isSetBetaineMin()) {
			double betaineMin = template.getBetaineMin();
			tuple.setValue(OpenFSMRSchema.ATT_BETAINE_MIN, betaineMin);
		}

		if (template.isSetBetaineMax()) {
			double betaineMax = template.getBetaineMax();
			tuple.setValue(OpenFSMRSchema.ATT_BETAINE_MAX, betaineMax);
		}

		if (template.isSetCarvacrolUnit()) {
			String carvacrolUnit = template.getCarvacrolUnit();
			tuple.setValue(OpenFSMRSchema.ATT_CARVACROL_UNIT, carvacrolUnit);
		}

		if (template.isSetCarvacrolMin()) {
			double carvacrolMin = template.getCarvacrolMin();
			tuple.setValue(OpenFSMRSchema.ATT_CARVACROL_MIN, carvacrolMin);
		}

		if (template.isSetCarvacrolMax()) {
			double carvacrolMax = template.getCarvacrolMax();
			tuple.setValue(OpenFSMRSchema.ATT_CARVACROL_MAX, carvacrolMax);
		}

		if (template.isSetChitosanUnit()) {
			String chitosanUnit = template.getChitosanUnit();
			tuple.setValue(OpenFSMRSchema.ATT_CHITOSAN_UNIT, chitosanUnit);
		}

		if (template.isSetChitosanMin()) {
			double chitosanMin = template.getChitosanMin();
			tuple.setValue(OpenFSMRSchema.ATT_CHITOSAN_MIN, chitosanMin);
		}

		if (template.isSetChitosanMax()) {
			double chitosanMax = template.getChitosanMax();
			tuple.setValue(OpenFSMRSchema.ATT_CHITOSAN_MAX, chitosanMax);
		}

		if (template.isSetCinnamaldehydeUnit()) {
			String cinnamaldehydeUnit = template.getCinnamaldehydeUnit();
			tuple.setValue(OpenFSMRSchema.ATT_CINNAMALDEHYDE_UNIT, cinnamaldehydeUnit);
		}

		if (template.isSetCinnamaldehydeMin()) {
			double cinnamaldehydeMin = template.getCinnamaldehydeMin();
			tuple.setValue(OpenFSMRSchema.ATT_CINNAMALDEHYDE_MIN, cinnamaldehydeMin);
		}

		if (template.isSetCinnamaldehydeMax()) {
			double cinnamaldehydeMax = template.getCinnamaldehydeMax();
			tuple.setValue(OpenFSMRSchema.ATT_CINNAMALDEHYDE_MAX, cinnamaldehydeMax);
		}

		if (template.isSetCured()) {
			boolean cured = template.getCured();
			int curedAsInt = cured ? 1 : 0;
			tuple.setValue(OpenFSMRSchema.ATT_CURED, curedAsInt);
		}

		if (template.isSetCut()) {
			boolean cut = template.getCut();
			int cutAsInt = cut ? 1 : 0;
			tuple.setValue(OpenFSMRSchema.ATT_CUT, cutAsInt);
		}

		if (template.isSetDesiredReductionUnit()) {
			String desiredReductionUnit = template.getDesiredReductionUnit();
			tuple.setValue(OpenFSMRSchema.ATT_DESIRED_REDUCTION_UNIT, desiredReductionUnit);
		}

		if (template.isSetDesiredReductionMin()) {
			double desiredReductionMin = template.getDesiredReductionMin();
			tuple.setValue(OpenFSMRSchema.ATT_DESIRED_REDUCTION_MIN, desiredReductionMin);
		}

		if (template.isSetDesiredReductionMax()) {
			double desiredReductionMax = template.getDesiredReductionMax();
			tuple.setValue(OpenFSMRSchema.ATT_DESIRED_REDUCTION_MAX, desiredReductionMax);
		}

		if (template.isSetDiaceticAcidUnit()) {
			String diaceticAcidUnit = template.getDiaceticAcidUnit();
			tuple.setValue(OpenFSMRSchema.ATT_DIACETIC_ACID_UNIT, diaceticAcidUnit);
		}

		if (template.isSetDiaceticAcidMin()) {
			double diaceticAcidMin = template.getDiaceticAcidMin();
			tuple.setValue(OpenFSMRSchema.ATT_DIACETIC_ACID_MIN, diaceticAcidMin);
		}

		if (template.isSetDiaceticAcidMax()) {
			double diaceticAcidMax = template.getDiaceticAcidMax();
			tuple.setValue(OpenFSMRSchema.ATT_DIACETIC_ACID_MAX, diaceticAcidMax);
		}

		if (template.isSetDisaccharideUnit()) {
			String disaccharideUnit = template.getDisaccharideUnit();
			tuple.setValue(OpenFSMRSchema.ATT_DISACCHARIDE_UNIT, disaccharideUnit);
		}

		if (template.isSetDisaccharideMin()) {
			double disaccharideMin = template.getDisaccharideMin();
			tuple.setValue(OpenFSMRSchema.ATT_DISACCHARIDE_MIN, disaccharideMin);
		}

		if (template.isSetDisaccharideMax()) {
			double disaccharideMax = template.getDisaccharideMax();
			tuple.setValue(OpenFSMRSchema.ATT_DISACCHARIDE_MIN, disaccharideMax);
		}

		if (template.isSetDried()) {
			boolean dried = template.getDried();
			tuple.setValue(OpenFSMRSchema.ATT_DRIED, dried);
		}

		if (template.isSetEthanolUnit()) {
			String ethanolUnit = template.getEthanolUnit();
			tuple.setValue(OpenFSMRSchema.ATT_ETHANOL_UNIT, ethanolUnit);
		}

		if (template.isSetEthanolMin()) {
			double ethanolMin = template.getEthanolMin();
			tuple.setValue(OpenFSMRSchema.ATT_ETHANOL_MIN, ethanolMin);
		}

		if (template.isSetEthanolMax()) {
			double ethanolMax = template.getEthanolMax();
			tuple.setValue(OpenFSMRSchema.ATT_ETHANOL_MAX, ethanolMax);
		}

		if (template.isSetExpInoculated()) {
			boolean expInoculated = template.getExpInoculated();
			tuple.setValue(OpenFSMRSchema.ATT_EXP_INOCULATED, expInoculated);
		}

		if (template.isSetFatUnit()) {
			String fatUnit = template.getFatUnit();
			tuple.setValue(OpenFSMRSchema.ATT_FAT_UNIT, fatUnit);
		}

		if (template.isSetFatMin()) {
			double fatMin = template.getFatMin();
			tuple.setValue(OpenFSMRSchema.ATT_FAT_MIN, fatMin);
		}

		if (template.isSetFatMax()) {
			double fatMax = template.getFatMax();
			tuple.setValue(OpenFSMRSchema.ATT_FAT_MAX, fatMax);
		}

		if (template.isSetFrozen()) {
			boolean frozen = template.getFrozen();
			int frozenAsInt = frozen ? 1 : 0;
			tuple.setValue(OpenFSMRSchema.ATT_FROZEN, frozenAsInt);
		}

		if (template.isSetFructoseUnit()) {
			String fructoseUnit = template.getFructoseUnit();
			tuple.setValue(OpenFSMRSchema.ATT_FRUCTOSE_UNIT, fructoseUnit);
		}

		if (template.isSetFructoseMin()) {
			double fructoseMin = template.getFructoseMin();
			tuple.setValue(OpenFSMRSchema.ATT_FRUCTOSE_MIN, fructoseMin);
		}

		if (template.isSetFructoseMax()) {
			double fructoseMax = template.getFructoseMax();
			tuple.setValue(OpenFSMRSchema.ATT_FRUCTOSE_MAX, fructoseMax);
		}

		if (template.isSetGelMicrostructureUnit()) {
			String gelMicrostructureUnit = template.getGelMicrostructureUnit();
			tuple.setValue(OpenFSMRSchema.ATT_GEL_MICROSTRUCTURE_UNIT, gelMicrostructureUnit);
		}

		if (template.isSetGelMicrostructureMin()) {
			double gelMicrostructureMin = template.getGelMicrostructureMin();
			tuple.setValue(OpenFSMRSchema.ATT_GEL_MICROSTRUCTURE_MIN, gelMicrostructureMin);
		}

		if (template.isSetGelMicrostructureMax()) {
			double gelMicrostructureMax = template.getGelMicrostructureMax();
			tuple.setValue(OpenFSMRSchema.ATT_GEL_MICROSTRUCTURE_MAX, gelMicrostructureMax);
		}

		if (template.isSetGlucoseUnit()) {
			String glucoseUnit = template.getGlucoseUnit();
			tuple.setValue(OpenFSMRSchema.ATT_GLUCOSE_UNIT, glucoseUnit);
		}

		if (template.isSetGlucoseMin()) {
			double glucoseMin = template.getGlucoseMin();
			tuple.setValue(OpenFSMRSchema.ATT_GLUCOSE_MIN, glucoseMin);
		}

		if (template.isSetGlucoseMax()) {
			double glucoseMax = template.getGlucoseMax();
			tuple.setValue(OpenFSMRSchema.ATT_GLUCOSE_MAX, glucoseMax);
		}

		if (template.isSetGlycerolUnit()) {
			String glycerolUnit = template.getGlycerolUnit();
			tuple.setValue(OpenFSMRSchema.ATT_GLYCEROL_UNIT, glycerolUnit);
		}

		if (template.isSetGlycerolMin()) {
			double glycerolMin = template.getGlycerolMin();
			tuple.setValue(OpenFSMRSchema.ATT_GLYCEROL_MIN, glycerolMin);
		}

		if (template.isSetGlycerolMax()) {
			double glycerolMax = template.getGlycerolMax();
			tuple.setValue(OpenFSMRSchema.ATT_GLYCEROL_MAX, glycerolMax);
		}

		if (template.isSetGreenTeaGroundPowderUnit()) {
			String greenTeaGroundPowderUnit = template.getGreenTeaGroundPowderUnit();
			tuple.setValue(OpenFSMRSchema.ATT_GREEN_TEA_GROUND_POWDER_UNIT, greenTeaGroundPowderUnit);
		}

		if (template.isSetGreenTeaGroundPowderMin()) {
			double greenTeaGroundPowderMin = template.getGreenTeaGroundPowderMin();
			tuple.setValue(OpenFSMRSchema.ATT_GREEN_TEA_GROUND_POWDER_MIN, greenTeaGroundPowderMin);
		}

		if (template.isSetGreenTeaGroundPowderMax()) {
			double greenTeaGroundPowderMax = template.getGreenTeaGroundPowderMin();
			tuple.setValue(OpenFSMRSchema.ATT_GREEN_TEA_GROUND_POWDER_MIN, greenTeaGroundPowderMax);
		}

		if (template.isSetGreenTeaLeafUnit()) {
			String greenTeaLeafUnit = template.getGreenTeaLeafUnit();
			tuple.setValue(OpenFSMRSchema.ATT_GREEN_TEA_LEAF_UNIT, greenTeaLeafUnit);
		}

		if (template.isSetGreenTeaLeafMin()) {
			double greenTeaLeafMin = template.getGreenTeaLeafMin();
			tuple.setValue(OpenFSMRSchema.ATT_GREEN_TEA_LEAF_MIN, greenTeaLeafMin);
		}

		if (template.isSetGreenTeaLeafMax()) {
			double greenTeaLeafMax = template.getGreenTeaLeafMax();
			tuple.setValue(OpenFSMRSchema.ATT_GREEN_TEA_LEAF_MAX, greenTeaLeafMax);
		}

		if (template.isSetHeated()) {
			boolean heated = template.getHeated();
			int heatedAsInt = heated ? 1 : 0;
			tuple.setValue(OpenFSMRSchema.ATT_HEATED, heatedAsInt);
		}

		if (template.isSetHexosesUnit()) {
			String hexosesUnit = template.getHexosesUnit();
			tuple.setValue(OpenFSMRSchema.ATT_HEXOSES_UNIT, hexosesUnit);
		}

		if (template.isSetHexosesMin()) {
			double hexosesMin = template.getHexosesMin();
			tuple.setValue(OpenFSMRSchema.ATT_HEXOSES_MIN, hexosesMin);
		}

		if (template.isSetHexosesMax()) {
			double hexosesMax = template.getHexosesMax();
			tuple.setValue(OpenFSMRSchema.ATT_HEXOSES_MAX, hexosesMax);
		}

		if (template.isSetIndigenous()) {
			boolean indigenous = template.getIndigenous();
			int indigenousAsInt = indigenous ? 1 : 0;
			tuple.setValue(OpenFSMRSchema.ATT_INDIGENOUS, indigenousAsInt);
		}

		if (template.isSetInitLevelHistamineUnit()) {
			String initLevelHistamineUnit = template.getInitLevelHistamineUnit();
			tuple.setValue(OpenFSMRSchema.ATT_INIT_LEVEL_HISTAMINE_UNIT, initLevelHistamineUnit);
		}

		if (template.isSetInitLevelHistamineMin()) {
			double initLevelHistamineMin = template.getInitLevelHistamineMin();
			tuple.setValue(OpenFSMRSchema.ATT_INIT_LEVEL_HISTAMINE_MIN, initLevelHistamineMin);
		}

		if (template.isSetInitLevelHistamineMax()) {
			double initLevelHistamineMax = template.getInitLevelHistamineMax();
			tuple.setValue(OpenFSMRSchema.ATT_INIT_LEVEL_HISTAMINE_MIN, initLevelHistamineMax);
		}

		if (template.isSetInjuredUnit()) {
			String injuredUnit = template.getInjuredUnit();
			tuple.setValue(OpenFSMRSchema.ATT_INJURED_UNIT, injuredUnit);
		}

		if (template.isSetInjuredMin()) {
			double injuredMin = template.getInjuredMin();
			tuple.setValue(OpenFSMRSchema.ATT_INJURED_MIN, injuredMin);
		}

		if (template.isSetInjuredMax()) {
			double injuredMax = template.getInjuredMax();
			tuple.setValue(OpenFSMRSchema.ATT_INJURED_MAX, injuredMax);
		}

		if (template.isSetIrradiated()) {
			boolean irradiated = template.getIrradiated();
			int irradiatedAsInt = irradiated ? 1 : 0;
			tuple.setValue(OpenFSMRSchema.ATT_IRRADIATED, irradiatedAsInt);
		}

		if (template.isSetIrradiationUnit()) {
			String irradiationUnit = template.getIrradiationUnit();
			tuple.setValue(OpenFSMRSchema.ATT_IRRADIATION_UNIT, irradiationUnit);
		}

		if (template.isSetIrradiationMin()) {
			double irradiationMin = template.getIrradiationMin();
			tuple.setValue(OpenFSMRSchema.ATT_IRRADIATION_MIN, irradiationMin);
		}

		if (template.isSetIrradiationMax()) {
			double irradiationMax = template.getIrradiationMax();
			tuple.setValue(OpenFSMRSchema.ATT_IRRADIATION_MAX, irradiationMax);
		}

		if (template.isSetLacticAcidUnit()) {
			String lacticAcidUnit = template.getLacticAcidUnit();
			tuple.setValue(OpenFSMRSchema.ATT_LACTIC_ACID_UNIT, lacticAcidUnit);
		}

		if (template.isSetLacticAcidMin()) {
			double lacticAcidMin = template.getLacticAcidMin();
			tuple.setValue(OpenFSMRSchema.ATT_LACTIC_ACID_MIN, lacticAcidMin);
		}

		if (template.isSetLacticAcidMax()) {
			double lacticAcidMax = template.getLacticAcidMax();
			tuple.setValue(OpenFSMRSchema.ATT_LACTIC_ACID_MAX, lacticAcidMax);
		}

		if (template.isSetLacticBacteriaFermented()) {
			boolean lacticBacteriaFermented = template.getLacticBacteriaFermented();
			int lacticBacteriaFermentedAsInt = lacticBacteriaFermented ? 1 : 0;
			tuple.setValue(OpenFSMRSchema.ATT_LACTIC_BACTERIA_FERMENTED, lacticBacteriaFermentedAsInt);
		}

		if (template.isSetLauricidinUnit()) {
			String lauricidinUnit = template.getLauricidinUnit();
			tuple.setValue(OpenFSMRSchema.ATT_LAURICIDIN_UNIT, lauricidinUnit);
		}

		if (template.isSetLauricidinMin()) {
			double lauricidinMin = template.getLauricidinMin();
			tuple.setValue(OpenFSMRSchema.ATT_LAURICIDIN_MIN, lauricidinMin);
		}

		if (template.isSetLauricidinMax()) {
			double lauricidinMax = template.getLauricidinMax();
			tuple.setValue(OpenFSMRSchema.ATT_LAURICIDIN_MAX, lauricidinMax);
		}

		if (template.isSetMalicAcidUnit()) {
			String malicAcidUnit = template.getMalicAcidUnit();
			tuple.setValue(OpenFSMRSchema.ATT_MALIC_ACID_UNIT, malicAcidUnit);
		}

		if (template.isSetMalicAcidMin()) {
			double malicAcidMin = template.getMalicAcidMin();
			tuple.setValue(OpenFSMRSchema.ATT_MALIC_ACID_MIN, malicAcidMin);
		}

		if (template.isSetMalicAcidMax()) {
			double malicAcidMax = template.getMalicAcidMax();
			tuple.setValue(OpenFSMRSchema.ATT_MALIC_ACID_MAX, malicAcidMax);
		}

		return tuple;
	}
}
