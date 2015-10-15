package de.bund.bfr.knime.pmm.annotation.sbml;

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
	public final static String NAME_TAG = "name";
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
	public UncertaintyNode(final XMLNode node) {
		this.node = node;
	}

	/**
	 * Builds an UncertaintyNode from uncertainty measures.
	 */
	public UncertaintyNode(final Uncertainties uncertainties) {
		XMLAttributes attrs = new XMLAttributes();

		EstModelXml estModel = uncertainties.getEstModelXml();
		if (estModel.getId() != null)
			attrs.add(ID_TAG, Integer.toString(estModel.getId()));
		if (!estModel.getName().isEmpty())
			attrs.add(NAME_TAG, estModel.getName());
		if (!estModel.getComment().isEmpty())
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

		XMLAttributes attrs = node.getAttributes();

		// the id attribute is mandatory
		Integer id = (attrs.hasAttribute(ID_TAG)) ? Integer.parseInt(attrs.getValue(ID_TAG)) : null;
		String name = (attrs.hasAttribute(NAME_TAG)) ? attrs.getValue(NAME_TAG) : "";
		String comment = (attrs.hasAttribute(COMMENT_TAG)) ? attrs.getValue(COMMENT_TAG) : "";
		Double r2 = (attrs.hasAttribute(R2_TAG)) ? Double.parseDouble(attrs.getValue(R2_TAG)) : null;
		Double rms = (attrs.hasAttribute(RMS_TAG)) ? Double.parseDouble(attrs.getValue(RMS_TAG)) : null;
		Double sse = (attrs.hasAttribute(SSE_TAG)) ? Double.parseDouble(attrs.getValue(SSE_TAG)) : null;
		Double aic = (attrs.hasAttribute(AIC_TAG)) ? Double.parseDouble(attrs.getValue(AIC_TAG)) : null;
		Double bic = (attrs.hasAttribute(BIC_TAG)) ? Double.parseDouble(attrs.getValue(BIC_TAG)) : null;
		Integer dof = (attrs.hasAttribute(DOF_TAG)) ? Integer.parseInt(attrs.getValue(DOF_TAG)) : null;

		Uncertainties uncertainties = new Uncertainties(id, name, comment, r2, rms, sse, aic, bic, dof);
		return uncertainties;
	}
}