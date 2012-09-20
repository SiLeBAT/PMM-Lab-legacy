/**
 * $Id: BfrFactoryImpl.java 651 2012-01-24 09:59:12Z sdoerl $
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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

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
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class BfrFactoryImpl extends EFactoryImpl implements BfrFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static BfrFactory init() {
		try {
			BfrFactory theBfrFactory = (BfrFactory)EPackage.Registry.INSTANCE.getEFactory("http://bfr.bund.de"); 
			if (theBfrFactory != null) {
				return theBfrFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new BfrFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BfrFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case BfrPackage.LITERATUR: return createLiteratur();
			case BfrPackage.STATISTIK_MODELL: return createStatistikModell();
			case BfrPackage.STATISTIK_MODELL_PARAMETER: return createStatistikModellParameter();
			case BfrPackage.STATISTIK_MODELL_KATALOG: return createStatistikModellKatalog();
			case BfrPackage.LITERATUR_LISTE: return createLiteraturListe();
			case BfrPackage.GESCHAETZT_STATISTIK_MODELL: return createGeschaetztStatistikModell();
			case BfrPackage.GESCH_MODELL_PARAMETER: return createGeschModellParameter();
			case BfrPackage.VERSUCHS_BEDINGUNG: return createVersuchsBedingung();
			case BfrPackage.GESCH_MODEL_LIST: return createGeschModelList();
			case BfrPackage.VERSUCHS_BEDINGUNG_LIST: return createVersuchsBedingungList();
			case BfrPackage.MESSWERTE: return createMesswerte();
			case BfrPackage.DOUBLE_KENNZAHLEN: return createDoubleKennzahlen();
			case BfrPackage.PARAMETER_COV_COR: return createParameterCovCor();
			case BfrPackage.EINHEITEN: return createEinheiten();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case BfrPackage.FREIGABE_TYP:
				return createFreigabeTypFromString(eDataType, initialValue);
			case BfrPackage.LITERATUR_TYP:
				return createLiteraturTypFromString(eDataType, initialValue);
			case BfrPackage.LEVEL_TYP:
				return createLevelTypFromString(eDataType, initialValue);
			case BfrPackage.KLASSE_TYP:
				return createKlasseTypFromString(eDataType, initialValue);
			case BfrPackage.SOFTWARE_TYPE:
				return createSoftwareTypeFromString(eDataType, initialValue);
			case BfrPackage.PARAMETER_ROLE_TYPE:
				return createParameterRoleTypeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case BfrPackage.FREIGABE_TYP:
				return convertFreigabeTypToString(eDataType, instanceValue);
			case BfrPackage.LITERATUR_TYP:
				return convertLiteraturTypToString(eDataType, instanceValue);
			case BfrPackage.LEVEL_TYP:
				return convertLevelTypToString(eDataType, instanceValue);
			case BfrPackage.KLASSE_TYP:
				return convertKlasseTypToString(eDataType, instanceValue);
			case BfrPackage.SOFTWARE_TYPE:
				return convertSoftwareTypeToString(eDataType, instanceValue);
			case BfrPackage.PARAMETER_ROLE_TYPE:
				return convertParameterRoleTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Literatur createLiteratur() {
		LiteraturImpl literatur = new LiteraturImpl();
		return literatur;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatistikModell createStatistikModell() {
		StatistikModellImpl statistikModell = new StatistikModellImpl();
		return statistikModell;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatistikModellParameter createStatistikModellParameter() {
		StatistikModellParameterImpl statistikModellParameter = new StatistikModellParameterImpl();
		return statistikModellParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatistikModellKatalog createStatistikModellKatalog() {
		StatistikModellKatalogImpl statistikModellKatalog = new StatistikModellKatalogImpl();
		return statistikModellKatalog;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LiteraturListe createLiteraturListe() {
		LiteraturListeImpl literaturListe = new LiteraturListeImpl();
		return literaturListe;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeschaetztStatistikModell createGeschaetztStatistikModell() {
		GeschaetztStatistikModellImpl geschaetztStatistikModell = new GeschaetztStatistikModellImpl();
		return geschaetztStatistikModell;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeschModellParameter createGeschModellParameter() {
		GeschModellParameterImpl geschModellParameter = new GeschModellParameterImpl();
		return geschModellParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VersuchsBedingung createVersuchsBedingung() {
		VersuchsBedingungImpl versuchsBedingung = new VersuchsBedingungImpl();
		return versuchsBedingung;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GeschModelList createGeschModelList() {
		GeschModelListImpl geschModelList = new GeschModelListImpl();
		return geschModelList;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VersuchsBedingungList createVersuchsBedingungList() {
		VersuchsBedingungListImpl versuchsBedingungList = new VersuchsBedingungListImpl();
		return versuchsBedingungList;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Messwerte createMesswerte() {
		MesswerteImpl messwerte = new MesswerteImpl();
		return messwerte;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DoubleKennzahlen createDoubleKennzahlen() {
		DoubleKennzahlenImpl doubleKennzahlen = new DoubleKennzahlenImpl();
		return doubleKennzahlen;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterCovCor createParameterCovCor() {
		ParameterCovCorImpl parameterCovCor = new ParameterCovCorImpl();
		return parameterCovCor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Einheiten createEinheiten() {
		EinheitenImpl einheiten = new EinheitenImpl();
		return einheiten;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FreigabeTyp createFreigabeTypFromString(EDataType eDataType, String initialValue) {
		FreigabeTyp result = FreigabeTyp.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFreigabeTypToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LiteraturTyp createLiteraturTypFromString(EDataType eDataType, String initialValue) {
		LiteraturTyp result = LiteraturTyp.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLiteraturTypToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LevelTyp createLevelTypFromString(EDataType eDataType, String initialValue) {
		LevelTyp result = LevelTyp.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLevelTypToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KlasseTyp createKlasseTypFromString(EDataType eDataType, String initialValue) {
		KlasseTyp result = KlasseTyp.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertKlasseTypToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SoftwareType createSoftwareTypeFromString(EDataType eDataType, String initialValue) {
		SoftwareType result = SoftwareType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertSoftwareTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterRoleType createParameterRoleTypeFromString(EDataType eDataType, String initialValue) {
		ParameterRoleType result = ParameterRoleType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertParameterRoleTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BfrPackage getBfrPackage() {
		return (BfrPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static BfrPackage getPackage() {
		return BfrPackage.eINSTANCE;
	}

} //BfrFactoryImpl
