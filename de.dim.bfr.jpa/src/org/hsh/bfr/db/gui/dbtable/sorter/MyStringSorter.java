/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.sorter;

import java.util.Comparator;

/**
 * @author Armin
 *
 */
public class MyStringSorter implements Comparator<String> {

  @Override
  public int compare(String o1, String o2) {
  	//System.out.println("MyStringSorter");
  	if (o1 == null && o2 == null) return 0;
  	else if (o1 == null) return 1;
  	else if (o2 == null) return -1;
  	else return o1.trim().compareToIgnoreCase(o2.trim());
  }
}