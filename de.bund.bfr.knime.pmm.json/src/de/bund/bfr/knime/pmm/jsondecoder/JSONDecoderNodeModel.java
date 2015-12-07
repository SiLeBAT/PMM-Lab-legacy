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
package de.bund.bfr.knime.pmm.jsondecoder;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.flowvariable.FlowVariablePortObject;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.jsonutil.JSONModel1;
import de.bund.bfr.knime.pmm.jsonutil.JSONModel2;
import de.bund.bfr.knime.pmm.jsonutil.JSONTimeSeries;
import de.bund.bfr.knime.pmm.jsonutil.ModelType;

/**
 * This is the model implementation of JSONDecoder. Turns a JSON table into a
 * PMM Lab table.
 * 
 * @author Miguel Alba
 */
public class JSONDecoderNodeModel extends NodeModel {

	/**
	 * Constructor for the node model.
	 */
	protected static final PortType[] inPortTypes = { FlowVariablePortObject.TYPE };
	protected static final PortType[] outPortTypes = { BufferedDataTable.TYPE };

	protected JSONDecoderNodeModel() {
		super(inPortTypes, outPortTypes);
	}

	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		String modelsString = getAvailableInputFlowVariables().get("models")
				.getStringValue();
		JSONArray models = (JSONArray) JSONValue.parse(modelsString);

		// Find out model type
		JSONObject jo = (JSONObject) models.get(0);
		de.bund.bfr.knime.pmm.jsonutil.ModelType modelType;
		if (jo.containsKey("TimeSeriesSchema")
				&& jo.containsKey("Model1Schema")
				&& jo.containsKey("Model2Schema")) {
			modelType = de.bund.bfr.knime.pmm.jsonutil.ModelType.TERTIARY;
		} else if (jo.containsKey("TimeSeriesSchema")
				&& jo.containsKey("Model1Schema")) {
			modelType = ModelType.PRIMARY;
		} else {
			modelType = ModelType.SECONDARY;
		}

		// Create output container
		BufferedDataContainer container = null;
		if (modelType == ModelType.TERTIARY) {
			KnimeSchema schema = SchemaFactory.createM12DataSchema();
			container = exec.createDataContainer(schema.createSpec());
		} else if (modelType == ModelType.PRIMARY) {
			KnimeSchema schema = SchemaFactory.createM1DataSchema();
			container = exec.createDataContainer(schema.createSpec());
		} else if (modelType == ModelType.SECONDARY) {
			KnimeSchema schema = SchemaFactory.createM2Schema();
			container = exec.createDataContainer(schema.createSpec());
		}

		// Create threads for each tuple
		List<Thread> threads = new LinkedList<>();

		// Parse primary models
		if (modelType == ModelType.PRIMARY) {
			for (int i = 0; i < models.size(); i++) {
				JSONObject model = (JSONObject) models.get(i);
				String modelString = model.toJSONString();
				threads.add(new Thread(new PrimaryModelParser(modelString)));
			}
		}

		// Parse secondary models
		else if (modelType == ModelType.SECONDARY) {
			for (int i = 0; i < models.size(); i++) {
				JSONObject model = (JSONObject) models.get(i);
				String modelString = model.toJSONString();
				threads.add(new Thread(new SecondaryModelParser(modelString)));
			}
		}

		// Parse tertiary models
		else if (modelType == ModelType.TERTIARY) {
			for (int i = 0; i < models.size(); i++) {
				JSONObject model = (JSONObject) models.get(i);
				String modelString = model.toJSONString();
				threads.add(new Thread(new TertiaryModelParser(modelString)));
			}
		}

		// Start threads
		for (Thread thread : threads) {
			thread.start();
		}

		// Join threads
		int counter = 0;
		for (Thread thread : threads) {
			thread.join();
			counter++;
			exec.setProgress((float) counter / threads.size());
		}

		// Add tuples to table
		for (KnimeTuple tuple : ModelTuples.getModelTuples().getTuples()) {
			container.addRowToTable(tuple);
		}

		// clear model tuples (otherwise they'll be kept on the next run)
		ModelTuples.getModelTuples().clear();

		// close container and return its table
		container.close();
		BufferedDataTable[] table = { container.getTable() };
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
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {
		return new PortObjectSpec[] { null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
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

class ModelTuples {
	private static ModelTuples m = new ModelTuples();
	private List<KnimeTuple> tuples = new LinkedList<>();

	private ModelTuples() {
	}

	public static ModelTuples getModelTuples() {
		return m;
	}

	public synchronized void addTuple(KnimeTuple tuple) {
		tuples.add(tuple);
	}

	public List<KnimeTuple> getTuples() {
		return tuples;
	}

	public void clear() {
		tuples.clear();
	}
}

class PrimaryModelParser implements Runnable {
	String modelString;

	public PrimaryModelParser(String modelString) {
		this.modelString = modelString;
	}

	public void run() {
		// Parse model string
		KnimeTuple tuple = parse(modelString);

		// Add tuple
		ModelTuples mt = ModelTuples.getModelTuples();
		mt.addTuple(tuple);
	}

	private KnimeTuple parse(String modelString) {
		JSONObject obj = (JSONObject) JSONValue.parse(modelString);

		// Parse JSONTimeSeries from JSON object
		JSONTimeSeries jTS = new JSONTimeSeries(
				(JSONObject) obj.get("TimeSeriesSchema"));
		KnimeTuple tsTuple = jTS.toKnimeTuple();

		// Parse JSONModel1 from JSON object
		JSONModel1 jM1 = new JSONModel1((JSONObject) obj.get("Model1Schema"));
		KnimeTuple m1Tuple = jM1.toKnimeTuple();

		// Build new tuple called outTuple
		KnimeTuple outTuple = new KnimeTuple(SchemaFactory.createM1DataSchema());

		// Add time series columns to outTuple
		outTuple.setValue(TimeSeriesSchema.ATT_CONDID,
				tsTuple.getInt(TimeSeriesSchema.ATT_CONDID));
		outTuple.setValue(TimeSeriesSchema.ATT_AGENT,
				tsTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
		outTuple.setValue(TimeSeriesSchema.ATT_MATRIX,
				tsTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
		outTuple.setValue(TimeSeriesSchema.ATT_TIMESERIES,
				tsTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
		outTuple.setValue(TimeSeriesSchema.ATT_MISC,
				tsTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
		outTuple.setValue(TimeSeriesSchema.ATT_MDINFO,
				tsTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
		outTuple.setValue(TimeSeriesSchema.ATT_LITMD,
				tsTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
		outTuple.setValue(TimeSeriesSchema.ATT_DBUUID,
				tsTuple.getString(TimeSeriesSchema.ATT_DBUUID));

		// Add model1 columns to outTuple
		outTuple.setValue(Model1Schema.ATT_MODELCATALOG,
				m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
		outTuple.setValue(Model1Schema.ATT_DEPENDENT,
				m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
		outTuple.setValue(Model1Schema.ATT_INDEPENDENT,
				m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
		outTuple.setValue(Model1Schema.ATT_PARAMETER,
				m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
		outTuple.setValue(Model1Schema.ATT_ESTMODEL,
				m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
		outTuple.setValue(Model1Schema.ATT_MLIT,
				m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
		outTuple.setValue(Model1Schema.ATT_EMLIT,
				m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
		outTuple.setValue(Model1Schema.ATT_DATABASEWRITABLE,
				m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
		outTuple.setValue(Model1Schema.ATT_DBUUID,
				m1Tuple.getString(Model1Schema.ATT_DBUUID));

		return outTuple;
	}
}

class SecondaryModelParser implements Runnable {

	String modelString;

	public SecondaryModelParser(String modelString) {
		this.modelString = modelString;
	}

	public void run() {
		// Parse model string
		KnimeTuple tuple = parse(modelString);

		// Add tuples
		ModelTuples mt = ModelTuples.getModelTuples();
		mt.addTuple(tuple);
	}

	private KnimeTuple parse(String modelString) {
		JSONObject obj = (JSONObject) JSONValue.parse(modelString);

		// Parse JSONModel2 from JSONObject
		JSONModel2 jM2 = new JSONModel2((JSONObject) obj.get("Model2Schema"));
		KnimeTuple tuple = jM2.toKnimeTuple();
		return tuple;
	}
}

class TertiaryModelParser implements Runnable {
	String modelString;

	public TertiaryModelParser(String modelString) {
		this.modelString = modelString;
	}

	public void run() {
		// Parse model string
		List<KnimeTuple> tuples = parse(modelString);

		// Add tuples
		ModelTuples mt = ModelTuples.getModelTuples();
		for (KnimeTuple tuple : tuples) {
			mt.addTuple(tuple);
		}
	}

	private List<KnimeTuple> parse(String modelString) {

		List<KnimeTuple> modelTuples = new LinkedList<>();
		JSONObject obj = (JSONObject) JSONValue.parse(modelString);

		// Parse JSONTimeSeries from modelString
		JSONTimeSeries jTS = new JSONTimeSeries(
				(JSONObject) obj.get("TimeSeriesSchema"));
		KnimeTuple tsTuple = jTS.toKnimeTuple();

		// Parse JSONModel1 from modelString
		JSONModel1 jM1 = new JSONModel1((JSONObject) obj.get("Model1Schema"));
		KnimeTuple m1Tuple = jM1.toKnimeTuple();

		// Get secondary models from modelString
		JSONArray secModels = (JSONArray) obj.get("Model2Schema");
		for (int i = 0; i < secModels.size(); i++) {
			// Parse JSONModel from secModel
			JSONModel2 jM2 = new JSONModel2((JSONObject) secModels.get(i));
			KnimeTuple m2Tuple = jM2.toKnimeTuple();

			// Build new tuple called outTuple
			KnimeTuple outTuple = new KnimeTuple(
					SchemaFactory.createM12DataSchema());

			// Add time series columns to outTuple
			outTuple.setValue(TimeSeriesSchema.ATT_CONDID,
					tsTuple.getInt(TimeSeriesSchema.ATT_CONDID));
			outTuple.setValue(TimeSeriesSchema.ATT_AGENT,
					tsTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
			outTuple.setValue(TimeSeriesSchema.ATT_MATRIX,
					tsTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
			outTuple.setValue(TimeSeriesSchema.ATT_TIMESERIES,
					tsTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
			outTuple.setValue(TimeSeriesSchema.ATT_MISC,
					tsTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
			outTuple.setValue(TimeSeriesSchema.ATT_MDINFO,
					tsTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
			outTuple.setValue(TimeSeriesSchema.ATT_LITMD,
					tsTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
			outTuple.setValue(TimeSeriesSchema.ATT_DBUUID,
					tsTuple.getString(TimeSeriesSchema.ATT_DBUUID));

			// Add model1 columns to outTuple
			outTuple.setValue(Model1Schema.ATT_MODELCATALOG,
					m1Tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG));
			outTuple.setValue(Model1Schema.ATT_DEPENDENT,
					m1Tuple.getPmmXml(Model1Schema.ATT_DEPENDENT));
			outTuple.setValue(Model1Schema.ATT_INDEPENDENT,
					m1Tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT));
			outTuple.setValue(Model1Schema.ATT_PARAMETER,
					m1Tuple.getPmmXml(Model1Schema.ATT_PARAMETER));
			outTuple.setValue(Model1Schema.ATT_ESTMODEL,
					m1Tuple.getPmmXml(Model1Schema.ATT_ESTMODEL));
			outTuple.setValue(Model1Schema.ATT_MLIT,
					m1Tuple.getPmmXml(Model1Schema.ATT_MLIT));
			outTuple.setValue(Model1Schema.ATT_EMLIT,
					m1Tuple.getPmmXml(Model1Schema.ATT_EMLIT));
			outTuple.setValue(Model1Schema.ATT_DATABASEWRITABLE,
					m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
			outTuple.setValue(Model1Schema.ATT_DBUUID,
					m1Tuple.getString(Model1Schema.ATT_DBUUID));

			// Add model2 columns to outTuple
			outTuple.setValue(Model2Schema.ATT_MODELCATALOG,
					m2Tuple.getPmmXml(Model2Schema.ATT_MODELCATALOG));
			outTuple.setValue(Model2Schema.ATT_DEPENDENT,
					m2Tuple.getPmmXml(Model2Schema.ATT_DEPENDENT));
			outTuple.setValue(Model2Schema.ATT_INDEPENDENT,
					m2Tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT));
			outTuple.setValue(Model2Schema.ATT_PARAMETER,
					m2Tuple.getPmmXml(Model2Schema.ATT_PARAMETER));
			outTuple.setValue(Model2Schema.ATT_MLIT,
					m2Tuple.getPmmXml(Model2Schema.ATT_MLIT));
			outTuple.setValue(Model2Schema.ATT_EMLIT,
					m2Tuple.getPmmXml(Model2Schema.ATT_EMLIT));
			outTuple.setValue(Model2Schema.ATT_DATABASEWRITABLE,
					m2Tuple.getInt(Model2Schema.ATT_DATABASEWRITABLE));
			outTuple.setValue(Model2Schema.ATT_DBUUID,
					m2Tuple.getString(Model2Schema.ATT_DBUUID));
			outTuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID,
					m2Tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID));

			// Add outTuple
			modelTuples.add(outTuple);
		}

		return modelTuples;
	}
}
