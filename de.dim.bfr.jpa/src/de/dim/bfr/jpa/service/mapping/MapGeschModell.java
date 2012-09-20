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
package de.dim.bfr.jpa.service.mapping;

import de.dim.bfr.BfrFactory;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.jpa.entities.JPAGeschaetzteModelle;
import de.dim.bfr.jpa.entities.JPALiteratur;
import de.dim.bfr.jpa.entities.JPAModellParameter;
import de.dim.bfr.jpa.service.BFRUIServiceImpl;
import de.dim.bfr.jpa.service.PersistenceManager;
import de.dim.bfr.ui.services.BFRUIService;

public class MapGeschModell {

		/**
	 * @param model
	 *            instance of {@link GeschaetztStatistikModell}
	 * @return a call to
	 *         {@link #copyEstimatedModelProperties2Entity(GeschaetztStatistikModell, JPAGeschaetzteModelle)}
	 */
	public JPAGeschaetzteModelle createJPAEstimatedModelFromEMFObj(
			GeschaetztStatistikModell model) {
		JPAGeschaetzteModelle jpaEstModel = new JPAGeschaetzteModelle();
		return emfToJPA(model, jpaEstModel);
	}

	/**
	 * @param estModel
	 *            the instance of {@link GeschaetztStatistikModell} to copy from
	 * @param jpaEstModel
	 *            the instance of {@link JPAGeschaetzteModelle} to copy to
	 * @return instance of {@link JPAGeschaetzteModelle}
	 */
	public JPAGeschaetzteModelle emfToJPA(
			GeschaetztStatistikModell estModel, JPAGeschaetzteModelle jpaEstModel) {
		PersistenceManager pm = new PersistenceManager();
		StatistikModell model = estModel.getStatistikModel();
		jpaEstModel.setKommentar(estModel.getKommentar());
		jpaEstModel.setManuellEingetragen(estModel.isManuellEingetragen());
		jpaEstModel.setRsquared(estModel.getRSquared());
		jpaEstModel.setRss(estModel.getRss());
		jpaEstModel.setScore(estModel.getScore());
		jpaEstModel.setModell(pm.getJPAModelById(model.getId()));
		
		return jpaEstModel;
	}

	/**
	 * @param jpaGeschModel
	 *            the instance of {@link JPAGeschaetzteModelle} to copy from
	 * @return a new instance of {@link GeschaetztStatistikModell}
	 */
	public GeschaetztStatistikModell jpaToEMF(
			JPAGeschaetzteModelle jpaGeschModel) {
		return jpaToEMF(jpaGeschModel, BfrFactory.eINSTANCE
				.createGeschaetztStatistikModell());
	}
	
	
	/**
	 * @param jpaGeschModel
	 *            the instance of {@link JPAGeschaetzteModelle} to copy from
	 * @return a new instance of {@link GeschaetztStatistikModell}
	 */
	public GeschaetztStatistikModell jpaToEMF(
			JPAGeschaetzteModelle jpaGeschModel, GeschaetztStatistikModell geschModel) {
		if (jpaGeschModel == null)
			return null;
		PersistenceManager pm = new PersistenceManager();
		BFRUIService service = new BFRUIServiceImpl();
		geschModel.setStatistikModel(service.getStatisticModellById(jpaGeschModel.getModell().getId()));
		if (jpaGeschModel.getVersuchsbedingung() != null)
			geschModel.setBedingung(service.getVersuchsbedingungByID(jpaGeschModel
					.getVersuchsbedingung().getId()));
		if (jpaGeschModel.getId()!=null)
			geschModel.setId(jpaGeschModel.getId());
		if (jpaGeschModel.getKommentar()!=null)
			geschModel.setKommentar(jpaGeschModel.getKommentar());
		if(jpaGeschModel.isManuellEingetragen()!=null)
			geschModel.setManuellEingetragen(jpaGeschModel.isManuellEingetragen());
		if(jpaGeschModel.getRsquared()!=null)
			geschModel.setRSquared(jpaGeschModel.getRsquared());
		if(jpaGeschModel.getRss()!= null)
			geschModel.setRss(jpaGeschModel.getRss());
		
		if (jpaGeschModel.getScore()!=null)
			geschModel.setScore(jpaGeschModel.getScore());
		for(JPALiteratur litertaur : jpaGeschModel.getLiteratur())
			geschModel.getLiteratur().add(service.getLiteratureById(litertaur.getId()));
		
		geschModel.getParameter().
				addAll(pm.getGeschModellParameterByModelFromDB(geschModel));
		if (jpaGeschModel.getResponseParameter()!= null) {
			JPAModellParameter jpaResponse = jpaGeschModel.getResponseParameter();
			geschModel.setResponse(service.getStatistikModellParameterById(jpaResponse.getId()));
		}
		
		return geschModel;
	}

	
}
