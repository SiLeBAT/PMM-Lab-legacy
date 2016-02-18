/**************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.pmf.sbml;

import java.util.List;
import java.util.Locale;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.Constraint;
import org.sbml.jsbml.text.parser.ParseException;

/**
 * A constraint with the limit values of a parameter.
 * 
 * @author Miguel Alba
 */
public class LimitsConstraint {

  static final int LEVEL = 3;
  static final int VERSION = 1;
  Constraint constraint;

  /**
   * Initializes constraint. var can't be null. Either min or max can be null but not both.
   * 
   * @param var Variable name.
   * @param min Minimum value.
   * @param max Maximum value.
   */
  public LimitsConstraint(final String var, final Double min, final Double max) {
    final String formula;

    if (min != null && max != null) {
      formula = String.format(Locale.ENGLISH, "(%s >= %f) && (%s <= %f)", var, min, var, max);
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

  /**
   * Creates a LimitConstraint from existing constraint.
   * 
   * @param constraint SBML Constraint
   */
  public LimitsConstraint(final Constraint constraint) {
    this.constraint = constraint;
  }

  public Constraint getConstraint() {
    return constraint;
  }

  /** Get limits */
  public Limits getLimits() {
    final ASTNode math = constraint.getMath();
    final List<ASTNode> nodes = math.getListOfNodes();
    final String var;
    Double min = null;
    Double max = null;

    // Constraint with a single condition
    if (math.toString().indexOf("&&") == -1) {
      var = nodes.get(0).getName();
      final double val = Double.parseDouble(nodes.get(1).toString());

      // Figure out whether val is a min or max, according to the math's
      // type
      final ASTNode.Type type = constraint.getMath().getType();

      // var <= val --> Val is a maximum
      if (type == ASTNode.Type.RELATIONAL_LEQ || type == ASTNode.Type.RELATIONAL_LT) {
        max = val;
      }

      // else: var >= val --> Val is a minimum
      else {
        min = val;
      }
    }

    // Constraint with two conditions. E.g. [mu_max >= 0, mu_max <= 5]
    else {
      // Get minimum from the left node (mu_max >= 0)
      final ASTNode leftNode = nodes.get(0);
      final List<ASTNode> leftNodes = leftNode.getListOfNodes();
      var = leftNodes.get(0).getName(); // variable (mu_max)
      min = Double.parseDouble(leftNodes.get(1).toString()); // min (0)

      // Get maximum from the right node (mu_max <= 5)
      final ASTNode rightNode = nodes.get(1);
      final List<ASTNode> rightNodes = rightNode.getListOfNodes();
      max = Double.parseDouble(rightNodes.get(1).toString()); // max (5)

    }

    return new Limits(var, min, max);
  }
}
