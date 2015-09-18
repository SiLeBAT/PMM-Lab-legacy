package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.List;

import org.sbml.jsbml.AssignmentRule;

import de.bund.bfr.knime.pmm.annotation.ModelRuleAnnotation;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;

/**
 * Parses the rule of a primary model.
 * 
 * @author Miguel Alba
 */
public class Model1Rule extends ModelRule {

	public static Model1Rule convertCatalogModelXmlToModel1Rule(CatalogModelXml catModel, final String var,
			List<LiteratureItem> lits) {
		// Trims out the "Value=" from the formula
		int pos = catModel.getFormula().indexOf("=");
		String formula = catModel.getFormula().substring(pos + 1);
		// Removes boundary conditions
		formula = MathUtilities.getAllButBoundaryCondition(formula);

		AssignmentRule assignmentRule = convertFormulaToAssignmentRule(var, formula);
		Model1Rule rule = new Model1Rule(assignmentRule);

		String formulaName;
		if (catModel.getName() == null) {
			formulaName = "Missing formula name";
		} else {
			formulaName = catModel.getName();
		}

		rule.addAnnotation(formulaName, catModel.getModelClass(), catModel.getId(), lits);

		return rule;
	}

	public Model1Rule(AssignmentRule rule) {
		this.rule = rule;
		if (rule.isSetAnnotation()) {
			ModelRuleAnnotation ruleAnnotation = new ModelRuleAnnotation(rule.getAnnotation());
			lits = ruleAnnotation.getLits();
		}
	}

	protected String createVariable() {
		return "Value";
	}
	
	public List<LiteratureItem> getLits() {
		return lits;
	}
}
