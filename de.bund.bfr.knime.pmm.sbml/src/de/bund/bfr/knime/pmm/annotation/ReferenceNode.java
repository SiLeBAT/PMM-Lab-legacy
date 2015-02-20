/**
 * Node with literature data.
 */
package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLNamespaces;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

public class ReferenceNode {
	private XMLNode node;

	public ReferenceNode(LiteratureItem lit) {
		// reference container
		XMLTriple refTriple = new XMLTriple("reference", null, "dc");
		XMLNamespaces refNS = new XMLNamespaces();
		refNS.add("http://foo.bar.com", "ref");
		node = new XMLNode(refTriple, null, refNS);

		// author node
		if (lit.getAuthor() != null) {
			XMLNode author = new XMLNode(new XMLTriple("author", null, "ref"));
			author.addChild(new XMLNode(lit.getAuthor()));
			node.addChild(author);
		}

		// year node
		if (lit.getYear() != null) {
			XMLNode year = new XMLNode(new XMLTriple("year", null, "ref"));
			year.addChild(new XMLNode(lit.getYear().toString()));
			node.addChild(year);
		}

		// title node
		if (lit.getTitle() != null) {
			XMLNode title = new XMLNode(new XMLTriple("title", null, "ref"));
			title.addChild(new XMLNode(lit.getTitle()));
			node.addChild(title);
		}

		// abstract node
		if (lit.getAbstractText() != null) {
			XMLNode abstractText = new XMLNode(new XMLTriple("abstract", null,
					"ref"));
			abstractText.addChild(new XMLNode(lit.getAbstractText()));
			node.addChild(abstractText);
		}

		// journal node
		if (lit.getJournal() != null) {
			XMLNode journal = new XMLNode(new XMLTriple("journal", null, "ref"));
			journal.addChild(new XMLNode(lit.getJournal()));
			node.addChild(journal);
		}

		// volume node
		if (lit.getVolume() != null) {
			XMLNode volume = new XMLNode(new XMLTriple("volume", null, "ref"));
			volume.addChild(new XMLNode(lit.getVolume()));
			node.addChild(volume);
		}

		// issue node
		if (lit.getIssue() != null) {
			XMLNode issue = new XMLNode(new XMLTriple("issue", null, "ref"));
			issue.addChild(new XMLNode(lit.getIssue()));
			node.addChild(issue);
		}

		// page node
		if (lit.getPage() != null) {
			XMLNode page = new XMLNode(new XMLTriple("page", null, "ref"));
			page.addChild(new XMLNode(lit.getPage().toString()));
			node.addChild(page);
		}

		// approval mode node
		if (lit.getApprovalMode() != null) {
			XMLNode approval = new XMLNode(new XMLTriple("approvalMode", null,
					"ref"));
			approval.addChild(new XMLNode(lit.getApprovalMode().toString()));
			node.addChild(approval);
		}

		// website node
		if (lit.getWebsite() != null) {
			XMLNode website = new XMLNode(new XMLTriple("website", null, "ref"));
			website.addChild(new XMLNode(lit.getWebsite()));
			node.addChild(website);
		}

		// type node
		if (lit.getType() != null) {
			XMLNode type = new XMLNode(new XMLTriple("type", null, "ref"));
			type.addChild(new XMLNode(lit.getType().toString()));
			node.addChild(type);
		}

		// comment node
		if (lit.getComment() != null) {
			XMLNode comment = new XMLNode(new XMLTriple("comment", null, "ref"));
			comment.addChild(new XMLNode(lit.getComment()));
			node.addChild(comment);
		}
	}

	public ReferenceNode(XMLNode node) {
		this.node = node;
	}

	public LiteratureItem toLiteratureItem() {
		String author = null;
		String title = null;
		String abstractText = null;
		Integer year = null;
		String journal = null;
		String volume = null;
		String issue = null;
		Integer page = null;
		Integer approvalMode = null;
		String website = null;
		Integer type = null;
		String comment = null;

		XMLNode authorNode = node.getChildElement("author", "");
		if (authorNode != null) {
			author = authorNode.getChildAt(0).getCharacters();
		}

		XMLNode titleNode = node.getChildElement("title", "");
		if (titleNode != null) {
			title = titleNode.getChildAt(0).getCharacters();
		}

		XMLNode abstractNode = node.getChildElement("abstractText", "");
		if (abstractNode != null) {
			abstractText = abstractNode.getChildAt(0).getCharacters();
		}

		XMLNode journalNode = node.getChildElement("journal", "");
		if (journalNode != null) {
			journal = journalNode.getChildAt(0).getCharacters();
		}

		XMLNode volumeNode = node.getChildElement("volume", "");
		if (volumeNode != null) {
			volume = volumeNode.getChildAt(0).getCharacters();
		}

		XMLNode issueNode = node.getChildElement("issue", "");
		if (issueNode != null) {
			issue = issueNode.getChildAt(0).getCharacters();
		}

		XMLNode pageNode = node.getChildElement("page", "");
		if (pageNode != null)
			page = Integer.parseInt(pageNode.getChildAt(0).getCharacters());

		XMLNode approvalNode = node.getChildElement("approvalMode", "");
		if (approvalNode != null) {
			approvalMode = Integer.parseInt(approvalNode.getChildAt(0)
					.getCharacters());
		}

		XMLNode websiteNode = node.getChildElement("website", "");
		if (websiteNode != null) {
			website = websiteNode.getChildAt(0).getCharacters();
		}

		XMLNode typeNode = node.getChildElement("type", "");
		if (typeNode != null) {
			type = Integer.parseInt(typeNode.getChildAt(0).getCharacters());
		}

		XMLNode commentNode = node.getChildElement("comment", "");
		if (commentNode != null) {
			comment = commentNode.getChildAt(0).getCharacters();
		}

		LiteratureItem lit = new LiteratureItem(author, year, title,
				abstractText, journal, volume, issue, page, approvalMode,
				website, type, comment);
		return lit;
	}

	public XMLNode getNode() {
		return node;
	}
}
