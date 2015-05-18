/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
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
 ******************************************************************************/
package de.bund.bfr.knime.pmm.sbmlwriter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.defaultnodesettings.SettingsModelOptionalString;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompModelPlugin;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.ext.comp.Submodel;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.annotation.CreatedNode;
import de.bund.bfr.knime.pmm.annotation.CreatorNode;
import de.bund.bfr.knime.pmm.annotation.ModelClassNode;
import de.bund.bfr.knime.pmm.annotation.ModifiedNode;
import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.sbmlutil.Agent;
import de.bund.bfr.knime.pmm.sbmlutil.Coefficient;
import de.bund.bfr.knime.pmm.sbmlutil.DBUnits;
import de.bund.bfr.knime.pmm.sbmlutil.DataFile;
import de.bund.bfr.knime.pmm.sbmlutil.Experiment;
import de.bund.bfr.knime.pmm.sbmlutil.LimitsConstraint;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;
import de.bund.bfr.knime.pmm.sbmlutil.Model1Annotation;
import de.bund.bfr.knime.pmm.sbmlutil.Model1Rule;
import de.bund.bfr.knime.pmm.sbmlutil.Model2Annotation;
import de.bund.bfr.knime.pmm.sbmlutil.Model2Rule;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;
import de.bund.bfr.knime.pmm.sbmlutil.PMFFile;
import de.bund.bfr.knime.pmm.sbmlutil.SecIndep;
import de.bund.bfr.knime.pmm.sbmlutil.UnitDefinitionWrapper;
import de.bund.bfr.knime.pmm.sbmlutil.Util;
import de.bund.bfr.numl.NuMLDocument;

/**
 * This is the model implementation of SBMLWriter.
 * 
 * 
 * @author Christian Thoens
 */
public class SBMLWriterNodeModel extends NodeModel {
	protected static final String CFG_OUT_PATH = "outPath";
	protected static final String CFG_VARIABLE_PARAM = "variableParams";
	protected static final String CFG_MODEL_NAME = "modelName";
	protected static final String CFG_CREATOR_GIVEN_NAME = "CreatorGivenName";
	protected static final String CFG_CREATOR_FAMILY_NAME = "CreatorFamilyName";
	protected static final String CFG_CREATOR_CONTACT = "CreatorContact";
	protected static final String CFG_CREATED_DATE = "CreationDate";
	protected static final String CFG_LAST_MODIFIED_DATE = "ModifiedDate";
	protected static final String CFG_REFERENCE = "Reference";

	private SettingsModelString outPath = new SettingsModelString(CFG_OUT_PATH,
			null);
	private SettingsModelString variableParams = new SettingsModelOptionalString(
			CFG_VARIABLE_PARAM, null, false);
	private SettingsModelString modelName = new SettingsModelString(
			CFG_MODEL_NAME, null);
	private SettingsModelString creatorGivenName = new SettingsModelString(
			CFG_CREATOR_GIVEN_NAME, null);
	private SettingsModelString creatorFamilyName = new SettingsModelString(
			CFG_CREATOR_FAMILY_NAME, null);
	private SettingsModelString creatorContact = new SettingsModelString(
			CFG_CREATOR_CONTACT, null);
	private SettingsModelDate createdDate = new SettingsModelDate(
			CFG_CREATED_DATE);
	private SettingsModelDate modifiedDate = new SettingsModelDate(
			CFG_LAST_MODIFIED_DATE);
	private SettingsModelString reference = new SettingsModelString(
			CFG_REFERENCE, null);

	private KnimeSchema schema;

	private ModelType modelType;

	/**
	 * Constructor for the node model.
	 */
	protected SBMLWriterNodeModel() {
		super(1, 0);
		schema = null;
		modelType = null;
	}

	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		List<KnimeTuple> tuples = PmmUtilities.getTuples(inData[0], schema);
		List<Experiment> experiments = null;

		// Retrieve info from dialog
		Map<String, String> dlgInfo = new HashMap<>(); // dialog info
		String givenName = creatorGivenName.getStringValue();
		if (!givenName.isEmpty())
			dlgInfo.put("GivenName", givenName);

		String familyName = creatorFamilyName.getStringValue();
		if (!familyName.isEmpty())
			dlgInfo.put("FamilyName", familyName);

		String contact = creatorContact.getStringValue();
		if (!contact.isEmpty())
			dlgInfo.put("Contact", contact);

		Date created = createdDate.getDate();
		if (created != null) {
			dlgInfo.put("Created", created.toString());
		}

		Date modified = modifiedDate.getDate();
		if (modified != null) {
			dlgInfo.put("Modified", modified.toString());
		}

		if (modelType == ModelType.PRIMARY) {
			PrimaryTableReader reader = new PrimaryTableReader(tuples, dlgInfo);
			experiments = reader.getExperiments();
		} else if (modelType == ModelType.TERTIARY) {
			TertiaryTableReader reader = new TertiaryTableReader(tuples,
					dlgInfo);
			experiments = reader.getExperiments();
		} else if (modelType == ModelType.SECONDARY) {
			SecondaryTableReader reader = new SecondaryTableReader(tuples,
					dlgInfo);
			experiments = reader.getExperiments();
		}

		PMFFile.write(outPath.getStringValue(), modelName.getStringValue(),
				experiments, exec);
		return new BufferedDataTable[] {};
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
		if (outPath.getStringValue() == null
				|| modelName.getStringValue() == null
				|| variableParams.getStringValue() == null) {
			throw new InvalidSettingsException("Node must be configured");
		} else {
			try {
				modelType = ModelType.getTableType((DataTableSpec) inSpecs[0]);
				switch (modelType) {
				case PRIMARY:
					schema = SchemaFactory.createM1DataSchema();
					break;
				case SECONDARY:
					schema = SchemaFactory.createM2Schema();
					break;
				case TERTIARY:
					schema = SchemaFactory.createM12DataSchema();
					break;
				}
				return new DataTableSpec[] {};
			} catch (Exception e) {
				throw new InvalidSettingsException("Invalid input");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		outPath.saveSettingsTo(settings);
		variableParams.saveSettingsTo(settings);
		modelName.saveSettingsTo(settings);
		creatorGivenName.saveSettingsTo(settings);
		creatorFamilyName.saveSettingsTo(settings);
		creatorContact.saveSettingsTo(settings);
		createdDate.saveSettingsTo(settings);
		modifiedDate.saveSettingsTo(settings);
		reference.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		outPath.loadSettingsFrom(settings);
		variableParams.loadSettingsFrom(settings);
		modelName.loadSettingsFrom(settings);
		creatorGivenName.loadSettingsFrom(settings);
		creatorFamilyName.loadSettingsFrom(settings);
		creatorContact.loadSettingsFrom(settings);
		createdDate.loadSettingsFrom(settings);
		modifiedDate.loadSettingsFrom(settings);
		reference.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		outPath.validateSettings(settings);
		variableParams.validateSettings(settings);
		modelName.validateSettings(settings);
		creatorGivenName.validateSettings(settings);
		creatorFamilyName.validateSettings(settings);
		creatorContact.validateSettings(settings);
		createdDate.validateSettings(settings);
		modifiedDate.validateSettings(settings);
		reference.validateSettings(settings);
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
}

abstract class TableReader {
	protected final static int LEVEL = 3;
	protected final static int VERSION = 1;

	protected List<Experiment> experiments = new LinkedList<>();

	public List<Experiment> getExperiments() {
		return experiments;
	}

	protected static void renameLog(KnimeTuple tuple) {
		PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
		CatalogModelXml model = (CatalogModelXml) modelXml.get(0);

		model.setFormula(MathUtilities.replaceVariable(model.getFormula(),
				"log", "log10"));
		tuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
	}

	protected static void replaceCelsiusAndFahrenheit(KnimeTuple tuple) {
		final String CELSIUS = "°C";
		final String FAHRENHEIT = "°F";
		final String KELVIN = "K";

		PmmXmlDoc indepXml = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
		PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
		CatalogModelXml model = (CatalogModelXml) modelXml.get(0);
		Category temp = Categories.getTempCategory();

		for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
			IndepXml indep = (IndepXml) el;

			if (CELSIUS.equals(indep.getUnit())) {
				try {
					String replacement = "("
							+ temp.getConversionString(indep.getName(), KELVIN,
									CELSIUS) + ")";

					model.setFormula(MathUtilities.replaceVariable(
							model.getFormula(), indep.getName(), replacement));
					indep.setUnit(KELVIN);
					indep.setMin(temp.convert(indep.getMin(), CELSIUS, KELVIN));
					indep.setMax(temp.convert(indep.getMax(), CELSIUS, KELVIN));
				} catch (ConvertException e) {
					e.printStackTrace();
				}
			} else if (FAHRENHEIT.equals(indep.getUnit())) {
				try {
					String replacement = "("
							+ temp.getConversionString(indep.getName(), KELVIN,
									FAHRENHEIT) + ")";

					model.setFormula(MathUtilities.replaceVariable(
							model.getFormula(), indep.getName(), replacement));
					indep.setUnit(FAHRENHEIT);
					indep.setMin(temp.convert(indep.getMin(), FAHRENHEIT,
							KELVIN));
					indep.setMax(temp.convert(indep.getMax(), FAHRENHEIT,
							KELVIN));
				} catch (ConvertException e) {
					e.printStackTrace();
				}
			}
		}

		tuple.setValue(Model1Schema.ATT_INDEPENDENT, indepXml);
		tuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
	}

	/**
	 * Create a document annotation.
	 * 
	 * @param givenName
	 *            . Creator given name.
	 * @param familyName
	 *            : Creator family name.
	 * @param contact
	 *            : Creator contact.
	 * @param created
	 *            : Created date.
	 * @param modified
	 *            : Modified date.
	 */
	protected Annotation createDocAnnotation(Map<String, String> docInfo) {
		Annotation annot = new Annotation();

		// pmf container
		XMLTriple pmfTriple = new XMLTriple("metadata", null, "pmf");
		XMLNode pmfNode = new XMLNode(pmfTriple, null, null);

		String givenName = docInfo.get("GivenName");
		String familyName = docInfo.get("FamilyName");
		String contact = docInfo.get("Contact");

		if (givenName != null || familyName != null || contact != null) {
			CreatorNode creatorNode = new CreatorNode(givenName, familyName,
					contact);
			pmfNode.addChild(creatorNode.getNode());
		}

		// Created date
		if (docInfo.containsKey("Created")) {
			CreatedNode createdNode = new CreatedNode(docInfo.get("Created"));
			pmfNode.addChild(createdNode.getNode());
		}

		// modified date
		if (docInfo.containsKey("Modified")) {
			ModifiedNode modifiedNode = new ModifiedNode(
					docInfo.get("Modified"));
			pmfNode.addChild(modifiedNode.getNode());
		}

		// model type
		if (docInfo.containsKey("type")) {
			ModelClassNode typeNode = new ModelClassNode(docInfo.get("type"));
			pmfNode.addChild(typeNode.getNode());
		}

		// add non-rdf annotation
		annot.setNonRDFAnnotation(pmfNode);

		return annot;
	}

	/**
	 * Parse model quality tags such as SSE and RMS from the EstModelXml cell.
	 * 
	 * @param estModel
	 * @return
	 */
	static Map<String, String> parseQualityTags(EstModelXml estModel) {
		Map<String, String> qualityTags = new HashMap<>();

		String dataUsage = estModel.getComment();
		if (dataUsage != null) {
			qualityTags.put("dataUsage", dataUsage);
		}

		String dataName = estModel.getName();
		if (dataUsage != null) {
			qualityTags.put("dataName", dataName);
		} else {
			qualityTags.put("dataName", "Missing data name");
		}

		Double r2 = estModel.getR2();
		if (r2 != null) {
			qualityTags.put("r-squared", r2.toString());
		}

		Double rms = estModel.getRms();
		if (rms != null) {
			qualityTags.put("rootMeanSquaredError", rms.toString());
		}

		Double sse = estModel.getSse();
		if (sse != null) {
			qualityTags.put("sumSquaredError", sse.toString());
		}

		Double aic = estModel.getAic();
		if (aic != null) {
			qualityTags.put("AIC", aic.toString());
		}

		Double bic = estModel.getBic();
		if (bic != null) {
			qualityTags.put("BIC", bic.toString());
		}

		Integer dof = estModel.getDof();
		if (dof != null) {
			qualityTags.put("degreesOfFreedom", dof.toString());
		}

		return qualityTags;
	}

	public static void addNamespaces(SBMLDocument doc) {
		doc.addDeclaredNamespace("xmlns:xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		doc.addDeclaredNamespace("xmlns:pmml", "http://www.dmg.org/PMML-4_2");
		doc.addDeclaredNamespace("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
		doc.addDeclaredNamespace("xmlns:dc", "http://purl.org/dc/elements/1.1");
		doc.addDeclaredNamespace("xmlns:dcterms", "http://purl.org/dc/terms/");
		doc.addDeclaredNamespace("xmlns:numl",
				"http://www.numl.org/numl/level1/version1");
		doc.addDeclaredNamespace("xmlns:xlink", "http//www.w3.org/1999/xlink");
	}

	public void addUnitDefinitions(Model model, DepXml depXml,
			List<IndepXml> indepXmls, List<ParamXml> constXmls) {
		// Get units from dep, indeps and consts
		HashSet<String> units = new HashSet<>();
		if (depXml.getUnit() != null) {
			units.add(depXml.getUnit());
		}

		for (IndepXml indepXml : indepXmls) {
			if (indepXml.getUnit() != null) {
				units.add(indepXml.getUnit());
			}
		}

		for (ParamXml paramXml : constXmls) {
			if (paramXml.getUnit() != null) {
				units.add(paramXml.getUnit());
			}
		}

		// Create and add unit definitions for the units present in DB. Missing
		// units in DB will not be retrievable and thus will lack a list of
		// units
		for (String unit : units) {
			UnitDefinition unitDefinition = new UnitDefinition(
					Util.createId(unit));
			unitDefinition.setLevel(LEVEL);
			unitDefinition.setVersion(VERSION);
			unitDefinition.setName(unit);

			// Current unit `unit` is in the DB
			if (DBUnits.getDBUnits().containsKey(unit)) {
				UnitsFromDB dbUnit = DBUnits.getDBUnits().get(unit);

				String mathml = dbUnit.getMathML_string();
				if (mathml != null && !mathml.isEmpty()) {
					UnitDefinitionWrapper wrapper = UnitDefinitionWrapper
							.xmlToUnitDefinition(dbUnit.getMathML_string());
					UnitDefinition ud = wrapper.getUnitDefinition();
					for (Unit wrapperUnit : ud.getListOfUnits()) {
						unitDefinition.addUnit(new Unit(wrapperUnit));
					}
				}
			}

			model.addUnitDefinition(unitDefinition);
		}
	}
}

class PrimaryTableReader extends TableReader {

	public PrimaryTableReader(List<KnimeTuple> tuples,
			Map<String, String> dlgInfo) throws URISyntaxException {
		super();

		dlgInfo.put("type", "Primary");

		for (KnimeTuple tuple : tuples) {
			SBMLDocument model = parsePrimaryTuple(tuple, dlgInfo);

			// Add NuML doc
			// TODO: Empty time series use case

			// * Get data points
			PmmXmlDoc mdData = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);

			// No data for this model
			if (mdData.size() == 0) {
				experiments.add(new Experiment(model));
			} else {
				// XML time series
				List<TimeSeriesXml> timeSeries = new LinkedList<>();
				for (PmmXmlElementConvertable point : mdData.getElementSet()) {
					timeSeries.add((TimeSeriesXml) point);
				}
				Map<Double, Double> dim = new HashMap<>(); // dimension
				for (TimeSeriesXml point : timeSeries) {
					dim.put(point.getTime(), point.getConcentration());
				}

				// * Create NuML document with this time series
				String unit = ((TimeSeriesXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_TIMESERIES).get(0))
						.getConcentrationUnit();
				MatrixXml matrixXml = (MatrixXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0);
				AgentXml agentXml = (AgentXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0);
				String depUnit = (String) ((DepXml) tuple.getPmmXml(
						Model1Schema.ATT_DEPENDENT).get(0)).getUnit();
				DataFile dataFile = new DataFile(dim, unit, matrixXml,
						agentXml, depUnit, dlgInfo);

				// * Get and add dataset
				NuMLDocument data = dataFile.getDocument();

				experiments.add(new Experiment(model, data));
			}
		}
	}

	private SBMLDocument parsePrimaryTuple(KnimeTuple tuple,
			Map<String, String> docInfo) {
		replaceCelsiusAndFahrenheit(tuple);
		renameLog(tuple);

		// retrieve XML cells
		CatalogModelXml modelXml = (CatalogModelXml) tuple.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estXml = (EstModelXml) tuple.getPmmXml(
				Model1Schema.ATT_ESTMODEL).get(0);
		DepXml depXml = (DepXml) tuple.getPmmXml(Model1Schema.ATT_DEPENDENT)
				.get(0);
		IndepXml indep = (IndepXml) tuple.getPmmXml(
				Model1Schema.ATT_INDEPENDENT).get(0);
		AgentXml organismXml = (AgentXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_AGENT).get(0);
		MatrixXml matrixXml = (MatrixXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);
		String modelId = "model";

		SBMLDocument doc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Composition package
		doc.enablePackage(CompConstants.shortLabel);

		addNamespaces(doc);

		// Document annotation
		Annotation docAnnot = createDocAnnotation(docInfo);
		doc.setAnnotation(docAnnot);

		Model model = doc.createModel(modelId);
		model.setName(modelXml.getName());

		// Annotation
		String modelTitle = estXml.getName();
		Map<String, String> qualityTags = parseQualityTags(estXml);

		// Get literature references
		List<PmmXmlElementConvertable> litItems = tuple.getPmmXml(
				Model1Schema.ATT_EMLIT).getElementSet();
		List<LiteratureItem> lits = new LinkedList<>();
		for (PmmXmlElementConvertable item : litItems) {
			LiteratureItem lit = (LiteratureItem) item;
			lits.add(lit);
		}

		// Add model annotations
		int condID = tuple.getInt(TimeSeriesSchema.ATT_CONDID);
		String combaseID = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
		Model1Annotation primModelAnnotation = new Model1Annotation(modelId,
				modelTitle, qualityTags, lits, combaseID, condID);
		model.getAnnotation()
				.setNonRDFAnnotation(primModelAnnotation.getNode());

		// Create compartment and add it to the model
		List<MiscXml> miscs = new LinkedList<>();
		for (PmmXmlElementConvertable misc : tuple.getPmmXml(
				TimeSeriesSchema.ATT_MISC).getElementSet()) {
			miscs.add((MiscXml) misc);
		}

		Map<String, Double> miscsMap = new HashMap<>();
		for (MiscXml misc : miscs) {
			miscsMap.put(misc.getName(), misc.getValue());
		}

		Matrix matrix = new Matrix(matrixXml, miscsMap);
		Compartment c = matrix.getCompartment();
		model.addCompartment(c);

		// Create species and add it to the model
		Agent organims = new Agent(organismXml, depXml.getUnit());
		model.addSpecies(organims.getSpecies());

		// Add indep constraint
		if (!indep.getName().isEmpty()) {
			Double min = indep.getMin();
			Double max = indep.getMax();
			LimitsConstraint lc = new LimitsConstraint(indep.getName(), min,
					max);
			if (lc.getConstraint() != null) {
				model.addConstraint(lc.getConstraint());
			}
		}

		// Add independent parameter
		Parameter indepParam = new Parameter(Categories.getTime());
		indepParam.setValue(0.0);
		indepParam.setConstant(false);
		indepParam.setUnits(indep.getUnit());
		model.addParameter(indepParam);

		// Add constant parameters
		LinkedList<ParamXml> constXmls = new LinkedList<>();
		for (PmmXmlElementConvertable pmmParam : tuple.getPmmXml(
				Model1Schema.ATT_PARAMETER).getElementSet()) {
			constXmls.add((ParamXml) pmmParam);
		}

		for (ParamXml constXml : constXmls) {
			// Add constant parameter
			Parameter param = new Coefficient(constXml).getParameter();
			model.addParameter(param);

			// Add constraint
			LimitsConstraint lc = new LimitsConstraint(constXml.getName(),
					constXml.getMin(), constXml.getMax());
			if (lc.getConstraint() != null) {
				model.addConstraint(lc.getConstraint());
			}
		}

		LinkedList<IndepXml> indepXmls = new LinkedList<>(Arrays.asList(indep));
		addUnitDefinitions(model, depXml, indepXmls, constXmls);

		// Create rule of the model and add it to the rest of rules
		Model1Rule model1Rule = Model1Rule.convertCatalogModelXmlToModel1Rule(
				modelXml, organims.getSpecies().getId());
		model.addRule(model1Rule.getRule());
		return doc;
	}
}

class SecondaryTableReader extends TableReader {

	public SecondaryTableReader(List<KnimeTuple> tuples,
			Map<String, String> dlgInfo) throws URISyntaxException {
		super();

		for (KnimeTuple tuple : tuples) {
			SBMLDocument sbmlDoc = parseSecondaryTuple(tuple, dlgInfo);
			experiments.add(new Experiment(sbmlDoc));
		}
	}

	private SBMLDocument parseSecondaryTuple(KnimeTuple tuple,
			Map<String, String> docInfo) {

		// retrieve XML cells
		CatalogModelXml catModelXml = (CatalogModelXml) tuple.getPmmXml(
				Model2Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estModelXml = (EstModelXml) tuple.getPmmXml(
				Model2Schema.ATT_ESTMODEL).get(0);
		DepXml depXml = (DepXml) tuple.getPmmXml(Model2Schema.ATT_DEPENDENT)
				.get(0);

		// Get independent parameters
		LinkedList<IndepXml> indepXmls = new LinkedList<>();
		for (PmmXmlElementConvertable xmlItem : tuple.getPmmXml(
				Model2Schema.ATT_INDEPENDENT).getElementSet()) {
			indepXmls.add((IndepXml) xmlItem);
		}

		// Get constant parameters
		LinkedList<ParamXml> constXmls = new LinkedList<>();
		for (PmmXmlElementConvertable xmlItem : tuple.getPmmXml(
				Model2Schema.ATT_PARAMETER).getElementSet()) {
			constXmls.add((ParamXml) xmlItem);
		}

		SBMLDocument doc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Composition package
		doc.enablePackage(CompConstants.shortLabel);

		// Add SBML comp plugin to SBML document
		CompSBMLDocumentPlugin docCompPlugin = (CompSBMLDocumentPlugin) doc
				.getPlugin(CompConstants.shortLabel);

		addNamespaces(doc);

		// Create model definition
		String modelDefinitionId = "model_" + depXml.getName();
		Model model = docCompPlugin.createModelDefinition(modelDefinitionId);
		model.setName(catModelXml.getName());

		addUnitDefinitions(model, depXml, indepXmls, constXmls);

		// Add dep
		Parameter depParam = new Parameter(depXml.getName());
		depParam.setConstant(false);
		depParam.setValue(0.0);
		if (depXml.getUnit() != null) {
			depParam.setUnits(Util.createId(depXml.getUnit()));
		}
		model.addParameter(depParam);

		// Add independent parameters
		for (IndepXml indepXml : indepXmls) {
			// Create SBML parameter
			Parameter indepParam = new SecIndep(indepXml).getParam();

			// Assign unit
			if (indepXml.getUnit() == null) {
				indepParam.setUnits("dimensionless");
			} else {
				indepParam.setUnits(Util.createId(indepXml.getUnit()));
			}
			model.addParameter(indepParam);

			// Add constraint
			Double min = indepXml.getMin(), max = indepXml.getMax();
			LimitsConstraint lc = new LimitsConstraint(indepXml.getName(), min,
					max);
			if (lc.getConstraint() != null) {
				model.addConstraint(lc.getConstraint());
			}
		}

		// Add constant parameters
		for (ParamXml paramXml : constXmls) {
			// Create SBML parameter
			Parameter constParam = new Coefficient(paramXml).getParameter();

			// Assign unit
			if (paramXml.getUnit() == null) {
				constParam.setUnits("dimensionless");
			} else {
				constParam.setUnits(Util.createId(paramXml.getUnit()));
			}
			model.addParameter(constParam);

			// Add constraint
			Double min = paramXml.getMin(), max = paramXml.getMax();
			LimitsConstraint lc = new LimitsConstraint(paramXml.getName(), min,
					max);
			if (lc.getConstraint() != null) {
				model.addConstraint(lc.getConstraint());
			}
		}

		Model2Rule rule2 = Model2Rule
				.convertCatalogModelXmlToModel2Rule(catModelXml);
		model.addRule(rule2.getRule());

		// Add literature items
		List<PmmXmlElementConvertable> litItems = tuple.getPmmXml(
				Model2Schema.ATT_EMLIT).getElementSet();
		LinkedList<LiteratureItem> lits = new LinkedList<>();
		for (PmmXmlElementConvertable item : litItems) {
			lits.add((LiteratureItem) item);
		}

		int globalModelID = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);

		// Add annotation
		Map<String, String> uncertainties = parseQualityTags(estModelXml);
		Model2Annotation modelAnnotation = new Model2Annotation(globalModelID,
				uncertainties, lits);
		model.getAnnotation().setNonRDFAnnotation(modelAnnotation.getNode());

		return doc;
	}
}

class TertiaryTableReader extends TableReader {

	public TertiaryTableReader(List<KnimeTuple> tuples,
			Map<String, String> dlgInfo) throws URISyntaxException {
		super();

		dlgInfo.put("type", "Tertiary");

		HashMap<Integer, HashMap<Integer, List<KnimeTuple>>> globalModels = new HashMap<>();
		for (KnimeTuple tuple : tuples) {
			Integer globalModelID = tuple
					.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
			Integer condID = tuple.getInt(TimeSeriesSchema.ATT_CONDID);

			// global model is in globalModels
			if (globalModels.containsKey(globalModelID)) {
				// Get global model
				HashMap<Integer, List<KnimeTuple>> globalModel = globalModels
						.get(globalModelID);
				// globalModel has tertiary model with condID => Add tuple to
				// this tertiary model
				if (globalModel.containsKey(condID)) {
					globalModel.get(condID).add(tuple);
				}
				// Otherwise, create a tertiary model with condID and add it the
				// current tuple
				else {
					LinkedList<KnimeTuple> tertiaryModel = new LinkedList<>();
					tertiaryModel.add(tuple);
					globalModel.put(condID, tertiaryModel);
				}
			}

			// else, create tertiary model with condID and add it to new global
			// model
			else {
				// Create new global model
				HashMap<Integer, List<KnimeTuple>> globalModel = new HashMap<>();

				// Create tertiary model and add it to new global model
				LinkedList<KnimeTuple> tertiaryModel = new LinkedList<>();
				tertiaryModel.add(tuple);
				globalModel.put(condID, tertiaryModel);

				// Add new global model
				globalModels.put(globalModelID, globalModel);
			}
		}

		for (Map<Integer, List<KnimeTuple>> globalModel : globalModels.values()) {
			for (List<KnimeTuple> tertiaryModel : globalModel.values()) {

				SBMLDocument model = parseTertiaryTuple(tertiaryModel, dlgInfo);

				// Add NuML doc
				KnimeTuple tuple = tertiaryModel.get(0);

				// * Get data points
				PmmXmlDoc mdData = tuple
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);

				// No data for this model
				if (mdData.size() == 0) {
					experiments.add(new Experiment(model));
				} else {
					// XML time series
					List<TimeSeriesXml> timeSeries = new LinkedList<>();
					for (PmmXmlElementConvertable point : mdData
							.getElementSet()) {
						timeSeries.add((TimeSeriesXml) point);
					}
					Map<Double, Double> dim = new HashMap<>(); // dimension
					for (TimeSeriesXml point : timeSeries) {
						dim.put(point.getTime(), point.getConcentration());
					}

					// * Create NuML document with this time series
					String unit = ((TimeSeriesXml) tuple.getPmmXml(
							TimeSeriesSchema.ATT_TIMESERIES).get(0))
							.getConcentrationUnit();
					MatrixXml matrixXml = (MatrixXml) tuple.getPmmXml(
							TimeSeriesSchema.ATT_MATRIX).get(0);
					AgentXml agentXml = (AgentXml) tuple.getPmmXml(
							TimeSeriesSchema.ATT_AGENT).get(0);
					String depUnit = (String) ((DepXml) tuple.getPmmXml(
							Model1Schema.ATT_DEPENDENT).get(0)).getUnit();
					DataFile dataFile = new DataFile(dim, unit, matrixXml,
							agentXml, depUnit, dlgInfo);

					// * Get and add data set
					NuMLDocument data = dataFile.getDocument();

					experiments.add(new Experiment(model, data));
				}
			}
		}
	}

	private SBMLDocument parseTertiaryTuple(List<KnimeTuple> tuples,
			Map<String, String> docInfo) {
		// modify formulas
		for (KnimeTuple tuple : tuples) {
			replaceCelsiusAndFahrenheit(tuple);
			renameLog(tuple);
		}

		// retrieve common cells from the first tuple
		KnimeTuple firstTuple = tuples.get(0);
		CatalogModelXml modelXml = (CatalogModelXml) firstTuple.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estXml = (EstModelXml) firstTuple.getPmmXml(
				Model1Schema.ATT_ESTMODEL).get(0);
		DepXml depXml = (DepXml) firstTuple.getPmmXml(
				Model1Schema.ATT_DEPENDENT).get(0);
		IndepXml indep = (IndepXml) firstTuple.getPmmXml(
				Model1Schema.ATT_INDEPENDENT).get(0);
		AgentXml organismXml = (AgentXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_AGENT).get(0);
		MatrixXml matrixXml = (MatrixXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);
		String modelId = "model";

		SBMLDocument doc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Composition package
		doc.enablePackage(CompConstants.shortLabel);
		CompSBMLDocumentPlugin compDocPlugin = (CompSBMLDocumentPlugin) doc
				.getPlugin(CompConstants.shortLabel);

		// Document annotation
		Annotation docAnnot = createDocAnnotation(docInfo);
		doc.setAnnotation(docAnnot);

		addNamespaces(doc);

		Model model = doc.createModel(modelId);
		model.setName(modelXml.getName());
		CompModelPlugin compModelPlugin = (CompModelPlugin) model
				.getPlugin(CompConstants.shortLabel);

		// Annotation
		String modelTitle = estXml.getName();
		Integer modelClassNum = modelXml.getModelClass();
		if (modelClassNum == null) {
			modelClassNum = Util.MODELCLASS_NUMS.get("unknown");
		}
		Map<String, String> qualityTags = parseQualityTags(estXml);

		// Get literature references
		List<PmmXmlElementConvertable> litItems = firstTuple.getPmmXml(
				Model1Schema.ATT_EMLIT).getElementSet();
		List<LiteratureItem> lits = new LinkedList<>();
		for (PmmXmlElementConvertable item : litItems) {
			LiteratureItem lit = (LiteratureItem) item;
			lits.add(lit);
		}

		// Add model annotations
		int condID = firstTuple.getInt(TimeSeriesSchema.ATT_CONDID);
		String combaseID = firstTuple.getString(TimeSeriesSchema.ATT_COMBASEID);
		Model1Annotation primModelAnnotation = new Model1Annotation(modelId,
				modelTitle, qualityTags, lits, combaseID, condID);
		model.getAnnotation()
				.setNonRDFAnnotation(primModelAnnotation.getNode());

		// Create a compartment and add it to the model
		List<MiscXml> miscs = new LinkedList<>();
		for (PmmXmlElementConvertable misc : firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_MISC).getElementSet()) {
			miscs.add((MiscXml) misc);
		}

		Map<String, Double> miscsMap = new HashMap<>();
		for (MiscXml misc : miscs) {
			miscsMap.put(misc.getName(), misc.getValue());
		}

		Matrix matrix = new Matrix(matrixXml, miscsMap);
		Compartment compartment = matrix.getCompartment();
		model.addCompartment(compartment);

		// Create species and add it to the model
		Agent organism = new Agent(organismXml, depXml.getUnit());
		model.addSpecies(organism.getSpecies());

		// Add indep constraint
		if (!indep.getName().isEmpty()) {
			Double min = indep.getMin();
			Double max = indep.getMax();
			LimitsConstraint lc = new LimitsConstraint(indep.getName(), min,
					max);
			if (lc.getConstraint() != null) {
				model.addConstraint(lc.getConstraint());
			}
		}

		// Add independent parameter
		Parameter indepParam = new Parameter(Categories.getTime());
		indepParam.setValue(0.0);
		indepParam.setConstant(false);
		indepParam.setUnits(indep.getUnit());
		model.addParameter(indepParam);

		// Add constant parameters
		LinkedList<ParamXml> constXmls = new LinkedList<>();
		for (PmmXmlElementConvertable pmmParam : firstTuple.getPmmXml(
				Model1Schema.ATT_PARAMETER).getElementSet()) {
			constXmls.add((ParamXml) pmmParam);
		}

		for (ParamXml constXml : constXmls) {
			// Add constant parameter
			Parameter param = new Coefficient(constXml).getParameter();
			model.addParameter(param);

			// Add constraint
			LimitsConstraint lc = new LimitsConstraint(constXml.getName(),
					constXml.getMin(), constXml.getMax());
			if (lc.getConstraint() != null) {
				model.addConstraint(lc.getConstraint());
			}
		}

		// Add unit definitions
		LinkedList<IndepXml> indepXmls = new LinkedList<>(Arrays.asList(indep));
		addUnitDefinitions(model, depXml, indepXmls, constXmls);

		// Create rule of the model and add it to the rest of rules
		Model1Rule model1Rule = Model1Rule.convertCatalogModelXmlToModel1Rule(
				modelXml, organism.getSpecies().getId());
		model.addRule(model1Rule.getRule());

		// Add submodels and model definitions
		int i = 0;
		for (KnimeTuple tuple : tuples) {
			CatalogModelXml secModelXml = (CatalogModelXml) tuple.getPmmXml(
					Model2Schema.ATT_MODELCATALOG).get(0);
			DepXml secDepXml = (DepXml) tuple.getPmmXml(
					Model2Schema.ATT_DEPENDENT).get(0);

			// Get independent parameters
			LinkedList<IndepXml> secIndepXmls = new LinkedList<>();
			for (PmmXmlElementConvertable xmlItem : tuple.getPmmXml(
					Model2Schema.ATT_INDEPENDENT).getElementSet()) {
				secIndepXmls.add((IndepXml) xmlItem);
			}

			// Get constant parameters
			LinkedList<ParamXml> secConstXmls = new LinkedList<>();
			for (PmmXmlElementConvertable xmlItem : tuple.getPmmXml(
					Model2Schema.ATT_PARAMETER).getElementSet()) {
				secConstXmls.add((ParamXml) xmlItem);
			}

			String modelDefinitionId = "model_" + secDepXml.getName();
			ModelDefinition secModel = new ModelDefinition(modelDefinitionId,
					LEVEL, VERSION);
			secModel.setName(secModelXml.getName());

			// Add unit definitions
			addUnitDefinitions(secModel, secDepXml, secIndepXmls, secConstXmls);

			// Add dep from sec
			Parameter secDep = new Parameter(secDepXml.getName());
			secDep.setConstant(false);
			secDep.setValue(0.0);
			secModel.addParameter(secDep);

			for (IndepXml indepXml : secIndepXmls) {
				// Create SBML parameter
				Parameter param = new SecIndep(indepXml).getParam();

				// Assign unit
				if (indepXml.getUnit() == null) {
					param.setUnits("dimensionless");
				} else {
					param.setUnits(Util.createId(indepXml.getUnit()));
				}

				// Add independent parameter to model
				secModel.addParameter(param);

				// Add constraint
				LimitsConstraint lc = new LimitsConstraint(indepXml.getName(),
						indepXml.getMin(), indepXml.getMax());
				if (lc.getConstraint() != null) {
					secModel.addConstraint(lc.getConstraint());
				}
			}

			for (ParamXml constXml : secConstXmls) {
				// Create SBML parameter
				Parameter constParam = new Coefficient(constXml).getParameter();

				// Assign unit
				if (constXml.getUnit() == null) {
					constParam.setUnits("dimensionless");
				} else {
					constParam.setUnits(Util.createId(constXml.getUnit()));
				}

				// Add constant parameter
				secModel.addParameter(constParam);

				// Add constraint
				LimitsConstraint lc = new LimitsConstraint(constXml.getName(),
						constXml.getMin(), constXml.getMax());
				if (lc.getConstraint() != null) {
					secModel.addConstraint(lc.getConstraint());
				}
			}

			// Get literature references
			litItems = tuple.getPmmXml(Model2Schema.ATT_EMLIT).getElementSet();
			lits = new LinkedList<>();
			for (PmmXmlElementConvertable item : litItems) {
				LiteratureItem lit = (LiteratureItem) item;
				lits.add(lit);
			}

			// Add sec literature references
			int globalModelID = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
			Map<String, String> uncertainties = parseQualityTags(estXml);
			Model2Annotation secModelAnnotation = new Model2Annotation(
					globalModelID, uncertainties, lits);
			secModel.getAnnotation().setNonRDFAnnotation(
					secModelAnnotation.getNode());

			Model2Rule rule2 = Model2Rule
					.convertCatalogModelXmlToModel2Rule(secModelXml);
			secModel.addRule(rule2.getRule());

			compDocPlugin.addModelDefinition(secModel);

			Submodel submodel = compModelPlugin.createSubmodel("submodel"
					+ Integer.toString(i));
			submodel.setModelRef(modelDefinitionId);

			i++;
		}
		return doc;
	}
}