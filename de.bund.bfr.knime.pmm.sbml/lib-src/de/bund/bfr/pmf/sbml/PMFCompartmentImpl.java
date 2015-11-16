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
package de.bund.bfr.pmf.sbml;

import java.util.Arrays;
import java.util.List;

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.xml.XMLAttributes;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

public class PMFCompartmentImpl implements PMFCompartment {

	private static final int LEVEL = 3;
	private static final int VERSION = 1;

	private static final String CODE_TAG = "source"; // PMF code tag
	private static final String CODE_NS = "dc"; // PMF code namespace
	private static final String DETAIL_TAG = "detail"; // Matrix detail tag
	private static final String DETAIL_NS = "pmmlab"; // Matrix detail namespace
	private static final String VAR_TAG = "environment"; // Model variable tag
	private static final String VAR_NS = "pmmlab"; // Model variable namespace
	private static final String ATTRIBUTE_NAME = "name";
	private static final String ATTRIBUTE_VALUE = "value";

	private static final String METADATA_NS = "pmf";
	private static final String METADATA_TAG = "metadata";

	private Compartment compartment;
	private String pmfCode;
	private String detail;
	private ModelVariable[] modelVariables;

	/** Creates a PMFCompartmentImpl instance from a Compartment. */
	public PMFCompartmentImpl(Compartment compartment) {
		this.compartment = compartment;
		if (compartment.isSetAnnotation()) {
			// Parses annotation
			XMLNode metadata = compartment.getAnnotation().getNonRDFannotation().getChildElement(METADATA_TAG, "");

			// Gets PMF code
			XMLNode codeNode = metadata.getChildElement(CODE_TAG, "");
			if (codeNode != null) {
				pmfCode = codeNode.getChild(0).getCharacters();
			}

			// Gets details
			XMLNode detailsNode = metadata.getChildElement(DETAIL_TAG, "");
			if (detailsNode != null) {
				detail = detailsNode.getChild(0).getCharacters();
			}

			// Gets model variables
			List<XMLNode> varNodes = metadata.getChildElements(VAR_TAG, "");
			if (!varNodes.isEmpty()) {
				int numVarNodes = varNodes.size();
				modelVariables = new ModelVariable[numVarNodes];

				for (int i = 0; i < numVarNodes; i++) {
					XMLNode varNode = varNodes.get(i);
					XMLAttributes attrs = varNode.getAttributes();

					String name = attrs.getValue(ATTRIBUTE_NAME);
					Double value = attrs.hasAttribute(ATTRIBUTE_VALUE)
							? Double.parseDouble(attrs.getValue(ATTRIBUTE_VALUE)) : null;
					modelVariables[i] = new ModelVariable(name, value);
				}
			}
		}
	}

	/**
	 * Creates a PMFCompartmentImpl instance from an id, name, PMF code, detail and model variables.
	 */
	public PMFCompartmentImpl(String id, String name, String pmfCode, String detail, ModelVariable[] modelVariables) {
		compartment = new Compartment(id, name, LEVEL, VERSION);
		compartment.setConstant(true);
		this.pmfCode = pmfCode;
		this.detail = detail;
		this.modelVariables = modelVariables;

		// Builds metadata node
		XMLTriple metadataTriple = new XMLTriple(METADATA_TAG, "", METADATA_NS);
		XMLNode metadataNode = new XMLNode(metadataTriple);

		// Creates annotation for the PMF code
		if (pmfCode != null) {
			XMLNode codeNode = new XMLNode(new XMLTriple(CODE_TAG, null, CODE_NS));
			codeNode.addChild(new XMLNode(pmfCode));
			metadataNode.addChild(codeNode);
		}

		// Creates annotation for the matrix details
		if (detail != null) {
			XMLTriple detailsTriple = new XMLTriple(DETAIL_TAG, null, DETAIL_NS);
			XMLNode detailsNode = new XMLNode(detailsTriple);
			detailsNode.addChild(new XMLNode(detail));
			metadataNode.addChild(detailsNode);
		}

		// Creates annotations for model variables (Temperature, pH, aW)
		if (modelVariables != null) {
			for (ModelVariable modelVariable : modelVariables) {
				XMLAttributes attrs = new XMLAttributes();
				attrs.add(ATTRIBUTE_NAME, modelVariable.getName());

				Double value = modelVariable.getValue();
				if (value != null) {
					attrs.add(ATTRIBUTE_VALUE, Double.toString(value));
				}

				XMLTriple varTriple = new XMLTriple(VAR_TAG, null, VAR_NS);
				metadataNode.addChild(new XMLNode(varTriple, attrs));
			}
		}

		compartment.getAnnotation().setNonRDFAnnotation(metadataNode);
	}

	/** Creates a PMFCompartmentImpl from an id and name. */
	public PMFCompartmentImpl(String id, String name) {
		this(id, name, null, null, null);
	}

	/** {@inheritDoc} */
	public String getId() {
		return compartment.getId();
	}

	/** {@inheritDoc} */
	public String getName() {
		return compartment.getName();
	}

	/** {@inheritDoc} */
	public String getPMFCode() {
		return pmfCode;
	}

	/** {@inheritDoc} */
	public String getDetail() {
		return detail;
	}

	/** {@inheritDoc} */
	public ModelVariable[] getModelVariables() {
		return modelVariables;
	}

	/** {@inheritDoc} */
	public Compartment getCompartment() {
		return compartment;
	}

	/** {@inheritDoc} */
	public void setId(String id) {
		compartment.setId(id);
	}

	/** {@inheritDoc} */
	public void setName(String name) {
		compartment.setName(name);
	}

	/** {@inheritDoc} */
	public void setPMFCode(String pmfCode) {
		this.pmfCode = pmfCode;
	}

	/** {@inheritDoc} */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/** {@inheritDoc} */
	public void setModelVariables(ModelVariable[] modelVariables) {
		this.modelVariables = modelVariables;
	}

	public boolean isSetPMFCode() {
		return pmfCode != null;
	}

	/** {@inheritDoc} */
	public boolean isSetDetail() {
		return detail != null;
	}

	/** {@inheritDoc} */
	public boolean isSetModelVariables() {
		return modelVariables != null;
	}

	@Override
	public String toString() {
		String string = String.format("Compartment [id=%s, name=%s]", compartment.getId(), compartment.getName());
		return string;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		PMFCompartmentImpl other = (PMFCompartmentImpl) obj;

		if (!compartment.getId().equals(other.compartment.getId()))
			return false;

		if (!compartment.getName().equals(other.compartment.getName()))
			return false;

		if (pmfCode != null && other.pmfCode != null && !pmfCode.equals(other.pmfCode))
			return false;

		if (detail != null && other.detail != null && !detail.equals(other.detail))
			return false;

		if (modelVariables != null && other.modelVariables != null
				&& !Arrays.equals(modelVariables, other.modelVariables))
			return false;

		return true;
	}
}
