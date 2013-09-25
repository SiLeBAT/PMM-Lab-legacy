/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
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
 * Created by JFormDesigner on Wed Jan 12 17:11:40 CET 2011
 */

package org.hsh.bfr.db.gui.dbtable.editoren;

import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.*;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;

import com.jgoodies.forms.layout.*;

/**
 * @author Armin Weiser
 */
public class CB_ConditionsEditor extends JDialog {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyDBTable myDBTable;
	private int selectedColumn;
	private static Hashtable<String, String> cbDefs = null;
	
	public CB_ConditionsEditor(MyDBTable myDBTable, int selectedColumn) {
		this.myDBTable = myDBTable;
		this.selectedColumn = selectedColumn;
		initComponents();
		this.setIconImage(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Database.gif")).getImage()); // SiLeBAT.gif
		Object value = myDBTable.getValueAt(myDBTable.getSelectedRow(), selectedColumn);
		readValues(value);
	}

	private void thisKeyTyped(KeyEvent e) {
	  	char ch = e.getKeyChar();
	  	if (ch == KeyEvent.VK_ENTER) {
	  		saveNdispose();
		}
	  	else if (ch == KeyEvent.VK_ESCAPE) {
	  		dispose();	
	  	}
	  	else {
		  	if (e.getSource() instanceof JTextField) {
		  		JTextField tf = (JTextField) e.getSource();
		  		if (tf != null) { 
		  			if (!DBKernel.isDouble(tf.getText() + ch)) e.consume();
		  		}
		  	}	  		
	  	}
	}

	private void button1ActionPerformed(ActionEvent e) {
		saveNdispose();
	}
	private void saveNdispose() {
  		saveValues();	
		dispose();	
	}

	private void button2ActionPerformed(ActionEvent e) {
		dispose();	
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		scrollPane1 = new JScrollPane();
		panel1 = new JPanel();
		label5 = new JLabel();
		textField5 = new JTextField();
		label6 = new JLabel();
		label72 = new JLabel();
		label70 = new JLabel();
		textField70 = new JTextField();
		label71 = new JLabel();
		label7 = new JLabel();
		textField7 = new JTextField();
		label9 = new JLabel();
		label38 = new JLabel();
		checkBox38 = new JCheckBox();
		label8 = new JLabel();
		checkBox8 = new JCheckBox();
		label39 = new JLabel();
		checkBox39 = new JCheckBox();
		label10 = new JLabel();
		textField10 = new JTextField();
		label13 = new JLabel();
		label40 = new JLabel();
		textField40 = new JTextField();
		label42 = new JLabel();
		label11 = new JLabel();
		textField11 = new JTextField();
		label14 = new JLabel();
		label41 = new JLabel();
		textField41 = new JTextField();
		label43 = new JLabel();
		label12 = new JLabel();
		textField12 = new JTextField();
		label15 = new JLabel();
		label44 = new JLabel();
		textField44 = new JTextField();
		label45 = new JLabel();
		comboBox1 = new JComboBox<String>();
		label46 = new JLabel();
		textField46 = new JTextField();
		label47 = new JLabel();
		label18 = new JLabel();
		checkBox18 = new JCheckBox();
		label2 = new JLabel();
		textField2 = new JTextField();
		label48 = new JLabel();
		label19 = new JLabel();
		checkBox19 = new JCheckBox();
		label49 = new JLabel();
		textField49 = new JTextField();
		label50 = new JLabel();
		label20 = new JLabel();
		checkBox20 = new JCheckBox();
		label51 = new JLabel();
		textField51 = new JTextField();
		label52 = new JLabel();
		label21 = new JLabel();
		textField21 = new JTextField();
		label22 = new JLabel();
		label53 = new JLabel();
		textField53 = new JTextField();
		label54 = new JLabel();
		label23 = new JLabel();
		textField23 = new JTextField();
		label24 = new JLabel();
		label55 = new JLabel();
		textField55 = new JTextField();
		label57 = new JLabel();
		label3 = new JLabel();
		textField3 = new JTextField();
		label25 = new JLabel();
		label56 = new JLabel();
		checkBox56 = new JCheckBox();
		label4 = new JLabel();
		checkBox4 = new JCheckBox();
		label58 = new JLabel();
		checkBox58 = new JCheckBox();
		label26 = new JLabel();
		textField26 = new JTextField();
		label29 = new JLabel();
		label59 = new JLabel();
		checkBox59 = new JCheckBox();
		label27 = new JLabel();
		textField27 = new JTextField();
		label30 = new JLabel();
		label60 = new JLabel();
		textField60 = new JTextField();
		label61 = new JLabel();
		label28 = new JLabel();
		textField28 = new JTextField();
		label31 = new JLabel();
		label62 = new JLabel();
		checkBox62 = new JCheckBox();
		label1 = new JLabel();
		textField1 = new JTextField();
		label32 = new JLabel();
		label63 = new JLabel();
		textField63 = new JTextField();
		label65 = new JLabel();
		label33 = new JLabel();
		checkBox33 = new JCheckBox();
		label64 = new JLabel();
		textField64 = new JTextField();
		label66 = new JLabel();
		label34 = new JLabel();
		textField34 = new JTextField();
		label36 = new JLabel();
		label67 = new JLabel();
		checkBox67 = new JCheckBox();
		label35 = new JLabel();
		textField35 = new JTextField();
		label37 = new JLabel();
		label68 = new JLabel();
		textField68 = new JTextField();
		label69 = new JLabel();
		label75 = new JLabel();
		textField75 = new JTextField();
		label16 = new JLabel();
		textField4 = new JTextField();
		textField6 = new JTextField();
		textField8 = new JTextField();
		label76 = new JLabel();
		textField76 = new JTextField();
		label73 = new JLabel();
		button1 = new JButton();
		button2 = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setAlwaysOnTop(true);
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"center:default:grow",
			"default:grow"));

		//======== scrollPane1 ========
		{

			//======== panel1 ========
			{
				panel1.setLayout(new FormLayout(
					"67dlu, 3*($lcgap, default), $lcgap, 87dlu, 2*($lcgap, default)",
					"24*(default, $lgap), default"));
				((FormLayout)panel1.getLayout()).setColumnGroups(new int[][] {{1, 9}, {3, 11}, {5, 13}});
				((FormLayout)panel1.getLayout()).setRowGroups(new int[][] {{1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, 41, 43, 45, 47, 49}});

				//---- label5 ----
				label5.setText("ALTA");
				label5.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label5, cc.xy(1, 1));

				//---- textField5 ----
				textField5.setPreferredSize(new Dimension(120, 20));
				textField5.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField5, cc.xy(3, 1));

				//---- label6 ----
				label6.setText("%");
				panel1.add(label6, cc.xy(5, 1));

				//---- label72 ----
				label72.setBackground(new Color(102, 102, 102));
				label72.setOpaque(true);
				panel1.add(label72, cc.xywh(7, 1, 1, 43));

				//---- label70 ----
				label70.setText("lactic_acid");
				label70.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label70, cc.xy(9, 1));

				//---- textField70 ----
				textField70.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField70, cc.xy(11, 1));

				//---- label71 ----
				label71.setText("ppm");
				panel1.add(label71, cc.xy(13, 1));

				//---- label7 ----
				label7.setText("acetic_acid");
				label7.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label7, cc.xy(1, 3));

				//---- textField7 ----
				textField7.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField7, cc.xy(3, 3));

				//---- label9 ----
				label9.setText("ppm");
				panel1.add(label9, cc.xy(5, 3));

				//---- label38 ----
				label38.setText("lactic_bacteria_fermented");
				label38.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label38, cc.xy(9, 3));

				//---- checkBox38 ----
				checkBox38.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(checkBox38, cc.xy(11, 3));

				//---- label8 ----
				label8.setText("anaerobic");
				label8.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label8, cc.xy(1, 5));

				//---- checkBox8 ----
				checkBox8.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(checkBox8, cc.xy(3, 5));

				//---- label39 ----
				label39.setText("Modified_Atmosphere");
				label39.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label39, cc.xy(9, 5));

				//---- checkBox39 ----
				checkBox39.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(checkBox39, cc.xy(11, 5));

				//---- label10 ----
				label10.setText("ascorbic_acid");
				label10.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label10, cc.xy(1, 7));

				//---- textField10 ----
				textField10.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField10, cc.xy(3, 7));

				//---- label13 ----
				label13.setText("ppm");
				panel1.add(label13, cc.xy(5, 7));

				//---- label40 ----
				label40.setText("malic_acid");
				label40.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label40, cc.xy(9, 7));

				//---- textField40 ----
				textField40.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField40, cc.xy(11, 7));

				//---- label42 ----
				label42.setText("ppm");
				panel1.add(label42, cc.xy(13, 7));

				//---- label11 ----
				label11.setText("benzoic_acid");
				label11.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label11, cc.xy(1, 9));

				//---- textField11 ----
				textField11.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField11, cc.xy(3, 9));

				//---- label14 ----
				label14.setText("ppm");
				panel1.add(label14, cc.xy(5, 9));

				//---- label41 ----
				label41.setText("moisture");
				label41.setHorizontalAlignment(SwingConstants.TRAILING);
				label41.setToolTipText("Luftfeuchtigkeit");
				panel1.add(label41, cc.xy(9, 9));

				//---- textField41 ----
				textField41.setToolTipText("Luftfeuchtigkeit");
				textField41.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField41, cc.xy(11, 9));

				//---- label43 ----
				label43.setText("%");
				label43.setToolTipText("Luftfeuchtigkeit");
				panel1.add(label43, cc.xy(13, 9));

				//---- label12 ----
				label12.setText("citric_acid");
				label12.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label12, cc.xy(1, 11));

				//---- textField12 ----
				textField12.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField12, cc.xy(3, 11));

				//---- label15 ----
				label15.setText("ppm");
				panel1.add(label15, cc.xy(5, 11));

				//---- label44 ----
				label44.setText("monolaurin");
				label44.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label44, cc.xy(9, 11));

				//---- textField44 ----
				textField44.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField44, cc.xy(11, 11));

				//---- label45 ----
				label45.setText("ppm");
				panel1.add(label45, cc.xy(13, 11));

				//---- comboBox1 ----
				comboBox1.setModel(new DefaultComboBoxModel<String>(new String[] {
					"ppm",
					"%",
					"IU/ml",
					"mM",
					"\u00b5g/ml",
					"kGy",
					"kGy/h",
					"g/l"
				}));
				panel1.add(comboBox1, cc.xy(5, 13));

				//---- label46 ----
				label46.setText("N2");
				label46.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label46, cc.xy(9, 13));

				//---- textField46 ----
				textField46.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField46, cc.xy(11, 13));

				//---- label47 ----
				label47.setText("%");
				panel1.add(label47, cc.xy(13, 13));

				//---- label18 ----
				label18.setText("competition");
				label18.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label18, cc.xy(1, 15));

				//---- checkBox18 ----
				checkBox18.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(checkBox18, cc.xy(3, 15));

				//---- label2 ----
				label2.setText("NaCl");
				label2.setHorizontalAlignment(SwingConstants.TRAILING);
				label2.setToolTipText("Kochsalzgehalt");
				panel1.add(label2, cc.xy(9, 15));

				//---- textField2 ----
				textField2.setToolTipText("Kochsalzgehalt");
				textField2.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField2, cc.xy(11, 15));

				//---- label48 ----
				label48.setText("%");
				label48.setToolTipText("Kochsalzgehalt");
				panel1.add(label48, cc.xy(13, 15));

				//---- label19 ----
				label19.setText("cut");
				label19.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label19, cc.xy(1, 17));

				//---- checkBox19 ----
				checkBox19.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(checkBox19, cc.xy(3, 17));

				//---- label49 ----
				label49.setText("nisin");
				label49.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label49, cc.xy(9, 17));

				//---- textField49 ----
				textField49.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField49, cc.xy(11, 17));

				//---- label50 ----
				label50.setText("IU/ml");
				panel1.add(label50, cc.xy(13, 17));

				//---- label20 ----
				label20.setText("dried");
				label20.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label20, cc.xy(1, 19));

				//---- checkBox20 ----
				checkBox20.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(checkBox20, cc.xy(3, 19));

				//---- label51 ----
				label51.setText("nitrite");
				label51.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label51, cc.xy(9, 19));

				//---- textField51 ----
				textField51.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField51, cc.xy(11, 19));

				//---- label52 ----
				label52.setText("ppm");
				panel1.add(label52, cc.xy(13, 19));

				//---- label21 ----
				label21.setText("EDTA");
				label21.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label21, cc.xy(1, 21));

				//---- textField21 ----
				textField21.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField21, cc.xy(3, 21));

				//---- label22 ----
				label22.setText("ppm");
				panel1.add(label22, cc.xy(5, 21));

				//---- label53 ----
				label53.setText("O2");
				label53.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label53, cc.xy(9, 21));

				//---- textField53 ----
				textField53.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField53, cc.xy(11, 21));

				//---- label54 ----
				label54.setText("%");
				panel1.add(label54, cc.xy(13, 21));

				//---- label23 ----
				label23.setText("ethanol");
				label23.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label23, cc.xy(1, 23));

				//---- textField23 ----
				textField23.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField23, cc.xy(3, 23));

				//---- label24 ----
				label24.setText("%");
				panel1.add(label24, cc.xy(5, 23));

				//---- label55 ----
				label55.setText("propionic_acid");
				label55.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label55, cc.xy(9, 23));

				//---- textField55 ----
				textField55.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField55, cc.xy(11, 23));

				//---- label57 ----
				label57.setText("ppm");
				panel1.add(label57, cc.xy(13, 23));

				//---- label3 ----
				label3.setText("fat");
				label3.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label3, cc.xy(1, 25));

				//---- textField3 ----
				textField3.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField3, cc.xy(3, 25));

				//---- label25 ----
				label25.setText("%");
				panel1.add(label25, cc.xy(5, 25));

				//---- label56 ----
				label56.setText("raw");
				label56.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label56, cc.xy(9, 25));

				//---- checkBox56 ----
				checkBox56.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(checkBox56, cc.xy(11, 25));

				//---- label4 ----
				label4.setText("frozen");
				label4.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label4, cc.xy(1, 27));

				//---- checkBox4 ----
				checkBox4.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(checkBox4, cc.xy(3, 27));

				//---- label58 ----
				label58.setText("shaken");
				label58.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label58, cc.xy(9, 27));

				//---- checkBox58 ----
				checkBox58.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(checkBox58, cc.xy(11, 27));

				//---- label26 ----
				label26.setText("fructose");
				label26.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label26, cc.xy(1, 29));

				//---- textField26 ----
				textField26.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField26, cc.xy(3, 29));

				//---- label29 ----
				label29.setText("%");
				panel1.add(label29, cc.xy(5, 29));

				//---- label59 ----
				label59.setText("smoked");
				label59.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label59, cc.xy(9, 29));

				//---- checkBox59 ----
				checkBox59.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(checkBox59, cc.xy(11, 29));

				//---- label27 ----
				label27.setText("glucose");
				label27.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label27, cc.xy(1, 31));

				//---- textField27 ----
				textField27.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField27, cc.xy(3, 31));

				//---- label30 ----
				label30.setText("%");
				panel1.add(label30, cc.xy(5, 31));

				//---- label60 ----
				label60.setText("sorbic_acid");
				label60.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label60, cc.xy(9, 31));

				//---- textField60 ----
				textField60.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField60, cc.xy(11, 31));

				//---- label61 ----
				label61.setText("ppm");
				panel1.add(label61, cc.xy(13, 31));

				//---- label28 ----
				label28.setText("glycerol");
				label28.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label28, cc.xy(1, 33));

				//---- textField28 ----
				textField28.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField28, cc.xy(3, 33));

				//---- label31 ----
				label31.setText("%");
				panel1.add(label31, cc.xy(5, 33));

				//---- label62 ----
				label62.setText("sterile");
				label62.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label62, cc.xy(9, 33));

				//---- checkBox62 ----
				checkBox62.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(checkBox62, cc.xy(11, 33));

				//---- label1 ----
				label1.setText("HCl");
				label1.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label1, cc.xy(1, 35));

				//---- textField1 ----
				textField1.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField1, cc.xy(3, 35));

				//---- label32 ----
				label32.setText("g/l");
				panel1.add(label32, cc.xy(5, 35));

				//---- label63 ----
				label63.setText("sucrose");
				label63.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label63, cc.xy(9, 35));

				//---- textField63 ----
				textField63.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField63, cc.xy(11, 35));

				//---- label65 ----
				label65.setText("%");
				panel1.add(label65, cc.xy(13, 35));

				//---- label33 ----
				label33.setText("heated");
				label33.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label33, cc.xy(1, 37));

				//---- checkBox33 ----
				checkBox33.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(checkBox33, cc.xy(3, 37));

				//---- label64 ----
				label64.setText("sugar");
				label64.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label64, cc.xy(9, 37));

				//---- textField64 ----
				textField64.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField64, cc.xy(11, 37));

				//---- label66 ----
				label66.setText("%");
				panel1.add(label66, cc.xy(13, 37));

				//---- label34 ----
				label34.setText("irradiated");
				label34.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label34, cc.xy(1, 39));

				//---- textField34 ----
				textField34.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField34, cc.xy(3, 39));

				//---- label36 ----
				label36.setText("kGy");
				panel1.add(label36, cc.xy(5, 39));

				//---- label67 ----
				label67.setText("vacuum");
				label67.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label67, cc.xy(9, 39));

				//---- checkBox67 ----
				checkBox67.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(checkBox67, cc.xy(11, 39));

				//---- label35 ----
				label35.setText("irradiation");
				label35.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label35, cc.xy(1, 41));

				//---- textField35 ----
				textField35.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField35, cc.xy(3, 41));

				//---- label37 ----
				label37.setText("kGy/h");
				panel1.add(label37, cc.xy(5, 41));

				//---- label68 ----
				label68.setText("oregano");
				label68.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label68, cc.xy(9, 41));

				//---- textField68 ----
				textField68.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField68, cc.xy(11, 41));

				//---- label69 ----
				label69.setText("%");
				panel1.add(label69, cc.xy(13, 41));

				//---- label75 ----
				label75.setText("silver-zinc zeolite");
				label75.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label75, cc.xy(1, 43));

				//---- textField75 ----
				textField75.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField75, cc.xy(3, 43));

				//---- label16 ----
				label16.setText("%");
				panel1.add(label16, cc.xy(5, 43));
				panel1.add(textField4, cc.xy(9, 43));

				//---- textField6 ----
				textField6.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						thisKeyTyped(e);
					}
				});
				panel1.add(textField6, cc.xy(11, 43));
				panel1.add(textField8, cc.xy(13, 43));

				//---- label76 ----
				label76.setText("Gewichtsverlust");
				label76.setHorizontalAlignment(SwingConstants.TRAILING);
				panel1.add(label76, cc.xy(1, 45));
				panel1.add(textField76, cc.xy(3, 45));

				//---- label73 ----
				label73.setText("%");
				panel1.add(label73, cc.xy(5, 45));

				//---- button1 ----
				button1.setText("OK");
				button1.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						button1ActionPerformed(e);
					}
				});
				panel1.add(button1, cc.xy(3, 49));

				//---- button2 ----
				button2.setText("Abbrechen");
				button2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						button2ActionPerformed(e);
					}
				});
				panel1.add(button2, cc.xy(11, 49));
			}
			scrollPane1.setViewportView(panel1);
		}
		contentPane.add(scrollPane1, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.FILL));
		setSize(715, 730);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JScrollPane scrollPane1;
	private JPanel panel1;
	private JLabel label5;
	private JTextField textField5;
	private JLabel label6;
	private JLabel label72;
	private JLabel label70;
	private JTextField textField70;
	private JLabel label71;
	private JLabel label7;
	private JTextField textField7;
	private JLabel label9;
	private JLabel label38;
	private JCheckBox checkBox38;
	private JLabel label8;
	private JCheckBox checkBox8;
	private JLabel label39;
	private JCheckBox checkBox39;
	private JLabel label10;
	private JTextField textField10;
	private JLabel label13;
	private JLabel label40;
	private JTextField textField40;
	private JLabel label42;
	private JLabel label11;
	private JTextField textField11;
	private JLabel label14;
	private JLabel label41;
	private JTextField textField41;
	private JLabel label43;
	private JLabel label12;
	private JTextField textField12;
	private JLabel label15;
	private JLabel label44;
	private JTextField textField44;
	private JLabel label45;
	private JComboBox<String> comboBox1;
	private JLabel label46;
	private JTextField textField46;
	private JLabel label47;
	private JLabel label18;
	private JCheckBox checkBox18;
	private JLabel label2;
	private JTextField textField2;
	private JLabel label48;
	private JLabel label19;
	private JCheckBox checkBox19;
	private JLabel label49;
	private JTextField textField49;
	private JLabel label50;
	private JLabel label20;
	private JCheckBox checkBox20;
	private JLabel label51;
	private JTextField textField51;
	private JLabel label52;
	private JLabel label21;
	private JTextField textField21;
	private JLabel label22;
	private JLabel label53;
	private JTextField textField53;
	private JLabel label54;
	private JLabel label23;
	private JTextField textField23;
	private JLabel label24;
	private JLabel label55;
	private JTextField textField55;
	private JLabel label57;
	private JLabel label3;
	private JTextField textField3;
	private JLabel label25;
	private JLabel label56;
	private JCheckBox checkBox56;
	private JLabel label4;
	private JCheckBox checkBox4;
	private JLabel label58;
	private JCheckBox checkBox58;
	private JLabel label26;
	private JTextField textField26;
	private JLabel label29;
	private JLabel label59;
	private JCheckBox checkBox59;
	private JLabel label27;
	private JTextField textField27;
	private JLabel label30;
	private JLabel label60;
	private JTextField textField60;
	private JLabel label61;
	private JLabel label28;
	private JTextField textField28;
	private JLabel label31;
	private JLabel label62;
	private JCheckBox checkBox62;
	private JLabel label1;
	private JTextField textField1;
	private JLabel label32;
	private JLabel label63;
	private JTextField textField63;
	private JLabel label65;
	private JLabel label33;
	private JCheckBox checkBox33;
	private JLabel label64;
	private JTextField textField64;
	private JLabel label66;
	private JLabel label34;
	private JTextField textField34;
	private JLabel label36;
	private JLabel label67;
	private JCheckBox checkBox67;
	private JLabel label35;
	private JTextField textField35;
	private JLabel label37;
	private JLabel label68;
	private JTextField textField68;
	private JLabel label69;
	private JLabel label75;
	private JTextField textField75;
	private JLabel label16;
	private JTextField textField4;
	private JTextField textField6;
	private JTextField textField8;
	private JLabel label76;
	private JTextField textField76;
	private JLabel label73;
	private JButton button1;
	private JButton button2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	private void readValues(Object value) {
		String fieldVal = (value == null ? "" : value.toString());
		
		setValues(fieldVal, label5, textField5);
		setValues(fieldVal, label7, textField7);
		setValues(fieldVal, label8, checkBox8);
		setValues(fieldVal, label10, textField10);
		setValues(fieldVal, label11, textField11);
		setValues(fieldVal, label12, textField12);
		//setValues(fieldVal, label16, textField16);
		setValues(fieldVal, label18, checkBox18);
		setValues(fieldVal, label19, checkBox19);
		setValues(fieldVal, label20, checkBox20);
		setValues(fieldVal, label21, textField21);
		setValues(fieldVal, label23, textField23);
		setValues(fieldVal, label3, textField3);
		setValues(fieldVal, label4, checkBox4);
		setValues(fieldVal, label26, textField26);
		setValues(fieldVal, label27, textField27);
		setValues(fieldVal, label28, textField28);
		setValues(fieldVal, label1, textField1);
		setValues(fieldVal, label33, checkBox33);
		setValues(fieldVal, label34, textField34);
		setValues(fieldVal, label35, textField35);
		setValues(fieldVal, label70, textField70);
		setValues(fieldVal, label38, checkBox38);
		setValues(fieldVal, label39, checkBox39);
		setValues(fieldVal, label40, textField40);
		setValues(fieldVal, label41, textField41);
		setValues(fieldVal, label44, textField44);
		setValues(fieldVal, label46, textField46);
		setValues(fieldVal, label2, textField2);
		setValues(fieldVal, label49, textField49);
		setValues(fieldVal, label51, textField51);
		setValues(fieldVal, label53, textField53);
		setValues(fieldVal, label55, textField55);
		setValues(fieldVal, label56, checkBox56);
		setValues(fieldVal, label58, checkBox58);
		setValues(fieldVal, label59, checkBox59);
		setValues(fieldVal, label60, textField60);
		setValues(fieldVal, label62, checkBox62);
		setValues(fieldVal, label63, textField63);
		setValues(fieldVal, label64, textField64);
		setValues(fieldVal, label67, checkBox67);
		setValues(fieldVal, label68, textField68);
		setValues(fieldVal, label75, textField75);
		setValues(fieldVal, textField4, textField6, textField8);
	}
	private void saveValues() {
		String result = "";

		result += getValues(label5, textField5);
		result += getValues(label7, textField7);
		result += getValues(label8, checkBox8);
		result += getValues(label10, textField10);
		result += getValues(label11, textField11);
		result += getValues(label12, textField12);
		//result += getValues(label16, textField16);
		result += getValues(label18, checkBox18);
		result += getValues(label19, checkBox19);
		result += getValues(label20, checkBox20);
		result += getValues(label21, textField21);
		result += getValues(label23, textField23);
		result += getValues(label3, textField3);
		result += getValues(label4, checkBox4);
		result += getValues(label26, textField26);
		result += getValues(label27, textField27);
		result += getValues(label28, textField28);
		result += getValues(label1, textField1);
		result += getValues(label33, checkBox33);
		result += getValues(label34, textField34);
		result += getValues(label35, textField35);
		result += getValues(label70, textField70);
		result += getValues(label38, checkBox38);
		result += getValues(label39, checkBox39);
		result += getValues(label40, textField40);
		result += getValues(label41, textField41);
		result += getValues(label44, textField44);
		result += getValues(label46, textField46);
		result += getValues(label2, textField2);
		result += getValues(label49, textField49);
		result += getValues(label51, textField51);
		result += getValues(label53, textField53);
		result += getValues(label55, textField55);
		result += getValues(label56, checkBox56);
		result += getValues(label58, checkBox58);
		result += getValues(label59, checkBox59);
		result += getValues(label60, textField60);
		result += getValues(label62, checkBox62);
		result += getValues(label63, textField63);
		result += getValues(label64, textField64);
		result += getValues(label67, checkBox67);
		result += getValues(label68, textField68);
		result += getValues(label75, textField75);
		result += getValues(new JLabel("ud:"+textField4.getText()), textField6, textField8.getText());
		
		myDBTable.setValueAt(result, myDBTable.getSelectedRow(), selectedColumn);    		  
	}
	private void setValues(String fieldVal, JTextField jl, JTextField tf, JTextField tfu) {
		int index;
		if ((index = fieldVal.toLowerCase().indexOf("ud:")) >= 0) {
			int index2;
			String val = "";
			if ((index2 = fieldVal.toLowerCase().indexOf("\n", index)) >= 0) {
				val = fieldVal.toLowerCase().substring(index + 3, index2);
			}
			else {
				val = fieldVal.toLowerCase().substring(index + 3);				
			}
			if ((index = val.indexOf("=")) >= 0) {
				index2 = val.indexOf(";;", index);
				jl.setText(val.substring(0, index));
				if (index2 > index) {
					tf.setText(val.substring(index + 1, index2));
					tfu.setText(val.substring(index2 + 2));
				}
				else {
					tf.setText(val.substring(index + 1));
					tfu.setText("");
				}
			}
			else tf.setText(""); 
		}
		else tf.setText(""); 
	}
	private void setValues(String fieldVal, JLabel jl, JTextField tf) {
		int index;
		if ((index = fieldVal.toLowerCase().indexOf(jl.getText().toLowerCase() + "=")) >= 0) tf.setText(fieldVal.substring(index + jl.getText().length() + 1, fieldVal.indexOf("\n", index)));
		else tf.setText(""); 
	}
	private void setValues(String fieldVal, JLabel jl, JCheckBox cb) {
		if (fieldVal.toLowerCase().indexOf(jl.getText().toLowerCase() + "=") >= 0) cb.setSelected(true);
		else cb.setSelected(false);
	}
	private String getValues(JLabel jl, JTextField tf) {
		return getValues(jl, tf, null);
	}
	private String getValues(JLabel jl, JTextField tf, String unit) {
		String result = "";
		if (tf.getText().trim().length() > 0) {
			result += jl.getText() + "=" + tf.getText() + (unit != null ? ";;" + unit : "") + "\n";
		}
		return result;
	}
	private String getValues(JLabel jl, JCheckBox cb) {
		String result = "";
		if (cb.isSelected()) result += jl.getText() + "=TRUE\n";
		return result;
	}
	public static String getMyStringFromDB(String dbCond, int tableID, String tablename) {
		String cbCond = "";
		StringTokenizer strtok = new StringTokenizer(dbCond);
		while (strtok.hasMoreTokens()) {
			String val = strtok.nextToken();
			if (val.endsWith("=TRUE")) {
				if (val.startsWith("smoked")) cbCond += "smoked food, ";
				else if (val.startsWith("vacuum")) cbCond += "vacuum-packed, ";
				else System.err.println(val);
			}
			else if (val.startsWith("moisture")) { // moisture=80
				cbCond += "moisture in the environment(%):" + val.substring(val.indexOf("=") + 1) + ", ";
			}
			else if (val.startsWith("irradiated")) { // irradiated=0
				cbCond += "in an environment that has been irradiated(kGy):" + val.substring(val.indexOf("=") + 1) + ", ";
			}
			else if (val.startsWith("irradiation")) { // irradiated=10
				cbCond += "irradiation at constant rate during the observation time(kGy/h):" + val.substring(val.indexOf("=") + 1) + ", ";
			}
			else if (val.startsWith("nisin")) { // nisin=0
				cbCond += "nisin in the environment(IU/ml):" + val.substring(val.indexOf("=") + 1) + ", ";
			}
			else if (val.startsWith("nitrite")) { // nitrite=50
				cbCond += "sodium or potassium nitrite in the environment(ppm):" + val.substring(val.indexOf("=") + 1) + ", ";
			}
			else if (val.startsWith("fat")) { // fat=14.2
				cbCond += "fat in the environment(%):" + val.substring(val.indexOf("=") + 1) + ", ";
			}
			else if (val.startsWith("glucose")) { // glucose=0.5
				cbCond += "glucose in the environment(%):" + val.substring(val.indexOf("=") + 1) + ", ";
			}
			else if (val.startsWith("NaCl")) { // NaCl=3.6
				cbCond += "sodium chloride in the environment(%):" + val.substring(val.indexOf("=") + 1) + ", ";
			}
			else {
				System.err.println(val);
			}
		}
		if (cbCond.length() > 0) cbCond = cbCond.substring(0, cbCond.length() - 2);
		return getMyString(cbCond, tableID, tablename);
	}
	public static String getMyString(String cbCond, int tableID, String tablename) {
		String result = "";
		try {
			PreparedStatement ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL(tablename + "_Sonstiges") +
					" (" + DBKernel.delimitL(tablename) + "," + DBKernel.delimitL("SonstigeParameter") + "," +
					DBKernel.delimitL("Einheit") + "," + DBKernel.delimitL("Ja_Nein") + ") VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			PreparedStatement ps1 = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Einheiten") +
					" (" + DBKernel.delimitL("Einheit") + "," + DBKernel.delimitL("Beschreibung") + ") VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);

			String cbCondMod = cbCond.trim().toLowerCase().replaceAll("\\s+", " "); // doppelte Spaces weg usw.
			String cbCondModOrig = cbCond.trim().replaceAll("\\s+", " "); // doppelte Spaces weg usw.
			if (cbCondMod.length() > 0) {
				if (cbDefs == null) loadCBDefs();
				int numFound = 0;
				Enumeration<String> e = cbDefs.keys();
				while (e.hasMoreElements()) {
					String key = e.nextElement().toString();
					int indexA;
					if ((indexA = cbCondMod.indexOf(key)) >= 0) {
						numFound++;
						numFound += key.replaceAll("[^,]","").length();
						String val = cbDefs.get(key);
						if (val.length() > 0) {
							Integer paramID = DBKernel.getID("SonstigeParameter", "Parameter", val);
							if (paramID != null) {
								if (tableID > 0) {
									ps.setInt(1, tableID);
									ps.setInt(2, paramID);
								}
								int len = key.length();
								String strTok = cbCondMod.substring(indexA + len);
								if (strTok.indexOf(',') >= 0) strTok = strTok.substring(0, strTok.indexOf(','));
								int index1 = strTok.indexOf(':');
								int index2 = strTok.indexOf('(');
								if (index2 >= 0) {
									String unit = strTok.substring(index2 + 1, strTok.indexOf(')'));
									if (tableID > 0) {
										String unitOrig = cbCondModOrig.substring(indexA + len);
										if (unitOrig.indexOf(',') >= 0) unitOrig = unitOrig.substring(0, unitOrig.indexOf(','));
										unitOrig = unitOrig.substring(index2 + 1, unitOrig.indexOf(')'));
										Integer unitID = DBKernel.getID("Einheiten", "Einheit", unitOrig);
										if (unitID == null) {
											System.out.println("neue Unit: " + unitOrig);
											ps1.setString(1, unitOrig);
											ps1.setString(2, unitOrig);
											if (ps1.executeUpdate() > 0) {
												unitID = DBKernel.getLastInsertedID(ps1);
											}
										}
										if (unitID != null) ps.setInt(3, unitID); else ps.setNull(3, java.sql.Types.INTEGER);
									}
									else {
										if (strTok.indexOf("ppm") < 0 && strTok.indexOf("%") < 0
												 && strTok.indexOf("kgy") < 0 && strTok.indexOf("iu/ml") < 0 && strTok.indexOf("g/l") < 0)
											System.err.println("Hopala: wasn hier fürne Einheit aus der Combase?\t" + unit + "\t" + strTok + "\t" + cbCond); // es gibt noch: µg/ml, mg/ml, logKill, iu/g, pm (=ppm?!)										
									}
								}
								else if (tableID > 0) ps.setNull(3, java.sql.Types.INTEGER);
								if (tableID > 0) {
									Double wert = null;
									if (index2 < 0 && index1 < 0) ps.setBoolean(4, true);
									else {
										ps.setNull(4, java.sql.Types.BOOLEAN);
										if (index1 >= 0) wert = Double.parseDouble(strTok.substring(index1 + 1).trim());
										else System.err.println("Hopala: Wert ohne Einheit (oder umgekehrt?) in der Combase?\t" + strTok + "\t" + cbCond);
									}
									try {
										//System.err.println(versuchsbedingungenID);
								    	DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL(tablename + "_Sonstiges") +
								    			" WHERE " + DBKernel.delimitL(tablename) + "=" + tableID +
								    			" AND " + DBKernel.delimitL("SonstigeParameter") + "=" + paramID, false);
										if (ps.executeUpdate() > 0) {
												Integer lastID = DBKernel.getLastInsertedID(ps);
												if (lastID != null) {
													if (wert != null) {
														Object kzID = DBKernel.insertDBL(tablename + "_Sonstiges", "Wert", lastID, null, "Wert", wert);
														if (kzID != null) DBKernel.insertDBL(tablename + "_Sonstiges", "Wert", lastID, kzID, "Wert_typ", 1);
													}	
												}
										}
										else {
											System.err.println("ps.executeUpdate() > 0?" + ps.toString());
										}
									}
									catch (Exception e2) {MyLogger.handleException(e2);System.err.println(ps.toString());}
										
								}
								else {
									if (index2 < 0 && index1 < 0) {
										if (isBoolean(val)) result += val + "=TRUE\n";
										else System.err.println("Hopala: Non-Boolean Eintrag ohne Wert in der Combase?\t" + strTok + "\t" + cbCond);
									}
									else if (index2 < 0) {
										result += val + "=" + strTok.substring(index1 + 1).trim() + "\n";
									}
									else if (index1 < 0) {
										System.err.println("Hopala: Wert ohne Einheit (oder umgekehrt?) in der Combase?\t" + strTok + "\t" + cbCond);
									}
									else {
										result += val + "=" + strTok.substring(index1 + 1).trim() + "\n";						
									}
								}
							}
						}
						while ((indexA = cbCondMod.indexOf(key, indexA + 1)) >= 0) numFound++; // es gibt doch tatsächlich doppelte Einträge
					}
				}
				int numberOfCommas = cbCond.replaceAll("[^,]","").length();
				if (numFound < numberOfCommas + 1) {
					System.err.println("Hopala: wasn hier fürn Key aus der Combase?\t" + cbCond + "\t" + cbCondMod);
				}
			}
		}
		catch (Exception e1) {
			MyLogger.handleException(e1);
		}
		return result;
	}
	private static void loadCBDefs() {
		cbDefs = new Hashtable<String, String>();
		cbDefs.put("alta fermentation product in the environment", "ALTA");
		cbDefs.put("acetic acid (possibly as salt) in the environment", "acetic_acid");
		cbDefs.put("anaerobic environment", "anaerobic"); // BOOLEAN
		cbDefs.put("ascorbic acid (possibly as salt) in the environment", "ascorbic_acid");
		cbDefs.put("benzoic acid (possibly as salt) in the environment", "benzoic_acid");
		cbDefs.put("citric acid (possibly as salt) in the environment", "citric_acid");
		cbDefs.put("carbon-dioxide in the environment", "CO_2");
		cbDefs.put("other species in the environment", "competition"); // BOOLEAN
		cbDefs.put("cut (minced, chopped etc)", "cut"); // BOOLEAN
		cbDefs.put("cut (minced, chopped, ground, etc)", "cut"); // BOOLEAN
		cbDefs.put("dried food", "dried"); // BOOLEAN
		cbDefs.put("ethylenenediaminetetraacetic acid in the environment", "EDTA");
		cbDefs.put("ethanol in the environment", "ethanol");
		cbDefs.put("fat in the environment", "fat");
		cbDefs.put("frozen food", "frozen");
		cbDefs.put("fructose in the environment", "fructose");
		cbDefs.put("glucose in the environment", "glucose");
		cbDefs.put("glycerol in the environment", "glycerol");
		cbDefs.put("hydrochloric acid in the environment", "HCl");
		cbDefs.put("inoculation in/on previously heated (cooked, baked, pasteurized, etc) but not sterilised food/medium", "heated"); // BOOLEAN
		cbDefs.put("in an environment that has been irradiated", "irradiated");
		cbDefs.put("irradiation at constant rate during the obs. time", "irradiation");
		cbDefs.put("irradiation at constant rate during the observation time", "irradiation");
		cbDefs.put("lactic acid (possibly as salt) in the environment", "lactic_acid");
		cbDefs.put("food fermented by lactic acid bacteria", "lactic_bacteria_fermented"); // BOOLEAN
		cbDefs.put("modified atmosphere environment", "Modified_Atmosphere"); // BOOLEAN
		cbDefs.put("malic acid in the environment", "malic_acid");
		cbDefs.put("moisture in the environment", "moisture");
		cbDefs.put("glycerol monolaurate (emulsifier) in the environment", "monolaurin");
		cbDefs.put("nitrogen in the environment", "N_2");
		cbDefs.put("sodium chloride in the environment", "NaCl");
		cbDefs.put("nisin in the environment", "nisin");
		cbDefs.put("sodium or potassium nitrite in the environment", "nitrite");
		cbDefs.put("oxygen (aerobic conditions) in the environment", "O_2");
		cbDefs.put("propionic acid (possibly as salt) in the environment", "propionic_acid");
		cbDefs.put("raw", "raw"); // BOOLEAN
		cbDefs.put("shaken (agitated, stirred)", "shaken"); // BOOLEAN
		cbDefs.put("smoked food", "smoked"); // BOOLEAN
		cbDefs.put("sorbic acid (possibly as salt) in the environment", "sorbic_acid");
		cbDefs.put("sterilised before inoculation", "sterile"); // BOOLEAN
		cbDefs.put("sucrose in the environment", "sucrose");
		cbDefs.put("sugar in the environment", "sugar");
		cbDefs.put("vacuum-packed", "vacuum"); // BOOLEAN
		cbDefs.put("oregano essential oil in the environment", "oregano");
		
		cbDefs.put("other species also deliberately inoculated (but only one organism counted here)", "");
		cbDefs.put("with the indigenous flora in the environment (but not counted)", "");
		cbDefs.put("pressure controlled", "");
		cbDefs.put("in presence of diacetic acid (possibly as salt)", "");
		cbDefs.put("in presence of betaine", "");
	}
	private static boolean isBoolean(String key) {
		return key.equals("vacuum") || key.equals("sterile") || key.equals("smoked") || key.equals("shaken") ||
		key.equals("raw") || key.equals("Modified_Atmosphere") || key.equals("lactic_bacteria_fermented") || key.equals("heated") ||
		key.equals("frozen") || key.equals("dried") || key.equals("cut") || key.equals("competition") || key.equals("anaerobic");
	}
}
