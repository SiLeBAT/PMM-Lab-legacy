/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
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
