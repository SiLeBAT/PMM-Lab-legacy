import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.sbml.jsbml.JSBML;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.ext.pmf.PMFConstants;

public class Test {
  
  public static void main(String[] args) {
    SBMLDocument doc = new SBMLDocument(3, 1);
    doc.enablePackage(PMFConstants.shortLabel);
    Model model = doc.createModel();
    
//    PMFModelPlugin plugin = (PMFModelPlugin) model.createPlugin(PMFConstants.shortLabel);
//    plugin.createModelVariable("pH", 5.0);
    
//    Parameter p = model.createParameter("p");
//    ParameterPlugin parameterPlugin = (ParameterPlugin) p.createPlugin(PMFConstants.shortLabel);
    
//    parameterPlugin.setP(0.1);
//    parameterPlugin.setError(0.5);
//    parameterPlugin.setDescription("some description");
//    parameterPlugin.setMin(0.1);
//    parameterPlugin.setMax(9.9);
//    parameterPlugin.createCorrelation("a", 1.0);
    
//    UnitDefinition ud = model.createUnitDefinition("ln_count_g");
//    ud.setName("ln(count/g)");
//    PMFUnitDefinitionPlugin unitDefinitionPlugin = (PMFUnitDefinitionPlugin) ud.createPlugin(PMFConstants.shortLabel);
//    unitDefinitionPlugin.createUnitTransformation("ln");
    
    try {
      System.out.println(JSBML.writeSBMLToString(doc));
      JSBML.writeSBML(doc, "C:/Temp/theFile.sbml");
        doc = JSBML.readSBMLFromFile("C:/Temp/theFile.sbml");
        
        System.out.println();
        System.out.println(JSBML.writeSBMLToString(doc));
      } catch (XMLStreamException | IOException e) {
      e.printStackTrace();
    }
  }
}
