/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
