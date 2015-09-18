package de.bund.bfr.knime.pmm.annotation;

import java.util.Locale;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.sbmlutil.Metadata;

public class MetadataAnnotation extends AnnotationBase {

	static final String CREATOR_TAG = "creator";
	static final String CREATOR_NS = "dc";

	static final String CREATED_TAG = "created";
	static final String CREATED_NS = "dcterms";

	static final String MODIFIED_TAG = "modified";
	static final String MODIFIED_NS = "dcterms";

	static final String TYPE_TAG = "type";
	static final String TYPE_NS = "dc";

	Metadata metadata;

	// Builds a MetadataAnntotation from a given metadata object.
	public MetadataAnnotation(final Metadata metadata) {

		XMLTriple pmfTriple = new XMLTriple(METADATA_TAG, "", METADATA_NS);
		XMLNode pmfNode = new XMLNode(pmfTriple);

		// Builds creator node
		if (metadata.getGivenName() != null || metadata.getFamilyName() != null || metadata.getContact() != null) {
			XMLTriple creatorTriple = new XMLTriple(CREATOR_TAG, null, CREATOR_NS);
			XMLNode creatorNode = new XMLNode(creatorTriple);

			String creator = String.format(Locale.ENGLISH, "%s.%s.%s", metadata.getGivenName(),
					metadata.getFamilyName(), metadata.getContact());
			creatorNode.addChild(new XMLNode(creator));

			pmfNode.addChild(creatorNode);
		}

		// Builds created date node
		if (metadata.getCreatedDate() != null) {
			XMLTriple createdTriple = new XMLTriple(CREATED_TAG, "", CREATED_NS);
			XMLNode createdNode = new XMLNode(createdTriple);
			createdNode.addChild(new XMLNode(metadata.getCreatedDate()));

			pmfNode.addChild(createdNode);
		}

		// Builds modified date node
		if (metadata.getModifiedDate() != null) {
			XMLTriple modifiedTriple = new XMLTriple(MODIFIED_TAG, "", MODIFIED_NS);
			XMLNode modifiedNode = new XMLNode(modifiedTriple);
			modifiedNode.addChild(new XMLNode(metadata.getModifiedDate()));

			pmfNode.addChild(modifiedNode);
		}

		// Builds type node
		if (metadata.getType() != null) {
			XMLTriple typeTriple = new XMLTriple(TYPE_TAG, "", TYPE_NS);
			XMLNode typeNode = new XMLNode(typeTriple);
			typeNode.addChild(new XMLNode(metadata.getType()));

			pmfNode.addChild(typeNode);
		}

		// Copies metadata
		this.metadata = metadata;

		// Creates annotation
		annotation = new Annotation();
		annotation.setNonRDFAnnotation(pmfNode);
	}

	public MetadataAnnotation(final Annotation annotation) {
		XMLNode pmfNode = annotation.getNonRDFannotation().getChildElement(METADATA_TAG, "");

		metadata = new Metadata();

		XMLNode creatorNode = pmfNode.getChildElement(CREATOR_TAG, "");
		if (creatorNode != null) {
			String[] tempStrings = creatorNode.getChild(0).getCharacters().split("\\.", 3);
			metadata.setGivenName(tempStrings[0]);
			metadata.setFamilyName(tempStrings[1]);
			metadata.setContact(tempStrings[2]);
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
			metadata.setType(typeNode.getChild(0).getCharacters());
		}

		// Copies annotation
		this.annotation = annotation;
	}

	// Getters
	public Metadata getMetadata() {
		return metadata;
	}
}