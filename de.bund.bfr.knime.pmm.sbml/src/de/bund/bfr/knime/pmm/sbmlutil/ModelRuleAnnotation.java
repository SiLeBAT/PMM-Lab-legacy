package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

// Model rule annotation. Holds formula name, subject, and its PmmLab ID
public class ModelRuleAnnotation {

	private static final String METADATA_TAG = "metadata";
	private static final String PMF_TAG = "pmf";
	private static final String NAME_TAG = "formulaName";
	private static final String SUBJECT_TAG = "subject";
	private static final String ID_TAG = "pmmlabID";

	private XMLNode node;
	private String formulaName;
	private String subject;
	private int pmmlabID;

	// Get formula name, subject, and PmmLab id from existing rule annotation
	public ModelRuleAnnotation(XMLNode node) {
		this.node = node;
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");

		// Get formula name
		XMLNode nameNode = metadata.getChildElement(NAME_TAG, "");
		formulaName = nameNode.getChild(0).getCharacters();

		// Get formula subject
		XMLNode subjectNode = metadata.getChildElement(SUBJECT_TAG, "");
		if (subjectNode == null) {
			subject = "unknown";
		} else {
			subject = subjectNode.getChild(0).getCharacters();
		}

		// Get PmmLab ID
		XMLNode idNode = metadata.getChildElement(ID_TAG, "");
		if (idNode == null) {
			pmmlabID = -1;
		} else {
			pmmlabID = Integer.parseInt(idNode.getChild(0).getCharacters());
		}
	}

	// Build new model rule annotation for formulaName, subject, and pmmlabID
	public ModelRuleAnnotation(String formulaName, String subject, int pmmlabID) {
		// Build metadata node
		node = new XMLNode(new XMLTriple(METADATA_TAG, null, PMF_TAG));

		// Create annotation for formulaName
		XMLNode nameNode = new XMLNode(new XMLTriple(NAME_TAG, null, PMF_TAG));
		nameNode.addChild(new XMLNode(formulaName));
		node.addChild(nameNode);

		// Create annotation for subject
		XMLNode subjectNode = new XMLNode(new XMLTriple(SUBJECT_TAG, null,
				PMF_TAG));
		subjectNode.addChild(new XMLNode(subject));
		node.addChild(subjectNode);

		// Create annotation for pmmlabID
		XMLNode idNode = new XMLNode(new XMLTriple(ID_TAG, null, PMF_TAG));
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
