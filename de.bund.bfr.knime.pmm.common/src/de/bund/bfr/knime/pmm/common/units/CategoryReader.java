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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hsh.bfr.db.UnitsFromDB;
import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class CategoryReader {

	private static CategoryReader instance = null;

	private Map<String, Category> categories;

	private CategoryReader() {
		UnitsFromDB unitDB = new UnitsFromDB();

		unitDB.askDB();

		Map<Integer, UnitsFromDB> map = unitDB.getMap();

		categories = new LinkedHashMap<>();

		for (UnitsFromDB unit : map.values()) {
			String categoryName = unit.getKind_of_property_quantity();

			if (categoryName != null && !categories.containsKey(categoryName)) {
				categories.put(categoryName, createCategory(map, categoryName));
			}
		}
	}

	public static CategoryReader getInstance() {
		if (instance == null) {
			instance = new CategoryReader();
		}

		return instance;
	}

	public Map<String, Category> getMap() {
		return categories;
	}

	private Category createCategory(Map<Integer, UnitsFromDB> units,
			String categoryName) {
		DJep parser = MathUtilities.createParser();
		Map<String, Node> fromFormulas = new LinkedHashMap<>();
		Map<String, Node> toFormulas = new LinkedHashMap<>();
		Map<String, String> fromFormulaStrings = new LinkedHashMap<>();
		Map<String, String> toFormulaStrings = new LinkedHashMap<>();
		String standardUnit = null;

		parser.addVariable("x", 0.0);

		for (UnitsFromDB unit : units.values()) {
			if (!categoryName.equals(unit.getKind_of_property_quantity())) {
				continue;
			}

			String unitName = unit.getDisplay_in_GUI_as();

			if (unit.getPriority_for_display_in_GUI() != null
					&& unit.getPriority_for_display_in_GUI().equals("TRUE")) {
				standardUnit = unitName;
			}

			if (unit.getConversion_function_factor() != null) {
				try {
					fromFormulas.put(unitName,
							parser.parse(unit.getConversion_function_factor()));
					fromFormulaStrings.put(unitName,
							unit.getConversion_function_factor());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			if (unit.getInverse_conversion_function_factor() != null) {
				try {
					toFormulas.put(unitName, parser.parse(unit
							.getInverse_conversion_function_factor()));
					toFormulaStrings.put(unitName,
							unit.getInverse_conversion_function_factor());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		return new DBCategory(categoryName, standardUnit, fromFormulas,
				toFormulas, fromFormulaStrings, toFormulaStrings, parser);
	}

	private static class DBCategory implements Category {

		private String name;
		private String standardUnit;
		private Map<String, Node> fromFormulas;
		private Map<String, Node> toFormulas;
		private Map<String, String> fromFormulaStrings;
		private Map<String, String> toFormulaStrings;
		private DJep parser;

		public DBCategory(String name, String standardUnit,
				Map<String, Node> fromFormulas, Map<String, Node> toFormulas,
				Map<String, String> fromFormulaStrings,
				Map<String, String> toFormulaStrings, DJep parser) {
			this.name = name;
			this.standardUnit = standardUnit;
			this.fromFormulas = fromFormulas;
			this.toFormulas = toFormulas;
			this.fromFormulaStrings = fromFormulaStrings;
			this.toFormulaStrings = toFormulaStrings;
			this.parser = parser;
		}

		@Override
		public String getStandardUnit() {
			return standardUnit;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public List<String> getAllUnits() {
			return new ArrayList<>(fromFormulas.keySet());
		}

		@Override
		public Double convert(Double value, String fromUnit, String toUnit)
				throws ConvertException {
			if (fromUnit != null && fromUnit.equals(toUnit)) {
				return value;
			}

			if (value == null) {
				return null;
			}

			if (fromUnit == null || toUnit == null
					|| fromFormulas.get(fromUnit) == null
					|| toFormulas.get(toUnit) == null) {
				throw new ConvertException(fromUnit, toUnit);
			}

			return apply(apply(value, fromFormulas.get(fromUnit)),
					toFormulas.get(toUnit));
		}

		@Override
		public String getConversionString(String var, String fromUnit,
				String toUnit) throws ConvertException {
			String from = fromFormulaStrings.get(fromUnit);
			String to = toFormulaStrings.get(toUnit);

			if (from == null || to == null) {
				throw new ConvertException(fromUnit, toUnit);
			}

			from = MathUtilities.replaceVariable(from, "x", var);
			to = MathUtilities.replaceVariable(to, "x", "(" + from + ")");

			return to;
		}

		private Double apply(Double value, Node formula) {
			parser.setVarValue("x", value);

			try {
				return (Double) parser.evaluate(formula);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
