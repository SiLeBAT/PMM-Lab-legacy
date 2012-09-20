/**
 * $Id: VersuchsBedingungListImpl.java 651 2012-01-24 09:59:12Z sdoerl $
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

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import de.dim.bfr.BfrPackage;
import de.dim.bfr.VersuchsBedingung;
import de.dim.bfr.VersuchsBedingungList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Versuchs Bedingung List</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.dim.bfr.impl.VersuchsBedingungListImpl#getBedingungen <em>Bedingungen</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VersuchsBedingungListImpl extends EObjectImpl implements VersuchsBedingungList {
	/**
	 * The cached value of the '{@link #getBedingungen() <em>Bedingungen</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBedingungen()
	 * @generated
	 * @ordered
	 */
	protected EList<VersuchsBedingung> bedingungen;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VersuchsBedingungListImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BfrPackage.Literals.VERSUCHS_BEDINGUNG_LIST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VersuchsBedingung> getBedingungen() {
		if (bedingungen == null) {
			bedingungen = new EObjectContainmentEList<VersuchsBedingung>(VersuchsBedingung.class, this, BfrPackage.VERSUCHS_BEDINGUNG_LIST__BEDINGUNGEN);
		}
		return bedingungen;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case BfrPackage.VERSUCHS_BEDINGUNG_LIST__BEDINGUNGEN:
				return ((InternalEList<?>)getBedingungen()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case BfrPackage.VERSUCHS_BEDINGUNG_LIST__BEDINGUNGEN:
				return getBedingungen();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case BfrPackage.VERSUCHS_BEDINGUNG_LIST__BEDINGUNGEN:
				getBedingungen().clear();
				getBedingungen().addAll((Collection<? extends VersuchsBedingung>)newValue);
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
			case BfrPackage.VERSUCHS_BEDINGUNG_LIST__BEDINGUNGEN:
				getBedingungen().clear();
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
			case BfrPackage.VERSUCHS_BEDINGUNG_LIST__BEDINGUNGEN:
				return bedingungen != null && !bedingungen.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //VersuchsBedingungListImpl
