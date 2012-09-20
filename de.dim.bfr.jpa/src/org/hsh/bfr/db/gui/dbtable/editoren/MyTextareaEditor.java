/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.editoren;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import org.hsh.bfr.db.gui.dbtable.MyDBTable;


import quick.dbtable.CellComponent;

/**
 * @author Armin
 *
 */
public class MyTextareaEditor extends JTextArea implements CellComponent, KeyListener {

	private MyDBTable myDB = null;
	private JScrollPane myScroller = null;
	private ActionListener listener = null;
  
	public MyTextareaEditor(MyDBTable myDB) {
		this.myDB = myDB;
		this.setLineWrap(true);
		this.setWrapStyleWord(true);
		myScroller = new JScrollPane(this);
		filterIM();
		this.addKeyListener(this);
  }

  public void setValue(Object value) {
  	if (value == null) this.setText("");
  	else  this.setText(value.toString());
  }

  public Object getValue() {
  	return this.getText();
  }

  public JComponent getComponent() {
     return myScroller;
  }

  public void addActionListener(ActionListener listener) {
  	this.listener = listener;
  	// listener.actionPerformed( null );
  }
  public void valueChangedInMyJComponent() {
    listener.actionPerformed( null );
  }

	private void filterIM() {
		KeyStroke[] disableKeys = new KeyStroke[] {KeyStroke.getKeyStroke("TAB"),KeyStroke.getKeyStroke("ENTER"),KeyStroke.getKeyStroke("ESCAPE")};
				
		InputMap im = new FilteringInputMap(this.getInputMap(WHEN_FOCUSED), disableKeys);
		this.setInputMap(WHEN_FOCUSED, im);				 
		im = new FilteringInputMap(this.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT), disableKeys);
		this.setInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, im);		
	}
	/*
	private void addKeyListener(final MyTextareaEditor mtae) {
    KeyListener keyListener = new KeyListener() {
      public void keyPressed(KeyEvent keyEvent) {
        if ((keyEvent.isAltDown() || keyEvent.isControlDown()) && keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
        	//System.out.println(keyEvent.isConsumed());
        	mtae.insert("\n", mtae.getCaretPosition());
        	keyEvent.consume();
        }
      }
      public void keyReleased(KeyEvent keyEvent) {
        //printIt("Released", keyEvent);
      }
      public void keyTyped(KeyEvent keyEvent) {
      	//printIt("keyTyped", keyEvent);
      }
    };
    this.addKeyListener(keyListener);		
	}
	*/
  public void keyPressed(KeyEvent keyEvent) {
    if ((keyEvent.isAltDown() || keyEvent.isControlDown()) && keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
    	//System.out.println(keyEvent.isConsumed());
    	this.insert("\n", this.getCaretPosition());
    	keyEvent.consume();
    }
    /*
    else if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
    	keyEvent.consume();
    	int row = myDB.getSelectedRow();
    	int col = myDB.getSelectedColumn();
    	if (row < myDB.getRowCount() - 1) {myDB.setRowSelectionInterval(row+1, row+1);myDB.selectCell(row+1, col, true);}
    	else if (col < myDB.getColumnCount() - 1) {myDB.setRowSelectionInterval(row, row);myDB.selectCell(row, col+1, true);}
    	else myDB.clearSelection();
    }
    */
  }
  public void keyReleased(KeyEvent keyEvent) {
    //printIt("Released", keyEvent);
  }
  public void keyTyped(KeyEvent keyEvent) {
  	//printIt("keyTyped", keyEvent);
  }
}
