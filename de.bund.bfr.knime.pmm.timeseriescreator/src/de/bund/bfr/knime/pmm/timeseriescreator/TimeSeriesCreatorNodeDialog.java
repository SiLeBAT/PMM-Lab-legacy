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
package de.bund.bfr.knime.pmm.timeseriescreator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.PmmConstants;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.StringTextField;

/**
 * <code>NodeDialog</code> for the "TimeSeriesCreator" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class TimeSeriesCreatorNodeDialog extends NodeDialogPane implements
		ActionListener {

	private static final int ROW_COUNT = 100;

	private JPanel panel;
	private JButton clearButton;
	private JButton xlsButton;
	private JTable table;
	private StringTextField agentField;
	private StringTextField matrixField;
	private StringTextField commentField;
	private DoubleTextField temperatureField;
	private DoubleTextField phField;
	private DoubleTextField waterActivityField;

	/**
	 * New pane for configuring the TimeSeriesCreator node.
	 */
	protected TimeSeriesCreatorNodeDialog() {
		JPanel upperPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		JPanel bottomPanel = new JPanel();

		panel = new JPanel();
		xlsButton = new JButton("Read from XLS file");
		xlsButton.addActionListener(this);
		clearButton = new JButton("Clear");
		clearButton.addActionListener(this);
		table = new JTable(new TimeSeriesTableModel());
		table.getColumn(TimeSeriesSchema.ATT_TIME).setCellEditor(
				new DoubleCellEditor());
		table.getColumn(TimeSeriesSchema.ATT_LOGC).setCellEditor(
				new DoubleCellEditor());
		table.getColumn(TimeSeriesSchema.ATT_TIME).setCellRenderer(
				new DefaultTableCellRenderer());
		table.getColumn(TimeSeriesSchema.ATT_LOGC).setCellRenderer(
				new DefaultTableCellRenderer());
		agentField = new StringTextField(true);
		matrixField = new StringTextField(true);
		commentField = new StringTextField(true);
		temperatureField = new DoubleTextField(true);
		phField = new DoubleTextField(PmmConstants.MIN_PH, PmmConstants.MAX_PH,
				true);
		waterActivityField = new DoubleTextField(
				PmmConstants.MIN_WATERACTIVITY, PmmConstants.MAX_WATERACTIVITY,
				true);

		leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		leftPanel.setLayout(new GridLayout(6, 1, 5, 5));
		leftPanel.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_AGENTDETAIL) + ":"));
		leftPanel.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_MATRIXDETAIL) + ":"));
		leftPanel.add(new JLabel(TimeSeriesSchema.ATT_COMMENT + ":"));
		leftPanel.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_TEMPERATURE) + ":"));
		leftPanel.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_PH) + ":"));
		leftPanel.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_WATERACTIVITY) + ":"));

		rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightPanel.setLayout(new GridLayout(6, 1, 5, 5));
		rightPanel.add(agentField);
		rightPanel.add(matrixField);
		rightPanel.add(commentField);
		rightPanel.add(temperatureField);
		rightPanel.add(phField);
		rightPanel.add(waterActivityField);

		upperPanel.setLayout(new BorderLayout());
		upperPanel.add(leftPanel, BorderLayout.WEST);
		upperPanel.add(rightPanel, BorderLayout.CENTER);

		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(xlsButton);
		bottomPanel.add(clearButton);

		panel.setLayout(new BorderLayout());
		panel.add(upperPanel, BorderLayout.NORTH);
		panel.add(new JScrollPane(table), BorderLayout.CENTER);
		panel.add(bottomPanel, BorderLayout.SOUTH);
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(final NodeSettingsRO settings,
			final DataTableSpec[] specs) throws NotConfigurableException {
		try {
			agentField.setValue(settings
					.getString(TimeSeriesCreatorNodeModel.CFGKEY_AGENT));
		} catch (InvalidSettingsException e) {
		}

		try {
			matrixField.setValue(settings
					.getString(TimeSeriesCreatorNodeModel.CFGKEY_MATRIX));
		} catch (InvalidSettingsException e) {
		}

		try {
			commentField.setValue(settings
					.getString(TimeSeriesCreatorNodeModel.CFGKEY_COMMENT));
		} catch (InvalidSettingsException e) {
		}

		try {
			temperatureField.setValue(settings
					.getDouble(TimeSeriesCreatorNodeModel.CFGKEY_TEMPERATURE));
		} catch (InvalidSettingsException e) {
		}

		try {
			phField.setValue(settings
					.getDouble(TimeSeriesCreatorNodeModel.CFGKEY_PH));
		} catch (InvalidSettingsException e) {
		}

		try {
			waterActivityField
					.setValue(settings
							.getDouble(TimeSeriesCreatorNodeModel.CFGKEY_WATERACTIVITY));
		} catch (InvalidSettingsException e) {
		}

		try {
			double[] timeArray = settings
					.getDoubleArray(TimeSeriesCreatorNodeModel.CFGKEY_TIMEARRAY);

			for (int i = 0; i < timeArray.length; i++) {
				table.setValueAt(timeArray[i], i, 0);
			}
		} catch (InvalidSettingsException e) {
		} catch (NullPointerException e) {
		}

		try {
			double[] logcArray = settings
					.getDoubleArray(TimeSeriesCreatorNodeModel.CFGKEY_LOGCARRAY);

			for (int i = 0; i < logcArray.length; i++) {
				table.setValueAt(logcArray[i], i, 1);
			}
		} catch (InvalidSettingsException e) {
		} catch (NullPointerException e) {
		}
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (!temperatureField.isValueValid()) {
			throw new InvalidSettingsException("Invalid Value");
		}

		if (!phField.isValueValid()) {
			throw new InvalidSettingsException("Invalid Value");
		}

		if (!waterActivityField.isValueValid()) {
			throw new InvalidSettingsException("Invalid Value");
		}

		if (agentField.getValue() != null) {
			settings.addString(TimeSeriesCreatorNodeModel.CFGKEY_AGENT,
					agentField.getValue());
		}

		if (matrixField.getValue() != null) {
			settings.addString(TimeSeriesCreatorNodeModel.CFGKEY_MATRIX,
					matrixField.getValue());
		}

		if (commentField.getValue() != null) {
			settings.addString(TimeSeriesCreatorNodeModel.CFGKEY_COMMENT,
					commentField.getValue());
		}

		if (temperatureField.getValue() != null) {
			settings.addDouble(TimeSeriesCreatorNodeModel.CFGKEY_TEMPERATURE,
					temperatureField.getValue());
		}

		if (phField.getValue() != null) {
			settings.addDouble(TimeSeriesCreatorNodeModel.CFGKEY_PH,
					phField.getValue());
		}

		if (waterActivityField.getValue() != null) {
			settings.addDouble(TimeSeriesCreatorNodeModel.CFGKEY_WATERACTIVITY,
					waterActivityField.getValue());
		}

		List<Double> timeList = new ArrayList<Double>();
		List<Double> logcList = new ArrayList<Double>();

		for (int i = 0; i < ROW_COUNT; i++) {
			Double time = (Double) table.getValueAt(i, 0);
			Double logc = (Double) table.getValueAt(i, 1);

			if (time != null && logc != null) {
				timeList.add(time);
				logcList.add(logc);
			}
		}

		double[] timeArray = new double[timeList.size()];
		double[] logcArray = new double[timeList.size()];

		for (int i = 0; i < timeList.size(); i++) {
			timeArray[i] = timeList.get(i);
			logcArray[i] = logcList.get(i);
		}

		settings.addDoubleArray(TimeSeriesCreatorNodeModel.CFGKEY_TIMEARRAY,
				timeArray);
		settings.addDoubleArray(TimeSeriesCreatorNodeModel.CFGKEY_LOGCARRAY,
				logcArray);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == xlsButton) {
			loadFromXLS();
		} else if (event.getSource() == clearButton) {
			agentField.setValue(null);
			matrixField.setValue(null);
			commentField.setValue(null);
			temperatureField.setValue(null);
			phField.setValue(null);
			waterActivityField.setValue(null);

			for (int i = 0; i < ROW_COUNT; i++) {
				table.setValueAt(null, i, 0);
				table.setValueAt(null, i, 1);
			}

			table.repaint();
		}
	}

	private void loadFromXLS() {
		JFileChooser fileChooser = new JFileChooser();
		FileFilter xlsFilter = new FileFilter() {

			@Override
			public String getDescription() {
				return "Excel Spreadsheat (*.xls)";
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory()
						|| f.getName().toLowerCase().endsWith(".xls");
			}
		};

		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(xlsFilter);

		if (fileChooser.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
			try {
				InputStream inputStream = new FileInputStream(
						fileChooser.getSelectedFile());
				Workbook wb = WorkbookFactory.create(inputStream);
				Sheet sheet = wb.getSheetAt(0);

				if (sheet.getRow(1).getCell(0) != null) {
					agentField.setText(sheet.getRow(1).getCell(0).toString());
				} else {
					agentField.setValue(null);
				}

				if (sheet.getRow(1).getCell(1) != null) {
					matrixField.setText(sheet.getRow(1).getCell(1).toString());
				} else {
					matrixField.setValue(null);
				}

				if (sheet.getRow(1).getCell(2) != null) {
					commentField.setText(sheet.getRow(1).getCell(2).toString());
				} else {
					commentField.setValue(null);
				}

				if (sheet.getRow(1).getCell(3) != null) {
					temperatureField.setText(sheet.getRow(1).getCell(3)
							.toString());
				} else {
					temperatureField.setValue(null);
				}

				if (sheet.getRow(1).getCell(4) != null) {
					phField.setText(sheet.getRow(1).getCell(4).toString());
				} else {
					phField.setValue(null);
				}

				if (sheet.getRow(1).getCell(5) != null) {
					waterActivityField.setText(sheet.getRow(1).getCell(5)
							.toString());
				} else {
					waterActivityField.setValue(null);
				}

				for (int i = 0; i < ROW_COUNT; i++) {
					Double time;
					Double logc;

					try {
						time = Double.parseDouble(sheet.getRow(i + 1)
								.getCell(6).toString());
					} catch (Exception e) {
						time = null;
					}

					try {
						logc = Double.parseDouble(sheet.getRow(i + 1)
								.getCell(7).toString());
					} catch (Exception e) {
						logc = null;
					}

					table.setValueAt(time, i, 0);
					table.setValueAt(logc, i, 1);
				}

				table.repaint();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			}
		}
	}

	private class TimeSeriesTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private List<Double> timeList;
		private List<Double> logcList;

		public TimeSeriesTableModel() {
			timeList = new ArrayList<Double>(ROW_COUNT);
			logcList = new ArrayList<Double>(ROW_COUNT);

			for (int i = 0; i < ROW_COUNT; i++) {
				timeList.add(null);
				logcList.add(null);
			}
		}

		@Override
		public int getRowCount() {
			return ROW_COUNT;
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(int column) {
			switch (column) {
			case 0:
				return TimeSeriesSchema.ATT_TIME;
			case 1:
				return TimeSeriesSchema.ATT_LOGC;
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

	private class DoubleCellEditor extends AbstractCellEditor implements
			TableCellEditor {

		private static final long serialVersionUID = 1L;

		private DoubleTextField field;

		public DoubleCellEditor() {
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
	}

}
