package de.bund.bfr.knime.pmm.dbdelete;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import org.hsh.bfr.db.DBKernel;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
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

import de.bund.bfr.knime.pmm.bfrdbiface.lib.Bfrdb;
import de.bund.bfr.knime.pmm.common.CellIO;
import de.bund.bfr.knime.pmm.common.EstModelXml;
import de.bund.bfr.knime.pmm.common.PmmException;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model1Schema;
import de.bund.bfr.knime.pmm.common.pmmtablemodel.Model2Schema;

/**
 * This is the model implementation of DatabaseDelete.
 * 
 *
 * @author Armin A. Weiser
 */
public class DatabaseDeleteNodeModel extends NodeModel {
    
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
    protected DatabaseDeleteNodeModel() {
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	Bfrdb db = null;
    	if( override ) {
			db = new Bfrdb( filename, login, passwd );
		} else {
			db = new Bfrdb(DBKernel.getLocalConn(true));
		}
    	String dbuuid = db.getDBUUID();
    	Connection conn = db.getConnection();
    	conn.setReadOnly(false);
		String warnings = "";

		DataTableSpec outSpec = getOutSpec(inData[0].getDataTableSpec());
		BufferedDataContainer container = exec.createDataContainer(outSpec);
		DataCell[] cells = new DataCell[outSpec.getNumColumns()];
		
    	boolean m1Conform = new Model1Schema().conforms(inData[0].getDataTableSpec());
    	boolean m2Conform = new Model2Schema().conforms(inData[0].getDataTableSpec());
    	for (DataRow row : inData[0]) {
			for (int ii=0;ii<row.getNumCells();ii++) {
				cells[outSpec.findColumnIndex(outSpec.getColumnNames()[ii])] = row.getCell(outSpec.findColumnIndex(outSpec.getColumnNames()[ii]));
			}

			int numDBSuccesses = getNumDBSuccesses(m1Conform, 1, dbuuid, row, outSpec, conn);
			numDBSuccesses += getNumDBSuccesses(m2Conform, 2, dbuuid, row, outSpec, conn);
			
			cells[cells.length-1] = new IntCell(numDBSuccesses);
			container.addRowToTable(new DefaultRow(row.getKey(), cells));
		}
    			    			
    	if (!warnings.isEmpty()) {
			this.setWarningMessage(warnings.trim());
		}			
    	conn.setReadOnly(DBKernel.prefs.getBoolean("PMM_LAB_SETTINGS_DB_RO", true));
    	db.close();

		container.close();
		return new BufferedDataTable[] {container.getTable()};
    }
    private int getNumDBSuccesses(boolean conform, int level, String dbuuid, DataRow row, DataTableSpec outSpec, Connection conn) {
    	int numDBSuccesses = 0;
		if (conform) {
			try {
				if (dbuuid.equals(row.getCell(outSpec.findColumnIndex(level == 1 ? Model1Schema.ATT_DBUUID : Model2Schema.ATT_DBUUID)).toString())) {
					DataCell dc = row.getCell(outSpec.findColumnIndex(level == 1 ? Model1Schema.ATT_ESTMODEL : Model2Schema.ATT_ESTMODEL));
					if (dc instanceof StringValue || dc.isMissing()) {
						PmmXmlDoc estModel = CellIO.getPmmXml(dc);
						if (estModel != null) {
							EstModelXml emx = null;
							for (PmmXmlElementConvertable el : estModel.getElementSet()) {
								if (el instanceof EstModelXml) {
									emx = (EstModelXml) el;
									break;
								}
							}
							Integer rowEstMID = emx.getID();
							numDBSuccesses += DBKernel.sendRequestGetAffectedRowNumber(conn, "DELETE FROM " + DBKernel.delimitL("VarParMaps") + " WHERE " + DBKernel.delimitL("GeschaetztesModell") + "=" + rowEstMID, false, false);
							numDBSuccesses += DBKernel.sendRequestGetAffectedRowNumber(conn, "DELETE FROM " + DBKernel.delimitL("GeschaetztesModell_Referenz") + " WHERE " + DBKernel.delimitL("GeschaetztesModell") + "=" + rowEstMID, false, false);
							numDBSuccesses += DBKernel.sendRequestGetAffectedRowNumber(conn, "DELETE FROM " + DBKernel.delimitL("GeschaetzteParameterCovCor") + " WHERE " + DBKernel.delimitL("GeschaetztesModell") + "=" + rowEstMID, false, false);
							numDBSuccesses += DBKernel.sendRequestGetAffectedRowNumber(conn, "DELETE FROM " + DBKernel.delimitL("GueltigkeitsBereiche") + " WHERE " + DBKernel.delimitL("GeschaetztesModell") + "=" + rowEstMID, false, false);
							numDBSuccesses += DBKernel.sendRequestGetAffectedRowNumber(conn, "DELETE FROM " + DBKernel.delimitL("GeschaetzteParameter") + " WHERE " + DBKernel.delimitL("GeschaetztesModell") + "=" + rowEstMID, false, false);
							numDBSuccesses += DBKernel.sendRequestGetAffectedRowNumber(conn, "DELETE FROM " + DBKernel.delimitL("Sekundaermodelle_Primaermodelle") + " WHERE " + DBKernel.delimitL("GeschaetztesPrimaermodell") + "=" + rowEstMID, false, false);
							numDBSuccesses += DBKernel.sendRequestGetAffectedRowNumber(conn, "DELETE FROM " + DBKernel.delimitL("Sekundaermodelle_Primaermodelle") + " WHERE " + DBKernel.delimitL("GeschaetztesSekundaermodell") + "=" + rowEstMID, false, false);
							numDBSuccesses += DBKernel.sendRequestGetAffectedRowNumber(conn, "DELETE FROM " + DBKernel.delimitL("GeschaetzteModelle") + " WHERE " + DBKernel.delimitL("ID") + "=" + rowEstMID, false, false);
						}
					}
				}					
			}
			catch (PmmException e) {e.printStackTrace();}
		}    	
		return numDBSuccesses;
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
        return new DataTableSpec[]{getOutSpec(inSpecs[0])};
    }
    private DataTableSpec getOutSpec(DataTableSpec inSpec) {
    	DataColumnSpec[] oldSpecs = new DataColumnSpec[inSpec.getNumColumns() + 1];
		for (int i=0;i<oldSpecs.length-1;i++) {
			oldSpecs[i] = inSpec.getColumnSpec(i);
		}
		DataColumnSpec resultSpec = new DataColumnSpecCreator("DBResult", IntCell.TYPE).createSpec();
		oldSpecs[oldSpecs.length-1] = resultSpec;
		return new DataTableSpec(oldSpecs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	settings.addString(PARAM_FILENAME, filename);
    	settings.addString(PARAM_LOGIN, login);
    	settings.addString(PARAM_PASSWD, passwd);
    	settings.addBoolean(PARAM_OVERRIDE, override);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	filename = settings.getString(PARAM_FILENAME);
    	login = settings.getString(PARAM_LOGIN);
    	passwd = settings.getString(PARAM_PASSWD);
    	override = settings.getBoolean(PARAM_OVERRIDE);
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

