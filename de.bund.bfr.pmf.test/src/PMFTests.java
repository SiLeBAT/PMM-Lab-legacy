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


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.bund.bfr.pmf.ModelClassTest;
import de.bund.bfr.pmf.numl.AtomicDescriptionTest;
import de.bund.bfr.pmf.numl.AtomicValueTest;
import de.bund.bfr.pmf.numl.ConcentrationOntologyTest;
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
import de.bund.bfr.pmf.sbml.MetadataImplTest;
import de.bund.bfr.pmf.sbml.ModelVariableTest;
import de.bund.bfr.pmf.sbml.PMFCoefficientImplTest;
import de.bund.bfr.pmf.sbml.PMFUnitTest;
import de.bund.bfr.pmf.sbml.PrimaryModelNodeTest;
import de.bund.bfr.pmf.sbml.ReferenceImplTest;
import de.bund.bfr.pmf.sbml.ReferenceTypeTest;
import de.bund.bfr.pmf.sbml.UncertaintiesImplTest;

/**
 * @author Miguel Alba
 */
@RunWith(Suite.class)
@SuiteClasses({
	// de.bund.bfr.pmf.numl
	AtomicDescriptionTest.class,
	AtomicValueTest.class,
	ConcentrationOntologyTest.class,
	ResultComponentTest.class,
	TimeOntologyTest.class,
	TupleDescriptionTest.class,
	TupleTest.class,
	UnitDefinitionNuMLNodeTest.class,
	// de.bund.bfr.pmf.sbml
	CondIdNodeTest.class,
	CorrelationTest.class,
	DataSourceNodeTest.class,
	GlobalModelIdNodeTest.class,
	LimitsTest.class,
	MetadataImplTest.class,
	ModelVariableTest.class,
	ReferenceImplTest.class,
	ReferenceTypeTest.class,
	PMFCoefficientImplTest.class,
	PMFUnitTest.class,
	PrimaryModelNodeTest.class,
	UncertaintiesImplTest.class,
	// de.bund.bfr.pmf
	ModelClassTest.class
})
public class PMFTests {
}
