/**
 * 
 */
package org.hsh.bfr.db.imports;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;

/**
 * @author Armin
 *
 */
public class KrankheitenVETImporter extends FileFilter implements MyImporter {
  /**
  This is the one of the methods that is declared in 
  the abstract class
 */
	public boolean accept(File f) {
	  if (f.isDirectory()) return true;
	
	  String extension = getExtension(f);
	  if ((extension.equals("xls")) || (extension.equals("xlsx"))) return true; 
	  return false;
	}
	  
	public String getDescription() {
	    return "Krankheitsarten_VET Datei (*.xls; *.xlsx)";
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
            progress.setString("Importiere Krankheitsarten_VET Datei...");
      			progress.setMinimum(0);
      			progress.setMaximum(4);
      			progress.setValue(0);
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

		      sheet = wb.getSheet("VET_Krankheitsarten");      
		      PreparedStatement ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Tierkrankheiten") +
		      		" (" + DBKernel.delimitL("VET_Code") + ", " + DBKernel.delimitL("Kurzbezeichnung") + ", " + DBKernel.delimitL("Krankheitsart") + ") VALUES (?,?,?)");
		      Iterator<Row> rows = sheet.rowIterator(); 
		      boolean go;
		      while( rows.hasNext() ) {
		        row = (HSSFRow) rows.next();
		      	if (row.getRowNum() > 0) {
		      		String z1 = row.getCell(1).getStringCellValue(); 
		      		String z2 = row.getCell(2).getStringCellValue(); 
		      		go = true;
		      		if (z1.startsWith("A - ") && !z2.equals("A")) {
			          ps.setString(1, "A"+z2);
			          ps.setString(2, row.getCell(3).getStringCellValue());
			          ps.setString(3, row.getCell(4).getStringCellValue());
			          if (progress != null) progress.setValue(1);
			    		}
		      		else if (z1.startsWith("M - ") && !z2.equals("M")) {
			          ps.setString(1, "M"+z2);
			          ps.setString(2, row.getCell(3).getStringCellValue());
			          ps.setString(3, row.getCell(4).getStringCellValue());		      			
			          if (progress != null) progress.setValue(2);
		      		}
		      		else if (z1.startsWith("S - ") && !z2.equals("S")) {
			          ps.setString(1, "S"+z2);
			          ps.setString(2, row.getCell(3).getStringCellValue());
			          ps.setString(3, row.getCell(4).getStringCellValue());		      			
			          if (progress != null) progress.setValue(3);
		      		}
		      		else {
		      			go = false;
		      		}
		          try {
		            if (go) ps.execute();
		          }
		          catch(SQLException e1) {} // e1.printStackTrace();
		      	}
		      }

    			if (progress != null) {
    				progress.setValue(4);
    				progress.setVisible(false);
  	  			// Refreshen:
    				MyDBTable myDB = DBKernel.myList.getMyDBTable();
    				if (myDB.getActualTable() != null) {
	    				String tablename = myDB.getActualTable().getTablename();
	    				if (tablename.equals("Tierkrankheiten")) {
	    					myDB.setTable(myDB.getActualTable());
	    				}
    				}
    			}
  				else {
  					System.out.println("KrankheitenVETImporter - Fin");
  				}
		    }
		    catch (Exception e) {e.printStackTrace();}
      }
    };
    
    Thread thread = new Thread(runnable);
    thread.start();
    try {
			thread.join();
		}
    catch (InterruptedException e) {
			e.printStackTrace();
		}
  }
}
