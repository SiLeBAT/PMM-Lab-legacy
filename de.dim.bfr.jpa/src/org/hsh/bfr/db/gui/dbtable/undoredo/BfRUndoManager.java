/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.undoredo;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import org.hsh.bfr.db.gui.dbtable.MyDBTable;

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
