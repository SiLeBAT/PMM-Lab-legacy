/**
 * $Id: GeschaetztStatistikModellImpl.java 651 2012-01-24 09:59:12Z sdoerl $
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
import de.dim.bfr.GeschModellParameter;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.Literatur;
import de.dim.bfr.ParameterCovCor;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.VersuchsBedingung;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Geschaetzt Statistik Modell</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.dim.bfr.impl.GeschaetztStatistikModellImpl#getStatistikModel <em>Statistik Model</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschaetztStatistikModellImpl#isManuellEingetragen <em>Manuell Eingetragen</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschaetztStatistikModellImpl#getRSquared <em>RSquared</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschaetztStatistikModellImpl#getRss <em>Rss</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschaetztStatistikModellImpl#getScore <em>Score</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschaetztStatistikModellImpl#getKommentar <em>Kommentar</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschaetztStatistikModellImpl#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschaetztStatistikModellImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschaetztStatistikModellImpl#getBedingung <em>Bedingung</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschaetztStatistikModellImpl#getLiteratur <em>Literatur</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschaetztStatistikModellImpl#getParameterCovCor <em>Parameter Cov Cor</em>}</li>
 *   <li>{@link de.dim.bfr.impl.GeschaetztStatistikModellImpl#getResponse <em>Response</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GeschaetztStatistikModellImpl extends EObjectImpl implements GeschaetztStatistikModell {
	/**
	 * The cached value of the '{@link #getStatistikModel() <em>Statistik Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatistikModel()
	 * @generated
	 * @ordered
	 */
	protected StatistikModell statistikModel;

	/**
	 * The default value of the '{@link #isManuellEingetragen() <em>Manuell Eingetragen</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isManuellEingetragen()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MANUELL_EINGETRAGEN_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isManuellEingetragen() <em>Manuell Eingetragen</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isManuellEingetragen()
	 * @generated
	 * @ordered
	 */
	protected boolean manuellEingetragen = MANUELL_EINGETRAGEN_EDEFAULT;

	/**
	 * The default value of the '{@link #getRSquared() <em>RSquared</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRSquared()
	 * @generated
	 * @ordered
	 */
	protected static final double RSQUARED_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getRSquared() <em>RSquared</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRSquared()
	 * @generated
	 * @ordered
	 */
	protected double rSquared = RSQUARED_EDEFAULT;

	/**
	 * The default value of the '{@link #getRss() <em>Rss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRss()
	 * @generated
	 * @ordered
	 */
	protected static final double RSS_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getRss() <em>Rss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRss()
	 * @generated
	 * @ordered
	 */
	protected double rss = RSS_EDEFAULT;

	/**
	 * The default value of the '{@link #getScore() <em>Score</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScore()
	 * @generated
	 * @ordered
	 */
	protected static final int SCORE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getScore() <em>Score</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScore()
	 * @generated
	 * @ordered
	 */
	protected int score = SCORE_EDEFAULT;

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
	 * The cached value of the '{@link #getParameter() <em>Parameter</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameter()
	 * @generated
	 * @ordered
	 */
	protected EList<GeschModellParameter> parameter;

	/**
	 * The cached value of the '{@link #getBedingung() <em>Bedingung</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBedingung()
	 * @generated
	 * @ordered
	 */
	protected VersuchsBedingung bedingung;

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
	 * The cached value of the '{@link #getParameterCovCor() <em>Parameter Cov Cor</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterCovCor()
	 * @generated
	 * @ordered
	 */
	protected EList<ParameterCovCor> parameterCovCor;

	/**
	 * The cached value of the '{@link #getResponse() <em>Response</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResponse()
	 * @generated
	 * @ordered
	 */
	protected StatistikModellParameter response;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GeschaetztStatistikModellImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BfrPackage.Literals.GESCHAETZT_STATISTIK_MODELL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatistikModell getStatistikModel() {
		if (statistikModel != null && statistikModel.eIsProxy()) {
			InternalEObject oldStatistikModel = (InternalEObject)statistikModel;
			statistikModel = (StatistikModell)eResolveProxy(oldStatistikModel);
			if (statistikModel != oldStatistikModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BfrPackage.GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL, oldStatistikModel, statistikModel));
			}
		}
		return statistikModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatistikModell basicGetStatistikModel() {
		return statistikModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStatistikModel(StatistikModell newStatistikModel) {
		StatistikModell oldStatistikModel = statistikModel;
		statistikModel = newStatistikModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL, oldStatistikModel, statistikModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isManuellEingetragen() {
		return manuellEingetragen;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setManuellEingetragen(boolean newManuellEingetragen) {
		boolean oldManuellEingetragen = manuellEingetragen;
		manuellEingetragen = newManuellEingetragen;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCHAETZT_STATISTIK_MODELL__MANUELL_EINGETRAGEN, oldManuellEingetragen, manuellEingetragen));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getRSquared() {
		return rSquared;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRSquared(double newRSquared) {
		double oldRSquared = rSquared;
		rSquared = newRSquared;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCHAETZT_STATISTIK_MODELL__RSQUARED, oldRSquared, rSquared));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getRss() {
		return rss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRss(double newRss) {
		double oldRss = rss;
		rss = newRss;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCHAETZT_STATISTIK_MODELL__RSS, oldRss, rss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getScore() {
		return score;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScore(int newScore) {
		int oldScore = score;
		score = newScore;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCHAETZT_STATISTIK_MODELL__SCORE, oldScore, score));
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
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCHAETZT_STATISTIK_MODELL__KOMMENTAR, oldKommentar, kommentar));
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
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCHAETZT_STATISTIK_MODELL__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<GeschModellParameter> getParameter() {
		if (parameter == null) {
			parameter = new EObjectContainmentEList<GeschModellParameter>(GeschModellParameter.class, this, BfrPackage.GESCHAETZT_STATISTIK_MODELL__PARAMETER);
		}
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VersuchsBedingung getBedingung() {
		if (bedingung != null && bedingung.eIsProxy()) {
			InternalEObject oldBedingung = (InternalEObject)bedingung;
			bedingung = (VersuchsBedingung)eResolveProxy(oldBedingung);
			if (bedingung != oldBedingung) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BfrPackage.GESCHAETZT_STATISTIK_MODELL__BEDINGUNG, oldBedingung, bedingung));
			}
		}
		return bedingung;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VersuchsBedingung basicGetBedingung() {
		return bedingung;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBedingung(VersuchsBedingung newBedingung) {
		VersuchsBedingung oldBedingung = bedingung;
		bedingung = newBedingung;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCHAETZT_STATISTIK_MODELL__BEDINGUNG, oldBedingung, bedingung));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Literatur> getLiteratur() {
		if (literatur == null) {
			literatur = new EObjectResolvingEList<Literatur>(Literatur.class, this, BfrPackage.GESCHAETZT_STATISTIK_MODELL__LITERATUR);
		}
		return literatur;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ParameterCovCor> getParameterCovCor() {
		if (parameterCovCor == null) {
			parameterCovCor = new EObjectResolvingEList<ParameterCovCor>(ParameterCovCor.class, this, BfrPackage.GESCHAETZT_STATISTIK_MODELL__PARAMETER_COV_COR);
		}
		return parameterCovCor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatistikModellParameter getResponse() {
		if (response != null && response.eIsProxy()) {
			InternalEObject oldResponse = (InternalEObject)response;
			response = (StatistikModellParameter)eResolveProxy(oldResponse);
			if (response != oldResponse) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BfrPackage.GESCHAETZT_STATISTIK_MODELL__RESPONSE, oldResponse, response));
			}
		}
		return response;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatistikModellParameter basicGetResponse() {
		return response;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResponse(StatistikModellParameter newResponse) {
		StatistikModellParameter oldResponse = response;
		response = newResponse;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.GESCHAETZT_STATISTIK_MODELL__RESPONSE, oldResponse, response));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__PARAMETER:
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
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL:
				if (resolve) return getStatistikModel();
				return basicGetStatistikModel();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__MANUELL_EINGETRAGEN:
				return isManuellEingetragen();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__RSQUARED:
				return getRSquared();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__RSS:
				return getRss();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__SCORE:
				return getScore();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__KOMMENTAR:
				return getKommentar();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__ID:
				return getId();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__PARAMETER:
				return getParameter();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__BEDINGUNG:
				if (resolve) return getBedingung();
				return basicGetBedingung();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__LITERATUR:
				return getLiteratur();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__PARAMETER_COV_COR:
				return getParameterCovCor();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__RESPONSE:
				if (resolve) return getResponse();
				return basicGetResponse();
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
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL:
				setStatistikModel((StatistikModell)newValue);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__MANUELL_EINGETRAGEN:
				setManuellEingetragen((Boolean)newValue);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__RSQUARED:
				setRSquared((Double)newValue);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__RSS:
				setRss((Double)newValue);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__SCORE:
				setScore((Integer)newValue);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__KOMMENTAR:
				setKommentar((String)newValue);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__ID:
				setId((Integer)newValue);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__PARAMETER:
				getParameter().clear();
				getParameter().addAll((Collection<? extends GeschModellParameter>)newValue);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__BEDINGUNG:
				setBedingung((VersuchsBedingung)newValue);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__LITERATUR:
				getLiteratur().clear();
				getLiteratur().addAll((Collection<? extends Literatur>)newValue);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__PARAMETER_COV_COR:
				getParameterCovCor().clear();
				getParameterCovCor().addAll((Collection<? extends ParameterCovCor>)newValue);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__RESPONSE:
				setResponse((StatistikModellParameter)newValue);
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
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL:
				setStatistikModel((StatistikModell)null);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__MANUELL_EINGETRAGEN:
				setManuellEingetragen(MANUELL_EINGETRAGEN_EDEFAULT);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__RSQUARED:
				setRSquared(RSQUARED_EDEFAULT);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__RSS:
				setRss(RSS_EDEFAULT);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__SCORE:
				setScore(SCORE_EDEFAULT);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__KOMMENTAR:
				setKommentar(KOMMENTAR_EDEFAULT);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__ID:
				setId(ID_EDEFAULT);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__PARAMETER:
				getParameter().clear();
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__BEDINGUNG:
				setBedingung((VersuchsBedingung)null);
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__LITERATUR:
				getLiteratur().clear();
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__PARAMETER_COV_COR:
				getParameterCovCor().clear();
				return;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__RESPONSE:
				setResponse((StatistikModellParameter)null);
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
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL:
				return statistikModel != null;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__MANUELL_EINGETRAGEN:
				return manuellEingetragen != MANUELL_EINGETRAGEN_EDEFAULT;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__RSQUARED:
				return rSquared != RSQUARED_EDEFAULT;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__RSS:
				return rss != RSS_EDEFAULT;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__SCORE:
				return score != SCORE_EDEFAULT;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__KOMMENTAR:
				return KOMMENTAR_EDEFAULT == null ? kommentar != null : !KOMMENTAR_EDEFAULT.equals(kommentar);
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__ID:
				return id != ID_EDEFAULT;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__PARAMETER:
				return parameter != null && !parameter.isEmpty();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__BEDINGUNG:
				return bedingung != null;
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__LITERATUR:
				return literatur != null && !literatur.isEmpty();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__PARAMETER_COV_COR:
				return parameterCovCor != null && !parameterCovCor.isEmpty();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL__RESPONSE:
				return response != null;
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
		result.append(" (manuellEingetragen: ");
		result.append(manuellEingetragen);
		result.append(", rSquared: ");
		result.append(rSquared);
		result.append(", rss: ");
		result.append(rss);
		result.append(", score: ");
		result.append(score);
		result.append(", kommentar: ");
		result.append(kommentar);
		result.append(", id: ");
		result.append(id);
		result.append(')');
		return result.toString();
	}

} //GeschaetztStatistikModellImpl
