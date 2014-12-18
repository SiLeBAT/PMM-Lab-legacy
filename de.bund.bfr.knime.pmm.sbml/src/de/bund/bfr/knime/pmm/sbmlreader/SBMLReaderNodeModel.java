package de.bund.bfr.knime.pmm.sbmlreader;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.sbml.jsbml.Compartment;
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
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

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
		if (source.getStringValue() == "file") {
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
		KnimeSchema schema;
		BufferedDataContainer container = null;
		if (isTertiary) {
			schema = SchemaFactory.createM12DataSchema();
			// Create container
			container = exec.createDataContainer(schema.createSpec());
			// Parse document
			List<KnimeTuple> tuples = TertiaryModelParser.parseDocument(doc);
			for (KnimeTuple tuple : tuples) {
				container.addRowToTable(tuple);
			}
		} else {
			schema = SchemaFactory.createM1DataSchema();
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

class PrimaryModelParser {

	/**
	 * Parse an SBML ListOfCompartment and return a PmmXmlDoc.
	 * 
	 * @param listOfCompartments
	 */
	private static PmmXmlDoc parseCompartments(
			ListOf<Compartment> listOfCompartments) {
		PmmXmlDoc compartmentDoc = new PmmXmlDoc();
		for (Compartment compartment : listOfCompartments) {
			String name = compartment.getName();
			MatrixXml matrix = new MatrixXml();
			matrix.setName(name);
			compartmentDoc.add(matrix);
		}

		return compartmentDoc;
	}

	/**
	 * Parse an SBML ListOfSpecies and return a PmmXmlDoc.
	 * 
	 * @param listOfSpecies
	 */
	private static PmmXmlDoc parseSpecies(ListOf<Species> listOfSpecies) {
		PmmXmlDoc speciesDoc = new PmmXmlDoc();
		for (Species specie : listOfSpecies) {
			String name = specie.getName();
			AgentXml agentXml = new AgentXml();
			agentXml.setName(name);
			speciesDoc.add(agentXml);
		}
		return speciesDoc;
	}

	/**
	 * Parse the SBML specie to produce the dependent variable and return a
	 * PmmXmlDoc.
	 * 
	 * @param specie
	 */
	private static PmmXmlDoc parseDependentParameter(Species specie) {
		String origName = specie.getId();
		String unit = specie.getUnits();

		DepXml dep = new DepXml("Value", origName, "", unit, "");
		PmmXmlDoc depDoc = new PmmXmlDoc(dep);
		return depDoc;
	}

	/**
	 * Parse the independent parameters and return a PmmXmlDoc.
	 * 
	 * @param listOfParameters
	 */
	private static PmmXmlDoc parseIndependentParameters(
			ListOf<Parameter> listOfParameters) {
		PmmXmlDoc indepDoc = new PmmXmlDoc();
		for (Parameter param : listOfParameters) {
			if (!param.isConstant()) {
				String id = param.getId();
				Double min = null, max = null;
				String unit = param.getUnits();

				IndepXml newCell = new IndepXml(id, min, max);
				newCell.setUnit(unit);
				indepDoc.add(newCell);
			}
		}
		return indepDoc;
	}

	/**
	 * Parse constant parameters and return a PmmXmlDoc.
	 * 
	 * @param listOfParameters
	 */
	private static PmmXmlDoc parseConstantParameters(
			ListOf<Parameter> listOfParameters) {
		PmmXmlDoc constsDoc = new PmmXmlDoc();
		for (Parameter param : listOfParameters) {
			if (param.isConstant()) {
				String id = param.getId();
				double value = param.getValue();
				String unit = param.getUnits();

				ParamXml newCell = new ParamXml(id, value);
				newCell.setUnit(unit);
				constsDoc.add(newCell);
			}
		}
		return constsDoc;
	}

	private static String parseFormula(AssignmentRule assignmentRule) {
		String leftHand = assignmentRule.getVariable();
		String rightHand = assignmentRule.getFormula();
		String formula = leftHand + "=" + rightHand;
		return formula;
	}

	public static KnimeTuple parseDocument(SBMLDocument doc) {
		Model model = doc.getModel();
		ListOf<Compartment> listOfCompartments = model.getListOfCompartments();
		ListOf<Species> listOfSpecies = model.getListOfSpecies();
		ListOf<Parameter> listOfParameters = model.getListOfParameters();
		ListOf<Rule> listOfRules = model.getListOfRules();
		AssignmentRule assignmentRule = (AssignmentRule) listOfRules.get(0);

		// time series cells
		String condID = "-1";
		String combaseID = model.getId();
		PmmXmlDoc organismCell = parseSpecies(listOfSpecies);
		PmmXmlDoc matrixCell = parseCompartments(listOfCompartments);
		PmmXmlDoc mdDataCell = new PmmXmlDoc();
		PmmXmlDoc miscCell = new PmmXmlDoc();
		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);
		PmmXmlDoc mdInfoCell = new PmmXmlDoc(mdInfo);
		PmmXmlDoc mdLiteratureCell = new PmmXmlDoc();
		String mdDBUID = "?";

		// primary model cells
		String formula = parseFormula(assignmentRule);
		CatalogModelXml catModel = new CatalogModelXml(1, "", formula, 1, "");
		PmmXmlDoc catModelCell = new PmmXmlDoc(catModel);
		// Parse dependent parameter (primary models only have one dependent
		// variable)
		PmmXmlDoc depCell = parseDependentParameter(listOfSpecies.get(0));
		// Parse independent parameter
		PmmXmlDoc indepCell = parseIndependentParameters(listOfParameters);
		// Parse constant parameter
		PmmXmlDoc paramCell = parseConstantParameters(listOfParameters);
		EstModelXml estModel = new EstModelXml(
				MathUtilities.getRandomNegativeInt(), null, null, null, null,
				null, null, null, null, 0);
		PmmXmlDoc estModelCell = new PmmXmlDoc(estModel);
		PmmXmlDoc mLiteratureCell = new PmmXmlDoc();
		PmmXmlDoc emLiteratureCell = new PmmXmlDoc();
		int databaseWritable = Model1Schema.NOTWRITABLE;
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
		row.setValue(Model1Schema.ATT_DATABASEWRITABLE, databaseWritable);
		row.setValue(Model1Schema.ATT_DBUUID, mDBUID);

		return row;
	}
}

class TertiaryModelParser {

	/**
	 * Parse an SBML ListOfCompartment and return a PmmXmlDoc.
	 * 
	 * @param listOfCompartments
	 */
	private static PmmXmlDoc parseCompartments(
			ListOf<Compartment> listOfCompartments) {
		PmmXmlDoc compartmentDoc = new PmmXmlDoc();
		for (Compartment compartment : listOfCompartments) {
			String name = compartment.getName();
			MatrixXml matrix = new MatrixXml();
			matrix.setName(name);
			compartmentDoc.add(matrix);
		}
		return compartmentDoc;
	}

	/**
	 * Parse an SBML ListOfSpecies and return a PmmXmlDoc.
	 * 
	 * @param listOfSpecies
	 */
	private static PmmXmlDoc parseSpecies(ListOf<Species> listOfSpecies) {
		PmmXmlDoc speciesDoc = new PmmXmlDoc();
		for (Species species : listOfSpecies) {
			String name = species.getName();
			AgentXml agent = new AgentXml();
			agent.setName(name);
			speciesDoc.add(agent);
		}
		return speciesDoc;
	}

	/**
	 * Parse the SBML specie to produce the dependent variable and return a
	 * PmmXmlDoc.
	 * 
	 * @param specie
	 */
	private static PmmXmlDoc parseDependentParameter(Species specie) {
		String origName = specie.getId();
		String unit = specie.getUnits();

		DepXml dep = new DepXml("Value", origName, "", unit, "");
		PmmXmlDoc depDoc = new PmmXmlDoc(dep);
		return depDoc;
	}

	/**
	 * Parse the independent parameters and return a PmmXmlDoc.
	 * 
	 * @param listOfParameters
	 */
	private static PmmXmlDoc parseIndependentParameters(
			ListOf<Parameter> listOfParameters) {
		PmmXmlDoc indepDoc = new PmmXmlDoc();
		for (Parameter param : listOfParameters) {
			if (!param.isConstant()) {
				String id = param.getId();
				Double min = null, max = null;
				String unit = param.getUnits();

				IndepXml indep = new IndepXml(id, min, max);
				indep.setUnit(unit);
				indepDoc.add(indep);
			}
		}
		return indepDoc;
	}

	/**
	 * Parse constant parameters and return a PmmXmlDoc.
	 * 
	 * @param listOfParameters
	 */
	private static PmmXmlDoc parseConstantParameters(
			ListOf<Parameter> listOfParameters) {
		PmmXmlDoc constsDoc = new PmmXmlDoc();
		for (Parameter param : listOfParameters) {
			if (param.isConstant()) {
				String id = param.getId();
				double value = param.getValue();
				String unit = param.getUnits();

				ParamXml c = new ParamXml(id, value);
				c.setUnit(unit);
				constsDoc.add(c);
			}
		}
		return constsDoc;
	}

	private static String parseFormula(AssignmentRule assignmentRule) {
		String leftHand = assignmentRule.getVariable();
		String rightHand = assignmentRule.getFormula();
		String formula = leftHand + "=" + rightHand;
		return formula;
	}

	private static PmmXmlDoc parseSecDependentParameter(
			ListOf<Parameter> params, String depName, String secModelId) {
		// Search dependent param
		Parameter p = null;
		for (Parameter param : params) {
			if (param.getId().equals(depName)) {
				p = param;
				break;
			}
		}

		// TODO:
		// if (p == null)
		// throw exc.

		String unit = p.getUnits();
		DepXml depXml = new DepXml("Value", secModelId, "", unit, "");
		PmmXmlDoc depDoc = new PmmXmlDoc(depXml);
		return depDoc;
	}

	private static PmmXmlDoc parseSecIndependentParameters(
			ListOf<Parameter> params, String depName) {
		PmmXmlDoc indepDoc = new PmmXmlDoc();
		// Search and add independent parameters
		for (Parameter param : params) {
			if (!param.getId().equals(depName) && !param.isConstant()) {
				String id = param.getId();
				Double min = null, max = null;
				String unit = param.getUnits();

				IndepXml indepXml = new IndepXml(id, min, max);
				indepXml.setUnit(unit);
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

		CompSBMLDocumentPlugin compPlugin = (CompSBMLDocumentPlugin) doc
				.getPlugin(CompConstants.shortLabel);
		ListOf<ModelDefinition> modelDefinitions = compPlugin
				.getListOfModelDefinitions();

		// create n rows for n secondary models
		List<KnimeTuple> rows = new ArrayList<>();

		// time series cells
		String condID = "-1";
		String combaseID = model.getId();
		PmmXmlDoc organismCell = parseSpecies(listOfSpecies);
		PmmXmlDoc matrixCell = parseCompartments(listOfCompartments);
		PmmXmlDoc mdDataCell = new PmmXmlDoc();
		PmmXmlDoc miscCell = new PmmXmlDoc();
		PmmXmlDoc mdInfoCell = new PmmXmlDoc();
		PmmXmlDoc mdLiteratureCell = new PmmXmlDoc();
		String mdDBUID = "?";

		// primary model cells
		String formula = parseFormula(assignmentRule);
		CatalogModelXml catModel = new CatalogModelXml(1, "", formula, 1, "");
		PmmXmlDoc catModelCell = new PmmXmlDoc(catModel);

		PmmXmlDoc depCell = parseDependentParameter(listOfSpecies.get(0));
		PmmXmlDoc indepCell = parseIndependentParameters(listOfParameters);
		PmmXmlDoc paramCell = parseConstantParameters(listOfParameters);
		PmmXmlDoc estModelCell = new PmmXmlDoc();
		PmmXmlDoc mLiteratureCell = new PmmXmlDoc();
		PmmXmlDoc emLiteratureCell = new PmmXmlDoc();
		int databaseWritable = Model1Schema.NOTWRITABLE;
		String mDBUID = "?";

		for (ModelDefinition secModel : modelDefinitions) {
			ListOf<Parameter> secParams = secModel.getListOfParameters();
			ListOf<Rule> secRules = secModel.getListOfRules();
			AssignmentRule secAssignmentRule = (AssignmentRule) secRules.get(0);
			String depName = secAssignmentRule.getVariable();

			// secondary model columns (19-27)
			String secFormula = parseFormula(secAssignmentRule);
			CatalogModelXml catModelSec = new CatalogModelXml(1, "",
					secFormula, 1, "");
			PmmXmlDoc catModelSecCell = new PmmXmlDoc(catModelSec);
			PmmXmlDoc dependentSecCell = parseSecDependentParameter(secParams,
					depName, secModel.getId());
			PmmXmlDoc independentSecCell = parseSecIndependentParameters(
					secParams, depName);
			PmmXmlDoc parameterSecCell = parseConstantParameters(secParams);

			PmmXmlDoc estModelSecCell = new PmmXmlDoc();
			PmmXmlDoc mLiteratureSecCell = new PmmXmlDoc();
			PmmXmlDoc emLiteratureSecCell = new PmmXmlDoc();
			String mDBUIDSEC = "?";
			String globalModelID = "?";

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
			row.setValue(Model1Schema.ATT_DATABASEWRITABLE, databaseWritable);
			row.setValue(Model1Schema.ATT_DBUUID, mDBUID);

			row.setValue(Model2Schema.ATT_MODELCATALOG, catModelSecCell);
			row.setValue(Model2Schema.ATT_DEPENDENT, dependentSecCell);
			row.setValue(Model2Schema.ATT_INDEPENDENT, independentSecCell);
			row.setValue(Model2Schema.ATT_PARAMETER, parameterSecCell);
			row.setValue(Model2Schema.ATT_ESTMODEL, estModelSecCell);
			row.setValue(Model2Schema.ATT_MLIT, mLiteratureSecCell);
			row.setValue(Model2Schema.ATT_EMLIT, emLiteratureSecCell);
			row.setValue(Model2Schema.ATT_DBUUID, mDBUIDSEC);
			row.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, globalModelID);

			rows.add(row);
		}

		return rows;
	}
}