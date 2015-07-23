package de.bund.bfr.knime.pmm.dbutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hsh.bfr.db.DBKernel;

import de.bund.bfr.knime.pmm.common.MatrixXml;

/** Holds DB Matrices which can be accessed using their ids. */
public class DBMatrices {

	static List<MatrixXml> dbMatrices;

	private DBMatrices() {
	}

	/** Get matrices from DB with their ids as keys */
	public static List<MatrixXml> getDBMatrices() {
		if (dbMatrices == null) {
			dbMatrices = new LinkedList<>();
			ResultSet rs = DBKernel.getResultSet("SELECT * FROM \"Matrices\"",
					true);
			try {
				while (rs.next()) {
					dbMatrices.add(new MatrixXml(rs.getInt("ID"), rs
							.getString("Matrixname"), null));
				}
			} catch (SQLException e) {
			}
		}

		return dbMatrices;
	}
}