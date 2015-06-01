package de.bund.bfr.knime.pmm.sbmlutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hsh.bfr.db.DBKernel;

/** Holds DB Matrices which can be accessed using their ids. */
public class DBMatrices {
	
	static Map<Integer, String> dbMatrices;
	
	private DBMatrices() {
	}
	
	/** Get matrices from DB with their ids as keys */
	public static Map<Integer, String> getDBMatrices() {
		if (dbMatrices == null) {
			dbMatrices = new HashMap<>();
			ResultSet rs = DBKernel.getResultSet("SELECT * FROM \"Matrices\"", true);
			try {
				while (rs.next()) {
					dbMatrices.put(rs.getInt("ID"), rs.getString("Matrixname"));
				}
			} catch (SQLException e) {
			}
		}
		
		return dbMatrices;
	}
}