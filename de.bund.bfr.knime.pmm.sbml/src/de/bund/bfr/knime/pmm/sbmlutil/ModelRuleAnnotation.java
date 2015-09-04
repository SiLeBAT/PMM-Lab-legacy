package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.LinkedList;
import java.util.List;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.annotation.SBMLReferenceNode;
import de.bund.bfr.knime.pmm.common.LiteratureItem;

// Model rule annotation. Holds formula name, subject, and its PmmLab ID
public class ModelRuleAnnotation {

	XMLNode node;
	String formulaName;
	String subject;
	int pmmlabID;
	List<LiteratureItem> lits;

	static final String FORMULA_TAG = "formulaName";
	static final String SUBJECT_TAG = "subject";
	static final String REFERENCE_TAG = "reference";
	static final String PmmLabID_TAG = "pmmlabID";

	// Get formula name, subject, and PmmLab id from existing rule annotation
	public ModelRuleAnnotation(XMLNode node) {
		this.node = node;
		XMLNode metadata = node.getChildElement("metadata", "");

		// Get formula name
		XMLNode nameNode = metadata.getChildElement(FORMULA_TAG, "");
		formulaName = nameNode.getChild(0).getCharacters();

		// Get formula subject
		XMLNode subjectNode = metadata.getChildElement(SUBJECT_TAG, "");
		if (subjectNode == null) {
			subject = "unknown";
		} else {
			subject = subjectNode.getChild(0).getCharacters();
		}

		// Get PmmLab ID
		XMLNode idNode = metadata.getChildElement(PmmLabID_TAG, "");
		if (idNode == null) {
			pmmlabID = -1;
		} else {
			pmmlabID = Integer.parseInt(idNode.getChild(0).getCharacters());
		}

		// Gets references
		lits = new LinkedList<>();
		for (XMLNode refNode : metadata.getChildElements(REFERENCE_TAG, "")) {
			lits.add(new SBMLReferenceNode(refNode).toLiteratureItem());
		}
	}

	// Build new model rule annotation for formulaName, subject, and pmmlabID
	public ModelRuleAnnotation(String formulaName, String subject, int pmmlabID, List<LiteratureItem> lits) {
		// Build metadata node
		node = new XMLNode(new XMLTriple("metadata", null, "pmf"));

		// Create annotation for formulaName
		XMLNode nameNode = new XMLNode(new XMLTriple(FORMULA_TAG, null, "pmmlab"));
		nameNode.addChild(new XMLNode(formulaName));
		node.addChild(nameNode);

		// Create annotation for subject
		XMLNode subjectNode = new XMLNode(new XMLTriple(SUBJECT_TAG, null, "pmmlab"));
		subjectNode.addChild(new XMLNode(subject));
		node.addChild(subjectNode);

		// Create annotation for pmmlabID
		XMLNode idNode = new XMLNode(new XMLTriple(PmmLabID_TAG, null, "pmmlab"));
		idNode.addChild(new XMLNode(new Integer(pmmlabID).toString()));
		node.addChild(idNode);

		// Builds reference nodes
		for (LiteratureItem lit : lits) {
			node.addChild(new SBMLReferenceNode(lit).getNode());
		}

		// Save formulaName, subject, pmmlabID and model literature
		this.formulaName = formulaName;
		this.subject = subject;
		this.pmmlabID = pmmlabID;
		this.lits = lits;
	}

	// Getters
	public XMLNode getNode() {
		return node;
	}

	public String getName() {
		return formulaName;
	}

	public String getSubject() {
		return subject;
	}

	public int getID() {
		return pmmlabID;
	}
	
	public List<LiteratureItem> getLits() {
		return lits;
	}
}