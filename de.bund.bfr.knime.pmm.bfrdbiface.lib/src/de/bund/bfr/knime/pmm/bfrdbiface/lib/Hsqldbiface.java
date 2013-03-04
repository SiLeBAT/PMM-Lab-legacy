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
				
				if( conn == null )
					throw new NullPointerException( "Did not get a connection from DBKernel.getNewLocalConnection." );
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	private void createUser(String path, String login, String pw) throws Exception {
		
		Connection conn;
		
		conn = DBKernel.getDefaultAdminConn(path, false);
		if (DBKernel.countUsers(conn, false) == 0) {
			conn.setReadOnly(false);
			pushUpdate("INSERT INTO " + DBKernel.delimitL("Users") +
					"(" + DBKernel.delimitL("Username") + "," + DBKernel.delimitL("Zugriffsrecht") +
					") VALUES ('" + login + "', " + Users.SUPER_WRITE_ACCESS + ")");
			pushUpdate("ALTER USER " + DBKernel.delimitL(login) + " SET PASSWORD '" + pw + "';");
			conn.setReadOnly(DBKernel.prefs.getBoolean("PMM_LAB_SETTINGS_DB_RO", true));
		}
		conn.close();
	}
	public Connection getConnection() {
		return conn;
	}
	
	public void pushUpdate( String query ) throws SQLException {		
		String[] q = new String[] { query };
		pushUpdate( q );
	}
	
	public void pushUpdate( String[] query ) throws SQLException {
		
		Statement statement = conn.createStatement();
		for( String q : query ) {
			//System.out.println( q );
			statement.addBatch( q );
		}
		conn.setAutoCommit( false );
		statement.executeBatch();
		conn.setAutoCommit( true );
		/* for( String q : query )
			statement.execute( q ); */
		

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
