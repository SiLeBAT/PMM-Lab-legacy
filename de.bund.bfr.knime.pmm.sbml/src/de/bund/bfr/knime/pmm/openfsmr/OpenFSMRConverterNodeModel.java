package de.bund.bfr.knime.pmm.openfsmr;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

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
import org.sbml.jsbml.Constraint;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.ExperimentalDataFile;
import de.bund.bfr.pmf.file.ManualSecondaryModelFile;
import de.bund.bfr.pmf.file.ManualTertiaryModelFile;
import de.bund.bfr.pmf.file.OneStepSecondaryModelFile;
import de.bund.bfr.pmf.file.OneStepTertiaryModelFile;
import de.bund.bfr.pmf.file.PMFMetadataNode;
import de.bund.bfr.pmf.file.PrimaryModelWDataFile;
import de.bund.bfr.pmf.file.PrimaryModelWODataFile;
import de.bund.bfr.pmf.file.TwoStepSecondaryModelFile;
import de.bund.bfr.pmf.file.TwoStepTertiaryModelFile;
import de.bund.bfr.pmf.model.ExperimentalData;
import de.bund.bfr.pmf.model.ManualSecondaryModel;
import de.bund.bfr.pmf.model.ManualTertiaryModel;
import de.bund.bfr.pmf.model.OneStepSecondaryModel;
import de.bund.bfr.pmf.model.OneStepTertiaryModel;
import de.bund.bfr.pmf.model.PrimaryModelWData;
import de.bund.bfr.pmf.model.PrimaryModelWOData;
import de.bund.bfr.pmf.model.TwoStepSecondaryModel;
import de.bund.bfr.pmf.model.TwoStepTertiaryModel;
import de.bund.bfr.pmf.numl.ConcentrationOntology;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.numl.ResultComponent;
import de.bund.bfr.pmf.sbml.Limits;
import de.bund.bfr.pmf.sbml.LimitsConstraint;
import de.bund.bfr.pmf.sbml.Metadata;
import de.bund.bfr.pmf.sbml.MetadataAnnotation;
import de.bund.bfr.pmf.sbml.ModelRule;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.bund.bfr.pmf.sbml.SBMLFactory;
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

		List<FSMRTemplate> templates = new LinkedList<>();

		switch (modelType) {
		case EXPERIMENTAL_DATA:
			// Obtain an OpenFSMR template per data file
			for (ExperimentalData ed : ExperimentalDataFile.read(filepath)) {
				NuMLDocument doc = ed.getDoc();
				FSMRTemplate template = processData(doc);
				templates.add(template);
			}
			break;
		case PRIMARY_MODEL_WDATA:
			// Obtain an OpenFSMR template per primary model
			for (PrimaryModelWData pm : PrimaryModelWDataFile.read(filepath)) {
				SBMLDocument primModelDoc = pm.getModelDoc();
				FSMRTemplate template = processPrimaryModelWithMicrobialData(primModelDoc);
				templates.add(template);
			}
			break;
		case PRIMARY_MODEL_WODATA:
			// Obtain an OpenFSMR template per primary model
			for (PrimaryModelWOData pm : PrimaryModelWODataFile.read(filepath)) {
				SBMLDocument primModelDoc = pm.getDoc();
				FSMRTemplate template = processPrimaryModelWithMicrobialData(primModelDoc);
				templates.add(template);
			}
			break;
		case TWO_STEP_SECONDARY_MODEL:
			// Obtain an OpenFSMR template per secondary model
			for (TwoStepSecondaryModel sm : TwoStepSecondaryModelFile.read(filepath)) {
				// Gets the model from the first primary model
				SBMLDocument primModelDoc = sm.getPrimModels().get(0).getModelDoc();
				FSMRTemplate template = processPrimaryModelWithMicrobialData(primModelDoc);
				templates.add(template);
			}
			break;
		case ONE_STEP_SECONDARY_MODEL:
			// Obtain an OpenFSMR template per secondary model
			for (OneStepSecondaryModel sm : OneStepSecondaryModelFile.read(filepath)) {
				SBMLDocument primModelDoc = sm.getModelDoc();
				FSMRTemplate template = processPrimaryModelWithMicrobialData(primModelDoc);
				templates.add(template);
			}
			break;
		case MANUAL_SECONDARY_MODEL:
			// Obtain an OpenFSMR template per secondary model
			for (ManualSecondaryModel sm : ManualSecondaryModelFile.read(filepath)) {
				SBMLDocument doc = sm.getDoc();
				FSMRTemplate template = processPrimaryModelWithoutMicrobialData(doc);
				templates.add(template);
			}
			break;
		case TWO_STEP_TERTIARY_MODEL:
			// Obtain a OpenFSMR template per tertiary model
			for (TwoStepTertiaryModel tm : TwoStepTertiaryModelFile.read(filepath)) {
				SBMLDocument tertDoc = tm.getTertDoc();
				FSMRTemplate template = processPrimaryModelWithMicrobialData(tertDoc);
				templates.add(template);
			}
			break;
		case ONE_STEP_TERTIARY_MODEL:
			// Obtain a OpenFSMR template per tertiary model
			for (OneStepTertiaryModel tm : OneStepTertiaryModelFile.read(filepath)) {
				SBMLDocument tertDoc = tm.getTertiaryDoc();
				FSMRTemplate template = processPrimaryModelWithMicrobialData(tertDoc);
				templates.add(template);
			}
			break;
		case MANUAL_TERTIARY_MODEL:
			// Obtain a OpenFSMR template per tertiary model
			for (ManualTertiaryModel tm : ManualTertiaryModelFile.read(filepath)) {
				SBMLDocument tertDoc = tm.getTertiaryDoc();
				FSMRTemplate template = processPrimaryModelWithMicrobialData(tertDoc);
				templates.add(template);
			}
			break;
		}

		DataTableSpec tableSpec = new OpenFSMRSchema().createSpec();
		BufferedDataContainer container = exec.createDataContainer(tableSpec);
		// Adds templates to the container
		for (FSMRTemplate template : templates) {
			KnimeTuple tuple = createTupleFromTemplate(template);
			container.addRowToTable(tuple);

			// Updates progress bar
			float progress = container.size() / templates.size();
			exec.setProgress(progress);
		}
		container.close();
		BufferedDataTable[] tables = { container.getTable() };
		return tables;
	}

	private static FSMRTemplate processData(NuMLDocument doc) {
		FSMRTemplate template = new FSMRTemplateImpl();

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

		return template;
	}

	private static FSMRTemplate processPrimaryModelWithMicrobialData(SBMLDocument doc) {
		FSMRTemplate template = new FSMRTemplateImpl();
		Model model = doc.getModel();

		template.setModelId(model.getId());

		if (model.isSetName()) {
			String modelName = model.getName();
			template.setModelName(modelName);
		}

		// PMF organism
		PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
		String speciesName = species.getName();
		template.setOrganismName(speciesName);
		if (species.isSetDetail()) {
			String speciesDetail = species.getDetail();
			template.setOrganismDetails(speciesDetail);
		}

		// PMF environment
		PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
		String compartmentName = compartment.getName();
		template.setMatrixName(compartmentName);
		if (compartment.isSetDetail()) {
			String matrixDetail = compartment.getDetail();
			template.setMatrixDetails(matrixDetail);
		}

		Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();

		// Sets creator
		if (metadata.isSetGivenName()) {
			String creatorGivenName = metadata.getGivenName();
			template.setCreator(creatorGivenName);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);

		// Sets created date
		if (metadata.isSetCreatedDate()) {
			String createdDateAsString = metadata.getCreatedDate();
			try {
				Date createdDate = dateFormat.parse(createdDateAsString);
				template.setCreatedDate(createdDate);
			} catch (ParseException e) {
				System.err.println(createdDateAsString + " is not a valid date");
				e.printStackTrace();
			}
		}

		// Sets modified date
		if (metadata.isSetModifiedDate()) {
			String modifiedDateAsString = metadata.getModifiedDate();
			try {
				Date modifiedDate = dateFormat.parse(modifiedDateAsString);
				template.setModifiedDate(modifiedDate);
			} catch (ParseException e) {
				System.err.println(modifiedDateAsString + " is not a valid date");
				e.printStackTrace();
			}
		}

		// Sets model rights
		if (metadata.isSetRights()) {
			String rights = metadata.getRights();
			template.setRights(rights);
		}

		if (model.isSetNotes()) {
			try {
				String notes = model.getNotesString();
				template.setNotes(notes);
			} catch (XMLStreamException e) {
				System.err.println("Error accesing the notes of " + model);
				e.printStackTrace();
			}
		}

		// Sets model type
		if (metadata.isSetType()) {
			template.setModelType(metadata.getType());
		}

		// Sets model subject
		ModelRule modelRule = new ModelRule((AssignmentRule) model.getRule(0));
		ModelClass modelSubject = modelRule.getModelClass();
		template.setModelSubject(modelSubject);

		if (species.getSpecies().isSetUnits()) {
			String depUnitID = species.getSpecies().getUnits();

			// Sets dependent variable unit
			String depUnitName = model.getUnitDefinition(depUnitID).getName();
			template.setDependentVariableUnit(depUnitName);

			// Sets dependent variable
			if (!depUnitID.equals("dimensionless")) {
				String depUnitCategory = DBUnits.getDBUnits().get(depUnitName).getKind_of_property_quantity();
				template.setDependentVariable(depUnitCategory);
			}

			// Sets dependent variable min & max
			for (Constraint constraint : model.getListOfConstraints()) {
				LimitsConstraint lc = new LimitsConstraint(constraint);
				Limits lcLimits = lc.getLimits();
				if (lcLimits.getVar().equals(depUnitID)) {
					Double min = lcLimits.getMin();
					if (min != null) {
						template.setDependentVariableMin(min);
					}
					Double max = lcLimits.getMax();
					if (max != null) {
						template.setDependentVariableMax(max);
					}

					break;
				}
			}
		}

		for (Parameter parameter : model.getListOfParameters()) {
			if (!parameter.isConstant() && parameter.isSetUnits()) {
				String unitID = parameter.getUnits();

				// Sets independent variable unit
				String unitName = model.getUnitDefinition(unitID).getName();
				template.setIndependentVariable(unitName);

				// Sets independent variable
				if (!unitID.equals("dimensionless")) {
					String unitCategory = DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity();
					template.setIndependentVariable(unitCategory);
				}

				// Sets dependent variable min & max
				for (Constraint constraint : model.getListOfConstraints()) {
					LimitsConstraint lc = new LimitsConstraint(constraint);
					Limits lcLimits = lc.getLimits();
					if (lcLimits.getVar().equals(unitID)) {
						Double min = lcLimits.getMin();
						if (min != null) {
							template.setDependentVariableMin(min);
						}
						Double max = lcLimits.getMax();
						if (max != null) {
							template.setDependentVariableMax(max);
						}

						break;
					}
				}

				// TODO: So far it's keeping only the first independent var it
				// found, it should be able to keep the rest as well
				break;
			}
		}

		return template;
	}

	private static FSMRTemplate processPrimaryModelWithoutMicrobialData(SBMLDocument doc) {

		FSMRTemplate template = new FSMRTemplateImpl();

		Model model = doc.getModel();

		template.setModelId(model.getId());

		if (model.isSetName()) {
			String modelName = model.getName();
			template.setModelName(modelName);
		}

		Metadata metadata = new MetadataAnnotation(doc.getAnnotation()).getMetadata();

		// Sets creator
		if (metadata.isSetGivenName()) {
			String creatorGivenName = metadata.getGivenName();
			template.setCreator(creatorGivenName);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);

		// Sets created date
		if (metadata.isSetCreatedDate()) {
			String createdDateAsString = metadata.getCreatedDate();
			try {
				Date createdDate = dateFormat.parse(createdDateAsString);
				template.setCreatedDate(createdDate);
			} catch (ParseException e) {
				System.err.println(createdDateAsString + " is not a valid date");
				e.printStackTrace();
			}
		}

		// Sets modified date
		if (metadata.isSetModifiedDate()) {
			String modifiedDateAsString = metadata.getModifiedDate();
			try {
				Date modifiedDate = dateFormat.parse(modifiedDateAsString);
				template.setModifiedDate(modifiedDate);
			} catch (ParseException e) {
				System.err.println(modifiedDateAsString + " is not a valid date");
				e.printStackTrace();
			}
		}

		// Sets model rights
		if (metadata.isSetRights()) {
			String rights = metadata.getRights();
			template.setRights(rights);
		}

		// Sets model type
		if (metadata.isSetType()) {
			template.setModelType(metadata.getType());
		}

		// Sets model subject
		ModelRule modelRule = new ModelRule((AssignmentRule) model.getRule(0));
		ModelClass modelSubject = modelRule.getModelClass();
		template.setModelSubject(modelSubject);

		// Sets model notes
		if (model.isSetNotes()) {
			try {
				String notes = model.getNotesString();
				template.setNotes(notes);
			} catch (XMLStreamException e) {
				System.err.println("Error accesing the notes of " + model);
				e.printStackTrace();
			}
		}

		ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
		String depName = rule.getRule().getVariable();
		Parameter depParam = model.getParameter(depName);

		if (depParam.isSetUnits()) {
			// Sets dependent variable unit
			String unitID = depParam.getUnits();
			String unitName = model.getUnitDefinition(unitID).getName();
			template.setDependentVariableUnit(unitName);

			// Sets dependent variable
			Map<String, UnitsFromDB> dbUnits = DBUnits.getDBUnits();
			if (dbUnits.containsKey(unitName)) {
				String unitCategory = dbUnits.get(unitName).getKind_of_property_quantity();
				template.setDependentVariable(unitCategory);
			}

			// Sets dependent variable min & max
			for (Constraint contraint : model.getListOfConstraints()) {
				LimitsConstraint lc = new LimitsConstraint(contraint);
				Limits lcLimits = lc.getLimits();
				if (lcLimits.getVar().equals(depParam.getId())) {
					Double min = lcLimits.getMin();
					if (min != null) {
						template.setDependentVariableMin(min);
					}
					Double max = lcLimits.getMax();
					if (max != null) {
						template.setDependentVariableMax(max);
					}
					break;
				}
			}
		}

		return template;
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

		if (template.isSetMatrixName()) {
			String matrixName = template.getMatrixName();
			tuple.setValue(OpenFSMRSchema.ATT_ENVIRONMENT_NAME, matrixName);
		}

		if (template.isSetMatrixDetails()) {
			String matrixDetail = template.getMatrixDetails();
			tuple.setValue(OpenFSMRSchema.ATT_ENVIRONMENT_DETAIL, matrixDetail);
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

		return tuple;
	}
}

// class Util {
//
// public static KnimeTuple createTuple(SBMLDocument doc) {
// // Add cells to the row
// KnimeTuple tuple = new KnimeTuple(new OpenFSMRSchema());
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
//
// return tuple;
// }
//
// }
