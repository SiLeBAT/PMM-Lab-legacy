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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

public class JSONModel2 {

	JSONObject obj;
	
	public JSONModel2(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONModel2(CatalogModelXml catModel, DepXml dep,
			List<IndepXml> indeps, List<ParamXml> params, EstModelXml estModel,
			List<LiteratureItem> mLits, List<LiteratureItem> emLits,
			Integer databaseWritable, String mDBUID, Integer globalModelID) {

		obj = new JSONObject();
		obj.put(Model2Schema.ATT_MODELCATALOG, new JSONCatalogModel(catModel).getObj());
		obj.put(Model2Schema.ATT_ESTMODEL, new JSONEstModel(estModel).getObj());
		obj.put(Model2Schema.ATT_DEPENDENT, new JSONDep(dep).getObj());
		obj.put(Model2Schema.ATT_PARAMETER, new JSONParamList(params).getObj());
		obj.put(Model2Schema.ATT_INDEPENDENT, new JSONIndepList(indeps).getObj());
		obj.put(Model2Schema.ATT_MLIT, new JSONLiteratureList(mLits).getObj());
		obj.put(Model2Schema.ATT_EMLIT, new JSONLiteratureList(emLits).getObj());
		obj.put(Model2Schema.ATT_DATABASEWRITABLE, databaseWritable);
		obj.put(Model2Schema.ATT_DBUUID, mDBUID);
		obj.put(Model2Schema.ATT_GLOBAL_MODEL_ID, globalModelID);
	}

	public JSONObject getObj() {
		return obj;
	}
	
	public KnimeTuple toKnimeTuple() {
		KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM2Schema());
		
		// Set catalog model
		if (obj.containsKey(Model2Schema.ATT_MODELCATALOG)) {
			JSONObject jo = (JSONObject) obj.get(Model2Schema.ATT_MODELCATALOG);
			CatalogModelXml xml = new JSONCatalogModel(jo).toCatalogModelXml();
			tuple.setValue(Model2Schema.ATT_MODELCATALOG, new PmmXmlDoc(xml));
		}
		
		// Set estimated model
		if (obj.containsKey(Model2Schema.ATT_ESTMODEL)) {
			JSONObject jo = (JSONObject) obj.get(Model2Schema.ATT_ESTMODEL);
			EstModelXml xml = new JSONEstModel(jo).toEstModelXml();
			tuple.setValue(Model2Schema.ATT_ESTMODEL, new PmmXmlDoc(xml));
		}
		
		// Set dependent
		if (obj.containsKey(Model2Schema.ATT_DEPENDENT)) {
			JSONObject jo = (JSONObject) obj.get(Model2Schema.ATT_DEPENDENT);
			DepXml xml = new JSONDep(jo).toDepXml();
			tuple.setValue(Model2Schema.ATT_DEPENDENT, new PmmXmlDoc(xml));
		}
		
		// Set parameters
		if (obj.containsKey(Model2Schema.ATT_PARAMETER)) {
			JSONArray ja = (JSONArray) obj.get(Model2Schema.ATT_PARAMETER);
			PmmXmlDoc paramCell = new PmmXmlDoc();
			for (ParamXml xml : new JSONParamList(ja).toParamXml()) {
				paramCell.add(xml);
			}
			tuple.setValue(Model2Schema.ATT_PARAMETER, paramCell);
		}
		
		// Set independent
		if (obj.containsKey(Model2Schema.ATT_INDEPENDENT)) {
			JSONArray ja = (JSONArray) obj.get(Model2Schema.ATT_INDEPENDENT);
			PmmXmlDoc indepsCell = new PmmXmlDoc();
			for (IndepXml xml : new JSONIndepList(ja).toIndepXml()) {
				indepsCell.add(xml);
			}
			tuple.setValue(Model2Schema.ATT_INDEPENDENT, indepsCell);
		}
		
		// Set model literature
		if (obj.containsKey(Model2Schema.ATT_MLIT)) {
			JSONArray ja = (JSONArray) obj.get(Model2Schema.ATT_MLIT);
			PmmXmlDoc mlitCell = new PmmXmlDoc();
			for (LiteratureItem xml : new JSONLiteratureList(ja).toLiteratureItem()) {
				mlitCell.add(xml);
			}
			tuple.setValue(Model2Schema.ATT_MLIT, mlitCell);
		}
		
		// Set estimated model literature
		if (obj.containsKey(Model2Schema.ATT_EMLIT)) {
			JSONArray ja = (JSONArray) obj.get(Model2Schema.ATT_EMLIT);
			PmmXmlDoc mlitCell = new PmmXmlDoc();
			for (LiteratureItem xml : new JSONLiteratureList(ja).toLiteratureItem()) {
				mlitCell.add(xml);
			}
			tuple.setValue(Model2Schema.ATT_EMLIT, mlitCell);
		}
		
		// Set databasewritable flag
		if (obj.containsKey(Model2Schema.ATT_DATABASEWRITABLE)) {
			int dw = ((Long) obj.get(Model2Schema.ATT_DATABASEWRITABLE)).intValue();
			tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, dw);
		}
		
		// Set dbuuid
		if (obj.containsKey(Model2Schema.ATT_DBUUID)) {
			String dbuuid = (String) obj.get(Model2Schema.ATT_DBUUID);
			tuple.setValue(Model2Schema.ATT_DBUUID, dbuuid);
		}
		
		// Set global model id
		if (obj.containsKey(Model2Schema.ATT_GLOBAL_MODEL_ID)) {
			int gid = ((Long) obj.get(Model2Schema.ATT_GLOBAL_MODEL_ID)).intValue();
			tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, gid);
		}

		return tuple;
	}

	public static void main(String[] args) {
		// Create model2 columns

		// Catalog model
		Integer catModelID = -3272315;
		String catModelName = "";
		String catModelFormula = "Log10mumax=a*(Temperature-Tmin)^2*(1-exp(b*Temperature-Tmax)))";
		Integer catModelClass = null;

		CatalogModelXml catModel = new CatalogModelXml(catModelID,
				catModelName, catModelFormula, catModelClass);

		// Dependent
		String depName = "Log10mumax";
		String origName = "Log10mumax";
		String depCategory = "Number Content (count/mass)";
		String depUnit = "ln(count/g)";
		String depDesc = "ln ...";

		DepXml dep = new DepXml(depName, origName, depCategory, depUnit,
				depDesc);

		// Independent
		String indepName = "Temperature";
		String indepOrigName = "Temperature";
		Double indepMin = 10.0;
		Double indepMax = 45.0;
		String indepCategory = "Temperature";
		String indepUnit = "ºC";
		String indepDesc = "degree Celsius";

		IndepXml indep = new IndepXml(indepName, indepOrigName, indepMin,
				indepMax, indepCategory, indepUnit, indepDesc);
		List<IndepXml> indeps = Arrays.asList(indep);

		// Tmax parameter
		ParamXml tmaxParam = new ParamXml("Tmax", false, 51.282);
		tmaxParam.setCategory("Temperature");
		tmaxParam.setDescription("degree celsius");
		tmaxParam.setOrigName("Tmax");
		tmaxParam.setUnit("ºC");

		// Tmin parameter
		ParamXml tminParam = new ParamXml("Tmin", false, 5.572);
		tminParam.setCategory("Temperature");
		tminParam.setDescription("degree celsius");
		tminParam.setOrigName("Tmin");
		tminParam.setUnit("ºC");

		// Join all the params in a list
		List<ParamXml> params = Arrays.asList(tmaxParam, tminParam);

		// EstModel
		EstModelXml estModel = new EstModelXml(-14244545, "", null, null, null,
				null, null, 0);
		
		// Create empty lists for M_Literature and EM_Literatur
		List<LiteratureItem> mLits = new ArrayList<>();
		List<LiteratureItem> emLits = new ArrayList<>();

		Integer databaseWritable = Model2Schema.WRITABLE;
		String dbUID = "";
		Integer globalModelID = -111090039;
		
		JSONModel2 model2Encoder = new JSONModel2(
				catModel, dep, indeps, params, estModel, mLits, emLits,
				databaseWritable, dbUID, globalModelID);
		JSONObject obj = model2Encoder.getObj();
		System.out.println(obj);
	}
}
