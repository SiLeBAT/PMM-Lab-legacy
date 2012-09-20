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

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "GeschaetzteParameterCovCor")
public class JPAParameterCovCor implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Column(name = "cor", nullable = false, insertable = true, updatable = true, length = 0, precision = 0)
	@Basic
	@Convert("booleanConverter")
	private Boolean cor;
	
	@Column(name = "Wert")
	@Basic
	private Double wert;
	
	@ManyToOne
	@JoinColumn(name = "GeschaetztesModell")
	private JPAGeschaetzteModelle geschaetztesModell;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "param1")
	private JPAGeschaetzteParameter param1;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name = "param2")
	private JPAGeschaetzteParameter param2;
	
	public JPAParameterCovCor() {
		super();
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public Boolean isCor() {
        return cor;
    }

    public void setCor(Boolean cor) {
        this.cor = cor;
    }


    public Double getWert() {
        return wert;
    }

    public void setWert(Double wert) {
        this.wert = wert;
    }

	public JPAGeschaetzteModelle getGeschaetztesModell() {
		return geschaetztesModell;
	}

	public void setGeschaetztesModell(JPAGeschaetzteModelle geschaetztesModell) {
		this.geschaetztesModell = geschaetztesModell;
	}

	public JPAGeschaetzteParameter getParam1() {
		return param1;
	}

	public void setParam1(JPAGeschaetzteParameter param1) {
		this.param1 = param1;
	}

	public JPAGeschaetzteParameter getParam2() {
		return param2;
	}

	public void setParam2(JPAGeschaetzteParameter param2) {
		this.param2 = param2;
	}
}
