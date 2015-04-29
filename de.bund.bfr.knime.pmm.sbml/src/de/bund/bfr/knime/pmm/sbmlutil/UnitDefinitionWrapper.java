package de.bund.bfr.knime.pmm.sbmlutil;

import java.util.HashMap;
import java.util.Map;

import groovy.util.Node;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.xml.XMLNode;

/** Wrapper class for SBML unit definitions */
public class UnitDefinitionWrapper {
	private UnitDefinition unitDefinition;

	public UnitDefinitionWrapper(UnitDefinition unitDefinition) {
		this.unitDefinition = unitDefinition;
	}

	public UnitDefinition getUnitDefinition() {
		return unitDefinition;
	}

	/**
	 * Creates a UnitDefinitionWrapper from a XML string.
	 * 
	 * @param xml
	 *            . XML string containing a valid SBML UnitDefinition.
	 * @return
	 */
	public static UnitDefinitionWrapper xmlToUnitDefinition(String xml) {
		String preXml = "<?xml version='1.0' encoding='UTF-8' standalone='no'?>"
				+ "<sbml xmlns=\"http://www.sbml.org/sbml/level3/version1/core\""
				+ " level=\"3\" version=\"1\""
				+ " xmlns:pmf=\"http://sourceforge.net/projects/microbialmodelingexchange/files/PMF-ML\""
				+ "><model id=\"ID\">" + "<listOfUnitDefinitions>";

		String postXml = "</listOfUnitDefinitions>" + "</model>" + "</sbml>";

		// Fix transformation annotation. It misses the pmf prefix.
		if (xml.indexOf("<transformation") != -1) {
			xml = xml.replaceFirst("<transformation", "<pmf:transformation");
		}
		// TODO: Remove namespace
		xml = xml
				.replaceAll(
						"xmlns=\"http://sourceforge.net/projects/microbialmodelingexchange/files/Units\"",
						"");
		System.out.println(xml);

		String totalXml = preXml + xml + postXml;

		try {
			// Create SBMLDocument and get its model
			SBMLDocument sbmlDoc = SBMLReader.read(totalXml);
			Model model = sbmlDoc.getModel();

			// Create a new UnitDefinition from XML
			UnitDefinition xmlUnitDef = model.getUnitDefinition(0);
			UnitDefinition ud = new UnitDefinition(3, 1);
			for (Unit unit : xmlUnitDef.getListOfUnits()) {
				ud.addUnit(new Unit(unit));
			}

			// Add transformation annotation
			ud.setAnnotation(xmlUnitDef.getAnnotation());

			UnitDefinitionWrapper wrapper = new UnitDefinitionWrapper(ud);
			return wrapper;
		} catch (XMLStreamException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Create Groovy node
	public Node toGroovyNode() {
		Node node = new Node(null, "sbml:unitDefinition",
				unitDefinition.writeXMLAttributes());

		// Add annotation
		if (unitDefinition.getAnnotation().getNonRDFannotation() != null) {
			XMLNode origAnnot = unitDefinition.getAnnotation()
					.getNonRDFannotation().getChild(0);

			// Get annotation attributes (name and xmlns)
			Map<String, String> attrs = new HashMap<>();
			attrs.put("xmlns",
					"http://sourceforge.net/projects/microbialmodelingexchange/files/Units");
			attrs.put("name", origAnnot.getAttrValue("name"));

			// Create Groovy annotation node
			Node annot = new Node(null, "pmf:transformation", attrs);
			node.append(annot);
		}

		// Add units
		for (Unit u : unitDefinition.getListOfUnits()) {
			node.appendNode("sbml:unit", u.writeXMLAttributes());
		}

		return node;
	}
}
