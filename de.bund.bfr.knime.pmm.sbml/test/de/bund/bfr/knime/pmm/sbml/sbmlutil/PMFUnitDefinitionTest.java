package de.bund.bfr.knime.pmm.sbml.sbmlutil;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sbml.jsbml.Unit;

import de.bund.bfr.knime.pmm.sbmlutil.PMFUnitDefinition;

public class PMFUnitDefinitionTest {

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
	
	@Test
	/**
	 * xmlToUnitDefinition should fill units with default parameters if missing
	 */
	public void testConvertXMLUnitParams() {
		String xml = "<unitDefinition id=\"day\"><listOfUnits><unit scale=\"86400\" kind=\"second\" xmlns=\"http://sourceforge.net/projects/microbialmodelingexchange/files/Units\"/></listOfUnits></unitDefinition>";
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
}