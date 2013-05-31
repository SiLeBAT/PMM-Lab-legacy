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
package de.bund.bfr.knime.pmm.fittedparameterview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.knime.core.data.DataTable;
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
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

/**
 * <code>NodeDialog</code> for the "FittedParameterView" Node.
 * 
 * 
 * This node dialog derives from {@link DefaultNodeSettingsPane} which allows
 * creation of a simple dialog with standard components. If you need a more
 * complex dialog please derive directly from
 * {@link org.knime.core.node.NodeDialogPane}.
 * 
 * @author Christian Thoens
 */
public class FittedParameterViewNodeDialog extends DataAwareNodeDialogPane
		implements ChartSelectionPanel.SelectionListener,
		ChartConfigPanel.ConfigListener, ChartConfigPanel.ExtraButtonListener {

	private DataTable table;
	private TableReader reader;

	private ChartCreator chartCreator;
	private ChartSelectionPanel selectionPanel;
	private ChartConfigPanel configPanel;

	private String selectedID;
	private String currentParamX;
	private Map<String, List<Boolean>> selectedValuesX;
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
	private String unitX;
	private String unitY;
	private String transformY;
	private int standardVisibleColumns;
	private List<String> visibleColumns;
	private List<String> usedConditions;

	/**
	 * New pane for configuring the FittedParameterView node.
	 */
	protected FittedParameterViewNodeDialog() {
		JPanel panel = new JPanel();

		panel.setLayout(new BorderLayout());
		addTab("Options", panel);
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			BufferedDataTable[] input) throws NotConfigurableException {
		try {
			selectedID = settings
					.getString(FittedParameterViewNodeModel.CFG_SELECTEDID);
		} catch (InvalidSettingsException e) {
			selectedID = null;
		}

		try {
			currentParamX = settings
					.getString(FittedParameterViewNodeModel.CFG_CURRENTPARAMX);
		} catch (InvalidSettingsException e) {
			currentParamX = null;
		}

		try {
			selectedValuesX = XmlConverter
					.xmlToObject(
							settings.getString(FittedParameterViewNodeModel.CFG_SELECTEDVALUESX),
							new LinkedHashMap<String, List<Boolean>>());
		} catch (InvalidSettingsException e) {
			selectedValuesX = new LinkedHashMap<>();
		}

		try {
			colorLists = XmlConverter.xmlToColorListMap(settings
					.getString(FittedParameterViewNodeModel.CFG_COLORLISTS));
		} catch (InvalidSettingsException e) {
			colorLists = new LinkedHashMap<>();
		}

		try {
			shapeLists = XmlConverter.xmlToShapeListMap(settings
					.getString(FittedParameterViewNodeModel.CFG_SHAPELISTS));
		} catch (InvalidSettingsException e) {
			shapeLists = new LinkedHashMap<>();
		}

		try {
			manualRange = settings
					.getInt(FittedParameterViewNodeModel.CFG_MANUALRANGE);
		} catch (InvalidSettingsException e) {
			manualRange = FittedParameterViewNodeModel.DEFAULT_MANUALRANGE;
		}

		try {
			minX = settings.getDouble(FittedParameterViewNodeModel.CFG_MINX);
		} catch (InvalidSettingsException e) {
			minX = FittedParameterViewNodeModel.DEFAULT_MINX;
		}

		try {
			maxX = settings.getDouble(FittedParameterViewNodeModel.CFG_MAXX);
		} catch (InvalidSettingsException e) {
			maxX = FittedParameterViewNodeModel.DEFAULT_MAXX;
		}

		try {
			minY = settings.getDouble(FittedParameterViewNodeModel.CFG_MINY);
		} catch (InvalidSettingsException e) {
			minY = FittedParameterViewNodeModel.DEFAULT_MINY;
		}

		try {
			maxY = settings.getDouble(FittedParameterViewNodeModel.CFG_MAXY);
		} catch (InvalidSettingsException e) {
			maxY = FittedParameterViewNodeModel.DEFAULT_MAXY;
		}

		try {
			drawLines = settings
					.getInt(FittedParameterViewNodeModel.CFG_DRAWLINES);
		} catch (InvalidSettingsException e) {
			drawLines = FittedParameterViewNodeModel.DEFAULT_DRAWLINES;
		}

		try {
			showLegend = settings
					.getInt(FittedParameterViewNodeModel.CFG_SHOWLEGEND);
		} catch (InvalidSettingsException e) {
			showLegend = FittedParameterViewNodeModel.DEFAULT_SHOWLEGEND;
		}

		try {
			addLegendInfo = settings
					.getInt(FittedParameterViewNodeModel.CFG_ADDLEGENDINFO);
		} catch (InvalidSettingsException e) {
			addLegendInfo = FittedParameterViewNodeModel.DEFAULT_ADDLEGENDINFO;
		}

		try {
			displayHighlighted = settings
					.getInt(FittedParameterViewNodeModel.CFG_DISPLAYHIGHLIGHTED);
		} catch (InvalidSettingsException e) {
			displayHighlighted = FittedParameterViewNodeModel.DEFAULT_DISPLAYHIGHLIGHTED;
		}

		try {
			unitX = settings.getString(FittedParameterViewNodeModel.CFG_UNITX);
		} catch (InvalidSettingsException e) {
			unitX = null;
		}

		try {
			unitY = settings.getString(FittedParameterViewNodeModel.CFG_UNITY);
		} catch (InvalidSettingsException e) {
			unitY = null;
		}

		try {
			transformY = settings
					.getString(FittedParameterViewNodeModel.CFG_TRANSFORMY);
		} catch (InvalidSettingsException e) {
			transformY = FittedParameterViewNodeModel.DEFAULT_TRANSFORMY;
		}

		try {
			standardVisibleColumns = settings
					.getInt(FittedParameterViewNodeModel.CFG_STANDARDVISIBLECOLUMNS);
		} catch (InvalidSettingsException e) {
			standardVisibleColumns = FittedParameterViewNodeModel.DEFAULT_STANDARDVISIBLECOLUMNS;
		}

		try {
			visibleColumns = XmlConverter
					.xmlToObject(
							settings.getString(FittedParameterViewNodeModel.CFG_VISIBLECOLUMNS),
							new ArrayList<String>());
		} catch (InvalidSettingsException e) {
			visibleColumns = new ArrayList<>();
		}

		try {
			usedConditions = XmlConverter
					.xmlToObject(
							settings.getString(FittedParameterViewNodeModel.CFG_USEDCONDITIONS),
							new ArrayList<String>());
		} catch (InvalidSettingsException e) {
			usedConditions = new ArrayList<>();
		}

		table = input[0];
		reader = new TableReader(table, usedConditions);
		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(createMainComponent());
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		writeSettingsToVariables();

		settings.addString(FittedParameterViewNodeModel.CFG_SELECTEDID,
				selectedID);
		settings.addString(FittedParameterViewNodeModel.CFG_CURRENTPARAMX,
				currentParamX);
		settings.addString(FittedParameterViewNodeModel.CFG_SELECTEDVALUESX,
				XmlConverter.objectToXml(selectedValuesX));
		settings.addString(FittedParameterViewNodeModel.CFG_COLORLISTS,
				XmlConverter.colorListMapToXml(colorLists));
		settings.addString(FittedParameterViewNodeModel.CFG_SHAPELISTS,
				XmlConverter.shapeListMapToXml(shapeLists));
		settings.addInt(FittedParameterViewNodeModel.CFG_MANUALRANGE,
				manualRange);
		settings.addDouble(FittedParameterViewNodeModel.CFG_MINX, minX);
		settings.addDouble(FittedParameterViewNodeModel.CFG_MAXX, maxX);
		settings.addDouble(FittedParameterViewNodeModel.CFG_MINY, minY);
		settings.addDouble(FittedParameterViewNodeModel.CFG_MAXY, maxY);
		settings.addInt(FittedParameterViewNodeModel.CFG_DRAWLINES, drawLines);
		settings.addInt(FittedParameterViewNodeModel.CFG_SHOWLEGEND, showLegend);
		settings.addInt(FittedParameterViewNodeModel.CFG_ADDLEGENDINFO,
				addLegendInfo);
		settings.addInt(FittedParameterViewNodeModel.CFG_DISPLAYHIGHLIGHTED,
				displayHighlighted);
		settings.addString(FittedParameterViewNodeModel.CFG_UNITX, unitX);
		settings.addString(FittedParameterViewNodeModel.CFG_UNITY, unitY);
		settings.addString(FittedParameterViewNodeModel.CFG_TRANSFORMY,
				transformY);
		settings.addInt(
				FittedParameterViewNodeModel.CFG_STANDARDVISIBLECOLUMNS,
				standardVisibleColumns);
		settings.addString(FittedParameterViewNodeModel.CFG_VISIBLECOLUMNS,
				XmlConverter.objectToXml(visibleColumns));
		settings.addString(FittedParameterViewNodeModel.CFG_USEDCONDITIONS,
				XmlConverter.objectToXml(usedConditions));
	}

	private JComponent createMainComponent() {
		if (standardVisibleColumns == 1) {
			visibleColumns = reader.getStandardVisibleColumns();
		}

		configPanel = new ChartConfigPanel(ChartConfigPanel.PARAMETER_BOXES,
				false, "Conditions");

		if (selectedID != null && reader.getPlotables().get(selectedID) != null) {
			Plotable plotable = reader.getPlotables().get(selectedID);

			configPanel.setParameters(plotable.getFunctionValue(),
					plotable.getPossibleArgumentValues(true, false),
					plotable.getMinArguments(), plotable.getMaxArguments(),
					plotable.getCategories(), plotable.getUnits(), null);
			configPanel.setParamX(currentParamX);
			configPanel.setUnitX(unitX);
			configPanel.setUnitY(unitY);
			configPanel.setSelectedValuesX(selectedValuesX);
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
		configPanel.setTransformY(transformY);
		configPanel.addConfigListener(this);
		configPanel.addExtraButtonListener(this);
		selectionPanel = new ChartSelectionPanel(reader.getIds(), true,
				reader.getStringColumns(), reader.getStringColumnValues(),
				reader.getDoubleColumns(), reader.getDoubleColumnValues(),
				visibleColumns, reader.getFilterableStringColumns(), null,
				null, reader.getColorCounts());
		selectionPanel.setColorLists(colorLists);
		selectionPanel.setShapeLists(shapeLists);
		selectionPanel.setSelectedIDs(Arrays.asList(selectedID));
		selectionPanel.addSelectionListener(this);
		chartCreator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());
		createChart();

		return new ChartAllPanel(chartCreator, selectionPanel, configPanel);
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

			configPanel.setParameters(plotable.getFunctionValue(),
					plotable.getPossibleArgumentValues(true, false), null,
					null, plotable.getCategories(), plotable.getUnits(), null);
			chartCreator.setParamX(configPanel.getParamX());
			chartCreator.setParamY(configPanel.getParamY());
			chartCreator.setUnitX(configPanel.getUnitX());
			chartCreator.setUnitY(configPanel.getUnitY());
			chartCreator.setTransformY(configPanel.getTransformY());
			plotable.setFunctionArguments(configPanel.getParamsX());
		} else {
			configPanel.setParameters(null, null, null, null, null, null, null);
			chartCreator.setParamX(null);
			chartCreator.setParamY(null);
			chartCreator.setUnitX(null);
			chartCreator.setUnitY(null);
			chartCreator.setTransformY(null);
		}

		chartCreator.setColorLists(selectionPanel.getColorLists());
		chartCreator.setShapeLists(selectionPanel.getShapeLists());
		chartCreator.setUseManualRange(configPanel.isUseManualRange());
		chartCreator.setMinX(configPanel.getMinX());
		chartCreator.setMinY(configPanel.getMinY());
		chartCreator.setMaxX(configPanel.getMaxX());
		chartCreator.setMaxY(configPanel.getMaxY());
		chartCreator.setDrawLines(configPanel.isDrawLines());
		chartCreator.setShowLegend(configPanel.isShowLegend());
		chartCreator.setAddInfoInLegend(configPanel.isAddInfoInLegend());
		chartCreator.createChart(selectedID);
	}

	private void writeSettingsToVariables() {
		if (!selectionPanel.getSelectedIDs().isEmpty()) {
			selectedID = selectionPanel.getSelectedIDs().get(0);
		} else {
			selectedID = null;
		}

		currentParamX = configPanel.getParamX();
		selectedValuesX = configPanel.getSelectedValuesX();
		colorLists = selectionPanel.getColorLists();
		shapeLists = selectionPanel.getShapeLists();

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

		unitX = configPanel.getUnitX();
		unitY = configPanel.getUnitY();
		transformY = configPanel.getTransformY();
		standardVisibleColumns = 0;
		visibleColumns = selectionPanel.getVisibleColumns();
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
		if (configPanel.isDisplayFocusedRow()) {
			createChart();
		}
	}

	@Override
	public void buttonPressed() {
		UsedConditionsDialog dialog = new UsedConditionsDialog(getPanel(),
				table, usedConditions);

		dialog.setVisible(true);

		if (dialog.isApproved()) {
			usedConditions = dialog.getResult();
			writeSettingsToVariables();
			selectedID = null;
			reader = new TableReader(table, usedConditions);
			((JPanel) getTab("Options")).removeAll();
			((JPanel) getTab("Options")).add(createMainComponent());
			getPanel().repaint();
		}
	}

	private static class UsedConditionsDialog extends JDialog implements
			ActionListener {

		private static final long serialVersionUID = 1L;

		private List<String> allConditions;
		private List<String> usedConditions;

		private JList<String> conditionList;
		private JButton addButton;
		private JButton removeButton;
		private JButton okButton;
		private JButton cancelButton;

		private boolean approved;

		public UsedConditionsDialog(JComponent owner, DataTable table,
				List<String> usedConditions) {
			super(JOptionPane.getFrameForComponent(owner),
					"Conditions to use (for x-axis)", true);
			approved = false;
			this.allConditions = PmmUtilities.getAllMiscParams(PmmUtilities
					.getTuples(table, SchemaFactory.createDataSchema()));
			this.usedConditions = new ArrayList<>();

			for (String cond : usedConditions) {
				if (allConditions.contains(cond)) {
					this.usedConditions.add(cond);
				}
			}

			conditionList = new JList<>(usedConditions.toArray(new String[0]));
			addButton = new JButton("Add");
			addButton.addActionListener(this);
			removeButton = new JButton("Remove");
			removeButton.addActionListener(this);
			okButton = new JButton("OK");
			okButton.addActionListener(this);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);

			JPanel addRemovePanel = new JPanel();

			addRemovePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			addRemovePanel.add(addButton);
			addRemovePanel.add(removeButton);

			JPanel mainPanel = new JPanel();

			mainPanel.setLayout(new BorderLayout());
			mainPanel.add(addRemovePanel, BorderLayout.NORTH);
			mainPanel.add(new JScrollPane(conditionList,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
					BorderLayout.CENTER);

			JPanel okCancelPanel = new JPanel();

			okCancelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			okCancelPanel.add(okButton);
			okCancelPanel.add(cancelButton);

			setLayout(new BorderLayout());
			add(mainPanel, BorderLayout.CENTER);
			add(okCancelPanel, BorderLayout.SOUTH);
			setResizable(false);
			setLocationRelativeTo(owner);
			pack();
		}

		public boolean isApproved() {
			return approved;
		}

		public List<String> getResult() {
			return usedConditions;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == addButton) {
				Object selection = JOptionPane.showInputDialog(this,
						"Select Condition", "Input",
						JOptionPane.QUESTION_MESSAGE, null,
						allConditions.toArray(), allConditions.get(0));

				if (selection != null && !usedConditions.contains(selection)) {
					usedConditions.add((String) selection);
					conditionList.setListData(usedConditions
							.toArray(new String[0]));
				}
			} else if (e.getSource() == removeButton) {
				List<String> selectedConditions = conditionList
						.getSelectedValuesList();

				if (!selectedConditions.isEmpty()) {
					usedConditions.removeAll(selectedConditions);
					conditionList.setListData(usedConditions
							.toArray(new String[0]));
				}
			} else if (e.getSource() == okButton) {
				approved = true;
				dispose();
			} else if (e.getSource() == cancelButton) {
				dispose();
			}
		}
	}

}
