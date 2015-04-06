package de.bund.bfr.knime.pmm.jsonencoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

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
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
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
 * This is the model implementation of JSONEncoder. Turns a PMM Lab table into
 * JSON
 * 
 * @author JSON Encoder
 */
public class JSONEncoderNodeModel extends NodeModel {

	/**
	 * Constructor for the node model.
	 */
	protected JSONEncoderNodeModel() {
		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		// Create output container
		BufferedDataContainer container = exec
				.createDataContainer(new JSONSchema().createSpec());

		// Get input table spec
		DataTableSpec inTableSpec = inData[0].getDataTableSpec();

		if (SchemaFactory.createM12DataSchema().conforms(inTableSpec)) {
			// Retrieve input tuples
			List<KnimeTuple> tuples = PmmUtilities.getTuples(inData[0],
					SchemaFactory.createM12DataSchema());

			// Get secondary models for every tertiary model
			Map<String, List<KnimeTuple>> tuplesMap = new HashMap<>();
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

			int counter = 0;
			for (List<KnimeTuple> modelTuples : tuplesMap.values()) {
				// Get first tuple
				KnimeTuple firstTuple = modelTuples.get(0);

				// Get time series columns from firstTuple
				Integer condId = (Integer) firstTuple
						.getInt(TimeSeriesSchema.ATT_CONDID);
				String combaseId = (String) firstTuple
						.getString(TimeSeriesSchema.ATT_DBUUID);
				AgentXml organism = (AgentXml) firstTuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0);
				MatrixXml matrix = (MatrixXml) firstTuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0);

				// Time series
				PmmXmlDoc dataDoc = (PmmXmlDoc) firstTuple
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
				List<TimeSeriesXml> data = new ArrayList<>();
				for (PmmXmlElementConvertable dataItem : dataDoc
						.getElementSet()) {
					data.add((TimeSeriesXml) dataItem);
				}

				// Misc items
				PmmXmlDoc miscDoc = (PmmXmlDoc) firstTuple
						.getPmmXml(TimeSeriesSchema.ATT_MISC);
				List<MiscXml> miscList = new ArrayList<>();
				for (PmmXmlElementConvertable miscItem : miscDoc
						.getElementSet()) {
					miscList.add((MiscXml) miscItem);
				}

				// Model info
				MdInfoXml mdInfo = (MdInfoXml) firstTuple.getPmmXml(
						TimeSeriesSchema.ATT_MDINFO).get(0);
				String dbuuid = (String) firstTuple
						.getString(TimeSeriesSchema.ATT_DBUUID);

				// Literature items
				PmmXmlDoc litDoc = (PmmXmlDoc) firstTuple
						.getPmmXml(TimeSeriesSchema.ATT_LITMD);
				List<LiteratureItem> lits = new ArrayList<>();
				for (PmmXmlElementConvertable litItem : litDoc.getElementSet()) {
					lits.add((LiteratureItem) litItem);
				}

				JSONTimeSeries ts = new JSONTimeSeries(condId, combaseId,
						organism, matrix, data, miscList, mdInfo, lits, dbuuid);

				// Model1 columns
				CatalogModelXml catModel = (CatalogModelXml) firstTuple
						.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0);
				DepXml dep = (DepXml) firstTuple.getPmmXml(
						Model1Schema.ATT_DEPENDENT).get(0);
				IndepXml indep = (IndepXml) firstTuple.getPmmXml(
						Model1Schema.ATT_INDEPENDENT).get(0);

				PmmXmlDoc paramsDoc = (PmmXmlDoc) firstTuple
						.getPmmXml(Model1Schema.ATT_PARAMETER);
				List<ParamXml> params = new ArrayList<>();
				for (PmmXmlElementConvertable paramItem : paramsDoc
						.getElementSet()) {
					params.add((ParamXml) paramItem);
				}

				EstModelXml modelXml = (EstModelXml) firstTuple.getPmmXml(
						Model1Schema.ATT_ESTMODEL).get(0);

				// M_Literature items
				PmmXmlDoc mlitDoc = (PmmXmlDoc) firstTuple
						.getPmmXml(Model1Schema.ATT_MLIT);
				List<LiteratureItem> mLits = new ArrayList<>();
				for (PmmXmlElementConvertable litItem : mlitDoc.getElementSet()) {
					mLits.add((LiteratureItem) litItem);
				}

				// EM_Literature items
				PmmXmlDoc emlitDoc = (PmmXmlDoc) firstTuple
						.getPmmXml(Model1Schema.ATT_EMLIT);
				List<LiteratureItem> emLits = new ArrayList<>();
				for (PmmXmlElementConvertable litItem : emlitDoc
						.getElementSet()) {
					emLits.add((LiteratureItem) litItem);
				}

				Integer databaseWritable = firstTuple
						.getInt(Model1Schema.ATT_DATABASEWRITABLE);
				dbuuid = (String) firstTuple.getString(Model1Schema.ATT_DBUUID);

				JSONModel1 m1 = new JSONModel1(catModel, dep, indep, params,
						modelXml, mLits, emLits, databaseWritable, dbuuid);

				for (KnimeTuple secTuple : modelTuples) {
					// Get Model2 columns from secTuple
					catModel = (CatalogModelXml) secTuple.getPmmXml(
							Model2Schema.ATT_MODELCATALOG).get(0);
					dep = (DepXml) secTuple.getPmmXml(
							Model2Schema.ATT_DEPENDENT).get(0);
					indep = (IndepXml) secTuple.getPmmXml(
							Model2Schema.ATT_INDEPENDENT).get(0);

					paramsDoc = (PmmXmlDoc) secTuple
							.getPmmXml(Model2Schema.ATT_PARAMETER);
					params = new ArrayList<>();
					for (PmmXmlElementConvertable paramItem : paramsDoc
							.getElementSet()) {
						params.add((ParamXml) paramItem);
					}

					modelXml = (EstModelXml) secTuple.getPmmXml(
							Model2Schema.ATT_ESTMODEL).get(0);

					// M_Literature items
					mlitDoc = (PmmXmlDoc) secTuple
							.getPmmXml(Model2Schema.ATT_MLIT);
					mLits = new ArrayList<>();
					for (PmmXmlElementConvertable litItem : mlitDoc
							.getElementSet()) {
						mLits.add((LiteratureItem) litItem);
					}

					// EM_Literature items
					emlitDoc = (PmmXmlDoc) secTuple
							.getPmmXml(Model2Schema.ATT_EMLIT);
					emLits = new ArrayList<>();
					for (PmmXmlElementConvertable litItem : emlitDoc
							.getElementSet()) {
						emLits.add((LiteratureItem) litItem);
					}

					databaseWritable = secTuple
							.getInt(Model2Schema.ATT_DATABASEWRITABLE);
					dbuuid = secTuple.getString(Model2Schema.ATT_DBUUID);
					Integer globalModelID = secTuple
							.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);

					JSONModel2 m2 = new JSONModel2(catModel, dep, indep,
							params, modelXml, mLits, emLits, databaseWritable,
							dbuuid, globalModelID);

					JSONObject obj = new JSONObject();
					obj.put("TimeSeries", ts.getObj());
					obj.put("Model1Schema", m1.getObj());
					obj.put("Model2Schema", m2.getObj());

					DataCell[] cells = new DataCell[1];
					cells[0] = new StringCell(obj.toJSONString());

					DataRow row = new DefaultRow(Integer.toString(counter),
							cells);
					container.addRowToTable(row);
				}
				counter++; // Increment counter
				// Update progress bar
				exec.setProgress((float) counter / modelTuples.size());
			}
		}

		else if (SchemaFactory.createM1DataSchema().conforms(inTableSpec)) {
			// Retrieve input tuples
			List<KnimeTuple> tuples = PmmUtilities.getTuples(inData[0],
					SchemaFactory.createM1DataSchema());

			// Each KNIME tuple is a primary model
			int counter = 0;
			for (KnimeTuple tuple : tuples) {
				// Get time series columns
				Integer condId = (Integer) tuple
						.getInt(TimeSeriesSchema.ATT_CONDID);
				String combaseId = (String) tuple
						.getString(TimeSeriesSchema.ATT_DBUUID);
				AgentXml organism = (AgentXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0);
				MatrixXml matrix = (MatrixXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0);

				// Time series
				PmmXmlDoc dataDoc = (PmmXmlDoc) tuple
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
				List<TimeSeriesXml> data = new ArrayList<>();
				for (PmmXmlElementConvertable dataItem : dataDoc
						.getElementSet()) {
					data.add((TimeSeriesXml) dataItem);
				}

				// Misc items
				PmmXmlDoc miscDoc = (PmmXmlDoc) tuple
						.getPmmXml(TimeSeriesSchema.ATT_MISC);
				List<MiscXml> miscList = new ArrayList<>();
				for (PmmXmlElementConvertable miscItem : miscDoc
						.getElementSet()) {
					miscList.add((MiscXml) miscItem);
				}

				// Model info
				MdInfoXml mdInfo = (MdInfoXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MDINFO).get(0);
				String dbuuid = (String) tuple
						.getString(TimeSeriesSchema.ATT_DBUUID);

				// Literature items
				PmmXmlDoc litDoc = (PmmXmlDoc) tuple
						.getPmmXml(TimeSeriesSchema.ATT_LITMD);
				List<LiteratureItem> lits = new ArrayList<>();
				for (PmmXmlElementConvertable litItem : litDoc.getElementSet()) {
					lits.add((LiteratureItem) litItem);
				}

				JSONTimeSeries ts = new JSONTimeSeries(condId, combaseId,
						organism, matrix, data, miscList, mdInfo, lits, dbuuid);

				// Model1 columns
				CatalogModelXml catModel = (CatalogModelXml) tuple.getPmmXml(
						Model1Schema.ATT_MODELCATALOG).get(0);
				DepXml dep = (DepXml) tuple.getPmmXml(
						Model1Schema.ATT_DEPENDENT).get(0);
				IndepXml indep = (IndepXml) tuple.getPmmXml(
						Model1Schema.ATT_INDEPENDENT).get(0);

				PmmXmlDoc paramsDoc = (PmmXmlDoc) tuple
						.getPmmXml(Model1Schema.ATT_PARAMETER);
				List<ParamXml> params = new ArrayList<>();
				for (PmmXmlElementConvertable paramItem : paramsDoc
						.getElementSet()) {
					params.add((ParamXml) paramItem);
				}

				EstModelXml modelXml = (EstModelXml) tuple.getPmmXml(
						Model1Schema.ATT_ESTMODEL).get(0);

				// M_Literature items
				PmmXmlDoc mlitDoc = (PmmXmlDoc) tuple
						.getPmmXml(Model1Schema.ATT_MLIT);
				List<LiteratureItem> mLits = new ArrayList<>();
				for (PmmXmlElementConvertable litItem : mlitDoc.getElementSet()) {
					mLits.add((LiteratureItem) litItem);
				}

				// EM_Literature items
				PmmXmlDoc emlitDoc = (PmmXmlDoc) tuple
						.getPmmXml(Model1Schema.ATT_EMLIT);
				List<LiteratureItem> emLits = new ArrayList<>();
				for (PmmXmlElementConvertable litItem : emlitDoc
						.getElementSet()) {
					emLits.add((LiteratureItem) litItem);
				}

				Integer databaseWritable = tuple
						.getInt(Model1Schema.ATT_DATABASEWRITABLE);
				dbuuid = tuple.getString(Model1Schema.ATT_DBUUID);

				JSONModel1 m1 = new JSONModel1(catModel, dep, indep, params,
						modelXml, mLits, emLits, databaseWritable, dbuuid);

				JSONObject obj = new JSONObject();
				obj.put("TimeSeries", ts.getObj());
				obj.put("Model1Schema", m1.getObj());

				DataCell[] cells = new DataCell[1];
				cells[0] = new StringCell(obj.toJSONString());

				DataRow row = new DefaultRow(Integer.toString(counter), cells);
				container.addRowToTable(row);

				counter++; // Increment counter
				// Update progress bar
				exec.setProgress((float)counter / tuples.size());
			}
		}

		// Close container and return table
		container.close();
		return new BufferedDataTable[] { container.getTable() };
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