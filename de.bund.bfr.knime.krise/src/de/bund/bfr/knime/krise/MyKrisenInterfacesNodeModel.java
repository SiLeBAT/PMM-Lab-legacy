package de.bund.bfr.knime.krise;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Vector;

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
	static final String PARAM_TRACINGBACK = "tracingBack";
	static final String PARAM_ANONYMIZE = "anonymize";
	static final String PARAM_FILTER_COMPANY = "filter_Company";
	static final String PARAM_FILTER_CHARGE = "filter_Charge";
	static final String PARAM_FILTER_ARTIKEL = "filter_Artikel";
	static final String PARAM_ANTIARTICLE = "antiArtikel";
	static final String PARAM_ANTICHARGE = "antiCharge";
	static final String PARAM_ANTICOMPANY = "antiCompany";

	static final String PARAM_FILTER_DATEFROM = "dateFrom";
	static final String PARAM_FILTER_DATETO = "dateTo";
	
	private static final int STATION = 0;
	private static final int ARTIKEL = 1;
	private static final int CHARGE = 2;
	private static final int LIEFERUNG = 3;

	private String filename;
	private String login;
	private String passwd;
	private boolean override;
	private boolean doAnonymize;
	private String companyFilter, chargeFilter, artikelFilter;
	private boolean antiArticle, antiCharge, antiCompany, tracingBack;
	private String dateFrom, dateTo;
	
	private int maxDepth = 2;
	private String referenzDatum;
	private SimpleDateFormat sdfToDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 2012-07-10 00:00:00
	
	private Connection conn = null;
	
	private String stage = "";

	/**
     * Constructor for the node model.
     */
    protected MyKrisenInterfacesNodeModel() {
        super(0, 4);
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
			conn = db.getConnection();
		} else {
			db = new Bfrdb(DBKernel.getLocalConn(true));
			conn = null;
		}

    	LinkedHashMap<Integer, Integer> compChain = applyCompanyFilter(db);
    	LinkedHashMap<Integer, Integer> chargeChain = applyChargeFilter(db);
    	LinkedHashMap<Integer, Integer> articleChain = applyArticleFilter(db);
    	
    	if (tracingBack) {
    		int objectType = ARTIKEL; // STATION CHARGE LIEFERUNG
    		stage = "";
    		HashSet<HashSet<Integer>> tb = makeTracingBack(db, true, objectType);
        	String warningMessage = stage;
        	int lfd = 0;
        	for (HashSet<Integer> hsi : tb) {
        		lfd++;
            	warningMessage += "\nKombi " + lfd + ":\n";
            	for (Integer objectID : hsi) {
            		String sql = "";
            		if (objectType == STATION) {
            			sql = "SELECT " + DBKernel.delimitL("Name") + " AS " + DBKernel.delimitL("Object") +
            					" FROM " + DBKernel.delimitL("Station") + " LEFT JOIN " + DBKernel.delimitL("Kontakte") +
                    			" ON " + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("ID") + "=" + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("Kontaktadresse") +
                    			" WHERE " + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("ID") + "=" + objectID;
            		}
            		else if (objectType == ARTIKEL) {
            			sql = "SELECT " + DBKernel.delimitL("Bezeichnung") + " AS " + DBKernel.delimitL("Object") +
            					" FROM " + DBKernel.delimitL("Produktkatalog") +
            					" WHERE " + DBKernel.delimitL("ID") + "=" + objectID;
            		}
            		else if (objectType == CHARGE) {
            			sql = "SELECT " + DBKernel.delimitL("Bezeichnung") + " AS " + DBKernel.delimitL("Object") +
            					" FROM " + DBKernel.delimitL("Chargen") + " LEFT JOIN " + DBKernel.delimitL("Produktkatalog") +
                    			" ON " + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("Artikel") + "=" + DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("ID") +
                    			" WHERE " + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("ID") + "=" + objectID;
            		}
            		else if (objectType == LIEFERUNG) {
            			sql = "SELECT " + DBKernel.delimitL("Lieferdatum") + " AS " + DBKernel.delimitL("Object") +
            					" FROM " + DBKernel.delimitL("Lieferungen") +
            					" WHERE " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + "=" + objectID;
            		}

                	ResultSet rs = db.pushQuery(sql);
                	rs.next();
            		warningMessage += rs.getString("Object") + " (ID:" + objectID + ")\n";
            	}
        	}
        	if (!warningMessage.isEmpty()) {
        		warningMessage = "Tracing successful, susceptible Companies:" + warningMessage;
            	this.setWarningMessage(warningMessage);
        	}
    	}

    	LinkedHashMap<Integer, String> id2Code = new LinkedHashMap<Integer, String>(); 
    	// Alle Stationen -> Nodes33
    	BufferedDataContainer output33Nodes = exec.createDataContainer(getSpec33Nodes());
    	ResultSet rs = db.pushQuery("SELECT * FROM " + DBKernel.delimitL("Station") + " LEFT JOIN " + DBKernel.delimitL("Kontakte") +
    			" ON " + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("ID") + "=" + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("Kontaktadresse"));
    	int rowNumber = 0;
    	while (rs.next()) {
    		int stationID = rs.getInt("Station.ID");
    		if (!antiArticle || !checkCase(stationID) || !checkCompanyReceivedArticle(stationID, articleChain)) {
        		String bl = getBL(rs.getString("Bundesland"));
        		String company  = (rs.getObject("Name") == null || doAnonymize) ? bl + rowNumber : rs.getString("Name");
        		id2Code.put(stationID, company);
        	    RowKey key = RowKey.createRowKey(rowNumber);
        	    DataCell[] cells = new DataCell[14];
        	    cells[0] = new IntCell(stationID);
        	    cells[1] = new StringCell(company);
        	    cells[2] = new StringCell("square"); // circle, square, triangle
        	    cells[3] = new DoubleCell(1.5);
        	    cells[4] = new StringCell("yellow"); // red, yellow
        	    cells[5] = (doAnonymize || rs.getObject("PLZ") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("PLZ"));
        	    cells[6] = (doAnonymize || rs.getObject("Ort") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("Ort"));
        	    cells[7] = (doAnonymize || rs.getObject("Bundesland") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("Bundesland"));
        	    cells[8] = (rs.getObject("Betriebsart") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("Betriebsart"));
        	    cells[9] = (rs.getObject("FallErfuellt") == null) ? DataType.getMissingCell() : (rs.getBoolean("FallErfuellt") ? BooleanCell.TRUE : BooleanCell.FALSE);
        	    cells[10] = (rs.getObject("AnzahlFaelle") == null) ? DataType.getMissingCell() : new IntCell(rs.getInt("AnzahlFaelle"));
        	    cells[11] = (rs.getObject("DatumBeginn") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("DatumBeginn"));
        	    cells[12] = (rs.getObject("DatumHoehepunkt") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("DatumHoehepunkt"));
        	    cells[13] = (rs.getObject("DatumEnde") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("DatumEnde"));
        	    DataRow outputRow = new DefaultRow(key, cells);

        	    output33Nodes.addRowToTable(outputRow);
    		}
    	    exec.checkCanceled();
    	    //exec.setProgress(rowNumber / 10000, "Adding row " + rowNumber);

    	    rowNumber++;
    	}
    	output33Nodes.close();
    	rs.close();

    	// Alle Lieferungen -> Links33
    	BufferedDataContainer output33Links = exec.createDataContainer(getSpec33Links());
    	rs = db.pushQuery("SELECT * FROM " + DBKernel.delimitL("Lieferungen") +
    			" LEFT JOIN " + DBKernel.delimitL("Chargen") +
    			" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Charge") + "=" + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("ID") +
    			" LEFT JOIN " + DBKernel.delimitL("Produktkatalog") +
    			" ON " + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("Artikel") + "=" + DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("ID") +
    			" ORDER BY " + DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("ID"));
    	rowNumber = 0;
    	while (rs.next()) {
    		int lieferID = rs.getInt("Lieferungen.ID");
    		if ((companyFilter.trim().isEmpty() || compChain.containsKey(lieferID)) &&
    				(chargeFilter.trim().isEmpty() || chargeChain.containsKey(lieferID)) &&
    				(antiArticle || artikelFilter.trim().isEmpty() || articleChain.containsKey(lieferID))) {
        		int id1 = rs.getInt("Produktkatalog.Station");
        		int id2 = rs.getInt("Lieferungen.Empfänger");
        		if (id2Code.containsKey(id1) && id2Code.containsKey(id2)) {
            		int from = id1;//id2Code.get(id1);
            		int to = id2;//id2Code.get(id2);
            	    RowKey key = RowKey.createRowKey(rowNumber);
            	    DataCell[] cells = new DataCell[18];
            	    cells[0] = new IntCell(from);
            	    cells[1] = new IntCell(to);
            	    cells[2] = new StringCell("black"); // black
            	    cells[3] = (doAnonymize || rs.getObject("Artikelnummer") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("Artikelnummer"));
            	    cells[4] = (doAnonymize || rs.getObject("Bezeichnung") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("Bezeichnung"));
            	    cells[5] = (rs.getObject("Prozessierung") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("Prozessierung"));
            	    cells[6] = (rs.getObject("IntendedUse") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("IntendedUse"));
            	    cells[7] = (doAnonymize || rs.getObject("ChargenNr") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("ChargenNr"));
            	    cells[8] = (rs.getObject("MHD") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("MHD"));
            	    cells[9] = (rs.getObject("Herstellungsdatum") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("Herstellungsdatum"));
            	    cells[10] = (rs.getObject("Lieferungen.Lieferdatum") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("Lieferungen.Lieferdatum"));
            	    cells[11] = (rs.getObject("#Units1") == null) ? DataType.getMissingCell() : new DoubleCell(rs.getDouble("#Units1"));
            	    cells[12] = (rs.getObject("BezUnits1") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("BezUnits1"));
            	    cells[13] = (rs.getObject("#Units2") == null) ? DataType.getMissingCell() : new DoubleCell(rs.getDouble("#Units2"));
            	    cells[14] = (rs.getObject("BezUnits2") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("BezUnits2"));
            	    cells[15] = (rs.getObject("Unitmenge") == null) ? DataType.getMissingCell() : new DoubleCell(rs.getDouble("Unitmenge"));
            	    cells[16] = (rs.getObject("UnitEinheit") == null) ? DataType.getMissingCell() : new StringCell(rs.getString("UnitEinheit"));
            	    cells[17] = new StringCell("Row" + rowNumber);
            	    DataRow outputRow = new DefaultRow(key, cells);

            	    output33Links.addRowToTable(outputRow);
            	    rowNumber++;
        		}
        		else {
        			exec.setMessage(id1 + " or " + id2 + " not found in Stationen...");
        		}
    		}
    	    exec.checkCanceled();
    	    //exec.setProgress(rowNumber / (double)inData[0].getRowCount(), "Adding row " + rowNumber);   	    
    	}
    	output33Links.close();
    	rs.close();
    	
    	// 4 Burow
    	BufferedDataContainer outputBurow = exec.createDataContainer(getSpecBurow());
    	rs = db.pushQuery("SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("AnzahlFaelle") + "," + DBKernel.delimitL("FallErfuellt") +
    			" FROM " + DBKernel.delimitL("Station")); //  + " WHERE " + DBKernel.delimitL("FallErfuellt")
    	rowNumber = 0;
    	referenzDatum = dateFrom;
	    HashMap<Integer, HashMap<Integer, Vector<Integer>>> stationenLieferanten = new HashMap<Integer, HashMap<Integer, Vector<Integer>>>();
	    HashMap<Integer, HashMap<Integer, Vector<Integer>>> stationenZutaten = new HashMap<Integer, HashMap<Integer, Vector<Integer>>>();
	    HashMap<Integer, HashMap<Integer, Vector<Integer>>> stationenDatums = new HashMap<Integer, HashMap<Integer, Vector<Integer>>>();
	    HashMap<Integer, DataCell[]> stationenSonstiges = new HashMap<Integer, DataCell[]>();
    	while (rs.next()) {
    		int numCases = rs.getInt("AnzahlFaelle");
    		boolean casesSi = rs.getBoolean("FallErfuellt");
    		int id = rs.getInt("ID");
    		RowKey key = RowKey.createRowKey(rowNumber);
    	    DataCell[] cells = new DataCell[14];
    	    cells[0] = new IntCell(id);
    	    cells[1] = new IntCell(numCases);
    	    cells[2] = casesSi ? BooleanCell.TRUE : BooleanCell.FALSE;
    	        	        	    
    	    cells[3] = DataType.getMissingCell(); // Produkte
    	    cells[4] = DataType.getMissingCell(); // Verzehrsdatums

    	    stationenSonstiges.put(id, cells);
    	    
    	    setLieferungen(db, id, cells, 3);
    	    
    	    cells[5] = DataType.getMissingCell(); // Zutaten
    	    cells[6] = DataType.getMissingCell(); // Lieferdatums
    	    cells[7] = DataType.getMissingCell(); // Zulieferer
    	    cells[8] = DataType.getMissingCell(); // Zutaten2
    	    cells[9] = DataType.getMissingCell(); // Lieferdatums2
    	    cells[10] = DataType.getMissingCell(); // Zulieferer2
    	    cells[11] = DataType.getMissingCell(); // Zutaten3
    	    cells[12] = DataType.getMissingCell(); // Lieferdatums3
    	    cells[13] = DataType.getMissingCell(); // Zulieferer3
    	    
    	    HashMap<Integer, Vector<Integer>> hm1 = new HashMap<Integer, Vector<Integer>>();
    	    HashMap<Integer, Vector<Integer>> hm2 = new HashMap<Integer, Vector<Integer>>();
    	    HashMap<Integer, Vector<Integer>> hm3 = new HashMap<Integer, Vector<Integer>>();
    	    
    	    setVorlieferungen(db, id, true, cells, 5, maxDepth, hm1, hm2, hm3);

    	    if (hm1.size() > 0) {
    	    	stationenLieferanten.put(id, hm1);
        	    stationenZutaten.put(id, hm2);
        	    stationenDatums.put(id, hm3);
    	    }

    	    if (!onlyMissingCells(cells, 3)) {
        	    DataRow outputRow = new DefaultRow(key, cells);

        	    outputBurow.addRowToTable(outputRow);
    	    }
    	    exec.checkCanceled();
    	    //exec.setProgress(rowNumber / (double)inData[0].getRowCount(), "Adding row " + rowNumber);   	    
    	    rowNumber++;
    	}
    	outputBurow.close();
    	rs.close();
    	
    	db.close();

	    HashMap<String, Integer> colMap = new HashMap<String, Integer>();
    	BufferedDataContainer outputBurowNew = exec.createDataContainer(getOutSpecBurow(stationenLieferanten, stationenZutaten, stationenDatums, colMap));
    	rowNumber = 0;
    	for (Integer endstation : stationenLieferanten.keySet()) {
    		DataCell[] c = stationenSonstiges.get(endstation);
    		RowKey key = RowKey.createRowKey(rowNumber);
    	    DataCell[] cells = new DataCell[colMap.size() + 5];
    	    cells[0] = c[0];
    	    cells[1] = c[1];
    	    cells[2] = c[2];
    	    cells[3] = c[3];
    	    cells[4] = c[4];
    	    int[] res = new int[colMap.size()];
    	    for (int i=0;i<colMap.size();i++) {
    	    	res[i] = 0;
    	    }
    	    
    	    HashMap<Integer, Vector<Integer>> hm1 = stationenLieferanten.get(endstation);
    	    for (Integer round : hm1.keySet()) {
    	    	for (Integer id : hm1.get(round)) {
            	    res[colMap.get("s" + round + "_" + id)]++;
    	    	}
    	    }
    	    HashMap<Integer, Vector<Integer>> hm2 = stationenZutaten.get(endstation);
    	    for (Integer round : hm2.keySet()) {
    	    	for (Integer id : hm2.get(round)) {
            	    res[colMap.get("Z" + round + "_" + id)]++;
    	    	}
    	    }
    	    HashMap<Integer, Vector<Integer>> hm3 = stationenDatums.get(endstation);
    	    for (Integer round : hm3.keySet()) {
    	    	for (Integer id : hm3.get(round)) {
            	    res[colMap.get("sdat_" + round + "_" + id)]++;
    	    	}
    	    }
    	    
    	    for (int i=0;i<colMap.size();i++) {
    	    	cells[5 + i] = new IntCell(res[i]);
    	    }

    	    DataRow outputRow = new DefaultRow(key, cells);
    	    outputBurowNew.addRowToTable(outputRow);

    	    exec.checkCanceled();
    	    rowNumber++;
    	}
    	outputBurowNew.close();

    	return new BufferedDataTable[]{output33Nodes.getTable(), output33Links.getTable(), outputBurow.getTable(), outputBurowNew.getTable()};
    }

    private boolean checkCompanyReceivedArticle(int stationID, LinkedHashMap<Integer, Integer> articleChain) throws SQLException {
    	boolean result = false;
    	for (Integer empfaengerID : articleChain.values()) {
    		if (empfaengerID == stationID) {
        		result = true;
    			break;
    		}
    	}
    	return result;
    }
    private boolean checkCase(int stationID) {
    	boolean result = (DBKernel.getValue(conn, "Station", "ID", stationID+"", "FallErfuellt") != null); 
		return result;
    }
    private boolean onlyMissingCells(DataCell[] cells, int startCol) {
    	boolean result = true;
    	for (int i=startCol;i<cells.length;i++) {
    		String tstr = cells[i].toString().replace(",", "").trim();
    		if (tstr.isEmpty()) {
    			cells[i] = DataType.getMissingCell();
    		}
    		if (!cells[i].isMissing() && !cells[i].toString().isEmpty()) result = false;
    	}
    	return result;
    }
    private void setLieferungen(Bfrdb db, Integer stationID, DataCell[] cells, int cellLfd) throws SQLException {
    	ResultSet rs = db.pushQuery("SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Artikel") + "," + DBKernel.delimitL("Lieferdatum") +
    			" FROM " + DBKernel.delimitL("Lieferungen") +
    			" LEFT JOIN " + DBKernel.delimitL("Chargen") +
    			" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Charge") + "=" + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("ID") +
    			" LEFT JOIN " + DBKernel.delimitL("Produktkatalog") +
    			" ON " + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("Artikel") + "=" + DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("ID") +
    			" WHERE " + DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("Station") + "=" + stationID +
	    		(dateFrom != null && !dateFrom.isEmpty() ? " AND " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Lieferdatum") + ">='" + dateFrom + "'" : "") +
	    		(dateTo != null && !dateTo.isEmpty() ? " AND " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Lieferdatum") + "<='" + dateTo + "'" : ""));
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
    private void setVorlieferungen(Bfrdb db, Integer lieferID, boolean isEmpfaenger, DataCell[] cells, int cellLfd, int depth,
    		HashMap<Integer, Vector<Integer>> stationenLieferanten,
    		HashMap<Integer, Vector<Integer>> stationenZutaten,
    		HashMap<Integer, Vector<Integer>> stationenDatums) throws SQLException {
    	String sql;
    	if (isEmpfaenger) {
    		sql = "SELECT " + DBKernel.delimitL("ID") + "," + DBKernel.delimitL("Charge") + "," + DBKernel.delimitL("Lieferdatum") +
    			" FROM " + DBKernel.delimitL("Lieferungen") +
    			" WHERE " + DBKernel.delimitL("Empfänger") + "=" + lieferID +
    			(depth < maxDepth ? "" : (dateFrom != null && !dateFrom.isEmpty() ? " AND " + DBKernel.delimitL("Lieferdatum") + ">='" + dateFrom + "'" : "") +
    					(dateTo != null && !dateTo.isEmpty() ? " AND " + DBKernel.delimitL("Lieferdatum") + "<='" + dateTo + "'" : ""));
    	}
    	else {
    		sql = "SELECT " + DBKernel.delimitL("ZutatLieferung") + "." + DBKernel.delimitL("ID") + " AS " + DBKernel.delimitL("ID") +
	    			"," + DBKernel.delimitL("ZutatLieferung") + "." + DBKernel.delimitL("Charge") + " AS " + DBKernel.delimitL("Charge") +
	    			"," + DBKernel.delimitL("ZutatLieferung") + "." + DBKernel.delimitL("Lieferdatum") + " AS " + DBKernel.delimitL("Lieferdatum") +
	    			" FROM " + DBKernel.delimitL("Lieferungen") + " AS " + DBKernel.delimitL("ProduktLieferung") +
	    			" LEFT JOIN " + DBKernel.delimitL("ChargenVerbindungen") +
	    			" ON " + DBKernel.delimitL("ChargenVerbindungen") + "." + DBKernel.delimitL("Produkt") + "=" + DBKernel.delimitL("ProduktLieferung") + "." + DBKernel.delimitL("Charge") +
	    			" LEFT JOIN " + DBKernel.delimitL("Lieferungen") + " AS " + DBKernel.delimitL("ZutatLieferung") +
	    			" ON " + DBKernel.delimitL("ChargenVerbindungen") + "." + DBKernel.delimitL("Zutat") + "=" + DBKernel.delimitL("ZutatLieferung") + "." + DBKernel.delimitL("ID") +
	    			" WHERE " + DBKernel.delimitL("ProduktLieferung") + "." + DBKernel.delimitL("ID") + "=" + lieferID +
	    			" AND " + DBKernel.delimitL("ZutatLieferung") + "." + DBKernel.delimitL("ID") + ">" + 0 +
	    			(depth < maxDepth ? "" : (dateFrom != null && !dateFrom.isEmpty() ? " AND " + DBKernel.delimitL("Lieferdatum") + ">='" + dateFrom + "'" : "") +
	    					(dateTo != null && !dateTo.isEmpty() ? " AND " + DBKernel.delimitL("Lieferdatum") + "<='" + dateTo + "'" : ""));
    		}
    	ResultSet rs = db.pushQuery(sql);
		String lieferdatum = "";
		String zutaten = "";
		String lieferanten = "";
		Vector<Integer> datum = new Vector<Integer>();
		Vector<Integer> zut = new Vector<Integer>();
		Vector<Integer> lief = new Vector<Integer>();
    	while (rs.next()) {
    		if (rs.getObject("Charge") != null) {
    			String charge = rs.getString("Charge");
    			String artikel = DBKernel.getValue(conn,"Chargen", "ID", charge, "Artikel") + "";
    			Object li = DBKernel.getValue(conn,"Produktkatalog", "ID", artikel, "Station");
    			
				int diffDays = 0;
    			if (rs.getObject("Lieferdatum") != null) {
    				if (referenzDatum == null || referenzDatum.isEmpty()) referenzDatum = sdfToDate.format(rs.getDate("Lieferdatum"));
    				try {
						long diff = rs.getDate("Lieferdatum").getTime() - sdfToDate.parse(referenzDatum).getTime();
						diffDays = (int) (diff / (24 * 60 * 60 * 1000));  
					}
    				catch (ParseException e) {
						e.printStackTrace();
					}
    			}
				datum.add(diffDays);
    			zut.add(rs.getInt("Charge"));
    			if (li instanceof Integer) lief.add((Integer) li); else lief.add(0);
    		}
    		String charge = rs.getObject("Charge") == null ? "" : rs.getString("Charge");
    		lieferdatum += "," + (rs.getObject("Lieferdatum") == null ? "" : rs.getString("Lieferdatum"));
    		zutaten += "," + charge;
    		String artikel = DBKernel.getValue(conn,"Chargen", "ID", charge, "Artikel") + "";
	    	lieferanten += "," + DBKernel.getValue(conn,"Produktkatalog", "ID", artikel, "Station");
	    	
	    	if (depth > 0) setVorlieferungen(db, rs.getInt("ID"), false, cells, cellLfd + 3, depth - 1, stationenLieferanten, stationenZutaten, stationenDatums);
    	}
		if (lief.size() > 0) {
			if (stationenLieferanten.containsKey(maxDepth - depth + 1)) {
				Vector<Integer> liefIn = stationenLieferanten.get(maxDepth - depth + 1);
				for (Integer li : liefIn) lief.add(li);
			}
			stationenLieferanten.put(maxDepth - depth + 1, lief);
			if (stationenZutaten.containsKey(maxDepth - depth + 1)) {
				Vector<Integer> zutIn = stationenZutaten.get(maxDepth - depth + 1);
				for (Integer li : zutIn) zut.add(li);
			}
			stationenZutaten.put(maxDepth - depth + 1, zut);
			if (stationenDatums.containsKey(maxDepth - depth + 1)) {
				Vector<Integer> datumIn = stationenDatums.get(maxDepth - depth + 1);
				for (Integer li : datumIn) datum.add(li);
			}
			stationenDatums.put(maxDepth - depth + 1, datum);
		}
	    if (lieferdatum.length() > 0) lieferdatum = "(" + lieferdatum.substring(1) + ")";
	    if (lieferanten.length() > 0) lieferanten = "(" + lieferanten.substring(1) + ")";
	    if (zutaten.length() > 1) zutaten = "(" + zutaten.substring(1) + ")";
	    
	    cells[cellLfd] = setStringCellVal(cells[cellLfd], zutaten);
	    cells[cellLfd + 1] = setStringCellVal(cells[cellLfd + 1], lieferdatum);
	    cells[cellLfd + 2] = setStringCellVal(cells[cellLfd + 2], lieferanten);
    }
    private DataCell setStringCellVal(DataCell cell, String content) {
	    String newVal = cell == null || cell.isMissing() ? content : ((StringCell) cell).getStringValue() + "," + content;
	    return new StringCell(newVal);
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
    private DataTableSpec getOutSpecBurow(HashMap<Integer, HashMap<Integer, Vector<Integer>>> stationenLieferanten,
    		HashMap<Integer, HashMap<Integer, Vector<Integer>>> stationenZutaten,
    		HashMap<Integer, HashMap<Integer, Vector<Integer>>> stationenDatums,
    		HashMap<String, Integer> colMap) {
    	LinkedHashSet<DataColumnSpec> hsSpec = new LinkedHashSet<DataColumnSpec>();
    	
    	hsSpec.add(new DataColumnSpecCreator("Endstation", IntCell.TYPE).createSpec());
    	hsSpec.add(new DataColumnSpecCreator("N_Fälle", IntCell.TYPE).createSpec());
    	hsSpec.add(new DataColumnSpecCreator("Falldefinition", BooleanCell.TYPE).createSpec());
    	hsSpec.add(new DataColumnSpecCreator("Produkte", StringCell.TYPE).createSpec());
    	hsSpec.add(new DataColumnSpecCreator("Verzehrsdatums", StringCell.TYPE).createSpec());

    	int lfd = 0;
    	for (Integer endstation : stationenLieferanten.keySet()) {
    	    HashMap<Integer, Vector<Integer>> hm1 = stationenLieferanten.get(endstation);
    	    for (Integer round : hm1.keySet()) {
    	    	for (Integer id : hm1.get(round)) {
    	    		String s = "s" + round + "_" + id;
    	    		if (!colMap.containsKey(s)) {
    	    			colMap.put(s, lfd);
    	    			hsSpec.add(new DataColumnSpecCreator(s, IntCell.TYPE).createSpec());
        	    		lfd++;
    	    		}
    	    	}
    	    }
    	    HashMap<Integer, Vector<Integer>> hm2 = stationenZutaten.get(endstation);
    	    for (Integer round : hm2.keySet()) {
    	    	for (Integer id : hm2.get(round)) {
    	    		String s = "Z" + round + "_" + id;
    	    		if (!colMap.containsKey(s)) {
    	    			colMap.put(s, lfd);
    	    			hsSpec.add(new DataColumnSpecCreator(s, IntCell.TYPE).createSpec());
        	    		lfd++;
    	    		}
    	    	}
    	    }
    	    HashMap<Integer, Vector<Integer>> hm3 = stationenDatums.get(endstation);
    	    for (Integer round : hm3.keySet()) {
    	    	for (Integer id : hm3.get(round)) {
    	    		String s = "sdat_" + round + "_" + id;
    	    		if (!colMap.containsKey(s)) {
    	    			colMap.put(s, lfd);
    	    			hsSpec.add(new DataColumnSpecCreator(s, IntCell.TYPE).createSpec());
        	    		lfd++;
    	    		}
    	    	}
    	    }
    	}

	    return new DataTableSpec(hsSpec.toArray(new DataColumnSpec[]{}));
    }
    private DataTableSpec getSpec33Nodes() {
    	DataColumnSpec[] spec = new DataColumnSpec[14];
    	spec[0] = new DataColumnSpecCreator("ID", IntCell.TYPE).createSpec();
    	spec[1] = new DataColumnSpecCreator("node", StringCell.TYPE).createSpec();
    	spec[2] = new DataColumnSpecCreator("shape", StringCell.TYPE).createSpec();
    	spec[3] = new DataColumnSpecCreator("size", DoubleCell.TYPE).createSpec();
    	spec[4] = new DataColumnSpecCreator("colour", StringCell.TYPE).createSpec();    
    	spec[5] = new DataColumnSpecCreator("PLZ", StringCell.TYPE).createSpec();    
    	spec[6] = new DataColumnSpecCreator("Ort", StringCell.TYPE).createSpec();    
    	spec[7] = new DataColumnSpecCreator("Bundesland", StringCell.TYPE).createSpec();    
    	spec[8] = new DataColumnSpecCreator("Betriebsart", StringCell.TYPE).createSpec();    
    	spec[9] = new DataColumnSpecCreator("FallErfuellt", BooleanCell.TYPE).createSpec();    
    	spec[10] = new DataColumnSpecCreator("NumFaelle", IntCell.TYPE).createSpec();    
    	spec[11] = new DataColumnSpecCreator("DatumBeginn", StringCell.TYPE).createSpec();    
    	spec[12] = new DataColumnSpecCreator("DatumHoehepunkt", StringCell.TYPE).createSpec();    
    	spec[13] = new DataColumnSpecCreator("DatumEnde", StringCell.TYPE).createSpec();    
    	return new DataTableSpec(spec);
    }
    private DataTableSpec getSpec33Links() {
    	DataColumnSpec[] spec = new DataColumnSpec[18];
    	spec[0] = new DataColumnSpecCreator("from", IntCell.TYPE).createSpec();
    	spec[1] = new DataColumnSpecCreator("to", IntCell.TYPE).createSpec();
    	spec[2] = new DataColumnSpecCreator("colour", StringCell.TYPE).createSpec();
    	spec[3] = new DataColumnSpecCreator("Artikelnummer", StringCell.TYPE).createSpec();
    	spec[4] = new DataColumnSpecCreator("Bezeichnung", StringCell.TYPE).createSpec();
    	spec[5] = new DataColumnSpecCreator("Prozessierung", StringCell.TYPE).createSpec();
    	spec[6] = new DataColumnSpecCreator("IntendedUse", StringCell.TYPE).createSpec();
    	spec[7] = new DataColumnSpecCreator("ChargenNr", StringCell.TYPE).createSpec();
    	spec[8] = new DataColumnSpecCreator("MHD", StringCell.TYPE).createSpec();
    	spec[9] = new DataColumnSpecCreator("Herstellungsdatum", StringCell.TYPE).createSpec();
    	spec[10] = new DataColumnSpecCreator("Lieferdatum", StringCell.TYPE).createSpec();
    	spec[11] = new DataColumnSpecCreator("#Units1", DoubleCell.TYPE).createSpec();
    	spec[12] = new DataColumnSpecCreator("BezUnits1", StringCell.TYPE).createSpec();
    	spec[13] = new DataColumnSpecCreator("#Units2", DoubleCell.TYPE).createSpec();
    	spec[14] = new DataColumnSpecCreator("BezUnits2", StringCell.TYPE).createSpec();
    	spec[15] = new DataColumnSpecCreator("Unitmenge", DoubleCell.TYPE).createSpec();
    	spec[16] = new DataColumnSpecCreator("UnitEinheit", StringCell.TYPE).createSpec();
    	spec[17] = new DataColumnSpecCreator("EdgeID", StringCell.TYPE).createSpec();
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
    
    
    private LinkedHashMap<Integer, Integer> applyChargeFilter(Bfrdb db) throws SQLException {
    	LinkedHashMap<Integer, Integer> result = new LinkedHashMap<Integer, Integer>(); 
    	if (!chargeFilter.trim().isEmpty()) {
	    	ResultSet rs = db.pushQuery("SELECT " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + "," +
	    			DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Empfänger") +
	    			" FROM " + DBKernel.delimitL("Chargen") +
	    			" LEFT JOIN " + DBKernel.delimitL("Lieferungen") +
	    			" ON " + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("ID") + "=" + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Charge") +
	    			" WHERE " + getFilterAsSQL(DBKernel.delimitL("ChargenNr"), chargeFilter));
			while (rs.next()) {
				int lieferID = rs.getInt("ID");
				if (lieferID > 0) {
					result.put(lieferID, rs.getInt("Empfänger"));
			    	goForward(db, lieferID, result);
			    	goBackward(db, lieferID, result);
				}
			}
    	}
		return result;
    }
    private LinkedHashMap<Integer, Integer> applyArticleFilter(Bfrdb db) throws SQLException {
    	LinkedHashMap<Integer, Integer> result = new LinkedHashMap<Integer, Integer>(); 
    	if (!artikelFilter.trim().isEmpty()) {
	    	ResultSet rs = db.pushQuery("SELECT " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + "," +
	    			DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Empfänger") +
	    			" FROM " + DBKernel.delimitL("Produktkatalog") +
	    			" LEFT JOIN " + DBKernel.delimitL("Chargen") +
	    			" ON " + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("Artikel") + "=" + DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("ID") +
	    			" LEFT JOIN " + DBKernel.delimitL("Lieferungen") +
	    			" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Charge") + "=" + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("ID") +
	    			" WHERE " + getFilterAsSQL(DBKernel.delimitL("Bezeichnung"), artikelFilter));
			while (rs.next()) {
				int lieferID = rs.getInt("ID");
				if (lieferID > 0) {
					result.put(lieferID, rs.getInt("Empfänger"));
			    	goForward(db, lieferID, result);
			    	goBackward(db, lieferID, result);
				}
			}
    	}
		return result;
    }
    private String getFilterAsSQL(String fieldname, String filter) {
    	String result = "";
    	String[] parts = filter.split(" ", 0);
    	for (int i=0;i<parts.length;i++) {
    		if (!parts[i].trim().isEmpty()) result += " OR UCASE(" + fieldname + ") LIKE '%" + parts[i].toUpperCase() + "%'";
    	}
    	if (!result.isEmpty()) result = result.substring(4);
    	return result;
    }
    private LinkedHashMap<Integer, Integer> applyCompanyFilter(Bfrdb db) throws SQLException {
    	LinkedHashMap<Integer, Integer> result = new LinkedHashMap<Integer, Integer>(); 
    	if (!companyFilter.trim().isEmpty()) {
        	ResultSet rs = db.pushQuery("SELECT " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + "," +
        			DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Empfänger") +
        			" FROM " + DBKernel.delimitL("Station") +
        			" LEFT JOIN " + DBKernel.delimitL("Kontakte") +
        			" ON " + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("ID") + "=" + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("Kontaktadresse") +
        			" LEFT JOIN " + DBKernel.delimitL("Lieferungen") +
        			" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Empfänger") + "=" + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("ID") +
        			" WHERE " + getFilterAsSQL(DBKernel.delimitL("Name"), companyFilter));
    		while (rs.next()) {
    			int lieferID = rs.getInt("ID");
    			if (lieferID > 0) {
    				result.put(lieferID, rs.getInt("Empfänger"));
    		    	goForward(db, lieferID, result);
    		    	goBackward(db, lieferID, result);
    			}
    		}
        	rs = db.pushQuery("SELECT " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") +
        			" FROM " + DBKernel.delimitL("Station") +
        			" LEFT JOIN " + DBKernel.delimitL("Kontakte") +
        			" ON " + DBKernel.delimitL("Kontakte") + "." + DBKernel.delimitL("ID") + "=" + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("Kontaktadresse") +
        			" LEFT JOIN " + DBKernel.delimitL("Produktkatalog") +
        			" ON " + DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("Station") + "=" + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("ID") +
        			" LEFT JOIN " + DBKernel.delimitL("Chargen") +
        			" ON " + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("Artikel") + "=" + DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("ID") +
        			" LEFT JOIN " + DBKernel.delimitL("Lieferungen") +
        			" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Charge") + "=" + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("ID") +
        			" WHERE " + getFilterAsSQL(DBKernel.delimitL("Name"), companyFilter));
    		while (rs.next()) {
    			int lieferID = rs.getInt("ID");
    			if (lieferID > 0) {
    				result.put(lieferID, 0);
    		    	goForward(db, lieferID, result);
    		    	goBackward(db, lieferID, result);				
    			}
    		}
    	}
    	return result;
    }
    
    private HashSet<HashSet<Integer>> makeTracingBack(Bfrdb db, boolean search4Best, int objectType) throws SQLException {
		HashSet<HashSet<Integer>> gemeinsamStationsSet = new HashSet<HashSet<Integer>>();

    	ResultSet rs = db.pushQuery("SELECT " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + "," +
    			DBKernel.delimitL("Station") + "." + DBKernel.delimitL("ID") +
    			" FROM " + DBKernel.delimitL("Station") +
    			" LEFT JOIN " + DBKernel.delimitL("Lieferungen") +
    			" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Empfänger") + "=" + DBKernel.delimitL("Station") + "." + DBKernel.delimitL("ID") +
    			" WHERE " + DBKernel.delimitL("FallErfuellt"));
    	List<LinkedHashMap<Integer, Integer>> allChains = new ArrayList<LinkedHashMap<Integer, Integer>>();
    	LinkedHashMap<Integer, Integer> chain_ = new LinkedHashMap<Integer, Integer>();
    	int oldStationID = 0;
		while (rs.next()) {
			int lieferID = rs.getInt("Lieferungen.ID");
			int stationID = rs.getInt("Station.ID");
			if (lieferID > 0) {
				if (oldStationID != stationID) {
					if (chain_.size() > 0) {
						allChains.add(chain_);
					}
					chain_ = new LinkedHashMap<Integer, Integer>();
					oldStationID = stationID;
				}
				chain_.put(lieferID, stationID);
		    	goBackward(db, lieferID, chain_);
			}
		}
		if (chain_.size() > 0) allChains.add(chain_);

		long lfd = 0;
		long startTime = System.currentTimeMillis();
		HashMap<Integer, Integer> lieferID2ObjectID = new HashMap<Integer, Integer>(); 
		int numCasesHavingCommonNode = allChains.size();
		for (;numCasesHavingCommonNode >= (search4Best ? allChains.size() - 4 : allChains.size());numCasesHavingCommonNode--) {
			CombinatorialIterator<LinkedHashMap<Integer, Integer>> ci = new CombinatorialIterator<LinkedHashMap<Integer, Integer>>(allChains, numCasesHavingCommonNode);
			//CombinationIterator ci = new CombinationIterator(allChains, numCasesHavingCommonNode);
	    	while (ci.hasNext()) {
	    		List<LinkedHashMap<Integer, Integer>> chains2Explore = (List<LinkedHashMap<Integer, Integer>>) ci.next();
	    		
	    		if (lfd % 100000 == 0) {
		    		long ts = ((System.currentTimeMillis() - startTime) / 1000);
	    			System.err.println(numCasesHavingCommonNode + "\t" + lfd + "\t" + ts);
	    		}	    		
	    		lfd++;
	    		HashSet<Integer> caseIDs = new HashSet<Integer>();
	    		HashSet<Integer> gemeinsamObjects = null; // Objects z.B. : STATION = 0, ARTIKEL = 1, CHARGE = 2, LIEFERUNG = 3

        		for (LinkedHashMap<Integer, Integer> chain : chains2Explore) {
        			int caseID = chain.entrySet().iterator().next().getValue();
        			caseIDs.add(caseID);
        			if (caseID == 200) {
        				System.err.print("");
        			}
	        		gemeinsamObjects = calcCommonLieferObjects(db, chain, lieferID2ObjectID, gemeinsamObjects, objectType, false);
        			if (gemeinsamObjects.size() == 0) break;
        		}

        		if (gemeinsamObjects != null && gemeinsamObjects.size() > 0) {
        			HashSet<Integer> diff = getDiff(allChains, caseIDs);
        			for (Integer id : diff) {
        				if (stage.isEmpty()) stage = "\tExceptions: " + id;
        				else stage += "," + id;
        			}
        			gemeinsamStationsSet.add(gemeinsamObjects);
        			//break;
        		}
	    	}
	    	if (gemeinsamStationsSet.size() > 0) {
	    		stage = "\t(" + numCasesHavingCommonNode + " / " + allChains.size() + ")" + stage;
	    		break;
	    	}
		}
		System.err.println("TracingBack - Fin! " + lfd + stage);
		return gemeinsamStationsSet;
    }
    private HashSet<Integer> getDiff(List<LinkedHashMap<Integer, Integer>> allChains, HashSet<Integer> caseIDs) {
    	HashSet<Integer> result = new HashSet<Integer>();
		for (LinkedHashMap<Integer, Integer> chain : allChains) {        			
			Integer caseID = chain.entrySet().iterator().next().getValue();
			if (!caseIDs.contains(caseID)) result.add(caseID);
		}
    	return result;
    }
    private HashSet<Integer> calcCommonLieferObjects(Bfrdb db, LinkedHashMap<Integer, Integer> chain, HashMap<Integer, Integer> lieferID2ObjectID,
    		HashSet<Integer> oldGemeinsamObjects, int objectType, boolean force) throws SQLException {
    	HashSet<Integer> gemeinsamObjects = new HashSet<Integer>();
    	//if (force || !chainMap.containsKey(chain)) {
        	for (Integer lieferID : chain.keySet()) {
        		if (force || !lieferID2ObjectID.containsKey(lieferID)) {
        			String sql = "";
        			if (objectType == STATION) {
        				sql = "SELECT " + DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("Station") + " AS " + DBKernel.delimitL("Object") +
                				" FROM " + DBKernel.delimitL("Lieferungen") +
                				" LEFT JOIN " + DBKernel.delimitL("Chargen") +
                				" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Charge") + "=" + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("ID") +
                				" LEFT JOIN " + DBKernel.delimitL("Produktkatalog") +
                				" ON " + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("Artikel") + "=" + DBKernel.delimitL("Produktkatalog") + "." + DBKernel.delimitL("ID") +
                				" WHERE " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + "=" + lieferID;
        			}
        			else if (objectType == ARTIKEL) {
        				sql = "SELECT " + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("Artikel") + " AS " + DBKernel.delimitL("Object") +
                				" FROM " + DBKernel.delimitL("Lieferungen") +
                				" LEFT JOIN " + DBKernel.delimitL("Chargen") +
                				" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Charge") + "=" + DBKernel.delimitL("Chargen") + "." + DBKernel.delimitL("ID") +
                				" WHERE " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + "=" + lieferID;
        			}
        			else if (objectType == CHARGE) {
        				sql = "SELECT " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Charge") + " AS " + DBKernel.delimitL("Object") +
                				" FROM " + DBKernel.delimitL("Lieferungen") +
                				" WHERE " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + "=" + lieferID;
        			}
        			else {//if (objectType == LIEFERUNG) {
        				sql = "SELECT " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + " AS " + DBKernel.delimitL("Object") +
                				" FROM " + DBKernel.delimitL("Lieferungen") +
                				" WHERE " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + "=" + lieferID;
        			}
            			
    				ResultSet rs = db.pushQuery(sql);
    				//int lfd=0;
    				while (rs.next()) {
    					lieferID2ObjectID.put(lieferID, rs.getInt("Object"));
    					//lfd++;
    					//if (lfd > 1) System.err.println("WWEWE");
    				}
        		}
        		int stationID = lieferID2ObjectID.get(lieferID);
        		if (oldGemeinsamObjects == null) gemeinsamObjects.add(stationID);
        		else if (oldGemeinsamObjects.contains(stationID)) gemeinsamObjects.add(stationID);
        		else if (stationID == 626) {
					//System.err.print(oldGemeinsamStations); // "," + urID
				}
        	}
        //	chainMap.put(chain, result);
    	//}
    	return gemeinsamObjects;
    }
    private void goForward(Bfrdb db, int lieferID, LinkedHashMap<Integer, Integer> results) throws SQLException {
		String sql = "SELECT " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") +
			"," + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Empfänger") +
			" FROM " + DBKernel.delimitL("ChargenVerbindungen") +
			" LEFT JOIN " + DBKernel.delimitL("Lieferungen") +
			" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Charge") + "=" + DBKernel.delimitL("ChargenVerbindungen") + "." + DBKernel.delimitL("Produkt") +
			" WHERE " + DBKernel.delimitL("Zutat") + "=" + lieferID;
		
		ResultSet rs = db.pushQuery(sql);
		while (rs.next()) {
			int newLieferID = rs.getInt("ID");
			if (!results.containsKey(newLieferID)) {
				results.put(newLieferID, rs.getInt("Empfänger"));
				goForward(db, newLieferID, results);
			}
		}
    }
    private void goBackward(Bfrdb db, int lieferID, LinkedHashMap<Integer, Integer> results) throws SQLException {
		String sql = "SELECT " + DBKernel.delimitL("Zutat") + "," + DBKernel.delimitL("Empfänger") +
			" FROM " + DBKernel.delimitL("Lieferungen") +
			" LEFT JOIN " + DBKernel.delimitL("ChargenVerbindungen") +
			" ON " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("Charge") + "=" + DBKernel.delimitL("ChargenVerbindungen") + "." + DBKernel.delimitL("Produkt") +
			" WHERE " + DBKernel.delimitL("Lieferungen") + "." + DBKernel.delimitL("ID") + "=" + lieferID;
	
		ResultSet rs = db.pushQuery(sql);
		while (rs.next()) {
			int newLieferID = rs.getInt("Zutat");
			if (newLieferID > 0 && !results.containsKey(newLieferID)) {
				results.put(newLieferID, rs.getInt("Empfänger"));
				goBackward(db, newLieferID, results);
			}
		}
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
        return new DataTableSpec[]{getSpec33Nodes(), getSpec33Links(), getSpecBurow(), null};
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
    	settings.addBoolean( PARAM_TRACINGBACK, tracingBack );
    	settings.addBoolean( PARAM_ANONYMIZE, doAnonymize );
    	settings.addString( PARAM_FILTER_COMPANY, companyFilter );
    	settings.addString( PARAM_FILTER_CHARGE, chargeFilter );
    	settings.addString( PARAM_FILTER_ARTIKEL, artikelFilter );
    	settings.addBoolean( PARAM_ANTIARTICLE, antiArticle );
    	settings.addBoolean( PARAM_ANTICHARGE, antiCharge );
    	settings.addBoolean( PARAM_ANTICOMPANY, antiCompany );
    	
    	settings.addString(PARAM_FILTER_DATEFROM, dateFrom);
    	settings.addString(PARAM_FILTER_DATETO, dateTo);
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
    	tracingBack = settings.getBoolean( PARAM_TRACINGBACK );
    	doAnonymize = settings.getBoolean( PARAM_ANONYMIZE );
    	companyFilter = settings.getString( PARAM_FILTER_COMPANY );
    	chargeFilter = settings.getString( PARAM_FILTER_CHARGE );
    	artikelFilter = settings.getString( PARAM_FILTER_ARTIKEL );
    	antiArticle = settings.getBoolean( PARAM_ANTIARTICLE );
    	antiCharge = settings.getBoolean( PARAM_ANTICHARGE );
    	antiCompany = settings.getBoolean( PARAM_ANTICOMPANY );
    	
    	dateFrom = settings.getString(PARAM_FILTER_DATEFROM);
    	dateTo = settings.getString(PARAM_FILTER_DATETO);
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

