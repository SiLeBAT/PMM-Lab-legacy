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

import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.LinkedHashMap;

import javax.swing.JComponent;
import javax.swing.JTextArea;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MainKernel;
import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.MyTable;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;

import quick.dbtable.CellComponent;

/**
 * @author Armin
 *
 */
public class MyMNRenderer extends JTextArea implements CellComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MyTable myT = null;
	private int selectedColumn;
	private boolean isINTmn = false;
	//private boolean isDBLmn = false;
	private LinkedHashMap<Object, String> theValues = new LinkedHashMap<Object, String>();
	private LinkedHashMap<Object, Long> lastUpdate = new LinkedHashMap<Object, Long>();
	
	public MyMNRenderer(MyDBTable myDB, int selectedColumn) {
		this.myT = myDB.getActualTable();
    	String[] mnTable = myT.getMNTable();
    	isINTmn = mnTable != null && selectedColumn < mnTable.length && mnTable[selectedColumn] != null && mnTable[selectedColumn].equals("INT");
		this.selectedColumn = selectedColumn;
		//this.setHorizontalAlignment(JLabel.CENTER);
		//this.setVerticalAlignment(JLabel.TOP);
		this.setLineWrap(true);
		this.setWrapStyleWord(true);
	}
	public void setValue(Object value) {
		String sql = "";
		String result = "";
		//this.setText(value+"");
		//if (true) return;
		//System.out.println(value + "\t" + (System.currentTimeMillis() - DBKernel.triggerFired) + "\t" + theValues.containsKey(value));
		if (value == null) {
			this.setText("");
			return;
		}		
		else if (theValues.containsKey(value) && lastUpdate.containsKey(value) &&
				lastUpdate.get(value) > DBKernel.triggerFired && lastUpdate.get(value) > MainKernel.triggerFired) {
				//System.currentTimeMillis() - DBKernel.triggerFired > 1000) {
			//System.out.println(selectedColumn + "\t" + value + "\t" + theValues.get(value));
			this.setText(theValues.get(value));
			return;
		}
		/*
		else if (DBKernel.scrolling) {
			this.setText("");
			return;
		}
		*/
		else if (value instanceof Integer) {
			String tn = myT.getTablename(); 
	    	if (isINTmn) {
				if (tn.equals("GeschaetzteModelle")) tn = "GeschaetztesModell";
	    		//System.out.println("mnTable: " + tn + value + myT.getFieldNames()[selectedColumn] + myT.getForeignFields()[selectedColumn].getTablename());
	    		if (myT.getForeignFields()[selectedColumn] != null) {
	    			String ft = myT.getForeignFields()[selectedColumn].getTablename();
	    			if (ft.equals("Zutatendaten")) {
						sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Matrixname") + " FROM " + DBKernel.delimitL(ft) +
						" LEFT JOIN " + DBKernel.delimitL("Matrices") +
						" ON " + DBKernel.delimitL("Zutatendaten") + "." + DBKernel.delimitL("Matrix") + "=" +
						DBKernel.delimitL("Matrices") + "." + DBKernel.delimitL("ID") +
						" WHERE " + DBKernel.delimitL(tn) + "=" + value;					    				    					    				
	    			}
	    			else if (ft.equals("Prozessdaten")) {
						sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("ProzessElement") + " FROM " + DBKernel.delimitL(ft) +
						" LEFT JOIN " + DBKernel.delimitL("ProzessElemente") +
						" ON " + DBKernel.delimitL("Prozessdaten") + "." + DBKernel.delimitL("Prozess_CARVER") + "=" +
						DBKernel.delimitL("ProzessElemente") + "." + DBKernel.delimitL("ID") +
						" WHERE " + DBKernel.delimitL("Workflow") + "=" + value;					    				    					    				
	    			}
	    			else if (ft.equals("Produktkatalog")) {
						sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Artikelnummer") + "," +
						DBKernel.delimitL("Bezeichnung") + " FROM " + DBKernel.delimitL(ft) +
						" WHERE " + DBKernel.delimitL("Knoten") + "=" + value;
	    			}
	    			else if (ft.equals("Lieferungen")) {
						sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Bezeichnung") + "," +
						DBKernel.delimitL("ChargenNr") + "," + DBKernel.delimitL("Lieferdatum") +
						" FROM " + DBKernel.delimitL(ft) +
						" LEFT JOIN " + DBKernel.delimitL("Produktkatalog") +
						" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Artikel") + "=" +
						DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("ID") +
						" WHERE " + DBKernel.delimitL("Artikel") + "=" + value;
	    			}
	    			else if (ft.equals("LieferungVerbindungen")) {
	    				String fn = myT.getFieldNames()[selectedColumn];
						sql = "SELECT " + DBKernel.delimitL("LieferungVerbindungen") + "." + DBKernel.delimitL("ID") + "," +
						DBKernel.delimitL("Bezeichnung") + "," + DBKernel.delimitL("Lieferdatum") + "," +
						DBKernel.delimitL("ChargenNr") + " FROM " + DBKernel.delimitL(ft) +
						" LEFT JOIN " + DBKernel.delimitL("Lieferungen") +
						" ON " + DBKernel.delimitL("LieferungVerbindungen") + "." + DBKernel.delimitL(fn) + "=" +
						DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") +
						" LEFT JOIN " + DBKernel.delimitL("Produktkatalog") +
						" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Artikel") + "=" +
						DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("ID") +
						" WHERE " + DBKernel.delimitL(fn.equals("Vorprodukt") ? "Zielprodukt" : "Vorprodukt") + "=" + value;
	    			}
	    			else if (ft.startsWith("Codes_")) {
						sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("CodeSystem") + "," + DBKernel.delimitL("Code") + " FROM " + DBKernel.delimitL(ft) +
						" WHERE " + DBKernel.delimitL("Basis") + "=" + value;	
	    			}
	    			else {
						sql = "SELECT " + DBKernel.delimitL("ID") + " FROM " + DBKernel.delimitL(ft) +
						" WHERE " + DBKernel.delimitL(tn) + "=" + value;					    				    					    				
	    			}
	    			sql += " ORDER BY " + DBKernel.delimitL("ID") + " ASC";
	    		}
	    	}
	    	else if (tn.equals("Aufbereitungsverfahren")) {
				String fn = myT.getFieldNames()[selectedColumn];
				if (fn.equals("Kits")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Bezeichnung") + " FROM " + DBKernel.delimitL("Kits") +
					" LEFT JOIN " + DBKernel.delimitL("Aufbereitungsverfahren_Kits") +
					" ON " + DBKernel.delimitL("Aufbereitungsverfahren_Kits") + "." + DBKernel.delimitL("Kits") + "=" +
					DBKernel.delimitL("Kits") + "." + DBKernel.delimitL("ID") +
					" WHERE " + DBKernel.delimitL("Aufbereitungsverfahren_Kits") + "." + DBKernel.delimitL(tn) + "=" + value;				
				}
				else if (fn.equals("Normen")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Name") + " FROM " + DBKernel.delimitL("Methodennormen") +
					" LEFT JOIN " + DBKernel.delimitL("Aufbereitungsverfahren_Normen") +
					" ON " + DBKernel.delimitL("Aufbereitungsverfahren_Normen") + "." + DBKernel.delimitL("Normen") + "=" +
					DBKernel.delimitL("Methodennormen") + "." + DBKernel.delimitL("ID") +
					" WHERE " + DBKernel.delimitL(tn) + "=" + value;	
				}
			}
			else if (tn.equals("Artikel_Lieferung")) {
				String fn = myT.getFieldNames()[selectedColumn];
				if (fn.equals("Vorprodukte")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Artikelnummer") + "," +
					DBKernel.delimitL("Bezeichnung") + " FROM " + DBKernel.delimitL("Artikel_Lieferung") +
					" LEFT JOIN " + DBKernel.delimitL("Lieferung_Lieferungen") +
					" ON " + DBKernel.delimitL("Lieferung_Lieferungen") + "." + DBKernel.delimitL("Vorprodukt") + "=" +
					DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("ID") +
					" LEFT JOIN " + DBKernel.delimitL("Artikel_Lieferung") +
					" ON " + DBKernel.delimitL("Lieferung_Lieferungen") + "." + DBKernel.delimitL("Artikel_Lieferung") + "=" +
					DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("ID") +
					" LEFT JOIN " + DBKernel.delimitL("Produzent_Artikel") +
					" ON " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("Artikel") + "=" +
					DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("ID") +
					" WHERE " + DBKernel.delimitL(tn) + "=" + value;				
				}
			}
	    	else if (tn.equals("Modell")) {	
					
				if (selectedColumn==3) sql = "SELECT " + DBKernel.delimitL("ID") +  "," + DBKernel.delimitL("Bezeichnung") + " FROM " + DBKernel.delimitL("Verwendung") +
				" LEFT JOIN " + DBKernel.delimitL("Modell_Verwendung_Verbund") +
				" ON " + DBKernel.delimitL("Modell_Verwendung_Verbund") + "." + DBKernel.delimitL("Verwendung") + "=" +
				DBKernel.delimitL("Verwendung") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;
				
				if (selectedColumn==4) sql = "SELECT " + DBKernel.delimitL("ID") +  "," + DBKernel.delimitL("Agensname") + " FROM " + DBKernel.delimitL("Agenzien") +
				" LEFT JOIN " + DBKernel.delimitL("Modell_Agenzien_Verbund") +
				" ON " + DBKernel.delimitL("Modell_Agenzien_Verbund") + "." + DBKernel.delimitL("Agenzien") + "=" +
				DBKernel.delimitL("Agenzien") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;
				
				if (selectedColumn==5) sql = "SELECT " + DBKernel.delimitL("ID") +  "," + DBKernel.delimitL("Bezeichnung") + " FROM " + DBKernel.delimitL("Resistenz") +
				" LEFT JOIN " + DBKernel.delimitL("Modell_Resistenz_Verbund") +
				" ON " + DBKernel.delimitL("Modell_Resistenz_Verbund") + "." + DBKernel.delimitL("Resistenz") + "=" +
				DBKernel.delimitL("Resistenz") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;
				
				if (selectedColumn==7) sql = "SELECT " + DBKernel.delimitL("ID") +  "," + DBKernel.delimitL("Bezeichnung") + " FROM " + DBKernel.delimitL("Methoden_Software") +
				" LEFT JOIN " + DBKernel.delimitL("Modell_Software_Verbund") +
				" ON " + DBKernel.delimitL("Modell_Software_Verbund") + "." + DBKernel.delimitL("Methoden_Software") + "=" +
				DBKernel.delimitL("Methoden_Software") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;
				
				if (selectedColumn==8) sql = "SELECT " + DBKernel.delimitL("ID") +  "," + DBKernel.delimitL("Bezeichnung") + " FROM " + DBKernel.delimitL("Preharvest") +
				" LEFT JOIN " + DBKernel.delimitL("Modell_Preharvest_Verbund") +
				" ON " + DBKernel.delimitL("Modell_Preharvest_Verbund") + "." + DBKernel.delimitL("Preharvest") + "=" +
				DBKernel.delimitL("Preharvest") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;
				
				if (selectedColumn==9) sql = "SELECT " + DBKernel.delimitL("ID") +  "," + DBKernel.delimitL("Bezeichnung") + " FROM " + DBKernel.delimitL("Harvest") +
				" LEFT JOIN " + DBKernel.delimitL("Modell_Harvest_Verbund") +
				" ON " + DBKernel.delimitL("Modell_Harvest_Verbund") + "." + DBKernel.delimitL("Harvest") + "=" +
				DBKernel.delimitL("Harvest") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;
				/*
				if (selectedColumn==10) sql = "SELECT " + DBKernel.delimitL("ID") +  "," + DBKernel.delimitL("Matrixname") + " FROM " + DBKernel.delimitL("Matrices") +
				" LEFT JOIN " + DBKernel.delimitL("Modell_Zwischenprodukt_Verbund") +
				" ON " + DBKernel.delimitL("Modell_Zwischenprodukt_Verbund") + "." + DBKernel.delimitL("Matrices") + "=" +
				DBKernel.delimitL("Matrices") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;
				*/
				
				if (selectedColumn==10) sql = "SELECT " + DBKernel.delimitL("ID") +  "," + DBKernel.delimitL("Bezeichnung") + " FROM " + DBKernel.delimitL("Produkt") +
				" LEFT JOIN " + DBKernel.delimitL("Modell_Zwischenprodukt_Verbund") +
				" ON " + DBKernel.delimitL("Modell_Zwischenprodukt_Verbund") + "." + DBKernel.delimitL("Produkt") + "=" +
				DBKernel.delimitL("Produkt") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;
				
				/*
				if (selectedColumn==xx) sql = "SELECT " + DBKernel.delimitL("ID") +  "," + DBKernel.delimitL("Name") + " FROM " + DBKernel.delimitL("ProzessWorkflow") +
				" LEFT JOIN " + DBKernel.delimitL("Modell_ProzessFlow_Verbund") +
				" ON " + DBKernel.delimitL("Modell_ProzessFlow_Verbund") + "." + DBKernel.delimitL("ProzessWorkflow") + "=" +
				DBKernel.delimitL("ProzessWorkflow") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;
				*/
				
				/*if (selectedColumn==11) sql = "SELECT " + DBKernel.delimitL("ID") +  "," + DBKernel.delimitL("Matrixname") + " FROM " + DBKernel.delimitL("Matrices") +
				" LEFT JOIN " + DBKernel.delimitL("Modell_Einzelhandelsprodukt_Verbund") +
				" ON " + DBKernel.delimitL("Modell_Einzelhandelsprodukt_Verbund") + "." + DBKernel.delimitL("Matrices") + "=" +
				DBKernel.delimitL("Matrices") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;
				*/
				if (selectedColumn==11) sql = "SELECT " + DBKernel.delimitL("ID") +  "," + DBKernel.delimitL("Bezeichnung") + " FROM " + DBKernel.delimitL("Produkt") +
				" LEFT JOIN " + DBKernel.delimitL("Modell_Einzelhandelsprodukt_Verbund") +
				" ON " + DBKernel.delimitL("Modell_Einzelhandelsprodukt_Verbund") + "." + DBKernel.delimitL("Produkt") + "=" +
				DBKernel.delimitL("Produkt") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;
				
				
				if (selectedColumn==12) sql = "SELECT " + DBKernel.delimitL("ID") +  "," + DBKernel.delimitL("Bezeichnung") + " FROM " + DBKernel.delimitL("Transport") +
				" LEFT JOIN " + DBKernel.delimitL("Modell_Transport_Verbund") +
				" ON " + DBKernel.delimitL("Modell_Transport_Verbund") + "." + DBKernel.delimitL("Transport") + "=" +
				DBKernel.delimitL("Transport") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;
				
				
				if (selectedColumn==13) { 
					/*sql = "SELECT " + DBKernel.delimitL("ID") +  "," + DBKernel.delimitL("Expositionsart") +  "," + DBKernel.delimitL("Produkt") 
					+ " FROM " + DBKernel.delimitL("Exposition") +
					" LEFT JOIN " + DBKernel.delimitL("Modell_Exposition_Verbund") +
					" ON " + DBKernel.delimitL("Modell_Exposition_Verbund") + "." + DBKernel.delimitL("Exposition") + "=" +
					DBKernel.delimitL("Exposition") + "." + DBKernel.delimitL("ID") +
					" WHERE " + DBKernel.delimitL(tn) + "=" + value;
					*/
					
					sql="SELECT " + DBKernel.delimitL("Exposition") + "." +  DBKernel.delimitL("Expositionsart") + "," +
					DBKernel.delimitL("Produkt") + "." +  DBKernel.delimitL("Bezeichnung")
				 	+ " FROM " + DBKernel.delimitL("Exposition") + " LEFT JOIN " + DBKernel.delimitL("Modell_Exposition_Verbund") 
				 	+  " ON " +  DBKernel.delimitL("Exposition") + "." +  DBKernel.delimitL("ID") + " = "  
				 	+ DBKernel.delimitL("Modell_Exposition_Verbund") + "." + DBKernel.delimitL("Exposition")  
				 	+ " LEFT JOIN " + DBKernel.delimitL("Produkt") + " ON " + DBKernel.delimitL("Exposition") + "." + DBKernel.delimitL("Produkt") + " = "  
				 	+ DBKernel.delimitL("Produkt") + "." + DBKernel.delimitL("ID") + " WHERE " + DBKernel.delimitL(tn) + "=" + value;
					
					/*
					try {
						ResultSet rs = DBKernel.getResultSet(sql, false);
						if (rs.first()) { 
							do {								
								result += rs.getString(1);
								if (rs.getString(2) != null)  result += ": " +  rs.getString(2) + "\n";								
							}  
							while (rs.next());
						}
					}
					catch (Exception e) {MyLogger.handleException(e); }
					System.out.println(result);
					this.setText(result);
					if (theValues.containsKey(value)) theValues.remove(value);
					theValues.put(value, result);
					return;
					*/
				}
				
				
				if (selectedColumn==14) sql = "SELECT " + DBKernel.delimitL("ID") +  "," + DBKernel.delimitL("Bezeichnung") + " FROM " + DBKernel.delimitL("Risikocharakterisierung") +
				" LEFT JOIN " + DBKernel.delimitL("Modell_Risikocharakterisierung_Verbund") +
				" ON " + DBKernel.delimitL("Modell_Risikocharakterisierung_Verbund") + "." + DBKernel.delimitL("Risikocharakterisierung") + "=" +
				DBKernel.delimitL("Risikocharakterisierung") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;
					
			}
    		else if (tn.equals("Nachweisverfahren")) {
				String fn = myT.getFieldNames()[selectedColumn];
				if (fn.equals("Kits")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Bezeichnung") + " FROM " + DBKernel.delimitL("Kits") +
					" LEFT JOIN " + DBKernel.delimitL("Nachweisverfahren_Kits") +
					" ON " + DBKernel.delimitL("Nachweisverfahren_Kits") + "." + DBKernel.delimitL("Kits") + "=" +
					DBKernel.delimitL("Kits") + "." + DBKernel.delimitL("ID") +
					" WHERE " + DBKernel.delimitL("Nachweisverfahren_Kits") + "." + DBKernel.delimitL(tn) + "=" + value;	
				}
				else if (fn.equals("Normen")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Name") + " FROM " + DBKernel.delimitL("Methodennormen") +
					" LEFT JOIN " + DBKernel.delimitL("Nachweisverfahren_Normen") +
					" ON " + DBKernel.delimitL("Nachweisverfahren_Normen") + "." + DBKernel.delimitL("Normen") + "=" +
					DBKernel.delimitL("Methodennormen") + "." + DBKernel.delimitL("ID") +
					" WHERE " + DBKernel.delimitL(tn) + "=" + value;	
				}
			}
			else if (tn.equals("Methoden")) {
				sql = "SELECT " + DBKernel.delimitL("ID") +
				//",CONCAT(" + DBKernel.delimitL("Name") + ",CONCAT(' - ',IFNULL(" + DBKernel.delimitL("Norm_Nummer") + ",''))) FROM " +
				",CASE WHEN " + DBKernel.delimitL("Norm_Nummer") + " IS NULL THEN " +
				DBKernel.delimitL("Name") + " ELSE CONCAT(" + DBKernel.delimitL("Name") + ",CONCAT(' - '," + DBKernel.delimitL("Norm_Nummer") + ")) END FROM " +
				DBKernel.delimitL("Methodennormen") +
				" LEFT JOIN " + DBKernel.delimitL("Methoden_Normen") +
				" ON " + DBKernel.delimitL("Methoden_Normen") + "." + DBKernel.delimitL("Normen") + "=" +
				DBKernel.delimitL("Methodennormen") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;				
			}
			else if (tn.equals("Labore")) {
				String fn = myT.getFieldNames()[selectedColumn];
				if (fn.equals("Matrices")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Matrixname") + " FROM " + DBKernel.delimitL("Matrices") +
					" LEFT JOIN " + DBKernel.delimitL("Labore_Matrices") +
					" ON " + DBKernel.delimitL("Labore_Matrices") + "." + DBKernel.delimitL("Matrices") + "=" +
					DBKernel.delimitL("Matrices") + "." + DBKernel.delimitL("ID") +
					" WHERE " + DBKernel.delimitL(tn) + "=" + value;									
				}
				else if (fn.equals("Untersuchungsart")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Name") + " FROM " + DBKernel.delimitL("Methodiken") +
					" LEFT JOIN " + DBKernel.delimitL("Labore_Methodiken") +
					" ON " + DBKernel.delimitL("Labore_Methodiken") + "." + DBKernel.delimitL("Methodiken") + "=" +
					DBKernel.delimitL("Methodiken") + "." + DBKernel.delimitL("ID") +
					" WHERE " + DBKernel.delimitL(tn) + "=" + value;														
				}
				else if (fn.equals("Agenzien")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Agensname") + " FROM " + DBKernel.delimitL("Agenzien") +
					" LEFT JOIN " + DBKernel.delimitL("Labore_Agenzien") +
					" ON " + DBKernel.delimitL("Labore_Agenzien") + "." + DBKernel.delimitL("Agenzien") + "=" +
					DBKernel.delimitL("Agenzien") + "." + DBKernel.delimitL("ID") +
					" WHERE " + DBKernel.delimitL(tn) + "=" + value;																			
				}
			}
			else if (tn.equals("Labore_Agenzien")) {
				sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Name") + " FROM " + DBKernel.delimitL("Methodiken") +
				" LEFT JOIN " + DBKernel.delimitL("Labore_Agenzien_Methodiken") +
				" ON " + DBKernel.delimitL("Labore_Agenzien_Methodiken") + "." + DBKernel.delimitL("Methodiken") + "=" +
				DBKernel.delimitL("Methodiken") + "." + DBKernel.delimitL("ID") +
				" WHERE " + DBKernel.delimitL(tn) + "=" + value;														
			}
			else if (tn.equals("Krankheitsbilder")) {
				String fn = myT.getFieldNames()[selectedColumn];
				if (fn.equals("Symptome")) {
					sql = "SELECT " + DBKernel.delimitL("ID") +
							",COALESCE(" + DBKernel.delimitL("Bezeichnung") + "," + DBKernel.delimitL("Bezeichnung_engl") + ") FROM " + DBKernel.delimitL("Symptome") +
							" LEFT JOIN " + DBKernel.delimitL("Krankheitsbilder_Symptome") +
							" ON " + DBKernel.delimitL("Krankheitsbilder_Symptome") + "." + DBKernel.delimitL("Symptome") + "=" +
							DBKernel.delimitL("Symptome") + "." + DBKernel.delimitL("ID") +
							" WHERE " + DBKernel.delimitL(tn) + "=" + value;														
				}
				else if (fn.equals("Risikogruppen")) {
					sql = "SELECT " + DBKernel.delimitL("ID") +
							"," + DBKernel.delimitL("Bezeichnung") + " FROM " + DBKernel.delimitL("Risikogruppen") +
							" LEFT JOIN " + DBKernel.delimitL("Krankheitsbilder_Risikogruppen") +
							" ON " + DBKernel.delimitL("Krankheitsbilder_Risikogruppen") + "." + DBKernel.delimitL("Risikogruppen") + "=" +
							DBKernel.delimitL("Risikogruppen") + "." + DBKernel.delimitL("ID") +
							" WHERE " + DBKernel.delimitL(tn) + "=" + value;														
				}
			}
	    	
			else if (tn.equals("Modellkatalog")) {
				String fn = myT.getFieldNames()[selectedColumn];
				if (fn.equals("Referenzen")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Erstautor") + "," + DBKernel.delimitL("Jahr") +
					" FROM " + DBKernel.delimitL("Literatur") +
					" LEFT JOIN " + DBKernel.delimitL("Modell_Referenz") + 
					" ON " + DBKernel.delimitL("Modell_Referenz") + "." + DBKernel.delimitL("Literatur") + "=" +
					DBKernel.delimitL("Literatur") + "." + DBKernel.delimitL("ID") +
					" LEFT JOIN " + DBKernel.delimitL(tn) + 
					" ON " + DBKernel.delimitL(tn) + "." + DBKernel.delimitL("ID") + "=" +
					DBKernel.delimitL("Modell_Referenz") + "." + DBKernel.delimitL("Modell") +
					" WHERE " + DBKernel.delimitL("Modell") + "=" + value;				
				}
				
				else if (fn.equals("Parameter")) {
					// CASE WHEN expr1 THEN v1[WHEN expr2 THEN v2] [ELSE v4] END
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Parametername") + "," +
							" CASE WHEN " + DBKernel.delimitL("Parametertyp") + "=1 THEN 'independent' " +
							" WHEN " + DBKernel.delimitL("Parametertyp") + "=2 THEN 'parameter' " +
							" WHEN " + DBKernel.delimitL("Parametertyp") + "=3 THEN 'dependent' " +
							" ELSE 'unknown' END" +
					" FROM " + DBKernel.delimitL("ModellkatalogParameter") +
					" LEFT JOIN " + DBKernel.delimitL(tn) + 
					" ON " + DBKernel.delimitL(tn) + "." + DBKernel.delimitL("Parameter") + "=" +
					DBKernel.delimitL("ModellkatalogParameter") + "." + DBKernel.delimitL("Modell") +
					" WHERE " + DBKernel.delimitL("Modell") + "=" + value;									
				}
				
			}
			
			else if (tn.equals("GeschaetzteModelle")) {
				String fn = myT.getFieldNames()[selectedColumn];
				if (fn.equals("Referenzen")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Erstautor") + "," + DBKernel.delimitL("Jahr") +
					" FROM " + DBKernel.delimitL("Literatur") +
					" LEFT JOIN " + DBKernel.delimitL("GeschaetztesModell_Referenz") + 
					" ON " + DBKernel.delimitL("GeschaetztesModell_Referenz") + "." + DBKernel.delimitL("Literatur") + "=" +
					DBKernel.delimitL("Literatur") + "." + DBKernel.delimitL("ID") +
					" LEFT JOIN " + DBKernel.delimitL(tn) + 
					" ON " + DBKernel.delimitL(tn) + "." + DBKernel.delimitL("ID") + "=" +
					DBKernel.delimitL("GeschaetztesModell_Referenz") + "." + DBKernel.delimitL("GeschaetztesModell") +
					" WHERE " + DBKernel.delimitL("GeschaetztesModell") + "=" + value;				
				}
				
				else if (fn.equals("GeschaetzteParameter")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Parametername") + "," + DBKernel.delimitL("Wert") +
					" FROM " + DBKernel.delimitL("ModellkatalogParameter") +
					" LEFT JOIN " + DBKernel.delimitL("GeschaetzteParameter") + 
					" ON " + DBKernel.delimitL("GeschaetzteParameter") + "." + DBKernel.delimitL("Parameter") + "=" +
					DBKernel.delimitL("ModellkatalogParameter") + "." + DBKernel.delimitL("ID") +
					" LEFT JOIN " + DBKernel.delimitL(tn) + 
					" ON " + DBKernel.delimitL(tn) + "." + DBKernel.delimitL("ID") + "=" +
					DBKernel.delimitL("GeschaetzteParameter") + "." + DBKernel.delimitL("GeschaetztesModell") +
					" WHERE " + DBKernel.delimitL("GeschaetztesModell") + "=" + value;									
				}
				else if (fn.equals("GueltigkeitsBereiche")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Parametername") + "," + DBKernel.delimitL("Gueltig_von") + "," + DBKernel.delimitL("Gueltig_bis") +
					" FROM " + DBKernel.delimitL("ModellkatalogParameter") +
					" LEFT JOIN " + DBKernel.delimitL("GueltigkeitsBereiche") + 
					" ON " + DBKernel.delimitL("GueltigkeitsBereiche") + "." + DBKernel.delimitL("Parameter") + "=" +
					DBKernel.delimitL("ModellkatalogParameter") + "." + DBKernel.delimitL("ID") +
					" LEFT JOIN " + DBKernel.delimitL(tn) + 
					" ON " + DBKernel.delimitL(tn) + "." + DBKernel.delimitL("ID") + "=" +
					DBKernel.delimitL("GueltigkeitsBereiche") + "." + DBKernel.delimitL("GeschaetztesModell") +
					" WHERE " + DBKernel.delimitL("GeschaetztesModell") + "=" + value;									
				}
			}
			else if (tn.equals("Zutatendaten")) {
				String fn = myT.getFieldNames()[selectedColumn];
				if (fn.equals("Kosten")) sql = getKostenSQL(tn, value);
				else sql = getSonstigeParameterSQL(tn, value);
			}
			else if (tn.equals("Versuchsbedingungen") || tn.equals("Messwerte")) {
				sql = getSonstigeParameterSQL(tn, value);
			}
			else if (tn.equals("Prozessdaten")) {
				String fn = myT.getFieldNames()[selectedColumn];
				if (fn.equals("Sonstiges")) sql = getSonstigeParameterSQL(tn, value);
				else if (fn.equals("Kosten")) sql = getKostenSQL(tn, value);
				else if (fn.equals("Tenazitaet")) {
					sql = "SELECT " + DBKernel.delimitL("Agensname") + "," + DBKernel.delimitL("DoubleKennzahlen") + "." + DBKernel.delimitL("Wert") + "," +
					DBKernel.delimitL("DoubleKennzahlen") + "." + DBKernel.delimitL("Wert") +
					",IFNULL(" + DBKernel.delimitL("Einheiten") + "." + DBKernel.delimitL("Einheit") + ",'') FROM " + DBKernel.delimitL("Agenzien") +
					" LEFT JOIN " + DBKernel.delimitL("Prozessdaten_Messwerte") +
					" ON " + DBKernel.delimitL("Prozessdaten_Messwerte") + "." + DBKernel.delimitL("Agens") + "=" +
					DBKernel.delimitL("Agenzien") + "." + DBKernel.delimitL("ID") +
					" LEFT JOIN " + DBKernel.delimitL("DoubleKennzahlen") +
					" ON " + DBKernel.delimitL("Prozessdaten_Messwerte") + "." + DBKernel.delimitL("Konzentration") + "=" +
					DBKernel.delimitL("DoubleKennzahlen") + "." + DBKernel.delimitL("ID") +
					" LEFT JOIN " + DBKernel.delimitL("Einheiten") +
					" ON " + DBKernel.delimitL("Prozessdaten_Messwerte") + "." + DBKernel.delimitL("Einheit") + "=" +
					DBKernel.delimitL("Einheiten") + "." + DBKernel.delimitL("ID") +
					" WHERE " + DBKernel.delimitL(tn) + "=" + value;		
				}
				else if (fn.equals("Referenz")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Erstautor") + "," + DBKernel.delimitL("Jahr") +
					" FROM " + DBKernel.delimitL("Literatur") +
					" LEFT JOIN " + DBKernel.delimitL("Prozessdaten_Literatur") +
					" ON " + DBKernel.delimitL("Prozessdaten_Literatur") + "." + DBKernel.delimitL("Literatur") + "=" +
					DBKernel.delimitL("Literatur") + "." + DBKernel.delimitL("ID") +
					" WHERE " + DBKernel.delimitL("Prozessdaten_Literatur") + "." + DBKernel.delimitL(tn) + "=" + value;				
				}				
			}
			else if (tn.equals("ProzessWorkflow")) {
				String fn = myT.getFieldNames()[selectedColumn];
				if (fn.equals("Referenz")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Erstautor") + "," + DBKernel.delimitL("Jahr") +
					" FROM " + DBKernel.delimitL("Literatur") +
					" LEFT JOIN " + DBKernel.delimitL("ProzessWorkflow_Literatur") +
					" ON " + DBKernel.delimitL("ProzessWorkflow_Literatur") + "." + DBKernel.delimitL("Literatur") + "=" +
					DBKernel.delimitL("Literatur") + "." + DBKernel.delimitL("ID") +
					" WHERE " + DBKernel.delimitL("ProzessWorkflow_Literatur") + "." + DBKernel.delimitL(tn) + "=" + value;				
				}
			}
			else if (tn.equals("Knoten")) {
				String fn = myT.getFieldNames()[selectedColumn];
				if (fn.equals("Erregernachweis")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Agensname") + "," +
					DBKernel.delimitL("AnzahlLabornachweise") + " FROM " + DBKernel.delimitL("Knoten_Agenzien") +
					" LEFT JOIN " + DBKernel.delimitL("Agenzien") +
					" ON " + DBKernel.delimitL("Knoten_Agenzien") + "." + DBKernel.delimitL("Erreger") + "=" +
					DBKernel.delimitL("Agenzien") + "." + DBKernel.delimitL("ID") +
					" WHERE " + DBKernel.delimitL(tn) + "=" + value;
				}
			}
			else if (tn.equals("Produktkatalog")) {
				String fn = myT.getFieldNames()[selectedColumn];
				if (fn.equals("Matrices")) {
					sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Matrixname") +
					" FROM " + DBKernel.delimitL("Produktkatalog_Matrices") +
					" LEFT JOIN " + DBKernel.delimitL("Matrices") +
					" ON " + DBKernel.delimitL("Produktkatalog_Matrices") + "." + DBKernel.delimitL("Matrix") + "=" +
					DBKernel.delimitL("Matrices") + "." + DBKernel.delimitL("ID") +
					" WHERE " + DBKernel.delimitL(tn) + "=" + value;
				}
			}
		}
		else {
			result = value.toString(); 			
		}
		if (sql.length() > 0) {
			try {
				ResultSet rs = DBKernel.getResultSet(sql, false);
				//System.err.println(rs + "\t" + sql);
				if (rs != null && rs.first()) {
					do {
						if (selectedColumn==13 && myT.getTablename().equals("Modell")) {
							// Exposition?
							result += rs.getString(1);
							if (rs.getString(2) != null)  result += ": " +  rs.getString(2) + "\n";								
						}
						else if (selectedColumn==10 && myT.getTablename().equals("GeschaetzteModelle")) {
							// estimated parameters
							String refinedNumber = "";
							Object dbl = rs.getObject(3);
							if (dbl != null) refinedNumber = " = " + DBKernel.getDoubleStr(dbl);		
							result += rs.getString(2) + refinedNumber + "\n";							
						}
						else if (selectedColumn==12 && myT.getTablename().equals("GeschaetzteModelle")) {
							// range of validity
							String refinedNumber = "";
							Object dbl = rs.getObject(3);
							if (dbl != null) refinedNumber = " [" + DBKernel.getDoubleStr(dbl);		
							Object dbl2 = rs.getObject(4);
							if (dbl2 != null) refinedNumber += ", " + DBKernel.getDoubleStr(dbl2) + "]";		
							result += rs.getString(2) + refinedNumber + "\n";							
						}
						else if (myT.getFieldNames()[selectedColumn].equals("Kosten")) { //  && myT.getTablename().equals("Prozessdaten")
							String refinedNumber = "";
							Object dbl = rs.getObject(2);
							if (dbl != null) refinedNumber = DBKernel.getDoubleStr(dbl);		
							result += rs.getString(1) + ": " + refinedNumber + " " + rs.getString(3) + "\n";							
						}
						else if (isINTmn) {
							String res = "";
							int numCols = rs.getMetaData().getColumnCount();
							for (int i=1;i<=numCols;i++) {
								if (rs.getString(i) != null) res += " ; " + rs.getString(i);
							}
							if (res.length() > 0) res = res.substring(3);
							if (numCols == 1) result += res + "; "; // es gibt wohl nur die ID
							else result += res + "\n";
						}
						/*
						else if (isDBLmn) { // value instanceof Double
							Object dblo = rs.getObject(2);
							if (dblo == null) { // Verteilung, Funktion (Zeit), Funktion (?)
								result += rs.getString(1) + ": " + rs.getString(3) + "\n";
							}
							else {
								Double dbl = rs.getDouble(2);
								NumberFormat f = NumberFormat.getInstance(Locale.US);
								f.setGroupingUsed(false);
								String refinedNumber = f.format(dbl);
								result += rs.getString(1) + ": " + refinedNumber + "\n";								
							}
						}
						*/
						else { // Integer
							int numCols = rs.getMetaData().getColumnCount(); 
							if (numCols == 1) {
								result += rs.getString(1) + "\n";
							}
							else if (numCols == 3) { // Artikel_Lieferung
								result += rs.getString(2) + "; " + rs.getString(3) + "\n";
							}
							else if (numCols == 4) { // Sonstiges mit Kennzahl und Wert und Einheit
								String refinedNumber = "";
								Object dbl = rs.getObject(3);
								if (dbl != null) refinedNumber = ": " + DBKernel.getDoubleStr(dbl) + " " + rs.getString(4);		
								result += rs.getString(1) + refinedNumber + "\n";
							}
							else {
								result += rs.getString(2) + "\n";								
							}
						}
					} while (rs.next());					
				}
			}
			catch (Exception e) {
				MyLogger.handleException(e);
			} 					
		}
		this.setText(result);
		if (theValues.containsKey(value)) theValues.remove(value);
		theValues.put(value, result);
		if (lastUpdate.containsKey(value)) lastUpdate.remove(value);
		lastUpdate.put(value, System.currentTimeMillis());
	}
	private String getSonstigeParameterSQL(String tn, Object value) {
		String sql = "SELECT " + DBKernel.delimitL("Parameter") + "," + DBKernel.delimitL("DoubleKennzahlen") + "." + DBKernel.delimitL("Wert") + "," +
		DBKernel.delimitL("DoubleKennzahlen") + "." + DBKernel.delimitL("Wert") +
		",IFNULL(" + DBKernel.delimitL("Einheiten") + "." + DBKernel.delimitL("Einheit") + ",'') FROM " + DBKernel.delimitL("SonstigeParameter") +
		" LEFT JOIN " + DBKernel.delimitL(tn + "_Sonstiges") +
		" ON " + DBKernel.delimitL(tn + "_Sonstiges") + "." + DBKernel.delimitL("SonstigeParameter") + "=" +
		DBKernel.delimitL("SonstigeParameter") + "." + DBKernel.delimitL("ID") +
		" LEFT JOIN " + DBKernel.delimitL("DoubleKennzahlen") +
		" ON " + DBKernel.delimitL(tn + "_Sonstiges") + "." + DBKernel.delimitL("Wert") + "=" +
		DBKernel.delimitL("DoubleKennzahlen") + "." + DBKernel.delimitL("ID") +
		" LEFT JOIN " + DBKernel.delimitL("Einheiten") +
		" ON " + DBKernel.delimitL(tn + "_Sonstiges") + "." + DBKernel.delimitL("Einheit") + "=" +
		DBKernel.delimitL("Einheiten") + "." + DBKernel.delimitL("ID") +
		" WHERE " + DBKernel.delimitL(tn) + "=" + value;				
		return sql;
	}
	private String getKostenSQL(String tn, Object value) {
		String sql = "SELECT " + DBKernel.delimitL("Kostenunterart") +
		 "," + DBKernel.delimitL("DoubleKennzahlen") + "." + DBKernel.delimitL("Wert") + "," +DBKernel.delimitL("Einheit") +
		" FROM " + DBKernel.delimitL("Kostenkatalog") +
		" LEFT JOIN " + DBKernel.delimitL(tn + "_Kosten") +
		" ON " + DBKernel.delimitL(tn + "_Kosten") + "." + DBKernel.delimitL("Kostenkatalog") + "=" +
		DBKernel.delimitL("Kostenkatalog") + "." + DBKernel.delimitL("ID") +
		" LEFT JOIN " + DBKernel.delimitL("DoubleKennzahlen") +
		" ON " + DBKernel.delimitL(tn + "_Kosten") + "." + DBKernel.delimitL("Menge") + "=" +
		DBKernel.delimitL("DoubleKennzahlen") + "." + DBKernel.delimitL("ID") +
		" WHERE " + DBKernel.delimitL(tn + "_Kosten") + "." + DBKernel.delimitL(tn) + "=" + value;	
		return sql;
	}

	public void addActionListener(ActionListener arg0) {
	}

	public JComponent getComponent() {
		return this;
	}

	public Object getValue() {
		return null;
	}
}
