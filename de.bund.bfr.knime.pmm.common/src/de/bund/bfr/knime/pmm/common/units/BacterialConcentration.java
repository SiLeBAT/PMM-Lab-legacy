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

import java.util.LinkedHashSet;
import java.util.Set;

public class BacterialConcentration implements Category {

	public static final String CFU_PER_GRAMM = "CFU/g";
	public static final String LOG_CFU_PER_GRAMM = "log CFU/g";
	public static final String LN_CFU_PER_GRAMM = "ln CFU/g";
	public static final String CFU_PER_MILLILITER = "CFU/ml";
	public static final String LOG_CFU_PER_MILLILITER = "log CFU/ml";
	public static final String LN_CFU_PER_MILLILITER = "ln CFU/ml";

	public BacterialConcentration() {
	}

	@Override
	public String[] getAllUnits() {
		return new String[] { CFU_PER_GRAMM, LOG_CFU_PER_GRAMM,
				LN_CFU_PER_GRAMM, CFU_PER_MILLILITER, LOG_CFU_PER_MILLILITER,
				LN_CFU_PER_MILLILITER };
	}
	
	@Override
	public String getStandardUnit() {		
		return LOG_CFU_PER_GRAMM;
	}

	@Override
	public Double convert(Double value, String fromUnit, String toUnit) {
		if (value == null || !canConvert(fromUnit, toUnit)) {
			return null;
		}

		return fromCFU(toCFU(value, fromUnit), toUnit);
	}

	@Override
	public boolean canConvert(String fromUnit, String toUnit) {
		Set<String> weightUnits = new LinkedHashSet<>();
		Set<String> volumeUnits = new LinkedHashSet<>();
		
		weightUnits.add(CFU_PER_GRAMM);
		weightUnits.add(LOG_CFU_PER_GRAMM);
		weightUnits.add(LN_CFU_PER_GRAMM);		
		volumeUnits.add(CFU_PER_MILLILITER);
		volumeUnits.add(LOG_CFU_PER_MILLILITER);
		volumeUnits.add(LN_CFU_PER_MILLILITER);

		return (weightUnits.contains(fromUnit) && weightUnits.contains(toUnit))
				|| (volumeUnits.contains(fromUnit) && volumeUnits
						.contains(toUnit));
	}

	private Double toCFU(Double value, String unit) {
		if (value == null) {
			return null;
		}

		switch (unit) {
		case CFU_PER_GRAMM:
		case CFU_PER_MILLILITER:
			return value;
		case LOG_CFU_PER_GRAMM:
		case LOG_CFU_PER_MILLILITER:
			return Math.pow(10.0, value);
		case LN_CFU_PER_GRAMM:
		case LN_CFU_PER_MILLILITER:
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
		case CFU_PER_GRAMM:
		case CFU_PER_MILLILITER:
			return value;
		case LOG_CFU_PER_GRAMM:
		case LOG_CFU_PER_MILLILITER:
			return Math.log10(value);
		case LN_CFU_PER_GRAMM:
		case LN_CFU_PER_MILLILITER:
			return Math.log(value);
		default:
			return null;
		}
	}

}
