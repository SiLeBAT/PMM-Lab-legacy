package de.bund.bfr.knime.pmm.sbmlutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hsh.bfr.db.DBKernel;

public class DBAgents {
	static Map<Integer, String> dbAgents;
	
	private DBAgents() {
	}
	
	/** Get matrices from DB with their ids as keys */
	public static Map<Integer, String> getDBAgents() {
		if (dbAgents == null) {
			dbAgents = new HashMap<>();
			ResultSet rs = DBKernel.getResultSet("SELECT * FROM \"Agenzien\"", true);
			try {
				while (rs.next()) {
					dbAgents.put(rs.getInt("ID"), rs.getString("Agensname"));
				}
			} catch (SQLException e) {
			}
		}
		
		return dbAgents;
	}
}