/**
 * $Id: StatistikModellImpl.java 651 2012-01-24 09:59:12Z sdoerl $
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
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import de.dim.bfr.BfrPackage;
import de.dim.bfr.KlasseTyp;
import de.dim.bfr.LevelTyp;
import de.dim.bfr.Literatur;
import de.dim.bfr.SoftwareType;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellParameter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Statistik Modell</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.dim.bfr.impl.StatistikModellImpl#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellImpl#getName <em>Name</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellImpl#getNotation <em>Notation</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellImpl#getLevel <em>Level</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellImpl#getKlasse <em>Klasse</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellImpl#getTyp <em>Typ</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellImpl#getEingabedatum <em>Eingabedatum</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellImpl#getBenutzer <em>Benutzer</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellImpl#getBeschreibung <em>Beschreibung</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellImpl#getFormel <em>Formel</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellImpl#getSoftware <em>Software</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellImpl#getKommentar <em>Kommentar</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link de.dim.bfr.impl.StatistikModellImpl#getLiteratur <em>Literatur</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class StatistikModellImpl extends EObjectImpl implements StatistikModell {
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
	protected static final String NAME_EDEFAULT = "";

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
	 * The default value of the '{@link #getNotation() <em>Notation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotation()
	 * @generated
	 * @ordered
	 */
	protected static final String NOTATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNotation() <em>Notation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNotation()
	 * @generated
	 * @ordered
	 */
	protected String notation = NOTATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getLevel() <em>Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLevel()
	 * @generated
	 * @ordered
	 */
	protected static final LevelTyp LEVEL_EDEFAULT = LevelTyp.NONE;

	/**
	 * The cached value of the '{@link #getLevel() <em>Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLevel()
	 * @generated
	 * @ordered
	 */
	protected LevelTyp level = LEVEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getKlasse() <em>Klasse</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKlasse()
	 * @generated
	 * @ordered
	 */
	protected static final KlasseTyp KLASSE_EDEFAULT = KlasseTyp.UNKNOWN;

	/**
	 * The cached value of the '{@link #getKlasse() <em>Klasse</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKlasse()
	 * @generated
	 * @ordered
	 */
	protected KlasseTyp klasse = KLASSE_EDEFAULT;

	/**
	 * The default value of the '{@link #getTyp() <em>Typ</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTyp()
	 * @generated
	 * @ordered
	 */
	protected static final String TYP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTyp() <em>Typ</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTyp()
	 * @generated
	 * @ordered
	 */
	protected String typ = TYP_EDEFAULT;

	/**
	 * The default value of the '{@link #getEingabedatum() <em>Eingabedatum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEingabedatum()
	 * @generated
	 * @ordered
	 */
	protected static final Date EINGABEDATUM_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEingabedatum() <em>Eingabedatum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEingabedatum()
	 * @generated
	 * @ordered
	 */
	protected Date eingabedatum = EINGABEDATUM_EDEFAULT;

	/**
	 * The default value of the '{@link #getBenutzer() <em>Benutzer</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBenutzer()
	 * @generated
	 * @ordered
	 */
	protected static final String BENUTZER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBenutzer() <em>Benutzer</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBenutzer()
	 * @generated
	 * @ordered
	 */
	protected String benutzer = BENUTZER_EDEFAULT;

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
	 * The default value of the '{@link #getFormel() <em>Formel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFormel()
	 * @generated
	 * @ordered
	 */
	protected static final String FORMEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFormel() <em>Formel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFormel()
	 * @generated
	 * @ordered
	 */
	protected String formel = FORMEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getSoftware() <em>Software</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSoftware()
	 * @generated
	 * @ordered
	 */
	protected static final SoftwareType SOFTWARE_EDEFAULT = SoftwareType.NONE;

	/**
	 * The cached value of the '{@link #getSoftware() <em>Software</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSoftware()
	 * @generated
	 * @ordered
	 */
	protected SoftwareType software = SOFTWARE_EDEFAULT;

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
	 * The cached value of the '{@link #getParameter() <em>Parameter</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameter()
	 * @generated
	 * @ordered
	 */
	protected EList<StatistikModellParameter> parameter;

	/**
	 * The cached value of the '{@link #getLiteratur() <em>Literatur</em>}' reference list.
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
	protected StatistikModellImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BfrPackage.Literals.STATISTIK_MODELL;
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
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL__ID, oldId, id));
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
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNotation() {
		return notation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNotation(String newNotation) {
		String oldNotation = notation;
		notation = newNotation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL__NOTATION, oldNotation, notation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LevelTyp getLevel() {
		return level;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLevel(LevelTyp newLevel) {
		LevelTyp oldLevel = level;
		level = newLevel == null ? LEVEL_EDEFAULT : newLevel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL__LEVEL, oldLevel, level));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KlasseTyp getKlasse() {
		return klasse;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKlasse(KlasseTyp newKlasse) {
		KlasseTyp oldKlasse = klasse;
		klasse = newKlasse == null ? KLASSE_EDEFAULT : newKlasse;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL__KLASSE, oldKlasse, klasse));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTyp() {
		return typ;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTyp(String newTyp) {
		String oldTyp = typ;
		typ = newTyp;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL__TYP, oldTyp, typ));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getEingabedatum() {
		return eingabedatum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEingabedatum(Date newEingabedatum) {
		Date oldEingabedatum = eingabedatum;
		eingabedatum = newEingabedatum;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL__EINGABEDATUM, oldEingabedatum, eingabedatum));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBenutzer() {
		return benutzer;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBenutzer(String newBenutzer) {
		String oldBenutzer = benutzer;
		benutzer = newBenutzer;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL__BENUTZER, oldBenutzer, benutzer));
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
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL__BESCHREIBUNG, oldBeschreibung, beschreibung));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFormel() {
		return formel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFormel(String newFormel) {
		String oldFormel = formel;
		formel = newFormel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL__FORMEL, oldFormel, formel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SoftwareType getSoftware() {
		return software;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSoftware(SoftwareType newSoftware) {
		SoftwareType oldSoftware = software;
		software = newSoftware == null ? SOFTWARE_EDEFAULT : newSoftware;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL__SOFTWARE, oldSoftware, software));
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
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.STATISTIK_MODELL__KOMMENTAR, oldKommentar, kommentar));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<StatistikModellParameter> getParameter() {
		if (parameter == null) {
			parameter = new EObjectContainmentEList<StatistikModellParameter>(StatistikModellParameter.class, this, BfrPackage.STATISTIK_MODELL__PARAMETER);
		}
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Literatur> getLiteratur() {
		if (literatur == null) {
			literatur = new EObjectResolvingEList<Literatur>(Literatur.class, this, BfrPackage.STATISTIK_MODELL__LITERATUR);
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
			case BfrPackage.STATISTIK_MODELL__PARAMETER:
				return ((InternalEList<?>)getParameter()).basicRemove(otherEnd, msgs);
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
			case BfrPackage.STATISTIK_MODELL__ID:
				return getId();
			case BfrPackage.STATISTIK_MODELL__NAME:
				return getName();
			case BfrPackage.STATISTIK_MODELL__NOTATION:
				return getNotation();
			case BfrPackage.STATISTIK_MODELL__LEVEL:
				return getLevel();
			case BfrPackage.STATISTIK_MODELL__KLASSE:
				return getKlasse();
			case BfrPackage.STATISTIK_MODELL__TYP:
				return getTyp();
			case BfrPackage.STATISTIK_MODELL__EINGABEDATUM:
				return getEingabedatum();
			case BfrPackage.STATISTIK_MODELL__BENUTZER:
				return getBenutzer();
			case BfrPackage.STATISTIK_MODELL__BESCHREIBUNG:
				return getBeschreibung();
			case BfrPackage.STATISTIK_MODELL__FORMEL:
				return getFormel();
			case BfrPackage.STATISTIK_MODELL__SOFTWARE:
				return getSoftware();
			case BfrPackage.STATISTIK_MODELL__KOMMENTAR:
				return getKommentar();
			case BfrPackage.STATISTIK_MODELL__PARAMETER:
				return getParameter();
			case BfrPackage.STATISTIK_MODELL__LITERATUR:
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
			case BfrPackage.STATISTIK_MODELL__ID:
				setId((Integer)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL__NAME:
				setName((String)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL__NOTATION:
				setNotation((String)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL__LEVEL:
				setLevel((LevelTyp)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL__KLASSE:
				setKlasse((KlasseTyp)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL__TYP:
				setTyp((String)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL__EINGABEDATUM:
				setEingabedatum((Date)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL__BENUTZER:
				setBenutzer((String)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL__BESCHREIBUNG:
				setBeschreibung((String)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL__FORMEL:
				setFormel((String)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL__SOFTWARE:
				setSoftware((SoftwareType)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL__KOMMENTAR:
				setKommentar((String)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL__PARAMETER:
				getParameter().clear();
				getParameter().addAll((Collection<? extends StatistikModellParameter>)newValue);
				return;
			case BfrPackage.STATISTIK_MODELL__LITERATUR:
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
			case BfrPackage.STATISTIK_MODELL__ID:
				setId(ID_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL__NOTATION:
				setNotation(NOTATION_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL__LEVEL:
				setLevel(LEVEL_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL__KLASSE:
				setKlasse(KLASSE_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL__TYP:
				setTyp(TYP_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL__EINGABEDATUM:
				setEingabedatum(EINGABEDATUM_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL__BENUTZER:
				setBenutzer(BENUTZER_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL__BESCHREIBUNG:
				setBeschreibung(BESCHREIBUNG_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL__FORMEL:
				setFormel(FORMEL_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL__SOFTWARE:
				setSoftware(SOFTWARE_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL__KOMMENTAR:
				setKommentar(KOMMENTAR_EDEFAULT);
				return;
			case BfrPackage.STATISTIK_MODELL__PARAMETER:
				getParameter().clear();
				return;
			case BfrPackage.STATISTIK_MODELL__LITERATUR:
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
			case BfrPackage.STATISTIK_MODELL__ID:
				return id != ID_EDEFAULT;
			case BfrPackage.STATISTIK_MODELL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case BfrPackage.STATISTIK_MODELL__NOTATION:
				return NOTATION_EDEFAULT == null ? notation != null : !NOTATION_EDEFAULT.equals(notation);
			case BfrPackage.STATISTIK_MODELL__LEVEL:
				return level != LEVEL_EDEFAULT;
			case BfrPackage.STATISTIK_MODELL__KLASSE:
				return klasse != KLASSE_EDEFAULT;
			case BfrPackage.STATISTIK_MODELL__TYP:
				return TYP_EDEFAULT == null ? typ != null : !TYP_EDEFAULT.equals(typ);
			case BfrPackage.STATISTIK_MODELL__EINGABEDATUM:
				return EINGABEDATUM_EDEFAULT == null ? eingabedatum != null : !EINGABEDATUM_EDEFAULT.equals(eingabedatum);
			case BfrPackage.STATISTIK_MODELL__BENUTZER:
				return BENUTZER_EDEFAULT == null ? benutzer != null : !BENUTZER_EDEFAULT.equals(benutzer);
			case BfrPackage.STATISTIK_MODELL__BESCHREIBUNG:
				return BESCHREIBUNG_EDEFAULT == null ? beschreibung != null : !BESCHREIBUNG_EDEFAULT.equals(beschreibung);
			case BfrPackage.STATISTIK_MODELL__FORMEL:
				return FORMEL_EDEFAULT == null ? formel != null : !FORMEL_EDEFAULT.equals(formel);
			case BfrPackage.STATISTIK_MODELL__SOFTWARE:
				return software != SOFTWARE_EDEFAULT;
			case BfrPackage.STATISTIK_MODELL__KOMMENTAR:
				return KOMMENTAR_EDEFAULT == null ? kommentar != null : !KOMMENTAR_EDEFAULT.equals(kommentar);
			case BfrPackage.STATISTIK_MODELL__PARAMETER:
				return parameter != null && !parameter.isEmpty();
			case BfrPackage.STATISTIK_MODELL__LITERATUR:
				return literatur != null && !literatur.isEmpty();
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
		result.append(", notation: ");
		result.append(notation);
		result.append(", level: ");
		result.append(level);
		result.append(", klasse: ");
		result.append(klasse);
		result.append(", typ: ");
		result.append(typ);
		result.append(", eingabedatum: ");
		result.append(eingabedatum);
		result.append(", benutzer: ");
		result.append(benutzer);
		result.append(", beschreibung: ");
		result.append(beschreibung);
		result.append(", formel: ");
		result.append(formel);
		result.append(", software: ");
		result.append(software);
		result.append(", kommentar: ");
		result.append(kommentar);
		result.append(')');
		return result.toString();
	}

} //StatistikModellImpl
