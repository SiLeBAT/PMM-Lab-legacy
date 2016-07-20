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
import java.net.URL;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.jdom2.JDOMException;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
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
import de.bund.bfr.knime.pmm.fskx.MissingValueError;
import de.bund.bfr.knime.pmm.fskx.RMetaDataNode;
import de.bund.bfr.knime.pmm.fskx.ZipUri;
import de.bund.bfr.knime.pmm.fskx.controller.IRController.RException;
import de.bund.bfr.knime.pmm.fskx.controller.LibRegistry;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObjectSpec;
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
	private static final DataTableSpec fsmrSpec = createMetaDataTableSpec();

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
		BufferedDataContainer fsmrContainer = exec.createDataContainer(createMetaDataTableSpec());
		if (template != null) {
			MetaDataTuple metadataTuple = new MetaDataTuple();
			if (template.isSetModelName())
				metadataTuple.setModelName(template.getModelName());
			if (template.isSetModelId()) 
				metadataTuple.setModelId(template.getModelId());
			if (template.isSetModelLink()) 
				metadataTuple.setModelLink(template.getModelLink());
			if (template.isSetOrganismName()) 
				metadataTuple.setOrganismName(template.getOrganismName());
			if (template.isSetOrganismDetails()) 
				metadataTuple.setOrganismDetails(template.getOrganismDetails());
			if (template.isSetMatrixName()) 
				metadataTuple.setMatrixName(template.getMatrixName());
			if (template.isSetMatrixDetails()) 
				metadataTuple.setMatrixDetails(template.getMatrixDetails());
			if (template.isSetCreator()) 
				metadataTuple.setCreator(template.getCreator());
			if (template.isSetFamilyName()) 
				metadataTuple.setFamilyName(template.getFamilyName());
			if (template.isSetContact()) 
				metadataTuple.setContact(template.getContact());
			if (template.isSetReferenceDescription()) 
				metadataTuple.setReferenceDescription(template.getReferenceDescription());
			if (template.isSetReferenceDescriptionLink()) 
				metadataTuple.setReferenceDescriptionLink(template.getReferenceDescriptionLink());
			if (template.isSetCreatedDate()) 
				metadataTuple.setCreatedDate(template.getCreatedDate().toString());
			if (template.isSetModifiedDate()) 
				metadataTuple.setModifiedDate(template.getModifiedDate().toString());
			if (template.isSetRights()) 
				metadataTuple.setRights(template.getRights());
			if (template.isSetNotes()) 
				metadataTuple.setNotes(template.getNotes());
			if (template.isSetCurationStatus()) 
				metadataTuple.setCurationStatus(template.getCurationStatus());
			if (template.isSetModelType()) 
				metadataTuple.setModelType(template.getModelType().toString());
			if (template.isSetModelSubject()) 
				metadataTuple.setModelSubject(template.getModelSubject().toString());
			if (template.isSetFoodProcess()) 
				metadataTuple.setFoodProcess(template.getFoodProcess());
			if (template.isSetDependentVariable()) 
				metadataTuple.setDependentVariable(template.getDependentVariable());
			if (template.isSetDependentVariableUnit()) 
				metadataTuple.setDependentVariableUnit(template.getDependentVariableUnit());
			if (template.isSetDependentVariableMin()) 
				metadataTuple.setDependentVariableMin(template.getDependentVariableMin());
			if (template.isSetDependentVariableMax()) 
				metadataTuple.setDependentVariableMax(template.getDependentVariableMax());
			if (template.isSetIndependentVariables()) 
				metadataTuple.setIndependentVariables(template.getIndependentVariables());
			if (template.isSetIndependentVariablesUnits())
				metadataTuple.setIndependentVariablesUnits(template.getIndependentVariablesUnits());
			if (template.isSetIndependentVariablesMins()) 
				metadataTuple.setIndependentVariablesMins(template.getIndependentVariablesMins());
			if (template.isSetIndependentVariablesMaxs()) 
				metadataTuple.setIndependentVariablesMaxs(template.getIndependentVariablesMaxs());
			if (template.isSetHasData())
				metadataTuple.setHasData(template.getHasData());
			
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
			metadataTuple.setIndependentVariablesValues(values);

			fsmrContainer.addRowToTable(metadataTuple);
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

	private enum MetaDataKeys {
		name,
		id,
		model_link,
		species,
		species_details,
		matrix,
		matrix_details,
		creator,
		family_name,
		contact,
		reference_description,
		reference_description_link,
		created_date,
		modified_date,
		rights,
		notes,
		curation_status,
		model_type,
		subject,
		food_process,
		depvar,
		depvar_unit,
		depvar_min,
		depvar_max,
		indepvars,
		indepvars_units,
		indepvars_mins,
		indepvars_maxs,
		indepvars_values,
		has_data
	}

	private static DataTableSpec createMetaDataTableSpec() {
		int numKeys = MetaDataKeys.values().length;

		String[] names = new String[numKeys];
		DataType[] types = new DataType[numKeys];

		names[MetaDataKeys.name.ordinal()] = "Model name";
		types[MetaDataKeys.name.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.id.ordinal()] = "Model id";
		types[MetaDataKeys.id.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.model_link.ordinal()] = "Model link";
		types[MetaDataKeys.model_link.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.species.ordinal()] = "Organism";
		types[MetaDataKeys.species.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.species_details.ordinal()] = "Organism details";
		types[MetaDataKeys.species_details.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.matrix.ordinal()] = "Environment";
		types[MetaDataKeys.matrix.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.matrix_details.ordinal()] = "Environment details";
		types[MetaDataKeys.matrix_details.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.creator.ordinal()] = "Model creator";
		types[MetaDataKeys.creator.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.family_name.ordinal()] = "Model family name";
		types[MetaDataKeys.family_name.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.contact.ordinal()] = "Model contact";
		types[MetaDataKeys.contact.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.reference_description.ordinal()] = "Model reference description";
		types[MetaDataKeys.reference_description.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.reference_description_link.ordinal()] = "Model reference description link";
		types[MetaDataKeys.reference_description_link.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.created_date.ordinal()] = "Created date";
		types[MetaDataKeys.created_date.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.modified_date.ordinal()] = "Modified date";
		types[MetaDataKeys.modified_date.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.rights.ordinal()] = "Rights";
		types[MetaDataKeys.rights.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.notes.ordinal()] = "Notes";
		types[MetaDataKeys.notes.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.curation_status.ordinal()] = "Curation status";
		types[MetaDataKeys.curation_status.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.model_type.ordinal()] = "Model type";
		types[MetaDataKeys.model_type.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.subject.ordinal()] = "Subject";
		types[MetaDataKeys.subject.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.food_process.ordinal()] = "Food process";
		types[MetaDataKeys.food_process.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.depvar.ordinal()] = "Dependent variable";
		types[MetaDataKeys.depvar.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.depvar_unit.ordinal()] = "Dependent variable unit";
		types[MetaDataKeys.depvar_unit.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.depvar_min.ordinal()] = "Dependent variable min";
		types[MetaDataKeys.depvar_min.ordinal()] = DoubleCell.TYPE;

		names[MetaDataKeys.depvar_max.ordinal()] = "Dependent variable max";
		types[MetaDataKeys.depvar_max.ordinal()] = DoubleCell.TYPE;

		names[MetaDataKeys.indepvars.ordinal()] = "Independent variables";
		types[MetaDataKeys.indepvars.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.indepvars_units.ordinal()] = "Independent variable units";
		types[MetaDataKeys.indepvars_units.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.indepvars_mins.ordinal()] = "Independent variable mins";
		types[MetaDataKeys.indepvars_mins.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.indepvars_maxs.ordinal()] = "Independent variable maxs";
		types[MetaDataKeys.indepvars_maxs.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.indepvars_values.ordinal()] = "Independent variable values";
		types[MetaDataKeys.indepvars_values.ordinal()] = StringCell.TYPE;

		names[MetaDataKeys.has_data.ordinal()] = "Has data?";
		types[MetaDataKeys.has_data.ordinal()] = BooleanCell.TYPE;

		return new DataTableSpec(DataTableSpec.createColumnSpecs(names, types));
	}

	private static class MetaDataTuple implements DataRow {

		private DataCell[] cell;
		private final RowKey rowKey;

		public MetaDataTuple() {
			cell = new DataCell[MetaDataKeys.values().length];

			cell[MetaDataKeys.name.ordinal()] = new StringCell("");
			cell[MetaDataKeys.id.ordinal()] = new StringCell("");
			cell[MetaDataKeys.model_link.ordinal()] = new StringCell("");
			cell[MetaDataKeys.species.ordinal()] = new StringCell("");
			cell[MetaDataKeys.species_details.ordinal()] = new StringCell("");
			cell[MetaDataKeys.matrix.ordinal()] = new StringCell("");
			cell[MetaDataKeys.matrix_details.ordinal()] = new StringCell("");
			cell[MetaDataKeys.creator.ordinal()] = new StringCell("");
			cell[MetaDataKeys.family_name.ordinal()] = new StringCell("");
			cell[MetaDataKeys.contact.ordinal()] = new StringCell("");
			cell[MetaDataKeys.reference_description.ordinal()] = new StringCell("");
			cell[MetaDataKeys.reference_description_link.ordinal()] = new StringCell("");
			cell[MetaDataKeys.created_date.ordinal()] = new StringCell("");
			cell[MetaDataKeys.modified_date.ordinal()] = new StringCell("");
			cell[MetaDataKeys.rights.ordinal()] = new StringCell("");
			cell[MetaDataKeys.notes.ordinal()] = new StringCell("");
			cell[MetaDataKeys.curation_status.ordinal()] = new StringCell("");
			cell[MetaDataKeys.model_type.ordinal()] = new StringCell("");
			cell[MetaDataKeys.subject.ordinal()] = new StringCell("");
			cell[MetaDataKeys.food_process.ordinal()] = new StringCell("");
			cell[MetaDataKeys.depvar.ordinal()] = new StringCell("");
			cell[MetaDataKeys.depvar_unit.ordinal()] = new StringCell("");
			cell[MetaDataKeys.depvar_min.ordinal()] = new DoubleCell(Double.NaN);
			cell[MetaDataKeys.depvar_max.ordinal()] = new DoubleCell(Double.NaN);
			cell[MetaDataKeys.indepvars.ordinal()] = new StringCell("");
			cell[MetaDataKeys.indepvars_units.ordinal()] = new StringCell("");
			cell[MetaDataKeys.indepvars_mins.ordinal()] = new StringCell("");
			cell[MetaDataKeys.indepvars_maxs.ordinal()] = new StringCell("");
			cell[MetaDataKeys.indepvars_values.ordinal()] = new StringCell("");
			cell[MetaDataKeys.has_data.ordinal()] = BooleanCell.FALSE;

			rowKey = new RowKey(String.valueOf(new Random().nextInt()));
		}

		// --- DataRow methods ---

		@Override
		public int getNumCells() {
			return cell.length;
		}

		@Override
		public RowKey getKey() {
			return rowKey;
		}

		@Override
		public DataCell getCell(final int index) {
			return cell[index];
		}

		@Override
		public Iterator<DataCell> iterator() {
			return new MetaDataTupleIterator(cell);
		}

		class MetaDataTupleIterator implements Iterator<DataCell> {

			private int i;
			private DataCell[] cell;

			public MetaDataTupleIterator(final DataCell[] cell) {
				i = 0;
				this.cell = cell;
			}

			@Override
			public boolean hasNext() {
				return i < cell.length;
			}

			@Override
			public DataCell next() {
				return cell[i++];
			}
		}

		// --- utility ---
		void setModelName(final String modelName) {
			cell[MetaDataKeys.name.ordinal()] = new StringCell(modelName);
		}

		void setModelId(final String modelId) {
			cell[MetaDataKeys.id.ordinal()] = new StringCell(modelId);
		}

		void setModelLink(final URL link) {
			cell[MetaDataKeys.model_link.ordinal()] = new StringCell(link.toString());
		}

		void setOrganismName(final String organismName) {
			cell[MetaDataKeys.species.ordinal()] = new StringCell(organismName);
		}

		void setOrganismDetails(final String organismDetails) {
			cell[MetaDataKeys.species_details.ordinal()] = new StringCell(organismDetails);
		}

		void setMatrixName(final String matrixName) {
			cell[MetaDataKeys.matrix.ordinal()] = new StringCell(matrixName);
		}

		void setMatrixDetails(final String matrixDetails) {
			cell[MetaDataKeys.matrix_details.ordinal()] = new StringCell(matrixDetails);
		}

		void setCreator(final String creator) {
			cell[MetaDataKeys.creator.ordinal()] = new StringCell(creator);
		}

		void setFamilyName(final String familyName) {
			cell[MetaDataKeys.family_name.ordinal()] = new StringCell(familyName);
		}

		void setContact(final String contact) {
			cell[MetaDataKeys.contact.ordinal()] = new StringCell(contact);
		}

		void setReferenceDescription(final String referenceDescription) {
			cell[MetaDataKeys.reference_description.ordinal()] = new StringCell(referenceDescription);
		}

		void setReferenceDescriptionLink(final URL link) {
			cell[MetaDataKeys.reference_description_link.ordinal()] = new StringCell(link.toString());
		}

		void setCreatedDate(final String createdDate) {
			cell[MetaDataKeys.created_date.ordinal()] = new StringCell(createdDate);
		}

		void setModifiedDate(final String modifiedDate) {
			cell[MetaDataKeys.modified_date.ordinal()] = new StringCell(modifiedDate);
		}

		void setRights(final String rights) {
			cell[MetaDataKeys.rights.ordinal()] = new StringCell(rights);
		}

		void setNotes(final String notes) {
			cell[MetaDataKeys.notes.ordinal()] = new StringCell(notes);
		}

		void setCurationStatus(final String curationStatus) {
			cell[MetaDataKeys.curation_status.ordinal()] = new StringCell(curationStatus);
		}

		void setModelType(final String modelType) {
			cell[MetaDataKeys.model_type.ordinal()] = new StringCell(modelType);
		}

		void setModelSubject(final String modelSubject) {
			cell[MetaDataKeys.subject.ordinal()] = new StringCell(modelSubject);
		}

		void setFoodProcess(final String foodProcess) {
			cell[MetaDataKeys.food_process.ordinal()] = new StringCell(foodProcess);
		}

		void setDependentVariable(final String var) {
			cell[MetaDataKeys.depvar.ordinal()] = new StringCell(var);
		}

		void setDependentVariableUnit(final String unit) {
			cell[MetaDataKeys.depvar_unit.ordinal()] = new StringCell(unit);
		}

		void setDependentVariableMin(final double min) {
			cell[MetaDataKeys.depvar_min.ordinal()] = new DoubleCell(min);
		}

		void setDependentVariableMax(final double max) {
			cell[MetaDataKeys.depvar_max.ordinal()] = new DoubleCell(max);
		}

		void setIndependentVariables(final String[] vars) {
			String formattedVars = Arrays.stream(vars).collect(Collectors.joining("||"));
			cell[MetaDataKeys.indepvars.ordinal()] = new StringCell(formattedVars);
		}

		void setIndependentVariablesUnits(final String[] units) {
			String formattedVars = Arrays.stream(units).collect(Collectors.joining("||"));
			cell[MetaDataKeys.indepvars_units.ordinal()] = new StringCell(formattedVars);
		}

		void setIndependentVariablesMins(final double[] mins) {
			String formattedMins = Arrays.stream(mins).mapToObj(Double::toString).collect(Collectors.joining("||"));
			cell[MetaDataKeys.indepvars_mins.ordinal()] = new StringCell(formattedMins);
		}

		void setIndependentVariablesMaxs(final double[] maxs) {
			String formattedMaxs = Arrays.stream(maxs).mapToObj(Double::toString).collect(Collectors.joining("||"));
			cell[MetaDataKeys.indepvars_maxs.ordinal()] = new StringCell(formattedMaxs);
		}

		void setIndependentVariablesValues(final String values) {
			cell[MetaDataKeys.indepvars_values.ordinal()] = new StringCell(values);
		}

		void setHasData(final boolean hasData) {
			cell[MetaDataKeys.has_data.ordinal()] = hasData ? BooleanCell.TRUE : BooleanCell.FALSE;
		}
	}
}
