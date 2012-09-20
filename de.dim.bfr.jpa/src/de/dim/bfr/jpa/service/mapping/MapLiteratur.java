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
import de.dim.bfr.Literatur;
import de.dim.bfr.FreigabeTyp;
import de.dim.bfr.LiteraturTyp;
import de.dim.bfr.jpa.entities.JPALiteratur;

public class MapLiteratur {
	
	
	/**
	 * @param literatur
	 *            the instance of {@link Literatur} to create a JPA object from
	 * @return a call to
	 *         {@link #copyLiteraturPropertiesToJPALiteratur(Literatur, JPALiteratur)}
	 */
	public JPALiteratur createJPALiteraturFromLiteratur(Literatur literatur) {
		JPALiteratur jpaLiteratur = new JPALiteratur();
		return emfToJPA(literatur, jpaLiteratur);
	}
	
	/**
	 * @param literatur
	 *            the instance of {@link Literatur} to copy from
	 * @param jpaLiteratur
	 *            the instance of {@link JPALiteratur} to copy to
	 * @return an instance of {@link JPALiteratur}
	 */
	public JPALiteratur emfToJPA(
			Literatur literatur, JPALiteratur jpaLiteratur) {

		jpaLiteratur.setErstautor(literatur.getErstautor());
		jpaLiteratur.setFreigabeModus(literatur.getFreigabeModus().getValue());
		jpaLiteratur.setIssue(literatur.getIssue());
		jpaLiteratur.setJahr(literatur.getJahr());
		jpaLiteratur.setJournal(literatur.getJournal());
		jpaLiteratur.setKommentar(literatur.getKommentar());
		jpaLiteratur.setLiteraturAbstract(literatur.getLiteraturAbstract());
		jpaLiteratur.setLiteraturTyp(literatur.getLiteraturTyp().getValue());
		jpaLiteratur.setPaper(literatur.getPaper());
		jpaLiteratur.setSeite(literatur.getSeite());
		jpaLiteratur.setTitel(literatur.getTitel());
		jpaLiteratur.setVolume(literatur.getVolume());
		jpaLiteratur.setWebseite(literatur.getWebseite());
		return jpaLiteratur;
	}

	/**
	 * @param jpaLiteratur
	 *            the instance of {@link JPALiteratur} to copy from
	 * @return a new instance of {@link Literatur}
	 */
	/**
	 * @param jpaLiteratur
	 * @return
	 */
	public Literatur jpaToEMF(JPALiteratur jpaLiteratur) {
		Literatur literature = BfrFactory.eINSTANCE.createLiteratur();
		
		if (jpaLiteratur == null)
			return null;
		literature.setLiteraturAbstract(jpaLiteratur.getLiteraturAbstract());
		literature.setErstautor(jpaLiteratur.getErstautor());
		if (jpaLiteratur.getFreigabeModus() == null) {
			literature.setFreigabeModus(FreigabeTyp.get(0));
		} else {
			literature.setFreigabeModus(FreigabeTyp.get(jpaLiteratur
					.getFreigabeModus().intValue()));
		}
		literature.setId(jpaLiteratur.getId());
		literature.setIssue(jpaLiteratur.getIssue());
		if (jpaLiteratur.getJahr() != null)
			literature.setJahr(jpaLiteratur.getJahr());
		literature.setJournal(jpaLiteratur.getJournal());
		literature.setKommentar(jpaLiteratur.getKommentar());
		if (jpaLiteratur.getLiteraturTyp() == null) {
			literature.setLiteraturTyp(LiteraturTyp.get(0));
		} else {
			literature.setLiteraturTyp(LiteraturTyp.get(jpaLiteratur
					.getLiteraturTyp()));
		}
		literature.setPaper(jpaLiteratur.getPaper());
		if (jpaLiteratur.getSeite() != null)
			literature.setSeite(jpaLiteratur.getSeite());
		literature.setTitel(jpaLiteratur.getTitel());
		literature.setVolume(jpaLiteratur.getVolume());
		literature.setWebseite(jpaLiteratur.getWebseite());
		return literature;
	}

	
}
