package de.bund.bfr.knime.pmm.sbmlwriter;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.Unit;
import org.sbml.jsbml.UnitDefinition;

public class SBMLUtilities {

	public static String toXml(UnitDefinition unit) {
		SBMLDocument doc = new SBMLDocument(unit.getLevel(), unit.getVersion());
		Model model = doc.createModel("ID");

		model.addUnitDefinition(unit);

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			SBMLWriter.write(doc, out, "test", "1.0", ' ', (short) 0);

			String xml = out.toString(StandardCharsets.UTF_8.name());
			String from = "<listOfUnitDefinitions>";
			String to = "</listOfUnitDefinitions>";

			return xml.substring(xml.indexOf(from) + from.length(),
					xml.indexOf(to)).replace("\n", "");
		} catch (SBMLException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static UnitDefinition fromXml(String xml) {
		String preXml = "<?xml version='1.0' encoding='UTF-8' standalone='no'?>"
				+ "<sbml xmlns=\"http://www.sbml.org/sbml/level2/version4\" level=\"2\" version=\"4\">"
				+ "<model id=\"ID\">" + "<listOfUnitDefinitions>";
		String postXml = "</listOfUnitDefinitions>" + "</model>" + "</sbml>";

		try {
			return SBMLReader.read(preXml + xml + postXml).getModel()
					.getUnitDefinition(0);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static UnitDefinition addUnitToModel(Model model, UnitDefinition unit) {
		UnitDefinition u = model.createUnitDefinition(unit.getId());

		for (int i = 0; i < unit.getNumUnits(); i++) {
			u.addUnit(new Unit(unit.getUnit(i)));
		}

		return u;
	}

	public static Unit.Kind simplify(UnitDefinition unit) {
		unit = unit.clone().simplify();

		if (unit.isUnitKind()) {
			Unit u = unit.getUnit(0);

			if (u.getScale() == 0 && u.getExponent() == 1
					&& u.getMultiplier() == 1) {
				return unit.getUnit(0).getKind();
			}
		}

		return null;
	}
}
