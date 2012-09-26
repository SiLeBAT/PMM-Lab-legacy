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
package org.hsh.bfr.db.gui.dbtable.editoren;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JTextField;
import javax.swing.table.TableModel;

import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.MyTable;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;

import quick.dbtable.Filter;

/**
 * @author Armin
 *
 */
public class MyFilter implements Filter {
	
	private MyDBTable myDBTable1;
	private LinkedHashMap<Integer, Vector<String>> userIDs = null;
	private String findString = "";
	private JTextField textField1 = null;
	
	public MyFilter(MyDBTable myDBTable1, String findString, LinkedHashMap<Integer, Vector<String>> userIDs, JTextField textField1) {
		this.myDBTable1 = myDBTable1;
		this.findString = findString;
		this.userIDs = userIDs;
		this.textField1 = textField1;
	}
	
     public int[] filter(TableModel tm) {
    	 int[] result = null;
    	 Vector<Integer> rows2check = null;
    	 if (userIDs != null) {
        	 rows2check = new Vector<Integer>();
		  	 for (int row = 0; row < tm.getRowCount(); row++) {
		  		Integer o = (Integer) tm.getValueAt(row, 1);
		  		if (userIDs.containsKey(o)) {
		  			rows2check.addElement(new Integer(row));
		  		}
			 }
    	 }
    	 Vector<Integer> vv = new Vector<Integer>();
    	 try {
    		 if (findString.trim().length() == 0) {
    			 result = getAllFilter(tm, rows2check);
    		 }
    		 else {
        		 StringTokenizer tok = new StringTokenizer(findString);
        		 String[] findStrings = new String[tok.countTokens()];
        		 for (int i=0;tok.hasMoreTokens();i++) {
    				findStrings[i] = tok.nextToken().toUpperCase();
        		 }
        		 int numCols = tm.getColumnCount();
        		 int numRows = tm.getRowCount();
        		 MyTable[] foreignFields = myDBTable1.getActualTable().getForeignFields();
        		 String[] mnTable = myDBTable1.getActualTable().getMNTable();
        		 if (userIDs != null) {
            		 for (Integer row : rows2check) {
         		  		 for (int col = 0; col < numCols; col++) {
         		  			if (loopInternal(vv, row.intValue(), col, mnTable, foreignFields, tm, findStrings)) break;
             	    	}
            		 }        		 
        		 }
        		 else {
        			 for (int row = 0; row < numRows; row++) {
         		  		 for (int col = 0; col < numCols; col++) {
          		  			if (loopInternal(vv, row, col, mnTable, foreignFields, tm, findStrings)) break;
             	    	}
        			 }
        		 }
            	 result = getAllFilter(tm, vv);
    		 }
    	 }
    	 catch (Exception e) {MyLogger.handleException(e); result = null;}
 		if (textField1 != null && !textField1.hasFocus()) {
 			textField1.requestFocus();
 		}
 		return result;
     }
     private boolean loopInternal(Vector<Integer> vv, int row, int col, String[] mnTable, MyTable[] foreignFields, TableModel tm, String[] findStrings) {
		 String res;
    	 boolean isMN = (col > 0 && mnTable != null && mnTable.length > col-1 && mnTable[col-1] != null);
    	 	if (myDBTable1.getColumn(col).getType() != java.sql.Types.BOOLEAN && !isMN) {
    	 		Object o = tm.getValueAt(row, col+1);
    	 		if (o != null) { // sonst Fehler in z.B. Methoden bei der Suchfunktion
      	  			boolean lookIn = (col > 0 && foreignFields != null && foreignFields.length > col-1 && foreignFields[col-1] != null && myDBTable1.hashBox[col-1] != null);
  		  	    	if (lookIn && myDBTable1.hashBox[col-1].get(o) != null) res = myDBTable1.hashBox[col-1].get(o).toString();
  		  	    	else res = o.toString();
  		  	    	//System.err.println(row + "\t" + col + "\t" + o + "\t" + res);
  		  	    	if (res != null) {
  		  	    		int i;
  			  	    	for (i=0;i<findStrings.length;i++) {
  			  	    		if (res.toUpperCase().indexOf(findStrings[i]) < 0) break;
  			  	    	}
  			  	    	if (i == findStrings.length) {
  			  	    		vv.addElement(new Integer(row));
  			  	    		return true;
  			  	    	}
  		  	    	}
  		  	    }
	  	}  	
	  			return false;
     }
     private int[] getAllFilter(TableModel tm, Vector<Integer> vv) {
    	 if (vv == null) {
        	 int numRows = tm.getRowCount();
        	 int arr[] = new int[numRows];
        	 for (int row = 0; row < numRows; row++) {
        		 arr[row] = row;
        	 }    		 
        	 return arr;
    	 }
    	 else {
             int arr[] = new int[vv.size()];
             for(int i=0; i< vv.size(); i++) {
               arr[i] = (vv.elementAt(i)).intValue();
             }          	 
             return arr;    		 
    	 }
     }
}
