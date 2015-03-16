package de.bund.bfr.knime.pmm.sbmlutil;

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Species;

import de.bund.bfr.knime.pmm.common.AgentXml;

/** Wrapper class for a Pmm Lab organism that holds a SBML species. */
public class Organism {
	private Species species;

	public Organism(Species species) {
		this.species = species;
	}

	public Species getSpecies() {
		return species;
	}

	public AgentXml toAgentXml() {
		AgentXml agentXml = new AgentXml();
		agentXml.setName(species.getName());
		return agentXml;
	}

	// Create an Organism from an AgentXml
	public static Organism convertAgentXmlToOrganism(final AgentXml agent,
			final String unit, final Compartment compartment) {
		Species species = new Species("species" + agent.getId());
		String name = (agent.getName() == null) ? "SpeciesMissing" : agent.getName();
		species.setName(name);
		species.setBoundaryCondition(false);
		species.setCompartment(compartment);
		species.setConstant(false);
		species.setUnits(createId(unit));

		return new Organism(species);
	}
	
	private static String createId(String s) {
		return s.replaceAll("\\W+", " ").trim().replace(" ", "_");
	}
}