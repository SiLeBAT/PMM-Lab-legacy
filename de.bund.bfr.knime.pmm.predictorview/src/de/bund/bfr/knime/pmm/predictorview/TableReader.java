package de.bund.bfr.knime.pmm.predictorview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ModelCombiner;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class TableReader {

	private List<String> ids;
	private Map<String, KnimeTuple> tupleMap;
	private List<String> stringColumns;
	private List<List<String>> stringColumnValues;
	private List<String> doubleColumns;
	private List<List<Double>> doubleColumnValues;
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

	public TableReader(List<KnimeTuple> tuples, Map<String, String> initParams) {
		Set<String> idSet = new LinkedHashSet<String>();
		Map<KnimeTuple, List<KnimeTuple>> combinedTuples = null;
		boolean isTertiaryModel = tuples.get(0).getSchema()
				.conforms(SchemaFactory.createM12Schema());
		boolean containsData = tuples.get(0).getSchema()
				.conforms(SchemaFactory.createDataSchema());
		List<String> miscParams = null;

		if (isTertiaryModel) {
			combinedTuples = ModelCombiner.combine(tuples, false, initParams);
			tuples = new ArrayList<KnimeTuple>(combinedTuples.keySet());
			containsData = false;

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
		} else {
			if (!tuples.isEmpty()) {
				if (tuples.get(0).getPmmXml(Model1Schema.ATT_INDEPENDENT)
						.size() > 1) {
					containsData = false;
				}
			}
		}

		ids = new ArrayList<String>();
		tupleMap = new LinkedHashMap<>();
		plotables = new LinkedHashMap<>();
		shortLegend = new LinkedHashMap<>();
		longLegend = new LinkedHashMap<>();
		formulas = new ArrayList<>();
		parameterData = new ArrayList<>();
		doubleColumns = Arrays.asList(Model1Schema.RMS, Model1Schema.RSQUARED,
				Model1Schema.AIC, Model1Schema.BIC);
		doubleColumnValues = new ArrayList<List<Double>>();
		doubleColumnValues.add(new ArrayList<Double>());
		doubleColumnValues.add(new ArrayList<Double>());
		doubleColumnValues.add(new ArrayList<Double>());
		doubleColumnValues.add(new ArrayList<Double>());
		conditions = null;
		conditionValues = null;
		conditionUnits = null;

		if (containsData) {
			if (isTertiaryModel) {
				stringColumns = Arrays.asList(ChartConstants.STATUS,
						Model1Schema.MODELNAME, Model2Schema.MODELNAME,
						AttributeUtilities.DATAID);
				stringColumnValues = new ArrayList<List<String>>();
				stringColumnValues.add(new ArrayList<String>());
				stringColumnValues.add(new ArrayList<String>());
				stringColumnValues.add(new ArrayList<String>());
				stringColumnValues.add(new ArrayList<String>());
				standardVisibleColumns = Arrays.asList(Model1Schema.MODELNAME,
						Model2Schema.MODELNAME, ChartConstants.STATUS,
						AttributeUtilities.DATAID);
				filterableStringColumns = Arrays.asList(Model1Schema.MODELNAME,
						Model2Schema.MODELNAME, ChartConstants.STATUS,
						AttributeUtilities.DATAID);
			} else {
				stringColumns = Arrays.asList(ChartConstants.STATUS,
						Model1Schema.MODELNAME, AttributeUtilities.DATAID);
				stringColumnValues = new ArrayList<List<String>>();
				stringColumnValues.add(new ArrayList<String>());
				stringColumnValues.add(new ArrayList<String>());
				stringColumnValues.add(new ArrayList<String>());
				standardVisibleColumns = Arrays.asList(Model1Schema.MODELNAME,
						ChartConstants.STATUS, AttributeUtilities.DATAID);
				filterableStringColumns = Arrays.asList(Model1Schema.MODELNAME,
						ChartConstants.STATUS, AttributeUtilities.DATAID);
			}

			miscParams = PmmUtilities.getMiscParams(tuples);
			conditions = new ArrayList<>();
			conditionValues = new ArrayList<>();
			conditionUnits = new ArrayList<>();

			for (String param : miscParams) {
				conditions.add(param);
				conditionValues.add(new ArrayList<Double>());
				conditionUnits.add(new ArrayList<String>());
			}
		} else {
			if (isTertiaryModel) {
				stringColumns = Arrays.asList(ChartConstants.STATUS,
						Model1Schema.MODELNAME, Model2Schema.MODELNAME);
				stringColumnValues = new ArrayList<List<String>>();
				stringColumnValues.add(new ArrayList<String>());
				stringColumnValues.add(new ArrayList<String>());
				stringColumnValues.add(new ArrayList<String>());
				standardVisibleColumns = Arrays.asList(Model1Schema.MODELNAME,
						Model2Schema.MODELNAME, ChartConstants.STATUS);
				filterableStringColumns = Arrays.asList(Model1Schema.MODELNAME,
						Model2Schema.MODELNAME, ChartConstants.STATUS);
			} else {
				stringColumns = Arrays.asList(ChartConstants.STATUS,
						Model1Schema.MODELNAME);
				stringColumnValues = new ArrayList<List<String>>();
				stringColumnValues.add(new ArrayList<String>());
				stringColumnValues.add(new ArrayList<String>());
				standardVisibleColumns = Arrays.asList(Model1Schema.MODELNAME,
						ChartConstants.STATUS);
				filterableStringColumns = Arrays.asList(Model1Schema.MODELNAME,
						ChartConstants.STATUS);
			}
		}

		for (KnimeTuple tuple : tuples) {
			String id = ((EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0)).getID()
					+ "";

			if (containsData) {
				id += "(" + tuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			}

			if (!idSet.add(id)) {
				continue;
			}

			ids.add(id);
			tupleMap.put(id, tuple);

			PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			DepXml depXml = (DepXml) tuple
					.getPmmXml(Model1Schema.ATT_DEPENDENT).get(0);
			String modelID = ((CatalogModelXml) modelXml.get(0)).getID() + "";
			String modelName = ((CatalogModelXml) modelXml.get(0)).getName();
			String formula = ((CatalogModelXml) modelXml.get(0)).getFormula();
			String depVar = depXml.getName();
			PmmXmlDoc indepXml = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			PmmXmlDoc paramXml = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
			Map<String, List<Double>> variables = new LinkedHashMap<>();
			Map<String, Double> varMin = new LinkedHashMap<>();
			Map<String, Double> varMax = new LinkedHashMap<>();
			Map<String, Double> parameters = new LinkedHashMap<>();
			Map<String, Double> paramData = new LinkedHashMap<>();
			Map<String, Map<String, Double>> covariances = new LinkedHashMap<>();
			String initParam = initParams.get(modelID);
			Map<String, List<String>> categories = new LinkedHashMap<>();
			Map<String, String> units = new LinkedHashMap<>();
			Plotable plotable = new Plotable(Plotable.FUNCTION_SAMPLE);

			categories.put(depXml.getName(),
					Arrays.asList(depXml.getCategory()));
			units.put(depXml.getName(), depXml.getUnit());

			for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
				IndepXml element = (IndepXml) el;

				variables.put(element.getName(), new ArrayList<Double>());
				varMin.put(element.getName(), element.getMin());
				varMax.put(element.getName(), element.getMax());

				categories.put(element.getName(),
						Arrays.asList(element.getCategory()));
				units.put(element.getName(), element.getUnit());
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
					paramData.put(element.getName(), element.getValue());
					paramData.put(element.getName() + ": SE",
							element.getError());
					paramData.put(element.getName() + ": t", element.gett());
					paramData.put(element.getName() + ": Pr > |t|",
							element.getP());
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

			formulas.add(formula);
			parameterData.add(paramData);

			PmmXmlDoc estModelXml = tuple.getPmmXml(Model1Schema.ATT_ESTMODEL);

			shortLegend.put(id, modelName);
			longLegend.put(id, modelName + " " + formula);
			stringColumnValues.get(1).add(modelName);

			if (isTertiaryModel) {
				Set<String> secModels = new LinkedHashSet<>();
				String secString = "";

				for (KnimeTuple t : combinedTuples.get(tuple)) {
					secModels.add(((CatalogModelXml) t.getPmmXml(
							Model2Schema.ATT_MODELCATALOG).get(0)).getName());
				}

				for (String s : secModels) {
					secString += "," + s;
				}

				stringColumnValues.get(2).add(secString.substring(1));
			}

			doubleColumnValues.get(0).add(
					((EstModelXml) estModelXml.get(0)).getRMS());
			doubleColumnValues.get(1).add(
					((EstModelXml) estModelXml.get(0)).getR2());
			doubleColumnValues.get(2).add(
					((EstModelXml) estModelXml.get(0)).getAIC());
			doubleColumnValues.get(3).add(
					((EstModelXml) estModelXml.get(0)).getBIC());

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

			if (containsData) {
				String dataName;

				if (tuple.getString(TimeSeriesSchema.ATT_COMBASEID) != null) {
					dataName = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
				} else {
					dataName = "" + tuple.getInt(TimeSeriesSchema.ATT_CONDID);
				}

				if (isTertiaryModel) {
					stringColumnValues.get(3).add(dataName);
				} else {
					stringColumnValues.get(2).add(dataName);
				}

				for (int i = 0; i < miscParams.size(); i++) {
					Double value = null;
					String unit = null;

					for (PmmXmlElementConvertable el : tuple.getPmmXml(
							TimeSeriesSchema.ATT_MISC).getElementSet()) {
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
			}

			if (!plotable.isPlotable()) {
				stringColumnValues.get(0).add(ChartConstants.FAILED);
			} else if (PmmUtilities.isOutOfRange(paramXml)) {
				stringColumnValues.get(0).add(ChartConstants.OUT_OF_LIMITS);
			} else if (PmmUtilities.covarianceMatrixMissing(paramXml)) {
				stringColumnValues.get(0).add(ChartConstants.NO_COVARIANCE);
			} else {
				stringColumnValues.get(0).add(ChartConstants.OK);
			}

			plotables.put(id, plotable);
		}
	}

	public List<String> getIds() {
		return ids;
	}

	public Map<String, KnimeTuple> getTupleMap() {
		return tupleMap;
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

	public Map<String, String> getShortLegend() {
		return shortLegend;
	}

	public Map<String, String> getLongLegend() {
		return longLegend;
	}
}
