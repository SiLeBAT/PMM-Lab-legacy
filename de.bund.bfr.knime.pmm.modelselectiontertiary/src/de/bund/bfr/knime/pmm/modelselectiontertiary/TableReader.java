package de.bund.bfr.knime.pmm.modelselectiontertiary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.node.BufferedDataTable;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ModelCombiner;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.QualityMeasurementComputation;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;

public class TableReader {

	private List<String> allIds;
	private List<KnimeTuple> allTuples;
	private Map<KnimeTuple, List<KnimeTuple>> tupleCombinations;

	private List<String> ids;

	private List<String> stringColumns;
	private List<List<String>> stringColumnValues;
	private List<String> doubleColumns;
	private List<List<Double>> doubleColumnValues;
	private List<List<TimeSeriesXml>> data;
	private List<String> formulas;
	private List<Map<String, Double>> parameterData;
	private List<String> conditions;
	private List<List<Double>> conditionValues;
	private List<List<String>> conditionUnits;
	private List<String> standardVisibleColumns;
	private List<String> filterableStringColumns;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;

	public TableReader(BufferedDataTable table, boolean schemaContainsData) {
		List<String> miscParams = null;
		List<KnimeTuple> tertiaryTuples;
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>();
		List<KnimeTuple> newTuples = null;
		List<List<KnimeTuple>> usedTuples = new ArrayList<List<KnimeTuple>>();
		Set<String> idSet = new LinkedHashSet<String>();

		if (schemaContainsData) {
			tertiaryTuples = PmmUtilities.getTuples(table,
					SchemaFactory.createM12DataSchema());
		} else {
			tertiaryTuples = PmmUtilities.getTuples(table,
					SchemaFactory.createM12Schema());
		}

		Map<KnimeTuple, List<KnimeTuple>> combinations = ModelCombiner.combine(
				tertiaryTuples, schemaContainsData, null, null);

		for (Map.Entry<KnimeTuple, List<KnimeTuple>> entry : combinations
				.entrySet()) {
			tuples.add(entry.getKey());
			usedTuples.add(entry.getValue());
		}

		allIds = new ArrayList<String>();
		allTuples = new ArrayList<KnimeTuple>();
		tupleCombinations = new LinkedHashMap<KnimeTuple, List<KnimeTuple>>();
		ids = new ArrayList<String>();
		plotables = new LinkedHashMap<String, Plotable>();
		shortLegend = new LinkedHashMap<String, String>();
		longLegend = new LinkedHashMap<String, String>();

		if (schemaContainsData) {
			try {
				tuples = QualityMeasurementComputation.computePrimary(tuples,
						false);
			} catch (Exception e) {
			}

			try {
				newTuples = QualityMeasurementComputation.computePrimary(
						tuples, true);
			} catch (Exception e) {
			}

			miscParams = PmmUtilities.getMiscParams(tuples);
			stringColumns = Arrays.asList(Model1Schema.FORMULA,
					Model1Schema.ATT_EMLIT, ChartConstants.STATUS,
					AttributeUtilities.DATAID, TimeSeriesSchema.ATT_AGENT,
					AttributeUtilities.AGENT_DETAILS,
					TimeSeriesSchema.ATT_MATRIX,
					AttributeUtilities.MATRIX_DETAILS, MdInfoXml.ATT_COMMENT);
			stringColumnValues = new ArrayList<List<String>>();
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			doubleColumns = Arrays.asList(Model1Schema.SSE, Model1Schema.MSE,
					Model1Schema.RMSE, Model1Schema.RSQUARED, Model1Schema.AIC,
					Model1Schema.SSE + " (Local)", Model1Schema.MSE
							+ " (Local)", Model1Schema.RMSE + " (Local)",
					Model1Schema.RSQUARED + " (Local)", Model1Schema.AIC
							+ " (Local)");
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
			standardVisibleColumns = new ArrayList<String>(Arrays.asList(
					ChartSelectionPanel.DATA, ChartSelectionPanel.FORMULA,
					ChartSelectionPanel.PARAMETERS));
			standardVisibleColumns.addAll(stringColumns);
			standardVisibleColumns.addAll(doubleColumns);
			filterableStringColumns = Arrays.asList(Model1Schema.FORMULA,
					ChartConstants.STATUS, AttributeUtilities.DATAID);

			data = new ArrayList<List<TimeSeriesXml>>();
			formulas = new ArrayList<String>();
			parameterData = new ArrayList<Map<String, Double>>();
			conditions = new ArrayList<String>();
			conditionValues = new ArrayList<List<Double>>();
			conditionUnits = new ArrayList<List<String>>();

			for (String param : miscParams) {
				conditions.add(param);
				conditionValues.add(new ArrayList<Double>());
				conditionUnits.add(new ArrayList<String>());
				standardVisibleColumns.add(param);
			}
		} else {
			stringColumns = Arrays.asList(Model1Schema.FORMULA,
					Model1Schema.ATT_EMLIT, ChartConstants.STATUS);
			stringColumnValues = new ArrayList<List<String>>();
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			doubleColumns = Arrays.asList(Model1Schema.SSE, Model1Schema.MSE,
					Model1Schema.RMSE, Model1Schema.RSQUARED, Model1Schema.AIC);
			doubleColumnValues = new ArrayList<List<Double>>();
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			standardVisibleColumns = new ArrayList<String>(
					Arrays.asList(ChartSelectionPanel.FORMULA,
							ChartSelectionPanel.PARAMETERS));
			standardVisibleColumns.addAll(stringColumns);
			standardVisibleColumns.addAll(doubleColumns);
			filterableStringColumns = Arrays.asList(Model1Schema.FORMULA,
					ChartConstants.STATUS);

			data = null;
			formulas = new ArrayList<String>();
			parameterData = new ArrayList<Map<String, Double>>();
			conditions = null;
			conditionValues = null;
			conditionUnits = null;
		}

		for (int nr = 0; nr < tuples.size(); nr++) {
			KnimeTuple tuple = tuples.get(nr);
			Integer catID = ((CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).getId();
			Integer estID = ((EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0)).getId();
			String id = "";

			if (estID != null) {
				id += estID;
			} else {
				id += catID;
			}

			if (schemaContainsData) {
				id += "(" + tuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			}

			allIds.add(id);
			allTuples.add(tuple);
			tupleCombinations.put(tuple, usedTuples.get(nr));

			if (!idSet.add(id)) {
				continue;
			}

			ids.add(id);

			CatalogModelXml modelXml = (CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0);
			DepXml depXml = (DepXml) tuple
					.getPmmXml(Model1Schema.ATT_DEPENDENT).get(0);
			String modelName = modelXml.getName();
			String formula = MathUtilities.getAllButBoundaryCondition(modelXml
					.getFormula());
			String depVar = depXml.getName();
			PmmXmlDoc indepXml = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			List<String> indepVars = CellIO.getNameList(indepXml);
			PmmXmlDoc paramXml = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
			List<Double> paramValues = new ArrayList<Double>();
			Plotable plotable = null;
			Map<String, Double> parameters = new LinkedHashMap<String, Double>();
			Map<String, Double> paramData = new LinkedHashMap<String, Double>();
			Map<String, List<Double>> variables = new LinkedHashMap<String, List<Double>>();
			Map<String, Double> varMin = new LinkedHashMap<String, Double>();
			Map<String, Double> varMax = new LinkedHashMap<String, Double>();
			String timeUnit = Categories.getTimeCategory().getStandardUnit();
			String concentrationUnit = depXml.getUnit();

			for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
				IndepXml element = (IndepXml) el;

				if (element.getName().equals(AttributeUtilities.TIME)) {
					variables.put(element.getName(), new ArrayList<Double>(
							Arrays.asList(0.0)));
					varMin.put(element.getName(), element.getMin());
					varMax.put(element.getName(), element.getMax());
					timeUnit = element.getUnit();
				}
			}

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml element = (ParamXml) el;

				parameters.put(element.getName(), element.getValue());
				paramValues.add(element.getValue());
				paramData.put(element.getName(), element.getValue());
				paramData.put(element.getName() + ": SE", element.getError());
				paramData.put(element.getName() + ": t", element.getT());
				paramData.put(element.getName() + ": Pr > |t|", element.getP());
			}

			formulas.add(formula);
			parameterData.add(paramData);

			if (schemaContainsData) {
				PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
				Map<String, Double> miscValues = new LinkedHashMap<String, Double>();

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					miscValues.put(element.getName(), element.getValue());
				}

				for (int i = 0; i < indepVars.size(); i++) {
					if (!indepVars.get(i).equals(AttributeUtilities.TIME)) {
						if (miscValues.containsKey(indepVars.get(i))) {
							parameters.put(indepVars.get(i),
									miscValues.get(indepVars.get(i)));
						}
					}
				}

				PmmXmlDoc timeSeriesXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
				List<Double> timeList = new ArrayList<Double>();
				List<Double> logcList = new ArrayList<Double>();
				List<TimeSeriesXml> dataPoints = new ArrayList<TimeSeriesXml>();

				for (PmmXmlElementConvertable el : timeSeriesXml
						.getElementSet()) {
					TimeSeriesXml element = (TimeSeriesXml) el;

					timeList.add(element.getTime());
					logcList.add(element.getConcentration());
					dataPoints.add(element);
				}

				plotable = new Plotable(Plotable.BOTH);
				plotable.addValueList(AttributeUtilities.TIME, timeList);
				plotable.addValueList(AttributeUtilities.CONCENTRATION,
						logcList);

				String dataName;
				AgentXml agent = (AgentXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0);
				MatrixXml matrix = (MatrixXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0);

				if (tuple.getString(TimeSeriesSchema.ATT_COMBASEID) != null) {
					dataName = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
				} else {
					dataName = "" + tuple.getInt(TimeSeriesSchema.ATT_CONDID);
				}

				PmmXmlDoc estModelXml = tuple
						.getPmmXml(Model1Schema.ATT_ESTMODEL);

				shortLegend.put(id, modelName + " (" + dataName + ")");
				longLegend
						.put(id, modelName + " (" + dataName + ") " + formula);
				stringColumnValues.get(3).add(dataName);
				stringColumnValues.get(4).add(agent.getName());
				stringColumnValues.get(5).add(agent.getDetail());
				stringColumnValues.get(6).add(matrix.getName());
				stringColumnValues.get(7).add(matrix.getDetail());
				stringColumnValues.get(8).add(
						((MdInfoXml) tuple.getPmmXml(
								TimeSeriesSchema.ATT_MDINFO).get(0))
								.getComment());
				doubleColumnValues.get(0).add(
						MathUtilities.getSSE(
								((EstModelXml) estModelXml.get(0)).getRms(),
								((EstModelXml) estModelXml.get(0)).getDof()));
				doubleColumnValues.get(1).add(
						MathUtilities.getMSE(((EstModelXml) estModelXml.get(0))
								.getRms()));
				doubleColumnValues.get(2).add(
						((EstModelXml) estModelXml.get(0)).getRms());
				doubleColumnValues.get(3).add(
						((EstModelXml) estModelXml.get(0)).getR2());
				doubleColumnValues.get(4).add(
						((EstModelXml) estModelXml.get(0)).getAic());
				data.add(dataPoints);

				if (newTuples != null) {
					PmmXmlDoc newEstModelXml = newTuples.get(nr).getPmmXml(
							Model1Schema.ATT_ESTMODEL);

					doubleColumnValues.get(5).add(
							MathUtilities.getSSE(((EstModelXml) newEstModelXml
									.get(0)).getRms(),
									((EstModelXml) newEstModelXml.get(0))
											.getDof()));
					doubleColumnValues.get(6).add(
							MathUtilities.getMSE(((EstModelXml) newEstModelXml
									.get(0)).getRms()));
					doubleColumnValues.get(7).add(
							((EstModelXml) newEstModelXml.get(0)).getRms());
					doubleColumnValues.get(8).add(
							((EstModelXml) newEstModelXml.get(0)).getR2());
					doubleColumnValues.get(9).add(
							((EstModelXml) newEstModelXml.get(0)).getAic());
				} else {
					doubleColumnValues.get(5).add(null);
					doubleColumnValues.get(6).add(null);
					doubleColumnValues.get(7).add(null);
					doubleColumnValues.get(8).add(null);
					doubleColumnValues.get(9).add(null);
				}

				for (int i = 0; i < miscParams.size(); i++) {
					Double value = null;
					String unit = null;

					for (PmmXmlElementConvertable el : misc.getElementSet()) {
						MiscXml element = (MiscXml) el;

						if (miscParams.get(i).equals(element.getName())) {
							value = element.getValue();
							unit = element.getUnit();
							break;
						}
					}

					conditionValues.get(i).add(value);
					conditionUnits.get(i).add(unit);
				}
			} else {
				for (int i = 0; i < indepVars.size(); i++) {
					if (!indepVars.get(i).equals(AttributeUtilities.TIME)) {
						parameters.put(indepVars.get(i), 0.0);
					}
				}

				PmmXmlDoc estModelXml = tuple
						.getPmmXml(Model1Schema.ATT_ESTMODEL);

				plotable = new Plotable(Plotable.FUNCTION);
				shortLegend.put(id, modelName);
				longLegend.put(id, modelName + " " + formula);
				doubleColumnValues.get(0).add(
						((EstModelXml) estModelXml.get(0)).getRms());
				doubleColumnValues.get(1).add(
						((EstModelXml) estModelXml.get(0)).getR2());
				doubleColumnValues.get(2).add(
						((EstModelXml) estModelXml.get(0)).getAic());
				doubleColumnValues.get(3).add(
						((EstModelXml) estModelXml.get(0)).getBic());
			}

			Map<String, List<String>> categories = new LinkedHashMap<String, List<String>>();
			Map<String, String> units = new LinkedHashMap<String, String>();

			categories.put(AttributeUtilities.TIME,
					Arrays.asList(Categories.getTime()));
			categories.put(AttributeUtilities.CONCENTRATION,
					Categories.getConcentrations());
			units.put(AttributeUtilities.TIME, timeUnit);
			units.put(AttributeUtilities.CONCENTRATION, concentrationUnit);

			plotable.setFunction(modelXml.getFormula());
			plotable.setFunctionParameters(parameters);
			plotable.setFunctionArguments(variables);
			plotable.setMinArguments(varMin);
			plotable.setMaxArguments(varMax);
			plotable.setFunctionValue(depVar);
			plotable.setCategories(categories);
			plotable.setUnits(units);

			String literature = "";

			for (PmmXmlElementConvertable el : tuple.getPmmXml(
					Model1Schema.ATT_EMLIT).getElementSet()) {
				literature += "," + (LiteratureItem) el;
			}

			if (!literature.isEmpty()) {
				literature = literature.substring(1);
			}

			stringColumnValues.get(0).add(modelName);
			stringColumnValues.get(1).add(literature);

			if (!plotable.isPlotable()) {
				stringColumnValues.get(2).add(ChartConstants.FAILED);
			} else if (PmmUtilities.isOutOfRange(paramXml)) {
				stringColumnValues.get(2).add(ChartConstants.OUT_OF_LIMITS);
			} else if (PmmUtilities.covarianceMatrixMissing(paramXml)) {
				stringColumnValues.get(2).add(ChartConstants.NO_COVARIANCE);
			} else {
				stringColumnValues.get(2).add(ChartConstants.OK);
			}

			plotables.put(id, plotable);
		}
	}

	public List<String> getAllIds() {
		return allIds;
	}

	public List<KnimeTuple> getAllTuples() {
		return allTuples;
	}

	public Map<KnimeTuple, List<KnimeTuple>> getTupleCombinations() {
		return tupleCombinations;
	}

	public List<String> getIds() {
		return ids;
	}

	public List<String> getStringColumns() {
		return stringColumns;
	}

	public List<List<String>> getStringColumnValues() {
		return stringColumnValues;
	}

	public List<String> getDoubleColumns() {
		return doubleColumns;
	}

	public List<List<Double>> getDoubleColumnValues() {
		return doubleColumnValues;
	}

	public List<List<TimeSeriesXml>> getData() {
		return data;
	}

	public List<String> getFormulas() {
		return formulas;
	}

	public List<Map<String, Double>> getParameterData() {
		return parameterData;
	}

	public List<String> getConditions() {
		return conditions;
	}

	public List<List<Double>> getConditionValues() {
		return conditionValues;
	}

	public List<List<String>> getConditionUnits() {
		return conditionUnits;
	}

	public List<String> getStandardVisibleColumns() {
		return standardVisibleColumns;
	}

	public List<String> getFilterableStringColumns() {
		return filterableStringColumns;
	}

	public Map<String, Plotable> getPlotables() {
		return plotables;
	}

	public Map<String, String> getShortLegend() {
		return shortLegend;
	}

	public Map<String, String> getLongLegend() {
		return longLegend;
	}

}
