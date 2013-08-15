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
package org.hsh.bfr.db.gui.dbtable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyDBTables;
import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.MyTable;
import org.hsh.bfr.db.SendMail;
import org.hsh.bfr.db.gui.dbtable.editoren.BLOBEditor;
import org.hsh.bfr.db.gui.dbtable.editoren.MyBlobSizeRenderer;
import org.hsh.bfr.db.gui.dbtable.editoren.MyCellEditorDate;
import org.hsh.bfr.db.gui.dbtable.editoren.MyCheckBoxEditor;
import org.hsh.bfr.db.gui.dbtable.editoren.MyComboBoxEditor;
import org.hsh.bfr.db.gui.dbtable.editoren.MyImageCell;
import org.hsh.bfr.db.gui.dbtable.editoren.MyJavaTypeRenderer;
import org.hsh.bfr.db.gui.dbtable.editoren.MyLabelRenderer;
import org.hsh.bfr.db.gui.dbtable.editoren.MyMNRenderer;
import org.hsh.bfr.db.gui.dbtable.editoren.MyNewDoubleEditor;
import org.hsh.bfr.db.gui.dbtable.editoren.MyTextareaEditor;
import org.hsh.bfr.db.gui.dbtable.editoren.MyTextareaRenderer;
import org.hsh.bfr.db.gui.dbtable.header.MyTableHeaderCellRenderer;
import org.hsh.bfr.db.gui.dbtable.header.MyTableRowModel;
import org.hsh.bfr.db.gui.dbtable.header.TableRowHeaderRenderer;
import org.hsh.bfr.db.gui.dbtable.header.TableRowHeaderResizer;
import org.hsh.bfr.db.gui.dbtable.sorter.MyBooleanSorter;
import org.hsh.bfr.db.gui.dbtable.sorter.MyComboSorter;
import org.hsh.bfr.db.gui.dbtable.sorter.MyDatetimeSorter;
import org.hsh.bfr.db.gui.dbtable.sorter.MyDblKZSorter;
import org.hsh.bfr.db.gui.dbtable.sorter.MyDoubleSorter;
import org.hsh.bfr.db.gui.dbtable.sorter.MyIntegerSorter;
import org.hsh.bfr.db.gui.dbtable.sorter.MyLongSorter;
import org.hsh.bfr.db.gui.dbtable.sorter.MyOtherSorter;
import org.hsh.bfr.db.gui.dbtable.sorter.MyStringSorter;
import org.hsh.bfr.db.gui.dbtable.sorter.MyTableModel4Sorter;
import org.hsh.bfr.db.imports.InfoBox;

import quick.dbtable.Column;
import quick.dbtable.DBTable;
import quick.dbtable.Filter;

/**
 * @author Armin
 *
 */
public class MyDBTable extends DBTable implements RowSorterListener, KeyListener, ListSelectionListener, MouseListener  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -129822283276967826L;
	private MyTable actualTable = null;
	public LinkedHashMap<Object, String>[] hashBox = null;
	private MyCellPropertiesModel cpm = new MyCellPropertiesModel(this);
	private MyDBPanel myDBPanel1 = null;
	private Color defaultBgColor = this.getTable().getTableHeader().getBackground();
	private MyTableModel4Sorter sorterModel = null;
	private TableRowSorter<TableModel> sorter = null;
	private MyDBTableErrorListener dberrlis = new MyDBTableErrorListener();
	private boolean bigbigTable = false;
	private Object[][] filterConditions = null;
	private Filter theFilter = null;
	/*
	private Vector<MyMNRenderer> myDblmnr = new Vector<MyMNRenderer>();
	private boolean doEFSA = false;
	*/
			
	public MyDBTable(){
		//this.addFocusListener(this);
	}
	public void refreshSort() {
		if (sorter != null) {
			sorterModel.initArray();
			sorter.sort();			
		}
	}
	public boolean initConn(final String username, final String password) {
		Connection conn = null;
		try {
			conn = DBKernel.getDBConnection(username, password);
			DBKernel.getTempSA(DBKernel.HSHDB_PATH);
			conn = DBKernel.getDBConnection(username, password);
		}
		catch (Exception e) {
			MyLogger.handleException(e);
		}
		return initConn(conn);
	}
	public boolean initConn(final Connection conn) {
		boolean result = false;
		DBKernel.topTable = this;
		if (conn != null) {
			this.setConnection(conn);
			this.setSortEnabled(false);	
			this.autoCommit = true;
			//this.enableExcelCopyPaste(); //Lieber nicht, das gibt nur Probleme mit FremdKeys und so...
			//this.debug = true;
			this.setDBTableLocale(Locale.GERMAN);
			this.getTable().setRowHeight(50);
			
			this.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		    boolean hasMouseListener = false;
		    for (int i=0;i<this.getTable().getMouseListeners().length;i++) {
		    	if (this.getTable().getMouseListeners()[i] instanceof MyDBTable) {
		    		hasMouseListener = true;
		    		break;
		    	}
		    }
		    if (!hasMouseListener) {
				this.getTable().addMouseListener(this);
			}
	
		    boolean hasKeyListener = false;
		    for (int i=0;i<this.getTable().getKeyListeners().length;i++) {
		    	if (this.getTable().getKeyListeners()[i] instanceof MyDBTable) {
		    		hasKeyListener = true;
		    		break;
		    	}
		    }
		    if (!hasKeyListener) {
		    	removeExternalKeystrokes();
		    	this.addKeyListener(this);
		    }

		    if (!hasMouseListener && !hasKeyListener) {			    
			    this.getTable().getColumnModel().getSelectionModel().addListSelectionListener(this);
			    this.getTable().getSelectionModel().addListSelectionListener(this);		    		    
		    }
		    
		    //this.doNotUseDatabaseSort = true;
			
			result = true;
		}
		return result;
	}
	private void removeExternalKeystrokes() {
		// Ich will ale Keys lieber selbst im Griff haben, also weg:
		// Java vergibt da einige Default InputMaps!!!
		// siehe hier: http://download.oracle.com/javase/1.4.2/docs/api/javax/swing/doc-files/Key-Metal.html
		
		// Quicktable
		this.getTable().unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT,0));
		this.getTable().unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
		this.getTable().unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_F3,0));
		this.getTable().unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
		this.getTable().unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));	
/*
		// JTable
		KeyStroke[] keys = new KeyStroke[] {KeyStroke.getKeyStroke("F2")};
		InputMap im = new MyFilteringInputmap(myDB.getTable().getInputMap(JComponent.WHEN_FOCUSED), keys);
		myDB.getTable().setInputMap(JComponent.WHEN_FOCUSED, im);
		im = new MyFilteringInputmap(myDB.getTable().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT), keys);
		myDB.getTable().setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, im);
*/
	}
	public MyCellPropertiesModel getMyCellPropertiesModel() {
		return cpm;
	}

	public boolean setTable() {
		if (actualTable != null) {
			return setTable(actualTable);
		} else {
			return false;
		}
	}
	public boolean setTable(final MyTable myT) {
		return setTable(myT, filterConditions); // null
	}
	  @SuppressWarnings("unchecked")
	public boolean setTable(final MyTable myT, final Object[][] conditions) {
		boolean result = true;
		if (DBKernel.mainFrame != null) {
			DBKernel.mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}
		bigbigTable = false; //myT.getTablename().equals("ChangeLog") || myT.getTablename().equals("Messwerte") || myT.getTablename().equals("Versuchsbedingungen"); // false;
		checkUnsavedStuff();
		actualTable = myT;
		if (this.getMyCellPropertiesModel() instanceof MyCellPropertiesModel) {
			(this.getMyCellPropertiesModel()).getModifiedCellsColl().clear();
		}
		this.filterConditions = conditions;
		String where = "";
		String order = "";
		if (conditions != null) {
			where = "WHERE ";
			for (int i=0;i<conditions.length;i++) {
				if (i>0) {
					where += " AND ";
				}
				where += DBKernel.delimitL(conditions[i][0].toString()) + (conditions[i][1] == null ? " IS NULL" : "=" + conditions[i][1]);
			}	
			order = " ORDER BY " + DBKernel.delimitL("ID") + " ASC";				
			if (conditions[0][0].equals("Zielprozess")) {
				ResultSet rs = DBKernel.getResultSet("SELECT " + DBKernel.delimitL("Ausgangsprozess") + " FROM " +
						DBKernel.delimitL("Prozess_Verbindungen") +	" " + where, false);
				where = "";
				try {
					if (rs != null && rs.first()) {
						do {
							if (rs.getObject("Ausgangsprozess") != null) {
								where += " OR " + DBKernel.delimitL("Prozessdaten") + "=" + rs.getInt("Ausgangsprozess");
							}
						} while (rs.next());
						if (where.length() > 2) {
							where = " WHERE (" + where.substring(3) + ") AND " + DBKernel.delimitL("Zutat_Produkt") + "='Produkt'";
						}
					}
				}
				catch (Exception e) {MyLogger.handleException(e); where = "";}
				if (where.length() == 0) {
					where = " WHERE 1=0";
				}
			}
		}
		//if (DBKernel.debug) System.out.println(myT.getMetadata() + "\n" + where + order);
		if (actualTable.getForeignFields() != null) {
			hashBox = new LinkedHashMap[actualTable.getForeignFields().length];
		} else {hashBox = null;}
		this.clearAllSettings();
		cpm.setFoundVector(null);
		if (myT.getTablename().equals("ChangeLog")) {
			int lastID = DBKernel.getLastID("ChangeLog");
			where = " WHERE " + DBKernel.delimitL("ID") + " > " + (lastID - 1000) + " "; // 230000
		}
		this.setSelectSql(myT.getSelectSQL() + " " + where + order);
		this.setCellPropertiesModel(cpm);
		//long ttt = System.currentTimeMillis();
		try {
			this.createColumnModelFromQuery();
			//if (DBKernel.debug) {System.out.println("createColumnModelFromQuery: " + (System.currentTimeMillis() - ttt));ttt = System.currentTimeMillis();} 
			if (!myT.isReadOnly() && !this.getConnection().isReadOnly()) {this.setEditable(true);} //  && where.trim().length() == 0
			prepareColumns();
			//if (DBKernel.debug) {System.out.println("prepareColumns: " + (System.currentTimeMillis() - ttt)); }
			if (myT.isReadOnly() || this.getConnection().isReadOnly()) {this.setEditable(false);} //  || where.trim().length() > 0
			this.addDatabaseChangeListener(new MyDataChangeListener(this));
		    this.addDBTableErrorListener(dberrlis);
			this.addTableCellListener(new MyTableCellListener(this));
			this.addUpdateSql(myT.getUpdateSQL1(), myT.getUpdateSQL2());
			this.addInsertSql(myT.getInsertSQL1(), myT.getInsertSQL2());
			this.addDeleteSql(myT.getDeleteSQL1(), myT.getDeleteSQL2());
			this.setRowCountSql(myT.getRowCountSQL() + " " + where);
			//this.getTable().getTableHeader().addMouseListener(new ColumnFitAdapter());
			//this.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			this.refresh();
			//if (DBKernel.debug) {System.out.println("refresh: " + (System.currentTimeMillis() - ttt));ttt = System.currentTimeMillis();}
			if (!bigbigTable) {
				if (sorterModel != null) {
					//sorterModel.initArray();
				}
			}
			//if (DBKernel.debug) {System.out.println("sorterModel: " + (System.currentTimeMillis() - ttt)); ttt = System.currentTimeMillis();}
			//AutoFitTableColumns.autoResizeTable(this.getTable(), true, false);
			//this.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
			/*
			if (myT.isFirstTime()) {
		    this.getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		    TableColumnAdjuster tca = new TableColumnAdjuster(this.getTable());
				tca.setOnlyAdjustLarger(true); tca.setColumnDataIncluded(false);
				tca.setColumnHeaderIncluded(true); tca.setDynamicAdjustment(false);
				tca.adjustColumns();
			}
			*/
		}
	  	catch (Exception e) {
	  		result = false;
	  		MyLogger.handleException(e);
		}
		if (myDBPanel1 != null) {
			myDBPanel1.getSuchfeld().setText("");
			myDBPanel1.handleSuchfeldChange(null);
		}
	  	updateRowHeader(!bigbigTable);
		// sortieren nach ID und damit nach Zeitstempel, das neueste zuoberst!
		// Das gilt erstmal für die beiden ReadOnly Tabellen: ChangeLog und DateiSpeicher
		if (sorter != null) {
			if (myT.isReadOnly()) {
				List<SortKey> sortKeys = new ArrayList<SortKey>();
		  		sortKeys.add(new SortKey(1, SortOrder.DESCENDING));
		  		sorter.setSortKeys(sortKeys);
		  		sorter.sort();
				//this.sortByColumn(1, false);
			} 
			else if (myT.getTablename().equals("ComBaseImport")) { // nur temporär, kann irgendwann wieder weg
				List<SortKey> sortKeys = new ArrayList<SortKey>();
		  		sortKeys.add(new SortKey(3, SortOrder.DESCENDING));
		  		sorter.setSortKeys(sortKeys);
		  		sorter.sort();
			}
		}
		if (!bigbigTable) {actualTable.restoreProperties(this); syncTableRowHeights();}			
		//if (DBKernel.debug) {System.out.println("syncTableRowHeights: " + (System.currentTimeMillis() - ttt));}
		if (DBKernel.mainFrame != null) {
			DBKernel.mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		if (myDBPanel1 != null && this.getRowCount() > 0) {
			myDBPanel1.getSpinner().setValue(this.getTable().getRowHeight(0));
		}
		
		addScrollerListeners4Rendering();
		return result;
	}
	public void setMyDBPanel(final MyDBPanel myDBPanel) {
		myDBPanel1 = myDBPanel;
	}
	public MyDBPanel getMyDBPanel() {
		return myDBPanel1;
	}
	public MyTable getActualTable() {
		return actualTable;
	}

	public String getVisibleCellContent(final int row, final int col) {
		String result = null;
	    MyTable[] foreignFields = actualTable.getForeignFields();
	    if (row >= this.getRowCount()) {
			return "";
		}
	    Object o = this.getValueAt(row, col);
	    if (o != null) { // sonst Fehler in z.B. Methoden bei der Suchfunktion
	  		if (col > 0 && foreignFields != null && foreignFields.length > col-1 && foreignFields[col-1] != null && hashBox[col-1] != null) {
	  			result = hashBox[col-1].get(o).toString();
	  		}
	  		else {
	  			result = o.toString();
	  		}
	    }
		return result;
		
		/*
		 Alternativ könnte folgender Code funktionieren. Der würde nicht so viel Speicher verbrauchen (hashBox)
		        c = this.getTable().getCellRenderer(i, j).getTableCellRendererComponent(this.getTable(), this.getValueAt(i, j), false, false, i, j);
		        if (c instanceof JLabel) {
		          result[i][j - beginCol] = ( (JLabel) c).getText();
		        }
		        else {
		          result[i][j - beginCol] = this.getValueAt(i, j);
		        }

		 */
	}
	public void insertNull(final int selRow, final int selCol) {
		String tablename = this.getActualTable().getTablename();
		if (!this.actualTable.isReadOnly() && selCol > 0 && selRow >= 0 && this.getRowCount() > 0 &&
				(!tablename.equals("Matrices") && !tablename.equals("Agenzien") || DBKernel.isAdmin())) {
    	String[] mnTable = actualTable.getMNTable();
    	MyTable[] myFs = actualTable.getForeignFields();
	    	if (mnTable != null && mnTable.length > selCol - 1 && mnTable[selCol - 1] != null) {
	    		String sql = "";
				/*if (mnTable[selCol - 1].equals("DBL")) {
					sql = "DELETE FROM " + DBKernel.delimitL("Kennzahlen") +
					" WHERE " + DBKernel.delimitL("Tabelle") + "='" + tablename + "'" +
					" AND " + DBKernel.delimitL("TabellenID") + "=" + this.getValueAt(selRow, 0) +
					" AND " + DBKernel.delimitL("Spaltenname") + "='" + this.getColumn(selCol).getHeaderValue().toString() + "'";				
				}
				else*/
				if (mnTable[selCol - 1].equals("INT")) {
					sql = "";				// todo - oder auch nicht... Lieber nicht löschen! Is gut so! Wenn da was gelöscht werden soll, dann sollte das Fremdfenster geöffnet werden und dort die Zeile (Beispiel: Zutat) gelöscht werden!!!
				}
				else {
					sql = "DELETE FROM " + DBKernel.delimitL(mnTable[selCol - 1]) +
					" WHERE " + DBKernel.delimitL(tablename) + "=" + this.getValueAt(selRow, 0);									
				}
				if (sql.length() > 0) {
					DBKernel.sendRequest(sql, false);
				}
			}
	    	else if (myFs != null && myFs.length > selCol - 1 && myFs[selCol - 1] != null && myFs[selCol - 1].getTablename().equals("DoubleKennzahlen")) {
	    		DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL("DoubleKennzahlen") +
	    				" WHERE " + DBKernel.delimitL("ID") + "=" + this.getValueAt(selRow, selCol), false);
	    	}
			else {
				DBKernel.setForeignNullAt(this.getActualTable().getTablename(), this.getColumn(selCol).getHeaderValue().toString(), this.getValueAt(selRow, 0));				
			}
			//this.getActualTable().saveProperties(this);
	    	int selID = this.getSelectedID();
			//System.err.println("id "+selID);
			myRefresh(selRow, selCol);
			int newSelID = this.getSelectedID();
			if (newSelID != selID) {
				//System.err.println("id "+selID + "\t" + newSelID);
				this.setSelectedID(selID);
			}
			//this.getActualTable().restoreProperties(this);
		}
	}
	public void deleteRow() {
		String tablename = this.getActualTable().getTablename();
		int selRow = this.getSelectedRow();
		if (this.getRowCount() > 0 && selRow >= 0 && selRow < this.getRowCount() &&
				(!tablename.equals("Matrices") && !tablename.equals("Agenzien") || DBKernel.isAdmin())) {
			int id = this.getSelectedID();
			int numForeignCounts = DBKernel.getUsagecountOfID(tablename, id);
			if (numForeignCounts > 0) {
    			InfoBox ib = new InfoBox("Please delete referencing data sets first.\n" + "numForeignCounts=" + numForeignCounts + " for Table " + tablename + ", ID " + id, true, new Dimension(700, 150), null, true);
    			ib.setVisible(true);    				    			
			}
			else {
				this.getActualTable().saveProperties(this);
				
				if (this.getTable().getRowSorter() != null && this.getTable().getRowSorter().getSortKeys().size() > 0) {
					try {this.delete(new int[]{this.getTable().convertRowIndexToModel(selRow)});}
					catch (Exception e1) {System.err.println("strangeDeleteRowBehaviour: " + e1.getMessage());}
				}
				else {
					this.delete();				
				}
					
				this.save();
				this.myRefresh(selRow);
				if (myDBPanel1 != null) {
					myDBPanel1.refreshTree();
				} 
				this.getActualTable().restoreProperties(this); this.syncTableRowHeights();
			}			
		}
	}

	public void insertNewRow(final boolean copySelected, final Vector<Object> vecIn) {
		MyTable myT = this.getActualTable();
		String tablename = myT.getTablename();
		if (!tablename.equals("ProzessWorkflow") && (!tablename.equals("Matrices") && !tablename.equals("Agenzien") || DBKernel.isAdmin())) {
			//JScrollPane scroller = getScroller();
			this.getActualTable().saveProperties(this);
			// Filter und Sorter ausschalten!!!
			String filterText = "";
			boolean ichChecked = false;
			if (myDBPanel1 != null) {
				if (sorter != null) {
					sorter.setSortKeys(null);
					//sorter.sort();
				}
				filterText = myDBPanel1.getSuchfeld().getText();
				if (filterText.trim().length() > 0) {
					myDBPanel1.getSuchfeld().setText("");
				}
				ichChecked = myDBPanel1.getSuchIchCheckBox().isSelected();
				if (ichChecked) {
					myDBPanel1.getSuchIchCheckBox().setSelected(false);
				}
				myDBPanel1.handleSuchfeldChange(null);
			}
			Object oldID = null;
			if (copySelected) {
				Vector<Object> vec = new Vector<Object>();
				int row = this.getSelectedRow();
				if (row < 0) {
					return;
				}
				try {
					oldID = this.getValueAt(row, 0);					
				}
				catch (Exception e) {MyLogger.handleException(e);System.err.println("oldID = (Integer) this.getValueAt(row, 0)\t" + row);}
				for (int i=0;i<this.getColumnCount();i++) {
					String colName = this.getColumn(i).getColumnName();
					if (colName.equals("ID") || colName.equals("Geprueft")) {
						vec.add(null); // colName.equals("Guetescore") || colName.equals("Kommentar") || 
					} else {
						vec.add(this.getValueAt(row, i));
					}
				}
				this.insert(vec);
			}
			else if (vecIn != null) {
				this.insert(vecIn);
			}
			else {
				this.insertEmptyRecord();
			}
			// Hier muss man höllisch aufpassen, dass auch bei geschalteten Filtern und Sortern die korrekte ID bzw. Row rauskommt!!!!
			int newSelRow = this.getSelectedRow();
			if (filterConditions != null) {
				for (int i=0;i<filterConditions.length;i++) {
					if (filterConditions[i][1] != null) {
						for (int j=0;j<this.getColumnCount();j++) {
							if (this.getColumn(j).getColumnName().equals(filterConditions[i][0])) {
								this.setValueAt(filterConditions[i][1], newSelRow, j);
								break;
							}
						}						
					}
				}
			}
			this.save();
			Object newID = this.getValueAt(newSelRow, 0);
			if (myDBPanel1 != null) {
				if (ichChecked) {
					myDBPanel1.getSuchIchCheckBox().setSelected(true);
				}
				if (filterText.trim().length() > 0) {
					myDBPanel1.getSuchfeld().setText(filterText);
				}
				myDBPanel1.handleSuchfeldChange(null);
				//this.updateRowHeader(!bigbigTable);
			}
			if (copySelected) {
				if (newID != null && oldID != null) {
					copyDetails(myT, (Integer) oldID, (Integer) newID);
				} else {
					MyLogger.handleMessage("id != null && oldID != null " + newID + "\t" + oldID);
				}
			}
			//System.out.println(id);
			this.myRefresh(this.getRowCount()-1);
  		// evtl. HashBox neu setzen, sonst wird nicht refresht
  		MyTable[] foreignFields = actualTable.getForeignFields();
  		String[] mnTable = actualTable.getMNTable();
  		if (foreignFields != null) {
			DBKernel.refreshHashTables();
  			for (int i=0;i<foreignFields.length;i++) {
    			if (foreignFields[i] != null && (mnTable == null || mnTable[i] == null)) {
    				hashBox[i] = DBKernel.fillHashtable(foreignFields[i], "", "\n", "\n", !bigbigTable); //" | " " ; "
    				Column c = this.getColumn(i+1); 
    				c.setUserCellRenderer(new MyComboBoxEditor(hashBox[i], true));
    			}
  			}
  		}
			this.getActualTable().restoreProperties(this); this.syncTableRowHeights();
			if (newID instanceof Integer) {
				this.setSelectedID((Integer)newID);
			} else {
				this.selectCell(this.getRowCount()-1, this.getSelectedColumn());
			}
		}
	}
	public void refreshHashbox() {
  		MyTable[] foreignFields = actualTable.getForeignFields();
  		String[] mnTable = actualTable.getMNTable();
  		if (foreignFields != null) {
			DBKernel.refreshHashTables();
  			for (int i=0;i<foreignFields.length;i++) {
    			if (foreignFields[i] != null && (mnTable == null || mnTable[i] == null)) {
    				hashBox[i] = DBKernel.fillHashtable(foreignFields[i], "", "\n", "\n", !bigbigTable); //" | " " ; "
    				Column c = this.getColumn(i+1); 
    				c.setUserCellRenderer(new MyComboBoxEditor(hashBox[i], true));
    			}
  			}
  		}		
	}
	private void copyDetails(final MyTable myT, final Integer oldID, final Integer id) {
		try {
			String tablename = myT.getTablename();
			copyKennzahlen(myT, oldID, id);
		    
		    String[] mnTable = myT.getMNTable();
		    if (mnTable != null) {
			    for (int i=0;i<mnTable.length;i++) {
			    	if (mnTable[i] != null) {
			    		if (mnTable[i].equals("INT") && myT.getForeignFields()[i] != null) { // z.B. Messwerte auch kopieren
			    			String fTablename = myT.getForeignFields()[i].getTablename();
			    			String tname = fTablename.startsWith("Codes_") ? "Basis" : tablename;
			    			MyTable[] ffTs = myT.getForeignFields()[i].getForeignFields();
			    			int ii=0;
			    			for (MyTable myFT : ffTs) {
			    				if (myFT != null && myFT.getTablename().equals(tablename)) {
			    					tname = myT.getForeignFields()[i].getFieldNames()[ii];
			    					break;
			    				}
			    				ii++;
			    			}
				    		ResultSet rs = DBKernel.getResultSet("SELECT " + DBKernel.delimitL("ID") +
				    				" FROM " + DBKernel.delimitL(fTablename) +
						    		" WHERE " + DBKernel.delimitL(tname) + "=" + oldID +
						    		" ORDER BY " + DBKernel.delimitL("ID") + " ASC", false);
						    if (rs != null && rs.first()) {
						    	do  {
						    		Integer oldfID = rs.getInt("ID");
						    		MyTable myfT = MyDBTables.getTable(fTablename);
						    		System.out.println(tablename + "-" + fTablename + " - oldfID: " + oldfID + "\toldID = " + oldID);
						    		ResultSet rs2 = DBKernel.getResultSet("SELECT * FROM " + DBKernel.delimitL(fTablename) +
								    		" WHERE " + DBKernel.delimitL("ID") + "=" + oldfID, false);
						    		if (rs2 != null && rs2.first()) {
								    	do  {
								    		Integer newfID = copyRow(rs2, fTablename, tname, id);								    		
								    		System.out.println(tablename + "-" + fTablename + " - newfID: " + newfID + "\tparentID = " + id + "\ttname = " + tname);
								    		DBKernel.doMNs(myfT);
								    		copyDetails(myfT, oldfID, newfID);
								    	} while (rs2.next());
						    		}
						    		else {
						    			System.err.println("Urrghhhh, wasn denn nu los???? " + oldfID + "\t" + fTablename);
						    		}
						    	} while (rs.next());
						    }
						}					    		
			    		else {//if (!mnTable[i].equals("DBL")) { // wurde ja schon bei copyKennzahlen gemacht, oder?
				    		MyTable myMNT = MyDBTables.getTable(mnTable[i]);
				    		String sql = "SELECT * FROM " + DBKernel.delimitL(mnTable[i]) + " WHERE ";
				    		if (tablename.equals("GeschaetzteModelle")) {
				    			sql += DBKernel.delimitL("GeschaetztesModell");
				    		}
				    		else {
				    			sql += DBKernel.delimitL(tablename);
				    		}
				    		sql += "=" + oldID;
				    		ResultSet rs = DBKernel.getResultSet(sql, false);
				    		//System.err.println(mnTable[i] + "\t" + tablename + "\t" + myMNT.getFieldNames()[0] + "\t" + myMNT.getFieldNames()[1]);
						    if (rs != null && rs.first()) {
						    	do  {
						    		Integer newID = copyRow(rs, mnTable[i], tablename, id);
								    copyKennzahlen(myMNT, rs.getInt(1), newID);
						    	} while (rs.next());
						    }
				    	}
			    	}
			    }				    	
		    }
		}
		catch (Exception e) {MyLogger.handleException(e);}		
	}
	private Integer copyRow(final ResultSet rs, final String tablename, final String parentTable, final Integer parentID) {
		Integer result = null;
		try {
			String columns = "";
			String vals = "";
			for (j=2;j<=rs.getMetaData().getColumnCount();j++) {
				if (rs.getObject(j) != null) {
	    			columns += "," + DBKernel.delimitL(rs.getMetaData().getColumnName(j));
	    			String fname = rs.getMetaData().getColumnName(j);
	    			if (fname.equals(parentTable)) {
						vals += ",'" + parentID + "'";
					} else {
						vals += ",'" + rs.getString(j) + "'";
					}		
				}
			}
			if (columns.length() > 0) {
				columns = columns.substring(1);
				vals = vals.substring(1);
				PreparedStatement psmt = DBKernel.getDBConnection().prepareStatement("INSERT INTO " + DBKernel.delimitL(tablename) +
	    				" (" + columns + ") VALUES (" + vals + ")", Statement.RETURN_GENERATED_KEYS);
				if (psmt.executeUpdate() > 0) {
					result = DBKernel.getLastInsertedID(psmt);
				}
			}		
		}
		catch (Exception e) {e.printStackTrace();}
		return result;
	}
	
	private void copyKennzahlen(final MyTable myT, final Integer oldID, final Integer newID) throws Exception {
		copyKennzahlen(myT, oldID, newID, null);
	}
	private void copyKennzahlen(final MyTable myT, final Integer oldID, final Integer newID, final Integer toRow) throws Exception {
		MyTable[] foreignFields = myT.getForeignFields();
		if (foreignFields != null) {
			for (int i=0; i<foreignFields.length; i++) {
				if (foreignFields[i] != null && foreignFields[i].getTablename().equals("DoubleKennzahlen")) {
					ResultSet rs = DBKernel.getResultSet("SELECT " + DBKernel.delimitL(myT.getFieldNames()[i]) +
							" FROM " + DBKernel.delimitL(myT.getTablename()) +
			    		" WHERE " + DBKernel.delimitL("ID") + "=" + oldID, false);
				    if (rs != null && rs.first()) {
				    	if (rs.getObject(1) != null) {
				    		Integer oldfID = rs.getInt(1);
				    		ResultSet rs2 = DBKernel.getResultSet("SELECT * FROM " + DBKernel.delimitL("DoubleKennzahlen") +
						    		" WHERE " + DBKernel.delimitL("ID") + "=" + oldfID, false);
				    		if (rs2 != null && rs2.first()) {
						    		Integer newfID = copyRow(rs2, "DoubleKennzahlen", "", null);
						    		/*
						    		System.err.println("UPDATE " + DBKernel.delimitL(myT.getTablename()) +
						    				" SET " + DBKernel.delimitL(myT.getFieldNames()[i]) + "=" + newfID +
						    				" WHERE " + DBKernel.delimitL("ID") + "=" + newID);
						    		*/
						    		DBKernel.sendRequest("UPDATE " + DBKernel.delimitL(myT.getTablename()) +
						    				" SET " + DBKernel.delimitL(myT.getFieldNames()[i]) + "=" + newfID +
						    				" WHERE " + DBKernel.delimitL("ID") + "=" + newID, false);
						    		// Es ist hier essentiell, dass im DBTable der Wert gesetzt wird, weil sonst ein späterer "myRefresh" dafür sorgt, dass die DB Einträge wieder mit NULL überschrieben werden
						    		// Alternativ könnte man this.save(); an der richtigen Stelle setzen?!?
						    		if (toRow != null && actualTable.getTablename().equals(myT.getTablename())) {
										this.setValueAt(newfID, toRow, i+1);
									}
				    		}
				    		else {
								MyLogger.handleMessage("aaahhUrrghhhh, wasn denn nu los???? " + oldfID + "\t" + "DoubleKennzahlen");
				    		}
				    	}
				    }
				}
			}
		}
	}
	
	/*
	public int getSelectedRow() {
		int selRow = super.getSelectedRow();
		if (this.getRowCount() > 0) {
			System.out.println(selRow + "\t" + this.getValueAt(selRow, 0) + "\t" +
				this.getTable().convertRowIndexToView(selRow) + "\t" + this.getValueAt(this.getTable().convertRowIndexToView(selRow), 0));
		}
		return selRow;
	}
	public void selectCell(int row, int col, boolean focus) {
		if (this.getRowCount() > 0 && this.getTable().getRowSorter() != null && this.getTable().getRowSorter().getSortKeys().size() > 0) {
			System.out.println(row + "\t" + this.getTable().convertRowIndexToView(row) + "\t" + this.getTable().convertRowIndexToModel(row));
			super.selectCell(row, col, focus);
		}
		else {
			super.selectCell(row, col, focus);
		}
	}
	public void selectCell(int row, int col) {
		this.selectCell(row, col, true);
	}
	*/
	public void myPrint() {
	  	// Zuerst DBTable aktualisieren:
	    try {this.refresh();}
	    catch (SQLException e1) {MyLogger.handleException(e1);}
	    /*
	    FormatTable ft = FormatGeneral.getDefaultSettings(java.util.Locale.GERMAN).getFormatTable();
	    FormatGeneral fg = FormatGeneral.getDefaultSettings(java.util.Locale.GERMAN);
	    ft.addDedicatedRow(new DedicatedRow(0, Color.WHITE, new Font("Dialog", Font.PLAIN, 11), Color.BLACK)); // einen Default setzen, sonst Fehler von KBTablePrinter
	    fg.setFormatTable(ft);
        KBTablePrinter.showPreviewFrame(getColumnTitles(), getVisibleData(), fg, false, false, false);
        */
	    this.printPreview();		
	}
	/*
	private Object[][] getVisibleData() {
		Object[][] result = new Object[this.getRowCount()][this.getColumnCount()];
		int i, j;
		for (i = 0; i < this.getRowCount(); i++) {
			for (j = 0; j < this.getColumnCount(); j++) {
				result [i][j] = this.getVisibleCellContent(i, j);
		    }
		}
		return result;
	}
	private String[] getColumnTitles() {
		String[] result = new String[this.getColumnCount()];
		for (int j = 0; j < this.getColumnCount(); j++) {
		      result[j] = this.getColumn(j).getHeaderValue().toString();
		}
		return result;
	}
	*/
	public void myRefresh() {
		myRefresh(this.getSelectedRow());
	}
	public void myRefresh(final int row) {
		if (!bigbigTable)
		 {
			myRefresh(row, this.getSelectedColumn()); // bei bigbigTable muss erstmal nicht nen autoupdate nach afterUpdate von MyDataChangeListener gemacht werden.. is sonst zu lahm
		}
	}
	
	public void setFilter(Filter mf) {
		theFilter = mf;
    	if (theFilter != null) {
    		this.filter(theFilter);
    		//this.setReadOnly(true);
    	}
	}
    	
	public void myRefresh(final int row, final int col) {
		JScrollPane scroller = getScroller();
		int scrollVal = (scroller == null) ? -1 : scroller.getVerticalScrollBar().getValue();
		int hscrollVal = (scroller == null) ? -1 : scroller.getHorizontalScrollBar().getValue();
//		System.err.println(row+" - "+getSelectedID());
		int id = getSelectedID();
	    try {
	    	this.refresh();
	    	
	    	if (theFilter != null) {
	    		this.filter(theFilter);
	    		//this.setReadOnly(true);
	    	}
	    	
	    }
	    catch (Exception e1) {
	    	MyLogger.handleException(e1);
	    }
//		System.err.println(row+" - "+getSelectedID());
if (myDBPanel1 != null) {
	myDBPanel1.handleSuchfeldChange(null);
}
	    this.updateRowHeader(!bigbigTable);
	    if (col >= 0 && col < this.getColumnCount()) {
			this.getTable().setColumnSelectionInterval(col, col);
		}
	    
	    if (row >= 0 && row < this.getRowCount()) {
	    	this.setRowSelectionInterval(row, row);
	    	this.goTo(row);
	    }
	    setSelectedID(id);
//	System.err.println(row+" - "+getSelectedID());
//if (myDBPanel1 != null) myDBPanel1.handleSuchfeldChange(null);
//this.updateRowHeader(!bigbigTable);
	    if (sorterModel != null) {
			sorterModel.initArray();
		}
	    if (scrollVal >= 0) {
			this.getScroller().getVerticalScrollBar().setValue(scrollVal);
		}
	    if (hscrollVal >= 0) {
			this.getScroller().getHorizontalScrollBar().setValue(hscrollVal);
		}
		this.getTable().requestFocus();
	}
	private void initSorter() {
		// Sorter initialisieren
		if (actualTable.isReadOnly()) {
			sorterModel = new MyTableModel4Sorter(this); 
			sorter = new TableRowSorter<TableModel>(sorterModel); //this.getTable().getModel());//new MyTableModel4Sorter(this)); //
			sorter.setMaxSortKeys(1); // eins genügt wohl
			sorter.addRowSorterListener(this);
			sorter.setSortsOnUpdates(false); // lieber nicht, danach ist alles immer so unübersichtlich.
			this.getTable().setRowSorter(sorter);      
      			
			sorter.setComparator(1, new MyIntegerSorter()); // ID
		}
		else {
			sorterModel = null;
			sorter = null;
			this.getTable().setRowSorter(null);
		}
      		
	}
	private void prepareColumns() {
		Column c = this.getColumn(0);
		c.setReadOnly(true);
		c.setPreferredWidth(50);

		if (actualTable != null) {
			this.getTable().getTableHeader().setReorderingAllowed(false);
			
			//this.setComparator(new MyDBComparator(this)); this.setSortEnabled(true);	

      //this.getTable().getTableHeader().addMouseListener((new MyTableHeaderMouseListener(this)));		
      
			initSorter();
/*
      for (int i=0;i<this.getTable().getColumnCount();i++) {
      	System.out.println(i + "\t" + this.getTable().convertColumnIndexToModel(i) + "\t" + this.getTable().convertColumnIndexToView(i)
      			+ "\t" + this.getTable().getModel().getColumnCount());
      }
*/    
			TableColumnModel tcm = this.getTable().getTableHeader().getColumnModel();
			tcm.getColumn(0).setHeaderRenderer(new MyTableHeaderCellRenderer(this, defaultBgColor, null));
			String[] fieldTypes = actualTable.getFieldTypes();
			String[] fieldComments = actualTable.getFieldComments();
			if (fieldTypes != null) {
				ResourceBundle bundle = ResourceBundle.getBundle("org.hsh.bfr.db.gui.PanelProps_" + DBKernel.getLanguage());
				for (int i=0; i<fieldTypes.length; i++) {		
					MyTableHeaderCellRenderer mthcr = null;
					c = this.getColumn(i+1);
					c.setReadOnly(false);
					if (fieldTypes[i].equals("OTHER")) {
					    c.setPreferredWidth(100);
				      //c.setUserCellEditor(new MyJavaTypeRenderer());
				      c.setUserCellRenderer(new MyJavaTypeRenderer());
				      mthcr = new MyTableHeaderCellRenderer(this, defaultBgColor, fieldComments[i]);
					    tcm.getColumn(i+1).setHeaderRenderer(mthcr);
					    if (sorter != null) {
							sorter.setComparator(i+2, new MyOtherSorter());
						}
				  }
				else if (fieldTypes[i].equals("BOOLEAN")) {
			    	String fname = actualTable.getFieldNames()[i];
					if (fname.startsWith("geschätzt")) {
						c.setPreferredWidth(105);
					} else if (fname.equals("Aufkonzentrierung")) {
						c.setPreferredWidth(120);
					} else if (fname.equals("AuftragsAnnahme")) {
						c.setPreferredWidth(115);
					} else if (fname.equals("RNA_Extraktion")) {
						c.setPreferredWidth(110);
					} else if (fname.equals("DNA_Extraktion")) {
						c.setPreferredWidth(110);
					} else if (fname.equals("Protein_Extraktion")) {
						c.setPreferredWidth(110);
					} else if (fname.equals("Quantitativ")) {
						c.setPreferredWidth(100);
					} else if (fname.equals("Meldepflicht")) {
						c.setPreferredWidth(80);
					} else if (fname.equals("Identifizierung")) {
						c.setPreferredWidth(100);
					} else if (fname.equals("Typisierung")) {
						c.setPreferredWidth(150);
					} else if (fname.equals("Homogenisierung")) {
						c.setPreferredWidth(120);
					} else if (fname.equals("Laienpersonal")) {
						c.setPreferredWidth(120);
					} else if (fname.equals("Spezialequipment")) {
						c.setPreferredWidth(120);
					} else if (fname.equals("ansteckend")) {
						c.setPreferredWidth(120);
					} else if (fname.equals("Therapie")) {
						c.setPreferredWidth(100);
					} else if (fname.equals("Antidot")) {
						c.setPreferredWidth(100);
					} else if (fname.equals("Impfung")) {
						c.setPreferredWidth(100);
					} else if (fname.equals("Spezialequipment")) {
						c.setPreferredWidth(120);
					} else if (fname.equals("Aufbereitungsverfahren")) {
						c.setPreferredWidth(140);
					} else if (fname.equals("Nachweisverfahren")) {
						c.setPreferredWidth(120);
					} else if (fname.equals("Labornachweis")) {
						c.setPreferredWidth(80);
					} else if (fname.equals("FallErfuellt")) {
						c.setPreferredWidth(80);
					} else {
						c.setPreferredWidth(50);
					}
			      c.setUserCellEditor(new MyCheckBoxEditor(bundle.getString("Häkchen vorhanden = JA"), this, false));
			      c.setUserCellRenderer(new MyCheckBoxEditor(bundle.getString("Häkchen vorhanden = JA"), this, false));
			      mthcr = new MyTableHeaderCellRenderer(this, defaultBgColor, fieldComments[i]);
				    tcm.getColumn(i+1).setHeaderRenderer(mthcr);
				    if (sorter != null) {
						sorter.setComparator(i+2, new MyBooleanSorter());
					}
				  }
					else if (fieldTypes[i].startsWith("BLOB(")) {
						c.setPreferredWidth(100);
						if (actualTable.getTablename().equals("DateiSpeicher")) {
							c.setVisible(false);
						}
						else {
					    	String endungen = "*.pdf, *.doc";
					    	if (this.getActualTable().getTablename().equals("ProzessWorkflow")) {
								endungen = "*.xml";
							}
					    	else if (this.getActualTable().getTablename().equals("PMMLabWorkflows")) {
								endungen = "*.zip";
							}
					    	BLOBEditor be = new BLOBEditor(endungen, this, i+1);
						    c.setCellEditor(be);				    	
						    if (sorter != null) {
								sorter.setComparator(i+2, new MyStringSorter());
							}
					    }
					    mthcr = new MyTableHeaderCellRenderer(this, Color.GRAY, fieldComments[i]);
					    tcm.getColumn(i+1).setHeaderRenderer(mthcr);
					  }
					else if (fieldTypes[i].startsWith("VARCHAR(")) {
				    	String fname = actualTable.getFieldNames()[i];
						if (fname.equals("ProzessElementSubKategorie")) {
							c.setPreferredWidth(175);
						} else if (fname.equals("ProzessElementSubKategorie_engl")) {
							c.setPreferredWidth(220);
						} else if (fname.equals("ProzessElementKategorie_engl")) {
							c.setPreferredWidth(190);
						} else if (fname.equals("WissenschaftlicheBezeichnung")) {
							c.setPreferredWidth(190);
						} else if (fname.equals("Extraktionssystem_Bezeichnung")) {
							c.setPreferredWidth(190);
						} else if (fname.equals("Code")) {
							c.setPreferredWidth(50);
						} else if (fname.equals("VATnumber")) {
							c.setPreferredWidth(90);
						} else if (fname.equals("Betriebsart")) {
							c.setPreferredWidth(80);
						} else if (fname.equals("Betriebsnummer")) {
							c.setPreferredWidth(110);
						} else if (fname.startsWith("BezUnits")) {
							c.setPreferredWidth(70);
						} else if (fname.equals("UnitEinheit")) {
							c.setPreferredWidth(70);
						} else if (fname.equals("ChargenNr")) {
							c.setPreferredWidth(100);
						} else if (fname.equals("Artikelnummer")) {
							c.setPreferredWidth(100);
						} else if (fname.equals("VATnumber")) {
							c.setPreferredWidth(110);
						} else {
							c.setPreferredWidth(150);
						}
						//String tname = actualTable.getTablename(); 
						/*
						if (fname.equals("Sonstiges") &&
								(tname.equals("Versuchsbedingungen") || tname.equals("Messwerte") || tname.equals("Prozessdaten") || tname.equals("Zutatendaten"))) {
							c.setReadOnly(true);
							//CB_ConditionsEditor cbce = new CB_ConditionsEditor();
							//c.setCellEditor(cbce);
						}
						else {
						*/
							//c.setType(Types.LONGVARCHAR);
							c.setUserCellEditor(new MyTextareaEditor(this, actualTable.getTablename(), actualTable.getFieldNames()[i]));							
						//}
						c.setUserCellRenderer(new MyTextareaRenderer());
						mthcr = new MyTableHeaderCellRenderer(this, defaultBgColor, fieldComments[i]);
					    tcm.getColumn(i+1).setHeaderRenderer(mthcr);
					    if (sorter != null) {
							sorter.setComparator(i+2, new MyStringSorter());
						}
					}
					else if (actualTable.getFieldNames()[i].equals("Dateigroesse")) { // Dateigroesse  && actualTable.getTablename().equals("DateiSpeicher")
					    c.setPreferredWidth(90);
				      c.setUserCellRenderer(new MyBlobSizeRenderer());
				      mthcr = new MyTableHeaderCellRenderer(this, defaultBgColor, fieldComments[i]);
					    tcm.getColumn(i+1).setHeaderRenderer(mthcr);
					    if (sorter != null) {
							sorter.setComparator(i+2, new MyIntegerSorter());
						}
					}
					else if (fieldTypes[i].startsWith("DATE")) { // DATE, DATETIME
						//c.setPreferredWidth(75);
					    c.setUserCellRenderer(new MyImageCell(fieldTypes[i].equals("DATETIME") ? MyImageCell.DATETIME : MyImageCell.DATE));
					    c.setUserCellEditor(new MyCellEditorDate());
				    	String fname = actualTable.getFieldNames()[i];
					    if (fname.equals("MHD")) {
					    	c.setPreferredWidth(70);
					    }
					    else if (fname.equals("Lieferdatum")) {
					    	c.setPreferredWidth(90);
					    }
					    else if (fname.equals("Zeitstempel")) {
					    	c.setPreferredWidth(140);
					    }
					    else if (fname.equals("DatumHoehepunkt")) {
					    	c.setPreferredWidth(120);
					    }
					    else if (fname.equals("Herstellungsdatum")) {
					    	c.setPreferredWidth(120);
					    }
					    else {
					    	c.setPreferredWidth(100); // datum
					    }
						//c.setType(Types.LONGVARCHAR);
						mthcr = new MyTableHeaderCellRenderer(this, defaultBgColor, fieldComments[i]); tcm.getColumn(i+1).setHeaderRenderer(mthcr);
						if (sorter != null) {
							sorter.setComparator(i+2, new MyDatetimeSorter());
						}
					}
					else { // INTEGER, DOUBLE
						c.setPreferredWidth(75);
						c.setUserCellRenderer(new MyLabelRenderer());
						String tooltip = fieldComments[i];
				    	String fname = actualTable.getFieldNames()[i];
						if (tooltip == null) {
							tooltip = fname;
						}
					    
					    if (fieldTypes[i].equals("DATETIME")) {
						    c.setUserCellRenderer(new MyImageCell(MyImageCell.DATETIME));
							c.setPreferredWidth(125);
							if (sorter != null) {
								sorter.setComparator(i+2, new MyDatetimeSorter());
							}
					    }
					    else if (fieldTypes[i].equals("DOUBLE")) {
							if (fname.equals("Nachweisgrenze")) {
								c.setPreferredWidth(120);
							} else if (fname.equals("Personalressourcen")) {
								c.setPreferredWidth(135);
							} else if (fname.equals("Temperatur")) {
								c.setPreferredWidth(120);
							} else if (fname.equals("Luftfeuchtigkeit")) {
								c.setPreferredWidth(140);
							} else if (fname.equals("Produktionsmenge")) {
								c.setPreferredWidth(120);
							} else if (fname.equals("Wiederfindungsrate")) {
								c.setPreferredWidth(140);
							} else if (fname.equals("Letalitaetsdosis100")) {
								c.setPreferredWidth(110);
							} else if (fname.equals("Konzentration_GKZ")) {
								c.setPreferredWidth(120);
							} else if (fname.equals("Konzentration")) {
								c.setPreferredWidth(170);
							} else if (fname.equals("Unitmenge")) {
								c.setPreferredWidth(80);
							} else if (fname.startsWith("#Units")) {
								c.setPreferredWidth(60);
							} else {
								c.setPreferredWidth(100);
							}
					    	c.setScale(32);
					    	c.setPrecision(64);
					    	//System.err.println(c.getScale() + "\t" + c.getPrecision());
					    	if (actualTable.getForeignFields() != null && actualTable.getForeignFields().length > i &&
					    			actualTable.getForeignFields()[i] != null) { // Es gibt hier einen Fremdtable!
					    		if (!actualTable.getForeignFields()[i].getTablename().equals("DoubleKennzahlen")) {
					    			System.err.println("Wasn jetzt los? keine DoubleKennzahlen???");
					    		}
					    		if (sorter != null) {
									sorter.setComparator(i+2, new MyDblKZSorter());
								}			
						    	//tooltip += "\nHier sind mehrere Einträge/Kennzahlen möglich!";
					    	}
					    	else {
					    		if (sorter != null) {
									sorter.setComparator(i+2, new MyDoubleSorter());
								}
					    	}
					    	/*
					    	String[] mnTable = actualTable.getMNTable();
					    	if (mnTable != null && i < mnTable.length && mnTable[i] != null && mnTable[i].equals("DBL")) {
					    		MyMNRenderer mymnr = new MyMNRenderer(this, i);
						    	c.setUserCellRenderer(mymnr);
					    		myDblmnr.add(mymnr);
									//updateDBLHash(i); c.setUserCellRenderer(new MyComboBoxEditor(hashBox[i], true));
						    	c.setReadOnly(true);					    		
						    	tooltip += "\nHier sind mehrere Einträge/Kennzahlen möglich!";
					    	}
					    	*/
					    	//c.setPrecision(6);
					    	//tooltip += "\n<ACHTUNG: als Trennzeichen ist es hier notwendig einen Punkt zu benutzen und kein Komma (Bsp: 0.45)>";
					    }
					    else if (fieldTypes[i].equals("INTEGER")) {
					    	//String fname = actualTable.getFieldNames()[i];
							if (fname.equals("Wiederholungen")) {
								c.setPreferredWidth(120);
							} else if (fname.equals("FreigabeModus")) {
								c.setPreferredWidth(110);
							} else if (fname.equals("Gramfaerbung")) {
								c.setPreferredWidth(100);
							} else if (fname.equals("Humanpathogen")) {
								c.setPreferredWidth(110);
							} else if (fname.equals("Risikogruppe")) {
								c.setPreferredWidth(100);
							} else if (fname.equals("Klassifizierung")) {
								c.setPreferredWidth(100);
							} else if (fname.equals("KapazitaetEinheit")) {
								c.setPreferredWidth(130);
							} else if (fname.equals("#Chargenunits")) {
								c.setPreferredWidth(130);
							} else if (fname.equals("Therapie_Letal")) {
								c.setPreferredWidth(130);
							} else if (fname.equals("AnzahlFaelle")) {
								c.setPreferredWidth(110);
							} else if (fname.equals("AnzahlLabornachweise")) {
								c.setPreferredWidth(120);
							}
					    	if (actualTable.getForeignFields() != null && actualTable.getForeignFields().length > i &&
					    			actualTable.getForeignFields()[i] != null) { // Es gibt hier einen Fremdtable!
					    		if (sorter != null) {
									sorter.setComparator(i+2, new MyComboSorter(hashBox, i));
								}			
					    	}
					    	else {
					    		if (sorter != null) {
									sorter.setComparator(i+2, new MyIntegerSorter());
								}				    		
					    	}
					    	String[] mnTable = actualTable.getMNTable();
					    	if (mnTable != null && i < mnTable.length && mnTable[i] != null && mnTable[i].equals("INT")) {
						    	c.setUserCellRenderer(new MyMNRenderer(this, i));
						    	c.setReadOnly(true);					    		
						    	//tooltip += "\nHier sind mehrere Einträge möglich!";
					    	}
					    }
					    else if (fieldTypes[i].equals("BIGINT")) {
					    	if (sorter != null) {
								sorter.setComparator(i+2, new MyLongSorter());
							}	
					    	if (actualTable.getFieldNames()[i].equals("HIT_Nummer")) {
								c.setPreferredWidth(90);
							}
					    }
						mthcr = new MyTableHeaderCellRenderer(this, defaultBgColor, tooltip);
					    tcm.getColumn(i+1).setHeaderRenderer(mthcr);
				    
				    //System.out.println(i + "\t" + fieldTypes[i]);
					}
					//String titleStr = actualTable.getFieldNames()[i];
					//if (c.getPreferredWidth() < 7*titleStr.length()) c.setPreferredWidth(titleStr.length() * 7);
					
					//System.out.println(titleStr + "\t" + titleStr.length() + "\t" + c.getPreferredWidth() + "\t" + packColumn(this.getTable(), mthcr, c.getColumnName(), 0));
				}
				int extraFields = 0;
				if (!actualTable.getHideScore()) {
					extraFields++;
				      c = this.getColumn(fieldTypes.length+extraFields); // Guetescore
				      c.setReadOnly(false);
				      c.setPreferredWidth(110);
				      Hashtable<Integer, ImageIcon> h = new Hashtable<Integer, ImageIcon>();
				      h.put(new Integer(1), new ImageIcon(this.getClass().getResource("/org/hsh/bfr/db/gui/res/green.gif")));
				      h.put(new Integer(2), new ImageIcon(this.getClass().getResource("/org/hsh/bfr/db/gui/res/yellow.gif")));
				      h.put(new Integer(3), new ImageIcon(this.getClass().getResource("/org/hsh/bfr/db/gui/res/red.gif")));
				      this.setCellComponent(c, Column.IMAGE_CELL, h);
					    tcm.getColumn(fieldTypes.length+extraFields).setHeaderRenderer(new MyTableHeaderCellRenderer(this, defaultBgColor, "Hier kann eine SUBJEKTIVE Einschätzung der Güte des Datensatzes (des Experiments, der Methode, ...) abgegeben werden\nACHTUNG: nicht vergessen diese Einschätzung zu kommentieren im Feld Kommentar"));
					    //if (actualTable.getHideScore()) c.setVisible(false);
					    if (sorter != null) {
							sorter.setComparator(fieldTypes.length+extraFields+1, new MyIntegerSorter());
						}
				}
				if (!actualTable.getHideKommentar()) {
					extraFields++;
				      c = this.getColumn(fieldTypes.length+extraFields); // Kommentar
				      c.setReadOnly(false); // Kommentar
				      c.setPreferredWidth(150);
							//c.setType(Types.LONGVARCHAR); // Kommentar
				      c.setUserCellEditor(new MyTextareaEditor(this, actualTable.getTablename(), "Kommentar")); c.setUserCellRenderer(new MyTextareaRenderer());
					    tcm.getColumn(fieldTypes.length+extraFields).setHeaderRenderer(new MyTableHeaderCellRenderer(this, defaultBgColor, null));
				      //if (actualTable.getHideKommentar()) c.setVisible(false);
					    if (sorter != null) {
							sorter.setComparator(fieldTypes.length+extraFields+1, new MyStringSorter());
						}
				}

				if (!actualTable.getHideTested()) {
					extraFields++;
				      //System.out.println(sorterLfd + "\t" + this.getTable().getModel().getColumnCount());
				      c = this.getColumn(fieldTypes.length+extraFields); // Geprueft
				      c.setReadOnly(false); 
					    c.setPreferredWidth(70);
				      c.setUserCellEditor(new MyCheckBoxEditor(bundle.getString("Häkchen vorhanden = Datensatz wurde von einer zweiten Person auf Richtigkeit überprüft"), this, true));
				      c.setUserCellRenderer(new MyCheckBoxEditor(bundle.getString("Häkchen vorhanden = Datensatz wurde von einer zweiten Person auf Richtigkeit überprüft"), this, true));
					    tcm.getColumn(fieldTypes.length+extraFields).setHeaderRenderer(new MyTableHeaderCellRenderer(this, defaultBgColor, "Datensätze können von einem anderen Benutzer auf Richtigkeit hin geprüft werden.\nDies erhöht die Güte des Eintrages."));
					    //if (actualTable.getHideTested()) c.setVisible(false);
					    if (sorter != null) {
							sorter.setComparator(fieldTypes.length+extraFields+1, new MyBooleanSorter());
						}
				}
		    

				LinkedHashMap<Object, String>[] foreignHashs = actualTable.getForeignHashs();
				if (foreignHashs != null) {
					for (int i=0; i<foreignHashs.length; i++) {
						if (foreignHashs[i] != null) {
							c = this.getColumn(i+1); 
							c.setUserCellEditor(new MyComboBoxEditor(foreignHashs[i], false)); c.setUserCellRenderer(new MyComboBoxEditor(foreignHashs[i], true));
						}
					}
				}					    

				MyTable[] foreignFields = actualTable.getForeignFields();
				if (foreignFields != null) {
					//long ttt = System.currentTimeMillis();
					DBKernel.refreshHashTables();
					for (int i=0; i<foreignFields.length; i++) {
						if (foreignFields[i] != null) {
							c = this.getColumn(i+1); 
							c.setPreferredWidth(200);
							c.setReadOnly(true);
							String[] mnTable = actualTable.getMNTable();
							if (mnTable != null && i < mnTable.length && mnTable[i] != null && mnTable[i].length() > 0) {
								c.setUserCellRenderer(new MyMNRenderer(this, i));																
							}
							else {
								hashBox[i] = DBKernel.fillHashtable(foreignFields[i], "", "\n", "\n", !bigbigTable); //" | " " ; "
								//c.setUserCellEditor(new MyComboBoxEditor(hashBox[i], false));
								c.setUserCellRenderer(new MyComboBoxEditor(hashBox[i], true));								
							}
							if (foreignFields[i].getTablename().equals("DoubleKennzahlen")) {
								c.setPreferredWidth(100);
							}
							else {
								tcm.getColumn(i+1).setHeaderRenderer(new MyTableHeaderCellRenderer(this, Color.LIGHT_GRAY, (fieldComments[i] == null ? actualTable.getFieldNames()[i] : fieldComments[i])));	//  + "\n<rechte Maustaste oder Ctrl+Enter>"							
							}
							//if (DBKernel.debug) {System.out.println("foreignFields (" + foreignFields[i].getTablename() + "): " + (System.currentTimeMillis() - ttt));ttt = System.currentTimeMillis();} 
						}
					}
					//if (DBKernel.debug) {System.out.println("foreignFields (Teilmenge von prepareColymns): " + (System.currentTimeMillis() - ttt));}
				}			
			}						
		}		
	}

	public void updateRowHeader(final boolean setVisible) {
		
		JScrollPane scroller = getScroller();
	    if (scroller != null) {
	    	if (setVisible) {
	        	int dataSize = this.getDataArray().length;
			      JTable rowHeader = new JTable(new MyTableRowModel(dataSize));
			      rowHeader.setFocusable(false);
			      LookAndFeel.installColorsAndFont(rowHeader, "TableHeader.background", "TableHeader.foreground", "TableHeader.font");
			
			      rowHeader.setIntercellSpacing(new Dimension(0, 0));
			      Dimension d = rowHeader.getPreferredScrollableViewportSize();
				    if (dataSize >= 10000) {
						d.width = 48;
					} else if (dataSize >= 1000) {
						d.width = 36;
					} else {
						d.width = 30;
					}
			      rowHeader.setPreferredScrollableViewportSize(d);
			      rowHeader.setRowHeight(this.getTable().getRowHeight());
			      rowHeader.setDefaultRenderer(Object.class, new TableRowHeaderRenderer());//new MyTableHeaderCellRenderer(this, defaultBgColor, ""));//new TableRowHeaderRenderer());
			    
			      scroller.setRowHeaderView(rowHeader);
				    
			      new TableRowHeaderResizer(rowHeader, this.getTable());
			}
	    	else {
	    		scroller.setRowHeaderView(null);
	    	}
    	}
	}
	public void checkUnsavedStuff() {
		checkUnsavedStuff(true);
	}
	public void checkUnsavedStuff(final boolean saveProps) {
		//if (actualTable == null || actualTable.isReadOnly()) System.err.println(" readonly, but saved??? " + actualTable);
		// eigentlich würde es genügen, wenn man nur this.save() ausführt. this.save() hat selbst eine Routine, die checkt, ob was geändert wurde oder nicht, d.h. es wird nicht in jedem Fall abgespeichert
		if (theFilter != null) return;
		if (this.getEditingColumn() >= 0 && this.getEditingRow() >= 0) {
			this.save();
		}		
		else if (actualTable != null) {
			this.save();
		}
		if (this.getMyCellPropertiesModel() instanceof MyCellPropertiesModel) {
			int num =(this.getMyCellPropertiesModel()).getModifiedCellsColl().size(); 
			if (num > 0) {
				MyLogger.handleMessage(actualTable.getTablename() + ": Nicht alles konnte abgespeichert werden.\n" + num + " (rot markierte) Änderungen gehen verloren... Hmmm, stimmt das?");
        		//InfoBox ib = new InfoBox("Nicht alles konnte abgespeichert werden.\n" + num + " (rot markierte) Änderungen gehen verloren.", true, new Dimension(300, 300), null, true);
        		//ib.setVisible(true);    				  										        			
			}
		}
		if (saveProps && actualTable != null && !bigbigTable) {
			actualTable.saveProperties(this);
		}
		if (myDBPanel1 != null) {
			myDBPanel1.checkUnsavedStuffInForm();
		}			
	}

	@Override
	public void valueChanged(final ListSelectionEvent e) {
		if (myDBPanel1 != null) {
			if (e.getFirstIndex() > 0 || e.getValueIsAdjusting()) { // Wenn ich diese Abfrage nicht mache, dann gibt es Probleme bei getSelectedID: es wird die vor "setSelectedID" selektierte row zurückgegeben. Dies ist z.B. der Fall, wenn im dbTree etwas selektiert wird. Beispiel: Matrices -> Favoriten -> Kuhmilch. Dann wird im Endeffekt eine andere "virtuelle" Kuhm,ilch selektiert im dbTree und zwar unter BLS
				//System.out.println(e + "\t" + e.getFirstIndex());
				myDBPanel1.setSelectedID(getSelectedID());
				myDBPanel1.setBLOBEnabled(isFilledBlobField(this.getTable().getSelectedRow(), this.getTable().getSelectedColumn()));							
			}
		}
	}
	private int getRowFromID(final int id) {
		int result = -1;
		if (id > 0) {
			for (int row=0;row<this.getRowCount();row++) {
				// evtl. sollte hier ein Thread eingebaut werden - wegen Gefahr zu langsam...
				Object o = this.getValueAt(row, 0);
				if (o instanceof Integer && ((Integer) o) == id) {
					result = row;
					break;
				}
			}
			/*
			Vector<Integer> columnVector = new Vector<Integer>();
			columnVector.addElement(new Integer(1));
			this.find(0, 0, id+"", columnVector, true);			
			find Methode ist Kacke, weil da nach Strings gesucht wird. Soll die ID auf 1 gesetzt werden, dann findet die ID 10...
			*/
		}
		return result;
	}
	public int getSelectedID() {
		int result = -1;
		int row = this.getSelectedRow(); 
		if (row >= 0 && this.getRowCount() > 0 && row < this.getRowCount()) {
			try {
				Object o = this.getValueAt(row, 0);
				if (o instanceof Integer) {
					result = (Integer) o;
				}
			}
			catch (Exception e) {MyLogger.handleException(e);}
		}
		return result;
	}
	public boolean setSelectedID(final int id) {
		return setSelectedID(id, false);
	}
	public boolean setSelectedID(final int id, final boolean force) {
		if (id > 0 && (force || id != getSelectedID())) {
			for (int row=0;row<this.getRowCount();row++) {
				// evtl. sollte hier ein Thread eingebaut werden - wegen Gefahr zu langsam...
				Object o = this.getValueAt(row, 0);
				if (o instanceof Integer && ((Integer) o) == id) {
					int col = 0, colScroll = 0;
					if (this.getSelectedColumn() > 0) {
						col = this.getSelectedColumn();
					}
					JScrollPane scroller = this.getScroller();
					if (scroller != null) {
						colScroll = scroller.getHorizontalScrollBar().getValue();
					}		
					this.setSelectedRowCol(row, col, row, colScroll, force);		// , force ist neu hier... ich weiss nicht, ob hier was schiefgehen kann... 27.12.2010
					//this.selectCell(row, 0);
					//this.goTo(row);
					//getScroller().getVerticalScrollBar().setValue(row-1);
					return true;
				}
			}
			/*
			Vector<Integer> columnVector = new Vector<Integer>();
			columnVector.addElement(new Integer(1));
			this.find(0, 0, id+"", columnVector, true);			
			find Methode ist Kacke, weil da nach Strings gesucht wird. Soll die ID auf 1 gesetzt werden, dann findet die ID 10...
			*/
			return false;
		}
		return true;
	}
	public void setSelectedRowCol(final int row, final int col) {
		int verticalScrollerPosition = 0;
		JScrollPane scroller = this.getScroller();
		if (scroller != null) {
			verticalScrollerPosition = scroller.getVerticalScrollBar().getValue();
		}
		setSelectedRowCol(row, col, verticalScrollerPosition, 0, false);
	}
	public void setSelectedRowCol(final int row, final int col, final int verticalScrollerPosition, final int horizontalScrollerPosition) {
		setSelectedRowCol(row, col, verticalScrollerPosition, horizontalScrollerPosition, false);
	}
	public void setSelectedRowCol(final int row, int col, final int verticalScrollerPosition, final int horizontalScrollerPosition, final boolean forceCol) {
		if (row >= 0) {
			JScrollPane scroller = this.getScroller();
			if (scroller != null) {
				scroller.getVerticalScrollBar().setValue(verticalScrollerPosition);
			}
			if (col < 0)
			 {
				col = 0;
				//System.out.println("restoreProperties\t" + selectedRow + "\t" + bigTable.convertRowIndexToModel(selectedRow));
				//bigTable.changeSelection(selectedRow, selectedCol, false, false);
			}

			if (col > 0 && !forceCol) {
				this.selectCell(row, col-1);
				final MyDBTable myDB = this;
				char ch = KeyEvent.VK_TAB;
				KeyEvent ke = new KeyEvent(myDB, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, ch);
				Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(ke);
				// this.selectCell(row, col); ist identisch mit folgenden 3 Zeilen!!!
				//this.setRowSelectionInterval(row, row);
				//this.getTable().setColumnSelectionInterval(col, col);
				//this.getTable().requestFocus();
				/// Dummerweise ist z.B. die Combobox dann sogleich im EditorModus... also obige Toolkit-Lösung!
			}
			else {
				//this.setRowSelectionInterval(row, row);
				//this.getTable().setColumnSelectionInterval(col, col);
				//System.err.println("row3: " + row + "\t" + col);
				try {
					// hier gibts leider manchmal Abstürze, wenn ein Filter gesetzt ist und gleichzeitig der Sorter gedrückt wird
					this.selectCell(row, col);					
				}
				catch (Exception e) {MyLogger.handleException(e);}
				//System.err.println("row4: " + row + "\t" + col);
			}
			if (scroller != null) {
				scroller.getHorizontalScrollBar().setValue(horizontalScrollerPosition);
			}
		}		
	}
	public boolean isFilledBlobField(final int row, final int col) {
		boolean result = false;
		if (row >= 0 && row < this.getRowCount()) {
			if (actualTable != null) {
				if (actualTable.getTablename().equals("DateiSpeicher")) {
					if (this.getColumnCount() > 7) {
						result = (this.getValueAt(row, 7) != null);
					}
				}
				else {
					Vector<Integer> myBLOBs = actualTable.getMyBLOBs();
					for (int i=0;i<myBLOBs.size();i++) {
						if (col - 1 == myBLOBs.get(i) && this.getValueAt(row, col) != null) {
							result = true;
							break;
						}
					}			
				}
			}
		}
		return result;
	}
	public void extractBLOB() {
		extractBLOB(this.getSelectedRow(), this.getSelectedColumn());
	}
	public void extractBLOB(final int row, final int col) {
		// extract BLOB
		if (this.getActualTable().getTablename().equals("DateiSpeicher")) {
			DBKernel.getPaper(0, "", "", (Integer) this.getValueAt(row, 0));			
		}
		else {
			if (row >= 0 && row < this.getRowCount()) {
				int tableID = (Integer) this.getValueAt(row, 0);
				//System.out.println(row + "\t" + col + "\t" + myDBTable1.getActualTable().getFieldNames()[col-1] + "\t" + tableID);
				DBKernel.getPaper(tableID, this.getActualTable().getTablename(), this.getActualTable().getFieldNames()[col-1], -1);			
			}
		}		
	}
  private void checkForeignWindow2Open(final int row, final int col) {
    MyTable[] myTs = actualTable.getForeignFields();
    //System.out.println(lastClickedRow + "\t" + lastClickedCol + "\t" + myTs.length);
    if (col > 0 && col <= myTs.length && myTs[col-1] != null &&
    		!myTs[col-1].getTablename().equals("DoubleKennzahlen")) { 
		JScrollPane scroller = getScroller();
		//int scrollVal = (scroller == null) ? -1 : scroller.getVerticalScrollBar().getValue();
		int hscrollVal = (scroller == null) ? -1 : scroller.getHorizontalScrollBar().getValue();

		//MyLogger.handleMessage("checkForeignWindow2Open1 : " + row);
		Object newVal = DBKernel.myList.openNewWindow(myTs[col-1], this.getValueAt(row, col), this.getColumn(col).getHeaderValue(), this, row, col);
		//MyLogger.handleMessage("checkForeignWindow2Open2 : " + row + "\t" + newVal);
    	if (!this.actualTable.isReadOnly()) {
	      	if (newVal != null) {
	          	String[] mnTable = actualTable.getMNTable();
	        	if (col > 0 && mnTable != null && col-1 < mnTable.length && mnTable[col - 1] != null && (mnTable[col - 1].equals("INT"))) { // mnTable[col - 1].equals("DBL") || 
	        		refreshMNs();
	        	}
	        	else {
		      		this.setValueAt(newVal, row, col);    
		      		// evtl. HashBox neu setzen, sonst wird nicht refresht
		      		MyTable[] foreignFields = actualTable.getForeignFields();
		      		if (foreignFields != null) {
		      			if (foreignFields[col-1] != null) {
	  						hashBox[col-1] = DBKernel.fillHashtable(foreignFields[col-1], "", "\n", "\n", !bigbigTable, true); //" | " " ; "
	  						Column c = this.getColumn(col); 
	  						c.setUserCellRenderer(new MyComboBoxEditor(hashBox[col-1], true));
	  					}
	    			}
	        	}
	      	}
	      	else if (actualTable.getListMNs() != null) {
	      		refreshMNs();
	      	}
	    }
		//MyLogger.handleMessage("checkForeignWindow2Open3 : " + row + "\t" + newVal);

    	//int[] rh = getRowHeights(this.getTable());
  		this.save();
  		// Ist die Refresherei überhaupt notwendig? Naja, setRowHeights wird nicht aufgerufen und damit gehen die RowHeights verloren...
  		// Damit kann ich aber erst mal leben.
  		// Nagut, die hashBox geht auch verloren...
  		/*
  		try {
  			this.refresh();
  			syncTableRowHeights(row);
  		}
  		catch (SQLException e) {}
  		*/
  		/*
  		this.setTable(actualTable);
      this.getTable().setRowSelectionInterval(row, row); this.goTo(row);
      this.getTable().setColumnSelectionInterval(col, col);
    	setRowHeights(this.getTable(), rh);
    	syncTableRowHeights();
    	*/
  		int sr = this.getSelectedID();//this.getSelectedRow();
  		int sc = this.getSelectedColumn();

		//MyLogger.handleMessage("checkForeignWindow2Open4 sr: " + sr + "\t" + this.getSelectedRow());
  		try {
	    	this.refresh();
	    }
	    catch (Exception e1) {
	    	MyLogger.handleException(e1);
	    }
		//MyLogger.handleMessage("checkForeignWindow2Open5 sr: " + sr + "\t" + this.getSelectedRow());
  		if (myDBPanel1 != null) {
			myDBPanel1.handleSuchfeldChange(null);
		}
		//MyLogger.handleMessage("checkForeignWindow2Open6 sr: " + sr + "\t" + this.getSelectedRow());
	    this.updateRowHeader(!bigbigTable);
	    if (sc >= 0 && sc < this.getColumnCount()) {
			this.getTable().setColumnSelectionInterval(sc, sc);
		}
	    
    	/*
	    if (sr >= 0 && sr < this.getRowCount()) {
	    	this.setRowSelectionInterval(sr, sr);
	    	this.goTo(sr);
	    }
	*/
	    if (sorterModel != null) {
			sorterModel.initArray();
		}
	    //if (scrollVal >= 0) this.getScroller().getVerticalScrollBar().setValue(scrollVal);
	    if (hscrollVal >= 0) {
			this.getScroller().getHorizontalScrollBar().setValue(hscrollVal);
		}
		//this.getTable().requestFocus();
		//MyLogger.handleMessage("checkForeignWindow2Open7 sr: " + sr + "\t" + this.getSelectedRow());
    	this.setSelectedID(sr);
		//MyLogger.handleMessage("checkForeignWindow2Open8 sr: " + sr + "\t" + this.getSelectedRow());
    }  	
  }
  private void refreshMNs() {
  	int selID = this.getSelectedID();
		DBKernel.doMNs(this);
		this.myRefresh();
		//MyLogger.handleMessage("refreshMNs sel vs sel: " + this.getSelectedID() + "\t" + selID);
		if (this.getSelectedID() != selID) {
			this.setSelectedID(selID);
		}
		syncTableRowHeights();	  
  }

	@Override
	public void sorterChanged(final RowSorterEvent e) {
		if (e == null || e.getType() == RowSorterEvent.Type.SORT_ORDER_CHANGED) {
			if (this.getTable().getCellEditor() != null) {
				this.getTable().getCellEditor().stopCellEditing();
			}			
		}
		if (e == null || e.getType() == RowSorterEvent.Type.SORTED) {
			//System.out.println("SORTED");
			/*
			if (this.getSelectedRow() >= 0) {
				System.out.println(this.getTable().convertRowIndexToModel(this.getSelectedRow()) + "\t" +
						this.getTable().convertRowIndexToView(this.getSelectedRow()) + "\t" + this.getSelectedRow());
			}
			*/
			if (myDBPanel1 != null) {
				//myDBPanel1.handleSuchfeldChange(null, false); // handleSuchfeldChange hier oben stehen lassen??? Oder lieber runter?? doFilter = false, weil sonst StackOverflow!
				syncTableRowHeights();
				int selID = this.getSelectedID();
				this.setSelectedID(selID, true);
				myDBPanel1.handleSuchfeldChange(null, false); // doFilter = false, weil sonst StackOverflow!
			}		
		}
	}
	public void syncTableRowHeights() {
		JScrollPane scroller = getScroller();
		if (scroller != null && scroller.getRowHeader() != null && scroller.getRowHeader().getView() instanceof JTable) {
			JTable jTable = (JTable) scroller.getRowHeader().getView();
			JTable bigTable = this.getTable(); 
			for (int i=0;i<bigTable.getRowCount();i++) {
			  	int newHeight = bigTable.getRowHeight(i);			
			  	if (newHeight > 0) {
					jTable.setRowHeight(i, newHeight);
				}
			}							
		}
	}
	public JScrollPane getScroller() {
	    JScrollPane scroller = null;
	    for (int i=0;i<this.getComponentCount();i++) {
	      if (this.getComponent(i) instanceof JScrollPane) {
	        scroller = (JScrollPane) this.getComponent(i);
	        break;
	      }
	    }		
	    return scroller;
	}
	private void addScrollerListeners4Rendering() {
		final MyDBTable myDB = this;
		JScrollPane scroller = getScroller();
		final JScrollBar scrollBarVertical = scroller.getVerticalScrollBar();
	    scrollBarVertical.addAdjustmentListener(new AdjustmentListener() {
	        @Override
			public void adjustmentValueChanged(final AdjustmentEvent ae) {
	          if (scrollBarVertical.getValueIsAdjusting()) {
	        	  DBKernel.scrolling = true; //System.out.println("Value of vertical scroll bar: " + ae.getValue());
	          }
	    	  else {
	    		  DBKernel.scrolling = false;
	    		  myDB.repaint();
	    	  }
	        }
	      });	
	    
		final JScrollBar scrollbarHorizontal = scroller.getHorizontalScrollBar();
		   scrollbarHorizontal.addAdjustmentListener(new AdjustmentListener() {
			      @Override
				public void adjustmentValueChanged(final AdjustmentEvent ae) {
			    	  if (scrollbarHorizontal.getValueIsAdjusting()) {
			    		  DBKernel.scrolling = true; //System.out.println("Value of horizontal scroll bar: " + ae.getValue());
			    	  }
			    	  else {
			    		  DBKernel.scrolling = false;
			    		  myDB.repaint();
			    	  }
			      }
			    });
	}
	/*
	private int[] getRowHeights(final JTable table) {
		int[] result = new int[table.getRowCount()];
		for (int i=0;i<table.getRowCount();i++) {
	  	result[i] = table.getRowHeight(i);		
		}	
		return result;
	}
	private void setRowHeights(final JTable table, final int[] rh) {
		for (int i=0;i<table.getRowCount();i++) {
	  	table.setRowHeight(i, rh[i]);		
		}			
	}
	private int packColumn(final JTable table, final TableCellRenderer renderer, final String title, final int margin) {
	    int width = 0;
	
	    // Get width of column header
	    Component comp = renderer.getTableCellRendererComponent(table, title, false, false, 0, 0);
	    width = comp.getPreferredSize().width;
	
	    // Add margin
	    width += 2*margin;
	    
	    return width;
	}
	*/
	  private void myCopyToClipboard() { 
		    int row = this.getTable().getSelectedRow(); 
		    int column = this.getTable().getSelectedColumn();
			if (row >= 0 && row < this.getRowCount() && column >= 0 && column < this.getColumnCount()) {
				 String excelStr = null;
				if (this.getTable().getValueAt(row, column) != null) {
		      excelStr = this.getTable().getValueAt(row, column).toString();			      
				}
				StringSelection sel  = new StringSelection(excelStr); 					
			  Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, sel); 									
			}
	  } 
	  
	  private void myPasteFromClipboard() { 
		    int row = this.getTable().getSelectedRow(); 
		    int column = this.getTable().getSelectedColumn();
			if (row >= 0 && row < this.getRowCount() && column >= 0 && column < this.getColumnCount()) {
			      String pasteString = ""; 
			      try { 
			              pasteString = (String)(Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this).getTransferData(DataFlavor.stringFlavor)); 
			      }
			      catch (Exception e) { 
			    	  MyLogger.handleException(e);
			              return; 
			      } 
			      if (actualTable.getFieldTypes()[column - 1].startsWith("DATE")) {
					  SimpleDateFormat dateFormat2 = new SimpleDateFormat(actualTable.getFieldTypes()[column - 1].equals("DATE") ? "yyyy-MM-dd" : "yyyy-MM-dd HH:mm:ss");
					  try {
					      this.getTable().setValueAt(dateFormat2.parse(pasteString), row, column); 	   
					  }
					  catch (ParseException e) {
						e.printStackTrace();
					  }
			      }
			      else {
				      this.getTable().setValueAt(pasteString, row, column); 	   			    	  
			      }
			    	int selID = this.getSelectedID();
					this.myRefresh(row, column);
					if (this.getSelectedID() != selID) {
						this.setSelectedID(selID);
					}
			}
	} 

	private void cancelEditing() { 
	      if (this.getTable().getCellEditor() != null) { 
	    	  this.getTable().getCellEditor().cancelCellEditing(); 
	  } 
	} 
	
	public void copyProzessschritt() {
		Integer id = getSelectedID();
		if (id >= 0) {
	    int retVal = JOptionPane.showConfirmDialog(this, "Sicher?\nDie aktuellen Parameter des selektierten Prozessschrittes könnten in der Folge überschrieben werden!",
	    		"Prozessschritt kopieren?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (retVal == JOptionPane.YES_OPTION) {
				Object carverID = DBKernel.getValue("Prozessdaten", "ID", getSelectedID()+"", "Prozess_CARVER");
				Object[][] cond = new Object[1][2]; cond[0][0] = "Prozess_CARVER"; cond[0][1] = carverID;
	    	Object fromID = DBKernel.myList.openNewWindow(actualTable, null, "Datensatz zur Parameterübernahme", this, null, null, cond);
				copyParameters(fromID, getSelectedID());
				this.myRefresh();
	  		// evtl. HashBox neu setzen, sonst wird nicht refresht
	  		MyTable[] foreignFields = actualTable.getForeignFields();
	  		String[] mnTable = actualTable.getMNTable();
	  		if (foreignFields != null) {
				DBKernel.refreshHashTables();
	  			for (int i=0;i<foreignFields.length;i++) {
	    			if (foreignFields[i] != null && (mnTable == null || mnTable[i] == null)) {
	    				hashBox[i] = DBKernel.fillHashtable(foreignFields[i], "", "\n", "\n", !bigbigTable); //" | " " ; "
	    				Column c = this.getColumn(i+1); 
	    				c.setUserCellRenderer(new MyComboBoxEditor(hashBox[i], true));
	    			}
	  			}
	  		}
			}		
		}
	}
	private void copyParameters(final Object fromID, final Object toID) {
		if (fromID != null && toID != null) {
			// KapazitaetEinheit, KapazitaetEinheitBezug, DauerEinheit
			//int fromRow = this.getRowFromID((Integer)fromID);
			int toRow = this.getRowFromID((Integer)toID);
			if (actualTable.getFieldNames()[6].equals("KapazitaetEinheit")) {
				Object val = DBKernel.getValue(actualTable.getTablename(), "ID", fromID.toString(), "KapazitaetEinheit");
				if (val != null) {
					this.setValueAt(val, toRow, 7);
				} else {
					this.insertNull(toRow, 7);
				}
			} else {
				MyLogger.handleMessage("KapazitaetEinheit ist nicht Column Number 6....");
			}
			if (actualTable.getFieldNames()[7].equals("KapazitaetEinheitBezug")) {
				Object val = DBKernel.getValue(actualTable.getTablename(), "ID", fromID.toString(), "KapazitaetEinheitBezug");
				if (val != null) {
					this.setValueAt(val, toRow, 8);
				} else {
					this.insertNull(toRow, 8);
				}
			} else {
				MyLogger.handleMessage("KapazitaetEinheitBezug ist nicht Column Number 7....");
			}
			if (actualTable.getFieldNames()[9].equals("DauerEinheit")) { 
				Object val = DBKernel.getValue(actualTable.getTablename(), "ID", fromID.toString(), "DauerEinheit");
				if (val != null) {
					this.setValueAt(val, toRow, 10);
				} else {
					this.insertNull(toRow, 10);
				}
			} else {
				MyLogger.handleMessage("DauerEinheit ist nicht Column Number 9....");
			}
			// Zutaten???
			this.save(); // jetzt ja eigentlich nicht mehr notwendigm da ja toRow an copyKennzahlen übergeben wird - doppelt gemoppelt, ok, hält besser
			// alle Kenzahlen
			MyTable pd = MyDBTables.getTable("Prozessdaten");
			try {
				copyKennzahlen(pd,(Integer)fromID,(Integer)toID, toRow);
			}
			catch (Exception e1) {e1.printStackTrace();}
			//manageKZ("Prozessdaten", fromID, toID);
			// Sonstiges
			DBKernel.doMNs(MyDBTables.getTable("Prozessdaten_Sonstiges"));
			DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL("Prozessdaten_Sonstiges") +
					" WHERE " + DBKernel.delimitL("Prozessdaten") + "=" + toID, false);				
			DBKernel.sendRequest("INSERT INTO " + DBKernel.delimitL("Prozessdaten_Sonstiges") +
					" SELECT NULL," + toID + "," + DBKernel.delimitL("SonstigeParameter") + "," +
					DBKernel.delimitL("Wert") + "," +
					DBKernel.delimitL("Einheit") + "," + DBKernel.delimitL("Ja_Nein") + "," + DBKernel.delimitL("Kommentar") +
					" FROM " + DBKernel.delimitL("Prozessdaten_Sonstiges") +
					" WHERE " + DBKernel.delimitL("Prozessdaten") + "=" + fromID, false);
			ResultSet rs = DBKernel.getResultSet("SELECT " + DBKernel.delimitL("ID") + " FROM " +
					DBKernel.delimitL("Prozessdaten_Sonstiges") +	" WHERE " + DBKernel.delimitL("Prozessdaten") + "=" + toID, false);
			try {
				if (rs != null && rs.first()) {
					MyTable ps = MyDBTables.getTable("Prozessdaten_Sonstiges");
					do {
						try {
							copyKennzahlen(ps,rs.getInt("ID"),rs.getInt("ID"));
						}
						catch (Exception e1) {e1.printStackTrace();}
					} while (rs.next());
				}
			}
			catch (Exception e) {MyLogger.handleException(e);}
			DBKernel.doMNs(MyDBTables.getTable("Prozessdaten_Sonstiges"));
		}
	}
	@Override
	public void keyPressed(final KeyEvent keyEvent) {
  	//System.out.println(keyEvent.getKeyCode() + "\t" + keyEvent.getKeyChar() + "\t" + KeyEvent.VK_F + "\t" + keyEvent.isControlDown());
    if (keyEvent.isControlDown() && keyEvent.getKeyCode() == KeyEvent.VK_LEFT) { //Ctrl+<-, Aussredem geht auch F8
    	keyEvent.consume();
    	DBKernel.myList.requestFocus();
    	return;
    }
    else if (keyEvent.isControlDown() && keyEvent.getKeyCode() == KeyEvent.VK_F) { // Ctrl+F
    	//System.out.println("Ctrl+F");
    	keyEvent.consume();
    	if (myDBPanel1 != null) {
    		myDBPanel1.getSuchfeld().grabFocus();
    	}
    	return;
    }
    else if (keyEvent.getKeyCode() == KeyEvent.VK_B && (keyEvent.isAltDown() || keyEvent.isControlDown())) {
    	keyEvent.consume();
    	SendMail.main(null);
    	return;
    }
    else if (keyEvent.getKeyCode() == KeyEvent.VK_P && (keyEvent.isAltDown() || keyEvent.isControlDown())) {
    	keyEvent.consume();
    	myPrint();
    	return;
    }
    else if (keyEvent.getKeyCode() == KeyEvent.VK_Z && (keyEvent.isAltDown() || keyEvent.isControlDown())) {
    	this.save();
    	keyEvent.consume();
    	DBKernel.undoManager.undo();    	
     	return;
    }
    else if (keyEvent.getKeyCode() == KeyEvent.VK_Y && (keyEvent.isAltDown() || keyEvent.isControlDown())) {
    	this.save();
    	keyEvent.consume();
    	DBKernel.undoManager.redo();
     	return;
    }
    else if (keyEvent.getKeyCode() == KeyEvent.VK_INSERT) {
    	if (getMyDBPanel().addingDisabled()) keyEvent.consume();
    	else this.insertNewRow(keyEvent.isAltDown() || keyEvent.isControlDown(), null);
    }
    else if (keyEvent.getKeyCode() == KeyEvent.VK_DELETE) {
    	keyEvent.consume();
    }
    else if (keyEvent.getKeyCode() == KeyEvent.VK_W && (keyEvent.isAltDown() || keyEvent.isControlDown())) {
			if (this.getActualTable().getTablename().equals("Prozessdaten")) {
				copyProzessschritt();
	    	keyEvent.consume();
	     	return;
			}
    }
    else if (keyEvent.getKeyCode() == KeyEvent.VK_K && (keyEvent.isAltDown() || keyEvent.isControlDown())) {
    	if (DBKernel.isKrise) {
    		makeKrisenGrafiken();
    		keyEvent.consume();
    	}
    }
    else if (keyEvent.getKeyCode() == KeyEvent.VK_O && (keyEvent.isAltDown() || keyEvent.isControlDown())) {
		if (myDBPanel1 != null) {
			myDBPanel1.button11ActionPerformed(null);
		}
    	keyEvent.consume();
     	return;
	}
    else if (keyEvent.getKeyCode() == KeyEvent.VK_Q && (keyEvent.isAltDown() || keyEvent.isControlDown())) {
		if (myDBPanel1 != null) {
			myDBPanel1.button10ActionPerformed(null);
		}
    	keyEvent.consume();
     	return;
	}
  }
  @Override
public void keyReleased(final KeyEvent keyEvent) {
      if (keyEvent.getKeyCode()==KeyEvent.VK_C) { // Copy                        
          cancelEditing(); 
          myCopyToClipboard(); 
	  }
      else if (keyEvent.getKeyCode()==KeyEvent.VK_V) { // Paste 
	          cancelEditing(); 
	          myPasteFromClipboard();           
	  } 
  }

  @Override
public void keyTyped(final KeyEvent keyEvent) {
  	char ch = keyEvent.getKeyChar();
  	//System.out.println(ch + "\t" + keyEvent.isConsumed());
    if (ch == KeyEvent.VK_TAB || ch == KeyEvent.VK_ENTER && !keyEvent.isAltDown() && !keyEvent.isControlDown()) {
		return;
	}
    int row = this.getTable().getSelectedRow(); 
    int column = this.getTable().getSelectedColumn();
    if (ch == KeyEvent.VK_ESCAPE) {
		keyEvent.consume();
    	this.clearSelection();
    	return;
    }
    else if (ch == KeyEvent.VK_ENTER) {
		keyEvent.consume();
    	if (column > 0 && this.getColumn(column).getReadOnly()) { // readonly ComboBox Verschnitt
    		checkForeignWindow2Open(row, column);
    		return;
    	}
    	else if (isFilledBlobField(row, column)) {
        	if (myDBPanel1 != null) {
				this.extractBLOB();
			}
        	return;    		
    	}
    }
    else if (ch == KeyEvent.VK_DELETE) {
		keyEvent.consume();
		if (keyEvent.isAltDown() || keyEvent.isControlDown()) {
			insertNull(row, column);        	
		}
		else {
			deleteRow();
		}
		return;
    }
    //mdt.getTable().editCellAt(row, column); // habe ich jetzt nach unten geschoben, sonst geht BLOBEditor nicht...
    TableCellEditor ed = this.getTable().getCellEditor();
    if (ed == null) {
  	  	if (column > 0 && this.getColumn(column).getReadOnly()) { // readonly ComboBox Verschnitt
  	  		Rectangle jTableRechteck = this.getTable().getCellRect(row, column, false);
  	  		JScrollPane scroller = getScroller();
  	  		int hscrollVal = (scroller == null) ? 0 : scroller.getHorizontalScrollBar().getValue();
  			int vscrollVal = (scroller == null) ? 0 : scroller.getVerticalScrollBar().getValue();
  	  		checkOtherEditor2Open(row, column, this.getLocationOnScreen().x + jTableRechteck.x - hscrollVal, this.getLocationOnScreen().y + jTableRechteck.y - vscrollVal, ch);
  	  	}
    	return;
    }
    Component comp = ed.getTableCellEditorComponent(this.getTable(), ed.getCellEditorValue(), true, row, column);    	
  	if (comp == null) {
		return;
	}
  	if (comp instanceof JScrollPane) {
      	this.getTable().editCellAt(row, column);
        comp = ((JScrollPane) comp).getViewport().getView();
        JTextArea ta = (JTextArea) comp;
      	//System.out.println("12e");  			
        if (ta.getFont().canDisplay(ch)) {
			ta.append(""+ch);
		}
		else {
			ta.append(""); // +System.currentTimeMillis()
		}
        keyEvent.consume();
    }
    else if (comp instanceof JComboBox) {
    	if (ch == KeyEvent.VK_ENTER) {
    		checkForeignWindow2Open(row, column);        	        		
    	}
    	else if (ch == KeyEvent.VK_BACK_SPACE) {
    		insertNull(row, column);        	
    	}
    }
    else if (comp instanceof JTextField) {  // Zahlen bspw. in JTextField
    	final JTextField tf = (JTextField) comp;
		tf.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(final FocusEvent e) {
				//System.err.println(System.currentTimeMillis() + "_focusGained_suchfeld");
			  	tf.select(0,0);
				tf.setCaretPosition(tf.getText().length());
			}
		});
    	tf.setText(ch+"");
      	keyEvent.consume();
    }
    else if (comp instanceof MyCheckBoxEditor) { // einfach nix machen, Haken wird gesetzt bei Space
    }
    else {
      	System.out.println("12e:" + comp);  	
    }
  	comp.requestFocusInWindow();      
  }
	@Override
	public void mouseClicked(final MouseEvent e) {
	    int lastClickedRow;// = e.getY()/this.getTable().getRowHeight();
	    int val = 0;
	    for (lastClickedRow = 0; lastClickedRow < this.getTable().getRowCount(); lastClickedRow++) {
	    	val += this.getTable().getRowHeight(lastClickedRow);
	    	if (val >= e.getY()) {
				break;
			}
	    }
	    this.setRowSelectionInterval(lastClickedRow, lastClickedRow);
		int lastClickedCol = getLastClickedCol(e);

		if (this.getColumn(lastClickedCol).getScale() != -1) { // bei GeschaetzteModelle
		    if (SwingUtilities.isLeftMouseButton(e)) {
		    	if (e.getClickCount() > 1) {
				      checkForeignWindow2Open(lastClickedRow, lastClickedCol);	    		
		    	}
		    	else {
		    		checkOtherEditor2Open(lastClickedRow, lastClickedCol, e.getXOnScreen(), e.getYOnScreen(), ' ');
		    	}
		  	}
		    else if (lastClickedCol == 0 && SwingUtilities.isRightMouseButton(e)) {
		    	merging();
		    }
		  	else {
		      checkForeignWindow2Open(lastClickedRow, lastClickedCol);
		  	}
		}
	}
	public void setReadOnly(boolean ro) {
		if (ro) {
			for (int i=0; i<this.getColumnCount(); i++) {		
				Column c = this.getColumn(i);
				c.setReadOnly(ro);
			}			
		}
	}
	private void merging() {
		int oldID = this.getSelectedID();
    	String response = JOptionPane.showInputDialog(this,
    			  "Bitte die ID eingeben, die ID " + oldID + " ersetzen soll:",
    			  "ID " + oldID + " ersetzen durch andere ID!",
    			  JOptionPane.QUESTION_MESSAGE);
    	try {
    		int newID = Integer.parseInt(response);
    		if (oldID == newID) {
    			InfoBox ib = new InfoBox(DBKernel.mainFrame, "IDs identisch: " + oldID, true, new Dimension(400,200), null, true);
    			ib.setVisible(true);
    		}
    		else if (!DBKernel.hasID(actualTable.getTablename(), newID)) {
    			InfoBox ib = new InfoBox(DBKernel.mainFrame, "Die neue ID gibt es gar nicht...", true, new Dimension(400,200), null, true);
    			ib.setVisible(true);
    		}
    		else {
	    		int reallyDoIt = JOptionPane.showConfirmDialog(this,
	    				"ID " + oldID + " wird durch " + newID + " ersetzt. Korrekt?",
		    			  "Datensatz ersetzen durch ID???",
		    			  JOptionPane.YES_NO_OPTION);
	    		if (reallyDoIt == JOptionPane.YES_OPTION) {
	    			if (DBKernel.mergeIDs(this.getConnection(), actualTable.getTablename(), oldID, newID)) {
		    			InfoBox ib = new InfoBox(DBKernel.mainFrame,
		    					"ID " + oldID + " wurde erfolgreich durch " + newID + " ersetzt!",
		    					true, new Dimension(400,200), null, true);
		    			ib.setVisible(true);
				    	this.setTable();
				    	
				    	if (theFilter != null) {
				    		this.filter(theFilter);
				    		//this.setReadOnly(true);
				    	}
				    	
	    			}
	    			else {
		    			InfoBox ib = new InfoBox(DBKernel.mainFrame,
		    					"Hmmm.... something went wrong...",
		    					true, new Dimension(400,200), null, true);
		    			ib.setVisible(true);
	    			}
	    		}
    		}
    	}
    	catch (Exception ee) {}		
	}
	private void checkOtherEditor2Open(final int lastClickedRow, final int lastClickedCol, final int x, final int y, final char ch) {
		if (lastClickedCol > 0) {
	    MyTable[] myTs = actualTable.getForeignFields();
	    if (lastClickedCol > 0 && lastClickedCol <= myTs.length && myTs[lastClickedCol-1] != null &&
	    		myTs[lastClickedCol-1].getTablename().equals("DoubleKennzahlen")) {
	  		JScrollPane scroller = getScroller();
	  		//int scrollVal = (scroller == null) ? -1 : scroller.getVerticalScrollBar().getValue();
	  		int hscrollVal = (scroller == null) ? -1 : scroller.getHorizontalScrollBar().getValue();

	  		String spaltenName = this.getActualTable().getFieldNames()[lastClickedCol-1];
	  		MyNewDoubleEditor mde = new MyNewDoubleEditor(this.getValueAt(lastClickedRow, lastClickedCol), spaltenName, x, y, ch);		
				mde.setVisible(true);
				if (mde.savePressed()) {
					if (mde.getNewValue() != null) {
						this.setValueAt(mde.getNewValue(), lastClickedRow, lastClickedCol);
					}
      		// evtl. HashBox neu setzen, sonst wird nicht refresht
					hashBox[lastClickedCol-1] = DBKernel.fillHashtable(myTs[lastClickedCol-1], "", "\n", "\n", !bigbigTable, true); //" | " " ; "
					Column c = this.getColumn(lastClickedCol); 
					c.setUserCellRenderer(new MyComboBoxEditor(hashBox[lastClickedCol-1], true));

					this.save();
			  		int sr = this.getSelectedID();
			  		int sc = this.getSelectedColumn();
	
			  		try {
				    	this.refresh();
				    }
				    catch (Exception e1) {
				    	MyLogger.handleException(e1);
				    }
			  		if (myDBPanel1 != null) {
						myDBPanel1.handleSuchfeldChange(null);
					}
				    this.updateRowHeader(!bigbigTable);
				    if (sc >= 0 && sc < this.getColumnCount()) {
						this.getTable().setColumnSelectionInterval(sc, sc);
					}
				    
			    	/*
				    if (sr >= 0 && sr < this.getRowCount()) {
				    	this.setRowSelectionInterval(sr, sr);
				    	this.goTo(sr);
				    }
				*/
				    if (sorterModel != null) {
						sorterModel.initArray();
					}
				    //if (scrollVal >= 0) this.getScroller().getVerticalScrollBar().setValue(scrollVal);
				    if (hscrollVal >= 0) {
						this.getScroller().getHorizontalScrollBar().setValue(hscrollVal);
					}
			    	this.setSelectedID(sr);
			    }
			}
				
	    /*
	    	String[] mnTable = actualTable.getMNTable();
	    	if (mnTable != null && lastClickedCol - 1 < mnTable.length && mnTable[lastClickedCol - 1] != null && mnTable[lastClickedCol - 1].equals("DBL")) {
				MyDoubleEditor mde = new MyDoubleEditor(this, lastClickedCol, x, y, ch);		
				mde.setVisible(true);
	    	}	
	    	*/
		}		
	}
	private int getLastClickedCol(final MouseEvent e) {
	      int lastClickedCol;
	      int val = 0;
	      for (lastClickedCol = 0; lastClickedCol < this.getTable().getColumnCount(); lastClickedCol++) {
	      	val += this.getTable().getColumnModel().getColumn(lastClickedCol).getWidth();
	      	if (val >= e.getX()) {
				break;
			}
	      }		
	      return lastClickedCol;
	}
	@Override
	public void mouseEntered(final MouseEvent e) {
	}
	@Override
	public void mouseExited(final MouseEvent e) {
	}
	@Override
	public void mousePressed(final MouseEvent e) {
	}
	@Override
	public void mouseReleased(final MouseEvent e) {
	}

	private void makeKrisenGrafiken() {
		/*
    	String[] os = new String[]{"Gärtnerhof Bienenbüttel GmbH (NI)","B&G Sprossenparadies GmbH (BY)"};
    	
    	//Hashtable<String, Vector<String>> prodcode = getProduktCode("C:/Dokumente und Einstellungen/Weiser/Desktop/Team8/Produktbezeichnungen.xls");
    	//Hashtable<String, String> betcode = getBetriebCode("C:/Dokumente und Einstellungen/Weiser/Desktop/Team8/Knoten_liste_codes.xls");
    	Hashtable<String, Vector<String>> prodcode = getProduktCode(DBKernel.HSHDB_PATH + "../../Produktbezeichnungen.xls");
    	Hashtable<String, String> betcode = getBetriebCode(DBKernel.HSHDB_PATH + "../../Knoten_liste_codes.xls");
    	//Hashtable<String, Vector<String>> prodcode = getProduktCode("C:/Users/Armin/Desktop/EHEC/Team8/Produktbezeichnungen.xls");
    	//Hashtable<String, String> betcode = getBetriebCode("C:/Users/Armin/Desktop/EHEC/Team8/Knoten_liste_codes.xls");
    	//exportKetten(prodcode, betcode, "alles", os, null, null, null); // new String[]{"8266","10866001"}  new String[]{"galke"}

    	exportKetten(prodcode, betcode, "FcLink_48088_plus", os, null, null, new String[]{"48088","6832","104350"}, true);
    	exportKetten(prodcode, betcode, "FcLink_05842001_plus", os, null, null, new String[]{"05842001"}, true);
    	exportKetten(prodcode, betcode, "FcLink_0615001_plus", os, null, null, new String[]{"0615001"}, true);
    	exportKetten(prodcode, betcode, "FcLink_8266_plus", os, null, null, new String[]{"8266","8223","10866001"}, true); 
    	exportKetten(prodcode, betcode, "FcLink_2660002_plus", os, null, null, new String[]{"2660002","8710"}, true);
    	exportKetten(prodcode, betcode, "FcLink_KT_plus", os, null, null, new String[]{"312"}, true);
    	exportKetten(prodcode, betcode, "FcLink_48088_Main", os, null, new String[]{"bienenbüttel","centre de loisirs de la petite enfance"}, new String[]{"48088","6832","104350"}, true);
    	exportKetten(prodcode, betcode, "alleSamenBB", os, new String[]{"Adzukibohnen","Alfalfa","Beluga Linsen","Berglinsen","Bockshornklee","braune Linsen","Chateau Linsen","grüne Linsen","Linsen Epson","Linsen","Radieschensamen","Rettichsamen","Rote Linsen","Rucolasamen","Tellerlinsen"}, null, null, true); // new String[]{"8266","10866001"}  new String[]{"galke"}
    	exportKetten(prodcode, betcode, "alleSamen", null, null, null, null, true); // new String[]{"8266","10866001"}  new String[]{"galke"}
    	
    	if (false) {
        	exportKetten(prodcode, betcode, "top6", os, new String[]{"Adzukibohnen","Alfalfa","Beluga Linsen","Berglinsen","Bockshornklee","braune Linsen","Chateau Linsen","grüne Linsen","Linsen Epson","Linsen","Radieschensamen","Rettichsamen","Rote Linsen","Tellerlinsen"}, null, null); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "bockshorn", os, new String[]{"Bockshornklee"}, null, null);
        	exportKetten(prodcode, betcode, "alfalfa", os, new String[]{"Alfalfa"}, null, null); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "azuki", os, new String[]{"Adzukibohnen"}, null, null); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "radieschen", os, new String[]{"Radieschensamen"}, null, null); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "rettich", os, new String[]{"Rettichsamen"}, null, null); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "linse", os, new String[]{"Beluga Linsen","Berglinsen","braune Linsen","Chateau Linsen","grüne Linsen","Linsen Epson","Linsen","Rote Linsen","Tellerlinsen"}, null, null); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "bockshorn_lot", os, new String[]{"Bockshornklee"}, null, new String[]{"0104350","8223","8266","10866001"});
        	exportKetten(prodcode, betcode, "alfalfa_lot", os, new String[]{"Alfalfa"}, null, new String[]{"b5a001"}); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "azuki_lot", os, new String[]{"Adzukibohnen"}, null, new String[]{"b-43","0107263"}); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "radieschen_lot", os, new String[]{"Radieschensamen"}, null, new String[]{"b0p137"}); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "rettich_lot", os, new String[]{"Rettichsamen"}, null, new String[]{"b0p038"}); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "linse_lot", os, new String[]{"Beluga Linsen","Berglinsen","braune Linsen","Chateau Linsen","grüne Linsen","Linsen Epson","Linsen","Rote Linsen","Tellerlinsen"}, null, new String[]{"b-1249","1635/0","0108799","0109589"});
        	exportKetten(prodcode, betcode, "bockshorn_10866001", os, new String[]{"Bockshornklee"}, null, new String[]{"8223","8266","10866001"}); // new String[]{"8266","10866001"}  new String[]{"galke"}    	
        	
        	exportKetten(prodcode, betcode, "sprossen", os, new String[]{"sprossen","keim","bohnen"}, null, null);
        	exportKetten(prodcode, betcode, "galke", os, null, new String[]{"galke"}, null); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "lots", os, null, null, new String[]{"b-43","b-1249","1635/0","b0p038","b0p137","0109589","0108799","0104350","b5a001","0107263","8223","8266","10866001"}); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "bienenbuettel", os, null, new String[]{"bienenbüttel"}, null); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "sodexo", os, null, new String[]{"sodexo"}, null); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "schloss", os, null, new String[]{"schloss"}, null); // new String[]{"8266","10866001"}  new String[]{"galke"}
        	exportKetten(prodcode, betcode, "edeka", os, null, new String[]{"edeka"}, null);
        	exportKetten(prodcode, betcode, "5cluster", os, null, new String[]{"schloss","kroog","sodexo","weinkeller"}, null);
        	exportKetten(prodcode, betcode, "SodexoServicesGmbHOPS", os, null, new String[]{"sodexo services gmbh ops"}, null, true);
        	exportKetten(prodcode, betcode, "SodexoServicesGmbHPWC", os, null, new String[]{"sodexo services gmbh pwc"}, null, true);
        	exportKetten(prodcode, betcode, "SenatorKroog", os, null, new String[]{"senator kroog"}, null, true);
        	exportKetten(prodcode, betcode, "ServaasSchlosshotel", os, null, new String[]{"servaas schlosshotel"}, null, true);
        	exportKetten(prodcode, betcode, "Weinkeller", os, null, new String[]{"weinkeller"}, null, true);
        	exportKetten(prodcode, betcode, "Golfanlage", os, null, new String[]{"golfanlage"}, null, true);
        	exportKetten(prodcode, betcode, "Bienenbuettel_LK", os, null, new String[]{"bienenbüttel"}, null, true);

        	exportKetten(prodcode, betcode, "FcLink_48088", os, null, null, new String[]{"48088","6832","104350"});
        	exportKetten(prodcode, betcode, "FcLink_8266", os, null, null, new String[]{"8266","8223","10866001"}); 
        	exportKetten(prodcode, betcode, "FcLink_2660002", os, null, null, new String[]{"2660002","8710"});
        	exportKetten(prodcode, betcode, "FcLink_EgyptLots", os, null, null, new String[]{"48088","6832","104350","8266","8223","10866001","2660002","8710"});
        	exportKetten(prodcode, betcode, "FcLink_EgyptLots_plus", os, null, null, new String[]{"48088","6832","104350","8266","8223","10866001","2660002","8710"}, true);
        	exportKetten(prodcode, betcode, "FcLink_OrganicGreen", os, null, new String[]{"organic green"}, null);
        	exportKetten(prodcode, betcode, "fenugreek_agasaat", os, new String[]{"bockshornklee"}, new String[]{"agasaat"}, null);
    	}
    	*/
    	System.out.println("Fin!");		
	}
	/*
	private void exportKetten(Hashtable<String, Vector<String>> prodcode, Hashtable<String, String> betcode, String fname, String[] obersteStufe, String[] keyProdukt, String[] keyBetriebsname, String[] keyLot) {
		exportKetten(prodcode, betcode, fname, obersteStufe, keyProdukt, keyBetriebsname, keyLot, false);
	}
	private void exportKetten(Hashtable<String, Vector<String>> prodcode, Hashtable<String, String> betcode, String fname, String[] obersteStufe, String[] keyProdukt, String[] keyBetriebsname, String[] keyLot, boolean showLDs) {
		System.err.println(fname + " (processing...)");
		Vector<String> theWs;// = getWs();
		Hashtable<String, String> wMapping = new Hashtable<String, String>();
		int wIndex = 0;
		theWs = new Vector<String>();
		String keyProds = "Alle Lebensmittel";
		String keyBNs = "Alle Betriebe";
		String keyLots = "Alle Chargen";
		Hashtable<String, String> existBetriebe = new Hashtable<String, String>();
		Hashtable<String, String> zwischenBetriebe = new Hashtable<String, String>();
		Hashtable<String, String> endBetriebe = new Hashtable<String, String>();
		boolean doAnonymize = false;
		boolean usePfeile4Text = true;
		boolean do25kg = false;
		boolean showLDText = false; // Der Text an den Pfeilen ist leider zu unübersichtlich, wenn zu viele Beteiligte...
		boolean doPfeilDicke = true; 
		String sql = "SELECT " + DBKernel.delimitL("Artikel_Lieferung") +
		"," + DBKernel.delimitL("Vorprodukt") + " FROM " + DBKernel.delimitL("Lieferung_Lieferungen");
		ResultSet rs = DBKernel.getResultSet(sql, false);
		try {
			if (rs != null && rs.first()) {
				String excelExport = "";
				String excelNodesExport = "";
				String digraph = "digraph EHEC_Cluster {\n";
				digraph += "#\tranksep=2;\n";
				digraph += "#\tsize=2;\n";
				digraph += "\tedge [color=black,arrowhead=open];\n";
				// label=\"\",
				digraph += "\tnode [fontsize=8,color=black,style=filled,fillcolor=white,shape=box];\n\n";	// (doAnonymize ? "circle,fixedsize=true,width=0.15,height=0.15" : "box") + "];\n\n";	
				Hashtable<String, String> lds = new Hashtable<String, String>();
		 		do {		 					 			
					int produzent1 = 0, produzent2 = 0, charge1 = 0, charge2 = 0;
					String p1 = "", p2 = "", p0 = "";
					String bl1 = "", bl2 = "", bl0 = "";
					String plz1 = "", plz2 = "", plz0 = "";
					String os0 = "", os1 = "", os2 = "";
					String ld0 = "", ld1 = "", ld2 = "";
					String bez0 = "", bez1 = "", bez2 = "";
					String cn0 = "", cn1 = "", cn2 = "";
					String qn0 = "", qn1 = "", qn2 = "";
					double qnd0 = 0, qnd1 = 0, qnd2 = 0;
					String vpe0 = "", vpe1 = "", vpe2 = "";
					boolean qne0 = false, qne1 = false, qne2 = false;
					Object recipient0 = null, recipient1 = null, recipient2 = null;
					charge1 = rs.getInt("Artikel_Lieferung");
					charge2 = rs.getInt("Vorprodukt");

					boolean goFurther = true;
		 			if (keyProdukt != null && keyProdukt.length > 0) {
		 				Vector<String> keyProduktNew = new Vector<String>();
		 				for (int ii=0;ii<keyProdukt.length;ii++) {
		 					Vector<String> v = prodcode.get(keyProdukt[ii]);
		 					if (v == null) {
		 						keyProduktNew.add(keyProdukt[ii].toLowerCase());
		 					}
		 					else {
			 					for (int iii=0;iii<v.size();iii++) {
			 						keyProduktNew.add(v.get(iii).toLowerCase());
			 					}		 						
		 					}
		 				}
		 				if (!chainHasProdukt1(keyProduktNew, charge1)) goFurther = chainHasProdukt2(keyProduktNew, charge2);
		 				keyProds = mkStr(keyProdukt);
		 			}
		 			if (goFurther && keyBetriebsname != null && keyBetriebsname.length > 0) {
		 				if (!chainHasBetrieb1(keyBetriebsname, charge1)) goFurther = chainHasBetrieb2(keyBetriebsname, charge2);
		 				keyBNs = mkStr(keyBetriebsname);
		 			}
		 			if (goFurther && keyLot != null && keyLot.length > 0) {
		 				if (!chainHasLot1(keyLot, charge1)) goFurther = chainHasLot2(keyLot, charge2);
		 				keyLots = mkStr(keyLot);
		 			}

		 			if (goFurther) {
			 			// Get weitere Info
						sql = "SELECT " + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("ID") +
						"," + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("Kontaktadresse") +
						"," + DBKernel.delimitL("Lieferdatum") + "," + DBKernel.delimitL("Bezeichnung") + "," + DBKernel.delimitL("ChargenNr") +
						"," + DBKernel.delimitL("#Units1") + "," + DBKernel.delimitL("BezUnits1") + "," + DBKernel.delimitL("#Units2") +
						"," + DBKernel.delimitL("BezUnits2") + "," + DBKernel.delimitL("Unitmenge") + "," + DBKernel.delimitL("UnitEinheit") +
						"," + DBKernel.delimitL("Empfänger") + 
						" FROM " + DBKernel.delimitL("Artikel_Lieferung") +
						" LEFT JOIN " + DBKernel.delimitL("Produzent_Artikel") +
						" ON " + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("ID") +
						" = " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("Artikel") +
						" LEFT JOIN " + DBKernel.delimitL("Produzent") +
						" ON " + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("Produzent") +
						" = " + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("ID") +
						" WHERE " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("ID") +
						"=" + charge1;
						ResultSet rs2 = DBKernel.getResultSet(sql, false);
						if (rs2 != null && rs2.first()) {
							produzent1 = rs2.getInt(1);
							p1 = DBKernel.getValue("Kontakte", "ID", rs2.getString(2), "Name").toString();
							p1 = p1.replaceAll("\"", "'");
							if (theWs.size() > 0) {
								if (!wMapping.containsKey(p1)) {
									wMapping.put(p1, theWs.get(wIndex % theWs.size()));
									wIndex++;
								}
								p1 = wMapping.get(p1);
							}
							bl1 = DBKernel.getValue("Kontakte", "ID", rs2.getString(2), "Bundesland").toString();
							plz1 = DBKernel.getValue("Kontakte", "ID", rs2.getString(1), "PLZ").toString();
							ld1 = rs2.getString(3);
							bez1 = rs2.getString(4);
							cn1 = rs2.getString(5).replaceAll("\n", " ");
							qn1 = "[" + rs2.getString(6) + " " + rs2.getString(7) + " ; " + rs2.getString(8) + " " + rs2.getString(9) + " ; " + rs2.getString(10) + " " + rs2.getString(11) + "]";
							qnd1 = (getDouble(rs2.getString(6)) * getDouble(rs2.getString(8)) * getDouble(rs2.getString(10)));
							if (is1_3Mischung(bez1)) {
								qnd1 /= 3.0;
								//System.err.println(bez1);
							}
							qn1 =  rs2.getString(11);
							vpe1 = rs2.getString(10) + rs2.getString(11);
							if (qnd1 >= 25 && qn1.equalsIgnoreCase("kg")) qne1 = true;
							if (rs2.getObject(12) != null) {
								recipient1 = DBKernel.getValue("Kontakte", "ID", DBKernel.getValue("Produzent", "ID", rs2.getString(12), "Kontaktadresse").toString(), "Name");
								if (recipient1 != null) {
									recipient1 = recipient1.toString().replaceAll("\"", "'");
									if (doAnonymize) {
										Object blr1 = DBKernel.getValue("Kontakte", "ID", DBKernel.getValue("Produzent", "ID", rs2.getString(12), "Kontaktadresse").toString(), "Bundesland");
										recipient1 = betcode.get(recipient1 + " (" + blr1 + ")");
									}
								}
							}
							else recipient1 = null;
							if (rs2.next()) {
								System.err.println("mist1...");
							}
						}
						sql = "SELECT " + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("ID") +
						"," + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("Kontaktadresse") +
						"," + DBKernel.delimitL("Lieferdatum") + "," + DBKernel.delimitL("Bezeichnung") + "," + DBKernel.delimitL("ChargenNr") +
						"," + DBKernel.delimitL("#Units1") + "," + DBKernel.delimitL("BezUnits1") + "," + DBKernel.delimitL("#Units2") +
						"," + DBKernel.delimitL("BezUnits2") + "," + DBKernel.delimitL("Unitmenge") + "," + DBKernel.delimitL("UnitEinheit") +
						"," + DBKernel.delimitL("Empfänger") + 
						" FROM " + DBKernel.delimitL("Artikel_Lieferung") +
						" LEFT JOIN " + DBKernel.delimitL("Produzent_Artikel") +
						" ON " + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("ID") +
						" = " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("Artikel") +
						" LEFT JOIN " + DBKernel.delimitL("Produzent") +
						" ON " + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("Produzent") +
						" = " + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("ID") +
						" WHERE " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("ID") +
						"=" + charge2;
						rs2 = DBKernel.getResultSet(sql, false);
						if (rs2 != null && rs2.first()) {
							produzent2 = rs2.getInt(1);
							p2 = DBKernel.getValue("Kontakte", "ID", rs2.getString(2), "Name").toString();
							p2 = p2.replaceAll("\"", "'");
							if (theWs.size() > 0) {
								if (!wMapping.containsKey(p2)) {
									wMapping.put(p2, theWs.get(wIndex % theWs.size()));
									wIndex++;
								}
								p2 = wMapping.get(p2);
							}
							bl2 = DBKernel.getValue("Kontakte", "ID", rs2.getString(2), "Bundesland").toString();
							plz2 = DBKernel.getValue("Kontakte", "ID", rs2.getString(1), "PLZ").toString();
							ld2 = rs2.getString(3);
							bez2 = rs2.getString(4);
							cn2 = rs2.getString(5).replaceAll("\n", " ");
							qn2 = "[" + rs2.getString(6) + " " + rs2.getString(7) + " ; " + rs2.getString(8) + " " + rs2.getString(9) + " ; " + rs2.getString(10) + " " + rs2.getString(11) + "]";
							qnd2 = (getDouble(rs2.getString(6)) * getDouble(rs2.getString(8)) * getDouble(rs2.getString(10)));
							if (is1_3Mischung(bez2)) {
								qnd2 /= 3.0;
								//System.err.println(bez2);
							}
							qn2 =  rs2.getString(11);
							vpe2 = rs2.getString(10) + rs2.getString(11);
							if (qnd2 >= 25 && qn2.equalsIgnoreCase("kg")) qne2 = true;
							if (rs2.getObject(12) != null) {
								recipient2 = DBKernel.getValue("Kontakte", "ID", DBKernel.getValue("Produzent", "ID", rs2.getString(12), "Kontaktadresse").toString(), "Name");
								if (recipient2 != null) {
									recipient2 = recipient2.toString().replaceAll("\"", "'");
									if (doAnonymize) {
										Object blr2 = DBKernel.getValue("Kontakte", "ID", DBKernel.getValue("Produzent", "ID", rs2.getString(12), "Kontaktadresse").toString(), "Bundesland");
										recipient2 = betcode.get(recipient2 + " (" + blr2 + ")");
									}
								}
							}
							else recipient2 = null;
							if (rs2.next()) {
								System.err.println("mist2...");
							}
						}

						String str1 = "";
						if (!do25kg || qne1 && qne2) {
							if (doAnonymize) {
								str1 = "\t\"" + betcode.get(p2 + " (" + bl2 + ")") + "\" -> \"" + betcode.get(p1 + " (" + bl1 + ")") + "\";\n";
								if (!existBetriebe.containsKey(betcode.get(p2 + " (" + bl2 + ")"))) existBetriebe.put(betcode.get(p2 + " (" + bl2 + ")"), plz2 + "\t" + ld2 + "\t" + qnd2 + "\t" + qn2 + "\t[" + vpe2 + "]");
								if (!existBetriebe.containsKey(betcode.get(p1 + " (" + bl1 + ")"))) existBetriebe.put(betcode.get(p1 + " (" + bl1 + ")"), plz1 + "\t" + ld1 + "\t" + qnd1 + "\t" + qn1 + "\t[" + vpe1 + "]");
								if (!zwischenBetriebe.containsKey(betcode.get(p2 + " (" + bl2 + ")"))) zwischenBetriebe.put(betcode.get(p2 + " (" + bl2 + ")"), plz2 + "\t" + ld2 + "\t" + qnd2 + "\t" + qn2 + "\t[" + vpe2 + "]");
							}
							else {
								str1 = "\t\"" + p2 + " (" + bl2 + ")\" -> \"" + p1 + " (" + bl1 + ")\";\n";
								if (!existBetriebe.containsKey(p2 + " (" + bl2 + ")")) existBetriebe.put(p2 + " (" + bl2 + ")", plz2 + "\t" + ld2 + "\t" + qnd2 + "\t" + qn2 + "\t[" + vpe2 + "]");
								if (!existBetriebe.containsKey(p1 + " (" + bl1 + ")")) existBetriebe.put(p1 + " (" + bl1 + ")", plz1 + "\t" + ld1 + "\t" + qnd1 + "\t" + qn1 + "\t[" + vpe1 + "]");
								if (!zwischenBetriebe.containsKey(p2 + " (" + bl2 + ")")) zwischenBetriebe.put(p2 + " (" + bl2 + ")", plz2 + "\t" + ld2 + "\t" + qnd2 + "\t" + qn2 + "\t[" + vpe2 + "]");
							}
						}
						if (digraph.indexOf(str1) < 0) {
							if (!do25kg || qne1 && qne2) digraph += str1;
							if (!betcode.containsKey(p1 + " (" + bl1 + ")")) {
								System.err.println(p1 + " (" + bl1 + ")");
							}
							if (!betcode.containsKey(p2 + " (" + bl2 + ")")) {
								System.err.println(p2 + " (" + bl2 + ")");
							}
							excelExport += betcode.get(p2 + " (" + bl2 + ")") + "\t" + betcode.get(p1 + " (" + bl1 + ")") + "\n";
							//excelExport += p1 + " (" + bl1 + ")\t" + p2 + " (" + bl2 + ")\n";
						}
						if (showLDs) {
							if (usePfeile4Text) {
								if ((ld2.length() > 0 || qnd2 != 0) && str1.indexOf("-> \"" + (doAnonymize ? recipient2 : recipient2 + " (")) >= 0) {
									//lds.put(str1, "");
									String theStr = (ld2.trim().length() == 0 ? "NN" : ld2) + " " + qnd2 + " " + qn2 + " [" + vpe2 + "]" + "\t" + plz2 + "\t" + cn2;
									int lw = doPfeilDicke ? estimateLineWidth(qn2, qnd2) : 1;
									if (lds.containsKey(str1)) {
										String str = lds.get(str1);
										if (str.indexOf(theStr) < 0) lds.put(str1, theStr + "\\n" + str);
									} 
									else lds.put(str1, theStr + "\",style=\"setlinewidth(" + lw + ")");								
								}
								else if ((ld1.length() > 0 || qnd1 != 0) && str1.indexOf("-> \"" + (doAnonymize ? recipient1 : recipient1 + " (")) >= 0) {
									//lds.put(str1, "");
									String theStr = (ld1.trim().length() == 0 ? "NN" : ld1) + " " + qnd1 + " " + qn1 + " [" + vpe1 + "]" + "\t" + plz1 + "\t" + cn1;
									int lw = doPfeilDicke ? estimateLineWidth(qn1, qnd1) : 1;
									if (lds.containsKey(str1)) {
										String str = lds.get(str1);
										if (str.indexOf(theStr) < 0) lds.put(str1, theStr + "\\n" + str);
									}
									else lds.put(str1, theStr + "\",style=\"setlinewidth(" + lw + ")");								
								}
							}
							else {
								if (doAnonymize) str1 = "\t\"" + betcode.get(p1 + " (" + bl1 + ")");
								else str1 = p1 + " (" + bl1 + ")";
								if (!lds.containsKey(str1)) lds.put(str1, "");
								if (ld1.length() > 0 || qnd1 != 0) {
									String str = lds.get(str1);
									String theStr = ld1 + "\t" + bez1 + "\t" + cn1 + "\t" + qnd1 + " " + qn1 + " [" + vpe1 + "]" + "\t" + recipient1 + "\t" + plz1 + "\t" + cn1;
									if (str.indexOf(theStr) < 0) lds.put(str1, theStr + "\\n" + str);
								}
								if (doAnonymize) str1 = "\t\"" + betcode.get(p2 + " (" + bl2 + ")");
								else str1 = p2 + " (" + bl2 + ")";
								if (!lds.containsKey(str1)) lds.put(str1, "");
								if (ld2.length() > 0 || qnd2 != 0) {
									String str = lds.get(str1);
									String theStr = ld2 + "\t" + bez2 + "\t" + cn2 + "\t" + qnd2 + " " + qn2 + " [" + vpe2 + "]" + "\t" + recipient2 + "\t" + plz2 + "\t" + cn2;
									if (str.indexOf(theStr) < 0) lds.put(str1, theStr + "\\n" + str);
								}
							}
						}
						
						sql = "SELECT " + DBKernel.delimitL("Artikel_Lieferung") + " FROM " + DBKernel.delimitL("Lieferung_Lieferungen") +
							" WHERE " + DBKernel.delimitL("Vorprodukt") + "=" + charge1;
						rs2 = DBKernel.getResultSet(sql, false);
						if (rs2 == null || !rs2.first()) {
							sql = "SELECT " + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("Kontaktadresse") +
							"," + DBKernel.delimitL("Lieferdatum") + "," + DBKernel.delimitL("Bezeichnung") + "," + DBKernel.delimitL("ChargenNr") +
							"," + DBKernel.delimitL("#Units1") + "," + DBKernel.delimitL("BezUnits1") + "," + DBKernel.delimitL("#Units2") +
							"," + DBKernel.delimitL("BezUnits2") + "," + DBKernel.delimitL("Unitmenge") + "," + DBKernel.delimitL("UnitEinheit") +
							"," + DBKernel.delimitL("Empfänger") + 
							" FROM " + DBKernel.delimitL("Artikel_Lieferung") +
							" LEFT JOIN " + DBKernel.delimitL("Produzent") +
							" ON " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("Empfänger") +
							" = " + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("ID") +
							" LEFT JOIN " + DBKernel.delimitL("Produzent_Artikel") +
							" ON " + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("ID") +
							" = " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("Artikel") +
							" WHERE " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("ID") +
							"=" + charge1;
							rs2 = DBKernel.getResultSet(sql, false);
							if (rs2 != null && rs2.first() && rs2.getObject(1) != null) {
								p0 = DBKernel.getValue("Kontakte", "ID", rs2.getString(1), "Name").toString();
								p0 = p0.replaceAll("\"", "'");
								if (theWs.size() > 0) {
									if (!wMapping.containsKey(p0)) {
										wMapping.put(p0, theWs.get(wIndex % theWs.size()));
										wIndex++;
									}
									p0 = wMapping.get(p0);
								}
								bl0 = DBKernel.getValue("Kontakte", "ID", rs2.getString(1), "Bundesland").toString();
								plz0 = DBKernel.getValue("Kontakte", "ID", rs2.getString(1), "PLZ").toString();
								os0 = rs2.getString(2) + "\t" + rs2.getString(4);

								ld0 = rs2.getString(2);
								bez0 = rs2.getString(3);
								cn0 = rs2.getString(4).replaceAll("\n", " ");
								qn0 = "[" + rs2.getString(5) + " " + rs2.getString(6) + " ; " + rs2.getString(7) + " " + rs2.getString(8) + " ; " + rs2.getString(9) + " " + rs2.getString(10) + "]";
								qnd0 = (getDouble(rs2.getString(5)) * getDouble(rs2.getString(7)) * getDouble(rs2.getString(9)));
								if (is1_3Mischung(bez0)) {
									qnd0 /= 3.0;
									//System.err.println(bez0);
								}
								qn0 =  rs2.getString(10);
								vpe0 = rs2.getString(9) + rs2.getString(10);
								if (qnd0 >= 25 && qn0.equalsIgnoreCase("kg")) qne0 = true;
								if (rs2.getObject(11) != null) {
									recipient0 = DBKernel.getValue("Kontakte", "ID", DBKernel.getValue("Produzent", "ID", rs2.getString(11), "Kontaktadresse").toString(), "Name");
									if (recipient0 != null) {
										recipient0 = recipient0.toString().replaceAll("\"", "'");
										if (doAnonymize) {
											Object blr0 = DBKernel.getValue("Kontakte", "ID", DBKernel.getValue("Produzent", "ID", rs2.getString(11), "Kontaktadresse").toString(), "Bundesland");
											recipient0 = betcode.get(recipient0 + " (" + blr0 + ")");
										}
									}
								}
								else recipient2 = null;
								//if (digraph.indexOf("\t\"" + p1 + " (" + bl1 + ")\" -> \"" + p0 + " (" + bl0 + ")\";\n") < 0) {
								str1 = "";
								if (!do25kg || qne1 && qne0) {
									if (doAnonymize) {
										str1 = "\t\"" + betcode.get(p1 + " (" + bl1 + ")") + "\" -> \"" + betcode.get(p0 + " (" + bl0 + ")") + "\";\n";
										//System.out.println(p0 + " (" + bl0 + ")");
										if (!existBetriebe.containsKey(betcode.get(p0 + " (" + bl0 + ")"))) existBetriebe.put(betcode.get(p0 + " (" + bl0 + ")"), plz0 + "\t" + os0 + "\t" + qnd0 + "\t" + qn0 + "\t[" + vpe0 + "]");
										if (!endBetriebe.containsKey(betcode.get(p0 + " (" + bl0 + ")") + " (" + plz0 + ")")) endBetriebe.put(betcode.get(p0 + " (" + bl0 + ")") + " (" + plz0 + ")", plz0 + "\t" + os0 + "\t" + qnd0 + "\t" + qn0 + "\t[" + vpe0 + "]");
									}
									else {
										str1 = "\t\"" + p1 + " (" + bl1 + ")\" -> \"" + p0 + " (" + bl0 + ")\";\n";
										if (!existBetriebe.containsKey(p0 + " (" + bl0 + ")")) existBetriebe.put(p0 + " (" + bl0 + ")", plz0 + "\t" + os0 + "\t" + qnd0 + "\t" + qn0 + "\t[" + vpe0 + "]");
										if (!endBetriebe.containsKey(p0 + " (" + bl0 + ") (" + plz0 + ")")) endBetriebe.put(p0 + " (" + bl0 + ") (" + plz0 + ")", plz0 + "\t" + os0 + "\t" + qnd0 + "\t" + qn0 + "\t[" + vpe0 + "]");
									}
								}
								if (digraph.indexOf(str1) < 0) {
									if (!do25kg || qne1 && qne0) digraph += str1;
									if (!betcode.containsKey(p1 + " (" + bl1 + ")")) {
										System.err.println(p1 + " (" + bl1 + ")");
									}
									if (!betcode.containsKey(p0 + " (" + bl0 + ")")) {
										System.err.println(p0 + " (" + bl0 + ")");
									}
									excelExport += betcode.get(p1 + " (" + bl1 + ")") + "\t" + betcode.get(p0 + " (" + bl0 + ")") + "\n";
									//excelExport += p0 + " (" + bl0 + ")\t" + p1 + " (" + bl1 + ")\n";
								}
								if (showLDs) {
									if (usePfeile4Text) {
										if ((ld0.length() > 0 || qnd0 != 0) && str1.indexOf("-> \"" + (doAnonymize ? recipient0 : recipient0 + " (")) >= 0) {
											//lds.put(str1, "");
											String theStr = (ld0.trim().length() == 0 ? "NN" : ld0) + " " + qnd0 + " " + qn0 + " [" + vpe0 + "]" + "\t" + plz0 + "\t" + cn0;
											int lw = doPfeilDicke ? estimateLineWidth(qn0, qnd0) : 1;
											if (lds.containsKey(str1)) {
												String str = lds.get(str1);
												if (str.indexOf(theStr) < 0) lds.put(str1, theStr + "\\n" + str);
											}
											else lds.put(str1, theStr + "\",style=\"setlinewidth(" + lw + ")");								
										}
										else if ((ld1.length() > 0 || qnd1 != 0) && str1.indexOf("-> \"" + (doAnonymize ? recipient1 : recipient1 + " (")) >= 0) {
											//lds.put(str1, "");
											String theStr = (ld1.trim().length() == 0 ? "NN" : ld1) + " " + qnd1 + " " + qn1 + " [" + vpe1 + "]" + "\t" + plz1 + "\t" + cn1;
											int lw = doPfeilDicke ? estimateLineWidth(qn1, qnd1) : 1;
											if (lds.containsKey(str1)) {
												String str = lds.get(str1);
												if (str.indexOf(theStr) < 0) lds.put(str1, theStr + "\\n" + str);
											}
											else lds.put(str1, theStr + "\",style=\"setlinewidth(" + lw + ")");								
										}
									}
									else {
										if (doAnonymize) str1 = "\t\"" + betcode.get(p0 + " (" + bl0 + ")");
										else str1 = p0 + " (" + bl0 + ")";
										if (!lds.containsKey(str1)) lds.put(str1, "");
										if (doAnonymize) str1 = "\t\"" + betcode.get(p1 + " (" + bl1 + ")");
										else str1 = p1 + " (" + bl1 + ")";
										if (!lds.containsKey(str1)) lds.put(str1, "");
									}
								}
								if (rs2.next()) {
									System.err.println("mist3...");
								}
							}
						}
		 			}
				} while (rs.next());
		 		Hashtable<String, Double> nodesIn = new Hashtable<String, Double>();
		 		Hashtable<String, Double> nodesOut = new Hashtable<String, Double>();
				if (showLDs) {					
					digraph += "\n";
					String mengen4XLS = "";
					for (Enumeration e=lds.keys(); e.hasMoreElements();) {
                        String name = (String)e.nextElement();
                        String datums = (String)lds.get(name);
                        //System.out.println(name + "\t" + datums);
                        if (usePfeile4Text) {
                            String str = name.substring(1, name.length()-2);
                            if (showLDText) digraph = digraph.replace(str, str + " [label=\"" + datums + "\",fontsize=6]");
                            else if (doPfeilDicke) digraph = digraph.replace(str, str + " [" + datums.substring(datums.indexOf("style=\"setlinewidth(")) + "\"]");
                            
                            String newname = name.replace(" -> ", "\t");
                            newname = newname.replace("\"", ""); 
                            newname = newname.replace(";", ""); 
                            int index = datums.indexOf("\",style=\"setlinewidth(");                                                       
                            String qans = datums;
                            String newQans = "";
                            if (index >= 0) qans = datums.substring(0, index);//.replaceAll("\\\\n", "\n\t");
                            qans = qans.replace(" ", "\t");
                            qans = qans.replace("\\n", "\n\t\t");
                            double val=0, total = 0;
                            String tunit, unit = "";
                            String tplz, plz = "";
                            String vpe = "";
                            StringTokenizer tok = new StringTokenizer(qans, "\n");
                            while (tok.hasMoreTokens()) {
                                StringTokenizer tok2 = new StringTokenizer(tok.nextToken());
                                if (tok2.hasMoreTokens()) newQans += tok2.nextToken(); // Datum
                                val = 0;
                                if (tok2.hasMoreTokens()) val = getDouble(tok2.nextToken());
                                //total += val;
                                tunit = "";
                                if (tok2.hasMoreTokens()) {
                                	tunit = tok2.nextToken();
                                	if (unit.length() == 0) unit = tunit;
                                	else if (!unit.equalsIgnoreCase(tunit)) unit = "?";
                                }
                                if (tunit.equals("kg")) {
                                	total += val;
                                	newQans += "\t" + val;
                                }
                                else if (tunit.equals("g")) {
                                	total += val / 1000.0;
                                	newQans += "\t" + (val / 1000.0);
                                }
                                else {
                                	System.err.println("Komische unit... ->" + tunit + "\t" + newname + "\t" + qans);
                                }
                                if (tok2.hasMoreTokens()) newQans += "\t" + tok2.nextToken(); // VPE
                                if (tok2.hasMoreTokens()) { // PLZ
                                	tplz = tok2.nextToken();
                                	if (plz.length() == 0) plz = tplz;
                                	else if (!plz.equalsIgnoreCase(tplz)) plz = "?";
                                	newQans += "\t" + plz;
                                }
                                if (tok2.hasMoreTokens()) { // ChargenNr, wurde extra eingeführt, um scheinbare DOUbletten korrekt zählen zu können, die ChargenNr hat in Klammern einen laufenden Index (1), (2), ...das wird schon in der Exceltabelle so angelegt
                                	newQans += "\t" + tok2.nextToken();
                                }
                                newQans += "\n";
                            }
                            StringTokenizer tok2 = new StringTokenizer(newname, "\t");
                            String outCompany=null, inCompany=null;
                            if (tok2.hasMoreTokens()) outCompany = tok2.nextToken().trim();
                            if (tok2.hasMoreTokens()) inCompany = tok2.nextToken().trim();
                            if (inCompany != null) {
                            	if (nodesIn.containsKey(inCompany)) nodesIn.put(inCompany, nodesIn.get(inCompany) + total);
                            	else nodesIn.put(inCompany, total);
                            }
                            if (outCompany != null) {
                            	if (nodesOut.containsKey(outCompany)) nodesOut.put(outCompany, nodesOut.get(outCompany) + total);
                            	else nodesOut.put(outCompany, total);
                            }
                            //mengen4XLS += newname.substring(1, newname.length() -1) + "\t\t" + total + "\t" + unit + "\t" + plz + "\n\t\t" + qans + "\n";
                            //for (int z=0;z<10;z++) qans = qans.replace("\t\t", "\t");
                            //for (int z=0;z<10;z++) qans = qans.replace("\n\t", "\n");
                            //if (qans.indexOf("kg]") < 0) mengen4XLS += qans.trim() + "\t" + newname.substring(1, newname.length() -1) + "\n";
                            if (true || newname.indexOf("Davert") >= 0) {
                            	mengen4XLS += newQans.trim() + "\t" + newname.substring(1, newname.length() -1) + (isExport(newname.substring(1, newname.length() -1)) ? "\t" + total : "") + "\n";
                            }
                        }
                        else {
                            digraph += "\t\"" + name + "\" [label=\"" + name + "\\n" + datums + "\",labelloc=b,fontsize=6]\n"; //I5 [shape=ellipse,color=red,style=bold,label="Caroline                        	
                        }
					}
					writeFile(DBKernel.HSHDB_PATH + "gv\\" + fname + (doEFSA ? "_EU":"_D") + (doAnonymize ? "_0" : "_1") + (do25kg ? "_25kg" : "") + "_qans.xls", mengen4XLS);
					
					//System.out.println(p1 + " (" + ld1 + ")\t" + p2 + " (" + ld2 + ")");
				}
				String endBetriebeStr = "";
				for (Enumeration e=existBetriebe.keys(); e.hasMoreElements();) {
		          String name = (String)e.nextElement();
		          String plz = existBetriebe.get(name);
		          if (!zwischenBetriebe.containsKey(name)) endBetriebeStr += name + "\t" + plz + "\n";
		          //digraph += "\t\"" + name + "\" -> \"" + name + "\" [headlabel=\"" + name + "\", fontsize=6, color=transparent]\n";
		          excelNodesExport += name;
		          if (nodesIn.containsKey(name)) excelNodesExport += "\t" + nodesIn.get(name);
	        	  if (nodesOut.containsKey(name)) excelNodesExport += "\t" + nodesOut.get(name);
	        	  excelNodesExport += "\n";	        		  
		          	if (name.equals("DE00") || name.equals("NI00")) digraph += "\t\"" + name + "\" [fillcolor = green]\n";
		          	else if (name.equals("FR177")) digraph += "\t\"" + name + "\" [fillcolor = green]\n";
		          	else if (name.equals("ET168")) digraph += "\t\"" + name + "\" [fillcolor = red]\n";
		          	else if (isD(name.substring(0, 2))) digraph += "\t\"" + name + "\" [fillcolor = white]\n";
		          	else if (name.indexOf("Austria") >= 0 || name.indexOf("Österreich") >= 0 || doAnonymize && name.startsWith("AT")) digraph += "\t\"" + name + "\" [fillcolor = red]\n";
		            else if (name.indexOf("Holland") >= 0 || name.indexOf("Niederlande") >= 0 || doAnonymize && name.startsWith("NL")) digraph += "\t\"" + name + "\" [fillcolor = orange]\n";
		            else if (name.indexOf("UK") >= 0 || doAnonymize && name.startsWith("UK")) digraph += "\t\"" + name + "\" [fillcolor = salmon2]\n";
		            else if (name.indexOf("Dänemark") >= 0 || doAnonymize && name.startsWith("DK")) digraph += "\t\"" + name + "\" [fillcolor = deepskyblue]\n";
		            else if (name.indexOf("Ungarn") >= 0 || doAnonymize && name.startsWith("HU")) digraph += "\t\"" + name + "\" [fillcolor = brown]\n";
		            else if (name.indexOf("Slovenien") >= 0 || doAnonymize && name.startsWith("SI")) digraph += "\t\"" + name + "\" [fillcolor = gold1]\n";
		            else if (name.indexOf("Spanien") >= 0 || doAnonymize && name.startsWith("ES")) digraph += "\t\"" + name + "\" [fillcolor = yellow]\n";
		            else if (name.indexOf("Portugal") >= 0 || doAnonymize && name.startsWith("PT")) digraph += "\t\"" + name + "\" [fillcolor = darkseagreen]\n";
		            else if (name.indexOf("Estland") >= 0 || doAnonymize && name.startsWith("EE")) digraph += "\t\"" + name + "\" [fillcolor = navy]\n";
		            else if (name.indexOf("Greece") >= 0 || doAnonymize && name.startsWith("EL")) digraph += "\t\"" + name + "\" [fillcolor = cyan]\n";
		            else if (name.indexOf("Gironde") >= 0 || doAnonymize && name.startsWith("FR")) digraph += "\t\"" + name + "\" [fillcolor = greenyellow]\n";
		            else if (name.indexOf("Bulgarien") >= 0 || doAnonymize && name.startsWith("BG")) digraph += "\t\"" + name + "\" [fillcolor = chartreuse4]\n";
		            else if (name.indexOf("Schweiz") >= 0 || doAnonymize && name.startsWith("CH")) digraph += "\t\"" + name + "\" [fillcolor = lemonchiffon2]\n";
		            else if (name.indexOf("Polen") >= 0 || doAnonymize && name.startsWith("PL")) digraph += "\t\"" + name + "\" [fillcolor = darkorange1]\n";
		            else if (name.indexOf("Sweden") >= 0 || doAnonymize && name.startsWith("SE")) digraph += "\t\"" + name + "\" [fillcolor = cadetblue1]\n";
		            else if (name.indexOf("Egypt") >= 0 || doAnonymize && name.startsWith("ET")) digraph += "\t\"" + name + "\" [fillcolor = firebrick]\n";
		            else if (name.indexOf("Italien") >= 0 || name.indexOf("Italy") >= 0 || doAnonymize && name.startsWith("IT")) digraph += "\t\"" + name + "\" [fillcolor = cornflowerblue]\n";
		            else if (name.indexOf("Türkei") >= 0 || doAnonymize && name.startsWith("TUR")) digraph += "\t\"" + name + "\" [fillcolor = deeppink]\n";
		            else if (name.indexOf("China") >= 0 || doAnonymize && name.startsWith("CN")) digraph += "\t\"" + name + "\" [fillcolor = goldenrod2]\n";
		            else if (name.indexOf("Kanada") >= 0 || doAnonymize && name.startsWith("CAN")) digraph += "\t\"" + name + "\" [fillcolor = goldenrod4]\n";
		            else if (name.indexOf("Mallorca") >= 0 || doAnonymize && name.startsWith("ES")) digraph += "\t\"" + name + "\" [fillcolor = blueviolet]\n";
		            else if (name.indexOf("Denmark") >= 0 || doAnonymize && name.startsWith("DK")) digraph += "\t\"" + name + "\" [fillcolor = crimson]\n";
		            else if (name.indexOf("Germany") >= 0 || doAnonymize && name.startsWith("DE")) digraph += "\t\"" + name + "\" [fillcolor = aquamarine]\n";
		            else if (name.indexOf("Litauen") >= 0 || doAnonymize && name.startsWith("LT")) digraph += "\t\"" + name + "\" [fillcolor = aquamarine]\n";
		            else if (name.indexOf("Luxemburg") >= 0 || doAnonymize && name.startsWith("DE")) digraph += "\t\"" + name + "\" [fillcolor = aquamarine]\n";
		            else if (name.indexOf("Finland") >= 0 || doAnonymize && name.startsWith("FI")) digraph += "\t\"" + name + "\" [fillcolor = aquamarine]\n";
		            else if (name.indexOf("Belgien") >= 0 || doAnonymize && name.startsWith("BL")) digraph += "\t\"" + name + "\" [fillcolor = aquamarine]\n";
		          //}
		          //System.out.println(name);
				}
				endBetriebeStr = "";
				for (Enumeration e=endBetriebe.keys(); e.hasMoreElements();) {
		          String name = (String)e.nextElement();
		          String plz = endBetriebe.get(name);
		          endBetriebeStr += name + "\t" + plz + "\n";
				}
				writeFile(DBKernel.HSHDB_PATH + "gv\\" + fname + (doEFSA ? "_EU":"_D") + (doAnonymize ? "_0" : "_1") + (do25kg ? "_25kg" : "") + "_eb.xls", endBetriebeStr);

				digraph += "\n\toverlap=false\n";
				digraph += "\tlabel=\"\\n\\nEHEC Cluster (backward strategy) - [" + keyProds + "][" + keyBNs + "][" + keyLots +"]" + (do25kg ? "[min. 25kg]" : "") + " - " + (new Date(System.currentTimeMillis())) + "\"\n";
				digraph += "\tfontsize=20;\n";
				digraph += "}\n";
				writeFile(DBKernel.HSHDB_PATH + "gv\\" + fname + (doEFSA ? "_EU":"_D") + (doAnonymize ? "_0" : "_1") + (do25kg ? "_25kg" : "") + ".txt", digraph);
				writeFile(DBKernel.HSHDB_PATH + "gv\\" + fname + (doEFSA ? "_EU":"_D") + (doAnonymize ? "_0" : "_1") + (do25kg ? "_25kg" : "") + "_links.xls", excelExport);
				writeFile(DBKernel.HSHDB_PATH + "gv\\" + fname + (doEFSA ? "_EU":"_D") + (doAnonymize ? "_0" : "_1") + (do25kg ? "_25kg" : "") + "_node.xls", excelNodesExport);				
				//System.out.println(digraph);
				//System.out.println(excelExport);
			}
		}
		catch (Exception e) {MyLogger.handleException(e);}
	}
	private boolean isExport(String companies) {
		return !isAusland(companies, true) && isAusland(companies, false);
	}
	private boolean isAusland(String companies, boolean first) {
		boolean result = false;
		int index, index2;
		if (first) {
			index = companies.indexOf("(");
			index2 = companies.indexOf(")");
		}
		else {
			index = companies.lastIndexOf("(");
			index2 = companies.lastIndexOf(")");
		}
		if (index > 0 && index2 > 0) {
			if (index2 - index > 3 || companies.substring(index+1, index2).equals("UK")) result = true; // d.h. in den Klammern sind max. 2 Zeichen => Bundesland
			//if (result) System.err.println(companies); else System.out.println(companies);			
		}
		return result;
	}
	private double getDouble(String str) {
		double result = 1;
		try {
			result = Double.parseDouble(str);
		}
		catch (Exception e) {}
		return result;
	}
	private void writeFile(String fname, String inhalt) {
		try {
			  // Create file 
			  FileWriter fstream = new FileWriter(fname);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(inhalt);
			  //Close the output stream
			  out.close();
		}
		catch (Exception e){e.printStackTrace();}
	}
	private boolean chainHasProdukt1(Vector<String> keyProdukt, int charge) {
		boolean result = false;
		try {
			String sql = "SELECT " + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("Bezeichnung") +
			" FROM " + DBKernel.delimitL("Artikel_Lieferung") +
			" LEFT JOIN " + DBKernel.delimitL("Produzent_Artikel") +
			" ON " + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("ID") +
			" = " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("Artikel") +
			" WHERE " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("ID") + "=" + charge;
			//keys = "[";
			if (keyProdukt.size() > 0) {
				sql += " AND (";
				for (int i=0;i<keyProdukt.size();i++) {
					sql += " LOCATE('" + keyProdukt.get(i) + "',LCASE(" + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("Bezeichnung") + "))>0 OR";	
					//keys += keyProdukt[i] + ";";
				}
				//keys = keys.substring(0, keys.length() - 1) + "]";
				sql = sql.substring(0, sql.length() - 3) + ")";
			}
			
			ResultSet rs = DBKernel.getResultSet(sql, false);
			if (rs != null && rs.first()) {
				return true;
			}
			else {
				sql = "SELECT " + DBKernel.delimitL("Artikel_Lieferung") + " FROM " + DBKernel.delimitL("Lieferung_Lieferungen") +
				" WHERE " + DBKernel.delimitL("Vorprodukt") + "=" + charge;
				rs = DBKernel.getResultSet(sql, false);
				if (rs != null && rs.first() && rs.getObject(1) != null) {
					do {		
						int newCharge = rs.getInt(1);	
						result = chainHasProdukt1(keyProdukt, newCharge);
						if (result) break;
					} while (rs.next());
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}
		return result;
	}
	private boolean chainHasProdukt2(Vector<String> keyProdukt, int charge) {
		boolean result = false;
		try {
			String sql = "SELECT " + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("Bezeichnung") +
			" FROM " + DBKernel.delimitL("Artikel_Lieferung") +
			" LEFT JOIN " + DBKernel.delimitL("Produzent_Artikel") +
			" ON " + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("ID") +
			" = " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("Artikel") +
			" WHERE " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("ID") + "=" + charge;
			//keys = "[";
			if (keyProdukt.size() > 0) {
				sql += " AND (";
				for (int i=0;i<keyProdukt.size();i++) {
					sql += " LOCATE('" + keyProdukt.get(i) + "',LCASE(" + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("Bezeichnung") + "))>0 OR";	
					//keys += keyProdukt[i] + ";";
				}
				//keys = keys.substring(0, keys.length() - 1) + "]";
				sql = sql.substring(0, sql.length() - 3) + ")";
			}
			
			ResultSet rs = DBKernel.getResultSet(sql, false);
			if (rs != null && rs.first()) {
				return true;
			}
			else {
				sql = "SELECT " + DBKernel.delimitL("Vorprodukt") + " FROM " + DBKernel.delimitL("Lieferung_Lieferungen") +
				" WHERE " + DBKernel.delimitL("Artikel_Lieferung") + "=" + charge;
				rs = DBKernel.getResultSet(sql, false);
				if (rs != null && rs.first() && rs.getObject(1) != null) {
					do {		
						int newCharge = rs.getInt(1);	
						result = chainHasProdukt2(keyProdukt, newCharge);
						if (result) break;
					} while (rs.next());
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}
		return result;
	}
	private boolean chainHasLot1(String[] keyLot, int charge) {
		boolean result = false;
		try {
			String sql = "SELECT " + DBKernel.delimitL("ChargenNr") +
			" FROM " + DBKernel.delimitL("Artikel_Lieferung") +
			" WHERE " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("ID") + "=" + charge;
			//keys = "[";
			sql += " AND (";
			for (int i=0;i<keyLot.length;i++) {
				sql += " LOCATE('" + keyLot[i] + "',LCASE(" + DBKernel.delimitL("ChargenNr") + "))>0 OR";	
				//keys += keyProdukt[i] + ";";
			}
			//keys = keys.substring(0, keys.length() - 1) + "]";
			sql = sql.substring(0, sql.length() - 3) + ")";
			
			ResultSet rs = DBKernel.getResultSet(sql, false);
			if (rs != null && rs.first()) {
				return true;
			}
			else {
				sql = "SELECT " + DBKernel.delimitL("Artikel_Lieferung") + " FROM " + DBKernel.delimitL("Lieferung_Lieferungen") +
				" WHERE " + DBKernel.delimitL("Vorprodukt") + "=" + charge;
				rs = DBKernel.getResultSet(sql, false);
				if (rs != null && rs.first() && rs.getObject(1) != null) {
					do {		
						int newCharge = rs.getInt(1);	
						result = chainHasLot1(keyLot, newCharge);
						if (result) break;
					} while (rs.next());
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}
		return result;
	}
	private boolean chainHasLot2(String[] keyLot, int charge) {
		boolean result = false;
		try {
			String sql = "SELECT " + DBKernel.delimitL("ChargenNr") +
			" FROM " + DBKernel.delimitL("Artikel_Lieferung") +
			" WHERE " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("ID") + "=" + charge;
			//keys = "[";
			sql += " AND (";
			for (int i=0;i<keyLot.length;i++) {
				sql += " LOCATE('" + keyLot[i] + "',LCASE(" + DBKernel.delimitL("ChargenNr") + "))>0 OR";	
				//keys += keyProdukt[i] + ";";
			}
			//keys = keys.substring(0, keys.length() - 1) + "]";
			sql = sql.substring(0, sql.length() - 3) + ")";
			// SELECT "ChargenNr" FROM "Artikel_Lieferung" WHERE "Artikel_Lieferung"."ID"=283 AND ( LOCATE('b-43',LCASE("ChargenNr"))>0 OR LOCATE('b-1249',LCASE("ChargenNr"))>0 OR LOCATE('1635/0',LCASE("ChargenNr"))>0 OR LOCATE('b0p038',LCASE("ChargenNr"))>0 OR LOCATE('b0p137',LCASE("ChargenNr"))>0 OR LOCATE('49228',LCASE("ChargenNr"))>0 OR LOCATE('49466',LCASE("ChargenNr"))>0 OR LOCATE('0109589',LCASE("ChargenNr"))>0 OR LOCATE('0108799',LCASE("ChargenNr"))>0 OR LOCATE('48928',LCASE("ChargenNr"))>0 OR LOCATE('0104350',LCASE("ChargenNr"))>0 OR LOCATE('b5a001',LCASE("ChargenNr"))>0 OR LOCATE('0107263',LCASE("ChargenNr"))>0 OR LOCATE('8223',LCASE("ChargenNr"))>0 OR LOCATE('8266',LCASE("ChargenNr"))>0 OR LOCATE('10866001',LCASE("ChargenNr"))>0)
			ResultSet rs = DBKernel.getResultSet(sql, false);
			if (rs != null && rs.first()) {
				return true;
			}
			else {
				sql = "SELECT " + DBKernel.delimitL("Vorprodukt") + " FROM " + DBKernel.delimitL("Lieferung_Lieferungen") +
				" WHERE " + DBKernel.delimitL("Artikel_Lieferung") + "=" + charge;
				rs = DBKernel.getResultSet(sql, false);
				if (rs != null && rs.first() && rs.getObject(1) != null) {
					do {		
						int newCharge = rs.getInt(1);	
						result = chainHasLot2(keyLot, newCharge);
						if (result) break;
					} while (rs.next());
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}
		return result;
	}
	private boolean chainHasBetrieb1(String[] keyBetriebsname, int charge) {
		boolean result = false;
		try {
			String sql = "SELECT " + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("Name") +
			" FROM " + DBKernel.delimitL("Artikel_Lieferung") +
			" LEFT JOIN " + DBKernel.delimitL("Produzent_Artikel") +
			" ON " + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("ID") +
			" = " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("Artikel") +
			" LEFT JOIN " + DBKernel.delimitL("Produzent") +
			" ON " + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("ID") +
			" = " + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("Produzent") +
			" LEFT JOIN " + DBKernel.delimitL("Kontakte") +
			" ON " + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("ID") +
			" = " + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("Kontaktadresse") +
			" WHERE " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("ID") + "=" + charge;
			//keys = "[";
 			
 			sql += " AND (";
			for (int i=0;i<keyBetriebsname.length;i++) {
				sql += " LOCATE('" + keyBetriebsname[i] + "',LCASE(" + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("Name") + "))>0 OR";	
				//keys += keyProdukt[i] + ";";
			}
			//keys = keys.substring(0, keys.length() - 1) + "]";
			sql = sql.substring(0, sql.length() - 3) + ")";
			
			ResultSet rs = DBKernel.getResultSet(sql, false);
			if (rs != null && rs.first()) {
				return true;
			}
			else {
				sql = "SELECT " + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("Name") +
				" FROM " + DBKernel.delimitL("Artikel_Lieferung") +
				" LEFT JOIN " + DBKernel.delimitL("Produzent") +
				" ON " + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("ID") +
				" = " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("Empfänger") +
				" LEFT JOIN " + DBKernel.delimitL("Kontakte") +
				" ON " + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("ID") +
				" = " + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("Kontaktadresse") +
				" WHERE " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("ID") + "=" + charge;
				//keys = "[";
	 			
	 			sql += " AND (";
				for (int i=0;i<keyBetriebsname.length;i++) {
					sql += " LOCATE('" + keyBetriebsname[i] + "',LCASE(" + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("Name") + "))>0 OR";	
					//keys += keyProdukt[i] + ";";
				}
				//keys = keys.substring(0, keys.length() - 1) + "]";
				sql = sql.substring(0, sql.length() - 3) + ")";
				
				rs = DBKernel.getResultSet(sql, false);
				if (rs != null && rs.first()) {
					return true;
				}
				else {
					sql = "SELECT " + DBKernel.delimitL("Artikel_Lieferung") + " FROM " + DBKernel.delimitL("Lieferung_Lieferungen") +
					" WHERE " + DBKernel.delimitL("Vorprodukt") + "=" + charge;
					rs = DBKernel.getResultSet(sql, false);
					if (rs != null && rs.first() && rs.getObject(1) != null) {
						do {		
							int newCharge = rs.getInt(1);	
							result = chainHasBetrieb1(keyBetriebsname, newCharge);
							if (result) break;
						} while (rs.next());
					}
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}
		return result;
	}
	private boolean chainHasBetrieb2(String[] keyBetriebsname, int charge) {
		boolean result = false;
		try {
			String sql = "SELECT " + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("Name") +
			" FROM " + DBKernel.delimitL("Artikel_Lieferung") +
			" LEFT JOIN " + DBKernel.delimitL("Produzent_Artikel") +
			" ON " + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("ID") +
			" = " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("Artikel") +
			" LEFT JOIN " + DBKernel.delimitL("Produzent") +
			" ON " + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("ID") +
			" = " + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("Produzent") +
			" LEFT JOIN " + DBKernel.delimitL("Kontakte") +
			" ON " + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("ID") +
			" = " + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("Kontaktadresse") +
			" WHERE " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("ID") + "=" + charge;
			//keys = "[";
 			
 			sql += " AND (";
			for (int i=0;i<keyBetriebsname.length;i++) {
				sql += " LOCATE('" + keyBetriebsname[i] + "',LCASE(" + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("Name") + "))>0 OR";	
				//keys += keyProdukt[i] + ";";
			}
			//keys = keys.substring(0, keys.length() - 1) + "]";
			sql = sql.substring(0, sql.length() - 3) + ")";
			
			ResultSet rs = DBKernel.getResultSet(sql, false);
			if (rs != null && rs.first()) {
				return true;
			}
			else {
				sql = "SELECT " + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("Name") +
				" FROM " + DBKernel.delimitL("Artikel_Lieferung") +
				" LEFT JOIN " + DBKernel.delimitL("Produzent") +
				" ON " + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("ID") +
				" = " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("Empfänger") +
				" LEFT JOIN " + DBKernel.delimitL("Kontakte") +
				" ON " + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("ID") +
				" = " + DBKernel.delimitL("Produzent") + "." + DBKernel.delimitL("Kontaktadresse") +
				" WHERE " + DBKernel.delimitL("Artikel_Lieferung") + "." + DBKernel.delimitL("ID") + "=" + charge;

	 			sql += " AND (";
				for (int i=0;i<keyBetriebsname.length;i++) {
					sql += " LOCATE('" + keyBetriebsname[i] + "',LCASE(" + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("Name") + "))>0 OR";	
					//keys += keyProdukt[i] + ";";
				}
				//keys = keys.substring(0, keys.length() - 1) + "]";
				sql = sql.substring(0, sql.length() - 3) + ")";
				
				rs = DBKernel.getResultSet(sql, false);
				if (rs != null && rs.first()) {
					return true;
				}
				else {
					sql = "SELECT " + DBKernel.delimitL("Vorprodukt") + " FROM " + DBKernel.delimitL("Lieferung_Lieferungen") +
					" WHERE " + DBKernel.delimitL("Artikel_Lieferung") + "=" + charge;
					rs = DBKernel.getResultSet(sql, false);
					if (rs != null && rs.first() && rs.getObject(1) != null) {
						do {		
							int newCharge = rs.getInt(1);	
							result = chainHasBetrieb2(keyBetriebsname, newCharge);
							if (result) {
								//System.out.println(charge + "\t" + newCharge + "\t" + result);
								break;
							}
						} while (rs.next());
					}
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}
		return result;
	}
	private String mkStr(String[] strArr) {
		String result = "";
		if (strArr != null) {
			for (int i=0;i<strArr.length;i++) {
				result += ";" + strArr[i];
			}
			if (result.length() > 0) result = result.substring(1);			
		}
		return result;
	}
	private Hashtable<String, String> getBetriebCode(String filename) {
	    Hashtable<String, String> result = new Hashtable<String, String>();
		try {
			InputStream is = new FileInputStream(filename);

			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet;
			HSSFRow row;

			sheet = wb.getSheetAt(0); 
			int numRows = sheet.getLastRowNum() + 1;
		      
			for (int i=0;i<numRows;i++) {
				row = sheet.getRow(i);
				if (row != null) {		
				      String key = getStrVal(row.getCell(0));
				      if (doEFSA && isD(key.substring(0, 2))) key = "DE" + key.substring(2);
				      String betrieb = getStrVal(row.getCell(1));
				      result.put(betrieb, key);
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}
		return result;
	}
	private boolean isD(String kz) {
		return kz.startsWith("DE") || kz.startsWith("BE") || kz.startsWith("BB")
		|| kz.startsWith("BW") || kz.startsWith("BY")
		|| kz.startsWith("HB") || kz.startsWith("HE")
		|| kz.startsWith("HH") || kz.startsWith("MV")
		|| kz.startsWith("NI") || kz.startsWith("NW")
		|| kz.startsWith("RP") || kz.startsWith("SH")
		|| kz.startsWith("SL") || kz.startsWith("SN")
		|| kz.startsWith("ST") || kz.startsWith("TH");
	}
	private Hashtable<String, Vector<String>> getProduktCode(String filename) {
	    Hashtable<String, Vector<String>> result = new Hashtable<String, Vector<String>>();
	    Hashtable<String, String> checkTable = new Hashtable<String, String>();
		try {
			InputStream is = new FileInputStream(filename);

			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet;
			HSSFRow row;

			sheet = wb.getSheetAt(0); 
			int numRows = sheet.getLastRowNum() + 1;
		      
			for (int i=0;i<numRows;i++) {
				row = sheet.getRow(i);
				if (row != null) {		
				      String key = getStrVal(row.getCell(0));
				      String bezeichnung = getStrVal(row.getCell(1));
				      checkTable.put(bezeichnung, bezeichnung);
				      Vector<String> v;
				      if (result.containsKey(key)) v = result.get(key);
				      else v = new Vector<String>();
				      v.add(bezeichnung);
				      result.put(key, v);
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}
		try {
			String sql = "SELECT " + DBKernel.delimitL("Produzent_Artikel") + "." + DBKernel.delimitL("Bezeichnung") +
			" FROM " + DBKernel.delimitL("Produzent_Artikel");
			
			ResultSet rs = DBKernel.getResultSet(sql, false);
			if (rs != null && rs.first()) {
				do {
					String str = rs.getString(1);
					if (str.trim().length() > 0 && !checkTable.containsKey(str)) System.err.println("not contain: " + rs.getString(1));
				} while (rs.next());
			}
		}
		catch (Exception e) {e.printStackTrace();}
		return result;
	}
	private String getStrVal(HSSFCell cell) {
		  	String result = "";
				if (cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
				}
				else if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					result = cell.getStringCellValue();
				}
				else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
					double dbl = cell.getNumericCellValue();
					if (Math.round(dbl) == dbl) result = "" + ((int) dbl);
					else result = "" + cell.getNumericCellValue();
				}
				else {
					result = cell.toString();
				}
		  	return result;
		  }
	private boolean is1_3Mischung(String bezeichnung) {
		return bezeichnung.equals("Alnatura Keimsaat Milde Mischung") ||
		bezeichnung.equals("Sprossen Mischung 8x125 g") ||
		bezeichnung.equals("agaKeimmix scharf ");		
	}
	private int estimateLineWidth(String qn, double qnd) {
		if (qn.equalsIgnoreCase("g")) return 1;
		else if (qnd < 100) return 4;
		else if (qnd < 1000) return 8;
		else return 16;
	}
	private Vector<String> getWs() {
		Vector<String> result = new Vector<String>();
		try {
			BufferedReader in = new BufferedReader(new FileReader("C:/Dokumente und Einstellungen/Weiser/Desktop/Team8/text.txt"));
			String zeile = null;
			while ((zeile = in.readLine()) != null) {
				StringTokenizer tok = new StringTokenizer(zeile);
				while (tok.hasMoreTokens()) result.add(tok.nextToken());
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}	
		return result;
	}
	*/
	/* (non-Javadoc)
	 * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
	 */
}
