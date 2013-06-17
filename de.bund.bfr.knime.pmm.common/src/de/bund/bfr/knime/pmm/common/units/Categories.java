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

import java.util.List;

public class Categories {

	public static final String NO_CATEGORY = "No Category";

	public static final String TIME = "Time";
	public static final String NUMBER_CONTENT = "Number Content";
	public static final String NUMBER_CONCENTRATION = "Number Concentration";
	public static final String TEMPERATURE = "Temperature";
	public static final String PH = "pH";
	public static final String WATER_ACTIVITY = "Water Activity";
	public static final String MASS_CONCENTRATION = "Mass Concentration";
	public static final String MASS_RATIO = "Mass Ratio";
	public static final String PRESSURE = "Pressure";
	public static final String TRUE_FALSE_VALUE = "True/False Value";

	public static String[] getAllCategories() {
		return new String[] { TIME, NUMBER_CONTENT, NUMBER_CONCENTRATION,
				TEMPERATURE, PH, WATER_ACTIVITY, MASS_CONCENTRATION,
				MASS_RATIO, PRESSURE, TRUE_FALSE_VALUE };
	}

	public static Category getCategory(String id) {
		if (id == null) {
			return new NoCategory();
		}

		switch (id) {
		case TIME:
			return new Time();
		case NUMBER_CONTENT:
			return new NumberContent();
		case NUMBER_CONCENTRATION:
			return new NumberConcentration();
		case TEMPERATURE:
			return new Temperature();
		case PH:
			return new PH();
		case WATER_ACTIVITY:
			return new WaterActivity();
		case MASS_CONCENTRATION:
			return new MassConcentration();
		case MASS_RATIO:
			return new MassRatio();
		case PRESSURE:
			return new Pressure();
		case TRUE_FALSE_VALUE:
			return new TrueFalseValue();
		default:
			return new NoCategory();
		}
	}

	public static Category getCategoryByUnit(List<String> categories,
			String unit) {
		Category category = null;

		for (String s : categories) {
			Category c = Categories.getCategory(s);

			if (c.getAllUnits().contains(unit)) {
				category = c;
				break;
			}
		}

		return category;
	}
}
