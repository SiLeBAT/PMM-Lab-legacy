package de.bund.bfr.knime.pmm.annotation;

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
	
	XMLNode node;
	String desc;
	
	/**
	 * Builds a DescriptionAnnotation from existing XMLNode.
	 * @param node XMLNode with description
	 */
	public DescriptionAnnotation(XMLNode node) {
		this.node = node;
		
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");
		XMLNode descNode = metadata.getChildElement(DESC_TAG, "");
		desc = descNode.getChild(0).getCharacters();
	}
	
	/**
	 * Builds new DescriptionAnnotation.
	 * @param desc Description.
	 */
	public DescriptionAnnotation(String desc) {
		// Creates annotation node
		node = new XMLNode(new XMLTriple(METADATA_TAG, null, PMF_TAG));
		
		// Creates description node and adds it to the annotation node
		XMLNode descNode = new XMLNode(new XMLTriple(DESC_TAG, null, PMF_TAG));
		descNode.addChild(new XMLNode(desc));
		node.addChild(descNode);
		
		this.desc = desc; // copies description
	}

	// Getters
	public XMLNode getNode() {
		return node;
	}
	
	public String getDescription() {
		return desc;
	}
}
