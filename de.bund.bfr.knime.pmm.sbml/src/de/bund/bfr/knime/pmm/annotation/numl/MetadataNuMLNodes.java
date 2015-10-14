package de.bund.bfr.knime.pmm.annotation.numl;

import java.util.Locale;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.bund.bfr.knime.pmm.sbmlutil.Metadata;

public class MetadataNuMLNodes {

	public static final String CREATOR_TAG = "dc:creator";
	public static final String CREATED_TAG = "dcterms:created";
	public static final String MODIFIED_TAG = "dcterms:modfied";
	public static final String TYPE_TAG = "dc:type";

	private Element creatorNode = null;
	private Element createdNode = null;
	private Element modifiedNode = null;
	private Element typeNode = null;

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

		String givenName = metadata.getGivenName();
		String familyName = metadata.getFamilyName();
		String contact = metadata.getContact();
		if (givenName != null || familyName != null || contact != null) {
			String creator = String.format(Locale.ENGLISH, "%s.%s.%s", metadata.getGivenName(),
					metadata.getFamilyName(), metadata.getContact());
			creatorNode = doc.createElement(CREATOR_TAG);
			creatorNode.setTextContent(creator);
		}
		
		if (metadata.getCreatedDate() != null) {
			createdNode = doc.createElement(CREATED_TAG);
			createdNode.setTextContent(metadata.getCreatedDate());
		}
		
		if (metadata.getModifiedDate() != null) {
			modifiedNode = doc.createElement(MODIFIED_TAG);
			modifiedNode.setTextContent(metadata.getModifiedDate());
		}
		
		if (metadata.getType() != null) {
			typeNode = doc.createElement(TYPE_TAG);
			typeNode.setTextContent(metadata.getType());
		}
	}
	
	public MetadataNuMLNodes(Element creatorNode, Element createdNode, Element modifiedNode, Element typeNode) {
		this.creatorNode = creatorNode;
		this.createdNode = createdNode;
		this.modifiedNode = modifiedNode;
		this.typeNode = typeNode;
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
	
	public Metadata toMetadata() {
		
		String givenName = "";
		String familyName = "";
		String contact = "";
		String created = "";
		String modified = "";
		String type = "";
		
		if (creatorNode != null) {
			String[] tempStrings = creatorNode.getTextContent().split("\\.", 3);
			givenName = tempStrings[0];
			familyName = tempStrings[1];
			contact = tempStrings[2];
		}
		
		if (createdNode != null) {
			created = createdNode.getTextContent();
		}
		
		if (modifiedNode != null) {
			modified = modifiedNode.getTextContent();
		}
		
		if (typeNode != null) {
			type = typeNode.getTextContent();
		}
		
		return new Metadata(givenName, familyName, contact, created, modified, type);
	}
}