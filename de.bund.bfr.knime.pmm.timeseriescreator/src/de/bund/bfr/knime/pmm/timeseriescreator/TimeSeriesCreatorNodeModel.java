/*******************************************************************************
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * 
 * PMM-Lab is a set of KNIME-Nodes and KNIME workflows running within the KNIME software plattform (http://www.knime.org.).
 * 
 * PMM-Lab © 2012, Federal Institute for Risk Assessment (BfR), Germany
 * Contact: armin.weiser@bfr.bund.de or matthias.filter@bfr.bund.de 
 * 
 * Developers and contributors to the PMM-Lab project are 
 * Jörgen Brandt (BfR)
 * Armin A. Weiser (BfR)
 * Matthias Filter (BfR)
 * Alexander Falenski (BfR)
 * Christian Thöns (BfR)
 * Annemarie Käsbohrer (BfR)
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
package de.bund.bfr.knime.pmm.timeseriescreator;

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
import de.bund.bfr.knime.pmm.common.MdInfoXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.TimeSeriesXml;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;
import de.bund.bfr.knime.pmm.common.units.BacterialConcentration;
import de.bund.bfr.knime.pmm.common.units.Time;

/**
 * This is the model implementation of TimeSeriesCreator.
 * 
 * 
 * @author Christian Thoens
 */
public class TimeSeriesCreatorNodeModel extends NodeModel {

	protected static final String CFGKEY_LITERATURE = "Literature";
	protected static final String CFGKEY_AGENT = "Agent";
	protected static final String CFGKEY_MATRIX = "Matrix";
	protected static final String CFGKEY_COMMENT = "Comment";
	protected static final String CFGKEY_MISC = "Misc";
	protected static final String CFGKEY_TIMESERIES = "TimeSeries";
	protected static final String CFGKEY_TIMEUNIT = "TimeUnit";
	protected static final String CFGKEY_LOGCUNIT = "LogcUnit";

	private List<LiteratureItem> literature;
	private AgentXml agent;
	private MatrixXml matrix;
	private String comment;
	private List<TimeSeriesXml> timeSeries;
	private String timeUnit;
	private String logcUnit;
	private List<MiscXml> misc;

	/**
	 * Constructor for the node model.
	 */
	protected TimeSeriesCreatorNodeModel() {
		super(0, 1);
		literature = new ArrayList<>();
		agent = null;
		matrix = null;
		comment = "";
		timeSeries = new ArrayList<>();
		timeUnit = new Time().getStandardUnit();
		logcUnit = new BacterialConcentration().getStandardUnit();
		misc = new ArrayList<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		BufferedDataContainer container = exec
				.createDataContainer(SchemaFactory.createDataSchema()
						.createSpec());
		int id = MathUtilities.getRandomNegativeInt();
		PmmXmlDoc timeSeriesXml = new PmmXmlDoc();
		PmmXmlDoc miscXML = new PmmXmlDoc();
		PmmXmlDoc agentXml = new PmmXmlDoc();
		PmmXmlDoc matrixXml = new PmmXmlDoc();
		PmmXmlDoc literatureXML = new PmmXmlDoc();

		literatureXML.getElementSet().addAll(literature);
		miscXML.getElementSet().addAll(misc);

		if (agent != null) {
			agentXml.add(agent);
		} else {
			agentXml.add(new AgentXml());
		}

		if (matrix != null) {
			matrixXml.add(matrix);
		} else {
			matrixXml.add(new MatrixXml());
		}

		for (TimeSeriesXml p : timeSeries) {
			timeSeriesXml.add(p);
		}

		KnimeTuple tuple = new KnimeTuple(SchemaFactory.createDataSchema());

		PmmXmlDoc dataInfo = new PmmXmlDoc();

		dataInfo.add(new MdInfoXml(null, null, comment, null, null));

		tuple.setValue(TimeSeriesSchema.ATT_CONDID, id);
		tuple.setValue(TimeSeriesSchema.ATT_AGENT, agentXml);
		tuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixXml);
		tuple.setValue(TimeSeriesSchema.ATT_MDINFO, dataInfo);
		tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES, timeSeriesXml);
		tuple.setValue(TimeSeriesSchema.ATT_MISC, miscXML);
		tuple.setValue(TimeSeriesSchema.ATT_LITMD, literatureXML);

		container.addRowToTable(tuple);
		exec.setProgress(1, "Adding row 0");
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
		return new DataTableSpec[] { SchemaFactory.createDataSchema()
				.createSpec() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		settings.addString(CFGKEY_LITERATURE,
				XmlConverter.listToXml(literature));
		settings.addString(CFGKEY_AGENT, XmlConverter.agentToXml(agent));
		settings.addString(CFGKEY_MATRIX, XmlConverter.matrixToXml(matrix));
		settings.addString(CFGKEY_COMMENT, comment);
		settings.addString(CFGKEY_TIMESERIES,
				XmlConverter.listToXml(timeSeries));
		settings.addString(CFGKEY_TIMEUNIT, timeUnit);
		settings.addString(CFGKEY_LOGCUNIT, logcUnit);
		settings.addString(CFGKEY_MISC, XmlConverter.listToXml(misc));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		literature = XmlConverter.xmlToLiteratureList(settings
				.getString(CFGKEY_LITERATURE));
		agent = XmlConverter.xmlToAgent(settings.getString(CFGKEY_AGENT));
		matrix = XmlConverter.xmlToMatrix(settings.getString(CFGKEY_MATRIX));
		comment = settings.getString(CFGKEY_COMMENT);
		timeSeries = XmlConverter.xmlToTimeSeriesList(settings
				.getString(CFGKEY_TIMESERIES));
		timeUnit = settings.getString(CFGKEY_TIMEUNIT);
		logcUnit = settings.getString(CFGKEY_LOGCUNIT);
		misc = XmlConverter.xmlToMiscList(settings.getString(CFGKEY_MISC));
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
