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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.SymbolTable;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.resources.Resources;
import de.dim.bfr.external.service.BFRNodeService;

/**
 * @author Armin Weiser
 */
public class MMC_M extends JPanel {

	private static final String LABEL_OWNMODEL = "Manually defined model";

	private int m_level = 1;
	private Frame m_parentFrame = null;
	private HashMap<String,ParametricModel> m_modelCatalog = null;
	private HashMap<String, ParametricModel> m_secondaryModels = null;
	private BFRNodeService m_service = null;
	private boolean dontTouch = false;

	public MMC_M() {
		this(null, 1, "");
	}
	public MMC_M(final Frame parentFrame, final int level, final String paramName) {
		this.m_level = level;
		this.m_parentFrame = parentFrame;
		initComponents();
		table.getModel().addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				modelnameField.setText(getOwnModelname(getPM()));
				modelnameFieldFocusLost(null);
			}
		});
		m_modelCatalog = new HashMap<String,ParametricModel>();	
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
		return m_modelCatalog.get(modelNameBox.getSelectedItem());
	}
	public void setPM(ParametricModel pm) {
		if (pm != null) {
			if (m_secondaryModels != null && m_secondaryModels.size() > 0) radioButton3.setSelected(true);
			modelnameField.setText(pm.getModelName());
			formulaArea.setText(pm.getFormula());
			table.setPM(pm, m_secondaryModels);
			if (pm.getModelId() != m_modelCatalog.get(modelNameBox.getSelectedItem()).getModelId()) {
				dontTouch = true;
				modelNameBox.setSelectedItem(pm.getModelName());
				dontTouch = false;
			}
			m_modelCatalog.put(modelNameBox.getSelectedItem().toString(), pm);
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
		m_modelCatalog.clear();
		if (m_secondaryModels != null) m_secondaryModels.clear();
		ParametricModel pm = new ParametricModel(LABEL_OWNMODEL, "", "", m_level);
		modelNameBox.addItem(LABEL_OWNMODEL);
		m_modelCatalog.put(LABEL_OWNMODEL, pm);
		try {			
			ResultSet result = db.selectModel(m_level);			
			while(result.next()) {				
				String modelName = result.getString(Bfrdb.ATT_NAME);
				String formula = result.getString(Bfrdb.ATT_FORMULA);
				int modelID = result.getInt(Bfrdb.ATT_MODELID);

				pm = new ParametricModel(modelName, formula, result.getString(Bfrdb.ATT_DEP), m_level, modelID);
				manageDBMinMax(result, pm);
				manageIndep(pm, result.getArray(Bfrdb.ATT_INDEP));
				
				modelNameBox.addItem(modelName);
				m_modelCatalog.put(modelName, pm);
			}			
			result.getStatement().close();
			result.close();
			
			db.close();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		modelNameBox.setSelectedItem(LABEL_OWNMODEL);
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
		
		ParametricModel pm = m_modelCatalog.get(modelNameBox.getSelectedItem());
		if (pm != null) {
			setPM(pm);
		}
		else if (modelNameBox.getItemCount() > 1) {
			System.err.println("pm = null???\t" + modelNameBox.getSelectedItem() + "\t" + modelNameBox.getItemCount());
		}
	}

	private void formulaAreaFocusLost(FocusEvent e) {
		ParametricModel pm = m_modelCatalog.get(modelNameBox.getSelectedItem());
		if (pm != null && !pm.getFormula().equals(formulaArea.getText())) {
			String newMN = getOwnModelname(pm);
			ParametricModel newPM = new ParametricModel(newMN, formulaArea.getText(), pm.getDepVar(), 1, MathUtilities.getRandomNegativeInt());
			m_modelCatalog.put(LABEL_OWNMODEL, newPM);
			parseFormula(pm, newPM);
			modelNameBox.setSelectedIndex(0);
		}
	}

	private String getOwnModelname(ParametricModel pm) {
		if (pm == null) return null;
		return pm.getModelName().endsWith("_" + LABEL_OWNMODEL) ? pm.getModelName() : pm.getModelName() + "_" + LABEL_OWNMODEL;
	}

	private void formulaAreaKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			formulaAreaFocusLost(null);
		}
	}

	private void modelnameFieldFocusLost(FocusEvent e) {
		ParametricModel pm = m_modelCatalog.get(modelNameBox.getSelectedItem());
		if (pm != null && !pm.getModelName().equals(modelnameField.getText())) {
			ParametricModel newPM = pm.clone();
			newPM.setModelName(modelnameField.getText());
			newPM.setModelId(MathUtilities.getRandomNegativeInt());
			m_modelCatalog.put(LABEL_OWNMODEL, newPM);
			modelNameBox.setSelectedIndex(0);
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
			int oldLevel = m_level;
			m_level = radioButton2.isSelected() ? 2 : 1;
			if (m_level != oldLevel) setDB(m_service);
			if (!radioButton3.isSelected()) {
				m_secondaryModels.clear();
			}
		}
	}

	public String toXmlString() {		
		PmmXmlDoc doc = new PmmXmlDoc();

		ParametricModel pm = m_modelCatalog.get(modelNameBox.getSelectedItem());
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
		
		if (m_level == 1) {
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
		catch( Exception e ) {
			e.printStackTrace();
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
		literatureLabel = new JLabel();

		//======== this ========
		setBorder(new CompoundBorder(
			new TitledBorder("Model Properties"),
			Borders.DLU2_BORDER));
		setLayout(new FormLayout(
			"default, 3*($lcgap, default:grow)",
			"6*(default, $lgap), default:grow"));
		((FormLayout)getLayout()).setColumnGroups(new int[][] {{3, 5, 7}});

		//---- depVarLabel ----
		depVarLabel.setText("Parameter");
		depVarLabel.setHorizontalAlignment(SwingConstants.CENTER);
		depVarLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		add(depVarLabel, CC.xywh(1, 1, 7, 1));

		//---- radioButton1 ----
		radioButton1.setText("primary");
		radioButton1.setSelected(true);
		radioButton1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radioButtonActionPerformed(e);
			}
		});
		add(radioButton1, CC.xy(3, 3));

		//---- radioButton2 ----
		radioButton2.setText("secondary");
		radioButton2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radioButtonActionPerformed(e);
			}
		});
		add(radioButton2, CC.xy(5, 3));

		//---- radioButton3 ----
		radioButton3.setText("primary (secondary)");
		radioButton3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				radioButtonActionPerformed(e);
			}
		});
		add(radioButton3, CC.xy(7, 3));

		//---- modelNameLabel ----
		modelNameLabel.setText("Model from DB:");
		add(modelNameLabel, CC.xy(1, 5));

		//---- modelNameBox ----
		modelNameBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modelNameBoxActionPerformed(e);
			}
		});
		add(modelNameBox, CC.xywh(3, 5, 5, 1));

		//---- label1 ----
		label1.setText("Modellname:");
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
		add(modelnameField, CC.xywh(3, 7, 5, 1));

		//---- label2 ----
		label2.setText("Model formula:");
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
		add(formulaArea, CC.xywh(3, 9, 5, 1));

		//---- tableLabel ----
		tableLabel.setText("Parameter definition:");
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
		add(scrollPane1, CC.xywh(3, 11, 5, 1));

		//---- literatureLabel ----
		literatureLabel.setText("Reference:");
		add(literatureLabel, CC.xy(1, 13));

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
	private JLabel literatureLabel;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

}
