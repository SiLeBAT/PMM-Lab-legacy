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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;

public class ModelTableModel extends JTable implements KeyListener {

	private static final long serialVersionUID = -6782674430592418376L;

	public final static String EXCLUDE = "F2";
	
	private boolean isBlankEditor = false;
	
	private String[] columns = new String[]{"Parameter", "Indep", "Value", "Min", "Max"};
	private LinkedHashMap<String, HashMap<String, Object>> data = new LinkedHashMap<String, HashMap<String, Object>>();
	
	public ModelTableModel() {
		super();
		this.setModel(new BooleanTableModel(this));
		//this.setDefaultRenderer(Object.class, new MyTableCellRenderer());
		//this.setDefaultRenderer(Boolean.class, new MyTableCellRenderer());
	}
	
	public void clearTable() {
		data = new LinkedHashMap<String, HashMap<String, Object>>();
	}
	public void addValue(Object o, int columnIndex) {
		if (columnIndex == 0) {
			HashMap<String, Object> row = new HashMap<String, Object>();
			row.put("Parameter", o);
			data.put(o.toString(), row);			
		}
	}
	public void setValueAt(Object o, String rowID, String columnName) {
		int columnIndex = getColIndex(columnName);
		setValueAt(o, rowID, columnIndex);
	}
	public Object getValueAt(String rowID, String columnName) {
		if (data.containsKey(rowID) && data.get(rowID).containsKey(columnName)) {
			return data.get(rowID).get(columnName);			
		}
		return null;
	}
	
	private int getColIndex(String columnName) {
		for (int i=0;i<columns.length; i++) {
			if (columns[i].equalsIgnoreCase(columnName)) return i;
		}
		return -1;
	}
	private void setValueAt(Object o, String rowID, int columnIndex) {
		if (columnIndex >= 0 && data.containsKey(rowID) && (columnIndex != 0 || o != null && !data.containsKey(o.toString()))) {
			HashMap<String, Object> row = data.get(rowID);
			data.remove(rowID);
			if (columnIndex == 0) rowID = o.toString();
			if (o instanceof Double && Double.isNaN((Double) o)) o = "";
        	row.put(columns[columnIndex], o);
			data.put(rowID, row);
		}
	}

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
		if (! EXCLUDE.equals(KeyEvent.getKeyText(e.getKeyCode()))) {
			isBlankEditor = true;
		}
		
		boolean retValue = super.processKeyBinding(ks, e, condition, pressed);
		
		isBlankEditor = false;
		return retValue;
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased( final KeyEvent ke ) {}

	@Override
	public void keyTyped( final KeyEvent ke ) {
		if (ke.getSource() instanceof ModelTableModel) {
		  	char ch = ke.getKeyChar();
			  			if (ch == ',') {
			  				ch = '.';
			  				ke.setKeyChar('.');
			  			}
		}
	}
	

	private class BooleanTableModel extends AbstractTableModel {
 
		private ModelTableModel mtm;
		public BooleanTableModel(ModelTableModel mtm) {
			this.mtm = mtm;
		}
        public int getRowCount() {
        	if (data == null) return 0;
        	else return data.size();
        }
 
        public int getColumnCount() {
            return columns.length;
        }
 
        public Object getValueAt(int rowIndex, int columnIndex) {
        	Object[] oa = data.keySet().toArray();
        	if (rowIndex < oa.length) {
            	String rowID = oa[rowIndex].toString();
            	if (data != null && data.get(rowID) != null) {
            		return data.get(rowID).get(columns[columnIndex]);
            	}
        	}
        	return null;
        }
 
        public void setValueAt(Object o, int rowIndex, int columnIndex) {
        	Object[] oa = data.keySet().toArray();
        	if (rowIndex < oa.length) {
            	String rowID = oa[rowIndex].toString();
            	mtm.setValueAt(o, rowID, columnIndex);
        	}
        }
 
        @Override
        public String getColumnName(int columnIndex) {
            return columns[columnIndex];
        }
 
		@Override
		public boolean isCellEditable(final int row, final int columnIndex) {
			return columnIndex != 0;
		}

		@Override
        public Class<?> getColumnClass(int columnIndex) {
        	if (columnIndex == 0) return Object.class;
        	else if (columnIndex == 1) return Boolean.class;
        	else if (columnIndex == 2) return Double.class;
        	else if (columnIndex == 3) return Double.class;
        	else if (columnIndex == 4) return Double.class;
        	else return Object.class;
        }
        
    }

	class MyTableCellRenderer implements TableCellRenderer {
		  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			  if (column == 1) {
				  JCheckBox checkbox = new JCheckBox();
				  checkbox.setSelected(value == null ? false : (Boolean) value);
				  checkbox.setBackground((row % 2 == 0) ? Color.white : Color.cyan);
				  return checkbox;
			  }
			  else if (column == 0) {
				    JTextField editor = new JTextField();
				    if (value != null) editor.setText(value.toString());
				    editor.setEnabled(false);
				    editor.setEditable(false);
				    editor.setBackground((row % 2 == 0) ? Color.white : Color.cyan);
				    return editor;
			  }
			  else {
				    DoubleTextField editor = new DoubleTextField(true);
				    if (value != null) editor.setText(value.toString());
				    editor.setBackground((row % 2 == 0) ? Color.white : Color.cyan);
				    return editor;
			  }
		  }
		}
}