package de.bund.bfr.knime.pmm.sbmlutil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;

import de.bund.bfr.knime.pmm.annotation.GroovyReferenceNode;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.numl.AtomicDescription;
import de.bund.bfr.numl.CompositeDescription;
import de.bund.bfr.numl.DataType;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.OntologyTerm;
import de.bund.bfr.numl.ResultComponent;
import groovy.util.Node;

public class DataFile {

	NuMLDocument doc;

	public DataFile(NuMLDocument doc) {
		this.doc = doc;
	}

	public DataFile(String dataId, LinkedHashMap<Double, Double> dimension,
			String concUnit, Matrix matrix, Agent agent,
			List<LiteratureItem> lits, Map<String, String> dlgInfo)
			throws URISyntaxException {

		// Creates ontologies
		OntologyTerm time = createTimeOntology();
		OntologyTerm conc = createConcOntology(concUnit, matrix, agent);

		// Creates concentration description
		AtomicDescription concDesc = new AtomicDescription();
		concDesc.setName("concentration");
		concDesc.setOntologyTerm(conc);
		concDesc.setValueType(DataType.Double);

		// Creates time description
		CompositeDescription timeDesc = new CompositeDescription();
		timeDesc.setName("Time");
		timeDesc.setIndexType(DataType.Double);
		timeDesc.setOntologyTerm(time);
		timeDesc.setDescription(concDesc);

		ResultComponent result = new ResultComponent();
		result.setId("exp1");
		result.setDimensionDescription(timeDesc);
		result.setDimension(dimension);

		// Adds PMF namespace to resultComponent's annotation
		Map<String, String> pmfNS = new HashMap<>();
		pmfNS.put("xmlns:pmf",
				"http://sourceforge.net/microbialmodelingexchange/files/PMF-ML");
		Node resultNode = new Node(null, "annotation", pmfNS);
		result.setAnnotation(resultNode);
		
		// Adds data id node
		resultNode.appendNode("pmf:dataId", dataId);

		// Adds PMF annotation
		Map<String, String> dcNS = new HashMap<>(); // dc and dcterms namespaces
		dcNS.put("xmlns:dc", "http://purl.org/dc/elements/1.1/");
		dcNS.put("xmlns:dcterms", "http://purl.org/dc/terms/");
		Node pmfNode = new Node(resultNode, "pmf:metadata", dcNS);

		String givenName = dlgInfo.get("GivenName");
		String familyName = dlgInfo.get("FamilyName");
		String contact = dlgInfo.get("Contact");
		if (givenName != null || familyName != null || contact != null) {
			StringBuilder sb = new StringBuilder();
			if (givenName != null) {
				sb.append(givenName + ". ");
			}
			if (familyName != null) {
				sb.append(familyName + ". ");
			}
			if (contact != null) {
				sb.append(contact);
			}
			String creator = sb.toString();
			pmfNode.appendNode("dc:creator", creator);
		}

		// Adds created
		if (dlgInfo.containsKey("Created")) {
			pmfNode.appendNode("dcterms:created", dlgInfo.get("Created"));
		}
		
		// Adds last modified
		if (dlgInfo.containsKey("Modified")) {
			pmfNode.appendNode("dcterms:modified", dlgInfo.get("Modified"));
		}

		// Adds annotations for literature items
		for (LiteratureItem lit : lits) {
			Node litNode = new GroovyReferenceNode(lit).getNode();
			pmfNode.append(litNode);
		}

		doc = new NuMLDocument();
		doc.setResultComponents(Arrays.asList(result));
	}

	public NuMLDocument getDocument() {
		return doc;
	}

	public List<TimeSeriesXml> getData() {
		List<TimeSeriesXml> ts = new LinkedList<>();

		@SuppressWarnings("unchecked")
		LinkedHashMap<Double, Double> dim = (LinkedHashMap<Double, Double>) doc
				.getResultComponents().get(0).getDimension();

		// Common fields
		String timeUnit = "h";
		String concentrationUnit = "log10(count/g)";
		String concentrationUnitObjectType = "CFU";
		Double concentrationStdDev = null;
		Integer numberOfMeasurements = null;

		int counter = 0;
		for (Entry<Double, Double> entry : dim.entrySet()) {
			String name = String.format("t%d", counter);
			double time = entry.getKey();
			double concentration = entry.getValue();

			TimeSeriesXml t = new TimeSeriesXml(name, time, timeUnit,
					concentration, concentrationUnit, concentrationStdDev,
					numberOfMeasurements);
			t.setConcentrationUnitObjectType(concentrationUnitObjectType);
			ts.add(t);
			counter++;
		}

		return ts;
	}

	/**
	 * Creates time ontology.
	 * 
	 * @return
	 * @throws URISyntaxException
	 */
	private OntologyTerm createTimeOntology() throws URISyntaxException {
		OntologyTerm time = new OntologyTerm();
		time.setTerm("time");
		time.setSourceTermId("SBO:0000345");
		time.setOntologyURI(new URI("http://www.ebi.ac.uk/sbo/"));

		// Adds PMF namespace to annotation
		Map<String, String> pmfNS = new HashMap<>();
		pmfNS.put("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
		time.setAnnotation(new Node(null, "annotation", pmfNS));

		// Adds PMF annotation
		Map<String, String> sbmlNS = new HashMap<>();
		sbmlNS.put("xmlns:sbml",
				"http://www.sbml.org/sbml/level3/version1/core");
		Node pmfNode = new Node(time.getAnnotation(), "pmf:metadata", sbmlNS);

		// Adds unit definition annotation
		UnitDefinition ud = new UnitDefinition("h", "h", 3, 1);
		ud.addUnit(new Unit(3600, 1, Unit.Kind.SECOND, 1, 3, 1));

		Node node = new Node(pmfNode, "sbml:unitDefinition",
				ud.writeXMLAttributes());
		node.appendNode("sbml:unit", ud.getUnit(0).writeXMLAttributes());

		return time;
	}

	/**
	 * Creates concentration ontology.
	 * 
	 * @param unit
	 *            : Concentration unit
	 * @param matrix
	 * @param agent
	 * @throws URISyntaxException
	 */
	private OntologyTerm createConcOntology(String unit, Matrix matrix,
			Agent agent) throws URISyntaxException {

		OntologyTerm ontology = new OntologyTerm();
		ontology.setTerm("concentration");
		ontology.setSourceTermId("SBO:0000196");
		ontology.setOntologyURI(new URI("http://www.ebi.ac.uk/sbo/"));

		// Adds PMF namespace to annotation
		Map<String, String> pmfNS = new HashMap<>();
		pmfNS.put("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
		Node annot = new Node(null, "annotation", pmfNS);
		ontology.setAnnotation(annot);

		// Adds PMF annotation
		Map<String, String> sbmlNS = new HashMap<>();
		sbmlNS.put("xmlns:sbml",
				"http://www.sbml.org/sbml/level3/version1/core");
		Node pmfNode = new Node(annot, "pmf:metadata", sbmlNS);

		// Gets unit definition from DB and adds unit annotation
		UnitsFromDB dbUnit = DBUnits.getDBUnits().get(unit);
		UnitDefinitionWrapper udWrapper = UnitDefinitionWrapper
				.xmlToUnitDefinition(dbUnit.getMathML_string());

		udWrapper.getUnitDefinition().setId(Util.createId(unit));
		udWrapper.getUnitDefinition().setName(unit);
		pmfNode.append(udWrapper.toGroovyNode());

		pmfNode.append(matrix.toGroovyNode()); // Adds matrix annotation
		pmfNode.append(agent.toGroovyNode()); // Adds agent annotation

		return ontology;
	}
}
