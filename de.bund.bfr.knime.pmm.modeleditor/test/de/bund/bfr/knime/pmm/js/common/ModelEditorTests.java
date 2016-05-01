package de.bund.bfr.knime.pmm.js.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AgentTest.class, AgentListTest.class, CatalogModelTest.class, DepTest.class, EstModelTest.class,
		IndepTest.class, IndepListTest.class, LiteratureListTest.class, LiteratureTest.class, MatrixTest.class,
		MdInfoTest.class, MiscTest.class, ParamTest.class, UnitTest.class })
public class ModelEditorTests {

}
