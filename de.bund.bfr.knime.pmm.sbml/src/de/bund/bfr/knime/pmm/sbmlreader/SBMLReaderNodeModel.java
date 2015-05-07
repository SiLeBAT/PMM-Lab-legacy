package de.bund.bfr.knime.pmm.sbmlreader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import org.sbml.jsbml.Species;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.sbmlutil.Agent;
import de.bund.bfr.knime.pmm.sbmlutil.DBUnits;
import de.bund.bfr.knime.pmm.sbmlutil.DataFile;
import de.bund.bfr.knime.pmm.sbmlutil.Experiment;
import de.bund.bfr.knime.pmm.sbmlutil.Limits;
import de.bund.bfr.knime.pmm.sbmlutil.LimitsConstraint;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;
import de.bund.bfr.knime.pmm.sbmlutil.Model1Annotation;
import de.bund.bfr.knime.pmm.sbmlutil.Model1Rule;
import de.bund.bfr.knime.pmm.sbmlutil.Model2Annotation;
import de.bund.bfr.knime.pmm.sbmlutil.Model2Rule;
import de.bund.bfr.knime.pmm.sbmlutil.PMFFile;
import de.bund.bfr.knime.pmm.sbmlutil.PrimCoefficient;
import de.bund.bfr.knime.pmm.sbmlutil.SecCoefficient;
import de.bund.bfr.knime.pmm.sbmlutil.Util;

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
		List<Experiment> exps = PMFFile.read(filename.getStringValue());

		// Get model typefrom the first model in the list of experiments
		SBMLDocument firstDoc = exps.get(0).getModel();
		CompSBMLDocumentPlugin plugin = (CompSBMLDocumentPlugin) firstDoc
				.getPlugin("comp");
		boolean isTertiary = plugin.getListOfModelDefinitions().size() > 0;

		// Create schema and container
		KnimeSchema schema;
		if (isTertiary) {
			schema = SchemaFactory.createM12DataSchema();
		} else {
			schema = SchemaFactory.createM1DataSchema();
		}
		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());

		// Parse models
		if (isTertiary) {
			short counter = 0;
			for (Experiment exp : exps) {
				// Parse model data
				PmmXmlDoc mdData = new PmmXmlDoc();
				if (exp.getData() != null) {
					DataFile df = new DataFile(exp.getData());
					for (TimeSeriesXml ts : df.getData()) {
						mdData.add(ts);
					}
				}

				// Parse model
				for (KnimeTuple tuple : TertiaryModelParser.parseDocument(exp
						.getModel())) {
					tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdData);
					container.addRowToTable(tuple);
				}

				// Increment counter and update progress bar
				counter++;
				exec.setProgress((float) counter / exps.size());
			}
		}

		else {
			short counter = 0;
			for (Experiment exp : exps) {
				// Parse model data
				PmmXmlDoc mdData = new PmmXmlDoc();
				if (exp.getData() != null) {
					DataFile df = new DataFile(exp.getData());
					for (TimeSeriesXml ts : df.getData()) {
						mdData.add(ts);
					}
				}

				// Parse model
				KnimeTuple tuple = PrimaryModelParser.parseDocument(exp
						.getModel());
				tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdData);
				container.addRowToTable(tuple);

				// Increment counter and update progress bar
				counter++;
				exec.setProgress((float) counter / exps.size());
			}
		}

		// Close container and return its table
		container.close();
		BufferedDataTable[] table = { container.getTable() };
		return table;
	}
}

class ReaderUtils {

	// Create dependent variable
	public static PmmXmlDoc parseDep(final Species species,
			final Map<String, UnitsFromDB> units,
			final ListOf<UnitDefinition> unitDefinitions) {
		String depUnitId = species.getUnits();
		String depUnit = unitDefinitions.get(depUnitId).getName();

		UnitsFromDB dbUnit = units.get(depUnitId);
		String category = dbUnit.getKind_of_property_quantity();

		return new PmmXmlDoc(new DepXml("Value", category, depUnit));
	}

	// Create independent variable
	public static PmmXmlDoc createIndep(final Parameter indepParam,
			final Map<String, Limits> limits) {
		String category = Categories.getTimeCategory().getName();
		String name = Categories.getTime();
		String origname = Categories.getTime();
		String unit = indepParam.getUnits();
		Double min = null, max = null;
		if (limits.containsKey(name)) {
			Limits indepLimits = limits.get(name);
			min = indepLimits.getMin();
			max = indepLimits.getMax();
		}
		IndepXml indep = new IndepXml(name, min, max, category, unit);
		indep.setDescription("time");
		indep.setOrigName(origname);

		return new PmmXmlDoc(indep);
	}

	// Create constant variables
	public static PmmXmlDoc parseConsts(final ListOf<Parameter> params,
			final ListOf<UnitDefinition> unitDefinitions,
			final Map<String, Limits> limits) {

		// Get constant parameters
		LinkedList<Parameter> constParams = new LinkedList<>();
		for (Parameter param : params) {
			if (param.isConstant()) {
				constParams.add(param);
			}
		}

		// Get unit data from DB
		UnitsFromDB ufdb = new UnitsFromDB();
		ufdb.askDB();
		Map<String, UnitsFromDB> unitsMap = new HashMap<>();
		for (UnitsFromDB dbUnit : ufdb.getMap().values()) {
			unitsMap.put(dbUnit.getDisplay_in_GUI_as(), dbUnit);
		}

		PmmXmlDoc constDoc = new PmmXmlDoc();
		for (Parameter param : constParams) {
			ParamXml paramXml = new PrimCoefficient(param).toParamXml();

			if (param.getUnits().equals("dimensionless")) {
				paramXml.setUnit("");
			} else {
				String unitId = Util.createId(param.getUnits());

				// Get description and category from DB
				String unit = unitDefinitions.get(unitId).getName();
				if (unit != null) {
					paramXml.setUnit(unit);
					if (unitsMap.containsKey(unit)) {
						UnitsFromDB dbUnit = unitsMap.get(unit);
						paramXml.setDescription(dbUnit.getDescription());
						paramXml.setCategory(dbUnit
								.getKind_of_property_quantity());
					}
				}
			}

			// Get limits
			if (limits.containsKey(param.getId())) {
				Limits paramLimits = limits.get(param.getId());
				paramXml.setMax(paramLimits.getMax());
				paramXml.setMin(paramLimits.getMin());
			}

			constDoc.add(paramXml);
		}

		return constDoc;
	}

	/**
	 * Parse a list of constraints and return a dictionary that maps variables
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

	// create EstModelXml from the model annotations
	public static EstModelXml createEstModel(
			final Map<String, String> annotations) {
		// Initialize variables
		int id = MathUtilities.getRandomNegativeInt(); // model id
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
			if (annotations.containsKey("dataName")) {
				name = annotations.get("dataName");
			}
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
	 * Parse misc items.
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

class PrimaryModelParser {

	public static KnimeTuple parseDocument(SBMLDocument doc) {
		Model model = doc.getModel();
		ListOf<Parameter> listOfParameters = model.getListOfParameters();

		DBUnits dbUnits = new DBUnits();

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
		Map<String, UnitsFromDB> units = dbUnits.getUnits(model
				.getListOfUnitDefinitions());
		PmmXmlDoc depCell = ReaderUtils.parseDep(organism.getSpecies(), units,
				model.getListOfUnitDefinitions());
		Parameter indepParam = listOfParameters.get("Time");
		PmmXmlDoc indepCell = ReaderUtils.createIndep(indepParam, limits);
		PmmXmlDoc paramCell = ReaderUtils.parseConsts(listOfParameters,
				model.getListOfUnitDefinitions(), limits);

		EstModelXml estModel = ReaderUtils.createEstModel(primModelAnnotation
				.getUncertainties());

		PmmXmlDoc estModelCell = new PmmXmlDoc(estModel);

		PmmXmlDoc mLiteratureCell = new PmmXmlDoc();

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
		row.setValue(Model1Schema.ATT_MLIT, mLiteratureCell);
		row.setValue(Model1Schema.ATT_EMLIT, emLiteratureCell);
		row.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
		row.setValue(Model1Schema.ATT_DBUUID, mDBUID);

		return row;
	}
}

class TertiaryModelParser {

	private static PmmXmlDoc parseSecDep(final AssignmentRule rule) {
		return new PmmXmlDoc(new DepXml(rule.getVariable(), "P", "", "", ""));
	}

	/**
	 * Parse independent parameters from a secondary model.
	 * 
	 * <ol>
	 * <li>Search non constant parameters not named as the dep.</li>
	 * <li>Get unit name from the parameter.</li>
	 * <li>Get unit from DB</li>
	 * <li>Get data from the parameter</li>
	 * <li>Get parameter limits using its unit's name</li>
	 * </ol>
	 */
	private static PmmXmlDoc parseSecIndeps(final String depName,
			final ListOf<Parameter> params, final Map<String, Limits> limits) {
		PmmXmlDoc indepDoc = new PmmXmlDoc();

		for (Parameter param : params) {
			if (!param.getId().equals(depName) && !param.isConstant()) {
				// Get limits
				Double min = null, max = null;
				if (limits.containsKey(param.getId())) {
					Limits indepLimits = limits.get(param.getId());
					min = indepLimits.getMin();
					max = indepLimits.getMax();
				}

				// Seconday model indeps lacks units and categories
				IndepXml indepXml = new IndepXml(param.getId(), min, max, "",
						"");
				indepXml.setDescription("variable");

				indepDoc.add(indepXml);
			}
		}

		return indepDoc;
	}

	/**
	 * Create PmmXmlDoc with ParamXmls for every constant parameter from the
	 * SBML document.
	 * 
	 * @param params
	 * @param limits
	 * @return
	 */
	private static PmmXmlDoc parseConstsSec(final ListOf<Parameter> params) {

		// Get coefficients
		LinkedList<SecCoefficient> coefficients = new LinkedList<>();
		for (Parameter param : params) {
			if (param.isConstant()) {
				coefficients.add(new SecCoefficient(param));
			}
		}

		PmmXmlDoc coefficientsXml = new PmmXmlDoc();
		for (SecCoefficient coefficient : coefficients) {
			coefficientsXml.add(coefficient.toParamXml());
		}

		return coefficientsXml;
	}

	public static List<KnimeTuple> parseDocument(SBMLDocument doc) {
		Model model = doc.getModel();
		ListOf<Parameter> listOfParameters = model.getListOfParameters();
		DBUnits dbUnits = new DBUnits();

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

		Set<UnitDefinition> unitDefs = new HashSet<>(
				model.getListOfUnitDefinitions());

		Map<String, UnitsFromDB> units = dbUnits.getUnits(model
				.getListOfUnitDefinitions());
		PmmXmlDoc depCell = ReaderUtils.parseDep(organism.getSpecies(), units,
				model.getListOfUnitDefinitions());
		Parameter indepParam = listOfParameters.get("Time");
		PmmXmlDoc indepCell = ReaderUtils.createIndep(indepParam, limits);
		PmmXmlDoc paramCell = ReaderUtils.parseConsts(listOfParameters,
				model.getListOfUnitDefinitions(), limits);

		// Parse uncertainty measures from the document's annotations
		EstModelXml estModel = ReaderUtils.createEstModel(primModelAnnotation.getUncertainties());
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

			// Add sec unit definitions
			for (UnitDefinition ud : secModel.getListOfUnitDefinitions()) {
				unitDefs.add(ud);
			}

			PmmXmlDoc dependentSecCell = parseSecDep(rule2.getRule());
			String depName = rule2.getRule().getVariable();
			PmmXmlDoc independentSecCell = parseSecIndeps(depName, secParams,
					secLimits);
			PmmXmlDoc parameterSecCell = parseConstsSec(secParams);

			PmmXmlDoc estModelSecCell = new PmmXmlDoc();
			estModelSecCell.add(new EstModelXml(MathUtilities
					.getRandomNegativeInt(), null, null, null, null, null,
					null, 0));

			PmmXmlDoc mLiteratureSecCell = new PmmXmlDoc();

			Model2Annotation secModelAnnotation = new Model2Annotation(
					secModel.getAnnotation().getNonRDFannotation());
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
			row.setValue(Model2Schema.ATT_INDEPENDENT, independentSecCell);
			row.setValue(Model2Schema.ATT_PARAMETER, parameterSecCell);
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