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
import java.awt.GridLayout;
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

import org.knime.core.node.NodeView;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.ModelCombiner;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.chart.ChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartInfoPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartSamplePanel;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;

/**
 * <code>NodeView</code> for the "PredictorView" Node.
 * 
 * 
 * @author Christian Thoens
 */
public class PredictorViewNodeView extends NodeView<PredictorViewNodeModel>
		implements ChartSelectionPanel.SelectionListener,
		ChartConfigPanel.ConfigListener, ChartSamplePanel.EditListener {

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
	private ChartSamplePanel samplePanel;

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link PredictorViewNodeModel})
	 */
	protected PredictorViewNodeView(final PredictorViewNodeModel nodeModel) {
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
					ChartConfigPanel.PARAMETER_FIELDS, true);
			configPanel.addConfigListener(this);
			selectionPanel = new ChartSelectionPanel(ids, true, stringColumns,
					stringColumnValues, doubleColumns, doubleColumnValues,
					visibleColumns, stringColumns);
			selectionPanel.addSelectionListener(this);
			chartCreator = new ChartCreator(plotables, shortLegend, longLegend);
			infoPanel = new ChartInfoPanel(ids, infoParameters,
					infoParameterValues);
			samplePanel = new ChartSamplePanel();
			samplePanel.setTimeColumnName(AttributeUtilities
					.getNameWithUnit(AttributeUtilities.TIME, null));
			samplePanel.setLogcColumnName(AttributeUtilities
					.getNameWithUnit(AttributeUtilities.LOGC, null));
			samplePanel.addEditListener(this);

			JPanel upperRightPanel = new JPanel();

			upperRightPanel.setLayout(new GridLayout(2, 1));
			upperRightPanel.add(selectionPanel);
			upperRightPanel.add(samplePanel);

			JSplitPane upperSplitPane = new JSplitPane(
					JSplitPane.HORIZONTAL_SPLIT, chartCreator, upperRightPanel);
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
				Double min = plotable.getMinArguments().get(var);
				Double max = plotable.getMaxArguments().get(var);

				if (plotable.getValueList(var) != null) {
					Set<Double> valuesSet = new LinkedHashSet<Double>(
							plotable.getValueList(var));
					List<Double> valuesList = new ArrayList<Double>(valuesSet);

					Collections.sort(valuesList);
					variables.put(var, valuesList);
				} else if (min != null) {
					variables.put(var,
							new ArrayList<Double>(Arrays.asList(min)));
				} else if (max != null) {
					variables.put(var,
							new ArrayList<Double>(Arrays.asList(max)));
				} else {
					variables.put(var, new ArrayList<Double>());
				}
			}

			configPanel.setParamsX(variables, plotable.getMinArguments(),
					plotable.getMaxArguments(), AttributeUtilities.TIME);
			configPanel.setParamsY(Arrays.asList(plotable.getFunctionValue()));
			plotable.setSamples(samplePanel.getTimeValues());
			plotable.setFunctionArguments(configPanel.getParamsXValues());
			chartCreator.setParamX(configPanel.getParamX());
			chartCreator.setParamY(configPanel.getParamY());
			chartCreator.setTransformY(configPanel.getTransformY());

			double[][] samplePoints = plotable.getFunctionSamplePoints(
					AttributeUtilities.TIME, AttributeUtilities.LOGC,
					configPanel.getTransformY(), Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY);

			samplePanel.setDataPoints(samplePoints);
		} else {
			configPanel.setParamsX(null, null, null, null);
			configPanel.setParamsY(null);
			chartCreator.setParamX(null);
			chartCreator.setParamY(null);
			chartCreator.setTransformY(null);
		}

		samplePanel.setLogcColumnName(AttributeUtilities.getNameWithUnit(
				AttributeUtilities.LOGC, configPanel.getTransformY()));
		chartCreator.setUseManualRange(configPanel.isUseManualRange());
		chartCreator.setMinX(configPanel.getMinX());
		chartCreator.setMinY(configPanel.getMinY());
		chartCreator.setMaxX(configPanel.getMaxX());
		chartCreator.setMaxY(configPanel.getMaxY());
		chartCreator.setDrawLines(configPanel.isDrawLines());
		chartCreator.setShowLegend(configPanel.isShowLegend());
		chartCreator.setAddInfoInLegend(configPanel.isAddInfoInLegend());
		chartCreator.setShowConfidenceInterval(configPanel
				.isShowConfidenceInterval());
		chartCreator.setColors(selectionPanel.getColors());
		chartCreator.setShapes(selectionPanel.getShapes());
		chartCreator.createChart(selectedID);
	}

	private void readTable() throws PmmException {
		Set<String> idSet = new LinkedHashSet<String>();
		KnimeRelationReader reader = new KnimeRelationReader(getNodeModel()
				.getSchema(), getNodeModel().getTable());
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>();

		while (reader.hasMoreElements()) {
			tuples.add(reader.nextElement());
		}

		Map<String, String> initParams = getNodeModel()
				.getConcentrationParameters();

		if (getNodeModel().isModel12Schema()) {
			Map<KnimeTuple, List<KnimeTuple>> combinedTuples = ModelCombiner
					.combine(tuples, getNodeModel().getSchema(), false,
							initParams);

			tuples = new ArrayList<KnimeTuple>(combinedTuples.keySet());

			for (KnimeTuple tuple : tuples) {
				List<KnimeTuple> usedTuples = combinedTuples.get(tuple);

				if (!usedTuples.isEmpty()) {
					String oldID = ((CatalogModelXml) usedTuples.get(0)
							.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0))
							.getID() + "";
					String newID = ((CatalogModelXml) tuple.getPmmXml(
							Model1Schema.ATT_MODELCATALOG).get(0)).getID()
							+ "";

					if (initParams.containsKey(oldID)) {
						initParams.put(newID, initParams.get(oldID));
						initParams.remove(oldID);
					}
				}
			}
		}

		ids = new ArrayList<String>();
		plotables = new LinkedHashMap<String, Plotable>();
		infoParameters = new ArrayList<List<String>>();
		infoParameterValues = new ArrayList<List<?>>();
		shortLegend = new LinkedHashMap<String, String>();
		longLegend = new LinkedHashMap<String, String>();
		visibleColumns = Arrays.asList(Model1Schema.RMS, Model1Schema.RSQUARED);
		stringColumns = Arrays.asList(Model1Schema.MODELNAME,
				ChartConstants.IS_FITTED);
		stringColumnValues = new ArrayList<List<String>>();
		stringColumnValues.add(new ArrayList<String>());
		stringColumnValues.add(new ArrayList<String>());
		doubleColumns = Arrays.asList(Model1Schema.RMS, Model1Schema.RSQUARED,
				Model1Schema.AIC, Model1Schema.BIC);
		doubleColumnValues = new ArrayList<List<Double>>();
		doubleColumnValues.add(new ArrayList<Double>());
		doubleColumnValues.add(new ArrayList<Double>());
		doubleColumnValues.add(new ArrayList<Double>());
		doubleColumnValues.add(new ArrayList<Double>());
		visibleColumns = Arrays.asList(Model1Schema.MODELNAME,
				Model1Schema.RMS, Model1Schema.RSQUARED);

		for (KnimeTuple row : tuples) {
			String id = ((EstModelXml) row.getPmmXml(Model1Schema.ATT_ESTMODEL)
					.get(0)).getID() + "";

			if (!idSet.add(id)) {
				continue;
			}

			ids.add(id);

			PmmXmlDoc modelXml = row.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			String modelID = ((CatalogModelXml) modelXml.get(0)).getID() + "";
			String modelName = ((CatalogModelXml) modelXml.get(0)).getName();
			String formula = ((CatalogModelXml) modelXml.get(0)).getFormula();
			String depVar = ((DepXml) row.getPmmXml(Model1Schema.ATT_DEPENDENT)
					.get(0)).getName();
			PmmXmlDoc indepXml = row.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			PmmXmlDoc paramXml = row.getPmmXml(Model1Schema.ATT_PARAMETER);
			List<Double> paramValues = new ArrayList<Double>();
			List<Double> paramMinValues = new ArrayList<Double>();
			List<Double> paramMaxValues = new ArrayList<Double>();
			Map<String, List<Double>> variables = new LinkedHashMap<String, List<Double>>();
			Map<String, Double> varMin = new LinkedHashMap<String, Double>();
			Map<String, Double> varMax = new LinkedHashMap<String, Double>();
			Map<String, Double> parameters = new LinkedHashMap<String, Double>();
			Map<String, Map<String, Double>> covariances = new LinkedHashMap<String, Map<String, Double>>();
			List<String> infoParams = null;
			List<Object> infoValues = null;
			String initParam = initParams.get(modelID);

			Plotable plotable = new Plotable(Plotable.FUNCTION_SAMPLE);

			for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
				IndepXml element = (IndepXml) el;

				variables.put(element.getName(), new ArrayList<Double>());
				varMin.put(element.getName(), element.getMin());
				varMax.put(element.getName(), element.getMax());
			}

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml element = (ParamXml) el;

				if (element.getName().equals(initParam)) {
					variables.put(element.getName(), new ArrayList<Double>());
					varMin.put(element.getName(), element.getMin());
					varMax.put(element.getName(), element.getMax());

					if (element.getValue() != null) {
						plotable.addValueList(
								element.getName(),
								new ArrayList<>(Arrays.asList(element
										.getValue())));
					} else {
						plotable.addValueList(element.getName(),
								new ArrayList<Double>());
					}
				} else {
					parameters.put(element.getName(), element.getValue());
					paramValues.add(element.getValue());
					paramMinValues.add(element.getMin());
					paramMaxValues.add(element.getMax());
				}

				if (initParam == null) {
					Map<String, Double> cov = new LinkedHashMap<String, Double>();

					for (PmmXmlElementConvertable el2 : paramXml
							.getElementSet()) {
						cov.put(((ParamXml) el2).getName(), element
								.getCorrelation(((ParamXml) el2).getOrigName()));
					}

					covariances.put(element.getName(), cov);
				}
			}

			PmmXmlDoc estModelXml = row.getPmmXml(Model1Schema.ATT_ESTMODEL);

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

			plotable.setFunction(formula);
			plotable.setFunctionValue(depVar);
			plotable.setFunctionArguments(variables);
			plotable.setMinArguments(varMin);
			plotable.setMaxArguments(varMax);
			plotable.setFunctionParameters(parameters);
			plotable.setCovariances(covariances);
			plotable.setDegreesOfFreedom(((EstModelXml) estModelXml.get(0))
					.getDOF());

			if (!plotable.isPlotable()) {
				stringColumnValues.get(1).add(ChartConstants.NO);
			} else if (!MathUtilities.areValuesInRange(paramValues,
					paramMinValues, paramMaxValues)) {
				stringColumnValues.get(1).add(ChartConstants.WARNING);
			} else {
				stringColumnValues.get(1).add(ChartConstants.YES);
			}

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml element = (ParamXml) el;

				if (element.getName().equals(initParam)) {
					continue;
				}

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

	@Override
	public void timeValuesChanged() {
		createChart();
	}

}
