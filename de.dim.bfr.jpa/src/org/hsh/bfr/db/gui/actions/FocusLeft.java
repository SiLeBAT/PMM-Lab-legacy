/**
 * 
 */
package org.hsh.bfr.db.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.hsh.bfr.db.DBKernel;

/**
 * @author Armin
 *
 */
public class FocusLeft extends AbstractAction {

  public void actionPerformed(ActionEvent e) {
  	DBKernel.myList.requestFocus();
  }
}
