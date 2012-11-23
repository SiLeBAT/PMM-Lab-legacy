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
package de.bund.bfr.knime.foodprocess;

import lombok.Data;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.foodprocess.lib.FoodProcessSetting;

/**
 * The settings object for the FoodProcess node.
 */
@Data
public class FoodProcessNodeSettings {
	public static final String VERSION_1_X = "version 1.x";
	
	private static final String VERSION = "version";
	private static final String FOOD_PROCESS_SETTING = "food_process_setting";
	
	private String version;
	private FoodProcessSetting foodProcessSetting;

	public FoodProcessNodeSettings() {
		version = VERSION_1_X;
		foodProcessSetting = new FoodProcessSetting(FoodProcessNodeModel.N_PORT_IN,FoodProcessNodeModel.N_PORT_OUT);
	}

	/** Saves current parameters to settings object.
     * @param settings to save to
     */
    public void saveSettings(final NodeSettingsWO settings) {     	
        settings.addString(VERSION, version);
        foodProcessSetting.saveSettings(settings.addConfig(FOOD_PROCESS_SETTING));
    }

    /** Loads parameters in NodeModel.
     * @param settings to load from
     * @throws InvalidSettingsException if settings are inconsistent
     */
    public void loadSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {    	
        version = settings.getString(VERSION);
        foodProcessSetting.loadSettings(settings.getConfig(FOOD_PROCESS_SETTING));
    }


    /** Loads parameters in Dialog.
     * @param settings to load from
     */
    public void loadSettingsForDialog(final NodeSettingsRO settings) {    	
        try {
            version = settings.getString(VERSION, VERSION_1_X);
            foodProcessSetting.loadSettingsForDialog(settings.getConfig(FOOD_PROCESS_SETTING));
        } catch (InvalidSettingsException e) {
        	// Rethrow using a runtime exception
            throw new IllegalStateException(e);
        }
    }

}
