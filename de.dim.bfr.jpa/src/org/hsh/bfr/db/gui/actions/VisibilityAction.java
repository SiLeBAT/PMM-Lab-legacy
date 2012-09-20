package org.hsh.bfr.db.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.gui.InvisibleNode;
import org.hsh.bfr.db.gui.MyList;


public class VisibilityAction  extends AbstractAction {

	private MyList myList;
	private String tableName;
	
  public VisibilityAction(String tableName, Icon icon, String toolTip, MyList myList) {
    putValue(Action.NAME, tableName);
    putValue(Action.SHORT_DESCRIPTION, toolTip);
    putValue(Action.SMALL_ICON, icon);
    this.myList = myList;
    this.tableName =  tableName;
  }    

  public void actionPerformed(ActionEvent e) {
	  
	  String childName;
	  //System.err.println("action.. " + tableName);
	  System.out.println(e);
	  InvisibleNode iNode = (InvisibleNode) myList.getModel().getRoot();
	  InvisibleNode iChild;
	  
	  for (int i=0; i<iNode.getChildCount(); i++) {   
		  iChild = (InvisibleNode)iNode.getChildAt(i);
		  childName = iChild.getUserObject().toString();
		  
		  if (childName.equals(tableName)) {
			  iChild.setVisible(!iChild.isVisible());
		  }
		 
		  myList.updateUI();
		
		  DBKernel.prefs.putBoolean("VIS_NODE_" + childName, iChild.isVisible());
			
		  
	  }
	 
	  
	}
}