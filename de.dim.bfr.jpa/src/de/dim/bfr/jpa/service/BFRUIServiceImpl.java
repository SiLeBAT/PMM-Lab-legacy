/**
 * Project: de.dim.bfr.ui
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.jpa/src/de/dim/bfr/jpa/service/BFRUIServiceImpl.java $
 * $LastChangedDate: 2012-02-02 16:35:00 +0100 (Do, 02 Feb 2012) $
 * $lastChangedBy$
 * $Revision: 702 $
 * /*******************************************************************************
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
package de.dim.bfr.jpa.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//import org.hsh.bfr.db.DBKernel;

import de.dim.bfr.BfrFactory;
import de.dim.bfr.GeschModelList;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.Literatur;
import de.dim.bfr.LiteraturListe;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellKatalog;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.VersuchsBedingung;
import de.dim.bfr.VersuchsBedingungList;
import de.dim.bfr.ui.services.BFRUIService;


/**
 * Implementation of the {@link BFRUIService}
 * @author Mark Hoffmann
 * @since 14.11.2011
 */
public class BFRUIServiceImpl implements BFRUIService {
	
	private static final LiteraturListe literatureList;
	private static final StatistikModellKatalog catalogue;
	private static final GeschModelList estimatedModels;
	private static final VersuchsBedingungList conditionsList;
	private PersistenceManager pm;
	
	static {
		literatureList = BfrFactory.eINSTANCE.createLiteraturListe();
		catalogue = BfrFactory.eINSTANCE.createStatistikModellKatalog();
		estimatedModels = BfrFactory.eINSTANCE.createGeschModelList();
		conditionsList = BfrFactory.eINSTANCE.createVersuchsBedingungList();
	}

	public BFRUIServiceImpl() {
		this.pm = PersistenceManager.getInstance();
	}

	@Override
	public Object getDBDetail(final String tablename, final int tableID,
			final String desiredColumn) {
		//openDBGUI(false);
		return null;//DBKernel.getValue(tablename, "ID", tableID+"", desiredColumn);
	}

	@Override
	public ResultSet getDBData(final String tablename, final String columnID, final Integer id) {
		//openDBGUI(false);
		//ResultSet rs = DBKernel.getResultSet("SELECT * FROM " + DBKernel.delimitL(tablename) +
		//		(columnID == null ? "" : " WHERE " + DBKernel.delimitL(columnID) + "=" + id), false);
		return null;//rs;
	}

	@Override
	public void openDBGUI(final boolean setVisible) {
		final Connection connection = pm.getJDBCConnection();
		/*
		org.hsh.bfr.db.StartApp.go(connection, setVisible, true);
        
		final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                //System.err.println(DBKernel.mainFrame.isVisible() + "\t" + connection + " Old: " + System.currentTimeMillis());
                if (connection == null || DBKernel.mainFrame != null && !DBKernel.mainFrame.isVisible()) {
                	if (connection != null) pm.closeJDBCConnection(connection, true);
                	timer.cancel();
                }
                else {
                	pm.closeJDBCConnection(connection, false);
                }
            }
        }, 0, 1000);	
        */
	}
	
	@Override
	public void refreshServiceContent() {

		literatureList.getLiteratur().clear();
		catalogue.getModelle().clear();
		estimatedModels.getModels().clear();
		conditionsList.getBedingungen().clear();
		pm.refresh();
		if(pm.isReady()){
			fillLiteratureList();
			fillCatalogue();
			fillConditionsList();
			fillEstimatedModels();
			
		}
	}

	public void shutdown(){
	}
	
	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.services.BFRUIService#getAllLiterature()
	 */
	@Override
	public LiteraturListe getAllLiterature() {
		return literatureList;
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.services.BFRUIService#getLiteratureById(int)
	 */
	@Override
	public Literatur getLiteratureById(final int id) {
		for (Literatur literature : literatureList.getLiteratur()) {
			if (literature.getId() == id) {
				return literature;
			}
		}
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.services.BFRUIService#saveLiterature(de.dim.bfr.Literatur)
	 */
	@Override
	public boolean saveLiterature(final Literatur literature) {
		if (internalSaveLiterature(literature)) {
			if (!literatureList.getLiteratur().contains(literature)) {
				literatureList.getLiteratur().add(literature);
			}
			return true;
		}
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.services.BFRUIService#deleteLiterature(de.dim.bfr.Literatur)
	 */
	@Override
	public boolean deleteLiterature(final Literatur literature) {
		literatureList.getLiteratur().remove(literature);
		return internalDeleteLiterature(literature);
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.services.BFRUIService#getAllStatisticModels()
	 */
	@Override
	public StatistikModellKatalog getAllStatisticModels() {
		return catalogue;
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.services.BFRUIService#getStatisticModellById(int)
	 */
	@Override
	public StatistikModell getStatisticModellById(final int id) {
		for (StatistikModell model : catalogue.getModelle()) {
			if (model.getId() == id) {
				return model;
			}
		}
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.services.BFRUIService#saveStatisticModel(de.dim.bfr.StatistikModell)
	 */
	@Override
	public boolean saveStatisticModel(final StatistikModell model) {
		if (internalSaveStatisticModel(model)) {
			if (!catalogue.getModelle().contains(model)) {
				catalogue.getModelle().add(model);
			}
			return true;
		}
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.services.BFRUIService#deleteStatisticModel(de.dim.bfr.StatistikModell)
	 */
	@Override
	public boolean deleteStatisticModel(final StatistikModell model) {
		catalogue.getModelle().remove(model);
		return internalDeleteStatisticModel(model);
	}
	
	@Override
	public boolean deleteStatisticModelParameter(final StatistikModellParameter parameter) {
		return internalDeleteStatisticModelParameter(parameter);
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.services.BFRUIService#getAllEstimatedStatisticModels()
	 */
	@Override
	public GeschModelList getAllEstimatedStatisticModels() {
		return estimatedModels;
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.services.BFRUIService#getEstimatedStatisticModellById(int)
	 */
	@Override
	public GeschaetztStatistikModell getEstimatedStatisticModellById(final int id) {
		for (GeschaetztStatistikModell model : estimatedModels.getModels()) {
			if (model.getId() == id) {
				return model;
			}
		}
		return null;
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.services.BFRUIService#saveEstimatedStatisticModel(de.dim.bfr.GeschaetztStatistikModell)
	 */
	@Override
	public boolean saveEstimatedStatisticModel(final GeschaetztStatistikModell model) {
		if (internalSaveEstimatedStatisticModel(model)) {
			if (!estimatedModels.getModels().contains(model)) {
				estimatedModels.getModels().add(model);
			}
			return true;
		}
		return false;
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.services.BFRUIService#deleteEstimatedStatisticModel(de.dim.bfr.GeschaetztStatistikModell)
	 */
	@Override
	public boolean deleteEstimatedStatisticModel(final GeschaetztStatistikModell model) {
		estimatedModels.getModels().remove(model);
		return internalDeleteEstimatedStatisticModel(model);
	}

	/* 
	 * (non-Javadoc)
	 * @see de.dim.bfr.ui.services.BFRUIService#getAllVersuchbedingungen()
	 */
	@Override
	public VersuchsBedingungList getAllVersuchbedingungen() {
		return conditionsList;
	}
	
	@Override
	public VersuchsBedingung getVersuchsbedingungByID(final int id) {
		for (VersuchsBedingung v : conditionsList.getBedingungen()) {
			if (v.getId() == id) {
				return v;
			}
		}
		return null;
	}

	public List<GeschaetztStatistikModell> getEstimatedStatisticModellsByExperiment(
			final int exID) {
		
		List<GeschaetztStatistikModell> result = new ArrayList<GeschaetztStatistikModell>();
		
		for(GeschaetztStatistikModell gsm : estimatedModels.getModels())
		{
			if(gsm.getBedingung() != null && gsm.getBedingung().getId() == exID) {
				result.add(gsm);
			}
		}
		
		return result;
	}

	/**
	 * Persist or update the literature entity
	 * @param literature the literature instance 
	 * @return <code>true</code>, if persist or update was successful, otherwise <code>false</code>
	 */
	private boolean internalSaveLiterature(final Literatur literature) {
		return pm.saveLiteratur(literature);
	}
	
	/**
	 * Deletes the literature entity
	 * @param literature the literature instance to delete
	 * @return <code>true</code>, if delete was successful, otherwise <code>false</code>
	 */
	private boolean internalDeleteLiterature(final Literatur literature) {
		return pm.deleteLiteratur(literature);
	}

	/**
	 * Persist or update the statistic model entity
	 * @param model the statistic model instance 
	 * @return <code>true</code>, if persist or update was successful, otherwise <code>false</code>
	 */
	private boolean internalSaveStatisticModel(final StatistikModell model) {
		return pm.saveStatisticModell(model);
	}
	
	/**
	 * Deletes the statistic model entity
	 * @param model the statistic model instance to delete
	 * @return <code>true</code>, if delete was successful, otherwise <code>false</code>
	 */
	private boolean internalDeleteStatisticModel(final StatistikModell model) {
		return pm.deleteStatistikModel(model);
	}
	
	/**
	 * Deletes the statistic model parameter entity
	 * @param parameter the statistic model parameter instance to delete
	 * @return <code>true</code>, if delete was successful, otherwise <code>false</code>
	 */
	private boolean internalDeleteStatisticModelParameter(final StatistikModellParameter parameter) {
		return pm.deleteStatistikModelParameter(parameter);
	}

	/**
	 * Persist or update the estimated statistic model entity
	 * @param model the estimated statistic model instance 
	 * @return <code>true</code>, if persist or update was successful, otherwise <code>false</code>
	 */
	private boolean internalSaveEstimatedStatisticModel(final GeschaetztStatistikModell model) {
		return pm.saveGeschaetztStatistikModell(model);
	}
	
	/**
	 * Deletes the estimated statistic model entity
	 * @param model the estimated statistic model instance to delete
	 * @return <code>true</code>, if delete was successful, otherwise <code>false</code>
	 */
	private boolean internalDeleteEstimatedStatisticModel(final GeschaetztStatistikModell model) {
		return pm.deleteGeschaetztStatistikModell(model);
	}

	/**
	 * Load all statistic model data and store it into the model catalog
	 */
	private void fillCatalogue() {
		catalogue.getModelle().addAll(pm.getStatistikModellkatalog().getModelle());
	}

	/**
	 * Load all model literature data and store it into the literature list
	 */
	private void fillLiteratureList() {
		literatureList.getLiteratur().addAll(pm.getLiteraturList().getLiteratur());
	}

	/**
	 * Loads all estimated statistic models
	 */
	private void fillEstimatedModels() {
		estimatedModels.getModels().addAll(pm.getGeschModellList().getModels());
	}

	/**
	 * Loads all experiment conditions
	 */
	private void fillConditionsList() {
		conditionsList.getBedingungen().addAll(pm.getAllVersuchsbedingungen().getBedingungen());
	}

	@Override
	public StatistikModellParameter getStatistikModellParameterById(final int id) {
		for (StatistikModell m : catalogue.getModelle()) {
			for (StatistikModellParameter p : m.getParameter()) {
				if (p.getId() == id) {
					return p;
				}
			}
		}
		return null;
	}
}
