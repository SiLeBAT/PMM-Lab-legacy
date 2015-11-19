package de.bund.bfr.knime.pmm.annotation.numl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

public class ReferenceNuMLNodeTest {
	/**
	 * Tests equality for a non null LiteratureItem.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void test() {
		LiteratureItem lit = new LiteratureItem("Baranyi, J.", 1994,
				"A dynamic approach to predicting bacterial growth in food",
				"A new member of the family of growth models described by Baranyi et al. (1993a)...",
				"International Journal of Food Microbiology", "23", "3", 277, 1,
				"http://www.sciencedirect.com/science/article/pii/0168160594901570", 1, "");
		
		ReferenceNuMLNode rn1 = null;
		try {
			rn1 = new ReferenceNuMLNode(lit);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			fail();
		}
		ReferenceNuMLNode rn2 = new ReferenceNuMLNode(rn1.getNode());
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
}
