/**
 * Base class for model rules.
 * 
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.text.parser.ParseException;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.annotation.ModelClassNode;
import de.bund.bfr.knime.pmm.annotation.ModelNameNode;
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
	 * 
	 * @param name
	 *            : Model name. Mandatory.
	 * @param type
	 *            : Model class.
	 */
	protected void addAnnotation(String name, String type) {
		// pmf container
		XMLTriple pmfTriple = new XMLTriple("metadata", null, "pmf");
		XMLNode pmfNode = new XMLNode(pmfTriple, null, null);

		// Add model name to pmfNode
		ModelNameNode nameNode = new ModelNameNode(name);
		pmfNode.addChild(nameNode.getNode());

		// Add model class to pmfNode
		ModelClassNode typeNode = new ModelClassNode(type);
		pmfNode.addChild(typeNode.getNode());

		// add non rdf annotation
		Annotation annot = new Annotation();
		annot.setNonRDFAnnotation(pmfNode);

		rule.setAnnotation(annot);
	}

	/** Get CatalogModelXml from ModelRule */
	public CatalogModelXml toCatModel() {
		// Get metadata annotation
		XMLNode nonRDFAnnot = rule.getAnnotation().getNonRDFannotation();

		XMLNode metadata = nonRDFAnnot.getChildElement("metadata", "");

		// Get formula name (which is mandatory)
		XMLNode formulaNameNode = metadata.getChildElement("formulaName", "");
		String formulaName = formulaNameNode.getChildAt(0).getCharacters();

		// Get type. If missing it will be assigned UNKNOWN
		XMLNode subjectNode = metadata.getChildElement("subject", "");
		int type;
		if (subjectNode == null) {
			type = Util.MODELCLASS_NUMS.get("unknown");
		} else {
			String classString = subjectNode.getChildAt(0).getCharacters();
			type = Util.MODELCLASS_NUMS.get(classString);
		}

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
					condString = String.format("(%s)*(%s)", lchild.toFormula(), rchild.toFormula());
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
			formula += modelMath.toFormula().replaceAll("log\\(", "ln(");
		}

		CatalogModelXml catModel = new CatalogModelXml(null, formulaName,
				formula, type);
		return catModel;
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