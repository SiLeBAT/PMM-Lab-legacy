package de.bund.bfr.knime.pmm.jsonutil;

import java.util.HashMap;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.ParamXml;

public class JSONParam {

	JSONObject obj; // Json object
	
	// attribute keys
	private static final String ATT_NAME = "name";
	private static final String ATT_ORIGNAME = "origname";
	private static final String ATT_VALUE = "value";
	private static final String ATT_ERROR = "error";
	private static final String ATT_MIN = "min";
	private static final String ATT_MAX = "max";
	private static final String ATT_P = "P";
	private static final String ATT_T = "t";
	private static final String ATT_MINGUESS = "minGuess";
	private static final String ATT_MAXGUESS = "maxGuess";
	private static final String ATT_CATEGORY = "category";
	private static final String ATT_UNIT = "unit";
	private static final String ATT_DESCRIPTION = "description";
	private static final String ATT_CORRELATION = "correlation";


	public JSONParam(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONParam(ParamXml param) {
		obj = new JSONObject();

		obj.put(ATT_NAME, param.getName());
		obj.put(ATT_ORIGNAME, param.getOrigName());
		obj.put(ATT_VALUE, param.getValue());
		obj.put(ATT_ERROR, param.getError());
		obj.put(ATT_MIN, param.getMin());
		obj.put(ATT_MAX, param.getMax());
		obj.put(ATT_P, param.getP());
		obj.put(ATT_T, param.getT());
		obj.put(ATT_MINGUESS, param.getMinGuess());
		obj.put(ATT_MAXGUESS, param.getMaxGuess());
		obj.put(ATT_CATEGORY, param.getCategory());
		obj.put(ATT_UNIT, param.getUnit());
		obj.put(ATT_DESCRIPTION, param.getDescription());
		obj.put(ATT_CORRELATION, param.getAllCorrelations());
	}

	public JSONObject getObj() {
		return obj;
	}

	public ParamXml toParamXml() {
		String name = (String) obj.get(ATT_NAME);
		String origName = (String) obj.get(ATT_ORIGNAME);
		double value = (double) obj.get(ATT_VALUE);
		double error = (double) obj.get(ATT_ERROR);
		double min = (double) obj.get(ATT_MIN);
		double max = (double) obj.get(ATT_MAX);
		double P = (double) obj.get(ATT_P);
		double t = (double) obj.get(ATT_T);
		double minGuess = (double) obj.get(ATT_MINGUESS);
		double maxGuess = (double) obj.get(ATT_MAXGUESS);
		String category = (String) obj.get(ATT_CATEGORY);
		String unit = (String) obj.get(ATT_UNIT);
		String description = (String) obj.get(ATT_DESCRIPTION);
		HashMap<String, Double> correlations = (HashMap<String, Double>) obj
				.get(ATT_CORRELATION);

		return new ParamXml(name, origName, value, error, min, max, P, t,
				minGuess, maxGuess, category, unit, description, correlations);
	}
}
