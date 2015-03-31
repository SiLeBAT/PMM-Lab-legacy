package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.MatrixXml;

public class JSONMatrix {

	JSONObject obj; // Json object
	
	public JSONMatrix(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONMatrix(MatrixXml matrix) {
		obj = new JSONObject();

		obj.put("id", matrix.getId());
		obj.put("name", matrix.getName());
		obj.put("detail", matrix.getDetail());
		obj.put("dbuuid", matrix.getDbuuid());
	}

	public JSONObject getObj() {
		return obj;
	}
	
	public MatrixXml toMatrixXml() {
		int id = ((Long) obj.get("id")).intValue();
		String name = (String) obj.get("name");
		String detail = (String) obj.get("detail");
		String dbuuid = (String) obj.get("dbuuid");
		
		return new MatrixXml(id, name, detail, dbuuid);
	}
}
