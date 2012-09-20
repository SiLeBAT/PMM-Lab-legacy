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
@Table(name = "Messwerte")
public class JPAMesswerte implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID", nullable = false, insertable = true, updatable = true, length = 32, precision = 0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	@Column(name = "ZeitEinheit", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
	@Basic
	private String zeitEinheit;
	
	@Column(name = "Delta", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
	@Basic
	@Convert("booleanConverter")
	private Boolean delta;
	
	@Column(name = "Sonstiges", nullable = true, insertable = true, updatable = true, length = 32, precision = 0)
	@Basic
	private Integer sonstiges;
	
	@Column(name = "Kommentar", nullable = true, insertable = true, updatable = true, length = 1023, precision = 0)
	@Basic
	private String kommentar;
	
	@Column(name = "Geprueft", nullable = true, insertable = true, updatable = true, length = 0, precision = 0)
	@Basic
	@Convert("booleanConverter")
	private Boolean geprueft;

	@ManyToOne
	@JoinColumn(name = "CO2", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPADoubleKennzahlen co2;
	
	@ManyToOne
	@JoinColumn(name = "Druck", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPADoubleKennzahlen druck;
	
	@ManyToOne
	@JoinColumn(name = "Konzentration", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPADoubleKennzahlen konzentration;
	
	@ManyToOne
	@JoinColumn(name = "Temperatur", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPADoubleKennzahlen temperatur;
	
	@ManyToOne
	@JoinColumn(name = "Zeit", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPADoubleKennzahlen zeit;
	
	@ManyToOne
	@JoinColumn(name = "aw", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPADoubleKennzahlen aw;
	
	@ManyToOne
	@JoinColumn(name = "pH", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPADoubleKennzahlen pH;
	
	@ManyToOne
	@JoinColumn(name = "Konz_Einheit", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAEinheiten konzEinheit;
	
	@ManyToOne
	@JoinColumn(name = "Versuchsbedingungen", referencedColumnName = "ID", updatable= false , insertable = false)
	private JPAVersuchsbedingungen versuchsbedingungen;
	
	@OneToMany(mappedBy = "messwerte")
	private Collection<JPAMesswerteSonstiges> messwerteSonstigeses;
	
	public JPAMesswerte() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getZeitEinheit() {
		return zeitEinheit;
	}

	public void setZeitEinheit(String zeitEinheit) {
		this.zeitEinheit = zeitEinheit;
	}

	public Boolean getDelta() {
		return delta;
	}

	public void setDelta(Boolean delta) {
		this.delta = delta;
	}

	public Integer getSonstiges() {
		return sonstiges;
	}

	public void setSonstiges(Integer sonstiges) {
		this.sonstiges = sonstiges;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public Boolean getGeprueft() {
		return geprueft;
	}

	public void setGeprueft(Boolean geprueft) {
		this.geprueft = geprueft;
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

	public JPADoubleKennzahlen getKonzentration() {
		return konzentration;
	}

	public void setKonzentration(JPADoubleKennzahlen konzentration) {
		this.konzentration = konzentration;
	}

	public JPADoubleKennzahlen getTemperatur() {
		return temperatur;
	}

	public void setTemperatur(JPADoubleKennzahlen temperatur) {
		this.temperatur = temperatur;
	}

	public JPADoubleKennzahlen getZeit() {
		return zeit;
	}

	public void setZeit(JPADoubleKennzahlen zeit) {
		this.zeit = zeit;
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

	public JPAEinheiten getKonzEinheit() {
		return konzEinheit;
	}

	public void setKonzEinheit(JPAEinheiten konzEinheit) {
		this.konzEinheit = konzEinheit;
	}

	public JPAVersuchsbedingungen getVersuchsbedingungen() {
		return versuchsbedingungen;
	}

	public void setVersuchsbedingungen(JPAVersuchsbedingungen versuchsbedingungen) {
		this.versuchsbedingungen = versuchsbedingungen;
	}

	public Collection<JPAMesswerteSonstiges> getMesswerteSonstigeses() {
		return messwerteSonstigeses;
	}

	public void setMesswerteSonstigeses(
			Collection<JPAMesswerteSonstiges> messwerteSonstigeses) {
		this.messwerteSonstigeses = messwerteSonstigeses;
	}
}
