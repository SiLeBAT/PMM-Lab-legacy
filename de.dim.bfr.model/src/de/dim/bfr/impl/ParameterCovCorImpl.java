/**
 * $Id: ParameterCovCorImpl.java 651 2012-01-24 09:59:12Z sdoerl $
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
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import de.dim.bfr.BfrPackage;
import de.dim.bfr.GeschModellParameter;
import de.dim.bfr.ParameterCovCor;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter Cov Cor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.dim.bfr.impl.ParameterCovCorImpl#getParameter1 <em>Parameter1</em>}</li>
 *   <li>{@link de.dim.bfr.impl.ParameterCovCorImpl#getParameter2 <em>Parameter2</em>}</li>
 *   <li>{@link de.dim.bfr.impl.ParameterCovCorImpl#getValue <em>Value</em>}</li>
 *   <li>{@link de.dim.bfr.impl.ParameterCovCorImpl#isCor <em>Cor</em>}</li>
 *   <li>{@link de.dim.bfr.impl.ParameterCovCorImpl#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ParameterCovCorImpl extends EObjectImpl implements ParameterCovCor {
	/**
	 * The cached value of the '{@link #getParameter1() <em>Parameter1</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameter1()
	 * @generated
	 * @ordered
	 */
	protected GeschModellParameter parameter1;

	/**
	 * The cached value of the '{@link #getParameter2() <em>Parameter2</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameter2()
	 * @generated
	 * @ordered
	 */
	protected GeschModellParameter parameter2;

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final Double VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected Double value = VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #isCor() <em>Cor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCor()
	 * @generated
	 * @ordered
	 */
	protected static final boolean COR_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isCor() <em>Cor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCor()
	 * @generated
	 * @ordered
	 */
	protected boolean cor = COR_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParameterCovCorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BfrPackage.Literals.PARAMETER_COV_COR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeschModellParameter getParameter1() {
		if (parameter1 != null && parameter1.eIsProxy()) {
			InternalEObject oldParameter1 = (InternalEObject)parameter1;
			parameter1 = (GeschModellParameter)eResolveProxy(oldParameter1);
			if (parameter1 != oldParameter1) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BfrPackage.PARAMETER_COV_COR__PARAMETER1, oldParameter1, parameter1));
			}
		}
		return parameter1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeschModellParameter basicGetParameter1() {
		return parameter1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameter1(GeschModellParameter newParameter1) {
		GeschModellParameter oldParameter1 = parameter1;
		parameter1 = newParameter1;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.PARAMETER_COV_COR__PARAMETER1, oldParameter1, parameter1));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeschModellParameter getParameter2() {
		if (parameter2 != null && parameter2.eIsProxy()) {
			InternalEObject oldParameter2 = (InternalEObject)parameter2;
			parameter2 = (GeschModellParameter)eResolveProxy(oldParameter2);
			if (parameter2 != oldParameter2) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BfrPackage.PARAMETER_COV_COR__PARAMETER2, oldParameter2, parameter2));
			}
		}
		return parameter2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeschModellParameter basicGetParameter2() {
		return parameter2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameter2(GeschModellParameter newParameter2) {
		GeschModellParameter oldParameter2 = parameter2;
		parameter2 = newParameter2;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.PARAMETER_COV_COR__PARAMETER2, oldParameter2, parameter2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(Double newValue) {
		Double oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.PARAMETER_COV_COR__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isCor() {
		return cor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCor(boolean newCor) {
		boolean oldCor = cor;
		cor = newCor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.PARAMETER_COV_COR__COR, oldCor, cor));
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
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.PARAMETER_COV_COR__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case BfrPackage.PARAMETER_COV_COR__PARAMETER1:
				if (resolve) return getParameter1();
				return basicGetParameter1();
			case BfrPackage.PARAMETER_COV_COR__PARAMETER2:
				if (resolve) return getParameter2();
				return basicGetParameter2();
			case BfrPackage.PARAMETER_COV_COR__VALUE:
				return getValue();
			case BfrPackage.PARAMETER_COV_COR__COR:
				return isCor();
			case BfrPackage.PARAMETER_COV_COR__ID:
				return getId();
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
			case BfrPackage.PARAMETER_COV_COR__PARAMETER1:
				setParameter1((GeschModellParameter)newValue);
				return;
			case BfrPackage.PARAMETER_COV_COR__PARAMETER2:
				setParameter2((GeschModellParameter)newValue);
				return;
			case BfrPackage.PARAMETER_COV_COR__VALUE:
				setValue((Double)newValue);
				return;
			case BfrPackage.PARAMETER_COV_COR__COR:
				setCor((Boolean)newValue);
				return;
			case BfrPackage.PARAMETER_COV_COR__ID:
				setId((Integer)newValue);
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
			case BfrPackage.PARAMETER_COV_COR__PARAMETER1:
				setParameter1((GeschModellParameter)null);
				return;
			case BfrPackage.PARAMETER_COV_COR__PARAMETER2:
				setParameter2((GeschModellParameter)null);
				return;
			case BfrPackage.PARAMETER_COV_COR__VALUE:
				setValue(VALUE_EDEFAULT);
				return;
			case BfrPackage.PARAMETER_COV_COR__COR:
				setCor(COR_EDEFAULT);
				return;
			case BfrPackage.PARAMETER_COV_COR__ID:
				setId(ID_EDEFAULT);
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
			case BfrPackage.PARAMETER_COV_COR__PARAMETER1:
				return parameter1 != null;
			case BfrPackage.PARAMETER_COV_COR__PARAMETER2:
				return parameter2 != null;
			case BfrPackage.PARAMETER_COV_COR__VALUE:
				return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
			case BfrPackage.PARAMETER_COV_COR__COR:
				return cor != COR_EDEFAULT;
			case BfrPackage.PARAMETER_COV_COR__ID:
				return id != ID_EDEFAULT;
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
		result.append(" (value: ");
		result.append(value);
		result.append(", cor: ");
		result.append(cor);
		result.append(", id: ");
		result.append(id);
		result.append(')');
		return result.toString();
	}

} //ParameterCovCorImpl
