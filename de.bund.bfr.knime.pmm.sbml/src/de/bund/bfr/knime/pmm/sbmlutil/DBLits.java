package de.bund.bfr.knime.pmm.sbmlutil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hsh.bfr.db.DBKernel;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

public class DBLits {
	
	static List<LiteratureItem> dbLits = null;
	
	private DBLits() {
	}
	
	/** Get literature items */
	public static List<LiteratureItem> getDBLits() {
		if (dbLits == null) {
			dbLits = new LinkedList<>();
			ResultSet rs = DBKernel.getResultSet("SELECT * FROM \"Literatur\"", true);
			try {
				while (rs.next()) {
					Integer id = rs.getInt("ID");
					String author = rs.getString("Erstautor");
					String title = rs.getString("Titel");
					String mAbstract = rs.getString("Abstract");
					String journal = rs.getString("Journal");
					String volume = rs.getString("Volume");
					String issue = rs.getString("Issue");
					String website = rs.getString("Webseite");
					String comment = rs.getString("Kommentar");
					Integer year = rs.getInt("Jahr");
					Integer page = rs.getInt("Seite");
					Integer approvalMode = rs.getInt("FreigabeModus");
					Integer type = rs.getInt("Literaturtyp");
					
					LiteratureItem li = new LiteratureItem(author, year, title, mAbstract, journal,
							volume, issue, page, approvalMode, website, type, comment, id);
					dbLits.add(li);
				}
			} catch (SQLException e) {
			}
		}
		
		return dbLits;
	}
}
