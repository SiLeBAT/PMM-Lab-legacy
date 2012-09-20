/**
 * $Id: Literatur.java 651 2012-01-24 09:59:12Z sdoerl $
 *******************************************************************************
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
 * A representation of the model object '<em><b>Literatur</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.dim.bfr.Literatur#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.Literatur#getErstautor <em>Erstautor</em>}</li>
 *   <li>{@link de.dim.bfr.Literatur#getJahr <em>Jahr</em>}</li>
 *   <li>{@link de.dim.bfr.Literatur#getTitel <em>Titel</em>}</li>
 *   <li>{@link de.dim.bfr.Literatur#getLiteraturAbstract <em>Literatur Abstract</em>}</li>
 *   <li>{@link de.dim.bfr.Literatur#getJournal <em>Journal</em>}</li>
 *   <li>{@link de.dim.bfr.Literatur#getVolume <em>Volume</em>}</li>
 *   <li>{@link de.dim.bfr.Literatur#getIssue <em>Issue</em>}</li>
 *   <li>{@link de.dim.bfr.Literatur#getFreigabeModus <em>Freigabe Modus</em>}</li>
 *   <li>{@link de.dim.bfr.Literatur#getWebseite <em>Webseite</em>}</li>
 *   <li>{@link de.dim.bfr.Literatur#getLiteraturTyp <em>Literatur Typ</em>}</li>
 *   <li>{@link de.dim.bfr.Literatur#getPaper <em>Paper</em>}</li>
 *   <li>{@link de.dim.bfr.Literatur#getKommentar <em>Kommentar</em>}</li>
 *   <li>{@link de.dim.bfr.Literatur#getSeite <em>Seite</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.dim.bfr.BfrPackage#getLiteratur()
 * @model
 * @generated
 */
public interface Literatur extends EObject {
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
	 * @see de.dim.bfr.BfrPackage#getLiteratur_Id()
	 * @model id="true" required="true"
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Literatur#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

	/**
	 * Returns the value of the '<em><b>Erstautor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Erstautor</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Erstautor</em>' attribute.
	 * @see #setErstautor(String)
	 * @see de.dim.bfr.BfrPackage#getLiteratur_Erstautor()
	 * @model
	 * @generated
	 */
	String getErstautor();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Literatur#getErstautor <em>Erstautor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Erstautor</em>' attribute.
	 * @see #getErstautor()
	 * @generated
	 */
	void setErstautor(String value);

	/**
	 * Returns the value of the '<em><b>Jahr</b></em>' attribute.
	 * The default value is <code>"2000"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Jahr</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Jahr</em>' attribute.
	 * @see #setJahr(int)
	 * @see de.dim.bfr.BfrPackage#getLiteratur_Jahr()
	 * @model default="2000"
	 * @generated
	 */
	int getJahr();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Literatur#getJahr <em>Jahr</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Jahr</em>' attribute.
	 * @see #getJahr()
	 * @generated
	 */
	void setJahr(int value);

	/**
	 * Returns the value of the '<em><b>Titel</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Titel</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Titel</em>' attribute.
	 * @see #setTitel(String)
	 * @see de.dim.bfr.BfrPackage#getLiteratur_Titel()
	 * @model default=""
	 * @generated
	 */
	String getTitel();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Literatur#getTitel <em>Titel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Titel</em>' attribute.
	 * @see #getTitel()
	 * @generated
	 */
	void setTitel(String value);

	/**
	 * Returns the value of the '<em><b>Literatur Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Literatur Abstract</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Literatur Abstract</em>' attribute.
	 * @see #setLiteraturAbstract(String)
	 * @see de.dim.bfr.BfrPackage#getLiteratur_LiteraturAbstract()
	 * @model
	 * @generated
	 */
	String getLiteraturAbstract();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Literatur#getLiteraturAbstract <em>Literatur Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Literatur Abstract</em>' attribute.
	 * @see #getLiteraturAbstract()
	 * @generated
	 */
	void setLiteraturAbstract(String value);

	/**
	 * Returns the value of the '<em><b>Journal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Journal</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Journal</em>' attribute.
	 * @see #setJournal(String)
	 * @see de.dim.bfr.BfrPackage#getLiteratur_Journal()
	 * @model
	 * @generated
	 */
	String getJournal();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Literatur#getJournal <em>Journal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Journal</em>' attribute.
	 * @see #getJournal()
	 * @generated
	 */
	void setJournal(String value);

	/**
	 * Returns the value of the '<em><b>Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Volume</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume</em>' attribute.
	 * @see #setVolume(String)
	 * @see de.dim.bfr.BfrPackage#getLiteratur_Volume()
	 * @model
	 * @generated
	 */
	String getVolume();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Literatur#getVolume <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume</em>' attribute.
	 * @see #getVolume()
	 * @generated
	 */
	void setVolume(String value);

	/**
	 * Returns the value of the '<em><b>Issue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Issue</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Issue</em>' attribute.
	 * @see #setIssue(String)
	 * @see de.dim.bfr.BfrPackage#getLiteratur_Issue()
	 * @model
	 * @generated
	 */
	String getIssue();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Literatur#getIssue <em>Issue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Issue</em>' attribute.
	 * @see #getIssue()
	 * @generated
	 */
	void setIssue(String value);

	/**
	 * Returns the value of the '<em><b>Seite</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Seite</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Seite</em>' attribute.
	 * @see #setSeite(Integer)
	 * @see de.dim.bfr.BfrPackage#getLiteratur_Seite()
	 * @model
	 * @generated
	 */
	Integer getSeite();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Literatur#getSeite <em>Seite</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Seite</em>' attribute.
	 * @see #getSeite()
	 * @generated
	 */
	void setSeite(Integer value);

	/**
	 * Returns the value of the '<em><b>Freigabe Modus</b></em>' attribute.
	 * The literals are from the enumeration {@link de.dim.bfr.FreigabeTyp}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Freigabe Modus</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Freigabe Modus</em>' attribute.
	 * @see de.dim.bfr.FreigabeTyp
	 * @see #setFreigabeModus(FreigabeTyp)
	 * @see de.dim.bfr.BfrPackage#getLiteratur_FreigabeModus()
	 * @model required="true"
	 * @generated
	 */
	FreigabeTyp getFreigabeModus();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Literatur#getFreigabeModus <em>Freigabe Modus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Freigabe Modus</em>' attribute.
	 * @see de.dim.bfr.FreigabeTyp
	 * @see #getFreigabeModus()
	 * @generated
	 */
	void setFreigabeModus(FreigabeTyp value);

	/**
	 * Returns the value of the '<em><b>Webseite</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Webseite</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Webseite</em>' attribute.
	 * @see #setWebseite(String)
	 * @see de.dim.bfr.BfrPackage#getLiteratur_Webseite()
	 * @model
	 * @generated
	 */
	String getWebseite();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Literatur#getWebseite <em>Webseite</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Webseite</em>' attribute.
	 * @see #getWebseite()
	 * @generated
	 */
	void setWebseite(String value);

	/**
	 * Returns the value of the '<em><b>Literatur Typ</b></em>' attribute.
	 * The literals are from the enumeration {@link de.dim.bfr.LiteraturTyp}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Literatur Typ</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Literatur Typ</em>' attribute.
	 * @see de.dim.bfr.LiteraturTyp
	 * @see #setLiteraturTyp(LiteraturTyp)
	 * @see de.dim.bfr.BfrPackage#getLiteratur_LiteraturTyp()
	 * @model required="true"
	 * @generated
	 */
	LiteraturTyp getLiteraturTyp();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Literatur#getLiteraturTyp <em>Literatur Typ</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Literatur Typ</em>' attribute.
	 * @see de.dim.bfr.LiteraturTyp
	 * @see #getLiteraturTyp()
	 * @generated
	 */
	void setLiteraturTyp(LiteraturTyp value);

	/**
	 * Returns the value of the '<em><b>Paper</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Paper</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Paper</em>' attribute.
	 * @see #setPaper(String)
	 * @see de.dim.bfr.BfrPackage#getLiteratur_Paper()
	 * @model
	 * @generated
	 */
	String getPaper();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Literatur#getPaper <em>Paper</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Paper</em>' attribute.
	 * @see #getPaper()
	 * @generated
	 */
	void setPaper(String value);

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
	 * @see de.dim.bfr.BfrPackage#getLiteratur_Kommentar()
	 * @model
	 * @generated
	 */
	String getKommentar();

	/**
	 * Sets the value of the '{@link de.dim.bfr.Literatur#getKommentar <em>Kommentar</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kommentar</em>' attribute.
	 * @see #getKommentar()
	 * @generated
	 */
	void setKommentar(String value);

} // Literatur
