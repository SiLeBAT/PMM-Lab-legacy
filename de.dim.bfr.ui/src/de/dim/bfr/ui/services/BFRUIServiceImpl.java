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
/**
 * Project: de.dim.bfr.ui
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/services/BFRUIServiceImpl.java $
 * $LastChangedDate: 2012-02-02 16:34:47 +0100 (Do, 02 Feb 2012) $
 * $lastChangedBy$
 * $Revision: 701 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.services;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import de.dim.bfr.BfrFactory;
import de.dim.bfr.GeschModelList;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.LevelTyp;
import de.dim.bfr.Literatur;
import de.dim.bfr.LiteraturListe;
import de.dim.bfr.ParameterRoleType;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellKatalog;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.VersuchsBedingung;
import de.dim.bfr.VersuchsBedingungList;


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
	private static int id = 1;
	
	static {
		literatureList = BfrFactory.eINSTANCE.createLiteraturListe();
		Literatur l = BfrFactory.eINSTANCE.createLiteratur();
		l.setErstautor("Donald Duck");
		l.setTitel("Lustiges Taschenbuch");
		literatureList.getLiteratur().add(l);
		catalogue = BfrFactory.eINSTANCE.createStatistikModellKatalog();
		StatistikModell m = BfrFactory.eINSTANCE.createStatistikModell();
		m.setName("Test P");
		m.setBeschreibung("Test P Beschreibung");
		m.setNotation("Test Notation");
		m.setLevel(LevelTyp.PRIMARY);
		StatistikModellParameter pi = BfrFactory.eINSTANCE.createStatistikModellParameter();
		pi.setName("Variable unabhängig");
		pi.setRole(ParameterRoleType.INDEPENDENT);
		StatistikModellParameter pd = BfrFactory.eINSTANCE.createStatistikModellParameter();
		pd.setName("Variable abhängig");
		pd.setRole(ParameterRoleType.DEPENDENT);
		StatistikModellParameter p1 = BfrFactory.eINSTANCE.createStatistikModellParameter();
		p1.setName("Parameter 1");
		p1.setRole(ParameterRoleType.PARAMETER);
		StatistikModellParameter p2 = BfrFactory.eINSTANCE.createStatistikModellParameter();
		p2.setName("Parameter 2");
		p2.setRole(ParameterRoleType.PARAMETER);
		StatistikModellParameter p3 = BfrFactory.eINSTANCE.createStatistikModellParameter();
		p3.setName("Parameter 3");
		p3.setRole(ParameterRoleType.PARAMETER);
		m.getParameter().add(p1);
		m.getParameter().add(p2);
		m.getParameter().add(p3);
		m.getParameter().add(pi);
		m.getParameter().add(pd);
		catalogue.getModelle().add(m);
		estimatedModels = BfrFactory.eINSTANCE.createGeschModelList();
		conditionsList = BfrFactory.eINSTANCE.createVersuchsBedingungList();
	}

	public BFRUIServiceImpl() {
		fillLiteratureList();
		fillCatalogue();
		fillEstimatedModels();
		fillConditionsList();
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
		System.out.println(conditionsList.getBedingungen().size());
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
			if(gsm.getBedingung().getId() == exID) {
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
		if (literature.getId() == 0) {
			literature.setId(id++);
		}
		return true;
	}
	
	/**
	 * Deletes the literature entity
	 * @param literature the literature instance to delete
	 * @return <code>true</code>, if delete was successful, otherwise <code>false</code>
	 */
	private boolean internalDeleteLiterature(final Literatur literature) {
		return true;
	}

	/**
	 * Persist or update the statistic model entity
	 * @param model the statistic model instance 
	 * @return <code>true</code>, if persist or update was successful, otherwise <code>false</code>
	 */
	private boolean internalSaveStatisticModel(final StatistikModell model) {
		if (model.getId() == 0) {
			model.setId(id++);
		}
		return true;
	}
	
	/**
	 * Deletes the statistic model entity
	 * @param model the statistic model instance to delete
	 * @return <code>true</code>, if delete was successful, otherwise <code>false</code>
	 */
	private boolean internalDeleteStatisticModel(final StatistikModell model) {
		return true;
	}
	
	/**
	 * Deletes the statistic model parameter entity
	 * @param parameter the statistic model parameter instance to delete
	 * @return <code>true</code>, if delete was successful, otherwise <code>false</code>
	 */
	private boolean internalDeleteStatisticModelParameter(final StatistikModellParameter parameter) {
		return true;
	}

	/**
	 * Persist or update the estimated statistic model entity
	 * @param model the estimated statistic model instance 
	 * @return <code>true</code>, if persist or update was successful, otherwise <code>false</code>
	 */
	private boolean internalSaveEstimatedStatisticModel(final GeschaetztStatistikModell model) {
		if (model.getId() == 0) {
			model.setId(id++);
		}
		return true;
	}
	
	/**
	 * Deletes the estimated statistic model entity
	 * @param model the estimated statistic model instance to delete
	 * @return <code>true</code>, if delete was successful, otherwise <code>false</code>
	 */
	private boolean internalDeleteEstimatedStatisticModel(final GeschaetztStatistikModell model) {
		return true;
	}

	/**
	 * Load all statistic model data and store it into the model catalog
	 */
	private void fillCatalogue() {
	}

	/**
	 * Load all model literature data and store it into the literature list
	 */
	private void fillLiteratureList() {
	}

	/**
	 * Loads all estimated statistic models
	 */
	private void fillEstimatedModels() {
	}

	/**
	 * Loads all experiment conditions
	 */
	private void fillConditionsList() {
		List<VersuchsBedingung> bs = new ArrayList<VersuchsBedingung>();
		VersuchsBedingung b1 = BfrFactory.eINSTANCE.createVersuchsBedingung();
		b1.setId(1);
		b1.setIdCB("Test 1");
		VersuchsBedingung b2 = BfrFactory.eINSTANCE.createVersuchsBedingung();
		b2.setId(2);
		b2.setIdCB("Test 2");
		VersuchsBedingung b3 = BfrFactory.eINSTANCE.createVersuchsBedingung();
		b3.setId(3);
		b3.setIdCB("Test 3");
		bs.add(b1);
		bs.add(b2);
		bs.add(b3);
		conditionsList.getBedingungen().addAll(bs);
	}

	@Override
	public void refreshServiceContent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public StatistikModellParameter getStatistikModellParameterById(final int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void openDBGUI(final boolean setVisible) {
	}

	@Override
	public ResultSet getDBData(final String tablename, final String columnID, final Integer id) {
		return null;
	}

	@Override
	public Object getDBDetail(String tablename, int tableID,
			String desiredColumn) {
		return null;
	}
}
