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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

import lombok.Getter;

import org.knime.core.node.InvalidSettingsException;

public class FoodProcessDef {
	
	public static final String[] COMBO_VOLUMEUNIT = { "l", "ml", "g","kg" };
	public static final String[] COMBO_TEMPERATUREUNIT = { "°C", "°F", "K" };
	public static final String[] COMBO_PRESSUREUNIT = { "bar", "Pa" };
	public static final String[] COMBO_TIMEUNIT = { " ", "s", "min", "h", "d" };

	private final String LABEL_EXPERT = "Expert";

	@Getter private JComboBox nameBox;
	@Getter private JFormattedTextField capacityField;
	@Getter private JComboBox capacityNomBox;
	@Getter private JComboBox capacityDenomBox;
	@Getter private JFormattedTextField durationField;
	@Getter private JComboBox durationBox;
	@Getter private JFormattedTextField stepwidthField;
	@Getter private JComboBox stepwidthBox;
	
	@Getter private ParametersDef parametersDef;

	@Getter private InPortDef[] inPortDef;
	@Getter private JCheckBox expertIn;
	
	@Getter private OutPortDef[] outPortDef;
	@Getter private JCheckBox expertOut;

	@Getter private AgentsDef agentsDef;

	private int n_outports;
	private int n_inports;

	public FoodProcessDef( final int n_inports, final int n_outports ) {		
		int i;
		NumberFormat nf;
		
		nf = NumberFormat.getNumberInstance( java.util.Locale.US );
		
		this.n_inports = n_inports;
		this.n_outports = n_outports;
		
		nameBox = new JComboBox();
		nameBox.setEditable(true);
		capacityField = new JFormattedTextField( nf );
		capacityField.setColumns(5);
		capacityField.setValue(null);
		capacityNomBox = new JComboBox( COMBO_VOLUMEUNIT );
		capacityDenomBox = new JComboBox( COMBO_TIMEUNIT );
		durationField = new JFormattedTextField( nf );
		durationField.setColumns(5);
		durationBox = new JComboBox( COMBO_TIMEUNIT );
		stepwidthField = new JFormattedTextField( nf );
		stepwidthField.setColumns(5);
		stepwidthBox = new JComboBox( COMBO_TIMEUNIT );

		parametersDef = new ParametersDef();
		
		inPortDef = new InPortDef[ n_inports ];
		for( i = 0; i < n_inports; i++ ) {
			inPortDef[ i ] = new InPortDef(inPortDef);
		}
		expertIn = new JCheckBox( LABEL_EXPERT );
		expertIn.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				expertInActionPerformed(e);
			}
		});
		
		outPortDef = new OutPortDef[ n_outports ];
		for( i = 0; i < n_outports; i++ ) {
			outPortDef[ i ] = new OutPortDef( n_inports, n_outports, outPortDef );
		}
		expertOut = new JCheckBox( LABEL_EXPERT );
		expertOut.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent e) {
				expertOutActionPerformed(e);
			}
		});
		
		agentsDef = new AgentsDef();
		
		expertInActionPerformed(null);
		expertOutActionPerformed(null);
	}
	
	public FoodProcessSetting getSetting() throws InvalidSettingsException {		
		FoodProcessSetting fps;
		int i;
		Double val;
		
		fps = new FoodProcessSetting( n_inports, n_outports );
		
		//fps.setProcessName((String) nameBox.getSelectedItem());
		fps.setProcessName((String) nameBox.getEditor().getItem());
		val = (capacityField.getValue() == null) ? null : ((Number)capacityField.getValue()).doubleValue();
		fps.setCapacity(val);
		fps.setCapacityUnitVolume((String) capacityNomBox.getSelectedItem());
		fps.setCapacityUnitTime((String) capacityDenomBox.getSelectedItem());
		val = (durationField.getValue() == null) ? null : ((Number)durationField.getValue()).doubleValue();
		fps.setDuration(val);
		fps.setDurationUnit((String) durationBox.getSelectedItem());
		val = (stepwidthField.getValue() == null) ? null : ((Number)stepwidthField.getValue()).doubleValue();
		fps.setStepWidth(val);
		fps.setStepWidthUnit((String) stepwidthBox.getSelectedItem());

		fps.setParametersSetting(parametersDef.getSetting());
		
		InPortSetting[] inPortSetting = new InPortSetting[n_inports]; 
		for( i = 0; i < n_inports; i++ ) {
			inPortSetting[i] = inPortDef[ i ].getSetting();
		}
		fps.setInPortSetting(inPortSetting);
		fps.setExpertIn( expertOut.isSelected() );
		
		OutPortSetting[] outPortSetting = new OutPortSetting[n_outports]; 
		for( i = 0; i < n_outports; i++ ) {
			outPortSetting[i] = outPortDef[ i ].getSetting();
		}
		fps.setOutPortSetting(outPortSetting);
		fps.setExpertOut( expertOut.isSelected() );
		
		fps.setAgentsSetting(agentsDef.getSetting());
		
		return fps;
	}
	
	public void setSetting( final FoodProcessSetting fps ) {		
		int i;
		Double val;
		
		nameBox.setSelectedItem(fps.getProcessName());
		
		val = fps.getCapacity();
		capacityField.setValue( val );
		capacityNomBox.setSelectedItem(fps.getCapacityUnitVolume());
		capacityDenomBox.setSelectedItem(fps.getCapacityUnitTime());
		
		val = fps.getDuration();
		durationField.setValue( val );
		durationBox.setSelectedItem(fps.getDurationUnit());
		
		val = fps.getStepWidth();
		stepwidthField.setValue( val );
		stepwidthBox.setSelectedItem(fps.getStepWidthUnit());
				
		parametersDef.setSetting(fps.getParametersSetting());
		
		for( i = 0; i < n_inports; i++ ) {
			if( inPortDef[ i ] == null ) {
				inPortDef[ i ] = new InPortDef(inPortDef);
			}
			inPortDef[ i ].setSetting( fps.getInPortSetting()[i] );
		}
		expertIn.setSelected( fps.isExpertIn() );

		for( i = 0; i < n_outports; i++ ) {
			if( outPortDef[ i ] == null ) {
				outPortDef[ i ] = new OutPortDef( n_inports, n_outports, outPortDef );
			}
			outPortDef[ i ].setSetting( fps.getOutPortSetting()[i] );
		}
		expertOut.setSelected( fps.isExpertOut() );
		
		agentsDef.setSetting(fps.getAgentsSetting());
	}	
	private void expertInActionPerformed(final ItemEvent e) {
		for(int i = 0; i < n_inports; i++ ) {
			inPortDef[ i ].getParametersDef().setEnabledBase(expertIn.isSelected());
			inPortDef[ i ].getParametersDef().setEnabledUnits(expertIn.isSelected());
		}
	}	
	private void expertOutActionPerformed(final ItemEvent e) {
		for(int i = 0; i < n_outports; i++ ) {
			outPortDef[ i ].getParametersDef().setEnabledBase(expertOut.isSelected());
			outPortDef[ i ].getParametersDef().setEnabledUnits(expertOut.isSelected());
		}
	}	
}
