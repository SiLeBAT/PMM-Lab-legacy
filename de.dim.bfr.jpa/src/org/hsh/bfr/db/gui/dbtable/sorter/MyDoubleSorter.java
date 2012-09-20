/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.sorter;

import java.util.Comparator;

/**
 * @author Armin
 *
 */
public class MyDoubleSorter implements Comparator<Double> {

  @Override
  public int compare(Double o1, Double o2) {
  	//System.out.println("MyDoubleSorter");
  	if (o1 == null && o2 == null) return 0;
  	else if (o1 == null) return 1;
  	else if (o2 == null) return -1;
  	else return o1.compareTo(o2);
  }
}