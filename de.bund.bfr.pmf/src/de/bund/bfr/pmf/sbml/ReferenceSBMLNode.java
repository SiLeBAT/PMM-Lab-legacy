/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
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

  public ReferenceSBMLNode(final Reference reference) {

    // Reference container
    final XMLNamespaces refNS = new XMLNamespaces();
    refNS.add("http://foo.bar.com", "ref");
    node = new XMLNode(new XMLTriple(TAG, null, NS), null, refNS);

    // author node
    if (reference.isSetAuthor()) {
      final XMLNode authorNode = new XMLNode(new XMLTriple(spec.getAuthor(), null, "ref"));
      authorNode.addChild(new XMLNode(reference.getAuthor()));
      node.addChild(authorNode);
    }

    // year node
    if (reference.isSetYear()) {
      final XMLNode yearNode = new XMLNode(new XMLTriple(spec.getYear(), null, "ref"));
      yearNode.addChild(new XMLNode(reference.getYear().toString()));
      node.addChild(yearNode);
    }

    // title node
    if (reference.isSetTitle()) {
      final XMLNode titleNode = new XMLNode(new XMLTriple(spec.getTitle(), null, "ref"));
      titleNode.addChild(new XMLNode(reference.getTitle()));
      node.addChild(titleNode);
    }

    // abstract node
    if (reference.isSetAbstractText()) {
      final XMLNode abstractNode = new XMLNode(new XMLTriple(spec.getAbstract(), null, "ref"));
      abstractNode.addChild(new XMLNode(reference.getAbstractText()));
      node.addChild(abstractNode);
    }

    // journal node
    if (reference.isSetJournal()) {
      final XMLNode journalNode = new XMLNode(new XMLTriple(spec.getJournal(), null, "ref"));
      journalNode.addChild(new XMLNode(reference.getJournal()));
      node.addChild(journalNode);
    }

    // volume node
    if (reference.isSetVolume()) {
      final XMLNode volumeNode = new XMLNode(new XMLTriple(spec.getVolume(), null, "ref"));
      volumeNode.addChild(new XMLNode(reference.getVolume()));
      node.addChild(volumeNode);
    }

    // issue node
    if (reference.isSetIssue()) {
      final XMLNode issueNode = new XMLNode(new XMLTriple(spec.getIssue(), null, "ref"));
      issueNode.addChild(new XMLNode(reference.getIssue()));
      node.addChild(issueNode);
    }

    // page node
    if (reference.isSetPage()) {
      final XMLNode pageNode = new XMLNode(new XMLTriple(spec.getPage(), null, "ref"));
      pageNode.addChild(new XMLNode(reference.getPage().toString()));
      node.addChild(pageNode);
    }

    // approval mode node
    if (reference.isSetApprovalMode()) {
      final XMLNode approvalNode = new XMLNode(new XMLTriple(spec.getApproval(), null, "ref"));
      approvalNode.addChild(new XMLNode(reference.getApprovalMode().toString()));
      node.addChild(approvalNode);
    }

    // website node
    if (reference.isSetWebsite()) {
      final XMLNode websiteNode = new XMLNode(new XMLTriple(spec.getWebsite(), null, "ref"));
      websiteNode.addChild(new XMLNode(reference.getWebsite()));
      node.addChild(websiteNode);
    }

    // type node
    if (reference.isSetType()) {
      final XMLNode typeNode = new XMLNode(new XMLTriple(spec.getType(), null, "ref"));
      typeNode.addChild(new XMLNode(reference.getType().toString()));
      node.addChild(typeNode);
    }

    // comment node
    if (reference.isSetComment()) {
      final XMLNode commentNode = new XMLNode(new XMLTriple(spec.getComment(), null, "ref"));
      commentNode.addChild(new XMLNode(reference.getComment()));
      node.addChild(commentNode);
    }
  }

  public ReferenceSBMLNode(final XMLNode node) {
    this.node = node;
  }

  public ReferenceImpl toReference() {
    final XMLNode authorNode = node.getChildElement(spec.getAuthor(), "");
    final String author = (authorNode == null) ? null : authorNode.getChild(0).getCharacters();

    final XMLNode titleNode = node.getChildElement(spec.getTitle(), "");
    final String title = (titleNode == null) ? null : titleNode.getChild(0).getCharacters();

    final XMLNode abstractNode = node.getChildElement(spec.getAbstract(), "");
    final String abstractText =
        (abstractNode == null) ? null : abstractNode.getChild(0).getCharacters();

    final XMLNode yearNode = node.getChildElement(spec.getYear(), "");
    final Integer year =
        (yearNode == null) ? null : Integer.parseInt(yearNode.getChild(0).getCharacters());

    final XMLNode journalNode = node.getChildElement(spec.getJournal(), "");
    final String journal = (journalNode == null) ? null : journalNode.getChild(0).getCharacters();

    final XMLNode volumeNode = node.getChildElement(spec.getVolume(), "");
    final String volume = (volumeNode == null) ? null : volumeNode.getChild(0).getCharacters();

    final XMLNode issueNode = node.getChildElement(spec.getIssue(), "");
    final String issue = (issueNode == null) ? null : issueNode.getChild(0).getCharacters();

    final XMLNode pageNode = node.getChildElement(spec.getPage(), "");
    final Integer page =
        (pageNode == null) ? null : Integer.parseInt(pageNode.getChild(0).getCharacters());

    final XMLNode approvalNode = node.getChildElement(spec.getApproval(), "");
    final Integer approvalMode =
        (approvalNode == null) ? null : Integer.parseInt(approvalNode.getChild(0).getCharacters());

    final XMLNode websiteNode = node.getChildElement(spec.getWebsite(), "");
    final String website = (websiteNode == null) ? null : websiteNode.getChild(0).getCharacters();

    final XMLNode typeNode = node.getChildElement(spec.getType(), "");
    final ReferenceType type =
        (typeNode == null) ? null : ReferenceType.valueOf(typeNode.getChild(0).getCharacters());

    final XMLNode commentNode = node.getChildElement(spec.getComment(), "");
    final String comment = (commentNode == null) ? null : commentNode.getChild(0).getCharacters();

    return new ReferenceImpl(author, year, title, abstractText, journal, volume, issue, page,
        approvalMode, website, type, comment);
  }

  public XMLNode getNode() {
    return node;
  }
}
