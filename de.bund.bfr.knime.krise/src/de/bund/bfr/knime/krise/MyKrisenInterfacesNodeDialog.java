package de.bund.bfr.knime.krise;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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

import com.toedter.calendar.JDateChooser;

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
	private JCheckBox doAnonymize, antiArticle, antiCharge, antiCompany;
	private JTextField company, charge, artikel;
	private JDateChooser dateFrom, dateTo;

	protected MyKrisenInterfacesNodeDialog() {
    	JPanel panel;
    	
    	panel = new JPanel();
    	panel.setLayout( new BoxLayout( panel, BoxLayout.Y_AXIS ) );
    	
    	
    	dbui = new DbConfigurationUi();
    	panel.add(dbui);
    	panel.add(new JLabel("dateFrom:")); dateFrom = new JDateChooser(); panel.add(dateFrom);
    	panel.add(new JLabel("dateTo:")); dateTo = new JDateChooser(); panel.add(dateTo);
    	doAnonymize = new JCheckBox(); doAnonymize.setText("Anonymisieren?"); panel.add(doAnonymize);
    	company = new JTextField(); panel.add(new JLabel("Company:")); panel.add(company);
    	antiCompany = new JCheckBox(); antiCompany.setText("Anti Company?"); panel.add(antiCompany); antiCompany.setVisible(false);
    	charge = new JTextField(); panel.add(new JLabel("Charge:")); panel.add(charge);
    	antiCharge = new JCheckBox(); antiCharge.setText("Anti Charge?"); panel.add(antiCharge); antiCharge.setVisible(false);
    	artikel = new JTextField(); panel.add(new JLabel("Artikel:")); panel.add(artikel);
    	antiArticle = new JCheckBox(); antiArticle.setText("Anti Artikel?"); panel.add(antiArticle);
    	
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
		settings.addString( MyKrisenInterfacesNodeModel.PARAM_FILTER_COMPANY, company.getText() );
		settings.addString( MyKrisenInterfacesNodeModel.PARAM_FILTER_CHARGE, charge.getText() );
		settings.addString( MyKrisenInterfacesNodeModel.PARAM_FILTER_ARTIKEL, artikel.getText() );
		settings.addBoolean( MyKrisenInterfacesNodeModel.PARAM_ANTIARTICLE, antiArticle.isSelected() );
		settings.addBoolean( MyKrisenInterfacesNodeModel.PARAM_ANTICHARGE, antiCharge.isSelected() );
		settings.addBoolean( MyKrisenInterfacesNodeModel.PARAM_ANTICOMPANY, antiCompany.isSelected() );
		
	    SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 2012-07-10 00:00:00
	    String strDate = dateFrom.getDate() == null ? "" : sdfToDate.format(dateFrom.getDate());
	    if (!strDate.isEmpty()) strDate = strDate.substring(0, strDate.indexOf(" ")) + " 00:00:00";
		settings.addString(MyKrisenInterfacesNodeModel.PARAM_FILTER_DATEFROM, strDate);
		strDate = dateTo.getDate() == null ? "" : sdfToDate.format(dateTo.getDate());
		if (!strDate.isEmpty()) strDate = strDate.substring(0, strDate.indexOf(" ")) + " 23:59:59";
		settings.addString(MyKrisenInterfacesNodeModel.PARAM_FILTER_DATETO, strDate);
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
			antiArticle.setSelected(settings.getBoolean(MyKrisenInterfacesNodeModel.PARAM_ANTIARTICLE));
			antiCharge.setSelected(settings.getBoolean(MyKrisenInterfacesNodeModel.PARAM_ANTICHARGE));
			antiCompany.setSelected(settings.getBoolean(MyKrisenInterfacesNodeModel.PARAM_ANTICOMPANY));

		    SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 2012-07-10 00:00:00
	    	try {
	    		String df = settings.getString(MyKrisenInterfacesNodeModel.PARAM_FILTER_DATEFROM);
				if (df != null && !df.isEmpty()) dateFrom.setDate(sdfToDate.parse(df));
	    		df = settings.getString(MyKrisenInterfacesNodeModel.PARAM_FILTER_DATETO);
				if (df != null && !df.isEmpty()) dateTo.setDate(sdfToDate.parse(df));
			}
	    	catch (ParseException e) {
				e.printStackTrace();
			}
		}
		catch( InvalidSettingsException ex ) {
			
			ex.printStackTrace( System.err );
		}
		
	}
}