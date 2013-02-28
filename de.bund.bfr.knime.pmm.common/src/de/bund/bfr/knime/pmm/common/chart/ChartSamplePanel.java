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
package de.bund.bfr.knime.pmm.common.chart;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.IntTextField;
import de.bund.bfr.knime.pmm.common.ui.TextListener;
import de.bund.bfr.knime.pmm.common.ui.TimeSeriesTable;

public class ChartSamplePanel extends JPanel implements ActionListener,
		CellEditorListener {

	private static final long serialVersionUID = 1L;

	private static final int ROW_COUNT = 100;
	private static final int DEFAULT_TIMESTEPCOUNT = 10;
	private static final double DEFAULT_TIMESTEPSIZE = 10.0;

	private TimeSeriesTable table;
	private JButton clearButton;
	private JButton stepsButton;

	private List<EditListener> listeners;

	public ChartSamplePanel() {
		listeners = new ArrayList<ChartSamplePanel.EditListener>();

		table = new TimeSeriesTable(ROW_COUNT, true, false);
		table.getTimeColumn().getCellEditor().addCellEditorListener(this);
		stepsButton = new JButton("Set equidistant time steps");
		stepsButton.addActionListener(this);
		clearButton = new JButton("Clear");
		clearButton.addActionListener(this);

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(stepsButton);
		buttonPanel.add(clearButton);

		setLayout(new BorderLayout());
		add(new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	public String getTimeColumnName() {
		return table.getTimeColumnName();
	}

	public void setTimeColumnName(String timeColumnName) {
		table.setTimeColumnName(timeColumnName);
	}

	public String getLogcColumnName() {
		return table.getLogcColumnName();
	}

	public void setLogcColumnName(String logcColumnName) {
		table.setLogcColumnName(logcColumnName);
	}

	public void addEditListener(EditListener listener) {
		listeners.add(listener);
	}

	public void removeEditListener(EditListener listener) {
		listeners.remove(listener);
	}

	public List<Double> getTimeValues() {
		List<Double> timeValues = new ArrayList<Double>();

		for (int i = 0; i < ROW_COUNT; i++) {
			timeValues.add(table.getTime(i));
		}

		return timeValues;
	}

	public void setTimeValues(List<Double> timeValues) {
		for (int i = 0; i < ROW_COUNT; i++) {
			if (i >= timeValues.size()) {
				table.setTime(i, null);
			} else {
				table.setTime(i, timeValues.get(i));
			}
		}
	}

	public void setDataPoints(double[][] points) {
		Map<Double, Double> logcValues = new LinkedHashMap<Double, Double>();

		if (points != null && points.length == 2) {
			for (int i = 0; i < points[0].length; i++) {
				if (!Double.isNaN(points[0][i]) && !Double.isNaN(points[1][i])) {
					logcValues.put(points[0][i], points[1][i]);
				}
			}
		}

		for (int i = 0; i < ROW_COUNT; i++) {
			Double logc = logcValues.get(table.getTime(i));

			if (logc != null) {
				table.setLogc(i, logc);
			} else {
				table.setLogc(i, null);
			}
		}

		table.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == clearButton) {
			for (int i = 0; i < ROW_COUNT; i++) {
				table.setTime(i, null);
				table.setLogc(i, null);
			}

			table.repaint();
			fireTimeValuesChanged();
		} else if (e.getSource() == stepsButton) {
			TimeStepDialog dialog = new TimeStepDialog(this);

			dialog.setVisible(true);

			if (dialog.isApproved()) {
				int stepNumber = dialog.getNumberOfSteps();
				double stepSize = dialog.getStepSize();

				for (int i = 0; i < ROW_COUNT; i++) {
					Double time = null;

					if (i < stepNumber) {
						time = i * stepSize;
					}

					table.setTime(i, time);
					table.setLogc(i, null);
				}

				table.repaint();
				fireTimeValuesChanged();
			}
		}
	}

	@Override
	public void editingStopped(ChangeEvent e) {
		fireTimeValuesChanged();
	}

	@Override
	public void editingCanceled(ChangeEvent e) {
	}

	private void fireTimeValuesChanged() {
		for (EditListener listener : listeners) {
			listener.timeValuesChanged();
		}
	}

	public static interface EditListener {

		public void timeValuesChanged();

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
			numberField.setValue(DEFAULT_TIMESTEPCOUNT);
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
		public void textChanged(Object source) {
			if (numberField.isValueValid() && sizeField.isValueValid()) {
				okButton.setEnabled(true);
			} else {
				okButton.setEnabled(false);
			}
		}
	}

}
