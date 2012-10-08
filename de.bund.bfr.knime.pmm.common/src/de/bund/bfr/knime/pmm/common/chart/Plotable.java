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
package de.bund.bfr.knime.pmm.common.chart;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lsmp.djep.djep.DJep;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

import de.bund.bfr.knime.pmm.common.math.MathUtilities;

public class Plotable {

	private static final int FUNCTION_STEPS = 1000;

	public static final int DATASET = 0;
	public static final int FUNCTION = 1;
	public static final int BOTH = 2;
	public static final int BOTH_STRICT = 3;

	private int type;
	private Map<String, List<Double>> valueLists;
	private String function;
	private String functionValue;
	private Map<String, List<Double>> functionArguments;
	private Map<String, Double> functionConstants;

	public Plotable(int type) {
		this.type = type;
		valueLists = new HashMap<String, List<Double>>();
	}

	public int getType() {
		return type;
	}

	public List<Double> getValueList(String parameter) {
		return valueLists.get(parameter);
	}

	public void addValueList(String parameter, List<Double> valueList) {
		valueLists.put(parameter, valueList);
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getFunctionValue() {
		return functionValue;
	}

	public void setFunctionValue(String functionValue) {
		this.functionValue = functionValue;
	}

	public Map<String, List<Double>> getFunctionArguments() {
		return functionArguments;
	}

	public void setFunctionArguments(Map<String, List<Double>> functionArguments) {
		this.functionArguments = functionArguments;
	}

	public Map<String, Double> getFunctionConstants() {
		return functionConstants;
	}

	public void setFunctionConstants(Map<String, Double> functionConstants) {
		this.functionConstants = functionConstants;
	}

	public double[][] getPoints(String paramX, String paramY, String transformY) {
		return getPoints(paramX, paramY, transformY, getStandardChoice());
	}

	public double[][] getPoints(String paramX, String paramY,
			String transformY, Map<String, Integer> choice) {
		List<Double> xList = valueLists.get(paramX);
		List<Double> yList = valueLists.get(paramY);

		if (xList == null || yList == null) {
			return null;
		}

		List<Boolean> usedPoints = new ArrayList<Boolean>(Collections.nCopies(
				xList.size(), true));

		if (type == BOTH_STRICT) {
			for (String arg : functionArguments.keySet()) {
				if (!arg.equals(paramX) && valueLists.containsKey(arg)) {
					Double fixedValue = functionArguments.get(arg).get(
							choice.get(arg));
					List<Double> values = valueLists.get(arg);

					for (int i = 0; i < values.size(); i++) {
						if (!fixedValue.equals(values.get(i))) {
							usedPoints.set(i, false);
						}
					}
				}
			}
		}

		List<Point2D.Double> points = new ArrayList<Point2D.Double>(
				xList.size());

		for (int i = 0; i < xList.size(); i++) {
			Double x = xList.get(i);
			Double y = yList.get(i);

			if (y != null) {
				y = transformDouble(y, transformY);
			}

			if (usedPoints.get(i) && isValidValue(x) && isValidValue(y)) {
				points.add(new Point2D.Double(x, y));
			}
		}

		Collections.sort(points, new Comparator<Point2D.Double>() {

			@Override
			public int compare(java.awt.geom.Point2D.Double p1,
					java.awt.geom.Point2D.Double p2) {
				return Double.compare(p1.x, p2.x);
			}
		});

		double[][] pointsArray = new double[2][points.size()];

		for (int i = 0; i < points.size(); i++) {
			pointsArray[0][i] = points.get(i).x;
			pointsArray[1][i] = points.get(i).y;
		}

		return pointsArray;
	}

	public double[][] getFunctionPoints(String paramX, String paramY,
			String transformY, double minX, double maxX, double minY,
			double maxY) {
		return getFunctionPoints(paramX, paramY, transformY, minX, maxX, minY,
				maxY, getStandardChoice());
	}

	public double[][] getFunctionPoints(String paramX, String paramY,
			String transformY, double minX, double maxX, double minY,
			double maxY, Map<String, Integer> choice) {
		if (function == null) {
			return null;
		}

		if (!function.startsWith(paramY + "=") || !function.contains(paramX)) {
			return null;
		}

		double[][] points = new double[2][FUNCTION_STEPS];
		DJep parser = MathUtilities.createParser();
		Node f = null;

		for (String param : functionConstants.keySet()) {
			if (functionConstants.get(param) == null) {
				return null;
			}

			parser.addConstant(param, functionConstants.get(param));
		}

		for (String param : functionArguments.keySet()) {
			if (!param.equals(paramX)) {
				parser.addConstant(param,
						functionArguments.get(param).get(choice.get(param)));
			}
		}

		parser.addVariable(paramX, 0.0);

		try {
			f = parser.parse(function.replace(paramY + "=", ""));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (int j = 0; j < FUNCTION_STEPS; j++) {
			double x = minX + (double) j / (double) (FUNCTION_STEPS - 1)
					* (maxX - minX);

			parser.setVarValue(paramX, x);

			try {
				Object number = parser.evaluate(f);
				double y;

				if (number instanceof Double) {
					y = (Double) number;
					y = transformDouble(y, transformY);

					if (y < minY || y > maxY || Double.isInfinite(y)) {
						y = Double.NaN;
					}
				} else {
					y = Double.NaN;
				}

				points[0][j] = x;
				points[1][j] = y;
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}

		return points;
	}

	public boolean isPlotable() {
		if (type == FUNCTION) {
			for (String param : functionConstants.keySet()) {
				if (functionConstants.get(param) == null) {
					return false;
				}
			}

			return true;
		} else {
			List<String> paramsX = new ArrayList<String>(valueLists.keySet());
			List<String> paramsY = new ArrayList<String>();

			if (type == DATASET) {
				paramsY = paramsX;
			} else if (type == BOTH || type == BOTH_STRICT) {
				if (functionValue != null) {
					paramsY = Arrays.asList(functionValue);
				}
			}

			for (String paramX : paramsX) {
				for (String paramY : paramsY) {
					if (isPlotable(paramX, paramY)) {
						return true;
					}
				}
			}

			return false;
		}
	}

	public List<Map<String, Integer>> getAllChoices() {
		List<Map<String, Integer>> choices = new ArrayList<Map<String, Integer>>();
		List<String> argList = new ArrayList<String>(functionArguments.keySet());
		List<Integer> choice = new ArrayList<Integer>(Collections.nCopies(
				argList.size(), 0));
		boolean done = false;

		while (!done) {
			Map<String, Integer> map = new LinkedHashMap<String, Integer>();

			for (int i = 0; i < argList.size(); i++) {
				map.put(argList.get(i), choice.get(i));
			}

			choices.add(map);

			for (int i = 0;; i++) {
				if (i >= argList.size()) {
					done = true;
					break;
				}

				choice.set(i, choice.get(i) + 1);

				if (choice.get(i) >= functionArguments.get(argList.get(i))
						.size()) {
					choice.set(i, 0);
				} else {
					break;
				}
			}
		}

		return choices;
	}

	public int getNumberOfCombinations() {
		int nMax = 1;

		for (String arg0 : functionArguments.keySet()) {
			int n = 1;

			for (String arg : functionArguments.keySet()) {
				if (!arg.equals(arg0) && valueLists.containsKey(arg)) {
					Set<Double> set = new HashSet<Double>(valueLists.get(arg));

					n *= set.size();
				}
			}

			nMax = Math.max(nMax, n);
		}

		return nMax;
	}

	private Map<String, Integer> getStandardChoice() {
		if (functionArguments == null) {
			return null;
		}

		Map<String, Integer> choice = new HashMap<String, Integer>();

		for (String arg : functionArguments.keySet()) {
			choice.put(arg, 0);
		}

		return choice;
	}

	private boolean isPlotable(String paramX, String paramY) {
		boolean dataSetPlotable = false;
		boolean functionPlotable = false;
		List<Double> xs = valueLists.get(paramX);
		List<Double> ys = valueLists.get(paramY);

		if (xs != null && ys != null) {
			for (int i = 0; i < xs.size(); i++) {
				if (isValidValue(xs.get(i)) && isValidValue(ys.get(i))) {
					dataSetPlotable = true;
					break;
				}
			}
		}

		if (function != null && functionValue.equals(paramY)
				&& functionArguments.containsKey(paramX)) {
			boolean notValid = false;

			for (Double value : functionConstants.values()) {
				if (!isValidValue(value)) {
					notValid = true;
					break;
				}
			}

			if (!notValid) {
				functionPlotable = true;
			}
		}

		if (type == DATASET) {
			return dataSetPlotable;
		} else if (type == FUNCTION) {
			return functionPlotable;
		} else if (type == BOTH || type == BOTH_STRICT) {
			return dataSetPlotable && functionPlotable;
		}

		return false;
	}

	private boolean isValidValue(Double value) {
		return value != null && !value.isNaN() && !value.isInfinite();
	}

	private double transformDouble(double value, String transform) {
		if (transform.equals(ChartConstants.NO_TRANSFORM)) {
			return value;
		} else if (transform.equals(ChartConstants.SQRT_TRANSFORM)) {
			return Math.sqrt(value);
		} else if (transform.equals(ChartConstants.LOG_TRANSFORM)) {
			return Math.log(value);
		} else if (transform.equals(ChartConstants.LOG10_TRANSFORM)) {
			return Math.log10(value);
		} else if (transform.equals(ChartConstants.EXP_TRANSFORM)) {
			return Math.exp(value);
		} else if (transform.equals(ChartConstants.EXP10_TRANSFORM)) {
			return Math.pow(10.0, value);
		}

		return Double.NaN;
	}

}