package de.bund.bfr.knime.pmm.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

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
		}
		else {
			URL url = new URL(file.getPath());
			inputStream = url.openStream();
		}
		Workbook wb = WorkbookFactory.create(inputStream);
		Sheet sheet = wb.getSheetAt(0);

		KnimeTuple tuple = null;
		String id = null;

		for (int i = 1;; i++) {
			boolean endOfFile = sheet.getRow(i) == null;

			endOfFile = endOfFile || sheet.getRow(i).getCell(7) == null;
			endOfFile = endOfFile || sheet.getRow(i).getCell(8) == null;
			endOfFile = endOfFile
					|| sheet.getRow(i).getCell(7).toString().trim().isEmpty();
			endOfFile = endOfFile
					|| sheet.getRow(i).getCell(8).toString().trim().isEmpty();

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

				if (sheet.getRow(i).getCell(4) != null
						&& !sheet.getRow(i).getCell(4).toString().trim()
								.isEmpty()) {
					try {
						tuple.setValue(
								TimeSeriesSchema.ATT_TEMPERATURE,
								Double.parseDouble(sheet.getRow(i).getCell(4)
										.toString().replace(",", ".")));
					} catch (NumberFormatException e) {
						throw new Exception("Temperature value in row "
								+ (i + 1) + " is not valid");
					}
				}

				if (sheet.getRow(i).getCell(5) != null
						&& !sheet.getRow(i).getCell(5).toString().trim()
								.isEmpty()) {
					try {
						tuple.setValue(
								TimeSeriesSchema.ATT_PH,
								Double.parseDouble(sheet.getRow(i).getCell(5)
										.toString().replace(",", ".")));
					} catch (NumberFormatException e) {
						throw new Exception("pH value in row " + (i + 1)
								+ " is not valid");
					}
				}

				if (sheet.getRow(i).getCell(6) != null
						&& !sheet.getRow(i).getCell(6).toString().trim()
								.isEmpty()) {
					try {
						tuple.setValue(
								TimeSeriesSchema.ATT_WATERACTIVITY,
								Double.parseDouble(sheet.getRow(i).getCell(6)
										.toString().replace(",", ".")));
					} catch (NumberFormatException e) {
						throw new Exception("Water Activity value in row "
								+ (i + 1) + " is not valid");
					}
				}
			}

			if (sheet.getRow(i).getCell(7) != null
					&& !sheet.getRow(i).getCell(7).toString().trim().isEmpty()) {
				try {
					tuple.addValue(
							TimeSeriesSchema.ATT_TIME,
							Double.parseDouble(sheet.getRow(i).getCell(7)
									.toString().replace(",", ".")));
				} catch (NumberFormatException e) {
					throw new Exception("Time value in row " + (i + 1)
							+ " is not valid");
				}
			}

			if (sheet.getRow(i).getCell(8) != null
					&& !sheet.getRow(i).getCell(8).toString().trim().isEmpty()) {
				try {
					tuple.addValue(
							TimeSeriesSchema.ATT_LOGC,
							Double.parseDouble(sheet.getRow(i).getCell(8)
									.toString().replace(",", ".")));
				} catch (NumberFormatException e) {
					throw new Exception("LogC value in row " + (i + 1)
							+ " is not valid");
				}
			}
		}

		return tuples;
	}
}
