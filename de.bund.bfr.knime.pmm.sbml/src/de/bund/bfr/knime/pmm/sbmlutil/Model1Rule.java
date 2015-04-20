package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.AssignmentRule;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;

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

		if (catModel.getModelClass() == null) {
			rule.addAnnotation(catModel.getName(), "unknown");
		} else {
			rule.addAnnotation(catModel.getName(),
					Util.MODELCLASS_STRS.get(catModel.getModelClass()));
		}

		return rule;
	}

	public Model1Rule(AssignmentRule rule) {
		this.rule = rule;
	}

	protected String createVariable() {
		return "Value";
	}
}
