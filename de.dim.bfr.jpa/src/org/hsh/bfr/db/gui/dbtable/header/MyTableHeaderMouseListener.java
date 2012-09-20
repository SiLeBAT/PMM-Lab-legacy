/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.header;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.RowSorter.SortKey;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.hsh.bfr.db.gui.dbtable.MyDBTable;

import quick.dbtable.DBTable;

/**
 * @author Armin
 *
 */
public class MyTableHeaderMouseListener implements MouseListener {

  private final MyDBTable theTable;

  public MyTableHeaderMouseListener(final MyDBTable theTable) {
    this.theTable = theTable;
  }

  public void actionPerformed(ActionEvent e) {
/*
  	if (table.getModel() instanceof SortableTableModel) {
      ((SortableTableModel)tabelle.getModel()).sort(Integer.parseInt(e.getActionCommand()));
    }
    */
  }

  public void mouseClicked(MouseEvent e) {
    //super.mouseClicked(e);
    System.out.println("Header clicked : (X: " + e.getX() + ", Y: " + e.getY() + ") With button " + e.getButton() );
    theTable.sorterChanged(null);
}

	public void mouseEntered(MouseEvent arg0) {
    System.out.println("mouseEntered");
	}

	public void mouseExited(MouseEvent arg0) {
    System.out.println("mouseExited");
	}

	public void mousePressed(MouseEvent arg0) {
    System.out.println("mousePressed");
	}

	public void mouseReleased(MouseEvent arg0) {
    System.out.println("mouseReleased");
	}
}