/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.sbmlreader;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
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
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.xml.XMLNode;

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
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.extendedtable.Model1Metadata;
import de.bund.bfr.knime.pmm.extendedtable.Model2Metadata;
import de.bund.bfr.knime.pmm.extendedtable.TimeSeriesMetadata;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.items.EMLiteratureItem;
import de.bund.bfr.knime.pmm.extendedtable.items.MDAgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.MDMatrixXml;
import de.bund.bfr.knime.pmm.extendedtable.items.MLiteratureItem;
import de.bund.bfr.knime.pmm.extendedtable.items.Model1AgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.Model1MatrixXml;
import de.bund.bfr.knime.pmm.extendedtable.items.Model2AgentXml;
import de.bund.bfr.knime.pmm.extendedtable.items.Model2MatrixXml;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.sbml.Correlation;
import de.bund.bfr.pmf.sbml.Limits;
import de.bund.bfr.pmf.sbml.LimitsConstraint;
import de.bund.bfr.pmf.sbml.Model1Annotation;
import de.bund.bfr.pmf.sbml.Model2Annotation;
import de.bund.bfr.pmf.sbml.ModelRule;
import de.bund.bfr.pmf.sbml.ModelVariable;
import de.bund.bfr.pmf.sbml.PMFCoefficient;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.bund.bfr.pmf.sbml.Reference;
import de.bund.bfr.pmf.sbml.SBMLFactory;
import de.bund.bfr.pmf.sbml.SecDep;
import de.bund.bfr.pmf.sbml.SecIndep;
import de.bund.bfr.pmf.sbml.Uncertainties;

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
		String modelTypeString = typeNode.getChild(0).getCharacters();
		ModelType modelType = ModelType.valueOf(modelTypeString);

		BufferedDataContainer container;

		switch (modelType) {
		case EXPERIMENTAL_DATA:
			setWarningMessage("SBML Reader does not support NuML. NuML Reader does.");
			throw new Exception();
		case PRIMARY_MODEL_WDATA:
		case PRIMARY_MODEL_WODATA:
			container = new PrimaryModelHandler().processDocument(exec, doc);
			break;
		case TWO_STEP_SECONDARY_MODEL:
			container = new TwoStepSecondaryModelHandler().processDocument(exec, doc);
			break;
		case ONE_STEP_SECONDARY_MODEL:
			container = new OneStepSecondaryModelHandler().processDocument(exec, doc);
			break;
		case MANUAL_SECONDARY_MODEL:
			container = new ManualSecondaryModelHandler().processDocument(exec, doc);
			break;
		case TWO_STEP_TERTIARY_MODEL:
		case ONE_STEP_TERTIARY_MODEL:
		case MANUAL_TERTIARY_MODEL:
			setWarningMessage("Tertiary models not supported currently");
		default:
			throw new Exception();
		}

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
}

abstract class DocumentHandler {

	public BufferedDataContainer processDocument(final ExecutionContext exec, final SBMLDocument doc) {
		
		// Creates table spec
		final DataTableSpec spec = createSpec();
		
		// Creates container
		final BufferedDataContainer container = exec.createDataContainer(spec);
		
		// Creates tuple from 'doc'
		KnimeTuple tuple = readModel(doc);
		
		// Adds tuple
		container.addRowToTable(tuple);
		
		// Updates the progress bar
		exec.setProgress(1.0);
		
		// closes the container
		container.close();
		
		return container;
	}
	
	protected abstract DataTableSpec createSpec();
	
	protected abstract KnimeTuple readModel(SBMLDocument doc);
}

class PrimaryModelHandler extends DocumentHandler {

	protected DataTableSpec createSpec() {
		return SchemaFactory.createM1DataSchema().createSpec();
	}

	protected KnimeTuple readModel(SBMLDocument doc) {
		return Util.mergeTuples(new DataTuple(doc), new Model1Tuple(doc));
	}
}

class TwoStepSecondaryModelHandler extends DocumentHandler {
	
	protected DataTableSpec createSpec() {
		return SchemaFactory.createM2Schema().createSpec();
	}
	
	protected KnimeTuple readModel(SBMLDocument doc) {
		return new Model2Tuple(doc.getModel()).tuple;
	}
}

class OneStepSecondaryModelHandler extends DocumentHandler {
	
	protected DataTableSpec createSpec() {
		return SchemaFactory.createM12DataSchema().createSpec();
	}
	
	protected KnimeTuple readModel(SBMLDocument doc) {
		// Parses data columns
		final KnimeTuple dataTuple = new DataTuple(doc).tuple;

		// Parses primary model
		final KnimeTuple m1Tuple = new Model1Tuple(doc).tuple;

		// Parses secondary model
		CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) doc.getPlugin(CompConstants.shortLabel);
		ModelDefinition secModel = secCompPlugin.getModelDefinition(0);
		final KnimeTuple m2Tuple = new Model2Tuple(secModel).tuple;

		final KnimeTuple row = Util.mergeTuples(dataTuple, m1Tuple, m2Tuple);
		return row;
	}
}

class ManualSecondaryModelHandler extends DocumentHandler {
	
	protected DataTableSpec createSpec() {
		return SchemaFactory.createM2Schema().createSpec();
	}
	
	protected KnimeTuple readModel(SBMLDocument doc) {
		return new Model2Tuple(doc.getModel()).tuple;
	}
}

class TertiaryModelHandler extends DocumentHandler {
	
	protected DataTableSpec createSpec() {
		return SchemaFactory.createM1DataSchema().createSpec();
	}
	
	protected KnimeTuple readModel(SBMLDocument doc) {
		return Util.mergeTuples(new DataTuple(doc), new Model1Tuple(doc));
	}
}

class DataTuple {

	KnimeTuple tuple;
	private static final KnimeSchema SCHEMA = SchemaFactory.createDataSchema();

	public DataTuple(SBMLDocument doc) {
		Model model = doc.getModel();

		// Parses annotation
		Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());

		PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
		final int agentID = MathUtilities.getRandomNegativeInt();
		AgentXml originalAgentXml = new AgentXml(agentID, species.getName(), species.getDetail(), null);
		MDAgentXml agentXml = new MDAgentXml(agentID, species.getName(), species.getDetail(), null);

		PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
		final int matrixID = MathUtilities.getRandomNegativeInt();
		MatrixXml originalMatrixXml = new MatrixXml(matrixID, compartment.getName(), compartment.getDetail(), null);
		MDMatrixXml matrixXml = new MDMatrixXml(matrixID, compartment.getName(), compartment.getDetail(), null);

		TimeSeriesMetadata metadata = new TimeSeriesMetadata();
		metadata.setAgentXml(agentXml);
		metadata.setMatrixXml(matrixXml);

		PmmXmlDoc miscCell = new PmmXmlDoc();
		if (compartment.isSetModelVariables()) {
			Map<String, Double> miscs = new HashMap<>(compartment.getModelVariables().length);
			for (final ModelVariable modelVariable : compartment.getModelVariables()) {
				miscs.put(modelVariable.getName(), modelVariable.getValue());
			}
			miscCell = Util.parseMiscs(miscs);
		}

		MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);

		tuple = new KnimeTuple(SCHEMA);
		tuple.setValue(TimeSeriesSchema.ATT_CONDID, m1Annot.getCondID());
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, "?");
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(originalAgentXml));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(originalMatrixXml));
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, new PmmXmlDoc());
		tuple.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, new PmmXmlDoc());
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, "?");
		tuple.setValue(TimeSeriesSchema.ATT_METADATA, metadata);
	}
}

class Model1Tuple {

	KnimeTuple tuple;

	private static final KnimeSchema SCHEMA = SchemaFactory.createM1Schema();

	public Model1Tuple(SBMLDocument doc) {
		Model model = doc.getModel();

		// Parses annotation
		Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());

		ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = Util.model1Rule2CatModel(rule);

		// Parses constraints
		Map<String, Limits> limits = Util.parseConstraints(model.getListOfConstraints());

		PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));

		DepXml depXml = new DepXml("Value");
		String depUnitID = species.getUnits();
		if (depUnitID != null) {
			if (depUnitID.equals("dimensionless")) {
				depXml.setUnit("dimensionless");
				depXml.setCategory("Dimensionless quantity");
			} else {
				String depUnitName = model.getUnitDefinition(depUnitID).getName();
				depXml.setUnit(depUnitName);
				depXml.setCategory(DBUnits.getDBUnits().get(depUnitName).getKind_of_property_quantity());
			}
		}
		if (species.isSetDescription()) {
			depXml.setDescription(species.getDescription());
		}

		// Gets limits
		if (limits.containsKey(species.getId())) {
			Limits depLimits = limits.get(species.getId());
			depXml.setMin(depLimits.getMin());
			depXml.setMax(depLimits.getMax());
		}

		// Parses indeps
		Parameter indepParam = model.getParameter(Categories.getTime());
		IndepXml indepXml = new IndepXml(indepParam.getId(), null, null);
		String indepUnitID = indepParam.getUnits();
		if (!indepUnitID.isEmpty() && !indepUnitID.equalsIgnoreCase(Unit.Kind.DIMENSIONLESS.name())) {
			String unitName = model.getUnitDefinition(indepUnitID).getName();
			indepXml.setUnit(unitName);
			;
			indepXml.setCategory(Categories.getTimeCategory().getName());
			indepXml.setDescription(Categories.getTime());
		}

		// Gets limits
		if (limits.containsKey(indepParam.getId())) {
			Limits indepLimits = limits.get(indepParam.getId());
			indepXml.setMin(indepLimits.getMin());
			indepXml.setMax(indepLimits.getMax());
		}

		// Parse consts
		PmmXmlDoc paramCell = new PmmXmlDoc();
		for (Parameter param : model.getListOfParameters()) {
			if (!param.isConstant())
				continue;

			ParamXml paramXml = new ParamXml(param.getId(), param.getValue());

			String unitID = param.getUnits();
			if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
				String unitName = model.getUnitDefinition(unitID).getName();
				paramXml.setUnit(unitName);
				paramXml.setCategory(DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity());
			}

			PMFCoefficient coefficient = SBMLFactory.createPMFCoefficient(param);
			if (coefficient.isSetP()) {
				paramXml.setP(coefficient.getP());
			}
			if (coefficient.isSetError()) {
				paramXml.setError(coefficient.getError());
			}
			if (coefficient.isSetT()) {
				paramXml.setT(coefficient.getT());
			}
			if (coefficient.isSetDescription()) {
				paramXml.setDescription(coefficient.getDescription());
			}
			if (coefficient.isSetCorrelations()) {
				for (Correlation correlation : coefficient.getCorrelations()) {
					paramXml.addCorrelation(correlation.getName(), correlation.getValue());
				}
			}
			// Adds limits
			if (limits.containsKey(param.getId())) {
				Limits constLimits = limits.get(param.getId());
				paramXml.setMin(constLimits.getMin());
				paramXml.setMax(constLimits.getMax());
			}
			paramCell.add(paramXml);
		}

		EstModelXml estModel = Util.uncertainties2EstModel(m1Annot.getUncertainties());
		if (model.isSetName()) {
			estModel.setName(model.getName());
		}

		Model1AgentXml agentXml = new Model1AgentXml(MathUtilities.getRandomNegativeInt(), species.getName(),
				species.getDetail(), null);

		PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
		Model1MatrixXml matrixXml = new Model1MatrixXml(MathUtilities.getRandomNegativeInt(), compartment.getName(),
				compartment.getDetail(), null);

		Model1Metadata metadata = new Model1Metadata();
		metadata.setAgentXml(agentXml);
		metadata.setMatrixXml(matrixXml);

		// Reads model literature
		PmmXmlDoc mLit = new PmmXmlDoc();
		for (Reference ref : rule.getReferences()) {
			String author = ref.getAuthor();
			Integer year = ref.getYear();
			String title = ref.getTitle();
			String abstractText = ref.getAbstractText();
			String journal = ref.getJournal();
			String volume = ref.getVolume();
			String issue = ref.getIssue();
			Integer page = ref.getPage();
			Integer approvalMode = ref.getApprovalMode();
			String website = ref.getWebsite();
			Integer type = ref.isSetType() ? ref.getType().value() : null;
			String comment = ref.getComment();

			LiteratureItem literatureItem = new LiteratureItem(author, year, title, abstractText, journal, volume,
					issue, page, approvalMode, website, type, comment);
			mLit.add(literatureItem);

			MLiteratureItem mLiteratureItem = new MLiteratureItem(author, year, title, abstractText, journal, volume,
					issue, page, approvalMode, website, type, comment);
			metadata.addLiteratureItem(mLiteratureItem);
		}

		// Reads estimated model literature
		PmmXmlDoc emLit = new PmmXmlDoc();
		for (Reference ref : m1Annot.getReferences()) {
			String author = ref.getAuthor();
			Integer year = ref.getYear();
			String title = ref.getTitle();
			String abstractText = ref.getAbstractText();
			String journal = ref.getJournal();
			String volume = ref.getVolume();
			String issue = ref.getIssue();
			Integer page = ref.getPage();
			Integer approvalMode = ref.getApprovalMode();
			String website = ref.getWebsite();
			Integer type = ref.isSetType() ? ref.getType().value() : null;
			String comment = ref.getComment();

			EMLiteratureItem emLiteratureItem = new EMLiteratureItem(author, year, title, abstractText, journal, volume,
					issue, page, approvalMode, website, type, comment);
			metadata.addLiteratureItem(emLiteratureItem);

			LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page,
					approvalMode, website, type, comment);
			emLit.add(lit);
		}

		tuple = new KnimeTuple(SCHEMA);
		tuple.setValue(Model1Schema.ATT_MODELCATALOG, new PmmXmlDoc(catModel));
		tuple.setValue(Model1Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));
		tuple.setValue(Model1Schema.ATT_INDEPENDENT, new PmmXmlDoc(indepXml));
		tuple.setValue(Model1Schema.ATT_PARAMETER, paramCell);
		tuple.setValue(Model1Schema.ATT_ESTMODEL, new PmmXmlDoc(estModel));
		tuple.setValue(Model1Schema.ATT_MLIT, mLit);
		tuple.setValue(Model1Schema.ATT_EMLIT, emLit);
		tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
		tuple.setValue(Model1Schema.ATT_DBUUID, "?");
		tuple.setValue(Model1Schema.ATT_METADATA, metadata);
	}
}

class Model2Tuple {

	KnimeTuple tuple;
	private static final KnimeSchema SCHEMA = SchemaFactory.createM2Schema();

	public Model2Tuple(Model model) {
		Map<String, Limits> limits = Util.parseConstraints(model.getListOfConstraints());

		// Parses rule
		ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
		CatalogModelXml catModel = Util.model2Rule2CatModel(rule);

		// Parses dep
		String depName = rule.getRule().getVariable();

		SecDep secDep = new SecDep(model.getParameter(depName));
		DepXml depXml = new DepXml(secDep.getParam().getId());
		depXml.setDescription(secDep.getDescription());
		if (secDep.getParam().isSetUnits()) {
			// Adds unit
			String unitID = secDep.getParam().getUnits();
			String unitName = model.getUnitDefinition(unitID).getName();
			depXml.setUnit(unitName);

			// Adds unit category
			Map<String, UnitsFromDB> dbUnits = DBUnits.getDBUnits();
			if (dbUnits.containsKey(unitName)) {
				UnitsFromDB dbUnit = dbUnits.get(unitName);
				depXml.setCategory(dbUnit.getKind_of_property_quantity());
			}

			// Adds limits
			if (limits.containsKey(secDep.getParam().getId())) {
				Limits depLimits = limits.get(secDep.getParam().getId());
				depXml.setMin(depLimits.getMin());
				depXml.setMax(depLimits.getMax());
			}
		}

		PmmXmlDoc indeps = new PmmXmlDoc();
		PmmXmlDoc consts = new PmmXmlDoc();

		for (Parameter param : model.getListOfParameters()) {
			if (param.isConstant()) {
				ParamXml paramXml = processCoefficient(param, model.getListOfUnitDefinitions(), limits);
				consts.add(paramXml);
			} else if (!param.getId().equals(depName)) {
				IndepXml indepXml = processIndep(param, model.getListOfUnitDefinitions(), limits);
				indeps.add(indepXml);
			}
		}

		// Get model annotations
		Model2Annotation m2Annot = new Model2Annotation(model.getAnnotation());

		// EstModel
		EstModelXml estModel = Util.uncertainties2EstModel(m2Annot.getUncertainties());
		if (model.isSetName()) {
			estModel.setName(model.getName());
		}

		Model2Metadata metadata = new Model2Metadata();

		if (model.getListOfSpecies().size() == 1) {
			PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
			Model2AgentXml agentXml = new Model2AgentXml(MathUtilities.getRandomNegativeInt(), species.getName(),
					species.getDetail(), null);
			metadata.setAgentXml(agentXml);
		}

		if (model.getListOfCompartments().size() == 1) {
			PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
			Model2MatrixXml matrixXml = new Model2MatrixXml(MathUtilities.getRandomNegativeInt(), compartment.getName(),
					compartment.getDetail(), null);
			metadata.setMatrixXml(matrixXml);
		}

		// Gets model literature
		final PmmXmlDoc mLits = new PmmXmlDoc();
		for (final Reference ref : rule.getReferences()) {
			final String author = ref.getAuthor();
			final Integer year = ref.getYear();
			final String title = ref.getTitle();
			final String abstractText = ref.getAbstractText();
			final String journal = ref.getJournal();
			final String volume = ref.getVolume();
			final String issue = ref.getIssue();
			final Integer page = ref.getPage();
			final Integer approvalMode = ref.getApprovalMode();
			final String website = ref.getWebsite();
			final Integer type = ref.isSetType() ? ref.getType().value() : null;
			final String comment = ref.getComment();

			final LiteratureItem literatureItem = new LiteratureItem(author, year, title, abstractText, journal, volume,
					issue, page, approvalMode, website, type, comment);
			mLits.add(literatureItem);

			final MLiteratureItem mLiteratureItem = new MLiteratureItem(author, year, title, abstractText, journal,
					volume, issue, page, approvalMode, website, type, comment);
			metadata.addLiteratureItem(mLiteratureItem);
		}

		// Gets estimated model literature
		final PmmXmlDoc emLits = new PmmXmlDoc();
		for (final Reference ref : m2Annot.getReferences()) {
			final String author = ref.getAuthor();
			final Integer year = ref.getYear();
			final String title = ref.getTitle();
			final String abstractText = ref.getAbstractText();
			final String journal = ref.getJournal();
			final String volume = ref.getVolume();
			final String issue = ref.getIssue();
			final Integer page = ref.getPage();
			final Integer approvalMode = ref.getApprovalMode();
			final String website = ref.getWebsite();
			final Integer type = ref.isSetType() ? ref.getType().value() : null;
			final String comment = ref.getComment();

			final LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal, volume, issue,
					page, approvalMode, website, type, comment);
			emLits.add(lit);

			final EMLiteratureItem emLiteratureItem = new EMLiteratureItem(author, year, title, abstractText, journal,
					volume, issue, page, approvalMode, website, type, comment);
			metadata.addLiteratureItem(emLiteratureItem);
		}

		tuple = new KnimeTuple(SCHEMA);
		tuple.setValue(Model2Schema.ATT_MODELCATALOG, new PmmXmlDoc(catModel));
		tuple.setValue(Model2Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));
		tuple.setValue(Model2Schema.ATT_INDEPENDENT, indeps);
		tuple.setValue(Model2Schema.ATT_PARAMETER, consts);
		tuple.setValue(Model2Schema.ATT_ESTMODEL, new PmmXmlDoc(estModel));
		tuple.setValue(Model2Schema.ATT_MLIT, mLits);
		tuple.setValue(Model2Schema.ATT_EMLIT, emLits);
		tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, Model2Schema.WRITABLE);
		tuple.setValue(Model2Schema.ATT_DBUUID, "?");
		tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, m2Annot.getGlobalModelID());
		tuple.setValue(Model2Schema.ATT_METADATA, metadata);
	}

	private static ParamXml processCoefficient(Parameter param, ListOf<UnitDefinition> unitDefs,
			Map<String, Limits> limits) {
		// Creates ParamXml and adds description
		ParamXml paramXml = new ParamXml(param.getId(), param.getValue());

		// Assigns unit and category
		String unitID = param.getUnits();
		if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
			String unitName = unitDefs.get(unitID).getName();
			paramXml.setUnit(unitName);
			paramXml.setCategory(DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity());
		}

		PMFCoefficient coefficient = SBMLFactory.createPMFCoefficient(param);
		if (coefficient.isSetDescription()) {
			paramXml.setDescription(coefficient.getDescription());
		}

		// Adds correlations
		if (coefficient.isSetCorrelations()) {
			for (Correlation corr : coefficient.getCorrelations()) {
				paramXml.addCorrelation(corr.getName(), corr.getValue());
			}
		}

		// Adds limits
		if (limits.containsKey(param.getId())) {
			Limits constLimits = limits.get(param.getId());
			paramXml.setMax(constLimits.getMax());
			paramXml.setMin(constLimits.getMin());
		}

		return paramXml;
	}

	private static IndepXml processIndep(Parameter param, ListOf<UnitDefinition> unitDefs, Map<String, Limits> limits) {

		// Adds limits
		Double min = null;
		Double max = null;
		if (limits.containsKey(param.getId())) {
			Limits indepLimits = limits.get(param.getId());
			min = indepLimits.getMin();
			max = indepLimits.getMax();
		}

		IndepXml indepXml = new IndepXml(param.getId(), min, max);

		SecIndep secIndep = new SecIndep(param);
		indepXml.setDescription(secIndep.getDescription());

		// Adds unit and unit category
		String unitID = param.getUnits();
		if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
			String unitName = unitDefs.get(unitID).getName();
			indepXml.setUnit(unitName);
			indepXml.setCategory(DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity());
		}

		return indepXml;
	}
}

class Util {

	private Util() {
	}

	public static Map<String, Limits> parseConstraints(final ListOf<Constraint> constraints) {
		Map<String, Limits> paramLimits = new HashMap<>();
		for (Constraint currConstraint : constraints) {
			LimitsConstraint lc = new LimitsConstraint(currConstraint);
			Limits lcLimits = lc.getLimits();
			paramLimits.put(lcLimits.getVar(), lcLimits);
		}
		return paramLimits;
	}

	public static CatalogModelXml model1Rule2CatModel(final ModelRule rule) {
		final int formulaId = rule.getPmmlabID();
		final String formulaName = rule.getFormulaName();
		final int modelClass = rule.getModelClass().ordinal();

		String formulaString = rule.getFormula();
		formulaString = formulaString.replace("time", "Time");
		formulaString = formulaString.replace("log(", "ln(");
		final String formula = String.format("Value=%s", formulaString);

		final CatalogModelXml catModel = new CatalogModelXml(formulaId, formulaName, formula, modelClass);
		return catModel;
	}

	public static CatalogModelXml model2Rule2CatModel(final ModelRule rule) {
		final int formulaId = rule.getPmmlabID();
		final String formulaName = rule.getFormulaName();
		final int modelClass = rule.getModelClass().ordinal();

		String formulaString = rule.getFormula();
		formulaString = formulaString.replace("time", "Time");
		formulaString = formulaString.replace("log(", "ln(");
		final String formula = String.format("%s=%s", rule.getVariable(), formulaString);

		final CatalogModelXml catModel = new CatalogModelXml(formulaId, formulaName, formula, modelClass);
		return catModel;
	}

	public static EstModelXml uncertainties2EstModel(final Uncertainties uncertainties) {
		final int estModelId = uncertainties.getID();
		final String modelName = uncertainties.getModelName();
		final Double sse = uncertainties.getSSE();
		final Double rms = uncertainties.getRMS();
		final Double r2 = uncertainties.getR2();
		final Double aic = uncertainties.getAIC();
		final Double bic = uncertainties.getBIC();
		final Integer dof = uncertainties.getDOF();

		final EstModelXml estModel = new EstModelXml(estModelId, modelName, sse, rms, r2, aic, bic, dof);
		return estModel;
	}

	public static PmmXmlDoc parseMiscs(final Map<String, Double> miscs) {
		PmmXmlDoc cell = new PmmXmlDoc();

		if (miscs != null) {
			// First misc item has id -1 and the rest of items have negative
			// ints
			int counter = -1;
			for (Map.Entry<String, Double> entry : miscs.entrySet()) {
				String name = entry.getKey();
				Double value = entry.getValue();

				if (name.equals("Temperature")) {
					final List<String> categories = Arrays.asList(Categories.getTempCategory().getName());
					final String description = name;
					final String unit = Categories.getTempCategory().getStandardUnit();

					final MiscXml miscXml = new MiscXml(counter, name, description, value, categories, unit);
					cell.add(miscXml);

					counter--;
				} else if (name.equals("pH")) {
					final List<String> categories = Arrays.asList(Categories.getPhCategory().getName());
					final String description = name;
					final String unit = Categories.getPhUnit();

					final MiscXml miscXml = new MiscXml(counter, name, description, value, categories, unit);
					cell.add(miscXml);

					counter--;
				}
			}
		}
		return cell;
	}

	public static KnimeTuple mergeTuples(final DataTuple dataTuple, final Model1Tuple m1Tuple) {
		final KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM1DataSchema());

		// Copies data columns
		tuple.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.tuple.getInt(TimeSeriesSchema.ATT_CONDID));
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, dataTuple.tuple.getString(TimeSeriesSchema.ATT_COMBASEID));
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.tuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.tuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, dataTuple.tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
		tuple.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.tuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.tuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.tuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.tuple.getString(TimeSeriesSchema.ATT_DBUUID));
		tuple.setValue(TimeSeriesSchema.ATT_METADATA, dataTuple.tuple.getPmmXml(TimeSeriesSchema.ATT_METADATA));
		
		// Copies model1 columns
		tuple.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
		tuple.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
		tuple.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
		tuple.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
		tuple.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
		tuple.setValue(Model1Schema.ATT_MLIT, m1Tuple.tuple.getPmmXml(Model1Schema.ATT_MLIT));
		tuple.setValue(Model1Schema.ATT_EMLIT, m1Tuple.tuple.getPmmXml(Model1Schema.ATT_EMLIT));
		tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, m1Tuple.tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
		tuple.setValue(Model1Schema.ATT_DBUUID, m1Tuple.tuple.getString(Model1Schema.ATT_DBUUID));
		tuple.setValue(Model1Schema.ATT_METADATA, m1Tuple.tuple.getPmmXml(Model1Schema.ATT_METADATA));
		
		return tuple;
	}

	public static KnimeTuple mergeTuples(final KnimeTuple dataTuple, final KnimeTuple m1Tuple,
			final KnimeTuple m2Tuple) {

		final KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM12DataSchema());

		tuple.setValue(TimeSeriesSchema.ATT_CONDID, dataTuple.getInt(TimeSeriesSchema.ATT_CONDID));
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, dataTuple.getString(TimeSeriesSchema.ATT_COMBASEID));
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, dataTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, dataTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
		tuple.setValue(TimeSeriesSchema.ATT_MISC, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, dataTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, dataTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, dataTuple.getString(TimeSeriesSchema.ATT_DBUUID));
		tuple.setValue(TimeSeriesSchema.ATT_METADATA, dataTuple.getPmmXml(TimeSeriesSchema.ATT_METADATA));

		// Copies model1 columns
		tuple.setValue(Model1Schema.ATT_MODELCATALOG, m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
		tuple.setValue(Model1Schema.ATT_DEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
		tuple.setValue(Model1Schema.ATT_INDEPENDENT, m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
		tuple.setValue(Model1Schema.ATT_PARAMETER, m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
		tuple.setValue(Model1Schema.ATT_ESTMODEL, m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
		tuple.setValue(Model1Schema.ATT_MLIT, m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
		tuple.setValue(Model1Schema.ATT_EMLIT, m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
		tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
		tuple.setValue(Model1Schema.ATT_DBUUID, m1Tuple.getString(Model1Schema.ATT_DBUUID));
		tuple.setValue(Model1Schema.ATT_METADATA, m1Tuple.getPmmXml(Model1Schema.ATT_METADATA));

		// Copies model2 columns
		tuple.setValue(Model2Schema.ATT_MODELCATALOG, m2Tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG));
		tuple.setValue(Model2Schema.ATT_DEPENDENT, m2Tuple.getPmmXml(Model2Schema.ATT_DEPENDENT));
		tuple.setValue(Model2Schema.ATT_INDEPENDENT, m2Tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT));
		tuple.setValue(Model2Schema.ATT_PARAMETER, m2Tuple.getPmmXml(Model2Schema.ATT_PARAMETER));
		tuple.setValue(Model2Schema.ATT_ESTMODEL, m2Tuple.getPmmXml(Model2Schema.ATT_ESTMODEL));
		tuple.setValue(Model2Schema.ATT_MLIT, m2Tuple.getPmmXml(Model2Schema.ATT_MLIT));
		tuple.setValue(Model2Schema.ATT_EMLIT, m2Tuple.getPmmXml(Model2Schema.ATT_EMLIT));
		tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, m2Tuple.getInt(Model2Schema.ATT_DATABASEWRITABLE));
		tuple.setValue(Model2Schema.ATT_DBUUID, m2Tuple.getString(Model2Schema.ATT_DBUUID));
		tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, m2Tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID));
		tuple.setValue(Model2Schema.ATT_METADATA, m2Tuple.getPmmXml(Model2Schema.ATT_METADATA));

		return tuple;
	}
}
