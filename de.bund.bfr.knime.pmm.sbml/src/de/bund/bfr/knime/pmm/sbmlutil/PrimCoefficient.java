/**
 * Pmm Lab primary model coefficient.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.ParamXml;

public class PrimCoefficient {

	private Parameter param;

	private Double P;
	private Double error;
	private Double t;

	/** Builds a PrimCoefficient from a SBML parameter */
	public PrimCoefficient(Parameter param) {
		// Get non RDF annotation
		XMLNode metadata = param.getAnnotation().getNonRDFannotation()
				.getChildElement("metadata", "");

		// Get P, error, and T
		XMLNode pNode = metadata.getChildElement("P", "");
		if (pNode != null) {
			P = Double.parseDouble(pNode.getChild(0).getCharacters());
		}

		XMLNode errorNode = metadata.getChildElement("error", "");
		if (errorNode != null) {
			error = Double.parseDouble(errorNode.getChild(0).getCharacters());
		}

		XMLNode tNode = metadata.getChildElement("t", "");
		if (tNode != null) {
			t = Double.parseDouble(tNode.getChild(0).getCharacters());
		}

		this.param = param;
	}

	/** Builds a PrimCoefficient from a PmmLab ParamXml */
	public PrimCoefficient(ParamXml paramXml) {
		param = new Parameter(paramXml.getName());

		// If parameter is not a secondary model's variable then it should have
		// a value
		if (paramXml.getValue() != null) {
			param.setValue(paramXml.getValue());
		}

		if (paramXml.getUnit() == null) {
			param.setUnits("dimensionless");
		} else {
			param.setUnits(Util.createId(paramXml.getUnit()));
		}
		param.setConstant(true);

		// Save P, error, and t
		P = paramXml.getP();
		error = paramXml.getError();
		t = paramXml.getT();

		// Build annotation

		// * Build non RDF annotation (metadata)
		XMLTriple pmfTriple = new XMLTriple("metadata", null, "pmf");
		XMLNode pmfNode = new XMLNode(pmfTriple);

		// * Create annotation for P
		if (P != null) {
			XMLNode pNode = new XMLNode(new XMLTriple("P", null, "pmf"));
			pNode.addChild(new XMLNode(paramXml.getP().toString()));
			pmfNode.addChild(pNode);
		}

		// * Create annotation for error
		if (error != null) {
			XMLNode errorNode = new XMLNode(new XMLTriple("error", null, "pmf"));
			errorNode.addChild(new XMLNode(paramXml.getError().toString()));
			pmfNode.addChild(errorNode);
		}

		// * Create annotation for t
		if (t != null) {
			XMLNode tNode = new XMLNode(new XMLTriple("t", null, "pmf"));
			tNode.addChild(new XMLNode(paramXml.getT().toString()));
			pmfNode.addChild(tNode);
		}

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
		return paramXml;
	}
}
