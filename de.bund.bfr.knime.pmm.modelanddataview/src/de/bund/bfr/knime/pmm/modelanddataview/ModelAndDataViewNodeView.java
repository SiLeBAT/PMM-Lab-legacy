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
package de.bund.bfr.knime.pmm.modelanddataview;

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
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * <code>NodeView</code> for the "ModelAndDataView" Node.
 * 
 * 
 * @author Christian Thoens
 */
public class ModelAndDataViewNodeView extends
		NodeView<ModelAndDataViewNodeModel> implements
		DataAndModelSelectionPanel.SelectionListener,
		DataAndModelChartConfigPanel.ConfigListener {

	private List<String> ids;
	private Map<String, Plotable> plotables;
	private List<String> stringColumns;
	private List<List<String>> stringColumnValues;
	private List<String> doubleColumns;
	private List<List<Double>> doubleColumnValues;
	private List<List<String>> infoParameters;
	private List<List<String>> infoParameterValues;
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
	 *            The model (class: {@link ModelAndDataViewNodeModel})
	 */
	protected ModelAndDataViewNodeView(final ModelAndDataViewNodeModel nodeModel) {
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

			configPanel = new DataAndModelChartConfigPanel(
					DataAndModelChartConfigPanel.PARAMETER_FIELDS);
			configPanel.addConfigListener(this);
			selectionPanel = new DataAndModelSelectionPanel(ids, true,
					stringColumns, stringColumnValues, doubleColumns,
					doubleColumnValues, Arrays.asList(true, true, false),
					Arrays.asList(true, true, true));
			selectionPanel.addSelectionListener(this);
			chartCreator = new DataAndModelChartCreator(plotables, shortLegend,
					longLegend);
			infoPanel = new DataAndModelChartInfoPanel(ids, infoParameters,
					infoParameterValues);

			JSplitPane upperSplitPane = new JSplitPane(
					JSplitPane.HORIZONTAL_SPLIT, chartCreator, selectionPanel);
			JPanel bottomPanel = new JPanel();

			bottomPanel.setLayout(new BorderLayout());
			bottomPanel.add(configPanel, BorderLayout.WEST);
			bottomPanel.add(infoPanel, BorderLayout.CENTER);

			setComponent(new JSplitPane(JSplitPane.VERTICAL_SPLIT,
					upperSplitPane, bottomPanel));
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
			List<List<Double>> possibleValues = new ArrayList<List<Double>>();

			for (String var : variables) {
				if (plotable.getValueList(var) != null) {
					Set<Double> valuesSet = new HashSet<Double>(
							plotable.getValueList(var));
					List<Double> valuesList = new ArrayList<Double>(valuesSet);

					Collections.sort(valuesList);
					possibleValues.add(valuesList);
				} else {
					possibleValues.add(null);
				}
			}

			configPanel.setParamsX(variables, possibleValues);
			configPanel.setParamsY(Arrays.asList(plotable.getFunctionValue()));
			plotable.setFunctionArguments(configPanel.getParamsXValues());
			chartCreator.setParamX(configPanel.getParamX());
			chartCreator.setParamY(configPanel.getParamY());
			chartCreator.setTransformY(configPanel.getTransformY());
		} else {
			configPanel.setParamsX(null);
			configPanel.setParamsY(null);
			chartCreator.setParamX(null);
			chartCreator.setParamY(null);
			chartCreator.setTransformY(null);
		}

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
		chartCreator.createChart(selectedID);
	}

	private void readTable() throws PmmException {
		Set<String> idSet = new HashSet<String>();
		KnimeRelationReader reader = new KnimeRelationReader(getNodeModel()
				.getSchema(), getNodeModel().getTable());

		ids = new ArrayList<String>();
		plotables = new HashMap<String, Plotable>();
		infoParameters = new ArrayList<List<String>>();
		infoParameterValues = new ArrayList<List<String>>();
		shortLegend = new HashMap<String, String>();
		longLegend = new HashMap<String, String>();

		if (getNodeModel().isPeiSchema()) {
			stringColumns = Arrays.asList(Model1Schema.ATT_MODELNAME,
					"Data ID", ChartConstants.IS_FITTED);
			stringColumnValues = new ArrayList<List<String>>();
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			doubleColumns = Arrays.asList(TimeSeriesSchema.ATT_TEMPERATURE,
					TimeSeriesSchema.ATT_PH,
					TimeSeriesSchema.ATT_WATERACTIVITY, Model1Schema.ATT_RMS,
					Model1Schema.ATT_RSQUARED);
			doubleColumnValues = new ArrayList<List<Double>>();
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
		} else if (getNodeModel().isModel1Schema()) {
			stringColumns = Arrays.asList(Model1Schema.ATT_MODELNAME,
					ChartConstants.IS_FITTED);
			stringColumnValues = new ArrayList<List<String>>();
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			doubleColumns = Arrays.asList(Model1Schema.ATT_RMS,
					Model1Schema.ATT_RSQUARED);
			doubleColumnValues = new ArrayList<List<Double>>();
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
		}

		while (reader.hasMoreElements()) {
			KnimeTuple row = reader.nextElement();
			String id = null;

			if (getNodeModel().isPeiSchema()) {
				id = row.getInt(Model1Schema.ATT_ESTMODELID) + "("
						+ row.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			} else if (getNodeModel().isModel1Schema()) {
				id = row.getInt(Model1Schema.ATT_ESTMODELID) + "";
			}

			if (!idSet.add(id)) {
				continue;
			}

			ids.add(id);

			String modelName = row.getString(Model1Schema.ATT_MODELNAME);
			String formula = row.getString(Model1Schema.ATT_FORMULA);
			String depVar = row.getString(Model1Schema.ATT_DEPVAR);
			List<String> indepVars = row
					.getStringList(Model1Schema.ATT_INDEPVAR);
			List<String> params = row.getStringList(Model1Schema.ATT_PARAMNAME);
			List<Double> paramValues = row
					.getDoubleList(Model1Schema.ATT_VALUE);
			List<Double> paramMinValues = row
					.getDoubleList(Model1Schema.ATT_MINVALUE);
			List<Double> paramMaxValues = row
					.getDoubleList(Model1Schema.ATT_MAXVALUE);

			Plotable plotable = null;
			Map<String, Double> variables = new HashMap<String, Double>();
			Map<String, Double> parameters = new HashMap<String, Double>();

			for (String iv : indepVars) {
				variables.put(iv, 0.0);
			}

			for (int i = 0; i < params.size(); i++) {
				parameters.put(params.get(i), paramValues.get(i));
			}

			if (getNodeModel().isPeiSchema()) {
				Double temperature = row
						.getDouble(TimeSeriesSchema.ATT_TEMPERATURE);
				Double ph = row.getDouble(TimeSeriesSchema.ATT_PH);
				Double waterActivity = row
						.getDouble(TimeSeriesSchema.ATT_WATERACTIVITY);
				List<Double> timeList = row
						.getDoubleList(TimeSeriesSchema.ATT_TIME);
				List<Double> logcList = row
						.getDoubleList(TimeSeriesSchema.ATT_LOGC);
				int n = 1;

				plotable = new Plotable(Plotable.BOTH);

				if (!timeList.isEmpty() && !logcList.isEmpty()) {
					n = timeList.size();
					plotable.addValueList(TimeSeriesSchema.ATT_TIME, timeList);
					plotable.addValueList(TimeSeriesSchema.ATT_LOGC, logcList);
				}

				if (temperature != null) {
					plotable.addValueList(TimeSeriesSchema.ATT_TEMPERATURE,
							Collections.nCopies(n, temperature));
				}

				if (ph != null) {
					plotable.addValueList(TimeSeriesSchema.ATT_PH,
							Collections.nCopies(n, ph));
				}

				if (waterActivity != null) {
					plotable.addValueList(TimeSeriesSchema.ATT_WATERACTIVITY,
							Collections.nCopies(n, waterActivity));
				}
			} else if (getNodeModel().isModel1Schema()) {
				plotable = new Plotable(Plotable.FUNCTION);
			}

			plotable.setFunction(formula);
			plotable.setFunctionValue(depVar);
			plotable.setFunctionArguments(variables);
			plotable.setFunctionConstants(parameters);
			plotables.put(id, plotable);

			List<String> infoParams = null;
			List<String> infoValues = null;

			if (getNodeModel().isPeiSchema()) {
				String dataName;
				String agent;
				String matrix;

				if (row.getString(TimeSeriesSchema.ATT_COMBASEID) != null) {
					dataName = row.getString(TimeSeriesSchema.ATT_COMBASEID);
				} else {
					dataName = "" + row.getInt(TimeSeriesSchema.ATT_CONDID);
				}

				if (row.getString(TimeSeriesSchema.ATT_AGENTNAME) != null) {
					agent = row.getString(TimeSeriesSchema.ATT_AGENTNAME)
							+ " ("
							+ row.getString(TimeSeriesSchema.ATT_AGENTDETAIL)
							+ ")";
				} else {
					agent = row.getString(TimeSeriesSchema.ATT_AGENTDETAIL);
				}

				if (row.getString(TimeSeriesSchema.ATT_MATRIXNAME) != null) {
					matrix = row.getString(TimeSeriesSchema.ATT_MATRIXNAME)
							+ " ("
							+ row.getString(TimeSeriesSchema.ATT_MATRIXDETAIL)
							+ ")";
				} else {
					matrix = row.getString(TimeSeriesSchema.ATT_MATRIXDETAIL);
				}

				shortLegend.put(id, modelName + " (" + dataName + ")");
				longLegend
						.put(id, modelName + " (" + dataName + ") " + formula);
				stringColumnValues.get(0).add(modelName);
				stringColumnValues.get(1).add(dataName);
				doubleColumnValues.get(0).add(
						row.getDouble(TimeSeriesSchema.ATT_TEMPERATURE));
				doubleColumnValues.get(1).add(
						row.getDouble(TimeSeriesSchema.ATT_PH));
				doubleColumnValues.get(2).add(
						row.getDouble(TimeSeriesSchema.ATT_WATERACTIVITY));
				doubleColumnValues.get(3).add(
						row.getDouble(Model1Schema.ATT_RMS));
				doubleColumnValues.get(4).add(
						row.getDouble(Model1Schema.ATT_RSQUARED));
				infoParams = new ArrayList<String>(
						Arrays.asList(Model1Schema.ATT_FORMULA,
								TimeSeriesSchema.ATT_AGENTNAME,
								TimeSeriesSchema.ATT_MATRIXNAME,
								TimeSeriesSchema.ATT_MISC,
								TimeSeriesSchema.ATT_COMMENT));
				infoValues = new ArrayList<String>(Arrays.asList(
						row.getString(Model1Schema.ATT_FORMULA), agent, matrix,
						row.getString(TimeSeriesSchema.ATT_MISC),
						row.getString(TimeSeriesSchema.ATT_COMMENT)));

				if (!plotable.isPlotable()) {
					stringColumnValues.get(2).add(ChartConstants.NO);
				} else if (!MathUtilities.areValuesInRange(paramValues,
						paramMinValues, paramMaxValues)) {
					stringColumnValues.get(2).add(ChartConstants.WARNING);
				} else {
					stringColumnValues.get(2).add(ChartConstants.YES);
				}
			} else if (getNodeModel().isModel1Schema()) {
				shortLegend.put(id, modelName);
				longLegend.put(id, modelName + " " + formula);
				stringColumnValues.get(0).add(modelName);
				doubleColumnValues.get(0).add(
						row.getDouble(Model1Schema.ATT_RMS));
				doubleColumnValues.get(1).add(
						row.getDouble(Model1Schema.ATT_RSQUARED));
				infoParams = new ArrayList<String>(
						Arrays.asList(Model1Schema.ATT_FORMULA));
				infoValues = new ArrayList<String>(Arrays.asList(row
						.getString(Model1Schema.ATT_FORMULA)));

				if (!plotable.isPlotable()) {
					stringColumnValues.get(1).add(ChartConstants.NO);
				} else if (!MathUtilities.areValuesInRange(paramValues,
						paramMinValues, paramMaxValues)) {
					stringColumnValues.get(1).add(ChartConstants.WARNING);
				} else {
					stringColumnValues.get(1).add(ChartConstants.YES);
				}
			}

			infoParams.addAll(params);

			for (Double value : paramValues) {
				infoValues.add("" + value);
			}

			infoParameters.add(infoParams);
			infoParameterValues.add(infoValues);
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
