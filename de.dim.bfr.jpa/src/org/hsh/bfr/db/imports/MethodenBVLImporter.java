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
import org.hsh.bfr.db.gui.dbtree.MyDBTree;

/**
 * @author Armin
 *
 */
public class MethodenBVLImporter extends FileFilter implements MyImporter {
  /**
  This is the one of the methods that is declared in 
  the abstract class
 */
	public boolean accept(File f) {
	  if (f.isDirectory()) return true;
	
	  String extension = getExtension(f);
	  if (extension.equals("xls")) return true; 
	  return false;
	}
	  
	public String getDescription() {
	    return "Nachweisverfahren BVL Datei (*.xls)";
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
            progress.setStringPainted(true);
            progress.setString("Importiere Methoden_BVL Datei...");
      			progress.setMinimum(0);
      			progress.setMaximum(1339);
      			progress.setValue(0);
      			progress.setVisible(true);
      		}

      		InputStream is = null;
		    	if (filename.startsWith("http://")) {
		    		URL url = new URL(filename);
		    		URLConnection uc = url.openConnection();
		    		is = uc.getInputStream();
		    	}
		    	else {
		    		is = new FileInputStream(filename);
		    	}
		
		    	POIFSFileSystem fs = new POIFSFileSystem(is);
		      HSSFWorkbook wb = new HSSFWorkbook(fs);
		      HSSFSheet sheet;
		      HSSFRow row;

		      sheet = wb.getSheet("Methoden");      
		      PreparedStatement ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Methoden") +
		      		" (" + DBKernel.delimitL("Code") + ", " + DBKernel.delimitL("Beschreibung") + ") VALUES (?,?)");
		      Iterator<Row> rows = sheet.rowIterator(); 
		      int lfd = 0;
		      while( rows.hasNext() ) {
		        row = (HSSFRow) rows.next();
		      	if (row.getRowNum() > 0) {
			          ps.setString(1, row.getCell(0).getStringCellValue().trim());
			          ps.setString(2, row.getCell(2).getStringCellValue());
		          try {
		           ps.execute();          	
		          }
		          catch(SQLException e1) {} // e1.printStackTrace();
		      	}
	    			if (progress != null) {lfd++;progress.setValue(lfd);}
		      }

		      sheet = wb.getSheet("Methoden-OG");      
		      ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Methoden-OG") +
		      		" (" + DBKernel.delimitL("Code") + ", " + DBKernel.delimitL("Name") + ") VALUES (?,?)");
		      rows = sheet.rowIterator(); 
		      while( rows.hasNext() ) {  
		        row = (HSSFRow) rows.next();
		      	if (row.getRowNum() > 0) {
		          ps.setString(1, row.getCell(0).getStringCellValue().trim());
		          ps.setString(2, row.getCell(1).getStringCellValue());
		          try {
		            ps.execute();          	
		          }
		          catch(SQLException e1) {} // e1.printStackTrace();
		      	}
	    			if (progress != null) {lfd++;progress.setValue(lfd);}
		      }

    			if (progress != null) {
    				progress.setVisible(false);
  	  			// Refreshen:
    				MyDBTable myDB = DBKernel.myList.getMyDBTable();
    				if (myDB.getActualTable() != null) {
	    				String tablename = myDB.getActualTable().getTablename();
	    				if (tablename.equals("Methoden-OG") || tablename.equals("Methoden")) {
	    					myDB.setTable(myDB.getActualTable());
	    				}
    				}
    				MyDBTree myTR = DBKernel.myList.getMyDBTree();
    				if (myTR.getActualTable() != null) {
      				String tablename = myTR.getActualTable().getTablename();
      				if (tablename.equals("Methoden-OG") || tablename.equals("Methoden")) {
        				myTR.setTable(myTR.getActualTable());
      				}
    				}
    			}
  				else {
  					System.out.println("MethodenBVLImporter - Fin");
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
