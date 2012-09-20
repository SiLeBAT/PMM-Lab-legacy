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
@Table(name = "Codes_Matrices")
public class JPACodesMatrices implements Serializable{

	private static final long serialVersionUID = 4223981895318253555L;

	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "CodeSystem")
	@Basic
	private String codeSystem;
	
    @Column(name = "Code")
	@Basic
	private String code;
	
    @Column(name = "Kommentar")
	@Basic
	private String kommentar;
	
    @ManyToOne
	@JoinColumn(name = "Basis", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAMatrices basis;
	
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getCodeSystem() {
        return codeSystem;
    }

    public void setCodeSystem(String codeSystem) {
        this.codeSystem = codeSystem;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JPACodesMatrices that = (JPACodesMatrices) o;

        if (basis.equals(that.basis)) return false;
        if (id != that.id) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (codeSystem != null ? !codeSystem.equals(that.codeSystem) : that.codeSystem != null) return false;
        if (kommentar != null ? !kommentar.equals(that.kommentar) : that.kommentar != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (codeSystem != null ? codeSystem.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (kommentar != null ? kommentar.hashCode() : 0);
        return result;
    }

	public JPAMatrices getBasis() {
		return basis;
	}

	public void setBasis(JPAMatrices basis) {
		this.basis = basis;
	}
}
