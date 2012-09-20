/**
 * $Id: GeschaetztStatistikModell.java 651 2012-01-24 09:59:12Z sdoerl $
 * /*******************************************************************************
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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Geschaetzt Statistik Modell</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.dim.bfr.GeschaetztStatistikModell#getStatistikModel <em>Statistik Model</em>}</li>
 *   <li>{@link de.dim.bfr.GeschaetztStatistikModell#isManuellEingetragen <em>Manuell Eingetragen</em>}</li>
 *   <li>{@link de.dim.bfr.GeschaetztStatistikModell#getRSquared <em>RSquared</em>}</li>
 *   <li>{@link de.dim.bfr.GeschaetztStatistikModell#getRss <em>Rss</em>}</li>
 *   <li>{@link de.dim.bfr.GeschaetztStatistikModell#getScore <em>Score</em>}</li>
 *   <li>{@link de.dim.bfr.GeschaetztStatistikModell#getKommentar <em>Kommentar</em>}</li>
 *   <li>{@link de.dim.bfr.GeschaetztStatistikModell#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.GeschaetztStatistikModell#getParameter <em>Parameter</em>}</li>
 *   <li>{@link de.dim.bfr.GeschaetztStatistikModell#getBedingung <em>Bedingung</em>}</li>
 *   <li>{@link de.dim.bfr.GeschaetztStatistikModell#getLiteratur <em>Literatur</em>}</li>
 *   <li>{@link de.dim.bfr.GeschaetztStatistikModell#getParameterCovCor <em>Parameter Cov Cor</em>}</li>
 *   <li>{@link de.dim.bfr.GeschaetztStatistikModell#getResponse <em>Response</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.dim.bfr.BfrPackage#getGeschaetztStatistikModell()
 * @model
 * @generated
 */
public interface GeschaetztStatistikModell extends EObject {
	/**
	 * Returns the value of the '<em><b>Statistik Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Statistik Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Statistik Model</em>' reference.
	 * @see #setStatistikModel(StatistikModell)
	 * @see de.dim.bfr.BfrPackage#getGeschaetztStatistikModell_StatistikModel()
	 * @model required="true"
	 * @generated
	 */
	StatistikModell getStatistikModel();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschaetztStatistikModell#getStatistikModel <em>Statistik Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Statistik Model</em>' reference.
	 * @see #getStatistikModel()
	 * @generated
	 */
	void setStatistikModel(StatistikModell value);

	/**
	 * Returns the value of the '<em><b>Manuell Eingetragen</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Manuell Eingetragen</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Manuell Eingetragen</em>' attribute.
	 * @see #setManuellEingetragen(boolean)
	 * @see de.dim.bfr.BfrPackage#getGeschaetztStatistikModell_ManuellEingetragen()
	 * @model default="false"
	 * @generated
	 */
	boolean isManuellEingetragen();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschaetztStatistikModell#isManuellEingetragen <em>Manuell Eingetragen</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Manuell Eingetragen</em>' attribute.
	 * @see #isManuellEingetragen()
	 * @generated
	 */
	void setManuellEingetragen(boolean value);

	/**
	 * Returns the value of the '<em><b>RSquared</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>RSquared</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>RSquared</em>' attribute.
	 * @see #setRSquared(double)
	 * @see de.dim.bfr.BfrPackage#getGeschaetztStatistikModell_RSquared()
	 * @model
	 * @generated
	 */
	double getRSquared();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschaetztStatistikModell#getRSquared <em>RSquared</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>RSquared</em>' attribute.
	 * @see #getRSquared()
	 * @generated
	 */
	void setRSquared(double value);

	/**
	 * Returns the value of the '<em><b>Rss</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rss</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rss</em>' attribute.
	 * @see #setRss(double)
	 * @see de.dim.bfr.BfrPackage#getGeschaetztStatistikModell_Rss()
	 * @model
	 * @generated
	 */
	double getRss();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschaetztStatistikModell#getRss <em>Rss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rss</em>' attribute.
	 * @see #getRss()
	 * @generated
	 */
	void setRss(double value);

	/**
	 * Returns the value of the '<em><b>Score</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Score</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Score</em>' attribute.
	 * @see #setScore(int)
	 * @see de.dim.bfr.BfrPackage#getGeschaetztStatistikModell_Score()
	 * @model
	 * @generated
	 */
	int getScore();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschaetztStatistikModell#getScore <em>Score</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Score</em>' attribute.
	 * @see #getScore()
	 * @generated
	 */
	void setScore(int value);

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
	 * @see de.dim.bfr.BfrPackage#getGeschaetztStatistikModell_Kommentar()
	 * @model
	 * @generated
	 */
	String getKommentar();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschaetztStatistikModell#getKommentar <em>Kommentar</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kommentar</em>' attribute.
	 * @see #getKommentar()
	 * @generated
	 */
	void setKommentar(String value);

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
	 * @see de.dim.bfr.BfrPackage#getGeschaetztStatistikModell_Id()
	 * @model id="true" required="true"
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschaetztStatistikModell#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

	/**
	 * Returns the value of the '<em><b>Parameter</b></em>' containment reference list.
	 * The list contents are of type {@link de.dim.bfr.GeschModellParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter</em>' containment reference list.
	 * @see de.dim.bfr.BfrPackage#getGeschaetztStatistikModell_Parameter()
	 * @model containment="true"
	 * @generated
	 */
	EList<GeschModellParameter> getParameter();

	/**
	 * Returns the value of the '<em><b>Bedingung</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bedingung</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bedingung</em>' reference.
	 * @see #setBedingung(VersuchsBedingung)
	 * @see de.dim.bfr.BfrPackage#getGeschaetztStatistikModell_Bedingung()
	 * @model
	 * @generated
	 */
	VersuchsBedingung getBedingung();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschaetztStatistikModell#getBedingung <em>Bedingung</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bedingung</em>' reference.
	 * @see #getBedingung()
	 * @generated
	 */
	void setBedingung(VersuchsBedingung value);

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
	 * @see de.dim.bfr.BfrPackage#getGeschaetztStatistikModell_Literatur()
	 * @model
	 * @generated
	 */
	EList<Literatur> getLiteratur();

	/**
	 * Returns the value of the '<em><b>Parameter Cov Cor</b></em>' reference list.
	 * The list contents are of type {@link de.dim.bfr.ParameterCovCor}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Cov Cor</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Cov Cor</em>' reference list.
	 * @see de.dim.bfr.BfrPackage#getGeschaetztStatistikModell_ParameterCovCor()
	 * @model
	 * @generated
	 */
	EList<ParameterCovCor> getParameterCovCor();

	/**
	 * Returns the value of the '<em><b>Response</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Response</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Response</em>' reference.
	 * @see #setResponse(StatistikModellParameter)
	 * @see de.dim.bfr.BfrPackage#getGeschaetztStatistikModell_Response()
	 * @model
	 * @generated
	 */
	StatistikModellParameter getResponse();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschaetztStatistikModell#getResponse <em>Response</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Response</em>' reference.
	 * @see #getResponse()
	 * @generated
	 */
	void setResponse(StatistikModellParameter value);

} // GeschaetztStatistikModell
