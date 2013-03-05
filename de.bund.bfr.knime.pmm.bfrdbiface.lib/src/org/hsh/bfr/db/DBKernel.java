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

import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.prefs.Preferences;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.undo.UndoableEditSupport;

import org.eclipse.core.runtime.FileLocator;
import org.hsh.bfr.db.gui.Login;
import org.hsh.bfr.db.gui.MainFrame;
import org.hsh.bfr.db.gui.MyList;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;
import org.hsh.bfr.db.gui.dbtable.undoredo.BfRUndoManager;
import org.hsh.bfr.db.gui.dbtable.undoredo.TableCellEdit;
import org.hsh.bfr.db.gui.dbtree.MyDBTree;
import org.hsh.bfr.db.imports.InfoBox;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * @author Armin
 *
 */
public class DBKernel {

	/**
	 * @param args
	 */
	
	public final static String HSH_PATH = System.getProperty("user.home") + System.getProperty("file.separator") + ".localHSH" + System.getProperty("file.separator") + "BfR" + System.getProperty("file.separator");
	public static String HSHDB_PATH = HSH_PATH + "DBs" + System.getProperty("file.separator");

	private static Connection localConn = null;
	private static String m_Username = "";
	private static String m_Password = "";
	
	public static BfRUndoManager undoManager = new BfRUndoManager();
	private static UndoableEditSupport undoSupport = new UndoableEditSupport();
	private static TableCellEdit lastCellEdit = null;
	public static long undoredoStart = 0;
	public static boolean importing = false;
	public static boolean dontLog = false;
	
	public static Preferences prefs = Preferences.userNodeForPackage(Login.class);
	public static MyList myList = null;
	public static MyDBTable topTable = null;
	public static MainFrame mainFrame = null;
	public static Login login = null;
	public static boolean passFalse = false;
	public static MyTable users = null;
	public static MyTable changeLog = null;
	public static MyTable blobSpeicher = null;
	public static long tempROZeit = 0;
	public static long triggerFired = System.currentTimeMillis();
	public static boolean scrolling = false;
	public static boolean isServerConnection = false;
	public static boolean isKNIME = false;

	private static LinkedHashMap<Object, LinkedHashMap<Object, String>> filledHashtables = new LinkedHashMap<Object, LinkedHashMap<Object, String>>();
	public static LinkedHashMap<Object, String> hashBundesland = new LinkedHashMap<Object, String>();
	public static LinkedHashMap<Object, String> hashModelType = new LinkedHashMap<Object, String>();

	public static String DBVersion = "1.5.9";
	public static boolean debug = true;
	public static boolean isKrise = false;
	
	public static String getTempSA(String dbPath, boolean other) {
		String sa = DBKernel.prefs.get("DBADMINUSER" + getCRC32(dbPath),"00");
		if (sa.equals("00")) {
			//if (debug) return "SA";
			if (other) sa = isKNIME || isKrise ? "defad": "SA";		
			else sa = isKNIME || isKrise ? "SA" : "defad";
		}
		
		return sa;
	}
	public static String getTempSAPass(String dbPath, boolean other) {
		String pass = DBKernel.prefs.get("DBADMINPASS" + getCRC32(dbPath),"00");
		if (pass.equals("00")) {
			//if (debug) return "";
			if (isServerConnection && isKrise) return "de6!§5ddy";
				
			if (other) pass = isKNIME || isKrise ? "de6!§5ddy" : "";
			else pass = isKNIME || isKrise ? "" : "de6!§5ddy";
		}
		
		return pass;
	}
	public static String getTempSA(String dbPath) {
		return getTempSA(dbPath, false);
	}
	public static String getTempSAPass(String dbPath) {
		return getTempSAPass(dbPath, false);
	}
	public static String getLanguage() {
		return isKrise || !isKNIME ? "de" : "en";
	}
	  public static boolean saveUP2PrefsTEMP(String dbPath) {
		  return saveUP2PrefsTEMP(dbPath, false);
	  }
	  public static boolean saveUP2PrefsTEMP(String dbPath, boolean onlyCheck) {
		  boolean result = false;
			String sa = DBKernel.prefs.get("DBADMINUSER" + getCRC32(dbPath),"00");
			String pass = DBKernel.prefs.get("DBADMINPASS" + getCRC32(dbPath),"00");
			if (onlyCheck || sa.equals("00") || pass.equals("00")) {
		  		DBKernel.closeDBConnections(false);
		  		
		  		try {
			  		sa = getTempSA(dbPath);
			  		pass = getTempSAPass(dbPath);
			  		Connection conn = getDBConnection(dbPath, sa, pass, false);
			  		if (conn != null && !isAdmin(conn, sa)) {conn.close(); conn = null;}
			  		if (!onlyCheck) {
				  		if (conn == null) {
				  			sa = getTempSA(dbPath, true);
				  			conn = getDBConnection(dbPath, sa, pass, false);
					  		if (conn != null && !isAdmin(conn, sa)) {conn.close(); conn = null;}
				  		}
				  		if (conn == null) {
				  			pass = getTempSAPass(dbPath, true);
				  			conn = getDBConnection(dbPath, sa, pass, false);
					  		if (conn != null && !isAdmin(conn, sa)) {conn.close(); conn = null;}
				  		}
				  		if (conn == null) {
				  			sa = getTempSA(dbPath, false);
				  			conn = getDBConnection(dbPath, sa, pass, false);
					  		if (conn != null && !isAdmin(conn, sa)) {conn.close(); conn = null;}
				  		}
			  		}
			  		if (conn == null) System.err.println("save Pass to Prefs failed...");
			  		else {
			  			if (!onlyCheck) {
							DBKernel.prefs.put("DBADMINUSER" + getCRC32(dbPath), sa);
							DBKernel.prefs.put("DBADMINPASS" + getCRC32(dbPath), pass);			  				
			  			}
						result = true;
						//System.err.println("pass combi is: " + sa + "\t" + pass);
			  		}
		  		}
		  		catch(Exception e) {e.printStackTrace();}
				
		  		try {
			  		DBKernel.closeDBConnections(false);
			  		DBKernel.getDBConnection();
			  		if (DBKernel.myList != null && DBKernel.myList.getMyDBTable() != null) {
			  			DBKernel.myList.getMyDBTable().setConnection(DBKernel.getDBConnection());
			  		}				
		  		}
		  		catch(Exception e) {}
			}
			return result;
	  }
	  public static long getCRC32(String str) {
		  if (str == null) return 0;
		  Checksum checksum = new CRC32();
		  byte b[] = str.getBytes();
		  checksum.update(b,0,b.length);
		  return checksum.getValue();
	  }
	protected static boolean insertIntoChangeLog(final String tablename, final Object[] rowBefore, final Object[] rowAfter) {
		return insertIntoChangeLog(tablename, rowBefore, rowAfter, localConn, false);
	}
	protected static boolean insertIntoChangeLog(final String tablename, final Object[] rowBefore, final Object[] rowAfter, final Connection conn) {
		return insertIntoChangeLog(tablename, rowBefore, rowAfter, conn, false);
	}
	private static boolean different(final Object[] rowBefore, final Object[] rowAfter) {
		if (rowBefore == null && rowAfter == null) {
			return false;
		}
		if (rowBefore == null && rowAfter != null || rowBefore != null && rowAfter == null) {
			return true;
		}
		if (rowBefore.equals(rowAfter)) {
			return false;
		}
		for (int i=0;i<rowBefore.length;i++) {
			if (rowBefore[i] == null && rowAfter[i] == null) {
				;
			}
			else if (rowBefore[i] == null && rowAfter[i] != null || 
					rowAfter[i] == null && rowBefore[i] != null || 
					!rowBefore[i].toString().equals(rowAfter[i].toString())) {
				return true;
			}
		}
		return false;
	}
	/*
	private static Integer getNextChangeLogID(final Connection conn) {
		Integer result = null;
	    try {
	    	Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    	ResultSet rs = stmt.executeQuery("SELECT MAX(" + DBKernel.delimitL("ID") + ") FROM " + DBKernel.delimitL("ChangeLog"));
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
	*/
	private static Integer callIdentity(final Connection conn) {
		Integer result = null;
	    try {
	    	Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    	ResultSet rs = stmt.executeQuery("CALL IDENTITY()");
	    	if (rs != null && rs.first()) {
		    	result = rs.getInt(1);
		    	rs.close();
	    	}
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
		return result;
	}
	
	protected static boolean insertIntoChangeLog(final String tablename, final Object[] rowBefore, final Object[] rowAfter, final Connection conn, final boolean suppressWarnings) {
		if (dontLog) {
			return true;
		}
		else {
			boolean diff = different(rowBefore, rowAfter); 
		    if (debug && !DBKernel.importing) {
		    	MyLogger.handleMessage("different: " + diff);
		    	if (diff) {
			    	System.err.println(eintragAlt2String(rowBefore));
			    	System.err.println(eintragAlt2String(rowAfter));
		    	}
		    }
		    if (!diff) {
				return true;
			}
		    boolean result = false;
		    try {
		    	//getDBConnection();
		    	String username = DBKernel.getUsername();
		    	PreparedStatement ps = conn.prepareStatement("INSERT INTO " + DBKernel.delimitL("ChangeLog") +
		      		" (" + DBKernel.delimitL("ID") + ", " + DBKernel.delimitL("Zeitstempel") + ", " + DBKernel.delimitL("Username") + ", " +
		      		DBKernel.delimitL("Tabelle") + ", " + DBKernel.delimitL("TabellenID") + ", " +
		      		DBKernel.delimitL("Alteintrag") + ") VALUES (NEXT VALUE FOR " + DBKernel.delimitL("ChangeLogSEQ") + ", ?, ?, ?, ?, ?)");

		    	ps.setTimestamp(1, new Timestamp(new Date().getTime()));
		    	ps.setString(2, username);
		    	ps.setString(3, tablename);
		    	int tableID;
		    	if (rowBefore != null && rowBefore.length > 0 && rowBefore[0] != null && rowBefore[0] instanceof Integer) {
					tableID = (Integer) rowBefore[0];
				} else if (rowAfter != null && rowAfter.length > 0 && rowAfter[0] != null && rowAfter[0] instanceof Integer) {
					tableID = (Integer) rowAfter[0];
				} else {
					tableID = -1;
				}
		    	ps.setInt(4, tableID);
		    	//System.err.println(eintragAlt2String(rowBefore));
		    	check4SerializationProblems(rowBefore);
		    	ps.setObject(5, rowBefore);
		    	triggerFired = System.currentTimeMillis();
		    	ps.execute();
		    	
		    	if (debug && !DBKernel.importing) {
		    		System.err.println("callIdentity: " + callIdentity(conn));
		    		//System.err.println("getLastInsertedID: " + getLastInsertedID(ps));
		    	}
		
		    	if (!importing && System.currentTimeMillis() - undoredoStart >= 50) {// Falls ein Foreign Key gelöscht wird, das dauert in der Regel 0 ms, aber wir gehen einfach mal auf Nummer sicher
		  			TableCellEdit cellEdit = new TableCellEdit(tablename, rowBefore, rowAfter);
		    		if (lastCellEdit != null && System.currentTimeMillis() - lastCellEdit.getEditTime() < 50 && !lastCellEdit.getTableName().equals(tablename)) { // Hier wurde wohl ein Foreign Key gelöscht. !lastCellEdit.getTableName().equals(tablename) ist notwendig, ansonsten werden hier auch 2 Datensätze derselben Datenbank gleichzeitig upgedatet (z.B. save des aktuell selektierten Datensatzes und Einfügen eines neuen Datensatzes).
		    			lastCellEdit.addForeignDelete(cellEdit);
			  			if (debug) {
							MyLogger.handleMessage("Foreign\t" + tableID + "\t" + lastCellEdit.getEditTime() + "\t" + tablename);
						}    		  
			    	}
			    	else {
						undoSupport.postEdit(cellEdit);  
						lastCellEdit = cellEdit;
						if (debug) {
							MyLogger.handleMessage("New\t" + tableID + "\t" + cellEdit.getEditTime() + "\t" + tablename);
						}    		  
			    	}
			    }
			
				result = true;
		    }
		    catch (Exception e) {
		    	if (!suppressWarnings) {
		    		MyLogger.handleMessage(tablename + ": " + eintragAlt2String(rowBefore) + "\t" + eintragAlt2String(rowAfter));
		    		MyLogger.handleException(e, true);
		    	}
		    }
		    return result;
		}
	}
	private static void check4SerializationProblems(final Object[] rowBefore) {
		if (rowBefore == null) {
			return;
		}
		for (int i=0;i<rowBefore.length;i++) {
			if (rowBefore[i] instanceof org.hsqldb.types.TimestampData) {
				rowBefore[i] = ((org.hsqldb.types.TimestampData) rowBefore[i]).getSeconds();
				//Long d = (Long) rowBefore[i];
				//System.err.println(d + "\t" + rowBefore[i]);
			}
		}
	}
	private static String eintragAlt2String(final Object[] eintragAlt) {
		if (eintragAlt == null) {
			return null;
		}
		String result = eintragAlt[0].toString();
		for (int i=1;i<eintragAlt.length;i++) {
			result += "," + eintragAlt[i];
		}
		return result;
	}
	public static Integer getLastInsertedID(final PreparedStatement psmt) {
		Integer lastInsertedID = null;
		try {
		  	ResultSet rs = psmt.getGeneratedKeys();
		    if (rs.next()) {
		    	lastInsertedID = rs.getInt(1);
		    }
		    else {
		    	System.err.println("getGeneratedKeys failed!\n" + psmt);
		    }
		    rs.close();			
		}
		catch (Exception e) {}
		return lastInsertedID;
	}
	
  protected static void createTable(final String tableName, final String fieldDefs, final List<String> indexSQL) {
  	createTable(tableName, fieldDefs, indexSQL, true, false);
  }
  
  protected static void createTable(final String tableName, final String fieldDefs, final List<String> indexSQL, final boolean cached, final boolean suppressWarnings) {
    try {
    	getDBConnection();
        if (tableName.equals("ChangeLog")) { //  || tableName.equals("DateiSpeicher") || tableName.equals("Infotabelle")
    		DBKernel.sendRequest("CREATE SEQUENCE " + DBKernel.delimitL(tableName + "SEQ") + " AS INTEGER START WITH 1 INCREMENT BY 1", false);
    		DBKernel.sendRequest("GRANT USAGE ON SEQUENCE " + DBKernel.delimitL("ChangeLogSEQ") + " TO " + DBKernel.delimitL("PUBLIC"), false);
        }

      Statement stmt = localConn.createStatement(); // ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY ResultSet.CONCUR_UPDATABLE
      //stmt.execute("DROP TABLE " + delimitL(tableName) + " IF EXISTS;");
      String sqlc = "CREATE " + (cached ? "CACHED" : "MEMORY") + " TABLE " + delimitL(tableName) + " (" + fieldDefs + ");";
      stmt.execute(sqlc);
      //System.out.println(sqlc);
      for (String sql : indexSQL) {
      	if (sql.length() > 0) {
      		//System.out.println(sql);
      		stmt.execute(sql);
      	}
      }
      if (!tableName.equals("ChangeLog") && !tableName.equals("DateiSpeicher") && !tableName.equals("Infotabelle")) {
        stmt.execute("CREATE TRIGGER " + delimitL("A_" + tableName + "_U") + " AFTER UPDATE ON " +
        		delimitL(tableName) + " FOR EACH ROW " + " CALL " + delimitL(new MyTrigger().getClass().getName())); // (oneThread ? "QUEUE 0" : "") +    
        stmt.execute("CREATE TRIGGER " + delimitL("A_" + tableName + "_D") + " AFTER DELETE ON " +
        		delimitL(tableName) + " FOR EACH ROW " + " CALL " + delimitL(new MyTrigger().getClass().getName())); // (oneThread ? "QUEUE 0" : "") +    
        stmt.execute("CREATE TRIGGER " + delimitL("A_" + tableName + "_I") + " AFTER INSERT ON " +
        		delimitL(tableName) + " FOR EACH ROW " + " CALL " + delimitL(new MyTrigger().getClass().getName())); // (oneThread ? "QUEUE 0" : "") +    			
		if (tableName.equals("Modellkatalog") || tableName.equals("ModellkatalogParameter") || tableName.equals("Modell_Referenz")) {
			/*
 				|| tableName.equals("GeschaetzteModelle")
				|| tableName.equals("GeschaetztesModell_Referenz") || tableName.equals("GeschaetzteParameter")
				|| tableName.equals("GeschaetzteParameterCovCor") || tableName.equals("Sekundaermodelle_Primaermodelle")
				 || tableName.equals("Literatur")
			 */
			// der INSERT TRIGGER verursacht leider Probleme, weil data-in-motion die neu generierte ID mittels CALL IDENTITY() abruft
			// das liefert aber die in ChangeLog eingetragene ID zurück und nicht die in der gewünschten Tabelle
			// muss also erst mal ohne gehen...
			// bei Literatur auch problematisch... vielleicht sollten wir den Literaturview verstecken?
		}
		else {
			/*
	        stmt.execute("CREATE TRIGGER " + delimitL("A_" + tableName + "_I") + " AFTER INSERT ON " +
	        		delimitL(tableName) + " FOR EACH ROW " + " CALL " + delimitL(new MyTrigger().getClass().getName())); // (oneThread ? "QUEUE 0" : "") +
	        		*/    			
		}
      }
      stmt.close();
    }
    catch (Exception e) {
      if (!suppressWarnings) MyLogger.handleException(e);
    }
  }
  
  public static String getPassword() {
	  return m_Password;
  }
  public static String getUsername() {
  	String username = DBKernel.m_Username;
	try { // im Servermodus muss ich schon abchecken, welcher User eingeloggt ist!
		Connection lconn = getDefaultConnection();
		if (lconn == null) {
			username = DBKernel.m_Username; // lokale Variante
		} else {
			//System.out.println(lconn.getMetaData());
			username = lconn.getMetaData().getUserName(); // Server (hoffe ich klappt immer ...?!?)
		}
	}
	catch (SQLException e) {
		//MyLogger.handleException(e);
	} 
  	return username;
  }
  
  public static void setForeignNullAt(final String tableName, final String fieldName, final Object id) {
    try {
      Statement anfrage = getDBConnection().createStatement();
      String sql = "UPDATE " + delimitL(tableName) + " SET " + delimitL(fieldName) + " = NULL WHERE " + delimitL("ID") + " = " + id;
      //System.out.println(sql);
      anfrage.execute(sql);
    }
    catch (Exception e) {
    	MyLogger.handleException(e);
    }
  }
  public static boolean insertBLOB(final String tableName, final String fieldName, final File fl, final int id) {
  	boolean result = false;
    try {
      if (fl.exists()) {
        String sql = "INSERT INTO " + delimitL("DateiSpeicher") +
        " (" + delimitL("Zeitstempel") + "," + delimitL("Tabelle") + "," + delimitL("Feld") + "," + delimitL("TabellenID") + "," +
        delimitL("Dateiname") + "," + delimitL("Dateigroesse") + "," + delimitL("Datei") + ")" +
        " VALUES (?,?,?,?,?,?,?);";

    	PreparedStatement psmt = getDBConnection().prepareStatement(sql);
  	    psmt.clearParameters();
  	    psmt.setTimestamp(1, new Timestamp(new Date().getTime()));
  	    psmt.setString(2, tableName);
  	    psmt.setString(3, fieldName);
  	    psmt.setInt(4, id);
  	    psmt.setString(5, fl.getName());
  	    psmt.setInt(6, (int)fl.length());
        FileInputStream fis = new FileInputStream(fl);
        psmt.setBinaryStream(7, fis, (int)fl.length());
  	    result = (psmt.executeUpdate() > 0);
  	    psmt.close();
  	    fis.close();
      }
	  }
    catch (Exception e) {
    	MyLogger.handleException(e);
    }
  	return result;
  }
  public static boolean insertBLOB(final String tableName, final String fieldName, final String content, final String filename, final int id) {
  	boolean result = false;
    try {
      String sql = "INSERT INTO " + delimitL("DateiSpeicher") +
      " (" + delimitL("Zeitstempel") + "," + delimitL("Tabelle") + "," + delimitL("Feld") + "," + delimitL("TabellenID") + "," +
      delimitL("Dateiname") + "," + delimitL("Dateigroesse") + "," + delimitL("Datei") + ")" +
      " VALUES (?,?,?,?,?,?,?);";

      PreparedStatement psmt = getDBConnection().prepareStatement(sql);
  	    psmt.clearParameters();
  	    psmt.setTimestamp(1, new Timestamp(new Date().getTime()));
  	    psmt.setString(2, tableName);
  	    psmt.setString(3, fieldName);
  	    psmt.setInt(4, id);
  	    psmt.setString(5, filename);
  	    byte[] b = content.getBytes();
  	    psmt.setInt(6, b.length);
  	    InputStream bais = new ByteArrayInputStream(b);
  	    psmt.setBinaryStream(7, bais, b.length);
  	    result = (psmt.executeUpdate() > 0);
  	    psmt.close();
  	    bais.close();
	  }
    catch (Exception e) {
    	MyLogger.handleException(e);
    }
  	return result;
  }
  public static LinkedHashMap<String, Timestamp> getFirstUserFromChangeLog(final String tablename, final Integer tableID) {
	  LinkedHashMap<String, Timestamp> result = new LinkedHashMap<String, Timestamp>();
	  String sql = "SELECT " + delimitL("Username") + "," + delimitL("Zeitstempel") + " FROM " + delimitL("ChangeLog") +
	  	" WHERE " + delimitL("Tabelle") + " = '" + tablename + "' AND " + delimitL("TabellenID") + " = " + tableID +
	  	" ORDER BY " + delimitL("Zeitstempel") + " ASC";	  	
		ResultSet rs = getResultSet(sql, false);
		try {
			if (rs != null && rs.first()) {
				result.put(rs.getString(1), rs.getTimestamp(2));		
			}
		}
		catch (Exception e) {MyLogger.handleException(e);}
  	return result;
  }
  public static LinkedHashMap<Integer, Vector<String>> getUsersFromChangeLog(final String tablename, final Integer tableID) {
  	return getUsersFromChangeLog(tablename, tableID, null);
  }
  public static LinkedHashMap<Integer, Vector<String>> getUsersFromChangeLog(final String tablename, final String username) {
	  return getUsersFromChangeLog(tablename, null, username);
  }
  public static LinkedHashMap<Integer, Vector<String>> getUsersFromChangeLog(final String tablename, final Integer tableID, final String username) {
	  return getUsersFromChangeLog(null, tablename, tableID, username, false);
  }
  public static LinkedHashMap<Integer, Vector<String>> getUsersFromChangeLog(final Statement anfrage, final String tablename, final Integer tableID, final String username, final boolean showDeletedAsWell) {
	  LinkedHashMap<Integer, Vector<String>> result = new LinkedHashMap<Integer, Vector<String>>();
	  Vector<String> entries = new Vector<String>();
	  String sql = "SELECT " + delimitL("TabellenID") + "," + delimitL("Username") + "," + delimitL("Zeitstempel") + // DISTINCT
	  	"," + delimitL(tablename) + "." + delimitL("ID") + " AS " + delimitL("ID") +
	  	"," + delimitL("ChangeLog") + "." + delimitL("ID") +
	  	"," + delimitL("Alteintrag") + "," + delimitL(tablename) + ".*" +
	  	" FROM " + delimitL("ChangeLog") +
	  	" LEFT JOIN " + delimitL(tablename) + " ON " + delimitL("ChangeLog") + "." + delimitL("TabellenID") + "=" +
	  	delimitL(tablename) + "." + delimitL("ID") + 
	  	" WHERE " + delimitL("ChangeLog") + "." + delimitL("Tabelle") + " = '" + tablename + "'" +
	  	(tableID != null ? " AND " + delimitL("ChangeLog") + "." + delimitL("TabellenID") + " = " + tableID : "") +
	  	(username != null ? " AND " + delimitL("ChangeLog") + "." + delimitL("Username") + " = '" + username + "'" : "") +
	  	" ORDER BY " + delimitL("ChangeLog") + "." + delimitL("ID") + " ASC";	  	// Zeitstempel DESC
  	//System.out.println(sql);
		ResultSet rs = anfrage == null ? getResultSet(sql, false) : getResultSet(anfrage, sql, false);
		try {
			if (rs != null && rs.first()) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				String actualRow = "";
				for (int j=8;j<=rs.getMetaData().getColumnCount();j++) {
					actualRow += "\t" + rs.getString(j);
				}
					do {	
						if (showDeletedAsWell || rs.getObject("ID") != null) { // wurde die ID in der Zwischenzeit gelöscht? Dann muss sie auch nicht gelistet werden!
							Integer id = rs.getInt("TabellenID");
							if (result.containsKey(id)) {
								entries = result.get(id);
							} else {
								entries = new Vector<String>();
							}
							String newEntry = rs.getString("Username") + "\t" + sdf.format(rs.getTimestamp("Zeitstempel"));
							Object o = rs.getObject("Alteintrag");
							if (o != null && o instanceof Object[]) {
								Object[] oo = (Object[]) o;
								String ae = "";
								for (int i=1;i<oo.length;i++) {
									ae += "\t" + oo[i];
								}
								if (entries.size() > 0) {
									String oldEntry = entries.get(entries.size() - 1);
									entries.remove(entries.size() - 1);
									int oe = oldEntry.indexOf("\n\t");
									if (oldEntry.startsWith("Unknown\n\t")) {
										oe = oldEntry.indexOf("\n\t", oe + 1);
									}
									if (oe > 0) {
										oldEntry = oldEntry.substring(0, oe) + "\n" + ae;
									} else {
										oldEntry = oldEntry + "\n" + ae;
									}
									entries.add(oldEntry);
								}
								else {
									// kann passieren, wenn erster Eintrag von defad, z.B. bei Katalogen
									entries.add("Unknown\n\t" + ae.substring(1));
									//newEntry = "Unknown\n\t" + ae.substring(1) + "\n" + newEntry;
								}
							}
							entries.add(newEntry + "\n" + actualRow);
							result.put(id, entries);									
						}
						else {
							//System.err.println(rs.getInt("TabellenID") + " wurde bereits gelöscht!");
						}
					} while (rs.next());					
			}
		}
		catch (Exception e) {MyLogger.handleException(e);}
  	return result;
  }
  public static String delimitL(final String name) {
    String newName = name.replace("\"", "\"\"");
    return "\"" + newName + "\"";
  }
  
  public static boolean closeDBConnections(final boolean kompakt) {
	boolean result = true;
	try {
		if (localConn != null && !localConn.isClosed()) {
			if (!DBKernel.isServerConnection) {      		
				try {
					if (kompakt && !isAdmin()) { // kompakt ist nur beim Programm schliessen true
						closeDBConnections(false);
						try {
							localConn = getDefaultAdminConn();
						}
						catch (Exception e) {e.printStackTrace();}
					}
					Statement stmt = localConn.createStatement(); // ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY
					MyLogger.handleMessage("vor SHUTDOWN");
    	      	    stmt.execute("SHUTDOWN"); // Hier kanns es eine Exception geben, weil nur der Admin SHUTDOWN machen darf!
				}
				catch (SQLException e) {result = false;} // e.printStackTrace();
			}
			MyLogger.handleMessage("vor close");
			localConn.close();
			MyLogger.handleMessage("vor gc");
			System.gc();
			System.runFinalization();
			try {
				if (myList != null && myList.getMyDBTable() != null && myList.getMyDBTable().getActualTable() != null) {
					DBKernel.prefs.put("LAST_SELECTED_TABLE", myList.getMyDBTable().getActualTable().getTablename());
					
					DBKernel.prefs.put("LAST_MainFrame_FULL", DBKernel.mainFrame.getExtendedState() == JFrame.MAXIMIZED_BOTH ? "TRUE" : "FALSE");
					//DBKernel.mainFrame.setExtendedState(JFrame.NORMAL);
					/*
					DBKernel.prefs.put("LAST_MainFrame_WIDTH", DBKernel.mainFrame.getWidth()+"");
					DBKernel.prefs.put("LAST_MainFrame_HEIGHT", DBKernel.mainFrame.getHeight()+"");
					DBKernel.prefs.put("LAST_MainFrame_X", DBKernel.mainFrame.getX()+"");
					DBKernel.prefs.put("LAST_MainFrame_Y", DBKernel.mainFrame.getY()+"");
					*/
				}
			}
			catch (Exception e) {e.printStackTrace();}
		}
	}
    catch (SQLException e) {
    	result = false;
    	MyLogger.handleException(e);
    }
    return result;
  }
  public static void getPaper(final int tableID, final String tablename, final String feldname, final int blobID) {
    try {
			ResultSet rs = getResultSet("SELECT " + DBKernel.delimitL("Dateiname") + "," + DBKernel.delimitL("Datei") +
					" FROM " + delimitL("DateiSpeicher") +
					(blobID > 0 ?
							" WHERE " + DBKernel.delimitL("ID") + "=" + blobID
							:
								" WHERE " + DBKernel.delimitL("Tabelle") + "='" + tablename + "' AND " +
								DBKernel.delimitL("Feld") + "='" + feldname + "' AND " +
								DBKernel.delimitL("TabellenID") + "=" + tableID + " " +
								" ORDER BY " + delimitL("ID") + " DESC"
								)
					, true);
			if (rs.first()) {
				do {
					try {
						final String filename = rs.getString("Dateiname");
		        //final InputStream is = rs.getBinaryStream("Datei");
		        final byte[] b = rs.getBytes("Datei");
		        if (b != null) { // is
		        	Runnable runnable = new Runnable() {
		            @Override
					public void run() {
		    					try {
					        	String tmpFolder = System.getProperty("java.io.tmpdir");
					        	String pathname = "";
					        	if (tmpFolder != null && tmpFolder.length() > 0) {
						        	//ByteArrayOutputStream out = null;
						        	FileOutputStream out = null;
						        	try {
						        		//out = new ByteArrayOutputStream();
						        		if (!tmpFolder.endsWith(System.getProperty("file.separator"))) {
											tmpFolder += System.getProperty("file.separator");
										}
						        		pathname = tmpFolder + filename;
						        		out = new FileOutputStream(pathname);
							        	//int c;
							        	//while ((c = is.read()) != -1) out.write(c);
				                          //int availableLength = is.available();
				                          //byte[] totalBytes = new byte[availableLength];
				                          //int bytedata = is.read(totalBytes);
				                          out.write(b); // totalBytes
							        	//byte[] ba = out.toByteArray();
							        	//System.out.println("InputStreamLen = " + ba.length + "\tfeldname = " + feldname + "\ttableID = " + tableID + "\tfilename = " + filename);
						          }
						          finally {
						        	  /*
						          	if (is != null) {
										is.close();
									}
									*/
						            if (out != null) {
										out.close();
									}
						          }
						          if (pathname.length() > 0) {
						        	  Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"" + new File(pathname) + "\"");
						          }
										}
		    					}
									catch (Exception e) {
										MyLogger.handleException(e);
									}
			        	}
		        	};
		          Thread thread = new Thread(runnable);
		          thread.start();
		        }
		        else {
		        	MyLogger.handleMessage("InputStream = null\tfeldname = " + feldname + "\ttableID = " + tableID + "\tfilename = " + filename);
		        }
					}
					catch (Exception e) {
						MyLogger.handleException(e);
					}
					break; // nur das zuletzt abgespeicherte soll geöffnet werden!
				} while (rs.next());
			}  	
    }
    catch (SQLException e) {
      MyLogger.handleException(e);
    }
  }
  
  public static Connection getDefaultConnection() {
	  Connection result = null;
	    String connStr = "jdbc:default:connection";
	    try {
	    	result = DriverManager.getConnection(connStr);
	    }
	    catch(Exception e) {
	    	//MyLogger.handleException(e);
	    }
	    return result;
  }
  public static Connection getDBConnection() throws Exception {
  	return getDBConnection(HSHDB_PATH, DBKernel.m_Username, DBKernel.m_Password, false);
  }
  public static Connection getDBConnection(final String username, final String password) throws Exception {
  	DBKernel.m_Username = username; 
  	DBKernel.m_Password = password; 
  	return getDBConnection(HSHDB_PATH, username, password, false);
  }
  public static void setLocalConn(final Connection conn, String path, String username, String password) {
	  localConn = conn;
	  DBKernel.HSHDB_PATH = path;
	  DBKernel.m_Username = username;
	  DBKernel.m_Password = password;
  }
  public static Connection getLocalConn(boolean try2Boot) {
	  try {
		if ((localConn == null || localConn.isClosed()) && try2Boot && isKNIME) localConn = getInternalKNIMEDB_LoadGui();
	}
	  catch (SQLException e) {
		e.printStackTrace();
	}
	  return localConn;
  }
  // newConn wird nur von MergeDBs benötigt
  public static Connection getDBConnection(final String dbPath, final String theUsername, final String thePassword, final boolean newConn) throws Exception {
	  if (newConn) {
		  return getNewConnection(theUsername, thePassword, dbPath);
	  }
	  else if (localConn == null || localConn.isClosed()) { 
		  localConn = getNewConnection(theUsername, thePassword, dbPath);
	  }
	  return localConn;
  }
  // newConn wird nur von MergeDBs benötigt
  public static Connection getDefaultAdminConn(final String dbPath, final boolean newConn) throws Exception {
	  Connection result = getDBConnection(dbPath, getTempSA(dbPath), getTempSAPass(dbPath), newConn);
	  if (result == null) result = getDBConnection(dbPath, getTempSA(dbPath, true), getTempSAPass(dbPath, true), newConn);
	  return result;
  }
  public static Connection getDefaultAdminConn() throws Exception {
	  return getDefaultAdminConn(DBKernel.HSHDB_PATH, false);
  }
  private static Connection getNewConnection(final String dbUsername, final String dbPassword, final String path) throws Exception {
	  if (isServerConnection) {
		return getNewServerConnection(dbUsername, dbPassword, path);
	} else {
		return getNewLocalConnection(dbUsername, dbPassword, path + "DB");
	}
  }
  public static Connection getNewServerConnection(final String dbUsername, final String dbPassword, final String serverPath) throws Exception {
	  //serverPath = "192.168.212.54/silebat";
	    Connection result = null;
	    passFalse = false;
	    Class.forName("org.hsqldb.jdbc.JDBCDriver").newInstance();
	    //System.out.println(dbFile); 
	    String connStr = "jdbc:hsqldb:hsql://" + serverPath;// + (isKNIME ? ";readonly=true" : "");// + ";hsqldb.cache_rows=1000000;hsqldb.cache_size=1000000";
	    try {
	    	result = DriverManager.getConnection(connStr, dbUsername, dbPassword);	    		
	    	if (isKNIME) result.setReadOnly(DBKernel.prefs.getBoolean("PMM_LAB_SETTINGS_DB_RO", true));
	    }
	    catch(Exception e) {
	    	passFalse = e.getMessage().startsWith("invalid authorization specification");
	    	MyLogger.handleException(e);
	    }
	    return result;
  }
	public static boolean isHsqlServer(String checkURL) {
		boolean result = false; //checkURL.startsWith("192") || checkURL.startsWith("localhost");
		String host = "";
		try {
			if (!checkURL.startsWith("http")) {
				checkURL = "http://" + checkURL;
			}
			URL url = new URL(checkURL); // "192.168.212.54/silebat"
			host = url.getHost();
			if (!host.isEmpty()) {
				InetSocketAddress isa = new InetSocketAddress(host, 9001);//new URL(checkURL).openConnection();
				result = !isa.isUnresolved();				
			}
		}
		catch (MalformedURLException e) {
			//e.printStackTrace();
		}
		//System.err.println(checkURL + "\t" + result + "\t" + host);
		return result;
	}
  public static Connection getNewLocalConnection(final String dbUsername, final String dbPassword, final String dbFile) throws Exception {
  	  //startHsqldbServer("c:/tmp/DB", "DB");
    Connection result = null;
    passFalse = false;
    Class.forName("org.hsqldb.jdbc.JDBCDriver").newInstance();
    //System.out.println(dbFile); 
    String connStr = "jdbc:hsqldb:file:" + dbFile + ";hsqldb.cache_rows=1000000;hsqldb.cache_size=1000000";// + (isKNIME ? ";readonly=true" : "");
    //connStr = "jdbc:hsqldb:hsql://localhost/DB;hsqldb.cache_rows=1000000;hsqldb.cache_size=1000000;hsqldb.tx=mvcc"; // 
    try {
    	result = DriverManager.getConnection(connStr
    			//+ ";crypt_key=65898eaeb54a0bc34097cae57259e8f9;crypt_type=blowfish"
    			,dbUsername, dbPassword);  
    	if (isKNIME) result.setReadOnly(DBKernel.prefs.getBoolean("PMM_LAB_SETTINGS_DB_RO", true));
    }
    catch(Exception e) {
    	// Database lock acquisition failure: lockFile: org.hsqldb.persist.LockFile@137939d4[file =C:\Dokumente und Einstellungen\Weiser\.localHSH\BfR\DBs\DB.lck, exists=true, locked=false, valid=false, ] method: checkHeartbeat read: 2010-12-08 09:08:12 heartbeat - read: -4406 ms.
    	// 
    	passFalse = e.getMessage().startsWith("invalid authorization specification");
    	//MyLogger.handleMessage(e.getMessage());
    	if (e.getMessage().startsWith("Database lock acquisition failure:")) {
    		result = getLocalCopyROConnection(dbFile, dbUsername, dbPassword);
    		if (result == null) {
    			InfoBox ib = new InfoBox(login, "Die Datenbank wird zur Zeit von\neinem anderen Benutzer verwendet!", true, new Dimension(300, 150), null, true);
    			ib.setVisible(true);    				    			
    		}
    	}
    	else {
    		if (!isKNIME) MyLogger.handleException(e);
    	}
    	//LOGGER.log(Level.INFO, dbUsername + " - " + dbPassword + " - " + dbFile, e);
    }
    return result;
  }
  private static Connection getLocalCopyROConnection(final String dbFile, final String dbUsername, final String dbPassword) {
	  Connection result = null;
	    try {
			File tempDir = File.createTempFile("DBdirectory",".db");
			tempDir.delete();
			copyDirectory(new File(dbFile).getParentFile(), tempDir);
			
			if (debug) {
				MyLogger.handleMessage(tempDir.getAbsolutePath());
			}
	    	result = DriverManager.getConnection("jdbc:hsqldb:file:" + tempDir.getAbsolutePath() + System.getProperty("file.separator") + "DB;hsqldb.cache_rows=1000000;hsqldb.cache_size=1000000" // ;readonly=true
	    	//result = DriverManager.getConnection("jdbc:hsqldb:file:" + "C:\\Dokumente und Einstellungen\\Weiser\\Lokale Einstellungen\\Temp\\DBdirectory3007129564907469644.db\\DB" + ";hsqldb.cache_rows=1000000;hsqldb.cache_size=1000000" + ";readonly=true"
	    			//+ ";crypt_key=65898eaeb54a0bc34097cae57259e8f9;crypt_type=blowfish"
	    			,dbUsername, dbPassword);
	    	result.setReadOnly(true);
	    	tempROZeit = System.currentTimeMillis();
	    }
	    catch(Exception e1) {
	    	MyLogger.handleException(e1);
	    }	  
	    return result;
  }
  private static void copyDirectory(final File sourceLocation, final File targetLocation) {
      
      if (sourceLocation.isDirectory()) {
          if (!targetLocation.exists()) {
              targetLocation.mkdir();
          }
          
          String[] children = sourceLocation.list();
          for (int i=0; i<children.length; i++) {
        	  if (children[i].startsWith("DB.")) {
				copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
			}
          }
      }
      else {          
    	  FileChannel inChannel = null, outChannel = null;
	      try {
	          inChannel = new FileInputStream(sourceLocation).getChannel();
	          outChannel = new FileOutputStream(targetLocation).getChannel();
	          inChannel.transferTo(0, inChannel.size(), outChannel);
	      } 
	      catch (IOException e) {
	          MyLogger.handleException(e);
	      }
	      finally {
	          
				try {
					if (inChannel != null) {
						inChannel.close();
					}
			        if (outChannel != null) {
						outChannel.close();
					}
				}
				catch (IOException e) {MyLogger.handleException(e);}
	      }
      }
  }  
  /*
   * 
   * 
   * 
   * 
   */
/*
  private static void startHsqldbServer(final String filename, final String DBname) {
      boolean isNetwork = true;
      if (isNetwork) {
          Server server = new Server();

          server.setDatabaseName(0, DBname);
          server.setDatabasePath(0, filename);
          server.setSilent(true);
          //server.setLogWriter(null);
          //server.setErrWriter(null);
          server.start();
      }
      //insertTestData();
      //readTestData();

      /*
      try {
          Thread.sleep(5000L);
      } catch (InterruptedException e) {
          MyLogger.handleException(e);
      }
*/
      //shutdownDataBase();
//  }
  /*
  public static void main(String[] args) { 
	  if (args.length > 1) {
		  startHsqldbServer(args[0], args[1]);
	  }
  }
*/
  /*
  private static Object getCryptKey() {
  	Object result = null;
		ResultSet rs = getResultSet("SELECT CRYPT_KEY('Blowfish', null) FROM " + delimitL("Users"), true);
		try {
			if (rs != null && rs.first()) {
				result = rs.getObject(1);
			}
		}
		catch (Exception e) {MyLogger.handleException(e);}
		return result;
  }
  */
  public static Integer getID(final String tablename, final String feldname, final String feldVal) {
	  Integer result = null;
	  String sql = "SELECT " + delimitL("ID") + " FROM " + delimitL(tablename) + " WHERE " + delimitL(feldname);
	  if (feldVal == null) {
		sql += " IS NULL";
	} else {
		sql += " = '" + feldVal.replace("'", "''") + "'";
	}
		ResultSet rs = getResultSet(sql, true);
		try {
			if (rs != null && rs.last()) {
				result = rs.getInt(1);
				if (rs.getRow() > 1) {
					System.err.println("Attention! Entry " + feldVal + " occurs " + rs.getRow() + "x in column " + feldname + " of table " + tablename + ", please check!!!");
				}
			}
		}
		catch (Exception e) {MyLogger.handleException(e);}
		return result;
  }
  public static Integer getLastID(final String tablename) {
	  Integer result = null;
	  String sql = "SELECT MAX(" + delimitL("ID") + ") FROM " + delimitL(tablename);
		ResultSet rs = getResultSet(sql, true);
		try {
			if (rs != null && rs.last()) {
				result = rs.getInt(1);
			}
		}
		catch (Exception e) {MyLogger.handleException(e);}
		return result;
  }
  public static Object getValue(final String tablename, final String feldname, final String feldVal, final String desiredColumn) {
	  return getValue(null, tablename, feldname, feldVal, desiredColumn);
  }
  public static Object getValue(Connection conn, final String tablename, final String feldname, final String feldVal, final String desiredColumn) {
	  return getValue(conn, tablename, new String[] {feldname}, new String[] {feldVal}, desiredColumn);
  }
  public static Object getValue(Connection conn, final String tablename, final String[] feldname, final String[] feldVal, final String desiredColumn) {
	  	Object result = null;
		  String sql = "SELECT " + delimitL(desiredColumn) + " FROM " + delimitL(tablename) + " WHERE ";
		  for (int i=0;i<feldname.length;i++) {
			  if (i < feldVal.length) {
				  if (!sql.trim().endsWith("WHERE")) sql += " AND ";
				  	sql += delimitL(feldname[i]);			  				  
					  if (feldVal[i] == null) {
							sql += " IS NULL";
						} else {
							sql += " = '" + feldVal[i].replace("'", "''") + "'";
						}
			  }
		  }
			ResultSet rs = getResultSet(conn, sql, true);
			try {
				if (rs != null && rs.last()) { //  && rs.getRow() == 1
					result = rs.getObject(1);
					if (rs.getRow() > 1) {
						System.err.println("Attention! Entry " + feldVal + " occurs " + rs.getRow() + "x in column " + feldname + " of table " + tablename + ", please check (getValue)!!!");
					}
				}
			}
			catch (Exception e) {MyLogger.handleException(e);}
			return result;
	  }
  public static boolean isDouble(final String textValue) {
		boolean result = true;
		try {
			//System.out.println(textValue);
			if (textValue.equals("-")) {
				return true;
			}
			Double.parseDouble(textValue);
		}
		catch(NumberFormatException e) {
			result = false;
		}	
		return result;
	}

  public static boolean hasID(final String tablename, final int id) {
  	boolean result = false;
		ResultSet rs = getResultSet("SELECT " + delimitL("ID") + " FROM " + delimitL(tablename) +
				" WHERE " + delimitL("ID") + "=" + id, true);
		try {
			if (rs != null && rs.last() && rs.getRow() == 1) {
				result = true;
			}
		}
		catch (Exception e) {MyLogger.handleException(e);}
		return result;
  }
  public static void refreshHashTables() {
	  filledHashtables.clear();
  }
  public static LinkedHashMap<Object, String> fillHashtable(final MyTable theTable, final String startDelim, final String delimiter, final String endDelim, final boolean goDeeper) {
	  return fillHashtable(theTable, startDelim, delimiter, endDelim, goDeeper, false);
  }
  public static LinkedHashMap<Object, String> fillHashtable(final MyTable theTable, final String startDelim, final String delimiter, final String endDelim, final boolean goDeeper, final boolean forceUpdate) {
    if (theTable == null) {
		return null;
	}
    String foreignTable = theTable.getTablename();
    //if (DBKernel.debug) System.err.println(foreignTable + "\t" + (System.currentTimeMillis() / 1000));
 	if (forceUpdate && filledHashtables.containsKey(foreignTable)) {
		filledHashtables.remove(foreignTable);
	}
    if (filledHashtables.containsKey(foreignTable)) {
		return filledHashtables.get(foreignTable);
	}
    LinkedHashMap<Object, String> h = new LinkedHashMap<Object, String>();
  	String selectSQL = theTable.getSelectSQL();
    String sql = selectSQL;
    ResultSet rs = getResultSet(sql, true);
    String value;
    int i;
    Object o = null;
    Object val = null;
    try {
      if (rs != null && rs.first()) {
      	MyTable[] foreignFields = theTable.getForeignFields();
      	String[] mnTable = theTable.getMNTable();
        do {
        	value="";
        	if (foreignTable.equals("ICD10_Kodes")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			if (rs.getMetaData().getColumnName(i).equals("Titel")) {
            			value = rs.getString(i);
        				break;
        			}
        		}
        	}
        	else if (foreignTable.equals("Versuchsbedingungen")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("Agens") || cn.equals("Matrix")) {
      	        	  value += handleField(rs.getInt(i), rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        		}
        	}
        	else if (foreignTable.equals("Prozessdaten")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("ID") || cn.equals("Prozess_CARVER")) {
      	        	  value += handleField(rs.getInt(i), rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        			else if (cn.equals("ProzessDetail")) {
        	        	  value += handleField(null, rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);        				
        			}
        		}        		
        	}
        	else if (foreignTable.equals("Lieferungen")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("Charge")) {
        				value += handleField(rs.getInt(i), rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);        			
        			}
        			else if (cn.equals("Unitmenge") || cn.equals("UnitEinheit") || cn.equals("Lieferdatum")) {
        				value += handleField(null, rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        		}        		
        	}
        	else if (foreignTable.equals("Chargen")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("Artikel")) {
        				value += handleField(rs.getInt(i), rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);        			
        			}
        			else if (cn.equals("ChargenNr") || cn.equals("Herstellungsdatum")) {
        				value += handleField(null, rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        		}        		
        	}
        	else if (foreignTable.equals("Produktkatalog")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("Artikelnummer") || cn.equals("Bezeichnung")) {
      	        	  value += handleField(null, rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        		}        		
        	}
        	else if (foreignTable.equals("Produzent")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("Kontaktadresse")) {
      	        	  value += handleField(rs.getInt(i), rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        		}        		
        	}
        	else if (foreignTable.equals("Kontakte")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("Name") || cn.equals("Strasse") || cn.equals("Ort")) {
      	        	  value += handleField(null, rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        		}        		
        	}
        	else if (foreignTable.startsWith("Codes_")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("CodeSystem") || cn.equals("Code")) {
      	        	  value += handleField(null, rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        		}        		
        	}
        	else if (foreignTable.equals("Zutatendaten")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("Vorprozess")) {
        	        	  value += handleField(rs.getInt(i), rs.getString(i), foreignFields, mnTable, i, false, startDelim, delimiter, endDelim);
          			}
          			else if (cn.equals("Matrix")) {
          	        	  value += handleField(rs.getInt(i), rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);        				
          			}
        		}       
        		if (value.isEmpty()) value ="(Mix...)";
        	}
        	else if (foreignTable.equals("Matrices")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("Matrixname")) {
      	        	  value += handleField(null, rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        		}        		
        	}
        	else if (foreignTable.equals("Agenzien")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("Agensname")) {
      	        	  value += handleField(null, rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        		}        		
        	}
        	else if (foreignTable.equals("Literatur")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("Erstautor") || cn.equals("Jahr") || cn.equals("Titel")) {
      	        	  value += handleField(null, rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        		}        		
        	}
        	else if (foreignTable.equals("Nachweisverfahren") || foreignTable.equals("Aufbereitungsverfahren")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("Bezeichnung")) {
      	        	  value += handleField(null, rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        		}        		
        	}
        	else if (foreignTable.equals("GeschaetzteModelle")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("Versuchsbedingung") || cn.equals("Modell") || cn.equals("ID")) {
      	        	  value += handleField(null, rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        		}        		
        	}
        	else if (foreignTable.equals("GeschaetzteParameter")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("Parameter")) {
        				value += handleField(rs.getInt(i), rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        			else if (cn.equals("Wert")) {
        	        	value += handleField(null, DBKernel.getDoubleStr(rs.getObject(i)), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
          			}
        		}        		
        	}
        	else if (foreignTable.equals("ModellkatalogParameter")) {
        		for (i=1;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
        			if (cn.equals("Parametername")) {
      	        	  value += handleField(null, rs.getString(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim);
        			}
        		}        		
        	}
        	else if (foreignTable.equals("DoubleKennzahlen")) {
        		for (i=2;i<=rs.getMetaData().getColumnCount();i++) {
        			String cn = rs.getMetaData().getColumnName(i); 
    				if (cn.equals("Wert")) {
		        	  value += handleField(null, getDoubleStr(rs.getObject(i)), null, null, i, false, startDelim, delimiter, endDelim, false);
    				}
    				else if (cn.equals("Exponent")) {
  		        	  String exp = handleField(null, getDoubleStr(rs.getObject(i)), null, null, i, false, startDelim, delimiter, endDelim, false);    		
  		        	  if (exp.length() > 0) {
						value += " * 10^" + exp;
					}
		        	  break;
    				}
        		}    
        		
        		if (value.length() == 0) {
					value = "...";
				}
        	}
        	else {
        	    boolean fetchID = foreignTable.equals("Kontakte");
        	    boolean reallyGD = !foreignTable.equals("Messwerte"); // lieber nicht in die Tiefe gehen, hier droht eine Endlosschleife, da sich die Tabellen gegenseitig referenzieren
    	          for (i=fetchID ? 1:2;i<=rs.getMetaData().getColumnCount();i++) { // bei 2 beginnen, damit die Spalte ID nicht zu sehen ist!
    	        	  value += handleField(rs.getObject(i), rs.getString(i), foreignFields, mnTable, i, goDeeper && reallyGD, startDelim, delimiter, endDelim);
    	  	      }
        	}
	          //if (value.length() > 0) value = value.substring(0,value.length()-delimiter.length());
	          //value += endDelim;
        	o = rs.getObject(1);
        	val = value;
        	if (theTable.getTablename().equals("DoubleKennzahlen")) {
				h.put(new Double((Integer)rs.getObject(1)), value);
			} else {
				h.put(rs.getObject(1), value);
			}
        } while (rs.next());
      }
    }
    catch (Exception e) {MyLogger.handleException(e);MyLogger.handleMessage(theTable.getTablename()+"\t"+o+"\t"+val + "\t" + selectSQL);}
    if (!filledHashtables.containsKey(foreignTable)) {
		filledHashtables.put(foreignTable, h);
	}
    return h;
  }
  public static String getDoubleStr(final Object dbl) {
  	if (dbl == null) {
		return null;
	}
		NumberFormat f = NumberFormat.getInstance(Locale.US);
		f.setGroupingUsed(false);
		return f.format(dbl);
  }
  public static boolean kzIsString(final String kennzahl) {
		return kennzahl.equals("Verteilung") ||
		kennzahl.equals("Funktion (Zeit)") ||
		kennzahl.equals("x") ||
		kennzahl.equals("Funktion (x)");
	}
  public static boolean kzIsBoolean(final String kennzahl) {
		return kennzahl.endsWith("_g") ||
		kennzahl.equals("Undefiniert (n.d.)");
	}
  public static Object insertDBL(final String tablename, final String fieldname, final Integer tableID, Object kzID, String kz, Object value) {
		try {
			if (kzID == null) {
    		kzID = DBKernel.getValue(tablename, "ID", tableID+"", fieldname);
    		if (kzID == null) {
  				PreparedStatement psmt = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL("DoubleKennzahlen") +
  		    		" (" + DBKernel.delimitL("Wert") + ") VALUES (NULL)", Statement.RETURN_GENERATED_KEYS);
  				if (psmt.executeUpdate() > 0) {
  					kzID = DBKernel.getLastInsertedID(psmt);
  					DBKernel.sendRequest("UPDATE " + DBKernel.delimitL(tablename) + " SET " + DBKernel.delimitL(fieldname) + "=" + kzID + " WHERE " + DBKernel.delimitL("ID") + "=" + tableID, false);
  				}
    		}
			}
	  	if (kzID == null) {
			System.err.println("eeeeeSHIIETEW...");
		} else { // UPDATE
				if (kz.indexOf("(?)") >= 0) {
					kz = kz.replace("(?)", "(x)");
				}
				if (value == null) {
					value = "NULL";
				}
				if (DBKernel.kzIsString(kz)) {
					DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL(kz) + "='" + value + "'" + " WHERE " + DBKernel.delimitL("ID") + "=" + kzID, false);
				} else if (DBKernel.kzIsBoolean(kz)) {
					DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL(kz) + "=" + value + "" + " WHERE " + DBKernel.delimitL("ID") + "=" + kzID, false);
				} else {
					DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("DoubleKennzahlen") + " SET " + DBKernel.delimitL(kz) + "=" + value + " WHERE " + DBKernel.delimitL("ID") + "=" + kzID, false);
				}
			}
		}
    catch (Exception e) {MyLogger.handleException(e);}		
    return kzID;
	}
  
  private static String handleField(final Object id, final String tmp, final MyTable[] foreignFields, final String[] mnTable, final int i, final boolean goDeeper, final String startDelim, final String delimiter, final String endDelim) {
	  return handleField(id, tmp, foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim, true);
  }
  private static String handleField(final Object id, String tmp, final MyTable[] foreignFields, final String[] mnTable, final int i, final boolean goDeeper, final String startDelim, final String delimiter, final String endDelim, final boolean newRow) {
	  String result = "";
		if (foreignFields != null && i > 1 && foreignFields.length > i-2 && foreignFields[i-2] != null) {
			String ft = foreignFields[i-2].getTablename(); 
			if (tmp == null) {
				tmp = "";//ft + ": leer\n";
			}
			else if (goDeeper && id != null) {
			    LinkedHashMap<Object, String> hashBox = fillHashtable(foreignFields[i-2], startDelim, delimiter, endDelim, goDeeper); //" | " " ; "
			    if (hashBox != null && hashBox.get(id) != null) {
			    	String ssttrr = hashBox.get(id).toString();
			    	tmp = ssttrr.trim().length() == 0 ? "" : ssttrr;   	// ft + ":\n" + 
			    }
			    else if (mnTable != null && i > 1 && i-2 < mnTable.length && mnTable[i-2] != null && mnTable[i-2].length() > 0) {
			    	tmp = "";
			    	//System.err.println("isMN..." + ft);
			    }
			    else {	
			    	System.err.println("hashBox überprüfen...\n" + tmp + "\t" + ft);
			    	tmp = "";//ft + ": leer\n";
			    }
			}
			else {
				tmp = ft + "-ID: " + tmp + "\n";
			}
		}
        if (tmp != null && tmp.length() > 0) {
			if (mnTable != null && i > 1 && i-2 < mnTable.length && mnTable[i-2] != null && mnTable[i-2].length() > 0) { // MN-Tabellen, wie z.B. INT oder DBL sollten hier unsichtbar bleiben!
			}
			else {
				result += tmp + (newRow ? "\n" : ""); // rs.getMetaData().getColumnName(i) + ": " + 	  				
			}
        }
        return result;
  }
  public static ResultSet getResultSet(final String sql, final boolean suppressWarnings) {
	    ResultSet ergebnis = null;
	    try {
	    	getDBConnection();
	      Statement anfrage = localConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	      ergebnis = anfrage.executeQuery(sql);
	      ergebnis.first();
	    }
	    catch (Exception e) {
	      if (!suppressWarnings) {
	    	  MyLogger.handleMessage(sql);
	        MyLogger.handleException(e);
	      }
	    }
	    return ergebnis;
	  }
  public static ResultSet getResultSet(final Connection conn, final String sql, final boolean suppressWarnings) {
		if (conn == null) {
			return getResultSet(sql, suppressWarnings);
		}
		else {
			try {
				return getResultSet(conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY), sql, suppressWarnings);
			}
			catch (SQLException e) {
				//e.printStackTrace();
			}
		}
		return null;
  }
  public static ResultSet getResultSet(final Statement anfrage, final String sql, final boolean suppressWarnings) {
	    ResultSet ergebnis = null;
	    try {
	      ergebnis = anfrage.executeQuery(sql);
	      ergebnis.first();
	    }
	    catch (Exception e) {
	      if (!suppressWarnings) {
	    	  MyLogger.handleMessage(sql);
	        MyLogger.handleException(e);
	      }
	    }
	    return ergebnis;
	  }
  public static boolean sendRequest(final String sql, final boolean suppressWarnings) {
	  return sendRequest(sql, suppressWarnings, false);
  }
  public static boolean sendRequest(final String sql, final boolean suppressWarnings, final boolean fetchAdminInCase) {
	try {
		Connection conn = getDBConnection();
		return sendRequest(conn, sql, suppressWarnings, fetchAdminInCase);
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	return false;
  }
  public static boolean sendRequest(Connection conn, final String sql, final boolean suppressWarnings, final boolean fetchAdminInCase) {
    boolean result = false;
    boolean adminGathered = false;
    try {
    	if (fetchAdminInCase && !DBKernel.isAdmin()) {
    		DBKernel.closeDBConnections(false);
    		conn = DBKernel.getDefaultAdminConn();
    		adminGathered = true;
    	}
      Statement anfrage = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      anfrage.execute(sql);
      result = true;
    }
    catch (Exception e) {
      if (!suppressWarnings) {
    	  if (!DBKernel.isKNIME ||
    			  (!e.getMessage().equals("The table data is read only") && !e.getMessage().equals("invalid transaction state: read-only SQL-transaction"))) MyLogger.handleMessage(sql);
    	  MyLogger.handleException(e);
      }
    }
    if (adminGathered) {
		DBKernel.closeDBConnections(false);
		try {
			DBKernel.getDBConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
    }
    return result;
  }
  public static Integer sendRequestGetAffectedRowNumber(Connection conn, final String sql, final boolean suppressWarnings, final boolean fetchAdminInCase) {
	    Integer result = null;
	    boolean adminGathered = false;
	    try {
	    	if (fetchAdminInCase && !DBKernel.isAdmin()) {
	    		DBKernel.closeDBConnections(false);
	    		conn = DBKernel.getDefaultAdminConn();
	    		adminGathered = true;
	    	}
	      Statement anfrage = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	      result = anfrage.executeUpdate(sql);
	    }
	    catch (Exception e) {
	      if (!suppressWarnings) {
	    	  if (!DBKernel.isKNIME ||
	    			  (!e.getMessage().equals("The table data is read only") && !e.getMessage().equals("invalid transaction state: read-only SQL-transaction"))) MyLogger.handleMessage(sql);
	    	  MyLogger.handleException(e);
	      }
	    }
	    if (adminGathered) {
			DBKernel.closeDBConnections(false);
			try {
				DBKernel.getDBConnection();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
	    }
	    return result;
	  }
  public static String sendRequestGetErr(final String sql) {
    String result = "";
    try {
    	getDBConnection();
      Statement anfrage = localConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      anfrage.execute(sql);
    }
    catch (Exception e) {
    	result = e.getMessage();
    	MyLogger.handleException(e);
    }
    return result;
  }
  public static boolean showHierarchic(final String tableName) {
  	return tableName.equals("Matrices") || tableName.equals("Methoden") || tableName.equals("Agenzien") || tableName.equals("Methodiken");
  }
	public static int countUsers(boolean adminsOnly) {
		return countUsers(localConn, adminsOnly);
	}
	public static int countUsers(Connection conn, boolean adminsOnly) {
		int result = -1;
		ResultSet rs = getResultSet(conn, "SELECT COUNT(*) FROM " + delimitL("Users") +
				" WHERE " + (adminsOnly ? delimitL("Zugriffsrecht") + " = " + Users.ADMIN + " AND " : "") + delimitL("Username") + " IS NOT NULL", true);
		try {
			if (rs != null && rs.first()) {
				result = rs.getInt(1);
			}
		}
		catch (Exception e) {
			MyLogger.handleException(e);
			result = -1;
		}
		//System.out.println(result);
    return result;
  }
	public static int getRowCount(final String tableName, final String where) {
		int result = 0;
		String sql = "SELECT COUNT(*) FROM " + DBKernel.delimitL(tableName) + (where != null && where.trim().length() > 0 ? " " + where : "");
		ResultSet rs = DBKernel.getResultSet(sql, false);
		try {
			if (rs != null && rs.first()) {
				result = rs.getInt(1);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static boolean isAdmin() {
		String un = getUsername();
		return isAdmin(null, un);
	}
	public static boolean isAdmin(Connection conn, String un) { // nur der Admin kann überhaupt die Users Tabelle abfragen, daher ist ein Wert <> -1 ein Zeichen für Adminrechte, das kann auch defad sein
		if (conn == null) {
			if (un.equals(getTempSA(HSHDB_PATH))) {
				return true;
			}
		}
		boolean result = false;
		ResultSet rs = getResultSet(conn, "SELECT COUNT(*) FROM " + delimitL("Users") +
				" WHERE " + delimitL("Zugriffsrecht") + " = " + Users.ADMIN +
				" AND " + delimitL("Username") + " = '" + un + "'", true);
		try {
			if (rs != null && rs.first()) {
				result = (rs.getInt(1) > (conn == null ? 0 : -1));
			}
		}
		catch (Exception e) {
			MyLogger.handleException(e);
		}
		return result;
	}
	public static String getCodesName(final String tablename) {
		int index = tablename.indexOf("_");
		if (index >= 0) {
			return "Codes" + tablename.substring(index);
		} else {
			return "Codes_" + tablename;
		}
	}
	public static boolean DBFilesDa() {
		return DBFilesDa(DBKernel.HSHDB_PATH);
	}
	public static boolean DBFilesDa(String path) {
		boolean result = false;
	    File f = new File(path + "DB.script");
	    if (!f.exists()) {
			f = new File(path + "DB.data");
		}
	    result = f.exists();
	    return result;
	}
	public static void doMNs(final MyDBTable table) {
		doMNs(table.getActualTable());
	}
	public static void doMNs(final MyTable table) {
		boolean dl = DBKernel.dontLog;
		DBKernel.dontLog = true;
		  Vector<String> listMNs = table.getListMNs();
		  if (listMNs != null) {
			  String tableName  = table.getTablename();
				// hier soll immer die ID drin stehen, die wird dann zur Darstellung der M:N Beziehung ausgelesen.
			  // Mach einfach für alle Zeilen, dauert ja nicht lange, oder?
			  for (int i=0;i<listMNs.size();i++) {
				  String feldname = listMNs.get(i);
				  DBKernel.sendRequest("UPDATE " + DBKernel.delimitL(tableName) + " SET " + DBKernel.delimitL(feldname) + "=" + DBKernel.delimitL("ID") +
						  " WHERE " + DBKernel.delimitL(feldname) + " IS NULL OR " +
						  DBKernel.delimitL(feldname) + "!=" + DBKernel.delimitL("ID"), false);			  
			  }
		  }	  
		DBKernel.dontLog = dl;
	  }

	public static int isDBVeraltet(final Login login) {
		//if (true) return JOptionPane.NO_OPTION;
		int result = JOptionPane.NO_OPTION;
		if (undoSupport.getUndoableEditListeners().length == 0) {
			if (debug) {
				MyLogger.handleMessage("UndoableEditListener added");
			}
			undoSupport.addUndoableEditListener(undoManager);
		}

		String dbVersion = getDBVersion();
		MyLogger.handleMessage("DBVersion: " + dbVersion);
		if (dbVersion == null || !dbVersion.equals(DBKernel.DBVersion)) {
			result = askVeraltetDBBackup(login);
		}
		/*
		ResultSet rs = getResultSet("SELECT " + delimitL("Katalogcodes") + " FROM " + delimitL("Methoden"), true);
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
		}
		catch (Exception e) {
			MyLogger.handleMessage("DB veraltet... mach ne neue!");
			result = askVeraltetDBBackup(login);
		}
		*/
		return result;
  }
	private static int askVeraltetDBBackup(final Login login) {
		int result = JOptionPane.YES_OPTION;
	    int retVal = JOptionPane.showConfirmDialog(login, "Die Datenbank ist veraltet und muss ersetzt werden.\nSoll zuvor ein Backup der alten Datenbank erstellt werden?",
	    		"Backup erstellen?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (retVal == JOptionPane.YES_OPTION) {if (!Backup.dbBackup(login)) {
			result = JOptionPane.CANCEL_OPTION;
		}}
		else if (retVal == JOptionPane.NO_OPTION) {
		    retVal = JOptionPane.showConfirmDialog(login, "Die Datenbank wirklich ohne Backup überschreiben?? Sicher?",
		    		"Sicher?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    if (retVal == JOptionPane.YES_OPTION) {
				;
			} else {
		    	return askVeraltetDBBackup(login);
		    }
		} else {
			result = JOptionPane.CANCEL_OPTION;
		}
		return result;
	}
	public static String getDBVersion() {
		String result = null;
		ResultSet rs = getResultSet("SELECT " + delimitL("Wert") + " FROM " + delimitL("Infotabelle") +
				" WHERE " + delimitL("Parameter") + " = 'DBVersion'", true);
		try {
			if (rs != null && rs.first()) {
				result = rs.getString(1);
			}
		}
		catch (Exception e) {
			MyLogger.handleException(e);
		}
		//System.out.println(result);
		return result;
	}
	public static void setDBVersion(final String dbVersion) {
		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Infotabelle") +
				" SET " + DBKernel.delimitL("Wert") + " = '" + dbVersion + "'" +
				" WHERE " + DBKernel.delimitL("Parameter") + " = 'DBVersion'", false);		
	}
	public static long getFileSize(final String filename) {
		File file = new File(filename);
		if (file == null || !file.exists() || !file.isFile()) {
			System.out.println("File doesn\'t exist");
			return -1;
		}
		return file.length();
	}
	/*
	public static boolean isDBL(MyTable myT, int column) {
		boolean result = false;
    	String[] mnTable = myT.getMNTable();
		if (column > 0 && mnTable != null && column-1 < mnTable.length && mnTable[column - 1] != null && mnTable[column - 1].equals("DBL")) result = true;
		return result;
	}
	*/
	public static boolean isNewDBL(final MyTable myT, final int column) {
		boolean result = false;
		MyTable[] myFs = myT.getForeignFields();
		if (column > 0 && myFs != null && column-1 < myFs.length && myFs[column - 1] != null && myFs[column - 1].getTablename().equals("DoubleKennzahlen")) {
			result = true;
		}
		return result;
	}
	public static void grantDefaults(final String tableName) {
		DBKernel.sendRequest("GRANT SELECT ON TABLE " + DBKernel.delimitL(tableName) + " TO " + DBKernel.delimitL("PUBLIC"), false);				
		if (tableName.startsWith("Codes_")) {
			DBKernel.sendRequest("GRANT SELECT ON TABLE " + DBKernel.delimitL(tableName) + " TO " + DBKernel.delimitL("WRITE_ACCESS"), false);				
		}
		else {
			DBKernel.sendRequest("GRANT SELECT, INSERT, UPDATE ON TABLE " + DBKernel.delimitL(tableName) + " TO " + DBKernel.delimitL("WRITE_ACCESS"), false);				
		}
		DBKernel.sendRequest("GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE " + DBKernel.delimitL(tableName) + " TO " + DBKernel.delimitL("SUPER_WRITE_ACCESS"), false);						
	}

	public static void openDBGUI() {
		final Connection connection = getLocalConn(true);
		try {
			connection.setReadOnly(DBKernel.prefs.getBoolean("PMM_LAB_SETTINGS_DB_RO", true));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		StartApp.go(connection);
	}
	public static String getInternalDefaultDBPath() {
		return ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() +
				System.getProperty("file.separator") + ".pmmlabDB" + System.getProperty("file.separator");
	}
	public static Connection getInternalKNIMEDB_LoadGui() {
		Connection result = null;
		try {
			// Create a file object from the URL
			String internalPath = DBKernel.prefs.get("PMM_LAB_SETTINGS_DB_PATH", getInternalDefaultDBPath());
			DBKernel.isServerConnection = DBKernel.isHsqlServer(internalPath);
			if (DBKernel.isServerConnection) {
			  	HSHDB_PATH = internalPath;
				try {
					//DBKernel.getNewServerConnection(login, pw, filename);		
					result = DBKernel.getDBConnection(DBKernel.prefs.get("PMM_LAB_SETTINGS_DB_USERNAME",""), 
							DBKernel.prefs.get("PMM_LAB_SETTINGS_DB_PASSWORD",""));
					createGui(result);
				}
				catch (Exception e) {e.printStackTrace();}
			}
			else {
				File incFileInternalDBFolder = new File(internalPath);
				if (!incFileInternalDBFolder.exists()) {
					if (!incFileInternalDBFolder.mkdirs()) return null;
				}
				if (incFileInternalDBFolder.list() == null) return null;
				// folder is empty? Create database!
				if (incFileInternalDBFolder.list().length == 0) {
					// Get the bundle this class belongs to.
					Bundle bundle = FrameworkUtil.getBundle(DBKernel.class);
					URL incURLfirstDB = bundle.getResource("org/hsh/bfr/db/res/firstDB.tar.gz");
					if (incURLfirstDB == null) { // incURLInternalDBFolder == null || 
						return null;
					}
					File incFilefirstDB = new File(FileLocator.toFileURL(incURLfirstDB).getPath());
					try {
						org.hsqldb.lib.tar.DbBackup.main(new String[]{
								"--extract",
								incFilefirstDB.getAbsolutePath(),
								incFileInternalDBFolder.getAbsolutePath()});
					}
					catch (Exception e) {
						throw new IllegalStateException("Creation of internal database not succeeded.", e);
					}
				}

				try {
				  	HSHDB_PATH = internalPath;
					result = getDBConnection(DBKernel.prefs.get("PMM_LAB_SETTINGS_DB_USERNAME", getTempSA(HSHDB_PATH)),
							DBKernel.prefs.get("PMM_LAB_SETTINGS_DB_PASSWORD", getTempSAPass(HSHDB_PATH)));
					if (result == null) result = getDBConnection(DBKernel.prefs.get("PMM_LAB_SETTINGS_DB_USERNAME", getTempSA(HSHDB_PATH, true)),
							DBKernel.prefs.get("PMM_LAB_SETTINGS_DB_PASSWORD", getTempSAPass(HSHDB_PATH, true)));

					createGui(result);
					// UpdateChecker
				  	String dbVersion = DBKernel.getDBVersion();
				  	if (!DBKernel.isServerConnection && (dbVersion == null || !dbVersion.equals(DBKernel.DBVersion))) {
						boolean dl = MainKernel.dontLog;
						MainKernel.dontLog = true;
					  	boolean isAdmin = DBKernel.isAdmin();
					  	if (!isAdmin) {
					  		DBKernel.closeDBConnections(false);
					  		DBKernel.getDefaultAdminConn();
					  	}
					  	
					  	if (DBKernel.getDBVersion() == null) {
					  		UpdateChecker.check4Updates_143_144();
					  		DBKernel.setDBVersion("1.4.4");
					  	}
					  	if (DBKernel.getDBVersion().equals("1.4.4")) {
					  		UpdateChecker.check4Updates_144_145();
					  		DBKernel.setDBVersion("1.4.5");
					  	}
					  	if (DBKernel.getDBVersion().equals("1.4.5")) {
					  		UpdateChecker.check4Updates_145_146();
					  		DBKernel.setDBVersion("1.4.6");
					  	}
					  	if (DBKernel.getDBVersion().equals("1.4.6")) {
					  		UpdateChecker.check4Updates_146_147(); 
					  		DBKernel.setDBVersion("1.4.7");
					  	}
					  	if (DBKernel.getDBVersion().equals("1.4.7")) {
					  		UpdateChecker.check4Updates_147_148(); 
					  		DBKernel.setDBVersion("1.4.8");
					  	}					  	
					  	if (DBKernel.getDBVersion().equals("1.4.8")) {
					  		UpdateChecker.check4Updates_148_149(); 
					  		DBKernel.setDBVersion("1.4.9");
					  	}
					  	if (DBKernel.getDBVersion().equals("1.4.9")) {
					  		UpdateChecker.check4Updates_149_150(); 
					  		DBKernel.setDBVersion("1.5.0");
					  	}
					  	if (DBKernel.getDBVersion().equals("1.5.0")) {
					  		UpdateChecker.check4Updates_150_151(); 
					  		DBKernel.setDBVersion("1.5.1");
					  	}
					  	if (DBKernel.getDBVersion().equals("1.5.1")) {
					  		UpdateChecker.check4Updates_151_152(); 
					  		DBKernel.setDBVersion("1.5.2");
					  	}
					  	if (DBKernel.getDBVersion().equals("1.5.2")) {
					  		UpdateChecker.check4Updates_152_153(); 
					  		DBKernel.setDBVersion("1.5.3");
					  	}
					  	if (DBKernel.getDBVersion().equals("1.5.3")) {
					  		UpdateChecker.check4Updates_153_154(); 
					  		DBKernel.setDBVersion("1.5.4");
					  	}
					  	if (DBKernel.getDBVersion().equals("1.5.4")) {
					  		UpdateChecker.check4Updates_154_155(); 
					  		DBKernel.setDBVersion("1.5.5");
					  	}
					  	if (DBKernel.getDBVersion().equals("1.5.5")) {
					  		UpdateChecker.check4Updates_155_156(); 
					  		DBKernel.setDBVersion("1.5.6");
					  	}
					  	if (DBKernel.getDBVersion().equals("1.5.6")) {
					  		UpdateChecker.check4Updates_156_157(); 
					  		DBKernel.setDBVersion("1.5.7");
					  	}
					  	if (DBKernel.getDBVersion().equals("1.5.7")) {
					  		UpdateChecker.check4Updates_157_158(); 
					  		DBKernel.setDBVersion("1.5.8");
					  	}
					  	if (DBKernel.getDBVersion().equals("1.5.8")) {
					  		UpdateChecker.check4Updates_158_159(); 
					  		DBKernel.setDBVersion("1.5.9");
					  	}
					  	
					  	if (!isAdmin) {
					  		DBKernel.closeDBConnections(false);
					  		DBKernel.getDBConnection();
					  		if (DBKernel.myList != null && DBKernel.myList.getMyDBTable() != null) {
					  			DBKernel.myList.getMyDBTable().setConnection(DBKernel.getDBConnection());
					  		}
					  	}
					  	MainKernel.dontLog = dl;
			  		}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		  	DBKernel.saveUP2PrefsTEMP(HSHDB_PATH);
		}
		catch (IOException e) {
			throw new IllegalStateException("Cannot locate necessary internal database path.", e);
		}
		return result;
	}
	public static void createGui(Connection conn) {
		MyDBTables.loadMyTables();
		try {
			if (DBKernel.myList == null && conn != null) {
	    	  	//Login login = new Login();
	  	    	MyDBTable myDB = new MyDBTable();
	  	    	myDB.initConn(conn);
	  	    	MyDBTree myDBTree = new MyDBTree();
				MyList myList = new MyList(myDB, myDBTree);
				DBKernel.myList = myList;
				myList.addAllTables();
		    	//login.loadMyTables(myList, null);
		    	
				MainFrame mf = new MainFrame(myList);
				DBKernel.mainFrame = mf;
				myList.setSelection(DBKernel.prefs.get("LAST_SELECTED_TABLE", "Versuchsbedingungen"));
				try {
					boolean full = Boolean.parseBoolean(DBKernel.prefs.get("LAST_MainFrame_FULL", "FALSE"));
					/*
					int w = Integer.parseInt(DBKernel.prefs.get("LAST_MainFrame_WIDTH", "1020"));
					int h = Integer.parseInt(DBKernel.prefs.get("LAST_MainFrame_HEIGHT", "700"));
					int x = Integer.parseInt(DBKernel.prefs.get("LAST_MainFrame_X", "0"));
					int y = Integer.parseInt(DBKernel.prefs.get("LAST_MainFrame_Y", "0"));
					DBKernel.mainFrame.setPreferredSize(new Dimension(w, h));
					DBKernel.mainFrame.setBounds(x, y, w, h);
					*/
					DBKernel.mainFrame.pack();
					DBKernel.mainFrame.setLocationRelativeTo(null);
					if (full) DBKernel.mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				}
				catch (Exception e) {}
			}					
		}
		catch (Exception he) {} // HeadlessException
	}
    public static String[] getItemListMisc(Connection conn) {
    	HashSet<String> hs = new HashSet<String>();
    	try {
    		ResultSet rs = null;
    		String sql = "SELECT " + DBKernel.delimitL("Parameter") + " FROM " + DBKernel.delimitL("SonstigeParameter");
    		rs = DBKernel.getResultSet(conn, sql, false);
			do {
				hs.add(rs.getString("Parameter"));    		
			} while (rs.next());
		}
    	catch (Exception e) {
			e.printStackTrace();
		}
    	return hs.toArray(new String[]{});
    }
    public static boolean mergeIDs(Connection conn, final String tableName, int oldID, int newID) {
		ResultSet rs = null;
		String sql = "SELECT FKTABLE_NAME, FKCOLUMN_NAME FROM INFORMATION_SCHEMA.SYSTEM_CROSSREFERENCE " +
				" WHERE PKTABLE_NAME = '" + tableName + "'";
		try {
			rs = DBKernel.getResultSet(conn, sql, false);
		    if (rs != null && rs.first()) {
		    	do {
		    		String fkt = rs.getObject("FKTABLE_NAME") != null ? rs.getString("FKTABLE_NAME") : "";
		    		String fkc = rs.getObject("FKCOLUMN_NAME") != null ? rs.getString("FKCOLUMN_NAME") : "";
		    		//System.err.println(tableName + " wird in " + fkt + "->" + fkc + " referenziert");
			    	if (!DBKernel.sendRequest(conn, "UPDATE " + DBKernel.delimitL(fkt) + " SET " + DBKernel.delimitL(fkc) + "=" + newID +
			    			" WHERE " + DBKernel.delimitL(fkc) + "=" + oldID, false, false)) return false;
		    	} while (rs.next());
		    	if (DBKernel.sendRequest(conn, "DELETE FROM " + DBKernel.delimitL(tableName) +
		    			" WHERE " + DBKernel.delimitL("ID") + "=" + oldID, false, false)) {
		    		return true;
		    	}
		    }
	    }
	    catch (Exception e) {MyLogger.handleException(e);}	
		return false;
	}
    public static int getUsagecountOfID(final String tableName, int id) {
    	int result = 0;
		ResultSet rs = DBKernel.getResultSet("SELECT FKTABLE_NAME, FKCOLUMN_NAME FROM INFORMATION_SCHEMA.SYSTEM_CROSSREFERENCE " +
				" WHERE PKTABLE_NAME = '" + tableName + "'", false);
		try {
		    if (rs != null && rs.first()) {
		    	do {
		    		String fkt = rs.getObject("FKTABLE_NAME") != null ? rs.getString("FKTABLE_NAME") : "";
		    		String fkc = rs.getObject("FKCOLUMN_NAME") != null ? rs.getString("FKCOLUMN_NAME") : "";
		    		//System.err.println(tableName + " wird in " + fkt + "->" + fkc + " referenziert");
			    	ResultSet rs2 = DBKernel.getResultSet("SELECT " + DBKernel.delimitL("ID") + " FROM " + DBKernel.delimitL(fkt) +
			    			" WHERE " + DBKernel.delimitL(fkc) + "=" + id, false);
			    	if (rs2 != null && rs2.last()) result += rs2.getRow();
		    	} while (rs.next());
		    }
	    }
	    catch (Exception e) {MyLogger.handleException(e);}		
		return result;
	}
    public static File getCopyOfInternalDB() {
		File temp = null;
		try {
			temp = File.createTempFile("firstDB",".tar.gz");
			InputStream in = DBKernel.class.getResourceAsStream("/org/hsh/bfr/db/res/firstDB.tar.gz");
			BufferedInputStream bufIn = new BufferedInputStream(in);
			BufferedOutputStream bufOut = null;
			try {
				bufOut = new BufferedOutputStream(new FileOutputStream(temp));
			}
			catch (FileNotFoundException e1) {MyLogger.handleException(e1);}

			byte[] inByte = new byte[4096];
			int count = -1;
			try {while ((count = bufIn.read(inByte))!=-1) {bufOut.write(inByte, 0, count);}}
			catch (IOException e) {MyLogger.handleException(e);}

			try {bufOut.close();}
			catch (IOException e) {MyLogger.handleException(e);}
			try {bufIn.close();}
			catch (IOException e) {MyLogger.handleException(e);}    	
		}
		catch (IOException e2) {
			e2.printStackTrace();
		}		
		return temp;
    }
    public static void convertEHEC2NewDB(String whichDB) {
    	System.err.println("convertEHEC2NewDB - " + whichDB + ":");
    	try {
        	Connection oldDB = getConnection("C:/Users/Armin/Desktop/krise/EHEC/old/" + whichDB + "/DB", "defad", "de6!§5ddy");
        	Connection newDB = getConnection("C:/Users/Armin/Desktop/krise/EHEC/" + whichDB + "/DB", "SA", "");
        	if (oldDB != null && newDB != null) {
	    		DBKernel.sendRequest(newDB, "DELETE FROM " + DBKernel.delimitL("Kontakte"), false, false);
        		ResultSet rs = DBKernel.getResultSet(oldDB, "SELECT * FROM " + DBKernel.delimitL("Kontakte"), false);
        		if (rs != null && rs.first()) {
        			do {
        				String bl = rs.getString("Bundesland");
        				boolean isBL = false;
        				if (bl != null) {
            				bl = getBL(bl);
            				isBL = bl.startsWith("_");        					
        				}
        	    		DBKernel.sendRequest(newDB, "INSERT INTO " + DBKernel.delimitL("Kontakte") + " VALUES (" + rs.getInt("ID") + "," +
        	    		getDBString(rs.getString("Name")) + "," + getDBString(rs.getString("Straße")) + "," + getDBString(rs.getString("Hausnummer")) + "," +
        	    		getDBString(rs.getString("Postfach")) + "," + getDBString(rs.getString("PLZ")) + "," + getDBString(rs.getString("Ort")) + "," +
        	    		getDBString(isBL ? bl.substring(1) : "NULL") + "," + getDBString(isBL ? rs.getString("Land") : bl) + "," + getDBString(rs.getString("Ansprechpartner")) + "," +
        	    		getDBString(rs.getString("Telefon")) + "," + getDBString(rs.getString("Fax")) + "," + getDBString(rs.getString("E-Mail")) + "," +
        	    		getDBString(rs.getString("Web-Site")) + "," + getDBString(rs.getString("Kommentar")) + ")", false, false);
        			} while (rs.next());
        		}

	    		DBKernel.sendRequest(newDB, "DELETE FROM " + DBKernel.delimitL("Station"), false, false);    				
        		rs = DBKernel.getResultSet(oldDB, "SELECT * FROM " + DBKernel.delimitL("Produzent"), false);
        		if (rs != null && rs.first()) {
        			do {
        				int id = rs.getInt("ID");
        				boolean fe = whichDB.equals("Cluster") && (id == 3 || id == 4 || id == 5 || id == 8 || id == 10 || id == 17); // die 5 definierten Cluster - Sodexo doppelt
        				if (!fe) fe = whichDB.equals("Samen") && (id == 68); // Bienenbüttel=20, Jardiland=68
        	    		DBKernel.sendRequest(newDB, "INSERT INTO " + DBKernel.delimitL("Station") + " (" + DBKernel.delimitL("ID") + "," +
        	    			DBKernel.delimitL("Kontaktadresse") + "," + DBKernel.delimitL("Betriebsnummer") + "," + DBKernel.delimitL("FallErfuellt") +
        	    			"," + DBKernel.delimitL("Kommentar") +
        	    			") VALUES (" + rs.getInt("ID") + "," +
        	    			rs.getInt("Kontaktadresse") + "," + getDBString(rs.getString("Betriebsnummer")) + "," + (fe ? "TRUE" : "NULL") + "," +
        	    			getDBString(rs.getString("Kommentar")) + ")", false, false);
        			} while (rs.next());
        		}

	    		DBKernel.sendRequest(newDB, "DELETE FROM " + DBKernel.delimitL("Produktkatalog"), false, false);    				
        		rs = DBKernel.getResultSet(oldDB, "SELECT * FROM " + DBKernel.delimitL("Produzent_Artikel"), false);
        		if (rs != null && rs.first()) {
        			do {
        	    		DBKernel.sendRequest(newDB, "INSERT INTO " + DBKernel.delimitL("Produktkatalog") + " (" + DBKernel.delimitL("ID") + "," +
            	    			DBKernel.delimitL("Station") + "," + DBKernel.delimitL("Artikelnummer") + "," + DBKernel.delimitL("Bezeichnung") +
            	    			") VALUES (" + rs.getInt("ID") + "," +
            	    			rs.getInt("Produzent") + "," + getDBString(rs.getString("Artikelnummer")) + "," +
            	    			getDBString(rs.getString("Bezeichnung")) + ")", false, false);
        			} while (rs.next());
        		}

        		HashMap<String, Integer> chargeLieferung = new HashMap<String, Integer>(); 
        		HashMap<Integer, Integer> chargeLieferungID = new HashMap<Integer, Integer>(); 
        		HashMap<Integer, Integer> lieferLieferungID = new HashMap<Integer, Integer>(); 
        	    DBKernel.sendRequest(newDB, "DELETE FROM " + DBKernel.delimitL("Chargen"), false, false);    				
        		DBKernel.sendRequest(newDB, "DELETE FROM " + DBKernel.delimitL("Lieferungen"), false, false);    				
        		rs = DBKernel.getResultSet(oldDB, "SELECT * FROM " + DBKernel.delimitL("Artikel_Lieferung"), false);
        		if (rs != null && rs.first()) {
        			do {
        				String chargenNr = rs.getString("ChargenNr");
        				String mhd = rs.getString("MHD");
        				if (chargenNr != null) chargenNr = chargenNr.trim();
        				// Achtung: hier typische Microsoft-CopyPaste-Änderung eines Strings... ala " (1)" ans Ende geschrieben.
        				int index = chargenNr.lastIndexOf(" (");
        				if (index > 0 && chargenNr.endsWith(")") && index == chargenNr.length() - 4) chargenNr = chargenNr.substring(0, index);
        				if (mhd != null) mhd = mhd.trim();
    					mhd = getDatum(mhd);
        				int lieferID = rs.getInt("ID");
        				int artikelID = rs.getInt("Artikel");
        				int chargenID = lieferID;
        				String hashKey = artikelID + "_" + chargenNr + "_" + mhd;
        				if (chargeLieferung.containsKey(hashKey)) chargenID = chargeLieferung.get(hashKey);
        				else {
        					if (chargenNr != null && !chargenNr.replace(",", "").trim().isEmpty()) chargeLieferung.put(hashKey, lieferID);
            				//if (DBKernel.getValue(newDB, "Chargen", new String[]{"Artikel","ChargenNr","MHD"}, new String[]{""+artikelID,cstr,dstr}, "ID") == null) {
                	    		DBKernel.sendRequest(newDB, "INSERT INTO " + DBKernel.delimitL("Chargen") + " (" + DBKernel.delimitL("ID") + "," +
                    	    			DBKernel.delimitL("Artikel") + "," + DBKernel.delimitL("ChargenNr") + "," + DBKernel.delimitL("MHD") +
                    	    			") VALUES (" + lieferID + "," +
                    	    			artikelID + "," + getDBString(chargenNr) + "," + getDBString(mhd) + ")", false, false);            					
            				//}
        				}
        				chargeLieferungID.put(lieferID, chargenID);

        	    		Integer empf = rs.getInt("Empfänger");
        	    		String lstr = getDatum(rs.getString("Lieferdatum"));
        	    		Integer oldLieferID = (Integer) DBKernel.getValue(newDB, "Lieferungen", new String[]{"Charge","Lieferdatum","Empfänger"}, new String[]{""+chargenID,lstr,empf==null?null:""+empf}, "ID"); 
        				if (oldLieferID == null) {
            	    		DBKernel.sendRequest(newDB, "INSERT INTO " + DBKernel.delimitL("Lieferungen") + " (" + DBKernel.delimitL("ID") + "," +
                	    			DBKernel.delimitL("Charge") + "," + DBKernel.delimitL("Lieferdatum") + "," + DBKernel.delimitL("#Units1") +
                	    			 "," + DBKernel.delimitL("BezUnits1") + "," + DBKernel.delimitL("#Units2") +
                	    			 "," + DBKernel.delimitL("BezUnits2") + "," + DBKernel.delimitL("Unitmenge") +
                	    			 "," + DBKernel.delimitL("UnitEinheit") + "," + DBKernel.delimitL("Empfänger") +
                	    			") VALUES (" + lieferID + "," +
                	    			chargenID + "," + getDBString(lstr) + "," + getDouble(rs.getString("#Units1")) + "," +
                	    			getDBString(rs.getString("BezUnits1")) + "," + getDouble(rs.getString("#Units2")) + "," + getDBString(rs.getString("BezUnits2")) + "," +
                	    			getDouble(rs.getString("Unitmenge")) + "," + getDBString(rs.getString("UnitEinheit")) + "," +
                	    			(empf == 0 ? "NULL" : empf) + ")", false, false);        					
        					lieferLieferungID.put(lieferID, lieferID);
        				}
        				else {
        					lieferLieferungID.put(lieferID, oldLieferID);
        				}
        			} while (rs.next());
        		}

	    		DBKernel.sendRequest(newDB, "DELETE FROM " + DBKernel.delimitL("ChargenVerbindungen"), false, false);    				
        		rs = DBKernel.getResultSet(oldDB, "SELECT * FROM " + DBKernel.delimitL("Lieferung_Lieferungen"), false);
        		if (rs != null && rs.first()) {
        			do {
        				int zulieferID = lieferLieferungID.get(rs.getInt("Vorprodukt"));
        				int lieferID = lieferLieferungID.get(rs.getInt("Artikel_Lieferung"));
        				int chargenID = chargeLieferungID.get(lieferID);
        				if (DBKernel.getValue(newDB, "ChargenVerbindungen", new String[]{"Zutat","Produkt"}, new String[]{""+zulieferID,""+chargenID}, "ID") == null) {
            	    		DBKernel.sendRequest(newDB, "INSERT INTO " + DBKernel.delimitL("ChargenVerbindungen") + " (" +
                	    			DBKernel.delimitL("Zutat") + "," + DBKernel.delimitL("Produkt") +
                	    			") VALUES (" + zulieferID + "," + chargenID + ")", false, false);        					
        				}
        			} while (rs.next());
        		}
        		DBKernel.sendRequest(oldDB, "SHUTDOWN", false, false);
        		DBKernel.sendRequest(newDB, "SHUTDOWN", false, false);
        	}
    	}
    	catch (Exception e) {e.printStackTrace();}
    	System.err.println("Fin!");
    }
    private static String getBL(String strVal) {
    	if (strVal == null) return strVal;
    	if (strVal.equals("BW") || strVal.equals("Baden-Württemberg")) return "_Baden-Württemberg";
    	else if (strVal.equals("BY") || strVal.equals("Bayern")) return "_Bayern";
    	else if (strVal.equals("BE") || strVal.equals("Berlin")) return "_Berlin";
    	else if (strVal.equals("BB") || strVal.equals("Brandenburg")) return "_Brandenburg";
    	else if (strVal.equals("HB") || strVal.equals("Bremen")) return "_Bremen";
    	else if (strVal.equals("HH") || strVal.equals("Hamburg")) return "_Hamburg";
    	else if (strVal.equals("HE") || strVal.equals("Hessen")) return "_Hessen";
    	else if (strVal.equals("MV") || strVal.equals("Mecklenburg-Vorpommern")) return "_Mecklenburg-Vorpommern";
    	else if (strVal.equals("NI") || strVal.equals("Niedersachsen")) return "_Niedersachsen";
    	else if (strVal.equals("NW") || strVal.equals("Nordrhein-Westfalen")) return "_Nordrhein-Westfalen";
    	else if (strVal.equals("RP") || strVal.equals("Rheinland-Pfalz")) return "_Rheinland-Pfalz";
    	else if (strVal.equals("SL") || strVal.equals("Saarland")) return "_Saarland";
    	else if (strVal.equals("SN") || strVal.equals("Sachsen")) return "_Sachsen";
    	else if (strVal.equals("ST") || strVal.equals("Sachsen-Anhalt")) return "_Sachsen-Anhalt";
    	else if (strVal.equals("SH") || strVal.equals("Schleswig-Holstein")) return "_Schleswig-Holstein";
    	else if (strVal.equals("TH") || strVal.equals("Thüringen")) return "_Thüringen";
    	else return strVal;
    }
	private static String getDBString(String strVal) {
		if (strVal == null || strVal.isEmpty()) return "NULL";
		else return "'" + strVal + "'";
	}
	private static Double getDouble(String strVal) {
		Double val = null;
		try {
			val = Double.valueOf(strVal);						
		}
		catch (Exception e) {}
		return val;
	}
	private static String getDatum(String strVal) { 
	    SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd"); //  HH:mm:ss
	    if (!strVal.isEmpty()) {
			if (strVal.equals("to germinate before Dec 2013")) strVal = "30.11.2013";
			else if (strVal.equals("January2011.")) strVal = "01/2011";
			else if (strVal.equals("23.05.")) strVal = "23.05.2011";
			else if (strVal.trim().equals("zwischen 14.05.2011 und 19.05.2011")) strVal = "14.05.2011";
			Date parsedUtilDate = parseDate(strVal, "dd.MM.yyyy");
			if (parsedUtilDate == null) {
				parsedUtilDate = parseDate(strVal, "MM/yyyy");
				//if (parsedUtilDate != null) System.err.println(strVal + "->" + outFormat.format(parsedUtilDate));
			}
			if (parsedUtilDate == null) parsedUtilDate = parseDate(strVal, "yyyy");
			if (parsedUtilDate != null) return outFormat.format(parsedUtilDate);
		    System.err.println("getDatum -> " + strVal);
	    }
		return null;
	}
	private static Date parseDate(String strVal, String format) {
		DateFormat inFormat = new SimpleDateFormat(format);
		try {
			Date parsedUtilDate = inFormat.parse(strVal);
			return parsedUtilDate;
		}
		catch (ParseException e1) {}
		return null;
	}
    private static Connection getConnection(String dbFile,String dbUsername,String dbPassword) {
        Connection result = null;
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver").newInstance();
            String connStr = "jdbc:hsqldb:file:" + dbFile;
        	result = DriverManager.getConnection(connStr,dbUsername, dbPassword);  
        }
    	catch (Exception e) {e.printStackTrace();}
        return result;
    }
    public static void getKnownIDs4PMM(Connection conn, HashMap<Integer, Integer> foreignDbIds, String tablename, String rowuuid) {
		  String sql = "SELECT " + DBKernel.delimitL("TableID") + "," + DBKernel.delimitL("SourceID") +
				  " FROM " + DBKernel.delimitL("DataSource") + " WHERE ";
		  sql += DBKernel.delimitL("Table") + "=" + "'" + tablename + "' AND";
		  sql += DBKernel.delimitL("SourceDBUUID") + "=" + "'" + rowuuid + "';";

		  ResultSet rs = DBKernel.getResultSet(conn, sql, true);
		  try {
			  if (rs != null && rs.first()) {
				  do {
					  if (rs.getObject("SourceID") != null && rs.getObject("TableID") != null) {
						  foreignDbIds.put(rs.getInt("SourceID"), rs.getInt("TableID"));						  
					  }
				  } while(rs.next());
			  }
		  }
		  catch (Exception e) {MyLogger.handleException(e);}
    }
    public static void setKnownIDs4PMM(Connection conn, HashMap<Integer, Integer> foreignDbIds, String tablename, String rowuuid) {
    	for (Integer sID : foreignDbIds.keySet()) {
			Object id = DBKernel.getValue(conn, "DataSource", new String[] {"Table","SourceDBUUID", "SourceID"}, new String[] {tablename, rowuuid, sID+""}, "TableID");
    		if (id == null) {
    			String sql = "INSERT INTO " + DBKernel.delimitL("DataSource") +
    					" (" + DBKernel.delimitL("Table") + "," + DBKernel.delimitL("TableID") + "," +
    					DBKernel.delimitL("SourceDBUUID") + "," + DBKernel.delimitL("SourceID") +
    					") VALUES ('" + tablename + "'," + foreignDbIds.get(sID) + ",'" + rowuuid + "'," + sID + ");";
    			DBKernel.sendRequest(conn, sql, false, false);
    		}
    	}
    }
}
