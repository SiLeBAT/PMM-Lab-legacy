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
package de.bund.bfr.knime.pmm.common.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.knime.core.node.InvalidSettingsException;

import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;

public class EstModelReaderUi extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 20120828;
	private JRadioButton qualityButtonNone;
	private JRadioButton qualityButtonRms;
	private JRadioButton qualityButtonR2;
	private DoubleTextField qualityField;
	private MdReaderUi tsReaderUi;
	private ModelReaderUi modelReaderUi;
	
	public static final int MODE_OFF = 0;
	public static final int MODE_R2 = 1;
	public static final int MODE_RMS = 2;
	
	public EstModelReaderUi() {
		this(null);
	}
	
	public EstModelReaderUi(String[] itemListMisc) {								
		modelReaderUi = new ModelReaderUi();
		modelReaderUi.addLevelListener( this );
		qualityButtonNone = new JRadioButton( "Do not filter" );
		qualityButtonNone.setSelected( true );
		qualityButtonNone.addActionListener( this );
		qualityButtonRms = new JRadioButton( "Filter by RMS" );
		qualityButtonRms.addActionListener( this );
		qualityButtonR2 = new JRadioButton( "Filter by R squared" );
		qualityButtonR2.addActionListener( this );
		qualityField = new DoubleTextField( false );
		qualityField.setText( "0.8" );
		qualityField.setEnabled( false );
		tsReaderUi = new MdReaderUi(itemListMisc);
						
		JPanel buttonPanel = new JPanel();
		ButtonGroup group = new ButtonGroup();	
		
		buttonPanel.setLayout( new GridLayout( 0, 1 ) );
		buttonPanel.add( qualityButtonNone );
		buttonPanel.add( qualityButtonRms );		
		buttonPanel.add( qualityButtonR2 );
		group.add( qualityButtonNone );		
		group.add( qualityButtonRms );
		group.add( qualityButtonR2 );		
		
		JPanel panel = new JPanel();
		
		panel.setBorder( BorderFactory.createTitledBorder( "Estimation quality" ) );
		panel.setLayout( new BorderLayout() );
		panel.setPreferredSize( new Dimension( 300, 125 ) );
		panel.add( buttonPanel, BorderLayout.NORTH );
		panel.add( new JLabel( "Quality threshold   " ), BorderLayout.WEST );
		panel.add( qualityField, BorderLayout.CENTER );
		
		JPanel southPanel = new JPanel();
		
		southPanel.setLayout(new BorderLayout());
		southPanel.add(panel, BorderLayout.CENTER);
		southPanel.add(tsReaderUi, BorderLayout.SOUTH);
		
		setPreferredSize( new Dimension( 300, 500 ) );
		setLayout(new BorderLayout());
		add( modelReaderUi, BorderLayout.CENTER );
		add( southPanel, BorderLayout.SOUTH );		
		
		updateTsReaderUi();
	}
	
	@Override
	public void actionPerformed( ActionEvent arg0 ) {

		if( arg0.getSource() instanceof JRadioButton ) {
			
			if( qualityButtonNone.isSelected() )
				qualityField.setEnabled( false );
			else
				qualityField.setEnabled( true );
			
			return;
		}
		
		if( arg0.getSource() instanceof JComboBox ) {
			
			updateTsReaderUi();
			
			return;
		}
		
	}
	
	public void addModelPrim( final int id, final String name ) throws PmmException {
		modelReaderUi.addModelPrim( id, name );
	}
	
	public void addModelSec( final int id, final String name ) throws PmmException {
		modelReaderUi.addModelSec( id, name );
	}
	
	public void setMatrixString( final String str ) throws InvalidSettingsException {
		tsReaderUi.setMatrixString(str);
	}
	public void setAgentString( final String str ) throws InvalidSettingsException {
		tsReaderUi.setAgentString(str);
	}
	public void setLiteratureString( final String str ) throws InvalidSettingsException {
		tsReaderUi.setLiteratureString(str);
	}
	public void setParameter(LinkedHashMap<String, DoubleTextField[]> params) {
		tsReaderUi.setParameter(params);
	}
	public void clearModelSet() { modelReaderUi.clearModelSet(); }
	public void enableModelList( String idList ) { modelReaderUi.enableModelList( idList ); }
	public String getAgentString() { return tsReaderUi.getAgentString(); }
	public String getLiteratureString() { return tsReaderUi.getLiteratureString(); }
	public LinkedHashMap<String, DoubleTextField[]> getParameter() { return tsReaderUi.getParameter(); }
	public int getLevel() { return modelReaderUi.getLevel(); }
	public String getMatrixString() { return tsReaderUi.getMatrixString(); }
	public String getModelList() { return modelReaderUi.getModelList(); }
	
	public void setMiscItems(String[] itemListMisc) {
		tsReaderUi.setMiscItems(itemListMisc);
	}
	public double getQualityThresh() throws InvalidSettingsException {
		
		if( !qualityField.isValueValid() )
			throw new InvalidSettingsException( "Threshold quality invalid." );
		
		return qualityField.getValue();
	}
	
	public int getQualityMode() {
		
		if( qualityButtonNone.isSelected() )
			return MODE_OFF;
		
		if( qualityButtonRms.isSelected() )
			return MODE_RMS;
		
		return MODE_R2;
	}
	
	public boolean isModelFilterEnabled() { return modelReaderUi.isModelFilterEnabled(); }
	
	public void setLevel( int level ) throws PmmException { modelReaderUi.setLevel( level ); }
	public void setModelFilterEnabled( boolean en ) { modelReaderUi.setModelFilterEnabled( en ); }
	
	public void setQualityMode( final int mode ) throws PmmException {
		
		
		switch( mode ) {
		
			case MODE_OFF :
				qualityButtonNone.setSelected( true );
				qualityField.setEnabled( false );
				break;
				
			case MODE_R2 :
				qualityButtonR2.setSelected( true );
				qualityField.setEnabled( true );
				break;
				
			case MODE_RMS :
				qualityButtonRms.setSelected( true );
				qualityField.setEnabled( true );
				break;
		
			default :
				throw new PmmException( "Invalid quality filter mode." );
		}
	}
	
	public void setQualityThresh( final double thresh ) {
		qualityField.setText( String.valueOf( thresh ) );
	}
	
    public static boolean passesFilter(
    		final int level,
    		final int qualityMode,
    		final double qualityThresh,
    		final String matrixString,
    		final String agentString,
    		final String literatureString,
    		final LinkedHashMap<String, Double[]> parameter,
    		final boolean modelFilterEnabled,
    		final String modelList,
    		final KnimeTuple tuple )
    throws PmmException {

    	if( level == 1 )
    		if( !MdReaderUi.passesFilter( matrixString,
				agentString, literatureString, parameter, tuple ) )
    			return false;
    	
    	if( !ModelReaderUi.passesFilter( modelFilterEnabled,
			modelList, tuple ) )
    		return false;
    		
    		
        	
		PmmXmlDoc x = tuple.getPmmXml(Model1Schema.getAttribute(Model1Schema.ATT_ESTMODEL, level));
		EstModelXml emx = null;
		if (x != null) {
			for (PmmXmlElementConvertable el : x.getElementSet()) {
				if (el instanceof EstModelXml) {
					emx = (EstModelXml) el;
					break;
				}
			}
		}

		switch( qualityMode ) {
    	
    		case MODE_OFF :
    			return true;
    			
    		case MODE_RMS :    			
    			if (emx != null && emx.getRMS() <= qualityThresh) return true;    			
    			else return false;
    			
    		case MODE_R2 :    			
    			if (emx != null && emx.getR2() != null && emx.getR2() >= qualityThresh) return true;    			
    			else return false;
    			
    		default :
    			throw new PmmException( "Unrecognized Quality Filter mode." );
    	}
    }
    
    private void updateTsReaderUi() {
    	/*
    	if( modelReaderUi.getLevel() == 1 )
    		tsReaderUi.setActive();
    	else
    		tsReaderUi.setInactive();
    		*/
    }
}
