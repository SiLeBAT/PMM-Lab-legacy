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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Convert;

/**
 * Created by IntelliJ IDEA.
 * User: sebastian
 * Date: 21.11.11
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "ModellkatalogParameter")
public class JPAModellParameter implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(name = "Parametername", nullable = false, insertable = true, updatable = true, length = 127, precision = 0)
	@Basic
	private String parametername;
	
	@Column(name="Parametertyp")
	@Basic 
	private Integer parameterTyp;
	
	@Column(name="ganzzahl")
	@Basic
	@Convert("booleanConverter")
	private Boolean ganzzahl;
	
	@Column(name = "min")
	@Basic
	private Double min;
	
	@Column(name = "max")
	@Basic
	private Double max;
	
	@Column(name = "Beschreibung")
	@Basic
	private String beschreibung;
	
	@OneToMany(mappedBy = "responseParameter", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
	private Collection<JPAGeschaetzteModelle> geschaetzteModelle;

	@OneToMany(mappedBy = "modellParameter", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Collection<JPAGeschaetzteParameter> parameter;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "Modell")
	private JPAModell modell;
	
	
	public JPAModellParameter() {
	}
	
	public JPAModellParameter(JPAModell modell) {
		this.modell = modell;
	}
	
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParametername() {
        return parametername;
    }

    public void setParametername(String parametername) {
        this.parametername = parametername;
    }

    public Integer getParameterTyp() {
		return parameterTyp;
	}

	public void setParameterTyp(Integer parameterTyp) {
		this.parameterTyp = parameterTyp;
	}

	public Boolean isGanzzahl() {
		return ganzzahl;
	}

	public void setGanzzahl(Boolean ganzzahl) {
		this.ganzzahl = ganzzahl;
	}

	public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }


    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }


    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public
    JPAModell getModell() {
        return modell;
    }

    public void setModell(JPAModell modell) {
        this.modell = modell;
    }

	public void setGeschaetzteModelle(Collection<JPAGeschaetzteModelle> geschaetzteModelle) {
		this.geschaetzteModelle = geschaetzteModelle;
	}

	public Collection<JPAGeschaetzteModelle> getGeschaetzteModelle() {
		return geschaetzteModelle;
	}

	public Collection<JPAGeschaetzteParameter> getParameter() {
		return parameter;
	}

	public void setParameter(Collection<JPAGeschaetzteParameter> parameter) {
		this.parameter = parameter;
	}

}
