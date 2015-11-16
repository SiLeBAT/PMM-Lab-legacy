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

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.pmf.ModelType;

/**
 * @author Miguel Alba
 */
public class MetadataTest {
	
	private MetadataImpl metadata = new MetadataImpl();
	
	@Test
	public void testGivenNameAccesors() {
		assertFalse(metadata.isSetGivenName());
		assertNull(metadata.getGivenName());

		metadata.setGivenName("doe");
		assertTrue(metadata.isSetGivenName());
		assertEquals("doe", metadata.getGivenName());
	}
	@Test
	public void testFamilyNameAccesors() {
		assertFalse(metadata.isSetFamilyName());
		assertNull(metadata.getFamilyName());
		
		metadata.setFamilyName("doe");
		assertTrue(metadata.isSetFamilyName());
		assertEquals("doe", metadata.getFamilyName());
	}
	
	@Test
	public void testContactAccesors() {
		assertFalse(metadata.isSetContact());
		assertNull(metadata.getContact());
		
		metadata.setContact("doe");
		assertTrue(metadata.isSetContact());
		assertEquals("doe", metadata.getContact());
	}
	
	@Test
	public void testCreatedDateAccesors() {
		assertFalse(metadata.isSetCreatedDate());
		assertNull(metadata.getCreatedDate());
		
		metadata.setCreatedDate("Wed Sep 02 02:00:00 CEST 2015");
		assertTrue(metadata.isSetCreatedDate());
		assertEquals("Wed Sep 02 02:00:00 CEST 2015", metadata.getCreatedDate());
	}

	@Test
	public void testModifiedDateAccesors() {
		assertFalse(metadata.isSetModifiedDate());
		assertNull(metadata.getModifiedDate());
		
		metadata.setModifiedDate("Wed Sep 02 02:00:00 CEST 2015");
		assertTrue(metadata.isSetModifiedDate());
		assertEquals("Wed Sep 02 02:00:00 CEST 2015", metadata.getModifiedDate());
	}
	
	@Test
	public void testTypeAccesors() {
		assertFalse(metadata.isSetType());
		assertNull(metadata.getType());
		
		metadata.setType(ModelType.EXPERIMENTAL_DATA);
		assertTrue(metadata.isSetType());
		assertEquals(ModelType.EXPERIMENTAL_DATA, metadata.getType());
	}
	
	@Test
	public void testRightAccesors() {
		assertFalse(metadata.isSetRights());
		assertNull(metadata.getRights());
		
		metadata.setRights("CC");
		assertTrue(metadata.isSetRights());
		assertEquals("CC", metadata.getRights());
	}
	
	@Test
	public void testReferenceLinkAccesors() {
		assertFalse(metadata.isSetReferenceLink());
		assertNull(metadata.getReferenceLink());
		
		metadata.setReferenceLink("www.bfr.bund.de");
		assertTrue(metadata.isSetReferenceLink());
		assertEquals("www.bfr.bund.de", metadata.getReferenceLink());
	}
}
