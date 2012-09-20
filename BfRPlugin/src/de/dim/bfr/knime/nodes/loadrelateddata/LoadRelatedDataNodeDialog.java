/*******************************************************************************
 * Copyright (C) 2012 Data In Motion
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
package de.dim.bfr.knime.nodes.loadrelateddata;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.port.PortObjectSpec;

import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.Messwerte;
import de.dim.bfr.external.service.BFRNodeService;
import de.dim.bfr.knime.util.PluginUtils;
import de.dim.knime.bfr.internal.BfRNodePluginActivator;

/**
 * <code>NodeDialog</code> for the "LoadRelatedData" Node.
 * Loads data related to experiments, necessary for using R scripts.
 * 
 * @author Data In Motion
 */
public class LoadRelatedDataNodeDialog extends NodeDialogPane{

	public static final String PRIMARY_GRAPHS = "primary graphs";
	public static final String PRIMARY_FORECASTING = "primary forecasting";
	public static final String PRIMARY_ESTIMATION = "primary estimation";
	public static final String SECONDARY_ESTIMATION = "secondary estimation";
	public static final String SECONDARY_FORECASTING = "secondary forecasting";
	
	private NodeLogger logger = NodeLogger.getLogger(LoadRelatedDataNodeDialog.class);
	private int[] expIds;
	
	private static List<GeschaetztStatistikModell> primGeschModelle;

	private List<Messwerte> data;
	private List<GeschaetztStatistikModell> geschModelleSek;
	
	private ModelsParamsTableModel noModelsModel;
	private int[] selectedPrimModelIDs;
	private PrimaryGraphsPanel primaryGraphsPanel;
	private SecondaryEstimationPanel secondaryEstPanel;
	private SelectOptionsPanel selectOptionsPanel;
	private PrimaryForecastingPanel primaryFCPanel;
	private SecondaryForecastingPanel secondaryFCPanel;
	private int[] selectedModelTypesIndices;
	private HashMap<GeschaetztStatistikModell, GeschaetztStatistikModell> geschModellePrimSekMap;
	
    /**
     * New pane for configuring Preload node dialog.
     * This is just a suggestion to demonstrate possible default dialog
     * components.
     */
    protected LoadRelatedDataNodeDialog() {
    	super.addTab("Options", selectOptionsPanel = new SelectOptionsPanel());
    	primaryGraphsPanel = selectOptionsPanel.getPrimaryGraphsPanel();
    	primaryFCPanel = selectOptionsPanel.getPrimaryForecastingPanel();
    	secondaryFCPanel = selectOptionsPanel.getSecondaryForecastingPanel();
    	selectOptionsPanel.getPrimaryEstimationPanel();
    	secondaryEstPanel = selectOptionsPanel.getSecondaryEstimationPanel();
    }
	
	@Override
	protected void saveSettingsTo(NodeSettingsWO settings)
			throws InvalidSettingsException {
		settings.addString(LoadRelatedDataNodeModel.ACTIVE_COMPONENT, selectOptionsPanel.getActiveComponent());
		
		if (selectOptionsPanel.getActiveComponent().equals(PRIMARY_GRAPHS)) {
			int[] selectedRowsPrimGraph = null;
			if (primaryGraphsPanel.getTable().getSelectedRowCount() != 0) {
				selectedRowsPrimGraph = SelectionHandler.getSelection(primaryGraphsPanel.getTable());
			}
			settings.addIntArray(LoadRelatedDataNodeModel.SELECTED_ROWS_PRIM_GRAPH, selectedRowsPrimGraph);
			int[] primModelIDs = getSelectedIDs(primaryGraphsPanel
					.getTable());
			settings.addIntArray(
					LoadRelatedDataNodeModel.CHOSEN_PRIM_MODEL_IDS,
					primModelIDs);
			settings.addString(LoadRelatedDataNodeModel.CHOOSEN_PLOT_TYPE, primaryGraphsPanel.getPlotType());
		}
		
		if (selectOptionsPanel.getActiveComponent().equals(SECONDARY_ESTIMATION)) {
			selectedPrimModelIDs = secondaryEstPanel.getSelectedPrimModelIDs();
			selectedModelTypesIndices = secondaryEstPanel.getModelTypeList().getSelectedIndices();
			
			if (selectedPrimModelIDs.length > 0)
				settings.addIntArray(
						LoadRelatedDataNodeModel.CHOSEN_PRIM_MODEL_IDS,
						selectedPrimModelIDs);
			if (secondaryEstPanel.getModelNameBox().getSelectedItem().toString()!= null)
				settings.addString(LoadRelatedDataNodeModel.CHOSEN_MODEL_NAME, secondaryEstPanel.getModelNameBox().getSelectedItem().toString());
			if (!secondaryEstPanel.getSelectedParamName().isEmpty()) {
				settings.addString(LoadRelatedDataNodeModel.CHOSEN_PARAM, secondaryEstPanel.getSelectedParamName());
			}
			if (selectedModelTypesIndices!= null && selectedModelTypesIndices.length > 0) {
				settings.addIntArray(LoadRelatedDataNodeModel.CHOSEN_MODEL_TYPES, selectedModelTypesIndices);
				settings.addStringArray(LoadRelatedDataNodeModel.SELECTED_MODELTYPE_STRING, secondaryEstPanel.getSelectedModelTypes());
			}
		}
		
		if (selectOptionsPanel.getActiveComponent().equals(PRIMARY_FORECASTING)) {
			int[] selectedRowsPrimForecast = null;
			if (primaryFCPanel.getTable().getSelectedRowCount() != 0) {
				selectedRowsPrimForecast = SelectionHandler.getSelection(primaryFCPanel.getTable());
			}
			settings.addIntArray(LoadRelatedDataNodeModel.SELECTED_ROWS_PRIM_FORECAST, selectedRowsPrimForecast);
			int[] primModelIDs = getSelectedIDs(primaryFCPanel.getTable());
			settings.addIntArray(LoadRelatedDataNodeModel.CHOSEN_PRIM_MODEL_IDS, primModelIDs);
		}
		
		if (selectOptionsPanel.getActiveComponent().equals(SECONDARY_FORECASTING)) {
			int[] selectedRowsSecForecast = null;
			if (secondaryFCPanel.getTable().getSelectedRowCount() != 0) {
				selectedRowsSecForecast = SelectionHandler.getSelection(secondaryFCPanel.getTable());
			}
			settings.addIntArray(LoadRelatedDataNodeModel.SELECTED_ROWS_SEC_FORECAST, selectedRowsSecForecast);
			int[] secModelIDs = getSelectedIDs(secondaryFCPanel.getTable());
			settings.addIntArray(LoadRelatedDataNodeModel.CHOSEN_SEC_MODEL_IDS, secModelIDs);
		}
				
	}

	@Override
	protected void loadSettingsFrom(NodeSettingsRO settings,
			PortObjectSpec[] specs) throws NotConfigurableException {
//		load settings
		String plotType = "";
		
		int[] selectedRowsPrimGraph = null;
		int[] selectedRowsPrimForecast = null;
		int[] selectedRowsSecForecast = null;
		String modelName = "";
		String paramName = "";
		String activeComponent = "";
		int[] modelTypesIndices = null;
		try {
			expIds = settings.getIntArray(LoadRelatedDataNodeModel.EXPERIMENTS);
			activeComponent = settings.getString(LoadRelatedDataNodeModel.ACTIVE_COMPONENT);
			plotType = settings.getString(LoadRelatedDataNodeModel.CHOOSEN_PLOT_TYPE, LoadRelatedDataNodeModel.PLOT_TYPE_MD);
			selectedRowsPrimGraph = settings.getIntArray(LoadRelatedDataNodeModel.SELECTED_ROWS_PRIM_GRAPH);
			selectedRowsPrimForecast = settings.getIntArray(LoadRelatedDataNodeModel.SELECTED_ROWS_PRIM_FORECAST);
			selectedRowsSecForecast = settings.getIntArray(LoadRelatedDataNodeModel.SELECTED_ROWS_SEC_FORECAST);
			modelName = settings.getString(LoadRelatedDataNodeModel.CHOSEN_MODEL_NAME, "");
			paramName = settings.getString(LoadRelatedDataNodeModel.CHOSEN_PARAM, "");
			modelTypesIndices = settings.getIntArray(LoadRelatedDataNodeModel.CHOSEN_MODEL_TYPES, null);
		} catch (InvalidSettingsException e1) {
			logger.error("Error loading NodeSettings: \n" + e1);
		}
		
//		gather data
		primGeschModelle = new ArrayList<GeschaetztStatistikModell>();
		data = new ArrayList<Messwerte>();
		BFRNodeService service = BfRNodePluginActivator.getBfRService();
		if (expIds != null && expIds.length > 0) {
			for (int i : expIds) {
				primGeschModelle.addAll(service
						.getEstimatedStatisticModellsByExperiment(i));
				if (service.getMesswerteByVersuchsbedingung(i)!=null)
					data.addAll(service.getMesswerteByVersuchsbedingung(i));
			}
		getAllSecModels();
		logger.info("Number of related secondary models: "+geschModelleSek.size());
		logger.info("Number of selected Experiments: " + expIds.length);
		logger.info("Number of related estimated models: " + primGeschModelle.size());
		}
//		PRIMARY_GRAPHS
		primaryGraphsPanel.setTable(new JTable());
		primaryGraphsPanel.getPrimGraphJSP().remove( primaryGraphsPanel.getTable());
//		PRIMARY_FORECASTING
		primaryFCPanel.setTable(new JTable());
		primaryFCPanel.getModelsJSP().remove(primaryFCPanel.getTable());
//		SECONDARY_FORECASTING
		secondaryFCPanel.setTable(new JTable());
		if (primGeschModelle.size() > 0) {
			primaryGraphsPanel.refreshPrimModelsPanel(primGeschModelle, selectedRowsPrimGraph);
			primaryFCPanel.refreshPrimModelsPanel(primGeschModelle, selectedRowsPrimForecast);
			if (geschModelleSek.size() > 0) 
				secondaryFCPanel.refreshSecModelsPanel(geschModelleSek, geschModellePrimSekMap, selectedRowsSecForecast);
			else 
				secondaryFCPanel.refreshNoModelsData(secondaryFCPanel.getTable());
//			SECONDARY_ESTIMATION
			try {
				refreshSecEstPanel();
				if (modelName != null && paramName != null && modelTypesIndices!=null) 
					secondaryEstPanel.restoreSettings(modelName, paramName, modelTypesIndices);
			} catch (InterruptedException e) {
				logger.error("Error loading models: " + e);
			} catch (InvocationTargetException e) {
				logger.error("Error loading models: " + e);
			}
		}else {
			primaryGraphsPanel.refreshNoModelsData(primaryGraphsPanel.getTable());
			primaryFCPanel.refreshNoModelsData(primaryFCPanel.getTable());
			secondaryEstPanel.refreshNoModelsData();
		}
		primaryGraphsPanel.refreshPanel();
		primaryGraphsPanel.updateRadioButtons(isDataAvailable(), isModelsAvailable());
		primaryGraphsPanel.setPlotType(plotType);
		primaryFCPanel.refreshPanel();
		secondaryFCPanel.refreshPanel();
		if (activeComponent != null) {
			selectOptionsPanel.setActiveComponent(activeComponent);
		}
	}

	@SuppressWarnings("unchecked")
	protected void getAllSecModels() {
		geschModelleSek = new ArrayList<GeschaetztStatistikModell>();
		geschModellePrimSekMap = new HashMap<GeschaetztStatistikModell, GeschaetztStatistikModell>();
		List<GeschaetztStatistikModell> sekModels = new ArrayList<GeschaetztStatistikModell>();
		for (GeschaetztStatistikModell primModel : primGeschModelle) {
			sekModels.addAll(BfRNodePluginActivator.getBfRService().getSecEstModelsForPrimEstModel(primModel));
			List<GeschaetztStatistikModell>secModelsUniqueList = PluginUtils.createUniqueList(sekModels, false);
			for (GeschaetztStatistikModell m : secModelsUniqueList)
				geschModellePrimSekMap.put(primModel, m);
		}
		geschModelleSek = PluginUtils.createUniqueList(sekModels, false);
	}

	protected boolean isModelsAvailable() {
		return !(null == primGeschModelle || primGeschModelle.size() == 0);
	}

	protected boolean isDataAvailable() {
		return !(null == data || data.size() == 0);
	}

	protected void refreshSecEstPanel() throws InterruptedException,
			InvocationTargetException {
		secondaryEstPanel.refreshPanel(primGeschModelle);
	}

	protected void refreshNoModelsData(final JTable... table) {
		for (final JTable t : table) {
			final Object[][] rowData = new Object[1][1];
			rowData[0][0] = "no data";
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					noModelsModel = new ModelsParamsTableModel(rowData,
							new String[] { "" });
					DataTableModelListener listener = new DataTableModelListener();
					noModelsModel.addTableModelListener(listener);
					TableModelEvent event = new TableModelEvent(noModelsModel);
					noModelsModel.newDataAvailable(event);
					t.setModel(noModelsModel);
					TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
							noModelsModel);
					t.setRowSorter(sorter);
				}
			});
		}
	}

	protected int[] getSelectedIDs(JTable table) throws InvalidSettingsException {
		if (hasNullModel(table) || hasNoDataMark(table) && table.getSelectedRow()==0) 
			throw new InvalidSettingsException("There is no data available to perform this operation.");
		else if (table != null) {
			int[] selectedRows = table.getSelectedRows();
			int[] selectedGModelIDs = new int[selectedRows.length];
			for (int i = 0; i < table.getSelectedRows().length; i++) {
				selectedGModelIDs[i] = Integer.parseInt(table.getModel()
						.getValueAt(selectedRows[i], 0).toString());
			}
			return selectedGModelIDs;
		} else 
		return null;
		
	}

	private boolean hasNullModel(JTable table) {
		return table.getModel().getColumnCount() == 0;
	}

	private boolean hasNoDataMark(JTable table) {
		return table.getModel().getValueAt(0, 0).equals("no data");
	}

	public static List<GeschaetztStatistikModell> getGeschModelle() {
		return primGeschModelle;
	}

}
