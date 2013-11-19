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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyDBTables;
import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;

/**
 * @author Armin
 *
 */
public class LieferkettenImporterEFSA extends FileFilter implements MyImporter {
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
				      String lfdNr = getStrVal(row.getCell(0)); // Serial_number
				      if (lfdNr.trim().length() == 0) {
				      	//continue;//break;
				      }
				      //String BL0 = getStrVal(row.getCell(1)); // Contact_Region
				      //String KP = getStrVal(row.getCell(2)); // Contact_person
				      String firma1 = getStrVal(row.getCell(3)); // Name_Receiver
				      String adress1 = getStrVal(row.getCell(4)); // Address_ Receiver
				      String plz1 = getStrVal(row.getCell(5)); // Postcode_Receiver
				      String ort1 = getStrVal(row.getCell(6)); // Town_Receiver
				      String BL1 = getStrVal(row.getCell(7)); // Region_Receiver
				      String country1 = getStrVal(row.getCell(8)); // Country_Reciever
				      String vat1 = getStrVal(row.getCell(9)); // VATNo_Reciever
				      
				      String bez1 = getStrVal(row.getCell(10)); // ProductName_Receiver
				      String proc1 = getStrVal(row.getCell(11)); // Processing_BusinessInspected
				      String use1 = getStrVal(row.getCell(12)); // IntendedUse_BusinessInspected
				      String num1 = getStrVal(row.getCell(13)); // ProductNo_BusinessInspected
				      String date1 = getStrVal(row.getCell(14)); // Date_To_Receiver
				      String m11 = getStrVal(row.getCell(15)); // No_LPU_BusinessInspected
				      String m21 = getStrVal(row.getCell(16)); // Type_ LPU_BusinessInspected
				      String m31 = getStrVal(row.getCell(17)); // No_ SPU_BusinessInspected
				      String m41 = getStrVal(row.getCell(18)); // Type_ SPU_BusinessInspected
				      String m51 = getStrVal(row.getCell(19)); // Amount_ SPU_BusinessInspected
				      String m61 = getStrVal(row.getCell(20)); // Amount_Unit_BusinessInspected
				      if (m61.endsWith(".")) m61 = m61.substring(0, m61.length() - 1); // keine Abkürzungszeichen erlaubt!
				      String lot1 = getStrVal(row.getCell(21)); // LotNo_BusinessInspected
				      String exp1 = getStrVal(row.getCell(22)); // ExpirationDate_BusinessInspected
				      String prod1 = "";
				      if (exp1.toLowerCase().indexOf("mhd") >= 0 || exp1.toLowerCase().indexOf("date") >= 0) {
				    	  exp1 = ""; prod1 = "";
				      }
				      else {
					      if (exp1.toLowerCase().startsWith("best before")) exp1 = exp1.substring(11).trim();
					      if (exp1.toLowerCase().startsWith("production")) {
					    	  prod1 = exp1.substring(10).trim();
					    	  exp1 = "";
					      }
				      }
				      if (exp1.toLowerCase().indexOf("production") > 0) exp1 = "";
				      String info1 = getStrVal(row.getCell(23)); // Source_Info_BusinessInspected

				      String firma2 = getStrVal(row.getCell(24)); // Name_BusinessInspected
				      String adress2 = getStrVal(row.getCell(25)); // Address_BusinessInspected
				      String plz2 = getStrVal(row.getCell(26)); // Postcode_BusinessInspected
				      String ort2 = getStrVal(row.getCell(27)); // Town_BusinessInspected
				      String BL2 = getStrVal(row.getCell(28)); // Region_BusinessInspected
				      String country2 = getStrVal(row.getCell(29)); // Country_BusinessInspected
				      String vat2 = getStrVal(row.getCell(30)); // VATNo_BusinessInspected

				      String bez2 = getStrVal(row.getCell(31)); // ProductName_Supplier
				      String num2 = getStrVal(row.getCell(32)); // ProductNo_Supplier
				      String date2 = getStrVal(row.getCell(33)); // Date_From_Supplier
				      String m12 = getStrVal(row.getCell(34)); // No_LPU_Supplier
				      String m22 = getStrVal(row.getCell(35)); // Type_ LPU_Supplier
				      String m32 = getStrVal(row.getCell(36)); // No_ SPU_Supplier
				      String m42 = getStrVal(row.getCell(37)); // Type_ SPU_Supplier
				      String m52 = getStrVal(row.getCell(38)); // Amount_ SPU_Supplier
				      String m62 = getStrVal(row.getCell(39)); // Amount_Unit_Supplier
				      if (m62.endsWith(".")) m62 = m62.substring(0, m62.length() - 1); // keine Abkürzungszeichen erlaubt!
				      String lot2 = getStrVal(row.getCell(40)); // LotNo_Supplier
				      String exp2 = getStrVal(row.getCell(41)); // ExpirationDate_Supplier
				      String prod2 = "";
				      if (exp2.toLowerCase().indexOf("mhd") >= 0 || exp2.toLowerCase().indexOf("date") >= 0) {
				    	  exp2 = ""; prod2 = "";
				      }
				      else {
					      if (exp2.toLowerCase().startsWith("best before")) exp2 = exp2.substring(11).trim();
					      if (exp2.toLowerCase().startsWith("production")) {
					    	  prod2 = exp2.substring(10).trim();
					    	  exp2 = "";
					      }
				      }
				      if (exp2.toLowerCase().indexOf("production") > 0) exp2 = "";
				      String info2 = getStrVal(row.getCell(42)); // Source_Info_Supplier

				      String firma3 = getStrVal(row.getCell(43)); // Name_Supplier
				      String adress3 = getStrVal(row.getCell(44)); // Address_ Supplier
				      String plz3 = getStrVal(row.getCell(45)); // Postcode_Supplier
				      String ort3 = getStrVal(row.getCell(46)); // Town_Supplier
				      String BL3 = getStrVal(row.getCell(47)); // Region_Supplier
				      String country3 = getStrVal(row.getCell(48)); // Country_Supplier
				      String vat3 = getStrVal(row.getCell(49)); // VATNo_Supplier
				      
				      //String ec = getStrVal(row.getCell(50)); // EndChain
				      //String ece = getStrVal(row.getCell(51)); // Explanation_EndChaim
				      //String rem = getStrVal(row.getCell(52)); // Remarks
				      //String oc = getStrVal(row.getCell(53)); // OriginCountry
				      //String cq = getStrVal(row.getCell(54)); // Contact_Questions
				      //String ft = getStrVal(row.getCell(55)); // Further_Traceback
				      //String ms = getStrVal(row.getCell(56)); // MicrobiologicalSample

				      Integer c1 = null;
				      Integer c2 = null;
				      if (firma2.trim().length() > 0) c1 = getCharge_Lieferung(firma2, adress2, plz2, ort2, BL2, country2, num1, lot1, exp1, info1, bez1, date1, m11, m21, m31, m41, m51, m61, firma1, adress1, plz1, ort1, BL1, country1, proc1, use1, true);
				      if (firma3.trim().length() > 0) c2 = getCharge_Lieferung(firma3, adress3, plz3, ort3, BL3, country3, num2, lot2, exp2, info2, bez2, date2, m12, m22, m32, m42, m52, m62, firma2, adress2, plz2, ort2, BL2, country2, "", "", false);
				      if (c1 == null) {
				      	System.err.println("Fehlerchenchen_1!!");
				      	numFails++;
				      }
				      else if (c2 == null) {
				      	System.err.println("Fehlerchenchen_2!! E.g. Station not defined?");
				      	getCharge_Lieferung(firma3, adress3, plz3, ort3, BL3, country3, num2, lot2, exp2, info2, bez2, date2, m12, m22, m32, m42, m52, m62, firma2, adress2, plz2, ort2, BL2, country2, "", "", false);
				      	numFails++;
				      }
				      else {
				      	if (c2 != null) {
					      	if (c1.intValue() == c2.intValue()) {
					      		System.err.println("Fehlerchenchen_3!!");
						      	numFails++;
					      	}
					      	else if (getID("ChargenVerbindungen",
		    							new String[]{"Zutat","Produkt"},
		    							new String[]{c2.toString(), c1.toString()},
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

	      		DBKernel.doMNs(MyDBTables.getTable("Station"));
			    DBKernel.doMNs(MyDBTables.getTable("Produktkatalog"));
			    DBKernel.doMNs(MyDBTables.getTable("Chargen"));
			    DBKernel.doMNs(MyDBTables.getTable("Lieferungen"));
    			if (progress != null) {
  	  			// Refreshen:
    				MyDBTable myDB = DBKernel.myList.getMyDBTable();
    				if (myDB.getActualTable() != null) {
	    				String actTablename = myDB.getActualTable().getTablename();
	    				if (actTablename.equals("Produktkatalog") || actTablename.equals("Lieferungen")
	    						 || actTablename.equals("Station") || actTablename.equals("Chargen")) {
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
	private Integer getCharge_Lieferung(String firma2, String adress2, String plz2, String ort2, String bl2
			, String country2, String an1, String cn1, String mhd1, String info1, String LM1, String ld1
			, String m11, String m12, String m13, String m14, String m15, String m16,
			String firma1, String adress1, String plz1, String ort1, String bl1
			, String country1, String proc1, String use1, boolean returnCharge) {
		Integer result = null;

			try {
			    String dtf = determineDateFormat(mhd1);
				if (dtf != null) mhd1 = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat(dtf).parse(mhd1));
			    dtf = determineDateFormat(ld1);
			    if (dtf != null) ld1 = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat(dtf).parse(ld1));
				if (dtf == null && !ld1.isEmpty()) {
					System.err.println("");
				}
			}
	      catch (ParseException e) {
				e.printStackTrace();
			}
		Integer lastID = getID("Station",
					new String[]{"Name","Strasse","PLZ","Ort","Bundesland","Land"},
					new String[]{firma2, adress2, plz2, ort2, bl2, country2},
					null);
			if (lastID != null) {
					lastID = getID("Produktkatalog",
							new String[]{"Station","Artikelnummer","Bezeichnung","Prozessierung","IntendedUse"},
							new String[]{lastID.toString(), an1, LM1, proc1, use1},
							null);
					if (lastID != null) {
						lastID = getID("Chargen",
								new String[]{"Artikel","ChargenNr","MHD"},
								new String[]{lastID.toString(), cn1, mhd1},
								null);
						if (returnCharge) result = lastID;
						if (lastID != null) {
							Integer empf = null;
							if (firma1.trim().length() > 0) {
								empf = getID("Station",
										new String[]{"Name","Strasse","PLZ","Ort","Bundesland","Land"},
										new String[]{firma1, adress1, plz1, ort1, bl1, country1},
										null);
							}
							if (cn1.trim().length() == 0) cn1 = an1 + "; " + mhd1;
							lastID = getID("Lieferungen",
									new String[]{"Charge","Lieferdatum","#Units1","BezUnits1","#Units2","BezUnits2","Unitmenge","UnitEinheit","Empfänger"},
									new String[]{lastID.toString(), ld1, m11, m12, m13, m14, m15, m16, empf == null ? null : empf.toString()},
									null);
							if (!returnCharge) result = lastID;
						}
					}
			}
		return result;
	}
	private Integer getID(String tablename, String[] feldnames, String[] feldVals, boolean[] key) {
	  Integer result = null;
	  String sql = "SELECT " + DBKernel.delimitL("ID") + " FROM " + DBKernel.delimitL(tablename) + " WHERE TRUE";
	  String fns = "";
	  String fvs = "";
	  for (int i=0;i<feldnames.length;i++) {
	  	if (feldVals[i] != null && feldVals[i].trim().length() > 0) {
		  	feldVals[i] = feldVals[i].replaceAll("'", "\\apos");
		  	fns += "," + DBKernel.delimitL(feldnames[i]);
		  	fvs += ",'" + feldVals[i] + "'";
			if (key == null || key[i]) {
				sql += " AND " + DBKernel.delimitL(feldnames[i]) + "='" + feldVals[i] + "'";	  
				if (feldVals[i].trim().length() == 0 && key != null && i + 1 < key.length) key[i+1] = true; // wichtig, falls die Artikelnummer nicht angegeben ist -> dann soll die Bezeichnung der Identifier sein
			}
	  	}
	  }
	  if (!fns.isEmpty() && !fvs.isEmpty()) {
		  ResultSet rs = null;
		  rs = DBKernel.getResultSet(sql, true);		  
		  try {
				if (rs != null && rs.last() && rs.getRow() == 1) result = rs.getInt(1);

				if (result == null) {
					sql = "INSERT INTO " + DBKernel.delimitL(tablename) + " (" + fns.substring(1) +	") VALUES (" + fvs.substring(1) + ")";
					PreparedStatement ps = DBKernel.getDBConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);					
					if (ps.executeUpdate() > 0) result = DBKernel.getLastInsertedID(ps);
				}
				else {
					//System.out.println(result + "\t" + sql);
				}
			}
			catch (Exception e) {
				System.err.println(sql);
				MyLogger.handleException(e);
			}
	  }
		
		return result;
	}
    private final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {{
        //put("^\\d{8}$", "yyyyMMdd");
        //put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
        put("^\\d{1,2}.\\d{1,2}.\\d{4}$", "dd.MM.yyyy");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "dd/MM/yyyy");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
        /*
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
        put("^\\d{12}$", "yyyyMMddHHmm");
        put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
        put("^\\d{14}$", "yyyyMMddHHmmss");
        put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
        put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
        put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
        put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
        put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
        put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
        put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
        */
        put("^\\d{1,2}/\\d{4}$", "MM/yyyy");
        put("^\\d{1,2}/\\d{1,2}/\\d{2}$", "dd/MM/yy");
    }};
    private String determineDateFormat(String dateString) {
        for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
            if (dateString.toLowerCase().matches(regexp)) {
                return DATE_FORMAT_REGEXPS.get(regexp);
            }
        }
        return null; // Unknown format.
    }
}
