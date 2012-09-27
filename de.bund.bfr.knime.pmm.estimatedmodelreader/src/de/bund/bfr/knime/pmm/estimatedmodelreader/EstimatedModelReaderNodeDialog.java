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
package de.bund.bfr.knime.pmm.estimatedmodelreader;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.DbConfigurationUi;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.estimatedmodelreader.ui.EstModelReaderUi;
import de.bund.bfr.knime.pmm.modelcatalogreader.ModelCatalogReaderNodeModel;
import de.bund.bfr.knime.pmm.timeseriesreader.TimeSeriesReaderNodeModel;
import de.dim.knime.bfr.internal.BfRNodePluginActivator;

/**
 * <code>NodeDialog</code> for the "EstimatedModelReader" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Jorgen Brandt
 */
public class EstimatedModelReaderNodeDialog extends NodeDialogPane implements ActionListener {

	// private JComboBox levelBox;
	private DbConfigurationUi dbui;
	private EstModelReaderUi estmodelui;

    /**
     * New pane for configuring the EstimatedModelReader node.
     */
    protected EstimatedModelReaderNodeDialog() {
    	
    	JPanel panel, panel0;
    	
    	
    	panel = new JPanel();
    	panel.setLayout( new BorderLayout() );
    	
    	dbui = new DbConfigurationUi( true );
    	dbui.getApplyButton().addActionListener( this );
    	panel.add( dbui, BorderLayout.NORTH );
    	
    	panel0 = new JPanel();
    	panel0.setLayout( new BoxLayout( panel0, BoxLayout.Y_AXIS ) );
    	panel.add( panel0, BorderLayout.SOUTH );
    	
    	estmodelui = new EstModelReaderUi();
    	panel0.add( estmodelui );
    	    	
    	addTab( "Database connection", panel );
    	


    }
	@Override
	protected void saveSettingsTo( NodeSettingsWO settings )
			throws InvalidSettingsException {
		
		settings.addString( DbConfigurationUi.PARAM_FILENAME, dbui.getFilename() );
		settings.addString( DbConfigurationUi.PARAM_LOGIN, dbui.getLogin() );
		settings.addString( DbConfigurationUi.PARAM_PASSWD, dbui.getPasswd() );
		settings.addBoolean( DbConfigurationUi.PARAM_OVERRIDE, dbui.isOverride() );
		settings.addInt( ModelCatalogReaderNodeModel.PARAM_LEVEL, estmodelui.getLevel() );
		settings.addBoolean( ModelCatalogReaderNodeModel.PARAM_MODELFILTERENABLED, estmodelui.isModelFilterEnabled() );
		settings.addString( ModelCatalogReaderNodeModel.PARAM_MODELLIST, estmodelui.getModelList() );
		settings.addInt( EstimatedModelReaderNodeModel.PARAM_QUALITYMODE, estmodelui.getQualityMode() );
		settings.addDouble( EstimatedModelReaderNodeModel.PARAM_QUALITYTHRESH, estmodelui.getQualityThresh() );
		settings.addBoolean( TimeSeriesReaderNodeModel.PARAM_MATRIXENABLED, estmodelui.isMatrixFilterEnabled() );
		settings.addBoolean( TimeSeriesReaderNodeModel.PARAM_AGENTENABLED, estmodelui.isAgentFilterEnabled() );
		settings.addString( TimeSeriesReaderNodeModel.PARAM_MATRIXSTRING, estmodelui.getMatrixString() );
		settings.addString( TimeSeriesReaderNodeModel.PARAM_AGENTSTRING, estmodelui.getAgentString() );
	}

	protected void loadSettingsFrom( NodeSettingsRO settings, PortObjectSpec[] specs )  {
		
		try {
			
			dbui.setFilename( settings.getString( DbConfigurationUi.PARAM_FILENAME ) );
			dbui.setLogin( settings.getString( DbConfigurationUi.PARAM_LOGIN ) );
			dbui.setPasswd( settings.getString( DbConfigurationUi.PARAM_PASSWD ) );
			dbui.setOverride( settings.getBoolean( DbConfigurationUi.PARAM_OVERRIDE ) );
			estmodelui.setLevel( settings.getInt( ModelCatalogReaderNodeModel.PARAM_LEVEL ) );
			estmodelui.setModelFilterEnabled( settings.getBoolean( ModelCatalogReaderNodeModel.PARAM_MODELFILTERENABLED ) );
			estmodelui.enableModelList( settings.getString( ModelCatalogReaderNodeModel.PARAM_MODELLIST ) );
			estmodelui.setQualityMode( settings.getInt( EstimatedModelReaderNodeModel.PARAM_QUALITYMODE ) );
			estmodelui.setQualityThresh( settings.getDouble( EstimatedModelReaderNodeModel.PARAM_QUALITYTHRESH ) );
		}
		catch( InvalidSettingsException e ) {			
			e.printStackTrace( System.err );
		}
		catch ( PmmException e ) {
			e.printStackTrace( System.err );
		}
		
	}
	
	@Override
	public void actionPerformed( ActionEvent arg0 ) {

		try {
			
			updateModelName();
		}
		catch( Exception e ) {
			
			e.printStackTrace( System.err );
		}
		
	}
	private void updateModelName() throws ClassNotFoundException, SQLException, PmmException {
        // fetch database connection
    	Bfrdb db;
    	ResultSet result;
    	
    	estmodelui.clearModelSet();

        db = null;
    	if( dbui.getOverride() ) {
			db = new Bfrdb( dbui.getFilename(), dbui.getLogin(), dbui.getPasswd() );
		} else {
			db = new Bfrdb( BfRNodePluginActivator.getBfRService() );
		}
    	
    	result = db.selectModel( 1 );
    	    	
    	while( result.next() )
    		estmodelui.addModelPrim(
				result.getInt( Bfrdb.ATT_MODELID ),
				result.getString( Bfrdb.ATT_NAME ) );
    		
    	result = db.selectModel( 2 );
    	
    	while( result.next() )
    		estmodelui.addModelSec(
				result.getInt( Bfrdb.ATT_MODELID ),
				result.getString( Bfrdb.ATT_NAME ) );
    	
	}
}

