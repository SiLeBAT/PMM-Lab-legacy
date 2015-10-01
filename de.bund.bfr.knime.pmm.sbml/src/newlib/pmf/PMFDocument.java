package newlib.pmf;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLWriter;

import newlib.numl.NuMLDocument;
import newlib.numl.NuMLWriter;

public class PMFDocument {
  
  private final static SBMLWriter sbmlWriter = new SBMLWriter();
  private final static NuMLWriter numlWriter = new NuMLWriter();

  Map<String, SBMLDocument> models = new HashMap<>();
  Map<String, NuMLDocument> dataSets = new HashMap<>();

  public void add(String name, SBMLDocument doc) {
    models.put(name, PMFUtil.wrap(doc));
  }

  public void add(String name, NuMLDocument doc) {
    dataSets.put(name, PMFUtil.wrap(doc));
  }
  
  public ByteArrayInputStream createFallbackInputStream(SBMLDocument doc) {
    SBMLDocument cloneDoc = doc.clone();
    PMFUtil.addStandardPrefixes(cloneDoc);
    String xmlString = sbmlWriter.writeSBMLToString(cloneDoc);
    byte[] bytes = xmlString.getBytes(StandardCharsets.UTF_8);
    ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
    return stream;
  }
  
  public ByteArrayInputStream createFallbackInputStream(NuMLDocument doc) {
    NuMLDocument cloneDoc = doc.clone();
    PMFUtil.addStandardPrefixes(cloneDoc);
    String xmlString = numlWriter.toString(cloneDoc);
    byte[] bytes = xmlString.getBytes(StandardCharsets.UTF_8);
    ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
    return stream;
  }
}
