package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.MdInfoXml;

public class JSONMdInfo {

	JSONObject obj; // JSON object

	public JSONMdInfo(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONMdInfo(MdInfoXml info) {
		obj = new JSONObject();

		obj.put("id", info.getId());
		obj.put("name", info.getName());
		obj.put("comment", info.getComment());
		obj.put("qualityScore", info.getQualityScore());
		obj.put("checked", info.getChecked());
	}

	public JSONObject getObj() {
		return obj;
	}
	
	public MdInfoXml toMdInfoXml() {
		Long lid = (Long) obj.get("id");
		Integer id;
		if (lid == null) {
			id = null;
		} else {
			id = lid.intValue();
		}
		String name = (String) obj.get("name");
		String comment = (String) obj.get("comment");
		Integer qualityScore = (Integer) obj.get("qualityScore");
		Boolean checked = (Boolean) obj.get("checked");
		
		return new MdInfoXml(id, name, comment, qualityScore, checked);
	}
}
