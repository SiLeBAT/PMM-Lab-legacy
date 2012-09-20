package org.hsh.bfr.db.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.hsh.bfr.db.DBKernel;

/**
 * @author Armin
 *
 */
public class UserAction extends AbstractAction {

  public UserAction(String name, Icon icon, String toolTip) {
    putValue(Action.NAME, name);
    putValue(Action.SHORT_DESCRIPTION, toolTip);
    putValue(Action.SMALL_ICON, icon);
  }    

  public void actionPerformed(ActionEvent e) {
  	DBKernel.myList.setSelection(DBKernel.users.getTablename());
	}
}
