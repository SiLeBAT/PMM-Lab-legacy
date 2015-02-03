package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.text.parser.ParseException;


/**
 * Parse the rule of a primary model
 * @author malba
 */
public class Model1Rule extends ModelRule {

	@Override
	public void parse(String formula) {
		// Get the right hand of the formula (trim value and =)
		int endIndex = formula.indexOf("=") + 1;
		String ruleFormula = formula.substring(endIndex);

		try {
			ASTNode math = parseMath(ruleFormula);
			rule = new AssignmentRule(math, LEVEL, VERSION);
			rule.setVariable("Value");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
