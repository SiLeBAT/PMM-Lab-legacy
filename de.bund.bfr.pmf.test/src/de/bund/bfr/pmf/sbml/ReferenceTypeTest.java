/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
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
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.pmf.sbml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * @author Miguel Alba
 */
public class ReferenceTypeTest {

	private ReferenceType paper = ReferenceType.Paper;
	private ReferenceType sop = ReferenceType.SOP;
	private ReferenceType la = ReferenceType.LA;
	private ReferenceType handbuch = ReferenceType.Handbuch;
	private ReferenceType laborbuch = ReferenceType.Laborbuch;
	private ReferenceType buch = ReferenceType.Buch;
	private ReferenceType webseite = ReferenceType.Webseite;
	private ReferenceType bericht = ReferenceType.Bericht;
	
	@Test
	public void testValue() {
		assertEquals(paper.value(), 1);
		assertEquals(sop.value(), 2);
		assertEquals(la.value(), 3);
		assertEquals(handbuch.value(), 4);
		assertEquals(laborbuch.value(), 5);
		assertEquals(buch.value(), 6);
		assertEquals(webseite.value(), 7);
		assertEquals(bericht.value(), 8);
	}
	
	@Test
	public void testFromValue() {
		assertEquals(paper, ReferenceType.fromValue(1));
		assertEquals(sop, ReferenceType.fromValue(2));
		assertEquals(la, ReferenceType.fromValue(3));
		assertEquals(handbuch, ReferenceType.fromValue(4));
		assertEquals(laborbuch, ReferenceType.fromValue(5));
		assertEquals(buch, ReferenceType.fromValue(6));
		assertEquals(webseite, ReferenceType.fromValue(7));
		assertEquals(bericht, ReferenceType.fromValue(8));
		assertNull(ReferenceType.fromValue(Integer.MAX_VALUE));
	}
}
