/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.header;

import javax.swing.table.DefaultTableModel;

/**
 * @author Armin
 *
 */
public class MyTableRowModel extends DefaultTableModel {

	private int numRows;
	
	public MyTableRowModel(int numRows) {
		this.numRows = numRows;
	}

  public int getColumnCount() {
  	return 1;
  }
  public int getRowCount() {
  	return numRows;
  }

  public Object getValueAt(int row, int col) {
  	return row+1;
  }

  public boolean isCellEditable(int row, int col) {
    return false;
  }
}
