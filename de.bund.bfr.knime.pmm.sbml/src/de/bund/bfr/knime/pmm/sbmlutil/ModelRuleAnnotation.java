package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

// Model rule annotation. Holds formula name, subject, and its PmmLab ID
public class ModelRuleAnnotation {

	XMLNode node;
	String formulaName;
	String subject;
	int pmmlabID;

	// Get formula name, subject, and PmmLab id from existing rule annotation
	public ModelRuleAnnotation(XMLNode node) {
		this.node = node;
		XMLNode metadata = node.getChildElement("metadata", "");

		// Get formula name
		XMLNode nameNode = metadata.getChildElement("formulaName", "");
		formulaName = nameNode.getChild(0).getCharacters();

		// Get formula subject
		XMLNode subjectNode = metadata.getChildElement("subject", "");
		if (subjectNode == null) {
			subject = "unknown";
		} else {
			subject = subjectNode.getChild(0).getCharacters();
		}

		// Get PmmLab ID
		XMLNode idNode = metadata.getChildElement("pmmlabID", "");
		if (idNode == null) {
			pmmlabID = -1;
		} else {
			pmmlabID = Integer.parseInt(idNode.getChild(0).getCharacters());
		}
	}

	// Build new model rule annotation for formulaName, subject, and pmmlabID
	public ModelRuleAnnotation(String formulaName, String subject, int pmmlabID) {
		// Build metadata node
		node = new XMLNode(new XMLTriple("metadata", null, "pmf"));

		// Create annotation for formulaName
		XMLNode nameNode = new XMLNode(new XMLTriple("formulaName", null,
				"pmmlab"));
		nameNode.addChild(new XMLNode(formulaName));
		node.addChild(nameNode);

		// Create annotation for subject
		XMLNode subjectNode = new XMLNode(new XMLTriple("subject", null,
				"pmmlab"));
		subjectNode.addChild(new XMLNode(subject));
		node.addChild(subjectNode);

		// Create annotation for pmmlabID
		XMLNode idNode = new XMLNode(new XMLTriple("pmmlabID", null, "pmmlab"));
		idNode.addChild(new XMLNode(new Integer(pmmlabID).toString()));
		node.addChild(idNode);

		// Save formulaName, subject, and pmmlabID
		this.formulaName = formulaName;
		this.subject = subject;
		this.pmmlabID = pmmlabID;
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
}