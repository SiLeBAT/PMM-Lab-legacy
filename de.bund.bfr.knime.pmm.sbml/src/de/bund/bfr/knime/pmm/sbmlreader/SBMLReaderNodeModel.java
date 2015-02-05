package de.bund.bfr.knime.pmm.sbmlreader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.hsh.bfr.db.DBKernel;
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
import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Constraint;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Rule;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
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
import de.bund.bfr.knime.pmm.sbmlutil.SBMLUtil;

/**
 * This is the model implementation of SBMLReader.
 * 
 * Author: Miguel de Alba Aparicio (malba@optimumquality.es)
 */
public class SBMLReaderNodeModel extends NodeModel {

	// configuration keys
	public static final String CFGKEY_FILE = "filename";
	public static final String CFGKEY_FOLDER = "foldername";
	public static final String CFGKEY_SOURCE = "source";
	public static final String CFGKEY_MODEL_TYPE = "modeltype";

	// defaults for persistent state
	private static final String DEFAULT_FILE = "c:/temp/foo.xml";
	private static final String DEFAULT_FOLDER = "c:/temp";

	// persistent state
	private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE,
			DEFAULT_FILE);
	private SettingsModelString folder = new SettingsModelString(CFGKEY_FOLDER,
			DEFAULT_FOLDER);
	private SettingsModelString source = new SettingsModelString(CFGKEY_SOURCE,
			"file");
	private SettingsModelString modelType = new SettingsModelString(
			CFGKEY_MODEL_TYPE, "primary");

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
		if (source.getStringValue().equals("file")) {
			table = parseSingleFile(exec);
		} else {
			table = parseFolder(exec);
		}
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
		folder.saveSettingsTo(settings);
		source.saveSettingsTo(settings);
		modelType.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		filename.loadSettingsFrom(settings);
		folder.loadSettingsFrom(settings);
		source.loadSettingsFrom(settings);
		modelType.loadSettingsFrom(settings);
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
		folder.validateSettings(settings);
		source.validateSettings(settings);
		modelType.validateSettings(settings);
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

	private BufferedDataTable[] parseSingleFile(final ExecutionContext exec) {
		String filePath = filename.getStringValue();
		File sbmlFile = new File(filePath);
		SBMLDocument doc = null;
		try {
			doc = SBMLReader.read(sbmlFile);
		} catch (XMLStreamException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Check model type from the model
		CompSBMLDocumentPlugin documentCompPlugin = (CompSBMLDocumentPlugin) doc
				.getPlugin("comp");
		Boolean isTertiary = documentCompPlugin.getListOfModelDefinitions()
				.size() > 0;

		// Create schema and parse model
		BufferedDataContainer container = null;
		if (isTertiary) {
			KnimeSchema schema = SchemaFactory.createM12DataSchema();
			schema = SchemaFactory.createM12DataSchema();
			// Create container
			container = exec.createDataContainer(schema.createSpec());
			// Parse document
			List<KnimeTuple> tuples = TertiaryModelParser.parseDocument(doc);
			for (KnimeTuple tuple : tuples) {
				container.addRowToTable(tuple);
			}
		} else {
			KnimeSchema schema = SchemaFactory.createM1DataSchema();
			// Create container
			container = exec.createDataContainer(schema.createSpec());
			// Parse document
			KnimeTuple tuple = PrimaryModelParser.parseDocument(doc);
			container.addRowToTable(tuple);
		}
		container.close();

		BufferedDataTable[] table = { container.getTable() };
		return table;
	}

	private BufferedDataTable[] parseFolder(final ExecutionContext exec) {
		// Create container with modelType
		Boolean isTertiary = modelType.getStringValue() == "tertiary";
		KnimeSchema schema = isTertiary ? SchemaFactory.createM12DataSchema()
				: SchemaFactory.createM1DataSchema();
		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());

		// Parse models and ignore the ones with wrong type
		String folderName = folder.getStringValue();
		File folder = new File(folderName);
		File[] files;
		files = folder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				return (arg0.isFile() && arg0.getAbsolutePath().toLowerCase()
						.endsWith("xml"));
			}
		});

		for (File f : files) {
			SBMLDocument doc = null;
			try {
				doc = SBMLReader.read(f);
			} catch (XMLStreamException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			CompSBMLDocumentPlugin documentPlugin = (CompSBMLDocumentPlugin) doc
					.getPlugin("comp");
			// Check model type
			Boolean isDocTertiary = documentPlugin.getListOfModelDefinitions()
					.size() > 0;
			// If the model type of the doc and the type specified on the dialog
			// don't match then skip this file
			if (isTertiary != isDocTertiary) {
				continue;
			}

			if (isTertiary) {
				List<KnimeTuple> tuples = TertiaryModelParser
						.parseDocument(doc);
				for (KnimeTuple tuple : tuples) {
					container.addRowToTable(tuple);
				}
			} else {
				KnimeTuple tuple = PrimaryModelParser.parseDocument(doc);
				container.addRowToTable(tuple);
			}
		}
		container.close();

		BufferedDataTable[] table = { container.getTable() };
		return table;
	}
}


class ReaderUtilities {

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
		XMLNode metadata = null;
		for (int nChild = 0; nChild < annot.getChildCount(); nChild++) {
			XMLNode currNode = annot.getChildAt(nChild);
			String nodeName = currNode.getName();
			if (nodeName.equals("metadata")) {
				metadata = currNode;
				break;
			}
		}

		// Parse metadata container
		if (metadata != null) {
			for (int nTag = 0; nTag < metadata.getChildCount(); nTag++) {
				XMLNode currNode = metadata.getChildAt(nTag);
				String nodeName = currNode.getName();
				if (!nodeName.isEmpty()) {
					// Process uncertainty annotations
					if (nodeName.equals("modelquality")) {
						XMLAttributes qualityAtts = currNode.getAttributes();
						for (int nattr = 0; nattr < qualityAtts.getLength(); nattr++) {
							String attName = qualityAtts.getName(nattr);
							String attValue = qualityAtts.getValue(nattr);
							annotations.put(attName, attValue);
						}
					}
					// Process other annotations
					else {
						String nodeValue = currNode.getChildAt(0)
								.getCharacters();
						annotations.put(nodeName, nodeValue);
					}
				}
			}
		}

		return annotations;
	}

	/**
	 * Parse an SBML ListOfCompartment and return a PmmXmlDoc.
	 * 
	 * @param compartments
	 *            : List of compartments
	 */
	public static PmmXmlDoc parseCompartments(
			final ListOf<Compartment> compartments) {
		PmmXmlDoc compartmentDoc = new PmmXmlDoc();
		for (Compartment compartment : compartments) {
			// Process annotation
			Annotation annot = compartment.getAnnotation();
			String compartmentName = compartment.getName();

			// if (annot != null) {
			// XMLNode nonRDFAnnot = annot.getNonRDFannotation();
			// // PMF compartment id obtained from the DB
			// String casNumber =
			// ReaderUtilities.parseComparmentAnnotation(nonRDFAnnot);
			// compartmentName = (String) DBKernel.getValue("Matrices",
			// "CAS_Nummer", casNumber, "Matrixname");
			// }
			MatrixXml matrix = new MatrixXml();
			matrix.setName(compartmentName);
			compartmentDoc.add(matrix);
		}

		return compartmentDoc;
	}

	public static String parseComparmentAnnotation(XMLNode annot) {
		// Search metadata container
		XMLNode metadata = null;
		for (int nChild = 0; nChild < annot.getChildCount(); nChild++) {
			XMLNode currNode = annot.getChildAt(nChild);
			String nodeName = currNode.getName();
			if (nodeName.equals("metadata")) {
				metadata = currNode;
				break;
			}
		}

		String casNumber = null;
		// Parse metadata container
		if (metadata != null) {
			for (int nTag = 0; nTag < metadata.getChildCount(); nTag++) {
				XMLNode currNode = metadata.getChildAt(nTag);
				String nodeName = currNode.getName();
				if (nodeName.equals("source")) {
					casNumber = currNode.getChildAt(0).getCharacters();
					int pos = casNumber.lastIndexOf("/");
					casNumber = casNumber.substring(pos + 1);
				}
			}
		}

		return casNumber;
	}

	/**
	 * Parse an SBML ListOfSpecies and return a PmmXmlDoc.
	 * 
	 * @param species
	 *            : List of species.
	 */
	public static PmmXmlDoc parseSpecies(final ListOf<Species> species) {
		PmmXmlDoc speciesDoc = new PmmXmlDoc();
		for (Species specie : species) {
			String speciesName = "";
			Integer speciesId = null;
			// Process annotation
			XMLNode nonRDFAnnot = specie.getAnnotation().getNonRDFannotation();
			if (nonRDFAnnot != null) {
				String casNumber = ReaderUtilities
						.parseSpeciesAnnotation(nonRDFAnnot);
				speciesName = (String) DBKernel.getValue("Agenzien",
						"CAS_Nummer", casNumber, "Agensname");
				speciesId = (Integer) DBKernel.getValue("Agenzien",
						"CAS_Nummer", casNumber, "ID");
			}
			AgentXml agentXml = new AgentXml(speciesId, speciesName, "");
			speciesDoc.add(agentXml);
		}
		return speciesDoc;
	}

	public static String parseSpeciesAnnotation(XMLNode annot) {
		// Search metadata container
		XMLNode metadata = null;
		for (int nChild = 0; nChild < annot.getChildCount(); nChild++) {
			XMLNode currNode = annot.getChildAt(nChild);
			String nodeName = currNode.getName();
			if (nodeName.equals("metadata")) {
				metadata = currNode;
				break;
			}
		}

		String casNumber = null;
		// Parse metadata container
		if (metadata != null) {
			for (int nTag = 0; nTag < metadata.getChildCount(); nTag++) {
				XMLNode currNode = metadata.getChildAt(nTag);
				String nodeName = currNode.getName();
				if (nodeName.equals("source")) {
					casNumber = currNode.getChildAt(0).getCharacters();
					int pos = casNumber.lastIndexOf("/");
					casNumber = casNumber.substring(pos + 1);
				}
			}
		}

		return casNumber;
	}

	// Create dependent variable
	public static PmmXmlDoc parseDep(final Species species,
			final Map<String, UnitsFromDB> units,
			final Map<String, Map<String, Double>> limits) {
		String name = species.getUnits(); // unit name
		UnitsFromDB dbUnit = units.get(name);

		// Retrieve unit data from dbUnit
		String category = dbUnit.getKind_of_property_quantity();
		String unit = dbUnit.getDisplay_in_GUI_as();
		String description = dbUnit.getDescription();

		DepXml dep = new DepXml("Value", "Value", category, unit, description);
		// Get limits
		if (limits.containsKey(name)) {
			Map<String, Double> depLimits = limits.get(name);
			if (depLimits.containsKey("MAX")) {
				dep.setMax(depLimits.get("MAX"));
			}
			if (depLimits.containsKey("MIN")) {
				dep.setMin(depLimits.get("MIN"));
			}
		}

		PmmXmlDoc depDoc = new PmmXmlDoc(dep);

		return depDoc;
	}

	// Create independent variables
	public static PmmXmlDoc parseIndeps(final ListOf<Parameter> params,
			final Map<String, UnitsFromDB> units,
			final Map<String, Map<String, Double>> limits) {
		PmmXmlDoc indepDoc = new PmmXmlDoc();

		for (Parameter param : params) {
			if (!param.isConstant()) {
				String unitName = param.getUnits(); // unit name
				UnitsFromDB dbUnit = units.get(unitName);

				String name = "";
				String category = "";
				String unit = "";
				String description = "";

				if (dbUnit != null) {
					// Retrieve unit data from dbUnit
					name = dbUnit.getKind_of_property_quantity();
					category = dbUnit.getKind_of_property_quantity();
					unit = dbUnit.getDisplay_in_GUI_as();
					description = dbUnit.getDescription();
				} else if (unitName.equals("pmf_celsius")) {
					name = unitName;
					category = Categories.getTempCategory().getName();
					unit = "°C";
					description = "degree Celsius";
				}

				// other fields
				Double min = null, max = null;

				IndepXml indep = new IndepXml(name, min, max, category, unit);
				indep.setDescription(description);
				// Get limits
				String paramName = param.getId();
				if (limits.containsKey(paramName)) {
					Map<String, Double> indepLimits = limits.get(paramName);
					if (indepLimits.containsKey("MAX")) {
						indep.setMax(indepLimits.get("MAX"));
					}
					if (indepLimits.containsKey("MIN")) {
						indep.setMin(indepLimits.get("MIN"));
					}
				}

				indepDoc.add(indep);
			}
		}

		return indepDoc;
	}

	// Create constant variables
	public static PmmXmlDoc parseConsts(final ListOf<Parameter> params,
			final Map<String, UnitsFromDB> units,
			final Map<String, Map<String, Double>> limits) {
		PmmXmlDoc constsDoc = new PmmXmlDoc();

		for (Parameter param : params) {
			if (param.isConstant()) {
				String unitName = param.getUnits(); // unit name
				UnitsFromDB dbUnit = units.get(unitName);

				String name = "", category = "", unit = "", description = "";

				// Retrieve unit data from dbUnit
				if (dbUnit != null) {
					name = dbUnit.getName();
					category = dbUnit.getKind_of_property_quantity();
					unit = dbUnit.getDisplay_in_GUI_as();
					description = dbUnit.getDescription();
				} else if (unitName.equals("pmf_celsius")) {
					name = unitName;
					category = Categories.getTempCategory().getName();
					unit = "°C";
					description = "degree Celsius";
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
					Map<String, Double> paramLimits = limits.get(paramName);
					if (paramLimits.containsKey("MAX")) {
						paramXml.setMax(paramLimits.get("MAX"));
					}
					if (paramLimits.containsKey("MIN")) {
						paramXml.setMin(paramLimits.get("MIN"));
					}
				}

				constsDoc.add(paramXml);
			}
		}

		return constsDoc;
	}

	/**
	 * Parse the formula of the model (rule) using the PMM Lab format:
	 * "Value = actual formula"
	 */
	public static String parseFormula(final AssignmentRule rule) {
		return "Value=" + rule.getMath().toFormula();
	}

	public static Map<String, Map<String, Double>> parseConstraint(
			final ListOf<Constraint> constraints) {
		Map<String, Map<String, Double>> limits = new HashMap<>();

		for (int n = 0; n < constraints.getChildCount(); n++) {
			Constraint currConstraint = constraints.get(n);
			ASTNode math = currConstraint.getMath();
			List<ASTNode> nodes = math.getListOfNodes();

			HashMap<String, Double> varLimits = new HashMap<>();

			// constraint with a single condition
			if (nodes.size() == 1) {
				ASTNode node = nodes.get(0);
				List<ASTNode> condNodes = node.getListOfNodes();
				String var = condNodes.get(0).getName();
				ASTNode valNode = condNodes.get(1);
				double val = 0;
				if (valNode.isReal()) {
					val = valNode.getReal();
				} else if (valNode.isInteger()) {
					val = valNode.getInteger();
				}
//				double val = condNodes.get(1).getMantissa();

				ASTNode.Type nodeType = node.getType();

				if (nodeType == ASTNode.Type.RELATIONAL_LT
						|| nodeType == ASTNode.Type.RELATIONAL_LEQ) {
					// set maximum
					varLimits.put("MAX", val);
				} else if (nodeType == ASTNode.Type.RELATIONAL_GT
						|| nodeType == ASTNode.Type.RELATIONAL_GEQ) {
					// set minimum
					varLimits.put("MIN", val);
				}

				limits.put(var, varLimits);
			}

			// constraint with two conditions
			else if (nodes.size() == 2) {
				ASTNode leftNode = nodes.get(0);
				List<ASTNode> leftNodes = leftNode.getListOfNodes();
				String leftVar = leftNodes.get(0).getName();
				ASTNode valNode = leftNodes.get(1);
				double min = 0;
				if (valNode.isReal()) {
					min = valNode.getReal();
				} else if (valNode.isInteger()) {
					min = valNode.getInteger();
				}
//				double min = leftNodes.get(1).getMantissa();
				varLimits.put("MIN", min);

				ASTNode rightNode = nodes.get(1);
				List<ASTNode> rightNodes = rightNode.getListOfNodes();
				String rightVar = rightNodes.get(0).getName();
				valNode = rightNodes.get(1);
				double max = 0;
				if (valNode.isReal()) {
					max = valNode.getReal();
				} else if (valNode.isInteger()) {
					max = valNode.getInteger();
				}

//				double max = rightNodes.get(1).getMantissa();
				varLimits.put("MAX", max);

				limits.put(leftVar, varLimits);
			}
		}

		return limits;
	}

	// Create a CatalogModelXml from the model annotations and its formula
	public static CatalogModelXml createCatModel(
			final Map<String, String> annotations, final AssignmentRule rule) {

		// Initialize variables
		Integer id = null; // model id
		String name = ""; // model name
		int type = SBMLUtil.CLASS_TO_INT.get("unknown"); // model class
		String formula = ReaderUtilities.parseFormula(rule); // model formula

		if (annotations != null) {
			// Get values from annotations
			if (annotations.containsKey("title")) {
				name = annotations.get("title");
			}
			if (annotations.containsKey("subject")) {
				String modelClass = annotations.get("subject");
				type = SBMLUtil.CLASS_TO_INT.get(modelClass);
			}
		}

		CatalogModelXml catModel = new CatalogModelXml(id, name, formula, type);
		return catModel;
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
		ListOf<Compartment> listOfCompartments = model.getListOfCompartments();
		ListOf<Species> listOfSpecies = model.getListOfSpecies();
		ListOf<Parameter> listOfParameters = model.getListOfParameters();
		ListOf<Rule> listOfRules = model.getListOfRules();
		AssignmentRule assignmentRule = (AssignmentRule) listOfRules.get(0);

		DBUnits dbUnits = new DBUnits();

		// Parse model annotations
		XMLNode modelAnnotation = model.getAnnotation().getNonRDFannotation();
		Map<String, String> annotations = null;
		// If the model is annotated then parse the annotations
		if (modelAnnotation != null) {
			annotations = ReaderUtilities.parseAnnotation(modelAnnotation);
		}

		CatalogModelXml catModel = ReaderUtilities.createCatModel(annotations,
				assignmentRule);

		// // Get reference
		// String reference = "";
		// if (annotations.containsKey("references")) {
		// reference = annotations.get("references");
		// }

		// Parse constraints
		ListOf<Constraint> constraints = model.getListOfConstraints();
		Map<String, Map<String, Double>> limits = ReaderUtilities
				.parseConstraint(constraints);

		// time series cells
		String combaseID = model.getId();
		PmmXmlDoc organismCell = ReaderUtilities.parseSpecies(listOfSpecies);
		PmmXmlDoc matrixCell = ReaderUtilities
				.parseCompartments(listOfCompartments);
		PmmXmlDoc mdDataCell = new PmmXmlDoc();
		PmmXmlDoc miscCell = new PmmXmlDoc();

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
		PmmXmlDoc depCell = ReaderUtilities.parseDep(listOfSpecies.get(0),
				units, limits);
		PmmXmlDoc indepCell = ReaderUtilities.parseIndeps(listOfParameters,
				units, limits);
		PmmXmlDoc paramCell = ReaderUtilities.parseConsts(listOfParameters,
				units, limits);

		EstModelXml estModel = ReaderUtilities.createEstModel(annotations);

		PmmXmlDoc estModelCell = new PmmXmlDoc(estModel);

		PmmXmlDoc mLiteratureCell = new PmmXmlDoc();
		PmmXmlDoc emLiteratureCell = new PmmXmlDoc();
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

	/**
	 * Parse the formula of the secondary model
	 */
	private static String parseSecFormula(final AssignmentRule rule) {
		return String.format("%s=%s", rule.getVariable(), rule.getMath()
				.toFormula());
	}

	private static PmmXmlDoc parseSecDep(final AssignmentRule rule,
			final ListOf<Parameter> params,
			final Map<String, UnitsFromDB> units,
			final Map<String, Map<String, Double>> limits) {
		String depName = rule.getVariable();
		Parameter depParam = params.get(depName);

		String unitName = depParam.getUnits();
		UnitsFromDB dbUnit = units.get(unitName);

		// Retrieve unit data from dbUnit
		String category = dbUnit.getKind_of_property_quantity();
		String unit = dbUnit.getDisplay_in_GUI_as();
		String description = dbUnit.getDescription();

		// other data
		String name = depParam.getId();
		String origname = depParam.getId();

		DepXml depXml = new DepXml(name, origname, category, unit, description);

		// Get limits
		if (limits.containsKey(unitName)) {
			Map<String, Double> depLimits = limits.get(unitName);
			if (depLimits.containsKey("MAX")) {
				depXml.setMax(depLimits.get("MAX"));
			}
			if (depLimits.containsKey("MIN")) {
				depXml.setMin(depLimits.get("MIN"));
			}
		}

		PmmXmlDoc depDoc = new PmmXmlDoc(depXml);
		return depDoc;
	}

	/**
	 * Parse independent parameters from a secondary model.
	 * 
	 * <ol>
	 * <li>Search non constant parameters not names as the dep.</li>
	 * <li>Get unit name from the parameter.</li>
	 * <li>Get unit from DB</li>
	 * <li>Get data from the parameter</li>
	 * <li>Get parameter limits using its unit's name</li>
	 * </ol>
	 */
	private static PmmXmlDoc parseSecIndeps(final String depName,
			final ListOf<Parameter> params,
			final Map<String, UnitsFromDB> units,
			final Map<String, Map<String, Double>> limits) {
		PmmXmlDoc indepDoc = new PmmXmlDoc();

		// Search and add independent parameters
		for (Parameter param : params) {
			String id = param.getId();
			if (!id.equals(depName) && !param.isConstant()) {
				String unitName = param.getUnits();

				String category, unit, description;

				if (unitName.equals("pmf_celsius")) {
					category = Categories.getTempCategory().getName();
					unit = "°C";
					description = "degree Celsius";
				} else {
					// Retrieve unit data from dbUnit
					UnitsFromDB dbUnit = units.get(unitName);
					category = dbUnit.getKind_of_property_quantity();
					unit = dbUnit.getDisplay_in_GUI_as();
					description = dbUnit.getDescription();
				}

				// other data
				String name = id, origname = id;
				Double min = null, max = null;

				if (limits.containsKey(id)) {
					Map<String, Double> indepLimits = limits.get(id);
					if (indepLimits.containsKey("MAX")) {
						max = indepLimits.get("MAX");
					}
					if (indepLimits.containsKey("MIN")) {
						min = indepLimits.get("MIN");
					}
				}

				IndepXml indepXml = new IndepXml(name, origname, min, max,
						category, unit, description);

				indepDoc.add(indepXml);
			}
		}

		return indepDoc;
	}

	public static List<KnimeTuple> parseDocument(SBMLDocument doc) {
		Model model = doc.getModel();
		ListOf<Compartment> listOfCompartments = model.getListOfCompartments();
		ListOf<Species> listOfSpecies = model.getListOfSpecies();
		ListOf<Parameter> listOfParameters = model.getListOfParameters();
		ListOf<Rule> listOfRules = model.getListOfRules();
		AssignmentRule assignmentRule = (AssignmentRule) listOfRules.get(0);

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
			annotations = ReaderUtilities.parseAnnotation(modelAnnotation);
		}

		// // Get reference
		// String reference = "";
		// if (annotations.containsKey("references")) {
		// reference = annotations.get("references");
		// }

		// Parse constraints
		ListOf<Constraint> constraints = model.getListOfConstraints();
		Map<String, Map<String, Double>> limits = ReaderUtilities
				.parseConstraint(constraints);

		// time series cells
		String combaseID = model.getId();
		PmmXmlDoc organismCell = ReaderUtilities.parseSpecies(listOfSpecies);
		PmmXmlDoc matrixCell = ReaderUtilities
				.parseCompartments(listOfCompartments);
		PmmXmlDoc mdDataCell = new PmmXmlDoc();
		PmmXmlDoc miscCell = new PmmXmlDoc();
		PmmXmlDoc mdInfoCell = new PmmXmlDoc(new MdInfoXml(null, null, null,
				null, null));
		PmmXmlDoc mdLiteratureCell = new PmmXmlDoc();
		String mdDBUID = "?";

		// primary model cells
		CatalogModelXml catModel = ReaderUtilities.createCatModel(annotations,
				assignmentRule);
		PmmXmlDoc catModelCell = new PmmXmlDoc(catModel);

		Map<String, UnitsFromDB> units = dbUnits.getUnits(model
				.getListOfUnitDefinitions());
		PmmXmlDoc depCell = ReaderUtilities.parseDep(listOfSpecies.get(0),
				units, limits);
		PmmXmlDoc indepCell = ReaderUtilities.parseIndeps(listOfParameters,
				units, limits);
		PmmXmlDoc paramCell = ReaderUtilities.parseConsts(listOfParameters,
				units, limits);

		// Parse uncertainty measures from the document's annotations
		EstModelXml estModel = ReaderUtilities.createEstModel(annotations);
		PmmXmlDoc estModelCell = new PmmXmlDoc(estModel);

		PmmXmlDoc mLiteratureCell = new PmmXmlDoc();
		PmmXmlDoc emLiteratureCell = new PmmXmlDoc();
		String mDBUID = "?";

		final int condID = MathUtilities.getRandomNegativeInt();
		final int globalModelID = MathUtilities.getRandomNegativeInt();

		for (ModelDefinition secModel : modelDefinitions) {
			ListOf<Parameter> secParams = secModel.getListOfParameters();
			ListOf<Rule> secRules = secModel.getListOfRules();
			AssignmentRule secAssignmentRule = (AssignmentRule) secRules.get(0);
			String depName = secAssignmentRule.getVariable();

			// Parse constraints
			ListOf<Constraint> secConstraints = secModel.getListOfConstraints();
			Map<String, Map<String, Double>> secLimits = ReaderUtilities
					.parseConstraint(secConstraints);

			// secondary model columns (19-27)
			String secFormula = parseSecFormula(secAssignmentRule);
			CatalogModelXml catModelSec = new CatalogModelXml(
					MathUtilities.getRandomNegativeInt(), "", secFormula, null);
			PmmXmlDoc catModelSecCell = new PmmXmlDoc(catModelSec);

			// PmmXmlDoc dependentSecCell =
			// parseSecDependentParameter(secParams,
			// depName, secModel.getId(), secLimits);
			units = dbUnits.getUnits(secModel.getListOfUnitDefinitions());

			PmmXmlDoc dependentSecCell = parseSecDep(secAssignmentRule,
					secParams, units, secLimits);
			PmmXmlDoc independentSecCell = parseSecIndeps(depName, secParams,
					units, secLimits);
			PmmXmlDoc parameterSecCell = ReaderUtilities.parseConsts(secParams,
					units, secLimits);

			PmmXmlDoc estModelSecCell = new PmmXmlDoc();
			estModelSecCell.add(new EstModelXml(MathUtilities
					.getRandomNegativeInt(), null, null, null, null, null,
					null, 0));

			PmmXmlDoc mLiteratureSecCell = new PmmXmlDoc();
			PmmXmlDoc emLiteratureSecCell = new PmmXmlDoc();
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