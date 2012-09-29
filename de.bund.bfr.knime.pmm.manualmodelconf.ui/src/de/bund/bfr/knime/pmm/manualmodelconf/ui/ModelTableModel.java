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

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;

public class ModelTableModel extends JTable {

	private static final long serialVersionUID = -6782674430592418376L;

	public final static String EXCLUDE = "F2";
	
	private boolean isBlankEditor = false;
	
	private String[] columns = new String[]{"Parameter", "Indep", "Value", "Min", "Max"};
	private List<Object[]> data = new ArrayList<Object[]>();;
	
	public ModelTableModel() {
		super();
		this.setModel(new BooleanTableModel());
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

	private class BooleanTableModel extends AbstractTableModel {
 
        public int getRowCount() {
        	if (data == null) return 0;
        	else return data.size();
        }
 
        public int getColumnCount() {
            return columns.length;
        }
 
        public Object getValueAt(int rowIndex, int columnIndex) {
        	if (data == null || data.get(rowIndex) == null) return null;
        	else return data.get(rowIndex)[columnIndex];
        }
 
        public void setValueAt(Object o, int rowIndex, int columnIndex) {
        	while (rowIndex >= data.size()) data.add(new Object[columns.length]);
        	Object[] row = data.get(rowIndex);
        	row[columnIndex] = o;
        }
 
        @Override
        public String getColumnName(int column) {
            return columns[column];
        }
 
		@Override
		public boolean isCellEditable(final int row, final int column) {
			return column != 0;
		}

		@Override
        public Class<?> getColumnClass(int columnIndex) {
        	if (columnIndex == 0) return String.class;
        	else if (columnIndex == 1) return Boolean.class;
        	else if (columnIndex == 2) return String.class;
        	else if (columnIndex == 3) return String.class;
        	else if (columnIndex == 4) return String.class;
        	else return String.class;
        }
    }
}