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
import de.dim.bfr.DoubleKennzahlen;
import de.dim.bfr.jpa.entities.JPADoubleKennzahlen;

public class MapDoubleKennzahlen {
	
	/**
	 * @param jpadk the instance of {@link JPADoubleKennzahlen} to copy from
	 * @return instance of {@link DoubleKennzahlen}
	 */
	public DoubleKennzahlen copyJpaToEMF(
			JPADoubleKennzahlen jpadk) {
		if (jpadk == null)
			return null;
		DoubleKennzahlen dk = BfrFactory.eINSTANCE.createDoubleKennzahlen();
		dk.setId(jpadk.getId());
		if (jpadk.getWert() != null)
			dk.setWert(jpadk.getWert());
		return dk;
	}
	
		/**
	 * @param jpaVal
	 *            the instance of {@link JPADoubleKennzahlen} to copy from
	 * @return an new instance of {@link DoubleKennzahlen}
	 */
//	private DoubleKennzahlen copyEntityPropertiesToDoubleKennzahlen(
	public DoubleKennzahlen jpaToEMF(
			JPADoubleKennzahlen jpaVal) {
		if (jpaVal == null)
			return null;
		DoubleKennzahlen values = BfrFactory.eINSTANCE.createDoubleKennzahlen();
		values.setFunctionX(jpaVal.getFunktionX());
		values.setFunctionXG(jpaVal.isFunktionXG());
		values.setFunctionZeit(jpaVal.getFunktionZeit());
		values.setFunctionZeitG(jpaVal.isFunktionZeitG());
		values.setId(jpaVal.getId());
		values.setLcl95(jpaVal.getLcl95());
		values.setLcl95G(jpaVal.isLcl95G());
		values.setMaximum(jpaVal.getMaximum());
		values.setMaximumG(jpaVal.isMaximumG());
		values.setMinimum(jpaVal.getMinimum());
		values.setMinimumG(jpaVal.isMinimumG());
		values.setStandardabweichung(jpaVal.getStandardabweichung());
		values.setStandardabweichungG(jpaVal.isStandardabweichungG());
		values.setTimes10PowerOf(jpaVal.getTimes10PowerOf());
		values.setUcl95(jpaVal.getUcl95());
		values.setUcl95G(jpaVal.isUcl95G());
		values.setUndefinedND(jpaVal.isUndefiniertND());
		values.setVerteilung(jpaVal.getVerteilung());
		values.setVerteilungG(jpaVal.isVerteilungG());
		values.setWert(jpaVal.getWert());
		values.setWertG(jpaVal.isWertG());
		values.setWertTyp(jpaVal.getWertTyp());
		values.setWiederholungenG(jpaVal.isWiederholungenG());
		values.setX(jpaVal.getX());
		return values;
	}

	
}
