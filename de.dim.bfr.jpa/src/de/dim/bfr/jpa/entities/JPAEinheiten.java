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
@Table(name = "Einheiten")
public class JPAEinheiten implements Serializable{

	private static final long serialVersionUID = -4298623138809282776L;

	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "Einheit")
	@Basic
	private String einheit;
	
    @Column(name = "Beschreibung")
	@Basic
	private String beschreibung;

	@OneToMany(mappedBy = "ngEinheit")
	private Collection<JPAAufbereitungsNachweisverfahren> aufbereitungsNachweisverfahrens;
	
	@OneToMany(mappedBy = "konzEinheit")
	private Collection<JPAMesswerte> messwertes;
	
	@OneToMany(mappedBy = "einheiten")
	private Collection<JPAMesswerteSonstiges> messwerteSonstigeses;
	
	@OneToMany(mappedBy = "einheiten")
	private Collection<JPAVersuchsbedingungenSonstiges> versuchsbedingungenSonstigeses;

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getEinheit() {
        return einheit;
    }

    public void setEinheit(String einheit) {
        this.einheit = einheit;
    }


    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JPAEinheiten that = (JPAEinheiten) o;

        if (id != that.id) return false;
        if (beschreibung != null ? !beschreibung.equals(that.beschreibung) : that.beschreibung != null) return false;
        if (einheit != null ? !einheit.equals(that.einheit) : that.einheit != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (einheit != null ? einheit.hashCode() : 0);
        result = 31 * result + (beschreibung != null ? beschreibung.hashCode() : 0);
        return result;
    }

	public Collection<JPAAufbereitungsNachweisverfahren> getAufbereitungsNachweisverfahrens() {
		return aufbereitungsNachweisverfahrens;
	}

	public void setAufbereitungsNachweisverfahrens(
			Collection<JPAAufbereitungsNachweisverfahren> aufbereitungsNachweisverfahrens) {
		this.aufbereitungsNachweisverfahrens = aufbereitungsNachweisverfahrens;
	}

	public Collection<JPAMesswerte> getMesswertes() {
		return messwertes;
	}

	public void setMesswertes(Collection<JPAMesswerte> messwertes) {
		this.messwertes = messwertes;
	}

	public Collection<JPAMesswerteSonstiges> getMesswerteSonstigeses() {
		return messwerteSonstigeses;
	}

	public void setMesswerteSonstigeses(
			Collection<JPAMesswerteSonstiges> messwerteSonstigeses) {
		this.messwerteSonstigeses = messwerteSonstigeses;
	}

	public Collection<JPAVersuchsbedingungenSonstiges> getVersuchsbedingungenSonstigeses() {
		return versuchsbedingungenSonstigeses;
	}

	public void setVersuchsbedingungenSonstigeses(
			Collection<JPAVersuchsbedingungenSonstiges> versuchsbedingungenSonstigeses) {
		this.versuchsbedingungenSonstigeses = versuchsbedingungenSonstigeses;
	}
    
}
