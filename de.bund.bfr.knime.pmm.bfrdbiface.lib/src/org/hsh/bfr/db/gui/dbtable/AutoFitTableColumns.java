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

/**
 * @author Armin
 *
 */
import java.awt.Dimension; 
import java.awt.Font; 
import java.awt.FontMetrics; 
import java.awt.Component; 
  
import javax.swing.text.JTextComponent; 
import javax.swing.SwingUtilities; 
import javax.swing.JLabel; 
  
import javax.swing.JTable; 
import javax.swing.table.JTableHeader; 
import javax.swing.table.TableColumnModel; 
import javax.swing.table.TableColumn; 
import javax.swing.table.TableCellRenderer; 
  
  
public class AutoFitTableColumns { 
  
    private static final int DEFAULT_COLUMN_PADDING = 5; 
  
  
    /* 
     * @param JTable aTable, the JTable to autoresize the columns on 
     * @param boolean includeColumnHeaderWidth, use the Column Header width as a minimum width 
     * @returns The table width, just in case the caller wants it... 
     */ 
  
    public static int autoResizeTable ( JTable aTable, boolean includeColumnHeaderWidth , boolean includeColumnContentWidth ) 
    { 
        return ( autoResizeTable ( aTable, includeColumnHeaderWidth, includeColumnContentWidth, DEFAULT_COLUMN_PADDING ) ); 
    } 
  
  
    /* 
     * @param JTable aTable, the JTable to autoresize the columns on 
     * @param boolean includeColumnHeaderWidth, use the Column Header width as a minimum width 
     * @param int columnPadding, how many extra pixels do you want on the end of each column 
     * @returns The table width, just in case the caller wants it... 
     */ 
    public static int autoResizeTable ( JTable aTable, boolean includeColumnHeaderWidth, boolean includeColumnContentWidth, int columnPadding ) 
    {
        int columnCount = aTable.getColumnCount();
        //int currentTableWidth = aTable.getWidth();
        int tableWidth = 0;
 
        Dimension cellSpacing = aTable.getIntercellSpacing();
 
        if ( columnCount > 0 )  // must have columns !
        {
            // STEP ONE : Work out the column widths
 
            int columnWidth[] = new int [ columnCount ];
 
            TableColumnModel tableColumnModel = aTable.getColumnModel(); 
            TableColumn tableColumn; 
            for ( int i=0; i<columnCount; i++ ) {
                columnWidth[i] = getMaxColumnWidth ( aTable, i, includeColumnHeaderWidth, includeColumnContentWidth, columnPadding );
                tableColumn = tableColumnModel.getColumn ( i ); 
                if (columnWidth[i] > tableColumn.getPreferredWidth()) tableWidth += columnWidth[i]; 
                else tableWidth += tableColumn.getPreferredWidth(); 
            }
 
            // account for cell spacing too
            tableWidth += ( ( columnCount - 1 ) * cellSpacing.width );
 
            // STEP TWO : Dynamically resize each column
 
            // try changing the size of the column names area
            JTableHeader tableHeader = aTable.getTableHeader();
 
            Dimension headerDim = tableHeader.getPreferredSize();
 
            // headerDim.height = tableHeader.getHeight();
            headerDim.width = tableWidth;
            tableHeader.setPreferredSize ( headerDim );
 
            Dimension interCellSpacing = aTable.getIntercellSpacing();
            Dimension dim = new Dimension();
            int rowHeight = aTable.getRowHeight();
 
            if ( rowHeight == 0 )
                 rowHeight = 16;    // default rowheight
 
            // System.out.println ("Row Height : " + rowHeight );
 
            dim.height = headerDim.height + ( ( rowHeight + interCellSpacing.height ) * aTable.getRowCount() );
            dim.width = tableWidth;
 
            // System.out.println ("AutofitTableColumns.autoResizeTable() - Setting Table size to ( " + dim.width + ", " + dim.height + " )" );
            aTable.setSize ( dim );
 
            for ( int i=0; i<columnCount; i++ ) { 
              tableColumn = tableColumnModel.getColumn ( i );   
              if (columnWidth[i] > tableColumn.getPreferredWidth()) tableColumn.setPreferredWidth ( columnWidth[i] ); 
          } 
 
            aTable.invalidate();
            aTable.doLayout();
            aTable.repaint();
        }
 
        return ( tableWidth );
    }
  
  
  
    /* 
     * @param JTable aTable, the JTable to autoresize the columns on 
     * @param int columnNo, the column number, starting at zero, to calculate the maximum width on 
     * @param boolean includeColumnHeaderWidth, use the Column Header width as a minimum width 
     * @param int columnPadding, how many extra pixels do you want on the end of each column 
     * @returns The table width, just in case the caller wants it... 
     */ 
  
    private static int getMaxColumnWidth ( JTable aTable, int columnNo, 
                                           boolean includeColumnHeaderWidth, 
                                           boolean includeColumnContentWidth,
                                           int columnPadding ) 
    { 
        TableColumn column = aTable.getColumnModel().getColumn ( columnNo ); 
        Component comp = null; 
        int maxWidth = 0; 
  
        if ( includeColumnHeaderWidth ) 
        {
            TableCellRenderer headerRenderer = column.getHeaderRenderer(); 
            if ( headerRenderer != null ) 
            { 
                comp = headerRenderer.getTableCellRendererComponent ( aTable, column.getHeaderValue(), false, false, 0, columnNo ); 
  
                if ( comp instanceof JTextComponent ) 
                { 
                    JTextComponent jtextComp = (JTextComponent)comp; 
  
                    String text = jtextComp.getText(); 
                    Font font = jtextComp.getFont(); 
                    FontMetrics fontMetrics = jtextComp.getFontMetrics ( font ); 
  
                    maxWidth = SwingUtilities.computeStringWidth ( fontMetrics, text ); 
                } 
                else 
                { 
                    maxWidth = comp.getPreferredSize().width; 
                } 
            } 
            else 
            { 
                try 
                { 
                    String headerText = (String)column.getHeaderValue(); 
                    JLabel defaultLabel = new JLabel ( headerText ); 
  
                    Font font = defaultLabel.getFont(); 
                    FontMetrics fontMetrics = defaultLabel.getFontMetrics ( font ); 
  
                    maxWidth = SwingUtilities.computeStringWidth ( fontMetrics, headerText ); 
                } 
                catch ( ClassCastException ce ) 
                { 
                    // Can't work out the header column width.. 
                    maxWidth = 0; 
                } 
            } 
        } 
  
        if (includeColumnContentWidth) {
          TableCellRenderer tableCellRenderer; 
          // Component comp; 
          int cellWidth   = 0; 
    
          for (int i = 0; i < aTable.getRowCount(); i++) 
          { 
              tableCellRenderer = aTable.getCellRenderer ( i, columnNo ); 
    
              comp = tableCellRenderer.getTableCellRendererComponent ( aTable, aTable.getValueAt ( i, columnNo ), false, false, i, columnNo ); 
    
              if ( comp instanceof JTextComponent ) 
              { 
                  JTextComponent jtextComp = (JTextComponent)comp; 
    
                  String text = jtextComp.getText(); 
                  Font font = jtextComp.getFont(); 
                  FontMetrics fontMetrics = jtextComp.getFontMetrics ( font ); 
    
                  int textWidth = SwingUtilities.computeStringWidth ( fontMetrics, text ); 
    
                  maxWidth = Math.max ( maxWidth, textWidth ); 
              } 
              else 
              { 
                  cellWidth = comp.getPreferredSize().width; 
    
                  // maxWidth = Math.max ( headerWidth, cellWidth ); 
                  maxWidth = Math.max ( maxWidth, cellWidth ); 
              } 
          } 
        }
  
        return ( maxWidth + columnPadding ); 
    } 
} 
