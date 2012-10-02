/*
 * Created by JFormDesigner on Tue Oct 02 09:16:35 CEST 2012
 */

package de.bund.bfr.knime.pmm.manualmodelconf.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import de.bund.bfr.knime.pmm.common.ParametricModel;

/**
 * @author Armin Weiser
 */
public class SecDialog extends JDialog {
	
	private HashMap<String, ParametricModel> m_secondaryModels;
	private String m_depVar;
	private MMC_M m_m2;
	
	public SecDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public SecDialog(Dialog owner) {
		super(owner);
		initComponents();
	}
	
	public void setPanel(final MMC_M m2, final String depVar, final HashMap<String, ParametricModel> secondaryModels) {
		m_m2 = m2;
		m_secondaryModels = secondaryModels;
		m_depVar = depVar;
		contentPanel.add(m2, CC.xy(1, 1));
	}

	private void okButtonActionPerformed(ActionEvent e) {
		ParametricModel pm = m_m2.getPM();
		pm.setDepVar(m_depVar);
		m_secondaryModels.put(m_depVar, pm);
		this.dispose();
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		this.dispose();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();

		//======== this ========
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
					"fill:default:grow"));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(
					"$glue, $button, $rgap, $button",
					"pref"));
				((FormLayout)buttonBar.getLayout()).setColumnGroups(new int[][] {{2, 4}});

				//---- okButton ----
				okButton.setText("OK");
				okButton.setPreferredSize(new Dimension(90, 25));
				okButton.setMaximumSize(new Dimension(90, 25));
				okButton.setMinimumSize(new Dimension(90, 25));
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				buttonBar.add(okButton, CC.xy(2, 1));

				//---- cancelButton ----
				cancelButton.setText("Cancel");
				cancelButton.setPreferredSize(new Dimension(90, 25));
				cancelButton.setMinimumSize(new Dimension(90, 25));
				cancelButton.setMaximumSize(new Dimension(90, 25));
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
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
