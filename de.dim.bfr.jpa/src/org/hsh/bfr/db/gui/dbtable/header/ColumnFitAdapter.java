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
package org.hsh.bfr.db.gui.dbtable.header;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * @author Armin
 *
 */
public class ColumnFitAdapter extends MouseAdapter { 
	       public void mouseClicked(MouseEvent e){ 
	           if (e.getClickCount()==2) {
	               JTableHeader header = (JTableHeader)e.getSource(); 
	               TableColumn tableColumn = getResizingColumn(header, e.getPoint()); 
	               if(tableColumn==null) 
	                   return; 
	               int col = header.getColumnModel().getColumnIndex(tableColumn.getIdentifier()); 
	               JTable table = header.getTable(); 
	               int rowCount = table.getRowCount(); 
	               int width = (int)header.getDefaultRenderer() 
	                       .getTableCellRendererComponent(table, tableColumn.getIdentifier() 
	                               , false, false, -1, col).getPreferredSize().getWidth(); 
	               for(int row = 0; row<rowCount; row++) { 
	                   int preferedWidth = (int)table.getCellRenderer(row, col).getTableCellRendererComponent(table, 
	                           table.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth(); 
	                   width = Math.max(width, preferedWidth); 
	               } 
	               header.setResizingColumn(tableColumn); // this line is very important 
	               tableColumn.setWidth(width+table.getIntercellSpacing().width); 
	           } 
	       } 
	    
	       // copied from BasicTableHeader.MouseInputHandler.getResizingColumn 
	       private TableColumn getResizingColumn(JTableHeader header, Point p){ 
	           return getResizingColumn(header, p, header.columnAtPoint(p)); 
	       } 
	    
	       // copied from BasicTableHeader.MouseInputHandler.getResizingColumn 
	       private TableColumn getResizingColumn(JTableHeader header, Point p, int column){ 
	           if(column==-1){ 
	               return null; 
	           } 
	           Rectangle r = header.getHeaderRect(column); 
	           r.grow(-3, 0); 
	           if(r.contains(p)) 
	               return null; 
	           int midPoint = r.x+r.width/2; 
	           int columnIndex; 
	           if(header.getComponentOrientation().isLeftToRight()) 
	               columnIndex = (p.x<midPoint) ? column-1 : column; 
	           else 
	               columnIndex = (p.x<midPoint) ? column : column-1; 
	           if(columnIndex==-1) 
	               return null; 
	           return header.getColumnModel().getColumn(columnIndex); 
	       } 
	   } 
