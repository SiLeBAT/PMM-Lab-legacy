package de.bund.bfr.knime.pmm.annotation;

import java.util.HashMap;
import java.util.Map;

import groovy.util.Node;
import groovy.util.NodeList;
import de.bund.bfr.knime.pmm.common.LiteratureItem;

/**
 * Groovy XML node with literature data
 * 
 * @author Miguel Alba
 */
public class GroovyReferenceNode {

	private Node node;
	
	private static LiteratureSpecification spec = new RIS();
	private static final String AUTHOR_TAG = "ref:" + spec.getAuthor();
	private static final String YEAR_TAG = "ref:" + spec.getYear();
	private static final String TITLE_TAG = "ref:" + spec.getTitle();
	private static final String ABSTRACT_TAG = "ref:" + spec.getAbstract();
	private static final String JOURNAL_TAG = "ref:" + spec.getJournal();
	private static final String VOLUME_TAG = "ref:" + spec.getVolume();
	private static final String ISSUE_TAG = "ref:" + spec.getIssue();
	private static final String PAGE_TAG = "ref:" + spec.getPage();
	private static final String APPROVAL_TAG = "ref:" + spec.getApproval();
	private static final String WEBSITE_TAG = "ref:" + spec.getWebsite();
	private static final String TYPE_TAG = "ref:" + spec.getType();
	private static final String COMMENT_TAG = "ref:" + spec.getComment();

	/**
	 * Builds a GroovyReferenceNode using the RIS tag set.
	 * 
	 * @param lit
	 *            : PmmLab LiteratureItem
	 */
	public GroovyReferenceNode(final LiteratureItem lit) {
		// reference container
		Map<String, String> refNS = new HashMap<>();
		refNS.put("xmlns:ref", "http://foo.bar.com");
		node = new Node(null, "dc:reference", refNS);
		
		// author node
		if (lit.getAuthor() != null) {
			node.appendNode(AUTHOR_TAG, lit.getAuthor());
		}

		// year node
		if (lit.getYear() != null) {
			node.appendNode(YEAR_TAG, lit.getYear());
		}
		
		// title node
		if (lit.getTitle() != null) {
			node.appendNode(TITLE_TAG, lit.getTitle());
		}
		
		// abstract node
		if (lit.getAbstractText() != null) {
			node.appendNode(ABSTRACT_TAG, lit.getAbstractText());
		}
		
		// journal node
		if (lit.getJournal() != null) {
			node.appendNode(JOURNAL_TAG, lit.getJournal());
		}
		
		// volume node
		if (lit.getVolume() != null) {
			node.appendNode(VOLUME_TAG, lit.getVolume());
		}
		
		// issue node
		if (lit.getIssue() != null) {
			node.appendNode(ISSUE_TAG, lit.getIssue());
		}
		
		// page node
		if (lit.getPage() != null) {
			node.appendNode(PAGE_TAG, lit.getPage());
		}
		
		// approval mode node
		if (lit.getApprovalMode() != null) {
			node.appendNode(APPROVAL_TAG, lit.getApprovalMode());
		}
		
		// website node
		if (lit.getWebsite() != null) {
			node.appendNode(WEBSITE_TAG, lit.getWebsite());
		}
		
		// type node
		if (lit.getType() != null) {
			node.appendNode(TYPE_TAG, lit.getType());
		}
		
		// comment node
		if (lit.getComment() != null) {
			node.appendNode(COMMENT_TAG, lit.getComment());
		}
	}
	
	/**
	 * Builds a GroovyReferenceNode from an existing XMLNode
	 */
	public GroovyReferenceNode(final Node node) {
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}
	
	/**
	 * Creates PmmLab LiteratureItem.
	 */
	public LiteratureItem toLiteratureItem() {
		
		String author;
		NodeList authorNodes = (NodeList) node.get(spec.getAuthor());
		if (authorNodes.size() == 0) {
			author = null;
		} else {
			author = (String) ((Node) authorNodes.get(0)).text();
		}
		
		String title;
		NodeList titleNodes = (NodeList) node.get(spec.getTitle());
		if (titleNodes.size() == 0) {
			title = null;
		} else {
			title = (String) ((Node) titleNodes.get(0)).text();
		}
		
		String abstractText;
		NodeList abstractNodes = (NodeList) node.get(spec.getAbstract());
		if (abstractNodes.size() == 0) {
			abstractText = null;
		} else {
			abstractText = (String) ((Node) abstractNodes.get(0)).text();
		}
		
		Integer year;
		NodeList yearNodes = (NodeList) node.get(spec.getYear());
		if (yearNodes.size() == 0) {
			year = null;
		} else {
			year = Integer.parseInt((String) ((Node) yearNodes.get(0)).text());
		}
		
		String journal;
		NodeList journalNodes = (NodeList) node.get(spec.getJournal());
		if (journalNodes.size() == 0) {
			journal = null;
		} else {
			journal = (String) ((Node) journalNodes.get(0)).text();
		}
		
		String volume;
		NodeList volumeNodes = (NodeList) node.get(spec.getVolume());
		if (volumeNodes.size() == 0) {
			volume = null;
		} else {
			volume = (String) ((Node) volumeNodes.get(0)).text();
		}
		
		String issue;
		NodeList issueNodes = (NodeList) node.get(spec.getIssue());
		if (issueNodes.size() == 0) {
			issue = null;
		} else {
			issue = (String) ((Node) issueNodes.get(0)).text();
		}
		
		Integer page;
		NodeList pageNodes = (NodeList) node.get(spec.getPage());
		if (pageNodes.size() == 0) {
			page = null;
		} else {
			page = Integer.parseInt((String) ((Node) pageNodes.get(0)).text());
		}
		
		Integer approvalMode;
		NodeList approvalNodes = (NodeList) node.get(spec.getApproval());
		if (approvalNodes.size() == 0) {
			approvalMode = null;
		} else {
			approvalMode = Integer.parseInt((String) ((Node) approvalNodes.get(0)).text());
		}
		
		String website;
		NodeList websiteNodes = (NodeList) node.get(spec.getWebsite());
		if (websiteNodes.size() == 0) {
			website = null;
		} else {
			website = (String) ((Node) websiteNodes.get(0)).text();
		}
		
		Integer type;
		NodeList typeNodes = (NodeList) node.get(spec.getType());
		if (typeNodes.size() == 0) {
			type = null;
		} else {
			type = Integer.parseInt((String) ((Node) typeNodes.get(0)).text());
		}
		
		String comment;
		NodeList commentNodes = (NodeList) node.get(spec.getComment());
		if (commentNodes.size() == 0) {
			comment = null;
		} else {
			comment = (String) ((Node) commentNodes.get(0)).text();
		}
		
		LiteratureItem lit = new LiteratureItem(author, year, title,
				abstractText, journal, volume, issue, page, approvalMode,
				website, type, comment);
		return lit;	
	}
}
