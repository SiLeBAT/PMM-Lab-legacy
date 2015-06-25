package de.bund.bfr.knime.pmm.sbmlreader;

import groovy.util.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.file.ExperimentalDataFile;
import de.bund.bfr.knime.pmm.file.ManualSecondaryModelFile;
import de.bund.bfr.knime.pmm.file.OneStepSecondaryModelFile;
import de.bund.bfr.knime.pmm.file.PrimaryModelWDataFile;
import de.bund.bfr.knime.pmm.file.PrimaryModelWODataFile;
import de.bund.bfr.knime.pmm.file.RawDataFile;
import de.bund.bfr.knime.pmm.file.TwoStepSecondaryModelFile;
import de.bund.bfr.knime.pmm.model.ExperimentalData;
import de.bund.bfr.knime.pmm.model.ManualSecondaryModel;
import de.bund.bfr.knime.pmm.model.OneStepSecondaryModel;
import de.bund.bfr.knime.pmm.model.PrimaryModelWData;
import de.bund.bfr.knime.pmm.model.PrimaryModelWOData;
import de.bund.bfr.knime.pmm.model.TwoStepSecondaryModel;
import de.bund.bfr.knime.pmm.sbmlutil.Agent;
import de.bund.bfr.knime.pmm.sbmlutil.Coefficient;
import de.bund.bfr.knime.pmm.sbmlutil.DataFile;
import de.bund.bfr.knime.pmm.sbmlutil.Limits;
import de.bund.bfr.knime.pmm.sbmlutil.LimitsConstraint;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;
import de.bund.bfr.knime.pmm.sbmlutil.Model1Annotation;
import de.bund.bfr.knime.pmm.sbmlutil.Model1Rule;
import de.bund.bfr.knime.pmm.sbmlutil.Model2Annotation;
import de.bund.bfr.knime.pmm.sbmlutil.Model2Rule;
import de.bund.bfr.knime.pmm.sbmlutil.SecIndep;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.OntologyTerm;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

/**
 * This is the model implementation of SBMLReader.
 * 
 * Author: Miguel de Alba Aparicio (malba@optimumquality.es)
 */
public class SBMLReaderNodeModel extends NodeModel {

	// configuration keys
	public static final String CFGKEY_FILE = "filename";
	// defaults for persistent state
	private static final String DEFAULT_FILE = "c:/temp/foo.xml";

	// persistent state
	private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE,
			DEFAULT_FILE);

	/**
	 * Constructor for the node model.
	 */
	protected SBMLReaderNodeModel() {
		// 0 input ports and 1 input port
		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		BufferedDataTable[] table = null;
		table = loadPMF(exec);
		return table;
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
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {
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
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		filename.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

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
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

	// Load PMF file
	private BufferedDataTable[] loadPMF(final ExecutionContext exec)
			throws Exception {
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

		BufferedDataContainer container = null;
		if (modelType.equals("Experimental data")) {
			container = parseExperimentalDataPMF(filepath, exec);
		} else if (modelType.equals("Primary model with data")) {
			container = parsePrimaryModelWDataPMF(filepath, exec);
		} else if (modelType.equals("Primary model without data")) {
			container = parsePrimaryModelWODataPMF(filepath, exec);
		} else if (modelType.equals("Two step secondary model")) {
			container = parseTwoStepSecondaryModelPMF(filepath, exec);
		} else if (modelType.equals("One step secondary model")) {
			container = parseOneStepSecondaryModelPMF(filepath, exec);
		} else if (modelType.equals("Manual secondary model")) {
			container = parseManualSecondaryModelPMF(filepath, exec);
		}
		// TODO: Add support for tertiary models

		BufferedDataTable[] table = { container.getTable() };
		return table;
	}

	private BufferedDataContainer parseExperimentalDataPMF(String filepath,
			ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec spec = SchemaFactory.createDataSchema().createSpec();
		BufferedDataContainer container = exec.createDataContainer(spec);

		// Reads in experimental data from file
		List<ExperimentalData> eds = ExperimentalDataFile.read(filepath);
		
		// Creates tuples and adds them to the container
		for (ExperimentalData ed : eds) {
			KnimeTuple tuple = ExperimentalDataParser.parseDocument(ed
					.getNuMLDocument());
			container.addRowToTable(tuple);
			exec.setProgress((float) container.size() / eds.size());
		}

		container.close();
		return container;
	}

	private BufferedDataContainer parsePrimaryModelWDataPMF(String filepath,
			ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec spec = SchemaFactory.createM1DataSchema().createSpec();
		BufferedDataContainer container = exec.createDataContainer(spec);

		// Reads in models from file
		List<PrimaryModelWData> models = PrimaryModelWDataFile.read(filepath);
		
		// Creates tuples and adds them to the container
		for (PrimaryModelWData model : models) {
			KnimeTuple tuple = PrimaryModelWDataParser.parse(
					model.getSBMLDoc(), model.getNuMLDoc());
			container.addRowToTable(tuple);
			exec.setProgress((float) container.size() / models.size());
		}

		container.close();
		return container;
	}

	private BufferedDataContainer parsePrimaryModelWODataPMF(String filepath,
			ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec spec = SchemaFactory.createM1DataSchema().createSpec();
		BufferedDataContainer container = exec.createDataContainer(spec);
		
		// Reads in models from file
		List<PrimaryModelWOData> models = PrimaryModelWODataFile.read(filepath);
		
		// Creates tuples and adds them to the container
		for (PrimaryModelWOData model : models) {
			KnimeTuple tuple = PrimaryModelWODataParser.parse(model
					.getSBMLDoc());
			container.addRowToTable(tuple);
			exec.setProgress((float) container.size() / models.size());
		}

		container.close();
		return container;
	}

	private BufferedDataContainer parseTwoStepSecondaryModelPMF(
			String filepath, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec spec = SchemaFactory.createM12DataSchema().createSpec();
		BufferedDataContainer container = exec.createDataContainer(spec);
		
		// Reads in models from file
		List<TwoStepSecondaryModel> models = TwoStepSecondaryModelFile
				.read(filepath);

		// Creates tuples and adds them to the container
		for (TwoStepSecondaryModel tssm : models) {
			List<KnimeTuple> tuples = TwoStepSecondaryModelParser.parse(tssm);
			for (KnimeTuple tuple : tuples) {
				container.addRowToTable(tuple);
			}
			exec.setProgress((float) container.size() / models.size());
		}

		container.close();
		return container;
	}

	private BufferedDataContainer parseOneStepSecondaryModelPMF(
			String filepath, ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec spec = SchemaFactory.createM12DataSchema().createSpec();
		BufferedDataContainer container = exec.createDataContainer(spec);
		
		// Reads in models from file
		List<OneStepSecondaryModel> models = OneStepSecondaryModelFile
				.read(filepath);

		// Creates tuples and adds them to the container
		for (OneStepSecondaryModel ossm : models) {
			List<KnimeTuple> tuples = OneStepSecondaryModelParser.parse(ossm);
			for (KnimeTuple tuple : tuples) {
				container.addRowToTable(tuple);
			}
			exec.setProgress((float) container.size() / models.size());
		}

		container.close();
		return container;
	}

	private BufferedDataContainer parseManualSecondaryModelPMF(String filepath,
			ExecutionContext exec) throws Exception {
		// Creates table spec and container
		DataTableSpec spec = SchemaFactory.createM2Schema().createSpec();
		BufferedDataContainer container = exec.createDataContainer(spec);
		
		// Reads in models from file
		List<ManualSecondaryModel> models = ManualSecondaryModelFile
				.read(filepath);
		
		// Creates tuples and adds them to the container
		for (ManualSecondaryModel model : models) {
			KnimeTuple tuple = ManualSecondaryModelParser.parse(model
					.getSBMLDoc());
			container.addRowToTable(tuple);
			exec.setProgress((float) container.size() / models.size());
		}

		container.close();
		return container;
	}
}

class ReaderUtils {

	/**
	 * Parses a list of constraints and returns a dictionary that maps variables
	 * and their limit values.
	 * 
	 * @param constraints
	 */
	public static Map<String, Limits> parseConstraints(
			final ListOf<Constraint> constraints) {
		Map<String, Limits> paramLimits = new HashMap<>();

		for (Constraint currConstraint : constraints) {
			LimitsConstraint lc = new LimitsConstraint(currConstraint);
			Limits lcLimits = lc.getLimits();
			paramLimits.put(lcLimits.getVar(), lcLimits);
		}

		return paramLimits;
	}

	/**
	 * Creates an EstModelXml from a number of model annotations.
	 * 
	 * @param annotations
	 *            Map of uncertainty measures and their values.
	 */
	public static EstModelXml createEstModel(
			final Map<String, String> annotations) {
		// Initialises variables
		int id = Integer.parseInt(annotations.get("id")); // model id
		String name = null;
		String comment = null;
		Double r2 = null;
		Double rms = null;
		Double sse = null;
		Double aic = null;
		Double bic = null;
		Integer dof = null;

		// Get values from the annotations
		if (annotations != null) {
			if (annotations.containsKey("dataUsage")) {
				comment = annotations.get("dataUsage");
			}
			if (annotations.containsKey("r-squared")) {
				r2 = Double.parseDouble(annotations.get("r-squared"));
			}
			if (annotations.containsKey("rootMeanSquaredError")) {
				rms = Double.parseDouble(annotations
						.get("rootMeanSquaredError"));
			}
			if (annotations.containsKey("sumSquaredError")) {
				sse = Double.parseDouble(annotations.get("sumSquaredError"));
			}
			if (annotations.containsKey("AIC")) {
				aic = Double.parseDouble(annotations.get("AIC"));
			}
			if (annotations.containsKey("BIC")) {
				bic = Double.parseDouble(annotations.get("BIC"));
			}
			if (annotations.containsKey("degreesOfFreedom")) {
				dof = Integer.parseInt(annotations.get("degreesOfFreedom"));
			}
		}

		EstModelXml estModel = new EstModelXml(id, name, sse, rms, r2, aic,
				bic, dof);
		estModel.setQualityScore(0); // unchecked model
		estModel.setComment(comment);
		return estModel;
	}

	/**
	 * Parses misc items.
	 * 
	 * @param miscs
	 *            . Dictionary that maps miscs names and their values.
	 * @return
	 */
	public static PmmXmlDoc parseMiscs(Map<String, Double> miscs) {
		PmmXmlDoc cell = new PmmXmlDoc();

		// First misc item has id -1 and the rest of items have negative ints
		int counter = -1;

		for (Entry<String, Double> entry : miscs.entrySet()) {
			String name = entry.getKey();
			Double value = entry.getValue();

			List<String> categories;
			String description, unit;

			switch (name) {
			case "Temperature":
				categories = Arrays.asList(Categories.getTempCategory()
						.getName());
				description = name;
				unit = Categories.getTempCategory().getStandardUnit();

				cell.add(new MiscXml(counter, name, description, value,
						categories, unit));

				counter -= 1;
				break;

			case "pH":
				categories = Arrays
						.asList(Categories.getPhCategory().getName());
				description = name;
				unit = Categories.getPhUnit();

				cell.add(new MiscXml(counter, name, description, value,
						categories, unit));

				counter -= 1;
				break;
			}
		}

		return cell;
	}
}

class ExperimentalDataParser {

	public static KnimeTuple parseDocument(NuMLDocument doc) {
		// Search concentration ontology
		OntologyTerm concOntology = null;
		for (OntologyTerm ot : doc.getOntologyTerms()) {
			if (ot.getTerm().equals("concentration")) {
				concOntology = ot;
				break;
			}
		}

		Node annotation = (Node) concOntology.getAnnotation();
		Node metadataNode = (Node) annotation.children().get(0);

		KnimeTuple tuple = new KnimeTuple(SchemaFactory.createDataSchema());
		tuple.setValue(TimeSeriesSchema.ATT_CONDID,
				MathUtilities.getRandomNegativeInt());

		// Set matrix
		Node matrixNode = (Node) metadataNode.children().get(0);
		MatrixXml matrixXml = new MatrixXml();
		matrixXml.setName((String) matrixNode.attribute("name"));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(matrixXml));

		// Set agent
		Node agentNode = (Node) metadataNode.children().get(1);
		AgentXml agentXml = new AgentXml();
		agentXml.setName((String) agentNode.attribute("name"));
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(agentXml));

		// Set time series
		RawDataFile dataFile = new RawDataFile(doc);
		PmmXmlDoc mdData = new PmmXmlDoc();
		for (TimeSeriesXml t : dataFile.getData()) {
			mdData.add(t);
		}
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdData);

		return tuple;
	}
}

class PrimaryModelWODataParser {

	public static KnimeTuple parse(SBMLDocument doc) {
		Model model = doc.getModel();
		ListOf<Parameter> listOfParameters = model.getListOfParameters();

		// Parse model annotations
		Model1Annotation primModelAnnotation = new Model1Annotation(model
				.getAnnotation().getNonRDFannotation());

		Model1Rule rule = new Model1Rule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = rule.toCatModel();

		// Parse constraints
		ListOf<Constraint> constraints = model.getListOfConstraints();
		Map<String, Limits> limits = ReaderUtils.parseConstraints(constraints);

		// time series cells
		final int condID = primModelAnnotation.getCondID();
		final String combaseID = primModelAnnotation.getCombaseID();
		Agent organism = new Agent(model.getSpecies(0));
		PmmXmlDoc organismCell = new PmmXmlDoc(organism.toAgentXml());
		Matrix matrix = new Matrix(model.getCompartment(0));
		PmmXmlDoc matrixCell = new PmmXmlDoc(matrix.toMatrixXml());

		PmmXmlDoc mdDataCell = new PmmXmlDoc();

		// Parse model variables: Temperature, pH and water activity
		PmmXmlDoc miscCell = ReaderUtils.parseMiscs(matrix.getMiscs());

		PmmXmlDoc mdInfoCell = new PmmXmlDoc(new MdInfoXml(null, null, null,
				null, null));

		PmmXmlDoc mdLiteratureCell = new PmmXmlDoc();
		String mdDBUID = "?";

		// primary model cells
		PmmXmlDoc catModelCell = new PmmXmlDoc(catModel);

		// Parse dependent parameter (primary models only have one dependent
		// variable)
		DepXml depXml = new DepXml("Value");
		String depUnitID = organism.getSpecies().getUnits();
		if (depUnitID != null) {
			String depUnitName = model.getUnitDefinition(depUnitID).getName();
			depXml.setUnit(depUnitName);
			depXml.setCategory(DBUnits.getDBUnits().get(depUnitName)
					.getKind_of_property_quantity());
		}
		PmmXmlDoc depCell = new PmmXmlDoc(depXml);

		// Parse indep
		Parameter indepParam = listOfParameters.get(Categories.getTime());
		IndepXml indepXml = new IndepXml(indepParam.getId(), null, null);
		String indepUnitID = indepParam.getUnits();
		if (!indepUnitID.equalsIgnoreCase("dimensionless")) {
			String unitName = model.getUnitDefinition(indepUnitID).getName();
			indepXml.setUnit(unitName);
			indepXml.setCategory(Categories.getTimeCategory().getName());
			indepXml.setDescription(Categories.getTime());
		}
		// Get limits
		if (limits.containsKey(indepParam.getId())) {
			Limits indepLimits = limits.get(indepParam.getId());
			indepXml.setMax(indepLimits.getMax());
			indepXml.setMin(indepLimits.getMin());
		}
		PmmXmlDoc indepCell = new PmmXmlDoc(indepXml);

		// Parse Consts
		LinkedList<Parameter> constParams = new LinkedList<>();
		for (Parameter param : listOfParameters) {
			if (param.isConstant()) {
				constParams.add(param);
			}
		}

		PmmXmlDoc paramCell = new PmmXmlDoc();
		for (Parameter constParam : constParams) {
			ParamXml paramXml = new Coefficient(constParam).toParamXml();

			// Assign unit and category
			String unitID = constParam.getUnits();
			if (!unitID.equals("dimensionless")) {
				String unitName = model.getUnitDefinition(unitID).getName();
				paramXml.setUnit(unitName);
				paramXml.setCategory(DBUnits.getDBUnits().get(unitName)
						.getKind_of_property_quantity());
			}

			// Get limits
			if (limits.containsKey(constParam.getId())) {
				Limits constLimits = limits.get(constParam.getId());
				paramXml.setMax(constLimits.getMax());
				paramXml.setMin(constLimits.getMin());
			}

			paramCell.add(paramXml);
		}

		EstModelXml estModel = ReaderUtils.createEstModel(primModelAnnotation
				.getUncertainties());
		if (model.isSetName()) {
			estModel.setName(model.getName());
		}

		PmmXmlDoc estModelCell = new PmmXmlDoc(estModel);

		PmmXmlDoc emLiteratureCell = new PmmXmlDoc();
		for (LiteratureItem lit : primModelAnnotation.getLits()) {
			emLiteratureCell.add(lit);
		}

		String mDBUID = "?";

		// Add cells to the row
		KnimeTuple row = new KnimeTuple(SchemaFactory.createM1DataSchema());

		// time series cells
		row.setValue(TimeSeriesSchema.ATT_CONDID, condID);
		row.setValue(TimeSeriesSchema.ATT_COMBASEID, combaseID);
		row.setValue(TimeSeriesSchema.ATT_AGENT, organismCell);
		row.setValue(TimeSeriesSchema.ATT_MATRIX, matrixCell);
		row.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdDataCell);
		row.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
		row.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoCell);
		row.setValue(TimeSeriesSchema.ATT_LITMD, mdLiteratureCell);
		row.setValue(TimeSeriesSchema.ATT_DBUUID, mdDBUID);

		// primary model cells
		row.setValue(Model1Schema.ATT_MODELCATALOG, catModelCell);
		row.setValue(Model1Schema.ATT_DEPENDENT, depCell);
		row.setValue(Model1Schema.ATT_INDEPENDENT, indepCell);
		row.setValue(Model1Schema.ATT_PARAMETER, paramCell);
		row.setValue(Model1Schema.ATT_ESTMODEL, estModelCell);
		row.setValue(Model1Schema.ATT_MLIT, new PmmXmlDoc());
		row.setValue(Model1Schema.ATT_EMLIT, emLiteratureCell);
		row.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
		row.setValue(Model1Schema.ATT_DBUUID, mDBUID);

		return row;
	}
}

class PrimaryModelWDataParser {

	public static KnimeTuple parse(SBMLDocument sbmlDoc, NuMLDocument numlDoc) {
		Model model = sbmlDoc.getModel();
		ListOf<Parameter> listOfParameters = model.getListOfParameters();

		// Parse model annotations
		Model1Annotation primModelAnnotation = new Model1Annotation(model
				.getAnnotation().getNonRDFannotation());

		Model1Rule rule = new Model1Rule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = rule.toCatModel();

		// Parse constraints
		ListOf<Constraint> constraints = model.getListOfConstraints();
		Map<String, Limits> limits = ReaderUtils.parseConstraints(constraints);

		// time series cells
		final int condID = primModelAnnotation.getCondID();
		final String combaseID = primModelAnnotation.getCombaseID();
		Agent organism = new Agent(model.getSpecies(0));
		PmmXmlDoc organismCell = new PmmXmlDoc(organism.toAgentXml());
		Matrix matrix = new Matrix(model.getCompartment(0));
		PmmXmlDoc matrixCell = new PmmXmlDoc(matrix.toMatrixXml());

		PmmXmlDoc mdDataCell = new PmmXmlDoc();
		DataFile df = new DataFile(numlDoc);
		for (TimeSeriesXml ts : df.getData()) {
			mdDataCell.add(ts);
		}

		// Parse model variables: Temperature, pH and water activity
		PmmXmlDoc miscCell = ReaderUtils.parseMiscs(matrix.getMiscs());

		PmmXmlDoc mdInfoCell = new PmmXmlDoc(new MdInfoXml(null, null, null,
				null, null));

		PmmXmlDoc mdLiteratureCell = new PmmXmlDoc();
		String mdDBUID = "?";

		// primary model cells
		PmmXmlDoc catModelCell = new PmmXmlDoc(catModel);

		// Parse dependent parameter (primary models only have one dependent
		// variable)
		DepXml depXml = new DepXml("Value");
		String depUnitID = organism.getSpecies().getUnits();
		if (depUnitID != null) {
			String depUnitName = model.getUnitDefinition(depUnitID).getName();
			depXml.setUnit(depUnitName);
			depXml.setCategory(DBUnits.getDBUnits().get(depUnitName)
					.getKind_of_property_quantity());
		}
		PmmXmlDoc depCell = new PmmXmlDoc(depXml);

		// Parse indep
		Parameter indepParam = listOfParameters.get(Categories.getTime());
		IndepXml indepXml = new IndepXml(indepParam.getId(), null, null);
		String indepUnitID = indepParam.getUnits();
		if (!indepUnitID.equalsIgnoreCase("dimensionless")) {
			String unitName = model.getUnitDefinition(indepUnitID).getName();
			indepXml.setUnit(unitName);
			indepXml.setCategory(Categories.getTimeCategory().getName());
			indepXml.setDescription(Categories.getTime());
		}
		// Get limits
		if (limits.containsKey(indepParam.getId())) {
			Limits indepLimits = limits.get(indepParam.getId());
			indepXml.setMax(indepLimits.getMax());
			indepXml.setMin(indepLimits.getMin());
		}
		PmmXmlDoc indepCell = new PmmXmlDoc(indepXml);

		// Parse Consts
		LinkedList<Parameter> constParams = new LinkedList<>();
		for (Parameter param : listOfParameters) {
			if (param.isConstant()) {
				constParams.add(param);
			}
		}

		PmmXmlDoc paramCell = new PmmXmlDoc();
		for (Parameter constParam : constParams) {
			ParamXml paramXml = new Coefficient(constParam).toParamXml();

			// Assign unit and category
			String unitID = constParam.getUnits();
			if (!unitID.equals("dimensionless")) {
				String unitName = model.getUnitDefinition(unitID).getName();
				paramXml.setUnit(unitName);
				paramXml.setCategory(DBUnits.getDBUnits().get(unitName)
						.getKind_of_property_quantity());
			}

			// Get limits
			if (limits.containsKey(constParam.getId())) {
				Limits constLimits = limits.get(constParam.getId());
				paramXml.setMax(constLimits.getMax());
				paramXml.setMin(constLimits.getMin());
			}

			paramCell.add(paramXml);
		}

		EstModelXml estModel = ReaderUtils.createEstModel(primModelAnnotation
				.getUncertainties());
		if (model.isSetName()) {
			estModel.setName(model.getName());
		}

		PmmXmlDoc estModelCell = new PmmXmlDoc(estModel);

		PmmXmlDoc emLiteratureCell = new PmmXmlDoc();
		for (LiteratureItem lit : primModelAnnotation.getLits()) {
			emLiteratureCell.add(lit);
		}

		String mDBUID = "?";

		// Add cells to the row
		KnimeTuple row = new KnimeTuple(SchemaFactory.createM1DataSchema());

		// time series cells
		row.setValue(TimeSeriesSchema.ATT_CONDID, condID);
		row.setValue(TimeSeriesSchema.ATT_COMBASEID, combaseID);
		row.setValue(TimeSeriesSchema.ATT_AGENT, organismCell);
		row.setValue(TimeSeriesSchema.ATT_MATRIX, matrixCell);
		row.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdDataCell);
		row.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
		row.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoCell);
		row.setValue(TimeSeriesSchema.ATT_LITMD, mdLiteratureCell);
		row.setValue(TimeSeriesSchema.ATT_DBUUID, mdDBUID);

		// primary model cells
		row.setValue(Model1Schema.ATT_MODELCATALOG, catModelCell);
		row.setValue(Model1Schema.ATT_DEPENDENT, depCell);
		row.setValue(Model1Schema.ATT_INDEPENDENT, indepCell);
		row.setValue(Model1Schema.ATT_PARAMETER, paramCell);
		row.setValue(Model1Schema.ATT_ESTMODEL, estModelCell);
		row.setValue(Model1Schema.ATT_MLIT, new PmmXmlDoc());
		row.setValue(Model1Schema.ATT_EMLIT, emLiteratureCell);
		row.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
		row.setValue(Model1Schema.ATT_DBUUID, mDBUID);

		return row;
	}
}

class TwoStepSecondaryModelParser {

	public static List<KnimeTuple> parse(TwoStepSecondaryModel tssm) {

		// create n rows for n secondary models
		List<KnimeTuple> rows = new LinkedList<>();

		// Parse secondary model
		CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) tssm
				.getSecDoc().getPlugin(CompConstants.shortLabel);
		ModelDefinition secModel = secCompPlugin.getModelDefinition(0);

		// Parse constraints
		ListOf<Constraint> secConstraints = secModel.getListOfConstraints();
		Map<String, Limits> secLimits = ReaderUtils
				.parseConstraints(secConstraints);

		// Parse rule
		Model2Rule secRule = new Model2Rule(
				(AssignmentRule) secModel.getRule(0));
		CatalogModelXml secCatModel = secRule.toCatModel();
		PmmXmlDoc secCatModelCell = new PmmXmlDoc(secCatModel);

		// Get parameters
		ListOf<Parameter> params = secModel.getListOfParameters();

		// Parse dep
		String secDepName = secRule.getRule().getVariable();
		DepXml secDepXml = new DepXml(secDepName);
		Parameter depParam = secModel.getParameter(secDepName);
		if (depParam.getUnits() != null && !depParam.getUnits().isEmpty()) {
			// Add unit
			String unitID = depParam.getUnits();
			String unitName = secModel.getUnitDefinition(unitID).getName();
			secDepXml.setUnit(unitName);

			// Add unit category
			if (unitName.equals("min") || unitName.equals("h")) {
				secDepXml.setCategory(Categories.getTimeCategory().getName());
			} else if (unitName.equals("°C")) {
				secDepXml.setCategory(Categories.getTempCategory().getName());
			}
		}
		PmmXmlDoc secDepCell = new PmmXmlDoc(secDepXml);

		// Sort const and indep params
		LinkedList<Parameter> secIndepParams = new LinkedList<>();
		LinkedList<Parameter> secConstParams = new LinkedList<>();
		for (Parameter param : params) {
			if (param.isConstant()) {
				secConstParams.add(param);
			} else if (!param.getId().equals(secDepName)) {
				secIndepParams.add(param);
			}
		}

		// Parse indeps
		PmmXmlDoc secIndepCell = new PmmXmlDoc();
		for (Parameter param : secIndepParams) {
			IndepXml indepXml = new SecIndep(param).toIndepXml();

			// Assign unit and category
			String unitID = param.getUnits();
			if (!unitID.equals("dimensionless")) {
				String unitName = secModel.getUnitDefinition(unitID).getName();
				indepXml.setUnit(unitName);
				indepXml.setCategory(DBUnits.getDBUnits().get(unitName)
						.getKind_of_property_quantity());
			}

			// Get limits
			if (secLimits.containsKey(param.getId())) {
				Limits indepLimits = secLimits.get(param.getId());
				indepXml.setMax(indepLimits.getMax());
				indepXml.setMin(indepLimits.getMin());
			}

			secIndepCell.add(indepXml);
		}

		// Parse consts
		PmmXmlDoc secConstCell = new PmmXmlDoc();
		for (Parameter param : secConstParams) {
			ParamXml paramXml = new Coefficient(param).toParamXml();

			// Assign unit and category
			String unitID = param.getUnits();
			if (!unitID.equals("dimensionless")) {
				String unitName = secModel.getUnitDefinition(unitID).getName();
				paramXml.setUnit(unitName);
				paramXml.setCategory(DBUnits.getDBUnits().get(unitName)
						.getKind_of_property_quantity());
			}

			// Get limits
			if (secLimits.containsKey(param.getId())) {
				Limits constLimits = secLimits.get(param.getId());
				paramXml.setMax(constLimits.getMax());
				paramXml.setMin(constLimits.getMin());
			}

			secConstCell.add(paramXml);
		}

		// Get model annotation
		Model2Annotation m2Annot = new Model2Annotation(secModel
				.getAnnotation().getNonRDFannotation());

		// EstModel
		EstModelXml secEstModel = ReaderUtils.createEstModel(m2Annot
				.getUncertainties());
		if (secModel.isSetName()) {
			secEstModel.setName(secModel.getName());
		}
		PmmXmlDoc secEstModelCell = new PmmXmlDoc(secEstModel);

		// Get globalModelID from annotation
		int globalModelID = m2Annot.getGlobalModelID();

		// Get EM_Literature (references) from annotation
		PmmXmlDoc secEmLitCell = new PmmXmlDoc();
		for (LiteratureItem lit : m2Annot.getLiteratureItems()) {
			secEmLitCell.add(lit);
		}

		for (PrimaryModelWData pmwd : tssm.getPrimModels()) {

			Model model = pmwd.getSBMLDoc().getModel();
			ListOf<Parameter> listOfParameters = model.getListOfParameters();

			// parse annotation
			Model1Annotation primModelAnnotation = new Model1Annotation(model
					.getAnnotation().getNonRDFannotation());

			Model1Rule rule = new Model1Rule((AssignmentRule) model.getRule(0));
			CatalogModelXml catModel = rule.toCatModel();

			// Parse constraints
			ListOf<Constraint> constraints = model.getListOfConstraints();
			Map<String, Limits> limits = ReaderUtils
					.parseConstraints(constraints);

			// time series cells
			final int condID = primModelAnnotation.getCondID();
			final String combaseID = primModelAnnotation.getCombaseID();
			Agent organism = new Agent(model.getSpecies(0));
			PmmXmlDoc organismCell = new PmmXmlDoc(organism.toAgentXml());

			Matrix matrix = new Matrix(model.getCompartment(0));
			PmmXmlDoc matrixCell = new PmmXmlDoc(matrix.toMatrixXml());

			// Add data
			PmmXmlDoc mdData = new PmmXmlDoc();
			if (pmwd.getNuMLDoc() != null) {
				DataFile df = new DataFile(pmwd.getNuMLDoc());
				for (TimeSeriesXml ts : df.getData()) {
					mdData.add(ts);
				}
			}

			// Parse model variables
			Map<String, Double> miscs = matrix.getMiscs();
			PmmXmlDoc miscCell = ReaderUtils.parseMiscs(miscs);

			PmmXmlDoc mdInfoCell = new PmmXmlDoc(new MdInfoXml(null, null,
					null, null, null));
			PmmXmlDoc mdLiteratureCell = new PmmXmlDoc();
			String mdDBUID = "?";

			// primary model cells
			PmmXmlDoc catModelCell = new PmmXmlDoc(catModel);

			// Parse dependent parameter (primary models only have one dependent
			// variable)
			DepXml depXml = new DepXml("Value");
			String depUnitID = organism.getSpecies().getUnits();
			if (depUnitID != null) {
				String depUnitName = model.getUnitDefinition(depUnitID)
						.getName();
				depXml.setUnit(depUnitName);
				depXml.setCategory(DBUnits.getDBUnits().get(depUnitName)
						.getKind_of_property_quantity());
			}
			PmmXmlDoc depCell = new PmmXmlDoc(depXml);

			// Parse indep
			Parameter indepParam = listOfParameters.get(Categories.getTime());
			IndepXml indepXml = new IndepXml(indepParam.getId(), null, null);
			String indepUnitID = indepParam.getUnits();
			if (!indepUnitID.equalsIgnoreCase("dimensionless")) {
				String unitName = model.getUnitDefinition(indepUnitID)
						.getName();
				indepXml.setUnit(unitName);
				indepXml.setCategory(Categories.getTimeCategory().getName());
				indepXml.setDescription(Categories.getTime());
			}
			// Get limits
			if (limits.containsKey(indepParam.getId())) {
				Limits indepLimits = limits.get(indepParam.getId());
				indepXml.setMax(indepLimits.getMax());
				indepXml.setMin(indepLimits.getMin());
			}
			PmmXmlDoc indepCell = new PmmXmlDoc(indepXml);

			// Parse Consts
			LinkedList<Parameter> constParams = new LinkedList<>();
			for (Parameter param : listOfParameters) {
				if (param.isConstant()) {
					constParams.add(param);
				}
			}

			PmmXmlDoc paramCell = new PmmXmlDoc();
			for (Parameter constParam : constParams) {
				ParamXml paramXml = new Coefficient(constParam).toParamXml();

				// Assign unit and category
				String unitID = constParam.getUnits();
				if (!unitID.equals("dimensionless")) {
					String unitName = model.getUnitDefinition(unitID).getName();
					paramXml.setUnit(unitName);
					paramXml.setCategory(DBUnits.getDBUnits().get(unitName)
							.getKind_of_property_quantity());
				}

				// Get limits
				if (limits.containsKey(constParam.getId())) {
					Limits constLimits = limits.get(constParam.getId());
					paramXml.setMax(constLimits.getMax());
					paramXml.setMin(constLimits.getMin());
				}

				paramCell.add(paramXml);
			}
			EstModelXml estModel = ReaderUtils
					.createEstModel(primModelAnnotation.getUncertainties());
			if (model.isSetName()) {
				estModel.setName(model.getName());
			}

			PmmXmlDoc estModelCell = new PmmXmlDoc(estModel);

			PmmXmlDoc emLiteratureCell = new PmmXmlDoc();
			for (LiteratureItem lit : primModelAnnotation.getLits()) {
				emLiteratureCell.add(lit);
			}
			String mDBUID = "?";

			KnimeTuple row = new KnimeTuple(SchemaFactory.createM12DataSchema());

			// TimeSeriesSchema cells
			row.setValue(TimeSeriesSchema.ATT_CONDID, condID);
			row.setValue(TimeSeriesSchema.ATT_COMBASEID, combaseID);
			row.setValue(TimeSeriesSchema.ATT_AGENT, organismCell);
			row.setValue(TimeSeriesSchema.ATT_MATRIX, matrixCell);
			row.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdData);
			row.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
			row.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoCell);
			row.setValue(TimeSeriesSchema.ATT_LITMD, mdLiteratureCell);
			row.setValue(TimeSeriesSchema.ATT_DBUUID, mdDBUID);

			// Model1Schema cells
			row.setValue(Model1Schema.ATT_MODELCATALOG, catModelCell);
			row.setValue(Model1Schema.ATT_DEPENDENT, depCell);
			row.setValue(Model1Schema.ATT_INDEPENDENT, indepCell);
			row.setValue(Model1Schema.ATT_PARAMETER, paramCell);
			row.setValue(Model1Schema.ATT_ESTMODEL, estModelCell);
			row.setValue(Model1Schema.ATT_MLIT, new PmmXmlDoc());
			row.setValue(Model1Schema.ATT_EMLIT, emLiteratureCell);
			row.setValue(Model1Schema.ATT_DATABASEWRITABLE,
					Model1Schema.WRITABLE);
			row.setValue(Model1Schema.ATT_DBUUID, mDBUID);

			// Model2Schema cells
			row.setValue(Model2Schema.ATT_MODELCATALOG, secCatModelCell);
			row.setValue(Model2Schema.ATT_DEPENDENT, secDepCell);
			row.setValue(Model2Schema.ATT_INDEPENDENT, secIndepCell);
			row.setValue(Model2Schema.ATT_PARAMETER, secConstCell);
			row.setValue(Model2Schema.ATT_ESTMODEL, secEstModelCell);
			row.setValue(Model2Schema.ATT_MLIT, new PmmXmlDoc());
			row.setValue(Model2Schema.ATT_EMLIT, secEmLitCell);
			row.setValue(Model2Schema.ATT_DATABASEWRITABLE,
					Model2Schema.WRITABLE);
			row.setValue(Model2Schema.ATT_DBUUID, "?");
			row.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, globalModelID);

			rows.add(row);
		}

		return rows;
	}
}

class OneStepSecondaryModelParser {

	public static List<KnimeTuple> parse(OneStepSecondaryModel ossm) {
		List<KnimeTuple> rows = new LinkedList<>();

		// Create primary model
		Model model = ossm.getSBMLDoc().getModel();
		ListOf<Parameter> listOfParameters = model.getListOfParameters();

		// parse annotation
		Model1Annotation primModelAnnotation = new Model1Annotation(model
				.getAnnotation().getNonRDFannotation());

		Model1Rule rule = new Model1Rule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = rule.toCatModel();

		// Parse constraints
		ListOf<Constraint> constraints = model.getListOfConstraints();
		Map<String, Limits> limits = ReaderUtils.parseConstraints(constraints);

		// time series cells
		final int condID = primModelAnnotation.getCondID();
		final String combaseID = primModelAnnotation.getCombaseID();
		Agent organism = new Agent(model.getSpecies(0));
		PmmXmlDoc organismCell = new PmmXmlDoc(organism.toAgentXml());

		Matrix matrix = new Matrix(model.getCompartment(0));
		PmmXmlDoc matrixCell = new PmmXmlDoc(matrix.toMatrixXml());

		// Parse model variables
		Map<String, Double> miscs = matrix.getMiscs();
		PmmXmlDoc miscCell = ReaderUtils.parseMiscs(miscs);

		PmmXmlDoc mdInfoCell = new PmmXmlDoc(new MdInfoXml(null, null, null,
				null, null));
		PmmXmlDoc mdLiteratureCell = new PmmXmlDoc();
		String mdDBUID = "?";

		// primary model cells
		PmmXmlDoc catModelCell = new PmmXmlDoc(catModel);

		// Parse dependent parameter (primary models only have one dependent
		// variable)
		DepXml depXml = new DepXml("Value");
		String depUnitID = organism.getSpecies().getUnits();
		if (depUnitID != null) {
			String depUnitName = model.getUnitDefinition(depUnitID).getName();
			depXml.setUnit(depUnitName);
			depXml.setCategory(DBUnits.getDBUnits().get(depUnitName)
					.getKind_of_property_quantity());
		}
		PmmXmlDoc depCell = new PmmXmlDoc(depXml);

		// Parse indep
		Parameter indepParam = listOfParameters.get(Categories.getTime());
		IndepXml indepXml = new IndepXml(indepParam.getId(), null, null);
		String indepUnitID = indepParam.getUnits();
		if (!indepUnitID.equalsIgnoreCase("dimensionless")) {
			String unitName = model.getUnitDefinition(indepUnitID).getName();
			indepXml.setUnit(unitName);
			indepXml.setCategory(Categories.getTimeCategory().getName());
			indepXml.setDescription(Categories.getTime());
		}
		// Get limits
		if (limits.containsKey(indepParam.getId())) {
			Limits indepLimits = limits.get(indepParam.getId());
			indepXml.setMax(indepLimits.getMax());
			indepXml.setMin(indepLimits.getMin());
		}
		PmmXmlDoc indepCell = new PmmXmlDoc(indepXml);

		// Parse Consts
		LinkedList<Parameter> constParams = new LinkedList<>();
		for (Parameter param : listOfParameters) {
			if (param.isConstant()) {
				constParams.add(param);
			}
		}

		PmmXmlDoc paramCell = new PmmXmlDoc();
		for (Parameter constParam : constParams) {
			ParamXml paramXml = new Coefficient(constParam).toParamXml();

			// Assign unit and category
			String unitID = constParam.getUnits();
			if (!unitID.equals("dimensionless")) {
				String unitName = model.getUnitDefinition(unitID).getName();
				paramXml.setUnit(unitName);
				paramXml.setCategory(DBUnits.getDBUnits().get(unitName)
						.getKind_of_property_quantity());
			}

			// Get limits
			if (limits.containsKey(constParam.getId())) {
				Limits constLimits = limits.get(constParam.getId());
				paramXml.setMax(constLimits.getMax());
				paramXml.setMin(constLimits.getMin());
			}

			paramCell.add(paramXml);
		}
		EstModelXml estModel = ReaderUtils.createEstModel(primModelAnnotation
				.getUncertainties());
		if (model.isSetName()) {
			estModel.setName(model.getName());
		}

		PmmXmlDoc estModelCell = new PmmXmlDoc(estModel);

		PmmXmlDoc emLiteratureCell = new PmmXmlDoc();
		for (LiteratureItem lit : primModelAnnotation.getLits()) {
			emLiteratureCell.add(lit);
		}
		String mDBUID = "?";

		// Parse secondary model
		CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) ossm
				.getSBMLDoc().getPlugin(CompConstants.shortLabel);
		ModelDefinition secModel = secCompPlugin.getModelDefinition(0);

		// Parse constraints
		ListOf<Constraint> secConstraints = secModel.getListOfConstraints();
		Map<String, Limits> secLimits = ReaderUtils
				.parseConstraints(secConstraints);

		// Parse rule
		Model2Rule secRule = new Model2Rule(
				(AssignmentRule) secModel.getRule(0));
		CatalogModelXml secCatModel = secRule.toCatModel();
		PmmXmlDoc secCatModelCell = new PmmXmlDoc(secCatModel);

		// Get parameters
		ListOf<Parameter> params = secModel.getListOfParameters();

		// Parse dep
		String secDepName = secRule.getRule().getVariable();
		DepXml secDepXml = new DepXml(secDepName);
		Parameter depParam = secModel.getParameter(secDepName);
		if (depParam.getUnits() != null && !depParam.getUnits().isEmpty()) {
			// Add unit
			String unitID = depParam.getUnits();
			String unitName = secModel.getUnitDefinition(unitID).getName();
			secDepXml.setUnit(unitName);

			// Add unit category
			if (unitName.equals("min") || unitName.equals("h")) {
				secDepXml.setCategory(Categories.getTimeCategory().getName());
			} else if (unitName.equals("°C")) {
				secDepXml.setCategory(Categories.getTempCategory().getName());
			}
		}
		PmmXmlDoc secDepCell = new PmmXmlDoc(secDepXml);

		// Sort const and indep params
		LinkedList<Parameter> secIndepParams = new LinkedList<>();
		LinkedList<Parameter> secConstParams = new LinkedList<>();
		for (Parameter param : params) {
			if (param.isConstant()) {
				secConstParams.add(param);
			} else if (!param.getId().equals(secDepName)) {
				secIndepParams.add(param);
			}
		}

		// Parse indeps
		PmmXmlDoc secIndepCell = new PmmXmlDoc();
		for (Parameter param : secIndepParams) {
			IndepXml secIndepXml = new SecIndep(param).toIndepXml();

			// Assign unit and category
			String unitID = param.getUnits();
			if (!unitID.equals("dimensionless")) {
				String unitName = secModel.getUnitDefinition(unitID).getName();
				secIndepXml.setUnit(unitName);
				secIndepXml.setCategory(DBUnits.getDBUnits().get(unitName)
						.getKind_of_property_quantity());
			}

			// Get limits
			if (secLimits.containsKey(param.getId())) {
				Limits indepLimits = secLimits.get(param.getId());
				secIndepXml.setMax(indepLimits.getMax());
				secIndepXml.setMin(indepLimits.getMin());
			}

			secIndepCell.add(secIndepXml);
		}

		// Parse consts
		PmmXmlDoc secConstCell = new PmmXmlDoc();
		for (Parameter param : secConstParams) {
			ParamXml paramXml = new Coefficient(param).toParamXml();

			// Assign unit and category
			String unitID = param.getUnits();
			if (!unitID.equals("dimensionless")) {
				String unitName = secModel.getUnitDefinition(unitID).getName();
				paramXml.setUnit(unitName);
				paramXml.setCategory(DBUnits.getDBUnits().get(unitName)
						.getKind_of_property_quantity());
			}

			// Get limits
			if (secLimits.containsKey(param.getId())) {
				Limits constLimits = secLimits.get(param.getId());
				paramXml.setMax(constLimits.getMax());
				paramXml.setMin(constLimits.getMin());
			}

			secConstCell.add(paramXml);
		}

		// Get model annotation
		Model2Annotation m2Annot = new Model2Annotation(secModel
				.getAnnotation().getNonRDFannotation());

		// EstModel
		EstModelXml secEstModel = ReaderUtils.createEstModel(m2Annot
				.getUncertainties());
		if (secModel.isSetName()) {
			secEstModel.setName(secModel.getName());
		}
		PmmXmlDoc secEstModelCell = new PmmXmlDoc(secEstModel);

		// Get globalModelID from annotation
		int globalModelID = m2Annot.getGlobalModelID();

		// Get EM_Literature (references) from annotation
		PmmXmlDoc secEmLitCell = new PmmXmlDoc();
		for (LiteratureItem lit : m2Annot.getLiteratureItems()) {
			secEmLitCell.add(lit);
		}

		KnimeTuple row = new KnimeTuple(SchemaFactory.createM12DataSchema());

		// TimeSeriesSchema cells
		row.setValue(TimeSeriesSchema.ATT_CONDID, condID);
		row.setValue(TimeSeriesSchema.ATT_COMBASEID, combaseID);
		row.setValue(TimeSeriesSchema.ATT_AGENT, organismCell);
		row.setValue(TimeSeriesSchema.ATT_MATRIX, matrixCell);
		row.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
		row.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoCell);
		row.setValue(TimeSeriesSchema.ATT_LITMD, mdLiteratureCell);
		row.setValue(TimeSeriesSchema.ATT_DBUUID, mdDBUID);

		// Model1Schema cells
		row.setValue(Model1Schema.ATT_MODELCATALOG, catModelCell);
		row.setValue(Model1Schema.ATT_DEPENDENT, depCell);
		row.setValue(Model1Schema.ATT_INDEPENDENT, indepCell);
		row.setValue(Model1Schema.ATT_PARAMETER, paramCell);
		row.setValue(Model1Schema.ATT_ESTMODEL, estModelCell);
		row.setValue(Model1Schema.ATT_MLIT, new PmmXmlDoc());
		row.setValue(Model1Schema.ATT_EMLIT, emLiteratureCell);
		row.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
		row.setValue(Model1Schema.ATT_DBUUID, mDBUID);

		// Model2Schema cells
		row.setValue(Model2Schema.ATT_MODELCATALOG, secCatModelCell);
		row.setValue(Model2Schema.ATT_DEPENDENT, secDepCell);
		row.setValue(Model2Schema.ATT_INDEPENDENT, secIndepCell);
		row.setValue(Model2Schema.ATT_PARAMETER, secConstCell);
		row.setValue(Model2Schema.ATT_ESTMODEL, secEstModelCell);
		row.setValue(Model2Schema.ATT_MLIT, new PmmXmlDoc());
		row.setValue(Model2Schema.ATT_EMLIT, secEmLitCell);
		row.setValue(Model2Schema.ATT_DATABASEWRITABLE, Model2Schema.WRITABLE);
		row.setValue(Model2Schema.ATT_DBUUID, "?");
		row.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, globalModelID);

		// Add data
		for (NuMLDocument numlDoc : ossm.getNumlDocs()) {
			PmmXmlDoc mdData = new PmmXmlDoc();
			DataFile df = new DataFile(numlDoc);
			for (TimeSeriesXml ts : df.getData()) {
				mdData.add(ts);
			}
			row.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdData);
			rows.add(row);
		}

		return rows;
	}
}

class ManualSecondaryModelParser {

	public static KnimeTuple parse(SBMLDocument doc) {

		CompSBMLDocumentPlugin compPlugin = (CompSBMLDocumentPlugin) doc
				.getPlugin(CompConstants.shortLabel);
		ModelDefinition model = compPlugin.getModelDefinition(0);

		// Parse constraints
		ListOf<Constraint> constraints = model.getListOfConstraints();
		Map<String, Limits> limits = ReaderUtils.parseConstraints(constraints);

		// Parse rule
		Model2Rule rule = new Model2Rule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = rule.toCatModel();
		PmmXmlDoc catModelCell = new PmmXmlDoc(catModel);

		// Get parameters
		ListOf<Parameter> params = model.getListOfParameters();

		// Parse dep
		String depName = rule.getRule().getVariable();
		DepXml depXml = new DepXml(depName);
		Parameter depParam = model.getParameter(depName);
		if (depParam.getUnits() != null && !depParam.getUnits().isEmpty()) {
			// Add unit
			String unitID = depParam.getUnits();
			String unitName = model.getUnitDefinition(unitID).getName();
			depXml.setUnit(unitName);

			// Add unit category
			if (unitName.equals("min") || unitName.equals("h")) {
				depXml.setCategory(Categories.getTimeCategory().getName());
			} else if (unitName.equals("°C")) {
				depXml.setCategory(Categories.getTempCategory().getName());
			}
		}
		PmmXmlDoc depCell = new PmmXmlDoc(depXml);

		// Sort const and indep params
		LinkedList<Parameter> indepParams = new LinkedList<>();
		LinkedList<Parameter> constParams = new LinkedList<>();
		for (Parameter param : params) {
			if (param.isConstant()) {
				constParams.add(param);
			} else if (!param.getId().equals(depName)) {
				indepParams.add(param);
			}
		}

		// Parse indeps
		PmmXmlDoc indepCell = new PmmXmlDoc();
		for (Parameter param : indepParams) {
			IndepXml indepXml = new SecIndep(param).toIndepXml();

			// Assign unit and category
			String unitID = param.getUnits();
			if (!unitID.equals("dimensionless")) {
				String unitName = model.getUnitDefinition(unitID).getName();
				indepXml.setUnit(unitName);
				indepXml.setCategory(DBUnits.getDBUnits().get(unitName)
						.getKind_of_property_quantity());
			}

			// Get limits
			if (limits.containsKey(param.getId())) {
				Limits indepLimits = limits.get(param.getId());
				indepXml.setMax(indepLimits.getMax());
				indepXml.setMin(indepLimits.getMin());
			}

			indepCell.add(indepXml);
		}

		// Parse consts
		PmmXmlDoc constCell = new PmmXmlDoc();
		for (Parameter param : constParams) {
			ParamXml paramXml = new Coefficient(param).toParamXml();

			// Assign unit and category
			String unitID = param.getUnits();
			if (!unitID.equals("dimensionless")) {
				String unitName = model.getUnitDefinition(unitID).getName();
				paramXml.setUnit(unitName);
				paramXml.setCategory(DBUnits.getDBUnits().get(unitName)
						.getKind_of_property_quantity());
			}

			// Get limits
			if (limits.containsKey(param.getId())) {
				Limits constLimits = limits.get(param.getId());
				paramXml.setMax(constLimits.getMax());
				paramXml.setMin(constLimits.getMin());
			}

			constCell.add(paramXml);
		}

		// Get model annotation
		Model2Annotation modelAnnotation = new Model2Annotation(model
				.getAnnotation().getNonRDFannotation());

		// EstModel
		EstModelXml estModelXml = ReaderUtils.createEstModel(modelAnnotation
				.getUncertainties());
		if (model.isSetName()) {
			estModelXml.setName(model.getName());
		}
		PmmXmlDoc estModelCell = new PmmXmlDoc(estModelXml);

		// Get globalModelID from annotation
		int globalModelID = modelAnnotation.getGlobalModelID();

		// Get EM_Literature (references) from annotation
		PmmXmlDoc emLiteratureCell = new PmmXmlDoc();
		for (LiteratureItem lit : modelAnnotation.getLiteratureItems()) {
			emLiteratureCell.add(lit);
		}

		// Add cells to the row
		KnimeTuple row = new KnimeTuple(SchemaFactory.createM2Schema());
		row.setValue(Model2Schema.ATT_MODELCATALOG, catModelCell);
		row.setValue(Model2Schema.ATT_DEPENDENT, depCell);
		row.setValue(Model2Schema.ATT_INDEPENDENT, indepCell);
		row.setValue(Model2Schema.ATT_PARAMETER, constCell);
		row.setValue(Model2Schema.ATT_ESTMODEL, estModelCell);
		row.setValue(Model2Schema.ATT_MLIT, new PmmXmlDoc());
		row.setValue(Model2Schema.ATT_EMLIT, emLiteratureCell);
		row.setValue(Model2Schema.ATT_DATABASEWRITABLE, Model2Schema.WRITABLE);
		row.setValue(Model2Schema.ATT_DBUUID, "?");
		row.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, globalModelID);
		return row;
	}
}

class SecondaryModelParser {

	public static KnimeTuple parseDocument(SBMLDocument doc) {

		CompSBMLDocumentPlugin compPlugin = (CompSBMLDocumentPlugin) doc
				.getPlugin(CompConstants.shortLabel);
		ModelDefinition model = compPlugin.getModelDefinition(0);

		// Parse constraints
		ListOf<Constraint> constraints = model.getListOfConstraints();
		Map<String, Limits> limits = ReaderUtils.parseConstraints(constraints);

		// Parse rule
		Model2Rule rule = new Model2Rule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = rule.toCatModel();
		PmmXmlDoc catModelCell = new PmmXmlDoc(catModel);

		// Get parameters
		ListOf<Parameter> params = model.getListOfParameters();

		// Parse dep
		String depName = rule.getRule().getVariable();
		DepXml depXml = new DepXml(depName);
		Parameter depParam = model.getParameter(depName);
		if (depParam.getUnits() != null) {
			// Add unit
			String unitID = depParam.getUnits();
			String unitName = model.getUnitDefinition(unitID).getName();
			depXml.setUnit(unitName);

			// Add unit category
			if (unitName.equals("min") || unitName.equals("h")) {
				depXml.setCategory(Categories.getTimeCategory().getName());
			} else if (unitName.equals("°C")) {
				depXml.setCategory(Categories.getTempCategory().getName());
			}
		}
		PmmXmlDoc depCell = new PmmXmlDoc(depXml);

		// Sort const and indep params
		LinkedList<Parameter> indepParams = new LinkedList<>();
		LinkedList<Parameter> constParams = new LinkedList<>();
		for (Parameter param : params) {
			if (param.isConstant()) {
				constParams.add(param);
			} else if (!param.getId().equals(depName)) {
				indepParams.add(param);
			}
		}

		// Parse indeps
		PmmXmlDoc indepCell = new PmmXmlDoc();
		for (Parameter param : indepParams) {
			IndepXml indepXml = new SecIndep(param).toIndepXml();

			// Assign unit and category
			String unitID = param.getUnits();
			if (!unitID.equals("dimensionless")) {
				String unitName = model.getUnitDefinition(unitID).getName();
				indepXml.setUnit(unitName);
				indepXml.setCategory(DBUnits.getDBUnits().get(unitName)
						.getKind_of_property_quantity());
			}

			// Get limits
			if (limits.containsKey(param.getId())) {
				Limits indepLimits = limits.get(param.getId());
				indepXml.setMax(indepLimits.getMax());
				indepXml.setMin(indepLimits.getMin());
			}

			indepCell.add(indepXml);
		}

		// Parse consts
		PmmXmlDoc constCell = new PmmXmlDoc();
		for (Parameter param : constParams) {
			ParamXml paramXml = new Coefficient(param).toParamXml();

			// Assign unit and category
			String unitID = param.getUnits();
			if (!unitID.equals("dimensionless")) {
				String unitName = model.getUnitDefinition(unitID).getName();
				paramXml.setUnit(unitName);
				paramXml.setCategory(DBUnits.getDBUnits().get(unitName)
						.getKind_of_property_quantity());
			}

			// Get limits
			if (limits.containsKey(param.getId())) {
				Limits constLimits = limits.get(param.getId());
				paramXml.setMax(constLimits.getMax());
				paramXml.setMin(constLimits.getMin());
			}

			constCell.add(paramXml);
		}

		// Get model annotation
		Model2Annotation modelAnnotation = new Model2Annotation(model
				.getAnnotation().getNonRDFannotation());

		// EstModel
		EstModelXml estModelXml = ReaderUtils.createEstModel(modelAnnotation
				.getUncertainties());
		if (model.isSetName()) {
			estModelXml.setName(model.getName());
		}
		PmmXmlDoc estModelCell = new PmmXmlDoc(estModelXml);

		// Get globalModelID from annotation
		int globalModelID = modelAnnotation.getGlobalModelID();

		// Get EM_Literature (references) from annotation
		PmmXmlDoc emLiteratureCell = new PmmXmlDoc();
		for (LiteratureItem lit : modelAnnotation.getLiteratureItems()) {
			emLiteratureCell.add(lit);
		}

		// Add cells to the row
		KnimeTuple row = new KnimeTuple(SchemaFactory.createM2Schema());
		row.setValue(Model2Schema.ATT_MODELCATALOG, catModelCell);
		row.setValue(Model2Schema.ATT_DEPENDENT, depCell);
		row.setValue(Model2Schema.ATT_INDEPENDENT, indepCell);
		row.setValue(Model2Schema.ATT_PARAMETER, constCell);
		row.setValue(Model2Schema.ATT_ESTMODEL, estModelCell);
		row.setValue(Model2Schema.ATT_MLIT, new PmmXmlDoc());
		row.setValue(Model2Schema.ATT_EMLIT, emLiteratureCell);
		row.setValue(Model2Schema.ATT_DATABASEWRITABLE, Model2Schema.WRITABLE);
		row.setValue(Model2Schema.ATT_DBUUID, "?");
		row.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, globalModelID);
		return row;
	}
}

class TertiaryModelParser {

	public static List<KnimeTuple> parseDocument(SBMLDocument doc) {
		Model model = doc.getModel();
		ListOf<Parameter> listOfParameters = model.getListOfParameters();

		CompSBMLDocumentPlugin compPlugin = (CompSBMLDocumentPlugin) doc
				.getPlugin(CompConstants.shortLabel);
		ListOf<ModelDefinition> modelDefinitions = compPlugin
				.getListOfModelDefinitions();

		// create n rows for n secondary models
		List<KnimeTuple> rows = new ArrayList<>();

		// parse annotation
		Model1Annotation primModelAnnotation = new Model1Annotation(model
				.getAnnotation().getNonRDFannotation());

		// Parse constraints
		ListOf<Constraint> constraints = model.getListOfConstraints();
		Map<String, Limits> limits = ReaderUtils.parseConstraints(constraints);

		// time series cells
		final int condID = primModelAnnotation.getCondID();
		final String combaseID = primModelAnnotation.getCombaseID();
		Agent organism = new Agent(model.getSpecies(0));
		PmmXmlDoc organismCell = new PmmXmlDoc(organism.toAgentXml());

		Matrix matrix = new Matrix(model.getCompartment(0));
		PmmXmlDoc matrixCell = new PmmXmlDoc(matrix.toMatrixXml());

		PmmXmlDoc mdDataCell = new PmmXmlDoc();

		Map<String, Double> miscs = matrix.getMiscs();
		PmmXmlDoc miscCell = ReaderUtils.parseMiscs(miscs);

		PmmXmlDoc mdInfoCell = new PmmXmlDoc(new MdInfoXml(null, null, null,
				null, null));
		PmmXmlDoc mdLiteratureCell = new PmmXmlDoc();
		String mdDBUID = "?";

		// primary model cells
		Model1Rule rule1 = new Model1Rule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = rule1.toCatModel();
		PmmXmlDoc catModelCell = new PmmXmlDoc(catModel);

		// Parse dep
		DepXml depXml = new DepXml("Value");
		String depUnitID = organism.getSpecies().getUnits();
		if (depUnitID != null) {
			String depUnitName = model.getUnitDefinition(depUnitID).getName();
			depXml.setUnit(depUnitName);
			depXml.setCategory(DBUnits.getDBUnits().get(depUnitName)
					.getKind_of_property_quantity());
		}
		PmmXmlDoc depCell = new PmmXmlDoc(depXml);

		// Parse indep
		Parameter indepParam = listOfParameters.get(Categories.getTime());
		IndepXml indepXml = new IndepXml(indepParam.getId(), null, null);
		String indepUnitID = indepParam.getUnits();
		if (!indepUnitID.equalsIgnoreCase("dimensionless")) {
			String unitName = model.getUnitDefinition(indepUnitID).getName();
			indepXml.setUnit(unitName);
			indepXml.setCategory(Categories.getTimeCategory().getName());
			indepXml.setDescription(Categories.getTime());
		}
		// Get limits
		if (limits.containsKey(indepParam.getId())) {
			Limits indepLimits = limits.get(indepParam.getId());
			indepXml.setMax(indepLimits.getMax());
			indepXml.setMin(indepLimits.getMin());
		}
		PmmXmlDoc indepCell = new PmmXmlDoc(indepXml);

		// Parse consts
		LinkedList<Parameter> constParams = new LinkedList<>();
		for (Parameter param : listOfParameters) {
			if (param.isConstant()) {
				constParams.add(param);
			}
		}
		PmmXmlDoc paramCell = new PmmXmlDoc();
		for (Parameter constParam : constParams) {
			ParamXml paramXml = new Coefficient(constParam).toParamXml();

			// Assign unit and category
			String unitID = constParam.getUnits();
			if (!unitID.equals("dimensionless")) {
				String unitName = model.getUnitDefinition(unitID).getName();
				paramXml.setUnit(unitName);
				paramXml.setCategory(DBUnits.getDBUnits().get(unitName)
						.getKind_of_property_quantity());
			}

			// Get limits
			if (limits.containsKey(constParam.getId())) {
				Limits constLimits = limits.get(constParam.getId());
				paramXml.setMax(constLimits.getMax());
				paramXml.setMin(constLimits.getMin());
			}

			paramCell.add(paramXml);
		}

		// Parse uncertainty measures from the document's annotations
		EstModelXml estModel = ReaderUtils.createEstModel(primModelAnnotation
				.getUncertainties());
		if (model.isSetName()) {
			estModel.setName(model.getName());
		}
		PmmXmlDoc estModelCell = new PmmXmlDoc(estModel);

		PmmXmlDoc mLiteratureCell = new PmmXmlDoc();

		PmmXmlDoc emLiteratureCell = new PmmXmlDoc();
		for (LiteratureItem lit : primModelAnnotation.getLits()) {
			emLiteratureCell.add(lit);
		}

		String mDBUID = "?";

		for (ModelDefinition secModel : modelDefinitions) {
			ListOf<Parameter> secParams = secModel.getListOfParameters();

			// Parse constraints
			ListOf<Constraint> secConstraints = secModel.getListOfConstraints();
			Map<String, Limits> secLimits = ReaderUtils
					.parseConstraints(secConstraints);

			// secondary model columns (19-27)
			Model2Rule rule2 = new Model2Rule(
					(AssignmentRule) secModel.getRule(0));
			CatalogModelXml catModelSec = rule2.toCatModel();
			PmmXmlDoc catModelSecCell = new PmmXmlDoc(catModelSec);

			// Create sec dep
			String depName = rule2.getRule().getVariable();
			Parameter depParam = listOfParameters.get(depName);
			Coefficient depCoeff = new Coefficient(depParam);
			DepXml secDepXml = new DepXml(depName);
			secDepXml.setDescription(depCoeff.getDescription());
			PmmXmlDoc dependentSecCell = new PmmXmlDoc(secDepXml);

			// Sort const and indep params
			LinkedList<Parameter> secIndepParams = new LinkedList<>();
			LinkedList<Parameter> secConstParams = new LinkedList<>();
			for (Parameter param : secParams) {
				if (param.isConstant()) {
					secConstParams.add(param);
				} else if (!param.getId().equals(depName)) {
					secIndepParams.add(param);
				}
			}

			// Parse sec indeps
			PmmXmlDoc secIndepCell = new PmmXmlDoc();
			for (Parameter param : secIndepParams) {
				IndepXml secIndepXml = new SecIndep(param).toIndepXml();

				// Assign unit and category
				String unitID = param.getUnits();
				if (!unitID.equals("dimensionless")) {
					String unitName = secModel.getUnitDefinition(unitID)
							.getName();
					secIndepXml.setUnit(unitName);
					secIndepXml.setCategory(DBUnits.getDBUnits().get(unitName)
							.getKind_of_property_quantity());
				}

				// Get limits
				if (secLimits.containsKey(param.getId())) {
					Limits indepLimits = secLimits.get(param.getId());
					secIndepXml.setMax(indepLimits.getMax());
					secIndepXml.setMin(indepLimits.getMin());
				}

				secIndepCell.add(secIndepXml);
			}

			// Parse sec consts
			PmmXmlDoc secConstCell = new PmmXmlDoc();
			for (Parameter param : secConstParams) {
				ParamXml paramXml = new Coefficient(param).toParamXml();

				// Assign unit and category
				String unitID = param.getUnits();
				if (!unitID.equals("dimensionless")) {
					String unitName = secModel.getUnitDefinition(unitID)
							.getName();
					paramXml.setUnit(unitName);
					paramXml.setCategory(DBUnits.getDBUnits().get(unitName)
							.getKind_of_property_quantity());
				}

				// Get limits
				if (secLimits.containsKey(param.getId())) {
					Limits constLimits = secLimits.get(param.getId());
					paramXml.setMax(constLimits.getMax());
					paramXml.setMin(constLimits.getMin());
				}

				secConstCell.add(paramXml);
			}

			PmmXmlDoc mLiteratureSecCell = new PmmXmlDoc();

			Model2Annotation secModelAnnotation = new Model2Annotation(secModel
					.getAnnotation().getNonRDFannotation());

			// EstModel
			EstModelXml secEstModelXml = ReaderUtils
					.createEstModel(secModelAnnotation.getUncertainties());
			if (secModel.isSetName()) {
				secEstModelXml.setName(secEstModelXml.getName());
			}
			PmmXmlDoc estModelSecCell = new PmmXmlDoc(secEstModelXml);

			final int globalModelID = secModelAnnotation.getGlobalModelID();

			// Add references to PMM Lab table
			PmmXmlDoc emLiteratureSecCell = new PmmXmlDoc();
			for (LiteratureItem lit : secModelAnnotation.getLiteratureItems()) {
				emLiteratureCell.add(lit);
			}

			String mDBUIDSEC = "?";

			// Add cells to the row
			KnimeTuple row = new KnimeTuple(SchemaFactory.createM12DataSchema());

			row.setValue(TimeSeriesSchema.ATT_CONDID, condID);
			row.setValue(TimeSeriesSchema.ATT_COMBASEID, combaseID);
			row.setValue(TimeSeriesSchema.ATT_AGENT, organismCell);
			row.setValue(TimeSeriesSchema.ATT_MATRIX, matrixCell);
			row.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdDataCell);
			row.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
			row.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoCell);
			row.setValue(TimeSeriesSchema.ATT_LITMD, mdLiteratureCell);
			row.setValue(TimeSeriesSchema.ATT_DBUUID, mdDBUID);

			row.setValue(Model1Schema.ATT_MODELCATALOG, catModelCell);
			row.setValue(Model1Schema.ATT_DEPENDENT, depCell);
			row.setValue(Model1Schema.ATT_INDEPENDENT, indepCell);
			row.setValue(Model1Schema.ATT_PARAMETER, paramCell);
			row.setValue(Model1Schema.ATT_ESTMODEL, estModelCell);
			row.setValue(Model1Schema.ATT_MLIT, mLiteratureCell);
			row.setValue(Model1Schema.ATT_EMLIT, emLiteratureCell);
			row.setValue(Model1Schema.ATT_DATABASEWRITABLE,
					Model1Schema.WRITABLE);
			row.setValue(Model1Schema.ATT_DBUUID, mDBUID);

			row.setValue(Model2Schema.ATT_MODELCATALOG, catModelSecCell);
			row.setValue(Model2Schema.ATT_DEPENDENT, dependentSecCell);
			row.setValue(Model2Schema.ATT_INDEPENDENT, secIndepCell);
			row.setValue(Model2Schema.ATT_PARAMETER, secConstCell);
			row.setValue(Model2Schema.ATT_ESTMODEL, estModelSecCell);
			row.setValue(Model2Schema.ATT_MLIT, mLiteratureSecCell);
			row.setValue(Model2Schema.ATT_EMLIT, emLiteratureSecCell);
			row.setValue(Model2Schema.ATT_DATABASEWRITABLE,
					Model2Schema.WRITABLE);
			row.setValue(Model2Schema.ATT_DBUUID, mDBUIDSEC);
			row.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, globalModelID);

			rows.add(row);
		}

		return rows;
	}
}