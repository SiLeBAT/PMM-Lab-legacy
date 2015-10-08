import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.bund.bfr.knime.pmm.jsonutil.JSONAgentTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONCatalogModelTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONDepTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONEstModelTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONIndepTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONLiteratureItemTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONMatrixTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONMdInfoTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONMiscTest;


@RunWith(Suite.class)
@SuiteClasses({
	// jsonutil
	JSONAgentTest.class,
	JSONCatalogModelTest.class,
	JSONDepTest.class,
	JSONEstModelTest.class,
	JSONIndepTest.class,
	JSONLiteratureItemTest.class,
	JSONMatrixTest.class,
	JSONMdInfoTest.class,
	JSONMiscTest.class
})
public class JSONTests {

}
