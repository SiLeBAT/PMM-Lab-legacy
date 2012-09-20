/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.sorter;

import java.sql.Timestamp;
import java.util.Comparator;

/**
 * @author Armin
 *
 */
public class MyDatetimeSorter implements Comparator<Timestamp> {

  @Override
  public int compare(Timestamp o1, Timestamp o2) {
  	//System.out.println("MyDatetimeSorter");
  	if (o1 == null && o2 == null) return 0;
  	else if (o1 == null) return 1;
  	else if (o2 == null) return -1;
  	else return o1.compareTo(o2);
  }
}