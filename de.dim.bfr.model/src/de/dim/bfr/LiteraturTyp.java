/**
 * $Id: LiteraturTyp.java 651 2012-01-24 09:59:12Z sdoerl $
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
package de.dim.bfr;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Literatur Typ</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see de.dim.bfr.BfrPackage#getLiteraturTyp()
 * @model
 * @generated
 */
public enum LiteraturTyp implements Enumerator {
	/**
	 * The '<em><b>UNBEKANNT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #UNBEKANNT_VALUE
	 * @generated
	 * @ordered
	 */
	UNBEKANNT(0, "UNBEKANNT", "unbekannt"),

	/**
	 * The '<em><b>PAPER</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PAPER_VALUE
	 * @generated
	 * @ordered
	 */
	PAPER(1, "PAPER", "Paper"), /**
	 * The '<em><b>BOOK</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BOOK_VALUE
	 * @generated
	 * @ordered
	 */
	BOOK(6, "BOOK", "Buch"), /**
	 * The '<em><b>LA</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #LA_VALUE
	 * @generated
	 * @ordered
	 */
	LA(3, "LA", "LA"), /**
	 * The '<em><b>MANUAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MANUAL_VALUE
	 * @generated
	 * @ordered
	 */
	MANUAL(4, "MANUAL", "Handbuch"), /**
	 * The '<em><b>TEST BOOK</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TEST_BOOK_VALUE
	 * @generated
	 * @ordered
	 */
	TEST_BOOK(5, "TEST_BOOK", "Laborbuch"), /**
	 * The '<em><b>SOP</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SOP_VALUE
	 * @generated
	 * @ordered
	 */
	SOP(2, "SOP", "SOP"), /**
	 * The '<em><b>WEBSITE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #WEBSITE_VALUE
	 * @generated
	 * @ordered
	 */
	WEBSITE(7, "WEBSITE", "Webseite"), /**
	 * The '<em><b>REPORT</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #REPORT_VALUE
	 * @generated
	 * @ordered
	 */
	REPORT(8, "REPORT", "Bericht");

	/**
	 * The '<em><b>UNBEKANNT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>UNBEKANNT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #UNBEKANNT
	 * @model literal="unbekannt"
	 * @generated
	 * @ordered
	 */
	public static final int UNBEKANNT_VALUE = 0;

	/**
	 * The '<em><b>PAPER</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>PAPER</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #PAPER
	 * @model literal="Paper"
	 * @generated
	 * @ordered
	 */
	public static final int PAPER_VALUE = 1;

	/**
	 * The '<em><b>BOOK</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>BOOK</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BOOK
	 * @model literal="Buch"
	 * @generated
	 * @ordered
	 */
	public static final int BOOK_VALUE = 6;

	/**
	 * The '<em><b>LA</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>LA</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #LA
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int LA_VALUE = 3;

	/**
	 * The '<em><b>MANUAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>MANUAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #MANUAL
	 * @model literal="Handbuch"
	 * @generated
	 * @ordered
	 */
	public static final int MANUAL_VALUE = 4;

	/**
	 * The '<em><b>TEST BOOK</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>TEST BOOK</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #TEST_BOOK
	 * @model literal="Laborbuch"
	 * @generated
	 * @ordered
	 */
	public static final int TEST_BOOK_VALUE = 5;

	/**
	 * The '<em><b>SOP</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>SOP</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #SOP
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int SOP_VALUE = 2;

	/**
	 * The '<em><b>WEBSITE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>WEBSITE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #WEBSITE
	 * @model literal="Webseite"
	 * @generated
	 * @ordered
	 */
	public static final int WEBSITE_VALUE = 7;

	/**
	 * The '<em><b>REPORT</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>REPORT</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #REPORT
	 * @model literal="Bericht"
	 * @generated
	 * @ordered
	 */
	public static final int REPORT_VALUE = 8;

	/**
	 * An array of all the '<em><b>Literatur Typ</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final LiteraturTyp[] VALUES_ARRAY =
		new LiteraturTyp[] {
			UNBEKANNT,
			PAPER,
			BOOK,
			LA,
			MANUAL,
			TEST_BOOK,
			SOP,
			WEBSITE,
			REPORT,
		};

	/**
	 * A public read-only list of all the '<em><b>Literatur Typ</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<LiteraturTyp> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Literatur Typ</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LiteraturTyp get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LiteraturTyp result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Literatur Typ</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LiteraturTyp getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			LiteraturTyp result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Literatur Typ</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static LiteraturTyp get(int value) {
		switch (value) {
			case UNBEKANNT_VALUE: return UNBEKANNT;
			case PAPER_VALUE: return PAPER;
			case BOOK_VALUE: return BOOK;
			case LA_VALUE: return LA;
			case MANUAL_VALUE: return MANUAL;
			case TEST_BOOK_VALUE: return TEST_BOOK;
			case SOP_VALUE: return SOP;
			case WEBSITE_VALUE: return WEBSITE;
			case REPORT_VALUE: return REPORT;
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
	private LiteraturTyp(int value, String name, String literal) {
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
	
} //LiteraturTyp
