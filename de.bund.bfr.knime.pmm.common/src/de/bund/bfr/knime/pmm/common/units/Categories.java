/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Categories {

	public static final String NO_CATEGORY = "No Category";

	public static List<String> getAllCategories() {
		return new ArrayList<String>(CategoryReader.getInstance().getMap().keySet());
	}

	public static Category getCategory(String id) {
		if (id == null) {
			return new NoCategory();
		}

		Category category = CategoryReader.getInstance().getMap().get(id);

		if (category == null) {
			return new NoCategory();
		}

		return category;
	}

	public static Category getCategoryByUnit(String unit) {
		Category category = new NoCategory();

		for (Object o : getAllCategories()) {
			Category cat;

			if (o instanceof Category) {
				cat = (Category) o;
			} else if (o instanceof String) {
				cat = Categories.getCategory((String) o);
			} else {
				continue;
			}

			if (cat.getAllUnits().contains(unit)) {
				category = cat;
				break;
			}
		}

		return category;
	}

	public static List<String> getUnitsFromCategories(List<?> categories) {
		List<String> units = new ArrayList<String>();

		if (categories != null) {
			for (Object o : categories) {
				if (o instanceof Category) {
					units.addAll(((Category) o).getAllUnits());
				} else if (o instanceof String) {
					units.addAll(Categories.getCategory((String) o)
							.getAllUnits());
				}
			}
		}

		return units;
	}

	public static String getTime() {
		return "Time";
	}

	public static Category getTimeCategory() {
		return getCategory(getTime());
	}

	public static List<String> getConcentrations() {
		return Arrays.asList("Number Content (count/mass)",
				"Number Concentration (count/vol)",
				"Number Aeric (number/area)", "Number",
				"Number Concentration Difference", "Number Content Difference",
				"Number Difference", "Number Fraction", "Arbitrary Fraction",
				"Energy Content", "Energy Content Rate", "Mass",
				"Mass Concentration", "Mass Ratio", "Mole Content",
				"True/False Value", "Volume Concentration", "Volume Ratio");
	}

	public static List<Category> getConcentrationCategories() {
		List<Category> categories = new ArrayList<Category>();

		for (String name : getConcentrations()) {
			categories.add(getCategory(name));
		}

		return categories;
	}

	public static String getTemp() {
		return "Temperature";
	}

	public static Category getTempCategory() {
		return getCategory(getTemp());
	}

	public static String getPh() {
		return "Dimensionless quantity";
	}

	public static Category getPhCategory() {
		return getCategory(getPh());
	}

	public static String getAw() {
		return "Dimensionless quantity";
	}

	public static Category getAwCategory() {
		return getCategory(getAw());
	}
}
