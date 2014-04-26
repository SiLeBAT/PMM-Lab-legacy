/*
 * Created by JFormDesigner on Tue Dec 18 15:46:21 CET 2012
 */

package de.bund.bfr.knime.pmm.ui.handlers;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;

import javax.swing.*;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyLogger;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.resources.Resources;
import de.bund.bfr.knime.pmm.common.units.CategoryReader;

/**
 * @author Armin Weiser
 */
public class SettingsDialog extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8433737081879655528L;
	
	public SettingsDialog() {
		initComponents();
		this.setIconImage(Resources.getInstance().getDefaultIcon());
		fillFields();
	}
	private void fillFields() {
		dbPath.setText(DBKernel.prefs.get("PMM_LAB_SETTINGS_DB_PATH", DBKernel.getInternalDefaultDBPath()));
		username.setText(DBKernel.prefs.get("PMM_LAB_SETTINGS_DB_USERNAME", DBKernel.getUsername()));
		password.setText(DBKernel.prefs.get("PMM_LAB_SETTINGS_DB_PASSWORD", DBKernel.getPassword()));
		readOnly.setSelected(DBKernel.prefs.getBoolean("PMM_LAB_SETTINGS_DB_RO", true));
	}

	private void button1ActionPerformed(ActionEvent e) {
		/*
		FileDialog chooser = new FileDialog(this, "Choose folder of database", FileDialog.LOAD);
		chooser.setFilenameFilter(new FolderFilter());
		chooser.setDirectory(dbPath.getText());
		chooser.setVisible(true);
		String folderSelected = chooser.getDirectory();
	    File folder = new File(folderSelected) ;
	    if(folder.exists() && folder.isDirectory()) {
	    	dbPath.setText(folder.getAbsolutePath());
	    }	    
	    else {
	    	MyLogger.handleMessage("No Selection ");
	    }
		*/
	    JFileChooser chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File(dbPath.getText()));
	    chooser.setDialogTitle("Choose folder of database");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
	    	dbPath.setText(chooser.getSelectedFile().getAbsolutePath());
	      }
	    else {
	    	MyLogger.handleMessage("No Selection ");
	    }

	}

	private void okButtonActionPerformed(ActionEvent e) {
		String dbt = dbPath.getText();
		boolean isServer = DBKernel.isHsqlServer(dbt); 
		if (!isServer && !dbt.endsWith(System.getProperty("file.separator"))) {
			dbt += System.getProperty("file.separator");
		}
		if (hasChanged(dbt, username.getText(), String.valueOf(password.getPassword()), readOnly.isSelected())) {
			DBKernel.prefs.put("PMM_LAB_SETTINGS_DB_PATH", dbt);
			DBKernel.prefs.put("PMM_LAB_SETTINGS_DB_USERNAME", username.getText());
			DBKernel.prefs.put("PMM_LAB_SETTINGS_DB_PASSWORD", String.valueOf(password.getPassword()));
			DBKernel.prefs.putBoolean("PMM_LAB_SETTINGS_DB_RO", readOnly.isSelected());
			DBKernel.prefs.prefsFlush();
			DBKernel.closeDBConnections(true);
			
			try {
				Bfrdb db = new Bfrdb(dbt + (isServer ? "" : "DB"), username.getText(), String.valueOf(password.getPassword()));
				Connection conn = db.getConnection();//DBKernel.getLocalConn(true);
				DBKernel.setLocalConn(conn, dbt, username.getText(), String.valueOf(password.getPassword()));
				if (!isServer) DBKernel.getUP(dbt);
				conn = DBKernel.getLocalConn(true);
				if (conn != null) {
					DBKernel.createGui(conn);
			  		DBKernel.mainFrame.getMyList().getMyDBTable().initConn(conn);
					DBKernel.mainFrame.getMyList().getMyDBTable().setTable();
					//DBKernel.myList.setSelection("Matrices");
					//DBKernel.myList.setSelection(DBKernel.prefs.get("LAST_SELECTED_TABLE", "Versuchsbedingungen"));
	        		CategoryReader.killInstance();
				}
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		this.dispose();
	}
	private boolean hasChanged(String dbt, String username, String password, boolean isRO) {
		return !DBKernel.prefs.get("PMM_LAB_SETTINGS_DB_PATH", "").equals(dbt) || !DBKernel.prefs.get("PMM_LAB_SETTINGS_DB_USERNAME", "").equals(username)
				|| !DBKernel.prefs.get("PMM_LAB_SETTINGS_DB_PASSWORD", "").equals(password)
				|| DBKernel.prefs.getBoolean("PMM_LAB_SETTINGS_DB_RO", true) != isRO;
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		this.dispose();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		dbPath = new JTextField();
		button1 = new JButton();
		label3 = new JLabel();
		username = new JTextField();
		label4 = new JLabel();
		password = new JPasswordField();
		label2 = new JLabel();
		readOnly = new JCheckBox();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();

		//======== this ========
		setTitle("Settings");
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG);
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(
					"2*(default, $lcgap), default",
					"3*(default, $lgap), default"));

				//---- label1 ----
				label1.setText("DB Path:");
				contentPanel.add(label1, CC.xy(1, 1));

				//---- dbPath ----
				dbPath.setColumns(50);
				contentPanel.add(dbPath, CC.xy(3, 1));

				//---- button1 ----
				button1.setText("...");
				button1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						button1ActionPerformed(e);
					}
				});
				contentPanel.add(button1, CC.xy(5, 1));

				//---- label3 ----
				label3.setText("Username:");
				contentPanel.add(label3, CC.xy(1, 3));
				contentPanel.add(username, CC.xywh(3, 3, 3, 1));

				//---- label4 ----
				label4.setText("Password:");
				contentPanel.add(label4, CC.xy(1, 5));
				contentPanel.add(password, CC.xywh(3, 5, 3, 1));

				//---- label2 ----
				label2.setText("DB Read-only:");
				label2.setVisible(false);
				contentPanel.add(label2, CC.xy(1, 7));

				//---- readOnly ----
				readOnly.setVisible(false);
				contentPanel.add(readOnly, CC.xywh(3, 7, 3, 1));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_PAD);
				buttonBar.setLayout(new FormLayout(
					"$glue, $button, $rgap, $button",
					"pref"));

				//---- okButton ----
				okButton.setText("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				buttonBar.add(okButton, CC.xy(2, 1));

				//---- cancelButton ----
				cancelButton.setText("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				buttonBar.add(cancelButton, CC.xy(4, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JTextField dbPath;
	private JButton button1;
	private JLabel label3;
	private JTextField username;
	private JLabel label4;
	private JPasswordField password;
	private JLabel label2;
	private JCheckBox readOnly;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	class FolderFilter implements FilenameFilter {
	    public boolean accept(File dir, String name) {
	    	File f = new File(dir.getAbsolutePath() + File.separator + name);
	    	System.err.println(name + "\t" + (f.exists() && f.isDirectory()));
	        return f.exists() && f.isDirectory();
	    }
	}
}
