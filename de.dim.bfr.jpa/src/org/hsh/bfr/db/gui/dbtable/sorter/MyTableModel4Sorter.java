/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.sorter;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.hsh.bfr.db.gui.dbtable.MyDBTable;

/**
 * @author Armin
 *
 */
public class MyTableModel4Sorter extends AbstractTableModel {

	private MyDBTable myDB;
	private JTable table;
	Object[][] o;
	
	public MyTableModel4Sorter(MyDBTable myDB) {
		this.table = myDB.getTable();
		this.myDB = myDB;
	}
	
	public void initArray() {
		o = myDB.getDataArray();
	}
	public int getColumnCount() {
		return table.getModel().getColumnCount() + 1;
	}

	public int getRowCount() {
		return table.getModel().getRowCount();
	}

	public Object getValueAt(int row, int col) {
		//System.out.println(table.getValueAt(0, col-1) + "\t" + o.length + "\t" + myDB.getDataArray().length);
		//if (o.length == 0) o = myDB.getDataArray();
		if (o == null || row >= o.length) return null;
		return o[row][col-1];//table.getValueAt(row, col-1);
	}
/*
  public String getColumnName(int col) {
    return table.getColumnName(col-1);
	}
	public Class getColumnClass(int col) {
	    return table.getColumnClass(col-1);
	}
	*/	
}