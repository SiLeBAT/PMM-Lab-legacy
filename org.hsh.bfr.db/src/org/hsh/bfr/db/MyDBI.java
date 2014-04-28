package org.hsh.bfr.db;

import java.awt.Dimension;
import java.awt.Frame;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedHashMap;

import javax.swing.JFrame;

import org.hsh.bfr.db.db.XmlLoader;
import org.hsh.bfr.db.gui.InfoBox;


public abstract class MyDBI {
	public abstract LinkedHashMap<String, MyTable> getAllTables();
	public abstract LinkedHashMap<Integer, String> getTreeStructure();
	public abstract LinkedHashMap<String, int[]> getKnownCodeSysteme();	
	public abstract String getSA();
	public abstract String getSAP();
	public abstract void setSA_P(String user, String pass);
	public abstract String getDbPath();
	public abstract void addData();
	public abstract void addViews();
	public abstract void recreateTriggers();
	public abstract void updateCheck(final String fromVersion, final String toVersion);
	public abstract boolean isReadOnly();
	public abstract String getSoftwareVersion();
	public abstract LinkedHashMap<Object, String> getHashMap(final String key);
	
	public static MyDBI loadDB(String filename) {
		MyDBI result = null;
  		Object o = XmlLoader.getObjectFromFile(filename);
  		if (o instanceof MyDBI) result = (MyDBI) o;	
  		return result;
	}
	private static void saveDB(String filename, MyDBI myDBi) {
		XmlLoader.save2File(filename, myDBi);
	}

	public MyTable getTable(String tableName) {
		LinkedHashMap<String, MyTable> allTables = getAllTables();
		if (allTables.containsKey(tableName)) return allTables.get(tableName);
		else return null;
	}
	public void bootstrapDB() {
		createRoles();
		LinkedHashMap<String, MyTable> allTables = getAllTables();
		for (MyTable myT : allTables.values()) {
			myT.createTable();
		}
		addViews();
		addData();
		setVersion2DB(getSoftwareVersion());
	}
	private void createRoles() {
		DBKernel.sendRequest("CREATE ROLE " + DBKernel.delimitL("READ_ONLY"), false);
		DBKernel.sendRequest("CREATE ROLE " + DBKernel.delimitL("WRITE_ACCESS"), false);
		DBKernel.sendRequest("CREATE ROLE " + DBKernel.delimitL("SUPER_WRITE_ACCESS"), false);
		DBKernel.sendRequest("CREATE ROLE " + DBKernel.delimitL("ADMIN"), false);
	}

	/*
	 * 
	 * 
	 -------------------------- Now starting with DB-specific operations ---------------------------------------
	 * 
	 * 
	 */

	private Connection conn = null;

	private String dbUsername, dbPassword, dbPath, path2XmlFile;

	private boolean isServerConnection = false;

	private Boolean passFalse = null;
	private LinkedHashMap<Object, LinkedHashMap<Object, String>> filledHashtables = new LinkedHashMap<Object, LinkedHashMap<Object, String>>();

	public String getDbPassword() {
		return dbPassword;
	}
	public String getDbUsername() {
		return dbUsername;
	}
	public Connection getConn() {
		if (conn == null && dbUsername != null && dbPassword != null && dbPath != null && path2XmlFile != null) establishDBConnection(dbUsername, dbPassword);
		return conn;
	}
	public boolean isServerConnection() {
		return isServerConnection;
	}
	public boolean establishDBConnection(String dbUsername, String dbPassword) {
		return establishDBConnection(dbUsername, dbPassword, path2XmlFile);
	}
	public boolean establishDBConnection(String dbUsername, String dbPassword, String path2XmlFile) {
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
		this.path2XmlFile = path2XmlFile;
		if (getDbPath().equals(".")) dbPath = path2XmlFile;
		else dbPath = getDbPath();
		isServerConnection = isHsqlServer(dbPath);
		if (!isServerConnection && !dbPath.endsWith(System.getProperty("file.separator"))) dbPath += System.getProperty("file.separator");
		passFalse = false;
		filledHashtables = new LinkedHashMap<Object, LinkedHashMap<Object, String>>();
		establishNewConnection(dbUsername, dbPassword, dbPath, true);
		return (conn != null);
	}
	public void changePasswort(final String newPassword) throws Exception {
    	if (isAdmin()) {
			DBKernel.sendRequest("SET PASSWORD '" + newPassword + "';", false);
    	}
		else {
    		sendRequest("ALTER USER " + DBKernel.delimitL(dbUsername) + " SET PASSWORD '" + newPassword + "';", false, true);
    	}		
	}

	boolean closeDBConnections(final boolean kompakt) {
		boolean result = true;
		try {
			if (conn != null && !conn.isClosed()) {
				if (!isServerConnection) {
					try {
						if (kompakt && !isAdmin()) { // kompakt ist nur beim Programm schliessen true
							try {
								establishDefaultAdminConn();
							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}
						Statement stmt = conn.createStatement();
						MyLogger.handleMessage("vor SHUTDOWN");
						stmt.execute("SHUTDOWN"); // Hier kanns es eine Exception geben, weil nur der Admin SHUTDOWN machen darf!
					}
					catch (SQLException e) {
						result = false;
						if (kompakt) e.printStackTrace();
					}
				}
				MyLogger.handleMessage("vor close");
				conn.close();
				MyLogger.handleMessage("vor gc");
				System.gc();
				System.runFinalization();
				saveWindowState();
				if (kompakt) MyDBI.saveDB(path2XmlFile + System.getProperty("file.separator") + "DB.xml", this);
			}
		}
		catch (SQLException e) {
			result = false;
			MyLogger.handleException(e);
		}
		return result;
	}
	public Boolean getPassFalse() {
		return passFalse;
	}

	
	private void saveWindowState() {
		try {
			if (DBKernel.mainFrame != null && DBKernel.mainFrame.getMyList() != null && DBKernel.mainFrame.getMyList().getMyDBTable() != null &&
					DBKernel.mainFrame.getMyList().getMyDBTable().getActualTable() != null) {
				DBKernel.prefs.put("LAST_SELECTED_TABLE", DBKernel.mainFrame.getMyList().getMyDBTable().getActualTable().getTablename());

				DBKernel.prefs.put("LAST_MainFrame_FULL", DBKernel.mainFrame.getExtendedState() == JFrame.MAXIMIZED_BOTH ? "TRUE" : "FALSE");
				
				// in order to be able to save the dimension and position of the NORMAL window we have to do the following
				if (DBKernel.mainFrame.isVisible() && DBKernel.mainFrame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
					DBKernel.mainFrame.setVisible(true);
					DBKernel.mainFrame.setExtendedState(JFrame.NORMAL);
					DBKernel.mainFrame.setExtendedState(JFrame.ICONIFIED);
				}

				DBKernel.prefs.put("LAST_MainFrame_WIDTH", DBKernel.mainFrame.getWidth() + "");
				DBKernel.prefs.put("LAST_MainFrame_HEIGHT", DBKernel.mainFrame.getHeight() + "");
				DBKernel.prefs.put("LAST_MainFrame_X", DBKernel.mainFrame.getX() + "");
				DBKernel.prefs.put("LAST_MainFrame_Y", DBKernel.mainFrame.getY() + "");

				DBKernel.prefs.prefsFlush();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
	}
	boolean isAdmin() {
		if (dbUsername == null) return false;
		if (conn == null) {
			if (dbUsername.equals(getSA())) return true;
		}
		boolean result = false;
		ResultSet rs = getResultSet("SELECT COUNT(*) FROM " + DBKernel.delimitL("Users") + " WHERE " + DBKernel.delimitL("Zugriffsrecht") + " = " + Users.ADMIN +
				" AND " + DBKernel.delimitL("Username") + " = '" + dbUsername + "'", true);
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
	private void establishDefaultAdminConn() {
		establishNewConnection(getSA(), getSAP(), dbPath, true);
	}
	private void establishNewConnection(final String dbUsername, final String dbPassword, final String dbPath, final boolean suppressWarnings) {
		closeDBConnections(false);
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver").newInstance();
			String connStr = isServerConnection ? "jdbc:hsqldb:hsql://" + dbPath : "jdbc:hsqldb:file:" + dbPath + "DB";
			conn = DriverManager.getConnection(connStr, dbUsername, dbPassword);
			conn.setReadOnly(DBKernel.isReadOnly());
		}
		catch (Exception e) {
			passFalse = e.getMessage().startsWith("invalid authorization specification");
			if (e.getMessage().startsWith("Database lock acquisition failure:")) {
				Frame[] fs = Frame.getFrames();
				if (fs != null && fs.length > 0) {
					InfoBox ib = new InfoBox(fs[0], "Die Datenbank wird zur Zeit von\neinem anderen Benutzer verwendet!", true, new Dimension(300, 150), null, true);
					ib.setVisible(true);
				}
			}
			else {
				if (!suppressWarnings) MyLogger.handleException(e);
			}
		}
	}
	ResultSet getResultSet(final String sql, final boolean suppressWarnings) {
		ResultSet ergebnis = null;
		if (conn != null) {
			try {
				Statement anfrage = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ergebnis = anfrage.executeQuery(sql);
				ergebnis.first();
			}
			catch (Exception e) {
				if (!suppressWarnings) {
					MyLogger.handleMessage(sql);
					MyLogger.handleException(e);
				}
			}
		}
		return ergebnis;
	}
	boolean sendRequest(final String sql, final boolean suppressWarnings, final boolean fetchAdminInCase) {
		boolean result = false;
		if (conn != null) {
			boolean adminGathered = false;
			if (fetchAdminInCase && !isAdmin()) {
				establishDefaultAdminConn();
				adminGathered = true;
			}
			try {
				Statement anfrage = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				anfrage.execute(sql);
				result = true;
			}
			catch (Exception e) {
				if (!suppressWarnings) {
					if (!DBKernel.isKNIME ||
							(!e.getMessage().equals("The table data is read only") && !e.getMessage().equals("invalid transaction state: read-only SQL-transaction"))) {
						MyLogger.handleMessage(sql);						
					}
					MyLogger.handleException(e);
				}
			}
			if (adminGathered) {
				establishDBConnection();
			}
		}
		return result;
	}
	private boolean establishDBConnection() {
		return establishDBConnection(dbUsername, dbPassword, dbPath);
	}
	private boolean isHsqlServer(String checkURL) {
		boolean result = false; // checkURL.startsWith("192") ||
								// checkURL.startsWith("localhost");
		String host = "";
		try {
			if (!checkURL.startsWith("http")) {
				checkURL = "http://" + checkURL;
			}
			URL url = new URL(checkURL); // "192.168.212.54/silebat"
			host = url.getHost();
			if (!host.isEmpty()) {
				InetSocketAddress isa = new InetSocketAddress(host, 9001);// new URL(checkURL).openConnection();
				result = !isa.isUnresolved();
			}
		}
		catch (MalformedURLException e) {
			// e.printStackTrace();
		}
		return result;
	}
	private void setVersion2DB(final String softwareVersion) {
		if (!DBKernel.sendRequest("INSERT INTO \"Infotabelle\" (\"Parameter\",\"Wert\") VALUES ('DBVersion','" + softwareVersion + "')", true, false)) {
			DBKernel.sendRequest("UPDATE " + DBKernel.delimitL("Infotabelle") + " SET " + DBKernel.delimitL("Wert") + " = '" + softwareVersion
					+ "'" + " WHERE " + DBKernel.delimitL("Parameter") + " = 'DBVersion'", false, false);
		}
	}
	
	void refreshHashTables() {
		filledHashtables.clear();
	}
	LinkedHashMap<Object, String> fillHashtable( final MyTable theTable, final String startDelim, final String delimiter, final String endDelim, final boolean goDeeper, final boolean forceUpdate, HashSet<MyTable> alreadyUsed) {
		if (theTable == null) {
			return null;
		}
		String foreignTable = theTable.getTablename();
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
				if (alreadyUsed == null) alreadyUsed = new HashSet<MyTable>();
				alreadyUsed.add(theTable);
				boolean isdkz = theTable.getTablename().equals("DoubleKennzahlen");
				do {
					value = "";
					String valueBkp = "";
					boolean hasSymbols = false;
					if (theTable.getFields2ViewInGui() != null) {
						for (String s : theTable.getFields2ViewInGui()) {
							Integer fi = theTable.getFieldIndex(s);
							if (fi == null) {
								value += s;
								hasSymbols = true;
							}
							else {
								if (!value.isEmpty() && !hasSymbols) value += "\t";
								for (i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
									if (rs.getMetaData().getColumnName(i).equals(s)) {
										String tVal = handleField(rs.getObject(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim, alreadyUsed);
										if (isdkz && tVal.equals("?") && s.equals("Exponent")) value = valueBkp;
										else value += tVal;
										break;
									}
								}
								if (isdkz && s.equals("Wert")) valueBkp = value;
							}
						}
					}
					else {
						for (i = 2; i <= rs.getMetaData().getColumnCount(); i++) {
							String v = handleField(rs.getObject(i), foreignFields, mnTable, i, goDeeper, startDelim, delimiter, endDelim, alreadyUsed);
							if (!v.isEmpty()) {
								v += "\t";
								String cn = rs.getMetaData().getColumnName(i);
								value += cn + ": " + v;
							}
						}
					}
					o = rs.getObject(1);
					val = value;
					if (theTable.getTablename().equals("DoubleKennzahlen")) {
						h.put(new Double((Integer) rs.getObject(1)), value);
					}
					else {
						h.put(rs.getObject(1), value);
					}
				} while (rs.next());
			}
		} catch (Exception e) {
			MyLogger.handleException(e);
			MyLogger.handleMessage(theTable.getTablename() + "\t" + o + "\t" + val + "\t" + selectSQL);
		}
		if (!filledHashtables.containsKey(foreignTable)) {
			filledHashtables.put(foreignTable, h);
		}
		return h;
	}	
	private String handleField(final Object id, final MyTable[] foreignFields, final String[] mnTable, final int i, final boolean goDeeper, final String startDelim,
			final String delimiter, final String endDelim, HashSet<MyTable> alreadyUsed) {
		String result = "";
		if (id == null) {
			;
		}
		else if (foreignFields != null && i > 1 && foreignFields.length > i - 2 && foreignFields[i - 2] != null) {
			if (goDeeper) {
				LinkedHashMap<Object, String> hashBox = fillHashtable(foreignFields[i - 2], startDelim, delimiter, endDelim,
						goDeeper && !alreadyUsed.contains(foreignFields[i - 2]), false, alreadyUsed);
				if (hashBox != null && hashBox.get(id) != null) {
					String ssttrr = hashBox.get(id).toString();
					result = ssttrr.trim().length() == 0 ? "" : ssttrr;
				}
				else if (mnTable != null && i > 1 && i - 2 < mnTable.length && mnTable[i - 2] != null && mnTable[i - 2].length() > 0) {
					result = "";
				}
				else {
					System.err.println("hashBox überprüfen...\t" + id);
					result = "";// ft + ": leer\n";
				}
			}
			else {
				String ft = foreignFields[i - 2].getTablename();
				result = ft + "-ID: " + id + "\n";
			}
		}
		else {
			result = (id instanceof Double ? DBKernel.getDoubleStr(id) : id.toString());
		}
		if (result.isEmpty()) result ="?";
		return result;
	}
	
	
	Integer getLastInsertedID(final PreparedStatement psmt) {
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
	
}
