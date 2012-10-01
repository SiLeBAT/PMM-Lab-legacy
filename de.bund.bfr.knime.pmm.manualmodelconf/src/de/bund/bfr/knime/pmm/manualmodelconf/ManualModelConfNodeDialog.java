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

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.manualmodelconf.ui.MMC_M;
import de.bund.bfr.knime.pmm.manualmodelconf.ui.MMC_TS;
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
public class ManualModelConfNodeDialog extends NodeDialogPane {
	
	private ManualModelConfUi confui;

    /**
     * New pane for configuring the ManualModelConf node.
     */
    protected ManualModelConfNodeDialog() {




        
    	try {    		
        	confui = new ManualModelConfUi(JOptionPane.getRootFrame());        	
    		confui.setDb( BfRNodePluginActivator.getBfRService() );        	
    		this.addTab( "Model definition", confui );    	
    		/*
    		MMC_M mmcm = new MMC_M();
    		mmcm.setDb(BfRNodePluginActivator.getBfRService());
    		this.addTab( "New Model definition", mmcm );    	
    		
    		this.addTab( "Time Series definition", new MMC_TS() );    
    		*/		
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
		try {
			PmmTimeSeries ts = confui.getTS();
			settings.addString(ManualModelConfNodeModel.CFGKEY_AGENT, ts.getAgentDetail());
			settings.addString(ManualModelConfNodeModel.CFGKEY_MATRIX, ts.getMatrixDetail());
			settings.addString(ManualModelConfNodeModel.CFGKEY_COMMENT, ts.getComment());
			if (ts.getTemperature() != null) {
				settings.addDouble(ManualModelConfNodeModel.CFGKEY_TEMPERATURE, ts.getTemperature());
			}
			else {
				settings.addDouble(ManualModelConfNodeModel.CFGKEY_TEMPERATURE, Double.NaN);
			}
			if (ts.getPh() != null) {
				settings.addDouble(ManualModelConfNodeModel.CFGKEY_PH, ts.getPh());
			}
			else {
				settings.addDouble(ManualModelConfNodeModel.CFGKEY_TEMPERATURE, Double.NaN);
			}
			if (ts.getWaterActivity() != null) {
				settings.addDouble(ManualModelConfNodeModel.CFGKEY_AW, ts.getWaterActivity());
			}
			else {
				settings.addDouble(ManualModelConfNodeModel.CFGKEY_TEMPERATURE, Double.NaN);
			}
		}
		catch (PmmException e) {
			if (e.getMessage().equals("Invalid Value")) {
				throw new InvalidSettingsException("Invalid Value");
			} else {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void loadSettingsFrom( final NodeSettingsRO settings, final DataTableSpec[] specs ) {

		try {
			if (settings.containsKey(ManualModelConfNodeModel.PARAM_XMLSTRING)) {
				confui.setFromXmlString( settings.getString(ManualModelConfNodeModel.PARAM_XMLSTRING) );
			}
		}
		catch( InvalidSettingsException e ) {
			e.printStackTrace( System.err );
		}
		
		try {
			confui.setTS(settings.getString(ManualModelConfNodeModel.CFGKEY_AGENT),
					settings.getString(ManualModelConfNodeModel.CFGKEY_MATRIX),
					settings.getString(ManualModelConfNodeModel.CFGKEY_COMMENT),
					settings.getDouble(ManualModelConfNodeModel.CFGKEY_TEMPERATURE),
					settings.getDouble(ManualModelConfNodeModel.CFGKEY_PH),
					settings.getDouble(ManualModelConfNodeModel.CFGKEY_AW));
		}
		catch (Exception e) {}
	}
}

