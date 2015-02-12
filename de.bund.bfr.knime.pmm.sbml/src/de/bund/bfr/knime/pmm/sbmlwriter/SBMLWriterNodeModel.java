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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import de.bund.bfr.knime.pmm.sbmlcommon.CreatedNode;
import de.bund.bfr.knime.pmm.sbmlcommon.CreatorNode;
import de.bund.bfr.knime.pmm.sbmlcommon.ModelIdNode;
import de.bund.bfr.knime.pmm.sbmlcommon.ModelTitleNode;
import de.bund.bfr.knime.pmm.sbmlcommon.ModifiedNode;
import de.bund.bfr.knime.pmm.sbmlcommon.UncertaintyNode;
import de.bund.bfr.knime.pmm.sbmlutil.LimitsConstraint;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;
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
			documents = reader.getDocuments();
		} else if (modelType == ModelType.TERCIARY) {
			TertiaryTableReader reader = new TertiaryTableReader(tuples,
					dlgInfo);
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
		// Get unit names and orig units
		HashSet<String> unitNames = new HashSet<>();
		// Maps every unit name with its original unit
		HashMap<String, String> origUnits = new HashMap<>();

		origUnits.put(depXml.getUnit(), depXml.getOrigName());

		for (PmmXmlElementConvertable pmmXmlElem : indepParams) {
			IndepXml indep = (IndepXml) pmmXmlElem;
			origUnits.put(indep.getUnit(), indep.getOrigName());
		}

		for (PmmXmlElementConvertable pmmXmlElem : constParams) {
			ParamXml constant = (ParamXml) pmmXmlElem;
			String constantUnit = constant.getUnit();
			if (constantUnit != null) {
				origUnits.put(constant.getUnit(), constant.getOrigName());
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

		ListOf<UnitDefinition> unitDefinitions = new ListOf<>();

		for (Entry<String, String> entry : origUnits.entrySet()) {
			String dbUnitName = entry.getKey();
			String origUnitName = entry.getValue();
			UnitsFromDB dbUnit = map.get(dbUnitName);
			if (dbUnit != null) {
				UnitDefinition unitDef = SBMLUtil.fromXml(dbUnit
						.getMathML_string());
				unitDef.setId(origUnitName);
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
	protected Species createSpecies(final String name, final String unit,
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
		species.setUnits(unit);

		return species;
	}

	protected List<Parameter> createIndependentParameter(
			final List<PmmXmlElementConvertable> params) {
		List<Parameter> indeps = new ArrayList<>();
		for (PmmXmlElementConvertable pmmParam : params) {
			IndepXml indepXml = (IndepXml) pmmParam;

			String name = indepXml.getName();
			String unit = indepXml.getOrigName();

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
			String unit = paramXml.getOrigName();

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
		XMLNamespaces pmfNS = new XMLNamespaces();
		pmfNS.add("http://purl.org/dc/terms/", "dc");
		pmfNS.add("http://purl.org/dc/terms/", "dcterms");
		pmfNS.add("http://www.dmg.org/PMML-4.2", "pmml");
		XMLNode pmfNode = new XMLNode(pmfTriple, null, pmfNS);

		String givenName = docInfo.get("GivenName");
		String familyName = docInfo.get("FamilyName");
		String contact = docInfo.get("Contact");
		String created = docInfo.get("Created");
		String modified = docInfo.get("Modified");

		if (givenName != null || familyName != null || contact != null) {
			CreatorNode creatorNode = new CreatorNode(givenName, familyName,
					contact);
			pmfNode.addChild(creatorNode.getNode());
		}

		// Created date
		if (created != null) {
			CreatedNode createdNode = new CreatedNode(created);
			pmfNode.addChild(createdNode.getNode());
		}

		// modified date
		if (modified != null) {
			ModifiedNode modifiedNode = new ModifiedNode(modified);
			pmfNode.addChild(modifiedNode.getNode());
		}

		// add non-rdf annotation
		annot.setNonRDFAnnotation(pmfNode);
		annot.addDeclaredNamespace("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");

		return annot;
	}

	protected Annotation createModelAnnotation(String modelId,
			String modelTitle, Map<String, String> uncertainties) {
		Annotation annot = new Annotation();

		// pmf container
		XMLTriple pmfTriple = new XMLTriple("metadata", null, "pmf");
		XMLNamespaces pmfNS = new XMLNamespaces();
		pmfNS.add("http://purl.org/dc/terms/", "dc");
		pmfNS.add("http://purl.org/dc/terms/", "dcterms");
		pmfNS.add("http://www.dmg.org/PMML-4.2", "pmml");
		XMLNode pmfNode = new XMLNode(pmfTriple, null, pmfNS);

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

		// add non-rdf annotation
		annot.setNonRDFAnnotation(pmfNode);
		annot.addDeclaredNamespace("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");

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

	public PrimaryTableReader(List<KnimeTuple> tuples,
			Map<String, String> dlgInfo) {
		super();

		for (KnimeTuple tuple : tuples) {
			SBMLDocument doc = parsePrimaryTuple(tuple, dlgInfo);
			documents.add(doc);
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
		AgentXml organismXml = (AgentXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_AGENT).get(0);
		MatrixXml matrixXml = (MatrixXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);
		LiteratureItem literatureXml = (LiteratureItem) tuple.getPmmXml(
				Model1Schema.ATT_MLIT).get(0);
		String modelId = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
		PmmXmlDoc units = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

		SBMLDocument doc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Composition package
		doc.enablePackage(CompConstants.shortLabel);

		// Document annotation
		Annotation docAnnot = createDocAnnotation(docInfo);
		doc.setAnnotation(docAnnot);

		Model model = doc.createModel(modelId);
		model.setName(modelXml.getName());

		// Annotation
		String modelTitle = modelXml.getName();
		int modelClassNum = modelXml.getModelClass();
		Map<String, String> qualityTags = parseQualityTags(estXml);

		// Add model annotations
		Annotation annot = createModelAnnotation(modelId, modelTitle, qualityTags);
		model.setAnnotation(annot);

		// Create compartment and add it to the model
		Matrix matrix = Matrix.convertMatrixXmlToMatrix(matrixXml);
		Compartment c = matrix.getCompartment();
		model.addCompartment(c);

		// Create species and add it to the model
		Species specie = createSpecies(organismXml.getName(), depXml.getUnit(),
				c);
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
			String name = indep.getName();
			if (!name.isEmpty()) {
				Double min = indep.getMin();
				Double max = indep.getMax();
				LimitsConstraint lc = new LimitsConstraint(name, min, max);
				if (lc.getConstraint() != null) {
					model.addConstraint(lc.getConstraint());
				}
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
		List<Parameter> consts = createConstantParameters(constParams);
		for (Parameter param : consts) {
			model.addParameter(param);
		}

		ListOf<UnitDefinition> unitDefs = getUnits(depXml, indepParams,
				constParams);
		model.setListOfUnitDefinitions(unitDefs);

		// Create rule of the model and add it to the rest of rules
		Model1Rule model1Rule = Model1Rule
				.convertCatalogModelXmlToModel1Rule(modelXml);
		rules.add(model1Rule.getRule());
		model.setListOfRules(rules);

		return doc;
	}
}

class TertiaryTableReader extends TableReader {

	public TertiaryTableReader(List<KnimeTuple> tuples,
			Map<String, String> dlgInfo) {
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
			SBMLDocument doc = parseTertiaryTuple(modelTuples, dlgInfo);
			documents.add(doc);
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

		// Document annotation
		Annotation docAnnot = createDocAnnotation(docInfo);
		doc.setAnnotation(docAnnot);

		Model model = doc.createModel(modelId);
		model.setName(modelXml.getName());
		CompModelPlugin compModelPlugin = (CompModelPlugin) model
				.getPlugin(CompConstants.shortLabel);

		// Annotation
		String modelTitle = modelXml.getName();
		Integer modelClassNum = modelXml.getModelClass();
		if (modelClassNum == null) {
			modelClassNum = SBMLUtil.CLASS_TO_INT.get("unknown");
		}
		Map<String, String> qualityTags = parseQualityTags(estXml);

		// Add model annotations
		Annotation annot = createModelAnnotation(modelId, modelTitle, qualityTags);
		model.setAnnotation(annot);

		// Create a compartment and add it to the model
		Matrix matrix = Matrix.convertMatrixXmlToMatrix(matrixXml);
		Compartment compartment = matrix.getCompartment();
		model.addCompartment(compartment);

		// Create species and add it to the model
		Species specie = createSpecies(organismXml.getName(), depXml.getUnit(),
				compartment);
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
			String name = indep.getName();
			if (!name.isEmpty()) {
				Double min = indep.getMin();
				Double max = indep.getMax();
				LimitsConstraint lc = new LimitsConstraint(name, min, max);
				if (lc.getConstraint() != null) {
					model.addConstraint(lc.getConstraint());
				}
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
		List<Parameter> consts = createConstantParameters(constParams);
		for (Parameter param : consts) {
			model.addParameter(param);
		}

		// Add units
		ListOf<UnitDefinition> unitDefs = getUnits(depXml, indepParams,
				constParams);
		model.setListOfUnitDefinitions(unitDefs);

		// Create rule of the model and add it to the rest of rules
		Model1Rule model1Rule = Model1Rule
				.convertCatalogModelXmlToModel1Rule(modelXml);
		rules.add(model1Rule.getRule());
		model.setListOfRules(rules);

		// Add submodels and model definitions
		int i = 0;
		for (KnimeTuple tuple : tuples) {
			CatalogModelXml secModelXml = (CatalogModelXml) tuple.getPmmXml(
					Model2Schema.ATT_MODELCATALOG).get(0);
			DepXml secDepXml = (DepXml) firstTuple.getPmmXml(
					Model2Schema.ATT_DEPENDENT).get(0);
			List<PmmXmlElementConvertable> secIndepsXml = tuple.getPmmXml(
					Model2Schema.ATT_INDEPENDENT).getElementSet();
			List<PmmXmlElementConvertable> secConstParams = tuple.getPmmXml(
					Model2Schema.ATT_PARAMETER).getElementSet();

			String modelDefinitionId = "model_" + secDepXml.getName();
			ModelDefinition modelDefinition = new ModelDefinition(
					modelDefinitionId, LEVEL, VERSION);
			modelDefinition.setName(secModelXml.getName());

			// Add units
			unitDefs = getUnits(secDepXml, secIndepsXml, secConstParams);
			modelDefinition.setListOfUnitDefinitions(unitDefs);

			// Add dep from sec
			Parameter secDep = new Parameter(secDepXml.getName());
			secDep.setConstant(false);
			secDep.setValue(0.0);
			secDep.setUnits(secDepXml.getOrigName());
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

			ListOf<Rule> secRules = new ListOf<>(LEVEL, VERSION);
			Model2Rule model2Rule = Model2Rule
					.convertCatalogModelXmlToModel2Rule(secModelXml);
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