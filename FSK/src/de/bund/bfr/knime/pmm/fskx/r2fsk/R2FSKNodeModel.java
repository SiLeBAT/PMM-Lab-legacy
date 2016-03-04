package de.bund.bfr.knime.pmm.fskx.r2fsk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettings;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.util.FileUtil;

import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.common.KnimeUtils;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.fskx.FSKUtil;
import de.bund.bfr.knime.pmm.fskx.FSKXTuple;
import de.bund.bfr.knime.pmm.fskx.FSKXTuple.KEYS;
import de.bund.bfr.knime.pmm.fskx.MissingValueError;
import de.bund.bfr.knime.pmm.fskx.RScript;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;

public class R2FSKNodeModel extends NodeModel {

  // configuration key of the path of the R model script
  static final String CFGKEY_MODEL_SCRIPT = "modelScript";

  // configuration key of the path of the R parameters script
  static final String CFGKEY_PARAM_SCRIPT = "paramScript";

  // configuration key of the path of the R visualization script
  static final String CFGKEY_VISUALIZATION_SCRIPT = "visualizationScript";

  // configuration key of the path of the XLSX spreadsheet with the model meta data
  static final String CFGKEY_SPREADSHEET = "spreadsheet";

  // Settings models
  private SettingsModelString modelScriptPath = new SettingsModelString(CFGKEY_MODEL_SCRIPT, null);
  private SettingsModelString paramScriptPath = new SettingsModelString(CFGKEY_PARAM_SCRIPT, null);
  private SettingsModelString visualizationScriptPath =
      new SettingsModelString(CFGKEY_VISUALIZATION_SCRIPT, null);
  private SettingsModelString spreadsheetPath = new SettingsModelString(CFGKEY_SPREADSHEET, null);

  // *** Internal Model Keys ***
  private static final String FILE_NAME = "r2fskNodeInternals.xml";
  private static final String INTERNAL_MODEL = "internalModel";

  /** {@inheritDoc} */
  protected R2FSKNodeModel() {
    super(0, 2);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    File file = new File(nodeInternDir, FILE_NAME);
    try (FileInputStream fis = new FileInputStream(file)) {
      NodeSettingsRO settings = NodeSettings.loadFromXML(fis);
      loadValidatedSettingsFrom(settings);
    } catch (InvalidSettingsException e) {
      System.err.println(e.getMessage());
    } catch (IOException e) {
      throw e;
    }
  }

  /** {@inheritDoc} */
  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // Creates and saves a file in the given directory
    File file = new File(nodeInternDir, FILE_NAME);
    try (FileOutputStream fos = new FileOutputStream(file)) {
      saveSettingsTo(new NodeSettings(INTERNAL_MODEL));
    } catch (IOException e) {
      throw e;
    }
  }

  /** {@inheritDoc} */
  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    this.modelScriptPath.saveSettingsTo(settings);
    this.paramScriptPath.saveSettingsTo(settings);
    this.visualizationScriptPath.saveSettingsTo(settings);
    this.spreadsheetPath.saveSettingsTo(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    this.modelScriptPath.validateSettings(settings);
    this.paramScriptPath.validateSettings(settings);
    this.visualizationScriptPath.validateSettings(settings);
    this.spreadsheetPath.validateSettings(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    this.modelScriptPath.loadSettingsFrom(settings);
    this.paramScriptPath.loadSettingsFrom(settings);
    this.visualizationScriptPath.loadSettingsFrom(settings);
    this.spreadsheetPath.loadSettingsFrom(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void reset() {
    // does nothing
  }

  /**
   * {@inheritDoc}
   * 
   * @throws MissingValueError
   * @throws Exception
   */
  @Override
  protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
      final ExecutionContext exec) throws InvalidSettingsException, IOException, MissingValueError {
    BufferedDataTable rTable = createRTable(exec);
    BufferedDataTable metaDataTable = createMetaDataTable(exec);

    return new BufferedDataTable[] {rTable, metaDataTable};
  }

  /** {@inheritDoc} */
  @Override
  protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
      throws InvalidSettingsException {
    return new DataTableSpec[] {null, null};
  }

  /**
   * Reads R script.
   * 
   * @param path File path to R model script.
   * @throws InvalidSettingsException if {@link path} is null or whitespace.
   * @throws IOException if the file cannot be read.
   */
  private static RScript readScript(final String path)
      throws InvalidSettingsException, IOException {

    // throws InvalidSettingsException if path is null
    if (path == null) {
      throw new InvalidSettingsException("Unespecified script");
    }

    // throws InvalidSettingsException if path is whitespace
    String trimmedPath = Strings.emptyToNull(path.trim());
    if (trimmedPath == null) {
      throw new InvalidSettingsException("Unespecified model script");
    }

    // path is not null or whitespace, thus try to read it
    try {
      RScript script = new RScript(KnimeUtils.getFile(trimmedPath)); // may throw IOException
      return script;
    } catch (IOException e) {
      System.err.println(e.getMessage());
      throw new IOException(trimmedPath + ": cannot be read");
    }
  }

  /**
   * Creates R table.
   * 
   * @param exec Execution context
   * @throws InvalidSettingsException if the path to the model script is null or whitespace
   * @throws IOException if the model script file cannot be read
   * @return BufferedDataTable
   * @throws InvalidSettingsException | IOException
   * @throws MissingValueError
   */
  private BufferedDataTable createRTable(final ExecutionContext exec)
      throws InvalidSettingsException, IOException, MissingValueError {
    final Set<String> librariesSet = new HashSet<>(); // Set of libraries
    final Set<String> sourcesSet = new HashSet<>(); // Set of sources
    final EnumMap<FSKXTuple.KEYS, String> valuesMap = new EnumMap<>(KEYS.class);

    // Reads model script. Since the model script is mandatory, if any error occurs it re-throws it.
    try {
      RScript script = readScript(this.modelScriptPath.getStringValue()); // may throw errors

      // if no errors occur, add scripts, libraries and sources
      valuesMap.put(KEYS.ORIG_MODEL, script.getOriginalScript());
      valuesMap.put(KEYS.SIMP_MODEL, script.getSimplifiedScript());
      librariesSet.addAll(script.getLibraries());
      sourcesSet.addAll(script.getSources());
      exec.setProgress(0.25);
    } catch (InvalidSettingsException | IOException e) {
      throw e;
    }

    // Reads parameters script. The parameters script is optional.
    try {
      RScript script = readScript(this.paramScriptPath.getStringValue()); // may throw errors

      // if no errors occur, add scripts, libraries, and sources
      valuesMap.put(KEYS.ORIG_PARAM, script.getOriginalScript());
      valuesMap.put(KEYS.SIMP_PARAM, script.getSimplifiedScript());
      librariesSet.addAll(script.getLibraries());
      sourcesSet.addAll(script.getSources());
      exec.setProgress(0.5);
    } catch (InvalidSettingsException e) {
      System.err.println(e.getMessage());
    } catch (IOException e) {
      System.err.println(e.getMessage());
      setWarningMessage(e.getMessage());
    }

    /**
     * Reads visualization script. The visualization script is optional, thus if the path is null or
     * whitespace only a warning will be printed.
     */
    try {
      RScript script = readScript(this.visualizationScriptPath.getStringValue()); // may throw
                                                                                  // errors

      // if no errors occur, add scripts, libraries, and sources
      valuesMap.put(KEYS.ORIG_VIZ, script.getOriginalScript());
      valuesMap.put(KEYS.SIMP_VIZ, script.getSimplifiedScript());
      librariesSet.addAll(script.getLibraries());
      sourcesSet.addAll(script.getSources());
      exec.setProgress(0.75);
    } catch (InvalidSettingsException e) {
      System.err.println(e.getMessage());
    } catch (IOException e) {
      System.err.println(e.getMessage());
      setWarningMessage(e.getMessage());
    }

    valuesMap.put(KEYS.LIBS, String.join(";", librariesSet)); // adds R libraries
    valuesMap.put(KEYS.SOURCES, String.join(";", sourcesSet)); // adds R sources

    // Creates table spec and container
    DataTableSpec spec = FSKUtil.createFSKTableSpec();
    BufferedDataContainer container = exec.createDataContainer(spec);

    // Adds row and closes the container
    FSKXTuple tuple = new FSKXTuple(valuesMap);
    container.addRowToTable(tuple);
    container.close();

    return container.getTable();
  }

  /**
   * Creates a {@link BufferedDataTable} with the meta data obtained from the given spreadsheet. If
   * an error occurs or the path is not specified the table will be empty.
   * 
   * @param exec Execution context
   * @return BufferedDataTable
   */
  private BufferedDataTable createMetaDataTable(final ExecutionContext exec) {

    DataTableSpec spec = new OpenFSMRSchema().createSpec();
    BufferedDataContainer container = exec.createDataContainer(spec);

    if (!Strings.isNullOrEmpty(this.spreadsheetPath.getStringValue())) {
      try (InputStream fis = FileUtil.openInputStream(this.spreadsheetPath.getStringValue())) {
        // Finds the workbook instance for XLSX file
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        fis.close();

        FSMRTemplate template = FSMRUtils.processSpreadsheet(workbook);
        KnimeTuple tuple = FSMRUtils.createTupleFromTemplate(template);
        container.addRowToTable(tuple);
      } catch (IOException | InvalidSettingsException e) {
        e.printStackTrace();
      }
    }

    container.close();

    return container.getTable();
  }
}
