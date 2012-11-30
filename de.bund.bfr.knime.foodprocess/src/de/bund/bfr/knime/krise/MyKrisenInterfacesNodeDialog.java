package de.bund.bfr.knime.krise;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.port.PortObjectSpec;

import de.bund.bfr.knime.pmm.common.ui.DbConfigurationUi;

/**
 * <code>NodeDialog</code> for the "MyKrisenInterfaces" Node.
 * 
 *
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more 
 * complex dialog please derive directly from 
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author draaw
 */
public class MyKrisenInterfacesNodeDialog extends NodeDialogPane {

	private DbConfigurationUi dbui;

	protected MyKrisenInterfacesNodeDialog() {
    	JPanel panel;
    	
    	panel = new JPanel();
    	panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
    	
    	
    	dbui = new DbConfigurationUi();
    	panel.add(dbui);
    	
    	addTab("Database connection", panel);
    }
	
	@Override
	protected void saveSettingsTo( final NodeSettingsWO settings )
			throws InvalidSettingsException {
		
		settings.addString( MyKrisenInterfacesNodeModel.PARAM_FILENAME, dbui.getFilename() );
		settings.addString( MyKrisenInterfacesNodeModel.PARAM_LOGIN, dbui.getLogin() );
		settings.addString( MyKrisenInterfacesNodeModel.PARAM_PASSWD, dbui.getPasswd() );
		settings.addBoolean( MyKrisenInterfacesNodeModel.PARAM_OVERRIDE, dbui.isOverride() );
	}

	@Override
	protected void loadSettingsFrom( final NodeSettingsRO settings, final PortObjectSpec[] specs )  {
		
		try {
			
			dbui.setFilename( settings.getString( MyKrisenInterfacesNodeModel.PARAM_FILENAME ) );
			dbui.setLogin( settings.getString( MyKrisenInterfacesNodeModel.PARAM_LOGIN ) );
			dbui.setPasswd( settings.getString( MyKrisenInterfacesNodeModel.PARAM_PASSWD ) );
			dbui.setOverride( settings.getBoolean( MyKrisenInterfacesNodeModel.PARAM_OVERRIDE ) );
		}
		catch( InvalidSettingsException ex ) {
			
			ex.printStackTrace( System.err );
		}
		
	}
}