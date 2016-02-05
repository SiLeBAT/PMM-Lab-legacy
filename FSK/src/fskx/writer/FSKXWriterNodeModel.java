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
package fskx.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.jdom2.JDOMException;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
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
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.SBMLWriter;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.common.writer.TableReader;
import de.bund.bfr.knime.pmm.common.writer.Util;
import de.bund.bfr.pmf.ModelClass;
import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.PMFUtil;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.sbml.LimitsConstraint;
import de.bund.bfr.pmf.sbml.Metadata;
import de.bund.bfr.pmf.sbml.MetadataAnnotation;
import de.bund.bfr.pmf.sbml.Model1Annotation;
import de.bund.bfr.pmf.sbml.PMFCoefficient;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.bund.bfr.pmf.sbml.Reference;
import de.bund.bfr.pmf.sbml.ReferenceImpl;
import de.bund.bfr.pmf.sbml.ReferenceSBMLNode;
import de.bund.bfr.pmf.sbml.SBMLFactory;
import de.bund.bfr.pmf.sbml.Uncertainties;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;
import fskx.RMetaDataNode;
import fskx.RUri;

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

  protected FSKXWriterNodeModel() {
    super(2, 0);

    // Sets current date in the dialog components
    long currentDate = Calendar.getInstance().getTimeInMillis();
    createdDate.setTimeInMillis(currentDate);
    modifiedDate.setTimeInMillis(currentDate);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws FileCreationException If a critical file could not be created. E.g. model script.
   */
  @Override
  protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
      final ExecutionContext exec) throws FileCreationException {

    // Creates file for the CombineArchive
    final File caFile = new File(filePath.getStringValue());
    if (caFile.exists()) {
      caFile.delete();
    }

    // The input of the writer currently can be any table generated in KNIME that has the model
    // script and the visualization script as the 1st and 2nd columns respectively. However, it
    // is desirable to use FSKXTuple in the future.
    final DataRow row = inData[0].iterator().next();
    final StringCell modelCell = (StringCell) row.getCell(0);
    final StringCell paramsScriptCell = (StringCell) row.getCell(1);
    final StringCell vizScriptCell = (StringCell) row.getCell(2);

    // Variables for the CombineArchive
    final URI rURI = new RUri().createURI();
    final RMetaDataNode metaDataNode = new RMetaDataNode();

    // Try to create CombineArchive. If an error occurs a FileCreationException will be thrown.
    final CombineArchive combineArchive;
    try {
      combineArchive = new CombineArchive(new File(filePath.getStringValue()));
    } catch (IOException | JDOMException | ParseException | CombineArchiveException e) {
      throw new FileCreationException("CombineArchive could not be created");
    }

    /**
     * Creates a file with the R model and adds it to the CombineArchive. Since the R model is
     * mandatory, should an I/O error occur the CombineArchive would be immediately closed and a
     * FileCreationException would be thrown.
     */
    try {
      final File rFile = writeToTempFile(modelCell.getStringValue());
      final String fileName = "model.R";
      combineArchive.addEntry(rFile, fileName, rURI);
      metaDataNode.setMainScript(fileName);
    } catch (IOException e) {
      closeCombineArchive(combineArchive);
      throw new FileCreationException("Model script could not be created");
    }

    /**
     * Creates a file with the R parameters and adds it to the CombineArchive. Since the R
     * parameters file is optional, should an I/O error occur it would be ignored.
     */
    final String paramScript = paramsScriptCell.getStringValue();
    if (!paramScript.isEmpty()) {
      try {
        final File paramFile = writeToTempFile(paramScript);
        final String fileName = "params.R";
        combineArchive.addEntry(paramFile, fileName, rURI);
        metaDataNode.setParamScript(fileName);
      } catch (IOException e) {
        e.printStackTrace(); // Print the stack trace and ignore the error
      }
    }

    /**
     * Creates a file with the R visualization script and adds it to the CombineArchive. Since the R
     * visualization script is optional, should a I/O error occur it would be ignored.
     */
    final String vizScript = vizScriptCell.getStringValue();
    if (!vizScript.isEmpty()) {
      try {
        final File vizFile = writeToTempFile(vizScript);
        final String fileName = "visualization.R";
        combineArchive.addEntry(vizFile, fileName, rURI);
        metaDataNode.setVisualizationScript(fileName);
      } catch (IOException e) {
        e.printStackTrace(); // Print the stack trace and ignore the error
      }
    }

    combineArchive.addDescription(new DefaultMetaDataObject(metaDataNode.getNode()));

    // Handles model metadata table
    final KnimeSchema schema = SchemaFactory.createM1DataSchema(); // Only support primary models
    final KnimeTuple tuple = PmmUtilities.getTuples(inData[1], schema).get(0);

    // Gets info from dialog
    Metadata metadata = SBMLFactory.createMetadata();
    metadata.setGivenName(creatorGivenName.getStringValue());
    metadata.setFamilyName(creatorFamilyName.getStringValue());
    metadata.setContact(creatorContact.getStringValue());
    if (createdDate.getSelectedFields() == 1) {
      metadata.setCreatedDate(createdDate.getDate().toString());
    }
    if (modifiedDate.getSelectedFields() == 1) {
      metadata.setModifiedDate(modifiedDate.getDate().toString());
    }
    metadata.setType(ModelType.PRIMARY_MODEL_WDATA);
    metadata.setRights(license.getStringValue());

    // Validate info from dialog: Throw warnings if empty fields
    if (!metadata.isSetGivenName()) {
      setWarningMessage("Given name missing");
    } else if (!metadata.isSetFamilyName()) {
      setWarningMessage("Creator family name missing");
    } else if (!metadata.isSetContact()) {
      setWarningMessage("Creator contact missing");
    } else if (!metadata.isSetCreatedDate()) {
      setWarningMessage("Created date missing");
    } else if (!metadata.isSetModifiedDate()) {
      setWarningMessage("Modified date msising");
    }

    final SBMLDocument sbmlDoc = createSBMLDocument(tuple, metadata, notes.getStringValue());

    /**
     * Creates a file with the model meta data (SBML) and adds it to the CombineArchive. Since the
     * meta data is mandatory, should an I/O error occur the CombineArchive would be immediately
     * closed and a FileCreationException would be thrown.
     */
    try {
      final File sbmlTmp = File.createTempFile("tempSbml", "");
      sbmlTmp.deleteOnExit();

      new SBMLWriter().write(sbmlDoc, sbmlTmp);
      final String fileName = sbmlDoc.getModel().getId() + ".pmf";
      combineArchive.addEntry(sbmlTmp, fileName, URIFactory.createPMFURI());
    } catch (IOException | SBMLException | XMLStreamException ex) {
      closeCombineArchive(combineArchive);
      throw new FileCreationException("Model meta data file could not be created");
    }

    // A TranformerException may occur when packing the CombineArchive
    try {
      combineArchive.pack();
    } catch (TransformerException | IOException ex) {
      closeCombineArchive(combineArchive);
      throw new FileCreationException("CombineArchive could not be packed properly");
    }

    /**
     * An IOException may occur when packing and closing the CombineArchive (when the file already
     * exists)
     */
    try {
      combineArchive.close();
    } catch (IOException e) {
      throw new FileCreationException("CombineArchive could not be created (not closed properly)");
    }

    return new BufferedDataTable[] {};
  }

  /** {@inheritDoc} */
  @Override
  protected void reset() {}

  /** {@inheritDoc} */
  @Override
  protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
      throws InvalidSettingsException {
    return new DataTableSpec[] {};
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

  private SBMLDocument createSBMLDocument(final KnimeTuple tuple, final Metadata metadata,
      final String notes) {
    /**
     * <ul>
     * <li>Retrieves TimeSeriesSchema cells
     * <li>Retrieves Model1Schema cells
     * <li>Creates SBMLDocument for the primary model
     * <li>Adds namespaces to the SBMLDocument
     * <li>Adds document annotation
     * <li>Creates model and names it
     * <li>Sets model notes
     * <li>Gets model references
     * <li>Gets estimated model references
     * <li>Gets uncertainty measures
     * <li>Creates and sets compartment
     * <li>Creates and sets species
     * <li>Adds dep constraint
     * <li>Adds independent parameter
     * <li>Adds independent parameter constraint
     * <li>Adds constant parameters
     * <li>Adds unit definitions
     * <li>Adds rule **
     */

    // Retrieves TimeSeriesSchema cells
    AgentXml agentXml = (AgentXml) tuple.getPmmXml(TimeSeriesSchema.ATT_AGENT).get(0);
    MatrixXml matrixXml = (MatrixXml) tuple.getPmmXml(TimeSeriesSchema.ATT_MATRIX).get(0);
    int condId = tuple.getInt(TimeSeriesSchema.ATT_CONDID);
    PmmXmlDoc miscDoc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

    // Retrieves Model1Schema cells
    CatalogModelXml catModel =
        (CatalogModelXml) tuple.getPmmXml(Model1Schema.ATT_MODELCATALOG).get(0);
    EstModelXml estModel = (EstModelXml) tuple.getPmmXml(Model1Schema.ATT_ESTMODEL).get(0);
    DepXml dep = (DepXml) tuple.getPmmXml(Model1Schema.ATT_DEPENDENT).get(0);
    IndepXml indep = (IndepXml) tuple.getPmmXml(Model1Schema.ATT_INDEPENDENT).get(0);
    PmmXmlDoc paramsDoc = tuple.getPmmXml(Model1Schema.ATT_PARAMETER);

    String modelId = PMFUtil.createId("model" + estModel.getId());

    // Creates SBMLDocument for the primary model
    final SBMLDocument sbmlDocument = new SBMLDocument(TableReader.LEVEL, TableReader.VERSION);

    // Adds namespaces to the sbmlDocument
    TableReader.addNamespaces(sbmlDocument);

    // Adds document annotation
    sbmlDocument.setAnnotation(new MetadataAnnotation(metadata).getAnnotation());

    // Creates model and names it
    Model model = sbmlDocument.createModel(modelId);
    if (estModel.getName() != null) {
      model.setName(estModel.getName());
    }

    // Sets model notes
    if (notes != null) {
      try {
        model.setNotes(notes);
      } catch (XMLStreamException e) {
        e.printStackTrace();
      }
    }

    // Gets model references
    List<LiteratureItem> mLits = new LinkedList<>();
    PmmXmlDoc mLitDoc = tuple.getPmmXml(Model1Schema.ATT_MLIT);
    for (PmmXmlElementConvertable item : mLitDoc.getElementSet()) {
      mLits.add((LiteratureItem) item);
    }

    // Gets estimated model references
    List<Reference> emLits = new LinkedList<>();
    PmmXmlDoc emLitDoc = tuple.getPmmXml(Model1Schema.ATT_EMLIT);
    for (PmmXmlElementConvertable item : emLitDoc.getElementSet()) {
      emLits.add(Util.literatureItem2Reference((LiteratureItem) item));
    }

    // Gets uncertainty measures
    Uncertainties uncertainties = Util.estModel2Uncertainties(estModel);
    model.setAnnotation(new Model1Annotation(uncertainties, emLits, condId).getAnnotation());

    // Creates and adds compartment to the model
    PMFCompartment compartment = Util.matrixXml2Compartment(matrixXml, miscDoc);
    model.addCompartment(compartment.getCompartment());

    // Creates and add species to the model
    PMFSpecies species = Util.createSpecies(agentXml, dep.getUnit(), compartment.getId());
    model.addSpecies(species.getSpecies());

    // Adds dep constraint
    LimitsConstraint depLc = new LimitsConstraint(species.getId(), dep.getMin(), dep.getMax());
    if (depLc.getConstraint() != null) {
      model.addConstraint(depLc.getConstraint());
    }

    // Adds independent parameter
    Parameter indepParam = new Parameter(Categories.getTime());
    indepParam.setValue(0.0);
    indepParam.setConstant(false);
    indepParam.setUnits(indep.getUnit());
    model.addParameter(indepParam);

    // Adds independent parameter constraint
    if (!indep.getName().isEmpty()) {
      LimitsConstraint lc = new LimitsConstraint(indep.getName(), indep.getMin(), indep.getMax());
      if (lc.getConstraint() != null) {
        model.addConstraint(lc.getConstraint());
      }
    }

    // Add constant parameters
    List<ParamXml> constXmls = new LinkedList<>();
    for (PmmXmlElementConvertable item : paramsDoc.getElementSet()) {
      constXmls.add((ParamXml) item);
    }

    for (ParamXml paramXml : constXmls) {
      // Adds constant parameter
      PMFCoefficient coefficient = Util.paramXml2Coefficient(paramXml);
      model.addParameter(coefficient.getParameter());

      // Adds constraint
      LimitsConstraint lc =
          new LimitsConstraint(paramXml.getName(), paramXml.getMin(), paramXml.getMax());
      if (lc.getConstraint() != null) {
        model.addConstraint(lc.getConstraint());
      }
    }

    // Adds unit definitions
    List<IndepXml> indepXmls = new LinkedList<>(Arrays.asList(indep));
    try {
      TableReader.addUnitDefinitions(model, dep, indepXmls, constXmls);
    } catch (XMLStreamException e) {
      e.printStackTrace();
    }

    // Creates rule of the model and adds it to the rest of rules
    Reference[] modelReferences = new ReferenceImpl[mLits.size()];
    for (int i = 0; i < mLits.size(); i++) {
      modelReferences[i] = Util.literatureItem2Reference(mLits.get(i));
    }

    ModelClass modelClass;
    if (catModel.getModelClass() == null) {
      modelClass = ModelClass.UNKNOWN;
    } else {
      modelClass = ModelClass.fromValue(catModel.getModelClass());
    }
    String formulaName = catModel.getName() == null ? catModel.getName() : "Missing formula name";
    int catModelId = catModel.getId();

    AssignmentRule rule = new AssignmentRule(3, 1);
    rule.setAnnotation(
        new ModelRuleAnnotation(formulaName, modelClass, catModelId, modelReferences).annotation);

    model.addRule(rule);

    return sbmlDocument;
  }

  /**
   * Write a String into a temporary file.
   * 
   * @param content
   * @return Temporary file.
   * @throws IOException If an I/O error occurs
   */
  private File writeToTempFile(final String content) throws IOException {
    final File tempFile = File.createTempFile("tmpFile", "");
    tempFile.deleteOnExit();
    final FileWriter fileWriter = new FileWriter(tempFile);
    fileWriter.write(content);
    fileWriter.close();

    return tempFile;
  }
  
  private void closeCombineArchive(final CombineArchive combineArchive) {
    try {
      combineArchive.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

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

