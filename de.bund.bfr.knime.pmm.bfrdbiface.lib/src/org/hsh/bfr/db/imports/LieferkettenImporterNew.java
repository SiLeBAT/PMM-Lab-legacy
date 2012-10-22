/**
 * 
 */
package org.hsh.bfr.db.imports;


/**
 * 
 */

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;

/**
 * @author Armin
 *
 */
public class LieferkettenImporterNew extends FileFilter implements MyImporter {
  /**
  This is the one of the methods that is declared in 
  the abstract class
 */
	public boolean accept(File f) {
	  if (f.isDirectory()) return true;
	
	  String extension = getExtension(f);
	  if ((extension.equals("xls"))) return true; 
	  return false;
	}
	  
	public String getDescription() {
	    return "New Lieferketten Datei (*.xls)";
	}
	
	private String getExtension(File f) {
	  String s = f.getName();
	  int i = s.lastIndexOf('.');
	  if (i > 0 &&  i < s.length() - 1) return s.substring(i+1).toLowerCase();
	  return "";
	}


	public void doImport(final String filename, final JProgressBar progress, final boolean showResults) {
  	Runnable runnable = new Runnable() {
      public void run() {
		    try {
      		if (progress != null) {
      			progress.setVisible(true);
      			progress.setStringPainted(true);
      			progress.setString("Importiere Lieferketten Datei...");
      			progress.setMinimum(0);
      		}

      		InputStream is = null;
		    	if (filename.startsWith("http://")) {
		    		URL url = new URL(filename);
		    		URLConnection uc = url.openConnection();
		    		is = uc.getInputStream();
		    	}
		    	else if (filename.startsWith("/org/hsh/bfr/db/res/")) {
						is = getClass().getResourceAsStream(filename);		    		
		    	}
		    	else {
		    		is = new FileInputStream(filename);
		    	}
		
		    	POIFSFileSystem fs = new POIFSFileSystem(is);
		      HSSFWorkbook wb = new HSSFWorkbook(fs);
		      HSSFSheet sheet;
		      HSSFRow row;

		      int numSuccess = 0;
		      int numFails = 0;
		      sheet = wb.getSheetAt(0); 
				  int numRows = sheet.getLastRowNum() + 1;
				  progress.setMaximum(numRows);
	      	progress.setValue(0);
				      
	      	for (int i=1;i<numRows;i++) {
			    row = sheet.getRow(i);
				if (row != null) {
				      String lfdNr = getStrVal(row.getCell(0));
				      if (lfdNr.trim().length() == 0) {
				      	break;
				      }
				      String BL0 = getStrVal(row.getCell(1));
				      String KP = getStrVal(row.getCell(2));
				      String firma1 = getStrVal(row.getCell(3));
				      String adress1 = getStrVal(row.getCell(4));
				      String plz1 = getStrVal(row.getCell(5));
				      String ort1 = getStrVal(row.getCell(6));
				      String BL1 = getStrVal(row.getCell(7));
				      String bn1 = getStrVal(row.getCell(8));
				      
				      String LM1 = getStrVal(row.getCell(9));
				      String an1 = getStrVal(row.getCell(10));
				      String ld1 = punktAnsDatum(getStrVal(row.getCell(11)));
				      String m11 = getStrVal(row.getCell(12));
				      String m12 = getStrVal(row.getCell(13));
				      String m13 = getStrVal(row.getCell(14));
				      String m14 = getStrVal(row.getCell(15));
				      String m15 = getStrVal(row.getCell(16));
				      String m16 = getStrVal(row.getCell(17));
				      if (m16.endsWith(".")) m16 = m16.substring(0, m16.length() - 1); // keine Abkürzungszeichen erlaubt!
				      String cn1 = getStrVal(row.getCell(18));
				      String mhd1 = getStrVal(row.getCell(19));
				      String fi1 = getStrVal(row.getCell(20));

				      String firma2 = getStrVal(row.getCell(21));
				      String adress2 = getStrVal(row.getCell(22));
				      String plz2 = getStrVal(row.getCell(23));
				      String ort2 = getStrVal(row.getCell(24));
				      String BL2 = getStrVal(row.getCell(25));
				      String bn2 = getStrVal(row.getCell(26));

				      String LM2 = getStrVal(row.getCell(27));
				      String an2 = getStrVal(row.getCell(28));
				      String ld2 = punktAnsDatum(getStrVal(row.getCell(29)));
				      String m21 = getStrVal(row.getCell(30));
				      String m22 = getStrVal(row.getCell(31));
				      String m23 = getStrVal(row.getCell(32));
				      String m24 = getStrVal(row.getCell(33));
				      String m25 = getStrVal(row.getCell(34));
				      String m26 = getStrVal(row.getCell(35));
				      if (m26.endsWith(".")) m26 = m26.substring(0, m26.length() - 1); // keine Abkürzungszeichen erlaubt!
				      String cn2 = getStrVal(row.getCell(36));
				      String mhd2 = getStrVal(row.getCell(37));
				      String fi2 = getStrVal(row.getCell(38));

				      String firma3 = getStrVal(row.getCell(39));
				      String adress3 = getStrVal(row.getCell(40));
				      String plz3 = getStrVal(row.getCell(41));
				      String ort3 = getStrVal(row.getCell(42));
				      String BL3 = getStrVal(row.getCell(43));
				      String bn3 = getStrVal(row.getCell(44));

				      Integer c1 = null;
				      Integer c2 = null;
				      if (firma2.trim().length() > 0) c1 = getCharge(firma2, adress2, plz2, ort2, BL2, bn2, an1, cn1, mhd1, fi1, LM1, ld1, m11, m12, m13, m14, m15, m16, firma1, adress1, plz1, ort1, BL1, bn1);
				      if (firma3.trim().length() > 0) c2 = getCharge(firma3, adress3, plz3, ort3, BL3, bn3, an2, cn2, mhd2, "", LM2, ld2, m21, m22, m23, m24, m25, m26, firma2, adress2, plz2, ort2, BL2, bn2);
				      if (c1 == null) {
				      	System.err.println("Fehlerchenchen_1!!");
				      	numFails++;
				      }
				      else if (c2 == null) {
				      	System.err.println("Fehlerchenchen_2!!");
				      	numFails++;
				      }
				      else {
				      	if (c2 != null) {
					      	if (c1.intValue() == c2.intValue()) {
					      		System.err.println("Fehlerchenchen_3!!");
						      	numFails++;
					      	}
					      	else if (getID("Lieferung_Lieferungen",
		    							new String[]{"Artikel_Lieferung","Vorprodukt"},
		    							new String[]{c1.toString(), c2.toString()},
		    							null) == null) {
						      	System.err.println("Fehlerchenchen_4!!");
						      	numFails++;
						      }
						      else {
						      	numSuccess++;
						      }
					      }
				    }
	      	}
	      }

			    DBKernel.doMNs(DBKernel.myList.getTable("Produzent_Artikel"));
			    DBKernel.doMNs(DBKernel.myList.getTable("Artikel_Lieferung"));
    			if (progress != null) {
  	  			// Refreshen:
    				MyDBTable myDB = DBKernel.myList.getMyDBTable();
    				if (myDB.getActualTable() != null) {
	    				String actTablename = myDB.getActualTable().getTablename();
	    				if (actTablename.equals("Produzent_Artikel") || actTablename.equals("Artikel_Lieferung")) {
	    					myDB.setTable(myDB.getActualTable());
	    				}
    				}
    				progress.setVisible(false);
    			}
    			if (showResults) {
    				String log = numSuccess + " erfolgreiche Importe.\n";
    				log += numFails + " fehlgeschlagene Importe.\n";
    				InfoBox ib = new InfoBox(log, true, new Dimension(300, 150), null);
    				ib.setVisible(true);    				
    			}
		    }
		    catch (Exception e) {MyLogger.handleException(e);}
      }
    };
    
    Thread thread = new Thread(runnable);
    thread.start();
    try {
			thread.join();
		}
    catch (InterruptedException e) {
    	MyLogger.handleException(e);
		}
  }
	
	private String punktAnsDatum(String datum) {
		String result = datum;
		// 15.05, 02.05.2011
	    if (result.trim().length() > 0 && !result.endsWith(".") && result.indexOf(".", result.indexOf(".") + 1) < 0) result += ".";
	    return result;  
	}
  private String getStrVal(HSSFCell cell) {
  	String result = "";
		if (cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
		}
		else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			result = cell.getStringCellValue();
		}
		else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
			double dbl = cell.getNumericCellValue();
			if (Math.round(dbl) == dbl) result = "" + ((int) dbl);
			else result = "" + cell.getNumericCellValue();
		}
		else {
			result = cell.toString();
		}
  	return result;
  }
	private Integer getCharge(String firma2, String adress2, String plz2, String ort2, String bl2
			, String bn2, String an1, String cn1, String mhd1, String fi1, String LM1, String ld1
			, String m11, String m12, String m13, String m14, String m15, String m16,
			String firma1, String adress1, String plz1, String ort1, String bl1
			, String bn1) {
		Integer result = null;
    Integer lastID = getID("Kontakte",
    		new String[]{"Name","Strasse","PLZ","Ort","Bundesland"},
    		new String[]{firma2, adress2, plz2, ort2, getBL(bl2)},
    		null);
		if (lastID != null) {
			lastID = getID("Produzent",
					new String[]{"Kontaktadresse","Betriebsnummer"},
					new String[]{lastID.toString(), bn2},
					null);
			if (lastID != null) {
					lastID = getID("Produzent_Artikel",
							new String[]{"Produzent","Artikelnummer","Bezeichnung"},
							new String[]{lastID.toString(), an1, LM1},
							new boolean[]{true,true,true});
					if (lastID != null) {
						Integer empf = null;
						if (firma1.trim().length() > 0) {
							empf = getID("Kontakte",
					    		new String[]{"Name","Strasse","PLZ","Ort","Bundesland"},
					    		new String[]{firma1, adress1, plz1, ort1, getBL(bl1)},
					    		null);
							if (empf != null) {
								empf = getID("Produzent",
										new String[]{"Kontaktadresse","Betriebsnummer"},
										new String[]{empf.toString(), bn1},
										null);
							}
						}
						if (cn1.trim().length() == 0) cn1 = an1 + ", " + mhd1;
						if (fi1.trim().length() == 1) cn1 += " (" + fi1 + ")"; // wenn == 1, dann ist es wohl sowas wie eine identische Lieferung...hoffentlich
						lastID = getID("Artikel_Lieferung",
								new String[]{"Artikel","ChargenNr","MHD","Lieferdatum","#Units1","BezUnits1","#Units2","BezUnits2","Unitmenge","UnitEinheit","Empfänger"},
								new String[]{lastID.toString(), cn1, mhd1, ld1, m11, m12, m13, m14, m15, m16, empf == null ? null : empf.toString()},
								null);
						if (lastID != null) {
							result = lastID;
						}
					}
			}
		}
		return result;
	}
	private String getBL(String bl) {
		return bl;
		/*
		if (bl.equalsIgnoreCase("TH")) return "Thüringen";
		else if (bl.equalsIgnoreCase("SH")) return "Schleswig-Holstein";
		else if (bl.equalsIgnoreCase("HH")) return "Hamburg";
		else if (bl.equalsIgnoreCase("NI")) return "Niedersachsen";
		else if (bl.equalsIgnoreCase("HB")) return "Bremen";
		else if (bl.equalsIgnoreCase("NW")) return "Nordrhein-Westfalen";
		else if (bl.equalsIgnoreCase("HE")) return "Hessen";
		else if (bl.equalsIgnoreCase("RP")) return "Rheinland-Pfalz";
		else if (bl.equalsIgnoreCase("BW")) return "Baden-Württemberg";
		else if (bl.equalsIgnoreCase("BY")) return "Bayern";
		else if (bl.equalsIgnoreCase("SL")) return "Saarland";
		else if (bl.equalsIgnoreCase("BE")) return "Berlin";
		else if (bl.equalsIgnoreCase("BB")) return "Brandenburg";
		else if (bl.equalsIgnoreCase("MV")) return "Mecklenburg-Vorpommern";
		else if (bl.equalsIgnoreCase("SN")) return "Sachsen";
		else if (bl.equalsIgnoreCase("ST")) return "Sachsen-Anhalt";
		else return bl;
		*/
	}
	private Integer getID(String tablename, String[] feldnames, String[] feldVals, boolean[] key) {
	  Integer result = null;
	  String sql = "SELECT " + DBKernel.delimitL("ID") + " FROM " + DBKernel.delimitL(tablename) + " WHERE ";
	  String fns = "";
	  String fvs = "";
	  for (int i=0;i<feldnames.length;i++) {
	  	if (feldVals[i] != null) {
		  	feldVals[i] = feldVals[i].replaceAll("'", "\\apos");
		  	fns += "," + DBKernel.delimitL(feldnames[i]);
		  	fvs += ",'" + feldVals[i] + "'";
			if (key == null || key[i]) {
				sql += DBKernel.delimitL(feldnames[i]) + "='" + feldVals[i] + "' AND ";	  
				if (feldVals[i].trim().length() == 0 && key != null && i + 1 < key.length) key[i+1] = true; // wichtig, falls die Artikelnummer nicht angegeben ist -> dann soll die Bezeichnung der Identifier sein
			}
	  	}
	  }
	  ResultSet rs = null;
		  sql = sql.substring(0, sql.length() - 5);
		  rs = DBKernel.getResultSet(sql, true);		  
		try {
			if (rs != null && rs.last() && rs.getRow() == 1) result = rs.getInt(1);

			if (result == null) {
				/*
				if (fvs.substring(1).indexOf("305") >= 0) {
					System.err.println();
				}
				*/
				sql = "INSERT INTO " + DBKernel.delimitL(tablename) +
    		" (" + fns.substring(1) +	") VALUES (" + fvs.substring(1) + ")";
				/*
			  if (tablename.equals("Kontakte")) {
			  	System.out.println(sql);
			  }
			  */
	      PreparedStatement ps = DBKernel.getDBConnection().prepareStatement(sql,
	      		Statement.RETURN_GENERATED_KEYS);					
				if (ps.executeUpdate() > 0) result = DBKernel.getLastInsertedID(ps);
			}
			else {
				//System.out.println(result + "\t" + sql);
			}
		}
		catch (Exception e) {MyLogger.handleException(e);}
		
		return result;
	}
}
