/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Vector;

import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.MyTable;
import org.hsh.bfr.db.PlausibilityChecker;
import org.hsh.bfr.db.imports.InfoBox;

import quick.dbtable.DBTableCellListener;

/**
 * @author Armin
 *
 */
public class MyTableCellListener implements DBTableCellListener {
	
	private MyDBTable theTable = null;
	private MyTable myT = null;
	
	public MyTableCellListener(MyDBTable theTable) {
		this.theTable = theTable;
		myT = theTable.getActualTable();
	}
	
	public Object cellValueChanged(int row, int col, Object oldValue, Object newValue) {
		//System.out.println(row + "\t" + col + "\t" + oldValue + "\t" + newValue);
		try {
			if (theTable.getMyCellPropertiesModel() instanceof MyCellPropertiesModel) {
				if (theTable.getColumn(col-1).getColumnName().equals("Geprueft") && oldValue == null && newValue != null && 
						newValue instanceof Boolean && ((Boolean)newValue) == false) {
					;
				}
				else if (oldValue == null || newValue == null || !String.valueOf(newValue).equals(String.valueOf(oldValue))) {
					if (oldValue == null && newValue != null && newValue.toString().length() == 0) {
						;
					}
					else {
						//System.out.println(oldValue + "\t" + newValue);
						//if (oldValue != null && newValue != null) System.out.println("gleuich" + String.valueOf(newValue).equals(String.valueOf(oldValue)));
						LinkedHashMap<Integer, HashSet<Integer>> modifiedCells = ((MyCellPropertiesModel) theTable.getMyCellPropertiesModel()).getModifiedCellsColl();
						if (modifiedCells != null) {
							HashSet<Integer> vec;
							int newRow = theTable.getTable().convertRowIndexToView(row);
							Object o = theTable.getValueAt(newRow, 0);
							if (o != null && o instanceof Integer) {
								Integer id = (Integer) o;
								if (modifiedCells.containsKey(id)) vec = modifiedCells.get(id);
								else vec = new HashSet<Integer>();
								vec.add(col-1);
								modifiedCells.put(id, vec);
							}							
						}						
					}
				}
			}
			if (col - 2 >= myT.getFieldNames().length) return newValue;
			String tname = myT.getTablename();
			String fname = myT.getFieldNames()[col - 2]; 
			if (newValue.toString().trim().length() == 0 || PlausibilityChecker.getValuePlausible(tname, fname, newValue) == null) return newValue;
			else return provideMessageAndReturn(fname, oldValue, newValue);			
		}
		catch (Exception e) {MyLogger.handleException(e); return null;}
	}
	
	private Object provideMessageAndReturn(String feldname, Object oldValue, Object newValue) {
		InfoBox ib = new InfoBox("Im Feld \"" + feldname + "\" wurde kein gültiger Wert eingegeben!\nEingegebener Wert: " + newValue +
				"\nDieser Wert kann nicht übernommen werden, daher wird der alte Wert (" + oldValue + ") wieder eingesetzt.",
				true, new Dimension(500, 150), null, true);
		ib.setVisible(true);   
		theTable.grabFocus();
		return null;
	}
}
