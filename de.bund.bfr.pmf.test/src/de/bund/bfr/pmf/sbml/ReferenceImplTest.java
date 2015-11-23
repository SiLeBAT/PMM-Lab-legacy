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

/**
 * @author Miguel Alba
 */
public class ReferenceImplTest {

	private Reference ref = new ReferenceImpl(null, null, null, null, null, null, null, null, null, null, null, null);

	@Test
	public void testAbstractTextAccesors() {
		assertFalse(ref.isSetAbstractText());
		assertNull(ref.getAbstractText());

		// setAbstractText should ignore null strings
		ref.setAbstractText(null);
		assertFalse(ref.isSetAbstractText());
		assertNull(ref.getAbstractText());

		// setAbstractText should accept non-empty strings
		ref.setAbstractText("A new member ...");
		assertTrue(ref.isSetAbstractText());
		assertEquals("A new member ...", ref.getAbstractText());
	}

	@Test
	public void testYearAccesors() {
		assertFalse(ref.isSetYear());
		assertNull(ref.getYear());

		ref.setYear(1994);
		assertTrue(ref.isSetYear());
		assertTrue(1994 == ref.getYear());
	}

	@Test
	public void testAuthorAccesors() {
		assertFalse(ref.isSetAuthor());
		assertNull(ref.getAuthor());

		// setAuthor should ignore null strings
		ref.setAuthor(null);
		assertFalse(ref.isSetAuthor());
		assertNull(ref.getAuthor());

		// setAuthor should ignore empty strings
		ref.setAuthor("");
		assertFalse(ref.isSetAuthor());
		assertNull(ref.getAuthor());

		// setAuthor should accept non-empty strings
		ref.setAuthor("Baranyi, J.");
		assertTrue(ref.isSetAuthor());
		assertEquals("Baranyi, J.", ref.getAuthor());
	}

	@Test
	public void testTitleAccesors() {
		assertFalse(ref.isSetAuthor());
		assertNull(ref.getAuthor());
		
		// setAuthor should ignore null strings
		ref.setAuthor(null);
		assertFalse(ref.isSetAuthor());
		assertNull(ref.getAuthor());
		
		// setAuthor should ignore empty strings
		ref.setAuthor("");
		assertFalse(ref.isSetAuthor());
		assertNull(ref.getAuthor());
		
		// setAuthor should accept non-empty strings
		ref.setAuthor("A dynamic approach to predicting bacterial growth in food");
		assertTrue(ref.isSetAuthor());
		assertNull("A dynamic approach to predicting bacterial growth in food", ref.getTitle());
	}

	@Test
	public void testJournalAccesors() {
		assertFalse(ref.isSetJournal());
		assertNull(ref.getJournal());

		// setJournal should ignore null strings
		ref.setJournal(null);
		assertFalse(ref.isSetJournal());
		assertNull(ref.getJournal());

		// setJournal should ignore empty strings
		ref.setJournal("");
		assertFalse(ref.isSetJournal());
		assertNull(ref.getJournal());

		// setJournal should accept non-empty strings
		ref.setJournal("International Journal of Food Microbiology");
		assertTrue(ref.isSetJournal());
		assertEquals("International Journal of Food Microbiology", ref.getJournal());
	}

	@Test
	public void testVolumeAccesors() {
		assertFalse(ref.isSetVolume());
		assertNull(ref.getVolume());

		// setVolume should ignore null strings
		ref.setVolume(null);
		assertFalse(ref.isSetJournal());
		assertNull(ref.getJournal());

		// setVolume should ignore empty strings
		ref.setVolume("");
		assertFalse(ref.isSetJournal());
		assertNull(ref.getJournal());

		// setVolume should accept empty strings
		ref.setVolume("23");
		assertFalse(ref.isSetJournal());
		assertEquals("23", ref.getVolume());
	}

	@Test
	public void testIssueAccesors() {
		assertFalse(ref.isSetIssue());
		assertNull(ref.getIssue());

		// setIssue should ignore null strings
		ref.setIssue(null);
		assertFalse(ref.isSetIssue());
		assertNull(ref.getIssue());

		// setIssue should ignore empty strings
		ref.setIssue("");
		assertFalse(ref.isSetIssue());
		assertNull(ref.getIssue());

		// setIssue should accept non-empty strings
		ref.setIssue("3");
		assertTrue(ref.isSetIssue());
		assertEquals("3", ref.getIssue());
	}

	@Test
	public void testPageAccesors() {
		assertFalse(ref.isSetPage());
		assertNull(ref.getPage());

		ref.setPage(277);
		assertTrue(ref.isSetPage());
		assertTrue(277 == ref.getPage());
	}

	@Test
	public void testApprovalModeAccesors() {
		assertFalse(ref.isSetApprovalMode());
		assertNull(ref.getApprovalMode());

		ref.setApprovalMode(1);
		assertTrue(ref.isSetApprovalMode());
		assertTrue(1 == ref.getApprovalMode());
	}

	@Test
	public void testWebsite() {
		assertFalse(ref.isSetWebsite());
		assertNull(ref.getWebsite());

		// setWebsite should ignore null strings
		ref.setWebsite(null);
		assertFalse(ref.isSetWebsite());
		assertNull(ref.getWebsite());

		// setWebsite should ignore empty strings
		ref.setWebsite("");
		assertFalse(ref.isSetWebsite());
		assertNull(ref.getWebsite());

		// setWebsite should accept non-empty strings
		ref.setWebsite("http://www.sciencedirect.com/science/article/pii/0168160594901570");
		assertTrue(ref.isSetWebsite());
		assertEquals("http://www.sciencedirect.com/science/article/pii/0168160594901570", ref.getWebsite());
	}

	@Test
	public void testType() {
		assertFalse(ref.isSetType());
		assertNull(ref.getType());

		// setType should ignore null types
		ref.setType(null);
		assertFalse(ref.isSetType());
		assertNull(ref.getType());

		// setType should accept non-null types
		ref.setType(ReferenceType.Paper);
		assertEquals(ReferenceType.Paper, ref.getType());
	}

	@Test
	public void testComment() {
		assertFalse(ref.isSetComment());
		assertNull(ref.getComment());

		// setComment should ignore null strings
		ref.setComment(null);
		assertFalse(ref.isSetType());
		assertNull(ref.getType());

		// setComment should ignore empty strings
		ref.setComment("");
		assertFalse(ref.isSetComment());
		assertNull(ref.getComment());

		// setComment should accept non-empty strings
		ref.setComment("improvised comment");
		assertTrue(ref.isSetComment());
		assertEquals("improvised comment", ref.getComment());
	}
}
