package de.bund.bfr.knime.pmm.file;

import groovy.util.Node;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Element;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;

import de.bund.bfr.knime.pmm.annotation.GroovyReferenceNode;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.sbmlutil.Agent;
import de.bund.bfr.knime.pmm.sbmlutil.Matrix;
import de.bund.bfr.numl.AtomicDescription;
import de.bund.bfr.numl.CompositeDescription;
import de.bund.bfr.numl.DataType;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.OntologyTerm;
import de.bund.bfr.numl.ResultComponent;

/**
 * Case 0: Experimental data file
 * 
 * @author Miguel Alba
 */
public class RawDataFile {

	NuMLDocument numlDoc;

	public RawDataFile(NuMLDocument numlDoc) {
		this.numlDoc = numlDoc;
	}

	public RawDataFile(LinkedHashMap<Double, Double> dimension,
			String concentrationUnit, Matrix matrix, Agent agent,
			List<LiteratureItem> lits) throws URISyntaxException {

		// Creates ontologies
		OntologyTerm time = createTimeOntology();
		OntologyTerm concentration = createConcentrationOntology(matrix, agent,
				lits);

		// Creates descriptions for the ontologies
		AtomicDescription concentrationDesc = new AtomicDescription();
		concentrationDesc.setName("concentration");
		concentrationDesc.setOntologyTerm(concentration);
		concentrationDesc.setValueType(DataType.Double);

		CompositeDescription timeDesc = new CompositeDescription();
		timeDesc.setName("Time");
		timeDesc.setIndexType(DataType.Double);
		timeDesc.setOntologyTerm(time);
		timeDesc.setDescription(concentrationDesc);

		ResultComponent resultComponent = new ResultComponent();
		resultComponent.setId("exp1");
		resultComponent.setDimensionDescription(timeDesc);
		resultComponent.setDimension(dimension);

		// Adds PMF namespace to resultComponent's annotation
		Map<String, String> pmfNS = new HashMap<>();
		pmfNS.put("xmlns:pmf",
				"http://sourceforge.net/microbialmodelingexchange/files/PMF-ML");
		Node resultComponentNode = new Node(null, "annotation", pmfNS);
		resultComponent.setAnnotation(resultComponentNode);

		// Adds PMF annotation
		Map<String, String> dcNS = new HashMap<>(); // dc and dcterms namespaces
		dcNS.put("xmlns:dc", "http://purl.org/dc/elements/1.1/");
		dcNS.put("xmlns:dcterms", "http://purl.org/dc/terms/");

		numlDoc = new NuMLDocument();
		numlDoc.setResultComponents(Arrays.asList(resultComponent));
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
	 * @param matrixXml
	 * @param agentXml
	 * @throws URISyntaxException
	 */
	private OntologyTerm createConcentrationOntology(Matrix matrix,
			Agent agent, List<LiteratureItem> lits) throws URISyntaxException {

		OntologyTerm concentration = new OntologyTerm();
		concentration.setTerm("concentration");
		concentration.setSourceTermId("SBO:0000196");
		concentration.setOntologyURI(new URI("http://www.ebi.ac.uk/sbo/"));

		// Adds PMF namespace to annotation
		Map<String, String> pmfNS = new HashMap<>();
		pmfNS.put("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
		pmfNS.put("xmlns:dc", "http://purl.org/dc/elements/1.1/");
		concentration.setAnnotation(new Node(null, "annotation", pmfNS));

		// Adds PMF annotation
		Map<String, String> sbmlNS = new HashMap<>();
		sbmlNS.put("xmlns:sbml",
				"http://www.sbml.org/sbml/level3/version1/core");
		Node pmfNode = new Node(concentration.getAnnotation(), "pmf:metadata",
				sbmlNS);

		// Comparment annotation
		Node matrixNode = matrix.toGroovyNode();
		pmfNode.append(matrixNode);

		// Species annotation
		Node agentNode = agent.toGroovyNode();
		pmfNode.append(agentNode);

		// Adds annotations for literature items
		for (LiteratureItem lit : lits) {
			Node litNode = new GroovyReferenceNode(lit).getNode();
			pmfNode.append(litNode);
		}

		return concentration;
	}

	public NuMLDocument getDocument() {
		return numlDoc;
	}

	public List<TimeSeriesXml> getData() {

		TimeSeriesXml basePoint = new TimeSeriesXml("", null, "h", null,
				"log10(count/g)", null, null);
		basePoint.setConcentrationUnitObjectType("CFU");
		Element basePointElement = basePoint.toXmlElement();

		List<TimeSeriesXml> ts = new LinkedList<>();
		@SuppressWarnings("unchecked")
		LinkedHashMap<Double, Double> dim = (LinkedHashMap<Double, Double>) numlDoc
				.getResultComponents().get(0).getDimension();

		for (Entry<Double, Double> entry : dim.entrySet()) {
			TimeSeriesXml t = new TimeSeriesXml(basePointElement);
			t.setName(String.format("t%d", ts.size()));
			t.setTime(entry.getKey());
			t.setConcentration(entry.getValue());
			ts.add(t);
		}

		return ts;
	}
}
