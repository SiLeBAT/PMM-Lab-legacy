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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.PmmConstants;
import de.bund.bfr.knime.pmm.common.XLSReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.IntTextField;
import de.bund.bfr.knime.pmm.common.ui.StringTextField;
import de.bund.bfr.knime.pmm.common.ui.TextListener;

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

	private static final int ROW_COUNT = 1000;
	private static final int DEFAULT_TIMESTEPNUMBER = 10;
	private static final double DEFAULT_TIMESTEPSIZE = 1.0;

	private JPanel panel;
	private JButton clearButton;
	private JButton stepsButton;
	private JButton xlsButton;
	private JTable table;
	private StringTextField agentField;
	private StringTextField matrixField;
	private StringTextField commentField;
	private DoubleTextField temperatureField;
	private DoubleTextField phField;
	private DoubleTextField waterActivityField;
	private JComboBox timeBox;
	private JComboBox logcBox;
	private JComboBox tempBox;

	/**
	 * New pane for configuring the TimeSeriesCreator node.
	 */
	protected TimeSeriesCreatorNodeDialog() {
		JPanel upperPanel = new JPanel();
		JPanel upperPanel1 = new JPanel();
		JPanel leftPanel1 = new JPanel();
		JPanel rightPanel1 = new JPanel();
		JPanel upperPanel2 = new JPanel();
		JPanel leftPanel2 = new JPanel();
		JPanel rightPanel2 = new JPanel();
		JPanel bottomPanel = new JPanel();

		panel = new JPanel();
		xlsButton = new JButton("Read from XLS file");
		xlsButton.addActionListener(this);
		stepsButton = new JButton("Set equidistant time steps");
		stepsButton.addActionListener(this);
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
		timeBox = new JComboBox(AttributeUtilities.getUnitsForAttribute(
				TimeSeriesSchema.ATT_TIME).toArray());
		logcBox = new JComboBox(AttributeUtilities.getUnitsForAttribute(
				TimeSeriesSchema.ATT_LOGC).toArray());
		tempBox = new JComboBox(AttributeUtilities.getUnitsForAttribute(
				TimeSeriesSchema.ATT_TEMPERATURE).toArray());

		leftPanel1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		leftPanel1.setLayout(new GridLayout(6, 1, 5, 5));
		leftPanel1.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_AGENTDETAIL) + ":"));
		leftPanel1.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_MATRIXDETAIL) + ":"));
		leftPanel1.add(new JLabel(TimeSeriesSchema.ATT_COMMENT + ":"));
		leftPanel1.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_TEMPERATURE) + ":"));
		leftPanel1.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_PH) + ":"));
		leftPanel1.add(new JLabel(AttributeUtilities
				.getFullName(TimeSeriesSchema.ATT_WATERACTIVITY) + ":"));

		rightPanel1.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightPanel1.setLayout(new GridLayout(6, 1, 5, 5));
		rightPanel1.add(agentField);
		rightPanel1.add(matrixField);
		rightPanel1.add(commentField);
		rightPanel1.add(temperatureField);
		rightPanel1.add(phField);
		rightPanel1.add(waterActivityField);

		leftPanel2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		leftPanel2.setLayout(new GridLayout(6, 1, 5, 5));
		leftPanel2.add(new JLabel("Unit for "
				+ AttributeUtilities.getFullName(TimeSeriesSchema.ATT_TIME)
				+ ":"));
		leftPanel2.add(new JLabel("Unit for "
				+ AttributeUtilities.getFullName(TimeSeriesSchema.ATT_LOGC)
				+ ":"));
		leftPanel2.add(new JLabel("Unit for "
				+ AttributeUtilities
						.getFullName(TimeSeriesSchema.ATT_TEMPERATURE) + ":"));

		rightPanel2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		rightPanel2.setLayout(new GridLayout(6, 1, 5, 5));
		rightPanel2.add(timeBox);
		rightPanel2.add(logcBox);
		rightPanel2.add(tempBox);

		upperPanel1.setLayout(new BorderLayout());
		upperPanel1.add(leftPanel1, BorderLayout.WEST);
		upperPanel1.add(rightPanel1, BorderLayout.CENTER);

		upperPanel2.setLayout(new BorderLayout());
		upperPanel2.add(leftPanel2, BorderLayout.WEST);
		upperPanel2.add(rightPanel2, BorderLayout.CENTER);

		upperPanel.setLayout(new BorderLayout());
		upperPanel.add(upperPanel1, BorderLayout.CENTER);
		upperPanel.add(upperPanel2, BorderLayout.EAST);

		bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(xlsButton);
		bottomPanel.add(stepsButton);
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
				if (!Double.isNaN(timeArray[i])) {
					table.setValueAt(timeArray[i], i, 0);
				}
			}
		} catch (InvalidSettingsException e) {
		} catch (NullPointerException e) {
		}

		try {
			double[] logcArray = settings
					.getDoubleArray(TimeSeriesCreatorNodeModel.CFGKEY_LOGCARRAY);

			for (int i = 0; i < logcArray.length; i++) {
				if (!Double.isNaN(logcArray[i])) {
					table.setValueAt(logcArray[i], i, 1);
				}
			}
		} catch (InvalidSettingsException e) {
		} catch (NullPointerException e) {
		}

		try {
			timeBox.setSelectedItem(settings
					.getString(TimeSeriesCreatorNodeModel.CFGKEY_TIMEUNIT));
		} catch (InvalidSettingsException e) {
			timeBox.setSelectedItem(AttributeUtilities
					.getStandardUnit(TimeSeriesSchema.ATT_TIME));
		}

		try {
			logcBox.setSelectedItem(settings
					.getString(TimeSeriesCreatorNodeModel.CFGKEY_LOGCUNIT));
		} catch (InvalidSettingsException e) {
			logcBox.setSelectedItem(AttributeUtilities
					.getStandardUnit(TimeSeriesSchema.ATT_LOGC));
		}

		try {
			tempBox.setSelectedItem(settings
					.getString(TimeSeriesCreatorNodeModel.CFGKEY_TEMPUNIT));
		} catch (InvalidSettingsException e) {
			tempBox.setSelectedItem(AttributeUtilities
					.getStandardUnit(TimeSeriesSchema.ATT_TEMPERATURE));
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

			if (time != null || logc != null) {
				if (time != null) {
					timeList.add(time);
				} else {
					timeList.add(Double.NaN);
				}

				if (logc != null) {
					logcList.add(logc);
				} else {
					logcList.add(Double.NaN);
				}
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
		settings.addString(TimeSeriesCreatorNodeModel.CFGKEY_TIMEUNIT,
				(String) timeBox.getSelectedItem());
		settings.addString(TimeSeriesCreatorNodeModel.CFGKEY_LOGCUNIT,
				(String) logcBox.getSelectedItem());
		settings.addString(TimeSeriesCreatorNodeModel.CFGKEY_TEMPUNIT,
				(String) tempBox.getSelectedItem());
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
		} else if (event.getSource() == stepsButton) {
			TimeStepDialog dialog = new TimeStepDialog(panel);

			dialog.setVisible(true);

			if (dialog.isApproved()) {
				int stepNumber = dialog.getNumberOfSteps();
				double stepSize = dialog.getStepSize();

				for (int i = 0; i < ROW_COUNT; i++) {
					Double time = null;
					Double logc = null;

					if (i < stepNumber) {
						time = i * stepSize;
					}

					table.setValueAt(time, i, 0);
					table.setValueAt(logc, i, 1);
				}

				table.repaint();
			}
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
				Map<String, KnimeTuple> tuples = XLSReader
						.getTuples(fileChooser.getSelectedFile());
				Object[] values = tuples.keySet().toArray();
				Object selection = JOptionPane.showInputDialog(panel,
						"Select Time Series", "Input",
						JOptionPane.QUESTION_MESSAGE, null, values, values[0]);
				KnimeTuple tuple = tuples.get(selection);

				agentField.setValue(tuple
						.getString(TimeSeriesSchema.ATT_AGENTDETAIL));
				matrixField.setValue(tuple
						.getString(TimeSeriesSchema.ATT_MATRIXDETAIL));
				commentField.setValue(tuple
						.getString(TimeSeriesSchema.ATT_COMMENT));
				temperatureField.setValue(tuple
						.getDouble(TimeSeriesSchema.ATT_TEMPERATURE));
				phField.setValue(tuple.getDouble(TimeSeriesSchema.ATT_PH));
				waterActivityField.setValue(tuple
						.getDouble(TimeSeriesSchema.ATT_WATERACTIVITY));

				List<Double> timeList = tuple
						.getDoubleList(TimeSeriesSchema.ATT_TIME);
				List<Double> logcList = tuple
						.getDoubleList(TimeSeriesSchema.ATT_LOGC);

				if (timeList.size() > ROW_COUNT) {
					JOptionPane.showMessageDialog(panel,
							"Number of measured points XLS-file exceeds maximum number of rows ("
									+ ROW_COUNT + ")", "Warning",
							JOptionPane.WARNING_MESSAGE);
				}

				for (int i = 0; i < ROW_COUNT; i++) {
					Double time = null;
					Double logc = null;

					if (i < timeList.size()) {
						time = timeList.get(i);
						logc = logcList.get(i);
					}

					table.setValueAt(time, i, 0);
					table.setValueAt(logc, i, 1);
				}

				table.repaint();
			} catch (Exception e) {
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

	private class TimeStepDialog extends JDialog implements ActionListener,
			TextListener {

		private static final long serialVersionUID = 1L;

		private boolean approved;
		private int numberOfSteps;
		private double stepSize;

		private IntTextField numberField;
		private DoubleTextField sizeField;

		private JButton okButton;
		private JButton cancelButton;

		public TimeStepDialog(Component owner) {
			super(JOptionPane.getFrameForComponent(owner), "Time Steps", true);

			approved = false;
			numberOfSteps = 0;
			stepSize = 0.0;

			numberField = new IntTextField(1, ROW_COUNT);
			numberField.setValue(DEFAULT_TIMESTEPNUMBER);
			numberField.setPreferredSize(new Dimension(150, numberField
					.getPreferredSize().height));
			numberField.addTextListener(this);
			sizeField = new DoubleTextField(0.0, Double.POSITIVE_INFINITY);
			sizeField.setPreferredSize(new Dimension(150, sizeField
					.getPreferredSize().height));
			sizeField.setValue(DEFAULT_TIMESTEPSIZE);
			sizeField.addTextListener(this);
			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			JPanel centerPanel = new JPanel();
			JPanel leftPanel = new JPanel();
			JPanel rightPanel = new JPanel();
			JPanel bottomPanel = new JPanel();

			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			leftPanel.setLayout(new GridLayout(2, 1, 5, 5));
			leftPanel.add(new JLabel("Number of Time Steps:"));
			leftPanel.add(new JLabel("Step Size:"));

			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			rightPanel.setLayout(new GridLayout(2, 1, 5, 5));
			rightPanel.add(numberField);
			rightPanel.add(sizeField);

			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(leftPanel, BorderLayout.WEST);
			centerPanel.add(rightPanel, BorderLayout.CENTER);

			bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			bottomPanel.add(okButton);
			bottomPanel.add(cancelButton);

			setLayout(new BorderLayout());
			add(centerPanel, BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();

			setResizable(false);
			setLocationRelativeTo(owner);
		}

		public boolean isApproved() {
			return approved;
		}

		public int getNumberOfSteps() {
			return numberOfSteps;
		}

		public double getStepSize() {
			return stepSize;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				approved = true;
				numberOfSteps = numberField.getValue();
				stepSize = sizeField.getValue();
				dispose();
			} else if (e.getSource() == cancelButton) {
				dispose();
			}
		}

		@Override
		public void textChanged() {
			if (numberField.isValueValid() && sizeField.isValueValid()) {
				okButton.setEnabled(true);
			} else {
				okButton.setEnabled(false);
			}
		}
	}

}
