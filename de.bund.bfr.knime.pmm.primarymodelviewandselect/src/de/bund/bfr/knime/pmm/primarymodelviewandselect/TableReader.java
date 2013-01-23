package de.bund.bfr.knime.pmm.primarymodelviewandselect;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.node.BufferedDataTable;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.QualityMeasurementComputation;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class TableReader {

	private List<String> allIds;
	private List<KnimeTuple> allTuples;

	private List<String> ids;

	private List<String> stringColumns;
	private List<List<String>> stringColumnValues;
	private List<String> doubleColumns;
	private List<List<Double>> doubleColumnValues;

	private List<List<String>> infoParameters;
	private List<List<?>> infoParameterValues;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;

	public TableReader(BufferedDataTable table, KnimeSchema schema,
			boolean schemaContainsData) throws PmmException {
		List<String> miscParams = null;

		allIds = new ArrayList<String>();
		allTuples = new ArrayList<KnimeTuple>();
		ids = new ArrayList<String>();
		plotables = new LinkedHashMap<String, Plotable>();
		infoParameters = new ArrayList<List<String>>();
		infoParameterValues = new ArrayList<List<?>>();
		shortLegend = new LinkedHashMap<String, String>();
		longLegend = new LinkedHashMap<String, String>();

		if (schemaContainsData) {
			miscParams = getAllMiscParams(table);
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

			for (String param : miscParams) {
				doubleColumns.add(param);
				doubleColumnValues.add(new ArrayList<Double>());
			}
		} else {
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
		}

		KnimeRelationReader reader = new KnimeRelationReader(schema, table);
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>();

		while (reader.hasMoreElements()) {
			tuples.add(reader.nextElement());
		}

		List<KnimeTuple> newTuples = null;

		if (schemaContainsData) {
			newTuples = QualityMeasurementComputation.computePrimary(tuples);
		}

		Set<String> idSet = new LinkedHashSet<String>();

		for (int nr = 0; nr < tuples.size(); nr++) {
			KnimeTuple tuple = tuples.get(nr);
			String id = ((EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0)).getID()
					+ "";

			if (schemaContainsData) {
				id += "(" + tuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			}

			allIds.add(id);
			allTuples.add(tuple);

			if (!idSet.add(id)) {
				continue;
			}

			ids.add(id);

			PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			PmmXmlDoc estModelXml = tuple.getPmmXml(Model1Schema.ATT_ESTMODEL);
			String modelName = ((CatalogModelXml) modelXml.get(0)).getName();
			String formula = ((CatalogModelXml) modelXml.get(0)).getFormula();
			String depVar = ((DepXml) tuple.getPmmXml(
					Model1Schema.ATT_DEPENDENT).get(0)).getName();
			PmmXmlDoc indepXml = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			List<String> indepVars = CellIO.getNameList(indepXml);
			PmmXmlDoc paramXml = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
			List<Double> paramValues = new ArrayList<Double>();
			List<Double> paramMinValues = new ArrayList<Double>();
			List<Double> paramMaxValues = new ArrayList<Double>();
			Plotable plotable = null;
			Map<String, Double> parameters = new LinkedHashMap<String, Double>();
			Map<String, List<Double>> variables = new LinkedHashMap<String, List<Double>>();
			Map<String, Double> varMin = new LinkedHashMap<String, Double>();
			Map<String, Double> varMax = new LinkedHashMap<String, Double>();
			Map<String, Map<String, Double>> covariances = new LinkedHashMap<String, Map<String, Double>>();
			List<String> infoParams = null;
			List<Object> infoValues = null;

			for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
				IndepXml element = (IndepXml) el;

				if (element.getName().equals(TimeSeriesSchema.TIME)) {
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
				paramMinValues.add(element.getMin());
				paramMaxValues.add(element.getMax());

				Map<String, Double> cov = new LinkedHashMap<String, Double>();

				for (PmmXmlElementConvertable el2 : paramXml.getElementSet()) {
					cov.put(((ParamXml) el2).getName(), element
							.getCorrelation(((ParamXml) el2).getOrigName()));
				}

				covariances.put(element.getName(), cov);
			}

			if (schemaContainsData) {
				PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
				Map<String, Double> miscValues = new LinkedHashMap<String, Double>();

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					miscValues.put(element.getName(), element.getValue());
				}

				for (int i = 0; i < indepVars.size(); i++) {
					if (!indepVars.get(i).equals(TimeSeriesSchema.TIME)) {
						if (miscValues.containsKey(indepVars.get(i))) {
							parameters.put(indepVars.get(i),
									miscValues.get(indepVars.get(i)));
						} else {
							parameters.put(indepVars.get(i),
									tuple.getDouble(indepVars.get(i)));
						}
					}
				}

				PmmXmlDoc timeSeriesXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
				List<Double> timeList = new ArrayList<Double>();
				List<Double> logcList = new ArrayList<Double>();
				List<Point2D.Double> dataPoints = new ArrayList<Point2D.Double>();

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

				if (!timeList.isEmpty() && !logcList.isEmpty()) {
					plotable = new Plotable(Plotable.BOTH);
					plotable.addValueList(TimeSeriesSchema.TIME, timeList);
					plotable.addValueList(TimeSeriesSchema.LOGC, logcList);
				} else {
					plotable = new Plotable(Plotable.FUNCTION);
				}

				String dataName;
				String agent;
				String matrix;

				if (tuple.getString(TimeSeriesSchema.ATT_COMBASEID) != null) {
					dataName = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
				} else {
					dataName = "" + tuple.getInt(TimeSeriesSchema.ATT_CONDID);
				}

				if (tuple.getString(TimeSeriesSchema.ATT_AGENTNAME) != null) {
					agent = tuple.getString(TimeSeriesSchema.ATT_AGENTNAME)
							+ " ("
							+ tuple.getString(TimeSeriesSchema.ATT_AGENTDETAIL)
							+ ")";
				} else {
					agent = tuple.getString(TimeSeriesSchema.ATT_AGENTDETAIL);
				}

				if (tuple.getString(TimeSeriesSchema.ATT_MATRIXNAME) != null) {
					matrix = tuple.getString(TimeSeriesSchema.ATT_MATRIXNAME)
							+ " ("
							+ tuple.getString(TimeSeriesSchema.ATT_MATRIXDETAIL)
							+ ")";
				} else {
					matrix = tuple.getString(TimeSeriesSchema.ATT_MATRIXDETAIL);
				}

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
						TimeSeriesSchema.ATT_AGENTNAME,
						TimeSeriesSchema.ATT_MATRIXNAME,
						TimeSeriesSchema.ATT_COMMENT));
				infoValues = new ArrayList<Object>(Arrays.asList(formula,
						dataPoints, agent, matrix,
						tuple.getString(TimeSeriesSchema.ATT_COMMENT)));

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
					if (!indepVars.get(i).equals(TimeSeriesSchema.TIME)) {
						parameters.put(indepVars.get(i), 0.0);
					}
				}

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
			plotable.setFunctionParameters(parameters);
			plotable.setFunctionArguments(variables);
			plotable.setMinArguments(varMin);
			plotable.setMaxArguments(varMax);
			plotable.setFunctionValue(depVar);
			plotable.setCovariances(covariances);
			plotable.setDegreesOfFreedom(((EstModelXml) estModelXml.get(0))
					.getDOF());

			if (schemaContainsData) {
				if (!plotable.isPlotable()) {
					stringColumnValues.get(2).add(ChartConstants.NO);
				} else if (!MathUtilities.areValuesInRange(paramValues,
						paramMinValues, paramMaxValues)) {
					stringColumnValues.get(2).add(ChartConstants.WARNING);
				} else {
					stringColumnValues.get(2).add(ChartConstants.YES);
				}
			} else {
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

	public List<String> getAllIds() {
		return allIds;
	}

	public List<KnimeTuple> getAllTuples() {
		return allTuples;
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

	public List<List<String>> getInfoParameters() {
		return infoParameters;
	}

	public List<List<?>> getInfoParameterValues() {
		return infoParameterValues;
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

	private List<String> getAllMiscParams(BufferedDataTable table)
			throws PmmException {
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

}
