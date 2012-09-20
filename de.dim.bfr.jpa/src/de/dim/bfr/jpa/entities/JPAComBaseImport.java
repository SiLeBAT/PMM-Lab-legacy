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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: sebastian
 * Date: 21.11.11
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "ComBaseImport")
public class JPAComBaseImport {

    @Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "Agensname")
	@Basic
	private String agensname;
	
    @Column(name = "Agenskatalog")
	@Basic
	private Integer agenskatalog;
	
    @Column(name = "b_f")
	@Basic
	private String bF;
	
    @Column(name = "Matrixname")
	@Basic
	private String matrixname;
	
    @Column(name = "Matrixkatalog")
	@Basic
	private Integer matrixkatalog;

	@ManyToOne
	@JoinColumn(name = "Agenskatalog", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAAgenzien agenzien;
	
	@ManyToOne
	@JoinColumn(name = "Matrixkatalog", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAMatrices matrices;
	
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


    public Integer getAgenskatalog() {
        return agenskatalog;
    }

    public void setAgenskatalog(Integer agenskatalog) {
        this.agenskatalog = agenskatalog;
    }


    public String getbF() {
        return bF;
    }

    public void setbF(String bF) {
        this.bF = bF;
    }


    public String getMatrixname() {
        return matrixname;
    }

    public void setMatrixname(String matrixname) {
        this.matrixname = matrixname;
    }


    public Integer getMatrixkatalog() {
        return matrixkatalog;
    }

    public void setMatrixkatalog(Integer matrixkatalog) {
        this.matrixkatalog = matrixkatalog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JPAComBaseImport that = (JPAComBaseImport) o;

        if (agenskatalog != that.agenskatalog) return false;
        if (id != that.id) return false;
        if (matrixkatalog != that.matrixkatalog) return false;
        if (agensname != null ? !agensname.equals(that.agensname) : that.agensname != null) return false;
        if (bF != null ? !bF.equals(that.bF) : that.bF != null) return false;
        if (matrixname != null ? !matrixname.equals(that.matrixname) : that.matrixname != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (agensname != null ? agensname.hashCode() : 0);
        result = 31 * result + agenskatalog;
        result = 31 * result + (bF != null ? bF.hashCode() : 0);
        result = 31 * result + (matrixname != null ? matrixname.hashCode() : 0);
        result = 31 * result + matrixkatalog;
        return result;
    }

	public JPAAgenzien getAgenzien() {
		return agenzien;
	}

	public void setAgenzien(JPAAgenzien agenzien) {
		this.agenzien = agenzien;
	}

	public JPAMatrices getMatrices() {
		return matrices;
	}

	public void setMatrices(JPAMatrices matrices) {
		this.matrices = matrices;
	}
}
