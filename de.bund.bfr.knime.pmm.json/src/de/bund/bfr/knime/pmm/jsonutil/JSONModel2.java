package de.bund.bfr.knime.pmm.jsonutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

public class JSONModel2 {

	JSONCatalogModel catModelCoder;
	JSONDep depCoder;
	JSONIndep indepCoder;
	JSONParamList paramsCoder;
	JSONEstModel estModelCoder;
	JSONLiteratureList mLitCoder;
	JSONLiteratureList emLitCoder;

	private JSONObject obj;
	
	public JSONModel2(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONModel2(CatalogModelXml catModel, DepXml dep,
			IndepXml indep, List<ParamXml> params, EstModelXml estModel,
			List<LiteratureItem> mLits, List<LiteratureItem> emLits,
			Integer databaseWritable, String mDBUID, Integer globalModelID) {

		catModelCoder = new JSONCatalogModel(catModel);
		depCoder = new JSONDep(dep);
		indepCoder = new JSONIndep(indep);
		paramsCoder = new JSONParamList(params);
		estModelCoder = new JSONEstModel(estModel);
		mLitCoder = new JSONLiteratureList(mLits);
		emLitCoder = new JSONLiteratureList(emLits);

		obj = new JSONObject();
		obj.put(Model2Schema.ATT_MODELCATALOG, catModelCoder.getObj());
		obj.put(Model2Schema.ATT_ESTMODEL, estModelCoder.getObj());
		obj.put(Model2Schema.ATT_DEPENDENT, depCoder.getObj());
		obj.put(Model2Schema.ATT_PARAMETER, paramsCoder.getObj());
		obj.put(Model2Schema.ATT_INDEPENDENT, indepCoder.getObj());
		obj.put(Model2Schema.ATT_MLIT, mLitCoder.getObj());
		obj.put(Model2Schema.ATT_EMLIT, emLitCoder.getObj());
		obj.put(Model2Schema.ATT_DATABASEWRITABLE, databaseWritable);
		obj.put(Model2Schema.ATT_DBUUID, mDBUID);
		obj.put(Model2Schema.ATT_GLOBAL_MODEL_ID, globalModelID);
	}

	public JSONObject getObj() {
		return obj;
	}
	
	public KnimeTuple toKnimeTuple() {
		KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM2Schema());
		
		tuple.setValue(Model2Schema.ATT_MODELCATALOG, catModelCoder.getObj());
		tuple.setValue(Model2Schema.ATT_ESTMODEL, estModelCoder.getObj());
		tuple.setValue(Model2Schema.ATT_DEPENDENT, depCoder.getObj());
		tuple.setValue(Model2Schema.ATT_PARAMETER, paramsCoder.getObj());
		tuple.setValue(Model2Schema.ATT_INDEPENDENT, indepCoder.getObj());
		tuple.setValue(Model2Schema.ATT_MLIT, mLitCoder.getObj());
		tuple.setValue(Model2Schema.ATT_EMLIT, emLitCoder.getObj());
		tuple.setValue(Model2Schema.ATT_DATABASEWRITABLE, obj.get(Model2Schema.ATT_DATABASEWRITABLE));
		tuple.setValue(Model2Schema.ATT_DBUUID, obj.get(Model2Schema.ATT_DBUUID));
		tuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID, obj.get(Model2Schema.ATT_GLOBAL_MODEL_ID));
		
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

		// Tmax parameter
		ParamXml tmaxParam = new ParamXml("Tmax", 51.282);
		tmaxParam.setCategory("Temperature");
		tmaxParam.setDescription("degree celsius");
		tmaxParam.setOrigName("Tmax");
		tmaxParam.setUnit("ºC");

		// Tmin parameter
		ParamXml tminParam = new ParamXml("Tmin", 5.572);
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
				catModel, dep, indep, params, estModel, mLits, emLits,
				databaseWritable, dbUID, globalModelID);
		JSONObject obj = model2Encoder.getObj();
		System.out.println(obj);
	}
}
