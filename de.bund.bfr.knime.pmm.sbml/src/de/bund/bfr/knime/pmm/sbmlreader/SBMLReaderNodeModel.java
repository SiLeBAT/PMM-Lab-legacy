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
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.Rule;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
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
			List<KnimeTuple> tuples = parseTertiaryModel(doc,
					documentCompPlugin);
			for (KnimeTuple tuple : tuples) {
				container.addRowToTable(tuple);
			}
		} else {
			schema = SchemaFactory.createM1DataSchema();
			// Create container
			container = exec.createDataContainer(schema.createSpec());
			// Parse document
			KnimeTuple tuple = parsePrimaryModel(doc);
			container.addRowToTable(tuple);
		}
		container.close();

		BufferedDataTable[] table = { container.getTable() };
		return table;
	}

	// TODO(malba)
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
				List<KnimeTuple> tuples = parseTertiaryModel(doc,
						documentPlugin);
				for (KnimeTuple tuple : tuples) {
					container.addRowToTable(tuple);
				}
			} else {
				KnimeTuple tuple = parsePrimaryModel(doc);
				container.addRowToTable(tuple);
			}
		}
		container.close();

		BufferedDataTable[] table = { container.getTable() };
		return table;
	}

	public KnimeTuple parsePrimaryModel(SBMLDocument doc) {

		ListOf<Compartment> listOfCompartments = doc.getModel()
				.getListOfCompartments();
		ListOf<Species> listOfSpecies = doc.getModel().getListOfSpecies();
		ListOf<Parameter> listOfParameters = doc.getModel()
				.getListOfParameters();
		ListOf<Rule> listOfRules = doc.getModel().getListOfRules();
		AssignmentRule assignmentRule = (AssignmentRule) listOfRules.get(0);

		// time series cells
		String condID = "-1";

		String combaseID = "?";

		List<AgentXml> organisms = parseSpecies(listOfSpecies);
		PmmXmlDoc organismCell = new PmmXmlDoc();
		for (AgentXml agent : organisms) {
			organismCell.add(agent);
		}

		List<MatrixXml> matrices = parseCompartments(listOfCompartments);
		PmmXmlDoc matrixCell = new PmmXmlDoc();
		for (MatrixXml matrix : matrices) {
			matrixCell.add(matrix);
		}

		TimeSeriesXml mdData = new TimeSeriesXml(null, null, null, null, null,
				null, null, null, null, null);
		PmmXmlDoc mdDataCell = new PmmXmlDoc(mdData);

		MiscXml misc = new MiscXml(null, null, null, null, null, null);
		PmmXmlDoc miscCell = new PmmXmlDoc(misc);

		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);
		PmmXmlDoc mdInfoCell = new PmmXmlDoc(mdInfo);

		LiteratureItem mdLiterature = new LiteratureItem(null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null);
		PmmXmlDoc mdLiteratureCell = new PmmXmlDoc(mdLiterature);

		String mdDBUID = "?";

		// primary model cells
		String formula = parseFormula(assignmentRule);
		CatalogModelXml catModel = new CatalogModelXml(1, "", formula, 1, "");
		PmmXmlDoc catModelCell = new PmmXmlDoc(catModel);

		DepXml dep = new DepXml("Value", "Value",
				"Number Content (count/mass)", "Number content (count/mass)",
				"response");
		PmmXmlDoc depCell = new PmmXmlDoc(dep);

		IndepXml indep = new IndepXml("Time", "Time", null, null, "Time", "h",
				"elapsed time");
		PmmXmlDoc indepCell = new PmmXmlDoc(indep);

		List<ParamXml> paramXmls = parseParams(listOfParameters);
		PmmXmlDoc paramCell = new PmmXmlDoc();
		// Add params
		for (ParamXml param : paramXmls) {
			paramCell.add(param);
		}

		EstModelXml estModel = new EstModelXml(-1942671369, null, null, null,
				null, null, null, null, null, 0);
		PmmXmlDoc estModelCell = new PmmXmlDoc(estModel);

		LiteratureItem mLiterature = new LiteratureItem(null, null, null, null,
				null, null, null, null, null, null, null, null, null, null);
		PmmXmlDoc mLiteratureCell = new PmmXmlDoc(mLiterature);

		LiteratureItem emLiterature = new LiteratureItem(null, null, null,
				null, null, null, null, null, null, null, null, null, null,
				null);
		PmmXmlDoc emLiteratureCell = new PmmXmlDoc(emLiterature);

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

	private List<KnimeTuple> parseTertiaryModel(SBMLDocument doc,
			CompSBMLDocumentPlugin compDocPlugin) {

		ListOf<Compartment> listOfCompartments = doc.getModel()
				.getListOfCompartments();
		ListOf<Species> listOfSpecies = doc.getModel().getListOfSpecies();
		ListOf<Parameter> listOfParameters = doc.getModel()
				.getListOfParameters();
		ListOf<Rule> listOfRules = doc.getModel().getListOfRules();
		AssignmentRule assignmentRule = (AssignmentRule) listOfRules.get(0);
		ListOf<ModelDefinition> modelDefinitions = compDocPlugin
				.getListOfModelDefinitions();

		// create n rows for n secondary models
		List<KnimeTuple> rows = new ArrayList<>();

		for (ModelDefinition secModel : modelDefinitions) {
			ListOf<Parameter> secParams = secModel.getListOfParameters();
			ListOf<Rule> secRules = secModel.getListOfRules();
			AssignmentRule secAssignmentRule = (AssignmentRule) secRules.get(0);

			// time series columns (1-9)
			String condID = "-1";

			String combaseID = "?";

			List<AgentXml> organisms = parseSpecies(listOfSpecies);
			PmmXmlDoc organismCell = new PmmXmlDoc();
			for (AgentXml agent : organisms) {
				organismCell.add(agent);
			}

			List<MatrixXml> matrices = parseCompartments(listOfCompartments);
			PmmXmlDoc matrixCell = new PmmXmlDoc();
			for (MatrixXml matrix : matrices) {
				matrixCell.add(matrix);
			}

			TimeSeriesXml mdData = new TimeSeriesXml(null, null, null, null,
					null, null, null, null, null, null);
			PmmXmlDoc mdDataCell = new PmmXmlDoc(mdData);

			MiscXml misc = new MiscXml(null, null, null, null, null, null);
			PmmXmlDoc miscCell = new PmmXmlDoc(misc);

			MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);
			PmmXmlDoc mdInfoCell = new PmmXmlDoc(mdInfo);

			LiteratureItem mdLiterature = new LiteratureItem(null, null, null,
					null, null, null, null, null, null, null, null, null, null,
					null);
			PmmXmlDoc mdLiteratureCell = new PmmXmlDoc(mdLiterature);

			String mdDBUID = "?";

			// primary model columns (10-18)
			String formula = parseFormula(assignmentRule);
			CatalogModelXml catModel = new CatalogModelXml(1, "", formula, 1,
					"");
			PmmXmlDoc catModelCell = new PmmXmlDoc(catModel);

			DepXml dep = new DepXml("Value", "Value",
					"Number Content (count/mass)",
					"Number content (count/mass)", "response");
			PmmXmlDoc depCell = new PmmXmlDoc(dep);

			IndepXml indep = new IndepXml("Time", "Time", null, null, "Time",
					"h", "elapsed time");
			PmmXmlDoc indepCell = new PmmXmlDoc(indep);

			List<ParamXml> paramXmls = parseParams(listOfParameters);
			PmmXmlDoc paramCell = new PmmXmlDoc();
			for (ParamXml param : paramXmls) {
				paramCell.add(param);
			}

			EstModelXml estModel = new EstModelXml(-1942671369, null, null,
					null, null, null, null, null, null, 0);
			PmmXmlDoc estModelCell = new PmmXmlDoc(estModel);

			LiteratureItem mLiterature = new LiteratureItem(null, null, null,
					null, null, null, null, null, null, null, null, null, null,
					null);
			PmmXmlDoc mLiteratureCell = new PmmXmlDoc(mLiterature);

			LiteratureItem emLiterature = new LiteratureItem(null, null, null,
					null, null, null, null, null, null, null, null, null, null,
					null);
			PmmXmlDoc emLiteratureCell = new PmmXmlDoc(emLiterature);

			int databaseWritable = Model1Schema.NOTWRITABLE;
			String mDBUID = "?";

			// secondary model columns (19-27)
			String secFormula = parseFormula(secAssignmentRule);
			CatalogModelXml catModelSec = new CatalogModelXml(1, "",
					secFormula, 1, "");
			PmmXmlDoc catModelSecCell = new PmmXmlDoc(catModelSec);

			DepXml dependentSec = new DepXml("Value", "Value",
					"Number Content (count/mass)",
					"Number content (count/mass)", "response");
			PmmXmlDoc dependentSecCell = new PmmXmlDoc(dependentSec);

			IndepXml independentSec = new IndepXml("Time", "Time", null, null,
					"Time", "h", "elapsed time");
			PmmXmlDoc independentSecCell = new PmmXmlDoc(independentSec);

			List<ParamXml> parameterSec = parseParams(secParams);
			PmmXmlDoc parameterSecCell = new PmmXmlDoc();
			for (ParamXml param : parameterSec) {
				paramCell.add(param);
			}

			EstModelXml estModelSec = new EstModelXml(-1942671369, null, null,
					null, null, null, null, null, null, 0);
			PmmXmlDoc estModelSecCell = new PmmXmlDoc(estModelSec);

			LiteratureItem mLiteratureSec = new LiteratureItem(null, null,
					null, null, null, null, null, null, null, null, null, null,
					null, null);
			PmmXmlDoc mLiteratureSecCell = new PmmXmlDoc(mLiteratureSec);

			LiteratureItem emLiteratureSec = new LiteratureItem(null, null,
					null, null, null, null, null, null, null, null, null, null,
					null, null);
			PmmXmlDoc emLiteratureSecCell = new PmmXmlDoc(emLiteratureSec);

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

	// helper functions
	private List<MatrixXml> parseCompartments(
			ListOf<Compartment> listOfCompartments) {
		List<MatrixXml> matrices = new ArrayList<MatrixXml>();
		for (Compartment compartment : listOfCompartments) {
			String matrixName = compartment.getName();
			MatrixXml xmlMatrix = new MatrixXml();
			xmlMatrix.setName(matrixName);
			matrices.add(xmlMatrix);
		}
		return matrices;
	}

	private List<AgentXml> parseSpecies(ListOf<Species> listOfSpecies) {
		List<AgentXml> agentXmls = new ArrayList<AgentXml>();
		for (Species species : listOfSpecies) {
			String speciesName = species.getName();
			AgentXml agentXml = new AgentXml();
			agentXml.setName(speciesName);
			agentXmls.add(agentXml);
		}
		return agentXmls;
	}

	private List<ParamXml> parseParams(ListOf<Parameter> listOfParameters) {
		List<ParamXml> paramXmls = new ArrayList<ParamXml>();
		for (Parameter param : listOfParameters) {
			String name = param.getId();
			Double value = param.getValue();
			String unit = param.getUnits();

			// SBMLWriter is adding a Value and time parameters on its own, thus
			// conflicting
			// with the Value parameter from the SBML file
			// TODO: Fix SBMLWriter and remove this if
			if (!name.equals("Value") && !name.equals("time")) {
				ParamXml paramXml = new ParamXml(name, value);
				paramXml.setUnit(unit);
				paramXmls.add(paramXml);
			}
		}
		return paramXmls;
	}

	private String parseFormula(AssignmentRule assignmentRule) {
		String leftHand = assignmentRule.getVariable();
		String rightHand = assignmentRule.getFormula();
		String formula = leftHand + "=" + rightHand;
		return formula;
	}

}