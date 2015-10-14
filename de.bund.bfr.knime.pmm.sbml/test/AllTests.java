import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.bund.bfr.knime.pmm.annotation.AgentAnnotationTest;
import de.bund.bfr.knime.pmm.annotation.CoefficientAnnotationTest;
import de.bund.bfr.knime.pmm.annotation.CondIDNodeTest;
import de.bund.bfr.knime.pmm.annotation.DataSourceNodeTest;
import de.bund.bfr.knime.pmm.annotation.DescriptionAnnotationTest;
import de.bund.bfr.knime.pmm.annotation.GlobalModelIdNodeTest;
import de.bund.bfr.knime.pmm.annotation.MatrixAnnotationTest;
import de.bund.bfr.knime.pmm.annotation.MetadataAnnotationTest;
import de.bund.bfr.knime.pmm.annotation.Model1AnnotationTest;
import de.bund.bfr.knime.pmm.annotation.Model2AnnotationTest;
import de.bund.bfr.knime.pmm.annotation.PrimaryModelNodeTest;
import de.bund.bfr.knime.pmm.annotation.SBMLReferenceNodeTest;
import de.bund.bfr.knime.pmm.annotation.UncertaintyNodeTest;
import de.bund.bfr.knime.pmm.annotation.numl.AgentNuMLNodeTest;
import de.bund.bfr.knime.pmm.annotation.numl.CondIDNuMLNodeTest;
import de.bund.bfr.knime.pmm.annotation.numl.MatrixNuMLNodeTest;
import de.bund.bfr.knime.pmm.annotation.numl.MetadataNuMLNodesTest;
import de.bund.bfr.knime.pmm.annotation.numl.ReferenceNuMLNodeTest;
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
	// de.bund.bfr.knime.pmm.sbml.sbmlutil
	MetadataTest.class,
	PMFUnitDefinitionTest.class,
	UncertaintiesTest.class })
public class AllTests {

}
