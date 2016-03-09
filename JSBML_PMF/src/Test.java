import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.JSBML;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.ext.pmf.ModelVariable;
import org.sbml.jsbml.ext.pmf.PMFConstants;
import org.sbml.jsbml.ext.pmf.PMFModelPlugin;
import org.sbml.jsbml.ext.pmf.Parameter;
import org.sbml.jsbml.ext.pmf.UnitDefinition;
import org.sbml.jsbml.xml.parsers.ParserManager;

public class Test {
  
  public static void main(String[] args) {
    SBMLDocument doc = new SBMLDocument(3, 1);
    doc.enablePackage(PMFConstants.shortLabel);
    Model model = doc.createModel();
    
    System.out.println(ParserManager.getManager().getPackageParser(PMFConstants.namespaceURI));
    
    PMFModelPlugin plugin = (PMFModelPlugin) model.createPlugin(PMFConstants.shortLabel);
    ModelVariable mv = plugin.createModelVariable("pH", 5.0);
    System.out.println(mv);
    
    Parameter p = plugin.createParameter("p");
    p.setP(0.1);
    p.setError(0.5);
    p.setDescription("some description");
    p.setMin(0.1);
    p.setMax(9.9);
    
    UnitDefinition ud = plugin.createUnitDefinition("ln_count_g");
    ud.setName("ln(count/g)");
    ud.setTransformation("ln");
    
    model.createUnitDefinition("Rjaklalfa");
    
    try {
      System.out.println(JSBML.writeSBMLToString(doc));
      JSBML.writeSBML(doc, "C:/Temp/theFile.sbml");
    } catch (SBMLException | XMLStreamException | IOException e) {
      e.printStackTrace();
    }
  }
}
