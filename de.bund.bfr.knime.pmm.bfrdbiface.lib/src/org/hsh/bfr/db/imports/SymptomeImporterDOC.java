/**
 * 
 */
package org.hsh.bfr.db.imports;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;

/**
 * @author Armin
 *
 */
public class SymptomeImporterDOC extends FileFilter implements MyImporter {

	public boolean accept(File f) {
	  if (f.isDirectory()) return true;
	
	  String extension = getExtension(f);
	  if ((extension.equals("doc")) || (extension.equals("docx"))) return true; 
	  return false;
	}
	  
	public String getDescription() {
	    return "Symptome Datei (*.doc; *.docx)";
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
            progress.setString("Importiere Symptome Datei...");
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
		    	else {
		    		is = new FileInputStream(filename);
		    	}
		
		      POIFSFileSystem fs = new POIFSFileSystem(is);

		      // Create a document for this file
		      HWPFDocument doc = new HWPFDocument( fs );

		      // Create a WordExtractor to read the text of the word document
		      WordExtractor we = new WordExtractor( doc );
		      
		      // Extract all paragraphs in the document as strings
		      String[] paragraphs = we.getParagraphText();

		      // Output the document
		      System.out.println( "Word Document has " + paragraphs.length + " paragraphs" );
		      for( int i=0; i<paragraphs.length; i++ ) {
		       System.out.println( i + ". Paragraph\n" + paragraphs[ i ] );
		      }

    			if (progress != null) {
    				progress.setVisible(false);
  	  			// Refreshen:
    				MyDBTable myDB = DBKernel.myList.getMyDBTable();
    				if (myDB.getActualTable() != null) {
	    				String tablename = myDB.getActualTable().getTablename();
	    				if (tablename.equals("Symptome")) {
	    					myDB.setTable(myDB.getActualTable());
	    				}
    				}
    			}
    			else {
    				System.out.println("SymptomeImporterDOC - Fin");
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
