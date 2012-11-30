package de.bund.bfr.knime.krise;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.LinkedHashMap;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
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
        super(0, 2);
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
    	db.close();
        return new BufferedDataTable[]{output33Nodes.getTable(), output33Links.getTable()};
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
        return new DataTableSpec[]{getSpec33Nodes(), getSpec33Links()};
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

