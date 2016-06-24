/*
 ***************************************************************************************************
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
 *************************************************************************************************
 */
package de.bund.bfr.knime.pmm.fskx.creator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.knime.base.node.util.exttool.ExtToolOutputNodeModel;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;
import org.rosuda.REngine.REXPMismatchException;

import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.common.KnimeUtils;
import de.bund.bfr.knime.pmm.fskx.MissingValueError;
import de.bund.bfr.knime.pmm.fskx.RScript;
import de.bund.bfr.knime.pmm.fskx.controller.IRController.RException;
import de.bund.bfr.knime.pmm.fskx.controller.LibRegistry;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObjectSpec;
import de.bund.bfr.knime.pmm.openfsmr.FSMRTemplate;

class FskCreatorNodeModel extends ExtToolOutputNodeModel {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(FskCreatorNodeModel.class);

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

	// configuration key of the path of the XLSX spreadsheet with the model meta
	// data
	static final String CFGKEY_SPREADSHEET = "spreadsheet";

	private final static PortType[] inPortTypes = new PortType[] {};
	private final static PortType[] outPortTypes = new PortType[] { FskPortObject.TYPE };

	// Settings models
	private final SettingsModelString m_modelScript = new SettingsModelString(CFGKEY_MODEL_SCRIPT, null);
	private final SettingsModelString m_paramScript = new SettingsModelString(CFGKEY_PARAM_SCRIPT, null);
	private final SettingsModelString m_vizScript = new SettingsModelString(CFGKEY_VISUALIZATION_SCRIPT, null);
	private final SettingsModelString m_metaDataDoc = new SettingsModelString(CFGKEY_SPREADSHEET, null);
	private final SettingsModelString m_libDirectory = new SettingsModelString(CFGKEY_DIR_LIBS, null);
	private final SettingsModelStringArray m_selectedLibs = new SettingsModelStringArray(CFGKEY_LIBS, null);

	/** {@inheritDoc} */
	protected FskCreatorNodeModel() {
		super(inPortTypes, outPortTypes);
	}

	/** {@inheritDoc} */
	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
		// nothing
	}

	/** {@inheritDoc} */
	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
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
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		m_modelScript.loadSettingsFrom(settings);
		m_paramScript.loadSettingsFrom(settings);
		m_vizScript.loadSettingsFrom(settings);
		m_metaDataDoc.loadSettingsFrom(settings);
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
	protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec)
			throws InvalidSettingsException, IOException, MissingValueError {

		// Reads model script
		String model;
		try {
			RScript modelScript = readScript(m_modelScript.getStringValue());
			model = modelScript.getScript();
		} catch (IOException e) {
			model = "";
		}

		// Reads parameters script
		String param;
		try {
			RScript paramScript = readScript(m_paramScript.getStringValue());
			param = paramScript.getScript();
		} catch (IOException | InvalidSettingsException e) {
			param = "";
		}

		// Reads visualization script
		String viz;
		try {
			RScript vizScript = readScript(m_vizScript.getStringValue());
			viz = vizScript.getScript();
		} catch (IOException | InvalidSettingsException e) {
			viz = "";
		}

		// Reads model meta data
		FSMRTemplate template;
		try (InputStream fis = FileUtil.openInputStream(m_metaDataDoc.getStringValue())) {
			// Finds the workbook instance for XLSX file
			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			fis.close();

			template = FSMRUtils.processSpreadsheet(workbook);
		}

		// Reads R libraries
		Set<File> libs = new HashSet<>();
		if (m_selectedLibs.getStringArrayValue() != null && m_selectedLibs.getStringArrayValue().length > 0) {
			try {
				collectLibs().stream().map(Path::toFile).forEach(libs::add);
			} catch (RException | REXPMismatchException e) {
				LOGGER.error(e.getMessage());
			}
		}

		return new PortObject[] { new FskPortObject(model, param, viz, template, null, libs) };
	}

	/** {@inheritDoc} */
	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] { FskPortObjectSpec.INSTANCE };
	}

	/**
	 * Reads R script.
	 * 
	 * @param path
	 *            File path to R model script.
	 * @throws InvalidSettingsException
	 *             if {@link path} is null or whitespace.
	 * @throws IOException
	 *             if the file cannot be read.
	 */
	private static RScript readScript(final String path) throws InvalidSettingsException, IOException {

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
			// may throw IOException
			RScript script = new RScript(KnimeUtils.getFile(trimmedPath));
			return script;
		} catch (IOException e) {
			System.err.println(e.getMessage());
			throw new IOException(trimmedPath + ": cannot be read");
		}
	}

	private Set<Path> collectLibs() throws IOException, RException, REXPMismatchException {

		List<String> libNames = Arrays.stream(m_selectedLibs.getStringArrayValue())
				.map(libName -> libName.split("\\.")[0]).collect(Collectors.toList());

		LibRegistry libRegistry = LibRegistry.instance();
		// Out of all the libraries name only install those missing
		List<String> missingLibs = libNames.stream().filter(lib -> !libRegistry.isInstalled(lib))
				.collect(Collectors.toList());

		if (!missingLibs.isEmpty()) {
			libRegistry.installLibs(missingLibs);
		}

		return libRegistry.getPaths(libNames);
	}
}
