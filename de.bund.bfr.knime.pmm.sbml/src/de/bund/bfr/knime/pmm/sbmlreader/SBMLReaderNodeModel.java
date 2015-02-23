package de.bund.bfr.knime.pmm.sbmlreader;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.stream.XMLStreamException;

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
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.annotation.ReferenceNode;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
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
import de.bund.bfr.knime.pmm.sbmlutil.Limits;
import de.bund.bfr.knime.pmm.sbmlutil.LimitsConstraint;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;
import de.bund.bfr.knime.pmm.sbmlutil.Model1Rule;
import de.bund.bfr.knime.pmm.sbmlutil.Model2Rule;
import de.bund.bfr.knime.pmm.sbmlutil.Organism;

/**
 * This is the model implementation of SBMLReader.
 * 
 * Author: Miguel de Alba Aparicio (malba@optimumquality.es)
 */
public class SBMLReaderNodeModel extends NodeModel {

	// configuration keys
	public static final String CFGKEY_FILE = "filename";
	public static final String CFGKEY_FOLDER = "foldername";
	public static final String CFGKEY_ZIP = "zipname";
	
	public static final String CFGKEY_SOURCE = "source";
	public static final String CFGKEY_MODEL_TYPE = "modeltype";

	// defaults for persistent state
	private static final String DEFAULT_FILE = "c:/temp/foo.xml";
	private static final String DEFAULT_FOLDER = "c:/temp";
	private static final String DEFAULT_ZIP = "c:/temp/foo.zip";


	// persistent state
	private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE,
			DEFAULT_FILE);
	private SettingsModelString folder = new SettingsModelString(CFGKEY_FOLDER,
			DEFAULT_FOLDER);
	private SettingsModelString zip = new SettingsModelString(CFGKEY_ZIP, DEFAULT_ZIP);
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
		} else if (source.getStringValue().equals("folder")) {
			table = parseFolder(exec);
		} else {
			table = loadZip(exec);
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
		zip.saveSettingsTo(settings);
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
		zip.loadSettingsFrom(settings);
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
		zip.validateSettings(settings);
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
	
	private BufferedDataTable[] loadZip(final ExecutionContext exec) {
		// create container with modelType
		Boolean isTertiary = modelType.getStringValue().equals("tertiary");
		KnimeSchema schema = isTertiary ? SchemaFactory.createM12DataSchema() : SchemaFactory.createM1DataSchema();
		BufferedDataContainer container = exec.createDataContainer(schema.createSpec());
		
		// open zip
		ZipInputStream zipFile = null;
		try {
			zipFile = new ZipInputStream(new FileInputStream(zip.getStringValue()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SBMLReader reader = new SBMLReader();

		// add every entry
		ZipEntry entry;
		try {
			while ((entry = zipFile.getNextEntry()) != null) {
				// skip non xml files
				if (!entry.getName().endsWith("xml"))
					continue;
				
				StringBuilder s = new StringBuilder();
				byte[] buffer = new byte[1024];
				int read = 0;
				while ((read = zipFile.read(buffer, 0, 1024)) >= 0) {
					s.append(new String(buffer, 0, read));
				}
				
				SBMLDocument doc = reader.readSBMLFromString(s.toString());
				
				CompSBMLDocumentPlugin plugin = (CompSBMLDocumentPlugin) doc.getPlugin("comp");
				Boolean isDocTertiary = plugin.getListOfModelDefinitions().size() > 0;
				
				if (isTertiary != isDocTertiary)
					continue;
				
				if (isTertiary) {
					List<KnimeTuple> tuples = TertiaryModelParser.parseDocument(doc);
					for (KnimeTuple tuple : tuples) {
						container.addRowToTable(tuple);
					}
				} else {
					KnimeTuple tuple = PrimaryModelParser.parseDocument(doc);
					container.addRowToTable(tuple);
				}
			}
		} catch (IOException | XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			zipFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		container.close();
		BufferedDataTable[] table = {container.getTable()};
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
						XMLAttributes qualityAtts = node.getAttributes();
						for (int nattr = 0; nattr < qualityAtts.getLength(); nattr++) {
							String attName = qualityAtts.getName(nattr);
							String attValue = qualityAtts.getValue(nattr);
							annotations.put(attName, attValue);
						}
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

		PmmXmlDoc depDoc = new PmmXmlDoc(dep);

		return depDoc;
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
			Model2Rule rule2 = new Model2Rule((AssignmentRule) secModel.getRule(0));
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
			XMLNode secModelAnnotation = model.getAnnotation().getNonRDFannotation();
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