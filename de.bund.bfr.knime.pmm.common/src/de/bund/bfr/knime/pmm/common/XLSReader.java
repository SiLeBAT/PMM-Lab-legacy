package de.bund.bfr.knime.pmm.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import de.bund.bfr.knime.pmm.common.generictablemodel.KnimeTuple;
import de.bund.bfr.knime.pmm.common.math.MathUtilities;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.TimeSeriesSchema;

public class XLSReader {

	private XLSReader() {
	}

	public static Map<String, KnimeTuple> getTuples(File file) throws Exception {
		Map<String, KnimeTuple> tuples = new LinkedHashMap<String, KnimeTuple>();
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
		Workbook wb = WorkbookFactory.create(inputStream);
		Sheet sheet = wb.getSheetAt(0);
		List<String> miscNames = getAllMiscNames(sheet);

		KnimeTuple tuple = null;
		String id = null;

		for (int i = 1;; i++) {
			boolean endOfFile = sheet.getRow(i) == null;

			endOfFile = endOfFile || sheet.getRow(i).getCell(4) == null;
			endOfFile = endOfFile || sheet.getRow(i).getCell(5) == null;
			endOfFile = endOfFile
					|| sheet.getRow(i).getCell(4).toString().trim().isEmpty();
			endOfFile = endOfFile
					|| sheet.getRow(i).getCell(5).toString().trim().isEmpty();

			if (endOfFile) {
				if (tuple != null) {
					tuples.put(id, tuple);
				}

				break;
			}

			if (sheet.getRow(i).getCell(0) != null
					&& !sheet.getRow(i).getCell(0).toString().trim().isEmpty()
					&& !sheet.getRow(i).getCell(0).toString().trim().equals(id)) {
				if (tuple != null) {
					tuples.put(id, tuple);
				}

				id = sheet.getRow(i).getCell(0).toString().trim();
				tuple = new KnimeTuple(new TimeSeriesSchema());
				tuple.setValue(TimeSeriesSchema.ATT_CONDID,
						MathUtilities.getRandomNegativeInt());

				if (sheet.getRow(i).getCell(1) != null) {
					tuple.setValue(TimeSeriesSchema.ATT_AGENTDETAIL, sheet
							.getRow(i).getCell(1).toString().trim());
				}

				if (sheet.getRow(i).getCell(2) != null) {
					tuple.setValue(TimeSeriesSchema.ATT_MATRIXDETAIL, sheet
							.getRow(i).getCell(2).toString().trim());
				}

				if (sheet.getRow(i).getCell(3) != null) {
					tuple.setValue(TimeSeriesSchema.ATT_COMMENT, sheet
							.getRow(i).getCell(3).toString().trim());
				}

				if (sheet.getRow(i).getCell(6) != null
						&& !sheet.getRow(i).getCell(6).toString().trim()
								.isEmpty()) {
					try {
						tuple.setValue(
								TimeSeriesSchema.ATT_TEMPERATURE,
								Double.parseDouble(sheet.getRow(i).getCell(6)
										.toString().replace(",", ".")));
					} catch (NumberFormatException e) {
						throw new Exception("Temperature value in row "
								+ (i + 1) + " is not valid");
					}
				}

				if (sheet.getRow(i).getCell(7) != null
						&& !sheet.getRow(i).getCell(7).toString().trim()
								.isEmpty()) {
					try {
						tuple.setValue(
								TimeSeriesSchema.ATT_PH,
								Double.parseDouble(sheet.getRow(i).getCell(7)
										.toString().replace(",", ".")));
					} catch (NumberFormatException e) {
						throw new Exception("pH value in row " + (i + 1)
								+ " is not valid");
					}
				}

				if (sheet.getRow(i).getCell(8) != null
						&& !sheet.getRow(i).getCell(8).toString().trim()
								.isEmpty()) {
					try {
						tuple.setValue(
								TimeSeriesSchema.ATT_WATERACTIVITY,
								Double.parseDouble(sheet.getRow(i).getCell(8)
										.toString().replace(",", ".")));
					} catch (NumberFormatException e) {
						throw new Exception("Water Activity value in row "
								+ (i + 1) + " is not valid");
					}
				}

				PmmXmlDoc miscXML = new PmmXmlDoc();

				for (int j = 0; j < miscNames.size(); j++) {
					String name = miscNames.get(j);
					Double value = null;

					try {
						value = Double.parseDouble(sheet.getRow(i)
								.getCell(j + 9).toString().replace(",", "."));
					} catch (Exception e) {
					}

					miscXML.add(new MiscXml(MathUtilities
							.getRandomNegativeInt(), name, "", value, ""));
				}

				tuple.setValue(TimeSeriesSchema.ATT_MISC, miscXML);
			}

			if (tuple != null) {
				if (sheet.getRow(i).getCell(4) != null
						&& !sheet.getRow(i).getCell(4).toString().trim()
								.isEmpty()) {
					try {
						tuple.addValue(
								TimeSeriesSchema.ATT_TIME,
								Double.parseDouble(sheet.getRow(i).getCell(4)
										.toString().replace(",", ".")));
					} catch (NumberFormatException e) {
						throw new Exception("Time value in row " + (i + 1)
								+ " is not valid");
					}
				}

				if (sheet.getRow(i).getCell(5) != null
						&& !sheet.getRow(i).getCell(5).toString().trim()
								.isEmpty()) {
					try {
						tuple.addValue(
								TimeSeriesSchema.ATT_LOGC,
								Double.parseDouble(sheet.getRow(i).getCell(5)
										.toString().replace(",", ".")));
					} catch (NumberFormatException e) {
						throw new Exception("LogC value in row " + (i + 1)
								+ " is not valid");
					}
				}
			}
		}

		return tuples;
	}

	private static List<String> getAllMiscNames(Sheet sheet) {
		List<String> miscNames = new ArrayList<String>();

		for (int i = 9;; i++) {
			Cell cell = sheet.getRow(0).getCell(i);

			if (cell == null || cell.toString().trim().isEmpty()) {
				break;
			}

			miscNames.add(cell.toString().trim());
		}

		return miscNames;
	}

}
