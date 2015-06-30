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
import java.util.LinkedHashMap;
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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
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
import de.bund.bfr.knime.pmm.annotation.DataSourceNode;
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
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.file.ExperimentalDataFile;
import de.bund.bfr.knime.pmm.file.ManualSecondaryModelFile;
import de.bund.bfr.knime.pmm.file.ManualTertiaryModelFile;
import de.bund.bfr.knime.pmm.file.OneStepSecondaryModelFile;
import de.bund.bfr.knime.pmm.file.OneStepTertiaryModelFile;
import de.bund.bfr.knime.pmm.file.PrimaryModelWDataFile;
import de.bund.bfr.knime.pmm.file.PrimaryModelWODataFile;
import de.bund.bfr.knime.pmm.file.RawDataFile;
import de.bund.bfr.knime.pmm.file.TwoStepSecondaryModelFile;
import de.bund.bfr.knime.pmm.file.TwoStepTertiaryModelFile;
import de.bund.bfr.knime.pmm.model.ExperimentalData;
import de.bund.bfr.knime.pmm.model.ManualSecondaryModel;
import de.bund.bfr.knime.pmm.model.ManualTertiaryModel;
import de.bund.bfr.knime.pmm.model.OneStepSecondaryModel;
import de.bund.bfr.knime.pmm.model.OneStepTertiaryModel;
import de.bund.bfr.knime.pmm.model.PrimaryModelWData;
import de.bund.bfr.knime.pmm.model.PrimaryModelWOData;
import de.bund.bfr.knime.pmm.model.TwoStepSecondaryModel;
import de.bund.bfr.knime.pmm.model.TwoStepTertiaryModel;
import de.bund.bfr.knime.pmm.sbmlutil.Agent;
import de.bund.bfr.knime.pmm.sbmlutil.Coefficient;
import de.bund.bfr.knime.pmm.sbmlutil.DataFile;
import de.bund.bfr.knime.pmm.sbmlutil.LimitsConstraint;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;
import de.bund.bfr.knime.pmm.sbmlutil.Model1Annotation;
import de.bund.bfr.knime.pmm.sbmlutil.Model1Rule;
import de.bund.bfr.knime.pmm.sbmlutil.Model2Annotation;
import de.bund.bfr.knime.pmm.sbmlutil.Model2Rule;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;
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
	protected static final String CFG_ISSECONDARY = "isSecondary";

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
	private SettingsModelBoolean isSecondary = new SettingsModelBoolean(
			CFG_ISSECONDARY, false);

	// TODO: Parsers
	// Parser parser; // current parser

//	Parser experimentalDataParser = new ExperimentalDataParser();

	// Parser primaryModelWDataParser = new PrimaryModelWDataParser();
	// Parser primaryModelWODataParser = new PrimaryModelWODataParser();
	// Parser twoStepSecondaryModelParser = new TwoStepSecondaryModelParser();
	// Parser oneStepSecondaryModelParser = new OneStepSecondaryModelParser();
	// Parser manualSecondaryModelParser = new ManualSecondaryModelParser();
	// Parser twoStepTertiaryModelParser = new TwoStepTertiaryModelParser();
	// Parser oneStepTertiaryModelParser = new OneStepTertiaryModelParser();
	// Parser manualTertiaryModelParser = new ManualTertiaryModelParser();

	/**
	 * Constructor for the node model.
	 */
	protected SBMLWriterNodeModel() {
		super(1, 0);
	}

	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		KnimeSchema schema = null;
		ModelType modelType = null;
		List<KnimeTuple> tuples;

		// Table has the structure Model1 + Model2 + Data
		if (SchemaFactory.createM12DataSchema().conforms(inData[0])) {
			schema = SchemaFactory.createM12DataSchema();
			tuples = PmmUtilities.getTuples(inData[0], schema);
			if (hasData(tuples)) {
				boolean identical = identicalEstModels(tuples);
				if (isSecondary.getBooleanValue() == true) {
					if (identical) {
						modelType = ModelType.ONE_STEP_SECONDARY_MODEL;
					} else {
						modelType = ModelType.TWO_STEP_SECONDARY_MODEL;
					}
				} else {
					if (identical) {
						modelType = ModelType.ONE_STEP_TERTIARY_MODEL;
					} else {
						modelType = ModelType.TWO_STEP_TERTIARY_MODEL;
					}
				}
			} else {
				modelType = ModelType.MANUAL_TERTIARY_MODEL;
			}
		}

		// Table has Model1 + Data
		else if (SchemaFactory.createM1DataSchema().conforms(inData[0])) {
			schema = SchemaFactory.createM1DataSchema();
			tuples = PmmUtilities.getTuples(inData[0], schema);

			// Check every tuple. If any tuple has data (number of data points >
			// 0) then assigns PRIMARY_MODEL_WDATA. Otherwise it assigns
			// PRIMARY_MODEL_WODATA
			modelType = ModelType.PRIMARY_MODEL_WODATA;
			for (KnimeTuple tuple : tuples) {
				PmmXmlDoc mdData = tuple
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
				if (mdData.size() > 0) {
					modelType = ModelType.PRIMARY_MODEL_WDATA;
					break;
				}
			}
		}

		// Table only has data
		else if (SchemaFactory.createDataSchema().conforms(inData[0])) {
			schema = SchemaFactory.createDataSchema();
			tuples = PmmUtilities.getTuples(inData[0], schema);
			modelType = ModelType.EXPERIMENTAL_DATA;
		}

		// Table only has secondary model cells
		else if (SchemaFactory.createM2Schema().conforms(inData[0])) {
			schema = SchemaFactory.createM2Schema();
			tuples = PmmUtilities.getTuples(inData[0], schema);
			modelType = ModelType.MANUAL_SECONDARY_MODEL;
		} else {
			throw new Exception();
		}

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

		String dir = outPath.getStringValue();
		String mdName = modelName.getStringValue();

		if (modelType == ModelType.EXPERIMENTAL_DATA) {
			ExperimentalDataReader reader = new ExperimentalDataReader(tuples);
			List<ExperimentalData> eds = reader.getExperimentalData();
			ExperimentalDataFile.write(dir, mdName, eds, exec);
		} else if (modelType == ModelType.PRIMARY_MODEL_WDATA) {
			PrimaryModelWDataReader reader = new PrimaryModelWDataReader(tuples);
			List<PrimaryModelWData> pms = reader.getModels();
			PrimaryModelWDataFile.write(dir, mdName, pms, exec);
		} else if (modelType == ModelType.PRIMARY_MODEL_WODATA) {
			PrimaryModelWODataReader reader = new PrimaryModelWODataReader(
					tuples);
			List<PrimaryModelWOData> pms = reader.getModels();
			PrimaryModelWODataFile.write(dir, mdName, pms, exec);
		} else if (modelType == ModelType.TWO_STEP_SECONDARY_MODEL) {
			TwoStepSecondaryModelReader reader = new TwoStepSecondaryModelReader(
					tuples);
			List<TwoStepSecondaryModel> sms = reader.getModels();
			TwoStepSecondaryModelFile.write(dir, mdName, sms, exec);
		} else if (modelType == ModelType.ONE_STEP_SECONDARY_MODEL) {
			OneStepSecondaryModelReader reader = new OneStepSecondaryModelReader(
					tuples);
			List<OneStepSecondaryModel> sms = reader.getModels();
			OneStepSecondaryModelFile.write(dir, mdName, sms, exec);
		} else if (modelType == ModelType.MANUAL_SECONDARY_MODEL) {
			ManualSecondaryModelReader reader = new ManualSecondaryModelReader(
					tuples);
			List<ManualSecondaryModel> sms = reader.getModels();
			ManualSecondaryModelFile.write(dir, mdName, sms, exec);
		} else if (modelType == ModelType.TWO_STEP_TERTIARY_MODEL) {
			TwoStepTertiaryModelReader reader = new TwoStepTertiaryModelReader(
					tuples);
			List<TwoStepTertiaryModel> tms = reader.getModels();
			TwoStepTertiaryModelFile.write(dir, mdName, tms, exec);
		} else if (modelType == ModelType.ONE_STEP_TERTIARY_MODEL) {
			OneStepTertiaryModelReader reader = new OneStepTertiaryModelReader(
					tuples);
			List<OneStepTertiaryModel> tms = reader.getModels();
			OneStepTertiaryModelFile.write(dir, mdName, tms, exec);
		} else if (modelType == ModelType.MANUAL_TERTIARY_MODEL) {
			ManualTertiaryModelReader reader = new ManualTertiaryModelReader(
					tuples);
			List<ManualTertiaryModel> tms = reader.getModels();
			ManualTertiaryModelFile.write(dir, mdName, tms, exec);
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
		if (outPath.getStringValue() == null
				|| modelName.getStringValue() == null
				|| variableParams.getStringValue() == null) {
			throw new InvalidSettingsException("Node must be configured");
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
		isSecondary.saveSettingsTo(settings);
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
		isSecondary.loadSettingsFrom(settings);
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
		isSecondary.validateSettings(settings);
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

	static boolean identicalEstModels(List<KnimeTuple> tuples) {
		int id = ((EstModelXml) tuples.get(0)
				.getPmmXml(Model1Schema.ATT_ESTMODEL).get(0)).getId();
		for (KnimeTuple tuple : tuples.subList(1, tuples.size())) {
			EstModelXml estModel = (EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0);
			if (id != estModel.getId()) {
				return false;
			}
		}
		return true;
	}

	public boolean hasData(List<KnimeTuple> tuples) {
		for (KnimeTuple tuple : tuples) {
			PmmXmlDoc mdData = (PmmXmlDoc) tuple
					.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			if (mdData != null && mdData.size() > 0) {
				return true;
			}
		}
		return false;
	}
}

abstract class TableReader {
	protected final static int LEVEL = 3;
	protected final static int VERSION = 1;

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

		if (estModel.getId() != null) {
			qualityTags.put("id", estModel.getId().toString());
		}

		if (estModel.getComment() != null) {
			qualityTags.put("dataUsage", estModel.getComment());
		}

		if (estModel.getName() == null) {
			qualityTags.put("dataName", "Missing data name");
		} else {
			qualityTags.put("dataName", estModel.getName());
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
						Unit u = new Unit(wrapperUnit);
						if (!u.isSetKind()) {
							u.setKind(Unit.Kind.DIMENSIONLESS);
						}
						if (!u.isSetExponent()) {
							u.setExponent(1.0);
						}
						if (!u.isSetScale()) {
							u.setScale(0);
						}
						if (!u.isSetMultiplier()) {
							u.setMultiplier(1);
						}
						unitDefinition.addUnit(u);
					}
				}
			}

			model.addUnitDefinition(unitDefinition);
		}
	}

	public Map<Integer, Map<Integer, List<KnimeTuple>>> sortGlobalModels(
			List<KnimeTuple> tuples) {
		// Sort tertiary models
		Map<Integer, Map<Integer, List<KnimeTuple>>> gms = new HashMap<>();
		for (KnimeTuple tuple : tuples) {
			Integer gmID = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);
			Integer condID = tuple.getInt(TimeSeriesSchema.ATT_CONDID);

			// global model is in globalModels
			if (gms.containsKey(gmID)) {
				// Get global model
				Map<Integer, List<KnimeTuple>> gm = gms.get(gmID);
				// globalModel has tertiary model with condID => Add tuple to
				// this tertiary model
				if (gm.containsKey(condID)) {
					gm.get(condID).add(tuple);
				}
				// Otherwise, create a tertiary model with condID and add it the
				// current tuple
				else {
					LinkedList<KnimeTuple> tertiaryModel = new LinkedList<>();
					tertiaryModel.add(tuple);
					gm.put(condID, tertiaryModel);
				}
			}

			// else, create tertiary model with condID and add it to new global
			// model
			else {
				// Create new global model
				HashMap<Integer, List<KnimeTuple>> gm = new HashMap<>();

				// Create tertiary model and add it to new global model
				LinkedList<KnimeTuple> tertiaryModel = new LinkedList<>();
				tertiaryModel.add(tuple);
				gm.put(condID, tertiaryModel);

				// Add new global model
				gms.put(gmID, gm);
			}
		}
		return gms;
	}

}

// TODO: ...
//interface Parser {
//	public void write(List<KnimeTuple> tuples, String dir, String mdName,
//			ExecutionContext exec) throws Exception;
//}
//
//class ExperimentalDataParser implements Parser {
//
//	@Override
//	public void write(List<KnimeTuple> tuples, String dir, String mdName,
//			ExecutionContext exec) throws Exception {
//		List<ExperimentalData> eds = new LinkedList<>();
//		
//		
//	}
//	
//	public void parse
//
//}

/**
 * Parse tuples from a table with timeseries.
 * 
 * @author Miguel Alba
 */
class ExperimentalDataReader extends TableReader {

	List<ExperimentalData> eds = new LinkedList<>();

	public ExperimentalDataReader(List<KnimeTuple> tuples)
			throws URISyntaxException {

		for (KnimeTuple tuple : tuples) {
			// Gets and adds dataset
			ExperimentalData ed = parse(tuple);
			eds.add(ed);
		}
	}

	public List<ExperimentalData> getExperimentalData() {
		return eds;
	}

	private ExperimentalData parse(KnimeTuple tuple) throws URISyntaxException {
		// Create dim
		LinkedHashMap<Double, Double> dim = new LinkedHashMap<>();
		PmmXmlDoc mdData = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
		for (PmmXmlElementConvertable origPoint : mdData.getElementSet()) {
			TimeSeriesXml point = (TimeSeriesXml) origPoint;
			dim.put(point.getTime(), point.getConcentration());
		}

		String unit = ((TimeSeriesXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_TIMESERIES).get(0)).getConcentrationUnit();
		MatrixXml matrixXml = (MatrixXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);
		AgentXml agentXml = (AgentXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_AGENT).get(0);

		RawDataFile dataFile = new RawDataFile(dim, unit, matrixXml, agentXml);

		return new ExperimentalData(dataFile.getDocument());
	}
}

/**
 * Parse tuples from a table with primary models with data.
 */
class PrimaryModelWDataReader extends TableReader {

	List<PrimaryModelWData> models = new LinkedList<>();

	public PrimaryModelWDataReader(List<KnimeTuple> tuples)
			throws URISyntaxException {
		super();

		for (KnimeTuple tuple : tuples) {
			SBMLDocument sbmlDoc = parse(tuple);
			PrimaryModelWData model;

			// TODO: model data should be parsed in the parse method
			PmmXmlDoc mdData = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			// No data for this model
			if (mdData.size() == 0) {
				model = new PrimaryModelWData(sbmlDoc, null);
			} else {
				// Create dim
				LinkedHashMap<Double, Double> dim = new LinkedHashMap<>();
				for (PmmXmlElementConvertable origPoint : mdData
						.getElementSet()) {
					TimeSeriesXml point = (TimeSeriesXml) origPoint;
					dim.put(point.getTime(), point.getConcentration());
				}

				TimeSeriesXml firstPoint = (TimeSeriesXml) mdData.get(0);
				String unit = firstPoint.getConcentrationUnit();

				MatrixXml matrix = (MatrixXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0);

				AgentXml agent = (AgentXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0);

				DepXml dep = (DepXml) tuple.getPmmXml(
						Model1Schema.ATT_DEPENDENT).get(0);

				DataFile dataFile = new DataFile(dim, unit, matrix, agent,
						dep.getUnit(), new HashMap<String, String>());

				// Gets and add dataset
				NuMLDocument numlDoc = dataFile.getDocument();
				model = new PrimaryModelWData(sbmlDoc, numlDoc);
			}
			models.add(model);
		}
	}

	public List<PrimaryModelWData> getModels() {
		return models;
	}

	private SBMLDocument parse(KnimeTuple tuple) {
		replaceCelsiusAndFahrenheit(tuple);
		renameLog(tuple);

		// Retrieve TimeSeriesSchema cells
		AgentXml agentXml = (AgentXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_AGENT).get(0);
		MatrixXml matrixXml = (MatrixXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);
		int condId = tuple.getInt(TimeSeriesSchema.ATT_CONDID);
		String combaseId = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
		PmmXmlDoc miscDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
		PmmXmlDoc mdData = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);

		// Retrieve Model1Schema cells
		CatalogModelXml catModel = (CatalogModelXml) tuple.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estModel = (EstModelXml) tuple.getPmmXml(
				Model1Schema.ATT_ESTMODEL).get(0);
		DepXml dep = (DepXml) tuple.getPmmXml(Model1Schema.ATT_DEPENDENT)
				.get(0);
		IndepXml indep = (IndepXml) tuple.getPmmXml(
				Model1Schema.ATT_INDEPENDENT).get(0);
		PmmXmlDoc paramsDoc = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
		PmmXmlDoc litDoc = tuple.getPmmXml(Model1Schema.ATT_EMLIT);

		String modelId = "model";

		SBMLDocument doc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Composition package
		doc.enablePackage(CompConstants.shortLabel);

		addNamespaces(doc);

		Model model = doc.createModel(modelId);
		if (estModel.getName() != null) {
			model.setName(estModel.getName());
		}

		// Annotation
		String modelTitle = estModel.getName();
		Map<String, String> qualityTags = parseQualityTags(estModel);

		// Get literature references
		List<LiteratureItem> lits = new LinkedList<>();
		for (PmmXmlElementConvertable item : litDoc.getElementSet()) {
			lits.add((LiteratureItem) item);
		}

		// Add model annotations
		Model1Annotation primModelAnnotation = new Model1Annotation(modelId,
				modelTitle, qualityTags, lits, combaseId, condId);
		model.getAnnotation()
				.setNonRDFAnnotation(primModelAnnotation.getNode());

		// Create compartment and add it to the model
		List<MiscXml> miscs = new LinkedList<>();
		for (PmmXmlElementConvertable item : miscDoc.getElementSet()) {
			miscs.add((MiscXml) item);
		}

		// Create and add compartment to model
		Map<String, Double> miscsMap = new HashMap<>();
		for (MiscXml misc : miscs) {
			miscsMap.put(misc.getName(), misc.getValue());
		}
		Matrix matrix = new Matrix(matrixXml, miscsMap);
		Compartment c = matrix.getCompartment();
		model.addCompartment(c);

		// Create species and add it to the model
		Agent organims = new Agent(agentXml, dep.getUnit(), c);
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
		for (PmmXmlElementConvertable item : paramsDoc.getElementSet()) {
			constXmls.add((ParamXml) item);
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
		addUnitDefinitions(model, dep, indepXmls, constXmls);

		// Create rule of the model and add it to the rest of rules
		Model1Rule model1Rule = Model1Rule.convertCatalogModelXmlToModel1Rule(
				catModel, organims.getSpecies().getId());
		model.addRule(model1Rule.getRule());
		return doc;
	}
}

/**
 * Parse tuples from a table with primary models without data.
 */
class PrimaryModelWODataReader extends TableReader {

	List<PrimaryModelWOData> models = new LinkedList<>();

	public PrimaryModelWODataReader(List<KnimeTuple> tuples)
			throws URISyntaxException {
		super();

		for (KnimeTuple tuple : tuples) {
			SBMLDocument sbmlDoc = parse(tuple);
			models.add(new PrimaryModelWOData(sbmlDoc));
		}
	}

	public List<PrimaryModelWOData> getModels() {
		return models;
	}

	private SBMLDocument parse(KnimeTuple tuple) {
		replaceCelsiusAndFahrenheit(tuple);
		renameLog(tuple);

		// Retrieve TimeSeriesSchema cells
		AgentXml agentXml = (AgentXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_AGENT).get(0);
		MatrixXml matrixXml = (MatrixXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);
		int condId = tuple.getInt(TimeSeriesSchema.ATT_CONDID);
		String combaseId = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
		PmmXmlDoc miscDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

		// Retrieve Model1Schema cells
		CatalogModelXml catModel = (CatalogModelXml) tuple.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estModel = (EstModelXml) tuple.getPmmXml(
				Model1Schema.ATT_ESTMODEL).get(0);
		DepXml dep = (DepXml) tuple.getPmmXml(Model1Schema.ATT_DEPENDENT)
				.get(0);
		IndepXml indep = (IndepXml) tuple.getPmmXml(
				Model1Schema.ATT_INDEPENDENT).get(0);
		PmmXmlDoc paramsDoc = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
		PmmXmlDoc litDoc = tuple.getPmmXml(Model1Schema.ATT_EMLIT);

		String modelId = "model";

		SBMLDocument doc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Composition package
		doc.enablePackage(CompConstants.shortLabel);

		addNamespaces(doc);

		Model model = doc.createModel(modelId);
		if (estModel.getName() != null) {
			model.setName(estModel.getName());
		}

		// Annotation
		String modelTitle = estModel.getName();
		Map<String, String> qualityTags = parseQualityTags(estModel);

		// Get literature references
		List<LiteratureItem> lits = new LinkedList<>();
		for (PmmXmlElementConvertable item : litDoc.getElementSet()) {
			lits.add((LiteratureItem) item);
		}

		// Add model annotations
		Model1Annotation primModelAnnotation = new Model1Annotation(modelId,
				modelTitle, qualityTags, lits, combaseId, condId);
		model.getAnnotation()
				.setNonRDFAnnotation(primModelAnnotation.getNode());

		// Create compartment and add it to the model
		List<MiscXml> miscs = new LinkedList<>();
		for (PmmXmlElementConvertable item : miscDoc.getElementSet()) {
			miscs.add((MiscXml) item);
		}

		// Create and add compartment to model
		Map<String, Double> miscsMap = new HashMap<>();
		for (MiscXml misc : miscs) {
			miscsMap.put(misc.getName(), misc.getValue());
		}
		Matrix matrix = new Matrix(matrixXml, miscsMap);
		Compartment c = matrix.getCompartment();
		model.addCompartment(c);

		// Create species and add it to the model
		Agent organims = new Agent(agentXml, dep.getUnit(), c);
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
		for (PmmXmlElementConvertable item : paramsDoc.getElementSet()) {
			constXmls.add((ParamXml) item);
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
		addUnitDefinitions(model, dep, indepXmls, constXmls);

		// Create rule of the model and add it to the rest of rules
		Model1Rule model1Rule = Model1Rule.convertCatalogModelXmlToModel1Rule(
				catModel, organims.getSpecies().getId());
		model.addRule(model1Rule.getRule());
		return doc;
	}
}

/**
 * Parse tuples from a table with only secondary models (without data or primary
 * models).
 */
class ManualSecondaryModelReader extends TableReader {

	List<ManualSecondaryModel> models = new LinkedList<>();

	public ManualSecondaryModelReader(List<KnimeTuple> tuples)
			throws URISyntaxException {
		super();

		for (KnimeTuple tuple : tuples) {
			SBMLDocument sbmlDoc = parse(tuple);
			models.add(new ManualSecondaryModel(sbmlDoc));
		}
	}

	private SBMLDocument parse(KnimeTuple tuple) {

		// Retrieve Model2Schema cells
		CatalogModelXml catModel = (CatalogModelXml) tuple.getPmmXml(
				Model2Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estModel = (EstModelXml) tuple.getPmmXml(
				Model2Schema.ATT_ESTMODEL).get(0);
		DepXml dep = (DepXml) tuple.getPmmXml(Model2Schema.ATT_DEPENDENT)
				.get(0);
		PmmXmlDoc indepDoc = tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT);
		PmmXmlDoc paramsDoc = tuple.getPmmXml(Model2Schema.ATT_PARAMETER);
		PmmXmlDoc litDoc = tuple.getPmmXml(Model2Schema.ATT_EMLIT);
		int globalModelID = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);

		// Get independent parameters
		LinkedList<IndepXml> indepXmls = new LinkedList<>();
		for (PmmXmlElementConvertable item : indepDoc.getElementSet()) {
			indepXmls.add((IndepXml) item);
		}

		// Get constant parameters
		LinkedList<ParamXml> constXmls = new LinkedList<>();
		for (PmmXmlElementConvertable item : paramsDoc.getElementSet()) {
			constXmls.add((ParamXml) item);
		}

		SBMLDocument doc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Composition package
		doc.enablePackage(CompConstants.shortLabel);

		// Add SBML comp plugin to SBML document
		CompSBMLDocumentPlugin docCompPlugin = (CompSBMLDocumentPlugin) doc
				.getPlugin(CompConstants.shortLabel);

		addNamespaces(doc);

		// Create model definition
		String modelDefinitionId = "model_" + dep.getName();
		Model model = docCompPlugin.createModelDefinition(modelDefinitionId);
		if (estModel.getName() != null) {
			model.setName(estModel.getName());
		}

		addUnitDefinitions(model, dep, indepXmls, constXmls);

		// Add dep
		Parameter depParam = new Parameter(dep.getName());
		depParam.setConstant(false);
		depParam.setValue(0.0);
		if (dep.getUnit() != null) {
			depParam.setUnits(Util.createId(dep.getUnit()));
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
				.convertCatalogModelXmlToModel2Rule(catModel);
		model.addRule(rule2.getRule());

		// Add literature items
		LinkedList<LiteratureItem> lits = new LinkedList<>();
		for (PmmXmlElementConvertable item : litDoc.getElementSet()) {
			lits.add((LiteratureItem) item);
		}

		// Add annotation
		Map<String, String> uncertainties = parseQualityTags(estModel);
		Model2Annotation modelAnnotation = new Model2Annotation(globalModelID,
				uncertainties, lits);
		model.getAnnotation().setNonRDFAnnotation(modelAnnotation.getNode());

		return doc;
	}

	public List<ManualSecondaryModel> getModels() {
		return models;
	}
}

/**
 * Parse tuples from a table with two step secondary models.
 */
class TwoStepSecondaryModelReader extends TableReader {

	List<TwoStepSecondaryModel> models = new LinkedList<>();

	public TwoStepSecondaryModelReader(List<KnimeTuple> tuples)
			throws URISyntaxException {
		super();

		// Sort secondary models
		Map<Integer, List<KnimeTuple>> secTuples = new HashMap<>();
		for (KnimeTuple tuple : tuples) {
			// Get secondary EstModel
			EstModelXml estModel = (EstModelXml) tuple.getPmmXml(
					Model2Schema.ATT_ESTMODEL).get(0);
			if (secTuples.containsKey(estModel.getId())) {
				secTuples.get(estModel.getId()).add(tuple);
			} else {
				List<KnimeTuple> tlist = new LinkedList<>();
				tlist.add(tuple);
				secTuples.put(estModel.getId(), tlist);
			}
		}

		// For the tuples of every secondary model
		for (List<KnimeTuple> tupleList : secTuples.values()) {
			TwoStepSecondaryModel model = parse(tupleList);
			models.add(model);
		}
	}

	public List<TwoStepSecondaryModel> getModels() {
		return models;
	}

	private TwoStepSecondaryModel parse(List<KnimeTuple> tuples)
			throws URISyntaxException {
		/**
		 * <ol>
		 * <li>Create n SBMLDocument for primary models</li>
		 * <li>Parse data and create n NuMLDocument</li>
		 * <li>Create SBMLDocument for secondary model</li>
		 * </ol>
		 */
		List<PrimaryModelWData> primModels = new LinkedList<>();

		for (KnimeTuple tuple : tuples) {
			// Retrieve TimeSeriesSchema cells
			AgentXml organismXml = (AgentXml) tuple.getPmmXml(
					TimeSeriesSchema.ATT_AGENT).get(0);
			MatrixXml matrixXml = (MatrixXml) tuple.getPmmXml(
					TimeSeriesSchema.ATT_MATRIX).get(0);
			int condId = tuple.getInt(TimeSeriesSchema.ATT_CONDID);
			String combaseId = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
			PmmXmlDoc miscDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
			PmmXmlDoc mdData = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);

			// Retrieve Model1Schema cells
			CatalogModelXml catModel = (CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0);
			EstModelXml estModel = (EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0);
			DepXml dep = (DepXml) tuple.getPmmXml(Model1Schema.ATT_DEPENDENT)
					.get(0);
			IndepXml indep = (IndepXml) tuple.getPmmXml(
					Model1Schema.ATT_INDEPENDENT).get(0);
			PmmXmlDoc paramsDoc = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
			PmmXmlDoc litDoc = tuple.getPmmXml(Model1Schema.ATT_EMLIT);

			// Create SBMLDocument for the primary model
			SBMLDocument doc = new SBMLDocument(LEVEL, VERSION);
			// Enable Hierarchical Compositon package
			doc.enablePackage(CompConstants.shortLabel);
			addNamespaces(doc);

			String modelId = Util.createId("model" + estModel.getId());

			// Creates model and names it
			Model model = doc.createModel(modelId);
			if (estModel.getName() != null) {
				model.setName(estModel.getName());
			}

			// Builds and adds model annotation
			// a) Model title
			String modelTitle = estModel.getName();

			// b) Literature references
			List<LiteratureItem> lits = new LinkedList<>();
			for (PmmXmlElementConvertable item : litDoc.getElementSet()) {
				lits.add((LiteratureItem) item);
			}

			// c) Parse quality measures
			Map<String, String> qualityTags = parseQualityTags(estModel);

			// d) Builds annotation
			Model1Annotation m1Annot = new Model1Annotation(modelId,
					modelTitle, qualityTags, lits, combaseId, condId);

			// e) Adds annotation to the model
			model.getAnnotation().setNonRDFAnnotation(m1Annot.getNode());

			// Creates and adds compartment to the model
			// a) Gather misc values
			Map<String, Double> miscs = new HashMap<>();
			for (PmmXmlElementConvertable item : miscDoc.getElementSet()) {
				MiscXml miscXml = (MiscXml) item;
				miscs.put(miscXml.getName(), miscXml.getValue());
			}

			// b) Creates matrix and compartment
			Matrix matrix = new Matrix(matrixXml, miscs);
			Compartment c = matrix.getCompartment();

			// c) Adds compartment to the model
			model.addCompartment(c);

			// Creates species and adds it to the model
			Agent agent = new Agent(organismXml, dep.getUnit(), c);
			model.addSpecies(agent.getSpecies());

			// Add indep constraint
			if (!indep.getName().isEmpty()) {
				Double min = indep.getMin();
				Double max = indep.getMax();
				LimitsConstraint lc = new LimitsConstraint(indep.getName(),
						min, max);
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
			for (PmmXmlElementConvertable pmmParam : paramsDoc.getElementSet()) {
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

			LinkedList<IndepXml> indepXmls = new LinkedList<>(
					Arrays.asList(indep));
			addUnitDefinitions(model, dep, indepXmls, constXmls);

			// Create rule of the model and add it to the rest of rules
			Model1Rule model1Rule = Model1Rule
					.convertCatalogModelXmlToModel1Rule(catModel, agent
							.getSpecies().getId());
			model.addRule(model1Rule.getRule());

			// Parse data
			PrimaryModelWData pmwd;
			// a) There is no data
			if (mdData.size() == 0) {
				pmwd = new PrimaryModelWData(doc, null);
			} else {
				// Create dim
				LinkedHashMap<Double, Double> dim = new LinkedHashMap<>();
				for (PmmXmlElementConvertable origPoint : mdData
						.getElementSet()) {
					TimeSeriesXml point = (TimeSeriesXml) origPoint;
					dim.put(point.getTime(), point.getConcentration());
				}

				TimeSeriesXml firstPoint = (TimeSeriesXml) mdData.get(0);
				String unit = firstPoint.getConcentrationUnit();

				DataFile dataFile = new DataFile(dim, unit, matrixXml,
						organismXml, dep.getUnit(),
						new HashMap<String, String>());

				// Adds DataSourceNode to the model
				DataSourceNode dsn = new DataSourceNode(estModel.getId()
						+ ".numl");
				model.getAnnotation().getNonRDFannotation()
						.addChild(dsn.getNode());

				pmwd = new PrimaryModelWData(doc, dataFile.getDocument());
			}
			primModels.add(pmwd);
		}

		// We get the first tuple to query the Model2 columns which are the same
		// for all the tuples of the secondary model
		KnimeTuple firstTuple = tuples.get(0);

		// Retrieve Model2Schema cells
		CatalogModelXml catModel = (CatalogModelXml) firstTuple.getPmmXml(
				Model2Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estModel = (EstModelXml) firstTuple.getPmmXml(
				Model2Schema.ATT_ESTMODEL).get(0);
		DepXml dep = (DepXml) firstTuple.getPmmXml(Model2Schema.ATT_DEPENDENT)
				.get(0);
		PmmXmlDoc indepDoc = firstTuple.getPmmXml(Model2Schema.ATT_INDEPENDENT);
		PmmXmlDoc paramsDoc = firstTuple.getPmmXml(Model2Schema.ATT_PARAMETER);
		PmmXmlDoc litDoc = firstTuple.getPmmXml(Model2Schema.ATT_EMLIT);
		int globalModelId = firstTuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);

		LinkedList<IndepXml> indepXmls = new LinkedList<>();
		for (PmmXmlElementConvertable item : indepDoc.getElementSet()) {
			indepXmls.add((IndepXml) item);
		}

		LinkedList<ParamXml> paramXmls = new LinkedList<>();
		for (PmmXmlElementConvertable item : paramsDoc.getElementSet()) {
			paramXmls.add((ParamXml) item);
		}

		// Create SBMLDocument for the secondary model
		SBMLDocument secDoc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Composition package
		secDoc.enablePackage(CompConstants.shortLabel);
		// Add SBML comp plugin to the SBMLDocument
		CompSBMLDocumentPlugin docCompPlugin = (CompSBMLDocumentPlugin) secDoc
				.getPlugin(CompConstants.shortLabel);
		addNamespaces(secDoc);

		// Creates model definition
		String modelDefinitionId = Util.createId("model" + estModel.getId());
		Model secModel = docCompPlugin.createModelDefinition(modelDefinitionId);
		if (estModel.getName() != null) {
			secModel.setName(estModel.getName());
		}

		addUnitDefinitions(secModel, dep, indepXmls, paramXmls);

		// Add dep
		Parameter depParam = new Parameter(dep.getName());
		depParam.setConstant(false);
		depParam.setValue(0.0);
		if (dep.getUnit() != null) {
			depParam.setUnits(Util.createId(dep.getUnit()));
		}
		secModel.addParameter(depParam);

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
			secModel.addParameter(indepParam);

			// Add constraint
			Double min = indepXml.getMin(), max = indepXml.getMax();
			LimitsConstraint lc = new LimitsConstraint(indepXml.getName(), min,
					max);
			if (lc.getConstraint() != null) {
				secModel.addConstraint(lc.getConstraint());
			}
		}

		// Add constant parameters
		for (ParamXml paramXml : paramXmls) {
			// Create SBML parameter
			Parameter constParam = new Coefficient(paramXml).getParameter();

			// Assign unit
			if (paramXml.getUnit() == null) {
				constParam.setUnits("dimensionless");
			} else {
				constParam.setUnits(Util.createId(paramXml.getUnit()));
			}
			secModel.addParameter(constParam);

			// Add constraint
			Double min = paramXml.getMin(), max = paramXml.getMax();
			LimitsConstraint lc = new LimitsConstraint(paramXml.getName(), min,
					max);
			if (lc.getConstraint() != null) {
				secModel.addConstraint(lc.getConstraint());
			}
		}

		Model2Rule rule2 = Model2Rule
				.convertCatalogModelXmlToModel2Rule(catModel);
		secModel.addRule(rule2.getRule());

		// Add literature items
		LinkedList<LiteratureItem> lits = new LinkedList<>();
		for (PmmXmlElementConvertable item : litDoc.getElementSet()) {
			lits.add((LiteratureItem) item);
		}

		// Add annotation
		Map<String, String> uncertainties = parseQualityTags(estModel);
		Model2Annotation m2Annot = new Model2Annotation(globalModelId,
				uncertainties, lits);
		secModel.getAnnotation().setNonRDFAnnotation(m2Annot.getNode());

		// Adds annotations for the primary models
		XMLNode metadataNode = m2Annot.getNode();
		for (PrimaryModelWData pmwd : primModels) {
			String modelId = pmwd.getSBMLDoc().getModel().getId();
			XMLTriple triple = new XMLTriple("primaryModel", "", "pmf");
			XMLNode node = new XMLNode(triple);
			node.addChild(new XMLNode(modelId + ".sbml"));
			metadataNode.addChild(node);
		}

		// Creates and return TwoStepSecondaryModel
		TwoStepSecondaryModel model = new TwoStepSecondaryModel(secDoc,
				primModels);
		return model;
	}
}

/**
 * Parse tuples from a table with one step secondary models.
 */
class OneStepSecondaryModelReader extends TableReader {

	List<OneStepSecondaryModel> models = new LinkedList<>();

	public OneStepSecondaryModelReader(List<KnimeTuple> tuples)
			throws URISyntaxException {

		// Sort tuples according to its secondary model
		Map<Integer, List<KnimeTuple>> secMap = new HashMap<>();
		for (KnimeTuple tuple : tuples) {
			// Get secondary EstModelXml
			EstModelXml estModel = (EstModelXml) tuple.getPmmXml(
					Model2Schema.ATT_ESTMODEL).get(0);

			if (secMap.containsKey(estModel.getId())) {
				secMap.get(estModel.getId()).add(tuple);
			} else {
				List<KnimeTuple> ltup = new LinkedList<>();
				ltup.add(tuple);
				secMap.put(estModel.getId(), ltup);
			}
		}

		// For the tuples of every secondary model
		for (List<KnimeTuple> ltup : secMap.values()) {
			OneStepSecondaryModel model = parse(ltup);
			models.add(model);
		}
	}

	public List<OneStepSecondaryModel> getModels() {
		return models;
	}

	private OneStepSecondaryModel parse(List<KnimeTuple> tuples)
			throws URISyntaxException {
		KnimeTuple firstTuple = tuples.get(0);

		// Retrieve TimeSeriesSchema cells
		AgentXml agentXml = (AgentXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_AGENT).get(0);
		MatrixXml matrixXml = (MatrixXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);
		int condId = firstTuple.getInt(TimeSeriesSchema.ATT_CONDID);
		String combaseId = firstTuple.getString(TimeSeriesSchema.ATT_COMBASEID);
		PmmXmlDoc miscDoc = firstTuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
		PmmXmlDoc mdData = firstTuple
				.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);

		// Retrieve Model1Schema cells
		CatalogModelXml catModel = (CatalogModelXml) firstTuple.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estModel = (EstModelXml) firstTuple.getPmmXml(
				Model1Schema.ATT_ESTMODEL).get(0);
		DepXml dep = (DepXml) firstTuple.getPmmXml(Model1Schema.ATT_DEPENDENT)
				.get(0);
		IndepXml indep = (IndepXml) firstTuple.getPmmXml(
				Model1Schema.ATT_INDEPENDENT).get(0);
		PmmXmlDoc paramsDoc = firstTuple.getPmmXml(Model1Schema.ATT_PARAMETER);
		PmmXmlDoc litDoc = firstTuple.getPmmXml(Model1Schema.ATT_EMLIT);

		// Retrieve Model2Schema cells
		CatalogModelXml secCatModel = (CatalogModelXml) firstTuple.getPmmXml(
				Model2Schema.ATT_MODELCATALOG).get(0);
		EstModelXml secEstModel = (EstModelXml) firstTuple.getPmmXml(
				Model2Schema.ATT_ESTMODEL).get(0);
		DepXml secDep = (DepXml) firstTuple.getPmmXml(
				Model2Schema.ATT_DEPENDENT).get(0);
		PmmXmlDoc secIndepDoc = firstTuple
				.getPmmXml(Model2Schema.ATT_INDEPENDENT);
		PmmXmlDoc secParamsDoc = firstTuple
				.getPmmXml(Model2Schema.ATT_PARAMETER);
		PmmXmlDoc secLitDoc = firstTuple.getPmmXml(Model2Schema.ATT_EMLIT);
		int globalModelId = firstTuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);

		SBMLDocument doc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Composition package
		doc.enablePackage(CompConstants.shortLabel);
		CompSBMLDocumentPlugin compDocPlugin = (CompSBMLDocumentPlugin) doc
				.getPlugin(CompConstants.shortLabel);

		String modelId = Util.createId("model" + secEstModel.getId());

		addNamespaces(doc);

		Model model = doc.createModel(modelId);
		if (estModel.getName() != null) {
			model.setName(estModel.getName());
		}
		CompModelPlugin compModelPlugin = (CompModelPlugin) model
				.getPlugin(CompConstants.shortLabel);

		// Annotation
		String modelTitle = estModel.getName();
		Integer modelClassNum = catModel.getModelClass();
		if (modelClassNum == null) {
			modelClassNum = Util.MODELCLASS_NUMS.get("unknown");
		}
		Map<String, String> qualityTags = parseQualityTags(estModel);

		// Get literature references
		List<LiteratureItem> lits = new LinkedList<>();
		for (PmmXmlElementConvertable item : litDoc.getElementSet()) {
			LiteratureItem lit = (LiteratureItem) item;
			lits.add(lit);
		}

		// Add model annotations
		Model1Annotation primModelAnnotation = new Model1Annotation(modelId,
				modelTitle, qualityTags, lits, combaseId, condId);
		model.getAnnotation()
				.setNonRDFAnnotation(primModelAnnotation.getNode());

		// Create a compartment and add it to the model
		Map<String, Double> miscs = new HashMap<>();
		for (PmmXmlElementConvertable item : miscDoc.getElementSet()) {
			MiscXml misc = (MiscXml) item;
			miscs.put(misc.getName(), misc.getValue());
		}

		Matrix matrix = new Matrix(matrixXml, miscs);
		Compartment compartment = matrix.getCompartment();
		model.addCompartment(compartment);

		// Create species and add it to the model
		Agent agent = new Agent(agentXml, dep.getUnit(), compartment);
		model.addSpecies(agent.getSpecies());

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
		for (PmmXmlElementConvertable pmmParam : paramsDoc.getElementSet()) {
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
		addUnitDefinitions(model, dep, indepXmls, constXmls);

		// Create rule of the model and add it to the rest of rules
		Model1Rule model1Rule = Model1Rule.convertCatalogModelXmlToModel1Rule(
				catModel, agent.getSpecies().getId());
		model.addRule(model1Rule.getRule());

		// Create secondary model

		// Get independent parameters
		List<IndepXml> secIndepXmls = new LinkedList<>();
		for (PmmXmlElementConvertable item : secIndepDoc.getElementSet()) {
			secIndepXmls.add((IndepXml) item);
		}

		// Get constant parameters
		List<ParamXml> secConstXmls = new LinkedList<>();
		for (PmmXmlElementConvertable item : secParamsDoc.getElementSet()) {
			secConstXmls.add((ParamXml) item);
		}

		String modelDefinitionId = "model_" + secDep.getName();
		ModelDefinition secModel = new ModelDefinition(modelDefinitionId,
				LEVEL, VERSION);
		if (secEstModel.getName() != null) {
			secModel.setName(secEstModel.getName());
		}

		// Add unit definitions
		addUnitDefinitions(secModel, secDep, secIndepXmls, secConstXmls);

		// Add dep from sec
		Parameter secDepParam = new Parameter(secDep.getName());
		secDepParam.setConstant(false);
		secDepParam.setValue(0.0);
		secModel.addParameter(secDepParam);

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
		List<LiteratureItem> secLits = new LinkedList<>();
		for (PmmXmlElementConvertable item : secLitDoc.getElementSet()) {
			secLits.add((LiteratureItem) item);
		}

		// Add uncertainties
		Map<String, String> uncertainties = parseQualityTags(secEstModel);

		Model2Annotation secModelAnnotation = new Model2Annotation(
				globalModelId, uncertainties, lits);
		secModel.getAnnotation().setNonRDFAnnotation(
				secModelAnnotation.getNode());

		Model2Rule rule2 = Model2Rule
				.convertCatalogModelXmlToModel2Rule(secCatModel);
		secModel.addRule(rule2.getRule());

		compDocPlugin.addModelDefinition(secModel);

		Submodel submodel = compModelPlugin.createSubmodel("submodel");
		submodel.setModelRef(modelDefinitionId);

		// Parse data sets and create NuML documents
		List<NuMLDocument> numlDocs = new LinkedList<>();
		short dataCounter = 0;
		for (KnimeTuple tuple : tuples) {
			if (mdData.size() == 0)
				continue;
			// Create dim
			mdData = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			LinkedHashMap<Double, Double> dim = new LinkedHashMap<>();
			for (PmmXmlElementConvertable origPoint : mdData.getElementSet()) {
				TimeSeriesXml point = (TimeSeriesXml) origPoint;
				dim.put(point.getTime(), point.getConcentration());
			}

			TimeSeriesXml firstPoint = (TimeSeriesXml) mdData.get(0);
			String unit = firstPoint.getConcentrationUnit();

			DataFile dataFile = new DataFile(dim, unit, matrixXml, agentXml,
					dep.getUnit(), new HashMap<String, String>());
			numlDocs.add(dataFile.getDocument());

			// Adds DataSourceNode to the model
			String dataId = String.format("data%d.numl", dataCounter);
			DataSourceNode dsn = new DataSourceNode(dataId);
			secModel.getAnnotation().getNonRDFannotation()
					.addChild(dsn.getNode());

			dataCounter++;
		}

		OneStepSecondaryModel ossm = new OneStepSecondaryModel(doc, numlDocs);
		return ossm;
	}
}

class TwoStepTertiaryModelReader extends TableReader {

	List<TwoStepTertiaryModel> models = new LinkedList<>();

	public TwoStepTertiaryModelReader(List<KnimeTuple> tuples)
			throws URISyntaxException {
		super();

		// Sort global models
		Map<Integer, Map<Integer, List<KnimeTuple>>> gms = sortGlobalModels(tuples);

		// Parse tertiary models
		int modelCounter = 0;
		for (Map<Integer, List<KnimeTuple>> tertiaryInstances : gms.values()) {
			List<List<KnimeTuple>> tuplesList = new LinkedList<>();
			for (List<KnimeTuple> tertiaryInstance : tertiaryInstances.values()) {
				tuplesList.add(tertiaryInstance);
			}
			// We have a list of tertiary instances. Each instance has the same
			// microbial data yet different data. Then we'll create a
			// TwoTertiaryModel from the first instance and create the data from
			// every instance.
			TwoStepTertiaryModel tm = parse(tuplesList, modelCounter);
			models.add(tm);

			modelCounter++;
		}
	}

	public List<TwoStepTertiaryModel> getModels() {
		return models;
	}

	private TwoStepTertiaryModel parse(List<List<KnimeTuple>> tupleList,
			int modelNum) throws URISyntaxException {

		// We'll get microbial data from the first instance
		List<KnimeTuple> firstInstance = tupleList.get(0);
		// and the primary model from the first tuple
		KnimeTuple firstTuple = firstInstance.get(0);

		// Retrieve TimeSeriesSchema cells (all but the data)
		AgentXml agentXml = (AgentXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_AGENT).get(0);
		MatrixXml matrixXml = (MatrixXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);
		int condId = firstTuple.getInt(TimeSeriesSchema.ATT_CONDID);
		String combaseId = firstTuple.getString(TimeSeriesSchema.ATT_COMBASEID);
		PmmXmlDoc miscDoc = firstTuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

		// Retrieve Model1Schema cells
		CatalogModelXml catModel = (CatalogModelXml) firstTuple.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estModel = (EstModelXml) firstTuple.getPmmXml(
				Model1Schema.ATT_ESTMODEL).get(0);
		DepXml dep = (DepXml) firstTuple.getPmmXml(Model1Schema.ATT_DEPENDENT)
				.get(0);
		IndepXml indep = (IndepXml) firstTuple.getPmmXml(
				Model1Schema.ATT_INDEPENDENT).get(0);
		PmmXmlDoc paramsDoc = firstTuple.getPmmXml(Model1Schema.ATT_PARAMETER);
		PmmXmlDoc litDoc = firstTuple.getPmmXml(Model1Schema.ATT_EMLIT);

		// Create SBMLDocument for the tertiary model
		SBMLDocument tertDoc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Compositon package
		tertDoc.enablePackage(CompConstants.shortLabel);
		CompSBMLDocumentPlugin compDocPlugin = (CompSBMLDocumentPlugin) tertDoc
				.getPlugin(CompConstants.shortLabel);

		addNamespaces(tertDoc);

		String modelId = Util.createId("model" + estModel.getId());

		// Creates model and names it
		Model model = tertDoc.createModel(modelId);
		if (estModel.getName() != null) {
			model.setName(estModel.getName());
		}
		CompModelPlugin compModelPlugin = (CompModelPlugin) model
				.getPlugin(CompConstants.shortLabel);

		// Builds and adds model annotation
		// a) Model title
		String modelTitle = estModel.getName();

		// b) Literature references
		List<LiteratureItem> lits = new LinkedList<>();
		for (PmmXmlElementConvertable item : litDoc.getElementSet()) {
			lits.add((LiteratureItem) item);
		}

		// c) Parse quality measures
		Map<String, String> qualityTags = parseQualityTags(estModel);

		// d) Builds annotation
		Model1Annotation m1Annot = new Model1Annotation(modelId, modelTitle,
				qualityTags, lits, combaseId, condId);

		// e) Adds annotation to the model
		model.getAnnotation().setNonRDFAnnotation(m1Annot.getNode());

		// Creates and adds compartment to the model
		// a) Gather misc values
		Map<String, Double> miscs = new HashMap<>();
		for (PmmXmlElementConvertable item : miscDoc.getElementSet()) {
			MiscXml miscXml = (MiscXml) item;
			miscs.put(miscXml.getName(), miscXml.getValue());
		}

		// b) Creates matrix and compartment
		Matrix matrix = new Matrix(matrixXml, miscs);
		Compartment c = matrix.getCompartment();

		// c) Adds compartment to the model
		model.addCompartment(c);

		// Creates species and adds it to the model
		Agent agent = new Agent(agentXml, dep.getUnit(), c);
		model.addSpecies(agent.getSpecies());

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
		for (PmmXmlElementConvertable pmmParam : paramsDoc.getElementSet()) {
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
		addUnitDefinitions(model, dep, indepXmls, constXmls);

		// Create rule of the model and add it to the rest of rules
		Model1Rule model1Rule = Model1Rule.convertCatalogModelXmlToModel1Rule(
				catModel, agent.getSpecies().getId());
		model.addRule(model1Rule.getRule());

		// Add submodels and model definitions
		List<SBMLDocument> secDocs = new LinkedList<>();
		int i = 0;
		for (KnimeTuple tuple : firstInstance) {
			CatalogModelXml secCatModel = (CatalogModelXml) tuple.getPmmXml(
					Model2Schema.ATT_MODELCATALOG).get(0);
			EstModelXml secEstModel = (EstModelXml) tuple.getPmmXml(
					Model2Schema.ATT_ESTMODEL).get(0);
			DepXml secDep = (DepXml) tuple
					.getPmmXml(Model2Schema.ATT_DEPENDENT).get(0);
			PmmXmlDoc secIndepsDoc = tuple
					.getPmmXml(Model2Schema.ATT_INDEPENDENT);
			PmmXmlDoc secParamsDoc = tuple
					.getPmmXml(Model2Schema.ATT_PARAMETER);
			PmmXmlDoc secLitDoc = tuple.getPmmXml(Model2Schema.ATT_EMLIT);
			int globalModelID = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);

			// Get independent parameters
			LinkedList<IndepXml> secIndeps = new LinkedList<>();
			for (PmmXmlElementConvertable item : secIndepsDoc.getElementSet()) {
				secIndeps.add((IndepXml) item);
			}

			// Get constant parameters
			LinkedList<ParamXml> secConsts = new LinkedList<>();
			for (PmmXmlElementConvertable item : secParamsDoc.getElementSet()) {
				secConsts.add((ParamXml) item);
			}

			String modelDefinitionId = "model_" + secDep.getName();
			ModelDefinition secModel = new ModelDefinition(modelDefinitionId,
					LEVEL, VERSION);
			if (secEstModel.getName() != null) {
				secModel.setName(secEstModel.getName());
			}

			// Add unit definitions
			addUnitDefinitions(secModel, secDep, secIndeps, secConsts);

			// Add dep from sec
			Parameter secDepParam = new Parameter(secDep.getName());
			secDepParam.setConstant(false);
			secDepParam.setValue(0.0);
			secModel.addParameter(secDepParam);

			for (IndepXml indepXml : secIndeps) {
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

			for (ParamXml constXml : secConsts) {
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
			List<LiteratureItem> secLits = new LinkedList<>();
			for (PmmXmlElementConvertable item : secLitDoc.getElementSet()) {
				secLits.add((LiteratureItem) item);
			}

			// Add uncertainties
			Map<String, String> uncertainties = parseQualityTags(secEstModel);

			Model2Annotation secModelAnnotation = new Model2Annotation(
					globalModelID, uncertainties, lits);
			secModel.getAnnotation().setNonRDFAnnotation(
					secModelAnnotation.getNode());

			Model2Rule rule2 = Model2Rule
					.convertCatalogModelXmlToModel2Rule(secCatModel);
			secModel.addRule(rule2.getRule());

			compDocPlugin.addModelDefinition(secModel);

			Submodel submodel = compModelPlugin.createSubmodel("submodel"
					+ Integer.toString(i));
			submodel.setModelRef(modelDefinitionId);

			i++;

			// Create SBMLDocument for the secondary model
			SBMLDocument secDoc = new SBMLDocument(LEVEL, VERSION);
			// Enable Hierarchical Composition package
			secDoc.enablePackage(CompConstants.shortLabel);

			CompSBMLDocumentPlugin secDocCompPlugin = (CompSBMLDocumentPlugin) secDoc
					.getPlugin(CompConstants.shortLabel);

			addNamespaces(secDoc);

			// Add model definition to the document
			secDocCompPlugin.addModelDefinition(new ModelDefinition(secModel));

			// Add annotation for the primary model
			XMLNode metadataNode = secModelAnnotation.getNode();
			XMLTriple triple = new XMLTriple("primaryModel", "", "pmf");
			XMLNode node = new XMLNode(triple);
			node.addChild(new XMLNode(modelId + ".sbml"));
			metadataNode.addChild(node);

			// Save secondary model
			secDocs.add(secDoc);
		}

		int dataCounter = 0;
		List<NuMLDocument> numlDocs = new LinkedList<>();
		for (List<KnimeTuple> instance : tupleList) {
			// Get first tuple: All the tuples of an instance have the same data
			KnimeTuple tuple = instance.get(0);

			PmmXmlDoc mdData = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			if (mdData.size() == 0)
				continue;
			// Create dim
			LinkedHashMap<Double, Double> dim = new LinkedHashMap<>();
			for (PmmXmlElementConvertable origPoint : mdData.getElementSet()) {
				TimeSeriesXml point = (TimeSeriesXml) origPoint;
				dim.put(point.getTime(), point.getConcentration());
			}

			TimeSeriesXml firstPoint = (TimeSeriesXml) mdData.get(0);
			String unit = firstPoint.getConcentrationUnit();

			DataFile dataFile = new DataFile(dim, unit, matrixXml, agentXml,
					dep.getUnit(), new HashMap<String, String>());
			numlDocs.add(dataFile.getDocument());

			// Adds DataSourceNode to the model
			String dataId = String.format("data_%d_%d.numl", modelNum,
					dataCounter);
			DataSourceNode dsn = new DataSourceNode(dataId);
			model.getAnnotation().getNonRDFannotation().addChild(dsn.getNode());

			dataCounter++;
		}

		TwoStepTertiaryModel tstm = new TwoStepTertiaryModel(tertDoc, secDocs,
				numlDocs);
		return tstm;
	}
}

/**
 * One Step Fit Tertiary Model
 * 
 * @author Miguel Alba
 */
class OneStepTertiaryModelReader extends TableReader {

	List<OneStepTertiaryModel> models = new LinkedList<>();

	public OneStepTertiaryModelReader(List<KnimeTuple> tuples)
			throws URISyntaxException {
		super();

		// Sort global models
		Map<Integer, Map<Integer, List<KnimeTuple>>> gms = sortGlobalModels(tuples);

		// Parse tertiary models
		int modelCounter = 0;
		for (Map<Integer, List<KnimeTuple>> tertiaryInstances : gms.values()) {
			List<List<KnimeTuple>> tuplesList = new LinkedList<>();
			for (List<KnimeTuple> tertiaryInstance : tertiaryInstances.values()) {
				tuplesList.add(tertiaryInstance);
			}
			// We have a list of tertiary instances. Each instance has the same
			// microbial data yet different data. Then we'll create a
			// TwoTertiaryModel from the first instance and create the data from
			// every instance.
			OneStepTertiaryModel tm = parse(tuplesList, modelCounter);
			models.add(tm);

			modelCounter++;
		}
	}

	public List<OneStepTertiaryModel> getModels() {
		return models;
	}

	private OneStepTertiaryModel parse(List<List<KnimeTuple>> tupleList,
			int modelNum) throws URISyntaxException {
		// We'll get microbial data from the first instance
		List<KnimeTuple> firstInstance = tupleList.get(0);
		// and the primary model from the first tuple
		KnimeTuple firstTuple = firstInstance.get(0);

		// Retrieve TimeSeriesSchema cells (all but the data)
		AgentXml agentXml = (AgentXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_AGENT).get(0);
		MatrixXml matrixXml = (MatrixXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);
		int condId = firstTuple.getInt(TimeSeriesSchema.ATT_CONDID);
		String combaseId = firstTuple.getString(TimeSeriesSchema.ATT_COMBASEID);
		PmmXmlDoc miscDoc = firstTuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

		// Retrieve Model1Schema cells
		CatalogModelXml catModel = (CatalogModelXml) firstTuple.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estModel = (EstModelXml) firstTuple.getPmmXml(
				Model1Schema.ATT_ESTMODEL).get(0);
		DepXml dep = (DepXml) firstTuple.getPmmXml(Model1Schema.ATT_DEPENDENT)
				.get(0);
		IndepXml indep = (IndepXml) firstTuple.getPmmXml(
				Model1Schema.ATT_INDEPENDENT).get(0);
		PmmXmlDoc paramsDoc = firstTuple.getPmmXml(Model1Schema.ATT_PARAMETER);
		PmmXmlDoc litDoc = firstTuple.getPmmXml(Model1Schema.ATT_EMLIT);

		// Create SBMLDocument for the tertiary model
		SBMLDocument tertDoc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Compositon package
		tertDoc.enablePackage(CompConstants.shortLabel);
		CompSBMLDocumentPlugin compDocPlugin = (CompSBMLDocumentPlugin) tertDoc
				.getPlugin(CompConstants.shortLabel);

		addNamespaces(tertDoc);

		String modelId = Util.createId("model" + estModel.getId());

		// Creates model and names it
		Model model = tertDoc.createModel(modelId);
		if (estModel.getName() != null) {
			model.setName(estModel.getName());
		}
		CompModelPlugin compModelPlugin = (CompModelPlugin) model
				.getPlugin(CompConstants.shortLabel);

		// Builds and adds model annotation
		// a) Model title
		String modelTitle = estModel.getName();

		// b) Literature references
		List<LiteratureItem> lits = new LinkedList<>();
		for (PmmXmlElementConvertable item : litDoc.getElementSet()) {
			lits.add((LiteratureItem) item);
		}

		// c) Parse quality measures
		Map<String, String> qualityTags = parseQualityTags(estModel);

		// d) Builds annotation
		Model1Annotation m1Annot = new Model1Annotation(modelId, modelTitle,
				qualityTags, lits, combaseId, condId);

		// e) Adds annotation to the model
		model.getAnnotation().setNonRDFAnnotation(m1Annot.getNode());

		// Creates and adds compartment to the model
		// a) Gather misc values
		Map<String, Double> miscs = new HashMap<>();
		for (PmmXmlElementConvertable item : miscDoc.getElementSet()) {
			MiscXml miscXml = (MiscXml) item;
			miscs.put(miscXml.getName(), miscXml.getValue());
		}

		// b) Creates matrix and compartment
		Matrix matrix = new Matrix(matrixXml, miscs);
		Compartment c = matrix.getCompartment();

		// c) Adds compartment to the model
		model.addCompartment(c);

		// Creates species and adds it to the model
		Agent agent = new Agent(agentXml, dep.getUnit(), c);
		model.addSpecies(agent.getSpecies());

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
		for (PmmXmlElementConvertable pmmParam : paramsDoc.getElementSet()) {
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
		addUnitDefinitions(model, dep, indepXmls, constXmls);

		// Create rule of the model and add it to the rest of rules
		Model1Rule model1Rule = Model1Rule.convertCatalogModelXmlToModel1Rule(
				catModel, agent.getSpecies().getId());
		model.addRule(model1Rule.getRule());

		// Add submodels and model definitions
		List<SBMLDocument> secDocs = new LinkedList<>();
		int i = 0;
		for (KnimeTuple tuple : firstInstance) {
			CatalogModelXml secCatModel = (CatalogModelXml) tuple.getPmmXml(
					Model2Schema.ATT_MODELCATALOG).get(0);
			EstModelXml secEstModel = (EstModelXml) tuple.getPmmXml(
					Model2Schema.ATT_ESTMODEL).get(0);
			DepXml secDep = (DepXml) tuple
					.getPmmXml(Model2Schema.ATT_DEPENDENT).get(0);
			PmmXmlDoc secIndepsDoc = tuple
					.getPmmXml(Model2Schema.ATT_INDEPENDENT);
			PmmXmlDoc secParamsDoc = tuple
					.getPmmXml(Model2Schema.ATT_PARAMETER);
			PmmXmlDoc secLitDoc = tuple.getPmmXml(Model2Schema.ATT_EMLIT);
			int globalModelID = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);

			// Get independent parameters
			LinkedList<IndepXml> secIndeps = new LinkedList<>();
			for (PmmXmlElementConvertable item : secIndepsDoc.getElementSet()) {
				secIndeps.add((IndepXml) item);
			}

			// Get constant parameters
			LinkedList<ParamXml> secConsts = new LinkedList<>();
			for (PmmXmlElementConvertable item : secParamsDoc.getElementSet()) {
				secConsts.add((ParamXml) item);
			}

			String modelDefinitionId = "model_" + secDep.getName();
			ModelDefinition secModel = new ModelDefinition(modelDefinitionId,
					LEVEL, VERSION);
			if (secEstModel.getName() != null) {
				secModel.setName(secEstModel.getName());
			}

			// Add unit definitions
			addUnitDefinitions(secModel, secDep, secIndeps, secConsts);

			// Add dep from sec
			Parameter secDepParam = new Parameter(secDep.getName());
			secDepParam.setConstant(false);
			secDepParam.setValue(0.0);
			secModel.addParameter(secDepParam);

			for (IndepXml indepXml : secIndeps) {
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

			for (ParamXml constXml : secConsts) {
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
			List<LiteratureItem> secLits = new LinkedList<>();
			for (PmmXmlElementConvertable item : secLitDoc.getElementSet()) {
				secLits.add((LiteratureItem) item);
			}

			// Add uncertainties
			Map<String, String> uncertainties = parseQualityTags(secEstModel);

			Model2Annotation secModelAnnotation = new Model2Annotation(
					globalModelID, uncertainties, lits);
			secModel.getAnnotation().setNonRDFAnnotation(
					secModelAnnotation.getNode());

			Model2Rule rule2 = Model2Rule
					.convertCatalogModelXmlToModel2Rule(secCatModel);
			secModel.addRule(rule2.getRule());

			compDocPlugin.addModelDefinition(secModel);

			Submodel submodel = compModelPlugin.createSubmodel("submodel"
					+ Integer.toString(i));
			submodel.setModelRef(modelDefinitionId);

			i++;

			// Create SBMLDocument for the secondary model
			SBMLDocument secDoc = new SBMLDocument(LEVEL, VERSION);
			// Enable Hierarchical Composition package
			secDoc.enablePackage(CompConstants.shortLabel);

			CompSBMLDocumentPlugin secDocCompPlugin = (CompSBMLDocumentPlugin) secDoc
					.getPlugin(CompConstants.shortLabel);

			addNamespaces(secDoc);

			// Add model definition to the document
			secDocCompPlugin.addModelDefinition(new ModelDefinition(secModel));

			// Add annotation for the primary model
			XMLNode metadataNode = secModelAnnotation.getNode();
			XMLTriple triple = new XMLTriple("primaryModel", "", "pmf");
			XMLNode node = new XMLNode(triple);
			node.addChild(new XMLNode(modelId + ".sbml"));
			metadataNode.addChild(node);

			// Save secondary model
			secDocs.add(secDoc);
		}

		int dataCounter = 0;
		List<NuMLDocument> numlDocs = new LinkedList<>();
		for (List<KnimeTuple> instance : tupleList) {
			// Get first tuple: All the tuples of an instance have the same data
			KnimeTuple tuple = instance.get(0);

			PmmXmlDoc mdData = tuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
			if (mdData.size() == 0)
				continue;
			// Create dim
			LinkedHashMap<Double, Double> dim = new LinkedHashMap<>();
			for (PmmXmlElementConvertable origPoint : mdData.getElementSet()) {
				TimeSeriesXml point = (TimeSeriesXml) origPoint;
				dim.put(point.getTime(), point.getConcentration());
			}

			TimeSeriesXml firstPoint = (TimeSeriesXml) mdData.get(0);
			String unit = firstPoint.getConcentrationUnit();

			DataFile dataFile = new DataFile(dim, unit, matrixXml, agentXml,
					dep.getUnit(), new HashMap<String, String>());
			numlDocs.add(dataFile.getDocument());

			// Adds DataSourceNode to the model
			String dataId = String.format("data_%d_%d.numl", modelNum,
					dataCounter);
			DataSourceNode dsn = new DataSourceNode(dataId);

			for (SBMLDocument secDoc : secDocs) {
				CompSBMLDocumentPlugin compPlugin = (CompSBMLDocumentPlugin) secDoc
						.getPlugin(CompConstants.shortLabel);
				compPlugin.getModelDefinition(0).getAnnotation()
						.getNonRDFannotation().addChild(dsn.getNode());
			}

			dataCounter++;
		}

		OneStepTertiaryModel tstm = new OneStepTertiaryModel(tertDoc, secDocs,
				numlDocs);
		return tstm;
	}
}

class ManualTertiaryModelReader extends TableReader {

	List<ManualTertiaryModel> models = new LinkedList<>();

	public ManualTertiaryModelReader(List<KnimeTuple> tuples)
			throws URISyntaxException {
		super();

		// Sort global models
		Map<Integer, Map<Integer, List<KnimeTuple>>> gms = sortGlobalModels(tuples);

		// Parse tertiary models
		int modelCounter = 0;
		for (Map<Integer, List<KnimeTuple>> tertiaryInstances : gms.values()) {
			List<List<KnimeTuple>> tuplesList = new LinkedList<>();
			for (List<KnimeTuple> tertiaryInstance : tertiaryInstances.values()) {
				tuplesList.add(tertiaryInstance);
			}
			// We have a list of tertiary instances. Each instance has the same
			// microbial data yet different data. Then we'll create a
			// TwoTertiaryModel from the first instance and create the data from
			// every instance.
			ManualTertiaryModel tm = parse(tuplesList, modelCounter);
			models.add(tm);

			modelCounter++;
		}
	}

	public List<ManualTertiaryModel> getModels() {
		return models;
	}

	private ManualTertiaryModel parse(List<List<KnimeTuple>> tupleList,
			int modelNum) throws URISyntaxException {
		// We'll get microbial data from the first instance
		List<KnimeTuple> firstInstance = tupleList.get(0);
		// and the primary model from the first tuple
		KnimeTuple firstTuple = firstInstance.get(0);

		// Retrieve TimeSeriesSchema cells (all but the data)
		AgentXml agentXml = (AgentXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_AGENT).get(0);
		MatrixXml matrixXml = (MatrixXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);
		int condId = firstTuple.getInt(TimeSeriesSchema.ATT_CONDID);
		String combaseId = firstTuple.getString(TimeSeriesSchema.ATT_COMBASEID);
		PmmXmlDoc miscDoc = firstTuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

		// Retrieve Model1Schema cells
		CatalogModelXml catModel = (CatalogModelXml) firstTuple.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estModel = (EstModelXml) firstTuple.getPmmXml(
				Model1Schema.ATT_ESTMODEL).get(0);
		DepXml dep = (DepXml) firstTuple.getPmmXml(Model1Schema.ATT_DEPENDENT)
				.get(0);
		IndepXml indep = (IndepXml) firstTuple.getPmmXml(
				Model1Schema.ATT_INDEPENDENT).get(0);
		PmmXmlDoc paramsDoc = firstTuple.getPmmXml(Model1Schema.ATT_PARAMETER);
		PmmXmlDoc litDoc = firstTuple.getPmmXml(Model1Schema.ATT_EMLIT);

		// Create SBMLDocument for the tertiary model
		SBMLDocument tertDoc = new SBMLDocument(LEVEL, VERSION);
		// Enable Hierarchical Compositon package
		tertDoc.enablePackage(CompConstants.shortLabel);
		CompSBMLDocumentPlugin compDocPlugin = (CompSBMLDocumentPlugin) tertDoc
				.getPlugin(CompConstants.shortLabel);

		addNamespaces(tertDoc);

		String modelId = Util.createId("model" + estModel.getId());

		// Creates model and names it
		Model model = tertDoc.createModel(modelId);
		if (estModel.getName() != null) {
			model.setName(estModel.getName());
		}
		CompModelPlugin compModelPlugin = (CompModelPlugin) model
				.getPlugin(CompConstants.shortLabel);

		// Builds and adds model annotation
		// a) Model title
		String modelTitle = estModel.getName();

		// b) Literature references
		List<LiteratureItem> lits = new LinkedList<>();
		for (PmmXmlElementConvertable item : litDoc.getElementSet()) {
			lits.add((LiteratureItem) item);
		}

		// c) Parse quality measures
		Map<String, String> qualityTags = parseQualityTags(estModel);

		// d) Builds annotation
		Model1Annotation m1Annot = new Model1Annotation(modelId, modelTitle,
				qualityTags, lits, combaseId, condId);

		// e) Adds annotation to the model
		model.getAnnotation().setNonRDFAnnotation(m1Annot.getNode());

		// Creates and adds compartment to the model
		// a) Gather misc values
		Map<String, Double> miscs = new HashMap<>();
		for (PmmXmlElementConvertable item : miscDoc.getElementSet()) {
			MiscXml miscXml = (MiscXml) item;
			miscs.put(miscXml.getName(), miscXml.getValue());
		}

		// b) Creates matrix and compartment
		Matrix matrix = new Matrix(matrixXml, miscs);
		Compartment c = matrix.getCompartment();

		// c) Adds compartment to the model
		model.addCompartment(c);

		// Creates species and adds it to the model
		Agent agent = new Agent(agentXml, dep.getUnit(), c);
		model.addSpecies(agent.getSpecies());

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
		for (PmmXmlElementConvertable pmmParam : paramsDoc.getElementSet()) {
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
		addUnitDefinitions(model, dep, indepXmls, constXmls);

		// Create rule of the model and add it to the rest of rules
		Model1Rule model1Rule = Model1Rule.convertCatalogModelXmlToModel1Rule(
				catModel, agent.getSpecies().getId());
		model.addRule(model1Rule.getRule());

		// Add submodels and model definitions
		List<SBMLDocument> secDocs = new LinkedList<>();
		int i = 0;
		for (KnimeTuple tuple : firstInstance) {
			CatalogModelXml secCatModel = (CatalogModelXml) tuple.getPmmXml(
					Model2Schema.ATT_MODELCATALOG).get(0);
			EstModelXml secEstModel = (EstModelXml) tuple.getPmmXml(
					Model2Schema.ATT_ESTMODEL).get(0);
			DepXml secDep = (DepXml) tuple
					.getPmmXml(Model2Schema.ATT_DEPENDENT).get(0);
			PmmXmlDoc secIndepsDoc = tuple
					.getPmmXml(Model2Schema.ATT_INDEPENDENT);
			PmmXmlDoc secParamsDoc = tuple
					.getPmmXml(Model2Schema.ATT_PARAMETER);
			PmmXmlDoc secLitDoc = tuple.getPmmXml(Model2Schema.ATT_EMLIT);
			int globalModelID = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);

			// Get independent parameters
			LinkedList<IndepXml> secIndeps = new LinkedList<>();
			for (PmmXmlElementConvertable item : secIndepsDoc.getElementSet()) {
				secIndeps.add((IndepXml) item);
			}

			// Get constant parameters
			LinkedList<ParamXml> secConsts = new LinkedList<>();
			for (PmmXmlElementConvertable item : secParamsDoc.getElementSet()) {
				secConsts.add((ParamXml) item);
			}

			String modelDefinitionId = "model_" + secDep.getName();
			ModelDefinition secModel = new ModelDefinition(modelDefinitionId,
					LEVEL, VERSION);
			if (secEstModel.getName() != null) {
				secModel.setName(secEstModel.getName());
			}

			// Add unit definitions
			addUnitDefinitions(secModel, secDep, secIndeps, secConsts);

			// Add dep from sec
			Parameter secDepParam = new Parameter(secDep.getName());
			secDepParam.setConstant(false);
			secDepParam.setValue(0.0);
			secModel.addParameter(secDepParam);

			for (IndepXml indepXml : secIndeps) {
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

			for (ParamXml constXml : secConsts) {
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
			List<LiteratureItem> secLits = new LinkedList<>();
			for (PmmXmlElementConvertable item : secLitDoc.getElementSet()) {
				secLits.add((LiteratureItem) item);
			}

			// Add uncertainties
			Map<String, String> uncertainties = parseQualityTags(secEstModel);

			Model2Annotation secModelAnnotation = new Model2Annotation(
					globalModelID, uncertainties, lits);
			secModel.getAnnotation().setNonRDFAnnotation(
					secModelAnnotation.getNode());

			Model2Rule rule2 = Model2Rule
					.convertCatalogModelXmlToModel2Rule(secCatModel);
			secModel.addRule(rule2.getRule());

			compDocPlugin.addModelDefinition(secModel);

			Submodel submodel = compModelPlugin.createSubmodel("submodel"
					+ Integer.toString(i));
			submodel.setModelRef(modelDefinitionId);

			i++;

			// Create SBMLDocument for the secondary model
			SBMLDocument secDoc = new SBMLDocument(LEVEL, VERSION);
			// Enable Hierarchical Composition package
			secDoc.enablePackage(CompConstants.shortLabel);

			CompSBMLDocumentPlugin secDocCompPlugin = (CompSBMLDocumentPlugin) secDoc
					.getPlugin(CompConstants.shortLabel);

			addNamespaces(secDoc);

			// Add model definition to the document
			secDocCompPlugin.addModelDefinition(new ModelDefinition(secModel));

			// Add annotation for the primary model
			XMLNode metadataNode = secModelAnnotation.getNode();
			XMLTriple triple = new XMLTriple("primaryModel", "", "pmf");
			XMLNode node = new XMLNode(triple);
			node.addChild(new XMLNode(modelId + ".sbml"));
			metadataNode.addChild(node);

			// Save secondary model
			secDocs.add(secDoc);
		}

		ManualTertiaryModel mtm = new ManualTertiaryModel(tertDoc, secDocs);
		return mtm;
	}
}
