/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package de.bund.bfr.knime.pmm.common.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.TDistribution;
import org.lsmp.djep.djep.DJep;
import org.lsmp.djep.djep.DiffRulesI;
import org.lsmp.djep.djep.diffRules.MacroDiffRules;
import org.lsmp.djep.xjep.MacroFunction;
import org.nfunk.jep.ASTConstant;
import org.nfunk.jep.ASTFunNode;
import org.nfunk.jep.ASTVarNode;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

public class MathUtilities {

	public static double EPSILON = 0.00001;

	private static Random random = null;

	private MathUtilities() {
	}

	public static String nodeToString(Node n) throws Exception {
		if (n instanceof ASTFunNode) {
			String s = n.toString() + "(";

			for (int i = 0; i < n.jjtGetNumChildren(); i++) {
				if (i != 0) {
					s += ",";
				}

				s += nodeToString(n.jjtGetChild(i));
			}

			return s + ")";
		} else if (n instanceof ASTConstant) {
			return n.toString();
		} else if (n instanceof ASTVarNode) {
			return n.toString();
		} else {
			throw new Exception("Unknown Node");
		}
	}

	public static void removeNullValues(List<Double> targetValues,
			List<List<Double>> argumentValues) {
		for (int i = 0; i < targetValues.size(); i++) {
			boolean remove = false;

			if (targetValues.get(i) == null) {
				remove = true;
				continue;
			}

			if (!remove) {
				for (int j = 0; j < argumentValues.size(); j++) {
					if (argumentValues.get(j).get(i) == null) {
						remove = true;
						break;
					}
				}
			}

			if (remove) {
				targetValues.remove(i);

				for (int j = 0; j < argumentValues.size(); j++) {
					argumentValues.get(j).remove(i);
				}

				i--;
			}
		}
	}

	public static Random getRandomGenerator() {
		if (random == null) {
			random = new Random();
		}

		return random;
	}

	public static int getRandomNegativeInt() {
		if (random == null) {
			random = new Random();
		}

		int value = random.nextInt();

		if (value > 0) {
			value = -value;
		}

		return value;
	}

	public static double computeSum(List<Double> values) {
		double sum = 0.0;

		for (double v : values) {
			sum += v;
		}

		return sum;
	}

	public static DJep createParser() {
		DJep parser = new DJep();

		parser.setAllowAssignment(true);
		parser.setAllowUndeclared(true);
		parser.setImplicitMul(true);
		parser.addStandardFunctions();
		parser.addStandardDiffRules();
		parser.removeVariable("x");

		try {
			// parser.removeFunction("log");
			// parser.addFunction("log", new MacroFunction("log", 1, "ln(x)",
			// parser));
			// parser.addDiffRule(new MacroDiffRules(parser, "log", "1/x"));
			parser.addFunction("log10", new MacroFunction("log10", 1,
					"ln(x)/ln(10)", parser));
			parser.addDiffRule(new MacroDiffRules(parser, "log10",
					"1/(x*ln(10))"));

			parser.addDiffRule(new ZeroDiffRule("<"));
			parser.addDiffRule(new ZeroDiffRule(">"));
			parser.addDiffRule(new ZeroDiffRule("<="));
			parser.addDiffRule(new ZeroDiffRule(">="));
			parser.addDiffRule(new ZeroDiffRule("&&"));
			parser.addDiffRule(new ZeroDiffRule("||"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return parser;
	}

	public static String replaceVariable(String formula, String var,
			String newVar) {
		if (var.equals(newVar)) {
			return formula;
		}

		String newFormular = " " + formula + " ";
		boolean foundReplacement = true;

		while (foundReplacement) {
			foundReplacement = false;

			for (int i = 1; i < newFormular.length() - var.length(); i++) {
				boolean matches = newFormular.substring(i, i + var.length())
						.equals(var);
				boolean start = !isVariableCharacter(newFormular.charAt(i - 1));
				boolean end = !isVariableCharacter(newFormular.charAt(i
						+ var.length()));

				if (matches && start && end) {
					String orginal = newFormular.substring(i - 1,
							i + var.length() + 1);
					String replacement = newFormular.charAt(i - 1) + newVar
							+ newFormular.charAt(i + var.length());

					newFormular = newFormular.replace(orginal, replacement);
					foundReplacement = true;
					break;
				}
			}
		}

		return newFormular.replace(" ", "");
	}

	public static boolean isFunctionDefinedFor(String formula,
			List<String> parameters, List<Double> parameterValues,
			String variable, double minValue, double maxValue, int steps) {
		DJep parser = createParser();
		Node function = null;

		parser.addVariable(variable, 0.0);

		for (int i = 0; i < parameters.size(); i++) {
			parser.addConstant(parameters.get(i), parameterValues.get(i));
		}

		try {
			function = parser
					.parse(formula.substring(formula.indexOf("=") + 1));

			for (int i = 0; i < steps; i++) {
				double value = minValue + (double) i / (double) (steps - 1)
						* (maxValue - minValue);

				parser.setVarValue(variable, value);

				if (!(parser.evaluate(function) instanceof Double)) {
					return false;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return true;
	}

	public static List<Double> evaluateFunction(String formula,
			List<String> parameters, List<Double> parameterValues,
			List<String> variables, List<List<Double>> variableValues) {
		List<Double> values = new ArrayList<Double>();
		DJep parser = createParser();

		for (int i = 0; i < parameters.size(); i++) {
			parser.addConstant(parameters.get(i), parameterValues.get(i));
		}

		for (int i = 0; i < variables.size(); i++) {
			parser.addVariable(variables.get(i), 0.0);
		}

		try {
			Node function = parser
					.parse(formula.substring(formula.indexOf("=") + 1));

			for (int i = 0; i < variableValues.get(0).size(); i++) {
				for (int j = 0; j < variables.size(); j++) {
					parser.setVarValue(variables.get(j), variableValues.get(j)
							.get(i));
				}

				values.add((Double) parser.evaluate(function));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return values;
	}

	private static class ZeroDiffRule implements DiffRulesI {

		private String name;

		public ZeroDiffRule(String name) {
			this.name = name;
		}

		@Override
		public Node differentiate(ASTFunNode node, String var, Node[] children,
				Node[] dchildren, DJep djep) throws ParseException {
			return djep.getNodeFactory().buildConstantNode(0.0);
		}

		@Override
		public String getName() {
			return name;
		}

	}

	public static Double getRSquared(double rms, List<Double> targetValues) {
		double targetMean = MathUtilities.computeSum(targetValues)
				/ targetValues.size();
		double targetTotalSumOfSquares = 0.0;

		for (int i = 0; i < targetValues.size(); i++) {
			targetTotalSumOfSquares += Math.pow(targetValues.get(i)
					- targetMean, 2.0);
		}

		double rSquared = 1 - rms * rms * targetValues.size()
				/ targetTotalSumOfSquares;

		// rSquare < 0 möglich, siehe hier:
		// http://mars.wiwi.hu-berlin.de/mediawiki/sk/index.php/Bestimmtheitsmass
		return Math.max(rSquared, 0.0);
	}

	public static Double akaikeCriterion(final int numParam,
			final int numSample, final double rms) {

		if (Double.isNaN(rms) || Double.isInfinite(rms)) {
			return null;
		}

		if (rms < 0) {
			return null;
		}

		if (numParam <= 0) {
			return null;
		}

		if (numSample <= 0) {
			return null;
		}

		if (numSample <= numParam + 2) {
			return null;
		}

		// return numSample * Math.log(rms * rms) + 2 * numParam;
		return numSample * Math.log(rms * rms) + 2 * (numParam + 1) + 2
				* (numParam + 1) * (numParam + 2) / (numSample - numParam - 2);
	}

	public static Double bayesCriterion(final int numParam,
			final int numSample, final double rms) {

		if (Double.isNaN(rms) || Double.isInfinite(rms))
			return null;

		if (rms < 0)
			return null;

		if (numParam <= 0)
			return null;

		if (numSample <= 0)
			return null;

		return numSample * Math.log(rms * rms) + numParam * Math.log(numSample);
	}

	public static double getPValue(double tValue, int degreesOfFreedom) {
		TDistribution dist = new TDistribution(degreesOfFreedom);

		return 1.0 - dist.probability(-Math.abs(tValue), Math.abs(tValue));
	}

	public static boolean isVariableCharacter(char ch) {
		return Character.isLetterOrDigit(ch) || ch == '_' || ch == '$';
	}

}
