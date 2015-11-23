import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.bund.bfr.knime.pmm.annotation.numl.AgentNuMLNodeTest;
import de.bund.bfr.knime.pmm.annotation.numl.CondIDNuMLNodeTest;
import de.bund.bfr.knime.pmm.annotation.numl.MatrixNuMLNodeTest;
import de.bund.bfr.knime.pmm.annotation.numl.MetadataNuMLNodesTest;
import de.bund.bfr.knime.pmm.annotation.numl.ReferenceNuMLNodeTest;
import de.bund.bfr.knime.pmm.annotation.sbml.AgentAnnotationTest;
import de.bund.bfr.knime.pmm.annotation.sbml.CoefficientAnnotationTest;
import de.bund.bfr.knime.pmm.annotation.sbml.CondIDNodeTest;
import de.bund.bfr.knime.pmm.annotation.sbml.DataSourceNodeTest;
import de.bund.bfr.knime.pmm.annotation.sbml.DescriptionAnnotationTest;
import de.bund.bfr.knime.pmm.annotation.sbml.GlobalModelIdNodeTest;
import de.bund.bfr.knime.pmm.annotation.sbml.MatrixAnnotationTest;
import de.bund.bfr.knime.pmm.annotation.sbml.MetadataAnnotationTest;
import de.bund.bfr.knime.pmm.annotation.sbml.Model1AnnotationTest;
import de.bund.bfr.knime.pmm.annotation.sbml.Model2AnnotationTest;
import de.bund.bfr.knime.pmm.annotation.sbml.PrimaryModelNodeTest;
import de.bund.bfr.knime.pmm.annotation.sbml.SBMLReferenceNodeTest;
import de.bund.bfr.knime.pmm.annotation.sbml.UncertaintyNodeTest;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplateImplTest;
import de.bund.bfr.knime.pmm.sbml.sbmlutil.PMFUnitDefinitionTest;
import de.bund.bfr.knime.pmm.sbml.sbmlutil.UncertaintiesTest;
import de.bund.bfr.knime.pmm.sbmlutil.MetadataTest;

@RunWith(Suite.class)
@SuiteClasses({
	// de.bund.bfr.knime.pmm.annotation
	AgentAnnotationTest.class,
	CoefficientAnnotationTest.class,
	CondIDNodeTest.class,
	DataSourceNodeTest.class,
	DescriptionAnnotationTest.class,
	GlobalModelIdNodeTest.class,
	MatrixAnnotationTest.class,
	MetadataAnnotationTest.class,
	Model1AnnotationTest.class,
	Model2AnnotationTest.class,
	PrimaryModelNodeTest.class,
	SBMLReferenceNodeTest.class,
	UncertaintyNodeTest.class,
	// de.bund.bfr.knime.pmm.annotation.numl
	AgentNuMLNodeTest.class,
	CondIDNuMLNodeTest.class,
	MatrixNuMLNodeTest.class,
	MetadataNuMLNodesTest.class,
	ReferenceNuMLNodeTest.class,
	// de.bund.bfr.knime.pmm.openfsmr
	FSMRTemplateImplTest.class,
	// de.bund.bfr.knime.pmm.sbml.sbmlutil
	MetadataTest.class,
	PMFUnitDefinitionTest.class,
	UncertaintiesTest.class
	})
public class AllTests {

}
