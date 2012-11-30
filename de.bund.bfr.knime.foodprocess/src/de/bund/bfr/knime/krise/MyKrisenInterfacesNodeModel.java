package de.bund.bfr.knime.krise;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
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

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;

/**
 * This is the model implementation of MyKrisenInterfaces.
 * 
 *
 * @author draaw
 */
public class MyKrisenInterfacesNodeModel extends NodeModel {
    
	static final String PARAM_FILENAME = "filename";
	static final String PARAM_LOGIN = "login";
	static final String PARAM_PASSWD = "passwd";
	static final String PARAM_OVERRIDE = "override";

	private String filename;
	private String login;
	private String passwd;
	private boolean override;

	/**
     * Constructor for the node model.
     */
    protected MyKrisenInterfacesNodeModel() {
        super(0, 3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
        Bfrdb db = null;
    	if (override) {
			db = new Bfrdb(filename, login, passwd);
		} else {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		}

    	LinkedHashMap<Integer, String> id2Code = new LinkedHashMap<Integer, String>(); 
    	// Alle Stationen
    	BufferedDataContainer output33Nodes = exec.createDataContainer(getSpec33Nodes());
    	ResultSet rs = db.pushQuery("SELECT * FROM " + DBKernel.delimitL("Station") + " LEFT JOIN " + DBKernel.delimitL("Kontakte") +
    			" ON " + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("ID") + "=" + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("Kontaktadresse"));
    	int rowNumber = 0;
    	while (rs.next()) {
    		int id = rs.getInt("ID");
    		String bl = getBL(rs.getString("Bundesland"));
    		id2Code.put(id, bl + rowNumber);
    	    RowKey key = RowKey.createRowKey(rowNumber);
    	    DataCell[] cells = new DataCell[4];
    	    cells[0] = new StringCell(bl + rowNumber);
    	    cells[1] = new StringCell("square"); // circle, square, triangle
    	    cells[2] = new DoubleCell(1.5);
    	    cells[3] = new StringCell("yellow"); // red, yellow
    	    DataRow outputRow = new DefaultRow(key, cells);

    	    output33Nodes.addRowToTable(outputRow);
    	    exec.checkCanceled();
    	    //exec.setProgress(rowNumber / 10000, "Adding row " + rowNumber);

    	    rowNumber++;
    	}
    	output33Nodes.close();
    	rs.close();

    	// Alle Lieferungen
    	BufferedDataContainer output33Links = exec.createDataContainer(getSpec33Links());
    	rs = db.pushQuery("SELECT * FROM " + DBKernel.delimitL("Lieferungen") + " LEFT JOIN " + DBKernel.delimitL("Produktkatalog") +
    			" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Artikel") + "=" + DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("ID") +
    			" ORDER BY " + DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("ID"));
    	rowNumber = 0;
    	while (rs.next()) {
    		int id1 = rs.getInt("Produktkatalog.Station");
    		int id2 = rs.getInt("Lieferungen.Empfänger");
    		if (id2Code.containsKey(id1) && id2Code.containsKey(id2)) {
        		String from = id2Code.get(id1);
        		String to = id2Code.get(id2);
        	    RowKey key = RowKey.createRowKey(rowNumber);
        	    DataCell[] cells = new DataCell[3];
        	    cells[0] = new StringCell(from);
        	    cells[1] = new StringCell(to);
        	    cells[2] = new StringCell("black"); // black
        	    DataRow outputRow = new DefaultRow(key, cells);

        	    output33Links.addRowToTable(outputRow);
        	    rowNumber++;
    		}
    		else {
    			exec.setMessage(id1 + " or " + id2 + " not found in Stationen...");
    		}
    	    exec.checkCanceled();
    	    //exec.setProgress(rowNumber / (double)inData[0].getRowCount(), "Adding row " + rowNumber);   	    
    	}
    	output33Links.close();
    	rs.close();
    	
    	// 4 Burow
		String dateFrom = "2012-09-20 00:00:00";
		String dateTo = "2012-09-30 00:00:00";

    	BufferedDataContainer outputBurow = exec.createDataContainer(getSpecBurow());
    	rs = db.pushQuery("SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("AnzahlFaelle") + "," + DBKernel.delimitL("FallErfuellt") +
    			" FROM " + DBKernel.delimitL("Station") + " WHERE " + DBKernel.delimitL("FallErfuellt"));
    	/*
    	rs = db.pushQuery("SELECT MIN(" + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("ID") + ") AS " + DBKernel.delimitL("ID") + 
    			",MIN(" + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("AnzahlFaelle") + ") AS " + DBKernel.delimitL("AnzahlFaelle") +
    			",MIN(" + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("FallErfuellt") + ") AS " + DBKernel.delimitL("FallErfuellt") +
    			",ARRAY_AGG(" + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + " ORDER BY " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + ") AS " + DBKernel.delimitL("LieferID") +
    			",ARRAY_AGG(" + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Lieferdatum") + " ORDER BY " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + ") AS " + DBKernel.delimitL("Lieferdatum") +
    			",ARRAY_AGG (" + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Artikel") + " ORDER BY " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + ") AS " + DBKernel.delimitL("Artikel") +
    			" FROM " + DBKernel.delimitL("Lieferungen") + " LEFT JOIN " + DBKernel.delimitL("Station") +
    			" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Empfänger") + "=" + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("ID") +
    			" WHERE " + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("FallErfuellt") +
    			" GROUP BY " + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Lieferdatum"));
    			*/
    	rowNumber = 0;
    	while (rs.next()) {
    		int numCases = rs.getInt("AnzahlFaelle");
    		boolean casesSi = rs.getBoolean("FallErfuellt");
    		int id = rs.getInt("ID");
    		RowKey key = RowKey.createRowKey(rowNumber);
    	    DataCell[] cells = new DataCell[14];
    	    cells[0] = new IntCell(id);
    	    cells[1] = new IntCell(numCases);
    	    cells[2] = casesSi ? BooleanCell.TRUE : BooleanCell.FALSE;
    	        	    
	    	setLieferungen(db, id, cells, 3);
    	    
    	    cells[8] = DataType.getMissingCell(); // Zutaten2
    	    cells[9] = DataType.getMissingCell(); // Lieferdatums2
    	    cells[10] = DataType.getMissingCell(); // Zulieferer2
    	    cells[11] = DataType.getMissingCell(); // Zutaten3
    	    cells[12] = DataType.getMissingCell(); // Lieferdatums3
    	    cells[13] = DataType.getMissingCell(); // Zulieferer3

    	    setVorlieferungen(db, id, true, cells, 5, 2);

    	    DataRow outputRow = new DefaultRow(key, cells);

    	    outputBurow.addRowToTable(outputRow);
    	    exec.checkCanceled();
    	    //exec.setProgress(rowNumber / (double)inData[0].getRowCount(), "Adding row " + rowNumber);   	    
    	    rowNumber++;
    	}
    	outputBurow.close();
    	rs.close();
    	
    	db.close();
        return new BufferedDataTable[]{output33Nodes.getTable(), output33Links.getTable(), outputBurow.getTable()};
    }

    private void setLieferungen(Bfrdb db, Integer stationID, DataCell[] cells, int cellLfd) throws SQLException {
    	ResultSet rs = db.pushQuery("SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Artikel") + "," + DBKernel.delimitL("Lieferdatum") +
    			" FROM " + DBKernel.delimitL("Lieferungen") + " LEFT JOIN " + DBKernel.delimitL("Produktkatalog") +
    			" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Artikel") + "=" + DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("ID") +
    			" WHERE " + DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("Station") + "=" + stationID);
		String lieferdatum = "";
		String zutaten = "";
    	while (rs.next()) {
    		lieferdatum += "," + (rs.getObject("Lieferdatum") == null ? "" : rs.getString("Lieferdatum"));
    		String zutat = rs.getObject("Artikel") == null ? "" : rs.getString("Artikel");
    		zutaten += "," + zutat;
    	}
	    if (lieferdatum.length() > 0) lieferdatum = lieferdatum.substring(1);
	    if (zutaten.length() > 0) zutaten = zutaten.substring(1);
	    
	    cells[cellLfd] = zutaten.length() == 0 ? DataType.getMissingCell() : new StringCell(zutaten); // Produkte
	    cells[cellLfd + 1] = lieferdatum.length() == 0 ? DataType.getMissingCell() : new StringCell(lieferdatum); // Verzehrsdatum
    }
    private void setVorlieferungen(Bfrdb db, Integer lieferID, boolean isEmpfaenger, DataCell[] cells, int cellLfd, int depth) throws SQLException {
    	ResultSet rs;
    	if (isEmpfaenger) {
    		rs = db.pushQuery("SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Artikel") + "," + DBKernel.delimitL("Lieferdatum") +
    			" FROM " + DBKernel.delimitL("Lieferungen") + " WHERE " + DBKernel.delimitL("Empfänger") + "=" + lieferID);
    	}
    	else {
			rs = db.pushQuery("SELECT " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + " AS " + DBKernel.delimitL("ID") +
	    			"," + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Artikel") + " AS " + DBKernel.delimitL("Artikel") +
	    			"," + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Lieferdatum") + " AS " + DBKernel.delimitL("Lieferdatum") +
	    			" FROM " + DBKernel.delimitL("LieferungVerbindungen") + " LEFT JOIN " + DBKernel.delimitL("Lieferungen") +
	    			" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + "=" + DBKernel.delimitL("LieferungVerbindungen") + "." + DBKernel.delimitL("Vorprodukt") +
	    			" WHERE " + DBKernel.delimitL("LieferungVerbindungen") + "." + DBKernel.delimitL("Zielprodukt") + "=" + lieferID);
    		}
		String lieferdatum = "";
		String zutaten = "";
		String lieferanten = "";
    	while (rs.next()) {
    		lieferdatum += "," + (rs.getObject("Lieferdatum") == null ? "" : rs.getString("Lieferdatum"));
    		String zutat = rs.getObject("Artikel") == null ? "" : rs.getString("Artikel");
    		zutaten += "," + zutat;
	    	lieferanten += "," + DBKernel.getValue("Produktkatalog", "ID", zutat, "Station");
	    	
	    	if (depth > 0) setVorlieferungen(db, rs.getInt("ID"), false, cells, cellLfd + 3, depth - 1);
    	}
	    if (lieferdatum.length() > 0) lieferdatum = "(" + lieferdatum.substring(1) + ")";
	    if (zutaten.length() > 0) zutaten = "(" + zutaten.substring(1) + ")";
	    if (lieferanten.length() > 0) lieferanten = "(" + lieferanten.substring(1) + ")";
	    
	    cells[cellLfd] = setStringCellVal(cells[cellLfd], zutaten);
	    cells[cellLfd + 1] = setStringCellVal(cells[cellLfd + 1], lieferdatum);
	    cells[cellLfd + 2] = setStringCellVal(cells[cellLfd + 2], lieferanten);
	    /*
	    cells[cellLfd] = zutaten.length() == 0 ? DataType.getMissingCell() : new StringCell(zutaten); // Zutaten
	    cells[cellLfd + 1] = lieferdatum.length() == 0 ? DataType.getMissingCell() : new StringCell(lieferdatum); // Lieferdatums
	    cells[cellLfd + 2] = lieferanten.length() == 0 ? DataType.getMissingCell() : new StringCell(lieferanten); // Zulieferer
	    */
    }
    private DataCell setStringCellVal(DataCell cell, String content) {
	    String newVal = cell == null || cell.isMissing() ? content : ((StringCell) cell).getStringValue() + "," + content;
	    return newVal.length() == 0 ? DataType.getMissingCell() : new StringCell(newVal);
    }
    private DataTableSpec getSpecBurow() {
    	DataColumnSpec[] spec = new DataColumnSpec[14];
    	spec[0] = new DataColumnSpecCreator("Endstation", IntCell.TYPE).createSpec();
    	spec[1] = new DataColumnSpecCreator("N_Fälle", IntCell.TYPE).createSpec();
    	spec[2] = new DataColumnSpecCreator("Falldefinition erfüllt", BooleanCell.TYPE).createSpec();
    	spec[3] = new DataColumnSpecCreator("Produkte", StringCell.TYPE).createSpec();
    	spec[4] = new DataColumnSpecCreator("Verzehrsdatums", StringCell.TYPE).createSpec();
    	
    	spec[5] = new DataColumnSpecCreator("Zutaten", StringCell.TYPE).createSpec();
    	spec[6] = new DataColumnSpecCreator("Lieferdatums", StringCell.TYPE).createSpec(); 	
    	spec[7] = new DataColumnSpecCreator("Zulieferer", StringCell.TYPE).createSpec();
    	spec[8] = new DataColumnSpecCreator("Zutaten2", StringCell.TYPE).createSpec();
    	spec[9] = new DataColumnSpecCreator("Lieferdatums2", StringCell.TYPE).createSpec(); 	
    	spec[10] = new DataColumnSpecCreator("Zulieferer2", StringCell.TYPE).createSpec();
    	spec[11] = new DataColumnSpecCreator("Zutaten3", StringCell.TYPE).createSpec();
    	spec[12] = new DataColumnSpecCreator("Lieferdatums3", StringCell.TYPE).createSpec(); 	
    	spec[13] = new DataColumnSpecCreator("Zulieferer3", StringCell.TYPE).createSpec();
    	return new DataTableSpec(spec);
    }
    private DataTableSpec getSpec33Nodes() {
    	DataColumnSpec[] spec = new DataColumnSpec[4];
    	spec[0] = new DataColumnSpecCreator("node", StringCell.TYPE).createSpec();
    	spec[1] = new DataColumnSpecCreator("shape", StringCell.TYPE).createSpec();
    	spec[2] = new DataColumnSpecCreator("size", DoubleCell.TYPE).createSpec();
    	spec[3] = new DataColumnSpecCreator("colour", StringCell.TYPE).createSpec();    
    	return new DataTableSpec(spec);
    }
    private DataTableSpec getSpec33Links() {
    	DataColumnSpec[] spec = new DataColumnSpec[3];
    	spec[0] = new DataColumnSpecCreator("from", StringCell.TYPE).createSpec();
    	spec[1] = new DataColumnSpecCreator("to", StringCell.TYPE).createSpec();
    	spec[2] = new DataColumnSpecCreator("colour", StringCell.TYPE).createSpec();
    	return new DataTableSpec(spec);
    }
    
    private String getBL(String bl) {
    	String result = bl;
    	if (result == null) result = "NN";
    	if (result.length() > 2) {
    		result = result.substring(0, 2);
    	}
    	return result;
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
        return new DataTableSpec[]{getSpec33Nodes(), getSpec33Links(), getSpecBurow()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	settings.addString( PARAM_FILENAME, filename );
    	settings.addString( PARAM_LOGIN, login );
    	settings.addString( PARAM_PASSWD, passwd );
    	settings.addBoolean( PARAM_OVERRIDE, override );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	filename = settings.getString( PARAM_FILENAME );
    	login = settings.getString( PARAM_LOGIN );
    	passwd = settings.getString( PARAM_PASSWD );
    	override = settings.getBoolean( PARAM_OVERRIDE );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }

}

