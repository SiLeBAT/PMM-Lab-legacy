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

/**
 * Created by IntelliJ IDEA.
 * User: sebastian
 * Date: 21.11.11
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "GeschaetzteParameter")
public class JPAGeschaetzteParameter implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "Wert")
	@Basic
	private Double wert;
	
    @Column(name="KI.unten")
	@Basic
	private Double kiUnten;
	
    @Column(name = "KI.oben")
	@Basic
	private Double kiOben;
	
    @Column(name = "SD")
	@Basic
	private Double sd;
	
    @Column(name = "t")
	@Basic
	private Double t;
	
    @Column(name = "p")
	@Basic
	private Double p;
    
	@ManyToOne(optional = false)
	@JoinColumn(name = "GeschaetztesModell")//, referencedColumnName = "ID", nullable = false, updatable= false , insertable = false)
	private JPAGeschaetzteModelle geschaetztesModell;
	
	@ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "Parameter")//, referencedColumnName = "ID", nullable = false, updatable = false, insertable = false)
	private JPAModellParameter modellParameter;
	
	@OneToMany(mappedBy = "param1", orphanRemoval = true)
	private Collection<JPAParameterCovCor> parameter1CovCors;
	
	@OneToMany(mappedBy = "param2", orphanRemoval = true)
	private Collection<JPAParameterCovCor> parameter2CovCors;
	
	public JPAGeschaetzteParameter() {
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getWert() {
        return wert;
    }
    
	public void setWert(Double wert) {
        this.wert = wert;
    }

    public Double getKiUnten() {
		return kiUnten;
	}

	public void setKiUnten(Double kiUnten) {
		this.kiUnten = kiUnten;
	}

	public Double getKiOben() {
		return kiOben;
	}

	public void setKiOben(Double kiOben) {
		this.kiOben = kiOben;
	}

	public Double getSd() {
        return sd;
    }

    public void setSd(Double sd) {
        this.sd = sd;
    }


    public Double getT() {
        return t;
    }

    public void setT(Double t) {
        this.t = t;
    }


    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public JPAGeschaetzteModelle getGeschaetztesModell() {
		return geschaetztesModell;
	}

	public void setGeschaetztesModell(JPAGeschaetzteModelle geschaetztesModell) {
		this.geschaetztesModell = geschaetztesModell;
	}
	
	public JPAModellParameter getModellParameter() {
		return modellParameter;
	}

	public void setModellParameter(JPAModellParameter modellParameter) {
		this.modellParameter = modellParameter;
	}

	public Collection<JPAParameterCovCor> getParameter1CovCors() {
		return parameter1CovCors;
	}

	public void setParameter1CovCors(
			Collection<JPAParameterCovCor> parameter1CovCors) {
		this.parameter1CovCors = parameter1CovCors;
	}

	public Collection<JPAParameterCovCor> getParameter2CovCors() {
		return parameter2CovCors;
	}

	public void setParameter2CovCors(
			Collection<JPAParameterCovCor> parameter2CovCors) {
		this.parameter2CovCors = parameter2CovCors;
	}
	
}
