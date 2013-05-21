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

public class Temperature implements Category {

	public static final String CELSIUS = "°C";
	public static final String FAHRENHEIT = "°F";

	public Temperature() {
	}

	@Override
	public String[] getAllUnits() {
		return new String[] { CELSIUS, FAHRENHEIT };
	}
	
	@Override
	public String getStandardUnit() {		
		return CELSIUS;
	}

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		return fromCelsius(toCelsius(value, fromUnit), toUnit);
	}

	@Override
	public boolean canConvert(String fromUnit, String toUnit) {
		return true;
	}

	private Double toCelsius(Double value, String unit) {
		if (value == null) {
			return null;
		}

		switch (unit) {
		case CELSIUS:
			return value;
		case FAHRENHEIT:
			return (value - 32.0) * 5.0 / 9.0;
		default:
			return null;
		}
	}

	private Double fromCelsius(Double value, String unit) {
		if (value == null) {
			return null;
		}

		switch (unit) {
		case CELSIUS:
			return value;
		case FAHRENHEIT:
			return value * 9.0 / 5.0 + 32.0;
		default:
			return null;
		}
	}

}
