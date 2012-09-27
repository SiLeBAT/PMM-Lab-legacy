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
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.optimization.PointVectorValuePair;
import org.apache.commons.math3.optimization.general.LevenbergMarquardtOptimizer;
import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.type.Complex;

public class ParameterOptimizer {

	private List<String> parameters;
	private List<Double> minParameterValues;
	private List<Double> maxParameterValues;
	private List<Double> targetValues;
	private List<String> arguments;
	private List<List<Double>> argumentValues;

	private Node function;
	private List<Node> derivatives;

	private DJep parser;

	private boolean successful;
	private List<Double> parameterValues;
	private double standardError;
	private double rSquare;
	private List<Double> parameterStandardErrors;

	public ParameterOptimizer(String formula, List<String> parameters,
			List<Double> minParameterValues, List<Double> maxParameterValues,
			List<Double> targetValues, List<String> arguments,
			List<List<Double>> argumentValues) throws ParseException {
		this.parameters = parameters;
		this.minParameterValues = minParameterValues;
		this.maxParameterValues = maxParameterValues;
		this.targetValues = targetValues;
		this.arguments = arguments;
		this.argumentValues = argumentValues;

		// for (int i = 0; i < parameters.size(); i++) {
		// Double min = minParameterValues.get(i);
		// Double max = maxParameterValues.get(i);
		//
		// if (min != null) {
		// formula += "+1000000*(" + parameters.get(i) + "<" + min + ")";
		// }
		//
		// if (max != null) {
		// formula += "+1000000*(" + parameters.get(i) + ">" + max + ")";
		// }
		// }

		parser = MathUtilities.createParser();
		function = parser.parse(formula.substring(formula.indexOf("=") + 1));
		derivatives = new ArrayList<Node>(parameters.size());

		for (String arg : arguments) {
			parser.addVariable(arg, 0.0);
		}

		for (String param : parameters) {
			parser.addVariable(param, 0.0);
			derivatives.add(parser.differentiate(function, param));
		}
	}

	public void optimize() {
		List<Double> paramMin = new ArrayList<Double>();
		List<Integer> paramStepCount = new ArrayList<Integer>();
		List<Double> paramStepSize = new ArrayList<Double>();
		int maxStepCount = 11;

		for (int i = 0; i < parameters.size(); i++) {
			Double min = minParameterValues.get(i);
			Double max = maxParameterValues.get(i);

			if (min != null && max != null) {
				paramMin.add(min);
				paramStepCount.add(maxStepCount);

				if (max > min) {
					paramStepSize.add((max - min) / (maxStepCount - 1));
				} else {
					paramStepSize.add(1.0);
				}
			} else if (min != null) {
				paramMin.add(min);
				paramStepCount.add(1);
				paramStepSize.add(1.0);
			} else if (max != null) {
				paramMin.add(max);
				paramStepCount.add(1);
				paramStepSize.add(1.0);
			} else {
				paramMin.add(1.0);
				paramStepCount.add(1);
				paramStepSize.add(1.0);
			}
		}

		List<Double> bestValues = new ArrayList<Double>(Collections.nCopies(
				parameters.size(), 1.0));
		double bestError = Double.POSITIVE_INFINITY;
		List<Integer> paramStepIndex = new ArrayList<Integer>(
				Collections.nCopies(parameters.size(), 0));
		boolean done = false;

		while (!done) {
			List<Double> values = new ArrayList<Double>();
			double error = 0.0;

			for (int i = 0; i < parameters.size(); i++) {
				double value = paramMin.get(i) + paramStepIndex.get(i)
						* paramStepSize.get(i);

				values.add(value);
				parser.setVarValue(parameters.get(i), value);
			}

			for (int i = 0; i < targetValues.size(); i++) {
				for (int j = 0; j < arguments.size(); j++) {
					parser.setVarValue(arguments.get(j), argumentValues.get(j)
							.get(i));
				}

				try {
					double value = (Double) parser.evaluate(function);
					double diff = targetValues.get(i) - value;

					error += diff * diff;
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (ClassCastException e) {
					error = Double.POSITIVE_INFINITY;
					break;
				}
			}

			if (error < bestError) {
				bestError = error;
				bestValues = values;
			}

			for (int i = 0;; i++) {
				if (i >= parameters.size()) {
					done = true;
					break;
				}

				paramStepIndex.set(i, paramStepIndex.get(i) + 1);

				if (paramStepIndex.get(i) >= paramStepCount.get(i)) {
					paramStepIndex.set(i, 0);
				} else {
					break;
				}
			}
		}

		double[] factors = new double[] { 1.0, 0.9, 1.1, 0.8, 1.2, 0.7, 1.3,
				0.6, 1.4, 0.5, 1.5 };

		successful = false;

		for (double factor : factors) {
			List<Double> startValues = new ArrayList<Double>(parameters.size());

			for (double value : bestValues) {
				startValues.add(value * factor);
			}

			try {
				optimize(startValues, 10000);
				successful = true;
				break;
			} catch (TooManyEvaluationsException e) {
				break;
			} catch (ConvergenceException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isSuccessful() {
		return successful;
	}

	public List<Double> getParameterValues() {
		return parameterValues;
	}

	public double getStandardError() {
		return standardError;
	}

	public double getRSquare() {
		return rSquare;
	}

	public List<Double> getParameterStandardErrors() {
		return parameterStandardErrors;
	}

	private void optimize(List<Double> startValues, int maxEval)
			throws Exception {
		LevenbergMarquardtOptimizer optimizer = new LevenbergMarquardtOptimizer();
		double[] targets = new double[targetValues.size()];
		double[] weights = new double[targetValues.size()];
		double[] startValueArray = new double[startValues.size()];

		for (int i = 0; i < targetValues.size(); i++) {
			targets[i] = targetValues.get(i);
			weights[i] = 1.0;
		}

		for (int i = 0; i < startValues.size(); i++) {
			startValueArray[i] = startValues.get(i);
		}

		PointVectorValuePair p = optimizer.optimize(maxEval, optimizerFunction,
				targets, weights, startValueArray);

		parameterValues = new ArrayList<Double>(parameters.size());

		for (int i = 0; i < parameters.size(); i++) {
			parameterValues.add(p.getPoint()[i]);
		}

		try {
			double[] guess = optimizer.guessParametersErrors();

			parameterStandardErrors = new ArrayList<Double>(parameters.size());

			for (int i = 0; i < parameters.size(); i++) {
				parameterStandardErrors.add(guess[i]);
			}
		} catch (Exception e) {
			parameterStandardErrors = Collections.nCopies(parameters.size(),
					null);
		}

		double targetMean = MathUtilities.computeSum(targetValues)
				/ targetValues.size();
		double targetTotalSumOfSquares = 0.0;

		for (int i = 0; i < targetValues.size(); i++) {
			targetTotalSumOfSquares += Math.pow(targetValues.get(i)
					- targetMean, 2.0);
		}

		standardError = optimizer.getRMS();
		rSquare = 1 - standardError * standardError * targetValues.size()
				/ targetTotalSumOfSquares;
	}

	private DifferentiableMultivariateVectorFunction optimizerFunction = new DifferentiableMultivariateVectorFunction() {

		@Override
		public double[] value(double[] point) throws IllegalArgumentException {
			double[] retValue = new double[targetValues.size()];

			for (int i = 0; i < parameters.size(); i++) {
				parser.setVarValue(parameters.get(i), point[i]);
			}

			try {
				for (int i = 0; i < targetValues.size(); i++) {
					for (int j = 0; j < arguments.size(); j++) {
						parser.setVarValue(arguments.get(j), argumentValues
								.get(j).get(i));
					}

					Object number = parser.evaluate(function);

					if (number instanceof Complex) {
						retValue[i] = Double.NaN;
					} else {
						retValue[i] = (Double) number;
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return retValue;
		}

		@Override
		public MultivariateMatrixFunction jacobian() {
			return optimizerFunctionJacobian;
		}

	};

	private MultivariateMatrixFunction optimizerFunctionJacobian = new MultivariateMatrixFunction() {

		@Override
		public double[][] value(double[] point) throws IllegalArgumentException {
			double[][] retValue = new double[targetValues.size()][parameters
					.size()];

			for (int i = 0; i < parameters.size(); i++) {
				parser.setVarValue(parameters.get(i), point[i]);
			}

			try {
				for (int i = 0; i < targetValues.size(); i++) {
					for (int j = 0; j < arguments.size(); j++) {
						parser.setVarValue(arguments.get(j), argumentValues
								.get(j).get(i));
					}

					for (int j = 0; j < derivatives.size(); j++) {
						Object number = parser.evaluate(derivatives.get(j));

						if (number instanceof Complex) {
							retValue[i][j] = Double.NaN;
						} else {
							retValue[i][j] = (Double) number;
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return retValue;
		}
	};

}
