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
package de.dim.bfr.jpa.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: sebastian
 * Date: 21.11.11
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "Agenzien")
public class JPAAgenzien implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
	@Column(name = "Agensname")
	@Basic
	private String agensname;
	
	@Column(name = "Kurzbezeichnung")
	@Basic
	private String kurzbezeichnung;
	
	@Column(name = "WissenschaftlicheBezeichnung")
	@Basic
	private String wissenschaftlicheBezeichnung;
	
	@Column(name = "Klassifizierung")
	@Basic
	private Integer klassifizierung;
	
	@Column(name = "Familie")
	@Basic
	private String familie;
	
	@Column(name = "Gattung")
	@Basic
	private String gattung;
	
	@Column(name = "Spezies")
	@Basic
	private String spezies;
	
	@Column(name = "Subspezies_Subtyp")
	@Basic
	private String subspeziesSubtyp;
	
	@Column(name = "Risikogruppe")
	@Basic
	private Integer risikogruppe;
	
	@Column(name = "Humanpathogen")
	@Basic
	private Integer humanpathogen;
	
	@Column(name = "Ursprung")
	@Basic
	private Integer ursprung;

	@Column(name = "Gramfaerbung")
	@Basic
	private Integer gramfaerbung;
	
	@Column(name = "CAS_Nummer")
	@Basic
	private String casNummer;
	
	@Column(name = "Carver_Nummer")
	@Basic
	private String carverNummer;
	
	@Column(name = "FaktSheet")
	@Basic
	private String faktSheet;
	
	@Column(name = "Katalogcodes")
	@Basic
	private Integer katalogcodes;
	
	@Column(name = "Kommentar")
	@Basic
	private String kommentar;
	
	@ManyToOne
	@JoinColumn(name = "Ursprung", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAToxinUrsprung toxinUrsprung;
	
	@OneToMany(mappedBy = "agenzien")
	private Collection<JPAAufbereitungsverfahren> aufbereitungsverfahrens;
	
	@OneToMany(mappedBy = "basis")
	private Collection<JPACodesAgenzien> codesAgenziens;
	
	@OneToMany(mappedBy = "agenzien")
	private Collection<JPAComBaseImport> comBaseImports;
	
	@OneToMany(mappedBy = "agens")
	private Collection<JPANachweisverfahren> nachweisverfahrens;
	
	@OneToMany(mappedBy = "agens")
	private Collection<JPAVersuchsbedingungen> versuchsbedingungen;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAgensname() {
		return agensname;
	}

	public void setAgensname(String agensname) {
		this.agensname = agensname;
	}

	public String getKurzbezeichnung() {
		return kurzbezeichnung;
	}

	public void setKurzbezeichnung(String kurzbezeichnung) {
		this.kurzbezeichnung = kurzbezeichnung;
	}

	public String getWissenschaftlicheBezeichnung() {
		return wissenschaftlicheBezeichnung;
	}

	public void setWissenschaftlicheBezeichnung(String wissenschaftlicheBezeichnung) {
		this.wissenschaftlicheBezeichnung = wissenschaftlicheBezeichnung;
	}

	public Integer getKlassifizierung() {
		return klassifizierung;
	}

	public void setKlassifizierung(Integer klassifizierung) {
		this.klassifizierung = klassifizierung;
	}

	public String getFamilie() {
		return familie;
	}

	public void setFamilie(String familie) {
		this.familie = familie;
	}

	public String getGattung() {
		return gattung;
	}

	public void setGattung(String gattung) {
		this.gattung = gattung;
	}

	public String getSpezies() {
		return spezies;
	}

	public void setSpezies(String spezies) {
		this.spezies = spezies;
	}

	public String getSubspeziesSubtyp() {
		return subspeziesSubtyp;
	}

	public void setSubspeziesSubtyp(String subspeziesSubtyp) {
		this.subspeziesSubtyp = subspeziesSubtyp;
	}

	public Integer getRisikogruppe() {
		return risikogruppe;
	}

	public void setRisikogruppe(Integer risikogruppe) {
		this.risikogruppe = risikogruppe;
	}

	public Integer getHumanpathogen() {
		return humanpathogen;
	}

	public void setHumanpathogen(Integer humanpathogen) {
		this.humanpathogen = humanpathogen;
	}

	public Integer getUrsprung() {
		return ursprung;
	}

	public void setUrsprung(Integer ursprung) {
		this.ursprung = ursprung;
	}

	public Integer getGramfaerbung() {
		return gramfaerbung;
	}

	public void setGramfaerbung(Integer gramfaerbung) {
		this.gramfaerbung = gramfaerbung;
	}

	public String getCasNummer() {
		return casNummer;
	}

	public void setCasNummer(String casNummer) {
		this.casNummer = casNummer;
	}

	public String getCarverNummer() {
		return carverNummer;
	}

	public void setCarverNummer(String carverNummer) {
		this.carverNummer = carverNummer;
	}

	public String getFaktSheet() {
		return faktSheet;
	}

	public void setFaktSheet(String faktSheet) {
		this.faktSheet = faktSheet;
	}

	public Integer getKatalogcodes() {
		return katalogcodes;
	}

	public void setKatalogcodes(Integer katalogcodes) {
		this.katalogcodes = katalogcodes;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public JPAToxinUrsprung getToxinUrsprung() {
		return toxinUrsprung;
	}

	public void setToxinUrsprung(JPAToxinUrsprung toxinUrsprung) {
		this.toxinUrsprung = toxinUrsprung;
	}

	public Collection<JPAAufbereitungsverfahren> getAufbereitungsverfahrens() {
		return aufbereitungsverfahrens;
	}

	public void setAufbereitungsverfahrens(
			Collection<JPAAufbereitungsverfahren> aufbereitungsverfahrens) {
		this.aufbereitungsverfahrens = aufbereitungsverfahrens;
	}

	public Collection<JPACodesAgenzien> getCodesAgenziens() {
		return codesAgenziens;
	}

	public void setCodesAgenziens(Collection<JPACodesAgenzien> codesAgenziens) {
		this.codesAgenziens = codesAgenziens;
	}

	public Collection<JPAComBaseImport> getComBaseImports() {
		return comBaseImports;
	}

	public void setComBaseImports(Collection<JPAComBaseImport> comBaseImports) {
		this.comBaseImports = comBaseImports;
	}

	public Collection<JPANachweisverfahren> getNachweisverfahrens() {
		return nachweisverfahrens;
	}

	public void setNachweisverfahrens(
			Collection<JPANachweisverfahren> nachweisverfahrens) {
		this.nachweisverfahrens = nachweisverfahrens;
	}

	public Collection<JPAVersuchsbedingungen> getVersuchsbedingungen() {
		return versuchsbedingungen;
	}

	public void setVersuchsbedingungen(
			Collection<JPAVersuchsbedingungen> versuchsbedingungen) {
		this.versuchsbedingungen = versuchsbedingungen;
	}
}
