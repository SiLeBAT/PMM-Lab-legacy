package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNamespaces;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

/**
 * XML node with literature data
 * 
 * @author Miguel Alba
 */
public class ReferenceSBMLNode extends SBMLNodeBase {
	
	static final String TAG = "reference";
	static final String NS = "dc";

	private LiteratureSpecification spec = new RIS();

	/**
	 * Builds a ReferenceSBMLNode using the RIS tag set.
	 * 
	 * @param lit
	 *            : PmmLab LiteratureItem
	 */
	public ReferenceSBMLNode(final LiteratureItem lit) {
		// reference container
		XMLTriple refTriple = new XMLTriple(TAG, null, NS);
		XMLNamespaces refNS = new XMLNamespaces();
		refNS.add("http://foo.bar.com", "ref");
		node = new XMLNode(refTriple, null, refNS);

		// author node
		if (lit.getAuthor() != null) {
			XMLTriple authorTriple = new XMLTriple(spec.getAuthor(), null, "ref");
			XMLNode authorNode = new XMLNode(authorTriple);
			authorNode.addChild(new XMLNode(lit.getAuthor()));
			node.addChild(authorNode);
		}

		// year node
		if (lit.getYear() != null) {
			XMLTriple yearTriple = new XMLTriple(spec.getYear(), null, "ref");
			XMLNode yearNode = new XMLNode(yearTriple);
			yearNode.addChild(new XMLNode(lit.getYear().toString()));
			node.addChild(yearNode);
		}

		// title node
		if (lit.getTitle() != null) {
			XMLTriple titleTriple = new XMLTriple(spec.getTitle(), null, "ref");
			XMLNode titleNode = new XMLNode(titleTriple);
			titleNode.addChild(new XMLNode(lit.getTitle()));
			node.addChild(titleNode);
		}

		// abstract node
		if (lit.getAbstractText() != null) {
			XMLTriple abstractTriple = new XMLTriple(spec.getAbstract(), null, "ref");
			XMLNode abstractNode = new XMLNode(abstractTriple);
			abstractNode.addChild(new XMLNode(lit.getAbstractText()));
			node.addChild(abstractNode);
		}

		// journal node
		if (lit.getJournal() != null) {
			XMLTriple journalTriple = new XMLTriple(spec.getJournal(), null, "ref");
			XMLNode journalNode = new XMLNode(journalTriple);
			journalNode.addChild(new XMLNode(lit.getJournal()));
			node.addChild(journalNode);
		}

		// volume node
		if (lit.getVolume() != null) {
			XMLTriple volumeTriple = new XMLTriple(spec.getVolume(), null, "ref");
			XMLNode volumeNode = new XMLNode(volumeTriple);
			volumeNode.addChild(new XMLNode(lit.getVolume()));
			node.addChild(volumeNode);
		}

		// issue node
		if (lit.getIssue() != null) {
			XMLTriple issueTriple = new XMLTriple(spec.getIssue(), null, "ref");
			XMLNode issueNode = new XMLNode(issueTriple);
			issueNode.addChild(new XMLNode(lit.getIssue()));
			node.addChild(issueNode);
		}

		// page node
		if (lit.getPage() != null) {
			XMLTriple pageTriple = new XMLTriple(spec.getPage(), null, "ref");
			XMLNode pageNode = new XMLNode(pageTriple);
			pageNode.addChild(new XMLNode(lit.getPage().toString()));
			node.addChild(pageNode);
		}

		// approval mode node
		if (lit.getApprovalMode() != null) {
			XMLTriple approvalTriple = new XMLTriple(spec.getApproval(), null, "ref");
			XMLNode approvalNode = new XMLNode(approvalTriple);
			approvalNode.addChild(new XMLNode(lit.getApprovalMode().toString()));
			node.addChild(approvalNode);
		}

		// website node
		if (lit.getWebsite() != null) {
			XMLTriple websiteTriple = new XMLTriple(spec.getWebsite(), null, "ref");
			XMLNode websiteNode = new XMLNode(websiteTriple);
			websiteNode.addChild(new XMLNode(lit.getWebsite()));
			node.addChild(websiteNode);
		}

		// type node
		if (lit.getType() != null) {
			XMLTriple typeTriple = new XMLTriple(spec.getType(), null, "ref");
			XMLNode typeNode = new XMLNode(typeTriple);
			typeNode.addChild(new XMLNode(lit.getType().toString()));
			node.addChild(typeNode);
		}

		// comment node
		if (lit.getComment() != null) {
			XMLTriple commentTriple = new XMLTriple(spec.getComment(), null, "ref");
			XMLNode commentNode = new XMLNode(commentTriple);
			commentNode.addChild(new XMLNode(lit.getComment()));
			node.addChild(commentNode);
		}
	}

	/**
	 * Builds a ReferenceSBMLNode from an existing XMLNode
	 * 
	 * @param node
	 *            XMLNode
	 */
	public ReferenceSBMLNode(final XMLNode node) {
		this.node = node;
	}

	/**
	 * Create Pmm Lab LiteratureItem.
	 */
	public LiteratureItem toLiteratureItem() {

		XMLNode authorNode = node.getChildElement(spec.getAuthor(), "");
		String author = (authorNode == null) ? null : authorNode.getChild(0).getCharacters();

		XMLNode titleNode = node.getChildElement(spec.getTitle(), "");
		String title = (titleNode == null) ? null : titleNode.getChild(0).getCharacters();

		XMLNode abstractNode = node.getChildElement(spec.getAbstract(), "");
		String abstractText = (abstractNode == null) ? null : abstractNode.getChild(0).getCharacters();

		XMLNode yearNode = node.getChildElement(spec.getYear(), "");
		Integer year = (yearNode == null) ? null : Integer.parseInt(yearNode.getChild(0).getCharacters());

		XMLNode journalNode = node.getChildElement(spec.getJournal(), "");
		String journal = (journalNode == null) ? null : journalNode.getChild(0).getCharacters();

		XMLNode volumeNode = node.getChildElement(spec.getVolume(), "");
		String volume = (volumeNode == null) ? null : volumeNode.getChild(0).getCharacters();

		XMLNode issueNode = node.getChildElement(spec.getIssue(), "");
		String issue = (issueNode == null) ? null : issueNode.getChild(0).getCharacters();

		XMLNode pageNode = node.getChildElement(spec.getPage(), "");
		Integer page = (pageNode == null) ? null : Integer.parseInt(pageNode.getChild(0).getCharacters());

		XMLNode approvalNode = node.getChildElement(spec.getApproval(), "");
		Integer approvalMode = (approvalNode == null) ? null
				: Integer.parseInt(approvalNode.getChild(0).getCharacters());

		XMLNode websiteNode = node.getChildElement(spec.getWebsite(), "");
		String website = (websiteNode == null) ? null : websiteNode.getChild(0).getCharacters();

		XMLNode typeNode = node.getChildElement(spec.getType(), "");
		Integer type = (typeNode == null) ? null : Integer.parseInt(typeNode.getChild(0).getCharacters());

		XMLNode commentNode = node.getChildElement(spec.getComment(), "");
		String comment = (commentNode == null) ? null : commentNode.getChild(0).getCharacters();

		LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page,
				approvalMode, website, type, comment);
		return lit;
	}
}
