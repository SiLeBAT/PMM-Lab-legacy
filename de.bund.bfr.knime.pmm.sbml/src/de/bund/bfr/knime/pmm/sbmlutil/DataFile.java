package de.bund.bfr.knime.pmm.sbmlutil;

import groovy.util.Node;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;

import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.units.UnitsFromDB;
import de.bund.bfr.numl.AtomicDescription;
import de.bund.bfr.numl.CompositeDescription;
import de.bund.bfr.numl.DataType;
import de.bund.bfr.numl.NuMLDocument;
import de.bund.bfr.numl.NuMLWriter;
import de.bund.bfr.numl.OntologyTerm;
import de.bund.bfr.numl.ResultComponent;

public class DataFile {
	private NuMLDocument doc;
	
	public DataFile(NuMLDocument doc) {
		this.doc = doc;
	}

	public DataFile(Map<Double, Double> dimension, String unit, String matrix,
			String organism, Map<String, String> dlgInfo) throws URISyntaxException {
		OntologyTerm time = createTimeOntology();
		OntologyTerm concentration = createConcentrationOntology(unit, matrix,
				organism);

		// * create descriptions for the ontologies
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

		// Add PMF namespace to resultComponent's annotation
		Map<String, String> pmfNS = new HashMap<>();
		pmfNS.put("xmlns:pmf",
				"http://sourceforge.net/microbialmodelingexchange/files/PMF-ML");
		Node resultComponentNode = new Node(null, "annotation", pmfNS);
		resultComponent.setAnnotation(resultComponentNode);

		// Add PMF annotation
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

	public String toString() {
		NuMLWriter writer = new NuMLWriter();
		return writer.toString(doc);
	}

	private OntologyTerm createTimeOntology() throws URISyntaxException {
		OntologyTerm time = new OntologyTerm();
		time.setTerm("time");
		time.setSourceTermId("SBO:0000345");
		time.setOntologyURI(new URI("http://www.ebi.ac.uk/sbo/"));

		// Add PMF namespace to annotation
		Map<String, String> pmfNS = new HashMap<>();
		pmfNS.put("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
		time.setAnnotation(new Node(null, "annotation", pmfNS));

		// Add PMF annotation
		Map<String, String> sbmlNS = new HashMap<>();
		sbmlNS.put("xmlns:sbml",
				"http://www.sbml.org/sbml/level3/version1/core");
		Node pmfNode = new Node(time.getAnnotation(), "pmf:metadata", sbmlNS);

		// Add unit definition annotation
		UnitDefinition ud = new UnitDefinition("h", "h", 3, 1);
		ud.addUnit(new Unit(3600, 1, Unit.Kind.SECOND, 1, 3, 1));

		Node node = new Node(pmfNode, "sbml:unitDefinition",
				ud.writeXMLAttributes());
		node.appendNode("sbml:unit", ud.getUnit(0).writeXMLAttributes());

		return time;
	}

	protected static String createId(String s) {
		return s.replaceAll("\\W+", " ").trim().replace(" ", "_");
	}

	private OntologyTerm createConcentrationOntology(String unit,
			String matrix, String organism) throws URISyntaxException {
		organism = (organism == null || organism.isEmpty()) ? "MissingSpecies"
				: organism;
		matrix = (matrix == null || matrix.isEmpty()) ? "MissingCompartment"
				: matrix;

		OntologyTerm concentration = new OntologyTerm();
		concentration.setTerm("concentration");
		concentration.setSourceTermId("SBO:0000196");
		concentration.setOntologyURI(new URI("http://www.ebi.ac.uk/sbo/"));

		// unit annotation
		// * Get units from DB
		UnitsFromDB unitDB = new UnitsFromDB();
		unitDB.askDB();
		Map<Integer, UnitsFromDB> origMap = unitDB.getMap();

		// * Create new map with unit display as keys
		Map<String, UnitsFromDB> map = new HashMap<>();
		for (UnitsFromDB ufdb : origMap.values())
			map.put(ufdb.getDisplay_in_GUI_as(), ufdb);

		// * Get unit definition from db
		UnitsFromDB dbUnit = map.get(unit);
		UnitDefinitionWrapper udwrapper = UnitDefinitionWrapper
				.xmlToUnitDefinition(dbUnit.getMathML_string());
		UnitDefinition ud = udwrapper.getUnitDefinition();
		ud.setId(createId(unit));
		ud.setName(unit);

		// Add PMF namespace to annotation
		Map<String, String> pmfNS = new HashMap<>();
		pmfNS.put("xmlns:pmf",
				"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
		concentration.setAnnotation(new Node(null, "annotation", pmfNS));

		// Add PMF annotation
		Map<String, String> sbmlNS = new HashMap<>();
		sbmlNS.put("xmlns:sbml",
				"http://www.sbml.org/sbml/level3/version1/core");
		Node pmfNode = new Node(concentration.getAnnotation(), "pmf:metadata",
				sbmlNS);

		// Add unit definition
		Node udNode = new Node(pmfNode, "sbml:unitDefinition",
				ud.writeXMLAttributes());
		for (Unit u : ud.getListOfUnits())
			udNode.appendNode("sbml:unit", u.writeXMLAttributes());

		// compartment annotation
		Compartment compartment = new Compartment(createId(matrix), matrix, 3,
				1);
		pmfNode.appendNode("sbml:compartment", compartment.writeXMLAttributes());

		// species annotation
		Species species = new Species(3, 1);
		species.setId(createId(organism));
		species.setName(organism);
		species.setCompartment(createId(matrix));
		species.setConstant(false);
		species.setBoundaryCondition(true);
		species.setSubstanceUnits(unit); // TODO: concentration
											// unit
		species.setHasOnlySubstanceUnits(false);

		pmfNode.appendNode("sbml:species", species.writeXMLAttributes());

		return concentration;
	}

	public NuMLDocument getDocument() {
		return doc;
	}

	public List<TimeSeriesXml> getData() {
		List<TimeSeriesXml> ts = new LinkedList<>();

		@SuppressWarnings("unchecked")
		Map<Double, Double> dim = (Map<Double, Double>) doc
				.getResultComponents().get(0).getDimension();

		int counter = 0;
		for (Entry<Double, Double> entry : dim.entrySet()) {
			String name = String.format("t%d", counter);
			double time = entry.getKey();
			String timeUnit = "h";
			double concentration = entry.getValue();
			String concentrationUnit = "log10(count/g)"; // TODO: parse
															// concentrationUnit
			String concentrationUnitObjectType = "CFU"; // TODO: this needs to
														// be parsed too
			Double concentrationStdDev = null;
			Integer numberOfMeasurements = null;

			TimeSeriesXml t = new TimeSeriesXml(name, time, timeUnit,
					concentration, concentrationUnit, concentrationStdDev,
					numberOfMeasurements);
			t.setConcentrationUnitObjectType(concentrationUnitObjectType);
			ts.add(t);
		}

		return ts;
	}
}