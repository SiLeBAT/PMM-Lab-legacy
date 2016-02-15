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


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.bund.bfr.pmf.ModelClassTest;
import de.bund.bfr.pmf.file.uri.URIFactoryTest;
import de.bund.bfr.pmf.model.ExperimentalDataTest;
import de.bund.bfr.pmf.model.ManualSecondaryModelTest;
import de.bund.bfr.pmf.model.ManualTertiaryModelTest;
import de.bund.bfr.pmf.model.OneStepSecondaryModelTest;
import de.bund.bfr.pmf.model.OneStepTertiaryModelTest;
import de.bund.bfr.pmf.model.PrimaryModelWDataTest;
import de.bund.bfr.pmf.model.PrimaryModelWODataTest;
import de.bund.bfr.pmf.model.TwoStepSecondaryModelTest;
import de.bund.bfr.pmf.model.TwoStepTertiaryModelTest;
import de.bund.bfr.pmf.numl.AtomicDescriptionTest;
import de.bund.bfr.pmf.numl.AtomicValueTest;
import de.bund.bfr.pmf.numl.ConcentrationOntologyTest;
import de.bund.bfr.pmf.numl.NuMLDocumentTest;
import de.bund.bfr.pmf.numl.ResultComponentTest;
import de.bund.bfr.pmf.numl.TimeOntologyTest;
import de.bund.bfr.pmf.numl.TupleDescriptionTest;
import de.bund.bfr.pmf.numl.TupleTest;
import de.bund.bfr.pmf.numl.UnitDefinitionNuMLNodeTest;
import de.bund.bfr.pmf.sbml.CondIdNodeTest;
import de.bund.bfr.pmf.sbml.CorrelationTest;
import de.bund.bfr.pmf.sbml.DataSourceNodeTest;
import de.bund.bfr.pmf.sbml.GlobalModelIdNodeTest;
import de.bund.bfr.pmf.sbml.LimitsTest;
import de.bund.bfr.pmf.sbml.MetadataAnnotationTest;
import de.bund.bfr.pmf.sbml.MetadataImplTest;
import de.bund.bfr.pmf.sbml.ModelRuleTest;
import de.bund.bfr.pmf.sbml.ModelVariableTest;
import de.bund.bfr.pmf.sbml.PMFCoefficientImplTest;
import de.bund.bfr.pmf.sbml.PMFCompartmentImplTest;
import de.bund.bfr.pmf.sbml.PMFSpeciesImplTest;
import de.bund.bfr.pmf.sbml.PMFUnitTest;
import de.bund.bfr.pmf.sbml.PrimaryModelNodeTest;
import de.bund.bfr.pmf.sbml.ReferenceImplTest;
import de.bund.bfr.pmf.sbml.ReferenceSBMLNodeTest;
import de.bund.bfr.pmf.sbml.ReferenceTypeTest;
import de.bund.bfr.pmf.sbml.SecDepTest;
import de.bund.bfr.pmf.sbml.SecIndepTest;
import de.bund.bfr.pmf.sbml.UncertaintiesImplTest;
import de.bund.bfr.pmf.sbml.UncertaintyNodeTest;

/**
 * @author Miguel Alba
 */
@RunWith(Suite.class)
@SuiteClasses({
    // de.bund.bfr.pmf.file
    URIFactoryTest.class,
    // de.bund.bfr.pmf.model
    ExperimentalDataTest.class, PrimaryModelWDataTest.class, PrimaryModelWODataTest.class,
    TwoStepSecondaryModelTest.class, OneStepSecondaryModelTest.class,
    ManualSecondaryModelTest.class, TwoStepTertiaryModelTest.class, OneStepTertiaryModelTest.class,
    ManualTertiaryModelTest.class,
    // de.bund.bfr.pmf.numl
    AtomicDescriptionTest.class, AtomicValueTest.class, ConcentrationOntologyTest.class,
    NuMLDocumentTest.class, ResultComponentTest.class, TimeOntologyTest.class,
    TupleDescriptionTest.class, TupleTest.class, UnitDefinitionNuMLNodeTest.class,
    // de.bund.bfr.pmf.sbml
    CondIdNodeTest.class, CorrelationTest.class, DataSourceNodeTest.class,
    GlobalModelIdNodeTest.class, LimitsTest.class, MetadataAnnotationTest.class,
    MetadataImplTest.class, ModelRuleTest.class, ModelVariableTest.class, ReferenceImplTest.class,
    ReferenceSBMLNodeTest.class, ReferenceTypeTest.class, PMFCoefficientImplTest.class,
    PMFCompartmentImplTest.class, PMFUnitTest.class, PMFSpeciesImplTest.class,
    PrimaryModelNodeTest.class, SecDepTest.class, SecIndepTest.class, UncertaintiesImplTest.class,
    UncertaintyNodeTest.class,
    // de.bund.bfr.pmf
    ModelClassTest.class})
public class PMFTests {
}
