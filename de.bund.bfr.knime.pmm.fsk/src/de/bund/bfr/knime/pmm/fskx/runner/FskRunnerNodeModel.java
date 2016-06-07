package de.bund.bfr.knime.pmm.fskx.runner;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.knime.core.data.image.png.PNGImageContent;
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
import org.knime.core.node.port.image.ImagePortObject;
import org.knime.core.node.port.image.ImagePortObjectSpec;
import org.knime.core.util.FileUtil;
import org.knime.ext.r.node.local.port.RPortObject;
import org.knime.ext.r.node.local.port.RPortObjectSpec;
import org.rosuda.REngine.REXPMismatchException;

import de.bund.bfr.knime.pmm.fskx.FSKNodePlugin;
import de.bund.bfr.knime.pmm.fskx.controller.IRController.RException;
import de.bund.bfr.knime.pmm.fskx.controller.RController;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObjectSpec;

class FskRunnerNodeModel extends NodeModel {

  private static final NodeLogger LOGGER = NodeLogger.getLogger("Fskx Runner Node Model");

  /** Output spec for an FSK object. */
  private static final FskPortObjectSpec FSK_SPEC = FskPortObjectSpec.INSTANCE;

  /** Output spec for an R object. */
  private static final RPortObjectSpec R_SPEC = RPortObjectSpec.INSTANCE;

  /** Output spec for a PNG image. */
  private static final ImagePortObjectSpec PNG_SPEC = new ImagePortObjectSpec(PNGImageContent.TYPE);

  private static final PortType[] inPortTypes = new PortType[] {FskPortObject.TYPE};
  private static final PortType[] outPortTypes =
      new PortType[] {FskPortObject.TYPE, RPortObject.TYPE, ImagePortObject.TYPE_OPTIONAL};

  private final InternalSettings internalSettings = new InternalSettings();

  public FskRunnerNodeModel() {
    super(inPortTypes, outPortTypes);
  }

  // --- internal settings methods ---

  /** {@inheritDoc} */
  @Override
  protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    internalSettings.loadInternals(nodeInternDir, exec);
  }

  /** {@inheritDoc} */
  @Override
  protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
      throws IOException, CanceledExecutionException {
    internalSettings.saveInternals(nodeInternDir, exec);
  }

  /** {@inheritDoc} */
  @Override
  protected void reset() {
    internalSettings.reset();
  }

  // --- node settings methods ---

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

  /** {@inheritDoc} */
  @Override
  protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
    return new PortObjectSpec[] {FSK_SPEC, R_SPEC, PNG_SPEC};
  }

  @Override
  protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
    exec.checkCanceled();
    FskPortObject fskObj;
    try (RController controller = new RController()) {
      fskObj = runSnippet(controller, (FskPortObject) inObjects[0], exec);
    }
    RPortObject rObj = new RPortObject(fskObj.getWorkspaceFile());

    
    try (FileInputStream fis = new FileInputStream(internalSettings.imageFile)) {
      final PNGImageContent content = new PNGImageContent(fis);
      internalSettings.plot = content.getImage();
      ImagePortObject imgObj = new ImagePortObject(content, PNG_SPEC);
      return new PortObject[] { fskObj, rObj, imgObj };
    } catch (IOException e) {
      LOGGER.warn("There is no image created");
      return new PortObject[] { fskObj, rObj };
    }
  }

  private FskPortObject runSnippet(final RController controller, final FskPortObject fskObj,
      final ExecutionContext exec)
      throws IOException, RException, CanceledExecutionException, REXPMismatchException {

    FSKNodePlugin plugin = FSKNodePlugin.getDefault();

    // Add path
    String cmd = ".libPaths(c(\"" + plugin.getInstallationPath().toString().replace("\\", "/")
        + "\", .libPaths()))";
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

    // Creates chart into m_imageFile
    try {
      controller.eval("png(\"" + internalSettings.imageFile.getAbsolutePath().replace("\\", "/")
          + "\", width=640, height=640, pointsize=12, bg=\"#ffffff\", res=\"NA\")");
      controller.eval(fskObj.getVizScript() + "\n");
      controller.eval("dev.off()");
    } catch (RException e) {
      LOGGER.warn("Visualization script failed");
    }

    // Restore .libPaths() to the original library path which happens to be in the last position
    controller.eval(".libPaths()[" + newPaths.length + "]");

    return fskObj;
  }

  Image getResultImage() {
    return internalSettings.plot;
  }

  private class InternalSettings {

    private static final String FILE_NAME = "Rplot";

    /**
     * Non-null image file to use for this current node. Initialized to temp location.
     */
    private File imageFile = null;

    private Image plot = null;

    InternalSettings() {
      try {
        imageFile = FileUtil.createTempFile("FskxRunner-", ".png");
      } catch (IOException e) {
        LOGGER.error("Cannot create temporary file.", e);
        throw new RuntimeException(e);
      }
      imageFile.deleteOnExit();
    }

    /** Loads the saved image. */
    void loadInternals(File nodeInternDir, ExecutionMonitor exec)
        throws IOException, CanceledExecutionException {
      final File file = new File(nodeInternDir, FILE_NAME + ".png");

      if (file.exists() && file.canRead()) {
        FileUtil.copy(file, imageFile);
        try (InputStream is = new FileInputStream(imageFile)) {
          plot = new PNGImageContent(is).getImage();
        }
      }
    }

    /** Saves the saved image. */
    protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
        throws IOException, CanceledExecutionException {
      if (plot != null) {
        final File file = new File(nodeInternDir, FILE_NAME + ".png");
        FileUtil.copy(imageFile, file);
      }
    }

    /** Clear the contents of the image file. */
    protected void reset() {
      plot = null;

      if (imageFile != null) {
        try (OutputStream erasor = new FileOutputStream(imageFile)) {
          erasor.write((new String()).getBytes());
        } catch (final FileNotFoundException e) {
          LOGGER.error("Temporary file is removed.", e);
        } catch (final IOException e) {
          LOGGER.error("Cannot write temporary file.", e);
        }
      }
    }
  }
}
