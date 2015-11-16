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

import org.sbml.jsbml.xml.XMLNamespaces;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.pmf.LiteratureSpecificationI;

/**
 * @author Miguel Alba
 */
public class ReferenceSBMLNode {

	public static final String NS = "dc";
	public static final String TAG = "reference";

	private static final LiteratureSpecificationI spec = new RIS();

	XMLNode node;

	public ReferenceSBMLNode(Reference reference) {

		// Reference container
		XMLTriple refTriple = new XMLTriple(TAG, null, NS);
		XMLNamespaces refNS = new XMLNamespaces();
		refNS.add("http://foo.bar.com", "ref");
		node = new XMLNode(refTriple, null, refNS);

		// author node
		if (reference.isSetAuthor()) {
			XMLTriple authorTriple = new XMLTriple(spec.getAuthor(), null, "ref");
			XMLNode authorNode = new XMLNode(authorTriple);
			authorNode.addChild(new XMLNode(reference.getAuthor()));
			node.addChild(authorNode);
		}

		// year node
		if (reference.isSetYear()) {
			XMLTriple yearTriple = new XMLTriple(spec.getYear(), null, "ref");
			XMLNode yearNode = new XMLNode(yearTriple);
			yearNode.addChild(new XMLNode(reference.getYear().toString()));
			node.addChild(yearNode);
		}

		// title node
		if (reference.isSetTitle()) {
			XMLTriple titleTriple = new XMLTriple(spec.getTitle(), null, "ref");
			XMLNode titleNode = new XMLNode(titleTriple);
			titleNode.addChild(new XMLNode(reference.getTitle()));
			node.addChild(titleNode);
		}

		// abstract node
		if (reference.isSetAbstractText()) {
			XMLTriple abstractTriple = new XMLTriple(spec.getAbstract(), null, "ref");
			XMLNode abstractNode = new XMLNode(abstractTriple);
			abstractNode.addChild(new XMLNode(reference.getAbstractText()));
			node.addChild(abstractNode);
		}

		// journal node
		if (reference.isSetJournal()) {
			XMLTriple journalTriple = new XMLTriple(spec.getJournal(), null, "ref");
			XMLNode journalNode = new XMLNode(journalTriple);
			journalNode.addChild(new XMLNode(reference.getJournal()));
			node.addChild(journalNode);
		}

		// volume node
		if (reference.isSetVolume()) {
			XMLTriple volumeTriple = new XMLTriple(spec.getVolume(), null, "ref");
			XMLNode volumeNode = new XMLNode(volumeTriple);
			volumeNode.addChild(new XMLNode(reference.getVolume()));
			node.addChild(volumeNode);
		}

		// issue node
		if (reference.isSetIssue()) {
			XMLTriple issueTriple = new XMLTriple(spec.getIssue(), null, "ref");
			XMLNode issueNode = new XMLNode(issueTriple);
			issueNode.addChild(new XMLNode(reference.getIssue()));
			node.addChild(issueNode);
		}

		// page node
		if (reference.isSetPage()) {
			XMLTriple pageTriple = new XMLTriple(spec.getPage(), null, "ref");
			XMLNode pageNode = new XMLNode(pageTriple);
			pageNode.addChild(new XMLNode(reference.getPage().toString()));
			node.addChild(pageNode);
		}

		// approval mode node
		if (reference.isSetApprovalMode()) {
			XMLTriple approvalTriple = new XMLTriple(spec.getApproval(), null, "ref");
			XMLNode approvalNode = new XMLNode(approvalTriple);
			approvalNode.addChild(new XMLNode(reference.getApprovalMode().toString()));
			node.addChild(approvalNode);
		}

		// website node
		if (reference.isSetWebsite()) {
			XMLTriple websiteTriple = new XMLTriple(spec.getWebsite(), null, "ref");
			XMLNode websiteNode = new XMLNode(websiteTriple);
			websiteNode.addChild(new XMLNode(reference.getWebsite()));
			node.addChild(websiteNode);
		}

		// type node
		if (reference.isSetType()) {
			XMLTriple typeTriple = new XMLTriple(spec.getType(), null, "ref");
			XMLNode typeNode = new XMLNode(typeTriple);
			typeNode.addChild(new XMLNode(reference.getType().toString()));
			node.addChild(typeNode);
		}

		// comment node
		if (reference.isSetComment()) {
			XMLTriple commentTriple = new XMLTriple(spec.getComment(), null, "ref");
			XMLNode commentNode = new XMLNode(commentTriple);
			commentNode.addChild(new XMLNode(reference.getComment()));
			node.addChild(commentNode);
		}
	}
	
	public ReferenceSBMLNode(XMLNode node) {
		this.node = node;
	}

	public ReferenceImpl toReference() {
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
		ReferenceType type = (typeNode == null) ? null : ReferenceType.valueOf(typeNode.getChild(0).getCharacters());

		XMLNode commentNode = node.getChildElement(spec.getComment(), "");
		String comment = (commentNode == null) ? null : commentNode.getChild(0).getCharacters();

		ReferenceImpl ref = new ReferenceImpl(author, year, title, abstractText, journal, volume, issue, page, approvalMode,
				website, type, comment);
		return ref;
	}
	
	public XMLNode getNode() { return node; }
}
