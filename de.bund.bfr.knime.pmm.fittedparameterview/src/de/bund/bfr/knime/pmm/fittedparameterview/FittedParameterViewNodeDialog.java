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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import de.bund.bfr.knime.pmm.common.chart.ChartAllPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.ui.UI;

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
	private SettingsHelper set;

	private ChartAllPanel mainComponent;
	private ChartCreator chartCreator;
	private ChartSelectionPanel selectionPanel;
	private ChartConfigPanel configPanel;

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
		set = new SettingsHelper();
		set.loadSettings(settings);
		table = input[0];
		reader = new TableReader(table, set.getUsedConditions());
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

		configPanel = new ChartConfigPanel(ChartConfigPanel.PARAMETER_BOXES,
				false, "Conditions");

		if (set.getSelectedID() != null
				&& reader.getPlotables().get(set.getSelectedID()) != null) {
			Plotable plotable = reader.getPlotables().get(set.getSelectedID());

			configPanel.setParameters(plotable.getFunctionValue(),
					plotable.getPossibleArgumentValues(true, false),
					plotable.getMinArguments(), plotable.getMaxArguments(),
					plotable.getCategories(), plotable.getUnits(), null);
			configPanel.setParamX(set.getCurrentParamX());
			configPanel.setUnitX(set.getUnitX());
			configPanel.setUnitY(set.getUnitY());
			configPanel.setSelectedValuesX(set.getSelectedValuesX());
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
		configPanel.setTransformX(set.getTransformX());
		configPanel.setTransformY(set.getTransformY());
		configPanel.addConfigListener(this);
		configPanel.addExtraButtonListener(this);
		selectionPanel = new ChartSelectionPanel(reader.getIds(), true,
				reader.getStringColumns(), reader.getStringColumnValues(),
				null, null, reader.getConditions(), null,
				reader.getConditionMinValues(), reader.getConditionMaxValues(),
				reader.getConditionUnits(), set.getVisibleColumns(),
				reader.getFilterableStringColumns(), null, null, null,
				reader.getColorCounts());
		selectionPanel.setColorLists(set.getColorLists());
		selectionPanel.setShapeLists(set.getShapeLists());
		selectionPanel.setSelectedIDs(Arrays.asList(set.getSelectedID()));
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
			chartCreator.setTransformX(configPanel.getTransformX());
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
			set.setSelectedID(selectionPanel.getSelectedIDs().get(0));
		} else {
			set.setSelectedID(null);
		}

		set.setCurrentParamX(configPanel.getParamX());
		set.setSelectedValuesX(configPanel.getSelectedValuesX());
		set.setColorLists(selectionPanel.getColorLists());
		set.setShapeLists(selectionPanel.getShapeLists());
		set.setManualRange(configPanel.isUseManualRange());
		set.setMinX(configPanel.getMinX());
		set.setMaxX(configPanel.getMaxX());
		set.setMinY(configPanel.getMinY());
		set.setMaxY(configPanel.getMaxY());
		set.setDrawLines(configPanel.isDrawLines());
		set.setShowLegend(configPanel.isShowLegend());
		set.setAddLegendInfo(configPanel.isAddInfoInLegend());
		set.setDisplayHighlighted(configPanel.isDisplayFocusedRow());
		set.setUnitX(configPanel.getUnitX());
		set.setUnitY(configPanel.getUnitY());
		set.setTransformX(configPanel.getTransformX());
		set.setTransformY(configPanel.getTransformY());
		set.setStandardVisibleColumns(false);
		set.setVisibleColumns(selectionPanel.getVisibleColumns());
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
				table, set.getUsedConditions());

		dialog.setVisible(true);

		if (dialog.isApproved()) {
			writeSettingsToVariables();
			set.setUsedConditions(dialog.getResult());
			set.setSelectedID(null);
			reader = new TableReader(table, set.getUsedConditions());

			int divider = mainComponent.getDividerLocation();

			mainComponent = createMainComponent();
			((JPanel) getTab("Options")).removeAll();
			((JPanel) getTab("Options")).add(mainComponent);
			((JPanel) getTab("Options")).revalidate();
			mainComponent.setDividerLocation(divider);
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
			this.allConditions = PmmUtilities.getMiscParams(PmmUtilities
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
			pack();
			
			setLocationRelativeTo(owner);
			UI.adjustDialog(this);
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
