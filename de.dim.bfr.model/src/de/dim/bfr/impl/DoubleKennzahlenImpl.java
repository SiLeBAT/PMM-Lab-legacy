/**
 * $Id: DoubleKennzahlenImpl.java 651 2012-01-24 09:59:12Z sdoerl $
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
import de.dim.bfr.DoubleKennzahlen;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Double Kennzahlen</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getFunctionX <em>Function X</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#isFunctionXG <em>Function XG</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getFunctionZeit <em>Function Zeit</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#isFunctionZeitG <em>Function Zeit G</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getId <em>Id</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getLcl95 <em>Lcl95</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#isLcl95G <em>Lcl95 G</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getMaximum <em>Maximum</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#isMaximumG <em>Maximum G</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getMedian <em>Median</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#isMedianG <em>Median G</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getMinimum <em>Minimum</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#isMinimumG <em>Minimum G</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getStandardabweichung <em>Standardabweichung</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#isStandardabweichungG <em>Standardabweichung G</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getTimes10PowerOf <em>Times10 Power Of</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getUcl95 <em>Ucl95</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#isUcl95G <em>Ucl95 G</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#isUndefinedND <em>Undefined ND</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getVerteilung <em>Verteilung</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#isVerteilungG <em>Verteilung G</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getWert <em>Wert</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#isWertG <em>Wert G</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getWertTyp <em>Wert Typ</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getWiederholungen <em>Wiederholungen</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#isWiederholungenG <em>Wiederholungen G</em>}</li>
 *   <li>{@link de.dim.bfr.impl.DoubleKennzahlenImpl#getX <em>X</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DoubleKennzahlenImpl extends EObjectImpl implements DoubleKennzahlen {
	/**
	 * The default value of the '{@link #getFunctionX() <em>Function X</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFunctionX()
	 * @generated
	 * @ordered
	 */
	protected static final String FUNCTION_X_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFunctionX() <em>Function X</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFunctionX()
	 * @generated
	 * @ordered
	 */
	protected String functionX = FUNCTION_X_EDEFAULT;

	/**
	 * The default value of the '{@link #isFunctionXG() <em>Function XG</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFunctionXG()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FUNCTION_XG_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFunctionXG() <em>Function XG</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFunctionXG()
	 * @generated
	 * @ordered
	 */
	protected boolean functionXG = FUNCTION_XG_EDEFAULT;

	/**
	 * The default value of the '{@link #getFunctionZeit() <em>Function Zeit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFunctionZeit()
	 * @generated
	 * @ordered
	 */
	protected static final String FUNCTION_ZEIT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFunctionZeit() <em>Function Zeit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFunctionZeit()
	 * @generated
	 * @ordered
	 */
	protected String functionZeit = FUNCTION_ZEIT_EDEFAULT;

	/**
	 * The default value of the '{@link #isFunctionZeitG() <em>Function Zeit G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFunctionZeitG()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FUNCTION_ZEIT_G_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFunctionZeitG() <em>Function Zeit G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFunctionZeitG()
	 * @generated
	 * @ordered
	 */
	protected boolean functionZeitG = FUNCTION_ZEIT_G_EDEFAULT;

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
	 * The default value of the '{@link #getLcl95() <em>Lcl95</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLcl95()
	 * @generated
	 * @ordered
	 */
	protected static final double LCL95_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getLcl95() <em>Lcl95</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLcl95()
	 * @generated
	 * @ordered
	 */
	protected double lcl95 = LCL95_EDEFAULT;

	/**
	 * The default value of the '{@link #isLcl95G() <em>Lcl95 G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLcl95G()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LCL95_G_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLcl95G() <em>Lcl95 G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLcl95G()
	 * @generated
	 * @ordered
	 */
	protected boolean lcl95G = LCL95_G_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaximum() <em>Maximum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximum()
	 * @generated
	 * @ordered
	 */
	protected static final double MAXIMUM_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMaximum() <em>Maximum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaximum()
	 * @generated
	 * @ordered
	 */
	protected double maximum = MAXIMUM_EDEFAULT;

	/**
	 * The default value of the '{@link #isMaximumG() <em>Maximum G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMaximumG()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MAXIMUM_G_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMaximumG() <em>Maximum G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMaximumG()
	 * @generated
	 * @ordered
	 */
	protected boolean maximumG = MAXIMUM_G_EDEFAULT;

	/**
	 * The default value of the '{@link #getMedian() <em>Median</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMedian()
	 * @generated
	 * @ordered
	 */
	protected static final double MEDIAN_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMedian() <em>Median</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMedian()
	 * @generated
	 * @ordered
	 */
	protected double median = MEDIAN_EDEFAULT;

	/**
	 * The default value of the '{@link #isMedianG() <em>Median G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMedianG()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MEDIAN_G_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMedianG() <em>Median G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMedianG()
	 * @generated
	 * @ordered
	 */
	protected boolean medianG = MEDIAN_G_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinimum() <em>Minimum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimum()
	 * @generated
	 * @ordered
	 */
	protected static final double MINIMUM_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMinimum() <em>Minimum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinimum()
	 * @generated
	 * @ordered
	 */
	protected double minimum = MINIMUM_EDEFAULT;

	/**
	 * The default value of the '{@link #isMinimumG() <em>Minimum G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMinimumG()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MINIMUM_G_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMinimumG() <em>Minimum G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMinimumG()
	 * @generated
	 * @ordered
	 */
	protected boolean minimumG = MINIMUM_G_EDEFAULT;

	/**
	 * The default value of the '{@link #getStandardabweichung() <em>Standardabweichung</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStandardabweichung()
	 * @generated
	 * @ordered
	 */
	protected static final double STANDARDABWEICHUNG_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getStandardabweichung() <em>Standardabweichung</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStandardabweichung()
	 * @generated
	 * @ordered
	 */
	protected double standardabweichung = STANDARDABWEICHUNG_EDEFAULT;

	/**
	 * The default value of the '{@link #isStandardabweichungG() <em>Standardabweichung G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isStandardabweichungG()
	 * @generated
	 * @ordered
	 */
	protected static final boolean STANDARDABWEICHUNG_G_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isStandardabweichungG() <em>Standardabweichung G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isStandardabweichungG()
	 * @generated
	 * @ordered
	 */
	protected boolean standardabweichungG = STANDARDABWEICHUNG_G_EDEFAULT;

	/**
	 * The default value of the '{@link #getTimes10PowerOf() <em>Times10 Power Of</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimes10PowerOf()
	 * @generated
	 * @ordered
	 */
	protected static final double TIMES10_POWER_OF_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getTimes10PowerOf() <em>Times10 Power Of</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimes10PowerOf()
	 * @generated
	 * @ordered
	 */
	protected double times10PowerOf = TIMES10_POWER_OF_EDEFAULT;

	/**
	 * The default value of the '{@link #getUcl95() <em>Ucl95</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUcl95()
	 * @generated
	 * @ordered
	 */
	protected static final double UCL95_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getUcl95() <em>Ucl95</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUcl95()
	 * @generated
	 * @ordered
	 */
	protected double ucl95 = UCL95_EDEFAULT;

	/**
	 * The default value of the '{@link #isUcl95G() <em>Ucl95 G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUcl95G()
	 * @generated
	 * @ordered
	 */
	protected static final boolean UCL95_G_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUcl95G() <em>Ucl95 G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUcl95G()
	 * @generated
	 * @ordered
	 */
	protected boolean ucl95G = UCL95_G_EDEFAULT;

	/**
	 * The default value of the '{@link #isUndefinedND() <em>Undefined ND</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUndefinedND()
	 * @generated
	 * @ordered
	 */
	protected static final boolean UNDEFINED_ND_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUndefinedND() <em>Undefined ND</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUndefinedND()
	 * @generated
	 * @ordered
	 */
	protected boolean undefinedND = UNDEFINED_ND_EDEFAULT;

	/**
	 * The default value of the '{@link #getVerteilung() <em>Verteilung</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVerteilung()
	 * @generated
	 * @ordered
	 */
	protected static final String VERTEILUNG_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVerteilung() <em>Verteilung</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVerteilung()
	 * @generated
	 * @ordered
	 */
	protected String verteilung = VERTEILUNG_EDEFAULT;

	/**
	 * The default value of the '{@link #isVerteilungG() <em>Verteilung G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVerteilungG()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VERTEILUNG_G_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isVerteilungG() <em>Verteilung G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVerteilungG()
	 * @generated
	 * @ordered
	 */
	protected boolean verteilungG = VERTEILUNG_G_EDEFAULT;

	/**
	 * The default value of the '{@link #getWert() <em>Wert</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWert()
	 * @generated
	 * @ordered
	 */
	protected static final double WERT_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getWert() <em>Wert</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWert()
	 * @generated
	 * @ordered
	 */
	protected double wert = WERT_EDEFAULT;

	/**
	 * The default value of the '{@link #isWertG() <em>Wert G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWertG()
	 * @generated
	 * @ordered
	 */
	protected static final boolean WERT_G_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isWertG() <em>Wert G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWertG()
	 * @generated
	 * @ordered
	 */
	protected boolean wertG = WERT_G_EDEFAULT;

	/**
	 * The default value of the '{@link #getWertTyp() <em>Wert Typ</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWertTyp()
	 * @generated
	 * @ordered
	 */
	protected static final int WERT_TYP_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getWertTyp() <em>Wert Typ</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWertTyp()
	 * @generated
	 * @ordered
	 */
	protected int wertTyp = WERT_TYP_EDEFAULT;

	/**
	 * The default value of the '{@link #getWiederholungen() <em>Wiederholungen</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWiederholungen()
	 * @generated
	 * @ordered
	 */
	protected static final double WIEDERHOLUNGEN_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getWiederholungen() <em>Wiederholungen</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWiederholungen()
	 * @generated
	 * @ordered
	 */
	protected double wiederholungen = WIEDERHOLUNGEN_EDEFAULT;

	/**
	 * The default value of the '{@link #isWiederholungenG() <em>Wiederholungen G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWiederholungenG()
	 * @generated
	 * @ordered
	 */
	protected static final boolean WIEDERHOLUNGEN_G_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isWiederholungenG() <em>Wiederholungen G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isWiederholungenG()
	 * @generated
	 * @ordered
	 */
	protected boolean wiederholungenG = WIEDERHOLUNGEN_G_EDEFAULT;

	/**
	 * The default value of the '{@link #getX() <em>X</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getX()
	 * @generated
	 * @ordered
	 */
	protected static final String X_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getX() <em>X</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getX()
	 * @generated
	 * @ordered
	 */
	protected String x = X_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DoubleKennzahlenImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BfrPackage.Literals.DOUBLE_KENNZAHLEN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFunctionX() {
		return functionX;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFunctionX(String newFunctionX) {
		String oldFunctionX = functionX;
		functionX = newFunctionX;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_X, oldFunctionX, functionX));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFunctionXG() {
		return functionXG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFunctionXG(boolean newFunctionXG) {
		boolean oldFunctionXG = functionXG;
		functionXG = newFunctionXG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_XG, oldFunctionXG, functionXG));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFunctionZeit() {
		return functionZeit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFunctionZeit(String newFunctionZeit) {
		String oldFunctionZeit = functionZeit;
		functionZeit = newFunctionZeit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_ZEIT, oldFunctionZeit, functionZeit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFunctionZeitG() {
		return functionZeitG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFunctionZeitG(boolean newFunctionZeitG) {
		boolean oldFunctionZeitG = functionZeitG;
		functionZeitG = newFunctionZeitG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_ZEIT_G, oldFunctionZeitG, functionZeitG));
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
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getLcl95() {
		return lcl95;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLcl95(double newLcl95) {
		double oldLcl95 = lcl95;
		lcl95 = newLcl95;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__LCL95, oldLcl95, lcl95));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLcl95G() {
		return lcl95G;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLcl95G(boolean newLcl95G) {
		boolean oldLcl95G = lcl95G;
		lcl95G = newLcl95G;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__LCL95_G, oldLcl95G, lcl95G));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMaximum() {
		return maximum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaximum(double newMaximum) {
		double oldMaximum = maximum;
		maximum = newMaximum;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__MAXIMUM, oldMaximum, maximum));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isMaximumG() {
		return maximumG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaximumG(boolean newMaximumG) {
		boolean oldMaximumG = maximumG;
		maximumG = newMaximumG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__MAXIMUM_G, oldMaximumG, maximumG));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMedian() {
		return median;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMedian(double newMedian) {
		double oldMedian = median;
		median = newMedian;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__MEDIAN, oldMedian, median));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isMedianG() {
		return medianG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMedianG(boolean newMedianG) {
		boolean oldMedianG = medianG;
		medianG = newMedianG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__MEDIAN_G, oldMedianG, medianG));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMinimum() {
		return minimum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinimum(double newMinimum) {
		double oldMinimum = minimum;
		minimum = newMinimum;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__MINIMUM, oldMinimum, minimum));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isMinimumG() {
		return minimumG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinimumG(boolean newMinimumG) {
		boolean oldMinimumG = minimumG;
		minimumG = newMinimumG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__MINIMUM_G, oldMinimumG, minimumG));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getStandardabweichung() {
		return standardabweichung;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStandardabweichung(double newStandardabweichung) {
		double oldStandardabweichung = standardabweichung;
		standardabweichung = newStandardabweichung;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG, oldStandardabweichung, standardabweichung));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isStandardabweichungG() {
		return standardabweichungG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStandardabweichungG(boolean newStandardabweichungG) {
		boolean oldStandardabweichungG = standardabweichungG;
		standardabweichungG = newStandardabweichungG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG_G, oldStandardabweichungG, standardabweichungG));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getTimes10PowerOf() {
		return times10PowerOf;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTimes10PowerOf(double newTimes10PowerOf) {
		double oldTimes10PowerOf = times10PowerOf;
		times10PowerOf = newTimes10PowerOf;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__TIMES10_POWER_OF, oldTimes10PowerOf, times10PowerOf));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getUcl95() {
		return ucl95;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUcl95(double newUcl95) {
		double oldUcl95 = ucl95;
		ucl95 = newUcl95;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__UCL95, oldUcl95, ucl95));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUcl95G() {
		return ucl95G;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUcl95G(boolean newUcl95G) {
		boolean oldUcl95G = ucl95G;
		ucl95G = newUcl95G;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__UCL95_G, oldUcl95G, ucl95G));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUndefinedND() {
		return undefinedND;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUndefinedND(boolean newUndefinedND) {
		boolean oldUndefinedND = undefinedND;
		undefinedND = newUndefinedND;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__UNDEFINED_ND, oldUndefinedND, undefinedND));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVerteilung() {
		return verteilung;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVerteilung(String newVerteilung) {
		String oldVerteilung = verteilung;
		verteilung = newVerteilung;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__VERTEILUNG, oldVerteilung, verteilung));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isVerteilungG() {
		return verteilungG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVerteilungG(boolean newVerteilungG) {
		boolean oldVerteilungG = verteilungG;
		verteilungG = newVerteilungG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__VERTEILUNG_G, oldVerteilungG, verteilungG));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getWert() {
		return wert;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWert(double newWert) {
		double oldWert = wert;
		wert = newWert;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__WERT, oldWert, wert));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isWertG() {
		return wertG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWertG(boolean newWertG) {
		boolean oldWertG = wertG;
		wertG = newWertG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__WERT_G, oldWertG, wertG));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getWertTyp() {
		return wertTyp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWertTyp(int newWertTyp) {
		int oldWertTyp = wertTyp;
		wertTyp = newWertTyp;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__WERT_TYP, oldWertTyp, wertTyp));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getWiederholungen() {
		return wiederholungen;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWiederholungen(double newWiederholungen) {
		double oldWiederholungen = wiederholungen;
		wiederholungen = newWiederholungen;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN, oldWiederholungen, wiederholungen));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isWiederholungenG() {
		return wiederholungenG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWiederholungenG(boolean newWiederholungenG) {
		boolean oldWiederholungenG = wiederholungenG;
		wiederholungenG = newWiederholungenG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN_G, oldWiederholungenG, wiederholungenG));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getX() {
		return x;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setX(String newX) {
		String oldX = x;
		x = newX;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BfrPackage.DOUBLE_KENNZAHLEN__X, oldX, x));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_X:
				return getFunctionX();
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_XG:
				return isFunctionXG();
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_ZEIT:
				return getFunctionZeit();
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_ZEIT_G:
				return isFunctionZeitG();
			case BfrPackage.DOUBLE_KENNZAHLEN__ID:
				return getId();
			case BfrPackage.DOUBLE_KENNZAHLEN__LCL95:
				return getLcl95();
			case BfrPackage.DOUBLE_KENNZAHLEN__LCL95_G:
				return isLcl95G();
			case BfrPackage.DOUBLE_KENNZAHLEN__MAXIMUM:
				return getMaximum();
			case BfrPackage.DOUBLE_KENNZAHLEN__MAXIMUM_G:
				return isMaximumG();
			case BfrPackage.DOUBLE_KENNZAHLEN__MEDIAN:
				return getMedian();
			case BfrPackage.DOUBLE_KENNZAHLEN__MEDIAN_G:
				return isMedianG();
			case BfrPackage.DOUBLE_KENNZAHLEN__MINIMUM:
				return getMinimum();
			case BfrPackage.DOUBLE_KENNZAHLEN__MINIMUM_G:
				return isMinimumG();
			case BfrPackage.DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG:
				return getStandardabweichung();
			case BfrPackage.DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG_G:
				return isStandardabweichungG();
			case BfrPackage.DOUBLE_KENNZAHLEN__TIMES10_POWER_OF:
				return getTimes10PowerOf();
			case BfrPackage.DOUBLE_KENNZAHLEN__UCL95:
				return getUcl95();
			case BfrPackage.DOUBLE_KENNZAHLEN__UCL95_G:
				return isUcl95G();
			case BfrPackage.DOUBLE_KENNZAHLEN__UNDEFINED_ND:
				return isUndefinedND();
			case BfrPackage.DOUBLE_KENNZAHLEN__VERTEILUNG:
				return getVerteilung();
			case BfrPackage.DOUBLE_KENNZAHLEN__VERTEILUNG_G:
				return isVerteilungG();
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT:
				return getWert();
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT_G:
				return isWertG();
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT_TYP:
				return getWertTyp();
			case BfrPackage.DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN:
				return getWiederholungen();
			case BfrPackage.DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN_G:
				return isWiederholungenG();
			case BfrPackage.DOUBLE_KENNZAHLEN__X:
				return getX();
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
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_X:
				setFunctionX((String)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_XG:
				setFunctionXG((Boolean)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_ZEIT:
				setFunctionZeit((String)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_ZEIT_G:
				setFunctionZeitG((Boolean)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__ID:
				setId((Integer)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__LCL95:
				setLcl95((Double)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__LCL95_G:
				setLcl95G((Boolean)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__MAXIMUM:
				setMaximum((Double)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__MAXIMUM_G:
				setMaximumG((Boolean)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__MEDIAN:
				setMedian((Double)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__MEDIAN_G:
				setMedianG((Boolean)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__MINIMUM:
				setMinimum((Double)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__MINIMUM_G:
				setMinimumG((Boolean)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG:
				setStandardabweichung((Double)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG_G:
				setStandardabweichungG((Boolean)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__TIMES10_POWER_OF:
				setTimes10PowerOf((Double)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__UCL95:
				setUcl95((Double)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__UCL95_G:
				setUcl95G((Boolean)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__UNDEFINED_ND:
				setUndefinedND((Boolean)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__VERTEILUNG:
				setVerteilung((String)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__VERTEILUNG_G:
				setVerteilungG((Boolean)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT:
				setWert((Double)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT_G:
				setWertG((Boolean)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT_TYP:
				setWertTyp((Integer)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN:
				setWiederholungen((Double)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN_G:
				setWiederholungenG((Boolean)newValue);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__X:
				setX((String)newValue);
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
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_X:
				setFunctionX(FUNCTION_X_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_XG:
				setFunctionXG(FUNCTION_XG_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_ZEIT:
				setFunctionZeit(FUNCTION_ZEIT_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_ZEIT_G:
				setFunctionZeitG(FUNCTION_ZEIT_G_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__ID:
				setId(ID_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__LCL95:
				setLcl95(LCL95_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__LCL95_G:
				setLcl95G(LCL95_G_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__MAXIMUM:
				setMaximum(MAXIMUM_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__MAXIMUM_G:
				setMaximumG(MAXIMUM_G_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__MEDIAN:
				setMedian(MEDIAN_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__MEDIAN_G:
				setMedianG(MEDIAN_G_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__MINIMUM:
				setMinimum(MINIMUM_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__MINIMUM_G:
				setMinimumG(MINIMUM_G_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG:
				setStandardabweichung(STANDARDABWEICHUNG_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG_G:
				setStandardabweichungG(STANDARDABWEICHUNG_G_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__TIMES10_POWER_OF:
				setTimes10PowerOf(TIMES10_POWER_OF_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__UCL95:
				setUcl95(UCL95_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__UCL95_G:
				setUcl95G(UCL95_G_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__UNDEFINED_ND:
				setUndefinedND(UNDEFINED_ND_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__VERTEILUNG:
				setVerteilung(VERTEILUNG_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__VERTEILUNG_G:
				setVerteilungG(VERTEILUNG_G_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT:
				setWert(WERT_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT_G:
				setWertG(WERT_G_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT_TYP:
				setWertTyp(WERT_TYP_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN:
				setWiederholungen(WIEDERHOLUNGEN_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN_G:
				setWiederholungenG(WIEDERHOLUNGEN_G_EDEFAULT);
				return;
			case BfrPackage.DOUBLE_KENNZAHLEN__X:
				setX(X_EDEFAULT);
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
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_X:
				return FUNCTION_X_EDEFAULT == null ? functionX != null : !FUNCTION_X_EDEFAULT.equals(functionX);
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_XG:
				return functionXG != FUNCTION_XG_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_ZEIT:
				return FUNCTION_ZEIT_EDEFAULT == null ? functionZeit != null : !FUNCTION_ZEIT_EDEFAULT.equals(functionZeit);
			case BfrPackage.DOUBLE_KENNZAHLEN__FUNCTION_ZEIT_G:
				return functionZeitG != FUNCTION_ZEIT_G_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__ID:
				return id != ID_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__LCL95:
				return lcl95 != LCL95_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__LCL95_G:
				return lcl95G != LCL95_G_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__MAXIMUM:
				return maximum != MAXIMUM_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__MAXIMUM_G:
				return maximumG != MAXIMUM_G_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__MEDIAN:
				return median != MEDIAN_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__MEDIAN_G:
				return medianG != MEDIAN_G_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__MINIMUM:
				return minimum != MINIMUM_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__MINIMUM_G:
				return minimumG != MINIMUM_G_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG:
				return standardabweichung != STANDARDABWEICHUNG_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG_G:
				return standardabweichungG != STANDARDABWEICHUNG_G_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__TIMES10_POWER_OF:
				return times10PowerOf != TIMES10_POWER_OF_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__UCL95:
				return ucl95 != UCL95_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__UCL95_G:
				return ucl95G != UCL95_G_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__UNDEFINED_ND:
				return undefinedND != UNDEFINED_ND_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__VERTEILUNG:
				return VERTEILUNG_EDEFAULT == null ? verteilung != null : !VERTEILUNG_EDEFAULT.equals(verteilung);
			case BfrPackage.DOUBLE_KENNZAHLEN__VERTEILUNG_G:
				return verteilungG != VERTEILUNG_G_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT:
				return wert != WERT_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT_G:
				return wertG != WERT_G_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__WERT_TYP:
				return wertTyp != WERT_TYP_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN:
				return wiederholungen != WIEDERHOLUNGEN_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN_G:
				return wiederholungenG != WIEDERHOLUNGEN_G_EDEFAULT;
			case BfrPackage.DOUBLE_KENNZAHLEN__X:
				return X_EDEFAULT == null ? x != null : !X_EDEFAULT.equals(x);
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
		result.append(" (functionX: ");
		result.append(functionX);
		result.append(", functionXG: ");
		result.append(functionXG);
		result.append(", functionZeit: ");
		result.append(functionZeit);
		result.append(", functionZeitG: ");
		result.append(functionZeitG);
		result.append(", id: ");
		result.append(id);
		result.append(", lcl95: ");
		result.append(lcl95);
		result.append(", lcl95G: ");
		result.append(lcl95G);
		result.append(", maximum: ");
		result.append(maximum);
		result.append(", maximumG: ");
		result.append(maximumG);
		result.append(", median: ");
		result.append(median);
		result.append(", medianG: ");
		result.append(medianG);
		result.append(", minimum: ");
		result.append(minimum);
		result.append(", minimumG: ");
		result.append(minimumG);
		result.append(", standardabweichung: ");
		result.append(standardabweichung);
		result.append(", standardabweichungG: ");
		result.append(standardabweichungG);
		result.append(", times10PowerOf: ");
		result.append(times10PowerOf);
		result.append(", ucl95: ");
		result.append(ucl95);
		result.append(", ucl95G: ");
		result.append(ucl95G);
		result.append(", undefinedND: ");
		result.append(undefinedND);
		result.append(", verteilung: ");
		result.append(verteilung);
		result.append(", verteilungG: ");
		result.append(verteilungG);
		result.append(", wert: ");
		result.append(wert);
		result.append(", wertG: ");
		result.append(wertG);
		result.append(", wertTyp: ");
		result.append(wertTyp);
		result.append(", wiederholungen: ");
		result.append(wiederholungen);
		result.append(", wiederholungenG: ");
		result.append(wiederholungenG);
		result.append(", x: ");
		result.append(x);
		result.append(')');
		return result.toString();
	}

} //DoubleKennzahlenImpl
