package de.bund.bfr.knime.pmm.jsonencoder;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hsh.bfr.db.DBKernel;
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

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
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
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBAgents;
import de.bund.bfr.knime.pmm.dbutil.DBLits;
import de.bund.bfr.knime.pmm.dbutil.DBMatrices;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.knime.pmm.jsonutil.JSONAgent;
import de.bund.bfr.knime.pmm.jsonutil.JSONLiteratureList;
import de.bund.bfr.knime.pmm.jsonutil.JSONMatrix;
import de.bund.bfr.knime.pmm.jsonutil.JSONModel1;
import de.bund.bfr.knime.pmm.jsonutil.JSONModel2;
import de.bund.bfr.knime.pmm.jsonutil.JSONTimeSeries;
import de.bund.bfr.knime.pmm.sbmlutil.ModelType;

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
		ModelType modelType = ModelType.getTableType(inTableSpec);

		List<Thread> threads = new LinkedList<>();
		// Parse tertiary models
		if (modelType == ModelType.TERTIARY) {
			// Retrieve input tuples
			KnimeSchema schema = SchemaFactory.createM12DataSchema();
			List<KnimeTuple> tuples = PmmUtilities.getTuples(inTable, schema);

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

		else if (modelType == ModelType.PRIMARY) {
			// Retrieve input tuples
			KnimeSchema schema = SchemaFactory.createM1DataSchema();
			List<KnimeTuple> tuples = PmmUtilities.getTuples(inTable, schema);

			// Each KNIME tuple is a primary model
			// Create threads for every tuple
			for (KnimeTuple tuple : tuples) {
				threads.add(new Thread(new PrimaryTupleParser(tuple)));
			}
		} else if (modelType == ModelType.SECONDARY) {
			KnimeSchema schema = SchemaFactory.createM2Schema();
			List<KnimeTuple> tuples = PmmUtilities.getTuples(inTable, schema);

			for (KnimeTuple tuple : tuples) {
				threads.add(new Thread(new SecondaryTupleParser(tuple)));
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

		// Push model strings into flow variable port
		Models m = Models.getInstance();
		pushFlowVariableString("models", m.getModels().toJSONString());

		// clear model strings (otherwise they'll be kept on the next run)
		m.clear();

		setMatrices();
		setAgents();
		setLits();
		setSecModels();
		setUnits();
		setUnitCategories();

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

	@SuppressWarnings("unchecked")
	private void setMatrices() {
		JSONArray matArray = new JSONArray();
		for (MatrixXml matrixXml : DBMatrices.getDBMatrices()) {
			matArray.add(new JSONMatrix(matrixXml).getObj());
		}
		pushFlowVariableString("matrices", matArray.toJSONString());
	}

	@SuppressWarnings("unchecked")
	private void setAgents() {
		JSONArray agentsArray = new JSONArray();
		for (AgentXml agentXml : DBAgents.getDBAgents()) {
			agentsArray.add(new JSONAgent(agentXml).getObj());
		}
		pushFlowVariableString("agents", agentsArray.toJSONString());
	}

	private void setLits() {
		JSONLiteratureList lits = new JSONLiteratureList(DBLits.getDBLits());
		pushFlowVariableString("references", lits.getObj().toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	private void setUnits() {
		Map<String, List<String>> unitsPerCategory = new HashMap<>();
		
		for (UnitsFromDB ufdb : DBUnits.getDBUnits().values()) {
			String category = ufdb.getKind_of_property_quantity();
			String unit = ufdb.getDisplay_in_GUI_as();
			if (unitsPerCategory.containsKey(category)) {
				List<String> units = unitsPerCategory.get(category);
				units.add(unit);
			} else {
				List<String> units = new LinkedList<>();
				units.add(unit);
				unitsPerCategory.put(category, units);
			}
		}
		
		JSONArray ja = new JSONArray();
		for (Entry<String, List<String>> entry : unitsPerCategory.entrySet()) {
			String category = entry.getKey();
			List<String> units = entry.getValue();
			
			JSONObject categoryJSON = new JSONObject();
			categoryJSON.put("text", category);
			
			JSONArray unitsJSON = new JSONArray();
			for (String unit : units) {
				JSONObject unitJSON = new JSONObject();
				unitJSON.put("id", unit);
				unitJSON.put("text", unit);
				unitsJSON.add(unitJSON);
			}
			categoryJSON.put("children", unitsJSON);
			ja.add(categoryJSON);
		}
		
		pushFlowVariableString("units", ja.toJSONString());
	}
	
	@SuppressWarnings("unchecked")
	private void setUnitCategories() {
		// Get categories
		Set<String> categories = new HashSet<>();
		for (UnitsFromDB ufdb : DBUnits.getDBUnits().values()) {
			categories.add(ufdb.getKind_of_property_quantity());
		}
		
		// Build JSONArray
		JSONArray ja = new JSONArray();
		for (String category : categories) {
			JSONObject jo = new JSONObject();
			jo.put("id", ja.size());
			jo.put("name", category);
			ja.add(jo);
		}
		
		pushFlowVariableString("categories", ja.toJSONString());
	}

	private void setSecModels() throws SQLException, InterruptedException {
		LinkedList<Integer> secModelIds = new LinkedList<>();
		ResultSet rs = DBKernel.getResultSet(
				"SELECT \"ID\" FROM \"Modellkatalog\" WHERE \"Level\"='2'",
				true);
		try {
			while (rs.next()) {
				secModelIds.add(rs.getInt("ID"));
			}
		} catch (SQLException e) {
		}
		Bfrdb db = new Bfrdb(DBKernel.getLocalConn(true));
		List<Thread> threads = new LinkedList<>();
		for (int id : secModelIds) {
			KnimeTuple tuple = db.getSecModelById(id);
			threads.add(new Thread(new SecondaryTupleParser(tuple)));
		}

		// Start threads
		for (Thread thread : threads) {
			thread.start();
		}

		// Join threads
		for (Thread thread : threads) {
			thread.join();
		}

		// Push model strings into flow variable port
		Models m = Models.getInstance();
		pushFlowVariableString("secondary", m.getModels().toJSONString());
		// Clear model strings
		m.clear();
	}
}

class Models {
	private static Models m = new Models();
	private JSONArray models = new JSONArray();

	private Models() {
	}

	public static Models getInstance() {
		return m;
	}

	public JSONArray getModels() {
		return models;
	}

	@SuppressWarnings("unchecked")
	public synchronized void addModel(JSONObject obj) {
		models.add(obj);
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
		Models.getInstance().addModel(parse(tuple));
	}

	@SuppressWarnings("unchecked")
	private JSONObject parse(KnimeTuple tuple) {
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
		List<IndepXml> indeps = new LinkedList<>();
		for (PmmXmlElementConvertable item : tuple.getPmmXml(
				Model1Schema.ATT_INDEPENDENT).getElementSet()) {
			indeps.add((IndepXml) item);
		}

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
		JSONModel1 m1 = new JSONModel1(catModel, dep, indeps, params, modelXml,
				mLits, emLits, databaseWritable, dbuuid);

		// Create JSONObject obj with TimeSeries and Model1Schema
		JSONObject obj = new JSONObject();
		obj.put("TimeSeriesSchema", ts.getObj());
		obj.put("Model1Schema", m1.getObj());
		return obj;
	}
}

class SecondaryTupleParser implements Runnable {

	KnimeTuple tuple;

	public SecondaryTupleParser(KnimeTuple tuple) {
		this.tuple = tuple;
	}

	public void run() {
		Models.getInstance().addModel(parse(tuple));
	}

	@SuppressWarnings("unchecked")
	private JSONObject parse(KnimeTuple tuple) {
		// Get model2 columns
		CatalogModelXml catModel = (CatalogModelXml) tuple.getPmmXml(
				Model2Schema.ATT_MODELCATALOG).get(0);
		EstModelXml estModel = (EstModelXml) tuple.getPmmXml(
				Model2Schema.ATT_ESTMODEL).get(0);

		DepXml dep = (DepXml) tuple.getPmmXml(Model2Schema.ATT_DEPENDENT)
				.get(0);

		// Get independent
		PmmXmlDoc indepDoc = (PmmXmlDoc) tuple
				.getPmmXml(Model2Schema.ATT_INDEPENDENT);
		LinkedList<IndepXml> indeps = new LinkedList<>();
		for (PmmXmlElementConvertable item : indepDoc.getElementSet()) {
			indeps.add((IndepXml) item);
		}

		// Get parameters
		PmmXmlDoc paramsDoc = (PmmXmlDoc) tuple
				.getPmmXml(Model2Schema.ATT_PARAMETER);
		LinkedList<ParamXml> params = new LinkedList<>();
		for (PmmXmlElementConvertable item : paramsDoc.getElementSet()) {
			params.add((ParamXml) item);
		}

		// Get model literature
		PmmXmlDoc mLitDoc = (PmmXmlDoc) tuple.getPmmXml(Model2Schema.ATT_MLIT);
		LinkedList<LiteratureItem> mLits = new LinkedList<>();
		for (PmmXmlElementConvertable litItem : mLitDoc.getElementSet()) {
			mLits.add((LiteratureItem) litItem);
		}

		// Get estimated model literature
		PmmXmlDoc emLitDoc = (PmmXmlDoc) tuple
				.getPmmXml(Model2Schema.ATT_EMLIT);
		LinkedList<LiteratureItem> emLits = new LinkedList<>();
		for (PmmXmlElementConvertable litItem : emLitDoc.getElementSet()) {
			emLits.add((LiteratureItem) litItem);
		}

		// Get databaseWritable, dbuuid and globalModelID
		Integer databaseWritable = tuple
				.getInt(Model2Schema.ATT_DATABASEWRITABLE);
		String dbuuid = tuple.getString(Model2Schema.ATT_DBUUID);
		Integer globalModelID = tuple.getInt(Model2Schema.ATT_GLOBAL_MODEL_ID);

		// Create JSONModel2
		JSONModel2 m2 = new JSONModel2(catModel, dep, indeps, params, estModel,
				mLits, emLits, databaseWritable, dbuuid, globalModelID);

		// Create JSONObject obj with Model2Schema
		JSONObject obj = new JSONObject();
		obj.put("Model2Schema", m2.getObj());
		return obj;
	}
}

class TertiaryTupleReader implements Runnable {

	List<KnimeTuple> tuples;

	public TertiaryTupleReader(List<KnimeTuple> tuples) {
		this.tuples = tuples;
	}

	public void run() {
		Models.getInstance().addModel(parse(tuples));
	}

	@SuppressWarnings("unchecked")
	private JSONObject parse(List<KnimeTuple> modelTuples) {
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
		List<IndepXml> indeps = new LinkedList<>();
		for (PmmXmlElementConvertable item : firstTuple.getPmmXml(
				Model1Schema.ATT_INDEPENDENT).getElementSet()) {
			indeps.add((IndepXml) item);
		}

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
		JSONModel1 m1 = new JSONModel1(catModel, dep, indeps, params, modelXml,
				mLits, emLits, databaseWritable, dbuuid);

		// Create JSONArray that will hold the secondary models
		JSONArray secModels = new JSONArray();

		for (KnimeTuple secTuple : modelTuples) {
			// Get Model2 columns from secTuple
			catModel = (CatalogModelXml) secTuple.getPmmXml(
					Model2Schema.ATT_MODELCATALOG).get(0);
			dep = (DepXml) secTuple.getPmmXml(Model2Schema.ATT_DEPENDENT)
					.get(0);
			indeps.clear();
			for (PmmXmlElementConvertable item : secTuple.getPmmXml(
					Model1Schema.ATT_INDEPENDENT).getElementSet()) {
				indeps.add((IndepXml) item);
			}

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

			JSONModel2 m2 = new JSONModel2(catModel, dep, indeps, params,
					modelXml, mLits, emLits, databaseWritable, dbuuid,
					globalModelID);
			secModels.add(m2.getObj());
		}

		// Create JSONObject with TimeSeries, Model1Schema, and Model2Schemas
		JSONObject obj = new JSONObject();
		obj.put("TimeSeriesSchema", ts.getObj());
		obj.put("Model1Schema", m1.getObj());
		obj.put("Model2Schema", secModels);
		return obj;
	}
}