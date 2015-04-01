/**
 * Code and decode a KnimeTuple with a TimeSeriesSchema into/from JSON.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.jsonutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class JSONTimeSeries {

	JSONAgent organismCoder;
	JSONMatrix matrixCoder;
	JSONMdInfo mdInfoCoder;
	JSONData dataCoder;
	JSONMiscList miscCoder;
	JSONLiteratureList litsCoder;

	private JSONObject obj;

	public JSONTimeSeries(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONTimeSeries(Integer condId, String combaseId, AgentXml organism,
			MatrixXml matrix, List<TimeSeriesXml> data, List<MiscXml> miscList,
			MdInfoXml mdInfo, List<LiteratureItem> lits, String mDBUID) {

		organismCoder = new JSONAgent(organism);
		matrixCoder = new JSONMatrix(matrix);
		mdInfoCoder = new JSONMdInfo(mdInfo);
		litsCoder = new JSONLiteratureList(lits);
		dataCoder = new JSONData(data);
		miscCoder = new JSONMiscList(miscList);

		obj = new JSONObject();
		obj.put(TimeSeriesSchema.ATT_CONDID, condId);
		obj.put(TimeSeriesSchema.ATT_COMBASEID, combaseId);
		obj.put(TimeSeriesSchema.ATT_MISC, organismCoder.getObj());
		obj.put(TimeSeriesSchema.ATT_MATRIX, matrixCoder.getObj());
		obj.put(TimeSeriesSchema.ATT_TIMESERIES, dataCoder.getObj());
		obj.put(TimeSeriesSchema.ATT_MISC, miscCoder.getObj());
		obj.put(TimeSeriesSchema.ATT_MDINFO, mdInfoCoder.getObj());
		obj.put(TimeSeriesSchema.ATT_LITMD, litsCoder.getObj());
		obj.put(TimeSeriesSchema.ATT_DBUUID, mDBUID);
	}

	public JSONObject getObj() {
		return obj;
	}

	public KnimeTuple toKnimeTuple() {
		KnimeTuple tuple = new KnimeTuple(SchemaFactory.createDataSchema());

		Integer condID = ((Long) obj.get(TimeSeriesSchema.ATT_CONDID))
				.intValue();

		String combaseID = (String) obj.get(TimeSeriesSchema.ATT_COMBASEID);

		JSONMiscList jm = new JSONMiscList(
				(JSONArray) obj.get(TimeSeriesSchema.ATT_MISC));
		PmmXmlDoc misc = new PmmXmlDoc();
		for (MiscXml item : jm.toMiscXml()) {
			misc.add(item);
		}

		JSONAgent ja = new JSONAgent((JSONObject) obj.get(TimeSeriesSchema.ATT_AGENT));
		PmmXmlDoc agent = new PmmXmlDoc(ja.toAgentXml());

		JSONMatrix mc = new JSONMatrix((JSONObject) obj.get(TimeSeriesSchema.ATT_MATRIX));
		PmmXmlDoc matrix = new PmmXmlDoc(mc.toMatrixXml());

		JSONData jd = new JSONData((JSONArray) obj.get(TimeSeriesSchema.ATT_TIMESERIES));
		PmmXmlDoc data = new PmmXmlDoc();
		for (TimeSeriesXml ts : jd.toTimeSeriesXml()) {
			data.add(ts);
		}

		JSONMdInfo jmi = new JSONMdInfo((JSONObject) obj.get(TimeSeriesSchema.ATT_MDINFO));
		PmmXmlDoc mdInfo = new PmmXmlDoc(jmi.toMdInfoXml());

		JSONLiteratureList jlits = new JSONLiteratureList(
				(JSONArray) obj.get(TimeSeriesSchema.ATT_LITMD));
		PmmXmlDoc lits = new PmmXmlDoc();
		for (LiteratureItem lit : jlits.toLiteratureItem()) {
			lits.add(lit);
		}

		String dbuuid = (String) obj.get(TimeSeriesSchema.ATT_DBUUID);

		tuple.setValue(TimeSeriesSchema.ATT_CONDID, condID);
		tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, combaseID);
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, agent);
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrix);
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, data);
		tuple.setValue(TimeSeriesSchema.ATT_MISC, misc);
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, mdInfo);
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, jlits);
		tuple.setValue(TimeSeriesSchema.ATT_DBUUID, dbuuid);

		return tuple;
	}

	public static void main(String[] args) {
		int condId = -1;
		String combaseId = "-1";
		AgentXml organism = new AgentXml(-1, "species",
				"Shewanella putrefaciens", "4025");
		MatrixXml matrix = new MatrixXml(-16884176, null, null);

		// Create a few points for the time series (MD_Data)
		String timeUnit = "h";
		String origTimeUnit = "h";
		String concentrationUnit = "ln(count/g)";
		String concentrationUnitObjectType = "CFU";
		String origConcentrationUnit = "log10(count/g)";
		Double concentrationStdDev = null;
		Integer numberOfMeasurements = null;

		TimeSeriesXml t0 = new TimeSeriesXml("t0", 0.0, timeUnit, origTimeUnit,
				6.148, concentrationUnit, concentrationUnitObjectType,
				origConcentrationUnit, concentrationStdDev,
				numberOfMeasurements);
		TimeSeriesXml t1 = new TimeSeriesXml("t1", 50.882, timeUnit,
				origTimeUnit, 6.700, concentrationUnit,
				concentrationUnitObjectType, origConcentrationUnit,
				concentrationStdDev, numberOfMeasurements);
		TimeSeriesXml t2 = new TimeSeriesXml("t2", 73.018, timeUnit,
				origTimeUnit, 6.608, concentrationUnit,
				concentrationUnitObjectType, origConcentrationUnit,
				concentrationStdDev, numberOfMeasurements);
		List<TimeSeriesXml> data = Arrays.asList(t0, t1, t2);

		// Misc
		MiscXml temp = new MiscXml(-1, "Temperature", "Temperature", 10.0,
				Arrays.asList("Temperature"), "ºC");
		temp.setOrigUnit("ºC");
		temp.setValue(10.0);

		MiscXml pH = new MiscXml(-2, "pH", "pH", 5.63,
				Arrays.asList("Dimensionless quantity"), "[pH]");
		pH.setOrigUnit("[pH]");
		pH.setValue(5.63);

		List<MiscXml> miscs = Arrays.asList(temp, pH);

		// Model info
		MdInfoXml mdInfo = new MdInfoXml(null, "", "", 1, true);

		// Create empty list for MD_Literatur
		List<LiteratureItem> lits = new ArrayList<>();

		String dbUID = "";

		JSONTimeSeries tsEncoder = new JSONTimeSeries(condId, combaseId,
				organism, matrix, data, miscs, mdInfo, lits, dbUID);
		JSONObject obj = tsEncoder.getObj();
		System.out.println(obj);
	}
}
