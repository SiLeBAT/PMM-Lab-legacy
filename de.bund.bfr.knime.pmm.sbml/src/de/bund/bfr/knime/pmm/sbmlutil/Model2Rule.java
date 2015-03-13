package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.text.parser.ParseException;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;

/**
 * Parse the rule of a secondary model
 * 
 * @author malba
 */
public class Model2Rule extends ModelRule {

	public static Model2Rule convertCatalogModelXmlToModel2Rule(
			CatalogModelXml catModel) {
		int pos = catModel.getFormula().indexOf("=");
		// Parse variable from the rule
		final String variable = catModel.getFormula().substring(0, pos);
		// The remaining chunk contains the actual formula
		final String formula = catModel.getFormula().substring(pos + 1);

		AssignmentRule assignmentRule = null;
		try {
			ASTNode math = parseMath(formula);
			assignmentRule = new AssignmentRule(math, LEVEL, VERSION);
			assignmentRule.setVariable(variable);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Model2Rule rule = new Model2Rule(assignmentRule);

		String modelClass;
		if (catModel.getModelClass() == null)
			modelClass = "unknown";
		else
			modelClass = Util.MODELCLASS_STRS.get(catModel.getModelClass());
		rule.addAnnotation(catModel.getName(), modelClass);

		return rule;

	}

	public Model2Rule(AssignmentRule rule) {
		this.rule = rule;
	}

	public CatalogModelXml toCatModel() {
		String formula = String.format("%s=%s", rule.getVariable(), rule
				.getMath().toFormula());

		String formulaName = null;
		int type = Util.MODELCLASS_NUMS.get("unknown"); // model class
		XMLNode nonRDFAnnot = rule.getAnnotation().getNonRDFannotation();
		XMLNode metadata = nonRDFAnnot.getChildElement("metadata", "");

		// the formula name node is mandatory
		XMLNode formulaNameNode = metadata.getChildElement("formulaName", "");
		formulaName = formulaNameNode.getChildAt(0).getCharacters();

		XMLNode subjectNode = metadata.getChildElement("subject", "");
		if (subjectNode != null) {
			String classString = subjectNode.getChildAt(0).getCharacters();
			type = Util.MODELCLASS_NUMS.get(classString);
		}

		CatalogModelXml catModel = new CatalogModelXml(null, formulaName,
				formula, type);
		return catModel;
	}
}
