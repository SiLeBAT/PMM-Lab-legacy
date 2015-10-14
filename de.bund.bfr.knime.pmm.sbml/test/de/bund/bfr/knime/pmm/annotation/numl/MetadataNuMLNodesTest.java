package de.bund.bfr.knime.pmm.annotation.numl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.bund.bfr.knime.pmm.sbmlutil.Metadata;

public class MetadataNuMLNodesTest {

	@SuppressWarnings("static-method")
	@Test
	public void test() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA", "CC BY", "areference.com");
		MetadataNuMLNodes nodes = new MetadataNuMLNodes(metadata);
		Metadata metadata2 = nodes.toMetadata();
		assertEquals(metadata, metadata2);
	}

}
