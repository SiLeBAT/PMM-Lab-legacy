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
package de.bund.bfr.knime.pmm.pmfxreader;

import java.io.File;
import java.io.IOException;

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

import de.bund.bfr.knime.pmm.common.reader.ExperimentalDataReader;
import de.bund.bfr.knime.pmm.common.reader.ManualSecondaryModelReader;
import de.bund.bfr.knime.pmm.common.reader.ManualTertiaryModelReader;
import de.bund.bfr.knime.pmm.common.reader.OneStepSecondaryModelReader;
import de.bund.bfr.knime.pmm.common.reader.OneStepTertiaryModelReader;
import de.bund.bfr.knime.pmm.common.reader.PrimaryModelWDataReader;
import de.bund.bfr.knime.pmm.common.reader.PrimaryModelWODataReader;
import de.bund.bfr.knime.pmm.common.reader.Reader;
import de.bund.bfr.knime.pmm.common.reader.TwoStepSecondaryModelReader;
import de.bund.bfr.knime.pmm.common.reader.TwoStepTertiaryModelReader;
import de.bund.bfr.pmf.file.PMFMetadataNode;
import de.unirostock.sems.cbarchive.CombineArchive;
import de.unirostock.sems.cbarchive.meta.MetaDataObject;

public class PMFXReaderNodeModel extends NodeModel {

	// configuration keys
	public static final String CFGKEY_FILE = "filename";
	// defaults for persistent state
	private static final String DEFAULT_FILE = "c:/temp/foo.xml";

	// persistent state
	private SettingsModelString filename = new SettingsModelString(CFGKEY_FILE, DEFAULT_FILE);

	Reader reader; // current reader

	/**
	 * Constructor for the node model.
	 */
	protected PMFXReaderNodeModel() {
		// 0 input ports and 2 input ports
		super(0, 2);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		BufferedDataTable[] tables = null;
		tables = loadPMF(exec);
		return tables;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		return new DataTableSpec[] { null, null };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		filename.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		filename.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {

		// TODO check if the settings could be applied to our model
		// e.g. if the count is in a certain range (which is ensured by the
		// SettingsModel).
		// Do not actually set any values of any member variables.
		filename.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	// Load PMF file
	private BufferedDataTable[] loadPMF(final ExecutionContext exec) throws Exception {
		// Get model type from annotation in the metadata file

		// a) Open archive
		String filepath = filename.getStringValue();
		CombineArchive ca = new CombineArchive(new File(filepath));

		// b) Get annotation
		MetaDataObject mdo = ca.getDescriptions().get(0);
		Element metaParent = mdo.getXmlDescription();
		PMFMetadataNode pmfMetadataNode = new PMFMetadataNode(metaParent);

		// c) Close archive
		ca.close();

		switch (pmfMetadataNode.getModelType()) {
		case EXPERIMENTAL_DATA:
			reader = new ExperimentalDataReader();
			break;
		case PRIMARY_MODEL_WDATA:
			reader = new PrimaryModelWDataReader();
			break;
		case PRIMARY_MODEL_WODATA:
			reader = new PrimaryModelWODataReader();
			break;
		case TWO_STEP_SECONDARY_MODEL:
			reader = new TwoStepSecondaryModelReader();
			break;
		case ONE_STEP_SECONDARY_MODEL:
			reader = new OneStepSecondaryModelReader();
			break;
		case MANUAL_SECONDARY_MODEL:
			reader = new ManualSecondaryModelReader();
			break;
		case TWO_STEP_TERTIARY_MODEL:
			reader = new TwoStepTertiaryModelReader();
			break;
		case ONE_STEP_TERTIARY_MODEL:
			reader = new OneStepTertiaryModelReader();
			break;
		case MANUAL_TERTIARY_MODEL:
			reader = new ManualTertiaryModelReader();
			break;
		}

		BufferedDataContainer[] containers = reader.read(filepath, true, exec);
		BufferedDataTable[] tables = { containers[0].getTable(), containers[1].getTable() };
		return tables;
	}
}
