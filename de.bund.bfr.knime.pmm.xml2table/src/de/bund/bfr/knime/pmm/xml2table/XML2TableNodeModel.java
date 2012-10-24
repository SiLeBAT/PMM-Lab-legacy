package de.bund.bfr.knime.pmm.xml2table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.JDOMException;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
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
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.common.MiscXml;
import de.bund.bfr.knime.pmm.common.ParamXml;
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
	static final String CFGKEY_APPENDDATA = "AppendDataBool";

    private final SettingsModelString m_col = new SettingsModelString(XML2TableNodeModel.CFGKEY_COLNAME, "xml");
    private final SettingsModelBoolean m_append = new SettingsModelBoolean(XML2TableNodeModel.CFGKEY_APPENDDATA, true);
        

    /**
     * Constructor for the node model.
     */
    protected XML2TableNodeModel() {
        super(1, 1);
    }

    private DataTableSpec getOutSpec(BufferedDataTable data, String selColumn) throws IOException, JDOMException {
    	DataTableSpec inSpec = data.getDataTableSpec();
    	DataColumnSpec[] oldColSpecs = new DataColumnSpec[inSpec.getNumColumns()];
    	DataColumnSpec[] addColSpecs = null;
    	for (int i = 0; i < inSpec.getNumColumns(); i++) {
            DataColumnSpec cspec = inSpec.getColumnSpec(i);
    		oldColSpecs[i] = cspec;
            String colName = cspec.getName();
            if (colName.equals(selColumn) && cspec.getType().equals(XMLCell.TYPE)) {
                for (RowIterator it = data.iterator(); it.hasNext();) {
                    DataRow row = it.next();
                    DataCell cell = row.getCell(i);
                    String xml = ((XMLCell) cell).getStringValue();
                	PmmXmlDoc doc = new PmmXmlDoc(xml);
                	for (PmmXmlElementConvertable el : doc.getElementSet()) {
                		if (el instanceof MiscXml) {
		                	addColSpecs = new DataColumnSpec[5];
		                	addColSpecs[0] = new DataColumnSpecCreator(selColumn+"_ID", IntCell.TYPE).createSpec();
		                	addColSpecs[1] = new DataColumnSpecCreator(selColumn+"_Name", StringCell.TYPE).createSpec();
		                	addColSpecs[2] = new DataColumnSpecCreator(selColumn+"_Description", StringCell.TYPE).createSpec();
		                	addColSpecs[3] = new DataColumnSpecCreator(selColumn+"_Value", DoubleCell.TYPE).createSpec();
		                	addColSpecs[4] = new DataColumnSpecCreator(selColumn+"_Unit", StringCell.TYPE).createSpec();
                		}
                		else if (el instanceof ParamXml) {
		                    addColSpecs = new DataColumnSpec[7];
		                    addColSpecs[0] = new DataColumnSpecCreator(selColumn+"_Name", StringCell.TYPE).createSpec();
		                    addColSpecs[1] = new DataColumnSpecCreator(selColumn+"_Value", DoubleCell.TYPE).createSpec();
		                    addColSpecs[2] = new DataColumnSpecCreator(selColumn+"_Error", DoubleCell.TYPE).createSpec();
		                    addColSpecs[3] = new DataColumnSpecCreator(selColumn+"_Min", DoubleCell.TYPE).createSpec();
		                    addColSpecs[4] = new DataColumnSpecCreator(selColumn+"_Max", DoubleCell.TYPE).createSpec();
		                    addColSpecs[5] = new DataColumnSpecCreator(selColumn+"_P-value", DoubleCell.TYPE).createSpec();
		                    addColSpecs[6] = new DataColumnSpecCreator(selColumn+"_t-value", DoubleCell.TYPE).createSpec();
                		}
                	}
                }
            }
        }
    	DataColumnSpec[] fullColSpecs;
    	if (m_append.getBooleanValue()) {
        	fullColSpecs = new DataColumnSpec[oldColSpecs.length + addColSpecs.length];
        	for (int i=0;i<oldColSpecs.length;i++) {
        		fullColSpecs[i] = oldColSpecs[i];
        	}
        	for (int i=0;i<addColSpecs.length;i++) {
        		fullColSpecs[i+oldColSpecs.length] = addColSpecs[i];
        	}
    	}
    	else {
        	fullColSpecs = new DataColumnSpec[addColSpecs.length];
        	for (int i=0;i<addColSpecs.length;i++) {
        		fullColSpecs[i] = addColSpecs[i];
        	}    		
    	}
    	return new DataTableSpec(fullColSpecs);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	BufferedDataTable data = inData[0];
    	if (data != null) {
    		DataTableSpec outputSpec = getOutSpec(data, m_col.getStringValue());
            DataTableSpec spec = data.getDataTableSpec();
            DataCell[] cells = new DataCell[outputSpec.getNumColumns()];
        	int count = 0;
        	BufferedDataContainer container = exec.createDataContainer(outputSpec);
            for (RowIterator it = data.iterator(); it.hasNext();count++) {
            	List<DataCell[]> v = new ArrayList<DataCell[]>();
                DataRow row = it.next();
            	for (int i = 0; i < spec.getNumColumns(); i++) {
                    DataCell cell = row.getCell(i);
                    if (m_append.getBooleanValue()) cells[i] = cell;
	                DataColumnSpec cspec = spec.getColumnSpec(i);
	                String colName = cspec.getName();
	                if (colName.equals(m_col.getStringValue()) && cspec.getType().equals(XMLCell.TYPE)) {
	                    String xml = ((XMLCell) cell).getStringValue();
	                	PmmXmlDoc doc = new PmmXmlDoc(xml);
	                	for (PmmXmlElementConvertable el : doc.getElementSet()) {
	                		if (el instanceof MiscXml) {
	                			MiscXml mx = (MiscXml) el;
	                			DataCell[] addCells = new DataCell[5];
	                			addCells[0] = new IntCell(mx.getID()); 
	                			addCells[1] = new StringCell(mx.getName()); 
	                			addCells[2] = new StringCell(mx.getDescription());
	                			addCells[3] = new DoubleCell(mx.getValue());
	                			addCells[4] = new StringCell(mx.getUnit());
	                			v.add(addCells);
	                		}
	                		else if (el instanceof ParamXml) {
	                			ParamXml px = (ParamXml) el;
	                			DataCell[] addCells = new DataCell[7];
	                			addCells[0] = new StringCell(px.getName()); 
	                			addCells[1] = new DoubleCell(px.getValue()); 
	                			addCells[2] = new DoubleCell(px.getError());
	                			addCells[3] = new DoubleCell(px.getMin());
	                			addCells[4] = new DoubleCell(px.getMax());
	                			addCells[5] = new DoubleCell(px.getP());
	                			addCells[6] = new DoubleCell(px.gett());
	                			v.add(addCells);
	                		}
	                	}
                    }
                }
            	int countResult = 0;
            	for (DataCell[] addCells : v) {
                    RowKey key = new RowKey(row.getKey().getString() + "." + countResult);
                    for (int i=0;i<addCells.length;i++) {
                    	if (m_append.getBooleanValue()) cells[i+spec.getNumColumns()] = addCells[i];
                    	else cells[i] = addCells[i];
                    }
                    container.addRowToTable(new DefaultRow(key, cells));
                    countResult++;
            	}

                exec.checkCanceled();
                exec.setProgress(count / (double)data.getRowCount(), "Adding row " + count);
            }
            if (container == null) return null;
            container.close();
            BufferedDataTable out = container.getTable();
            return new BufferedDataTable[]{out};
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
    	m_append.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	m_col.loadSettingsFrom(settings);
    	m_append.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	m_col.validateSettings(settings);
    	m_append.validateSettings(settings);
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

