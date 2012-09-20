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
import de.dim.bfr.ParameterRoleType;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.jpa.entities.JPAModellParameter;

public class MapStatistikModellParameter {

	/**
	 * @param param
	 *            the instance of {@link JPAModellParameter} to copy from
	 * @return a new instance of {@link StatistikModellParameter}
	 */
	public StatistikModellParameter jpaToEMF(
			final JPAModellParameter param) {
		if (param == null) {
			return null;
		}
		StatistikModellParameter parameter = BfrFactory.eINSTANCE
				.createStatistikModellParameter();
		parameter.setBeschreibung(param.getBeschreibung());
		parameter.setId(param.getId());
		if (param.isGanzzahl() != null) {
			parameter.setInteger(param.isGanzzahl());
		}
		/*
		else {
			parameter.setInteger(false);
		}
		*/
		parameter.setMax(param.getMax());
		parameter.setMin(param.getMin());
		parameter.setName(param.getParametername());
		if (param.getParameterTyp() != null) {
			parameter.setRole(ParameterRoleType.get(param.getParameterTyp()));
		}
		return parameter;
	}

	/**
	 * copies properties from an instance of {@link StatistikModellParameter} to
	 * an instance of {@link JPAModellParameter}
	 * 
	 * @param param
	 *            the instance of {@link StatistikModellParameter} to copy from
	 * @param jpaParam
	 *            the instance of {@link JPAModellParameter} to insert into
	 * @return
	 */
	public JPAModellParameter emfToJPA(
			final StatistikModellParameter param, final JPAModellParameter jpaParam) {
		jpaParam.setParametername(param.getName());
		jpaParam.setParameterTyp(param.getRole().getValue());
		jpaParam.setBeschreibung(param.getBeschreibung());
		jpaParam.setMax(param.getMax());
		jpaParam.setMin(param.getMin());
		jpaParam.setGanzzahl(param.isInteger()); // hat Statup wohl vergessen. old - StatUp
		if (param.getId() != 0) {
			jpaParam.setId(param.getId());
		}
		return jpaParam;
	}

	
}
