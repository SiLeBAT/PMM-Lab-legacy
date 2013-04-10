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
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

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
	private List<Map<String, Double>> parameterData;
	private List<String> standardVisibleColumns;
	private List<String> filterableStringColumns;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;

	public TableReader(BufferedDataTable table, boolean schemaContainsData) {
		List<String> miscParams = null;
		List<KnimeTuple> tertiaryTuples = new ArrayList<>();
		List<KnimeTuple> tuples = new ArrayList<>();
		List<KnimeTuple> newTuples = null;
		List<List<KnimeTuple>> usedTuples = new ArrayList<>();
		Set<String> idSet = new LinkedHashSet<>();
		KnimeRelationReader reader = null;

		if (schemaContainsData) {
			reader = new KnimeRelationReader(
					SchemaFactory.createM12DataSchema(), table);
		} else {
			reader = new KnimeRelationReader(SchemaFactory.createM12Schema(),
					table);
		}

		while (reader.hasMoreElements()) {
			tertiaryTuples.add(reader.nextElement());
		}

		Map<KnimeTuple, List<KnimeTuple>> combinations = ModelCombiner.combine(
				tertiaryTuples, schemaContainsData, false, null);

		for (Map.Entry<KnimeTuple, List<KnimeTuple>> entry : combinations
				.entrySet()) {
			tuples.add(entry.getKey());
			usedTuples.add(entry.getValue());
		}

		allIds = new ArrayList<String>();
		allTuples = new ArrayList<KnimeTuple>();
		tupleCombinations = new LinkedHashMap<>();
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

			miscParams = PmmUtilities.getAllMiscParams(table);
			standardVisibleColumns = new ArrayList<>(Arrays.asList(
					Model1Schema.MODELNAME, AttributeUtilities.DATAID));
			filterableStringColumns = Arrays.asList(Model1Schema.MODELNAME,
					AttributeUtilities.DATAID, ChartConstants.STATUS);
			stringColumns = Arrays.asList(Model1Schema.MODELNAME,
					Model1Schema.FORMULA, ChartConstants.STATUS,
					AttributeUtilities.DATAID, TimeSeriesSchema.ATT_AGENT,
					TimeSeriesSchema.ATT_MATRIX, MdInfoXml.ATT_COMMENT);
			stringColumnValues = new ArrayList<List<String>>();
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			doubleColumns = new ArrayList<String>(Arrays.asList(
					Model1Schema.RMS, Model1Schema.RSQUARED, Model1Schema.AIC,
					Model1Schema.BIC, Model1Schema.RMS + " (Local)",
					Model1Schema.RSQUARED + " (Local)", Model1Schema.AIC
							+ " (Local)", Model1Schema.BIC + " (Local)"));
			doubleColumnValues = new ArrayList<List<Double>>();
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());

			for (String param : miscParams) {
				doubleColumns.add(param);
				doubleColumnValues.add(new ArrayList<Double>());
				standardVisibleColumns.add(param);
			}

			data = new ArrayList<>();
			parameterData = new ArrayList<>();
		} else {
			standardVisibleColumns = new ArrayList<>(
					Arrays.asList(Model1Schema.MODELNAME));
			filterableStringColumns = Arrays.asList(Model1Schema.MODELNAME);
			stringColumns = Arrays.asList(Model1Schema.MODELNAME,
					Model1Schema.FORMULA, ChartConstants.STATUS);
			stringColumnValues = new ArrayList<List<String>>();
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			doubleColumns = Arrays.asList(Model1Schema.RMS,
					Model1Schema.RSQUARED, Model1Schema.AIC, Model1Schema.BIC);
			doubleColumnValues = new ArrayList<List<Double>>();
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());

			data = null;
			parameterData = new ArrayList<>();
		}

		for (int nr = 0; nr < tuples.size(); nr++) {
			KnimeTuple tuple = tuples.get(nr);
			Integer catID = ((CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).getID();
			Integer estID = ((EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0)).getID();
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

			PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			String modelName = ((CatalogModelXml) modelXml.get(0)).getName();
			String formula = ((CatalogModelXml) modelXml.get(0)).getFormula();
			String depVar = ((DepXml) tuple.getPmmXml(
					Model1Schema.ATT_DEPENDENT).get(0)).getName();
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

			for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
				IndepXml element = (IndepXml) el;

				if (element.getName().equals(AttributeUtilities.TIME)) {
					variables.put(element.getName(), new ArrayList<Double>(
							Arrays.asList(0.0)));
					varMin.put(element.getName(), element.getMin());
					varMax.put(element.getName(), element.getMax());
				}
			}

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml element = (ParamXml) el;

				parameters.put(element.getName(), element.getValue());
				paramValues.add(element.getValue());
				paramData.put(element.getName(), element.getValue());
				paramData.put(element.getName() + ": SE", element.getError());
				paramData.put(element.getName() + ": t", element.gett());
				paramData.put(element.getName() + ": Pr > |t|", element.getP());
			}

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
				List<TimeSeriesXml> dataPoints = new ArrayList<>();

				for (PmmXmlElementConvertable el : timeSeriesXml
						.getElementSet()) {
					TimeSeriesXml element = (TimeSeriesXml) el;

					timeList.add(element.getTime());
					logcList.add(element.getLog10C());
					dataPoints.add(element);
				}

				plotable = new Plotable(Plotable.BOTH);
				plotable.addValueList(AttributeUtilities.TIME, timeList);
				plotable.addValueList(AttributeUtilities.LOGC, logcList);

				String dataName;
				String agent;
				String matrix;

				PmmXmlDoc agentXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_AGENT);
				String agentName = ((AgentXml) agentXml.get(0)).getName();
				String agentDetail = ((AgentXml) agentXml.get(0)).getDetail();
				PmmXmlDoc matrixXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_MATRIX);
				String matrixName = ((MatrixXml) matrixXml.get(0)).getName();
				String matrixDetail = ((MatrixXml) matrixXml.get(0))
						.getDetail();

				if (tuple.getString(TimeSeriesSchema.ATT_COMBASEID) != null) {
					dataName = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
				} else {
					dataName = "" + tuple.getInt(TimeSeriesSchema.ATT_CONDID);
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

				PmmXmlDoc estModelXml = tuple
						.getPmmXml(Model1Schema.ATT_ESTMODEL);

				shortLegend.put(id, modelName + " (" + dataName + ")");
				longLegend
						.put(id, modelName + " (" + dataName + ") " + formula);
				stringColumnValues.get(3).add(dataName);
				stringColumnValues.get(4).add(agent);
				stringColumnValues.get(5).add(matrix);
				stringColumnValues.get(6).add(
						((MdInfoXml) tuple.getPmmXml(
								TimeSeriesSchema.ATT_MDINFO).get(0))
								.getComment());
				doubleColumnValues.get(0).add(
						((EstModelXml) estModelXml.get(0)).getRMS());
				doubleColumnValues.get(1).add(
						((EstModelXml) estModelXml.get(0)).getR2());
				doubleColumnValues.get(2).add(
						((EstModelXml) estModelXml.get(0)).getAIC());
				doubleColumnValues.get(3).add(
						((EstModelXml) estModelXml.get(0)).getBIC());
				data.add(dataPoints);

				if (newTuples != null) {
					PmmXmlDoc newEstModelXml = newTuples.get(nr).getPmmXml(
							Model1Schema.ATT_ESTMODEL);

					doubleColumnValues.get(4).add(
							((EstModelXml) newEstModelXml.get(0)).getRMS());
					doubleColumnValues.get(5).add(
							((EstModelXml) newEstModelXml.get(0)).getR2());
					doubleColumnValues.get(6).add(
							((EstModelXml) newEstModelXml.get(0)).getAIC());
					doubleColumnValues.get(7).add(
							((EstModelXml) newEstModelXml.get(0)).getBIC());
				} else {
					doubleColumnValues.get(4).add(null);
					doubleColumnValues.get(5).add(null);
					doubleColumnValues.get(6).add(null);
					doubleColumnValues.get(7).add(null);
				}

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
						((EstModelXml) estModelXml.get(0)).getRMS());
				doubleColumnValues.get(1).add(
						((EstModelXml) estModelXml.get(0)).getR2());
				doubleColumnValues.get(2).add(
						((EstModelXml) estModelXml.get(0)).getAIC());
				doubleColumnValues.get(3).add(
						((EstModelXml) estModelXml.get(0)).getBIC());
			}

			plotable.setFunction(formula);
			plotable.setFunctionParameters(parameters);
			plotable.setFunctionArguments(variables);
			plotable.setMinArguments(varMin);
			plotable.setMaxArguments(varMax);
			plotable.setFunctionValue(depVar);

			stringColumnValues.get(0).add(modelName);
			stringColumnValues.get(1).add(formula);

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

	public List<Map<String, Double>> getParameterData() {
		return parameterData;
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
