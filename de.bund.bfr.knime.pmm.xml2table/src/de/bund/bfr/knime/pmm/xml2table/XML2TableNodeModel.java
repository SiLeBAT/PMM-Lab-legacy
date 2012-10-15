package de.bund.bfr.knime.pmm.xml2table;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.IntValue;
import org.knime.core.data.RowIterator;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.xml.XMLCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParametricModel;
import de.bund.bfr.knime.pmm.common.PmmXmlDoc;
import de.bund.bfr.knime.pmm.common.PmmXmlElementConvertable;


/**
 * This is the model implementation of XML2Table.
 * 
 *
 * @author BfR
 */
public class XML2TableNodeModel extends NodeModel {
    
	static final String CFGKEY_COLNAME = "SelectedColumn";

    private final SettingsModelString m_col = new SettingsModelString(XML2TableNodeModel.CFGKEY_COLNAME, "xml");
    

    /**
     * Constructor for the node model.
     */
    protected XML2TableNodeModel() {
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	BufferedDataTable data = inData[0];
    	if (data != null) {
            DataTableSpec spec = data.getDataTableSpec();
            for (int i = 0; i < spec.getNumColumns(); i++) {
                DataColumnSpec cspec = spec.getColumnSpec(i);
                String colName = cspec.getName();
                if (colName.equals(m_col.getStringValue()) && cspec.getType().equals(XMLCell.TYPE)) {
                	int count = -1;
                	BufferedDataContainer container = null;
                    for (RowIterator it = data.iterator(); it.hasNext(); count++) {
	                    DataRow row = it.next();
	                    DataCell cell = row.getCell(i);
	                    String xml = ((XMLCell) cell).getStringValue();
	                	PmmXmlDoc doc = new PmmXmlDoc(xml);
	                	int countResult = 0;
	                	for (PmmXmlElementConvertable el : doc.getElementSet()) {
	                		if (el instanceof MiscXml) {		
	                			MiscXml mx = (MiscXml) el;
	    	                    if (count < 0) {
	    		                    DataColumnSpec[] allColSpecs = new DataColumnSpec[5];
	    		                    allColSpecs[0] = new DataColumnSpecCreator("ID", IntCell.TYPE).createSpec();
	    		                    allColSpecs[1] = new DataColumnSpecCreator("Name", StringCell.TYPE).createSpec();
	    		                    allColSpecs[2] = new DataColumnSpecCreator("Description", StringCell.TYPE).createSpec();
	    		                    allColSpecs[3] = new DataColumnSpecCreator("Value", DoubleCell.TYPE).createSpec();
	    		                    allColSpecs[4] = new DataColumnSpecCreator("Unit", StringCell.TYPE).createSpec();
	    		                    DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
	    		                    container = exec.createDataContainer(outputSpec);
	    		                    count = 0;
	    	                    }	                		                	
	                            RowKey key = new RowKey("Row " + count + "." + countResult);
	                            DataCell[] cells = new DataCell[5];
	                            cells[0] = new IntCell(mx.getID()); 
	                            cells[1] = new StringCell(mx.getName()); 
	                            cells[2] = new StringCell(mx.getDescription());
	                            cells[3] = new DoubleCell(mx.getValue());
	                            cells[4] = new StringCell(mx.getUnit());
	                            container.addRowToTable(new DefaultRow(key, cells));
	                            countResult++;

	                            exec.checkCanceled();
	                            exec.setProgress(count / (double)data.getRowCount(), "Adding row " + count);
	                		}
	                	}
                    }
                    if (container == null) return null;
                    container.close();
                    BufferedDataTable out = container.getTable();
                    return new BufferedDataTable[]{out};
                }
            }
    	}
    	return null;
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
    	// Spec at output is not known in advance. It depends on the XML.
    	return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
    	m_col.saveSettingsTo(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	m_col.loadSettingsFrom(settings);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	m_col.validateSettings(settings);

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

