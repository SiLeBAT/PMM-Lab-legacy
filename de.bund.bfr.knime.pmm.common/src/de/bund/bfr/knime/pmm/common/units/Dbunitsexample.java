package de.bund.bfr.knime.pmm.common.units;

import java.util.HashMap;

import org.hsh.bfr.db.UnitsFromDB;

public class Dbunitsexample {
	public Dbunitsexample() {
		UnitsFromDB ufdb = new UnitsFromDB();
		ufdb.askDB();
		HashMap<Integer, UnitsFromDB> z = ufdb.getMap();
		System.err.print(z.size());
	}
}
