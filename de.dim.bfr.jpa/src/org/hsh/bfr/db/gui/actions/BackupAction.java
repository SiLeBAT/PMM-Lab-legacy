package org.hsh.bfr.db.gui.actions;

import java.awt.Cursor;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.hsh.bfr.db.Backup;
import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;

/**
 * @author Armin
 *
 */
public class BackupAction extends AbstractAction {

	public BackupAction(String name, Icon icon, String toolTip) {
    putValue(Action.NAME, name);
    putValue(Action.SHORT_DESCRIPTION, toolTip);
    putValue(Action.SMALL_ICON, icon);
  }    

  public void actionPerformed(ActionEvent e) {
	  try {
		  if (DBKernel.mainFrame != null) DBKernel.mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		  Backup.dbBackup();
	  }
	  finally {
		  if (DBKernel.mainFrame != null) DBKernel.mainFrame.setCursor(Cursor.getDefaultCursor());
	  }
  }
}
