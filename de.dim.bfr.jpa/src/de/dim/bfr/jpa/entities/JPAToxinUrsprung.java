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
@Table(name = "ToxinUrsprung")//, schema = "PUBLIC", catalog = "PUBLIC")
public class JPAToxinUrsprung implements Serializable{

	private static final long serialVersionUID = -7765079017699992606L;

	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Ursprung")//, length = 255, precision = 0)
	@Basic
	private String ursprung;
	
    @OneToMany(mappedBy = "toxinUrsprung")
	private Collection<JPAAgenzien> agenziens;
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getUrsprung() {
        return ursprung;
    }

    public void setUrsprung(String ursprung) {
        this.ursprung = ursprung;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JPAToxinUrsprung that = (JPAToxinUrsprung) o;

        if (id != that.id) return false;
        if (ursprung != null ? !ursprung.equals(that.ursprung) : that.ursprung != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        Integer result = id;
        result = 31 * result + (ursprung != null ? ursprung.hashCode() : 0);
        return result;
    }

	public Collection<JPAAgenzien> getAgenziens() {
		return agenziens;
	}

	public void setAgenziens(Collection<JPAAgenzien> agenziens) {
		this.agenziens = agenziens;
	}
}
