package org.hsh.bfr.db.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.hsh.bfr.db.gui.dbtable.MyDBPanel;

/**
 * @author Armin
 *
 */
public class FindAction extends AbstractAction {

	private MyDBPanel myDBPanel = null;
	
  public FindAction(MyDBPanel myDBPanel) {
  	this.myDBPanel = myDBPanel;
  }    

  public void actionPerformed(ActionEvent e) {
  	if (myDBPanel != null) {
  		myDBPanel.getSuchfeld().grabFocus();
  	}
  }
}
