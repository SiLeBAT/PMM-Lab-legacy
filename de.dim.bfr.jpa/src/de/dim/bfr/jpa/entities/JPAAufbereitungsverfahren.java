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
@Table(name = "Aufbereitungsverfahren")
public class JPAAufbereitungsverfahren {

    @Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
   
    @Column(name = "Bezeichnung", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
	@Basic
	private String bezeichnung;
	
    @Column(name = "Kurzbezeichnung", nullable = true, insertable = true, updatable = true, length = 30, precision = 0)
	@Basic
	private String kurzbezeichnung;
	
    @Column(name = "WissenschaftlicheBezeichnung", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
	@Basic
	private String wissenschaftlicheBezeichnung;
	
    @Column(name = "Aufkonzentrierung", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
	@Basic
	@Convert("booleanConverter")
	private Boolean aufkonzentrierung;

    @Column(name = "DNA_Extraktion", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
	@Basic
	@Convert("booleanConverter")
	private Boolean dnaExtraktion;

    @Column(name = "RNA_Extraktion", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
	@Basic
	@Convert("booleanConverter")
	private Boolean rnaExtraktion;

    @Column(name = "Protein_Extraktion", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
	@Basic
	@Convert("booleanConverter")
	private Boolean proteinExtraktion;

    @Column(name = "Homogenisierung", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
	@Basic
	@Convert("booleanConverter")
	private Boolean homogenisierung;

    @Column(name = "Zelllyse", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
	@Basic
	@Convert("booleanConverter")
	private Boolean zelllyse;

    @Column(name = "Matrix", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
	@Basic
	private Integer matrix;

    @Column(name = "MatrixDetail", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
	@Basic
	private String matrixDetail;

    @Column(name = "Agens", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
	@Basic
	private Integer agens;

    @Column(name = "AgensDetail", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
	@Basic
	private String agensDetail;

    @Column(name = "Kits", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
	@Basic
	private Integer kits;

    @Column(name = "Dauer", nullable = true, insertable = true, updatable = true, length = 64, precision = 0)
	@Basic
	private Double dauer;

    @Column(name = "DauerEinheit", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
	@Basic
	private String dauerEinheit;

    @Column(name = "Personalressourcen", nullable = true, insertable = true, updatable = true, length = 64, precision = 0)
	@Basic
	private Double personalressourcen;

    @Column(name = "ZeitEinheit", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
	@Basic
	private String zeitEinheit;

    @Column(name = "Kosten", nullable = true, insertable = true, updatable = true, length = 64, precision = 0)
	@Basic
	private Double kosten;

    @Column(name = "KostenEinheit", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
	@Basic
	private String kostenEinheit;

    @Column(name = "Normen", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
	@Basic
	private Integer normen;

    @Column(name = "SOP_LA", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
	@Basic
	private String sopLa;

    @Column(name = "Spezialequipment", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
	@Basic
	@Convert("booleanConverter")
	private Boolean spezialequipment;
    
    @Column(name = "Laienpersonal", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
	@Basic
	@Convert("booleanConverter")
	private Boolean laienpersonal;

    @Column(name = "Guetescore", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
	@Basic
	private Integer guetescoreSubj;

    @Column(name = "Kommentar", nullable = true, insertable = true, updatable = true, length = 1023, precision = 0)
	@Basic
	private String kommentar;

    @Column(name = "Geprueft", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
	@Basic
	@Convert("booleanConverter")
	private Boolean geprueft;

	@OneToMany(mappedBy = "aufbereitungsverfahren")
	private Collection<JPAAufbereitungsNachweisverfahren> aufbereitungsNachweisverfahren;

	@ManyToOne
	@JoinColumn(name = "Agens", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAAgenzien agenzien;

	@ManyToOne
	@JoinColumn(name = "Referenz", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPALiteratur literatur;

	@ManyToOne
	@JoinColumn(name = "Matrix", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAMatrices matrices;

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
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


    public Boolean isAufkonzentrierung() {
        return aufkonzentrierung;
    }

    public void setAufkonzentrierung(Boolean aufkonzentrierung) {
        this.aufkonzentrierung = aufkonzentrierung;
    }


    public Boolean isDnaExtraktion() {
        return dnaExtraktion;
    }

    public void setDnaExtraktion(Boolean dnaExtraktion) {
        this.dnaExtraktion = dnaExtraktion;
    }


    public Boolean isRnaExtraktion() {
        return rnaExtraktion;
    }

    public void setRnaExtraktion(Boolean rnaExtraktion) {
        this.rnaExtraktion = rnaExtraktion;
    }


    public Boolean isProteinExtraktion() {
        return proteinExtraktion;
    }

    public void setProteinExtraktion(Boolean proteinExtraktion) {
        this.proteinExtraktion = proteinExtraktion;
    }


    public Boolean isHomogenisierung() {
        return homogenisierung;
    }

    public void setHomogenisierung(Boolean homogenisierung) {
        this.homogenisierung = homogenisierung;
    }


    public Boolean isZelllyse() {
        return zelllyse;
    }

    public void setZelllyse(Boolean zelllyse) {
        this.zelllyse = zelllyse;
    }


    public Integer getMatrix() {
        return matrix;
    }

    public void setMatrix(Integer matrix) {
        this.matrix = matrix;
    }


    public String getMatrixDetail() {
        return matrixDetail;
    }

    public void setMatrixDetail(String matrixDetail) {
        this.matrixDetail = matrixDetail;
    }


    public Integer getAgens() {
        return agens;
    }

    public void setAgens(Integer agens) {
        this.agens = agens;
    }


    public String getAgensDetail() {
        return agensDetail;
    }

    public void setAgensDetail(String agensDetail) {
        this.agensDetail = agensDetail;
    }


    public Integer getKits() {
        return kits;
    }

    public void setKits(Integer kits) {
        this.kits = kits;
    }


    public Double getDauer() {
        return dauer;
    }

    public void setDauer(Double dauer) {
        this.dauer = dauer;
    }


    public String getDauerEinheit() {
        return dauerEinheit;
    }

    public void setDauerEinheit(String dauerEinheit) {
        this.dauerEinheit = dauerEinheit;
    }


    public Double getPersonalressourcen() {
        return personalressourcen;
    }

    public void setPersonalressourcen(Double personalressourcen) {
        this.personalressourcen = personalressourcen;
    }


    public String getZeitEinheit() {
        return zeitEinheit;
    }

    public void setZeitEinheit(String zeitEinheit) {
        this.zeitEinheit = zeitEinheit;
    }


    public Double getKosten() {
        return kosten;
    }

    public void setKosten(Double kosten) {
        this.kosten = kosten;
    }


    public String getKostenEinheit() {
        return kostenEinheit;
    }

    public void setKostenEinheit(String kostenEinheit) {
        this.kostenEinheit = kostenEinheit;
    }


    public Integer getNormen() {
        return normen;
    }

    public void setNormen(Integer normen) {
        this.normen = normen;
    }


    public String getSopLa() {
        return sopLa;
    }

    public void setSopLa(String sopLa) {
        this.sopLa = sopLa;
    }


    public Boolean isSpezialequipment() {
        return spezialequipment;
    }

    public void setSpezialequipment(Boolean spezialequipment) {
        this.spezialequipment = spezialequipment;
    }


    public Boolean isLaienpersonal() {
        return laienpersonal;
    }

    public void setLaienpersonal(Boolean laienpersonal) {
        this.laienpersonal = laienpersonal;
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

        JPAAufbereitungsverfahren that = (JPAAufbereitungsverfahren) o;

        if (agens != that.agens) return false;
        if (aufkonzentrierung != that.aufkonzentrierung) return false;
        if (Double.compare(that.dauer, dauer) != 0) return false;
        if (dnaExtraktion != that.dnaExtraktion) return false;
        if (geprueft != that.geprueft) return false;
        if (guetescoreSubj != that.guetescoreSubj) return false;
        if (homogenisierung != that.homogenisierung) return false;
        if (id != that.id) return false;
        if (kits != that.kits) return false;
        if (Double.compare(that.kosten, kosten) != 0) return false;
        if (laienpersonal != that.laienpersonal) return false;
        if (matrix != that.matrix) return false;
        if (normen != that.normen) return false;
        if (Double.compare(that.personalressourcen, personalressourcen) != 0) return false;
        if (proteinExtraktion != that.proteinExtraktion) return false;
        if (rnaExtraktion != that.rnaExtraktion) return false;
        if (spezialequipment != that.spezialequipment) return false;
        if (zelllyse != that.zelllyse) return false;
        if (agensDetail != null ? !agensDetail.equals(that.agensDetail) : that.agensDetail != null) return false;
        if (bezeichnung != null ? !bezeichnung.equals(that.bezeichnung) : that.bezeichnung != null) return false;
        if (dauerEinheit != null ? !dauerEinheit.equals(that.dauerEinheit) : that.dauerEinheit != null) return false;
        if (kommentar != null ? !kommentar.equals(that.kommentar) : that.kommentar != null) return false;
        if (kostenEinheit != null ? !kostenEinheit.equals(that.kostenEinheit) : that.kostenEinheit != null)
            return false;
        if (kurzbezeichnung != null ? !kurzbezeichnung.equals(that.kurzbezeichnung) : that.kurzbezeichnung != null)
            return false;
        if (matrixDetail != null ? !matrixDetail.equals(that.matrixDetail) : that.matrixDetail != null) return false;
        if (sopLa != null ? !sopLa.equals(that.sopLa) : that.sopLa != null) return false;
        if (wissenschaftlicheBezeichnung != null ? !wissenschaftlicheBezeichnung.equals(that.wissenschaftlicheBezeichnung) : that.wissenschaftlicheBezeichnung != null)
            return false;
        if (zeitEinheit != null ? !zeitEinheit.equals(that.zeitEinheit) : that.zeitEinheit != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (bezeichnung != null ? bezeichnung.hashCode() : 0);
        result = 31 * result + (kurzbezeichnung != null ? kurzbezeichnung.hashCode() : 0);
        result = 31 * result + (wissenschaftlicheBezeichnung != null ? wissenschaftlicheBezeichnung.hashCode() : 0);
        result = 31 * result + (aufkonzentrierung ? 1 : 0);
        result = 31 * result + (dnaExtraktion ? 1 : 0);
        result = 31 * result + (rnaExtraktion ? 1 : 0);
        result = 31 * result + (proteinExtraktion ? 1 : 0);
        result = 31 * result + (homogenisierung ? 1 : 0);
        result = 31 * result + (zelllyse ? 1 : 0);
        result = 31 * result + matrix;
        result = 31 * result + (matrixDetail != null ? matrixDetail.hashCode() : 0);
        result = 31 * result + agens;
        result = 31 * result + (agensDetail != null ? agensDetail.hashCode() : 0);
        result = 31 * result + kits;
        temp = dauer != +0.0d ? Double.doubleToLongBits(dauer) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (dauerEinheit != null ? dauerEinheit.hashCode() : 0);
        temp = personalressourcen != +0.0d ? Double.doubleToLongBits(personalressourcen) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (zeitEinheit != null ? zeitEinheit.hashCode() : 0);
        temp = kosten != +0.0d ? Double.doubleToLongBits(kosten) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (kostenEinheit != null ? kostenEinheit.hashCode() : 0);
        result = 31 * result + normen;
        result = 31 * result + (sopLa != null ? sopLa.hashCode() : 0);
        result = 31 * result + (spezialequipment ? 1 : 0);
        result = 31 * result + (laienpersonal ? 1 : 0);
        result = 31 * result + guetescoreSubj;
        result = 31 * result + (kommentar != null ? kommentar.hashCode() : 0);
        result = 31 * result + (geprueft ? 1 : 0);
        return result;
    }


    public Collection<JPAAufbereitungsNachweisverfahren> getAufbereitungsNachweisverfahren() {
		return aufbereitungsNachweisverfahren;
	}

	public void setAufbereitungsNachweisverfahren(
			Collection<JPAAufbereitungsNachweisverfahren> aufbereitungsNachweisverfahren) {
		this.aufbereitungsNachweisverfahren = aufbereitungsNachweisverfahren;
	}


    public JPALiteratur getLiteratur() {
		return literatur;
	}

	public void setLiteratur(JPALiteratur literatur) {
		this.literatur = literatur;
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

	public Boolean getAufkonzentrierung() {
		return aufkonzentrierung;
	}

	public Boolean getDnaExtraktion() {
		return dnaExtraktion;
	}

	public Boolean getRnaExtraktion() {
		return rnaExtraktion;
	}

	public Boolean getProteinExtraktion() {
		return proteinExtraktion;
	}

	public Boolean getHomogenisierung() {
		return homogenisierung;
	}

	public Boolean getZelllyse() {
		return zelllyse;
	}

	public Boolean getSpezialequipment() {
		return spezialequipment;
	}

	public Boolean getLaienpersonal() {
		return laienpersonal;
	}

	public Boolean getGeprueft() {
		return geprueft;
	}

}
