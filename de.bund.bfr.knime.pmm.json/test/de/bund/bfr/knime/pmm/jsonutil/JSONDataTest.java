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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.TimeSeriesXml;

public class JSONDataTest {

	@Test
	public void test() {

		Double concentration = 6.147902198294102;
		Double concentrationStdDev = null;
		String concentrationUnit = "ln(count/g)";
		String concentrationUnitObjectType = "CFU";
		String name = "t0";
		Integer numberOfMeasurements = null;
		String origConcentrationUnit = "log10(count/g)";
		String origTimeUnit = "h";
		Double time = 0.0;
		String timeUnit = "h";

		// 1st data point
		TimeSeriesXml ts1 = new TimeSeriesXml(name, time, timeUnit,
				origTimeUnit, concentration, concentrationUnit,
				concentrationUnitObjectType, origConcentrationUnit,
				concentrationStdDev, numberOfMeasurements);
		
		// 2nd data point
		TimeSeriesXml ts2 = new TimeSeriesXml(ts1.toXmlElement());
		ts2.setName("t1");
		ts2.setConcentration(6.700522620612674);
		ts2.setTime(50.882);
		
		// 3rd data point
		TimeSeriesXml ts3 = new TimeSeriesXml(ts1.toXmlElement());
		ts3.setName("t2");
		ts3.setConcentration(6.6084192168929095);
		ts3.setTime(73.018);
		
		List<TimeSeriesXml> expectedData = Arrays.asList(ts1, ts2, ts3);
		JSONData jsonData = new JSONData(expectedData);
		List<TimeSeriesXml> obtainedData = jsonData.toTimeSeriesXml();
		
		// Tests obtainedData
		for (int i = 0; i < obtainedData.size(); i++) {
			TimeSeriesXml expectedTS = expectedData.get(i);
			TimeSeriesXml obtainedTS = obtainedData.get(i);
			
			assertEquals(expectedTS.getConcentration(), obtainedTS.getConcentration());
			assertEquals(expectedTS.getConcentrationStdDev(), obtainedTS.getConcentrationStdDev());
			assertEquals(expectedTS.getConcentrationUnit(), obtainedTS.getConcentrationUnit());
			assertEquals(expectedTS.getConcentrationUnitObjectType(), obtainedTS.getConcentrationUnitObjectType());
			assertEquals(expectedTS.getName(), obtainedTS.getName());
			assertEquals(expectedTS.getNumberOfMeasurements(), obtainedTS.getNumberOfMeasurements());
			assertEquals(expectedTS.getOrigConcentrationUnit(), obtainedTS.getOrigConcentrationUnit());
			assertEquals(expectedTS.getOrigTimeUnit(), obtainedTS.getOrigTimeUnit());
			assertEquals(expectedTS.getTime(), obtainedTS.getTime());
			assertEquals(expectedTS.getTimeUnit(), obtainedTS.getTimeUnit());
		}

	}
}
