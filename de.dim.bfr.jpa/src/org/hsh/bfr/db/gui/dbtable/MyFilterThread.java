/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable;

import org.hsh.bfr.db.gui.dbtree.MyDBTree;

/**
 * @author Armin
 *
 */
public class MyFilterThread extends Thread {

  private MyDBTree myDBTree1;
  private String findString = "";

  public MyFilterThread(MyDBTree myDBTree1, String findString) {
  	this.myDBTree1 = myDBTree1;
  	this.findString = findString;
  }
  
  public void run() {
  	if (myDBTree1 != null) {
  		myDBTree1.checkFilter(findString);
  	}
  }
}
