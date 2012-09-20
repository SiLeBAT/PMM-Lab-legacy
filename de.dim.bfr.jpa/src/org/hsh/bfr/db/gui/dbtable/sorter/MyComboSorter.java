/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.sorter;

import java.util.Comparator;
import java.util.LinkedHashMap;

/**
 * @author Armin
 *
 */
public class MyComboSorter implements Comparator<Integer> {

	private LinkedHashMap<Object, String>[] hashBox = null;
	private int pos;
	
	public MyComboSorter(LinkedHashMap<Object, String>[] hashBox, int pos) {		
		this.hashBox = hashBox;
		this.pos = pos;
	}
	
  @Override
  public int compare(Integer o1, Integer o2) {
  	//System.out.println("MyComboSorter");
  	if (o1 == null && o2 == null) return 0;
  	else if (o1 == null) return 1;
  	else if (o2 == null) return -1;
  	else if (hashBox[pos] == null) return 0;
  	else if (hashBox[pos].get(o1) == null && hashBox[pos].get(o2) == null) return 0;
  	else if (hashBox[pos].get(o1) == null) return 1;
  	else if (hashBox[pos].get(o2) == null) return -1;
  	else return hashBox[pos].get(o1).compareTo(hashBox[pos].get(o2));
  }
}