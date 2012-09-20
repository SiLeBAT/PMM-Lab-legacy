/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable;

import javax.swing.InputMap;
import javax.swing.KeyStroke;

/**
 * @author Armin
 *
 */

public class MyFilteringInputmap extends InputMap {

	 private KeyStroke[] keys;

	 public MyFilteringInputmap(InputMap parent, KeyStroke[] keys) {
		 super();
		 setParent(parent);
		 this.keys = keys;
	 }

	 public Object get(KeyStroke keyStroke) {
		 for(int i=0; i<keys.length; i++) {
			 if (keyStroke.equals(keys[i])) {
				 return null;
			 }
		 }
		 return super.get(keyStroke);
	 }
}