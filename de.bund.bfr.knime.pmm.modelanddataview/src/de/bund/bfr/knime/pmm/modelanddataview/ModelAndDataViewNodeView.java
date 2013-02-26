/*******************************************************************************
 * PMM-Lab � 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab � 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * J�rgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Th�ns (BfR)
 * Annemarie K�sbohrer (BfR)
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
package de.bund.bfr.knime.pmm.modelanddataview;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.knime.core.data.DataTable;
import org.knime.core.node.NodeView;

import de.bund.bfr.knime.pmm.common.chart.ChartConfigPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartCreator;
import de.bund.bfr.knime.pmm.common.chart.ChartInfoPanel;
import de.bund.bfr.knime.pmm.common.chart.ChartSelectionPanel;
import de.bund.bfr.knime.pmm.common.chart.Plotable;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

/**
 * <code>NodeView</code> for the "ModelAndDataView" Node.
 * 
 * 
 * @author Christian Thoens
 */
public class ModelAndDataViewNodeView extends
		NodeView<ModelAndDataViewNodeModel> implements
		ChartSelectionPanel.SelectionListener, ChartConfigPanel.ConfigListener {

	private ChartCreator chartCreator;
	private ChartSelectionPanel selectionPanel;
	private ChartConfigPanel configPanel;
	private ChartInfoPanel infoPanel;

	/**
	 * Creates a new view.
	 * 
	 * @param nodeModel
	 *            The model (class: {@link ModelAndDataViewNodeModel})
	 */
	protected ModelAndDataViewNodeView(final ModelAndDataViewNodeModel nodeModel) {
		super(nodeModel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void modelChanged() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onClose() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onOpen() {
		setComponent(createComponent());
	}

	private JComponent createComponent() {
		DataTable table = getNodeModel().getTable();
		TableReader reader;

		if (SchemaFactory.createDataSchema().conforms(table)) {
			reader = new TableReader(table, true);			
		} else {
			reader = new TableReader(table, false);
		}

		configPanel = new ChartConfigPanel(ChartConfigPanel.PARAMETER_FIELDS,
				true);
		configPanel.addConfigListener(this);
		selectionPanel = new ChartSelectionPanel(reader.getIds(), true,
				reader.getStringColumns(), reader.getStringColumnValues(),
				reader.getDoubleColumns(), reader.getDoubleColumnValues(),
				reader.getVisibleColumns(), reader.getStringColumns());
		selectionPanel.addSelectionListener(this);
		chartCreator = new ChartCreator(reader.getPlotables(),
				reader.getShortLegend(), reader.getLongLegend());
		infoPanel = new ChartInfoPanel(reader.getIds(),
				reader.getInfoParameters(), reader.getInfoParameterValues());

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
			Map<String, List<Double>> variables = new LinkedHashMap<String, List<Double>>();

			for (String var : plotable.getFunctionArguments().keySet()) {
				if (plotable.getValueList(var) != null) {
					Set<Double> valuesSet = new LinkedHashSet<Double>(
							plotable.getValueList(var));
					List<Double> valuesList = new ArrayList<Double>(valuesSet);

					Collections.sort(valuesList);
					variables.put(var, valuesList);
				} else {
					variables.put(var, new ArrayList<Double>());
				}
			}

			configPanel.setParamsX(variables, plotable.getMinArguments(),
					plotable.getMaxArguments(), null);
			configPanel.setParamsY(Arrays.asList(plotable.getFunctionValue()));
			plotable.setFunctionArguments(configPanel.getParamsX());
			chartCreator.setParamX(configPanel.getParamX());
			chartCreator.setParamY(configPanel.getParamY());
			chartCreator.setUnitX(configPanel.getUnitX());
			chartCreator.setUnitY(configPanel.getUnitY());
			chartCreator.setTransformY(configPanel.getTransformY());
		} else {
			configPanel.setParamsX(null, null, null, null);
			configPanel.setParamsY(null);
			chartCreator.setParamX(null);
			chartCreator.setParamY(null);
			chartCreator.setUnitX(null);
			chartCreator.setUnitY(null);
			chartCreator.setTransformY(null);
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
		chartCreator.setColors(selectionPanel.getColors());
		chartCreator.setShapes(selectionPanel.getShapes());
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
