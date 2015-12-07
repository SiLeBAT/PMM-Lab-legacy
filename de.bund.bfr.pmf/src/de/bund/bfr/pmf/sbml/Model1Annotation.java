package de.bund.bfr.pmf.sbml;

import java.util.LinkedList;
import java.util.List;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Primary model annotation. Holds model id, model title, uncertainties,
 * references, and condId.
 * 
 * @author Miguel de Alba
 */
public class Model1Annotation {
	
	private static final String METADATA_NS = "pmf";
	private static final String METADATA_TAG = "metadata";

	Uncertainties uncertainties;
	List<Reference> refs;
	int condID;
	Annotation annotation;

	/**
	 * Gets fields from existing primary model annotation.
	 */
	public Model1Annotation(Annotation annotation) {

		this.annotation = annotation;

		XMLNode metadataNode = annotation.getNonRDFannotation().getChildElement(METADATA_TAG, "");

		// Gets condID
		condID = new CondIdNode(metadataNode.getChildElement(CondIdNode.TAG, "")).getCondId();

		// Gets model quality annotation
		XMLNode modelQualityNode = metadataNode.getChildElement(UncertaintyNode.TAG, "");
		if (modelQualityNode != null) {
			uncertainties = new UncertaintyNode(modelQualityNode).getMeasures();
		}

		// Gets references
		refs = new LinkedList<>();
		for (XMLNode refNode : metadataNode.getChildElements(ReferenceSBMLNode.TAG, "")) {
			refs.add(new ReferenceSBMLNode(refNode).toReference());
		}
	}

	public Model1Annotation(Uncertainties uncertainties, List<Reference> references, int condID) {

		// Builds metadata node
		XMLTriple metadataTriple = new XMLTriple(METADATA_TAG, "", METADATA_NS);
		XMLNode metadataNode = new XMLNode(metadataTriple);

		// Builds uncertainties node
		metadataNode.addChild(new UncertaintyNode(uncertainties).getNode());

		// Builds reference nodes
		for (Reference reference : references) {
			metadataNode.addChild(new ReferenceSBMLNode(reference).node);
		}

		// Builds condID node
		metadataNode.addChild(new CondIdNode(condID).node);

		// Saves fields
		this.uncertainties = uncertainties;
		this.refs = references;
		this.condID = condID;

		this.annotation = new Annotation();
		this.annotation.setNonRDFAnnotation(metadataNode);
	}

	public Uncertainties getUncertainties() { return uncertainties; }
	public List<Reference> getReferences() { return refs; }
	public int getCondID() { return condID; }
	public Annotation getAnnotation() { return annotation; }
}
