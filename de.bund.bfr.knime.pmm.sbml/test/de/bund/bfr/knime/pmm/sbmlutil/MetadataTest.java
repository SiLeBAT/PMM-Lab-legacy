package de.bund.bfr.knime.pmm.sbmlutil;

import static org.junit.Assert.*;

import org.junit.Test;

public class MetadataTest {

	@Test
	public void testDifferentGivenName() {
		Metadata metadataA = new Metadata("John", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		Metadata metadataB = new Metadata("Jane", "doe", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		assertFalse(metadataA.equals(metadataB));
	}
	
	
	@Test
	public void testDifferentFamilyName() {
		Metadata metadataA = new Metadata("John", "Sauer", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		Metadata metadataB = new Metadata("John", "Bauer", "doe", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		assertFalse(metadataA.equals(metadataB));
	}

	@Test
	public void testDifferentContact() {
		Metadata metadataA = new Metadata("John", "Sauer", "Baranyi", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		Metadata metadataB = new Metadata("John", "Sauer", "Buchanan", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		assertFalse(metadataA.equals(metadataB));
	}

	@Test
	public void testDifferentCreatedDate() {
		Metadata metadataA = new Metadata("John", "Sauer", "Baranyi", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		Metadata metadataB = new Metadata("John", "Sauer", "Baranyi", "Tue Sep 02 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		assertFalse(metadataA.equals(metadataB));
	}

	@Test
	public void testDifferenteModifiedDate() {
		Metadata metadataA = new Metadata("John", "Sauer", "Baranyi", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		Metadata metadataB = new Metadata("John", "Sauer", "Baranyi", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 02 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		assertFalse(metadataA.equals(metadataB));
	}
	
	@Test
	public void testDifferentType() {
		Metadata metadataA = new Metadata("John", "Sauer", "Baranyi", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "EXPERIMENTAL_DATA");
		Metadata metadataB = new Metadata("John", "Sauer", "Baranyi", "Tue Sep 01 02:00:00 CEST 2015",
				"Tue Sep 01 02:00:00 CEST 2015", "PRIMARY_MODEL_WDATA");
		assertFalse(metadataA.equals(metadataB));
	}
}
