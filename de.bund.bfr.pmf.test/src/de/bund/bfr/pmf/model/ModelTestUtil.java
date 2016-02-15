/***************************************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 **************************************************************************************************/
package de.bund.bfr.pmf.model;

import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.Unit;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.numl.AtomicDescription;
import de.bund.bfr.pmf.numl.AtomicValue;
import de.bund.bfr.pmf.numl.ConcentrationOntology;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.bund.bfr.pmf.numl.ResultComponent;
import de.bund.bfr.pmf.numl.TimeOntology;
import de.bund.bfr.pmf.numl.Tuple;
import de.bund.bfr.pmf.numl.TupleDescription;
import de.bund.bfr.pmf.sbml.ModelVariable;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.bund.bfr.pmf.sbml.PMFUnit;
import de.bund.bfr.pmf.sbml.PMFUnitDefinition;
import de.bund.bfr.pmf.sbml.Reference;
import de.bund.bfr.pmf.sbml.ReferenceType;
import de.bund.bfr.pmf.sbml.SBMLFactory;

/**
 * @author Miguel Alba
 */
public class ModelTestUtil {

  private ModelTestUtil() {}

  static SBMLDocument createDummyModel() {
    return new SBMLDocument();
  }

  static NuMLDocument createDummyData() {
    return new NuMLDocument(createConcentrationOntology(), createTimeOntology(),
        createResultComponent());
  }

  private static ConcentrationOntology createConcentrationOntology() {
    final PMFUnit[] concUnits = new PMFUnit[] {new PMFUnit(1.0, 0, Unit.Kind.ITEM, 1.0),
        new PMFUnit(1.0, 0, Unit.Kind.GRAM, 1.0)};
    final PMFUnitDefinition concUnitDef =
        new PMFUnitDefinition("log10_count_g", "log10(count/g)", "log10", concUnits);

    final String compartmentId = "culture_broth_broth_culture_culture_medium";
    final String compartmentName = "culture broth, broth culture, culture medium";
    final String pmfCode = null;
    final String compartmentDetail = "broth";
    final ModelVariable[] modelVariables =
        new ModelVariable[] {new ModelVariable("Temperature", 10.0), new ModelVariable("pH", 5.63)};
    final PMFCompartment compartment = SBMLFactory.createPMFCompartment(compartmentId,
        compartmentName, pmfCode, compartmentDetail, modelVariables);

    final String speciesId = "species4024";
    final String speciesName = "salmonella spp";
    final String combaseCode = null;
    final String speciesDetail = "Salmonella spec";
    final String depDesc = null;
    final PMFSpecies species = SBMLFactory.createPMFSpecies(compartmentId, speciesId, speciesName,
        concUnitDef.getId(), combaseCode, speciesDetail, depDesc);

    return new ConcentrationOntology(concUnitDef, compartment, species);
  }

  private static TimeOntology createTimeOntology() {
    final PMFUnit[] hourUnits = new PMFUnit[] {new PMFUnit(3600, 0, Unit.Kind.SECOND, 1)};
    final PMFUnitDefinition timeUnitDef = new PMFUnitDefinition("h", "h", null, hourUnits);

    return new TimeOntology(timeUnitDef);
  }

  private static ResultComponent createResultComponent() {
    final String id = "exp1";
    final int condId = 1;
    final String combaseId = "salm25";
    final String creatorGivenName = "Jane Doe";
    final String creatorFamilyName = "Doe";
    final String creatorContact = "jane.doe@people.com";
    final String createdDate = null;
    final String modifiedDate = null;
    final ModelType modelType = ModelType.EXPERIMENTAL_DATA;
    final String notes = null;
    final String rights = "CC";

    // Create references
    final String author = "Baranyi, J.";
    final int year = 1994;
    final String title = "A dynamic approach to predicting microbial bacterial growth in food";
    final String abstractText =
        "A new member of the family of groth models described by Baranyi ...";
    final String journal = "International Journal of Food Microbiology";
    final String volume = "23";
    final String issue = "3";
    final int page = 277;
    final String website = "http://www.sciencedirect.com/science/article/pii/0168160594901570";
    final ReferenceType referenceType = ReferenceType.Paper;

    final Reference ref = SBMLFactory.createReference(author, year, title, abstractText, journal,
        volume, issue, page, null, website, referenceType, null);
    final Reference[] references = new Reference[] {ref};

    final AtomicDescription concDesc = new AtomicDescription("concentration", "concentration");
    final AtomicDescription timeDesc = new AtomicDescription("Time", "time");
    final TupleDescription tupleDescription = new TupleDescription(concDesc, timeDesc);

    final Tuple[] tuples = new Tuple[] {new Tuple(new AtomicValue(2.67), new AtomicValue(0.00)),
        new Tuple(new AtomicValue(2.91), new AtomicValue(50.88)),
        new Tuple(new AtomicValue(2.87), new AtomicValue(73.02)),
        new Tuple(new AtomicValue(3.69), new AtomicValue(103.96)),
        new Tuple(new AtomicValue(4.25), new AtomicValue(145.01))};

    return new ResultComponent(id, condId, combaseId, creatorGivenName, creatorFamilyName,
        creatorContact, createdDate, modifiedDate, modelType, rights, notes, references,
        tupleDescription, tuples);
  }
}
