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
package de.bund.bfr.knime.pmm.modelselectiontertiary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.DataAwareNodeDialogPane;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;

import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.chart.ChartAllPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.units.BacterialConcentration;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.units.Time;

/**
 * <code>NodeDialog</code> for the "ModelSelectionTertiary" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class ModelSelectionTertiaryNodeDialog extends DataAwareNodeDialogPane
		implements ChartSelectionPanel.SelectionListener,
		ChartConfigPanel.ConfigListener {

	private TableReader reader;

	private ChartCreator chartCreator;
	private ChartSelectionPanel selectionPanel;
	private ChartConfigPanel configPanel;

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
	private String modelFilter;
	private String dataFilter;
	private String fittedFilter;

	/**
	 * New pane for configuring the ModelSelectionTertiary node.
	 */
	protected ModelSelectionTertiaryNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		boolean schemaContainsData;

		if (SchemaFactory.createDataSchema().conforms(input[0])) {
			schemaContainsData = true;
		} else {
			schemaContainsData = false;
		}

		try {
			selectedIDs = XmlConverter
					.xmlToObject(
							settings.getString(ModelSelectionTertiaryNodeModel.CFG_SELECTEDIDS),
							new ArrayList<String>());
		} catch (InvalidSettingsException e) {
			selectedIDs = new ArrayList<String>();
		}

		try {
			colors = XmlConverter.xmlToColorMap(settings
					.getString(ModelSelectionTertiaryNodeModel.CFG_COLORS));
		} catch (InvalidSettingsException e) {
			colors = new LinkedHashMap<String, Color>();
		}

		try {
			shapes = XmlConverter.xmlToShapeMap(settings
					.getString(ModelSelectionTertiaryNodeModel.CFG_SHAPES));
		} catch (InvalidSettingsException e) {
			shapes = new LinkedHashMap<String, Shape>();
		}

		try {
			selectAllIDs = settings
					.getInt(ModelSelectionTertiaryNodeModel.CFG_SELECTALLIDS);
		} catch (InvalidSettingsException e) {
			selectAllIDs = ModelSelectionTertiaryNodeModel.DEFAULT_SELECTALLIDS;
		}

		try {
			manualRange = settings
					.getInt(ModelSelectionTertiaryNodeModel.CFG_MANUALRANGE);
		} catch (InvalidSettingsException e) {
			manualRange = ModelSelectionTertiaryNodeModel.DEFAULT_MANUALRANGE;
		}

		try {
			minX = settings.getDouble(ModelSelectionTertiaryNodeModel.CFG_MINX);
		} catch (InvalidSettingsException e) {
			minX = ModelSelectionTertiaryNodeModel.DEFAULT_MINX;
		}

		try {
			maxX = settings.getDouble(ModelSelectionTertiaryNodeModel.CFG_MAXX);
		} catch (InvalidSettingsException e) {
			maxX = ModelSelectionTertiaryNodeModel.DEFAULT_MAXX;
		}

		try {
			minY = settings.getDouble(ModelSelectionTertiaryNodeModel.CFG_MINY);
		} catch (InvalidSettingsException e) {
			minY = ModelSelectionTertiaryNodeModel.DEFAULT_MINY;
		}

		try {
			maxY = settings.getDouble(ModelSelectionTertiaryNodeModel.CFG_MAXY);
		} catch (InvalidSettingsException e) {
			maxY = ModelSelectionTertiaryNodeModel.DEFAULT_MAXY;
		}

		try {
			drawLines = settings
					.getInt(ModelSelectionTertiaryNodeModel.CFG_DRAWLINES);
		} catch (InvalidSettingsException e) {
			drawLines = ModelSelectionTertiaryNodeModel.DEFAULT_DRAWLINES;
		}

		try {
			showLegend = settings
					.getInt(ModelSelectionTertiaryNodeModel.CFG_SHOWLEGEND);
		} catch (InvalidSettingsException e) {
			showLegend = ModelSelectionTertiaryNodeModel.DEFAULT_SHOWLEGEND;
		}

		try {
			addLegendInfo = settings
					.getInt(ModelSelectionTertiaryNodeModel.CFG_ADDLEGENDINFO);
		} catch (InvalidSettingsException e) {
			addLegendInfo = ModelSelectionTertiaryNodeModel.DEFAULT_ADDLEGENDINFO;
		}

		try {
			displayHighlighted = settings
					.getInt(ModelSelectionTertiaryNodeModel.CFG_DISPLAYHIGHLIGHTED);
		} catch (InvalidSettingsException e) {
			displayHighlighted = ModelSelectionTertiaryNodeModel.DEFAULT_DISPLAYHIGHLIGHTED;
		}

		try {
			unitX = settings
					.getString(ModelSelectionTertiaryNodeModel.CFG_UNITX);
		} catch (InvalidSettingsException e) {
			unitX = ModelSelectionTertiaryNodeModel.DEFAULT_UNITX;
		}

		try {
			unitY = settings
					.getString(ModelSelectionTertiaryNodeModel.CFG_UNITY);
		} catch (InvalidSettingsException e) {
			unitY = ModelSelectionTertiaryNodeModel.DEFAULT_UNITY;
		}

		try {
			transformY = settings
					.getString(ModelSelectionTertiaryNodeModel.CFG_TRANSFORMY);
		} catch (InvalidSettingsException e) {
			transformY = ModelSelectionTertiaryNodeModel.DEFAULT_TRANSFORMY;
		}

		try {
			standardVisibleColumns = settings
					.getInt(ModelSelectionTertiaryNodeModel.CFG_STANDARDVISIBLECOLUMNS);
		} catch (InvalidSettingsException e) {
			standardVisibleColumns = ModelSelectionTertiaryNodeModel.DEFAULT_STANDARDVISIBLECOLUMNS;
		}

		try {
			visibleColumns = XmlConverter
					.xmlToObject(
							settings.getString(ModelSelectionTertiaryNodeModel.CFG_VISIBLECOLUMNS),
							new ArrayList<String>());
		} catch (InvalidSettingsException e) {
			visibleColumns = new ArrayList<>();
		}

		try {
			modelFilter = settings
					.getString(ModelSelectionTertiaryNodeModel.CFG_MODELFILTER);
		} catch (InvalidSettingsException e) {
			modelFilter = ModelSelectionTertiaryNodeModel.DEFAULT_MODELFILTER;
		}

		try {
			dataFilter = settings
					.getString(ModelSelectionTertiaryNodeModel.CFG_DATAFILTER);
		} catch (InvalidSettingsException e) {
			dataFilter = ModelSelectionTertiaryNodeModel.DEFAULT_DATAFILTER;
		}

		try {
			fittedFilter = settings
					.getString(ModelSelectionTertiaryNodeModel.CFG_FITTEDFILTER);
		} catch (InvalidSettingsException e) {
			fittedFilter = ModelSelectionTertiaryNodeModel.DEFAULT_FITTEDFILTER;
		}

		reader = new TableReader(input[0], schemaContainsData);
		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(createMainComponent());
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		settings.addString(ModelSelectionTertiaryNodeModel.CFG_SELECTEDIDS,
				XmlConverter.objectToXml(selectionPanel.getSelectedIDs()));
		settings.addString(ModelSelectionTertiaryNodeModel.CFG_COLORS,
				XmlConverter.colorMapToXml(selectionPanel.getColors()));
		settings.addString(ModelSelectionTertiaryNodeModel.CFG_SHAPES,
				XmlConverter.shapeMapToXml(selectionPanel.getShapes()));
		settings.addInt(
				ModelSelectionTertiaryNodeModel.CFG_STANDARDVISIBLECOLUMNS, 0);
		settings.addString(ModelSelectionTertiaryNodeModel.CFG_VISIBLECOLUMNS,
				XmlConverter.objectToXml(selectionPanel.getVisibleColumns()));

		settings.addInt(ModelSelectionTertiaryNodeModel.CFG_SELECTALLIDS, 0);

		if (configPanel.isUseManualRange()) {
			settings.addInt(ModelSelectionTertiaryNodeModel.CFG_MANUALRANGE, 1);
		} else {
			settings.addInt(ModelSelectionTertiaryNodeModel.CFG_MANUALRANGE, 0);
		}

		settings.addDouble(ModelSelectionTertiaryNodeModel.CFG_MINX,
				configPanel.getMinX());
		settings.addDouble(ModelSelectionTertiaryNodeModel.CFG_MAXX,
				configPanel.getMaxX());
		settings.addDouble(ModelSelectionTertiaryNodeModel.CFG_MINY,
				configPanel.getMinY());
		settings.addDouble(ModelSelectionTertiaryNodeModel.CFG_MAXY,
				configPanel.getMaxY());

		if (configPanel.isDrawLines()) {
			settings.addInt(ModelSelectionTertiaryNodeModel.CFG_DRAWLINES, 1);
		} else {
			settings.addInt(ModelSelectionTertiaryNodeModel.CFG_DRAWLINES, 0);
		}

		if (configPanel.isShowLegend()) {
			settings.addInt(ModelSelectionTertiaryNodeModel.CFG_SHOWLEGEND, 1);
		} else {
			settings.addInt(ModelSelectionTertiaryNodeModel.CFG_SHOWLEGEND, 0);
		}

		if (configPanel.isAddInfoInLegend()) {
			settings.addInt(ModelSelectionTertiaryNodeModel.CFG_ADDLEGENDINFO,
					1);
		} else {
			settings.addInt(ModelSelectionTertiaryNodeModel.CFG_ADDLEGENDINFO,
					0);
		}

		if (configPanel.isDisplayFocusedRow()) {
			settings.addInt(
					ModelSelectionTertiaryNodeModel.CFG_DISPLAYHIGHLIGHTED, 1);
		} else {
			settings.addInt(
					ModelSelectionTertiaryNodeModel.CFG_DISPLAYHIGHLIGHTED, 0);
		}

		settings.addString(ModelSelectionTertiaryNodeModel.CFG_UNITX,
				configPanel.getUnitX());
		settings.addString(ModelSelectionTertiaryNodeModel.CFG_UNITY,
				configPanel.getUnitY());
		settings.addString(ModelSelectionTertiaryNodeModel.CFG_TRANSFORMY,
				configPanel.getTransformY());
		settings.addString(ModelSelectionTertiaryNodeModel.CFG_MODELFILTER,
				selectionPanel.getFilter(Model1Schema.MODELNAME));
		settings.addString(ModelSelectionTertiaryNodeModel.CFG_DATAFILTER,
				selectionPanel.getFilter(AttributeUtilities.DATAID));
		settings.addString(ModelSelectionTertiaryNodeModel.CFG_FITTEDFILTER,
				selectionPanel.getFilter(ChartConstants.STATUS));
	}

	private JComponent createMainComponent() {
		Map<String, String> categories = new LinkedHashMap<>();
		Map<String, String> units = new LinkedHashMap<>();
		Map<String, List<Double>> paramsX = new LinkedHashMap<String, List<Double>>();

		categories.put(AttributeUtilities.TIME, Categories.TIME);
		categories.put(AttributeUtilities.LOGC,
				Categories.BACTERIAL_CONCENTRATION);
		units.put(AttributeUtilities.TIME, new Time().getStandardUnit());
		units.put(AttributeUtilities.LOGC,
				new BacterialConcentration().getStandardUnit());
		paramsX.put(AttributeUtilities.TIME, new ArrayList<Double>());

		if (selectAllIDs == 1) {
			selectedIDs = reader.getIds();
		}

		if (standardVisibleColumns == 1) {
			visibleColumns = reader.getStandardVisibleColumns();
		}

		configPanel = new ChartConfigPanel(ChartConfigPanel.NO_PARAMETER_INPUT,
				false, null);
		configPanel.setParameters(AttributeUtilities.LOGC, paramsX, null, null,
				categories, units, null);
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
				reader.getConditions(), reader.getConditionValues(),
				reader.getConditionUnits(), visibleColumns,
				reader.getFilterableStringColumns(), reader.getData(),
				reader.getParameterData());
		selectionPanel.setColors(colors);
		selectionPanel.setShapes(shapes);
		selectionPanel.setFilter(Model1Schema.MODELNAME, modelFilter);
		selectionPanel.setFilter(AttributeUtilities.DATAID, dataFilter);
		selectionPanel.setFilter(ChartConstants.STATUS, fittedFilter);
		selectionPanel.addSelectionListener(this);
		chartCreator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());

		if (selectedIDs != null) {
			selectionPanel.setSelectedIDs(selectedIDs);
		}

		return new ChartAllPanel(chartCreator, selectionPanel, configPanel);
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
		if (configPanel.isDisplayFocusedRow()) {
			createChart();
		}
	}

	@Override
	public void configChanged() {
		createChart();
	}
}
