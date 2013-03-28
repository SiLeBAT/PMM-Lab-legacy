package de.bund.bfr.knime.pmm.secondarymodelanddataview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.data.DataTable;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.QualityMeasurementComputation;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class TableReader {

	private List<String> ids;
	private List<Integer> colorCounts;
	private List<String> stringColumns;
	private List<List<String>> stringColumnValues;
	private List<String> doubleColumns;
	private List<List<Double>> doubleColumnValues;
	private List<Map<String, Double>> parameterData;
	private List<String> standardVisibleColumns;
	private List<String> filterableStringColumns;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;

	public TableReader(DataTable table, boolean schemaContainsData) {
		KnimeRelationReader reader;
		Set<String> idSet = new LinkedHashSet<String>();
		Map<String, String> formulaMap = new LinkedHashMap<>();
		Map<String, PmmXmlDoc> paramMap = new LinkedHashMap<>();
		Map<String, String> depVarMap = new LinkedHashMap<>();
		Map<String, PmmXmlDoc> indepVarMap = new LinkedHashMap<>();
		Map<String, List<Double>> depVarDataMap = new LinkedHashMap<>();
		Map<String, Map<String, List<Double>>> miscDataMaps = new LinkedHashMap<>();
		Map<String, Double> rmsMap = new LinkedHashMap<>();
		Map<String, Double> rSquaredMap = new LinkedHashMap<>();
		Map<String, Double> aicMap = new LinkedHashMap<>();
		Map<String, Double> bicMap = new LinkedHashMap<>();
		Map<String, Integer> dofMap = new LinkedHashMap<>();
		List<String> miscParams = null;
		List<KnimeTuple> tuples = new ArrayList<>();

		if (schemaContainsData) {
			reader = new KnimeRelationReader(
					SchemaFactory.createM12DataSchema(), table);
		} else {
			reader = new KnimeRelationReader(SchemaFactory.createM2Schema(),
					table);
		}

		while (reader.hasMoreElements()) {
			tuples.add(reader.nextElement());
		}

		ids = new ArrayList<String>();
		plotables = new LinkedHashMap<String, Plotable>();
		shortLegend = new LinkedHashMap<String, String>();
		longLegend = new LinkedHashMap<String, String>();
		parameterData = new ArrayList<>();
		stringColumns = Arrays.asList(Model2Schema.ATT_DEPENDENT,
				Model2Schema.MODELNAME, Model2Schema.FORMULA,
				ChartConstants.STATUS);
		filterableStringColumns = Arrays.asList(ChartConstants.STATUS);
		stringColumnValues = new ArrayList<List<String>>();
		stringColumnValues.add(new ArrayList<String>());
		stringColumnValues.add(new ArrayList<String>());
		stringColumnValues.add(new ArrayList<String>());
		stringColumnValues.add(new ArrayList<String>());
		standardVisibleColumns = new ArrayList<>(Arrays.asList(
				Model2Schema.ATT_DEPENDENT, Model2Schema.MODELNAME));

		if (schemaContainsData) {
			try {
				tuples = QualityMeasurementComputation.computeSecondary(tuples);
			} catch (Exception e) {
			}

			miscParams = PmmUtilities.getAllMiscParams(table);
			doubleColumns = new ArrayList<String>(Arrays.asList(
					Model2Schema.RMS, Model2Schema.RSQUARED, Model2Schema.AIC,
					Model2Schema.BIC));
			doubleColumnValues = new ArrayList<List<Double>>();
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
				standardVisibleColumns.add("Min " + param);
				standardVisibleColumns.add("Max " + param);
			}
		} else {
			doubleColumns = Arrays.asList(Model2Schema.RMS,
					Model2Schema.RSQUARED, Model2Schema.AIC, Model2Schema.BIC);
			doubleColumnValues = new ArrayList<List<Double>>();
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
			doubleColumnValues.add(new ArrayList<Double>());
		}

		for (KnimeTuple tuple : tuples) {
			String id = ((DepXml) tuple.getPmmXml(Model2Schema.ATT_DEPENDENT)
					.get(0)).getName()
					+ "("
					+ ((CatalogModelXml) tuple.getPmmXml(
							Model2Schema.ATT_MODELCATALOG).get(0)).getID()
					+ ")";

			if (!idSet.contains(id)) {
				PmmXmlDoc modelXmlSec = tuple
						.getPmmXml(Model2Schema.ATT_MODELCATALOG);
				PmmXmlDoc estModelXmlSec = tuple
						.getPmmXml(Model2Schema.ATT_ESTMODEL);
				String modelNameSec = ((CatalogModelXml) modelXmlSec.get(0))
						.getName();
				String formulaSec = ((CatalogModelXml) modelXmlSec.get(0))
						.getFormula();
				String depVarSec = ((DepXml) tuple.getPmmXml(
						Model2Schema.ATT_DEPENDENT).get(0)).getName();
				PmmXmlDoc paramXmlSec = tuple
						.getPmmXml(Model2Schema.ATT_PARAMETER);
				Map<String, Double> paramData = new LinkedHashMap<>();

				for (PmmXmlElementConvertable el : paramXmlSec.getElementSet()) {
					ParamXml element = (ParamXml) el;

					paramData.put(element.getName(), element.getValue());
					paramData.put(element.getName() + ": SE",
							element.getError());
					paramData.put(element.getName() + ": t", element.gett());
					paramData.put(element.getName() + ": Pr > |t|",
							element.getP());
				}

				idSet.add(id);
				ids.add(id);
				stringColumnValues.get(0).add(depVarSec);
				stringColumnValues.get(1).add(modelNameSec);
				stringColumnValues.get(2).add(formulaSec);
				parameterData.add(paramData);
				shortLegend.put(id, depVarSec);
				longLegend.put(id, depVarSec + " (" + modelNameSec + ")");

				formulaMap.put(id, formulaSec);
				depVarMap.put(id, depVarSec);
				indepVarMap.put(id,
						tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT));
				paramMap.put(id, paramXmlSec);
				depVarDataMap.put(id, new ArrayList<Double>());
				rmsMap.put(id, ((EstModelXml) estModelXmlSec.get(0)).getRMS());
				rSquaredMap.put(id,
						((EstModelXml) estModelXmlSec.get(0)).getR2());
				aicMap.put(id, ((EstModelXml) estModelXmlSec.get(0)).getAIC());
				bicMap.put(id, ((EstModelXml) estModelXmlSec.get(0)).getBIC());
				dofMap.put(id, ((EstModelXml) estModelXmlSec.get(0)).getDOF());

				if (schemaContainsData) {
					miscDataMaps.put(id,
							new LinkedHashMap<String, List<Double>>());

					for (String param : miscParams) {
						miscDataMaps.get(id)
								.put(param, new ArrayList<Double>());
					}
				}
			}

			if (schemaContainsData) {
				PmmXmlDoc paramXml = tuple
						.getPmmXml(Model1Schema.ATT_PARAMETER);
				String depVar = depVarMap.get(id);
				int depVarIndex = CellIO.getNameList(paramXml).indexOf(depVar);
				Double depVarValue = ((ParamXml) paramXml.get(depVarIndex))
						.getValue();

				depVarDataMap.get(id).add(depVarValue);

				PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

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
			Map<String, Map<String, Double>> covariances = new LinkedHashMap<>();
			boolean hasArguments = !indepVarMap.get(id).getElementSet()
					.isEmpty();

			if (schemaContainsData) {
				plotable = new Plotable(Plotable.BOTH_STRICT);
			} else {
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

				Map<String, Double> cov = new LinkedHashMap<String, Double>();

				for (PmmXmlElementConvertable el2 : paramMap.get(id)
						.getElementSet()) {
					cov.put(((ParamXml) el2).getName(), element
							.getCorrelation(((ParamXml) el2).getOrigName()));
				}

				covariances.put(element.getName(), cov);
			}

			plotable.setFunction(formulaMap.get(id));
			plotable.setFunctionValue(depVarMap.get(id));
			plotable.setFunctionArguments(arguments);
			plotable.setMinArguments(minArg);
			plotable.setMaxArguments(maxArg);
			plotable.setFunctionParameters(constants);
			plotable.setCovariances(covariances);
			plotable.setDegreesOfFreedom(dofMap.get(id));

			doubleColumnValues.get(0).add(rmsMap.get(id));
			doubleColumnValues.get(1).add(rSquaredMap.get(id));
			doubleColumnValues.get(2).add(bicMap.get(id));
			doubleColumnValues.get(3).add(aicMap.get(id));

			if (schemaContainsData) {
				List<Double> depVarData = depVarDataMap.get(id);
				Map<String, List<Double>> miscs = miscDataMaps.get(id);

				for (int i = 0; i < depVarData.size(); i++) {
					if (depVarData.get(i) == null) {
						depVarData.remove(i);

						for (String param : miscParams) {
							miscs.get(param).remove(i);
						}
					}
				}

				plotable.addValueList(depVarMap.get(id), depVarData);

				for (String param : miscParams) {
					plotable.addValueList(param, miscs.get(param));
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

						doubleColumnValues.get(2 * i + 4).add(
								Collections.min(nonNullValues));
						doubleColumnValues.get(2 * i + 5).add(
								Collections.max(nonNullValues));
					} else {
						doubleColumnValues.get(2 * i + 4).add(null);
						doubleColumnValues.get(2 * i + 5).add(null);
					}
				}

				colorCounts.add(plotable.getNumberOfCombinations());
			} else {
				if (!hasArguments) {
					plotable.getFunctionArguments().put("No argument",
							new ArrayList<Double>(Arrays.asList(0.0)));
				}
			}

			if (!plotable.isPlotable()) {
				stringColumnValues.get(3).add(ChartConstants.FAILED);
			} else if (PmmUtilities.isOutOfRange(paramMap.get(id))) {
				stringColumnValues.get(3).add(ChartConstants.OUT_OF_LIMITS);
			} else if (PmmUtilities.covarianceMatrixMissing(paramMap.get(id))) {
				stringColumnValues.get(3).add(ChartConstants.NO_COVARIANCE);
			} else {
				stringColumnValues.get(3).add(ChartConstants.OK);
			}

			plotables.put(id, plotable);
		}
	}

	public List<String> getIds() {
		return ids;
	}

	public List<Integer> getColorCounts() {
		return colorCounts;
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

	public List<Map<String, Double>> getParameterData() {
		return parameterData;
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
