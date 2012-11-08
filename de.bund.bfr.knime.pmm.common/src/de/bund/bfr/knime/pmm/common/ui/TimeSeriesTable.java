package de.bund.bfr.knime.pmm.common.ui;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class TimeSeriesTable extends JTable implements ActionListener {

	private static final long serialVersionUID = 1L;

	public TimeSeriesTable(List<Point2D.Double> timeSeries, boolean editable) {
		this(timeSeries.size(), editable);

		for (int i = 0; i < timeSeries.size(); i++) {
			setTime(i, timeSeries.get(i).x);
			setLogc(i, timeSeries.get(i).y);
		}
	}

	public TimeSeriesTable(int rowCount, boolean editable) {
		setModel(new TimeSeriesTableModel(rowCount));
		getColumn(AttributeUtilities.getFullName(TimeSeriesSchema.TIME))
				.setCellEditor(new DoubleCellEditor(editable));
		getColumn(AttributeUtilities.getFullName(TimeSeriesSchema.LOGC))
				.setCellEditor(new DoubleCellEditor(editable));
		getColumn(AttributeUtilities.getFullName(TimeSeriesSchema.TIME))
				.setCellRenderer(new DoubleCellRenderer());
		getColumn(AttributeUtilities.getFullName(TimeSeriesSchema.LOGC))
				.setCellRenderer(new DoubleCellRenderer());
		setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		setCellSelectionEnabled(true);
		registerKeyboardAction(this, "Copy", KeyStroke.getKeyStroke(
				KeyEvent.VK_C, ActionEvent.CTRL_MASK, false),
				JComponent.WHEN_FOCUSED);
		registerKeyboardAction(this, "Paste", KeyStroke.getKeyStroke(
				KeyEvent.VK_V, ActionEvent.CTRL_MASK, false),
				JComponent.WHEN_FOCUSED);
	}

	public Double getTime(int i) {
		return (Double) getValueAt(i, 0);
	}

	public void setTime(int i, Double time) {
		setValueAt(time, i, 0);
	}

	public Double getLogc(int i) {
		return (Double) getValueAt(i, 1);
	}

	public void setLogc(int i, Double logc) {
		setValueAt(logc, i, 1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Copy")) {
			performCopy();
		} else if (e.getActionCommand().equals("Paste")) {
			performPaste();
		}
	}

	private void performCopy() {
		StringBuilder sbf = new StringBuilder();
		int numcols = getSelectedColumnCount();
		int numrows = getSelectedRowCount();
		int[] rowsselected = getSelectedRows();
		int[] colsselected = getSelectedColumns();

		for (int i = 0; i < numrows; i++) {
			for (int j = 0; j < numcols; j++) {
				sbf.append(getValueAt(rowsselected[i], colsselected[j]));

				if (j < numcols - 1) {
					sbf.append("\t");
				}
			}
			sbf.append("\n");
		}

		StringSelection stsel = new StringSelection(sbf.toString());

		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(stsel, stsel);
	}

	private void performPaste() {
		int startRow = getSelectedRows()[0];
		int startCol = getSelectedColumns()[0];
		Clipboard system = Toolkit.getDefaultToolkit().getSystemClipboard();
		String trstring = null;

		try {
			trstring = (String) system.getContents(this).getTransferData(
					DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] rows = trstring.split("\n");

		for (int i = 0; i < rows.length; i++) {
			String[] cells = rows[i].split("\t");

			for (int j = 0; j < cells.length; j++) {
				if (startRow + i < getRowCount()
						&& startCol + j < getColumnCount()) {
					try {
						setValueAt(
								Double.parseDouble(cells[j].replace(",", ".")),
								startRow + i, startCol + j);
					} catch (NumberFormatException e) {
					}
				}
			}
		}

		repaint();
	}

	private class TimeSeriesTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private int rowCount;

		private List<Double> timeList;
		private List<Double> logcList;

		public TimeSeriesTableModel(int rowCount) {
			this.rowCount = rowCount;
			timeList = new ArrayList<Double>(rowCount);
			logcList = new ArrayList<Double>(rowCount);

			for (int i = 0; i < rowCount; i++) {
				timeList.add(null);
				logcList.add(null);
			}
		}

		@Override
		public int getRowCount() {
			return rowCount;
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(int column) {
			switch (column) {
			case 0:
				return AttributeUtilities
						.getFullName(TimeSeriesSchema.TIME);
			case 1:
				return AttributeUtilities
						.getFullName(TimeSeriesSchema.LOGC);
			default:
				return null;
			}
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return Double.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return timeList.get(rowIndex);
			case 1:
				return logcList.get(rowIndex);
			default:
				return null;
			}
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				timeList.set(rowIndex, (Double) aValue);
				break;
			case 1:
				logcList.set(rowIndex, (Double) aValue);
				break;
			}
		}

	}

	private class DoubleCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		@Override
		protected void setValue(Object value) {
			if (value != null) {
				NumberFormat format = new DecimalFormat("0.####");

				setText(format.format((Double) value));
			} else {
				super.setValue(value);
			}
		}

	}

	private class DoubleCellEditor extends AbstractCellEditor implements
			TableCellEditor {

		private static final long serialVersionUID = 1L;

		private boolean editable;

		private DoubleTextField field;

		public DoubleCellEditor(boolean editable) {
			this.editable = editable;
			field = new DoubleTextField(true);
		}

		@Override
		public Object getCellEditorValue() {
			return field.getValue();
		}

		@Override
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			if (value != null) {
				field.setText(value.toString());
			} else {
				field.setText("");
			}

			return field;
		}

		@Override
		public boolean isCellEditable(EventObject e) {
			if (!editable) {
				return false;
			}

			if (e instanceof MouseEvent) {
				return ((MouseEvent) e).getClickCount() >= 2;
			}

			return true;
		}
	}

}
