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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: sebastian
 * Date: 21.11.11
 * Time: 17:29
 * To change this template use File | Settings | File Templates.
 */
@Entity(name = "JPALiteratur")
@Table(name = "Literatur")
@NamedQuery(name = "getAllJPALiteraturs", query = "select l from JPALiteratur l")
public class JPALiteratur implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
   
	@Column(name = "Erstautor")
	@Basic
	private String erstautor;
	
	@Column(name = "Jahr")
	@Basic
	private Integer jahr;
	
	@Column(name = "Titel")
	@Basic
	private String titel;
	
	@Column(name = "Abstract")
	@Basic
	private String literaturAbstract;
	
	@Column(name = "Journal")
	@Basic
	private String journal;
	
	@Column(name = "Volume")
	@Basic
	private String volume;
	
	@Column(name = "Issue")
	@Basic
	private String issue;
	
	@Column(name = "Seite")
	@Basic
	private Integer seite;
	
	@Column(name = "FreigabeModus")
	@Basic
	private Integer freigabeModus;
	
	@Column(name = "Webseite")
	@Basic
	private String webseite;
	
	@Column(name = "Literaturtyp")
	@Basic
	private Integer literaturTyp;
	
	@Column(name = "Paper")
	@Basic
	private String paper;
	
	@Column(name = "Kommentar")
	@Basic
	private String kommentar;
	
	@OneToMany(mappedBy = "literatur")
	private Collection<JPAAufbereitungsNachweisverfahren> aufbereitungsNachweisverfahrens;
	
	@OneToMany(mappedBy = "literatur")
	private Collection<JPAAufbereitungsverfahren> aufbereitungsverfahrens;
	
//	@ManyToMany
//	@JoinTable(name = "GeschaetztesModell_Referenz",
//			joinColumns = @javax.persistence.JoinColumn(name = "Literatur"),
//			inverseJoinColumns = @javax.persistence.JoinColumn(name = "GeschaetztesModell"))
	
	@ManyToMany(mappedBy = "literatur")
	private Collection<JPAGeschaetzteModelle> geschaetzteModelle;
	
//	@ManyToMany
//	@JoinTable(name = "Modell_Referenz",
//			joinColumns = @javax.persistence.JoinColumn(name = "Literatur"),
//			inverseJoinColumns = @javax.persistence.JoinColumn(name = "Modell"))
	
	@ManyToMany(mappedBy = "literatur")
	private Collection<JPAModell> modelle;
	
	@OneToMany(mappedBy = "literatur")
	private Collection<JPANachweisverfahren> nachweisverfahrens;
	
	@OneToMany(mappedBy = "literatur")
	private Collection<JPAVersuchsbedingungen> versuchsbedingungen;
	
	public JPALiteratur() {
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getErstautor() {
        return erstautor;
    }

    public void setErstautor(String erstautor) {
        this.erstautor = erstautor;
    }


    public Integer getJahr() {
        return jahr;
    }

    public void setJahr(Integer jahr) {
        this.jahr = jahr;
    }


    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }


    public String getLiteraturAbstract() {
        return literaturAbstract;
    }

	public void setLiteraturAbstract(String literaturAbstract) {
		this.literaturAbstract = literaturAbstract;
	}


	public String getJournal() {
       return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }


    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }


    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }


    public Integer getSeite() {
        return seite;
    }

    public void setSeite(Integer seite) {
        this.seite = seite;
    }


    public Integer getFreigabeModus() {
        return freigabeModus;
    }

    public void setFreigabeModus(Integer freigabeModus) {
        this.freigabeModus = freigabeModus;
    }


    public String getWebseite() {
        return webseite;
    }

    public void setWebseite(String webseite) {
        this.webseite = webseite;
    }


    public Integer getLiteraturTyp() {
        return literaturTyp;
    }

    public void setLiteraturTyp(Integer literaturtyp) {
        this.literaturTyp = literaturtyp;
    }


    public String getPaper() {
        return paper;
    }

    public void setPaper(String paper) {
        this.paper = paper;
    }


    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }

    public Collection<JPAAufbereitungsNachweisverfahren> getAufbereitungsNachweisverfahrens() {
		return aufbereitungsNachweisverfahrens;
	}

	public void setAufbereitungsNachweisverfahrens(
			Collection<JPAAufbereitungsNachweisverfahren> aufbereitungsNachweisverfahrens) {
		this.aufbereitungsNachweisverfahrens = aufbereitungsNachweisverfahrens;
	}

	public Collection<JPAAufbereitungsverfahren> getAufbereitungsverfahrens() {
		return aufbereitungsverfahrens;
	}

	public void setAufbereitungsverfahrens(
			Collection<JPAAufbereitungsverfahren> aufbereitungsverfahrens) {
		this.aufbereitungsverfahrens = aufbereitungsverfahrens;
	}

	public Collection<JPAGeschaetzteModelle> getGeschaetzteModelle() {
		return geschaetzteModelle;
	}

	public void setGeschaetzteModelle(
			Collection<JPAGeschaetzteModelle> geschaetzteModelle) {
		this.geschaetzteModelle = geschaetzteModelle;
	}

	public void addGeschModell(JPAGeschaetzteModelle geschModell) {
		if (!getGeschaetzteModelle().contains(geschModell)) {
			getGeschaetzteModelle().add(geschModell);
		}
		if (!geschModell.getLiteratur().contains(this)) {
			geschModell.getLiteratur().add(this);
		}
	}
	
	public void removeGeschModell(JPAGeschaetzteModelle geschModell) {
		if (getGeschaetzteModelle().contains(geschModell)) {
			getGeschaetzteModelle().remove(geschModell);
		}
		if (geschModell.getLiteratur().contains(this)) {
			geschModell.getLiteratur().remove(this);
		}
	}
	
	public Collection<JPAModell> getModelle() {
		return modelle;
	}

	public void setModelle(Collection<JPAModell> modelle) {
		this.modelle = modelle;
	}

	public void addModell(JPAModell modell) {
		if (!getModelle().contains(modell)) {
			getModelle().add(modell);
		}
		if (!modell.getLiteratur().contains(this)) {
			modell.getLiteratur().add(this);
		}
	}
	
	public void removeModell(JPAModell modell) {
		if (getModelle().contains(modell)) {
			getModelle().remove(modell);
		}
		if (modell.getLiteratur().contains(this)) {
			modell.getLiteratur().remove(this);
		}
	}
	
	public Collection<JPANachweisverfahren> getNachweisverfahrens() {
		return nachweisverfahrens;
	}

	public void setNachweisverfahrens(
			Collection<JPANachweisverfahren> nachweisverfahrens) {
		this.nachweisverfahrens = nachweisverfahrens;
	}

	public Collection<JPAVersuchsbedingungen> getVersuchsbedingungen() {
		return versuchsbedingungen;
	}

	public void setVersuchsbedingungen(
			Collection<JPAVersuchsbedingungen> versuchsbedingungen) {
		this.versuchsbedingungen = versuchsbedingungen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((aufbereitungsNachweisverfahrens == null) ? 0
						: aufbereitungsNachweisverfahrens.hashCode());
		result = prime
				* result
				+ ((aufbereitungsverfahrens == null) ? 0
						: aufbereitungsverfahrens.hashCode());
		result = prime * result
				+ ((erstautor == null) ? 0 : erstautor.hashCode());
		result = prime * result
				+ ((freigabeModus == null) ? 0 : freigabeModus.hashCode());
		result = prime
				* result
				+ ((geschaetzteModelle == null) ? 0 : geschaetzteModelle
						.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((issue == null) ? 0 : issue.hashCode());
		result = prime * result + ((jahr == null) ? 0 : jahr.hashCode());
		result = prime * result + ((journal == null) ? 0 : journal.hashCode());
		result = prime * result
				+ ((kommentar == null) ? 0 : kommentar.hashCode());
		result = prime
				* result
				+ ((literaturAbstract == null) ? 0 : literaturAbstract
						.hashCode());
		result = prime * result
				+ ((literaturTyp == null) ? 0 : literaturTyp.hashCode());
		result = prime * result + ((modelle == null) ? 0 : modelle.hashCode());
		result = prime
				* result
				+ ((nachweisverfahrens == null) ? 0 : nachweisverfahrens
						.hashCode());
		result = prime * result + ((paper == null) ? 0 : paper.hashCode());
		result = prime * result + ((seite == null) ? 0 : seite.hashCode());
		result = prime * result + ((titel == null) ? 0 : titel.hashCode());
		result = prime
				* result
				+ ((versuchsbedingungen == null) ? 0 : versuchsbedingungen
						.hashCode());
		result = prime * result + ((volume == null) ? 0 : volume.hashCode());
		result = prime * result
				+ ((webseite == null) ? 0 : webseite.hashCode());
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
		JPALiteratur other = (JPALiteratur) obj;
		if (aufbereitungsNachweisverfahrens == null) {
			if (other.aufbereitungsNachweisverfahrens != null)
				return false;
		} else if (!aufbereitungsNachweisverfahrens
				.equals(other.aufbereitungsNachweisverfahrens))
			return false;
		if (aufbereitungsverfahrens == null) {
			if (other.aufbereitungsverfahrens != null)
				return false;
		} else if (!aufbereitungsverfahrens
				.equals(other.aufbereitungsverfahrens))
			return false;
		if (erstautor == null) {
			if (other.erstautor != null)
				return false;
		} else if (!erstautor.equals(other.erstautor))
			return false;
		if (freigabeModus == null) {
			if (other.freigabeModus != null)
				return false;
		} else if (!freigabeModus.equals(other.freigabeModus))
			return false;
		if (geschaetzteModelle == null) {
			if (other.geschaetzteModelle != null)
				return false;
		} else if (!geschaetzteModelle.equals(other.geschaetzteModelle))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (issue == null) {
			if (other.issue != null)
				return false;
		} else if (!issue.equals(other.issue))
			return false;
		if (jahr == null) {
			if (other.jahr != null)
				return false;
		} else if (!jahr.equals(other.jahr))
			return false;
		if (journal == null) {
			if (other.journal != null)
				return false;
		} else if (!journal.equals(other.journal))
			return false;
		if (kommentar == null) {
			if (other.kommentar != null)
				return false;
		} else if (!kommentar.equals(other.kommentar))
			return false;
		if (literaturAbstract == null) {
			if (other.literaturAbstract != null)
				return false;
		} else if (!literaturAbstract.equals(other.literaturAbstract))
			return false;
		if (literaturTyp == null) {
			if (other.literaturTyp != null)
				return false;
		} else if (!literaturTyp.equals(other.literaturTyp))
			return false;
		if (modelle == null) {
			if (other.modelle != null)
				return false;
		} else if (!modelle.equals(other.modelle))
			return false;
		if (nachweisverfahrens == null) {
			if (other.nachweisverfahrens != null)
				return false;
		} else if (!nachweisverfahrens.equals(other.nachweisverfahrens))
			return false;
		if (paper == null) {
			if (other.paper != null)
				return false;
		} else if (!paper.equals(other.paper))
			return false;
		if (seite == null) {
			if (other.seite != null)
				return false;
		} else if (!seite.equals(other.seite))
			return false;
		if (titel == null) {
			if (other.titel != null)
				return false;
		} else if (!titel.equals(other.titel))
			return false;
		if (versuchsbedingungen == null) {
			if (other.versuchsbedingungen != null)
				return false;
		} else if (!versuchsbedingungen.equals(other.versuchsbedingungen))
			return false;
		if (volume == null) {
			if (other.volume != null)
				return false;
		} else if (!volume.equals(other.volume))
			return false;
		if (webseite == null) {
			if (other.webseite != null)
				return false;
		} else if (!webseite.equals(other.webseite))
			return false;
		return true;
	}
}
