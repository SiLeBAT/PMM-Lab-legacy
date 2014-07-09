/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.IntTextField;
import de.bund.bfr.knime.pmm.common.ui.TextListener;
import de.bund.bfr.knime.pmm.common.ui.TimeSeriesTable;
import de.bund.bfr.knime.pmm.common.ui.UI;

public class ChartSamplePanel extends JPanel implements ActionListener,
		CellEditorListener {

	private static final long serialVersionUID = 1L;

	private static final int ROW_COUNT = 100;
	private static final int DEFAULT_TIMESTEPCOUNT = 10;
	private static final double DEFAULT_TIMESTEPSIZE = 10.0;

	private JScrollPane tablePane;
	private TimeSeriesTable table;	
	private TextArea warningArea;
	private JButton clearButton;
	private JButton stepsButton;

	private List<EditListener> listeners;

	public ChartSamplePanel() {
		listeners = new ArrayList<ChartSamplePanel.EditListener>();

		table = new TimeSeriesTable(ROW_COUNT, 1, true, false);
		table.getTimeColumn().getCellEditor().addCellEditorListener(this);
		tablePane = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		warningArea = new TextArea(3, 10);
		warningArea.setEditable(false);
		stepsButton = new JButton("Set equidistant time steps");
		stepsButton.addActionListener(this);
		clearButton = new JButton("Clear");
		clearButton.addActionListener(this);

		JPanel buttonPanel = new JPanel();

		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.add(stepsButton);
		buttonPanel.add(clearButton);

		JPanel buttomPanel = new JPanel();

		buttomPanel.setLayout(new BorderLayout());
		buttomPanel.add(warningArea, BorderLayout.CENTER);
		buttomPanel.add(buttonPanel, BorderLayout.SOUTH);

		setLayout(new BorderLayout());
		add(tablePane, BorderLayout.CENTER);
		add(buttomPanel, BorderLayout.SOUTH);
	}

	public void setWarnings(List<String> warnings) {
		String s = "";

		for (String w : warnings) {
			s += w + "\n";
		}

		if (!s.isEmpty()) {
			s = s.substring(0, s.length() - 1);
		}
		
		warningArea.setText(s);
		
		if (warnings.isEmpty()) {
			warningArea.setBackground(Color.WHITE);
		} else {
			warningArea.setBackground(Color.YELLOW);
		}
	}

	public TimeSeriesTable getTimeSeriesTable() {
		return table;
	}

	public String getTimeColumnName() {
		return table.getTimeColumnName();
	}

	public void setTimeColumnName(String timeColumnName) {
		table.setTimeColumnName(timeColumnName);
	}

	public List<String> getCColumnNames() {
		return table.getCColumnNames();
	}

	public void setCColumnName(int i, String cColumnName) {
		table.setCColumnName(i, cColumnName);
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

	public void setDataPoints(Map<String, double[][]> points) {
		String timeColumnName = getTimeColumnName();
		List<Double> timeValues = getTimeValues();

		Map<String, Map<Double, Double>> cValues = new LinkedHashMap<String, Map<Double, Double>>();

		for (String id : points.keySet()) {
			Map<Double, Double> values = new LinkedHashMap<Double, Double>();
			double[][] ps = points.get(id);

			if (ps != null && ps.length == 2) {
				for (int i = 0; i < ps[0].length; i++) {
					if (!Double.isNaN(ps[0][i]) && !Double.isNaN(ps[1][i])) {
						values.put(ps[0][i], ps[1][i]);
					}
				}
			}

			cValues.put(id, values);
		}

		if (table.getCColumns().size() != cValues.size()) {
			remove(tablePane);
			table = new TimeSeriesTable(ROW_COUNT, cValues.size(), true, false);
			table.getTimeColumn().getCellEditor().addCellEditorListener(this);
			tablePane = new JScrollPane(table,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			add(tablePane, BorderLayout.CENTER);
			revalidate();
			setTimeColumnName(timeColumnName);
			setTimeValues(timeValues);
		}

		int count = 0;

		for (String id : cValues.keySet()) {
			table.setCColumnName(count, id);

			for (int i = 0; i < ROW_COUNT; i++) {
				Double logc = cValues.get(id).get(table.getTime(i));

				if (logc != null) {
					table.setLogc(i, count, logc);
				} else {
					table.setLogc(i, count, null);
				}
			}

			count++;
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

	public void fireTimeValuesChanged() {
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
			super(SwingUtilities.getWindowAncestor(owner), "Time Steps",
					DEFAULT_MODALITY_TYPE);

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
			UI.adjustDialog(this);
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
