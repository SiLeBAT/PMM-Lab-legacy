/**
 * $Id: LiteraturImpl.java 651 2012-01-24 09:59:12Z sdoerl $
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
import de.dim.bfr.FreigabeTyp;
import de.dim.bfr.Literatur;
import de.dim.bfr.LiteraturTyp;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Literatur</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.dim.bfr.impl.LiteraturImpl#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.impl.LiteraturImpl#getErstautor <em>Erstautor</em>}</li>
 *   <li>{@link de.dim.bfr.impl.LiteraturImpl#getJahr <em>Jahr</em>}</li>
 *   <li>{@link de.dim.bfr.impl.LiteraturImpl#getTitel <em>Titel</em>}</li>
 *   <li>{@link de.dim.bfr.impl.LiteraturImpl#getLiteraturAbstract <em>Literatur Abstract</em>}</li>
 *   <li>{@link de.dim.bfr.impl.LiteraturImpl#getJournal <em>Journal</em>}</li>
 *   <li>{@link de.dim.bfr.impl.LiteraturImpl#getVolume <em>Volume</em>}</li>
 *   <li>{@link de.dim.bfr.impl.LiteraturImpl#getIssue <em>Issue</em>}</li>
 *   <li>{@link de.dim.bfr.impl.LiteraturImpl#getFreigabeModus <em>Freigabe Modus</em>}</li>
 *   <li>{@link de.dim.bfr.impl.LiteraturImpl#getWebseite <em>Webseite</em>}</li>
 *   <li>{@link de.dim.bfr.impl.LiteraturImpl#getLiteraturTyp <em>Literatur Typ</em>}</li>
 *   <li>{@link de.dim.bfr.impl.LiteraturImpl#getPaper <em>Paper</em>}</li>
 *   <li>{@link de.dim.bfr.impl.LiteraturImpl#getKommentar <em>Kommentar</em>}</li>
 *   <li>{@link de.dim.bfr.impl.LiteraturImpl#getSeite <em>Seite</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LiteraturImpl extends EObjectImpl implements Literatur {
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
	 * The default value of the '{@link #getErstautor() <em>Erstautor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getErstautor()
	 * @generated
	 * @ordered
	 */
	protected static final String ERSTAUTOR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getErstautor() <em>Erstautor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getErstautor()
	 * @generated
	 * @ordered
	 */
	protected String erstautor = ERSTAUTOR_EDEFAULT;

	/**
	 * The default value of the '{@link #getJahr() <em>Jahr</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJahr()
	 * @generated
	 * @ordered
	 */
	protected static final int JAHR_EDEFAULT = 2000;

	/**
	 * The cached value of the '{@link #getJahr() <em>Jahr</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJahr()
	 * @generated
	 * @ordered
	 */
	protected int jahr = JAHR_EDEFAULT;

	/**
	 * The default value of the '{@link #getTitel() <em>Titel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitel()
	 * @generated
	 * @ordered
	 */
	protected static final String TITEL_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getTitel() <em>Titel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitel()
	 * @generated
	 * @ordered
	 */
	protected String titel = TITEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getLiteraturAbstract() <em>Literatur Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLiteraturAbstract()
	 * @generated
	 * @ordered
	 */
	protected static final String LITERATUR_ABSTRACT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLiteraturAbstract() <em>Literatur Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLiteraturAbstract()
	 * @generated
	 * @ordered
	 */
	protected String literaturAbstract = LITERATUR_ABSTRACT_EDEFAULT;

	/**
	 * The default value of the '{@link #getJournal() <em>Journal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJournal()
	 * @generated
	 * @ordered
	 */
	protected static final String JOURNAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getJournal() <em>Journal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJournal()
	 * @generated
	 * @ordered
	 */
	protected String journal = JOURNAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolume() <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolume()
	 * @generated
	 * @ordered
	 */
	protected static final String VOLUME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVolume() <em>Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolume()
	 * @generated
	 * @ordered
	 */
	protected String volume = VOLUME_EDEFAULT;

	/**
	 * The default value of the '{@link #getIssue() <em>Issue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIssue()
	 * @generated
	 * @ordered
	 */
	protected static final String ISSUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIssue() <em>Issue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIssue()
	 * @generated
	 * @ordered
	 */
	protected String issue = ISSUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getFreigabeModus() <em>Freigabe Modus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFreigabeModus()
	 * @generated
	 * @ordered
	 */
	protected static final FreigabeTyp FREIGABE_MODUS_EDEFAULT = FreigabeTyp.KEINE;

	/**
	 * The cached value of the '{@link #getFreigabeModus() <em>Freigabe Modus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFreigabeModus()
	 * @generated
	 * @ordered
	 */
	protected FreigabeTyp freigabeModus = FREIGABE_MODUS_EDEFAULT;

	/**
	 * The default value of the '{@link #getWebseite() <em>Webseite</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWebseite()
	 * @generated
	 * @ordered
	 */
	protected static final String WEBSEITE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWebseite() <em>Webseite</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWebseite()
	 * @generated
	 * @ordered
	 */
	protected String webseite = WEBSEITE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLiteraturTyp() <em>Literatur Typ</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLiteraturTyp()
	 * @generated
	 * @ordered
	 */
	protected static final LiteraturTyp LITERATUR_TYP_EDEFAULT = LiteraturTyp.UNBEKANNT;

	/**
	 * The cached value of the '{@link #getLiteraturTyp() <em>Literatur Typ</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLiteraturTyp()
	 * @generated
	 * @ordered
	 */
	protected LiteraturTyp literaturTyp = LITERATUR_TYP_EDEFAULT;

	/**
	 * The default value of the '{@link #getPaper() <em>Paper</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPaper()
	 * @generated
	 * @ordered
	 */
	protected static final String PAPER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPaper() <em>Paper</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPaper()
	 * @generated
	 * @ordered
	 */
	protected String paper = PAPER_EDEFAULT;

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
	 * The default value of the '{@link #getSeite() <em>Seite</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeite()
	 * @generated
	 * @ordered
	 */
	protected static final Integer SEITE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSeite() <em>Seite</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeite()
	 * @generated
	 * @ordered
	 */
	protected Integer seite = SEITE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LiteraturImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BfrPackage.Literals.LITERATUR;
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
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.LITERATUR__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getErstautor() {
		return erstautor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setErstautor(String newErstautor) {
		String oldErstautor = erstautor;
		erstautor = newErstautor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.LITERATUR__ERSTAUTOR, oldErstautor, erstautor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getJahr() {
		return jahr;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setJahr(int newJahr) {
		int oldJahr = jahr;
		jahr = newJahr;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.LITERATUR__JAHR, oldJahr, jahr));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTitel() {
		return titel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTitel(String newTitel) {
		String oldTitel = titel;
		titel = newTitel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.LITERATUR__TITEL, oldTitel, titel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteraturAbstract() {
		return literaturAbstract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLiteraturAbstract(String newLiteraturAbstract) {
		String oldLiteraturAbstract = literaturAbstract;
		literaturAbstract = newLiteraturAbstract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.LITERATUR__LITERATUR_ABSTRACT, oldLiteraturAbstract, literaturAbstract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getJournal() {
		return journal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setJournal(String newJournal) {
		String oldJournal = journal;
		journal = newJournal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.LITERATUR__JOURNAL, oldJournal, journal));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVolume() {
		return volume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolume(String newVolume) {
		String oldVolume = volume;
		volume = newVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.LITERATUR__VOLUME, oldVolume, volume));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIssue() {
		return issue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIssue(String newIssue) {
		String oldIssue = issue;
		issue = newIssue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.LITERATUR__ISSUE, oldIssue, issue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer getSeite() {
		return seite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSeite(Integer newSeite) {
		Integer oldSeite = seite;
		seite = newSeite;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.LITERATUR__SEITE, oldSeite, seite));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FreigabeTyp getFreigabeModus() {
		return freigabeModus;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFreigabeModus(FreigabeTyp newFreigabeModus) {
		FreigabeTyp oldFreigabeModus = freigabeModus;
		freigabeModus = newFreigabeModus == null ? FREIGABE_MODUS_EDEFAULT : newFreigabeModus;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.LITERATUR__FREIGABE_MODUS, oldFreigabeModus, freigabeModus));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getWebseite() {
		return webseite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWebseite(String newWebseite) {
		String oldWebseite = webseite;
		webseite = newWebseite;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.LITERATUR__WEBSEITE, oldWebseite, webseite));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LiteraturTyp getLiteraturTyp() {
		return literaturTyp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLiteraturTyp(LiteraturTyp newLiteraturTyp) {
		LiteraturTyp oldLiteraturTyp = literaturTyp;
		literaturTyp = newLiteraturTyp == null ? LITERATUR_TYP_EDEFAULT : newLiteraturTyp;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.LITERATUR__LITERATUR_TYP, oldLiteraturTyp, literaturTyp));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPaper() {
		return paper;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPaper(String newPaper) {
		String oldPaper = paper;
		paper = newPaper;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.LITERATUR__PAPER, oldPaper, paper));
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
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.LITERATUR__KOMMENTAR, oldKommentar, kommentar));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case BfrPackage.LITERATUR__ID:
				return getId();
			case BfrPackage.LITERATUR__ERSTAUTOR:
				return getErstautor();
			case BfrPackage.LITERATUR__JAHR:
				return getJahr();
			case BfrPackage.LITERATUR__TITEL:
				return getTitel();
			case BfrPackage.LITERATUR__LITERATUR_ABSTRACT:
				return getLiteraturAbstract();
			case BfrPackage.LITERATUR__JOURNAL:
				return getJournal();
			case BfrPackage.LITERATUR__VOLUME:
				return getVolume();
			case BfrPackage.LITERATUR__ISSUE:
				return getIssue();
			case BfrPackage.LITERATUR__FREIGABE_MODUS:
				return getFreigabeModus();
			case BfrPackage.LITERATUR__WEBSEITE:
				return getWebseite();
			case BfrPackage.LITERATUR__LITERATUR_TYP:
				return getLiteraturTyp();
			case BfrPackage.LITERATUR__PAPER:
				return getPaper();
			case BfrPackage.LITERATUR__KOMMENTAR:
				return getKommentar();
			case BfrPackage.LITERATUR__SEITE:
				return getSeite();
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
			case BfrPackage.LITERATUR__ID:
				setId((Integer)newValue);
				return;
			case BfrPackage.LITERATUR__ERSTAUTOR:
				setErstautor((String)newValue);
				return;
			case BfrPackage.LITERATUR__JAHR:
				setJahr((Integer)newValue);
				return;
			case BfrPackage.LITERATUR__TITEL:
				setTitel((String)newValue);
				return;
			case BfrPackage.LITERATUR__LITERATUR_ABSTRACT:
				setLiteraturAbstract((String)newValue);
				return;
			case BfrPackage.LITERATUR__JOURNAL:
				setJournal((String)newValue);
				return;
			case BfrPackage.LITERATUR__VOLUME:
				setVolume((String)newValue);
				return;
			case BfrPackage.LITERATUR__ISSUE:
				setIssue((String)newValue);
				return;
			case BfrPackage.LITERATUR__FREIGABE_MODUS:
				setFreigabeModus((FreigabeTyp)newValue);
				return;
			case BfrPackage.LITERATUR__WEBSEITE:
				setWebseite((String)newValue);
				return;
			case BfrPackage.LITERATUR__LITERATUR_TYP:
				setLiteraturTyp((LiteraturTyp)newValue);
				return;
			case BfrPackage.LITERATUR__PAPER:
				setPaper((String)newValue);
				return;
			case BfrPackage.LITERATUR__KOMMENTAR:
				setKommentar((String)newValue);
				return;
			case BfrPackage.LITERATUR__SEITE:
				setSeite((Integer)newValue);
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
			case BfrPackage.LITERATUR__ID:
				setId(ID_EDEFAULT);
				return;
			case BfrPackage.LITERATUR__ERSTAUTOR:
				setErstautor(ERSTAUTOR_EDEFAULT);
				return;
			case BfrPackage.LITERATUR__JAHR:
				setJahr(JAHR_EDEFAULT);
				return;
			case BfrPackage.LITERATUR__TITEL:
				setTitel(TITEL_EDEFAULT);
				return;
			case BfrPackage.LITERATUR__LITERATUR_ABSTRACT:
				setLiteraturAbstract(LITERATUR_ABSTRACT_EDEFAULT);
				return;
			case BfrPackage.LITERATUR__JOURNAL:
				setJournal(JOURNAL_EDEFAULT);
				return;
			case BfrPackage.LITERATUR__VOLUME:
				setVolume(VOLUME_EDEFAULT);
				return;
			case BfrPackage.LITERATUR__ISSUE:
				setIssue(ISSUE_EDEFAULT);
				return;
			case BfrPackage.LITERATUR__FREIGABE_MODUS:
				setFreigabeModus(FREIGABE_MODUS_EDEFAULT);
				return;
			case BfrPackage.LITERATUR__WEBSEITE:
				setWebseite(WEBSEITE_EDEFAULT);
				return;
			case BfrPackage.LITERATUR__LITERATUR_TYP:
				setLiteraturTyp(LITERATUR_TYP_EDEFAULT);
				return;
			case BfrPackage.LITERATUR__PAPER:
				setPaper(PAPER_EDEFAULT);
				return;
			case BfrPackage.LITERATUR__KOMMENTAR:
				setKommentar(KOMMENTAR_EDEFAULT);
				return;
			case BfrPackage.LITERATUR__SEITE:
				setSeite(SEITE_EDEFAULT);
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
			case BfrPackage.LITERATUR__ID:
				return id != ID_EDEFAULT;
			case BfrPackage.LITERATUR__ERSTAUTOR:
				return ERSTAUTOR_EDEFAULT == null ? erstautor != null : !ERSTAUTOR_EDEFAULT.equals(erstautor);
			case BfrPackage.LITERATUR__JAHR:
				return jahr != JAHR_EDEFAULT;
			case BfrPackage.LITERATUR__TITEL:
				return TITEL_EDEFAULT == null ? titel != null : !TITEL_EDEFAULT.equals(titel);
			case BfrPackage.LITERATUR__LITERATUR_ABSTRACT:
				return LITERATUR_ABSTRACT_EDEFAULT == null ? literaturAbstract != null : !LITERATUR_ABSTRACT_EDEFAULT.equals(literaturAbstract);
			case BfrPackage.LITERATUR__JOURNAL:
				return JOURNAL_EDEFAULT == null ? journal != null : !JOURNAL_EDEFAULT.equals(journal);
			case BfrPackage.LITERATUR__VOLUME:
				return VOLUME_EDEFAULT == null ? volume != null : !VOLUME_EDEFAULT.equals(volume);
			case BfrPackage.LITERATUR__ISSUE:
				return ISSUE_EDEFAULT == null ? issue != null : !ISSUE_EDEFAULT.equals(issue);
			case BfrPackage.LITERATUR__FREIGABE_MODUS:
				return freigabeModus != FREIGABE_MODUS_EDEFAULT;
			case BfrPackage.LITERATUR__WEBSEITE:
				return WEBSEITE_EDEFAULT == null ? webseite != null : !WEBSEITE_EDEFAULT.equals(webseite);
			case BfrPackage.LITERATUR__LITERATUR_TYP:
				return literaturTyp != LITERATUR_TYP_EDEFAULT;
			case BfrPackage.LITERATUR__PAPER:
				return PAPER_EDEFAULT == null ? paper != null : !PAPER_EDEFAULT.equals(paper);
			case BfrPackage.LITERATUR__KOMMENTAR:
				return KOMMENTAR_EDEFAULT == null ? kommentar != null : !KOMMENTAR_EDEFAULT.equals(kommentar);
			case BfrPackage.LITERATUR__SEITE:
				return SEITE_EDEFAULT == null ? seite != null : !SEITE_EDEFAULT.equals(seite);
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
		result.append(", erstautor: ");
		result.append(erstautor);
		result.append(", jahr: ");
		result.append(jahr);
		result.append(", titel: ");
		result.append(titel);
		result.append(", literaturAbstract: ");
		result.append(literaturAbstract);
		result.append(", journal: ");
		result.append(journal);
		result.append(", volume: ");
		result.append(volume);
		result.append(", issue: ");
		result.append(issue);
		result.append(", freigabeModus: ");
		result.append(freigabeModus);
		result.append(", webseite: ");
		result.append(webseite);
		result.append(", literaturTyp: ");
		result.append(literaturTyp);
		result.append(", paper: ");
		result.append(paper);
		result.append(", kommentar: ");
		result.append(kommentar);
		result.append(", seite: ");
		result.append(seite);
		result.append(')');
		return result.toString();
	}

} //LiteraturImpl
