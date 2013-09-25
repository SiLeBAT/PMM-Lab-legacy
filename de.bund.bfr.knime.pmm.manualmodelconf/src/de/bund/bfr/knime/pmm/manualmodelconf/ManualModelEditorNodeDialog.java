/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
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

import java.io.IOException;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.hsh.bfr.db.DBKernel;
import org.jdom2.JDOMException;
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
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
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
public class ManualModelEditorNodeDialog extends DataAwareNodeDialogPane {
	
	private MMC_M m_mmcm;
	private MMC_TS m_mmcts;

	/**
     * New pane for configuring the ManualModelConf node.
     */
    protected ManualModelEditorNodeDialog() {
    	try {    
    		m_mmcts = new MMC_TS();
    		m_mmcm = new MMC_M(JOptionPane.getRootFrame(), 1, "", false, m_mmcts);
    		m_mmcm.setConnection(DBKernel.getLocalConn(true));
    		this.addTab("Model Definition", m_mmcm);    	    		
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
		settings.addString( ManualModelConfNodeModel.PARAM_XMLSTRING, m_mmcm.listToXmlString() );
		String tStr = m_mmcm.tssToXmlString();
		settings.addString( ManualModelConfNodeModel.PARAM_TSXMLSTRING, tStr );//-1673022417
	}
	
	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] inData) throws NotConfigurableException {
		String mStr = null;
		String tsStr = null;
		// MMC_M
		try {
			if (settings.containsKey(ManualModelConfNodeModel.PARAM_XMLSTRING)) {
				mStr = settings.getString(ManualModelConfNodeModel.PARAM_XMLSTRING);
			}
		}
		catch( InvalidSettingsException e ) {
			e.printStackTrace();
		}

		//MMC_TS
		try {
			if (settings.containsKey(ManualModelConfNodeModel.PARAM_TSXMLSTRING)) {
				tsStr = settings.getString(ManualModelConfNodeModel.PARAM_TSXMLSTRING);
			}
		}
		catch (Exception e) {} // e.printStackTrace();
		if (inData != null && inData.length == 1) {
			HashMap<Integer, ParametricModel> mlist = new HashMap<Integer, ParametricModel>();
			HashMap<Integer, PmmTimeSeries> tslist = new HashMap<Integer, PmmTimeSeries>();
			try {
				if (mStr != null && !mStr.isEmpty()) {
					PmmXmlDoc mDoc = new PmmXmlDoc(mStr);
					for (int i = 0; i < mDoc.size(); i++) {
						PmmXmlElementConvertable el = mDoc.get(i);
						if (el instanceof ParametricModel) {
							ParametricModel pm = (ParametricModel) el;
							mlist.put(pm.getEstModelId(), pm);
						}
					}
				}
				if (tsStr != null && !tsStr.isEmpty()) {
					PmmXmlDoc tsDoc = new PmmXmlDoc(tsStr);
					for (int i = 0; i < tsDoc.size(); i++) {
						PmmXmlElementConvertable el = tsDoc.get(i);
						if (el instanceof PmmTimeSeries) {
							PmmTimeSeries ts = (PmmTimeSeries) el;
							tslist.put(ts.getCondId(), ts);
						}
					}
				}
			}
			catch (IOException | JDOMException e) {
				e.printStackTrace();
			}	
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
		    		HashMap<Integer, PmmTimeSeries> tss = new HashMap<Integer, PmmTimeSeries>();
		    		HashMap<Integer, ParametricModel> m1s = new HashMap<Integer, ParametricModel>();
		    		HashMap<Integer, ParametricModel> m2s = new HashMap<Integer, ParametricModel>();
		    		HashMap<ParametricModel, HashMap<String, ParametricModel>> m_secondaryModels = new HashMap<ParametricModel, HashMap<String, ParametricModel>>();
		    		Integer condID = null;
		    		Integer m1EstID = null, m2EstID;
		    		while (reader.hasMoreElements()) {
		    			KnimeTuple row = reader.nextElement();
		    			if (hasTs) {
			    			PmmTimeSeries ts = new PmmTimeSeries(row);
			    			condID = ts.getCondId();
			    			//System.err.println(condID);
			    			if (tslist.containsKey(condID)) tss.put(condID, tslist.get(condID));
			    			else tss.put(condID, ts);
		    			}
		    			if (hasM1) {
			    			ParametricModel pm1 = new ParametricModel(row, 1, hasTs ? condID : null);
			    			m1EstID = pm1.getEstModelId();
			    			if (!m1s.containsKey(m1EstID)) {
				    			if (mlist.containsKey(m1EstID)) m1s.put(m1EstID, mlist.get(m1EstID));
				    			else m1s.put(m1EstID, pm1);			    				
			    			}
			    			if (hasM2) {
			    				ParametricModel pm2 = new ParametricModel(row, 2, null);
			    				m2EstID = pm2.getEstModelId();
				    			if (!m2s.containsKey(m2EstID)) {
					    			if (mlist.containsKey(m2EstID)) m2s.put(m2EstID, mlist.get(m2EstID));
					    			else m2s.put(m2EstID, pm2);			    				
				    			}
				    			if (!m_secondaryModels.containsKey(m1s.get(m1EstID))) m_secondaryModels.put(m1s.get(m1EstID), new HashMap<String, ParametricModel>());
				    			HashMap<String, ParametricModel> hm = m_secondaryModels.get(m1s.get(m1EstID));
				    			hm.put(pm2.getDepVar(), m2s.get(m2EstID));
			    			}
		    			}
		    		}
		    		m_mmcm.setInputData(m1s.values(), m_secondaryModels, tss);
		    	}
		    }
		    catch (PmmException e) {}
		}
		else {
			if (tsStr != null) m_mmcts.setTS(tsStr);
			if (mStr != null) m_mmcm.setFromXmlString(mStr);
		}
	}
}

