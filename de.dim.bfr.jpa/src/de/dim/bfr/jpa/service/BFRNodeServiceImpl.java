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
package de.dim.bfr.jpa.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import de.dim.bfr.BfrFactory;
import de.dim.bfr.Einheiten;
import de.dim.bfr.GeschModellParameter;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.Messwerte;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.VersuchsBedingung;
import de.dim.bfr.VersuchsBedingungList;
import de.dim.bfr.external.service.BFRNodeService;
import de.dim.bfr.jpa.internal.BFRJPAActivator;

public class BFRNodeServiceImpl implements BFRNodeService {

	private PersistenceManager pm;
	private VersuchsBedingungList vbl = null;

	public BFRNodeServiceImpl() {
		this.pm = PersistenceManager.getInstance();
	}

	public void shutdown() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.dim.bfr.ui.services.BFRUIService#getAllVersuchbedingungen()
	 */
	@Override
	public VersuchsBedingungList getAllVersuchbedingungen() {
		if (vbl != null)
			return vbl;
		vbl = BfrFactory.eINSTANCE.createVersuchsBedingungList();
		vbl.getBedingungen().addAll(
				pm.getAllVersuchsbedingungen().getBedingungen());
		return vbl;
	}

	@Override
	public void refreshServiceContent() {
		BFRJPAActivator.getDefault().getUIService().refreshServiceContent();
	}

	@Override
	public VersuchsBedingung getVersuchsbedingungByID(int versuchID) {
		for (VersuchsBedingung v : vbl.getBedingungen())
			if (v.getId() == versuchID)
				return v;
		return null;
	}

	@Override
	public List<VersuchsBedingung> getVersuchsbedingungenByGeschModel(int id) {
		if (vbl == null)
			getAllVersuchbedingungen();
		List<VersuchsBedingung> vList = new ArrayList<VersuchsBedingung>();
		for (VersuchsBedingung v : vbl.getBedingungen()) {
			if (getEstimatedStatisticModellsByID(id).getBedingung().getId() == v
					.getId()) {
				vList.add(v);
			}
		}
		return vList;

	}

	@Override
	public List<GeschaetztStatistikModell> getEstimatedStatisticModellsByExperiment(
			int experimentId) {
		return BFRJPAActivator.getDefault().getUIService()
				.getEstimatedStatisticModellsByExperiment(experimentId);
	}

	@Override
	public Connection getJDBCConnection() {
		return pm.getJDBCConnection();
	}

	@Override
	public void closeJDBCConnection(Connection connection) {
		pm.closeJDBCConnection(connection);
	}

	@Override
	public GeschaetztStatistikModell getEstimatedStatisticModellsByID(int id) {
		return BFRJPAActivator.getDefault().getUIService()
				.getEstimatedStatisticModellById(id);
	}

	@Override
	public List<Messwerte> getMesswerteByVersuchsbedingung(int id) {
		return pm.getMesswerteByVersuchsbedingungID(id);
	}

	public List<StatistikModellParameter> getParameterById(int id) {
		return BFRJPAActivator.getDefault().getUIService()
				.getStatisticModellById(id).getParameter();
	}

	public StatistikModell getModellById(int id) {
		return BFRJPAActivator.getDefault().getUIService()
				.getStatisticModellById(id);
	}

	@Override
	public List<GeschModellParameter> getGeschModellParameterByModellId(int id) {
		return getEstimatedStatisticModellsByID(id).getParameter();
	}

	@Override
	public boolean arePreferencesSet() {
		return pm.isReady();
	}

	public GeschModellParameter getGeschParameterByParamId(int id) {
		List<GeschaetztStatistikModell> models = BFRJPAActivator.getDefault()
				.getUIService().getAllEstimatedStatisticModels().getModels();
		for (GeschaetztStatistikModell model : models) {
			for (GeschModellParameter param : model.getParameter())
				if (param.getId() == id)
					return param;
		}
		return null;
	}

	public List<Einheiten> getAllEinheiten() {
		return pm.getAllEinheiten();
	}

	@Override
	public List<StatistikModell> getAllModels() {
		return BFRJPAActivator.getDefault().getUIService()
				.getAllStatisticModels().getModelle();
	}

	@Override
	public List<GeschaetztStatistikModell> getAllEstimatedModels() {
		return BFRJPAActivator.getDefault().getUIService()
				.getAllEstimatedStatisticModels().getModels();
	}

	@Override
	public List<GeschaetztStatistikModell> getSecEstModelsForPrimEstModel(
			GeschaetztStatistikModell primModel) {
		List<GeschaetztStatistikModell> result = new ArrayList<GeschaetztStatistikModell>();
		List<GeschaetztStatistikModell> availableModelsInDB = pm
				.getSecondaryEstModelByPrimaryEstModel(primModel);
		for (GeschaetztStatistikModell model : availableModelsInDB) {
			GeschaetztStatistikModell emfModel = getEstimatedStatisticModellsByID(model
					.getId());
			if (emfModel != null)
				result.add(emfModel);
			else
				result.add(model);
		}

		return result;
	}

	@Override
	public List<String> getPrimEstModelNamesForSecEstModel(
			GeschaetztStatistikModell secModel) {
		return pm.getPrimaryEstModelNamesBySecondaryEstModel(secModel);
	}

}
