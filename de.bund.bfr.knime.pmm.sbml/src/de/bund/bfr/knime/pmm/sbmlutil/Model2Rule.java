package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.text.parser.ParseException;

/**
 * Parse the rule of a secondary model
 * @author malba
 */
public class Model2Rule extends ModelRule {

	@Override
	public void parse(String formula) {
		int separatorPos = formula.indexOf("=");
		String ruleVariable = formula.substring(0, separatorPos);
		String ruleFormula = formula.substring(separatorPos + 1);

		try {
			ASTNode math = parseMath(ruleFormula);
			rule = new AssignmentRule(math, LEVEL, VERSION);
			rule.setVariable(ruleVariable);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
