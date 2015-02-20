// Organism wrapper
package de.bund.bfr.knime.pmm.sbmlutil;

import org.hsh.bfr.db.DBKernel;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.xml.XMLNamespaces;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.annotation.OrganismNode;
import de.bund.bfr.knime.pmm.common.AgentXml;

public class Organism {
	private Species species;
	private String casNumber;

	public Organism(Species species) {
		this.species = species;

		// parse casNumber
		XMLNode nonRDFAnnot = species.getAnnotation().getNonRDFannotation();
		XMLNode metadata = nonRDFAnnot.getChildElement("metadata", "");

		XMLNode casNumberNode = metadata.getChildElement("source", "");
		String chars = casNumberNode.getChildAt(0).getCharacters();
		int pos = chars.lastIndexOf("/");
		this.casNumber = chars.substring(pos + 1);
	}

	public Organism(Species species, String casNumber) {
		this.species = species;
		this.casNumber = casNumber;
	}

	public Species getSpecies() {
		return species;
	}

	public AgentXml toAgentXml() {
		// Get id and name from DB
		Integer id = (Integer) DBKernel.getValue("Agenzien", "CAS_Nummer",
				this.casNumber, "ID");
		String name = (String) DBKernel.getValue("Agenzien", "CAS_Nummer",
				this.casNumber, "Agensname");

		return new AgentXml(id, name, "", "");
	}

	public static Organism convertAgentXmlToOrganism(final AgentXml agent,
			final String unit, final Compartment compartment) {
		Species species = new Species("species" + agent.getId());
		species.setName(agent.getName());
		species.setBoundaryCondition(false);
		species.setCompartment(compartment);
		species.setConstant(false);
		species.setUnits(createId(unit));

		// Add cas number annotation
		XMLTriple pmfTriple = new XMLTriple("metadata", null, "pmf");
		XMLNamespaces pmfNS = new XMLNamespaces();
		pmfNS.add("http://purl.org/dc/terms/", "dc");
		XMLNode pmfNode = new XMLNode(pmfTriple, null, pmfNS);

		// Add cas number node
		String casNumber = (String) DBKernel.getValue("Agenzien", "ID", agent
				.getId().toString(), "CAS_Nummer");

		OrganismNode organismNode = new OrganismNode(casNumber);
		pmfNode.addChild(organismNode.getNode());

		// add non rdf annotation
		Annotation annot = new Annotation();
		annot.setNonRDFAnnotation(pmfNode);
		annot.addDeclaredNamespace("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");

		species.setAnnotation(annot);

		return new Organism(species);

	}

	private static String createId(String s) {
		return s.replaceAll("\\W+", " ").trim().replace(" ", "_");
	}
}
