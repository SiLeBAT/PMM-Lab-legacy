/**
 * Pmm Lab primary model coefficient.
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.Map.Entry;

import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.common.ParamXml;

// Primary model coefficient
public class PrimCoefficient extends Coefficient {

	/** Builds a PrimCoefficient from a SBML parameter */
	public PrimCoefficient(Parameter param) {
		// Get non RDF annotation
		XMLNode nonRDFAnnotation = param.getAnnotation().getNonRDFannotation();
		CoefficientAnnotation annot = new CoefficientAnnotation(
				nonRDFAnnotation);

		P = annot.getP();
		error = annot.getError();
		t = annot.getT();
		correlations = annot.getCorrelations();

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

		// Save P, error, t, and correlations
		P = paramXml.getP();
		error = paramXml.getError();
		t = paramXml.getT();
		correlations = paramXml.getAllCorrelations();

		// Build and set non RDF annotation
		CoefficientAnnotation annot = new CoefficientAnnotation(P, error, t,
				correlations);
		param.getAnnotation().setNonRDFAnnotation(annot.getNode());
	}

	public ParamXml toParamXml() {
		ParamXml paramXml = new ParamXml(param.getId(), param.getValue(),
				error, null, null, P, t);
		for (Entry<String, Double> entry : correlations.entrySet()) {
			paramXml.addCorrelation(entry.getKey(), entry.getValue());
		}
		return paramXml;
	}
}