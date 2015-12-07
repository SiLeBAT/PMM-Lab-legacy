package de.bund.bfr.pmf.sbml;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ReferenceSBMLNodeTest {

	private Reference exampleRef;

	@Before
	public void setUp() {
		final String author = "Baranyi, J.";
		final int year = 1994;
		final String title = "A dynamic approach to predicting bacterial growth in food";
		final String abstractText = "A new member ...";
		final String journal = "International Journal of Food Microbiology";
		final String volume = "23";
		final String issue = "3";
		final int page = 277;
		final int approvalMode = 1;
		final String website = "http://www.sciencedirect.com/science/article/pii/0168160594901570";
		final ReferenceType type = ReferenceType.Paper;
		final String comment = "improvised comment";

		exampleRef = SBMLFactory.createReference(author, year, title, abstractText, journal, volume, issue, page,
				approvalMode, website, type, comment);
	}

	@Test
	public void testAuthor() {
		Reference ref = SBMLFactory.createReference(exampleRef.getAuthor(), null, null, null, null, null, null, null,
				null, null, null, null);

		ReferenceSBMLNode refNode = new ReferenceSBMLNode(ref);
		refNode = new ReferenceSBMLNode(refNode.getNode());
		assertEquals(exampleRef.getAuthor(), refNode.toReference().getAuthor());
	}

	@Test
	public void testYear() {
		Reference ref = SBMLFactory.createReference(null, exampleRef.getYear(), null, null, null, null, null, null,
				null, null, null, null);

		ReferenceSBMLNode refNode = new ReferenceSBMLNode(ref);
		refNode = new ReferenceSBMLNode(refNode.getNode());
		assertEquals(exampleRef.getYear(), refNode.toReference().getYear());
	}

	@Test
	public void testTitle() {
		Reference ref = SBMLFactory.createReference(null, null, exampleRef.getTitle(), null, null, null, null, null,
				null, null, null, null);

		ReferenceSBMLNode refNode = new ReferenceSBMLNode(ref);
		refNode = new ReferenceSBMLNode(refNode.getNode());
		assertEquals(exampleRef.getTitle(), refNode.toReference().getTitle());
	}

	@Test
	public void testAbstractText() {
		Reference ref = SBMLFactory.createReference(null, null, null, exampleRef.getAbstractText(), null, null, null,
				null, null, null, null, null);

		ReferenceSBMLNode refNode = new ReferenceSBMLNode(ref);
		refNode = new ReferenceSBMLNode(refNode.getNode());
		assertEquals(exampleRef.getAbstractText(), refNode.toReference().getAbstractText());
	}

	@Test
	public void testJournal() {
		Reference ref = SBMLFactory.createReference(null, null, null, null, exampleRef.getJournal(), null, null, null,
				null, null, null, null);

		ReferenceSBMLNode refNode = new ReferenceSBMLNode(ref);
		refNode = new ReferenceSBMLNode(refNode.getNode());
		assertEquals(exampleRef.getJournal(), refNode.toReference().getJournal());
	}

	@Test
	public void testVolume() {
		Reference ref = SBMLFactory.createReference(null, null, null, null, null, exampleRef.getVolume(), null, null,
				null, null, null, null);

		ReferenceSBMLNode refNode = new ReferenceSBMLNode(ref);
		refNode = new ReferenceSBMLNode(refNode.getNode());
		assertEquals(exampleRef.getVolume(), refNode.toReference().getVolume());
	}

	@Test
	public void testIssue() {
		Reference ref = SBMLFactory.createReference(null, null, null, null, null, null, exampleRef.getIssue(), null,
				null, null, null, null);

		ReferenceSBMLNode refNode = new ReferenceSBMLNode(ref);
		refNode = new ReferenceSBMLNode(refNode.getNode());
		assertEquals(exampleRef.getIssue(), refNode.toReference().getIssue());
	}

	@Test
	public void testPage() {
		Reference ref = SBMLFactory.createReference(null, null, null, null, null, null, null, exampleRef.getPage(),
				null, null, null, null);

		ReferenceSBMLNode refNode = new ReferenceSBMLNode(ref);
		refNode = new ReferenceSBMLNode(refNode.getNode());
		assertEquals(exampleRef.getPage(), refNode.toReference().getPage());
	}

	@Test
	public void testApprovalMode() {
		Reference ref = SBMLFactory.createReference(null, null, null, null, null, null, null, null,
				exampleRef.getApprovalMode(), null, null, null);

		ReferenceSBMLNode refNode = new ReferenceSBMLNode(ref);
		refNode = new ReferenceSBMLNode(refNode.getNode());
		assertEquals(exampleRef.getApprovalMode(), refNode.toReference().getApprovalMode());
	}

	@Test
	public void testWebsite() {
		Reference ref = SBMLFactory.createReference(null, null, null, null, null, null, null, null, null,
				exampleRef.getWebsite(), null, null);

		ReferenceSBMLNode refNode = new ReferenceSBMLNode(ref);
		refNode = new ReferenceSBMLNode(refNode.getNode());
		assertEquals(exampleRef.getWebsite(), refNode.toReference().getWebsite());
	}

	@Test
	public void testType() {
		Reference ref = SBMLFactory.createReference(null, null, null, null, null, null, null, null, null, null,
				exampleRef.getType(), null);

		ReferenceSBMLNode refNode = new ReferenceSBMLNode(ref);
		refNode = new ReferenceSBMLNode(refNode.getNode());
		assertEquals(exampleRef.getType(), refNode.toReference().getType());
	}

	@Test
	public void testComment() {
		Reference ref = SBMLFactory.createReference(null, null, null, null, null, null, null, null, null, null, null,
				exampleRef.getComment());

		ReferenceSBMLNode refNode = new ReferenceSBMLNode(ref);
		refNode = new ReferenceSBMLNode(refNode.getNode());
		assertEquals(exampleRef.getComment(), refNode.toReference().getComment());
	}
}
