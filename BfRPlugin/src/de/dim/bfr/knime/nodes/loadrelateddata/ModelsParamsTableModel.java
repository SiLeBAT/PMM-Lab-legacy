/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
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
package de.dim.bfr.knime.nodes.loadrelateddata;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class ModelsParamsTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ModelsParamsTableModel(Object[][] rows, Object[] columns) {
		super(rows, columns);
	}

	
	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if ((columnIndex >= 0) && (columnIndex < getColumnCount())) {
			return getValueAt(0, columnIndex).getClass();
		}
		else {
			return Object.class;
		}
//			Object value=this.getValueAt(0,columnIndex);  
//			return (value==null?Object.class:value.getClass());  
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
         return false;
    }
}
