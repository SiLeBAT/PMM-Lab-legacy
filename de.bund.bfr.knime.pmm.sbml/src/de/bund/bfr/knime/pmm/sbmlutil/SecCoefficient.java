/**
 * Pmm Lab secondary model coefficient
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.Map.Entry;

import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.xml.XMLNode;

import de.bund.bfr.knime.pmm.common.ParamXml;

// Secondary model coefficient
public class SecCoefficient extends Coefficient {

	/** Builds a SecCoefficient from a SBML parameter */
	public SecCoefficient(Parameter param) {
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

	/** Builds a SecCoefficient from a PmmLab ParamXml */
	public SecCoefficient(ParamXml paramXml) {
		param = new Parameter(paramXml.getName());
		param.setValue(paramXml.getValue());
		param.setUnits("dimensionless");
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
		paramXml.setDescription("coefficient");
		for (Entry<String, Double> entry : correlations.entrySet()) {
			paramXml.addCorrelation(entry.getKey(), entry.getValue());
		}
		return paramXml;
	}
}