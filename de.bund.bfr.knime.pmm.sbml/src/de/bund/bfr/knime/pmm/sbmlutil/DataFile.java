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

/**
 * NuML data document handler
 * 
 * @author Miguel Alba
 */
public class DataFile {

	NuMLDocument doc;

	/**
	 * Builds a DataFile with an existing NuMLDocument.
	 * 
	 * @param doc
	 *            NumlDocument.
	 */
	public DataFile(NuMLDocument doc) {
		this.doc = doc;
	}

	/**
	 * Builds a DataFile.
	 * 
	 * @param dimension
	 * @param concentrationUnit
	 * @param matrixXml
	 * @param agentXml
	 * @param depUnit
	 * @param dlgInfo
	 * @throws URISyntaxException
	 */
	public DataFile(LinkedHashMap<Double, Double> dimension, String concentrationUnit,
			Matrix matrix, Agent agent, List<LiteratureItem> lits, String depUnit,
			Map<String, String> dlgInfo) throws URISyntaxException {
		
		// Creates ontologies
		OntologyTerm time = createTimeOntology();
		OntologyTerm concentration = createConcentrationOntology(
				concentrationUnit, matrix, agent, lits, depUnit);

		// * creates descriptions for the ontologies
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
		Node pmfNode = new Node(resultComponentNode, "pmf:metadata", dcNS);

		// Add creator
		if (dlgInfo.containsKey("GivenName")) {
			Node creatorNode = new Node(pmfNode, "dc:creator", "");
			creatorNode.setValue(dlgInfo.get("GivenName"));
		}

		// Add created
		if (dlgInfo.containsKey("Created")) {
			Node createdNode = new Node(pmfNode, "dcterms:created", "");
			createdNode.setValue(dlgInfo.get("Created"));
		}

		// Add model type
		if (dlgInfo.containsKey("type")) {
			Node typeNode = new Node(pmfNode, "dc:type", "");
			typeNode.setValue(dlgInfo.get("type"));
		}

		doc = new NuMLDocument();
		doc.setResultComponents(Arrays.asList(resultComponent));
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
	 * @param concentrationUnit
	 * @param matrixXml
	 * @param agentXml
	 * @param depUnit
	 * @return
	 * @throws URISyntaxException
	 */
	private OntologyTerm createConcentrationOntology(String concentrationUnit,
			Matrix matrix, Agent agent, List<LiteratureItem> lits, String depUnit)
			throws URISyntaxException {

		OntologyTerm concentration = new OntologyTerm();
		concentration.setTerm("concentration");
		concentration.setSourceTermId("SBO:0000196");
		concentration.setOntologyURI(new URI("http://www.ebi.ac.uk/sbo/"));

		// unit annotation
		// * Gets unit definition from db
		UnitsFromDB dbUnit = DBUnits.getDBUnits().get(concentrationUnit);
		UnitDefinitionWrapper udwrapper = UnitDefinitionWrapper
				.xmlToUnitDefinition(dbUnit.getMathML_string());
		UnitDefinition ud = udwrapper.getUnitDefinition();
		ud.setId(Util.createId(concentrationUnit));
		ud.setName(concentrationUnit);

		// Adds PMF namespace to annotation
		Map<String, String> pmfNS = new HashMap<>();
		pmfNS.put("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
		concentration.setAnnotation(new Node(null, "annotation", pmfNS));

		// Adds PMF annotation
		Map<String, String> sbmlNS = new HashMap<>();
		sbmlNS.put("xmlns:sbml",
				"http://www.sbml.org/sbml/level3/version1/core");
		Node pmfNode = new Node(concentration.getAnnotation(), "pmf:metadata",
				sbmlNS);

		pmfNode.append(udwrapper.toGroovyNode());  // Adds unit definition

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
}