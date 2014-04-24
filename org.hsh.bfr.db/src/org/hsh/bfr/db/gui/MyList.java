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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.hsh.bfr.db.DBKernel;
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
	private LinkedHashMap<Integer, String> myTs = null;
	private HashMap<Integer, Integer> indexMap = null;
	private InvisibleNode[] children = null;
	private MyDBTable myDB = null;
	private MyDBTree myDBTree = null;
	private boolean catchEvent = true;
	
	public MyList() {
		this(null,null);
	}
	public MyList(final MyDBTable myDB, final MyDBTree myDBTree) {
		this.myDB = myDB;
		this.myDBTree = myDBTree;
		myTs = DBKernel.myDBi.getTreeStructure();
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
		    
	
		    root = new InvisibleNode(".");
			children = new InvisibleNode[myTs.size()];
			indexMap = new HashMap<Integer, Integer>();
			int i=0;
		    for (Integer key : myTs.keySet()) {
			    children[i] = new InvisibleNode(GuiMessages.getString(myTs.get(key)));
				DBKernel.prefs.getBoolean("VIS_NODE_" + children[i], true);
				root.add(children[i]);
				indexMap.put(key, i);
				i++;
		    }
		    this.setModel(new InvisibleTreeModel(root, false, true));
		    ((InvisibleTreeModel) this.getModel()).reload();
		    this.addTreeSelectionListener(this);
		    this.addKeyListener(this);
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
  private boolean walk(final TreeModel model, final Object o, final String selection) {
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
		LinkedHashMap<String, MyTable> myTables = DBKernel.myDBi.getAllTables();
		for (String key : myTables.keySet()) {
			MyTable myT = myTables.get(key);

			//String tn = myT.getTablename();
			//myTables.put(tn, myT);
			int child = myT.getChild();
			if (indexMap.containsKey(child) && children[indexMap.get(child)] != null) {
				InvisibleNode iNode = new InvisibleNode(myT);
				iNode.setVisible(true);
				children[indexMap.get(child)].add(iNode);
			}
		}
		this.setModel(new InvisibleTreeModel(root, false, true));
		expandAll();	
		checkChildren();
	}
	private void checkChildren() {
		for (int i=0; i < children.length; i++) {
			if (children[i].getChildCount() == 0) {
				children[i].setVisible(false);
			}
		}
		this.updateUI();
	}
	
	public MyTable getTable(final String tableName) {
		return DBKernel.myDBi.getTable(tableName);
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
	  	if (dbForm != null || owner != null) f.setModalityType(JDialog.ModalityType.DOCUMENT_MODAL); // DOCUMENT_MODAL APPLICATION_MODAL

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
						MyTable myMNTable = this.getTable(mnTable);
						String tname = myT.getTablename();
						tname = myMNTable.getForeignFieldName(myT);
						/*
						if (tname.equals("GeschaetzteModelle")) {
							tname = "GeschaetztesModell";
						}
						*/
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
  public Object openNewWindow(final MyTable theNewTable, final Object value, final Object headerValue, final MyDBTable dbTable, final Integer row, final Integer col, final Object[][] conditions, boolean fromMMC, Filter mf, Component parent) {
	  return openNewWindow(theNewTable, value, headerValue, dbTable, row, col, conditions, fromMMC, mf, parent, null);
  }
  public Object openNewWindow(final MyTable theNewTable, final Object value, final Object headerValue, final MyDBTable dbTable, final Integer row, final Integer col, final Object[][] conditions, boolean fromMMC, Filter mf, Component parent, String level1Expansion) {
  	Object result = null;
  	String titel = (headerValue == null) ? theNewTable.getTablename() : (DBKernel.getLanguage().equals("en") ? "Choose " + GuiMessages.getString((String) headerValue) + "..." : headerValue + " auswählen...");
  	//JDialog.setDefaultLookAndFeelDecorated(true);
  	Window parentFrame = null;
  	if (parent == null) {
  		parentFrame = DBKernel.mainFrame;
  	}
  	else {
	  	Window parentWindow = SwingUtilities.windowForComponent(parent); 
	  	if (parentWindow != null) {
	  	    parentFrame = parentWindow;
	  	}
  	}
  	@SuppressWarnings("unused")
	boolean isRO = DBKernel.isReadOnly();  	
  	JDialog f = new JDialog(parentFrame, titel, JDialog.ModalityType.MODELESS); // !isRO && 
  	if (dbTable != null || fromMMC) f.setModalityType(JDialog.ModalityType.DOCUMENT_MODAL); // DOCUMENT_MODAL APPLICATION_MODAL

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
				/*
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
				*/
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
						String mntname = mnTable[col - 1];
						MyTable myMNTable = this.getTable(mntname);
						String tname = myT.getTablename();
						// Bitte auch schauen in MyDBTable, ca. Zeile 451 (insertNull)
						/*
						if (tname.equals("GeschaetzteModelle")) {
							tname = "GeschaetztesModell";
							//System.err.println(myT + "\t" + theNewTable + "\t" + myMNTable.getForeignFieldName(myT));
						} else if (tname.equals("Modellkatalog")) {
							tname = "Modell";
						}
						*/
						//System.err.println(tname + "\t" + myMNTable.getForeignFieldName(myT));
						tname = myMNTable.getForeignFieldName(myT);
						Object[][] o2 = new Object[1][2]; o2[0][0] = tname; o2[0][1] = dbTable.getValueAt(row, 0);
						//System.err.println(myT + "\t" + theNewTable + "\t" + myMNTable.getForeignFieldName(myT));
						// looking for: tname.equals("GeschaetztesModell") && !mntname.equals("GeschaetztesModell_Referenz") ... o1[0][0] = "Modell"; o1[0][1] = dbTable.getValueAt(row, 3);
						for (MyTable myTLeft : theNewTable.getForeignFields()) {
							for (MyTable myTOrigin : myT.getForeignFields()) {
								if (myTLeft != null && myTOrigin != null && myTLeft.equals(myTOrigin) && !myTLeft.getTablename().equals("DoubleKennzahlen")) {
									Object[][] o1 = new Object[1][2];
									o1[0][0] = theNewTable.getForeignFieldName(myTLeft); o1[0][1] = dbTable.getValueAt(row, myT.getForeignFieldIndex(myTOrigin)+1);
									myDBTable2 = myP.setListVisible(true, myMNTable, o1, o2, row);
									//System.err.println(theNewTable.getForeignFieldName(myTLeft) + "\t" + myT.getForeignFieldIndex(myTOrigin));
									break;
								}
							}
						}
						if (myDBTable2 == null) myDBTable2 = myP.setListVisible(true, myMNTable, o2);
						/*
						if (tname.equals("GeschaetztesModell") && !mntname.equals("GeschaetztesModell_Referenz")) {
							Object[][] o1 = new Object[1][2];
							o1[0][0] = "Modell"; o1[0][1] = dbTable.getValueAt(row, 3);
							
							if (mntname.equals("GueltigkeitsBereiche")) {
								o1[1][0] = "Parametertyp"; o1[1][1] = 1;						
							}
							else { // GeschaetzteParameter
								o1[1][0] = "Parametertyp"; o1[1][1] = 2;						
							}
							
							myDBTable2 = myP.setListVisible(true, myMNTable, o1, o2, row);
						}
						else {
							myDBTable2 = myP.setListVisible(true, myMNTable, o2);
						}
						*/
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
			if (dbTable != null) dbTable.getActualTable().doMNs();
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
		/*
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
		else if (myT != null && tn.equals("GeschaetzteParameterCovCor") &&
				headerValue != null && (headerValue.toString().equals("param1") || headerValue.toString().equals("param2"))) {
			Object[][] o = new Object[1][2]; o[0][0] = "GeschaetztesModell"; o[0][1] = dbTable.getValueAt(row, 3);
			newDBTable.setTable(theNewTable, o);
		}
	*/
		if (myT != null) {
			Object[][] o = null;
			String[] dff = myT.getDeepForeignFields();
			// Zutat.Empfänger=Produkt.Artikel.Station
			// Vorprozess.Prozessdaten=Prozess_Verbindungen.Ausgangsprozess WHERE Prozess_Verbindungen.Zielprozess=Prozessdaten; AND " + DBKernel.delimitL("Zutat_Produkt") + "='Produkt'
			if (dff != null && dff[col-1] != null) {
				StringTokenizer tokADD = new StringTokenizer(dff[col-1],";");
				String sAdd = tokADD.nextToken();
				String justAdd = null;
				if (tokADD.hasMoreTokens()) justAdd = tokADD.nextToken();
				if (dff[col-1].indexOf(";") < 0) sAdd = dff[col-1];
				StringTokenizer tokWhere = new StringTokenizer(sAdd," WHERE ");
					String strLeft = tokWhere.nextToken();
					if (sAdd.indexOf(" WHERE ") < 0) strLeft = sAdd;
					StringTokenizer tok = new StringTokenizer(strLeft,"=");
					if (tok.hasMoreTokens()) {
						String left = tok.nextToken();
						if (tok.hasMoreTokens()) {
							String right = tok.nextToken();
							tok = new StringTokenizer(left,".");
							if (tok.hasMoreTokens()) {
								tok.nextToken();// Zutat
								if (tok.hasMoreTokens()) {
									o = new Object[1][2];
									o[0][0] = tok.nextToken(); // Empfänger
									tok = new StringTokenizer(right,".");
									if (tok.hasMoreTokens()) {
										String field = tok.nextToken(); // Produkt
										Integer i = myT.getFieldIndex(field);
										if (i == null && tokWhere.hasMoreTokens()) {
											MyTable myOT = DBKernel.myDBi.getTable(field);
											if (myOT != null) { // Prozess_Verbindungen
												String strRight = tokWhere.nextToken(); // Prozess_Verbindungen.Zielprozess=Prozessdaten
												StringTokenizer tok2 = new StringTokenizer(strRight,"=");
												if (tok2.hasMoreTokens()) {
													String left2 = tok2.nextToken(); // Prozess_Verbindungen.Zielprozess
													if (tok2.hasMoreTokens()) {
														String right2 = tok2.nextToken(); // Prozessdaten
														tok2 = new StringTokenizer(left2,".");
														if (tok2.hasMoreTokens()) {
															if (field.equals(tok2.nextToken())) {// Prozess_Verbindungen
																if (tok2.hasMoreTokens()) {
																	String field2Where = tok2.nextToken(); // Zielprozess
																	Integer i2 = myT.getFieldIndex(right2);
																	if (i2 != null) {
																		Object o2 = dbTable.getValueAt(row, i2+1);
																		if (tok.hasMoreTokens()) {
																			String field1 = tok.nextToken();
																			String sql = "SELECT " + DBKernel.delimitL(field1) + " FROM " + DBKernel.delimitL(field) + " WHERE " + DBKernel.delimitL(field2Where) + "=" + o2;																		
																			ResultSet rs = DBKernel.getResultSet(sql, false);
																			try {
																				if (rs != null && rs.first()) {
																					List<Object> l = new ArrayList<Object>();
																					do {
																						if (rs.getObject(field1) != null) {
																							l.add(rs.getObject(field1));
																						}
																					} while (rs.next());
																					Object t = o[0][0];
																					o = new Object[l.size()][3];
																					for (int ii=0;ii<o.length;ii++) {
																						o[ii][0] = t;
																						o[ii][1] = l.get(ii);
																						o[ii][2] = justAdd;
																					}
																				}
																			}
																			catch (Exception e) {MyLogger.handleException(e);}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
										else {
											Object o1 = dbTable.getValueAt(row, i+1);
											if (o1 != null) {
												MyTable myT1 = myT.getForeignFields()[i];
												while (tok.hasMoreTokens()) {
													String field1 = tok.nextToken(); // Artikel / Station
													i = myT1.getFieldIndex(field1);
													o1 = DBKernel.getValue(myT1.getTablename(), "ID", o1+"", field1);
													myT1 = myT1.getForeignFields()[i];
												}
												o[0][1] = o1;
											}
										}
									}
									if (o[0][1] != null) newDBTable.setTable(theNewTable, o, "OR");
								}
							}
						}
					}
			}
			if (!theNewTable.equals(myT) && (o == null || o[0][1] == null)) {
				Integer i1 = theNewTable.getForeignFieldIndex(myT);
				if (i1 != null) {
					if (theNewTable.getMNTable() != null && theNewTable.getMNTable()[i1] != null && theNewTable.getMNTable()[i1].equals("INT")) {
						Integer i2 = myT.getForeignFieldIndex(theNewTable);
						if (i2 != null) {
							o = new Object[1][2]; o[0][0] = "ID"; o[0][1] = dbTable.getValueAt(row, i2+1);
							newDBTable.setTable(theNewTable, o);
						}
					}
					else {
						String fn = theNewTable.getFieldNames()[i1];
						if (fn != null) {
							o = new Object[1][2]; o[0][0] = fn; o[0][1] = dbTable.getValueAt(row, 0);
							newDBTable.setTable(theNewTable, o);
						}
					}
				}
				else {
					for (MyTable myTLeft : theNewTable.getForeignFields()) {
						for (MyTable myTOrigin : myT.getForeignFields()) {
							if (myTLeft != null && myTOrigin != null && myTLeft.equals(myTOrigin) && !myTLeft.getTablename().equals("DoubleKennzahlen")) {
								o = new Object[1][2];
								o[0][0] = theNewTable.getForeignFieldName(myTLeft); o[0][1] = dbTable.getValueAt(row, myT.getForeignFieldIndex(myTOrigin)+1);
								newDBTable.setTable(theNewTable, o);
								break;
							}
						}
					}					
				}
			}
			if (o == null || o[0][1] == null) newDBTable.setTable(theNewTable, conditions);
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
