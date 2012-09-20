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

/**
 * Created by IntelliJ IDEA. User: sebastian Date: 21.11.11 Time: 17:29 To
 * change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "Matrices")
public class JPAMatrices implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "Matrixname", nullable = true, insertable = true, updatable = true, length = 255, precision = 0)
    @Basic
    private String matrixname;

    @Column(name = "Leitsatznummer", nullable = true, insertable = true, updatable = true, length = 20, precision = 0)
    @Basic
    private String leitsatznummer;

    @Column(name = "Katalogcodes", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
    @Basic
    private Integer katalogcodes;

    @Column(name = "Kommentar", nullable = true, insertable = true, updatable = true, length = 1023, precision = 0)
    @Basic
    private String kommentar;

    @OneToMany(mappedBy = "matrices")
    private Collection<JPAAufbereitungsverfahren> aufbereitungsverfahrens;

    @OneToMany(mappedBy = "basis")
    private Collection<JPACodesMatrices> codesMatriceses;

    @OneToMany(mappedBy = "matrices")
    private Collection<JPAComBaseImport> comBaseImports;

    @ManyToOne
    @JoinColumn(name = "Dichte", referencedColumnName = "ID", updatable = false, insertable = false)
    private JPADoubleKennzahlen dichte;

    @ManyToOne
    @JoinColumn(name = "aw", referencedColumnName = "ID", updatable = false, insertable = false)
    private JPADoubleKennzahlen aw;

    @ManyToOne
    @JoinColumn(name = "pH", referencedColumnName = "ID", updatable = false, insertable = false)
    private JPADoubleKennzahlen pH;

    @OneToMany(mappedBy = "matrices")
    private Collection<JPANachweisverfahren> nachweisverfahren;

    @OneToMany(mappedBy = "matrices")
    private Collection<JPAVersuchsbedingungen> versuchsbedingungen;

    public JPAMatrices() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatrixname() {
        return matrixname;
    }

    public void setMatrixname(String matrixname) {
        this.matrixname = matrixname;
    }

    public String getLeitsatznummer() {
        return leitsatznummer;
    }

    public void setLeitsatznummer(String leitsatznummer) {
        this.leitsatznummer = leitsatznummer;
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
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;

        JPAMatrices that = (JPAMatrices) o;

        if(id != that.id)
            return false;
        if(katalogcodes != that.katalogcodes)
            return false;
        if(kommentar != null ? !kommentar.equals(that.kommentar)
                : that.kommentar != null)
            return false;
        if(leitsatznummer != null ? !leitsatznummer
                .equals(that.leitsatznummer) : that.leitsatznummer != null)
            return false;
        if(matrixname != null ? !matrixname.equals(that.matrixname)
                : that.matrixname != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        Integer result;
        long temp;
        result = id;
        result = 31 * result + (matrixname != null ? matrixname.hashCode() : 0);
        result = 31 * result
                + (leitsatznummer != null ? leitsatznummer.hashCode() : 0);
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

    public Collection<JPAVersuchsbedingungen> getVersuchsbedingungen() {
        return versuchsbedingungen;
    }

    public void setVersuchsbedingungen(
            Collection<JPAVersuchsbedingungen> versuchsbedingungen) {
        this.versuchsbedingungen = versuchsbedingungen;
    }

	public Collection<JPAAufbereitungsverfahren> getAufbereitungsverfahrens() {
		return aufbereitungsverfahrens;
	}

	public void setAufbereitungsverfahrens(
			Collection<JPAAufbereitungsverfahren> aufbereitungsverfahrens) {
		this.aufbereitungsverfahrens = aufbereitungsverfahrens;
	}

	public Collection<JPACodesMatrices> getCodesMatriceses() {
		return codesMatriceses;
	}

	public void setCodesMatriceses(Collection<JPACodesMatrices> codesMatriceses) {
		this.codesMatriceses = codesMatriceses;
	}

	public Collection<JPAComBaseImport> getComBaseImports() {
		return comBaseImports;
	}

	public void setComBaseImports(Collection<JPAComBaseImport> comBaseImports) {
		this.comBaseImports = comBaseImports;
	}

	public JPADoubleKennzahlen getDichte() {
		return dichte;
	}

	public void setDichte(JPADoubleKennzahlen dichte) {
		this.dichte = dichte;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
