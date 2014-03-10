package de.bund.bfr.knime.pmm.sbmlwriter;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;

public class Test {

	private static final int LEVEL = 2;
	private static final int VERSION = 4;

	public static void main(String[] args) throws SBMLException,
			FileNotFoundException, XMLStreamException,
			UnsupportedEncodingException {
		UnitDefinition unit = new UnitDefinition(LEVEL, VERSION);

		unit.addUnit(new Unit(Unit.Kind.ITEM, LEVEL, VERSION));
		unit.addUnit(new Unit(Unit.Kind.GRAM, -1, LEVEL, VERSION));

		String xml = SBMLUtilities.toXml(unit);

		System.out.println(xml);
		System.out.println(SBMLUtilities.fromXml(xml));
	}
}
