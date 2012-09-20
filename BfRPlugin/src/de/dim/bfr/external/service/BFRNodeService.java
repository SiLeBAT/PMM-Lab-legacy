/**
 * Project: de.dim.bfr.ui

 * $HeadURL: http://datainmotion.de/statup/trunk/de.dim.bfr.ui/src/de/dim/bfr/ui/services/BFRNodeService.java $
 * $LastChangedDate: 2011-11-20 21:30:19 +0100 (So, 20 Nov 2011) $
 * $lastChangedBy$
 * $Revision: 74 $
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
package de.dim.bfr.external.service;

import java.sql.Connection;
import java.util.List;

import de.dim.bfr.Einheiten;
import de.dim.bfr.GeschModellParameter;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.Messwerte;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.VersuchsBedingung;
import de.dim.bfr.VersuchsBedingungList;

/**
 * Service interface for the BFR data handling regarding the nodes
 * @author Juergen Albert
 * @since 14.11.2011
 */
public interface BFRNodeService {
	
	public boolean arePreferencesSet();

	public void refreshServiceContent();

	public VersuchsBedingung getVersuchsbedingungByID(int versuchID);
	
	public List<VersuchsBedingung> getVersuchsbedingungenByGeschModel(int id);

	public VersuchsBedingungList getAllVersuchbedingungen();

	public List<GeschaetztStatistikModell> getEstimatedStatisticModellsByExperiment(
			int experimentId);
	
	public Connection getJDBCConnection();
	
	public void closeJDBCConnection(Connection connection);

	public GeschaetztStatistikModell getEstimatedStatisticModellsByID(
			int id);

    public List<Messwerte> getMesswerteByVersuchsbedingung(int m_experimentID);
    
    public List<StatistikModellParameter> getParameterById (int id);
    
	public StatistikModell getModellById(int id);
	
	public List<GeschModellParameter> getGeschModellParameterByModellId(int id); 
	
	public GeschModellParameter getGeschParameterByParamId(int id);
	
	public List<Einheiten> getAllEinheiten();
	
	public List<StatistikModell> getAllModels();
	
	public List<GeschaetztStatistikModell> getAllEstimatedModels();
	
	public List<GeschaetztStatistikModell> getSecEstModelsForPrimEstModel(GeschaetztStatistikModell primModel);
	
	public List<String> getPrimEstModelNamesForSecEstModel(GeschaetztStatistikModell secModel);
}
