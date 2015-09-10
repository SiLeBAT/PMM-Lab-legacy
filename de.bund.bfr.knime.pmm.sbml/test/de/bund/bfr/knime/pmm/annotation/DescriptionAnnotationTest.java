package de.bund.bfr.knime.pmm.annotation;

import static org.junit.Assert.assertEquals;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

public class DescriptionAnnotationTest {

	@Test
	public void test() throws XMLStreamException {
		String description = "decimal reduction time";
		DescriptionAnnotation dn = new DescriptionAnnotation(description);		
		DescriptionAnnotation dn2 = new DescriptionAnnotation(dn.getNode());
		assertEquals(description, dn2.getDescription());
	}
}
