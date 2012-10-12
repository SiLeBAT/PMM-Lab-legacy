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
package de.bund.bfr.knime.pmm.secondarymodelanddataview;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.knime.core.node.NodeView;

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.DataAndModelChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.DataAndModelChartCreator;
import de.bund.bfr.knime.pmm.common.chart.DataAndModelChartInfoPanel;
import de.bund.bfr.knime.pmm.common.chart.DataAndModelSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * <code>NodeView</code> for the "SecondaryModelAndDataView" Node.
 * 
 * 
 * @author Christian Thoens
 */
public class SecondaryModelAndDataViewNodeView extends
		NodeView<SecondaryModelAndDataViewNodeModel> implements
		DataAndModelSelectionPanel.SelectionListener,
		DataAndModelChartConfigPanel.ConfigListener {

	private List<String> ids;
	private List<Integer> colorCounts;
	private Map<String, Plotable> plotables;
	private List<String> stringColumns;
	private List<List<String>> stringColumnValues;
	private List<String> doubleColumns;
	private List<List<Double>> doubleColumnValues;
	private List<List<String>> infoParameters;
	private List<List<?>> infoParameterValues;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;

	private DataAndModelChartCreator chartCreator;
	private DataAndModelSelectionPanel selectionPanel;
	private DataAndModelChartConfigPanel configPanel;
	private DataAndModelChartInfoPanel infoPanel;

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link SecondaryModelAndDataViewNodeModel})
	 */
	protected SecondaryModelAndDataViewNodeView(
			final SecondaryModelAndDataViewNodeModel nodeModel) {
		super(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onClose() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onOpen() {
		try {
			readTable();

			if (getNodeModel().isSeiSchema()) {
				configPanel = new DataAndModelChartConfigPanel(
						DataAndModelChartConfigPanel.PARAMETER_BOXES);
				selectionPanel = new DataAndModelSelectionPanel(ids, true,
						stringColumns, stringColumnValues, doubleColumns,
						doubleColumnValues, Arrays.asList(true, true, false),
						Arrays.asList(false, false, true), colorCounts);
			} else if (getNodeModel().isModel2Schema()) {
				configPanel = new DataAndModelChartConfigPanel(
						DataAndModelChartConfigPanel.PARAMETER_FIELDS);
				selectionPanel = new DataAndModelSelectionPanel(ids, true,
						stringColumns, stringColumnValues, doubleColumns,
						doubleColumnValues, Arrays.asList(true, true, false),
						Arrays.asList(false, false, true));
			}

			configPanel.addConfigListener(this);
			selectionPanel.addSelectionListener(this);
			chartCreator = new DataAndModelChartCreator(plotables, shortLegend,
					longLegend);
			infoPanel = new DataAndModelChartInfoPanel(ids, infoParameters,
					infoParameterValues);

			JSplitPane upperSplitPane = new JSplitPane(
					JSplitPane.HORIZONTAL_SPLIT, chartCreator, selectionPanel);
			JPanel bottomPanel = new JPanel();

			upperSplitPane.setResizeWeight(1.0);
			bottomPanel.setLayout(new BorderLayout());
			bottomPanel.add(configPanel, BorderLayout.WEST);
			bottomPanel.add(infoPanel, BorderLayout.CENTER);

			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
					upperSplitPane, bottomPanel);

			splitPane.setResizeWeight(1.0);

			setComponent(splitPane);
		} catch (PmmException e) {
			e.printStackTrace();
		}
	}

	private void createChart() {
		String selectedID = null;

		if (configPanel.isDisplayFocusedRow()) {
			selectedID = selectionPanel.getFocusedID();
		} else {
			if (!selectionPanel.getSelectedIDs().isEmpty()) {
				selectedID = selectionPanel.getSelectedIDs().get(0);
			}
		}

		if (selectedID != null) {
			Plotable plotable = chartCreator.getPlotables().get(selectedID);
			List<String> variables = new ArrayList<String>(plotable
					.getFunctionArguments().keySet());
			List<List<Double>> possibleValues = null;

			if (getNodeModel().isSeiSchema()) {
				possibleValues = new ArrayList<List<Double>>();

				for (String var : variables) {
					Set<Double> valuesSet = new HashSet<Double>(
							plotable.getValueList(var));

					valuesSet.remove(null);

					List<Double> valuesList = new ArrayList<Double>(valuesSet);

					Collections.sort(valuesList);
					possibleValues.add(valuesList);
				}
			}

			configPanel.setParamsX(variables, possibleValues);
			configPanel.setParamsY(Arrays.asList(plotable.getFunctionValue()));
			chartCreator.setParamX(configPanel.getParamX());
			chartCreator.setParamY(configPanel.getParamY());
			chartCreator.setTransformY(configPanel.getTransformY());
			plotable.setFunctionArguments(configPanel.getParamsXValues());
		} else {
			configPanel.setParamsX(null);
			configPanel.setParamsY(null);
			chartCreator.setParamX(null);
			chartCreator.setParamY(null);
			chartCreator.setTransformY(null);
		}

		if (getNodeModel().isSeiSchema()) {
			chartCreator.setColorLists(selectionPanel.getColorLists());
			chartCreator.setShapeLists(selectionPanel.getShapeLists());
		} else if (getNodeModel().isModel2Schema()) {
			chartCreator.setColors(selectionPanel.getColors());
			chartCreator.setShapes(selectionPanel.getShapes());
		}

		chartCreator.setUseManualRange(configPanel.isUseManualRange());
		chartCreator.setMinX(configPanel.getMinX());
		chartCreator.setMinY(configPanel.getMinY());
		chartCreator.setMaxX(configPanel.getMaxX());
		chartCreator.setMaxY(configPanel.getMaxY());
		chartCreator.setDrawLines(configPanel.isDrawLines());
		chartCreator.setShowLegend(configPanel.isShowLegend());
		chartCreator.setAddInfoInLegend(configPanel.isAddInfoInLegend());
		chartCreator.createChart(selectedID);
	}

	private void readTable() throws PmmException {
		Set<String> idSet = new HashSet<String>();
		KnimeRelationReader reader = new KnimeRelationReader(getNodeModel()
				.getSchema(), getNodeModel().getTable());
		Map<String, String> formulaMap = new HashMap<String, String>();
		Map<String, String> depVarMap = new HashMap<String, String>();
		Map<String, List<String>> indepVarMap = new HashMap<String, List<String>>();
		Map<String, List<Double>> minIndepVarMap = new HashMap<String, List<Double>>();
		Map<String, List<Double>> maxIndepVarMap = new HashMap<String, List<Double>>();
		Map<String, List<String>> keyMap = new HashMap<String, List<String>>();
		Map<String, List<Double>> valueMap = new HashMap<String, List<Double>>();
		Map<String, List<Double>> minValueMap = new HashMap<String, List<Double>>();
		Map<String, List<Double>> maxValueMap = new HashMap<String, List<Double>>();
		Map<String, List<Double>> depVarDataMap = new HashMap<String, List<Double>>();
		Map<String, List<Double>> temperatureDataMap = new HashMap<String, List<Double>>();
		Map<String, List<Double>> phDataMap = new HashMap<String, List<Double>>();
		Map<String, List<Double>> awDataMap = new HashMap<String, List<Double>>();
		Map<String, Double> rmsMap = new HashMap<String, Double>();
		Map<String, Double> rSquaredMap = new HashMap<String, Double>();

		ids = new ArrayList<String>();
		plotables = new HashMap<String, Plotable>();
		infoParameters = new ArrayList<List<String>>();
		infoParameterValues = new ArrayList<List<?>>();
		shortLegend = new HashMap<String, String>();
		longLegend = new HashMap<String, String>();
		stringColumns = Arrays.asList(Model1Schema.ATT_DEPVAR,
				Model1Schema.ATT_MODELNAME, ChartConstants.IS_FITTED);
		stringColumnValues = new ArrayList<List<String>>();
		stringColumnValues.add(new ArrayList<String>());
		stringColumnValues.add(new ArrayList<String>());
		stringColumnValues.add(new ArrayList<String>());

		if (getNodeModel().isSeiSchema()) {
			doubleColumns = Arrays.asList("Min "
					+ TimeSeriesSchema.ATT_TEMPERATURE, "Max "
					+ TimeSeriesSchema.ATT_TEMPERATURE, "Min "
					+ TimeSeriesSchema.ATT_PH,
					"Max " + TimeSeriesSchema.ATT_PH, "Min "
							+ TimeSeriesSchema.ATT_WATERACTIVITY, "Max "
							+ TimeSeriesSchema.ATT_WATERACTIVITY,
					Model2Schema.ATT_RMS, Model2Schema.ATT_RSQUARED);
			doubleColumnValues = new ArrayList<List<Double>>();
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			colorCounts = new ArrayList<Integer>();
		} else if (getNodeModel().isModel2Schema()) {
			doubleColumns = Arrays.asList(Model2Schema.ATT_RMS,
					Model2Schema.ATT_RSQUARED);
			doubleColumnValues = new ArrayList<List<Double>>();
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
		}

		while (reader.hasMoreElements()) {
			KnimeTuple row = reader.nextElement();
			String id = row.getString(Model2Schema.ATT_DEPVAR);

			if (!idSet.contains(id)) {
				String modelNameSec = row.getString(Model2Schema.ATT_MODELNAME);
				String formulaSec = row.getString(Model2Schema.ATT_FORMULA);
				String depVarSec = row.getString(Model2Schema.ATT_DEPVAR);
				List<String> indepVarSec = row
						.getStringList(Model2Schema.ATT_INDEPVAR);
				List<Double> minIndepVarSec = row
						.getDoubleList(Model2Schema.ATT_MININDEP);
				List<Double> maxIndepVarSec = row
						.getDoubleList(Model2Schema.ATT_MAXINDEP);
				List<String> paramNamesSec = row
						.getStringList(Model2Schema.ATT_PARAMNAME);
				List<Double> paramValuesSec = row
						.getDoubleList(Model2Schema.ATT_VALUE);
				List<Double> paramMinValuesSec = row
						.getDoubleList(Model2Schema.ATT_MINVALUE);
				List<Double> paramMaxValuesSec = row
						.getDoubleList(Model2Schema.ATT_MAXVALUE);
				List<String> infoParams = new ArrayList<String>(Arrays.asList(
						Model2Schema.ATT_MODELNAME, Model2Schema.ATT_FORMULA));
				List<String> infoValues = new ArrayList<String>(Arrays.asList(
						modelNameSec, formulaSec));

				infoParams.addAll(paramNamesSec);

				for (Double value : paramValuesSec) {
					infoValues.add(value + "");
				}

				idSet.add(id);
				ids.add(id);
				stringColumnValues.get(0).add(depVarSec);
				stringColumnValues.get(1).add(modelNameSec);
				infoParameters.add(infoParams);
				infoParameterValues.add(infoValues);
				shortLegend.put(id, depVarSec);
				longLegend.put(id, depVarSec + " (" + modelNameSec + ")");

				formulaMap.put(id, formulaSec);
				depVarMap.put(id, depVarSec);
				indepVarMap.put(id, indepVarSec);
				minIndepVarMap.put(id, minIndepVarSec);
				maxIndepVarMap.put(id, maxIndepVarSec);
				keyMap.put(id, paramNamesSec);
				valueMap.put(id, paramValuesSec);
				minValueMap.put(id, paramMinValuesSec);
				maxValueMap.put(id, paramMaxValuesSec);
				temperatureDataMap.put(id, new ArrayList<Double>());
				phDataMap.put(id, new ArrayList<Double>());
				awDataMap.put(id, new ArrayList<Double>());
				depVarDataMap.put(id, new ArrayList<Double>());
				rmsMap.put(id, row.getDouble(Model2Schema.ATT_RMS));
				rSquaredMap.put(id, row.getDouble(Model2Schema.ATT_RSQUARED));
			}

			if (getNodeModel().isSeiSchema()) {
				List<String> keys = row
						.getStringList(Model1Schema.ATT_PARAMNAME);
				List<Double> values = row.getDoubleList(Model1Schema.ATT_VALUE);
				String depVar = depVarMap.get(id);
				Double depVarValue = values.get(keys.indexOf(depVar));

				depVarDataMap.get(id).add(depVarValue);
				temperatureDataMap.get(id).add(
						row.getDouble(TimeSeriesSchema.ATT_TEMPERATURE));
				phDataMap.get(id).add(row.getDouble(TimeSeriesSchema.ATT_PH));
				awDataMap.get(id).add(
						row.getDouble(TimeSeriesSchema.ATT_WATERACTIVITY));
			}
		}

		for (String id : ids) {
			Plotable plotable = null;
			Map<String, List<Double>> arguments = new HashMap<String, List<Double>>();
			Map<String, Double> minArg = new HashMap<String, Double>();
			Map<String, Double> maxArg = new HashMap<String, Double>();
			Map<String, Double> constants = new HashMap<String, Double>();

			if (getNodeModel().isSeiSchema()) {
				plotable = new Plotable(Plotable.BOTH_STRICT);
			} else if (getNodeModel().isModel2Schema()) {
				plotable = new Plotable(Plotable.FUNCTION);
			}

			for (int i = 0; i < indepVarMap.get(id).size(); i++) {
				arguments.put(indepVarMap.get(id).get(i),
						new ArrayList<Double>(Arrays.asList(0.0)));
				minArg.put(indepVarMap.get(id).get(i), minIndepVarMap.get(id)
						.get(i));
				maxArg.put(indepVarMap.get(id).get(i), maxIndepVarMap.get(id)
						.get(i));
			}

			for (int i = 0; i < keyMap.get(id).size(); i++) {
				constants.put(keyMap.get(id).get(i), valueMap.get(id).get(i));
			}

			plotable.setFunction(formulaMap.get(id));
			plotable.setFunctionValue(depVarMap.get(id));
			plotable.setFunctionArguments(arguments);
			plotable.setMinArguments(minArg);
			plotable.setMaxArguments(maxArg);
			plotable.setFunctionConstants(constants);

			if (getNodeModel().isSeiSchema()) {
				List<Double> depVarData = depVarDataMap.get(id);
				List<Double> temperatures = temperatureDataMap.get(id);
				List<Double> phs = phDataMap.get(id);
				List<Double> aws = awDataMap.get(id);

				for (int i = 0; i < depVarData.size(); i++) {
					if (depVarData.get(i) == null) {
						depVarData.remove(i);
						temperatures.remove(i);
						phs.remove(i);
						aws.remove(i);
					}
				}

				plotable.addValueList(depVarMap.get(id), depVarData);
				plotable.addValueList(TimeSeriesSchema.ATT_TEMPERATURE,
						temperatures);
				plotable.addValueList(TimeSeriesSchema.ATT_PH, phs);
				plotable.addValueList(TimeSeriesSchema.ATT_WATERACTIVITY, aws);

				List<Double> nonNullTemperatures = new ArrayList<Double>(
						temperatures);
				List<Double> nonNullPHs = new ArrayList<Double>(phs);
				List<Double> nonNullWaterActivites = new ArrayList<Double>(aws);

				nonNullTemperatures.removeAll(Arrays.asList((Double) null));
				nonNullPHs.removeAll(Arrays.asList((Double) null));
				nonNullWaterActivites.removeAll(Arrays.asList((Double) null));

				if (!plotable.isPlotable()) {
					stringColumnValues.get(2).add(ChartConstants.NO);
				} else if (!MathUtilities.areValuesInRange(valueMap.get(id),
						minValueMap.get(id), maxValueMap.get(id))) {
					stringColumnValues.get(2).add(ChartConstants.WARNING);
				} else {
					stringColumnValues.get(2).add(ChartConstants.YES);
				}

				if (!nonNullTemperatures.isEmpty()) {
					doubleColumnValues.get(0).add(
							Collections.min(nonNullTemperatures));
					doubleColumnValues.get(1).add(
							Collections.max(nonNullTemperatures));
				} else {
					doubleColumnValues.get(0).add(null);
					doubleColumnValues.get(1).add(null);
				}

				if (!nonNullPHs.isEmpty()) {
					doubleColumnValues.get(2).add(Collections.min(nonNullPHs));
					doubleColumnValues.get(3).add(Collections.max(nonNullPHs));
				} else {
					doubleColumnValues.get(2).add(null);
					doubleColumnValues.get(3).add(null);
				}

				if (!nonNullWaterActivites.isEmpty()) {
					doubleColumnValues.get(4).add(
							Collections.min(nonNullWaterActivites));
					doubleColumnValues.get(5).add(
							Collections.max(nonNullWaterActivites));
				} else {
					doubleColumnValues.get(4).add(null);
					doubleColumnValues.get(5).add(null);
				}

				doubleColumnValues.get(6).add(rmsMap.get(id));
				doubleColumnValues.get(7).add(rSquaredMap.get(id));
				colorCounts.add(plotable.getNumberOfCombinations());
			} else if (getNodeModel().isModel2Schema()) {
				if (!plotable.isPlotable()) {
					stringColumnValues.get(2).add(ChartConstants.NO);
				} else if (!MathUtilities.areValuesInRange(valueMap.get(id),
						minValueMap.get(id), maxValueMap.get(id))) {
					stringColumnValues.get(2).add(ChartConstants.WARNING);
				} else {
					stringColumnValues.get(2).add(ChartConstants.YES);
				}

				doubleColumnValues.get(0).add(rmsMap.get(id));
				doubleColumnValues.get(1).add(rSquaredMap.get(id));
			}

			plotables.put(id, plotable);
		}
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
		infoPanel.showID(selectionPanel.getFocusedID());

		if (configPanel.isDisplayFocusedRow()) {
			createChart();
		}
	}

}
