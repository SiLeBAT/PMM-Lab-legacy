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
		XMLNode annotation = species.getAnnotation().getNonRDFannotation();
		XMLNode metadata = annotation.getChildElement("metadata", "");

		if (metadata != null) {
			XMLNode source = metadata.getChildElement("source", "");
			String ref = source.getChild(0).getCharacters(); // whole reference
			casNumber = ref.substring(ref.lastIndexOf("/") + 1);
		}

		this.species = species;
	}

	public Agent(AgentXml agent, String unit) {
		species = new Species("species" + agent.getId());
		species.setBoundaryCondition(false);
		species.setConstant(false);
		species.setUnits(createId(unit));

		// Search species in DB
		String name = (String) DBKernel.getValue("Agenzien", "ID", agent
				.getId().toString(), "Agensname");

		// If found
		if (name != null) {
			species.setName(name);

			// Get CAS number from DB
			casNumber = (String) DBKernel.getValue("Agenzien", "ID", agent
					.getId().toString(), "CAS_Nummer");

			// Create and add an annotation with the CAS number

			// * Build dc:source tag
			XMLNode source = new XMLNode(new XMLTriple("source", null, "dc"));
			source.addChild(new XMLNode("http://identifiers.org/ncim/"
					+ casNumber));

			// * Build PMF container
			XMLNode pmfNode = new XMLNode(
					new XMLTriple("metadata", null, "pmf"));
			pmfNode.addChild(source);

			// Add non RDF annotation
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

	private static String createId(String s) {
		return s.replaceAll("\\W+", " ").trim().replace(" ", "_");
	}

	public Node toGroovyNode() {
		return new Node(null, "sbml:species", species.writeXMLAttributes());
	}
}