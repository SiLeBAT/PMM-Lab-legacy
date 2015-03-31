package de.bund.bfr.knime.pmm.jsonutil;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.TimeSeriesXml;

public class JSONData {
	private JSONArray obj;

	public JSONData(JSONArray obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONData(List<TimeSeriesXml> timeSeries) {
		obj = new JSONArray();

		for (TimeSeriesXml point : timeSeries) {
			JSONObject jp = new JSONObject();

			jp.put("concentration", point.getConcentration());
			jp.put("concentrationStdDev", point.getConcentrationStdDev());
			jp.put("concentrationUnitObjectType",
					point.getConcentrationUnitObjectType());
			jp.put("name", point.getName());
			jp.put("numberOfMeasurements", point.getNumberOfMeasurements());
			jp.put("origConcentrationUnit", point.getOrigConcentrationUnit());
			jp.put("origTimeUnit", point.getOrigTimeUnit());
			jp.put("time", point.getTime());
			jp.put("timeUnit", point.getTimeUnit());

			obj.add(jp);
		}
	}

	public JSONArray getObj() {
		return obj;
	}

	public List<TimeSeriesXml> toTimeSeriesXml() {
		List<TimeSeriesXml> ts = new ArrayList<>();

		for (int i = 0; i < obj.size(); i++) {
			JSONObject jp = (JSONObject) obj.get(i);

			String name = (String) jp.get("name");
			double time = (double) jp.get("time");
			String timeUnit = (String) jp.get("timeUnit");
			String origTimeUnit = (String) jp.get("origTimeUnit");
			double concentration = (double) jp.get("concentration");
			String concentrationUnit = (String) jp.get("concentrationUnit");
			String concentrationUnitObjectType = (String) jp
					.get("concentrationUnitObjectType");
			String origConcentrationUnit = (String) jp
					.get("origConcentrationUnit");
			Double concentrationStdDev = (Double) jp.get("concentrationStdDev");
			Integer numberOfMeasurements = (Integer) jp.get("numberOfMeasurements");

			TimeSeriesXml point = new TimeSeriesXml(name, time, timeUnit,
					origTimeUnit, concentration, concentrationUnit,
					concentrationUnitObjectType, origConcentrationUnit,
					concentrationStdDev, numberOfMeasurements);
			ts.add(point);
		}

		return ts;
	}
}
