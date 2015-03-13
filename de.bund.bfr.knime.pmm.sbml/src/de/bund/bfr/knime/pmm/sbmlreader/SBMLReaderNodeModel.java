package de.bund.bfr.knime.pmm.sbmlreader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.annotation.ReferenceNode;
import de.bund.bfr.knime.pmm.annotation.UncertaintyNode;
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
import de.bund.bfr.knime.pmm.sbmlutil.DBUnits;
import de.bund.bfr.knime.pmm.sbmlutil.DataFile;
import de.bund.bfr.knime.pmm.sbmlutil.Limits;
import de.bund.bfr.knime.pmm.sbmlutil.LimitsConstraint;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;
import de.bund.bfr.knime.pmm.sbmlutil.Model1Rule;
import de.bund.bfr.knime.pmm.sbmlutil.Model2Rule;
import de.bund.bfr.knime.pmm.sbmlutil.Organism;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.NuMLReader;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;

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

	// Get data source name from a SBML document
	private String getDataName(SBMLDocument doc) {
		XMLNode modelAnnotation = doc.getModel().getAnnotation()
				.getNonRDFannotation();
		if (modelAnnotation == null) {
			return null;
		}

		XMLNode dataSource = modelAnnotation.getChildElement("dataSource", "");
		if (dataSource == null) {
			return null;
		}

		String dataName = dataSource.getAttrValue("href");
		return dataName;
	}

	// Load PMF file
	private BufferedDataTable[] loadPMF(final ExecutionContext exec)
			throws Exception {

		// Create list for models and data
		List<SBMLDocument> models = new ArrayList<>();
		Map<String, NuMLDocument> data = new HashMap<>();

		CombineArchive ca = new CombineArchive(new File(
				filename.getStringValue()));

		SBMLReader sbmlReader = new SBMLReader();
		NuMLReader numlReader = new NuMLReader();

		for (ArchiveEntry entry : ca.getEntries()) {
			// Parse model
			if (entry.getFileName().endsWith(".sbml")) {
				InputStream stream = Files.newInputStream(entry.getPath(),
						StandardOpenOption.READ);
				SBMLDocument doc = sbmlReader.readSBMLFromStream(stream);
				stream.close();
				models.add(doc);
			}

			// Parse data
			else if (entry.getFileName().endsWith(".numl")) {
				InputStream stream = Files.newInputStream(entry.getPath(),
						StandardOpenOption.READ);
				data.put(entry.getFileName(), numlReader.read(stream));
				stream.close();
			}
		}

		ca.close();

		// Get model type from the first model in the list
		SBMLDocument firstDoc = models.get(0);
		CompSBMLDocumentPlugin plugin = (CompSBMLDocumentPlugin) firstDoc
				.getPlugin("comp");
		Boolean isTertiary = plugin.getListOfModelDefinitions().size() > 0;

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
			for (SBMLDocument model : models) {
				String dataName = getDataName(model);
				
						PmmXmlDoc mdData = new PmmXmlDoc();
				if (dataName != null) {
					if (data.containsKey(dataName)) {
						DataFile df = new DataFile(data.get(dataName));
						List<TimeSeriesXml> timeSeries = df.getData();
						
						for (TimeSeriesXml ts : timeSeries) {
							mdData.add(ts);
						}
					}
				}
				
				for (KnimeTuple tuple : TertiaryModelParser.parseDocument(model)) {
					if (dataName == null) {
						tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdData);
						container.addRowToTable(tuple);
					} else {
						container.addRowToTable(tuple);
					}
				}
					
			}
		} else {
			for (SBMLDocument model : models) {
				KnimeTuple tuple = PrimaryModelParser.parseDocument(model);
				
				String dataName = getDataName(model);
				
				if (dataName != null) {
					if (data.containsKey(dataName)) {
						DataFile df = new DataFile(data.get(dataName));
						List<TimeSeriesXml> timeSeries = df.getData();
						
						PmmXmlDoc mdData = new PmmXmlDoc();
						for (TimeSeriesXml ts : timeSeries) {
							mdData.add(ts);
						}

						tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, mdData);
					}
				}
				
				container.addRowToTable(tuple);
			}
		}

		// close container and return its table
		container.close();
		BufferedDataTable[] table = { container.getTable() };
		return table;
	}
}

class ReaderUtils {

	/**
	 * Parse nonRdfAnnotation
	 * 
	 * @param annot
	 * @return Dictionary with keys and values of annotations. E.g. {'title':
	 *         'Salmonella ...', ...}
	 */
	public static Map<String, String> parseAnnotation(final XMLNode annot) {
		Map<String, String> annotations = new HashMap<>();

		// Search metadata container
		XMLNode metadata = annot.getChildElement("metadata", "");

		// Parse metadata container
		if (metadata != null) {
			for (XMLNode node : metadata.getChildElements("", "")) {
				String nodeName = node.getName();
				System.out.println(nodeName);
				if (!nodeName.isEmpty()) {
					// Process uncertainty annotations
					if (nodeName.equals("modelquality")) {
						UncertaintyNode unode = new UncertaintyNode(node);
						annotations.putAll(unode.getMeasures());
					}
					// Process other annotations
					else {
						String nodeValue = node.getChildAt(0).getCharacters();
						annotations.put(nodeName, nodeValue);
					}
				}
			}
		}

		return annotations;
	}

	/**
	 * Parse references in a model annotation.
	 * 
	 * @param annot
	 * @return
	 */
	public static List<LiteratureItem> parseLit(final XMLNode annot) {
		List<LiteratureItem> lits = new LinkedList<>();

		// search metadata container
		XMLNode metadata = annot.getChildElement("metadata", "");

		if (metadata != null) {
			// Parse references
			for (XMLNode ref : metadata.getChildElements("reference", "")) {
				ReferenceNode node = new ReferenceNode(ref);
				lits.add(node.toLiteratureItem());
			}
		}

		return lits;
	}

	// Create dependent variable
	public static PmmXmlDoc parseDep(final Species species,
			final Map<String, UnitsFromDB> units,
			final Map<String, Limits> limits) {
		String origUnit = species.getUnits(); // unit name
		String description = ""; // param description

		// original unit used in SBML doc
		UnitsFromDB dbUnit = units.get(origUnit);

		// Retrieve unit data from dbUnit
		String category = dbUnit.getKind_of_property_quantity();
		String unit = dbUnit.getDisplay_in_GUI_as();

		DepXml dep = new DepXml("Value", origUnit, category, unit, description);
		// Get limits
		if (limits.containsKey("Value")) {
			Limits depLimits = limits.get(origUnit);
			dep.setMax(depLimits.getMax());
			dep.setMin(depLimits.getMin());
		}

		return new PmmXmlDoc(dep);
	}

	// Create independent variables
	public static PmmXmlDoc parseIndeps(final ListOf<Parameter> params,
			final Map<String, UnitsFromDB> units,
			final Map<String, Limits> limits) {
		PmmXmlDoc indepDoc = new PmmXmlDoc();

		for (Parameter param : params) {
			if (!param.isConstant()) {
				String origUnit = param.getUnits(); // unit name
				UnitsFromDB dbUnit = units.get(origUnit);

				String name = "";
				String category = "";
				String unit = "";
				String description = "";

				if (dbUnit != null) {
					// Retrieve unit data from dbUnit
					name = dbUnit.getKind_of_property_quantity();
					category = dbUnit.getKind_of_property_quantity();
					unit = dbUnit.getDisplay_in_GUI_as();
				} else if (origUnit.equals("pmf_celsius")) {
					name = origUnit;
					category = Categories.getTempCategory().getName();
					unit = "°C";
				}

				// other fields
				Double min = null, max = null;

				IndepXml indep = new IndepXml(name, min, max, category, unit);
				indep.setDescription(description);
				// Get limits
				String paramName = param.getId();
				if (limits.containsKey(paramName)) {
					Limits indepLimits = limits.get(paramName);
					indep.setMax(indepLimits.getMax());
					indep.setMin(indepLimits.getMin());
				}

				indepDoc.add(indep);
			}
		}

		return indepDoc;
	}

	// Create constant variables
	public static PmmXmlDoc parseConsts(final ListOf<Parameter> params,
			final Map<String, UnitsFromDB> units,
			final Map<String, Limits> limits) {
		PmmXmlDoc constsDoc = new PmmXmlDoc();

		for (Parameter param : params) {
			if (param.isConstant()) {
				String origUnit = param.getUnits(); // unit name
				UnitsFromDB dbUnit = units.get(origUnit);

				String name = "", category = "", unit = "", description = "";

				// Retrieve unit data from dbUnit
				if (dbUnit != null) {
					name = dbUnit.getName();
					category = dbUnit.getKind_of_property_quantity();
					unit = dbUnit.getDisplay_in_GUI_as();
				} else if (origUnit.equals("pmf_celsius")) {
					name = origUnit;
					category = Categories.getTempCategory().getName();
					unit = "°C";
				}

				// other fields
				String id = param.getId();
				double value = param.getValue();

				ParamXml paramXml = new ParamXml(id, value);
				paramXml.setCategory(category);
				paramXml.setUnit(unit);
				paramXml.setDescription(description);

				// Get limits
				String paramName = param.getId();
				if (limits.containsKey(paramName)) {
					Limits paramLimits = limits.get(paramName);
					paramXml.setMax(paramLimits.getMax());
					paramXml.setMin(paramLimits.getMin());
				}

				constsDoc.add(paramXml);
			}
		}

		return constsDoc;
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
}

class PrimaryModelParser {

	public static KnimeTuple parseDocument(SBMLDocument doc) {
		Model model = doc.getModel();
		ListOf<Parameter> listOfParameters = model.getListOfParameters();

		DBUnits dbUnits = new DBUnits();

		// Parse model annotations
		XMLNode modelAnnotation = model.getAnnotation().getNonRDFannotation();
		Map<String, String> annotations = null;
		// If the model is annotated then parse the annotations
		if (modelAnnotation != null) {
			annotations = ReaderUtils.parseAnnotation(modelAnnotation);
		}

		Model1Rule rule = new Model1Rule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = rule.toCatModel();

		// Parse constraints
		ListOf<Constraint> constraints = model.getListOfConstraints();
		Map<String, Limits> limits = ReaderUtils.parseConstraints(constraints);

		// time series cells
		String combaseID = model.getId();
		Organism organism = new Organism(model.getSpecies(0));
		PmmXmlDoc organismCell = new PmmXmlDoc(organism.toAgentXml());
		Matrix matrix = new Matrix(model.getCompartment(0));
		PmmXmlDoc matrixCell = new PmmXmlDoc(matrix.toMatrixXml());

		PmmXmlDoc mdDataCell = new PmmXmlDoc();

		PmmXmlDoc miscCell = new PmmXmlDoc();

		// TODO: Parse model variables: Temperature, pH and water activity
		if (model.containsParameter("Temperature")) {
			Parameter param = model.getParameter("Temperature");

			int id = MathUtilities.getRandomNegativeInt();
			String name = "Temperature";
			String description = "Temperature";
			double value = param.getValue();
			List<String> categories = Arrays.asList("Temperature");
			String unit = "";
			if (param.getUnits().equals("pmf_celsius")) {
				unit = "°C";
			} else if (param.getUnits().equals("pmf_fahrenheit")) {
				unit = "°F";
			}

			MiscXml item = new MiscXml(id, name, description, value,
					categories, unit);
			item.setOrigUnit(unit);
			miscCell.add(item);
		}

		if (model.containsParameter("pH")) {
			Parameter param = model.getParameter("pH");

			int id = MathUtilities.getRandomNegativeInt();
			String name = "pH";
			String description = "pH";
			double value = param.getValue();
			List<String> categories = Arrays.asList("Dimensionless quantity");
			String unit = "[pH]";

			MiscXml item = new MiscXml(id, name, description, value,
					categories, unit);
			item.setOrigUnit(unit);
			miscCell.add(item);
		}

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
				limits);
		PmmXmlDoc indepCell = ReaderUtils.parseIndeps(listOfParameters, units,
				limits);
		PmmXmlDoc paramCell = ReaderUtils.parseConsts(listOfParameters, units,
				limits);

		EstModelXml estModel = ReaderUtils.createEstModel(annotations);

		PmmXmlDoc estModelCell = new PmmXmlDoc(estModel);

		PmmXmlDoc mLiteratureCell = new PmmXmlDoc();

		List<LiteratureItem> lits = ReaderUtils.parseLit(modelAnnotation);
		PmmXmlDoc emLiteratureCell = new PmmXmlDoc();
		for (LiteratureItem lit : lits) {
			emLiteratureCell.add(lit);
		}

		String mDBUID = "?";

		// Add cells to the row
		KnimeTuple row = new KnimeTuple(SchemaFactory.createM1DataSchema());

		// time series cells
		row.setValue(TimeSeriesSchema.ATT_CONDID,
				MathUtilities.getRandomNegativeInt());
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

	private static PmmXmlDoc parseSecDep(final AssignmentRule rule,
			final ListOf<Parameter> params,
			final Map<String, UnitsFromDB> units,
			final Map<String, Limits> limits) {
		String depName = rule.getVariable();
		Parameter depParam = params.get(depName);

		String origUnit = depParam.getUnits();
		UnitsFromDB dbUnit = units.get(origUnit);

		// Retrieve unit data from dbUnit
		String category = dbUnit.getKind_of_property_quantity();
		String unit = dbUnit.getDisplay_in_GUI_as();
		String description = "";

		// other data
		String name = depParam.getId();
		String origname = depParam.getId();

		DepXml depXml = new DepXml(name, origname, category, unit, description);
		depXml.setOrigName(origUnit);

		// Get limits
		if (limits.containsKey(origUnit)) {
			Limits depLimits = limits.get(origUnit);
			depXml.setMin(depLimits.getMin());
			depXml.setMax(depLimits.getMax());
		}

		PmmXmlDoc depDoc = new PmmXmlDoc(depXml);
		return depDoc;
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
			final ListOf<Parameter> params,
			final Map<String, UnitsFromDB> units,
			final Map<String, Limits> limits) {

		PmmXmlDoc indepDoc = new PmmXmlDoc();

		// Search non constant parameters not named as the dep.
		for (Parameter param : params) {
			if (!param.getId().equals(depName) && !param.isConstant()) {
				// Get unit name from the parameter
				String origUnit = param.getUnits();
				String name = "", category = "", unit = "", desc = "";

				// Get unit from DB
				UnitsFromDB dbUnit = units.get(origUnit);
				if (dbUnit != null) {
					// retrieve unit data from dbUnit
					name = dbUnit.getName();
					category = dbUnit.getKind_of_property_quantity();
					unit = dbUnit.getDisplay_in_GUI_as();
				} else if (origUnit.equals("pmf_celsius")) {
					name = Categories.getTempCategory().getName();
					category = Categories.getTempCategory().getName();
					unit = "°C";
				}

				// Get limits
				Double min = null, max = null;
				if (limits.containsKey(param.getId())) {
					Limits indepLimits = limits.get(param.getId());
					min = indepLimits.getMin();
					max = indepLimits.getMax();
				}

				IndepXml indep = new IndepXml(name, min, max, category, unit);
				indep.setOrigName(origUnit);
				indep.setDescription(desc);

				indepDoc.add(indep);
			}
		}

		return indepDoc;
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
		XMLNode modelAnnotation = model.getAnnotation().getNonRDFannotation();
		Map<String, String> annotations = null;
		// If the model is annotated then parse the annotations
		if (modelAnnotation != null) {
			annotations = ReaderUtils.parseAnnotation(modelAnnotation);
		}

		// Parse constraints
		ListOf<Constraint> constraints = model.getListOfConstraints();
		Map<String, Limits> limits = ReaderUtils.parseConstraints(constraints);

		// time series cells
		String combaseID = model.getId();
		Organism organism = new Organism(model.getSpecies(0));
		PmmXmlDoc organismCell = new PmmXmlDoc(organism.toAgentXml());

		Matrix matrix = new Matrix(model.getCompartment(0));
		PmmXmlDoc matrixCell = new PmmXmlDoc(matrix.toMatrixXml());

		PmmXmlDoc mdDataCell = new PmmXmlDoc();

		PmmXmlDoc miscCell = new PmmXmlDoc();

		// TODO: Parse model variables: Temperature, pH and water activity
		if (model.containsParameter("Temperature")) {
			Parameter param = model.getParameter("Temperature");

			int id = MathUtilities.getRandomNegativeInt();
			String name = "Temperature";
			String description = "Temperature";
			double value = param.getValue();
			List<String> categories = Arrays.asList("Temperature");
			String unit = "";
			if (param.getUnits().equals("pmf_celsius")) {
				unit = "°C";
			} else if (param.getUnits().equals("pmf_fahrenheit")) {
				unit = "°F";
			}

			MiscXml item = new MiscXml(id, name, description, value,
					categories, unit);
			item.setOrigUnit(unit);
			miscCell.add(item);
		}

		if (model.containsParameter("pH")) {
			Parameter param = model.getParameter("pH");

			int id = MathUtilities.getRandomNegativeInt();
			String name = "pH";
			String description = "pH";
			double value = param.getValue();
			List<String> categories = Arrays.asList("Dimensionless quantity");
			String unit = "[pH]";

			MiscXml item = new MiscXml(id, name, description, value,
					categories, unit);
			item.setOrigUnit(unit);
			miscCell.add(item);
		}

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
				limits);
		PmmXmlDoc indepCell = ReaderUtils.parseIndeps(listOfParameters, units,
				limits);
		PmmXmlDoc paramCell = ReaderUtils.parseConsts(listOfParameters, units,
				limits);

		// Parse uncertainty measures from the document's annotations
		EstModelXml estModel = ReaderUtils.createEstModel(annotations);
		PmmXmlDoc estModelCell = new PmmXmlDoc(estModel);

		PmmXmlDoc mLiteratureCell = new PmmXmlDoc();

		PmmXmlDoc emLiteratureCell = new PmmXmlDoc();
		List<LiteratureItem> lits = ReaderUtils.parseLit(modelAnnotation);
		for (LiteratureItem lit : lits) {
			emLiteratureCell.add(lit);
		}

		String mDBUID = "?";

		final int condID = MathUtilities.getRandomNegativeInt();
		final int globalModelID = MathUtilities.getRandomNegativeInt();

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

			// PmmXmlDoc dependentSecCell =
			// parseSecDependentParameter(secParams,
			// depName, secModel.getId(), secLimits);
			units = dbUnits.getUnits(secModel.getListOfUnitDefinitions());

			// Add sec unit definitions
			for (UnitDefinition ud : secModel.getListOfUnitDefinitions()) {
				unitDefs.add(ud);
			}

			PmmXmlDoc dependentSecCell = parseSecDep(rule2.getRule(),
					secParams, units, secLimits);
			String depName = rule2.getRule().getVariable();
			PmmXmlDoc independentSecCell = parseSecIndeps(depName, secParams,
					units, secLimits);
			PmmXmlDoc parameterSecCell = ReaderUtils.parseConsts(secParams,
					units, secLimits);

			PmmXmlDoc estModelSecCell = new PmmXmlDoc();
			estModelSecCell.add(new EstModelXml(MathUtilities
					.getRandomNegativeInt(), null, null, null, null, null,
					null, 0));

			PmmXmlDoc mLiteratureSecCell = new PmmXmlDoc();

			PmmXmlDoc emLiteratureSecCell = new PmmXmlDoc();
			XMLNode secModelAnnotation = model.getAnnotation()
					.getNonRDFannotation();
			lits = ReaderUtils.parseLit(secModelAnnotation);
			for (LiteratureItem lit : lits) {
				emLiteratureSecCell.add(lit);
			}

			String mDBUIDSEC = "?";

			// Add cells to the row
			KnimeTuple row = new KnimeTuple(SchemaFactory.createM12DataSchema());

			row.setValue(TimeSeriesSchema.ATT_CONDID, condID);
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