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
package de.bund.bfr.knime.pmm.manualmodelconf.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.SortedMap;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;

public class ModelTableModel extends JTable {

	private static final long serialVersionUID = -6782674430592418376L;
		
	private String[] columns = new String[]{"Parameter", "Independent", "Value", "Min", "Max"};
	private HashMap<String, ParametricModel> m_secondaryModels = null;
	private ParametricModel thePM;
	
	private boolean isBlankEditor = false;

	public ModelTableModel() {
		super();
		BooleanTableModel btm = new BooleanTableModel();
		this.setModel(btm);	
		this.setDefaultRenderer(Object.class, new MyTableCellRenderer());
		this.setDefaultRenderer(Boolean.class, new MyTableCellRenderer());
		this.setDefaultRenderer(Double.class, new MyTableCellRenderer());
		this.getTableHeader().setReorderingAllowed(false);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	}
	public void setPM(ParametricModel pm, HashMap<String, ParametricModel> secondaryModels) {
		thePM = pm;
		m_secondaryModels = secondaryModels;
		this.revalidate();
	}
	public void clearTable() {
		thePM = new ParametricModel("", "", "", 1);
		this.revalidate();
	}

	// Here: functionality: always overwrite cell except for pressed F2, which means: activate cell
	@Override
	public Component prepareEditor(final TableCellEditor editor, final int row, final int column) {
		Component c = super.prepareEditor(editor, row, column);		
		if (isBlankEditor) {
			((JTextField) c).setText("");
		}		
		return c;
	}
	@Override
	protected boolean processKeyBinding(final KeyStroke ks, final KeyEvent e, final int condition, final boolean pressed) {
		char ch = e.getKeyChar();
		if (ch == ',') {
  			e.setKeyChar('.');
		}
		if (!KeyEvent.getKeyText(e.getKeyCode()).equals("F2")) {
			isBlankEditor = true;
		}		
		boolean retValue = super.processKeyBinding(ks, e, condition, pressed);		
		isBlankEditor = false;
		return retValue;
	}
	
	private class BooleanTableModel extends AbstractTableModel {
 
        public int getRowCount() {
        	if (thePM == null) return 0;
        	else return thePM.getAllParVars().size();
        }
 
        public int getColumnCount() {
            return columns.length;
        }
 
        public Object getValueAt(int rowIndex, int columnIndex) {
        	if (thePM == null) return null;
        	SortedMap<String, Boolean> sm = thePM.getAllParVars();
        	Object[] oa = sm.keySet().toArray();
        	if (rowIndex < oa.length) {
            	String rowID = oa[rowIndex].toString();
            	boolean isIndep = sm.get(rowID);
            	if (columnIndex == 0) return rowID;
            	if (columnIndex == 1) return isIndep;
            	if (columnIndex == 2) return isIndep ? null : 
            		Double.isNaN(thePM.getParamValue(rowID)) ? null : thePM.getParamValue(rowID);
            	if (columnIndex == 3) return isIndep ? (Double.isNaN(thePM.getIndepMin(rowID)) ? null : thePM.getIndepMin(rowID)) :
            		(thePM.getParamMin(rowID) == null || Double.isNaN(thePM.getParamMin(rowID)) ? null : thePM.getParamMin(rowID));
            	if (columnIndex == 4) return isIndep ? (Double.isNaN(thePM.getIndepMax(rowID)) ? null : thePM.getIndepMax(rowID)) :
            		(thePM.getParamMax(rowID) == null || Double.isNaN(thePM.getParamMax(rowID)) ? null : thePM.getParamMax(rowID));
        	}
        	return null;
        }
 
        public void setValueAt(Object o, int rowIndex, int columnIndex) {
        	if (thePM == null) return;
        	SortedMap<String, Boolean> sm = thePM.getAllParVars();
        	Object[] oa = sm.keySet().toArray();
        	if (rowIndex < oa.length) {
            	String rowID = oa[rowIndex].toString();
            	if (columnIndex == 1 && o instanceof Boolean) {
            		boolean isIndep = (Boolean) o;
            		if (isIndep) {
            			thePM.addIndepVar(rowID, thePM.getParamMin(rowID), thePM.getParamMax(rowID));
            			thePM.removeParam(rowID);
                		if (thePM.getLevel() == 1) { // only one indepVar allowed
                			for (int i=0;i<this.getRowCount();i++) {
                				if (i != rowIndex && this.getValueAt(i, 1) != null && (Boolean) this.getValueAt(i, 1)) {
                					this.setValueAt(false, i, 1);
                				}
                			}
                		}
            		}
            		else {
            			thePM.addParam(rowID, Double.NaN, Double.NaN, thePM.getIndepMin(rowID), thePM.getIndepMax(rowID));
            			thePM.removeIndepVar(rowID);
            		}
            	}
            	else {
                	boolean isIndep = sm.get(rowID);
                	if (isIndep) {
                    	if (columnIndex == 3 && o instanceof Double) thePM.setIndepMin(rowID, (Double) o);
                    	if (columnIndex == 4 && o instanceof Double) thePM.setIndepMax(rowID, (Double) o);
                	}
                	else {
                    	if (columnIndex == 2 && o instanceof Double) thePM.setParamValue(rowID, (Double) o);
                    	if (columnIndex == 3 && o instanceof Double) thePM.setParamMin(rowID, (Double) o);
                    	if (columnIndex == 4 && o instanceof Double) thePM.setParamMax(rowID, (Double) o);            		
                	}
            	}
            	super.fireTableCellUpdated(rowIndex, columnIndex);
        	}
        }
 
        public String getColumnName(int columnIndex) {
            return columns[columnIndex];
        }
 
		public boolean isCellEditable(final int row, final int columnIndex) {
		    Boolean indep = (Boolean) this.getValueAt(row, 1);
		    if (indep == null) indep = false;
			return columnIndex == 1 || columnIndex > 2 || (!indep && columnIndex == 2);
		}

        public Class<?> getColumnClass(int columnIndex) {
        	if (columnIndex == 0) return Object.class;
        	else if (columnIndex == 1) return Boolean.class;
        	else if (columnIndex == 2) return Double.class;
        	else if (columnIndex == 3) return Double.class;
        	else if (columnIndex == 4) return Double.class;
        	else return Object.class;
        }
    }

	private class MyTableCellRenderer implements TableCellRenderer {
		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
			  JComponent c;
			  if (columnIndex == 1) {
				  JCheckBox checkbox = new JCheckBox();
				  checkbox.setHorizontalAlignment(SwingConstants.CENTER);
				  checkbox.setEnabled(true);
				  checkbox.setSelected(value == null ? false : (Boolean) value);
				  checkbox.setBackground(Color.WHITE);
				  c = checkbox;
			  }
			  else if (columnIndex == 0) {
				    JTextField editor = new JTextField();
				    if (value != null) editor.setText(value.toString());
				    editor.setEnabled(false);
				    boolean hasSecondary = m_secondaryModels != null && m_secondaryModels.containsKey(value);
				    editor.setFont(editor.getFont().deriveFont(hasSecondary ? Font.BOLD : Font.PLAIN));
				    editor.setToolTipText(hasSecondary ? m_secondaryModels.get(value).getModelName() : "");
				    editor.setOpaque(true);
				    c = editor;
			  }
			  else {
				    DoubleTextField editor = new DoubleTextField(true);
				    if (value != null && value instanceof Double && !Double.isNaN((Double) value)) editor.setText(value.toString());
				    else editor.setText(null);
				    if (columnIndex == 2) { // Value
					    Boolean indep = (Boolean) table.getValueAt(rowIndex, 1);
					    if (indep == null) indep = false;
					    editor.setEnabled(!indep);				    	
				    }
				    c = editor;
			  }
			  
			  try {
				  UIDefaults uiDefaults = UIManager.getDefaults();
				  Color selBGColor = (Color) uiDefaults.get("Table.selectionBackground");
				  if (isSelected) c.setBackground(selBGColor);				  
			  }
			  catch (Exception e) {}
			  
			  c.setBorder(null);
			  table.repaint();
			  table.revalidate();
			  return c;
		  }
		}
}