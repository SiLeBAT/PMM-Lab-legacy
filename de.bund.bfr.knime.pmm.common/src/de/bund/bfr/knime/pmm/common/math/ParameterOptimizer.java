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
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

	private LevenbergMarquardtOptimizer optimizer;
	private PointVectorValuePair optimizerValues;

	private boolean successful;
	private List<Double> parameterValues;
	private double rms;
	private double rSquare;
	private double aic;
	private double bic;
	private List<Double> parameterStandardErrors;
	private List<Double> parameterTValues;
	private List<Double> parameterPValues;

	private List<List<Integer>> changeLists;

	public ParameterOptimizer(String formula, List<String> parameters,
			List<Double> minParameterValues, List<Double> maxParameterValues,
			List<Double> minGuessValues, List<Double> maxGuessValues,
			List<Double> targetValues, List<String> arguments,
			List<List<Double>> argumentValues, boolean enforceLimits)
			throws ParseException {
		this.parameters = parameters;
		this.minParameterValues = minGuessValues;
		this.maxParameterValues = maxGuessValues;
		this.targetValues = targetValues;
		this.arguments = arguments;
		this.argumentValues = argumentValues;

		if (enforceLimits) {
			for (int i = 0; i < parameters.size(); i++) {
				Double min = minParameterValues.get(i);
				Double max = maxParameterValues.get(i);
				if (min != null) {
					formula += "+1000000*(" + parameters.get(i) + "<" + min
							+ ")";
				}
				if (max != null) {
					formula += "+1000000*(" + parameters.get(i) + ">" + max
							+ ")";
				}
			}
		}

		parser = MathUtilities.createParser();
		function = parser.parse(formula.substring(formula.indexOf("=") + 1));
		derivatives = new ArrayList<Node>(parameters.size());
		changeLists = createChangeLists();

		for (String arg : arguments) {
			parser.addVariable(arg, 0.0);
		}

		for (String param : parameters) {
			parser.addVariable(param, 0.0);
			derivatives.add(parser.differentiate(function, param));
		}
	}

	public void optimize(AtomicInteger progress, int nParameterSpace,
			int nLevenberg, boolean stopWhenSuccessful) {
		List<Double> paramMin = new ArrayList<Double>();
		List<Integer> paramStepCount = new ArrayList<Integer>();
		List<Double> paramStepSize = new ArrayList<Double>();
		int maxCounter = 1;
		int paramsWithRange = 0;
		int maxStepCount = 10;

		for (int i = 0; i < parameters.size(); i++) {
			Double min = minParameterValues.get(i);
			Double max = maxParameterValues.get(i);

			if (min != null && max != null) {
				paramsWithRange++;
			}
		}

		if (paramsWithRange != 0) {
			maxStepCount = (int) Math.pow(nParameterSpace,
					1.0 / paramsWithRange);
			maxStepCount = Math.max(maxStepCount, 2);
			maxStepCount = Math.min(maxStepCount, 10);
		}

		for (int i = 0; i < parameters.size(); i++) {
			Double min = minParameterValues.get(i);
			Double max = maxParameterValues.get(i);

			if (min != null && max != null) {
				paramMin.add(min);
				paramStepCount.add(maxStepCount);
				maxCounter *= maxStepCount;

				if (max > min) {
					paramStepSize.add((max - min) / (maxStepCount - 1));
				} else {
					paramStepSize.add(1.0);
				}
			} else if (min != null) {
				if (min != 0.0) {
					paramMin.add(min);
				} else {
					paramMin.add(MathUtilities.EPSILON);
				}

				paramStepCount.add(1);
				paramStepSize.add(1.0);
			} else if (max != null) {
				if (max != 0.0) {
					paramMin.add(max);
				} else {
					paramMin.add(-MathUtilities.EPSILON);
				}

				paramStepCount.add(1);
				paramStepSize.add(1.0);
			} else {
				paramMin.add(MathUtilities.EPSILON);
				paramStepCount.add(1);
				paramStepSize.add(1.0);
			}
		}

		List<List<Double>> bestValues = new ArrayList<List<Double>>();
		List<Double> bestError = new ArrayList<Double>();

		for (int i = 0; i < nLevenberg; i++) {
			bestValues.add(new ArrayList<Double>(Collections.nCopies(
					parameters.size(), i + 1.0)));
			bestError.add(Double.POSITIVE_INFINITY);
		}

		List<Integer> paramStepIndex = new ArrayList<Integer>(
				Collections.nCopies(parameters.size(), 0));
		boolean done = false;
		int counter = 0;

		while (!done) {
			progress.set(Float.floatToIntBits((float) counter
					/ (float) maxCounter));
			counter++;

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

			for (int i = nLevenberg; i >= 0; i--) {
				if (i == 0 || !(error < bestError.get(i - 1))) {
					if (i != nLevenberg) {
						bestError.add(i, error);
						bestValues.add(i, values);
						bestError.remove(nLevenberg);
						bestValues.remove(nLevenberg);
					}

					break;
				}
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

		successful = false;

		for (List<Double> startValues : bestValues) {
			try {
				optimize(startValues, 10000);

				if (!successful || optimizer.getRMS() < rms) {
					useCurrentResults();

					if (rSquare != 0.0) {
						successful = true;

						if (stopWhenSuccessful) {
							break;
						}
					}
				}
			} catch (TooManyEvaluationsException e) {
				break;
			} catch (ConvergenceException e) {
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

	public double getRMS() {
		return rms;
	}

	public double getRSquare() {
		return rSquare;
	}

	public double getAIC() {
		return aic;
	}

	public double getBIC() {
		return bic;
	}

	public List<Double> getParameterStandardErrors() {
		return parameterStandardErrors;
	}

	public List<Double> getParameterTValues() {
		return parameterTValues;
	}

	public List<Double> getParameterPValues() {
		return parameterPValues;
	}

	private void optimize(List<Double> startValues, int maxEval)
			throws Exception {
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

		optimizer = new LevenbergMarquardtOptimizer();
		optimizerValues = optimizer.optimize(maxEval, optimizerFunction,
				targets, weights, startValueArray);
	}

	private void useCurrentResults() {
		parameterValues = new ArrayList<Double>(parameters.size());

		for (int i = 0; i < parameters.size(); i++) {
			parameterValues.add(optimizerValues.getPoint()[i]);
		}

		try {
			double[] guess = optimizer.guessParametersErrors();

			parameterStandardErrors = new ArrayList<Double>(parameters.size());
			parameterTValues = new ArrayList<Double>(parameters.size());
			parameterPValues = new ArrayList<Double>(parameters.size());

			for (int i = 0; i < parameters.size(); i++) {
				parameterStandardErrors.add(guess[i]);

				double tValue = optimizerValues.getPoint()[i] / guess[i];
				int degreesOfFreedom = targetValues.size() - parameters.size();

				parameterTValues.add(tValue);
				parameterPValues.add(MathUtilities.getStudentProbability(
						tValue, degreesOfFreedom));
			}
		} catch (Exception e) {
			parameterStandardErrors = Collections.nCopies(parameters.size(),
					null);
			parameterTValues = Collections.nCopies(parameters.size(), null);
			parameterPValues = Collections.nCopies(parameters.size(), null);
		}

		double targetMean = MathUtilities.computeSum(targetValues)
				/ targetValues.size();
		double targetTotalSumOfSquares = 0.0;

		for (int i = 0; i < targetValues.size(); i++) {
			targetTotalSumOfSquares += Math.pow(targetValues.get(i)
					- targetMean, 2.0);
		}

		rms = optimizer.getRMS();
		rSquare = MathUtilities.getRSquared(rms, targetValues);
		aic = MathUtilities.akaikeCriterion(parameters.size(),
				targetValues.size(), rms);
		bic = MathUtilities.bayesCriterion(parameters.size(),
				targetValues.size(), rms);
	}

	private double evalWithSingularityCheck(Node f, List<Double> argValues,
			List<Double> paramValues) throws ParseException {
		for (int i = 0; i < parameters.size(); i++) {
			parser.setVarValue(parameters.get(i), paramValues.get(i));
		}

		for (List<Integer> list : changeLists) {
			for (int i = 0; i < arguments.size(); i++) {
				double d = list.get(i) * MathUtilities.EPSILON;

				parser.setVarValue(arguments.get(i), argValues.get(i) + d);
			}

			Object number = parser.evaluate(f);

			if (number instanceof Double && !Double.isNaN((Double) number)) {
				return (Double) number;
			}
		}

		if (derivatives.contains(f)) {
			for (List<Integer> list : changeLists) {
				for (int i = 0; i < arguments.size(); i++) {
					double d = list.get(i) * MathUtilities.EPSILON;

					parser.setVarValue(arguments.get(i), argValues.get(i) + d);
				}

				int index = derivatives.indexOf(f);

				parser.setVarValue(parameters.get(index),
						paramValues.get(index) - MathUtilities.EPSILON);

				Object number1 = parser.evaluate(function);

				parser.setVarValue(parameters.get(index),
						paramValues.get(index) + MathUtilities.EPSILON);

				Object number2 = parser.evaluate(function);

				if (number1 instanceof Double
						&& !Double.isNaN((Double) number1)
						&& number2 instanceof Double
						&& !Double.isNaN((Double) number2)) {
					return ((Double) number2 - (Double) number1)
							/ (2 * MathUtilities.EPSILON);
				}
			}
		}

		return Double.NaN;
	}

	private List<List<Integer>> createChangeLists() {
		int n = arguments.size();
		boolean done = false;
		List<List<Integer>> changeLists = new ArrayList<List<Integer>>();
		List<Integer> list = new ArrayList<Integer>(Collections.nCopies(n, -1));

		while (!done) {
			changeLists.add(new ArrayList<Integer>(list));

			for (int i = 0;; i++) {
				if (i >= n) {
					done = true;
					break;
				}

				list.set(i, list.get(i) + 1);

				if (list.get(i) > 1) {
					list.set(i, -1);
				} else {
					break;
				}
			}
		}

		Collections.sort(changeLists, new Comparator<List<Integer>>() {

			@Override
			public int compare(List<Integer> l1, List<Integer> l2) {
				int n1 = 0;
				int n2 = 0;

				for (int i : l1) {
					if (i == 0) {
						n1++;
					}
				}

				for (int i : l2) {
					if (i == 0) {
						n2++;
					}
				}

				if (n1 < n2) {
					return 1;
				} else if (n1 > n2) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		return changeLists;
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
			List<Double> paramValues = new ArrayList<Double>();

			for (int i = 0; i < parameters.size(); i++) {
				paramValues.add(point[i]);
			}

			try {
				for (int i = 0; i < targetValues.size(); i++) {
					List<Double> argValues = new ArrayList<Double>();

					for (int j = 0; j < arguments.size(); j++) {
						argValues.add(argumentValues.get(j).get(i));
					}

					for (int j = 0; j < derivatives.size(); j++) {
						retValue[i][j] = evalWithSingularityCheck(
								derivatives.get(j), argValues, paramValues);
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return retValue;
		}
	};
}
