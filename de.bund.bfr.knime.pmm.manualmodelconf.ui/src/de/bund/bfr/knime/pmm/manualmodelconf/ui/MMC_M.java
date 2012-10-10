/*
 * Created by JFormDesigner on Sat Sep 29 12:27:04 CEST 2012
 */

package de.bund.bfr.knime.pmm.manualmodelconf.ui;

import java.awt.*;
import java.awt.event.*;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.*;
import de.bund.bfr.knime.pmm.common.ui.*;

import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.SymbolTable;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.resources.Resources;
import de.dim.bfr.external.service.BFRNodeService;

/**
 * @author Armin Weiser
 */
public class MMC_M extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String LABEL_OWNMODEL = "Manually defined formula";

	private Frame m_parentFrame = null;
	private HashMap<String, ParametricModel> m_secondaryModels = null;
	private BFRNodeService m_service = null;
	private boolean dontTouch = false;

	public MMC_M() {
		this(null, 1, "");
	}
	public MMC_M(final Frame parentFrame, final int level, final String paramName) {
		this.m_parentFrame = parentFrame;
		initComponents();
		if (level == 1) m_secondaryModels = new HashMap<String, ParametricModel>();
		depVarLabel.setText(paramName);
		if (level == 1) {
			radioButton1.setSelected(true);
			//depVarLabel.setVisible(false);
		}
		else {
			depVarLabel.setVisible(true);
			radioButton2.setSelected(true);
			radioButton1.setEnabled(false);
			radioButton2.setEnabled(false);
			radioButton3.setEnabled(false);
		}
	}
	
	public ParametricModel getPM() {
		return table.getPM();
	}
	public void setPM(ParametricModel pm) {
		if (pm != null) {
			if (pm.getLevel() == 2) {
				if (!radioButton2.isSelected()) {
					radioButton2.setSelected(true);
					setDB(m_service);
				}
			}
			else if (m_secondaryModels != null && m_secondaryModels.size() > 0) {
				radioButton3.setSelected(true);
			}
			modelnameField.setText(pm.getModelName());
			String depVar = depVarLabel.getText();
			if (!depVar.isEmpty()) {
				String formula = pm.getFormula();
				int index = formula.lastIndexOf("=");
				if (index >= 0) {
					String oldDepVar = formula.substring(0, index);
					//System.err.println(oldDepVar + "=" + depVar);
					pm.addVarParMap(depVar, oldDepVar);
					formula = depVar + formula.substring(index);
					pm.setDepVar(depVar);
					pm.setFormula(formula);
				}
			}			
			formulaArea.setText(pm.getFormula());
			table.setPM(pm, m_secondaryModels);
			insertNselectPMintoBox(pm);
		}
	}
	public void setDB(final BFRNodeService service) {	
		this.m_service = service;
		Bfrdb db;
		try {			
			db = new Bfrdb(service);
			getFromDB(db);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	private void getFromDB(final Bfrdb db) {		
		modelNameBox.removeAllItems();
		if (m_secondaryModels != null) m_secondaryModels.clear();
		int level = radioButton2.isSelected() ? 2 : 1;
		ParametricModel pm = new ParametricModel(LABEL_OWNMODEL, "", "", level);
		modelNameBox.addItem(pm);
		//System.err.println("added1:" + pm + "\t" + pm.hashCode());
		try {			
			ResultSet result = db.selectModel(level);			
			while(result.next()) {				
				String modelName = result.getString(Bfrdb.ATT_NAME);
				String formula = result.getString(Bfrdb.ATT_FORMULA);
				int modelID = result.getInt(Bfrdb.ATT_MODELID);

				pm = new ParametricModel(modelName, formula, result.getString(Bfrdb.ATT_DEP), level, modelID);
				manageDBMinMax(result, pm);
				manageIndep(pm, result.getArray(Bfrdb.ATT_INDEP));
				
				modelNameBox.addItem(pm);
				//System.err.println("added2:" + pm + "\t" + pm.hashCode());
			}			
			result.getStatement().close();
			result.close();
			
			db.close();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
	private void manageDBMinMax(ResultSet result, ParametricModel pm) throws SQLException {
		Array array = result.getArray(Bfrdb.ATT_PARAMNAME);
		Array arrayMin = result.getArray(Bfrdb.ATT_MINVALUE);
		Array arrayMax = result.getArray(Bfrdb.ATT_MAXVALUE);
	    if (array != null && arrayMin != null && arrayMax != null) {
		    try {
				Object[] o = (Object[])array.getArray();
				Object[] oMin = (Object[])arrayMin.getArray();
				Object[] oMax = (Object[])arrayMax.getArray();
				if (o != null && o.length > 0) {
					for (int ii=0;ii<o.length;ii++) {
						pm.addParam(o[ii].toString(), Double.NaN, Double.NaN);
						if (oMin != null && oMin.length > ii && oMin[ii] != null) {
							pm.setParamMin(o[ii].toString(), Double.parseDouble(oMin[ii].toString()));
						}
						if (oMax != null && oMax.length > ii && oMax[ii] != null) {
							pm.setParamMax(o[ii].toString(), Double.parseDouble(oMax[ii].toString()));
						}
					}					
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	}
    private String manageIndep(ParametricModel pm, Array array) {
    	String result = null;
	    if (array != null) {
		    try {
				Object[] o = (Object[])array.getArray();
				if (o != null && o.length > 0) {
					for (int i=0;i<o.length;i++) {
						pm.addIndepVar(o[i].toString());
					}					
				}
			}
		    catch (SQLException e) {
				e.printStackTrace();
			}
	    }
    	return result;
    }

	public void stopCellEditing() {
		if (table.isEditing()) {
			table.getCellEditor().stopCellEditing();
		}
	}

	private void parseFormula(ParametricModel oldPM, ParametricModel newPM) {
		String formula = formulaArea.getText();
		formula = formula.replaceAll( "\n", "" );
		formula = formula.replaceAll( "\\s", "" );
		formula = formula.replace("~", "=").trim();
		
		int index = formula.indexOf("=");
		if (index < 0) {
			return;
		}
		
		formulaArea.setText(formula);
		String depVar = formula.substring(0, index).trim();
		newPM.setDepVar(depVar);

		DJep parser = MathUtilities.createParser();
		try {
			parser.parse(formula);
			SymbolTable st = parser.getSymbolTable();
		    for (Object o : st.keySet()) {
		    	String os = o.toString();
		        if (!os.equals(depVar)) {
		        	if (oldPM.getIndepVarSet().contains(os)) {
		        		newPM.addIndepVar(os, oldPM.getIndepMin(os), oldPM.getIndepMax(os));
		        	}
		        	else if (oldPM.getParamNameSet().contains(os)) {
		        		newPM.addParam(os, oldPM.getParamValue(os), oldPM.getParamError(os), oldPM.getParamMin(os), oldPM.getParamMax(os));
		        	}
		        	else {
		        		newPM.addParam(os);
		        	}
		        }		        
		    }
		}
		catch (ParseException e) {
			if (!e.getErrorInfo().startsWith("Unexpected \"<EOF>\"") &&
					!e.getErrorInfo().startsWith("Encountered \"-\" at")) {
				e.printStackTrace();
			}
		}
	}

	private void modelNameBoxActionPerformed(ActionEvent e) {
		if (dontTouch) return;
		table.clearTable();
		if (m_secondaryModels != null) m_secondaryModels.clear();
		formulaArea.setText("");
		modelnameField.setText("");
		
		ParametricModel pm = (ParametricModel) modelNameBox.getSelectedItem();
		if (pm != null) {
			setPM(pm);
		}
		else if (modelNameBox.getItemCount() > 1) {
			System.err.println("pm = null???\t" + modelNameBox.getSelectedItem() + "\t" + modelNameBox.getItemCount());
		}
		else {
			//System.err.println("pm = null???\t" + modelNameBox.getItemCount());
		}
	}
	
	private void insertNselectPMintoBox(ParametricModel pm) {
		int i=0;
		for (i=0;i<modelNameBox.getItemCount();i++) {
			if (pm.getModelId() == ((ParametricModel) modelNameBox.getItemAt(i)).getModelId()) {
			//if (pm.hashCode() == ((ParametricModel) modelNameBox.getItemAt(i)).hashCode()) {
				break;
			}
		}
		dontTouch = true;
		if (i == modelNameBox.getItemCount()) {
			modelNameBox.addItem(pm);
			//System.err.println("added3:" + pm + "\t" + pm.hashCode());
		}
		modelNameBox.setSelectedItem(pm);
		dontTouch = false;		
	}

	private void formulaAreaFocusLost(FocusEvent e) {
		ParametricModel pm = table.getPM();
		if (pm != null && !pm.getFormula().equals(formulaArea.getText())) {
			String newMN = getNewModelname(pm);
			ParametricModel newPM = new ParametricModel(newMN, formulaArea.getText(), pm.getDepVar(), pm.getLevel(), MathUtilities.getRandomNegativeInt());
			insertNselectPMintoBox(newPM);
			parseFormula(pm, newPM);
			modelNameBox.setSelectedItem(newPM);
		}
	}

	private void formulaAreaKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			formulaAreaFocusLost(null);
		}
	}

	private String getNewModelname(ParametricModel pm) {
		if (pm == null) return null;
		String mn = pm.getModelName();
		int lio = mn.lastIndexOf(" (v");
		String result = mn;
		try {
			if (lio >= 0) {
				String number = mn.substring(lio + 3, mn.length() - 1);
				Long.parseLong(number);
				result = mn.substring(0, lio);
			}
		}
		catch (Exception e) {}
		result += " (v" + System.currentTimeMillis() + ")";
		return result;
	}

	private void modelnameFieldFocusLost(FocusEvent e) {
		ParametricModel pm = table.getPM();
		if (pm != null && !pm.getModelName().equals(modelnameField.getText())) {
			ParametricModel newPM = pm.clone();
			newPM.setModelName(modelnameField.getText());
			newPM.setModelId(MathUtilities.getRandomNegativeInt());
			/*
			System.err.println(pm.getParamValue("a0") + "\t" + pm + "\t" + pm.hashCode() + "\n" +
					newPM.getParamValue("a0") + "\t" + newPM + "\t" + newPM.hashCode() + "\n" +
					table.getValueAt(0, 2) + "\t" + table.getPM() + "\t" + table.getPM().hashCode() + "\t" + table.getPM().getParamValue("a0"));
					*/
			insertNselectPMintoBox(newPM);
			modelNameBox.setSelectedItem(newPM);
		}
	}

	private void modelnameFieldKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			modelnameFieldFocusLost(null);
		}
	}

	private void tableMouseClicked(MouseEvent e) {
		if (radioButton3.isSelected()) {
			int row = table.getSelectedRow();
			Object isIndep = table.getValueAt(row, 1);
			if (isIndep == null || isIndep instanceof Boolean && !((Boolean) isIndep)) {
				SecDialog secondaryDialog = new SecDialog(m_parentFrame);
				secondaryDialog.setModal(true);
				secondaryDialog.setIconImage(Resources.getInstance().getDefaultIcon());
				String param = table.getValueAt(row, 0).toString();
				MMC_M m2 = new MMC_M(null, 2, param);
				m2.setDB(m_service);
				m2.setPM(m_secondaryModels.get(param));
				secondaryDialog.setPanel(m2, param, m_secondaryModels);
				secondaryDialog.pack();
				
				secondaryDialog.setLocationRelativeTo(this);
				secondaryDialog.setAlwaysOnTop(true);
				secondaryDialog.setVisible(true);
			}
		}
	}

	private void radioButtonActionPerformed(ActionEvent e) {
		if (m_service != null) {
			int level = radioButton2.isSelected() ? 2 : 1;
			if (!radioButton3.isSelected()) m_secondaryModels.clear();
			ParametricModel pm = table.getPM();
			if (level != pm.getLevel()) setDB(m_service);
		}
	}

	public String toXmlString() {		
		PmmXmlDoc doc = new PmmXmlDoc();

		ParametricModel pm = table.getPM();
		if (table.hasChanged()) {
			if (pm != null && pm.getModelName().equals(modelnameField.getText())) {
				modelnameField.setText(getNewModelname(pm));
				modelnameFieldFocusLost(null);	
				pm = (ParametricModel) modelNameBox.getSelectedItem();
			}
		}
		/*
		// add literature items
			for (Map.Entry<Integer, String> entry : possLiterature.entrySet()) {
			    Integer key = entry.getKey();
			    String value = entry.getValue();
				ParametricModel pmc = modelCatalog.get(modelNameBox.getSelectedItem());
				List<Integer> li = modLitMat.get(pmc.getModelId());
			    if (li != null && li.contains(key)) {
					int q = value.indexOf( " et al. " );
					
					if (hasParamValues && getLevel() == 1) {
						pm.addEstModelLit(value.substring( 0, q ), Integer.valueOf( value.substring( q+8 ) ), key);
					} else {
						pm.addModelLit(value.substring( 0, q ), Integer.valueOf( value.substring( q+8 ) ), key);
					}
			    }
			}
		*/
		doc.add(pm);
		
		if (!radioButton2.isSelected()) {
			for (Map.Entry<String, ParametricModel> entry : m_secondaryModels.entrySet()) {
				String key = entry.getKey();
				if (pm.getParamNameSet().contains(key)) {
					ParametricModel value = entry.getValue();
					doc.add(value);
				}
			}
		}
		return doc.toXmlString();
	}
	
	public void setFromXmlString(final String xmlString) {		
		try {			
			PmmXmlDoc doc = new PmmXmlDoc(xmlString);			
			// fetch model set
			ParametricModel theModel = null;
			for (int i = 0; i < doc.size(); i++) {				
				PmmXmlElementConvertable el = doc.get(i);
				if (el instanceof ParametricModel) {
					ParametricModel pm = (ParametricModel) el;
					
					if (pm.getLevel() == 1) {
						theModel = pm;
					}
					else {
						if (theModel == null) theModel = pm;
						m_secondaryModels.put(pm.getDepVar(), pm);
					}
				}				
			}

			if (theModel != null) {
				if (theModel.getLevel() == 2) m_secondaryModels.clear();
				setPM(theModel);
			}
			/*
			List<Integer> li = new ArrayList<Integer>();
			for( LiteratureItem item : primModel.getModelLit()) {
				li.add(item.getId());
			}
			for( LiteratureItem item : primModel.getEstModelLit()) {
				li.add(item.getId());
			}
			ParametricModel pmc = modelCatalog.get( modelNameBox.getSelectedItem() );
			modLitMat.put(pmc.getModelId(), li);
			
			updatePossibleLiterature();
			updateLiterature();
			*/
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void button1ActionPerformed(ActionEvent e) {
		/*
		BFRNodeService service = BfRNodePluginActivator.getBfRService();
		//final Connection connection = service.getJDBCConnection();
		//org.hsh.bfr.db.StartApp.go(connection, true, true);
		Object newVal = DBKernel.myList.openNewWindow(
				DBKernel.myList.getTable("Literatur"),
				null,
				(Object) "Literatur",
				(MyDBTable) null,
				1,
				1);
		//org.hsh.bfr.db.StartApp.go(connection, true, true, "Literatur");
		 * */
	}

	private void r2FieldFocusLost(FocusEvent e) {
		ParametricModel pm = table.getPM();
		if (pm != null) {
			try {
				pm.setRsquared(r2Field.getValue());
			} catch (PmmException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void r2FieldKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			r2FieldFocusLost(null);
		}
	}

	private void rmsFieldFocusLost(FocusEvent e) {
		ParametricModel pm = table.getPM();
		if (pm != null) {
			try {
				pm.setRms(rmsField.getValue());
			} catch (PmmException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void rmsFieldKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			rmsFieldFocusLost(null);
		}
	}

	private void aicFieldFocusLost(FocusEvent e) {
		ParametricModel pm = table.getPM();
		if (pm != null) {
			pm.setAic(aicField.getValue());
		}
	}

	private void aicFieldKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			aicFieldFocusLost(null);
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		depVarLabel = new JLabel();
		radioButton1 = new JRadioButton();
		radioButton2 = new JRadioButton();
		radioButton3 = new JRadioButton();
		modelNameLabel = new JLabel();
		modelNameBox = new JComboBox();
		label1 = new JLabel();
		modelnameField = new JTextField();
		label2 = new JLabel();
		formulaArea = new JTextField();
		tableLabel = new JLabel();
		scrollPane1 = new JScrollPane();
		table = new ModelTableModel();
		label3 = new JLabel();
		r2Field = new DoubleTextField(true);
		label4 = new JLabel();
		rmsField = new DoubleTextField(true);
		label5 = new JLabel();
		aicField = new DoubleTextField(true);
		button1 = new JButton();

		//======== this ========
		setBorder(new CompoundBorder(
			new TitledBorder("Model Properties"),
			Borders.DLU2_BORDER));
		setLayout(new FormLayout(
			"3*(default, $lcgap), default:grow, 2*($lcgap, default), $lcgap, default:grow, 2*($lcgap, default), $lcgap, default:grow",
			"default, $rgap, default, $ugap, 2*(default, $pgap), 3*(default, $ugap), default:grow"));
		((FormLayout)getLayout()).setColumnGroups(new int[][] {{3, 9, 15}, {5, 11, 17}, {7, 13, 19}});

		//---- depVarLabel ----
		depVarLabel.setText("Parameter");
		depVarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		depVarLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		add(depVarLabel, CC.xywh(1, 1, 19, 1));

		//---- radioButton1 ----
		radioButton1.setText("primary");
		radioButton1.setSelected(true);
		radioButton1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radioButtonActionPerformed(e);
			}
		});
		add(radioButton1, CC.xywh(3, 3, 5, 1));

		//---- radioButton2 ----
		radioButton2.setText("secondary");
		radioButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radioButtonActionPerformed(e);
			}
		});
		add(radioButton2, CC.xywh(9, 3, 5, 1));

		//---- radioButton3 ----
		radioButton3.setText("primary (secondary)");
		radioButton3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radioButtonActionPerformed(e);
			}
		});
		add(radioButton3, CC.xywh(15, 3, 5, 1));

		//---- modelNameLabel ----
		modelNameLabel.setText("Formula from DB:");
		add(modelNameLabel, CC.xy(1, 5));

		//---- modelNameBox ----
		modelNameBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modelNameBoxActionPerformed(e);
			}
		});
		add(modelNameBox, CC.xywh(3, 5, 17, 1));

		//---- label1 ----
		label1.setText("Formula Name:");
		add(label1, CC.xy(1, 7));

		//---- modelnameField ----
		modelnameField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				modelnameFieldFocusLost(e);
			}
		});
		modelnameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				modelnameFieldKeyReleased(e);
			}
		});
		add(modelnameField, CC.xywh(3, 7, 17, 1));

		//---- label2 ----
		label2.setText("Formula:");
		add(label2, CC.xy(1, 9));

		//---- formulaArea ----
		formulaArea.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				formulaAreaFocusLost(e);
			}
		});
		formulaArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				formulaAreaKeyReleased(e);
			}
		});
		add(formulaArea, CC.xywh(3, 9, 17, 1));

		//---- tableLabel ----
		tableLabel.setText("Parameter Definition:");
		add(tableLabel, CC.xy(1, 11));

		//======== scrollPane1 ========
		{

			//---- table ----
			table.setPreferredScrollableViewportSize(new Dimension(450, 175));
			table.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					tableMouseClicked(e);
				}
			});
			scrollPane1.setViewportView(table);
		}
		add(scrollPane1, CC.xywh(3, 11, 17, 1));

		//---- label3 ----
		label3.setText("R\u00b2:");
		label3.setHorizontalAlignment(SwingConstants.CENTER);
		add(label3, CC.xy(3, 13));

		//---- r2Field ----
		r2Field.setColumns(7);
		r2Field.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				r2FieldFocusLost(e);
			}
		});
		r2Field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				r2FieldKeyReleased(e);
			}
		});
		add(r2Field, CC.xy(5, 13));

		//---- label4 ----
		label4.setText("RMS:");
		label4.setHorizontalAlignment(SwingConstants.CENTER);
		add(label4, CC.xy(9, 13));

		//---- rmsField ----
		rmsField.setColumns(7);
		rmsField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				rmsFieldFocusLost(e);
			}
		});
		rmsField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				rmsFieldKeyReleased(e);
			}
		});
		add(rmsField, CC.xy(11, 13));

		//---- label5 ----
		label5.setText("AIC:");
		label5.setVisible(false);
		add(label5, CC.xy(15, 13));

		//---- aicField ----
		aicField.setVisible(false);
		aicField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				aicFieldFocusLost(e);
			}
		});
		aicField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				aicFieldKeyReleased(e);
			}
		});
		add(aicField, CC.xy(17, 13));

		//---- button1 ----
		button1.setText("Choose References");
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1ActionPerformed(e);
			}
		});
		add(button1, CC.xywh(15, 15, 5, 1));

		//---- buttonGroup1 ----
		ButtonGroup buttonGroup1 = new ButtonGroup();
		buttonGroup1.add(radioButton1);
		buttonGroup1.add(radioButton2);
		buttonGroup1.add(radioButton3);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JLabel depVarLabel;
	private JRadioButton radioButton1;
	private JRadioButton radioButton2;
	private JRadioButton radioButton3;
	private JLabel modelNameLabel;
	private JComboBox modelNameBox;
	private JLabel label1;
	private JTextField modelnameField;
	private JLabel label2;
	private JTextField formulaArea;
	private JLabel tableLabel;
	private JScrollPane scrollPane1;
	private ModelTableModel table;
	private JLabel label3;
	private DoubleTextField r2Field;
	private JLabel label4;
	private DoubleTextField rmsField;
	private JLabel label5;
	private DoubleTextField aicField;
	private JButton button1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

}
