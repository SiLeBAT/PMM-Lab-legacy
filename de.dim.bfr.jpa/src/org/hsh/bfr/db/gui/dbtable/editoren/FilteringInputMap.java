/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.editoren;

import javax.swing.InputMap;
import javax.swing.KeyStroke;

/**
 * @author Armin
 *
 */
public class FilteringInputMap extends InputMap {

	 private KeyStroke[] disableKeys;

	 public FilteringInputMap(InputMap parent, KeyStroke[] disableKeys) {
		 super();
		 setParent(parent);
		 this.disableKeys = disableKeys;
	 }

	 public Object get(KeyStroke keyStroke) {
		 if (disableKeys != null) {
			 for(int i=0; i<disableKeys.length; i++) {
				 if(keyStroke.equals(disableKeys[i])) {
					 return null;
				 }
			 }			 
		 }
		 return super.get(keyStroke);
	 }

}