/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Joergen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thoens (BfR)
 * Annemarie Kaesbohrer (BfR)
 * Bernd Appel (BfR)
 * 
 * PMM-Lab is a project under development. Contributions are welcome.
 * 
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
 ******************************************************************************/
package de.bund.bfr.knime.pmm.xlstimeseriesreader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.XLSReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of XLSTimeSeriesReader.
 * 
 * 
 * @author Christian Thoens
 */
public class XLSTimeSeriesReaderNodeModel extends NodeModel {

	private SettingsHelper set;

	/**
	 * Constructor for the node model.
	 */
	protected XLSTimeSeriesReaderNodeModel() {
		super(0, 1);
		set = new SettingsHelper();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		XLSReader xlsReader = new XLSReader();
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>(xlsReader
				.getTimeSeriesTuples(new File(set.getFileName()),
						set.getSheetName(), set.getColumnMappings(),
						set.getTimeUnit(), set.getConcentrationUnit(),
						set.getAgentColumn(), set.getAgentMappings(),
						set.getMatrixColumn(), set.getMatrixMappings())
				.values());

		for (String warning : xlsReader.getWarnings()) {
			setWarningMessage(warning);
		}

		if (set.getAgentColumn() == null && set.getAgent() != null) {
			for (KnimeTuple tuple : tuples) {
				PmmXmlDoc agentXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_AGENT);

				((AgentXml) agentXml.get(0)).setId(set.getAgent().getId());
				((AgentXml) agentXml.get(0)).setName(set.getAgent().getName());
				((AgentXml) agentXml.get(0)).setDbuuid(set.getAgent()
						.getDbuuid());
				tuple.setValue(TimeSeriesSchema.ATT_AGENT, agentXml);
			}
		}

		if (set.getMatrixColumn() == null && set.getMatrix() != null) {
			for (KnimeTuple tuple : tuples) {
				PmmXmlDoc matrixXml = tuple
						.getPmmXml(TimeSeriesSchema.ATT_MATRIX);

				((MatrixXml) matrixXml.get(0)).setID(set.getMatrix().getID());
				((MatrixXml) matrixXml.get(0)).setName(set.getMatrix()
						.getName());
				((MatrixXml) matrixXml.get(0)).setDbuuid(set.getMatrix()
						.getDbuuid());
				tuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixXml);
			}
		}

		PmmXmlDoc literatureXML = new PmmXmlDoc();

		for (LiteratureItem item : set.getLiterature()) {
			literatureXML.add(item);
		}

		for (KnimeTuple tuple : tuples) {
			tuple.setValue(TimeSeriesSchema.ATT_LITMD, literatureXML);
		}

		BufferedDataContainer container = exec
				.createDataContainer(SchemaFactory.createDataSchema()
						.createSpec());

		for (KnimeTuple tuple : tuples) {
			container.addRowToTable(tuple);
		}

		container.close();

		return new BufferedDataTable[] { container.getTable() };
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
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {
		if (set.getFileName() == null) {
			throw new InvalidSettingsException("");
		}

		return new DataTableSpec[] { SchemaFactory.createDataSchema()
				.createSpec() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		set.saveSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		set.loadSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
			CanceledExecutionException {
	}

}
