/**
 * Secondary model annotation. Holds its global model ID and references.
 * @author Miguel Alba (malba@optimumquality.es)
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.LinkedList;
import java.util.List;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.annotation.GlobalModelIdNode;
import de.bund.bfr.knime.pmm.annotation.SBMLReferenceNode;
import de.bund.bfr.knime.pmm.annotation.UncertaintyNode;
import de.bund.bfr.knime.pmm.common.LiteratureItem;

/**
 * Secondary model annotation. Holds global model ID, references and uncertainty
 * measures.
 * 
 * @author Miguel Alba
 */
public class Model2Annotation {

	static final String METADATA_TAG = "metadata";

	static final String PMF_TAG = "pmf";
	static final String REFERENCE_TAG = "reference";

	XMLNode node;
	List<LiteratureItem> literatureItems;
	int globalModelID;
	Uncertainties uncertainties;

	/**
	 * Gets literature items and globalModelID from existing Model2Annotation
	 */
	public Model2Annotation(XMLNode node) {
		this.node = node;
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");

		// Gets globalModelID
		XMLNode globalModelIDNode = metadata.getChildElement(
				GlobalModelIdNode.TAG, "");
		globalModelID = Integer.parseInt(globalModelIDNode.getChild(0)
				.getCharacters());

		// Gets model quality annotation
		XMLNode qualityNode = metadata.getChildElement(UncertaintyNode.TAG, "");
		if (qualityNode != null) {
			uncertainties = new UncertaintyNode(qualityNode).getMeasures();
		}

		// Gets references
		literatureItems = new LinkedList<>();
		for (XMLNode ref : metadata.getChildElements(REFERENCE_TAG, "")) {
			SBMLReferenceNode refNode = new SBMLReferenceNode(ref);
			literatureItems.add(refNode.toLiteratureItem());
		}
	}

	// Builds new coefficient annotation for globalModelID and references
	public Model2Annotation(int globalModelID,
			Uncertainties uncertainties, List<LiteratureItem> lits) {
		// Builds metadata node
		node = new XMLNode(new XMLTriple(METADATA_TAG, null, PMF_TAG));

		// Builds globalModelID node
		node.addChild(new GlobalModelIdNode(globalModelID).getNode());

		// Builds uncertainties node
		node.addChild(new UncertaintyNode(uncertainties).getNode());

		// Builds references node
		for (LiteratureItem lit : lits) {
			SBMLReferenceNode ref = new SBMLReferenceNode(lit);
			node.addChild(ref.getNode());
		}

		// Saves fields
		this.globalModelID = globalModelID;
		this.literatureItems = lits;
		this.uncertainties = uncertainties;
	}

	// Getters
	public XMLNode getNode() {
		return node;
	}

	public int getGlobalModelID() {
		return globalModelID;
	}

	public List<LiteratureItem> getLiteratureItems() {
		return literatureItems;
	}

	public Uncertainties getUncertainties() {
		return uncertainties;
	}
}