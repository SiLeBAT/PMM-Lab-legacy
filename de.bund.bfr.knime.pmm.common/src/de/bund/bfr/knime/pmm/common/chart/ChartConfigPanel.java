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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.ui.DoubleTextField;
import de.bund.bfr.knime.pmm.common.ui.SpacePanel;
import de.bund.bfr.knime.pmm.common.ui.TextListener;

public class ChartConfigPanel extends JPanel implements ActionListener,
		TextListener, ChangeListener, MouseListener {

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

	private JComboBox<String> xBox;
	private JComboBox<String> yBox;
	private JComboBox<String> xUnitBox;
	private JComboBox<String> yUnitBox;
	private JComboBox<String> yTransBox;
	private Map<String, List<Double>> parametersX;
	private List<String> parametersY;
	private Map<String, List<Boolean>> selectedValuesX;
	private Map<String, Double> minParamValuesX;
	private Map<String, Double> maxParamValuesX;

	private JPanel parameterValuesPanel;
	private List<JButton> parameterButtons;
	private List<JLabel> parameterLabels;
	private List<DoubleTextField> parameterFields;
	private List<JSlider> parameterSliders;

	private int type;

	private String currentParamX;

	public ChartConfigPanel(int type, boolean allowConfidenceInterval) {
		this.type = type;
		listeners = new ArrayList<ConfigListener>();
		setLayout(new GridLayout(4, 1));

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

		xBox = new JComboBox<String>();
		xBox.addActionListener(this);
		yBox = new JComboBox<String>();
		yBox.addActionListener(this);
		xUnitBox = new JComboBox<String>();
		xUnitBox.addActionListener(this);
		yUnitBox = new JComboBox<String>();
		yUnitBox.addActionListener(this);
		yTransBox = new JComboBox<String>(ChartConstants.TRANSFORMS);
		yTransBox.addActionListener(this);

		parametersPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		parametersPanel.setBorder(BorderFactory
				.createTitledBorder("Parameters"));
		parametersPanel.add(new JLabel("X:"));
		parametersPanel.add(xBox);
		parametersPanel.add(new JLabel("Y:"));
		parametersPanel.add(yBox);
		parametersPanel.add(new JLabel("X Unit:"));
		parametersPanel.add(xUnitBox);
		parametersPanel.add(new JLabel("Y Unit:"));
		parametersPanel.add(yUnitBox);
		parametersPanel.add(new JLabel("Y Transform:"));
		parametersPanel.add(yTransBox);
		add(new SpacePanel(parametersPanel));

		parameterValuesPanel = new JPanel();
		parameterValuesPanel.setBorder(BorderFactory
				.createTitledBorder("Parameter Values"));
		parameterValuesPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		parameterFields = new ArrayList<DoubleTextField>();
		parameterButtons = new ArrayList<JButton>();
		parameterLabels = new ArrayList<>();
		parameterSliders = new ArrayList<>();
		add(new SpacePanel(parameterValuesPanel));

		currentParamX = null;
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

	public String getUnitX() {
		return (String) xUnitBox.getSelectedItem();
	}

	public void setUnitX(String unitX) {
		xUnitBox.setSelectedItem(unitX);
	}

	public String getUnitY() {
		return (String) yUnitBox.getSelectedItem();
	}

	public void setUnitY(String unitY) {
		yUnitBox.setSelectedItem(unitY);
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
					String paramName = parameterLabels.get(i).getText()
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
			for (String param : parametersX.keySet()) {
				List<Double> values = parametersX.get(param);
				List<Boolean> selected = selectedValuesX.get(param);
				List<Double> newValues = new ArrayList<Double>();

				for (int j = 0; j < values.size(); j++) {
					if (selected.get(j)) {
						newValues.add(values.get(j));
					}
				}

				if (newValues.isEmpty()) {
					newValues.add(0.0);
				}

				valueLists.put(param, newValues);
			}
		}

		valueLists.put((String) xBox.getSelectedItem(), new ArrayList<Double>(
				Arrays.asList(0.0)));

		return valueLists;
	}

	public String getCurrentParamX() {
		return currentParamX;
	}

	public void setCurrentParamX(String currentParamX) {
		this.currentParamX = currentParamX;
	}

	public Map<String, List<Boolean>> getSelectedValuesX() {
		return selectedValuesX;
	}

	public void setSelectedValuesX(Map<String, List<Boolean>> selectedValuesX) {
		this.selectedValuesX = selectedValuesX;
	}

	public void setParamsX(Map<String, List<Double>> parameters,
			Map<String, Double> minParamValues,
			Map<String, Double> maxParamValues, String paramX) {
		boolean parametersChanged = false;

		if (parameters == null) {
			parameters = new LinkedHashMap<String, List<Double>>();
		}

		if (minParamValues == null) {
			minParamValues = new LinkedHashMap<String, Double>();
		}

		if (maxParamValues == null) {
			maxParamValues = new LinkedHashMap<String, Double>();
		}

		if (this.parametersX == null
				|| parameters.size() != this.parametersX.size()) {
			parametersChanged = true;
		} else {
			for (String param : parameters.keySet()) {
				if (!this.parametersX.containsKey(param)) {
					parametersChanged = true;
					break;
				} else if (!parameters.get(param).equals(
						this.parametersX.get(param))) {
					parametersChanged = true;
					break;
				}
			}
		}

		this.parametersX = parameters;
		this.minParamValuesX = minParamValues;
		this.maxParamValuesX = maxParamValues;

		if (parametersChanged) {
			xBox.removeActionListener(this);
			xBox.removeAllItems();

			if (paramX != null) {
				xBox.addItem(paramX);
			} else {
				for (String param : parameters.keySet()) {
					xBox.addItem(param);
				}
			}

			if (!parameters.isEmpty()) {
				if (parameters.containsKey(currentParamX)) {
					xBox.setSelectedItem(currentParamX);
				} else if (parameters.containsKey(AttributeUtilities.TIME)) {
					xBox.setSelectedItem(AttributeUtilities.TIME);
				} else {
					xBox.setSelectedIndex(0);
				}

				currentParamX = (String) xBox.getSelectedItem();
			} else {
				currentParamX = null;
			}

			xBox.addActionListener(this);

			boolean isInitialized = false;

			if (selectedValuesX != null) {
				isInitialized = true;

				for (String param : parameters.keySet()) {
					if (!selectedValuesX.containsKey(param)
							|| selectedValuesX.get(param).size() != parameters
									.get(param).size()) {
						isInitialized = false;
						break;
					}
				}
			}

			if (!isInitialized) {
				selectedValuesX = new LinkedHashMap<String, List<Boolean>>();

				for (String param : parameters.keySet()) {
					List<Double> values = parameters.get(param);

					if (!values.isEmpty()) {
						List<Boolean> selected = new ArrayList<Boolean>(
								Collections.nCopies(values.size(), true));

						selectedValuesX.put(param, selected);
					} else {
						selectedValuesX.put(param, new ArrayList<Boolean>());
					}
				}
			}

			updateParametersPanel();
			updateXUnitBox();
		}
	}

	public void setParamsY(List<String> paramsY) {
		if (paramsY == null) {
			paramsY = new ArrayList<String>();
		}

		if (!paramsY.equals(parametersY)) {
			parametersY = paramsY;
			yBox.removeActionListener(this);
			yBox.removeAllItems();

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
			updateYUnitBox();
		}
	}

	private void updateXUnitBox() {
		List<String> units = AttributeUtilities
				.getUnitsForAttribute((String) xBox.getSelectedItem());

		xUnitBox.removeActionListener(this);
		xUnitBox.removeAllItems();

		for (String unit : units) {
			xUnitBox.addItem(unit);
		}

		if (!units.isEmpty()) {
			xUnitBox.setSelectedIndex(0);
		}

		xUnitBox.addActionListener(this);
	}

	private void updateYUnitBox() {
		List<String> units = AttributeUtilities
				.getUnitsForAttribute((String) yBox.getSelectedItem());

		yUnitBox.removeActionListener(this);
		yUnitBox.removeAllItems();

		for (String unit : units) {
			yUnitBox.addItem(unit);
		}

		if (!units.isEmpty()) {
			yUnitBox.setSelectedIndex(0);
		}

		yUnitBox.addActionListener(this);
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

		for (JLabel label : parameterLabels) {
			parameterValuesPanel.remove(label);
		}

		for (JSlider slider : parameterSliders) {
			if (slider != null) {
				parameterValuesPanel.remove(slider);
			}
		}

		parameterFields.clear();
		parameterButtons.clear();
		parameterLabels.clear();
		parameterSliders.clear();

		for (String param : parametersX.keySet()) {
			if (param.equals(xBox.getSelectedItem())) {
				continue;
			}

			if (type == PARAMETER_FIELDS) {
				JLabel label = new JLabel(param + ":");
				DoubleTextField input = new DoubleTextField();
				JSlider slider = null;
				Double value = null;
				Double min = minParamValuesX.get(param);
				Double max = maxParamValuesX.get(param);

				if (!parametersX.get(param).isEmpty()) {
					value = parametersX.get(param).get(0);
				}

				if (min != null && max != null) {
					if (value == null) {
						value = min;
					}

					if (value < min) {
						slider = new JSlider(0, 100, doubleToInt(min, min, max));
					} else if (value > max) {
						slider = new JSlider(0, 100, doubleToInt(max, min, max));
					} else {
						slider = new JSlider(0, 100, doubleToInt(value, min,
								max));
					}

					slider.setPreferredSize(new Dimension(75, slider
							.getPreferredSize().height));
					slider.addChangeListener(this);
					slider.addMouseListener(this);
				}

				if (value == null) {
					value = 0.0;
				}

				input.setPreferredSize(new Dimension(50, input
						.getPreferredSize().height));
				input.setValue(value);
				input.addTextListener(this);

				parameterFields.add(input);
				parameterLabels.add(label);
				parameterSliders.add(slider);
				parameterValuesPanel.add(label);

				if (slider != null) {
					parameterValuesPanel.add(slider);
				}

				parameterValuesPanel.add(input);
			} else if (type == PARAMETER_BOXES) {
				JButton selectButton = new JButton(param + " Values");

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

	private int doubleToInt(double d, double min, double max) {
		return (int) ((d - min) / (max - min) * (double) 100);
	}

	private double intToDouble(int i, double min, double max) {
		return (double) i / (double) 100 * (max - min) + min;
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
			
			fireConfigChanged();
		} else if (e.getSource() == showLegendBox) {
			if (showLegendBox.isSelected()) {
				addInfoInLegendBox.setEnabled(true);
			} else {
				addInfoInLegendBox.setEnabled(false);
			}
			
			fireConfigChanged();
		} else if (e.getSource() == xBox) {
			currentParamX = (String) xBox.getSelectedItem();
			updateXUnitBox();
			updateParametersPanel();			
			fireConfigChanged();
		} else if (parameterButtons.contains(e.getSource())) {
			JButton button = (JButton) e.getSource();
			String param = button.getText().replace(" Values", "");
			SelectDialog dialog = new SelectDialog(param,
					parametersX.get(param), selectedValuesX.get(param));

			dialog.setVisible(true);

			if (dialog.isApproved()) {
				selectedValuesX.put(param, dialog.getSelected());
				fireConfigChanged();
			}
		} else {
			fireConfigChanged();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		int i = parameterSliders.indexOf(e.getSource());
		String paramName = parameterLabels.get(i).getText().replace(":", "");
		JSlider slider = parameterSliders.get(i);
		DoubleTextField field = parameterFields.get(i);

		field.removeTextListener(this);
		field.setValue(intToDouble(slider.getValue(),
				minParamValuesX.get(paramName), maxParamValuesX.get(paramName)));
		field.addTextListener(this);
	}

	@Override
	public void textChanged() {
		fireConfigChanged();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int i = parameterSliders.indexOf(e.getSource());
		String paramName = parameterLabels.get(i).getText().replace(":", "");
		JSlider slider = parameterSliders.get(i);
		DoubleTextField field = parameterFields.get(i);

		field.setValue(intToDouble(slider.getValue(),
				minParamValuesX.get(paramName), maxParamValuesX.get(paramName)));
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

}
