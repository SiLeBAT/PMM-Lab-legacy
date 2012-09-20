/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.sorter;

import java.util.Comparator;

/**
 * @author Armin
 *
 */
public class MyBooleanSorter implements Comparator<Boolean> {

  @Override
  public int compare(Boolean o1, Boolean o2) {
  	//System.out.println("MyBooleanSorter\t" + o1 + "\t" + o2);
  	if (o1 == null && o2 == null) return 0;
  	else if (o1 == null) return 1;
  	else if (o2 == null) return -1;
  	else return o1.compareTo(o2);
  }
}