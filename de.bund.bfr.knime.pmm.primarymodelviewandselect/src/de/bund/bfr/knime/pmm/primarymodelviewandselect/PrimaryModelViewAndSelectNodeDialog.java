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
package de.bund.bfr.knime.pmm.primarymodelviewandselect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.chart.DataAndModelChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.DataAndModelChartCreator;
import de.bund.bfr.knime.pmm.common.chart.DataAndModelChartInfoPanel;
import de.bund.bfr.knime.pmm.common.chart.DataAndModelSelectionPanel;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * <code>NodeDialog</code> for the "PrimaryModelViewAndSelect" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class PrimaryModelViewAndSelectNodeDialog extends
		DataAwareNodeDialogPane implements
		DataAndModelSelectionPanel.SelectionListener,
		DataAndModelChartConfigPanel.ConfigListener {

	private TableReader reader;

	private DataAndModelChartCreator chartCreator;
	private DataAndModelSelectionPanel selectionPanel;
	private DataAndModelChartConfigPanel configPanel;
	private DataAndModelChartInfoPanel infoPanel;

	/**
	 * New pane for configuring the PrimaryModelViewAndSelect node.
	 */
	protected PrimaryModelViewAndSelectNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		KnimeSchema schema = null;
		KnimeSchema model1Schema = null;
		KnimeSchema peiSchema = null;

		try {
			model1Schema = new Model1Schema();
			peiSchema = new KnimeSchema(new Model1Schema(),
					new TimeSeriesSchema());

			if (peiSchema.conforms(input[0].getSpec())) {
				schema = peiSchema;
			} else if (model1Schema.conforms(input[0].getSpec())) {
				schema = model1Schema;
			}
		} catch (PmmException e) {
		}

		List<String> selectedIDs;
		Map<String, Color> colors;
		Map<String, Shape> shapes;
		int selectAllIDS;
		int manualRange;
		double minX;
		double maxX;
		double minY;
		double maxY;

		try {
			selectedIDs = PrimaryModelViewAndSelectNodeModel
					.readSelectedIDs(settings);
		} catch (InvalidSettingsException e) {
			selectedIDs = new ArrayList<String>();
		}

		try {
			colors = PrimaryModelViewAndSelectNodeModel.readColors(settings);
		} catch (InvalidSettingsException e) {
			colors = new HashMap<String, Color>();
		}

		try {
			shapes = PrimaryModelViewAndSelectNodeModel.readShapes(settings);
		} catch (InvalidSettingsException e) {
			shapes = new HashMap<String, Shape>();
		}

		try {
			selectAllIDS = settings
					.getInt(PrimaryModelViewAndSelectNodeModel.CFG_SELECTALLIDS);
		} catch (InvalidSettingsException e) {
			selectAllIDS = PrimaryModelViewAndSelectNodeModel.DEFAULT_SELECTALLIDS;
		}

		try {
			manualRange = settings
					.getInt(PrimaryModelViewAndSelectNodeModel.CFG_MANUALRANGE);
		} catch (InvalidSettingsException e) {
			manualRange = PrimaryModelViewAndSelectNodeModel.DEFAULT_MANUALRANGE;
		}

		try {
			minX = settings
					.getDouble(PrimaryModelViewAndSelectNodeModel.CFG_MINX);
		} catch (InvalidSettingsException e) {
			minX = PrimaryModelViewAndSelectNodeModel.DEFAULT_MINX;
		}

		try {
			maxX = settings
					.getDouble(PrimaryModelViewAndSelectNodeModel.CFG_MAXX);
		} catch (InvalidSettingsException e) {
			maxX = PrimaryModelViewAndSelectNodeModel.DEFAULT_MAXX;
		}

		try {
			minY = settings
					.getDouble(PrimaryModelViewAndSelectNodeModel.CFG_MINY);
		} catch (InvalidSettingsException e) {
			minY = PrimaryModelViewAndSelectNodeModel.DEFAULT_MINY;
		}

		try {
			maxY = settings
					.getDouble(PrimaryModelViewAndSelectNodeModel.CFG_MAXY);
		} catch (InvalidSettingsException e) {
			maxY = PrimaryModelViewAndSelectNodeModel.DEFAULT_MAXY;
		}

		try {
			reader = new TableReader(input[0], schema, schema == peiSchema);
		} catch (PmmException e) {
			reader = null;
			e.printStackTrace();
		}

		if (selectAllIDS == 1) {
			selectedIDs = reader.getIds();
		}

		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(createMainComponent(selectedIDs,
				colors, shapes, manualRange == 1, minX, maxX, minY, maxY));
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		PrimaryModelViewAndSelectNodeModel.writeSelectedIDs(
				selectionPanel.getSelectedIDs(), settings);
		PrimaryModelViewAndSelectNodeModel.writeColors(
				selectionPanel.getColors(), settings);
		PrimaryModelViewAndSelectNodeModel.writeShapes(
				selectionPanel.getShapes(), settings);

		settings.addInt(PrimaryModelViewAndSelectNodeModel.CFG_SELECTALLIDS, 0);

		if (configPanel.isUseManualRange()) {
			settings.addInt(PrimaryModelViewAndSelectNodeModel.CFG_MANUALRANGE,
					1);
		} else {
			settings.addInt(PrimaryModelViewAndSelectNodeModel.CFG_MANUALRANGE,
					0);
		}

		settings.addDouble(PrimaryModelViewAndSelectNodeModel.CFG_MINX,
				configPanel.getMinX());
		settings.addDouble(PrimaryModelViewAndSelectNodeModel.CFG_MAXX,
				configPanel.getMaxX());
		settings.addDouble(PrimaryModelViewAndSelectNodeModel.CFG_MINY,
				configPanel.getMinY());
		settings.addDouble(PrimaryModelViewAndSelectNodeModel.CFG_MAXY,
				configPanel.getMaxY());
	}

	private JComponent createMainComponent(List<String> selectedIDs,
			Map<String, Color> colors, Map<String, Shape> shapes,
			boolean manualRange, double minX, double maxX, double minY,
			double maxY) {		
		configPanel = new DataAndModelChartConfigPanel(
				DataAndModelChartConfigPanel.NO_PARAMETER_INPUT);
		configPanel.setParamsX(Arrays.asList(TimeSeriesSchema.ATT_TIME));
		configPanel.setParamsY(Arrays.asList(TimeSeriesSchema.ATT_LOGC));
		configPanel.setUseManualRange(manualRange);
		configPanel.setMinX(minX);
		configPanel.setMaxX(maxX);
		configPanel.setMinY(minY);
		configPanel.setMaxY(maxY);
		configPanel.addConfigListener(this);
		selectionPanel = new DataAndModelSelectionPanel(reader.getIds(), false,
				reader.getStringColumns(), reader.getStringColumnValues(),
				reader.getDoubleColumns(), reader.getDoubleColumnValues(),
				Arrays.asList(true, true, false), Arrays.asList(true, true,
						true));
		selectionPanel.setColors(colors);
		selectionPanel.setShapes(shapes);
		selectionPanel.addSelectionListener(this);
		chartCreator = new DataAndModelChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());
		infoPanel = new DataAndModelChartInfoPanel(reader.getIds(),
				reader.getInfoParameters(), reader.getInfoParameterValues());

		if (selectedIDs != null) {
			selectionPanel.setSelectedIDs(selectedIDs);
		}

		JSplitPane upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				chartCreator, selectionPanel);
		JPanel bottomPanel = new JPanel();

		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(configPanel, BorderLayout.WEST);
		bottomPanel.add(infoPanel, BorderLayout.CENTER);

		return new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperSplitPane,
				bottomPanel);
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
