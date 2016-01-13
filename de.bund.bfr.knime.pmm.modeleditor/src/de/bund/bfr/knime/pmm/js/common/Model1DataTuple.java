package de.bund.bfr.knime.pmm.js.common;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Model1DataTuple implements ViewValue {

	private static final String CONDID = "CondID";
	private static final String COMBASEID = "CombaseID";
	private static final String MISC = "Misc";
	private static final String AGENT = "Organism";
	private static final String MATRIX = "Matrix";
	private static final String TIMESERIES = "MD_Data";
	private static final String MDINFO = "MD_Info";
	private static final String LITMD = "MD_Literatur";
	private static final String DBUUID = "M_DB_UID";
	private static final String MODELCATALOG = "CatModel";
	private static final String ESTMODEL = "EstModel";
	private static final String DEPENDENT = "Dependent";
	private static final String PARAMETER = "Parameter";
	private static final String INDEPENDENT = "Independent";
	private static final String MLIT = "M_Literatur";
	private static final String EMLIT = "EM_Literatur";
	private static final String DATABASEWRITABLE = "DatabaseWritable";

	private int condId;
	private String combaseId;
	private Agent agent;
	private Matrix matrix;
	private TimeSeriesList timeSeriesList;
	private MiscList miscList;
	private MdInfo mdInfo;
	private LiteratureList litMd;
	private String dbuuid;
	private CatalogModel catModel;
	private EstModel estModel;
	private Dep dep;
	private ParamList params;
	private IndepList indeps;
	private LiteratureList mLit;
	private LiteratureList emLit;
	private boolean databaseWritable;

	public int getCondId() {
		return condId;
	}

	public String getCombaseId() {
		return combaseId;
	}

	public Agent getAgent() {
		return agent;
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public TimeSeriesList getTimeSeriesList() {
		return timeSeriesList;
	}

	public MiscList getMiscList() {
		return miscList;
	}

	public MdInfo getMdInfo() {
		return mdInfo;
	}

	public LiteratureList getLitMd() {
		return litMd;
	}

	public String getDbuuid() {
		return dbuuid;
	}

	public CatalogModel getCatModel() {
		return catModel;
	}

	public EstModel getEstModel() {
		return estModel;
	}

	public Dep getDep() {
		return dep;
	}

	public ParamList getParams() {
		return params;
	}

	public IndepList getIndeps() {
		return indeps;
	}

	public LiteratureList getmLit() {
		return mLit;
	}

	public LiteratureList getEmLit() {
		return emLit;
	}

	public boolean isDatabaseWritable() {
		return databaseWritable;
	}

	public void setCondId(int condId) {
		this.condId = condId;
	}

	public void setCombaseId(String combaseId) {
		this.combaseId = combaseId;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}

	public void setTimeSeriesList(TimeSeriesList timeSeriesList) {
		this.timeSeriesList = timeSeriesList;
	}

	public void setMiscList(MiscList miscList) {
		this.miscList = miscList;
	}

	public void setMdInfo(MdInfo mdInfo) {
		this.mdInfo = mdInfo;
	}

	public void setLitMd(LiteratureList litMd) {
		this.litMd = litMd;
	}

	public void setDbuuid(String dbuuid) {
		this.dbuuid = dbuuid;
	}

	public void setCatModel(CatalogModel catModel) {
		this.catModel = catModel;
	}

	public void setEstModel(EstModel estModel) {
		this.estModel = estModel;
	}

	public void setDep(Dep dep) {
		this.dep = dep;
	}

	public void setParams(ParamList params) {
		this.params = params;
	}

	public void setIndeps(IndepList indeps) {
		this.indeps = indeps;
	}

	public void setmLit(LiteratureList mLit) {
		this.mLit = mLit;
	}

	public void setEmLit(LiteratureList emLit) {
		this.emLit = emLit;
	}

	public void setDatabaseWritable(boolean databaseWritable) {
		this.databaseWritable = databaseWritable;
	}

	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		settings.addInt(CONDID, condId);
		settings.addString(COMBASEID, combaseId);
		agent.saveToNodeSettings(settings.addNodeSettings(AGENT));
		matrix.saveToNodeSettings(settings.addNodeSettings(MATRIX));
		timeSeriesList.saveToNodeSettings(settings.addNodeSettings(TIMESERIES));
		miscList.saveToNodeSettings(settings.addNodeSettings(MISC));
		mdInfo.saveToNodeSettings(settings.addNodeSettings(MDINFO));
		litMd.saveToNodeSettings(settings.addNodeSettings(LITMD));
		settings.addString(DBUUID, dbuuid);
		catModel.saveToNodeSettings(settings.addNodeSettings(MODELCATALOG));
		estModel.saveToNodeSettings(settings.addNodeSettings(ESTMODEL));
		dep.saveToNodeSettings(settings.addNodeSettings(DEPENDENT));
		params.saveToNodeSettings(settings.addNodeSettings(PARAMETER));
		indeps.saveToNodeSettings(settings.addNodeSettings(INDEPENDENT));
		mLit.saveToNodeSettings(settings.addNodeSettings(MLIT));
		emLit.saveToNodeSettings(settings.addNodeSettings(EMLIT));
		settings.addBoolean(DATABASEWRITABLE, databaseWritable);
	}

	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		condId = settings.getInt(CONDID);
		combaseId = settings.getString(AGENT);
		agent.loadFromNodeSettings(settings.getNodeSettings(AGENT));
		matrix.loadFromNodeSettings(settings.getNodeSettings(MATRIX));
		timeSeriesList.loadFromNodeSettings(settings.getNodeSettings(TIMESERIES));
		miscList.loadFromNodeSettings(settings.getNodeSettings(MISC));
		mdInfo.loadFromNodeSettings(settings.getNodeSettings(MDINFO));
		litMd.loadFromNodeSettings(settings.getNodeSettings(LITMD));
		dbuuid = settings.getString(DBUUID);
		catModel.loadFromNodeSettings(settings.getNodeSettings(MODELCATALOG));
		estModel.loadFromNodeSettings(settings.getNodeSettings(ESTMODEL));
		dep.loadFromNodeSettings(settings.getNodeSettings(DEPENDENT));
		params.loadFromNodeSettings(settings.getNodeSettings(PARAMETER));
		indeps.loadFromNodeSettings(settings.getNodeSettings(INDEPENDENT));
		mLit.loadFromNodeSettings(settings.getNodeSettings(MLIT));
		emLit.loadFromNodeSettings(settings.getNodeSettings(EMLIT));
		databaseWritable = settings.getBoolean(DATABASEWRITABLE);
	}
}