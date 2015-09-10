package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.annotation.MetadataAnnotation;
import de.bund.bfr.knime.pmm.sbmlutil.Metadata;

public class MetadataAnnotationTest {

	@Test
	public void test() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertEquals(metadata, ma2.getMetadata());
	}

	@Test
	public void testMissingGivenName() {
		Metadata metadata = new Metadata("", "doe", "doe", null, "Tue Sep 01 02:00:00 CEST 2015",
				"EXPERIMENTAL_DATA");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getGivenName().isEmpty());
	}

	@Test
	public void testMissingFamilyName() {
		Metadata metadata = new Metadata("doe", "", "doe", null, "Tue Sep 01 02:00:00 CEST 2015",
				"EXPERIMENTAL_DATA");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getFamilyName().isEmpty());
	}
	
	@Test
	public void testMissingContact() {
		Metadata metadata = new Metadata("doe", "doe", "", null, "Tue Sep 01 02:00:00 CEST 2015",
				"EXPERIMENTAL_DATA");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getContact().isEmpty());
	}

	@Test
	public void testMissingCreatedDate() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "", "Tue Sep 01 02:00:00 CEST 2015",
				"EXPERIMENTAL_DATA");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getCreatedDate().isEmpty());
	}

	@Test
	public void testMissingModifiedDate() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015", null,
				"EXPERIMENTAL_DATA");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getModifiedDate().isEmpty());
	}
	
	@Test
	public void testMissingType() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", null);
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getType().isEmpty());
	}

}
