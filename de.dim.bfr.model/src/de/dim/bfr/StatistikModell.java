/**
 * $Id: StatistikModell.java 651 2012-01-24 09:59:12Z sdoerl $
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

import java.util.Date;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Statistik Modell</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.dim.bfr.StatistikModell#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModell#getName <em>Name</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModell#getNotation <em>Notation</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModell#getLevel <em>Level</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModell#getKlasse <em>Klasse</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModell#getTyp <em>Typ</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModell#getEingabedatum <em>Eingabedatum</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModell#getBenutzer <em>Benutzer</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModell#getBeschreibung <em>Beschreibung</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModell#getFormel <em>Formel</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModell#getSoftware <em>Software</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModell#getKommentar <em>Kommentar</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModell#getParameter <em>Parameter</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModell#getLiteratur <em>Literatur</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.dim.bfr.BfrPackage#getStatistikModell()
 * @model
 * @generated
 */
public interface StatistikModell extends EObject {
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
	 * @see de.dim.bfr.BfrPackage#getStatistikModell_Id()
	 * @model id="true" required="true"
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModell#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see de.dim.bfr.BfrPackage#getStatistikModell_Name()
	 * @model default="" required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModell#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Notation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notation</em>' attribute.
	 * @see #setNotation(String)
	 * @see de.dim.bfr.BfrPackage#getStatistikModell_Notation()
	 * @model required="true"
	 * @generated
	 */
	String getNotation();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModell#getNotation <em>Notation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notation</em>' attribute.
	 * @see #getNotation()
	 * @generated
	 */
	void setNotation(String value);

	/**
	 * Returns the value of the '<em><b>Level</b></em>' attribute.
	 * The default value is <code>"NONE"</code>.
	 * The literals are from the enumeration {@link de.dim.bfr.LevelTyp}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Level</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Level</em>' attribute.
	 * @see de.dim.bfr.LevelTyp
	 * @see #setLevel(LevelTyp)
	 * @see de.dim.bfr.BfrPackage#getStatistikModell_Level()
	 * @model default="NONE"
	 * @generated
	 */
	LevelTyp getLevel();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModell#getLevel <em>Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Level</em>' attribute.
	 * @see de.dim.bfr.LevelTyp
	 * @see #getLevel()
	 * @generated
	 */
	void setLevel(LevelTyp value);

	/**
	 * Returns the value of the '<em><b>Klasse</b></em>' attribute.
	 * The default value is <code>"UNKNOWN"</code>.
	 * The literals are from the enumeration {@link de.dim.bfr.KlasseTyp}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Klasse</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Klasse</em>' attribute.
	 * @see de.dim.bfr.KlasseTyp
	 * @see #setKlasse(KlasseTyp)
	 * @see de.dim.bfr.BfrPackage#getStatistikModell_Klasse()
	 * @model default="UNKNOWN"
	 * @generated
	 */
	KlasseTyp getKlasse();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModell#getKlasse <em>Klasse</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Klasse</em>' attribute.
	 * @see de.dim.bfr.KlasseTyp
	 * @see #getKlasse()
	 * @generated
	 */
	void setKlasse(KlasseTyp value);

	/**
	 * Returns the value of the '<em><b>Typ</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Typ</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Typ</em>' attribute.
	 * @see #setTyp(String)
	 * @see de.dim.bfr.BfrPackage#getStatistikModell_Typ()
	 * @model
	 * @generated
	 */
	String getTyp();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModell#getTyp <em>Typ</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Typ</em>' attribute.
	 * @see #getTyp()
	 * @generated
	 */
	void setTyp(String value);

	/**
	 * Returns the value of the '<em><b>Eingabedatum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Eingabedatum</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Eingabedatum</em>' attribute.
	 * @see #setEingabedatum(Date)
	 * @see de.dim.bfr.BfrPackage#getStatistikModell_Eingabedatum()
	 * @model required="true"
	 * @generated
	 */
	Date getEingabedatum();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModell#getEingabedatum <em>Eingabedatum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Eingabedatum</em>' attribute.
	 * @see #getEingabedatum()
	 * @generated
	 */
	void setEingabedatum(Date value);

	/**
	 * Returns the value of the '<em><b>Benutzer</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Benutzer</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Benutzer</em>' attribute.
	 * @see #setBenutzer(String)
	 * @see de.dim.bfr.BfrPackage#getStatistikModell_Benutzer()
	 * @model required="true"
	 * @generated
	 */
	String getBenutzer();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModell#getBenutzer <em>Benutzer</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Benutzer</em>' attribute.
	 * @see #getBenutzer()
	 * @generated
	 */
	void setBenutzer(String value);

	/**
	 * Returns the value of the '<em><b>Beschreibung</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Beschreibung</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Beschreibung</em>' attribute.
	 * @see #setBeschreibung(String)
	 * @see de.dim.bfr.BfrPackage#getStatistikModell_Beschreibung()
	 * @model
	 * @generated
	 */
	String getBeschreibung();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModell#getBeschreibung <em>Beschreibung</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Beschreibung</em>' attribute.
	 * @see #getBeschreibung()
	 * @generated
	 */
	void setBeschreibung(String value);

	/**
	 * Returns the value of the '<em><b>Formel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Formel</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Formel</em>' attribute.
	 * @see #setFormel(String)
	 * @see de.dim.bfr.BfrPackage#getStatistikModell_Formel()
	 * @model required="true"
	 * @generated
	 */
	String getFormel();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModell#getFormel <em>Formel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Formel</em>' attribute.
	 * @see #getFormel()
	 * @generated
	 */
	void setFormel(String value);

	/**
	 * Returns the value of the '<em><b>Software</b></em>' attribute.
	 * The default value is <code>"NONE"</code>.
	 * The literals are from the enumeration {@link de.dim.bfr.SoftwareType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Software</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Software</em>' attribute.
	 * @see de.dim.bfr.SoftwareType
	 * @see #setSoftware(SoftwareType)
	 * @see de.dim.bfr.BfrPackage#getStatistikModell_Software()
	 * @model default="NONE"
	 * @generated
	 */
	SoftwareType getSoftware();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModell#getSoftware <em>Software</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Software</em>' attribute.
	 * @see de.dim.bfr.SoftwareType
	 * @see #getSoftware()
	 * @generated
	 */
	void setSoftware(SoftwareType value);

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
	 * @see de.dim.bfr.BfrPackage#getStatistikModell_Kommentar()
	 * @model
	 * @generated
	 */
	String getKommentar();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModell#getKommentar <em>Kommentar</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kommentar</em>' attribute.
	 * @see #getKommentar()
	 * @generated
	 */
	void setKommentar(String value);

	/**
	 * Returns the value of the '<em><b>Parameter</b></em>' containment reference list.
	 * The list contents are of type {@link de.dim.bfr.StatistikModellParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter</em>' containment reference list.
	 * @see de.dim.bfr.BfrPackage#getStatistikModell_Parameter()
	 * @model containment="true"
	 * @generated
	 */
	EList<StatistikModellParameter> getParameter();

	/**
	 * Returns the value of the '<em><b>Literatur</b></em>' reference list.
	 * The list contents are of type {@link de.dim.bfr.Literatur}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Literatur</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Literatur</em>' reference list.
	 * @see de.dim.bfr.BfrPackage#getStatistikModell_Literatur()
	 * @model
	 * @generated
	 */
	EList<Literatur> getLiteratur();

} // StatistikModell
