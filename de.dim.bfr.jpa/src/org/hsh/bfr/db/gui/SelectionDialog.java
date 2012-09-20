package org.hsh.bfr.db.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.gui.actions.VisibilityAction;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class SelectionDialog extends JDialog {
	MyList myList;
	public SelectionDialog(MyList myList) {
		this.myList = myList;
		initComponents();						
	}

	private void okButtonActionPerformed(ActionEvent e) {
		this.dispose();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("org.hsh.bfr.db.gui.PanelProps");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		buttonBar = new JPanel();
		okButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(bundle.getString("SelectionDialog.this.title"));
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
				DefaultMutableTreeNode dmt = (DefaultMutableTreeNode) myList.getModel().getRoot();
				checkboxes = new JCheckBox[dmt.getChildCount()];
				String rowSpec = "pref, 3dlu, pref, 3dlu";
				for (int i=0; i<checkboxes.length; i++) {
					rowSpec += ",pref, 3dlu";
				}
				contentPanel.setLayout(new FormLayout("left:pref, 6dlu, 50dlu, 4dlu, default", // columns 
		         rowSpec)); 

				//---- label1 ----
				//label1.setText("Auswahl der Tabellen: ");
				//contentPanel.add(label1, cc.xy(1,1)); //cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL));

							
				for (int i=0; i<checkboxes.length; i++) {
					checkboxes[i]= new JCheckBox(dmt.getChildAt(i).toString());			
					checkboxes[i].setSelected(DBKernel.prefs.getBoolean("VIS_NODE_" + checkboxes[i].getText(), true));										
					contentPanel.add(checkboxes[i], cc.xy(1, 5+i*2));
				}
				
				VisibilityAction[] va = new VisibilityAction[checkboxes.length] ;
			    for (int i=0; i<checkboxes.length; i++)
			    {	checkboxes[i].setText(((DefaultMutableTreeNode)myList.getModel().getRoot()).getChildAt(i).toString());
			    	va[i] = new VisibilityAction(checkboxes[i].getText(), null, null, myList);   	
			    	checkboxes[i].setAction(va[i]);	   	
			    }
				
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
				buttonBar.add(okButton, cc.xy(2, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		setSize(425, checkboxes.length*40 );
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JCheckBox[] checkboxes; //JFW
	private JPanel buttonBar;
	private JButton okButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables 
}
