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

import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.IndepXml;

/**
 * JSON object with a list of PmmLab IndepXml.
 * 
 * @author Miguel Alba
 */
public class JSONIndepList {

	JSONArray obj;
	
	public JSONIndepList(JSONArray obj) {
		this.obj = obj;
	}
	
	@SuppressWarnings("unchecked")
	public JSONIndepList(List<IndepXml> indeps) {
		obj = new JSONArray();
		
		for (IndepXml indep : indeps) {
			obj.add(new JSONIndep(indep).getObj());
		}
	}
	
	public JSONArray getObj() {
		return obj;
	}
	
	public List<IndepXml> toIndepXml() {
		List<IndepXml> indeps = new LinkedList<>();
		
		for (int i =  0; i < obj.size(); i++) {
			JSONObject jo = (JSONObject) obj.get(i);
			indeps.add(new JSONIndep(jo).toIndepXml());
		}
		
		return indeps;
	}
}
