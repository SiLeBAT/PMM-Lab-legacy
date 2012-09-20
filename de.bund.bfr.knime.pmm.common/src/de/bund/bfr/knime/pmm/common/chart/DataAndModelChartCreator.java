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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.xy.DefaultXYDataset;

import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class DataAndModelChartCreator extends ChartPanel {

	private static final long serialVersionUID = 1L;

	private Map<String, Plotable> plotables;
	private Map<String, String> shortLegend;
	private Map<String, String> longLegend;
	private Map<String, Color> colors;
	private Map<String, Shape> shapes;

	private String paramX;
	private String paramY;
	private String transformY;
	private boolean useManualRange;
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;
	private boolean drawLines;
	private boolean showLegend;
	private boolean addInfoInLegend;

	public DataAndModelChartCreator(Map<String, Plotable> plotables,
			Map<String, String> shortLegend, Map<String, String> longLegend) {
		super(new JFreeChart(new XYPlot()));
		getPopupMenu().insert(new DataAndModelChartSaveAsItem(), 4);
		this.plotables = plotables;
		this.shortLegend = shortLegend;
		this.longLegend = longLegend;
		colors = new HashMap<String, Color>();
		shapes = new HashMap<String, Shape>();
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

		String labelY;

		if (transformY.equals(ChartConstants.NO_TRANSFORM)) {
			labelY = paramY;
		} else {
			labelY = transformY + "(" + paramY + ")";
		}

		NumberAxis xAxis = new NumberAxis(paramX);
		NumberAxis yAxis = new NumberAxis(labelY);
		XYPlot plot = new XYPlot(null, xAxis, yAxis, null);
		boolean containsDataPoints = false;
		double usedMinX = Double.POSITIVE_INFINITY;
		double usedMaxX = Double.NEGATIVE_INFINITY;
		int index = 0;
		int colorShapeIndex = 0;
		ColorAndShapeCreator colorAndShapeCreator = new ColorAndShapeCreator(
				idsToPaint.size());

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable.getType() == Plotable.DATASET
					|| plotable.getType() == Plotable.BOTH
					|| plotable.getType() == Plotable.BOTH_STRICT) {
				double[][] points = plotable.getPoints(paramX, paramY,
						transformY);

				if (points != null) {
					for (int i = 0; i < points[0].length; i++) {
						containsDataPoints = true;
						usedMinX = Math.min(usedMinX, points[0][i]);
						usedMaxX = Math.max(usedMaxX, points[0][i]);
					}
				}
			}
		}

		if (!containsDataPoints) {
			usedMinX = 0.0;
			usedMaxX = 100.0;
		}

		if (paramX.equals(TimeSeriesSchema.ATT_TIME)
				|| paramX.equals(TimeSeriesSchema.ATT_LOGC)) {
			usedMinX = Math.min(usedMinX, 0.0);
			xAxis.setAutoRangeIncludesZero(true);
		} else {
			xAxis.setAutoRangeIncludesZero(false);
		}

		if (paramY.equals(TimeSeriesSchema.ATT_TIME)
				|| paramY.equals(TimeSeriesSchema.ATT_LOGC)) {
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

			if (plotable.getType() == Plotable.DATASET) {
				DefaultXYDataset dataset = new DefaultXYDataset();
				XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(
						drawLines, true);
				double[][] points = plotable.getPoints(paramX, paramY,
						transformY);

				if (points != null) {
					if (addInfoInLegend) {
						dataset.addSeries(longLegend.get(id), points);
					} else {
						dataset.addSeries(shortLegend.get(id), points);
					}

					if (colors.containsKey(id)) {
						renderer.setSeriesPaint(0, colors.get(id));
					} else {
						renderer.setSeriesPaint(0, colorAndShapeCreator
								.getColorList().get(colorShapeIndex));
					}

					if (shapes.containsKey(id)) {
						renderer.setSeriesShape(0, shapes.get(id));
					} else {
						renderer.setSeriesShape(0, colorAndShapeCreator
								.getShapeList().get(colorShapeIndex));
					}

					plot.setDataset(index, dataset);
					plot.setRenderer(index, renderer);
					index++;
				}

				colorShapeIndex++;
			}
		}

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable.getType() == Plotable.FUNCTION) {
				DefaultXYDataset dataset = new DefaultXYDataset();
				XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(
						true, false);
				double[][] points = plotable.getFunctionPoints(paramX, paramY,
						transformY, usedMinX, usedMaxX,
						Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

				if (points != null) {
					if (addInfoInLegend) {
						dataset.addSeries(longLegend.get(id), points);
					} else {
						dataset.addSeries(shortLegend.get(id), points);
					}

					if (colors.containsKey(id)) {
						renderer.setSeriesPaint(0, colors.get(id));
					} else {
						renderer.setSeriesPaint(0, colorAndShapeCreator
								.getColorList().get(colorShapeIndex));
					}

					if (shapes.containsKey(id)) {
						renderer.setSeriesShape(0, shapes.get(id));
					} else {
						renderer.setSeriesShape(0, colorAndShapeCreator
								.getShapeList().get(colorShapeIndex));
					}

					plot.setDataset(index, dataset);
					plot.setRenderer(index, renderer);
					index++;
				}

				colorShapeIndex++;
			}
		}

		for (String id : idsToPaint) {
			Plotable plotable = plotables.get(id);

			if (plotable.getType() == Plotable.BOTH
					|| plotable.getType() == Plotable.BOTH_STRICT) {
				double[][] modelPoints = plotable.getFunctionPoints(paramX,
						paramY, transformY, usedMinX, usedMaxX,
						Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
				double[][] dataPoints = plotable.getPoints(paramX, paramY,
						transformY);

				if (modelPoints != null) {
					DefaultXYDataset modelSet = new DefaultXYDataset();
					XYLineAndShapeRenderer modelRenderer = new XYLineAndShapeRenderer(
							true, false);

					if (addInfoInLegend) {
						modelSet.addSeries(longLegend.get(id) + " (Model)",
								modelPoints);
					} else {
						modelSet.addSeries(shortLegend.get(id) + " (Model)",
								modelPoints);
					}

					if (colors.containsKey(id)) {
						modelRenderer.setSeriesPaint(0, colors.get(id));
					} else {
						modelRenderer.setSeriesPaint(0, colorAndShapeCreator
								.getColorList().get(colorShapeIndex));
					}

					if (shapes.containsKey(id)) {
						modelRenderer.setSeriesShape(0, shapes.get(id));
					} else {
						modelRenderer.setSeriesShape(0, colorAndShapeCreator
								.getShapeList().get(colorShapeIndex));
					}

					plot.setDataset(index, modelSet);
					plot.setRenderer(index, modelRenderer);
					index++;
				}

				if (dataPoints != null) {
					DefaultXYDataset dataSet = new DefaultXYDataset();
					XYLineAndShapeRenderer dataRenderer = new XYLineAndShapeRenderer(
							drawLines, true);

					if (addInfoInLegend) {
						dataSet.addSeries(longLegend.get(id) + " (Data)",
								dataPoints);
					} else {
						dataSet.addSeries(shortLegend.get(id) + " (Data)",
								dataPoints);
					}

					if (colors.containsKey(id)) {
						dataRenderer.setSeriesPaint(0, colors.get(id));
					} else {
						dataRenderer.setSeriesPaint(0, colorAndShapeCreator
								.getColorList().get(colorShapeIndex));
					}

					if (shapes.containsKey(id)) {
						dataRenderer.setSeriesShape(0, shapes.get(id));
					} else {
						dataRenderer.setSeriesShape(0, colorAndShapeCreator
								.getShapeList().get(colorShapeIndex));
					}

					plot.setDataset(index, dataSet);
					plot.setRenderer(index, dataRenderer);
					index++;
				}

				colorShapeIndex++;
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

	public String getDataY() {
		return paramY;
	}

	public void setParamY(String paramY) {
		this.paramY = paramY;
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

	private class DataAndModelChartSaveAsItem extends JMenuItem implements
			ActionListener {

		private static final long serialVersionUID = 1L;

		public DataAndModelChartSaveAsItem() {
			super("Save as... (SVG)");

			addActionListener(this);
		}

		private void fireSaveAsButtonClicked(String fileName) {
			DataAndModelChartCreator chartPanel = DataAndModelChartCreator.this;

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

			if (fileChooser.showSaveDialog(DataAndModelChartCreator.this) == JFileChooser.APPROVE_OPTION) {
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
