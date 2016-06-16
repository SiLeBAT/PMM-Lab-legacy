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
import org.sbml.jsbml.Constraint;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.ext.comp.CompConstants;
import org.sbml.jsbml.ext.comp.CompSBMLDocumentPlugin;
import org.sbml.jsbml.ext.comp.ModelDefinition;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.reader.DataTuple;
import de.bund.bfr.knime.pmm.common.reader.Model1Tuple;
import de.bund.bfr.knime.pmm.common.reader.Model2Tuple;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.extendedtable.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.sbml.Limits;
import de.bund.bfr.pmf.sbml.LimitsConstraint;
import de.bund.bfr.pmf.sbml.ModelRule;
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
		return Util.mergeTuples(new DataTuple(doc).getTuple(), new Model1Tuple(doc).getTuple());
	}
}

class TwoStepSecondaryModelHandler extends DocumentHandler {
	
	protected DataTableSpec createSpec() {
		return SchemaFactory.createM2Schema().createSpec();
	}
	
	protected KnimeTuple readModel(SBMLDocument doc) {
		return new Model2Tuple(doc.getModel()).getTuple();
	}
}

class OneStepSecondaryModelHandler extends DocumentHandler {
	
	protected DataTableSpec createSpec() {
		return SchemaFactory.createM12DataSchema().createSpec();
	}
	
	protected KnimeTuple readModel(SBMLDocument doc) {
		// Parses data columns
		final KnimeTuple dataTuple = new DataTuple(doc).getTuple();

		// Parses primary model
		final KnimeTuple m1Tuple = new Model1Tuple(doc).getTuple();

		// Parses secondary model
		CompSBMLDocumentPlugin secCompPlugin = (CompSBMLDocumentPlugin) doc.getPlugin(CompConstants.shortLabel);
		ModelDefinition secModel = secCompPlugin.getModelDefinition(0);
		final KnimeTuple m2Tuple = new Model2Tuple(secModel).getTuple();

		final KnimeTuple row = Util.mergeTuples(dataTuple, m1Tuple, m2Tuple);
		return row;
	}
}

class ManualSecondaryModelHandler extends DocumentHandler {
	
	protected DataTableSpec createSpec() {
		return SchemaFactory.createM2Schema().createSpec();
	}
	
	protected KnimeTuple readModel(SBMLDocument doc) {
		return new Model2Tuple(doc.getModel()).getTuple();
	}
}

class TertiaryModelHandler extends DocumentHandler {
	
	protected DataTableSpec createSpec() {
		return SchemaFactory.createM1DataSchema().createSpec();
	}
	
	protected KnimeTuple readModel(SBMLDocument doc) {
		return Util.mergeTuples(new DataTuple(doc).getTuple(), new Model1Tuple(doc).getTuple());
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

	public static KnimeTuple mergeTuples(final KnimeTuple dataTuple, final KnimeTuple m1Tuple) {
		final KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM1DataSchema());

		// Copies data columns
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
