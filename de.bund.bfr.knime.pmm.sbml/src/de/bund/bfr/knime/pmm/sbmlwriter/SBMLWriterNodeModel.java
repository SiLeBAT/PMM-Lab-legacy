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
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompModelPlugin;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.ext.comp.Submodel;
import org.sbml.jsbml.text.parser.FormulaParser;
import org.sbml.jsbml.text.parser.ParseException;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNamespaces;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
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
import de.bund.bfr.knime.pmm.sbmlutil.Model1Rule;
import de.bund.bfr.knime.pmm.sbmlutil.Model2Rule;
import de.bund.bfr.knime.pmm.sbmlutil.SBMLUtil;

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
		List<SBMLDocument> documents = null;

		if (modelType == ModelType.PRIMARY) {
			PrimaryTableReader reader = new PrimaryTableReader(tuples);
			documents = reader.getDocuments();
		} else if (modelType == ModelType.TERCIARY) {
			TertiaryTableReader reader = new TertiaryTableReader(tuples);
			documents = reader.getDocuments();
		}

		for (int i = 0; i < documents.size(); i++) {
			String fileName = String.format("%s/%s_%d.sbml.xml",
					outPath.getStringValue(), modelName.getStringValue(), i);
			File file = new File(fileName);
			SBMLWriter.write(documents.get(i), file, "SBMLWriter Node", "1.0");
		}

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
		// Terciary model (primary+secondary)
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

abstract class TableReader {
	protected final static int LEVEL = 3;
	protected final static int VERSION = 1;

	protected List<SBMLDocument> documents = new ArrayList<>();

	public List<SBMLDocument> getDocuments() {
		return documents;
	}

	/*
	 * Get units from the parameters (dep, indep and consts), get their data
	 * from DB and add them to the model.
	 */
	public ListOf<UnitDefinition> getUnits(DepXml depXml,
			List<PmmXmlElementConvertable> indepParams,
			List<PmmXmlElementConvertable> constParams) {
		// Get unit names
		HashSet<String> unitNames = new HashSet<>();
		unitNames.add(depXml.getUnit());
		for (PmmXmlElementConvertable pmmXmlElem : indepParams) {
			IndepXml indep = (IndepXml) pmmXmlElem;
			unitNames.add(indep.getUnit());
		}
		for (PmmXmlElementConvertable pmmXmlElem : constParams) {
			ParamXml constant = (ParamXml) pmmXmlElem;
			String constantUnit = constant.getUnit();
			if (constantUnit != null) {
				unitNames.add(constant.getUnit());
			}
		}

		// Get units from DB
		UnitsFromDB unitDB = new UnitsFromDB();
		unitDB.askDB();
		Map<Integer, UnitsFromDB> origMap = unitDB.getMap();

		// Create new map with unit names as keys
		Map<String, UnitsFromDB> map = new HashMap<>();
		for (Entry<Integer, UnitsFromDB> entry : origMap.entrySet()) {
			map.put(entry.getValue().getUnit(), entry.getValue());
		}

		ListOf<UnitDefinition> unitDefinitions = new ListOf<>();
		for (String unitName : unitNames) {
			UnitsFromDB unit = map.get(unitName);
			if (unit != null) {
				UnitDefinition unitDef = SBMLUtil.fromXml(unit
						.getMathML_string());
				unitDefinitions.add(unitDef);
			}
		}

		return unitDefinitions;
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

	// Fix independent parameter units
	void fixIndepUnits(List<PmmXmlElementConvertable> params) {
		for (PmmXmlElementConvertable item : params) {
			IndepXml constant = (IndepXml) item;
			String unit = constant.getUnit();
			if (unit == null) {
				constant.setUnit("dimensionless");
			} else if (unit.equals("°C")) {
				constant.setUnit("pmf_celsius");
			}
		}
	}

	// fix constant parameter units
	void fixConstUnits(List<PmmXmlElementConvertable> params) {
		for (PmmXmlElementConvertable item : params) {
			ParamXml constant = (ParamXml) item;
			String unit = constant.getUnit();
			if (unit == null) {
				constant.setUnit("dimensionless");
			} else if (unit.equals("°C")) {
				constant.setUnit("pmf_celsius");
			}
		}
	}

	/**
	 * Create a compartment with the name given. This compartment is not added
	 * to the model.
	 * 
	 * @param name
	 *            : Name of the compartment. If the name is null then the will
	 *            be assigned COMPARTMENT_MISSING.
	 * 
	 * @return comparment.
	 */
	protected Compartment createCompartment(final String name) {
		final String COMPARTMENT_MISSING = "CompartmentMissing";
		String compartmentName;
		String compartmentId;

		if (name == null) {
			compartmentId = COMPARTMENT_MISSING;
			compartmentName = COMPARTMENT_MISSING;
		} else {
			compartmentId = createId(name);
			compartmentName = name;
		}

		Compartment compartment = new Compartment(compartmentId);
		compartment.setName(compartmentName);
		return compartment;
	}

	/**
	 * Create a species element with a name and add it to the compartment
	 * passed. If the name is null then the species will be assigned
	 * SPECIES_MISSING. This species element will not be assigned to the model.
	 * 
	 * @param: name
	 * @param: comparment
	 * 
	 * @return: species
	 */
	protected Species createSpecies(final String name,
			final Compartment compartment) {
		final String SPECIES_MISSING = "SpeciesMissing";
		String speciesId;
		String speciesName;

		if (name == null) {
			speciesId = SPECIES_MISSING;
			speciesName = SPECIES_MISSING;
		} else {
			speciesId = createId(name);
			speciesName = name;
		}

		Species species = new Species(speciesId);
		species.setName(speciesName);
		species.setCompartment(compartment);

		return species;
	}

	protected List<Parameter> createIndependentParameter(
			final List<PmmXmlElementConvertable> params) {
		List<Parameter> indeps = new ArrayList<>();
		for (PmmXmlElementConvertable pmmParam : params) {
			IndepXml indepXml = (IndepXml) pmmParam;

			String name = indepXml.getName();
			String unit = indepXml.getUnit();

			Parameter p = new Parameter(name);
			p.setValue(0.0);
			p.setConstant(false);
			p.setUnits(unit);
			indeps.add(p);
		}
		return indeps;
	}

	protected List<Parameter> createConstantParameters(
			final List<PmmXmlElementConvertable> params) {
		List<Parameter> consts = new ArrayList<>();
		for (PmmXmlElementConvertable pmmParam : params) {
			ParamXml paramXml = (ParamXml) pmmParam;

			String name = paramXml.getName();
			String unit = paramXml.getUnit();

			Double value = paramXml.getValue();
			if (value == null) {
				value = 0.0;
			}

			Parameter p = new Parameter(name);
			p.setValue(value);
			p.setConstant(true);
			p.setUnits(unit);
			consts.add(p);
		}
		return consts;
	}

	protected Annotation createAnnotation(String modelId, String modelTitle,
			String modelClass, Map<String, String> pmfTags) {
		Annotation annot = new Annotation();

		// pmf container
		XMLTriple pmfTriple = new XMLTriple("metadata", null, "pmf");
		XMLNamespaces pmfNS = new XMLNamespaces();
		pmfNS.add("http://purl.org/dc/terms/", "dc");
		pmfNS.add("http://purl.org/dc/terms/", "dcterms");
		XMLNode pmfNode = new XMLNode(pmfTriple, null, pmfNS);

		// model id annotation
		if (modelId != null) {
			XMLTriple modelIDTriple = new XMLTriple("identifer", "", "dc");
			XMLNode modelIDNode = new XMLNode(modelIDTriple);
			modelIDNode.addChild(new XMLNode(modelId));
			pmfNode.addChild(modelIDNode);
		}

		// model title annotation
		if (modelTitle != null) {
			XMLTriple modelTitleTriple = new XMLTriple("title", null, "dc");
			XMLNode modelTitleNode = new XMLNode(modelTitleTriple);
			modelTitleNode.addChild(new XMLNode(modelTitle));
			pmfNode.addChild(modelTitleNode);
		}

		// model class annotation
		if (modelClass != null) {
			XMLTriple modelClassTriple = new XMLTriple("type", null, "dc");
			XMLNode modelClassNode = new XMLNode(modelClassTriple);
			modelClassNode.addChild(new XMLNode(modelClass.toString()));
			pmfNode.addChild(modelClassNode);
		}

		// model quality annotation
		if (!pmfTags.isEmpty()) {
			XMLTriple modelQualityTriple = new XMLTriple("modelquality", null,
					"pmml");
			XMLAttributes qualityAttrs = new XMLAttributes();
			for (Map.Entry<String, String> entry : pmfTags.entrySet()) {
				qualityAttrs.add(entry.getKey(), entry.getValue());
			}
			XMLNode modelQualityNode = new XMLNode(modelQualityTriple,
					qualityAttrs);
			pmfNode.addChild(modelQualityNode);
		}

		// add non-rdf annotation
		annot.setNonRDFAnnotation(pmfNode);
		annot.addDeclaredNamespace("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");

		return annot;
	}

	static Constraint createConstraint(String var, Double min, Double max) {
		String formula = "";

		if (min != null)
			formula += String.format(Locale.ENGLISH, "(%s >= %f)", var, min.doubleValue());
		if (max != null) {
			if (min != null)
				formula += "&&";
			formula += String.format(Locale.ENGLISH, "(%s <= %f)", var, max.doubleValue());
		}
		
		if (formula.isEmpty()) {
			return null;
		}

		ASTNode math = null;
		try {
			math = ASTNode.parseFormula(formula);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Constraint constraint = new Constraint(math, LEVEL, VERSION);
		return constraint;
	}

	/**
	 * Parse model quality tags such as SSE and RMS from the EstModelXml cell.
	 * 
	 * @param estModel
	 * @return
	 */
	static Map<String, String> parseQualityTags(EstModelXml estModel) {
		Map<String, String> qualityTags = new HashMap<>();

		String targetField = estModel.getName();
		if (targetField != null) {
			qualityTags.put("targetField", targetField);
		}

		String dataUsage = estModel.getComment();
		if (dataUsage != null) {
			qualityTags.put("dataUsage", dataUsage);
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

		Integer dof = estModel.getDof();
		if (dof != null) {
			qualityTags.put("degreesOfFreedom", dof.toString());
		}

		return qualityTags;
	}
}

class PrimaryTableReader extends TableReader {

	public PrimaryTableReader(List<KnimeTuple> tuples) {
		super();

		// filter tuples with duplicated ids (from ExtXML)
		Set<String> idSet = new HashSet<>();
		for (Iterator<KnimeTuple> iter = tuples.listIterator(); iter.hasNext();) {
			iter.hasNext();
			KnimeTuple tuple = iter.next();
			String id = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
			if (!idSet.add(id)) {
				iter.remove();
			}
		}

		for (KnimeTuple tuple : tuples) {
			SBMLDocument doc = parsePrimaryTuple(tuple);
			documents.add(doc);
		}
	}

	private SBMLDocument parsePrimaryTuple(KnimeTuple tuple) {
		replaceCelsiusAndFahrenheit(tuple);
		renameLog(tuple);

		// retrieve XML cells
		CatalogModelXml modelXml = (CatalogModelXml) tuple.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estXml = (EstModelXml) tuple.getPmmXml(
				Model1Schema.ATT_ESTMODEL).get(0);
		DepXml depXml = (DepXml) tuple.getPmmXml(Model1Schema.ATT_DEPENDENT)
				.get(0);
		AgentXml organismXml = (AgentXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_AGENT).get(0);
		MatrixXml matrixXml = (MatrixXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);
		LiteratureItem literatureXml = (LiteratureItem) tuple.getPmmXml(
				Model1Schema.ATT_MLIT).get(0);
		String modelId = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);

		SBMLDocument doc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Composition package
		doc.enablePackage(CompConstants.shortLabel);

		Model model = doc.createModel(modelId);

		// Annotation
		String modelTitle = modelXml.getName();
		int modelClassNum = modelXml.getModelClass();
		String modelClass = SBMLUtil.INT_TO_CLASS.get(modelClassNum);
		Map<String, String> qualityTags = parseQualityTags(estXml);

		Annotation annot = createAnnotation(modelId, modelTitle, modelClass,
				qualityTags);
		model.setAnnotation(annot);

		// Create compartment and add it to the model
		Compartment c = createCompartment(matrixXml.getName());
		model.addCompartment(c);

		// Create species and add it to the model
		Species specie = createSpecies(organismXml.getName(), c);
		model.addSpecies(specie);

		ListOf<Rule> rules = new ListOf<>(LEVEL, VERSION);

		String depName = depXml.getOrigName();
		String depUnit = depXml.getUnit();

		// Parse independent parameters
		List<PmmXmlElementConvertable> indepParams = tuple.getPmmXml(
				Model1Schema.ATT_INDEPENDENT).getElementSet();

		// Add constraints
		for (PmmXmlElementConvertable item : indepParams) {
			IndepXml indep = (IndepXml) item;
			Constraint constraint = createConstraint(indep.getName(),
					indep.getMin(), indep.getMax());
			if (constraint != null) {
				model.addConstraint(constraint);
			}
		}

		// Add independent parameters
		List<Parameter> indeps = createIndependentParameter(indepParams);
		for (Parameter param : indeps) {
			model.addParameter(param);
		}

		// Parse constant parameters
		List<PmmXmlElementConvertable> constParams = tuple.getPmmXml(
				Model1Schema.ATT_PARAMETER).getElementSet();
		// Add constraints
		for (PmmXmlElementConvertable item : constParams) {
			ParamXml param = (ParamXml) item;
			Constraint constraint = createConstraint(param.getName(),
					param.getMin(), param.getMax());
			if (constraint != null) {
				model.addConstraint(constraint);
			}
		}
		// Add constant parameters
		List<Parameter> consts = createConstantParameters(constParams);
		for (Parameter param : consts) {
			model.addParameter(param);
		}

		ListOf<UnitDefinition> unitDefs = getUnits(depXml, indepParams,
				constParams);
		model.setListOfUnitDefinitions(unitDefs);

		// Create rule of the model and add it to the rest of rules
		String modelFormula = modelXml.getFormula();
		Model1Rule model1Rule = new Model1Rule();
		model1Rule.parse(modelFormula);
		rules.add(model1Rule.getRule());
		model.setListOfRules(rules);

		return doc;
	}
}

class TertiaryTableReader extends TableReader {

	public TertiaryTableReader(List<KnimeTuple> tuples) {
		super();

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
			SBMLDocument doc = parseTertiaryTuple(modelTuples);
			documents.add(doc);
		}
	}

	private SBMLDocument parseTertiaryTuple(List<KnimeTuple> tuples) {
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
		AgentXml organismXml = (AgentXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_AGENT).get(0);
		MatrixXml matrixXml = (MatrixXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);
		LiteratureItem literatureXml = (LiteratureItem) firstTuple.getPmmXml(
				Model1Schema.ATT_MLIT).get(0);
		String modelId = firstTuple.getString(TimeSeriesSchema.ATT_COMBASEID);

		SBMLDocument doc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Composition package
		doc.enablePackage(CompConstants.shortLabel);
		CompSBMLDocumentPlugin compDocPlugin = (CompSBMLDocumentPlugin) doc
				.getPlugin(CompConstants.shortLabel);

		Model model = doc.createModel(modelId);
		CompModelPlugin compModelPlugin = (CompModelPlugin) model
				.getPlugin(CompConstants.shortLabel);

		// Annotation
		String modelTitle = modelXml.getName();
		Integer modelClassNum = modelXml.getModelClass();
		if (modelClassNum == null) {
			modelClassNum = SBMLUtil.CLASS_TO_INT.get("unknown");
		}
		String modelClass = SBMLUtil.INT_TO_CLASS.get(modelClassNum);
		Map<String, String> qualityTags = parseQualityTags(estXml);

		Annotation annot = createAnnotation(modelId, modelTitle, modelClass,
				qualityTags);
		model.setAnnotation(annot);

		// Create a compartment and add it to the model
		Compartment compartment = createCompartment(matrixXml.getName());
		model.addCompartment(compartment);

		// Create species and add it to the model
		Species specie = createSpecies(organismXml.getName(), compartment);
		model.addSpecies(specie);

		ListOf<Rule> rules = new ListOf<>(LEVEL, VERSION);

		String depName = depXml.getOrigName();
		String depUnit = depXml.getUnit();

		// Parse independent params
		List<PmmXmlElementConvertable> indepParams = firstTuple.getPmmXml(
				Model1Schema.ATT_INDEPENDENT).getElementSet();

		// Add constraints
		for (PmmXmlElementConvertable item : indepParams) {
			IndepXml indep = (IndepXml) item;
			Constraint constraint = createConstraint(indep.getName(),
					indep.getMin(), indep.getMax());
			if (constraint != null) {
				model.addConstraint(constraint);
			}
		}
		// Add independent parameters
		List<Parameter> indeps = createIndependentParameter(indepParams);
		for (Parameter param : indeps) {
			model.addParameter(param);
		}

		// Parse constant parameters
		List<PmmXmlElementConvertable> constParams = firstTuple.getPmmXml(
				Model1Schema.ATT_PARAMETER).getElementSet();

		// Add constraints
		for (PmmXmlElementConvertable item : constParams) {
			ParamXml param = (ParamXml) item;
			Constraint constraint = createConstraint(param.getName(),
					param.getMin(), param.getMax());
			if (constraint != null) {
				model.addConstraint(constraint);
			}
		}
		// Add constant params
		List<Parameter> consts = createConstantParameters(constParams);
		for (Parameter param : consts) {
			model.addParameter(param);
		}

		// Add units
		ListOf<UnitDefinition> unitDefs = getUnits(depXml, indepParams,
				constParams);
		model.setListOfUnitDefinitions(unitDefs);

		// Create rule of the model and add it to the rest of rules
		String modelFormula = modelXml.getFormula();
		Model1Rule model1Rule = new Model1Rule();
		model1Rule.parse(modelFormula);
		rules.add(model1Rule.getRule());
		model.setListOfRules(rules);

		// Add submodels and model definitions
		int i = 0;
		Model2Rule model2Rule = new Model2Rule();
		for (KnimeTuple tuple : tuples) {
			CatalogModelXml secModelXml = (CatalogModelXml) tuple.getPmmXml(
					Model2Schema.ATT_MODELCATALOG).get(0);
			DepXml secDepXml = (DepXml) firstTuple.getPmmXml(
					Model2Schema.ATT_DEPENDENT).get(0);
			List<PmmXmlElementConvertable> secIndepsXml = tuple.getPmmXml(
					Model2Schema.ATT_INDEPENDENT).getElementSet();
			fixIndepUnits(secIndepsXml);
			List<PmmXmlElementConvertable> secConstParams = tuple.getPmmXml(
					Model2Schema.ATT_PARAMETER).getElementSet();
			fixConstUnits(secConstParams);

			String modelDefinitionId = "model_" + secDepXml.getName();
			ModelDefinition modelDefinition = new ModelDefinition(
					modelDefinitionId, LEVEL, VERSION);
			modelDefinition.setName(modelDefinitionId);

			// Add units
			unitDefs = getUnits(secDepXml, secIndepsXml, secConstParams);
			modelDefinition.setListOfUnitDefinitions(unitDefs);

			// Add dep from sec
			Parameter secDep = new Parameter(secDepXml.getName());
			secDep.setConstant(false);
			secDep.setValue(0.0);
			secDep.setUnits(secDepXml.getUnit());
			modelDefinition.addParameter(secDep);

			// Add indeps
			List<Parameter> secIndeps = createIndependentParameter(secIndepsXml);
			for (Parameter param : secIndeps) {
				modelDefinition.addParameter(param);
			}

			// Add constant parameters
			List<Parameter> secConsts = createConstantParameters(secConstParams);
			for (Parameter param : secConsts) {
				modelDefinition.addParameter(param);
			}

			String secFormula = secModelXml.getFormula();
			ListOf<Rule> secRules = new ListOf<>(LEVEL, VERSION);
			model2Rule.parse(secFormula);
			secRules.add(model2Rule.getRule());
			modelDefinition.setListOfRules(secRules);

			compDocPlugin.addModelDefinition(modelDefinition);

			Submodel submodel = compModelPlugin.createSubmodel("submodel"
					+ Integer.toString(i));
			submodel.setModelRef(modelDefinitionId);

			i++;
		}
		return doc;
	}
}