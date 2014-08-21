/*******************************************************************************
 * PMM-Lab � 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab � 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
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
package de.bund.bfr.knime.pmm.secondarymodelanddataview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.knime.core.data.DataTable;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.QualityMeasurementComputation;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
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
	private List<String> formulas;
	private List<Map<String, Double>> parameterData;
	private List<String> conditions;
	private List<List<Double>> conditionMinValues;
	private List<List<Double>> conditionMaxValues;
	private List<List<String>> conditionUnits;
	private List<String> standardVisibleColumns;
	private List<String> filterableStringColumns;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;

	public TableReader(DataTable table, boolean schemaContainsData) {
		Set<String> idSet = new LinkedHashSet<>();
		Map<String, String> formulaMap = new LinkedHashMap<>();
		Map<String, PmmXmlDoc> paramMap = new LinkedHashMap<>();
		Map<String, String> depVarMap = new LinkedHashMap<>();
		Map<String, PmmXmlDoc> indepVarMap = new LinkedHashMap<>();
		Map<String, List<Double>> depVarDataMap = new LinkedHashMap<>();
		Map<String, Map<String, List<Double>>> miscDataMaps = new LinkedHashMap<>();
		Map<String, Map<String, String>> miscUnitMaps = new LinkedHashMap<>();
		Map<String, Double> sseMap = new LinkedHashMap<>();
		Map<String, Double> rmsMap = new LinkedHashMap<>();
		Map<String, Double> rSquaredMap = new LinkedHashMap<>();
		Map<String, Double> aicMap = new LinkedHashMap<>();		
		Map<String, Integer> dofMap = new LinkedHashMap<>();
		Map<String, Set<String>> agentMap = new LinkedHashMap<>();
		Map<String, Set<String>> matrixMap = new LinkedHashMap<>();
		List<String> miscParams = null;
		Map<String, List<String>> miscCategories = null;
		List<KnimeTuple> tuples;

		if (schemaContainsData) {
			tuples = PmmUtilities.getTuples(table,
					SchemaFactory.createM12DataSchema());
		} else {
			tuples = PmmUtilities.getTuples(table,
					SchemaFactory.createM2Schema());
		}

		ids = new ArrayList<>();
		plotables = new LinkedHashMap<>();
		shortLegend = new LinkedHashMap<>();
		longLegend = new LinkedHashMap<>();
		formulas = new ArrayList<>();
		parameterData = new ArrayList<>();
		doubleColumns = Arrays.asList(Model2Schema.SSE, Model2Schema.MSE,
				Model2Schema.RMSE, Model2Schema.RSQUARED, Model2Schema.AIC);
		doubleColumnValues = new ArrayList<>();
		doubleColumnValues.add(new ArrayList<Double>());
		doubleColumnValues.add(new ArrayList<Double>());
		doubleColumnValues.add(new ArrayList<Double>());
		doubleColumnValues.add(new ArrayList<Double>());
		doubleColumnValues.add(new ArrayList<Double>());
		filterableStringColumns = Arrays.asList(ChartConstants.STATUS);
		standardVisibleColumns = new ArrayList<>(Arrays.asList(
				ChartSelectionPanel.FORMULA, ChartSelectionPanel.PARAMETERS));

		if (schemaContainsData) {
			try {
				tuples = QualityMeasurementComputation.computeSecondary(tuples);
			} catch (Exception e) {
			}

			miscParams = PmmUtilities.getMiscParams(tuples);
			miscCategories = PmmUtilities.getMiscCategories(tuples);

			colorCounts = new ArrayList<>();
			conditions = new ArrayList<>();
			conditionMinValues = new ArrayList<>();
			conditionMaxValues = new ArrayList<>();
			conditionUnits = new ArrayList<>();

			for (String param : miscParams) {
				conditions.add(param);
				conditionMinValues.add(new ArrayList<Double>());
				conditionMaxValues.add(new ArrayList<Double>());
				conditionUnits.add(new ArrayList<String>());
				standardVisibleColumns.add(param);
			}

			stringColumns = Arrays.asList(Model2Schema.ATT_DEPENDENT,
					Model2Schema.FORMULA, Model2Schema.ATT_EMLIT,
					ChartConstants.STATUS, TimeSeriesSchema.ATT_AGENT,
					TimeSeriesSchema.ATT_MATRIX);
			stringColumnValues = new ArrayList<>();
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
		} else {
			conditions = null;
			conditionMinValues = null;
			conditionMaxValues = null;
			conditionUnits = null;

			stringColumns = Arrays.asList(Model2Schema.ATT_DEPENDENT,
					Model2Schema.FORMULA, Model2Schema.ATT_EMLIT,
					ChartConstants.STATUS);
			stringColumnValues = new ArrayList<>();
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
			stringColumnValues.add(new ArrayList<String>());
		}

		standardVisibleColumns.addAll(stringColumns);
		standardVisibleColumns.addAll(doubleColumns);

		for (KnimeTuple tuple : tuples) {
			DepXml depXml = (DepXml) tuple
					.getPmmXml(Model2Schema.ATT_DEPENDENT).get(0);
			EstModelXml estXml = (EstModelXml) tuple.getPmmXml(
					Model2Schema.ATT_ESTMODEL).get(0);
			String id = depXml.getName() + estXml.getId();

			if (schemaContainsData) {
				CatalogModelXml primModelXml = (CatalogModelXml) tuple
						.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0);

				id += " (" + primModelXml.getId() + ")";
			}

			if (!idSet.contains(id)) {
				CatalogModelXml modelXmlSec = (CatalogModelXml) tuple
						.getPmmXml(Model2Schema.ATT_MODELCATALOG).get(0);
				PmmXmlDoc estModelXmlSec = tuple
						.getPmmXml(Model2Schema.ATT_ESTMODEL);
				String modelNameSec = modelXmlSec.getName();
				String formulaSec = MathUtilities
						.getAllButBoundaryCondition(modelXmlSec.getFormula());
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
					paramData.put(element.getName() + ": t", element.getT());
					paramData.put(element.getName() + ": Pr > |t|",
							element.getP());
				}

				String depVarSecDesc = depVarSec;

				if (schemaContainsData) {
					CatalogModelXml primModelXml = (CatalogModelXml) tuple
							.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0);

					depVarSecDesc += " (" + primModelXml.getName() + ")";
				}

				String literature = "";

				for (PmmXmlElementConvertable el : tuple.getPmmXml(
						Model2Schema.ATT_EMLIT).getElementSet()) {
					literature += "," + (LiteratureItem) el;
				}

				if (!literature.isEmpty()) {
					literature = literature.substring(1);
				}

				idSet.add(id);
				ids.add(id);
				stringColumnValues.get(0).add(depVarSecDesc);
				stringColumnValues.get(1).add(modelNameSec);
				stringColumnValues.get(2).add(literature);
				formulas.add(formulaSec);
				parameterData.add(paramData);
				shortLegend.put(id, depVarSec);
				longLegend.put(id, depVarSec + " (" + modelNameSec + ")");

				formulaMap.put(id, modelXmlSec.getFormula());
				depVarMap.put(id, depVarSec);
				indepVarMap.put(id,
						tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT));
				paramMap.put(id, paramXmlSec);
				depVarDataMap.put(id, new ArrayList<Double>());
				sseMap.put(id, ((EstModelXml) estModelXmlSec.get(0)).getSse());
				rmsMap.put(id, ((EstModelXml) estModelXmlSec.get(0)).getRms());
				rSquaredMap.put(id,
						((EstModelXml) estModelXmlSec.get(0)).getR2());
				aicMap.put(id, ((EstModelXml) estModelXmlSec.get(0)).getAic());				
				dofMap.put(id, ((EstModelXml) estModelXmlSec.get(0)).getDof());
				agentMap.put(id, new LinkedHashSet<String>());
				matrixMap.put(id, new LinkedHashSet<String>());

				if (schemaContainsData) {
					miscDataMaps.put(id,
							new LinkedHashMap<String, List<Double>>());
					miscUnitMaps.put(id, new LinkedHashMap<String, String>());

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
							miscUnitMaps.get(id).put(param, element.getUnit());
							break;
						}
					}

					miscDataMaps.get(id).get(param).add(paramValue);
				}

				AgentXml agent = (AgentXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_AGENT).get(0);
				MatrixXml matrix = (MatrixXml) tuple.getPmmXml(
						TimeSeriesSchema.ATT_MATRIX).get(0);

				if (agent != null) {
					agentMap.get(id).add(agent.getName());
				}

				if (matrix != null) {
					matrixMap.get(id).add(matrix.getName());
				}
			}
		}

		for (String id : ids) {
			Plotable plotable = null;
			Map<String, List<Double>> arguments = new LinkedHashMap<>();
			Map<String, Double> minArg = new LinkedHashMap<>();
			Map<String, Double> maxArg = new LinkedHashMap<>();
			Map<String, Double> constants = new LinkedHashMap<>();
			Map<String, Map<String, Double>> covariances = new LinkedHashMap<>();
			Map<String, List<String>> categories = new LinkedHashMap<>();
			Map<String, String> units = new LinkedHashMap<>();
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
						new ArrayList<>(Arrays.asList(0.0)));
				minArg.put(element.getName(), element.getMin());
				maxArg.put(element.getName(), element.getMax());
				categories.put(element.getName(),
						Arrays.asList(element.getCategory()));
				units.put(element.getName(), element.getUnit());
			}

			for (PmmXmlElementConvertable el : paramMap.get(id).getElementSet()) {
				ParamXml element = (ParamXml) el;

				constants.put(element.getName(), element.getValue());

				Map<String, Double> cov = new LinkedHashMap<>();

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

			doubleColumnValues.get(0).add(sseMap.get(id));
			doubleColumnValues.get(1).add(MathUtilities.getMSE(rmsMap.get(id)));
			doubleColumnValues.get(2).add(rmsMap.get(id));
			doubleColumnValues.get(3).add(rSquaredMap.get(id));
			doubleColumnValues.get(4).add(aicMap.get(id));

			if (schemaContainsData) {
				stringColumnValues.get(4).add(toString(agentMap.get(id)));
				stringColumnValues.get(5).add(toString(matrixMap.get(id)));

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

					if (categories.get(param) == null
							|| categories.get(param).equals(
									Arrays.asList((String) null))) {
						categories.put(param, miscCategories.get(param));
					}

					if (units.get(param) == null) {
						units.put(param, miscUnitMaps.get(id).get(param));
					}
				}

				for (int i = 0; i < miscParams.size(); i++) {
					List<Double> nonNullValues = new ArrayList<>(
							miscs.get(miscParams.get(i)));
					Double min = null;
					Double max = null;
					String unit = null;

					nonNullValues.removeAll(Arrays.asList((Double) null));

					if (!nonNullValues.isEmpty()) {
						if (!hasArguments) {
							plotable.getFunctionArguments().put(
									miscParams.get(i),
									new ArrayList<>(Arrays.asList(0.0)));
						}

						min = Collections.min(nonNullValues);
						max = Collections.max(nonNullValues);
						unit = miscUnitMaps.get(id).get(miscParams.get(i));
					}

					conditionMinValues.get(i).add(min);
					conditionMaxValues.get(i).add(max);
					conditionUnits.get(i).add(unit);
				}

				colorCounts.add(plotable.getNumberOfCombinations());
			} else {
				if (!hasArguments) {
					plotable.getFunctionArguments().put("No argument",
							new ArrayList<>(Arrays.asList(0.0)));
				}
			}

			plotable.setCategories(categories);
			plotable.setUnits(units);

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

	public List<String> getFormulas() {
		return formulas;
	}

	public List<Map<String, Double>> getParameterData() {
		return parameterData;
	}

	public List<String> getConditions() {
		return conditions;
	}

	public List<List<Double>> getConditionMinValues() {
		return conditionMinValues;
	}

	public List<List<Double>> getConditionMaxValues() {
		return conditionMaxValues;
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

	private static String toString(Collection<String> c) {
		String result = "";

		for (String s : c) {
			result += s + ",";
		}

		if (!result.isEmpty()) {
			result = result.substring(0, result.length() - 1);
		}

		return result;
	}

}
