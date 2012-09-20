/**
 * $Id: KlasseTyp.java 651 2012-01-24 09:59:12Z sdoerl $
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Klasse Typ</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see de.dim.bfr.BfrPackage#getKlasseTyp()
 * @model
 * @generated
 */
public enum KlasseTyp implements Enumerator {
	/**
	 * The '<em><b>UNKNOWN</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #UNKNOWN_VALUE
	 * @generated
	 * @ordered
	 */
	UNKNOWN(0, "UNKNOWN", "Unbekannt"), /**
	 * The '<em><b>GROWTH</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #GROWTH_VALUE
	 * @generated
	 * @ordered
	 */
	GROWTH(1, "GROWTH", "Growth"), /**
	 * The '<em><b>INACTIVATION</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #INACTIVATION_VALUE
	 * @generated
	 * @ordered
	 */
	INACTIVATION(2, "INACTIVATION", "Inactivation"), /**
	 * The '<em><b>SURVIVAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SURVIVAL_VALUE
	 * @generated
	 * @ordered
	 */
	SURVIVAL(3, "SURVIVAL", "Survival"), /**
	 * The '<em><b>GROWTH INACTIVATION</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #GROWTH_INACTIVATION_VALUE
	 * @generated
	 * @ordered
	 */
	GROWTH_INACTIVATION(4, "GROWTH_INACTIVATION", "Growth/Inactivation"), /**
	 * The '<em><b>INACTIVATION SURVIVAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #INACTIVATION_SURVIVAL_VALUE
	 * @generated
	 * @ordered
	 */
	INACTIVATION_SURVIVAL(5, "INACTIVATION_SURVIVAL", "Inactivation/Survival"), /**
	 * The '<em><b>GROWTH SURVIVAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #GROWTH_SURVIVAL_VALUE
	 * @generated
	 * @ordered
	 */
	GROWTH_SURVIVAL(6, "GROWTH_SURVIVAL", "Growth/Survival"), /**
	 * The '<em><b>GROWTH INACTIVATION SURVIVAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #GROWTH_INACTIVATION_SURVIVAL_VALUE
	 * @generated
	 * @ordered
	 */
	GROWTH_INACTIVATION_SURVIVAL(7, "GROWTH_INACTIVATION_SURVIVAL", "Growth/Inactivation/Survival"), /**
	 * The '<em><b>T</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #T_VALUE
	 * @generated
	 * @ordered
	 */
	T(8, "T", "T"), /**
	 * The '<em><b>PH</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PH_VALUE
	 * @generated
	 * @ordered
	 */
	PH(9, "PH", "pH"), /**
	 * The '<em><b>AW</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #AW_VALUE
	 * @generated
	 * @ordered
	 */
	AW(10, "AW", "aw"), /**
	 * The '<em><b>TPH</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TPH_VALUE
	 * @generated
	 * @ordered
	 */
	TPH(11, "T_PH", "T/pH"), /**
	 * The '<em><b>TAW</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TAW_VALUE
	 * @generated
	 * @ordered
	 */
	TAW(12, "T_AW", "T/aw"), /**
	 * The '<em><b>PH AW</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PH_AW_VALUE
	 * @generated
	 * @ordered
	 */
	PH_AW(13, "PH_AW", "pH/aw"), /**
	 * The '<em><b>TPH AW</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TPH_AW_VALUE
	 * @generated
	 * @ordered
	 */
	TPH_AW(14, "T_PH_AW", "T/pH/aw");

	/**
	 * The '<em><b>UNKNOWN</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>UNKNOWN</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #UNKNOWN
	 * @model literal="Unbekannt"
	 * @generated
	 * @ordered
	 */
	public static final int UNKNOWN_VALUE = 0;

	/**
	 * The '<em><b>GROWTH</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>GROWTH</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #GROWTH
	 * @model literal="Growth"
	 * @generated
	 * @ordered
	 */
	public static final int GROWTH_VALUE = 1;

	/**
	 * The '<em><b>INACTIVATION</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>INACTIVATION</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #INACTIVATION
	 * @model literal="Inactivation"
	 * @generated
	 * @ordered
	 */
	public static final int INACTIVATION_VALUE = 2;

	/**
	 * The '<em><b>SURVIVAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SURVIVAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SURVIVAL
	 * @model literal="Survival"
	 * @generated
	 * @ordered
	 */
	public static final int SURVIVAL_VALUE = 3;

	/**
	 * The '<em><b>GROWTH INACTIVATION</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>GROWTH INACTIVATION</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #GROWTH_INACTIVATION
	 * @model literal="Growth/Inactivation"
	 * @generated
	 * @ordered
	 */
	public static final int GROWTH_INACTIVATION_VALUE = 4;

	/**
	 * The '<em><b>INACTIVATION SURVIVAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>INACTIVATION SURVIVAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #INACTIVATION_SURVIVAL
	 * @model literal="Inactivation/Survival"
	 * @generated
	 * @ordered
	 */
	public static final int INACTIVATION_SURVIVAL_VALUE = 5;

	/**
	 * The '<em><b>GROWTH SURVIVAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>GROWTH SURVIVAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #GROWTH_SURVIVAL
	 * @model literal="Growth/Survival"
	 * @generated
	 * @ordered
	 */
	public static final int GROWTH_SURVIVAL_VALUE = 6;

	/**
	 * The '<em><b>GROWTH INACTIVATION SURVIVAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>GROWTH INACTIVATION SURVIVAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #GROWTH_INACTIVATION_SURVIVAL
	 * @model literal="Growth/Inactivation/Survival"
	 * @generated
	 * @ordered
	 */
	public static final int GROWTH_INACTIVATION_SURVIVAL_VALUE = 7;

	/**
	 * The '<em><b>T</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>T</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #T
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int T_VALUE = 8;

	/**
	 * The '<em><b>PH</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>PH</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PH
	 * @model literal="pH"
	 * @generated
	 * @ordered
	 */
	public static final int PH_VALUE = 9;

	/**
	 * The '<em><b>AW</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>AW</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #AW
	 * @model literal="aw"
	 * @generated
	 * @ordered
	 */
	public static final int AW_VALUE = 10;

	/**
	 * The '<em><b>TPH</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TPH</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TPH
	 * @model name="T_PH" literal="T/pH"
	 * @generated
	 * @ordered
	 */
	public static final int TPH_VALUE = 11;

	/**
	 * The '<em><b>TAW</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TAW</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TAW
	 * @model name="T_AW" literal="T/aw"
	 * @generated
	 * @ordered
	 */
	public static final int TAW_VALUE = 12;

	/**
	 * The '<em><b>PH AW</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>PH AW</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PH_AW
	 * @model literal="pH/aw"
	 * @generated
	 * @ordered
	 */
	public static final int PH_AW_VALUE = 13;

	/**
	 * The '<em><b>TPH AW</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TPH AW</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TPH_AW
	 * @model name="T_PH_AW" literal="T/pH/aw"
	 * @generated
	 * @ordered
	 */
	public static final int TPH_AW_VALUE = 14;

	/**
	 * An array of all the '<em><b>Klasse Typ</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final KlasseTyp[] VALUES_ARRAY =
		new KlasseTyp[] {
			UNKNOWN,
			GROWTH,
			INACTIVATION,
			SURVIVAL,
			GROWTH_INACTIVATION,
			INACTIVATION_SURVIVAL,
			GROWTH_SURVIVAL,
			GROWTH_INACTIVATION_SURVIVAL,
			T,
			PH,
			AW,
			TPH,
			TAW,
			PH_AW,
			TPH_AW,
		};

	/**
	 * A public read-only list of all the '<em><b>Klasse Typ</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<KlasseTyp> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Klasse Typ</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static KlasseTyp get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			KlasseTyp result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Klasse Typ</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static KlasseTyp getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			KlasseTyp result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Klasse Typ</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static KlasseTyp get(int value) {
		switch (value) {
			case UNKNOWN_VALUE: return UNKNOWN;
			case GROWTH_VALUE: return GROWTH;
			case INACTIVATION_VALUE: return INACTIVATION;
			case SURVIVAL_VALUE: return SURVIVAL;
			case GROWTH_INACTIVATION_VALUE: return GROWTH_INACTIVATION;
			case INACTIVATION_SURVIVAL_VALUE: return INACTIVATION_SURVIVAL;
			case GROWTH_SURVIVAL_VALUE: return GROWTH_SURVIVAL;
			case GROWTH_INACTIVATION_SURVIVAL_VALUE: return GROWTH_INACTIVATION_SURVIVAL;
			case T_VALUE: return T;
			case PH_VALUE: return PH;
			case AW_VALUE: return AW;
			case TPH_VALUE: return TPH;
			case TAW_VALUE: return TAW;
			case PH_AW_VALUE: return PH_AW;
			case TPH_AW_VALUE: return TPH_AW;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private KlasseTyp(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
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
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //KlasseTyp
