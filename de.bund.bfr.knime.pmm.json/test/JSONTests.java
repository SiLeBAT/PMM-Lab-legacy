import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.bund.bfr.knime.pmm.jsonutil.JSONAgentTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONCatalogModelTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONDataTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONDepTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONEstModelTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONIndepListTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONIndepTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONLiteratureItemTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONLiteratureListTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONMatrixTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONMdInfoTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONMiscListTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONMiscTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONParamListTest;
import de.bund.bfr.knime.pmm.jsonutil.JSONParamTest;


@RunWith(Suite.class)
@SuiteClasses({
	// jsonutil
	JSONAgentTest.class,
	JSONCatalogModelTest.class,
	JSONDataTest.class,
	JSONDepTest.class,
	JSONEstModelTest.class,
	JSONIndepTest.class,
	JSONIndepListTest.class,
	JSONLiteratureItemTest.class,
	JSONLiteratureListTest.class,
	JSONMatrixTest.class,
	JSONMdInfoTest.class,
	JSONMiscTest.class,
	JSONMiscListTest.class,
	JSONParamListTest.class,
	JSONParamTest.class
})
public class JSONTests {

}
