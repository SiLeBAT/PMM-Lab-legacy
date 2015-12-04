/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.pmf.numl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.bund.bfr.pmf.sbml.ModelVariable;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.bund.bfr.pmf.sbml.PMFUnitDefinition;
import de.bund.bfr.pmf.sbml.SBMLFactory;

public class ConcentrationOntology {

	static final String ELEMENT_NAME = "ontologyTerm";
	private static final String ANNOTATION = "annotation";

	public static final String ID = "concentration";
	public static final String TERM = "concentration";
	public static final String SOURCE_TERM_ID = "SBO:0000196";
	public static final String URI = "http://www.ebi.ac.uk/sbo/";

	private static final String ID_TAG = "id";
	private static final String TERM_TAG = "term";
	private static final String SOURCE_TERM_ID_TAG = "sourceTermId";
	private static final String URI_TAG = "ontologyURI";

	private PMFUnitDefinition unitDefinition;
	private PMFCompartment compartment;
	private PMFSpecies species;

	public ConcentrationOntology(PMFUnitDefinition unitDefinition, PMFCompartment compartment, PMFSpecies species) {
		this.unitDefinition = unitDefinition;
		this.compartment = compartment;
		this.species = species;
	}

	public ConcentrationOntology(Element node) {
		NodeList annotationNodes = node.getElementsByTagName(ANNOTATION);
		Element annotationNode = (Element) annotationNodes.item(0);

		NodeList unitNodes = annotationNode.getElementsByTagName(UnitDefinitionNuMLNode.TAG);
		Element unitNode = (Element) unitNodes.item(0);
		UnitDefinitionNuMLNode unitNuMLNode = new UnitDefinitionNuMLNode(unitNode);
		unitDefinition = unitNuMLNode.toPMFUnitDefinition();

		NodeList compartmentNodes = annotationNode.getElementsByTagName(CompartmentNuMLNode.TAG);
		Element compartmentNode = (Element) compartmentNodes.item(0);
		CompartmentNuMLNode compartmentNuMLNode = new CompartmentNuMLNode(compartmentNode);
		compartment = compartmentNuMLNode.toPMFCompartment();

		NodeList speciesNodes = annotationNode.getElementsByTagName(SpeciesNuMLNode.TAG);
		Element speciesNode = (Element) speciesNodes.item(0);
		SpeciesNuMLNode speciesNuMLNode = new SpeciesNuMLNode(speciesNode);
		species = speciesNuMLNode.toPMFSpecies();
	}

	public PMFUnitDefinition getUnitDefinition() {
		return unitDefinition;
	}

	public PMFCompartment getCompartment() {
		return compartment;
	}

	public PMFSpecies getSpecies() {
		return species;
	}

	@Override
	public String toString() {
		String string = String.format("OntologyTerm [id=%s, term=%s, sourceTermId=%s, ontologyURI=%s]", ID, TERM,
				SOURCE_TERM_ID, URI);
		return string;
	}

	@Override
	public boolean equals(Object obj) {
		ConcentrationOntology other = (ConcentrationOntology) obj;
		return unitDefinition.equals(other.unitDefinition) && compartment.equals(other.compartment)
				&& species.equals(other.species);
	}

	public Element toNode(Document doc) {
		Element node = doc.createElement(ELEMENT_NAME);
		node.setAttribute(ID_TAG, ID);
		node.setAttribute(TERM_TAG, TERM);
		node.setAttribute(SOURCE_TERM_ID_TAG, SOURCE_TERM_ID);
		node.setAttribute(URI_TAG, URI);

		Element annotation = doc.createElement(ANNOTATION);
		node.appendChild(annotation);

		UnitDefinitionNuMLNode unitNuMLNode = new UnitDefinitionNuMLNode(unitDefinition, doc);
		annotation.appendChild(unitNuMLNode.node);

		CompartmentNuMLNode compartmentNuMLNode = new CompartmentNuMLNode(compartment, doc);
		annotation.appendChild(compartmentNuMLNode.node);

		SpeciesNuMLNode speciesNuMLNode = new SpeciesNuMLNode(species, doc);
		annotation.appendChild(speciesNuMLNode.node);

		return node;
	}
}

class CompartmentNuMLNode {

	static final String TAG = "sbml:compartment";
	private static final String ANNOTATION = "annotation";
	private static final String METADATA = "pmf:metadata";

	private static final String SOURCE_TAG = "dc:source";
	private static final String DETAIL_TAG = "pmmlab:detail";
	private static final String VARIABLE_TAG = "pmmlab:modelVariable";

	private static final String ID_ATTR = "id";
	private static final String NAME_ATTR = "name";

	Element node;

	public CompartmentNuMLNode(PMFCompartment compartment, Document doc) {

		node = doc.createElement(TAG);
		node.setAttribute(ID_ATTR, compartment.getId());
		node.setAttribute(NAME_ATTR, compartment.getName());

		if (compartment.isSetPMFCode() || compartment.isSetDetail() || compartment.isSetModelVariables()) {
			Element annotation = doc.createElement(ANNOTATION);
			node.appendChild(annotation);

			Element metadata = doc.createElement(METADATA);
			annotation.appendChild(metadata);

			if (compartment.isSetPMFCode()) {
				Element pmfCodeNode = doc.createElement(SOURCE_TAG);
				pmfCodeNode.setTextContent(compartment.getPMFCode());
				metadata.appendChild(pmfCodeNode);
			}

			if (compartment.isSetDetail()) {
				Element detailNode = doc.createElement(DETAIL_TAG);
				detailNode.setTextContent(compartment.getDetail());
				metadata.appendChild(detailNode);
			}

			if (compartment.isSetModelVariables()) {
				for (ModelVariable modelVariable : compartment.getModelVariables()) {
					Element modelVariableNode = doc.createElement(VARIABLE_TAG);
					modelVariableNode.setAttribute("name", modelVariable.getName());
					if (modelVariable.getValue() != null) {
						modelVariableNode.setAttribute("value", Double.toString(modelVariable.getValue()));
					}
					metadata.appendChild(modelVariableNode);
				}
			}

		}
	}

	public CompartmentNuMLNode(Element node) {
		this.node = node;
	}

	public PMFCompartment toPMFCompartment() {
		String id = node.getAttribute(ID_ATTR);
		String name = node.getAttribute(NAME_ATTR);

		NodeList annotationNodes = node.getElementsByTagName(ANNOTATION);
		if (annotationNodes.getLength() == 0) {
			PMFCompartment compartment = SBMLFactory.createPMFCompartment(id, name);
			return compartment;
		}

		Element annotationNode = (Element) annotationNodes.item(0);
		NodeList metadataNodes = annotationNode.getElementsByTagName(METADATA);
		Element metadataNode = (Element) metadataNodes.item(0);

		String pmfCode = null;
		NodeList pmfCodeNodes = metadataNode.getElementsByTagName(SOURCE_TAG);
		if (pmfCodeNodes.getLength() == 1) {
			Element pmfCodeNode = (Element) pmfCodeNodes.item(0);
			pmfCode = pmfCodeNode.getTextContent();
		}

		String detail = null;
		NodeList detailNodes = metadataNode.getElementsByTagName(DETAIL_TAG);
		if (detailNodes.getLength() == 1) {
			Element detailNode = (Element) detailNodes.item(0);
			detail = detailNode.getTextContent();
		}

		ModelVariable[] modelVariables;
		NodeList varNodes = metadataNode.getElementsByTagName(VARIABLE_TAG);
		if (varNodes.getLength() == 0) {
			modelVariables = null;
		} else {
			modelVariables = new ModelVariable[varNodes.getLength()];
			for (int i = 0; i < varNodes.getLength(); i++) {
				Element varNode = (Element) varNodes.item(i);
				String varName = varNode.getAttribute("name");
				Double varValue;
				if (varNode.hasAttribute("value")) {
					varValue = Double.parseDouble(varNode.getAttribute("value"));
				} else {
					varValue = null;
				}
				modelVariables[i] = new ModelVariable(varName, varValue);
			}
		}

		PMFCompartment compartment = SBMLFactory.createPMFCompartment(id, name, pmfCode, detail, modelVariables);
		return compartment;
	}
}

class SpeciesNuMLNode {

	public static final String TAG = "sbml:species";
	private static final String ANNOTATION = "annotation";
	private static final String METADATA = "pmf:metadata";

	private static final String BOUNDARY_CONDITION_ATTR = "boundaryCondition";
	private static final String COMPARTMENT_ATTR = "compartment";
	private static final String CONSTANT_ATTR = "constant";
	private static final String HAS_ONLY_SUBSTANCE_UNITS_ATTR = "hasOnlySubstanceUnits";
	private static final String ID_ATTR = "id";
	private static final String NAME_ATTR = "name";
	private static final String SUBSTANCE_UNITS_ATTR = "substanceUnits";

	private static final String SOURCE_TAG = "dc:source";
	private static final String DETAIL_TAG = "pmmmlab:detail";
	private static final String DESCRIPTION_TAG = "pmmlab:description";

	Element node;

	public SpeciesNuMLNode(PMFSpecies species, Document doc) {

		node = doc.createElement(TAG);
		node.setAttribute(BOUNDARY_CONDITION_ATTR, Boolean.toString(PMFSpecies.BOUNDARY_CONDITION));
		node.setAttribute(CONSTANT_ATTR, Boolean.toString(PMFSpecies.CONSTANT));
		node.setAttribute(HAS_ONLY_SUBSTANCE_UNITS_ATTR, Boolean.toString(PMFSpecies.ONLY_SUBSTANCE_UNITS));
		node.setAttribute(COMPARTMENT_ATTR, species.getCompartment());
		node.setAttribute(ID_ATTR, species.getId());
		node.setAttribute(NAME_ATTR, species.getName());
		node.setAttribute(SUBSTANCE_UNITS_ATTR, species.getUnits());

		if (species.isSetCombaseCode() || species.isSetDetail() || species.isSetDescription()) {
			Element annotation = doc.createElement(ANNOTATION);
			node.appendChild(annotation);

			Element metadata = doc.createElement(METADATA);
			annotation.appendChild(metadata);

			if (species.isSetCombaseCode()) {
				Element combaseCodeNode = doc.createElement(SOURCE_TAG);
				combaseCodeNode.setTextContent(species.getCombaseCode());
				metadata.appendChild(combaseCodeNode);
			}

			if (species.isSetDetail()) {
				Element detailNode = doc.createElement(DETAIL_TAG);
				detailNode.setTextContent(species.getDetail());
				metadata.appendChild(detailNode);
			}

			if (species.isSetDescription()) {
				Element descNode = doc.createElement(DESCRIPTION_TAG);
				descNode.setTextContent(species.getDescription());
				metadata.appendChild(descNode);
			}
		}
	}

	public SpeciesNuMLNode(Element node) {
		this.node = node;
	}

	public PMFSpecies toPMFSpecies() {
		String id = node.getAttribute(ID_ATTR);
		String name = node.getAttribute(NAME_ATTR);
		String compartment = node.getAttribute(COMPARTMENT_ATTR);
		String substanceUnits = node.getAttribute(SUBSTANCE_UNITS_ATTR);
		PMFSpecies species = SBMLFactory.createPMFSpecies(compartment, id, name, substanceUnits);

		NodeList annotationNodes = node.getElementsByTagName(ANNOTATION);
		if (annotationNodes.getLength() == 1) {
			Element annotationNode = (Element) annotationNodes.item(0);

			NodeList metadataNodes = annotationNode.getElementsByTagName(METADATA);
			Element metadataNode = (Element) metadataNodes.item(0);

			NodeList combaseCodeNodes = metadataNode.getElementsByTagName(SOURCE_TAG);
			if (combaseCodeNodes.getLength() == 1) {
				Element combaseCodeNode = (Element) combaseCodeNodes.item(0);
				species.setCombaseCode(combaseCodeNode.getTextContent());
			}

			NodeList detailNodes = metadataNode.getElementsByTagName(DETAIL_TAG);
			if (detailNodes.getLength() == 1) {
				Element detailNode = (Element) detailNodes.item(0);
				species.setDetail(detailNode.getTextContent());
			}

			NodeList descriptionNodes = metadataNode.getElementsByTagName(DESCRIPTION_TAG);
			if (descriptionNodes.getLength() == 1) {
				Element descriptionNode = (Element) descriptionNodes.item(0);
				species.setDescription(descriptionNode.getTextContent());
			}
		}

		return species;
	}
}