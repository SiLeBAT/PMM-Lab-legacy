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
@Table(name = "Nachweisverfahren")
public class JPANachweisverfahren implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@Column(name = "Bezeichnung")
	@Basic
	private String bezeichnung;
	
	@Column(name = "Quantitativ")
	@Basic
	@Convert("booleanConverter")
	private Boolean quantitativ;
	
	@Column(name = "Identifizierung")
	@Basic
	@Convert("booleanConverter")
	private Boolean identifizierung;
	
	@Column(name = "Typisierung")
	@Basic
	@Convert("booleanConverter")
	private Boolean typisierung;
	
	@Column(name = "MatrixDetail")
	@Basic
	private String matrixDetail;
	
	@Column(name = "AgensDetail")
	@Basic
	private String agensDetail;
	
	@Column(name = "Kits")
	@Basic
	private Integer kits;
	
	@Column(name = "Dauer")
	@Basic
	private Double dauer;
	
	@Column(name = "DauerEinheit")
	@Basic
	private String dauerEinheit;
	
	@Column(name = "Personalressourcen")
	@Basic
	private Double personalressourcen;
	
	@Column(name = "ZeitEinheit")
	@Basic
	private String zeitEinheit;
	
	@Column(name = "Kosten")
	@Basic
	private Double kosten;
	
	@Column(name = "KostenEinheit")
	@Basic
	private String kostenEinheit;
	
	@Column(name = "Normen")
	@Basic
	private Integer normen;
	
	@Column(name = "SOP_LA")
	@Basic
	private String sopLa;
	
	@Column(name = "Spezialequipment")
	@Basic
	@Convert("booleanConverter")
	private Boolean spezialequipment;
	
	@Column(name = "Laienpersonal")
	@Basic
	@Convert("booleanConverter")
	private Boolean laienpersonal;
	
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
	
	@OneToMany(mappedBy = "nachweisverfahren")
	private Collection<JPAAufbereitungsNachweisverfahren> aufbereitungsNachweisverfahren;
	
	@ManyToOne
	@JoinColumn(name = "Agens", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAAgenzien agens;
	
	@ManyToOne
	@JoinColumn(name = "Referenz", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPALiteratur literatur;
	
	@ManyToOne
	@JoinColumn(name = "Matrix", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAMatrices matrices;
	
	@ManyToOne
	@JoinColumn(name = "Methoden", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAMethodiken methodiken;
	
	public JPANachweisverfahren() {
	}

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


    public Boolean isQuantitativ() {
        return quantitativ;
    }

    public void setQuantitativ(Boolean quantitativ) {
        this.quantitativ = quantitativ;
    }


    public Boolean isIdentifizierung() {
        return identifizierung;
    }

    public void setIdentifizierung(Boolean identifizierung) {
        this.identifizierung = identifizierung;
    }


    public Boolean isTypisierung() {
        return typisierung;
    }

    public void setTypisierung(Boolean typisierung) {
        this.typisierung = typisierung;
    }

    public String getMatrixDetail() {
        return matrixDetail;
    }

    public void setMatrixDetail(String matrixDetail) {
        this.matrixDetail = matrixDetail;
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

    public Collection<JPAAufbereitungsNachweisverfahren> getAufbereitungsNachweisverfahren() {
		return aufbereitungsNachweisverfahren;
	}

	public void setAufbereitungsNachweisverfahren(
			Collection<JPAAufbereitungsNachweisverfahren> aufbereitungsNachweisverfahren) {
		this.aufbereitungsNachweisverfahren = aufbereitungsNachweisverfahren;
	}


	public JPAAgenzien getAgens() {
		return agens;
	}

	public void setAgens(JPAAgenzien agens) {
		this.agens = agens;
	}

	public JPALiteratur getLiteratur() {
		return literatur;
	}

	public void setLiteratur(JPALiteratur literatur) {
		this.literatur = literatur;
	}

    public JPAMatrices getMatrices() {
		return matrices;
	}

	public void setMatrices(JPAMatrices matrices) {
		this.matrices = matrices;
	}

	public JPAMethodiken getMethodiken() {
		return methodiken;
	}

	public void setMethodiken(JPAMethodiken methodiken) {
		this.methodiken = methodiken;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agens == null) ? 0 : agens.hashCode());
		result = prime * result
				+ ((agensDetail == null) ? 0 : agensDetail.hashCode());
		result = prime
				* result
				+ ((aufbereitungsNachweisverfahren == null) ? 0
						: aufbereitungsNachweisverfahren.hashCode());
		result = prime * result
				+ ((bezeichnung == null) ? 0 : bezeichnung.hashCode());
		result = prime * result + ((dauer == null) ? 0 : dauer.hashCode());
		result = prime * result
				+ ((dauerEinheit == null) ? 0 : dauerEinheit.hashCode());
		result = prime * result
				+ ((geprueft == null) ? 0 : geprueft.hashCode());
		result = prime * result
				+ ((guetescoreSubj == null) ? 0 : guetescoreSubj.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((identifizierung == null) ? 0 : identifizierung.hashCode());
		result = prime * result + ((kits == null) ? 0 : kits.hashCode());
		result = prime * result
				+ ((kommentar == null) ? 0 : kommentar.hashCode());
		result = prime * result + ((kosten == null) ? 0 : kosten.hashCode());
		result = prime * result
				+ ((kostenEinheit == null) ? 0 : kostenEinheit.hashCode());
		result = prime * result
				+ ((laienpersonal == null) ? 0 : laienpersonal.hashCode());
		result = prime * result
				+ ((literatur == null) ? 0 : literatur.hashCode());
		result = prime * result
				+ ((matrices == null) ? 0 : matrices.hashCode());
		result = prime * result
				+ ((matrixDetail == null) ? 0 : matrixDetail.hashCode());
		result = prime * result
				+ ((methodiken == null) ? 0 : methodiken.hashCode());
		result = prime * result + ((normen == null) ? 0 : normen.hashCode());
		result = prime
				* result
				+ ((personalressourcen == null) ? 0 : personalressourcen
						.hashCode());
		result = prime * result
				+ ((quantitativ == null) ? 0 : quantitativ.hashCode());
		result = prime * result + ((sopLa == null) ? 0 : sopLa.hashCode());
		result = prime
				* result
				+ ((spezialequipment == null) ? 0 : spezialequipment.hashCode());
		result = prime * result
				+ ((typisierung == null) ? 0 : typisierung.hashCode());
		result = prime * result
				+ ((zeitEinheit == null) ? 0 : zeitEinheit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JPANachweisverfahren other = (JPANachweisverfahren) obj;
		if (agens == null) {
			if (other.agens != null)
				return false;
		} else if (!agens.equals(other.agens))
			return false;
		if (agensDetail == null) {
			if (other.agensDetail != null)
				return false;
		} else if (!agensDetail.equals(other.agensDetail))
			return false;
		if (aufbereitungsNachweisverfahren == null) {
			if (other.aufbereitungsNachweisverfahren != null)
				return false;
		} else if (!aufbereitungsNachweisverfahren
				.equals(other.aufbereitungsNachweisverfahren))
			return false;
		if (bezeichnung == null) {
			if (other.bezeichnung != null)
				return false;
		} else if (!bezeichnung.equals(other.bezeichnung))
			return false;
		if (dauer == null) {
			if (other.dauer != null)
				return false;
		} else if (!dauer.equals(other.dauer))
			return false;
		if (dauerEinheit == null) {
			if (other.dauerEinheit != null)
				return false;
		} else if (!dauerEinheit.equals(other.dauerEinheit))
			return false;
		if (geprueft == null) {
			if (other.geprueft != null)
				return false;
		} else if (!geprueft.equals(other.geprueft))
			return false;
		if (guetescoreSubj == null) {
			if (other.guetescoreSubj != null)
				return false;
		} else if (!guetescoreSubj.equals(other.guetescoreSubj))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (identifizierung == null) {
			if (other.identifizierung != null)
				return false;
		} else if (!identifizierung.equals(other.identifizierung))
			return false;
		if (kits == null) {
			if (other.kits != null)
				return false;
		} else if (!kits.equals(other.kits))
			return false;
		if (kommentar == null) {
			if (other.kommentar != null)
				return false;
		} else if (!kommentar.equals(other.kommentar))
			return false;
		if (kosten == null) {
			if (other.kosten != null)
				return false;
		} else if (!kosten.equals(other.kosten))
			return false;
		if (kostenEinheit == null) {
			if (other.kostenEinheit != null)
				return false;
		} else if (!kostenEinheit.equals(other.kostenEinheit))
			return false;
		if (laienpersonal == null) {
			if (other.laienpersonal != null)
				return false;
		} else if (!laienpersonal.equals(other.laienpersonal))
			return false;
		if (literatur == null) {
			if (other.literatur != null)
				return false;
		} else if (!literatur.equals(other.literatur))
			return false;
		if (matrices == null) {
			if (other.matrices != null)
				return false;
		} else if (!matrices.equals(other.matrices))
			return false;
		if (matrixDetail == null) {
			if (other.matrixDetail != null)
				return false;
		} else if (!matrixDetail.equals(other.matrixDetail))
			return false;
		if (methodiken == null) {
			if (other.methodiken != null)
				return false;
		} else if (!methodiken.equals(other.methodiken))
			return false;
		if (normen == null) {
			if (other.normen != null)
				return false;
		} else if (!normen.equals(other.normen))
			return false;
		if (personalressourcen == null) {
			if (other.personalressourcen != null)
				return false;
		} else if (!personalressourcen.equals(other.personalressourcen))
			return false;
		if (quantitativ == null) {
			if (other.quantitativ != null)
				return false;
		} else if (!quantitativ.equals(other.quantitativ))
			return false;
		if (sopLa == null) {
			if (other.sopLa != null)
				return false;
		} else if (!sopLa.equals(other.sopLa))
			return false;
		if (spezialequipment == null) {
			if (other.spezialequipment != null)
				return false;
		} else if (!spezialequipment.equals(other.spezialequipment))
			return false;
		if (typisierung == null) {
			if (other.typisierung != null)
				return false;
		} else if (!typisierung.equals(other.typisierung))
			return false;
		if (zeitEinheit == null) {
			if (other.zeitEinheit != null)
				return false;
		} else if (!zeitEinheit.equals(other.zeitEinheit))
			return false;
		return true;
	}

}
