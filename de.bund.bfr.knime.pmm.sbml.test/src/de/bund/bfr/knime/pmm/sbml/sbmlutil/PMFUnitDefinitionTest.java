package de.bund.bfr.knime.pmm.sbml.sbmlutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;

import de.bund.bfr.knime.pmm.sbmlutil.PMFUnitDefinition;

public class PMFUnitDefinitionTest {

	@Test
	public void testConstructor() {
		UnitDefinition unitDefinition = new UnitDefinition("week", 3, 1);
		unitDefinition.addUnit(new Unit(1, 604800, Unit.Kind.SECOND, 1, 3, 1));

		PMFUnitDefinition pud = new PMFUnitDefinition(unitDefinition);
		assertEquals(unitDefinition, pud.getUnitDefinition());
	}

	@Test
	public void testConvertXMLUnitWTransformation() {
		String xml = "<unitDefinition id=\"log10_item_g\"><annotation><transformation name=\"log10\" xmlns=\"http://sourceforge.net/projects/microbialmodelingexchange/files/Units\"/></annotation><listOfUnits><unit kind=\"item\"/><unit exponent=\"-1\" kind=\"gram\"/></listOfUnits></unitDefinition>";
		PMFUnitDefinition pud = PMFUnitDefinition.xmlToPMFUnitDefinition(xml);
		assertEquals("log10", pud.getTransformationName());
	}

	@Test
	public void testConvertXMLUnitWOTransformation() {
		String xml = "<unitDefinition id=\"day\"><listOfUnits><unit scale=\"86400\" kind=\"second\" xmlns=\"http://sourceforge.net/projects/microbialmodelingexchange/files/Units\"/></listOfUnits></unitDefinition>";
		PMFUnitDefinition pud = PMFUnitDefinition.xmlToPMFUnitDefinition(xml);
		assertNull(pud.getTransformationName());
	}

	/**
	 * xmlToUnitDefinition should fill units with default parameters if missing
	 */
	@Test
	public void testConvertXMLUnitParams() {
		String xml = "<unitDefinition id=\"day\"><listOfUnits><unit scale=\"86400\" multiplier=\"1\" kind=\"second\" xmlns=\"http://sourceforge.net/projects/microbialmodelingexchange/files/Units\"/></listOfUnits></unitDefinition>";
		Unit expectedUnit = new Unit(1.0, 86400, Unit.Kind.SECOND, 1, 3, 1);

		PMFUnitDefinition pud = PMFUnitDefinition.xmlToPMFUnitDefinition(xml);
		Unit parsedUnit = pud.getUnitDefinition().getUnit(0);

		assertTrue("Multiplier is not set", parsedUnit.isSetMultiplier());
		assertTrue("Scale is not set", parsedUnit.isSetScale());
		assertTrue("Kind is not set", parsedUnit.isSetKind());
		assertTrue("Exponent is not set", parsedUnit.isSetExponent());

		assertEquals(expectedUnit.getMultiplier(), parsedUnit.getMultiplier(), 0.0);
		assertEquals(expectedUnit.getScale(), parsedUnit.getScale());
		assertEquals(expectedUnit.getKind(), parsedUnit.getKind());
		assertEquals(expectedUnit.getExponent(), parsedUnit.getExponent(), 0.0);
	}

	// TODO: does not work!!
	/**
	 * Test Groovy unit without transformation
	 */
//	@Test
//	public void testGroovyNodeWOTransformation() {
//		UnitDefinition unitDefinition = new UnitDefinition("week", 3, 1);
//		unitDefinition.addUnit(new Unit(1.0, 604800, Unit.Kind.SECOND, 1.0, 3, 1));
//
//		Node expectedNode = new Node(null, "sbml:unitDefinition", unitDefinition.writeXMLAttributes());
//		expectedNode.appendNode("sbml:unit", unitDefinition.getUnit(0).writeXMLAttributes());
//		
//		Node obtainedNode = new PMFUnitDefinition(unitDefinition).toGroovyNode();
//		
//		assertEquals(expectedNode, obtainedNode);
//	}
}