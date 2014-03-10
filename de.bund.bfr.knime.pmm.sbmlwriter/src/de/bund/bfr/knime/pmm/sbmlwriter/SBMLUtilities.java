package de.bund.bfr.knime.pmm.sbmlwriter;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.UnitDefinition;

public class SBMLUtilities {

	public static String toXml(UnitDefinition unit) throws SBMLException,
			XMLStreamException, UnsupportedEncodingException {
		SBMLDocument doc = new SBMLDocument(unit.getLevel(), unit.getVersion());
		Model model = doc.createModel("ID");

		model.addUnitDefinition(unit);

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		SBMLWriter.write(doc, out, "test", "1.0", ' ', (short) 0);

		String xml = out.toString("UTF-8");
		String from = "<listOfUnitDefinitions>";
		String to = "</listOfUnitDefinitions>";

		return xml
				.substring(xml.indexOf(from) + from.length(), xml.indexOf(to))
				.replace("\n", "");
	}

	public static UnitDefinition fromXml(String xml) throws XMLStreamException {
		String preXml = "<?xml version='1.0' encoding='UTF-8' standalone='no'?>"
				+ "<sbml xmlns=\"http://www.sbml.org/sbml/level2/version4\" level=\"2\" version=\"4\">"
				+ "<model id=\"ID\">" + "<listOfUnitDefinitions>";
		String postXml = "</listOfUnitDefinitions>" + "</model>" + "</sbml>";
		SBMLDocument doc = SBMLReader.read(preXml + xml + postXml);

		return doc.getModel().getUnitDefinition(0);
	}
}
