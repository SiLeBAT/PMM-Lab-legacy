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
package de.bund.bfr.knime.pmm.openfsmr;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jdom2.Element;
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

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.openfsmr.FSMRTemplate;
import de.bund.bfr.pmfml.ModelType;
import de.bund.bfr.pmfml.file.ExperimentalDataFile;
import de.bund.bfr.pmfml.file.ManualSecondaryModelFile;
import de.bund.bfr.pmfml.file.ManualTertiaryModelFile;
import de.bund.bfr.pmfml.file.OneStepSecondaryModelFile;
import de.bund.bfr.pmfml.file.OneStepTertiaryModelFile;
import de.bund.bfr.pmfml.file.PMFMetadataNode;
import de.bund.bfr.pmfml.file.PrimaryModelWDataFile;
import de.bund.bfr.pmfml.file.PrimaryModelWODataFile;
import de.bund.bfr.pmfml.file.TwoStepSecondaryModelFile;
import de.bund.bfr.pmfml.file.TwoStepTertiaryModelFile;
import de.bund.bfr.pmfml.model.ExperimentalData;
import de.bund.bfr.pmfml.model.ManualSecondaryModel;
import de.bund.bfr.pmfml.model.ManualTertiaryModel;
import de.bund.bfr.pmfml.model.OneStepSecondaryModel;
import de.bund.bfr.pmfml.model.OneStepTertiaryModel;
import de.bund.bfr.pmfml.model.PrimaryModelWData;
import de.bund.bfr.pmfml.model.PrimaryModelWOData;
import de.bund.bfr.pmfml.model.TwoStepSecondaryModel;
import de.bund.bfr.pmfml.model.TwoStepTertiaryModel;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

/**
 * This is the model implementation of OpenFSMRConverter.
 * 
 * Author: Miguel de Alba
 */
public class OpenFSMRConverterNodeModel extends NodeModel {

  // configuration keys
  public static final String CFGKEY_DIR = "directory";
  public static final String CFGKEY_FILES = "files";

  // defaults for persistent state
  public static final String DEFAULT_DIR = "c:/";
  public static final String[] DEFAULT_FILES = new String[0];

  // persistent state
  private SettingsModelString selectedDirectory = new SettingsModelString(CFGKEY_DIR, DEFAULT_DIR);
  private SettingsModelStringArray selectedFiles = new SettingsModelStringArray(CFGKEY_FILES,
      DEFAULT_FILES);

  private static final Map<ModelType, Converter> CONVERTERS = new HashMap<>();
  static {
    CONVERTERS.put(ModelType.EXPERIMENTAL_DATA, new ExperimentalDataConverter());
    CONVERTERS.put(ModelType.PRIMARY_MODEL_WDATA, new PrimaryModelWithDataConverter());
    CONVERTERS.put(ModelType.PRIMARY_MODEL_WODATA, new PrimaryModelWithoutDataConverter());
    CONVERTERS.put(ModelType.TWO_STEP_SECONDARY_MODEL, new TwoStepSecondaryModelConverter());
    CONVERTERS.put(ModelType.ONE_STEP_SECONDARY_MODEL, new OneStepSecondaryModelConverter());
    CONVERTERS.put(ModelType.MANUAL_SECONDARY_MODEL, new ManualSecondaryModelConverter());
    CONVERTERS.put(ModelType.TWO_STEP_TERTIARY_MODEL, new TwoStepTertiaryModelConverter());
    CONVERTERS.put(ModelType.ONE_STEP_TERTIARY_MODEL, new OneStepTertiaryModelConverter());
    CONVERTERS.put(ModelType.MANUAL_TERTIARY_MODEL, new ManualTertiaryModelConverter());
  }

  /** Constructor for the node model. */
  protected OpenFSMRConverterNodeModel() {
    // 0 input ports and 1 output port
    super(0, 1);
  }

  /** {@inheritDoc} */
  @Override
  protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
      final ExecutionContext exec) throws Exception {
    BufferedDataContainer container = exec.createDataContainer(new OpenFSMRSchema().createSpec());
    for (String selectedFile : selectedFiles.getStringArrayValue()) {
      // Builds full path
      String fullpath = selectedDirectory.getStringValue() + "/" + selectedFile;

      try {
        List<FSMRTemplate> templates = createTemplatesFromPMF(fullpath);

        for (FSMRTemplate template : templates) {
          KnimeTuple tuple = FSMRUtils.createTupleFromTemplate(template);
          container.addRowToTable(tuple);
        }
      } catch (Exception e) {
        System.err.println("Could not process file: " + selectedFile);
        e.printStackTrace();
      }
    }
    container.close();
    BufferedDataTable[] tables = {container.getTable()};
    return tables;
  }

  /** {@inheritDoc} */
  @Override
  protected void reset() {}

  /** {@inheritDoc} */
  @Override
  protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
      throws InvalidSettingsException {
    return new DataTableSpec[] {null};
  }

  /** {@inheritDoc} */
  @Override
  protected void saveSettingsTo(final NodeSettingsWO settings) {
    selectedDirectory.saveSettingsTo(settings);
    selectedFiles.saveSettingsTo(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
      throws InvalidSettingsException {
    selectedDirectory.loadSettingsFrom(settings);
    selectedFiles.loadSettingsFrom(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
    selectedDirectory.validateSettings(settings);
    selectedFiles.validateSettings(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void loadInternals(final File internDir, final ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  /** {@inheritDoc} */
  @Override
  protected void saveInternals(final File internDir, final ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {}

  private static List<FSMRTemplate> createTemplatesFromPMF(String filepath) throws Exception {

    // Get model type from annotation in the metadata file
    // a) Open archive
    CombineArchive ca = new CombineArchive(new File(filepath));

    // b) Get annotation
    MetaDataObject mdo = ca.getDescriptions().get(0);
    Element metaParent = mdo.getXmlDescription();
    PMFMetadataNode pmfMetadataNode = new PMFMetadataNode(metaParent);

    // c) Close archive
    ca.close();

    Converter converter = CONVERTERS.get(pmfMetadataNode.getModelType());

    return converter.convert(filepath);
  }
}


/**
 * OpenFSMR template converter.
 * 
 * Convert all the models within a PMFX file to OpenFSMR templates. PMF files are also supported.
 * 
 * @author Miguel Alba
 */
interface Converter {

  List<FSMRTemplate> convert(final String filepath) throws Exception;
}


class ExperimentalDataConverter implements Converter {

  /** Obtain an OpenFSMR template per data file. */
  public List<FSMRTemplate> convert(final String filepath) throws Exception {
    List<ExperimentalData> eds =
        filepath.endsWith(".pmfx") ? ExperimentalDataFile.readPMFX(filepath) : ExperimentalDataFile
            .readPMF(filepath);

    return eds.stream().map(ExperimentalData::getDoc).map(FSMRUtils::processData)
        .collect(Collectors.toList());
  }
}


class PrimaryModelWithDataConverter implements Converter {

  /** Obtain an OpenFSMR template per primary model. */
  public List<FSMRTemplate> convert(final String filepath) throws Exception {
    List<PrimaryModelWData> pms =
        filepath.endsWith(".pmfx") ? PrimaryModelWDataFile.readPMFX(filepath)
            : PrimaryModelWDataFile.readPMF(filepath);

    return pms.stream().map(PrimaryModelWData::getModelDoc)
        .map(FSMRUtils::processModelWithMicrobialData).collect(Collectors.toList());
  }
}


class PrimaryModelWithoutDataConverter implements Converter {

  /** Obtain an OpenFSMR template per primary model. */
  public List<FSMRTemplate> convert(final String filepath) throws Exception {
    List<PrimaryModelWOData> pms =
        filepath.endsWith(".pmfx") ? PrimaryModelWODataFile.readPMFX(filepath)
            : PrimaryModelWODataFile.readPMF(filepath);

    return pms.stream().map(PrimaryModelWOData::getDoc)
        .map(FSMRUtils::processModelWithMicrobialData).collect(Collectors.toList());
  }
}


class TwoStepSecondaryModelConverter implements Converter {

  /** Obtain an OpenFSMR template per secondary model. */
  public List<FSMRTemplate> convert(final String filepath) throws Exception {
    List<TwoStepSecondaryModel> sms =
        filepath.endsWith(".pmfx") ? TwoStepSecondaryModelFile.readPMFX(filepath)
            : TwoStepSecondaryModelFile.readPMF(filepath);

    return sms.stream().map(FSMRUtils::processTwoStepSecondaryModel).collect(Collectors.toList());
  }
}


class OneStepSecondaryModelConverter implements Converter {

  /** Obtain an OpenFSMR template per secondary model. */
  public List<FSMRTemplate> convert(final String filepath) throws Exception {
    List<OneStepSecondaryModel> sms =
        filepath.endsWith(".pmfx") ? OneStepSecondaryModelFile.readPMFX(filepath)
            : OneStepSecondaryModelFile.readPMF(filepath);

    return sms.stream().map(FSMRUtils::processOneStepSecondaryModel).collect(Collectors.toList());
  }
}


class ManualSecondaryModelConverter implements Converter {

  /** Obtain an OpenFSMR template per secondary model. */
  public List<FSMRTemplate> convert(final String filepath) throws Exception {
    List<ManualSecondaryModel> sms =
        filepath.endsWith(".pmfx") ? ManualSecondaryModelFile.readPMFX(filepath)
            : ManualSecondaryModelFile.readPMF(filepath);

    return sms.stream().map(FSMRUtils::processManualSecondaryModel).collect(Collectors.toList());
  }
}


class TwoStepTertiaryModelConverter implements Converter {

  /** Obtain an OpenFSMR template per tertiary model. */
  public List<FSMRTemplate> convert(String filepath) throws Exception {
    List<TwoStepTertiaryModel> tms =
        filepath.endsWith(".pmfx") ? TwoStepTertiaryModelFile.readPMFX(filepath)
            : TwoStepTertiaryModelFile.readPMF(filepath);

    return tms.stream().map(FSMRUtils::processTwoStepTertiaryModel).collect(Collectors.toList());
  }
}


class OneStepTertiaryModelConverter implements Converter {

  /** Obtain an OpenFSMR template per tertiary model. */
  public List<FSMRTemplate> convert(String filepath) throws Exception {
    List<OneStepTertiaryModel> tms =
        filepath.endsWith(".pmfx") ? OneStepTertiaryModelFile.readPMFX(filepath)
            : OneStepTertiaryModelFile.readPMF(filepath);

    return tms.stream().map(FSMRUtils::processOneStepTertiaryModel).collect(Collectors.toList());
  }
}


class ManualTertiaryModelConverter implements Converter {

  /** Obtain an OpenFSMR template per tertiary model. */
  public List<FSMRTemplate> convert(String filepath) throws Exception {
    List<ManualTertiaryModel> tms =
        filepath.endsWith(".pmfx") ? ManualTertiaryModelFile.readPMFX(filepath)
            : ManualTertiaryModelFile.readPMF(filepath);

    return tms.stream().map(FSMRUtils::processManualTertiaryModel).collect(Collectors.toList());
  }
}
