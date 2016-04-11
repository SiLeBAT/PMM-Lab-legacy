package de.bund.bfr.knime.pmm.fskx.runner;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec)
      throws Exception {
    exec.checkCanceled();
    FskPortObject fskObj;
    try (RController controller = new RController()) {
      fskObj = runSnippet(controller, (FskPortObject) inObjects[0], exec);
    }
    RPortObject rObj = new RPortObject(fskObj.getWorkspaceFile());
    
    return new PortObject[] { fskObj, rObj };
  }

  private FskPortObject runSnippet(final RController controller, final FskPortObject fskObj,
      final ExecutionContext exec)
      throws IOException, RException, CanceledExecutionException, REXPMismatchException {

    String dirPath = createTempDir();
    String[] newPaths = addPath(controller, dirPath);
    installLibs(dirPath, controller, fskObj.getLibraries());
    controller.eval(fskObj.getParamScript() + "\n" + fskObj.getModelScript());
    saveWorkspace(controller, exec, fskObj);

    // Restore .libPaths() to the original library path which happens to be in the last position
    controller.eval(".libPaths()[" + newPaths.length + "]");

    return fskObj;
  }


  /**
   * Create temporary directory for R libraries.
   * 
   * @throws IOException
   */
  private String createTempDir() throws IOException {
    File libDir = FileUtil.createTempDir("lib");
    String dirPath = libDir.getAbsolutePath().replace("\\", "/");

    return dirPath;
  }

  /**
   * Add temporary directory for libraries to .libPaths.
   * 
   * @throws RException
   * @throws REXPMismatchException
   */
  private String[] addPath(final RController controller, final String dirPath)
      throws REXPMismatchException, RException {
    String addPathCmd = ".libPaths(c(\"" + dirPath + "\", .libPaths()))";
    String[] newPaths = controller.eval(addPathCmd).asStrings();

    return newPaths;
  }

  /**
   * Install binary libraries into the temporary directory.
   * 
   * @throws RException
   */
  private void installLibs(final String dirPath, final RController controller,
      final Set<File> libs) throws RException {
    List<String> libPaths =
        libs.stream().map(f -> "\"" + f.getAbsolutePath().replace("\\", "/") + "\"")
            .collect(Collectors.toList());

    String pkgs = "c(" + String.join(",", libPaths) + ")";
    String installCmd =
        "install.packages(" + pkgs + ", repos=NULL, lib=\"" + dirPath + "\", type=\"binary\")";
    controller.eval(installCmd);
  }

  private void saveWorkspace(final RController controller, final ExecutionContext exec,
      final FskPortObject fskObj) throws RException, CanceledExecutionException, IOException {
    File workspaceFile = FileUtil.createTempFile("workspace", ".R");
    controller.eval("save.image(\"" + workspaceFile.getAbsolutePath().replace('\\', '/') + "\")");
    fskObj.setWorkspaceFile(workspaceFile);
  }
}
