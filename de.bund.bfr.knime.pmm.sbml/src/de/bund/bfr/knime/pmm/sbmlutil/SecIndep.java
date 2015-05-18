/**
 * Pmm Lab secondary independent
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.IndepXml;

/**
 * Secondary independent. Holds an SBML parameter and its description.
 * 
 * @author Miguel Alba
 */
public class SecIndep {

	Parameter param;
	String desc;

	/**
	 * Builds a SecIndep from an SBML parameter which can be annotated with an
	 * SecIndepAnnotation.
	 * 
	 * @param param
	 *            SBML parameter.
	 */
	public SecIndep(Parameter param) {
		// If param has annotation, processes it
		if (param.getAnnotation().getNonRDFannotation() != null) {
			SecIndepAnnotation annot = new SecIndepAnnotation(param
					.getAnnotation().getNonRDFannotation());
			desc = annot.getDescription();
		}

		// copies parameter
		this.param = param;
	}

	/**
	 * Builds a SecIndep from a Pmm Lab IndepXml.
	 * 
	 * @param indepXml Pmm Lab IndepXml.
	 */
	public SecIndep(IndepXml indepXml) {
		// Initializes param
		param = new Parameter(indepXml.getName());
		param.setConstant(false);

		// If indepXml has an description, saves it within a SecIndepAnnotation
		if (indepXml.getDescription() != null) {
			// Builds and sets non RDF annotation
			SecIndepAnnotation annot = new SecIndepAnnotation(
					indepXml.getDescription());
			param.getAnnotation().setNonRDFAnnotation(annot.getNode());
		}
	}

	/** Creates a Pmm Lab IndepXml. */
	public IndepXml toIndepXml() {
		IndepXml indepXml = new IndepXml(param.getId(), null, null);
		indepXml.setDescription(desc);
		return indepXml;
	}

	// Getters
	public Parameter getParam() {
		return param;
	}

	public String getDescription() {
		return desc;
	}
}

/** SecIndep non RDF annotation. Holds the description of a secondary indep. */
class SecIndepAnnotation {

	static final String METADATA_TAG = "metadata";
	static final String PMF_TAG = "pmf";
	static final String DESC_TAG = "description";

	XMLNode node;
	String desc;

	/** Builds a SecIndepAnnotation from existing XMLNode.
	 * @param node XMLNode with description.
	 */
	public SecIndepAnnotation(XMLNode node) {
		this.node = node;
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");
		XMLNode descNode = metadata.getChildElement(DESC_TAG, "");
		desc = descNode.getChild(0).getCharacters();
	}

	/**
	 * Builds new SecIndepAnnotation.
	 * @param desc Description.
	 */
	public SecIndepAnnotation(String desc) {
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