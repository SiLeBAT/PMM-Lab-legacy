package de.bund.bfr.knime.pmm.jsonencoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.flowvariable.FlowVariablePortObject;

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
import de.bund.bfr.knime.pmm.jsonutil.JSONTimeSeries;

/**
 * This is the model implementation of JSONEncoder. Turns a PMM Lab table into
 * JSON
 * 
 * @author Miguel Alba
 */
public class JSONEncoderNodeModel extends NodeModel {

	/**
	 * Constructor for the node model.
	 */
	protected static final PortType[] inPortTypes = { BufferedDataTable.TYPE };
	protected static final PortType[] outPortTypes = { FlowVariablePortObject.TYPE };

	protected JSONEncoderNodeModel() {
		super(inPortTypes, outPortTypes);
	}

	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		// Get input table spec
		BufferedDataTable inTable = (BufferedDataTable) inData[0];
		DataTableSpec inTableSpec = inTable.getDataTableSpec();

		// Check model type
		boolean isTertiary = SchemaFactory.createM12DataSchema().conforms(
				inTableSpec);

		List<Thread> threads = new LinkedList<>();
		// Parse tertiary models
		if (isTertiary) {
			// Retrieve input tuples
			List<KnimeTuple> tuples = PmmUtilities.getTuples(inTable,
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

			// Create threads for the tuples of each tertiary model
			for (List<KnimeTuple> modelTuples : tuplesMap.values()) {
				threads.add(new Thread(new TertiaryTupleReader(modelTuples)));
			}
		}

		else {
			// Retrieve input tuples
			List<KnimeTuple> tuples = PmmUtilities.getTuples(inTable,
					SchemaFactory.createM1DataSchema());

			// Each KNIME tuple is a primary model
			// Create threads for every tuple
			for (KnimeTuple tuple : tuples) {
				threads.add(new Thread(new PrimaryTupleParser(tuple)));
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

		// Push model strings into flow variable ports
		counter = 0;
		ModelStrings ms = ModelStrings.getModelStrings();
		for (String modelString : ms.getModels()) {
			pushFlowVariableString(Integer.toString(counter), modelString);
			counter++;
		}

		// clear model strings (otherwise they'll be kept on the next run)
		ms.clear();

		return new PortObject[] { FlowVariablePortObject.INSTANCE };
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

class ModelStrings {
	private static ModelStrings ms = new ModelStrings();
	private List<String> models = new LinkedList<>();

	private ModelStrings() {
	}

	public static ModelStrings getModelStrings() {
		return ms;
	}

	public synchronized void addModel(String model) {
		models.add(model);
	}

	public List<String> getModels() {
		return models;
	}

	public void clear() {
		models.clear();
	}
}

class PrimaryTupleParser implements Runnable {

	KnimeTuple tuple;

	public PrimaryTupleParser(KnimeTuple tuple) {
		this.tuple = tuple;
	}

	public void run() {
		// Parse primary tuple
		String model = parse(tuple);

		// Add model
		ModelStrings ms = ModelStrings.getModelStrings();
		ms.addModel(model);
	}

	@SuppressWarnings("unchecked")
	private String parse(KnimeTuple tuple) {
		// Get time series columns
		Integer condId = (Integer) tuple.getInt(TimeSeriesSchema.ATT_CONDID);
		String combaseId = (String) tuple
				.getString(TimeSeriesSchema.ATT_DBUUID);
		AgentXml organism = (AgentXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_AGENT).get(0);
		MatrixXml matrix = (MatrixXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);

		PmmXmlDoc dataDoc = (PmmXmlDoc) tuple
				.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
		List<TimeSeriesXml> data = new LinkedList<>();
		for (PmmXmlElementConvertable dataItem : dataDoc.getElementSet()) {
			data.add((TimeSeriesXml) dataItem);
		}

		PmmXmlDoc miscDoc = (PmmXmlDoc) tuple
				.getPmmXml(TimeSeriesSchema.ATT_MISC);
		List<MiscXml> miscList = new LinkedList<>();
		for (PmmXmlElementConvertable miscItem : miscDoc.getElementSet()) {
			miscList.add((MiscXml) miscItem);
		}

		MdInfoXml mdInfo = (MdInfoXml) tuple.getPmmXml(
				TimeSeriesSchema.ATT_MDINFO).get(0);
		String dbuuid = (String) tuple.getString(TimeSeriesSchema.ATT_DBUUID);

		PmmXmlDoc litDoc = (PmmXmlDoc) tuple
				.getPmmXml(TimeSeriesSchema.ATT_LITMD);
		List<LiteratureItem> lits = new LinkedList<>();
		for (PmmXmlElementConvertable litItem : litDoc.getElementSet()) {
			lits.add((LiteratureItem) litItem);
		}

		// Create JSONTimeSeries
		JSONTimeSeries ts = new JSONTimeSeries(condId, combaseId, organism,
				matrix, data, miscList, mdInfo, lits, dbuuid);

		// Get model1 columns
		CatalogModelXml catModel = (CatalogModelXml) tuple.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0);
		DepXml dep = (DepXml) tuple.getPmmXml(Model1Schema.ATT_DEPENDENT)
				.get(0);
		IndepXml indep = (IndepXml) tuple.getPmmXml(
				Model1Schema.ATT_INDEPENDENT).get(0);

		PmmXmlDoc paramsDoc = (PmmXmlDoc) tuple
				.getPmmXml(Model1Schema.ATT_PARAMETER);
		List<ParamXml> params = new LinkedList<>();
		for (PmmXmlElementConvertable paramItem : paramsDoc.getElementSet()) {
			params.add((ParamXml) paramItem);
		}

		EstModelXml modelXml = (EstModelXml) tuple.getPmmXml(
				Model1Schema.ATT_ESTMODEL).get(0);

		PmmXmlDoc mlitDoc = (PmmXmlDoc) tuple.getPmmXml(Model1Schema.ATT_MLIT);
		List<LiteratureItem> mLits = new LinkedList<>();
		for (PmmXmlElementConvertable litItem : mlitDoc.getElementSet()) {
			mLits.add((LiteratureItem) litItem);
		}

		PmmXmlDoc emlitDoc = (PmmXmlDoc) tuple
				.getPmmXml(Model1Schema.ATT_EMLIT);
		List<LiteratureItem> emLits = new LinkedList<>();
		for (PmmXmlElementConvertable litItem : emlitDoc.getElementSet()) {
			emLits.add((LiteratureItem) litItem);
		}

		Integer databaseWritable = tuple
				.getInt(Model1Schema.ATT_DATABASEWRITABLE);
		dbuuid = tuple.getString(Model1Schema.ATT_DBUUID);

		// Create JSONModel1
		JSONModel1 m1 = new JSONModel1(catModel, dep, indep, params, modelXml,
				mLits, emLits, databaseWritable, dbuuid);

		// Create JSONObject obj with TimeSeries and Model1Schema
		JSONObject obj = new JSONObject();
		obj.put("TimeSeries", ts.getObj());
		obj.put("Model1Schema", m1.getObj());

		// Turn obj into json string and return it
		String modelString = obj.toJSONString();

		return modelString;
	}
}

class TertiaryTupleReader implements Runnable {

	List<KnimeTuple> tuples;

	public TertiaryTupleReader(List<KnimeTuple> tuples) {
		this.tuples = tuples;
	}

	public void run() {
		// Parse primary tuple
		String model = parse(tuples);

		// Add model
		ModelStrings ms = ModelStrings.getModelStrings();
		ms.addModel(model);
	}

	@SuppressWarnings("unchecked")
	private String parse(List<KnimeTuple> modelTuples) {
		// Get first tuple
		KnimeTuple firstTuple = modelTuples.get(0);

		// Get time series columns from firsTuple
		Integer condId = (Integer) firstTuple
				.getInt(TimeSeriesSchema.ATT_CONDID);
		String combaseId = (String) firstTuple
				.getString(TimeSeriesSchema.ATT_DBUUID);
		AgentXml organism = (AgentXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_AGENT).get(0);
		MatrixXml matrix = (MatrixXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_MATRIX).get(0);

		PmmXmlDoc dataDoc = (PmmXmlDoc) firstTuple
				.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
		List<TimeSeriesXml> data = new LinkedList<>();
		for (PmmXmlElementConvertable dataItem : dataDoc.getElementSet()) {
			data.add((TimeSeriesXml) dataItem);
		}

		PmmXmlDoc miscDoc = firstTuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
		List<MiscXml> miscList = new LinkedList<>();
		for (PmmXmlElementConvertable miscItem : miscDoc.getElementSet()) {
			miscList.add((MiscXml) miscItem);
		}

		MdInfoXml mdInfo = (MdInfoXml) firstTuple.getPmmXml(
				TimeSeriesSchema.ATT_MDINFO).get(0);
		String dbuuid = (String) firstTuple
				.getString(TimeSeriesSchema.ATT_DBUUID);

		PmmXmlDoc litDoc = (PmmXmlDoc) firstTuple
				.getPmmXml(TimeSeriesSchema.ATT_LITMD);
		List<LiteratureItem> lits = new LinkedList<>();
		for (PmmXmlElementConvertable litItem : litDoc.getElementSet()) {
			lits.add((LiteratureItem) litItem);
		}

		// Create JSONTimeSeries
		JSONTimeSeries ts = new JSONTimeSeries(condId, combaseId, organism,
				matrix, data, miscList, mdInfo, lits, dbuuid);

		// Get model1 columns from firstTuple
		CatalogModelXml catModel = (CatalogModelXml) firstTuple.getPmmXml(
				Model1Schema.ATT_MODELCATALOG).get(0);
		DepXml dep = (DepXml) firstTuple.getPmmXml(Model1Schema.ATT_DEPENDENT)
				.get(0);
		IndepXml indep = (IndepXml) firstTuple.getPmmXml(
				Model1Schema.ATT_INDEPENDENT).get(0);

		PmmXmlDoc paramsDoc = (PmmXmlDoc) firstTuple
				.getPmmXml(Model1Schema.ATT_PARAMETER);
		List<ParamXml> params = new LinkedList<>();
		for (PmmXmlElementConvertable paramItem : paramsDoc.getElementSet()) {
			params.add((ParamXml) paramItem);
		}

		EstModelXml modelXml = (EstModelXml) firstTuple.getPmmXml(
				Model1Schema.ATT_ESTMODEL).get(0);

		PmmXmlDoc mlitDoc = (PmmXmlDoc) firstTuple
				.getPmmXml(Model1Schema.ATT_MLIT);
		List<LiteratureItem> mLits = new LinkedList<>();
		for (PmmXmlElementConvertable litItem : mlitDoc.getElementSet()) {
			mLits.add((LiteratureItem) litItem);
		}

		PmmXmlDoc emlitDoc = (PmmXmlDoc) firstTuple
				.getPmmXml(Model1Schema.ATT_EMLIT);
		List<LiteratureItem> emLits = new LinkedList<>();
		for (PmmXmlElementConvertable litItem : emlitDoc.getElementSet()) {
			emLits.add((LiteratureItem) litItem);
		}

		Integer databaseWritable = firstTuple
				.getInt(Model1Schema.ATT_DATABASEWRITABLE);
		dbuuid = (String) firstTuple.getString(Model1Schema.ATT_DBUUID);

		// Create JSONModel1
		JSONModel1 m1 = new JSONModel1(catModel, dep, indep, params, modelXml,
				mLits, emLits, databaseWritable, dbuuid);

		// Create JSONArray that will hold the secondary models
		JSONArray secModels = new JSONArray();

		for (KnimeTuple secTuple : modelTuples) {
			// Get Model2 columns from secTuple
			catModel = (CatalogModelXml) secTuple.getPmmXml(
					Model2Schema.ATT_MODELCATALOG).get(0);
			dep = (DepXml) secTuple.getPmmXml(Model2Schema.ATT_DEPENDENT)
					.get(0);
			indep = (IndepXml) secTuple.getPmmXml(Model2Schema.ATT_INDEPENDENT)
					.get(0);

			paramsDoc = (PmmXmlDoc) secTuple
					.getPmmXml(Model2Schema.ATT_PARAMETER);
			params = new LinkedList<>();
			for (PmmXmlElementConvertable paramItem : paramsDoc.getElementSet()) {
				params.add((ParamXml) paramItem);
			}

			modelXml = (EstModelXml) secTuple.getPmmXml(
					Model2Schema.ATT_ESTMODEL).get(0);

			mlitDoc = (PmmXmlDoc) secTuple.getPmmXml(Model2Schema.ATT_MLIT);
			mLits = new LinkedList<>();
			for (PmmXmlElementConvertable litItem : mlitDoc.getElementSet()) {
				mLits.add((LiteratureItem) litItem);
			}

			emlitDoc = (PmmXmlDoc) secTuple.getPmmXml(Model2Schema.ATT_EMLIT);
			emLits = new LinkedList<>();
			for (PmmXmlElementConvertable litItem : emlitDoc.getElementSet()) {
				emLits.add((LiteratureItem) litItem);
			}

			databaseWritable = secTuple
					.getInt(Model2Schema.ATT_DATABASEWRITABLE);
			dbuuid = secTuple.getString(Model2Schema.ATT_DBUUID);
			Integer globalModelID = secTuple
					.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);

			JSONModel2 m2 = new JSONModel2(catModel, dep, indep, params,
					modelXml, mLits, emLits, databaseWritable, dbuuid,
					globalModelID);
			secModels.add(m2.getObj());
		}

		// Create JSONObject with TimeSeries, Model1Schema, and Model2Schemas
		JSONObject obj = new JSONObject();
		obj.put("TimeSeries", ts.getObj());
		obj.put("Model1Schema", m1.getObj());
		obj.put("Model2Schema", secModels);

		// Turn obj into json string and return it
		String modelString = obj.toJSONString();

		return modelString;
	}
}