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
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.TimeSeriesXml;

public class JSONData {
	JSONArray obj;
	
	// attribute keys
	static final String ATT_NAME = "name";
	static final String ATT_TIME = "time";
	static final String ATT_TIME_UNIT = "timeUnit";
	static final String ATT_ORIG_TIME_UNIT = "origTimeUnit";
	static final String ATT_CONCENTRATION = "concentration";
	static final String ATT_CONCENTRATION_UNIT = "concentrationUnit";
	static final String ATT_CONCENTRATION_UNIT_OBJECT_TYPE = "concentrationUnitObjectType";
	static final String ATT_ORIG_CONCENTRATION_UNIT = "origConcentrationUnit";
	static final String ATT_CONCENTRATION_STDDEV = "concentrationStdDev";
	static final String ATT_NUMBER_OF_MEASUREMENTS = "numberOfMeasurements";

	public JSONData(JSONArray obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONData(List<TimeSeriesXml> timeSeries) {
		obj = new JSONArray();

		for (TimeSeriesXml point : timeSeries) {
			JSONObject jp = new JSONObject();

			jp.put(ATT_NAME, point.getName());
			jp.put(ATT_TIME, point.getTime());
			jp.put(ATT_TIME_UNIT, point.getTimeUnit());
			jp.put(ATT_ORIG_TIME_UNIT, point.getOrigTimeUnit());
			jp.put(ATT_CONCENTRATION, point.getConcentration());
			jp.put(ATT_CONCENTRATION_UNIT, point.getConcentrationUnit());
			jp.put(ATT_CONCENTRATION_UNIT_OBJECT_TYPE, point.getConcentrationUnitObjectType());
			jp.put(ATT_ORIG_CONCENTRATION_UNIT, point.getOrigConcentrationUnit());
			jp.put(ATT_CONCENTRATION_STDDEV, point.getConcentrationStdDev());
			jp.put(ATT_NUMBER_OF_MEASUREMENTS, point.getNumberOfMeasurements());

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

			String name = (String) jp.get(ATT_NAME);
			double time = (double) jp.get(ATT_TIME);
			String timeUnit = (String) jp.get(ATT_TIME_UNIT);
			String origTimeUnit = (String) jp.get(ATT_ORIG_TIME_UNIT);
			double concentration = (double) jp.get(ATT_CONCENTRATION);
			String concentrationUnit = (String) jp.get(ATT_CONCENTRATION_UNIT);
			String concentrationUnitObjectType = (String) jp
					.get(ATT_CONCENTRATION_UNIT_OBJECT_TYPE);
			String origConcentrationUnit = (String) jp
					.get(ATT_ORIG_CONCENTRATION_UNIT);
			Double concentrationStdDev = (Double) jp.get(ATT_CONCENTRATION_STDDEV);
			Integer numberOfMeasurements = (Integer) jp
					.get(ATT_NUMBER_OF_MEASUREMENTS);

			TimeSeriesXml point = new TimeSeriesXml(name, time, timeUnit,
					origTimeUnit, concentration, concentrationUnit,
					concentrationUnitObjectType, origConcentrationUnit,
					concentrationStdDev, numberOfMeasurements);
			ts.add(point);
		}

		return ts;
	}
}
