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
		ChartConfigPanel.ExtraButtonListener, ChartSamplePanel.EditListener {

	private List<KnimeTuple> tuples;
	private TableReader reader;
	private SettingsHelper set;

	private JPanel mainComponent;
	private ChartCreator chartCreator;
	private ChartSelectionPanel selectionPanel;
	private ChartConfigPanel configPanel;
	private ChartSamplePanel samplePanel;

	private boolean showSamplePanel;

	/**
	 * New pane for configuring the ForecastStaticConditions node.
	 */
	protected PredictorViewNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel);
		showSamplePanel = true;
	}

	public PredictorViewNodeDialog(List<KnimeTuple> tuples) {
		this(tuples, new LinkedHashMap<String, String>());
	}

	public PredictorViewNodeDialog(List<KnimeTuple> tuples,
			Map<String, String> initParams) {
		this.tuples = tuples;
		set = new SettingsHelper();
		set.setConcentrationParameters(initParams);
		reader = new TableReader(tuples, set.getConcentrationParameters());
		mainComponent = new JPanel();
		mainComponent.setLayout(new BorderLayout());
		mainComponent.add(createMainComponent(), BorderLayout.CENTER);
		showSamplePanel = false;
	}

	public JPanel getMainComponent() {
		return mainComponent;
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		set = new SettingsHelper();
		set.loadSettings(settings);
		tuples = PredictorViewNodeModel.getTuples(input[0]);
		reader = new TableReader(tuples, set.getConcentrationParameters());
		mainComponent = new JPanel();
		mainComponent.setLayout(new BorderLayout());
		mainComponent.add(createMainComponent(), BorderLayout.CENTER);
		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(mainComponent);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		writeSettingsToVariables();
		set.saveSettings(settings);
	}

	public ChartAllPanel createMainComponent() {
		if (set.isStandardVisibleColumns()) {
			set.setVisibleColumns(reader.getStandardVisibleColumns());
		}

		Map<String, List<Double>> paramsX = new LinkedHashMap<>();
		Map<String, Double> minValues = new LinkedHashMap<>();
		Map<String, Double> maxValues = new LinkedHashMap<>();
		Map<String, List<String>> categories = new LinkedHashMap<>();
		Map<String, String> units = new LinkedHashMap<>();

		for (Plotable plotable : reader.getPlotables().values()) {
			paramsX.putAll(plotable.getFunctionArguments());
			categories.putAll(plotable.getCategories());
			units.putAll(plotable.getUnits());

			for (Map.Entry<String, Double> min : plotable.getMinArguments()
					.entrySet()) {
				Double oldMin = minValues.get(min.getKey());

				if (oldMin == null) {
					minValues.put(min.getKey(), min.getValue());
				} else if (min.getValue() != null) {
					minValues.put(min.getKey(),
							Math.min(min.getValue(), oldMin));
				}
			}

			for (Map.Entry<String, Double> max : plotable.getMaxArguments()
					.entrySet()) {
				Double oldMax = minValues.get(max.getKey());

				if (oldMax == null) {
					maxValues.put(max.getKey(), max.getValue());
				} else if (max.getValue() != null) {
					maxValues.put(max.getKey(),
							Math.max(max.getValue(), oldMax));
				}
			}
		}

		for (String var : paramsX.keySet()) {
			if (minValues.get(var) != null) {
				paramsX.put(var, Arrays.asList(minValues.get(var)));
			}
		}

		configPanel = new ChartConfigPanel(ChartConfigPanel.PARAMETER_FIELDS,
				false, "Change Init Params");
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
			configPanel.setUnitX(units.get(AttributeUtilities.CONCENTRATION));
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
		selectionPanel.setFilter(Model1Schema.MODELNAME, set.getModelFilter());
		selectionPanel
				.setFilter(AttributeUtilities.DATAID, set.getDataFilter());
		selectionPanel.setFilter(ChartConstants.STATUS, set.getFittedFilter());
		selectionPanel.addSelectionListener(this);
		chartCreator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());
		samplePanel = new ChartSamplePanel();
		samplePanel.setTimeValues(set.getTimeValues());
		samplePanel.addEditListener(this);

		selectionPanel.setSelectedIDs(set.getSelectedIDs());

		if (showSamplePanel) {
			return new ChartAllPanel(chartCreator, selectionPanel, configPanel,
					samplePanel);
		} else {
			return new ChartAllPanel(chartCreator, selectionPanel, configPanel);
		}
	}

	public Map<String, String> getInitParams() {
		return set.getConcentrationParameters();
	}

	public Map<String, Double> getParamValues() {
		return configPanel.getParamXValues();
	}

	public void setShowSamplePanel(boolean showSamplePanel) {
		this.showSamplePanel = showSamplePanel;
	}

	public void createChart() {
		List<String> selectedIDs = null;

		if (configPanel.isDisplayFocusedRow()) {
			selectedIDs = Arrays.asList(selectionPanel.getFocusedID());
		} else {
			selectedIDs = selectionPanel.getSelectedIDs();
		}

		for (String id : selectedIDs) {
			Plotable plotable = chartCreator.getPlotables().get(id);

			if (plotable != null) {
				plotable.setSamples(samplePanel.getTimeValues());
				plotable.setFunctionArguments(configPanel.getParamsX());
			}
		}

		selectionPanel.setInvalidIds(getInvalidIds(selectedIDs));
		selectionPanel.repaint();
		chartCreator.setParamX(configPanel.getParamX());
		chartCreator.setParamY(configPanel.getParamY());
		chartCreator.setUnitX(configPanel.getUnitX());
		chartCreator.setUnitY(configPanel.getUnitY());
		chartCreator.setTransformY(configPanel.getTransformY());

		Map<String, double[][]> points = new LinkedHashMap<>();

		for (String id : selectedIDs) {
			Plotable plotable = chartCreator.getPlotables().get(id);

			if (plotable != null) {
				try {
					points.put(id, plotable.getFunctionSamplePoints(
							AttributeUtilities.TIME,
							AttributeUtilities.CONCENTRATION,
							configPanel.getUnitX(), configPanel.getUnitY(),
							configPanel.getTransformY(),
							Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
							Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
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
		chartCreator.createChart(selectedIDs);
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
		set.setTransformY(configPanel.getTransformY());
		set.setStandardVisibleColumns(false);
		set.setVisibleColumns(selectionPanel.getVisibleColumns());
		set.setModelFilter(selectionPanel.getFilter(Model1Schema.MODELNAME));
		set.setDataFilter(selectionPanel.getFilter(AttributeUtilities.DATAID));
		set.setFittedFilter(selectionPanel.getFilter(ChartConstants.STATUS));
	}

	private List<String> getInvalidIds(List<String> selectedIDs) {
		List<String> invalid = new ArrayList<>();

		for (String id : selectedIDs) {
			Plotable plotable = chartCreator.getPlotables().get(id);

			if (plotable != null) {
				for (String param : configPanel.getParamsX().keySet()) {
					if (configPanel.getParamsX().get(param).size() == 1) {
						double value = configPanel.getParamsX().get(param)
								.get(0);
						Double min = plotable.getMinArguments().get(param);
						Double max = plotable.getMaxArguments().get(param);

						if ((min != null && value < min)
								|| (max != null && value > max)) {
							invalid.add(id);
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
	}

	@Override
	public void buttonPressed() {
		InitParamDialog dialog = new InitParamDialog(getPanel(), tuples,
				set.getConcentrationParameters());

		dialog.setVisible(true);

		if (dialog.isApproved()) {
			writeSettingsToVariables();
			set.setConcentrationParameters(dialog.getResult());
			set.setSelectedIDs(new ArrayList<String>());
			reader = new TableReader(tuples, set.getConcentrationParameters());

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
		private Map<String, JComboBox<String>> paramBoxes;
		private JButton okButton;
		private JButton cancelButton;

		private Map<String, String> result;

		public InitParamDialog(JComponent owner, List<KnimeTuple> tuples,
				Map<String, String> concentrationParameters) {
			super(JOptionPane.getFrameForComponent(owner),
					"Select Initial Parameter", true);
			readTable(tuples);

			setLayout(new BorderLayout());
			add(createPanel(concentrationParameters), BorderLayout.CENTER);
			setResizable(false);
			pack();

			setLocationRelativeTo(owner);
			UI.adjustDialog(this);
		}

		public Map<String, String> getResult() {
			return result;
		}

		public boolean isApproved() {
			return result != null;
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
				String id = ((CatalogModelXml) modelXml.get(0)).getID() + "";

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

		private JPanel createPanel(Map<String, String> concentrationParameters) {
			paramBoxes = new LinkedHashMap<String, JComboBox<String>>();
			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			JPanel leftPanel = new JPanel();
			JPanel rightPanel = new JPanel();

			leftPanel.setLayout(new GridLayout(ids.size(), 1, 5, 5));
			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			rightPanel.setLayout(new GridLayout(ids.size(), 1, 5, 5));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			for (String id : ids) {
				JComboBox<String> box = new JComboBox<String>(availableParams
						.get(id).toArray(new String[0]));
				JLabel label = new JLabel(modelNames.get(id) + ":");

				if (concentrationParameters.get(id) != null) {
					box.setSelectedItem(concentrationParameters.get(id));
				}

				box.setPreferredSize(new Dimension(150,
						box.getPreferredSize().height));
				label.setToolTipText(formulas.get(id));
				paramBoxes.put(id, box);
				leftPanel.add(label);
				rightPanel.add(box);
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

		private Map<String, String> getParameterMap() {
			Map<String, String> parameterMap = new LinkedHashMap<String, String>();

			for (String id : ids) {
				if (!paramBoxes.get(id).getSelectedItem().equals(NO_PARAM)) {
					parameterMap.put(id, (String) paramBoxes.get(id)
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
				result = getParameterMap();
				dispose();
			} else if (e.getSource() == cancelButton) {
				dispose();
			}
		}
	}

}
