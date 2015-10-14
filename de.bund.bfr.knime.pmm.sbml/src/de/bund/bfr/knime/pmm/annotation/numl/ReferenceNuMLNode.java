package de.bund.bfr.knime.pmm.annotation.numl;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.bund.bfr.knime.pmm.annotation.LiteratureSpecification;
import de.bund.bfr.knime.pmm.annotation.RIS;
import de.bund.bfr.knime.pmm.common.LiteratureItem;

public class ReferenceNuMLNode extends NuMLNodeBase {

	public static final String TAG = "dc:reference";

	private static final LiteratureSpecification SPEC = new RIS();

	/**
	 * Builds a {@link ReferenceNuMLNode} using the RIS tag set.
	 * 
	 * @param lit:
	 *            PmmLab LiteratureItem.
	 * @throws ParserConfigurationException
	 */
	public ReferenceNuMLNode(final LiteratureItem lit) throws ParserConfigurationException {
		// reference container
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		node = doc.createElement(TAG);
		node.setAttribute("xmlns:ref", "http://foo.bar.com");

		if (lit.getAuthor() != null) {
			Element authorNode = doc.createElement(SPEC.getAuthor());
			authorNode.setTextContent(lit.getAuthor());
			node.appendChild(authorNode);
		}

		if (lit.getYear() != null) {
			Element yearNode = doc.createElement(SPEC.getYear());
			yearNode.setTextContent(lit.getYear().toString());
			node.appendChild(yearNode);
		}

		if (lit.getTitle() != null) {
			Element titleNode = doc.createElement(SPEC.getTitle());
			titleNode.setTextContent(lit.getTitle());
			node.appendChild(titleNode);
		}

		if (lit.getAbstractText() != null) {
			Element abstractNode = doc.createElement(SPEC.getAbstract());
			abstractNode.setTextContent(lit.getAbstractText());
			node.appendChild(abstractNode);
		}

		if (lit.getJournal() != null) {
			Element journalNode = doc.createElement(SPEC.getJournal());
			journalNode.setTextContent(lit.getJournal());
			node.appendChild(journalNode);
		}

		if (lit.getVolume() != null) {
			Element volumeNode = doc.createElement(SPEC.getVolume());
			volumeNode.setTextContent(lit.getVolume());
			node.appendChild(volumeNode);
		}

		if (lit.getIssue() != null) {
			Element issueNode = doc.createElement(SPEC.getIssue());
			issueNode.setTextContent(lit.getIssue());
			node.appendChild(issueNode);
		}

		if (lit.getPage() != null) {
			Element pageNode = doc.createElement(SPEC.getPage());
			pageNode.setTextContent(lit.getPage().toString());
			node.appendChild(pageNode);
		}

		if (lit.getApprovalMode() != null) {
			Element approvalNode = doc.createElement(SPEC.getApproval());
			approvalNode.setTextContent(lit.getApprovalMode().toString());
			node.appendChild(approvalNode);
		}

		if (lit.getWebsite() != null) {
			Element websiteNode = doc.createElement(SPEC.getWebsite());
			websiteNode.setTextContent(lit.getWebsite());
			node.appendChild(websiteNode);
		}

		if (lit.getType() != null) {
			Element typeNode = doc.createElement(SPEC.getType());
			typeNode.setTextContent(lit.getType().toString());
			node.appendChild(typeNode);
		}

		if (lit.getComment() != null) {
			Element commentNode = doc.createElement(SPEC.getComment());
			commentNode.setTextContent(lit.getComment());
			node.appendChild(commentNode);
		}
	}

	/**
	 * Builds a {@link ReferenceNuMLNode} from an existing {@link Element}.
	 */
	public ReferenceNuMLNode(final Element element) {
		node = element;
	}

	/**
	 * Creates a PmmLab {@link LiteratureItem}.
	 */
	public LiteratureItem toLiteratureItem() {

		String author;
		NodeList authorNodes = node.getElementsByTagName(SPEC.getAuthor());
		if (authorNodes.getLength() == 1) {
			Element authorNode = (Element) authorNodes.item(0);
			author = authorNode.getTextContent();
		} else {
			author = null;
		}

		String title;
		NodeList titleNodes = node.getElementsByTagName(SPEC.getTitle());
		if (titleNodes.getLength() == 1) {
			Element titleNode = (Element) titleNodes.item(0);
			title = titleNode.getTextContent();
		} else {
			title = null;
		}

		String abstractText;
		NodeList abstractNodes = node.getElementsByTagName(SPEC.getAbstract());
		if (abstractNodes.getLength() == 1) {
			Element abstractNode = (Element) abstractNodes.item(0);
			abstractText = abstractNode.getTextContent();
		} else {
			abstractText = null;
		}

		Integer year;
		NodeList yearNodes = node.getElementsByTagName(SPEC.getYear());
		if (yearNodes.getLength() == 1) {
			Element yearNode = (Element) yearNodes.item(0);
			year = Integer.parseInt(yearNode.getTextContent());
		} else {
			year = null;
		}

		String journal;
		NodeList journalNodes = node.getElementsByTagName(SPEC.getJournal());
		if (journalNodes.getLength() == 1) {
			Element journalNode = (Element) journalNodes.item(0);
			journal = journalNode.getTextContent();
		} else {
			journal = null;
		}

		String volume;
		NodeList volumeNodes = node.getElementsByTagName(SPEC.getVolume());
		if (volumeNodes.getLength() == 1) {
			Element volumeNode = (Element) volumeNodes.item(0);
			volume = volumeNode.getTextContent();
		} else {
			volume = null;
		}

		String issue;
		NodeList issueNodes = node.getElementsByTagName(SPEC.getIssue());
		if (issueNodes.getLength() == 1) {
			Element issueNode = (Element) issueNodes.item(0);
			issue = issueNode.getTextContent();
		} else {
			issue = null;
		}

		Integer page;
		NodeList pageNodes = node.getElementsByTagName(SPEC.getPage());
		if (pageNodes.getLength() == 1) {
			Element pageNode = (Element) pageNodes.item(0);
			page = Integer.parseInt(pageNode.getTextContent());
		} else {
			page = null;
		}

		Integer approvalMode;
		NodeList approvalNodes = node.getElementsByTagName(SPEC.getApproval());
		if (approvalNodes.getLength() == 1) {
			Element approvalNode = (Element) approvalNodes.item(0);
			approvalMode = Integer.parseInt(approvalNode.getTextContent());
		} else {
			approvalMode = null;
		}

		String website;
		NodeList websiteNodes = node.getElementsByTagName(SPEC.getWebsite());
		if (websiteNodes.getLength() == 1) {
			Element websiteNode = (Element) websiteNodes.item(0);
			website = websiteNode.getTextContent();
		} else {
			website = null;
		}

		Integer type;
		NodeList typeNodes = node.getElementsByTagName(SPEC.getType());
		if (typeNodes.getLength() == 1) {
			Element typeNode = (Element) typeNodes.item(0);
			type = Integer.parseInt(typeNode.getTextContent());
		} else {
			type = null;
		}

		String comment;
		NodeList commentNodes = node.getElementsByTagName(SPEC.getComment());
		if (commentNodes.getLength() == 1) {
			Element commentNode = (Element) commentNodes.item(0);
			comment = commentNode.getTextContent();
		} else {
			comment = null;
		}

		LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal, volume, issue, page,
				approvalMode, website, type, comment);
		return lit;
	}
}
