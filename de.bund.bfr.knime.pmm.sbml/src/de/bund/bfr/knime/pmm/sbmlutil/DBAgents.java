package de.bund.bfr.knime.pmm.sbmlutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hsh.bfr.db.DBKernel;

import de.bund.bfr.knime.pmm.common.AgentXml;

public class DBAgents {
	static List<AgentXml> dbAgents;

	private DBAgents() {
	}

	/** Get matrices from DB with their ids as keys */
	public static List<AgentXml> getDBAgents() {
		if (dbAgents == null) {
			dbAgents = new LinkedList<>();
			ResultSet rs = DBKernel.getResultSet("SELECT * FROM \"Agenzien\"",
					true);
			try {
				while (rs.next()) {
					dbAgents.add(new AgentXml(rs.getInt("ID"), rs
							.getString("Agensname"), null));
				}
			} catch (SQLException e) {
			}
		}

		return dbAgents;
	}
}