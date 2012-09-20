/**
 * $Id: StatistikModellKatalog.java 651 2012-01-24 09:59:12Z sdoerl $
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
package de.dim.bfr;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Statistik Modell Katalog</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link de.dim.bfr.StatistikModellKatalog#getModelle <em>Modelle</em>}</li>
 * </ul>
 * </p>
 *
 * @see de.dim.bfr.BfrPackage#getStatistikModellKatalog()
 * @model
 * @generated
 */
public interface StatistikModellKatalog extends EObject {
	/**
	 * Returns the value of the '<em><b>Modelle</b></em>' containment reference list.
	 * The list contents are of type {@link de.dim.bfr.StatistikModell}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Modelle</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Modelle</em>' containment reference list.
	 * @see de.dim.bfr.BfrPackage#getStatistikModellKatalog_Modelle()
	 * @model containment="true"
	 * @generated
	 */
	EList<StatistikModell> getModelle();

} // StatistikModellKatalog
