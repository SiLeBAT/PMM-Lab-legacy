import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.bund.bfr.knime.pmm.annotation.CondIDNodeTest;
import de.bund.bfr.knime.pmm.annotation.DataSourceNodeTest;
import de.bund.bfr.knime.pmm.annotation.DescriptionAnnotationTest;
import de.bund.bfr.knime.pmm.annotation.GlobalModelIdNodeTest;
import de.bund.bfr.knime.pmm.annotation.GroovyReferenceNodeTest;
import de.bund.bfr.knime.pmm.annotation.MetadataAnnotationTest;
import de.bund.bfr.knime.pmm.annotation.PrimaryModelNodeTest;
import de.bund.bfr.knime.pmm.annotation.SBMLReferenceNodeTest;
import de.bund.bfr.knime.pmm.annotation.UncertaintyNodeTest;
import de.bund.bfr.knime.pmm.sbml.sbmlutil.PMFUnitDefinitionTest;
import de.bund.bfr.knime.pmm.sbml.sbmlutil.UncertaintiesTest;
import de.bund.bfr.knime.pmm.sbmlutil.MetadataTest;

@RunWith(Suite.class)
@SuiteClasses({
	// de.bund.bfr.knime.pmm.annotation
	CondIDNodeTest.class,
	DescriptionAnnotationTest.class,
	DataSourceNodeTest.class,
	GlobalModelIdNodeTest.class,
	GroovyReferenceNodeTest.class,
	MetadataAnnotationTest.class,
	PrimaryModelNodeTest.class,
	SBMLReferenceNodeTest.class,
	UncertaintyNodeTest.class,
	// de.bund.bfr.knime.pmm.sbml.sbmlutil
	MetadataTest.class,
	PMFUnitDefinitionTest.class,
	UncertaintiesTest.class })
public class AllTests {

}
