package de.bund.bfr.knime.pmm.jsondecoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
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

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.jsonutil.JSONModel1;
import de.bund.bfr.knime.pmm.jsonutil.JSONModel2;
import de.bund.bfr.knime.pmm.jsonutil.JSONSchema;
import de.bund.bfr.knime.pmm.jsonutil.JSONTimeSeries;

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
	protected JSONDecoderNodeModel() {
		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		// Get input tuples
		List<KnimeTuple> inTuples = PmmUtilities.getTuples(inData[0],
				new JSONSchema());

		// Find out model type
		JSONObject obj = (JSONObject) JSONValue.parse(inTuples.get(0)
				.getString("Model"));
		boolean isTertiary = obj.containsKey("Model2Schema");

		// Create output container
		BufferedDataContainer container;
		if (isTertiary) {
			KnimeSchema schema = SchemaFactory.createM1DataSchema();
			container = exec.createDataContainer(schema.createSpec());
		} else {
			KnimeSchema schema = SchemaFactory.createM12DataSchema();
			container = exec.createDataContainer(schema.createSpec());
		}

		// Parse models
		if (isTertiary) {
			// TODO: parse tertiary models

			HashMap<String, List<JSONObject>> jsonModelsMap = new HashMap<>();
			for (KnimeTuple inTuple : inTuples) {
				obj = (JSONObject) JSONValue.parse(inTuple.getString("Model"));
				JSONObject m2 = (JSONObject) obj.get("Model2Schema");
				String id = ((JSONObject) m2.get("GlobalModelId")).toString();

				if (jsonModelsMap.containsKey(id)) {
					jsonModelsMap.get(id).add(obj);
				} else {
					List<JSONObject> newModel = new ArrayList<>();
					newModel.add(obj);
					jsonModelsMap.put(id, newModel);
				}

				for (List<JSONObject> jsonModels : jsonModelsMap.values()) {
					JSONObject firstModel = jsonModels.get(0);

					for (JSONObject model : jsonModels) {
						JSONTimeSeries jTS = new JSONTimeSeries(
								(JSONObject) firstModel.get("TimeSeries"));
						JSONModel1 jM1 = new JSONModel1(
								(JSONObject) firstModel.get("Model1Schema"));
						JSONModel2 jM2 = new JSONModel2(
								(JSONObject) model.get("Model2Schema"));

						KnimeTuple tsTuple = jTS.toKnimeTuple();
						KnimeTuple m1Tuple = jM1.toKnimeTuple();
						KnimeTuple m2Tuple = jM2.toKnimeTuple();

						KnimeTuple outTuple = new KnimeTuple(
								SchemaFactory.createM1DataSchema());

						// Add time series columns
						outTuple.setValue(TimeSeriesSchema.ATT_CONDID,
								tsTuple.getInt(TimeSeriesSchema.ATT_CONDID));
						outTuple.setValue(TimeSeriesSchema.ATT_AGENT,
								tsTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT));
						outTuple.setValue(TimeSeriesSchema.ATT_MATRIX,
								tsTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX));
						outTuple.setValue(
								TimeSeriesSchema.ATT_TIMESERIES,
								tsTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES));
						outTuple.setValue(TimeSeriesSchema.ATT_MISC,
								tsTuple.getPmmXml(TimeSeriesSchema.ATT_MISC));
						outTuple.setValue(TimeSeriesSchema.ATT_MDINFO,
								tsTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO));
						outTuple.setValue(TimeSeriesSchema.ATT_LITMD,
								tsTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD));
						outTuple.setValue(TimeSeriesSchema.ATT_DBUUID,
								tsTuple.getString(TimeSeriesSchema.ATT_DBUUID));

						// Add model1 columns
						outTuple.setValue(
								Model1Schema.ATT_MODELCATALOG,
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
						outTuple.setValue(
								Model1Schema.ATT_DATABASEWRITABLE,
								m1Tuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));
						outTuple.setValue(Model1Schema.ATT_DBUUID,
								m1Tuple.getString(Model1Schema.ATT_DBUUID));

						// Add model2 columns
						outTuple.setValue(
								Model2Schema.ATT_MODELCATALOG,
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
						outTuple.setValue(
								Model2Schema.ATT_DATABASEWRITABLE,
								m2Tuple.getInt(Model2Schema.ATT_DATABASEWRITABLE));
						outTuple.setValue(Model2Schema.ATT_DBUUID,
								m2Tuple.getString(Model2Schema.ATT_DBUUID));
						outTuple.setValue(
								Model2Schema.ATT_GLOBAL_MODEL_ID,
								m2Tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID));

						container.addRowToTable(outTuple);
					}
				}
			}

		} else {
			for (KnimeTuple tuple : inTuples) {
				obj = (JSONObject) JSONValue.parse(tuple.getString("Model"));

				JSONTimeSeries jTS = new JSONTimeSeries(
						(JSONObject) obj.get("TimeSeries"));
				JSONModel1 jM1 = new JSONModel1(
						(JSONObject) obj.get("Model1Schema"));

				KnimeTuple tsTuple = jTS.toKnimeTuple();
				KnimeTuple m1Tuple = jM1.toKnimeTuple();

				KnimeTuple outTuple = new KnimeTuple(
						SchemaFactory.createM1DataSchema());

				// Add time series columns
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

				// Add model1 columns
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

				container.addRowToTable(outTuple);
			}
		}

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
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {
		return new DataTableSpec[] { null };
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