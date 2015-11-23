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
public class MetadataImplTest {
	
	private MetadataImpl metadata = new MetadataImpl();
	
	@Test
	public void testGivenNameAccesors() {
		assertFalse(metadata.isSetGivenName());
		assertNull(metadata.getGivenName());

		// setGivenName should ignore null strings
		metadata.setGivenName(null);
		assertFalse(metadata.isSetGivenName());
		assertNull(metadata.getGivenName());
		
		// setGivenName should ignore empty strings
		metadata.setGivenName("");
		assertFalse(metadata.isSetGivenName());
		assertNull(metadata.getGivenName());
		
		// setGivenName should accept non-empty strings
		metadata.setGivenName("doe");
		assertTrue(metadata.isSetGivenName());
		assertEquals("doe", metadata.getGivenName());
	}
	@Test
	public void testFamilyNameAccesors() {
		assertFalse(metadata.isSetFamilyName());
		assertNull(metadata.getFamilyName());
		
		// setFamilyName should ignore null strings
		metadata.setFamilyName(null);
		assertFalse(metadata.isSetFamilyName());
		assertNull(metadata.getFamilyName());
		
		// setFamilyName should ignore empty strings
		metadata.setFamilyName("");
		assertFalse(metadata.isSetContact());
		assertNull(metadata.getContact());
		
		// setFamilyName should accept non-empty strings
		metadata.setFamilyName("doe");
		assertTrue(metadata.isSetFamilyName());
		assertEquals("doe", metadata.getFamilyName());
	}
	
	@Test
	public void testContactAccesors() {
		assertFalse(metadata.isSetContact());
		assertNull(metadata.getContact());
		
		// setContact should ignore null strings
		metadata.setContact(null);
		assertFalse(metadata.isSetContact());
		assertNull(metadata.getContact());
		
		// setContact should accept non-empty strings
		metadata.setContact("");
		assertFalse(metadata.isSetContact());
		assertNull(metadata.getContact());
		
		// setContact should accept non-empty strings
		metadata.setContact("doe");
		assertTrue(metadata.isSetContact());
		assertEquals("doe", metadata.getContact());
	}
	
	@Test
	public void testCreatedDateAccesors() {
		assertFalse(metadata.isSetCreatedDate());
		assertNull(metadata.getCreatedDate());
		
		// setCreatedDate should ignore null strings
		metadata.setCreatedDate(null);
		assertFalse(metadata.isSetCreatedDate());
		assertNull(metadata.getCreatedDate());
		
		// setCreatedDate should ignore empty strings
		metadata.setCreatedDate("");
		assertFalse(metadata.isSetCreatedDate());
		assertNull(metadata.getCreatedDate());
		
		// setCreatedDate should accept non-empty strings
		metadata.setCreatedDate("Wed Sep 02 02:00:00 CEST 2015");
		assertTrue(metadata.isSetCreatedDate());
		assertEquals("Wed Sep 02 02:00:00 CEST 2015", metadata.getCreatedDate());
	}

	@Test
	public void testModifiedDateAccesors() {
		assertFalse(metadata.isSetModifiedDate());
		assertNull(metadata.getModifiedDate());
		
		// setModifiedDate should ignore null strings
		metadata.setModifiedDate(null);
		assertFalse(metadata.isSetModifiedDate());
		assertNull(metadata.getModifiedDate());
		
		// setModifiedDate should ignore empty strings
		metadata.setModifiedDate("");
		assertFalse(metadata.isSetModifiedDate());
		assertNull(metadata.getModifiedDate());
		
		// setModifiedDate should accept non-empty strings
		metadata.setModifiedDate("Wed Sep 02 02:00:00 CEST 2015");
		assertTrue(metadata.isSetModifiedDate());
		assertEquals("Wed Sep 02 02:00:00 CEST 2015", metadata.getModifiedDate());
	}
	
	@Test
	public void testTypeAccesors() {
		assertFalse(metadata.isSetType());
		assertNull(metadata.getType());
		
		// setType should ignore null values
		metadata.setType(null);
		assertFalse(metadata.isSetType());
		assertNull(metadata.getType());
		
		// setType should accept non-null ModelType values
		metadata.setType(ModelType.EXPERIMENTAL_DATA);
		assertTrue(metadata.isSetType());
		assertEquals(ModelType.EXPERIMENTAL_DATA, metadata.getType());
	}
	
	@Test
	public void testRightAccesors() {
		assertFalse(metadata.isSetRights());
		assertNull(metadata.getRights());
		
		// setRights should ignore null strings
		metadata.setRights(null);
		assertFalse(metadata.isSetRights());
		assertNull(metadata.getRights());
		
		// setRights should ignore empty strings
		metadata.setRights("");
		assertFalse(metadata.isSetRights());
		assertNull(metadata.getRights());

		// setRights should accept non-empty strings
		metadata.setRights("CC");
		assertTrue(metadata.isSetRights());
		assertEquals("CC", metadata.getRights());
	}
	
	@Test
	public void testReferenceLinkAccesors() {
		assertFalse(metadata.isSetReferenceLink());
		assertNull(metadata.getReferenceLink());
		
		// setReferenceLink should ignore null strings
		metadata.setReferenceLink(null);
		assertFalse(metadata.isSetReferenceLink());
		assertNull(metadata.getReferenceLink());
		
		// setReferenceLink should ignore empty strings
		metadata.setReferenceLink("");
		assertFalse(metadata.isSetReferenceLink());
		assertNull(metadata.getReferenceLink());
		
		// setReferenceLink should accept non-empty strings
		metadata.setReferenceLink("www.bfr.bund.de");
		assertTrue(metadata.isSetReferenceLink());
		assertEquals("www.bfr.bund.de", metadata.getReferenceLink());
	}
}
