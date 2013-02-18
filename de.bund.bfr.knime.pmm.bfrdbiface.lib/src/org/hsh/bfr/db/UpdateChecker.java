/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
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

import org.hsh.bfr.db.gui.MyList;
import org.hsh.bfr.db.imports.GeneralXLSImporter;
import org.hsh.bfr.db.imports.SQLScriptImporter;

/**
 * @author Weiser
 *
 */

// ACHTUNG: beim MERGEN sind sowohl KZ2NKZ als auch moveDblIntoDoubleKZ ohne Effekt!!! Da sie nicht im ChangeLog drin stehen!!!! Da muss KZ2NKZ nachträglich ausgeführt werden (solange die Tabelle Kennzahlen noch existiert). Bei moveDblIntoDoubleKZ???

public class UpdateChecker {
	public static void check4Updates_153_154(final MyList myList) {		
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelPrimView") + ";", false);
		DBKernel.sendRequest("DROP VIEW IF EXISTS " + DBKernel.delimitL("EstModelSecView") + ";", false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_ParamVarView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelPrimView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelSecView.sql", null, false);		
	}
	public static void check4Updates_152_153(final MyList myList) {		
		myList.getTable("LinkedTestConditions").createTable();
		DBKernel.grantDefaults("LinkedTestConditions");
	}
	public static void check4Updates_151_152(final MyList myList) {		
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/CombaseRawDataImport.sql", null, false);
	}
	public static void check4Updates_150_151(final MyList myList) {		
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_DepVarView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_IndepVarView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_LitEmView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_LitMView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_ParamVarView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/001_VarParMapView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelPrimView.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/002_EstModelSecView.sql", null, false);		
	}
	public static void check4Updates_149_150(final MyList myList) {
		
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/01_sonstigeseinfach.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/02_create_doublekennzahleneinfach.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/03_create_messwerteeinfach.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/04_create_versuchsbedingungeneinfach.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/05_create_modelview.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/06_create_estmodelprimview.sql", null, false);
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/07_create_estmodelsecview.sql", null, false);
		
	}
	public static void check4Updates_148_149(final MyList myList) {
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/create_view_set.sql", null, false);
	}
	public static void check4Updates_147_148(final MyList myList) {
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
	public static void check4Updates_146_147(final MyList myList) {
		myList.getTable("PMMLabWorkflows").createTable();
		DBKernel.grantDefaults("PMMLabWorkflows");
		myList.getTable("DataSource").createTable();
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
	public static void check4Updates_145_146(final MyList myList) {
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("Guetescore") + " INTEGER BEFORE " + DBKernel.delimitL("Kommentar"), false))
			updateChangeLog("GeschaetzteModelle", 16, false);		
		if (DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("Geprueft") + " BOOLEAN", false))
			updateChangeLog("GeschaetzteModelle", 18, false);	
		refreshFKs("GeschaetzteModelle");
	}
	public static void check4Updates_144_145(final MyList myList) {
		
		myList.getTable("Chargen").createTable();
		DBKernel.grantDefaults("Chargen");
		myList.getTable("ChargenVerbindungen").createTable();
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
		
		DBKernel.doMNs(DBKernel.myList.getTable("Chargen"));

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
	public static void check4Updates_143_144(final MyList myList) {
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
		
		myList.getTable("VarParMaps").createTable(true);
		DBKernel.grantDefaults("VarParMaps");
		
		myList.getTable("Kostenkatalog").createTable();
		DBKernel.grantDefaults("Kostenkatalog");
		myList.getTable("Kostenkatalogpreise").createTable();
		DBKernel.grantDefaults("Kostenkatalogpreise");
		myList.getTable("Prozessdaten_Kosten").createTable();
		DBKernel.grantDefaults("Prozessdaten_Kosten");
		myList.getTable("Zutatendaten_Kosten").createTable();
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
		myList.getTable("Station").createTable();
		DBKernel.grantDefaults("Station");
		if (DBKernel.isKNIME) DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Knoten_Agenzien") + " IF EXISTS", false);
		myList.getTable("Station_Agenzien").createTable();
		DBKernel.grantDefaults("Station_Agenzien");
		if (DBKernel.isKNIME) DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Produktkatalog") + " IF EXISTS", false);
		myList.getTable("Produktkatalog").createTable();
		DBKernel.grantDefaults("Produktkatalog");
		if (DBKernel.isKNIME) DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Produktkatalog_Matrices") + " IF EXISTS", false);
		myList.getTable("Produktkatalog_Matrices").createTable();
		DBKernel.grantDefaults("Produktkatalog_Matrices");
		if (DBKernel.isKNIME) DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Lieferungen") + " IF EXISTS", false);
		myList.getTable("Lieferungen").createTable();
		DBKernel.grantDefaults("Lieferungen");
		if (DBKernel.isKNIME) DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("LieferungVerbindungen") + " IF EXISTS", false);
		MyTable myT = myList.getTable("LieferungVerbindungen");
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
	public static void check4Updates_142_143(final MyList myList) {
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
	public static void check4Updates_141_142(final MyList myList) {
		//wegen recreateTriggers muss das hier noch vorgezogen werden, bei Alex die Triggers auch nochmal recreaten!
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozess_Workflow") +
				" RENAME TO " + DBKernel.delimitL("ProzessWorkflow"), false);
		refreshFKs("ProzessWorkflow");
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DateiSpeicher") + " SET " + DBKernel.delimitL("Tabelle") + "='ProzessWorkflow' WHERE " + DBKernel.delimitL("Tabelle") + "='Prozess_Workflow'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("ChangeLog") + " SET " + DBKernel.delimitL("Tabelle") + "='ProzessWorkflow' WHERE " + DBKernel.delimitL("Tabelle") + "='Prozess_Workflow'", false);

		// ACHTUNG: Alex hat bereits jetzt schon diese DB 1.4.2 - von hier
		DBKernel.myList.recreateTriggers();
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
		
		MyTable pdl = myList.getTable("Prozessdaten_Literatur");
		pdl.createTable(false);
		DBKernel.grantDefaults("Prozessdaten_Literatur");
		DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("Prozessdaten_Literatur") +
				"(" + DBKernel.delimitL("Prozessdaten") + "," + DBKernel.delimitL("Literatur") +
				") SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Referenz") +
				" FROM " + DBKernel.delimitL("Prozessdaten"), false);
		refreshFKs("Prozessdaten");		
		DBKernel.doMNs(myList.getTable("Prozessdaten"));
		
		pdl = myList.getTable("ProzessWorkflow_Literatur");
		pdl.createTable(false);
		DBKernel.grantDefaults("ProzessWorkflow_Literatur");
		DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("ProzessWorkflow_Literatur") +
				"(" + DBKernel.delimitL("ProzessWorkflow") + "," + DBKernel.delimitL("Literatur") +
				") SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Referenz") +
				" FROM " + DBKernel.delimitL("ProzessWorkflow"), false);
		refreshFKs("ProzessWorkflow");		
		DBKernel.doMNs(myList.getTable("ProzessWorkflow"));
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
	public static void check4Updates_140_141(final MyList myList) {
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

		myList.getTable("Parametertyp").createTable(false);
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
		
		myList.getTable("GueltigkeitsBereiche").createTable(false);
		DBKernel.grantDefaults("GueltigkeitsBereiche");
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("GueltigkeitsBereiche") + " INTEGER BEFORE " + DBKernel.delimitL("Kommentar"), false);
		updateChangeLog("GeschaetzteModelle", 11, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") +
				" ADD COLUMN " + DBKernel.delimitL("PMML") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Kommentar"), false);
		updateChangeLog("GeschaetzteModelle", 12, false);
		refreshFKs("GeschaetzteModelle");
	}
	public static void check4Updates_139_140(final MyList myList) {
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
	
	public static void check4Updates_138_139(final MyList myList) {
		check4Updates_138_139(myList, false);
	}
	public static void check4Updates_138_139(final MyList myList, final boolean weseSpecial) {
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
		myList.getTable("ImportedCombaseData").createTable(false);
		DBKernel.grantDefaults("ImportedCombaseData");		
		myList.getTable("Verpackungsmaterial").createTable(false);
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
	public static void check4Updates_137_138(final MyList myList) {
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameter") +
				" ALTER COLUMN " + DBKernel.delimitL("Einheit") + " RENAME TO " + DBKernel.delimitL("ZeitEinheit"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameter") +
				" ADD COLUMN " + DBKernel.delimitL("Konz_Einheit") + " INTEGER BEFORE " + DBKernel.delimitL("KI.unten"), false);
		updateChangeLog("GeschaetzteParameter", 5, false);
		refreshFKs("GeschaetzteParameter");
	}
	/*
	private static boolean checkChangeLog() {
		boolean result = true;
		ResultSet rs = DBKernel.getResultSet("SELECT * FROM " + DBKernel.delimitL("ChangeLog") +
				" WHERE " + DBKernel.delimitL("ID") + " > 169239", false); //  + " AND " + DBKernel.delimitL("ID") + " > 169239"
		try {
		    if (rs != null && rs.first()) {
		    	do {
		    		Object o = rs.getObject("Alteintrag");
		    		System.out.println(rs.getInt("ID") + "\t" + rs.getString("Zeitstempel") + "\t" + rs.getString("Username") + "\t" +
		    	rs.getString("Tabelle") + "\t" + rs.getString("TabellenID") + "\t" + o);
		    	} while (rs.next());
		    }
	    }
	    catch(Exception e) {MyLogger.handleException(e);}	
	    return result;
	}
	*/
	public static void check4Updates_136_137(final MyList myList) {
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
	public static void check4Updates_135_136(final MyList myList) {
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Modellkatalog") + " ALTER COLUMN " + DBKernel.delimitL("Name") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Modellkatalog") + " ALTER COLUMN " + DBKernel.delimitL("Notation") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Modellkatalog") + " ALTER COLUMN " + DBKernel.delimitL("Eingabedatum") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ModellkatalogParameter") + " ALTER COLUMN " + DBKernel.delimitL("Modell") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ModellkatalogParameter") + " ALTER COLUMN " + DBKernel.delimitL("Parametername") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ModellkatalogParameter") + " ALTER COLUMN " + DBKernel.delimitL("Parametertyp") + " SET DEFAULT 1", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ModellkatalogParameter") + " ALTER COLUMN " + DBKernel.delimitL("ganzzahl") + " SET DEFAULT FALSE", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Modell_Referenz") + " ALTER COLUMN " + DBKernel.delimitL("Modell") + " SET NOT NULL ", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Modell_Referenz") + " ALTER COLUMN " + DBKernel.delimitL("Literatur") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") + " ALTER COLUMN " + DBKernel.delimitL("Modell") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteModelle") + " ALTER COLUMN " + DBKernel.delimitL("manuellEingetragen") + " SET DEFAULT FALSE", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetztesModell_Referenz") + " ALTER COLUMN " + DBKernel.delimitL("GeschaetztesModell") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetztesModell_Referenz") + " ALTER COLUMN " + DBKernel.delimitL("Literatur") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameter") + " ALTER COLUMN " + DBKernel.delimitL("GeschaetztesModell") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameter") + " ALTER COLUMN " + DBKernel.delimitL("Parameter") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameterCovCor") + " ALTER COLUMN " + DBKernel.delimitL("param1") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameterCovCor") + " ALTER COLUMN " + DBKernel.delimitL("param2") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameterCovCor") + " ALTER COLUMN " + DBKernel.delimitL("GeschaetztesModell") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("GeschaetzteParameterCovCor") + " ALTER COLUMN " + DBKernel.delimitL("cor") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Sekundaermodelle_Primaermodelle") + " ALTER COLUMN " + DBKernel.delimitL("GeschaetztesPrimaermodell") + " SET NOT NULL", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Sekundaermodelle_Primaermodelle") + " ALTER COLUMN " + DBKernel.delimitL("GeschaetztesSekundaermodell") + " SET NOT NULL", false);
		refreshFKs("Modellkatalog");
		refreshFKs("ModellkatalogParameter");
		refreshFKs("Modell_Referenz");
		refreshFKs("GeschaetzteModelle");
		refreshFKs("GeschaetztesModell_Referenz");
		refreshFKs("GeschaetzteParameter");
		refreshFKs("GeschaetzteParameterCovCor");
		refreshFKs("Sekundaermodelle_Primaermodelle");
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Modellkatalog_I"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_ModellkatalogParameter_I"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Modell_Referenz_I"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_GeschaetzteModelle_I"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_GeschaetztesModell_Referenz_I"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_GeschaetzteParameter_I"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_GeschaetzteParameterCovCor_I"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Sekundaermodelle_Primaermodelle_I"), false);
		DBKernel.sendRequest("DROP TRIGGER " + DBKernel.delimitL("A_Literatur_I"), false);
		
		new SQLScriptImporter().doImport("/org/hsh/bfr/db/res/StatupInserts.sql", null, false);
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Labore") +
				" ALTER COLUMN " + DBKernel.delimitL("privat/staatlich") + " RENAME TO " + DBKernel.delimitL("privat_staatlich"), false);
		
		refreshFKs("Versuchsbedingungen"); // ID_CB wurde auf UNIQUE gesetzt, damit der Import von der Combase reibungslos funktioniert!
	}

	/*
	private static void moveDblIntoDoubleKZ(String tableName, String DBLColumn) {		
		//move data
		String sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL(DBLColumn) + " FROM " + DBKernel.delimitL(tableName) + " WHERE " + DBKernel.delimitL(DBLColumn) + " IS NOT NULL";
		ResultSet rs = DBKernel.getResultSet(sql, false);
		try {
		    if (rs != null && rs.first()) {
				PreparedStatement ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("DoubleKennzahlen") +
						" (" + DBKernel.delimitL("Wert") + "," + DBKernel.delimitL("Wert_typ") + ") VALUES (?,1)", Statement.RETURN_GENERATED_KEYS);
		    	do {
			    	if (rs.getObject(DBLColumn) == null) System.err.println("WIIIIW:\t" + tableName + "\t" + DBLColumn + "\tID: " + rs.getInt("ID"));
			    	else {
				    	double dbl = rs.getDouble(DBLColumn);
				    	DBKernel.sendRequest(sql, false);
				    	ps.setDouble(1, dbl);
						if (ps.executeUpdate() > 0) {
					    	Date tableChange = checkLastChange(tableName, rs.getInt("ID"));
					        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					        Date datumAb = (Date)formatter.parse("2011-10-07 12:00:00");
					    	if (tableChange.getTime() > datumAb.getTime()) {
					    		System.err.print("FUCK!!! Pass auf beim mergen, ChangeLog ist hier ja nicht vorhanden!!!!\t" + tableName + "\t" + DBLColumn + "\t" + dbl + "\tID: " + rs.getInt("ID"));
					    	}
							Integer lastID = DBKernel.getLastInsertedID(ps);
							if (tableChange.getTime() > datumAb.getTime()) System.err.println("\t" + lastID);
							if (lastID != null) {
						    	sql = "UPDATE " + DBKernel.delimitL(tableName) +
						    			" SET " + DBKernel.delimitL(DBLColumn) + " = " + lastID + " WHERE " + DBKernel.delimitL("ID") + " = " + rs.getInt("ID");
						    	DBKernel.sendRequest(sql, false);
							}
							else {
								System.err.println("WSSDWW: lastID === NULL???\t" + tableName + "\t" + DBLColumn + "\t" + rs.getInt("ID") + "\t" + dbl);
							}
						}
					}	    		
		    	} while (rs.next());
		    }
		}
	    catch(Exception e) {MyLogger.handleException(e);}	
	}
	private static Date checkLastChange(String tableName, int id) {
		Date result = null;
		String sql = "SELECT " + DBKernel.delimitL("Zeitstempel") + " FROM " + DBKernel.delimitL("ChangeLog") +
				" WHERE " + DBKernel.delimitL("Tabelle") + " = '" + tableName + "' AND " + DBKernel.delimitL("TabellenID") + " = " + id +
				" ORDER BY " + DBKernel.delimitL("Zeitstempel") + " DESC";
		ResultSet rs = DBKernel.getResultSet(sql, false);
		try {
		    if (rs != null && rs.first()) {
		    	result = rs.getDate(1);
		    }
		}
	    catch(Exception e) {MyLogger.handleException(e);}	
		return result;
	}
	private static void moveExponent(String tableName, String expName, String DBLColumn, int column) {
		//move data
		String sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL(DBLColumn) + "," + DBKernel.delimitL(expName) + " FROM " + DBKernel.delimitL(tableName) + " WHERE " + DBKernel.delimitL(expName) + " IS NOT NULL";
		ResultSet rs = DBKernel.getResultSet(sql, false);
		try {
		    if (rs != null && rs.first()) {
		    	do {
			    	if (rs.getObject(DBLColumn) == null) System.err.println("WOW:\t" + tableName + "\t" + DBLColumn + "\t" + expName + "\t" + rs.getDouble(expName) + "\tID: " + rs.getInt("ID"));
			    	else {
				    	double dbl = rs.getDouble(expName);
				    	sql = "UPDATE " + DBKernel.delimitL("DoubleKennzahlen") +
				    			" SET " + DBKernel.delimitL("Exponent") + " = " + dbl + "," + DBKernel.delimitL("Standardabweichung_exp") + " = " + dbl + "," +
				    			DBKernel.delimitL("Minimum_exp") + " = " + dbl + "," + DBKernel.delimitL("Maximum_exp") + " = " + dbl + "," +
				    			DBKernel.delimitL("LCL95_exp") + " = " + dbl + "," + DBKernel.delimitL("UCL95_exp") + " = " + dbl +
				    			" WHERE " + DBKernel.delimitL("ID") + " = " + rs.getInt(DBLColumn);

						    	Date tableChange = checkLastChange(tableName, rs.getInt("ID"));
						        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						        Date datumAb = (Date)formatter.parse("2011-10-07 12:00:00");
						    	if (tableChange.getTime() > datumAb.getTime()) {
							    	Date dblChange = checkLastChange("DoubleKennzahlen", rs.getInt(DBLColumn));
							    	if (dblChange == null || datumAb.getTime() > dblChange.getTime()) {
							    		System.err.println("WERWWWEEW: Achtung: Bei einem folgenden Merge könnte dieser Exponent verloren gehen (wenn beim merge gilt: (tableChange >) datumAb > dblChange)...\t" + tableName + "\t" + DBLColumn + "\t" + expName + "\t" + rs.getDouble(expName) + "\tID: " + rs.getInt("ID") + "\t" + dblChange + "\t" + datumAb + "\t" + tableChange);
							    		System.err.println(sql);
							    	}
						    	}
				    	
				    	DBKernel.sendRequest(sql, false);
			    	}		    		
		    	} while (rs.next());
		    }
		}
	    catch(Exception e) {MyLogger.handleException(e);}	
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL(tableName) + " DROP COLUMN " + DBKernel.delimitL(expName), false);
		updateChangeLog(tableName, column, true);
	}
	public static void check4Updates_134_135(MyList myList) {
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DoubleKennzahlen") +
				" ALTER COLUMN " + DBKernel.delimitL("Einzelwert") + " RENAME TO " + DBKernel.delimitL("Wert"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DoubleKennzahlen") +
				" ALTER COLUMN " + DBKernel.delimitL("Einzelwert_g") + " RENAME TO " + DBKernel.delimitL("Wert_g"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DoubleKennzahlen") +
				" ADD COLUMN " + DBKernel.delimitL("Exponent") + " DOUBLE BEFORE " + DBKernel.delimitL("Wert_g"), false);
		updateChangeLog("DoubleKennzahlen", 2, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DoubleKennzahlen") +
				" ADD COLUMN " + DBKernel.delimitL("Wert_typ") + " INTEGER BEFORE " + DBKernel.delimitL("Wert_g"), false);
		updateChangeLog("DoubleKennzahlen", 3, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DoubleKennzahlen") +
				" ADD COLUMN " + DBKernel.delimitL("Standardabweichung_exp") + " DOUBLE BEFORE " + DBKernel.delimitL("Standardabweichung_g"), false);
		updateChangeLog("DoubleKennzahlen", 10, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DoubleKennzahlen") +
				" ADD COLUMN " + DBKernel.delimitL("Minimum_exp") + " DOUBLE BEFORE " + DBKernel.delimitL("Minimum_g"), false);
		updateChangeLog("DoubleKennzahlen", 15, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DoubleKennzahlen") +
				" ADD COLUMN " + DBKernel.delimitL("Maximum_exp") + " DOUBLE BEFORE " + DBKernel.delimitL("Maximum_g"), false);
		updateChangeLog("DoubleKennzahlen", 18, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DoubleKennzahlen") +
				" ADD COLUMN " + DBKernel.delimitL("LCL95_exp") + " DOUBLE BEFORE " + DBKernel.delimitL("LCL95_g"), false);
		updateChangeLog("DoubleKennzahlen", 21, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DoubleKennzahlen") +
				" ADD COLUMN " + DBKernel.delimitL("UCL95_exp") + " DOUBLE BEFORE " + DBKernel.delimitL("UCL95_g"), false);
		updateChangeLog("DoubleKennzahlen", 24, false);

		// alte Fehler bereinigen:
		String sql = "UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL("Standardabweichung") + " = 0.48," + DBKernel.delimitL("Wert") + " = NULL," +
				DBKernel.delimitL("Median") + " = NULL WHERE " + DBKernel.delimitL("ID") + " = 1418";
		DBKernel.sendRequest(sql, false);
		sql = "UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL("Standardabweichung") + " = 0.2," + DBKernel.delimitL("Wert") + " = NULL," +
				DBKernel.delimitL("Median") + " = NULL WHERE " + DBKernel.delimitL("ID") + " = 4049";
		DBKernel.sendRequest(sql, false);
		sql = "UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL("Standardabweichung") + " = 0," + DBKernel.delimitL("Wert") + " = NULL," +
				DBKernel.delimitL("Median") + " = NULL WHERE " + DBKernel.delimitL("ID") + " = 4659";
		DBKernel.sendRequest(sql, false);
		sql = "UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL("Standardabweichung") + " = 0.59," + DBKernel.delimitL("Wert") + " = NULL," +
				DBKernel.delimitL("Median") + " = NULL WHERE " + DBKernel.delimitL("ID") + " = 4672";
		DBKernel.sendRequest(sql, false);
		sql = "UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL("Standardabweichung") + " = 0.17," + DBKernel.delimitL("Wert") + " = NULL," +
				DBKernel.delimitL("Median") + " = NULL WHERE " + DBKernel.delimitL("ID") + " = 6251";
		DBKernel.sendRequest(sql, false);
		
		//move data
		sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Wert") + "," + DBKernel.delimitL("Mittelwert") + "," + DBKernel.delimitL("Median") + "," +
				 DBKernel.delimitL("Mittelwert_g") + "," + DBKernel.delimitL("Median_g") +
				" FROM " + DBKernel.delimitL("DoubleKennzahlen") + " WHERE " + DBKernel.delimitL("Mittelwert") + " IS NOT NULL OR " + DBKernel.delimitL("Median") + " IS NOT NULL";
		ResultSet rs = DBKernel.getResultSet(sql, false);
		try {
		    if (rs != null && rs.first()) {
		    	do {
		    		
		    		//WOWDBLKZ:	0.0	4.66	0.48	ID: 1418 => Messwerte ID: 1140, pH
		    		//WOWDBLKZ:	0.0	2.0	0.2	ID: 4049 => Messwerte ID: 718, Zeit
		    		//WOWDBLKZ:	6.5	0.0	0.0	ID: 4659 => Messwerte ID: 1663, Konzentration
		    		//WOWDBLKZ:	2.2	6.24	0.0	ID: 4672 => Messwerte ID: 1671, Konzentration
		    		//WOWDBLKZ:	0.0	2.62	0.17	ID: 6251 => Messwerte ID: 2653, Konzentration
		    		
			    	if (rs.getObject("Wert") != null || rs.getObject("Mittelwert") != null && rs.getObject("Median") != null) {
			    		System.err.println("WOWDBLKZ:\t" + rs.getDouble("Wert") + "\t" + rs.getDouble("Mittelwert") + "\t" + rs.getDouble("Median") + "\tID: " + rs.getInt("ID"));
			    	}
			    	else {
				    	double dbl = 0;
				    	Boolean dbl_g = null;
			    		if (rs.getObject("Mittelwert") != null) {
			    			dbl = rs.getDouble("Mittelwert");
			    			dbl_g = rs.getBoolean("Mittelwert_g");
			    		}
			    		else {
			    			dbl = rs.getDouble("Median");
			    			dbl_g = rs.getBoolean("Median_g");
			    		}
				    	sql = "UPDATE " + DBKernel.delimitL("DoubleKennzahlen") +
				    			" SET " + DBKernel.delimitL("Wert") + " = " + dbl + "," + DBKernel.delimitL("Wert_typ") + " = " + (rs.getObject("Mittelwert") != null ? 2 : 3) +
				    			"," + DBKernel.delimitL("Wert_g") + " = " + (dbl_g == null ? "NULL" : (dbl_g ? "TRUE" : "FALSE")) +
				    			" WHERE " + DBKernel.delimitL("ID") + " = " + rs.getInt("ID");
				    	DBKernel.sendRequest(sql, false);
			    	}		    		
		    	} while (rs.next());
		    }
		}
	    catch(Exception e) {MyLogger.handleException(e);}	
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DoubleKennzahlen") +
				" DROP COLUMN " + DBKernel.delimitL("Median_g"), false);
		updateChangeLog("DoubleKennzahlen", 13, true);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DoubleKennzahlen") +
				" DROP COLUMN " + DBKernel.delimitL("Median"), false);
		updateChangeLog("DoubleKennzahlen", 12, true);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DoubleKennzahlen") +
				" DROP COLUMN " + DBKernel.delimitL("Mittelwert_g"), false);
		updateChangeLog("DoubleKennzahlen", 8, true);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DoubleKennzahlen") +
				" DROP COLUMN " + DBKernel.delimitL("Mittelwert"), false);
		updateChangeLog("DoubleKennzahlen", 7, true);

		moveExponent("Versuchsbedingungen_Sonstiges", "*10^n", "Wert", 4);
		moveExponent("Messwerte_Sonstiges", "*10^n", "Wert", 4);
		moveExponent("Messwerte", "*10^n", "Konzentration", 6);
		moveExponent("Krankheitsbilder", "*10^l", "Letalitätsdosis100", 25);
		moveExponent("Krankheitsbilder", "*10^m", "Letalitätsdosis50", 20);
		moveExponent("Krankheitsbilder", "*10^n", "Infektionsdosis", 17);
		moveDblIntoDoubleKZ("Aufbereitungs_Nachweisverfahren", "Nachweisgrenze");
		moveExponent("Aufbereitungs_Nachweisverfahren", "*10^n", "Nachweisgrenze", 4);
		moveExponent("Prozessdaten_Sonstiges", "*10^n", "Wert", 4);
		moveExponent("Prozessdaten_Messwerte", "*10^n (GKZ)", "Konzentration (GKZ)", 9);
		moveExponent("Prozessdaten_Messwerte", "*10^n", "Konzentration", 6);
		moveExponent("Zutatendaten_Sonstiges", "*10^n", "Wert", 4);
		
		// weitere alte Fehler bereinigen, die mit dem Exponenten zu tun haben:
		sql = "UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL("Exponent") + " = 2 WHERE " + DBKernel.delimitL("ID") + " = 4667";
		DBKernel.sendRequest(sql, false);
		
		// Jetzt noch alle Umlaute und Sonderzeichen weg!
		rs = DBKernel.getResultSet("SELECT TABLE_NAME,TABLE_TYPE FROM INFORMATION_SCHEMA.TABLES", false);
		try {
		    if (rs != null && rs.first()) {
		    	do {
		    		if (rs.getObject("TABLE_NAME") != null && rs.getObject("TABLE_TYPE").equals("BASE TABLE")) {
		    			String tn = rs.getString("TABLE_NAME");
		    			if (!tn.equals("BLOCKS") && !tn.equals("LOBS") && !tn.equals("LOB_IDS")
		    					 && !tn.equals("Agenzien") && !tn.equals("Artikel_Lieferung") && !tn.equals("Aufbereitungsverfahren_Kits")
		    					 && !tn.equals("ChangeLog") && !tn.equals("Codes-Agenzien") && !tn.equals("Codes-Matrices")
		    					 && !tn.equals("DateiSpeicher") && !tn.equals("DoubleKennzahlen") && !tn.equals("Einheiten")
		    					 && !tn.equals("Exposition") && !tn.equals("ICD10_Gruppen") && !tn.equals("ICD10_Kapitel")
		    					 && !tn.equals("ICD10_Kodes") && !tn.equals("ICD10_MorbL") && !tn.equals("ICD10_MortL1")
		    					 && !tn.equals("ICD10_MortL1Grp") && !tn.equals("ICD10_MortL2") && !tn.equals("ICD10_MortL3")
		    					 && !tn.equals("ICD10_MortL3Grp") && !tn.equals("ICD10_MortL4") && !tn.equals("Infotabelle")
		    					 && !tn.equals("Kontakte") && !tn.equals("Krankheitsbilder_Symptome") && !tn.equals("Labore")
		    					 && !tn.equals("Labore_Agenzien") && !tn.equals("Labore_Agenzien_Methodiken") && !tn.equals("Labore_Matrices")
		    					 && !tn.equals("Labore_Methodiken") && !tn.equals("Lieferung_Lieferungen") && !tn.equals("Literatur")
		    					 && !tn.equals("Länder") && !tn.equals("Matrices") && !tn.equals("Messwerte_Sonstiges")
		    					 && !tn.equals("Methoden") && !tn.equals("Methoden_Normen") && !tn.equals("Methoden_Software")
		    					 && !tn.equals("Methodennormen") && !tn.equals("Methodiken") && !tn.equals("Modell_Agenzien_Verbund")
		    					 && !tn.equals("Modell_Einzelhandelsprodukt_Verbund") && !tn.equals("Modell_Exposition_Verbund") && !tn.equals("Modell_Harvest_Verbund")
		    					 && !tn.equals("Modell_Preharvest_Verbund") && !tn.equals("Modell_Resistenz_Verbund") && !tn.equals("Modell_Risikocharakterisierung_Verbund")
		    					 && !tn.equals("Modell_Software_Verbund") && !tn.equals("Modell_Transport_Verbund") && !tn.equals("Modell_Verwendung_Verbund")
		    					 && !tn.equals("Modell_Zwischenprodukt_Verbund") && !tn.equals("Nachweisverfahren_Kits") && !tn.equals("Produkt")
		    					 && !tn.equals("Produzent") && !tn.equals("Produzent_Artikel") && !tn.equals("Prozess-Verbindungen")
		    					 && !tn.equals("ProzessElemente") && !tn.equals("Prozessdaten_Messwerte") && !tn.equals("Prozessdaten_Sonstiges")
		    					 && !tn.equals("Resistenz") && !tn.equals("Risikocharakterisierung") && !tn.equals("SonstigeParameter")
		    					 && !tn.equals("Symptome") && !tn.equals("Tierkrankheiten") && !tn.equals("ToxinUrsprung")
		    					 && !tn.equals("Transport") && !tn.equals("Users") && !tn.equals("Versuchsbedingungen_Sonstiges")
		    					 && !tn.equals("Verwendung") && !tn.equals("Zertifizierungssysteme") && !tn.equals("Zutatendaten_Sonstiges")
		    					 && !tn.equals("Codes-Methoden") && !tn.equals("Codes-Methodiken") && !tn.equals("ComBaseImport")
		    					 && !tn.equals("Kennzahlen")) {
			    			System.out.println(tn + "\t" + rs.getObject("TABLE_TYPE"));
			    			DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL(tn) +
			    					" ALTER COLUMN " + DBKernel.delimitL("Geprüft") + " RENAME TO " + DBKernel.delimitL("Geprueft"), false);
			    			if (!tn.equals("Kits") && !tn.equals("Messwerte") && !tn.equals("Zutatendaten")) {
			    				DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL(tn) +
				    					" ALTER COLUMN " + DBKernel.delimitL("Gütescore (subj.)") + " RENAME TO " + DBKernel.delimitL("Guetescore"), false);
			    			}
		    			}
		    		}
		    	} while (rs.next());
		    }
	    }
	    catch(Exception e) {MyLogger.handleException(e);}	
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("DateiSpeicher") +
				" ALTER COLUMN " + DBKernel.delimitL("Dateigröße") + " RENAME TO " + DBKernel.delimitL("Dateigroesse"), false);
		
		//DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Kennzahlen") + " IF EXISTS", false);
		// Lieber doch noch nicht droppen, vielleicht später - möglicherweise benötige ich das noch für niederberger und buschulte DB zum Mergen... oder andere? Analytic Jena? Burchardi?
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ALTER COLUMN " + DBKernel.delimitL("Wissenschaftliche Bezeichnung") + " RENAME TO " + DBKernel.delimitL("WissenschaftlicheBezeichnung"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ALTER COLUMN " + DBKernel.delimitL("Subspezies/Subtyp") + " RENAME TO " + DBKernel.delimitL("Subspezies_Subtyp"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ALTER COLUMN " + DBKernel.delimitL("Gramfärbung") + " RENAME TO " + DBKernel.delimitL("Gramfaerbung"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ALTER COLUMN " + DBKernel.delimitL("CAS-Nummer") + " RENAME TO " + DBKernel.delimitL("CAS_Nummer"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ALTER COLUMN " + DBKernel.delimitL("Carver-Nummer") + " RENAME TO " + DBKernel.delimitL("Carver_Nummer"), false);
		refreshFKs("Agenzien");
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Codes-Matrices") +
				" RENAME TO " + DBKernel.delimitL("Codes_Matrices"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Codes-Agenzien") +
				" RENAME TO " + DBKernel.delimitL("Codes_Agenzien"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Codes-Methoden") +
				" RENAME TO " + DBKernel.delimitL("Codes_Methoden"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Codes-Methodiken") +
				" RENAME TO " + DBKernel.delimitL("Codes_Methodiken"), false);
		refreshFKs("Codes_Matrices");
		refreshFKs("Codes_Agenzien");
		refreshFKs("Codes_Methoden");
		refreshFKs("Codes_Methodiken");
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DateiSpeicher") + " SET " + DBKernel.delimitL("Tabelle") + "='Codes_Matrices' WHERE " + DBKernel.delimitL("Tabelle") + "='Codes-Matrices'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("ChangeLog") + " SET " + DBKernel.delimitL("Tabelle") + "='Codes_Matrices' WHERE " + DBKernel.delimitL("Tabelle") + "='Codes-Matrices'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DateiSpeicher") + " SET " + DBKernel.delimitL("Tabelle") + "='Codes_Agenzien' WHERE " + DBKernel.delimitL("Tabelle") + "='Codes-Agenzien'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("ChangeLog") + " SET " + DBKernel.delimitL("Tabelle") + "='Codes_Agenzien' WHERE " + DBKernel.delimitL("Tabelle") + "='Codes-Agenzien'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DateiSpeicher") + " SET " + DBKernel.delimitL("Tabelle") + "='Codes_Methoden' WHERE " + DBKernel.delimitL("Tabelle") + "='Codes-Methoden'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("ChangeLog") + " SET " + DBKernel.delimitL("Tabelle") + "='Codes_Methoden' WHERE " + DBKernel.delimitL("Tabelle") + "='Codes-Methoden'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DateiSpeicher") + " SET " + DBKernel.delimitL("Tabelle") + "='Codes_Methodiken' WHERE " + DBKernel.delimitL("Tabelle") + "='Codes-Methodiken'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("ChangeLog") + " SET " + DBKernel.delimitL("Tabelle") + "='Codes_Methodiken' WHERE " + DBKernel.delimitL("Tabelle") + "='Codes-Methodiken'", false);

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Länder") +
				" RENAME TO " + DBKernel.delimitL("Laender"), false);
		refreshFKs("Laender");
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DateiSpeicher") + " SET " + DBKernel.delimitL("Tabelle") + "='Laender' WHERE " + DBKernel.delimitL("Tabelle") + "='Länder'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("ChangeLog") + " SET " + DBKernel.delimitL("Tabelle") + "='Laender' WHERE " + DBKernel.delimitL("Tabelle") + "='Länder'", false);

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kontakte") +
				" ALTER COLUMN " + DBKernel.delimitL("Straße") + " RENAME TO " + DBKernel.delimitL("Strasse"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kontakte") +
				" ALTER COLUMN " + DBKernel.delimitL("E-Mail") + " RENAME TO " + DBKernel.delimitL("EMail"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kontakte") +
				" ALTER COLUMN " + DBKernel.delimitL("Web-Site") + " RENAME TO " + DBKernel.delimitL("Webseite"), false);
		refreshFKs("Kontakte");
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Symptome") +
				" ALTER COLUMN " + DBKernel.delimitL("Bezeichnung(engl)") + " RENAME TO " + DBKernel.delimitL("Bezeichnung_engl"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Symptome") +
				" ALTER COLUMN " + DBKernel.delimitL("Beschreibung(engl)") + " RENAME TO " + DBKernel.delimitL("Beschreibung_engl"), false);
		refreshFKs("Symptome");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Tierkrankheiten") +
				" ALTER COLUMN " + DBKernel.delimitL("VET-Code") + " RENAME TO " + DBKernel.delimitL("VET_Code"), false);
		refreshFKs("Tierkrankheiten");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("Risikokategorie (CDC)") + " RENAME TO " + DBKernel.delimitL("Risikokategorie_CDC"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("Inzidenz Alter") + " RENAME TO " + DBKernel.delimitL("Inzidenz_Alter"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("IZ-Einheit") + " RENAME TO " + DBKernel.delimitL("IZ_Einheit"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("SD-Einheit") + " RENAME TO " + DBKernel.delimitL("SD_Einheit"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("ID-Einheit") + " RENAME TO " + DBKernel.delimitL("ID_Einheit"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("Letalitätsdosis50") + " RENAME TO " + DBKernel.delimitL("Letalitaetsdosis50"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("LD50-Einheit") + " RENAME TO " + DBKernel.delimitL("LD50_Einheit"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("LD50-Organismus") + " RENAME TO " + DBKernel.delimitL("LD50_Organismus"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("LD50-Aufnahmeroute") + " RENAME TO " + DBKernel.delimitL("LD50_Aufnahmeroute"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("Letalitätsdosis100") + " RENAME TO " + DBKernel.delimitL("Letalitaetsdosis100"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("LD100-Einheit") + " RENAME TO " + DBKernel.delimitL("LD100_Einheit"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("LD100-Organismus") + " RENAME TO " + DBKernel.delimitL("LD100_Organismus"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("LD100-Aufnahmeroute") + " RENAME TO " + DBKernel.delimitL("LD100_Aufnahmeroute"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("Morbidität") + " RENAME TO " + DBKernel.delimitL("Morbiditaet"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("Mortalität") + " RENAME TO " + DBKernel.delimitL("Mortalitaet"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("Letalität") + " RENAME TO " + DBKernel.delimitL("Letalitaet"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("<-Therapie(j/n)") + " RENAME TO " + DBKernel.delimitL("Therapie_Letal"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("Spätschäden") + " RENAME TO " + DBKernel.delimitL("Spaetschaeden"), false);

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zertifizierungssysteme") +
				" ALTER COLUMN " + DBKernel.delimitL("Abkürzung") + " RENAME TO " + DBKernel.delimitL("Abkuerzung"), false);
		refreshFKs("Zertifizierungssysteme");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Methodiken") +
				" ALTER COLUMN " + DBKernel.delimitL("Wissenschaftliche Bezeichnung") + " RENAME TO " + DBKernel.delimitL("WissenschaftlicheBezeichnung"), false);
		refreshFKs("Methodiken");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Labore") +
				" ALTER COLUMN " + DBKernel.delimitL("HIT-Nummer") + " RENAME TO " + DBKernel.delimitL("HIT_Nummer"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Labore") +
				" ALTER COLUMN " + DBKernel.delimitL("ADV-Nummer") + " RENAME TO " + DBKernel.delimitL("ADV_Nummer"), false);
		refreshFKs("Labore");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
				" ALTER COLUMN " + DBKernel.delimitL("Gültigkeit") + " RENAME TO " + DBKernel.delimitL("Gueltigkeit"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
				" ALTER COLUMN " + DBKernel.delimitL("DNA Extraktion") + " RENAME TO " + DBKernel.delimitL("DNA_Extraktion"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
				" ALTER COLUMN " + DBKernel.delimitL("RNA Extraktion") + " RENAME TO " + DBKernel.delimitL("RNA_Extraktion"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
				" ALTER COLUMN " + DBKernel.delimitL("Protein Extraktion") + " RENAME TO " + DBKernel.delimitL("Protein_Extraktion"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kits") +
				" ALTER COLUMN " + DBKernel.delimitL("Bezeichnung des Extraktionssystems") + " RENAME TO " + DBKernel.delimitL("Extraktionssystem_Bezeichnung"), false);
		refreshFKs("Kits");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Aufbereitungsverfahren") +
				" ALTER COLUMN " + DBKernel.delimitL("Wissenschaftliche Bezeichnung") + " RENAME TO " + DBKernel.delimitL("WissenschaftlicheBezeichnung"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Aufbereitungsverfahren") +
				" ALTER COLUMN " + DBKernel.delimitL("DNA Extraktion") + " RENAME TO " + DBKernel.delimitL("DNA_Extraktion"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Aufbereitungsverfahren") +
				" ALTER COLUMN " + DBKernel.delimitL("RNA Extraktion") + " RENAME TO " + DBKernel.delimitL("RNA_Extraktion"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Aufbereitungsverfahren") +
				" ALTER COLUMN " + DBKernel.delimitL("Protein Extraktion") + " RENAME TO " + DBKernel.delimitL("Protein_Extraktion"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Aufbereitungsverfahren") +
				" ALTER COLUMN " + DBKernel.delimitL("SOP/LA") + " RENAME TO " + DBKernel.delimitL("SOP_LA"), false);
		refreshFKs("Aufbereitungsverfahren");
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DateiSpeicher") + " SET " + DBKernel.delimitL("Feld") + "='SOP_LA' WHERE " + DBKernel.delimitL("Feld") + "='SOP/LA'", false);

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Aufbereitungsverfahren_Normen") +
				" ALTER COLUMN " + DBKernel.delimitL("Norm-Nummer") + " RENAME TO " + DBKernel.delimitL("Norm_Nummer"), false);
		refreshFKs("Aufbereitungsverfahren_Normen");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Methoden_Normen") +
				" ALTER COLUMN " + DBKernel.delimitL("Norm-Nummer") + " RENAME TO " + DBKernel.delimitL("Norm_Nummer"), false);
		refreshFKs("Methoden_Normen");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Nachweisverfahren_Normen") +
				" ALTER COLUMN " + DBKernel.delimitL("Norm-Nummer") + " RENAME TO " + DBKernel.delimitL("Norm_Nummer"), false);
		refreshFKs("Nachweisverfahren_Normen");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Nachweisverfahren") +
				" ALTER COLUMN " + DBKernel.delimitL("SOP/LA") + " RENAME TO " + DBKernel.delimitL("SOP_LA"), false);
		refreshFKs("Nachweisverfahren");
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DateiSpeicher") + " SET " + DBKernel.delimitL("Feld") + "='SOP_LA' WHERE " + DBKernel.delimitL("Feld") + "='SOP/LA'", false);

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Aufbereitungs_Nachweisverfahren") +
				" ALTER COLUMN " + DBKernel.delimitL("NG-Einheit") + " RENAME TO " + DBKernel.delimitL("NG_Einheit"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Aufbereitungs_Nachweisverfahren") +
				" ALTER COLUMN " + DBKernel.delimitL("Sensitivität") + " RENAME TO " + DBKernel.delimitL("Sensitivitaet"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Aufbereitungs_Nachweisverfahren") +
				" ALTER COLUMN " + DBKernel.delimitL("Spezifität") + " RENAME TO " + DBKernel.delimitL("Spezifitaet"), false);
		refreshFKs("Aufbereitungs_Nachweisverfahren");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Labor_Aufbereitungs_Nachweisverfahren") +
				" ALTER COLUMN " + DBKernel.delimitL("Gültigkeit") + " RENAME TO " + DBKernel.delimitL("Gueltigkeit"), false);
		refreshFKs("Labor_Aufbereitungs_Nachweisverfahren");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Versuchsbedingungen") +
				" ALTER COLUMN " + DBKernel.delimitL("in/on") + " RENAME TO " + DBKernel.delimitL("in_on"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Versuchsbedingungen") +
				" ALTER COLUMN " + DBKernel.delimitL("ID (CB)") + " RENAME TO " + DBKernel.delimitL("ID_CB"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Versuchsbedingungen") +
				" ALTER COLUMN " + DBKernel.delimitL("Organismus (CB)") + " RENAME TO " + DBKernel.delimitL("Organismus_CB"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Versuchsbedingungen") +
				" ALTER COLUMN " + DBKernel.delimitL("environment (CB)") + " RENAME TO " + DBKernel.delimitL("environment_CB"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Versuchsbedingungen") +
				" ALTER COLUMN " + DBKernel.delimitL("b_f (CB)") + " RENAME TO " + DBKernel.delimitL("b_f_CB"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Versuchsbedingungen") +
				" ALTER COLUMN " + DBKernel.delimitL("b_f_details (CB)") + " RENAME TO " + DBKernel.delimitL("b_f_details_CB"), false);
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Messwerte") +
				" ALTER COLUMN " + DBKernel.delimitL("Konz-Einheit") + " RENAME TO " + DBKernel.delimitL("Konz_Einheit"), false);

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Versuchsbedingungen_Sonstiges") +
				" ALTER COLUMN " + DBKernel.delimitL("Ja/Nein") + " RENAME TO " + DBKernel.delimitL("Ja_Nein"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Messwerte_Sonstiges") +
				" ALTER COLUMN " + DBKernel.delimitL("Ja/Nein") + " RENAME TO " + DBKernel.delimitL("Ja_Nein"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten_Sonstiges") +
				" ALTER COLUMN " + DBKernel.delimitL("Ja/Nein") + " RENAME TO " + DBKernel.delimitL("Ja_Nein"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten_Sonstiges") +
				" ALTER COLUMN " + DBKernel.delimitL("Ja/Nein") + " RENAME TO " + DBKernel.delimitL("Ja_Nein"), false);
		refreshFKs("Versuchsbedingungen_Sonstiges");
		refreshFKs("Messwerte_Sonstiges");
		refreshFKs("Prozessdaten_Sonstiges");
		refreshFKs("Zutatendaten_Sonstiges");
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Betrieb_Matrix_Produktion") +
				" ALTER COLUMN " + DBKernel.delimitL("Anteil in %") + " RENAME TO " + DBKernel.delimitL("Anteil"), false);
		refreshFKs("Betrieb_Matrix_Produktion");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ProzessElemente") +
				" ALTER COLUMN " + DBKernel.delimitL("Prozess-ID") + " RENAME TO " + DBKernel.delimitL("Prozess_ID"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ProzessElemente") +
				" ALTER COLUMN " + DBKernel.delimitL("ProzessElement (engl.)") + " RENAME TO " + DBKernel.delimitL("ProzessElement_engl"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ProzessElemente") +
				" ALTER COLUMN " + DBKernel.delimitL("ProzessElementKategorie (engl.)") + " RENAME TO " + DBKernel.delimitL("ProzessElementKategorie_engl"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ProzessElemente") +
				" ALTER COLUMN " + DBKernel.delimitL("ProzessElementSubKategorie (engl.)") + " RENAME TO " + DBKernel.delimitL("ProzessElementSubKategorie_engl"), false);
		refreshFKs("ProzessElemente");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozess-Workflow") +
				" RENAME TO " + DBKernel.delimitL("Prozess_Workflow"), false);
		refreshFKs("Prozess_Workflow");
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DateiSpeicher") + " SET " + DBKernel.delimitL("Tabelle") + "='Prozess_Workflow' WHERE " + DBKernel.delimitL("Tabelle") + "='Prozess-Workflow'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("ChangeLog") + " SET " + DBKernel.delimitL("Tabelle") + "='Prozess_Workflow' WHERE " + DBKernel.delimitL("Tabelle") + "='Prozess-Workflow'", false);
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten") +
				" ALTER COLUMN " + DBKernel.delimitL("Prozess (CARVER)") + " RENAME TO " + DBKernel.delimitL("Prozess_CARVER"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten") +
				" ALTER COLUMN " + DBKernel.delimitL("Kapazität") + " RENAME TO " + DBKernel.delimitL("Kapazitaet"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten") +
				" ALTER COLUMN " + DBKernel.delimitL("KapazitätEinheit") + " RENAME TO " + DBKernel.delimitL("KapazitaetEinheit"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten") +
				" ALTER COLUMN " + DBKernel.delimitL("KapazitätEinheitBezug") + " RENAME TO " + DBKernel.delimitL("KapazitaetEinheitBezug"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten") +
				" ALTER COLUMN " + DBKernel.delimitL("Tenazität") + " RENAME TO " + DBKernel.delimitL("Tenazitaet"), false);
		refreshFKs("Prozessdaten");
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten_Messwerte") +
				" ALTER COLUMN " + DBKernel.delimitL("Konzentration (GKZ)") + " RENAME TO " + DBKernel.delimitL("Konzentration_GKZ"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten_Messwerte") +
				" ALTER COLUMN " + DBKernel.delimitL("Einheit (GKZ)") + " RENAME TO " + DBKernel.delimitL("Einheit_GKZ"), false);
		refreshFKs("Prozessdaten_Messwerte");

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozess-Verbindungen") +
				" RENAME TO " + DBKernel.delimitL("Prozess_Verbindungen"), false);
		refreshFKs("Prozess_Verbindungen");
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DateiSpeicher") + " SET " + DBKernel.delimitL("Tabelle") + "='Prozess_Verbindungen' WHERE " + DBKernel.delimitL("Tabelle") + "='Prozess-Verbindungen'", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("ChangeLog") + " SET " + DBKernel.delimitL("Tabelle") + "='Prozess_Verbindungen' WHERE " + DBKernel.delimitL("Tabelle") + "='Prozess-Verbindungen'", false);

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ALTER COLUMN " + DBKernel.delimitL("Zutat/Produkt") + " RENAME TO " + DBKernel.delimitL("Zutat_Produkt"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ALTER COLUMN " + DBKernel.delimitL("#Units") + " RENAME TO " + DBKernel.delimitL("Units"), false);
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Versuchsbedingungen") +
				" ADD COLUMN " + DBKernel.delimitL("Luftfeuchtigkeit") + " DOUBLE BEFORE " + DBKernel.delimitL("in_on"), false);
		updateChangeLog("Versuchsbedingungen", 13, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Messwerte") +
				" ADD COLUMN " + DBKernel.delimitL("Luftfeuchtigkeit") + " DOUBLE BEFORE " + DBKernel.delimitL("Sonstiges"), false);
		updateChangeLog("Messwerte", 12, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ADD COLUMN " + DBKernel.delimitL("Luftfeuchtigkeit") + " DOUBLE BEFORE " + DBKernel.delimitL("Sonstiges"), false);
		updateChangeLog("Zutatendaten", 15, false);

		refreshFKs("Versuchsbedingungen");
		refreshFKs("Messwerte");
		refreshFKs("Zutatendaten");	
		
		// StatUp Tabellen
		myList.getTable("Modellkatalog").createTable();
		myList.getTable("ModellkatalogParameter").createTable();
		myList.getTable("Modell_Referenz").createTable();
		myList.getTable("GeschaetzteModelle").createTable();
		myList.getTable("GeschaetztesModell_Referenz").createTable();
		myList.getTable("GeschaetzteParameter").createTable();
		myList.getTable("GeschaetzteParameterCovCor").createTable();
		myList.getTable("Sekundaermodelle_Primaermodelle").createTable();
		doStatUpGrants();
		
		myList.getTable("Risikogruppen").createTable();		
		DBKernel.grantDefaults("Risikogruppen");		
		myList.getTable("Krankheitsbilder_Risikogruppen").createTable();
		DBKernel.grantDefaults("Krankheitsbilder_Risikogruppen");		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") + " ADD COLUMN " + DBKernel.delimitL("Risikogruppen") + " INTEGER BEFORE " + DBKernel.delimitL("Inzidenz"), false);
		updateChangeLog("Krankheitsbilder", 10, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") + " ADD COLUMN " + DBKernel.delimitL("AgensDetail") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Risikokategorie_CDC"), false);
		updateChangeLog("Krankheitsbilder", 3, false);
		refreshFKs("Krankheitsbilder");
		try {
	    	PreparedStatement ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Users") +
					" (" + DBKernel.delimitL("Username") + "," + DBKernel.delimitL("Vorname") + "," + DBKernel.delimitL("Name") + "," + DBKernel.delimitL("Zugriffsrecht") + ") VALUES (?,?,?,?)");
	    	ps.setString(1, "frentzel"); ps.setString(2, "Hendrik"); ps.setString(3, "Frentzel"); ps.setInt(4, Users.SUPER_WRITE_ACCESS); ps.execute();			
			ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Risikogruppen") +
					" (" + DBKernel.delimitL("Bezeichnung") + ") VALUES (?)");
			ps.setString(1, "Senioren"); ps.execute();
			ps.setString(1, "Kinder"); ps.execute();
			ps.setString(1, "Jugendliche"); ps.execute();
			ps.setString(1, "Immunsupprimierte Menschen"); ps.execute();
			ps.setString(1, "Schwangere"); ps.execute();
			ps.setString(1, "Kleinkinder/Säuglinge"); ps.execute();			
		}
		catch (Exception e) {e.printStackTrace();}
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten") +
				" ALTER COLUMN " + DBKernel.delimitL("Tenazitaet") + " SET DATA TYPE INTEGER", false);

		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Versuchsbedingungen") +
				" ALTER COLUMN " + DBKernel.delimitL("ID_CB") + " SET DATA TYPE VARCHAR(50)", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Versuchsbedingungen") +
				" ALTER COLUMN " + DBKernel.delimitL("b_f_CB") + " SET DATA TYPE VARCHAR(255)", false);
		
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Literatur") +
				" SET " + DBKernel.delimitL("Erstautor") + " = 'Abdul-Raouf' WHERE " + DBKernel.delimitL("ID") + " = 2", false);
	}
	public static void check4Updates_133_134(MyList myList) {
		// Prozessdaten, leider wurde beim Update 131_132 Tenazität ans Ende gestellt!! Noch hinter Kommentar u.ä...
		// ACHTUNG: hier ggf. überprüfen, ob (vor allem) Almut hier neue Daten eingegeben hat, da ist die Reihenfolge des ChangeLog Arrays etwas durcheinander
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten") +
				" ADD COLUMN " + DBKernel.delimitL("tenzt") + " DOUBLE BEFORE " + DBKernel.delimitL("Gütescore (subj.)"), false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten") +
				" SET " + DBKernel.delimitL("tenzt") + " = " + DBKernel.delimitL("Tenazität"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten") +
				" DROP COLUMN " + DBKernel.delimitL("Tenazität"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten") +
				" ALTER COLUMN " + DBKernel.delimitL("tenzt") + " RENAME TO " + DBKernel.delimitL("Tenazität"), false);
		//updateChangeLog("Prozessdaten", 18, false);
		refreshFKs("Prozessdaten");
	}
	public static void check4Updates_132_133(MyList myList) {
		// Krankheitsbilder
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") + " ADD COLUMN " + DBKernel.delimitL("Ausscheidungsdauer") + " VARCHAR(50) BEFORE " + DBKernel.delimitL("ansteckend"), false);
		updateChangeLog("Krankheitsbilder", 34, false);

	  // Zutatendaten_Sonstiges
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten_Sonstiges") + " ADD COLUMN " + DBKernel.delimitL("Kommentar") + " VARCHAR(1023)", false);
		updateChangeLog("Zutatendaten_Sonstiges", 7, false);
	  // Versuchsbedingungen_Sonstiges
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Versuchsbedingungen_Sonstiges") + " ADD COLUMN " + DBKernel.delimitL("Kommentar") + " VARCHAR(1023)", false);
		updateChangeLog("Versuchsbedingungen_Sonstiges", 7, false);
	  // Messwerte_Sonstiges
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Messwerte_Sonstiges") + " ADD COLUMN " + DBKernel.delimitL("Kommentar") + " VARCHAR(1023)", false);
		updateChangeLog("Messwerte_Sonstiges", 7, false);
	  // Prozessdaten_Sonstiges
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten_Sonstiges") + " ADD COLUMN " + DBKernel.delimitL("Kommentar") + " VARCHAR(1023)", false);
		updateChangeLog("Prozessdaten_Sonstiges", 7, false);
	  
	  // Prozessdaten_Messwerte
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten_Messwerte") + " ADD COLUMN " + DBKernel.delimitL("Zeit") + " DOUBLE BEFORE " + DBKernel.delimitL("Konzentration"), false);
		updateChangeLog("Prozessdaten_Messwerte", 3, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten_Messwerte") + " ADD COLUMN " + DBKernel.delimitL("ZeitEinheit") + " VARCHAR(50) BEFORE " + DBKernel.delimitL("Konzentration"), false);
		updateChangeLog("Prozessdaten_Messwerte", 4, false);
				
		// Überführen der DBL in die neue Tabelle
		KZ2NKZ();
		
		DBKernel.dontLog = true;
		try {
			PreparedStatement ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Matrices") +
					" (" + DBKernel.delimitL("Matrixname") + ") VALUES (?)");
			ps.setString(1, "Brucella-Bouillon"); ps.execute();
			ps.setString(1, "feste Nährmedien"); ps.execute();
			ps.setString(1, "Butterfield's Phosphate Buffer"); ps.execute();
			ps.setString(1, "flüssige Nährmedien"); ps.execute();
			ps.setString(1, "Brucella-Agar"); ps.execute();
			ps.setString(1, "Brucella-Selektiv-Agar"); ps.execute();
	    	ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Codes-Matrices") +
					" (" + DBKernel.delimitL("CodeSystem") + "," + DBKernel.delimitL("Code") + "," + DBKernel.delimitL("Basis") + ") VALUES (?,?,?)");
			ps.setString(1, "SiLeBAT"); ps.setString(2, "0100"); ps.setInt(3, 19986); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "00"); ps.setInt(3, 19987); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "0101"); ps.setInt(3, 19988); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "01"); ps.setInt(3, 19989); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "0000"); ps.setInt(3, 19990); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "0001"); ps.setInt(3, 19991); ps.execute();
			ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Agenzien") +
					" (" + DBKernel.delimitL("Agensname") + ") VALUES (?)");
			ps.setString(1, "Gruppe fakultativ anaerober gramnegativer Stäbchen"); ps.execute();
			ps.setString(1, "Genus Escherichia"); ps.execute();
			ps.setString(1, "Escherichia coli 0104:H4"); ps.execute();
	    	ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Codes-Agenzien") +
					" (" + DBKernel.delimitL("CodeSystem") + "," + DBKernel.delimitL("Code") + "," + DBKernel.delimitL("Basis") + ") VALUES (?,?,?)");
			ps.setString(1, "SiLeBAT"); ps.setString(2, "00"); ps.setInt(3, 3603); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "0000"); ps.setInt(3, 3604); ps.execute();
			ps.setString(1, "SiLeBAT"); ps.setString(2, "000000"); ps.setInt(3, 3605); ps.execute();
			DBKernel.doMNs(DBKernel.myList.getTable("Matrices"));
			DBKernel.doMNs(DBKernel.myList.getTable("Agenzien"));
		}
		catch (Exception e) {MyLogger.handleException(e);}
		DBKernel.dontLog = false;
	}
	private static void prepareNewDBL() {
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Matrices") + " SET " + DBKernel.delimitL("pH") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Matrices") + " SET " + DBKernel.delimitL("aw") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Matrices") + " SET " + DBKernel.delimitL("Dichte") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Krankheitsbilder") + " SET " + DBKernel.delimitL("Inzidenz") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Krankheitsbilder") + " SET " + DBKernel.delimitL("Inzidenz Alter") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Krankheitsbilder") + " SET " + DBKernel.delimitL("Inkubationszeit") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Krankheitsbilder") + " SET " + DBKernel.delimitL("Symptomdauer") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Krankheitsbilder") + " SET " + DBKernel.delimitL("Infektionsdosis") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Krankheitsbilder") + " SET " + DBKernel.delimitL("Letalitätsdosis50") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Krankheitsbilder") + " SET " + DBKernel.delimitL("Letalitätsdosis100") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Krankheitsbilder") + " SET " + DBKernel.delimitL("Morbidität") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Krankheitsbilder") + " SET " + DBKernel.delimitL("Mortalität") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Krankheitsbilder") + " SET " + DBKernel.delimitL("Letalität") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Versuchsbedingungen") + " SET " + DBKernel.delimitL("Temperatur") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Versuchsbedingungen") + " SET " + DBKernel.delimitL("pH") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Versuchsbedingungen") + " SET " + DBKernel.delimitL("aw") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Versuchsbedingungen") + " SET " + DBKernel.delimitL("CO2") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Versuchsbedingungen") + " SET " + DBKernel.delimitL("Druck") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte") + " SET " + DBKernel.delimitL("Zeit") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte") + " SET " + DBKernel.delimitL("Konzentration") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte") + " SET " + DBKernel.delimitL("Temperatur") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte") + " SET " + DBKernel.delimitL("pH") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte") + " SET " + DBKernel.delimitL("aw") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte") + " SET " + DBKernel.delimitL("CO2") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte") + " SET " + DBKernel.delimitL("Druck") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Versuchsbedingungen_Sonstiges") + " SET " + DBKernel.delimitL("Wert") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Messwerte_Sonstiges") + " SET " + DBKernel.delimitL("Wert") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten") + " SET " + DBKernel.delimitL("Kapazität") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten") + " SET " + DBKernel.delimitL("Dauer") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten") + " SET " + DBKernel.delimitL("Temperatur") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten") + " SET " + DBKernel.delimitL("pH") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten") + " SET " + DBKernel.delimitL("aw") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten") + " SET " + DBKernel.delimitL("CO2") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten") + " SET " + DBKernel.delimitL("Druck") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten") + " SET " + DBKernel.delimitL("Luftfeuchtigkeit") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten_Sonstiges") + " SET " + DBKernel.delimitL("Wert") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten_Messwerte") + " SET " + DBKernel.delimitL("Zeit") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten_Messwerte") + " SET " + DBKernel.delimitL("Konzentration") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Prozessdaten_Messwerte") + " SET " + DBKernel.delimitL("Konzentration (GKZ)") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Zutatendaten") + " SET " + DBKernel.delimitL("#Units") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Zutatendaten") + " SET " + DBKernel.delimitL("Unitmenge") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Zutatendaten") + " SET " + DBKernel.delimitL("Temperatur") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Zutatendaten") + " SET " + DBKernel.delimitL("pH") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Zutatendaten") + " SET " + DBKernel.delimitL("aw") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Zutatendaten") + " SET " + DBKernel.delimitL("CO2") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Zutatendaten") + " SET " + DBKernel.delimitL("Druck") + " = NULL", false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Zutatendaten_Sonstiges") + " SET " + DBKernel.delimitL("Wert") + " = NULL", false);
		
		// DoubleKennzahlen
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("DoubleKennzahlen") + " IF EXISTS", false);
		DBKernel.myList.getTable("DoubleKennzahlen").createTable();
		DBKernel.grantDefaults("DoubleKennzahlen");	
		
		refreshFKs("Matrices", false);
		refreshFKs("Krankheitsbilder", false);
		refreshFKs("Versuchsbedingungen", false);
		refreshFKs("Messwerte", false);
		refreshFKs("Versuchsbedingungen_Sonstiges", false);
		refreshFKs("Messwerte_Sonstiges", false);
		refreshFKs("Prozessdaten", false);
		refreshFKs("Prozessdaten_Sonstiges", false);
		refreshFKs("Prozessdaten_Messwerte", false);
		refreshFKs("Zutatendaten", false);
		refreshFKs("Zutatendaten_Sonstiges", false);		
	}
	private static void KZ2NKZ() {
		DBKernel.dontLog = true;
		prepareNewDBL();
		// Überführen der DBL in die neue Tabelle
		ResultSet rs = DBKernel.getResultSet("SELECT * FROM " + DBKernel.delimitL("Kennzahlen") +
				" ORDER BY " + DBKernel.delimitL("ID") + " ASC", false);
	    try {
		    if (rs != null && rs.first()) {
		    	LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();
		    	do {
		    		if (!rs.getString("Tabelle").equals("MatrixEigenschaften")) {
			    		String key = rs.getString("Tabelle") + "\t" + rs.getString("Spaltenname") + "\t" + rs.getInt("TabellenID");
			    		Object o = DBKernel.getValue(rs.getString("Tabelle"), "ID", rs.getString("TabellenID"), "ID");
			    		if (o != null) { // gibts den Datensatz überhaupt noch?
			    			//DBKernel.insertDBL(rs.getString("Tabelle"), rs.getString("Spaltenname"), rs.getString("TabellenID"), null, rs.getString("Kennzahl"), value);
				    		o = DBKernel.getValue(rs.getString("Tabelle"), "ID", rs.getString("TabellenID"), rs.getString("Spaltenname"));
				    		if (o == null) { // INSERT NEW
				  				PreparedStatement psmt = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("DoubleKennzahlen") +
					    				" (" + DBKernel.delimitL("Einzelwert") + ") VALUES (NULL)", Statement.RETURN_GENERATED_KEYS);
				  				if (psmt.executeUpdate() > 0) {
				  					o = DBKernel.getLastInsertedID(psmt);
				  					DBKernel.sendRequest("UPDATE " + DBKernel.delimitL(rs.getString("Tabelle")) + " SET " + DBKernel.delimitL(rs.getString("Spaltenname")) + "=" + o + " WHERE " + DBKernel.delimitL("ID") + "=" + rs.getString("TabellenID"), false);
				  				}
				    		}
				    		if (o == null) System.err.println("SHIIETEW...");
				    		else { // UPDATE
				    			String kz = rs.getString("Kennzahl");
				    			if (kz.indexOf("(?)") >= 0) kz = kz.replace("(?)", "(x)");
				    			if (kz.indexOf("Verteilung") >= 0 || kz.indexOf("Funktion (") >= 0) DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL(kz) + "='" + rs.getString("StrWert") + "'" + " WHERE " + DBKernel.delimitL("ID") + "=" + o, false);
				    			else DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL(kz) + "=" + rs.getDouble("Wert") + " WHERE " + DBKernel.delimitL("ID") + "=" + o, false);
				    			// geschätzt
			    				kz += "_g";
				    			if (rs.getBoolean("geschätzt")) {
							    	DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL(kz) + "=TRUE" + " WHERE " + DBKernel.delimitL("ID") + "=" + o, false);	    			
				    			}
				    			else {
							    	DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL(kz) + "=NULL" + " WHERE " + DBKernel.delimitL("ID") + "=" + o, false);	    				    				
				    			}
				    			if (rs.getString("StrWert") != null && rs.getString("StrWert").equals("n.d.")) DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL("Undefiniert (n.d.)") + "=TRUE" + " WHERE " + DBKernel.delimitL("ID") + "=" + o, false);	    			
				    			if (rs.getString("Bezugsgröße") != null) DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL("x") + "='" + rs.getString("Bezugsgröße") + "'" + " WHERE " + DBKernel.delimitL("ID") + "=" + o, false);	    			
				    		}
			    		}
		    		}
		    	} while (rs.next());
		    }
	    }
	    catch (Exception e) {MyLogger.handleException(e);}		
		DBKernel.dontLog = false;
	}
	public static void check4Updates_131_132(MyList myList) {
		// Literatur 
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Literatur") + " ALTER COLUMN " + DBKernel.delimitL("Titel") + " SET DATA TYPE VARCHAR(1023)", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Literatur") + " ALTER COLUMN " + DBKernel.delimitL("Volume") + " SET DATA TYPE VARCHAR(50)", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Literatur") + " ALTER COLUMN " + DBKernel.delimitL("Issue") + " SET DATA TYPE VARCHAR(50)", false);

		// Krankheitsbilder
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("Risikokategorie (CDC)") + " VARCHAR(10) BEFORE " + DBKernel.delimitL("Krankheit"), false);		
		updateChangeLog("Krankheitsbilder", 3, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("BioStoffV") + " VARCHAR(10) BEFORE " + DBKernel.delimitL("Krankheit"), false);		
		updateChangeLog("Krankheitsbilder", 4, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("LD50-Organismus") + " VARCHAR(100) BEFORE " + DBKernel.delimitL("Meldepflicht"), false);		
		updateChangeLog("Krankheitsbilder", 22, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("LD50-Aufnahmeroute") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Meldepflicht"), false);		
		updateChangeLog("Krankheitsbilder", 23, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("Letalitätsdosis100") + " DOUBLE BEFORE " + DBKernel.delimitL("Meldepflicht"), false);		
		updateChangeLog("Krankheitsbilder", 24, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("*10^l") + " DOUBLE BEFORE " + DBKernel.delimitL("Meldepflicht"), false);		
		updateChangeLog("Krankheitsbilder", 25, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("LD100-Einheit") + " VARCHAR(50) BEFORE " + DBKernel.delimitL("Meldepflicht"), false);		
		updateChangeLog("Krankheitsbilder", 26, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("LD100-Organismus") + " VARCHAR(100) BEFORE " + DBKernel.delimitL("Meldepflicht"), false);		
		updateChangeLog("Krankheitsbilder", 27, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("LD100-Aufnahmeroute") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Meldepflicht"), false);		
		updateChangeLog("Krankheitsbilder", 28, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("Letalität") + " DOUBLE BEFORE " + DBKernel.delimitL("Spätschäden"), false);		
		updateChangeLog("Krankheitsbilder", 32, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("<-Therapie(j/n)") + " INTEGER BEFORE " + DBKernel.delimitL("Spätschäden"), false);		
		updateChangeLog("Krankheitsbilder", 33, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("ansteckend") + " BOOLEAN BEFORE " + DBKernel.delimitL("Spätschäden"), false);		
		updateChangeLog("Krankheitsbilder", 34, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("Therapie") + " BOOLEAN BEFORE " + DBKernel.delimitL("Spätschäden"), false);		
		updateChangeLog("Krankheitsbilder", 35, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("Antidot") + " BOOLEAN BEFORE " + DBKernel.delimitL("Spätschäden"), false);		
		updateChangeLog("Krankheitsbilder", 36, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("Impfung") + " BOOLEAN BEFORE " + DBKernel.delimitL("Spätschäden"), false);		
		updateChangeLog("Krankheitsbilder", 37, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ADD COLUMN " + DBKernel.delimitL("Todeseintritt") + " VARCHAR(50) BEFORE " + DBKernel.delimitL("Spätschäden"), false);		
		updateChangeLog("Krankheitsbilder", 38, false);
		refreshFKs("Krankheitsbilder");
		DBKernel.doMNs(DBKernel.myList.getTable("Krankheitsbilder"));
		
		// Prozessdaten
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten") +
				" ADD COLUMN " + DBKernel.delimitL("Tenazität") + " INTEGER", false);		
		updateChangeLog("Prozessdaten", 18, false);
		refreshFKs("Prozessdaten");
		DBKernel.doMNs(DBKernel.myList.getTable("Prozessdaten"));
		
		// Prozessdaten_Messwerte
		DBKernel.myList.getTable("Prozessdaten_Messwerte").createTable();
		DBKernel.grantDefaults("Prozessdaten_Messwerte");

		// ComBaseImport
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("ComBaseImport") +
				" ALTER COLUMN " + DBKernel.delimitL("Erreger") + " RENAME TO " + DBKernel.delimitL("Agensname"), false);
		refreshFKs("ComBaseImport");
	}
	public static void check4Updates_130_131(MyList myList) {
		
		// Aufbereitungsverfahren
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Aufbereitungsverfahren") +
				" ADD COLUMN " + DBKernel.delimitL("Homogenisierung") + " BOOLEAN BEFORE " + DBKernel.delimitL("Matrix"), false);		
		updateChangeLog("Aufbereitungsverfahren", 8, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Aufbereitungsverfahren") +
				" ADD COLUMN " + DBKernel.delimitL("Zelllyse") + " BOOLEAN BEFORE " + DBKernel.delimitL("Matrix"), false);		
		updateChangeLog("Aufbereitungsverfahren", 9, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Aufbereitungsverfahren") +
				" ADD COLUMN " + DBKernel.delimitL("Spezialequipment") + " BOOLEAN BEFORE " + DBKernel.delimitL("Referenz"), false);		
		updateChangeLog("Aufbereitungsverfahren", 23, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Aufbereitungsverfahren") +
				" ADD COLUMN " + DBKernel.delimitL("Laienpersonal") + " BOOLEAN BEFORE " + DBKernel.delimitL("Referenz"), false);		
		updateChangeLog("Aufbereitungsverfahren", 24, false);
		refreshFKs("Aufbereitungsverfahren");
		DBKernel.doMNs(DBKernel.myList.getTable("Aufbereitungsverfahren"));

		// Nachweisverfahren
		
		// Hammerl hatte in seinem Kram noch Kurzbezeichnung, Wissenschaftliche Bezeichnung drin
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Nachweisverfahren") + " DROP COLUMN " + DBKernel.delimitL("Kurzbezeichnung"), true);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Nachweisverfahren") + " DROP COLUMN " + DBKernel.delimitL("Wissenschaftliche Bezeichnung"), true);
		updateChangeLog("Nachweisverfahren", 2, true, 21);
		updateChangeLog("Nachweisverfahren", 2, true, 21);			
		
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Nachweisverfahren") +
				" ADD COLUMN " + DBKernel.delimitL("Matrix") + " INTEGER BEFORE " + DBKernel.delimitL("Agens"), false);
		updateChangeLog("Nachweisverfahren", 6, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Nachweisverfahren") +
				" ADD COLUMN " + DBKernel.delimitL("MatrixDetail") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Agens"), false);		
		updateChangeLog("Nachweisverfahren", 7, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Nachweisverfahren") +
				" ADD COLUMN " + DBKernel.delimitL("Spezialequipment") + " BOOLEAN BEFORE " + DBKernel.delimitL("Referenz"), false);		
		updateChangeLog("Nachweisverfahren", 19, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Nachweisverfahren") +
				" ADD COLUMN " + DBKernel.delimitL("Laienpersonal") + " BOOLEAN BEFORE " + DBKernel.delimitL("Referenz"), false);		
		updateChangeLog("Nachweisverfahren", 20, false);
		refreshFKs("Nachweisverfahren");
		DBKernel.doMNs(DBKernel.myList.getTable("Nachweisverfahren"));

		// Aufbereitungs_Nachweisverfahren
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Aufbereitungs_Nachweisverfahren") +
				" ADD COLUMN " + DBKernel.delimitL("Wiederfindungsrate") + " DOUBLE BEFORE " + DBKernel.delimitL("Referenz"), false);		
		updateChangeLog("Aufbereitungs_Nachweisverfahren", 9, false);
		refreshFKs("Aufbereitungs_Nachweisverfahren");
		DBKernel.doMNs(DBKernel.myList.getTable("Aufbereitungs_Nachweisverfahren"));

		// Literatur 
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Literatur") + " ALTER COLUMN " + DBKernel.delimitL("Titel") + " SET DATA TYPE VARCHAR(1023)", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Literatur") + " ALTER COLUMN " + DBKernel.delimitL("Volume") + " SET DATA TYPE VARCHAR(50)", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Literatur") + " ALTER COLUMN " + DBKernel.delimitL("Issue") + " SET DATA TYPE VARCHAR(50)", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Literatur") +
				" ADD COLUMN " + DBKernel.delimitL("Webseite") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Paper"), false);
		updateChangeLog("Literatur", 10, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Literatur") +
				" ADD COLUMN " + DBKernel.delimitL("Literaturtyp") + " INTEGER BEFORE " + DBKernel.delimitL("Paper"), false);		
		updateChangeLog("Literatur", 11, false);
		refreshFKs("Literatur");
		DBKernel.doMNs(DBKernel.myList.getTable("Literatur"));
	}
	public static void check4Updates_129_130(MyList myList) {
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Lieferung_Lieferungen") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Artikel_Lieferung") + " IF EXISTS", false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Produzent_Artikel") + " IF EXISTS", false);
		// Produzent_Artikel
		DBKernel.myList.getTable("Produzent_Artikel").createTable();
		// Artikel_Lieferung
		DBKernel.myList.getTable("Artikel_Lieferung").createTable();
		// Lieferung_Lieferungen
		DBKernel.myList.getTable("Lieferung_Lieferungen").createTable();
		DBKernel.grantDefaults("Produzent_Artikel");
		DBKernel.grantDefaults("Artikel_Lieferung");
		DBKernel.grantDefaults("Lieferung_Lieferungen");
		// Kontakte 
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kontakte") + " ALTER COLUMN " + DBKernel.delimitL("Straße") + " SET DATA TYPE VARCHAR(255)", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kontakte") + " ALTER COLUMN " + DBKernel.delimitL("Name") + " SET DATA TYPE VARCHAR(255)", false);
	}
	public static void check4Updates_128_129(MyList myList) {
		// Jans Update - Start
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Modelle_Matrix_Verbund"), false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Modelle"), false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Länder"), false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Resistenzen"), false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("MethodenSoftware"), false);
		myList.getTable("Länder").createTable();
		myList.getTable("Resistenz").createTable();
		myList.getTable("Verwendung").createTable();
		myList.getTable("Methoden_Software").createTable();
		myList.getTable("Preharvest").createTable();
		myList.getTable("Harvest").createTable();
		myList.getTable("Produkt").createTable();
		myList.getTable("Transport").createTable();
		myList.getTable("Exposition").createTable();
		myList.getTable("Risikocharakterisierung").createTable();
		myList.getTable("Modell").createTable();
		myList.getTable("Modell_Verwendung_Verbund").createTable();
		myList.getTable("Modell_Agenzien_Verbund").createTable();
		myList.getTable("Modell_Resistenz_Verbund").createTable();
		myList.getTable("Modell_Software_Verbund").createTable();		
		myList.getTable("Modell_Preharvest_Verbund").createTable();
		myList.getTable("Modell_Harvest_Verbund").createTable();
		myList.getTable("Modell_Zwischenprodukt_Verbund").createTable();
		myList.getTable("Modell_Einzelhandelsprodukt_Verbund").createTable();
		myList.getTable("Modell_Transport_Verbund").createTable();
		myList.getTable("Modell_Exposition_Verbund").createTable();
		myList.getTable("Modell_Risikocharakterisierung_Verbund").createTable();
		doJansGrants();
		new GeneralXLSImporter().doImport("/org/hsh/bfr/db/res/Modell_Tabellen.xls", DBKernel.mainFrame.getProgressBar(), false);
		// Jans Update - Ende
			
		// ToxinUrsprung
		DBKernel.myList.getTable("ToxinUrsprung").createTable();
		DBKernel.grantDefaults("ToxinUrsprung");
		DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("ToxinUrsprung") + " (" + DBKernel.delimitL("Ursprung") + ") VALUES ('Bakterium')", false);
		DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("ToxinUrsprung") + " (" + DBKernel.delimitL("Ursprung") + ") VALUES ('Pflanzensamen')", false);
		// Agenzien
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ADD COLUMN " + DBKernel.delimitL("Gramfärbung") + " INTEGER BEFORE " + DBKernel.delimitL("CAS-Nummer"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ADD COLUMN " + DBKernel.delimitL("Ursprung") + " INTEGER BEFORE " + DBKernel.delimitL("Gramfärbung"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ADD COLUMN " + DBKernel.delimitL("Humanpathogen") + " INTEGER BEFORE " + DBKernel.delimitL("Ursprung"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ADD COLUMN " + DBKernel.delimitL("Risikogruppe") + " INTEGER BEFORE " + DBKernel.delimitL("Humanpathogen"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ALTER COLUMN " + DBKernel.delimitL("Subspezies") + " RENAME TO " + DBKernel.delimitL("Subspezies/Subtyp"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ADD COLUMN " + DBKernel.delimitL("Familie") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Gattung"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ADD COLUMN " + DBKernel.delimitL("Klassifizierung") + " INTEGER BEFORE " + DBKernel.delimitL("Familie"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ADD COLUMN " + DBKernel.delimitL("Wissenschaftliche Bezeichnung") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Klassifizierung"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" DROP COLUMN " + DBKernel.delimitL("Erreger (engl.)"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ALTER COLUMN " + DBKernel.delimitL("Erreger") + " RENAME TO " + DBKernel.delimitL("Agensname"), false);
		refreshFKs("Agenzien");
		DBKernel.doMNs(DBKernel.myList.getTable("Agenzien"));
		
		// Methodiken
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Methodiken") +
				" ADD COLUMN " + DBKernel.delimitL("Beschreibung") + " VARCHAR(255)", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Methodiken") +
				" ADD COLUMN " + DBKernel.delimitL("Kurzbezeichnung") + " VARCHAR(30)", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Methodiken") +
				" ADD COLUMN " + DBKernel.delimitL("Wissenschaftliche Bezeichnung") + " VARCHAR(255)", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Methodiken") +
				" ADD COLUMN " + DBKernel.delimitL("Katalogcodes") + " INTEGER", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Methodiken") +
				" ADD COLUMN " + DBKernel.delimitL("Kommentar") + " VARCHAR(1023)", false);
		myList.getTable("Codes-Methodiken").createTable();
		DBKernel.grantDefaults("Codes-Methodiken");
		refreshFKs("Methodiken");
		new GeneralXLSImporter().doImport("/org/hsh/bfr/db/res/Methodiken.xls", DBKernel.mainFrame.getProgressBar(), false);
		
		// Aufbereitungsverfahren & Co
		refreshFKs("Versuchsbedingungen", true);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Aufbereitungsverfahren_Kits"), false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Nachweisverfahren_Kits"), false);

		dropTriggers("Labor_Agens_Nachweisverfahren_Matrix_Aufbereitungsverfahren");
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Labor_Agens_Nachweisverfahren_Matrix_Aufbereitungsverfahren"), false);
		dropTriggers("Agens_Nachweisverfahren_Matrix_Aufbereitungsverfahren");
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Agens_Nachweisverfahren_Matrix_Aufbereitungsverfahren"), false);
		dropTriggers("Matrix_Aufbereitungsverfahren");
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Matrix_Aufbereitungsverfahren"), false);
		dropTriggers("Aufbereitungsverfahren");
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Aufbereitungsverfahren"), false);

		myList.getTable("Aufbereitungsverfahren").createTable();
		DBKernel.grantDefaults("Aufbereitungsverfahren");
		myList.getTable("Aufbereitungsverfahren_Normen").createTable();
		DBKernel.grantDefaults("Aufbereitungsverfahren_Normen");

		// Nachweisverfahren
		dropTriggers("Agens_Nachweisverfahren");
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Agens_Nachweisverfahren"), false);
		dropTriggers("Nachweisverfahren");
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("Nachweisverfahren"), false);

		myList.getTable("Nachweisverfahren").createTable();
		DBKernel.grantDefaults("Nachweisverfahren");
		myList.getTable("Nachweisverfahren_Normen").createTable();
		DBKernel.grantDefaults("Nachweisverfahren_Normen");

		// Agens_Nachweisverfahren_Matrix_Aufbereitungsverfahren		
		myList.getTable("Aufbereitungs_Nachweisverfahren").createTable();
		DBKernel.grantDefaults("Aufbereitungs_Nachweisverfahren");
		myList.getTable("Labor_Aufbereitungs_Nachweisverfahren").createTable();
		DBKernel.grantDefaults("Labor_Aufbereitungs_Nachweisverfahren");
		
		refreshFKs("Versuchsbedingungen");
		myList.getTable("Aufbereitungsverfahren_Kits").createTable();
		DBKernel.grantDefaults("Aufbereitungsverfahren_Kits");
		myList.getTable("Nachweisverfahren_Kits").createTable();
		DBKernel.grantDefaults("Nachweisverfahren_Kits");
		
		// Krankheitsbilder
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Krankheitsbilder") +
				" ALTER COLUMN " + DBKernel.delimitL("Inzidenz Alter") + " SET DATA TYPE DOUBLE", false);

		// Kontakte
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kontakte") +
				" ALTER COLUMN " + DBKernel.delimitL("Name") + " SET DATA TYPE VARCHAR(255)", false);

		// Codes-Methoden
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Codes-Methoden") +
				" ALTER COLUMN " + DBKernel.delimitL("Code") + " SET DATA TYPE VARCHAR(40)", false);
	}
	public static void check4Updates_127_128() {	
		// Matrices
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Matrices") +
				" ADD COLUMN " + DBKernel.delimitL("Katalogcodes") + " INTEGER BEFORE " + DBKernel.delimitL("Kommentar"), false);
		refreshFKs("Matrices");
		DBKernel.doMNs(DBKernel.myList.getTable("Matrices"));
		// Agenzien
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Agenzien") +
				" ADD COLUMN " + DBKernel.delimitL("Katalogcodes") + " INTEGER BEFORE " + DBKernel.delimitL("Kommentar"), false);
		refreshFKs("Agenzien");
		DBKernel.doMNs(DBKernel.myList.getTable("Agenzien"));
		// Methoden
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Methoden") +
				" ALTER COLUMN " + DBKernel.delimitL("Name") + " SET DATA TYPE VARCHAR(1023)", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Methoden") +
				" ADD COLUMN " + DBKernel.delimitL("Katalogcodes") + " INTEGER BEFORE " + DBKernel.delimitL("Kommentar"), false);
		refreshFKs("Methoden");
		DBKernel.doMNs(DBKernel.myList.getTable("Methoden"));
		
		// Prozess-Workflow
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozess-Workflow") +
				" ADD COLUMN " + DBKernel.delimitL("Produktmatrix") + " INTEGER BEFORE " + DBKernel.delimitL("XML"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozess-Workflow") +
				" ADD COLUMN " + DBKernel.delimitL("EAN") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("XML"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozess-Workflow") +
				" ADD COLUMN " + DBKernel.delimitL("Prozessdaten") + " INTEGER BEFORE " + DBKernel.delimitL("XML"), false);
		refreshFKs("Prozess-Workflow");

		// Versuchsbedingungen
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Versuchsbedingungen") +
				" ADD COLUMN " + DBKernel.delimitL("EAN") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("MatrixDetail"), false);
		updateChangeLog("Versuchsbedingungen", 5, false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Versuchsbedingungen") +
				" ADD COLUMN " + DBKernel.delimitL("Nachweisverfahren") + " INTEGER BEFORE " + DBKernel.delimitL("FreigabeModus"), false);
		updateChangeLog("Versuchsbedingungen", 15, false);
		refreshFKs("Versuchsbedingungen");

		// Kennzahlen
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Kennzahlen") +
				" ALTER COLUMN " + DBKernel.delimitL("StrWert") + " SET DATA TYPE VARCHAR(1023)", false);
		
		// Prozessdaten
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten") +
				" ALTER COLUMN " + DBKernel.delimitL("Endtemperatur") + " RENAME TO " + DBKernel.delimitL("Temperatur"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Prozessdaten") +
				" ALTER COLUMN " + DBKernel.delimitL("Enddruck") + " RENAME TO " + DBKernel.delimitL("Druck"), false);

		// Betrieb_Matrix_Produktion
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Betrieb_Matrix_Produktion") +
				" ADD COLUMN " + DBKernel.delimitL("EAN") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Produktionsmenge"), false);
		refreshFKs("Betrieb_Matrix_Produktion");
		
		// Zutatendaten_Sonstiges
		DBKernel.myList.getTable("Zutatendaten_Sonstiges").createTable();
		DBKernel.grantDefaults("Zutatendaten_Sonstiges");
		
		// Infotabelle
		DBKernel.myList.getTable("Infotabelle").createTable();
		DBKernel.grantDefaults("Infotabelle");
		DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("Infotabelle") +
				" (" + DBKernel.delimitL("Parameter") + "," + DBKernel.delimitL("Wert") + ") VALUES ('DBVersion','1.2.8')", false);

		// Zutatendaten
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ALTER COLUMN " + DBKernel.delimitL("#Units") + " SET DATA TYPE DOUBLE", false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ADD COLUMN " + DBKernel.delimitL("EAN") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Kommentar"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ADD COLUMN " + DBKernel.delimitL("MatrixDetail") + " VARCHAR(255) BEFORE " + DBKernel.delimitL("Kommentar"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ADD COLUMN " + DBKernel.delimitL("Temperatur") + " DOUBLE BEFORE " + DBKernel.delimitL("Kommentar"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ADD COLUMN " + DBKernel.delimitL("pH") + " DOUBLE BEFORE " + DBKernel.delimitL("Kommentar"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ADD COLUMN " + DBKernel.delimitL("aw") + " DOUBLE BEFORE " + DBKernel.delimitL("Kommentar"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ADD COLUMN " + DBKernel.delimitL("CO2") + " DOUBLE BEFORE " + DBKernel.delimitL("Kommentar"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ADD COLUMN " + DBKernel.delimitL("Druck") + " DOUBLE BEFORE " + DBKernel.delimitL("Kommentar"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ADD COLUMN " + DBKernel.delimitL("Sonstiges") + " INTEGER BEFORE " + DBKernel.delimitL("Kommentar"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" DROP COLUMN " + DBKernel.delimitL("Gütescore (subj.)"), false);
		// fehlt noch: Vorprozess <-> Matrix
		refreshFKs("Zutatendaten", true);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ADD COLUMN " + DBKernel.delimitL("mtrx") + " INTEGER BEFORE " + DBKernel.delimitL("EAN"), false);
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Zutatendaten") +
				" SET " + DBKernel.delimitL("mtrx") + " = " + DBKernel.delimitL("Matrix"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" DROP COLUMN " + DBKernel.delimitL("Matrix"), false);
		DBKernel.sendRequest("ALTER TABLE " + DBKernel.delimitL("Zutatendaten") +
				" ALTER COLUMN " + DBKernel.delimitL("mtrx") + " RENAME TO " + DBKernel.delimitL("Matrix"), false);
		refreshFKs("Zutatendaten");
		
		dropTriggers("MatrixEigenschaften");
		dropTriggers("MatrixEigenschaften_Sonstiges");
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("MatrixEigenschaften_Sonstiges"), false);
		DBKernel.sendRequest("DROP TABLE " + DBKernel.delimitL("MatrixEigenschaften"), false);
		
		// Users
		try {    	
	    	PreparedStatement ps = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("Users") +
						" (" + DBKernel.delimitL("Username") + "," + DBKernel.delimitL("Vorname") + "," + DBKernel.delimitL("Name") + "," + DBKernel.delimitL("Zugriffsrecht") + ") VALUES (?,?,?,?)");
			ps.setString(1, "wese"); ps.setString(2, "Anne-Kathrin"); ps.setString(3, "Wese"); ps.setInt(4, Users.SUPER_WRITE_ACCESS); ps.execute();
			ps.setString(1, "schielke"); ps.setString(2, "Anika"); ps.setString(3, "Schielke"); ps.setInt(4, Users.SUPER_WRITE_ACCESS); ps.execute();
			ps.setString(1, "thiele"); ps.setString(2, "Holger"); ps.setString(3, "Thiele"); ps.setInt(4, Users.SUPER_WRITE_ACCESS); ps.execute();
			ps.setString(1, "burchardi"); ps.setString(2, "Henrike"); ps.setString(3, "Burchardi"); ps.setInt(4, Users.SUPER_WRITE_ACCESS); ps.execute();
	    } 
	    catch (Exception e) {MyLogger.handleException(e);}
	    
	}
	*/
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
	      for (String sql : DBKernel.myList.getTable(tableName).getIndexSQL()) {
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
