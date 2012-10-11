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
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;

public class ModelReaderUi extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 20120828;
	
	protected static final String LABEL_UNSPEC = "Unspecified only";
	private static final String LABEL_PRIM = "Primary";
	private static final String LABEL_SEC = "Secondary";
	private static final int LEVEL_PRIM = 1;
	private static final int LEVEL_SEC = 2;
		
	private JCheckBox modelNameSwitch;
	private JComboBox levelBox;
	private JPanel modelPanel;
	private JPanel panel;
	
	private HashMap<String, Integer> modelIdPrim;
	private HashMap<String, Integer> modelIdSec;
	private LinkedList<JCheckBox> modelBoxSetPrim;
	private LinkedList<JCheckBox> modelBoxSetSec;
	
	
	public ModelReaderUi() {
		
		JPanel panel0;
		
		clearModelSet();
		
		setLayout( new BorderLayout() );
		setPreferredSize( new Dimension( 300, 180 ) );
		
		panel = new JPanel();
		panel.setBorder( BorderFactory.createTitledBorder( "Level" ) );
		panel.setLayout( new BorderLayout() );
		panel.setPreferredSize( new Dimension( 250, 50 ) );
		add( panel, BorderLayout.NORTH );
		
		panel0 = new JPanel();
		panel0.setLayout( new BoxLayout( panel0, BoxLayout.X_AXIS ) );
		panel.add( panel0 );

		panel0.add( new JLabel( "Level   " ) );
		
		levelBox = new JComboBox( new String[] { LABEL_PRIM, LABEL_SEC } );
		levelBox.addActionListener( this );
		levelBox.setPreferredSize( new Dimension( 50, 32 ) );
		panel0.add( levelBox );
		
		panel = new JPanel();
		panel.setBorder( BorderFactory.createTitledBorder( "Model" ) );
		panel.setLayout( new BorderLayout() );
		panel.setPreferredSize( new Dimension( 250, 150 ) );
		add( panel, BorderLayout.CENTER );
				
		modelNameSwitch = new JCheckBox( "Filter by model name" );
		modelNameSwitch.addActionListener( this );
		panel.add( modelNameSwitch, BorderLayout.NORTH );		
		
		/* modelNameBox = new JComboBox();
		modelNameBox.setEnabled( false );
		panel.add( modelNameBox, BorderLayout.CENTER ); */
		
		modelPanel = new JPanel();
		modelPanel.setLayout( new GridLayout( 0, 1 ) );
		// modelPanel.setPreferredSize( new Dimension( 150, 200 ) );
		JScrollPane pane;
		
		pane = new JScrollPane( modelPanel );
		// pane.setPreferredSize( new Dimension( 200, 10 ) );
		panel.add( pane );
	}
	
	public void clearModelSet() {
		modelIdPrim = new HashMap<String, Integer>();
		modelIdSec = new HashMap<String, Integer>();
		modelBoxSetPrim = new LinkedList<JCheckBox>();
		modelBoxSetSec = new LinkedList<JCheckBox>();
	}

	@Override
	public void actionPerformed( ActionEvent arg0 ) {

		if( arg0.getSource() == modelNameSwitch ) {
			
			updateModelNameEnabled();
			return;
		}
		
		if( arg0.getSource() == levelBox ) {
			
			updateModelName();
			return;
		}
	}
	
	public void addModelPrim( final int id, final String name )
	throws PmmException {
		
		if( name == null )
			throw new PmmException( "Model name must not be null." );
		
		modelIdPrim.put( name, id );
		modelBoxSetPrim.add( new JCheckBox( name ) );
		updateModelName();
	}
	
	public void addModelSec( final int id, final String name )
	throws PmmException {
		
		if( name == null )
			throw new PmmException( "Model name must not be null." );
		
		modelIdSec.put( name, id );
		modelBoxSetSec.add( new JCheckBox( name ) );
		updateModelName();
	}
	
	public int getLevel() {
		return levelBox.getSelectedIndex()+1;
	}
	
	public boolean isPrim() {
		return getLevel() == LEVEL_PRIM;
	}
	
	public boolean isSec() {
		return getLevel() == LEVEL_SEC;
	}
	
	public boolean isModelFilterEnabled() {
		return modelNameSwitch.isSelected();
	}
	
	public boolean modelNameEnabled( final String name ) {
		
		for( JCheckBox box : modelBoxSetPrim )
			if( box.getText().equals( name ) )
				return true;
		
		for( JCheckBox box : modelBoxSetSec )
			if( box.getText().equals( name ) )
				return true;
		
		return false;
	}
	
	public void setLevel( int level ) throws PmmException {
		
		if( !( level == 1 || level == 2 ) )
			throw new PmmException( "Level must be in {1, 2}" );
		
		levelBox.setSelectedIndex( level-1 );
	}
	
	private void updateModelName() {
		
		modelPanel.removeAll();
		
		if( isPrim() )
			for( JCheckBox box : modelBoxSetPrim )
				modelPanel.add( box );
		else
			for( JCheckBox box : modelBoxSetSec )
				modelPanel.add( box );

		updateModelNameEnabled();
		
		panel.validate();
	}
	
	private void updateModelNameEnabled() {
		if( modelNameSwitch.isSelected() ) {
			for( JCheckBox box : modelBoxSetPrim )
				box.setEnabled( true );
			for( JCheckBox box : modelBoxSetSec )
				box.setEnabled( true );
		}
		else {
			for( JCheckBox box : modelBoxSetPrim )
				box.setEnabled( false );
			for( JCheckBox box : modelBoxSetSec )
				box.setEnabled( false );
		}

	}
	
	public boolean complies( KnimeTuple tuple ) throws PmmException {
		
		if( isModelFilterEnabled() )
			if( modelNameEnabled( tuple.getString( Model1Schema.ATT_MODELNAME ) ) )
				return true;
		
		return false;
	}
	
	public String getModelList() {
		
		String ret;
		
		ret = "";
		
		for( JCheckBox box : modelBoxSetPrim )
			if( box.isSelected() ) {
				if( !ret.isEmpty() )
					ret += ",";
				ret += modelIdPrim.get( box.getText() );
			}

		for( JCheckBox box : modelBoxSetSec )
			if( box.isSelected() ) {
				if( !ret.isEmpty() )
					ret += ",";
				ret += modelIdSec.get( box.getText() );
			}
		
		return ret;
	}
	
	public void enableModelList( final String idlist ) {
		
		String[] token;
		
		if( idlist.isEmpty() )
			return;
		
		token = idlist.split( "," );
		
		// disable everything
		for( JCheckBox box : modelBoxSetPrim )
			box.setSelected( false );
		
		for( JCheckBox box : modelBoxSetSec )
			box.setSelected( false );
		
		// enable model if appropriate
		for( JCheckBox box : modelBoxSetPrim )
			for( String id : token )
				if( Integer.valueOf( id ) == modelIdPrim.get( box.getText() ) ) {
					box.setSelected( true );
					break;
				}
		
		for( JCheckBox box : modelBoxSetSec )
			for( String id : token )
				if( Integer.valueOf( id ) == modelIdSec.get( box.getText() ) ) {
					box.setSelected( true );
					break;
				}
	}
	
	@Override
	public String toString() {
		return getModelList();
	}
	
	public void setModelFilterEnabled( final boolean en ) {
		
		if( en != isModelFilterEnabled() )
			modelNameSwitch.doClick();
	}
	
    public static boolean passesFilter(
    		final boolean modelFilterEnabled,
    		final String modelList,
    		final KnimeTuple tuple )
    throws PmmException {
        	
    	int id;
    	String[] token;
    	
    	if( !modelFilterEnabled )
    		return true;
    	
    	if( modelList.isEmpty() )
    		return false;
    	
    	if( tuple.getSchema().conforms( new Model1Schema() ) )
    		id = tuple.getInt( Model1Schema.ATT_MODELID );
		else
			id = tuple.getInt( Model2Schema.ATT_MODELID );
    	
    	token = modelList.split( "," );
    	for( String candidate : token )
    		if( Integer.valueOf( candidate ) == id )
    			return true;
    
    	return false;
    }
    
    
    
    public void addLevelListener( ActionListener listener ) {    	
    	levelBox.addActionListener( listener );
    }

}
