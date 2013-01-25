/*
 * Created by JFormDesigner on Thu Dec 13 22:23:43 CET 2012
 */

package de.bund.bfr.knime.pmm.timeseriesreader;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.LinkedHashMap;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyTable;
import org.knime.core.node.InvalidSettingsException;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;

/**
 * @author Armin Weiser
 */
public class MdReaderUi extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4203454706269875728L;
	private LinkedHashMap<String, DoubleTextField[]> params;
	private String[] itemListMisc;
	private String deselectedItem = "";
	
	private Connection conn;

	public MdReaderUi(Connection conn) {
		initComponents();
		this.conn = conn;
	}
	public MdReaderUi(Connection conn, String[] itemListMisc) {
		initComponents();
		this.conn = conn;
		this.itemListMisc = itemListMisc;
		handleParams();
	}

	private void handleParams() {
		DoubleTextField[] dtf;
		if (params == null || params.size() == 0) {
			params = new LinkedHashMap<String, DoubleTextField[]>();
			dtf = new DoubleTextField[2]; dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
			params.put(AttributeUtilities.ATT_TEMPERATURE, dtf);
			dtf = new DoubleTextField[2]; dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
			params.put(AttributeUtilities.ATT_PH, dtf);
			dtf = new DoubleTextField[2]; dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
			params.put(AttributeUtilities.ATT_WATERACTIVITY, dtf);
			dtf = new DoubleTextField[2]; dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
			params.put("other1", dtf); // raw
			dtf = new DoubleTextField[2]; dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
			params.put("other2", dtf); // HCl
			dtf = new DoubleTextField[2]; dtf[0] = new DoubleTextField(true); dtf[1] = new DoubleTextField(true);
			params.put("other3", dtf); // irradiation
		}
		
		panel4.setVisible(false);
		panel4.removeAll();

		panel4.setBorder(new TitledBorder("Parameters"));
		panel4.setLayout(new FormLayout(
			"default:grow, 2*($lcgap, default)",
			params.size() + "*(default, $lgap), default"));

		//---- label4 ----
		label4.setText("Name");
		label4.setHorizontalAlignment(SwingConstants.CENTER);
		panel4.add(label4, CC.xy(1, 1));

		//---- label5 ----
		label5.setText("Min");
		label5.setHorizontalAlignment(SwingConstants.CENTER);
		panel4.add(label5, CC.xy(3, 1));

		//---- label6 ----
		label6.setText("Max");
		label6.setHorizontalAlignment(SwingConstants.CENTER);
		panel4.add(label6, CC.xy(5, 1));

		int index = 3;
		for (String par : params.keySet()) {
			JComponent c;
			if (par.equalsIgnoreCase(AttributeUtilities.ATT_TEMPERATURE) ||
					par.equalsIgnoreCase(AttributeUtilities.ATT_PH) ||
					par.equalsIgnoreCase(AttributeUtilities.ATT_WATERACTIVITY)) {
				JTextField textField = new JTextField();
				textField.setText(par);
				textField.setEnabled(false);
				textField.setHorizontalAlignment(SwingConstants.RIGHT);
				c = textField;
			}
			else {
				final JComboBox<String> comboBox = new JComboBox<String>();
				ListCellRenderer<?> renderer = comboBox.getRenderer();
				if(renderer instanceof BasicComboBoxRenderer) {
					((BasicComboBoxRenderer) renderer).setHorizontalAlignment(SwingConstants.RIGHT);
				}
				fillCombo(comboBox);
				comboBox.setSelectedItem(par);
				if (comboBox.getSelectedItem() != null && !comboBox.getSelectedItem().toString().equals(par)) comboBox.setSelectedItem(null);
			    ItemListener itemListener = new ItemListener() {
			    	boolean flag = false;
			        public void itemStateChanged(ItemEvent itemEvent) {
			        	if (itemEvent.getStateChange() != ItemEvent.SELECTED) {
			        		deselectedItem = itemEvent.getItem().toString();
			        	}
			        	else if (flag) {
			        		flag = false;
			        	}
			        	else if (params.containsKey(itemEvent.getItem().toString())) {
			        		flag = true;
			        		comboBox.setSelectedItem(deselectedItem);
			        	}
			        	else if (params.containsKey(deselectedItem)) {
			        		LinkedHashMap<String, DoubleTextField[]> newParams = new LinkedHashMap<String, DoubleTextField[]>();
			        		for (String key : params.keySet()) {
			        			if (key.equals(deselectedItem)) newParams.put(itemEvent.getItem().toString(), params.get(deselectedItem));
			        			else newParams.put(key, params.get(key));
			        		}
			        		params = newParams;
			        	}
			        }
			    }; 
			    comboBox.addItemListener(itemListener);
				c = comboBox;
			}
			panel4.add(c, CC.xy(1, index));

			dtf = params.get(par);
			DoubleTextField doubleTextFieldMin = dtf[0];
			doubleTextFieldMin.setColumns(5);
			panel4.add(doubleTextFieldMin, CC.xy(3, index));

			DoubleTextField doubleTextFieldMax = dtf[1];
			doubleTextFieldMax.setColumns(5);
			panel4.add(doubleTextFieldMax, CC.xy(5, index));
			
			index += 2;
		}	
		panel4.revalidate();
		panel4.setVisible(true);
		//add(panel4, CC.xy(1, 7));		
	}
	
	private void fillCombo(JComboBox<String> comboBox) {
		if (itemListMisc != null) {
			for (String misc : itemListMisc) {
				comboBox.addItem(misc);			
			}
		}
	}
	
	public String getMatrixString() { return matrixField.getText(); }
	public String getAgentString() { return agentField.getText(); }
	public String getLiteratureString() { return literatureField.getText(); }
	public int getMatrixID() {int id = 0; try {id = Integer.parseInt(matrixIDField.getText());}catch (Exception e1) {} return id;}
	public int getAgentID() {int id = 0; try {id = Integer.parseInt(agensIDField.getText());}catch (Exception e1) {} return id;}
	public int getLiteratureID() {int id = 0; try {id = Integer.parseInt(literatureIDField.getText());}catch (Exception e1) {} return id;}
	
	public LinkedHashMap<String, DoubleTextField[]> getParameter() {
		return params;
	}
	public void setParameter(LinkedHashMap<String, DoubleTextField[]> params) {
		this.params = params;
		handleParams();
	}
	public void setMiscItems(String[] itemListMisc) {
		this.itemListMisc = itemListMisc;
		handleParams();
	}
	
	public void setMatrixID( final int id ) throws InvalidSettingsException {
		matrixIDField.setText(id > 0 ? id+"" : "");
	}
	public void setAgensID( final int id ) throws InvalidSettingsException {
		agensIDField.setText(id > 0 ? id+"" : "");
	}
	public void setLiteratureID( final int id ) throws InvalidSettingsException {
		literatureIDField.setText(id > 0 ? id+"" : "");
	}
	public void setMatrixString( final String str ) throws InvalidSettingsException {
		
		if( str == null )
			throw new InvalidSettingsException( "Matrix Filter string must not be null." );
		
		matrixField.setText( str );
	}
	
	public void setAgentString( final String str ) throws InvalidSettingsException {
		
		if( str == null )
			throw new InvalidSettingsException( "Matrix Filter string must not be null." );
		
		agentField.setText( str );
	}
	
	public void setLiteratureString( final String str ) throws InvalidSettingsException {
		
		if( str == null )
			throw new InvalidSettingsException( "Literature Filter string must not be null." );
		
		literatureField.setText( str );
	}
	
	public static boolean passesFilter(
		final String matrixString,
		final String agentString,
		final String literatureString,
		int matrixID, int agentID, int literatureID,
		final LinkedHashMap<String, Double[]> parameter,
		final KnimeTuple tuple ) throws PmmException {
			
		if (matrixID > 0) {
			int id = tuple.getInt(TimeSeriesSchema.ATT_MATRIXID);
			if (matrixID != id) return false;
		}
		else {
			if (matrixString != null && !matrixString.trim().isEmpty()) {
				String s = tuple.getString( TimeSeriesSchema.ATT_MATRIXNAME );
				String sd = tuple.getString( TimeSeriesSchema.ATT_MATRIXDETAIL );
				if (s == null) s = ""; else s = s.toLowerCase();
				if (sd == null) sd = ""; else sd = sd.toLowerCase();
				if (!s.contains(matrixString.toLowerCase()) && !sd.contains(matrixString.toLowerCase())) return false;
			}
		}
		
		if (agentID > 0) {
			int id = tuple.getInt(TimeSeriesSchema.ATT_AGENTID);
			if (agentID != id) return false;
		}
		else {
			if (agentString != null && !agentString.trim().isEmpty()) {
				String s = tuple.getString( TimeSeriesSchema.ATT_AGENTNAME );
				String sd = tuple.getString( TimeSeriesSchema.ATT_AGENTDETAIL );
				if (s == null) s = ""; else s = s.toLowerCase();
				if (sd == null) sd = ""; else sd = sd.toLowerCase();
				if (!s.contains(agentString.toLowerCase()) && !sd.contains(agentString.toLowerCase())) return false;
			}
		}
		
		if (literatureString != null && !literatureString.trim().isEmpty()) {
			PmmXmlDoc litXmlDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_LITMD);
        	for (PmmXmlElementConvertable el : litXmlDoc.getElementSet()) {
        		if (el instanceof LiteratureItem) {
        			LiteratureItem lit = (LiteratureItem) el;
        			if (literatureID > 0) {
        				int id = lit.getId();
        				if (literatureID != id) return false;
        			}
        			else {
            			String s = lit.getAuthor();
            			String sd = lit.getTitle();
            			if (s == null) s = ""; else s = s.toLowerCase();
            			if (sd == null) sd = ""; else sd = sd.toLowerCase();
            			if (!s.contains(literatureString.toLowerCase()) && !sd.contains(literatureString.toLowerCase())) return false;
        			}
        		}
        	}
		}
		
		for (String par : parameter.keySet()) {
			Double[] dbl = parameter.get(par);
			if (dbl[0] == null && dbl[1] == null) continue;
			/*
			if (par.equalsIgnoreCase("temperature")) {
				Double temp = tuple.getDouble(TimeSeriesSchema.ATT_TEMPERATURE);
				if (temp == null || dbl[0] != null && temp < dbl[0] || dbl[1] != null && temp > dbl[1]) {
					return false;
				}
			}
			else if (par.equalsIgnoreCase("ph")) {
				Double temp = tuple.getDouble(TimeSeriesSchema.ATT_PH);
				if (temp == null || dbl[0] != null && temp < dbl[0] || dbl[1] != null && temp > dbl[1]) {
					return false;
				}
			}
			else if (par.equalsIgnoreCase("aw")) {
				Double temp = tuple.getDouble(TimeSeriesSchema.ATT_WATERACTIVITY);
				if (temp == null || dbl[0] != null && temp < dbl[0] || dbl[1] != null && temp > dbl[1]) {
					return false;
				}
			}
			*/
			//else {
				boolean paramFound = false;
				PmmXmlDoc miscXmlDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
	        	for (PmmXmlElementConvertable el : miscXmlDoc.getElementSet()) {
	        		if (el instanceof MiscXml) {
	        			MiscXml mx = (MiscXml) el;
	        			if (mx.getName().toLowerCase().equals(par)) {
	        				if (mx.getValue() == null) {
	        					paramFound = true;
	        					break;
	        				}
	        				if (dbl[0] != null && mx.getValue() < dbl[0] || dbl[1] != null && mx.getValue() > dbl[1]) {
	        					return false;
	        				}
	        				else {
	        					paramFound = true;
	        					break;
	        				}
	        			}
	        		}
	        	//}
	        	if (!paramFound && (dbl[0] != null || dbl[1] != null)) return false;
			}
		}
		
		return true;
	}
	private void selectMatrixButtonActionPerformed(ActionEvent e) {
		MyTable mat = DBKernel.myList.getTable("Matrices");
		Integer matrixID = null;
		try {matrixID = Integer.parseInt(matrixIDField.getText());}
		catch (Exception e1) {}
		Object newVal = DBKernel.myList.openNewWindow(
				mat,
				matrixID,
				(Object) "Matrices",
				null,
				1,
				1,
				null,
				true);
		if (newVal != null && newVal instanceof Integer) {
			Object matrixname = DBKernel.getValue(conn, "Matrices", "ID", newVal.toString(), "Matrixname");
			matrixField.setText(matrixname+"");
			matrixIDField.setText(""+newVal);
		}	
		else {
			matrixField.setText("");
			matrixIDField.setText("");
		}
	}

	
	private void selectAgensButtonActionPerformed(ActionEvent e) {
		MyTable age = DBKernel.myList.getTable("Agenzien");
		Integer agensID = null;
		try {agensID = Integer.parseInt(agensIDField.getText());}
		catch (Exception e1) {}
		Object newVal = DBKernel.myList.openNewWindow(
				age,
				agensID,
				(Object) "Agenzien",
				null,
				1,
				1,
				null,
				true);
		if (newVal != null && newVal instanceof Integer) {
			Object agensname = DBKernel.getValue(conn, "Agenzien", "ID", newVal.toString(), "Agensname");
			agentField.setText(agensname+"");
			agensIDField.setText(""+newVal);
		}		
		else {
			agentField.setText("");
			agensIDField.setText("");
		}
	}

	private void selectLiteratureButtonActionPerformed(ActionEvent e) {
		MyTable lit = DBKernel.myList.getTable("Literatur");
		Integer litID = null;
		try {litID = Integer.parseInt(literatureIDField.getText());}
		catch (Exception e1) {}
		Object newVal = DBKernel.myList.openNewWindow(
				lit,
				litID,
				(Object) "References",
				null,
				1,
				1,
				null,
				true);
		if (newVal != null && newVal instanceof Integer) {
			Object author = DBKernel.getValue(conn,"Literatur", "ID", newVal.toString(), "Erstautor");
			Object year = DBKernel.getValue(conn,"Literatur", "ID", newVal.toString(), "Jahr");
			Object title = DBKernel.getValue(conn,"Literatur", "ID", newVal.toString(), "Titel");
			literatureField.setText(author+"_"+year+"_"+title);
			literatureIDField.setText(""+newVal);
		}		
		else {
			literatureField.setText("");
			literatureIDField.setText("");
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		panel1 = new JPanel();
		label1 = new JLabel();
		agentField = new JTextField();
		selectAgensButton = new JButton();
		agensIDField = new JTextField();
		panel2 = new JPanel();
		label2 = new JLabel();
		matrixField = new JTextField();
		selectMatrixButton = new JButton();
		matrixIDField = new JTextField();
		panel3 = new JPanel();
		label3 = new JLabel();
		literatureField = new JTextField();
		selectLiteratureButton = new JButton();
		literatureIDField = new JTextField();
		panel4 = new JPanel();
		label4 = new JLabel();
		label5 = new JLabel();
		label6 = new JLabel();
		textField4 = new JTextField();
		doubleTextField1 = new DoubleTextField(true);
		doubleTextField2 = new DoubleTextField(true);
		textField5 = new JTextField();
		doubleTextField3 = new DoubleTextField(true);
		doubleTextField4 = new DoubleTextField(true);
		textField6 = new JTextField();
		doubleTextField5 = new DoubleTextField(true);
		doubleTextField6 = new DoubleTextField(true);
		textField7 = new JTextField();
		doubleTextField7 = new DoubleTextField(true);
		doubleTextField8 = new DoubleTextField(true);
		doubleTextField9 = new DoubleTextField(true);
		doubleTextField10 = new DoubleTextField(true);

		//======== this ========
		setLayout(new FormLayout(
			"default:grow",
			"3*(default, $lgap), default"));

		//======== panel1 ========
		{
			panel1.setBorder(new TitledBorder("Organism"));
			panel1.setLayout(new FormLayout(
				"80px, $lcgap, default:grow, 2*($lcgap, default)",
				"default"));

			//---- label1 ----
			label1.setText("Name");
			panel1.add(label1, CC.xy(1, 1));

			//---- agentField ----
			agentField.setColumns(10);
			agentField.setPreferredSize(new Dimension(50, 20));
			panel1.add(agentField, CC.xy(3, 1));

			//---- selectAgensButton ----
			selectAgensButton.setText("...");
			selectAgensButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectAgensButtonActionPerformed(e);
				}
			});
			panel1.add(selectAgensButton, CC.xy(5, 1));

			//---- agensIDField ----
			agensIDField.setColumns(1);
			agensIDField.setVisible(false);
			panel1.add(agensIDField, CC.xy(7, 1));
		}
		add(panel1, CC.xy(1, 1));

		//======== panel2 ========
		{
			panel2.setBorder(new TitledBorder("Matrix"));
			panel2.setLayout(new FormLayout(
				"80px, $lcgap, default:grow, 2*($lcgap, default)",
				"default"));

			//---- label2 ----
			label2.setText("Name");
			panel2.add(label2, CC.xy(1, 1));

			//---- matrixField ----
			matrixField.setColumns(10);
			matrixField.setPreferredSize(new Dimension(50, 20));
			panel2.add(matrixField, CC.xy(3, 1));

			//---- selectMatrixButton ----
			selectMatrixButton.setText("...");
			selectMatrixButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectMatrixButtonActionPerformed(e);
				}
			});
			panel2.add(selectMatrixButton, CC.xy(5, 1));

			//---- matrixIDField ----
			matrixIDField.setColumns(1);
			matrixIDField.setVisible(false);
			panel2.add(matrixIDField, CC.xy(7, 1));
		}
		add(panel2, CC.xy(1, 3));

		//======== panel3 ========
		{
			panel3.setBorder(new TitledBorder("Literature"));
			panel3.setLayout(new FormLayout(
				"80px, $lcgap, default:grow, 2*($lcgap, default)",
				"default"));

			//---- label3 ----
			label3.setText("Author/Title");
			panel3.add(label3, CC.xy(1, 1));

			//---- literatureField ----
			literatureField.setColumns(10);
			literatureField.setPreferredSize(new Dimension(50, 20));
			panel3.add(literatureField, CC.xy(3, 1));

			//---- selectLiteratureButton ----
			selectLiteratureButton.setText("...");
			selectLiteratureButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					selectLiteratureButtonActionPerformed(e);
				}
			});
			panel3.add(selectLiteratureButton, CC.xy(5, 1));

			//---- literatureIDField ----
			literatureIDField.setVisible(false);
			literatureIDField.setColumns(1);
			panel3.add(literatureIDField, CC.xy(7, 1));
		}
		add(panel3, CC.xy(1, 5));

		//======== panel4 ========
		{
			panel4.setBorder(new TitledBorder("Parameters"));
			panel4.setLayout(new FormLayout(
				"default:grow, 2*($lcgap, default)",
				"5*(default, $lgap), default"));

			//---- label4 ----
			label4.setText("Name");
			label4.setHorizontalAlignment(SwingConstants.CENTER);
			panel4.add(label4, CC.xy(1, 1));

			//---- label5 ----
			label5.setText("Min");
			label5.setHorizontalAlignment(SwingConstants.CENTER);
			panel4.add(label5, CC.xy(3, 1));

			//---- label6 ----
			label6.setText("Max");
			label6.setHorizontalAlignment(SwingConstants.CENTER);
			panel4.add(label6, CC.xy(5, 1));

			//---- textField4 ----
			textField4.setColumns(10);
			textField4.setText("Temperature");
			textField4.setHorizontalAlignment(SwingConstants.RIGHT);
			panel4.add(textField4, CC.xy(1, 3));

			//---- doubleTextField1 ----
			doubleTextField1.setColumns(5);
			panel4.add(doubleTextField1, CC.xy(3, 3));

			//---- doubleTextField2 ----
			doubleTextField2.setColumns(5);
			panel4.add(doubleTextField2, CC.xy(5, 3));

			//---- textField5 ----
			textField5.setColumns(20);
			textField5.setText("pH");
			textField5.setHorizontalAlignment(SwingConstants.RIGHT);
			panel4.add(textField5, CC.xy(1, 5));
			panel4.add(doubleTextField3, CC.xy(3, 5));
			panel4.add(doubleTextField4, CC.xy(5, 5));

			//---- textField6 ----
			textField6.setColumns(20);
			textField6.setText("aw");
			textField6.setHorizontalAlignment(SwingConstants.RIGHT);
			panel4.add(textField6, CC.xy(1, 7));
			panel4.add(doubleTextField5, CC.xy(3, 7));
			panel4.add(doubleTextField6, CC.xy(5, 7));

			//---- textField7 ----
			textField7.setColumns(20);
			textField7.setText("param1");
			textField7.setHorizontalAlignment(SwingConstants.RIGHT);
			panel4.add(textField7, CC.xy(1, 9));
			panel4.add(doubleTextField7, CC.xy(3, 9));
			panel4.add(doubleTextField8, CC.xy(5, 9));
			panel4.add(doubleTextField9, CC.xy(3, 11));
			panel4.add(doubleTextField10, CC.xy(5, 11));
		}
		add(panel4, CC.xy(1, 7));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel panel1;
	private JLabel label1;
	private JTextField agentField;
	private JButton selectAgensButton;
	private JTextField agensIDField;
	private JPanel panel2;
	private JLabel label2;
	private JTextField matrixField;
	private JButton selectMatrixButton;
	private JTextField matrixIDField;
	private JPanel panel3;
	private JLabel label3;
	private JTextField literatureField;
	private JButton selectLiteratureButton;
	private JTextField literatureIDField;
	private JPanel panel4;
	private JLabel label4;
	private JLabel label5;
	private JLabel label6;
	private JTextField textField4;
	private DoubleTextField doubleTextField1;
	private DoubleTextField doubleTextField2;
	private JTextField textField5;
	private DoubleTextField doubleTextField3;
	private DoubleTextField doubleTextField4;
	private JTextField textField6;
	private DoubleTextField doubleTextField5;
	private DoubleTextField doubleTextField6;
	private JTextField textField7;
	private DoubleTextField doubleTextField7;
	private DoubleTextField doubleTextField8;
	private DoubleTextField doubleTextField9;
	private DoubleTextField doubleTextField10;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}