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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.FormattedDoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.SpacePanel;
import de.bund.bfr.knime.pmm.common.ui.TextListener;

public class ChartConfigPanel extends JPanel implements ActionListener,
		TextListener {

	public static final int NO_PARAMETER_INPUT = 1;
	public static final int PARAMETER_FIELDS = 2;
	public static final int PARAMETER_BOXES = 3;

	private static final long serialVersionUID = 1L;

	private static final double DEFAULT_MINX = 0.0;
	private static final double DEFAULT_MAXX = 1.0;
	private static final double DEFAULT_MINY = 0.0;
	private static final double DEFAULT_MAXY = 1.0;

	private List<ConfigListener> listeners;

	private JCheckBox drawLinesBox;
	private JCheckBox showLegendBox;
	private JCheckBox addInfoInLegendBox;
	private JCheckBox displayFocusedRowBox;
	private JCheckBox showConfidenceBox;

	private JCheckBox manualRangeBox;
	private DoubleTextField minXField;
	private DoubleTextField minYField;
	private DoubleTextField maxXField;
	private DoubleTextField maxYField;

	private JComboBox xBox;
	private JComboBox yBox;
	private JComboBox yTransBox;
	private List<String> parameters;
	private List<List<Double>> possibleValues;
	private List<List<Boolean>> selectedValues;
	private Map<String, Double> minParamValues;
	private Map<String, Double> maxParamValues;

	private JPanel parameterValuesPanel;
	private List<JButton> parameterButtons;
	private List<DoubleTextField> parameterFields;

	private int type;

	private String lastParamX;

	public ChartConfigPanel(int type, boolean allowConfidenceInterval) {
		this.type = type;
		listeners = new ArrayList<ConfigListener>();
		setLayout(new GridLayout(3, 1));

		JPanel displayOptionsPanel = new JPanel();

		drawLinesBox = new JCheckBox("Draw Lines");
		drawLinesBox.setSelected(false);
		drawLinesBox.addActionListener(this);
		showLegendBox = new JCheckBox("Show Legend");
		showLegendBox.setSelected(true);
		showLegendBox.addActionListener(this);
		addInfoInLegendBox = new JCheckBox("Add Info in Lengend");
		addInfoInLegendBox.setSelected(false);
		addInfoInLegendBox.addActionListener(this);
		displayFocusedRowBox = new JCheckBox("Display Highlighted Row");
		displayFocusedRowBox.setSelected(false);
		displayFocusedRowBox.addActionListener(this);

		displayOptionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		displayOptionsPanel.setBorder(BorderFactory
				.createTitledBorder("Display Options"));
		displayOptionsPanel.add(drawLinesBox);
		displayOptionsPanel.add(showLegendBox);
		displayOptionsPanel.add(addInfoInLegendBox);
		displayOptionsPanel.add(displayFocusedRowBox);

		if (allowConfidenceInterval) {
			showConfidenceBox = new JCheckBox("Show Confidence Interval");
			showConfidenceBox.setSelected(false);
			showConfidenceBox.addActionListener(this);
			displayOptionsPanel.add(showConfidenceBox);
		}

		add(new SpacePanel(displayOptionsPanel));

		JPanel rangePanel = new JPanel();

		manualRangeBox = new JCheckBox("Set Manual Range");
		manualRangeBox.setSelected(false);
		manualRangeBox.addActionListener(this);
		minXField = new DoubleTextField();
		minXField.setValue(DEFAULT_MINX);
		minXField.setPreferredSize(new Dimension(50, minXField
				.getPreferredSize().height));
		minXField.setEnabled(false);
		minXField.addTextListener(this);
		minYField = new DoubleTextField();
		minYField.setValue(DEFAULT_MINY);
		minYField.setPreferredSize(new Dimension(50, minYField
				.getPreferredSize().height));
		minYField.setEnabled(false);
		minYField.addTextListener(this);
		maxXField = new DoubleTextField();
		maxXField.setValue(DEFAULT_MAXX);
		maxXField.setPreferredSize(new Dimension(50, maxXField
				.getPreferredSize().height));
		maxXField.setEnabled(false);
		maxXField.addTextListener(this);
		maxYField = new DoubleTextField();
		maxYField.setValue(DEFAULT_MAXY);
		maxYField.setPreferredSize(new Dimension(50, maxYField
				.getPreferredSize().height));
		maxYField.setEnabled(false);
		maxYField.addTextListener(this);

		rangePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		rangePanel.setBorder(BorderFactory.createTitledBorder("Range"));
		rangePanel.add(manualRangeBox);
		rangePanel.add(new JLabel("Min X:"));
		rangePanel.add(minXField);
		rangePanel.add(new JLabel("Max X:"));
		rangePanel.add(maxXField);
		rangePanel.add(new JLabel("Min Y:"));
		rangePanel.add(minYField);
		rangePanel.add(new JLabel("Max Y:"));
		rangePanel.add(maxYField);
		add(new SpacePanel(rangePanel));

		JPanel parametersPanel = new JPanel();

		xBox = new JComboBox();
		xBox.addActionListener(this);
		yBox = new JComboBox();
		yBox.addActionListener(this);
		yTransBox = new JComboBox(ChartConstants.TRANSFORMS);
		yTransBox.addActionListener(this);

		parameterValuesPanel = new JPanel();
		parameterFields = new ArrayList<DoubleTextField>();
		parameterButtons = new ArrayList<JButton>();

		parametersPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		parametersPanel.setBorder(BorderFactory
				.createTitledBorder("Parameters"));
		parametersPanel.add(new JLabel("X:"));
		parametersPanel.add(xBox);
		parametersPanel.add(new JLabel("Y:"));
		parametersPanel.add(yBox);
		parametersPanel.add(new JLabel("Y Transform:"));
		parametersPanel.add(yTransBox);
		parametersPanel.add(parameterValuesPanel);
		add(new SpacePanel(parametersPanel));

		lastParamX = null;
	}

	public void addConfigListener(ConfigListener listener) {
		listeners.add(listener);
	}

	public void removeConfigListener(ConfigListener listener) {
		listeners.remove(listener);
	}

	public boolean isUseManualRange() {
		return manualRangeBox.isSelected();
	}

	public void setUseManualRange(boolean manualRange) {
		manualRangeBox.setSelected(manualRange);

		if (manualRangeBox.isSelected()) {
			minXField.setEnabled(true);
			minYField.setEnabled(true);
			maxXField.setEnabled(true);
			maxYField.setEnabled(true);
		} else {
			minXField.setEnabled(false);
			minYField.setEnabled(false);
			maxXField.setEnabled(false);
			maxYField.setEnabled(false);
		}
	}

	public double getMinX() {
		if (minXField.isValueValid()) {
			return minXField.getValue();
		} else {
			return DEFAULT_MINX;
		}
	}

	public void setMinX(double minX) {
		minXField.setValue(minX);
	}

	public double getMinY() {
		if (minYField.isValueValid()) {
			return minYField.getValue();
		} else {
			return DEFAULT_MINY;
		}
	}

	public void setMinY(double minY) {
		minYField.setValue(minY);
	}

	public double getMaxX() {
		if (maxXField.isValueValid()) {
			return maxXField.getValue();
		} else {
			return DEFAULT_MAXX;
		}
	}

	public void setMaxX(double maxX) {
		maxXField.setValue(maxX);
	}

	public double getMaxY() {
		if (maxYField.isValueValid()) {
			return maxYField.getValue();
		} else {
			return DEFAULT_MAXY;
		}
	}

	public void setMaxY(double maxY) {
		maxYField.setValue(maxY);
	}

	public boolean isDrawLines() {
		return drawLinesBox.isSelected();
	}

	public void setDrawLines(boolean drawLines) {
		drawLinesBox.setSelected(drawLines);
	}

	public boolean isShowLegend() {
		return showLegendBox.isSelected();
	}

	public void setShowLegend(boolean showLegend) {
		showLegendBox.setSelected(showLegend);

		if (showLegendBox.isSelected()) {
			addInfoInLegendBox.setEnabled(true);
		} else {
			addInfoInLegendBox.setEnabled(false);
		}
	}

	public boolean isAddInfoInLegend() {
		return addInfoInLegendBox.isSelected();
	}

	public void setAddInfoInLegend(boolean addInfoInLegend) {
		addInfoInLegendBox.setSelected(addInfoInLegend);
	}

	public boolean isDisplayFocusedRow() {
		return displayFocusedRowBox.isSelected();
	}

	public void setDisplayFocusedRow(boolean displayFocusedRow) {
		displayFocusedRowBox.setSelected(displayFocusedRow);
	}

	public boolean isShowConfidenceInterval() {
		return showConfidenceBox.isSelected();
	}

	public void setShowConfidenceInterval(boolean showConfidenceInterval) {
		showConfidenceBox.setSelected(showConfidenceInterval);
	}

	public String getParamX() {
		return (String) xBox.getSelectedItem();
	}

	public String getParamY() {
		return (String) yBox.getSelectedItem();
	}

	public String getTransformY() {
		return (String) yTransBox.getSelectedItem();
	}

	public void setTransformY(String transformY) {
		yTransBox.setSelectedItem(transformY);
	}

	public Map<String, List<Double>> getParamsXValues() {
		Map<String, List<Double>> valueLists = new LinkedHashMap<String, List<Double>>();

		if (type == PARAMETER_FIELDS) {
			for (int i = 0; i < parameterFields.size(); i++) {
				if (parameterFields.get(i) instanceof DoubleTextField) {
					DoubleTextField field = (DoubleTextField) parameterFields
							.get(i);
					String paramName = parameterButtons.get(i).getText()
							.replace(":", "");

					if (field.getValue() != null) {
						valueLists.put(
								paramName,
								new ArrayList<Double>(Arrays.asList(field
										.getValue())));
					} else {
						valueLists.put(paramName,
								new ArrayList<Double>(Arrays.asList(0.0)));
					}
				}
			}
		} else if (type == PARAMETER_BOXES) {
			for (int i = 0; i < parameters.size(); i++) {
				List<Double> values = possibleValues.get(i);
				List<Boolean> selected = selectedValues.get(i);
				List<Double> newValues = new ArrayList<Double>();

				for (int j = 0; j < values.size(); j++) {
					if (selected.get(j)) {
						newValues.add(values.get(j));
					}
				}

				valueLists.put(parameters.get(i), newValues);
			}
		}

		valueLists.put((String) xBox.getSelectedItem(), new ArrayList<Double>(
				Arrays.asList(0.0)));

		return valueLists;
	}

	public void setParamsX(List<String> parameters) {
		setParamsX(parameters, null);
	}

	public void setParamsX(List<String> parameters,
			List<List<Double>> possibleValues) {
		setParamsX(parameters, possibleValues, null, null, null);
	}

	public void setParamsX(List<String> parameters,
			List<List<Double>> possibleValues,
			Map<String, Double> minParamValues,
			Map<String, Double> maxParamValues, String paramX) {
		boolean parametersChanged = false;

		if (parameters == null) {
			parameters = new ArrayList<String>();
		}

		if (possibleValues == null) {
			possibleValues = new ArrayList<List<Double>>();
		}

		if (minParamValues == null) {
			minParamValues = new LinkedHashMap<String, Double>();
		}

		if (maxParamValues == null) {
			maxParamValues = new LinkedHashMap<String, Double>();
		}

		if (this.parameters == null || this.possibleValues == null
				|| parameters.size() != this.parameters.size()
				|| possibleValues.size() != this.possibleValues.size()) {
			parametersChanged = true;
		} else {
			for (int i = 0; i < parameters.size(); i++) {
				String param = parameters.get(i);
				List<Double> values = possibleValues.get(i);

				if (!this.parameters.contains(param)) {
					parametersChanged = true;
					break;
				} else {
					List<Double> oldValues = this.possibleValues
							.get(this.parameters.indexOf(param));

					if ((values == null && oldValues != null)
							|| (values != null && !values.equals(oldValues))) {
						parametersChanged = true;
						break;
					}
				}
			}
		}

		this.parameters = parameters;
		this.possibleValues = possibleValues;
		this.minParamValues = minParamValues;
		this.maxParamValues = maxParamValues;

		if (parametersChanged) {
			xBox.removeActionListener(this);
			xBox.removeAllItems();

			if (paramX != null) {
				xBox.addItem(paramX);
			} else {
				for (String param : parameters) {
					xBox.addItem(param);
				}
			}

			if (!parameters.isEmpty()) {
				if (parameters.contains(lastParamX)) {
					xBox.setSelectedItem(lastParamX);
				} else if (parameters.contains(TimeSeriesSchema.TIME)) {
					xBox.setSelectedItem(TimeSeriesSchema.TIME);
				} else {
					xBox.setSelectedIndex(0);
				}

				lastParamX = (String) xBox.getSelectedItem();
			}

			xBox.addActionListener(this);

			if (possibleValues != null) {
				selectedValues = new ArrayList<List<Boolean>>();

				for (List<Double> values : possibleValues) {
					if (values != null) {
						List<Boolean> selected = new ArrayList<Boolean>(
								Collections.nCopies(values.size(), false));

						selected.set(0, true);
						selectedValues.add(selected);
					} else {
						selectedValues.add(null);
					}
				}
			}

			updateParametersPanel();
		}
	}

	public void setParamsY(List<String> paramsY) {
		yBox.removeActionListener(this);
		yBox.removeAllItems();

		if (paramsY == null) {
			paramsY = new ArrayList<String>();
		}

		for (String param : paramsY) {
			yBox.addItem(param);
		}

		if (!paramsY.isEmpty()) {
			if (yBox.getItemAt(0).equals(xBox.getSelectedItem())
					&& yBox.getItemCount() >= 2) {
				yBox.setSelectedIndex(1);
			} else {
				yBox.setSelectedIndex(0);
			}
		}

		yBox.addActionListener(this);
	}

	private void updateParametersPanel() {
		if (type == NO_PARAMETER_INPUT) {
			return;
		}

		for (DoubleTextField input : parameterFields) {
			parameterValuesPanel.remove(input);
		}

		for (JButton button : parameterButtons) {
			parameterValuesPanel.remove(button);
		}

		parameterFields.clear();
		parameterButtons.clear();

		for (int i = 0; i < parameters.size(); i++) {
			if (parameters.get(i).equals(xBox.getSelectedItem())) {
				continue;
			}

			if (type == PARAMETER_FIELDS) {
				JButton selectButton = new JButton(parameters.get(i) + ":");
				DoubleTextField input = new DoubleTextField();
				double value = 0.0;

				if (possibleValues != null && possibleValues.get(i) != null) {
					value = possibleValues.get(i).get(0);
				}

				selectButton.addActionListener(this);
				input.setPreferredSize(new Dimension(50, input
						.getPreferredSize().height));
				input.setValue(value);
				input.addTextListener(this);
				parameterButtons.add(selectButton);
				parameterValuesPanel.add(selectButton);
				parameterFields.add(input);
				parameterValuesPanel.add(input);
			} else if (type == PARAMETER_BOXES) {
				JButton selectButton = new JButton(parameters.get(i)
						+ " Values");

				selectButton.addActionListener(this);
				parameterButtons.add(selectButton);
				parameterValuesPanel.add(selectButton);
			}
		}

		parameterValuesPanel.updateUI();
		revalidate();
	}

	private void fireConfigChanged() {
		for (ConfigListener listener : listeners) {
			listener.configChanged();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == manualRangeBox) {
			if (manualRangeBox.isSelected()) {
				minXField.setEnabled(true);
				minYField.setEnabled(true);
				maxXField.setEnabled(true);
				maxYField.setEnabled(true);
			} else {
				minXField.setEnabled(false);
				minYField.setEnabled(false);
				maxXField.setEnabled(false);
				maxYField.setEnabled(false);
			}
		} else if (e.getSource() == showLegendBox) {
			if (showLegendBox.isSelected()) {
				addInfoInLegendBox.setEnabled(true);
			} else {
				addInfoInLegendBox.setEnabled(false);
			}
		} else if (e.getSource() == xBox) {
			lastParamX = (String) xBox.getSelectedItem();
			updateParametersPanel();
		} else if (parameterButtons.contains(e.getSource())) {
			if (type == PARAMETER_FIELDS) {
				JButton button = (JButton) e.getSource();
				DoubleTextField field = parameterFields.get(parameterButtons
						.indexOf(button));
				String param = button.getText().replace(":", "");
				Double min = minParamValues.get(param);
				Double max = maxParamValues.get(param);

				if (min != null && max != null) {
					double value = field.getValue() != null ? field.getValue()
							: min;
					SliderDialog dialog = new SliderDialog(param, min, max,
							value);

					dialog.setVisible(true);

					if (dialog.isApproved()) {
						field.removeTextListener(this);
						field.setValue(dialog.getValue());
						field.addTextListener(this);
					}
				} else {
					JOptionPane.showMessageDialog(this,
							"Range for Parameter is not available");
					return;
				}
			} else if (type == PARAMETER_BOXES) {
				JButton button = (JButton) e.getSource();
				String param = button.getText().replace(" Values", "");
				int i = parameters.indexOf(param);
				SelectDialog dialog = new SelectDialog(param,
						possibleValues.get(i), selectedValues.get(i));

				dialog.setVisible(true);

				if (dialog.isApproved()) {
					selectedValues.set(i, dialog.getSelected());
				}
			}
		}

		fireConfigChanged();
	}

	@Override
	public void textChanged() {
		fireConfigChanged();
	}

	public static interface ConfigListener {

		public void configChanged();
	}

	private class SelectDialog extends JDialog implements ActionListener {

		private static final long serialVersionUID = 1L;

		private boolean approved;
		private List<Boolean> selected;

		private List<JCheckBox> selectBoxes;

		private JButton okButton;
		private JButton cancelButton;

		public SelectDialog(String title, List<Double> values,
				List<Boolean> initialSelected) {
			super(JOptionPane.getFrameForComponent(ChartConfigPanel.this),
					title, true);

			approved = false;
			selected = null;

			selectBoxes = new ArrayList<JCheckBox>();
			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			JPanel centerPanel = new JPanel();
			JPanel bottomPanel = new JPanel();

			centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			centerPanel.setLayout(new GridLayout(values.size(), 1, 5, 5));

			for (int i = 0; i < values.size(); i++) {
				JCheckBox box = new JCheckBox(values.get(i) + "");

				box.setSelected(initialSelected.get(i));
				box.addActionListener(this);
				selectBoxes.add(box);
				centerPanel.add(box);
			}

			bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			bottomPanel.add(okButton);
			bottomPanel.add(cancelButton);

			setLayout(new BorderLayout());
			add(centerPanel, BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();

			setResizable(false);
			setLocationRelativeTo(ChartConfigPanel.this);
		}

		public boolean isApproved() {
			return approved;
		}

		public List<Boolean> getSelected() {
			return selected;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				approved = true;
				selected = new ArrayList<Boolean>();

				for (JCheckBox box : selectBoxes) {
					selected.add(box.isSelected());
				}

				dispose();
			} else if (e.getSource() == cancelButton) {
				dispose();
			} else {
				boolean noSelection = true;

				for (JCheckBox box : selectBoxes) {
					if (box.isSelected()) {
						noSelection = false;
						break;
					}
				}

				if (noSelection) {
					okButton.setEnabled(false);
				} else {
					okButton.setEnabled(true);
				}
			}
		}

	}

	private class SliderDialog extends JDialog implements ActionListener,
			ChangeListener {

		private static final long serialVersionUID = 1L;
		private static final int SLIDER_STEPS = 100;

		private boolean approved;
		private double value;
		private double min;
		private double max;

		private JSlider slider;
		private FormattedDoubleTextField valueField;
		private JButton okButton;
		private JButton cancelButton;

		public SliderDialog(String title, double min, double max,
				double initialValue) {
			super(JOptionPane.getFrameForComponent(ChartConfigPanel.this),
					title, true);
			this.min = min;
			this.max = max;

			approved = false;
			value = Double.NaN;

			valueField = new FormattedDoubleTextField();
			valueField.setValue(initialValue);
			valueField.setEditable(false);
			slider = new JSlider(0, SLIDER_STEPS, doubleToInt(initialValue));
			slider.addChangeListener(this);
			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			JPanel valuePanel = new JPanel();
			JPanel centerPanel = new JPanel();
			JPanel bottomPanel = new JPanel();

			valuePanel.setLayout(new BorderLayout(5, 5));
			valuePanel.add(new JLabel("Value:"), BorderLayout.WEST);
			valuePanel.add(valueField, BorderLayout.CENTER);

			centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			centerPanel.setLayout(new BorderLayout(5, 5));
			centerPanel.add(valuePanel, BorderLayout.NORTH);
			centerPanel.add(slider, BorderLayout.CENTER);

			bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			bottomPanel.add(okButton);
			bottomPanel.add(cancelButton);

			setLayout(new BorderLayout());
			add(centerPanel, BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
			pack();

			setResizable(false);
			setLocationRelativeTo(ChartConfigPanel.this);
		}

		public boolean isApproved() {
			return approved;
		}

		public double getValue() {
			return value;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				approved = true;
				value = valueField.getValue();
				dispose();
			} else if (e.getSource() == cancelButton) {
				dispose();
			}
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			valueField.setValue(intToDouble(slider.getValue()));
		}

		private int doubleToInt(double d) {
			return (int) ((d - min) / (max - min) * (double) SLIDER_STEPS);
		}

		private double intToDouble(int i) {
			return (double) i / (double) SLIDER_STEPS * (max - min) + min;
		}
	}

}
