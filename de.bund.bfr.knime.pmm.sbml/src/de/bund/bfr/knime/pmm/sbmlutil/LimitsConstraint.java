package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.List;
import java.util.Locale;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Constraint;
import org.sbml.jsbml.text.parser.ParseException;

/**
 * A constraint with the limit values of a parameter.
 * 
 * @author malba
 */
public class LimitsConstraint {
	private static final int LEVEL = 3;
	private static final int VERSION = 1;
	Constraint constraint;

	/**
	 * Initialize constraint. var can't be null. Either min or max can be null
	 * but not both.
	 * 
	 * @param var
	 *            : Variable name.
	 * @param min
	 *            : Minimum value.
	 * @param max
	 *            : Maximum value.
	 */
	public LimitsConstraint(String var, Double min, Double max) {
		String formula;

		if (min != null && max != null) {
			formula = String.format(Locale.ENGLISH, "(%s >= %f) && (%s <= %f)",
					var, min, var, max);
		} else if (min != null) {
			formula = String.format(Locale.ENGLISH, "(%s >= %f)", var, min);
		} else if (max != null) {
			formula = String.format(Locale.ENGLISH, "(%s <= %f)", var, min);
		} else {
			formula = "";
		}

		if (!formula.isEmpty()) {
			ASTNode math = null;
			try {
				math = ASTNode.parseFormula(formula);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			constraint = new Constraint(math, LEVEL, VERSION);
		}
	}

	public LimitsConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	public Constraint getConstraint() {
		return constraint;
	}

	public Limits getLimits() {
		final ASTNode math = constraint.getMath();
		List<ASTNode> nodes = math.getListOfNodes();
		String var;
		Double min = null;
		Double max = null;

		// Constraint with a single condition
		if (math.toString().indexOf("&&") == -1) {
			var = nodes.get(0).getName();
			double val = Double.parseDouble(nodes.get(1).toString());

			ASTNode.Type type = constraint.getMath().getType();
			if (type == ASTNode.Type.RELATIONAL_LEQ
					|| type == ASTNode.Type.RELATIONAL_LT) {
				max = val;
			}

			else {
				min = val;
			}
		}

		// Constraint with two conditions
		else {
			ASTNode leftNode = nodes.get(0);
			List<ASTNode> leftNodes = leftNode.getListOfNodes();
			var = leftNodes.get(0).getName();
			min = Double.parseDouble(leftNodes.get(1).toString());

			ASTNode rightNode = nodes.get(1);
			List<ASTNode> rightNodes = rightNode.getListOfNodes();
			max = Double.parseDouble(rightNodes.get(1).toString());

		}
		Limits limits = new Limits(var, min, max);
		return limits;
	}
}
