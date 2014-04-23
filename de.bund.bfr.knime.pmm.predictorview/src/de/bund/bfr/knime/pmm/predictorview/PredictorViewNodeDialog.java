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
package de.bund.bfr.knime.pmm.predictorview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.chart.ChartAllPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartSamplePanel;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.ui.UI;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Category;
import de.bund.bfr.knime.pmm.common.units.ConvertException;

/**
 * <code>NodeDialog</code> for the "PredictorView" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class PredictorViewNodeDialog extends DataAwareNodeDialogPane implements
		ChartSelectionPanel.SelectionListener, ChartConfigPanel.ConfigListener,
		ChartConfigPanel.ExtraButtonListener, ChartSamplePanel.EditListener,
		ChartCreator.ZoomListener {

	private List<KnimeTuple> tuples;
	private TableReader reader;
	private SettingsHelper set;

	private JPanel mainComponent;
	private ChartAllPanel chartAllPanel;
	private ChartCreator chartCreator;
	private ChartSelectionPanel selectionPanel;
	private ChartConfigPanel configPanel;
	private ChartSamplePanel samplePanel;

	private boolean defaultBehaviour;
	private boolean removeInvalid;
	private List<String> warnings;

	/**
	 * New pane for configuring the ForecastStaticConditions node.
	 */
	protected PredictorViewNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel);
		defaultBehaviour = true;
		removeInvalid = true;
	}

	public PredictorViewNodeDialog(List<KnimeTuple> tuples, SettingsHelper set,
			boolean newTuples, boolean removeInvalid) {
		this.set = set;
		this.tuples = tuples;
		this.removeInvalid = removeInvalid;
		defaultBehaviour = false;

		if (newTuples) {
			reader = new TableReader(tuples,
					set.getNewConcentrationParameters(),
					set.getNewLagParameters(), defaultBehaviour);
		} else {
			reader = new TableReader(tuples, set.getConcentrationParameters(),
					set.getLagParameters(), defaultBehaviour);
		}

		mainComponent = new JPanel();
		mainComponent.setLayout(new BorderLayout());
		mainComponent.add(createMainComponent(), BorderLayout.CENTER);
	}

	public SettingsHelper getSettings() {
		writeSettingsToVariables();
		return set;
	}

	public TableReader getReader() {
		return reader;
	}

	public ChartSamplePanel getSamplePanel() {
		return samplePanel;
	}

	public JPanel getMainComponent() {
		return mainComponent;
	}

	public ChartAllPanel getChartAllPanel() {
		return chartAllPanel;
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		try {
			set = new SettingsHelper();
			set.loadSettings(settings);
			tuples = PredictorViewNodeModel.getTuples(input[0]);
			reader = new TableReader(tuples, set.getConcentrationParameters(),
					set.getLagParameters(), defaultBehaviour);
			mainComponent = new JPanel();
			mainComponent.setLayout(new BorderLayout());
			mainComponent.add(createMainComponent(), BorderLayout.CENTER);
			((JPanel) getTab("Options")).removeAll();
			((JPanel) getTab("Options")).add(mainComponent);
			selectionPanel.selectFirstRow();
		} catch (ConvertException e) {
			throw new NotConfigurableException(e.getMessage()
					+ "\nThis might be due errors in the unit table");
		}
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		writeSettingsToVariables();
		set.saveSettings(settings);
	}

	private ChartAllPanel createMainComponent() {
		if (set.isStandardVisibleColumns()) {
			set.setVisibleColumns(reader.getStandardVisibleColumns());
		}

		Map<String, List<Double>> paramsX = new LinkedHashMap<String, List<Double>>();
		Map<String, Double> minValues = new LinkedHashMap<String, Double>();
		Map<String, Double> maxValues = new LinkedHashMap<String, Double>();
		Map<String, List<String>> categories = new LinkedHashMap<String, List<String>>();
		Map<String, String> units = new LinkedHashMap<String, String>();

		for (Plotable plotable : reader.getPlotables().values()) {
			paramsX.putAll(plotable.getFunctionArguments());
			units.putAll(plotable.getUnits());

			for (String param : plotable.getCategories().keySet()) {
				if (!categories.containsKey(param)) {
					categories.put(param, new ArrayList<String>());
				}

				categories.get(param).addAll(
						plotable.getCategories().get(param));
			}

			for (String arg : plotable.getMinArguments().keySet()) {
				Double oldMin = minValues.get(arg);
				String unit = plotable.getUnits().get(arg);
				Category cat = Categories.getCategoryByUnit(plotable.getUnits()
						.get(arg));
				String stdUnit = cat.getStandardUnit();
				Double newMin = null;

				try {
					newMin = cat.convert(plotable.getMinArguments().get(arg),
							unit, stdUnit);
				} catch (ConvertException e) {
					e.printStackTrace();
				}

				if (oldMin == null) {
					minValues.put(arg, newMin);
				} else if (newMin != null) {
					minValues.put(arg, Math.min(newMin, oldMin));
				}
			}

			for (String arg : plotable.getMaxArguments().keySet()) {
				Double oldMax = maxValues.get(arg);
				String unit = plotable.getUnits().get(arg);
				Category cat = Categories.getCategoryByUnit(plotable.getUnits()
						.get(arg));
				String stdUnit = cat.getStandardUnit();
				Double newMax = null;

				try {
					newMax = cat.convert(plotable.getMaxArguments().get(arg),
							unit, stdUnit);
				} catch (ConvertException e) {
					e.printStackTrace();
				}

				if (oldMax == null) {
					maxValues.put(arg, newMax);
				} else if (newMax != null) {
					maxValues.put(arg, Math.max(newMax, oldMax));
				}
			}
		}

		for (String var : paramsX.keySet()) {
			if (minValues.get(var) != null) {
				paramsX.put(var, Arrays.asList(minValues.get(var)));
			}
		}

		configPanel = new ChartConfigPanel(ChartConfigPanel.PARAMETER_FIELDS,
				false, "Change Init/Lag Params", true, true);
		configPanel.setParameters(AttributeUtilities.CONCENTRATION, paramsX,
				minValues, maxValues, categories, units,
				AttributeUtilities.TIME);

		if (set.getUnitX() != null) {
			configPanel.setUnitX(set.getUnitX());
		} else {
			configPanel.setUnitX(units.get(AttributeUtilities.TIME));
		}

		if (set.getUnitY() != null) {
			configPanel.setUnitY(set.getUnitY());
		} else {
			configPanel.setUnitY(units.get(AttributeUtilities.CONCENTRATION));
		}

		if (!set.getParamXValues().isEmpty()) {
			configPanel.setParamXValues(set.getParamXValues());
		}

		configPanel.setUseManualRange(set.isManualRange());
		configPanel.setMinX(set.getMinX());
		configPanel.setMaxX(set.getMaxX());
		configPanel.setMinY(set.getMinY());
		configPanel.setMaxY(set.getMaxY());
		configPanel.setDrawLines(set.isDrawLines());
		configPanel.setShowLegend(set.isShowLegend());
		configPanel.setAddInfoInLegend(set.isAddLegendInfo());
		configPanel.setDisplayFocusedRow(set.isDisplayHighlighted());
		configPanel.setExportAsSvg(set.isExportAsSvg());
		configPanel.setTransformX(set.getTransformX());
		configPanel.setTransformY(set.getTransformY());
		configPanel.addConfigListener(this);
		configPanel.addExtraButtonListener(this);
		selectionPanel = new ChartSelectionPanel(reader.getIds(), false,
				reader.getStringColumns(), reader.getStringColumnValues(),
				reader.getDoubleColumns(), reader.getDoubleColumnValues(),
				reader.getConditions(), reader.getConditionValues(),
				reader.getConditionMinValues(), reader.getConditionMaxValues(),
				reader.getConditionUnits(), set.getVisibleColumns(),
				reader.getFilterableStringColumns(), null,
				reader.getParameterData(), reader.getFormulas());
		selectionPanel.setColors(set.getColors());
		selectionPanel.setShapes(set.getShapes());
		selectionPanel.setColumnWidths(set.getColumnWidths());
		selectionPanel.setFilter(ChartConstants.STATUS, set.getFittedFilter());
		selectionPanel.addSelectionListener(this);
		chartCreator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());
		chartCreator.addZoomListener(this);
		samplePanel = new ChartSamplePanel();
		samplePanel.setTimeValues(set.getTimeValues());
		samplePanel.addEditListener(this);

		selectionPanel.setSelectedIDs(set.getSelectedIDs());

		if (defaultBehaviour) {
			chartAllPanel = new ChartAllPanel(chartCreator, selectionPanel,
					configPanel, samplePanel);
		} else {
			chartAllPanel = new ChartAllPanel(chartCreator, selectionPanel,
					configPanel);
		}

		return chartAllPanel;
	}

	public void createChart() throws ConvertException {
		List<String> selectedIDs = null;

		if (configPanel.isDisplayFocusedRow()) {
			selectedIDs = Arrays.asList(selectionPanel.getFocusedID());
		} else {
			selectedIDs = selectionPanel.getSelectedIDs();
		}

		List<String> validIds = new ArrayList<String>(selectedIDs);

		warnings = new ArrayList<String>();

		for (String id : selectedIDs) {
			Plotable plotable = chartCreator.getPlotables().get(id);

			if (plotable != null) {
				plotable.setSamples(samplePanel.getTimeValues());
				plotable.setFunctionArguments(PredictorViewNodeModel
						.convertToUnits(configPanel.getParamsX(),
								plotable.getUnits()));
			}
		}

		if (removeInvalid) {
			List<String> invalidIds = new ArrayList<String>(getInvalidIds(
					reader.getIds()).keySet());

			validIds.removeAll(invalidIds);
			selectionPanel.setInvalidIds(invalidIds);
		} else {
			warnings.addAll(getInvalidIds(selectedIDs).values());
		}

		selectionPanel.repaint();
		chartCreator.setParamX(configPanel.getParamX());
		chartCreator.setParamY(configPanel.getParamY());
		chartCreator.setUnitX(configPanel.getUnitX());
		chartCreator.setUnitY(configPanel.getUnitY());
		chartCreator.setTransformX(configPanel.getTransformX());
		chartCreator.setTransformY(configPanel.getTransformY());

		Map<String, double[][]> points = new LinkedHashMap<String, double[][]>();

		for (String id : validIds) {
			Plotable plotable = chartCreator.getPlotables().get(id);

			if (plotable != null) {
				try {
					points.put(reader.getShortIds().get(id), plotable
							.getFunctionSamplePoints(AttributeUtilities.TIME,
									AttributeUtilities.CONCENTRATION,
									configPanel.getUnitX(),
									configPanel.getUnitY(),
									configPanel.getTransformX(),
									configPanel.getTransformY(),
									Double.NEGATIVE_INFINITY,
									Double.POSITIVE_INFINITY,
									Double.NEGATIVE_INFINITY,
									Double.POSITIVE_INFINITY, null));
				} catch (ConvertException e) {
					e.printStackTrace();
				}
			}
		}

		samplePanel.setDataPoints(points);
		// samplePanel.setTimeColumnName(AttributeUtilities.getNameWithUnit(
		// AttributeUtilities.TIME, configPanel.getUnitX()));
		// samplePanel.setLogcColumnName(AttributeUtilities.getNameWithUnit(
		// AttributeUtilities.CONCENTRATION, configPanel.getUnitY(),
		// configPanel.getTransformY()));
		chartCreator.setUseManualRange(configPanel.isUseManualRange());
		chartCreator.setMinX(configPanel.getMinX());
		chartCreator.setMinY(configPanel.getMinY());
		chartCreator.setMaxX(configPanel.getMaxX());
		chartCreator.setMaxY(configPanel.getMaxY());
		chartCreator.setDrawLines(configPanel.isDrawLines());
		chartCreator.setShowLegend(configPanel.isShowLegend());
		chartCreator.setAddInfoInLegend(configPanel.isAddInfoInLegend());
		chartCreator.setColors(selectionPanel.getColors());
		chartCreator.setShapes(selectionPanel.getShapes());
		chartCreator.createChart(validIds);
	}

	private void writeSettingsToVariables() {
		set.setSelectedIDs(selectionPanel.getSelectedIDs());
		set.setParamXValues(configPanel.getParamXValues());
		set.setTimeValues(samplePanel.getTimeValues());
		set.setColors(selectionPanel.getColors());
		set.setShapes(selectionPanel.getShapes());
		set.setManualRange(configPanel.isUseManualRange());
		set.setMinX(configPanel.getMinX());
		set.setMaxX(configPanel.getMaxX());
		set.setMinY(configPanel.getMinY());
		set.setMaxY(configPanel.getMaxY());
		set.setDrawLines(configPanel.isDrawLines());
		set.setShowLegend(configPanel.isShowLegend());
		set.setAddLegendInfo(configPanel.isAddInfoInLegend());
		set.setDisplayHighlighted(configPanel.isDisplayFocusedRow());
		set.setExportAsSvg(configPanel.isExportAsSvg());
		set.setUnitX(configPanel.getUnitX());
		set.setUnitY(configPanel.getUnitY());
		set.setTransformX(configPanel.getTransformX());
		set.setTransformY(configPanel.getTransformY());
		set.setStandardVisibleColumns(false);
		set.setVisibleColumns(selectionPanel.getVisibleColumns());
		set.setColumnWidths(selectionPanel.getColumnWidths());
		set.setFittedFilter(selectionPanel.getFilter(ChartConstants.STATUS));

		set.getSelectedTuples().clear();
		for (String id : set.getSelectedIDs()) {
			set.getSelectedTuples().add(reader.getTupleMap().get(id));
		}
		set.getSelectedOldTuples().clear();
		if (reader.getCombinedTuples() != null) {
			for (KnimeTuple tuple : set.getSelectedTuples()) {
				set.getSelectedOldTuples().addAll(
						reader.getCombinedTuples().get(tuple));
			}
		}
		set.setNewConcentrationParameters(reader.getNewInitParams());
		set.setNewLagParameters(reader.getNewLagParams());
	}

	private Map<String, String> getInvalidIds(List<String> selectedIDs)
			throws ConvertException {
		Map<String, String> invalid = new LinkedHashMap<String, String>();
		Set<String> nonVariables = new LinkedHashSet<String>();

		nonVariables.addAll(set.getConcentrationParameters().values());
		nonVariables.addAll(set.getLagParameters().values());

		for (String id : selectedIDs) {
			Plotable plotable = chartCreator.getPlotables().get(id);

			if (plotable != null) {
				Map<String, List<Double>> converted = PredictorViewNodeModel
						.convertToUnits(configPanel.getParamsX(),
								plotable.getUnits());

				for (String param : converted.keySet()) {
					if (!nonVariables.contains(param)
							&& converted.get(param).size() == 1) {
						double value = converted.get(param).get(0);
						Double min = plotable.getMinArguments().get(param);
						Double max = plotable.getMaxArguments().get(param);
						String unit = plotable.getUnits().get(param);

						if ((min != null && value < min)
								|| (max != null && value > max)) {
							invalid.put(id, param + " of " + value + " " + unit
									+ " is not in range of model " + min + " "
									+ unit + " to " + max + " " + unit);
						}
					}
				}
			}
		}

		return invalid;
	}

	@Override
	public void configChanged() {
		createChart();
	}

	@Override
	public void selectionChanged() {
		createChart();
	}

	@Override
	public void focusChanged() {
		if (configPanel.isDisplayFocusedRow()) {
			createChart();
		}
	}

	@Override
	public void timeValuesChanged() {
		createChart();

		if (!chartCreator.getWarnings().isEmpty()) {
			JOptionPane.showMessageDialog(chartCreator,
					"Some sample values are out of model range", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	@Override
	public void buttonPressed() {
		InitParamDialog dialog = new InitParamDialog(getPanel(), tuples,
				set.getConcentrationParameters(), set.getLagParameters());

		dialog.setVisible(true);

		if (dialog.isApproved()) {
			set.setConcentrationParameters(dialog.getInitParams());
			set.setLagParameters(dialog.getLagParams());
			set.setSelectedIDs(new ArrayList<String>());
			writeSettingsToVariables();
			reader = new TableReader(tuples, set.getConcentrationParameters(),
					set.getLagParameters(), defaultBehaviour);

			int divider = ((ChartAllPanel) mainComponent.getComponent(0))
					.getDividerLocation();

			ChartAllPanel chartPanel = createMainComponent();

			if (getTab("Options") != null) {
				((JPanel) getTab("Options")).removeAll();
				((JPanel) getTab("Options")).add(mainComponent);
				((JPanel) getTab("Options")).revalidate();
			}

			chartPanel.setDividerLocation(divider);
			mainComponent.removeAll();
			mainComponent.add(chartPanel, BorderLayout.CENTER);
			mainComponent.revalidate();
		}
	}

	private static class InitParamDialog extends JDialog implements
			ActionListener {

		private static final long serialVersionUID = 1L;

		private static final String NO_PARAM = "";

		private List<String> ids;
		private Map<String, String> modelNames;
		private Map<String, String> formulas;
		private Map<String, List<String>> availableParams;
		private Map<String, JComboBox<String>> initBoxes;
		private Map<String, JComboBox<String>> lagBoxes;
		private JButton okButton;
		private JButton cancelButton;

		private Map<String, String> initParams;
		private Map<String, String> lagParams;

		public InitParamDialog(JComponent owner, List<KnimeTuple> tuples,
				Map<String, String> concentrationParameters,
				Map<String, String> lagParameters) {
			super(SwingUtilities.getWindowAncestor(owner),
					"Select Initial/Lag Parameter", DEFAULT_MODALITY_TYPE);
			readTable(tuples);

			setLayout(new BorderLayout());
			add(createPanel(concentrationParameters, lagParameters),
					BorderLayout.CENTER);
			setResizable(false);
			pack();

			setLocationRelativeTo(owner);
			UI.adjustDialog(this);
		}

		public Map<String, String> getInitParams() {
			return initParams;
		}

		public Map<String, String> getLagParams() {
			return lagParams;
		}

		public boolean isApproved() {
			return initParams != null && lagParams != null;
		}

		private void readTable(List<KnimeTuple> tuples) {
			Set<String> idSet = new LinkedHashSet<String>();

			ids = new ArrayList<String>();
			modelNames = new LinkedHashMap<String, String>();
			formulas = new LinkedHashMap<String, String>();
			availableParams = new LinkedHashMap<String, List<String>>();

			for (KnimeTuple tuple : tuples) {
				PmmXmlDoc modelXml = tuple
						.getPmmXml(Model1Schema.ATT_MODELCATALOG);
				String id = ((CatalogModelXml) modelXml.get(0)).getId() + "";

				if (!idSet.add(id)) {
					continue;
				}

				List<String> params = new ArrayList<String>();

				params.add(NO_PARAM);
				params.addAll(CellIO.getNameList(tuple
						.getPmmXml(Model1Schema.ATT_PARAMETER)));

				ids.add(id);
				modelNames.put(id,
						((CatalogModelXml) modelXml.get(0)).getName());
				formulas.put(id,
						((CatalogModelXml) modelXml.get(0)).getFormula());
				availableParams.put(id, params);
			}
		}

		private JPanel createPanel(Map<String, String> concentrationParameters,
				Map<String, String> lagParameters) {
			initBoxes = new LinkedHashMap<String, JComboBox<String>>();
			lagBoxes = new LinkedHashMap<String, JComboBox<String>>();
			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			JPanel leftPanel = new JPanel();
			JPanel rightPanel = new JPanel();

			leftPanel.setLayout(new GridLayout(ids.size() + 1, 1, 5, 5));
			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			rightPanel.setLayout(new GridLayout(ids.size() + 1, 2, 5, 5));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			leftPanel.add(new JLabel());
			rightPanel.add(new JLabel("Initial Concentration"));
			rightPanel.add(new JLabel("Lag"));

			for (String id : ids) {
				JComboBox<String> initBox = new JComboBox<String>(
						availableParams.get(id).toArray(new String[0]));
				JComboBox<String> lagBox = new JComboBox<String>(
						availableParams.get(id).toArray(new String[0]));
				JLabel label = new JLabel(modelNames.get(id) + ":");

				if (concentrationParameters.get(id) != null) {
					initBox.setSelectedItem(concentrationParameters.get(id));
				}

				if (lagParameters.get(id) != null) {
					lagBox.setSelectedItem(lagParameters.get(id));
				}

				initBox.setPreferredSize(new Dimension(150, initBox
						.getPreferredSize().height));
				lagBox.setPreferredSize(new Dimension(150, initBox
						.getPreferredSize().height));
				label.setToolTipText(formulas.get(id));
				initBoxes.put(id, initBox);
				lagBoxes.put(id, lagBox);
				leftPanel.add(label);
				rightPanel.add(initBox);
				rightPanel.add(lagBox);
			}

			JPanel parameterPanel = new JPanel();

			parameterPanel.setLayout(new BorderLayout());
			parameterPanel.add(leftPanel, BorderLayout.WEST);
			parameterPanel.add(rightPanel, BorderLayout.EAST);

			JPanel buttonPanel = new JPanel();

			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPanel.add(okButton);
			buttonPanel.add(cancelButton);

			JPanel panel = new JPanel();

			panel.setLayout(new BorderLayout());
			panel.add(parameterPanel, BorderLayout.CENTER);
			panel.add(buttonPanel, BorderLayout.SOUTH);

			return panel;
		}

		private Map<String, String> getInitMap() {
			Map<String, String> parameterMap = new LinkedHashMap<String, String>();

			for (String id : ids) {
				if (!initBoxes.get(id).getSelectedItem().equals(NO_PARAM)) {
					parameterMap.put(id, (String) initBoxes.get(id)
							.getSelectedItem());
				} else {
					parameterMap.put(id, null);
				}
			}

			return parameterMap;
		}

		private Map<String, String> getLagMap() {
			Map<String, String> parameterMap = new LinkedHashMap<String, String>();

			for (String id : ids) {
				if (!lagBoxes.get(id).getSelectedItem().equals(NO_PARAM)) {
					parameterMap.put(id, (String) lagBoxes.get(id)
							.getSelectedItem());
				} else {
					parameterMap.put(id, null);
				}
			}

			return parameterMap;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				initParams = getInitMap();
				lagParams = getLagMap();
				dispose();
			} else if (e.getSource() == cancelButton) {
				dispose();
			}
		}
	}

	public List<String> getWarnings() {
		return warnings;
	}

	@Override
	public void zoomChanged() {
		configPanel.removeConfigListener(this);
		configPanel.setUseManualRange(true);
		configPanel.setMinX(chartCreator.getMinX());
		configPanel.setMaxX(chartCreator.getMaxX());
		configPanel.setMinY(chartCreator.getMinY());
		configPanel.setMaxY(chartCreator.getMaxY());
		configPanel.addConfigListener(this);
		createChart();	
	}

}
