/**
 * Code and decode a CatalogModelXml into/from JSON
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;

public class JSONCatalogModel {
	JSONObject obj; // Json object
	
	// attribute keys
	private static final String ATT_ID = "id";
	private static final String ATT_NAME = "name";
	private static final String ATT_FORMULA = "formula";
	private static final String ATT_MODEL_CLASS = "modelClass";
	private static final String ATT_COMMENT = "comment";
	private static final String ATT_DBUUID = "dbuuid";

	public JSONCatalogModel(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONCatalogModel(CatalogModelXml model) {
		obj = new JSONObject();

		obj.put(ATT_ID, model.getId());
		obj.put(ATT_NAME, model.getName());
		obj.put(ATT_FORMULA, model.getFormula());
		obj.put(ATT_MODEL_CLASS, model.getModelClass());
		obj.put(ATT_COMMENT, model.getComment());
		obj.put(ATT_DBUUID, model.getDbuuid());
	}

	public JSONObject getObj() {
		return obj;
	}

	public CatalogModelXml toCatalogModelXml() {
		int id = (int) obj.get(ATT_ID);
		String name = (String) obj.get(ATT_NAME);
		String formula = (String) obj.get(ATT_FORMULA);
		int modelClass = (int) obj.get(ATT_MODEL_CLASS);
		String comment = (String) obj.get(ATT_COMMENT);
		String dbuuid = (String) obj.get(ATT_DBUUID);

		CatalogModelXml catModel = new CatalogModelXml(id, name, formula,
				modelClass, dbuuid);
		catModel.setComment(comment);
		return catModel;
	}
}
