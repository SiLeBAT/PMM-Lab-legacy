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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.IOUtils;
import org.jdom2.JDOMException;
import org.jsoup.Jsoup;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
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
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.util.filters.Filter;
import org.sbml.jsbml.xml.stax.SBMLReader;

import com.google.common.base.Strings;

import de.bund.bfr.knime.pmm.common.KnimeUtils;
import de.bund.bfr.knime.pmm.fskx.FskMetaData;
import de.bund.bfr.knime.pmm.fskx.FskMetaData.DataType;
import de.bund.bfr.knime.pmm.fskx.FskMetaDataTuple;
import de.bund.bfr.knime.pmm.fskx.RMetaDataNode;
import de.bund.bfr.knime.pmm.fskx.URIS;
import de.bund.bfr.knime.pmm.fskx.Variable;
import de.bund.bfr.knime.pmm.fskx.controller.IRController.RException;
import de.bund.bfr.knime.pmm.fskx.controller.LibRegistry;
import de.bund.bfr.knime.pmm.fskx.controller.RController;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObjectSpec;
import de.bund.bfr.pmfml.sbml.Limits;
import de.bund.bfr.pmfml.sbml.LimitsConstraint;
import de.bund.bfr.pmfml.sbml.Metadata;
import de.bund.bfr.pmfml.sbml.MetadataAnnotation;
import de.bund.bfr.pmfml.sbml.ModelRule;
import de.bund.bfr.pmfml.sbml.PMFCompartment;
import de.bund.bfr.pmfml.sbml.PMFSpecies;
import de.bund.bfr.pmfml.sbml.SBMLFactory;
import de.unirostock.sems.cbarchive.ArchiveEntry;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;

class FskxReaderNodeModel extends NodeModel {

	private static final NodeLogger LOGGER = NodeLogger.getLogger(FskxReaderNodeModel.class);

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
	private static final DataTableSpec metadataSpec = FskMetaDataTuple.createSpec();

	protected FskxReaderNodeModel() {
		super(inPortTypes, outPortTypes);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws RException
	 * @throws REXPMismatchException
	 * @throws MalformedURLException
	 * @throws InvalidPathException
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec)
			throws CombineArchiveException, REXPMismatchException, RException, InvalidPathException,
			MalformedURLException {

		FskPortObject portObj = new FskPortObject();

		File archiveFile = KnimeUtils.getFile(filename.getStringValue());
		try (CombineArchive archive = new CombineArchive(archiveFile)) {
			// Gets annotation
			RMetaDataNode node = new RMetaDataNode(archive.getDescriptions().get(0).getXmlDescription());

			// Gets model script
			if (node.getMainScript() != null) {
				ArchiveEntry entry = archive.getEntry(node.getMainScript());
				portObj.model = loadScriptFromEntry(entry);
			}

			// Gets parameters script
			if (node.getParametersScript() != null) {
				ArchiveEntry entry = archive.getEntry(node.getParametersScript());
				portObj.param = loadScriptFromEntry(entry);
			}

			// Gets visualization script
			if (node.getVisualizationScript() != null) {
				ArchiveEntry entry = archive.getEntry(node.getVisualizationScript());
				portObj.viz = loadScriptFromEntry(entry);
			}

			// Gets workspace file
			if (node.getWorkspaceFile() != null) {
				ArchiveEntry entry = archive.getEntry(node.getWorkspaceFile());
				try {
					portObj.workspace = FileUtil.createTempFile("workspace", ".r");
					entry.extractFile(portObj.workspace);
				} catch (IOException e) {
					LOGGER.warn("Workspace could not be restored. Please rerun model to obtain results.");
				}
			}

			// Gets model meta data
			if (archive.getNumEntriesWithFormat(URIS.pmf) == 1) {
				ArchiveEntry entry = archive.getEntriesWithFormat(URIS.pmf).get(0);
				try {
					File f = FileUtil.createTempFile("metaData", ".pmf");
					entry.extractFile(f);

					SBMLDocument doc = new SBMLReader().readSBML(f);
					portObj.template = processMetadata(doc);

				} catch (IOException | XMLStreamException e) {
					LOGGER.error("Metadata document could not be read: " + entry.getFileName());
				}
			}
			portObj.template.software = FskMetaData.Software.R;

			// Set types of variables
			{
				// TODO: usually the type of the depvar is numeric although it
				// should be checked
				portObj.template.dependentVariable.type = DataType.numeric;

				/*
				 * TODO: FskMetaData is keeping only numeric types for
				 * independent variables so it does not make sense to try to
				 * obtain the type here since it will always be numeric. Once
				 * the rest of types are supported in FskMetaData the following
				 * code should be update to retrieve the types.
				 */
				for (Variable v : portObj.template.independentVariables) {
					v.type = DataType.numeric;
				}
			}

			// Gets R libraries
			// Gets library names from the zip entries in the CombineArchive
			List<String> libNames = archive.getEntriesWithFormat(URIS.zip).stream()
					.map(entry -> entry.getFileName().split("\\_")[0]).collect(Collectors.toList());

			if (!libNames.isEmpty()) {

				LibRegistry libRegistry = LibRegistry.instance();

				// Filters and installs missing libraries
				List<String> missingLibs = libNames.stream().filter(lib -> !libRegistry.isInstalled(lib))
						.collect(Collectors.toList());
				if (!missingLibs.isEmpty()) {
					libRegistry.installLibs(missingLibs);
				}

				// Converts and return set of Paths returned from plugin to set
				Set<File> libs = libRegistry.getPaths(libNames).stream().map(Path::toFile).collect(Collectors.toSet());
				portObj.libs.addAll(libs);
			}

		} catch (IOException | JDOMException | ParseException e) {
			e.printStackTrace();
		}

		// Meta data port
		BufferedDataContainer fsmrContainer = exec.createDataContainer(metadataSpec);
		FskMetaDataTuple metaDataTuple = new FskMetaDataTuple(portObj.template);

		// Add independent variables values from parameters script
		if (!Strings.isNullOrEmpty(portObj.param)) {
			List<String> varList = getValuesFromAssignments(portObj.param);
			String values = varList.stream().collect(Collectors.joining("||"));

			metaDataTuple.setCell(FskMetaDataTuple.Key.indepvars_values.ordinal(), values);
		}

		// Validate model
		try (RController controller = new RController()) {
			String fullScript = portObj.param + "\n" + portObj.model;
			controller.eval(fullScript);
		} catch (RException e) {
			throw new RException("Input model is not valid", e);
		}

		fsmrContainer.addRowToTable(metaDataTuple);
		fsmrContainer.close();

		RPortObject rObj = new RPortObject(portObj.workspace);

		return new PortObject[] { portObj, rObj, fsmrContainer.getTable() };
	}

	private static String loadScriptFromEntry(final ArchiveEntry entry) throws IOException {
		// Create temporary file with a random name. The name does not matter,
		// since the file will be
		// deleted by KNIME itself.
		File f = FileUtil.createTempFile("script", ".r");
		entry.extractFile(f);

		// Read script from f and return script
		try (FileInputStream fis = new FileInputStream(f)) {
			return IOUtils.toString(fis, "UTF-8");
		}
	}

	/** {@inheritDoc} */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] { fskSpec, rSpec, metadataSpec };
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
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// nothing
	}

	/** {@inheritDoc} */
	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// nothing
	}

	/** {@inheritDoc} */
	@Override
	protected void reset() {
		// does nothing
	}

	// --- utility ---

	// TODO: take functionality out of FSMRUtils processPrevalenceModel
	private static FskMetaData processMetadata(final SBMLDocument doc) {

		FskMetaData template = new FskMetaData();

		Model model = doc.getModel();
		AssignmentRule rule = (AssignmentRule) model.getRule(0);

		// caches limits
		List<Limits> limits = model.getListOfConstraints().stream().map(LimitsConstraint::new)
				.map(LimitsConstraint::getLimits).collect(Collectors.toList());

		template.modelId = model.getId();
		template.modelName = model.getName();

		// organism data
		{
			PMFSpecies species = SBMLFactory.createPMFSpecies(model.getSpecies(0));
			template.organism = species.getName();
			if (species.isSetDetail()) {
				template.organismDetails = species.getDetail();
			}
		}

		// matrix data
		{
			PMFCompartment compartment = SBMLFactory.createPMFCompartment(model.getCompartment(0));
			template.matrix = compartment.getName();
			if (compartment.isSetDetail()) {
				template.matrixDetails = compartment.getDetail();
			}
		}

		// creator
		Metadata metadataAnnot = new MetadataAnnotation(doc.getAnnotation()).getMetadata();
		if (metadataAnnot.isSetGivenName()) {
			template.creator = metadataAnnot.getGivenName();
		}

		// family name
		if (metadataAnnot.isSetFamilyName()) {
			template.familyName = metadataAnnot.getFamilyName();
		}

		// contact
		if (metadataAnnot.isSetContact()) {
			template.contact = metadataAnnot.getContact();
		}

		// reference description link
		if (metadataAnnot.isSetReferenceLink()) {
			String linkAsString = metadataAnnot.getReferenceLink();
			try {
				template.referenceDescriptionLink = new URL(linkAsString);
			} catch (MalformedURLException e) {
				System.err.println(linkAsString + " is not a valid URL");
				e.printStackTrace();
			}
		}

		// created date
		if (metadataAnnot.isSetCreatedDate()) {
			String dateAsString = metadataAnnot.getCreatedDate();
			try {
				template.createdDate = FskMetaData.dateFormat.parse(dateAsString);
			} catch (ParseException e) {
				System.err.println(dateAsString + " is not a valid date");
				e.printStackTrace();
			}
		}

		// modified date
		if (metadataAnnot.isSetModifiedDate()) {
			String dateAsString = metadataAnnot.getModifiedDate();
			try {
				template.modifiedDate = FskMetaData.dateFormat.parse(dateAsString);
			} catch (ParseException e) {
				System.err.println(dateAsString + " is not a valid date");
				e.printStackTrace();
			}
		}

		// model rights
		if (metadataAnnot.isSetRights()) {
			template.rights = metadataAnnot.getRights();
		}

		// model type
		if (metadataAnnot.isSetType()) {
			template.type = metadataAnnot.getType();
		}

		template.subject = new ModelRule(rule).getModelClass();

		// model notes
		if (model.isSetNotes()) {
			try {
				template.notes = Jsoup.parse(model.getNotesString()).text();
			} catch (XMLStreamException e) {
				System.err.println("Error accesing the notes of " + model);
				e.printStackTrace();
			}
		}

		// dependent variable data
		{
			String depId = rule.getVariable();

			// Gets parameter for the dependent variable and sets it
			Parameter param = model.getParameter(depId);
			template.dependentVariable.name = param.getName();

			// Gets and sets dependent variable unit
			String unitId = param.getUnits();
			if (!unitId.equals("dimensionless")) {
				UnitDefinition unitDef = model.getUnitDefinition(unitId);
				if (unitDef != null) {
					template.dependentVariable.unit = unitDef.getName();
				}
			}

			// Sets dependent variable min & max
			for (Limits lim : limits) {
				if (lim.getVar().equals(depId)) {
					if (lim.getMin() != null)
						template.dependentVariable.min = lim.getMin().toString();
					if (lim.getMax() != null)
						template.dependentVariable.max = lim.getMax().toString();
					break;
				}
			}
		}

		// independent variable data
		{
			String depId = rule.getVariable();

			List<Parameter> indepParams = model.getListOfParameters().filterList(new Filter() {
				@Override
				public boolean accepts(Object o) {
					return !((Parameter) o).getId().equals(depId);
				}
			});

			final int numParams = indepParams.size();

			for (int p = 0; p < numParams; p++) {
				Parameter param = indepParams.get(p);
				Variable variable = new Variable();

				variable.name = param.getName();

				// unit
				String unitId = param.getUnits();
				variable.unit = "";
				if (!unitId.equals("dimensionless")) {
					UnitDefinition unitDef = model.getUnitDefinition(unitId);
					if (unitDef != null) {
						variable.unit = unitDef.getName();
					}
				}

				Limits paramLimits = limits.stream().filter(lim -> lim.getVar().equals(param.getId())).findFirst()
						.get();
				variable.min = paramLimits.getMin().toString();
				variable.max = paramLimits.getMax().toString();
				
				variable.value = "";
				
				template.independentVariables.add(variable);
			}
		}

		template.hasData = false;

		return template;
	}

	/**
	 * Parses an R command with the = assignment operator. E.g. x = value.
	 */
	private static class EqualsAssignment {
		String variable;
		String value;

		public EqualsAssignment(String line) {
			String[] tokens = line.split("||");
			variable = tokens[0].trim();
			value = tokens[1].trim();
		}
	}

	/**
	 * Parses an R command with the <- assignment operator. E.g. x <- value
	 */
	private static class LeftAssignment {
		String variable;
		String value;

		public LeftAssignment(String line) {
			String[] tokens = line.split("<-");
			variable = tokens[0].trim();
			value = tokens[1].trim();
		}
	}

	/**
	 * Parses an R command with the <<- scoping assignment operator. E.g. x <<-
	 * value
	 */
	private static class SuperLeftAssignment {
		String variable;
		String value;

		public SuperLeftAssignment(String line) {
			String[] tokens = line.split("<<-");
			variable = tokens[0].trim();
			value = tokens[1].trim();
		}
	}

	/**
	 * Parses an R command with the -> assignment operator. E.g. value -> x
	 */
	private static class RightAssignment {
		String variable;
		String value;

		public RightAssignment(String line) {
			String[] tokens = line.split("->");
			value = tokens[0].trim();
			variable = tokens[1].trim();
		}
	}

	/**
	 * Parses an R command with the ->> assignment operator. E.g. value ->> x
	 */
	private static class SuperRightAssignment {
		String variable;
		String value;

		public SuperRightAssignment(String line) {
			String[] tokens = line.split("->>");
			value = tokens[0].trim();
			variable = tokens[1].trim();
		}
	}

	private static List<String> getValuesFromAssignments(String paramScript) {

		List<String> varList = new ArrayList<>();
		for (String line : paramScript.split("\\r?\\n")) {
			line = line.trim();
			if (line.startsWith("#"))
				continue;

			if (line.indexOf("=") != -1) {
				varList.add(new EqualsAssignment(line).value);
			} else if (line.indexOf("<<-") != -1) {
				varList.add(new SuperLeftAssignment(line).value);
			} else if (line.indexOf("<-") != -1) {
				varList.add(new LeftAssignment(line).value);
			} else if (line.indexOf("->>") != -1) {
				varList.add(new SuperRightAssignment(line).value);
			} else if (line.indexOf("->") != -1) {
				varList.add(new RightAssignment(line).value);
			}
		}

		return varList;
	}

	private static Map<String, DataType> getTypesFromAssignments(String paramScript) {
		Map<String, DataType> typeMap = new HashMap<>();
		for (String line : paramScript.split("\\r?\\n")) {
			line = line.trim();

			// Skip comments
			if (line.startsWith("#"))
				continue;

			if (line.indexOf("=") != -1) {
				EqualsAssignment ea = new EqualsAssignment(line);
				DataType type = getDataTypeFromValue(ea.value);
				typeMap.put(ea.variable, type);
			} else if (line.indexOf("<<-") != -1) {
				SuperLeftAssignment sla = new SuperLeftAssignment(line);
				DataType type = getDataTypeFromValue(sla.value);
				typeMap.put(sla.variable, type);
			} else if (line.indexOf("<-") != -1) {
				LeftAssignment la = new LeftAssignment(line);
				DataType type = getDataTypeFromValue(la.value);
				typeMap.put(la.variable, type);
			} else if (line.indexOf("->>") != -1) {
				SuperRightAssignment sra = new SuperRightAssignment(line);
				DataType type = getDataTypeFromValue(sra.value);
				typeMap.put(sra.variable, type);
			} else if (line.indexOf("->") != -1) {
				RightAssignment ra = new RightAssignment(line);
				DataType type = getDataTypeFromValue(ra.value);
				typeMap.put(ra.variable, type);
			}
		}

		return typeMap;
	}

	private static DataType getDataTypeFromValue(String value) {

		try {
			Integer.parseInt(value);
			// If value is parsed as an integer then return DataType.integer
			return DataType.integer;
		} catch (NumberFormatException e) {
			// Keep parsing
		}

		try {
			Double.parseDouble(value);
			// If value is parsed as an double then return DataType#numeric
			return DataType.numeric;
		} catch (NumberFormatException e) {
			// Keep parsing
		}

		return DataType.character;
	}
}
