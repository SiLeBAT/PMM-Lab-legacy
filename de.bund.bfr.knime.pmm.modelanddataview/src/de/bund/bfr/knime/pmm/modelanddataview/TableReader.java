package de.bund.bfr.knime.pmm.modelanddataview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.data.DataTable;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.QualityMeasurementComputation;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class TableReader {

	private List<String> ids;
	private List<String> stringColumns;
	private List<List<String>> stringColumnValues;
	private List<String> doubleColumns;
	private List<List<Double>> doubleColumnValues;
	private List<List<TimeSeriesXml>> data;
	private List<Map<String, Double>> parameterData;
	private List<String> visibleColumns;
	private List<String> filterableStringColumns;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;

	public TableReader(DataTable table, boolean schemaContainsData) {
		List<String> miscParams = null;
		List<KnimeTuple> tuples;
		List<KnimeTuple> newTuples = null;

		if (schemaContainsData) {
			tuples = PmmUtilities.getTuples(table,
					SchemaFactory.createM1DataSchema());
		} else {
			tuples = PmmUtilities.getTuples(table,
					SchemaFactory.createM1Schema());
		}

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

			miscParams = PmmUtilities.getAllMiscParams(tuples);
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
			visibleColumns = new ArrayList<>(Arrays.asList(
					Model1Schema.MODELNAME, AttributeUtilities.DATAID));
			filterableStringColumns = Arrays.asList(Model1Schema.MODELNAME,
					AttributeUtilities.DATAID, ChartConstants.STATUS);

			for (String param : miscParams) {
				doubleColumns.add(param);
				doubleColumnValues.add(new ArrayList<Double>());
				visibleColumns.add(param);
			}

			data = new ArrayList<>();
			parameterData = new ArrayList<>();
		} else {
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
			visibleColumns = Arrays.asList(Model1Schema.MODELNAME);
			filterableStringColumns = Arrays.asList(Model1Schema.MODELNAME,
					ChartConstants.STATUS);

			data = null;
			parameterData = new ArrayList<>();
		}

		Set<String> idSet = new LinkedHashSet<String>();

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

			if (!idSet.add(id)) {
				continue;
			}

			ids.add(id);

			PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			PmmXmlDoc estModelXml = tuple.getPmmXml(Model1Schema.ATT_ESTMODEL);
			DepXml depXml = (DepXml) tuple
					.getPmmXml(Model1Schema.ATT_DEPENDENT).get(0);
			String modelName = ((CatalogModelXml) modelXml.get(0)).getName();
			String formula = ((CatalogModelXml) modelXml.get(0)).getFormula();
			String depVar = depXml.getName();
			PmmXmlDoc indepXml = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			PmmXmlDoc paramXml = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
			Plotable plotable = null;
			Map<String, List<Double>> variables = new LinkedHashMap<String, List<Double>>();
			Map<String, Double> varMin = new LinkedHashMap<String, Double>();
			Map<String, Double> varMax = new LinkedHashMap<String, Double>();
			Map<String, Double> parameters = new LinkedHashMap<>();
			Map<String, Double> paramData = new LinkedHashMap<>();
			Map<String, Map<String, Double>> covariances = new LinkedHashMap<String, Map<String, Double>>();
			Map<String, String> categories = new LinkedHashMap<>();
			Map<String, String> units = new LinkedHashMap<>();

			categories.put(depXml.getName(), depXml.getCategory());
			units.put(depXml.getName(), depXml.getUnit());

			for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
				IndepXml element = (IndepXml) el;

				variables.put(element.getName(),
						new ArrayList<Double>(Arrays.asList(0.0)));
				varMin.put(element.getName(), element.getMin());
				varMax.put(element.getName(), element.getMax());

				categories.put(element.getName(), element.getCategory());
				units.put(element.getName(), element.getUnit());
			}

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml element = (ParamXml) el;

				parameters.put(element.getName(), element.getValue());

				Map<String, Double> cov = new LinkedHashMap<String, Double>();

				for (PmmXmlElementConvertable el2 : paramXml.getElementSet()) {
					cov.put(((ParamXml) el2).getName(), element
							.getCorrelation(((ParamXml) el2).getOrigName()));
				}

				covariances.put(element.getName(), cov);
				paramData.put(element.getName(), element.getValue());
				paramData.put(element.getName() + ": SE", element.getError());
				paramData.put(element.getName() + ": t", element.gett());
				paramData.put(element.getName() + ": Pr > |t|", element.getP());
			}

			parameterData.add(paramData);

			if (schemaContainsData) {
				PmmXmlDoc timeSeriesXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);
				List<TimeSeriesXml> dataPoints = new ArrayList<>();
				PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
				List<Double> timeList = new ArrayList<Double>();
				List<Double> logcList = new ArrayList<Double>();
				int n = timeSeriesXml.getElementSet().size();

				for (PmmXmlElementConvertable el : timeSeriesXml
						.getElementSet()) {
					TimeSeriesXml element = (TimeSeriesXml) el;

					timeList.add(element.getTime());
					logcList.add(element.getConcentration());
					dataPoints.add(element);
				}

				plotable = new Plotable(Plotable.BOTH);
				plotable.addValueList(AttributeUtilities.TIME, timeList);
				plotable.addValueList(AttributeUtilities.LOGC, logcList);

				for (PmmXmlElementConvertable el : misc.getElementSet()) {
					MiscXml element = (MiscXml) el;

					if (element.getValue() != null) {
						plotable.addValueList(
								element.getName(),
								new ArrayList<Double>(Collections.nCopies(n,
										element.getValue())));
					}
				}

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
			plotable.setFunctionValue(depVar);
			plotable.setFunctionArguments(variables);
			plotable.setMinArguments(varMin);
			plotable.setMaxArguments(varMax);
			plotable.setFunctionParameters(parameters);
			plotable.setCovariances(covariances);
			plotable.setDegreesOfFreedom(((EstModelXml) estModelXml.get(0))
					.getDOF());
			plotable.setCategories(categories);
			plotable.setUnits(units);

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

	public List<String> getIds() {
		return ids;
	}

	public Map<String, Plotable> getPlotables() {
		return plotables;
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
		return visibleColumns;
	}

	public List<String> getFilterableStringColumns() {
		return filterableStringColumns;
	}

	public Map<String, String> getShortLegend() {
		return shortLegend;
	}

	public Map<String, String> getLongLegend() {
		return longLegend;
	}
}
