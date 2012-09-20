package org.hsh.bfr.db.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.hsh.bfr.db.MergeDBs;
import org.hsh.bfr.db.gui.About;

/**
 * @author Armin
 *
 */
public class MergeAction extends AbstractAction {

  public MergeAction(String name, Icon icon, String toolTip) {
    putValue(Action.NAME, name);
    putValue(Action.SHORT_DESCRIPTION, toolTip);
    putValue(Action.SMALL_ICON, icon);
    this.setEnabled(false);
  }    

  public void actionPerformed(ActionEvent e) {
 // Zusammenführung
	  new MergeDBs();
	}
}
