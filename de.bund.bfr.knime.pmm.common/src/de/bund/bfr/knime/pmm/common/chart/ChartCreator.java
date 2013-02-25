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

import java.awt.Color;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;

import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;

public class ChartCreator extends ChartPanel {

	private static final long serialVersionUID = 1L;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;
	private Map<String, Color> colors;
	private Map<String, Shape> shapes;
	private Map<String, List<Color>> colorLists;
	private Map<String, List<Shape>> shapeLists;

	private String paramX;
	private String paramY;
	private String unitX;
	private String unitY;
	private String transformY;
	private boolean useManualRange;
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;
	private boolean drawLines;
	private boolean showLegend;
	private boolean addInfoInLegend;
	private boolean showConfidenceInterval;

	public ChartCreator(Map<String, Plotable> plotables,
			Map<String, String> shortLegend, Map<String, String> longLegend) {
		super(new JFreeChart(new XYPlot()));
		getPopupMenu().insert(new DataAndModelChartSaveAsItem(), 4);
		this.plotables = plotables;
		this.shortLegend = shortLegend;
		this.longLegend = longLegend;
		colors = new LinkedHashMap<String, Color>();
		shapes = new LinkedHashMap<String, Shape>();
		colorLists = new LinkedHashMap<String, List<Color>>();
		shapeLists = new LinkedHashMap<String, List<Shape>>();
	}

	public void createChart(String idToPaint) {
		setChart(getChart(idToPaint));
	}

	public void createChart(List<String> idsToPaint) {
		setChart(getChart(idsToPaint));
	}

	public JFreeChart getChart(String idToPaint) {
		if (idToPaint != null) {
			return getChart(Arrays.asList(idToPaint));
		} else {
			return getChart(new ArrayList<String>());
		}
	}

	public JFreeChart getChart(List<String> idsToPaint) {
		if (paramX == null || paramY == null) {
			return new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT,
					new XYPlot(), showLegend);
		}

		String labelY = AttributeUtilities.getNameWithUnit(paramY, unitY,
				transformY);
		NumberAxis xAxis = new NumberAxis(AttributeUtilities.getNameWithUnit(
				paramX, unitX));
		NumberAxis yAxis = new NumberAxis(labelY);
		XYPlot plot = new XYPlot(null, xAxis, yAxis, null);
		double usedMinX = Double.POSITIVE_INFINITY;
		double usedMaxX = Double.NEGATIVE_INFINITY;
		int index = 0;
		ColorAndShapeCreator colorAndShapeCreator = new ColorAndShapeCreator(
				idsToPaint.size());

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable != null) {
				if (plotable.getType() == Plotable.BOTH
						|| plotable.getType() == Plotable.BOTH_STRICT) {
					Double minArg = AttributeUtilities.convertFromStandardUnit(
							paramX, plotable.getMinArguments().get(paramX),
							unitX);
					Double maxArg = AttributeUtilities.convertFromStandardUnit(
							paramX, plotable.getMaxArguments().get(paramX),
							unitX);

					if (minArg != null) {
						usedMinX = Math.min(usedMinX, minArg);
					}

					if (maxArg != null) {
						usedMaxX = Math.max(usedMaxX, maxArg);
					}

					for (Map<String, Integer> choice : plotable.getAllChoices()) {
						double[][] points = plotable.getPoints(paramX, paramY,
								unitX, unitY, transformY, choice);

						if (points != null) {
							for (int i = 0; i < points[0].length; i++) {
								usedMinX = Math.min(usedMinX, points[0][i]);
								usedMaxX = Math.max(usedMaxX, points[0][i]);
							}
						}
					}
				} else if (plotable.getType() == Plotable.DATASET
						|| plotable.getType() == Plotable.DATASET_STRICT) {
					double[][] points = plotable.getPoints(paramX, paramY,
							unitX, unitY, transformY);

					if (points != null) {
						for (int i = 0; i < points[0].length; i++) {
							usedMinX = Math.min(usedMinX, points[0][i]);
							usedMaxX = Math.max(usedMaxX, points[0][i]);
						}
					}
				} else if (plotable.getType() == Plotable.FUNCTION) {
					Double minArg = AttributeUtilities.convertFromStandardUnit(
							paramX, plotable.getMinArguments().get(paramX),
							unitX);
					Double maxArg = AttributeUtilities.convertFromStandardUnit(
							paramX, plotable.getMaxArguments().get(paramX),
							unitX);

					if (minArg != null) {
						usedMinX = Math.min(usedMinX, minArg);
					}

					if (maxArg != null) {
						usedMaxX = Math.max(usedMaxX, maxArg);
					}
				} else if (plotable.getType() == Plotable.FUNCTION_SAMPLE) {
					Double minArg = AttributeUtilities.convertFromStandardUnit(
							paramX, plotable.getMinArguments().get(paramX),
							unitX);
					Double maxArg = AttributeUtilities.convertFromStandardUnit(
							paramX, plotable.getMaxArguments().get(paramX),
							unitX);

					if (minArg != null) {
						usedMinX = Math.min(usedMinX, minArg);
					}

					if (maxArg != null) {
						usedMaxX = Math.max(usedMaxX, maxArg);
					}

					for (Double x : plotable.getSamples()) {
						if (x != null) {
							usedMinX = Math.min(usedMinX, x);
							usedMaxX = Math.max(usedMaxX, x);
						}
					}
				}
			}
		}

		if (Double.isInfinite(usedMinX)) {
			usedMinX = 0.0;
		}

		if (Double.isInfinite(usedMaxX)) {
			usedMaxX = 100.0;
		}

		if (paramX.equals(AttributeUtilities.TIME)
				|| paramX.equals(AttributeUtilities.LOGC)) {
			usedMinX = Math.min(usedMinX, 0.0);
			xAxis.setAutoRangeIncludesZero(true);
		} else {
			xAxis.setAutoRangeIncludesZero(false);
		}

		if (paramY.equals(AttributeUtilities.TIME)
				|| paramY.equals(AttributeUtilities.LOGC)) {
			yAxis.setAutoRangeIncludesZero(true);
		} else {
			yAxis.setAutoRangeIncludesZero(false);
		}

		if (usedMinX == usedMaxX) {
			usedMinX -= 1.0;
			usedMaxX += 1.0;
		}

		if (useManualRange && minX < maxX && minY < maxY) {
			usedMinX = minX;
			usedMaxX = maxX;
			xAxis.setRange(new Range(minX, maxX));
			yAxis.setRange(new Range(minY, maxY));
		}

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable != null && plotable.getType() == Plotable.DATASET) {
				plotDataSet(plot, plotable, id, colorAndShapeCreator
						.getColorList().get(index), colorAndShapeCreator
						.getShapeList().get(index));
				index++;
			}
		}

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable != null
					&& plotable.getType() == Plotable.DATASET_STRICT) {
				plotDataSetStrict(plot, plotable, id);
				index++;
			}
		}

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable != null && plotable.getType() == Plotable.FUNCTION) {
				plotFunction(plot, plotable, id, colorAndShapeCreator
						.getColorList().get(index), colorAndShapeCreator
						.getShapeList().get(index), usedMinX, usedMaxX);
				index++;
			}
		}

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable != null
					&& plotable.getType() == Plotable.FUNCTION_SAMPLE) {
				plotFunctionSample(plot, plotable, id, colorAndShapeCreator
						.getColorList().get(index), colorAndShapeCreator
						.getShapeList().get(index), usedMinX, usedMaxX);
				index++;
			}
		}

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable != null && plotable.getType() == Plotable.BOTH) {
				plotBoth(plot, plotable, id, colorAndShapeCreator
						.getColorList().get(index), colorAndShapeCreator
						.getShapeList().get(index), usedMinX, usedMaxX);
				index++;
			}
		}

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable != null && plotable.getType() == Plotable.BOTH_STRICT) {
				plotBothStrict(plot, plotable, id, usedMinX, usedMaxX);
				index++;
			}
		}

		return new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, plot,
				showLegend);
	}

	public Map<String, Plotable> getPlotables() {
		return plotables;
	}

	public String getParamX() {
		return paramX;
	}

	public void setParamX(String paramX) {
		this.paramX = paramX;
	}

	public String getParamY() {
		return paramY;
	}

	public void setParamY(String paramY) {
		this.paramY = paramY;
	}

	public String getUnitX() {
		return unitX;
	}

	public void setUnitX(String unitX) {
		this.unitX = unitX;
	}

	public String getUnitY() {
		return unitY;
	}

	public void setUnitY(String unitY) {
		this.unitY = unitY;
	}

	public String getTransformY() {
		return transformY;
	}

	public void setTransformY(String transformY) {
		this.transformY = transformY;
	}

	public boolean isUseManualRange() {
		return useManualRange;
	}

	public void setUseManualRange(boolean useManualRange) {
		this.useManualRange = useManualRange;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMaxY() {
		return maxY;
	}

	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	public boolean isDrawLines() {
		return drawLines;
	}

	public void setDrawLines(boolean drawLines) {
		this.drawLines = drawLines;
	}

	public boolean isShowLegend() {
		return showLegend;
	}

	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}

	public boolean isAddInfoInLegend() {
		return addInfoInLegend;
	}

	public void setAddInfoInLegend(boolean addInfoInLegend) {
		this.addInfoInLegend = addInfoInLegend;
	}

	public boolean isShowConfidenceInterval() {
		return showConfidenceInterval;
	}

	public void setShowConfidenceInterval(boolean showConfidenceInterval) {
		this.showConfidenceInterval = showConfidenceInterval;
	}

	public Map<String, Color> getColors() {
		return colors;
	}

	public void setColors(Map<String, Color> colors) {
		this.colors = colors;
	}

	public Map<String, Shape> getShapes() {
		return shapes;
	}

	public void setShapes(Map<String, Shape> shapes) {
		this.shapes = shapes;
	}

	public Map<String, List<Color>> getColorLists() {
		return colorLists;
	}

	public void setColorLists(Map<String, List<Color>> colorLists) {
		this.colorLists = colorLists;
	}

	public Map<String, List<Shape>> getShapeLists() {
		return shapeLists;
	}

	public void setShapeLists(Map<String, List<Shape>> shapeLists) {
		this.shapeLists = shapeLists;
	}

	private void plotDataSet(XYPlot plot, Plotable plotable, String id,
			Color defaultColor, Shape defaultShape) {
		double[][] points = plotable.getPoints(paramX, paramY, unitX, unitY,
				transformY);

		if (points != null) {
			DefaultXYDataset dataset = new DefaultXYDataset();
			XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(
					drawLines, true);

			renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator());

			if (addInfoInLegend) {
				dataset.addSeries(longLegend.get(id), points);
			} else {
				dataset.addSeries(shortLegend.get(id), points);
			}

			if (colors.containsKey(id)) {
				renderer.setSeriesPaint(0, colors.get(id));
			} else {
				renderer.setSeriesPaint(0, defaultColor);
			}

			if (shapes.containsKey(id)) {
				renderer.setSeriesShape(0, shapes.get(id));
			} else {
				renderer.setSeriesShape(0, defaultShape);
			}

			int i;

			if (plot.getDataset(0) == null) {
				i = 0;
			} else {
				i = plot.getDatasetCount();
			}

			plot.setDataset(i, dataset);
			plot.setRenderer(i, renderer);
		}
	}

	private void plotDataSetStrict(XYPlot plot, Plotable plotable, String id) {
		List<Color> colorList = colorLists.get(id);
		List<Shape> shapeList = shapeLists.get(id);
		int index = 0;

		for (Map<String, Integer> choiceMap : plotable.getAllChoices()) {
			String addLegend = "";

			for (String arg : choiceMap.keySet()) {
				if (!arg.equals(paramX)) {
					addLegend += " ("
							+ arg
							+ "="
							+ plotable.getFunctionArguments().get(arg)
									.get(choiceMap.get(arg)) + ")";
				}
			}

			double[][] dataPoints = plotable.getPoints(paramX, paramY, unitX,
					unitY, transformY, choiceMap);

			if (dataPoints != null) {
				DefaultXYDataset dataSet = new DefaultXYDataset();
				XYLineAndShapeRenderer dataRenderer = new XYLineAndShapeRenderer(
						drawLines, true);

				dataRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());

				if (addInfoInLegend) {
					dataSet.addSeries(longLegend.get(id) + addLegend
							+ " (Data)", dataPoints);
				} else {
					dataSet.addSeries(shortLegend.get(id) + addLegend
							+ " (Data)", dataPoints);
				}

				dataRenderer.setSeriesPaint(0, colorList.get(index));
				dataRenderer.setSeriesShape(0, shapeList.get(index));

				int i;

				if (plot.getDataset(0) == null) {
					i = 0;
				} else {
					i = plot.getDatasetCount();
				}

				plot.setDataset(i, dataSet);
				plot.setRenderer(i, dataRenderer);
			}

			index++;
		}
	}

	private void plotFunction(XYPlot plot, Plotable plotable, String id,
			Color defaultColor, Shape defaultShape, double minX, double maxX) {
		double[][] points = plotable.getFunctionPoints(paramX, paramY, unitX,
				unitY, transformY, minX, maxX, Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		double[][] functionErrors = null;

		if (showConfidenceInterval) {
			functionErrors = plotable.getFunctionErrors(paramX, paramY, unitX,
					unitY, transformY, minX, maxX, Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY);
		}

		if (points != null) {
			int i;

			if (plot.getDataset(0) == null) {
				i = 0;
			} else {
				i = plot.getDatasetCount();
			}

			if (functionErrors != null) {
				YIntervalSeriesCollection functionDataset = new YIntervalSeriesCollection();
				DeviationRenderer functionRenderer = new DeviationRenderer(
						true, false);
				YIntervalSeries series;
				int n = points[0].length;

				if (addInfoInLegend) {
					series = new YIntervalSeries(longLegend.get(id));
				} else {
					series = new YIntervalSeries(shortLegend.get(id));
				}

				for (int j = 0; j < n; j++) {
					double error = Double.isNaN(functionErrors[1][j]) ? 0.0
							: functionErrors[1][j];

					series.add(points[0][j], points[1][j],
							points[1][j] - error, points[1][j] + error);
				}

				functionDataset.addSeries(series);

				if (colors.containsKey(id)) {
					functionRenderer.setSeriesPaint(0, colors.get(id));
					functionRenderer.setSeriesFillPaint(0, colors.get(id));
				} else {
					functionRenderer.setSeriesPaint(0, defaultColor);
					functionRenderer.setSeriesFillPaint(0, defaultColor);
				}

				if (shapes.containsKey(id)) {
					functionRenderer.setSeriesShape(0, shapes.get(id));
				} else {
					functionRenderer.setSeriesShape(0, defaultShape);
				}

				plot.setDataset(i, functionDataset);
				plot.setRenderer(i, functionRenderer);
			} else {
				DefaultXYDataset functionDataset = new DefaultXYDataset();
				XYLineAndShapeRenderer functionRenderer = new XYLineAndShapeRenderer(
						true, false);

				if (addInfoInLegend) {
					functionDataset.addSeries(longLegend.get(id), points);
				} else {
					functionDataset.addSeries(shortLegend.get(id), points);
				}

				functionRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());

				if (colors.containsKey(id)) {
					functionRenderer.setSeriesPaint(0, colors.get(id));
				} else {
					functionRenderer.setSeriesPaint(0, defaultColor);
				}

				if (shapes.containsKey(id)) {
					functionRenderer.setSeriesShape(0, shapes.get(id));
				} else {
					functionRenderer.setSeriesShape(0, defaultShape);
				}

				plot.setDataset(i, functionDataset);
				plot.setRenderer(i, functionRenderer);
			}
		}
	}

	private void plotFunctionSample(XYPlot plot, Plotable plotable, String id,
			Color defaultColor, Shape defaultShape, double minX, double maxX) {
		double[][] functionPoints = plotable.getFunctionPoints(paramX, paramY,
				unitX, unitY, transformY, minX, maxX, Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		double[][] samplePoints = plotable.getFunctionSamplePoints(paramX,
				paramY, unitX, unitY, transformY, minX, maxX,
				Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		double[][] functionErrors = null;

		if (showConfidenceInterval) {
			functionErrors = plotable.getFunctionErrors(paramX, paramY, unitX,
					unitY, transformY, minX, maxX, Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY);
		}

		if (functionPoints != null) {
			int i;

			if (plot.getDataset(0) == null) {
				i = 0;
			} else {
				i = plot.getDatasetCount();
			}

			if (functionErrors != null) {
				YIntervalSeriesCollection functionDataset = new YIntervalSeriesCollection();
				DeviationRenderer functionRenderer = new DeviationRenderer(
						true, false);
				YIntervalSeries series;
				int n = functionPoints[0].length;

				if (addInfoInLegend) {
					series = new YIntervalSeries(longLegend.get(id));
				} else {
					series = new YIntervalSeries(shortLegend.get(id));
				}

				for (int j = 0; j < n; j++) {
					double error = Double.isNaN(functionErrors[1][j]) ? 0.0
							: functionErrors[1][j];

					series.add(functionPoints[0][j], functionPoints[1][j],
							functionPoints[1][j] - error, functionPoints[1][j]
									+ error);
				}

				functionDataset.addSeries(series);

				if (colors.containsKey(id)) {
					functionRenderer.setSeriesPaint(0, colors.get(id));
					functionRenderer.setSeriesFillPaint(0, colors.get(id));
				} else {
					functionRenderer.setSeriesPaint(0, defaultColor);
					functionRenderer.setSeriesFillPaint(0, defaultColor);
				}

				if (shapes.containsKey(id)) {
					functionRenderer.setSeriesShape(0, shapes.get(id));
				} else {
					functionRenderer.setSeriesShape(0, defaultShape);
				}

				plot.setDataset(i, functionDataset);
				plot.setRenderer(i, functionRenderer);
			} else {
				DefaultXYDataset functionDataset = new DefaultXYDataset();
				XYLineAndShapeRenderer functionRenderer = new XYLineAndShapeRenderer(
						true, false);

				if (addInfoInLegend) {
					functionDataset.addSeries(longLegend.get(id),
							functionPoints);
				} else {
					functionDataset.addSeries(shortLegend.get(id),
							functionPoints);
				}

				functionRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());

				if (colors.containsKey(id)) {
					functionRenderer.setSeriesPaint(0, colors.get(id));
				} else {
					functionRenderer.setSeriesPaint(0, defaultColor);
				}

				if (shapes.containsKey(id)) {
					functionRenderer.setSeriesShape(0, shapes.get(id));
				} else {
					functionRenderer.setSeriesShape(0, defaultShape);
				}

				plot.setDataset(i, functionDataset);
				plot.setRenderer(i, functionRenderer);
			}
		}

		if (samplePoints != null) {
			DefaultXYDataset sampleDataset = new DefaultXYDataset();
			XYLineAndShapeRenderer sampleRenderer = new XYLineAndShapeRenderer(
					false, true);

			sampleRenderer
					.setBaseToolTipGenerator(new StandardXYToolTipGenerator());

			if (addInfoInLegend) {
				sampleDataset.addSeries(longLegend.get(id), samplePoints);
			} else {
				sampleDataset.addSeries(shortLegend.get(id), samplePoints);
			}

			if (colors.containsKey(id)) {
				sampleRenderer.setSeriesPaint(0, colors.get(id));
			} else {
				sampleRenderer.setSeriesPaint(0, defaultColor);
			}

			if (shapes.containsKey(id)) {
				sampleRenderer.setSeriesShape(0, shapes.get(id));
			} else {
				sampleRenderer.setSeriesShape(0, defaultShape);
			}

			int i;

			if (plot.getDataset(0) == null) {
				i = 0;
			} else {
				i = plot.getDatasetCount();
			}

			plot.setDataset(i, sampleDataset);
			plot.setRenderer(i, sampleRenderer);
		}
	}

	private void plotBoth(XYPlot plot, Plotable plotable, String id,
			Color defaultColor, Shape defaultShape, double minX, double maxX) {
		double[][] modelPoints = plotable.getFunctionPoints(paramX, paramY,
				unitX, unitY, transformY, minX, maxX, Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		double[][] dataPoints = plotable.getPoints(paramX, paramY, unitX,
				unitY, transformY);
		double[][] functionErrors = null;

		if (showConfidenceInterval) {
			functionErrors = plotable.getFunctionErrors(paramX, paramY, unitX,
					unitY, transformY, minX, maxX, Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY);
		}

		if (modelPoints != null) {
			int i;

			if (plot.getDataset(0) == null) {
				i = 0;
			} else {
				i = plot.getDatasetCount();
			}

			if (functionErrors != null) {
				YIntervalSeriesCollection functionDataset = new YIntervalSeriesCollection();
				DeviationRenderer functionRenderer = new DeviationRenderer(
						true, false);
				YIntervalSeries series;
				int n = modelPoints[0].length;

				if (addInfoInLegend) {
					series = new YIntervalSeries(longLegend.get(id));
				} else {
					series = new YIntervalSeries(shortLegend.get(id));
				}

				for (int j = 0; j < n; j++) {
					double error = Double.isNaN(functionErrors[1][j]) ? 0.0
							: functionErrors[1][j];

					series.add(modelPoints[0][j], modelPoints[1][j],
							modelPoints[1][j] - error, modelPoints[1][j]
									+ error);
				}

				functionDataset.addSeries(series);

				if (colors.containsKey(id)) {
					functionRenderer.setSeriesPaint(0, colors.get(id));
					functionRenderer.setSeriesFillPaint(0, colors.get(id));
				} else {
					functionRenderer.setSeriesPaint(0, defaultColor);
					functionRenderer.setSeriesFillPaint(0, defaultColor);
				}

				if (shapes.containsKey(id)) {
					functionRenderer.setSeriesShape(0, shapes.get(id));
				} else {
					functionRenderer.setSeriesShape(0, defaultShape);
				}

				plot.setDataset(i, functionDataset);
				plot.setRenderer(i, functionRenderer);
			} else {
				DefaultXYDataset functionDataset = new DefaultXYDataset();
				XYLineAndShapeRenderer functionRenderer = new XYLineAndShapeRenderer(
						true, false);

				if (addInfoInLegend) {
					functionDataset.addSeries(longLegend.get(id), modelPoints);
				} else {
					functionDataset.addSeries(shortLegend.get(id), modelPoints);
				}

				functionRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());

				if (colors.containsKey(id)) {
					functionRenderer.setSeriesPaint(0, colors.get(id));
				} else {
					functionRenderer.setSeriesPaint(0, defaultColor);
				}

				if (shapes.containsKey(id)) {
					functionRenderer.setSeriesShape(0, shapes.get(id));
				} else {
					functionRenderer.setSeriesShape(0, defaultShape);
				}

				plot.setDataset(i, functionDataset);
				plot.setRenderer(i, functionRenderer);
			}
		}

		if (dataPoints != null) {
			DefaultXYDataset dataSet = new DefaultXYDataset();
			XYLineAndShapeRenderer dataRenderer = new XYLineAndShapeRenderer(
					drawLines, true);

			dataRenderer
					.setBaseToolTipGenerator(new StandardXYToolTipGenerator());

			if (addInfoInLegend) {
				dataSet.addSeries(longLegend.get(id) + " (Data)", dataPoints);
			} else {
				dataSet.addSeries(shortLegend.get(id) + " (Data)", dataPoints);
			}

			if (colors.containsKey(id)) {
				dataRenderer.setSeriesPaint(0, colors.get(id));
			} else {
				dataRenderer.setSeriesPaint(0, defaultColor);
			}

			if (shapes.containsKey(id)) {
				dataRenderer.setSeriesShape(0, shapes.get(id));
			} else {
				dataRenderer.setSeriesShape(0, defaultShape);
			}

			int i;

			if (plot.getDataset(0) == null) {
				i = 0;
			} else {
				i = plot.getDatasetCount();
			}

			plot.setDataset(i, dataSet);
			plot.setRenderer(i, dataRenderer);
		}
	}

	private void plotBothStrict(XYPlot plot, Plotable plotable, String id,
			double minX, double maxX) {
		List<Color> colorList = colorLists.get(id);
		List<Shape> shapeList = shapeLists.get(id);
		ColorAndShapeCreator creator = new ColorAndShapeCreator(
				plotable.getNumberOfCombinations());
		int index = 0;

		if (colorList == null) {
			colorList = creator.getColorList();
		}

		if (shapeList == null) {
			shapeList = creator.getShapeList();
		}

		for (Map<String, Integer> choiceMap : plotable.getAllChoices()) {
			String addLegend = "";

			for (String arg : choiceMap.keySet()) {
				if (!arg.equals(paramX)) {
					addLegend += " ("
							+ arg
							+ "="
							+ plotable.getFunctionArguments().get(arg)
									.get(choiceMap.get(arg)) + ")";
				}
			}

			double[][] modelPoints = plotable.getFunctionPoints(paramX, paramY,
					unitX, unitY, transformY, minX, maxX,
					Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
					choiceMap);
			double[][] dataPoints = plotable.getPoints(paramX, paramY, unitX,
					unitY, transformY, choiceMap);

			if (modelPoints != null) {
				DefaultXYDataset modelSet = new DefaultXYDataset();
				XYLineAndShapeRenderer modelRenderer = new XYLineAndShapeRenderer(
						true, false);

				modelRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());

				if (addInfoInLegend) {
					modelSet.addSeries(longLegend.get(id) + addLegend
							+ " (Model)", modelPoints);
				} else {
					modelSet.addSeries(shortLegend.get(id) + addLegend
							+ " (Model)", modelPoints);
				}

				modelRenderer.setSeriesPaint(0, colorList.get(index));
				modelRenderer.setSeriesShape(0, shapeList.get(index));

				int i;

				if (plot.getDataset(0) == null) {
					i = 0;
				} else {
					i = plot.getDatasetCount();
				}

				plot.setDataset(i, modelSet);
				plot.setRenderer(i, modelRenderer);
			}

			if (dataPoints != null) {
				DefaultXYDataset dataSet = new DefaultXYDataset();
				XYLineAndShapeRenderer dataRenderer = new XYLineAndShapeRenderer(
						drawLines, true);

				dataRenderer
						.setBaseToolTipGenerator(new StandardXYToolTipGenerator());

				if (addInfoInLegend) {
					dataSet.addSeries(longLegend.get(id) + addLegend
							+ " (Data)", dataPoints);
				} else {
					dataSet.addSeries(shortLegend.get(id) + addLegend
							+ " (Data)", dataPoints);
				}

				dataRenderer.setSeriesPaint(0, colorList.get(index));
				dataRenderer.setSeriesShape(0, shapeList.get(index));

				int i;

				if (plot.getDataset(0) == null) {
					i = 0;
				} else {
					i = plot.getDatasetCount();
				}

				plot.setDataset(i, dataSet);
				plot.setRenderer(i, dataRenderer);
			}

			index++;
		}
	}

	private class DataAndModelChartSaveAsItem extends JMenuItem implements
			ActionListener {

		private static final long serialVersionUID = 1L;

		public DataAndModelChartSaveAsItem() {
			super("Save as... (SVG)");

			addActionListener(this);
		}

		private void fireSaveAsButtonClicked(String fileName) {
			ChartCreator chartPanel = ChartCreator.this;

			ChartUtilities.saveChartAs(chartPanel.getChart(), fileName,
					chartPanel.getWidth(), chartPanel.getHeight());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			FileFilter svgFilter = new FileFilter() {

				@Override
				public String getDescription() {
					return "SVG Vector Graphic (*.svg)";
				}

				@Override
				public boolean accept(File f) {
					return f.isDirectory()
							|| f.getName().toLowerCase().endsWith(".svg");
				}
			};

			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.addChoosableFileFilter(svgFilter);

			if (fileChooser.showSaveDialog(ChartCreator.this) == JFileChooser.APPROVE_OPTION) {
				String fileName = fileChooser.getSelectedFile().getName();
				String path = fileChooser.getSelectedFile().getAbsolutePath();

				if (fileName.toLowerCase().endsWith(".svg")) {
					fireSaveAsButtonClicked(path);
				} else {
					fireSaveAsButtonClicked(path + ".svg");
				}
			}
		}

	}

}
