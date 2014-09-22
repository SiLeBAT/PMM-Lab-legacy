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
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.AttributeUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.SchemaFactory;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class XLSReader {

	public static String ID_COLUMN = "ID";
	public static String NAME_COLUMN = "Name";
	public static String CONCENTRATION_STDDEV_COLUMN = "Value StdDev";
	public static String CONCENTRATION_MEASURE_NUMBER = "Value Measurements";

	private List<String> warnings;
	private FormulaEvaluator evaluator;

	public XLSReader() {
		warnings = new ArrayList<>();
		evaluator = null;
	}

	public Map<String, KnimeTuple> getTimeSeriesTuples(File file, String sheet,
			Map<String, Object> columnMappings, String timeUnit,
			String concentrationUnit, String agentColumnName,
			Map<String, AgentXml> agentMappings, String matrixColumnName,
			Map<String, MatrixXml> matrixMappings) throws Exception {
		Workbook wb = getWorkbook(file);
		Sheet s = wb.getSheet(sheet);

		warnings.clear();
		evaluator = wb.getCreationHelper().createFormulaEvaluator();

		if (s == null) {
			throw new Exception("Sheet not found");
		}

		Map<String, KnimeTuple> tuples = new LinkedHashMap<>();
		Map<String, Integer> columns = getColumns(s);
		Map<String, Integer> miscColumns = new LinkedHashMap<>();
		Integer idColumn = null;
		Integer commentColumn = null;
		Integer timeColumn = null;
		Integer logcColumn = null;
		Integer stdDevColumn = null;
		Integer nMeasureColumn = null;
		Integer agentDetailsColumn = null;
		Integer matrixDetailsColumn = null;
		Integer agentColumn = null;
		Integer matrixColumn = null;
		String timeColumnName = null;
		String logcColumnName = null;
		String stdDevColumnName = null;
		String nMeasureColumnName = null;

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
				} else if (mapping.equals(MdInfoXml.ATT_COMMENT)) {
					commentColumn = columns.get(column);
				} else if (mapping.equals(AttributeUtilities.TIME)) {
					timeColumn = columns.get(column);
					timeColumnName = column;
				} else if (mapping.equals(AttributeUtilities.CONCENTRATION)) {
					logcColumn = columns.get(column);
					logcColumnName = column;
				} else if (mapping
						.equals(XLSReader.CONCENTRATION_STDDEV_COLUMN)) {
					stdDevColumn = columns.get(column);
					stdDevColumnName = column;
				} else if (mapping
						.equals(XLSReader.CONCENTRATION_MEASURE_NUMBER)) {
					nMeasureColumn = columns.get(column);
					nMeasureColumnName = column;
				} else if (mapping.equals(AttributeUtilities.AGENT_DETAILS)) {
					agentDetailsColumn = columns.get(column);
				} else if (mapping.equals(AttributeUtilities.MATRIX_DETAILS)) {
					matrixDetailsColumn = columns.get(column);
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
			Cell idCell = null;
			Cell commentCell = null;
			Cell timeCell = null;
			Cell logcCell = null;
			Cell stdDevCell = null;
			Cell nMeasureCell = null;
			Cell agentDetailsCell = null;
			Cell matrixDetailsCell = null;
			Cell agentCell = null;
			Cell matrixCell = null;

			if (idColumn != null) {
				idCell = row.getCell(idColumn);
			}

			if (commentColumn != null) {
				commentCell = row.getCell(commentColumn);
			}

			if (timeColumn != null) {
				timeCell = row.getCell(timeColumn);
			}

			if (logcColumn != null) {
				logcCell = row.getCell(logcColumn);
			}

			if (stdDevColumn != null) {
				stdDevCell = row.getCell(stdDevColumn);
			}

			if (nMeasureColumn != null) {
				nMeasureCell = row.getCell(nMeasureColumn);
			}

			if (agentDetailsColumn != null) {
				agentDetailsCell = row.getCell(agentDetailsColumn);
			}

			if (matrixDetailsColumn != null) {
				matrixDetailsCell = row.getCell(matrixDetailsColumn);
			}

			if (agentColumn != null) {
				agentCell = row.getCell(agentColumn);
			}

			if (matrixColumn != null) {
				matrixCell = row.getCell(matrixColumn);
			}

			if (hasData(idCell) && !getData(idCell).equals(id)) {
				if (tuple != null) {
					tuple.setValue(TimeSeriesSchema.ATT_TIMESERIES,
							timeSeriesXml);
					tuples.put(id, tuple);
				}

				id = getData(idCell);
				tuple = new KnimeTuple(SchemaFactory.createDataSchema());
				tuple.setValue(TimeSeriesSchema.ATT_COMBASEID, id);
				tuple.setValue(TimeSeriesSchema.ATT_CONDID,
						MathUtilities.getRandomNegativeInt());
				timeSeriesXml = new PmmXmlDoc();

				PmmXmlDoc dataInfo = new PmmXmlDoc();
				PmmXmlDoc agentXml = new PmmXmlDoc();
				PmmXmlDoc matrixXml = new PmmXmlDoc();

				if (commentCell != null) {
					dataInfo.add(new MdInfoXml(null, null,
							getData(commentCell), null, null));
				} else {
					dataInfo.add(new MdInfoXml(null, null, null, null, null));
				}

				if (hasData(agentCell)
						&& agentMappings.get(getData(agentCell)) != null) {
					agentXml.add(agentMappings.get(getData(agentCell)));
				} else {
					agentXml.add(new AgentXml());
				}

				if (hasData(matrixCell)
						&& matrixMappings.get(getData(matrixCell)) != null) {
					matrixXml.add(matrixMappings.get(getData(matrixCell)));
				} else {
					matrixXml.add(new MatrixXml());
				}

				if (hasData(agentDetailsCell)) {
					((AgentXml) agentXml.get(0))
							.setDetail(getData(agentDetailsCell));
				}

				if (hasData(matrixDetailsCell)) {
					((MatrixXml) matrixXml.get(0))
							.setDetail(getData(matrixDetailsCell));
				}

				tuple.setValue(TimeSeriesSchema.ATT_MDINFO, dataInfo);
				tuple.setValue(TimeSeriesSchema.ATT_AGENT, agentXml);
				tuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixXml);

				PmmXmlDoc miscXML = new PmmXmlDoc();

				for (String column : miscColumns.keySet()) {
					MiscXml misc = (MiscXml) columnMappings.get(column);
					Cell cell = row.getCell(miscColumns.get(column));

					if (hasData(cell)) {
						try {
							misc.setValue(Double.parseDouble(getData(cell)
									.replace(",", ".")));
						} catch (NumberFormatException e) {
							warnings.add(column + " value in row " + (i + 1)
									+ " is not valid (" + getData(cell) + ")");
							misc.setValue(null);
						}
					} else {
						misc.setValue(null);
					}

					misc.setOrigUnit(misc.getUnit());
					miscXML.add(misc);
				}

				tuple.setValue(TimeSeriesSchema.ATT_MISC, miscXML);
			}

			if (tuple != null) {
				Double time = null;
				Double logc = null;
				Double stdDev = null;
				Integer nMeasure = null;

				if (hasData(timeCell)) {
					try {
						time = Double.parseDouble(getData(timeCell).replace(
								",", "."));
					} catch (NumberFormatException e) {
						warnings.add(timeColumnName + " value in row "
								+ (i + 1) + " is not valid ("
								+ getData(timeCell) + ")");
					}
				}

				if (hasData(logcCell)) {
					try {
						logc = Double.parseDouble(getData(logcCell).replace(
								",", "."));
					} catch (NumberFormatException e) {
						warnings.add(logcColumnName + " value in row "
								+ (i + 1) + " is not valid ("
								+ getData(logcCell) + ")");
					}
				}

				if (hasData(stdDevCell)) {
					try {
						stdDev = Double.parseDouble(getData(stdDevCell)
								.replace(",", "."));
					} catch (NumberFormatException e) {
						warnings.add(stdDevColumnName + " value in row "
								+ (i + 1) + " is not valid ("
								+ getData(stdDevCell) + ")");
					}
				}

				if (hasData(nMeasureCell)) {
					try {
						String number = getData(nMeasureCell).replace(",", ".");

						if (number.contains(".")) {
							number = number.substring(0, number.indexOf("."));
						}

						nMeasure = Integer.parseInt(number);
					} catch (NumberFormatException e) {
						warnings.add(nMeasureColumnName + " value in row "
								+ (i + 1) + " is not valid ("
								+ getData(nMeasureCell) + ")");
					}
				}

				for (String column : miscColumns.keySet()) {
					PmmXmlDoc misc = tuple.getPmmXml(TimeSeriesSchema.ATT_MISC);
					Cell cell = row.getCell(miscColumns.get(column));

					if (hasData(cell)) {
						try {
							String param = ((MiscXml) columnMappings
									.get(column)).getName();
							double value = Double.parseDouble(getData(cell)
									.replace(",", "."));

							if (!hasSameValue(param, value, misc)) {
								warnings.add("Variable conditions cannot be imported: "
										+ "Only first value for "
										+ column
										+ " is used");
							}
						} catch (NumberFormatException e) {
						}
					}
				}

				timeSeriesXml.add(new TimeSeriesXml(null, time, timeUnit, logc,
						concentrationUnit, stdDev, nMeasure));
			}
		}

		return tuples;
	}

	public Map<String, KnimeTuple> getModelTuples(File file, String sheet,
			Map<String, Object> columnMappings, String agentColumnName,
			Map<String, AgentXml> agentMappings, String matrixColumnName,
			Map<String, MatrixXml> matrixMappings, KnimeTuple modelTuple,
			Map<String, String> modelMappings, String modelDepUnit,
			String modelIndepUnit, Map<String, KnimeTuple> secModelTuples,
			Map<String, Map<String, String>> secModelMappings,
			Map<String, Map<String, String>> secModelIndepMins,
			Map<String, Map<String, String>> secModelIndepMaxs,
			Map<String, Map<String, String>> secModelIndepUnits)
			throws Exception {
		Workbook wb = getWorkbook(file);
		Sheet s = wb.getSheet(sheet);

		warnings.clear();
		evaluator = wb.getCreationHelper().createFormulaEvaluator();

		if (s == null) {
			throw new Exception("Sheet not found");
		}

		Map<String, KnimeTuple> tuples = new LinkedHashMap<>();
		Map<String, Integer> columns = getColumns(s);
		Map<String, Integer> miscColumns = new LinkedHashMap<>();
		Integer idColumn = null;
		Integer commentColumn = null;
		Integer agentDetailsColumn = null;
		Integer matrixDetailsColumn = null;
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
				} else if (mapping.equals(NAME_COLUMN)) {
					idColumn = columns.get(column);
				} else if (mapping.equals(MdInfoXml.ATT_COMMENT)) {
					commentColumn = columns.get(column);
				} else if (mapping.equals(AttributeUtilities.AGENT_DETAILS)) {
					agentDetailsColumn = columns.get(column);
				} else if (mapping.equals(AttributeUtilities.MATRIX_DETAILS)) {
					matrixDetailsColumn = columns.get(column);
				}
			}
		}

		int index = 0;

		for (int rowNumber = 1;; rowNumber++) {
			if (isEndOfFile(s, rowNumber)) {
				break;
			}

			int globalID = MathUtilities.getRandomNegativeInt();
			KnimeTuple dataTuple = new KnimeTuple(
					SchemaFactory.createDataSchema());
			Row row = s.getRow(rowNumber);
			Cell idCell = null;
			Cell commentCell = null;
			Cell agentDetailsCell = null;
			Cell matrixDetailsCell = null;
			Cell agentCell = null;
			Cell matrixCell = null;

			if (idColumn != null) {
				idCell = row.getCell(idColumn);
			}

			if (commentColumn != null) {
				commentCell = row.getCell(commentColumn);
			}

			if (agentDetailsColumn != null) {
				agentDetailsCell = row.getCell(agentDetailsColumn);
			}

			if (matrixDetailsColumn != null) {
				matrixDetailsCell = row.getCell(matrixDetailsColumn);
			}

			if (agentColumn != null) {
				agentCell = row.getCell(agentColumn);
			}

			if (matrixColumn != null) {
				matrixCell = row.getCell(matrixColumn);
			}

			dataTuple.setValue(TimeSeriesSchema.ATT_CONDID,
					MathUtilities.getRandomNegativeInt());

			PmmXmlDoc dataInfo = new PmmXmlDoc();
			PmmXmlDoc agentXml = new PmmXmlDoc();
			PmmXmlDoc matrixXml = new PmmXmlDoc();

			if (hasData(commentCell)) {
				dataInfo.add(new MdInfoXml(null, null, getData(commentCell),
						null, null));
			} else {
				dataInfo.add(new MdInfoXml(null, null, null, null, null));

				if (commentColumn != null) {
					warnings.add(MdInfoXml.ATT_COMMENT + " value in row "
							+ (rowNumber + 1) + " is missing");
				}
			}

			if (hasData(agentCell)
					&& agentMappings.get(getData(agentCell)) != null) {
				agentXml.add(new AgentXml(agentMappings.get(getData(agentCell))));
			} else {
				agentXml.add(new AgentXml());

				if (agentColumn != null) {
					warnings.add(TimeSeriesSchema.ATT_AGENT + " value in row "
							+ (rowNumber + 1) + " is missing");
				}
			}

			if (hasData(matrixCell)
					&& matrixMappings.get(getData(matrixCell)) != null) {
				matrixXml.add(new MatrixXml(matrixMappings
						.get(getData(matrixCell))));
			} else {
				matrixXml.add(new MatrixXml());

				if (matrixColumn != null) {
					warnings.add(TimeSeriesSchema.ATT_MATRIX + " value in row "
							+ (rowNumber + 1) + " is missing");
				}
			}

			if (hasData(agentDetailsCell)) {
				((AgentXml) agentXml.get(0))
						.setDetail(getData(agentDetailsCell));
			} else if (agentDetailsColumn != null) {
				warnings.add(AttributeUtilities.AGENT_DETAILS
						+ " value in row " + (rowNumber + 1) + " is missing");
			}

			if (hasData(matrixDetailsCell)) {
				((MatrixXml) matrixXml.get(0))
						.setDetail(getData(matrixDetailsCell));
			} else if (matrixDetailsColumn != null) {
				warnings.add(AttributeUtilities.MATRIX_DETAILS
						+ " value in row " + (rowNumber + 1) + " is missing");
			}

			dataTuple.setValue(TimeSeriesSchema.ATT_MDINFO, dataInfo);
			dataTuple.setValue(TimeSeriesSchema.ATT_AGENT, agentXml);
			dataTuple.setValue(TimeSeriesSchema.ATT_MATRIX, matrixXml);

			PmmXmlDoc miscXML = new PmmXmlDoc();

			for (String column : miscColumns.keySet()) {
				MiscXml misc = new MiscXml((MiscXml) columnMappings.get(column));
				Cell cell = row.getCell(miscColumns.get(column));

				if (hasData(cell)) {
					try {
						misc.setValue(Double.parseDouble(getData(cell).replace(
								",", ".")));
					} catch (NumberFormatException e) {
						warnings.add(column + " value in row "
								+ (rowNumber + 1) + " is not valid ("
								+ getData(cell) + ")");
					}
				} else {
					warnings.add(column + " value in row " + (rowNumber + 1)
							+ " is missing");
				}

				misc.setOrigUnit(misc.getUnit());
				miscXML.add(misc);
			}

			dataTuple.setValue(TimeSeriesSchema.ATT_MISC, miscXML);

			PmmXmlDoc modelXml = modelTuple
					.getPmmXml(Model1Schema.ATT_MODELCATALOG);
			PmmXmlDoc paramXml = modelTuple
					.getPmmXml(Model1Schema.ATT_PARAMETER);
			PmmXmlDoc estXml = modelTuple.getPmmXml(Model1Schema.ATT_ESTMODEL);
			PmmXmlDoc depXml = modelTuple.getPmmXml(Model1Schema.ATT_DEPENDENT);
			PmmXmlDoc indepXml = modelTuple
					.getPmmXml(Model1Schema.ATT_INDEPENDENT);

			if (modelDepUnit != null
					&& !modelDepUnit.equals(((DepXml) depXml.get(0)).getUnit())) {
				((DepXml) depXml.get(0)).setUnit(modelDepUnit);
				((CatalogModelXml) modelXml.get(0)).setId(MathUtilities
						.getRandomNegativeInt());
			}

			if (modelIndepUnit != null
					&& !modelIndepUnit.equals(((IndepXml) indepXml.get(0))
							.getUnit())) {
				((IndepXml) indepXml.get(0)).setUnit(modelIndepUnit);
				((CatalogModelXml) modelXml.get(0)).setId(MathUtilities
						.getRandomNegativeInt());
			}

			((EstModelXml) estXml.get(0)).setId(MathUtilities
					.getRandomNegativeInt());
			((EstModelXml) estXml.get(0)).setComment(getData(commentCell));

			if (hasData(idCell)) {
				((EstModelXml) estXml.get(0)).setName(getData(idCell));
			}

			for (PmmXmlElementConvertable el : paramXml.getElementSet()) {
				ParamXml element = (ParamXml) el;
				String mapping = modelMappings.get(element.getName());

				if (mapping != null) {
					int column = columns.get(mapping);
					Cell cell = row.getCell(column);

					if (hasData(cell)) {
						try {
							element.setValue(Double.parseDouble(getData(cell)
									.replace(",", ".")));
						} catch (NumberFormatException e) {
							warnings.add(mapping + " value in row "
									+ (rowNumber + 1) + " is not valid ("
									+ getData(cell) + ")");
						}
					} else {
						warnings.add(mapping + " value in row "
								+ (rowNumber + 1) + " is missing");
					}
				}
			}

			modelTuple.setValue(Model1Schema.ATT_DEPENDENT, depXml);
			modelTuple.setValue(Model1Schema.ATT_INDEPENDENT, indepXml);
			modelTuple.setValue(Model1Schema.ATT_MODELCATALOG, modelXml);
			modelTuple.setValue(Model1Schema.ATT_PARAMETER, paramXml);
			modelTuple.setValue(Model1Schema.ATT_ESTMODEL, estXml);

			if (secModelTuples.isEmpty()) {
				tuples.put(index + "",
						new KnimeTuple(SchemaFactory.createM1DataSchema(),
								modelTuple, dataTuple));
				index++;
			} else {
				for (String param : secModelTuples.keySet()) {
					KnimeTuple secTuple = secModelTuples.get(param);
					PmmXmlDoc secParamXml = secTuple
							.getPmmXml(Model2Schema.ATT_PARAMETER);
					PmmXmlDoc secDepXml = secTuple
							.getPmmXml(Model2Schema.ATT_DEPENDENT);
					PmmXmlDoc secEstXml = secTuple
							.getPmmXml(Model2Schema.ATT_ESTMODEL);
					PmmXmlDoc secModelXml = secTuple
							.getPmmXml(Model2Schema.ATT_MODELCATALOG);
					PmmXmlDoc secIndepXml = secTuple
							.getPmmXml(Model2Schema.ATT_INDEPENDENT);
					String formula = ((CatalogModelXml) secModelXml.get(0))
							.getFormula();

					formula = MathUtilities.replaceVariable(formula,
							((DepXml) secDepXml.get(0)).getName(), param);
					((CatalogModelXml) secModelXml.get(0)).setFormula(formula);
					((DepXml) secDepXml.get(0)).setName(param);
					((EstModelXml) secEstXml.get(0)).setId(MathUtilities
							.getRandomNegativeInt());

					for (PmmXmlElementConvertable el : secParamXml
							.getElementSet()) {
						ParamXml element = (ParamXml) el;
						String mapping = secModelMappings.get(param).get(
								element.getName());

						if (mapping != null) {
							Cell cell = row.getCell(columns.get(mapping));

							if (hasData(cell)) {
								try {
									element.setValue(Double
											.parseDouble(getData(cell).replace(
													",", ".")));
								} catch (NumberFormatException e) {
									warnings.add(mapping + " value in row "
											+ (rowNumber + 1)
											+ " is not valid (" + getData(cell)
											+ ")");
								}
							} else {
								warnings.add(mapping + " value in row "
										+ (rowNumber + 1) + " is missing");
							}
						}
					}

					for (PmmXmlElementConvertable el : secIndepXml
							.getElementSet()) {
						IndepXml element = (IndepXml) el;
						String unit = secModelIndepUnits.get(param).get(
								element.getName());

						if (unit == null) {
							continue;
						}

						if (!unit.equals(element.getUnit())) {
							element.setUnit(unit);
							((CatalogModelXml) secModelXml.get(0))
									.setId(MathUtilities.getRandomNegativeInt());
						}

						String minMapping = secModelIndepMins.get(param).get(
								element.getName());
						String maxMapping = secModelIndepMaxs.get(param).get(
								element.getName());

						if (minMapping != null) {
							Cell minCell = row.getCell(columns.get(minMapping));

							if (hasData(minCell)) {
								try {
									element.setMin(Double.parseDouble(getData(
											minCell).replace(",", ".")));
								} catch (NumberFormatException e) {
									warnings.add(minMapping + " value in row "
											+ (rowNumber + 1)
											+ " is not valid ("
											+ getData(minCell) + ")");
								}
							} else {
								warnings.add(minMapping + " value in row "
										+ (rowNumber + 1) + " is missing");
							}
						}

						if (maxMapping != null) {
							Cell maxCell = row.getCell(columns.get(maxMapping));

							if (hasData(maxCell)) {
								try {
									element.setMax(Double.parseDouble(getData(
											maxCell).replace(",", ".")));
								} catch (NumberFormatException e) {
									warnings.add(maxMapping + " value in row "
											+ (rowNumber + 1)
											+ " is not valid ("
											+ getData(maxCell) + ")");
								}
							} else {
								warnings.add(maxMapping + " value in row "
										+ (rowNumber + 1) + " is missing");
							}
						}
					}

					secTuple.setValue(Model2Schema.ATT_MODELCATALOG,
							secModelXml);
					secTuple.setValue(Model2Schema.ATT_PARAMETER, secParamXml);
					secTuple.setValue(Model2Schema.ATT_DEPENDENT, secDepXml);
					secTuple.setValue(Model2Schema.ATT_ESTMODEL, secEstXml);
					secTuple.setValue(Model2Schema.ATT_INDEPENDENT, secIndepXml);
					secTuple.setValue(Model2Schema.ATT_GLOBAL_MODEL_ID,
							globalID);

					tuples.put(
							index + "",
							new KnimeTuple(SchemaFactory.createM12DataSchema(),
									new KnimeTuple(SchemaFactory
											.createM1DataSchema(), modelTuple,
											dataTuple), secTuple));
					index++;
				}
			}
		}

		return tuples;
	}

	public List<String> getWarnings() {
		return warnings;
	}

	public List<String> getSheets(File file) throws Exception {
		List<String> sheets = new ArrayList<>();
		Workbook workbook = getWorkbook(file);

		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			sheets.add(workbook.getSheetName(i));
		}

		return sheets;
	}

	public List<String> getColumns(File file, String sheet) throws Exception {
		Workbook wb = getWorkbook(file);
		Sheet s = wb.getSheet(sheet);

		evaluator = wb.getCreationHelper().createFormulaEvaluator();

		if (s == null) {
			throw new Exception("Sheet not found");
		}

		return new ArrayList<>(getColumns(s).keySet());
	}

	public Set<String> getValuesInColumn(File file, String sheet, String column)
			throws Exception {
		Set<String> valueSet = new LinkedHashSet<>();
		Workbook wb = getWorkbook(file);
		Sheet s = wb.getSheet(sheet);

		evaluator = wb.getCreationHelper().createFormulaEvaluator();

		if (s == null) {
			throw new Exception("Sheet not found");
		}

		Map<String, Integer> columns = getColumns(s);
		int columnId = columns.get(column);

		for (int i = 1; i <= s.getLastRowNum(); i++) {
			if (s.getRow(i) != null) {
				Cell cell = s.getRow(i).getCell(columnId);

				if (hasData(cell)) {
					valueSet.add(getData(cell));
				}
			}
		}

		return valueSet;
	}

	public List<Integer> getMissingData(File file, String sheet, String column)
			throws Exception {
		List<Integer> missing = new ArrayList<>();
		Workbook wb = getWorkbook(file);
		Sheet s = wb.getSheet(sheet);

		evaluator = wb.getCreationHelper().createFormulaEvaluator();

		Map<String, Integer> columns = getColumns(s);
		int columnId = columns.get(column);

		for (int i = 1; i <= s.getLastRowNum(); i++) {
			if (s.getRow(i) != null && !hasData(s.getRow(i).getCell(columnId))) {
				missing.add(i + 1);
			}
		}

		return missing;
	}

	private Workbook getWorkbook(File file) throws Exception {
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

	private Map<String, Integer> getColumns(Sheet sheet) {
		Map<String, Integer> columns = new LinkedHashMap<>();

		for (int i = 0;; i++) {
			Cell cell = sheet.getRow(0).getCell(i);

			if (!hasData(cell)) {
				break;
			}

			columns.put(getData(cell), i);
		}

		return columns;
	}

	private boolean isEndOfFile(Sheet sheet, int i) {
		Row row = sheet.getRow(i);

		if (row == null) {
			return true;
		}

		for (int j = 0;; j++) {
			Cell headerCell = sheet.getRow(0).getCell(j);
			Cell cell = sheet.getRow(i).getCell(j);

			if (!hasData(headerCell)) {
				return true;
			}

			if (hasData(cell)) {
				return false;
			}
		}
	}

	private boolean hasData(Cell cell) {
		return cell != null && !cell.toString().trim().isEmpty();
	}

	private String getData(Cell cell) {
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
				CellValue value = evaluator.evaluate(cell);

				switch (value.getCellType()) {
				case Cell.CELL_TYPE_BOOLEAN:
					return value.getBooleanValue() + "";
				case Cell.CELL_TYPE_NUMERIC:
					return value.getNumberValue() + "";
				case Cell.CELL_TYPE_STRING:
					return value.getStringValue();
				default:
					return "";
				}
			} else {
				return cell.toString().trim();
			}
		}

		return null;
	}

	private boolean hasSameValue(String param, Double value, PmmXmlDoc miscs) {
		for (PmmXmlElementConvertable el : miscs.getElementSet()) {
			MiscXml misc = (MiscXml) el;

			if (misc.getName().equals(param) && !value.equals(misc.getValue())) {
				return false;
			}
		}

		return true;
	}
}
