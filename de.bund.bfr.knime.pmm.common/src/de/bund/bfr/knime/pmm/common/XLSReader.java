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
package de.bund.bfr.knime.pmm.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeSchema;
import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class XLSReader {

	public static String ID_COLUMN = "ID";

	private XLSReader() {
	}

	public static Map<String, KnimeTuple> getTimeSeriesTuples(File file,
			String sheet, Map<String, Object> columnMappings,
			String agentColumnName, Map<String, AgentXml> agentMappings,
			String matrixColumnName, Map<String, MatrixXml> matrixMappings)
			throws Exception {
		Sheet s = getWorkbook(file).getSheet(sheet);
		Map<String, KnimeTuple> tuples = new LinkedHashMap<String, KnimeTuple>();
		Map<String, Integer> columns = getColumns(s);
		Map<String, Integer> miscColumns = new LinkedHashMap<>();
		Integer idColumn = null;
		Integer commentColumn = null;
		Integer timeColumn = null;
		Integer logcColumn = null;
		Integer agentColumn = null;
		Integer matrixColumn = null;

		if (agentColumnName != null) {
			agentColumn = columns.get(agentColumnName);
		}

		if (matrixColumnName != null) {
			matrixColumn = columns.get(matrixColumnName);
		}

		for (String column : columns.keySet()) {
			if (columnMappings.containsKey(column)) {
				Object mapping = columnMappings.get(column);

				if (mapping instanceof MiscXml) {
					miscColumns.put(column, columns.get(column));
				} else if (mapping.equals(ID_COLUMN)) {
					idColumn = columns.get(column);
				} else if (mapping.equals(TimeSeriesSchema.ATT_COMMENT)) {
					commentColumn = columns.get(column);
				} else if (mapping.equals(TimeSeriesSchema.TIME)) {
					timeColumn = columns.get(column);
				} else if (mapping.equals(TimeSeriesSchema.LOGC)) {
					logcColumn = columns.get(column);
				}
			}
		}

		KnimeTuple tuple = null;
		PmmXmlDoc timeSeriesXml = null;
		String id = null;

		for (int i = 1;; i++) {
			if (isEndOfFile(s, i)) {
				if (tuple != null) {
					tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES,
							timeSeriesXml);
					tuples.put(id, tuple);
				}

				break;
			}

			Row row = s.getRow(i);
			Cell idCell = row.getCell(idColumn);
			Cell commentCell = row.getCell(commentColumn);
			Cell timeCell = row.getCell(timeColumn);
			Cell logcCell = row.getCell(logcColumn);
			Cell agentCell = null;
			Cell matrixCell = null;

			if (agentColumn != null) {
				agentCell = row.getCell(agentColumn);
			}

			if (matrixColumn != null) {
				matrixCell = row.getCell(matrixColumn);
			}

			if (idCell != null && !idCell.toString().trim().isEmpty()
					&& !idCell.toString().trim().equals(id)) {
				if (tuple != null) {
					tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES,
							timeSeriesXml);
					tuples.put(id, tuple);
				}

				id = idCell.toString().trim();
				tuple = new KnimeTuple(new TimeSeriesSchema());
				tuple.setValue(TimeSeriesSchema.ATT_CONDID,
						MathUtilities.getRandomNegativeInt());
				timeSeriesXml = new PmmXmlDoc();

				if (commentCell != null) {
					tuple.setValue(TimeSeriesSchema.ATT_COMMENT, commentCell
							.toString().trim());
				}

				if (agentCell != null) {
					AgentXml agent = agentMappings.get(agentCell.toString()
							.trim());

					if (agent != null) {
						PmmXmlDoc agentXml = new PmmXmlDoc();

						agentXml.add(agent);
						tuple.setValue(TimeSeriesSchema.ATT_AGENT, agentXml);
					}
				}

				if (matrixCell != null) {
					MatrixXml matrix = matrixMappings.get(matrixCell.toString()
							.trim());

					if (matrix != null) {
						PmmXmlDoc matrixXml = new PmmXmlDoc();

						matrixXml.add(matrix);
						tuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixXml);
					}
				}

				PmmXmlDoc miscXML = new PmmXmlDoc();

				for (String column : miscColumns.keySet()) {
					MiscXml misc = (MiscXml) columnMappings.get(column);
					Cell cell = row.getCell(miscColumns.get(column));

					try {
						misc.setValue(Double.parseDouble(cell.toString()
								.replace(",", ".")));
					} catch (Exception e) {
						misc.setValue(null);
					}

					miscXML.add(misc);
				}

				tuple.setValue(TimeSeriesSchema.ATT_MISC, miscXML);
			}

			if (tuple != null) {
				Double time = null;
				Double logc = null;

				if (timeCell != null && !timeCell.toString().trim().isEmpty()) {
					try {
						time = Double.parseDouble(timeCell.toString().replace(
								",", "."));
					} catch (NumberFormatException e) {
						throw new Exception(TimeSeriesSchema.TIME
								+ " value in row " + (i + 1) + " is not valid");
					}
				}

				if (logcCell != null && !logcCell.toString().trim().isEmpty()) {
					try {
						logc = Double.parseDouble(logcCell.toString().replace(
								",", "."));
					} catch (NumberFormatException e) {
						throw new Exception(TimeSeriesSchema.LOGC
								+ " value in row " + (i + 1) + " is not valid");
					}
				}

				timeSeriesXml.add(new TimeSeriesXml(null, time, logc));
			}
		}

		return tuples;
	}

	public static Map<String, KnimeTuple> getDValueTuples(File file,
			String sheet, Map<String, Object> columnMappings,
			String agentColumnName, Map<String, AgentXml> agentMappings,
			String matrixColumnName, Map<String, MatrixXml> matrixMappings,
			KnimeTuple modelTuple, Map<String, String> modelMappings)
			throws Exception {
		Sheet s = getWorkbook(file).getSheet(sheet);
		Map<String, KnimeTuple> tuples = new LinkedHashMap<String, KnimeTuple>();
		Map<String, Integer> columns = getColumns(s);
		Map<String, Integer> miscColumns = new LinkedHashMap<>();
		Integer commentColumn = null;
		Integer agentColumn = null;
		Integer matrixColumn = null;

		if (agentColumnName != null) {
			agentColumn = columns.get(agentColumnName);
		}

		if (matrixColumnName != null) {
			matrixColumn = columns.get(matrixColumnName);
		}

		for (String column : columns.keySet()) {
			if (columnMappings.containsKey(column)) {
				Object mapping = columnMappings.get(column);

				if (mapping instanceof MiscXml) {
					miscColumns.put(column, columns.get(column));
				} else if (mapping.equals(TimeSeriesSchema.ATT_COMMENT)) {
					commentColumn = columns.get(column);
				}
			}
		}

		for (int i = 1;; i++) {
			if (isEndOfFile(s, i)) {
				break;
			}

			KnimeTuple dataTuple = new KnimeTuple(new TimeSeriesSchema());
			Row row = s.getRow(i);
			Cell commentCell = row.getCell(commentColumn);
			Cell agentCell = null;
			Cell matrixCell = null;

			if (agentColumn != null) {
				agentCell = row.getCell(agentColumn);
			}

			if (matrixColumn != null) {
				matrixCell = row.getCell(matrixColumn);
			}

			dataTuple.setValue(TimeSeriesSchema.ATT_CONDID,
					MathUtilities.getRandomNegativeInt());

			if (commentCell != null) {
				dataTuple.setValue(TimeSeriesSchema.ATT_COMMENT, commentCell
						.toString().trim());
			}

			if (agentCell != null) {
				AgentXml agent = agentMappings.get(agentCell.toString().trim());

				if (agent != null) {
					PmmXmlDoc agentXml = new PmmXmlDoc();

					agentXml.add(agent);
					dataTuple.setValue(TimeSeriesSchema.ATT_AGENT, agentXml);
				}
			}

			if (matrixCell != null) {
				MatrixXml matrix = matrixMappings.get(matrixCell.toString()
						.trim());

				if (matrix != null) {
					PmmXmlDoc matrixXml = new PmmXmlDoc();

					matrixXml.add(matrix);
					dataTuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixXml);
				}
			}

			PmmXmlDoc miscXML = new PmmXmlDoc();

			for (String column : miscColumns.keySet()) {
				MiscXml misc = (MiscXml) columnMappings.get(column);
				Cell cell = row.getCell(miscColumns.get(column));

				try {
					misc.setValue(Double.parseDouble(cell.toString().replace(
							",", ".")));
				} catch (Exception e) {
					misc.setValue(null);
				}

				miscXML.add(misc);
			}

			dataTuple.setValue(TimeSeriesSchema.ATT_MISC, miscXML);

			PmmXmlDoc paramXml = modelTuple
					.getPmmXml(Model1Schema.ATT_PARAMETER);

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml element = (ParamXml) el;
				String columnName = modelMappings.get(element.getName());
				int column = columns.get(columnName);

				try {
					element.setValue(Double.parseDouble(row.getCell(column)
							.toString()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			modelTuple.setValue(Model1Schema.ATT_PARAMETER, paramXml);

			KnimeTuple tuple = new KnimeTuple(new KnimeSchema(
					new Model1Schema(), new TimeSeriesSchema()), dataTuple,
					modelTuple);

			tuples.put((i + 1) + "", tuple);
		}

		return tuples;
	}

	public static List<String> getSheets(File file) throws Exception {
		List<String> sheets = new ArrayList<>();
		Workbook workbook = getWorkbook(file);

		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			sheets.add(workbook.getSheetName(i));
		}

		return sheets;
	}

	public static List<String> getColumns(File file, String sheet)
			throws Exception {
		return new ArrayList<>(getColumns(getWorkbook(file).getSheet(sheet))
				.keySet());
	}

	public static Set<String> getValuesInColumn(File file, String sheet,
			String column) throws Exception {
		Set<String> valueSet = new LinkedHashSet<>();
		Sheet s = getWorkbook(file).getSheet(sheet);
		Map<String, Integer> columns = getColumns(s);
		int columnId = columns.get(column);

		for (int i = 1; i < s.getLastRowNum(); i++) {
			Cell cell = s.getRow(i).getCell(columnId);

			if (cell != null && !cell.toString().trim().isEmpty()) {
				valueSet.add(cell.toString().trim());
			}
		}

		return valueSet;
	}

	private static Workbook getWorkbook(File file) throws Exception {
		InputStream inputStream = null;

		if (file.exists()) {
			inputStream = new FileInputStream(file);
		} else {
			try {
				URL url = new URL(file.getPath());

				inputStream = url.openStream();
			} catch (Exception e) {
				throw new FileNotFoundException("File not found");
			}
		}

		return WorkbookFactory.create(inputStream);
	}

	private static Map<String, Integer> getColumns(Sheet sheet) {
		Map<String, Integer> columns = new LinkedHashMap<String, Integer>();

		for (int i = 0;; i++) {
			Cell cell = sheet.getRow(0).getCell(i);

			if (cell == null || cell.toString().trim().isEmpty()) {
				break;
			}

			columns.put(cell.toString().trim(), i);
		}

		return columns;
	}

	private static boolean isEndOfFile(Sheet sheet, int i) {
		Row row = sheet.getRow(i);

		if (row == null) {
			return true;
		}

		for (int j = 0;; j++) {
			Cell headerCell = sheet.getRow(0).getCell(j);
			Cell cell = sheet.getRow(i).getCell(j);

			if (headerCell == null || headerCell.toString().trim().isEmpty()) {
				return true;
			}

			if (cell != null && !cell.toString().trim().isEmpty()) {
				return false;
			}
		}
	}

}
