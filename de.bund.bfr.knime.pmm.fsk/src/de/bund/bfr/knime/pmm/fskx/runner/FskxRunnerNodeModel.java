package de.bund.bfr.knime.pmm.fskx.runner;

import java.io.File;
import java.io.IOException;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;
import org.knime.ext.r.node.local.port.RPortObject;
import org.rosuda.REngine.REXPMismatchException;

import de.bund.bfr.knime.pmm.fskx.FSKNodePlugin;
import de.bund.bfr.knime.pmm.fskx.controller.IRController.RException;
import de.bund.bfr.knime.pmm.fskx.controller.RController;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;

public class FskxRunnerNodeModel extends NodeModel {

  private static final PortType[] inPortTypes = new PortType[] {FskPortObject.TYPE};
  private static final PortType[] outPortTypes =
      new PortType[] {FskPortObject.TYPE, RPortObject.TYPE};

  public FskxRunnerNodeModel() {
    super(inPortTypes, outPortTypes);
  }

  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // no settings
  }

  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    // no settings
  }

  @Override
  protected void saveSettingsTo(NodeSettingsWO settings) {
    // no settings
  }

  @Override
  protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
    // no settings
  }

  @Override
  protected void loadValidatedSettingsFrom(NodeSettingsRO settings)
      throws InvalidSettingsException {
    // no settings
  }

  @Override
  protected void reset() {
    // no settings
  }

  /** {@inheritDoc} */
  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {null, null};
  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
    exec.checkCanceled();
    FskPortObject fskObj;
    try (RController controller = new RController()) {
      fskObj = runSnippet(controller, (FskPortObject) inObjects[0], exec);
    }
    RPortObject rObj = new RPortObject(fskObj.getWorkspaceFile());

    return new PortObject[] {fskObj, rObj};
  }

  private FskPortObject runSnippet(final RController controller, final FskPortObject fskObj,
      final ExecutionContext exec)
      throws IOException, RException, CanceledExecutionException, REXPMismatchException {

    FSKNodePlugin plugin = FSKNodePlugin.getDefault();

    // Add path
    String cmd = ".libPaths(c(\"" + plugin.getInstallationPath().toString().replace("\\", "/") + "\", .libPaths()))";
    String[] newPaths = controller.eval(cmd).asStrings();

    // Run model
    controller.eval(fskObj.getParamScript() + "\n" + fskObj.getModelScript());

    // Save workspace
    File wf;
    if (fskObj.getWorkspaceFile() == null) {
      wf = FileUtil.createTempFile("workspace", ".R");
      fskObj.setWorkspaceFile(wf);
    } else {
      wf = fskObj.getWorkspaceFile();
    }
    controller.eval("save.image('" + wf.getAbsolutePath().replace("\\", "/") + "')");

    // Restore .libPaths() to the original library path which happens to be in the last position
    controller.eval(".libPaths()[" + newPaths.length + "]");

    return fskObj;
  }
}
