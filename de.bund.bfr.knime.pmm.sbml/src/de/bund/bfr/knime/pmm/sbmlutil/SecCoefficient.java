/**
 * Pmm Lab secondary model coefficient
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.ParamXml;

public class SecCoefficient {
	private Parameter param;

	private double P;
	private double error;
	private double t;

	public SecCoefficient(Parameter param) {
		// Get non RDF annotation
		XMLNode metadata = param.getAnnotation().getNonRDFannotation()
				.getChildElement("metadata", "");

		// Get P, error, and T
		P = Double.parseDouble(metadata.getChildElement("P", "")
				.getChild(0).getCharacters());
		error = Double.parseDouble(metadata.getChildElement("error", "")
				.getChild(0).getCharacters());
		t = Double.parseDouble(metadata.getChildElement("t", "").getChild(0)
				.getCharacters());

		this.param = param;
	}

	public SecCoefficient(ParamXml paramXml) {
		param = new Parameter(paramXml.getName());
		param.setValue(paramXml.getValue());
		param.setUnits("dimensionless");
		param.setConstant(true);

		// Save P, error, and T
		P = paramXml.getP();
		error = paramXml.getError();
		t = paramXml.getT();

		// Build annotation

		// * Build non RDF annotation (metadata)
		XMLTriple pmfTriple = new XMLTriple("metadata", null, "pmf");
		XMLNode pmfNode = new XMLNode(pmfTriple);

		// * Create annotation for P
		XMLNode pNode = new XMLNode(new XMLTriple("P", null, "pmf"));
		pNode.addChild(new XMLNode(paramXml.getP().toString()));
		pmfNode.addChild(pNode);

		// * Create annotation for error
		XMLNode errorNode = new XMLNode(new XMLTriple("error", null, "pmf"));
		errorNode.addChild(new XMLNode(paramXml.getError().toString()));
		pmfNode.addChild(errorNode);

		// * Create annotation for t
		XMLNode tNode = new XMLNode(new XMLTriple("t", null, "pmf"));
		tNode.addChild(new XMLNode(paramXml.getT().toString()));
		pmfNode.addChild(tNode);

		// * Set non RDF annotation
		param.getAnnotation().setNonRDFAnnotation(pmfNode);
	}

	// Getters
	public double getP() {
		return P;
	}

	public double getError() {
		return error;
	}

	public double getT() {
		return t;
	}

	public Parameter getParameter() {
		return param;
	}

	public ParamXml toParamXml() {
		ParamXml paramXml = new ParamXml(param.getId(), param.getValue(),
				error, null, null, P, t);
		paramXml.setDescription("coefficient");
		return paramXml;
	}
}
