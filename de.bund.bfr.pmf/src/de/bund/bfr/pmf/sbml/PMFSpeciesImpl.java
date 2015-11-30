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

import org.sbml.jsbml.Species;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

public class PMFSpeciesImpl implements PMFSpecies {

	private static final int LEVEL = 3;
	private static final int VERSION = 1;

	private static final String SOURCE_NS = "dc";
	private static final String SOURCE_TAG = "source";
	private static final String DETAIL_NS = "pmmlab";
	private static final String DETAIL_TAG = "detail";
	private static final String DESC_NS = "pmmlab";
	private static final String DESC_TAG = "desc";

	private static final String METADATA_NS = "pmf";
	private static final String METADATA_TAG = "metadata";

	private Species species;
	private String combaseCode;
	private String detail;
	private String description;

	/** Creates a PMFSpeciesImpl instance from an Species. */
	public PMFSpeciesImpl(Species species) {
		this.species = species;
		if (species.getAnnotation().isSetNonRDFannotation()) {
			// Parses annotation
			XMLNode metadata = species.getAnnotation().getNonRDFannotation().getChildElement(METADATA_TAG, "");

			// Gets CAS number
			XMLNode sourceNode = metadata.getChildElement(SOURCE_TAG, "");
			if (sourceNode != null) {
				String wholeReference = sourceNode.getChild(0).getCharacters();
				combaseCode = wholeReference.substring(wholeReference.lastIndexOf('/') + 1);
			}

			// Gets description
			XMLNode detailNode = metadata.getChildElement(DETAIL_TAG, "");
			if (detailNode != null) {
				detail = detailNode.getChild(0).getCharacters();
			}

			// Gets dep description
			XMLNode descNode = metadata.getChildElement(DESC_TAG, "");
			if (descNode != null) {
				description = descNode.getChild(0).getCharacters();
			}
		}
	}

	/**
	 * Creates a PMFSpeciesImpl instance from a compartment, id, name,
	 * substanceUnits, combaseCode, detail and description.
	 */
	public PMFSpeciesImpl(String compartment, String id, String name, String substanceUnits, String combaseCode,
			String detail, String description) {
		species = new Species(id, name, LEVEL, VERSION);
		species.setCompartment(compartment);
		species.setSubstanceUnits(substanceUnits);
		species.setBoundaryCondition(BOUNDARY_CONDITION);
		species.setConstant(CONSTANT);
		species.setHasOnlySubstanceUnits(ONLY_SUBSTANCE_UNITS);

		if (combaseCode != null || detail != null || description != null) {
			// Builds PMF container
			XMLNode metadataNode = new XMLNode(new XMLTriple(METADATA_TAG, null, METADATA_NS));
			species.getAnnotation().setNonRDFAnnotation(metadataNode);

			// Builds reference tag
			if (combaseCode != null) {
				XMLNode refNode = new XMLNode(new XMLTriple(SOURCE_TAG, null, SOURCE_NS));
				refNode.addChild(new XMLNode("http://identifiers.org/ncim/" + combaseCode));
				metadataNode.addChild(refNode);
				this.combaseCode = combaseCode;
			}

			// Builds detail tag
			if (detail != null) {
				XMLTriple detailTriple = new XMLTriple(DETAIL_TAG, null, DETAIL_NS);
				XMLNode detailNode = new XMLNode(detailTriple);
				detailNode.addChild(new XMLNode(detail));
				metadataNode.addChild(detailNode);
				this.detail = detail;
			}

			// Builds dep description tag
			if (description != null) {
				XMLTriple descTriple = new XMLTriple(DESC_TAG, null, DESC_NS);
				XMLNode descNode = new XMLNode(descTriple);
				descNode.addChild(new XMLNode(description));
				metadataNode.addChild(descNode);
				this.description = description;
			}
		}
	}

	/**
	 * Creates a PMFSpeciesImpl instance from a compartment, id, name and
	 * substanceUnits.
	 */
	public PMFSpeciesImpl(String compartment, String id, String name, String substanceUnits) {
		this(compartment, id, name, substanceUnits, null, null, null);
	}

	/** {@inheritDoc} */
	public Species getSpecies() {
		return species;
	}

	/** {@inheritDoc} */
	public String getCompartment() {
		return species.getCompartment();
	}

	/** {@inheritDoc} */
	public String getId() {
		return species.getId();
	}

	/** {@inheritDoc} */
	public String getName() {
		return species.getName();
	}

	/** {@inheritDoc} */
	public String getUnits() {
		return species.getUnits();
	}

	/** {@inheritDoc} */
	public String getCombaseCode() {
		return combaseCode;
	}

	/** {@inheritDoc} */
	public String getDetail() {
		return detail;
	}

	/** {@inheritDoc} */
	public String getDescription() {
		return description;
	}

	/** {@inheritDoc} */
	public void setCompartment(String compartment) {
		species.setCompartment(compartment);
	}

	/** {@inheritDoc} */
	public void setId(String id) {
		species.setId(id);
	}

	/** {@inheritDoc} */
	public void setName(String name) {
		species.setName(name);
	}

	/** {@inheritDoc} */
	public void setUnits(String units) {
		species.setUnits(units);
	}

	/** {@inheritDoc} */
	public void setCombaseCode(String combaseCode) {
		if (combaseCode != null && !combaseCode.isEmpty()) {
			this.combaseCode = combaseCode;
		}
	}

	/** {@inheritDoc} */
	public void setDetail(String detail) {
		if (detail != null && !detail.isEmpty()) {
			this.detail = detail;
		}
	}

	/** {@inheritDoc} */
	public void setDescription(String description) {
		if (description != null && !description.isEmpty()) {
			this.description = description;
		}
	}

	/** {@inheritDoc} */
	public boolean isSetCombaseCode() {
		return combaseCode != null;
	}

	/** {@inheritDoc} */
	public boolean isSetDetail() {
		return detail != null;
	}

	/** {@inheritDoc} */
	public boolean isSetDescription() {
		return description != null;
	}

	@Override
	public String toString() {
		String string = String.format("Species [compartment=%s, id=%s, name=%s, substanceUnits=%s]",
				species.getCompartment(), species.getId(), species.getName(), species.getSubstanceUnits());
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

		PMFSpeciesImpl other = (PMFSpeciesImpl) obj;
		if (!species.getCompartment().equals(other.species.getCompartment()))
			return false;

		if (!species.getId().equals(other.species.getId()))
			return false;

		if (!species.getName().equals(other.species.getName()))
			return false;

		if (!species.getSubstanceUnits().equals(other.species.getSubstanceUnits()))
			return false;

		if (combaseCode != null && other.combaseCode != null && !combaseCode.equals(other.combaseCode))
			return false;

		if (detail != null && other.detail != null && !detail.equals(other.detail))
			return false;

		if (description != null && other.description != null && !description.equals(other.description))
			return false;

		return true;
	}
}
