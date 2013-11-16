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
package org.hsh.bfr.db.gui;



import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyDBTables;
import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.MyTable;
import org.hsh.bfr.db.gui.dbtable.MyDBForm;
import org.hsh.bfr.db.gui.dbtable.MyDBPanel;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;
import org.hsh.bfr.db.gui.dbtable.header.GuiMessages;
import org.hsh.bfr.db.gui.dbtree.MyDBTree;

import quick.dbtable.Filter;


/**
 * @author Armin
 *
 */
public class MyList extends JTree implements TreeSelectionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5697967857506372930L;
	private InvisibleNode root = null;
	private InvisibleNode[] children = new InvisibleNode[9];
	private MyDBTable myDB = null;
	private MyDBTree myDBTree = null;
	private boolean catchEvent = true;

	public static int SystemTabellen_LIST = 0;
	public static int BasisTabellen_LIST = 1;
	public static int Tenazitaet_LIST = 2;
	public static int PMModelle_LIST = 3;
	public static int Krankheitsbilder_LIST = 4;
	public static int Prozessdaten_LIST = 5;
	public static int Nachweissysteme_LIST = 6;
	public static int Modell_LIST = 7;
	public static int Lieferketten_LIST = 8;
	
	public MyList() {
		this(null,null);
	}
	public MyList(final MyDBTable myDB, final MyDBTree myDBTree) {
		this.myDB = myDB;
		this.myDBTree = myDBTree;
		if (myDB != null && myDBTree != null) {
		    this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);   
		    ImageIcon customLeafIcon = rescaleImage(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Table.gif")), 12);
		    //ImageIcon customOpenIcon = rescaleImage(new ImageIcon(getClass().getResource("/org/hsh/bfr/db/gui/res/Display.gif")), 18);
		    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		    //renderer.setOpenIcon(customOpenIcon);
		    //renderer.setClosedIcon(customClosedIcon);
		    renderer.setLeafIcon(customLeafIcon);
		    this.setCellRenderer(renderer);
	
		    this.setCellRenderer(new DefaultTreeCellRenderer() {
		        /**
				 * 
				 */
				private static final long serialVersionUID = 6600365951951898780L;

				@Override
				public Component getTreeCellRendererComponent(final JTree tree,
		            final Object value, final boolean sel, final boolean expanded, final boolean leaf,
		            final int row, final boolean hasFocus) {
		          super.getTreeCellRendererComponent(tree, value, sel, expanded,
		              leaf, row, hasFocus);
		          if (value instanceof InvisibleNode && !((InvisibleNode) value).isVisible()) {
		            setForeground(Color.yellow);
		          }
		          setText(GuiMessages.getString(getText()));
		          return this;
		        }
		      });
		    ((DefaultTreeCellRenderer)this.getCellRenderer()).setLeafIcon(customLeafIcon);
		    
		    //this.setToggleClickCount(0);
		    
	
		    root 		= new InvisibleNode(".");
		    children[SystemTabellen_LIST] = new InvisibleNode(GuiMessages.getString("System-Tabellen"));
		    children[BasisTabellen_LIST] = new InvisibleNode(GuiMessages.getString("Basis-Tabellen"));
		    children[Tenazitaet_LIST] = new InvisibleNode(GuiMessages.getString("Tenazitaet"));
		    children[PMModelle_LIST] = new InvisibleNode(GuiMessages.getString("PMModelle"));
		    children[Krankheitsbilder_LIST] = new InvisibleNode(GuiMessages.getString("Krankheitsbilder"));
		    children[Prozessdaten_LIST] = new InvisibleNode(GuiMessages.getString("Prozessdaten"));
		    children[Nachweissysteme_LIST] = new InvisibleNode(GuiMessages.getString("Nachweissysteme"));
		    children[Modell_LIST] = new InvisibleNode(GuiMessages.getString("Modell"));
		    children[Lieferketten_LIST] = new InvisibleNode(GuiMessages.getString("Lieferketten"));
	
		    for (int i=0; i < children.length; i++) {
		    	if (DBKernel.isKrise) {
					children[i].setVisible(false);
				}
				else {
					DBKernel.prefs.getBoolean("VIS_NODE_" + children[i], true); // children[i].setVisible(false);//
				}
		    }
		    if (DBKernel.isKrise) {
				children[Lieferketten_LIST].setVisible(true);
				if (DBKernel.debug) children[SystemTabellen_LIST].setVisible(true);
			}
		    
		    boolean isAdmin  = DBKernel.isAdmin();
		    if (isAdmin && !DBKernel.isKrise) { //  && (!DBKernel.isKNIME || DBKernel.debug)
				root.add(children[SystemTabellen_LIST]);
			}		
		    if (!DBKernel.getUsername().equals("burchardi")) root.add(children[BasisTabellen_LIST]);
		    if (!DBKernel.getUsername().equals("burchardi")) root.add(children[Tenazitaet_LIST]); //
		    if (!DBKernel.getUsername().equals("burchardi")) root.add(children[PMModelle_LIST]); //
		    if (!DBKernel.isKNIME && !DBKernel.getUsername().equals("burchardi")) root.add(children[Krankheitsbilder_LIST]);
		    if (!DBKernel.isKNIME) root.add(children[Prozessdaten_LIST]);
		    if (!DBKernel.isKNIME && !DBKernel.getUsername().equals("burchardi")) root.add(children[Nachweissysteme_LIST]);
		    //root.add(children[Modell_LIST]);
		    if (DBKernel.isKrise) {
				root.add(children[Lieferketten_LIST]);
			}
		    
		    
		    
		    this.setModel(new InvisibleTreeModel(root, false, true));
		    //((InvisibleTreeModel) this.getModel()).activateFilter(true);
		    ((InvisibleTreeModel) this.getModel()).reload();
		    //DefaultTreeModel dfm = (InvisibleTreeModel) this.getModel();
		    //dfm.setRoot(root);
		    //((InvisibleTreeModel) this.getModel()).setRoot(root);
		    this.addTreeSelectionListener(this);
		    /*
		    final JTree tree = this;
		    this.addMouseListener( new MouseAdapter() {
		      public void mousePressed(MouseEvent e) {
		      	if (e.getButton() != MouseEvent.BUTTON1) {
		      		TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
		      		if (tp != null) {
				        InvisibleNode selectedInvisibleNode = (InvisibleNode) tp.getLastPathComponent();
				       	if (selectedInvisibleNode != null && selectedInvisibleNode.getUserObject() instanceof MyTable) {
				        	MyTable myT = (MyTable) selectedInvisibleNode.getUserObject();
				        	openNewWindow(myT, null, null, null, null, null);
				      	}	      			
		      		}		      	
		      	}
		      }
		    });
		    */
		    this.addKeyListener(this);
		    //this.setRootVisible(false);
		}		
	}
	public MyDBTree getMyDBTree() {
		return myDBTree;
	}
	public MyDBTable getMyDBTable() {
		return myDB;
	}

  @Override
public void valueChanged(final TreeSelectionEvent event) {
    if (catchEvent && myDB != null && myDBTree != null) { // !event.getValueIsAdjusting()    	
    	InvisibleNode selectedInvisibleNode = (InvisibleNode) event.getPath().getLastPathComponent();
    	if (selectedInvisibleNode.getUserObject() instanceof MyTable) {
    		MyTable myT = (MyTable) selectedInvisibleNode.getUserObject();
      		myDB.setTable(myT);
      		myDB.getMyDBPanel().setLeftComponent(myT);
      		//DBKernel.mainFrame.setRC();
      		//myT.restoreProperties(myDB); // myDB.getActualTable()
    		if (DBKernel.showHierarchic(myT.getTablename())) {
    			myDBTree.setTable(myT);  
    			myDBTree.setSelectedID(myDB.getSelectedID());
    			myDB.getMyDBPanel().setTreeVisible(true, null);  
    			//myDBTree.grabFocus();
    		}
    		else {
    			myDB.getMyDBPanel().setTreeVisible(false, null);     
    		}
    		myDB.grabFocus();
    	}
    	else {
    		if (DBKernel.debug && event != null && event.getOldLeadSelectionPath() != null) {
				MyLogger.handleMessage(event.getOldLeadSelectionPath().toString());
			}
	      	catchEvent = false;
	      	this.setSelectionPath(event.getOldLeadSelectionPath());
    	}
    }
  	catchEvent = true;
  }
	
  public boolean setSelection(final String selection) {
  	return walk(this.getModel(), this.getModel().getRoot(), selection);  
  }
  protected boolean walk(final TreeModel model, final Object o, final String selection) {
    int cc;
    cc = model.getChildCount(o);
    for (int i=0; i < cc; i++) {
      Object child = model.getChild(o, i);
      if (model.isLeaf(child)) {
      	InvisibleNode inc = (InvisibleNode) child;
        if (selection == null || selection.equals(child.toString())) {
        	if (inc.isVisible) {
        		this.setSelectionPath(new TreePath((inc).getPath()));
        		return true;
        	}
        }
      }
      else {
      	if (walk(model, child, selection)) {
			return true;
		} 
      }
    }
    return false;
  } 
	public void addAllTables() {
		LinkedHashMap<String, MyTable> myTables = MyDBTables.getAllTables();
		for(String key : myTables.keySet()) {
			MyTable myT = myTables.get(key);

			String tn = myT.getTablename();

			myTables.put(tn, myT);
			int child = myT.getChild();
			if (child >= children.length) { //  || child < 0 wird nicht angezeigt!
				InvisibleNode iNode = new InvisibleNode(myT);
				iNode.setVisible(DBKernel.prefs.getBoolean("VIS_NODE_" + myT.getTablename(), true));
				root.add(iNode);
			}
			else if (child >= 0) {
				InvisibleNode iNode = new InvisibleNode(myT);
				if (DBKernel.isKrise) {
					iNode.setVisible(child == Lieferketten_LIST || DBKernel.debug && child == SystemTabellen_LIST); // visible			
				}
				children[child].add(iNode);
			}

			this.setModel(new InvisibleTreeModel(root, false, true));
			expandAll();	
		}
	}
	
	public MyTable getTable(final String tableName) {
		if (MyDBTables.getAllTables().containsKey(tableName)) {
			return MyDBTables.getAllTables().get(tableName);
		} else {
			return null;
		}
	}
  public Object openNewWindow(final MyTable theNewTable, final Object value, final Object headerValue, final String mnTable, final String mnID, final MyDBForm dbForm) {
	  return openNewWindow(theNewTable, value, headerValue, mnTable, mnID, dbForm, null);
  }
  public Object openNewWindow(final MyTable theNewTable, final Object value, final Object headerValue, final String mnTable, final String mnID, final MyDBForm dbForm, final JDialog owner) {
	  	Object result = null;
	  	String titel = (headerValue == null) ? theNewTable.getTablename() : (DBKernel.getLanguage().equals("en") ? "Choose " + GuiMessages.getString((String) headerValue) + "..." : headerValue + " auswählen...");
	  	//JDialog f = new JDialog(DBKernel.mainFrame, titel, dbForm != null);
	  	JDialog f = new JDialog(owner == null ? DBKernel.mainFrame : owner, titel);
	  	f.setModal(dbForm != null || owner != null);

		MyDBTable newDBTable = new MyDBTable(); 
		try {
			boolean disableButtons = false;
			newDBTable.initConn(DBKernel.getDBConnection());
			MyTable myT = null;
			if (dbForm != null) {
				myT = dbForm.getActualTable();
			}
			newDBTable.setTable(theNewTable);
		
			final MyDBPanel myP = new MyDBPanel(newDBTable, null, disableButtons);
	  		if (value != null && value instanceof Integer) {
	  			newDBTable.setSelectedID((Integer) value);
	  			myP.setFirstSelectedID((Integer) value);
	  		}
	  		else if (value != null && value instanceof Double &&
	  				theNewTable != null && theNewTable.getTablename().equals("DoubleKennzahlen")) {
	  			Integer intVal = (int)Math.round((Double)value);
	  			newDBTable.setSelectedID(intVal);
	  			myP.setFirstSelectedID(intVal);
	  		}
	  		else {
	  			newDBTable.clearSelection();
	  		}

	        f.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowOpened(final WindowEvent e) {
	        		myP.getSuchfeld().requestFocus();
	            }
	        });
if (dbForm != null || owner != null) {
	myP.setParentDialog(f, true);
}  

			f.setMinimumSize(new Dimension(1000, 500)); // sonst ist der OK Knopf möglicherweise nicht zu sehen...
			f.getContentPane().add(myP);
			f.pack();
			if (dbForm == null) {
				f.setSize(DBKernel.mainFrame.getRightSize());
			} else {
				Dimension dim = dbForm.getSize();
				try {dim.width = (DBKernel.mainFrame.getSize().width + dim.width) / 2;}
				catch (Exception e) {}
				f.setSize(dim);
			}
			f.setLocationRelativeTo(f.getOwner());

			MyDBTable myDBTable2 = null;
			if (myT != null && myT.getListMNs() != null) {
				if (headerValue != null && mnTable != null && mnTable.length() > 0) { // headerValue.toString().equals("Kits")
					if (!mnTable.equals("INT")) {
						String tname = myT.getTablename();
						if (tname.equals("GeschaetzteModelle")) {
							tname = "GeschaetztesModell";
						}
						Object[][] o = new Object[1][2]; o[0][0] = tname; o[0][1] = mnID; // dbTable.getValueAt(row, 0);
						//if (tname == "GeschaetztesModell") myDBTable2 = myP.setListVisible(true, this.getTable(mnTable), o, dbTable, row);
						//else
						myDBTable2 = myP.setListVisible(true, this.getTable(mnTable), o);
					}
					myP.setParentDialog(f, false);
				}
			}
				
			//DBKernel.topTable = newDBTable;
			f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			f.setVisible(true);
				
			newDBTable.checkUnsavedStuff();
		    if (myDBTable2 != null) {
				myDBTable2.checkUnsavedStuff();
			}

			if (myP.isSavePressed()) {
				result = myP.getSelectedID();
			}
			if (result instanceof Integer && (Integer) result < 0)
			 {
				result = null;
				//DBKernel.topTable = dbTable;
			}
		}
		catch (Exception e) {
			result = value;
			MyLogger.handleException(e);
		}
		
		return result;
  }
  public Object openNewWindow(final MyTable theNewTable, final Object value, final Object headerValue, final MyDBTable dbTable, final Integer row, final Integer col) {
	  	return openNewWindow(theNewTable, value, headerValue, dbTable, row, col, null);
	  }
  public Object openNewWindow(final MyTable theNewTable, final Object value, final Object headerValue, final MyDBTable dbTable, final Integer row, final Integer col, final Object[][] conditions) {
	  	return openNewWindow(theNewTable, value, headerValue, dbTable, row, col, conditions, false);	  
  }
  public Object openNewWindow(final MyTable theNewTable, final Object value, final Object headerValue, final MyDBTable dbTable, final Integer row, final Integer col, final Object[][] conditions, boolean fromMMC) {
	  return openNewWindow(theNewTable, value, headerValue, dbTable, row, col, conditions, fromMMC, null);
  }
  public Object openNewWindow(final MyTable theNewTable, final Object value, final Object headerValue, final MyDBTable dbTable, final Integer row, final Integer col, final Object[][] conditions, boolean fromMMC, Filter mf) {
	  return openNewWindow(theNewTable, value, headerValue, dbTable, row, col, conditions, fromMMC, mf, null);
  }
  public Object openNewWindow(final MyTable theNewTable, final Object value, final Object headerValue, final MyDBTable dbTable, final Integer row, final Integer col, final Object[][] conditions, boolean fromMMC, Filter mf, JPanel parent) {
	  return openNewWindow(theNewTable, value, headerValue, dbTable, row, col, conditions, fromMMC, mf, parent, null);
  }
  public Object openNewWindow(final MyTable theNewTable, final Object value, final Object headerValue, final MyDBTable dbTable, final Integer row, final Integer col, final Object[][] conditions, boolean fromMMC, Filter mf, JPanel parent, String level1Expansion) {
  	Object result = null;
  	String titel = (headerValue == null) ? theNewTable.getTablename() : (DBKernel.getLanguage().equals("en") ? "Choose " + GuiMessages.getString((String) headerValue) + "..." : headerValue + " auswählen...");
  	//JDialog.setDefaultLookAndFeelDecorated(true);
  	Frame parentFrame = null;
  	if (parent == null) {
  		parentFrame = DBKernel.mainFrame;
  	}
  	else {
	  	Window parentWindow = SwingUtilities.windowForComponent(parent); 
	  	if (parentWindow instanceof Frame) {
	  	    parentFrame = (Frame)parentWindow;
	  	}
  	}
  	@SuppressWarnings("unused")
	boolean isRO = DBKernel.isReadOnly();
  	JDialog f = new JDialog(parentFrame, titel, (dbTable != null || fromMMC)); // !isRO && 

		MyDBTable newDBTable = new MyDBTable(); 
		MyDBTree newDBTree = null; 
		boolean isHierarchic = DBKernel.showHierarchic(theNewTable.getTablename());
		try {
			if (dbTable == null) {
				newDBTable.initConn(DBKernel.getDBConnection());
			} else {
				newDBTable.initConn(dbTable.getConnection());
			}
			
			MyTable myT = null;
			if (dbTable != null) {
				myT = dbTable.getActualTable();
			}
			boolean disableButtons = defineTable4NewDBTable(myT, dbTable, value, headerValue, theNewTable, newDBTable, row, col, conditions);
			if (isHierarchic) {
				newDBTree = new MyDBTree(); 
				String[] showOnly = null;
				if (myT != null && myT.getTablename().equals("Versuchsbedingungen")
						&& headerValue != null && headerValue.toString().equals("Matrix")) {
					showOnly = new String[] {"TOP", "ADV", "GS1", "Nährmedien"};
				}
				else if (myT != null && myT.getTablename().equals("Zutatendaten")
						&& headerValue != null && headerValue.toString().equals("Matrix")) {
					showOnly = new String[] {"TOP", "ADV", "GS1"};
				}
				else if (myT != null && myT.getTablename().equals("Aufbereitungsverfahren")
						&& headerValue != null && headerValue.toString().equals("Matrix")) {
					showOnly = new String[] {"TOP", "ADV", "GS1"};
				}
				newDBTree.setTable(theNewTable, showOnly);
			}

			final MyDBPanel myP = new MyDBPanel(newDBTable, newDBTree, disableButtons);
			
			if (mf != null) {
				newDBTable.setReadOnly(true);
				newDBTable.setFilter(mf);
				myP.disableAdding();
				myP.setDefaultFilter(mf);
				//myP.disableFilter();
			}
			
	  		if (value != null && value instanceof Integer) {
	  			newDBTable.setSelectedID((Integer) value);
	  			myP.setFirstSelectedID((Integer) value);
	  		}
	  		else if (value != null && value instanceof Double &&
	  				theNewTable != null && theNewTable.getTablename().equals("DoubleKennzahlen")) {
	  			Integer intVal = (int)Math.round((Double)value);
	  			newDBTable.setSelectedID(intVal);
	  			myP.setFirstSelectedID(intVal);
	  		}
	  		else {
	  			newDBTable.clearSelection();
	  		}

	        f.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowOpened(final WindowEvent e) {
	        		myP.getSuchfeld().requestFocus();
	            }
	        });
			if (dbTable != null || fromMMC) {
				myP.setParentDialog(f, true);
			}  
			myP.setTreeVisible(isHierarchic, level1Expansion);  
			f.setMinimumSize(new Dimension(1000, 500)); // sonst ist der OK Knopf möglicherweise nicht zu sehen...
			f.getContentPane().add(myP);
			f.pack();
			if (dbTable == null) {
				f.setSize(DBKernel.mainFrame.getRightSize());
			} else {
				Dimension dim = dbTable.getSize();
				try {dim.width = (DBKernel.mainFrame.getSize().width + dim.width) / 2;}
				catch (Exception e) {}
				f.setSize(dim);
			}
			f.setLocationRelativeTo(f.getOwner());

			MyDBTable myDBTable2 = null;
			if (myT != null && myT.getListMNs() != null) {
				String[] mnTable = myT.getMNTable();
				if (headerValue != null && mnTable != null && col != null && col > 0 && col-1 < mnTable.length && mnTable[col - 1] != null && mnTable[col - 1].length() > 0) { // headerValue.toString().equals("Kits")
					if (!mnTable[col - 1].equals("INT")) {
						String tname = myT.getTablename();
						String mntname = mnTable[col - 1];
						// Bitte auch schauen in MyDBTable, ca. Zeile 451 (insertNull)
						if (tname.equals("GeschaetzteModelle")) {
							tname = "GeschaetztesModell";
						} else if (tname.equals("Modellkatalog")) {
							tname = "Modell";
						}
						Object[][] o = new Object[1][2]; o[0][0] = tname; o[0][1] = dbTable.getValueAt(row, 0);
						//if (tname == "GeschaetztesModell") myDBTable2 = myP.setListVisible(true, this.getTable(mntname), o, dbTable, row);
						if (tname.equals("GeschaetztesModell") && !mntname.equals("GeschaetztesModell_Referenz")) {
							myDBTable2 = myP.setListVisible(true, this.getTable(mntname), o, dbTable, row);
						} else if (tname.equals("Modell") && mntname.equals("ModellkatalogParameter")) {
							myDBTable2 = myP.setListVisible(true, this.getTable(mntname), o, true);
						} else {
							myDBTable2 = myP.setListVisible(true, this.getTable(mntname), o);
						}
					}
					myP.setParentDialog(f, false);
				}
			}
			
			DBKernel.topTable = newDBTable;
			f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			f.setVisible(true);
			
			newDBTable.checkUnsavedStuff();
	    	if (myDBTable2 != null) {
				myDBTable2.checkUnsavedStuff();
			}

			if (myP.isSavePressed()) {
				result = myP.getSelectedID();
			}
			//MyLogger.handleMessage(result);
			if (result instanceof Integer && (Integer) result < 0) {
				if (mf == null) result = null;
			}
			//MyLogger.handleMessage(result);
			DBKernel.topTable = dbTable;
		}
		catch (Exception e) {
			result = value;
			MyLogger.handleException(e);
		}
		
		return result;
  }
  private boolean defineTable4NewDBTable(MyTable myT, MyDBTable dbTable, final Object value, final Object headerValue, final MyTable theNewTable, MyDBTable newDBTable, final Integer row, final Integer col, final Object[][] conditions) {
		boolean disableButtons = false;
		String tn = "";
		if (myT != null) {
			tn = myT.getTablename();
		}
		if (myT != null && tn.equals("Zutatendaten") &&
				headerValue != null && headerValue.toString().equals("Vorprozess")) {
			Object[][] o = new Object[1][2]; o[0][0] = "Zielprozess"; o[0][1] = dbTable.getValueAt(row, 1);
			newDBTable.setTable(theNewTable, o);
			disableButtons = true;
		}
		else if (myT != null && tn.equals("Prozessdaten") &&
				headerValue != null && headerValue.toString().equals("Zutaten")) {
			Object[][] o = new Object[1][2]; o[0][0] = "Prozessdaten"; o[0][1] = dbTable.getValueAt(row, 0);
			newDBTable.setTable(theNewTable, o);
		}
		else if (myT != null && tn.equals("Produzent_Artikel") &&
				headerValue != null && headerValue.toString().equals("Lieferungen")) {
			Object[][] o = new Object[1][2]; o[0][0] = "Artikel"; o[0][1] = dbTable.getValueAt(row, 0);
			newDBTable.setTable(theNewTable, o);
		}
		else if (myT != null && tn.equals("Station") &&
				headerValue != null && headerValue.toString().equals("Produktkatalog")) {
			Object[][] o = new Object[1][2]; o[0][0] = "Station"; o[0][1] = dbTable.getValueAt(row, 0);
			newDBTable.setTable(theNewTable, o);
		}
		else if (myT != null && tn.equals("Produktkatalog") &&
				headerValue != null && headerValue.toString().equals("Chargen")) {
			Object[][] o = new Object[1][2]; o[0][0] = "Artikel"; o[0][1] = dbTable.getValueAt(row, 0);
			newDBTable.setTable(theNewTable, o);
		}
		else if (myT != null && tn.equals("Chargen") &&
				headerValue != null && headerValue.toString().equals("Zutaten")) {
			Object[][] o = new Object[1][2]; o[0][0] = "Produkt"; o[0][1] = dbTable.getValueAt(row, 0);
			newDBTable.setTable(theNewTable, o);
		}
		else if (myT != null && tn.equals("Chargen") &&
				headerValue != null && headerValue.toString().equals("Lieferungen")) {
			Object[][] o = new Object[1][2]; o[0][0] = "Charge"; o[0][1] = dbTable.getValueAt(row, 0);
			newDBTable.setTable(theNewTable, o);
		}
		else if (myT != null && tn.equals("Lieferungen") && headerValue != null) {					
			if (headerValue.toString().equals("Vorprodukt")) {
				Object[][] o = new Object[1][2]; o[0][0] = "Zielprodukt"; o[0][1] = dbTable.getValueAt(row, 0);
				newDBTable.setTable(theNewTable, o);
			}
			else if (headerValue.toString().equals("Zielprodukt")) {
				Object[][] o = new Object[1][2]; o[0][0] = "Vorprodukt"; o[0][1] = dbTable.getValueAt(row, 0);
				newDBTable.setTable(theNewTable, o);
			}
			else {
				newDBTable.setTable(theNewTable, conditions);
			}
		}
		else if (myT != null && tn.equals("ChargenVerbindungen") &&
				headerValue != null && headerValue.toString().equals("Zutat")) {
			Object charge = dbTable.getValueAt(row, 2);
			Object artikel = DBKernel.getValue("Chargen", "ID", charge+"", "Artikel");
			Object station = DBKernel.getValue("Produktkatalog", "ID", artikel+"", "Station");
			//System.err.println("WW:" + charge + "\t" + artikel + "\t" + station);
			Object[][] o = new Object[1][2]; o[0][0] = "Empfänger"; o[0][1] = station;//dbTable.getValueAt(row, 0);
			newDBTable.setTable(theNewTable, o);
		}

		else if (myT != null && tn.equals("ProzessWorkflow") &&
				headerValue != null && headerValue.toString().equals("Prozessdaten")) {
			Object[][] o = new Object[1][2]; o[0][0] = "Workflow"; o[0][1] = dbTable.getValueAt(row, 0);
			newDBTable.setTable(theNewTable, o);
		}
		else if (myT != null && headerValue != null && headerValue.toString().equals("Katalogcodes")) { // tn.equals("Matrices"), tn.equals("Agenzien"), tn.equals("Methoden") 
			Object[][] o = new Object[1][2]; o[0][0] = "Basis"; o[0][1] = dbTable.getValueAt(row, 0);
			newDBTable.setTable(theNewTable, o);
		}
		else if (myT != null && tn.equals("Versuchsbedingungen") &&
				headerValue != null && headerValue.toString().equals("Messwerte")) {
			Object[][] o = new Object[1][2]; o[0][0] = "Versuchsbedingungen"; o[0][1] = dbTable.getValueAt(row, 0);
			newDBTable.setTable(theNewTable, o);
		}
		else if (myT != null && tn.equals("GeschaetzteModelle") &&
				headerValue != null && headerValue.toString().equals("GeschaetzteParameterCovCor")) {
			Object[][] o = new Object[1][2]; o[0][0] = "GeschaetztesModell"; o[0][1] = dbTable.getValueAt(row, 0);
			newDBTable.setTable(theNewTable, o);
		}
		else if (myT != null && tn.equals("GeschaetzteParameterCovCor") &&
				headerValue != null && (headerValue.toString().equals("param1") || headerValue.toString().equals("param2"))) {
			Object[][] o = new Object[1][2]; o[0][0] = "GeschaetztesModell"; o[0][1] = dbTable.getValueAt(row, 3);
			newDBTable.setTable(theNewTable, o);
		}
		else {
			newDBTable.setTable(theNewTable, conditions);
		}
		return disableButtons;
  }

  private void expandAll() {
	  boolean isAdmin = DBKernel.isAdmin();
	  for (int i=0;i < this.getRowCount();i++) {
		  //if (i != 16 && i != 17 && i != 18) this.expandRow(i);
		  if (!isAdmin || i != 1) {
			this.expandRow(i);
		}
	  }		
  }

  private ImageIcon rescaleImage(final ImageIcon image, final int length) {
    BufferedImage thumbImage = new BufferedImage(length, length, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = thumbImage.createGraphics();
    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics2D.drawImage(image.getImage(), 0, 0, length, length, null);

    return new ImageIcon(thumbImage);
  }
	@Override
	public void keyPressed(final KeyEvent keyEvent) {
    if (keyEvent.isControlDown() && keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
    	if (myDB != null && myDB.getTable() != null) {
    		keyEvent.consume();
    		myDB.getTable().requestFocus();
    	}
    }
	}
	@Override
	public void keyReleased(final KeyEvent keyEvent) {
	}
	@Override
	public void keyTyped(final KeyEvent keyEvent) {
	}
}
