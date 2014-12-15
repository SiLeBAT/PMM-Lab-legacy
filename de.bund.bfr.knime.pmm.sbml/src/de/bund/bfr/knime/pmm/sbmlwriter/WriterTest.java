package de.bund.bfr.knime.pmm.sbmlwriter;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.ext.comp.CompConstants;

public class WriterTest {

	public WriterTest() {
		SBMLDocument doc = new SBMLDocument(3, 1);
		doc.enablePackage(CompConstants.shortLabel);
		
		Model model = doc.createModel("test_model");
		Compartment compartment = model.createCompartment("default");
		compartment.setSize(1d);
		
		try {
			SBMLWriter.write(doc,  "test.xml", ' ', (short) 2);
		} catch (SBMLException | FileNotFoundException | XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new WriterTest();
	}
}
