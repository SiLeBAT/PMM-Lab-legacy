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
 * Created by JFormDesigner on Fri Feb 11 14:27:53 CET 2011
 */

package org.hsh.bfr.db.gui.dbtable.editoren;

import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.*;
import com.jgoodies.forms.factories.*;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;

import com.jgoodies.forms.layout.*;

/**
 * @author Armin Weiser
 */
public class MyDoubleEditor extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyDBTable myDBTable;
	private boolean inited = false;
	private String spaltenName = "";
	
	public MyDoubleEditor(MyDBTable myDBTable, int selectedColumn, int x, int y, char ch) {
		this.myDBTable = myDBTable;
		spaltenName = myDBTable.getActualTable().getFieldNames()[selectedColumn-1];
		initComponents();
		this.setTitle(spaltenName);
		//int w = this.getPreferredSize().width;
		//int h = this.getPreferredSize().height;
		int w = this.getSize().width;
		int h = this.getSize().height;
		
		int newX = x - w / 2;
		int newY = y - h / 2;
		if (newX < 0) newX = 0;
		if (newY < 0) newY = 0;
	    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	    if (newX + w > screen.width) newX = screen.width - w;
	    if (newY + h > screen.height) newY = screen.height - h;
	  //System.out.println(newX + "\t" + newY + "\t" + x + "\t" + y + "\t" + w + "\t" + h + "\t" + this.getSize().width + "\t" + this.getSize().height);
		this.setLocation(newX, newY);
		this.setIconImage(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Database.gif")).getImage()); //  SiLeBAT.gif
		readValues();
		if (DBKernel.isDouble(""+ch)) {
			textField1.setText(""+ch);
			inited = true;
		}
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
		  		if (tf != null && tf != textField10) { // textField10 ist die Verteilung
		  			String text = tf.getText();
		  			int cp = tf.getCaretPosition();
		  			text = text.substring(0, cp) + ch + text.substring(cp);
		  			if (!DBKernel.isDouble(text)) e.consume();
			  		//System.out.println(tf.getCaretPosition() + "\t" + (tf == textField10) + "\t" + DBKernel.isDouble(text) + "\t" + text);	  			
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

	private void textField1FocusGained(FocusEvent e) {
		if (inited) {
			textField1.select(0, 0);
			textField1.setCaretPosition(textField1.getText().length());
			inited = false;
		}
	}

	private void button11ActionPerformed(ActionEvent e) {
		openCD(button11, "Zeit (h)");
	}

	private void button12ActionPerformed(ActionEvent e) {
		openCD(button12, comboBox12.getSelectedItem().toString());
	}
	private void openCD(JButton bt, String xAxis) {
		
        MyChartDialog mcd = new MyChartDialog(this, bt.getToolTipText(), xAxis, spaltenName);
        mcd.setModal(true);
        //mcd.pack();
        mcd.setVisible(true);
        bt.setToolTipText(mcd.getDatenpunkte());
		
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		label111 = new JLabel();
		label1 = new JLabel();
		textField1 = new JTextField();
		checkBox1 = new JCheckBox();
		label9 = new JLabel();
		textField9 = new JTextField();
		checkBox9 = new JCheckBox();
		label2 = new JLabel();
		textField2 = new JTextField();
		checkBox2 = new JCheckBox();
		label6 = new JLabel();
		textField6 = new JTextField();
		checkBox6 = new JCheckBox();
		label3 = new JLabel();
		textField3 = new JTextField();
		checkBox3 = new JCheckBox();
		label4 = new JLabel();
		textField4 = new JTextField();
		checkBox4 = new JCheckBox();
		label5 = new JLabel();
		textField5 = new JTextField();
		checkBox5 = new JCheckBox();
		label7 = new JLabel();
		textField7 = new JTextField();
		checkBox7 = new JCheckBox();
		label8 = new JLabel();
		textField8 = new JTextField();
		checkBox8 = new JCheckBox();
		label10 = new JLabel();
		textField10 = new JTextField();
		checkBox10 = new JCheckBox();
		label11 = new JLabel();
		button11 = new JButton();
		checkBox11 = new JCheckBox();
		label12 = new JLabel();
		button12 = new JButton();
		comboBox12 = new JComboBox();
		label13 = new JLabel();
		checkBox13 = new JCheckBox();
		panel1 = new JPanel();
		button1 = new JButton();
		button2 = new JButton();

		//======== this ========
		setAlwaysOnTop(true);
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			"default, $lcgap, default:grow, $lcgap, center:default",
			"14*(default, $lgap), default"));

		//---- label111 ----
		label111.setText("gesch\u00e4tzt");
		contentPane.add(label111, CC.xy(5, 1));

		//---- label1 ----
		label1.setText("Einzelwert");
		contentPane.add(label1, CC.xy(1, 3));

		//---- textField1 ----
		textField1.setPreferredSize(new Dimension(80, 20));
		textField1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		textField1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				textField1FocusGained(e);
			}
		});
		contentPane.add(textField1, CC.xy(3, 3));

		//---- checkBox1 ----
		checkBox1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(checkBox1, CC.xy(5, 3));

		//---- label9 ----
		label9.setText("Wiederholungen");
		label9.setToolTipText("Anzahl der Wiederholungsmessungen/technischen Replikate f\u00fcr diesen Wert");
		contentPane.add(label9, CC.xy(1, 5));

		//---- textField9 ----
		textField9.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(textField9, CC.xy(3, 5));

		//---- checkBox9 ----
		checkBox9.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(checkBox9, CC.xy(5, 5));

		//---- label2 ----
		label2.setText("Mittelwert");
		contentPane.add(label2, CC.xy(1, 7));

		//---- textField2 ----
		textField2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(textField2, CC.xy(3, 7));

		//---- checkBox2 ----
		checkBox2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(checkBox2, CC.xy(5, 7));

		//---- label6 ----
		label6.setText("Standardabweichung");
		label6.setToolTipText("Standardabweichung des gemessenen Wertes - Eintrag nur bei Mehrfachmessungen m\u00f6glich");
		contentPane.add(label6, CC.xy(1, 9));

		//---- textField6 ----
		textField6.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(textField6, CC.xy(3, 9));

		//---- checkBox6 ----
		checkBox6.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(checkBox6, CC.xy(5, 9));

		//---- label3 ----
		label3.setText("Median");
		contentPane.add(label3, CC.xy(1, 11));

		//---- textField3 ----
		textField3.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(textField3, CC.xy(3, 11));

		//---- checkBox3 ----
		checkBox3.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(checkBox3, CC.xy(5, 11));

		//---- label4 ----
		label4.setText("Minimum");
		contentPane.add(label4, CC.xy(1, 13));

		//---- textField4 ----
		textField4.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(textField4, CC.xy(3, 13));

		//---- checkBox4 ----
		checkBox4.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(checkBox4, CC.xy(5, 13));

		//---- label5 ----
		label5.setText("Maximum");
		contentPane.add(label5, CC.xy(1, 15));

		//---- textField5 ----
		textField5.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(textField5, CC.xy(3, 15));

		//---- checkBox5 ----
		checkBox5.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(checkBox5, CC.xy(5, 15));

		//---- label7 ----
		label7.setText("LCL95");
		label7.setToolTipText("Untere Konfidenzgrenze des gemessenen Wertes (95%-KI) - Eintrag nur bei Mehrfachmessungen m\u00f6glich");
		contentPane.add(label7, CC.xy(1, 17));

		//---- textField7 ----
		textField7.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(textField7, CC.xy(3, 17));

		//---- checkBox7 ----
		checkBox7.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(checkBox7, CC.xy(5, 17));

		//---- label8 ----
		label8.setText("UCL95");
		label8.setToolTipText("Obere Konfidenzgrenze des gemessenen Wertes (95%-KI) - Eintrag nur bei Mehrfachmessungen m\u00f6glich");
		contentPane.add(label8, CC.xy(1, 19));

		//---- textField8 ----
		textField8.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(textField8, CC.xy(3, 19));

		//---- checkBox8 ----
		checkBox8.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(checkBox8, CC.xy(5, 19));

		//---- label10 ----
		label10.setText("Verteilung");
		label10.setToolTipText("Verteilung der Werte bei Mehrfachmessungen, z.B. Normalverteilung. Anzugeben ist die entsprechende Funktion in R, z.B. rnorm(n, mean = 0, sd = 1) ");
		contentPane.add(label10, CC.xy(1, 21));

		//---- textField10 ----
		textField10.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(textField10, CC.xy(3, 21));

		//---- checkBox10 ----
		checkBox10.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				thisKeyTyped(e);
			}
		});
		contentPane.add(checkBox10, CC.xy(5, 21));

		//---- label11 ----
		label11.setText("Funktion (Zeit)");
		label11.setToolTipText("\"Parameter\"/Zeit-Profil. Funktion des Parameters in Abh\u00e4ngigkeit von der Zeit.");
		contentPane.add(label11, CC.xy(1, 23));

		//---- button11 ----
		button11.setText("Starte Editor");
		button11.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button11ActionPerformed(e);
			}
		});
		contentPane.add(button11, CC.xy(3, 23));
		contentPane.add(checkBox11, CC.xy(5, 23));

		//---- label12 ----
		label12.setText("Funktion (?)");
		label12.setToolTipText("\"Parameter\"/?-Profil. Funktion des Parameters in Abh\u00e4ngigkeit des anzugebenden ?-Parameters.");
		contentPane.add(label12, CC.xy(1, 25));

		//---- button12 ----
		button12.setText("Starte Editor");
		button12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button12ActionPerformed(e);
			}
		});
		contentPane.add(button12, CC.xy(3, 25));

		//---- comboBox12 ----
		comboBox12.setModel(new DefaultComboBoxModel(new String[] {
			"\u00b0C",
			"pH",
			"aw",
			"CO2",
			"Druck"
		}));
		contentPane.add(comboBox12, CC.xy(5, 25, CC.FILL, CC.DEFAULT));

		//---- label13 ----
		label13.setText("Undefiniert (n.d.)");
		contentPane.add(label13, CC.xy(1, 27));
		contentPane.add(checkBox13, CC.xy(3, 27));

		//======== panel1 ========
		{
			panel1.setLayout(new FormLayout(
				"2*(default:grow, $lcgap), default:grow",
				"default"));
			((FormLayout)panel1.getLayout()).setColumnGroups(new int[][] {{1, 3, 5}});

			//---- button1 ----
			button1.setText("OK");
			button1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					button1ActionPerformed(e);
				}
			});
			panel1.add(button1, CC.xy(3, 1));

			//---- button2 ----
			button2.setText("Abbrechen");
			button2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					button2ActionPerformed(e);
				}
			});
			panel1.add(button2, CC.xy(5, 1));
		}
		contentPane.add(panel1, CC.xywh(1, 29, 5, 1));
		setSize(325, 435);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel label111;
	private JLabel label1;
	private JTextField textField1;
	private JCheckBox checkBox1;
	private JLabel label9;
	private JTextField textField9;
	private JCheckBox checkBox9;
	private JLabel label2;
	private JTextField textField2;
	private JCheckBox checkBox2;
	private JLabel label6;
	private JTextField textField6;
	private JCheckBox checkBox6;
	private JLabel label3;
	private JTextField textField3;
	private JCheckBox checkBox3;
	private JLabel label4;
	private JTextField textField4;
	private JCheckBox checkBox4;
	private JLabel label5;
	private JTextField textField5;
	private JCheckBox checkBox5;
	private JLabel label7;
	private JTextField textField7;
	private JCheckBox checkBox7;
	private JLabel label8;
	private JTextField textField8;
	private JCheckBox checkBox8;
	private JLabel label10;
	private JTextField textField10;
	private JCheckBox checkBox10;
	private JLabel label11;
	private JButton button11;
	private JCheckBox checkBox11;
	private JLabel label12;
	private JButton button12;
	private JComboBox comboBox12;
	private JLabel label13;
	private JCheckBox checkBox13;
	private JPanel panel1;
	private JButton button1;
	private JButton button2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables


	private void readValues() {
		textField1.setText(""); checkBox1.setSelected(false);
		textField2.setText(""); checkBox2.setSelected(false);
		textField3.setText(""); checkBox3.setSelected(false);
		textField4.setText(""); checkBox4.setSelected(false);
		textField5.setText(""); checkBox5.setSelected(false);
		textField6.setText(""); checkBox6.setSelected(false);
		textField7.setText(""); checkBox7.setSelected(false);
		textField8.setText(""); checkBox8.setSelected(false);
		textField9.setText(""); checkBox9.setSelected(false);
		textField10.setText(""); checkBox10.setSelected(false);
		button11.setToolTipText(""); checkBox11.setSelected(false);
		button12.setToolTipText(""); comboBox12.setSelectedIndex(0);
		checkBox13.setSelected(false);
		String sql = "SELECT " + DBKernel.delimitL("Kennzahl") + "," + DBKernel.delimitL("Wert") + "," + DBKernel.delimitL("StrWert") +
		"," + DBKernel.delimitL("geschätzt") + "," + DBKernel.delimitL("Bezugsgröße") +
			" FROM " + DBKernel.delimitL("Kennzahlen") +
			" WHERE " + DBKernel.delimitL("Tabelle") + "='" + myDBTable.getActualTable().getTablename() + "'" +
			" AND " + DBKernel.delimitL("TabellenID") + "=" + myDBTable.getValueAt(myDBTable.getSelectedRow(), 0) +
			" AND " + DBKernel.delimitL("Spaltenname") + "='" + spaltenName + "'";
		try {
			ResultSet rs = DBKernel.getResultSet(sql, false);
			if (rs.first()) {
				do {					
					if (readValue(rs, label1, textField1, checkBox1));
					else if (readValue(rs, label2, textField2, checkBox2));
					else if (readValue(rs, label3, textField3, checkBox3));
					else if (readValue(rs, label4, textField4, checkBox4));
					else if (readValue(rs, label5, textField5, checkBox5));
					else if (readValue(rs, label6, textField6, checkBox6));
					else if (readValue(rs, label7, textField7, checkBox7));
					else if (readValue(rs, label8, textField8, checkBox8));
					else if (readValue(rs, label9, textField9, checkBox9));
					else if (readValue(rs, label10, textField10, checkBox10, true, null, null));
					else if (readValue(rs, label11, button11, checkBox11, null));
					else if (readValue(rs, label12, button12, null, comboBox12));
					else if (readValue(rs, label13, null, checkBox13, true, null, null));
				} while (rs.next());					
			}
		}
		catch (Exception e) {
			MyLogger.handleException(e);
		}
	}
	private boolean readValue(ResultSet rs, JLabel jl, JButton bt, JCheckBox cb, JComboBox combo) {
		return readValue(rs, jl, null, cb, true, bt, combo);
	}
	private boolean readValue(ResultSet rs, JLabel jl, JTextField tf, JCheckBox cb) {
		return readValue(rs, jl, tf, cb, false, null, null);
	}
	private boolean readValue(ResultSet rs, JLabel jl, JTextField tf, JCheckBox cb, boolean isString, JButton bt, JComboBox combo) {
		boolean result = false;
		try {
			String kz = rs.getString("Kennzahl");
			if (kz.equals(jl.getText())) {
				boolean ndSet = false;
				if (isString) {
					String strVal = rs.getString("StrWert") == null ? "" : rs.getString("StrWert");
					if (tf != null) tf.setText(strVal);
					else if (bt != null) bt.setToolTipText(strVal);
					else if (strVal.equals("n.d.")) ndSet = true;
				}
				else {
					Double dbl = rs.getDouble("Wert");
					NumberFormat f = NumberFormat.getInstance(Locale.US);
					f.setGroupingUsed(false);
					String refinedNumber = f.format(dbl);
					tf.setText(refinedNumber);					
				}
				if (cb != null) {
					boolean ass = rs.getString("geschätzt") == null ? false : rs.getBoolean("geschätzt");
					cb.setSelected(ndSet || ass);					
				}
				if (combo != null) {
					String comboVal = rs.getString("Bezugsgröße") == null ? "" : rs.getString("Bezugsgröße");
					for (int i=0;i<combo.getItemCount();i++) {
						if (combo.getItemAt(i).equals(comboVal)) {
							combo.setSelectedIndex(i);
							break;
						}
					}						
				}
				result = true;
			}			
		}
		catch (Exception e) {MyLogger.handleException(e);}
		return result;
	}

	private void saveValues() {
		try {
			String sql = "DELETE FROM " + DBKernel.delimitL("Kennzahlen") + " WHERE " + DBKernel.delimitL("Tabelle") + "='" + myDBTable.getActualTable().getTablename() + "'" +
			" AND " + DBKernel.delimitL("TabellenID") + "=" + myDBTable.getValueAt(myDBTable.getSelectedRow(), 0) +
			" AND " + DBKernel.delimitL("Spaltenname") + "='" + spaltenName + "'";
			DBKernel.sendRequest(sql, false);
			PreparedStatement ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Kennzahlen") +
		      		" (" + DBKernel.delimitL("Tabelle") + "," + DBKernel.delimitL("TabellenID") + "," + DBKernel.delimitL("Spaltenname") + "," +
		      				DBKernel.delimitL("Kennzahl") + ", " + DBKernel.delimitL("Wert") + ", " + DBKernel.delimitL("StrWert") + ", " +
		      				DBKernel.delimitL("geschätzt") + "," + DBKernel.delimitL("Bezugsgröße") +
		      		") VALUES (?,?,?,?,?,?,?,?)");

			saveValue(ps, label1, textField1.getText(), checkBox1);
			saveValue(ps, label2, textField2.getText(), checkBox2);
			saveValue(ps, label3, textField3.getText(), checkBox3);
			saveValue(ps, label4, textField4.getText(), checkBox4);
			saveValue(ps, label5, textField5.getText(), checkBox5);
			saveValue(ps, label6, textField6.getText(), checkBox6);
			saveValue(ps, label7, textField7.getText(), checkBox7);
			saveValue(ps, label8, textField8.getText(), checkBox8);
			saveValue(ps, label9, textField9.getText(), checkBox9);
			saveValue(ps, label10, textField10.getText(), checkBox10, true);
			saveValue(ps, label11, button11.getToolTipText(), checkBox11, true);
			saveValue(ps, label12, button12.getToolTipText(), null, true, comboBox12);
			if (checkBox13.isSelected()) saveValue(ps, label13, "n.d.", null, true, null);
		}
		catch (Exception e) {MyLogger.handleException(e);}
	}
	private void saveValue(PreparedStatement ps, JLabel jl, String tfText, JCheckBox cb) {
		saveValue(ps, jl, tfText, cb, false);
	}
	private void saveValue(PreparedStatement ps, JLabel jl, String tfText, JCheckBox cb, boolean isString) {
		saveValue(ps, jl, tfText, cb, isString, null);
	}
	private void saveValue(PreparedStatement ps, JLabel jl, String tfText, JCheckBox cb, boolean isString, JComboBox combo) {
		try {
			if (tfText.trim().length() > 0) {
				try {
					String tn = myDBTable.getActualTable().getTablename();
					Integer id = (Integer) myDBTable.getValueAt(myDBTable.getSelectedRow(), 0);
					ps.setString(1, tn);					        	  
					ps.setInt(2, id);					        	  
					ps.setString(3, spaltenName);	
					ps.setString(4, jl.getText());					        	  										        	  
					if (isString) {
						ps.setNull(5, java.sql.Types.DOUBLE);
						ps.setString(6, tfText);
					}	
					else {
						try {
							double dbl = Double.parseDouble(tfText);
							ps.setDouble(5, dbl);
							ps.setNull(6, java.sql.Types.VARCHAR);					
						}
						catch (Exception ed) {MyLogger.handleException(ed); ps.setNull(5, java.sql.Types.DOUBLE);ps.setNull(6, java.sql.Types.VARCHAR);}
					}
					ps.setBoolean(7, cb == null ? false : cb.isSelected());							
					if (combo != null) ps.setString(8, combo.getSelectedItem().toString());
					else ps.setNull(8, java.sql.Types.VARCHAR);
					ps.execute();									
				}
				catch (Exception ed) {MyLogger.handleException(ed);}
			}		
		}
		catch (Exception e) {MyLogger.handleException(e);}
	}
}
