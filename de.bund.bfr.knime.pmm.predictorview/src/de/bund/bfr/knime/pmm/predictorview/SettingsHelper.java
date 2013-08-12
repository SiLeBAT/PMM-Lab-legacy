/*******************************************************************************
 * PMM-Lab © 2013, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2013, Federal Institute for Risk Assessment (BfR), Germany
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
package de.bund.bfr.knime.pmm.predictorview;

import java.awt.Color;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;

public class SettingsHelper {

	protected static final String CFG_SELECTEDIDS = "SelectedIDs";
	protected static final String CFG_PARAMXVALUES = "ParamXValues";
	protected static final String CFG_TIMEVALUES = "TimeValues";
	protected static final String CFG_COLORS = "Colors";
	protected static final String CFG_SHAPES = "Shapes";
	protected static final String CFG_MANUALRANGE = "ManualRange";
	protected static final String CFG_MINX = "MinX";
	protected static final String CFG_MAXX = "MaxX";
	protected static final String CFG_MINY = "MinY";
	protected static final String CFG_MAXY = "MaxY";
	protected static final String CFG_DRAWLINES = "DrawLines";
	protected static final String CFG_SHOWLEGEND = "ShowLegend";
	protected static final String CFG_ADDLEGENDINFO = "AddLegendInfo";
	protected static final String CFG_DISPLAYHIGHLIGHTED = "DisplayHighlighted";
	protected static final String CFG_EXPORTASSVG = "ExportAsSvg";
	protected static final String CFG_UNITX = "UnitX";
	protected static final String CFG_UNITY = "UnitY";
	protected static final String CFG_TRANSFORMY = "TransformY";
	protected static final String CFG_STANDARDVISIBLECOLUMNS = "StandardVisibleColumns";
	protected static final String CFG_VISIBLECOLUMNS = "VisibleColumns";
	protected static final String CFG_MODELFILTER = "ModelFilter";
	protected static final String CFG_DATAFILTER = "DataFilter";
	protected static final String CFG_FITTEDFILTER = "FittedFilter";
	protected static final String CFGKEY_CONCENTRATIONPARAMETERS = "ConcentrationParameters";

	protected static final boolean DEFAULT_MANUALRANGE = false;
	protected static final double DEFAULT_MINX = 0.0;
	protected static final double DEFAULT_MAXX = 100.0;
	protected static final double DEFAULT_MINY = 0.0;
	protected static final double DEFAULT_MAXY = 10.0;
	protected static final boolean DEFAULT_DRAWLINES = false;
	protected static final boolean DEFAULT_SHOWLEGEND = true;
	protected static final boolean DEFAULT_ADDLEGENDINFO = false;
	protected static final boolean DEFAULT_DISPLAYHIGHLIGHTED = false;
	protected static final boolean DEFAULT_EXPORTASSVG = false;
	protected static final String DEFAULT_TRANSFORMY = ChartConstants.NO_TRANSFORM;
	protected static final boolean DEFAULT_STANDARDVISIBLECOLUMNS = true;

	private List<String> selectedIDs;
	private Map<String, Double> paramXValues;
	private List<Double> timeValues;
	private Map<String, Color> colors;
	private Map<String, Shape> shapes;
	private boolean manualRange;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private boolean drawLines;
	private boolean showLegend;
	private boolean addLegendInfo;
	private boolean displayHighlighted;
	private boolean exportAsSvg;
	private String unitX;
	private String unitY;
	private String transformY;
	private boolean standardVisibleColumns;
	private List<String> visibleColumns;
	private String modelFilter;
	private String dataFilter;
	private String fittedFilter;
	private Map<String, String> concentrationParameters;

	public SettingsHelper() {
		selectedIDs = new ArrayList<>();
		paramXValues = new LinkedHashMap<>();
		timeValues = new ArrayList<>();
		colors = new LinkedHashMap<>();
		shapes = new LinkedHashMap<>();
		manualRange = DEFAULT_MANUALRANGE;
		minX = DEFAULT_MINX;
		maxX = DEFAULT_MAXX;
		minY = DEFAULT_MINY;
		maxY = DEFAULT_MAXY;
		drawLines = DEFAULT_DRAWLINES;
		showLegend = DEFAULT_SHOWLEGEND;
		addLegendInfo = DEFAULT_ADDLEGENDINFO;
		displayHighlighted = DEFAULT_DISPLAYHIGHLIGHTED;
		exportAsSvg = DEFAULT_EXPORTASSVG;
		unitX = null;
		unitY = null;
		transformY = DEFAULT_TRANSFORMY;
		standardVisibleColumns = DEFAULT_STANDARDVISIBLECOLUMNS;
		visibleColumns = new ArrayList<>();
		modelFilter = null;
		dataFilter = null;
		fittedFilter = null;
		concentrationParameters = new LinkedHashMap<>();
	}

	public void loadSettings(NodeSettingsRO settings) {
		try {
			selectedIDs = XmlConverter.xmlToObject(
					settings.getString(CFG_SELECTEDIDS),
					new ArrayList<String>());
		} catch (InvalidSettingsException e) {
			selectedIDs = new ArrayList<String>();
		}

		try {
			paramXValues = XmlConverter.xmlToObject(
					settings.getString(CFG_PARAMXVALUES),
					new LinkedHashMap<String, Double>());
		} catch (InvalidSettingsException e) {
			paramXValues = new LinkedHashMap<>();
		}

		try {
			timeValues = XmlConverter
					.xmlToObject(settings.getString(CFG_TIMEVALUES),
							new ArrayList<Double>());
		} catch (InvalidSettingsException e1) {
			timeValues = new ArrayList<>();
		}

		try {
			colors = XmlConverter.xmlToColorMap(settings.getString(CFG_COLORS));
		} catch (InvalidSettingsException e) {
			colors = new LinkedHashMap<>();
		}

		try {
			shapes = XmlConverter.xmlToShapeMap(settings.getString(CFG_SHAPES));
		} catch (InvalidSettingsException e) {
			shapes = new LinkedHashMap<>();
		}

		try {
			manualRange = settings.getBoolean(CFG_MANUALRANGE);
		} catch (InvalidSettingsException e) {
			manualRange = DEFAULT_MANUALRANGE;
		}

		try {
			minX = settings.getDouble(CFG_MINX);
		} catch (InvalidSettingsException e) {
			minX = DEFAULT_MINX;
		}

		try {
			maxX = settings.getDouble(CFG_MAXX);
		} catch (InvalidSettingsException e) {
			maxX = DEFAULT_MAXX;
		}

		try {
			minY = settings.getDouble(CFG_MINY);
		} catch (InvalidSettingsException e) {
			minY = DEFAULT_MINY;
		}

		try {
			maxY = settings.getDouble(CFG_MAXY);
		} catch (InvalidSettingsException e) {
			maxY = DEFAULT_MAXY;
		}

		try {
			drawLines = settings.getBoolean(CFG_DRAWLINES);
		} catch (InvalidSettingsException e) {
			drawLines = DEFAULT_DRAWLINES;
		}

		try {
			showLegend = settings.getBoolean(CFG_SHOWLEGEND);
		} catch (InvalidSettingsException e) {
			showLegend = DEFAULT_SHOWLEGEND;
		}

		try {
			addLegendInfo = settings.getBoolean(CFG_ADDLEGENDINFO);
		} catch (InvalidSettingsException e) {
			addLegendInfo = DEFAULT_ADDLEGENDINFO;
		}

		try {
			displayHighlighted = settings.getBoolean(CFG_DISPLAYHIGHLIGHTED);
		} catch (InvalidSettingsException e) {
			displayHighlighted = DEFAULT_DISPLAYHIGHLIGHTED;
		}

		try {
			exportAsSvg = settings.getBoolean(CFG_EXPORTASSVG);
		} catch (InvalidSettingsException e) {
			exportAsSvg = DEFAULT_EXPORTASSVG;
		}

		try {
			unitX = settings.getString(CFG_UNITX);
		} catch (InvalidSettingsException e) {
			unitX = null;
		}

		try {
			unitY = settings.getString(CFG_UNITY);
		} catch (InvalidSettingsException e) {
			unitY = null;
		}

		try {
			transformY = settings.getString(CFG_TRANSFORMY);
		} catch (InvalidSettingsException e) {
			transformY = DEFAULT_TRANSFORMY;
		}

		try {
			standardVisibleColumns = settings
					.getBoolean(CFG_STANDARDVISIBLECOLUMNS);
		} catch (InvalidSettingsException e) {
			standardVisibleColumns = DEFAULT_STANDARDVISIBLECOLUMNS;
		}

		try {
			visibleColumns = XmlConverter.xmlToObject(
					settings.getString(CFG_VISIBLECOLUMNS),
					new ArrayList<String>());
		} catch (InvalidSettingsException e) {
			visibleColumns = new ArrayList<>();
		}

		try {
			modelFilter = settings.getString(CFG_MODELFILTER);
		} catch (InvalidSettingsException e) {
			modelFilter = null;
		}

		try {
			dataFilter = settings.getString(CFG_DATAFILTER);
		} catch (InvalidSettingsException e) {
			dataFilter = null;
		}

		try {
			fittedFilter = settings.getString(CFG_FITTEDFILTER);
		} catch (InvalidSettingsException e) {
			fittedFilter = null;
		}

		try {
			concentrationParameters = XmlConverter.xmlToObject(
					settings.getString(CFGKEY_CONCENTRATIONPARAMETERS),
					new LinkedHashMap<String, String>());
		} catch (InvalidSettingsException e) {
			concentrationParameters = new LinkedHashMap<String, String>();
		}
	}

	public void saveSettings(NodeSettingsWO settings) {
		settings.addString(CFG_SELECTEDIDS,
				XmlConverter.objectToXml(selectedIDs));
		settings.addString(CFG_PARAMXVALUES,
				XmlConverter.objectToXml(paramXValues));
		settings.addString(CFG_TIMEVALUES, XmlConverter.objectToXml(timeValues));
		settings.addString(CFG_COLORS, XmlConverter.colorMapToXml(colors));
		settings.addString(CFG_SHAPES, XmlConverter.shapeMapToXml(shapes));
		settings.addBoolean(CFG_MANUALRANGE, manualRange);
		settings.addDouble(CFG_MINX, minX);
		settings.addDouble(CFG_MAXX, maxX);
		settings.addDouble(CFG_MINY, minY);
		settings.addDouble(CFG_MAXY, maxY);
		settings.addBoolean(CFG_DRAWLINES, drawLines);
		settings.addBoolean(CFG_SHOWLEGEND, showLegend);
		settings.addBoolean(CFG_ADDLEGENDINFO, addLegendInfo);
		settings.addBoolean(CFG_DISPLAYHIGHLIGHTED, displayHighlighted);
		settings.addBoolean(CFG_EXPORTASSVG, exportAsSvg);
		settings.addString(CFG_UNITX, unitX);
		settings.addString(CFG_UNITY, unitY);
		settings.addString(CFG_TRANSFORMY, transformY);
		settings.addBoolean(CFG_STANDARDVISIBLECOLUMNS, standardVisibleColumns);
		settings.addString(CFG_VISIBLECOLUMNS,
				XmlConverter.objectToXml(visibleColumns));
		settings.addString(CFG_MODELFILTER, modelFilter);
		settings.addString(CFG_DATAFILTER, dataFilter);
		settings.addString(CFG_FITTEDFILTER, fittedFilter);
		settings.addString(CFGKEY_CONCENTRATIONPARAMETERS,
				XmlConverter.objectToXml(concentrationParameters));
	}

	public List<String> getSelectedIDs() {
		return selectedIDs;
	}

	public void setSelectedIDs(List<String> selectedIDs) {
		this.selectedIDs = selectedIDs;
	}

	public Map<String, Double> getParamXValues() {
		return paramXValues;
	}

	public void setParamXValues(Map<String, Double> paramXValues) {
		this.paramXValues = paramXValues;
	}

	public List<Double> getTimeValues() {
		return timeValues;
	}

	public void setTimeValues(List<Double> timeValues) {
		this.timeValues = timeValues;
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

	public boolean isManualRange() {
		return manualRange;
	}

	public void setManualRange(boolean manualRange) {
		this.manualRange = manualRange;
	}

	public double getMinX() {
		return minX;
	}

	public void setMinX(double minX) {
		this.minX = minX;
	}

	public double getMaxX() {
		return maxX;
	}

	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	public double getMinY() {
		return minY;
	}

	public void setMinY(double minY) {
		this.minY = minY;
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

	public boolean isAddLegendInfo() {
		return addLegendInfo;
	}

	public void setAddLegendInfo(boolean addLegendInfo) {
		this.addLegendInfo = addLegendInfo;
	}

	public boolean isDisplayHighlighted() {
		return displayHighlighted;
	}

	public void setDisplayHighlighted(boolean displayHighlighted) {
		this.displayHighlighted = displayHighlighted;
	}

	public boolean isExportAsSvg() {
		return exportAsSvg;
	}

	public void setExportAsSvg(boolean exportAsSvg) {
		this.exportAsSvg = exportAsSvg;
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

	public boolean isStandardVisibleColumns() {
		return standardVisibleColumns;
	}

	public void setStandardVisibleColumns(boolean standardVisibleColumns) {
		this.standardVisibleColumns = standardVisibleColumns;
	}

	public List<String> getVisibleColumns() {
		return visibleColumns;
	}

	public void setVisibleColumns(List<String> visibleColumns) {
		this.visibleColumns = visibleColumns;
	}

	public String getModelFilter() {
		return modelFilter;
	}

	public void setModelFilter(String modelFilter) {
		this.modelFilter = modelFilter;
	}

	public String getDataFilter() {
		return dataFilter;
	}

	public void setDataFilter(String dataFilter) {
		this.dataFilter = dataFilter;
	}

	public String getFittedFilter() {
		return fittedFilter;
	}

	public void setFittedFilter(String fittedFilter) {
		this.fittedFilter = fittedFilter;
	}

	public Map<String, String> getConcentrationParameters() {
		return concentrationParameters;
	}

	public void setConcentrationParameters(
			Map<String, String> concentrationParameters) {
		this.concentrationParameters = concentrationParameters;
	}
}
