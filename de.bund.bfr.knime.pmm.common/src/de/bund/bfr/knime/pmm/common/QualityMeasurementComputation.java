package de.bund.bfr.knime.pmm.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
		Map<Integer, KnimeTuple> tupleMap = new LinkedHashMap<Integer, KnimeTuple>();
		Map<Integer, List<Double>> targetValueMap = new LinkedHashMap<Integer, List<Double>>();
		Map<Integer, Map<String, List<Double>>> variableValueMap = new LinkedHashMap<Integer, Map<String, List<Double>>>();

		for (KnimeTuple tuple : tuples) {
			Integer estID = tuple.getInt(Model1Schema.ATT_ESTMODELID);

			if (estID == null) {
				continue;
			}

			if (!tupleMap.containsKey(estID)) {
				PmmXmlDoc indepXml = tuple
						.getPmmXml(Model1Schema.ATT_INDEPENDENT);

				tupleMap.put(estID, tuple);
				targetValueMap.put(estID, new ArrayList<Double>());
				variableValueMap.put(estID,
						new LinkedHashMap<String, List<Double>>());

				for (PmmXmlElementConvertable el : indepXml.getElementSet()) {
					IndepXml element = (IndepXml) el;

					variableValueMap.get(estID).put(element.getName(),
							new ArrayList<Double>());
				}
			}

			List<Double> targetValues = targetValueMap.get(estID);
			Map<String, List<Double>> variableValues = variableValueMap
					.get(estID);
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
						variableValues.get(var).add(miscValues.get(var));
					}
				}
			}
		}

		Map<Integer, Double> rmsMap = new LinkedHashMap<Integer, Double>();
		Map<Integer, Double> rSquaredMap = new LinkedHashMap<Integer, Double>();
		Map<Integer, Double> aicMap = new LinkedHashMap<Integer, Double>();
		Map<Integer, Double> bicMap = new LinkedHashMap<Integer, Double>();

		for (Integer estID : tupleMap.keySet()) {
			KnimeTuple tuple = tupleMap.get(estID);
			List<Double> targetValues = targetValueMap.get(estID);
			Map<String, List<Double>> variableValues = variableValueMap
					.get(estID);

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

				parser.addConstant(element.getName(), element.getValue());
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

			rms = Math.sqrt(rms / usedTargetValues.size());

			double rSquared = MathUtilities.getRSquared(rms, usedTargetValues);
			double aic = MathUtilities.akaikeCriterion(variableValues.keySet()
					.size(), usedTargetValues.size(), rms);
			double bic = MathUtilities.bayesCriterion(variableValues.keySet()
					.size(), usedTargetValues.size(), rms);

			rmsMap.put(estID, rms);
			rSquaredMap.put(estID, rSquared);
			aicMap.put(estID, aic);
			bicMap.put(estID, bic);
		}

		List<KnimeTuple> newTuples = new ArrayList<KnimeTuple>();

		for (KnimeTuple tuple : tuples) {
			KnimeTuple newTuple = new KnimeTuple(tuple.getSchema(), tuple
					.getSchema().createSpec(), tuple);
			Integer estID = newTuple.getInt(Model1Schema.ATT_ESTMODELID);

			if (rmsMap.containsKey(estID)) {
				newTuple.setValue(Model1Schema.ATT_RMS, rmsMap.get(estID));
				newTuple.setValue(Model1Schema.ATT_RSQUARED,
						rSquaredMap.get(estID));
				newTuple.setValue(Model1Schema.ATT_AIC, aicMap.get(estID));
				newTuple.setValue(Model1Schema.ATT_BIC, bicMap.get(estID));
			}

			newTuples.add(newTuple);
		}

		return newTuples;
	}

	public static List<KnimeTuple> computeSecondary(List<KnimeTuple> tuples) {
		return null;
	}

}
