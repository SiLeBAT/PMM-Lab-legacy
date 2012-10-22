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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLWarning;
import java.util.UUID;

public class Hsqldbiface {
	
	protected Connection conn;
	private String uuid = null;
	
	public Hsqldbiface( Connection conn ) { this.conn = conn; }
	
	public Hsqldbiface( String filename, String login, String pw ) throws ClassNotFoundException, SQLException {
		
		Class.forName( "org.hsqldb.jdbc.JDBCDriver" );
		// Class.forName( "org.sqlite.JDBC" );
		
		conn = DriverManager.getConnection(
				"jdbc:hsqldb:file:"
				// "jdbc:sqlite:"
				+filename+";shutdown=true", login, pw );
		
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
	
	public ResultSet pushQuery( String query ) throws SQLException {		
		Statement statement = conn.createStatement();
		//System.err.println(query);
		ResultSet result = statement.executeQuery( query );
		
		SQLWarning warn = statement.getWarnings();
		if( warn != null ) System.out.println( warn );

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
		pushUpdate("INSERT INTO \"Infotabelle\" (\"Parameter\",\"Wert\") VALUES ('DBuuid','" + uuid + "')");
	}
}
