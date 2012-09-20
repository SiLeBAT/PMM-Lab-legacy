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
package de.bund.bfr.knime.pmm.estimatedmodelwriter;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;
import de.bund.bfr.knime.pmm.common.DbConfigurationUi;

/**
 * <code>NodeDialog</code> for the "EstimatedModelWriter" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Jorgen Brandt
 */
public class EstimatedModelWriterNodeDialog extends NodeDialogPane {

	private DbConfigurationUi ui;

    /**
     * New pane for configuring the EstimatedModelWriter node.
     */
    protected EstimatedModelWriterNodeDialog() {
    	
    	/* addDialogComponent(
    			new DialogComponentBoolean(
    				new SettingsModelBoolean( EstimatedModelWriterNodeModel.PARAM_OVERRIDE, false ),
    				"Override default database"
    			)
        	);


    	addDialogComponent(
    			new DialogComponentString(
    					new SettingsModelString(
    							EstimatedModelWriterNodeModel.PARAM_FILENAME,
    							"C:\\Dokumente und Einstellungen\\BrandtJ\\"+
    							"localHSH\\BfR\\DB" ),
    					"Filename : jdbc:hsqldb:file:", true, 40 ) );
    	
    	addDialogComponent(
    			new DialogComponentString(
    					new SettingsModelString(
    							EstimatedModelWriterNodeModel.PARAM_LOGIN,
    							"SA" ),
    					"Login :", true, 10 ) );
    	
    	addDialogComponent(
    			new DialogComponentString(
    					new SettingsModelString(
    							EstimatedModelWriterNodeModel.PARAM_PASSWD,
    							"" ),
    					"Password :", false, 7 ) ); */
    	
    	
    	ui = new DbConfigurationUi();    	
    	addTab( "Database connection", ui );
    }
    
	@Override
	protected void saveSettingsTo( NodeSettingsWO settings )
			throws InvalidSettingsException {
		
		settings.addString( DbConfigurationUi.PARAM_FILENAME, ui.getFilename() );
		settings.addString( DbConfigurationUi.PARAM_LOGIN, ui.getLogin() );
		settings.addString( DbConfigurationUi.PARAM_PASSWD, ui.getPasswd() );
		settings.addBoolean( DbConfigurationUi.PARAM_OVERRIDE, ui.isOverride() );
	}

	protected void loadSettingsFrom( NodeSettingsRO settings, PortObjectSpec[] specs )  {
		
		try {
			
			ui.setFilename( settings.getString( DbConfigurationUi.PARAM_FILENAME ) );
			ui.setLogin( settings.getString( DbConfigurationUi.PARAM_LOGIN ) );
			ui.setPasswd( settings.getString( DbConfigurationUi.PARAM_PASSWD ) );
			ui.setOverride( settings.getBoolean( DbConfigurationUi.PARAM_OVERRIDE ) );
		}
		catch( InvalidSettingsException ex ) {
			
			ex.printStackTrace( System.err );
		}
		
	}


}

