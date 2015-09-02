package de.bund.bfr.knime.pmm.annotation;

import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.sbmlutil.Uncertainties;

/**
 * Uncertainty xml node. Uses the pmml:modelquality tag. E.g.
 * <pmml:modelquality r-squared="0.996" rootMeanSquaredError="0.345" dataName=
 * "Missing data name" AIC="-32.977" BIC="-34.994"/>
 * 
 * @author Miguel Alba
 */
public class UncertaintyNode extends SBMLNodeBase {

	public final static String TAG = "modelquality";
	public final static String NS = "pmmlab";
	
	public final static String ID_TAG = "id";
	public final static String COMMENT_TAG = "dataUsage";
	public final static String R2_TAG = "r-squared";
	public final static String RMS_TAG = "rootMeanSquaredError";
	public final static String SSE_TAG = "sumSquaredError";
	public final static String AIC_TAG = "AIC";
	public final static String BIC_TAG = "BIC";
	public final static String DOF_TAG = "degreesOfFreedom";

	/**
	 * Builds an UncertaintyNode from an existing XMLNode.
	 * 
	 * @param node
	 *            XMLNode.
	 */
	public UncertaintyNode(XMLNode node) {
		this.node = node;
	}

	/**
	 * Builds an UncertaintyNode from uncertainty measures.
	 */
	public UncertaintyNode(Uncertainties uncertainties) {
		XMLAttributes attrs = new XMLAttributes();
		
		EstModelXml estModel = uncertainties.getEstModelXml();
		attrs.add(ID_TAG, Integer.toString(estModel.getId()));
		if (estModel.getComment() != null)
			attrs.add(COMMENT_TAG, estModel.getComment());
		if (estModel.getR2() != null)
			attrs.add(R2_TAG, Double.toString(estModel.getR2()));
		if (estModel.getRms() != null)
			attrs.add(RMS_TAG, Double.toString(estModel.getRms()));
		if (estModel.getSse() != null)
			attrs.add(SSE_TAG, Double.toString(estModel.getSse()));
		if (estModel.getAic() != null)
			attrs.add(AIC_TAG, Double.toString(estModel.getAic()));
		if (estModel.getBic() != null)
			attrs.add(BIC_TAG, Double.toString(estModel.getBic()));
		if (estModel.getDof() != null)
			attrs.add(DOF_TAG, Integer.toString(estModel.getDof()));

		XMLTriple triple = new XMLTriple(TAG, null, NS);
		node = new XMLNode(triple, attrs);
	}

	/**
	 * Gets uncertainty measures.
	 */
	public Uncertainties getMeasures() {

		int id; // id
		String comment = null; // dataUsage
		Double r2 = null;
		Double rms = null;
		Double sse = null;
		Double aic = null;
		Double bic = null;
		Integer dof = null;

		XMLAttributes attributes = node.getAttributes();

		// the id attribute is mandatory
		id = Integer.parseInt(attributes.getValue(ID_TAG));

		if (attributes.hasAttribute(COMMENT_TAG))
			comment = attributes.getValue(COMMENT_TAG);
		
		if (attributes.hasAttribute(R2_TAG))
			r2 = Double.parseDouble(attributes.getValue(R2_TAG));

		if (attributes.hasAttribute(RMS_TAG))
			rms = Double.parseDouble(attributes.getValue(RMS_TAG));
		
		if (attributes.hasAttribute(SSE_TAG))
			sse = Double.parseDouble(attributes.getValue(SSE_TAG));
			
		if (attributes.hasAttribute(AIC_TAG))
			aic = Double.parseDouble(attributes.getValue(AIC_TAG));
		
		if (attributes.hasAttribute(BIC_TAG))
			bic = Double.parseDouble(attributes.getValue(BIC_TAG));
		
		if (attributes.hasAttribute(DOF_TAG))
			dof = Integer.parseInt(attributes.getValue(DOF_TAG));
		
		Uncertainties uncertainties = new Uncertainties(id, null, comment, r2, rms, sse, aic, bic, dof);
		return uncertainties;	
	}
}