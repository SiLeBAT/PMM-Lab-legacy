/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.header;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.MouseInputAdapter;

/**
 * @author Armin
 * http://www.jroller.com/santhosh/entry/make_jtable_resiable_better_than
 */
public class TableRowHeaderResizer extends MouseInputAdapter { 
	
  public static Cursor resizeCursor = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR); 

  private int mouseYOffset, resizingRow; 
  private Cursor otherCursor = resizeCursor; 
  private JTable jTable;
  private JTable bigTable;
  
  public TableRowHeaderResizer(JTable rowHeader, JTable bigTable) { 
    jTable = rowHeader; 
    this.bigTable = bigTable; 
    jTable.addMouseListener(this); 
    jTable.addMouseMotionListener(this); 
  } 

  private int getResizingRow(Point p){ 
      return getResizingRow(p, jTable.rowAtPoint(p)); 
  } 

  private int getResizingRow(Point p, int row){ 
      if(row == -1){ 
          return -1; 
      } 
      int col = jTable.columnAtPoint(p); 
      if(col==-1) 
          return -1; 
      Rectangle r = jTable.getCellRect(row, col, true); 
      r.grow(0, -3); 
      if(r.contains(p)) 
          return -1; 

      int midPoint = r.y + r.height / 2; 
      int rowIndex = (p.y < midPoint) ? row - 1 : row; 

      return rowIndex; 
  } 

  public void mousePressed(MouseEvent e){ 
      Point p = e.getPoint(); 

      resizingRow = getResizingRow(p); 
      mouseYOffset = p.y - jTable.getRowHeight(resizingRow); 
  } 

  private void swapCursor(){ 
      Cursor tmp = jTable.getCursor(); 
      jTable.setCursor(otherCursor); 
      otherCursor = tmp; 
  } 

  public void mouseMoved(MouseEvent e){ 
      if ((getResizingRow(e.getPoint())>=0) != (jTable.getCursor() == resizeCursor)){ 
          swapCursor(); 
      } 
  } 

  public void mouseDragged(MouseEvent e){ 
      int mouseY = e.getY(); 

      if (resizingRow >= 0) { 
          int newHeight = mouseY - mouseYOffset; 
          if (newHeight > 0) {
          	jTable.setRowHeight(resizingRow, newHeight);
          	bigTable.setRowHeight(resizingRow, newHeight);
          }
      } 
  } 
} 