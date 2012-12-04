package de.bund.bfr.knime.krise;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	private JCheckBox doAnonymize;
	private JTextField company, charge, artikel;

	protected MyKrisenInterfacesNodeDialog() {
    	JPanel panel;
    	
    	panel = new JPanel();
    	panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
    	
    	
    	dbui = new DbConfigurationUi();
    	panel.add(dbui);
    	doAnonymize = new JCheckBox(); doAnonymize.setText("Anonymisieren?"); panel.add(doAnonymize);
    	company = new JTextField(); panel.add(new JLabel("Company:")); panel.add(company);
    	charge = new JTextField(); panel.add(new JLabel("Charge:")); panel.add(charge);
    	artikel = new JTextField(); panel.add(new JLabel("Artikel:")); panel.add(artikel);
    	
    	addTab("Database connection", panel);
    }
	
	@Override
	protected void saveSettingsTo( final NodeSettingsWO settings )
			throws InvalidSettingsException {
		
		settings.addString( MyKrisenInterfacesNodeModel.PARAM_FILENAME, dbui.getFilename() );
		settings.addString( MyKrisenInterfacesNodeModel.PARAM_LOGIN, dbui.getLogin() );
		settings.addString( MyKrisenInterfacesNodeModel.PARAM_PASSWD, dbui.getPasswd() );
		settings.addBoolean( MyKrisenInterfacesNodeModel.PARAM_OVERRIDE, dbui.isOverride() );
		settings.addBoolean( MyKrisenInterfacesNodeModel.PARAM_ANONYMIZE, doAnonymize.isSelected() );
		settings.addBoolean( MyKrisenInterfacesNodeModel.PARAM_ANONYMIZE, doAnonymize.isSelected() );
		settings.addString( MyKrisenInterfacesNodeModel.PARAM_FILTER_COMPANY, company.getText() );
		settings.addString( MyKrisenInterfacesNodeModel.PARAM_FILTER_CHARGE, charge.getText() );
		settings.addString( MyKrisenInterfacesNodeModel.PARAM_FILTER_ARTIKEL, artikel.getText() );
	}

	@Override
	protected void loadSettingsFrom( final NodeSettingsRO settings, final PortObjectSpec[] specs )  {
		
		try {
			
			dbui.setFilename( settings.getString( MyKrisenInterfacesNodeModel.PARAM_FILENAME ) );
			dbui.setLogin( settings.getString( MyKrisenInterfacesNodeModel.PARAM_LOGIN ) );
			dbui.setPasswd( settings.getString( MyKrisenInterfacesNodeModel.PARAM_PASSWD ) );
			dbui.setOverride( settings.getBoolean( MyKrisenInterfacesNodeModel.PARAM_OVERRIDE ) );
			doAnonymize.setSelected(settings.getBoolean(MyKrisenInterfacesNodeModel.PARAM_ANONYMIZE));
			company.setText(settings.getString( MyKrisenInterfacesNodeModel.PARAM_FILTER_COMPANY ));
			charge.setText(settings.getString( MyKrisenInterfacesNodeModel.PARAM_FILTER_CHARGE ));
			artikel.setText(settings.getString( MyKrisenInterfacesNodeModel.PARAM_FILTER_ARTIKEL ));
		}
		catch( InvalidSettingsException ex ) {
			
			ex.printStackTrace( System.err );
		}
		
	}
}