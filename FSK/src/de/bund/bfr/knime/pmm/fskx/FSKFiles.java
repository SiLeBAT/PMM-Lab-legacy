/**
 * 
 */
package de.bund.bfr.knime.pmm.fskx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.jdom2.JDOMException;
import org.knime.core.data.DataRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.port.PortObject;
import org.knime.core.util.FileUtil;
import org.knime.ext.r.node.local.port.RPortObject;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.writer.TableReader;
import de.bund.bfr.knime.pmm.common.writer.Util;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.PMFUtil;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.sbml.LimitsConstraint;
import de.bund.bfr.pmf.sbml.Metadata;
import de.bund.bfr.pmf.sbml.MetadataAnnotation;
import de.bund.bfr.pmf.sbml.MetadataImpl;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.bund.bfr.pmf.sbml.PMFUnitDefinition;
import de.bund.bfr.pmf.sbml.Reference;
import de.bund.bfr.pmf.sbml.ReferenceImpl;
import de.bund.bfr.pmf.sbml.ReferenceSBMLNode;
import de.bund.bfr.pmf.sbml.SBMLFactory;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;

/**
 * @author Miguel Alba
 *
 */
public class FSKFiles {
  private File modelScript;
  private File paramScript;
  private File vizScript;
  private File workspace;
  private File metaData;

  public FSKFiles(BufferedDataTable rTable, BufferedDataTable metaDataTable, PortObject workspace)
      throws IOException {

    DataRow row = rTable.iterator().next();
    StringCell modelCell = (StringCell) row.getCell(FSKXTuple.KEYS.ORIG_MODEL.ordinal());
    StringCell paramCell = (StringCell) row.getCell(FSKXTuple.KEYS.ORIG_PARAM.ordinal());
    StringCell vizCell = (StringCell) row.getCell(FSKXTuple.KEYS.ORIG_VIZ.ordinal());

    // write modelCell to File
    this.setModelScript(FileUtil.createTempFile("model", ".R"));
    try (FileWriter fw = new FileWriter(this.getModelScript());) {
      fw.write(modelCell.getStringValue());
    }

    // write paramCell to File
    if (!paramCell.getStringValue().isEmpty()) {
      this.setParamScript(FileUtil.createTempFile("param", ".R"));
      try (FileWriter fw = new FileWriter(this.getParamScript());) {
        fw.write(paramCell.getStringValue());
      }
    }

    // write vizCell to File
    if (!vizCell.getStringValue().isEmpty()) {
      this.setVizScript(FileUtil.createTempFile("visualization", ".R"));
      try (FileWriter fw = new FileWriter(this.getVizScript());) {
        fw.write(vizCell.getStringValue());
      }
    }

    // handle metaDataTable
    KnimeSchema schema = new OpenFSMRSchema();
    KnimeTuple tuple = PmmUtilities.getTuples(metaDataTable, schema).get(0);
    SBMLDocument doc = createSBMLDocument(tuple);
    this.setMetaData(File.createTempFile("metaData", ".pmf"));
    this.getMetaData().deleteOnExit();
    try {
      new SBMLWriter().write(doc, this.getMetaData());
    } catch (SBMLException | XMLStreamException e) {
      throw new IOException(e.getMessage());
    }

    // R workspace
    this.setWorkspace(((RPortObject) workspace).getFile());
  }
  
  /**
   * Reads the contents of a FSKX archive
   * 
   * @return {@link FSKFiles} with the {@link File}s of the FSKX archive.
   * @throws CombineArchiveException if the model script cannot be read
   */
  public FSKFiles(String path) throws CombineArchiveException {
    File archiveFile = new File(path);
    if (!archiveFile.exists()) {
      throw new CombineArchiveException(path + " does not exist");
    }

    try (CombineArchive archive = new CombineArchive(archiveFile)) {

      // Gets annotation
      RMetaDataNode node =
          new RMetaDataNode(archive.getDescriptions().get(0).getXmlDescription());

      // Add model script file
      String modelFileName = node.getMainScript();
      if (modelFileName == null) {
        throw new CombineArchiveException("Missing model file name in FSK metadata");
      }

      try {
        this.modelScript = extractFile(archive.getEntry(modelFileName), "modelScript", ".R");
      } catch (IOException e) {
        System.err.println(e.getMessage());
        throw new CombineArchiveException(e.getMessage());
      }

      // Add parameters script file
      String paramFileName = node.getParametersScript();
      if (paramFileName != null) {
        try {
          this.paramScript = extractFile(archive.getEntry("paramFileName"), "paramScript", ".R");
        } catch (IOException e) {
          System.err.println(e.getMessage());
        }
      }

      // Add visualization script file
      String vizFileName = node.getVisualizationScript();
      if (vizFileName != null) {
        try {
          this.vizScript = extractFile(archive.getEntry(vizFileName), "vizScript", ".R");
        } catch (IOException e) {
          System.err.println(e.getMessage());
        }
      }
      
      // Adds R workspace
      if (node.getWorkspaceFile() != null) {
        try {
          this.workspace = extractFile(archive.getEntry(node.getWorkspaceFile()), "workspace", ".R");
        } catch (IOException e) {
          System.err.println(e.getMessage());
        }
      }

      // Adds meta data file (SBMLDocument)
      URI pmfURI = URIFactory.createPMFURI();
      if (archive.getNumEntriesWithFormat(pmfURI) == 1) {
        try {
          this.metaData =
              extractFile(archive.getEntriesWithFormat(pmfURI).get(0), "metadata", ".pmf");
        } catch (IOException e) {
          System.err.println(e.getMessage());
        }
      }

    } catch (IOException | JDOMException | ParseException e) {
      e.printStackTrace();
    }
  }

  private static File extractFile(final ArchiveEntry entry, final String name, final String sufix)
      throws IOException {

    File file = FileUtil.createTempFile(name, sufix);
    entry.extractFile(file);

    return file;
  }

  public File getModelScript() {
    return this.modelScript;
  }

  public void setModelScript(File modelScript) {
    this.modelScript = modelScript;
  }

  public File getParamScript() {
    return this.paramScript;
  }

  public void setParamScript(File paramScript) {
    this.paramScript = paramScript;
  }

  public File getVizScript() {
    return this.vizScript;
  }

  public void setVizScript(File vizScript) {
    this.vizScript = vizScript;
  }

  public File getWorkspace() {
    return this.workspace;
  }

  public void setWorkspace(File workspace) {
    this.workspace = workspace;
  }

  public File getMetaData() {
    return this.metaData;
  }

  public void setMetaData(File metaData) {
    this.metaData = metaData;
  }

  /** Creates SBMLDocument out of a OpenFSMR tuple. */
  private static SBMLDocument createSBMLDocument(final KnimeTuple tuple) {

    final FSMRTemplate template = FSMRUtils.createTemplateFromTuple(tuple);

    // Creates SBMLDocument for the primary model
    final SBMLDocument sbmlDocument = new SBMLDocument(TableReader.LEVEL, TableReader.VERSION);

    // Adds namespaces to the sbmlDocument
    TableReader.addNamespaces(sbmlDocument);

    // Adds document annotation
    Metadata metaData = new MetadataImpl();
    if (template.isSetCreator()) {
      metaData.setGivenName(template.getCreator());
    }
    if (template.isSetFamilyName()) {
      metaData.setFamilyName(template.getFamilyName());
    }
    if (template.isSetContact()) {
      metaData.setContact(template.getContact());
    }
    if (template.isSetCreatedDate()) {
      metaData.setCreatedDate(template.getCreatedDate().toString());
    }
    if (template.isSetModifiedDate()) {
      metaData.setModifiedDate(template.getModifiedDate().toString());
    }
    if (template.isSetCreatedDate()) {
      metaData.setType(template.getModelType());
    }
    if (template.isSetRights()) {
      metaData.setRights(template.getRights());
    }
    if (template.isSetReferenceDescriptionLink()) {
      metaData.setReferenceLink(template.getReferenceDescriptionLink().toString());
    }

    sbmlDocument.setAnnotation(new MetadataAnnotation(metaData).getAnnotation());

    // Creates model and names it
    Model model = sbmlDocument.createModel(PMFUtil.createId(template.getModelId()));
    if (template.isSetModelName()) {
      model.setName(template.getModelName());
    }

    // Sets model notes
    if (template.isSetNotes()) {
      try {
        model.setNotes(template.getNotes());
      } catch (XMLStreamException e) {
        e.printStackTrace();
      }
    }

    // Creates and adds compartment to the model
    PMFCompartment compartment = SBMLFactory
        .createPMFCompartment(PMFUtil.createId(template.getMatrixName()), template.getMatrixName());
    compartment.setDetail(template.getMatrixDetails());
    model.addCompartment(compartment.getCompartment());

    // Creates and adds species to the model
    String speciesId = PMFUtil.createId(template.getOrganismName());
    String speciesName = template.getOrganismName();
    String speciesUnit = PMFUtil.createId(template.getDependentVariableUnit());
    PMFSpecies species =
        SBMLFactory.createPMFSpecies(compartment.getId(), speciesId, speciesName, speciesUnit);
    model.addSpecies(species.getSpecies());

    // Add unit definitions here (before parameters)
    Set<String> unitsSet = new LinkedHashSet<>();
    unitsSet.add(template.getDependentVariableUnit().trim());
    for (String unit : template.getIndependentVariablesUnits()) {
      unitsSet.add(unit.trim());
    }
    for (String unit : unitsSet) {
      try {
        PMFUnitDefinition unitDef = Util.createUnitFromDB(unit);

        // unitDef is not in PmmLab DB
        if (unitDef == null) {
          UnitDefinition ud = model.createUnitDefinition(PMFUtil.createId(unit));
          ud.setName(unit);
        } else {
          model.addUnitDefinition(unitDef.getUnitDefinition());
        }
      } catch (XMLStreamException e) {
        e.printStackTrace();
      }
    }

    // Adds dep parameter
    Parameter depParam = new Parameter(PMFUtil.createId(template.getDependentVariable()));
    depParam.setName(template.getDependentVariable());
    depParam.setUnits(PMFUtil.createId(template.getDependentVariableUnit()));
    model.addParameter(depParam);

    // Adds dep constraint
    if (template.isSetDependentVariableMin() || template.isSetDependentVariableMax()) {
      LimitsConstraint lc = new LimitsConstraint(template.getDependentVariable(),
          template.getDependentVariableMin(), template.getDependentVariableMax());
      if (lc.getConstraint() != null) {
        model.addConstraint(lc.getConstraint());
      }
    }

    // Adds independent parameters
    for (int i = 0; i < template.getIndependentVariables().length; i++) {
      String var = template.getIndependentVariables()[i];
      Parameter param = model.createParameter(PMFUtil.createId(var));
      param.setName(var);

      try {
        param.setUnits(PMFUtil.createId(template.getIndependentVariablesUnits()[i]));
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      }

      Double min = template.isSetIndependentVariablesMins()
          ? template.getIndependentVariablesMins()[i] : null;
      Double max = template.isSetIndependentVariablesMaxs()
          ? template.getIndependentVariablesMaxs()[i] : null;
      LimitsConstraint lc = new LimitsConstraint(param.getId(), min, max);
      if (lc.getConstraint() != null) {
        model.addConstraint(lc.getConstraint());
      }
    }

    // Add rule
    String formulaName = "Missing formula name";
    ModelClass modelClass = template.getModelSubject();
    int modelId = MathUtilities.getRandomNegativeInt();
    Reference[] references = new Reference[0];

    AssignmentRule rule = new AssignmentRule(3, 1);
    rule.setVariable(depParam.getId());
    rule.setAnnotation(
        new ModelRuleAnnotation(formulaName, modelClass, modelId, references).annotation);
    model.addRule(rule);

    return sbmlDocument;
  }
  
  static class ModelRuleAnnotation {

    String formulaName;
    ModelClass modelClass;
    Reference[] references;
    int pmmlabID;
    Annotation annotation;

    private static final String FORMULA_TAG = "formulaName";
    private static final String SUBJECT_TAG = "subject";
    private static final String REFERENCE_TAG = "reference";
    private static final String PMMLAB_ID = "pmmlabID";

    public ModelRuleAnnotation(Annotation annotation) {
      this.annotation = annotation;

      XMLNode metadata = annotation.getNonRDFannotation().getChildElement("metadata", "");

      // Gets formula name
      XMLNode nameNode = metadata.getChildElement(FORMULA_TAG, "");
      this.formulaName = nameNode.getChild(0).getCharacters();

      // Gets formula subject
      XMLNode modelclassNode = metadata.getChildElement(SUBJECT_TAG, "");
      if (modelclassNode == null) {
        this.modelClass = ModelClass.UNKNOWN;
      } else {
        this.modelClass = ModelClass.fromName(modelclassNode.getChild(0).getCharacters());
      }

      // Get PmmLab ID
      XMLNode idNode = metadata.getChildElement(PMMLAB_ID, "");
      if (idNode == null) {
        this.pmmlabID = -1;
      } else {
        this.pmmlabID = Integer.parseInt(idNode.getChild(0).getCharacters());
      }

      // Gets references
      List<XMLNode> refNodes = metadata.getChildElements(REFERENCE_TAG, "");
      int numRefNodes = refNodes.size();
      this.references = new ReferenceImpl[numRefNodes];
      for (int i = 0; i < numRefNodes; i++) {
        XMLNode refNode = refNodes.get(i);
        this.references[i] = new ReferenceSBMLNode(refNode).toReference();
      }
    }

    public ModelRuleAnnotation(String formulaName, ModelClass modelClass, int pmmlabID,
        Reference[] references) {
      // Builds metadata node
      XMLNode metadataNode = new XMLNode(new XMLTriple("metadata", null, "pmf"));
      this.annotation = new Annotation();
      this.annotation.setNonRDFAnnotation(metadataNode);

      // Creates annotation for formula name
      XMLNode nameNode = new XMLNode(new XMLTriple(FORMULA_TAG, null, "pmmlab"));
      nameNode.addChild(new XMLNode(formulaName));
      metadataNode.addChild(nameNode);

      // Creates annotation for modelClass
      XMLNode modelClassNode = new XMLNode(new XMLTriple(SUBJECT_TAG, null, "pmmlab"));
      modelClassNode.addChild(new XMLNode(modelClass.fullName()));
      metadataNode.addChild(modelClassNode);

      // Create annotation for pmmlabID
      XMLNode idNode = new XMLNode(new XMLTriple(PMMLAB_ID, null, "pmmlab"));
      idNode.addChild(new XMLNode(new Integer(pmmlabID).toString()));
      metadataNode.addChild(idNode);

      // Builds reference nodes
      for (Reference ref : references) {
        metadataNode.addChild(new ReferenceSBMLNode(ref).getNode());
      }

      // Saves formulaName, subject and model literature
      this.formulaName = formulaName;
      this.modelClass = modelClass;
      this.pmmlabID = pmmlabID;
      this.references = references;
    }
  }
}
