package org.hsh.bfr.db.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.hsh.bfr.db.gui.MyList;
import org.hsh.bfr.db.gui.SelectionDialog;

public class SelectionAction extends AbstractAction {
		MyList myList;
	  public SelectionAction(String name, Icon icon, String toolTip, MyList myList) {
	    this.myList = myList;
		putValue(Action.NAME, name);
	    putValue(Action.SHORT_DESCRIPTION, toolTip);
	    putValue(Action.SMALL_ICON, icon);
	  }    

	  public void actionPerformed(ActionEvent e) {
			new SelectionDialog(myList).setVisible(true);
		}

}
