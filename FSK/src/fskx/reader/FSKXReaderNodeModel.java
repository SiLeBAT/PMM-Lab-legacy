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
package fskx.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Unit;

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.reader.Util;
import de.bund.bfr.knime.pmm.common.units.Categories;
import de.bund.bfr.knime.pmm.dbutil.DBUnits;
import de.bund.bfr.pmf.file.uri.URIFactory;
import de.bund.bfr.pmf.sbml.Correlation;
import de.bund.bfr.pmf.sbml.Limits;
import de.bund.bfr.pmf.sbml.Model1Annotation;
import de.bund.bfr.pmf.sbml.ModelRule;
import de.bund.bfr.pmf.sbml.ModelVariable;
import de.bund.bfr.pmf.sbml.PMFCoefficient;
import de.bund.bfr.pmf.sbml.PMFCompartment;
import de.bund.bfr.pmf.sbml.PMFSpecies;
import de.bund.bfr.pmf.sbml.Reference;
import de.bund.bfr.pmf.sbml.SBMLFactory;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import fskx.FSKXTuple;
import fskx.RMetaDataNode;
import fskx.RUri;

public class FSKXReaderNodeModel extends NodeModel {

  // configuration keys
  public static final String CFGKEY_FILE = "filename";

  // defaults for persistent state
  private static final String DEFAULT_FILE = "c:/temp/foo.numl";

  // defaults for persistent state
  private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE, DEFAULT_FILE);

  protected FSKXReaderNodeModel() {
    // 0 input ports and 2 input port
    super(0, 2);
  }

  /** {@inheritDoc} */
  @Override
  protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
      final ExecutionContext exec) throws FileAccessException {

    String filepath = filename.getStringValue();

    /**
     * Try to open the CombineArchive. Should an error occur, a FileAccessException would be thrown.
     */
    CombineArchive combineArchive;
    try {
      combineArchive = new CombineArchive(new File(filepath));
    } catch (IOException | JDOMException | ParseException | CombineArchiveException e) {
      throw new FileAccessException("Error opening " + filepath);
    }

    final Set<String> librariesSet = new HashSet<>(); // Set of libraries
    final Set<String> sourcesSet = new HashSet<>(); // Set of sources

    // Gets annotation
    final Element xmlElement = combineArchive.getDescriptions().get(0).getXmlDescription();
    final RMetaDataNode metaDataNode = new RMetaDataNode(xmlElement);

    final List<ArchiveEntry> rEntriesList =
        combineArchive.getEntriesWithFormat(new RUri().createURI());
    final Map<String, ArchiveEntry> rEntriesMap = new HashMap<>(rEntriesList.size());
    for (final ArchiveEntry entry : rEntriesList) {
      rEntriesMap.put(entry.getFileName(), entry);
    }

    /**
     * Looks for model script. Since the model script is mandatory, it closes the CombineArchive and
     * throws a FileAccessException when the model script cannot be retrieved.
     */
    final String modelScriptFileName = metaDataNode.getMainScript();
    final String modelScriptString;
    if (modelScriptFileName == null) {
      closeCombineArchive(combineArchive);
      throw new FileAccessException("Model script could not be accessed");
    } else {
      try {
        modelScriptString = getTextFromArchiveEntry(rEntriesMap.get(modelScriptFileName));

        // Extract libraries and sources from the parameters script
        final List<String> modelScriptLines = extractLinesFromText(modelScriptString);
        librariesSet.addAll(extractLibrariesFromLines(modelScriptLines));
        sourcesSet.addAll(extractSourcesFromLines(modelScriptLines));
      } catch (IOException e) {
        closeCombineArchive(combineArchive);
        throw new FileAccessException("Model script could not be accessed");
      }
    }

    /**
     * Looks for parameters script. Since the parameter script is optional, it produces an empty
     * script (empty string) if the parameter script could not be retrieved.
     */
    final String paramScriptFileName = metaDataNode.getParametersScript();
    String paramScriptString;
    if (paramScriptFileName == null) {
      paramScriptString = "";
    } else {
      try {
        paramScriptString = getTextFromArchiveEntry(rEntriesMap.get(paramScriptFileName));

        // Extract libraries and sources from the parameters script
        final List<String> paramScriptLines = extractLinesFromText(paramScriptString);
        librariesSet.addAll(extractLibrariesFromLines(paramScriptLines));
        sourcesSet.addAll(extractSourcesFromLines(paramScriptLines));
      } catch (IOException e) {
        paramScriptString = "";
      }
    }

    /**
     * Looks for visualization script. Since the visualization script is optional, it produces an
     * empty script (empty string) if the visualization script could not be retrieved.
     */
    final String vizScriptFileName = metaDataNode.getVisualizationScript();
    String vizScriptString;
    if (vizScriptFileName == null) {
      vizScriptString = "";
    } else {
      try {
        vizScriptString = getTextFromArchiveEntry(rEntriesMap.get(vizScriptFileName));

        // Extract libraries and sources from the visualization script
        final List<String> vizScriptLines = extractLinesFromText(vizScriptString);
        librariesSet.addAll(extractLibrariesFromLines(vizScriptLines));
        sourcesSet.addAll(extractSourcesFromLines(vizScriptLines));
      } catch (IOException e) {
        vizScriptString = "";
      }
    }

    /**
     * Process the SBMLDocument with the model meta data. Should an error occur the meta data table
     * will be empty.
     */
    final ArchiveEntry modelEntry =
        combineArchive.getEntriesWithFormat(URIFactory.createPMFURI()).get(0);
    KnimeTuple tuple;
    try {
      final InputStream stream =
          Files.newInputStream(modelEntry.getPath(), StandardOpenOption.READ);
      final SBMLDocument sbmlDoc = new SBMLReader().readSBMLFromStream(stream);
      tuple = processMetadata(sbmlDoc);
    } catch (IOException | XMLStreamException e) {
      tuple = new KnimeTuple(SchemaFactory.createM1DataSchema());
    }

    closeCombineArchive(combineArchive);

    // Creates column spec, table spec and container
    final DataColumnSpecCreator mainScriptSpecCreator =
        new DataColumnSpecCreator("RModel", StringCell.TYPE);
    final DataColumnSpecCreator paramsScriptSpecCreator =
        new DataColumnSpecCreator("Parameters", StringCell.TYPE);
    final DataColumnSpecCreator vizScriptSpecCreator =
        new DataColumnSpecCreator("Visualization", StringCell.TYPE);
    final DataColumnSpecCreator rLibsSpecCreator =
        new DataColumnSpecCreator("RLibraries", StringCell.TYPE);
    final DataColumnSpecCreator rSourcesSpecCreator =
        new DataColumnSpecCreator("RSources", StringCell.TYPE);


    final DataColumnSpec[] colSpec = new DataColumnSpec[] {mainScriptSpecCreator.createSpec(),
        paramsScriptSpecCreator.createSpec(), vizScriptSpecCreator.createSpec(),
        rLibsSpecCreator.createSpec(), rSourcesSpecCreator.createSpec()};
    final DataTableSpec tableSpec = new DataTableSpec(colSpec);
    final BufferedDataContainer dataContainer = exec.createDataContainer(tableSpec);

    // Adds row and closes the container
    final FSKXTuple row = new FSKXTuple(modelScriptString, paramScriptString, vizScriptString,
        librariesSet, sourcesSet);
    dataContainer.addRowToTable(row);
    dataContainer.close();

    // Creates model table spec and container
    final DataTableSpec modelTableSpec = SchemaFactory.createM1DataSchema().createSpec();
    final BufferedDataContainer modelContainer = exec.createDataContainer(modelTableSpec);
    modelContainer.addRowToTable(tuple);
    modelContainer.close();

    return new BufferedDataTable[] {dataContainer.getTable(), modelContainer.getTable()};
  }

  /** {@inheritDoc} */
  @Override
  protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
      throws InvalidSettingsException {
    return new DataTableSpec[] {null, null};
  }

  /** {@inheritDoc} */
  @Override
  protected void saveSettingsTo(final NodeSettingsWO settings) {
    filename.saveSettingsTo(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
      throws InvalidSettingsException {
    filename.loadSettingsFrom(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    filename.validateSettings(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  /** {@inheritDoc} */
  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {};

  /** {@inheritDoc} */
  @Override
  protected void reset() {}

  private KnimeTuple processMetadata(final SBMLDocument sbmlDocument) {
    /**
     * <ol>
     * <li>Gets SBML model
     * <li>Parses annotation
     * <li>Gets rule and converts it to CatalogModelXml
     * <li>Parse constraints
     * <li>Gets species
     * <li>Gets matrix
     * <li>Gets dependent variable
     * <li>Gets limits for the dependent variable
     * <li>Parse independent variables
     * <li>Gets limits for the independent variables
     * <li>Parse independent variables
     * <li>Gets limits for the independent variables
     * <li>Parse constant parameters
     * <li>Creates EstModelXml
     * <li>Creates PMFCompartment
     * <li>Reads model literature
     * <li>Reads estimated model literature
     * <li>Creates and returns tuple
     * <li>Creates PmmXmlDoc with MiscXmls
     * <li>Creates MdInfoXml
     */

    // Gets SBML model
    final Model model = sbmlDocument.getModel();

    // Parses annotation
    final Model1Annotation m1Annot = new Model1Annotation(model.getAnnotation());

    // Gets rule and converts it to CatalogModelXml
    final ModelRule rule = new ModelRule((AssignmentRule) model.getRule(0));
    final int formulaId = rule.getPmmlabID();
    final String formulaName = rule.getFormulaName();
    final int modelClass = rule.getModelClass().ordinal();
    final CatalogModelXml catModel = new CatalogModelXml(formulaId, formulaName, null, modelClass);

    // Parse constraints
    final Map<String, Limits> limits = Util.parseConstraints(model.getListOfConstraints());

    // Gets species
    final PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
    final AgentXml agentXml = new AgentXml(MathUtilities.getRandomNegativeInt(), species.getName(),
        species.getDetail(), null);

    // Creates PMFCompartment
    final PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
    final MatrixXml matrixXml = new MatrixXml(MathUtilities.getRandomNegativeInt(),
        compartment.getName(), compartment.getDetail(), null);

    // Gets dependent variable
    final DepXml depXml = new DepXml("Value");
    final String depUnitID = species.getUnits();
    if (depUnitID != null) {
      if (depUnitID.equals("dimensionless")) {
        depXml.setUnit("dimensionless");
        depXml.setCategory("Dimensionless quantity");
      } else {
        final String depUnitName = model.getUnitDefinition(depUnitID).getName();
        depXml.setUnit(depUnitName);
        if (DBUnits.getDBUnits().containsKey(depUnitName)) {
          depXml.setCategory(DBUnits.getDBUnits().get(depUnitName).getKind_of_property_quantity());
        }
      }
    }
    if (species.isSetDescription()) {
      depXml.setDescription(species.getDescription());
    }

    // Gets limits for the dependent variable
    if (limits.containsKey(species.getId())) {
      final Limits depLimits = limits.get(species.getId());
      depXml.setMax(depLimits.getMax());
      depXml.setMin(depLimits.getMin());
    }

    // Parse independent variables
    final Parameter indepParam = model.getParameter(Categories.getTime());
    final IndepXml indepXml = new IndepXml(indepParam.getId(), null, null);
    final String indepUnitID = indepParam.getUnits();
    if (!indepUnitID.isEmpty()
        && !indepUnitID.equalsIgnoreCase(Unit.Kind.DIMENSIONLESS.getName())) {
      final String unitName = model.getUnitDefinition(indepUnitID).getName();
      indepXml.setUnit(unitName);
      indepXml.setCategory(Categories.getTimeCategory().getName());
      indepXml.setDescription(Categories.getTime());
    }

    // Get limits for the independent variable
    if (limits.containsKey(indepParam.getId())) {
      final Limits indepLimits = limits.get(indepParam.getId());
      indepXml.setMax(indepLimits.getMax());
      indepXml.setMin(indepLimits.getMin());
    }

    // Parse constant parameters
    final PmmXmlDoc paramCell = new PmmXmlDoc();
    for (final Parameter param : model.getListOfParameters()) {
      if (!param.isConstant())
        continue;

      final ParamXml paramXml = new ParamXml(param.getId(), null, param.getValue());

      final String unitID = param.getUnits();
      if (!unitID.equals(Unit.Kind.DIMENSIONLESS.getName())) {
        final String unitName = model.getUnitDefinition(unitID).getName();
        paramXml.setUnit(unitName);
        if (DBUnits.getDBUnits().containsKey(unitName)) {
          paramXml.setCategory(DBUnits.getDBUnits().get(unitName).getKind_of_property_quantity());
        }
      }

      final PMFCoefficient coefficient = SBMLFactory.createPMFCoefficient(param);
      if (coefficient.isSetP()) {
        paramXml.setP(coefficient.getP());
      }
      if (coefficient.isSetError()) {
        paramXml.setError(coefficient.getError());
      }
      if (coefficient.isSetT()) {
        paramXml.setT(coefficient.getT());
      }
      if (coefficient.isSetDescription()) {
        paramXml.setDescription(coefficient.getDescription());
      }
      if (coefficient.isSetCorrelations()) {
        for (Correlation correlation : coefficient.getCorrelations()) {
          paramXml.addCorrelation(correlation.getName(), correlation.getValue());
        }
      }
      // Adds limits
      if (limits.containsKey(param.getId())) {
        final Limits constLimits = limits.get(param.getId());
        paramXml.setMax(constLimits.getMax());
        paramXml.setMin(constLimits.getMin());
      }
      paramCell.add(paramXml);
    }

    // Creates EstModelXml
    final EstModelXml estModel = Util.uncertainties2EstModel(m1Annot.getUncertainties());
    if (model.isSetName()) {
      estModel.setName(model.getName());
    }

    // Reads model literature
    final PmmXmlDoc mLit = new PmmXmlDoc();
    for (final Reference ref : rule.getReferences()) {
      final String author = ref.getAuthor();
      final Integer year = ref.getYear();
      final String title = ref.getTitle();
      final String abstractText = ref.getAbstractText();
      final String journal = ref.getJournal();
      final String volume = ref.getVolume();
      final String issue = ref.getIssue();
      final Integer page = ref.getPage();
      final Integer approvalMode = ref.getApprovalMode();
      final String website = ref.getWebsite();
      final Integer type = ref.isSetType() ? ref.getType().value() : null;
      final String comment = ref.getComment();

      final LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal,
          volume, issue, page, approvalMode, website, type, comment);
      mLit.add(lit);
    }

    // Reads estimated model literature
    final PmmXmlDoc emLit = new PmmXmlDoc();
    for (final Reference ref : rule.getReferences()) {
      final String author = ref.getAuthor();
      final Integer year = ref.getYear();
      final String title = ref.getTitle();
      final String abstractText = ref.getAbstractText();
      final String journal = ref.getJournal();
      final String volume = ref.getVolume();
      final String issue = ref.getIssue();
      final Integer page = ref.getPage();
      final Integer approvalMode = ref.getApprovalMode();
      final String website = ref.getWebsite();
      final Integer type = ref.isSetType() ? ref.getType().value() : null;
      final String comment = ref.getComment();

      final LiteratureItem lit = new LiteratureItem(author, year, title, abstractText, journal,
          volume, issue, page, approvalMode, website, type, comment);
      emLit.add(lit);
    }

    // Creates PmmXmlDoc with MiscXmls
    final PmmXmlDoc miscCell;
    if (compartment.isSetModelVariables()) {
      final Map<String, Double> miscs = new HashMap<>(compartment.getModelVariables().length);
      for (final ModelVariable modelVariable : compartment.getModelVariables()) {
        miscs.put(modelVariable.getName(), modelVariable.getValue());
      }
      miscCell = Util.parseMiscs(miscs);
    } else {
      miscCell = new PmmXmlDoc();
    }

    // Creates MdInfoXml
    final MdInfoXml mdInfo = new MdInfoXml(null, null, null, null, null);

    // Creates and return tuple
    final KnimeTuple tuple = new KnimeTuple(SchemaFactory.createM1DataSchema());
    // TimeSeriesSchema cells
    tuple.setValue(TimeSeriesSchema.ATT_CONDID, m1Annot.getCondID());
    tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, "?");
    tuple.setValue(TimeSeriesSchema.ATT_AGENT, new PmmXmlDoc(agentXml));
    tuple.setValue(TimeSeriesSchema.ATT_MATRIX, new PmmXmlDoc(matrixXml));
    tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, new PmmXmlDoc());
    tuple.setValue(TimeSeriesSchema.ATT_MISC, miscCell);
    tuple.setValue(TimeSeriesSchema.ATT_MDINFO, new PmmXmlDoc(mdInfo));
    tuple.setValue(TimeSeriesSchema.ATT_LITMD, new PmmXmlDoc());
    tuple.setValue(TimeSeriesSchema.ATT_DBUUID, "?");
    // Model1Schema cells
    tuple.setValue(Model1Schema.ATT_MODELCATALOG, new PmmXmlDoc(catModel));
    tuple.setValue(Model1Schema.ATT_DEPENDENT, new PmmXmlDoc(depXml));
    tuple.setValue(Model1Schema.ATT_INDEPENDENT, new PmmXmlDoc(indepXml));
    tuple.setValue(Model1Schema.ATT_PARAMETER, paramCell);
    tuple.setValue(Model1Schema.ATT_ESTMODEL, new PmmXmlDoc(estModel));
    tuple.setValue(Model1Schema.ATT_MLIT, mLit);
    tuple.setValue(Model1Schema.ATT_EMLIT, emLit);
    tuple.setValue(Model1Schema.ATT_DATABASEWRITABLE, Model1Schema.WRITABLE);
    tuple.setValue(Model1Schema.ATT_DBUUID, "?");
    return tuple;
  }

  /**
   * Reads all the lines in a text string. Skips empty lines or lines with only whitespace
   * characters (tabs, newline, ...).
   * 
   * @param text
   * @return List of lines.
   */
  private List<String> extractLinesFromText(final String text) {
    final List<String> lines = new LinkedList<>();
    final BufferedReader reader = new BufferedReader(new StringReader(text));

    while (true) {
      String line;
      try {
        line = reader.readLine();
        if (line == null) {
          break;
        } else {
          line = line.trim(); // Remove leading and trailing whitespace
          if (!line.isEmpty()) {
            lines.add(line);
          }
        }
      } catch (IOException e) {
        // If an I/O error occurs while reading this line, skips this line
        e.printStackTrace();
      }
    }

    return lines;
  }

  /**
   * Obtain the libraries used in a list of lines from an R code.
   * 
   * @param lines Lines from an R code.
   * @return List of libraries names.
   */
  private List<String> extractLibrariesFromLines(final List<String> lines) {
    final List<String> libraryNames = new LinkedList<>();

    final String regex1 = "^\\s*\\b(library|require)\\((\"?.+\"?)\\)";
    // final String regex2 = "library|require\((\w)\)";

    final Pattern PATTERN = Pattern.compile(regex1);
    for (final String line : lines) {
      final Matcher matcher = PATTERN.matcher(line);
      if (matcher.find()) {
        final String libraryName = matcher.group(2).replace("\"", "");
        libraryNames.add(libraryName);
      }
    }

    return libraryNames;
  }

  /**
   * Obtain the sources files used in a list of lines from an R code.
   * 
   * @param lines Lines from an R code.
   * @return List of source filenames.
   */
  private List<String> extractSourcesFromLines(final List<String> lines) {
    final List<String> sourceNames = new LinkedList<>();

    final String regex1 = "^\\s*\\b(source)\\((\"?.+\"?)\\)";
    // final String regex2 = "library|require\((\w)\)";

    final Pattern PATTERN = Pattern.compile(regex1);
    for (final String line : lines) {
      final Matcher matcher = PATTERN.matcher(line);
      if (matcher.find()) {
        final String sourceName = matcher.group(2).replace("\"", "");
        sourceNames.add(sourceName);
      }
    }

    return sourceNames;
  }

  /**
   * Gets text content from an ArchiveEntry of a CombineArchive.
   * 
   * @throws IOException if an I/O error occurs
   */
  private String getTextFromArchiveEntry(final ArchiveEntry entry) throws IOException {
    final InputStream stream = Files.newInputStream(entry.getPath(), StandardOpenOption.READ);
    final String text = IOUtils.toString(stream, StandardCharsets.UTF_8);

    return text;
  }

  private void closeCombineArchive(final CombineArchive combineArchive) {
    try {
      combineArchive.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  class FileAccessException extends Exception {

    private static final long serialVersionUID = 1L;

    public FileAccessException(final String descr) {
      super(descr);
    }
  }
}
