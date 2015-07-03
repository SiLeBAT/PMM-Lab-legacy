/**
 * Base class for model rules.
 * 
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.JSBML;
import org.sbml.jsbml.text.parser.ParseException;

import de.bund.bfr.knime.pmm.common.CatalogModelXml;

public abstract class ModelRule {
	protected final static int LEVEL = 3;
	protected final static int VERSION = 1;

	protected AssignmentRule rule;

	public AssignmentRule getRule() {
		return rule;
	}

	/**
	 * Add annotation to the rule.
	 */
	protected void addAnnotation(String name, Integer type, int id) {
		String subject;
		if (type == null) {
			subject = "unknown";
		} else {
			subject = Util.MODELCLASS_STRS.get(type);
		}
		ModelRuleAnnotation annot = new ModelRuleAnnotation(name, subject, id);
		rule.getAnnotation().setNonRDFAnnotation(annot.getNode());
	}

	/** Get CatalogModelXml from ModelRule */
	public CatalogModelXml toCatModel() {
		ModelRuleAnnotation annot = new ModelRuleAnnotation(rule.getAnnotation().getNonRDFannotation());
		String formulaName = annot.getName();
		int type = Util.MODELCLASS_NUMS.get(annot.getSubject());
		int pmmlabID = annot.getID();
		
		String formula = String.format("%s=", createVariable());
		ASTNode modelMath = rule.getMath();
		// Replace MathML's log with Pmm Lab's ln
		String formulaStr = modelMath.toFormula();
		formulaStr = formulaStr.replace("time", "Time");
        formulaStr = formulaStr.replace("log(", "ln(");
		formula += formulaStr;
	
		CatalogModelXml catModel = new CatalogModelXml(pmmlabID, formulaName, formula, type);
		return catModel;
	}

	protected abstract String createVariable();

	public static AssignmentRule convertFormulaToAssignmentRule(String var,
			String formula) {
		AssignmentRule assignmentRule = null;
		// csymbol time needs a lower case t
		formula = formula.replace("Time", "time");
		
		try {
			ASTNode math = JSBML.parseFormula(formula);
			assignmentRule = new AssignmentRule(math, LEVEL, VERSION);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assignmentRule.setVariable(var);

		return assignmentRule;
	}
}