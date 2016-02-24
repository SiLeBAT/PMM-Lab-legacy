package de.bund.bfr.knime.pmm.fskx.r2fsk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.fskx.FSKUtil;
import de.bund.bfr.knime.pmm.fskx.FSKXTuple;
import de.bund.bfr.knime.pmm.fskx.RScript;
import de.bund.bfr.knime.pmm.fskx.FSKXTuple.KEYS;
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

  /** {@inheritDoc} */
  protected R2FSKNodeModel() {
    super(0, 2);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  /** {@inheritDoc} */
  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  /** {@inheritDoc} */
  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    modelScriptPath.saveSettingsTo(settings);
    paramScriptPath.saveSettingsTo(settings);
    visualizationScriptPath.saveSettingsTo(settings);
    spreadsheetPath.saveSettingsTo(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    modelScriptPath.validateSettings(settings);
    paramScriptPath.validateSettings(settings);
    visualizationScriptPath.validateSettings(settings);
    spreadsheetPath.validateSettings(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    modelScriptPath.loadSettingsFrom(settings);
    paramScriptPath.loadSettingsFrom(settings);
    visualizationScriptPath.loadSettingsFrom(settings);
    spreadsheetPath.loadSettingsFrom(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void reset() {}

  /**
   * {@inheritDoc}
   * 
   * @throws InvalidSettingsException if the model script is not specified
   */
  @Override
  protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
      final ExecutionContext exec) throws InvalidSettingsException {

    final Set<String> librariesSet = new HashSet<>(); // Set of libraries
    final Set<String> sourcesSet = new HashSet<>(); // Set of sources

    /**
     * Reads model script. Since the model script is mandatory, if modelScriptPath is not set, then
     * a InvalidSettingsException will be thrown
     */
    final EnumMap<FSKXTuple.KEYS, String> valuesMap = new EnumMap<>(KEYS.class);

    // trim white spaces and convert empty string to null
    if (modelScriptPath.getStringValue() != null) {
      modelScriptPath.setStringValue(Strings.emptyToNull(modelScriptPath.getStringValue().trim()));
    }

    if (modelScriptPath.getStringValue() == null) {
      throw new InvalidSettingsException("Unespecified model script");
    }
    try {
      RScript modelScript = new RScript(modelScriptPath.getStringValue()); // throws IOException

      valuesMap.put(KEYS.ORIG_MODEL, modelScript.getOriginalScript());
      valuesMap.put(KEYS.SIMP_MODEL, modelScript.getSimplifiedScript());

      librariesSet.addAll(modelScript.getLibraries());
      sourcesSet.addAll(modelScript.getSources());
    } catch (IOException e) {
      throw new InvalidSettingsException(modelScriptPath.getStringValue() + ": cannot be read");
    }

    /**
     * Reads parameters script. The parameters script is optional, thus if paraScriptPath is not set
     * paramScriptLines will be assigned an empty list
     */
    // trim white spaces and convert empty string to null
    if (paramScriptPath.getStringValue() != null) {
      paramScriptPath.setStringValue(Strings.emptyToNull(paramScriptPath.getStringValue().trim()));
    }

    if (paramScriptPath.getStringValue() == null) {
      valuesMap.put(KEYS.ORIG_PARAM, "");
      valuesMap.put(KEYS.SIMP_PARAM, "");
    } else {
      try {
        RScript paramScript = new RScript(paramScriptPath.getStringValue()); // IOException

        valuesMap.put(KEYS.ORIG_PARAM, paramScript.getOriginalScript());
        valuesMap.put(KEYS.SIMP_PARAM, paramScript.getSimplifiedScript());

        librariesSet.addAll(paramScript.getLibraries());
        sourcesSet.addAll(paramScript.getSources());
      } catch (IOException e) {
        throw new InvalidSettingsException(paramScriptPath.getStringValue() + ": cannot be read");
      }
    }

    /**
     * Reads visualization script. The visualization script is optional, thus if
     * visualizationScriptPath is not set visualizationScriptLines will be assigned an empty list
     */
    // trim white spaces and convert empty string to null
    if (visualizationScriptPath.getStringValue() != null) {
      visualizationScriptPath
          .setStringValue(Strings.emptyToNull(visualizationScriptPath.getStringValue().trim()));
    }

    if (visualizationScriptPath.getStringValue() == null) {
      valuesMap.put(KEYS.ORIG_VIZ, "");
      valuesMap.put(KEYS.SIMP_VIZ, "");
    } else {
      try {
        RScript vizScript = new RScript(visualizationScriptPath.getStringValue()); // IOException

        valuesMap.put(KEYS.ORIG_VIZ, vizScript.getOriginalScript());
        valuesMap.put(KEYS.SIMP_VIZ, vizScript.getSimplifiedScript());

        librariesSet.addAll(vizScript.getLibraries());
        sourcesSet.addAll(vizScript.getSources());
      } catch (IOException e) {
        throw new InvalidSettingsException(
            visualizationScriptPath.getStringValue() + ": cannot be read");
      }
    }
    valuesMap.put(KEYS.LIBS, String.join(";", librariesSet)); // adds R libraries
    valuesMap.put(KEYS.SOURCES, String.join(";", sourcesSet)); // adds R sources

    // Creates table spec and container
    final DataTableSpec tableSpec = FSKUtil.createFSKTableSpec();
    final BufferedDataContainer container = exec.createDataContainer(tableSpec);

    // Adds row and closes the container
    container.addRowToTable(new FSKXTuple(valuesMap));
    container.close();

    BufferedDataTable metaDataTable = createMetaDataTable(exec, spreadsheetPath.getStringValue());

    return new BufferedDataTable[] {container.getTable(), metaDataTable};
  }

  /** {@inheritDoc} */
  @Override
  protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
      throws InvalidSettingsException {
    return new DataTableSpec[] {null, null};
  }

  /**
   * Creates a {@link BufferedDataTable} with the meta data obtained from the given spreadsheet. If
   * an error occurs or the path is not specified the table will be empty.
   * 
   * @param exec Execution context
   * @param path File path to the XLSX spreadsheet
   * @return BufferedDataTable
   */
  private BufferedDataTable createMetaDataTable(final ExecutionContext exec, String path) {

    DataTableSpec spec = new OpenFSMRSchema().createSpec();
    BufferedDataContainer container = exec.createDataContainer(spec);

    if (!Strings.isNullOrEmpty(path)) {
      try {
        FileInputStream fis = new FileInputStream(path);
        // Finds the workbook instance for XLSX file
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        fis.close();

        FSMRTemplate template = FSMRUtils.processSpreadsheet(workbook);
        KnimeTuple tuple = FSMRUtils.createTupleFromTemplate(template);
        container.addRowToTable(tuple);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    container.close();

    return container.getTable();
  }
}
