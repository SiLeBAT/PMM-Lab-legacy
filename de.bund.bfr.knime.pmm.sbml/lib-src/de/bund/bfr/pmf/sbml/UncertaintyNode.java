package de.bund.bfr.pmf.sbml;

import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Uncertainty xml node. Uses the pmml:modelquality tag. E.g.
 * <pmml:modelquality r-squared="0.996" rootMeanSquaredError="0.345" dataName=
 * "Missing data name" AIC="-32.977" BIC="-34.994"/>
 * 
 * @author Miguel Alba
 */
public class UncertaintyNode {

	public final static String TAG = "modelquality";
	public final static String NS = "pmmlab";

	public final static String ID = "id";
	public final static String NAME = "name";
	public final static String COMMENT = "dataUsage";
	public final static String R2 = "r-squared";
	public final static String RMS = "rootMeanSquaredError";
	public final static String SSE = "sumSquaredError";
	public final static String AIC = "AIC";
	public final static String BIC = "BIC";
	public final static String DOF = "degreesOfFreedom";

	private XMLNode node;

	public UncertaintyNode(XMLNode node) {
		this.node = node;
	}

	/**
	 * Builds an UncertaintyNode from uncertainty measures.
	 */
	public UncertaintyNode(Uncertainties uncertainties) {
		XMLAttributes attrs = new XMLAttributes();
		if (uncertainties.isSetID())
			attrs.add(ID, uncertainties.getID().toString());
		if (uncertainties.isSetModelName())
			attrs.add(NAME, uncertainties.getModelName());
		if (uncertainties.isSetComment())
			attrs.add(COMMENT, uncertainties.getComment());
		if (uncertainties.isSetR2())
			attrs.add(R2, uncertainties.getR2().toString());
		if (uncertainties.isSetRMS())
			attrs.add(RMS, uncertainties.getRMS().toString());
		if (uncertainties.isSetSSE())
			attrs.add(SSE, uncertainties.getSSE().toString());
		if (uncertainties.isSetAIC())
			attrs.add(AIC, uncertainties.getAIC().toString());
		if (uncertainties.isSetBIC())
			attrs.add(BIC, uncertainties.getBIC().toString());
		if (uncertainties.isSetDOF())
			attrs.add(DOF, uncertainties.getDOF().toString());

		XMLTriple triple = new XMLTriple(TAG, null, NS);
		node = new XMLNode(triple, attrs);
	}

	/**
	 * Gets uncertainty measures.
	 */
	public Uncertainties getMeasures() {

		XMLAttributes attrs = node.getAttributes();

		Integer id = (attrs.hasAttribute(ID)) ? Integer.parseInt(attrs.getValue(ID)) : null;
		String modelName = attrs.getValue(NAME);
		String comment = attrs.getValue(COMMENT);
		Double r2 = (attrs.hasAttribute(R2)) ? Double.parseDouble(attrs.getValue(R2)) : null;
		Double rms = (attrs.hasAttribute(RMS)) ? Double.parseDouble(attrs.getValue(RMS)) : null;
		Double sse = (attrs.hasAttribute(SSE)) ? Double.parseDouble(attrs.getValue(SSE)) : null;
		Double aic = (attrs.hasAttribute(AIC)) ? Double.parseDouble(attrs.getValue(AIC)) : null;
		Double bic = (attrs.hasAttribute(BIC)) ? Double.parseDouble(attrs.getValue(BIC)) : null;
		Integer dof = (attrs.hasAttribute(DOF)) ? Integer.parseInt(attrs.getValue(DOF)) : null;

		Uncertainties uncertainties = new UncertaintiesImpl(id, modelName, comment, r2, rms, sse, aic, bic, dof);
		return uncertainties;
	}

	public XMLNode getNode() {
		return node;
	}
}