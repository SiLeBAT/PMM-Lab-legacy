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

import de.bund.bfr.knime.pmm.common.chart.ChartConstants;

public class AttributeUtilities {

	public static final String DATAID = "DataID";

	public static final String TIME = "Time";
	public static final String LOGC = "Log10C";
	public static final String ATT_TEMPERATURE = "Temperature";
	public static final String ATT_PH = "pH";
	public static final String ATT_WATERACTIVITY = "aw";
	public static final int ATT_TEMPERATURE_ID = -1;
	public static final int ATT_PH_ID = -2;
	public static final int ATT_AW_ID = -3;

	public static final String HOURS = "h";
	public static final String MINUTES = "min";
	public static final String SECONDS = "sec";
	public static final String DAYS = "days";
	public static final String WEEKS = "weeks";

	public static final String LOGCFU = "log10(...)";
	public static final String LNCFU = "ln(...)";
	public static final String CFU = "...";

	public static final String CELSIUS = "°C";
	public static final String FAHRENHEIT = "°F";

	public static final String AGENT_DETAILS = TimeSeriesSchema.ATT_AGENT
			+ " Details";
	public static final String MATRIX_DETAILS = TimeSeriesSchema.ATT_MATRIX
			+ " Details";

	private AttributeUtilities() {
	}

	public static String getName(String attr) {
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

	public static String getName(String attr, String transform) {
		if (transform.equals(ChartConstants.NO_TRANSFORM)) {
			return getName(attr);
		} else {
			return transform + "(" + getName(attr) + ")";
		}
	}

	public static String getNameWithUnit(String attr, String unit) {
		if (unit != null) {
			return getName(attr) + " [" + unit + "]";
		} else {
			return getName(attr);
		}
	}

	public static String getNameWithUnit(String attr, String unit,
			String transform) {
		if (transform == null || transform.equals(ChartConstants.NO_TRANSFORM)) {
			return getNameWithUnit(attr, unit);
		} else if (unit != null) {
			return getName(attr, transform) + " [" + transform + "(" + unit
					+ ")]";
		} else {
			return getName(attr, transform);
		}
	}

}
