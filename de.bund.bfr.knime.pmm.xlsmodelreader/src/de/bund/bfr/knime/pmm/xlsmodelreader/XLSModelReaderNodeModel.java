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
package de.bund.bfr.knime.pmm.xlsmodelreader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hsh.bfr.db.DBKernel;
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

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.AgentXml;
import de.bund.bfr.knime.pmm.common.CatalogModelXml;
import de.bund.bfr.knime.pmm.common.DepXml;
import de.bund.bfr.knime.pmm.common.IndepXml;
import de.bund.bfr.knime.pmm.common.LiteratureItem;
import de.bund.bfr.knime.pmm.common.MatrixXml;
import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.XLSReader;
import de.bund.bfr.knime.pmm.common.XmlConverter;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of XLSModelReader.
 * 
 * 
 * @author Christian Thoens
 */
public class XLSModelReaderNodeModel extends NodeModel {

	protected static final String CFGKEY_FILENAME = "FileName";
	protected static final String CFGKEY_MODELMAPPINGS = "ModelMappings";
	protected static final String CFGKEY_COLUMNMAPPINGS = "ColumnMappings";
	protected static final String CFGKEY_AGENTCOLUMN = "AgentColumn";
	protected static final String CFGKEY_AGENTMAPPINGS = "AgentMappings";
	protected static final String CFGKEY_MATRIXCOLUMN = "MatrixColumn";
	protected static final String CFGKEY_MATRIXMAPPINGS = "MatrixMappings";
	protected static final String CFGKEY_TEMPUNIT = "TempUnit";
	protected static final String CFGKEY_MODELID = "ModelID";
	protected static final String CFGKEY_AGENTID = "AgentID";
	protected static final String CFGKEY_MATRIXID = "MatrixID";
	protected static final String CFGKEY_LITERATUREIDS = "LiteratureIDs";

	private String fileName;
	private Map<String, String> modelMappings;
	private Map<String, String> columnMappings;
	private String agentColumn;
	private Map<String, String> agentMappings;
	private String matrixColumn;
	private Map<String, String> matrixMappings;
	private String tempUnit;
	private int modelID;
	private int agentID;
	private int matrixID;
	private List<Integer> literatureIDs;

	private KnimeSchema schema;

	/**
	 * Constructor for the node model.
	 */
	protected XLSModelReaderNodeModel() {
		super(0, 1);
		fileName = null;
		modelID = -1;
		modelMappings = new LinkedHashMap<>();
		columnMappings = new LinkedHashMap<>();
		agentColumn = null;
		agentMappings = new LinkedHashMap<>();
		matrixColumn = null;
		matrixMappings = new LinkedHashMap<>();
		tempUnit = AttributeUtilities
				.getStandardUnit(AttributeUtilities.ATT_TEMPERATURE);
		agentID = -1;
		matrixID = -1;
		literatureIDs = new ArrayList<>();

		try {
			schema = new KnimeSchema(new Model1Schema(), new TimeSeriesSchema());
		} catch (PmmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		Map<String, Object> cMappings = new LinkedHashMap<>();
		Map<String, AgentXml> aMappings = new LinkedHashMap<>();
		Map<String, MatrixXml> mMappings = new LinkedHashMap<>();

		for (String column : columnMappings.keySet()) {
			String id = columnMappings.get(column);

			if (id.equals(TimeSeriesSchema.ATT_COMMENT)) {
				cMappings.put(column, id);
			} else if (id.equals(AttributeUtilities.ATT_TEMPERATURE_ID + "")) {
				cMappings.put(column, new MiscXml(
						AttributeUtilities.ATT_TEMPERATURE_ID,
						AttributeUtilities.ATT_TEMPERATURE, null, null, null));
			} else if (id.equals(AttributeUtilities.ATT_PH_ID + "")) {
				cMappings.put(column, new MiscXml(AttributeUtilities.ATT_PH_ID,
						AttributeUtilities.ATT_PH, null, null, null));
			} else if (id.equals(AttributeUtilities.ATT_AW_ID + "")) {
				cMappings
						.put(column, new MiscXml(AttributeUtilities.ATT_AW_ID,
								AttributeUtilities.ATT_WATERACTIVITY, null,
								null, null));
			} else {
				String name = DBKernel.getValue("SonstigeParameter", "ID", id,
						"Parameter") + "";

				cMappings.put(column, new MiscXml(Integer.parseInt(id), name,
						null, null, null));
			}
		}

		for (String value : agentMappings.keySet()) {
			String id = agentMappings.get(value);
			String agentName = DBKernel.getValue("Agenzien", "ID", id,
					"Agensname") + "";

			aMappings.put(value, new AgentXml(Integer.parseInt(id), agentName,
					null));
		}

		for (String value : matrixMappings.keySet()) {
			String id = matrixMappings.get(value);
			String matrixName = DBKernel.getValue("Matrices", "ID", id,
					"Matrixname") + "";

			mMappings.put(value, new MatrixXml(Integer.parseInt(id),
					matrixName, null));
		}

		Bfrdb db = new Bfrdb(DBKernel.getLocalConn(true));
		KnimeTuple modelTuple = db.getPrimModelById(modelID);
		PmmXmlDoc modelXml = modelTuple
				.getPmmXml(Model1Schema.ATT_MODELCATALOG);
		String formula = ((CatalogModelXml) modelXml.get(0)).getFormula();
		PmmXmlDoc depVar = modelTuple.getPmmXml(Model1Schema.ATT_DEPENDENT);
		PmmXmlDoc indepVar = modelTuple.getPmmXml(Model1Schema.ATT_INDEPENDENT);

		if (depVar.size() == 1) {
			formula = MathUtilities.replaceVariable(formula,
					((DepXml) depVar.get(0)).getName(), TimeSeriesSchema.LOGC);
			((DepXml) depVar.get(0)).setName(TimeSeriesSchema.LOGC);
		}

		if (indepVar.size() == 1) {
			formula = MathUtilities.replaceVariable(formula,
					((IndepXml) indepVar.get(0)).getName(),
					TimeSeriesSchema.TIME);
			((IndepXml) indepVar.get(0)).setName(TimeSeriesSchema.TIME);
		}

		((CatalogModelXml) modelXml.get(0)).setFormula(formula);
		modelTuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
		modelTuple.setValue(Model1Schema.ATT_DEPENDENT, depVar);
		modelTuple.setValue(Model1Schema.ATT_INDEPENDENT, indepVar);

		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>(XLSReader
				.getDValueTuples(new File(fileName), cMappings, agentColumn,
						aMappings, matrixColumn, mMappings, modelTuple,
						modelMappings).values());

		if (agentColumn == null) {
			PmmXmlDoc agentXml = new PmmXmlDoc();

			if (agentID != -1) {
				String agentName = DBKernel.getValue("Agenzien", "ID", agentID
						+ "", "Agensname")
						+ "";

				agentXml.add(new AgentXml(agentID, agentName, null));
			} else {
				agentXml.add(new AgentXml(null, null, null));
			}

			for (KnimeTuple tuple : tuples) {
				tuple.setValue(TimeSeriesSchema.ATT_AGENT, agentXml);
			}
		}

		if (matrixColumn == null) {
			PmmXmlDoc matrixXml = new PmmXmlDoc();

			if (matrixID != -1) {
				String matrixName = DBKernel.getValue("Matrices", "ID",
						matrixID + "", "Matrixname") + "";

				matrixXml.add(new MatrixXml(matrixID, matrixName, null));
			} else {
				matrixXml.add(new MatrixXml(null, null, null));
			}

			for (KnimeTuple tuple : tuples) {
				tuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixXml);
			}
		}

		PmmXmlDoc literatureXML = new PmmXmlDoc();

		for (int id : literatureIDs) {
			String author = DBKernel.getValue("Literatur", "ID", id + "",
					"Erstautor") + "";
			String year = DBKernel.getValue("Literatur", "ID", id + "", "Jahr")
					+ "";
			String title = DBKernel.getValue("Literatur", "ID", id + "",
					"Titel") + "";
			String mAbstract = DBKernel.getValue("Literatur", "ID", id + "",
					"Abstract") + "";

			literatureXML.add(new LiteratureItem(author,
					Integer.parseInt(year), title, mAbstract, id));
		}

		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());

		for (KnimeTuple tuple : tuples) {
			PmmXmlDoc miscXml = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);

			for (PmmXmlElementConvertable el : miscXml.getElementSet()) {
				MiscXml element = (MiscXml) el;

				if (AttributeUtilities.ATT_TEMPERATURE
						.equals(element.getName())) {
					element.setValue(AttributeUtilities.convertToStandardUnit(
							AttributeUtilities.ATT_TEMPERATURE,
							element.getValue(), tempUnit));
				}
			}

			tuple.setValue(TimeSeriesSchema.ATT_MISC, miscXml);
			tuple.setValue(TimeSeriesSchema.ATT_LITMD, literatureXML);

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
		if (fileName == null) {
			throw new InvalidSettingsException("");
		}

		return new DataTableSpec[] { schema.createSpec() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		settings.addString(CFGKEY_FILENAME, fileName);
		settings.addInt(CFGKEY_MODELID, modelID);
		settings.addString(CFGKEY_MODELMAPPINGS,
				XmlConverter.mapToXml(modelMappings));
		settings.addString(CFGKEY_COLUMNMAPPINGS,
				XmlConverter.mapToXml(columnMappings));
		settings.addString(CFGKEY_AGENTCOLUMN, agentColumn);
		settings.addString(CFGKEY_AGENTMAPPINGS,
				XmlConverter.mapToXml(agentMappings));
		settings.addString(CFGKEY_MATRIXCOLUMN, matrixColumn);
		settings.addString(CFGKEY_MATRIXMAPPINGS,
				XmlConverter.mapToXml(matrixMappings));
		settings.addString(CFGKEY_TEMPUNIT, tempUnit);
		settings.addInt(CFGKEY_AGENTID, agentID);
		settings.addInt(CFGKEY_MATRIXID, matrixID);
		settings.addString(CFGKEY_LITERATUREIDS,
				XmlConverter.listToXml(literatureIDs));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		fileName = settings.getString(CFGKEY_FILENAME);
		modelID = settings.getInt(CFGKEY_MODELID);
		modelMappings = XmlConverter.xmlToStringMap(settings
				.getString(CFGKEY_MODELMAPPINGS));
		columnMappings = XmlConverter.xmlToStringMap(settings
				.getString(CFGKEY_COLUMNMAPPINGS));
		agentColumn = settings.getString(CFGKEY_AGENTCOLUMN);
		agentMappings = XmlConverter.xmlToStringMap(settings
				.getString(CFGKEY_AGENTMAPPINGS));
		matrixColumn = settings.getString(CFGKEY_MATRIXCOLUMN);
		matrixMappings = XmlConverter.xmlToStringMap(settings
				.getString(CFGKEY_MATRIXMAPPINGS));
		tempUnit = settings.getString(CFGKEY_TEMPUNIT);
		agentID = settings.getInt(CFGKEY_AGENTID);
		matrixID = settings.getInt(CFGKEY_MATRIXID);
		literatureIDs = XmlConverter.xmlToIntList(settings
				.getString(CFGKEY_LITERATUREIDS));
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
