/**
 * 
 */
package org.hsh.bfr.db.imports;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.swing.JProgressBar;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyLogger;

/**
 * @author Weiser
 *
 */
public class SQLScriptImporter implements MyImporter {

	private String delimiter;
	
	public SQLScriptImporter() {
		this.delimiter = ";";
	}
	public SQLScriptImporter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	public void doImport(final String filename, final JProgressBar progress, final boolean showResults) {
	  	Runnable runnable = new Runnable() {
	        public void run() {
	  		    try {
	        		if (progress != null) {
	        			progress.setVisible(true);
			            progress.setStringPainted(true);
			            progress.setString("Importiere SQL Script...");
	        			progress.setMinimum(0);
	        			progress.setMaximum(1);
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

        		    Scanner scanner;
    		        scanner = new Scanner(is).useDelimiter(delimiter);
        		    while(scanner.hasNext()) {
        		        String rawStatement = scanner.next();
        		        if (rawStatement.trim().length() > 0) {
        		        	DBKernel.sendRequest(rawStatement + delimiter, false);
        		        }
        		    }

	        		if (progress != null) {
	    				progress.setVisible(false);
	    			}
	  				else {
	  					MyLogger.handleMessage("SQL Script Importer - Fin");
	  				}
	    			if (showResults) {
	    				//   				
	    			}
	    			else {
	    				MyLogger.handleMessage("SQL Script Importer (" + filename + "): Fin!");
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
}
