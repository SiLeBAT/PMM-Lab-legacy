/*
 * For Standalone-App compilation
package org.hsh.bfr.db;

import org.hsh.bfr.db.gui.Login;

public class MyPreferences {

	public java.util.prefs.Preferences prefsReg = java.util.prefs.Preferences.userNodeForPackage(Login.class);

	public void prefsFlush() {
	}

	public void put(String key, String value) {
		prefsReg.put(key, value);
	}

	public String get(String key, String defaultValue) {
		return prefsReg.get(key, defaultValue);
	}

	public void putBoolean(String key, boolean value) {
		prefsReg.putBoolean(key, value);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return prefsReg.getBoolean(key, defaultValue);
	}
}*/
package org.hsh.bfr.db;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.hsh.bfr.db.gui.Login;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class MyPreferences {

	private java.util.prefs.Preferences prefsReg = java.util.prefs.Preferences.userNodeForPackage(Login.class);
	private Preferences preferences = InstanceScope.INSTANCE.getNode("org.hsh.bfr.db");
	private Preferences prefs = preferences.node("db");

	public void prefsFlush() {
		if (DBKernel.isKNIME) {
			try {
				preferences.flush();
			}
			catch (BackingStoreException e) {
				e.printStackTrace();
			}		  
		}
	}

	public void put(String key, String value) {
		if (DBKernel.isKNIME) prefs.put(key, value);
		else prefsReg.put(key, value);
	}

	public String get(String key, String defaultValue) {
		if (DBKernel.isKNIME) return prefs.get(key, defaultValue);
		else return prefsReg.get(key, defaultValue);
	}

	public void putBoolean(String key, boolean value) {
		if (DBKernel.isKNIME) prefs.putBoolean(key, value);
		else prefsReg.putBoolean(key, value);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		if (DBKernel.isKNIME) return prefs.getBoolean(key, defaultValue);
		else return prefsReg.getBoolean(key, defaultValue);
	}
}