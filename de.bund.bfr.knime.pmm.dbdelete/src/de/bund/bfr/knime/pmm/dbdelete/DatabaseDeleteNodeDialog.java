package de.bund.bfr.knime.pmm.dbdelete;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.ui.DbConfigurationUi;

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

	private DbConfigurationUi dbui;
	private JCheckBox delTS;
	private JCheckBox delPM;
	private JCheckBox delSM;

	protected DatabaseDeleteNodeDialog() {
    	JPanel panel = new JPanel();    	    	
    	dbui = new DbConfigurationUi();
    	    	
    	panel.setLayout(new BorderLayout());
    	panel.add(dbui, BorderLayout.CENTER);    	
    	    	
    	JPanel panel2 = new JPanel();    	    	
    	delTS = new JCheckBox(); delTS.setText("Delete test conditions?"); panel2.add(delTS);
    	delPM = new JCheckBox(); delPM.setText("Delete primary models?"); panel2.add(delPM);
    	delSM = new JCheckBox(); delSM.setText("Delete secondary models?"); panel2.add(delSM);
    	panel.add(panel2, BorderLayout.SOUTH);    	

    	addTab("Database settings", panel2);
    }

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		
		settings.addString(DatabaseDeleteNodeModel.PARAM_FILENAME, dbui.getFilename());
		settings.addString(DatabaseDeleteNodeModel.PARAM_LOGIN, dbui.getLogin());
		settings.addString(DatabaseDeleteNodeModel.PARAM_PASSWD, dbui.getPasswd());
		settings.addBoolean(DatabaseDeleteNodeModel.PARAM_OVERRIDE, dbui.isOverride());

		settings.addBoolean(DatabaseDeleteNodeModel.PARAM_DELTESTCOND, delTS.isSelected());
		settings.addBoolean(DatabaseDeleteNodeModel.PARAM_DELPRIMARYMODELS, delPM.isSelected());
		settings.addBoolean(DatabaseDeleteNodeModel.PARAM_DELSECONDARYMODELS, delSM.isSelected());
	}

	protected void loadSettingsFrom( NodeSettingsRO settings, PortObjectSpec[] specs )  {		
		try {			
			dbui.setFilename(settings.getString(DatabaseDeleteNodeModel.PARAM_FILENAME));
			dbui.setLogin(settings.getString(DatabaseDeleteNodeModel.PARAM_LOGIN));
			dbui.setPasswd(settings.getString(DatabaseDeleteNodeModel.PARAM_PASSWD));
			dbui.setOverride(settings.getBoolean(DatabaseDeleteNodeModel.PARAM_OVERRIDE));
			
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

