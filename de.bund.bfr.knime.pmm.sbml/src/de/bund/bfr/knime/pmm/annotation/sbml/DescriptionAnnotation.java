package de.bund.bfr.knime.pmm.annotation.sbml;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Holds an annotation.
 * @author Miguel de Alba
 */
public class DescriptionAnnotation extends AnnotationBase {
	
	static final String DESC_TAG = "description";

	String desc;
	
	/**
	 * Builds a DescriptionAnnotation from existing XMLNode.
	 * @param node XMLNode with description
	 * @throws XMLStreamException 
	 */
	public DescriptionAnnotation(final Annotation annotation) {
		this.annotation = annotation;
		
		XMLNode metadata = annotation.getNonRDFannotation().getChildElement(METADATA_TAG, "");
		XMLNode descNode = metadata.getChildElement(DESC_TAG, "");
		desc = descNode.getChild(0).getCharacters();
	}
	
	/**
	 * Builds new DescriptionAnnotation.
	 * @param desc Description.
	 */
	public DescriptionAnnotation(final String desc) {

		// Creates description node and adds it to the annotation node
		XMLNode descNode = new XMLNode(new XMLTriple(DESC_TAG, null, "pmf"));
		descNode.addChild(new XMLNode(desc));

		// Creates metadata node
		XMLNode metadata = new XMLNode(new XMLTriple(METADATA_TAG, null, METADATA_NS));
		metadata.addChild(descNode);

		// Creates annotation
		annotation = new Annotation();
		annotation.setNonRDFAnnotation(metadata);

		// Copies description
		this.desc = desc;
	}

	// Getters
	public String getDescription() {
		return desc;
	}
}
