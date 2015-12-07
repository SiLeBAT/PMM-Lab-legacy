/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.jsonutil;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.EstModelXml;

/**
 * JSON object with a PmmLab EstModelXml.
 * 
 * @author Miguel Alba
 */
public class JSONEstModel {
	JSONObject obj; // Json object

	// attribute keys
	static final String ATT_ID = "id";
	static final String ATT_NAME = "name";
	static final String ATT_SSE = "sse";
	static final String ATT_RMS = "rms";
	static final String ATT_R2 = "r2";
	static final String ATT_AIC = "aic";
	static final String ATT_BIC = "bic";
	static final String ATT_DOF = "dof";
	static final String ATT_QUALITYSCORE = "qualityScore";
	static final String ATT_CHECKED = "checked";
	static final String ATT_COMMENT = "comment";
	static final String ATT_DBUUID = "dbuuid";

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
		Object idObj = obj.get(ATT_ID);
		int id;
		if (idObj instanceof Long) {
			id = ((Long) idObj).intValue();
		} else {
			id = (Integer) idObj;
		}
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