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

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * Extension of the Swing UndoManager that manages the PCS Edit Menu items
 * 
 * @author dominik
 *
 */
public class BfRUndoManager extends UndoManager {

	private static final long serialVersionUID = 1917643938808307260L;	
	
	private static final int MIN_TIME = 200;	// msecs between do and undo
	
	private boolean undoingOrRedoing;
	/**
	 * Time of last undoable edit<p>
	 * We use this as a workaround when undoing in the following case:<br>
	 * - table cell was edited and <br>
	 * - undo toolbar-button clicked<br>
	 * this causes a stopEdit which in turn causes setValueAt and produces a new edit.<br>
	 * If the last edit was created less then MINTIME far (ex. 0.2sec), we undo twice.
	 */
	private GregorianCalendar editTime;
	
	
	public synchronized boolean addEdit(UndoableEdit anEdit) {
		this.editTime = new GregorianCalendar();
        editTime.add(Calendar.MILLISECOND, MIN_TIME);	// only after this time, an  xxxxxxxxxx
        boolean success = super.addEdit(anEdit);
        if (success) {
        	adjustMenuState();
        }
		return success;
	}
	
	@Override
	public synchronized void undo()  {
		if (this.canUndo()) {
			try {
				  	// do the undo
		            super.undo();
		        }
		        finally {
		        	// will call undoTo adjustMenuState();
		        }			
		}
	}
	@Override
	public synchronized void redo()  {
		if (this.canRedo()) {
			try {
				/*
				UndoableEdit e = this.editToBeRedone();
	          	if (e instanceof TableCellEdit) {
	        		TableCellEdit tce = (TableCellEdit) e;
	        		System.out.println("Remove edit\t" + tce.getPresentationName() + "\t" + tce.getEditTime() + "\t" + tce.toString());
	        	}
	        	*/
		        super.redo();
		    }
	        finally {
	        	// will call undoTo adjustMenuState();
	        }			
		}
	}


    @Override
    protected void undoTo(UndoableEdit edit) {
        try {
        	undoingOrRedoing = true;
            super.undoTo(edit);
        }
        finally {
        	undoingOrRedoing = false;
        	adjustMenuState();
        }
    }

    @Override
    protected void redoTo(UndoableEdit edit) {
        try {
        	undoingOrRedoing = true;
            super.redoTo(edit);
        }
        finally {
        	adjustMenuState();
        	undoingOrRedoing = false;
        }
    }

    @Override
    public void discardAllEdits() {
    	super.discardAllEdits();
    	adjustMenuState();
    }
    

    public void adjustMenuState() {
		// adjust menu state
    	/*
    	DraftGlobals.getInstance().getDraftGui().getMenuHandler().refreshUndoRedo(
				this.getUndoPresentationName(), this.canUndo(), 
				this.getRedoPresentationName(), this.canRedo());
    	// adjust toolbar state
    	globals.getDraftGui().getToolbarHandler().refreshUndoRedoToolbar(
				getUndoPresentationName(), canUndo(), 
				getRedoPresentationName(), canRedo());
				*/
	}


	/**
	 * Returns true while a undo or redo operation is active, otherwise false;
	 * @return the undoingOrRedoing
	 */
	public boolean isUndoingOrRedoing() {
		return undoingOrRedoing;
	}
}
