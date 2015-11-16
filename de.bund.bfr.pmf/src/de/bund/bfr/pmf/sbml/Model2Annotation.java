package de.bund.bfr.pmf.sbml;

import java.util.List;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

/**
 * Secondary model annotation. Holds global model ID, references and uncertainty
 * measures.
 * 
 * @author Miguel de Alba
 */
public class Model2Annotation {
	
	private static final String METADATA_NS = "pmf";
	private static final String METADATA_TAG = "metadata";

	Reference[] references;
	int globalModelID;
	Uncertainties uncertainties;
	Annotation annotation;

	/**
	 * Gets global model id, uncertainties and literature items of the model.
	 */
	public Model2Annotation(Annotation annotation) {
		this.annotation = annotation;

		XMLNode metadata = annotation.getNonRDFannotation().getChildElement(METADATA_TAG, "");

		// Gets globalModelID
		globalModelID = new GlobalModelIdNode(metadata.getChildElement(GlobalModelIdNode.TAG, "")).getGlobalModelId();

		// Gets model quality annotation
		XMLNode qualityNode = metadata.getChildElement(UncertaintyNode.TAG, "");
		if (qualityNode != null) {
			uncertainties = new UncertaintyNode(qualityNode).getMeasures();
		}

		// Gets references
		List<XMLNode> refNodes = metadata.getChildElements(ReferenceSBMLNode.TAG, "");
		references = new ReferenceImpl[refNodes.size()];
		for (int i = 0; i < refNodes.size(); i++) {
			references[i] = new ReferenceSBMLNode(refNodes.get(i)).toReference();
		}
	}

	/**
	 * Builds new coefficient annotation for global model id, uncertainties and
	 * references.
	 */
	public Model2Annotation(int globalModelID, Uncertainties uncertainties, Reference[] references) {
		// Builds metadata node
		XMLTriple metadataTriple = new XMLTriple(METADATA_TAG, "", METADATA_NS);
		XMLNode metadataNode = new XMLNode(metadataTriple);

		// Builds globalModelID node
		metadataNode.addChild(new GlobalModelIdNode(globalModelID).node);

		// Builds uncertainties node
		metadataNode.addChild(new UncertaintyNode(uncertainties).getNode());

		// Builds references node
		for (Reference reference : references) {
			metadataNode.addChild(new ReferenceSBMLNode(reference).node);
		}

		// Saves fields
		this.globalModelID = globalModelID;
		this.references = references;
		this.uncertainties = uncertainties;
		this.annotation = new Annotation();
		this.annotation.setNonRDFAnnotation(metadataNode);
	}

	// Getters
	public int getGlobalModelID() { return globalModelID; }
	public Reference[] getReferences() { return references; }
	public Uncertainties getUncertainties() { return uncertainties; }
	public Annotation getAnnotation() { return annotation; }
}
