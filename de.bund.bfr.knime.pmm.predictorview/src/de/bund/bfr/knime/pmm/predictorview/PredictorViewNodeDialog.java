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
package de.bund.bfr.knime.pmm.predictorview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.chart.ChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartInfoPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartSamplePanel;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

/**
 * <code>NodeDialog</code> for the "PredictorView" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class PredictorViewNodeDialog extends DataAwareNodeDialogPane implements
		ChartSelectionPanel.SelectionListener, ChartConfigPanel.ConfigListener,
		ChartConfigPanel.ExtraButtonListener, ChartSamplePanel.EditListener {

	private DataTable table;
	private TableReader reader;

	private ChartCreator chartCreator;
	private ChartSelectionPanel selectionPanel;
	private ChartConfigPanel configPanel;
	private ChartInfoPanel infoPanel;
	private ChartSamplePanel samplePanel;

	private String selectedID;
	private Map<String, Double> paramXValues;
	private List<Double> timeValues;
	private Map<String, Color> colors;
	private Map<String, Shape> shapes;
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
	private String modelFilter;
	private String dataFilter;
	private String fittedFilter;
	private Map<String, String> concentrationParameters;

	/**
	 * New pane for configuring the ForecastStaticConditions node.
	 */
	protected PredictorViewNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		try {
			selectedID = settings
					.getString(PredictorViewNodeModel.CFG_SELECTEDID);
		} catch (InvalidSettingsException e) {
			selectedID = null;
		}

		try {
			paramXValues = XmlConverter.xmlToDoubleMap(settings
					.getString(PredictorViewNodeModel.CFG_PARAMXVALUES));
		} catch (InvalidSettingsException e) {
			paramXValues = new LinkedHashMap<>();
		}

		try {
			timeValues = XmlConverter.xmlToDoubleList(settings
					.getString(PredictorViewNodeModel.CFG_TIMEVALUES));
		} catch (InvalidSettingsException e1) {
			timeValues = new ArrayList<>();
		}

		try {
			colors = XmlConverter.xmlToColorMap(settings
					.getString(PredictorViewNodeModel.CFG_COLORS));
		} catch (InvalidSettingsException e) {
			colors = new LinkedHashMap<>();
		}

		try {
			shapes = XmlConverter.xmlToShapeMap(settings
					.getString(PredictorViewNodeModel.CFG_SHAPES));
		} catch (InvalidSettingsException e) {
			shapes = new LinkedHashMap<>();
		}

		try {
			manualRange = settings
					.getInt(PredictorViewNodeModel.CFG_MANUALRANGE);
		} catch (InvalidSettingsException e) {
			manualRange = PredictorViewNodeModel.DEFAULT_MANUALRANGE;
		}

		try {
			minX = settings.getDouble(PredictorViewNodeModel.CFG_MINX);
		} catch (InvalidSettingsException e) {
			minX = PredictorViewNodeModel.DEFAULT_MINX;
		}

		try {
			maxX = settings.getDouble(PredictorViewNodeModel.CFG_MAXX);
		} catch (InvalidSettingsException e) {
			maxX = PredictorViewNodeModel.DEFAULT_MAXX;
		}

		try {
			minY = settings.getDouble(PredictorViewNodeModel.CFG_MINY);
		} catch (InvalidSettingsException e) {
			minY = PredictorViewNodeModel.DEFAULT_MINY;
		}

		try {
			maxY = settings.getDouble(PredictorViewNodeModel.CFG_MAXY);
		} catch (InvalidSettingsException e) {
			maxY = PredictorViewNodeModel.DEFAULT_MAXY;
		}

		try {
			drawLines = settings.getInt(PredictorViewNodeModel.CFG_DRAWLINES);
		} catch (InvalidSettingsException e) {
			drawLines = PredictorViewNodeModel.DEFAULT_DRAWLINES;
		}

		try {
			showLegend = settings.getInt(PredictorViewNodeModel.CFG_SHOWLEGEND);
		} catch (InvalidSettingsException e) {
			showLegend = PredictorViewNodeModel.DEFAULT_SHOWLEGEND;
		}

		try {
			addLegendInfo = settings
					.getInt(PredictorViewNodeModel.CFG_ADDLEGENDINFO);
		} catch (InvalidSettingsException e) {
			addLegendInfo = PredictorViewNodeModel.DEFAULT_ADDLEGENDINFO;
		}

		try {
			displayHighlighted = settings
					.getInt(PredictorViewNodeModel.CFG_DISPLAYHIGHLIGHTED);
		} catch (InvalidSettingsException e) {
			displayHighlighted = PredictorViewNodeModel.DEFAULT_DISPLAYHIGHLIGHTED;
		}

		try {
			showConfidence = settings
					.getInt(PredictorViewNodeModel.CFG_SHOWCONFIDENCE);
		} catch (InvalidSettingsException e) {
			showConfidence = PredictorViewNodeModel.DEFAULT_SHOWCONFIDENCE;
		}

		try {
			unitX = settings.getString(PredictorViewNodeModel.CFG_UNITX);
		} catch (InvalidSettingsException e) {
			unitX = null;
		}

		try {
			unitY = settings.getString(PredictorViewNodeModel.CFG_UNITY);
		} catch (InvalidSettingsException e) {
			unitY = null;
		}

		try {
			transformY = settings
					.getString(PredictorViewNodeModel.CFG_TRANSFORMY);
		} catch (InvalidSettingsException e) {
			transformY = PredictorViewNodeModel.DEFAULT_TRANSFORMY;
		}

		try {
			standardVisibleColumns = settings
					.getInt(PredictorViewNodeModel.CFG_STANDARDVISIBLECOLUMNS);
		} catch (InvalidSettingsException e) {
			standardVisibleColumns = PredictorViewNodeModel.DEFAULT_STANDARDVISIBLECOLUMNS;
		}

		try {
			visibleColumns = XmlConverter.xmlToStringList(settings
					.getString(PredictorViewNodeModel.CFG_VISIBLECOLUMNS));
		} catch (InvalidSettingsException e) {
			visibleColumns = new ArrayList<>();
		}

		try {
			modelFilter = settings
					.getString(PredictorViewNodeModel.CFG_MODELFILTER);
		} catch (InvalidSettingsException e) {
			modelFilter = null;
		}

		try {
			dataFilter = settings
					.getString(PredictorViewNodeModel.CFG_DATAFILTER);
		} catch (InvalidSettingsException e) {
			dataFilter = null;
		}

		try {
			fittedFilter = settings
					.getString(PredictorViewNodeModel.CFG_FITTEDFILTER);
		} catch (InvalidSettingsException e) {
			fittedFilter = null;
		}

		try {
			concentrationParameters = XmlConverter
					.xmlToStringMap(settings
							.getString(PredictorViewNodeModel.CFGKEY_CONCENTRATIONPARAMETERS));
		} catch (InvalidSettingsException e) {
			concentrationParameters = new LinkedHashMap<String, String>();
		}

		table = input[0];
		reader = new TableReader(table, concentrationParameters);
		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(createMainComponent());
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		writeSettingsToVariables();

		settings.addString(PredictorViewNodeModel.CFG_SELECTEDID, selectedID);
		settings.addString(PredictorViewNodeModel.CFG_PARAMXVALUES,
				XmlConverter.mapToXml(paramXValues));
		settings.addString(PredictorViewNodeModel.CFG_TIMEVALUES,
				XmlConverter.listToXml(timeValues));
		settings.addString(PredictorViewNodeModel.CFG_COLORS,
				XmlConverter.colorMapToXml(colors));
		settings.addString(PredictorViewNodeModel.CFG_SHAPES,
				XmlConverter.shapeMapToXml(shapes));
		settings.addInt(PredictorViewNodeModel.CFG_MANUALRANGE, manualRange);
		settings.addDouble(PredictorViewNodeModel.CFG_MINX, minX);
		settings.addDouble(PredictorViewNodeModel.CFG_MAXX, maxX);
		settings.addDouble(PredictorViewNodeModel.CFG_MINY, minY);
		settings.addDouble(PredictorViewNodeModel.CFG_MAXY, maxY);
		settings.addInt(PredictorViewNodeModel.CFG_DRAWLINES, drawLines);
		settings.addInt(PredictorViewNodeModel.CFG_SHOWLEGEND, showLegend);
		settings.addInt(PredictorViewNodeModel.CFG_ADDLEGENDINFO, addLegendInfo);
		settings.addInt(PredictorViewNodeModel.CFG_DISPLAYHIGHLIGHTED,
				displayHighlighted);
		settings.addInt(PredictorViewNodeModel.CFG_SHOWCONFIDENCE,
				showConfidence);
		settings.addString(PredictorViewNodeModel.CFG_UNITX, unitX);
		settings.addString(PredictorViewNodeModel.CFG_UNITY, unitY);
		settings.addString(PredictorViewNodeModel.CFG_TRANSFORMY, transformY);
		settings.addInt(PredictorViewNodeModel.CFG_STANDARDVISIBLECOLUMNS,
				standardVisibleColumns);
		settings.addString(PredictorViewNodeModel.CFG_VISIBLECOLUMNS,
				XmlConverter.listToXml(visibleColumns));
		settings.addString(PredictorViewNodeModel.CFG_MODELFILTER, modelFilter);
		settings.addString(PredictorViewNodeModel.CFG_DATAFILTER, dataFilter);
		settings.addString(PredictorViewNodeModel.CFG_FITTEDFILTER,
				fittedFilter);
		settings.addString(
				PredictorViewNodeModel.CFGKEY_CONCENTRATIONPARAMETERS,
				XmlConverter.mapToXml(concentrationParameters));
	}

	private JComponent createMainComponent() {
		if (standardVisibleColumns == 1) {
			visibleColumns = reader.getStandardVisibleColumns();
		}

		configPanel = new ChartConfigPanel(ChartConfigPanel.PARAMETER_FIELDS,
				true, "Change Init Params");

		if (selectedID != null && reader.getPlotables().get(selectedID) != null) {
			Plotable plotable = reader.getPlotables().get(selectedID);

			configPanel.setParamsX(
					plotable.getPossibleArgumentValues(true, true),
					plotable.getMinArguments(), plotable.getMaxArguments(),
					AttributeUtilities.TIME);
			configPanel.setParamY(plotable.getFunctionValue());
			configPanel.setParamXValues(paramXValues);
			configPanel.setUnitX(unitX);
			configPanel.setUnitY(unitY);
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
		configPanel.addExtraButtonListener(this);
		selectionPanel = new ChartSelectionPanel(reader.getIds(), true,
				reader.getStringColumns(), reader.getStringColumnValues(),
				reader.getDoubleColumns(), reader.getDoubleColumnValues(),
				visibleColumns, reader.getStringColumns(), null);
		selectionPanel.setColors(colors);
		selectionPanel.setShapes(shapes);
		selectionPanel.setFilter(Model1Schema.MODELNAME, modelFilter);
		selectionPanel.setFilter(AttributeUtilities.DATAID, dataFilter);
		selectionPanel.setFilter(ChartConstants.STATUS, fittedFilter);
		selectionPanel.addSelectionListener(this);
		chartCreator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());
		infoPanel = new ChartInfoPanel(reader.getIds(),
				reader.getInfoParameters(), reader.getInfoParameterValues());
		samplePanel = new ChartSamplePanel();
		samplePanel.setTimeColumnName(AttributeUtilities
				.getName(AttributeUtilities.TIME));
		samplePanel.setLogcColumnName(AttributeUtilities
				.getName(AttributeUtilities.LOGC));
		samplePanel.setTimeValues(timeValues);
		samplePanel.addEditListener(this);

		if (selectedID != null) {
			selectionPanel.setSelectedIDs(Arrays.asList(selectedID));
		}

		JPanel upperRightPanel = new JPanel();

		upperRightPanel.setLayout(new GridLayout(2, 1));
		upperRightPanel.add(selectionPanel);
		upperRightPanel.add(samplePanel);

		JSplitPane upperSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				chartCreator, upperRightPanel);
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

			configPanel.setParamsX(
					plotable.getPossibleArgumentValues(true, true),
					plotable.getMinArguments(), plotable.getMaxArguments(),
					AttributeUtilities.TIME);
			configPanel.setParamY(plotable.getFunctionValue());
			plotable.setSamples(samplePanel.getTimeValues());
			plotable.setFunctionArguments(configPanel.getParamsX());
			chartCreator.setParamX(configPanel.getParamX());
			chartCreator.setParamY(configPanel.getParamY());
			chartCreator.setUnitX(configPanel.getUnitX());
			chartCreator.setUnitY(configPanel.getUnitY());
			chartCreator.setTransformY(configPanel.getTransformY());

			double[][] samplePoints = plotable.getFunctionSamplePoints(
					AttributeUtilities.TIME, AttributeUtilities.LOGC,
					configPanel.getUnitX(), configPanel.getUnitY(),
					configPanel.getTransformY(), Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY);

			samplePanel.setDataPoints(samplePoints);
		} else {
			configPanel.setParamsX(null, null, null, null);
			configPanel.setParamY(null);
			chartCreator.setParamX(null);
			chartCreator.setParamY(null);
			chartCreator.setUnitX(null);
			chartCreator.setUnitY(null);
			chartCreator.setTransformY(null);
		}

		samplePanel.setTimeColumnName(AttributeUtilities.getNameWithUnit(
				AttributeUtilities.TIME, configPanel.getUnitX()));
		samplePanel.setLogcColumnName(AttributeUtilities.getNameWithUnit(
				AttributeUtilities.LOGC, configPanel.getUnitY(),
				configPanel.getTransformY()));
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
		chartCreator.setColors(selectionPanel.getColors());
		chartCreator.setShapes(selectionPanel.getShapes());
		chartCreator.createChart(selectedID);
	}

	private void writeSettingsToVariables() {
		if (!selectionPanel.getSelectedIDs().isEmpty()) {
			selectedID = selectionPanel.getSelectedIDs().get(0);
		} else {
			selectedID = null;
		}

		paramXValues = configPanel.getParamXValues();
		timeValues = samplePanel.getTimeValues();
		colors = selectionPanel.getColors();
		shapes = selectionPanel.getShapes();

		if (configPanel.isUseManualRange()) {
			manualRange = 1;
		} else {
			manualRange = 0;
		}

		minX = configPanel.getMinX();
		maxX = configPanel.getMaxX();
		minY = configPanel.getMinY();
		maxY = configPanel.getMaxY();

		if (configPanel.isDrawLines()) {
			drawLines = 1;
		} else {
			drawLines = 0;
		}

		if (configPanel.isShowLegend()) {
			showLegend = 1;
		} else {
			showLegend = 0;
		}

		if (configPanel.isAddInfoInLegend()) {
			addLegendInfo = 1;
		} else {
			addLegendInfo = 0;
		}

		if (configPanel.isDisplayFocusedRow()) {
			displayHighlighted = 1;
		} else {
			displayHighlighted = 0;
		}

		if (configPanel.isShowConfidenceInterval()) {
			showConfidence = 1;
		} else {
			showConfidence = 0;
		}

		unitX = configPanel.getUnitX();
		unitY = configPanel.getUnitY();
		transformY = configPanel.getTransformY();
		standardVisibleColumns = 0;
		visibleColumns = selectionPanel.getVisibleColumns();
		modelFilter = selectionPanel.getFilter(Model1Schema.MODELNAME);
		dataFilter = selectionPanel.getFilter(AttributeUtilities.DATAID);
		fittedFilter = selectionPanel.getFilter(ChartConstants.STATUS);
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

	@Override
	public void timeValuesChanged() {
		createChart();
	}

	@Override
	public void buttonPressed() {
		InitParamDialog dialog = new InitParamDialog(getPanel(), table,
				concentrationParameters);

		dialog.setVisible(true);

		if (dialog.isApproved()) {
			concentrationParameters = dialog.getResult();
			writeSettingsToVariables();
			selectedID = null;
			reader = new TableReader(table, concentrationParameters);
			((JPanel) getTab("Options")).removeAll();
			((JPanel) getTab("Options")).add(createMainComponent());
			getPanel().repaint();
		}
	}

	private static class InitParamDialog extends JDialog implements
			ActionListener {

		private static final long serialVersionUID = 1L;

		private static final String NO_PARAM = "";

		private List<String> ids;
		private Map<String, String> modelNames;
		private Map<String, String> formulas;
		private Map<String, List<String>> availableParams;
		private Map<String, JComboBox<String>> paramBoxes;
		private JButton okButton;
		private JButton cancelButton;

		private Map<String, String> result;

		public InitParamDialog(JComponent owner, DataTable table,
				Map<String, String> concentrationParameters) {
			super(JOptionPane.getFrameForComponent(owner),
					"Select Initial Parameter", true);
			readTable(table);

			setLayout(new BorderLayout());
			add(createPanel(concentrationParameters), BorderLayout.CENTER);
			setResizable(false);
			setLocationRelativeTo(owner);
			pack();
		}

		public Map<String, String> getResult() {
			return result;
		}

		public boolean isApproved() {
			return result != null;
		}

		private void readTable(DataTable table) {
			KnimeRelationReader reader = new KnimeRelationReader(
					SchemaFactory.createM1Schema(), table);
			List<KnimeTuple> tuples = new ArrayList<KnimeTuple>();

			while (reader.hasMoreElements()) {
				tuples.add(reader.nextElement());
			}

			Set<String> idSet = new LinkedHashSet<String>();

			ids = new ArrayList<String>();
			modelNames = new LinkedHashMap<String, String>();
			formulas = new LinkedHashMap<String, String>();
			availableParams = new LinkedHashMap<String, List<String>>();

			for (KnimeTuple tuple : tuples) {
				PmmXmlDoc modelXml = tuple
						.getPmmXml(Model1Schema.ATT_MODELCATALOG);
				String id = ((CatalogModelXml) modelXml.get(0)).getID() + "";

				if (!idSet.add(id)) {
					continue;
				}

				List<String> params = new ArrayList<String>();

				params.add(NO_PARAM);
				params.addAll(CellIO.getNameList(tuple
						.getPmmXml(Model1Schema.ATT_PARAMETER)));

				ids.add(id);
				modelNames.put(id,
						((CatalogModelXml) modelXml.get(0)).getName());
				formulas.put(id,
						((CatalogModelXml) modelXml.get(0)).getFormula());
				availableParams.put(id, params);
			}
		}

		private JPanel createPanel(Map<String, String> concentrationParameters) {
			paramBoxes = new LinkedHashMap<String, JComboBox<String>>();
			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			JPanel leftPanel = new JPanel();
			JPanel rightPanel = new JPanel();

			leftPanel.setLayout(new GridLayout(ids.size(), 1, 5, 5));
			leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			rightPanel.setLayout(new GridLayout(ids.size(), 1, 5, 5));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			for (String id : ids) {
				JComboBox<String> box = new JComboBox<String>(availableParams
						.get(id).toArray(new String[0]));
				JLabel label = new JLabel(modelNames.get(id) + ":");

				if (concentrationParameters.get(id) != null) {
					box.setSelectedItem(concentrationParameters.get(id));
				}

				box.setPreferredSize(new Dimension(150,
						box.getPreferredSize().height));
				label.setToolTipText(formulas.get(id));
				paramBoxes.put(id, box);
				leftPanel.add(label);
				rightPanel.add(box);
			}

			JPanel parameterPanel = new JPanel();

			parameterPanel.setLayout(new BorderLayout());
			parameterPanel.add(leftPanel, BorderLayout.WEST);
			parameterPanel.add(rightPanel, BorderLayout.EAST);

			JPanel buttonPanel = new JPanel();

			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			buttonPanel.add(okButton);
			buttonPanel.add(cancelButton);

			JPanel panel = new JPanel();

			panel.setLayout(new BorderLayout());
			panel.add(parameterPanel, BorderLayout.CENTER);
			panel.add(buttonPanel, BorderLayout.SOUTH);

			return panel;
		}

		private Map<String, String> getParameterMap() {
			Map<String, String> parameterMap = new LinkedHashMap<String, String>();

			for (String id : ids) {
				if (!paramBoxes.get(id).getSelectedItem().equals(NO_PARAM)) {
					parameterMap.put(id, (String) paramBoxes.get(id)
							.getSelectedItem());
				} else {
					parameterMap.put(id, null);
				}
			}

			return parameterMap;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == okButton) {
				result = getParameterMap();
				dispose();
			} else if (e.getSource() == cancelButton) {
				dispose();
			}
		}
	}

}
