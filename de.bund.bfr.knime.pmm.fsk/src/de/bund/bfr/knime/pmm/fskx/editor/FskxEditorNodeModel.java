package de.bund.bfr.knime.pmm.fskx.editor;

import java.io.File;
import java.io.IOException;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObjectSpec;

public class FskxEditorNodeModel extends NodeModel {

  private static final NodeLogger LOGGER = NodeLogger.getLogger(FskxEditorNodeModel.class);

  private static final PortType[] inPortTypes = new PortType[] {FskPortObject.TYPE};
  private static final PortType[] outPortTypes = new PortType[] {FskPortObject.TYPE};

  private FskxEditorNodeSettings settings = new FskxEditorNodeSettings();

  public FskxEditorNodeModel() {
    super(inPortTypes, outPortTypes);
  }

  // --- internal settings methods ---
  /** {@inheritDoc} */
  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // no internal settings
  }

  /** {@inheritDoc} */
  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // no internal settings
  }

  /** {@inheritDoc} */
  @Override
  protected void reset() {}

  // --- node settings methods ---
  /** {@inheritDoc} */
  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    this.settings.loadSettings(settings);
  }

  /** {@inheritDoc} */
  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {}

  /** {@inheritDoc} */
  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    this.settings.saveSettings(settings);
  }

  // --- other methods ---
  /** {@inheritDoc} */
  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {FskPortObjectSpec.INSTANCE};
  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
    FskPortObject inObj = (FskPortObject) inObjects[0];
    
    // Creates output FSK object
    // - Takes scripts from node settings
    // - Takes meta data, R workspace and libraries from input object (not saved yet in settings)
    FskPortObject outObj = new FskPortObject(settings.modelScript, settings.paramScript,
        settings.vizScript, inObj.getTemplate(), inObj.getWorkspaceFile(), inObj.getLibraries());
    
    return new PortObject[] { outObj };
  }
}
