/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.estimatedmodelwriter;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObjectSpec;

/**
 * <code>NodeDialog</code> for the "EstimatedModelWriter" Node.
 * 
 * @author Jorgen Brandt
 */
public class EstimatedModelWriterNodeDialog extends NodeDialogPane {

	//private DbConfigurationUi dbui;

    /**
     * New pane for configuring the EstimatedModelWriter node.
     */
    protected EstimatedModelWriterNodeDialog() {
    	
    	//dbui = new DbConfigurationUi();    	
    	//addTab("Database connection", dbui);
    }
    
	@Override
	protected void saveSettingsTo( NodeSettingsWO settings )
			throws InvalidSettingsException {
		/*
		settings.addString( EstimatedModelWriterNodeModel.PARAM_FILENAME, dbui.getFilename() );
		settings.addString( EstimatedModelWriterNodeModel.PARAM_LOGIN, dbui.getLogin() );
		settings.addString( EstimatedModelWriterNodeModel.PARAM_PASSWD, dbui.getPasswd() );
		settings.addBoolean( EstimatedModelWriterNodeModel.PARAM_OVERRIDE, dbui.isOverride() );
		*/
	}

	protected void loadSettingsFrom( NodeSettingsRO settings, PortObjectSpec[] specs )  {
		/*
		try {
			
			dbui.setFilename( settings.getString( EstimatedModelWriterNodeModel.PARAM_FILENAME ) );
			dbui.setLogin( settings.getString( EstimatedModelWriterNodeModel.PARAM_LOGIN ) );
			dbui.setPasswd( settings.getString( EstimatedModelWriterNodeModel.PARAM_PASSWD ) );
			dbui.setOverride( settings.getBoolean( EstimatedModelWriterNodeModel.PARAM_OVERRIDE ) );
		}
		catch( InvalidSettingsException ex ) {
			
			ex.printStackTrace( System.err );
		}
		*/
	}


}

