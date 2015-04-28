/**
 * Pmm Lab agent.
 * 
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import groovy.util.Node;

import org.hsh.bfr.db.DBKernel;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.AgentXml;

/** Wrapper class for a Pmm Lab organism that holds a SBML species. */
public class Agent {
	private Species species;

	private String casNumber;

	public Agent(Species species) {
		// Get CAS number from annotation
		XMLNode nonRDFannotation = species.getAnnotation()
				.getNonRDFannotation();
		casNumber = new AgentAnnotation(nonRDFannotation).getRef();
		this.species = species;
	}

	public Agent(AgentXml agent, String unit) {
		species = new Species("species" + agent.getId());
		species.setBoundaryCondition(false);
		species.setConstant(false);
		species.setUnits(Util.createId(unit));

		// Search species in DB
		String name = (String) DBKernel.getValue("Agenzien", "ID", agent
				.getId().toString(), "Agensname");

		// If found
		if (name != null) {
			species.setName(name);

			// Get CAS number from DB
			casNumber = (String) DBKernel.getValue("Agenzien", "ID", agent
					.getId().toString(), "CAS_Nummer");

			// Create and set a non RDF annotation with the CAS number
			XMLNode pmfNode = new AgentAnnotation(casNumber).getNode();
			species.getAnnotation().setNonRDFAnnotation(pmfNode);
		}
	}

	// Getters
	public Species getSpecies() {
		return species;
	}

	public String getCasNumber() {
		return casNumber;
	}

	public AgentXml toAgentXml() {
		AgentXml agent = new AgentXml();

		if (casNumber != null) {
			// Get agent id
			int id = (int) DBKernel.getValue("Agenzien", "CAS_Nummer",
					casNumber, "ID");
			agent.setId(id);

			// Get agent name
			String name = (String) DBKernel.getValue("Agenzien", "CAS_Nummer",
					casNumber, "Agensname");
			agent.setName(name);

			// Get dbuuid
			String dbuuid = DBKernel.getLocalDBUUID();
			agent.setDbuuid(dbuuid);

		} else {
			agent.setName(species.getName());
		}

		return agent;
	}

	public Node toGroovyNode() {
		return new Node(null, "sbml:species", species.writeXMLAttributes());
	}
}

// Agent non RDF annotation. Holds an agent's CAS number
class AgentAnnotation {
	private static String METADATA_TAG = "metadata";
	private static String PMF_TAG = "pmf";
	private static String REF_TAG = "source";
	private static String REF_NS = "dc";

	private XMLNode node;
	private String ref; // CAS number

	// Get CAS number from an existing agent annotation
	public AgentAnnotation(XMLNode node) {
		this.node = node;
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");

		if (metadata != null) {
			XMLNode source = metadata.getChildElement(REF_TAG, "");
			ref = source.getChild(0).getCharacters(); // whole reference
			ref = ref.substring(ref.lastIndexOf("/") + 1);
		}
	}

	/**
	 * Build new Agent annotation for a CAS number
	 * 
	 * @param code
	 *            : CAS number
	 */
	public AgentAnnotation(String code) {
		// Build reference tag
		XMLNode refNode = new XMLNode(new XMLTriple(REF_TAG, null, REF_NS));
		refNode.addChild(new XMLNode("http://identifiers.org/ncim/" + code));

		// Build PMF container
		node = new XMLNode(new XMLTriple(METADATA_TAG, null, PMF_TAG));
		node.addChild(refNode);
	}

	// Getters
	public XMLNode getNode() {
		return node;
	}

	public String getRef() {
		return ref;
	}
}