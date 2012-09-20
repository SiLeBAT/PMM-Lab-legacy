/**
 * $Id: LiteraturListeImpl.java 651 2012-01-24 09:59:12Z sdoerl $
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
import de.dim.bfr.Literatur;
import de.dim.bfr.LiteraturListe;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Literatur Liste</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.dim.bfr.impl.LiteraturListeImpl#getLiteratur <em>Literatur</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LiteraturListeImpl extends EObjectImpl implements LiteraturListe {
	/**
	 * The cached value of the '{@link #getLiteratur() <em>Literatur</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLiteratur()
	 * @generated
	 * @ordered
	 */
	protected EList<Literatur> literatur;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LiteraturListeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BfrPackage.Literals.LITERATUR_LISTE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Literatur> getLiteratur() {
		if (literatur == null) {
			literatur = new EObjectContainmentEList<Literatur>(Literatur.class, this, BfrPackage.LITERATUR_LISTE__LITERATUR);
		}
		return literatur;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case BfrPackage.LITERATUR_LISTE__LITERATUR:
				return ((InternalEList<?>)getLiteratur()).basicRemove(otherEnd, msgs);
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
			case BfrPackage.LITERATUR_LISTE__LITERATUR:
				return getLiteratur();
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
			case BfrPackage.LITERATUR_LISTE__LITERATUR:
				getLiteratur().clear();
				getLiteratur().addAll((Collection<? extends Literatur>)newValue);
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
			case BfrPackage.LITERATUR_LISTE__LITERATUR:
				getLiteratur().clear();
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
			case BfrPackage.LITERATUR_LISTE__LITERATUR:
				return literatur != null && !literatur.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //LiteraturListeImpl
