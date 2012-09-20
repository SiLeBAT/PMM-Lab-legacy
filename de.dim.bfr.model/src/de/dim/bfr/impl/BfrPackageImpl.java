/**
 *
 * $Id: BfrPackageImpl.java 651 2012-01-24 09:59:12Z sdoerl $
 *******************************************************************************
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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import de.dim.bfr.BfrFactory;
import de.dim.bfr.BfrPackage;
import de.dim.bfr.DoubleKennzahlen;
import de.dim.bfr.Einheiten;
import de.dim.bfr.FreigabeTyp;
import de.dim.bfr.GeschModelList;
import de.dim.bfr.GeschModellParameter;
import de.dim.bfr.GeschaetztStatistikModell;
import de.dim.bfr.KlasseTyp;
import de.dim.bfr.LevelTyp;
import de.dim.bfr.Literatur;
import de.dim.bfr.LiteraturListe;
import de.dim.bfr.LiteraturTyp;
import de.dim.bfr.Messwerte;
import de.dim.bfr.ParameterCovCor;
import de.dim.bfr.ParameterRoleType;
import de.dim.bfr.SoftwareType;
import de.dim.bfr.StatistikModell;
import de.dim.bfr.StatistikModellKatalog;
import de.dim.bfr.StatistikModellParameter;
import de.dim.bfr.VersuchsBedingung;
import de.dim.bfr.VersuchsBedingungList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class BfrPackageImpl extends EPackageImpl implements BfrPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass literaturEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass statistikModellEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass statistikModellParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass statistikModellKatalogEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass literaturListeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass geschaetztStatistikModellEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass geschModellParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass versuchsBedingungEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass geschModelListEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass versuchsBedingungListEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass messwerteEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass doubleKennzahlenEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parameterCovCorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass einheitenEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum freigabeTypEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum literaturTypEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum levelTypEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum klasseTypEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum softwareTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum parameterRoleTypeEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see de.dim.bfr.BfrPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private BfrPackageImpl() {
		super(eNS_URI, BfrFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link BfrPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static BfrPackage init() {
		if (isInited) return (BfrPackage)EPackage.Registry.INSTANCE.getEPackage(BfrPackage.eNS_URI);

		// Obtain or create and register package
		BfrPackageImpl theBfrPackage = (BfrPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof BfrPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new BfrPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theBfrPackage.createPackageContents();

		// Initialize created meta-data
		theBfrPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theBfrPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(BfrPackage.eNS_URI, theBfrPackage);
		return theBfrPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLiteratur() {
		return literaturEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteratur_Id() {
		return (EAttribute)literaturEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteratur_Erstautor() {
		return (EAttribute)literaturEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteratur_Jahr() {
		return (EAttribute)literaturEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteratur_Titel() {
		return (EAttribute)literaturEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteratur_LiteraturAbstract() {
		return (EAttribute)literaturEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteratur_Journal() {
		return (EAttribute)literaturEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteratur_Volume() {
		return (EAttribute)literaturEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteratur_Issue() {
		return (EAttribute)literaturEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteratur_Seite() {
		return (EAttribute)literaturEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteratur_FreigabeModus() {
		return (EAttribute)literaturEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteratur_Webseite() {
		return (EAttribute)literaturEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteratur_LiteraturTyp() {
		return (EAttribute)literaturEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteratur_Paper() {
		return (EAttribute)literaturEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLiteratur_Kommentar() {
		return (EAttribute)literaturEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStatistikModell() {
		return statistikModellEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModell_Id() {
		return (EAttribute)statistikModellEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModell_Name() {
		return (EAttribute)statistikModellEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModell_Notation() {
		return (EAttribute)statistikModellEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModell_Level() {
		return (EAttribute)statistikModellEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModell_Klasse() {
		return (EAttribute)statistikModellEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModell_Typ() {
		return (EAttribute)statistikModellEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModell_Eingabedatum() {
		return (EAttribute)statistikModellEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModell_Benutzer() {
		return (EAttribute)statistikModellEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModell_Beschreibung() {
		return (EAttribute)statistikModellEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModell_Formel() {
		return (EAttribute)statistikModellEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModell_Software() {
		return (EAttribute)statistikModellEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModell_Kommentar() {
		return (EAttribute)statistikModellEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStatistikModell_Parameter() {
		return (EReference)statistikModellEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStatistikModell_Literatur() {
		return (EReference)statistikModellEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStatistikModellParameter() {
		return statistikModellParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModellParameter_Id() {
		return (EAttribute)statistikModellParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModellParameter_Name() {
		return (EAttribute)statistikModellParameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModellParameter_Min() {
		return (EAttribute)statistikModellParameterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModellParameter_Max() {
		return (EAttribute)statistikModellParameterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModellParameter_Beschreibung() {
		return (EAttribute)statistikModellParameterEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModellParameter_Role() {
		return (EAttribute)statistikModellParameterEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStatistikModellParameter_Integer() {
		return (EAttribute)statistikModellParameterEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStatistikModellKatalog() {
		return statistikModellKatalogEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStatistikModellKatalog_Modelle() {
		return (EReference)statistikModellKatalogEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLiteraturListe() {
		return literaturListeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLiteraturListe_Literatur() {
		return (EReference)literaturListeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGeschaetztStatistikModell() {
		return geschaetztStatistikModellEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeschaetztStatistikModell_StatistikModel() {
		return (EReference)geschaetztStatistikModellEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeschaetztStatistikModell_ManuellEingetragen() {
		return (EAttribute)geschaetztStatistikModellEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeschaetztStatistikModell_RSquared() {
		return (EAttribute)geschaetztStatistikModellEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeschaetztStatistikModell_Rss() {
		return (EAttribute)geschaetztStatistikModellEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeschaetztStatistikModell_Score() {
		return (EAttribute)geschaetztStatistikModellEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeschaetztStatistikModell_Kommentar() {
		return (EAttribute)geschaetztStatistikModellEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeschaetztStatistikModell_Id() {
		return (EAttribute)geschaetztStatistikModellEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeschaetztStatistikModell_Parameter() {
		return (EReference)geschaetztStatistikModellEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeschaetztStatistikModell_Bedingung() {
		return (EReference)geschaetztStatistikModellEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeschaetztStatistikModell_Literatur() {
		return (EReference)geschaetztStatistikModellEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeschaetztStatistikModell_ParameterCovCor() {
		return (EReference)geschaetztStatistikModellEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeschaetztStatistikModell_Response() {
		return (EReference)geschaetztStatistikModellEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGeschModellParameter() {
		return geschModellParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeschModellParameter_Name() {
		return (EAttribute)geschModellParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeschModellParameter_Sd() {
		return (EAttribute)geschModellParameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeschModellParameter_Wert() {
		return (EAttribute)geschModellParameterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeschModellParameter_VersuchsBedingung() {
		return (EReference)geschModellParameterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeschModellParameter_Id() {
		return (EAttribute)geschModellParameterEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeschModellParameter_T() {
		return (EAttribute)geschModellParameterEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeschModellParameter_P() {
		return (EAttribute)geschModellParameterEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeschModellParameter_GeschaetztesModell() {
		return (EReference)geschModellParameterEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeschModellParameter_ModelParameter() {
		return (EReference)geschModellParameterEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeschModellParameter_KiOben() {
		return (EAttribute)geschModellParameterEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getGeschModellParameter_KiUnten() {
		return (EAttribute)geschModellParameterEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVersuchsBedingung() {
		return versuchsBedingungEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVersuchsBedingung_Id() {
		return (EAttribute)versuchsBedingungEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVersuchsBedingung_IdCB() {
		return (EAttribute)versuchsBedingungEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGeschModelList() {
		return geschModelListEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGeschModelList_Models() {
		return (EReference)geschModelListEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVersuchsBedingungList() {
		return versuchsBedingungListEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getVersuchsBedingungList_Bedingungen() {
		return (EReference)versuchsBedingungListEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMesswerte() {
		return messwerteEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMesswerte_Geprueft() {
		return (EAttribute)messwerteEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMesswerte_Id() {
		return (EAttribute)messwerteEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMesswerte_Kommentar() {
		return (EAttribute)messwerteEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMesswerte_ZeitEinheit() {
		return (EAttribute)messwerteEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMesswerte_Aw() {
		return (EReference)messwerteEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMesswerte_Co2() {
		return (EReference)messwerteEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMesswerte_Druck() {
		return (EReference)messwerteEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMesswerte_Temperatur() {
		return (EReference)messwerteEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMesswerte_PH() {
		return (EReference)messwerteEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMesswerte_Konzentration() {
		return (EReference)messwerteEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMesswerte_Zeit() {
		return (EReference)messwerteEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMesswerte_Sonstiges() {
		return (EReference)messwerteEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMesswerte_Versuchsbedingungen() {
		return (EReference)messwerteEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMesswerte_KonzEinheit() {
		return (EReference)messwerteEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDoubleKennzahlen() {
		return doubleKennzahlenEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_FunctionX() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_FunctionXG() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_FunctionZeit() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_FunctionZeitG() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_Id() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_Lcl95() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_Lcl95G() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_Maximum() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_MaximumG() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_Median() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_MedianG() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_Minimum() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_MinimumG() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_Standardabweichung() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_StandardabweichungG() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_Times10PowerOf() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_Ucl95() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(16);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_Ucl95G() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(17);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_UndefinedND() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(18);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_Verteilung() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(19);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_VerteilungG() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(20);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_Wert() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(21);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_WertG() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(22);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_WertTyp() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(23);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_Wiederholungen() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(24);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_WiederholungenG() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(25);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDoubleKennzahlen_X() {
		return (EAttribute)doubleKennzahlenEClass.getEStructuralFeatures().get(26);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParameterCovCor() {
		return parameterCovCorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameterCovCor_Parameter1() {
		return (EReference)parameterCovCorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameterCovCor_Parameter2() {
		return (EReference)parameterCovCorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameterCovCor_Value() {
		return (EAttribute)parameterCovCorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameterCovCor_Cor() {
		return (EAttribute)parameterCovCorEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameterCovCor_Id() {
		return (EAttribute)parameterCovCorEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEinheiten() {
		return einheitenEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEinheiten_Id() {
		return (EAttribute)einheitenEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEinheiten_Einheit() {
		return (EAttribute)einheitenEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEinheiten_Beschreibung() {
		return (EAttribute)einheitenEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getFreigabeTyp() {
		return freigabeTypEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getLiteraturTyp() {
		return literaturTypEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getLevelTyp() {
		return levelTypEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getKlasseTyp() {
		return klasseTypEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSoftwareType() {
		return softwareTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getParameterRoleType() {
		return parameterRoleTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BfrFactory getBfrFactory() {
		return (BfrFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		literaturEClass = createEClass(LITERATUR);
		createEAttribute(literaturEClass, LITERATUR__ID);
		createEAttribute(literaturEClass, LITERATUR__ERSTAUTOR);
		createEAttribute(literaturEClass, LITERATUR__JAHR);
		createEAttribute(literaturEClass, LITERATUR__TITEL);
		createEAttribute(literaturEClass, LITERATUR__LITERATUR_ABSTRACT);
		createEAttribute(literaturEClass, LITERATUR__JOURNAL);
		createEAttribute(literaturEClass, LITERATUR__VOLUME);
		createEAttribute(literaturEClass, LITERATUR__ISSUE);
		createEAttribute(literaturEClass, LITERATUR__FREIGABE_MODUS);
		createEAttribute(literaturEClass, LITERATUR__WEBSEITE);
		createEAttribute(literaturEClass, LITERATUR__LITERATUR_TYP);
		createEAttribute(literaturEClass, LITERATUR__PAPER);
		createEAttribute(literaturEClass, LITERATUR__KOMMENTAR);
		createEAttribute(literaturEClass, LITERATUR__SEITE);

		statistikModellEClass = createEClass(STATISTIK_MODELL);
		createEAttribute(statistikModellEClass, STATISTIK_MODELL__ID);
		createEAttribute(statistikModellEClass, STATISTIK_MODELL__NAME);
		createEAttribute(statistikModellEClass, STATISTIK_MODELL__NOTATION);
		createEAttribute(statistikModellEClass, STATISTIK_MODELL__LEVEL);
		createEAttribute(statistikModellEClass, STATISTIK_MODELL__KLASSE);
		createEAttribute(statistikModellEClass, STATISTIK_MODELL__TYP);
		createEAttribute(statistikModellEClass, STATISTIK_MODELL__EINGABEDATUM);
		createEAttribute(statistikModellEClass, STATISTIK_MODELL__BENUTZER);
		createEAttribute(statistikModellEClass, STATISTIK_MODELL__BESCHREIBUNG);
		createEAttribute(statistikModellEClass, STATISTIK_MODELL__FORMEL);
		createEAttribute(statistikModellEClass, STATISTIK_MODELL__SOFTWARE);
		createEAttribute(statistikModellEClass, STATISTIK_MODELL__KOMMENTAR);
		createEReference(statistikModellEClass, STATISTIK_MODELL__PARAMETER);
		createEReference(statistikModellEClass, STATISTIK_MODELL__LITERATUR);

		statistikModellParameterEClass = createEClass(STATISTIK_MODELL_PARAMETER);
		createEAttribute(statistikModellParameterEClass, STATISTIK_MODELL_PARAMETER__ID);
		createEAttribute(statistikModellParameterEClass, STATISTIK_MODELL_PARAMETER__NAME);
		createEAttribute(statistikModellParameterEClass, STATISTIK_MODELL_PARAMETER__MIN);
		createEAttribute(statistikModellParameterEClass, STATISTIK_MODELL_PARAMETER__MAX);
		createEAttribute(statistikModellParameterEClass, STATISTIK_MODELL_PARAMETER__BESCHREIBUNG);
		createEAttribute(statistikModellParameterEClass, STATISTIK_MODELL_PARAMETER__ROLE);
		createEAttribute(statistikModellParameterEClass, STATISTIK_MODELL_PARAMETER__INTEGER);

		statistikModellKatalogEClass = createEClass(STATISTIK_MODELL_KATALOG);
		createEReference(statistikModellKatalogEClass, STATISTIK_MODELL_KATALOG__MODELLE);

		literaturListeEClass = createEClass(LITERATUR_LISTE);
		createEReference(literaturListeEClass, LITERATUR_LISTE__LITERATUR);

		geschaetztStatistikModellEClass = createEClass(GESCHAETZT_STATISTIK_MODELL);
		createEReference(geschaetztStatistikModellEClass, GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL);
		createEAttribute(geschaetztStatistikModellEClass, GESCHAETZT_STATISTIK_MODELL__MANUELL_EINGETRAGEN);
		createEAttribute(geschaetztStatistikModellEClass, GESCHAETZT_STATISTIK_MODELL__RSQUARED);
		createEAttribute(geschaetztStatistikModellEClass, GESCHAETZT_STATISTIK_MODELL__RSS);
		createEAttribute(geschaetztStatistikModellEClass, GESCHAETZT_STATISTIK_MODELL__SCORE);
		createEAttribute(geschaetztStatistikModellEClass, GESCHAETZT_STATISTIK_MODELL__KOMMENTAR);
		createEAttribute(geschaetztStatistikModellEClass, GESCHAETZT_STATISTIK_MODELL__ID);
		createEReference(geschaetztStatistikModellEClass, GESCHAETZT_STATISTIK_MODELL__PARAMETER);
		createEReference(geschaetztStatistikModellEClass, GESCHAETZT_STATISTIK_MODELL__BEDINGUNG);
		createEReference(geschaetztStatistikModellEClass, GESCHAETZT_STATISTIK_MODELL__LITERATUR);
		createEReference(geschaetztStatistikModellEClass, GESCHAETZT_STATISTIK_MODELL__PARAMETER_COV_COR);
		createEReference(geschaetztStatistikModellEClass, GESCHAETZT_STATISTIK_MODELL__RESPONSE);

		geschModellParameterEClass = createEClass(GESCH_MODELL_PARAMETER);
		createEAttribute(geschModellParameterEClass, GESCH_MODELL_PARAMETER__NAME);
		createEAttribute(geschModellParameterEClass, GESCH_MODELL_PARAMETER__SD);
		createEAttribute(geschModellParameterEClass, GESCH_MODELL_PARAMETER__WERT);
		createEReference(geschModellParameterEClass, GESCH_MODELL_PARAMETER__VERSUCHS_BEDINGUNG);
		createEAttribute(geschModellParameterEClass, GESCH_MODELL_PARAMETER__ID);
		createEAttribute(geschModellParameterEClass, GESCH_MODELL_PARAMETER__T);
		createEAttribute(geschModellParameterEClass, GESCH_MODELL_PARAMETER__P);
		createEReference(geschModellParameterEClass, GESCH_MODELL_PARAMETER__GESCHAETZTES_MODELL);
		createEReference(geschModellParameterEClass, GESCH_MODELL_PARAMETER__MODEL_PARAMETER);
		createEAttribute(geschModellParameterEClass, GESCH_MODELL_PARAMETER__KI_OBEN);
		createEAttribute(geschModellParameterEClass, GESCH_MODELL_PARAMETER__KI_UNTEN);

		versuchsBedingungEClass = createEClass(VERSUCHS_BEDINGUNG);
		createEAttribute(versuchsBedingungEClass, VERSUCHS_BEDINGUNG__ID);
		createEAttribute(versuchsBedingungEClass, VERSUCHS_BEDINGUNG__ID_CB);

		geschModelListEClass = createEClass(GESCH_MODEL_LIST);
		createEReference(geschModelListEClass, GESCH_MODEL_LIST__MODELS);

		versuchsBedingungListEClass = createEClass(VERSUCHS_BEDINGUNG_LIST);
		createEReference(versuchsBedingungListEClass, VERSUCHS_BEDINGUNG_LIST__BEDINGUNGEN);

		messwerteEClass = createEClass(MESSWERTE);
		createEAttribute(messwerteEClass, MESSWERTE__GEPRUEFT);
		createEAttribute(messwerteEClass, MESSWERTE__ID);
		createEAttribute(messwerteEClass, MESSWERTE__KOMMENTAR);
		createEAttribute(messwerteEClass, MESSWERTE__ZEIT_EINHEIT);
		createEReference(messwerteEClass, MESSWERTE__AW);
		createEReference(messwerteEClass, MESSWERTE__CO2);
		createEReference(messwerteEClass, MESSWERTE__DRUCK);
		createEReference(messwerteEClass, MESSWERTE__TEMPERATUR);
		createEReference(messwerteEClass, MESSWERTE__PH);
		createEReference(messwerteEClass, MESSWERTE__KONZENTRATION);
		createEReference(messwerteEClass, MESSWERTE__ZEIT);
		createEReference(messwerteEClass, MESSWERTE__SONSTIGES);
		createEReference(messwerteEClass, MESSWERTE__VERSUCHSBEDINGUNGEN);
		createEReference(messwerteEClass, MESSWERTE__KONZ_EINHEIT);

		doubleKennzahlenEClass = createEClass(DOUBLE_KENNZAHLEN);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__FUNCTION_X);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__FUNCTION_XG);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__FUNCTION_ZEIT);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__FUNCTION_ZEIT_G);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__ID);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__LCL95);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__LCL95_G);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__MAXIMUM);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__MAXIMUM_G);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__MEDIAN);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__MEDIAN_G);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__MINIMUM);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__MINIMUM_G);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG_G);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__TIMES10_POWER_OF);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__UCL95);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__UCL95_G);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__UNDEFINED_ND);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__VERTEILUNG);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__VERTEILUNG_G);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__WERT);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__WERT_G);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__WERT_TYP);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN_G);
		createEAttribute(doubleKennzahlenEClass, DOUBLE_KENNZAHLEN__X);

		parameterCovCorEClass = createEClass(PARAMETER_COV_COR);
		createEReference(parameterCovCorEClass, PARAMETER_COV_COR__PARAMETER1);
		createEReference(parameterCovCorEClass, PARAMETER_COV_COR__PARAMETER2);
		createEAttribute(parameterCovCorEClass, PARAMETER_COV_COR__VALUE);
		createEAttribute(parameterCovCorEClass, PARAMETER_COV_COR__COR);
		createEAttribute(parameterCovCorEClass, PARAMETER_COV_COR__ID);

		einheitenEClass = createEClass(EINHEITEN);
		createEAttribute(einheitenEClass, EINHEITEN__ID);
		createEAttribute(einheitenEClass, EINHEITEN__EINHEIT);
		createEAttribute(einheitenEClass, EINHEITEN__BESCHREIBUNG);

		// Create enums
		freigabeTypEEnum = createEEnum(FREIGABE_TYP);
		literaturTypEEnum = createEEnum(LITERATUR_TYP);
		levelTypEEnum = createEEnum(LEVEL_TYP);
		klasseTypEEnum = createEEnum(KLASSE_TYP);
		softwareTypeEEnum = createEEnum(SOFTWARE_TYPE);
		parameterRoleTypeEEnum = createEEnum(PARAMETER_ROLE_TYPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(literaturEClass, Literatur.class, "Literatur", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLiteratur_Id(), ecorePackage.getEInt(), "id", null, 1, 1, Literatur.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLiteratur_Erstautor(), ecorePackage.getEString(), "erstautor", null, 0, 1, Literatur.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLiteratur_Jahr(), ecorePackage.getEInt(), "jahr", "2000", 0, 1, Literatur.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLiteratur_Titel(), ecorePackage.getEString(), "titel", "", 0, 1, Literatur.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLiteratur_LiteraturAbstract(), ecorePackage.getEString(), "literaturAbstract", null, 0, 1, Literatur.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLiteratur_Journal(), ecorePackage.getEString(), "journal", null, 0, 1, Literatur.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLiteratur_Volume(), ecorePackage.getEString(), "volume", null, 0, 1, Literatur.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLiteratur_Issue(), ecorePackage.getEString(), "issue", null, 0, 1, Literatur.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLiteratur_FreigabeModus(), this.getFreigabeTyp(), "freigabeModus", null, 1, 1, Literatur.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLiteratur_Webseite(), ecorePackage.getEString(), "webseite", null, 0, 1, Literatur.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLiteratur_LiteraturTyp(), this.getLiteraturTyp(), "literaturTyp", null, 1, 1, Literatur.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLiteratur_Paper(), ecorePackage.getEString(), "paper", null, 0, 1, Literatur.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLiteratur_Kommentar(), ecorePackage.getEString(), "kommentar", null, 0, 1, Literatur.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLiteratur_Seite(), ecorePackage.getEIntegerObject(), "seite", null, 0, 1, Literatur.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(statistikModellEClass, StatistikModell.class, "StatistikModell", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStatistikModell_Id(), ecorePackage.getEInt(), "id", null, 1, 1, StatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModell_Name(), ecorePackage.getEString(), "name", "", 1, 1, StatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModell_Notation(), ecorePackage.getEString(), "notation", null, 1, 1, StatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModell_Level(), this.getLevelTyp(), "level", "NONE", 0, 1, StatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModell_Klasse(), this.getKlasseTyp(), "klasse", "UNKNOWN", 0, 1, StatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModell_Typ(), ecorePackage.getEString(), "typ", null, 0, 1, StatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModell_Eingabedatum(), ecorePackage.getEDate(), "eingabedatum", null, 1, 1, StatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModell_Benutzer(), ecorePackage.getEString(), "benutzer", null, 1, 1, StatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModell_Beschreibung(), ecorePackage.getEString(), "beschreibung", null, 0, 1, StatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModell_Formel(), ecorePackage.getEString(), "formel", null, 1, 1, StatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModell_Software(), this.getSoftwareType(), "software", "NONE", 0, 1, StatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModell_Kommentar(), ecorePackage.getEString(), "kommentar", null, 0, 1, StatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStatistikModell_Parameter(), this.getStatistikModellParameter(), null, "parameter", null, 0, -1, StatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStatistikModell_Literatur(), this.getLiteratur(), null, "literatur", null, 0, -1, StatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(statistikModellParameterEClass, StatistikModellParameter.class, "StatistikModellParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStatistikModellParameter_Id(), ecorePackage.getEInt(), "id", null, 1, 1, StatistikModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModellParameter_Name(), ecorePackage.getEString(), "name", null, 1, 1, StatistikModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModellParameter_Min(), ecorePackage.getEDoubleObject(), "min", null, 0, 1, StatistikModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModellParameter_Max(), ecorePackage.getEDoubleObject(), "max", null, 0, 1, StatistikModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModellParameter_Beschreibung(), ecorePackage.getEString(), "beschreibung", null, 0, 1, StatistikModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModellParameter_Role(), this.getParameterRoleType(), "role", "NONE", 0, 1, StatistikModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStatistikModellParameter_Integer(), ecorePackage.getEBoolean(), "integer", "true", 0, 1, StatistikModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(statistikModellKatalogEClass, StatistikModellKatalog.class, "StatistikModellKatalog", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStatistikModellKatalog_Modelle(), this.getStatistikModell(), null, "modelle", null, 0, -1, StatistikModellKatalog.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(literaturListeEClass, LiteraturListe.class, "LiteraturListe", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getLiteraturListe_Literatur(), this.getLiteratur(), null, "literatur", null, 0, -1, LiteraturListe.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(geschaetztStatistikModellEClass, GeschaetztStatistikModell.class, "GeschaetztStatistikModell", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGeschaetztStatistikModell_StatistikModel(), this.getStatistikModell(), null, "statistikModel", null, 1, 1, GeschaetztStatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeschaetztStatistikModell_ManuellEingetragen(), ecorePackage.getEBoolean(), "manuellEingetragen", "false", 0, 1, GeschaetztStatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeschaetztStatistikModell_RSquared(), ecorePackage.getEDouble(), "rSquared", null, 0, 1, GeschaetztStatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeschaetztStatistikModell_Rss(), ecorePackage.getEDouble(), "rss", null, 0, 1, GeschaetztStatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeschaetztStatistikModell_Score(), ecorePackage.getEInt(), "score", null, 0, 1, GeschaetztStatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeschaetztStatistikModell_Kommentar(), ecorePackage.getEString(), "kommentar", null, 0, 1, GeschaetztStatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeschaetztStatistikModell_Id(), ecorePackage.getEInt(), "id", null, 1, 1, GeschaetztStatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGeschaetztStatistikModell_Parameter(), this.getGeschModellParameter(), null, "parameter", null, 0, -1, GeschaetztStatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGeschaetztStatistikModell_Bedingung(), this.getVersuchsBedingung(), null, "bedingung", null, 0, 1, GeschaetztStatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGeschaetztStatistikModell_Literatur(), this.getLiteratur(), null, "literatur", null, 0, -1, GeschaetztStatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGeschaetztStatistikModell_ParameterCovCor(), this.getParameterCovCor(), null, "parameterCovCor", null, 0, -1, GeschaetztStatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGeschaetztStatistikModell_Response(), this.getStatistikModellParameter(), null, "response", null, 0, 1, GeschaetztStatistikModell.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(geschModellParameterEClass, GeschModellParameter.class, "GeschModellParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGeschModellParameter_Name(), ecorePackage.getEString(), "name", null, 1, 1, GeschModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeschModellParameter_Sd(), ecorePackage.getEDoubleObject(), "sd", null, 0, 1, GeschModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeschModellParameter_Wert(), ecorePackage.getEDoubleObject(), "wert", null, 0, 1, GeschModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGeschModellParameter_VersuchsBedingung(), this.getVersuchsBedingung(), null, "versuchsBedingung", null, 1, 1, GeschModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeschModellParameter_Id(), ecorePackage.getEInt(), "id", null, 1, 1, GeschModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeschModellParameter_T(), ecorePackage.getEDoubleObject(), "t", null, 0, 1, GeschModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeschModellParameter_P(), ecorePackage.getEDoubleObject(), "p", null, 0, 1, GeschModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGeschModellParameter_GeschaetztesModell(), this.getGeschaetztStatistikModell(), null, "geschaetztesModell", null, 1, 1, GeschModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGeschModellParameter_ModelParameter(), this.getStatistikModellParameter(), null, "modelParameter", null, 1, 1, GeschModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeschModellParameter_KiOben(), ecorePackage.getEDouble(), "kiOben", null, 0, 1, GeschModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeschModellParameter_KiUnten(), ecorePackage.getEDouble(), "kiUnten", null, 0, 1, GeschModellParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(versuchsBedingungEClass, VersuchsBedingung.class, "VersuchsBedingung", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVersuchsBedingung_Id(), ecorePackage.getEInt(), "id", null, 1, 1, VersuchsBedingung.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVersuchsBedingung_IdCB(), ecorePackage.getEString(), "idCB", null, 1, 1, VersuchsBedingung.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(geschModelListEClass, GeschModelList.class, "GeschModelList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGeschModelList_Models(), this.getGeschaetztStatistikModell(), null, "models", null, 0, -1, GeschModelList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(versuchsBedingungListEClass, VersuchsBedingungList.class, "VersuchsBedingungList", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVersuchsBedingungList_Bedingungen(), this.getVersuchsBedingung(), null, "bedingungen", null, 0, -1, VersuchsBedingungList.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(messwerteEClass, Messwerte.class, "Messwerte", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMesswerte_Geprueft(), ecorePackage.getEBoolean(), "geprueft", null, 0, 1, Messwerte.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMesswerte_Id(), ecorePackage.getEInt(), "id", null, 0, 1, Messwerte.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMesswerte_Kommentar(), ecorePackage.getEString(), "kommentar", null, 0, 1, Messwerte.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMesswerte_ZeitEinheit(), ecorePackage.getEString(), "zeitEinheit", null, 0, 1, Messwerte.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMesswerte_Aw(), this.getDoubleKennzahlen(), null, "aw", null, 0, 1, Messwerte.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMesswerte_Co2(), this.getDoubleKennzahlen(), null, "co2", null, 0, 1, Messwerte.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMesswerte_Druck(), this.getDoubleKennzahlen(), null, "druck", null, 0, 1, Messwerte.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMesswerte_Temperatur(), this.getDoubleKennzahlen(), null, "temperatur", null, 0, 1, Messwerte.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMesswerte_PH(), this.getDoubleKennzahlen(), null, "pH", null, 0, 1, Messwerte.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMesswerte_Konzentration(), this.getDoubleKennzahlen(), null, "konzentration", null, 0, 1, Messwerte.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMesswerte_Zeit(), this.getDoubleKennzahlen(), null, "zeit", null, 0, 1, Messwerte.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMesswerte_Sonstiges(), ecorePackage.getEObject(), null, "sonstiges", null, 0, 1, Messwerte.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMesswerte_Versuchsbedingungen(), this.getVersuchsBedingung(), null, "versuchsbedingungen", null, 0, 1, Messwerte.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMesswerte_KonzEinheit(), this.getEinheiten(), null, "konzEinheit", null, 0, 1, Messwerte.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(doubleKennzahlenEClass, DoubleKennzahlen.class, "DoubleKennzahlen", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDoubleKennzahlen_FunctionX(), ecorePackage.getEString(), "functionX", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_FunctionXG(), ecorePackage.getEBoolean(), "functionXG", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_FunctionZeit(), ecorePackage.getEString(), "functionZeit", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_FunctionZeitG(), ecorePackage.getEBoolean(), "functionZeitG", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_Id(), ecorePackage.getEInt(), "id", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_Lcl95(), ecorePackage.getEDouble(), "lcl95", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_Lcl95G(), ecorePackage.getEBoolean(), "lcl95G", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_Maximum(), ecorePackage.getEDouble(), "maximum", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_MaximumG(), ecorePackage.getEBoolean(), "maximumG", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_Median(), ecorePackage.getEDouble(), "median", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_MedianG(), ecorePackage.getEBoolean(), "medianG", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_Minimum(), ecorePackage.getEDouble(), "minimum", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_MinimumG(), ecorePackage.getEBoolean(), "minimumG", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_Standardabweichung(), ecorePackage.getEDouble(), "standardabweichung", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_StandardabweichungG(), ecorePackage.getEBoolean(), "standardabweichungG", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_Times10PowerOf(), ecorePackage.getEDouble(), "times10PowerOf", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_Ucl95(), ecorePackage.getEDouble(), "ucl95", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_Ucl95G(), ecorePackage.getEBoolean(), "ucl95G", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_UndefinedND(), ecorePackage.getEBoolean(), "undefinedND", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_Verteilung(), ecorePackage.getEString(), "verteilung", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_VerteilungG(), ecorePackage.getEBoolean(), "verteilungG", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_Wert(), ecorePackage.getEDouble(), "wert", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_WertG(), ecorePackage.getEBoolean(), "wertG", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_WertTyp(), ecorePackage.getEInt(), "wertTyp", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_Wiederholungen(), ecorePackage.getEDouble(), "wiederholungen", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_WiederholungenG(), ecorePackage.getEBoolean(), "wiederholungenG", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDoubleKennzahlen_X(), ecorePackage.getEString(), "x", null, 0, 1, DoubleKennzahlen.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parameterCovCorEClass, ParameterCovCor.class, "ParameterCovCor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getParameterCovCor_Parameter1(), this.getGeschModellParameter(), null, "parameter1", null, 1, 1, ParameterCovCor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getParameterCovCor_Parameter2(), this.getGeschModellParameter(), null, "parameter2", null, 0, 1, ParameterCovCor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterCovCor_Value(), ecorePackage.getEDoubleObject(), "value", null, 0, 1, ParameterCovCor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterCovCor_Cor(), ecorePackage.getEBoolean(), "cor", "false", 1, 1, ParameterCovCor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameterCovCor_Id(), ecorePackage.getEInt(), "id", null, 1, 1, ParameterCovCor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(einheitenEClass, Einheiten.class, "Einheiten", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEinheiten_Id(), ecorePackage.getEInt(), "id", null, 1, 1, Einheiten.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEinheiten_Einheit(), ecorePackage.getEString(), "Einheit", null, 0, 1, Einheiten.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEinheiten_Beschreibung(), ecorePackage.getEString(), "Beschreibung", null, 0, 1, Einheiten.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(freigabeTypEEnum, FreigabeTyp.class, "FreigabeTyp");
		addEEnumLiteral(freigabeTypEEnum, FreigabeTyp.KEINE);
		addEEnumLiteral(freigabeTypEEnum, FreigabeTyp.KRISE);
		addEEnumLiteral(freigabeTypEEnum, FreigabeTyp.IMMER);

		initEEnum(literaturTypEEnum, LiteraturTyp.class, "LiteraturTyp");
		addEEnumLiteral(literaturTypEEnum, LiteraturTyp.UNBEKANNT);
		addEEnumLiteral(literaturTypEEnum, LiteraturTyp.PAPER);
		addEEnumLiteral(literaturTypEEnum, LiteraturTyp.BOOK);
		addEEnumLiteral(literaturTypEEnum, LiteraturTyp.LA);
		addEEnumLiteral(literaturTypEEnum, LiteraturTyp.MANUAL);
		addEEnumLiteral(literaturTypEEnum, LiteraturTyp.TEST_BOOK);
		addEEnumLiteral(literaturTypEEnum, LiteraturTyp.SOP);
		addEEnumLiteral(literaturTypEEnum, LiteraturTyp.WEBSITE);
		addEEnumLiteral(literaturTypEEnum, LiteraturTyp.REPORT);

		initEEnum(levelTypEEnum, LevelTyp.class, "LevelTyp");
		addEEnumLiteral(levelTypEEnum, LevelTyp.NONE);
		addEEnumLiteral(levelTypEEnum, LevelTyp.PRIMARY);
		addEEnumLiteral(levelTypEEnum, LevelTyp.SECONDARY);
		addEEnumLiteral(levelTypEEnum, LevelTyp.OTHER);

		initEEnum(klasseTypEEnum, KlasseTyp.class, "KlasseTyp");
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.UNKNOWN);
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.GROWTH);
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.INACTIVATION);
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.SURVIVAL);
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.GROWTH_INACTIVATION);
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.INACTIVATION_SURVIVAL);
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.GROWTH_SURVIVAL);
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.GROWTH_INACTIVATION_SURVIVAL);
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.T);
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.PH);
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.AW);
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.TPH);
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.TAW);
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.PH_AW);
		addEEnumLiteral(klasseTypEEnum, KlasseTyp.TPH_AW);

		initEEnum(softwareTypeEEnum, SoftwareType.class, "SoftwareType");
		addEEnumLiteral(softwareTypeEEnum, SoftwareType.NONE);
		addEEnumLiteral(softwareTypeEEnum, SoftwareType.PMP);
		addEEnumLiteral(softwareTypeEEnum, SoftwareType.COMBASE_PREDICTOR);
		addEEnumLiteral(softwareTypeEEnum, SoftwareType.R);
		addEEnumLiteral(softwareTypeEEnum, SoftwareType.OTHER);

		initEEnum(parameterRoleTypeEEnum, ParameterRoleType.class, "ParameterRoleType");
		addEEnumLiteral(parameterRoleTypeEEnum, ParameterRoleType.NONE);
		addEEnumLiteral(parameterRoleTypeEEnum, ParameterRoleType.INDEPENDENT);
		addEEnumLiteral(parameterRoleTypeEEnum, ParameterRoleType.DEPENDENT);
		addEEnumLiteral(parameterRoleTypeEEnum, ParameterRoleType.PARAMETER);

		// Create resource
		createResource(eNS_URI);
	}

} //BfrPackageImpl
