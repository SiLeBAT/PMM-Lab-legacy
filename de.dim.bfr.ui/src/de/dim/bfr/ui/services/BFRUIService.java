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
 * $HeadURL: http://data-in-motion.biz/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/services/BFRUIService.java $
 * $LastChangedDate: 2012-02-02 16:34:47 +0100 (Do, 02 Feb 2012) $
 * $lastChangedBy$
 * $Revision: 701 $
 * (c) Data in Motion 2011
 */
package de.dim.bfr.ui.services;

import java.sql.ResultSet;

import de.dim.bfr.GeschModelList;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.Literatur;
import de.dim.bfr.LiteraturListe;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellKatalog;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.VersuchsBedingung;
import de.dim.bfr.VersuchsBedingungList;

/**
 * Service interface for the BFR data handling of literature or statistic models
 * @author Mark Hoffmann
 * @since 14.11.2011
 */
public interface BFRUIService {
	
	public Object getDBDetail(final String tablename, final int tableID, final String desiredColumn);
	public ResultSet getDBData(final String tablename, final String columnID, final Integer id);
	public void openDBGUI(final boolean setVisible);
	/**
	 * Returns all literature instances
	 * @return all literature instances
	 */
	public LiteraturListe getAllLiterature();
	
	/**
	 * Returns the literature instance of the given id or <code>null</code> of no instance
	 * with this id exists.
	 * @param id the id of the literature object
	 * @return the literature instance of the given id or <code>null</code>
	 */
	public Literatur getLiteratureById(final int id);
	
	/**
	 * Saves a literature object
	 * @param literature the object to save
	 * @return <code>true</code>, if saving was successful, otherwise <code>false</code>
	 */
	public boolean saveLiterature(final Literatur literature);
	
	/**
	 * Deletes the literature instance
	 * @param literature the object to delete
	 * @return <code>true</code>, if delete was successful, otherwise <code>false</code>
	 */
	public boolean deleteLiterature(final Literatur literature);
	
	/**
	 * Returns all statistic model instances
	 * @return all statistic model instances
	 */
	public StatistikModellKatalog getAllStatisticModels();
	
	/**
	 * Returns the statistic model instance of the given id or <code>null</code> of no instance
	 * with this id exists.
	 * @param id the id of the statistic model object
	 * @return the statistic model instance of the given id or <code>null</code>
	 */
	public StatistikModell getStatisticModellById(final int id);
	
	/**
	 * Saves a statistic model object
	 * @param model the object to save
	 * @return <code>true</code>, if saving was successful, otherwise <code>false</code>
	 */
	public boolean saveStatisticModel(final StatistikModell model);
	
	/**
	 * Deletes the statistic model instance
	 * @param model the object to delete
	 * @return <code>true</code>, if delete was successful, otherwise <code>false</code>
	 */
	public boolean deleteStatisticModel(final StatistikModell model);
	
	/**
	 * Returns all estimated statistic model instances
	 * @return all estimated statistic model instances
	 */
	public GeschModelList getAllEstimatedStatisticModels();
	
	/**
	 * Returns the estimated statistic model instance of the given id or <code>null</code> of no instance
	 * with this id exists.
	 * @param id the id of the statistic model object
	 * @return the statistic model instance of the given id or <code>null</code>
	 */
	public GeschaetztStatistikModell getEstimatedStatisticModellById(final int id);
	
	/**
	 * Saves a estimated statistic model object
	 * @param model the object to save
	 * @return <code>true</code>, if saving was successful, otherwise <code>false</code>
	 */
	public boolean saveEstimatedStatisticModel(final GeschaetztStatistikModell model);
	
	/**
	 * Deletes the estimated statistic model instance
	 * @param model the object to delete
	 * @return <code>true</code>, if delete was successful, otherwise <code>false</code>
	 */
	public boolean deleteEstimatedStatisticModel(final GeschaetztStatistikModell model);
	
	/**
	 * Returns all experiment conditions
	 * @return all experiment conditions
	 */
	public VersuchsBedingungList getAllVersuchbedingungen();

	public boolean deleteStatisticModelParameter(final StatistikModellParameter parameter);
	
	public VersuchsBedingung getVersuchsbedingungByID(final int id); 
	
	public StatistikModellParameter getStatistikModellParameterById(final int id);
	
	public void refreshServiceContent();
}
