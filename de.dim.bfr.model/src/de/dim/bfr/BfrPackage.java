/**
 * $Id: BfrPackage.java 651 2012-01-24 09:59:12Z sdoerl $
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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see de.dim.bfr.BfrFactory
 * @model kind="package"
 * @generated
 */
public interface BfrPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "bfr";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://bfr.bund.de";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "bfr";

	/**
	 * The package content type ID.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eCONTENT_TYPE = "bfr_1.0";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	BfrPackage eINSTANCE = de.dim.bfr.impl.BfrPackageImpl.init();

	/**
	 * The meta object id for the '{@link de.dim.bfr.impl.LiteraturImpl <em>Literatur</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.impl.LiteraturImpl
	 * @see de.dim.bfr.impl.BfrPackageImpl#getLiteratur()
	 * @generated
	 */
	int LITERATUR = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR__ID = 0;

	/**
	 * The feature id for the '<em><b>Erstautor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR__ERSTAUTOR = 1;

	/**
	 * The feature id for the '<em><b>Jahr</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR__JAHR = 2;

	/**
	 * The feature id for the '<em><b>Titel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR__TITEL = 3;

	/**
	 * The feature id for the '<em><b>Literatur Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR__LITERATUR_ABSTRACT = 4;

	/**
	 * The feature id for the '<em><b>Journal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR__JOURNAL = 5;

	/**
	 * The feature id for the '<em><b>Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR__VOLUME = 6;

	/**
	 * The feature id for the '<em><b>Issue</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR__ISSUE = 7;

	/**
	 * The feature id for the '<em><b>Freigabe Modus</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR__FREIGABE_MODUS = 8;

	/**
	 * The feature id for the '<em><b>Webseite</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR__WEBSEITE = 9;

	/**
	 * The feature id for the '<em><b>Literatur Typ</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR__LITERATUR_TYP = 10;

	/**
	 * The feature id for the '<em><b>Paper</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR__PAPER = 11;

	/**
	 * The feature id for the '<em><b>Kommentar</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR__KOMMENTAR = 12;

	/**
	 * The feature id for the '<em><b>Seite</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR__SEITE = 13;

	/**
	 * The number of structural features of the '<em>Literatur</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR_FEATURE_COUNT = 14;

	/**
	 * The meta object id for the '{@link de.dim.bfr.impl.StatistikModellImpl <em>Statistik Modell</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.impl.StatistikModellImpl
	 * @see de.dim.bfr.impl.BfrPackageImpl#getStatistikModell()
	 * @generated
	 */
	int STATISTIK_MODELL = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL__NAME = 1;

	/**
	 * The feature id for the '<em><b>Notation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL__NOTATION = 2;

	/**
	 * The feature id for the '<em><b>Level</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL__LEVEL = 3;

	/**
	 * The feature id for the '<em><b>Klasse</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL__KLASSE = 4;

	/**
	 * The feature id for the '<em><b>Typ</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL__TYP = 5;

	/**
	 * The feature id for the '<em><b>Eingabedatum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL__EINGABEDATUM = 6;

	/**
	 * The feature id for the '<em><b>Benutzer</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL__BENUTZER = 7;

	/**
	 * The feature id for the '<em><b>Beschreibung</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL__BESCHREIBUNG = 8;

	/**
	 * The feature id for the '<em><b>Formel</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL__FORMEL = 9;

	/**
	 * The feature id for the '<em><b>Software</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL__SOFTWARE = 10;

	/**
	 * The feature id for the '<em><b>Kommentar</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL__KOMMENTAR = 11;

	/**
	 * The feature id for the '<em><b>Parameter</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL__PARAMETER = 12;

	/**
	 * The feature id for the '<em><b>Literatur</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL__LITERATUR = 13;

	/**
	 * The number of structural features of the '<em>Statistik Modell</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL_FEATURE_COUNT = 14;

	/**
	 * The meta object id for the '{@link de.dim.bfr.impl.StatistikModellParameterImpl <em>Statistik Modell Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.impl.StatistikModellParameterImpl
	 * @see de.dim.bfr.impl.BfrPackageImpl#getStatistikModellParameter()
	 * @generated
	 */
	int STATISTIK_MODELL_PARAMETER = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL_PARAMETER__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL_PARAMETER__NAME = 1;

	/**
	 * The feature id for the '<em><b>Min</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL_PARAMETER__MIN = 2;

	/**
	 * The feature id for the '<em><b>Max</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL_PARAMETER__MAX = 3;

	/**
	 * The feature id for the '<em><b>Beschreibung</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL_PARAMETER__BESCHREIBUNG = 4;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL_PARAMETER__ROLE = 5;

	/**
	 * The feature id for the '<em><b>Integer</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL_PARAMETER__INTEGER = 6;

	/**
	 * The number of structural features of the '<em>Statistik Modell Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL_PARAMETER_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link de.dim.bfr.impl.StatistikModellKatalogImpl <em>Statistik Modell Katalog</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.impl.StatistikModellKatalogImpl
	 * @see de.dim.bfr.impl.BfrPackageImpl#getStatistikModellKatalog()
	 * @generated
	 */
	int STATISTIK_MODELL_KATALOG = 3;

	/**
	 * The feature id for the '<em><b>Modelle</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL_KATALOG__MODELLE = 0;

	/**
	 * The number of structural features of the '<em>Statistik Modell Katalog</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIK_MODELL_KATALOG_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link de.dim.bfr.impl.LiteraturListeImpl <em>Literatur Liste</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.impl.LiteraturListeImpl
	 * @see de.dim.bfr.impl.BfrPackageImpl#getLiteraturListe()
	 * @generated
	 */
	int LITERATUR_LISTE = 4;

	/**
	 * The feature id for the '<em><b>Literatur</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR_LISTE__LITERATUR = 0;

	/**
	 * The number of structural features of the '<em>Literatur Liste</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERATUR_LISTE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link de.dim.bfr.impl.GeschaetztStatistikModellImpl <em>Geschaetzt Statistik Modell</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.impl.GeschaetztStatistikModellImpl
	 * @see de.dim.bfr.impl.BfrPackageImpl#getGeschaetztStatistikModell()
	 * @generated
	 */
	int GESCHAETZT_STATISTIK_MODELL = 5;

	/**
	 * The feature id for the '<em><b>Statistik Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Manuell Eingetragen</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCHAETZT_STATISTIK_MODELL__MANUELL_EINGETRAGEN = 1;

	/**
	 * The feature id for the '<em><b>RSquared</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCHAETZT_STATISTIK_MODELL__RSQUARED = 2;

	/**
	 * The feature id for the '<em><b>Rss</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCHAETZT_STATISTIK_MODELL__RSS = 3;

	/**
	 * The feature id for the '<em><b>Score</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCHAETZT_STATISTIK_MODELL__SCORE = 4;

	/**
	 * The feature id for the '<em><b>Kommentar</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCHAETZT_STATISTIK_MODELL__KOMMENTAR = 5;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCHAETZT_STATISTIK_MODELL__ID = 6;

	/**
	 * The feature id for the '<em><b>Parameter</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCHAETZT_STATISTIK_MODELL__PARAMETER = 7;

	/**
	 * The feature id for the '<em><b>Bedingung</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCHAETZT_STATISTIK_MODELL__BEDINGUNG = 8;

	/**
	 * The feature id for the '<em><b>Literatur</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCHAETZT_STATISTIK_MODELL__LITERATUR = 9;

	/**
	 * The feature id for the '<em><b>Parameter Cov Cor</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCHAETZT_STATISTIK_MODELL__PARAMETER_COV_COR = 10;

	/**
	 * The feature id for the '<em><b>Response</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCHAETZT_STATISTIK_MODELL__RESPONSE = 11;

	/**
	 * The number of structural features of the '<em>Geschaetzt Statistik Modell</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCHAETZT_STATISTIK_MODELL_FEATURE_COUNT = 12;

	/**
	 * The meta object id for the '{@link de.dim.bfr.impl.GeschModellParameterImpl <em>Gesch Modell Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.impl.GeschModellParameterImpl
	 * @see de.dim.bfr.impl.BfrPackageImpl#getGeschModellParameter()
	 * @generated
	 */
	int GESCH_MODELL_PARAMETER = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCH_MODELL_PARAMETER__NAME = 0;

	/**
	 * The feature id for the '<em><b>Sd</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCH_MODELL_PARAMETER__SD = 1;

	/**
	 * The feature id for the '<em><b>Wert</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCH_MODELL_PARAMETER__WERT = 2;

	/**
	 * The feature id for the '<em><b>Versuchs Bedingung</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCH_MODELL_PARAMETER__VERSUCHS_BEDINGUNG = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCH_MODELL_PARAMETER__ID = 4;

	/**
	 * The feature id for the '<em><b>T</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCH_MODELL_PARAMETER__T = 5;

	/**
	 * The feature id for the '<em><b>P</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCH_MODELL_PARAMETER__P = 6;

	/**
	 * The feature id for the '<em><b>Geschaetztes Modell</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCH_MODELL_PARAMETER__GESCHAETZTES_MODELL = 7;

	/**
	 * The feature id for the '<em><b>Model Parameter</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCH_MODELL_PARAMETER__MODEL_PARAMETER = 8;

	/**
	 * The feature id for the '<em><b>Ki Oben</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCH_MODELL_PARAMETER__KI_OBEN = 9;

	/**
	 * The feature id for the '<em><b>Ki Unten</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCH_MODELL_PARAMETER__KI_UNTEN = 10;

	/**
	 * The number of structural features of the '<em>Gesch Modell Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCH_MODELL_PARAMETER_FEATURE_COUNT = 11;

	/**
	 * The meta object id for the '{@link de.dim.bfr.impl.VersuchsBedingungImpl <em>Versuchs Bedingung</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.impl.VersuchsBedingungImpl
	 * @see de.dim.bfr.impl.BfrPackageImpl#getVersuchsBedingung()
	 * @generated
	 */
	int VERSUCHS_BEDINGUNG = 7;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VERSUCHS_BEDINGUNG__ID = 0;

	/**
	 * The feature id for the '<em><b>Id CB</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VERSUCHS_BEDINGUNG__ID_CB = 1;

	/**
	 * The number of structural features of the '<em>Versuchs Bedingung</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VERSUCHS_BEDINGUNG_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link de.dim.bfr.impl.GeschModelListImpl <em>Gesch Model List</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.impl.GeschModelListImpl
	 * @see de.dim.bfr.impl.BfrPackageImpl#getGeschModelList()
	 * @generated
	 */
	int GESCH_MODEL_LIST = 8;

	/**
	 * The feature id for the '<em><b>Models</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCH_MODEL_LIST__MODELS = 0;

	/**
	 * The number of structural features of the '<em>Gesch Model List</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GESCH_MODEL_LIST_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link de.dim.bfr.impl.VersuchsBedingungListImpl <em>Versuchs Bedingung List</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.impl.VersuchsBedingungListImpl
	 * @see de.dim.bfr.impl.BfrPackageImpl#getVersuchsBedingungList()
	 * @generated
	 */
	int VERSUCHS_BEDINGUNG_LIST = 9;

	/**
	 * The feature id for the '<em><b>Bedingungen</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VERSUCHS_BEDINGUNG_LIST__BEDINGUNGEN = 0;

	/**
	 * The number of structural features of the '<em>Versuchs Bedingung List</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VERSUCHS_BEDINGUNG_LIST_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link de.dim.bfr.impl.MesswerteImpl <em>Messwerte</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.impl.MesswerteImpl
	 * @see de.dim.bfr.impl.BfrPackageImpl#getMesswerte()
	 * @generated
	 */
	int MESSWERTE = 10;

	/**
	 * The feature id for the '<em><b>Geprueft</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE__GEPRUEFT = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE__ID = 1;

	/**
	 * The feature id for the '<em><b>Kommentar</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE__KOMMENTAR = 2;

	/**
	 * The feature id for the '<em><b>Zeit Einheit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE__ZEIT_EINHEIT = 3;

	/**
	 * The feature id for the '<em><b>Aw</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE__AW = 4;

	/**
	 * The feature id for the '<em><b>Co2</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE__CO2 = 5;

	/**
	 * The feature id for the '<em><b>Druck</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE__DRUCK = 6;

	/**
	 * The feature id for the '<em><b>Temperatur</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE__TEMPERATUR = 7;

	/**
	 * The feature id for the '<em><b>PH</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE__PH = 8;

	/**
	 * The feature id for the '<em><b>Konzentration</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE__KONZENTRATION = 9;

	/**
	 * The feature id for the '<em><b>Zeit</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE__ZEIT = 10;

	/**
	 * The feature id for the '<em><b>Sonstiges</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE__SONSTIGES = 11;

	/**
	 * The feature id for the '<em><b>Versuchsbedingungen</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE__VERSUCHSBEDINGUNGEN = 12;

	/**
	 * The feature id for the '<em><b>Konz Einheit</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE__KONZ_EINHEIT = 13;

	/**
	 * The number of structural features of the '<em>Messwerte</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSWERTE_FEATURE_COUNT = 14;

	/**
	 * The meta object id for the '{@link de.dim.bfr.impl.DoubleKennzahlenImpl <em>Double Kennzahlen</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.impl.DoubleKennzahlenImpl
	 * @see de.dim.bfr.impl.BfrPackageImpl#getDoubleKennzahlen()
	 * @generated
	 */
	int DOUBLE_KENNZAHLEN = 11;

	/**
	 * The feature id for the '<em><b>Function X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__FUNCTION_X = 0;

	/**
	 * The feature id for the '<em><b>Function XG</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__FUNCTION_XG = 1;

	/**
	 * The feature id for the '<em><b>Function Zeit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__FUNCTION_ZEIT = 2;

	/**
	 * The feature id for the '<em><b>Function Zeit G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__FUNCTION_ZEIT_G = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__ID = 4;

	/**
	 * The feature id for the '<em><b>Lcl95</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__LCL95 = 5;

	/**
	 * The feature id for the '<em><b>Lcl95 G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__LCL95_G = 6;

	/**
	 * The feature id for the '<em><b>Maximum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__MAXIMUM = 7;

	/**
	 * The feature id for the '<em><b>Maximum G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__MAXIMUM_G = 8;

	/**
	 * The feature id for the '<em><b>Median</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__MEDIAN = 9;

	/**
	 * The feature id for the '<em><b>Median G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__MEDIAN_G = 10;

	/**
	 * The feature id for the '<em><b>Minimum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__MINIMUM = 11;

	/**
	 * The feature id for the '<em><b>Minimum G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__MINIMUM_G = 12;

	/**
	 * The feature id for the '<em><b>Standardabweichung</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG = 13;

	/**
	 * The feature id for the '<em><b>Standardabweichung G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG_G = 14;

	/**
	 * The feature id for the '<em><b>Times10 Power Of</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__TIMES10_POWER_OF = 15;

	/**
	 * The feature id for the '<em><b>Ucl95</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__UCL95 = 16;

	/**
	 * The feature id for the '<em><b>Ucl95 G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__UCL95_G = 17;

	/**
	 * The feature id for the '<em><b>Undefined ND</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__UNDEFINED_ND = 18;

	/**
	 * The feature id for the '<em><b>Verteilung</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__VERTEILUNG = 19;

	/**
	 * The feature id for the '<em><b>Verteilung G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__VERTEILUNG_G = 20;

	/**
	 * The feature id for the '<em><b>Wert</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__WERT = 21;

	/**
	 * The feature id for the '<em><b>Wert G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__WERT_G = 22;

	/**
	 * The feature id for the '<em><b>Wert Typ</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__WERT_TYP = 23;

	/**
	 * The feature id for the '<em><b>Wiederholungen</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN = 24;

	/**
	 * The feature id for the '<em><b>Wiederholungen G</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN_G = 25;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN__X = 26;

	/**
	 * The number of structural features of the '<em>Double Kennzahlen</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_KENNZAHLEN_FEATURE_COUNT = 27;

	/**
	 * The meta object id for the '{@link de.dim.bfr.impl.ParameterCovCorImpl <em>Parameter Cov Cor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.impl.ParameterCovCorImpl
	 * @see de.dim.bfr.impl.BfrPackageImpl#getParameterCovCor()
	 * @generated
	 */
	int PARAMETER_COV_COR = 12;

	/**
	 * The feature id for the '<em><b>Parameter1</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_COV_COR__PARAMETER1 = 0;

	/**
	 * The feature id for the '<em><b>Parameter2</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_COV_COR__PARAMETER2 = 1;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_COV_COR__VALUE = 2;

	/**
	 * The feature id for the '<em><b>Cor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_COV_COR__COR = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_COV_COR__ID = 4;

	/**
	 * The number of structural features of the '<em>Parameter Cov Cor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_COV_COR_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link de.dim.bfr.impl.EinheitenImpl <em>Einheiten</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.impl.EinheitenImpl
	 * @see de.dim.bfr.impl.BfrPackageImpl#getEinheiten()
	 * @generated
	 */
	int EINHEITEN = 13;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EINHEITEN__ID = 0;

	/**
	 * The feature id for the '<em><b>Einheit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EINHEITEN__EINHEIT = 1;

	/**
	 * The feature id for the '<em><b>Beschreibung</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EINHEITEN__BESCHREIBUNG = 2;

	/**
	 * The number of structural features of the '<em>Einheiten</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EINHEITEN_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link de.dim.bfr.FreigabeTyp <em>Freigabe Typ</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.FreigabeTyp
	 * @see de.dim.bfr.impl.BfrPackageImpl#getFreigabeTyp()
	 * @generated
	 */
	int FREIGABE_TYP = 14;

	/**
	 * The meta object id for the '{@link de.dim.bfr.LiteraturTyp <em>Literatur Typ</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.LiteraturTyp
	 * @see de.dim.bfr.impl.BfrPackageImpl#getLiteraturTyp()
	 * @generated
	 */
	int LITERATUR_TYP = 15;

	/**
	 * The meta object id for the '{@link de.dim.bfr.LevelTyp <em>Level Typ</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.LevelTyp
	 * @see de.dim.bfr.impl.BfrPackageImpl#getLevelTyp()
	 * @generated
	 */
	int LEVEL_TYP = 16;

	/**
	 * The meta object id for the '{@link de.dim.bfr.KlasseTyp <em>Klasse Typ</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.KlasseTyp
	 * @see de.dim.bfr.impl.BfrPackageImpl#getKlasseTyp()
	 * @generated
	 */
	int KLASSE_TYP = 17;


	/**
	 * The meta object id for the '{@link de.dim.bfr.SoftwareType <em>Software Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.SoftwareType
	 * @see de.dim.bfr.impl.BfrPackageImpl#getSoftwareType()
	 * @generated
	 */
	int SOFTWARE_TYPE = 18;

	/**
	 * The meta object id for the '{@link de.dim.bfr.ParameterRoleType <em>Parameter Role Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see de.dim.bfr.ParameterRoleType
	 * @see de.dim.bfr.impl.BfrPackageImpl#getParameterRoleType()
	 * @generated
	 */
	int PARAMETER_ROLE_TYPE = 19;


	/**
	 * Returns the meta object for class '{@link de.dim.bfr.Literatur <em>Literatur</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Literatur</em>'.
	 * @see de.dim.bfr.Literatur
	 * @generated
	 */
	EClass getLiteratur();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Literatur#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see de.dim.bfr.Literatur#getId()
	 * @see #getLiteratur()
	 * @generated
	 */
	EAttribute getLiteratur_Id();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Literatur#getErstautor <em>Erstautor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Erstautor</em>'.
	 * @see de.dim.bfr.Literatur#getErstautor()
	 * @see #getLiteratur()
	 * @generated
	 */
	EAttribute getLiteratur_Erstautor();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Literatur#getJahr <em>Jahr</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Jahr</em>'.
	 * @see de.dim.bfr.Literatur#getJahr()
	 * @see #getLiteratur()
	 * @generated
	 */
	EAttribute getLiteratur_Jahr();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Literatur#getTitel <em>Titel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Titel</em>'.
	 * @see de.dim.bfr.Literatur#getTitel()
	 * @see #getLiteratur()
	 * @generated
	 */
	EAttribute getLiteratur_Titel();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Literatur#getLiteraturAbstract <em>Literatur Abstract</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Literatur Abstract</em>'.
	 * @see de.dim.bfr.Literatur#getLiteraturAbstract()
	 * @see #getLiteratur()
	 * @generated
	 */
	EAttribute getLiteratur_LiteraturAbstract();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Literatur#getJournal <em>Journal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Journal</em>'.
	 * @see de.dim.bfr.Literatur#getJournal()
	 * @see #getLiteratur()
	 * @generated
	 */
	EAttribute getLiteratur_Journal();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Literatur#getVolume <em>Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume</em>'.
	 * @see de.dim.bfr.Literatur#getVolume()
	 * @see #getLiteratur()
	 * @generated
	 */
	EAttribute getLiteratur_Volume();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Literatur#getIssue <em>Issue</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Issue</em>'.
	 * @see de.dim.bfr.Literatur#getIssue()
	 * @see #getLiteratur()
	 * @generated
	 */
	EAttribute getLiteratur_Issue();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Literatur#getSeite <em>Seite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Seite</em>'.
	 * @see de.dim.bfr.Literatur#getSeite()
	 * @see #getLiteratur()
	 * @generated
	 */
	EAttribute getLiteratur_Seite();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Literatur#getFreigabeModus <em>Freigabe Modus</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Freigabe Modus</em>'.
	 * @see de.dim.bfr.Literatur#getFreigabeModus()
	 * @see #getLiteratur()
	 * @generated
	 */
	EAttribute getLiteratur_FreigabeModus();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Literatur#getWebseite <em>Webseite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Webseite</em>'.
	 * @see de.dim.bfr.Literatur#getWebseite()
	 * @see #getLiteratur()
	 * @generated
	 */
	EAttribute getLiteratur_Webseite();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Literatur#getLiteraturTyp <em>Literatur Typ</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Literatur Typ</em>'.
	 * @see de.dim.bfr.Literatur#getLiteraturTyp()
	 * @see #getLiteratur()
	 * @generated
	 */
	EAttribute getLiteratur_LiteraturTyp();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Literatur#getPaper <em>Paper</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Paper</em>'.
	 * @see de.dim.bfr.Literatur#getPaper()
	 * @see #getLiteratur()
	 * @generated
	 */
	EAttribute getLiteratur_Paper();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Literatur#getKommentar <em>Kommentar</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kommentar</em>'.
	 * @see de.dim.bfr.Literatur#getKommentar()
	 * @see #getLiteratur()
	 * @generated
	 */
	EAttribute getLiteratur_Kommentar();

	/**
	 * Returns the meta object for class '{@link de.dim.bfr.StatistikModell <em>Statistik Modell</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Statistik Modell</em>'.
	 * @see de.dim.bfr.StatistikModell
	 * @generated
	 */
	EClass getStatistikModell();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModell#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see de.dim.bfr.StatistikModell#getId()
	 * @see #getStatistikModell()
	 * @generated
	 */
	EAttribute getStatistikModell_Id();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModell#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see de.dim.bfr.StatistikModell#getName()
	 * @see #getStatistikModell()
	 * @generated
	 */
	EAttribute getStatistikModell_Name();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModell#getNotation <em>Notation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Notation</em>'.
	 * @see de.dim.bfr.StatistikModell#getNotation()
	 * @see #getStatistikModell()
	 * @generated
	 */
	EAttribute getStatistikModell_Notation();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModell#getLevel <em>Level</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Level</em>'.
	 * @see de.dim.bfr.StatistikModell#getLevel()
	 * @see #getStatistikModell()
	 * @generated
	 */
	EAttribute getStatistikModell_Level();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModell#getKlasse <em>Klasse</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Klasse</em>'.
	 * @see de.dim.bfr.StatistikModell#getKlasse()
	 * @see #getStatistikModell()
	 * @generated
	 */
	EAttribute getStatistikModell_Klasse();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModell#getTyp <em>Typ</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Typ</em>'.
	 * @see de.dim.bfr.StatistikModell#getTyp()
	 * @see #getStatistikModell()
	 * @generated
	 */
	EAttribute getStatistikModell_Typ();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModell#getEingabedatum <em>Eingabedatum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Eingabedatum</em>'.
	 * @see de.dim.bfr.StatistikModell#getEingabedatum()
	 * @see #getStatistikModell()
	 * @generated
	 */
	EAttribute getStatistikModell_Eingabedatum();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModell#getBenutzer <em>Benutzer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Benutzer</em>'.
	 * @see de.dim.bfr.StatistikModell#getBenutzer()
	 * @see #getStatistikModell()
	 * @generated
	 */
	EAttribute getStatistikModell_Benutzer();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModell#getBeschreibung <em>Beschreibung</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Beschreibung</em>'.
	 * @see de.dim.bfr.StatistikModell#getBeschreibung()
	 * @see #getStatistikModell()
	 * @generated
	 */
	EAttribute getStatistikModell_Beschreibung();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModell#getFormel <em>Formel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Formel</em>'.
	 * @see de.dim.bfr.StatistikModell#getFormel()
	 * @see #getStatistikModell()
	 * @generated
	 */
	EAttribute getStatistikModell_Formel();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModell#getSoftware <em>Software</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Software</em>'.
	 * @see de.dim.bfr.StatistikModell#getSoftware()
	 * @see #getStatistikModell()
	 * @generated
	 */
	EAttribute getStatistikModell_Software();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModell#getKommentar <em>Kommentar</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kommentar</em>'.
	 * @see de.dim.bfr.StatistikModell#getKommentar()
	 * @see #getStatistikModell()
	 * @generated
	 */
	EAttribute getStatistikModell_Kommentar();

	/**
	 * Returns the meta object for the containment reference list '{@link de.dim.bfr.StatistikModell#getParameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameter</em>'.
	 * @see de.dim.bfr.StatistikModell#getParameter()
	 * @see #getStatistikModell()
	 * @generated
	 */
	EReference getStatistikModell_Parameter();

	/**
	 * Returns the meta object for the reference list '{@link de.dim.bfr.StatistikModell#getLiteratur <em>Literatur</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Literatur</em>'.
	 * @see de.dim.bfr.StatistikModell#getLiteratur()
	 * @see #getStatistikModell()
	 * @generated
	 */
	EReference getStatistikModell_Literatur();

	/**
	 * Returns the meta object for class '{@link de.dim.bfr.StatistikModellParameter <em>Statistik Modell Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Statistik Modell Parameter</em>'.
	 * @see de.dim.bfr.StatistikModellParameter
	 * @generated
	 */
	EClass getStatistikModellParameter();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModellParameter#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see de.dim.bfr.StatistikModellParameter#getId()
	 * @see #getStatistikModellParameter()
	 * @generated
	 */
	EAttribute getStatistikModellParameter_Id();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModellParameter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see de.dim.bfr.StatistikModellParameter#getName()
	 * @see #getStatistikModellParameter()
	 * @generated
	 */
	EAttribute getStatistikModellParameter_Name();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModellParameter#getMin <em>Min</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min</em>'.
	 * @see de.dim.bfr.StatistikModellParameter#getMin()
	 * @see #getStatistikModellParameter()
	 * @generated
	 */
	EAttribute getStatistikModellParameter_Min();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModellParameter#getMax <em>Max</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max</em>'.
	 * @see de.dim.bfr.StatistikModellParameter#getMax()
	 * @see #getStatistikModellParameter()
	 * @generated
	 */
	EAttribute getStatistikModellParameter_Max();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModellParameter#getBeschreibung <em>Beschreibung</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Beschreibung</em>'.
	 * @see de.dim.bfr.StatistikModellParameter#getBeschreibung()
	 * @see #getStatistikModellParameter()
	 * @generated
	 */
	EAttribute getStatistikModellParameter_Beschreibung();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModellParameter#getRole <em>Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Role</em>'.
	 * @see de.dim.bfr.StatistikModellParameter#getRole()
	 * @see #getStatistikModellParameter()
	 * @generated
	 */
	EAttribute getStatistikModellParameter_Role();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.StatistikModellParameter#isInteger <em>Integer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Integer</em>'.
	 * @see de.dim.bfr.StatistikModellParameter#isInteger()
	 * @see #getStatistikModellParameter()
	 * @generated
	 */
	EAttribute getStatistikModellParameter_Integer();

	/**
	 * Returns the meta object for class '{@link de.dim.bfr.StatistikModellKatalog <em>Statistik Modell Katalog</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Statistik Modell Katalog</em>'.
	 * @see de.dim.bfr.StatistikModellKatalog
	 * @generated
	 */
	EClass getStatistikModellKatalog();

	/**
	 * Returns the meta object for the containment reference list '{@link de.dim.bfr.StatistikModellKatalog#getModelle <em>Modelle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Modelle</em>'.
	 * @see de.dim.bfr.StatistikModellKatalog#getModelle()
	 * @see #getStatistikModellKatalog()
	 * @generated
	 */
	EReference getStatistikModellKatalog_Modelle();

	/**
	 * Returns the meta object for class '{@link de.dim.bfr.LiteraturListe <em>Literatur Liste</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Literatur Liste</em>'.
	 * @see de.dim.bfr.LiteraturListe
	 * @generated
	 */
	EClass getLiteraturListe();

	/**
	 * Returns the meta object for the containment reference list '{@link de.dim.bfr.LiteraturListe#getLiteratur <em>Literatur</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Literatur</em>'.
	 * @see de.dim.bfr.LiteraturListe#getLiteratur()
	 * @see #getLiteraturListe()
	 * @generated
	 */
	EReference getLiteraturListe_Literatur();

	/**
	 * Returns the meta object for class '{@link de.dim.bfr.GeschaetztStatistikModell <em>Geschaetzt Statistik Modell</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Geschaetzt Statistik Modell</em>'.
	 * @see de.dim.bfr.GeschaetztStatistikModell
	 * @generated
	 */
	EClass getGeschaetztStatistikModell();

	/**
	 * Returns the meta object for the reference '{@link de.dim.bfr.GeschaetztStatistikModell#getStatistikModel <em>Statistik Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Statistik Model</em>'.
	 * @see de.dim.bfr.GeschaetztStatistikModell#getStatistikModel()
	 * @see #getGeschaetztStatistikModell()
	 * @generated
	 */
	EReference getGeschaetztStatistikModell_StatistikModel();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.GeschaetztStatistikModell#isManuellEingetragen <em>Manuell Eingetragen</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Manuell Eingetragen</em>'.
	 * @see de.dim.bfr.GeschaetztStatistikModell#isManuellEingetragen()
	 * @see #getGeschaetztStatistikModell()
	 * @generated
	 */
	EAttribute getGeschaetztStatistikModell_ManuellEingetragen();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.GeschaetztStatistikModell#getRSquared <em>RSquared</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>RSquared</em>'.
	 * @see de.dim.bfr.GeschaetztStatistikModell#getRSquared()
	 * @see #getGeschaetztStatistikModell()
	 * @generated
	 */
	EAttribute getGeschaetztStatistikModell_RSquared();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.GeschaetztStatistikModell#getRss <em>Rss</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rss</em>'.
	 * @see de.dim.bfr.GeschaetztStatistikModell#getRss()
	 * @see #getGeschaetztStatistikModell()
	 * @generated
	 */
	EAttribute getGeschaetztStatistikModell_Rss();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.GeschaetztStatistikModell#getScore <em>Score</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Score</em>'.
	 * @see de.dim.bfr.GeschaetztStatistikModell#getScore()
	 * @see #getGeschaetztStatistikModell()
	 * @generated
	 */
	EAttribute getGeschaetztStatistikModell_Score();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.GeschaetztStatistikModell#getKommentar <em>Kommentar</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kommentar</em>'.
	 * @see de.dim.bfr.GeschaetztStatistikModell#getKommentar()
	 * @see #getGeschaetztStatistikModell()
	 * @generated
	 */
	EAttribute getGeschaetztStatistikModell_Kommentar();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.GeschaetztStatistikModell#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see de.dim.bfr.GeschaetztStatistikModell#getId()
	 * @see #getGeschaetztStatistikModell()
	 * @generated
	 */
	EAttribute getGeschaetztStatistikModell_Id();

	/**
	 * Returns the meta object for the containment reference list '{@link de.dim.bfr.GeschaetztStatistikModell#getParameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameter</em>'.
	 * @see de.dim.bfr.GeschaetztStatistikModell#getParameter()
	 * @see #getGeschaetztStatistikModell()
	 * @generated
	 */
	EReference getGeschaetztStatistikModell_Parameter();

	/**
	 * Returns the meta object for the reference '{@link de.dim.bfr.GeschaetztStatistikModell#getBedingung <em>Bedingung</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Bedingung</em>'.
	 * @see de.dim.bfr.GeschaetztStatistikModell#getBedingung()
	 * @see #getGeschaetztStatistikModell()
	 * @generated
	 */
	EReference getGeschaetztStatistikModell_Bedingung();

	/**
	 * Returns the meta object for the reference list '{@link de.dim.bfr.GeschaetztStatistikModell#getLiteratur <em>Literatur</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Literatur</em>'.
	 * @see de.dim.bfr.GeschaetztStatistikModell#getLiteratur()
	 * @see #getGeschaetztStatistikModell()
	 * @generated
	 */
	EReference getGeschaetztStatistikModell_Literatur();

	/**
	 * Returns the meta object for the reference list '{@link de.dim.bfr.GeschaetztStatistikModell#getParameterCovCor <em>Parameter Cov Cor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Parameter Cov Cor</em>'.
	 * @see de.dim.bfr.GeschaetztStatistikModell#getParameterCovCor()
	 * @see #getGeschaetztStatistikModell()
	 * @generated
	 */
	EReference getGeschaetztStatistikModell_ParameterCovCor();

	/**
	 * Returns the meta object for the reference '{@link de.dim.bfr.GeschaetztStatistikModell#getResponse <em>Response</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Response</em>'.
	 * @see de.dim.bfr.GeschaetztStatistikModell#getResponse()
	 * @see #getGeschaetztStatistikModell()
	 * @generated
	 */
	EReference getGeschaetztStatistikModell_Response();

	/**
	 * Returns the meta object for class '{@link de.dim.bfr.GeschModellParameter <em>Gesch Modell Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Gesch Modell Parameter</em>'.
	 * @see de.dim.bfr.GeschModellParameter
	 * @generated
	 */
	EClass getGeschModellParameter();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.GeschModellParameter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see de.dim.bfr.GeschModellParameter#getName()
	 * @see #getGeschModellParameter()
	 * @generated
	 */
	EAttribute getGeschModellParameter_Name();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.GeschModellParameter#getSd <em>Sd</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sd</em>'.
	 * @see de.dim.bfr.GeschModellParameter#getSd()
	 * @see #getGeschModellParameter()
	 * @generated
	 */
	EAttribute getGeschModellParameter_Sd();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.GeschModellParameter#getWert <em>Wert</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wert</em>'.
	 * @see de.dim.bfr.GeschModellParameter#getWert()
	 * @see #getGeschModellParameter()
	 * @generated
	 */
	EAttribute getGeschModellParameter_Wert();

	/**
	 * Returns the meta object for the reference '{@link de.dim.bfr.GeschModellParameter#getVersuchsBedingung <em>Versuchs Bedingung</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Versuchs Bedingung</em>'.
	 * @see de.dim.bfr.GeschModellParameter#getVersuchsBedingung()
	 * @see #getGeschModellParameter()
	 * @generated
	 */
	EReference getGeschModellParameter_VersuchsBedingung();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.GeschModellParameter#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see de.dim.bfr.GeschModellParameter#getId()
	 * @see #getGeschModellParameter()
	 * @generated
	 */
	EAttribute getGeschModellParameter_Id();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.GeschModellParameter#getT <em>T</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>T</em>'.
	 * @see de.dim.bfr.GeschModellParameter#getT()
	 * @see #getGeschModellParameter()
	 * @generated
	 */
	EAttribute getGeschModellParameter_T();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.GeschModellParameter#getP <em>P</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>P</em>'.
	 * @see de.dim.bfr.GeschModellParameter#getP()
	 * @see #getGeschModellParameter()
	 * @generated
	 */
	EAttribute getGeschModellParameter_P();

	/**
	 * Returns the meta object for the reference '{@link de.dim.bfr.GeschModellParameter#getGeschaetztesModell <em>Geschaetztes Modell</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Geschaetztes Modell</em>'.
	 * @see de.dim.bfr.GeschModellParameter#getGeschaetztesModell()
	 * @see #getGeschModellParameter()
	 * @generated
	 */
	EReference getGeschModellParameter_GeschaetztesModell();

	/**
	 * Returns the meta object for the reference '{@link de.dim.bfr.GeschModellParameter#getModelParameter <em>Model Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Model Parameter</em>'.
	 * @see de.dim.bfr.GeschModellParameter#getModelParameter()
	 * @see #getGeschModellParameter()
	 * @generated
	 */
	EReference getGeschModellParameter_ModelParameter();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.GeschModellParameter#getKiOben <em>Ki Oben</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ki Oben</em>'.
	 * @see de.dim.bfr.GeschModellParameter#getKiOben()
	 * @see #getGeschModellParameter()
	 * @generated
	 */
	EAttribute getGeschModellParameter_KiOben();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.GeschModellParameter#getKiUnten <em>Ki Unten</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ki Unten</em>'.
	 * @see de.dim.bfr.GeschModellParameter#getKiUnten()
	 * @see #getGeschModellParameter()
	 * @generated
	 */
	EAttribute getGeschModellParameter_KiUnten();

	/**
	 * Returns the meta object for class '{@link de.dim.bfr.VersuchsBedingung <em>Versuchs Bedingung</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Versuchs Bedingung</em>'.
	 * @see de.dim.bfr.VersuchsBedingung
	 * @generated
	 */
	EClass getVersuchsBedingung();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.VersuchsBedingung#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see de.dim.bfr.VersuchsBedingung#getId()
	 * @see #getVersuchsBedingung()
	 * @generated
	 */
	EAttribute getVersuchsBedingung_Id();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.VersuchsBedingung#getIdCB <em>Id CB</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id CB</em>'.
	 * @see de.dim.bfr.VersuchsBedingung#getIdCB()
	 * @see #getVersuchsBedingung()
	 * @generated
	 */
	EAttribute getVersuchsBedingung_IdCB();

	/**
	 * Returns the meta object for class '{@link de.dim.bfr.GeschModelList <em>Gesch Model List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Gesch Model List</em>'.
	 * @see de.dim.bfr.GeschModelList
	 * @generated
	 */
	EClass getGeschModelList();

	/**
	 * Returns the meta object for the containment reference list '{@link de.dim.bfr.GeschModelList#getModels <em>Models</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Models</em>'.
	 * @see de.dim.bfr.GeschModelList#getModels()
	 * @see #getGeschModelList()
	 * @generated
	 */
	EReference getGeschModelList_Models();

	/**
	 * Returns the meta object for class '{@link de.dim.bfr.VersuchsBedingungList <em>Versuchs Bedingung List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Versuchs Bedingung List</em>'.
	 * @see de.dim.bfr.VersuchsBedingungList
	 * @generated
	 */
	EClass getVersuchsBedingungList();

	/**
	 * Returns the meta object for the containment reference list '{@link de.dim.bfr.VersuchsBedingungList#getBedingungen <em>Bedingungen</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Bedingungen</em>'.
	 * @see de.dim.bfr.VersuchsBedingungList#getBedingungen()
	 * @see #getVersuchsBedingungList()
	 * @generated
	 */
	EReference getVersuchsBedingungList_Bedingungen();

	/**
	 * Returns the meta object for class '{@link de.dim.bfr.Messwerte <em>Messwerte</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Messwerte</em>'.
	 * @see de.dim.bfr.Messwerte
	 * @generated
	 */
	EClass getMesswerte();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Messwerte#isGeprueft <em>Geprueft</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Geprueft</em>'.
	 * @see de.dim.bfr.Messwerte#isGeprueft()
	 * @see #getMesswerte()
	 * @generated
	 */
	EAttribute getMesswerte_Geprueft();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Messwerte#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see de.dim.bfr.Messwerte#getId()
	 * @see #getMesswerte()
	 * @generated
	 */
	EAttribute getMesswerte_Id();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Messwerte#getKommentar <em>Kommentar</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kommentar</em>'.
	 * @see de.dim.bfr.Messwerte#getKommentar()
	 * @see #getMesswerte()
	 * @generated
	 */
	EAttribute getMesswerte_Kommentar();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Messwerte#getZeitEinheit <em>Zeit Einheit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Zeit Einheit</em>'.
	 * @see de.dim.bfr.Messwerte#getZeitEinheit()
	 * @see #getMesswerte()
	 * @generated
	 */
	EAttribute getMesswerte_ZeitEinheit();

	/**
	 * Returns the meta object for the containment reference '{@link de.dim.bfr.Messwerte#getAw <em>Aw</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Aw</em>'.
	 * @see de.dim.bfr.Messwerte#getAw()
	 * @see #getMesswerte()
	 * @generated
	 */
	EReference getMesswerte_Aw();

	/**
	 * Returns the meta object for the containment reference '{@link de.dim.bfr.Messwerte#getCo2 <em>Co2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Co2</em>'.
	 * @see de.dim.bfr.Messwerte#getCo2()
	 * @see #getMesswerte()
	 * @generated
	 */
	EReference getMesswerte_Co2();

	/**
	 * Returns the meta object for the containment reference '{@link de.dim.bfr.Messwerte#getDruck <em>Druck</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Druck</em>'.
	 * @see de.dim.bfr.Messwerte#getDruck()
	 * @see #getMesswerte()
	 * @generated
	 */
	EReference getMesswerte_Druck();

	/**
	 * Returns the meta object for the containment reference '{@link de.dim.bfr.Messwerte#getTemperatur <em>Temperatur</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Temperatur</em>'.
	 * @see de.dim.bfr.Messwerte#getTemperatur()
	 * @see #getMesswerte()
	 * @generated
	 */
	EReference getMesswerte_Temperatur();

	/**
	 * Returns the meta object for the containment reference '{@link de.dim.bfr.Messwerte#getPH <em>PH</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>PH</em>'.
	 * @see de.dim.bfr.Messwerte#getPH()
	 * @see #getMesswerte()
	 * @generated
	 */
	EReference getMesswerte_PH();

	/**
	 * Returns the meta object for the containment reference '{@link de.dim.bfr.Messwerte#getKonzentration <em>Konzentration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Konzentration</em>'.
	 * @see de.dim.bfr.Messwerte#getKonzentration()
	 * @see #getMesswerte()
	 * @generated
	 */
	EReference getMesswerte_Konzentration();

	/**
	 * Returns the meta object for the containment reference '{@link de.dim.bfr.Messwerte#getZeit <em>Zeit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Zeit</em>'.
	 * @see de.dim.bfr.Messwerte#getZeit()
	 * @see #getMesswerte()
	 * @generated
	 */
	EReference getMesswerte_Zeit();

	/**
	 * Returns the meta object for the reference '{@link de.dim.bfr.Messwerte#getSonstiges <em>Sonstiges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Sonstiges</em>'.
	 * @see de.dim.bfr.Messwerte#getSonstiges()
	 * @see #getMesswerte()
	 * @generated
	 */
	EReference getMesswerte_Sonstiges();

	/**
	 * Returns the meta object for the reference '{@link de.dim.bfr.Messwerte#getVersuchsbedingungen <em>Versuchsbedingungen</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Versuchsbedingungen</em>'.
	 * @see de.dim.bfr.Messwerte#getVersuchsbedingungen()
	 * @see #getMesswerte()
	 * @generated
	 */
	EReference getMesswerte_Versuchsbedingungen();

	/**
	 * Returns the meta object for the containment reference '{@link de.dim.bfr.Messwerte#getKonzEinheit <em>Konz Einheit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Konz Einheit</em>'.
	 * @see de.dim.bfr.Messwerte#getKonzEinheit()
	 * @see #getMesswerte()
	 * @generated
	 */
	EReference getMesswerte_KonzEinheit();

	/**
	 * Returns the meta object for class '{@link de.dim.bfr.DoubleKennzahlen <em>Double Kennzahlen</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Double Kennzahlen</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen
	 * @generated
	 */
	EClass getDoubleKennzahlen();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getFunctionX <em>Function X</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Function X</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getFunctionX()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_FunctionX();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#isFunctionXG <em>Function XG</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Function XG</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#isFunctionXG()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_FunctionXG();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getFunctionZeit <em>Function Zeit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Function Zeit</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getFunctionZeit()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_FunctionZeit();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#isFunctionZeitG <em>Function Zeit G</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Function Zeit G</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#isFunctionZeitG()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_FunctionZeitG();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getId()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_Id();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getLcl95 <em>Lcl95</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lcl95</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getLcl95()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_Lcl95();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#isLcl95G <em>Lcl95 G</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lcl95 G</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#isLcl95G()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_Lcl95G();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getMaximum <em>Maximum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Maximum</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getMaximum()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_Maximum();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#isMaximumG <em>Maximum G</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Maximum G</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#isMaximumG()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_MaximumG();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getMedian <em>Median</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Median</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getMedian()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_Median();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#isMedianG <em>Median G</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Median G</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#isMedianG()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_MedianG();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getMinimum <em>Minimum</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Minimum</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getMinimum()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_Minimum();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#isMinimumG <em>Minimum G</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Minimum G</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#isMinimumG()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_MinimumG();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getStandardabweichung <em>Standardabweichung</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Standardabweichung</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getStandardabweichung()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_Standardabweichung();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#isStandardabweichungG <em>Standardabweichung G</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Standardabweichung G</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#isStandardabweichungG()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_StandardabweichungG();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getTimes10PowerOf <em>Times10 Power Of</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Times10 Power Of</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getTimes10PowerOf()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_Times10PowerOf();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getUcl95 <em>Ucl95</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ucl95</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getUcl95()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_Ucl95();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#isUcl95G <em>Ucl95 G</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ucl95 G</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#isUcl95G()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_Ucl95G();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#isUndefinedND <em>Undefined ND</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Undefined ND</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#isUndefinedND()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_UndefinedND();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getVerteilung <em>Verteilung</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Verteilung</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getVerteilung()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_Verteilung();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#isVerteilungG <em>Verteilung G</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Verteilung G</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#isVerteilungG()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_VerteilungG();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getWert <em>Wert</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wert</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getWert()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_Wert();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#isWertG <em>Wert G</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wert G</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#isWertG()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_WertG();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getWertTyp <em>Wert Typ</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wert Typ</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getWertTyp()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_WertTyp();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getWiederholungen <em>Wiederholungen</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wiederholungen</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getWiederholungen()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_Wiederholungen();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#isWiederholungenG <em>Wiederholungen G</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wiederholungen G</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#isWiederholungenG()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_WiederholungenG();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.DoubleKennzahlen#getX <em>X</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>X</em>'.
	 * @see de.dim.bfr.DoubleKennzahlen#getX()
	 * @see #getDoubleKennzahlen()
	 * @generated
	 */
	EAttribute getDoubleKennzahlen_X();

	/**
	 * Returns the meta object for class '{@link de.dim.bfr.ParameterCovCor <em>Parameter Cov Cor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter Cov Cor</em>'.
	 * @see de.dim.bfr.ParameterCovCor
	 * @generated
	 */
	EClass getParameterCovCor();

	/**
	 * Returns the meta object for the reference '{@link de.dim.bfr.ParameterCovCor#getParameter1 <em>Parameter1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Parameter1</em>'.
	 * @see de.dim.bfr.ParameterCovCor#getParameter1()
	 * @see #getParameterCovCor()
	 * @generated
	 */
	EReference getParameterCovCor_Parameter1();

	/**
	 * Returns the meta object for the reference '{@link de.dim.bfr.ParameterCovCor#getParameter2 <em>Parameter2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Parameter2</em>'.
	 * @see de.dim.bfr.ParameterCovCor#getParameter2()
	 * @see #getParameterCovCor()
	 * @generated
	 */
	EReference getParameterCovCor_Parameter2();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.ParameterCovCor#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see de.dim.bfr.ParameterCovCor#getValue()
	 * @see #getParameterCovCor()
	 * @generated
	 */
	EAttribute getParameterCovCor_Value();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.ParameterCovCor#isCor <em>Cor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cor</em>'.
	 * @see de.dim.bfr.ParameterCovCor#isCor()
	 * @see #getParameterCovCor()
	 * @generated
	 */
	EAttribute getParameterCovCor_Cor();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.ParameterCovCor#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see de.dim.bfr.ParameterCovCor#getId()
	 * @see #getParameterCovCor()
	 * @generated
	 */
	EAttribute getParameterCovCor_Id();

	/**
	 * Returns the meta object for class '{@link de.dim.bfr.Einheiten <em>Einheiten</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Einheiten</em>'.
	 * @see de.dim.bfr.Einheiten
	 * @generated
	 */
	EClass getEinheiten();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Einheiten#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see de.dim.bfr.Einheiten#getId()
	 * @see #getEinheiten()
	 * @generated
	 */
	EAttribute getEinheiten_Id();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Einheiten#getEinheit <em>Einheit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Einheit</em>'.
	 * @see de.dim.bfr.Einheiten#getEinheit()
	 * @see #getEinheiten()
	 * @generated
	 */
	EAttribute getEinheiten_Einheit();

	/**
	 * Returns the meta object for the attribute '{@link de.dim.bfr.Einheiten#getBeschreibung <em>Beschreibung</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Beschreibung</em>'.
	 * @see de.dim.bfr.Einheiten#getBeschreibung()
	 * @see #getEinheiten()
	 * @generated
	 */
	EAttribute getEinheiten_Beschreibung();

	/**
	 * Returns the meta object for enum '{@link de.dim.bfr.FreigabeTyp <em>Freigabe Typ</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Freigabe Typ</em>'.
	 * @see de.dim.bfr.FreigabeTyp
	 * @generated
	 */
	EEnum getFreigabeTyp();

	/**
	 * Returns the meta object for enum '{@link de.dim.bfr.LiteraturTyp <em>Literatur Typ</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Literatur Typ</em>'.
	 * @see de.dim.bfr.LiteraturTyp
	 * @generated
	 */
	EEnum getLiteraturTyp();

	/**
	 * Returns the meta object for enum '{@link de.dim.bfr.LevelTyp <em>Level Typ</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Level Typ</em>'.
	 * @see de.dim.bfr.LevelTyp
	 * @generated
	 */
	EEnum getLevelTyp();

	/**
	 * Returns the meta object for enum '{@link de.dim.bfr.KlasseTyp <em>Klasse Typ</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Klasse Typ</em>'.
	 * @see de.dim.bfr.KlasseTyp
	 * @generated
	 */
	EEnum getKlasseTyp();

	/**
	 * Returns the meta object for enum '{@link de.dim.bfr.SoftwareType <em>Software Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Software Type</em>'.
	 * @see de.dim.bfr.SoftwareType
	 * @generated
	 */
	EEnum getSoftwareType();

	/**
	 * Returns the meta object for enum '{@link de.dim.bfr.ParameterRoleType <em>Parameter Role Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Parameter Role Type</em>'.
	 * @see de.dim.bfr.ParameterRoleType
	 * @generated
	 */
	EEnum getParameterRoleType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	BfrFactory getBfrFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link de.dim.bfr.impl.LiteraturImpl <em>Literatur</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.impl.LiteraturImpl
		 * @see de.dim.bfr.impl.BfrPackageImpl#getLiteratur()
		 * @generated
		 */
		EClass LITERATUR = eINSTANCE.getLiteratur();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERATUR__ID = eINSTANCE.getLiteratur_Id();

		/**
		 * The meta object literal for the '<em><b>Erstautor</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERATUR__ERSTAUTOR = eINSTANCE.getLiteratur_Erstautor();

		/**
		 * The meta object literal for the '<em><b>Jahr</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERATUR__JAHR = eINSTANCE.getLiteratur_Jahr();

		/**
		 * The meta object literal for the '<em><b>Titel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERATUR__TITEL = eINSTANCE.getLiteratur_Titel();

		/**
		 * The meta object literal for the '<em><b>Literatur Abstract</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERATUR__LITERATUR_ABSTRACT = eINSTANCE.getLiteratur_LiteraturAbstract();

		/**
		 * The meta object literal for the '<em><b>Journal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERATUR__JOURNAL = eINSTANCE.getLiteratur_Journal();

		/**
		 * The meta object literal for the '<em><b>Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERATUR__VOLUME = eINSTANCE.getLiteratur_Volume();

		/**
		 * The meta object literal for the '<em><b>Issue</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERATUR__ISSUE = eINSTANCE.getLiteratur_Issue();

		/**
		 * The meta object literal for the '<em><b>Seite</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERATUR__SEITE = eINSTANCE.getLiteratur_Seite();

		/**
		 * The meta object literal for the '<em><b>Freigabe Modus</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERATUR__FREIGABE_MODUS = eINSTANCE.getLiteratur_FreigabeModus();

		/**
		 * The meta object literal for the '<em><b>Webseite</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERATUR__WEBSEITE = eINSTANCE.getLiteratur_Webseite();

		/**
		 * The meta object literal for the '<em><b>Literatur Typ</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERATUR__LITERATUR_TYP = eINSTANCE.getLiteratur_LiteraturTyp();

		/**
		 * The meta object literal for the '<em><b>Paper</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERATUR__PAPER = eINSTANCE.getLiteratur_Paper();

		/**
		 * The meta object literal for the '<em><b>Kommentar</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LITERATUR__KOMMENTAR = eINSTANCE.getLiteratur_Kommentar();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.impl.StatistikModellImpl <em>Statistik Modell</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.impl.StatistikModellImpl
		 * @see de.dim.bfr.impl.BfrPackageImpl#getStatistikModell()
		 * @generated
		 */
		EClass STATISTIK_MODELL = eINSTANCE.getStatistikModell();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL__ID = eINSTANCE.getStatistikModell_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL__NAME = eINSTANCE.getStatistikModell_Name();

		/**
		 * The meta object literal for the '<em><b>Notation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL__NOTATION = eINSTANCE.getStatistikModell_Notation();

		/**
		 * The meta object literal for the '<em><b>Level</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL__LEVEL = eINSTANCE.getStatistikModell_Level();

		/**
		 * The meta object literal for the '<em><b>Klasse</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL__KLASSE = eINSTANCE.getStatistikModell_Klasse();

		/**
		 * The meta object literal for the '<em><b>Typ</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL__TYP = eINSTANCE.getStatistikModell_Typ();

		/**
		 * The meta object literal for the '<em><b>Eingabedatum</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL__EINGABEDATUM = eINSTANCE.getStatistikModell_Eingabedatum();

		/**
		 * The meta object literal for the '<em><b>Benutzer</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL__BENUTZER = eINSTANCE.getStatistikModell_Benutzer();

		/**
		 * The meta object literal for the '<em><b>Beschreibung</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL__BESCHREIBUNG = eINSTANCE.getStatistikModell_Beschreibung();

		/**
		 * The meta object literal for the '<em><b>Formel</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL__FORMEL = eINSTANCE.getStatistikModell_Formel();

		/**
		 * The meta object literal for the '<em><b>Software</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL__SOFTWARE = eINSTANCE.getStatistikModell_Software();

		/**
		 * The meta object literal for the '<em><b>Kommentar</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL__KOMMENTAR = eINSTANCE.getStatistikModell_Kommentar();

		/**
		 * The meta object literal for the '<em><b>Parameter</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATISTIK_MODELL__PARAMETER = eINSTANCE.getStatistikModell_Parameter();

		/**
		 * The meta object literal for the '<em><b>Literatur</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATISTIK_MODELL__LITERATUR = eINSTANCE.getStatistikModell_Literatur();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.impl.StatistikModellParameterImpl <em>Statistik Modell Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.impl.StatistikModellParameterImpl
		 * @see de.dim.bfr.impl.BfrPackageImpl#getStatistikModellParameter()
		 * @generated
		 */
		EClass STATISTIK_MODELL_PARAMETER = eINSTANCE.getStatistikModellParameter();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL_PARAMETER__ID = eINSTANCE.getStatistikModellParameter_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL_PARAMETER__NAME = eINSTANCE.getStatistikModellParameter_Name();

		/**
		 * The meta object literal for the '<em><b>Min</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL_PARAMETER__MIN = eINSTANCE.getStatistikModellParameter_Min();

		/**
		 * The meta object literal for the '<em><b>Max</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL_PARAMETER__MAX = eINSTANCE.getStatistikModellParameter_Max();

		/**
		 * The meta object literal for the '<em><b>Beschreibung</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL_PARAMETER__BESCHREIBUNG = eINSTANCE.getStatistikModellParameter_Beschreibung();

		/**
		 * The meta object literal for the '<em><b>Role</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL_PARAMETER__ROLE = eINSTANCE.getStatistikModellParameter_Role();

		/**
		 * The meta object literal for the '<em><b>Integer</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STATISTIK_MODELL_PARAMETER__INTEGER = eINSTANCE.getStatistikModellParameter_Integer();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.impl.StatistikModellKatalogImpl <em>Statistik Modell Katalog</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.impl.StatistikModellKatalogImpl
		 * @see de.dim.bfr.impl.BfrPackageImpl#getStatistikModellKatalog()
		 * @generated
		 */
		EClass STATISTIK_MODELL_KATALOG = eINSTANCE.getStatistikModellKatalog();

		/**
		 * The meta object literal for the '<em><b>Modelle</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATISTIK_MODELL_KATALOG__MODELLE = eINSTANCE.getStatistikModellKatalog_Modelle();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.impl.LiteraturListeImpl <em>Literatur Liste</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.impl.LiteraturListeImpl
		 * @see de.dim.bfr.impl.BfrPackageImpl#getLiteraturListe()
		 * @generated
		 */
		EClass LITERATUR_LISTE = eINSTANCE.getLiteraturListe();

		/**
		 * The meta object literal for the '<em><b>Literatur</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LITERATUR_LISTE__LITERATUR = eINSTANCE.getLiteraturListe_Literatur();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.impl.GeschaetztStatistikModellImpl <em>Geschaetzt Statistik Modell</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.impl.GeschaetztStatistikModellImpl
		 * @see de.dim.bfr.impl.BfrPackageImpl#getGeschaetztStatistikModell()
		 * @generated
		 */
		EClass GESCHAETZT_STATISTIK_MODELL = eINSTANCE.getGeschaetztStatistikModell();

		/**
		 * The meta object literal for the '<em><b>Statistik Model</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GESCHAETZT_STATISTIK_MODELL__STATISTIK_MODEL = eINSTANCE.getGeschaetztStatistikModell_StatistikModel();

		/**
		 * The meta object literal for the '<em><b>Manuell Eingetragen</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GESCHAETZT_STATISTIK_MODELL__MANUELL_EINGETRAGEN = eINSTANCE.getGeschaetztStatistikModell_ManuellEingetragen();

		/**
		 * The meta object literal for the '<em><b>RSquared</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GESCHAETZT_STATISTIK_MODELL__RSQUARED = eINSTANCE.getGeschaetztStatistikModell_RSquared();

		/**
		 * The meta object literal for the '<em><b>Rss</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GESCHAETZT_STATISTIK_MODELL__RSS = eINSTANCE.getGeschaetztStatistikModell_Rss();

		/**
		 * The meta object literal for the '<em><b>Score</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GESCHAETZT_STATISTIK_MODELL__SCORE = eINSTANCE.getGeschaetztStatistikModell_Score();

		/**
		 * The meta object literal for the '<em><b>Kommentar</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GESCHAETZT_STATISTIK_MODELL__KOMMENTAR = eINSTANCE.getGeschaetztStatistikModell_Kommentar();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GESCHAETZT_STATISTIK_MODELL__ID = eINSTANCE.getGeschaetztStatistikModell_Id();

		/**
		 * The meta object literal for the '<em><b>Parameter</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GESCHAETZT_STATISTIK_MODELL__PARAMETER = eINSTANCE.getGeschaetztStatistikModell_Parameter();

		/**
		 * The meta object literal for the '<em><b>Bedingung</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GESCHAETZT_STATISTIK_MODELL__BEDINGUNG = eINSTANCE.getGeschaetztStatistikModell_Bedingung();

		/**
		 * The meta object literal for the '<em><b>Literatur</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GESCHAETZT_STATISTIK_MODELL__LITERATUR = eINSTANCE.getGeschaetztStatistikModell_Literatur();

		/**
		 * The meta object literal for the '<em><b>Parameter Cov Cor</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GESCHAETZT_STATISTIK_MODELL__PARAMETER_COV_COR = eINSTANCE.getGeschaetztStatistikModell_ParameterCovCor();

		/**
		 * The meta object literal for the '<em><b>Response</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GESCHAETZT_STATISTIK_MODELL__RESPONSE = eINSTANCE.getGeschaetztStatistikModell_Response();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.impl.GeschModellParameterImpl <em>Gesch Modell Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.impl.GeschModellParameterImpl
		 * @see de.dim.bfr.impl.BfrPackageImpl#getGeschModellParameter()
		 * @generated
		 */
		EClass GESCH_MODELL_PARAMETER = eINSTANCE.getGeschModellParameter();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GESCH_MODELL_PARAMETER__NAME = eINSTANCE.getGeschModellParameter_Name();

		/**
		 * The meta object literal for the '<em><b>Sd</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GESCH_MODELL_PARAMETER__SD = eINSTANCE.getGeschModellParameter_Sd();

		/**
		 * The meta object literal for the '<em><b>Wert</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GESCH_MODELL_PARAMETER__WERT = eINSTANCE.getGeschModellParameter_Wert();

		/**
		 * The meta object literal for the '<em><b>Versuchs Bedingung</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GESCH_MODELL_PARAMETER__VERSUCHS_BEDINGUNG = eINSTANCE.getGeschModellParameter_VersuchsBedingung();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GESCH_MODELL_PARAMETER__ID = eINSTANCE.getGeschModellParameter_Id();

		/**
		 * The meta object literal for the '<em><b>T</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GESCH_MODELL_PARAMETER__T = eINSTANCE.getGeschModellParameter_T();

		/**
		 * The meta object literal for the '<em><b>P</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GESCH_MODELL_PARAMETER__P = eINSTANCE.getGeschModellParameter_P();

		/**
		 * The meta object literal for the '<em><b>Geschaetztes Modell</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GESCH_MODELL_PARAMETER__GESCHAETZTES_MODELL = eINSTANCE.getGeschModellParameter_GeschaetztesModell();

		/**
		 * The meta object literal for the '<em><b>Model Parameter</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GESCH_MODELL_PARAMETER__MODEL_PARAMETER = eINSTANCE.getGeschModellParameter_ModelParameter();

		/**
		 * The meta object literal for the '<em><b>Ki Oben</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GESCH_MODELL_PARAMETER__KI_OBEN = eINSTANCE.getGeschModellParameter_KiOben();

		/**
		 * The meta object literal for the '<em><b>Ki Unten</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GESCH_MODELL_PARAMETER__KI_UNTEN = eINSTANCE.getGeschModellParameter_KiUnten();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.impl.VersuchsBedingungImpl <em>Versuchs Bedingung</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.impl.VersuchsBedingungImpl
		 * @see de.dim.bfr.impl.BfrPackageImpl#getVersuchsBedingung()
		 * @generated
		 */
		EClass VERSUCHS_BEDINGUNG = eINSTANCE.getVersuchsBedingung();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VERSUCHS_BEDINGUNG__ID = eINSTANCE.getVersuchsBedingung_Id();

		/**
		 * The meta object literal for the '<em><b>Id CB</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VERSUCHS_BEDINGUNG__ID_CB = eINSTANCE.getVersuchsBedingung_IdCB();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.impl.GeschModelListImpl <em>Gesch Model List</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.impl.GeschModelListImpl
		 * @see de.dim.bfr.impl.BfrPackageImpl#getGeschModelList()
		 * @generated
		 */
		EClass GESCH_MODEL_LIST = eINSTANCE.getGeschModelList();

		/**
		 * The meta object literal for the '<em><b>Models</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GESCH_MODEL_LIST__MODELS = eINSTANCE.getGeschModelList_Models();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.impl.VersuchsBedingungListImpl <em>Versuchs Bedingung List</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.impl.VersuchsBedingungListImpl
		 * @see de.dim.bfr.impl.BfrPackageImpl#getVersuchsBedingungList()
		 * @generated
		 */
		EClass VERSUCHS_BEDINGUNG_LIST = eINSTANCE.getVersuchsBedingungList();

		/**
		 * The meta object literal for the '<em><b>Bedingungen</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VERSUCHS_BEDINGUNG_LIST__BEDINGUNGEN = eINSTANCE.getVersuchsBedingungList_Bedingungen();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.impl.MesswerteImpl <em>Messwerte</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.impl.MesswerteImpl
		 * @see de.dim.bfr.impl.BfrPackageImpl#getMesswerte()
		 * @generated
		 */
		EClass MESSWERTE = eINSTANCE.getMesswerte();

		/**
		 * The meta object literal for the '<em><b>Geprueft</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSWERTE__GEPRUEFT = eINSTANCE.getMesswerte_Geprueft();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSWERTE__ID = eINSTANCE.getMesswerte_Id();

		/**
		 * The meta object literal for the '<em><b>Kommentar</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSWERTE__KOMMENTAR = eINSTANCE.getMesswerte_Kommentar();

		/**
		 * The meta object literal for the '<em><b>Zeit Einheit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSWERTE__ZEIT_EINHEIT = eINSTANCE.getMesswerte_ZeitEinheit();

		/**
		 * The meta object literal for the '<em><b>Aw</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSWERTE__AW = eINSTANCE.getMesswerte_Aw();

		/**
		 * The meta object literal for the '<em><b>Co2</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSWERTE__CO2 = eINSTANCE.getMesswerte_Co2();

		/**
		 * The meta object literal for the '<em><b>Druck</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSWERTE__DRUCK = eINSTANCE.getMesswerte_Druck();

		/**
		 * The meta object literal for the '<em><b>Temperatur</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSWERTE__TEMPERATUR = eINSTANCE.getMesswerte_Temperatur();

		/**
		 * The meta object literal for the '<em><b>PH</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSWERTE__PH = eINSTANCE.getMesswerte_PH();

		/**
		 * The meta object literal for the '<em><b>Konzentration</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSWERTE__KONZENTRATION = eINSTANCE.getMesswerte_Konzentration();

		/**
		 * The meta object literal for the '<em><b>Zeit</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSWERTE__ZEIT = eINSTANCE.getMesswerte_Zeit();

		/**
		 * The meta object literal for the '<em><b>Sonstiges</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSWERTE__SONSTIGES = eINSTANCE.getMesswerte_Sonstiges();

		/**
		 * The meta object literal for the '<em><b>Versuchsbedingungen</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSWERTE__VERSUCHSBEDINGUNGEN = eINSTANCE.getMesswerte_Versuchsbedingungen();

		/**
		 * The meta object literal for the '<em><b>Konz Einheit</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSWERTE__KONZ_EINHEIT = eINSTANCE.getMesswerte_KonzEinheit();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.impl.DoubleKennzahlenImpl <em>Double Kennzahlen</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.impl.DoubleKennzahlenImpl
		 * @see de.dim.bfr.impl.BfrPackageImpl#getDoubleKennzahlen()
		 * @generated
		 */
		EClass DOUBLE_KENNZAHLEN = eINSTANCE.getDoubleKennzahlen();

		/**
		 * The meta object literal for the '<em><b>Function X</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__FUNCTION_X = eINSTANCE.getDoubleKennzahlen_FunctionX();

		/**
		 * The meta object literal for the '<em><b>Function XG</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__FUNCTION_XG = eINSTANCE.getDoubleKennzahlen_FunctionXG();

		/**
		 * The meta object literal for the '<em><b>Function Zeit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__FUNCTION_ZEIT = eINSTANCE.getDoubleKennzahlen_FunctionZeit();

		/**
		 * The meta object literal for the '<em><b>Function Zeit G</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__FUNCTION_ZEIT_G = eINSTANCE.getDoubleKennzahlen_FunctionZeitG();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__ID = eINSTANCE.getDoubleKennzahlen_Id();

		/**
		 * The meta object literal for the '<em><b>Lcl95</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__LCL95 = eINSTANCE.getDoubleKennzahlen_Lcl95();

		/**
		 * The meta object literal for the '<em><b>Lcl95 G</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__LCL95_G = eINSTANCE.getDoubleKennzahlen_Lcl95G();

		/**
		 * The meta object literal for the '<em><b>Maximum</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__MAXIMUM = eINSTANCE.getDoubleKennzahlen_Maximum();

		/**
		 * The meta object literal for the '<em><b>Maximum G</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__MAXIMUM_G = eINSTANCE.getDoubleKennzahlen_MaximumG();

		/**
		 * The meta object literal for the '<em><b>Median</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__MEDIAN = eINSTANCE.getDoubleKennzahlen_Median();

		/**
		 * The meta object literal for the '<em><b>Median G</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__MEDIAN_G = eINSTANCE.getDoubleKennzahlen_MedianG();

		/**
		 * The meta object literal for the '<em><b>Minimum</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__MINIMUM = eINSTANCE.getDoubleKennzahlen_Minimum();

		/**
		 * The meta object literal for the '<em><b>Minimum G</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__MINIMUM_G = eINSTANCE.getDoubleKennzahlen_MinimumG();

		/**
		 * The meta object literal for the '<em><b>Standardabweichung</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG = eINSTANCE.getDoubleKennzahlen_Standardabweichung();

		/**
		 * The meta object literal for the '<em><b>Standardabweichung G</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__STANDARDABWEICHUNG_G = eINSTANCE.getDoubleKennzahlen_StandardabweichungG();

		/**
		 * The meta object literal for the '<em><b>Times10 Power Of</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__TIMES10_POWER_OF = eINSTANCE.getDoubleKennzahlen_Times10PowerOf();

		/**
		 * The meta object literal for the '<em><b>Ucl95</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__UCL95 = eINSTANCE.getDoubleKennzahlen_Ucl95();

		/**
		 * The meta object literal for the '<em><b>Ucl95 G</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__UCL95_G = eINSTANCE.getDoubleKennzahlen_Ucl95G();

		/**
		 * The meta object literal for the '<em><b>Undefined ND</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__UNDEFINED_ND = eINSTANCE.getDoubleKennzahlen_UndefinedND();

		/**
		 * The meta object literal for the '<em><b>Verteilung</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__VERTEILUNG = eINSTANCE.getDoubleKennzahlen_Verteilung();

		/**
		 * The meta object literal for the '<em><b>Verteilung G</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__VERTEILUNG_G = eINSTANCE.getDoubleKennzahlen_VerteilungG();

		/**
		 * The meta object literal for the '<em><b>Wert</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__WERT = eINSTANCE.getDoubleKennzahlen_Wert();

		/**
		 * The meta object literal for the '<em><b>Wert G</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__WERT_G = eINSTANCE.getDoubleKennzahlen_WertG();

		/**
		 * The meta object literal for the '<em><b>Wert Typ</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__WERT_TYP = eINSTANCE.getDoubleKennzahlen_WertTyp();

		/**
		 * The meta object literal for the '<em><b>Wiederholungen</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN = eINSTANCE.getDoubleKennzahlen_Wiederholungen();

		/**
		 * The meta object literal for the '<em><b>Wiederholungen G</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__WIEDERHOLUNGEN_G = eINSTANCE.getDoubleKennzahlen_WiederholungenG();

		/**
		 * The meta object literal for the '<em><b>X</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_KENNZAHLEN__X = eINSTANCE.getDoubleKennzahlen_X();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.impl.ParameterCovCorImpl <em>Parameter Cov Cor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.impl.ParameterCovCorImpl
		 * @see de.dim.bfr.impl.BfrPackageImpl#getParameterCovCor()
		 * @generated
		 */
		EClass PARAMETER_COV_COR = eINSTANCE.getParameterCovCor();

		/**
		 * The meta object literal for the '<em><b>Parameter1</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETER_COV_COR__PARAMETER1 = eINSTANCE.getParameterCovCor_Parameter1();

		/**
		 * The meta object literal for the '<em><b>Parameter2</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETER_COV_COR__PARAMETER2 = eINSTANCE.getParameterCovCor_Parameter2();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER_COV_COR__VALUE = eINSTANCE.getParameterCovCor_Value();

		/**
		 * The meta object literal for the '<em><b>Cor</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER_COV_COR__COR = eINSTANCE.getParameterCovCor_Cor();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER_COV_COR__ID = eINSTANCE.getParameterCovCor_Id();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.impl.EinheitenImpl <em>Einheiten</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.impl.EinheitenImpl
		 * @see de.dim.bfr.impl.BfrPackageImpl#getEinheiten()
		 * @generated
		 */
		EClass EINHEITEN = eINSTANCE.getEinheiten();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EINHEITEN__ID = eINSTANCE.getEinheiten_Id();

		/**
		 * The meta object literal for the '<em><b>Einheit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EINHEITEN__EINHEIT = eINSTANCE.getEinheiten_Einheit();

		/**
		 * The meta object literal for the '<em><b>Beschreibung</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EINHEITEN__BESCHREIBUNG = eINSTANCE.getEinheiten_Beschreibung();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.FreigabeTyp <em>Freigabe Typ</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.FreigabeTyp
		 * @see de.dim.bfr.impl.BfrPackageImpl#getFreigabeTyp()
		 * @generated
		 */
		EEnum FREIGABE_TYP = eINSTANCE.getFreigabeTyp();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.LiteraturTyp <em>Literatur Typ</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.LiteraturTyp
		 * @see de.dim.bfr.impl.BfrPackageImpl#getLiteraturTyp()
		 * @generated
		 */
		EEnum LITERATUR_TYP = eINSTANCE.getLiteraturTyp();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.LevelTyp <em>Level Typ</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.LevelTyp
		 * @see de.dim.bfr.impl.BfrPackageImpl#getLevelTyp()
		 * @generated
		 */
		EEnum LEVEL_TYP = eINSTANCE.getLevelTyp();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.KlasseTyp <em>Klasse Typ</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.KlasseTyp
		 * @see de.dim.bfr.impl.BfrPackageImpl#getKlasseTyp()
		 * @generated
		 */
		EEnum KLASSE_TYP = eINSTANCE.getKlasseTyp();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.SoftwareType <em>Software Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.SoftwareType
		 * @see de.dim.bfr.impl.BfrPackageImpl#getSoftwareType()
		 * @generated
		 */
		EEnum SOFTWARE_TYPE = eINSTANCE.getSoftwareType();

		/**
		 * The meta object literal for the '{@link de.dim.bfr.ParameterRoleType <em>Parameter Role Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see de.dim.bfr.ParameterRoleType
		 * @see de.dim.bfr.impl.BfrPackageImpl#getParameterRoleType()
		 * @generated
		 */
		EEnum PARAMETER_ROLE_TYPE = eINSTANCE.getParameterRoleType();

	}

} //BfrPackage
