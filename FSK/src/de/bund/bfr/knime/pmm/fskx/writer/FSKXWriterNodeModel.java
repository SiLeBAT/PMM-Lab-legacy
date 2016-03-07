/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors: Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.fskx.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.jdom2.JDOMException;
import org.knime.core.data.DataRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDate;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
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
import de.bund.bfr.knime.pmm.fskx.FSKXTuple;
import de.bund.bfr.knime.pmm.fskx.RMetaDataNode;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.PMFUtil;
import de.bund.bfr.pmf.file.uri.RUri;
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
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 */
public class FSKXWriterNodeModel extends NodeModel {

  // Configuration keys
  protected static final String CFG_FILE = "file";
  protected static final String CFG_CREATOR_GIVEN_NAME = "creatorGivenName";
  protected static final String CFG_CREATOR_FAMILY_NAME = "creatorFamilyName";
  protected static final String CFG_CREATOR_CONTACT = "creatorContact";
  protected static final String CFG_CREATED_DATE = "creationDate";
  protected static final String CFG_MODIFIED_DATE = "modifiedDate";
  protected static final String CFG_REFERENCE_LINK = "referenceLink";
  protected static final String CFG_LICENSE = "license";
  protected static final String CFG_NOTES = "notes";

  private SettingsModelString filePath = new SettingsModelString(CFG_FILE, null);
  private SettingsModelString creatorGivenName =
      new SettingsModelString(CFG_CREATOR_GIVEN_NAME, null);
  private SettingsModelString creatorFamilyName =
      new SettingsModelString(CFG_CREATOR_FAMILY_NAME, null);
  private SettingsModelString creatorContact = new SettingsModelString(CFG_CREATOR_CONTACT, null);
  private SettingsModelDate createdDate = new SettingsModelDate(CFG_CREATED_DATE);
  private SettingsModelDate modifiedDate = new SettingsModelDate(CFG_MODIFIED_DATE);
  private SettingsModelString referenceLink = new SettingsModelString(CFG_REFERENCE_LINK, null);
  private SettingsModelString license = new SettingsModelString(CFG_LICENSE, null);
  private SettingsModelString notes = new SettingsModelString(CFG_NOTES, null);

  private static final PortType[] inPortTypes =
      {BufferedDataTable.TYPE, BufferedDataTable.TYPE, RPortObject.TYPE};
  private static final PortType[] outPortTypes = {};

  protected FSKXWriterNodeModel() {
    super(inPortTypes, outPortTypes);

    // Sets current date in the dialog components
    long currentDate = Calendar.getInstance().getTimeInMillis();
    createdDate.setTimeInMillis(currentDate);
    modifiedDate.setTimeInMillis(currentDate);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws FileCreationException If a critical file could not be created. E.g. model script.
   * @throws IOException
   */
  @Override
  protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec)
      throws CombineArchiveException {

    BufferedDataTable rTable = (BufferedDataTable) inData[0];
    BufferedDataTable metaDataTable = (BufferedDataTable) inData[1];
    PortObject rWorkspace = inData[2];

    FSKFiles files;
    try {
      files = new FSKFiles(rTable, metaDataTable, rWorkspace);
    } catch (IOException e) {
      throw new CombineArchiveException(e.getMessage());
    }

    File archiveFile = new File(filePath.getStringValue());
    try {
      Files.deleteIfExists(archiveFile.toPath());
    } catch (IOException e) {
      throw new CombineArchiveException(filePath.getStringValue() + " cannot be overwritten");
    }

    // Try to create CombineArchive
    CombineArchive archive;
    try {
      archive = new CombineArchive(archiveFile);
    } catch (IOException | JDOMException | ParseException | CombineArchiveException e) {
      throw new CombineArchiveException(e.getMessage());
    }

    RMetaDataNode metaDataNode = new RMetaDataNode();
    URI rURI = new RUri().createURI();

    // Adds R model script
    if (files.modelScript == null) {
      try {
        archive.close();
      } catch (IOException e) {
      }
      throw new CombineArchiveException("Missing model script file");
    }
    try {
      String filename = "model.R";
      archive.addEntry(files.modelScript, filename, rURI);
      metaDataNode.setMainScript(filename);
    } catch (IOException e) {
      try {
        archive.close();
      } catch (IOException ioe) {
      }
      throw new CombineArchiveException("Model script cannot be added");
    }

    // Adds R parameters script
    if (files.paramScript != null) {
      try {
        String filename = "params.R";
        archive.addEntry(files.paramScript, filename, rURI);
        metaDataNode.setParamScript(filename);
      } catch (IOException e) {
        try {
          archive.close();
        } catch (IOException ioe) {
        }
        throw new CombineArchiveException("Parameters script cannot be added");
      }
    }

    // Adds R visualization script
    if (files.vizScript != null) {
      try {
        String filename = "visualization.R";
        archive.addEntry(files.vizScript, filename, rURI);
        metaDataNode.setVisualizationScript(filename);
      } catch (IOException e) {
        try {
          archive.close();
        } catch (IOException ioe) {
        }
        throw new CombineArchiveException("Visualization script cannot be added");
      }
    }

    // Adds R workspace
    if (files.workspace != null) {
      try {
        archive.addEntry(files.workspace, files.workspace.getName(), rURI);
        metaDataNode.setWorkspaceFile(files.workspace.getName());
      } catch (IOException e) {
        try {
          archive.close();
        } catch (IOException ioe) {
        }
        throw new CombineArchiveException("R workspace file cannot be added");
      }
    }

    // Adds PMF document with meta data
    if (files.metaData != null) {
      String filename = "metadata.pmf";
      try {
        archive.addEntry(files.metaData, filename, URIFactory.createPMFURI());
      } catch (IOException e) {
        try {
          archive.close();
        } catch (IOException ioe) {
        }
        throw new CombineArchiveException("PMF document cannot be added");
      }
    }
    
    archive.addDescription(new DefaultMetaDataObject(metaDataNode.getNode()));

    try {
      archive.pack();
    } catch (IOException | TransformerException e1) {
      e1.printStackTrace();
    }

    try {
      archive.close();
    } catch (IOException e) {
    }

    return new BufferedDataTable[] {};
  }

  /** {@inheritDoc} */
  @Override
  protected void reset() {}

  /** {@inheritDoc} */
  @Override
  protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
      throws InvalidSettingsException {
    return new PortObjectSpec[] {

    };
  }

  /** {@inheritDoc} */
  @Override
  protected void saveSettingsTo(final NodeSettingsWO settings) {
    filePath.saveSettingsTo(settings);
    creatorGivenName.saveSettingsTo(settings);
    creatorFamilyName.saveSettingsTo(settings);
    creatorContact.saveSettingsTo(settings);
    createdDate.saveSettingsTo(settings);
    modifiedDate.saveSettingsTo(settings);
    referenceLink.saveSettingsTo(settings);
    license.saveSettingsTo(settings);
    notes.saveSettingsTo(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
      throws InvalidSettingsException {
    filePath.loadSettingsFrom(settings);
    creatorGivenName.loadSettingsFrom(settings);
    creatorFamilyName.loadSettingsFrom(settings);
    creatorContact.loadSettingsFrom(settings);
    createdDate.loadSettingsFrom(settings);
    modifiedDate.loadSettingsFrom(settings);
    referenceLink.loadSettingsFrom(settings);
    license.loadSettingsFrom(settings);
    notes.loadSettingsFrom(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    filePath.validateSettings(settings);
    creatorGivenName.validateSettings(settings);
    creatorFamilyName.validateSettings(settings);
    creatorContact.validateSettings(settings);
    createdDate.validateSettings(settings);
    modifiedDate.validateSettings(settings);
    referenceLink.validateSettings(settings);
    license.validateSettings(settings);
    notes.validateSettings(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadInternals(final File internDir, final ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  /** {@inheritDoc} */
  @Override
  protected void saveInternals(final File internDir, final ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  class FileCreationException extends Exception {
    private static final long serialVersionUID = 1L;

    public FileCreationException(final String descr) {
      super(descr);
    }
  }
}


class ModelRuleAnnotation {

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
    formulaName = nameNode.getChild(0).getCharacters();

    // Gets formula subject
    XMLNode modelclassNode = metadata.getChildElement(SUBJECT_TAG, "");
    if (modelclassNode == null) {
      modelClass = ModelClass.UNKNOWN;
    } else {
      modelClass = ModelClass.fromName(modelclassNode.getChild(0).getCharacters());
    }

    // Get PmmLab ID
    XMLNode idNode = metadata.getChildElement(PMMLAB_ID, "");
    if (idNode == null) {
      pmmlabID = -1;
    } else {
      pmmlabID = Integer.parseInt(idNode.getChild(0).getCharacters());
    }

    // Gets references
    List<XMLNode> refNodes = metadata.getChildElements(REFERENCE_TAG, "");
    int numRefNodes = refNodes.size();
    references = new ReferenceImpl[numRefNodes];
    for (int i = 0; i < numRefNodes; i++) {
      XMLNode refNode = refNodes.get(i);
      references[i] = new ReferenceSBMLNode(refNode).toReference();
    }
  }

  public ModelRuleAnnotation(String formulaName, ModelClass modelClass, int pmmlabID,
      Reference[] references) {
    // Builds metadata node
    XMLNode metadataNode = new XMLNode(new XMLTriple("metadata", null, "pmf"));
    annotation = new Annotation();
    annotation.setNonRDFAnnotation(metadataNode);

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


/** Contents of a FSKX archive. */
class FSKFiles {
  File modelScript;
  File paramScript;
  File vizScript;
  File workspace;
  File metaData;

  public FSKFiles(BufferedDataTable rTable, BufferedDataTable metaDataTable, PortObject workspace)
      throws IOException {

    DataRow row = rTable.iterator().next();
    StringCell modelCell = (StringCell) row.getCell(FSKXTuple.KEYS.ORIG_MODEL.ordinal());
    StringCell paramCell = (StringCell) row.getCell(FSKXTuple.KEYS.ORIG_PARAM.ordinal());
    StringCell vizCell = (StringCell) row.getCell(FSKXTuple.KEYS.ORIG_VIZ.ordinal());

    // write modelCell to File
    modelScript = File.createTempFile("model", ".R");
    modelScript.deleteOnExit();
    FileWriter modelFileWriter = new FileWriter(modelScript);
    modelFileWriter.write(modelCell.getStringValue());
    modelFileWriter.close();

    // write paramCell to File
    if (!paramCell.getStringValue().isEmpty()) {
      paramScript = File.createTempFile("param", ".R");
      paramScript.deleteOnExit();
      FileWriter paramFileWriter = new FileWriter(paramScript);
      paramFileWriter.write(paramCell.getStringValue());
      paramFileWriter.close();
    }

    // write vizCell to File
    if (!vizCell.getStringValue().isEmpty()) {
      vizScript = File.createTempFile("visualization", ".R");
      vizScript.deleteOnExit();
      FileWriter vizFileWriter = new FileWriter(vizScript);
      vizFileWriter.write(vizCell.getStringValue());
      vizFileWriter.close();
    }

    // handle metaDataTable
    KnimeSchema schema = new OpenFSMRSchema();
    KnimeTuple tuple = PmmUtilities.getTuples(metaDataTable, schema).get(0);
    SBMLDocument doc = createSBMLDocument(tuple);
    metaData = File.createTempFile("metaData", ".pmf");
    metaData.deleteOnExit();
    try {
      new SBMLWriter().write(doc, metaData);
    } catch (SBMLException | XMLStreamException e) {
      throw new IOException(e.getMessage());
    }

    // R workspace
    this.workspace = ((RPortObject) workspace).getFile();
  }

  /** Creates SBMLDocument out of a OpenFSMR tuple. */
  private SBMLDocument createSBMLDocument(final KnimeTuple tuple) {

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
}


