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
public class SBMLReferenceNode extends SBMLNodeBase {

	private LiteratureSpecification spec = new RIS();

	/**
	 * Builds a SBMLReferenceNode using the RIS tag set.
	 * 
	 * @param lit
	 *            : PmmLab LiteratureItem
	 */
	public SBMLReferenceNode(LiteratureItem lit) {
		// reference container
		XMLTriple refTriple = new XMLTriple("reference", null, "dc");
		XMLNamespaces refNS = new XMLNamespaces();
		refNS.add("http://foo.bar.com", "ref");
		node = new XMLNode(refTriple, null, refNS);

		// author node
		if (lit.getAuthor() != null) {
			XMLTriple authorTriple = new XMLTriple(spec.getAuthor(), null,
					"ref");
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
			XMLTriple abstractTriple = new XMLTriple(spec.getAbstract(), null,
					"ref");
			XMLNode abstractNode = new XMLNode(abstractTriple);
			abstractNode.addChild(new XMLNode(lit.getAbstractText()));
			node.addChild(abstractNode);
		}

		// journal node
		if (lit.getJournal() != null) {
			XMLTriple journalTriple = new XMLTriple(spec.getJournal(), null,
					"ref");
			XMLNode journalNode = new XMLNode(journalTriple);
			journalNode.addChild(new XMLNode(lit.getJournal()));
			node.addChild(journalNode);
		}

		// volume node
		if (lit.getVolume() != null) {
			XMLTriple volumeTriple = new XMLTriple(spec.getVolume(), null,
					"ref");
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
			XMLTriple approvalTriple = new XMLTriple(spec.getApproval(), null,
					"ref");
			XMLNode approvalNode = new XMLNode(approvalTriple);
			approvalNode
					.addChild(new XMLNode(lit.getApprovalMode().toString()));
			node.addChild(approvalNode);
		}

		// website node
		if (lit.getWebsite() != null) {
			XMLTriple websiteTriple = new XMLTriple(spec.getWebsite(), null,
					"ref");
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
			XMLTriple commentTriple = new XMLTriple(spec.getComment(), null,
					"ref");
			XMLNode commentNode = new XMLNode(commentTriple);
			commentNode.addChild(new XMLNode(lit.getComment()));
			node.addChild(commentNode);
		}
	}

	/**
	 * Builds a SBMLReferenceNode from an existing XMLNode
	 * 
	 * @param node
	 *            XMLNode
	 */
	public SBMLReferenceNode(XMLNode node) {
		this.node = node;
	}

	/**
	 * Create Pmm Lab LiteratureItem.
	 */
	public LiteratureItem toLiteratureItem() {

		String author;
		XMLNode authorNode = node.getChildElement(spec.getAuthor(), "");
		if (authorNode != null) {
			author = authorNode.getChildAt(0).getCharacters();
		} else {
			author = null;
		}

		String title;
		XMLNode titleNode = node.getChildElement(spec.getTitle(), "");
		if (titleNode != null) {
			title = titleNode.getChildAt(0).getCharacters();
		} else {
			title = null;
		}

		String abstractText;
		XMLNode abstractNode = node.getChildElement(spec.getAbstract(), "");
		if (abstractNode != null) {
			abstractText = abstractNode.getChildAt(0).getCharacters();
		} else {
			abstractText = null;
		}

		Integer year;
		XMLNode yearNode = node.getChildElement(spec.getYear(), "");
		if (yearNode != null) {
			year = Integer.parseInt(yearNode.getChild(0).getCharacters());
		} else {
			year = null;
		}

		String journal;
		XMLNode journalNode = node.getChildElement(spec.getJournal(), "");
		if (journalNode != null) {
			journal = journalNode.getChildAt(0).getCharacters();
		} else {
			journal = null;
		}

		String volume;
		XMLNode volumeNode = node.getChildElement(spec.getVolume(), "");
		if (volumeNode != null) {
			volume = volumeNode.getChildAt(0).getCharacters();
		} else {
			volume = null;
		}

		String issue;
		XMLNode issueNode = node.getChildElement(spec.getIssue(), "");
		if (issueNode != null) {
			issue = issueNode.getChildAt(0).getCharacters();
		} else {
			issue = null;
		}

		Integer page;
		XMLNode pageNode = node.getChildElement(spec.getPage(), "");
		if (pageNode != null) {
			page = Integer.parseInt(pageNode.getChildAt(0).getCharacters());
		} else {
			page = null;
		}

		Integer approvalMode;
		XMLNode approvalNode = node.getChildElement(spec.getApproval(), "");
		if (approvalNode != null) {
			approvalMode = Integer.parseInt(approvalNode.getChildAt(0)
					.getCharacters());
		} else {
			approvalMode = null;
		}

		String website = null;
		XMLNode websiteNode = node.getChildElement(spec.getWebsite(), "");
		if (websiteNode != null) {
			website = websiteNode.getChildAt(0).getCharacters();
		} else {
			website = null;
		}

		Integer type;
		XMLNode typeNode = node.getChildElement(spec.getType(), "");
		if (typeNode != null) {
			type = Integer.parseInt(typeNode.getChildAt(0).getCharacters());
		} else {
			type = null;
		}

		String comment;
		XMLNode commentNode = node.getChildElement(spec.getComment(), "");
		if (commentNode != null) {
			comment = commentNode.getChildAt(0).getCharacters();
		} else {
			comment = null;
		}

		LiteratureItem lit = new LiteratureItem(author, year, title,
				abstractText, journal, volume, issue, page, approvalMode,
				website, type, comment);
		return lit;
	}
}
