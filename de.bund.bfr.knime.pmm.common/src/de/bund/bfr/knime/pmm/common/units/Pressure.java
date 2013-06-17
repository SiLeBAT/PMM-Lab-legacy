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

public class Pressure implements Category {

	public static final String PASCAL = "Pa";
	public static final String KILO_PASCAL = "kPa";
	public static final String MEGA_PASCAL = "MPa";
	public static final String BAR = "bar";
	public static final String MILLI_BAR = "mbar";
	public static final String KILO_BAR = "kbar";

	public Pressure() {
	}

	@Override
	public String getName() {
		return Categories.PRESSURE;
	}

	@Override
	public List<String> getAllUnits() {
		return Arrays.asList(PASCAL, KILO_PASCAL, MEGA_PASCAL, BAR, MILLI_BAR,
				KILO_BAR);
	}

	@Override
	public String getStandardUnit() {
		return PASCAL;
	}

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		Map<String, Double> factors = new LinkedHashMap<>();

		factors.put(PASCAL, 1.0);
		factors.put(KILO_PASCAL, 1e3);
		factors.put(MEGA_PASCAL, 1e6);
		factors.put(BAR, 1e5);
		factors.put(MILLI_BAR, 1e2);
		factors.put(KILO_BAR, 1e8);

		if (value == null || factors.get(fromUnit) == null
				|| factors.get(toUnit) == null) {
			return null;
		}

		return value * factors.get(fromUnit) / factors.get(toUnit);
	}

}
