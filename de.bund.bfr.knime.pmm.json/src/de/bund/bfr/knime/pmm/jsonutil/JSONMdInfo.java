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

import de.bund.bfr.knime.pmm.common.MdInfoXml;

/**
 * JSON object with a PmmLab MdInfoXml.
 * 
 * @author Miguel Alba
 */
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
		Object idObj = obj.get(ATT_ID);
		Integer id;
		if (idObj == null) {
			id = null;
		} else if (idObj instanceof Long) {
			id = ((Long) idObj).intValue();
		} else {
			id = (Integer) idObj;
		}

		String name = (String) obj.get(ATT_NAME);
		String comment = (String) obj.get(ATT_COMMENT);
		Integer qualityScore = (Integer) obj.get(ATT_QUALITYSCORE);
		Boolean checked = (Boolean) obj.get(ATT_CHECKED);
		
		return new MdInfoXml(id, name, comment, qualityScore, checked);
	}
}
