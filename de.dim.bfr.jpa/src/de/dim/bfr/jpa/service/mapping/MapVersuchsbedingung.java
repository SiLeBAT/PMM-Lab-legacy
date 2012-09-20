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
import de.dim.bfr.VersuchsBedingung;
import de.dim.bfr.jpa.entities.JPAVersuchsbedingungen;

public class MapVersuchsbedingung {

		/**
	 * @param condition
	 *            the instance of {@link VersuchsBedingung} to create a JPA
	 *            object from
	 * @return a call to
	 *         {@link #copyVersuchBedProperties2Entity(VersuchsBedingung, JPAVersuchsbedingungen)}
	 */
	public JPAVersuchsbedingungen createJPAVersuchsbedingungFromEMFObj(
			VersuchsBedingung condition) {
		JPAVersuchsbedingungen jpaCondition = new JPAVersuchsbedingungen();
		return emfToJPA(condition, jpaCondition);
	}

	/**
	 * @param condition
	 *            the instance of {@link VersuchsBedingung} to copy from
	 * @param jpaCondition
	 *            the instance of {@link JPAVersuchsbedingungen} to copy to
	 * @return instance of {@link JPAVersuchsbedingungen}
	 */
	public JPAVersuchsbedingungen emfToJPA(
			VersuchsBedingung condition, JPAVersuchsbedingungen jpaCondition) {
		jpaCondition.setIdCb(condition.getIdCB());
		return jpaCondition;
	}

	/**
	 * @param jpaCondition
	 *            the instance of {@link JPAVersuchsbedingungen} to copy from
	 * @return new instance of {@link VersuchsBedingung}
	 */
	public VersuchsBedingung jpaToEMF(
			JPAVersuchsbedingungen jpaCondition) {
		if (jpaCondition == null)
			return null;
		VersuchsBedingung condition = BfrFactory.eINSTANCE
				.createVersuchsBedingung();
		condition.setId(jpaCondition.getId());
		condition.setIdCB(jpaCondition.getIdCb());
		return condition;
	}
	
	
}
