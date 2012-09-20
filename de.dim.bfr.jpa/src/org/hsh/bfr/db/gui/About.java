/*
 * Created by JFormDesigner on Thu Jul 01 14:24:49 CEST 2010
 */

package org.hsh.bfr.db.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.hsh.bfr.db.DBKernel;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Armin Weiser
 */
public class About extends JDialog {
	public About() {
		initComponents();
		label4.setText("Copyright \u00a9 2010 Armin Weiser.");
		String ver = About.class.getPackage().getImplementationVersion();
		label2.setText("Version " + (ver == null ? DBKernel.DBVersion : ver)); //"TP-100701");
		label4.setVisible(false);
	}

	private void okButtonActionPerformed(ActionEvent e) {
		this.dispose();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("org.hsh.bfr.db.gui.PanelProps");
		DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = compFactory.createLabel("");
		label3 = new JLabel();
		label2 = new JLabel();
		label4 = new JLabel();
		buttonBar = new JPanel();
		okButton = new JButton();

		//======== this ========
		setTitle(bundle.getString("About.this.title"));
		setModal(true);
		setResizable(false);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(
					"default:grow",
					"fill:default:grow, 4*($lgap, default)"));

				//---- label1 ----
				label1.setIcon(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Database.gif")));
				label1.setPreferredSize(new Dimension(180, 170));
				label1.setMaximumSize(new Dimension(255, 240));
				label1.setMinimumSize(new Dimension(128, 120));
				label1.setHorizontalAlignment(SwingConstants.CENTER);
				contentPanel.add(label1, CC.xy(1, 1, CC.FILL, CC.FILL));

				//---- label3 ----
				label3.setText(bundle.getString("About.label3.text"));
				label3.setHorizontalAlignment(SwingConstants.LEFT);
				label3.setFont(new Font("Dotum", Font.BOLD, 20));
				contentPanel.add(label3, CC.xy(1, 3));

				//---- label2 ----
				label2.setText(bundle.getString("About.label2.text"));
				label2.setHorizontalAlignment(SwingConstants.LEFT);
				contentPanel.add(label2, CC.xy(1, 5));

				//---- label4 ----
				label4.setText(bundle.getString("About.label4.text"));
				label4.setHorizontalAlignment(SwingConstants.LEFT);
				contentPanel.add(label4, CC.xy(1, 9));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(
					"$glue, $button",
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
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		setSize(210, 310);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JLabel label3;
	private JLabel label2;
	private JLabel label4;
	private JPanel buttonBar;
	private JButton okButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
