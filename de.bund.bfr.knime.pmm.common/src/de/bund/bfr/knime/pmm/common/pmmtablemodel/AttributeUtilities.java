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
package de.bund.bfr.knime.pmm.common.pmmtablemodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.bund.bfr.knime.pmm.common.chart.ChartConstants;

public class AttributeUtilities {
	
	public static final String DATAID = "DataID";
	public static final String DATAPOINTS = "Data Points";

	public static final String TIME = "Time";
	public static final String LOGC = "Log10C";
	public static final String ATT_TEMPERATURE = "Temperature";
	public static final String ATT_PH = "pH";
	public static final String ATT_WATERACTIVITY = "aw";
	public static final int ATT_TEMPERATURE_ID = -1;
	public static final int ATT_PH_ID = -2;
	public static final int ATT_AW_ID = -3;

	private static final String HOURS = "h";
	private static final String MINUTES = "min";
	private static final String SECONDS = "sec";
	private static final String DAYS = "days";
	private static final String WEEKS = "weeks";

	private static final String LOGCFU = "log cfu/g";
	private static final String LNCFU = "ln cfu/g";
	private static final String CFU = "cfu/g";

	private static final String CELSIUS = "°C";
	private static final String FAHRENHEIT = "°F";

	private AttributeUtilities() {
	}

	public static String getName(String attr) {
		return attr;
	}

	public static String getName(String attr, String transform) {
		return transform + "(" + attr + ")";
	}

	public static String getNameWithUnit(String attr) {
		String unit = getStandardUnit(attr);

		if (unit != null) {
			return getName(attr) + " [" + unit + "]";
		} else {
			return getName(attr);
		}
	}

	public static String getNameWithUnit(String attr, String transform) {
		String unit = getStandardUnit(attr);

		if (unit != null) {
			return getName(attr, transform) + " [" + transform + "(" + unit
					+ ")]";
		} else {
			return getName(attr, transform);
		}
	}

	public static String getFullName(String attr) {
		if (attr.equals(ATT_TEMPERATURE)) {
			return "Temperature";
		} else if (attr.equals(ATT_PH)) {
			return "pH";
		} else if (attr.equals(ATT_WATERACTIVITY)) {
			return "Water Activity";
		} else if (attr.equals(LOGC)) {
			return "Concentration";
		} else {
			return attr;
		}
	}

	public static String getFullName(String attr, String transform) {
		if (transform.equals(ChartConstants.NO_TRANSFORM)) {
			return getFullName(attr);
		} else {
			return transform + "(" + getFullName(attr) + ")";
		}
	}

	public static String getFullNameWithUnit(String attr) {
		String unit = getStandardUnit(attr);

		if (unit != null) {
			return getFullName(attr) + " [" + unit + "]";
		} else {
			return getFullName(attr);
		}
	}

	public static String getFullNameWithUnit(String attr, String transform) {
		String unit = getStandardUnit(attr);

		if (transform.equals(ChartConstants.NO_TRANSFORM)) {
			return getFullNameWithUnit(attr);
		} else if (unit != null) {
			return getFullName(attr, transform) + " [" + transform + "(" + unit
					+ ")]";
		} else {
			return getFullName(attr, transform);
		}
	}

	public static List<String> getUnitsForAttribute(String attr) {
		if (attr.equals(TIME)) {
			return Arrays.asList(HOURS, MINUTES, SECONDS, DAYS, WEEKS);
		} else if (attr.equals(LOGC)) {
			return Arrays.asList(LOGCFU, LNCFU, CFU);
		} else if (attr.equals(ATT_TEMPERATURE)) {
			return Arrays.asList(CELSIUS, FAHRENHEIT);
		} else {
			return new ArrayList<String>();
		}
	}

	public static Double convertToStandardUnit(String attr, Double value,
			String unit) {
		if (value == null || value.isNaN()) {
			return null;
		}

		if (attr.equals(TIME)) {
			if (unit.equals(HOURS)) {
				return value;
			} else if (unit.equals(MINUTES)) {
				return value / 60.0;
			} else if (unit.equals(SECONDS)) {
				return value / 3600.0;
			} else if (unit.equals(DAYS)) {
				return value * 24.0;
			} else if (unit.equals(WEEKS)) {
				return value * 168.0;
			}
		} else if (attr.equals(LOGC)) {
			if (unit.equals(LOGCFU)) {
				return value;
			} else if (unit.equals(LNCFU)) {
				return Math.log10(Math.exp(value));
			} else if (unit.equals(CFU)) {
				return Math.log10(value);
			}
		} else if (attr.equals(ATT_TEMPERATURE)) {
			if (unit.equals(CELSIUS)) {
				return value;
			} else if (unit.equals(FAHRENHEIT)) {
				return (value - 32.0) * 5.0 / 9.0;
			}
		}

		return null;
	}

	public static String getStandardUnit(String attr) {
		if (attr.equals(TIME)) {
			return HOURS;
		} else if (attr.equals(LOGC)) {
			return LOGCFU;
		} else if (attr.equals(ATT_TEMPERATURE)) {
			return CELSIUS;
		} else {
			return null;
		}
	}

}
