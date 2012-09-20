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

import org.eclipse.persistence.annotations.Convert;

/**
 * Created by IntelliJ IDEA.
 * User: sebastian
 * Date: 21.11.11
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "Messwerte_Sonstiges")
public class JPAMesswerteSonstiges implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Column(name = "Ja_Nein")//, length = 0, precision = 0)
	@Basic
	@Convert("booleanConverter")
	private Boolean jaNein;

	@Column(name = "Kommentar")//, length = 1023, precision = 0)
	@Basic
	private String kommentar;
	
	@ManyToOne
	@JoinColumn(name = "Wert", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPADoubleKennzahlen wert;

	@ManyToOne
	@JoinColumn(name = "Einheit", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAEinheiten einheiten;
	
	@ManyToOne
	@JoinColumn(name = "Messwerte", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAMesswerte messwerte;
	
	@ManyToOne
	@JoinColumn(name = "SonstigeParameter", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPASonstigeParameter sonstigeParameter;
	
	public JPAMesswerteSonstiges() {
		super();
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean isJaNein() {
        return jaNein;
    }

    public void setJaNein(Boolean jaNein) {
        this.jaNein = jaNein;
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

        JPAMesswerteSonstiges that = (JPAMesswerteSonstiges) o;

        if (id != that.id) return false;
        if (jaNein != that.jaNein) return false;
        if (messwerte != that.messwerte) return false;
        if (sonstigeParameter != that.sonstigeParameter) return false;
        if (kommentar != null ? !kommentar.equals(that.kommentar) : that.kommentar != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        Integer result;
        long temp;
        result = id;
        result = 31 * result + (jaNein ? 1 : 0);
        result = 31 * result + (kommentar != null ? kommentar.hashCode() : 0);
        return result;
    }


    public JPAMesswerte getMesswerte() {
		return messwerte;
	}

	public void setMesswerte(JPAMesswerte messwerte) {
		this.messwerte = messwerte;
	}

	public JPASonstigeParameter getSonstigeParameter() {
		return sonstigeParameter;
	}

	public void setSonstigeParameter(JPASonstigeParameter sonstigeParameter) {
		this.sonstigeParameter = sonstigeParameter;
	}

	public JPADoubleKennzahlen getWert() {
		return wert;
	}

	public void setWert(JPADoubleKennzahlen wert) {
		this.wert = wert;
	}

	public JPAEinheiten getEinheiten() {
		return einheiten;
	}

	public void setEinheiten(JPAEinheiten einheiten) {
		this.einheiten = einheiten;
	}

	public Boolean getJaNein() {
		return jaNein;
	}
}
