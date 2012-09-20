/**
 * $Id: VersuchsBedingungImpl.java 651 2012-01-24 09:59:12Z sdoerl $
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
package de.dim.bfr.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import de.dim.bfr.BfrPackage;
import de.dim.bfr.VersuchsBedingung;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Versuchs Bedingung</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.dim.bfr.impl.VersuchsBedingungImpl#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.impl.VersuchsBedingungImpl#getIdCB <em>Id CB</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VersuchsBedingungImpl extends EObjectImpl implements VersuchsBedingung {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final int ID_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected int id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getIdCB() <em>Id CB</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdCB()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_CB_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIdCB() <em>Id CB</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdCB()
	 * @generated
	 * @ordered
	 */
	protected String idCB = ID_CB_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VersuchsBedingungImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BfrPackage.Literals.VERSUCHS_BEDINGUNG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(int newId) {
		int oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.VERSUCHS_BEDINGUNG__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIdCB() {
		return idCB;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIdCB(String newIdCB) {
		String oldIdCB = idCB;
		idCB = newIdCB;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.VERSUCHS_BEDINGUNG__ID_CB, oldIdCB, idCB));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case BfrPackage.VERSUCHS_BEDINGUNG__ID:
				return getId();
			case BfrPackage.VERSUCHS_BEDINGUNG__ID_CB:
				return getIdCB();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case BfrPackage.VERSUCHS_BEDINGUNG__ID:
				setId((Integer)newValue);
				return;
			case BfrPackage.VERSUCHS_BEDINGUNG__ID_CB:
				setIdCB((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case BfrPackage.VERSUCHS_BEDINGUNG__ID:
				setId(ID_EDEFAULT);
				return;
			case BfrPackage.VERSUCHS_BEDINGUNG__ID_CB:
				setIdCB(ID_CB_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case BfrPackage.VERSUCHS_BEDINGUNG__ID:
				return id != ID_EDEFAULT;
			case BfrPackage.VERSUCHS_BEDINGUNG__ID_CB:
				return ID_CB_EDEFAULT == null ? idCB != null : !ID_CB_EDEFAULT.equals(idCB);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (id: ");
		result.append(id);
		result.append(", idCB: ");
		result.append(idCB);
		result.append(')');
		return result.toString();
	}

} //VersuchsBedingungImpl
