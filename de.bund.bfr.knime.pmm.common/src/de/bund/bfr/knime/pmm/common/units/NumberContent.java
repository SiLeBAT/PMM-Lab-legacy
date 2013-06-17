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
import java.util.List;

public class NumberContent implements Category {

	public static final String COUNT_PER_GRAMM = "count/g";
	public static final String LOG_COUNT_PER_GRAMM = "log10(count/g)";
	public static final String LN_COUNT_PER_GRAMM = "ln(count/g)";

	public NumberContent() {
	}

	@Override
	public String getName() {
		return Categories.NUMBER_CONTENT;
	}

	@Override
	public List<String> getAllUnits() {
		return Arrays.asList(COUNT_PER_GRAMM, LOG_COUNT_PER_GRAMM,
				LN_COUNT_PER_GRAMM);
	}

	@Override
	public String getStandardUnit() {
		return LOG_COUNT_PER_GRAMM;
	}

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		return fromCFU(toCFU(value, fromUnit), toUnit);
	}

	private Double toCFU(Double value, String unit) {
		if (value == null) {
			return null;
		}

		switch (unit) {
		case COUNT_PER_GRAMM:
			return value;
		case LOG_COUNT_PER_GRAMM:
			return Math.pow(10.0, value);
		case LN_COUNT_PER_GRAMM:
			return Math.exp(value);
		default:
			return null;
		}
	}

	private Double fromCFU(Double value, String unit) {
		if (value == null) {
			return null;
		}

		switch (unit) {
		case COUNT_PER_GRAMM:
			return value;
		case LOG_COUNT_PER_GRAMM:
			return Math.log10(value);
		case LN_COUNT_PER_GRAMM:
			return Math.log(value);
		default:
			return null;
		}
	}

}
