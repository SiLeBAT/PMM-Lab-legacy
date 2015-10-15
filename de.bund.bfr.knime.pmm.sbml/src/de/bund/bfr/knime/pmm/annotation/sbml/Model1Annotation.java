package de.bund.bfr.knime.pmm.annotation.sbml;

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
public class Model1Annotation extends AnnotationBase {

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
		condID = new CondIDNode(metadataNode.getChildElement(CondIDNode.TAG, "")).getCondId();

		// Gets model quality annotation
		XMLNode modelQualityNode = metadataNode.getChildElement(UncertaintyNode.TAG, "");
		if (modelQualityNode != null) {
			uncertainties = new UncertaintyNode(modelQualityNode).getMeasures();
		}

		// Gets references
		lits = new LinkedList<>();
		for (XMLNode refNode : metadataNode.getChildElements(ReferenceSBMLNode.TAG, "")) {
			lits.add(new ReferenceSBMLNode(refNode).toLiteratureItem());
		}
	}

	public Model1Annotation(Uncertainties uncertainties, List<LiteratureItem> literatureItems, int condID) {

		// Builds metadata node
		XMLTriple metadataTriple = new XMLTriple(METADATA_TAG, "", METADATA_NS);
		XMLNode metadataNode = new XMLNode(metadataTriple);

		// Builds uncertainties node
		metadataNode.addChild(new UncertaintyNode(uncertainties).getNode());

		// Builds reference nodes
		for (LiteratureItem lit : literatureItems) {
			metadataNode.addChild(new ReferenceSBMLNode(lit).getNode());
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
