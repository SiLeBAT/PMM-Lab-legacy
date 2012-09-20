/**
 * $Id: GeschModellParameter.java 651 2012-01-24 09:59:12Z sdoerl $
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

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Gesch Modell Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.dim.bfr.GeschModellParameter#getName <em>Name</em>}</li>
 *   <li>{@link de.dim.bfr.GeschModellParameter#getSd <em>Sd</em>}</li>
 *   <li>{@link de.dim.bfr.GeschModellParameter#getWert <em>Wert</em>}</li>
 *   <li>{@link de.dim.bfr.GeschModellParameter#getVersuchsBedingung <em>Versuchs Bedingung</em>}</li>
 *   <li>{@link de.dim.bfr.GeschModellParameter#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.GeschModellParameter#getT <em>T</em>}</li>
 *   <li>{@link de.dim.bfr.GeschModellParameter#getP <em>P</em>}</li>
 *   <li>{@link de.dim.bfr.GeschModellParameter#getGeschaetztesModell <em>Geschaetztes Modell</em>}</li>
 *   <li>{@link de.dim.bfr.GeschModellParameter#getModelParameter <em>Model Parameter</em>}</li>
 *   <li>{@link de.dim.bfr.GeschModellParameter#getKiOben <em>Ki Oben</em>}</li>
 *   <li>{@link de.dim.bfr.GeschModellParameter#getKiUnten <em>Ki Unten</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.dim.bfr.BfrPackage#getGeschModellParameter()
 * @model
 * @generated
 */
public interface GeschModellParameter extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see de.dim.bfr.BfrPackage#getGeschModellParameter_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschModellParameter#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Sd</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sd</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sd</em>' attribute.
	 * @see #setSd(Double)
	 * @see de.dim.bfr.BfrPackage#getGeschModellParameter_Sd()
	 * @model
	 * @generated
	 */
	Double getSd();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschModellParameter#getSd <em>Sd</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sd</em>' attribute.
	 * @see #getSd()
	 * @generated
	 */
	void setSd(Double value);

	/**
	 * Returns the value of the '<em><b>Wert</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wert</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wert</em>' attribute.
	 * @see #setWert(Double)
	 * @see de.dim.bfr.BfrPackage#getGeschModellParameter_Wert()
	 * @model
	 * @generated
	 */
	Double getWert();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschModellParameter#getWert <em>Wert</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wert</em>' attribute.
	 * @see #getWert()
	 * @generated
	 */
	void setWert(Double value);

	/**
	 * Returns the value of the '<em><b>Versuchs Bedingung</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Versuchs Bedingung</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Versuchs Bedingung</em>' reference.
	 * @see #setVersuchsBedingung(VersuchsBedingung)
	 * @see de.dim.bfr.BfrPackage#getGeschModellParameter_VersuchsBedingung()
	 * @model required="true"
	 * @generated
	 */
	VersuchsBedingung getVersuchsBedingung();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschModellParameter#getVersuchsBedingung <em>Versuchs Bedingung</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Versuchs Bedingung</em>' reference.
	 * @see #getVersuchsBedingung()
	 * @generated
	 */
	void setVersuchsBedingung(VersuchsBedingung value);

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
	 * @see de.dim.bfr.BfrPackage#getGeschModellParameter_Id()
	 * @model id="true" required="true"
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschModellParameter#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

	/**
	 * Returns the value of the '<em><b>T</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>T</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>T</em>' attribute.
	 * @see #setT(Double)
	 * @see de.dim.bfr.BfrPackage#getGeschModellParameter_T()
	 * @model
	 * @generated
	 */
	Double getT();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschModellParameter#getT <em>T</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>T</em>' attribute.
	 * @see #getT()
	 * @generated
	 */
	void setT(Double value);

	/**
	 * Returns the value of the '<em><b>P</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>P</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>P</em>' attribute.
	 * @see #setP(Double)
	 * @see de.dim.bfr.BfrPackage#getGeschModellParameter_P()
	 * @model
	 * @generated
	 */
	Double getP();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschModellParameter#getP <em>P</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>P</em>' attribute.
	 * @see #getP()
	 * @generated
	 */
	void setP(Double value);

	/**
	 * Returns the value of the '<em><b>Geschaetztes Modell</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Geschaetztes Modell</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Geschaetztes Modell</em>' reference.
	 * @see #setGeschaetztesModell(GeschaetztStatistikModell)
	 * @see de.dim.bfr.BfrPackage#getGeschModellParameter_GeschaetztesModell()
	 * @model required="true"
	 * @generated
	 */
	GeschaetztStatistikModell getGeschaetztesModell();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschModellParameter#getGeschaetztesModell <em>Geschaetztes Modell</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Geschaetztes Modell</em>' reference.
	 * @see #getGeschaetztesModell()
	 * @generated
	 */
	void setGeschaetztesModell(GeschaetztStatistikModell value);

	/**
	 * Returns the value of the '<em><b>Model Parameter</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model Parameter</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Parameter</em>' reference.
	 * @see #setModelParameter(StatistikModellParameter)
	 * @see de.dim.bfr.BfrPackage#getGeschModellParameter_ModelParameter()
	 * @model required="true"
	 * @generated
	 */
	StatistikModellParameter getModelParameter();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschModellParameter#getModelParameter <em>Model Parameter</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Parameter</em>' reference.
	 * @see #getModelParameter()
	 * @generated
	 */
	void setModelParameter(StatistikModellParameter value);

	/**
	 * Returns the value of the '<em><b>Ki Oben</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ki Oben</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ki Oben</em>' attribute.
	 * @see #setKiOben(double)
	 * @see de.dim.bfr.BfrPackage#getGeschModellParameter_KiOben()
	 * @model
	 * @generated
	 */
	double getKiOben();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschModellParameter#getKiOben <em>Ki Oben</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ki Oben</em>' attribute.
	 * @see #getKiOben()
	 * @generated
	 */
	void setKiOben(double value);

	/**
	 * Returns the value of the '<em><b>Ki Unten</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ki Unten</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ki Unten</em>' attribute.
	 * @see #setKiUnten(double)
	 * @see de.dim.bfr.BfrPackage#getGeschModellParameter_KiUnten()
	 * @model
	 * @generated
	 */
	double getKiUnten();

	/**
	 * Sets the value of the '{@link de.dim.bfr.GeschModellParameter#getKiUnten <em>Ki Unten</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ki Unten</em>' attribute.
	 * @see #getKiUnten()
	 * @generated
	 */
	void setKiUnten(double value);

} // GeschModellParameter
