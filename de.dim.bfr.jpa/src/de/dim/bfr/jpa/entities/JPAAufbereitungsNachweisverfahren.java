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
@Table(name = "Aufbereitungs_Nachweisverfahren")
public class JPAAufbereitungsNachweisverfahren implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(name = "Nachweisgrenze")
	@Basic
	private Double nachweisgrenze;
	
	@Column(name = "Sensitivitaet")
	@Basic
	private Double sensitivitaet;
	
	@Column(name = "Spezifitaet")
	@Basic
	private Double spezifitaet;
	
	@Column(name = "Effizienz")
	@Basic
	private Double effizienz;
	
	@Column(name = "Wiederfindungsrate")
	@Basic
	private Double wiederfindungsrate;
	
	@Column(name = "Guetescore")
	@Basic
	private Integer guetescoreSubj;

	@Column(name = "Kommentar")
	@Basic
	private String kommentar;
	
	@Column(name = "Geprueft")
	@Basic
	@Convert("booleanConverter")
	private Boolean geprueft;

	@ManyToOne
	@JoinColumn(name = "Aufbereitungsverfahren", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAAufbereitungsverfahren aufbereitungsverfahren;
	
	@ManyToOne
	@JoinColumn(name = "NG_Einheit", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAEinheiten ngEinheit;
	
	@ManyToOne
	@JoinColumn(name = "Referenz", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPALiteratur literatur;
	
	@ManyToOne
	@JoinColumn(name = "Nachweisverfahren", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPANachweisverfahren nachweisverfahren;
	
	@OneToMany(mappedBy = "aufbereitungsNachweisverfahren")
	private Collection<JPAVersuchsbedingungen> versuchsbedingungen;

	
	public JPAAufbereitungsNachweisverfahren() {
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getNachweisgrenze() {
        return nachweisgrenze;
    }

    public void setNachweisgrenze(Double nachweisgrenze) {
        this.nachweisgrenze = nachweisgrenze;
    }


    public Double getSensitivitaet() {
        return sensitivitaet;
    }

    public void setSensitivitaet(Double sensitivitaet) {
        this.sensitivitaet = sensitivitaet;
    }


    public Double getSpezifitaet() {
        return spezifitaet;
    }

    public void setSpezifitaet(Double spezifitaet) {
        this.spezifitaet = spezifitaet;
    }


    public Double getEffizienz() {
        return effizienz;
    }

    public void setEffizienz(Double effizienz) {
        this.effizienz = effizienz;
    }


    public Double getWiederfindungsrate() {
        return wiederfindungsrate;
    }

    public void setWiederfindungsrate(Double wiederfindungsrate) {
        this.wiederfindungsrate = wiederfindungsrate;
    }

    public Integer getGuetescoreSubj() {
        return guetescoreSubj;
    }

    public void setGuetescoreSubj(Integer guetescoreSubj) {
        this.guetescoreSubj = guetescoreSubj;
    }


    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }


    public Boolean isGeprueft() {
        return geprueft;
    }

    public void setGeprueft(Boolean geprueft) {
        this.geprueft = geprueft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JPAAufbereitungsNachweisverfahren that = (JPAAufbereitungsNachweisverfahren) o;

        if (aufbereitungsverfahren != that.aufbereitungsverfahren) return false;
        if (Double.compare(that.effizienz, effizienz) != 0) return false;
        if (geprueft != that.geprueft) return false;
        if (guetescoreSubj != that.guetescoreSubj) return false;
        if (id != that.id) return false;
        if (Double.compare(that.nachweisgrenze, nachweisgrenze) != 0) return false;
        if (nachweisverfahren != that.nachweisverfahren) return false;
        if (ngEinheit != that.ngEinheit) return false;
        if (Double.compare(that.sensitivitaet, sensitivitaet) != 0) return false;
        if (Double.compare(that.spezifitaet, spezifitaet) != 0) return false;
        if (Double.compare(that.wiederfindungsrate, wiederfindungsrate) != 0) return false;
        if (kommentar != null ? !kommentar.equals(that.kommentar) : that.kommentar != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        Integer result;
        long temp;
        result = id;
        temp = nachweisgrenze != +0.0d ? Double.doubleToLongBits(nachweisgrenze) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = sensitivitaet != +0.0d ? Double.doubleToLongBits(sensitivitaet) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = spezifitaet != +0.0d ? Double.doubleToLongBits(spezifitaet) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = effizienz != +0.0d ? Double.doubleToLongBits(effizienz) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = wiederfindungsrate != +0.0d ? Double.doubleToLongBits(wiederfindungsrate) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + guetescoreSubj;
        result = 31 * result + (kommentar != null ? kommentar.hashCode() : 0);
        result = 31 * result + (geprueft ? 1 : 0);
        return result;
    }
	
    public JPAAufbereitungsverfahren getAufbereitungsverfahren() {
		return aufbereitungsverfahren;
	}

	public void setAufbereitungsverfahren(
			JPAAufbereitungsverfahren aufbereitungsverfahren) {
		this.aufbereitungsverfahren = aufbereitungsverfahren;
	}

   public JPAEinheiten getNgEinheit() {
		return ngEinheit;
	}

	public void setNgEinheit(JPAEinheiten ngEinheit) {
		this.ngEinheit = ngEinheit;
	}

	public Boolean getGeprueft() {
		return geprueft;
	}

	public JPALiteratur getLiteratur() {
		return literatur;
	}

	public void setLiteratur(JPALiteratur literatur) {
		this.literatur = literatur;
	}

    public Collection<JPAVersuchsbedingungen> getVersuchsbedingungen() {
		return versuchsbedingungen;
	}

	public void setVersuchsbedingungen(
			Collection<JPAVersuchsbedingungen> versuchsbedingungen) {
		this.versuchsbedingungen = versuchsbedingungen;
	}

	public JPANachweisverfahren getNachweisverfahren() {
		return nachweisverfahren;
	}

	public void setNachweisverfahren(JPANachweisverfahren nachweisverfahren) {
		this.nachweisverfahren = nachweisverfahren;
	}
    
}
