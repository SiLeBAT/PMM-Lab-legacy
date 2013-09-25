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
package de.bund.bfr.knime.pmm.bfrdbiface.lib;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLWarning;
import java.util.UUID;

import org.hsh.bfr.db.Backup;
import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.Users;

public class Hsqldbiface {
	
	protected Connection conn;
	private String uuid = null;
	
	public Hsqldbiface( Connection conn ) {
	
		if( conn == null )
			throw new NullPointerException( "Connection must not be null." );
		
		this.conn = conn;
		
	}
	
	public Hsqldbiface( String filename, String login, String pw ) throws ClassNotFoundException, SQLException {
		
		DBKernel.isServerConnection = DBKernel.isHsqlServer(filename);
		if (DBKernel.isServerConnection) {
			try {
				conn = DBKernel.getNewServerConnection(login, pw, filename);	
				
				if( conn == null )
					throw new NullPointerException( "Did not get a connection from DBKernel.getNewServerConnection()." );
			}
			catch (Exception e) {throw new SQLException(e.getMessage());}
		}
		else {
			try {
				String path = filename.endsWith(System.getProperty("file.separator") + "DB") ? filename.substring(0, filename.length() - 2) : filename;
				if (!path.endsWith(System.getProperty("file.separator"))) path += System.getProperty("file.separator");
				if (!DBKernel.DBFilesDa(path)) {
					File temp = DBKernel.getCopyOfInternalDB();
					if (!Backup.doRestore(path, null, temp, true, false)) {
						
					}
				}
				conn = DBKernel.getNewLocalConnection(login, pw, path + "DB");
				if (conn == null) {
					createUser(path, login, pw);
					conn = DBKernel.getNewLocalConnection(login, pw, path + "DB");
				}
				
				if( conn == null ) throw new NullPointerException( "Did not get a connection from DBKernel.getNewLocalConnection." );				
				//conn = check4DbUpdate(conn, path, login, pw);
				//if( conn == null ) throw new NullPointerException( "Did not get a connection from DBKernel.getNewLocalConnection." );
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	/*
	private Connection check4DbUpdate(Connection conn, String dbPath, String login, String pw) throws Exception {
	  	String dbVersion = DBKernel.getDBVersion(conn);
	  	if (!DBKernel.isServerConnection && (dbVersion == null || !dbVersion.equals(DBKernel.DBVersion))) {
		  	boolean dl = MainKernel.dontLog;
			MainKernel.dontLog = true;
		  	boolean isAdmin = DBKernel.isAdmin(conn, login);
		  	if (!isAdmin) {
		  		conn.close();
		  	    conn = DBKernel.getNewLocalConnection(DBKernel.getTempSA(dbPath), DBKernel.getTempSAPass(dbPath), dbPath + "DB");
			    if (conn == null) conn = DBKernel.getNewLocalConnection(DBKernel.getTempSA(dbPath, true), DBKernel.getTempSAPass(dbPath, true), dbPath + "DB");
		  	}
		  	
		  	if (dbVersion == null || dbVersion.equals("1.4.3")) {
		  		UpdateChecker.check4Updates_143_144(conn);
		  		DBKernel.setDBVersion(conn, "1.4.4");
		  	}
		  	if (dbVersion.equals("1.4.4")) {
		  		UpdateChecker.check4Updates_144_145();
		  		DBKernel.setDBVersion(conn, "1.4.5");
		  	}
		  	if (dbVersion.equals("1.4.5")) {
		  		UpdateChecker.check4Updates_145_146();
		  		DBKernel.setDBVersion(conn, "1.4.6");
		  	}
		  	if (dbVersion.equals("1.4.6")) {
		  		UpdateChecker.check4Updates_146_147(); 
		  		DBKernel.setDBVersion(conn, "1.4.7");
		  	}
		  	if (dbVersion.equals("1.4.7")) {
		  		UpdateChecker.check4Updates_147_148(); 
		  		DBKernel.setDBVersion(conn, "1.4.8");
		  	}					  	
		  	if (dbVersion.equals("1.4.8")) {
		  		UpdateChecker.check4Updates_148_149(); 
		  		DBKernel.setDBVersion(conn, "1.4.9");
		  	}
		  	if (dbVersion.equals("1.4.9")) {
		  		UpdateChecker.check4Updates_149_150(); 
		  		DBKernel.setDBVersion(conn, "1.5.0");
		  	}
		  	if (dbVersion.equals("1.5.0")) {
		  		UpdateChecker.check4Updates_150_151(); 
		  		DBKernel.setDBVersion(conn, "1.5.1");
		  	}
		  	if (dbVersion.equals("1.5.1")) {
		  		UpdateChecker.check4Updates_151_152(); 
		  		DBKernel.setDBVersion(conn, "1.5.2");
		  	}
		  	if (dbVersion.equals("1.5.2")) {
		  		UpdateChecker.check4Updates_152_153(); 
		  		DBKernel.setDBVersion(conn, "1.5.3");
		  	}
		  	if (dbVersion.equals("1.5.3")) {
		  		UpdateChecker.check4Updates_153_154(); 
		  		DBKernel.setDBVersion(conn, "1.5.4");
		  	}
		  	if (dbVersion.equals("1.5.4")) {
		  		UpdateChecker.check4Updates_154_155(); 
		  		DBKernel.setDBVersion(conn, "1.5.5");
		  	}
		  	if (dbVersion.equals("1.5.5")) {
		  		UpdateChecker.check4Updates_155_156(); 
		  		DBKernel.setDBVersion(conn, "1.5.6");
		  	}
		  	if (dbVersion.equals("1.5.6")) {
		  		UpdateChecker.check4Updates_156_157(); 
		  		DBKernel.setDBVersion(conn, "1.5.7");
		  	}
		  	if (dbVersion.equals("1.5.7")) {
		  		UpdateChecker.check4Updates_157_158(); 
		  		DBKernel.setDBVersion(conn, "1.5.8");
		  	}
		  	if (dbVersion.equals("1.5.8")) {
		  		UpdateChecker.check4Updates_158_159(); 
		  		DBKernel.setDBVersion(conn, "1.5.9");
		  	}
		  	if (dbVersion.equals("1.5.9")) {
		  		UpdateChecker.check4Updates_159_160(); 
		  		DBKernel.setDBVersion(conn, "1.6.0");
		  	}
		  	
		  	if (!isAdmin) {
		  		conn.close();
		  		conn = DBKernel.getNewLocalConnection(login, pw, dbPath + "DB");
		  	}
		  	MainKernel.dontLog = dl;			  		
	  	}
	  	return conn;
	}
	*/
	private void createUser(String path, String login, String pw) throws Exception {		
		Connection conn = DBKernel.getDefaultAdminConn(path, true);
		if (DBKernel.countUsers(conn, false) == 0) {
			conn.setReadOnly(false);
			pushUpdate("INSERT INTO " + DBKernel.delimitL("Users") +
					"(" + DBKernel.delimitL("Username") + "," + DBKernel.delimitL("Zugriffsrecht") +
					") VALUES ('" + login + "', " + Users.SUPER_WRITE_ACCESS + ")", conn);
			pushUpdate("ALTER USER " + DBKernel.delimitL(login) + " SET PASSWORD '" + pw + "';", conn);
			conn.setReadOnly(DBKernel.prefs.getBoolean("PMM_LAB_SETTINGS_DB_RO", true));
		}
		conn.close();
	}
	public Connection getConnection() {
		return conn;
	}
	public void pushUpdate(String query) throws SQLException {		
		pushUpdate(query, conn);
	}
	private void pushUpdate(String query, Connection conn) throws SQLException {	
		String[] q = new String[] { query };
		pushUpdate(q, conn);
	}
	
	private void pushUpdate(String[] query, Connection conn) throws SQLException {		
		Statement statement = conn.createStatement();
		for (String q : query) {
			//System.out.println( q );
			statement.addBatch( q );
		}
		conn.setAutoCommit( false );
		statement.executeBatch();
		conn.setAutoCommit( true );
		statement.close();			
	}
	
	public ResultSet pushQuery(String query) throws SQLException {	
		return pushQuery(query, false);
	}
	public ResultSet pushQuery(String query, boolean inclBackwardResultSets) throws SQLException {		
		Statement statement = inclBackwardResultSets ? conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY) : conn.createStatement();
		//System.err.println(query);
		ResultSet result = statement.executeQuery(query);
		
		SQLWarning warn = statement.getWarnings();
		if (warn != null) System.out.println(warn);

		// statement.close();
		
		return result;
	}

	public String getDBUUID() throws SQLException {
		if (uuid != null) return uuid;
		String result = null;
		ResultSet rs = pushQuery("SELECT \"Wert\" FROM \"Infotabelle\" WHERE \"Parameter\" = 'DBuuid'");
		if (rs != null && rs.next()) {
			result = rs.getString(1);
		}
		if (result == null) {
			setDBUUID(UUID.randomUUID().toString());
			result = getDBUUID();
		}
		uuid = result;
		return result;
	}
	private void setDBUUID(final String uuid) throws SQLException {
		conn.setReadOnly(false);
		pushUpdate("INSERT INTO \"Infotabelle\" (\"Parameter\",\"Wert\") VALUES ('DBuuid','" + uuid + "')");
		conn.setReadOnly(DBKernel.prefs.getBoolean("PMM_LAB_SETTINGS_DB_RO", true));
	}
}
