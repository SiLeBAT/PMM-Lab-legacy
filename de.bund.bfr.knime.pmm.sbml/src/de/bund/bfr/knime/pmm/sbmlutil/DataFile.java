package de.bund.bfr.knime.pmm.sbmlutil;

import groovy.util.Node;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.knime.pmm.annotation.GroovyReferenceNode;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.numl.AtomicDescription;
import de.bund.bfr.numl.CompositeDescription;
import de.bund.bfr.numl.DataType;
import de.bund.bfr.numl.Description;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.OntologyTerm;
import de.bund.bfr.numl.ResultComponent;
import de.bund.bfr.numl.TupleDescription;

public class DataFile {

	NuMLDocument doc;

	public DataFile(NuMLDocument doc) {
		this.doc = doc;
	}

	public DataFile(int condId, String combaseId,
			LinkedHashMap<Integer, List<Double>> dim, String concUnit,
			String timeUnit, Matrix matrix, Agent agent,
			List<LiteratureItem> lits, Map<String, String> dlgInfo) {

		// Creates ontologies
		OntologyTerm time = new TimeOntology().prepareOntology(timeUnit);
		OntologyTerm conc = new ConcentrationOntology()
				.prepareOntology(concUnit);

		// Adds matrix and agent to the concentration metadata
		Node concMetadata = (Node) conc.getAnnotation().children().get(0);
		concMetadata.append(matrix.toGroovyNode());
		concMetadata.append(agent.toGroovyNode());

		// Creates concentration description
		AtomicDescription concDesc = new AtomicDescription();
		concDesc.setName("concentration");
		concDesc.setOntologyTerm(conc);
		concDesc.setValueType(DataType.Double);

		// Creates time description
		AtomicDescription timeDesc = new AtomicDescription();
		timeDesc.setName("Time");
		timeDesc.setOntologyTerm(time);
		timeDesc.setValueType(DataType.Double);

		TupleDescription td = new TupleDescription();
		List<Description> descriptions = new LinkedList<>();
		descriptions.add((Description) timeDesc);
		descriptions.add((Description) concDesc);
		td.setDescriptions(descriptions);

		CompositeDescription cd = new CompositeDescription();
		cd.setName("index");
		cd.setIndexType(DataType.Integer);
		cd.setDescription(td);

		ResultComponent result = new ResultComponent();
		result.setId("exp1");
		result.setDimensionDescription(cd);
		result.setDimension(dim);

		// Adds PMF namespace to resultComponent's annotation
		Map<String, String> pmfNS = new HashMap<>();
		pmfNS.put("xmlns:pmf",
				"http://sourceforge.net/microbialmodelingexchange/files/PMF-ML");
		Node resultNode = new Node(null, "annotation", pmfNS);
		result.setAnnotation(resultNode);

		// Adds CondId node
		resultNode.appendNode("pmf:condId", condId);
		// Adds CombaseId node
		if (combaseId != null) {
			resultNode.appendNode("pmf:combaseId", combaseId);
		}

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

	public double[][] getData() {

		@SuppressWarnings("unchecked")
		LinkedHashMap<Integer, List<Double>> dim = (LinkedHashMap<Integer, List<Double>>) doc
				.getResultComponents().get(0).getDimension();

		double[][] data = new double[dim.size()][2];
		int i = 0;
		for (List<Double> list : dim.values()) {
			data[i][0] = list.get(0); // Assigns time
			data[i][1] = list.get(1); // Assigns concentration
			i++;
		}

		return data;
	}
}

abstract class Ontology {

	final public OntologyTerm prepareOntology(String unit) {
		OntologyTerm ontology = createOntology();
		ontology.setAnnotation(createAnnotation(unit));
		return ontology;
	}

	protected abstract OntologyTerm createOntology();

	protected Node createAnnotation(String unit) {
		// Creates annotation with the PMF namespace
		Map<String, String> pmfNS = new HashMap<>();
		pmfNS.put("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
		Node annot = new Node(null, "annotation", pmfNS);

		// Creates and adds PMF annotation
		Map<String, String> sbmlNS = new HashMap<>();
		sbmlNS.put("xmlns:sbml", SBMLDocument.URI_NAMESPACE_L3V1Core);
		Node pmfNode = new Node(annot, "pmf:metadata", sbmlNS);

		// Gets unit definition from DB
		UnitsFromDB dbUnit = DBUnits.getDBUnits().get(unit);
		UnitDefinitionWrapper udWrapper = UnitDefinitionWrapper
				.xmlToUnitDefinition(dbUnit.getMathML_string());

		// Modifies the unit definition and adds it to the pmfNode
		udWrapper.getUnitDefinition().setId(Util.createId(unit));
		udWrapper.getUnitDefinition().setName(unit);
		pmfNode.append(udWrapper.toGroovyNode());

		return annot;
	}
}

class TimeOntology extends Ontology {

	protected OntologyTerm createOntology() {
		OntologyTerm ontology = new OntologyTerm();
		ontology.setTerm("time");
		ontology.setSourceTermId("SBO:0000345");
		try {
			ontology.setOntologyURI(new URI("http://www.ebi.ac.uk/sbo/"));
		} catch (URISyntaxException e) {
			// The URI assigned is valid so there is no need to do something
			// here
		}

		return ontology;
	}
}

class ConcentrationOntology extends Ontology {

	protected OntologyTerm createOntology() {
		OntologyTerm ontology = new OntologyTerm();
		ontology.setTerm("concentration");
		ontology.setSourceTermId("SBO:0000196");
		try {
			ontology.setOntologyURI(new URI("http://www.ebi.ac.uk/sbo/"));
		} catch (URISyntaxException e) {
			// The URI assigned is valid so there is no need to something here
		}

		return ontology;
	}
}