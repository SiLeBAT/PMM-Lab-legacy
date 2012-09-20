package org.hsh.bfr.db.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.imports.GeneralXLSImporter;
import org.hsh.bfr.db.imports.LieferkettenImporter;
import org.hsh.bfr.db.imports.LieferkettenImporterNew;
import org.hsh.bfr.db.imports.MyImporter;
import org.hsh.bfr.db.imports.MyProzessXMLImporter;
import org.hsh.bfr.db.imports.MyRisImporter;

/**
 * @author Armin
 *
 */
public class ImportAction extends AbstractAction {

	private JProgressBar progressBar1;
	
  public ImportAction(String name, Icon icon, String toolTip, JProgressBar progressBar1) {
  	this.progressBar1 = progressBar1;
    putValue(Action.NAME, name);
    putValue(Action.SHORT_DESCRIPTION, toolTip);
    putValue(Action.SMALL_ICON, icon);
  }    

  public void actionPerformed(ActionEvent e) {
  	String lastOutDir = DBKernel.prefs.get("LAST_OUTPUT_DIR", "");
	  JFileChooser fc = new JFileChooser(lastOutDir);
//	  if (!DBKernel.isKrise) fc.addChoosableFileFilter(new LieferkettenImporterNew());	  
	  fc.addChoosableFileFilter(new MyProzessXMLImporter());
	  fc.addChoosableFileFilter(new MyRisImporter());
	  if (DBKernel.isAdmin()) fc.addChoosableFileFilter(new GeneralXLSImporter());
	  if (DBKernel.isKrise) fc.addChoosableFileFilter(new LieferkettenImporterNew());	  
	  //fc.addChoosableFileFilter(new LieferkettenImporter());	  
	  //fc.addChoosableFileFilter(new MethodenADVImporterDOC());
	  //fc.addChoosableFileFilter(new SymptomeImporterDOC());
	  fc.setAcceptAllFileFilterUsed(false);
	  fc.setMultiSelectionEnabled(false);
	  fc.setDialogTitle("Import");
	  try {
		  int returnVal = fc.showOpenDialog(progressBar1); // this
		  if (returnVal == JFileChooser.APPROVE_OPTION) {
		  	File selectedFile = fc.getSelectedFile();
		  	if (selectedFile != null) {
		  		DBKernel.prefs.put("LAST_OUTPUT_DIR", selectedFile.getParent());
		  		if (fc.getFileFilter() instanceof MyImporter) {
		  			DBKernel.importing = true;
		  			MyImporter mi = (MyImporter) fc.getFileFilter();
		  			mi.doImport(selectedFile.getAbsolutePath(), progressBar1, true);
		  			DBKernel.importing = false;
		  			MyLogger.handleMessage("Importing - Fin!");
		  		}
		  	}
		  }	  
	  }
	  catch (Exception e1) {
		  MyLogger.handleMessage(fc + "\t" + lastOutDir);
		  MyLogger.handleException(e1);
	  }
		MyLogger.handleMessage("Importing - FinFin!");
	}
}
