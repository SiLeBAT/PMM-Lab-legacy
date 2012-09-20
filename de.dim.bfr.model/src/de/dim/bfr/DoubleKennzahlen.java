/**
 *
 * $Id: DoubleKennzahlen.java 651 2012-01-24 09:59:12Z sdoerl $
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
 * A representation of the model object '<em><b>Double Kennzahlen</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getFunctionX <em>Function X</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#isFunctionXG <em>Function XG</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getFunctionZeit <em>Function Zeit</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#isFunctionZeitG <em>Function Zeit G</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getLcl95 <em>Lcl95</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#isLcl95G <em>Lcl95 G</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getMaximum <em>Maximum</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#isMaximumG <em>Maximum G</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getMedian <em>Median</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#isMedianG <em>Median G</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getMinimum <em>Minimum</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#isMinimumG <em>Minimum G</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getStandardabweichung <em>Standardabweichung</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#isStandardabweichungG <em>Standardabweichung G</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getTimes10PowerOf <em>Times10 Power Of</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getUcl95 <em>Ucl95</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#isUcl95G <em>Ucl95 G</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#isUndefinedND <em>Undefined ND</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getVerteilung <em>Verteilung</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#isVerteilungG <em>Verteilung G</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getWert <em>Wert</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#isWertG <em>Wert G</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getWertTyp <em>Wert Typ</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getWiederholungen <em>Wiederholungen</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#isWiederholungenG <em>Wiederholungen G</em>}</li>
 *   <li>{@link de.dim.bfr.DoubleKennzahlen#getX <em>X</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen()
 * @model
 * @generated
 */
public interface DoubleKennzahlen extends EObject {
	/**
	 * Returns the value of the '<em><b>Function X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Function X</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Function X</em>' attribute.
	 * @see #setFunctionX(String)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_FunctionX()
	 * @model
	 * @generated
	 */
	String getFunctionX();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getFunctionX <em>Function X</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Function X</em>' attribute.
	 * @see #getFunctionX()
	 * @generated
	 */
	void setFunctionX(String value);

	/**
	 * Returns the value of the '<em><b>Function XG</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Function XG</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Function XG</em>' attribute.
	 * @see #setFunctionXG(boolean)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_FunctionXG()
	 * @model
	 * @generated
	 */
	boolean isFunctionXG();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#isFunctionXG <em>Function XG</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Function XG</em>' attribute.
	 * @see #isFunctionXG()
	 * @generated
	 */
	void setFunctionXG(boolean value);

	/**
	 * Returns the value of the '<em><b>Function Zeit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Function Zeit</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Function Zeit</em>' attribute.
	 * @see #setFunctionZeit(String)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_FunctionZeit()
	 * @model
	 * @generated
	 */
	String getFunctionZeit();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getFunctionZeit <em>Function Zeit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Function Zeit</em>' attribute.
	 * @see #getFunctionZeit()
	 * @generated
	 */
	void setFunctionZeit(String value);

	/**
	 * Returns the value of the '<em><b>Function Zeit G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Function Zeit G</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Function Zeit G</em>' attribute.
	 * @see #setFunctionZeitG(boolean)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_FunctionZeitG()
	 * @model
	 * @generated
	 */
	boolean isFunctionZeitG();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#isFunctionZeitG <em>Function Zeit G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Function Zeit G</em>' attribute.
	 * @see #isFunctionZeitG()
	 * @generated
	 */
	void setFunctionZeitG(boolean value);

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
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_Id()
	 * @model
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

	/**
	 * Returns the value of the '<em><b>Lcl95</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lcl95</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lcl95</em>' attribute.
	 * @see #setLcl95(double)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_Lcl95()
	 * @model
	 * @generated
	 */
	double getLcl95();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getLcl95 <em>Lcl95</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lcl95</em>' attribute.
	 * @see #getLcl95()
	 * @generated
	 */
	void setLcl95(double value);

	/**
	 * Returns the value of the '<em><b>Lcl95 G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lcl95 G</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lcl95 G</em>' attribute.
	 * @see #setLcl95G(boolean)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_Lcl95G()
	 * @model
	 * @generated
	 */
	boolean isLcl95G();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#isLcl95G <em>Lcl95 G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lcl95 G</em>' attribute.
	 * @see #isLcl95G()
	 * @generated
	 */
	void setLcl95G(boolean value);

	/**
	 * Returns the value of the '<em><b>Maximum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Maximum</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Maximum</em>' attribute.
	 * @see #setMaximum(double)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_Maximum()
	 * @model
	 * @generated
	 */
	double getMaximum();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getMaximum <em>Maximum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Maximum</em>' attribute.
	 * @see #getMaximum()
	 * @generated
	 */
	void setMaximum(double value);

	/**
	 * Returns the value of the '<em><b>Maximum G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Maximum G</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Maximum G</em>' attribute.
	 * @see #setMaximumG(boolean)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_MaximumG()
	 * @model
	 * @generated
	 */
	boolean isMaximumG();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#isMaximumG <em>Maximum G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Maximum G</em>' attribute.
	 * @see #isMaximumG()
	 * @generated
	 */
	void setMaximumG(boolean value);

	/**
	 * Returns the value of the '<em><b>Median</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Median</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Median</em>' attribute.
	 * @see #setMedian(double)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_Median()
	 * @model
	 * @generated
	 */
	double getMedian();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getMedian <em>Median</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Median</em>' attribute.
	 * @see #getMedian()
	 * @generated
	 */
	void setMedian(double value);

	/**
	 * Returns the value of the '<em><b>Median G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Median G</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Median G</em>' attribute.
	 * @see #setMedianG(boolean)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_MedianG()
	 * @model
	 * @generated
	 */
	boolean isMedianG();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#isMedianG <em>Median G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Median G</em>' attribute.
	 * @see #isMedianG()
	 * @generated
	 */
	void setMedianG(boolean value);

	/**
	 * Returns the value of the '<em><b>Minimum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Minimum</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Minimum</em>' attribute.
	 * @see #setMinimum(double)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_Minimum()
	 * @model
	 * @generated
	 */
	double getMinimum();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getMinimum <em>Minimum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minimum</em>' attribute.
	 * @see #getMinimum()
	 * @generated
	 */
	void setMinimum(double value);

	/**
	 * Returns the value of the '<em><b>Minimum G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Minimum G</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Minimum G</em>' attribute.
	 * @see #setMinimumG(boolean)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_MinimumG()
	 * @model
	 * @generated
	 */
	boolean isMinimumG();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#isMinimumG <em>Minimum G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Minimum G</em>' attribute.
	 * @see #isMinimumG()
	 * @generated
	 */
	void setMinimumG(boolean value);

	/**
	 * Returns the value of the '<em><b>Standardabweichung</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Standardabweichung</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Standardabweichung</em>' attribute.
	 * @see #setStandardabweichung(double)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_Standardabweichung()
	 * @model
	 * @generated
	 */
	double getStandardabweichung();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getStandardabweichung <em>Standardabweichung</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Standardabweichung</em>' attribute.
	 * @see #getStandardabweichung()
	 * @generated
	 */
	void setStandardabweichung(double value);

	/**
	 * Returns the value of the '<em><b>Standardabweichung G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Standardabweichung G</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Standardabweichung G</em>' attribute.
	 * @see #setStandardabweichungG(boolean)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_StandardabweichungG()
	 * @model
	 * @generated
	 */
	boolean isStandardabweichungG();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#isStandardabweichungG <em>Standardabweichung G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Standardabweichung G</em>' attribute.
	 * @see #isStandardabweichungG()
	 * @generated
	 */
	void setStandardabweichungG(boolean value);

	/**
	 * Returns the value of the '<em><b>Times10 Power Of</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Times10 Power Of</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Times10 Power Of</em>' attribute.
	 * @see #setTimes10PowerOf(double)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_Times10PowerOf()
	 * @model
	 * @generated
	 */
	double getTimes10PowerOf();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getTimes10PowerOf <em>Times10 Power Of</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Times10 Power Of</em>' attribute.
	 * @see #getTimes10PowerOf()
	 * @generated
	 */
	void setTimes10PowerOf(double value);

	/**
	 * Returns the value of the '<em><b>Ucl95</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ucl95</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ucl95</em>' attribute.
	 * @see #setUcl95(double)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_Ucl95()
	 * @model
	 * @generated
	 */
	double getUcl95();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getUcl95 <em>Ucl95</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ucl95</em>' attribute.
	 * @see #getUcl95()
	 * @generated
	 */
	void setUcl95(double value);

	/**
	 * Returns the value of the '<em><b>Ucl95 G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ucl95 G</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ucl95 G</em>' attribute.
	 * @see #setUcl95G(boolean)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_Ucl95G()
	 * @model
	 * @generated
	 */
	boolean isUcl95G();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#isUcl95G <em>Ucl95 G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ucl95 G</em>' attribute.
	 * @see #isUcl95G()
	 * @generated
	 */
	void setUcl95G(boolean value);

	/**
	 * Returns the value of the '<em><b>Undefined ND</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Undefined ND</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Undefined ND</em>' attribute.
	 * @see #setUndefinedND(boolean)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_UndefinedND()
	 * @model
	 * @generated
	 */
	boolean isUndefinedND();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#isUndefinedND <em>Undefined ND</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Undefined ND</em>' attribute.
	 * @see #isUndefinedND()
	 * @generated
	 */
	void setUndefinedND(boolean value);

	/**
	 * Returns the value of the '<em><b>Verteilung</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Verteilung</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Verteilung</em>' attribute.
	 * @see #setVerteilung(String)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_Verteilung()
	 * @model
	 * @generated
	 */
	String getVerteilung();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getVerteilung <em>Verteilung</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Verteilung</em>' attribute.
	 * @see #getVerteilung()
	 * @generated
	 */
	void setVerteilung(String value);

	/**
	 * Returns the value of the '<em><b>Verteilung G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Verteilung G</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Verteilung G</em>' attribute.
	 * @see #setVerteilungG(boolean)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_VerteilungG()
	 * @model
	 * @generated
	 */
	boolean isVerteilungG();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#isVerteilungG <em>Verteilung G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Verteilung G</em>' attribute.
	 * @see #isVerteilungG()
	 * @generated
	 */
	void setVerteilungG(boolean value);

	/**
	 * Returns the value of the '<em><b>Wert</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wert</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wert</em>' attribute.
	 * @see #setWert(double)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_Wert()
	 * @model
	 * @generated
	 */
	double getWert();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getWert <em>Wert</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wert</em>' attribute.
	 * @see #getWert()
	 * @generated
	 */
	void setWert(double value);

	/**
	 * Returns the value of the '<em><b>Wert G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wert G</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wert G</em>' attribute.
	 * @see #setWertG(boolean)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_WertG()
	 * @model
	 * @generated
	 */
	boolean isWertG();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#isWertG <em>Wert G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wert G</em>' attribute.
	 * @see #isWertG()
	 * @generated
	 */
	void setWertG(boolean value);

	/**
	 * Returns the value of the '<em><b>Wert Typ</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wert Typ</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wert Typ</em>' attribute.
	 * @see #setWertTyp(int)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_WertTyp()
	 * @model
	 * @generated
	 */
	int getWertTyp();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getWertTyp <em>Wert Typ</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wert Typ</em>' attribute.
	 * @see #getWertTyp()
	 * @generated
	 */
	void setWertTyp(int value);

	/**
	 * Returns the value of the '<em><b>Wiederholungen</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wiederholungen</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wiederholungen</em>' attribute.
	 * @see #setWiederholungen(double)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_Wiederholungen()
	 * @model
	 * @generated
	 */
	double getWiederholungen();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getWiederholungen <em>Wiederholungen</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wiederholungen</em>' attribute.
	 * @see #getWiederholungen()
	 * @generated
	 */
	void setWiederholungen(double value);

	/**
	 * Returns the value of the '<em><b>Wiederholungen G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wiederholungen G</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wiederholungen G</em>' attribute.
	 * @see #setWiederholungenG(boolean)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_WiederholungenG()
	 * @model
	 * @generated
	 */
	boolean isWiederholungenG();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#isWiederholungenG <em>Wiederholungen G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wiederholungen G</em>' attribute.
	 * @see #isWiederholungenG()
	 * @generated
	 */
	void setWiederholungenG(boolean value);

	/**
	 * Returns the value of the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>X</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>X</em>' attribute.
	 * @see #setX(String)
	 * @see de.dim.bfr.BfrPackage#getDoubleKennzahlen_X()
	 * @model
	 * @generated
	 */
	String getX();

	/**
	 * Sets the value of the '{@link de.dim.bfr.DoubleKennzahlen#getX <em>X</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>X</em>' attribute.
	 * @see #getX()
	 * @generated
	 */
	void setX(String value);

} // DoubleKennzahlen
