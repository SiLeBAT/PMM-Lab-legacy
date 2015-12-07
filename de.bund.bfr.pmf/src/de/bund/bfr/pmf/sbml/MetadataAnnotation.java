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

import java.util.Locale;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.pmf.ModelType;

public class MetadataAnnotation {

	private static final String METADATA_TAG = "metadata"; // Metadata tag
	private static final String METADATA_NS = "pmf"; // Metadata namespace

	private final static String CREATOR_NS = "dc";
	private final static String CREATOR_TAG = "creator";

	private final static String CREATED_NS = "dcterms";
	private final static String CREATED_TAG = "created";

	private final static String MODIFIED_NS = "dcterms";
	private final static String MODIFIED_TAG = "modified";

	private final static String TYPE_NS = "dc";
	private final static String TYPE_TAG = "type";

	private final static String RIGHTS_NS = "dc";
	private final static String RIGHTS_TAG = "rights";

	private final static String REFERENCE_NS = "dc";
	private final static String REFERENCE_TAG = "source";

	Metadata metadata;
	Annotation annotation;

	// Builds a MetadataAnntotation from a given metadata object.
	public MetadataAnnotation(final Metadata metadata) {

		XMLTriple pmfTriple = new XMLTriple(METADATA_TAG, "", METADATA_NS);
		XMLNode pmfNode = new XMLNode(pmfTriple);

		// Builds creator node
		if (metadata.isSetGivenName() || metadata.isSetFamilyName() || metadata.isSetContact()) {

			String givenName = metadata.isSetGivenName() ? metadata.getGivenName() : "";
			String familyName = metadata.isSetFamilyName() ? metadata.getFamilyName() : "";
			String contact = metadata.isSetContact() ? metadata.getContact() : "";

			String creator = String.format(Locale.ENGLISH, "%s.%s.%s", givenName, familyName, contact);

			XMLTriple creatorTriple = new XMLTriple(CREATOR_TAG, null, CREATOR_NS);
			XMLNode creatorNode = new XMLNode(creatorTriple);
			creatorNode.addChild(new XMLNode(creator));

			pmfNode.addChild(creatorNode);
		}

		// Builds created date node
		if (metadata.isSetCreatedDate()) {
			XMLTriple createdTriple = new XMLTriple(CREATED_TAG, "", CREATED_NS);
			XMLNode createdNode = new XMLNode(createdTriple);
			createdNode.addChild(new XMLNode(metadata.getCreatedDate()));

			pmfNode.addChild(createdNode);
		}

		// Builds modified date node
		if (metadata.isSetModifiedDate()) {
			XMLTriple modifiedTriple = new XMLTriple(MODIFIED_TAG, "", MODIFIED_NS);
			XMLNode modifiedNode = new XMLNode(modifiedTriple);
			modifiedNode.addChild(new XMLNode(metadata.getModifiedDate()));

			pmfNode.addChild(modifiedNode);
		}

		// Builds type node
		if (metadata.isSetType()) {
			XMLTriple typeTriple = new XMLTriple(TYPE_TAG, "", TYPE_NS);
			XMLNode typeNode = new XMLNode(typeTriple);
			typeNode.addChild(new XMLNode(metadata.getType().name()));

			pmfNode.addChild(typeNode);
		}

		// Builds rights node
		if (metadata.isSetRights()) {
			XMLTriple rightsTriple = new XMLTriple(RIGHTS_TAG, "", RIGHTS_NS);
			XMLNode rightsNode = new XMLNode(rightsTriple);
			rightsNode.addChild(new XMLNode(metadata.getRights()));

			pmfNode.addChild(rightsNode);
		}

		// Builds reference node
		if (metadata.isSetReferenceLink()) {
			XMLTriple refTriple = new XMLTriple(REFERENCE_TAG, "", REFERENCE_NS);
			XMLNode refNode = new XMLNode(refTriple);
			refNode.addChild(new XMLNode(metadata.getReferenceLink()));

			pmfNode.addChild(refNode);
		}

		// Copies metadata
		this.metadata = metadata;

		// Creates annotation
		annotation = new Annotation();
		annotation.setNonRDFAnnotation(pmfNode);
	}

	public MetadataAnnotation(final Annotation annotation) {
		XMLNode pmfNode = annotation.getNonRDFannotation().getChildElement(METADATA_TAG, "");

		metadata = SBMLFactory.createMetadata();

		XMLNode creatorNode = pmfNode.getChildElement(CREATOR_TAG, "");
		if (creatorNode != null) {
			String[] tempStrings = creatorNode.getChild(0).getCharacters().split("\\.", 3);
			
			metadata.setGivenName((tempStrings[0] == null) ? "" : tempStrings[0]);
			metadata.setFamilyName((tempStrings[1] == null) ? "" : tempStrings[1]);
			metadata.setContact((tempStrings[2] == null) ? "" : tempStrings[2]);
		}

		XMLNode createdNode = pmfNode.getChildElement(CREATED_TAG, "");
		if (createdNode != null) {
			metadata.setCreatedDate(createdNode.getChild(0).getCharacters());
		}

		XMLNode modifiedNode = pmfNode.getChildElement(MODIFIED_TAG, "");
		if (modifiedNode != null) {
			metadata.setModifiedDate(modifiedNode.getChild(0).getCharacters());
		}

		XMLNode typeNode = pmfNode.getChildElement(TYPE_TAG, "");
		if (typeNode != null) {
			ModelType modelType = ModelType.valueOf(typeNode.getChild(0).getCharacters());
			metadata.setType(modelType);
		}

		XMLNode rightsNode = pmfNode.getChildElement(RIGHTS_TAG, "");
		if (rightsNode != null) {
			metadata.setRights(rightsNode.getChild(0).getCharacters());
		}

		XMLNode refNode = pmfNode.getChildElement(REFERENCE_TAG, "");
		if (refNode != null) {
			metadata.setReferenceLink(refNode.getChild(0).getCharacters());
		}

		// Copies annotation
		this.annotation = annotation;
	}

	// Getters
	public Metadata getMetadata() {
		return metadata;
	}
	
	public Annotation getAnnotation() {
		return annotation;
	}
}