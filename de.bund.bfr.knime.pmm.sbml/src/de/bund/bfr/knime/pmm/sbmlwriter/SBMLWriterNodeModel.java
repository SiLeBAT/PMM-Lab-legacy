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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompModelPlugin;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.ext.comp.Submodel;
import org.sbml.jsbml.xml.XMLNamespaces;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.annotation.CreatedNode;
import de.bund.bfr.knime.pmm.annotation.CreatorNode;
import de.bund.bfr.knime.pmm.annotation.DataSourceNode;
import de.bund.bfr.knime.pmm.annotation.ModelClassNode;
import de.bund.bfr.knime.pmm.annotation.ModelIdNode;
import de.bund.bfr.knime.pmm.annotation.ModelTitleNode;
import de.bund.bfr.knime.pmm.annotation.ModifiedNode;
import de.bund.bfr.knime.pmm.annotation.ReferenceNode;
import de.bund.bfr.knime.pmm.annotation.UncertaintyNode;
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
import de.bund.bfr.knime.pmm.sbmlutil.DataFile;
import de.bund.bfr.knime.pmm.sbmlutil.LimitsConstraint;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;
import de.bund.bfr.knime.pmm.sbmlutil.Model1Rule;
import de.bund.bfr.knime.pmm.sbmlutil.Model2Rule;
import de.bund.bfr.knime.pmm.sbmlutil.Agent;
import de.bund.bfr.knime.pmm.sbmlutil.PrimCoefficient;
import de.bund.bfr.knime.pmm.sbmlutil.SecCoefficient;
import de.bund.bfr.knime.pmm.sbmlutil.UnitDefinitionWrapper;
import de.bund.bfr.knime.pmm.sbmlutil.Util;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.NuMLWriter;
import de.unirostock.sems.cbarchive.CombineArchive;

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

	public enum ModelType {
		PRIMARY, TERCIARY
	};

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
		} else if (modelType == ModelType.TERCIARY) {
			TertiaryTableReader reader = new TertiaryTableReader(tuples,
					dlgInfo);
			experiments = reader.getExperiments();
		}

		String caName = String.format("%s/%s.pmf", outPath.getStringValue(),
				modelName.getStringValue());

		// Remove previous Combine archive if it exists
		File fileTemp = new File(caName);
		if (fileTemp.exists()) {
			fileTemp.delete();
		}

		CombineArchive ca = new CombineArchive(new File(caName));

		SBMLWriter sbmlWriter = new SBMLWriter();
		sbmlWriter.setProgramName("SBML Writer node");
		sbmlWriter.setProgramVersion("1.0");

		NuMLWriter numlWriter = new NuMLWriter();

		URI sbmlURI = new URI(
				"http://identifiers.org/combine.specifications/sbml");
		URI numlURI = new URI(
				"http://numl.googlecode.com/svn/trunk/NUMLSchema.xsd");

		short counter = 0;
		for (Experiment exp : experiments) {
			// Create temp file
			File sbmlTemp = File.createTempFile("temp1", "");
			sbmlTemp.deleteOnExit();

			String mdName = String.format("%s_%d.sbml",
					modelName.getStringValue(), counter);

			// Add data set
			if (exp.getData() != null) {
				File numlTemp = File.createTempFile("temp2", "");
				numlTemp.deleteOnExit();
				String dataName = String.format("%s_%d.numl",
						modelName.getStringValue(), counter);
				numlWriter.write(exp.getData(), numlTemp);
				ca.addEntry(numlTemp, dataName, numlURI);

				DataSourceNode node = new DataSourceNode(dataName);
				exp.getModel().getModel().getAnnotation().getNonRDFannotation()
						.addChild(node.getNode());

				counter++; // Increment counter
				// update progress bar
				exec.setProgress((float) counter / experiments.size());
			}

			// Add model
			sbmlWriter.write(exp.getModel(), sbmlTemp);
			ca.addEntry(sbmlTemp, mdName, sbmlURI);
		}

		ca.pack();
		ca.close();

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
		// Tertiary model (primary+secondary)
		if (SchemaFactory.createM12DataSchema().conforms(
				(DataTableSpec) inSpecs[0])) {
			schema = SchemaFactory.createM12DataSchema();
			modelType = ModelType.TERCIARY;
		} else if (SchemaFactory.createM1DataSchema().conforms(
				(DataTableSpec) inSpecs[0])) {
			schema = SchemaFactory.createM1DataSchema();
			modelType = ModelType.PRIMARY;
		} else if (outPath.getStringValue() == null
				|| modelName.getStringValue() == null
				|| variableParams.getStringValue() == null) {
			throw new InvalidSettingsException("Node must be configured");
		} else {
			throw new InvalidSettingsException("Invalid Input");
		}

		return new DataTableSpec[] {};
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

// Holder class for related models and datasets
class Experiment {
	private SBMLDocument model;
	private NuMLDocument data;

	public Experiment(SBMLDocument model) {
		this.model = model;
	}

	public Experiment(SBMLDocument model, NuMLDocument data) {
		this.model = model;
		this.data = data;
	}

	public SBMLDocument getModel() {
		return model;
	}

	public void setModel(SBMLDocument model) {
		this.model = model;
	}

	public NuMLDocument getData() {
		return data;
	}

	public void setData(NuMLDocument data) {
		this.data = data;
	}
}

abstract class TableReader {
	protected final static int LEVEL = 3;
	protected final static int VERSION = 1;

	protected List<Experiment> experiments = new LinkedList<>();

	public List<Experiment> getExperiments() {
		return experiments;
	}

	/*
	 * Get units from the parameters (dep, indep and consts), get their data
	 * from DB and return them.
	 */
	public ListOf<UnitDefinition> getUnits(DepXml dep, IndepXml indep,
			List<PmmXmlElementConvertable> constParams) {

		// get unit names
		HashSet<String> units = new HashSet<>();
		if (dep.getUnit() != null)
			units.add(dep.getUnit());
		if (indep.getUnit() != null)
			units.add(indep.getUnit());
		for (PmmXmlElementConvertable pmmXmlElement : constParams) {
			ParamXml param = (ParamXml) pmmXmlElement;
			if (param.getUnit() != null) {
				units.add(param.getUnit());
			}
		}

		// Get units from DB
		UnitsFromDB unitDB = new UnitsFromDB();
		unitDB.askDB();
		Map<Integer, UnitsFromDB> origMap = unitDB.getMap();

		// Create new map with unit display as keys
		Map<String, UnitsFromDB> map = new HashMap<>();
		for (Entry<Integer, UnitsFromDB> entry : origMap.entrySet()) {
			map.put(entry.getValue().getDisplay_in_GUI_as(), entry.getValue());
		}

		ListOf<UnitDefinition> unitDefs = new ListOf<>(3, 1);
		for (String unit : units) {
			UnitsFromDB dbUnit = map.get(unit);
			if (dbUnit != null) {
				UnitDefinitionWrapper wrapper = UnitDefinitionWrapper
						.xmlToUnitDefinition(dbUnit.getMathML_string());
				UnitDefinition ud = wrapper.getUnitDefinition();
				ud.setId(createId(unit));
				ud.setName(unit);
				unitDefs.add(ud);
			}
		}

		return unitDefs;
	}

	protected static void renameLog(KnimeTuple tuple) {
		PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
		CatalogModelXml model = (CatalogModelXml) modelXml.get(0);

		model.setFormula(MathUtilities.replaceVariable(model.getFormula(),
				"log", "log10"));
		tuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
	}

	protected static String createId(String s) {
		return s.replaceAll("\\W+", " ").trim().replace(" ", "_");
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

	protected Parameter createIndep() {
		Parameter param = new Parameter(Categories.getTime());
		param.setValue(0.0);
		param.setConstant(false);
		param.setUnits(Categories.getTimeCategory().getStandardUnit());
		return param;
	}

	protected List<Parameter> createConstantParameters(
			final List<PmmXmlElementConvertable> params) {
		List<Parameter> coefficients = new LinkedList<>();
		for (PmmXmlElementConvertable pmmParam : params) {
			ParamXml paramXml = (ParamXml) pmmParam;
			coefficients.add(new PrimCoefficient(paramXml).getParameter());
		}
		return coefficients;
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

	protected Annotation createModelAnnotation(String modelId,
			String modelTitle, Map<String, String> uncertainties,
			List<LiteratureItem> lits) {
		Annotation annot = new Annotation();

		// pmf container
		XMLTriple pmfTriple = new XMLTriple("metadata", null, "pmf");
		XMLNode pmfNode = new XMLNode(pmfTriple, null, null);

		// add model id annotation
		if (modelId != null) {
			ModelIdNode modelIdNode = new ModelIdNode(modelId);
			pmfNode.addChild(modelIdNode.getNode());
		}

		// add model title annotation
		if (modelTitle != null) {
			ModelTitleNode modelTitleNode = new ModelTitleNode(modelTitle);
			pmfNode.addChild(modelTitleNode.getNode());
		}

		// add model quality annotation
		if (!uncertainties.isEmpty()) {
			UncertaintyNode uncertaintiesNode = new UncertaintyNode(
					uncertainties);
			pmfNode.addChild(uncertaintiesNode.getNode());
		}

		// add reference
		for (LiteratureItem lit : lits) {
			ReferenceNode ref = new ReferenceNode(lit);
			pmfNode.addChild(ref.getNode());
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
		String modelId = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);

		SBMLDocument doc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Composition package
		doc.enablePackage(CompConstants.shortLabel);

		// Add namespaces
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

		// Document annotation
		Annotation docAnnot = createDocAnnotation(docInfo);
		doc.setAnnotation(docAnnot);

		Model model = doc.createModel(modelId);
		model.setName(modelXml.getName());

		// Annotation
		String modelTitle = modelXml.getName();
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

		Annotation annot = createModelAnnotation(modelId, modelTitle,
				qualityTags, lits);
		model.setAnnotation(annot);

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

		String depName = depXml.getOrigName();
		String depUnit = depXml.getUnit();

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
		model.addParameter(createIndep());

		// Parse constant parameters
		List<PmmXmlElementConvertable> constParams = tuple.getPmmXml(
				Model1Schema.ATT_PARAMETER).getElementSet();

		// Add constraints
		for (PmmXmlElementConvertable item : constParams) {
			ParamXml param = (ParamXml) item;
			String name = param.getName();
			if (!name.isEmpty()) {
				Double min = param.getMin();
				Double max = param.getMax();
				LimitsConstraint lc = new LimitsConstraint(name, min, max);
				if (lc.getConstraint() != null) {
					model.addConstraint(lc.getConstraint());
				}
			}
		}
		// Add constant parameters
		for (Parameter param : createConstantParameters(constParams)) {
			model.addParameter(param);
		}

		ListOf<UnitDefinition> unitDefs = getUnits(depXml, indep, constParams);
		model.setListOfUnitDefinitions(unitDefs);

		// Create rule of the model and add it to the rest of rules
		Model1Rule model1Rule = Model1Rule.convertCatalogModelXmlToModel1Rule(
				modelXml, organims.getSpecies().getId());
		model.addRule(model1Rule.getRule());
		return doc;
	}
}

class TertiaryTableReader extends TableReader {

	public TertiaryTableReader(List<KnimeTuple> tuples,
			Map<String, String> dlgInfo) throws URISyntaxException {
		super();

		dlgInfo.put("type", "Tertiary");

		HashMap<String, List<KnimeTuple>> tuplesMap = new HashMap<>();
		for (KnimeTuple tuple : tuples) {
			String id = tuple.getString(Model2Schema.ATT_GLOBAL_MODEL_ID);
			if (tuplesMap.containsKey(id)) {
				tuplesMap.get(id).add(tuple);
			} else {
				List<KnimeTuple> newModel = new ArrayList<>();
				newModel.add(tuple);
				tuplesMap.put(id, newModel);
			}
		}

		for (List<KnimeTuple> modelTuples : tuplesMap.values()) {
			SBMLDocument model = parseTertiaryTuple(modelTuples, dlgInfo);

			// Add NuML doc
			KnimeTuple tuple = modelTuples.get(0);

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

				// * Get and add data set
				NuMLDocument data = dataFile.getDocument();

				experiments.add(new Experiment(model, data));
			}
		}
	}

	private Annotation createSecAnnotation(List<LiteratureItem> lits) {
		Annotation annot = new Annotation();

		// pmf container
		XMLTriple pmfTriple = new XMLTriple("metadata", null, "pmf");
		XMLNamespaces pmfNS = new XMLNamespaces();
		pmfNS.add("http://purl.org/dc/terms/", "dcterms");
		XMLNode pmfNode = new XMLNode(pmfTriple, null, pmfNS);

		// add reference
		for (LiteratureItem lit : lits) {
			ReferenceNode ref = new ReferenceNode(lit);
			pmfNode.addChild(ref.getNode());
		}

		// add non-rdf annotation
		annot.setNonRDFAnnotation(pmfNode);
		annot.addDeclaredNamespace("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");

		return annot;
	}

	private List<Parameter> createConstsSec(
			final List<PmmXmlElementConvertable> params) {
		List<Parameter> consts = new LinkedList<>();
		for (PmmXmlElementConvertable pmmParam : params) {
			ParamXml xml = (ParamXml) pmmParam;
			consts.add(new SecCoefficient(xml).getParameter());
		}

		return consts;
	}

	private List<Parameter> createSecIndeps(
			final List<PmmXmlElementConvertable> params) {
		List<Parameter> indeps = new LinkedList<>();
		for (PmmXmlElementConvertable pmmParam : params) {
			IndepXml xml = (IndepXml) pmmParam;
			Parameter p = new Parameter(xml.getName());
			p.setConstant(false);
			indeps.add(p);
		}
		return indeps;
	}

	public ListOf<UnitDefinition> getUnits(DepXml dep,
			List<PmmXmlElementConvertable> constParams) {

		// get unit names
		HashSet<String> units = new HashSet<>();
		if (dep.getUnit() != null)
			units.add(dep.getUnit());
		for (PmmXmlElementConvertable pmmXmlElement : constParams) {
			ParamXml param = (ParamXml) pmmXmlElement;
			if (param.getUnit() != null) {
				units.add(param.getUnit());
			}
		}

		// Get units from DB
		UnitsFromDB unitDB = new UnitsFromDB();
		unitDB.askDB();
		Map<Integer, UnitsFromDB> origMap = unitDB.getMap();

		// Create new map with unit display as keys
		Map<String, UnitsFromDB> map = new HashMap<>();
		for (Entry<Integer, UnitsFromDB> entry : origMap.entrySet()) {
			map.put(entry.getValue().getDisplay_in_GUI_as(), entry.getValue());
		}

		ListOf<UnitDefinition> unitDefs = new ListOf<>(3, 1);
		for (String unit : units) {
			UnitsFromDB dbUnit = map.get(unit);
			if (dbUnit != null) {
				UnitDefinitionWrapper wrapper = UnitDefinitionWrapper
						.xmlToUnitDefinition(dbUnit.getMathML_string());
				UnitDefinition ud = wrapper.getUnitDefinition();
				ud.setId(createId(unit));
				ud.setName(unit);
				unitDefs.add(ud);
			}
		}

		return unitDefs;
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
		String modelId = firstTuple.getString(TimeSeriesSchema.ATT_COMBASEID);

		SBMLDocument doc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Composition package
		doc.enablePackage(CompConstants.shortLabel);
		CompSBMLDocumentPlugin compDocPlugin = (CompSBMLDocumentPlugin) doc
				.getPlugin(CompConstants.shortLabel);

		// Document annotation
		Annotation docAnnot = createDocAnnotation(docInfo);
		doc.setAnnotation(docAnnot);

		// Add namespaces
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

		Model model = doc.createModel(modelId);
		model.setName(modelXml.getName());
		CompModelPlugin compModelPlugin = (CompModelPlugin) model
				.getPlugin(CompConstants.shortLabel);

		// Annotation
		String modelTitle = modelXml.getName();
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
		Annotation annot = createModelAnnotation(modelId, modelTitle,
				qualityTags, lits);
		model.setAnnotation(annot);

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

		String depName = depXml.getOrigName();
		String depUnit = depXml.getUnit();

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
		model.addParameter(createIndep());

		// Parse constant parameters
		List<PmmXmlElementConvertable> constParams = firstTuple.getPmmXml(
				Model1Schema.ATT_PARAMETER).getElementSet();

		// Add constraints
		for (PmmXmlElementConvertable item : constParams) {
			ParamXml param = (ParamXml) item;
			String name = param.getName();
			if (!name.isEmpty()) {
				Double min = param.getMin();
				Double max = param.getMax();
				LimitsConstraint lc = new LimitsConstraint(name, min, max);
				if (lc.getConstraint() != null) {
					model.addConstraint(lc.getConstraint());
				}
			}
		}
		// Add constant params
		for (Parameter param : createConstantParameters(constParams)) {
			model.addParameter(param);
		}

		// Add units
		ListOf<UnitDefinition> unitDefs = getUnits(depXml, indep, constParams);
		model.setListOfUnitDefinitions(unitDefs);

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
			List<PmmXmlElementConvertable> secIndepParams = tuple.getPmmXml(
					Model2Schema.ATT_INDEPENDENT).getElementSet();
			List<PmmXmlElementConvertable> secConstParams = tuple.getPmmXml(
					Model2Schema.ATT_PARAMETER).getElementSet();

			String modelDefinitionId = "model_" + secDepXml.getName();
			ModelDefinition modelDefinition = new ModelDefinition(
					modelDefinitionId, LEVEL, VERSION);
			modelDefinition.setName(secModelXml.getName());

			// Add units
			unitDefs = getUnits(secDepXml, secConstParams);
			modelDefinition.setListOfUnitDefinitions(unitDefs);

			// Add dep from sec
			Parameter secDep = new Parameter(secDepXml.getName());
			secDep.setConstant(false);
			secDep.setValue(0.0);
			secDep.setUnits(secDepXml.getOrigName());
			modelDefinition.addParameter(secDep);

			// Add independent parameters
			List<Parameter> secIndeps = createSecIndeps(secIndepParams);
			for (Parameter param : secIndeps) {
				modelDefinition.addParameter(param);
			}

			// Add constraints of independent parameters
			for (PmmXmlElementConvertable item : secIndepParams) {
				IndepXml xml = (IndepXml) item;
				Double min = xml.getMin(), max = xml.getMax();
				LimitsConstraint lc = new LimitsConstraint(xml.getName(), min,
						max);
				if (lc.getConstraint() != null) {
					modelDefinition.addConstraint(lc.getConstraint());
				}
			}

			// Add constant parameters
			List<Parameter> secConsts = createConstsSec(secConstParams);
			for (Parameter param : secConsts) {
				modelDefinition.addParameter(param);
			}

			// Add constraint of constant parameters
			for (PmmXmlElementConvertable item : secConstParams) {
				ParamXml xml = (ParamXml) item;
				Double min = xml.getMin(), max = xml.getMax();
				LimitsConstraint lc = new LimitsConstraint(xml.getName(), min,
						max);
				if (lc.getConstraint() != null) {
					modelDefinition.addConstraint(lc.getConstraint());
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
			modelDefinition.setAnnotation(createSecAnnotation(lits));

			Model2Rule rule2 = Model2Rule
					.convertCatalogModelXmlToModel2Rule(secModelXml);
			modelDefinition.addRule(rule2.getRule());

			compDocPlugin.addModelDefinition(modelDefinition);

			Submodel submodel = compModelPlugin.createSubmodel("submodel"
					+ Integer.toString(i));
			submodel.setModelRef(modelDefinitionId);

			i++;
		}
		return doc;
	}
}