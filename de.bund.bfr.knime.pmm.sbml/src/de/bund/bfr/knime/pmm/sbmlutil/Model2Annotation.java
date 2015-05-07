/**
 * Secondary model annotation. Holds its global model ID and references.
 * @author Miguel Alba (malba@optimumquality.es)
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.LinkedList;
import java.util.List;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.annotation.ReferenceNode;
import de.bund.bfr.knime.pmm.common.LiteratureItem;

public class Model2Annotation {

	private static final String METADATA_TAG = "metadata";
	private static final String PMF_TAG = "pmf";
	private static final String GLOBAL_MODEL_ID_TAG = "globalModelID";
	private static final String REFERENCE_TAG = "reference";

	private XMLNode node;
	private List<LiteratureItem> literatureItems;
	private int globalModelID;

	// Get literature items and globalModelID from existing sec model annotation
	public Model2Annotation(XMLNode node) {
		this.node = node;
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");

		// Get globalModelID
		XMLNode globalModelIDNode = metadata.getChildElement(
				GLOBAL_MODEL_ID_TAG, "");
		globalModelID = Integer.parseInt(globalModelIDNode.getChild(0)
				.getCharacters());

		// Get references
		literatureItems = new LinkedList<>();
		for (XMLNode ref : metadata.getChildElements(REFERENCE_TAG, "")) {
			ReferenceNode refNode = new ReferenceNode(ref);
			literatureItems.add(refNode.toLiteratureItem());
		}
	}

	// Build new coefficient annotation for globalModelID and references
	public Model2Annotation(int globalModelID, List<LiteratureItem> lits) {
		// Build metadata node
		node = new XMLNode(new XMLTriple(METADATA_TAG, null, PMF_TAG));
		
		// Build globalModelID node
		XMLTriple globalModelIDTriple = new XMLTriple(GLOBAL_MODEL_ID_TAG, "", PMF_TAG);
		XMLNode globalModelIDNode = new XMLNode(globalModelIDTriple);
		globalModelIDNode.addChild(new XMLNode(new Integer(globalModelID).toString()));
		node.addChild(globalModelIDNode);
		
		// Build references node
		for (LiteratureItem lit : lits) {
			ReferenceNode ref = new ReferenceNode(lit);
			node.addChild(ref.getNode());
		}
		
		// Save globalModelID and references
		this.globalModelID = globalModelID;
		this.literatureItems = lits;
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
}