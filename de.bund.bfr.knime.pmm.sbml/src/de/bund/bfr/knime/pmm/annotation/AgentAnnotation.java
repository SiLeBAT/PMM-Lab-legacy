package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Holds an JSBML annotation of a SBML species. Describes a species with a CAS
 * number, details and description of the dependent variable.
 * 
 * @author Miguel de Alba
 */
public class AgentAnnotation extends AnnotationBase {

	static final String SOURCE_TAG = "source";
	static final String SOURCE_NS = "dc";

	static final String DETAIL_TAG = "detail";
	static final String DETAIL_NS = "pmmlab";

	static final String DESC_TAG = "desc";
	static final String DESC_NS = "pmmlab";

	String casNumber;
	String detail; // Agent detail
	String depDesc; // Description of the dependent variable

	/**
	 * Builds an AgentAnnotation from existing annotation, parsing its CAS
	 * number and description.
	 * 
	 * @param annot
	 *            Annotation.
	 */
	public AgentAnnotation(final Annotation annotation) {
		this.annotation = annotation;

		// Parses annotation
		XMLNode metadata = annotation.getNonRDFannotation().getChildElement(METADATA_TAG, "");

		// Gets CAS number
		XMLNode sourceNode = metadata.getChildElement(SOURCE_TAG, "");
		if (sourceNode != null) {
			casNumber = sourceNode.getChild(0).getCharacters(); // whole
																// reference
			casNumber = casNumber.substring(casNumber.lastIndexOf('/') + 1);
		}

		// Gets description
		XMLNode detailNode = metadata.getChildElement(DETAIL_TAG, "");
		if (detailNode != null) {
			detail = detailNode.getChild(0).getCharacters();
		}

		// Gets dep description
		XMLNode descNode = metadata.getChildElement(DESC_TAG, "");
		if (descNode != null) {
			depDesc = descNode.getChild(0).getCharacters();
		}
	}

	/**
	 * Builds new AgentAnnotation for a CAS number, agent detail and dep
	 * description.
	 * 
	 * @param casNumber
	 *            CAS number
	 * @param detail
	 *            Agent detail
	 * @param description
	 *            Description of the dependent variable
	 */
	public AgentAnnotation(final String casNumber, final String detail, final String depDesc) {

		annotation = new Annotation();

		// Builds PMF container
		XMLNode metadataNode = new XMLNode(new XMLTriple(METADATA_TAG, null, METADATA_NS));
		annotation.setNonRDFAnnotation(metadataNode);

		// Builds reference tag
		if (casNumber != null) {
			XMLNode refNode = new XMLNode(new XMLTriple(SOURCE_TAG, null, SOURCE_NS));
			refNode.addChild(new XMLNode("http://identifiers.org/ncim/" + casNumber));
			metadataNode.addChild(refNode);
		}

		// Builds detail tag
		if (detail != null) {
			XMLTriple detailTriple = new XMLTriple(DETAIL_TAG, null, DETAIL_NS);
			XMLNode detailNode = new XMLNode(detailTriple);
			detailNode.addChild(new XMLNode(detail));
			metadataNode.addChild(detailNode);
		}

		// Builds dep description tag
		if (depDesc != null) {
			XMLTriple descTriple = new XMLTriple(DESC_TAG, null, DESC_NS);
			XMLNode descNode = new XMLNode(descTriple);
			descNode.addChild(new XMLNode(depDesc));
			metadataNode.addChild(descNode);
		}

		this.casNumber = casNumber; // Copies CAS number
		this.detail = detail; // Copies description
		this.depDesc = depDesc; // Copies dep description
	}

	// Getters
	public String getCasNumber() {
		return casNumber;
	}

	public String getDetail() {
		return detail;
	}

	public String getDepDesc() {
		return depDesc;
	}
}
