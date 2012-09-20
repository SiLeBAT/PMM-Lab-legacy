/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.sorter;

import java.util.Comparator;

/**
 * @author Armin
 *
 */
public class MyOtherSorter implements Comparator<Object> {

  @Override
  public int compare(Object o1, Object o2) {
  	//System.out.println("MyOtherSorter");
  	if (o1 == null && o2 == null) return 0;
  	else if (o1 == null) return 1;
  	else if (o2 == null) return -1;
		if (o1 instanceof Object[] && o2 instanceof Object[]) {
			Object[] oo1 = (Object[]) o1;
			Object[] oo2 = (Object[]) o2;
			// Tja, was tun?
			return 0;
		}
  	else {
  		return 0;
  	}
  }
}