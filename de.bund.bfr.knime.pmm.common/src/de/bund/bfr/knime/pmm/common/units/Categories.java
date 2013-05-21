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

public class Categories {

	public static final String BACTERIAL_CONCENTRATION = "Bacterial Concentration";
	public static final String PH = "pH";
	public static final String PRESSURE = "Pressure";
	public static final String SOLUTION_CONCENTRATION = "Solution Concentration";
	public static final String TEMPERATURE = "Temperature";
	public static final String TIME = "Time";
	public static final String TRUE_FALSE_VALUE = "True/False Value";
	public static final String WATER_ACTIVITY = "Water Activity";

	public static String[] getAllCategories() {
		return new String[] { BACTERIAL_CONCENTRATION, PH, PRESSURE,
				SOLUTION_CONCENTRATION, TEMPERATURE, TIME, TRUE_FALSE_VALUE,
				WATER_ACTIVITY };
	}

	public static Category getCategory(String id) {
		if (id == null) {
			return new NoCategory();
		}

		switch (id) {
		case BACTERIAL_CONCENTRATION:
			return new BacterialConcentration();
		case PH:
			return new PH();
		case PRESSURE:
			return new Pressure();
		case SOLUTION_CONCENTRATION:
			return new SolutionConcentration();
		case TEMPERATURE:
			return new Temperature();
		case TIME:
			return new Time();
		case TRUE_FALSE_VALUE:
			return new TrueFalseValue();
		case WATER_ACTIVITY:
			return new WaterActivity();
		default:
			return new NoCategory();
		}
	}
}
