/**
 * $Id: StatistikModellParameterImpl.java 651 2012-01-24 09:59:12Z sdoerl $
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
import de.dim.bfr.ParameterRoleType;
import de.dim.bfr.StatistikModellParameter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Statistik Modell Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.dim.bfr.impl.StatistikModellParameterImpl#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellParameterImpl#getName <em>Name</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellParameterImpl#getMin <em>Min</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellParameterImpl#getMax <em>Max</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellParameterImpl#getBeschreibung <em>Beschreibung</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellParameterImpl#getRole <em>Role</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellParameterImpl#isInteger <em>Integer</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StatistikModellParameterImpl extends EObjectImpl implements StatistikModellParameter {
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
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getMin() <em>Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMin()
	 * @generated
	 * @ordered
	 */
	protected static final Double MIN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMin() <em>Min</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMin()
	 * @generated
	 * @ordered
	 */
	protected Double min = MIN_EDEFAULT;

	/**
	 * The default value of the '{@link #getMax() <em>Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMax()
	 * @generated
	 * @ordered
	 */
	protected static final Double MAX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMax() <em>Max</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMax()
	 * @generated
	 * @ordered
	 */
	protected Double max = MAX_EDEFAULT;

	/**
	 * The default value of the '{@link #getBeschreibung() <em>Beschreibung</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeschreibung()
	 * @generated
	 * @ordered
	 */
	protected static final String BESCHREIBUNG_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBeschreibung() <em>Beschreibung</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeschreibung()
	 * @generated
	 * @ordered
	 */
	protected String beschreibung = BESCHREIBUNG_EDEFAULT;

	/**
	 * The default value of the '{@link #getRole() <em>Role</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRole()
	 * @generated
	 * @ordered
	 */
	protected static final ParameterRoleType ROLE_EDEFAULT = ParameterRoleType.NONE;

	/**
	 * The cached value of the '{@link #getRole() <em>Role</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRole()
	 * @generated
	 * @ordered
	 */
	protected ParameterRoleType role = ROLE_EDEFAULT;

	/**
	 * The default value of the '{@link #isInteger() <em>Integer</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isInteger()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INTEGER_EDEFAULT = false;  // old Statup, lieber Default zu false!

	/**
	 * The cached value of the '{@link #isInteger() <em>Integer</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isInteger()
	 * @generated
	 * @ordered
	 */
	protected boolean integer = INTEGER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StatistikModellParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BfrPackage.Literals.STATISTIK_MODELL_PARAMETER;
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
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL_PARAMETER__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL_PARAMETER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Double getMin() {
		return min;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMin(Double newMin) {
		Double oldMin = min;
		min = newMin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL_PARAMETER__MIN, oldMin, min));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Double getMax() {
		return max;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMax(Double newMax) {
		Double oldMax = max;
		max = newMax;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL_PARAMETER__MAX, oldMax, max));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBeschreibung() {
		return beschreibung;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeschreibung(String newBeschreibung) {
		String oldBeschreibung = beschreibung;
		beschreibung = newBeschreibung;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL_PARAMETER__BESCHREIBUNG, oldBeschreibung, beschreibung));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterRoleType getRole() {
		return role;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRole(ParameterRoleType newRole) {
		ParameterRoleType oldRole = role;
		role = newRole == null ? ROLE_EDEFAULT : newRole;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL_PARAMETER__ROLE, oldRole, role));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isInteger() {
		return integer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInteger(boolean newInteger) {
		boolean oldInteger = integer;
		integer = newInteger;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL_PARAMETER__INTEGER, oldInteger, integer));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case BfrPackage.STATISTIK_MODELL_PARAMETER__ID:
				return getId();
			case BfrPackage.STATISTIK_MODELL_PARAMETER__NAME:
				return getName();
			case BfrPackage.STATISTIK_MODELL_PARAMETER__MIN:
				return getMin();
			case BfrPackage.STATISTIK_MODELL_PARAMETER__MAX:
				return getMax();
			case BfrPackage.STATISTIK_MODELL_PARAMETER__BESCHREIBUNG:
				return getBeschreibung();
			case BfrPackage.STATISTIK_MODELL_PARAMETER__ROLE:
				return getRole();
			case BfrPackage.STATISTIK_MODELL_PARAMETER__INTEGER:
				return isInteger();
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
			case BfrPackage.STATISTIK_MODELL_PARAMETER__ID:
				setId((Integer)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL_PARAMETER__NAME:
				setName((String)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL_PARAMETER__MIN:
				setMin((Double)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL_PARAMETER__MAX:
				setMax((Double)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL_PARAMETER__BESCHREIBUNG:
				setBeschreibung((String)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL_PARAMETER__ROLE:
				setRole((ParameterRoleType)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL_PARAMETER__INTEGER:
				setInteger((Boolean)newValue);
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
			case BfrPackage.STATISTIK_MODELL_PARAMETER__ID:
				setId(ID_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL_PARAMETER__NAME:
				setName(NAME_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL_PARAMETER__MIN:
				setMin(MIN_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL_PARAMETER__MAX:
				setMax(MAX_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL_PARAMETER__BESCHREIBUNG:
				setBeschreibung(BESCHREIBUNG_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL_PARAMETER__ROLE:
				setRole(ROLE_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL_PARAMETER__INTEGER:
				setInteger(INTEGER_EDEFAULT);
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
			case BfrPackage.STATISTIK_MODELL_PARAMETER__ID:
				return id != ID_EDEFAULT;
			case BfrPackage.STATISTIK_MODELL_PARAMETER__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case BfrPackage.STATISTIK_MODELL_PARAMETER__MIN:
				return MIN_EDEFAULT == null ? min != null : !MIN_EDEFAULT.equals(min);
			case BfrPackage.STATISTIK_MODELL_PARAMETER__MAX:
				return MAX_EDEFAULT == null ? max != null : !MAX_EDEFAULT.equals(max);
			case BfrPackage.STATISTIK_MODELL_PARAMETER__BESCHREIBUNG:
				return BESCHREIBUNG_EDEFAULT == null ? beschreibung != null : !BESCHREIBUNG_EDEFAULT.equals(beschreibung);
			case BfrPackage.STATISTIK_MODELL_PARAMETER__ROLE:
				return role != ROLE_EDEFAULT;
			case BfrPackage.STATISTIK_MODELL_PARAMETER__INTEGER:
				return integer != INTEGER_EDEFAULT;
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
		result.append(", name: ");
		result.append(name);
		result.append(", min: ");
		result.append(min);
		result.append(", max: ");
		result.append(max);
		result.append(", beschreibung: ");
		result.append(beschreibung);
		result.append(", role: ");
		result.append(role);
		result.append(", integer: ");
		result.append(integer);
		result.append(')');
		return result.toString();
	}

} //StatistikModellParameterImpl
