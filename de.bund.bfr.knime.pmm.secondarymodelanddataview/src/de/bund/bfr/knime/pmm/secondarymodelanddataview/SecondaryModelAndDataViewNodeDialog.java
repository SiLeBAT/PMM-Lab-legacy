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
package de.bund.bfr.knime.pmm.secondarymodelanddataview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.knime.core.data.DataTable;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.chart.ChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartInfoPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

/**
 * <code>NodeDialog</code> for the "SecondaryModelAndDataView" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class SecondaryModelAndDataViewNodeDialog extends
		DataAwareNodeDialogPane implements
		ChartSelectionPanel.SelectionListener, ChartConfigPanel.ConfigListener {

	private TableReader reader;
	private boolean containsData;

	private ChartCreator chartCreator;
	private ChartSelectionPanel selectionPanel;
	private ChartConfigPanel configPanel;
	private ChartInfoPanel infoPanel;

	private String selectedID;
	private String currentParamX;
	private Map<String, Double> paramXValues;
	private Map<String, List<Boolean>> selectedValuesX;
	private Map<String, Color> colors;
	private Map<String, Shape> shapes;
	private Map<String, List<Color>> colorLists;
	private Map<String, List<Shape>> shapeLists;
	private int manualRange;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private int drawLines;
	private int showLegend;
	private int addLegendInfo;
	private int displayHighlighted;
	private int showConfidence;
	private String unitX;
	private String unitY;
	private String transformY;
	private int standardVisibleColumns;
	private List<String> visibleColumns;
	private String fittedFilter;

	/**
	 * New pane for configuring the SecondaryModelAndDataView node.
	 */
	protected SecondaryModelAndDataViewNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		try {
			selectedID = settings
					.getString(SecondaryModelAndDataViewNodeModel.CFG_SELECTEDID);
		} catch (InvalidSettingsException e) {
			selectedID = null;
		}

		try {
			currentParamX = settings
					.getString(SecondaryModelAndDataViewNodeModel.CFG_CURRENTPARAMX);
		} catch (InvalidSettingsException e) {
			currentParamX = null;
		}

		try {
			paramXValues = XmlConverter
					.xmlToDoubleMap(settings
							.getString(SecondaryModelAndDataViewNodeModel.CFG_PARAMXVALUES));
		} catch (InvalidSettingsException e) {
			paramXValues = new LinkedHashMap<>();
		}

		try {
			selectedValuesX = XmlConverter
					.xmlToBoolListMap(settings
							.getString(SecondaryModelAndDataViewNodeModel.CFG_SELECTEDVALUESX));
		} catch (InvalidSettingsException e) {
			selectedValuesX = new LinkedHashMap<>();
		}

		try {
			colors = XmlConverter.xmlToColorMap(settings
					.getString(SecondaryModelAndDataViewNodeModel.CFG_COLORS));
		} catch (InvalidSettingsException e) {
			colors = new LinkedHashMap<>();
		}

		try {
			shapes = XmlConverter.xmlToShapeMap(settings
					.getString(SecondaryModelAndDataViewNodeModel.CFG_SHAPES));
		} catch (InvalidSettingsException e) {
			shapes = new LinkedHashMap<>();
		}

		try {
			colorLists = XmlConverter
					.xmlToColorListMap(settings
							.getString(SecondaryModelAndDataViewNodeModel.CFG_COLORLISTS));
		} catch (InvalidSettingsException e) {
			colorLists = new LinkedHashMap<>();
		}

		try {
			shapeLists = XmlConverter
					.xmlToShapeListMap(settings
							.getString(SecondaryModelAndDataViewNodeModel.CFG_SHAPELISTS));
		} catch (InvalidSettingsException e) {
			shapeLists = new LinkedHashMap<>();
		}

		try {
			manualRange = settings
					.getInt(SecondaryModelAndDataViewNodeModel.CFG_MANUALRANGE);
		} catch (InvalidSettingsException e) {
			manualRange = SecondaryModelAndDataViewNodeModel.DEFAULT_MANUALRANGE;
		}

		try {
			minX = settings
					.getDouble(SecondaryModelAndDataViewNodeModel.CFG_MINX);
		} catch (InvalidSettingsException e) {
			minX = SecondaryModelAndDataViewNodeModel.DEFAULT_MINX;
		}

		try {
			maxX = settings
					.getDouble(SecondaryModelAndDataViewNodeModel.CFG_MAXX);
		} catch (InvalidSettingsException e) {
			maxX = SecondaryModelAndDataViewNodeModel.DEFAULT_MAXX;
		}

		try {
			minY = settings
					.getDouble(SecondaryModelAndDataViewNodeModel.CFG_MINY);
		} catch (InvalidSettingsException e) {
			minY = SecondaryModelAndDataViewNodeModel.DEFAULT_MINY;
		}

		try {
			maxY = settings
					.getDouble(SecondaryModelAndDataViewNodeModel.CFG_MAXY);
		} catch (InvalidSettingsException e) {
			maxY = SecondaryModelAndDataViewNodeModel.DEFAULT_MAXY;
		}

		try {
			drawLines = settings
					.getInt(SecondaryModelAndDataViewNodeModel.CFG_DRAWLINES);
		} catch (InvalidSettingsException e) {
			drawLines = SecondaryModelAndDataViewNodeModel.DEFAULT_DRAWLINES;
		}

		try {
			showLegend = settings
					.getInt(SecondaryModelAndDataViewNodeModel.CFG_SHOWLEGEND);
		} catch (InvalidSettingsException e) {
			showLegend = SecondaryModelAndDataViewNodeModel.DEFAULT_SHOWLEGEND;
		}

		try {
			addLegendInfo = settings
					.getInt(SecondaryModelAndDataViewNodeModel.CFG_ADDLEGENDINFO);
		} catch (InvalidSettingsException e) {
			addLegendInfo = SecondaryModelAndDataViewNodeModel.DEFAULT_ADDLEGENDINFO;
		}

		try {
			displayHighlighted = settings
					.getInt(SecondaryModelAndDataViewNodeModel.CFG_DISPLAYHIGHLIGHTED);
		} catch (InvalidSettingsException e) {
			displayHighlighted = SecondaryModelAndDataViewNodeModel.DEFAULT_DISPLAYHIGHLIGHTED;
		}

		try {
			showConfidence = settings
					.getInt(SecondaryModelAndDataViewNodeModel.CFG_SHOWCONFIDENCE);
		} catch (InvalidSettingsException e) {
			showConfidence = SecondaryModelAndDataViewNodeModel.DEFAULT_SHOWCONFIDENCE;
		}

		try {
			unitX = settings
					.getString(SecondaryModelAndDataViewNodeModel.CFG_UNITX);
		} catch (InvalidSettingsException e) {
			unitX = null;
		}

		try {
			unitY = settings
					.getString(SecondaryModelAndDataViewNodeModel.CFG_UNITY);
		} catch (InvalidSettingsException e) {
			unitY = null;
		}

		try {
			transformY = settings
					.getString(SecondaryModelAndDataViewNodeModel.CFG_TRANSFORMY);
		} catch (InvalidSettingsException e) {
			transformY = SecondaryModelAndDataViewNodeModel.DEFAULT_TRANSFORMY;
		}

		try {
			standardVisibleColumns = settings
					.getInt(SecondaryModelAndDataViewNodeModel.CFG_STANDARDVISIBLECOLUMNS);
		} catch (InvalidSettingsException e) {
			standardVisibleColumns = SecondaryModelAndDataViewNodeModel.DEFAULT_STANDARDVISIBLECOLUMNS;
		}

		try {
			visibleColumns = XmlConverter
					.xmlToStringList(settings
							.getString(SecondaryModelAndDataViewNodeModel.CFG_VISIBLECOLUMNS));
		} catch (InvalidSettingsException e) {
			visibleColumns = new ArrayList<>();
		}

		try {
			fittedFilter = settings
					.getString(SecondaryModelAndDataViewNodeModel.CFG_FITTEDFILTER);
		} catch (InvalidSettingsException e) {
			fittedFilter = null;
		}

		DataTable table = input[0];

		if (SchemaFactory.createDataSchema().conforms(table)) {
			reader = new TableReader(table, true);

			if (Collections.max(reader.getColorCounts()) == 0) {
				reader = new TableReader(table, false);
				containsData = false;
			} else {
				containsData = true;
			}
		} else {
			reader = new TableReader(table, false);
			containsData = false;
		}

		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(createMainComponent());
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		if (!selectionPanel.getSelectedIDs().isEmpty()) {
			settings.addString(
					SecondaryModelAndDataViewNodeModel.CFG_SELECTEDID,
					selectionPanel.getSelectedIDs().get(0));
		} else {
			settings.addString(
					SecondaryModelAndDataViewNodeModel.CFG_SELECTEDID, null);
		}

		settings.addString(
				SecondaryModelAndDataViewNodeModel.CFG_CURRENTPARAMX,
				configPanel.getParamX());
		settings.addString(SecondaryModelAndDataViewNodeModel.CFG_PARAMXVALUES,
				XmlConverter.mapToXml(configPanel.getParamXValues()));
		settings.addString(
				SecondaryModelAndDataViewNodeModel.CFG_SELECTEDVALUESX,
				XmlConverter.mapToXml(configPanel.getSelectedValuesX()));

		if (containsData) {
			settings.addString(SecondaryModelAndDataViewNodeModel.CFG_COLORS,
					null);
			settings.addString(SecondaryModelAndDataViewNodeModel.CFG_SHAPES,
					null);
			settings.addString(
					SecondaryModelAndDataViewNodeModel.CFG_COLORLISTS,
					XmlConverter.colorListMapToXml(selectionPanel
							.getColorLists()));
			settings.addString(
					SecondaryModelAndDataViewNodeModel.CFG_SHAPELISTS,
					XmlConverter.shapeListMapToXml(selectionPanel
							.getShapeLists()));
		} else {
			settings.addString(SecondaryModelAndDataViewNodeModel.CFG_COLORS,
					XmlConverter.colorMapToXml(selectionPanel.getColors()));
			settings.addString(SecondaryModelAndDataViewNodeModel.CFG_SHAPES,
					XmlConverter.shapeMapToXml(selectionPanel.getShapes()));
			settings.addString(
					SecondaryModelAndDataViewNodeModel.CFG_COLORLISTS, null);
			settings.addString(
					SecondaryModelAndDataViewNodeModel.CFG_SHAPELISTS, null);
		}

		if (configPanel.isUseManualRange()) {
			settings.addInt(SecondaryModelAndDataViewNodeModel.CFG_MANUALRANGE,
					1);
		} else {
			settings.addInt(SecondaryModelAndDataViewNodeModel.CFG_MANUALRANGE,
					0);
		}

		settings.addDouble(SecondaryModelAndDataViewNodeModel.CFG_MINX,
				configPanel.getMinX());
		settings.addDouble(SecondaryModelAndDataViewNodeModel.CFG_MAXX,
				configPanel.getMaxX());
		settings.addDouble(SecondaryModelAndDataViewNodeModel.CFG_MINY,
				configPanel.getMinY());
		settings.addDouble(SecondaryModelAndDataViewNodeModel.CFG_MAXY,
				configPanel.getMaxY());

		if (configPanel.isDrawLines()) {
			settings.addInt(SecondaryModelAndDataViewNodeModel.CFG_DRAWLINES, 1);
		} else {
			settings.addInt(SecondaryModelAndDataViewNodeModel.CFG_DRAWLINES, 0);
		}

		if (configPanel.isShowLegend()) {
			settings.addInt(SecondaryModelAndDataViewNodeModel.CFG_SHOWLEGEND,
					1);
		} else {
			settings.addInt(SecondaryModelAndDataViewNodeModel.CFG_SHOWLEGEND,
					0);
		}

		if (configPanel.isAddInfoInLegend()) {
			settings.addInt(
					SecondaryModelAndDataViewNodeModel.CFG_ADDLEGENDINFO, 1);
		} else {
			settings.addInt(
					SecondaryModelAndDataViewNodeModel.CFG_ADDLEGENDINFO, 0);
		}

		if (configPanel.isDisplayFocusedRow()) {
			settings.addInt(
					SecondaryModelAndDataViewNodeModel.CFG_DISPLAYHIGHLIGHTED,
					1);
		} else {
			settings.addInt(
					SecondaryModelAndDataViewNodeModel.CFG_DISPLAYHIGHLIGHTED,
					0);
		}

		if (configPanel.isShowConfidenceInterval()) {
			settings.addInt(
					SecondaryModelAndDataViewNodeModel.CFG_SHOWCONFIDENCE, 1);
		} else {
			settings.addInt(
					SecondaryModelAndDataViewNodeModel.CFG_SHOWCONFIDENCE, 0);
		}

		settings.addString(SecondaryModelAndDataViewNodeModel.CFG_UNITX,
				configPanel.getUnitX());
		settings.addString(SecondaryModelAndDataViewNodeModel.CFG_UNITY,
				configPanel.getUnitY());
		settings.addString(SecondaryModelAndDataViewNodeModel.CFG_TRANSFORMY,
				configPanel.getTransformY());
		settings.addInt(
				SecondaryModelAndDataViewNodeModel.CFG_STANDARDVISIBLECOLUMNS,
				0);
		settings.addString(
				SecondaryModelAndDataViewNodeModel.CFG_VISIBLECOLUMNS,
				XmlConverter.listToXml(selectionPanel.getVisibleColumns()));
		settings.addString(SecondaryModelAndDataViewNodeModel.CFG_FITTEDFILTER,
				selectionPanel.getFilter(ChartConstants.STATUS));
	}

	private JComponent createMainComponent() {
		if (standardVisibleColumns == 1) {
			visibleColumns = reader.getStandardVisibleColumns();
		}

		if (containsData) {
			configPanel = new ChartConfigPanel(
					ChartConfigPanel.PARAMETER_BOXES, true, null);
			selectionPanel = new ChartSelectionPanel(reader.getIds(), true,
					reader.getStringColumns(), reader.getStringColumnValues(),
					reader.getDoubleColumns(), reader.getDoubleColumnValues(),
					visibleColumns, reader.getFilterableStringColumns(), null,
					reader.getColorCounts());
		} else {
			configPanel = new ChartConfigPanel(
					ChartConfigPanel.PARAMETER_FIELDS, true, null);
			selectionPanel = new ChartSelectionPanel(reader.getIds(), true,
					reader.getStringColumns(), reader.getStringColumnValues(),
					reader.getDoubleColumns(), reader.getDoubleColumnValues(),
					visibleColumns, reader.getFilterableStringColumns(), null);
		}

		if (selectedID != null && reader.getPlotables().get(selectedID) != null) {
			Plotable plotable = reader.getPlotables().get(selectedID);

			configPanel.setParamsX(plotable.getPossibleArgumentValues(
					containsData, !containsData), plotable.getMinArguments(),
					plotable.getMaxArguments(), null);
			configPanel.setParamX(currentParamX);
			configPanel.setParamY(plotable.getFunctionValue());
			configPanel.setUnitX(unitX);
			configPanel.setUnitY(unitY);

			if (containsData) {
				configPanel.setSelectedValuesX(selectedValuesX);
			} else {
				configPanel.setParamXValues(paramXValues);
			}
		}

		if (containsData) {
			selectionPanel.setColorLists(colorLists);
			selectionPanel.setShapeLists(shapeLists);
		} else {
			selectionPanel.setColors(colors);
			selectionPanel.setShapes(shapes);
		}

		configPanel.setUseManualRange(manualRange == 1);
		configPanel.setMinX(minX);
		configPanel.setMaxX(maxX);
		configPanel.setMinY(minY);
		configPanel.setMaxY(maxY);
		configPanel.setDrawLines(drawLines == 1);
		configPanel.setShowLegend(showLegend == 1);
		configPanel.setAddInfoInLegend(addLegendInfo == 1);
		configPanel.setDisplayFocusedRow(displayHighlighted == 1);
		configPanel.setShowConfidenceInterval(showConfidence == 1);
		configPanel.setTransformY(transformY);
		configPanel.addConfigListener(this);
		selectionPanel.setFilter(ChartConstants.STATUS, fittedFilter);
		selectionPanel.setSelectedIDs(Arrays.asList(selectedID));
		selectionPanel.addSelectionListener(this);
		chartCreator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());
		infoPanel = new ChartInfoPanel(reader.getIds(),
				reader.getInfoParameters(), reader.getInfoParameterValues());
		createChart();

		JSplitPane upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				chartCreator, selectionPanel);
		JPanel bottomPanel = new JPanel();

		upperSplitPane.setResizeWeight(1.0);
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(configPanel, BorderLayout.WEST);
		bottomPanel.add(infoPanel, BorderLayout.CENTER);
		bottomPanel.setMinimumSize(bottomPanel.getPreferredSize());

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
				upperSplitPane, bottomPanel);
		Dimension preferredSize = splitPane.getPreferredSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		preferredSize.width = Math.min(preferredSize.width,
				(int) (screenSize.width * 0.9));
		preferredSize.height = Math.min(preferredSize.height,
				(int) (screenSize.height * 0.9));

		splitPane.setResizeWeight(1.0);
		splitPane.setPreferredSize(preferredSize);

		return splitPane;
	}

	private void createChart() {
		String selectedID = null;

		if (configPanel.isDisplayFocusedRow()) {
			selectedID = selectionPanel.getFocusedID();
		} else {
			if (!selectionPanel.getSelectedIDs().isEmpty()) {
				selectedID = selectionPanel.getSelectedIDs().get(0);
			}
		}

		if (selectedID != null) {
			Plotable plotable = chartCreator.getPlotables().get(selectedID);

			configPanel.setParamsX(plotable.getPossibleArgumentValues(
					containsData, !containsData), plotable.getMinArguments(),
					plotable.getMaxArguments(), null);
			configPanel.setParamY(plotable.getFunctionValue());
			chartCreator.setParamX(configPanel.getParamX());
			chartCreator.setParamY(configPanel.getParamY());
			chartCreator.setUnitX(configPanel.getUnitX());
			chartCreator.setUnitY(configPanel.getUnitY());
			chartCreator.setTransformY(configPanel.getTransformY());
			plotable.setFunctionArguments(configPanel.getParamsX());
		} else {
			configPanel.setParamsX(null, null, null, null);
			configPanel.setParamY(null);
			chartCreator.setParamX(null);
			chartCreator.setParamY(null);
			chartCreator.setUnitX(null);
			chartCreator.setUnitY(null);
			chartCreator.setTransformY(null);
		}

		if (containsData) {
			chartCreator.setColorLists(selectionPanel.getColorLists());
			chartCreator.setShapeLists(selectionPanel.getShapeLists());
		} else {
			chartCreator.setColors(selectionPanel.getColors());
			chartCreator.setShapes(selectionPanel.getShapes());
		}

		chartCreator.setUseManualRange(configPanel.isUseManualRange());
		chartCreator.setMinX(configPanel.getMinX());
		chartCreator.setMinY(configPanel.getMinY());
		chartCreator.setMaxX(configPanel.getMaxX());
		chartCreator.setMaxY(configPanel.getMaxY());
		chartCreator.setDrawLines(configPanel.isDrawLines());
		chartCreator.setShowLegend(configPanel.isShowLegend());
		chartCreator.setAddInfoInLegend(configPanel.isAddInfoInLegend());
		chartCreator.setShowConfidenceInterval(configPanel
				.isShowConfidenceInterval());
		chartCreator.createChart(selectedID);
	}

	@Override
	public void configChanged() {
		createChart();
	}

	@Override
	public void selectionChanged() {
		createChart();
	}

	@Override
	public void focusChanged() {
		infoPanel.showID(selectionPanel.getFocusedID());

		if (configPanel.isDisplayFocusedRow()) {
			createChart();
		}
	}

}
