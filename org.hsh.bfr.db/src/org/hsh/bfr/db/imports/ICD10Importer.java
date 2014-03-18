/**
 * 
 */
package org.hsh.bfr.db.imports;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;

import javax.swing.JProgressBar;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.MyTable;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;

/**
 * @author Armin
 *
 */
public class ICD10Importer implements MyImporter {
		
	public void doImport(final String filename, final JProgressBar progress, final boolean showResults) {
  	Runnable runnable = new Runnable() {
      public void run() {
				try {
		      		if (progress != null) {
		      			progress.setVisible(true);
		      			progress.setStringPainted(true);
		      			progress.setString("Importiere ICD Dateien...");
		      			progress.setMinimum(0);
		      			progress.setMaximum(1);
		      			progress.setValue(0);						
		      		}
		      		
		      		String[] ICD10_Filenames = {"icd10gmsyst_kapitel2011.txt","icd10gmsyst_gruppen2011.txt",
		      				"MORBL.TXT","MORTL1Grp.TXT","MORTL1.TXT","MORTL2.TXT","MORTL3Grp.TXT","MORTL3.TXT","MORTL4.TXT",
		      				"icd10gmsyst_kodes2011.txt"};
		      		String[] ICD10_Tables = {"ICD10_Kapitel","ICD10_Gruppen",
		      				"ICD10_MorbL","ICD10_MortL1Grp","ICD10_MortL1","ICD10_MortL2","ICD10_MortL3Grp","ICD10_MortL3","ICD10_MortL4",
		      				"ICD10_Kodes"};
		      		for (int i=0;i<ICD10_Filenames.length;i++) {
		          		InputStream is = null;
				    	if (filename.startsWith("http://")) {
				    		URL url = new URL(filename + ICD10_Filenames[i]);
				    		URLConnection uc = url.openConnection();
				    		is = uc.getInputStream();
				    	}
				    	else if (filename.startsWith("/org/hsh/bfr/db/res/")) {
								is = getClass().getResourceAsStream(filename + ICD10_Filenames[i]);		    		
				    	}
				    	else {
				    		is = new FileInputStream(filename + ICD10_Filenames[i]);
				    	}
					    	MyLogger.handleMessage(ICD10_Tables[i]);
					    	MyTable myT = DBKernel.myDBi.getTable(ICD10_Tables[i]);
					    	String[] myTCols = myT.getFieldNames();
					    	String[] myTTypes = myT.getFieldTypes();
					    	MyTable[] myTForeigns = myT.getForeignFields();
					    	String myTCs = "";
					    	String myQMs = "";
					    	for (int ii=0;ii<myTCols.length;ii++) {
					    		if (myTCs.length() > 0) {myTCs += ",";myQMs += ",";}
					    		myTCs += DBKernel.delimitL(myTCols[ii]);
					    		myQMs += "?";
					    	}
			      		DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL(ICD10_Tables[i]), false);
					      String sql = "INSERT INTO " + DBKernel.delimitL(ICD10_Tables[i]) +
					      " (" + myTCs + ") VALUES (" + myQMs + ")";
					      PreparedStatement ps = DBKernel.getDBConnection().prepareStatement(sql);
					  		String trennzeichen = ";";
					  		try {
					  			BufferedReader in = new BufferedReader(new InputStreamReader(is, "UTF8"));
					  			String readString;
					  			String[] strArr;
					  			while ((readString = in.readLine()) != null) {
					  				strArr = readString.split(trennzeichen);
					  				for (int ii=0;ii<strArr.length;ii++) {
					  					if (myTTypes[ii].equals("INTEGER")) {
					  						ps.setInt(ii+1, DBKernel.getID(myTForeigns[ii].getTablename(), myTCols[ii], strArr[ii]));
					  					}
					  					else {
					  						ps.setString(ii+1, strArr[ii]);
					  					}
					  				}
					  				ps.execute();
					  			}
					  			in.close();
					  			is.close();
					  		}
					  		catch (Exception e) {
					  			MyLogger.handleException(e);
					  		}
		      		}
		
		    			if (progress != null) {
		    				progress.setVisible(false);
		  	  			// Refreshen:
		    				MyDBTable myDB = DBKernel.myList.getMyDBTable();
		    				if (myDB.getActualTable() != null) {
			    				String tablename = myDB.getActualTable().getTablename();
			    				if (tablename.startsWith("ICD10_")) {
			    					myDB.setTable(myDB.getActualTable());
			    				}
		    				}
		    				
		    			}
						}
						catch (Exception e) {
							MyLogger.handleException(e);
						}
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
