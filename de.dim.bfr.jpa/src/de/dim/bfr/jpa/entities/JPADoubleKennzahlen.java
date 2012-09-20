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
@Table(name = "DoubleKennzahlen")
public class JPADoubleKennzahlen {
    
	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
	@Column(name = "Wert")
	@Basic
	private Double wert;
	
	@Column(name = "Exponent")
	@Basic
	private Double times10PowerOf;
	
	@Column(name = "Wert_typ")
	@Basic
	private Integer wertTyp;
	
	@Column(name = "Wert_g")
	@Basic
	@Convert("booleanConverter")
	private Boolean wertG;

	@Column(name = "Wiederholungen")
	@Basic
	private Double wiederholungen;
	
	@Column(name = "Wiederholungen_g")
	@Basic
	@Convert("booleanConverter")
	private Boolean wiederholungenG;
	
	@Column(name = "Standardabweichung")
	@Basic
	private Double standardabweichung;
	
	@Column(name = "Standardabweichung_exp")
	@Basic
	private Double standardabweichungExp;
	
	@Column(name = "Standardabweichung_g")
	@Basic
	@Convert("booleanConverter")
	private Boolean standardabweichungG;
	
	@Column(name = "Minimum")
	@Basic
	private Double minimum;
	
	@Column(name = "Minimum_exp")
	@Basic
	private Double minimumExp;
	
	@Column(name = "Minimum_g")
	@Basic
	@Convert("booleanConverter")
	private Boolean minimumG;
	
	@Column(name = "Maximum")
	@Basic
	private Double maximum;
	
	@Column(name = "Maximum_exp")
	@Basic
	private Double maximumExp;
	
	@Column(name = "Maximum_g")
	@Basic
	@Convert("booleanConverter")
	private Boolean maximumG;
	
	@Column(name = "LCL95")
	@Basic
	private Double lcl95;
	
	@Column(name="LCL95_exp")
	@Basic
	private Double lcl95Exp;
	
	@Column(name = "LCL95_g")
	@Basic
	@Convert("booleanConverter")
	private Boolean lcl95G;
	
	@Column(name = "UCL95")
	@Basic
	private Double ucl95;
	
	@Column(name="UCL95_exp")
	@Basic
	private Double ucl95Exp;
	
	@Column(name = "UCL95_g")
	@Basic
	@Convert("booleanConverter")
	private Boolean ucl95G;
	
	@Column(name = "Verteilung")
	@Basic
	private String verteilung;

	@Column(name = "Verteilung_g")
	@Basic
	@Convert("booleanConverter")
	private Boolean verteilungG;
	@Column(name = "Funktion (Zeit)")
	@Basic
	private String funktionZeit;
	@Column(name = "Funktion (Zeit)_g")
	@Basic
	@Convert("booleanConverter")
	private Boolean funktionZeitG;
	@Column(name = "Funktion (x)")
	@Basic
	private String funktionX;
	@Column(name = "x")
	@Basic
	private String x;
	
	@Column(name = "Funktion (x)_g")
	@Basic
	@Convert("booleanConverter")
	private Boolean funktionXG;
	
	@Column(name = "Undefiniert (n.d.)")
	@Basic
	@Convert("booleanConverter")
	private Boolean undefiniertND;

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


    public Double getTimes10PowerOf() {
        return times10PowerOf;
    }

    public void setTimes10PowerOf(Double times10PowerOf) {
        this.times10PowerOf = times10PowerOf;
    }


    public Integer getWertTyp() {
        return wertTyp;
    }

    public void setWertTyp(Integer wertTyp) {
        this.wertTyp = wertTyp;
    }


    public Boolean isWertG() {
        return wertG;
    }

    public void setWertG(Boolean wertG) {
        this.wertG = wertG;
    }


    public Double getWiederholungen() {
        return wiederholungen;
    }

    public void setWiederholungen(Double wiederholungen) {
        this.wiederholungen = wiederholungen;
    }


    public Boolean isWiederholungenG() {
        return wiederholungenG;
    }

    public void setWiederholungenG(Boolean wiederholungenG) {
        this.wiederholungenG = wiederholungenG;
    }


    public Double getStandardabweichung() {
        return standardabweichung;
    }

    public void setStandardabweichung(Double standardabweichung) {
        this.standardabweichung = standardabweichung;
    }


    public Boolean isStandardabweichungG() {
        return standardabweichungG;
    }

    public void setStandardabweichungG(Boolean standardabweichungG) {
        this.standardabweichungG = standardabweichungG;
    }


    public Double getMinimum() {
        return minimum;
    }

    public void setMinimum(Double minimum) {
        this.minimum = minimum;
    }


    public Boolean isMinimumG() {
        return minimumG;
    }

    public void setMinimumG(Boolean minimumG) {
        this.minimumG = minimumG;
    }


    public Double getMaximum() {
        return maximum;
    }

    public void setMaximum(Double maximum) {
        this.maximum = maximum;
    }


    public Boolean isMaximumG() {
        return maximumG;
    }

    public void setMaximumG(Boolean maximumG) {
        this.maximumG = maximumG;
    }


    public Double getLcl95() {
        return lcl95;
    }

    public void setLcl95(Double lcl95) {
        this.lcl95 = lcl95;
    }


    public Boolean isLcl95G() {
        return lcl95G;
    }

    public void setLcl95G(Boolean lcl95G) {
        this.lcl95G = lcl95G;
    }


    public Double getUcl95() {
        return ucl95;
    }

    public void setUcl95(Double ucl95) {
        this.ucl95 = ucl95;
    }


    public Boolean isUcl95G() {
        return ucl95G;
    }

    public void setUcl95G(Boolean ucl95G) {
        this.ucl95G = ucl95G;
    }


    public String getVerteilung() {
        return verteilung;
    }

    public void setVerteilung(String verteilung) {
        this.verteilung = verteilung;
    }


    public Boolean isVerteilungG() {
        return verteilungG;
    }

    public void setVerteilungG(Boolean verteilungG) {
        this.verteilungG = verteilungG;
    }


    public String getFunktionZeit() {
        return funktionZeit;
    }

    public void setFunktionZeit(String funktionZeit) {
        this.funktionZeit = funktionZeit;
    }


    public Boolean isFunktionZeitG() {
        return funktionZeitG;
    }

    public void setFunktionZeitG(Boolean funktionZeitG) {
        this.funktionZeitG = funktionZeitG;
    }


    public String getFunktionX() {
        return funktionX;
    }

    public void setFunktionX(String funktionX) {
        this.funktionX = funktionX;
    }


    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }


    public Boolean isFunktionXG() {
        return funktionXG;
    }

    public void setFunktionXG(Boolean funktionXG) {
        this.funktionXG = funktionXG;
    }


    public Boolean isUndefiniertND() {
        return undefiniertND;
    }

    public void setUndefiniertND(Boolean undefiniertND) {
        this.undefiniertND = undefiniertND;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JPADoubleKennzahlen that = (JPADoubleKennzahlen) o;

        if (funktionXG != that.funktionXG) return false;
        if (funktionZeitG != that.funktionZeitG) return false;
        if (id != that.id) return false;
        if (Double.compare(that.lcl95, lcl95) != 0) return false;
        if (lcl95G != that.lcl95G) return false;
        if (Double.compare(that.maximum, maximum) != 0) return false;
        if (maximumG != that.maximumG) return false;
        if (Double.compare(that.minimum, minimum) != 0) return false;
        if (minimumG != that.minimumG) return false;
        if (Double.compare(that.standardabweichung, standardabweichung) != 0) return false;
        if (standardabweichungG != that.standardabweichungG) return false;
        if (Double.compare(that.times10PowerOf, times10PowerOf) != 0) return false;
        if (Double.compare(that.ucl95, ucl95) != 0) return false;
        if (ucl95G != that.ucl95G) return false;
        if (undefiniertND != that.undefiniertND) return false;
        if (verteilungG != that.verteilungG) return false;
        if (Double.compare(that.wert, wert) != 0) return false;
        if (wertG != that.wertG) return false;
        if (Double.compare(that.wiederholungen, wiederholungen) != 0) return false;
        if (wiederholungenG != that.wiederholungenG) return false;
        if (funktionX != null ? !funktionX.equals(that.funktionX) : that.funktionX != null) return false;
        if (funktionZeit != null ? !funktionZeit.equals(that.funktionZeit) : that.funktionZeit != null) return false;
        if (verteilung != null ? !verteilung.equals(that.verteilung) : that.verteilung != null) return false;
        if (wertTyp != 0 ? wertTyp != that.wertTyp : that.wertTyp != 0) return false;
        if (x != null ? !x.equals(that.x) : that.x != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        temp = wert != +0.0d ? Double.doubleToLongBits(wert) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = times10PowerOf != +0.0d ? Double.doubleToLongBits(times10PowerOf) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (wertTyp ^(temp >>> 32));
        result = 31 * result + (wertG ? 1 : 0);
        temp = wiederholungen != +0.0d ? Double.doubleToLongBits(wiederholungen) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (wiederholungenG ? 1 : 0);
        temp = standardabweichung != +0.0d ? Double.doubleToLongBits(standardabweichung) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (standardabweichungG ? 1 : 0);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = minimum != +0.0d ? Double.doubleToLongBits(minimum) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (minimumG ? 1 : 0);
        temp = maximum != +0.0d ? Double.doubleToLongBits(maximum) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (maximumG ? 1 : 0);
        temp = lcl95 != +0.0d ? Double.doubleToLongBits(lcl95) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (lcl95G ? 1 : 0);
        temp = ucl95 != +0.0d ? Double.doubleToLongBits(ucl95) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (ucl95G ? 1 : 0);
        result = 31 * result + (verteilung != null ? verteilung.hashCode() : 0);
        result = 31 * result + (verteilungG ? 1 : 0);
        result = 31 * result + (funktionZeit != null ? funktionZeit.hashCode() : 0);
        result = 31 * result + (funktionZeitG ? 1 : 0);
        result = 31 * result + (funktionX != null ? funktionX.hashCode() : 0);
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (funktionXG ? 1 : 0);
        result = 31 * result + (undefiniertND ? 1 : 0);
        return result;
    }

	public void setStandardabweichungExp(Double standardabweichungExp) {
		this.standardabweichungExp = standardabweichungExp;
	}

	public Double getStandardabweichungExp() {
		return standardabweichungExp;
	}

	public void setMinimumExp(Double minimumExp) {
		this.minimumExp = minimumExp;
	}

	public Double getMinimumExp() {
		return minimumExp;
	}

	public void setMaximumExp(Double maximumExp) {
		this.maximumExp = maximumExp;
	}

	public Double getMaximumExp() {
		return maximumExp;
	}

	public void setLcl95Exp(Double lcl95Exp) {
		this.lcl95Exp = lcl95Exp;
	}

	public Double getLcl95Exp() {
		return lcl95Exp;
	}

	public void setUcl95Exp(Double ucl95Exp) {
		this.ucl95Exp = ucl95Exp;
	}

	public Double getUcl95Exp() {
		return ucl95Exp;
	}

    

    
    
//    public Collection<JPAMatrices> getMatricesesByDichteId() {
//        return matricesesByDichteId;
//    }
//
//    public void setMatricesesByDichteId(Collection<JPAMatrices> matricesesByDichteId) {
//        this.matricesesByDichteId = matricesesByDichteId;
//    }
//
//    public Collection<JPAMatrices> getMatricesesByAwId() {
//        return matricesesByAwId;
//    }
//
//    public void setMatricesesByAwId(Collection<JPAMatrices> matricesesByAwId) {
//        this.matricesesByAwId = matricesesByAwId;
//    }
//
//    public Collection<JPAMatrices> getMatricesesByPHId() {
//        return matricesesByPHId;
//    }
//
//    public void setMatricesesByPHId(Collection<JPAMatrices> matricesesByPHId) {
//        this.matricesesByPHId = matricesesByPHId;
//    }
//
//    public Collection<JPAMesswerte> getMesswertesByCO2Id() {
//        return messwertesByCO2Id;
//    }
//
//    public void setMesswertesByCO2Id(Collection<JPAMesswerte> messwertesByCO2Id) {
//        this.messwertesByCO2Id = messwertesByCO2Id;
//    }
//
//    public Collection<JPAMesswerte> getMesswertesByDruckId() {
//        return messwertesByDruckId;
//    }
//
//    public void setMesswertesByDruckId(Collection<JPAMesswerte> messwertesByDruckId) {
//        this.messwertesByDruckId = messwertesByDruckId;
//    }
//
//
//    
//    public Collection<JPAMesswerte> getMesswertesByKonzentrationId() {
//        return messwertesByKonzentrationId;
//    }
//
//    public void setMesswertesByKonzentrationId(Collection<JPAMesswerte> messwertesByKonzentrationId) {
//        this.messwertesByKonzentrationId = messwertesByKonzentrationId;
//    }
//
//    public Collection<JPAMesswerte> getMesswertesByTempId() {
//        return messwertesByTempId;
//    }
//
//    public void setMesswertesByTempId(Collection<JPAMesswerte> messwertesByTempId) {
//        this.messwertesByTempId = messwertesByTempId;
//    }
//
//    public Collection<JPAMesswerte> getMesswertesByZeitId() {
//        return messwertesByZeitId;
//    }
//
//    public void setMesswertesByZeitId(Collection<JPAMesswerte> messwertesByZeitId) {
//        this.messwertesByZeitId = messwertesByZeitId;
//    }
//
//    public Collection<JPAMesswerte> getMesswertesByAwId() {
//        return messwertesByAwId;
//    }
//
//    public void setMesswertesByAwId(Collection<JPAMesswerte> messwertesByAwId) {
//        this.messwertesByAwId = messwertesByAwId;
//    }
//
//    public Collection<JPAMesswerte> getMesswertesByPHId() {
//        return messwertesByPHId;
//    }
//
//    public void setMesswertesByPHId(Collection<JPAMesswerte> messwertesByPHId) {
//        this.messwertesByPHId = messwertesByPHId;
//    }
//
//    public Collection<JPAMesswerteSonstiges> getMesswerteSonstigesesByWertId() {
//        return messwerteSonstigesesByWertId;
//    }
//
//    public void setMesswerteSonstigesesByWertId(Collection<JPAMesswerteSonstiges> messwerteSonstigesesByWertId) {
//        this.messwerteSonstigesesByWertId = messwerteSonstigesesByWertId;
//    }
//
//    public Collection<JPAVersuchsbedingungen> getVersuchsbedingungensByCO2Id() {
//        return versuchsbedingungensByCO2Id;
//    }
//
//    public void setVersuchsbedingungensByCO2Id(Collection<JPAVersuchsbedingungen> versuchsbedingungensByCO2Id) {
//        this.versuchsbedingungensByCO2Id = versuchsbedingungensByCO2Id;
//    }
//
//    
//    
//    
//    public Collection<JPAVersuchsbedingungen> getVersuchsbedingungensByDruckId() {
//        return versuchsbedingungensByDruckId;
//    }
//
//    public void setVersuchsbedingungensByDruckId(Collection<JPAVersuchsbedingungen> versuchsbedingungensByDruckId) {
//        this.versuchsbedingungensByDruckId = versuchsbedingungensByDruckId;
//    }
//
//    public Collection<JPAVersuchsbedingungen> getVersuchsbedingungensByTempId() {
//        return versuchsbedingungensByTempId;
//    }
//
//    public void setVersuchsbedingungensByTempId(Collection<JPAVersuchsbedingungen> versuchsbedingungensByTempId) {
//        this.versuchsbedingungensByTempId = versuchsbedingungensByTempId;
//    }
//
//    public Collection<JPAVersuchsbedingungen> getVersuchsbedingungensByAwId() {
//        return versuchsbedingungensByAwId;
//    }
//
//    public void setVersuchsbedingungensByAwId(Collection<JPAVersuchsbedingungen> versuchsbedingungensByAwId) {
//        this.versuchsbedingungensByAwId = versuchsbedingungensByAwId;
//    }
//
//    public Collection<JPAVersuchsbedingungen> getVersuchsbedingungensByPHId() {
//        return versuchsbedingungensByPHId;
//    }
//
//    public void setVersuchsbedingungensByPHId(Collection<JPAVersuchsbedingungen> versuchsbedingungensByPHId) {
//        this.versuchsbedingungensByPHId = versuchsbedingungensByPHId;
//    }
//
//    public Collection<JPAVersuchsbedingungenSonstiges> getVersuchsbedingungenSonstigesesByWertId() {
//        return versuchsbedingungenSonstigesesByWertId;
//    }
//
//    public void setVersuchsbedingungenSonstigesesByWertId(Collection<JPAVersuchsbedingungenSonstiges> versuchsbedingungenSonstigesesByWertId) {
//        this.versuchsbedingungenSonstigesesByWertId = versuchsbedingungenSonstigesesByWertId;
//    }
}
