package de.bund.bfr.knime.pmm.sbmlutil;

import org.hsh.bfr.db.DBKernel;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.AgentXml;

/** Wrapper class for a Pmm Lab organism that holds a SBML species. */
public class Organism {
	private Species species;
	private OrganismAnnotation annotation;

	public Organism(Species species) {
		this.species = species;
		if (species.getAnnotation().getNonRDFannotation() != null) {
			annotation = new OrganismAnnotation(species.getAnnotation());
		}
	}

	public Organism(final AgentXml agent, final String unit,
			final Compartment compartment) {
		species = new Species("species" + agent.getId());
		species.setBoundaryCondition(false);
		species.setConstant(false);
		species.setUnits(createId(unit));

		// Gets species' name from DB
		String name = (String) DBKernel.getValue("Agenzien", "ID", agent
				.getId().toString(), "Agensname");
		if (name != null) {
			species.setName(name);

			// Get CAS number from DB
			String casNumber = (String) DBKernel.getValue("Agenzien", "ID",
					agent.getId().toString(), "CAS_Nummer");

			// Create and add an annotation with the CAS number
			annotation = new OrganismAnnotation(casNumber);
			species.setAnnotation(annotation);
		}
	}

	public Species getSpecies() {
		return species;
	}

	public AgentXml toAgentXml() {
		AgentXml agent = new AgentXml();

		if (annotation != null) {
			// Get agent id
			int id = (int) DBKernel.getValue("Agenzien", "CAS_Nummer",
					annotation.getCasNumber(), "ID");
			// Get agent name
			String name = (String) DBKernel.getValue("Agenzien", "CAS_Nummer",
					annotation.getCasNumber(), "Agensname");
			// Get dbuuid
			String dbuuid = DBKernel.getLocalDBUUID();

			agent.setId(id);
			agent.setName(name);
			agent.setDbuuid(dbuuid);
		} else {
			agent.setName(species.getName());
		}

		return agent;
	}

	private static String createId(String s) {
		return s.replaceAll("\\W+", " ").trim().replace(" ", "_");
	}
}

class OrganismAnnotation extends Annotation {

	private static final long serialVersionUID = -1663349380957935722L;
	private String casNumber;

	/**
	 * Creates a new OrganismAnnotation cloning an annotations and gets its CAS
	 * number.
	 */
	public OrganismAnnotation(Annotation annotation) {
		super(annotation);

		// Get CAS number from annotation
		XMLNode nonRDFAnnot = annotation.getNonRDFannotation();
		XMLNode metadata = nonRDFAnnot.getChildElement("metadata", "");
		if (metadata != null) {
			XMLNode source = metadata.getChildElement("source", "");
			String ref = source.getChild(0).getCharacters();
			casNumber = ref.substring(ref.lastIndexOf("/") + 1);
		}
	}

	/**
	 * Builds a new OrganismAnnotation from scratch which contains the species'
	 * CAS number.
	 */
	public OrganismAnnotation(String casNumber) {
		super();

		this.casNumber = casNumber;

		// build dc:source tag
		XMLNode source = new XMLNode(new XMLTriple("source", null, "dc"));
		source.addChild(new XMLNode("http://identifiers.org/ncim/" + casNumber));

		// Build PMF container
		XMLNode pmfNode = new XMLNode(new XMLTriple("metadata", null, "pmf"));
		pmfNode.addChild(source);

		// Add non Rdf annotation
		setNonRDFAnnotation(pmfNode);
	}

	public String getCasNumber() {
		return casNumber;
	}

}
