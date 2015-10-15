/**
 * Pmm Lab agent.
 * 
 * @author Miguel Alba
 */
package de.bund.bfr.knime.pmm.sbmlutil;

import org.hsh.bfr.db.DBKernel;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.Unit;

import de.bund.bfr.knime.pmm.annotation.sbml.AgentAnnotation;
import de.bund.bfr.knime.pmm.common.AgentXml;

/** Wrapper class for a Pmm Lab organism that holds a SBML species. */
public class Agent {

	Species species; // SBML species
	String description; // Description of the dependent variable
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
		AgentAnnotation agentAnnotation = new AgentAnnotation(species.getAnnotation());
		casNumber = agentAnnotation.getCasNumber();
		detail = agentAnnotation.getDetail();
		description = agentAnnotation.getDepDesc();

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
	public Agent(AgentXml agent, String unit, Compartment compartment, String description) {

		// Create SBML species with id prefixed by "species"
		species = new Species(Util.createId("species" + agent.getId()), 3, 1);
		species.setBoundaryCondition(false);
		species.setConstant(false);

		// Deal with null units
		if (unit == null) {
			species.setUnits(Unit.Kind.DIMENSIONLESS);
		} else {
			species.setUnits(Util.createId(unit));
		}

		species.setCompartment(compartment);
		species.setHasOnlySubstanceUnits(false);

		// Search species in DB
		String name = (String) DBKernel.getValue("Agenzien", "ID", agent.getId().toString(), "Agensname");

		// If found, try to get CAS number from DB
		if (name != null) {
			species.setName(name);

			// Get CAS number from DB
			casNumber = (String) DBKernel.getValue("Agenzien", "ID", agent.getId().toString(), "CAS_Nummer");
		} else {
			species.setName(agent.getName());
		}

		detail = agent.getDetail();

		// Build and set non RDF annotation
		species.setAnnotation(new AgentAnnotation(casNumber, detail, description).getAnnotation());
	}

	/**
	 * Creates a Pmm Lab AgentXml.
	 */
	public AgentXml toAgentXml() {
		AgentXml agent = new AgentXml();

		// If casNumber is not null, then gets ID, name, and dbuuid from DB
		if (casNumber != null) {
			// Get agent id
			int id = (int) DBKernel.getValue("Agenzien", "CAS_Nummer", casNumber, "ID");
			agent.setId(id);

			// Get agent name
			String name = (String) DBKernel.getValue("Agenzien", "CAS_Nummer", casNumber, "Agensname");
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

	public String getDescription() {
		return description;
	}
}
