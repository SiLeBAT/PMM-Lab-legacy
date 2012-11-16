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
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
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
			if (tuple.getInt(Model1Schema.ATT_ESTMODELID) == null) {
				continue;
			}

			String id;

			if (tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT).size() > 1) {
				id = tuple.getInt(Model1Schema.ATT_ESTMODELID) + "";
			} else {
				id = tuple.getInt(Model1Schema.ATT_ESTMODELID) + "("
						+ tuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
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
				if (var.equals(TimeSeriesSchema.TIME)) {
					continue;
				} else if (var.equals(TimeSeriesSchema.ATT_TEMPERATURE)
						|| var.equals(TimeSeriesSchema.ATT_PH)
						|| var.equals(TimeSeriesSchema.ATT_WATERACTIVITY)) {
					Double value = tuple.getDouble(var);

					if (value == null) {
						miscMissing = true;
						break;
					} else {
						miscValues.put(var, value);
					}
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
					variableValues.get(TimeSeriesSchema.TIME).add(
							element.getTime());

					for (String var : variableValues.keySet()) {
						if (!var.equals(TimeSeriesSchema.TIME)) {
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
			String formula = tuple.getString(Model1Schema.ATT_FORMULA);
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

				if (value instanceof Double) {
					double diff = targetValues.get(i) - (Double) value;

					rms += diff * diff;
					usedTargetValues.add(targetValues.get(i));
				}
			}

			if (!usedTargetValues.isEmpty()) {
				rms = Math.sqrt(rms / usedTargetValues.size());

				double rSquared = MathUtilities.getRSquared(rms,
						usedTargetValues);
				double aic = MathUtilities.akaikeCriterion(paramXml
						.getElementSet().size(), usedTargetValues.size(), rms);
				double bic = MathUtilities.bayesCriterion(paramXml
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

			if (newTuple.getInt(Model1Schema.ATT_ESTMODELID) == null) {
				continue;
			}

			String id;

			if (tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT).size() > 1) {
				id = tuple.getInt(Model1Schema.ATT_ESTMODELID) + "";
			} else {
				id = tuple.getInt(Model1Schema.ATT_ESTMODELID) + "("
						+ tuple.getInt(TimeSeriesSchema.ATT_CONDID) + ")";
			}

			if (rmsMap.containsKey(id)) {
				newTuple.setValue(Model1Schema.ATT_RMS, rmsMap.get(id));
				newTuple.setValue(Model1Schema.ATT_RSQUARED,
						rSquaredMap.get(id));
				newTuple.setValue(Model1Schema.ATT_AIC, aicMap.get(id));
				newTuple.setValue(Model1Schema.ATT_BIC, bicMap.get(id));
			}

			newTuples.add(newTuple);
		}

		return newTuples;
	}

	public static List<KnimeTuple> computeSecondary(List<KnimeTuple> tuples) {
		return null;
	}

}
