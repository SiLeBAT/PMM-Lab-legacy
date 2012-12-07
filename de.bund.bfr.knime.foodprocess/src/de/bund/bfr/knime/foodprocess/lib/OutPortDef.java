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

import java.awt.event.FocusEvent;
import java.text.NumberFormat;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

import lombok.Data;
import lombok.Getter;

@Data
public class OutPortDef {
		
	private int n_inports, n_outports;

	@Getter private JFormattedTextField outFlux;
	@Getter private JComboBox<String> newMatrixDefinition;
	@Getter private JFormattedTextField[] fromInPort;
	@Getter private ParametersDef parametersDef;

	private OutPortDef[] outPortDef;
	
	public OutPortDef( final int n_inports, final int n_outports, final OutPortDef[] outPortDef ) {
		
		int i;
		this.n_inports = n_inports;
		this.n_outports = n_outports;
		this.outPortDef = outPortDef;

		NumberFormat nf;
		nf = NumberFormat.getNumberInstance( java.util.Locale.US );
		outFlux = new JFormattedTextField( nf ) {
		    /**
			 * 
			 */
			private static final long serialVersionUID = -5745718848688964119L;

			@Override  
		    protected void processFocusEvent(final FocusEvent e) {  
		        if (e.getID() == FocusEvent.FOCUS_LOST) {  
		            if (getText() == null || getText().isEmpty()) {  
		                setValue(null);  
		            }  
		        }  
		        super.processFocusEvent(e);  
		    }  
		};
		outFlux.setColumns(5);
		
		newMatrixDefinition = new JComboBox<String>( fetchMatrixSuggestion() );
		newMatrixDefinition.setSelectedIndex( 0 );
		newMatrixDefinition.setEditable( true );
		
		fromInPort = new JFormattedTextField[ n_inports ];
		for( i = 0; i < n_inports; i++ ) {
			
			fromInPort[ i ] = new JFormattedTextField( nf );
			fromInPort[ i ].setColumns(10);
		}
				
		parametersDef = new ParametersDef();

	}
	
	public OutPortSetting getSetting() {
		
		OutPortSetting ops;
		int i;
		Double val;
		
		ops = new OutPortSetting( n_inports );
		
		//ops.setMatrix( ( String )newMatrixDefinition.getSelectedItem() );
		ops.setMatrix((String) newMatrixDefinition.getEditor().getItem());
		
		val = outFlux.getValue() == null ? null : ((Number)outFlux.getValue()).doubleValue();
		ops.setOutFlux( val );
		
		Double[] fromInVal = ops.getFromInPort();
		for( i = 0; i < n_inports; i++ ) {
			
			val = fromInPort[ i ].getValue() == null ? null : ((Number)fromInPort[ i ].getValue()).doubleValue();
			
			fromInVal[i] = val;
		}
		ops.setFromInPort(fromInVal);
				
		ops.setParametersSetting(parametersDef.getSetting());
		
		return ops;
	}
	
	public void setSetting( final OutPortSetting ops ) {		
		int i;
		Double val;
		
		val = ops.getOutFlux();
		outFlux.setValue( val );
		
		newMatrixDefinition.setSelectedItem( ops.getMatrix() );
		
		for( i = 0; i < n_inports; i++ ) {
			val = ops.getFromInPort()[i];
			fromInPort[ i ].setValue( val );
		}
		
		parametersDef.setSetting(ops.getParametersSetting());
	}
	
	// TODO: Fetch suggestions from database
	private String[] fetchMatrixSuggestion() {
		return new String[] { "Schwein", "Rind", "Lamm", "Pute", "Ente", "Gans", "Hühnchen" };
	}
}
