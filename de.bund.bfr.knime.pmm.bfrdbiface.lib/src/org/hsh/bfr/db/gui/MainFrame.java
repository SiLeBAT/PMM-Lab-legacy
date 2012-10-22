/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
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
/*
 * Created by JFormDesigner on Thu Jun 17 07:37:46 CEST 2010
 */

package org.hsh.bfr.db.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.gui.actions.BackupAction;
import org.hsh.bfr.db.gui.actions.BlobAction;
import org.hsh.bfr.db.gui.actions.ChangeLogAction;
import org.hsh.bfr.db.gui.actions.ExportAction;
import org.hsh.bfr.db.gui.actions.FindAction;
import org.hsh.bfr.db.gui.actions.FocusLeft;
import org.hsh.bfr.db.gui.actions.FocusRight;
import org.hsh.bfr.db.gui.actions.ImportAction;
import org.hsh.bfr.db.gui.actions.InfoAction;
import org.hsh.bfr.db.gui.actions.MergeAction;
import org.hsh.bfr.db.gui.actions.PlausibleAction;
import org.hsh.bfr.db.gui.actions.RestoreAction;
import org.hsh.bfr.db.gui.actions.SelectionAction;
import org.hsh.bfr.db.gui.actions.UserAction;
import org.hsh.bfr.db.gui.dbtable.MyDBPanel;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;

import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Armin Weiser
 */
public class MainFrame extends JFrame {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyDBTable myDB;
	
	public MainFrame(final MyList myList) {
		this.myList = myList;
		myDB = myList.getMyDBTable();
		//myDBTree = myList.getMyDBTree();
		this.myDBPanel1 = new MyDBPanel(myDB, myList.getMyDBTree());
		//this.myDBTreePanel = new MyDBTreePanel(myDBTree);
		initComponents();
		addBindings();
		//centerOnScreen(this);
		//this.setExtendedState(this.getExtendedState()|JFrame.MAXIMIZED_BOTH);
	}
	/*
	private void centerOnScreen(JFrame frame) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = frame.getSize().width;
		int h = frame.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		frame.setLocation(x, y);
	}
	*/
  protected void addBindings() {
    InputMap inputMap = toolBar1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = toolBar1.getActionMap();

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), "CTRL+F");
    FindAction finda = new FindAction(myDBPanel1);
    actionMap.put("CTRL+F", finda);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK), "CTRL+Left");
    FocusLeft focusLeft = new FocusLeft();
    actionMap.put("CTRL+Left", focusLeft);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK), "CTRL+Right");
    FocusRight focusRight = new FocusRight(myDB);
    actionMap.put("CTRL+Right", focusRight);

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_DOWN_MASK), "F1");
    ImportAction impa = new ImportAction(button7.getName(), button7.getIcon(), button7.getToolTipText(), progressBar1);
    actionMap.put("F1", impa);
    button7.setAction(impa);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, InputEvent.CTRL_DOWN_MASK), "F2");
    ExportAction expa = new ExportAction(button6.getName(), button6.getIcon(), button6.getToolTipText(), progressBar1, myDB);
    actionMap.put("F2", expa);
    button6.setAction(expa);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.CTRL_DOWN_MASK), "F3");
    UserAction usa = new UserAction(button10.getName(), button10.getIcon(), button10.getToolTipText());
    actionMap.put("F3", usa);
    button10.setAction(usa);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_DOWN_MASK), "F4");
    PlausibleAction pla = new PlausibleAction(button8.getName(), button8.getIcon(), button8.getToolTipText(), progressBar1, myDB);
    actionMap.put("F4", pla);
    button8.setAction(pla);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, InputEvent.CTRL_DOWN_MASK), "F5");
    BlobAction bla = new BlobAction(button5.getName(), button5.getIcon(), button5.getToolTipText());
    actionMap.put("F5", bla);
    button5.setAction(bla);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6, InputEvent.CTRL_DOWN_MASK), "F6");
    ChangeLogAction cla = new ChangeLogAction(button3.getName(), button3.getIcon(), button3.getToolTipText());
    actionMap.put("F6", cla);
    button3.setAction(cla);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, InputEvent.CTRL_DOWN_MASK), "F7");
    MergeAction ma = new MergeAction(button4.getName(), button4.getIcon(), button4.getToolTipText());
    actionMap.put("F7", ma);
    button4.setAction(ma);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F8, InputEvent.CTRL_DOWN_MASK), "F8");
    BackupAction ba = new BackupAction(button1.getName(), button1.getIcon(), button1.getToolTipText());
    actionMap.put("F8", ba);
    button1.setAction(ba);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F9, InputEvent.CTRL_DOWN_MASK), "F9");
    RestoreAction ra = new RestoreAction(button2.getName(), button2.getIcon(), button2.getToolTipText(), myDB);
    actionMap.put("F9", ra);
    button2.setAction(ra);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F10, InputEvent.CTRL_DOWN_MASK), "F10");
    SelectionAction sa = new SelectionAction(button12.getName(), button12.getIcon(), button12.getToolTipText(), myList);
    actionMap.put("F10", sa); 
    button12.setAction(sa);
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F11, InputEvent.CTRL_DOWN_MASK), "F11");
    InfoAction ia = new InfoAction(button9.getName(), button9.getIcon(), button9.getToolTipText());
    actionMap.put("F11", ia);
    button9.setAction(ia);
  }

	public JProgressBar getProgressBar() {
		return progressBar1;
	}
	/*
	public void setRC() { // myDBPanel1 
		setRC(myDBPanel1);
	}
	public void setRC(JComponent comp) { // myDBPanel1 oder MyDBForm
		splitPane1.setRightComponent(comp);
	}
	*/
	public Dimension getRightSize() {
		return splitPane1.getRightComponent().getSize();
	}
	@Override
	public void setVisible(final boolean doVisible) {
		boolean isAdmin = DBKernel.isAdmin();
		boolean isRO = false; 
		try {isRO = DBKernel.getDBConnection().isReadOnly();}
		catch (Exception e) {MyLogger.handleException(e);}
		boolean isEnabable = isAdmin && !isRO;
		button8.getAction().setEnabled(true);
		button10.getAction().setEnabled(isEnabable);
		button1.getAction().setEnabled(!DBKernel.isServerConnection);
		button12.getAction().setEnabled(true);
		button2.getAction().setEnabled(!DBKernel.isServerConnection);
		button3.getAction().setEnabled(isEnabable);
		button5.getAction().setEnabled(isEnabable);
		button7.getAction().setEnabled(!isRO);
		button6.getAction().setEnabled(!isRO);
		button11.setEnabled(isEnabable);
		button4.getAction().setEnabled(isEnabable && DBKernel.debug && !DBKernel.isServerConnection);
		if (isRO) {
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			this.setTitle("OriginalDB ist von anderem Benutzer geöffnet. ReadOnly Modus... " +
					(DBKernel.tempROZeit > 0 ? "Diese Datenbank-Kopie" + " wurde um " + sdf.format(DBKernel.tempROZeit) + " erstellt.": ""));
		}
		super.setVisible(doVisible);
	}

	private void thisWindowClosing(final WindowEvent e) {
	    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
	    	if (myDB != null) {
	    		myDB.checkUnsavedStuff();
	    	}
	    	this.setVisible(false);
	    	if (DBKernel.isKNIME) {
	    		
	    	}
	    	else {
		    	if (!DBKernel.isServerConnection) {
					MyLogger.handleMessage("vor closeDBConnections COMPACT: " + DBKernel.getFileSize(DBKernel.HSHDB_PATH + "DB.data"));
				}
		    	DBKernel.closeDBConnections(true); // lieber false???
		    	if (!DBKernel.isServerConnection) {
					MyLogger.handleMessage("nach closeDBConnections COMPACT: " + DBKernel.getFileSize(DBKernel.HSHDB_PATH + "DB.data"));
				}
		    	this.dispose();
		    	System.exit(0);
	    	}
	    }
	}

	private void button11ActionPerformed(final ActionEvent e) {
		// reset Database:
	    int retVal = JOptionPane.showConfirmDialog(this, "Sind Sie sicher, daß Sie die Datenbank neu initialisieren möchten?", "Reset bestätigen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	    if (retVal == JOptionPane.YES_OPTION) {
	  		this.dispose();
	  		Login login = new Login(true);
	  		login.dropDatabase();
	  		login.setVisible(true);    	
	    }
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("org.hsh.bfr.db.gui.PanelProps_" + DBKernel.lang);
		toolBar1 = new JToolBar();
		button7 = new JButton();
		button6 = new JButton();
		button10 = new JButton();
		button8 = new JButton();
		button5 = new JButton();
		button3 = new JButton();
		button4 = new JButton();
		button1 = new JButton();
		button2 = new JButton();
		button12 = new JButton();
		button9 = new JButton();
		button11 = new JButton();
		progressBar1 = new JProgressBar();
		splitPane1 = new JSplitPane();
		panel2 = new JPanel();
		scrollPane1 = new JScrollPane();
		//myList = new MyList();
		//myDBPanel1 = new MyDBPanel();

		//======== this ========
		setTitle(bundle.getString("MainFrame.this.title"));
		setIconImage(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Database.gif")).getImage());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				thisWindowClosing(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"default:grow",
			"default, default:grow"));

		//======== toolBar1 ========
		{
			toolBar1.setFloatable(false);

			//---- button7 ----
			button7.setToolTipText(bundle.getString("MainFrame.button7.toolTipText"));
			button7.setIcon(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Downloads folder.gif")));
			toolBar1.add(button7);
			toolBar1.addSeparator();

			//---- button6 ----
			button6.setToolTipText(bundle.getString("MainFrame.button6.toolTipText"));
			button6.setIcon(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/3d bar chart.gif")));
			toolBar1.add(button6);
			toolBar1.addSeparator();

			//---- button10 ----
			button10.setToolTipText(bundle.getString("MainFrame.button10.toolTipText"));
			button10.setIcon(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Database.gif")));
			button10.setVisible(false);
			toolBar1.add(button10);
			toolBar1.addSeparator();

			//---- button8 ----
			button8.setToolTipText(bundle.getString("MainFrame.button8.toolTipText"));
			button8.setIcon(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Eye.gif")));
			toolBar1.add(button8);
			toolBar1.addSeparator();

			//---- button5 ----
			button5.setIcon(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Script.gif")));
			button5.setToolTipText(bundle.getString("MainFrame.button5.toolTipText"));
			button5.setVisible(false);
			toolBar1.add(button5);

			//---- button3 ----
			button3.setToolTipText(bundle.getString("MainFrame.button3.toolTipText"));
			button3.setIcon(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/View.gif")));
			button3.setVisible(false);
			toolBar1.add(button3);
			toolBar1.addSeparator();

			//---- button4 ----
			button4.setToolTipText(bundle.getString("MainFrame.button4.toolTipText"));
			button4.setIcon(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Filter.gif")));
			button4.setAlignmentX(1.0F);
			toolBar1.add(button4);
			toolBar1.addSeparator();

			//---- button1 ----
			button1.setToolTipText(bundle.getString("MainFrame.button1.toolTipText"));
			button1.setIcon(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Record.gif")));
			toolBar1.add(button1);

			//---- button2 ----
			button2.setToolTipText(bundle.getString("MainFrame.button2.toolTipText"));
			button2.setIcon(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Target.gif")));
			toolBar1.add(button2);

			//---- button12 ----
			button12.setToolTipText(bundle.getString("MainFrame.button12.toolTipText"));
			button12.setIcon(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Find.gif")));
			toolBar1.add(button12);
			toolBar1.addSeparator();

			//---- button9 ----
			button9.setToolTipText(bundle.getString("MainFrame.button9.toolTipText"));
			button9.setIcon(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Info.gif")));
			toolBar1.add(button9);

			//---- button11 ----
			button11.setText(bundle.getString("MainFrame.button11.text"));
			button11.setVisible(false);
			button11.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					button11ActionPerformed(e);
				}
			});
			toolBar1.add(button11);

			//---- progressBar1 ----
			progressBar1.setVisible(false);
			toolBar1.add(progressBar1);
		}
		contentPane.add(toolBar1, CC.xy(1, 1));

		//======== splitPane1 ========
		{
			splitPane1.setDividerLocation(300);

			//======== panel2 ========
			{
				panel2.setLayout(new FormLayout(
					"default:grow",
					"default, $lgap, fill:default:grow"));

				//======== scrollPane1 ========
				{
					scrollPane1.setViewportView(myList);
				}
				panel2.add(scrollPane1, CC.xywh(1, 1, 1, 3));
			}
			splitPane1.setLeftComponent(panel2);
			splitPane1.setRightComponent(myDBPanel1);
		}
		contentPane.add(splitPane1, CC.xy(1, 2, CC.DEFAULT, CC.FILL));
		setSize(1020, 700);
		setLocationRelativeTo(null);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JToolBar toolBar1;
	private JButton button7;
	private JButton button6;
	private JButton button10;
	private JButton button8;
	private JButton button5;
	private JButton button3;
	private JButton button4;
	private JButton button1;
	private JButton button2;
	private JButton button12;
	private JButton button9;
	private JButton button11;
	private JProgressBar progressBar1;
	private JSplitPane splitPane1;
	private JPanel panel2;
	private JScrollPane scrollPane1;
	private MyList myList;
	private MyDBPanel myDBPanel1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
}
