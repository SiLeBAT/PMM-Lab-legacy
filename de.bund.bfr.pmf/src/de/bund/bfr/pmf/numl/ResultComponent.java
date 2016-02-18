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
package de.bund.bfr.pmf.numl;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.bund.bfr.pmf.LiteratureSpecificationI;
import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.sbml.RIS;
import de.bund.bfr.pmf.sbml.ReferenceImpl;
import de.bund.bfr.pmf.sbml.Reference;
import de.bund.bfr.pmf.sbml.ReferenceType;

/**
 * @author Miguel Alba
 */
public class ResultComponent {

  static final String ELEMENT_NAME = "resultComponent";
  private static final String ANNOTATION = "annotation";
  private static final String METADATA = "metadata";

  private static final String ID = "id";
  private static final String CONDID = "pmmlab:condID";
  private static final String COMBASEID = "pmmlab:combaseID";
  private static final String CREATOR_GIVEN_NAME = "pmmlab:creatorGivenName";
  private static final String CREATOR_FAMILY_NAME = "pmmlab:creatorFamilyName";
  private static final String CREATOR_CONTACT = "pmmlab:creatorContact";
  private static final String CREATED_DATE = "pmmlab:createdDate";
  private static final String MODIFIED_DATE = "pmmlab:modifiedDate";
  private static final String MODEL_TYPE = "pmmlab:modelType";
  private static final String RIGHTS = "pmmlab:rights";

  private static final String DIMENSION_DESCRIPTION = "dimensionDescription";
  private static final String DIMENSION = "dimension";

  private Integer condID;
  private ModelType modelType;
  private Reference[] references;
  private TupleDescription dimensionDescription;
  private Tuple[] dimensions;
  private Map<String, String> strProps; // String properties
  private String notes;

  public ResultComponent(final String id, final TupleDescription dimensionDescription,
      final Tuple[] dimensions) {
    strProps = new HashMap<>(9);
    if (id != null && !id.isEmpty()) {
      strProps.put(ID, id);
    }
    this.dimensionDescription = dimensionDescription;
    this.dimensions = dimensions;
  }

  public ResultComponent(final String id, final Integer condID, final String combaseID,
      final String creatorGivenName, final String creatorFamilyName, final String creatorContact,
      final String createdDate, final String modifiedDate, final ModelType modelType,
      final String rights, final String notes, final Reference[] references,
      final TupleDescription dimensionDescription, final Tuple[] dimensions) {
    strProps = new HashMap<>(9);
    if (id != null && !id.isEmpty())
      strProps.put(ID, id);
    if (condID != null)
      this.condID = condID;
    if (combaseID != null && !combaseID.isEmpty())
      strProps.put(COMBASEID, combaseID);
    if (creatorGivenName != null && !creatorGivenName.isEmpty())
      strProps.put(CREATOR_GIVEN_NAME, creatorGivenName);
    if (creatorFamilyName != null && !creatorFamilyName.isEmpty())
      strProps.put(CREATOR_FAMILY_NAME, creatorFamilyName);
    if (creatorContact != null && !creatorContact.isEmpty())
      strProps.put(CREATOR_CONTACT, creatorContact);
    if (createdDate != null && !createdDate.isEmpty())
      strProps.put(CREATED_DATE, createdDate);
    if (modifiedDate != null && !modifiedDate.isEmpty())
      strProps.put(MODIFIED_DATE, modifiedDate);
    if (modelType != null)
      this.modelType = modelType;
    if (rights != null && !rights.isEmpty())
      strProps.put(RIGHTS, rights);
    if (notes != null && !notes.isEmpty())
      this.notes = notes;
    if (references != null)
      this.references = references;
    this.dimensionDescription = dimensionDescription;
    this.dimensions = dimensions;
  }

  public ResultComponent(final Element node) {

    strProps = new HashMap<>(8);
    strProps.put(ID, node.getAttribute(ID));

    final NodeList annotationNodes = node.getElementsByTagName(ANNOTATION);
    final Element annotationNode = (Element) annotationNodes.item(0);

    final NodeList metadataNodes = annotationNode.getElementsByTagName(METADATA);
    final Element metadataNode = (Element) metadataNodes.item(0);

    final NodeList condIdNodes = metadataNode.getElementsByTagName(CONDID);
    if (condIdNodes.getLength() == 1) {
      final Element condIdElement = (Element) condIdNodes.item(0);
      condID = Integer.valueOf(condIdElement.getTextContent());
    } else {
      condID = null;
    }

    final NodeList combaseIdNodes = metadataNode.getElementsByTagName(COMBASEID);
    if (combaseIdNodes.getLength() == 1) {
      strProps.put(COMBASEID, ((Element) combaseIdNodes.item(0)).getTextContent());
    }

    final NodeList creatorGivenNameNodes = metadataNode.getElementsByTagName(CREATOR_GIVEN_NAME);
    if (creatorGivenNameNodes.getLength() == 1) {
      strProps.put(CREATOR_GIVEN_NAME, ((Element) creatorGivenNameNodes.item(0)).getTextContent());
    }

    final NodeList creatorFamilyNameNodes = metadataNode.getElementsByTagName(CREATOR_FAMILY_NAME);
    if (creatorFamilyNameNodes.getLength() == 1) {
      strProps.put(CREATOR_FAMILY_NAME,
          ((Element) creatorFamilyNameNodes.item(0)).getTextContent());
    }

    final NodeList creatorContactNodes = metadataNode.getElementsByTagName(CREATOR_CONTACT);
    if (creatorContactNodes.getLength() == 1) {
      strProps.put(CREATOR_CONTACT, ((Element) creatorContactNodes.item(0)).getTextContent());
    }

    final NodeList createdDateNodes = metadataNode.getElementsByTagName(CREATED_DATE);
    if (createdDateNodes.getLength() == 1) {
      strProps.put(CREATED_DATE, ((Element) createdDateNodes.item(0)).getTextContent());
    }

    final NodeList modifiedDateNodes = metadataNode.getElementsByTagName(MODIFIED_DATE);
    if (modifiedDateNodes.getLength() == 1) {
      strProps.put(MODIFIED_DATE, ((Element) modifiedDateNodes.item(0)).getTextContent());
    }

    final NodeList modelTypeNodes = metadataNode.getElementsByTagName(MODEL_TYPE);
    if (modelTypeNodes.getLength() == 1) {
      final Element modelTypeElement = (Element) modelTypeNodes.item(0);
      modelType = ModelType.valueOf(modelTypeElement.getTextContent());
    } else {
      modelType = null;
    }

    final NodeList rightsNodes = metadataNode.getElementsByTagName(RIGHTS);
    if (rightsNodes.getLength() == 1) {
      strProps.put(RIGHTS, ((Element) rightsNodes.item(0)).getTextContent());
    }

    final NodeList refNodes = metadataNode.getElementsByTagName(ReferenceNuMLNode.TAG);
    references = new ReferenceImpl[refNodes.getLength()];
    for (int i = 0; i < refNodes.getLength(); i++) {
      final Element refElement = (Element) refNodes.item(i);
      references[i] = new ReferenceNuMLNode(refElement).toReference();
    }

    final NodeList dimensionDescriptionNodes = node.getElementsByTagName(DIMENSION_DESCRIPTION);
    final Element dimensionDescriptionNode = (Element) dimensionDescriptionNodes.item(0);

    final NodeList tupleDescriptionNodes =
        dimensionDescriptionNode.getElementsByTagName(TupleDescription.ELEMENT_NAME);
    dimensionDescription = new TupleDescription((Element) tupleDescriptionNodes.item(0));

    final NodeList dimensionNodes = node.getElementsByTagName(DIMENSION);
    final Element dimensionNode = (Element) dimensionNodes.item(0);

    final NodeList tupleNodes = dimensionNode.getElementsByTagName(Tuple.ELEMENT_NAME);
    dimensions = new Tuple[tupleNodes.getLength()];
    for (int i = 0; i < tupleNodes.getLength(); i++) {
      dimensions[i] = new Tuple((Element) tupleNodes.item(i));
    }
  }

  public String getID() {
    return strProps.get(ID);
  }

  public Integer getCondID() {
    return condID;
  }

  public void setCondID(final Integer condID) {
    this.condID = condID;
  }

  public boolean isSetCondID() {
    return condID != null;
  }

  public String getCombaseID() {
    return strProps.get(COMBASEID);
  }

  public void setCombaseID(final String combaseID) {
    strProps.put(COMBASEID, combaseID);
  }

  public boolean isSetCombaseID() {
    return strProps.containsKey(COMBASEID);
  }

  public String getCreatorGivenName() {
    return strProps.get(CREATOR_GIVEN_NAME);
  }

  public void setCreatorGivenName(final String creatorGivenName) {
    strProps.put(CREATOR_GIVEN_NAME, creatorGivenName);
  }

  public boolean isSetCreatorGivenName() {
    return strProps.containsKey(CREATOR_GIVEN_NAME);
  }

  public String getCreatorFamilyName() {
    return strProps.get(CREATOR_FAMILY_NAME);
  }

  public void setCreatorFamilyName(final String creatorFamilyName) {
    strProps.put(CREATOR_FAMILY_NAME, creatorFamilyName);
  }

  public boolean isSetCreatorFamilyName() {
    return strProps.containsKey(CREATOR_FAMILY_NAME);
  }

  public String getCreatorContact() {
    return strProps.get(CREATOR_CONTACT);
  }

  public void setCreatorContact(final String creatorContact) {
    strProps.put(CREATOR_CONTACT, creatorContact);
  }

  public boolean isSetCreatorContact() {
    return strProps.containsKey(CREATOR_CONTACT);
  }

  public String getCreatedDate() {
    return strProps.get(CREATED_DATE);
  }

  public void setCreatedDate(final String createdDate) {
    strProps.put(CREATED_DATE, createdDate);
  }

  public boolean isSetCreatedDate() {
    return strProps.containsKey(CREATED_DATE);
  }

  public String getModifiedDate() {
    return strProps.get(MODIFIED_DATE);
  }

  public void setModifiedDate(final String modifiedDate) {
    strProps.put(MODIFIED_DATE, modifiedDate);
  }

  public boolean isSetModifiedDate() {
    return strProps.containsKey(MODIFIED_DATE);
  }

  public ModelType getModelType() {
    return modelType;
  }

  public void setModelType(final ModelType modelType) {
    this.modelType = modelType;
  }

  public boolean isSetModelType() {
    return modelType != null;
  }

  public String getRights() {
    return strProps.get(RIGHTS);
  }

  public void setRights(final String rights) {
    strProps.put(RIGHTS, rights);
  }

  public boolean isSetRights() {
    return strProps.containsKey(RIGHTS);
  }

  public Reference[] getReferences() {
    return references;
  }

  public void setReferences(final Reference[] references) {
    this.references = references;
  }

  public boolean isSetReferences() {
    return references != null;
  }

  public TupleDescription getDimensionDescription() {
    return dimensionDescription;
  }

  public void setDimensionDescription(final TupleDescription dimensionDescription) {
    this.dimensionDescription = dimensionDescription;
  }

  public Tuple[] getDimensions() {
    return dimensions;
  }

  public void setDimensions(final Tuple[] dimensions) {
    this.dimensions = dimensions;
  }

  @Override
  public String toString() {
    final String string =
        String.format("ResultComponent [id=%s, dimensionDescription=%s, dimension=%s]",
            strProps.get(ID), dimensionDescription, dimensions);
    return string;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;

    final ResultComponent other = (ResultComponent) obj;

    if (strProps != null && other.strProps != null && !strProps.equals(other.strProps))
      return false;
    if (condID != null && other.condID != null && !condID.equals(other.condID))
      return false;
    if (modelType != null && other.modelType != null && !modelType.equals(other.modelType))
      return false;
    return true;
  }

  public Element toNode(final Document doc) {

    final Element node = doc.createElement(ELEMENT_NAME);
    node.setAttribute(ID, strProps.get(ID));

    final Element annotation = doc.createElement(ANNOTATION);
    node.appendChild(annotation);

    final Element metadata = doc.createElement(METADATA);
    annotation.appendChild(metadata);

    if (condID != null) {
      final Element condIdNode = doc.createElement(CONDID);
      condIdNode.setTextContent(condID.toString());
      metadata.appendChild(condIdNode);
    }

    if (strProps.containsKey(COMBASEID)) {
      final Element combaseIdNode = doc.createElement(COMBASEID);
      combaseIdNode.setTextContent(strProps.get(COMBASEID));
      metadata.appendChild(combaseIdNode);
    }

    if (strProps.containsKey(CREATOR_GIVEN_NAME)) {
      final Element creatorGivenNameNode = doc.createElement(CREATOR_GIVEN_NAME);
      creatorGivenNameNode.setTextContent(strProps.get(CREATOR_GIVEN_NAME));
      metadata.appendChild(creatorGivenNameNode);
    }

    if (strProps.containsKey(CREATOR_FAMILY_NAME)) {
      final Element creatorFamilyNameNode = doc.createElement(CREATOR_FAMILY_NAME);
      creatorFamilyNameNode.setTextContent(strProps.get(CREATOR_FAMILY_NAME));
      metadata.appendChild(creatorFamilyNameNode);
    }

    if (strProps.containsKey(CREATOR_CONTACT)) {
      final Element creatorContactNode = doc.createElement(CREATOR_CONTACT);
      creatorContactNode.setTextContent(strProps.get(CREATOR_CONTACT));
      metadata.appendChild(creatorContactNode);
    }

    if (strProps.containsKey(CREATED_DATE)) {
      final Element createdDateNode = doc.createElement(CREATED_DATE);
      createdDateNode.setTextContent(strProps.get(CREATED_DATE));
      metadata.appendChild(createdDateNode);
    }

    if (strProps.containsKey(MODIFIED_DATE)) {
      final Element modifiedDateNode = doc.createElement(MODIFIED_DATE);
      modifiedDateNode.setTextContent(strProps.get(MODIFIED_DATE));
      metadata.appendChild(modifiedDateNode);
    }

    if (modelType != null) {
      final Element modelTypeNode = doc.createElement(MODEL_TYPE);
      modelTypeNode.setTextContent(modelType.name());
      metadata.appendChild(modelTypeNode);
    }

    if (strProps.containsKey(RIGHTS)) {
      final Element rightsNode = doc.createElement(RIGHTS);
      rightsNode.setTextContent(strProps.get(RIGHTS));
      metadata.appendChild(rightsNode);
    }

    if (notes != null) {
      final Element notesNode = doc.createElement("notes");
      notesNode.setTextContent(notes);
      metadata.appendChild(notesNode);
    }

    if (references != null) {
      for (final Reference reference : getReferences()) {
        metadata.appendChild(new ReferenceNuMLNode(reference, doc).node);
      }
    }

    final Element dimensionDescriptionNode = doc.createElement(DIMENSION_DESCRIPTION);
    dimensionDescriptionNode.appendChild(dimensionDescription.toNode(doc));
    node.appendChild(dimensionDescriptionNode);

    final Element dimensionNode = doc.createElement(DIMENSION);
    for (final Tuple tuple : getDimensions()) {
      dimensionNode.appendChild(tuple.toNode(doc));
    }
    node.appendChild(dimensionNode);

    return node;
  }
}


class ReferenceNuMLNode {

  public static final String TAG = "dc:reference";

  private static final LiteratureSpecificationI SPEC = new RIS();
  private static final String DC_URI = "http://purl.org/dc/elements/1.1/";

  Element node;

  /**
   * Builds a {@link ReferenceNuMLNode} using the RIS tag set.
   */
  public ReferenceNuMLNode(final Reference reference, final Document doc) {

    // Reference container
    node = doc.createElementNS(DC_URI, TAG);

    if (reference.isSetAuthor()) {
      final Element authorNode = doc.createElement(SPEC.getAuthor());
      authorNode.setTextContent(reference.getAuthor());
      node.appendChild(authorNode);
    }

    if (reference.isSetYear()) {
      final Element yearNode = doc.createElement(SPEC.getYear());
      yearNode.setTextContent(reference.getYear().toString());
      node.appendChild(yearNode);
    }

    if (reference.isSetTitle()) {
      final Element titleNode = doc.createElement(SPEC.getTitle());
      titleNode.setTextContent(reference.getTitle());
      node.appendChild(titleNode);
    }

    if (reference.isSetAbstractText()) {
      final Element abstractNode = doc.createElement(SPEC.getAbstract());
      abstractNode.setTextContent(reference.getAbstractText());
      node.appendChild(abstractNode);
    }

    if (reference.isSetJournal()) {
      final Element journalNode = doc.createElement(SPEC.getJournal());
      journalNode.setTextContent(reference.getJournal());
      node.appendChild(journalNode);
    }

    if (reference.isSetVolume()) {
      final Element volumeNode = doc.createElement(SPEC.getVolume());
      volumeNode.setTextContent(reference.getVolume());
      node.appendChild(volumeNode);
    }

    if (reference.isSetIssue()) {
      final Element issueNode = doc.createElement(SPEC.getIssue());
      issueNode.setTextContent(reference.getIssue());
      node.appendChild(issueNode);
    }

    if (reference.isSetPage()) {
      final Element pageNode = doc.createElement(SPEC.getPage());
      pageNode.setTextContent(reference.getPage().toString());
      node.appendChild(pageNode);
    }

    if (reference.isSetApprovalMode()) {
      final Element approvalNode = doc.createElement(SPEC.getApproval());
      approvalNode.setTextContent(reference.getApprovalMode().toString());
      node.appendChild(approvalNode);
    }

    if (reference.isSetWebsite()) {
      final Element websiteNode = doc.createElement(SPEC.getWebsite());
      websiteNode.setTextContent(reference.getWebsite());
      node.appendChild(websiteNode);
    }

    if (reference.isSetType()) {
      final Element typeNode = doc.createElement(SPEC.getType());
      typeNode.setTextContent(reference.getType().toString());
      node.appendChild(typeNode);
    }

    if (reference.isSetComment()) {
      final Element commentNode = doc.createElement(SPEC.getComment());
      commentNode.setTextContent(reference.getComment());
      node.appendChild(commentNode);
    }
  }

  /**
   * Builds a {@link ReferenceNuMLNode} from a existing {@link Element}.
   */
  public ReferenceNuMLNode(final Element element) {
    node = element;
  }

  /**
   * Creates a {@link ReferenceImpl}.
   */
  ReferenceImpl toReference() {

    final String author;
    final NodeList authorNodes = node.getElementsByTagName(SPEC.getAuthor());
    if (authorNodes.getLength() == 1) {
      author = ((Element) authorNodes.item(0)).getTextContent();
    } else {
      author = null;
    }

    final String title;
    final NodeList titleNodes = node.getElementsByTagName(SPEC.getTitle());
    if (titleNodes.getLength() == 1) {
      title = ((Element) titleNodes.item(0)).getTextContent();
    } else {
      title = null;
    }

    final String abstractText;
    final NodeList abstractNodes = node.getElementsByTagName(SPEC.getAbstract());
    if (abstractNodes.getLength() == 1) {
      abstractText = ((Element) abstractNodes.item(0)).getTextContent();
    } else {
      abstractText = null;
    }

    final Integer year;
    final NodeList yearNodes = node.getElementsByTagName(SPEC.getYear());
    if (yearNodes.getLength() == 1) {
      year = Integer.parseInt(((Element) yearNodes.item(0)).getTextContent());
    } else {
      year = null;
    }

    final String journal;
    final NodeList journalNodes = node.getElementsByTagName(SPEC.getJournal());
    if (journalNodes.getLength() == 1) {
      journal = ((Element) journalNodes.item(0)).getTextContent();
    } else {
      journal = null;
    }

    final String volume;
    final NodeList volumeNodes = node.getElementsByTagName(SPEC.getVolume());
    if (volumeNodes.getLength() == 1) {
      volume = ((Element) volumeNodes.item(0)).getTextContent();
    } else {
      volume = null;
    }

    final String issue;
    final NodeList issueNodes = node.getElementsByTagName(SPEC.getIssue());
    if (issueNodes.getLength() == 1) {
      issue = ((Element) issueNodes.item(0)).getTextContent();
    } else {
      issue = null;
    }

    final Integer page;
    final NodeList pageNodes = node.getElementsByTagName(SPEC.getPage());
    if (pageNodes.getLength() == 1) {
      page = Integer.parseInt(((Element) pageNodes.item(0)).getTextContent());
    } else {
      page = null;
    }

    final Integer approvalMode;
    final NodeList approvalNodes = node.getElementsByTagName(SPEC.getApproval());
    if (approvalNodes.getLength() == 1) {
      approvalMode = Integer.parseInt(((Element) approvalNodes.item(0)).getTextContent());
    } else {
      approvalMode = null;
    }

    final String website;
    final NodeList websiteNodes = node.getElementsByTagName(SPEC.getWebsite());
    if (websiteNodes.getLength() == 1) {
      website = ((Element) websiteNodes.item(0)).getTextContent();
    } else {
      website = null;
    }

    final ReferenceType type;
    final NodeList typeNodes = node.getElementsByTagName(SPEC.getType());
    if (typeNodes.getLength() == 1) {
      type = ReferenceType.valueOf(((Element) typeNodes.item(0)).getTextContent());
    } else {
      type = null;
    }

    final String comment;
    final NodeList commentNodes = node.getElementsByTagName(SPEC.getComment());
    if (commentNodes.getLength() == 1) {
      comment = ((Element) commentNodes.item(0)).getTextContent();
    } else {
      comment = null;
    }

    return new ReferenceImpl(author, year, title, abstractText, journal, volume, issue, page,
        approvalMode, website, type, comment);
  }
}
