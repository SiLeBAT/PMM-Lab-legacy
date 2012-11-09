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

import org.hsh.bfr.db.DBKernel;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmTimeSeries;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.manualmodelconf.ui.MMC_M;
import de.bund.bfr.knime.pmm.manualmodelconf.ui.MMC_TS;

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
public class ManualModelConfNodeDialog extends DataAwareNodeDialogPane {
	
	private MMC_M m_mmcm;
	private MMC_TS m_mmcts;

    /**
     * New pane for configuring the ManualModelConf node.
     */
    protected ManualModelConfNodeDialog() {
    	try {    
    		m_mmcm = new MMC_M(JOptionPane.getRootFrame(), 1, "");
    		m_mmcm.setConnection(DBKernel.getLocalConn(true));
    		this.addTab("Model Definition", m_mmcm);    	
    		
    		m_mmcts = new MMC_TS();
    		this.addTab("Microbial Data", m_mmcts);    
    		
    	}
    	catch( Exception e ) {
    		e.printStackTrace( System.err );
    	}
    }

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings)
			throws InvalidSettingsException {	
		//m_confui.stopCellEditing();
		m_mmcm.stopCellEditing();
		//settings.addString( ManualModelConfNodeModel.PARAM_XMLSTRING, m_confui.toXmlString() );
		settings.addString( ManualModelConfNodeModel.PARAM_XMLSTRING, m_mmcm.toXmlString() );
		try {
			//PmmTimeSeries ts = confui.getTS();
			PmmTimeSeries ts = m_mmcts.getTS();
			if (ts.getAgentDetail() != null) {
				settings.addString(ManualModelConfNodeModel.CFGKEY_AGENT, ts.getAgentDetail());
			}
			else {
				settings.addString(ManualModelConfNodeModel.CFGKEY_AGENT, "");
			}
			if (ts.getMatrixDetail() != null) {
				settings.addString(ManualModelConfNodeModel.CFGKEY_MATRIX, ts.getMatrixDetail());
			}
			else {
				settings.addString(ManualModelConfNodeModel.CFGKEY_MATRIX, "");
			}
			if (ts.getComment() != null) {
				settings.addString(ManualModelConfNodeModel.CFGKEY_COMMENT, ts.getComment());
			}
			else {
				settings.addString(ManualModelConfNodeModel.CFGKEY_COMMENT, "");
			}
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
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] inData) throws NotConfigurableException {
		if (inData != null && inData.length == 1) {
		    DataTableSpec inSpec = inData[0].getDataTableSpec();
		    try {
			    KnimeSchema tsSchema = new TimeSeriesSchema();
			    KnimeSchema inSchema1 = new Model1Schema();
			    KnimeSchema inSchema2 = new Model2Schema();
			    boolean hasTs = tsSchema.conforms(inSpec);
			    boolean hasM1 = inSchema1.conforms(inSpec);
			    boolean hasM2 = inSchema2.conforms(inSpec);
		    	
			    KnimeSchema finalSchema = null;
		    	if (hasM1 && hasM2) finalSchema = KnimeSchema.merge(inSchema1, inSchema2);
		    	else if (hasM1) finalSchema = inSchema1;
		    	else if (hasM2) finalSchema = inSchema2;
		    	if (hasTs) finalSchema = (finalSchema == null) ? tsSchema : KnimeSchema.merge(tsSchema, finalSchema);
		    	if (finalSchema != null) {
		    		KnimeRelationReader reader = new KnimeRelationReader(finalSchema, inData[0]);
		    		PmmXmlDoc pmmDoc = new PmmXmlDoc();
		    		Integer condID = null;
		    		Integer firstM1EstID = null;
		    		while (reader.hasMoreElements()) {
		    			KnimeTuple row = reader.nextElement();
		    			Integer actualM1EstID = row.getInt(Model1Schema.getAttribute(Model1Schema.ATT_ESTMODELID, 1));
		    			if (hasTs) {
		    				if (firstM1EstID == null || actualM1EstID.intValue() == firstM1EstID.intValue()) {
				    			PmmTimeSeries ts = new PmmTimeSeries(row);
				    			m_mmcts.setTS(ts);
				    			condID = ts.getCondId();
		    				}
		    			}
		    			if (hasM1) {
		    				if (firstM1EstID == null) {
				    			ParametricModel pm = new ParametricModel(row, 1, hasTs ? condID : null);
				    			pmmDoc.add(pm);
			    				if (!hasM2) break;
			    				firstM1EstID = pm.getEstModelId();
		    				}
		    			}
		    			if (hasM2) {
		    				if (!hasM1 || actualM1EstID.intValue() == firstM1EstID.intValue()) {
				    			pmmDoc.add(new ParametricModel(row, 2, null));
		    				}
			    			if (!hasM1) break;
		    			}
		    		}
		    		m_mmcm.setFromXmlString(pmmDoc.toXmlString());
		    	}
		    }
		    catch (PmmException e) {}
		}
		else {
			try {
				if (settings.containsKey(ManualModelConfNodeModel.PARAM_XMLSTRING)) {
					m_mmcm.setFromXmlString(settings.getString(ManualModelConfNodeModel.PARAM_XMLSTRING));
				}
			}
			catch( InvalidSettingsException e ) {
				e.printStackTrace( System.err );
			}
			
			try {
				m_mmcts.setTS(settings.getString(ManualModelConfNodeModel.CFGKEY_AGENT),
						settings.getString(ManualModelConfNodeModel.CFGKEY_MATRIX),
						settings.getString(ManualModelConfNodeModel.CFGKEY_COMMENT),
						settings.getDouble(ManualModelConfNodeModel.CFGKEY_TEMPERATURE),
						settings.getDouble(ManualModelConfNodeModel.CFGKEY_PH),
						settings.getDouble(ManualModelConfNodeModel.CFGKEY_AW));
			}
			catch (Exception e) {}
		}
	}
}

