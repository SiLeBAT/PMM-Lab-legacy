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
package org.hsh.bfr.db.gui.dbtable.undoredo;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.MyDBTables;
import org.hsh.bfr.db.MyLogger;
import org.hsh.bfr.db.MyTable;

/**
 * @author Weiser
 *
 */
public class TableCellEdit extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tablename;
	private Object[] rowBefore;
	private Object[] rowAfter;
	private long editTime;
	private Vector<TableCellEdit> foreignTCEs = null;
	
	public TableCellEdit(String tablename, Object[] rowBefore, Object[] rowAfter) {
		this.tablename = tablename;
		this.rowBefore = rowBefore;
		this.rowAfter = rowAfter;
		editTime = System.currentTimeMillis();
	}
	public void addForeignDelete(TableCellEdit tce) {
		if (foreignTCEs == null) foreignTCEs = new Vector<TableCellEdit>();
		foreignTCEs.add(tce);
	}
	
	public String getPresentationName() {
		return "Cell Edit";
	}
	public String getTableName() {
		return tablename;
	}
	public Object[] getRowBefore() {
		return rowBefore;
	}
	public Object[] getRowAfter() {
		return rowAfter;
	}
	public long getEditTime() {
		return editTime;
	}
	public void undo() throws CannotUndoException {
		if (DBKernel.debug) System.out.println("Undo\t" + editTime);
		// Call the UndoableEdit class for housekeeping, wenn alles glatt gelaufen ist		
		if (doMyUndoRedo(rowBefore, rowAfter)) super.undo();
	}
	private boolean doMyUndoRedo(Object[] row1, Object[] row2) { // rowBefore, rowAfter
		if (DBKernel.debug) System.out.println(tablename + "\t" + rowBefore + "\t" + rowAfter);

		MyTable myT = MyDBTables.getTable(tablename);
		if (row1 == null) {
			if (DBKernel.topTable != null) {
				if (DBKernel.topTable.equals(DBKernel.myList.getMyDBTable())) {
					DBKernel.myList.setSelection(myT.getTablename());
				}
				DBKernel.topTable.setTable(myT);
				DBKernel.topTable.setSelectedID((Integer)row2[0]);
			}
			else if (DBKernel.debug) System.out.println("kein TopTable????");
			DBKernel.undoredoStart = System.currentTimeMillis();
			DBKernel.sendRequest("DELETE FROM " + DBKernel.delimitL(tablename) + " WHERE " + DBKernel.delimitL("ID") + "=" + row2[0], false); // tableID
			if (DBKernel.topTable != null) {
				DBKernel.topTable.myRefresh();
			}
			return true;
		}
		try {
			PreparedStatement ps = null;
			boolean doUpdate = false;
			if (row2 == null) {
				ps = DBKernel.getDBConnection().prepareStatement(myT.getInsertSQL1(), Statement.RETURN_GENERATED_KEYS);
			}
			else {
				ps = DBKernel.getDBConnection().prepareStatement(myT.getUpdateSQL1());
				doUpdate = true;
			}
			String[] ft = myT.getFieldTypes();
			int i=1;
			for (;i<=ft.length;i++) { // ID wird hier nicht benötigt
				if (ft[i-1].equals("DOUBLE")) {
					if (row1[i] == null) ps.setNull(i, java.sql.Types.DOUBLE);
					else ps.setDouble(i, (Double)row1[i]);											
				}
				else if (ft[i-1].startsWith("VARCHAR(") || ft[i-1].startsWith("BLOB(")) {
					if (row1[i] == null) ps.setNull(i, java.sql.Types.VARCHAR);
					else ps.setString(i, (String)row1[i]);											
				}
				else if (ft[i-1].equals("BOOLEAN")) {
					if (row1[i] == null) ps.setNull(i, java.sql.Types.BOOLEAN);
					else ps.setBoolean(i, (Boolean)row1[i]);											
				}
				else if (ft[i-1].equals("INTEGER")) {
					if (row1[i] == null) ps.setNull(i, java.sql.Types.INTEGER);
					else ps.setInt(i, (Integer)row1[i]);											
				}
				else if (ft[i-1].equals("BIGINT")) {
					if (row1[i] == null) ps.setNull(i, java.sql.Types.BIGINT);
					else ps.setLong(i, (Long)row1[i]);											
				}
			}
			if (!myT.getHideScore()) { // INTEGER
				if (row1[i] == null) ps.setNull(i, java.sql.Types.INTEGER);
				else ps.setInt(i, (Integer)row1[i]);	
				i++;
			}
			if (!myT.getHideKommentar()) { // VARCHAR(1023)
				if (row1[i] == null) ps.setNull(i, java.sql.Types.VARCHAR);
				else ps.setString(i, (String)row1[i]);											
				i++;
			}
			if (!myT.getHideTested()) { // BOOLEAN
				if (row1[i] == null) ps.setNull(i, java.sql.Types.BOOLEAN);
				else ps.setBoolean(i, (Boolean)row1[i]);											
				i++;
			}
				
			DBKernel.undoredoStart = System.currentTimeMillis();
			if (doUpdate) {
				ps.setInt(row1.length, (Integer) row1[0]); // WHERE ID = ...
				ps.execute();
			}
			else if (ps.executeUpdate() > 0) { // INSERT
				Integer lastID = DBKernel.getLastInsertedID(ps);
				row1[0] = lastID; // wenigstens noch abspeichern für die nächste Undo/Redo Runde
				if (foreignTCEs != null) {
					for (i=0;i<foreignTCEs.size();i++) {
						TableCellEdit tce = foreignTCEs.get(i);
						MyTable myT1 = MyDBTables.getTable(tce.getTableName());
						MyTable[] myTs = myT1.getForeignFields();
						for (int j=0;j<myTs.length;j++) {
							if (myTs[j] != null && myTs[j].equals(myT)) {
								DBKernel.undoredoStart = System.currentTimeMillis();
								DBKernel.sendRequest("UPDATE " + DBKernel.delimitL(myT1.getTablename()) +
										" SET " + DBKernel.delimitL(myT1.getFieldNames()[j]) + "=" + lastID +
										" WHERE " + DBKernel.delimitL("ID") + "=" + tce.getRowAfter()[0], false); // tableID								
							}
						}
					}
				}
			} 
			if (DBKernel.topTable != null) {
				if (DBKernel.topTable.equals(DBKernel.myList.getMyDBTable())) {
					DBKernel.myList.setSelection(myT.getTablename());
				}
				DBKernel.topTable.setTable(myT);
				DBKernel.topTable.setSelectedID((Integer)row1[0]);
				DBKernel.topTable.myRefresh();					
			}
			else if (DBKernel.debug) System.out.println("kein TopTable????");
		}
		catch (Exception e) {MyLogger.handleException(e);return false;}
		return true;
	}
	public void redo() throws CannotRedoException {
		if (DBKernel.debug) System.out.println("Redo\t" + editTime);
		// Call the UndoableEdit class for housekeeping, wenn alles glatt gelaufen ist		
		if (doMyUndoRedo(rowAfter, rowBefore)) super.redo();
	}
}
