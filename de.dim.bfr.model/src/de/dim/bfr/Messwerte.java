/**
 * $Id: Messwerte.java 651 2012-01-24 09:59:12Z sdoerl $
 ********************************************************************************
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
package de.dim.bfr;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Messwerte</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.dim.bfr.Messwerte#isGeprueft <em>Geprueft</em>}</li>
 *   <li>{@link de.dim.bfr.Messwerte#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.Messwerte#getKommentar <em>Kommentar</em>}</li>
 *   <li>{@link de.dim.bfr.Messwerte#getZeitEinheit <em>Zeit Einheit</em>}</li>
 *   <li>{@link de.dim.bfr.Messwerte#getAw <em>Aw</em>}</li>
 *   <li>{@link de.dim.bfr.Messwerte#getCo2 <em>Co2</em>}</li>
 *   <li>{@link de.dim.bfr.Messwerte#getDruck <em>Druck</em>}</li>
 *   <li>{@link de.dim.bfr.Messwerte#getTemperatur <em>Temperatur</em>}</li>
 *   <li>{@link de.dim.bfr.Messwerte#getPH <em>PH</em>}</li>
 *   <li>{@link de.dim.bfr.Messwerte#getKonzentration <em>Konzentration</em>}</li>
 *   <li>{@link de.dim.bfr.Messwerte#getZeit <em>Zeit</em>}</li>
 *   <li>{@link de.dim.bfr.Messwerte#getSonstiges <em>Sonstiges</em>}</li>
 *   <li>{@link de.dim.bfr.Messwerte#getVersuchsbedingungen <em>Versuchsbedingungen</em>}</li>
 *   <li>{@link de.dim.bfr.Messwerte#getKonzEinheit <em>Konz Einheit</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.dim.bfr.BfrPackage#getMesswerte()
 * @model
 * @generated
 */
public interface Messwerte extends EObject {
	/**
	 * Returns the value of the '<em><b>Geprueft</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Geprueft</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Geprueft</em>' attribute.
	 * @see #setGeprueft(boolean)
	 * @see de.dim.bfr.BfrPackage#getMesswerte_Geprueft()
	 * @model
	 * @generated
	 */
	boolean isGeprueft();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Messwerte#isGeprueft <em>Geprueft</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Geprueft</em>' attribute.
	 * @see #isGeprueft()
	 * @generated
	 */
	void setGeprueft(boolean value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(int)
	 * @see de.dim.bfr.BfrPackage#getMesswerte_Id()
	 * @model
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Messwerte#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

	/**
	 * Returns the value of the '<em><b>Kommentar</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Kommentar</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kommentar</em>' attribute.
	 * @see #setKommentar(String)
	 * @see de.dim.bfr.BfrPackage#getMesswerte_Kommentar()
	 * @model
	 * @generated
	 */
	String getKommentar();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Messwerte#getKommentar <em>Kommentar</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kommentar</em>' attribute.
	 * @see #getKommentar()
	 * @generated
	 */
	void setKommentar(String value);

	/**
	 * Returns the value of the '<em><b>Zeit Einheit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Zeit Einheit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Zeit Einheit</em>' attribute.
	 * @see #setZeitEinheit(String)
	 * @see de.dim.bfr.BfrPackage#getMesswerte_ZeitEinheit()
	 * @model
	 * @generated
	 */
	String getZeitEinheit();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Messwerte#getZeitEinheit <em>Zeit Einheit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Zeit Einheit</em>' attribute.
	 * @see #getZeitEinheit()
	 * @generated
	 */
	void setZeitEinheit(String value);

	/**
	 * Returns the value of the '<em><b>Aw</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Aw</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Aw</em>' containment reference.
	 * @see #setAw(DoubleKennzahlen)
	 * @see de.dim.bfr.BfrPackage#getMesswerte_Aw()
	 * @model containment="true"
	 * @generated
	 */
	DoubleKennzahlen getAw();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Messwerte#getAw <em>Aw</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Aw</em>' containment reference.
	 * @see #getAw()
	 * @generated
	 */
	void setAw(DoubleKennzahlen value);

	/**
	 * Returns the value of the '<em><b>Co2</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Co2</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Co2</em>' containment reference.
	 * @see #setCo2(DoubleKennzahlen)
	 * @see de.dim.bfr.BfrPackage#getMesswerte_Co2()
	 * @model containment="true"
	 * @generated
	 */
	DoubleKennzahlen getCo2();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Messwerte#getCo2 <em>Co2</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Co2</em>' containment reference.
	 * @see #getCo2()
	 * @generated
	 */
	void setCo2(DoubleKennzahlen value);

	/**
	 * Returns the value of the '<em><b>Druck</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Druck</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Druck</em>' containment reference.
	 * @see #setDruck(DoubleKennzahlen)
	 * @see de.dim.bfr.BfrPackage#getMesswerte_Druck()
	 * @model containment="true"
	 * @generated
	 */
	DoubleKennzahlen getDruck();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Messwerte#getDruck <em>Druck</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Druck</em>' containment reference.
	 * @see #getDruck()
	 * @generated
	 */
	void setDruck(DoubleKennzahlen value);

	/**
	 * Returns the value of the '<em><b>Temperatur</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Temperatur</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Temperatur</em>' containment reference.
	 * @see #setTemperatur(DoubleKennzahlen)
	 * @see de.dim.bfr.BfrPackage#getMesswerte_Temperatur()
	 * @model containment="true"
	 * @generated
	 */
	DoubleKennzahlen getTemperatur();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Messwerte#getTemperatur <em>Temperatur</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Temperatur</em>' containment reference.
	 * @see #getTemperatur()
	 * @generated
	 */
	void setTemperatur(DoubleKennzahlen value);

	/**
	 * Returns the value of the '<em><b>PH</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>PH</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>PH</em>' containment reference.
	 * @see #setPH(DoubleKennzahlen)
	 * @see de.dim.bfr.BfrPackage#getMesswerte_PH()
	 * @model containment="true"
	 * @generated
	 */
	DoubleKennzahlen getPH();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Messwerte#getPH <em>PH</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>PH</em>' containment reference.
	 * @see #getPH()
	 * @generated
	 */
	void setPH(DoubleKennzahlen value);

	/**
	 * Returns the value of the '<em><b>Konzentration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Konzentration</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Konzentration</em>' containment reference.
	 * @see #setKonzentration(DoubleKennzahlen)
	 * @see de.dim.bfr.BfrPackage#getMesswerte_Konzentration()
	 * @model containment="true"
	 * @generated
	 */
	DoubleKennzahlen getKonzentration();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Messwerte#getKonzentration <em>Konzentration</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Konzentration</em>' containment reference.
	 * @see #getKonzentration()
	 * @generated
	 */
	void setKonzentration(DoubleKennzahlen value);

	/**
	 * Returns the value of the '<em><b>Zeit</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Zeit</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Zeit</em>' containment reference.
	 * @see #setZeit(DoubleKennzahlen)
	 * @see de.dim.bfr.BfrPackage#getMesswerte_Zeit()
	 * @model containment="true"
	 * @generated
	 */
	DoubleKennzahlen getZeit();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Messwerte#getZeit <em>Zeit</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Zeit</em>' containment reference.
	 * @see #getZeit()
	 * @generated
	 */
	void setZeit(DoubleKennzahlen value);

	/**
	 * Returns the value of the '<em><b>Sonstiges</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sonstiges</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sonstiges</em>' reference.
	 * @see #setSonstiges(EObject)
	 * @see de.dim.bfr.BfrPackage#getMesswerte_Sonstiges()
	 * @model
	 * @generated
	 */
	EObject getSonstiges();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Messwerte#getSonstiges <em>Sonstiges</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sonstiges</em>' reference.
	 * @see #getSonstiges()
	 * @generated
	 */
	void setSonstiges(EObject value);

	/**
	 * Returns the value of the '<em><b>Versuchsbedingungen</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Versuchsbedingungen</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Versuchsbedingungen</em>' reference.
	 * @see #setVersuchsbedingungen(VersuchsBedingung)
	 * @see de.dim.bfr.BfrPackage#getMesswerte_Versuchsbedingungen()
	 * @model
	 * @generated
	 */
	VersuchsBedingung getVersuchsbedingungen();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Messwerte#getVersuchsbedingungen <em>Versuchsbedingungen</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Versuchsbedingungen</em>' reference.
	 * @see #getVersuchsbedingungen()
	 * @generated
	 */
	void setVersuchsbedingungen(VersuchsBedingung value);

	/**
	 * Returns the value of the '<em><b>Konz Einheit</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Konz Einheit</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Konz Einheit</em>' containment reference.
	 * @see #setKonzEinheit(Einheiten)
	 * @see de.dim.bfr.BfrPackage#getMesswerte_KonzEinheit()
	 * @model containment="true"
	 * @generated
	 */
	Einheiten getKonzEinheit();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Messwerte#getKonzEinheit <em>Konz Einheit</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Konz Einheit</em>' containment reference.
	 * @see #getKonzEinheit()
	 * @generated
	 */
	void setKonzEinheit(Einheiten value);

} // Messwerte
