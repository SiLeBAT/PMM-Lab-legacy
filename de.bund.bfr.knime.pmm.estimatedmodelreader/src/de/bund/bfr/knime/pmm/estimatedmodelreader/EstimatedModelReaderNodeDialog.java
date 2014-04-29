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
package de.bund.bfr.knime.pmm.estimatedmodelreader;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import javax.swing.JPanel;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.config.Config;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;

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
	//private DbConfigurationUi dbui;
	private EmReaderUi estmodelui;

	private Bfrdb db = null;
    /**
     * New pane for configuring the EstimatedModelReader node.
     */
    protected EstimatedModelReaderNodeDialog() {
    	JPanel panel = new JPanel();    	
    	/*
    	dbui = new DbConfigurationUi( true );
    	dbui.getApplyButton().addActionListener( this );
    	
    	try {
            // fetch database connection
        	db = null;
        	if( dbui.getOverride() ) {
    			db = new Bfrdb( dbui.getFilename(), dbui.getLogin(), dbui.getPasswd() );
    		} else {
    			db = new Bfrdb(DBKernel.getLocalConn(true));
    		}
    	}
    	catch (Exception e) {}
    	    	*/
    	try {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		} catch (Exception e1) {}
    	estmodelui = new EmReaderUi(db);
    	
    	panel.setLayout( new BorderLayout() );
    	//panel.add( dbui, BorderLayout.NORTH );    	
    	panel.add( estmodelui, BorderLayout.CENTER );    	
    	    	
    	addTab("Filter models", panel);
    	//addTab("Database connection", dbui);
    	
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
    	//dbui.saveSettingsTo(settings.addConfig("DbConfigurationUi"));
    	estmodelui.saveSettingsTo(settings.addConfig("EstModelReaderUi"));
		/*
		settings.addString( EstimatedModelReaderNodeModel.PARAM_FILENAME, dbui.getFilename() );
		settings.addString( EstimatedModelReaderNodeModel.PARAM_LOGIN, dbui.getLogin() );
		settings.addString( EstimatedModelReaderNodeModel.PARAM_PASSWD, dbui.getPasswd() );
		settings.addBoolean( EstimatedModelReaderNodeModel.PARAM_OVERRIDE, dbui.isOverride() );
		settings.addInt( EstimatedModelReaderNodeModel.PARAM_LEVEL, estmodelui.getLevel() );
		settings.addString( EstimatedModelReaderNodeModel.PARAM_MODELCLASS, estmodelui.getModelClass() );
		settings.addBoolean( EstimatedModelReaderNodeModel.PARAM_MODELFILTERENABLED, estmodelui.isModelFilterEnabled() );
		settings.addString( EstimatedModelReaderNodeModel.PARAM_MODELLIST, estmodelui.getModelList() );
		settings.addInt( EstimatedModelReaderNodeModel.PARAM_QUALITYMODE, estmodelui.getQualityMode() );
		settings.addDouble( EstimatedModelReaderNodeModel.PARAM_QUALITYTHRESH, estmodelui.getQualityThresh() );
		settings.addString( EstimatedModelReaderNodeModel.PARAM_MATRIXSTRING, estmodelui.getMatrixString() );
		settings.addString( EstimatedModelReaderNodeModel.PARAM_AGENTSTRING, estmodelui.getAgentString() );
		settings.addString( EstimatedModelReaderNodeModel.PARAM_LITERATURESTRING, estmodelui.getLiteratureString() );
		
		LinkedHashMap<String, DoubleTextField[]> params = estmodelui.getParameter();
		Config c = settings.addConfig(EstimatedModelReaderNodeModel.PARAM_PARAMETERS);
		String[] pars = new String[params.size()];
		String[] mins = new String[params.size()];
		String[] maxs = new String[params.size()];
		int i=0;
		for (String par : params.keySet()) {
			DoubleTextField[] dbl = params.get(par);
			pars[i] = par;
			mins[i] = ""+dbl[0].getValue();
			maxs[i] = ""+dbl[1].getValue();
			i++;
		}
		c.addStringArray(EstimatedModelReaderNodeModel.PARAM_PARAMETERNAME, pars);
		c.addStringArray(EstimatedModelReaderNodeModel.PARAM_PARAMETERMIN, mins);
		c.addStringArray(EstimatedModelReaderNodeModel.PARAM_PARAMETERMAX, maxs);
		*/
	}

	protected void loadSettingsFrom( NodeSettingsRO settings, PortObjectSpec[] specs )  {		
		try {	
			/*
			if (settings.containsKey("DbConfigurationUi")) dbui.setSettings(settings.getConfig("DbConfigurationUi"));
			else { // old Config
				dbui.setFilename( settings.getString( EstimatedModelReaderNodeModel.PARAM_FILENAME ) );
				dbui.setLogin( settings.getString( EstimatedModelReaderNodeModel.PARAM_LOGIN ) );
				dbui.setPasswd( settings.getString( EstimatedModelReaderNodeModel.PARAM_PASSWD ) );
				dbui.setOverride( settings.getBoolean( EstimatedModelReaderNodeModel.PARAM_OVERRIDE ) );
			}
			*/
			if (settings.containsKey("EstModelReaderUi")) estmodelui.setSettings(settings.getConfig("EstModelReaderUi"));
			else {
				estmodelui.setLevel( settings.getInt( EstimatedModelReaderNodeModel.PARAM_LEVEL ) );
				estmodelui.setModelClass( settings.getString( EstimatedModelReaderNodeModel.PARAM_MODELCLASS ) );
				estmodelui.setModelFilterEnabled( settings.getBoolean( EstimatedModelReaderNodeModel.PARAM_MODELFILTERENABLED ) );
				estmodelui.enableModelList( settings.getString( EstimatedModelReaderNodeModel.PARAM_MODELLIST ) );
				estmodelui.setQualityMode( settings.getInt( EstimatedModelReaderNodeModel.PARAM_QUALITYMODE ) );
				estmodelui.setQualityThresh( settings.getDouble( EstimatedModelReaderNodeModel.PARAM_QUALITYTHRESH ) );
				estmodelui.setMatrixString( settings.getString( EstimatedModelReaderNodeModel.PARAM_MATRIXSTRING ) );
				estmodelui.setAgentString( settings.getString( EstimatedModelReaderNodeModel.PARAM_AGENTSTRING ) );
				estmodelui.setLiteratureString(settings.getString( EstimatedModelReaderNodeModel.PARAM_LITERATURESTRING ) );
				
				Config c = settings.getConfig(EstimatedModelReaderNodeModel.PARAM_PARAMETERS);
				String[] pars = c.getStringArray(EstimatedModelReaderNodeModel.PARAM_PARAMETERNAME);
				String[] mins = c.getStringArray(EstimatedModelReaderNodeModel.PARAM_PARAMETERMIN);
				String[] maxs = c.getStringArray(EstimatedModelReaderNodeModel.PARAM_PARAMETERMAX);

				LinkedHashMap<String, DoubleTextField[]> params = new LinkedHashMap<String, DoubleTextField[]>();
				for (int i=0;i<pars.length;i++) {
					DoubleTextField[] dbl = new DoubleTextField[2];
					dbl[0] = new DoubleTextField(true);
					dbl[1] = new DoubleTextField(true);
					if (!mins[i].equals("null")) dbl[0].setValue(Double.parseDouble(mins[i]));
					if (!maxs[i].equals("null")) dbl[1].setValue(Double.parseDouble(maxs[i]));
					params.put(pars[i], dbl);
				}
				estmodelui.setParameter(params);	
			}
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
    	
    	estmodelui.clearModelSet();

    	estmodelui.setMiscItems(DBKernel.getItemListMisc(db.getConnection()));
    	
    	ResultSet result = db.selectModel(1);    	    	
    	while (result.next()) {
    		estmodelui.addModelPrim(result.getInt(Bfrdb.ATT_MODELID), result.getString(Bfrdb.ATT_NAME), DBKernel.myDBi.getHashMap("ModelType").get(result.getInt("Klasse")));
    	}
    	result = db.selectModel(2);    	
    	while (result.next()) {
    		estmodelui.addModelSec(result.getInt(Bfrdb.ATT_MODELID), result.getString(Bfrdb.ATT_NAME), DBKernel.myDBi.getHashMap("ModelType").get(result.getInt("Klasse")));
    	}
	}
}

