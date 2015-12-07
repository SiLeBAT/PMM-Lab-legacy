package de.bund.bfr.pmf.sbml;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.bund.bfr.pmf.ModelType;

public class MetadataAnnotationTest {

	private Metadata exampleMetadata;

	@Before
	public void setUp() {
		exampleMetadata = SBMLFactory.createMetadata("doe", "doe", "doe", "Wed Sep 02 02:00:00 CEST 2015",
				"Wed Sep 02 02:00:00 CEST 2015", ModelType.EXPERIMENTAL_DATA, "CC", "www.bfr.bund.de");
	}

	@Test
	public void testCreator() {
		Metadata metadata = SBMLFactory.createMetadata();
		metadata.setGivenName(exampleMetadata.getGivenName());
		metadata.setFamilyName(exampleMetadata.getFamilyName());
		metadata.setContact(exampleMetadata.getContact());

		MetadataAnnotation annotFromMetadata = new MetadataAnnotation(metadata);
		MetadataAnnotation annotFromOtherAnnotation = new MetadataAnnotation(annotFromMetadata.getAnnotation());
		assertEquals(metadata, annotFromOtherAnnotation.getMetadata());
	}

	@Test
	public void testCreatedDate() {
		Metadata metadata = SBMLFactory.createMetadata();
		metadata.setCreatedDate(exampleMetadata.getCreatedDate());

		MetadataAnnotation annotFromMetadata = new MetadataAnnotation(metadata);
		MetadataAnnotation annotFromOtherAnnotation = new MetadataAnnotation(annotFromMetadata.getAnnotation());
		assertEquals(metadata, annotFromOtherAnnotation.getMetadata());
	}

	@Test
	public void testModifiedDate() {
		Metadata metadata = SBMLFactory.createMetadata();
		metadata.setModifiedDate(exampleMetadata.getModifiedDate());

		MetadataAnnotation annotFromMetadata = new MetadataAnnotation(metadata);
		MetadataAnnotation annotFromOtherAnnotation = new MetadataAnnotation(annotFromMetadata.getAnnotation());
		assertEquals(metadata, annotFromOtherAnnotation.getMetadata());
	}

	@Test
	public void testType() {
		Metadata metadata = SBMLFactory.createMetadata();
		metadata.setType(exampleMetadata.getType());

		MetadataAnnotation annotFromMetadata = new MetadataAnnotation(metadata);
		MetadataAnnotation annotFromOtherAnnotation = new MetadataAnnotation(annotFromMetadata.getAnnotation());
		assertEquals(metadata, annotFromOtherAnnotation.getMetadata());
	}

	@Test
	public void testRights() {
		Metadata metadata = SBMLFactory.createMetadata();
		metadata.setRights(exampleMetadata.getRights());

		MetadataAnnotation annotFromMetadata = new MetadataAnnotation(metadata);
		MetadataAnnotation annotFromOtherAnnotation = new MetadataAnnotation(annotFromMetadata.getAnnotation());
		assertEquals(metadata, annotFromOtherAnnotation.getMetadata());
	}

	@Test
	public void testReferenceLink() {
		Metadata metadata = SBMLFactory.createMetadata();
		metadata.setReferenceLink(exampleMetadata.getReferenceLink());

		MetadataAnnotation annotFromMetadata = new MetadataAnnotation(metadata);
		MetadataAnnotation annotFromOtherAnnotation = new MetadataAnnotation(annotFromMetadata.getAnnotation());
		assertEquals(metadata, annotFromOtherAnnotation.getMetadata());
	}
}
