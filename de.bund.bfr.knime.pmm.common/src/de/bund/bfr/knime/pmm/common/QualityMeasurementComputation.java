package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class QualityMeasurementComputation {

	private QualityMeasurementComputation() {
	}

	public static List<KnimeTuple> computePrimary(List<KnimeTuple> tuples)
			throws PmmException {
		Map<String, KnimeTuple> tupleMap = new LinkedHashMap<String, KnimeTuple>();
		Map<String, Set<Integer>> usedCondIDs = new LinkedHashMap<String, Set<Integer>>();
		Map<String, List<Double>> targetValueMap = new LinkedHashMap<String, List<Double>>();
		Map<String, Map<String, List<Double>>> variableValueMap = new LinkedHashMap<String, Map<String, List<Double>>>();

		for (KnimeTuple tuple : tuples) {
			if (((EstModelXml) tuple.getPmmXml(Model1Schema.ATT_ESTMODEL)
					.get(0)).getID() == null) {
				continue;
			}

			String id = ((EstModelXml) tuple.getPmmXml(
					Model1Schema.ATT_ESTMODEL).get(0)).getID()
					+ "";

			if (tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT).size() <= 1) {
				id += "(" + tuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			}

			if (!tupleMap.containsKey(id)) {
				PmmXmlDoc indepXml = tuple
						.getPmmXml(Model1Schema.ATT_INDEPENDENT);

				tupleMap.put(id, tuple);
				usedCondIDs.put(id, new LinkedHashSet<Integer>());
				targetValueMap.put(id, new ArrayList<Double>());
				variableValueMap.put(id,
						new LinkedHashMap<String, List<Double>>());

				for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
					IndepXml element = (IndepXml) el;

					variableValueMap.get(id).put(element.getName(),
							new ArrayList<Double>());
				}
			}

			if (!usedCondIDs.get(id).add(
					tuple.getInt(TimeSeriesSchema.ATT_CONDID))) {
				continue;
			}

			List<Double> targetValues = targetValueMap.get(id);
			Map<String, List<Double>> variableValues = variableValueMap.get(id);
			Map<String, Double> miscValues = new LinkedHashMap<String, Double>();
			PmmXmlDoc miscXml = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
			List<String> miscNames = CellIO.getNameList(miscXml);
			boolean miscMissing = false;

			for (String var : variableValues.keySet()) {
				if (var.equals(AttributeUtilities.TIME)) {
					continue;
				} else {
					if (!miscNames.contains(var)) {
						miscMissing = true;
						break;
					}

					Double value = ((MiscXml) miscXml.get(miscNames
							.indexOf(var))).getValue();

					if (value == null) {
						miscMissing = true;
						break;
					} else {
						miscValues.put(var, value);
					}
				}
			}

			if (miscMissing) {
				continue;
			}

			PmmXmlDoc timeSeriesXml = tuple
					.getPmmXml(TimeSeriesSchema.ATT_TIMESERIES);

			for (PmmXmlElementConvertable el : timeSeriesXml.getElementSet()) {
				TimeSeriesXml element = (TimeSeriesXml) el;

				if (element.getTime() != null && element.getLog10C() != null) {
					targetValues.add(element.getLog10C());
					variableValues.get(AttributeUtilities.TIME).add(
							element.getTime());

					for (String var : variableValues.keySet()) {
						if (!var.equals(AttributeUtilities.TIME)) {
							variableValues.get(var).add(miscValues.get(var));
						}
					}
				}
			}
		}

		Map<String, Double> rmsMap = new LinkedHashMap<String, Double>();
		Map<String, Double> rSquaredMap = new LinkedHashMap<String, Double>();
		Map<String, Double> aicMap = new LinkedHashMap<String, Double>();
		Map<String, Double> bicMap = new LinkedHashMap<String, Double>();

		for (String id : tupleMap.keySet()) {
			KnimeTuple tuple = tupleMap.get(id);
			List<Double> targetValues = targetValueMap.get(id);
			Map<String, List<Double>> variableValues = variableValueMap.get(id);

			DJep parser = MathUtilities.createParser();
			String formula = ((CatalogModelXml) tuple.getPmmXml(
					Model1Schema.ATT_MODELCATALOG).get(0)).getFormula();
			Node function = null;

			try {
				function = parser
						.parse(formula.substring(formula.indexOf("=") + 1));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			PmmXmlDoc paramXml = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml element = (ParamXml) el;

				parser.addVariable(element.getName(), element.getValue());
			}

			for (String var : variableValues.keySet()) {
				parser.addVariable(var, 0.0);
			}

			double rms = 0.0;
			List<Double> usedTargetValues = new ArrayList<Double>();

			for (int i = 0; i < targetValues.size(); i++) {
				Object value = null;

				for (String var : variableValues.keySet()) {
					parser.setVarValue(var, variableValues.get(var).get(i));
				}

				try {
					value = parser.evaluate(function);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				if (value instanceof Double && !((Double) value).isNaN()
						&& !((Double) value).isInfinite()) {
					double diff = targetValues.get(i) - (Double) value;

					rms += diff * diff;
					usedTargetValues.add(targetValues.get(i));
				}
			}

			if (!usedTargetValues.isEmpty()) {
				rms = Math.sqrt(rms / usedTargetValues.size());

				Double rSquared = MathUtilities.getRSquared(rms,
						usedTargetValues);
				Double aic = MathUtilities.akaikeCriterion(paramXml
						.getElementSet().size(), usedTargetValues.size(), rms);
				Double bic = MathUtilities.bayesCriterion(paramXml
						.getElementSet().size(), usedTargetValues.size(), rms);

				rmsMap.put(id, rms);
				rSquaredMap.put(id, rSquared);
				aicMap.put(id, aic);
				bicMap.put(id, bic);
			}
		}

		List<KnimeTuple> newTuples = new ArrayList<KnimeTuple>();

		for (KnimeTuple tuple : tuples) {
			KnimeTuple newTuple = new KnimeTuple(tuple.getSchema(), tuple
					.getSchema().createSpec(), tuple);

			if (((EstModelXml) tuple.getPmmXml(Model1Schema.ATT_ESTMODEL)
					.get(0)).getID() != null) {
				String id = ((EstModelXml) tuple.getPmmXml(
						Model1Schema.ATT_ESTMODEL).get(0)).getID()
						+ "";

				if (tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT).size() <= 1) {
					id += "(" + tuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
				}

				if (rmsMap.containsKey(id)) {
					PmmXmlDoc estModelXml = newTuple
							.getPmmXml(Model1Schema.ATT_ESTMODEL);

					((EstModelXml) estModelXml.get(0)).setRMS(rmsMap.get(id));
					((EstModelXml) estModelXml.get(0)).setR2(rSquaredMap
							.get(id));
					((EstModelXml) estModelXml.get(0)).setAIC(aicMap.get(id));
					((EstModelXml) estModelXml.get(0)).setBIC(bicMap.get(id));

					newTuple.setValue(Model1Schema.ATT_ESTMODEL, estModelXml);
				}
			}

			newTuples.add(newTuple);
		}

		return newTuples;
	}

	public static List<KnimeTuple> computeSecondary(List<KnimeTuple> tuples)
			throws PmmException {
		Set<String> idSet = new LinkedHashSet<>();
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
		List<String> miscParams = getAllMiscParams(tuples);

		for (KnimeTuple tuple : tuples) {
			String id = ((DepXml) tuple.getPmmXml(Model2Schema.ATT_DEPENDENT)
					.get(0)).getName();

			if (!idSet.contains(id)) {
				PmmXmlDoc modelXmlSec = tuple
						.getPmmXml(Model2Schema.ATT_MODELCATALOG);
				String formulaSec = ((CatalogModelXml) modelXmlSec.get(0))
						.getFormula();
				String depVarSec = ((DepXml) tuple.getPmmXml(
						Model2Schema.ATT_DEPENDENT).get(0)).getName();
				PmmXmlDoc paramXmlSec = tuple
						.getPmmXml(Model2Schema.ATT_PARAMETER);

				idSet.add(id);
				formulaMap.put(id, formulaSec);
				depVarMap.put(id, depVarSec);
				indepVarMap.put(id,
						tuple.getPmmXml(Model2Schema.ATT_INDEPENDENT));
				paramMap.put(id, paramXmlSec);
				depVarDataMap.put(id, new ArrayList<Double>());
				miscDataMaps.put(id, new LinkedHashMap<String, List<Double>>());

				for (String param : miscParams) {
					miscDataMaps.get(id).put(param, new ArrayList<Double>());
				}
			}

			PmmXmlDoc paramXml = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);
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

		for (String id : idSet) {
			DJep parser = MathUtilities.createParser();
			Node function = null;
			String formula = formulaMap.get(id);
			List<Double> depVarData = depVarDataMap.get(id);
			Map<String, List<Double>> miscs = miscDataMaps.get(id);
			List<String> indepVars = CellIO.getNameList(indepVarMap.get(id));

			for (int i = 0; i < depVarData.size(); i++) {
				boolean isNull = depVarData.get(i) == null;

				if (!isNull) {
					for (String var : indepVars) {
						if (miscs.get(var).get(i) == null) {
							isNull = true;
							break;
						}
					}
				}

				if (isNull) {
					depVarData.remove(i);

					for (String param : miscParams) {
						miscs.get(param).remove(i);
					}
				}
			}

			try {
				function = parser
						.parse(formula.substring(formula.indexOf("=") + 1));
			} catch (ParseException e) {
				e.printStackTrace();
			}

			for (PmmXmlElementConvertable el : paramMap.get(id).getElementSet()) {
				ParamXml element = (ParamXml) el;

				parser.addVariable(element.getName(), element.getValue());
			}

			for (String var : indepVars) {
				parser.addVariable(var, 0.0);
			}

			double rms = 0.0;
			List<Double> usedTargetValues = new ArrayList<Double>();

			for (int i = 0; i < depVarData.size(); i++) {
				Object value = null;

				for (String var : indepVars) {
					parser.setVarValue(var, miscs.get(var).get(i));
				}

				try {
					value = parser.evaluate(function);
				} catch (ParseException e) {					
				}

				if (value instanceof Double && !((Double) value).isNaN()
						&& !((Double) value).isInfinite()) {
					double diff = depVarData.get(i) - (Double) value;

					rms += diff * diff;
					usedTargetValues.add(depVarData.get(i));
				}
			}

			if (!usedTargetValues.isEmpty()) {
				rms = Math.sqrt(rms / usedTargetValues.size());

				Double rSquared = MathUtilities.getRSquared(rms,
						usedTargetValues);
				Double aic = MathUtilities.akaikeCriterion(paramMap.get(id)
						.getElementSet().size(), usedTargetValues.size(), rms);
				Double bic = MathUtilities.bayesCriterion(paramMap.get(id)
						.getElementSet().size(), usedTargetValues.size(), rms);

				rmsMap.put(id, rms);
				rSquaredMap.put(id, rSquared);
				aicMap.put(id, aic);
				bicMap.put(id, bic);
			}
		}

		List<KnimeTuple> newTuples = new ArrayList<KnimeTuple>();

		for (KnimeTuple tuple : tuples) {
			KnimeTuple newTuple = new KnimeTuple(tuple.getSchema(), tuple
					.getSchema().createSpec(), tuple);
			String id = ((DepXml) newTuple
					.getPmmXml(Model2Schema.ATT_DEPENDENT).get(0)).getName();

			if (rmsMap.containsKey(id)) {
				PmmXmlDoc estModelXml = newTuple
						.getPmmXml(Model2Schema.ATT_ESTMODEL);

				((EstModelXml) estModelXml.get(0)).setRMS(rmsMap.get(id));
				((EstModelXml) estModelXml.get(0)).setR2(rSquaredMap.get(id));
				((EstModelXml) estModelXml.get(0)).setAIC(aicMap.get(id));
				((EstModelXml) estModelXml.get(0)).setBIC(bicMap.get(id));

				newTuple.setValue(Model2Schema.ATT_ESTMODEL, estModelXml);
			}

			newTuples.add(newTuple);
		}

		return newTuples;
	}

	private static List<String> getAllMiscParams(List<KnimeTuple> tuples)
			throws PmmException {
		Set<String> paramSet = new LinkedHashSet<String>();

		for (KnimeTuple tuple : tuples) {
			PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : misc.getElementSet()) {
				MiscXml element = (MiscXml) el;

				paramSet.add(element.getName());
			}
		}

		return new ArrayList<String>(paramSet);
	}

}
