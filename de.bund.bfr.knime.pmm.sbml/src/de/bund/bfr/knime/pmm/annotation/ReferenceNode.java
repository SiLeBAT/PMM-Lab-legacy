/**
 * Node with literature data.
 */
package de.bund.bfr.knime.pmm.annotation;

import java.util.HashMap;
import java.util.Map;

import org.sbml.jsbml.xml.XMLNamespaces;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.LiteratureItem;

/**
 * XML node with literature data.
 * @author Miguel Alba
 */
public class ReferenceNode {
	private XMLNode node;

	// RIS tag set
	private static final Map<String, String> TAGSET = new HashMap<>();

	static {
		TAGSET.put("author", "AU");
		TAGSET.put("year", "PY");
		TAGSET.put("title", "TI");
		TAGSET.put("abstract", "AB");
		TAGSET.put("journal", "T2");
		TAGSET.put("volume", "VL");
		TAGSET.put("issue", "IS");
		TAGSET.put("page", "SP");
		TAGSET.put("approval", "LB");
		TAGSET.put("website", "UR");
		TAGSET.put("type", "M3");
		TAGSET.put("comment", "N1");
	}

	public ReferenceNode(LiteratureItem lit) {
		// reference container
		XMLTriple refTriple = new XMLTriple("reference", null, "dc");
		XMLNamespaces refNS = new XMLNamespaces();
		refNS.add("http://foo.bar.com", "ref");
		node = new XMLNode(refTriple, null, refNS);

		// author node
		if (lit.getAuthor() != null) {
			XMLNode author = new XMLNode(new XMLTriple(TAGSET.get("author"),
					null, "ref"));
			author.addChild(new XMLNode(lit.getAuthor()));
			node.addChild(author);
		}

		// year node
		if (lit.getYear() != null) {
			XMLNode year = new XMLNode(new XMLTriple(TAGSET.get("year"), null,
					"ref"));
			year.addChild(new XMLNode(lit.getYear().toString()));
			node.addChild(year);
		}

		// title node
		if (lit.getTitle() != null) {
			XMLNode title = new XMLNode(new XMLTriple(TAGSET.get("title"),
					null, "ref"));
			title.addChild(new XMLNode(lit.getTitle()));
			node.addChild(title);
		}

		// abstract node
		if (lit.getAbstractText() != null) {
			XMLNode abstractText = new XMLNode(new XMLTriple(
					TAGSET.get("abstract"), null, "ref"));
			abstractText.addChild(new XMLNode(lit.getAbstractText()));
			node.addChild(abstractText);
		}

		// journal node
		if (lit.getJournal() != null) {
			XMLNode journal = new XMLNode(new XMLTriple(TAGSET.get("journal"),
					null, "ref"));
			journal.addChild(new XMLNode(lit.getJournal()));
			node.addChild(journal);
		}

		// volume node
		if (lit.getVolume() != null) {
			XMLNode volume = new XMLNode(new XMLTriple(TAGSET.get("volume"),
					null, "ref"));
			volume.addChild(new XMLNode(lit.getVolume()));
			node.addChild(volume);
		}

		// issue node
		if (lit.getIssue() != null) {
			XMLNode issue = new XMLNode(new XMLTriple(TAGSET.get("issue"),
					null, "ref"));
			issue.addChild(new XMLNode(lit.getIssue()));
			node.addChild(issue);
		}

		// page node
		if (lit.getPage() != null) {
			XMLNode page = new XMLNode(new XMLTriple(TAGSET.get("page"), null,
					"ref"));
			page.addChild(new XMLNode(lit.getPage().toString()));
			node.addChild(page);
		}

		// approval mode node
		if (lit.getApprovalMode() != null) {
			XMLNode approval = new XMLNode(new XMLTriple(
					TAGSET.get("approval"), null, "ref"));
			approval.addChild(new XMLNode(lit.getApprovalMode().toString()));
			node.addChild(approval);
		}

		// website node
		if (lit.getWebsite() != null) {
			XMLNode website = new XMLNode(new XMLTriple(TAGSET.get("website"),
					null, "ref"));
			website.addChild(new XMLNode(lit.getWebsite()));
			node.addChild(website);
		}

		// type node
		if (lit.getType() != null) {
			XMLNode type = new XMLNode(new XMLTriple(TAGSET.get("type"), null,
					"ref"));
			type.addChild(new XMLNode(lit.getType().toString()));
			node.addChild(type);
		}

		// comment node
		if (lit.getComment() != null) {
			XMLNode comment = new XMLNode(new XMLTriple(TAGSET.get("comment"),
					null, "ref"));
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

		XMLNode authorNode = node.getChildElement(TAGSET.get("author"), "");
		if (authorNode != null) {
			author = authorNode.getChildAt(0).getCharacters();
		}

		XMLNode titleNode = node.getChildElement(TAGSET.get("title"), "");
		if (titleNode != null) {
			title = titleNode.getChildAt(0).getCharacters();
		}

		XMLNode abstractNode = node.getChildElement(TAGSET.get("abstract"),
				"");
		if (abstractNode != null) {
			abstractText = abstractNode.getChildAt(0).getCharacters();
		}

		XMLNode journalNode = node.getChildElement(TAGSET.get("journal"), "");
		if (journalNode != null) {
			journal = journalNode.getChildAt(0).getCharacters();
		}

		XMLNode volumeNode = node.getChildElement(TAGSET.get("volume"), "");
		if (volumeNode != null) {
			volume = volumeNode.getChildAt(0).getCharacters();
		}

		XMLNode issueNode = node.getChildElement(TAGSET.get("issue"), "");
		if (issueNode != null) {
			issue = issueNode.getChildAt(0).getCharacters();
		}

		XMLNode pageNode = node.getChildElement(TAGSET.get("page"), "");
		if (pageNode != null)
			page = Integer.parseInt(pageNode.getChildAt(0).getCharacters());

		XMLNode approvalNode = node.getChildElement(TAGSET.get("approval"),
				"");
		if (approvalNode != null) {
			approvalMode = Integer.parseInt(approvalNode.getChildAt(0)
					.getCharacters());
		}

		XMLNode websiteNode = node.getChildElement(TAGSET.get("website"), "");
		if (websiteNode != null) {
			website = websiteNode.getChildAt(0).getCharacters();
		}

		XMLNode typeNode = node.getChildElement(TAGSET.get("type"), "");
		if (typeNode != null) {
			type = Integer.parseInt(typeNode.getChildAt(0).getCharacters());
		}

		XMLNode commentNode = node.getChildElement(TAGSET.get("comment"), "");
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
