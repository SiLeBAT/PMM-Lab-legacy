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
package de.bund.bfr.knime.pmm.dataedit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.IntCell;
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

import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeAttribute;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeRelationReader;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

/**
 * This is the model implementation of DataEdit.
 * 
 * 
 * @author Christian Thoens, Joergen Brandt
 */
public class DataEditNodeModel extends NodeModel {

	static final String CFG_DATACHANGES = "DataChanges";
	static final String DELETED = "Deleted";

	private KnimeSchema schema;
	private List<String> dataChanges;

	/**
	 * Constructor for the node model.
	 */
	protected DataEditNodeModel() {
		super(1, 2);
		schema = new TimeSeriesSchema();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {
		KnimeRelationReader reader = new KnimeRelationReader(schema, inData[0]);
		Map<Integer, List<String>> changeMap = new HashMap<Integer, List<String>>();
		List<KnimeTuple> tuples = new ArrayList<KnimeTuple>();

		for (String c : dataChanges) {
			int i = c.indexOf(":");
			int id = Integer.parseInt(c.substring(0, i));
			String change = c.substring(i + 1);

			if (!changeMap.containsKey(id)) {
				changeMap.put(id, new ArrayList<String>());
			}

			changeMap.get(id).add(change);
		}

		while (reader.hasMoreElements()) {
			tuples.add(reader.nextElement());
		}

		List<KnimeTuple> newTuples = performChanges(tuples, changeMap);
		BufferedDataContainer container = exec.createDataContainer(schema
				.createSpec());
		int index = 0;

		for (KnimeTuple tuple : newTuples) {
			container.addRowToTable(tuple);
			exec.setProgress((double) index / (double) inData[0].getRowCount(),
					"");
			index++;
		}

		container.close();

		BufferedDataContainer container2 = exec
				.createDataContainer(getLogFileSpec());
		int i = 0;

		for (int id : changeMap.keySet()) {
			for (String change : changeMap.get(id)) {

				container2.addRowToTable(new DefaultRow(i++ + "",
						new DataCell[] { new IntCell(id),
								new StringCell(change) }));
			}
		}

		container2.close();

		return new BufferedDataTable[] { container.getTable(),
				container2.getTable() };
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
		try {
			if (!schema.conforms((DataTableSpec) inSpecs[0])) {
				throw new InvalidSettingsException("Wrong input!");
			}

			return new DataTableSpec[] { schema.createSpec(), getLogFileSpec() };
		} catch (PmmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {
		if (dataChanges != null) {
			settings.addStringArray(CFG_DATACHANGES,
					dataChanges.toArray(new String[0]));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		try {
			dataChanges = new ArrayList<String>(Arrays.asList(settings
					.getStringArray(CFG_DATACHANGES)));
		} catch (InvalidSettingsException e) {
			dataChanges = new ArrayList<String>();
		}
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

	protected static List<KnimeTuple> performChanges(List<KnimeTuple> tuples,
			Map<Integer, List<String>> changes) {
		List<KnimeTuple> newTuples = new ArrayList<KnimeTuple>();

		for (KnimeTuple tuple : tuples) {
			try {
				int id = tuple.getInt(TimeSeriesSchema.ATT_CONDID);

				if (changes.containsKey(id)) {
					List<String> tupleChanges = changes.get(id);

					if (tupleChanges.size() == 1
							&& tupleChanges.get(0).equals(DELETED)) {
						// Do nothing
					} else {
						for (String change : tupleChanges) {

							int i = change.indexOf("->");
							String attr = change.substring(0, i);
							String value = change.substring(i + 2);
							int type = tuple.getSchema().getType(
									tuple.getSchema().getIndex(attr));

							if (!value.isEmpty()) {
								if (type == KnimeAttribute.TYPE_DOUBLE) {
									tuple.setValue(attr,
											Double.parseDouble(value));
								} else if (type == KnimeAttribute.TYPE_INT) {
									tuple.setValue(attr,
											Integer.parseInt(value));
								} else if (type == KnimeAttribute.TYPE_STRING) {
									tuple.setValue(attr, value);
								} else if (type == KnimeAttribute.TYPE_COMMASEP_DOUBLE) {
									tuple.setValue(attr,
											convertToDoubleList(value));
								}
							} else {
								tuple.setValue(attr, null);
							}
						}

						newTuples.add(tuple);
					}
				} else {
					newTuples.add(tuple);
				}
			} catch (PmmException e) {
				e.printStackTrace();
			}
		}

		return newTuples;
	}

	public static List<Double> convertToDoubleList(String s) {
		List<Double> list = new ArrayList<Double>();
		String[] toks = s.split(",");

		if (toks.length == 0) {
			return null;
		}

		for (String t : toks) {
			if (t.equals("?")) {
				list.add(null);
			} else {
				try {
					list.add(Double.parseDouble(t));
				} catch (NumberFormatException e) {
					return null;
				}
			}
		}

		return list;
	}

	public static String convertToString(List<Double> list) {
		if (list == null || list.isEmpty()) {
			return null;
		}

		String s = "";

		for (Double d : list) {
			if (d == null) {
				s += "?,";
			} else {
				if (d.isNaN() || d.isInfinite()) {
					s += "?,";
				} else {
					s += d + ",";
				}
			}
		}

		return s.substring(0, s.length() - 1);
	}

	private DataTableSpec getLogFileSpec() {
		return new DataTableSpec(new DataColumnSpecCreator(
				TimeSeriesSchema.ATT_CONDID, IntCell.TYPE).createSpec(),
				new DataColumnSpecCreator("Change", StringCell.TYPE)
						.createSpec());
	}

}
