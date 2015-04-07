/**
 * Code and decode an EstModelXml into/from JSON.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.EstModelXml;

public class JSONEstModel {
	JSONObject obj; // Json object

	// attribute keys
	private static final String ATT_ID = "id";
	private static final String ATT_NAME = "name";
	private static final String ATT_SSE = "sse";
	private static final String ATT_RMS = "rms";
	private static final String ATT_R2 = "r2";
	private static final String ATT_AIC = "aic";
	private static final String ATT_BIC = "bic";
	private static final String ATT_DOF = "dof";
	private static final String ATT_QUALITYSCORE = "qualityScore";
	private static final String ATT_CHECKED = "checked";
	private static final String ATT_COMMENT = "comment";
	private static final String ATT_DBUUID = "dbuuid";

	public JSONEstModel(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONEstModel(EstModelXml model) {
		obj = new JSONObject();

		obj.put(ATT_ID, model.getId());
		obj.put(ATT_NAME, model.getName());
		obj.put(ATT_SSE, model.getSse());
		obj.put(ATT_RMS, model.getRms());
		obj.put(ATT_R2, model.getR2());
		obj.put(ATT_AIC, model.getAic());
		obj.put(ATT_BIC, model.getBic());
		obj.put(ATT_DOF, model.getDof());
		obj.put(ATT_QUALITYSCORE, model.getQualityScore());
		obj.put(ATT_CHECKED, model.getChecked());
		obj.put(ATT_COMMENT, model.getComment());
		obj.put(ATT_DBUUID, model.getDbuuid());
	}

	public JSONObject getObj() {
		return obj;
	}
	
	public EstModelXml toEstModelXml() {
		int id = ((Long) obj.get(ATT_ID)).intValue();
		String name = (String) obj.get(ATT_NAME);
		Double sse = (Double) obj.get(ATT_SSE);
		Double rms = (Double) obj.get(ATT_RMS);
		Double r2 = (Double) obj.get(ATT_R2);
		Double aic = (Double) obj.get(ATT_AIC);
		Double bic = (Double) obj.get(ATT_BIC);
		Integer dof = (Integer) obj.get(ATT_DOF);
		Long lQualityScore = (Long) obj.get(ATT_QUALITYSCORE);
		Integer qualityScore = (lQualityScore == null) ? null : lQualityScore.intValue();
		Boolean checked = (Boolean) obj.get(ATT_CHECKED);
		String comment = (String) obj.get(ATT_COMMENT);
		String dbuuid = (String) obj.get(ATT_DBUUID);
		
		EstModelXml modelXml = new EstModelXml(id, name, sse, rms, r2, aic, bic, dof);
		modelXml.setQualityScore(qualityScore);
		modelXml.setChecked(checked);
		modelXml.setComment(comment);
		modelXml.setDbuuid(dbuuid);
		return modelXml;
	}
}