/*******************************************************************************
 * PMM-Lab © 2013, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2013, Federal Institute for Risk Assessment (BfR), Germany
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
package de.bund.bfr.knime.pmm.forecaststaticconditions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.common.XmlConverter;

public class SettingsHelper {

	protected static final String CFGKEY_CONCENTRATION = "Concentration";
	protected static final String CFGKEY_CONCENTRATIONPARAMETERS = "ConcentrationParameters";

	protected static final double DEFAULT_CONCENTRATION = 3.0;

	private double concentration;
	private Map<String, String> concentrationParameters;

	public SettingsHelper() {
		concentration = DEFAULT_CONCENTRATION;
		concentrationParameters = new LinkedHashMap<>();
	}

	public void loadSettings(NodeSettingsRO settings) {
		try {
			concentration = settings.getDouble(CFGKEY_CONCENTRATION);
		} catch (InvalidSettingsException e) {
			concentration = DEFAULT_CONCENTRATION;
		}

		try {
			concentrationParameters = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_CONCENTRATIONPARAMETERS),
					new LinkedHashMap<String, String>());
		} catch (InvalidSettingsException e) {
			concentrationParameters = new LinkedHashMap<String, String>();
		}
	}

	public void saveSettings(NodeSettingsWO settings) {
		settings.addDouble(CFGKEY_CONCENTRATION, concentration);
		settings.addString(CFGKEY_CONCENTRATIONPARAMETERS,
				XmlConverter.objectToXml(concentrationParameters));
	}

	public double getConcentration() {
		return concentration;
	}

	public void setConcentration(double concentration) {
		this.concentration = concentration;
	}

	public Map<String, String> getConcentrationParameters() {
		return concentrationParameters;
	}

	public void setConcentrationParameters(
			Map<String, String> concentrationParameters) {
		this.concentrationParameters = concentrationParameters;
	}
}
