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
public class AgentsSetting {
	
	private final String PARAM_AGENTS = "agentsDef";
	private final String PARAM_AGENTS_1 = "agentsDef1";
	private final String PARAM_AGENTS_2 = "agentsDef2";
	private final String PARAM_AGENTS_3 = "agentsDef3";
	private final String PARAM_AGENTS_4 = "agentsDef4";
	private final String PARAM_RECIPE_GUESS = "recipeGuess";
	private final String PARAM_MANUAL_DEF = "manualDef";
	
	private boolean recipeGuess;
	private boolean manualDef;
	
	private String[] agentsDef;
	private double[] agentsDef1;
	private double[] agentsDef2;
	private double[] agentsDef3;
	private double[] agentsDef4;
	
	private ParametersSetting parametersSetting;
	
	public AgentsSetting() {
		parametersSetting = new ParametersSetting();
	}
	
	public void saveSettings( final Config config ) {		
		if (agentsDef != null) {
			config.addStringArray(PARAM_AGENTS, agentsDef);
			config.addDoubleArray(PARAM_AGENTS_1, agentsDef1);
			config.addDoubleArray(PARAM_AGENTS_2, agentsDef2);
			config.addDoubleArray(PARAM_AGENTS_3, agentsDef3);
			config.addDoubleArray(PARAM_AGENTS_4, agentsDef4);
		}

		config.addBoolean( PARAM_RECIPE_GUESS, recipeGuess );
		config.addBoolean( PARAM_MANUAL_DEF, manualDef );
	}
	
	public void loadSettings( final Config config ) throws InvalidSettingsException {		
		recipeGuess = config.getBoolean( PARAM_RECIPE_GUESS );
		manualDef = config.getBoolean( PARAM_MANUAL_DEF );
		
		agentsDef = config.containsKey(PARAM_AGENTS) ? config.getStringArray(PARAM_AGENTS) : null;
		agentsDef1 = config.containsKey(PARAM_AGENTS_1) ? config.getDoubleArray(PARAM_AGENTS_1) : null;
		agentsDef2 = config.containsKey(PARAM_AGENTS_2) ? config.getDoubleArray(PARAM_AGENTS_2) : null;
		agentsDef3 = config.containsKey(PARAM_AGENTS_3) ? config.getDoubleArray(PARAM_AGENTS_3) : null;
		agentsDef4 = config.containsKey(PARAM_AGENTS_4) ? config.getDoubleArray(PARAM_AGENTS_4) : null;
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
