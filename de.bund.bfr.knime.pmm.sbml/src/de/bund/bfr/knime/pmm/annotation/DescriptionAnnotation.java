package de.bund.bfr.knime.pmm.annotation;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Holds an annotation.
 * @author Miguel de Alba
 */
public class DescriptionAnnotation {
	
	static final String METADATA_TAG = "metadata";
	static final String PMF_TAG = "pmf";
	static final String DESC_TAG = "description";

	String desc;
	XMLNode node;
	
	/**
	 * Builds a DescriptionAnnotation from existing XMLNode.
	 * @param node XMLNode with description
	 * @throws XMLStreamException 
	 */
	public DescriptionAnnotation(final XMLNode node) {
		this.node = node;
		
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");
		XMLNode descNode = metadata.getChildElement(DESC_TAG, "");
		desc = descNode.getChild(0).getCharacters();
	}
	
	/**
	 * Builds new DescriptionAnnotation.
	 * @param desc Description.
	 */
	public DescriptionAnnotation(final String desc) {
		// Creates description node and adds it to the annotation node
		XMLNode descNode = new XMLNode(new XMLTriple(DESC_TAG, null, PMF_TAG));
		descNode.addChild(new XMLNode(desc));
		// Creates metadata node
		XMLNode metadata = new XMLNode(new XMLTriple(METADATA_TAG, null, PMF_TAG));
		metadata.addChild(descNode);
		// Creates node
		node = new XMLNode();
		node.addChild(metadata);
		// Copies description
		this.desc = desc;
	}

	// Getters
	public XMLNode getNode() {
		return node;
	}
	
	public String getDescription() {
		return desc;
	}
}
