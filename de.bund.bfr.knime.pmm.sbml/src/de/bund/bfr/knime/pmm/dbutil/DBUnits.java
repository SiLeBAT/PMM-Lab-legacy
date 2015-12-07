package de.bund.bfr.knime.pmm.dbutil;

import java.util.HashMap;
import java.util.Map;

import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;

/** Holds DB units, which can be accessed using the display strings. */
public class DBUnits {

	static Map<String, UnitsFromDB> dbUnits;

	// This class is so ridiculously expensive, it should not instantiated more
	// than once
	private DBUnits() {
	}
	
	/** Get units from DB with their display strings as keys */
	public static Map<String, UnitsFromDB> getDBUnits() {
		if (dbUnits == null) {
			UnitsFromDB tempUnits = new UnitsFromDB();
			tempUnits.askDB();
			
			// Create unit map with unit display as keys
			dbUnits = new HashMap<>();
			for (UnitsFromDB ufdb : tempUnits.getMap().values()) {
				dbUnits.put(ufdb.getDisplay_in_GUI_as(), ufdb);
			}
		}
		return dbUnits;
	}
}
