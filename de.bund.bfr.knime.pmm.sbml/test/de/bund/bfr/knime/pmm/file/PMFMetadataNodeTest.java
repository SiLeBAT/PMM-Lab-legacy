package de.bund.bfr.knime.pmm.file;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.bund.bfr.knime.pmm.sbmlutil.ModelType;

public class PMFMetadataNodeTest {

	@SuppressWarnings("static-method")
	@Test
	public void test() {
		String modelType = ModelType.TWO_STEP_TERTIARY_MODEL.name();
		Set<String> masterFiles = new HashSet<>(2);
		masterFiles.add("model0.sbml");
		masterFiles.add("model1.sbml");
		
		PMFMetadataNode node1 = new PMFMetadataNode(modelType, masterFiles);
		PMFMetadataNode node2 = new PMFMetadataNode(node1.getNode());
		
		assertEquals(modelType, node2.getModelType());
		assertEquals(masterFiles, node2.getMasterFiles());
	}
}