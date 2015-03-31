package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.EstModelXml;

public class JSONEstModel {
	JSONObject obj; // Json object

	public JSONEstModel(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONEstModel(EstModelXml model) {
		obj = new JSONObject();

		obj.put("id", model.getId());
		obj.put("name", model.getName());
		obj.put("sse", model.getSse());
		obj.put("rms", model.getRms());
		obj.put("r2", model.getR2());
		obj.put("aic", model.getAic());
		obj.put("dof", model.getDof());
		obj.put("qualityScore", model.getQualityScore());
		obj.put("dbuuid", model.getDbuuid());
	}

	public JSONObject getObj() {
		return obj;
	}
	
	public EstModelXml toEstModelXml() {
		int id = (int) obj.get("id");
		String name = (String) obj.get("name");
		double sse = (double) obj.get("sse");
		double rms = (double) obj.get("rms");
		double r2 = (double) obj.get("r2");
		double aic = (double) obj.get("aic");
		double bic = (double) obj.get("bic");
		int dof = (int) obj.get("dof");
		int qualityScore = (int) obj.get("qualityScore");
		
		EstModelXml modelXml = new EstModelXml(id, name, sse, rms, r2, aic, bic, dof);
		modelXml.setQualityScore(qualityScore);
		return modelXml;
	}
}