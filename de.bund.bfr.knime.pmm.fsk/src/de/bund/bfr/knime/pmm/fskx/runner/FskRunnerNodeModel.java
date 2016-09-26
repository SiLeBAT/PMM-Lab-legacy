package de.bund.bfr.knime.pmm.fskx.runner;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import org.knime.core.data.DataRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.image.png.PNGImageContent;
import org.knime.core.node.BufferedDataTable;
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

import de.bund.bfr.knime.pmm.fskx.FskMetaData;
import de.bund.bfr.knime.pmm.fskx.FskMetaDataTuple;
import de.bund.bfr.knime.pmm.fskx.controller.IRController.RException;
import de.bund.bfr.knime.pmm.fskx.controller.LibRegistry;
import de.bund.bfr.knime.pmm.fskx.controller.RController;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObjectSpec;
import de.bund.bfr.pmfml.ModelType;

class FskRunnerNodeModel extends NodeModel {

	private static final NodeLogger LOGGER = NodeLogger.getLogger("Fskx Runner Node Model");

	/** Output spec for an FSK object. */
	private static final FskPortObjectSpec FSK_SPEC = FskPortObjectSpec.INSTANCE;

	/** Output spec for an R object. */
	private static final RPortObjectSpec R_SPEC = RPortObjectSpec.INSTANCE;

	/** Output spec for a PNG image. */
	private static final ImagePortObjectSpec PNG_SPEC = new ImagePortObjectSpec(PNGImageContent.TYPE);

	private static final PortType[] inPortTypes = new PortType[] { FskPortObject.TYPE,
			BufferedDataTable.TYPE_OPTIONAL };
	private static final PortType[] outPortTypes = new PortType[] { FskPortObject.TYPE, RPortObject.TYPE,
			ImagePortObject.TYPE_OPTIONAL };

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
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		// no settings
	}

	/** {@inheritDoc} */
	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] { FSK_SPEC, R_SPEC, PNG_SPEC };
	}

	@Override
	protected PortObject[] execute(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		exec.checkCanceled();
		FskPortObject fskObj = (FskPortObject) inObjects[0];

		// If a metadata table is connected then update the model metadata
		if (inObjects.length == 2 && inObjects[1] != null) {
			BufferedDataTable metadataTable = (BufferedDataTable) inObjects[1];
			if (metadataTable.size() == 1) {
				Iterator<DataRow> iterator = metadataTable.iterator();
				DataRow dataRow = iterator.next();
				fskObj.template = tuple2Template(dataRow);

				// Replace with the default values with the new metadata
				if (fskObj.template.independentVariables != null && fskObj.template.independentVariables.length > 0 &&
						fskObj.template.independentVariableValues != null && fskObj.template.independentVariableValues.length > 0) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < fskObj.template.independentVariables.length; i++) {
						String var = fskObj.template.independentVariables[i];
						String value = fskObj.template.independentVariableUnits[i];
						sb.append(var + " <- " + value + "\n");
					}
					fskObj.param = sb.toString();
				}
			}
		}

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

		// Add path
		LibRegistry libRegistry = LibRegistry.instance();
		String cmd = ".libPaths(c(\"" + libRegistry.getInstallationPath().toString().replace("\\", "/")
				+ "\", .libPaths()))";
		String[] newPaths = controller.eval(cmd).asStrings();

		// Run model
		controller.eval(fskObj.param + "\n" + fskObj.model);

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
			controller.eval(fskObj.viz + "\n");
			controller.eval("dev.off()");
		} catch (RException e) {
			LOGGER.warn("Visualization script failed");
		}

		// Restore .libPaths() to the original library path which happens to be
		// in the last position
		controller.eval(".libPaths()[" + newPaths.length + "]");

		return fskObj;
	}

	Image getResultImage() {
		return internalSettings.plot;
	}

	private class InternalSettings {

		private static final String FILE_NAME = "Rplot";

		/**
		 * Non-null image file to use for this current node. Initialized to temp
		 * location.
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
		void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException, CanceledExecutionException {
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

	private FskMetaData tuple2Template(final DataRow row) {

		FskMetaData template = new FskMetaData();
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy", Locale.ENGLISH);

		template.modelName = ((StringCell) row.getCell(FskMetaDataTuple.Key.name.ordinal())).getStringValue();
		template.modelId = ((StringCell) row.getCell(FskMetaDataTuple.Key.id.ordinal())).getStringValue();

		// model link
		{
			StringCell cell = (StringCell) row.getCell(FskMetaDataTuple.Key.model_link.ordinal());
			try {
				template.modelLink = new URL(cell.getStringValue());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		template.organism = ((StringCell) row.getCell(FskMetaDataTuple.Key.species.ordinal())).getStringValue();
		template.organismDetails = ((StringCell) row.getCell(FskMetaDataTuple.Key.species_details.ordinal()))
				.getStringValue();
		template.matrix = ((StringCell) row.getCell(FskMetaDataTuple.Key.matrix.ordinal())).getStringValue();
		template.matrixDetails = ((StringCell) row.getCell(FskMetaDataTuple.Key.matrix_details.ordinal()))
				.getStringValue();
		template.creator = ((StringCell) row.getCell(FskMetaDataTuple.Key.creator.ordinal())).getStringValue();
		template.familyName = ((StringCell) row.getCell(FskMetaDataTuple.Key.family_name.ordinal())).getStringValue();
		template.contact = ((StringCell) row.getCell(FskMetaDataTuple.Key.contact.ordinal())).getStringValue();
		template.familyName = ((StringCell) row.getCell(FskMetaDataTuple.Key.family_name.ordinal())).getStringValue();
		template.referenceDescription = ((StringCell) row.getCell(FskMetaDataTuple.Key.reference_description.ordinal()))
				.getStringValue();

		// created date
		{
			StringCell cell = (StringCell) row.getCell(FskMetaDataTuple.Key.created_date.ordinal());
			try {
				template.createdDate = dateFormat.parse(cell.getStringValue());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// modified date
		{
			StringCell cell = (StringCell) row.getCell(FskMetaDataTuple.Key.modified_date.ordinal());
			try {
				template.modifiedDate = dateFormat.parse(cell.getStringValue());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		template.rights = ((StringCell) row.getCell(FskMetaDataTuple.Key.rights.ordinal())).getStringValue();
		template.notes = ((StringCell) row.getCell(FskMetaDataTuple.Key.notes.ordinal())).getStringValue();

		// curated
		{
			StringCell cell = (StringCell) row.getCell(FskMetaDataTuple.Key.curation_status.ordinal());
			if (!cell.getStringValue().isEmpty()) {
				template.curated = Boolean.parseBoolean(cell.getStringValue());
			}
		}

		// model type
		{
			StringCell cell = (StringCell) row.getCell(FskMetaDataTuple.Key.model_type.ordinal());
			if (!cell.getStringValue().isEmpty()) {
				template.type = ModelType.valueOf(cell.getStringValue());
			}
		}

		template.foodProcess = ((StringCell) row.getCell(FskMetaDataTuple.Key.food_process.ordinal())).getStringValue();
		template.dependentVariable = ((StringCell) row.getCell(FskMetaDataTuple.Key.depvar.ordinal())).getStringValue();
		template.dependentVariableUnit = ((StringCell) row.getCell(FskMetaDataTuple.Key.depvar_min.ordinal()))
				.getStringValue();

		// dependent variable min
		{
			StringCell cell = (StringCell) row.getCell(FskMetaDataTuple.Key.depvar_min.ordinal());
			if (!cell.getStringValue().isEmpty()) {
				template.dependentVariableMin = Double.parseDouble(cell.getStringValue());
			}
		}

		// dependent variable max
		{
			StringCell cell = (StringCell) row.getCell(FskMetaDataTuple.Key.depvar_max.ordinal());
			if (!cell.getStringValue().isEmpty()) {
				template.dependentVariableMax = Double.parseDouble(cell.getStringValue());
			}
		}

		// independent variables
		{
			StringCell cell = (StringCell) row.getCell(FskMetaDataTuple.Key.indepvars.ordinal());
			if (!cell.getStringValue().isEmpty()) {
				template.independentVariables = cell.getStringValue().split("\\|\\|");
			}
		}

		// independent variable units
		{
			StringCell cell = (StringCell) row.getCell(FskMetaDataTuple.Key.indepvars_units.ordinal());
			if (!cell.getStringValue().isEmpty()) {
				template.independentVariableUnits = cell.getStringValue().split("\\|\\|");
			}
		}

		// independent variable minimum values
		{
			StringCell cell = (StringCell) row.getCell(FskMetaDataTuple.Key.indepvars_mins.ordinal());
			if (!cell.getStringValue().isEmpty()) {
				String[] tokens = cell.getStringValue().split("\\|\\|");
				template.independentVariableMins = Arrays.stream(tokens).mapToDouble(Double::parseDouble).toArray();
			}
		}

		// independent variable maximum values
		{
			StringCell cell = (StringCell) row.getCell(FskMetaDataTuple.Key.indepvars_maxs.ordinal());
			if (!cell.getStringValue().isEmpty()) {
				String[] tokens = cell.getStringValue().split("\\|\\|");
				template.independentVariableMaxs = Arrays.stream(tokens).mapToDouble(Double::parseDouble).toArray();
			}
		}

		// independent variable values
		{
			StringCell cell = (StringCell) row.getCell(FskMetaDataTuple.Key.indepvars_values.ordinal());
			if (!cell.getStringValue().isEmpty()) {
				String[] tokens = cell.getStringValue().split("\\|\\|");
				template.independentVariableValues = Arrays.stream(tokens).mapToDouble(Double::parseDouble).toArray();
			}
		}

		// has data
		{
			StringCell cell = (StringCell) row.getCell(FskMetaDataTuple.Key.has_data.ordinal());
			template.hasData = Boolean.parseBoolean(cell.getStringValue());
		}

		return template;
	}
}
