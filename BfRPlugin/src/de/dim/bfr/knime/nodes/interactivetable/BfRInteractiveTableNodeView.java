/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
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
package de.dim.bfr.knime.nodes.interactivetable;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.knime.base.node.io.csvwriter.CSVFilesHistoryPanel;
import org.knime.base.node.io.csvwriter.CSVWriter;
import org.knime.base.node.io.csvwriter.FileWriterSettings;
import org.knime.base.node.preproc.filter.row.RowFilterTable;
import org.knime.base.node.preproc.filter.row.rowfilter.EndOfTableException;
import org.knime.base.node.preproc.filter.row.rowfilter.IncludeFromNowOn;
import org.knime.base.node.preproc.filter.row.rowfilter.RowFilter;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.DefaultNodeProgressMonitor;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeProgressMonitorView;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NodeView;
import org.knime.core.node.property.hilite.HiLiteHandler;
import org.knime.core.node.tableview.TableContentModel;
import org.knime.core.node.tableview.TableContentModel.TableContentFilter;
import org.knime.core.node.tableview.TableContentView;
import org.knime.core.node.tableview.TableView;

import de.dim.bfr.knime.util.CustomDataTable;
import de.dim.bfr.knime.util.CustomRowComparator;

/**
 * @author Patrick Seeber, Data In Motion UG (haftungsbeschraenkt)
 */
public class BfRInteractiveTableNodeView extends NodeView {

    /** The Component displaying the table. */
    private final TableView m_tableView;

    /**
     * Starts a new <code>TableNodeView</code> displaying "&lt;no data&gt;".
     * The content comes up when the super class {@link NodeView} calls the
     * {@link #modelChanged()} method.
     * 
     * @param nodeModel the underlying model
     */
    public BfRInteractiveTableNodeView(final BfRInteractiveTableNodeModel nodeModel) {
        super(nodeModel);
        // get data model, init view
        TableContentModel cntModel = nodeModel.getContentModel();
        
        assert (cntModel != null);
        
        m_tableView = new TableView(cntModel);
//        m_tableView.getContentTable().getTableHeader().addMouseListener(new MouseHandler(m_tableView));
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(cntModel);
        m_tableView.getContentTable().setRowSorter(sorter);
        
        cntModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(final TableModelEvent e) {
                // fired when new rows have been seen (refer to description
                // of caching strategy of the model)
                updateTitle();
            }
        });
        getJMenuBar().add(m_tableView.createHiLiteMenu());
        getJMenuBar().add(m_tableView.createNavigationMenu());
        getJMenuBar().add(m_tableView.createViewMenu());
        getJMenuBar().add(createWriteCSVMenu());
        setHiLiteHandler(getNodeModel().getInHiLiteHandler(
        		BfRInteractiveTableNodeModel.INPORT));
        setComponent(m_tableView);
        
    }

	/**
	 * Checks if there is data to display. That is: The model's content model
	 * (keeping the cache and so on) needs to have a {@link DataTable} to show.
	 * This method returns <code>true</code> when the node was executed and
	 * <code>false</code> otherwise.
	 * 
	 * @return <code>true</code> if there is data to display
	 */
    public boolean hasData() {
        return m_tableView.hasData();
    }

    /**
     * Checks is property handler is set.
     * 
     * @return <code>true</code> if property handler set
     * @see TableContentView#hasHiLiteHandler()
     */
    public boolean hasHiLiteHandler() {
        return m_tableView.hasHiLiteHandler();
    }

    /**
     * Sets a new handler for this view.
     * 
     * @param hiLiteHdl the new handler to set, may be <code>null</code> to
     *            disable any brushing
     */
    public void setHiLiteHandler(final HiLiteHandler hiLiteHdl) {
        m_tableView.setHiLiteHandler(hiLiteHdl);
    }

    /**
     * Shall row header encode the color information in an icon.
     * 
     * @param isShowColor <code>true</code> for show icon (and thus the
     *            color), <code>false</code> ignore colors
     * @see org.knime.core.node.tableview.TableRowHeaderView
     *      #setShowColorInfo(boolean)
     */
    public void setShowColorInfo(final boolean isShowColor) {
        m_tableView.getHeaderTable().setShowColorInfo(isShowColor);
    } // setShowColorInfo(boolean)

    /**
     * Is the color info shown.
     * 
     * @return <code>true</code> Icon with the color is present
     */
    public boolean isShowColorInfo() {
        return m_tableView.getHeaderTable().isShowColorInfo();
    } // isShowColorInfo()

    /**
     * Get row height from table.
     * 
     * @return current row height
     * @see javax.swing.JTable#getRowHeight()
     */
    public int getRowHeight() {
        return m_tableView.getRowHeight();
    }

    /**
     * Set a new row height in the table.
     * 
     * @param newHeight the new height
     * @see javax.swing.JTable#setRowHeight(int)
     */
    public void setRowHeight(final int newHeight) {
        m_tableView.setRowHeight(newHeight);
    }

    /**
     * Hilites selected rows in the hilite handler.
     * 
     * @see TableView#hiliteSelected()
     */
    public void hiliteSelected() {
        m_tableView.hiliteSelected();
    }

    /**
     * Unhilites selected rows in the hilite handler.
     * 
     * @see TableView#unHiliteSelected()
     */
    public void unHiliteSelected() {
        m_tableView.unHiliteSelected();
    }

    /**
     * Resets hiliting in the hilite handler.
     * 
     * @see TableView#resetHilite()
     */
    public void resetHilite() {
        m_tableView.resetHilite();
    }

    /**
     * Updates the title of the frame. It prints: "Table (#rows[+] x #cols)". It
     * is invoked each time new rows are inserted (user scrolls down).
     */
    protected void updateTitle() {
        final TableContentView view = m_tableView.getContentTable();
        TableContentModel model = view.getContentModel();
        StringBuffer title = new StringBuffer();
        if (model.hasData()) {
            String tableName = model.getTableName();
        } else {
            title.append(" <no data>");
        }
        super.setViewTitleSuffix(title.toString());
    }

    /**
     * Called from the super class when a property of the node has been changed.
     * 
     * @see org.knime.core.node.NodeView#modelChanged()
     */
    @Override
    protected void modelChanged() {
        if (isOpen()) {
            countRowsInBackground();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
        // unregister from hilite handler
        m_tableView.cancelRowCountingInBackground();
    }

    /**
     * Does nothing since view is in sync anyway.
     * 
     * @see org.knime.core.node.NodeView#onOpen()
     */
    @Override
    protected void onOpen() {
    	countRowsInBackground();
        updateTitle();
    }
    
    private class MouseHandler extends MouseAdapter 
    {
    	private TableView tableView;
    	private Map<Integer, Boolean> sorting = new HashMap<Integer, Boolean>();
    	
    	public MouseHandler(TableView m_tableView) {
    		this.tableView = m_tableView;
		}
    	
        public void mouseClicked(MouseEvent e) {
        	if(e.getButton() != e.BUTTON1)
        		return;
        	
            JTableHeader h = (JTableHeader) e.getSource();
            TableColumnModel columnModel = h.getColumnModel();
            int viewColumn = h.columnAtPoint(e.getPoint());
            int column = columnModel.getColumn(viewColumn).getModelIndex();
            if (column != -1) {
            	DataTable table = tableView.getContentModel().getDataTable();
                try {
                	boolean asc = true;
                	if(sorting.containsKey(column))
                		asc = sorting.get(column);
                	asc = !asc;
					DataTable dt = CustomDataTable.getSortedDataTable(table, new CustomRowComparator(table.getDataTableSpec(), new int[]{column}, new boolean[]{asc}));
					tableView.setDataTable(dt);
					sorting.put(column, asc);
					
				} catch (CanceledExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        }
    }
 

    /**
     * Delegates to the table view that it should start a row counter thread.
     * Multiple invocations of this method don't harm.
     */
    private void countRowsInBackground() {
        if (hasData()) {
            m_tableView.countRowsInBackground();
        }
    }
    
    /* A JMenu that has one entry "Write to CSV file". */
    private JMenu createWriteCSVMenu() {
        JMenu menu = new JMenu("Output");
        JMenuItem item = new JMenuItem("Write CSV");
        item.addPropertyChangeListener("ancestor",
                new PropertyChangeListener() {
                    public void propertyChange(final PropertyChangeEvent evt) {
                        ((JMenuItem)evt.getSource()).setEnabled(hasData());
                    }
                });
        final CSVFilesHistoryPanel hist = new CSVFilesHistoryPanel();
        item.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                int i = JOptionPane.showConfirmDialog(m_tableView, hist,
                        "Choose File", JOptionPane.OK_CANCEL_OPTION);
                if (i == JOptionPane.OK_OPTION) {
                    String sfile = hist.getSelectedFile();
                    File file = CSVFilesHistoryPanel.getFile(sfile);
                    writeToCSV(file);
                }
            }
        });
        menu.add(item);
        return menu;
    }

    /**
     * Called by the JMenu item "Write to CVS", it write the table as shown in
     * table view to a CSV file.
     * 
     * @param file the file to write to
     */
    private void writeToCSV(final File file) {
        // CSV Writer supports ExecutionMonitor. Some table may be big.
        DefaultNodeProgressMonitor progMon = new DefaultNodeProgressMonitor();
        ExecutionMonitor e = new ExecutionMonitor(progMon);
        // Frame of m_tableView (if any)
        Frame f = (Frame)SwingUtilities.getAncestorOfClass(Frame.class,
                m_tableView);
        final NodeProgressMonitorView progView = new NodeProgressMonitorView(f,
                progMon);
        // CSV Writer does not support 1-100 progress (unknown row count)
        progView.setShowProgress(false);
        // Writing is done in a thread (allows repainting of GUI)
        final CSVWriterThread t = new CSVWriterThread(file, e);
        t.start();
        // A thread that waits for t to finish and then disposes the prog view
        new Thread(new Runnable() {
            public void run() {
                try {
                    t.join();
                } catch (InterruptedException ie) {
                    // do nothing. Only dispose the view
                } finally {
                    progView.dispose();
                }
            }
        }).start();
        progView.pack();
        progView.setLocationRelativeTo(m_tableView);
        progView.setVisible(true);
    }

    /** Thread that write the current table to a file. */
    private final class CSVWriterThread extends Thread {

        private final File m_file;

        private final ExecutionMonitor m_exec;

        /**
         * Creates instance.
         * 
         * @param file the file to write to
         * @param exec the execution monitor
         */
        public CSVWriterThread(final File file, final ExecutionMonitor exec) {
            m_file = file;
            m_exec = exec;
        }

        @Override
        public void run() {
            TableContentModel model = m_tableView.getContentModel();
            TableContentFilter filter = model.getTableContentFilter();
            DataTable table = model.getDataTable();
            HiLiteHandler hdl = model.getHiLiteHandler();
            Object mutex = filter.performsFiltering() ? hdl : new Object();
            // if hilighted rows are written only, we need to sync with
            // the handler (prevent others to (un-)hilight rows in the meantime)
            synchronized (mutex) {
                if (filter.performsFiltering()) {
                    DataTable hilightOnlyTable = new RowFilterTable(table,
                            new RowHiliteFilter(filter, hdl));
                    table = hilightOnlyTable;
                }
                try {
                    FileWriterSettings settings = new FileWriterSettings();
                    settings.setWriteColumnHeader(true);
                    settings.setWriteRowID(true);
                    settings.setColSeparator(",");
                    settings.setSeparatorReplacement("");
                    settings.setReplaceSeparatorInStrings(true);
                    settings.setMissValuePattern("");
                    CSVWriter writer = new CSVWriter(new FileWriter(m_file), 
                            settings);
                    String message;
                    try {
                        writer.write(table, m_exec);
                        writer.close();
                        message = "Done.";
                    } catch (CanceledExecutionException ce) {
                        writer.close();
                        m_file.delete();
                        message = "Canceled.";
                    }
                    JOptionPane.showMessageDialog(m_tableView,
                            message, "Write CSV",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(m_tableView,
                            ioe.getMessage(), "Write error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Row filter that filters non-hilited rows - it's the most convenient way
     * to write only the hilited rows.
     * 
     * @author Bernd Wiswedel, University of Konstanz
     */
    private static final class RowHiliteFilter extends RowFilter {

        private final HiLiteHandler m_handler;
        private final TableContentFilter m_filter;

        /**
         * Creates new instance given a hilite handler.
         * @param filter table content filter
         * @param handler the handler to get the hilite info from
         */
        public RowHiliteFilter(final TableContentFilter filter, 
                final HiLiteHandler handler) {
            m_handler = handler;
            m_filter = filter;
        }

        @Override
        public DataTableSpec configure(final DataTableSpec inSpec)
                throws InvalidSettingsException {
            throw new IllegalStateException("Not intended for permanent usage");
        }

        @Override
        public void loadSettingsFrom(final NodeSettingsRO cfg)
                throws InvalidSettingsException {
            throw new IllegalStateException("Not intended for permanent usage");
        }

        @Override
        protected void saveSettings(final NodeSettingsWO cfg) {
            throw new IllegalStateException("Not intended for permanent usage");
        }

        @Override
        public boolean matches(final DataRow row, final int rowIndex)
                throws EndOfTableException, IncludeFromNowOn {
            return m_filter.matches(m_handler.isHiLit(row.getKey()));
        }
    }
}
