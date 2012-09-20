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
package de.bund.bfr.knime.pmm.estimatedmodelreader.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.knime.core.node.InvalidSettingsException;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class TsReaderUi extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 20120913;
	private JTextArea matrixArea;
	private JTextArea agentArea;
	private JCheckBox matrixSwitch;
	private JCheckBox agentSwitch;

	
	public TsReaderUi() {
		
		JPanel panel;

		panel = new JPanel();
		panel.setBorder( BorderFactory.createTitledBorder( "Agent" ) );
		panel.setLayout( new BorderLayout() );
		panel.setPreferredSize( new Dimension( 250, 75 ) );
		add( panel );
		
		agentSwitch = new JCheckBox( "Filter by agent" );
		agentSwitch.addActionListener( this );
		panel.add( agentSwitch, BorderLayout.NORTH );
		panel.add( new JLabel( "Agent name   " ), BorderLayout.WEST );
		agentArea = new JTextArea();
		agentArea.setEnabled( false );
		panel.add( agentArea, BorderLayout.CENTER );

		
		panel = new JPanel();
		panel.setBorder( BorderFactory.createTitledBorder( "Matrix" ) );
		panel.setLayout( new BorderLayout() );
		panel.setPreferredSize( new Dimension( 250, 75 ) );
		add( panel );
		
		matrixSwitch = new JCheckBox( "Filter by matrix" );
		matrixSwitch.addActionListener( this );
		panel.add( matrixSwitch, BorderLayout.NORTH );
		panel.add( new JLabel( "Matrix name   " ), BorderLayout.WEST );
		
		matrixArea = new JTextArea();
		matrixArea.setEnabled( false );
		panel.add( matrixArea, BorderLayout.CENTER );

	}


	@Override
	public void actionPerformed(ActionEvent arg0) {

		if( arg0.getSource() == agentSwitch ) {
			
			if( agentSwitch.isSelected() )
				agentArea.setEnabled( true );
			else
				agentArea.setEnabled( false );
			
			return;
		}
		
		if( arg0.getSource() == matrixSwitch ) {
			
			if( matrixSwitch.isSelected() )
				matrixArea.setEnabled( true );
			else
				matrixArea.setEnabled( false );
			
			return;
		}
		
	}
	
	public String getMatrixString() { return matrixArea.getText(); }
	public String getAgentString() { return agentArea.getText(); }
	public boolean isMatrixFilterEnabled() { return matrixSwitch.isSelected(); }
	public boolean isAgentFilterEnabled() { return agentSwitch.isSelected(); }
	
	public void setMatrixEnabled( final boolean en ) {
		matrixSwitch.setSelected( en );
		matrixArea.setEnabled( en );
	}
	
	public void setAgentEnabled( final boolean en ) {
		agentSwitch.setSelected( en );
			agentArea.setEnabled( en );
	}
	
	public void setMatrixString( final String str ) throws InvalidSettingsException {
		
		if( str == null )
			throw new InvalidSettingsException( "Matrix Filter string must not be null." );
		
		matrixArea.setText( str );
	}
	
	public void setAgentString( final String str ) throws InvalidSettingsException {
		
		if( str == null )
			throw new InvalidSettingsException( "Matrix Filter string must not be null." );
		
		agentArea.setText( str );
	}
	
	public static boolean passesFilter(
		final boolean matrixEnabled,
		final String matrixString,
		final boolean agentEnabled,
		final String agentString,
		final KnimeTuple tuple ) throws PmmException {
		
		String s;
		String t;
		
		s = tuple.getString( TimeSeriesSchema.ATT_MATRIXNAME );
		if( s == null )
			s = tuple.getString( TimeSeriesSchema.ATT_MATRIXDETAIL );
		if( s == null )
			s = "";
		
		s = s.toLowerCase();
		t = matrixString.toLowerCase();
		
		if( matrixEnabled )
			if( !s.contains( t ) )
				return false;
		
		s = tuple.getString( TimeSeriesSchema.ATT_AGENTNAME );
		if( s == null )
			s = tuple.getString( TimeSeriesSchema.ATT_AGENTDETAIL );
		if( s == null )
			s = "";
		
		s = s.toLowerCase();
		t = agentString.toLowerCase();
		
		if( agentEnabled )
			if( !s.contains( t ) )
				return false;
		
		return true;
	}
	

}
