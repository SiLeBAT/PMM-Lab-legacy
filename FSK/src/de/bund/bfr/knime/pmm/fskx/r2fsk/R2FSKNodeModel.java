package de.bund.bfr.knime.pmm.fskx.r2fsk;

import java.io.File;
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
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.util.FileUtil;

import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.common.KnimeUtils;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.fskx.FSKUtil;
import de.bund.bfr.knime.pmm.fskx.FSKXTuple;
import de.bund.bfr.knime.pmm.fskx.FSKXTuple.KEYS;
import de.bund.bfr.knime.pmm.fskx.LibTuple;
import de.bund.bfr.knime.pmm.fskx.MissingValueError;
import de.bund.bfr.knime.pmm.fskx.RScript;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;

public class R2FSKNodeModel extends NodeModel {

  // configuration key of the libraries directory
  static final String CFGKEY_DIR_LIBS = "dirLibs";

  // configuration key of the selected libraries
  static final String CFGKEY_LIBS = "libs";

  // configuration key of the path of the R model script
  static final String CFGKEY_MODEL_SCRIPT = "modelScript";

  // configuration key of the path of the R parameters script
  static final String CFGKEY_PARAM_SCRIPT = "paramScript";

  // configuration key of the path of the R visualization script
  static final String CFGKEY_VISUALIZATION_SCRIPT = "visualizationScript";

  // configuration key of the path of the XLSX spreadsheet with the model meta data
  static final String CFGKEY_SPREADSHEET = "spreadsheet";

  // Settings models
  private SettingsModelString m_modelScript = new SettingsModelString(CFGKEY_MODEL_SCRIPT, null);
  private SettingsModelString m_paramScript = new SettingsModelString(CFGKEY_PARAM_SCRIPT, null);
  private SettingsModelString m_vizScript =
      new SettingsModelString(CFGKEY_VISUALIZATION_SCRIPT, null);
  private SettingsModelString m_metaDataDoc = new SettingsModelString(CFGKEY_SPREADSHEET, null);
  private SettingsModelString m_libDirectory = new SettingsModelString(CFGKEY_DIR_LIBS, null);
  private SettingsModelStringArray m_selectedLibs = new SettingsModelStringArray(CFGKEY_LIBS, null);

  /** {@inheritDoc} */
  protected R2FSKNodeModel() {
    super(0, 3);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // nothing
  }

  /** {@inheritDoc} */
  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // nothing
  }

  /** {@inheritDoc} */
  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    m_modelScript.saveSettingsTo(settings);
    m_paramScript.saveSettingsTo(settings);
    m_vizScript.saveSettingsTo(settings);
    m_metaDataDoc.saveSettingsTo(settings);
    m_libDirectory.saveSettingsTo(settings);
    m_selectedLibs.saveSettingsTo(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    m_modelScript.validateSettings(settings);
    m_paramScript.validateSettings(settings);
    m_vizScript.validateSettings(settings);
    m_metaDataDoc.validateSettings(settings);
    m_libDirectory.validateSettings(settings);
    m_selectedLibs.validateSettings(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    this.m_modelScript.loadSettingsFrom(settings);
    this.m_paramScript.loadSettingsFrom(settings);
    this.m_vizScript.loadSettingsFrom(settings);
    this.m_metaDataDoc.loadSettingsFrom(settings);
    m_libDirectory.loadSettingsFrom(settings);
    m_selectedLibs.loadSettingsFrom(settings);
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
    BufferedDataTable libTable = createLibTable(exec);

    return new BufferedDataTable[] {rTable, metaDataTable, libTable};
  }

  /** {@inheritDoc} */
  @Override
  protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
      throws InvalidSettingsException {
    return new DataTableSpec[] {null, null, null};
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
      RScript script = readScript(this.m_modelScript.getStringValue()); // may throw errors

      // if no errors occur, add scripts, libraries and sources
      valuesMap.put(KEYS.MODEL_SCRIPT, script.getScript());
      librariesSet.addAll(script.getLibraries());
      sourcesSet.addAll(script.getSources());
      exec.setProgress(0.25);
    } catch (InvalidSettingsException | IOException e) {
      throw e;
    }

    // Reads parameters script. The parameters script is optional.
    try {
      RScript script = readScript(this.m_paramScript.getStringValue()); // may throw errors

      // if no errors occur, add scripts, libraries, and sources
      valuesMap.put(KEYS.PARAM_SCRIPT, script.getScript());
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
      RScript script = readScript(this.m_vizScript.getStringValue()); // may throw
                                                                      // errors

      // if no errors occur, add scripts, libraries, and sources
      valuesMap.put(KEYS.VIZ_SCRIPT, script.getScript());
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

    if (!Strings.isNullOrEmpty(this.m_metaDataDoc.getStringValue())) {
      try (InputStream fis = FileUtil.openInputStream(this.m_metaDataDoc.getStringValue())) {
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

  /**
   * Creates a {@link BufferedDataTable} with the selected libraries.
   *
   * @param exec ExecutionContext exec
   * @return BufferedDataTable
   */
  private BufferedDataTable createLibTable(final ExecutionContext exec) {

    BufferedDataContainer container = exec.createDataContainer(FSKUtil.createLibTableSpec());
    if (m_selectedLibs.getStringArrayValue() != null) {
      for (String lib : m_selectedLibs.getStringArrayValue()) {
        // Builds full path
        String fullpath = m_libDirectory.getStringValue() + "/" + lib;
        container.addRowToTable(new LibTuple(lib, fullpath));
      }
    }
    container.close();

    return container.getTable();
  }

}
