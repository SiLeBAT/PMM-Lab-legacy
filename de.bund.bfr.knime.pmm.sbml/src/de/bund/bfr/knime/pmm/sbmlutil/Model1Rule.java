package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.text.parser.ParseException;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;

/**
 * Parse the rule of a primary model
 * 
 * @author malba
 */
public class Model1Rule extends ModelRule {

	public static Model1Rule convertCatalogModelXmlToModel1Rule(CatalogModelXml catModel) {
		int pos = catModel.getFormula().indexOf("=") + 1;
		final String formula = catModel.getFormula().substring(pos);

		AssignmentRule assignmentRule = null;
		try {
			ASTNode math = parseMath(formula);
			assignmentRule = new AssignmentRule(math, LEVEL, VERSION);
			assignmentRule.setVariable("Value");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Model1Rule rule = new Model1Rule(assignmentRule);

		String modelClass;
		if (catModel.getModelClass() == null)
			modelClass = "unknown";
		else
			modelClass = SBMLUtil.INT_TO_CLASS.get(catModel.getModelClass());
		rule.addAnnotation(catModel.getName(), modelClass);
		
		return rule;
		
	}
	
	public Model1Rule(AssignmentRule rule) {
		this.rule = rule;
	}

	public CatalogModelXml toCatModel() {
		String formula = "Value=" + rule.getMath().toFormula();

		String formulaName = null;
		int type = SBMLUtil.CLASS_TO_INT.get("unknown"); // model class
		XMLNode nonRDFAnnot = rule.getAnnotation().getNonRDFannotation();
		XMLNode metadata = nonRDFAnnot.getChildElement("metadata", "");

		// the formula name node is mandatory
		XMLNode formulaNameNode = metadata.getChildElement("formulaName", "");
		formulaName = formulaNameNode.getChildAt(0).getCharacters();
		
		XMLNode subjectNode = metadata.getChildElement("subject", "");
		if (subjectNode != null) {
			String classString = subjectNode.getChildAt(0).getCharacters();
			type = SBMLUtil.CLASS_TO_INT.get(classString);
		}

		CatalogModelXml catModel = new CatalogModelXml(null, formulaName,
				formula, type);
		return catModel;
	}
}
