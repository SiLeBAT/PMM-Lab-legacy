/**
 * 
 */
package org.hsh.bfr.db.imports;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.hsh.bfr.db.DBKernel;
import org.hsh.bfr.db.gui.dbtable.MyDBTable;
import org.hsh.bfr.db.gui.dbtree.MyDBTree;

/**
 * @author Armin
 * 
 */
public class MatricesBLSImporter extends FileFilter implements MyImporter {
	/**
	 * This is the one of the methods that is declared in the abstract class
	 */
	public boolean accept(File f) {
		if (f.isDirectory())
			return true;

		String extension = getExtension(f);
		if ((extension.equals("xls")))
			return true;
		return false;
	}

	public String getDescription() {
		return "Matrix_BLS Datei (*.xls)";
	}

	private String getExtension(File f) {
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if (i > 0 && i < s.length() - 1)
			return s.substring(i + 1).toLowerCase();
		return "";
	}

	public void doImport(final String filename, final JProgressBar progress,
			final boolean showResults) {
		// filename =
		// "C:/Users/Armin/Documents/private/freelance/BfR/Data/100716/Matrices_BLS-Liste.xls";
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					if (progress != null) {
						progress.setVisible(true);
						progress.setStringPainted(true);
						progress.setString("Importiere Matrix_BLS Datei...");
						progress.setMinimum(0);
						progress.setMaximum(5);
						progress.setValue(0);
					}

					InputStream is = null;
					if (filename.startsWith("http://")) {
						URL url = new URL(filename);
						URLConnection uc = url.openConnection();
						is = uc.getInputStream();
					} else {
						is = new FileInputStream(filename);
					}

					POIFSFileSystem fs = new POIFSFileSystem(is);
					HSSFWorkbook wb = new HSSFWorkbook(fs);
					HSSFSheet sheet;
					HSSFRow row;

					sheet = wb.getSheet("BLS");
					PreparedStatement ps = DBKernel.getDBConnection()
							.prepareStatement(
									"INSERT INTO "
											+ DBKernel.delimitL("Matrices")
											+ " ("
											+ DBKernel.delimitL("CodeTyp")
											+ "," + DBKernel.delimitL("Code")
											+ ","
											+ DBKernel.delimitL("Matrixname")
											+ ") VALUES (?,?,?)");
					Iterator<Row> rows = sheet.rowIterator();
					while (rows.hasNext()) {
						row = (HSSFRow) rows.next();
						if (row.getRowNum() > 0) {
							ps.setString(1, "BLS");
							ps.setString(2, row.getCell(2).getStringCellValue());
							ps.setString(3, row.getCell(0).getStringCellValue());
							try {
								ps.execute();
							} catch (SQLException e1) {
							} // e1.printStackTrace();
						}
					}
					if (progress != null)
						progress.setValue(1);

					sheet = wb.getSheet("ADV");
					ps = DBKernel.getDBConnection().prepareStatement(
							"INSERT INTO " + DBKernel.delimitL("Matrices")
									+ " (" + DBKernel.delimitL("CodeTyp") + ","
									+ DBKernel.delimitL("Code") + ","
									+ DBKernel.delimitL("Name")
									+ ") VALUES (?,?,?)");
					rows = sheet.rowIterator();
					while (rows.hasNext()) {
						row = (HSSFRow) rows.next();
						if (row.getRowNum() > 0) {
							ps.setString(1, "ADV");
							ps.setString(2, row.getCell(3).getStringCellValue());
							ps.setString(3, row.getCell(0).getStringCellValue());
							try {
								ps.execute();
							} catch (SQLException e1) {
							} // e1.printStackTrace();
						}
					}
					if (progress != null)
						progress.setValue(2);

					sheet = wb.getSheet("GS1");
					ps = DBKernel.getDBConnection().prepareStatement(
							"INSERT INTO " + DBKernel.delimitL("Matrices")
									+ " (" + DBKernel.delimitL("CodeTyp") + ","
									+ DBKernel.delimitL("Code") + ","
									+ DBKernel.delimitL("Name")
									+ ") VALUES (?,?,?)");
					rows = sheet.rowIterator();
					while (rows.hasNext()) {
						row = (HSSFRow) rows.next();
						if (row.getRowNum() > 0) {
							ps.setString(1, "GS1");
							ps.setString(2, row.getCell(4).getStringCellValue());
							ps.setString(3, row.getCell(0).getStringCellValue());
							try {
								ps.execute();
							} catch (SQLException e1) {
							} // e1.printStackTrace();
						}
					}
					if (progress != null)
						progress.setValue(3);

					sheet = wb.getSheet("Nährmedien");
					ps = DBKernel.getDBConnection().prepareStatement(
							"INSERT INTO " + DBKernel.delimitL("Matrices")
									+ " (" + DBKernel.delimitL("Code") + ", "
									+ DBKernel.delimitL("Name")
									+ ") VALUES (?, ?)");
					rows = sheet.rowIterator();
					while (rows.hasNext()) {
						row = (HSSFRow) rows.next();
						if (row.getRowNum() > 0) {
							ps.setString(1, row.getCell(5).getStringCellValue());
							ps.setString(2, row.getCell(0).getStringCellValue());
							try {
								if (row.getRowNum() > 0)
									ps.execute();
							} catch (SQLException e1) {
							} // e1.printStackTrace();
						}
					}
					if (progress != null)
						progress.setValue(4);

					sheet = wb.getSheet("Obergruppen");
					ps = DBKernel.getDBConnection().prepareStatement(
							"INSERT INTO " + DBKernel.delimitL("Matrices-OG")
									+ " (" + DBKernel.delimitL("CodeTyp")
									+ ", " + DBKernel.delimitL("Code") + ", "
									+ DBKernel.delimitL("Name")
									+ ") VALUES (?, ?, ?)");
					rows = sheet.rowIterator();
					while (rows.hasNext()) {
						row = (HSSFRow) rows.next();
						if (row.getRowNum() > 0) {
							ps.setString(1, row.getCell(1).getStringCellValue());
							ps.setString(2, row.getCell(2).getStringCellValue());
							ps.setString(3, row.getCell(0).getStringCellValue());
							try {
								ps.execute();
							} catch (SQLException e1) {
							} // e1.printStackTrace();
						}
					}

					if (progress != null) {
						progress.setValue(5);
						progress.setVisible(false);
						// Refreshen:
						MyDBTable myDB = DBKernel.myList.getMyDBTable();
						if (myDB.getActualTable() != null) {
							String tablename = myDB.getActualTable()
									.getTablename();
							if (tablename.equals("Matrices")) {
								myDB.setTable(myDB.getActualTable());
							}
						}
						MyDBTree myTR = DBKernel.myList.getMyDBTree();
						if (myTR.getActualTable() != null) {
							String tablename = myTR.getActualTable()
									.getTablename();
							if (tablename.equals("Matrices")) {
								myTR.setTable(myTR.getActualTable());
							}
						}
					} else {
						System.out.println("MatricesBLSImporter - Fin");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		Thread thread = new Thread(runnable);
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
