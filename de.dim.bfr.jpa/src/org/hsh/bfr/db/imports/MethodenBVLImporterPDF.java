/**
 * 
 */
package org.hsh.bfr.db.imports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;

/**
 * @author Armin
 *
 */
public class MethodenBVLImporterPDF extends FileFilter implements MyImporter {
	public boolean accept(File f) {
	  if (f.isDirectory()) return true;

	  String extension = getExtension(f);
	  if (extension.equals("pdf")) return true; 
	  return false;
	}
	  
	public String getDescription() {
	    return "Methoden_BVL Datei (*.pdf)";
	}

	private String getExtension(File f) {
	  String s = f.getName();
	  int i = s.lastIndexOf('.');
	  if (i > 0 &&  i < s.length() - 1) return s.substring(i+1).toLowerCase();
	  return "";
	}


	public void doImport(final String filename, final JProgressBar progress, final boolean showResults) {
		/*
		//filename = "C:/Users/Armin/Documents/private/freelance/BfR/Data/100711/AmtlicheMethodensammlungBVL.pdf";
  	Runnable runnable = new Runnable() {
      public void run() {
        try {
      		if (progress != null) {
      			progress.setVisible(true);
            progress.setStringPainted(true);
            progress.setString("Importiere Methoden_BVL Datei...");
      			progress.setMinimum(0);
      			progress.setMaximum(61);
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

        	PreparedStatement ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Methoden") +
    					" (" + DBKernel.delimitL("Methoden-Nr") + ", " + DBKernel.delimitL("Beschreibung") + ") VALUES (?, ?)");
    			PreparedStatement ps2 = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Methoden") +
    					" (" + DBKernel.delimitL("Methoden-Nr") + ", " + DBKernel.delimitL("Name") + ") VALUES (?, ?)");

    			String newline, line, page;
    			String toptop = "";
    			String methodTop = "";
    			String descriptionTop = "";
    			Vector<String> methodM = new Vector<String>();
    			Vector<String> descriptionM = new Vector<String>();
    			String description = "";
    			PDDocument doc;
    			PDFTextStripper stripper;
    			BufferedReader buf;

    			doc = PDDocument.load(is);
    			stripper = new PDFTextStripper();
    			Pattern patTT = Pattern.compile("^[\\s]*[0-9]{2}\\.[0-9]{2}[\\S]*[\\s]{0,}");
    			Pattern patL = Pattern.compile("^[\\s]*L [0-9]{2}\\.[0-9]{2}[\\S]*[\\s]{0,}"); // .[E0-9]{1,2}
    			Pattern patM = Pattern.compile("[\\s][0-9]{4}-[0-9]{2}[\\s]");
    			Matcher matL, matM, matTT;
    			for (int i=1; ;i++) {
    				if (progress != null) {
    					progress.setValue(i);
    				}
    				stripper.setStartPage(i);
    	      stripper.setEndPage(i);
    	      page = stripper.getText(doc);
    	      if (page.length() == 0) break;
    	      buf = new BufferedReader(new StringReader(page));
    	      while ((line = buf.readLine()) != null) {
    	      	if (line.trim().startsWith("(Durchführung nach")) {
    	      		description += " " + line.trim();
    	      		while ((line = buf.readLine()) != null) {
    	      			description += " " + line.trim();
    	      			if (line.endsWith(")")) break;
    	      		}
    	      		line = buf.readLine();
    	      		if (line == null) continue;
    	      	}
    	      	if (line.trim().endsWith("(Durchführung nach")) {
    	      		while ((newline = buf.readLine()) != null) {
    	      			line += " " + newline.trim();
    	      			if (newline.endsWith(")")) break;
    	      		}
    	      	}
    	      	matTT = patTT.matcher(line);
    	      	if (matTT.find()) {
    	      		//System.err.println(line);
    	      		//toptop = line;
    	      		//System.err.println(matTT.group() + "\t" + line.substring(matTT.end()).trim());
    	      		ps2.setString(1, matTT.group());
    	      		ps2.setString(2, line.substring(matTT.end()).trim());
    	      		ps2.execute();
    	      		continue;
    	      	}
    	      	matL = patL.matcher(line);
    	  			matM = patM.matcher(line);
    	      	if (matL.find()) {
    	      		if (methodTop.length() > 0) {
    		      		if (description.length() > 0) descriptionM.add(description);
    		      		//System.out.println(methodTop + "\t" + descriptionTop);
    		      		for (int j=0;j<methodM.size();j++) {
    		      			//System.out.println(methodM.get(j) + "\t" + descriptionM.get(j));
    			      		ps.setString(1, methodTop + " _ " + methodM.get(j));
    			      		ps.setString(2, descriptionM.get(j));
    			      		ps.execute();
    		      		}
    		      		description = ""; descriptionM = new Vector(); methodM = new Vector();
    	      		}
    	      		methodTop = matL.group();
    	      		if (methodTop.equals(line.trim())) {
    	      			continue;
    	      		}
    	      		if (!matM.find()) {
    	      			descriptionTop = line.substring(matL.end()).trim();
    	      			continue;
    	      		}
    	      		else {
    	      			matM.reset();
    	      			descriptionTop = "";
    	      		}
    	      	}
    	      	if (matM.find()) {
    	      		if (description.length() > 0) descriptionM.add(description);
    	      		methodM.add(matM.group());
    	      		description = line.substring(matM.end()).trim();
    	      	}
    	      	else if (line.trim().startsWith("Seite ") || line.trim().startsWith("Amtl. Sammlung § 64 LFGB")) {
    	      		line = buf.readLine();
    	      	}
    	      	else if (!line.trim().startsWith("1) Siehe dazu") && !line.trim().startsWith("1) Zur Ermittlung") &&
    	      			!line.trim().startsWith("1) Anwendungsbereich") && !line.trim().startsWith("*) einschließlich")) {
    		      	if (methodM.size() == 0) {
    		      		if (descriptionTop.endsWith("-")) descriptionTop = descriptionTop.substring(0, descriptionTop.length() - 1) + line.trim();
    		      		else descriptionTop += " " + line.trim();
    		      	}
    		      	else {
    		      		if (description.endsWith("-")) description = description.substring(0, description.length() - 1) + line.trim();
    		      		else description += " " + line.trim();
    		      	}
    	      	}
    	      }
    			}
      		if (methodTop.length() > 0) {
        		if (description.length() > 0) descriptionM.add(description);
        		//System.out.println(methodTop + "\t" + descriptionTop);
        		for (int j=0;j<methodM.size();j++) {
        			//System.out.println(methodM.get(j) + "\t" + descriptionM.get(j));
          		ps.setString(1, methodTop + " _ " + methodM.get(j));
          		ps.setString(2, descriptionM.get(j));
          		ps.execute();
        		}
      		}
    			doc.close();

    			if (progress != null) {
    				progress.setVisible(false);
  	  			// Refreshen:
    				MyDBTable myDB = DBKernel.myList.getMyDBTable();
    				if (myDB.getActualTable() != null) {
	    				String tablename = myDB.getActualTable().getTablename();
	    				if (tablename.equals("Methoden")) {
	    					myDB.setTable(myDB.getActualTable());
	    				}
    				}
    			}
  				else {
  					System.out.println("MethodenBVLImporterPDF - Fin");
  				}
        }
    		catch (Exception e) {
    			e.printStackTrace();
    		}
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
		*/
	}
}
