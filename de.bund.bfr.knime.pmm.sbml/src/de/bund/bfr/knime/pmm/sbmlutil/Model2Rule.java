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

	public static Model2Rule convertCatalogModelXmlToModel2Rule(CatalogModelXml catModel) {
		int pos = catModel.getFormula().indexOf("=");
		final String variable = catModel.getFormula().substring(0, pos);
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
			modelClass = SBMLUtil.INT_TO_CLASS.get(catModel.getModelClass());
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
		int type = SBMLUtil.CLASS_TO_INT.get("unknown"); // model class
		XMLNode nonRDFAnnot = rule.getAnnotation().getNonRDFannotation();
		XMLNode metadata = null;
		for (XMLNode node : nonRDFAnnot.getChildElements("", "")) {
			if (node.getName().equals("metadata")) {
				metadata = node;
				break;
			}
		}

		for (XMLNode node : metadata.getChildElements("", "")) {
			if (node.getName().equals("formulaName")) {
				formulaName = node.getChildAt(0).getCharacters();
			} else if (node.getName().equals("subject")) {
				String classString = node.getChildAt(0).getCharacters();
				type = SBMLUtil.CLASS_TO_INT.get(classString);
			}
		}

		CatalogModelXml catModel = new CatalogModelXml(null, formulaName,
				formula, type);
		return catModel;
	}
}
