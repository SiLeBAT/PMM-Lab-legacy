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
package de.bund.bfr.knime.pmm.combinedmodelanddataview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
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

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ModelCombiner;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.QualityMeasurementComputation;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
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
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * <code>NodeView</code> for the "CombinedModelAndDataView" Node.
 * 
 * 
 * @author Christian Thoens
 */
public class CombinedModelAndDataViewNodeView extends
		NodeView<CombinedModelAndDataViewNodeModel> implements
		ChartSelectionPanel.SelectionListener, ChartConfigPanel.ConfigListener {

	private List<String> ids;
	private Map<String, Plotable> plotables;
	private List<String> stringColumns;
	private List<List<String>> stringColumnValues;
	private List<String> doubleColumns;
	private List<List<Double>> doubleColumnValues;
	private List<String> visibleColumns;
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
	 *            The model (class: {@link ModelAndDataViewNodeModel})
	 */
	protected CombinedModelAndDataViewNodeView(
			final CombinedModelAndDataViewNodeModel nodeModel) {
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

			configPanel = new ChartConfigPanel(
					ChartConfigPanel.PARAMETER_FIELDS, false);
			configPanel.addConfigListener(this);
			selectionPanel = new ChartSelectionPanel(ids, true, stringColumns,
					stringColumnValues, doubleColumns, doubleColumnValues,
					visibleColumns, stringColumns);
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
				if (plotable.getValueList(var) != null) {
					Set<Double> valuesSet = new LinkedHashSet<Double>(
							plotable.getValueList(var));
					List<Double> valuesList = new ArrayList<Double>(valuesSet);

					Collections.sort(valuesList);
					variables.put(var, valuesList);
				} else {
					variables.put(var, new ArrayList<Double>());
				}
			}

			configPanel.setParamsX(variables, plotable.getMinArguments(),
					plotable.getMaxArguments(), null);
			configPanel.setParamsY(Arrays.asList(plotable.getFunctionValue()));
			plotable.setFunctionArguments(configPanel.getParamsXValues());
			chartCreator.setParamX(configPanel.getParamX());
			chartCreator.setParamY(configPanel.getParamY());
			chartCreator.setTransformY(configPanel.getTransformY());
		} else {
			configPanel.setParamsX(null, null, null, null);
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
		Set<String> idSet = new LinkedHashSet<String>();
		KnimeRelationReader reader = new KnimeRelationReader(getNodeModel()
				.getSchema(), getNodeModel().getTable());
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>();
		List<String> miscParams = null;

		while (reader.hasMoreElements()) {
			tuples.add(reader.nextElement());
		}

		List<KnimeTuple> combinedTuples = new ArrayList<KnimeTuple>(
				ModelCombiner.combine(tuples, getNodeModel().getSchema(),
						false, null).keySet());
		List<KnimeTuple> newTuples = null;

		if (getNodeModel().isSeiSchema()) {
			newTuples = QualityMeasurementComputation
					.computePrimary(combinedTuples);
		}

		ids = new ArrayList<String>();
		plotables = new LinkedHashMap<String, Plotable>();
		infoParameters = new ArrayList<List<String>>();
		infoParameterValues = new ArrayList<List<?>>();
		shortLegend = new LinkedHashMap<String, String>();
		longLegend = new LinkedHashMap<String, String>();
		visibleColumns = Arrays.asList(Model1Schema.RMS, Model1Schema.RSQUARED);

		if (getNodeModel().isSeiSchema()) {
			miscParams = getAllMiscParams(getNodeModel().getTable());
			stringColumns = Arrays.asList(Model1Schema.MODELNAME,
					TimeSeriesSchema.DATAID, ChartConstants.IS_FITTED);
			stringColumnValues = new ArrayList<List<String>>();
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			doubleColumns = new ArrayList<String>(Arrays.asList(
					Model1Schema.RMS, Model1Schema.RSQUARED, Model1Schema.AIC,
					Model1Schema.BIC, Model1Schema.RMS + "(Data)",
					Model1Schema.RSQUARED + "(Data)", Model1Schema.AIC
							+ "(Data)", Model1Schema.BIC + "(Data)"));
			doubleColumnValues = new ArrayList<List<Double>>();
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			visibleColumns = Arrays.asList(Model1Schema.MODELNAME,
					TimeSeriesSchema.DATAID, Model1Schema.RMS,
					Model1Schema.RSQUARED);

			for (String param : miscParams) {
				doubleColumns.add(param);
				doubleColumnValues.add(new ArrayList<Double>());
			}
		} else if (getNodeModel().isModel12Schema()) {
			stringColumns = Arrays.asList(Model1Schema.MODELNAME,
					ChartConstants.IS_FITTED);
			stringColumnValues = new ArrayList<List<String>>();
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			doubleColumns = Arrays.asList(Model1Schema.RMS,
					Model1Schema.RSQUARED, Model1Schema.AIC, Model1Schema.BIC);
			doubleColumnValues = new ArrayList<List<Double>>();
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			visibleColumns = Arrays.asList(Model1Schema.MODELNAME,
					Model1Schema.RMS, Model1Schema.RSQUARED);
		}

		for (int nr = 0; nr < combinedTuples.size(); nr++) {
			KnimeTuple row = combinedTuples.get(nr);
			String id = ((EstModelXml) row.getPmmXml(Model1Schema.ATT_ESTMODEL)
					.get(0)).getID() + "";

			if (getNodeModel().isSeiSchema()) {
				id += "(" + row.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			}

			if (!idSet.add(id)) {
				continue;
			}

			ids.add(id);

			PmmXmlDoc modelXml = row.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			String modelName = ((CatalogModelXml) modelXml.get(0)).getName();
			String formula = ((CatalogModelXml) modelXml.get(0)).getFormula();
			String depVar = ((DepXml) row.getPmmXml(Model1Schema.ATT_DEPENDENT)
					.get(0)).getName();
			PmmXmlDoc indepXml = row.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			PmmXmlDoc paramXml = row.getPmmXml(Model1Schema.ATT_PARAMETER);
			List<Double> paramValues = new ArrayList<Double>();
			List<Double> paramMinValues = new ArrayList<Double>();
			List<Double> paramMaxValues = new ArrayList<Double>();
			Plotable plotable = new Plotable(Plotable.BOTH);
			Map<String, List<Double>> variables = new LinkedHashMap<String, List<Double>>();
			Map<String, Double> varMin = new LinkedHashMap<String, Double>();
			Map<String, Double> varMax = new LinkedHashMap<String, Double>();
			Map<String, Double> parameters = new LinkedHashMap<String, Double>();
			List<String> infoParams = null;
			List<Object> infoValues = null;

			for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
				IndepXml element = (IndepXml) el;

				variables.put(element.getName(),
						new ArrayList<Double>(Arrays.asList(0.0)));
				varMin.put(element.getName(), element.getMin());
				varMax.put(element.getName(), element.getMax());
			}

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml element = (ParamXml) el;

				parameters.put(element.getName(), element.getValue());
				paramValues.add(element.getValue());
				paramMinValues.add(element.getMin());
				paramMaxValues.add(element.getMax());
			}

			if (getNodeModel().isSeiSchema()) {
				PmmXmlDoc timeSeriesXml = row
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
				List<Point2D.Double> dataPoints = new ArrayList<Point2D.Double>();
				int n = Math.max(1, timeSeriesXml.getElementSet().size());

				plotable = new Plotable(Plotable.BOTH);

				if (!timeSeriesXml.getElementSet().isEmpty()) {
					List<Double> timeList = new ArrayList<Double>();
					List<Double> logcList = new ArrayList<Double>();

					for (PmmXmlElementConvertable el : timeSeriesXml
							.getElementSet()) {
						TimeSeriesXml element = (TimeSeriesXml) el;
						double time = Double.NaN;
						double logc = Double.NaN;

						if (element.getTime() != null) {
							time = element.getTime();
						}

						if (element.getLog10C() != null) {
							logc = element.getLog10C();
						}

						timeList.add(element.getTime());
						logcList.add(element.getLog10C());
						dataPoints.add(new Point2D.Double(time, logc));
					}

					plotable.addValueList(TimeSeriesSchema.TIME, timeList);
					plotable.addValueList(TimeSeriesSchema.LOGC, logcList);
				}

				PmmXmlDoc misc = row.getPmmXml(TimeSeriesSchema.ATT_MISC);

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					if (element.getValue() != null
							&& !element.getValue().isNaN()) {
						plotable.addValueList(element.getName(),
								Collections.nCopies(n, element.getValue()));
					}
				}

				String dataName;
				String agent;
				String matrix;

				PmmXmlDoc agentXml = row.getPmmXml(TimeSeriesSchema.ATT_AGENT);
				String agentName = ((AgentXml) agentXml.get(0)).getName();
				String agentDetail = ((AgentXml) agentXml.get(0)).getDetail();
				PmmXmlDoc matrixXml = row
						.getPmmXml(TimeSeriesSchema.ATT_MATRIX);
				String matrixName = ((MatrixXml) matrixXml.get(0)).getName();
				String matrixDetail = ((MatrixXml) matrixXml.get(0))
						.getDetail();

				if (row.getString(TimeSeriesSchema.ATT_COMBASEID) != null) {
					dataName = row.getString(TimeSeriesSchema.ATT_COMBASEID);
				} else {
					dataName = "" + row.getInt(TimeSeriesSchema.ATT_CONDID);
				}

				if (agentName != null) {
					agent = agentName + " (" + agentDetail + ")";
				} else {
					agent = agentDetail;
				}

				if (matrixName != null) {
					matrix = matrixName + " (" + matrixDetail + ")";
				} else {
					matrix = matrixDetail;
				}

				PmmXmlDoc estModelXml = row
						.getPmmXml(Model1Schema.ATT_ESTMODEL);
				PmmXmlDoc newEstModelXml = newTuples.get(nr).getPmmXml(
						Model1Schema.ATT_ESTMODEL);

				shortLegend.put(id, modelName + " (" + dataName + ")");
				longLegend
						.put(id, modelName + " (" + dataName + ") " + formula);
				stringColumnValues.get(0).add(modelName);
				stringColumnValues.get(1).add(dataName);
				doubleColumnValues.get(0).add(
						((EstModelXml) estModelXml.get(0)).getRMS());
				doubleColumnValues.get(1).add(
						((EstModelXml) estModelXml.get(0)).getR2());
				doubleColumnValues.get(2).add(
						((EstModelXml) estModelXml.get(0)).getAIC());
				doubleColumnValues.get(3).add(
						((EstModelXml) estModelXml.get(0)).getBIC());
				doubleColumnValues.get(4).add(
						((EstModelXml) newEstModelXml.get(0)).getRMS());
				doubleColumnValues.get(5).add(
						((EstModelXml) newEstModelXml.get(0)).getR2());
				doubleColumnValues.get(6).add(
						((EstModelXml) newEstModelXml.get(0)).getAIC());
				doubleColumnValues.get(7).add(
						((EstModelXml) newEstModelXml.get(0)).getBIC());
				infoParams = new ArrayList<String>(Arrays.asList(
						Model1Schema.FORMULA, TimeSeriesSchema.DATAPOINTS,
						TimeSeriesSchema.ATT_AGENT,
						TimeSeriesSchema.ATT_MATRIX,
						TimeSeriesSchema.ATT_COMMENT));
				infoValues = new ArrayList<Object>(Arrays.asList(formula,
						dataPoints, agent, matrix,
						row.getString(TimeSeriesSchema.ATT_COMMENT)));

				for (int i = 0; i < miscParams.size(); i++) {
					boolean paramFound = false;

					for (PmmXmlElementConvertable el : misc.getElementSet()) {
						MiscXml element = (MiscXml) el;

						if (miscParams.get(i).equals(element.getName())) {
							doubleColumnValues.get(i + 8).add(
									element.getValue());
							paramFound = true;
							break;
						}
					}

					if (!paramFound) {
						doubleColumnValues.get(i + 8).add(null);
					}
				}
			} else if (getNodeModel().isModel12Schema()) {
				PmmXmlDoc estModelXml = row
						.getPmmXml(Model1Schema.ATT_ESTMODEL);

				plotable = new Plotable(Plotable.FUNCTION);
				shortLegend.put(id, modelName);
				longLegend.put(id, modelName + " " + formula);
				stringColumnValues.get(0).add(modelName);
				doubleColumnValues.get(0).add(
						((EstModelXml) estModelXml.get(0)).getRMS());
				doubleColumnValues.get(1).add(
						((EstModelXml) estModelXml.get(0)).getR2());
				doubleColumnValues.get(2).add(
						((EstModelXml) estModelXml.get(0)).getAIC());
				doubleColumnValues.get(3).add(
						((EstModelXml) estModelXml.get(0)).getBIC());
				infoParams = new ArrayList<String>(
						Arrays.asList(Model1Schema.FORMULA));
				infoValues = new ArrayList<Object>(Arrays.asList(formula));
			}

			plotable.setFunction(formula);
			plotable.setFunctionValue(depVar);
			plotable.setFunctionArguments(variables);
			plotable.setMinArguments(varMin);
			plotable.setMaxArguments(varMax);
			plotable.setFunctionParameters(parameters);

			if (getNodeModel().isSeiSchema()) {
				if (!plotable.isPlotable()) {
					stringColumnValues.get(2).add(ChartConstants.NO);
				} else if (!MathUtilities.areValuesInRange(paramValues,
						paramMinValues, paramMaxValues)) {
					stringColumnValues.get(2).add(ChartConstants.WARNING);
				} else {
					stringColumnValues.get(2).add(ChartConstants.YES);
				}
			} else if (getNodeModel().isModel12Schema()) {
				if (!plotable.isPlotable()) {
					stringColumnValues.get(1).add(ChartConstants.NO);
				} else if (!MathUtilities.areValuesInRange(paramValues,
						paramMinValues, paramMaxValues)) {
					stringColumnValues.get(1).add(ChartConstants.WARNING);
				} else {
					stringColumnValues.get(1).add(ChartConstants.YES);
				}
			}

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
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

			plotables.put(id, plotable);
			infoParameters.add(infoParams);
			infoParameterValues.add(infoValues);
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
