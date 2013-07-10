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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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
import de.bund.bfr.knime.pmm.common.chart.ChartAllPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConstants;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartSamplePanel;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.units.ConvertException;

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
	private SettingsHelper set;

	private ChartAllPanel mainComponent;
	private ChartCreator chartCreator;
	private ChartSelectionPanel selectionPanel;
	private ChartConfigPanel configPanel;
	private ChartSamplePanel samplePanel;

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
		set = new SettingsHelper();
		set.loadSettings(settings);
		table = input[0];
		reader = new TableReader(table, set.getConcentrationParameters());
		mainComponent = createMainComponent();
		((JPanel) getTab("Options")).removeAll();
		((JPanel) getTab("Options")).add(mainComponent);
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		writeSettingsToVariables();
		set.saveSettings(settings);
	}

	private ChartAllPanel createMainComponent() {
		if (set.isStandardVisibleColumns()) {
			set.setVisibleColumns(reader.getStandardVisibleColumns());
		}

		configPanel = new ChartConfigPanel(ChartConfigPanel.PARAMETER_FIELDS,
				true, "Change Init Params");

		if (set.getSelectedID() != null
				&& reader.getPlotables().get(set.getSelectedID()) != null) {
			Plotable plotable = reader.getPlotables().get(set.getSelectedID());

			configPanel.setParameters(plotable.getFunctionValue(),
					plotable.getPossibleArgumentValues(true, true),
					plotable.getMinArguments(), plotable.getMaxArguments(),
					plotable.getCategories(), plotable.getUnits(),
					AttributeUtilities.TIME);
			configPanel.setParamXValues(set.getParamXValues());
			configPanel.setUnitX(set.getUnitX());
			configPanel.setUnitY(set.getUnitY());
		}

		configPanel.setUseManualRange(set.isManualRange());
		configPanel.setMinX(set.getMinX());
		configPanel.setMaxX(set.getMaxX());
		configPanel.setMinY(set.getMinY());
		configPanel.setMaxY(set.getMaxY());
		configPanel.setDrawLines(set.isDrawLines());
		configPanel.setShowLegend(set.isShowLegend());
		configPanel.setAddInfoInLegend(set.isAddLegendInfo());
		configPanel.setDisplayFocusedRow(set.isDisplayHighlighted());
		configPanel.setShowConfidenceInterval(set.isShowConfidence());
		configPanel.setTransformY(set.getTransformY());
		configPanel.addConfigListener(this);
		configPanel.addExtraButtonListener(this);
		selectionPanel = new ChartSelectionPanel(reader.getIds(), true,
				reader.getStringColumns(), reader.getStringColumnValues(),
				reader.getDoubleColumns(), reader.getDoubleColumnValues(),
				reader.getConditions(), reader.getConditionValues(), null,
				null, reader.getConditionUnits(), set.getVisibleColumns(),
				reader.getFilterableStringColumns(), null,
				reader.getParameterData(), reader.getFormulas());
		selectionPanel.setColors(set.getColors());
		selectionPanel.setShapes(set.getShapes());
		selectionPanel.setFilter(Model1Schema.MODELNAME, set.getModelFilter());
		selectionPanel
				.setFilter(AttributeUtilities.DATAID, set.getDataFilter());
		selectionPanel.setFilter(ChartConstants.STATUS, set.getFittedFilter());
		selectionPanel.addSelectionListener(this);
		chartCreator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());
		samplePanel = new ChartSamplePanel();
		samplePanel.setTimeColumnName(AttributeUtilities
				.getName(AttributeUtilities.TIME));
		samplePanel.setLogcColumnName(AttributeUtilities
				.getName(AttributeUtilities.CONCENTRATION));
		samplePanel.setTimeValues(set.getTimeValues());
		samplePanel.addEditListener(this);

		if (set.getSelectedID() != null) {
			selectionPanel.setSelectedIDs(Arrays.asList(set.getSelectedID()));
		}

		return new ChartAllPanel(chartCreator, selectionPanel, configPanel,
				samplePanel);
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
					plotable.getPossibleArgumentValues(true, true),
					plotable.getMinArguments(), plotable.getMaxArguments(),
					plotable.getCategories(), plotable.getUnits(),
					AttributeUtilities.TIME);
			plotable.setSamples(samplePanel.getTimeValues());
			plotable.setFunctionArguments(configPanel.getParamsX());
			chartCreator.setParamX(configPanel.getParamX());
			chartCreator.setParamY(configPanel.getParamY());
			chartCreator.setUnitX(configPanel.getUnitX());
			chartCreator.setUnitY(configPanel.getUnitY());
			chartCreator.setTransformY(configPanel.getTransformY());

			try {
				samplePanel.setDataPoints(plotable.getFunctionSamplePoints(
						AttributeUtilities.TIME,
						AttributeUtilities.CONCENTRATION,
						configPanel.getUnitX(), configPanel.getUnitY(),
						configPanel.getTransformY(), Double.NEGATIVE_INFINITY,
						Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
						Double.POSITIVE_INFINITY));
			} catch (ConvertException e) {
				e.printStackTrace();
			}
		} else {
			configPanel.setParameters(null, null, null, null, null, null, null);
			chartCreator.setParamX(null);
			chartCreator.setParamY(null);
			chartCreator.setUnitX(null);
			chartCreator.setUnitY(null);
			chartCreator.setTransformY(null);
		}

		samplePanel.setTimeColumnName(AttributeUtilities.getNameWithUnit(
				AttributeUtilities.TIME, configPanel.getUnitX()));
		samplePanel.setLogcColumnName(AttributeUtilities.getNameWithUnit(
				AttributeUtilities.CONCENTRATION, configPanel.getUnitY(),
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
			set.setSelectedID(selectionPanel.getSelectedIDs().get(0));
		} else {
			set.setSelectedID(null);
		}

		set.setParamXValues(configPanel.getParamXValues());
		set.setTimeValues(samplePanel.getTimeValues());
		set.setColors(selectionPanel.getColors());
		set.setShapes(selectionPanel.getShapes());
		set.setManualRange(configPanel.isUseManualRange());
		set.setMinX(configPanel.getMinX());
		set.setMaxX(configPanel.getMaxX());
		set.setMinY(configPanel.getMinY());
		set.setMaxY(configPanel.getMaxY());
		set.setDrawLines(configPanel.isDrawLines());
		set.setShowLegend(configPanel.isShowLegend());
		set.setAddLegendInfo(configPanel.isAddInfoInLegend());
		set.setDisplayHighlighted(configPanel.isDisplayFocusedRow());
		set.setShowConfidence(configPanel.isShowConfidenceInterval());
		set.setUnitX(configPanel.getUnitX());
		set.setUnitY(configPanel.getUnitY());
		set.setTransformY(configPanel.getTransformY());
		set.setStandardVisibleColumns(false);
		set.setVisibleColumns(selectionPanel.getVisibleColumns());
		set.setModelFilter(selectionPanel.getFilter(Model1Schema.MODELNAME));
		set.setDataFilter(selectionPanel.getFilter(AttributeUtilities.DATAID));
		set.setFittedFilter(selectionPanel.getFilter(ChartConstants.STATUS));
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
	public void timeValuesChanged() {
		createChart();
	}

	@Override
	public void buttonPressed() {
		InitParamDialog dialog = new InitParamDialog(getPanel(), table,
				set.getConcentrationParameters());

		dialog.setVisible(true);

		if (dialog.isApproved()) {
			writeSettingsToVariables();
			set.setConcentrationParameters(dialog.getResult());
			set.setSelectedID(null);
			reader = new TableReader(table, set.getConcentrationParameters());

			int divider = mainComponent.getDividerLocation();

			mainComponent = createMainComponent();
			((JPanel) getTab("Options")).removeAll();
			((JPanel) getTab("Options")).add(mainComponent);
			((JPanel) getTab("Options")).revalidate();
			mainComponent.setDividerLocation(divider);
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
