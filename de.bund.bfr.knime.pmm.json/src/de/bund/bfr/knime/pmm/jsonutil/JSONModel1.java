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
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

public class JSONModel1 {

	JSONCatalogModel catModelCoder;
	JSONDep depCoder;
	JSONIndep indepCoder;
	JSONParamList paramsCoder;
	JSONEstModel estModelCoder;
	JSONLiteratureList mLitCoder;
	JSONLiteratureList emLitCoder;

	private JSONObject obj;
	
	public JSONModel1(JSONObject obj) {
		this.obj = obj;
	}

	@SuppressWarnings("unchecked")
	public JSONModel1(CatalogModelXml catModel, DepXml dep,
			IndepXml indep, List<ParamXml> params, EstModelXml modelXml,
			List<LiteratureItem> mLits, List<LiteratureItem> emLits,
			Integer databaseWritable, String mDBUID) {

		catModelCoder = new JSONCatalogModel(catModel);
		depCoder = new JSONDep(dep);
		indepCoder = new JSONIndep(indep);
		paramsCoder = new JSONParamList(params);
		estModelCoder = new JSONEstModel(modelXml);
		mLitCoder = new JSONLiteratureList(mLits);
		emLitCoder = new JSONLiteratureList(emLits);

		obj = new JSONObject();
		obj.put(Model1Schema.ATT_MODELCATALOG, catModelCoder.getObj());
		obj.put(Model1Schema.ATT_ESTMODEL, estModelCoder.getObj());
		obj.put(Model1Schema.ATT_DEPENDENT, depCoder.getObj());
		obj.put(Model1Schema.ATT_PARAMETER, paramsCoder.getObj());
		obj.put(Model1Schema.ATT_INDEPENDENT, indepCoder.getObj());
		obj.put(Model1Schema.ATT_MLIT, mLitCoder.getObj());
		obj.put(Model1Schema.ATT_EMLIT, emLitCoder.getObj());
		obj.put(Model1Schema.ATT_DATABASEWRITABLE, databaseWritable);
		obj.put(Model1Schema.ATT_DBUUID, mDBUID);
	}

	public JSONObject getObj() {
		return obj;
	}

	public KnimeTuple toKnimeTuple() {
		KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM1Schema());
		
		tuple.setValue(Model1Schema.ATT_MODELCATALOG, catModelCoder.getObj());
		tuple.setValue(Model1Schema.ATT_ESTMODEL, estModelCoder.getObj());
		tuple.setValue(Model1Schema.ATT_DEPENDENT, depCoder.getObj());
		tuple.setValue(Model1Schema.ATT_PARAMETER, paramsCoder.getObj());
		tuple.setValue(Model1Schema.ATT_INDEPENDENT, indepCoder.getObj());
		tuple.setValue(Model1Schema.ATT_MLIT, mLitCoder.getObj());
		tuple.setValue(Model1Schema.ATT_EMLIT, emLitCoder.getObj());
		tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, obj.get(Model1Schema.ATT_DATABASEWRITABLE));
		tuple.setValue(Model1Schema.ATT_DBUUID, obj.get(Model1Schema.ATT_DBUUID));
		
		return tuple;
	}
	public static void main(String[] args) {
		// Create model1 columns

		// Catalog model
		CatalogModelXml catModel = new CatalogModelXml(
				-396519350,
				"Salmonella spp. blah blah",
				"Value=Log10N0+(Log10Nmax-Log10N0)/(1+exp(4*Log10mumax*(0.97/Log10mumax-Time)/(Log10Nmax-Log10N0)+2))",
				1);

		// Dependent variable
		DepXml dep = new DepXml("Value", "Value",
				"Number Content (count/mass)", "ln(count/g)", "ln...");

		// Independent variable
		IndepXml indep = new IndepXml("Time", 0.0, 6.0, "Time", "h");
		indep.setOrigName("Time");
		indep.setDescription("Stunde");

		// Log10N param - Create with name and value
		ParamXml log10NParam = new ParamXml("Log10N", 0.0);
		log10NParam.setCategory("Number Content (count/mass)");
		log10NParam.setDescription("ln ...");
		log10NParam.setOrigName("Log10N");
		log10NParam.setUnit("ln(count/g)");

		// Log10N0 param - Create with name and value
		ParamXml log10N0Param = new ParamXml("Log10N0", 2.68134);
		log10N0Param.setCategory("Number Content (count/mass)");
		log10N0Param.setDescription("ln ...");
		log10N0Param.setOrigName("Log10N0");
		log10N0Param.setUnit("ln(count/g)");

		// Join all the params in a list
		List<ParamXml> params = Arrays.asList(log10NParam, log10N0Param);

		// EstModel
		EstModelXml estModel = new EstModelXml(-1317912733, null, null, null,
				null, null, null, null);

		// Florent Baty literature item
		String fbatyAuthor = "Florent Baty";
		Integer fbatyYear = 2012;
		String fbatyTitle = "Package `nlstools`";
		String fbatyAbstract = "";
		String fbatyJournal = "";
		String fbatyVolume = "";
		String fbatyIssue = "";
		Integer fbatyPage = null;
		Integer fbatyApprovalMode = null;
		String fbatyWebsite = "http://cran.r-project.org/web/packages/nlstools/";
		Integer fbatyType = null;
		String fbatyComment = "";
		Integer fbatyId = 240;

		LiteratureItem fbaty = new LiteratureItem(fbatyAuthor, fbatyYear,
				fbatyTitle, fbatyAbstract, fbatyJournal, fbatyVolume,
				fbatyIssue, fbatyPage, fbatyApprovalMode, fbatyWebsite,
				fbatyType, fbatyComment, fbatyId);

		// Jagannath literature item
		String jagAuthor = "Jagannath";
		Integer jagYear = 2005;
		String jagTitle = "Comparison of the thermal inactivation of Bacillus subtilis spores in&#xa;foods using the modified Weibull and Bigelow equations";
		String jagAbstract = "";
		String jagJournal = "Food Microbiology";
		String jagVolume = "22";
		String jagIssue = "";
		Integer jagPage = 233;
		Integer jagApprovalMode = null;
		String jagWebsite = "";
		Integer jagType = null;
		String jagComment = "";
		Integer jagId = 242;

		LiteratureItem jag = new LiteratureItem(jagAuthor, jagYear, jagTitle,
				jagAbstract, jagJournal, jagVolume, jagIssue, jagPage,
				jagApprovalMode, jagWebsite, jagType, jagComment, jagId);

		// Create list of literature items for M_Literatur
		List<LiteratureItem> mLits = Arrays.asList(fbaty, jag);

		// Create emtpy list of literature items for EM_Literatur
		List<LiteratureItem> emLits = new ArrayList<>();

		Integer databaseWritable = Model1Schema.WRITABLE;
		String mDBUID = null;

		JSONModel1 model1Encoder = new JSONModel1(
				catModel, dep, indep, params, estModel, mLits, emLits,
				databaseWritable, mDBUID);
		JSONObject obj = model1Encoder.getObj();
		System.out.println(obj);
	}
}
