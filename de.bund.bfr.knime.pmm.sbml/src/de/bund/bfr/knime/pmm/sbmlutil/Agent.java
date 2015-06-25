/**
 * Pmm Lab agent.
 * 
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import groovy.util.Node;

import org.hsh.bfr.db.DBKernel;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.AgentXml;

/** Wrapper class for a Pmm Lab organism that holds a SBML species. */
public class Agent {

	Species species; // SBML species
	String casNumber; // Agent CAS number
	String detail; // Agent description

	/**
	 * Creates and Agent from existing SBML species.
	 * 
	 * @param species
	 *            SBML species with an AgentAnnotation.
	 */
	public Agent(Species species) {
		// Get CAS number from annotation
		XMLNode nonRDFannotation = species.getAnnotation()
				.getNonRDFannotation();
		AgentAnnotation agentAnnotation = new AgentAnnotation(nonRDFannotation);
		casNumber = agentAnnotation.getRef();
		detail = agentAnnotation.getDetail();

		// Copy reference to SBML species
		this.species = species;
	}

	/**
	 * Creates an Agent from existing AgentXml and unit name.
	 * 
	 * @param agent
	 *            Pmm Lab AgentXml.
	 * @param unit
	 *            Unit name (as displayed in GUI).
	 */
	public Agent(AgentXml agent, String unit, Compartment compartment) {

		// Create SBML species with id prefixed by "species"
		species = new Species(Util.createId("species" + agent.getId()), 3, 1);
		species.setBoundaryCondition(false);
		species.setConstant(false);
		species.setUnits(Util.createId(unit));
		species.setCompartment(compartment);
		species.setHasOnlySubstanceUnits(false);

		// Search species in DB
		String name = (String) DBKernel.getValue("Agenzien", "ID", agent
				.getId().toString(), "Agensname");

		// If found, try to get CAS number from DB
		if (name != null) {
			species.setName(name);

			// Get CAS number from DB
			casNumber = (String) DBKernel.getValue("Agenzien", "ID", agent
					.getId().toString(), "CAS_Nummer");
		}

		detail = agent.getDetail();

		// Build and set non RDF annotation
		XMLNode annot = new AgentAnnotation(casNumber, detail).getNode();
		species.getAnnotation().setNonRDFAnnotation(annot);
	}

	/**
	 * Creates a Pmm Lab AgentXml.
	 */
	public AgentXml toAgentXml() {
		AgentXml agent = new AgentXml();

		// If casNumber is not null, then gets ID, name, and dbuuid from DB
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
		}
		// Else, use the SBML species' name
		else {
			agent.setName(species.getName());
		}

		agent.setDetail(detail);

		return agent;
	}

	// Getters
	public Species getSpecies() {
		return species;
	}

	public String getCasNumber() {
		return casNumber;
	}

	public String getDetail() {
		return detail;
	}

	public Node toGroovyNode() {
		return new Node(null, "sbml:species", species.writeXMLAttributes());
	}
}

/** Agent non RDF annotation. Holds an agent's CAS number */
class AgentAnnotation {

	static String METADATA_TAG = "metadata";
	static String PMF_TAG = "pmf";
	static String REF_TAG = "source"; // reference tag
	static String REF_NS = "dc"; // reference namespace
	static String DETAIL_TAG = "detail"; // description tag
	static String DETAIL_NS = "pmf"; // description namespace

	XMLNode node;
	String ref; // CAS number
	String detail; // Agent detail

	/**
	 * Builds an AgentAnnotation from existing XMLNode, parsing its CAS number
	 * and description.
	 * 
	 * @param node
	 *            XMLNode.
	 */
	public AgentAnnotation(XMLNode node) {
		this.node = node; // copies XMLNode

		// Parses annotation
		XMLNode metadata = node.getChildElement(METADATA_TAG, "");
		if (metadata != null) {

			// Gets CAS number
			XMLNode sourceNode = metadata.getChildElement(REF_TAG, "");
			if (sourceNode != null) {
				ref = sourceNode.getChild(0).getCharacters(); // whole reference
				ref = ref.substring(ref.lastIndexOf("/") + 1);
			}

			// Gets description
			XMLNode detailNode = metadata.getChildElement(DETAIL_TAG, "");
			if (detailNode != null) {
				detail = detailNode.getChild(0).getCharacters();
			}
		}
	}

	/**
	 * Builds new Agent annotation for a CAS number and description.
	 * 
	 * @param code
	 *            CAS number
	 * @param detail
	 *            Description
	 */
	public AgentAnnotation(String code, String detail) {
		// Builds PMF container
		node = new XMLNode(new XMLTriple(METADATA_TAG, null, PMF_TAG));

		// Builds reference tag
		if (code != null) {
			XMLNode refNode = new XMLNode(new XMLTriple(REF_TAG, null, REF_NS));
			refNode.addChild(new XMLNode("http://identifiers.org/ncim/" + code));
			node.addChild(refNode);
		}

		// Builds detail tag
		if (detail != null) {
			XMLTriple detailTriple = new XMLTriple(DETAIL_TAG, null, DETAIL_NS);
			XMLNode detailNode = new XMLNode(detailTriple);
			detailNode.addChild(new XMLNode(detail));
			node.addChild(detailNode);
		}

		this.ref = code; // Copies CAS number
		this.detail = detail; // Copies description
	}

	// Getters
	public XMLNode getNode() {
		return node;
	}

	public String getRef() {
		return ref;
	}

	public String getDetail() {
		return detail;
	}
}