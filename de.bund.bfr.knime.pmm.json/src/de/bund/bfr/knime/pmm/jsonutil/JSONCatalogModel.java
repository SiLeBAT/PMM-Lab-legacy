package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;

public class JSONCatalogModel {
	JSONObject obj; // Json object

	public JSONCatalogModel(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONCatalogModel(CatalogModelXml model) {
		obj = new JSONObject();

		obj.put("id", model.getId());
		obj.put("name", model.getName());
		obj.put("formula", model.getFormula());
		obj.put("modelClass", model.getModelClass());
		obj.put("comment", model.getComment());
		obj.put("dbuuid", model.getDbuuid());
	}

	public JSONObject getObj() {
		return obj;
	}

	public CatalogModelXml toCatalogModelXml() {
		int id = (int) obj.get("id");
		String name = (String) obj.get("name");
		String formula = (String) obj.get("formula");
		int modelClass = (int) obj.get("modelClass");
		String comment = (String) obj.get("comment");
		String dbuuid = (String) obj.get("dbuuid");

		CatalogModelXml catModel = new CatalogModelXml(id, name, formula,
				modelClass, dbuuid);
		catModel.setComment(comment);
		return catModel;
	}
}
