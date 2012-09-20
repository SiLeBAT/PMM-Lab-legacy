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
public class MyDblComboSorter implements Comparator<Double> {

	private LinkedHashMap<Object, String>[] hashBox = null;
	private int pos;
	
	public MyDblComboSorter(LinkedHashMap<Object, String>[] hashBox, int pos) {		
		this.hashBox = hashBox;
		this.pos = pos;
	}
	
  @Override
  public int compare(Double o1, Double o2) {
  	//System.out.println("MyDblComboSorter");
  	if (o1 == null && o2 == null) return 0;
  	else if (o1 == null) return 1;
  	else if (o2 == null) return -1;
  	else if (hashBox[pos] == null) return 0;
  	else if (hashBox[pos].get(o1) == null && hashBox[pos].get(o2) == null) return 0;
  	else if (hashBox[pos].get(o1) == null) return 1;
  	else if (hashBox[pos].get(o2) == null) return -1;
  	else {
  		try {
  			String str1 = hashBox[pos].get(o1);
  			String str2 = hashBox[pos].get(o2);
  			int i1 = str1.indexOf(" (");
  			int i2 = str2.indexOf(" (");
  	  		Double dbl1 = Double.parseDouble(str1.substring(0, i1));
  	  		Double dbl2 = Double.parseDouble(str2.substring(0, i2));
  			return dbl1.compareTo(dbl2);
  		}
  		catch (Exception e)  {return hashBox[pos].get(o1).compareTo(hashBox[pos].get(o2));}  		
  	}
  }
}