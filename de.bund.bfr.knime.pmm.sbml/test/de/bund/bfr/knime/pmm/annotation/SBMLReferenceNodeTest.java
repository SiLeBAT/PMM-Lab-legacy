package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

public class SBMLReferenceNodeTest {

	/**
	 * Tests equality for a non null LiteratureItem.
	 */
	@Test
	public void test() {
		LiteratureItem lit = new LiteratureItem("Baranyi, J.", 1994,
				"A dynamic approach to predicting bacterial growth in food",
				"A new member of the family of growth models described by Baranyi et al. (1993a)...",
				"International Journal of Food Microbiology", "23", "3", 277, 1,
				"http://www.sciencedirect.com/science/article/pii/0168160594901570", 1, "");
		
		ReferenceSBMLNode rn = new ReferenceSBMLNode(lit);
		ReferenceSBMLNode rn2 = new ReferenceSBMLNode(rn.getNode());
		LiteratureItem lit2 = rn2.toLiteratureItem();

		assertEquals(lit.getAuthor(), lit2.getAuthor());
		assertEquals(lit.getTitle(), lit2.getTitle());
		assertEquals(lit.getAbstractText(), lit2.getAbstractText());
		assertEquals(lit.getYear(), lit2.getYear());
		assertEquals(lit.getJournal(), lit2.getJournal());
		assertEquals(lit.getVolume(), lit2.getVolume());
		assertEquals(lit.getIssue(), lit2.getIssue());
		assertEquals(lit.getPage(), lit2.getPage());
		assertEquals(lit.getApprovalMode(), lit2.getApprovalMode());
		assertEquals(lit.getWebsite(), lit2.getWebsite());
		assertEquals(lit.getType(), lit2.getType());
		assertEquals(lit.getComment(), lit2.getComment());
		assertEquals(lit.getDbuuid(), lit2.getDbuuid());
	}
	
	/**
	 * Tests equality for a null LiteratureItem.
	 */
	@Test
	public void testNullLit() {
		LiteratureItem lit = new LiteratureItem(null, null, null, null, null, null, null, null, null, null, null, null);
		
		ReferenceSBMLNode rn = new ReferenceSBMLNode(lit);
		LiteratureItem lit2 = rn.toLiteratureItem();
		
		assertNull(lit2.getAuthor());
		assertNull(lit2.getTitle());
		assertNull(lit2.getAbstractText());
		assertNull(lit2.getYear());
		assertNull(lit2.getJournal());
		assertNull(lit2.getVolume());
		assertNull(lit2.getIssue());
		assertNull(lit2.getPage());
		assertNull(lit2.getApprovalMode());
		assertNull(lit2.getWebsite());
		assertNull(lit2.getType());
		assertNull(lit2.getComment());
		assertNull(lit2.getDbuuid());
	}
}
