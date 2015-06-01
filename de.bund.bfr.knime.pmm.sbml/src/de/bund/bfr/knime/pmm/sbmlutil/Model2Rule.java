package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.AssignmentRule;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;

/**
 * Parses the rule of a secondary model
 * 
 * @author Miguel Alba
 */
public class Model2Rule extends ModelRule {

	public static Model2Rule convertCatalogModelXmlToModel2Rule(
			CatalogModelXml catModel) {
		// Parse variable from the rule
		final int pos = catModel.getFormula().indexOf("=");
		final String var = catModel.getFormula().substring(0, pos);
		// The remaining chunk contains the actual formula
		String formula = catModel.getFormula().substring(pos + 1);
		// Removes boundary conditions
		formula = MathUtilities.getAllButBoundaryCondition(formula);

		AssignmentRule assignmentRule = convertFormulaToAssignmentRule(var,
				formula);
		Model2Rule rule = new Model2Rule(assignmentRule);
		
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

	public Model2Rule(AssignmentRule rule) {
		this.rule = rule;
	}

	protected String createVariable() {
		return rule.getVariable();
	}
}
