/**
 * $Id: ParameterCovCor.java 651 2012-01-24 09:59:12Z sdoerl $
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
 * A representation of the model object '<em><b>Parameter Cov Cor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.dim.bfr.ParameterCovCor#getParameter1 <em>Parameter1</em>}</li>
 *   <li>{@link de.dim.bfr.ParameterCovCor#getParameter2 <em>Parameter2</em>}</li>
 *   <li>{@link de.dim.bfr.ParameterCovCor#getValue <em>Value</em>}</li>
 *   <li>{@link de.dim.bfr.ParameterCovCor#isCor <em>Cor</em>}</li>
 *   <li>{@link de.dim.bfr.ParameterCovCor#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.dim.bfr.BfrPackage#getParameterCovCor()
 * @model
 * @generated
 */
public interface ParameterCovCor extends EObject {
	/**
	 * Returns the value of the '<em><b>Parameter1</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter1</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter1</em>' reference.
	 * @see #setParameter1(GeschModellParameter)
	 * @see de.dim.bfr.BfrPackage#getParameterCovCor_Parameter1()
	 * @model required="true"
	 * @generated
	 */
	GeschModellParameter getParameter1();

	/**
	 * Sets the value of the '{@link de.dim.bfr.ParameterCovCor#getParameter1 <em>Parameter1</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter1</em>' reference.
	 * @see #getParameter1()
	 * @generated
	 */
	void setParameter1(GeschModellParameter value);

	/**
	 * Returns the value of the '<em><b>Parameter2</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter2</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter2</em>' reference.
	 * @see #setParameter2(GeschModellParameter)
	 * @see de.dim.bfr.BfrPackage#getParameterCovCor_Parameter2()
	 * @model
	 * @generated
	 */
	GeschModellParameter getParameter2();

	/**
	 * Sets the value of the '{@link de.dim.bfr.ParameterCovCor#getParameter2 <em>Parameter2</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameter2</em>' reference.
	 * @see #getParameter2()
	 * @generated
	 */
	void setParameter2(GeschModellParameter value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(Double)
	 * @see de.dim.bfr.BfrPackage#getParameterCovCor_Value()
	 * @model
	 * @generated
	 */
	Double getValue();

	/**
	 * Sets the value of the '{@link de.dim.bfr.ParameterCovCor#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(Double value);

	/**
	 * Returns the value of the '<em><b>Cor</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cor</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cor</em>' attribute.
	 * @see #setCor(boolean)
	 * @see de.dim.bfr.BfrPackage#getParameterCovCor_Cor()
	 * @model default="false" required="true"
	 * @generated
	 */
	boolean isCor();

	/**
	 * Sets the value of the '{@link de.dim.bfr.ParameterCovCor#isCor <em>Cor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cor</em>' attribute.
	 * @see #isCor()
	 * @generated
	 */
	void setCor(boolean value);

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
	 * @see de.dim.bfr.BfrPackage#getParameterCovCor_Id()
	 * @model required="true"
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link de.dim.bfr.ParameterCovCor#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

} // ParameterCovCor
