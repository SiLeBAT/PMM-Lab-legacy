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
package de.bund.bfr.knime.pmm.fskx.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;

import org.jdom2.JDOMException;
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
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.AssignmentRule;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Parameter;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLException;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.xml.XMLNode;
import org.sbml.jsbml.xml.XMLTriple;
import org.sbml.jsbml.xml.stax.SBMLWriter;

import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.writer.TableReader;
import de.bund.bfr.knime.pmm.common.writer.WriterUtils;
import de.bund.bfr.knime.pmm.fskx.FskMetaData;
import de.bund.bfr.knime.pmm.fskx.RMetaDataNode;
import de.bund.bfr.knime.pmm.fskx.RUri;
import de.bund.bfr.knime.pmm.fskx.ZipUri;
import de.bund.bfr.knime.pmm.fskx.port.FskPortObject;
import de.bund.bfr.pmfml.ModelClass;
import de.bund.bfr.pmfml.PMFUtil;
import de.bund.bfr.pmfml.file.uri.UriFactory;
import de.bund.bfr.pmfml.sbml.LimitsConstraint;
import de.bund.bfr.pmfml.sbml.Metadata;
import de.bund.bfr.pmfml.sbml.MetadataAnnotation;
import de.bund.bfr.pmfml.sbml.MetadataImpl;
import de.bund.bfr.pmfml.sbml.PMFCompartment;
import de.bund.bfr.pmfml.sbml.PMFSpecies;
import de.bund.bfr.pmfml.sbml.PMFUnitDefinition;
import de.bund.bfr.pmfml.sbml.Reference;
import de.bund.bfr.pmfml.sbml.ReferenceSBMLNode;
import de.bund.bfr.pmfml.sbml.SBMLFactory;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.CombineArchiveException;
import de.unirostock.sems.cbarchive.meta.DefaultMetaDataObject;

/**
 */
class FskxWriterNodeModel extends NodeModel {

	// Configuration keys
	protected static final String CFG_FILE = "file";

	private final SettingsModelString filePath = new SettingsModelString(CFG_FILE, null);

	private static final PortType[] inPortTypes = { FskPortObject.TYPE };
	private static final PortType[] outPortTypes = {};

	protected FskxWriterNodeModel() {
		super(inPortTypes, outPortTypes);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws FileCreationException
	 *             If a critical file could not be created. E.g. model script.
	 * @throws IOException
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec)
			throws CombineArchiveException {

		FskPortObject portObject = (FskPortObject) inData[0];

		try {
			Files.deleteIfExists(Paths.get(filePath.getStringValue()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// try to create CombineArchive
		try (CombineArchive archive = new CombineArchive(new File(filePath.getStringValue()))) {

			RMetaDataNode metaDataNode = new RMetaDataNode();
			URI rUri = RUri.createURI();

			// Adds model script
			if (portObject.getModelScript() != null) {
				archive.addEntry(createScriptFile(portObject.getModelScript()), "model.r", rUri);
				metaDataNode.setMainScript("model.r");
			}

			// Adds parameters script
			if (portObject.getParamScript() != null) {
				archive.addEntry(createScriptFile(portObject.getParamScript()), "param.r", rUri);
				metaDataNode.setParamScript("param.r");
			}

			// Adds visualization script
			if (portObject.getVizScript() != null) {
				archive.addEntry(createScriptFile(portObject.getVizScript()), "visualization.r", rUri);
				metaDataNode.setVisualizationScript("visualization.r");
			}

			// Adds R workspace file
			if (portObject.getWorkspaceFile() != null) {
				archive.addEntry(portObject.getWorkspaceFile(), "workspace.r", rUri);
				metaDataNode.setWorkspaceFile("workspace.r");
			}

			// Adds model meta data
			if (portObject.getTemplate() != null) {
				SBMLDocument doc = createSbmlDocument(portObject.getTemplate());

				File f = FileUtil.createTempFile("metaData", ".pmf");
				try {
					new SBMLWriter().write(doc, f);
					archive.addEntry(f, "metaData.pmf", UriFactory.createPMFURI());
				} catch (SBMLException | XMLStreamException e) {
					e.printStackTrace();
				}
			}

			// Adds R libraries
			URI zipUri = ZipUri.createURI();
			for (File lib : portObject.getLibraries()) {
				archive.addEntry(lib, lib.getName(), zipUri);
			}

			archive.addDescription(new DefaultMetaDataObject(metaDataNode.getNode()));
			archive.pack();

		} catch (IOException | JDOMException | ParseException | TransformerException e1) {
			e1.printStackTrace();
		}

		return new PortObject[] {};
	}

	private File createScriptFile(String script) throws IOException {
		File f = FileUtil.createTempFile("script", ".r");
		try (FileWriter fw = new FileWriter(f)) {
			fw.write(script);
		}

		return f;
	}

	/** {@inheritDoc} */
	@Override
	protected void reset() {
		// does nothing
	}

	/** {@inheritDoc} */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		return new PortObjectSpec[] {};
	}

	/** {@inheritDoc} */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		filePath.saveSettingsTo(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		filePath.loadSettingsFrom(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		filePath.validateSettings(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// nothing
	}

	/** {@inheritDoc} */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// nothing
	}

	/** Creates SBMLDocument out of a OpenFSMR template. */
	private static SBMLDocument createSbmlDocument(final FskMetaData template) {

		// Creates SBMLDocument for the primary model
		final SBMLDocument sbmlDocument = new SBMLDocument(TableReader.LEVEL, TableReader.VERSION);

		// Adds namespaces to the sbmlDocument
		TableReader.addNamespaces(sbmlDocument);

		// Adds document annotation
		Metadata metaData = new MetadataImpl();
		if (template.isSetCreator()) {
			metaData.setGivenName(template.getCreator());
		}
		if (template.isSetFamilyName()) {
			metaData.setFamilyName(template.getFamilyName());
		}
		if (template.isSetContact()) {
			metaData.setContact(template.getContact());
		}
		if (template.isSetCreatedDate()) {
			metaData.setCreatedDate(template.getCreatedDate().toString());
		}
		if (template.isSetModifiedDate()) {
			metaData.setModifiedDate(template.getModifiedDate().toString());
		}
		if (template.isSetCreatedDate()) {
			metaData.setType(template.getModelType());
		}
		if (template.isSetRights()) {
			metaData.setRights(template.getRights());
		}
		if (template.isSetReferenceDescriptionLink()) {
			metaData.setReferenceLink(template.getReferenceDescriptionLink().toString());
		}

		sbmlDocument.setAnnotation(new MetadataAnnotation(metaData).getAnnotation());

		// Creates model and names it
		Model model = sbmlDocument.createModel(PMFUtil.createId(template.getModelId()));
		if (template.isSetModelName()) {
			model.setName(template.getModelName());
		}

		// Sets model notes
		if (template.isSetNotes()) {
			try {
				model.setNotes(template.getNotes());
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}

		// Creates and adds compartment to the model
		PMFCompartment compartment = SBMLFactory.createPMFCompartment(PMFUtil.createId(template.getMatrix()),
				template.getMatrix());
		compartment.setDetail(template.getMatrixDetails());
		model.addCompartment(compartment.getCompartment());

		// Creates and adds species to the model
		String speciesId = PMFUtil.createId(template.getOrganism());
		String speciesName = template.getOrganism();
		String speciesUnit = PMFUtil.createId(template.getDependentVariableUnit());
		PMFSpecies species = SBMLFactory.createPMFSpecies(compartment.getId(), speciesId, speciesName, speciesUnit);
		model.addSpecies(species.getSpecies());

		// Add unit definitions here (before parameters)
		Set<String> unitsSet = new LinkedHashSet<>();
		unitsSet.add(template.getDependentVariableUnit().trim());
		template.getIndependentVariableUnits().forEach(unit -> unitsSet.add(unit.trim()));
		for (String unit : unitsSet) {
			try {
				PMFUnitDefinition unitDef = WriterUtils.createUnitFromDB(unit);

				// unitDef is not in PmmLab DB
				if (unitDef == null) {
					UnitDefinition ud = model.createUnitDefinition(PMFUtil.createId(unit));
					ud.setName(unit);
				} else {
					model.addUnitDefinition(unitDef.getUnitDefinition());
				}
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}

		// Adds dep parameter
		Parameter depParam = new Parameter(PMFUtil.createId(template.getDependentVariable()));
		depParam.setName(template.getDependentVariable());
		depParam.setUnits(PMFUtil.createId(template.getDependentVariableUnit()));
		model.addParameter(depParam);

		// Adds dep constraint
		if (template.isSetDependentVariableMin() || template.isSetDependentVariableMax()) {
			LimitsConstraint lc = new LimitsConstraint(template.getDependentVariable(),
					template.getDependentVariableMin(), template.getDependentVariableMax());
			if (lc.getConstraint() != null) {
				model.addConstraint(lc.getConstraint());
			}
		}

		// Adds independent parameters
		for (int i = 0; i < template.getIndependentVariables().size(); i++) {
			String var = template.getIndependentVariables().get(i);
			Parameter param = model.createParameter(PMFUtil.createId(var));
			param.setName(var);

			try {
				param.setUnits(PMFUtil.createId(template.getIndependentVariableUnits().get(i)));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			Double min = template.isSetIndependentVariableMins() ? template.getIndependentVariableMins().get(i) : null;
			Double max = template.isSetIndependentVariableMaxs() ? template.getIndependentVariableMaxs().get(i) : null;
			LimitsConstraint lc = new LimitsConstraint(param.getId(), min, max);
			if (lc.getConstraint() != null) {
				model.addConstraint(lc.getConstraint());
			}
		}

		// Add rule
		String formulaName = "Missing formula name";
		ModelClass modelClass = template.isSetModelSubject() ? template.getModelSubject() : ModelClass.UNKNOWN;
		int modelId = MathUtilities.getRandomNegativeInt();
		Reference[] references = new Reference[0];

		AssignmentRule rule = new AssignmentRule(3, 1);
		rule.setVariable(depParam.getId());
		rule.setAnnotation(new ModelRuleAnnotation(formulaName, modelClass, modelId, references).annotation);
		model.addRule(rule);

		return sbmlDocument;
	}

	private static class ModelRuleAnnotation {

		private Annotation annotation;

		private static final String FORMULA_TAG = "formulaName";
		private static final String SUBJECT_TAG = "subject";
		private static final String PMMLAB_ID = "pmmlabID";

		private ModelRuleAnnotation(String formulaName, ModelClass modelClass, int pmmlabID, Reference[] references) {
			// Builds metadata node
			XMLNode metadataNode = new XMLNode(new XMLTriple("metadata", null, "pmf"));
			this.annotation = new Annotation();
			this.annotation.setNonRDFAnnotation(metadataNode);

			// Creates annotation for formula name
			XMLNode nameNode = new XMLNode(new XMLTriple(FORMULA_TAG, null, "pmmlab"));
			nameNode.addChild(new XMLNode(formulaName));
			metadataNode.addChild(nameNode);

			// Creates annotation for modelClass
			XMLNode modelClassNode = new XMLNode(new XMLTriple(SUBJECT_TAG, null, "pmmlab"));
			modelClassNode.addChild(new XMLNode(modelClass.fullName()));
			metadataNode.addChild(modelClassNode);

			// Create annotation for pmmlabID
			XMLNode idNode = new XMLNode(new XMLTriple(PMMLAB_ID, null, "pmmlab"));
			idNode.addChild(new XMLNode(new Integer(pmmlabID).toString()));
			metadataNode.addChild(idNode);

			// Builds reference nodes
			for (Reference ref : references) {
				metadataNode.addChild(new ReferenceSBMLNode(ref).getNode());
			}
		}
	}
}
