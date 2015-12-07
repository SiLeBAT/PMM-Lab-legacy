/*******************************************************************************
 * Copyright (c) 2015 Federal Institute for Risk Assessment (BfR), Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Department Biological Safety - BfR
 *******************************************************************************/
package de.bund.bfr.knime.pmm.openfsmr;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.Element;
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
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.sbml.jsbml.SBMLDocument;

import de.bund.bfr.knime.pmm.FSMRUtils;
import de.bund.bfr.knime.pmm.extendedtable.generictablemodel.KnimeTuple;
import de.bund.bfr.pmf.ModelType;
import de.bund.bfr.pmf.file.ExperimentalDataFile;
import de.bund.bfr.pmf.file.ManualSecondaryModelFile;
import de.bund.bfr.pmf.file.ManualTertiaryModelFile;
import de.bund.bfr.pmf.file.OneStepSecondaryModelFile;
import de.bund.bfr.pmf.file.OneStepTertiaryModelFile;
import de.bund.bfr.pmf.file.PMFMetadataNode;
import de.bund.bfr.pmf.file.PrimaryModelWDataFile;
import de.bund.bfr.pmf.file.PrimaryModelWODataFile;
import de.bund.bfr.pmf.file.TwoStepSecondaryModelFile;
import de.bund.bfr.pmf.file.TwoStepTertiaryModelFile;
import de.bund.bfr.pmf.model.ExperimentalData;
import de.bund.bfr.pmf.model.ManualSecondaryModel;
import de.bund.bfr.pmf.model.ManualTertiaryModel;
import de.bund.bfr.pmf.model.OneStepSecondaryModel;
import de.bund.bfr.pmf.model.OneStepTertiaryModel;
import de.bund.bfr.pmf.model.PrimaryModelWData;
import de.bund.bfr.pmf.model.PrimaryModelWOData;
import de.bund.bfr.pmf.model.TwoStepSecondaryModel;
import de.bund.bfr.pmf.model.TwoStepTertiaryModel;
import de.bund.bfr.pmf.numl.NuMLDocument;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

/**
 * This is the model implementation of OpenFSMRConverter.
 * 
 * Author: Miguel de Alba Aparicio
 */
public class OpenFSMRConverterNodeModel extends NodeModel {

	// configuration keys
	public static final String CFGKEY_DIR = "directory";
	public static final String CFGKEY_FILES = "files";

	// defaults for persistent state
	public static final String DEFAULT_DIR = "c:/";
	public static final String[] DEFAULT_FILES = new String[0];

	// persistent state
	private SettingsModelString selectedDirectory = new SettingsModelString(CFGKEY_DIR, DEFAULT_DIR);
	private SettingsModelStringArray selectedFiles = new SettingsModelStringArray(CFGKEY_FILES, DEFAULT_FILES);

	/** Constructor for the node model. */
	protected OpenFSMRConverterNodeModel() {
		// 0 input ports and 1 output port
		super(0, 1);
	}

	/** {@inheritDoc} */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		BufferedDataContainer container = exec.createDataContainer(new OpenFSMRSchema().createSpec());
		for (String selectedFile : selectedFiles.getStringArrayValue()) {
			// Builds full path
			String fullpath = selectedDirectory.getStringValue() + "/" + selectedFile;

			try {
				List<FSMRTemplate> templates = createTemplatesFromPMF(fullpath);

				for (FSMRTemplate template : templates) {
					KnimeTuple tuple = FSMRUtils.createTupleFromTemplate(template);
					container.addRowToTable(tuple);
				}
			} catch (Exception e) {
				System.err.println("Could not process file: " + selectedFile);
				e.printStackTrace();
			}
		}
		container.close();
		BufferedDataTable[] tables = { container.getTable() };
		return tables;
	}

	/** {@inheritDoc} */
	@Override
	protected void reset() {
	}

	/** {@inheritDoc} */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		return new DataTableSpec[] { null };
	}

	/** {@inheritDoc} */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		selectedDirectory.saveSettingsTo(settings);
		selectedFiles.saveSettingsTo(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		selectedDirectory.loadSettingsFrom(settings);
		selectedFiles.loadSettingsFrom(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		selectedDirectory.validateSettings(settings);
		selectedFiles.validateSettings(settings);
	}

	/** {@inheritDoc} */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	/** {@inheritDoc} */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	private static List<FSMRTemplate> createTemplatesFromPMF(String filepath) throws Exception {

		// Get model type from annotation in the metadata file
		// a) Open archive
		CombineArchive ca = new CombineArchive(new File(filepath));

		// b) Get annotation
		MetaDataObject mdo = ca.getDescriptions().get(0);
		Element metaParent = mdo.getXmlDescription();
		PMFMetadataNode pmfMetadataNode = new PMFMetadataNode(metaParent);
		ModelType modelType = pmfMetadataNode.getModelType();

		// c) Close archive
		ca.close();

		List<FSMRTemplate> templates = new LinkedList<>();
		switch (modelType) {
		case EXPERIMENTAL_DATA:
			// Obtain an OpenFSMR template per data file
			for (ExperimentalData ed : ExperimentalDataFile.read(filepath)) {
				NuMLDocument doc = ed.getDoc();
				FSMRTemplate template = FSMRUtils.processData(doc);
				templates.add(template);
			}
			break;
		case PRIMARY_MODEL_WDATA:
			// Obtain an OpenFSMR template per primary model
			for (PrimaryModelWData pm : PrimaryModelWDataFile.read(filepath)) {
				SBMLDocument primModelDoc = pm.getModelDoc();
				FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(primModelDoc);
				templates.add(template);
			}
			break;
		case PRIMARY_MODEL_WODATA:
			// Obtain an OpenFSMR template per primary model
			for (PrimaryModelWOData pm : PrimaryModelWODataFile.read(filepath)) {
				SBMLDocument primModelDoc = pm.getDoc();
				FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(primModelDoc);
				templates.add(template);
			}
			break;
		case TWO_STEP_SECONDARY_MODEL:
			// Obtain an OpenFSMR template per secondary model
			for (TwoStepSecondaryModel sm : TwoStepSecondaryModelFile.read(filepath)) {
				// Gets the model from the first primary model
				SBMLDocument primModelDoc = sm.getPrimModels().get(0).getModelDoc();
				FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(primModelDoc);
				templates.add(template);
			}
			break;
		case ONE_STEP_SECONDARY_MODEL:
			// Obtain an OpenFSMR template per secondary model
			for (OneStepSecondaryModel sm : OneStepSecondaryModelFile.read(filepath)) {
				SBMLDocument primModelDoc = sm.getModelDoc();
				FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(primModelDoc);
				templates.add(template);
			}
			break;
		case MANUAL_SECONDARY_MODEL:
			// Obtain an OpenFSMR template per secondary model
			for (ManualSecondaryModel sm : ManualSecondaryModelFile.read(filepath)) {
				SBMLDocument doc = sm.getDoc();
				FSMRTemplate template = FSMRUtils.processModelWithoutMicrobialData(doc);
				templates.add(template);
			}
			break;
		case TWO_STEP_TERTIARY_MODEL:
			// Obtain a OpenFSMR template per tertiary model
			for (TwoStepTertiaryModel tm : TwoStepTertiaryModelFile.read(filepath)) {
				SBMLDocument tertDoc = tm.getTertDoc();
				FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(tertDoc);
				templates.add(template);
			}
			break;
		case ONE_STEP_TERTIARY_MODEL:
			// Obtain a OpenFSMR template per tertiary model
			for (OneStepTertiaryModel tm : OneStepTertiaryModelFile.read(filepath)) {
				SBMLDocument tertDoc = tm.getTertiaryDoc();
				FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(tertDoc);
				templates.add(template);
			}
			break;
		case MANUAL_TERTIARY_MODEL:
			// Obtain a OpenFSMR template per tertiary model
			for (ManualTertiaryModel tm : ManualTertiaryModelFile.read(filepath)) {
				SBMLDocument tertDoc = tm.getTertiaryDoc();
				FSMRTemplate template = FSMRUtils.processModelWithMicrobialData(tertDoc);
				templates.add(template);
			}
			break;
		}
		return templates;
	}
}
