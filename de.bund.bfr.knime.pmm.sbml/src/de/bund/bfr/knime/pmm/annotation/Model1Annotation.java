package de.bund.bfr.knime.pmm.annotation;

import java.util.LinkedList;
import java.util.List;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.sbmlutil.Uncertainties;

/**
 * Primary model annotation. Holds model id, model title, uncertainties,
 * references, and condId.
 * 
 * @author Miguel de Alba
 */
public class Model1Annotation {

	static final String METADATA_TAG = "metadata";
	static final String CONDID_TAG = "condID";
	static final String REFERENCE_TAG = "reference";

	Annotation annotation;
	Uncertainties uncertainties;
	List<LiteratureItem> lits;
	int condID;

	/**
	 * Gets fields from existing primary model annotation.
	 */
	public Model1Annotation(Annotation annotation) {

		this.annotation = annotation;

		XMLNode metadataNode = annotation.getNonRDFannotation().getChildElement(METADATA_TAG, "");

		// Gets condID
		XMLNode condIDNode = metadataNode.getChildElement(CONDID_TAG, "");
		condID = Integer.parseInt(condIDNode.getChild(0).getCharacters());

		// Gets model quality annotation
		XMLNode modelQualityNode = metadataNode.getChildElement(UncertaintyNode.TAG, "");
		if (modelQualityNode != null) {
			uncertainties = new UncertaintyNode(modelQualityNode).getMeasures();
		}

		// Gets references
		lits = new LinkedList<>();
		for (XMLNode refNode : metadataNode.getChildElements(REFERENCE_TAG, "")) {
			lits.add(new SBMLReferenceNode(refNode).toLiteratureItem());
		}
	}

	public Model1Annotation(Uncertainties uncertainties, List<LiteratureItem> literatureItems, int condID) {

		// Builds metadata node
		XMLNode metadataNode = new XMLNode(new XMLTriple(METADATA_TAG, null, "pmf"));

		// Builds uncertainties node
		metadataNode.addChild(new UncertaintyNode(uncertainties).getNode());

		// Builds reference nodes
		for (LiteratureItem lit : literatureItems) {
			metadataNode.addChild(new SBMLReferenceNode(lit).getNode());
		}

		// Builds condID node
		metadataNode.addChild(new CondIDNode(condID).getNode());

		// Saves fields
		this.uncertainties = uncertainties;
		this.lits = literatureItems;
		this.condID = condID;

		this.annotation = new Annotation();
		this.annotation.setNonRDFAnnotation(metadataNode);
	}

	// Getters
	public Annotation getAnnotation() {
		return annotation;
	}

	public Uncertainties getUncertainties() {
		return uncertainties;
	}

	public List<LiteratureItem> getLits() {
		return lits;
	}

	public int getCondID() {
		return condID;
	}
}
