package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.MdInfoXml;

public class JSONMdInfo {

	JSONObject obj; // JSON object
	
	// attribute keys
	public static final String ATT_ID = "ID";
	public static final String ATT_NAME = "Name";
	public static final String ATT_COMMENT = "Comment";
	public static final String ATT_QUALITYSCORE = "QualityScore";
	public static final String ATT_CHECKED = "Checked";

	public JSONMdInfo(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONMdInfo(MdInfoXml info) {
		obj = new JSONObject();

		obj.put(ATT_ID, info.getId());
		obj.put(ATT_NAME, info.getName());
		obj.put(ATT_COMMENT, info.getComment());
		obj.put(ATT_QUALITYSCORE, info.getQualityScore());
		obj.put(ATT_CHECKED, info.getChecked());
	}

	public JSONObject getObj() {
		return obj;
	}
	
	public MdInfoXml toMdInfoXml() {
		Long lid = (Long) obj.get("id");
		Integer id = (lid == null) ? null : lid.intValue();
		String name = (String) obj.get(ATT_NAME);
		String comment = (String) obj.get(ATT_COMMENT);
		Integer qualityScore = (Integer) obj.get(ATT_QUALITYSCORE);
		Boolean checked = (Boolean) obj.get(ATT_CHECKED);
		
		return new MdInfoXml(id, name, comment, qualityScore, checked);
	}
}
