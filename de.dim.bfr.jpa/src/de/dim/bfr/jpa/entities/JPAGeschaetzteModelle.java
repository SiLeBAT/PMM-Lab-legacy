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

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.PrivateOwned;

/**
 * Created by IntelliJ IDEA.
 * User: sebastian
 * Date: 21.11.11
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "GeschaetzteModelle")
public class JPAGeschaetzteModelle implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(name = "manuellEingetragen")
	@Basic
	@Convert("booleanConverter")
	private Boolean manuellEingetragen;
	
	@Column(name = "Rsquared")
	@Basic
	private Double rsquared;
	
	@Column(name = "RSS")
	@Basic
	private Double rss;
	
	@Column(name = "Score")
	@Basic
	private Integer score;

	@Column(name = "Kommentar")
	@Lob
	@Basic
	private String kommentar;
	
	@ManyToOne
	@JoinColumn(name = "Modell", referencedColumnName = "ID", nullable = false)
	private JPAModell modell;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}) 
	@JoinColumn(name = "Versuchsbedingung", referencedColumnName = "ID")
	private JPAVersuchsbedingungen versuchsbedingung;

	@ManyToOne
	@JoinColumn(name = "Response", referencedColumnName = "ID")
	private JPAModellParameter responseParameter;
	
	@OneToMany(mappedBy = "geschaetztesModell", cascade = CascadeType.ALL, orphanRemoval = true)
	@PrivateOwned
	private Collection<JPAGeschaetzteParameter> geschaetzteParameter;
	
	@ManyToMany
	@JoinTable(name = "GeschaetztesModell_Referenz",
			joinColumns = @javax.persistence.JoinColumn(name = "GeschaetztesModell"),
			inverseJoinColumns = @javax.persistence.JoinColumn(name = "Literatur"))
	private Collection<JPALiteratur> literatur;
	
	@ManyToMany
	@JoinTable(name = "Sekundaermodelle_Primaermodelle",
			joinColumns = @javax.persistence.JoinColumn(name = "GeschaetztesSekundaermodell"),
			inverseJoinColumns = @javax.persistence.JoinColumn(name = "GeschaetztesPrimaermodell"))
	private Collection<JPAGeschaetzteModelle> primaermodelle;
	
	@ManyToMany
	@JoinTable(name = "Sekundaermodelle_Primaermodelle",
			joinColumns = @javax.persistence.JoinColumn(name = "GeschaetztesPrimaermodell"),
			inverseJoinColumns = @javax.persistence.JoinColumn(name = "GeschaetztesSekundaermodell"))
	private Collection<JPAGeschaetzteModelle> sekundaermodelle;
    
	@OneToMany(mappedBy = "geschaetztesModell", cascade = {CascadeType.ALL}, orphanRemoval = true)	
	private Collection<JPAParameterCovCor> geschaetzteParameterCovCors;


	public JPAGeschaetzteModelle() {
	}

	public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Boolean isManuellEingetragen() {
        return manuellEingetragen;
    }

    public void setManuellEingetragen(final Boolean manuellEingetragen) {
        this.manuellEingetragen = manuellEingetragen;
    }

    public Double getRsquared() {
        return rsquared;
    }

    public void setRsquared(final Double rsquared) {
        this.rsquared = rsquared;
    }

    public Double getRss() {
        return rss;
    }

    public void setRss(final Double rss) {
        this.rss = rss;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(final Integer score) {
        this.score = score;
    }


    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(final String kommentar) {
        this.kommentar = kommentar;
    }

    public JPAModell getModell() {
		return modell;
	}

	public void setModell(final JPAModell modell) {
		this.modell = modell;
	}

    public JPAVersuchsbedingungen getVersuchsbedingung() {
		return versuchsbedingung;
	}

	public void setVersuchsbedingung(final JPAVersuchsbedingungen versuchsbedingung) {
		this.versuchsbedingung = versuchsbedingung;
	}

    public Collection<JPAGeschaetzteParameter> getGeschaetzteParameter() {
		return geschaetzteParameter;
	}

	public void setGeschaetzteParameter(
			final Collection<JPAGeschaetzteParameter> geschaetzteParameter) {
		this.geschaetzteParameter = geschaetzteParameter;
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
		if (!literatur.getGeschaetzteModelle().contains(this)) {
			literatur.getGeschaetzteModelle().add(this);
		}
	}

	public void removeLiteratur(final JPALiteratur literatur) {
		if (getLiteratur().contains(literatur)) {
			getLiteratur().remove(literatur);
		}
		if (literatur.getGeschaetzteModelle().contains(this)) {
			literatur.getGeschaetzteModelle().remove(this);
		}
	}

	public Collection<JPAGeschaetzteModelle> getPrimaermodelle() {
		return primaermodelle;
	}

	public void setPrimaermodelle(
			final Collection<JPAGeschaetzteModelle> primaermodelle) {
		this.primaermodelle = primaermodelle;
	}

	public Collection<JPAGeschaetzteModelle> getSekundaermodelle() {
		return sekundaermodelle;
	}

	public void setSekundaermodelle(
			final Collection<JPAGeschaetzteModelle> sekundaermodelle) {
		this.sekundaermodelle = sekundaermodelle;
	}

	public JPAModellParameter getResponseParameter() {
		return responseParameter;
	}

	public void setResponseParameter(final JPAModellParameter responseParameter) {
		this.responseParameter = responseParameter;
	}
	
	public Collection<JPAParameterCovCor> getGeschaetzteParameterCovCors() {
        return geschaetzteParameterCovCors;
    }

    public void setGeschaetzteParameterCovCors(final Collection<JPAParameterCovCor> geschaetzteParameterCovCors) {
        this.geschaetzteParameterCovCors = geschaetzteParameterCovCors;
    }
}
