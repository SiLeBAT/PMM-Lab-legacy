package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.AssignmentRule;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;

/**
 * Parse the rule of a primary model
 * 
 * @author Miguel Alba
 */
public class Model1Rule extends ModelRule {

	public static Model1Rule convertCatalogModelXmlToModel1Rule(
			CatalogModelXml catModel, final String var) {
		// Trims out the "Value=" from the formula
		int pos = catModel.getFormula().indexOf("=");
		String formula = catModel.getFormula().substring(pos + 1);
		// Remove boundary conditions
		pos = formula.lastIndexOf("*((((");
		if (pos != -1) {
			formula = formula.substring(0, pos);
		}

		AssignmentRule assignmentRule = convertFormulaToAssignmentRule(var,
				formula);
		Model1Rule rule = new Model1Rule(assignmentRule);
		
		String formulaName;
		if (catModel.getName() == null) {
			formulaName = "Missing formula name";
		} else {
			formulaName = catModel.getName();
		}

		rule.addAnnotation(formulaName, catModel.getModelClass(),
				catModel.getId());

		return rule;
	}

	public Model1Rule(AssignmentRule rule) {
		this.rule = rule;
	}

	protected String createVariable() {
		return "Value";
	}
}
