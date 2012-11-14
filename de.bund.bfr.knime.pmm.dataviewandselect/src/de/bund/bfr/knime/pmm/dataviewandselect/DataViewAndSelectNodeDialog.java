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
import java.util.Arrays;
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

import de.bund.bfr.knime.pmm.common.ListUtilities;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.chart.ChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartInfoPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

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
		List<String> selectedIDs;
		Map<String, Color> colors;
		Map<String, Shape> shapes;
		int selectAllIDS;
		int manualRange;
		double minX;
		double maxX;
		double minY;
		double maxY;
		int drawLines;
		int showLegend;
		int addLegendInfo;
		int displayHighlighted;
		String transformY;
		List<String> visibleColumns;

		try {
			selectedIDs = ListUtilities.getStringListFromString(settings
					.getString(DataViewAndSelectNodeModel.CFG_SELECTEDIDS));
		} catch (InvalidSettingsException e) {
			selectedIDs = new ArrayList<String>();
		}

		try {
			colors = DataViewAndSelectNodeModel.readColors(settings);
		} catch (InvalidSettingsException e) {
			colors = new LinkedHashMap<String, Color>();
		}

		try {
			shapes = DataViewAndSelectNodeModel.readShapes(settings);
		} catch (InvalidSettingsException e) {
			shapes = new LinkedHashMap<String, Shape>();
		}

		try {
			selectAllIDS = settings
					.getInt(DataViewAndSelectNodeModel.CFG_SELECTALLIDS);
		} catch (InvalidSettingsException e) {
			selectAllIDS = DataViewAndSelectNodeModel.DEFAULT_SELECTALLIDS;
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
			transformY = settings
					.getString(DataViewAndSelectNodeModel.CFG_TRANSFORMY);
		} catch (InvalidSettingsException e) {
			transformY = DataViewAndSelectNodeModel.DEFAULT_TRANSFORMY;
		}

		try {
			visibleColumns = ListUtilities.getStringListFromString(settings
					.getString(DataViewAndSelectNodeModel.CFG_VISIBLECOLUMNS));
		} catch (InvalidSettingsException e) {
			visibleColumns = ListUtilities
					.getStringListFromString(DataViewAndSelectNodeModel.DEFAULT_VISIBLECOLUMNS);
		}

		try {
			reader = new TableReader(input[0], new TimeSeriesSchema());
		} catch (PmmException e) {
			reader = null;
			e.printStackTrace();
		}

		if (selectAllIDS == 1) {
			selectedIDs = reader.getIds();
		}

		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(createMainComponent(selectedIDs,
				colors, shapes, manualRange == 1, minX, maxX, minY, maxY,
				drawLines == 1, showLegend == 1, addLegendInfo == 1,
				displayHighlighted == 1, transformY, visibleColumns));
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		settings.addString(
				DataViewAndSelectNodeModel.CFG_SELECTEDIDS,
				ListUtilities.getStringFromList(selectionPanel.getSelectedIDs()));
		DataViewAndSelectNodeModel.writeColors(selectionPanel.getColors(),
				settings);
		DataViewAndSelectNodeModel.writeShapes(selectionPanel.getShapes(),
				settings);
		settings.addString(DataViewAndSelectNodeModel.CFG_VISIBLECOLUMNS,
				ListUtilities.getStringFromList(selectionPanel
						.getVisibleColumns()));

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

		settings.addString(DataViewAndSelectNodeModel.CFG_TRANSFORMY,
				configPanel.getTransformY());
	}

	private JComponent createMainComponent(List<String> selectedIDs,
			Map<String, Color> colors, Map<String, Shape> shapes,
			boolean manualRange, double minX, double maxX, double minY,
			double maxY, boolean drawLines, boolean showLegend,
			boolean addLegendInfo, boolean displayHighlighted,
			String transformY, List<String> visibleColumns) {
		Map<String, List<Double>> paramsX = new LinkedHashMap<String, List<Double>>();

		paramsX.put(TimeSeriesSchema.TIME, new ArrayList<Double>());

		configPanel = new ChartConfigPanel(ChartConfigPanel.NO_PARAMETER_INPUT,
				false);
		configPanel.setParamsX(paramsX);
		configPanel.setParamsY(Arrays.asList(TimeSeriesSchema.LOGC));
		configPanel.setUseManualRange(manualRange);
		configPanel.setMinX(minX);
		configPanel.setMaxX(maxX);
		configPanel.setMinY(minY);
		configPanel.setMaxY(maxY);
		configPanel.setDrawLines(drawLines);
		configPanel.setShowLegend(showLegend);
		configPanel.setAddInfoInLegend(addLegendInfo);
		configPanel.setDisplayFocusedRow(displayHighlighted);
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
