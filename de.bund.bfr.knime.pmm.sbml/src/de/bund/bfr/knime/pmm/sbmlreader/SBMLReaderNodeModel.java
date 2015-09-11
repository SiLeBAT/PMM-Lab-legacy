package de.bund.bfr.knime.pmm.sbmlreader;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.annotation.Model1Annotation;
import de.bund.bfr.knime.pmm.annotation.Model2Annotation;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.sbmlutil.Agent;
import de.bund.bfr.knime.pmm.sbmlutil.Coefficient;
import de.bund.bfr.knime.pmm.sbmlutil.Limits;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;
import de.bund.bfr.knime.pmm.sbmlutil.Model1Rule;
import de.bund.bfr.knime.pmm.sbmlutil.Model2Rule;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;
import de.bund.bfr.knime.pmm.sbmlutil.ReaderUtils;
import de.bund.bfr.knime.pmm.sbmlutil.SecDep;
import de.bund.bfr.knime.pmm.sbmlutil.SecIndep;
import de.bund.bfr.knime.pmm.sbmlutil.Uncertainties;

public class SBMLReaderNodeModel extends NodeModel {

	// configuration keys
	public static final String CFGKEY_FILE = "filename";

	// defaults for persistent state
	private static final String DEFAULT_FILE = "c:/temp/foo.sbml";

	// persistent state
	private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE, DEFAULT_FILE);

	/**
	 * Constructor for the node model
	 */
	protected SBMLReaderNodeModel() {
		// 0 input ports and 1 input port
		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {

		// Gets model type from the document annotation
		SBMLDocument doc = new SBMLReader().readSBML(filename.getStringValue());

		XMLNode docMetadata = doc.getAnnotation().getNonRDFannotation().getChildElement("metadata", "");
		XMLNode typeNode = docMetadata.getChildElement("type", "");
		String modelType = typeNode.getChild(0).getCharacters();

		BufferedDataContainer container;

		if (modelType.equals(ModelType.PRIMARY_MODEL_WDATA.toString())) {
			// Creates data spec and container
			DataTableSpec spec = SchemaFactory.createM1DataSchema().createSpec();
			container = exec.createDataContainer(spec);

			KnimeTuple tuple = readPrimaryModelWData(doc);

			// Adds tuple
			container.addRowToTable(tuple);

			// Shows warning
			setWarningMessage("This primary model includes links to external data files that cannot be reached");

			// Updates the progress bar
			exec.setProgress(1.0);

			// Closes the container
			container.close();
		}

		else if (modelType.equals(ModelType.PRIMARY_MODEL_WODATA.toString())) {
			// Creates data spec and container
			DataTableSpec spec = SchemaFactory.createM1DataSchema().createSpec();
			container = exec.createDataContainer(spec);

			KnimeTuple tuple = readPrimaryModelWOData(doc);

			// Adds tuple
			container.addRowToTable(tuple);

			// Updates the progress bar
			exec.setProgress(1.0);

			// Closes the container
			container.close();
		}

		else if (modelType.equals(ModelType.TWO_STEP_SECONDARY_MODEL.toString())) {
			// Creates data spec and container
			DataTableSpec spec = SchemaFactory.createM12DataSchema().createSpec();
			container = exec.createDataContainer(spec);

			KnimeTuple tuple = readTwoStepSecondaryModel(doc);

			// Adds tuple
			container.addRowToTable(tuple);

			// Shows warning
			setWarningMessage(
					"This secondary model includes links to external primary model and data files that cannot be reached");

			// Updates the progress bar
			exec.setProgress(1.0);

			// Closes the container
			container.close();
		}

		else if (modelType.equals(ModelType.ONE_STEP_SECONDARY_MODEL.toString())) {
			// Creates data spec and container
			DataTableSpec spec = SchemaFactory.createM12DataSchema().createSpec();
			container = exec.createDataContainer(spec);

			KnimeTuple tuple = readOneStepSecondaryModel(doc);

			// Adds tuple
			container.addRowToTable(tuple);

			// Updates the progress bar
			exec.setProgress(1.0);

			// Closes the container
			container.close();
		}

		else if (modelType.equals(ModelType.MANUAL_SECONDARY_MODEL.toString())) {
			// Creates data spec and container
			DataTableSpec spec = SchemaFactory.createM2Schema().createSpec();
			container = exec.createDataContainer(spec);

			KnimeTuple tuple = readManualSecondaryModel(doc);

			// Adds tuple
			container.addRowToTable(tuple);

			// Updates the progress bar
			exec.setProgress(1.0);

			// Closes the container
			container.close();
		}

		else {
			throw new Exception();
		}
		// TODO: tertiary model cases

		BufferedDataTable[] table = { container.getTable() };
		return table;
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
	protected void reset() {
	}

	/**
	 * {@inheritDoc}
	 */
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	private KnimeTuple readPrimaryModelWData(SBMLDocument doc) {

		Model model = doc.getModel();

		// Parse model annotations
		Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());

		Model1Rule rule = new Model1Rule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = rule.toCatModel();

		// Parse constraints
		Map<String, Limits> limits = ReaderUtils.parseConstraints(model.getListOfConstraints());

		// time series cells
		int condID = m1Annot.getCondID();
		Agent agent = new Agent(model.getSpecies(0));
		Matrix matrix = new Matrix(model.getCompartment(0));

		// Parse model variables
		PmmXmlDoc miscCell = ReaderUtils.parseMiscs(matrix.getMiscs());

		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);

		// primary model cells

		// Parse dep parameter (primary models only have one dependent var)
		DepXml depXml = new DepXml("Value");
		String depUnitID = agent.getSpecies().getUnits();
		if (depUnitID != null) {
			String depUnitName = model.getUnitDefinition(depUnitID).getName();
			depXml.setUnit(depUnitName);
			depXml.setCategory(DBUnits.getDBUnits().get(depUnitName).getKind_of_property_quantity());
		}
		depXml.setDescription(agent.getDescription());

		// Parse indep
		Parameter indepParam = model.getParameter(Categories.getTime());
		IndepXml indepXml = new IndepXml(indepParam.getId(), null, null);
		String indepUnitID = indepParam.getUnits();
		if (!indepUnitID.equalsIgnoreCase(Unit.Kind.DIMENSIONLESS.getName())) {
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

		// Parse Consts
		LinkedList<Parameter> constParams = new LinkedList<>();
		for (Parameter param : model.getListOfParameters()) {
			if (param.isConstant()) {
				constParams.add(param);
			}
		}

		PmmXmlDoc paramCell = new PmmXmlDoc();
		for (Parameter constParam : constParams) {
			paramCell.add(new Coefficient(constParam).toParamXml(model.getListOfUnitDefinitions(), limits));
		}

		Uncertainties uncertainties = m1Annot.getUncertainties();
		EstModelXml estModel = uncertainties.getEstModelXml();
		if (model.isSetName()) {
			estModel.setName(model.getName());
		}

		PmmXmlDoc mLitCell = new PmmXmlDoc();
		for (LiteratureItem lit : rule.getLits()) {
			mLitCell.add(lit);
		}

		PmmXmlDoc emLitCell = new PmmXmlDoc();
		for (LiteratureItem lit : m1Annot.getLits()) {
			emLitCell.add(lit);
		}

		// Add cells to the row
		KnimeTuple row = new KnimeTuple(SchemaFactory.createM1DataSchema());

		// time series cells
		row.setValue(TimeSeriesSchema.ATT_CONDID, condID);
		row.setValue(TimeSeriesSchema.ATT_COMBASEID, "?");
		row.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(agent.toAgentXml()));
		row.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(matrix.toMatrixXml()));
		row.setValue(TimeSeriesSchema.ATT_TIMESERIES, new PmmXmlDoc());
		row.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
		row.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
		row.setValue(TimeSeriesSchema.ATT_LITMD, new PmmXmlDoc());
		row.setValue(TimeSeriesSchema.ATT_DBUUID, "?");

		// primary model cells
		row.setValue(Model1Schema.ATT_MODELCATALOG, new PmmXmlDoc(catModel));
		row.setValue(Model1Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));
		row.setValue(Model1Schema.ATT_INDEPENDENT, new PmmXmlDoc(indepXml));
		row.setValue(Model1Schema.ATT_PARAMETER, paramCell);
		row.setValue(Model1Schema.ATT_ESTMODEL, new PmmXmlDoc(estModel));
		row.setValue(Model1Schema.ATT_MLIT, mLitCell);
		row.setValue(Model1Schema.ATT_EMLIT, emLitCell);
		row.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
		row.setValue(Model1Schema.ATT_DBUUID, "?");

		return row;
	}

	private KnimeTuple readPrimaryModelWOData(SBMLDocument doc) {

		Model model = doc.getModel();

		// Parse model annotations
		Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());

		Model1Rule rule = new Model1Rule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = rule.toCatModel();

		// Parse constraints
		Map<String, Limits> limits = ReaderUtils.parseConstraints(model.getListOfConstraints());

		// time series cells
		final int condID = m1Annot.getCondID();
		Agent agent = new Agent(model.getSpecies(0));
		Matrix matrix = new Matrix(model.getCompartment(0));

		// Parse model variables: Temperature, pH and water activity
		PmmXmlDoc miscCell = ReaderUtils.parseMiscs(matrix.getMiscs());

		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);

		// primary model cells
		// Parse dependent parameter (primary models only have one dependent
		// variable)
		DepXml depXml = new DepXml("Value");
		String depUnitID = agent.getSpecies().getUnits();
		if (depUnitID != null) {
			String depUnitName = model.getUnitDefinition(depUnitID).getName();
			depXml.setUnit(depUnitName);
			depXml.setCategory(DBUnits.getDBUnits().get(depUnitName).getKind_of_property_quantity());
		}
		depXml.setDescription(agent.getDescription());

		// Parse indep
		Parameter indepParam = model.getParameter(Categories.getTime());
		IndepXml indepXml = new IndepXml(indepParam.getId(), null, null);
		String indepUnitID = indepParam.getUnits();
		if (!indepUnitID.equalsIgnoreCase(Unit.Kind.DIMENSIONLESS.getName())) {
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

		// Parse Consts
		LinkedList<Parameter> constParams = new LinkedList<>();
		for (Parameter param : model.getListOfParameters()) {
			if (param.isConstant()) {
				constParams.add(param);
			}
		}

		PmmXmlDoc paramCell = new PmmXmlDoc();
		for (Parameter constParam : constParams) {
			paramCell.add(new Coefficient(constParam).toParamXml(model.getListOfUnitDefinitions(), limits));
		}

		Uncertainties uncertainties = m1Annot.getUncertainties();
		EstModelXml estModel = uncertainties.getEstModelXml();

		if (model.isSetName()) {
			estModel.setName(model.getName());
		}

		PmmXmlDoc mLitCell = new PmmXmlDoc();
		for (LiteratureItem lit : rule.getLits()) {
			mLitCell.add(lit);
		}

		PmmXmlDoc emLitCell = new PmmXmlDoc();
		for (LiteratureItem lit : m1Annot.getLits()) {
			emLitCell.add(lit);
		}

		// Add cells to the row
		KnimeTuple row = new KnimeTuple(SchemaFactory.createM1DataSchema());

		// time series cells
		row.setValue(TimeSeriesSchema.ATT_CONDID, condID);
		row.setValue(TimeSeriesSchema.ATT_COMBASEID, "?");
		row.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(agent.toAgentXml()));
		row.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(matrix.toMatrixXml()));
		row.setValue(TimeSeriesSchema.ATT_TIMESERIES, new PmmXmlDoc());
		row.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
		row.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
		row.setValue(TimeSeriesSchema.ATT_LITMD, new PmmXmlDoc());
		row.setValue(TimeSeriesSchema.ATT_DBUUID, "?");

		// primary model cells
		row.setValue(Model1Schema.ATT_MODELCATALOG, new PmmXmlDoc(catModel));
		row.setValue(Model1Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));
		row.setValue(Model1Schema.ATT_INDEPENDENT, new PmmXmlDoc(indepXml));
		row.setValue(Model1Schema.ATT_PARAMETER, paramCell);
		row.setValue(Model1Schema.ATT_ESTMODEL, new PmmXmlDoc(estModel));
		row.setValue(Model1Schema.ATT_MLIT, mLitCell);
		row.setValue(Model1Schema.ATT_EMLIT, emLitCell);
		row.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
		row.setValue(Model1Schema.ATT_DBUUID, "?");

		return row;
	}

	private KnimeTuple readManualSecondaryModel(SBMLDocument doc) {

		Model model = doc.getModel();

		// Parse constraints
		Map<String, Limits> limits = ReaderUtils.parseConstraints(model.getListOfConstraints());

		// Parse rule
		Model2Rule rule = new Model2Rule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = rule.toCatModel();

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

		// Sort const and indep params
		LinkedList<Parameter> indepParams = new LinkedList<>();
		LinkedList<Parameter> constParams = new LinkedList<>();
		for (Parameter param : model.getListOfParameters()) {
			if (param.isConstant()) {
				constParams.add(param);
			} else if (!param.getId().equals(depName)) {
				indepParams.add(param);
			}
		}

		// Parse indeps
		PmmXmlDoc indepCell = new PmmXmlDoc();
		for (Parameter param : indepParams) {
			indepCell.add(new SecIndep(param).toIndepXml(model.getListOfUnitDefinitions(), limits));
		}

		// Parse consts
		PmmXmlDoc constCell = new PmmXmlDoc();
		for (Parameter param : constParams) {
			constCell.add(new Coefficient(param).toParamXml(model.getListOfUnitDefinitions(), limits));
		}

		// Get model annotation
		Model2Annotation m2Annot = new Model2Annotation(model.getAnnotation());

		// EstModel
		Uncertainties uncertainties = m2Annot.getUncertainties();
		EstModelXml estModelXml = uncertainties.getEstModelXml();
		if (model.isSetName()) {
			estModelXml.setName(model.getName());
		}

		// Get globalModelID from annotation
		int globalModelID = m2Annot.getGlobalModelID();

		PmmXmlDoc mLitCell = new PmmXmlDoc();
		for (LiteratureItem lit : rule.getLits()) {
			mLitCell.add(lit);
		}

		// Get EM_Literature (references) from annotation
		PmmXmlDoc emLitCell = new PmmXmlDoc();
		for (LiteratureItem lit : m2Annot.getLiteratureItems()) {
			emLitCell.add(lit);
		}

		// Add cells to the row
		KnimeTuple row = new KnimeTuple(SchemaFactory.createM2Schema());
		row.setValue(Model2Schema.ATT_MODELCATALOG, new PmmXmlDoc(catModel));
		row.setValue(Model2Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));
		row.setValue(Model2Schema.ATT_INDEPENDENT, indepCell);
		row.setValue(Model2Schema.ATT_PARAMETER, constCell);
		row.setValue(Model2Schema.ATT_ESTMODEL, new PmmXmlDoc(estModelXml));
		row.setValue(Model2Schema.ATT_MLIT, mLitCell);
		row.setValue(Model2Schema.ATT_EMLIT, emLitCell);
		row.setValue(Model2Schema.ATT_DATABASEWRITABLE, Model2Schema.WRITABLE);
		row.setValue(Model2Schema.ATT_DBUUID, "?");
		row.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, globalModelID);
		return row;
	}

	private KnimeTuple readTwoStepSecondaryModel(SBMLDocument doc) {

		Model model = doc.getModel();

		// Parse constraints
		Map<String, Limits> limits = ReaderUtils.parseConstraints(model.getListOfConstraints());

		// Parse rule
		Model2Rule rule = new Model2Rule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = rule.toCatModel();

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

		// Sort const and indep params
		LinkedList<Parameter> indepParams = new LinkedList<>();
		LinkedList<Parameter> constParams = new LinkedList<>();
		for (Parameter param : model.getListOfParameters()) {
			if (param.isConstant()) {
				constParams.add(param);
			} else if (!param.getId().equals(depName)) {
				indepParams.add(param);
			}
		}

		// Parse indeps
		PmmXmlDoc indepCell = new PmmXmlDoc();
		for (Parameter param : indepParams) {
			indepCell.add(new SecIndep(param).toIndepXml(model.getListOfUnitDefinitions(), limits));
		}

		// Parse consts
		PmmXmlDoc constCell = new PmmXmlDoc();
		for (Parameter param : constParams) {
			constCell.add(new Coefficient(param).toParamXml(model.getListOfUnitDefinitions(), limits));
		}

		// Get model annotation
		Model2Annotation m2Annot = new Model2Annotation(model.getAnnotation());

		// EstModel
		Uncertainties uncertainties = m2Annot.getUncertainties();
		EstModelXml estModel = uncertainties.getEstModelXml();
		if (model.isSetName()) {
			estModel.setName(model.getName());
		}

		// Get globalModelID from annotation
		int globalModelID = m2Annot.getGlobalModelID();

		PmmXmlDoc secMLitCell = new PmmXmlDoc();
		for (LiteratureItem lit : rule.getLits()) {
			secMLitCell.add(lit);
		}

		// Get EM_Literature (references) from annotation
		PmmXmlDoc secEmLitCell = new PmmXmlDoc();
		for (LiteratureItem lit : m2Annot.getLiteratureItems()) {
			secEmLitCell.add(lit);
		}

		KnimeTuple row = new KnimeTuple(SchemaFactory.createM12DataSchema());

		// TimeSeriesSchema cells
		row.setValue(TimeSeriesSchema.ATT_CONDID, MathUtilities.getRandomNegativeInt());
		row.setValue(TimeSeriesSchema.ATT_COMBASEID, "?");
		row.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc());
		row.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc());
		row.setValue(TimeSeriesSchema.ATT_TIMESERIES, new PmmXmlDoc());
		row.setValue(TimeSeriesSchema.ATT_MISC, new PmmXmlDoc());
		row.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc());
		row.setValue(TimeSeriesSchema.ATT_LITMD, new PmmXmlDoc());
		row.setValue(TimeSeriesSchema.ATT_DBUUID, "?");

		// Model1Schema cells
		row.setValue(Model1Schema.ATT_MODELCATALOG, new PmmXmlDoc());
		row.setValue(Model1Schema.ATT_DEPENDENT, new PmmXmlDoc());
		row.setValue(Model1Schema.ATT_INDEPENDENT, new PmmXmlDoc());
		row.setValue(Model1Schema.ATT_PARAMETER, new PmmXmlDoc());
		row.setValue(Model1Schema.ATT_ESTMODEL, new PmmXmlDoc());
		row.setValue(Model1Schema.ATT_MLIT, new PmmXmlDoc());
		row.setValue(Model1Schema.ATT_EMLIT, new PmmXmlDoc());
		row.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
		row.setValue(Model1Schema.ATT_DBUUID, "?");

		// Model2Schema cells
		row.setValue(Model2Schema.ATT_MODELCATALOG, new PmmXmlDoc(catModel));
		row.setValue(Model2Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));
		row.setValue(Model2Schema.ATT_INDEPENDENT, indepCell);
		row.setValue(Model2Schema.ATT_PARAMETER, constCell);
		row.setValue(Model2Schema.ATT_ESTMODEL, new PmmXmlDoc(estModel));
		row.setValue(Model2Schema.ATT_MLIT, secMLitCell);
		row.setValue(Model2Schema.ATT_EMLIT, secEmLitCell);
		row.setValue(Model2Schema.ATT_DATABASEWRITABLE, Model2Schema.WRITABLE);
		row.setValue(Model2Schema.ATT_DBUUID, "?");
		row.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, globalModelID);

		return row;
	}

	private KnimeTuple readOneStepSecondaryModel(SBMLDocument doc) {

		Model model = doc.getModel();
		// Parses annotation
		Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());

		Model1Rule rule = new Model1Rule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = rule.toCatModel();

		// Parse constraints
		ListOf<Constraint> constraints = model.getListOfConstraints();
		Map<String, Limits> limits = ReaderUtils.parseConstraints(constraints);

		Agent agent = new Agent(model.getSpecies(0));

		// Parse dependent parameter (primary models only have one dependent
		// variable)
		DepXml depXml = new DepXml("Value");
		String depUnitID = agent.getSpecies().getUnits();
		if (depUnitID != null) {
			String depUnitName = model.getUnitDefinition(depUnitID).getName();
			depXml.setUnit(depUnitName);
			depXml.setCategory(DBUnits.getDBUnits().get(depUnitName).getKind_of_property_quantity());
		}
		depXml.setDescription(agent.getDescription());
		// Gets limits
		if (limits.containsKey(agent.getSpecies().getId())) {
			Limits depLimits = limits.get(agent.getSpecies().getId());
			depXml.setMax(depLimits.getMax());
			depXml.setMin(depLimits.getMin());
		}

		// Parse indep
		Parameter indepParam = model.getParameter(Categories.getTime());
		IndepXml indepXml = new IndepXml(indepParam.getId(), null, null);
		String indepUnitID = indepParam.getUnits();
		if (!indepUnitID.equalsIgnoreCase(Unit.Kind.DIMENSIONLESS.getName())) {
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

		// Parse Consts
		List<Parameter> constParams = new LinkedList<>();
		for (Parameter param : model.getListOfParameters()) {
			if (param.isConstant()) {
				constParams.add(param);
			}
		}

		PmmXmlDoc paramCell = new PmmXmlDoc();
		for (Parameter constParam : constParams) {
			paramCell.add(new Coefficient(constParam).toParamXml(model.getListOfUnitDefinitions(), limits));
		}

		Uncertainties uncertainties = m1Annot.getUncertainties();
		EstModelXml estModel = uncertainties.getEstModelXml();
		if (model.isSetName()) {
			estModel.setName(model.getName());
		}

		// Gets model literature
		PmmXmlDoc mLitCell = new PmmXmlDoc();
		for (LiteratureItem lit : rule.getLits()) {
			mLitCell.add(lit);
		}

		// Gets estimated model literature
		PmmXmlDoc emLitCell = new PmmXmlDoc();
		for (LiteratureItem lit : m1Annot.getLits()) {
			emLitCell.add(lit);
		}

		KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM12DataSchema());
		tuple.setValue(Model1Schema.ATT_MODELCATALOG, new PmmXmlDoc(catModel));
		tuple.setValue(Model1Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));
		tuple.setValue(Model1Schema.ATT_INDEPENDENT, new PmmXmlDoc(indepXml));
		tuple.setValue(Model1Schema.ATT_PARAMETER, paramCell);
		tuple.setValue(Model1Schema.ATT_ESTMODEL, new PmmXmlDoc(estModel));
		tuple.setValue(Model1Schema.ATT_MLIT, mLitCell);
		tuple.setValue(Model1Schema.ATT_EMLIT, emLitCell);
		tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
		tuple.setValue(Model1Schema.ATT_DBUUID, "?");

		// Parses secondary model
		CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) doc.getPlugin(CompConstants.shortLabel);
		ModelDefinition secModel = secCompPlugin.getModelDefinition(0);
		
		// Parse constraints
		Map<String, Limits> secLimits = ReaderUtils.parseConstraints(secModel.getListOfConstraints());

		// Parse rule
		Model2Rule rule2 = new Model2Rule((AssignmentRule) secModel.getRule(0));
		CatalogModelXml secCatModel = rule.toCatModel();

		// Parse dep
		String secDepName = rule2.getRule().getVariable();
		SecDep secDep = new SecDep(secModel.getParameter(secDepName));
		DepXml secDepXml = secDep.toDepXml(secModel.getListOfUnitDefinitions(), secLimits);

		// Sort consts and indep params
		List<Parameter> secIndeps = new LinkedList<>();
		List<Parameter> secConsts = new LinkedList<>();
		for (Parameter param : secModel.getListOfParameters()) {
			if (param.isConstant()) {
				secConsts.add(param);
			} else if (!param.getId().equals(secDepName)) {
				secIndeps.add(param);
			}
		}

		// Parse indeps
		PmmXmlDoc secIndepCell = new PmmXmlDoc();
		for (Parameter param : secIndeps) {
			secIndepCell.add(new SecIndep(param).toIndepXml(secModel.getListOfUnitDefinitions(), limits));
		}

		// Parse consts
		PmmXmlDoc secConstCell = new PmmXmlDoc();
		for (Parameter param : secConsts) {
			secConstCell.add(new Coefficient(param).toParamXml(secModel.getListOfUnitDefinitions(), limits));
		}

		// Get model annotation
		Model2Annotation m2Annot = new Model2Annotation(secModel.getAnnotation());

		// EstModel
		Uncertainties secUncertainties = m2Annot.getUncertainties();
		EstModelXml secEstModel = secUncertainties.getEstModelXml();
		if (secModel.isSetName()) {
			secEstModel.setName(secModel.getName());
		}

		// Get globalModelID from annotation
		int globalModelID = m2Annot.getGlobalModelID();

		// Gets model literature
		PmmXmlDoc secMLitCell = new PmmXmlDoc();
		for (LiteratureItem lit : rule2.getLits()) {
			secMLitCell.add(lit);
		}

		// Get EM_Literature (references) from annotation
		PmmXmlDoc secEmLitCell = new PmmXmlDoc();
		for (LiteratureItem lit : m2Annot.getLiteratureItems()) {
			secEmLitCell.add(lit);
		}

		tuple.setValue(Model2Schema.ATT_MODELCATALOG, new PmmXmlDoc(secCatModel));
		tuple.setValue(Model2Schema.ATT_DEPENDENT, new PmmXmlDoc(secDepXml));
		tuple.setValue(Model2Schema.ATT_INDEPENDENT, secIndepCell);
		tuple.setValue(Model2Schema.ATT_PARAMETER, secConstCell);
		tuple.setValue(Model2Schema.ATT_ESTMODEL, new PmmXmlDoc(secEstModel));
		tuple.setValue(Model2Schema.ATT_MLIT, secMLitCell);
		tuple.setValue(Model2Schema.ATT_EMLIT, secEmLitCell);
		tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, Model2Schema.WRITABLE);
		tuple.setValue(Model2Schema.ATT_DBUUID, "?");
		tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, globalModelID);

		return tuple;
	}
}
