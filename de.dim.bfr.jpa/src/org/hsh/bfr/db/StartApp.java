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

import java.awt.Component;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import org.hsh.bfr.db.gui.Login;
import org.hsh.bfr.db.gui.MainFrame;
import org.hsh.bfr.db.gui.MyList;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;
import org.hsh.bfr.db.gui.dbtree.MyDBTree;

import com.jgoodies.looks.windows.WindowsLookAndFeel;

/**
 * @author Armin
 *
 */

public class StartApp {

	public static void main(final String[] args) {
	    try {
	        //UIManager.setLookAndFeel(new com.jgoodies.looks.plastic.Plastic3DLookAndFeel()); // .plastic.Plastic3DLookAndFeel() .windows.WindowsLookAndFeel()
	        //UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel"); // PlasticXPLookAndFeel Plastic3DLookAndFeel
	        //UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
	        UIManager.setLookAndFeel(new WindowsLookAndFeel());
	        /*
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	        UIManager.put("Table.gridColor", Color.black);
	        UIManager.put("Table.selectionBackground", Color.white);
	        UIManager.put("Table.selectionForeground", Color.black);
	        UIManager.put("Table.focusCellBackground", Color.white);
	        UIManager.put("Table.focusCellForeground", Color.black);
	         */
	  		setUIFont(new javax.swing.plaf.FontUIResource("Tahoma",Font.PLAIN,13));
	      }
	      catch (Exception e) {MyLogger.handleException(e);}
	      System.setProperty("line.separator", "\n"); // Damit RDiff auch funktioniert, sonst haben wir einmal (unter Windows) "\r\n" und bei Linux nur "\n"

	      go(null, true, false);
	}
	public static void go(final Connection conn, final boolean setVisible, final boolean fromKNIME) {
	  	if (!DBKernel.debug) {
	  		MyLogger.setup(DBKernel.HSH_PATH + "LOGs" + System.getProperty("file.separator") + "log_" + System.currentTimeMillis() + ".txt");
	  	}
	      MyLogger.handleMessage(System.getProperty("java.version") + "\t" + (Runtime.getRuntime().maxMemory()/1024/1024)+ "MB"); // -Xms256m -Xmx1g
	      
	  	ToolTipManager ttm = null;
	      ttm = ToolTipManager.sharedInstance();
	      ttm.setInitialDelay(0);
	      ttm.setDismissDelay(60000);

	      if (!fromKNIME || conn == null) {
		  		Login login = new Login(true);
		  		login.setVisible(setVisible);	    	  
	      }
	      else {
	    	  	DBKernel.isKNIME = true;
	    	  	Connection kernConn = DBKernel.getLocalConn(); 
	    	  	boolean integrateConn = false;
	    	  	if (kernConn != null) {
	    	  		try {
						DatabaseMetaData meta1 = kernConn.getMetaData();
		    	  		DatabaseMetaData meta2 = conn.getMetaData();
		    	  		if (meta1.getURL().equals(meta2.getURL()) &&
		    	  				meta1.getUserName().equals(meta2.getUserName())) {
		    	  					// identical
		    	  				}
		    	  		else {
		    	  			integrateConn = true;
		    	  		}
					}
	    	  		catch (SQLException e) {
	    	  			integrateConn = true;
						//e.printStackTrace();
					}
	    	  	}
	    	  	System.err.println("W1");
	    	  	if (kernConn == null || integrateConn) {
		    	  	System.err.println("W2");
					DBKernel.setLocalConn(conn);
		    	  	Login login = new Login();
		  	    	MyDBTable myDB = new MyDBTable();
		  	    	myDB.initConn(conn);
		  	    	MyDBTree myDBTree = new MyDBTree();
					MyList myList = new MyList(myDB, myDBTree);
					DBKernel.myList = myList;
			    	login.loadMyTables(myList, null);
		    	  	System.err.println("W21");
			    	
					MainFrame mf = new MainFrame(myList);
					DBKernel.mainFrame = mf;
					myList.setSelection("Versuchsbedingungen"); // DBKernel.prefs.get("LAST_SELECTED_TABLE", 
		    	  	System.err.println("W22");
		    	  	DBKernel.mainFrame.pack();
		    	  	//mf.setExtendedState(JFrame.MAXIMIZED_BOTH);
		    	  	/*
			  		try {
					  	if (!DBKernel.sendRequest("SELECT * FROM  " + DBKernel.delimitL("ProzessWorkflow"), false)) {
						  	boolean isAdmin = DBKernel.isAdmin();
						  	if (!isAdmin) {
						  		DBKernel.closeDBConnections(false);
								DBKernel.getDefaultAdminConn();
						  	}
					  		UpdateChecker.check4Updates_141_142(myList); 
					  		DBKernel.setDBVersion("1.4.2");
							DBKernel.closeDBConnections(false);
							go(conn, setVisible);
							return;
					  	}
					}
			  		catch (Exception e) {
						e.printStackTrace();
					}
		    	  	*/
		    	  	System.err.println("W23");
		    	  	DBKernel.mainFrame.setVisible(setVisible);
		    	  	System.err.println("W24");
		    	  	DBKernel.mainFrame.toFront();
		    	  	System.err.println("W25");
		    	  	try {
		    	  		myDB.requestFocus(); //.grabFocus();//myDB.selectCell(0, 0);
		    	  	}
		    	  	catch (Exception e) {}
		    	  	System.err.println("W26");
	    	  	}
	    	  	else if (kernConn != null) {
		    	  	System.err.println("W3");
		    	  	try {
			    	  	DBKernel.mainFrame.setVisible(setVisible);		    	  		
			    	  	DBKernel.mainFrame.toFront();	    	  		
		    	  	}
		    	  	catch (Exception e) {e.printStackTrace();}
	    	  	}
	    	  	System.err.println("W4");
	    	  	
	    	  	MyTable myT = DBKernel.myList.getTable("GeschaetzteModelle"); DBKernel.doMNs(myT);
	    	  	myT = DBKernel.myList.getTable("Modellkatalog"); DBKernel.doMNs(myT);
	    	  	myT = DBKernel.myList.getTable("Versuchsbedingungen"); DBKernel.doMNs(myT);
	      }
	      //DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL("Infotabelle") + " WHERE " + DBKernel.delimitL("Parameter") + " = 'DBuuid'", false);
		    //DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL("ChangeLog"), false);
		    //DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL("DateiSpeicher") + " WHERE " + DBKernel.delimitL("ID") + " != 5", false);
	      //DBKernel.sendRequest("GRANT CREATE VIEW ON * TO " + DBKernel.delimitL("SUPER_WRITE_ACCESS"), false);
	      //DBKernel.sendRequest("GRANT ALL ON * TO " + DBKernel.delimitL("WRITE_ACCESS"), false);
	      
	  		//if (args != null && args.length > 0) login.setUN(args[0]);
	  		//loadDB(false);
	  		
	  		//testFocus();	
	      
	      //DBKernel.myList.setSelection("Versuchsbedingungen");
	}
	private static void setUIFont (final javax.swing.plaf.FontUIResource f){
	    //
	    // sets the default font for all Swing components.
	    // ex. 
	    //  setUIFont (new javax.swing.plaf.FontUIResource("Serif",Font.ITALIC,12));
	    //
	    java.util.Enumeration keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements()) {
	      Object key = keys.nextElement();
	      Object value = UIManager.get (key);
	      if (value instanceof javax.swing.plaf.FontUIResource) {
			UIManager.put (key, f);
		}
	      }
	    }   
	private static void testFocus() {

		final Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (false) {
					timer.cancel();
				} else {
					// null is returned if none of the components in this application has the focus
					Component compFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

					// null is returned if none of the windows in this application has the focus
					Window windowFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
					
					System.out.println(windowFocusOwner + "\n" + compFocusOwner);					
				}
			}
		};
		//start des Timers:
		timer.scheduleAtFixedRate(task, 0, 1000);
		// wiederholt sich unendlich immer nach einer Sekunde (1000 Millisekunden)
	}
}
