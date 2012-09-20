/**
 * $Id: StatistikModellParameter.java 651 2012-01-24 09:59:12Z sdoerl $
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
 * A representation of the model object '<em><b>Statistik Modell Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.dim.bfr.StatistikModellParameter#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModellParameter#getName <em>Name</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModellParameter#getMin <em>Min</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModellParameter#getMax <em>Max</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModellParameter#getBeschreibung <em>Beschreibung</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModellParameter#getRole <em>Role</em>}</li>
 *   <li>{@link de.dim.bfr.StatistikModellParameter#isInteger <em>Integer</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.dim.bfr.BfrPackage#getStatistikModellParameter()
 * @model
 * @generated
 */
public interface StatistikModellParameter extends EObject {
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
	 * @see de.dim.bfr.BfrPackage#getStatistikModellParameter_Id()
	 * @model id="true" required="true"
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModellParameter#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

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
	 * @see de.dim.bfr.BfrPackage#getStatistikModellParameter_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModellParameter#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Min</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min</em>' attribute.
	 * @see #setMin(Double)
	 * @see de.dim.bfr.BfrPackage#getStatistikModellParameter_Min()
	 * @model
	 * @generated
	 */
	Double getMin();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModellParameter#getMin <em>Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min</em>' attribute.
	 * @see #getMin()
	 * @generated
	 */
	void setMin(Double value);

	/**
	 * Returns the value of the '<em><b>Max</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max</em>' attribute.
	 * @see #setMax(Double)
	 * @see de.dim.bfr.BfrPackage#getStatistikModellParameter_Max()
	 * @model
	 * @generated
	 */
	Double getMax();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModellParameter#getMax <em>Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max</em>' attribute.
	 * @see #getMax()
	 * @generated
	 */
	void setMax(Double value);

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
	 * @see de.dim.bfr.BfrPackage#getStatistikModellParameter_Beschreibung()
	 * @model
	 * @generated
	 */
	String getBeschreibung();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModellParameter#getBeschreibung <em>Beschreibung</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Beschreibung</em>' attribute.
	 * @see #getBeschreibung()
	 * @generated
	 */
	void setBeschreibung(String value);

	/**
	 * Returns the value of the '<em><b>Role</b></em>' attribute.
	 * The default value is <code>"NONE"</code>.
	 * The literals are from the enumeration {@link de.dim.bfr.ParameterRoleType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Role</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Role</em>' attribute.
	 * @see de.dim.bfr.ParameterRoleType
	 * @see #setRole(ParameterRoleType)
	 * @see de.dim.bfr.BfrPackage#getStatistikModellParameter_Role()
	 * @model default="NONE"
	 * @generated
	 */
	ParameterRoleType getRole();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModellParameter#getRole <em>Role</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Role</em>' attribute.
	 * @see de.dim.bfr.ParameterRoleType
	 * @see #getRole()
	 * @generated
	 */
	void setRole(ParameterRoleType value);

	/**
	 * Returns the value of the '<em><b>Integer</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Integer</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Integer</em>' attribute.
	 * @see #setInteger(boolean)
	 * @see de.dim.bfr.BfrPackage#getStatistikModellParameter_Integer()
	 * @model default="true"
	 * @generated
	 */
	boolean isInteger();

	/**
	 * Sets the value of the '{@link de.dim.bfr.StatistikModellParameter#isInteger <em>Integer</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Integer</em>' attribute.
	 * @see #isInteger()
	 * @generated
	 */
	void setInteger(boolean value);

} // StatistikModellParameter
