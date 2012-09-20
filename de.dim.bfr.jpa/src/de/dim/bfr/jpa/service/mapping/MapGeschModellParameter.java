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
import de.dim.bfr.GeschModellParameter;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.jpa.entities.JPAGeschaetzteParameter;
import de.dim.bfr.jpa.service.PersistenceManager;

public class MapGeschModellParameter {

	
	/**
	 * @param param
	 *            the instance of {@link GeschModellParameter} to copy from
	 * @param jpaParam
	 *            the instance of {@link JPAGeschaetzteParameter} to copy to
	 * @return instance of {@link JPAGeschaetzteParameter}
	 */
	public JPAGeschaetzteParameter emfToJPA(
			GeschModellParameter param, JPAGeschaetzteParameter jpaParam) {
//		jpaParam.setModellParameter(getJPAModellParameterById(param.getModelParameter().getId()));
//		jpaParam.setModellParameter(copyStatistikModellParameterPropertiesToEntity(param.getModelParameter(), getJPAModellParameterById(param.getModelParameter().getId())));
//		jpaParam.setId(param.getId());
		if (param.getP()!= null)
			jpaParam.setP(param.getP());
		if (param.getSd()!= null)
			jpaParam.setSd(param.getSd());
		if (param.getT()!= null)
			jpaParam.setT(param.getT());
		if (param.getKiOben()!= 0.0d)
			jpaParam.setKiOben(param.getKiOben());
		if (param.getKiUnten()!= 0.0d)
			jpaParam.setKiUnten(param.getKiUnten());
		jpaParam.setWert(param.getWert());
		return jpaParam;
	}

	/**
	 * @param jpaParam
	 *            the instance of {@link JPAGeschaetzteParameter} to copy from
	 * @return new instance of {@link GeschModellParameter}
	 */
	public GeschModellParameter jpaToEMF(
			JPAGeschaetzteParameter jpaParam, GeschaetztStatistikModell model) {
		PersistenceManager pm = new PersistenceManager();
		MapVersuchsbedingung mapVersuchsbedingung = new MapVersuchsbedingung();
		if (jpaParam == null)
			return null;
		GeschModellParameter param = BfrFactory.eINSTANCE
				.createGeschModellParameter();
		param.setGeschaetztesModell(model);
		param.setModelParameter(pm.getStatistikModellParameterById(jpaParam
				.getModellParameter().getId()));

		param.setId(jpaParam.getId());
		param.setP(jpaParam.getP());
		param.setSd(jpaParam.getSd());
		param.setT(jpaParam.getT());
		param.setName(jpaParam.getModellParameter().getParametername());
		if(jpaParam.getGeschaetztesModell().getVersuchsbedingung() != null)
			param.setVersuchsBedingung(mapVersuchsbedingung.jpaToEMF(jpaParam
					.getGeschaetztesModell().getVersuchsbedingung()));
		if (jpaParam.getKiOben() != null)
			param.setKiOben(jpaParam.getKiOben());
		if (jpaParam.getKiUnten() != null)
			param.setKiUnten(jpaParam.getKiUnten());
		if (jpaParam.getWert() != null)
			param.setWert(jpaParam.getWert());
		else
			param.setWert(0.0); // TODO: das sollte nicht nötig sein!!!

		return param;
	}


	
}
