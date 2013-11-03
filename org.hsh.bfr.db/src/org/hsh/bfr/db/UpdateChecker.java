/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
/**
 * 
 */
package org.hsh.bfr.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.hsh.bfr.db.imports.GeneralXLSImporter;
import org.hsh.bfr.db.imports.SQLScriptImporter;

/**
 * @author Weiser
 *
 */

// ACHTUNG: beim MERGEN sind sowohl KZ2NKZ als auch moveDblIntoDoubleKZ ohne Effekt!!! Da sie nicht im ChangeLog drin stehen!!!! Da muss KZ2NKZ nachträglich ausgeführt werden (solange die Tabelle Kennzahlen noch existiert). Bei moveDblIntoDoubleKZ???

public class UpdateChecker {
	public static void check4Updates_172_173() {	
		// Tonne in die Einnheitentabelle rein...
		/*
			DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("Einheiten") +
					" (" + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Einheit") + "," + DBKernel.delimitL("Beschreibung") +
					") VALUES (44,'°Bé','Grad Baumé')", false); // 31 -> 44
			DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("Einheiten") +
					" (" + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Einheit") + "," + DBKernel.delimitL("Beschreibung") +
					") VALUES (46,'U/min','Umdrehungen pro Minute')", false); // 32 -> 46
			DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("Einheiten") +
					" (" + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Einheit") + "," + DBKernel.delimitL("Beschreibung") +
					") VALUES (70,'mm','Millimeter')", false); // 35 -> 70
		 */
	}
	public static void check4Updates_171_172() {	
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelPrimView_172.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelSecView_172.sql", null, false);
	}
	public static void check4Updates_170_171() {	
		boolean isBios = false;
		if (isBios) {
			new GeneralXLSImporter(true).doImport("/org/hsh/bfr/db/res/Einheiten_130902.xls", null, false);
			DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL("Einheiten") + " WHERE " + DBKernel.delimitL("ID") + "=102", false);
			DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL("Einheiten") + " WHERE " + DBKernel.delimitL("ID") + "=101", false);
			DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL("Einheiten") + " WHERE " + DBKernel.delimitL("ID") + "=100", false);
			DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL("Einheiten") + " WHERE " + DBKernel.delimitL("ID") + "=84", false);
			DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL("Einheiten") + " WHERE " + DBKernel.delimitL("ID") + "=70", false);
			DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL("Einheiten") + " WHERE " + DBKernel.delimitL("ID") + "=46", false);
			DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL("Einheiten") + " WHERE " + DBKernel.delimitL("ID") + "=44", false);
			// 34 -> 35, 33 -> 31
			DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("Einheiten") +
					" (" + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Einheit") + "," + DBKernel.delimitL("Beschreibung") +
					") VALUES (44,'°Bé','Grad Baumé')", false); // 31 -> 44
			DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("Einheiten") +
					" (" + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Einheit") + "," + DBKernel.delimitL("Beschreibung") +
					") VALUES (46,'U/min','Umdrehungen pro Minute')", false); // 32 -> 46
			DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("Einheiten") +
					" (" + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Einheit") + "," + DBKernel.delimitL("Beschreibung") +
					") VALUES (70,'mm','Millimeter')", false); // 35 -> 70
			// 35 -> 70
			DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten_Sonstiges") +
					" SET " + DBKernel.delimitL("Einheit") + "=70 WHERE " + DBKernel.delimitL("Einheit") + "=35", false);
			// 32 -> 46
			DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten_Sonstiges") +
					" SET " + DBKernel.delimitL("Einheit") + "=46 WHERE " + DBKernel.delimitL("Einheit") + "=32", false);
			// 31 -> 44
			DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten_Sonstiges") +
					" SET " + DBKernel.delimitL("Einheit") + "=44 WHERE " + DBKernel.delimitL("Einheit") + "=31", false);
			// 33 -> 31
			DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten_Sonstiges") +
					" SET " + DBKernel.delimitL("Einheit") + "=31 WHERE " + DBKernel.delimitL("Einheit") + "=33", false);
			// 34 -> 35
			DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten_Sonstiges") +
					" SET " + DBKernel.delimitL("Einheit") + "=35 WHERE " + DBKernel.delimitL("Einheit") + "=34", false);
		}
		else {
			new GeneralXLSImporter().doImport("/org/hsh/bfr/db/res/Einheiten_130902.xls", null, false);
		}
	}
	public static void check4Updates_169_170() {	
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelPrimView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelSecView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("DepVarView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("IndepVarView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("ParamView") + ";", false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_ParamVarView_170.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_IndepVarView_170.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_DepVarView_170.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelPrimView_170.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelSecView_170.sql", null, false);
	}
	public static void check4Updates_168_169() {	
		new GeneralXLSImporter().doImport("/org/hsh/bfr/db/res/Einheiten_130823.xls", null, false);
	}
	public static void check4Updates_167_168() {	
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") + " ADD COLUMN " + DBKernel.delimitL("Name") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Versuchsbedingung"), false);
		updateChangeLog("GeschaetzteModelle", 1, false);		
		refreshFKs("GeschaetzteModelle");

		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelPrimView_168.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelSecView_168.sql", null, false);
	}
	public static void check4Updates_166_167() {	
		new GeneralXLSImporter().doImport("/org/hsh/bfr/db/res/SonstigeParameter_167.xls", null, false);
		try {
			ResultSet rs = DBKernel.getResultSet("SELECT " + DBKernel.delimitL("ID") + " FROM " + DBKernel.delimitL("Modellkatalog") +
					" WHERE " + DBKernel.delimitL("Level") + "=1", false); // primary
			if (rs != null && rs.first())  {
				do {
					DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("ModellkatalogParameter") +
							" SET " + DBKernel.delimitL("Einheit") + "=113 WHERE " +
							DBKernel.delimitL("Modell") + "=" + rs.getInt("ID") + " AND " +
							DBKernel.delimitL("Parametertyp") + "=1", false); // independent (1) -> Stunde (113)
					DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("ModellkatalogParameter") +
							" SET " + DBKernel.delimitL("Einheit") + "=1 WHERE " +
							DBKernel.delimitL("Modell") + "=" + rs.getInt("ID") + " AND " +
							DBKernel.delimitL("Parametertyp") + "=3", false); // dependent (3) -> log10(count/g) (1)
				} while (rs.next());
			}
		}
		catch (Exception e) {e.printStackTrace();}
	}
	public static void check4Updates_165_166() {	
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelPrimView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelSecView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("DepVarView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("IndepVarView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("ParamView") + ";", false);
		
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ModellkatalogParameter") +
				" DROP COLUMN " + DBKernel.delimitL("Kategorie"), false)) {
			updateChangeLog("ModellkatalogParameter", 7, true);		
			DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("ModellkatalogParameter") +
					" SET " + DBKernel.delimitL("Einheit") + " = NULL", false);
			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ModellkatalogParameter") +
					" ALTER COLUMN " + DBKernel.delimitL("Einheit") + " INTEGER", false);
		}
		refreshFKs("ModellkatalogParameter");
		
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_ParamVarView_166.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_IndepVarView_166.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_DepVarView_166.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelPrimView_165.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelSecView_165.sql", null, false);
	}
	public static void check4Updates_164_165() {	
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelPrimView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelSecView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("ParamView") + ";", false);
		
  		Integer idS = DBKernel.getID("Einheiten", new String[] {"Einheit", "kind of property / quantity"}, new String[] {"s", "Time"});
  		Integer idM = DBKernel.getID("Einheiten", new String[] {"Einheit", "kind of property / quantity"}, new String[] {"min", "Time"});
  		Integer idH = DBKernel.getID("Einheiten", new String[] {"Einheit", "kind of property / quantity"}, new String[] {"h", "Time"});
  		Integer idMo = DBKernel.getID("Einheiten", new String[] {"Einheit", "kind of property / quantity"}, new String[] {"mo_j", "Time"});
  		Integer idJ = DBKernel.getID("Einheiten", new String[] {"Einheit", "kind of property / quantity"}, new String[] {"a_j", "Time"});

  		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("GeschaetzteParameter") +
				" SET " + DBKernel.delimitL("ZeitEinheit") + "='" + idS + "'  WHERE " + DBKernel.delimitL("ZeitEinheit") + "='Sekunde'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("GeschaetzteParameter") +
				" SET " + DBKernel.delimitL("ZeitEinheit") + "='" + idM + "'  WHERE " + DBKernel.delimitL("ZeitEinheit") + "='Minute'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("GeschaetzteParameter") +
				" SET " + DBKernel.delimitL("ZeitEinheit") + "='" + idH + "'  WHERE " + DBKernel.delimitL("ZeitEinheit") + "='Stunde'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("GeschaetzteParameter") +
				" SET " + DBKernel.delimitL("ZeitEinheit") + "='86'  WHERE " + DBKernel.delimitL("ZeitEinheit") + "='Tag'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("GeschaetzteParameter") +
				" SET " + DBKernel.delimitL("ZeitEinheit") + "='92'  WHERE " + DBKernel.delimitL("ZeitEinheit") + "='Woche'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("GeschaetzteParameter") +
				" SET " + DBKernel.delimitL("ZeitEinheit") + "='" + idMo + "'  WHERE " + DBKernel.delimitL("ZeitEinheit") + "='Monat'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("GeschaetzteParameter") +
				" SET " + DBKernel.delimitL("ZeitEinheit") + "='" + idJ + "'  WHERE " + DBKernel.delimitL("ZeitEinheit") + "='Jahr'", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameter") + " ALTER COLUMN " + DBKernel.delimitL("ZeitEinheit") + " INTEGER", false);
		refreshFKs("GeschaetzteParameter");

		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_ParamVarView_165.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelPrimView_165.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelSecView_165.sql", null, false);		
	}
	public static void check4Updates_163_164() {	
		/*
		// für Krisen EHEC-DB, da wurde wohl ein Update mitten "im Übergang" von check4Updates_162_163 gemacht
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("MesswerteEinfach"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " DROP COLUMN " + DBKernel.delimitL("name"), false);
		updateChangeLog("Einheiten", 3, true);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " DROP COLUMN " + DBKernel.delimitL("kind of quantity"), false);
		updateChangeLog("Einheiten", 3, true);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " DROP COLUMN " + DBKernel.delimitL("print"), false);
		updateChangeLog("Einheiten", 3, true);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " DROP COLUMN " + DBKernel.delimitL("c/s"), false);
		updateChangeLog("Einheiten", 3, true);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " DROP COLUMN " + DBKernel.delimitL("c/i"), false);
		updateChangeLog("Einheiten", 3, true);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " DROP COLUMN " + DBKernel.delimitL("M"), false);
		updateChangeLog("Einheiten", 3, true);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " DROP COLUMN " + DBKernel.delimitL("definition value"), false);
		updateChangeLog("Einheiten", 3, true);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " DROP COLUMN " + DBKernel.delimitL("definition unit"), false);
		updateChangeLog("Einheiten", 3, true);		
		check4Updates_162_163();
		*/
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/03_create_messwerteeinfach_164.sql", null, false);
	}
	public static void check4Updates_162_163() {	
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " ADD COLUMN " + DBKernel.delimitL("name") + " VARCHAR(255)", false);
		updateChangeLog("Einheiten", 3, false);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " ADD COLUMN " + DBKernel.delimitL("kind of property / quantity") + " VARCHAR(255)", false);
		updateChangeLog("Einheiten", 4, false);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " ADD COLUMN " + DBKernel.delimitL("notation case sensitive") + " VARCHAR(255)", false);
		updateChangeLog("Einheiten", 5, false);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " ADD COLUMN " + DBKernel.delimitL("convert to") + " VARCHAR(255)", false);
		updateChangeLog("Einheiten", 6, false);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " ADD COLUMN " + DBKernel.delimitL("conversion function / factor") + " VARCHAR(255)", false);
		updateChangeLog("Einheiten", 7, false);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " ADD COLUMN " + DBKernel.delimitL("inverse conversion function / factor") + " VARCHAR(255)", false);
		updateChangeLog("Einheiten", 8, false);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " ADD COLUMN " + DBKernel.delimitL("object type") + " VARCHAR(255)", false);
		updateChangeLog("Einheiten", 9, false);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " ADD COLUMN " + DBKernel.delimitL("display in GUI as") + " VARCHAR(255)", false);
		updateChangeLog("Einheiten", 10, false);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " ADD COLUMN " + DBKernel.delimitL("MathML string") + " VARCHAR(255)", false);
		updateChangeLog("Einheiten", 11, false);		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Einheiten") + " ADD COLUMN " + DBKernel.delimitL("Priority for display in GUI") + " BOOLEAN", false);
		updateChangeLog("Einheiten", 12, false);		
		refreshFKs("Einheiten");
		
		new GeneralXLSImporter().doImport("/org/hsh/bfr/db/res/Einheiten_New.xls", null, false);
/*
		// sync der firstDB with SiLeBAT - DB!!!
  		for (int i=112;i>=94;i--) {
	  		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Einheiten") +
	  				" SET " + DBKernel.delimitL("ID") + "=" + (i+9) + " WHERE " + DBKernel.delimitL("ID") + "=" + i, false);
  		}
  		for (int i=99;i>=85;i--) {
	  		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Einheiten") +
	  				" SET " + DBKernel.delimitL("ID") + "=" + i + " WHERE " + DBKernel.delimitL("ID") + "=" + (i-6), false);
  		}
  		for (int i=83;i>=71;i--) {
	  		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Einheiten") +
	  				" SET " + DBKernel.delimitL("ID") + "=" + i + " WHERE " + DBKernel.delimitL("ID") + "=" + (i-5), false);
  		}
  		for (int i=69;i>=47;i--) {
	  		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Einheiten") +
	  				" SET " + DBKernel.delimitL("ID") + "=" + i + " WHERE " + DBKernel.delimitL("ID") + "=" + (i-4), false);
  		}
  		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Einheiten") +
  				" SET " + DBKernel.delimitL("ID") + "=" + 45 + " WHERE " + DBKernel.delimitL("ID") + "=" + 42, false);
  		for (int i=43;i>=38;i--) {
	  		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Einheiten") +
	  				" SET " + DBKernel.delimitL("ID") + "=" + i + " WHERE " + DBKernel.delimitL("ID") + "=" + (i-2), false);
  		}
		// sync der firstDB with SiLeBAT - DB!!!
  		*/
  		Integer idS = DBKernel.getID("Einheiten", new String[] {"Einheit", "kind of property / quantity"}, new String[] {"s", "Time"});
  		Integer idM = DBKernel.getID("Einheiten", new String[] {"Einheit", "kind of property / quantity"}, new String[] {"min", "Time"});
  		Integer idH = DBKernel.getID("Einheiten", new String[] {"Einheit", "kind of property / quantity"}, new String[] {"h", "Time"});
  		Integer idMo = DBKernel.getID("Einheiten", new String[] {"Einheit", "kind of property / quantity"}, new String[] {"mo_j", "Time"});
  		Integer idJ = DBKernel.getID("Einheiten", new String[] {"Einheit", "kind of property / quantity"}, new String[] {"a_j", "Time"});

  		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("MesswerteEinfach") + ";", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte") +
				" SET " + DBKernel.delimitL("ZeitEinheit") + "='" + idS + "'  WHERE " + DBKernel.delimitL("ZeitEinheit") + "='Sekunde'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte") +
				" SET " + DBKernel.delimitL("ZeitEinheit") + "='" + idM + "'  WHERE " + DBKernel.delimitL("ZeitEinheit") + "='Minute'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte") +
				" SET " + DBKernel.delimitL("ZeitEinheit") + "='" + idH + "'  WHERE " + DBKernel.delimitL("ZeitEinheit") + "='Stunde'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte") +
				" SET " + DBKernel.delimitL("ZeitEinheit") + "='86'  WHERE " + DBKernel.delimitL("ZeitEinheit") + "='Tag'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte") +
				" SET " + DBKernel.delimitL("ZeitEinheit") + "='92'  WHERE " + DBKernel.delimitL("ZeitEinheit") + "='Woche'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte") +
				" SET " + DBKernel.delimitL("ZeitEinheit") + "='" + idMo + "'  WHERE " + DBKernel.delimitL("ZeitEinheit") + "='Monat'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte") +
				" SET " + DBKernel.delimitL("ZeitEinheit") + "='" + idJ + "'  WHERE " + DBKernel.delimitL("ZeitEinheit") + "='Jahr'", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Messwerte") + " ALTER COLUMN " + DBKernel.delimitL("ZeitEinheit") + " INTEGER", false);
		refreshFKs("Messwerte");

		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/03_create_messwerteeinfach_163.sql", null, false);
	}
	public static void check4Updates_161_162() {		
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ModellkatalogParameter") +
				" ADD COLUMN " + DBKernel.delimitL("Kategorie") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Beschreibung"), false)) {
			updateChangeLog("ModellkatalogParameter", 7, false);		
			if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ModellkatalogParameter") +
					" ADD COLUMN " + DBKernel.delimitL("Einheit") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Beschreibung"), false)) {
				updateChangeLog("ModellkatalogParameter", 8, false);		
			}
		}
		refreshFKs("ModellkatalogParameter");

		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelPrimView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelSecView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("ParamView") + ";", false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_ParamVarView_162.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_IndepVarView_162.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_DepVarView_162.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelPrimView_162.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelSecView_162.sql", null, false);		
	}
	public static void check4Updates_160_161() {		
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("SonstigeParameter") +
				" ADD COLUMN " + DBKernel.delimitL("Kategorie") + " VARCHAR(255)", false))
			updateChangeLog("SonstigeParameter", 3, false);		
		refreshFKs("SonstigeParameter");
	}
	public static void check4Updates_159_160() {		
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_SonstigesEinfach_160.sql", null, false);
	}
	public static void check4Updates_158_159() {		
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelPrimView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelSecView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("ParamView") + ";", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameter") +
				" ALTER COLUMN " + DBKernel.delimitL("Konz_Einheit") + " RENAME TO " + DBKernel.delimitL("Einheit"), false);
		refreshFKs("GeschaetzteParameter");
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_ParamVarView_159.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_IndepVarView_159.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelPrimView_159.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelSecView_159.sql", null, false);		
	}
	public static void check4Updates_157_158() {		
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelPrimView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelSecView") + ";", false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/04_create_versuchsbedingungeneinfach_156.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_ParamVarView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelPrimView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelSecView.sql", null, false);		
	}
	public static void check4Updates_156_157() {		
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelPrimView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelSecView") + ";", false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_IndepVarView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelPrimView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelSecView.sql", null, false);		
	}
	public static void check4Updates_155_156() {		
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/04_create_versuchsbedingungeneinfach_156.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelPrimView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelSecView.sql", null, false);		
	}
	public static void check4Updates_154_155() {		
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/01_sonstigeseinfach_155.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/03_create_messwerteeinfach_155.sql", null, false);
	}
	public static void check4Updates_153_154() {		
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelPrimView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelSecView") + ";", false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_ParamVarView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelPrimView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelSecView.sql", null, false);		
	}
	public static void check4Updates_152_153() {		
		MyDBTables.getTable("LinkedTestConditions").createTable();
		DBKernel.grantDefaults("LinkedTestConditions");
	}
	public static void check4Updates_151_152() {		
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/CombaseRawDataImport.sql", null, false);
	}
	public static void check4Updates_150_151() {		
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_DepVarView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_IndepVarView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_LitEmView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_LitMView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_ParamVarView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_VarParMapView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelPrimView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelSecView.sql", null, false);		
	}
	public static void check4Updates_149_150() {
		
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/01_sonstigeseinfach.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/02_create_doublekennzahleneinfach.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/03_create_messwerteeinfach.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/04_create_versuchsbedingungeneinfach.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/05_create_modelview.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/06_create_estmodelprimview.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/07_create_estmodelsecview.sql", null, false);
		
	}
	public static void check4Updates_148_149() {
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/create_view_set.sql", null, false);
	}
	public static void check4Updates_147_148() {
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten_Messwerte") +
				" ADD COLUMN " + DBKernel.delimitL("ExperimentID") + " INTEGER BEFORE " + DBKernel.delimitL("Agens"), false))
			updateChangeLog("Prozessdaten_Messwerte", 2, false);		
		refreshFKs("Prozessdaten_Messwerte");	
		
		if (DBKernel.sendRequest(SQL_CREATE_VIEW_DOUBLE, false)) {
			DBKernel.grantDefaults("DoubleKennzahlenEinfach");
		}
		if (DBKernel.sendRequest(SQL_CREATE_VIEW_DATA, false)) {
			DBKernel.grantDefaults("MesswerteEinfach");
		}
		if (DBKernel.sendRequest(SQL_CREATE_VIEW_CONDITION, false)) {
			DBKernel.grantDefaults("VersuchsbedingungenEinfach");
		}
		if (DBKernel.sendRequest(SQL_CREATE_VIEW_MISC, false)) {
			DBKernel.grantDefaults("SonstigesEinfach");
		}
	}
	public static void check4Updates_146_147() {
		MyDBTables.getTable("PMMLabWorkflows").createTable();
		DBKernel.grantDefaults("PMMLabWorkflows");
		MyDBTables.getTable("DataSource").createTable();
		DBKernel.grantDefaults("DataSource");

		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("PMMLabWF") + " INTEGER BEFORE " + DBKernel.delimitL("Guetescore"), false))
			updateChangeLog("GeschaetzteModelle", 16, false);		
		refreshFKs("GeschaetzteModelle");		
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Modellkatalog") +
				" ADD COLUMN " + DBKernel.delimitL("Ableitung") + " INTEGER BEFORE " + DBKernel.delimitL("Software"), false))
			updateChangeLog("Modellkatalog", 10, false);		
		refreshFKs("Modellkatalog");		
	}
	public static void check4Updates_145_146() {
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("Guetescore") + " INTEGER BEFORE " + DBKernel.delimitL("Kommentar"), false))
			updateChangeLog("GeschaetzteModelle", 16, false);		
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("Geprueft") + " BOOLEAN", false))
			updateChangeLog("GeschaetzteModelle", 18, false);	
		refreshFKs("GeschaetzteModelle");
	}
	public static void check4Updates_144_145() {
		
		MyDBTables.getTable("Chargen").createTable();
		DBKernel.grantDefaults("Chargen");
		MyDBTables.getTable("ChargenVerbindungen").createTable();
		DBKernel.grantDefaults("ChargenVerbindungen");
		//DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL("LieferungVerbindungen"), false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("LieferungVerbindungen") + " IF EXISTS", false);
		
		DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL("Lieferungen") + " WHERE " + DBKernel.delimitL("Unitmenge") + "=0", false);

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Produktkatalog") +
				" ALTER COLUMN " + DBKernel.delimitL("Lieferungen") + " RENAME TO " + DBKernel.delimitL("Chargen"), false);
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Lieferungen") +
				" ADD COLUMN " + DBKernel.delimitL("Charge") + " INTEGER BEFORE " + DBKernel.delimitL("Artikel"), false)) {
			updateChangeLog("Lieferungen", 1, false);		
		}
		DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("Chargen") +
				" (" + DBKernel.delimitL("Artikel") + "," + DBKernel.delimitL("ChargenNr") + "," + DBKernel.delimitL("MHD") +
				") SELECT DISTINCT " + DBKernel.delimitL("Artikel") + "," +
				DBKernel.delimitL("ChargenNr") + "," + DBKernel.delimitL("MHD") +
				" FROM " + DBKernel.delimitL("Lieferungen"), false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Lieferungen") +
				" SET " + DBKernel.delimitL("Charge") + "=" +
				" SELECT " + DBKernel.delimitL("ID") + " FROM " + DBKernel.delimitL("Chargen") + " WHERE " +
				DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("Artikel") + "=" + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Artikel"), false);
		
		DBKernel.doMNs(MyDBTables.getTable("Chargen"));

		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Lieferungen") + " DROP COLUMN " + DBKernel.delimitL("Zielprodukt"), false)) {
			updateChangeLog("Lieferungen", 14, true);		
		}
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Lieferungen") + " DROP COLUMN " + DBKernel.delimitL("Vorprodukt"), false)) {
			updateChangeLog("Lieferungen", 13, true);		
		}
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Lieferungen") + " DROP COLUMN " + DBKernel.delimitL("MHD"), false)) {
			updateChangeLog("Lieferungen", 4, true);		
		}
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Lieferungen") + " DROP COLUMN " + DBKernel.delimitL("ChargenNr"), false)) {
			updateChangeLog("Lieferungen", 3, true);		
		}
		refreshFKs("Lieferungen", true);
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Lieferungen") + " DROP COLUMN " + DBKernel.delimitL("Artikel"), false)) {
			updateChangeLog("Lieferungen", 2, true);		
		}
		refreshFKs("Lieferungen");
		refreshFKs("Produktkatalog");		
	}
	public static void check4Updates_143_144() {
		boolean refreshFK = false;
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("RMS") + " DOUBLE BEFORE " + DBKernel.delimitL("Score"), DBKernel.isKNIME)) {
			updateChangeLog("GeschaetzteModelle", 7, false);		
			refreshFK = true;
		}
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("AIC") + " DOUBLE BEFORE " + DBKernel.delimitL("Score"), DBKernel.isKNIME)) {
			updateChangeLog("GeschaetzteModelle", 8, false);			
			refreshFK = true;
		}
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("BIC") + " DOUBLE BEFORE " + DBKernel.delimitL("Score"), DBKernel.isKNIME)) {
			updateChangeLog("GeschaetzteModelle", 9, false);
			refreshFK = true;
		}
		if (refreshFK) refreshFKs("GeschaetzteModelle");
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameter") +
				" ADD COLUMN " + DBKernel.delimitL("StandardError") + " DOUBLE BEFORE " + DBKernel.delimitL("t"), DBKernel.isKNIME)) {
			updateChangeLog("GeschaetzteParameter", 9, false);
			refreshFKs("GeschaetzteParameter");			
		}
		if (DBKernel.getID("Parametertyp", "Parametertyp", 4+"") == null) {
			DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("Parametertyp") +
					" (" + DBKernel.delimitL("Parametertyp") + ") VALUES (4)", DBKernel.isKNIME);
		}
		
		MyDBTables.getTable("VarParMaps").createTable(true);
		DBKernel.grantDefaults("VarParMaps");
		
		MyDBTables.getTable("Kostenkatalog").createTable();
		DBKernel.grantDefaults("Kostenkatalog");
		MyDBTables.getTable("Kostenkatalogpreise").createTable();
		DBKernel.grantDefaults("Kostenkatalogpreise");
		MyDBTables.getTable("Prozessdaten_Kosten").createTable();
		DBKernel.grantDefaults("Prozessdaten_Kosten");
		MyDBTables.getTable("Zutatendaten_Kosten").createTable();
		DBKernel.grantDefaults("Zutatendaten_Kosten");
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten") +
				" ADD COLUMN " + DBKernel.delimitL("Kosten") + " INTEGER BEFORE " + DBKernel.delimitL("Guetescore"), DBKernel.isKNIME)) {
			updateChangeLog("Prozessdaten", 20, false);
			refreshFKs("Prozessdaten");			
		}
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ADD COLUMN " + DBKernel.delimitL("Kosten") + " INTEGER BEFORE " + DBKernel.delimitL("Kommentar"), DBKernel.isKNIME)) {
			updateChangeLog("Zutatendaten", 18, false);
			refreshFKs("Zutatendaten");			
		}
		if (!DBKernel.isKNIME) new GeneralXLSImporter().doImport("/org/hsh/bfr/db/res/Kostenkatalog_mit_Einheiten.xls", DBKernel.mainFrame.getProgressBar(), false);
		
		// Krise
		refreshFKs("Knoten", true);
		refreshFKs("Knoten_Agenzien", true);
		refreshFKs("Produktkatalog", true);
		refreshFKs("Produktkatalog_Matrices", true);
		refreshFKs("Lieferungen", true);
		refreshFKs("LieferungVerbindungen", true);
		if (DBKernel.isKNIME) DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Knoten") + " IF EXISTS", false);
		MyDBTables.getTable("Station").createTable();
		DBKernel.grantDefaults("Station");
		if (DBKernel.isKNIME) DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Knoten_Agenzien") + " IF EXISTS", false);
		MyDBTables.getTable("Station_Agenzien").createTable();
		DBKernel.grantDefaults("Station_Agenzien");
		if (DBKernel.isKNIME) DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Produktkatalog") + " IF EXISTS", false);
		MyDBTables.getTable("Produktkatalog").createTable();
		DBKernel.grantDefaults("Produktkatalog");
		if (DBKernel.isKNIME) DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Produktkatalog_Matrices") + " IF EXISTS", false);
		MyDBTables.getTable("Produktkatalog_Matrices").createTable();
		DBKernel.grantDefaults("Produktkatalog_Matrices");
		if (DBKernel.isKNIME) DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Lieferungen") + " IF EXISTS", false);
		MyDBTables.getTable("Lieferungen").createTable();
		DBKernel.grantDefaults("Lieferungen");
		if (DBKernel.isKNIME) DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("LieferungVerbindungen") + " IF EXISTS", false);
		MyTable myT = MyDBTables.getTable("LieferungVerbindungen");
		if (myT != null) {
			myT.createTable();
			DBKernel.grantDefaults("LieferungVerbindungen");
		}

		if (DBKernel.isKNIME) {
		    try {
		    	if (!DBKernel.getUsername().equals("SA")) DBKernel.getDBConnection().createStatement().execute("CREATE USER " + DBKernel.delimitL("SA") + " PASSWORD '' ADMIN");
		    	else DBKernel.getDBConnection().createStatement().execute("DROP USER " + DBKernel.delimitL("defad"));
		    }
		    catch (Exception e) {if (!DBKernel.isKNIME) MyLogger.handleException(e);}			
		}
	}
	public static void check4Updates_142_143() {
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Codes-Matrices_U"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Codes-Matrices_D"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Codes-Matrices_I"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Codes-Agenzien_U"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Codes-Agenzien_D"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Codes-Agenzien_I"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Codes-Methoden_U"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Codes-Methoden_D"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Codes-Methoden_I"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Codes-Methodiken_U"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Codes-Methodiken_D"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Codes-Methodiken_I"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Prozess-Verbindungen_U"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Prozess-Verbindungen_D"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Prozess-Verbindungen_I"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Prozess-Workflow_U"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Prozess-Workflow_D"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Prozess-Workflow_I"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("B_Prozess-Workflow_U"), false);
		
	}
	public static void check4Updates_141_142() {
		//wegen recreateTriggers muss das hier noch vorgezogen werden, bei Alex die Triggers auch nochmal recreaten!
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozess_Workflow") +
				" RENAME TO " + DBKernel.delimitL("ProzessWorkflow"), false);
		refreshFKs("ProzessWorkflow");
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DateiSpeicher") + " SET " + DBKernel.delimitL("Tabelle") + "='ProzessWorkflow' WHERE " + DBKernel.delimitL("Tabelle") + "='Prozess_Workflow'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("ChangeLog") + " SET " + DBKernel.delimitL("Tabelle") + "='ProzessWorkflow' WHERE " + DBKernel.delimitL("Tabelle") + "='Prozess_Workflow'", false);

		// ACHTUNG: Alex hat bereits jetzt schon diese DB 1.4.2 - von hier
		MyDBTables.recreateTriggers();
		Integer nextID = getNextID("ChangeLog");
		MyLogger.handleMessage("getNextID(ChangeLog): " + nextID);
		DBKernel.sendRequest("CREATE SEQUENCE " + DBKernel.delimitL("ChangeLogSEQ") + " AS INTEGER START WITH " + nextID + " INCREMENT BY 1", false);
		DBKernel.sendRequest("GRANT USAGE ON SEQUENCE " + DBKernel.delimitL("ChangeLogSEQ") + " TO " + DBKernel.delimitL("PUBLIC"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ChangeLog") +
				" DROP PRIMARY KEY", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ChangeLog") +
				" ALTER COLUMN " + DBKernel.delimitL("ID") + " INTEGER", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ChangeLog") +
				" ADD PRIMARY KEY(" + DBKernel.delimitL("ID") + ")", false);
		// geht irgendwie nicht... hier manuell nachhelfen!!!!!
		// INTEGER GENERATED BY DEFAULT AS SEQUENCE PUBLIC."ChangeLogSEQ" NOT NULL PRIMARY KEY
//		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ChangeLog") +
	//			" ALTER COLUMN " + DBKernel.delimitL("ID") + " INTEGER GENERATED BY DEFAULT AS SEQUENCE " + DBKernel.delimitL("ChangeLogSEQ") + " NOT NULL PRIMARY KEY", false);
		refreshFKs("ChangeLog");

		DBKernel.sendRequest("DROP FUNCTION IF EXISTS LD", false); 
		/*
	    DBKernel.sendRequest(
	    		"CREATE PROCEDURE INSERTINTOCL(IN tableName VARCHAR(255), IN oldrow OBJECT, IN newrow OBJECT)\n" +
	    	    		"MODIFIES SQL DATA\n" +
	    	    		"BEGIN ATOMIC\n" + 
	    	    		"  DECLARE tableID INTEGER;\n" + 
	    	    		"  IF oldrow = NULL THEN\n" +
	    	    		"    SET tableID = 1;" +
	    	    		"  ELSE\n" +
	    	    		"    SET tableID = 2;" +
	    	    		"  END IF;\n" +

	    	    		"  INSERT INTO " + DBKernel.delimitL("ChangeLog") +
	    	    		" (" + DBKernel.delimitL("ID") + ", " + DBKernel.delimitL("Zeitstempel") + ", " + DBKernel.delimitL("Username") + ", " +
	    	    			DBKernel.delimitL("Tabelle") + ", " + DBKernel.delimitL("TabellenID") + ") VALUES (NEXT VALUE FOR " + DBKernel.delimitL("ChangeLogSEQ") +
	    	    		", CURRENT_TIMESTAMP, 'username', tableName, tableID);\n" + 
	    	    		"END"
	    		, false);
		 */
		// ACHTUNG: Alex hat bereits jetzt schon diese DB 1.4.2 - bis hierher
		
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DoubleKennzahlen") +
				" ADD COLUMN " + DBKernel.delimitL("Referenz") + " INTEGER", false);
		updateChangeLog("DoubleKennzahlen", 30, false);
		refreshFKs("DoubleKennzahlen");		
		
		MyTable pdl = MyDBTables.getTable("Prozessdaten_Literatur");
		pdl.createTable(false);
		DBKernel.grantDefaults("Prozessdaten_Literatur");
		DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("Prozessdaten_Literatur") +
				"(" + DBKernel.delimitL("Prozessdaten") + "," + DBKernel.delimitL("Literatur") +
				") SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Referenz") +
				" FROM " + DBKernel.delimitL("Prozessdaten"), false);
		refreshFKs("Prozessdaten");		
		DBKernel.doMNs(MyDBTables.getTable("Prozessdaten"));
		
		pdl = MyDBTables.getTable("ProzessWorkflow_Literatur");
		pdl.createTable(false);
		DBKernel.grantDefaults("ProzessWorkflow_Literatur");
		DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("ProzessWorkflow_Literatur") +
				"(" + DBKernel.delimitL("ProzessWorkflow") + "," + DBKernel.delimitL("Literatur") +
				") SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Referenz") +
				" FROM " + DBKernel.delimitL("ProzessWorkflow"), false);
		refreshFKs("ProzessWorkflow");		
		DBKernel.doMNs(MyDBTables.getTable("ProzessWorkflow"));
	}
	private static Integer getNextID(final String tablename) {
		Integer result = null;
	    try {
	    	ResultSet rs = DBKernel.getResultSet("SELECT MAX(" + DBKernel.delimitL("ID") + ") FROM " + DBKernel.delimitL(tablename), false);
	    	if (rs != null && rs.first()) {
		    	result = rs.getInt(1) + 1;
		    	rs.close();
	    	}
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
		return result;
	}
	public static void check4Updates_140_141() {
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Modellkatalog") +
				" ADD COLUMN " + DBKernel.delimitL("Parameter") + " INTEGER BEFORE " + DBKernel.delimitL("Kommentar"), false);
		updateChangeLog("Modellkatalog", 11, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Modellkatalog") +
				" ADD COLUMN " + DBKernel.delimitL("Referenzen") + " INTEGER BEFORE " + DBKernel.delimitL("Kommentar"), false);
		updateChangeLog("Modellkatalog", 12, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Modellkatalog") + " ALTER COLUMN " + DBKernel.delimitL("Name") + " SET NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Modellkatalog") + " ALTER COLUMN " + DBKernel.delimitL("Notation") + " SET NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Modellkatalog") + " ALTER COLUMN " + DBKernel.delimitL("Eingabedatum") + " SET NULL", false);
		refreshFKs("Modellkatalog");
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ModellkatalogParameter") + " ALTER COLUMN " + DBKernel.delimitL("Parametername") + " SET NULL", false);
		refreshFKs("ModellkatalogParameter");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameterCovCor") + " ALTER COLUMN " + DBKernel.delimitL("param1") + " SET NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameterCovCor") + " ALTER COLUMN " + DBKernel.delimitL("param2") + " SET NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameterCovCor") + " ALTER COLUMN " + DBKernel.delimitL("GeschaetztesModell") + " SET NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameterCovCor") + " ALTER COLUMN " + DBKernel.delimitL("cor") + " SET NULL", false);
		refreshFKs("GeschaetzteParameterCovCor");

		MyDBTables.getTable("Parametertyp").createTable(false);
		DBKernel.grantDefaults("Parametertyp");
		PreparedStatement ps;
		try {
			ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Parametertyp") +
					" (" + DBKernel.delimitL("Parametertyp") + ") VALUES (?)");
			ps.setInt(1, 1); ps.execute();
			ps.setInt(1, 2); ps.execute();
			ps.setInt(1, 3); ps.execute();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		MyDBTables.getTable("GueltigkeitsBereiche").createTable(false);
		DBKernel.grantDefaults("GueltigkeitsBereiche");
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("GueltigkeitsBereiche") + " INTEGER BEFORE " + DBKernel.delimitL("Kommentar"), false);
		updateChangeLog("GeschaetzteModelle", 11, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("PMML") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Kommentar"), false);
		updateChangeLog("GeschaetzteModelle", 12, false);
		refreshFKs("GeschaetzteModelle");
	}
	public static void check4Updates_139_140() {
		// das muss hier noch bei Buschulte, Niederberger und Burchardi nachgeholt werden
		String tableName = "Literatur";
		DBKernel.sendRequest("CREATE TRIGGER " + DBKernel.delimitL("A_" + tableName + "_I") + " AFTER INSERT ON " + DBKernel.delimitL(tableName) + " FOR EACH ROW " + " CALL " + DBKernel.delimitL(new MyTrigger().getClass().getName()), false); 
		tableName = "GeschaetzteModelle"; DBKernel.sendRequest("CREATE TRIGGER " + DBKernel.delimitL("A_" + tableName + "_I") + " AFTER INSERT ON " + DBKernel.delimitL(tableName) + " FOR EACH ROW " + " CALL " + DBKernel.delimitL(new MyTrigger().getClass().getName()), false); 
		tableName = "GeschaetztesModell_Referenz"; DBKernel.sendRequest("CREATE TRIGGER " + DBKernel.delimitL("A_" + tableName + "_I") + " AFTER INSERT ON " + DBKernel.delimitL(tableName) + " FOR EACH ROW " + " CALL " + DBKernel.delimitL(new MyTrigger().getClass().getName()), false); 
		tableName = "GeschaetzteParameter"; DBKernel.sendRequest("CREATE TRIGGER " + DBKernel.delimitL("A_" + tableName + "_I") + " AFTER INSERT ON " + DBKernel.delimitL(tableName) + " FOR EACH ROW " + " CALL " + DBKernel.delimitL(new MyTrigger().getClass().getName()), false); 
		tableName = "GeschaetzteParameterCovCor"; DBKernel.sendRequest("CREATE TRIGGER " + DBKernel.delimitL("A_" + tableName + "_I") + " AFTER INSERT ON " + DBKernel.delimitL(tableName) + " FOR EACH ROW " + " CALL " + DBKernel.delimitL(new MyTrigger().getClass().getName()), false); 
		tableName = "Sekundaermodelle_Primaermodelle"; DBKernel.sendRequest("CREATE TRIGGER " + DBKernel.delimitL("A_" + tableName + "_I") + " AFTER INSERT ON " + DBKernel.delimitL(tableName) + " FOR EACH ROW " + " CALL " + DBKernel.delimitL(new MyTrigger().getClass().getName()), false); 

		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ComBaseImport") + " ADD COLUMN " + DBKernel.delimitL("Referenz") + " INTEGER BEFORE " + DBKernel.delimitL("Agensname"), false)) {
			MyLogger.handleMessage("ACHTUNG!!!! check4Updates_139_140, ComBaseImport, Referenz wurde upgedatet!!!!! -> updateChangeLog unbedingt checken!!!!!");
			updateChangeLog("ComBaseImport", 1, false);
			refreshFKs("ComBaseImport");				
		}
		else {
			MyLogger.handleMessage("kein check4Updates_139_140, ComBaseImport, Referenz Update");
		}
	}
	
	public static void check4Updates_138_139() {
		check4Updates_138_139(false);
	}
	public static void check4Updates_138_139(final boolean weseSpecial) {
		if (!weseSpecial) {
			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten") +
					" ADD COLUMN " + DBKernel.delimitL("ProzessDetail") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Kapazitaet"), false);
			updateChangeLog("Prozessdaten", 5, false);
			refreshFKs("Prozessdaten");
			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
					" ADD COLUMN " + DBKernel.delimitL("Aufbereitungsverfahren") + " BOOLEAN BEFORE " + DBKernel.delimitL("Extraktionssystem_Bezeichnung"), false);
			updateChangeLog("Kits", 11, false);
			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
					" ADD COLUMN " + DBKernel.delimitL("Nachweisverfahren") + " BOOLEAN BEFORE " + DBKernel.delimitL("Extraktionssystem_Bezeichnung"), false);
			updateChangeLog("Kits", 12, false);
			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
					" ADD COLUMN " + DBKernel.delimitL("Quantitativ") + " BOOLEAN BEFORE " + DBKernel.delimitL("Format"), false);
			updateChangeLog("Kits", 18, false);
			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
					" ADD COLUMN " + DBKernel.delimitL("Identifizierung") + " BOOLEAN BEFORE " + DBKernel.delimitL("Format"), false);
			updateChangeLog("Kits", 19, false);
			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
					" ADD COLUMN " + DBKernel.delimitL("Typisierung") + " BOOLEAN BEFORE " + DBKernel.delimitL("Format"), false);
			updateChangeLog("Kits", 20, false);
			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
					" ADD COLUMN " + DBKernel.delimitL("Methoden") + " INTEGER BEFORE " + DBKernel.delimitL("Format"), false);
			updateChangeLog("Kits", 21, false);
			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
					" ADD COLUMN " + DBKernel.delimitL("Matrix") + " INTEGER BEFORE " + DBKernel.delimitL("Format"), false);
			updateChangeLog("Kits", 22, false);
			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
					" ADD COLUMN " + DBKernel.delimitL("MatrixDetail") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Format"), false);
			updateChangeLog("Kits", 23, false);
			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
					" ADD COLUMN " + DBKernel.delimitL("Agens") + " INTEGER BEFORE " + DBKernel.delimitL("Format"), false);
			updateChangeLog("Kits", 24, false);
			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
					" ADD COLUMN " + DBKernel.delimitL("AgensDetail") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Format"), false);
			updateChangeLog("Kits", 25, false);
			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
					" ADD COLUMN " + DBKernel.delimitL("Spezialequipment") + " BOOLEAN BEFORE " + DBKernel.delimitL("Format"), false);
			updateChangeLog("Kits", 26, false);
			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
					" ADD COLUMN " + DBKernel.delimitL("Laienpersonal") + " BOOLEAN BEFORE " + DBKernel.delimitL("Format"), false);
			updateChangeLog("Kits", 27, false);
			refreshFKs("Kits");			
		}

		// ACHTUNG!!! Ab hier auch bei Wese machen!
		MyDBTables.getTable("ImportedCombaseData").createTable(false);
		DBKernel.grantDefaults("ImportedCombaseData");		
		MyDBTables.getTable("Verpackungsmaterial").createTable(false);
		DBKernel.grantDefaults("Verpackungsmaterial");		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ADD COLUMN " + DBKernel.delimitL("Verpackung") + " INTEGER BEFORE " + DBKernel.delimitL("Temperatur"), false);
		updateChangeLog("Zutatendaten", 10, false);
		refreshFKs("Zutatendaten");
		new GeneralXLSImporter().doImport("/org/hsh/bfr/db/res/Verpackungsmaterial.xls", DBKernel.mainFrame.getProgressBar(), false);
		new GeneralXLSImporter().doImport("/org/hsh/bfr/db/res/ImportedCombaseData.xls", DBKernel.mainFrame.getProgressBar(), false);
		dropJansTabellen();

		String tableName = "Literatur";
		DBKernel.sendRequest("CREATE TRIGGER " + DBKernel.delimitL("A_" + tableName + "_I") + " AFTER INSERT ON " + DBKernel.delimitL(tableName) + " FOR EACH ROW " + " CALL " + DBKernel.delimitL(new MyTrigger().getClass().getName()), false); 
		tableName = "GeschaetzteModelle"; DBKernel.sendRequest("CREATE TRIGGER " + DBKernel.delimitL("A_" + tableName + "_I") + " AFTER INSERT ON " + DBKernel.delimitL(tableName) + " FOR EACH ROW " + " CALL " + DBKernel.delimitL(new MyTrigger().getClass().getName()), false); 
		tableName = "GeschaetztesModell_Referenz"; DBKernel.sendRequest("CREATE TRIGGER " + DBKernel.delimitL("A_" + tableName + "_I") + " AFTER INSERT ON " + DBKernel.delimitL(tableName) + " FOR EACH ROW " + " CALL " + DBKernel.delimitL(new MyTrigger().getClass().getName()), false); 
		tableName = "GeschaetzteParameter"; DBKernel.sendRequest("CREATE TRIGGER " + DBKernel.delimitL("A_" + tableName + "_I") + " AFTER INSERT ON " + DBKernel.delimitL(tableName) + " FOR EACH ROW " + " CALL " + DBKernel.delimitL(new MyTrigger().getClass().getName()), false); 
		tableName = "GeschaetzteParameterCovCor"; DBKernel.sendRequest("CREATE TRIGGER " + DBKernel.delimitL("A_" + tableName + "_I") + " AFTER INSERT ON " + DBKernel.delimitL(tableName) + " FOR EACH ROW " + " CALL " + DBKernel.delimitL(new MyTrigger().getClass().getName()), false); 
		tableName = "Sekundaermodelle_Primaermodelle"; DBKernel.sendRequest("CREATE TRIGGER " + DBKernel.delimitL("A_" + tableName + "_I") + " AFTER INSERT ON " + DBKernel.delimitL(tableName) + " FOR EACH ROW " + " CALL " + DBKernel.delimitL(new MyTrigger().getClass().getName()), false); 

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ComBaseImport") + " ADD COLUMN " + DBKernel.delimitL("Referenz") + " INTEGER BEFORE " + DBKernel.delimitL("Agensname"), false);
		updateChangeLog("ComBaseImport", 1, false);
		refreshFKs("ComBaseImport");
	}
	public static void check4Updates_137_138() {
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameter") +
				" ALTER COLUMN " + DBKernel.delimitL("Einheit") + " RENAME TO " + DBKernel.delimitL("ZeitEinheit"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameter") +
				" ADD COLUMN " + DBKernel.delimitL("Konz_Einheit") + " INTEGER BEFORE " + DBKernel.delimitL("KI.unten"), false);
		updateChangeLog("GeschaetzteParameter", 5, false);
		refreshFKs("GeschaetzteParameter");
	}
	public static void check4Updates_136_137() {
		//checkChangeLog();
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozess_Workflow") +
				" ADD COLUMN " + DBKernel.delimitL("Referenz") + " INTEGER BEFORE " + DBKernel.delimitL("Guetescore"), false);
		updateChangeLog("Prozess_Workflow", 10, false);
		refreshFKs("Prozess_Workflow");
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameter") +
				" ADD COLUMN " + DBKernel.delimitL("Einheit") + " VARCHAR(50) BEFORE " + DBKernel.delimitL("KI.unten"), false);
		updateChangeLog("GeschaetzteParameter", 4, false);
		refreshFKs("GeschaetzteParameter");
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("Referenzen") + " INTEGER BEFORE " + DBKernel.delimitL("Kommentar"), false);
		updateChangeLog("GeschaetzteModelle", 8, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("GeschaetzteParameter") + " INTEGER BEFORE " + DBKernel.delimitL("Kommentar"), false);
		updateChangeLog("GeschaetzteModelle", 9, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("GeschaetzteParameterCovCor") + " INTEGER BEFORE " + DBKernel.delimitL("Kommentar"), false);
		updateChangeLog("GeschaetzteModelle", 10, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") + " ALTER COLUMN " + DBKernel.delimitL("Modell") + " SET NULL", false);
		refreshFKs("GeschaetzteModelle"); // ID_CB wurde auf UNIQUE gesetzt, damit der Import von der Combase reibungslos funktioniert!

		// neue Daten ab 1.3.7
		try {
			PreparedStatement ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Matrices") +
					" (" + DBKernel.delimitL("Matrixname") + ") VALUES (?)");
			ps.setString(1, "Sprossensamen"); ps.execute();
			ps.setString(1, "Bockshornkleesamen"); ps.execute();
			ps.setString(1, "Alfalfasamen"); ps.execute();
			ps.setString(1, "Mungobohnensamen"); ps.execute();
			ps.setString(1, "Rettichsamen"); ps.execute();
			ps.setString(1, "Linsensamen"); ps.execute();
			ps.setString(1, "Zwiebelsamen"); ps.execute();

			ps.setString(1, "Frischgemüse"); ps.execute();
			ps.setString(1, "Sprossgemüse"); ps.execute();
			ps.setString(1, "Bockshornkleesprossen"); ps.execute();
			ps.setString(1, "Alfalfasprossen"); ps.execute();
			ps.setString(1, "Mungobohnensprossen"); ps.execute();
			ps.setString(1, "Rettichsprossen"); ps.execute();
			ps.setString(1, "Linsensprossen"); ps.execute();
			ps.setString(1, "Zwiebelsprossen"); ps.execute();
			
	    	ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Codes_Matrices") +
					" (" + DBKernel.delimitL("CodeSystem") + "," + DBKernel.delimitL("Code") + "," + DBKernel.delimitL("Basis") + ") VALUES (?,?,?)");
			ps.setString(1, "SiLeBAT"); ps.setString(2, "02"); ps.setInt(3, 19992); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "0200"); ps.setInt(3, 19993); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "0201"); ps.setInt(3, 19994); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "0202"); ps.setInt(3, 19995); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "0203"); ps.setInt(3, 19996); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "0204"); ps.setInt(3, 19997); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "0205"); ps.setInt(3, 19998); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "03"); ps.setInt(3, 19999); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "0301"); ps.setInt(3, 20000); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "030100"); ps.setInt(3, 20001); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "030101"); ps.setInt(3, 20002); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "030102"); ps.setInt(3, 20003); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "030103"); ps.setInt(3, 20004); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "030104"); ps.setInt(3, 20005); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "030105"); ps.setInt(3, 20006); ps.execute();
			
			ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Modellkatalog") +
					" (" + DBKernel.delimitL("Name") + "," + DBKernel.delimitL("Notation") + "," +
					DBKernel.delimitL("Level") + "," + DBKernel.delimitL("Klasse") + "," +
					DBKernel.delimitL("Formel") + "," + DBKernel.delimitL("Eingabedatum") + "," +
					DBKernel.delimitL("Software") + ") VALUES (?,?,?,?,?,?,?)");
			ps.setString(1, "D-Wert (Bigelow)"); 
			ps.setString(2, "d_wert");
			ps.setInt(3, 1);
			ps.setInt(4, 2);
			ps.setString(5, "LOG10N ~ LOG10N0 - t / D");
			ps.setDate(6, new java.sql.Date(System.currentTimeMillis()));
			ps.setString(7, "R");
			ps.execute();

			ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("ModellkatalogParameter") +
					" (" + DBKernel.delimitL("Modell") + "," + DBKernel.delimitL("Parametername") + "," +
					DBKernel.delimitL("Parametertyp") + "," + DBKernel.delimitL("ganzzahl") + ") VALUES (?,?,?,?)");
			ps.setInt(1, 44); 
			ps.setString(2, "D");
			ps.setInt(3, 2);
			ps.setBoolean(4, false);
			ps.execute();
			ps.setInt(1, 44); 
			ps.setString(2, "LOG10N0");
			ps.setInt(3, 2);
			ps.setBoolean(4, false);
			ps.execute();
			ps.setInt(1, 44); 
			ps.setString(2, "t");
			ps.setInt(3, 1);
			ps.setBoolean(4, false);
			ps.execute();
			ps.setInt(1, 44); 
			ps.setString(2, "LOG10N");
			ps.setInt(3, 3);
			ps.setBoolean(4, false);
			ps.execute();
		}
		catch (Exception e) {e.printStackTrace();}
	}
	private static boolean updateChangeLog(final String tablename, final int modifiedCol, final boolean deleted) {
		return updateChangeLog(tablename, modifiedCol, deleted, -1);
	}
	private static boolean updateChangeLog(final String tablename, final int modifiedCol, final boolean deleted, final int oSize) {
		boolean result = true;
		boolean showMessages = false;
		ResultSet rs = DBKernel.getResultSet("SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Alteintrag") +
				" FROM " + DBKernel.delimitL("ChangeLog") +
				" WHERE " + DBKernel.delimitL("Tabelle") + "='" + tablename + "'", false); //  + " AND " + DBKernel.delimitL("ID") + " > 169000"
		try {
		    if (rs != null && rs.first()) {
		    	String sql = "UPDATE " + DBKernel.delimitL("ChangeLog") +
		        " SET " + DBKernel.delimitL("Alteintrag") + " = ? WHERE " + DBKernel.delimitL("ID") + "=?";
		    	PreparedStatement psmt = DBKernel.getDBConnection().prepareStatement(sql);
		    	do {
		    		Object[] o = (Object[]) rs.getObject("Alteintrag");
		    		if (o != null) {
		    			if (modifiedCol < o.length || !deleted && modifiedCol == o.length) {
			    			if (showMessages && deleted) {
								System.out.println(o.length);
							}
			    			if (!deleted || o.length > oSize) {
				    			Object[] newO = new Object[o.length + (deleted ? -1:1)]; 
				    			for (int i=0;i<newO.length;i++) {
				    				if (deleted) {
				    					if (i > modifiedCol) {
											newO[i] = o[i+1];
										} else if (i < modifiedCol) {
											newO[i] = o[i];
										}
				    				}
				    				else {
				    					if (i > modifiedCol) {
											newO[i] = o[i-1];
										} else if (i < modifiedCol) {
											newO[i] = o[i];
										} else if (i == modifiedCol) {
											newO[i] = null;
										}		    					
				    				}
				    			}
				    			if (showMessages) {
				    				if (deleted) {
										System.out.println("before: " + newO[modifiedCol-1] + "\tdeleted: " + newO[modifiedCol] + "\tafter: " + newO[modifiedCol+1]);
									} else {
										System.out.println("before: " + newO[modifiedCol-1] + (modifiedCol+1 < newO.length ? "\tafter: " + newO[modifiedCol+1] : "\tnix"));
									}
				    			}
				    			psmt.clearParameters();
				    		    psmt.setObject(1, newO);
				    		    psmt.setInt(2, rs.getInt("ID"));
				    		  	result = result && (psmt.executeUpdate() > 0);
			    			}		    				
		    			}
		    			else {
		    				if (DBKernel.debug) {
								System.out.println("modifiedRow < o.length: " + o.length + "\t" + modifiedCol);
							}
		    			}
		    		}
		    	} while (rs.next());
    		  	psmt.close();
		    }
	    }
	    catch(Exception e) {MyLogger.handleException(e);}	
	    return result;
	}
	private static void refreshFKs(final String tableName) {
		refreshFKs(tableName, false);
	}
	private static void refreshFKs(final String tableName, final boolean dropOnly) {
		// Foreign Keys setzen, ACHTUNG: immer checken, wenn ein Fehler auftritt
		ResultSet rs = DBKernel.getResultSet("SELECT TABLE_NAME, CONSTRAINT_NAME, CONSTRAINT_TYPE FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS", false);
		try {
		    if (rs != null && rs.first()) {
		    	do {
		    		String tn = rs.getObject("TABLE_NAME") != null ? rs.getString("TABLE_NAME") : "";
		    		String cn = rs.getObject("CONSTRAINT_NAME") != null ? rs.getString("CONSTRAINT_NAME") : "";
		    		String ct = rs.getObject("CONSTRAINT_TYPE") != null ? rs.getString("CONSTRAINT_TYPE") : "";
		    		if (tn.equals(tableName)) {
		    			if (ct.equals("FOREIGN KEY") || ct.equals("UNIQUE")) {
		    				if (cn.length() > 0) {
			    				DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL(tableName) + " DROP CONSTRAINT " + DBKernel.delimitL(cn), false);
			    				System.out.println("Dropped\t" + ct + "\t" + tn + "\t" + cn);
		    				}
		    				else {
		    					MyLogger.handleMessage("CONSTRAINT_NAME = null?? " + ct + "\t" + tn + "\t" + cn);
		    				}
		    			}
		    			else {
		    				if (!ct.equals("PRIMARY KEY") && !ct.equals("CHECK")) {
			    				MyLogger.handleMessage("Wasn das jetzt fürn CONSTRAINT. Soll ich wirklich löschen??? " + ct + "\t" + tn + "\t" + cn);	    					
		    				}
		    			}
		    		}
		    	} while (rs.next());
		    }
	    }
	    catch(Exception e) {MyLogger.handleException(e);}	
	    
	    if (!dropOnly) {
	      for (String sql : MyDBTables.getTable(tableName).getIndexSQL()) {
	      	if (sql.length() > 0) {
	  				System.out.println("sent\t" + sql);
	      		DBKernel.sendRequest(sql, false);
	      	}
	      }		    	
	    }
	}
	/*
	private static void dropTriggers(final String tableName) {
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_" + tableName + "_U"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_" + tableName + "_D"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_" + tableName + "_I"), false);
		if (tableName.equals("Prozess_Workflow")) {
			DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("B_Prozess-Workflow_U"), true);
			DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("B_Prozess_Workflow_U"), false);
		}
	}
	*/
	public static void doStatUpGrants() {
		DBKernel.grantDefaults("Modellkatalog");
		DBKernel.grantDefaults("ModellkatalogParameter");
		DBKernel.grantDefaults("Modell_Referenz");
		DBKernel.grantDefaults("GeschaetzteModelle");
		DBKernel.grantDefaults("GeschaetztesModell_Referenz");
		DBKernel.grantDefaults("GeschaetzteParameter");
		DBKernel.grantDefaults("GeschaetzteParameterCovCor");
		DBKernel.grantDefaults("Sekundaermodelle_Primaermodelle");
	}
	public static void doJansGrants() {
		DBKernel.grantDefaults("Verwendung");
		DBKernel.grantDefaults("Preharvest");
		DBKernel.grantDefaults("Harvest");
		DBKernel.grantDefaults("Produkt");
		DBKernel.grantDefaults("Transport");
		DBKernel.grantDefaults("Exposition");
		DBKernel.grantDefaults("Risikocharakterisierung");
		
		DBKernel.grantDefaults("Methoden_Software");
		DBKernel.grantDefaults("Resistenz");
		DBKernel.grantDefaults("Laender");
		DBKernel.grantDefaults("Modell");
		DBKernel.grantDefaults("Modell_Verwendung_Verbund");
		DBKernel.grantDefaults("Modell_Resistenz_Verbund");
		DBKernel.grantDefaults("Modell_Agenzien_Verbund");
		DBKernel.grantDefaults("Modell_Software_Verbund");
		DBKernel.grantDefaults("Modell_Preharvest_Verbund");
		DBKernel.grantDefaults("Modell_Harvest_Verbund");
		DBKernel.grantDefaults("Modell_Zwischenprodukt_Verbund");
		DBKernel.grantDefaults("Modell_Einzelhandelsprodukt_Verbund");
		DBKernel.grantDefaults("Modell_Transport_Verbund");
		DBKernel.grantDefaults("Modell_Exposition_Verbund");
		DBKernel.grantDefaults("Modell_Risikocharakterisierung_Verbund");
	}
	public static void dropJansTabellen() {
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Modell_Verwendung_Verbund") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Modell_Resistenz_Verbund") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Modell_Agenzien_Verbund") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Modell_Software_Verbund") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Modell_Preharvest_Verbund") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Modell_Harvest_Verbund") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Modell_Zwischenprodukt_Verbund") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Modell_Einzelhandelsprodukt_Verbund") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Modell_Transport_Verbund") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Modell_Exposition_Verbund") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Modell_Risikocharakterisierung_Verbund") + " IF EXISTS", false);
		
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Methoden_Software") + " IF EXISTS", false);

		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Verwendung") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Preharvest") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Harvest") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Exposition") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Produkt") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Transport") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Risikocharakterisierung") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Resistenz") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Modell") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Laender") + " IF EXISTS", false);
	}
	private static final String SQL_CREATE_VIEW_DOUBLE = "CREATE VIEW \"DoubleKennzahlenEinfach\" AS SELECT \"ID\", CASE WHEN \"Wert\" IS NULL THEN CASE WHEN \"Minimum\" IS NULL THEN \"Maximum\" ELSE CASE WHEN \"Maximum\" IS NULL THEN \"Minimum\" ELSE ( \"Minimum\"+\"Maximum\" )/2 END END ELSE \"Wert\" END AS \"Wert\" FROM( SELECT ID, CASE WHEN \"Exponent\" IS NULL THEN \"Wert\" ELSE CASE WHEN \"Wert\" IS NULL THEN POWER( 10, \"Exponent\" ) ELSE \"Wert\"*POWER( 10, \"Exponent\" ) END END AS \"Wert\", CASE WHEN \"Minimum_exp\" IS NULL THEN \"Minimum\" ELSE CASE WHEN \"Minimum\" IS NULL THEN POWER( 10, \"Minimum_exp\" ) ELSE \"Minimum\"*POWER( 10, \"Minimum_exp\" ) END END AS \"Minimum\", CASE WHEN \"Maximum_exp\" IS NULL THEN \"Maximum\" ELSE CASE WHEN \"Maximum\" IS NULL THEN POWER( 10, \"Maximum_exp\" ) ELSE \"Maximum\"*POWER( 10, \"Maximum_exp\" ) END END AS \"Maximum\" FROM \"DoubleKennzahlen\" )\n";
	private static final String SQL_CREATE_VIEW_DATA = "CREATE VIEW \"MesswerteEinfach\" AS SELECT \"ID\", \"Versuchsbedingungen\" AS \"Versuchsbedingung\", CASE WHEN \"Messwerte\".\"ZeitEinheit\" LIKE 'Stunde' THEN \"T\".\"Wert\" WHEN \"Messwerte\".\"ZeitEinheit\" LIKE 'Minute' THEN \"T\".\"Wert\"/60 WHEN \"Messwerte\".\"ZeitEinheit\" LIKE 'Sekunde' THEN \"T\".\"Wert\"/3600 WHEN \"Messwerte\".\"ZeitEinheit\" LIKE 'Tag' THEN \"T\".\"Wert\"*24 WHEN \"Messwerte\".\"ZeitEinheit\" LIKE 'Woche' THEN \"T\".\"Wert\"*168 WHEN \"Messwerte\".\"ZeitEinheit\" LIKE 'Monat' THEN \"T\".\"Wert\"*730.5 WHEN \"Messwerte\".\"ZeitEinheit\" LIKE 'Jahr' THEN \"T\".\"Wert\"*8766 ELSE NULL END AS \"Zeit\", CASE WHEN REGEXP_MATCHES( \"Einheiten\".\"Einheit\", 'log(10)?.*( pro |/)25(g|m[lL])' ) THEN \"K\".\"Wert\"-LOG10( 25 ) WHEN REGEXP_MATCHES( \"Einheiten\".\"Einheit\", 'log(10)?.*( pro |/)(kg|[lL])' ) THEN \"K\".\"Wert\"-3 WHEN REGEXP_MATCHES( \"Einheiten\".\"Einheit\", 'log(10)?.*( pro |/)100(g|m[lL])' ) THEN \"K\".\"Wert\"-2 WHEN REGEXP_MATCHES( \"Einheiten\".\"Einheit\", 'log(10)?.*( pro |/)0\\.1(g|m[lL])' ) THEN \"K\".\"Wert\"+1 WHEN REGEXP_MATCHES( \"Einheiten\".\"Einheit\", 'log(10)?.*( pro |/)(g|m[lL])' ) THEN \"K\".\"Wert\" WHEN REGEXP_MATCHES( \"Einheiten\".\"Einheit\", '.*( pro |/)25(g|m[lL])' ) THEN LOG10( \"K\".\"Wert\"/25 ) WHEN REGEXP_MATCHES( \"Einheiten\".\"Einheit\", '.*( pro |/)(kg|[lL])' ) THEN LOG10( \"K\".\"Wert\" )-3 WHEN REGEXP_MATCHES( \"Einheiten\".\"Einheit\", '.*( pro |/)100(g|m[lL])' ) THEN LOG10( \"K\".\"Wert\" )-2 WHEN REGEXP_MATCHES( \"Einheiten\".\"Einheit\", '.*( pro |/)0\\.1(g|m[lL])' ) THEN LOG10( \"K\".\"Wert\" )+1 WHEN REGEXP_MATCHES( \"Einheiten\".\"Einheit\", '.*( pro |/)(g|m[lL])' ) THEN CASE WHEN \"K\".\"Wert\" <= 1 THEN 0 ELSE LOG10( \"C\".\"Wert\" ) END ELSE NULL END AS \"Konzentration\", \"C\".\"Wert\" AS \"Temperatur\", \"P\".\"Wert\" AS \"pH\", \"A\".\"Wert\" AS \"aw\", \"Q\".\"Wert\" AS \"Druck\", \"R\".\"Wert\" AS \"CO2\", \"S\".\"Wert\" AS \"Luftfeuchtigkeit\", \"Messwerte\".\"Sonstiges\", \"Messwerte\".\"Kommentar\" FROM \"Messwerte\" JOIN \"DoubleKennzahlenEinfach\" AS \"T\" ON \"Messwerte\".\"Zeit\"=\"T\".\"ID\" JOIN \"DoubleKennzahlenEinfach\" AS \"K\" ON \"Messwerte\".\"Konzentration\"=\"K\".\"ID\" JOIN \"Einheiten\" ON \"Messwerte\".\"Konz_Einheit\"=\"Einheiten\".\"ID\" LEFT JOIN \"DoubleKennzahlenEinfach\" AS \"C\" ON \"Messwerte\".\"Temperatur\"=\"C\".\"ID\" LEFT JOIN \"DoubleKennzahlenEinfach\" AS \"P\" ON \"Messwerte\".\"pH\"=\"P\".\"ID\" LEFT JOIN \"DoubleKennzahlenEinfach\" AS \"A\" ON \"Messwerte\".\"aw\"=\"A\".\"ID\" LEFT JOIN \"DoubleKennzahlenEinfach\" AS \"Q\" ON \"Messwerte\".\"Druck\"=\"Q\".\"ID\" LEFT JOIN \"DoubleKennzahlenEinfach\" AS \"R\" ON \"Messwerte\".\"CO2\"=\"R\".\"ID\" LEFT JOIN \"DoubleKennzahlenEinfach\" AS \"S\" ON \"Messwerte\".\"Luftfeuchtigkeit\"=\"S\".\"ID\" WHERE \"Delta\" IS NULL OR NOT \"Delta\"\n";
	private static final String SQL_CREATE_VIEW_CONDITION = "CREATE VIEW \"VersuchsbedingungenEinfach\" AS SELECT \"Versuchsbedingungen\".\"ID\", \"Versuchsbedingungen\".\"Referenz\", \"Versuchsbedingungen\".\"Agens\", \"Versuchsbedingungen\".\"AgensDetail\", \"Versuchsbedingungen\".\"Matrix\", \"Versuchsbedingungen\".\"MatrixDetail\", \"C\".\"Wert\" AS \"Temperatur\", \"P\".\"Wert\" AS \"pH\", \"A\".\"Wert\" AS \"aw\", \"O\".\"Wert\" AS \"CO2\", \"D\".\"Wert\" AS \"Druck\", \"L\".\"Wert\" AS \"Luftfeuchtigkeit\", \"Versuchsbedingungen\".\"Sonstiges\", \"Versuchsbedingungen\".\"Kommentar\" FROM \"Versuchsbedingungen\" LEFT JOIN \"DoubleKennzahlenEinfach\" AS \"C\" ON \"Versuchsbedingungen\".\"Temperatur\"=\"C\".\"ID\" LEFT JOIN \"DoubleKennzahlenEinfach\" AS \"P\" ON \"Versuchsbedingungen\".\"pH\"=\"P\".\"ID\" LEFT JOIN \"DoubleKennzahlenEinfach\" AS \"A\" ON \"Versuchsbedingungen\".\"aw\"=\"A\".\"ID\" LEFT JOIN \"DoubleKennzahlenEinfach\" AS \"O\" ON \"Versuchsbedingungen\".\"CO2\"=\"O\".\"ID\" LEFT JOIN \"DoubleKennzahlenEinfach\" AS \"D\" ON \"Versuchsbedingungen\".\"Druck\"=\"D\".\"ID\" LEFT JOIN \"DoubleKennzahlenEinfach\" AS \"L\" ON \"Versuchsbedingungen\".\"Luftfeuchtigkeit\"=\"L\".\"ID\"\n";
	private static final String SQL_CREATE_VIEW_MISC = "CREATE VIEW \"SonstigesEinfach\" AS SELECT \"Versuchsbedingungen_Sonstiges\".\"Versuchsbedingungen\" AS \"Versuchsbedingung\", \"SonstigeParameter\".\"ID\" AS \"SonstigesID\", \"SonstigeParameter\".\"Beschreibung\", \"Einheiten\".\"Einheit\", \"DoubleKennzahlen\".\"Wert\" FROM \"Versuchsbedingungen_Sonstiges\" LEFT JOIN \"Einheiten\" ON \"Versuchsbedingungen_Sonstiges\".\"Einheit\"=\"Einheiten\".\"ID\" JOIN \"SonstigeParameter\" ON \"Versuchsbedingungen_Sonstiges\".\"SonstigeParameter\"=\"SonstigeParameter\".\"ID\" LEFT JOIN \"DoubleKennzahlen\" ON \"Versuchsbedingungen_Sonstiges\".\"Wert\"=\"DoubleKennzahlen\".\"ID\"\n";
}
