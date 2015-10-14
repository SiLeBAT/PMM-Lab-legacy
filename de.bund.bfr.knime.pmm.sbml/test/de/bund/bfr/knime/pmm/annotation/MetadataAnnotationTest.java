package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.annotation.MetadataAnnotation;
import de.bund.bfr.knime.pmm.sbmlutil.Metadata;

public class MetadataAnnotationTest {

	@SuppressWarnings("static-method")
	@Test
	public void test() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA", "CC BY", "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertEquals(metadata, ma2.getMetadata());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingGivenName() {
		Metadata metadata = new Metadata("", "doe", "doe", "", "Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA",
				"CC BY", "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getGivenName().isEmpty());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingFamilyName() {
		Metadata metadata = new Metadata("doe", "", "doe", "", "Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA",
				"CC BY", "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getFamilyName().isEmpty());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingContact() {
		Metadata metadata = new Metadata("doe", "doe", "", "", "Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA",
				"CC BY", "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getContact().isEmpty());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingCreatedDate() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "", "Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA",
				"CC BY", "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getCreatedDate().isEmpty());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingModifiedDate() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015", "", "EXPERIMENTAL_DATA",
				"CC BY", "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getModifiedDate().isEmpty());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingType() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "", "CC BY", "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getType().isEmpty());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingRights() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "Tue Sep 01 02:00:00 CEST 2015", "", "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getRights().isEmpty());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingReferenceLink() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "Tue Sep 01 02:00:00 CEST 2015", "CC BY", "");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getReferenceLink().isEmpty());
	}
}
