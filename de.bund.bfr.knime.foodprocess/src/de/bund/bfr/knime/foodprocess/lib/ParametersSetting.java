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
package de.bund.bfr.knime.foodprocess.lib;

import lombok.Data;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.config.Config;

@Data
public class ParametersSetting {
	private final String PARAM_TEMPERATURE = "temperature";
	private final String PARAM_TEMPERATURE_UNIT = "temperatureUnit";
	private final String PARAM_VOLUME = "volume";
	private final String PARAM_VOLUME_UNIT = "volumeUnit";
	private final String PARAM_AW = "aw";
	private final String PARAM_PH = "ph";
	private final String PARAM_PRESSURE = "pressure";
	private final String PARAM_PRESSURE_UNIT = "pressureUnit";
	
	private String temperature;
	private String temperatureUnit;
	
	private String volume;
	private String volumeUnit;
	
	private String aw;
	private String ph;
	
	private String pressure;
	private String pressureUnit;

	public ParametersSetting() {
		
	}
	public void saveSettings( final Config config ) {
		if (volume != null) {
			config.addString( PARAM_VOLUME, volume );
		}
		config.addString(PARAM_VOLUME_UNIT, volumeUnit);
		if (temperature != null) {
			config.addString( PARAM_TEMPERATURE, temperature );
		}
		config.addString(PARAM_TEMPERATURE_UNIT, temperatureUnit);
		if (ph != null) {
			config.addString( PARAM_PH, ph );
		}
		if (aw != null) {
			config.addString( PARAM_AW, aw );
		}
		if (pressure != null) {
			config.addString( PARAM_PRESSURE, pressure );
		}
		config.addString(PARAM_PRESSURE_UNIT, pressureUnit);
	}
	public void loadSettings( final Config config ) throws InvalidSettingsException {		
		volume = config.containsKey(PARAM_VOLUME) ? config.getString( PARAM_VOLUME ) : null;
		volumeUnit = config.getString(PARAM_VOLUME_UNIT);
		temperature = config.containsKey(PARAM_TEMPERATURE) ? config.getString( PARAM_TEMPERATURE ) : null;
		temperatureUnit = config.getString(PARAM_TEMPERATURE_UNIT);
		ph = config.containsKey(PARAM_PH) ? config.getString( PARAM_PH ) : null;
		aw = config.containsKey(PARAM_AW) ? config.getString( PARAM_AW ) : null;
		pressure = config.containsKey(PARAM_PRESSURE) ? config.getString( PARAM_PRESSURE ) : null;
		pressureUnit = config.getString(PARAM_PRESSURE_UNIT);
	}
	
	public void loadSettingsForDialog( final Config config ) {		
		try {
			loadSettings( config );
		}
		catch( InvalidSettingsException e ) {
			e.printStackTrace( System.err );
			assert false;
		}
		
	}
	
}
