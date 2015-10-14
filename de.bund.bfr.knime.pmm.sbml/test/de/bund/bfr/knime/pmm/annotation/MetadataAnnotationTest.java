package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.annotation.MetadataAnnotation;
import de.bund.bfr.knime.pmm.sbmlutil.Metadata;

public class MetadataAnnotationTest {

	/**
	 * Tests equality for a MetadataAnnotation with given name, family name,
	 * contact, created date, modified date, and type.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void test() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertEquals(metadata, ma2.getMetadata());
	}

	/**
	 * Tests that a MetadataAnnotation non initialized given name is an empty
	 * string.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testMissingGivenName() {
		Metadata metadata = new Metadata("", "doe", "doe", "", "Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getGivenName().isEmpty());
	}

	/**
	 * Tests that a MetadataAnnotation non initialized family name is an empty
	 * string.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testMissingFamilyName() {
		Metadata metadata = new Metadata("doe", "", "doe", "", "Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getFamilyName().isEmpty());
	}

	/**
	 * Tests that a MetadataAnnotation non initialized contact is an empty
	 * string.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testMissingContact() {
		Metadata metadata = new Metadata("doe", "doe", "", "", "Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getContact().isEmpty());
	}

	/**
	 * Tests that a MetadataAnnotation non initialized created date is an empty
	 * string.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testMissingCreatedDate() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "", "Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getCreatedDate().isEmpty());
	}

	/**
	 * Tests that a MetadataAnnotation non initialized modified date is an empty
	 * string.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testMissingModifiedDate() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015", "",
				"EXPERIMENTAL_DATA");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getModifiedDate().isEmpty());
	}

	/**
	 * Tests that a MetadataAnnotation non initialized type is an empty string.
	 */
	@SuppressWarnings("static-method")
	@Test
	public void testMissingType() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertTrue(ma2.getMetadata().getType().isEmpty());
	}

}
