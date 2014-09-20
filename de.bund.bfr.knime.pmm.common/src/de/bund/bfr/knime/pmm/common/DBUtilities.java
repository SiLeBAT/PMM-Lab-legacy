package de.bund.bfr.knime.pmm.common;

import java.sql.Connection;

import org.hsh.bfr.db.DBKernel;

public class DBUtilities {

	private DBUtilities() {
	}

	public static LiteratureItem getLiteratureItem(Integer id) {
		return getLiteratureItem(null, id, null);
	}

	public static LiteratureItem getLiteratureItem(Connection conn, Integer id, String dbuuid) {
		String author = (String) DBKernel.getValue(conn, "Literatur", "ID", id
				+ "", "Erstautor");
		String title = (String) DBKernel.getValue(conn, "Literatur", "ID", id
				+ "", "Titel");
		String mAbstract = (String) DBKernel.getValue(conn, "Literatur", "ID",
				id + "", "Abstract");
		String journal = (String) DBKernel.getValue(conn, "Literatur", "ID", id
				+ "", "Journal");
		String volume = (String) DBKernel.getValue(conn, "Literatur", "ID", id
				+ "", "Volume");
		String issue = (String) DBKernel.getValue(conn, "Literatur", "ID", id
				+ "", "Issue");
		String website = (String) DBKernel.getValue(conn, "Literatur", "ID", id
				+ "", "Webseite");
		String comment = (String) DBKernel.getValue(conn, "Literatur", "ID", id
				+ "", "Kommentar");
		Integer year = (Integer) DBKernel.getValue("Literatur", "ID", id + "",
				"Jahr");
		Integer page = (Integer) DBKernel.getValue("Literatur", "ID", id + "",
				"Seite");
		Integer approvalMode = (Integer) DBKernel.getValue("Literatur", "ID",
				id + "", "FreigabeModus");
		Integer type = (Integer) DBKernel.getValue("Literatur", "ID", id + "",
				"Literaturtyp");

		LiteratureItem li = new LiteratureItem(author, year, title, mAbstract, journal,
				volume, issue, page, approvalMode, website, type, comment, id);
		li.setDbuuid(dbuuid);

		return li;
	}
}
