package de.bund.bfr.knime.pmm.editor;

import java.util.List;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractWizardNodeModel;

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
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.js.common.Agent;
import de.bund.bfr.knime.pmm.js.common.CatalogModel;
import de.bund.bfr.knime.pmm.js.common.Dep;
import de.bund.bfr.knime.pmm.js.common.EstModel;
import de.bund.bfr.knime.pmm.js.common.Indep;
import de.bund.bfr.knime.pmm.js.common.IndepList;
import de.bund.bfr.knime.pmm.js.common.Literature;
import de.bund.bfr.knime.pmm.js.common.LiteratureList;
import de.bund.bfr.knime.pmm.js.common.Matrix;
import de.bund.bfr.knime.pmm.js.common.MdInfo;
import de.bund.bfr.knime.pmm.js.common.Misc;
import de.bund.bfr.knime.pmm.js.common.MiscList;
import de.bund.bfr.knime.pmm.js.common.Model1DataTuple;
import de.bund.bfr.knime.pmm.js.common.ModelList;
import de.bund.bfr.knime.pmm.js.common.Param;
import de.bund.bfr.knime.pmm.js.common.ParamList;
import de.bund.bfr.knime.pmm.js.common.TimeSeries;
import de.bund.bfr.knime.pmm.js.common.TimeSeriesList;

/**
 * Model Editor node model.
 *
 * @author Miguel Alba
 */
public final class ModelEditorNodeModel
		extends AbstractWizardNodeModel<ModelEditorViewRepresentation, ModelEditorViewValue> {

	private boolean m_executed = false;
	private final ModelEditorViewConfig m_config;

	/** Constructor of (@code ModelEditorNodeModel). */
	protected ModelEditorNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE }, new PortType[] { BufferedDataTable.TYPE }, "Model editor");
		m_config = new ModelEditorViewConfig();
	}

	/** {@inheritDoc} */
	@Override
	public ModelEditorViewRepresentation createEmptyViewRepresentation() {
		return new ModelEditorViewRepresentation();
	}

	/** {@inheritDoc} */
	@Override
	public ModelEditorViewValue createEmptyViewValue() {
		return new ModelEditorViewValue();
	}

	@Override
	public String getJavascriptObjectID() {
		return "de.bund.bfr.knime.pmm.editor";
	}

	@Override
	public boolean isHideInWizard() {
		return false;
	}

	@Override
	public ValidationError validateViewValue(ModelEditorViewValue viewContent) {
		synchronized (getLock()) {
			// Nothing to do.
		}
		return null;
	}

	@Override
	public void saveCurrentValue(NodeSettingsWO settings) {
		getViewValue().getModels().saveToNodeSettings(settings);
	}

	@Override
	public DataTableSpec[] configure(DataTableSpec[] inSpecs) throws InvalidSettingsException {
		if (!SchemaFactory.createM1DataSchema().conforms(inSpecs[0])) {
			throw new InvalidSettingsException("Wrong input. Only works so far with M1DataSchema");
		}
		return new DataTableSpec[] { null };
	}

	@Override
	protected PortObject[] performExecute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		BufferedDataTable table = (BufferedDataTable) inObjects[0];
		List<KnimeTuple> tuples = PmmUtilities.getTuples(table, SchemaFactory.createM1DataSchema());

		ModelEditorViewValue viewValue = getViewValue();
		if (viewValue == null) {
			viewValue = createEmptyViewValue();
			setViewValue(viewValue);
		}

		if (!m_executed) {
			// Config of JavaScript view
//			viewValue.setModels(m_config.getModels());

			// Convert KNIME tuples to Model1DataTuple
			Model1DataTuple[] m1DataTuples = new Model1DataTuple[tuples.size()];
			for (int i = 0; i < tuples.size(); i++) {
				m1DataTuples[i] = codeTuple(tuples.get(i));
			}
			ModelList modelList = new ModelList();
			modelList.setModels(m1DataTuples);
			viewValue.setModels(modelList);

			setViewValue(viewValue);
			m_executed = true;
		}
		
		exec.setProgress(1);

		// return edited table
		BufferedDataContainer container = exec.createDataContainer(SchemaFactory.createM1DataSchema().createSpec());
		ModelList outModelList = getViewValue().getModels();
		for (Model1DataTuple m1DataTuple : outModelList.getModels()) {
			KnimeTuple outTuple = decodeTuple(m1DataTuple);
			container.addRowToTable(outTuple);
		}
		container.close();
		return new BufferedDataTable[] { container.getTable() };
	}

	@Override
	protected void performReset() {
		m_executed = false;
	}

	@Override
	protected void useCurrentValueAsDefault() {
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_config.saveSettings(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		new ModelEditorViewConfig().loadSettings(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		m_config.loadSettings(settings);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////

	// TODO: Move to another class: --> Model1DataTuple
	public static Model1DataTuple codeTuple(KnimeTuple inTuple) {
		Model1DataTuple outTuple = new Model1DataTuple();
		// process
		outTuple.setCondId(inTuple.getInt(TimeSeriesSchema.ATT_CONDID));
		outTuple.setCombaseId(inTuple.getString(TimeSeriesSchema.ATT_COMBASEID));

		PmmXmlDoc miscDoc = (PmmXmlDoc) inTuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
		Misc[] miscs = new Misc[miscDoc.size()];
		for (int i = 0; i < miscDoc.size(); i++) {
			MiscXml miscXml = (MiscXml) miscDoc.get(i);
			miscs[i] = Misc.toMisc(miscXml);
		}
		MiscList miscList = new MiscList();
		miscList.setMiscs(miscs);
		outTuple.setMiscList(miscList);

		AgentXml agentXml = (AgentXml) inTuple.getPmmXml(TimeSeriesSchema.ATT_AGENT).get(0);
		outTuple.setAgent(Agent.toAgent(agentXml));

		MatrixXml matrixXml = (MatrixXml) inTuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX).get(0);
		outTuple.setMatrix(Matrix.toMatrix(matrixXml));

		PmmXmlDoc timeSeriesDoc = (PmmXmlDoc) inTuple.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
		TimeSeries[] timeSeriesArray = new TimeSeries[timeSeriesDoc.size()];
		for (int i = 0; i < timeSeriesDoc.size(); i++) {
			TimeSeriesXml timeSeriesXml = (TimeSeriesXml) timeSeriesDoc.get(i);
			timeSeriesArray[i] = TimeSeries.toTimeSeries(timeSeriesXml);
		}
		TimeSeriesList timeSeriesList = new TimeSeriesList();
		timeSeriesList.setTimeSeries(timeSeriesArray);
		outTuple.setTimeSeriesList(timeSeriesList);

		MdInfoXml mdInfoXml = (MdInfoXml) inTuple.getPmmXml(TimeSeriesSchema.ATT_MDINFO).get(0);
		outTuple.setMdInfo(MdInfo.toMdInfo(mdInfoXml));

		PmmXmlDoc mdLitDoc = (PmmXmlDoc) inTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD);
		Literature[] mdLiteratureArray = new Literature[mdLitDoc.size()];
		for (int i = 0; i < mdLitDoc.size(); i++) {
			LiteratureItem literatureItem = (LiteratureItem) mdLitDoc.get(i);
			mdLiteratureArray[i] = Literature.toLiterature(literatureItem);
		}
		LiteratureList mdLiteratureList = new LiteratureList();
		mdLiteratureList.setLiterature(mdLiteratureArray);
		outTuple.setLitMd(mdLiteratureList);

		outTuple.setDbuuid(inTuple.getString(TimeSeriesSchema.ATT_DBUUID));

		CatalogModelXml catalogModelXml = (CatalogModelXml) inTuple.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0);
		outTuple.setCatModel(CatalogModel.toCatalogModel(catalogModelXml));

		EstModelXml estModelXml = (EstModelXml) inTuple.getPmmXml(Model1Schema.ATT_ESTMODEL).get(0);
		outTuple.setEstModel(EstModel.toEstModel(estModelXml));

		DepXml depXml = (DepXml) inTuple.getPmmXml(Model1Schema.ATT_DEPENDENT).get(0);
		outTuple.setDep(Dep.toDep(depXml));

		PmmXmlDoc paramDoc = inTuple.getPmmXml(Model1Schema.ATT_PARAMETER);
		Param[] paramArray = new Param[paramDoc.size()];
		for (int i = 0; i < paramDoc.size(); i++) {
			ParamXml paramXml = (ParamXml) paramDoc.get(i);
			paramArray[i] = Param.toParam(paramXml);
		}
		ParamList paramList = new ParamList();
		paramList.setParams(paramArray);
		outTuple.setParams(paramList);

		PmmXmlDoc indepDoc = inTuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
		Indep[] indepArray = new Indep[indepDoc.size()];
		for (int i = 0; i < indepDoc.size(); i++) {
			IndepXml indepXml = (IndepXml) indepDoc.get(i);
			indepArray[i] = Indep.toIndep(indepXml);
		}
		IndepList indepList = new IndepList();
		indepList.setIndeps(indepArray);
		outTuple.setIndeps(indepList);

		PmmXmlDoc mLitDoc = (PmmXmlDoc) inTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD);
		Literature[] mLiteratureArray = new Literature[mdLitDoc.size()];
		for (int i = 0; i < mLitDoc.size(); i++) {
			LiteratureItem literatureItem = (LiteratureItem) mLitDoc.get(i);
			mLiteratureArray[i] = Literature.toLiterature(literatureItem);
		}
		LiteratureList mLiteratureList = new LiteratureList();
		mLiteratureList.setLiterature(mLiteratureArray);
		outTuple.setmLit(mLiteratureList);

		PmmXmlDoc emLitDoc = (PmmXmlDoc) inTuple.getPmmXml(TimeSeriesSchema.ATT_LITMD);
		Literature[] emLiteratureArray = new Literature[mdLitDoc.size()];
		for (int i = 0; i < emLitDoc.size(); i++) {
			LiteratureItem literatureItem = (LiteratureItem) emLitDoc.get(i);
			emLiteratureArray[i] = Literature.toLiterature(literatureItem);
		}
		LiteratureList emLiteratureList = new LiteratureList();
		emLiteratureList.setLiterature(emLiteratureArray);
		outTuple.setEmLit(emLiteratureList);

		outTuple.setDatabaseWritable(Model1Schema.WRITABLE == inTuple.getInt(Model1Schema.ATT_DATABASEWRITABLE));

		return outTuple;
	}

	// TODO: Move to another class: --> Model1DataTuple
	public static KnimeTuple decodeTuple(final Model1DataTuple m1DataTuple) {
		KnimeTuple outTuple = new KnimeTuple(SchemaFactory.createM1DataSchema());

		outTuple.setValue(TimeSeriesSchema.ATT_CONDID, m1DataTuple.getCondId());
		outTuple.setValue(TimeSeriesSchema.ATT_COMBASEID, m1DataTuple.getCombaseId());

		MiscList miscList = m1DataTuple.getMiscList();
		PmmXmlDoc miscDoc = new PmmXmlDoc();
		for (Misc misc : miscList.getMiscs()) {
			miscDoc.add(misc.toMiscXml());
		}
		outTuple.setValue(TimeSeriesSchema.ATT_MISC, miscDoc);

		Agent agent = m1DataTuple.getAgent();
		PmmXmlDoc agentDoc = new PmmXmlDoc(agent.toAgentXml());
		outTuple.setValue(TimeSeriesSchema.ATT_AGENT, agentDoc);

		Matrix matrix = m1DataTuple.getMatrix();
		PmmXmlDoc matrixDoc = new PmmXmlDoc(matrix.toMatrixXml());
		outTuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixDoc);

		PmmXmlDoc timeSeriesDoc = new PmmXmlDoc();
		TimeSeriesList timeSeriesList = m1DataTuple.getTimeSeriesList();
		for (TimeSeries timeSeries : timeSeriesList.getTimeSeries()) {
			timeSeriesDoc.add(timeSeries.toTimeSeriesXml());
		}
		outTuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesDoc);

		MdInfo mdInfo = (MdInfo) m1DataTuple.getMdInfo();
		PmmXmlDoc mdInfoDoc = new PmmXmlDoc(mdInfo.toMdInfoXml());
		outTuple.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfoDoc);

		PmmXmlDoc mdLitDoc = new PmmXmlDoc();
		for (Literature literature : m1DataTuple.getLitMd().getLiterature()) {
			mdLitDoc.add(literature.toLiteratureItem());
		}
		outTuple.setValue(TimeSeriesSchema.ATT_LITMD, mdLitDoc);

		outTuple.setValue(TimeSeriesSchema.ATT_DBUUID, m1DataTuple.getDbuuid());
		outTuple.setValue(Model1Schema.ATT_DBUUID, m1DataTuple.getDbuuid());

		CatalogModel catalogModel = m1DataTuple.getCatModel();
		PmmXmlDoc catalogModelDoc = new PmmXmlDoc(catalogModel.toCatalogModelXml());
		outTuple.setValue(Model1Schema.ATT_MODELCATALOG, catalogModelDoc);

		EstModel estModel = m1DataTuple.getEstModel();
		PmmXmlDoc estModelDoc = new PmmXmlDoc(estModel.toEstModelXml());
		outTuple.setValue(Model1Schema.ATT_ESTMODEL, estModelDoc);

		Dep dep = m1DataTuple.getDep();
		PmmXmlDoc depDoc = new PmmXmlDoc(dep.toDepXml());
		outTuple.setValue(Model1Schema.ATT_DEPENDENT, depDoc);

		PmmXmlDoc paramDoc = new PmmXmlDoc();
		ParamList paramList = m1DataTuple.getParams();
		for (Param param : paramList.getParams()) {
			paramDoc.add(param.toParamXml());
		}
		outTuple.setValue(Model1Schema.ATT_PARAMETER, paramDoc);

		PmmXmlDoc indepDoc = new PmmXmlDoc();
		IndepList indepList = m1DataTuple.getIndeps();
		for (Indep indep : indepList.getIndeps()) {
			paramDoc.add(indep.toIndepXml());
		}
		outTuple.setValue(Model1Schema.ATT_INDEPENDENT, indepDoc);

		PmmXmlDoc mLitDoc = new PmmXmlDoc();
		LiteratureList mLitList = m1DataTuple.getmLit();
		for (Literature literature : mLitList.getLiterature()) {
			mLitDoc.add(literature.toLiteratureItem());
		}

		PmmXmlDoc emLitDoc = new PmmXmlDoc();
		LiteratureList emLitList = m1DataTuple.getmLit();
		for (Literature literature : emLitList.getLiterature()) {
			emLitDoc.add(literature.toLiteratureItem());
		}

		int dbWritableAsInt = m1DataTuple.isDatabaseWritable() ? 1 : 0;
		outTuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, dbWritableAsInt);

		return outTuple;
	}

}