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
package de.bund.bfr.pmf.numl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.sbml.ReferenceImpl;
import de.bund.bfr.pmf.sbml.Reference;
import de.bund.bfr.pmf.sbml.ReferenceType;

/**
 * @author Miguel Alba
 *
 */
public class ResultComponentTest {

	private String id = "exp1";
	private int condId = 1;
	private String combaseId = "salm25";
	private String creatorGivenName = "doe";
	private String creatorFamilyName = null;
	private String creatorContact = null;
	private String createdDate = null;
	private String modifiedDate = null;
	private ModelType modelType = ModelType.EXPERIMENTAL_DATA;
	private String notes = null;
	private String rights = null;
	private Reference[] references;
	private TupleDescription tupleDescription;
	private Tuple[] tuples;

	private Document doc;

	@Before
	public void setUp() {
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String author = "Baranyi, J.";
		int year = 1994;
		String title = "A dynamic approach to predicting microbial bacterial growth in food";
		String abstractText = "A new member of the family of groth models described by Baranyi ...";
		String journal = "International Journal of Food Microbiology";
		String volume = "23";
		String issue = "3";
		int page = 277;
		String website = "http://www.sciencedirect.com/science/article/pii/0168160594901570";
		ReferenceType referenceType = ReferenceType.Paper;

		ReferenceImpl reference = new ReferenceImpl(author, year, title, abstractText, journal, volume, issue, page,
				null, website, referenceType, null);
		references = new ReferenceImpl[] { reference };

		AtomicDescription concDesc = new AtomicDescription("concentration", "concentration");
		AtomicDescription timeDesc = new AtomicDescription("Time", "time");
		tupleDescription = new TupleDescription(concDesc, timeDesc);

		tuples = new Tuple[] { new Tuple(new AtomicValue(2.67), new AtomicValue(0.00)),
				new Tuple(new AtomicValue(2.91), new AtomicValue(50.88)),
				new Tuple(new AtomicValue(2.87), new AtomicValue(73.02)),
				new Tuple(new AtomicValue(3.69), new AtomicValue(103.96)),
				new Tuple(new AtomicValue(4.25), new AtomicValue(145.01)) };
	}

	@Test
	public void testIdAccesor() {
		ResultComponent rc = new ResultComponent("exp1", tupleDescription, tuples);
		assertEquals(rc.getID(), id);
	}

	@Test
	public void testCondIdAccessors() {
		ResultComponent rc = new ResultComponent("exp1", tupleDescription, tuples);

		assertFalse(rc.isSetCondID());
		assertNull(rc.getCondID());
		rc.setCondID(1);
		assertTrue(1 == rc.getCondID());
		assertNotNull(rc.getCondID());
		assertTrue(rc.isSetCondID());
	}

	@Test
	public void testCombaseIdAccesors() {
		ResultComponent rc = new ResultComponent("exp1", tupleDescription, tuples);

		assertFalse(rc.isSetCombaseID());
		assertNull(rc.getCombaseID());
		rc.setCombaseID("salm25");
		assertEquals("salm25", rc.getCombaseID());
		assertNotNull(rc.getCombaseID());
		assertTrue(rc.isSetCombaseID());
	}

	@Test
	public void testCreatorGivenNameAccessors() {
		ResultComponent rc = new ResultComponent("exp1", tupleDescription, tuples);

		assertFalse(rc.isSetCreatorGivenName());
		assertNull(rc.getCreatorGivenName());
		rc.setCreatorGivenName("doe");
		assertEquals("doe", rc.getCreatorGivenName());
		assertNotNull(rc.getCreatorGivenName());
		assertTrue(rc.isSetCreatorGivenName());
	}

	@Test
	public void testCreatorFamilyNameAccessors() {
		ResultComponent rc = new ResultComponent("exp1", tupleDescription, tuples);

		assertFalse(rc.isSetCreatorFamilyName());
		assertNull(rc.getCreatorFamilyName());
		rc.setCreatorFamilyName("doe");
		assertEquals("doe", rc.getCreatorFamilyName());
		assertNotNull(rc.getCreatorFamilyName());
		assertTrue(rc.isSetCreatorFamilyName());
	}

	@Test
	public void testCreatorContactAccesors() {
		ResultComponent rc = new ResultComponent("exp1", tupleDescription, tuples);

		assertFalse(rc.isSetCreatorContact());
		assertNull(rc.getCreatorContact());
		rc.setCreatorContact("doe");
		assertEquals("doe", rc.getCreatorContact());
		assertNotNull(rc.getCreatorContact());
		assertTrue(rc.isSetCreatorContact());
	}

	@Test
	public void testCreatedDateAccesors() {
		ResultComponent rc = new ResultComponent("exp1", tupleDescription, tuples);

		assertFalse(rc.isSetCreatedDate());
		assertNull(rc.getCreatedDate());
		rc.setCreatedDate("Wed Sep 02 02:00:00 CEST 2015");
		assertEquals("Wed Sep 02 02:00:00 CEST 2015", rc.getCreatedDate());
		assertNotNull(rc.getCreatedDate());
		assertTrue(rc.isSetCreatedDate());

	}

	@Test
	public void testModifiedDateAccesors() {
		ResultComponent rc = new ResultComponent("exp1", tupleDescription, tuples);

		assertFalse(rc.isSetModifiedDate());
		assertNull(rc.getModifiedDate());
		rc.setModifiedDate("Wed Sep 02 02:00:00 CEST 2015");
		assertEquals("Wed Sep 02 02:00:00 CEST 2015", rc.getModifiedDate());
		assertNotNull(rc.getModifiedDate());
		assertTrue(rc.isSetModifiedDate());
	}

	@Test
	public void testModelTypeAccesors() {
		ResultComponent rc = new ResultComponent("exp1", tupleDescription, tuples);

		assertFalse(rc.isSetModelType());
		assertNull(rc.getModelType());
		rc.setModelType(ModelType.TWO_STEP_SECONDARY_MODEL);
		assertEquals(ModelType.TWO_STEP_SECONDARY_MODEL, rc.getModelType());
		assertTrue(rc.isSetModelType());
	}

	@Test
	public void testRightsAccesors() {
		ResultComponent rc = new ResultComponent("exp1", tupleDescription, tuples);

		assertFalse(rc.isSetRights());
		assertNull(rc.getRights());
		rc.setRights("CC");
		assertEquals("CC", rc.getRights());
		assertTrue(rc.isSetRights());
	}

	@Test
	public void testReferenceAccesors() {
		ResultComponent rc = new ResultComponent("exp1", tupleDescription, tuples);

		assertFalse(rc.isSetReferences());
		assertNull(rc.getReferences());
		rc.setReferences(references);
		assertArrayEquals(references, rc.getReferences());
		assertTrue(rc.isSetReferences());
	}

	@Test
	public void testDimensionDescriptionAccesors() {
		ResultComponent rc = new ResultComponent("exp1", tupleDescription, tuples);
		assertEquals(tupleDescription, rc.getDimensionDescription());
	}

	@Test
	public void testTupleAccesors() {
		ResultComponent rc = new ResultComponent("exp1", tupleDescription, tuples);
		assertArrayEquals(tuples, rc.getDimensions());
	}

	@Test
	public void testNodes() {
		ResultComponent rc = new ResultComponent(id, condId, combaseId, creatorGivenName, creatorFamilyName,
				creatorContact, createdDate, modifiedDate, modelType, rights, notes, references, tupleDescription,
				tuples);
		assertEquals(rc, new ResultComponent(rc.toNode(doc)));
	}

	@Test
	public void testToString() {
		ResultComponent rc = new ResultComponent(id, tupleDescription, tuples);
		String expectedString = String.format("ResultComponent [id=%s, dimensionDescription=%s, dimension=%s]",
				rc.getID(), rc.getDimensionDescription().toString(), rc.getDimensions().toString());
		String obtainedString = rc.toString();
		assertEquals(expectedString, obtainedString);
	}
}
