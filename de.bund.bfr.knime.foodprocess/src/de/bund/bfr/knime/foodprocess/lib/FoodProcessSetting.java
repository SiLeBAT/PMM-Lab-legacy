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
public class FoodProcessSetting {
	private final String PARAM_PROCESSNAME = "processName";
	private final String PARAM_CAPACITY = "capacity";
	private final String PARAM_DURATION = "duration";
	private final String PARAM_STEPWIDTH = "stepWidth";
	private final String PARAM_CAPACITY_UNIT_VOLUME = "capacityUnitVolume";
	private final String PARAM_CAPACITY_UNIT_TIME = "capacityUnitTime";
	private final String PARAM_DURATION_UNIT = "durationUnit";
	private final String PARAM_STEPWIDTH_UNIT = "stepWidthUnit";
	private final String PARAM_PARAMETERS = "parameters";
	private final String IN_PORT_SETTING = "in_port_setting";
	private final String PARAM_EXPERT_IN = "expertIn";
	private final String OUT_PORT_SETTING = "out_port_setting";
	private final String PARAM_EXPERT_OUT = "expertOut";
	private final String PARAM_AGENTS = "agents";
	
	private String processName;
	private Double capacity;
	private Double duration;
	private Double stepWidth;

	private String capacityUnitVolume;
	private String capacityUnitTime;
	private String durationUnit;
	private String stepWidthUnit;
	
	private ParametersSetting parametersSetting;
	
	private InPortSetting[] inPortSetting;
	private boolean expertIn;
	
	private OutPortSetting[] outPortSetting;
	private boolean expertOut;
	
	private AgentsSetting agentsSetting;

	private int n_outports;
	private int n_inports;

	public FoodProcessSetting( final int n_inports, final int n_outports ) {		
		int i;
		
		this.n_inports = n_inports;
		this.n_outports = n_outports;

		parametersSetting = new ParametersSetting();
		
		inPortSetting = new InPortSetting[ n_inports ];
		for( i = 0; i < n_inports; i++ ) {
			inPortSetting[ i ] = new InPortSetting();
		}
		
		outPortSetting = new OutPortSetting[ n_outports ];
		for( i = 0; i < n_outports; i++ ) {
			outPortSetting[ i ] = new OutPortSetting( n_inports );
		}
		outPortSetting[ 0 ].setOutFlux(100.0);
		
		agentsSetting = new AgentsSetting();
	}
		
	/** Saves current parameters to settings object.
     * @param config to save to
     */
	public void saveSettings( final Config config ) {		
		int i;
		Config c;
		
		config.addString(PARAM_PROCESSNAME, processName);
		if (capacity != null) {
			config.addDouble(PARAM_CAPACITY, capacity);
		}
		if (duration != null) {
			config.addDouble(PARAM_DURATION, duration);
		}
		if (stepWidth != null) {
			config.addDouble(PARAM_STEPWIDTH, stepWidth);
		}
		config.addString(PARAM_CAPACITY_UNIT_VOLUME, capacityUnitVolume);
		config.addString(PARAM_CAPACITY_UNIT_TIME, capacityUnitTime);
		config.addString(PARAM_DURATION_UNIT, durationUnit);
		config.addString(PARAM_STEPWIDTH_UNIT, stepWidthUnit);
		
		c = config.addConfig(PARAM_PARAMETERS);
		//assert c != null;		
		//assert parametersSetting != null;		
		parametersSetting.saveSettings( c );
		
		//assert inPortSetting != null;
		for( i = 0; i < inPortSetting.length; i++ ) {			
			c = config.addConfig( IN_PORT_SETTING + "_" + i );			
			//assert c != null;			
			//assert inPortSetting[ i ] != null;			
			inPortSetting[ i ].saveSettings( c );
		}
		config.addBoolean( PARAM_EXPERT_IN, expertIn );
		
		//assert outPortSetting != null;
		for( i = 0; i < outPortSetting.length; i++ ) {			
			c = config.addConfig( OUT_PORT_SETTING + "_" + i );			
			//assert c != null;			
			//assert outPortSetting[ i ] != null;			
			outPortSetting[ i ].saveSettings( c );
		}
		config.addBoolean( PARAM_EXPERT_OUT, expertOut );
		
		c = config.addConfig(PARAM_AGENTS);
		//assert c != null;		
		//assert agentsSetting != null;		
		agentsSetting.saveSettings( c );
	}

    /** Loads parameters in NodeModel.
     * @param config to load from
     * @throws InvalidSettingsException if settings are inconsistent
     */
	public void loadSettings( final Config config ) throws InvalidSettingsException {		
		int i;
		Config c;
		
		processName = config.getString(PARAM_PROCESSNAME);
		capacity = config.containsKey(PARAM_CAPACITY) ? config.getDouble(PARAM_CAPACITY) : null;
		duration = config.containsKey(PARAM_DURATION) ? config.getDouble(PARAM_DURATION) : null;
		stepWidth = config.containsKey(PARAM_STEPWIDTH) ? config.getDouble(PARAM_STEPWIDTH) : null;
		capacityUnitVolume = config.getString(PARAM_CAPACITY_UNIT_VOLUME);
		capacityUnitTime = config.getString(PARAM_CAPACITY_UNIT_TIME);
		durationUnit = config.getString(PARAM_DURATION_UNIT);
		stepWidthUnit = config.getString(PARAM_STEPWIDTH_UNIT);
		
		parametersSetting = new ParametersSetting();
		c = config.getConfig(PARAM_PARAMETERS);
		parametersSetting.loadSettings( c );

		inPortSetting = new InPortSetting[ n_inports ];
		for( i = 0; i < n_inports; i++ ) {			
			c = config.getConfig( IN_PORT_SETTING+"_"+i );
			inPortSetting[ i ] = new InPortSetting();
			inPortSetting[ i ].loadSettings( c );
		}
		expertIn = config.getBoolean( PARAM_EXPERT_IN );

		outPortSetting = new OutPortSetting[ n_outports ];
		for( i = 0; i < n_outports; i++ ) {			
			c = config.getConfig( OUT_PORT_SETTING+"_"+i );
			outPortSetting[ i ] = new OutPortSetting( n_inports );
			outPortSetting[ i ].loadSettings( c );
		}		
		expertOut = config.getBoolean( PARAM_EXPERT_OUT );
		
		agentsSetting = new AgentsSetting();
		c = config.getConfig(PARAM_AGENTS);
		agentsSetting.loadSettings( c );
	}

    /** Loads parameters in Dialog.
     * @param config to load from
     */
	public void loadSettingsForDialog(final Config config) {		
		try {
			loadSettings( config );
		}
		catch( InvalidSettingsException e ) {
			e.printStackTrace( System.err );
			//assert false;
		}
	}
}
