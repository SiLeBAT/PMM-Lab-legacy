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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.ObjectTypeConverter;

/**
 * Created by IntelliJ IDEA.
 * User: sebastian
 * Date: 21.11.11
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "Versuchsbedingungen")
public class JPAVersuchsbedingungen implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ObjectTypeConverter(name="booleanConverter", objectType = java.lang.Boolean.class, dataType = java.lang.Integer.class,
			conversionValues = {
				@ConversionValue(objectValue = "TRUE", dataValue = "1"),
				@ConversionValue(objectValue = "FALSE", dataValue = "0")
		}
	)
	
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(name = "AgensDetail")
	@Basic
	private String agensDetail;

	@Column(name = "EAN")
	@Basic
	private String ean;
	
	@Column(name = "MatrixDetail")
	@Basic
	private String matrixDetail;
	
	@Column(name = "in_on")
	@Basic
	private String inOn;
	
	@Column(name = "Sonstiges")
	@Basic
	private Integer sonstiges;
	
	@Column(name = "FreigabeModus")
	@Basic
	private Integer freigabeModus;
	
	@Column(name = "ID_CB")
	@Basic
	private String idCb;
	
	@Column(name = "Organismus_CB")
	@Basic
	private String organismusCb;
	
	@Column(name = "environment_CB")
	@Basic
	private String environmentCb;
	
	@Column(name = "b_f_CB")
	@Basic
	private String bFCb;
	
	@Column(name = "b_f_details_CB")
	@Basic
	private String bFDetailsCb;
	
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
	
	@OneToMany(mappedBy = "versuchsbedingung")
	private Collection<JPAGeschaetzteModelle> geschaetzteModelle;
	
	@OneToMany(mappedBy = "versuchsbedingungen")
	private Collection<JPAMesswerte> messwerte;
	
	@ManyToOne
	@JoinColumn(name = "Agens", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAAgenzien agens;
	
	@ManyToOne
	@JoinColumn(name = "Nachweisverfahren", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAAufbereitungsNachweisverfahren aufbereitungsNachweisverfahren;
	
	@ManyToOne
	@JoinColumn(name = "CO2", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPADoubleKennzahlen co2;
	
	@ManyToOne
	@JoinColumn(name = "Druck", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPADoubleKennzahlen druck;
	
	@ManyToOne
	@JoinColumn(name = "Temperatur", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPADoubleKennzahlen temperatur;
	
	@ManyToOne
	@JoinColumn(name = "aw", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPADoubleKennzahlen aw;
	
	@ManyToOne
	@JoinColumn(name = "pH", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPADoubleKennzahlen pH;
	
	@ManyToOne
	@JoinColumn(name = "Referenz", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPALiteratur literatur;
	
	@ManyToOne
	@JoinColumn(name = "Matrix", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAMatrices matrices;
	
	@OneToMany(mappedBy = "versuchsbedingungen", cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
	private Collection<JPAVersuchsbedingungenSonstiges> versuchsbedingungenSonstiges;
	
	
	public JPAVersuchsbedingungen() {
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgensDetail() {
        return agensDetail;
    }

    public void setAgensDetail(String agensDetail) {
        this.agensDetail = agensDetail;
    }


    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }


    public String getMatrixDetail() {
        return matrixDetail;
    }

    public void setMatrixDetail(String matrixDetail) {
        this.matrixDetail = matrixDetail;
    }
    
    public String getInOn() {
        return inOn;
    }

    public void setInOn(String inOn) {
        this.inOn = inOn;
    }


    public Integer getSonstiges() {
        return sonstiges;
    }

    public void setSonstiges(Integer sonstiges) {
        this.sonstiges = sonstiges;
    }


    public Integer getFreigabeModus() {
        return freigabeModus;
    }

    public void setFreigabeModus(Integer freigabeModus) {
        this.freigabeModus = freigabeModus;
    }


    public String getIdCb() {
        return idCb;
    }

    public void setIdCb(String idCb) {
        this.idCb = idCb;
    }


    public String getOrganismusCb() {
        return organismusCb;
    }

    public void setOrganismusCb(String organismusCb) {
        this.organismusCb = organismusCb;
    }


    public String getEnvironmentCb() {
        return environmentCb;
    }

    public void setEnvironmentCb(String environmentCb) {
        this.environmentCb = environmentCb;
    }


    public String getbFCb() {
        return bFCb;
    }

    public void setbFCb(String bFCb) {
        this.bFCb = bFCb;
    }


    public String getbFDetailsCb() {
        return bFDetailsCb;
    }

    public void setbFDetailsCb(String bFDetailsCb) {
        this.bFDetailsCb = bFDetailsCb;
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

    public Collection<JPAGeschaetzteModelle> getGeschaetzteModelle() {
		return geschaetzteModelle;
	}

	public void setGeschaetzteModelle(
			Collection<JPAGeschaetzteModelle> geschaetzteModelle) {
		this.geschaetzteModelle = geschaetzteModelle;
	}


    public Collection<JPAMesswerte> getMesswerte() {
		return messwerte;
	}

	public void setMesswerte(Collection<JPAMesswerte> messwerte) {
		this.messwerte = messwerte;
	}

	public JPAAgenzien getAgens() {
		return agens;
	}

	public void setAgens(JPAAgenzien agens) {
		this.agens = agens;
	}

	public JPAAufbereitungsNachweisverfahren getAufbereitungsNachweisverfahren() {
		return aufbereitungsNachweisverfahren;
	}

	public void setAufbereitungsNachweisverfahren(
			JPAAufbereitungsNachweisverfahren aufbereitungsNachweisverfahren) {
		this.aufbereitungsNachweisverfahren = aufbereitungsNachweisverfahren;
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

	public Collection<JPAVersuchsbedingungenSonstiges> getVersuchsbedingungenSonstiges() {
		return versuchsbedingungenSonstiges;
	}

	public void setVersuchsbedingungenSonstiges(
			Collection<JPAVersuchsbedingungenSonstiges> versuchsbedingungenSonstiges) {
		this.versuchsbedingungenSonstiges = versuchsbedingungenSonstiges;
	}
	
	public JPADoubleKennzahlen getCo2() {
		return co2;
	}

	public void setCo2(JPADoubleKennzahlen co2) {
		this.co2 = co2;
	}

	public JPADoubleKennzahlen getDruck() {
		return druck;
	}

	public void setDruck(JPADoubleKennzahlen druck) {
		this.druck = druck;
	}

	public JPADoubleKennzahlen getTemperatur() {
		return temperatur;
	}

	public void setTemperatur(JPADoubleKennzahlen temperatur) {
		this.temperatur = temperatur;
	}

	public JPADoubleKennzahlen getAw() {
		return aw;
	}

	public void setAw(JPADoubleKennzahlen aw) {
		this.aw = aw;
	}

	public JPADoubleKennzahlen getpH() {
		return pH;
	}

	public void setpH(JPADoubleKennzahlen pH) {
		this.pH = pH;
	}

	public Boolean getGeprueft() {
		return geprueft;
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
		result = prime * result + ((aw == null) ? 0 : aw.hashCode());
		result = prime * result + ((bFCb == null) ? 0 : bFCb.hashCode());
		result = prime * result
				+ ((bFDetailsCb == null) ? 0 : bFDetailsCb.hashCode());
		result = prime * result + ((co2 == null) ? 0 : co2.hashCode());
		result = prime * result + ((druck == null) ? 0 : druck.hashCode());
		result = prime * result + ((ean == null) ? 0 : ean.hashCode());
		result = prime * result
				+ ((environmentCb == null) ? 0 : environmentCb.hashCode());
		result = prime * result
				+ ((freigabeModus == null) ? 0 : freigabeModus.hashCode());
		result = prime * result
				+ ((geprueft == null) ? 0 : geprueft.hashCode());
		result = prime
				* result
				+ ((geschaetzteModelle == null) ? 0 : geschaetzteModelle
						.hashCode());
		result = prime * result
				+ ((guetescoreSubj == null) ? 0 : guetescoreSubj.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((idCb == null) ? 0 : idCb.hashCode());
		result = prime * result + ((inOn == null) ? 0 : inOn.hashCode());
		result = prime * result
				+ ((kommentar == null) ? 0 : kommentar.hashCode());
		result = prime * result
				+ ((literatur == null) ? 0 : literatur.hashCode());
		result = prime * result
				+ ((matrices == null) ? 0 : matrices.hashCode());
		result = prime * result
				+ ((matrixDetail == null) ? 0 : matrixDetail.hashCode());
		result = prime * result
				+ ((messwerte == null) ? 0 : messwerte.hashCode());
		result = prime * result
				+ ((organismusCb == null) ? 0 : organismusCb.hashCode());
		result = prime * result + ((pH == null) ? 0 : pH.hashCode());
		result = prime * result
				+ ((sonstiges == null) ? 0 : sonstiges.hashCode());
		result = prime * result
				+ ((temperatur == null) ? 0 : temperatur.hashCode());
		result = prime
				* result
				+ ((versuchsbedingungenSonstiges == null) ? 0
						: versuchsbedingungenSonstiges.hashCode());
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
		JPAVersuchsbedingungen other = (JPAVersuchsbedingungen) obj;
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
		if (aw == null) {
			if (other.aw != null)
				return false;
		} else if (!aw.equals(other.aw))
			return false;
		if (bFCb == null) {
			if (other.bFCb != null)
				return false;
		} else if (!bFCb.equals(other.bFCb))
			return false;
		if (bFDetailsCb == null) {
			if (other.bFDetailsCb != null)
				return false;
		} else if (!bFDetailsCb.equals(other.bFDetailsCb))
			return false;
		if (co2 == null) {
			if (other.co2 != null)
				return false;
		} else if (!co2.equals(other.co2))
			return false;
		if (druck == null) {
			if (other.druck != null)
				return false;
		} else if (!druck.equals(other.druck))
			return false;
		if (ean == null) {
			if (other.ean != null)
				return false;
		} else if (!ean.equals(other.ean))
			return false;
		if (environmentCb == null) {
			if (other.environmentCb != null)
				return false;
		} else if (!environmentCb.equals(other.environmentCb))
			return false;
		if (freigabeModus == null) {
			if (other.freigabeModus != null)
				return false;
		} else if (!freigabeModus.equals(other.freigabeModus))
			return false;
		if (geprueft == null) {
			if (other.geprueft != null)
				return false;
		} else if (!geprueft.equals(other.geprueft))
			return false;
		if (geschaetzteModelle == null) {
			if (other.geschaetzteModelle != null)
				return false;
		} else if (!geschaetzteModelle.equals(other.geschaetzteModelle))
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
		if (idCb == null) {
			if (other.idCb != null)
				return false;
		} else if (!idCb.equals(other.idCb))
			return false;
		if (inOn == null) {
			if (other.inOn != null)
				return false;
		} else if (!inOn.equals(other.inOn))
			return false;
		if (kommentar == null) {
			if (other.kommentar != null)
				return false;
		} else if (!kommentar.equals(other.kommentar))
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
		if (messwerte == null) {
			if (other.messwerte != null)
				return false;
		} else if (!messwerte.equals(other.messwerte))
			return false;
		if (organismusCb == null) {
			if (other.organismusCb != null)
				return false;
		} else if (!organismusCb.equals(other.organismusCb))
			return false;
		if (pH == null) {
			if (other.pH != null)
				return false;
		} else if (!pH.equals(other.pH))
			return false;
		if (sonstiges == null) {
			if (other.sonstiges != null)
				return false;
		} else if (!sonstiges.equals(other.sonstiges))
			return false;
		if (temperatur == null) {
			if (other.temperatur != null)
				return false;
		} else if (!temperatur.equals(other.temperatur))
			return false;
		if (versuchsbedingungenSonstiges == null) {
			if (other.versuchsbedingungenSonstiges != null)
				return false;
		} else if (!versuchsbedingungenSonstiges
				.equals(other.versuchsbedingungenSonstiges))
			return false;
		return true;
	}
	
}
