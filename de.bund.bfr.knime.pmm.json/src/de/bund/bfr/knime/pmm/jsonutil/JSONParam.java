package de.bund.bfr.knime.pmm.jsonutil;

import java.util.HashMap;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.ParamXml;

public class JSONParam {

	JSONObject obj; // Json object

	public JSONParam(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONParam(ParamXml param) {
		obj = new JSONObject();

		obj.put("name", param.getName());
		obj.put("origName", param.getOrigName());
		obj.put("value", param.getValue());
		obj.put("error", param.getError());
		obj.put("min", param.getMin());
		obj.put("max", param.getMax());
		obj.put("P", param.getP());
		obj.put("T", param.getT());
		obj.put("minGuess", param.getMinGuess());
		obj.put("maxGuess", param.getMaxGuess());
		obj.put("category", param.getCategory());
		obj.put("unit", param.getUnit());
		obj.put("description", param.getDescription());
		obj.put("correlations", param.getAllCorrelations());

	}

	public JSONObject getObj() {
		return obj;
	}

	public ParamXml toParamXml() {
		String name = (String) obj.get("name");
		String origName = (String) obj.get("origName");
		double value = (double) obj.get("value");
		double error = (double) obj.get("error");
		double min = (double) obj.get("min");
		double max = (double) obj.get("max");
		double P = (double) obj.get("P");
		double t = (double) obj.get("T");
		double minGuess = (double) obj.get("minGuess");
		double maxGuess = (double) obj.get("maxGuess");
		String category = (String) obj.get("category");
		String unit = (String) obj.get("unit");
		String description = (String) obj.get("description");
		HashMap<String, Double> correlations = (HashMap<String, Double>) obj
				.get("correlations");

		return new ParamXml(name, origName, value, error, min, max, P, t,
				minGuess, maxGuess, category, unit, description, correlations);
	}
}
