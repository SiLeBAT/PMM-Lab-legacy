/**
 * 
 */
package org.hsh.bfr.db.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;

/**
 * @author Armin
 *
 */
public class FocusRight extends AbstractAction {

	private MyDBTable myDB = null;
	
  public FocusRight(MyDBTable myDB) {
  	this.myDB = myDB;
  }    

  public void actionPerformed(ActionEvent e) {
  	if (myDB != null && myDB.getTable() != null) {
  		myDB.getTable().requestFocus();
  	}
  }
}
