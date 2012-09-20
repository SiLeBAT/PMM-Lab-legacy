/**
 * $Id: BfrFactory.java 651 2012-01-24 09:59:12Z sdoerl $
 * /*******************************************************************************
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
package de.dim.bfr;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see de.dim.bfr.BfrPackage
 * @generated
 */
public interface BfrFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	BfrFactory eINSTANCE = de.dim.bfr.impl.BfrFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Literatur</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Literatur</em>'.
	 * @generated
	 */
	Literatur createLiteratur();

	/**
	 * Returns a new object of class '<em>Statistik Modell</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Statistik Modell</em>'.
	 * @generated
	 */
	StatistikModell createStatistikModell();

	/**
	 * Returns a new object of class '<em>Statistik Modell Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Statistik Modell Parameter</em>'.
	 * @generated
	 */
	StatistikModellParameter createStatistikModellParameter();

	/**
	 * Returns a new object of class '<em>Statistik Modell Katalog</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Statistik Modell Katalog</em>'.
	 * @generated
	 */
	StatistikModellKatalog createStatistikModellKatalog();

	/**
	 * Returns a new object of class '<em>Literatur Liste</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Literatur Liste</em>'.
	 * @generated
	 */
	LiteraturListe createLiteraturListe();

	/**
	 * Returns a new object of class '<em>Geschaetzt Statistik Modell</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Geschaetzt Statistik Modell</em>'.
	 * @generated
	 */
	GeschaetztStatistikModell createGeschaetztStatistikModell();

	/**
	 * Returns a new object of class '<em>Gesch Modell Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Gesch Modell Parameter</em>'.
	 * @generated
	 */
	GeschModellParameter createGeschModellParameter();

	/**
	 * Returns a new object of class '<em>Versuchs Bedingung</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Versuchs Bedingung</em>'.
	 * @generated
	 */
	VersuchsBedingung createVersuchsBedingung();

	/**
	 * Returns a new object of class '<em>Gesch Model List</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Gesch Model List</em>'.
	 * @generated
	 */
	GeschModelList createGeschModelList();

	/**
	 * Returns a new object of class '<em>Versuchs Bedingung List</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Versuchs Bedingung List</em>'.
	 * @generated
	 */
	VersuchsBedingungList createVersuchsBedingungList();

	/**
	 * Returns a new object of class '<em>Messwerte</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Messwerte</em>'.
	 * @generated
	 */
	Messwerte createMesswerte();

	/**
	 * Returns a new object of class '<em>Double Kennzahlen</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Double Kennzahlen</em>'.
	 * @generated
	 */
	DoubleKennzahlen createDoubleKennzahlen();

	/**
	 * Returns a new object of class '<em>Parameter Cov Cor</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parameter Cov Cor</em>'.
	 * @generated
	 */
	ParameterCovCor createParameterCovCor();

	/**
	 * Returns a new object of class '<em>Einheiten</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Einheiten</em>'.
	 * @generated
	 */
	Einheiten createEinheiten();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	BfrPackage getBfrPackage();

} //BfrFactory
