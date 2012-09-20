/**
 * $Id: MesswerteImpl.java 651 2012-01-24 09:59:12Z sdoerl $
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
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import de.dim.bfr.BfrPackage;
import de.dim.bfr.DoubleKennzahlen;
import de.dim.bfr.Einheiten;
import de.dim.bfr.Messwerte;
import de.dim.bfr.VersuchsBedingung;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Messwerte</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.dim.bfr.impl.MesswerteImpl#isGeprueft <em>Geprueft</em>}</li>
 *   <li>{@link de.dim.bfr.impl.MesswerteImpl#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.impl.MesswerteImpl#getKommentar <em>Kommentar</em>}</li>
 *   <li>{@link de.dim.bfr.impl.MesswerteImpl#getZeitEinheit <em>Zeit Einheit</em>}</li>
 *   <li>{@link de.dim.bfr.impl.MesswerteImpl#getAw <em>Aw</em>}</li>
 *   <li>{@link de.dim.bfr.impl.MesswerteImpl#getCo2 <em>Co2</em>}</li>
 *   <li>{@link de.dim.bfr.impl.MesswerteImpl#getDruck <em>Druck</em>}</li>
 *   <li>{@link de.dim.bfr.impl.MesswerteImpl#getTemperatur <em>Temperatur</em>}</li>
 *   <li>{@link de.dim.bfr.impl.MesswerteImpl#getPH <em>PH</em>}</li>
 *   <li>{@link de.dim.bfr.impl.MesswerteImpl#getKonzentration <em>Konzentration</em>}</li>
 *   <li>{@link de.dim.bfr.impl.MesswerteImpl#getZeit <em>Zeit</em>}</li>
 *   <li>{@link de.dim.bfr.impl.MesswerteImpl#getSonstiges <em>Sonstiges</em>}</li>
 *   <li>{@link de.dim.bfr.impl.MesswerteImpl#getVersuchsbedingungen <em>Versuchsbedingungen</em>}</li>
 *   <li>{@link de.dim.bfr.impl.MesswerteImpl#getKonzEinheit <em>Konz Einheit</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MesswerteImpl extends EObjectImpl implements Messwerte {
	/**
	 * The default value of the '{@link #isGeprueft() <em>Geprueft</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isGeprueft()
	 * @generated
	 * @ordered
	 */
	protected static final boolean GEPRUEFT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isGeprueft() <em>Geprueft</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isGeprueft()
	 * @generated
	 * @ordered
	 */
	protected boolean geprueft = GEPRUEFT_EDEFAULT;

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
	 * The default value of the '{@link #getKommentar() <em>Kommentar</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKommentar()
	 * @generated
	 * @ordered
	 */
	protected static final String KOMMENTAR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getKommentar() <em>Kommentar</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKommentar()
	 * @generated
	 * @ordered
	 */
	protected String kommentar = KOMMENTAR_EDEFAULT;

	/**
	 * The default value of the '{@link #getZeitEinheit() <em>Zeit Einheit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getZeitEinheit()
	 * @generated
	 * @ordered
	 */
	protected static final String ZEIT_EINHEIT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getZeitEinheit() <em>Zeit Einheit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getZeitEinheit()
	 * @generated
	 * @ordered
	 */
	protected String zeitEinheit = ZEIT_EINHEIT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAw() <em>Aw</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAw()
	 * @generated
	 * @ordered
	 */
	protected DoubleKennzahlen aw;

	/**
	 * The cached value of the '{@link #getCo2() <em>Co2</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCo2()
	 * @generated
	 * @ordered
	 */
	protected DoubleKennzahlen co2;

	/**
	 * The cached value of the '{@link #getDruck() <em>Druck</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDruck()
	 * @generated
	 * @ordered
	 */
	protected DoubleKennzahlen druck;

	/**
	 * The cached value of the '{@link #getTemperatur() <em>Temperatur</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemperatur()
	 * @generated
	 * @ordered
	 */
	protected DoubleKennzahlen temperatur;

	/**
	 * The cached value of the '{@link #getPH() <em>PH</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPH()
	 * @generated
	 * @ordered
	 */
	protected DoubleKennzahlen pH;

	/**
	 * The cached value of the '{@link #getKonzentration() <em>Konzentration</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKonzentration()
	 * @generated
	 * @ordered
	 */
	protected DoubleKennzahlen konzentration;

	/**
	 * The cached value of the '{@link #getZeit() <em>Zeit</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getZeit()
	 * @generated
	 * @ordered
	 */
	protected DoubleKennzahlen zeit;

	/**
	 * The cached value of the '{@link #getSonstiges() <em>Sonstiges</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSonstiges()
	 * @generated
	 * @ordered
	 */
	protected EObject sonstiges;

	/**
	 * The cached value of the '{@link #getVersuchsbedingungen() <em>Versuchsbedingungen</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersuchsbedingungen()
	 * @generated
	 * @ordered
	 */
	protected VersuchsBedingung versuchsbedingungen;

	/**
	 * The cached value of the '{@link #getKonzEinheit() <em>Konz Einheit</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKonzEinheit()
	 * @generated
	 * @ordered
	 */
	protected Einheiten konzEinheit;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MesswerteImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BfrPackage.Literals.MESSWERTE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isGeprueft() {
		return geprueft;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGeprueft(boolean newGeprueft) {
		boolean oldGeprueft = geprueft;
		geprueft = newGeprueft;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__GEPRUEFT, oldGeprueft, geprueft));
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
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getKommentar() {
		return kommentar;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKommentar(String newKommentar) {
		String oldKommentar = kommentar;
		kommentar = newKommentar;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__KOMMENTAR, oldKommentar, kommentar));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getZeitEinheit() {
		return zeitEinheit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setZeitEinheit(String newZeitEinheit) {
		String oldZeitEinheit = zeitEinheit;
		zeitEinheit = newZeitEinheit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__ZEIT_EINHEIT, oldZeitEinheit, zeitEinheit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DoubleKennzahlen getAw() {
		return aw;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAw(DoubleKennzahlen newAw, NotificationChain msgs) {
		DoubleKennzahlen oldAw = aw;
		aw = newAw;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__AW, oldAw, newAw);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAw(DoubleKennzahlen newAw) {
		if (newAw != aw) {
			NotificationChain msgs = null;
			if (aw != null)
				msgs = ((InternalEObject)aw).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__AW, null, msgs);
			if (newAw != null)
				msgs = ((InternalEObject)newAw).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__AW, null, msgs);
			msgs = basicSetAw(newAw, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__AW, newAw, newAw));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DoubleKennzahlen getCo2() {
		return co2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCo2(DoubleKennzahlen newCo2, NotificationChain msgs) {
		DoubleKennzahlen oldCo2 = co2;
		co2 = newCo2;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__CO2, oldCo2, newCo2);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCo2(DoubleKennzahlen newCo2) {
		if (newCo2 != co2) {
			NotificationChain msgs = null;
			if (co2 != null)
				msgs = ((InternalEObject)co2).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__CO2, null, msgs);
			if (newCo2 != null)
				msgs = ((InternalEObject)newCo2).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__CO2, null, msgs);
			msgs = basicSetCo2(newCo2, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__CO2, newCo2, newCo2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DoubleKennzahlen getDruck() {
		return druck;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDruck(DoubleKennzahlen newDruck, NotificationChain msgs) {
		DoubleKennzahlen oldDruck = druck;
		druck = newDruck;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__DRUCK, oldDruck, newDruck);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDruck(DoubleKennzahlen newDruck) {
		if (newDruck != druck) {
			NotificationChain msgs = null;
			if (druck != null)
				msgs = ((InternalEObject)druck).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__DRUCK, null, msgs);
			if (newDruck != null)
				msgs = ((InternalEObject)newDruck).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__DRUCK, null, msgs);
			msgs = basicSetDruck(newDruck, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__DRUCK, newDruck, newDruck));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DoubleKennzahlen getTemperatur() {
		return temperatur;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTemperatur(DoubleKennzahlen newTemperatur, NotificationChain msgs) {
		DoubleKennzahlen oldTemperatur = temperatur;
		temperatur = newTemperatur;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__TEMPERATUR, oldTemperatur, newTemperatur);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTemperatur(DoubleKennzahlen newTemperatur) {
		if (newTemperatur != temperatur) {
			NotificationChain msgs = null;
			if (temperatur != null)
				msgs = ((InternalEObject)temperatur).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__TEMPERATUR, null, msgs);
			if (newTemperatur != null)
				msgs = ((InternalEObject)newTemperatur).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__TEMPERATUR, null, msgs);
			msgs = basicSetTemperatur(newTemperatur, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__TEMPERATUR, newTemperatur, newTemperatur));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DoubleKennzahlen getPH() {
		return pH;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPH(DoubleKennzahlen newPH, NotificationChain msgs) {
		DoubleKennzahlen oldPH = pH;
		pH = newPH;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__PH, oldPH, newPH);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPH(DoubleKennzahlen newPH) {
		if (newPH != pH) {
			NotificationChain msgs = null;
			if (pH != null)
				msgs = ((InternalEObject)pH).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__PH, null, msgs);
			if (newPH != null)
				msgs = ((InternalEObject)newPH).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__PH, null, msgs);
			msgs = basicSetPH(newPH, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__PH, newPH, newPH));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DoubleKennzahlen getKonzentration() {
		return konzentration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetKonzentration(DoubleKennzahlen newKonzentration, NotificationChain msgs) {
		DoubleKennzahlen oldKonzentration = konzentration;
		konzentration = newKonzentration;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__KONZENTRATION, oldKonzentration, newKonzentration);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKonzentration(DoubleKennzahlen newKonzentration) {
		if (newKonzentration != konzentration) {
			NotificationChain msgs = null;
			if (konzentration != null)
				msgs = ((InternalEObject)konzentration).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__KONZENTRATION, null, msgs);
			if (newKonzentration != null)
				msgs = ((InternalEObject)newKonzentration).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__KONZENTRATION, null, msgs);
			msgs = basicSetKonzentration(newKonzentration, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__KONZENTRATION, newKonzentration, newKonzentration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DoubleKennzahlen getZeit() {
		return zeit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetZeit(DoubleKennzahlen newZeit, NotificationChain msgs) {
		DoubleKennzahlen oldZeit = zeit;
		zeit = newZeit;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__ZEIT, oldZeit, newZeit);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setZeit(DoubleKennzahlen newZeit) {
		if (newZeit != zeit) {
			NotificationChain msgs = null;
			if (zeit != null)
				msgs = ((InternalEObject)zeit).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__ZEIT, null, msgs);
			if (newZeit != null)
				msgs = ((InternalEObject)newZeit).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__ZEIT, null, msgs);
			msgs = basicSetZeit(newZeit, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__ZEIT, newZeit, newZeit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getSonstiges() {
		if (sonstiges != null && sonstiges.eIsProxy()) {
			InternalEObject oldSonstiges = (InternalEObject)sonstiges;
			sonstiges = eResolveProxy(oldSonstiges);
			if (sonstiges != oldSonstiges) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BfrPackage.MESSWERTE__SONSTIGES, oldSonstiges, sonstiges));
			}
		}
		return sonstiges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetSonstiges() {
		return sonstiges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSonstiges(EObject newSonstiges) {
		EObject oldSonstiges = sonstiges;
		sonstiges = newSonstiges;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__SONSTIGES, oldSonstiges, sonstiges));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VersuchsBedingung getVersuchsbedingungen() {
		if (versuchsbedingungen != null && versuchsbedingungen.eIsProxy()) {
			InternalEObject oldVersuchsbedingungen = (InternalEObject)versuchsbedingungen;
			versuchsbedingungen = (VersuchsBedingung)eResolveProxy(oldVersuchsbedingungen);
			if (versuchsbedingungen != oldVersuchsbedingungen) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BfrPackage.MESSWERTE__VERSUCHSBEDINGUNGEN, oldVersuchsbedingungen, versuchsbedingungen));
			}
		}
		return versuchsbedingungen;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VersuchsBedingung basicGetVersuchsbedingungen() {
		return versuchsbedingungen;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersuchsbedingungen(VersuchsBedingung newVersuchsbedingungen) {
		VersuchsBedingung oldVersuchsbedingungen = versuchsbedingungen;
		versuchsbedingungen = newVersuchsbedingungen;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__VERSUCHSBEDINGUNGEN, oldVersuchsbedingungen, versuchsbedingungen));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Einheiten getKonzEinheit() {
		return konzEinheit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetKonzEinheit(Einheiten newKonzEinheit, NotificationChain msgs) {
		Einheiten oldKonzEinheit = konzEinheit;
		konzEinheit = newKonzEinheit;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__KONZ_EINHEIT, oldKonzEinheit, newKonzEinheit);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKonzEinheit(Einheiten newKonzEinheit) {
		if (newKonzEinheit != konzEinheit) {
			NotificationChain msgs = null;
			if (konzEinheit != null)
				msgs = ((InternalEObject)konzEinheit).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__KONZ_EINHEIT, null, msgs);
			if (newKonzEinheit != null)
				msgs = ((InternalEObject)newKonzEinheit).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BfrPackage.MESSWERTE__KONZ_EINHEIT, null, msgs);
			msgs = basicSetKonzEinheit(newKonzEinheit, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.MESSWERTE__KONZ_EINHEIT, newKonzEinheit, newKonzEinheit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case BfrPackage.MESSWERTE__AW:
				return basicSetAw(null, msgs);
			case BfrPackage.MESSWERTE__CO2:
				return basicSetCo2(null, msgs);
			case BfrPackage.MESSWERTE__DRUCK:
				return basicSetDruck(null, msgs);
			case BfrPackage.MESSWERTE__TEMPERATUR:
				return basicSetTemperatur(null, msgs);
			case BfrPackage.MESSWERTE__PH:
				return basicSetPH(null, msgs);
			case BfrPackage.MESSWERTE__KONZENTRATION:
				return basicSetKonzentration(null, msgs);
			case BfrPackage.MESSWERTE__ZEIT:
				return basicSetZeit(null, msgs);
			case BfrPackage.MESSWERTE__KONZ_EINHEIT:
				return basicSetKonzEinheit(null, msgs);
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
			case BfrPackage.MESSWERTE__GEPRUEFT:
				return isGeprueft();
			case BfrPackage.MESSWERTE__ID:
				return getId();
			case BfrPackage.MESSWERTE__KOMMENTAR:
				return getKommentar();
			case BfrPackage.MESSWERTE__ZEIT_EINHEIT:
				return getZeitEinheit();
			case BfrPackage.MESSWERTE__AW:
				return getAw();
			case BfrPackage.MESSWERTE__CO2:
				return getCo2();
			case BfrPackage.MESSWERTE__DRUCK:
				return getDruck();
			case BfrPackage.MESSWERTE__TEMPERATUR:
				return getTemperatur();
			case BfrPackage.MESSWERTE__PH:
				return getPH();
			case BfrPackage.MESSWERTE__KONZENTRATION:
				return getKonzentration();
			case BfrPackage.MESSWERTE__ZEIT:
				return getZeit();
			case BfrPackage.MESSWERTE__SONSTIGES:
				if (resolve) return getSonstiges();
				return basicGetSonstiges();
			case BfrPackage.MESSWERTE__VERSUCHSBEDINGUNGEN:
				if (resolve) return getVersuchsbedingungen();
				return basicGetVersuchsbedingungen();
			case BfrPackage.MESSWERTE__KONZ_EINHEIT:
				return getKonzEinheit();
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
			case BfrPackage.MESSWERTE__GEPRUEFT:
				setGeprueft((Boolean)newValue);
				return;
			case BfrPackage.MESSWERTE__ID:
				setId((Integer)newValue);
				return;
			case BfrPackage.MESSWERTE__KOMMENTAR:
				setKommentar((String)newValue);
				return;
			case BfrPackage.MESSWERTE__ZEIT_EINHEIT:
				setZeitEinheit((String)newValue);
				return;
			case BfrPackage.MESSWERTE__AW:
				setAw((DoubleKennzahlen)newValue);
				return;
			case BfrPackage.MESSWERTE__CO2:
				setCo2((DoubleKennzahlen)newValue);
				return;
			case BfrPackage.MESSWERTE__DRUCK:
				setDruck((DoubleKennzahlen)newValue);
				return;
			case BfrPackage.MESSWERTE__TEMPERATUR:
				setTemperatur((DoubleKennzahlen)newValue);
				return;
			case BfrPackage.MESSWERTE__PH:
				setPH((DoubleKennzahlen)newValue);
				return;
			case BfrPackage.MESSWERTE__KONZENTRATION:
				setKonzentration((DoubleKennzahlen)newValue);
				return;
			case BfrPackage.MESSWERTE__ZEIT:
				setZeit((DoubleKennzahlen)newValue);
				return;
			case BfrPackage.MESSWERTE__SONSTIGES:
				setSonstiges((EObject)newValue);
				return;
			case BfrPackage.MESSWERTE__VERSUCHSBEDINGUNGEN:
				setVersuchsbedingungen((VersuchsBedingung)newValue);
				return;
			case BfrPackage.MESSWERTE__KONZ_EINHEIT:
				setKonzEinheit((Einheiten)newValue);
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
			case BfrPackage.MESSWERTE__GEPRUEFT:
				setGeprueft(GEPRUEFT_EDEFAULT);
				return;
			case BfrPackage.MESSWERTE__ID:
				setId(ID_EDEFAULT);
				return;
			case BfrPackage.MESSWERTE__KOMMENTAR:
				setKommentar(KOMMENTAR_EDEFAULT);
				return;
			case BfrPackage.MESSWERTE__ZEIT_EINHEIT:
				setZeitEinheit(ZEIT_EINHEIT_EDEFAULT);
				return;
			case BfrPackage.MESSWERTE__AW:
				setAw((DoubleKennzahlen)null);
				return;
			case BfrPackage.MESSWERTE__CO2:
				setCo2((DoubleKennzahlen)null);
				return;
			case BfrPackage.MESSWERTE__DRUCK:
				setDruck((DoubleKennzahlen)null);
				return;
			case BfrPackage.MESSWERTE__TEMPERATUR:
				setTemperatur((DoubleKennzahlen)null);
				return;
			case BfrPackage.MESSWERTE__PH:
				setPH((DoubleKennzahlen)null);
				return;
			case BfrPackage.MESSWERTE__KONZENTRATION:
				setKonzentration((DoubleKennzahlen)null);
				return;
			case BfrPackage.MESSWERTE__ZEIT:
				setZeit((DoubleKennzahlen)null);
				return;
			case BfrPackage.MESSWERTE__SONSTIGES:
				setSonstiges((EObject)null);
				return;
			case BfrPackage.MESSWERTE__VERSUCHSBEDINGUNGEN:
				setVersuchsbedingungen((VersuchsBedingung)null);
				return;
			case BfrPackage.MESSWERTE__KONZ_EINHEIT:
				setKonzEinheit((Einheiten)null);
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
			case BfrPackage.MESSWERTE__GEPRUEFT:
				return geprueft != GEPRUEFT_EDEFAULT;
			case BfrPackage.MESSWERTE__ID:
				return id != ID_EDEFAULT;
			case BfrPackage.MESSWERTE__KOMMENTAR:
				return KOMMENTAR_EDEFAULT == null ? kommentar != null : !KOMMENTAR_EDEFAULT.equals(kommentar);
			case BfrPackage.MESSWERTE__ZEIT_EINHEIT:
				return ZEIT_EINHEIT_EDEFAULT == null ? zeitEinheit != null : !ZEIT_EINHEIT_EDEFAULT.equals(zeitEinheit);
			case BfrPackage.MESSWERTE__AW:
				return aw != null;
			case BfrPackage.MESSWERTE__CO2:
				return co2 != null;
			case BfrPackage.MESSWERTE__DRUCK:
				return druck != null;
			case BfrPackage.MESSWERTE__TEMPERATUR:
				return temperatur != null;
			case BfrPackage.MESSWERTE__PH:
				return pH != null;
			case BfrPackage.MESSWERTE__KONZENTRATION:
				return konzentration != null;
			case BfrPackage.MESSWERTE__ZEIT:
				return zeit != null;
			case BfrPackage.MESSWERTE__SONSTIGES:
				return sonstiges != null;
			case BfrPackage.MESSWERTE__VERSUCHSBEDINGUNGEN:
				return versuchsbedingungen != null;
			case BfrPackage.MESSWERTE__KONZ_EINHEIT:
				return konzEinheit != null;
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
		result.append(" (geprueft: ");
		result.append(geprueft);
		result.append(", id: ");
		result.append(id);
		result.append(", kommentar: ");
		result.append(kommentar);
		result.append(", zeitEinheit: ");
		result.append(zeitEinheit);
		result.append(')');
		return result.toString();
	}

} //MesswerteImpl
