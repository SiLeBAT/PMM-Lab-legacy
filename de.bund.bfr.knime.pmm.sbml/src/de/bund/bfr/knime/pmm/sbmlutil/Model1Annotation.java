/**
 * Primary model annotation. Holds model id, model title, uncertainties,
 * references, combase ID, and cond ID.
 * @author Miguel Alba (malba@optimumquality.es)
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.annotation.SBMLReferenceNode;
import de.bund.bfr.knime.pmm.annotation.UncertaintyNode;
import de.bund.bfr.knime.pmm.common.LiteratureItem;

/**
 * Primary model annotation. Holds model id, model title, uncertainties,
 * references, and cond ID.
 * 
 * @author Miguel Alba (malba@optimumquality.es)
 */
public class Model1Annotation {

	static final String METADATA_TAG = "metadata";

	static final String PMF_TAG = "pmf";
	static final String MODEL_ID_TAG = "identifier";
	static final String COND_ID_TAG = "condID";
	static final String MODEL_QUALITY_TAG = "modelquality";
	static final String REFERENCE_TAG = "reference";

	XMLNode node;
	String modelID;
	String modelTitle;
	Map<String, String> uncertainties;
	List<LiteratureItem> lits;
	int condID;

	/** Get fields from existing prim model annotation */
	public Model1Annotation(XMLNode node) {
		this.node = node;
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");

		// Gets modelID
		XMLNode modelIDNode = metadata.getChildElement(MODEL_ID_TAG, "");
		if (modelIDNode != null) {
			modelID = modelIDNode.getChild(0).getCharacters();
		}

		// Gets condID
		XMLNode condIDNode = metadata.getChildElement(COND_ID_TAG, "");
		condID = Integer.parseInt(condIDNode.getChild(0).getCharacters());

		// Gets model quality annotation
		XMLNode modelQualityNode = metadata.getChildElement(MODEL_QUALITY_TAG,
				"");
		if (modelQualityNode != null) {
			uncertainties = new UncertaintyNode(modelQualityNode).getMeasures();
		}

		// Gets references
		lits = new LinkedList<>();
		for (XMLNode refNode : metadata.getChildElements(REFERENCE_TAG, "")) {
			lits.add(new SBMLReferenceNode(refNode).toLiteratureItem());
		}
	}

	public Model1Annotation(String modelID, String modelTitle,
			Map<String, String> uncertainties,
			List<LiteratureItem> literatureItems, int condID) {

		// Builds metadata node
		node = new XMLNode(new XMLTriple(METADATA_TAG, null, PMF_TAG));

		// Builds modelID node
		XMLTriple modelIDTriple = new XMLTriple(MODEL_ID_TAG, null, PMF_TAG);
		XMLNode modelIDNode = new XMLNode(modelIDTriple);
		modelIDNode.addChild(new XMLNode(modelID));
		node.addChild(modelIDNode);

		// Builds uncertainties node
		if (!uncertainties.isEmpty()) {
			node.addChild(new UncertaintyNode(uncertainties).getNode());
		}

		// Builds reference nodes
		for (LiteratureItem lit : literatureItems) {
			node.addChild(new SBMLReferenceNode(lit).getNode());
		}

		// Builds condID node
		XMLTriple condIDTriple = new XMLTriple(COND_ID_TAG, null, PMF_TAG);
		XMLNode condIDNode = new XMLNode(condIDTriple);
		condIDNode.addChild(new XMLNode(new Integer(condID).toString()));
		node.addChild(condIDNode);

		// Saves fields
		this.modelID = modelID;
		this.modelTitle = modelTitle;
		this.uncertainties = uncertainties;
		this.lits = literatureItems;
		this.condID = condID;
	}

	// Getters
	public XMLNode getNode() {
		return node;
	}

	public String getModelID() {
		return modelID;
	}

	public String getModelTitle() {
		return modelTitle;
	}

	public Map<String, String> getUncertainties() {
		return uncertainties;
	}

	public List<LiteratureItem> getLits() {
		return lits;
	}

	public int getCondID() {
		return condID;
	}
}