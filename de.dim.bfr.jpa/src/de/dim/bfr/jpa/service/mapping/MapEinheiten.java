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
import de.dim.bfr.Einheiten;
import de.dim.bfr.jpa.entities.JPAEinheiten;

public class MapEinheiten {
	
	/**
	 * @param jpaUnit the instance of {@link JPAEinheiten} to copy from
	 * @return instance of {@link Einheiten}
	 */
	public Einheiten jpaToEMF(JPAEinheiten jpaUnit) {
		if(jpaUnit == null)
			return null;
		Einheiten einheit = BfrFactory.eINSTANCE.createEinheiten();
		einheit.setId(jpaUnit.getId());
		einheit.setBeschreibung(jpaUnit.getBeschreibung());
		einheit.setEinheit(jpaUnit.getEinheit());
		return einheit;
	}

}
