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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;

import de.dim.bfr.Einheiten;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.Messwerte;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.VersuchsBedingung;
import de.dim.bfr.external.service.BFRNodeService;
import de.dim.bfr.knime.ports.BufferedTableContainer;
import de.dim.bfr.knime.util.EMFUtils;
import de.dim.bfr.knime.util.PluginUtils;
import de.dim.knime.bfr.internal.BfRNodePluginActivator;

/**
 * This is the model implementation of LoadRelatedData. Loads data related to
 * experiments, necessary for using R scripts.
 * 
 * @author Data In Motion
 */
public class LoadRelatedDataNodeModel extends NodeModel {

	private static final NodeLogger logger = NodeLogger
			.getLogger(LoadRelatedDataNodeModel.class);

	public static final String EXPERIMENTS = "EXPERIMENTS";
	public static final String CHOSEN_G_MODEL_IDS = "CHOSEN_G_MODEL_IDS";
	public static final String CHOSEN_PRIM_MODEL_IDS = "CHOSEN_PRIM_MODEL_IDS";
	public static final String CHOSEN_SEC_MODEL_IDS = "CHOSEN_SEC_MODEL_IDS";
	public static final String CHOSEN_PARAM = "CHOSEN_PARAM";
	public static final String CHOOSEN_PLOT_TYPE = "CHOOSEN_PLOT_TYPE";
	public static final String PLOT_TYPE_MD = "MD";
	public static final String PLOT_TYPE_M = "M";
	public static final String PLOT_TYPE_D = "D";

	public static final String CHOSEN_MODEL_TYPES = "CHOSEN_MODEL_TYPES";

	public static final String SELECTED_ROWS_PRIM_GRAPH = "SELECTED_ROWS_PRIM_GRAPH";
	public static final String SELECTED_ROWS_PRIM_FORECAST = "SELECTED_ROWS_PRIM_FORECAST";

	public static final String SELECTED_ROWS_SEC_FORECAST = "SELECTED_ROWS_SEC_FORECAST";

	public static final String ACTIVE_COMPONENT = "ACTIVE_COMPONENT";

	public static final String CHOSEN_MODEL_NAME = "CHOSEN_MODEL_NAME";

	public static final String SELECTED_MODELTYPE_STRING = "SELECTED_MODELTYPE_STRING";

	private BufferedDataTable expTable;
	private int[] expIds;
	private String param;
	private int[] selectedRowsPrimGraph;
	private String plotType;

	private String fileName;

	private int[] primModelIDs;
	private int[] secModelIDs;

	private int[] selectedRowsPrimForecast;

	private int[] selectedRowsSecForecast;

	private String[] modelTypes;

	private String activeComponent;

	private List<VersuchsBedingung> experiments;

	private String modelName;

	private String paramName;

	private int[] modelTypesIndices;

	protected LoadRelatedDataNodeModel() {
		super(new PortType[] { BufferedDataTable.TYPE },
				new PortType[] { BufferedTableContainer.TYPE });
		this.plotType = PLOT_TYPE_MD;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {
		logger.info("Executing preload of data...");
		if (activeComponent != null)
			pushFlowVariableString(PluginUtils.ACTIVE_COMPONENT_LRDND, activeComponent);
		if (plotType != null && plotType.equals(PLOT_TYPE_M))
			pushFlowVariableString(PluginUtils.PLOT_TYPE, plotType);
		BFRNodeService service = BfRNodePluginActivator.getBfRService();

		BufferedTableContainer container = new BufferedTableContainer();
		try {
			expTable = (BufferedDataTable) inData[0];
		} catch (Exception e) {
			expTable = null;
			logger.error("Parameter data in inport is invaild!");
		}

		experiments = new ArrayList<VersuchsBedingung>();
		for (int i = 0; i < expTable.getDataTableSpec().getNumColumns(); i++) {
			if (expTable.getDataTableSpec().getColumnSpec(i).getName()
					.equals("ID")) {
				RowIterator rowIter = expTable.iterator();
				int rowCounter = 0;
				while (rowIter.hasNext()) {
					DataRow row = rowIter.next();
					Iterator<DataCell> colIter = row.iterator();
					int counter = 0;
					while (colIter.hasNext() && counter < 1) {
						DataCell cell = colIter.next();
						if (cell.toString() != null) {
							if (expIds==null) {
								logger.error("Refresh the database reader node to continue with your workflow.");
								throw new InvalidSettingsException("Cannot load the selection of conditions. KNIME was probably restarted. Refresh the database reader to continue.");
							}
							expIds[rowCounter] = Integer.parseInt(cell.toString());
							counter++;
							rowCounter++;
						} else {
							setWarningMessage("No conditions were selected. Most probably a restart of KNIME marked the workflow's configuration obsolete.");
						}
					}
				}
			}
		}
		for (Integer id : expIds) {
			service.getAllVersuchbedingungen();
			VersuchsBedingung exp = service.getVersuchsbedingungByID(id);
			exp.getId();
			experiments.add(exp);
		}

		if (activeComponent!= null)
			container = preload(exec);
		else {
			throw new InvalidSettingsException("Node must be configured!");
		}

		return new PortObject[] { container };
	}

	/**
	 * Preload data necessary for specific calls to R.
	 * @param exec the node's {@link ExecutionContext}
	 * @return a {@link BufferedTableContainer}
	 * @throws Exception
	 */
	protected BufferedTableContainer preload(ExecutionContext exec)
			throws Exception {
		
		BufferedTableContainer container = new BufferedTableContainer();

		if (!activeComponent.equals(LoadRelatedDataNodeDialog.PRIMARY_FORECASTING) && !activeComponent.equals(LoadRelatedDataNodeDialog.PRIMARY_GRAPHS)) {
			BufferedDataTable statModelle = PluginUtils.readStatistikModellFromDB(exec);	
			container.addTableFile(PluginUtils.MODELLKATALOG, PluginUtils.writeIntoCsvFile(statModelle, exec));
			BufferedDataTable parameters = PluginUtils.readStatistikModellParameterFromDB(exec);
			container.addTableFile(PluginUtils.MODELLKATALOGPARAMETER, PluginUtils.writeIntoCsvFile(parameters, exec));
		}
		if (activeComponent.equals(LoadRelatedDataNodeDialog.PRIMARY_GRAPHS)) 
			preloadPrimaryGraphs(container, exec);
		if (activeComponent.equals(LoadRelatedDataNodeDialog.PRIMARY_ESTIMATION)) 
			preloadPrimaryEstimation(container, exec);
		if (activeComponent.equals(LoadRelatedDataNodeDialog.SECONDARY_ESTIMATION)) 
			preloadSecondaryEstimation(container, exec);
		if (activeComponent.equals(LoadRelatedDataNodeDialog.PRIMARY_FORECASTING)) 
			preloadPrimaryForecasting(container, exec);
		if (activeComponent.equals(LoadRelatedDataNodeDialog.SECONDARY_FORECASTING)) 
			preloadSecondaryForecasting(container, exec);
		
		return container;
	}

	/**
	 * All data required for <em>secondary forecasting<em> is added to the table container.
	 * @param container the {@link BufferedTableContainer} the node will return
	 * @param exec the node's {@link ExecutionContext}
	 * @throws CanceledExecutionException
	 * @throws IOException
	 * @throws InvalidSettingsException 
	 */
	private void preloadSecondaryForecasting(BufferedTableContainer container,
			ExecutionContext exec) throws CanceledExecutionException, IOException, InvalidSettingsException {
//			EST_MODELS & EST_PARAMS secondary models and params by SELECTION
			List<GeschaetztStatistikModell> geschModellList = new ArrayList<GeschaetztStatistikModell>();
			if (secModelIDs != null && secModelIDs.length > 0) {
				for (int id : secModelIDs)
					geschModellList.add(BfRNodePluginActivator.getBfRService().getEstimatedStatisticModellsByID(id));
				BufferedDataTable secEstModelsTable = EMFUtils .createBufferedDataTableFromEstimatedModels(geschModellList, exec);
				container.addTableFile(PluginUtils.GES_MODELL, PluginUtils.writeIntoCsvFile(secEstModelsTable, exec)); 
				BufferedDataTable secEstParamsTable = EMFUtils.createBufferedDataTableFromEstimatedParameters(geschModellList, exec);
				container.addTableFile(PluginUtils.GES_MODELL_PARAMETER, PluginUtils.writeIntoCsvFile(secEstParamsTable, exec));
			} else 
				throw new InvalidSettingsException("No models selected.");
	}

	/**
		 * All data required for <em>secondary estimation<em> will be added to the table container.
		 * @param container the {@link BufferedTableContainer} the node will return
		 * @param exec the node's {@link ExecutionContext}
		 * @throws CanceledExecutionException
		 * @throws Exception
		 */
		private void preloadSecondaryEstimation(BufferedTableContainer container,
				ExecutionContext exec) throws CanceledExecutionException, Exception {
	//			CONDITIONS - selection/full
				BufferedDataTable conditions = PluginUtils.readConditionsFromDBWhereIdIN(expIds, exec);
				container.addTableFile(PluginUtils.VERSUCHSBEDINGUNGEN, PluginUtils.writeIntoCsvFile(conditions, exec));
	//			NUMBERS - full
				BufferedDataTable doublekennzahlenTable = PluginUtils.readDoublekennzahlenFromDB(exec);
				container.addTableFile(PluginUtils.DOUBLEKENNZAHLEN, PluginUtils.writeIntoCsvFile(doublekennzahlenTable, exec));		
	//			EST_MODELS extract by CONDITIONS by selection 
				List<GeschaetztStatistikModell> estModelsList = new ArrayList<GeschaetztStatistikModell>();
				if (primModelIDs == null || primModelIDs.length < 1) {
					throw new InvalidSettingsException("No Models selected. Cannot load related data.");
				}
				else {
					for (int i : primModelIDs) 
						estModelsList.add(BfRNodePluginActivator.getBfRService().getEstimatedStatisticModellsByID(i));
					@SuppressWarnings("unchecked")
					List<GeschaetztStatistikModell> uniqueEstModelsList = PluginUtils.createUniqueList(estModelsList, false);
					BufferedDataTable estModelsTable = EMFUtils.createBufferedDataTableFromEstimatedModels(uniqueEstModelsList, exec);
					container.addTableFile(PluginUtils.GES_MODELL, PluginUtils.writeIntoCsvFile(estModelsTable, exec));
		//			EST_PARAMS extract by CONDITIONS
					BufferedDataTable estParamsTable = EMFUtils.createBufferedDataTableFromEstimatedParameters(uniqueEstModelsList, exec);
					container.addTableFile(PluginUtils.GES_MODELL_PARAMETER, PluginUtils.writeIntoCsvFile(estParamsTable, exec));
		//			EST_COVCOR extract by CONDITIONS
					BufferedDataTable covCorTable = EMFUtils.createBufferedDataTableFromParameterCovCor(uniqueEstModelsList, exec);
					container.addTableFile(PluginUtils.COVCOR, PluginUtils.writeIntoCsvFile(covCorTable, exec));
		//			PRIMARYSECONDARY
					int[] primIds = new int[estModelsList.size()];
					for (int i = 0; i < estModelsList.size(); i++) {
						primIds[i] = estModelsList.get(i).getId();
					}
					BufferedDataTable primSecTable = PluginUtils.readPrimSecFromDBWherePrim(primIds, exec);
					container.addTableFile(PluginUtils.PRIMARYSECONDARY, PluginUtils.writeIntoCsvFile(primSecTable, exec));
		//			type	
					if (modelTypes != null && modelTypes.length > 0)
						sendModelTypes();
		//			primparameter
					if (param != null && !param.isEmpty())
						sendParams();
				}
		}

	/**
	 * All data required for <em>primary forecasting<em> will be added to the table container.
	 * @param container the {@link BufferedTableContainer} the node will return
	 * @param exec the node's {@link ExecutionContext}
	 * @throws CanceledExecutionException
	 * @throws IOException
	 */
	private void preloadPrimaryForecasting(BufferedTableContainer container,
			ExecutionContext exec) throws CanceledExecutionException, IOException {
//			EST_MODELS & EST_PARAMS extract by CONDITIONS by SELECTION
			List<GeschaetztStatistikModell> geschModellList = new ArrayList<GeschaetztStatistikModell>();
			if (primModelIDs != null) {
				for (int id : primModelIDs) 
					geschModellList.add(BfRNodePluginActivator .getBfRService().getEstimatedStatisticModellsByID(id));
				BufferedDataTable geschModelleTable = EMFUtils .createBufferedDataTableFromEstimatedModels(geschModellList, exec);
				BufferedDataTable geschParameterTable = EMFUtils .createBufferedDataTableFromEstimatedParameters( geschModellList, exec);
				container.addTableFile(PluginUtils.GES_MODELL, PluginUtils.writeIntoCsvFile(geschModelleTable, exec));
				container.addTableFile(PluginUtils.GES_MODELL_PARAMETER, PluginUtils.writeIntoCsvFile(geschParameterTable, exec));
			}
//			MODELCATALOG & PARAMETERCATALOG by selected EST_MODELS
			List<StatistikModell> statModelsList = new ArrayList<StatistikModell>();
			for (GeschaetztStatistikModell m : geschModellList) 
				statModelsList.add(m.getStatistikModel());
			@SuppressWarnings("unchecked")
			List<StatistikModell> uniqueStatModelsList = PluginUtils.createUniqueList(statModelsList, false);
			BufferedDataTable statModelsTable = EMFUtils.createBufferedDataTableFromModel(uniqueStatModelsList, exec);
			BufferedDataTable paramsTable = EMFUtils.createBufferedDataTableFromModelParameter(uniqueStatModelsList, exec);
			container.addTableFile(PluginUtils.MODELLKATALOG, PluginUtils.writeIntoCsvFile(statModelsTable, exec));
			container.addTableFile(PluginUtils.MODELLKATALOGPARAMETER, PluginUtils.writeIntoCsvFile(paramsTable, exec));
	}

	/**
	 * All data required for <em>primary estimation<em> will be added to the table container.
	 * @param container the {@link BufferedTableContainer} the node will return
	 * @param exec the node's {@link ExecutionContext}
	 * @throws CanceledExecutionException
	 * @throws Exception
	 */
	private void preloadPrimaryEstimation(BufferedTableContainer container,
			ExecutionContext exec) throws CanceledExecutionException, Exception {
//			CONDITIONS - selection/full
			BufferedDataTable conditions = PluginUtils.readConditionsFromDBWhereIdIN(expIds, exec);
			container.addTableFile(PluginUtils.VERSUCHSBEDINGUNGEN, PluginUtils.writeIntoCsvFile(conditions, exec));
//			MEASURED - extract by CONDITION
			BufferedDataTable messwerteTable;
			List<Messwerte> messwerteList = new ArrayList<Messwerte>();
			for (VersuchsBedingung vb : experiments)  
				messwerteList.addAll(BfRNodePluginActivator.getBfRService().getMesswerteByVersuchsbedingung(vb.getId()));
			messwerteTable = EMFUtils.createBufferedDataTableFromMesswerte(messwerteList, exec);
			if (messwerteTable != null && (plotType.equals(PLOT_TYPE_D) || plotType.equals(PLOT_TYPE_MD)))
			container.addTableFile(PluginUtils.MESSWERTE, PluginUtils.writeIntoCsvFile(messwerteTable, exec));
//			UNITS - full
			List<Einheiten> units = BfRNodePluginActivator.getBfRService().getAllEinheiten();	
			BufferedDataTable einheitenTable = EMFUtils .createBufferedDataTableFromEinheiten(units, exec);
			container.addTableFile(PluginUtils.EINHEITEN, PluginUtils.writeIntoCsvFile(einheitenTable, exec));
//			NUMBERS - full
			BufferedDataTable doublekennzahlenTable = PluginUtils.readDoublekennzahlenFromDB(exec);
			container.addTableFile(PluginUtils.DOUBLEKENNZAHLEN, PluginUtils.writeIntoCsvFile(doublekennzahlenTable, exec));
//			EST_MODELS extract by CONDITIONS 
			List<GeschaetztStatistikModell> estModelsList = new ArrayList<GeschaetztStatistikModell>();
			for (int i : expIds) 
				estModelsList.addAll(BfRNodePluginActivator.getBfRService().getEstimatedStatisticModellsByExperiment(i));
			@SuppressWarnings("unchecked")
			List<GeschaetztStatistikModell> uniqueEstModelsList = PluginUtils.createUniqueList(estModelsList, false);
			BufferedDataTable estModelsTable = EMFUtils.createBufferedDataTableFromEstimatedModels(uniqueEstModelsList, exec);
			container.addTableFile(PluginUtils.GES_MODELL, PluginUtils.writeIntoCsvFile(estModelsTable, exec));
//			EST_PARAMS extract by CONDITIONS
			BufferedDataTable estParamsTable = EMFUtils.createBufferedDataTableFromEstimatedParameters(uniqueEstModelsList, exec);
			container.addTableFile(PluginUtils.GES_MODELL_PARAMETER, PluginUtils.writeIntoCsvFile(estParamsTable, exec));
//			EST_COVCOR extract by CONDITIONS
			BufferedDataTable covCorTable = EMFUtils.createBufferedDataTableFromParameterCovCor(uniqueEstModelsList, exec);
			container.addTableFile(PluginUtils.COVCOR, PluginUtils.writeIntoCsvFile(covCorTable, exec));	
	}

	/**
	 * All data required for <em>primary graphs<em> will be added to the table container.
	 * @param container the {@link BufferedTableContainer} the node will return
	 * @param exec the node's {@link ExecutionContext}
	 * @throws Exception
	 */
	private void preloadPrimaryGraphs(BufferedTableContainer container, ExecutionContext exec) throws Exception {
	
//			CONDITIONS - selection/full
			BufferedDataTable conditions = PluginUtils.readConditionsFromDBWhereIdIN(expIds, exec);
			container.addTableFile(PluginUtils.VERSUCHSBEDINGUNGEN, PluginUtils.writeIntoCsvFile(conditions, exec));
//			
//			MODELCATALOG selection by CONDITION/null by plotType
			BufferedDataTable statModelle = PluginUtils.readStatistikModellFromDB(exec);	
			if (statModelle != null && (plotType.equals(PLOT_TYPE_M) || plotType .equals(PLOT_TYPE_MD)))
				container.addTableFile(PluginUtils.MODELLKATALOG, PluginUtils.writeIntoCsvFile(statModelle, exec));
//			PARAMETERCATALOG selection by CONDITION/null by plotType
			BufferedDataTable parameters = PluginUtils.readStatistikModellParameterFromDB(exec);
			if (parameters != null && (plotType.equals(PLOT_TYPE_M) || plotType .equals(PLOT_TYPE_MD)))
				container.addTableFile(PluginUtils.MODELLKATALOGPARAMETER, PluginUtils.writeIntoCsvFile(parameters, exec));
//			MEASURED - extract by CONDITION/null by plotType
			BufferedDataTable messwerteTable;
			List<Messwerte> messwerteList = new ArrayList<Messwerte>();
			for (VersuchsBedingung vb : experiments)  
				messwerteList.addAll(BfRNodePluginActivator.getBfRService().getMesswerteByVersuchsbedingung(vb.getId()));
			messwerteTable = EMFUtils.createBufferedDataTableFromMesswerte(messwerteList, exec);
			if (messwerteTable != null && (plotType.equals(PLOT_TYPE_D) || plotType.equals(PLOT_TYPE_MD)))
			container.addTableFile(PluginUtils.MESSWERTE, PluginUtils.writeIntoCsvFile(messwerteTable, exec));
//			UNITS - full
			List<Einheiten> units = BfRNodePluginActivator.getBfRService().getAllEinheiten();	
			BufferedDataTable einheitenTable = EMFUtils .createBufferedDataTableFromEinheiten(units, exec);
			container.addTableFile(PluginUtils.EINHEITEN, PluginUtils.writeIntoCsvFile(einheitenTable, exec));
//			NUMBERS - full
			BufferedDataTable doublekennzahlenTable = PluginUtils.readDoublekennzahlenFromDB(exec);
			container.addTableFile(PluginUtils.DOUBLEKENNZAHLEN, PluginUtils.writeIntoCsvFile(doublekennzahlenTable, exec));
//			EST_MODELS & EST_PARAMS extract by CONDITIONS by SELECTION/null by plotType
			List<GeschaetztStatistikModell> geschModellList = new ArrayList<GeschaetztStatistikModell>();
			if (primModelIDs != null) {
				for (int id : primModelIDs) 
					geschModellList.add(BfRNodePluginActivator .getBfRService().getEstimatedStatisticModellsByID(id));
				BufferedDataTable geschModelleTable = EMFUtils .createBufferedDataTableFromEstimatedModels(geschModellList, exec);
				BufferedDataTable geschParameterTable = EMFUtils .createBufferedDataTableFromEstimatedParameters( geschModellList, exec);
				if (geschModelleTable != null && (plotType.equals(PLOT_TYPE_M) || plotType.equals(PLOT_TYPE_MD)))
				container.addTableFile(PluginUtils.GES_MODELL, PluginUtils.writeIntoCsvFile(geschModelleTable, exec));
				if (geschParameterTable != null && (plotType.equals(PLOT_TYPE_M) || plotType.equals(PLOT_TYPE_MD)))
				container.addTableFile(PluginUtils.GES_MODELL_PARAMETER, PluginUtils.writeIntoCsvFile(geschParameterTable, exec));
			}
	}

	private void sendParams() {
		logger.info("\"" + param + "\"");
		pushFlowVariableString(PluginUtils.CHOSEN_PARAM, "\"" + param + "\"");
	}

	//out SelectionHandler
	private void sendModelTypes() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < modelTypes.length; i++) {
			builder.append("\",\"");
			builder.append(modelTypes[i]);
		}
		builder.append("\"");
		logger.info(builder.substring(2));
		pushFlowVariableString(PluginUtils.MODELTYPE, builder.substring(2));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		expIds = null;
		primModelIDs = null;
		secModelIDs = null;
	}

	@Override
	protected DataTableSpec[] configure(DataTableSpec[] inSpecs)
			throws InvalidSettingsException {

		if (getAvailableFlowVariables().containsKey(
				PluginUtils.VERSUCHSBEDINGUNGEN)
				&& getAvailableFlowVariables().get(
						PluginUtils.VERSUCHSBEDINGUNGEN) != null) {
			fileName = peekFlowVariableString(PluginUtils.VERSUCHSBEDINGUNGEN);
			// parse csv, get id column only
			try {
				@SuppressWarnings("rawtypes")
				List[] csvContent = PluginUtils.getCsvContent(fileName, true);
				expIds = new int[csvContent[1].size()];
				for (int i = 0; i < csvContent[1].size(); i++) {
					expIds[i] = Integer.parseInt(csvContent[1].get(i)
							.toString());
				}
				pushFlowVariableInt(PluginUtils.EXPERIMENTS_SIZE, expIds.length);
			} catch (FileNotFoundException e) {
				logger.error("Could not load data referenced by flow variable. " +
						"Either no conditions were selected or the selection " +
						"became obsolete by restarting KNIME.\n"
						+ e);
				setWarningMessage("No conditions were selected.");
			}
		}

		List<String> colNames = new ArrayList<String>();
		for (int i = 0; i < inSpecs[0].getNumColumns(); i++) {
			colNames.add(inSpecs[0].getColumnSpec(i).getName());
		}
		if (!PluginUtils.getTableNameByColNames(colNames).equals(
				"Versuchsbedingungen")) {
			logger.error("Invalid data at input port! The node requires a set of experiments.");
		}

		return inSpecs;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		settings.addIntArray(EXPERIMENTS, expIds);
		settings.addIntArray(CHOSEN_PRIM_MODEL_IDS, primModelIDs);
		settings.addIntArray(CHOSEN_PRIM_MODEL_IDS, secModelIDs);
		settings.addStringArray(CHOSEN_PARAM, param);
		settings.addStringArray(SELECTED_MODELTYPE_STRING, modelTypes);
		settings.addString(CHOOSEN_PLOT_TYPE, plotType);
		settings.addIntArray(SELECTED_ROWS_PRIM_GRAPH, selectedRowsPrimGraph);
		settings.addIntArray(SELECTED_ROWS_PRIM_FORECAST,
				selectedRowsPrimForecast);
		settings.addIntArray(SELECTED_ROWS_SEC_FORECAST,
				selectedRowsSecForecast);
		settings.addString(ACTIVE_COMPONENT, activeComponent);
		settings.addString(CHOSEN_MODEL_NAME, modelName);
		settings.addString(CHOSEN_PARAM, paramName);
		settings.addIntArray(CHOSEN_MODEL_TYPES, modelTypesIndices);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		expIds = settings.getIntArray(EXPERIMENTS, null);
		primModelIDs = settings.getIntArray(CHOSEN_PRIM_MODEL_IDS, null);
		selectedRowsPrimGraph = settings.getIntArray(SELECTED_ROWS_PRIM_GRAPH,
				null);
		selectedRowsPrimForecast = settings.getIntArray(
				SELECTED_ROWS_PRIM_FORECAST, null);
		selectedRowsSecForecast = settings.getIntArray(
				SELECTED_ROWS_SEC_FORECAST, null);
		secModelIDs = settings.getIntArray(CHOSEN_SEC_MODEL_IDS, null);
		param = settings.getString(CHOSEN_PARAM, "");
		modelTypes = settings.getStringArray(SELECTED_MODELTYPE_STRING, "");
		plotType = settings.getString(CHOOSEN_PLOT_TYPE, PLOT_TYPE_MD);
		activeComponent = settings.getString(ACTIVE_COMPONENT);
		modelName = settings.getString(CHOSEN_MODEL_NAME, "");
		paramName = settings.getString(CHOSEN_PARAM, "");
		modelTypesIndices = settings.getIntArray(CHOSEN_MODEL_TYPES, null);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

}
