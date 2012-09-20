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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Created by IntelliJ IDEA.
 * User: sebastian
 * Date: 21.11.11
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "Modellkatalog")
public class JPAModell implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Column(name = "Name")
	@Basic
	private String name;
	
	@Column(name = "Notation")
	@Basic
	private String notation;
	
	@Column(name = "Level")
	@Basic
	private Integer level;
	
	@Column(name = "Klasse")
	@Basic
	private Integer klasse;
	
	@Column(name = "Typ")
	@Basic
	private String typ;
	
	@Column(name = "Eingabedatum")
	@Temporal(value = TemporalType.DATE)
	private Date eingabedatum;
	
	@Column(name = "eingegeben_von")
	@Basic
	private String eingegebenVon;
	
	@Column(name = "Beschreibung")
	@Lob
	@Basic
	private String beschreibung;
	
	@Column(name = "Formel")
	@Basic
	private String formel;
	
	@Column(name = "Software")
	@Basic
	private String software;
	
	@Column(name = "Kommentar")
	@Basic
	private String kommentar;
	
	@OneToMany(mappedBy = "modell")
	private Collection<JPAGeschaetzteModelle> geschaetzteModelle;
	
	@ManyToMany
	@JoinTable(name = "Modell_Referenz",
			joinColumns = @javax.persistence.JoinColumn(name = "Modell"),
			inverseJoinColumns = @javax.persistence.JoinColumn(name = "Literatur"))
//	@ManyToMany(mappedBy = "modelle")
	private Collection<JPALiteratur> literatur;
	
	@OneToMany(mappedBy = "modell", cascade = CascadeType.ALL, orphanRemoval = true)
	private Collection<JPAModellParameter> parameters;
	
	public JPAModell() {}

	public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getNotation() {
        return notation;
    }

    public void setNotation(final String notation) {
        this.notation = notation;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(final Integer level) {
        this.level = level;
    }


    public Integer getKlasse() {
        return klasse;
    }

    public void setKlasse(final Integer klasse) {
        this.klasse = klasse;
    }


    public String getTyp() {
        return typ;
    }

    public void setTyp(final String typ) {
        this.typ = typ;
    }


    public Date getEingabedatum() {
        return eingabedatum;
    }

    public void setEingabedatum(final Date eingabedatum) {
        this.eingabedatum = eingabedatum;
    }


    public String getEingegebenVon() {
        return eingegebenVon;
    }

    public void setEingegebenVon(final String eingegebenVon) {
        this.eingegebenVon = eingegebenVon;
    }


    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(final String beschreibung) {
        this.beschreibung = beschreibung;
    }


    public String getFormel() {
        return formel;
    }

    public void setFormel(final String formel) {
        this.formel = formel;
    }


    public String getSoftware() {
        return software;
    }

    public void setSoftware(final String software) {
        this.software = software;
    }


    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(final String kommentar) {
        this.kommentar = kommentar;
    }

    public Collection<JPALiteratur> getLiteratur() {
		return literatur;
	}

	public void setLiteratur(final Collection<JPALiteratur> literatur) {
		this.literatur = literatur;
	}

	public void addLiteratur(final JPALiteratur literatur) {
		if (getLiteratur() == null) {
			setLiteratur(new ArrayList<JPALiteratur>());
		}
		if (literatur.getModelle() == null) {
			literatur.setModelle(new ArrayList<JPAModell>());
		}
		if (!getLiteratur().contains(literatur)) {
			getLiteratur().add(literatur);
		}
		if (!literatur.getModelle().contains(this)) {
			literatur.getModelle().add(this);
		}
	}
	
	public void removeLiteratur(final JPALiteratur literatur) {
		if (getLiteratur().contains(literatur)) {
			getLiteratur().remove(literatur);
		}
		if (literatur.getModelle().contains(this)) {
			literatur.getModelle().remove(this);
		}
	}
	
	public Collection<JPAGeschaetzteModelle> getGeschaetzteModelle() {
        return geschaetzteModelle;
    }

    public void setGeschaetzteModelle(final Collection<JPAGeschaetzteModelle> geschaetzteModelle) {
        this.geschaetzteModelle = geschaetzteModelle;
    }

    public Collection<JPAModellParameter> getParameters() {
		return parameters;
	}

	public void setParameters(final Collection<JPAModellParameter> parameters) {
		this.parameters = parameters;
	}
    
    
}
