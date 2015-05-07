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

import de.bund.bfr.knime.pmm.annotation.ReferenceNode;
import de.bund.bfr.knime.pmm.annotation.UncertaintyNode;
import de.bund.bfr.knime.pmm.common.LiteratureItem;

public class Model1Annotation {

	private static final String METADATA_TAG = "metadata";
	private static final String PMF_TAG = "pmf";
	private static final String MODEL_ID_TAG = "identifier";
	private static final String COMBASE_ID_TAG = "combaseID";
	private static final String COND_ID_TAG = "condID";
	private static final String MODEL_TITLE_TAG = "modelTitle";
	private static final String MODEL_QUALITY_TAG = "modelquality";
	private static final String REFERENCE_TAG = "reference";

	private XMLNode node;
	private String modelID;
	private String modelTitle;
	private Map<String, String> uncertainties;
	private List<LiteratureItem> lits;
	private String combaseID;
	private int condID;

	// Get fields from existing prim model annotation
	public Model1Annotation(XMLNode node) {
		this.node = node;
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");

		// Get modelID
		XMLNode modelIDNode = metadata.getChildElement(MODEL_ID_TAG, "");
		if (modelIDNode != null) {
			modelID = modelIDNode.getChild(0).getCharacters();
		}

		// Get combaseID
		XMLNode combaseIDNode = metadata.getChildElement(COMBASE_ID_TAG, "");
		if (combaseIDNode != null) {
			combaseID = combaseIDNode.getChild(0).getCharacters();
		}

		// Get condID
		XMLNode condIDNode = metadata.getChildElement(COND_ID_TAG, "");
		condID = Integer.parseInt(condIDNode.getChild(0).getCharacters());

		// Get modelTitle
		XMLNode modelTitleNode = metadata.getChildElement(MODEL_TITLE_TAG, "");
		if (modelTitleNode != null) {
			modelTitle = modelTitleNode.getChild(0).getCharacters();
		}

		// Get model quality annotation
		XMLNode modelQualityNode = metadata.getChildElement(MODEL_QUALITY_TAG,
				"");
		if (modelQualityNode != null) {
			uncertainties = new UncertaintyNode(modelQualityNode).getMeasures();
		}

		// Get references
		lits = new LinkedList<>();
		for (XMLNode refNode : metadata.getChildElements(REFERENCE_TAG, "")) {
			lits.add(new ReferenceNode(refNode).toLiteratureItem());
		}
	}

	public Model1Annotation(String modelID, String modelTitle,
			Map<String, String> uncertainties,
			List<LiteratureItem> literatureItems, String combaseID, int condID) {

		// Build metadata node
		node = new XMLNode(new XMLTriple(METADATA_TAG, null, PMF_TAG));

		// Build modelID node
		XMLTriple modelIDTriple = new XMLTriple(MODEL_ID_TAG, null, PMF_TAG);
		XMLNode modelIDNode = new XMLNode(modelIDTriple);
		modelIDNode.addChild(new XMLNode(modelID));
		node.addChild(modelIDNode);

		// Build modelTitle node
		XMLTriple modelTitleTriple = new XMLTriple(MODEL_TITLE_TAG, null,
				PMF_TAG);
		XMLNode modelTitleNode = new XMLNode(modelTitleTriple);
		modelTitleNode.addChild(new XMLNode(modelTitle));
		node.addChild(modelTitleNode);

		// Build uncertainties node
		if (!uncertainties.isEmpty()) {
			node.addChild(new UncertaintyNode(uncertainties).getNode());
		}

		// Build reference nodes
		for (LiteratureItem lit : literatureItems) {
			node.addChild(new ReferenceNode(lit).getNode());
		}

		// Build combaseID node
		if (combaseID != null) {
			XMLTriple combaseIDTriple = new XMLTriple(COMBASE_ID_TAG, null,
					PMF_TAG);
			XMLNode combaseIDNode = new XMLNode(combaseIDTriple);
			combaseIDNode.addChild(new XMLNode(combaseID));
			node.addChild(combaseIDNode);
		}

		// Build condID node
		XMLTriple condIDTriple = new XMLTriple(COND_ID_TAG, null, PMF_TAG);
		XMLNode condIDNode = new XMLNode(condIDTriple);
		condIDNode.addChild(new XMLNode(new Integer(condID).toString()));
		node.addChild(condIDNode);

		// Save fields
		this.modelID = modelID;
		this.modelTitle = modelTitle;
		this.uncertainties = uncertainties;
		this.lits = literatureItems;
		this.combaseID = combaseID;
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

	public String getCombaseID() {
		return combaseID;
	}

	public int getCondID() {
		return condID;
	}
}
