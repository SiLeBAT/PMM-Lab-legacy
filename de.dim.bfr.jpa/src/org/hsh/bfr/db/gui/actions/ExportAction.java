package org.hsh.bfr.db.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.exports.ExcelExport;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;

/**
 * @author Armin
 *
 */
public class ExportAction extends AbstractAction {

	private JProgressBar progressBar1;
	private MyDBTable myDB;

	public ExportAction(String name, Icon icon, String toolTip, JProgressBar progressBar1, MyDBTable myDB) {
  	this.progressBar1 = progressBar1;
  	this.myDB = myDB;
    putValue(Action.NAME, name);
    putValue(Action.SHORT_DESCRIPTION, toolTip);
    putValue(Action.SMALL_ICON, icon);
  }    

  public void actionPerformed(ActionEvent e) {
		int retVal = JOptionPane.showConfirmDialog(DBKernel.mainFrame, "Soll der Export den Volltext beinhalten?\n\nJa:\nAlle Felder werden im Volltext abgespeichert.\n\nNein:\nFür (die grauen) \"Fremdfelder\" werden nur die VerlinkungsIDs abgespeichert.",
				"Excel Export - Wie?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		String retValStr = ""; // Bei Gelegenheit mal aktivieren (parsen!) hier!
		/*(String)JOptionPane.showInputDialog(
				DBKernel.mainFrame,
                "Welche Zeilen sollen exportiert werden (Default: alle, Syntax: 1;3;5-12)?",
                "Excel Export - Was genau?",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "");*/
		String lastOutDir = DBKernel.prefs.get("LAST_OUTPUT_DIR", "");
	  JFileChooser fc = new JFileChooser(lastOutDir);
	  ExcelExport xls = new ExcelExport();
	  fc.setFileFilter(xls);
	  fc.setAcceptAllFileFilterUsed(false);
	  fc.setMultiSelectionEnabled(false);
	  fc.setSelectedFile(new File(myDB.getActualTable().getTablename() + ".xls"));
	  fc.setDialogTitle("Export");
	  int returnVal = fc.showSaveDialog(progressBar1);// MainFrame
	  if(returnVal == JFileChooser.APPROVE_OPTION) {
	  	File selectedFile = fc.getSelectedFile();
	  	if (selectedFile != null) {
	  		DBKernel.prefs.put("LAST_OUTPUT_DIR", selectedFile.getParent());
	  		if(selectedFile.exists()) {
	  			returnVal = JOptionPane.showConfirmDialog(progressBar1, "Soll die Datei ersetzt werden?", "Excel Datei bereits vorhanden", JOptionPane.YES_NO_CANCEL_OPTION);
	  			if (returnVal == JOptionPane.NO_OPTION) {actionPerformed(null); return;}
	  			else if (returnVal == JOptionPane.YES_OPTION) ;
	  			else return;
	  		}
	  		xls.doExport(selectedFile.getAbsolutePath(), myDB, progressBar1, retVal == JOptionPane.YES_OPTION, retValStr);
	  	}
	  }
	}
}
