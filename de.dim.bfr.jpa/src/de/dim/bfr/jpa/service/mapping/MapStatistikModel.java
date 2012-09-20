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
import de.dim.bfr.KlasseTyp;
import de.dim.bfr.LevelTyp;
import de.dim.bfr.SoftwareType;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.jpa.entities.JPALiteratur;
import de.dim.bfr.jpa.entities.JPAModell;
import de.dim.bfr.jpa.entities.JPAModellParameter;
import de.dim.bfr.jpa.internal.BFRJPAActivator;

public class MapStatistikModel {

	/**
	 * @param model
	 *            the instance of {@link StatistikModell} to copy from
	 * @param jpaModell
	 *            the instance of {@link JPAModell} to copy to
	 * @return an instance of {@link JPAModell}
	 */
	public JPAModell emfToJPA(
			final StatistikModell model, final JPAModell jpaModell) {
		jpaModell.setBeschreibung(model.getBeschreibung());
		jpaModell.setEingabedatum(model.getEingabedatum());
		jpaModell.setEingegebenVon(model.getBenutzer());
		jpaModell.setFormel(model.getFormel());
		try {
			jpaModell.setKlasse(model.getKlasse().getValue());			
		}
		catch (Exception e) {e.printStackTrace();}
		jpaModell.setKommentar(model.getKommentar());
		jpaModell.setLevel(model.getLevel().getValue());
		jpaModell.setName(model.getName());
		jpaModell.setNotation(model.getNotation());
		jpaModell.setSoftware(model.getSoftware().getLiteral());
		jpaModell.setTyp(model.getTyp());
		return jpaModell;
	}

	
	/**
	 * @param jpaModellkatalog
	 *            the instance of {@link JPAModell} to copy from
	 * @return new instance of {@link StatistikModell}
	 */
	public StatistikModell jpaToEMF(
			final JPAModell jpaModellkatalog) {
		if (jpaModellkatalog == null) {
			return null;
		}
		StatistikModell model = BfrFactory.eINSTANCE.createStatistikModell();
		model.setBeschreibung(jpaModellkatalog.getBeschreibung());
		model.setEingabedatum(jpaModellkatalog.getEingabedatum());
		model.setBenutzer(jpaModellkatalog.getEingegebenVon());
		model.setFormel(jpaModellkatalog.getFormel());
		model.setId(jpaModellkatalog.getId());
		try {
			Integer klasse = jpaModellkatalog.getKlasse();
			model.setKlasse(KlasseTyp.get(klasse == null ? 0 : klasse));
		}
		catch (Exception e) {e.printStackTrace();}

		model.setKommentar(jpaModellkatalog.getKommentar());
		model.setLevel(LevelTyp.get(jpaModellkatalog.getLevel()));
		model.setName(jpaModellkatalog.getName());
		model.setNotation(jpaModellkatalog.getNotation());
		model.setSoftware(SoftwareType.getByName(jpaModellkatalog.getSoftware()));
		model.setTyp(jpaModellkatalog.getTyp());
		
		for(JPALiteratur literatur  : jpaModellkatalog.getLiteratur()){
			model.getLiteratur().add(BFRJPAActivator.getDefault().getUIService().getLiteratureById(literatur.getId()));
		}

		MapStatistikModellParameter mapStatParam = new MapStatistikModellParameter();
		for (JPAModellParameter mp : jpaModellkatalog.getParameters()) {
			StatistikModellParameter smp = mapStatParam.jpaToEMF(mp);
			model.getParameter().add(smp);
		}
		return model;
	}
	
	/**
	 * @param model
	 *            the instance of {@link StatistikModell} to create a JPA object
	 *            from
	 * @return a call to
	 *         {@link #copyStatisticModelProperties2Entity(StatistikModell, JPAModell)}
	 */
	public JPAModell createJPAModellFromEMFObj(final StatistikModell model) {
		JPAModell jpaModell = new JPAModell();
		MapStatistikModel map = new MapStatistikModel();
		return map.emfToJPA(model, jpaModell);
	}
	
}
