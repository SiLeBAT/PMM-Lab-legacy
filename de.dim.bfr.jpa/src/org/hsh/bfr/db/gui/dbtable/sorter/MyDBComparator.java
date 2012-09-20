/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.sorter;

import quick.dbtable.Comparator;
import quick.dbtable.DBTable;

/**
 * @author Armin
 *
 */
public class MyDBComparator implements Comparator {

	private DBTable myDB;
	
	public MyDBComparator(DBTable myDB) {
		this.myDB = myDB;
		System.out.println("MyDBComparator");
	}
	
	public int compare(int column, Object currentData, Object nextData) {
		System.out.println(column);
		return Comparator.DEFAULT_SORT;
	}

}
