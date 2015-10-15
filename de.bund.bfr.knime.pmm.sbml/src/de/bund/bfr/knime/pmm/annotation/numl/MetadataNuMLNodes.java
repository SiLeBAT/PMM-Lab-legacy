package de.bund.bfr.knime.pmm.annotation.numl;

import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.sbmlutil.Metadata;

public class MetadataNuMLNodes {

	public static final String CREATOR_TAG = "dc:creator";
	public static final String CREATED_TAG = "dcterms:created";
	public static final String MODIFIED_TAG = "dcterms:modfied";
	public static final String TYPE_TAG = "dc:type";
	public static final String LICENSE_TAG = "dc:rights";
	public static final String REFERENCE_TAG = "dc:source";

	private Element creatorNode = null;
	private Element createdNode = null;
	private Element modifiedNode = null;
	private Element typeNode = null;
	private Element rightsNode = null;
	private Element referenceNode = null;

	Metadata metadata;

	/**
	 * Builds a {@link MetadataNuMLNodes} from a {@link Metadata}.
	 */
	public MetadataNuMLNodes(final Metadata metadata) {

		Document doc = null;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		if (metadata.isGivenNameSet() || metadata.isFamilyNameSet() || metadata.isContactSet()) {
			String givenName = Strings.emptyToNull(metadata.getGivenName());
			String familyName = Strings.emptyToNull(metadata.getFamilyName());
			String contact = Strings.emptyToNull(metadata.getContact());
			
			String creator = String.format(Locale.ENGLISH, "%s.%s.%s", givenName,
					familyName, contact);
			creatorNode = doc.createElement(CREATOR_TAG);
			creatorNode.setTextContent(creator);
		}

		if (metadata.isCreatedDateSet()) {
			createdNode = doc.createElement(CREATED_TAG);
			createdNode.setTextContent(metadata.getCreatedDate());
		}

		if (metadata.isModifiedDateSet()) {
			modifiedNode = doc.createElement(MODIFIED_TAG);
			modifiedNode.setTextContent(metadata.getModifiedDate());
		}

		if (metadata.isTypeSet()) {
			typeNode = doc.createElement(TYPE_TAG);
			typeNode.setTextContent(metadata.getType());
		}

		if (metadata.areRightsSet()) {
			rightsNode = doc.createElement(LICENSE_TAG);
			rightsNode.setTextContent(metadata.getRights());
		}
		
		if (metadata.isReferenceLinkSet()) {
			referenceNode = doc.createElement(REFERENCE_TAG);
			referenceNode.setTextContent(metadata.getReferenceLink());
		}
		
		this.metadata = metadata;
	}

	public MetadataNuMLNodes(Element creatorNode, Element createdNode, Element modifiedNode, Element typeNode,
			Element rightsNode, Element referenceNode) {
		
		metadata = new Metadata();
		
		if (creatorNode != null) {
			String[] tempStrings = creatorNode.getTextContent().split("\\.", 3);
			metadata.setGivenName(Strings.emptyToNull(tempStrings[0]));
			metadata.setFamilyName(Strings.emptyToNull(tempStrings[1]));
			metadata.setContact(Strings.emptyToNull(tempStrings[2]));
		}

		if (createdNode != null) {
			metadata.setCreatedDate(Strings.emptyToNull(createdNode.getTextContent()));
		}

		if (modifiedNode != null) {
			metadata.setModifiedDate(Strings.emptyToNull(modifiedNode.getTextContent()));
		}

		if (typeNode != null) {
			metadata.setType(Strings.emptyToNull(typeNode.getTextContent()));
		}

		if (rightsNode != null) {
			metadata.setRights(Strings.emptyToNull(rightsNode.getTextContent()));
		}
		
		if (referenceNode != null) {
			metadata.setReferenceLink(Strings.emptyToNull(referenceNode.getTextContent()));
		}

		
		// Copies nodes
		this.creatorNode = creatorNode;
		this.createdNode = createdNode;
		this.modifiedNode = modifiedNode;
		this.typeNode = typeNode;
		this.rightsNode = rightsNode;
		this.referenceNode = referenceNode;
	}

	public Element getCreatorNode() {
		return creatorNode;
	}

	public Element getCreatedNode() {
		return createdNode;
	}

	public Element getModifiedNode() {
		return modifiedNode;
	}

	public Element getTypeNode() {
		return typeNode;
	}

	public Element getRightsNode() {
		return rightsNode;
	}
	
	public Element getReferenceNode() {
		return referenceNode;
	}

	public boolean isCreatorSet() {
		return creatorNode != null;
	}

	public boolean isCreatedSet() {
		return createdNode != null;
	}

	public boolean isModifiedSet() {
		return modifiedNode != null;
	}

	public boolean isTypeSet() {
		return typeNode != null;
	}

	public boolean isLicenseSet() {
		return rightsNode != null;
	}
	
	public boolean isReferenceSet() {
		return referenceNode != null;
	}

	public Metadata getMetadata() {
		return metadata;
	}
}