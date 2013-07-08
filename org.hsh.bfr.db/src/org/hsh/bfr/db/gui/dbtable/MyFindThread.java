/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
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
package org.hsh.bfr.db.gui.dbtable;

import java.util.LinkedHashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import org.hsh.bfr.db.MyTable;


/**
 * @author Armin
 *
 */
public class MyFindThread extends Thread {

  private int actualFindPos = 0;
  private LinkedHashMap<String, int[]> myFounds = new LinkedHashMap<String, int[]>();
  private MyDBTable myDBTable1;
  private String findString = "";
  private MyDBPanel parent;
  public boolean stop = false;

  public MyFindThread(MyDBTable myDBTable1, String findString, MyDBPanel parent) {
  	this.myDBTable1 = myDBTable1;
  	this.findString = findString;
  	this.parent = parent;
  }
  
  public void run() {
  	myFounds.clear(); actualFindPos = 0;  
	int numRows = myDBTable1.getRowCount();
	int numCols = myDBTable1.getColumnCount();
	findString = findString.toUpperCase();
	
    MyTable[] foreignFields = myDBTable1.getActualTable().getForeignFields();
	String[] mnTable = myDBTable1.getActualTable().getMNTable();
    boolean lookIn = false;
    String res;
    StringTokenizer tok = new StringTokenizer(findString);
    String[] findStrings = new String[tok.countTokens()];
    for (int i=0;tok.hasMoreTokens();i++) {
    	findStrings[i] = tok.nextToken();
    }

    Vector<Integer> v = new Vector<Integer>();
	for (int col = 0; col < numCols; col++) {
		boolean isMN = (col > 0 && mnTable != null && mnTable.length > col-1 && mnTable[col-1] != null);
		if (myDBTable1.getColumn(col).getType() != java.sql.Types.BOOLEAN && !isMN) {
			lookIn = (col > 0 && foreignFields != null && foreignFields.length > col-1 && foreignFields[col-1] != null && myDBTable1.hashBox[col-1] != null);
		  	for (int row = 0; row < numRows; row++) {
		  	    Object o = myDBTable1.getValueAt(row, col);
		  	    if (o != null) { // sonst Fehler in z.B. Methoden bei der Suchfunktion
		  	    	//System.out.println(col);
		  	    	if (lookIn && myDBTable1.hashBox[col-1].get(o) != null) res = myDBTable1.hashBox[col-1].get(o).toString();
		  	    	else res = o.toString();
		  	    	if (res != null) {
		  	    		int i;
			  	    	for (i=0;i<findStrings.length;i++) {
			    			if (res.toUpperCase().indexOf(findStrings[i]) < 0) break;
			  	    	}
			  	    	if (i == findStrings.length) {
			  	    		myFounds.put("." + row + "." + col + ".", new int[]{row,col});
			  	    		if (actualFindPos == 0 && myDBTable1.getSelectedRow() <= row) actualFindPos = myFounds.size();	
			  	    		v.addElement(new Integer(row));
			  	    	}
		  	    	}
		  	    }
		  	  if (stop) return;
		  	}  			
  		}
	}
	//myDBTable1.filter(new MyFilter(v));


    /*
  	for (int i = 0; i < numRows; i++) {
  		for (int j = 0; j < numCols; j++) {
  			if (myDBTable1.getColumn(j).getType() != java.sql.Types.BOOLEAN) {
    			String res = myDBTable1.getVisibleCellContent(i, j);
    			if (res != null && res.toUpperCase().indexOf(findString) >= 0) { //  
	  	    		myFounds.put("." + i + "." + j + ".", new int[]{i,j});
	  	    		if (actualFindPos == 0 && myDBTable1.getSelectedRow() <= i) actualFindPos = myFounds.size();
	  	    	}
  			}
  			if (stop) return;
  		}
  	}
*/
		parent.initFindVector(myFounds, actualFindPos, findString);

		//myDBTable1.find(0, 1, textField1.getText(), null, true);
		//System.out.println(textField1.getText());
		
		//myDBTable1.doFind();
		//System.out.println("threadDone");
  }
}
