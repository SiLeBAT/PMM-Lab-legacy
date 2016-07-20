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
package de.bund.bfr.knime.pmm.fskx.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.jdom2.JDOMException;
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
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;
import org.knime.ext.r.node.local.port.RPortObject;
import org.knime.ext.r.node.local.port.RPortObjectSpec;
import org.rosuda.REngine.REXPMismatchException;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.xml.stax.SBMLReader;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.fskx.MissingValueError;
import de.bund.bfr.knime.pmm.fskx.RMetaDataNode;
import de.bund.bfr.knime.pmm.fskx.ZipUri;
import de.bund.bfr.knime.pmm.fskx.controller.IRController.RException;
import de.bund.bfr.knime.pmm.fskx.controller.LibRegistry;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObjectSpec;
import de.bund.bfr.knime.pmm.openfsmr.OpenFSMRSchema;
import de.bund.bfr.openfsmr.FSMRTemplate;
import de.bund.bfr.openfsmr.FSMRTemplateImpl;
import de.bund.bfr.pmfml.file.uri.UriFactory;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;

class FskxReaderNodeModel extends NodeModel {

	// configuration keys
	static final String CFGKEY_FILE = "filename";

	// defaults for persistent state
	private static final String DEFAULT_FILE = "c:/temp/foo.numl";

	// defaults for persistent state
	private final SettingsModelString filename = new SettingsModelString(CFGKEY_FILE, DEFAULT_FILE);

	private static final PortType[] inPortTypes = {};
	private static final PortType[] outPortTypes = { FskPortObject.TYPE, RPortObject.TYPE, BufferedDataTable.TYPE };

	// Specs
	private static final FskPortObjectSpec fskSpec = FskPortObjectSpec.INSTANCE;
	private static final RPortObjectSpec rSpec = RPortObjectSpec.INSTANCE;
	private static final DataTableSpec fsmrSpec = new MetaDataSchema().createSpec();

	protected FskxReaderNodeModel() {
		super(inPortTypes, outPortTypes);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws MissingValueError
	 * @throws RException
	 * @throws REXPMismatchException
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec)
			throws CombineArchiveException, FileAccessException, MissingValueError, REXPMismatchException, RException {

		String model = "";
		String param = "";
		String viz = "";
		FSMRTemplate template = new FSMRTemplateImpl();
		File workspaceFile = null;
		Set<File> libs = new HashSet<>();

		File archiveFile = new File(filename.getStringValue());
		try (CombineArchive archive = new CombineArchive(archiveFile)) {
			// Gets annotation
			RMetaDataNode node = new RMetaDataNode(archive.getDescriptions().get(0).getXmlDescription());

			// Gets model script
			if (node.getMainScript() != null) {
				ArchiveEntry entry = archive.getEntry(node.getMainScript());
				model = loadScriptFromEntry(entry);
			}

			// Gets parameters script
			if (node.getParametersScript() != null) {
				ArchiveEntry entry = archive.getEntry(node.getParametersScript());
				param = loadScriptFromEntry(entry);
			}

			// Gets visualization script
			if (node.getVisualizationScript() != null) {
				ArchiveEntry entry = archive.getEntry(node.getVisualizationScript());
				viz = loadScriptFromEntry(entry);
			}

			// Gets workspace file
			if (node.getWorkspaceFile() != null) {
				ArchiveEntry entry = archive.getEntry(node.getWorkspaceFile());
				workspaceFile = FileUtil.createTempFile("workspace", ".r");
				entry.extractFile(workspaceFile);
			}

			// Gets model meta data
			URI pmfUri = UriFactory.createPMFURI();
			if (archive.getNumEntriesWithFormat(pmfUri) == 1) {
				ArchiveEntry entry = archive.getEntriesWithFormat(pmfUri).get(0);
				File f = FileUtil.createTempFile("metaData", ".pmf");
				entry.extractFile(f);

				SBMLDocument doc = new SBMLReader().readSBML(f);
				template = FSMRUtils.processPrevalenceModel(doc);
			}

			// Gets R libraries
			URI zipUri = ZipUri.createURI();

			// Gets library names from the zip entries in the CombineArchive
			List<String> libNames = archive.getEntriesWithFormat(zipUri).stream()
					.map(entry -> entry.getFileName().split("\\_")[0]).collect(Collectors.toList());

			if (!libNames.isEmpty()) {

				LibRegistry libRegistry = LibRegistry.instance();

				// Filters missing libraries
				List<String> missingLibs = new LinkedList<>();
				for (String lib : libNames) {
					if (!libRegistry.isInstalled(lib)) {
						missingLibs.add(lib);
					}
				}

				if (!missingLibs.isEmpty()) {
					libRegistry.installLibs(missingLibs);
				}

				// Converts and return set of Paths returned from plugin to set
				libs = libRegistry.getPaths(libNames).stream().map(Path::toFile).collect(Collectors.toSet());
			}

		} catch (IOException | JDOMException | ParseException | XMLStreamException e) {
			e.printStackTrace();
		}

		// Meta data port
		BufferedDataContainer fsmrContainer = exec.createDataContainer(fsmrSpec);
		if (template != null) {
			KnimeTuple fsmrTuple = FSMRUtils.createTupleFromTemplate(template);

			MetaDataSchema metaDataSchema = new MetaDataSchema();
			KnimeTuple metaDataTuple = new KnimeTuple(metaDataSchema);
			// Copy fields from fsmrTuple
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_NAME, fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_NAME));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_ID, fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_ID));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_LINK, fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_LINK));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_ORGANISM_NAME,
					fsmrTuple.getString(OpenFSMRSchema.ATT_ORGANISM_NAME));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_ORGANISM_DETAIL,
					fsmrTuple.getString(OpenFSMRSchema.ATT_ORGANISM_DETAIL));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_ENVIRONMENT_NAME,
					fsmrTuple.getString(OpenFSMRSchema.ATT_ENVIRONMENT_NAME));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_ENVIRONMENT_DETAIL,
					fsmrTuple.getString(OpenFSMRSchema.ATT_ENVIRONMENT_DETAIL));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_CREATOR,
					fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_CREATOR));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_FAMILY_NAME,
					fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_FAMILY_NAME));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_CONTACT,
					fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_CONTACT));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION,
					fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION_LINK,
					fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_REFERENCE_DESCRIPTION_LINK));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_CREATED_DATE,
					fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_CREATED_DATE));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_MODIFIED_DATE,
					fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_MODIFIED_DATE));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_RIGHTS,
					fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_RIGHTS));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_NOTES, fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_NOTES));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_CURATION_STATUS,
					fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_CURATION_STATUS));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_TYPE, fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_TYPE));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_SUBJECT,
					fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_SUBJECT));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_FOOD_PROCESS,
					fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_FOOD_PROCESS));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE,
					fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_UNIT,
					fsmrTuple.getString(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_UNIT));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MIN,
					fsmrTuple.getDouble(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MIN));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MAX,
					fsmrTuple.getDouble(OpenFSMRSchema.ATT_MODEL_DEPENDENT_VARIABLE_MAX));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE,
					fsmrTuple.getString(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_UNITS,
					fsmrTuple.getString(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_UNITS));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_MINS,
					fsmrTuple.getString(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_MINS));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_MAXS,
					fsmrTuple.getString(OpenFSMRSchema.ATT_INDEPENDENT_VARIABLE_MAXS));
			metaDataTuple.setValue(OpenFSMRSchema.ATT_HAS_DATA, fsmrTuple.getInt(OpenFSMRSchema.ATT_HAS_DATA));

			Map<String, String> indepValues = new HashMap<>();
			for (String line : param.split("\\r?\\n")) {
				if (line.indexOf("<-") != -1) {
					String[] tokens = line.split("<-");
					String variableName = tokens[0].trim();
					String variableValue = tokens[1].trim();
					indepValues.put(variableName, variableValue);
				}
			}
			String values = Arrays.stream(template.getIndependentVariables()).map(indepValues::get)
					.collect(Collectors.joining("||"));
			metaDataTuple.setValue(MetaDataSchema.ATT_INDEPENDENT_VARIABLE_VALUES, values);

			fsmrContainer.addRowToTable(metaDataTuple);
		}
		fsmrContainer.close();

		FskPortObject fskObj = new FskPortObject(model, param, viz, template, workspaceFile, libs);
		RPortObject rObj = new RPortObject(fskObj.getWorkspaceFile());

		return new PortObject[] { fskObj, rObj, fsmrContainer.getTable() };
	}

	private String loadScriptFromEntry(final ArchiveEntry entry) throws IOException {
		// Create temporary file with a random name. The name does not matter,
		// since the file will be
		// deleted by KNIME itself.
		File f = FileUtil.createTempFile("script", ".r");
		entry.extractFile(f);

		// Read script from f and return script
		FileInputStream fis = new FileInputStream(f);
		String script = IOUtils.toString(fis, "UTF-8");
		fis.close();

		return script;
	}

	/** {@inheritDoc} */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] { fskSpec, rSpec, fsmrSpec };
	}

	/** {@inheritDoc} */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		filename.saveSettingsTo(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		filename.loadSettingsFrom(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		filename.validateSettings(settings);
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
	protected void reset() {
		// does nothing
	}

	private class FileAccessException extends Exception {

		private static final long serialVersionUID = 1L;
	}

	/*
	 * Temporal metadata schema that extends the OpenFSMRSchema with the values
	 * of the independent variables (which are defined in the parameters
	 * script).
	 * 
	 * It would be eventually replaced with the metadata schema defined in the
	 * guidance document.
	 */
	private static class MetaDataSchema extends OpenFSMRSchema {

		public static final String ATT_INDEPENDENT_VARIABLE_VALUES = "Model-IndependentVariableValues";

		public MetaDataSchema() {
			super();
			addStringAttribute(ATT_INDEPENDENT_VARIABLE_VALUES);
		}
	}
}
