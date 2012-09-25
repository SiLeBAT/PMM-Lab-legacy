package org.hsh.bfr.db.gui.dbtable.header;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.hsh.bfr.db.DBKernel;

public class GuiMessages {
	private static final String BUNDLE_NAME = "org.hsh.bfr.db.gui.dbtable.header.guimessages_" + DBKernel.lang; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private GuiMessages() {
		// empty
	}

	/**
	* @param key
	*            the key for the message
	* @return the string that matches the key
	*/
	public static String getString(final String key) {
		try {
			String res = RESOURCE_BUNDLE.getString(key);
			return res == null || res.isEmpty() ? key : res;
		}
		catch (final Exception e) {
			return key;//'!' + key + '!';
		}
	}
}
