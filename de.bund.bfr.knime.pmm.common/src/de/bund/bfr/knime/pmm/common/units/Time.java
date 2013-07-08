/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
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
 ******************************************************************************/
package de.bund.bfr.knime.pmm.common.units;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class Time implements Category {

	public static final String SECOND = "s";
	public static final String MINUTE = "min";
	public static final String HOUR = "h";
	public static final String DAY = "day";
	public static final String WEEK = "week";

	public Time() {
	}

	@Override
	public String getName() {
		return Categories.TIME;
	}

	@Override
	public List<String> getAllUnits() {
		return Arrays.asList(SECOND, MINUTE, HOUR, DAY, WEEK);
	}

	@Override
	public String getStandardUnit() {
		return HOUR;
	}

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		Map<String, Double> factors = new LinkedHashMap<>();

		factors.put(SECOND, 1.0);
		factors.put(MINUTE, 60.0);
		factors.put(HOUR, 60 * 60.0);
		factors.put(DAY, 24.0 * 60.0 * 60.0);
		factors.put(WEEK, 7.0 * 24.0 * 60.0 * 60.0);

		if (value == null || factors.get(fromUnit) == null
				|| factors.get(toUnit) == null) {
			return null;
		}

		return value * factors.get(fromUnit) / factors.get(toUnit);
	}

}
