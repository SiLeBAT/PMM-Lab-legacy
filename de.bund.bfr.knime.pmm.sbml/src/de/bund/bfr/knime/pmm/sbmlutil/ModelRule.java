/**
 * Base class for model rules.
 * 
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.AssignmentRule;
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
		ModelRuleAnnotation annot = new ModelRuleAnnotation(rule
				.getAnnotation().getNonRDFannotation());
		String formulaName = annot.getName();
		int type = Util.MODELCLASS_NUMS.get(annot.getSubject());
		int pmmlabID = annot.getID();

		String formula = String.format("%s=", createVariable());
		ASTNode modelMath = rule.getMath();

		// Parse model
		// if piecewise-defined function
		if (modelMath.isPiecewise()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < modelMath.getChildCount(); i += 2) {
				ASTNode val = modelMath.getChild(i); // Value node
				ASTNode cond = modelMath.getChild(i + 1); // Condition node

				String valString;
				// Value node is a literal value. No need to surround it with
				// parentheses
				if (val.getChildCount() == 0) {
					valString = val.toFormula();
				}
				// Value node is an expression that must be surrounded with
				// parentheses
				else {
					valString = String.format("(%s)", val.toFormula());
				}

				String condString;
				// Multiple conditions must be concatenated with an asterisk
				if (cond.getType() == ASTNode.Type.TIMES) {
					ASTNode lchild = cond.getLeftChild();
					ASTNode rchild = cond.getRightChild();
					condString = String.format("(%s)*(%s)", lchild.toFormula(),
							rchild.toFormula());
				}

				// If only one condition, this one needs to be surrounded with
				// parentheses
				else {
					condString = String.format("(%s)", cond.toFormula());
				}

				if (i > 0) {
					sb.append("+");
				}
				String piece = valString + "*" + condString;
				sb.append(piece);
			}
			formula += sb.toString();
		} else {
			// Replace MathML's log with Pmm Lab's ln
			String formulaStr;
			if (modelMath.toFormula().indexOf("^") != -1) {
				formulaStr = postorder(modelMath);
			} else {
				formulaStr = modelMath.toFormula();
			}
			formulaStr = formulaStr.replaceAll("log\\(", "ln(");
			formula += formulaStr;
		}

		CatalogModelXml catModel = new CatalogModelXml(pmmlabID, formulaName,
				formula, type);
		return catModel;
	}

	private String postorder(ASTNode node) {
		String lStr = "", rStr = "";
		if (!node.isLeaf()) {
			lStr = postorder(node.getLeftChild());
			rStr = postorder(node.getRightChild());

			switch (node.getType()) {
			case PLUS:
				return "(" + lStr + "+" + rStr + ")";
			case MINUS:
				return "(" + lStr + "-" + rStr + ")";
			case TIMES:
				return "(" + lStr + "*" + rStr + ")";
			case DIVIDE:
				return "(" + lStr + "/" + rStr + ")";
			case FUNCTION_POWER:
				return "(" + lStr + "^" + rStr + ")";
			case FUNCTION_EXP:
				return "exp" + lStr;
			default:
				return "";
			}
		} else {
			return node.toFormula();
		}

	}

	protected abstract String createVariable();

	public static AssignmentRule convertFormulaToAssignmentRule(String var,
			String formula) {
		AssignmentRule assignmentRule = null;
		if ((formula.indexOf('<') != -1) || (formula.indexOf("<=") != -1)
				|| (formula.indexOf(">") != -1)
				|| (formula.indexOf(">=") != -1)
				|| (formula.indexOf("==") != -1)
				|| (formula.indexOf("!=") != -1)) {

			try {
				ASTNode[] nodes = PiecewiseFormula
						.parsePiecewiseFormula(formula);
				ASTNode math = ASTNode.piecewise(new ASTNode(), nodes);
				assignmentRule = new AssignmentRule(math, LEVEL, VERSION);
				assignmentRule.setVariable(var);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			try {
				// TODO: ASTNode.parseFormula skips parentheses with powers
				// (pH^2) -> pH^2
				ASTNode math = ASTNode.parseFormula(formula);
				assignmentRule = new AssignmentRule(math, LEVEL, VERSION);
				assignmentRule.setVariable(var);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return assignmentRule;
	}
}