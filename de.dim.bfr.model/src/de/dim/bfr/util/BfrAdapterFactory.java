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
 * $Id: BfrAdapterFactory.java 651 2012-01-24 09:59:12Z sdoerl $
 */
package de.dim.bfr.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
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
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see de.dim.bfr.BfrPackage
 * @generated
 */
public class BfrAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static BfrPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BfrAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = BfrPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BfrSwitch<Adapter> modelSwitch =
		new BfrSwitch<Adapter>() {
			@Override
			public Adapter caseLiteratur(Literatur object) {
				return createLiteraturAdapter();
			}
			@Override
			public Adapter caseStatistikModell(StatistikModell object) {
				return createStatistikModellAdapter();
			}
			@Override
			public Adapter caseStatistikModellParameter(StatistikModellParameter object) {
				return createStatistikModellParameterAdapter();
			}
			@Override
			public Adapter caseStatistikModellKatalog(StatistikModellKatalog object) {
				return createStatistikModellKatalogAdapter();
			}
			@Override
			public Adapter caseLiteraturListe(LiteraturListe object) {
				return createLiteraturListeAdapter();
			}
			@Override
			public Adapter caseGeschaetztStatistikModell(GeschaetztStatistikModell object) {
				return createGeschaetztStatistikModellAdapter();
			}
			@Override
			public Adapter caseGeschModellParameter(GeschModellParameter object) {
				return createGeschModellParameterAdapter();
			}
			@Override
			public Adapter caseVersuchsBedingung(VersuchsBedingung object) {
				return createVersuchsBedingungAdapter();
			}
			@Override
			public Adapter caseGeschModelList(GeschModelList object) {
				return createGeschModelListAdapter();
			}
			@Override
			public Adapter caseVersuchsBedingungList(VersuchsBedingungList object) {
				return createVersuchsBedingungListAdapter();
			}
			@Override
			public Adapter caseMesswerte(Messwerte object) {
				return createMesswerteAdapter();
			}
			@Override
			public Adapter caseDoubleKennzahlen(DoubleKennzahlen object) {
				return createDoubleKennzahlenAdapter();
			}
			@Override
			public Adapter caseParameterCovCor(ParameterCovCor object) {
				return createParameterCovCorAdapter();
			}
			@Override
			public Adapter caseEinheiten(Einheiten object) {
				return createEinheitenAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link de.dim.bfr.Literatur <em>Literatur</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.dim.bfr.Literatur
	 * @generated
	 */
	public Adapter createLiteraturAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.dim.bfr.StatistikModell <em>Statistik Modell</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.dim.bfr.StatistikModell
	 * @generated
	 */
	public Adapter createStatistikModellAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.dim.bfr.StatistikModellParameter <em>Statistik Modell Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.dim.bfr.StatistikModellParameter
	 * @generated
	 */
	public Adapter createStatistikModellParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.dim.bfr.StatistikModellKatalog <em>Statistik Modell Katalog</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.dim.bfr.StatistikModellKatalog
	 * @generated
	 */
	public Adapter createStatistikModellKatalogAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.dim.bfr.LiteraturListe <em>Literatur Liste</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.dim.bfr.LiteraturListe
	 * @generated
	 */
	public Adapter createLiteraturListeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.dim.bfr.GeschaetztStatistikModell <em>Geschaetzt Statistik Modell</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.dim.bfr.GeschaetztStatistikModell
	 * @generated
	 */
	public Adapter createGeschaetztStatistikModellAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.dim.bfr.GeschModellParameter <em>Gesch Modell Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.dim.bfr.GeschModellParameter
	 * @generated
	 */
	public Adapter createGeschModellParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.dim.bfr.VersuchsBedingung <em>Versuchs Bedingung</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.dim.bfr.VersuchsBedingung
	 * @generated
	 */
	public Adapter createVersuchsBedingungAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.dim.bfr.GeschModelList <em>Gesch Model List</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.dim.bfr.GeschModelList
	 * @generated
	 */
	public Adapter createGeschModelListAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.dim.bfr.VersuchsBedingungList <em>Versuchs Bedingung List</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.dim.bfr.VersuchsBedingungList
	 * @generated
	 */
	public Adapter createVersuchsBedingungListAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.dim.bfr.Messwerte <em>Messwerte</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.dim.bfr.Messwerte
	 * @generated
	 */
	public Adapter createMesswerteAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.dim.bfr.DoubleKennzahlen <em>Double Kennzahlen</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.dim.bfr.DoubleKennzahlen
	 * @generated
	 */
	public Adapter createDoubleKennzahlenAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.dim.bfr.ParameterCovCor <em>Parameter Cov Cor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.dim.bfr.ParameterCovCor
	 * @generated
	 */
	public Adapter createParameterCovCorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link de.dim.bfr.Einheiten <em>Einheiten</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see de.dim.bfr.Einheiten
	 * @generated
	 */
	public Adapter createEinheitenAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //BfrAdapterFactory
