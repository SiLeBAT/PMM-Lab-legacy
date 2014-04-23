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

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.hsqldb.server.Server;

/**
 * @author Armin
 *
 */

public class MainKernel {

	public static long triggerFired = System.currentTimeMillis();
	static boolean dontLog = false;
	private static boolean isServer = false;
	private static Server s = null;
	
	private static String dbFolder = "/opt/hsqldb/data/";
	private static String bkpFolder = "/opt/hsqldb/backup/";
	private static String logFolder = "/var/log/hsqldb/";
	/*
	private static String dbFolder = "C:/Dokumente und Einstellungen/Weiser/Desktop/";
	private static String bkpFolder = "C:/Dokumente und Einstellungen/Weiser/.localHSH/BfR/LOGs/";
	private static String logFolder = "C:/Dokumente und Einstellungen/Weiser/.localHSH/BfR/LOGs/";
	*/
	private static String[][] dbDefs = new String[][] {
		//{"krise_145","krise_145","SA","de6!§5ddy"},
		{"silebat_DB","silebat_DB","defad","de6!§5ddy"},
		{"silebat_DB_test","silebat_DB_test","defad","de6!§5ddy"}//,
	};
	
	public static void main(final String[] args) { // Servervariante
		isServer = true;
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("start")) {
				MyLogger.setup(logFolder + System.currentTimeMillis() + ".log");
				s = new Server();
				//s.setDaemon(true);
				for (int i=0;i<dbDefs.length;i++) {
					s.setDatabaseName(i, dbDefs[i][0]);
					s.setDatabasePath(i, dbFolder + dbDefs[i][1] + "/DB");					
				}
				s.start();
				s.setNoSystemExit(false);
				MyLogger.handleMessage("Server connected!");
				new Wecker(24).start();
			}
			else if (args[0].equalsIgnoreCase("stop")) {
				shutdownServer();
			}
			else {
				Server.main(args);
			}
		}
    }
	private static void shutdownServer() {
		for (int i=0;i<dbDefs.length;i++) {
    	    try {
    	    	Connection conn = getDefaultAdminConn(i);
    	    	//defragDB(conn);// wir machen hier lieber 'CHECKPOINT DEFRAG', weil 'CHECKPOINT DEFRAG' im Gegensatz zu SHUTDOWN COMPACT bisher noch keine outofmemory Exception geworfen hat
            	MainKernel.sendRequest("SHUTDOWN", false, conn);
    	    }
    	    catch (Exception e) {
    	    	MyLogger.handleException(e);
    	    	System.exit(1);
    	    }
		}
		System.exit(0);		
	}
	/*
	private static void defragDB(final Connection conn) {
    	MyLogger.handleMessage("start CHECKPOINT DEFRAG!");
    	MainKernel.sendRequest("CHECKPOINT DEFRAG", false, conn);
    	MyLogger.handleMessage("fin CHECKPOINT DEFRAG!");		
	}
	*/
	static void dbBackup() {
		for (int i=0;i<dbDefs.length;i++) {
    	    try {
    	    	Connection conn = getDefaultAdminConn(i);
    	    	//defragDB(conn);
	    		Calendar cal = Calendar.getInstance();
	    		cal.setTime(new Date());
	    		int day = cal.get(Calendar.DAY_OF_MONTH);
    	    	String backupFile = bkpFolder + dbDefs[i][0] + "_" + day + ".tar.gz"; // System.currentTimeMillis()		
    	    	File f = new File(backupFile);
    	    	if (f.exists()) {
    	    		f.delete();
    	    		System.gc();
    	    	}
            	MainKernel.sendRequest("BACKUP DATABASE TO '" + backupFile + "' BLOCKING", false, conn);
    	    	System.gc();
    	    }
    	    catch (Exception e) {
    	    	MyLogger.handleException(e);
    	    }
		}
      }
	private static Connection getDefaultAdminConn(final int index) throws Exception {
	    Connection result = null;
	    Class.forName("org.hsqldb.jdbc.JDBCDriver").newInstance();
	    String connStr = "jdbc:hsqldb:hsql://localhost/" + dbDefs[index][0];
	    try {
	    	result = DriverManager.getConnection(connStr, dbDefs[index][2], dbDefs[index][3]);	    			
	    }
	    catch(Exception e) {
	    	MyLogger.handleException(e);
	    }
	    return result;
	}

	static boolean isServer() {
		return isServer;
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
	    	ResultSet rs = stmt.executeQuery("SELECT MAX(" + delimitL("ID") + ") FROM " + delimitL("ChangeLog"));
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
	/*
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
	*/
	protected static boolean insertIntoChangeLog(final String tablename, final Object[] rowBefore, final Object[] rowAfter) {
		return insertIntoChangeLog(tablename, rowBefore, rowAfter, false);
	}
	private static boolean insertIntoChangeLog(final String tablename, final Object[] rowBefore, final Object[] rowAfter, final boolean suppressWarnings) {
		if (dontLog) {
			return true;
		}
		else {
			boolean diff = different(rowBefore, rowAfter); 
		    if (!diff) {
				return true;
			}
		    boolean result = false;
		    try {
		    	Connection conn = getDefaultConnection();
		    	String username = getUsername();
		    	PreparedStatement ps = conn.prepareStatement("INSERT INTO " + delimitL("ChangeLog") +
			      		" (" + delimitL("ID") + ", " + delimitL("Zeitstempel") + ", " + delimitL("Username") + ", " +
			      		delimitL("Tabelle") + ", " + delimitL("TabellenID") + ", " +
			      		delimitL("Alteintrag") + ") VALUES (NEXT VALUE FOR " + delimitL("ChangeLogSEQ") + ", ?, ?, ?, ?, ?)");
	
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
		
				result = true;
		    }
		    catch (Exception e) {
		    	if (!suppressWarnings) {
		    		MyLogger.handleException(e);
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
	static String getUsername() {
		  	String username = "";
			try {
				Connection lconn = getDefaultConnection();
				if (lconn != null) {
					username = lconn.getMetaData().getUserName();
				}
			}
			catch (SQLException e) {
				MyLogger.handleException(e);
			} 
		  	return username;
		  }
	  private static Connection getDefaultConnection() {
		  Connection result = null;
		    String connStr = "jdbc:default:connection";
		    try {
		    	result = DriverManager.getConnection(connStr);
		    }
		    catch(Exception e) {
		    	MyLogger.handleException(e);
		    }
		    return result;
	  }
	  static String delimitL(final String name) {
		    String newName = name.replace("\"", "\"\"");
		    return "\"" + newName + "\"";
		  }
	  static boolean sendRequest(final String sql, final boolean suppressWarnings) {
		  return sendRequest(sql, suppressWarnings, null);
	  }
	  private static boolean sendRequest(final String sql, final boolean suppressWarnings, Connection conn) {
		  boolean result = false;
		    try {
		    	if (conn == null) {
					conn = getDefaultConnection();
				}
		      Statement anfrage = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		      anfrage.execute(sql);
		      result = true;
		    }
		    catch (Exception e) {
		      if (!suppressWarnings) {
		    	  MyLogger.handleMessage(sql);
		        MyLogger.handleException(e);
		      }
		    }
		    return result;
		  }
}
