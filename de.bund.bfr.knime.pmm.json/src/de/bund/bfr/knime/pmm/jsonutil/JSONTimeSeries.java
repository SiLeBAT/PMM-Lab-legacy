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

	JSONObject obj;

	public JSONTimeSeries(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONTimeSeries(Integer condId, String combaseId, AgentXml organism,
			MatrixXml matrix, List<TimeSeriesXml> data, List<MiscXml> miscList,
			MdInfoXml mdInfo, List<LiteratureItem> lits, String mDBUID) {

		obj = new JSONObject();
		obj.put(TimeSeriesSchema.ATT_CONDID, condId);
		obj.put(TimeSeriesSchema.ATT_COMBASEID, combaseId);
		obj.put(TimeSeriesSchema.ATT_MISC, new JSONMiscList(miscList).getObj());
		obj.put(TimeSeriesSchema.ATT_AGENT, new JSONAgent(organism).getObj());
		obj.put(TimeSeriesSchema.ATT_MATRIX, new JSONMatrix(matrix).getObj());
		obj.put(TimeSeriesSchema.ATT_TIMESERIES, new JSONData(data).getObj());
		obj.put(TimeSeriesSchema.ATT_MDINFO, new JSONMdInfo(mdInfo).getObj());
		obj.put(TimeSeriesSchema.ATT_LITMD, new JSONLiteratureList(lits).getObj());
		obj.put(TimeSeriesSchema.ATT_DBUUID, mDBUID);
	}

	public JSONObject getObj() {
		return obj;
	}

	public KnimeTuple toKnimeTuple() {
		KnimeTuple tuple = new KnimeTuple(SchemaFactory.createDataSchema());

		// Set CondID
		if (obj.containsKey(TimeSeriesSchema.ATT_CONDID)) {
			int condID = ((Long)obj.get(TimeSeriesSchema.ATT_CONDID)).intValue();
			tuple.setValue(TimeSeriesSchema.ATT_CONDID, condID);
		}

		// Set CombaseID
		if (obj.containsKey(TimeSeriesSchema.ATT_COMBASEID)) {
			String combaseID = (String) obj.get(TimeSeriesSchema.ATT_COMBASEID);
			tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, combaseID);
		}

		// Set misc
		if (obj.containsKey(TimeSeriesSchema.ATT_MISC)) {
			JSONArray ja = (JSONArray) obj.get(TimeSeriesSchema.ATT_MISC);
			PmmXmlDoc miscCell = new PmmXmlDoc();
			for (MiscXml xml : new JSONMiscList(ja).toMiscXml()) {
				miscCell.add(xml);
			}
			tuple.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
		}

		// Set agent
		if (obj.containsKey(TimeSeriesSchema.ATT_AGENT)) {
			JSONObject jo = (JSONObject)obj.get(TimeSeriesSchema.ATT_AGENT);
			AgentXml xml = new JSONAgent(jo).toAgentXml();
			tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(xml));
		}

		// Set matrix
		if (obj.containsKey(TimeSeriesSchema.ATT_MATRIX)) {
			JSONObject jo = (JSONObject) obj.get(TimeSeriesSchema.ATT_MATRIX);
			MatrixXml xml = new JSONMatrix(jo).toMatrixXml();
			tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(xml));
		}

		// Set model data
		if (obj.containsKey(TimeSeriesSchema.ATT_TIMESERIES)) {
			JSONArray ja = (JSONArray) obj.get(TimeSeriesSchema.ATT_TIMESERIES);
			PmmXmlDoc data = new PmmXmlDoc();
			for (TimeSeriesXml xml : new JSONData(ja).toTimeSeriesXml()) {
				data.add(xml);
			}
			tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, data);
		}

		// Set model info
		if (obj.containsKey(TimeSeriesSchema.ATT_MDINFO)) {
			JSONObject jo = (JSONObject) obj.get(TimeSeriesSchema.ATT_MDINFO);
			MdInfoXml mdInfo = new JSONMdInfo(jo).toMdInfoXml();
			tuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
		}

		// Set model literature
		if (obj.containsKey(TimeSeriesSchema.ATT_LITMD)) {
			JSONArray ja = (JSONArray) obj.get(TimeSeriesSchema.ATT_LITMD);
			PmmXmlDoc litCell = new PmmXmlDoc();
			for (LiteratureItem lit : new JSONLiteratureList(ja).toLiteratureItem()) {
				litCell.add(lit);
			}
			tuple.setValue(TimeSeriesSchema.ATT_LITMD, litCell);
		}

		// Set dbuuid
		if (obj.containsKey(TimeSeriesSchema.ATT_DBUUID)) {
			String dbuuid = (String) obj.get(TimeSeriesSchema.ATT_DBUUID);
			tuple.setValue(TimeSeriesSchema.ATT_DBUUID, dbuuid);
		}

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
