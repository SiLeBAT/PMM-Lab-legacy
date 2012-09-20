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
import de.dim.bfr.Messwerte;
import de.dim.bfr.jpa.entities.JPAMesswerte;
import de.dim.bfr.jpa.entities.JPAModell;

public class MapMesswerte {

		/**
	 * @param jpamw
	 *            the instance of {@link JPAModell} to copy from
	 * @return an new instance of {@link Messwerte}
	 */
	public Messwerte jpaToEMF(JPAMesswerte jpamw) {
		MapEinheiten mapEinheiten = new MapEinheiten();
		MapDoubleKennzahlen mapKennzahlen = new MapDoubleKennzahlen();
		MapVersuchsbedingung mapVersuchsbedingung = new MapVersuchsbedingung();
		if (jpamw == null)
			return null;
		Messwerte mw = BfrFactory.eINSTANCE.createMesswerte();
		if (jpamw.getAw() != null)
			mw.setAw(mapKennzahlen.copyJpaToEMF(jpamw.getAw()));
		if (jpamw.getCo2() != null)
			mw.setCo2(mapKennzahlen.copyJpaToEMF(jpamw.getCo2()));
		if (jpamw.getDruck() != null)
			mw.setDruck(mapKennzahlen.copyJpaToEMF(jpamw.getDruck()));
		if (jpamw.getKonzentration() != null)
			mw.setKonzentration(mapKennzahlen.copyJpaToEMF(jpamw
					.getKonzentration()));
		mw.setKonzEinheit(mapEinheiten.jpaToEMF(jpamw.getKonzEinheit()));
		if (jpamw.getpH() != null)
			mw.setPH(mapKennzahlen.copyJpaToEMF(jpamw.getpH()));
		if (jpamw.getTemperatur() != null)
			mw.setTemperatur(mapKennzahlen.copyJpaToEMF(jpamw
					.getTemperatur()));
		if (jpamw.getZeit() != null)
			mw.setZeit(mapKennzahlen.copyJpaToEMF(jpamw.getZeit()));
		mw.setZeitEinheit(jpamw.getZeitEinheit());
		if (jpamw.getGeprueft() != null)
			mw.setGeprueft(jpamw.getGeprueft());
		mw.setId(jpamw.getId());
		mw.setKommentar(jpamw.getKommentar());
		mw.setVersuchsbedingungen(mapVersuchsbedingung.jpaToEMF(jpamw
				.getVersuchsbedingungen()));

		return mw;
	}


}
