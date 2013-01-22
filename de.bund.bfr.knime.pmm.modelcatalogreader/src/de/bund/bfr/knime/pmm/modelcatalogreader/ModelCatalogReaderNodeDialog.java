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
package de.bund.bfr.knime.pmm.modelcatalogreader;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.ui.DbConfigurationUi;
import de.bund.bfr.knime.pmm.common.ui.ModelReaderUi;

/**
 * <code>NodeDialog</code> for the "ModelCatalogReader" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Jorgen Brandt
 */
public class ModelCatalogReaderNodeDialog extends NodeDialogPane implements ActionListener {

	// private JComboBox levelBox;
	private DbConfigurationUi ui;
	private ModelReaderUi filterui;
	
    /**
     * New pane for configuring the ModelReader node.
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @throws PmmException 
     */
    protected ModelCatalogReaderNodeDialog() {    	
    	JPanel panel = new JPanel();
    	    	
    	ui = new DbConfigurationUi( true );    	    	
    	ui.getApplyButton().addActionListener( this );    	
    	filterui = new ModelReaderUi();
    	
    	panel.setLayout( new BorderLayout() );
    	panel.add( ui, BorderLayout.NORTH );
    	panel.add( filterui, BorderLayout.CENTER );
    	
    	addTab( "Database connection", panel );
    	
    	try {
    		updateModelName();
    	}
    	catch( Exception e ) {
    		e.printStackTrace( System.err );
    	}
    }

	@Override
	protected void saveSettingsTo( NodeSettingsWO settings )
			throws InvalidSettingsException {
		
		settings.addString( ModelCatalogReaderNodeModel.PARAM_FILENAME, ui.getFilename() );
		settings.addString( ModelCatalogReaderNodeModel.PARAM_LOGIN, ui.getLogin() );
		settings.addString( ModelCatalogReaderNodeModel.PARAM_PASSWD, ui.getPasswd() );
		settings.addBoolean( ModelCatalogReaderNodeModel.PARAM_OVERRIDE, ui.isOverride() );
		settings.addInt( ModelCatalogReaderNodeModel.PARAM_LEVEL, filterui.getLevel() );
		settings.addBoolean( ModelCatalogReaderNodeModel.PARAM_MODELFILTERENABLED, filterui.isModelFilterEnabled() );
		settings.addString( ModelCatalogReaderNodeModel.PARAM_MODELLIST, filterui.toString() );
	}

	protected void loadSettingsFrom( NodeSettingsRO settings, PortObjectSpec[] specs )  {
		
		try {
			
			ui.setFilename( settings.getString( ModelCatalogReaderNodeModel.PARAM_FILENAME ) );
			ui.setLogin( settings.getString( ModelCatalogReaderNodeModel.PARAM_LOGIN ) );
			ui.setPasswd( settings.getString( ModelCatalogReaderNodeModel.PARAM_PASSWD ) );
			ui.setOverride( settings.getBoolean( ModelCatalogReaderNodeModel.PARAM_OVERRIDE ) );
			filterui.setLevel( settings.getInt( ModelCatalogReaderNodeModel.PARAM_LEVEL ) );
			filterui.setModelFilterEnabled( settings.getBoolean( ModelCatalogReaderNodeModel.PARAM_MODELFILTERENABLED ) );
			filterui.enableModelList( settings.getString( ModelCatalogReaderNodeModel.PARAM_MODELLIST ) );
		}
		catch( InvalidSettingsException e ) {
			e.printStackTrace( System.err );
		}
		catch( PmmException e ) {
			e.printStackTrace( System.err );
		}
		
	}

	private void updateModelName() throws ClassNotFoundException, SQLException, PmmException {
        // fetch database connection
    	Bfrdb db;
    	ResultSet result;
    	
    	filterui.clearModelSet();

        db = null;
    	if( ui.getOverride() ) {
			db = new Bfrdb( ui.getFilename(), ui.getLogin(), ui.getPasswd() );
		} else {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		}
    	
    	result = db.selectModel(1);    	    	
    	while (result.next()) {
    		//System.err.println(result.getString(Bfrdb.ATT_NAME) + "\t" + result.getInt("Klasse"));
    		filterui.addModelPrim(result.getInt(Bfrdb.ATT_MODELID), result.getString(Bfrdb.ATT_NAME), DBKernel.hashModelType.get(result.getInt("Klasse")));
    	}
    	result = db.selectModel(2);    	
    	while (result.next()) {
    		filterui.addModelSec(result.getInt(Bfrdb.ATT_MODELID), result.getString(Bfrdb.ATT_NAME), DBKernel.hashModelType.get(result.getInt("Klasse")));
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
	
}

