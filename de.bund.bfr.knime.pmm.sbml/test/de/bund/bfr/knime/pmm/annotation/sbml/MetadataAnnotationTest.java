package de.bund.bfr.knime.pmm.annotation.sbml;

import static org.junit.Assert.*;

import org.junit.Test;

import de.bund.bfr.knime.pmm.annotation.sbml.MetadataAnnotation;
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
		Metadata metadata = new Metadata(null, "doe", "doe", null, "Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA",
				"CC BY", "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertFalse(ma2.getMetadata().isGivenNameSet());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingFamilyName() {
		Metadata metadata = new Metadata("doe", null, "doe", null, "Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA",
				"CC BY", "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertFalse(ma2.getMetadata().isFamilyNameSet());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingContact() {
		Metadata metadata = new Metadata("doe", "doe", null, null, "Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA",
				"CC BY", "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertFalse(ma2.getMetadata().isContactSet());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingCreatedDate() {
		Metadata metadata = new Metadata("doe", "doe", "doe", null, "Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA",
				"CC BY", "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertFalse(ma2.getMetadata().isCreatedDateSet());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingModifiedDate() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015", "", "EXPERIMENTAL_DATA",
				"CC BY", "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertFalse(ma2.getMetadata().isModifiedDateSet());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingType() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", null, "CC BY", "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertFalse(ma2.getMetadata().isTypeSet());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingRights() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "Tue Sep 01 02:00:00 CEST 2015", null, "areference.com");
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertFalse(ma2.getMetadata().areRightsSet());
	}

	@SuppressWarnings("static-method")
	@Test
	public void testMissingReferenceLink() {
		Metadata metadata = new Metadata("doe", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "Tue Sep 01 02:00:00 CEST 2015", "CC BY", null);
		MetadataAnnotation ma = new MetadataAnnotation(metadata);
		MetadataAnnotation ma2 = new MetadataAnnotation(ma.getAnnotation());
		assertFalse(ma2.getMetadata().isReferenceLinkSet());
	}
}
