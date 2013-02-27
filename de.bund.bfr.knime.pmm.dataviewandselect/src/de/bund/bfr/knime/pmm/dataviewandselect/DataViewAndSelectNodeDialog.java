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
package de.bund.bfr.knime.pmm.dataviewandselect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.chart.ChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartInfoPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;

/**
 * <code>NodeDialog</code> for the "DataViewAndSelect" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class DataViewAndSelectNodeDialog extends DataAwareNodeDialogPane
		implements ChartSelectionPanel.SelectionListener,
		ChartConfigPanel.ConfigListener {

	private TableReader reader;

	private ChartCreator chartCreator;
	private ChartSelectionPanel selectionPanel;
	private ChartConfigPanel configPanel;
	private ChartInfoPanel infoPanel;

	private List<String> selectedIDs;
	private Map<String, Color> colors;
	private Map<String, Shape> shapes;
	private int selectAllIDs;
	private int manualRange;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private int drawLines;
	private int showLegend;
	private int addLegendInfo;
	private int displayHighlighted;
	private String unitX;
	private String unitY;
	private String transformY;
	private int standardVisibleColumns;
	private List<String> visibleColumns;

	/**
	 * New pane for configuring the DataViewAndSelect node.
	 */
	protected DataViewAndSelectNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		try {
			selectedIDs = XmlConverter.xmlToStringList(settings
					.getString(DataViewAndSelectNodeModel.CFG_SELECTEDIDS));
		} catch (InvalidSettingsException e) {
			selectedIDs = new ArrayList<String>();
		}

		try {
			colors = XmlConverter.xmlToColorMap(settings
					.getString(DataViewAndSelectNodeModel.CFG_COLORS));
		} catch (InvalidSettingsException e) {
			colors = new LinkedHashMap<String, Color>();
		}

		try {
			shapes = XmlConverter.xmlToShapeMap(settings
					.getString(DataViewAndSelectNodeModel.CFG_SHAPES));
		} catch (InvalidSettingsException e) {
			shapes = new LinkedHashMap<String, Shape>();
		}

		try {
			selectAllIDs = settings
					.getInt(DataViewAndSelectNodeModel.CFG_SELECTALLIDS);
		} catch (InvalidSettingsException e) {
			selectAllIDs = DataViewAndSelectNodeModel.DEFAULT_SELECTALLIDS;
		}

		try {
			manualRange = settings
					.getInt(DataViewAndSelectNodeModel.CFG_MANUALRANGE);
		} catch (InvalidSettingsException e) {
			manualRange = DataViewAndSelectNodeModel.DEFAULT_MANUALRANGE;
		}

		try {
			minX = settings.getDouble(DataViewAndSelectNodeModel.CFG_MINX);
		} catch (InvalidSettingsException e) {
			minX = DataViewAndSelectNodeModel.DEFAULT_MINX;
		}

		try {
			maxX = settings.getDouble(DataViewAndSelectNodeModel.CFG_MAXX);
		} catch (InvalidSettingsException e) {
			maxX = DataViewAndSelectNodeModel.DEFAULT_MAXX;
		}

		try {
			minY = settings.getDouble(DataViewAndSelectNodeModel.CFG_MINY);
		} catch (InvalidSettingsException e) {
			minY = DataViewAndSelectNodeModel.DEFAULT_MINY;
		}

		try {
			maxY = settings.getDouble(DataViewAndSelectNodeModel.CFG_MAXY);
		} catch (InvalidSettingsException e) {
			maxY = DataViewAndSelectNodeModel.DEFAULT_MAXY;
		}

		try {
			drawLines = settings
					.getInt(DataViewAndSelectNodeModel.CFG_DRAWLINES);
		} catch (InvalidSettingsException e) {
			drawLines = DataViewAndSelectNodeModel.DEFAULT_DRAWLINES;
		}

		try {
			showLegend = settings
					.getInt(DataViewAndSelectNodeModel.CFG_SHOWLEGEND);
		} catch (InvalidSettingsException e) {
			showLegend = DataViewAndSelectNodeModel.DEFAULT_SHOWLEGEND;
		}

		try {
			addLegendInfo = settings
					.getInt(DataViewAndSelectNodeModel.CFG_ADDLEGENDINFO);
		} catch (InvalidSettingsException e) {
			addLegendInfo = DataViewAndSelectNodeModel.DEFAULT_ADDLEGENDINFO;
		}

		try {
			displayHighlighted = settings
					.getInt(DataViewAndSelectNodeModel.CFG_DISPLAYHIGHLIGHTED);
		} catch (InvalidSettingsException e) {
			displayHighlighted = DataViewAndSelectNodeModel.DEFAULT_DISPLAYHIGHLIGHTED;
		}

		try {
			unitX = settings.getString(DataViewAndSelectNodeModel.CFG_UNITX);
		} catch (InvalidSettingsException e) {
			unitX = DataViewAndSelectNodeModel.DEFAULT_UNITX;
		}

		try {
			unitY = settings.getString(DataViewAndSelectNodeModel.CFG_UNITY);
		} catch (InvalidSettingsException e) {
			unitY = DataViewAndSelectNodeModel.DEFAULT_UNITY;
		}

		try {
			transformY = settings
					.getString(DataViewAndSelectNodeModel.CFG_TRANSFORMY);
		} catch (InvalidSettingsException e) {
			transformY = DataViewAndSelectNodeModel.DEFAULT_TRANSFORMY;
		}

		try {
			standardVisibleColumns = settings
					.getInt(DataViewAndSelectNodeModel.CFG_STANDARDVISIBLECOLUMNS);
		} catch (InvalidSettingsException e) {
			standardVisibleColumns = DataViewAndSelectNodeModel.DEFAULT_STANDARDVISIBLECOLUMNS;
		}

		try {
			visibleColumns = XmlConverter.xmlToStringList(settings
					.getString(DataViewAndSelectNodeModel.CFG_VISIBLECOLUMNS));
		} catch (InvalidSettingsException e) {
			visibleColumns = new ArrayList<>();
		}

		reader = new TableReader(input[0]);

		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(createMainComponent());
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		settings.addString(DataViewAndSelectNodeModel.CFG_SELECTEDIDS,
				XmlConverter.listToXml(selectionPanel.getSelectedIDs()));
		settings.addString(DataViewAndSelectNodeModel.CFG_COLORS,
				XmlConverter.colorMapToXml(selectionPanel.getColors()));
		settings.addString(DataViewAndSelectNodeModel.CFG_SHAPES,
				XmlConverter.shapeMapToXml(selectionPanel.getShapes()));
		settings.addInt(DataViewAndSelectNodeModel.CFG_STANDARDVISIBLECOLUMNS,
				0);
		settings.addString(DataViewAndSelectNodeModel.CFG_VISIBLECOLUMNS,
				XmlConverter.listToXml(selectionPanel.getVisibleColumns()));

		settings.addInt(DataViewAndSelectNodeModel.CFG_SELECTALLIDS, 0);

		if (configPanel.isUseManualRange()) {
			settings.addInt(DataViewAndSelectNodeModel.CFG_MANUALRANGE, 1);
		} else {
			settings.addInt(DataViewAndSelectNodeModel.CFG_MANUALRANGE, 0);
		}

		settings.addDouble(DataViewAndSelectNodeModel.CFG_MINX,
				configPanel.getMinX());
		settings.addDouble(DataViewAndSelectNodeModel.CFG_MAXX,
				configPanel.getMaxX());
		settings.addDouble(DataViewAndSelectNodeModel.CFG_MINY,
				configPanel.getMinY());
		settings.addDouble(DataViewAndSelectNodeModel.CFG_MAXY,
				configPanel.getMaxY());

		if (configPanel.isDrawLines()) {
			settings.addInt(DataViewAndSelectNodeModel.CFG_DRAWLINES, 1);
		} else {
			settings.addInt(DataViewAndSelectNodeModel.CFG_DRAWLINES, 0);
		}

		if (configPanel.isShowLegend()) {
			settings.addInt(DataViewAndSelectNodeModel.CFG_SHOWLEGEND, 1);
		} else {
			settings.addInt(DataViewAndSelectNodeModel.CFG_SHOWLEGEND, 0);
		}

		if (configPanel.isAddInfoInLegend()) {
			settings.addInt(DataViewAndSelectNodeModel.CFG_ADDLEGENDINFO, 1);
		} else {
			settings.addInt(DataViewAndSelectNodeModel.CFG_ADDLEGENDINFO, 0);
		}

		if (configPanel.isDisplayFocusedRow()) {
			settings.addInt(DataViewAndSelectNodeModel.CFG_DISPLAYHIGHLIGHTED,
					1);
		} else {
			settings.addInt(DataViewAndSelectNodeModel.CFG_DISPLAYHIGHLIGHTED,
					0);
		}

		settings.addString(DataViewAndSelectNodeModel.CFG_UNITX,
				configPanel.getUnitX());
		settings.addString(DataViewAndSelectNodeModel.CFG_UNITY,
				configPanel.getUnitY());
		settings.addString(DataViewAndSelectNodeModel.CFG_TRANSFORMY,
				configPanel.getTransformY());
	}

	private JComponent createMainComponent() {
		Map<String, List<Double>> paramsX = new LinkedHashMap<String, List<Double>>();

		paramsX.put(AttributeUtilities.TIME, new ArrayList<Double>());

		if (selectAllIDs == 1) {
			selectedIDs = reader.getIds();
		}

		if (standardVisibleColumns == 1) {
			visibleColumns = reader.getStandardVisibleColumns();
		}

		configPanel = new ChartConfigPanel(ChartConfigPanel.NO_PARAMETER_INPUT,
				false);
		configPanel.setParamsX(paramsX, null, null, null);
		configPanel.setParamY(AttributeUtilities.LOGC);
		configPanel.setUseManualRange(manualRange == 1);
		configPanel.setMinX(minX);
		configPanel.setMaxX(maxX);
		configPanel.setMinY(minY);
		configPanel.setMaxY(maxY);
		configPanel.setDrawLines(drawLines == 1);
		configPanel.setShowLegend(showLegend == 1);
		configPanel.setAddInfoInLegend(addLegendInfo == 1);
		configPanel.setDisplayFocusedRow(displayHighlighted == 1);
		configPanel.setUnitX(unitX);
		configPanel.setUnitY(unitY);
		configPanel.setTransformY(transformY);
		configPanel.addConfigListener(this);
		selectionPanel = new ChartSelectionPanel(reader.getIds(), false,
				reader.getStringColumns(), reader.getStringColumnValues(),
				reader.getDoubleColumns(), reader.getDoubleColumnValues(),
				visibleColumns, new ArrayList<String>());
		selectionPanel.setColors(colors);
		selectionPanel.setShapes(shapes);
		selectionPanel.addSelectionListener(this);
		chartCreator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());
		infoPanel = new ChartInfoPanel(reader.getIds(),
				reader.getInfoParameters(), reader.getInfoParameterValues());

		if (selectedIDs != null) {
			selectionPanel.setSelectedIDs(selectedIDs);
		}

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

		splitPane.setPreferredSize(preferredSize);
		splitPane.setResizeWeight(1.0);

		return splitPane;
	}

	private void createChart() {
		chartCreator.setParamX(configPanel.getParamX());
		chartCreator.setParamY(configPanel.getParamY());
		chartCreator.setUnitX(configPanel.getUnitX());
		chartCreator.setUnitY(configPanel.getUnitY());
		chartCreator.setTransformY(configPanel.getTransformY());
		chartCreator.setUseManualRange(configPanel.isUseManualRange());
		chartCreator.setMinX(configPanel.getMinX());
		chartCreator.setMinY(configPanel.getMinY());
		chartCreator.setMaxX(configPanel.getMaxX());
		chartCreator.setMaxY(configPanel.getMaxY());
		chartCreator.setDrawLines(configPanel.isDrawLines());
		chartCreator.setShowLegend(configPanel.isShowLegend());
		chartCreator.setAddInfoInLegend(configPanel.isAddInfoInLegend());
		chartCreator.setColors(selectionPanel.getColors());
		chartCreator.setShapes(selectionPanel.getShapes());

		if (configPanel.isDisplayFocusedRow()) {
			chartCreator.createChart(selectionPanel.getFocusedID());
		} else {
			chartCreator.createChart(selectionPanel.getSelectedIDs());
		}
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

	@Override
	public void configChanged() {
		createChart();
	}

}
