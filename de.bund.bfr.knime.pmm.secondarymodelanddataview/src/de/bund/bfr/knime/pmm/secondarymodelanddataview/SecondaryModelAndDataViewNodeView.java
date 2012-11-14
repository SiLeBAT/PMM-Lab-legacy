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
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.knime.core.data.DataTable;
import org.knime.core.node.NodeView;

import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartInfoPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
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
		ChartSelectionPanel.SelectionListener, ChartConfigPanel.ConfigListener {

	private List<String> ids;
	private List<Integer> colorCounts;
	private Map<String, Plotable> plotables;
	private List<String> stringColumns;
	private List<List<String>> stringColumnValues;
	private List<String> doubleColumns;
	private List<List<Double>> doubleColumnValues;
	private List<String> visibleColumns;
	private List<String> filterableStringColumns;
	private List<List<String>> infoParameters;
	private List<List<?>> infoParameterValues;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;

	private ChartCreator chartCreator;
	private ChartSelectionPanel selectionPanel;
	private ChartConfigPanel configPanel;
	private ChartInfoPanel infoPanel;

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
				configPanel = new ChartConfigPanel(
						ChartConfigPanel.PARAMETER_BOXES, false);
				selectionPanel = new ChartSelectionPanel(ids, true,
						stringColumns, stringColumnValues, doubleColumns,
						doubleColumnValues, visibleColumns,
						filterableStringColumns, colorCounts);
			} else if (getNodeModel().isModel2Schema()) {
				configPanel = new ChartConfigPanel(
						ChartConfigPanel.PARAMETER_FIELDS, false);
				selectionPanel = new ChartSelectionPanel(ids, true,
						stringColumns, stringColumnValues, doubleColumns,
						doubleColumnValues, visibleColumns,
						filterableStringColumns);
			}

			configPanel.addConfigListener(this);
			selectionPanel.addSelectionListener(this);
			chartCreator = new ChartCreator(plotables, shortLegend, longLegend);
			infoPanel = new ChartInfoPanel(ids, infoParameters,
					infoParameterValues);

			JSplitPane upperSplitPane = new JSplitPane(
					JSplitPane.HORIZONTAL_SPLIT, chartCreator, selectionPanel);
			JPanel bottomPanel = new JPanel();

			upperSplitPane.setResizeWeight(1.0);
			bottomPanel.setLayout(new BorderLayout());
			bottomPanel.add(configPanel, BorderLayout.WEST);
			bottomPanel.add(infoPanel, BorderLayout.CENTER);
			bottomPanel.setMinimumSize(bottomPanel.getPreferredSize());

			JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
					upperSplitPane, bottomPanel);
			Dimension preferredSize = splitPane.getPreferredSize();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			preferredSize.width = Math.min(preferredSize.width,
					(int) (screenSize.width * 0.9));
			preferredSize.height = Math.min(preferredSize.height,
					(int) (screenSize.height * 0.9));

			splitPane.setResizeWeight(1.0);
			splitPane.setPreferredSize(preferredSize);
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
			Map<String, List<Double>> variables = new LinkedHashMap<String, List<Double>>();

			for (String var : plotable.getFunctionArguments().keySet()) {
				if (getNodeModel().isSeiSchema()) {
					Set<Double> valuesSet = new LinkedHashSet<Double>(
							plotable.getValueList(var));

					valuesSet.remove(null);

					List<Double> valuesList = new ArrayList<Double>(valuesSet);

					Collections.sort(valuesList);
					variables.put(var, valuesList);
				} else if (getNodeModel().isModel2Schema()) {
					variables.put(var, new ArrayList<Double>());
				}
			}

			configPanel.setParamsX(variables);
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
		Set<String> idSet = new LinkedHashSet<String>();
		KnimeRelationReader reader = new KnimeRelationReader(getNodeModel()
				.getSchema(), getNodeModel().getTable());
		Map<String, String> formulaMap = new LinkedHashMap<String, String>();
		Map<String, PmmXmlDoc> paramMap = new LinkedHashMap<String, PmmXmlDoc>();
		Map<String, String> depVarMap = new LinkedHashMap<String, String>();
		Map<String, PmmXmlDoc> indepVarMap = new LinkedHashMap<String, PmmXmlDoc>();
		Map<String, List<Double>> depVarDataMap = new LinkedHashMap<String, List<Double>>();
		Map<String, List<Double>> temperatureDataMap = new LinkedHashMap<String, List<Double>>();
		Map<String, List<Double>> phDataMap = new LinkedHashMap<String, List<Double>>();
		Map<String, List<Double>> awDataMap = new LinkedHashMap<String, List<Double>>();
		Map<String, Map<String, List<Double>>> miscDataMaps = new LinkedHashMap<String, Map<String, List<Double>>>();
		Map<String, Double> rmsMap = new LinkedHashMap<String, Double>();
		Map<String, Double> rSquaredMap = new LinkedHashMap<String, Double>();
		Map<String, Double> aicMap = new LinkedHashMap<String, Double>();
		Map<String, Double> bicMap = new LinkedHashMap<String, Double>();
		List<String> miscParams = null;

		ids = new ArrayList<String>();
		plotables = new LinkedHashMap<String, Plotable>();
		infoParameters = new ArrayList<List<String>>();
		infoParameterValues = new ArrayList<List<?>>();
		shortLegend = new LinkedHashMap<String, String>();
		longLegend = new LinkedHashMap<String, String>();
		stringColumns = Arrays.asList(Model1Schema.ATT_DEPENDENT,
				Model1Schema.ATT_MODELNAME, ChartConstants.IS_FITTED);
		filterableStringColumns = Arrays.asList(ChartConstants.IS_FITTED);
		stringColumnValues = new ArrayList<List<String>>();
		stringColumnValues.add(new ArrayList<String>());
		stringColumnValues.add(new ArrayList<String>());
		stringColumnValues.add(new ArrayList<String>());
		visibleColumns = Arrays.asList(Model1Schema.ATT_DEPENDENT,
				Model1Schema.ATT_MODELNAME, Model2Schema.ATT_RMS,
				Model2Schema.ATT_RSQUARED);

		if (getNodeModel().isSeiSchema()) {
			miscParams = getAllMiscParams(getNodeModel().getTable());
			doubleColumns = new ArrayList<String>(Arrays.asList(
					Model2Schema.ATT_RMS, Model2Schema.ATT_RSQUARED,
					Model2Schema.ATT_AIC, Model2Schema.ATT_BIC, "Min "
							+ TimeSeriesSchema.ATT_TEMPERATURE, "Max "
							+ TimeSeriesSchema.ATT_TEMPERATURE, "Min "
							+ TimeSeriesSchema.ATT_PH, "Max "
							+ TimeSeriesSchema.ATT_PH, "Min "
							+ TimeSeriesSchema.ATT_WATERACTIVITY, "Max "
							+ TimeSeriesSchema.ATT_WATERACTIVITY));
			doubleColumnValues = new ArrayList<List<Double>>();
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			colorCounts = new ArrayList<Integer>();

			for (String param : miscParams) {
				doubleColumns.add("Min " + param);
				doubleColumns.add("Max " + param);
				doubleColumnValues.add(new ArrayList<Double>());
				doubleColumnValues.add(new ArrayList<Double>());
			}
		} else if (getNodeModel().isModel2Schema()) {
			doubleColumns = Arrays.asList(Model2Schema.ATT_RMS,
					Model2Schema.ATT_RSQUARED, Model2Schema.ATT_AIC,
					Model2Schema.ATT_BIC);
			doubleColumnValues = new ArrayList<List<Double>>();
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
		}

		while (reader.hasMoreElements()) {
			KnimeTuple row = reader.nextElement();
			String id = ((DepXml) row.getPmmXml(Model2Schema.ATT_DEPENDENT)
					.get(0)).getName();

			if (!idSet.contains(id)) {
				String modelNameSec = row.getString(Model2Schema.ATT_MODELNAME);
				String formulaSec = row.getString(Model2Schema.ATT_FORMULA);
				String depVarSec = ((DepXml) row.getPmmXml(
						Model2Schema.ATT_DEPENDENT).get(0)).getName();
				PmmXmlDoc paramXmlSec = row
						.getPmmXml(Model2Schema.ATT_PARAMETER);
				List<String> infoParams = new ArrayList<String>(Arrays.asList(
						Model2Schema.ATT_MODELNAME, Model2Schema.ATT_FORMULA));
				List<Object> infoValues = new ArrayList<Object>(Arrays.asList(
						modelNameSec, formulaSec));

				for (PmmXmlElementConvertable el : paramXmlSec.getElementSet()) {
					ParamXml element = (ParamXml) el;

					infoParams.add(element.getName());
					infoValues.add(element.getValue());
					infoParams.add(element.getName() + ": SE");
					infoValues.add(element.getError());
					infoParams.add(element.getName() + ": t");
					infoValues.add(element.gett());
					infoParams.add(element.getName() + ": Pr > |t|");
					infoValues.add(element.getP());
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
				indepVarMap
						.put(id, row.getPmmXml(Model2Schema.ATT_INDEPENDENT));
				paramMap.put(id, paramXmlSec);
				depVarDataMap.put(id, new ArrayList<Double>());
				rmsMap.put(id, row.getDouble(Model2Schema.ATT_RMS));
				rSquaredMap.put(id, row.getDouble(Model2Schema.ATT_RSQUARED));
				aicMap.put(id, row.getDouble(Model2Schema.ATT_AIC));
				bicMap.put(id, row.getDouble(Model2Schema.ATT_BIC));

				if (getNodeModel().isSeiSchema()) {
					temperatureDataMap.put(id, new ArrayList<Double>());
					phDataMap.put(id, new ArrayList<Double>());
					awDataMap.put(id, new ArrayList<Double>());
					miscDataMaps.put(id,
							new LinkedHashMap<String, List<Double>>());

					for (String param : miscParams) {
						miscDataMaps.get(id)
								.put(param, new ArrayList<Double>());
					}
				}
			}

			if (getNodeModel().isSeiSchema()) {
				PmmXmlDoc paramXml = row.getPmmXml(Model1Schema.ATT_PARAMETER);
				String depVar = depVarMap.get(id);
				int depVarIndex = CellIO.getNameList(paramXml).indexOf(depVar);
				Double depVarValue = ((ParamXml) paramXml.get(depVarIndex))
						.getValue();

				depVarDataMap.get(id).add(depVarValue);
				temperatureDataMap.get(id).add(
						row.getDouble(TimeSeriesSchema.ATT_TEMPERATURE));
				phDataMap.get(id).add(row.getDouble(TimeSeriesSchema.ATT_PH));
				awDataMap.get(id).add(
						row.getDouble(TimeSeriesSchema.ATT_WATERACTIVITY));

				PmmXmlDoc misc = row.getPmmXml(TimeSeriesSchema.ATT_MISC);

				for (String param : miscParams) {
					Double paramValue = null;

					for (PmmXmlElementConvertable el : misc.getElementSet()) {
						MiscXml element = (MiscXml) el;

						if (param.equals(element.getName())) {
							paramValue = element.getValue();
							break;
						}
					}

					miscDataMaps.get(id).get(param).add(paramValue);
				}
			}
		}

		for (String id : ids) {
			Plotable plotable = null;
			Map<String, List<Double>> arguments = new LinkedHashMap<String, List<Double>>();
			Map<String, Double> minArg = new LinkedHashMap<String, Double>();
			Map<String, Double> maxArg = new LinkedHashMap<String, Double>();
			Map<String, Double> constants = new LinkedHashMap<String, Double>();
			List<Double> paramValues = new ArrayList<Double>();
			List<Double> minParamValues = new ArrayList<Double>();
			List<Double> maxParamValues = new ArrayList<Double>();
			boolean hasArguments = !indepVarMap.get(id).getElementSet()
					.isEmpty();

			if (getNodeModel().isSeiSchema()) {
				plotable = new Plotable(Plotable.BOTH_STRICT);
			} else if (getNodeModel().isModel2Schema()) {
				plotable = new Plotable(Plotable.FUNCTION);
			}

			for (PmmXmlElementConvertable el : indepVarMap.get(id)
					.getElementSet()) {
				IndepXml element = (IndepXml) el;

				arguments.put(element.getName(),
						new ArrayList<Double>(Arrays.asList(0.0)));
				minArg.put(element.getName(), element.getMin());
				maxArg.put(element.getName(), element.getMax());
			}

			for (PmmXmlElementConvertable el : paramMap.get(id).getElementSet()) {
				ParamXml element = (ParamXml) el;

				constants.put(element.getName(), element.getValue());
				paramValues.add(element.getValue());
				minParamValues.add(element.getMin());
				maxParamValues.add(element.getMax());
			}

			plotable.setFunction(formulaMap.get(id));
			plotable.setFunctionValue(depVarMap.get(id));
			plotable.setFunctionArguments(arguments);
			plotable.setMinArguments(minArg);
			plotable.setMaxArguments(maxArg);
			plotable.setFunctionParameters(constants);

			if (getNodeModel().isSeiSchema()) {
				List<Double> depVarData = depVarDataMap.get(id);
				List<Double> temperatures = temperatureDataMap.get(id);
				List<Double> phs = phDataMap.get(id);
				List<Double> aws = awDataMap.get(id);
				Map<String, List<Double>> miscs = miscDataMaps.get(id);

				for (int i = 0; i < depVarData.size(); i++) {
					if (depVarData.get(i) == null) {
						depVarData.remove(i);
						temperatures.remove(i);
						phs.remove(i);
						aws.remove(i);

						for (String param : miscParams) {
							miscs.get(param).remove(i);
						}
					}
				}

				plotable.addValueList(depVarMap.get(id), depVarData);
				plotable.addValueList(TimeSeriesSchema.ATT_TEMPERATURE,
						temperatures);
				plotable.addValueList(TimeSeriesSchema.ATT_PH, phs);
				plotable.addValueList(TimeSeriesSchema.ATT_WATERACTIVITY, aws);

				for (String param : miscParams) {
					plotable.addValueList(param, miscs.get(param));
				}

				List<Double> nonNullTemperatures = new ArrayList<Double>(
						temperatures);
				List<Double> nonNullPHs = new ArrayList<Double>(phs);
				List<Double> nonNullWaterActivites = new ArrayList<Double>(aws);

				nonNullTemperatures.removeAll(Arrays.asList((Double) null));
				nonNullPHs.removeAll(Arrays.asList((Double) null));
				nonNullWaterActivites.removeAll(Arrays.asList((Double) null));

				if (!hasArguments) {
					if (!nonNullTemperatures.isEmpty()) {
						plotable.getFunctionArguments().put(
								TimeSeriesSchema.ATT_TEMPERATURE,
								new ArrayList<Double>(Arrays.asList(0.0)));
					}

					if (!nonNullPHs.isEmpty()) {
						plotable.getFunctionArguments().put(
								TimeSeriesSchema.ATT_PH,
								new ArrayList<Double>(Arrays.asList(0.0)));
					}

					if (!nonNullWaterActivites.isEmpty()) {
						plotable.getFunctionArguments().put(
								TimeSeriesSchema.ATT_WATERACTIVITY,
								new ArrayList<Double>(Arrays.asList(0.0)));
					}
				}

				if (!plotable.isPlotable()) {
					stringColumnValues.get(2).add(ChartConstants.NO);
				} else if (!MathUtilities.areValuesInRange(paramValues,
						minParamValues, maxParamValues)) {
					stringColumnValues.get(2).add(ChartConstants.WARNING);
				} else {
					stringColumnValues.get(2).add(ChartConstants.YES);
				}

				doubleColumnValues.get(0).add(rmsMap.get(id));
				doubleColumnValues.get(1).add(rSquaredMap.get(id));
				doubleColumnValues.get(2).add(bicMap.get(id));
				doubleColumnValues.get(3).add(aicMap.get(id));

				if (!nonNullTemperatures.isEmpty()) {
					doubleColumnValues.get(4).add(
							Collections.min(nonNullTemperatures));
					doubleColumnValues.get(5).add(
							Collections.max(nonNullTemperatures));
				} else {
					doubleColumnValues.get(4).add(null);
					doubleColumnValues.get(5).add(null);
				}

				if (!nonNullPHs.isEmpty()) {
					doubleColumnValues.get(6).add(Collections.min(nonNullPHs));
					doubleColumnValues.get(7).add(Collections.max(nonNullPHs));
				} else {
					doubleColumnValues.get(6).add(null);
					doubleColumnValues.get(7).add(null);
				}

				if (!nonNullWaterActivites.isEmpty()) {
					doubleColumnValues.get(8).add(
							Collections.min(nonNullWaterActivites));
					doubleColumnValues.get(9).add(
							Collections.max(nonNullWaterActivites));
				} else {
					doubleColumnValues.get(8).add(null);
					doubleColumnValues.get(9).add(null);
				}

				for (int i = 0; i < miscParams.size(); i++) {
					List<Double> nonNullValues = new ArrayList<Double>(
							miscs.get(miscParams.get(i)));

					nonNullValues.removeAll(Arrays.asList((Double) null));

					if (!nonNullValues.isEmpty()) {
						if (!hasArguments) {
							plotable.getFunctionArguments().put(
									miscParams.get(i),
									new ArrayList<Double>(Arrays.asList(0.0)));
						}

						doubleColumnValues.get(2 * i + 10).add(
								Collections.min(nonNullValues));
						doubleColumnValues.get(2 * i + 11).add(
								Collections.max(nonNullValues));
					} else {
						doubleColumnValues.get(2 * i + 10).add(null);
						doubleColumnValues.get(2 * i + 11).add(null);
					}
				}

				colorCounts.add(plotable.getNumberOfCombinations());
			} else if (getNodeModel().isModel2Schema()) {
				if (!plotable.isPlotable()) {
					stringColumnValues.get(2).add(ChartConstants.NO);
				} else if (!MathUtilities.areValuesInRange(paramValues,
						minParamValues, maxParamValues)) {
					stringColumnValues.get(2).add(ChartConstants.WARNING);
				} else {
					stringColumnValues.get(2).add(ChartConstants.YES);
				}

				doubleColumnValues.get(0).add(rmsMap.get(id));
				doubleColumnValues.get(1).add(rSquaredMap.get(id));
				doubleColumnValues.get(2).add(bicMap.get(id));
				doubleColumnValues.get(3).add(aicMap.get(id));

				if (!hasArguments) {
					plotable.getFunctionArguments().put("No argument",
							new ArrayList<Double>(Arrays.asList(0.0)));
				}
			}

			plotables.put(id, plotable);
		}
	}

	private List<String> getAllMiscParams(DataTable table) throws PmmException {
		KnimeRelationReader reader = new KnimeRelationReader(
				new TimeSeriesSchema(), table);
		Set<String> paramSet = new LinkedHashSet<String>();

		while (reader.hasMoreElements()) {
			KnimeTuple tuple = reader.nextElement();
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				paramSet.add(element.getName());
			}
		}

		return new ArrayList<String>(paramSet);
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
