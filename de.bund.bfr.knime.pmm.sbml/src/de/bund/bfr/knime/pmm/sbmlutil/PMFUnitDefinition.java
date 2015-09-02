package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import groovy.util.Node;

/**
 * PMF Unit Definition
 * 
 * @author Miguel de Alba
 *
 */
public class PMFUnitDefinition {

	UnitDefinition unitDefinition;
	String transformationName;

	/**
	 * Inits a PMFUnitDefinition with no transformation
	 */
	public PMFUnitDefinition(UnitDefinition unitDefinition) {
		this.unitDefinition = unitDefinition;
	}

	/**
	 * Inits a PMFUnitDefinition with a transformation
	 */
	public PMFUnitDefinition(UnitDefinition unitDefinition, String transformationName) {
		this.unitDefinition = unitDefinition;
		this.transformationName = transformationName;
	}

	/**
	 * Creates a PMFUnitDefinition from an XML string
	 */
	public static PMFUnitDefinition xmlToPMFUnitDefinition(String xml) {
		String preXml = "<?xml version='1.0' encoding='UTF-8' standalone='no'?>"
				+ "<sbml xmlns=\"http://www.sbml.org/sbml/level3/version1/core\"" + " level=\"3\" version=\"1\""
				+ " xmlns:pmf=\"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML\""
				+ "><model id=\"ID\">" + "<listOfUnitDefinitions>";

		String postXml = "</listOfUnitDefinitions>" + "</model>" + "</sbml>";

		// Parse transformation name if present
		String transformation = null;
		int transformationIndex = xml.indexOf("<transformation");
		int endTransformationIndex = xml.indexOf("<", transformationIndex + 1);
		if (transformationIndex != -1) {
			int firstQuotePos = xml.indexOf("\"", transformationIndex);
			int secQuotePos = xml.indexOf("\"", firstQuotePos + 1);
			transformation = xml.substring(firstQuotePos + 1, secQuotePos);
			
			// Removes the transformation annotation (this annotation has a name
			// which is not prefixed and then cannot be parsed properly)
			xml = xml.substring(0, transformationIndex) + xml.substring(endTransformationIndex);
		}

		// Remove namespace (all the DB units have this namespace which is not
		// necessary)
		xml = xml.replaceAll("xmlns=\"http://sourceforge.net/projects/microbialmodelingexchange/files/Units\"", "");
		String totalXml = preXml + xml + postXml;

		try {
			// Create SBMLDocument and get its model
			SBMLDocument sbmlDoc = SBMLReader.read(totalXml);
			Model model = sbmlDoc.getModel();

			// Create a new UnitDefinition from XML
			UnitDefinition xmlUnitDef = model.getUnitDefinition(0);
			UnitDefinition ud = new UnitDefinition(3, 1);
			for (Unit unit : xmlUnitDef.getListOfUnits()) {
				// Creates new unit
				Unit newUnit = new Unit(unit);

				// Adds default parameters if missing
				if (!unit.isSetMultiplier()) {
					newUnit.setMultiplier(1.0);
				}
				if (!unit.isSetScale()) {
					newUnit.setScale(0);
				}
				if (!unit.isSetExponent()) {
					newUnit.setExponent(1.0);
				}

				ud.addUnit(newUnit);
			}

			if (transformation != null) {
				// Add transformation annotation
				XMLTriple transformationTriple = new XMLTriple("transformation", null, "pmf");
				XMLNode transformationNode = new XMLNode(transformationTriple);
				transformationNode.addChild(new XMLNode(transformation));

				XMLTriple metadataTriple = new XMLTriple("metadata", null, "pmf");
				XMLNode metadataNode = new XMLNode(metadataTriple);
				metadataNode.addChild(transformationNode);

				ud.getAnnotation().setNonRDFAnnotation(metadataNode);
			}

			return new PMFUnitDefinition(ud, transformation);
		} catch (XMLStreamException e) {
			e.printStackTrace();
			return null;
		}

	}

	public UnitDefinition getUnitDefinition() {
		return unitDefinition;
	}

	public String getTransformationName() {
		return transformationName;
	}

	// Create Groovy node
	public Node toGroovyNode() {
		Node node = new Node(null, "sbml:unitDefinition", unitDefinition.writeXMLAttributes());

		if (transformationName != null) {
			Map<String, String> annotAttrs = new HashMap<>();
			annotAttrs.put("xmlns", "http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML");
			Node annot = new Node(node, "annotation", annotAttrs);

			Map<String, String> metadataAttrs = new HashMap<>();
			Node metadataNode = new Node(annot, "pmf:metadata", metadataAttrs);

			Map<String, String> transformationAttrs = new HashMap<>();
			Node transformationNode = new Node(metadataNode, "pmf:transformation", transformationAttrs);
			transformationNode.setValue(transformationName);
		}

		// Add units
		for (Unit u : unitDefinition.getListOfUnits()) {
			node.appendNode("sbml:unit", u.writeXMLAttributes());
		}

		return node;
	}
}
