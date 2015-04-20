package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import groovy.util.Node;

import org.hsh.bfr.db.DBKernel;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MiscXml;

/**
 * Pmm Lab matrix.
 * 
 * @author Miguel Alba
 */
public class Matrix {
	private Compartment compartment;
	private MatrixAnnotation annotation;

	public Matrix(Compartment compartment) {
		this.compartment = compartment;
		if (compartment.getAnnotation().getNonRDFannotation() != null) {
			annotation = new MatrixAnnotation(compartment.getAnnotation());
		}
	}

	/** Build a PMM Lab Matrix from a MatrixXml */
	public Matrix(MatrixXml matrixXml, List<MiscXml> miscs) {
		compartment = new Compartment(createId(matrixXml.getName()));
		compartment.setConstant(true);
		
		// Tries to get data from DB (name and PMF code)
		Integer id = matrixXml.getId();
		String name = (String) DBKernel.getValue("Matrices", "ID",
				id.toString(), "Matrixname");

		if (name != null) {
			compartment.setName(name);
			
			// Get PMF code from DB
			String[] colNames = { "CodeSystem", "Basis" };
			String[] colVals = { "PMF", id.toString() };
			String pmfCode = (String) DBKernel.getValue(null, "Codes_Matrices",
					colNames, colVals, "Code");
			
			// Create dictionary with model variables values
			Map<String, Double> miscMap = new HashMap<>();
			for (MiscXml misc : miscs) {
				miscMap.put(misc.getName(), misc.getValue());
			}
			
			// Create and add an annotation with the PMF code
			annotation = new MatrixAnnotation(pmfCode, miscMap);
			compartment.setAnnotation(annotation);
		}
	}

	public Compartment getCompartment() {
		return compartment;
	}

	// Create MatrixXml
	public MatrixXml toMatrixXml() {
		MatrixXml matrix = new MatrixXml();

		if (annotation.getPMFCode() != null) {
			// Get matrix DB id
			String[] colNames = { "CodeSystem", "Code" };
			String[] colVals = { "PMF", annotation.getPMFCode() };
			int basis = (int) DBKernel.getValue(null, "Codes_Matrices",
					colNames, colVals, "Basis");

			// Get matrix name and dbuuid
			String name = (String) DBKernel.getValue("Matrices", "ID",
					Integer.toString(basis), "Matrixname");
			String dbuuid = DBKernel.getLocalDBUUID();

			// Set id, name and dbuuid
			matrix.setId(basis);
			matrix.setName(name);
			matrix.setDbuuid(dbuuid);
		}

		else if (compartment.getName() != null) {
			matrix.setName(compartment.getName());
		} else {
			matrix.setName("MissingCompartment");
		}

		return matrix;
	}

	private static String createId(String s) {
		return s.replaceAll("\\W+", " ").trim().replace(" ", "_");
	}
	
	public Node toGroovyNode() {
		return new Node(null, "sbml:compartment", compartment.writeXMLAttributes());
	}
	
	public Map<String, Double> getMiscs() {
		return annotation.getMiscs();
	}
}

class MatrixAnnotation extends Annotation {

	private static final long serialVersionUID = -8640288945266345113L;
	private String pmfCode;
	private Map<String, Double> miscs;

	/**
	 * Creates new MatrixAnnotation cloning an annotation and gets its PMF code.
	 * 
	 * @param annotation
	 *            . SBML Annotation.
	 */
	public MatrixAnnotation(Annotation annotation) {
		super(annotation);

		// Get PMF code from annotation
		XMLNode nonRDFAnnot = annotation.getNonRDFannotation();
		XMLNode metadata = nonRDFAnnot.getChildElement("metadata", "");
		if (metadata != null) {
			XMLNode source = metadata.getChildElement("source", "");
			pmfCode = source.getChild(0).getCharacters();
			
			miscs = new HashMap<>();
			for (XMLNode variableNode : metadata.getChildElements("modelvariable", "")) {
				XMLAttributes attrs = variableNode.getAttributes();
				miscs.put(attrs.getValue("name"), Double.parseDouble(attrs.getValue("value")));
			}
		}
	}

	/**
	 * Builds a new MatrixAnnotation from scratch which contains the matrix PMF
	 * code.
	 * 
	 * @param pmfCode
	 */
	public MatrixAnnotation(String pmfCode, Map<String, Double> miscs) {
		super();

		this.pmfCode = pmfCode;
		this.miscs = miscs;

		// Build dc:source tag
		XMLNode source = new XMLNode(new XMLTriple("source", null, "dc"));
		source.addChild(new XMLNode(pmfCode));

		// Build PMF container
		XMLNode pmfNode = new XMLNode(new XMLTriple("metadata", null, "pmf"));
		pmfNode.addChild(source);
		
		for (Entry<String, Double> entry : miscs.entrySet()) {
			XMLTriple triple = new XMLTriple("modelvariable", null, "pmml");
			XMLAttributes attrs = new XMLAttributes();
			attrs.add("name", entry.getKey());
			attrs.add("value", entry.getValue().toString());
			pmfNode.addChild(new XMLNode(triple, attrs));
		}

		// Add non Rdf annotation
		setNonRDFAnnotation(pmfNode);
	}

	public String getPMFCode() {
		return pmfCode;
	}
	
	public Map<String, Double> getMiscs() {
		return miscs;
	}
}