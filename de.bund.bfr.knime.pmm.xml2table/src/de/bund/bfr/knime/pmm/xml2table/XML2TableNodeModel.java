package de.bund.bfr.knime.pmm.xml2table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import de.bund.bfr.knime.pmm.common.CellIO;
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
    
	private static final String CFGKEY_COLNAME = "SelectedColumn";
	private static final String CFGKEY_APPENDDATA = "AppendDataBool";
	private static final String CFGKEY_SELXMLENTRY = "SelectXMLEntry";

    final static SettingsModelString m_col = new SettingsModelString(XML2TableNodeModel.CFGKEY_COLNAME, "");
    final static SettingsModelBoolean m_append = new SettingsModelBoolean(XML2TableNodeModel.CFGKEY_APPENDDATA, true);
    final static SettingsModelStringArray m_xmlsel = new SettingsModelStringArray(XML2TableNodeModel.CFGKEY_SELXMLENTRY, null);
        

    /**
     * Constructor for the node model.
     */
    protected XML2TableNodeModel() {
        super(1, 1);
    }

    private DataTableSpec getOutSpec(BufferedDataTable data, String selColumn) throws IOException, JDOMException {
    	DataTableSpec inSpec = data.getDataTableSpec();
    	DataColumnSpec[] oldColSpecs = new DataColumnSpec[inSpec.getNumColumns()];
    	LinkedHashMap<String, DataColumnSpec> addColSpecs = new LinkedHashMap<String, DataColumnSpec>();
    	for (int i = 0; i < inSpec.getNumColumns(); i++) {
            DataColumnSpec cspec = inSpec.getColumnSpec(i);
    		oldColSpecs[i] = cspec;
            String colName = cspec.getName();
            if (colName.equals(selColumn) && cspec.getType().equals(XMLCell.TYPE)) {
                for (RowIterator it = data.iterator(); it.hasNext();) {
                    DataRow row = it.next();
                    DataCell cell = row.getCell(i);
                    if (!cell.isMissing()) {
                        String xml = ((XMLCell) cell).getStringValue();
                    	PmmXmlDoc doc = new PmmXmlDoc(xml);
                    	for (PmmXmlElementConvertable el : doc.getElementSet()) {
                    		if (el instanceof MiscXml) {
                    			MiscXml mx = (MiscXml) el;
                    			String[] sarr = m_xmlsel.getStringArrayValue();
                    			if (sarr != null && sarr.length > 0) {
                    				for (int j=0;j<sarr.length;j++) {
                    					if (!addColSpecs.containsKey(selColumn+"_"+mx.getName()+"_"+sarr[j]))
                    						addColSpecs.put(selColumn+"_"+mx.getName()+"_"+sarr[j],
                    								new DataColumnSpecCreator(selColumn+"_"+mx.getName()+"_"+sarr[j], MiscXml.getDataType(sarr[j])).createSpec());                				                					
                    				}
                    			}
                    			else {
        		                	addColSpecs.put(selColumn+"_ID", new DataColumnSpecCreator(selColumn+"_ID", IntCell.TYPE).createSpec());
        		                	addColSpecs.put(selColumn+"_Name", new DataColumnSpecCreator(selColumn+"_Name", StringCell.TYPE).createSpec());
        		                	addColSpecs.put(selColumn+"_Description", new DataColumnSpecCreator(selColumn+"_Description", StringCell.TYPE).createSpec());
        		                	addColSpecs.put(selColumn+"_Value", new DataColumnSpecCreator(selColumn+"_Value", DoubleCell.TYPE).createSpec());
        		                	addColSpecs.put(selColumn+"_Unit", new DataColumnSpecCreator(selColumn+"_Unit", StringCell.TYPE).createSpec());                				
        		                	break;
                    			}
                    		}
                    		else if (el instanceof ParamXml) {
                    			ParamXml px = (ParamXml) el;
                    			String[] sarr = m_xmlsel.getStringArrayValue();
                    			if (sarr != null && sarr.length > 0) {
                    				for (int j=0;j<sarr.length;j++) {
                    					if (!addColSpecs.containsKey(selColumn+"_"+px.getName()+"_"+sarr[j]))
                    						addColSpecs.put(selColumn+"_"+px.getName()+"_"+sarr[j],
                    								new DataColumnSpecCreator(selColumn+"_"+px.getName()+"_"+sarr[j], ParamXml.getDataType(sarr[j])).createSpec());                				                					
                    				}
                    			}
                    			else {
                    				addColSpecs.put(selColumn+"_Name", new DataColumnSpecCreator(selColumn+"_Name", StringCell.TYPE).createSpec());
    	                			addColSpecs.put(selColumn+"_Value", new DataColumnSpecCreator(selColumn+"_Value", DoubleCell.TYPE).createSpec());
    	                			addColSpecs.put(selColumn+"_Error", new DataColumnSpecCreator(selColumn+"_Error", DoubleCell.TYPE).createSpec());
    	                			addColSpecs.put(selColumn+"_Min", new DataColumnSpecCreator(selColumn+"_Min", DoubleCell.TYPE).createSpec());
    	                			addColSpecs.put(selColumn+"_Max", new DataColumnSpecCreator(selColumn+"_Max", DoubleCell.TYPE).createSpec());
    	                			addColSpecs.put(selColumn+"_P", new DataColumnSpecCreator(selColumn+"_P", DoubleCell.TYPE).createSpec());
    	                			addColSpecs.put(selColumn+"_t", new DataColumnSpecCreator(selColumn+"_t", DoubleCell.TYPE).createSpec());
    		                        break;
                    			}
                    		}
                    	}
                    }
                }
            }
        }
    	DataColumnSpec[] fullColSpecs;
    	if (m_append.getBooleanValue()) {
        	fullColSpecs = new DataColumnSpec[oldColSpecs.length + addColSpecs.size()];
        	for (int i=0;i<oldColSpecs.length;i++) {
        		fullColSpecs[i] = oldColSpecs[i];
        	}
        	int i=0;
        	for (DataColumnSpec colSpec : addColSpecs.values()) {
        		fullColSpecs[i+oldColSpecs.length] = colSpec;
        		i++;
        	}
    	}
    	else {
        	fullColSpecs = new DataColumnSpec[addColSpecs.size()];
        	int i=0;
        	for (DataColumnSpec colSpec : addColSpecs.values()) {
        		fullColSpecs[i] = colSpec;
        		i++;
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
    		DataTableSpec outSpec = getOutSpec(data, m_col.getStringValue());
            DataTableSpec inSpec = data.getDataTableSpec();
            DataCell[] cells = new DataCell[outSpec.getNumColumns()];
        	int count = 0;
        	BufferedDataContainer container = exec.createDataContainer(outSpec);
            for (RowIterator it = data.iterator(); it.hasNext();count++) {
            	List<LinkedHashMap<String, DataCell>> v = new ArrayList<LinkedHashMap<String, DataCell>>();
                DataRow row = it.next();
            	for (int i = 0; i < inSpec.getNumColumns(); i++) {
                    DataCell cell = row.getCell(i);
                    if (m_append.getBooleanValue()) cells[i] = cell;
	                DataColumnSpec cspec = inSpec.getColumnSpec(i);
	                String colName = cspec.getName();
	                if (colName.equals(m_col.getStringValue()) && cspec.getType().equals(XMLCell.TYPE)) {
	                	if (!cell.isMissing()) {
		                    String xml = ((XMLCell) cell).getStringValue();
		                	PmmXmlDoc doc = new PmmXmlDoc(xml);
		                	for (PmmXmlElementConvertable el : doc.getElementSet()) {
		                		LinkedHashMap<String, DataCell> addCells = new LinkedHashMap<String, DataCell>();
		                		if (el instanceof MiscXml) {
		                			MiscXml mx = (MiscXml) el;
		                			addCells.put("id", mx.getID() == null ? CellIO.createMissingCell() : new IntCell(mx.getID())); 
		                			addCells.put("name", mx.getName() == null ? CellIO.createMissingCell() : new StringCell(mx.getName())); 
		                			addCells.put("description", mx.getDescription() == null ? CellIO.createMissingCell() : new StringCell(mx.getDescription()));
		                			addCells.put("value", mx.getValue() == null ? CellIO.createMissingCell() : new DoubleCell(mx.getValue()));
		                			addCells.put("unit", mx.getUnit() == null ? CellIO.createMissingCell() : new StringCell(mx.getUnit()));
		                			v.add(addCells);
		                		}
		                		else if (el instanceof ParamXml) {
		                			ParamXml px = (ParamXml) el;
		                			addCells.put("name", px.getName() == null ? CellIO.createMissingCell() : new StringCell(px.getName())); 
		                			addCells.put("value", px.getValue() == null ? CellIO.createMissingCell() : new DoubleCell(px.getValue())); 
		                			addCells.put("error", px.getError() == null ? CellIO.createMissingCell() : new DoubleCell(px.getError()));
		                			addCells.put("min", px.getMin() == null ? CellIO.createMissingCell() : new DoubleCell(px.getMin()));
		                			addCells.put("max", px.getMax() == null ? CellIO.createMissingCell() : new DoubleCell(px.getMax()));
		                			addCells.put("p", px.getP() == null ? CellIO.createMissingCell() : new DoubleCell(px.getP()));
		                			addCells.put("t", px.gett() == null ? CellIO.createMissingCell() : new DoubleCell(px.gett()));
		                			v.add(addCells);
		                		}
		                	}
	                	}
                    }
                }
    			String[] sarr = m_xmlsel.getStringArrayValue();
    			if (sarr != null && sarr.length > 0) {
                	if (m_append.getBooleanValue()) {
                		for (int k=0;k<outSpec.getNumColumns()-inSpec.getNumColumns();k++) {
                			cells[k+inSpec.getNumColumns()] = CellIO.createMissingCell();
                		}
                    	for (LinkedHashMap<String, DataCell> addCells : v) {
            				for (int j=0;j<sarr.length;j++) {
            					for (int k=0;k<outSpec.getNumColumns()-inSpec.getNumColumns();k++) {
            						if (outSpec.getColumnNames()[k+inSpec.getNumColumns()].equalsIgnoreCase(
            								m_col.getStringValue()+"_"+addCells.get("name")+"_"+sarr[j])) {
            							cells[k+inSpec.getNumColumns()] = addCells.get(sarr[j].toLowerCase());
            							break;
            						}
            					}
            				}
                    	}
                        container.addRowToTable(new DefaultRow(row.getKey(), cells));
                	}
    			}
    			else {
                	int countResult = 0;
                	for (LinkedHashMap<String, DataCell> addCells : v) {
                		int i=0;
                        for (DataCell dataCell : addCells.values()) {
                        	if (m_append.getBooleanValue()) {
                            	cells[i+inSpec.getNumColumns()] = dataCell;
                        	}
                        	else {
                        		cells[i] = dataCell;
                        	}
                        	i++;
                        }
                        RowKey key = new RowKey(row.getKey().getString() + "." + countResult);
                        container.addRowToTable(new DefaultRow(key, cells));
                        countResult++;
                	}
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
    	m_xmlsel.saveSettingsTo(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	m_col.loadSettingsFrom(settings);
    	m_append.loadSettingsFrom(settings);
    	m_xmlsel.loadSettingsFrom(settings);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
    	m_col.validateSettings(settings);
    	m_append.validateSettings(settings);
    	m_xmlsel.validateSettings(settings);
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

