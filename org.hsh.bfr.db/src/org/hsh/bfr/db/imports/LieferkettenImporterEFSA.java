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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	private Calendar calendar = Calendar.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

		    HSSFSheet transactionSheet = wb.getSheet("Transactions"); 
		    HSSFSheet businessSheet = wb.getSheet("Business_List"); 
		    int numSuccess = 0;
		    int numFails = 0;
			int numRows = transactionSheet.getLastRowNum() + 1;
			progress.setMaximum(numRows);
	      	progress.setValue(0);
				      
	      	for (int i=1;i<numRows;i++) {
	      		HSSFRow row = transactionSheet.getRow(i);
				if (row != null) {
				      String serial = getStrVal(row.getCell(0)); // Serial_number
				      //String BL0 = getStrVal(row.getCell(1)); // Contact_Region
				      //String KP = getStrVal(row.getCell(2)); // Contact_person
				      
				      String idRec = getStrVal(row.getCell(3)); // ID_Address
				      String adressRec = getStrVal(row.getCell(4)); // Address
				      if ((serial == null || serial.trim().isEmpty()) && (adressRec == null || adressRec.trim().isEmpty())) {
					      	continue;//break;
					      }
				      String activityRec = getStrVal(row.getCell(5)); // Activity				      
				      String nameRec = adressRec;
				      String streetRec = null;
				      String streetNoRec = null;
				      String zipRec = null;
				      String cityRec = null;
				      String countyRec = null;
				      String countryRec = null;
				      String vatRec = null;
				      HSSFRow busRow = getRow(businessSheet, idRec, 0);
				      if (busRow != null) {
					      nameRec = getStrVal(busRow.getCell(1)); //
					      streetRec = getStrVal(busRow.getCell(2)); //
					      streetNoRec = getStrVal(busRow.getCell(3)); //
					      zipRec = getStrVal(busRow.getCell(4)); //
					      cityRec = getStrVal(busRow.getCell(5)); //
					      countyRec = getStrVal(busRow.getCell(6)); 
					      countryRec = getStrVal(busRow.getCell(7)); // 
					      vatRec = getStrVal(busRow.getCell(8)); //
				      }
				      
				      String prodNameOut = getStrVal(row.getCell(6)); // ProductName
				      String prodNumOut = getStrVal(row.getCell(7)); // ProductNo
				      String dayOut = getStrVal(row.getCell(8)); // Day
				      String monthOut = getStrVal(row.getCell(9)); // Month
				      String yearOut = getStrVal(row.getCell(10)); // Year
				      String amountKG_Out = getStrVal(row.getCell(11)); // amountKG
				      String typePUOut = getStrVal(row.getCell(12)); // typePU
				      String numPUOut = getStrVal(row.getCell(13)); // numPU
				      String lotNo_Out = getStrVal(row.getCell(14)); // 
				      String dayMHDOut = getStrVal(row.getCell(15)); 
				      String monthMHDOut = getStrVal(row.getCell(16)); 
				      String yearMHDOut = getStrVal(row.getCell(17)); // 
				      String dayPDOut = getStrVal(row.getCell(18)); 
				      String monthPDOut = getStrVal(row.getCell(19)); 
				      String yearPDOut = getStrVal(row.getCell(20)); 
				      Date dateOut = getDate(dayOut, monthOut, yearOut);
				      Date dateMHDOut = getDate(dayMHDOut, monthMHDOut, yearMHDOut);
				      Date datePDOut = getDate(dayPDOut, monthPDOut, yearPDOut);
				      
				      String idInsp = getStrVal(row.getCell(21)); // ID_Address
				      String adressInsp = getStrVal(row.getCell(22)); // Address
				      String activityInsp = getStrVal(row.getCell(23)); // Activity
				      String nameInsp = adressInsp;
				      String streetInsp = null;
				      String streetNoInsp = null;
				      String zipInsp = null;
				      String cityInsp = null;
				      String countyInsp = null;
				      String countryInsp = null;
				      String vatInsp = null;
				      busRow = getRow(businessSheet, idInsp, 0);
				      if (busRow != null) {
					      nameInsp = getStrVal(busRow.getCell(1)); //
					      streetInsp = getStrVal(busRow.getCell(2)); //
					      streetNoInsp = getStrVal(busRow.getCell(3)); //
					      zipInsp = getStrVal(busRow.getCell(4)); //
					      cityInsp = getStrVal(busRow.getCell(5)); //
					      countyInsp = getStrVal(busRow.getCell(6)); 
					      countryInsp = getStrVal(busRow.getCell(7)); // 
					      vatInsp = getStrVal(busRow.getCell(8)); //
				      }

				      String prodNameIn = getStrVal(row.getCell(24)); // ProductName
				      String prodNumIn = getStrVal(row.getCell(25)); // ProductNo
				      String dayIn = getStrVal(row.getCell(26)); // Day
				      String monthIn = getStrVal(row.getCell(27)); // Month
				      String yearIn = getStrVal(row.getCell(28)); // Year
				      String amountKG_In = getStrVal(row.getCell(29)); // amountKG
				      String typePUIn = getStrVal(row.getCell(30)); // typePU
				      String numPUIn = getStrVal(row.getCell(31)); // numPU
				      String lotNo_In = getStrVal(row.getCell(32)); // 
				      String dayMHDIn = getStrVal(row.getCell(33)); 
				      String monthMHDIn = getStrVal(row.getCell(34)); 
				      String yearMHDIn = getStrVal(row.getCell(35)); // 
				      String dayPDIn = getStrVal(row.getCell(36)); 
				      String monthPDIn = getStrVal(row.getCell(37)); 
				      String yearPDIn = getStrVal(row.getCell(38)); 
				      Date dateIn = getDate(dayIn, monthIn, yearIn);
				      Date dateMHDIn = getDate(dayMHDIn, monthMHDIn, yearMHDIn);
				      Date datePDIn = getDate(dayPDIn, monthPDIn, yearPDIn);
				      
				      String idSup = getStrVal(row.getCell(39)); // ID_Address
				      String adressSup = getStrVal(row.getCell(40)); // Address
				      String activitySup = getStrVal(row.getCell(41)); // Activity
				      String nameSup = adressSup;
				      String streetSup = null;
				      String streetNoSup = null;
				      String zipSup = null;
				      String citySup = null;
				      String countySup = null;
				      String countrySup = null;
				      String vatSup = null;
				      busRow = getRow(businessSheet, idSup, 0);
				      if (busRow != null) {
					      nameSup = getStrVal(busRow.getCell(1)); //
					      streetSup = getStrVal(busRow.getCell(2)); //
					      streetNoSup = getStrVal(busRow.getCell(3)); //
					      zipSup = getStrVal(busRow.getCell(4)); //
					      citySup = getStrVal(busRow.getCell(5)); //
					      countySup = getStrVal(busRow.getCell(6)); 
					      countrySup = getStrVal(busRow.getCell(7)); // 
					      vatSup = getStrVal(busRow.getCell(8)); //
				      }

				      //String ec = getStrVal(row.getCell(42)); // EndChain
				      //String ece = getStrVal(row.getCell(43)); // Explanation_EndChain
				      String oc = getStrVal(row.getCell(44)); // OriginCountry
				      String comment = getStrVal(row.getCell(45)); // Contact_Questions_Remarks
				      //String ft = getStrVal(row.getCell(46)); // Further_Traceback
				      //String ms = getStrVal(row.getCell(47)); // MicrobiologicalSample

				      if (amountKG_Out != null && amountKG_In != null && Integer.parseInt(amountKG_Out) > Integer.parseInt(amountKG_In)) System.err.println("amountOut > aomountIn!!! Row " + i + "; amountKG_Out: " + amountKG_Out + "; amountKG_In: " + amountKG_In);
				      if (dateOut != null && dateIn != null && dateOut.getTime() < dateIn.getTime()) System.err.println("dateOut < dateIn!!! Row " + i + "; dateOut: " + dateOut + "; dateIn: " + dateIn);

				      Integer c1 = null;
				      Integer c2 = null;
				      if (nameInsp != null && !nameInsp.trim().isEmpty()) {
				    	  c1 = getCharge_Lieferung(nameInsp, streetInsp, streetNoInsp, zipInsp, cityInsp, countyInsp, countryInsp, activityInsp, vatInsp,
				    			  prodNameOut, prodNumOut, lotNo_Out, dateMHDOut, datePDOut, oc, dateOut, amountKG_Out, typePUOut, numPUOut,
				    			  nameRec, streetRec, streetNoRec, zipRec, cityRec, countyRec, countryRec, activityRec, vatRec,
				    			  comment, true);
				      }
				      if (nameSup != null && !nameSup.trim().isEmpty()) {
				    	  c2 = getCharge_Lieferung(nameSup, streetSup, streetNoSup, zipSup, citySup, countySup, countrySup, activitySup, vatSup,
					    		  prodNameIn, prodNumIn, lotNo_In, dateMHDIn, datePDIn, oc, dateIn, amountKG_In, typePUIn, numPUIn,
					    		  nameInsp, streetInsp, streetNoInsp, zipInsp, cityInsp, countyInsp, countryInsp, activityInsp, vatInsp,
					    		  comment, false);
				      }
				      if (c1 == null) {
				      	System.err.println("Fehlerchenchen_1!! Row " + i);
				      	numFails++;
				      }
				      else if (c2 == null) {
				      	System.err.println("Fehlerchenchen_2!! E.g. Station not defined? Row " + i);
				      	getCharge_Lieferung(nameSup, streetSup, streetNoSup, zipSup, citySup, countySup, countrySup, activitySup, vatSup,
					    		prodNameIn, prodNumIn, lotNo_In, dateMHDIn, datePDIn, oc, dateIn, amountKG_In, typePUIn, numPUIn,
				      			nameSup, streetSup, streetNoSup, zipSup, citySup, countySup, countrySup, activityInsp, vatInsp,
				      			comment, false);
				      	numFails++;
				      }
				      else {
				      	if (c2 != null) {
					      	if (c1.intValue() == c2.intValue()) {
					      		System.err.println("Fehlerchenchen_3!! Row " + i);
						      	numFails++;
					      	}
					      	else if (getID("ChargenVerbindungen",
		    							new String[]{"Zutat","Produkt"},
		    							new String[]{c2.toString(), c1.toString()},
		    							null) == null) {
						      	System.err.println("Fehlerchenchen_4!! Row " + i);
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
	private Date getDate(String day, String month, String year) {
		calendar.clear();
		if (day != null && !day.trim().isEmpty()) calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
		if (month != null && !month.trim().isEmpty()) calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1);
		if (year != null && !year.trim().isEmpty()) calendar.set(Calendar.YEAR, Integer.parseInt(year));
		Date date = calendar.getTime();
		if (date.getTime() > 0) return date;
		else return null;
	}
	private HSSFRow getRow(HSSFSheet sheet, String value, int column) {
		HSSFRow result = null;
		if (value != null && !value.trim().isEmpty()) {
			int numRows = sheet.getLastRowNum() + 1;
	      	for (int i=1;i<numRows;i++) {
	      		HSSFRow row = sheet.getRow(i);
				if (row != null) {
			      String val = getStrVal(row.getCell(column));
			      if (val != null && !val.trim().isEmpty()) {
				      if (value.trim().equals(val.trim())) {
				    	  result = row;
				    	  break;
				      }
			      }
				}
	      	}
		}
		return result;
	}
  private String getStrVal(HSSFCell cell) {
  	String result = null;
		if (cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
		}
		else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
			result = cell.getStringCellValue();
			if (result.equals(".")) result = null;
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
	private Integer getCharge_Lieferung(String name, String street, String streetNumber, String zip, String city, String county, String country, String kind, String vat,
			String article, String articleNumber, String charge, Date mhd, Date prod, String originCountry, Date delivery, String amountKG, String typePU, String numPU,
			String nameTo, String streetTo, String streetNumberTo, String zipTo, String cityTo, String countyTo, String countryTo, String kindTo, String vatTo,
			String comment, boolean returnCharge) {
		Integer result = null;

		String mhdS = mhd == null ? null : sdf.format(mhd);
		String prodS = prod == null ? null : sdf.format(prod);
		String deliveryS = delivery == null ? null : sdf.format(delivery);
		Integer lastID = getID("Station",
					new String[]{"Name","Strasse","Hausnummer","PLZ","Ort","Bundesland","Land","Betriebsart","VATnumber"},
					new String[]{name, street, streetNumber, zip, city, county, country, kind, vat},
					null);
			if (lastID != null) {
					lastID = getID("Produktkatalog",
							new String[]{"Station","Artikelnummer","Bezeichnung"},
							new String[]{lastID.toString(), articleNumber, article},
							null);
					if (lastID != null) {
						lastID = getID("Chargen",
								new String[]{"Artikel","ChargenNr","MHD","Herstellungsdatum","Kommentar"},
								new String[]{lastID.toString(), charge, mhdS, prodS, originCountry},
								new boolean[]{true,true,true,true,false});
						if (returnCharge) result = lastID;
						if (lastID != null) {
							Integer empf = null;
							if (nameTo != null && !nameTo.trim().isEmpty()) {
								empf = getID("Station",
										new String[]{"Name","Strasse","Hausnummer","PLZ","Ort","Bundesland","Land","Betriebsart","VATnumber"},
										new String[]{nameTo, streetTo, streetNumberTo, zipTo, cityTo, countyTo, countryTo, kindTo, vatTo},
										null);
							}
							if (charge == null || charge.trim().isEmpty()) charge = articleNumber + "; " + mhd;
							lastID = getID("Lieferungen",
									new String[]{"Charge","Lieferdatum","Unitmenge","UnitEinheit","Empfänger","Kommentar"}, // "#Units2","BezUnits2",
									new String[]{lastID.toString(), deliveryS, amountKG, "kg", empf == null ? null : empf.toString(),comment}, // numPU, typePU, 
									new boolean[]{true,true,true,true,true,false});
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
		  if (feldVals[i] != null && feldVals[i].trim().isEmpty()) feldVals[i] = null;
		  	if (feldVals[i] != null) feldVals[i] = feldVals[i].replaceAll("'", "\\apos");
		  	fns += "," + DBKernel.delimitL(feldnames[i]);
		  	fvs += feldVals[i] != null ? ",'" + feldVals[i] + "'" : ",NULL";
			if (key == null || key[i]) {
				sql += " AND " + DBKernel.delimitL(feldnames[i]) + (feldVals[i] != null ? "='" + feldVals[i] + "'" : " IS NULL");	  
			}
	  }
	  if (!fns.isEmpty() && !fvs.isEmpty()) {
		  ResultSet rs = null;
		  //System.err.println(sql);
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
}
