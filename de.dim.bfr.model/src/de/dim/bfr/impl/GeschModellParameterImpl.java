/**
 * $Id: GeschModellParameterImpl.java 651 2012-01-24 09:59:12Z sdoerl $
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
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.VersuchsBedingung;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Gesch Modell Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.dim.bfr.impl.GeschModellParameterImpl#getName <em>Name</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschModellParameterImpl#getSd <em>Sd</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschModellParameterImpl#getWert <em>Wert</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschModellParameterImpl#getVersuchsBedingung <em>Versuchs Bedingung</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschModellParameterImpl#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschModellParameterImpl#getT <em>T</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschModellParameterImpl#getP <em>P</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschModellParameterImpl#getGeschaetztesModell <em>Geschaetztes Modell</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschModellParameterImpl#getModelParameter <em>Model Parameter</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschModellParameterImpl#getKiOben <em>Ki Oben</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschModellParameterImpl#getKiUnten <em>Ki Unten</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GeschModellParameterImpl extends EObjectImpl implements GeschModellParameter {
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
	 * The default value of the '{@link #getSd() <em>Sd</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSd()
	 * @generated
	 * @ordered
	 */
	protected static final Double SD_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSd() <em>Sd</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSd()
	 * @generated
	 * @ordered
	 */
	protected Double sd = SD_EDEFAULT;

	/**
	 * The default value of the '{@link #getWert() <em>Wert</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWert()
	 * @generated
	 * @ordered
	 */
	protected static final Double WERT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWert() <em>Wert</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWert()
	 * @generated
	 * @ordered
	 */
	protected Double wert = WERT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVersuchsBedingung() <em>Versuchs Bedingung</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersuchsBedingung()
	 * @generated
	 * @ordered
	 */
	protected VersuchsBedingung versuchsBedingung;

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
	 * The default value of the '{@link #getT() <em>T</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getT()
	 * @generated
	 * @ordered
	 */
	protected static final Double T_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getT() <em>T</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getT()
	 * @generated
	 * @ordered
	 */
	protected Double t = T_EDEFAULT;

	/**
	 * The default value of the '{@link #getP() <em>P</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getP()
	 * @generated
	 * @ordered
	 */
	protected static final Double P_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getP() <em>P</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getP()
	 * @generated
	 * @ordered
	 */
	protected Double p = P_EDEFAULT;

	/**
	 * The cached value of the '{@link #getGeschaetztesModell() <em>Geschaetztes Modell</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeschaetztesModell()
	 * @generated
	 * @ordered
	 */
	protected GeschaetztStatistikModell geschaetztesModell;

	/**
	 * The cached value of the '{@link #getModelParameter() <em>Model Parameter</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelParameter()
	 * @generated
	 * @ordered
	 */
	protected StatistikModellParameter modelParameter;

	/**
	 * The default value of the '{@link #getKiOben() <em>Ki Oben</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKiOben()
	 * @generated
	 * @ordered
	 */
	protected static final double KI_OBEN_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getKiOben() <em>Ki Oben</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKiOben()
	 * @generated
	 * @ordered
	 */
	protected double kiOben = KI_OBEN_EDEFAULT;

	/**
	 * The default value of the '{@link #getKiUnten() <em>Ki Unten</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKiUnten()
	 * @generated
	 * @ordered
	 */
	protected static final double KI_UNTEN_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getKiUnten() <em>Ki Unten</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKiUnten()
	 * @generated
	 * @ordered
	 */
	protected double kiUnten = KI_UNTEN_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GeschModellParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BfrPackage.Literals.GESCH_MODELL_PARAMETER;
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
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCH_MODELL_PARAMETER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Double getSd() {
		return sd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSd(Double newSd) {
		Double oldSd = sd;
		sd = newSd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCH_MODELL_PARAMETER__SD, oldSd, sd));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Double getWert() {
		return wert;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWert(Double newWert) {
		Double oldWert = wert;
		wert = newWert;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCH_MODELL_PARAMETER__WERT, oldWert, wert));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VersuchsBedingung getVersuchsBedingung() {
		if (versuchsBedingung != null && versuchsBedingung.eIsProxy()) {
			InternalEObject oldVersuchsBedingung = (InternalEObject)versuchsBedingung;
			versuchsBedingung = (VersuchsBedingung)eResolveProxy(oldVersuchsBedingung);
			if (versuchsBedingung != oldVersuchsBedingung) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BfrPackage.GESCH_MODELL_PARAMETER__VERSUCHS_BEDINGUNG, oldVersuchsBedingung, versuchsBedingung));
			}
		}
		return versuchsBedingung;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VersuchsBedingung basicGetVersuchsBedingung() {
		return versuchsBedingung;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersuchsBedingung(VersuchsBedingung newVersuchsBedingung) {
		VersuchsBedingung oldVersuchsBedingung = versuchsBedingung;
		versuchsBedingung = newVersuchsBedingung;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCH_MODELL_PARAMETER__VERSUCHS_BEDINGUNG, oldVersuchsBedingung, versuchsBedingung));
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
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCH_MODELL_PARAMETER__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Double getT() {
		return t;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setT(Double newT) {
		Double oldT = t;
		t = newT;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCH_MODELL_PARAMETER__T, oldT, t));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Double getP() {
		return p;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setP(Double newP) {
		Double oldP = p;
		p = newP;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCH_MODELL_PARAMETER__P, oldP, p));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeschaetztStatistikModell getGeschaetztesModell() {
		if (geschaetztesModell != null && geschaetztesModell.eIsProxy()) {
			InternalEObject oldGeschaetztesModell = (InternalEObject)geschaetztesModell;
			geschaetztesModell = (GeschaetztStatistikModell)eResolveProxy(oldGeschaetztesModell);
			if (geschaetztesModell != oldGeschaetztesModell) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BfrPackage.GESCH_MODELL_PARAMETER__GESCHAETZTES_MODELL, oldGeschaetztesModell, geschaetztesModell));
			}
		}
		return geschaetztesModell;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeschaetztStatistikModell basicGetGeschaetztesModell() {
		return geschaetztesModell;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGeschaetztesModell(GeschaetztStatistikModell newGeschaetztesModell) {
		GeschaetztStatistikModell oldGeschaetztesModell = geschaetztesModell;
		geschaetztesModell = newGeschaetztesModell;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCH_MODELL_PARAMETER__GESCHAETZTES_MODELL, oldGeschaetztesModell, geschaetztesModell));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatistikModellParameter getModelParameter() {
		if (modelParameter != null && modelParameter.eIsProxy()) {
			InternalEObject oldModelParameter = (InternalEObject)modelParameter;
			modelParameter = (StatistikModellParameter)eResolveProxy(oldModelParameter);
			if (modelParameter != oldModelParameter) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BfrPackage.GESCH_MODELL_PARAMETER__MODEL_PARAMETER, oldModelParameter, modelParameter));
			}
		}
		return modelParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatistikModellParameter basicGetModelParameter() {
		return modelParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModelParameter(StatistikModellParameter newModelParameter) {
		StatistikModellParameter oldModelParameter = modelParameter;
		modelParameter = newModelParameter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCH_MODELL_PARAMETER__MODEL_PARAMETER, oldModelParameter, modelParameter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getKiOben() {
		return kiOben;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKiOben(double newKiOben) {
		double oldKiOben = kiOben;
		kiOben = newKiOben;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCH_MODELL_PARAMETER__KI_OBEN, oldKiOben, kiOben));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getKiUnten() {
		return kiUnten;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKiUnten(double newKiUnten) {
		double oldKiUnten = kiUnten;
		kiUnten = newKiUnten;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCH_MODELL_PARAMETER__KI_UNTEN, oldKiUnten, kiUnten));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case BfrPackage.GESCH_MODELL_PARAMETER__NAME:
				return getName();
			case BfrPackage.GESCH_MODELL_PARAMETER__SD:
				return getSd();
			case BfrPackage.GESCH_MODELL_PARAMETER__WERT:
				return getWert();
			case BfrPackage.GESCH_MODELL_PARAMETER__VERSUCHS_BEDINGUNG:
				if (resolve) return getVersuchsBedingung();
				return basicGetVersuchsBedingung();
			case BfrPackage.GESCH_MODELL_PARAMETER__ID:
				return getId();
			case BfrPackage.GESCH_MODELL_PARAMETER__T:
				return getT();
			case BfrPackage.GESCH_MODELL_PARAMETER__P:
				return getP();
			case BfrPackage.GESCH_MODELL_PARAMETER__GESCHAETZTES_MODELL:
				if (resolve) return getGeschaetztesModell();
				return basicGetGeschaetztesModell();
			case BfrPackage.GESCH_MODELL_PARAMETER__MODEL_PARAMETER:
				if (resolve) return getModelParameter();
				return basicGetModelParameter();
			case BfrPackage.GESCH_MODELL_PARAMETER__KI_OBEN:
				return getKiOben();
			case BfrPackage.GESCH_MODELL_PARAMETER__KI_UNTEN:
				return getKiUnten();
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
			case BfrPackage.GESCH_MODELL_PARAMETER__NAME:
				setName((String)newValue);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__SD:
				setSd((Double)newValue);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__WERT:
				setWert((Double)newValue);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__VERSUCHS_BEDINGUNG:
				setVersuchsBedingung((VersuchsBedingung)newValue);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__ID:
				setId((Integer)newValue);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__T:
				setT((Double)newValue);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__P:
				setP((Double)newValue);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__GESCHAETZTES_MODELL:
				setGeschaetztesModell((GeschaetztStatistikModell)newValue);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__MODEL_PARAMETER:
				setModelParameter((StatistikModellParameter)newValue);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__KI_OBEN:
				setKiOben((Double)newValue);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__KI_UNTEN:
				setKiUnten((Double)newValue);
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
			case BfrPackage.GESCH_MODELL_PARAMETER__NAME:
				setName(NAME_EDEFAULT);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__SD:
				setSd(SD_EDEFAULT);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__WERT:
				setWert(WERT_EDEFAULT);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__VERSUCHS_BEDINGUNG:
				setVersuchsBedingung((VersuchsBedingung)null);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__ID:
				setId(ID_EDEFAULT);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__T:
				setT(T_EDEFAULT);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__P:
				setP(P_EDEFAULT);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__GESCHAETZTES_MODELL:
				setGeschaetztesModell((GeschaetztStatistikModell)null);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__MODEL_PARAMETER:
				setModelParameter((StatistikModellParameter)null);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__KI_OBEN:
				setKiOben(KI_OBEN_EDEFAULT);
				return;
			case BfrPackage.GESCH_MODELL_PARAMETER__KI_UNTEN:
				setKiUnten(KI_UNTEN_EDEFAULT);
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
			case BfrPackage.GESCH_MODELL_PARAMETER__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case BfrPackage.GESCH_MODELL_PARAMETER__SD:
				return SD_EDEFAULT == null ? sd != null : !SD_EDEFAULT.equals(sd);
			case BfrPackage.GESCH_MODELL_PARAMETER__WERT:
				return WERT_EDEFAULT == null ? wert != null : !WERT_EDEFAULT.equals(wert);
			case BfrPackage.GESCH_MODELL_PARAMETER__VERSUCHS_BEDINGUNG:
				return versuchsBedingung != null;
			case BfrPackage.GESCH_MODELL_PARAMETER__ID:
				return id != ID_EDEFAULT;
			case BfrPackage.GESCH_MODELL_PARAMETER__T:
				return T_EDEFAULT == null ? t != null : !T_EDEFAULT.equals(t);
			case BfrPackage.GESCH_MODELL_PARAMETER__P:
				return P_EDEFAULT == null ? p != null : !P_EDEFAULT.equals(p);
			case BfrPackage.GESCH_MODELL_PARAMETER__GESCHAETZTES_MODELL:
				return geschaetztesModell != null;
			case BfrPackage.GESCH_MODELL_PARAMETER__MODEL_PARAMETER:
				return modelParameter != null;
			case BfrPackage.GESCH_MODELL_PARAMETER__KI_OBEN:
				return kiOben != KI_OBEN_EDEFAULT;
			case BfrPackage.GESCH_MODELL_PARAMETER__KI_UNTEN:
				return kiUnten != KI_UNTEN_EDEFAULT;
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
		result.append(" (name: ");
		result.append(name);
		result.append(", sd: ");
		result.append(sd);
		result.append(", wert: ");
		result.append(wert);
		result.append(", id: ");
		result.append(id);
		result.append(", t: ");
		result.append(t);
		result.append(", p: ");
		result.append(p);
		result.append(", kiOben: ");
		result.append(kiOben);
		result.append(", kiUnten: ");
		result.append(kiUnten);
		result.append(')');
		return result.toString();
	}

} //GeschModellParameterImpl
