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
package de.bund.bfr.knime.pmm.sbmlwriter;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.sbml.jsbml.SBMLWriter;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.PmmUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;

/**
 * This is the model implementation of SBMLWriter.
 *
 * @author Christian Thoens
 * @author Miguel de Alba
 */
public class SBMLWriterNodeModel extends NodeModel {

	private final SBMLWriterNodeSettings nodeSettings = new SBMLWriterNodeSettings();

	private KnimeSchema schema;

	/**
	 * Constructor for the node model.
	 */
	protected SBMLWriterNodeModel() {
		super(1, 0);
		schema = null;
	}

	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData, final ExecutionContext exec)
			throws Exception {
		TableReader reader = new TableReader(PmmUtilities.getTuples(inData[0], schema), nodeSettings.variableParams,
				nodeSettings.creatorGivenName, nodeSettings.creatorFamilyName, nodeSettings.creatorContact,
				new Date(nodeSettings.createdDate), new Date(nodeSettings.modifiedDate), nodeSettings.reference);
		Map<String, File> files = new LinkedHashMap<>();

		for (String name : reader.getDocuments().keySet()) {
			File file = new File(nodeSettings.outPath + "/" + name + ".sbml.xml");

			if (!nodeSettings.overwrite && file.exists()) {
				throw new IOException(file.getAbsolutePath() + " already exists");
			}

			files.put(name, file);
		}

		for (String name : reader.getDocuments().keySet()) {
			SBMLWriter.write(reader.getDocuments().get(name), files.get(name), name, "1.0");
		}

		return new BufferedDataTable[] {};
	}

	@Override
	protected void reset() {
	}

	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs) throws InvalidSettingsException {
		if (SchemaFactory.createM12DataSchema().conforms(inSpecs[0])) {
			schema = SchemaFactory.createM12DataSchema();
		} else if (SchemaFactory.createM1DataSchema().conforms(inSpecs[0])) {
			schema = SchemaFactory.createM1DataSchema();
		} else if (nodeSettings.outPath == null || nodeSettings.variableParams == null) {
			throw new InvalidSettingsException("Node must be configured");
		} else {
			throw new InvalidSettingsException("Invalid Input");
		}

		return new DataTableSpec[] {};
	}

	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		nodeSettings.save(settings);
	}

	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
		nodeSettings.load(settings);
	}

	@Override
	protected void validateSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
		// Does nothing
	}

	@Override
	protected void loadInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}

	@Override
	protected void saveInternals(final File internDir, final ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
	}
}
