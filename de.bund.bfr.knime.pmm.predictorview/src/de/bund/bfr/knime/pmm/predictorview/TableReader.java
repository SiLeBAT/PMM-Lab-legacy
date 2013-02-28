package de.bund.bfr.knime.pmm.predictorview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.data.DataTable;

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
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class TableReader {

	private List<String> ids;
	private Map<String, KnimeTuple> tupleMap;
	private Map<String, Plotable> plotables;
	private List<String> stringColumns;
	private List<List<String>> stringColumnValues;
	private List<String> doubleColumns;
	private List<List<Double>> doubleColumnValues;
	private List<String> standardVisibleColumns;
	private List<List<String>> infoParameters;
	private List<List<?>> infoParameterValues;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;

	public TableReader(DataTable table, Map<String, String> initParams) {
		Set<String> idSet = new LinkedHashSet<String>();
		KnimeRelationReader reader;
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>();
		boolean isTertiaryModel = SchemaFactory.createM12Schema().conforms(
				table);
		boolean containsData = SchemaFactory.createDataSchema().conforms(table);
		List<String> miscParams = null;

		if (isTertiaryModel) {
			if (containsData) {
				reader = new KnimeRelationReader(
						SchemaFactory.createM12DataSchema(), table);
			} else {
				reader = new KnimeRelationReader(
						SchemaFactory.createM12Schema(), table);
			}
		} else {
			if (containsData) {
				reader = new KnimeRelationReader(
						SchemaFactory.createM1DataSchema(), table);
			} else {
				reader = new KnimeRelationReader(
						SchemaFactory.createM1Schema(), table);
			}
		}

		while (reader.hasMoreElements()) {
			tuples.add(reader.nextElement());
		}

		if (isTertiaryModel) {
			Map<KnimeTuple, List<KnimeTuple>> combinedTuples = ModelCombiner
					.combine(tuples, containsData, false, initParams);

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
		tupleMap = new LinkedHashMap<>();
		plotables = new LinkedHashMap<>();
		infoParameters = new ArrayList<>();
		infoParameterValues = new ArrayList<>();
		shortLegend = new LinkedHashMap<>();
		longLegend = new LinkedHashMap<>();

		if (containsData) {
			miscParams = PmmUtilities.getAllMiscParams(table);
			stringColumns = Arrays.asList(Model1Schema.MODELNAME,
					AttributeUtilities.DATAID, ChartConstants.STATUS);
			stringColumnValues = new ArrayList<List<String>>();
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			doubleColumns = new ArrayList<String>(Arrays.asList(
					Model1Schema.RMS, Model1Schema.RSQUARED, Model1Schema.AIC,
					Model1Schema.BIC));
			doubleColumnValues = new ArrayList<List<Double>>();
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			standardVisibleColumns = new ArrayList<>(Arrays.asList(
					Model1Schema.MODELNAME, AttributeUtilities.DATAID));

			for (String param : miscParams) {
				doubleColumns.add(param);
				doubleColumnValues.add(new ArrayList<Double>());
				standardVisibleColumns.add(param);
			}
		} else {
			stringColumns = Arrays.asList(Model1Schema.MODELNAME,
					ChartConstants.STATUS);
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
			standardVisibleColumns = Arrays.asList(Model1Schema.MODELNAME);
		}

		for (KnimeTuple tuple : tuples) {
			String id = ((EstModelXml) tuple.getPmmXml(Model1Schema.ATT_ESTMODEL)
					.get(0)).getID() + "";

			if (containsData) {
				id += "(" + tuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			}

			if (!idSet.add(id)) {
				continue;
			}

			ids.add(id);
			tupleMap.put(id, tuple);

			PmmXmlDoc modelXml = tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			String modelID = ((CatalogModelXml) modelXml.get(0)).getID() + "";
			String modelName = ((CatalogModelXml) modelXml.get(0)).getName();
			String formula = ((CatalogModelXml) modelXml.get(0)).getFormula();
			String depVar = ((DepXml) tuple.getPmmXml(Model1Schema.ATT_DEPENDENT)
					.get(0)).getName();
			PmmXmlDoc indepXml = tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);
			PmmXmlDoc paramXml = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
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

			PmmXmlDoc estModelXml = tuple.getPmmXml(Model1Schema.ATT_ESTMODEL);

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

			if (containsData) {
				String dataName;

				if (tuple.getString(TimeSeriesSchema.ATT_COMBASEID) != null) {
					dataName = tuple.getString(TimeSeriesSchema.ATT_COMBASEID);
				} else {
					dataName = "" + tuple.getInt(TimeSeriesSchema.ATT_CONDID);
				}

				stringColumnValues.get(1).add(dataName);

				for (int i = 0; i < miscParams.size(); i++) {
					boolean paramFound = false;

					for (PmmXmlElementConvertable el : tuple.getPmmXml(
							TimeSeriesSchema.ATT_MISC).getElementSet()) {
						MiscXml element = (MiscXml) el;

						if (miscParams.get(i).equals(element.getName())) {
							doubleColumnValues.get(i + 4).add(
									element.getValue());

							if (element.getValue() != null
									&& !element.getValue().isNaN()) {
								plotable.addValueList(element.getName(),
										Arrays.asList(element.getValue()));
							}

							paramFound = true;
							break;
						}
					}

					if (!paramFound) {
						doubleColumnValues.get(i + 4).add(null);
					}
				}

				if (!plotable.isPlotable()) {
					stringColumnValues.get(2).add(ChartConstants.FAILED);
				} else if (PmmUtilities.isOutOfRange(paramXml)) {
					stringColumnValues.get(2).add(ChartConstants.OUT_OF_LIMITS);
				} else if (PmmUtilities.covarianceMatrixMissing(paramXml)) {
					stringColumnValues.get(2).add(ChartConstants.NO_COVARIANCE);
				} else {
					stringColumnValues.get(2).add(ChartConstants.OK);
				}
			} else {
				if (!plotable.isPlotable()) {
					stringColumnValues.get(1).add(ChartConstants.FAILED);
				} else if (PmmUtilities.isOutOfRange(paramXml)) {
					stringColumnValues.get(1).add(ChartConstants.OUT_OF_LIMITS);
				} else if (PmmUtilities.covarianceMatrixMissing(paramXml)) {
					stringColumnValues.get(1).add(ChartConstants.NO_COVARIANCE);
				} else {
					stringColumnValues.get(1).add(ChartConstants.OK);
				}
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

	public List<String> getStandardVisibleColumns() {
		return standardVisibleColumns;
	}

	public List<List<String>> getInfoParameters() {
		return infoParameters;
	}

	public List<List<?>> getInfoParameterValues() {
		return infoParameterValues;
	}

	public Map<String, String> getShortLegend() {
		return shortLegend;
	}

	public Map<String, String> getLongLegend() {
		return longLegend;
	}
}
