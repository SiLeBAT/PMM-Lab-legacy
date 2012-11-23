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

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.foodprocess.lib.FoodProcessSetting;
import de.bund.bfr.knime.foodprocess.ui.FoodProcessUi;

/**
 * <code>NodeDialog</code> for the "FoodProcess" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Jorgen Brandt
 */
public class FoodProcessNodeDialog extends NodeDialogPane {
	private final FoodProcessNodeSettings settings;
	private final FoodProcessUi fpui;

    /**
     * New pane for configuring the FoodProcess node.
     */
    protected FoodProcessNodeDialog() {
    	this.settings = new FoodProcessNodeSettings();
		//addTab( "FoodProcess settings", new JnFoodProcessUi() );
    	fpui = new FoodProcessUi();
		addTab( "FoodProcess settings", fpui );		
    }
    
    @Override
	public void saveSettingsTo( final NodeSettingsWO s ) {    	
    	try {
			settings.setFoodProcessSetting(fpui.getSettings());
	    	//assert settings != null;
	    	
	    	settings.saveSettings(s);
		} catch (InvalidSettingsException e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getMessage(), e);
		}    	
    }
    
    @Override
    protected void loadSettingsFrom(final NodeSettingsRO s,
    		final PortObjectSpec[] specs) throws NotConfigurableException {
	
    	FoodProcessSetting fps;
    	
    	//assert settings != null;    	
    	settings.loadSettingsForDialog(s);
    	
    	fps = settings.getFoodProcessSetting();
    	fpui.setSettings(fps);
    }
}

