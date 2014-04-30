/*******************************************************************************
 * PMM-Lab © 2012-2014, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012-2014, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Christian Thöns (BfR)
 * Matthias Filter (BfR)
 * Armin A. Weiser (BfR)
 * Alexander Falenski (BfR)
 * Jörgen Brandt (BfR)
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
package de.bund.bfr.knime.pmm.dbdelete;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.pmm.common.PmmException;

/**
 * <code>NodeDialog</code> for the "DatabaseDelete" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Armin A. Weiser
 */
public class DatabaseDeleteNodeDialog extends NodeDialogPane {

	//private DbConfigurationUi dbui;
	private JCheckBox delTS;
	private JCheckBox delPM;
	private JCheckBox delSM;

	protected DatabaseDeleteNodeDialog() {
		/*
    	JPanel panel = new JPanel();    	    	
    	dbui = new DbConfigurationUi();
    	    	
    	panel.setLayout(new BorderLayout());
    	panel.add(dbui, BorderLayout.CENTER);    	
    	    	*/
    	JPanel panel2 = new JPanel();    	    	
    	delTS = new JCheckBox(); delTS.setText("Delete test conditions?"); panel2.add(delTS);
    	delPM = new JCheckBox(); delPM.setText("Delete primary models?"); panel2.add(delPM);
    	delSM = new JCheckBox(); delSM.setText("Delete secondary models?"); panel2.add(delSM);
    	//panel.add(panel2, BorderLayout.SOUTH);    	

    	addTab("Database settings", panel2);
    }

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		/*
		settings.addString(DatabaseDeleteNodeModel.PARAM_FILENAME, dbui.getFilename());
		settings.addString(DatabaseDeleteNodeModel.PARAM_LOGIN, dbui.getLogin());
		settings.addString(DatabaseDeleteNodeModel.PARAM_PASSWD, dbui.getPasswd());
		settings.addBoolean(DatabaseDeleteNodeModel.PARAM_OVERRIDE, dbui.isOverride());
*/
		settings.addBoolean(DatabaseDeleteNodeModel.PARAM_DELTESTCOND, delTS.isSelected());
		settings.addBoolean(DatabaseDeleteNodeModel.PARAM_DELPRIMARYMODELS, delPM.isSelected());
		settings.addBoolean(DatabaseDeleteNodeModel.PARAM_DELSECONDARYMODELS, delSM.isSelected());
	}

	protected void loadSettingsFrom( NodeSettingsRO settings, PortObjectSpec[] specs )  {		
		try {	
			/*
			dbui.setFilename(settings.getString(DatabaseDeleteNodeModel.PARAM_FILENAME));
			dbui.setLogin(settings.getString(DatabaseDeleteNodeModel.PARAM_LOGIN));
			dbui.setPasswd(settings.getString(DatabaseDeleteNodeModel.PARAM_PASSWD));
			dbui.setOverride(settings.getBoolean(DatabaseDeleteNodeModel.PARAM_OVERRIDE));
			*/
			delTS.setSelected(settings.getBoolean(DatabaseDeleteNodeModel.PARAM_DELTESTCOND));
			delPM.setSelected(settings.getBoolean(DatabaseDeleteNodeModel.PARAM_DELPRIMARYMODELS));
			delSM.setSelected(settings.getBoolean(DatabaseDeleteNodeModel.PARAM_DELSECONDARYMODELS));
		}
		catch( InvalidSettingsException e ) {			
			e.printStackTrace( System.err );
		}
		catch ( PmmException e ) {
			e.printStackTrace( System.err );
		}
	}
}

