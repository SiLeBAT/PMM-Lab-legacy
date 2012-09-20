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
package de.bund.bfr.knime.pmm.manualmodelconf;

import javax.swing.JOptionPane;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.manualmodelconf.ui.ManualModelConfUi;
import de.dim.knime.bfr.internal.BfRNodePluginActivator;

/**
 * <code>NodeDialog</code> for the "ManualModelConf" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author ManualModelConf
 */
public class ManualModelEditNodeDialog extends NodeDialogPane {
	
	private ManualModelConfUi confui;

    /**
     * New pane for configuring the ManualModelConf node.
     */
    protected ManualModelEditNodeDialog() {




        
    	try {
        	confui = new ManualModelConfUi(JOptionPane.getRootFrame());        	
    		confui.setDb( BfRNodePluginActivator.getBfRService() );
        	
    		this.addTab( "Model definition", confui );
    	}
    	catch( Exception e ) {
    		e.printStackTrace( System.err );
    	}
    }

	@Override
	protected void saveSettingsTo( final NodeSettingsWO settings )
			throws InvalidSettingsException {	
		confui.stopCellEditing();
		settings.addString( ManualModelConfNodeModel.PARAM_XMLSTRING, confui.toXmlString() );
	}
	
	@Override
	protected void loadSettingsFrom( final NodeSettingsRO settings, final DataTableSpec[] specs ) {

		try {
			confui.setFromXmlString( settings.getString( ManualModelConfNodeModel.PARAM_XMLSTRING ) );
		}
		catch( InvalidSettingsException e ) {
			e.printStackTrace( System.err );
		}
	}
}

