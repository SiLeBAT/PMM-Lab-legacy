package de.bund.bfr.knime.pmm.annotation.sbml;

import static org.junit.Assert.assertEquals;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import de.bund.bfr.knime.pmm.annotation.sbml.DescriptionAnnotation;

public class DescriptionAnnotationTest {

	@Test
	public void test() throws XMLStreamException {
		String description = "decimal reduction time";
		DescriptionAnnotation dn = new DescriptionAnnotation(description);		
		DescriptionAnnotation dn2 = new DescriptionAnnotation(dn.getAnnotation());
		assertEquals(description, dn2.getDescription());
	}
}
