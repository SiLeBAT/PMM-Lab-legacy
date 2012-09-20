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
@Table(name = "Methodiken")
public class JPAMethodiken implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Column(name = "Name")
	@Basic
	private String name;
	
	@Column(name = "Beschreibung")
	@Basic
	private String beschreibung;
	
	@Column(name = "Kurzbezeichnung")
	@Basic
	private String kurzbezeichnung;
	
	@Column(name = "WissenschaftlicheBezeichnung")
	@Basic
	private String wissenschaftlicheBezeichnung;
	
	@Column(name = "Katalogcodes")
	@Basic
	private Integer katalogcodes;
	
	@Column(name = "Kommentar")
	@Basic
	private String kommentar;
	
	@OneToMany(mappedBy = "methodiken")
	private Collection<JPANachweisverfahren> nachweisverfahren;
	
	public JPAMethodiken() {
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JPAMethodiken that = (JPAMethodiken) o;

        if (id != that.id) return false;
        if (katalogcodes != that.katalogcodes) return false;
        if (beschreibung != null ? !beschreibung.equals(that.beschreibung) : that.beschreibung != null) return false;
        if (kommentar != null ? !kommentar.equals(that.kommentar) : that.kommentar != null) return false;
        if (kurzbezeichnung != null ? !kurzbezeichnung.equals(that.kurzbezeichnung) : that.kurzbezeichnung != null)
            return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (wissenschaftlicheBezeichnung != null ? !wissenschaftlicheBezeichnung.equals(that.wissenschaftlicheBezeichnung) : that.wissenschaftlicheBezeichnung != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        Integer result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (beschreibung != null ? beschreibung.hashCode() : 0);
        result = 31 * result + (kurzbezeichnung != null ? kurzbezeichnung.hashCode() : 0);
        result = 31 * result + (wissenschaftlicheBezeichnung != null ? wissenschaftlicheBezeichnung.hashCode() : 0);
        result = 31 * result + katalogcodes;
        result = 31 * result + (kommentar != null ? kommentar.hashCode() : 0);
        return result;
    }

	public Collection<JPANachweisverfahren> getNachweisverfahren() {
		return nachweisverfahren;
	}

	public void setNachweisverfahren(
			Collection<JPANachweisverfahren> nachweisverfahren) {
		this.nachweisverfahren = nachweisverfahren;
	}


}
