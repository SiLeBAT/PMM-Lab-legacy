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
	private JCheckBox delTestConditions;

	protected DatabaseDeleteNodeDialog() {
    	JPanel panel = new JPanel();    	    	
    	dbui = new DbConfigurationUi(true);
    	    	
    	panel.setLayout(new BorderLayout());
    	panel.add(dbui, BorderLayout.CENTER);    	
    	    	
    	delTestConditions = new JCheckBox(); delTestConditions.setText("Delete all entries for test conditions as well?"); panel.add(delTestConditions);

    	addTab("Database settings", panel);
    }

	@Override
	protected void saveSettingsTo( NodeSettingsWO settings )
			throws InvalidSettingsException {
		
		settings.addString(DatabaseDeleteNodeModel.PARAM_FILENAME, dbui.getFilename());
		settings.addString(DatabaseDeleteNodeModel.PARAM_LOGIN, dbui.getLogin());
		settings.addString(DatabaseDeleteNodeModel.PARAM_PASSWD, dbui.getPasswd());
		settings.addBoolean(DatabaseDeleteNodeModel.PARAM_OVERRIDE, dbui.isOverride());

		settings.addBoolean(DatabaseDeleteNodeModel.PARAM_DELTESTCOND, delTestConditions.isSelected());
	}

	protected void loadSettingsFrom( NodeSettingsRO settings, PortObjectSpec[] specs )  {		
		try {			
			dbui.setFilename(settings.getString(DatabaseDeleteNodeModel.PARAM_FILENAME));
			dbui.setLogin(settings.getString(DatabaseDeleteNodeModel.PARAM_LOGIN));
			dbui.setPasswd(settings.getString(DatabaseDeleteNodeModel.PARAM_PASSWD));
			dbui.setOverride(settings.getBoolean(DatabaseDeleteNodeModel.PARAM_OVERRIDE));
			
			delTestConditions.setSelected(settings.getBoolean(DatabaseDeleteNodeModel.PARAM_DELTESTCOND));
		}
		catch( InvalidSettingsException e ) {			
			e.printStackTrace( System.err );
		}
		catch ( PmmException e ) {
			e.printStackTrace( System.err );
		}
	}
}

