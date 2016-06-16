package de.bund.bfr.knime.pmm.common.writer;

import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.hsh.bfr.db.DBKernel;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.PMFUtil;
import de.bund.bfr.pmf.sbml.Correlation;
import de.bund.bfr.pmf.sbml.ModelRule;
import de.bund.bfr.pmf.sbml.ModelVariable;
import de.bund.bfr.pmf.sbml.PMFCoefficient;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.bund.bfr.pmf.sbml.PMFUnit;
import de.bund.bfr.pmf.sbml.PMFUnitDefinition;
import de.bund.bfr.pmf.sbml.Reference;
import de.bund.bfr.pmf.sbml.ReferenceType;
import de.bund.bfr.pmf.sbml.SBMLFactory;
import de.bund.bfr.pmf.sbml.Uncertainties;

public class Util {

	private Util() {
	}

	public static PMFCoefficient paramXml2Coefficient(ParamXml paramXml) {
		String name = paramXml.getName();
		double value = (paramXml.getValue() == null) ? 0.0 : paramXml.getValue();
		String unit = (paramXml.getUnit() == null) ? "dimensionless" : PMFUtil.createId(paramXml.getUnit());
		Double P = paramXml.getP();
		Double error = paramXml.getError();
		Double t = paramXml.getT();
		Boolean isStart = paramXml.isStartParam();

		Map<String, Double> correlationMap = paramXml.getAllCorrelations();
		Correlation[] correlations = new Correlation[correlationMap.size()];
		int i = 0;
		for (Map.Entry<String, Double> entry : correlationMap.entrySet()) {
			if (entry.getValue() == null) {
				correlations[i] = new Correlation(entry.getKey());
			} else {
				correlations[i] = new Correlation(entry.getKey(), entry.getValue());
			}
			i++;
		}

		String desc = paramXml.getDescription();

		PMFCoefficient coefficient = SBMLFactory.createPMFCoefficient(name, value, unit, P, error, t, correlations,
				desc, isStart);
		return coefficient;
	}

	public static Uncertainties estModel2Uncertainties(EstModelXml estModel) {
		Integer id = estModel.getId();
		String modelName = estModel.getName();
		String comment = estModel.getComment();
		Double r2 = estModel.getR2();
		Double rms = estModel.getRms();
		Double sse = estModel.getSse();
		Double aic = estModel.getAic();
		Double bic = estModel.getBic();
		Integer dof = estModel.getDof();
		Uncertainties uncertainties = SBMLFactory.createUncertainties(id, modelName, comment, r2, rms, sse, aic, bic,
				dof);
		return uncertainties;
	}

	public static Reference literatureItem2Reference(LiteratureItem lit) {
		String author = lit.getAuthor();
		Integer year = lit.getYear();
		String title = lit.getTitle();
		String abstractText = lit.getAbstractText();
		String journal = lit.getJournal();
		String volume = lit.getVolume();
		String issue = lit.getIssue();
		Integer page = lit.getPage();
		Integer approvalMode = lit.getApprovalMode();
		String website = lit.getWebsite();
		ReferenceType type = (lit.getType() == null) ? null : ReferenceType.fromValue(lit.getType());
		String comment = lit.getComment();

		Reference ref = SBMLFactory.createReference(author, year, title, abstractText, journal, volume, issue, page,
				approvalMode, website, type, comment);
		return ref;
	}

	public static PMFCompartment matrixXml2Compartment(MatrixXml matrixXml, PmmXmlDoc miscDoc) {
		ModelVariable[] modelVariables = new ModelVariable[miscDoc.size()];
		for (int i = 0; i < miscDoc.size(); i++) {
			MiscXml miscXml = (MiscXml) miscDoc.get(i);
			modelVariables[i] = new ModelVariable(miscXml.getName(), miscXml.getValue());
		}
		String compartmentDetail = matrixXml.getDetail();

		String compartmentId;
		String compartmentName;

		if (matrixXml.getName() == null) {
			compartmentId = "MISSING_COMPARTMENT";
			compartmentName = "MISSING_COMPARTMENT";
		} else {
			compartmentId = PMFUtil.createId(matrixXml.getName());
			compartmentName = matrixXml.getName();
		}

		// Gets PMF code from DB
		String[] colNames = { "CodeSystem", "Basis" };
		String[] colVals = { "PMF", matrixXml.getId().toString() };
		String pmfCode = (String) DBKernel.getValue(null, "Codes_Matrices", colNames, colVals, "Codes");
		PMFCompartment compartment = SBMLFactory.createPMFCompartment(compartmentId, compartmentName, pmfCode,
				compartmentDetail, modelVariables);
		return compartment;
	}

	public static PMFSpecies createSpecies(AgentXml agentXml, String unit, String compartmentId) {
		// Creates species and adds it to the model
		String speciesId = PMFUtil.createId("species" + agentXml.getId());
		String speciesUnit = (unit == null) ? "dimensionless" : PMFUtil.createId(unit);
		String speciesName = (String) DBKernel.getValue("Agenzien", "ID", agentXml.getId().toString(), "Agensname");
		String casNumber;
		if (speciesName == null) {
			speciesName = agentXml.getName();
			casNumber = null;
		} else {
			casNumber = (String) DBKernel.getValue("Agenzien", "ID", agentXml.getId().toString(), "Cas_Nummer");
		}
		String speciesDetail = agentXml.getDetail();

		PMFSpecies species = SBMLFactory.createPMFSpecies(compartmentId, speciesId, speciesName, speciesUnit, casNumber,
				speciesDetail, null);
		return species;
	}

	public static PMFUnitDefinition createUnitFromDB(String unit) throws XMLStreamException {
		if (!DBUnits.getDBUnits().containsKey(unit)) {
			return null;
		}

		String id = PMFUtil.createId(unit);
		String name = unit;
		String transformation = null;
		PMFUnit[] units = null;

		UnitsFromDB dbUnit = DBUnits.getDBUnits().get(unit);

		String mathml = dbUnit.getMathML_string();
		if (mathml != null && !mathml.isEmpty()) {

			// Parse transformation name if present
			int transformationIndex = mathml.indexOf("<transformation");
			if (transformationIndex == -1) {
				transformation = null;
			} else {
				int endTransformationIndex = mathml.indexOf("<", transformationIndex + 1);
				int firstQuotePos = mathml.indexOf("\"", transformationIndex);
				int secQuotePos = mathml.indexOf("\"", firstQuotePos + 1);
				transformation = mathml.substring(firstQuotePos + 1, secQuotePos);

				// Removes the transformation annotation (this
				// annotation has a name which is not prefixed and then
				// cannot be parsed properly)
				mathml = mathml.substring(0, transformationIndex) + mathml.substring(endTransformationIndex);
			}

			// Remove namespace (all the DB units have this namespace
			// which is not necessary)
			mathml = mathml
					.replaceAll("xmlns=\"http://sourceforge.net/projects/microbialmodelingexchange/files/Units\"", "");

			String preXml = "<?xml version='1.0' encoding='UTF-8' standalone='no'?>"
					+ "<sbml xmlns=\"http://www.sbml.org/sbml/level3/version1/core\"" + " level=\"3\" version=\"1\""
					+ " xmlns:pmf=\"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML\""
					+ "><model id=\"ID\">" + "<listOfUnitDefinitions>";

			String postXml = "</listOfUnitDefinitions></model></sbml>";
			String totalXml = preXml + mathml + postXml;

			// Create a new UnitDefinition from XML
			UnitDefinition xmlUnitDef = SBMLReader.read(totalXml).getModel().getUnitDefinition(0);

			ListOf<Unit> listOfUnits = xmlUnitDef.getListOfUnits();
			int numOfUnits = listOfUnits.size();
			units = new PMFUnit[numOfUnits];
			for (int i = 0; i < numOfUnits; i++) {
				Unit sbmlUnit = listOfUnits.get(i);
				Unit.Kind kind = sbmlUnit.getKind();
				double multiplier = sbmlUnit.isSetMultiplier() ? sbmlUnit.getMultiplier() : PMFUnit.DEFAULT_MULTIPLIER;
				int scale = sbmlUnit.isSetScale() ? sbmlUnit.getScale() : PMFUnit.DEFAULT_SCALE;
				double exponent = sbmlUnit.isSetExponent() ? sbmlUnit.getExponent() : PMFUnit.DEFAULT_EXPONENT;

				units[i] = new PMFUnit(multiplier, scale, kind, exponent);
			}
		}

		PMFUnitDefinition unitDefinition = new PMFUnitDefinition(id, name, transformation, units);
		return unitDefinition;
	}

	public static ModelRule createM1Rule(CatalogModelXml catModel, String variable, Reference[] references) {

		// Trims out the "Value=" from the formula
		int pos = catModel.getFormula().indexOf("=");
		String formula = catModel.getFormula().substring(pos + 1);

		// Removes boundary conditions
		formula = MathUtilities.getAllButBoundaryCondition(formula);
		// csymbol time needs a lower case t
		formula = formula.replace("Time", "time");

		ModelClass modelClass;
		if (catModel.getModelClass() == null) {
			modelClass = ModelClass.UNKNOWN;
		} else {
			modelClass = ModelClass.fromValue(catModel.getModelClass());
		}

		String formulaName;
		if (catModel.getName() == null) {
			formulaName = "Missing formula name";
		} else {
			formulaName = catModel.getName();
		}

		int catModelId = catModel.getId();

		ModelRule rule = new ModelRule(variable, formula, formulaName, modelClass, catModelId, references);
		return rule;
	}

	public static ModelRule createM2Rule(CatalogModelXml catModel, Reference[] references) {
		// Parses variable from the formula
		int pos = catModel.getFormula().indexOf("=");
		String variable = catModel.getFormula().substring(0, pos);

		// The remaining chunk contains the actual formula
		String formula = catModel.getFormula().substring(pos + 1);

		// Removes boundary conditions
		formula = MathUtilities.getAllButBoundaryCondition(formula);

		ModelClass modelClass;
		if (catModel.getModelClass() == null) {
			modelClass = ModelClass.UNKNOWN;
		} else {
			modelClass = ModelClass.fromValue(catModel.getModelClass());
		}

		String formulaName;
		if (catModel.getName() == null) {
			formulaName = "Missing formula name";
		} else {
			formulaName = catModel.getName();
		}

		int catModelId = catModel.getId();

		ModelRule rule = new ModelRule(variable, formula, formulaName, modelClass, catModelId, references);
		return rule;
	}
}