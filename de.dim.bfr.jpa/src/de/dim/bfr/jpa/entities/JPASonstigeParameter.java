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
@Table(name = "SonstigeParameter")
public class JPASonstigeParameter implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
	@Column(name = "Parameter")
	@Basic
	private String parameter;
	
	@Column(name = "Beschreibung")
	@Basic
	private String beschreibung;
	
	@OneToMany(mappedBy = "sonstigeParameter")
	private Collection<JPAMesswerteSonstiges> messwerteSonstiges;
	
	@OneToMany(mappedBy = "sonstigeParameter")
	private Collection<JPAVersuchsbedingungenSonstiges> versuchsbedingungenSonstigeses;
	
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }


    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Collection<JPAMesswerteSonstiges> getMesswerteSonstiges() {
		return messwerteSonstiges;
	}

	public void setMesswerteSonstiges(
			Collection<JPAMesswerteSonstiges> messwerteSonstiges) {
		this.messwerteSonstiges = messwerteSonstiges;
	}

	public Collection<JPAVersuchsbedingungenSonstiges> getVersuchsbedingungenSonstigeses() {
		return versuchsbedingungenSonstigeses;
	}

	public void setVersuchsbedingungenSonstigeses(
			Collection<JPAVersuchsbedingungenSonstiges> versuchsbedingungenSonstigeses) {
		this.versuchsbedingungenSonstigeses = versuchsbedingungenSonstigeses;
	}

}
