/**
 * Pmm Lab secondary independent
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.IndepXml;

public class SecIndep {

	Parameter param;
	String desc;

	public SecIndep(Parameter param) {
		// If param has annotation, process it
		if (param.getAnnotation().getNonRDFannotation() != null) {
			SecIndepAnnotation annot = new SecIndepAnnotation(param
					.getAnnotation().getNonRDFannotation());
			desc = annot.getDescription();
		}

		// copy parameter
		this.param = param;
	}

	public SecIndep(IndepXml indepXml) {
		// Initialize param
		param = new Parameter(indepXml.getName());
		param.setConstant(false);

		if (indepXml.getDescription() != null) {
			// Build and set non RDF annotation
			SecIndepAnnotation annot = new SecIndepAnnotation(
					indepXml.getDescription());
			param.getAnnotation().setNonRDFAnnotation(annot.getNode());
		}
	}

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

class SecIndepAnnotation {

	static final String METADATA_TAG = "metadata";
	static final String PMF_TAG = "pmf";
	static final String DESC_TAG = "description";

	private XMLNode node;
	private String desc;

	public SecIndepAnnotation(XMLNode node) {
		this.node = node;
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");
		XMLNode descNode = metadata.getChildElement(DESC_TAG, "");
		desc = descNode.getChild(0).getCharacters();
	}

	public SecIndepAnnotation(String desc) {
		node = new XMLNode(new XMLTriple(METADATA_TAG, null, PMF_TAG));

		XMLNode descNode = new XMLNode(new XMLTriple(DESC_TAG, null, PMF_TAG));
		descNode.addChild(new XMLNode(desc));
		node.addChild(descNode);

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