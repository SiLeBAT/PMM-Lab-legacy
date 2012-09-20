/*******************************************************************************
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
/**
 * $Id: BfrSwitch.java 651 2012-01-24 09:59:12Z sdoerl $
 */
package de.dim.bfr.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import de.dim.bfr.BfrPackage;
import de.dim.bfr.DoubleKennzahlen;
import de.dim.bfr.Einheiten;
import de.dim.bfr.GeschModelList;
import de.dim.bfr.GeschModellParameter;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.Literatur;
import de.dim.bfr.LiteraturListe;
import de.dim.bfr.Messwerte;
import de.dim.bfr.ParameterCovCor;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellKatalog;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.VersuchsBedingung;
import de.dim.bfr.VersuchsBedingungList;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see de.dim.bfr.BfrPackage
 * @generated
 */
public class BfrSwitch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static BfrPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BfrSwitch() {
		if (modelPackage == null) {
			modelPackage = BfrPackage.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public T doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List<EClass> eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch(eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case BfrPackage.LITERATUR: {
				Literatur literatur = (Literatur)theEObject;
				T result = caseLiteratur(literatur);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BfrPackage.STATISTIK_MODELL: {
				StatistikModell statistikModell = (StatistikModell)theEObject;
				T result = caseStatistikModell(statistikModell);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BfrPackage.STATISTIK_MODELL_PARAMETER: {
				StatistikModellParameter statistikModellParameter = (StatistikModellParameter)theEObject;
				T result = caseStatistikModellParameter(statistikModellParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BfrPackage.STATISTIK_MODELL_KATALOG: {
				StatistikModellKatalog statistikModellKatalog = (StatistikModellKatalog)theEObject;
				T result = caseStatistikModellKatalog(statistikModellKatalog);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BfrPackage.LITERATUR_LISTE: {
				LiteraturListe literaturListe = (LiteraturListe)theEObject;
				T result = caseLiteraturListe(literaturListe);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL: {
				GeschaetztStatistikModell geschaetztStatistikModell = (GeschaetztStatistikModell)theEObject;
				T result = caseGeschaetztStatistikModell(geschaetztStatistikModell);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BfrPackage.GESCH_MODELL_PARAMETER: {
				GeschModellParameter geschModellParameter = (GeschModellParameter)theEObject;
				T result = caseGeschModellParameter(geschModellParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BfrPackage.VERSUCHS_BEDINGUNG: {
				VersuchsBedingung versuchsBedingung = (VersuchsBedingung)theEObject;
				T result = caseVersuchsBedingung(versuchsBedingung);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BfrPackage.GESCH_MODEL_LIST: {
				GeschModelList geschModelList = (GeschModelList)theEObject;
				T result = caseGeschModelList(geschModelList);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BfrPackage.VERSUCHS_BEDINGUNG_LIST: {
				VersuchsBedingungList versuchsBedingungList = (VersuchsBedingungList)theEObject;
				T result = caseVersuchsBedingungList(versuchsBedingungList);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BfrPackage.MESSWERTE: {
				Messwerte messwerte = (Messwerte)theEObject;
				T result = caseMesswerte(messwerte);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BfrPackage.DOUBLE_KENNZAHLEN: {
				DoubleKennzahlen doubleKennzahlen = (DoubleKennzahlen)theEObject;
				T result = caseDoubleKennzahlen(doubleKennzahlen);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BfrPackage.PARAMETER_COV_COR: {
				ParameterCovCor parameterCovCor = (ParameterCovCor)theEObject;
				T result = caseParameterCovCor(parameterCovCor);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case BfrPackage.EINHEITEN: {
				Einheiten einheiten = (Einheiten)theEObject;
				T result = caseEinheiten(einheiten);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Literatur</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Literatur</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLiteratur(Literatur object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Statistik Modell</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Statistik Modell</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStatistikModell(StatistikModell object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Statistik Modell Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Statistik Modell Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStatistikModellParameter(StatistikModellParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Statistik Modell Katalog</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Statistik Modell Katalog</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStatistikModellKatalog(StatistikModellKatalog object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Literatur Liste</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Literatur Liste</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLiteraturListe(LiteraturListe object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Geschaetzt Statistik Modell</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Geschaetzt Statistik Modell</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGeschaetztStatistikModell(GeschaetztStatistikModell object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Gesch Modell Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Gesch Modell Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGeschModellParameter(GeschModellParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Versuchs Bedingung</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Versuchs Bedingung</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVersuchsBedingung(VersuchsBedingung object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Gesch Model List</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Gesch Model List</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGeschModelList(GeschModelList object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Versuchs Bedingung List</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Versuchs Bedingung List</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVersuchsBedingungList(VersuchsBedingungList object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Messwerte</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Messwerte</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMesswerte(Messwerte object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Double Kennzahlen</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Double Kennzahlen</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDoubleKennzahlen(DoubleKennzahlen object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Parameter Cov Cor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Parameter Cov Cor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseParameterCovCor(ParameterCovCor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Einheiten</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Einheiten</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEinheiten(Einheiten object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public T defaultCase(EObject object) {
		return null;
	}

} //BfrSwitch
