package de.bund.bfr.knime.pmm.annotation.sbml;

import java.util.LinkedList;
import java.util.List;

import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.sbmlutil.Uncertainties;

/**
 * Secondary model annotation. Holds global model ID, references and uncertainty
 * measures.
 * 
 * @author Miguel de Alba
 */
public class Model2Annotation extends AnnotationBase {

	List<LiteratureItem> literatureItems;
	int globalModelID;
	Uncertainties uncertainties;

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
		literatureItems = new LinkedList<>();
		for (XMLNode ref : metadata.getChildElements(ReferenceSBMLNode.TAG, "")) {
			ReferenceSBMLNode refNode = new ReferenceSBMLNode(ref);
			literatureItems.add(refNode.toLiteratureItem());
		}
	}

	/**
	 * Builds new coefficient annotation for global model id, uncertainties and
	 * references.
	 */
	public Model2Annotation(int globalModelID, Uncertainties uncertainties, List<LiteratureItem> lits) {
		// Builds metadata node
		XMLTriple metadataTriple = new XMLTriple(METADATA_TAG, "", METADATA_NS);
		XMLNode metadataNode = new XMLNode(metadataTriple);

		// Builds globalModelID node
		metadataNode.addChild(new GlobalModelIdNode(globalModelID).getNode());

		// Builds uncertainties node
		metadataNode.addChild(new UncertaintyNode(uncertainties).getNode());

		// Builds references node
		for (LiteratureItem lit : lits) {
			ReferenceSBMLNode ref = new ReferenceSBMLNode(lit);
			metadataNode.addChild(ref.getNode());
		}

		// Saves fields
		this.globalModelID = globalModelID;
		this.literatureItems = lits;
		this.uncertainties = uncertainties;
		this.annotation = new Annotation();
		this.annotation.setNonRDFAnnotation(metadataNode);
	}

	// Getters
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
